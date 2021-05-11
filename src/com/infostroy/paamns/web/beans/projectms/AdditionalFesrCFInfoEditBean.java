package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrCFInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

public class AdditionalFesrCFInfoEditBean  extends EntityProjectEditBean<AdditionalFesrCFInfo>
{
	
	private List<SelectItem> listPartners;
	
	private List<SelectItem> transferTypes;
	
	private Double totalDDR;
	
	//QUERY TO GET SUMM OF STC_CERTIFICATION FROM COST DEFINITIONS BY PROJECT AND CFPARTNERANAGRAPH
	private final String QUERY1 = "SELECT SUM(agu_certification) FROM cost_definitions where project_id=:project_id AND user_id=(SELECT user_id from cf_partner_anagraphs where id =:partner_id)";
	
	//QUERY TO GET SUMM OF TRANSFER IMPORT FOR CURRENT PROJECT AND CURRENT PARTNER
	private final String QUERY2="SELECT SUM(AFI.TRANSFER_IMPORT) FROM ADDITIONAL_FESR_CF_INFO AFI WHERE AFI.project_id=:project_id and fesr_bf_id=:fesr_bf_id and add_user_role_id=:add_user_role_id";
	
	//QUERY TO GET SUMM OF TRANSFER IMPORT FOR CURRENT PROJECT AND CURRENT PARTNER AND TRANSFERTYPE CODE
	private final String QUERY3="SELECT SUM(AFI.TRANSFER_IMPORT) FROM ADDITIONAL_FESR_CF_INFO AFI WHERE AFI.project_id=:project_id and fesr_bf_id=:fesr_bf_id and transfer_type_code=:code and add_user_role_id=:add_user_role_id and user_id=:user_id";
	
	
	private Documents							document;
	
	private UploadedFile						documentUpFile;
	
	private List<SelectItem>					listSelectRoles;
	
	private List<SelectItem>					listPartnersDoc;
	
	private List<SelectItem>					categories;
	
	private String								selectedCategory;
	
	private String								docTitle;
	
	private Roles								userRole;
	
	private String 								axis;
	
	private List<SelectItem>					axisList;
	
	
	
	
	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave()
	{

		this.GoBack();
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack()
	{
		this.getSession().remove("selectedPartner");
		this.getSession().remove("transferType");
		this.getSession().remove("additionalFesrInfoCFEdit");
		this.goTo(PagesTypes.ADDITIONALFESRICFNFOLIST);
		
	}
	
	private void fillAxis() throws HibernateException, PersistenceBeanException {
		this.axisList = new ArrayList<>();
		
		SelectItem item = new SelectItem();
		this.axisList.add(SelectItemHelper.getFirst());
		//for (int i=1; i<=4; i++) {
		for (int i=1; i<=3; i++) {
			item = new SelectItem();
			item.setValue(String.valueOf(i));
			item.setLabel(String.valueOf(i));
			this.axisList.add(item);
		}
		if(this.getViewState().get("axis")!=null){
			this.setAxis((String)this.getViewState().get("axis"));
		}
		else if(this.getEntity().getAxis()!=null && this.getViewState().get("axis")==null){
			this.setAxis(this.getEntity().getAxis());
		}
		else if (this.axis == null) {
			if (!this.axisList.isEmpty()) {
				this.setAxis(this.axisList.get(0).getValue().toString());

			}
		}

	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, IOException, NullPointerException
	{
		if(this.getSession().get("additionalFesrInfoCFEdit")!=null){
			this.setEntity(BeansFactory.AdditionalFESRCFInfo().Load((Long)this.getSession().get("additionalFesrInfoCFEdit")));
		}
		else{
			this.setEntity(new AdditionalFesrCFInfo());
		}
		
		if (this.getSessionBean().isAGU())
		{
			this.setUserRole(
					BeansFactory.Roles().GetRoleByName(UserRoleTypes.AGU));
		}
		else if (this.getSessionBean().isCF())
		{
			this.setUserRole(
					BeansFactory.Roles().GetRoleByName(UserRoleTypes.BP));
		}
		
		this.fillPartners();
		this.fillTransferTypes();
		this.recalculateDDR();
		this.recalculateTotalTransfer();
		this.recalculateTotalAdvance();
		this.recalculateaTotaleAdvanceStateAidExemptionScheme();
		this.recalculateaTotaleAdvanceStateAidDeMinimis();
		this.recalculateTotalRepayments();
		this.fillRoles();
		this.fillCategories();
		this.fillDocuments();
		this.fillAxis();
	}
	
	private void fillDocuments(){
		
		document = new Documents();
		document.setEditableDate(new Date());
		if (this.getViewState().get("Document") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document");

			setDocTitle(docinfo.getName());
			document.setTitle(FileHelper.getFileNameWOExtension(docinfo
					.getName()));
			document.setDocumentDate(docinfo.getDate());
		}
		else if (this.getEntity().getDocument() != null)
		{
			setDocTitle(this.getEntity().getDocument().getName());
			document
					.setTitle(this.getEntity().getDocument().getTitle());
			document.setDocumentDate(this.getEntity().getDocument()
					.getDocumentDate());
		}
		else
		{
			document.setDocumentDate(new Date());
			setDocTitle("");
		}
	}
	
	public void fillTransferTypes(){
		if(this.getTransferTypes()==null){
			this.setTransferTypes(new ArrayList<SelectItem>());
		}else{
			this.getTransferTypes().clear();
		}
		this.getTransferTypes().add(SelectItemHelper.getFirst());
		for(TransferTypes type : TransferTypes.values()){
			this.getTransferTypes().add(new SelectItem(type.getCode(), type.toString()));
		}
		if(this.getEntity().getTransferTypeCode()!=null){
			this.getSession().put("transferType", this.getEntity().getTransferTypeCode());
		}
		
	}
		
	public void fillPartners(){
		
		try
		{
			List<CFPartnerAnagraphs> partners = null;
			if(this.getSessionBean().isAGU()){
				partners =BeansFactory.CFPartnerAnagraphs().GetCFAnagraphForProject(this.getProjectId(), null);
			}
			else if(this.getSessionBean().isCF()){
				partners =BeansFactory.CFPartnerAnagraphs().GetCFAnagraphForProject(this.getProjectId(), CFAnagraphTypes.PartnerAnagraph);
			}
			if(this.getListPartners()==null){
				this.setListPartners(new ArrayList<SelectItem>());
			}else{
				this.getListPartners().clear();
			}
			this.getListPartners().add(SelectItemHelper.getFirst());
			for(CFPartnerAnagraphs partner : partners){
				this.getListPartners().add(new SelectItem(partner.getId(), partner.getNaming()));
			}
			if(this.getEntity().getFesrBf()!=null){
				this.getSession().put("selectedPartner", this.getEntity().getFesrBf().getId());
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}
	
	@Override
	public Boolean BeforeSave(){
		boolean validation = true;
		this.setDocInvalid(Boolean.FALSE);
		if(this.getSessionBean().isCF()){
			if(this.getDocTitle()==null || this.getDocTitle().isEmpty() || this.getAxis().trim().isEmpty() || this.getAxis().trim().equals("0")){
				validation = false;
				this.setDocInvalid(Boolean.TRUE);
			}
		}
		
		return validation;
		
	}
	
	public void recalculateDDR(){
		if(this.getPartner()!=null){
			
	        SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
				        .createSQLQuery(QUERY1);
		        query.setParameter("project_id", this.getProjectId());
		        query.setParameter("partner_id", this.getPartner());
		        if(query.uniqueResult()!=null){
		        	this.getEntity().setTotalDDR((Double)query.uniqueResult());	
		        }
		        else{
		        	this.getEntity().setTotalDDR(0d);	
		        }
		        
		        
			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}
	
		}
		else{
			this.getEntity().setTotalDDR(0d);	
		}
	}
	
	public void recalculateTransferImport(){
		if (this.getTransferType()!=null && this.getTransferType().equals(
				TransferTypes.BalanceReimbursement.getCode())
				&& this.getPartner() != null)
		{

			SQLQuery query = null;
			Double result = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY2);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				result = (Double) query.uniqueResult();
			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			if (result != null && this.getEntity().getTotalDDR() != null)
			{
				result = this.getEntity().getTotalDDR() - result;
				if (result.compareTo(new Double(0d)) > 0)
				{
					this.getEntity().setTransferImport(result);
				}
				else
				{
					this.getEntity().setTransferImport(0d);
				}
			}
			else if (this.getEntity().getTotalDDR() == null)
			{
				this.getEntity().setTransferImport(0d);
			}
			else
			{
				this.getEntity().setTransferImport(
						this.getEntity().getTotalDDR());
			}
		}
	}
	
	public void recalculateTotalTransfer(){
		if (this.getPartner() != null)
		{
			SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY2);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				query.setParameter("add_user_role_id", this.getUserRole().getId());
				if (query.uniqueResult() != null)
				{
					this.getEntity().setTotalTransfer((Double) query.uniqueResult());
				}
				else
				{
					this.getEntity().setTotalTransfer(0d);
				}

			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}

		}
		else
		{
			this.getEntity().setTotalDDR(0d);
		}
	}
	
	public void recalculateTotalAdvance(){
		if (this.getPartner() != null)
		{
			SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY3);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				query.setParameter("add_user_role_id", this.getUserRole().getId());
				query.setParameter("code", TransferTypes.Advance.getCode());
				query.setParameter("user_id", this.getSessionBean().getCurrentUser().getId());
				if (query.uniqueResult() != null)
				{
					this.getEntity().setTotalAdvances((Double) query.uniqueResult());
				}
				else
				{
					this.getEntity().setTotalAdvances(0d);
				}

			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}

		}
		else
		{
			this.getEntity().setTotalDDR(0d);
		}
	}
	
	
	public void recalculateaTotaleAdvanceStateAidDeMinimis(){
		if (this.getPartner() != null)
		{
			SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY3);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				query.setParameter("add_user_role_id", this.getUserRole().getId());
				query.setParameter("code", TransferTypes.AdvanceStateAidDeMinimis.getCode());
				query.setParameter("user_id", this.getSessionBean().getCurrentUser().getId());
				if (query.uniqueResult() != null)
				{
					this.getEntity().setAdvanceStateAidDeMinimis((Double) query.uniqueResult());
				}
				else
				{
					this.getEntity().setAdvanceStateAidDeMinimis(0d);
				}

			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}

		}
	}
	
	public void recalculateaTotaleAdvanceStateAidExemptionScheme(){
		if (this.getPartner() != null)
		{
			SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY3);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				query.setParameter("add_user_role_id", this.getUserRole().getId());
				//query.setParameter("code", TransferTypes.AdvanceStateAidExemptionScheme.getCode());
				query.setParameter("user_id", this.getSessionBean().getCurrentUser().getId());
				if (query.uniqueResult() != null)
				{
					this.getEntity().setAdvanceStateAidExemptionScheme((Double) query.uniqueResult());
				}
				else
				{
					this.getEntity().setAdvanceStateAidExemptionScheme(0d);
				}

			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}

		}
	}
	
	
	public void recalculateTotalRepayments(){
		if (this.getPartner() != null)
		{
			SQLQuery query = null;
			try
			{
				query = PersistenceSessionManager.getBean().getSession()
						.createSQLQuery(QUERY3);

				query.setParameter("project_id", this.getProjectId());
				query.setParameter("fesr_bf_id", this.getPartner());
				query.setParameter("add_user_role_id", this.getUserRole().getId());
				query.setParameter("code", TransferTypes.InermediateReimbursement.getCode());
				query.setParameter("user_id", this.getSessionBean().getCurrentUser().getId());
				if (query.uniqueResult() != null)
				{
					this.getEntity().setTotalRepayments((Double) query.uniqueResult());
				}
				else
				{
					this.getEntity().setTotalRepayments(0d);
				}

			}
			catch (HibernateException e)
			{
				log.error(e);
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}

		}
		else
		{
			this.getEntity().setTotalDDR(0d);
		}
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException
	{
		if(BeforeSave()){
			this.getEntity().setFesrBf(BeansFactory.CFPartnerAnagraphs().Load(this.getPartner()));
			this.getEntity().setTransferTypeCode(this.getTransferType());
			this.getEntity().setProject(this.getProject());
			this.saveOrDeleteDocument();
			this.getEntity().setAddUserRole(this.getUserRole());
			this.getEntity().setAxis(this.getAxis());
			this.getEntity().setUser(this.getSessionBean().getCurrentUser());
			BeansFactory.AdditionalFESRCFInfo().Save(this.getEntity());	
			this.AfterSave();
		}

	}
	
	
	private void saveOrDeleteDocument() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException{
		Documents doc = null;
		if (this.getViewState().get("Document") != null)
		{

			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(
					newFileName)))
			{
				doc = new Documents();
				doc.setTitle(FileHelper.getFileNameWOExtension(docinfo
						.getName()));
				doc.setName(docinfo.getName());
				doc.setUser(this.getSessionBean().getCurrentUser());
//				doc.setSection(BeansFactory.Sections().Load(
//						DocumentSections.AdditionalFestInfo.getValue()));
				doc.setSection(BeansFactory.Sections().Load(
						DocumentSections.LeadTranser.getValue()));
				doc.setProject(this.getProject());
				doc.setDocumentDate(docinfo.getDate());
				doc.setProtocolNumber(docinfo.getProtNumber());
				doc.setIsPublic(docinfo.getIsPublic());
				doc.setCategory(docinfo.getCategory());
				if (this.getRole() != null
						&& !this.getRole().isEmpty())
				{
					doc.setUploadRole(Integer.parseInt(this
							.getRole()));
				}
				doc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(doc);
			}
		}
		
		if (doc != null)
		{
			this.getEntity().setDocument(doc);
		}
		else if(this.getEntity().getDocument()!=null){
			this.getEntity().setDocument(this.getEntity().getDocument());
		}
		if (this.getViewState().get("DocumentToDel") != null)
		{
			BeansFactory.Documents().DeleteInTransaction(
					(Long) this.getViewState().get("DocumentToDel"));
			this.getEntity().setDocument(doc);
		}
		
	}
	
	public void getDoc()
	{
		if (this.getViewState().get("Document") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document");

			FileHelper.sendFile(docinfo, true);
		}
		else if (this.getEntity().getDocument() != null)
		{
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity()
					.getDocument().getName(), this.getEntity()
					.getDocument().getFileName(), null, this.getDocument().getSignflag()), false);
		}
	}

	public void deleteDoc() throws NumberFormatException, HibernateException,
			PersistenceBeanException, NullPointerException, IOException
	{
		if (this.getViewState().get("Document") != null)
		{
			this.getViewState().put("Document", null);
		}
		else if (this.getEntity().getDocument() != null)
		{
			this.getViewState().put(
					"DocumentToDel",
					BeansFactory
					.Documents()
					.Load(this.getEntity().getDocument()
							.getId()).getId());
			this.getEntity().setDocument(null);
		}

		this.Page_Load();
	}
	
	public void saveDocument() throws NumberFormatException,
			HibernateException, PersistenceBeanException, NullPointerException, IOException
	{
		try
		{
			if (this.getDocumentUpFile() != null
					&& this.getDocument() != null)
			{
				String fileName = FileHelper.newTempFileName(this
						.getDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null
						&& !this.getSelectedCategory().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getDocument()
						.getDocumentDate(),
						this.getDocumentUpFile().getName(), fileName, this
								.getDocument().getProtocolNumber(), cat, this
								.getDocument().getIsPublic(), this.getDocument().getSignflag());

				this.getViewState().put("Document", doc);
				this.setRole(null);
				this.setSelectedCategory(null);
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e);
		}
		catch (IOException e)
		{
			log.error(e);
		}

		this.Page_Load();
	}

	/**
	 * Gets listPartners
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners()
	{
		return listPartners;
	}

	/**
	 * Sets listPartners
	 * @param listPartners the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners)
	{
		this.listPartners = listPartners;
	}

	/**
	 * Gets transferTypes
	 * @return transferTypes the transferTypes
	 */
	public List<SelectItem> getTransferTypes()
	{
		return transferTypes;
	}

	/**
	 * Sets transferTypes
	 * @param transferTypes the transferTypes to set
	 */
	public void setTransferTypes(List<SelectItem> transferTypes)
	{
		this.transferTypes = transferTypes;
	}

	/**
	 * Gets partner
	 * @return partner the partner
	 */
	public Long getPartner()
	{
		if(this.getSession().get("selectedPartner")!=null){
			return ((Long)this.getSession().get("selectedPartner"));
		}
		return null;
	}

	/**
	 * Sets partner
	 * @param partner the partner to set
	 */
	public void setPartner(Long partner)
	{
		
		try
		{
			this.getSession().put("selectedPartner", partner);
			this.Page_Load();
			//this.recalculateTransferImport();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * Gets transferType
	 * @return transferType the transferType
	 */
	public Long getTransferType()
	{
		if(this.getSession().get("transferType")!=null){
			return ((Long)this.getSession().get("transferType"));
		}
		return null;
	}
	
	private void fillRoles() throws PersistenceBeanException
	{
		this.listSelectRoles = new ArrayList<SelectItem>();

		this.listSelectRoles.add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();
		for (UserRoles userRole : UserRolesBean.GetByUser(String.valueOf(this
				.getCurrentUser().getId())))
		{
			if(userRole.getRole().getCode().equals("DAEC")){
				continue;
			}
			if (temp.add(userRole.getRole().getCode()))
			{
				UploadDocumentRoleType roleType = UploadDocumentRoleType
						.getByName(userRole.getRole().getCode());
				if (roleType != null)
				{
					this.getListSelectRoles().add(
							new SelectItem(String.valueOf(roleType.getId()),
									roleType.getDisplayName()));
				}
			}
		}
	}
	
	private void fillCategories() throws PersistenceBeanException
	{
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load())
		{
			categories.add(new SelectItem(String.valueOf(item.getId()), item
					.getName()));
		}
	}
	

	/**
	 * Sets transferType
	 * @param transferType the transferType to set
	 */
	public void setTransferType(Long transferType)
	{
		try
		{
			this.getSession().put("transferType", transferType);
			this.Page_Load();
			//this.recalculateTransferImport();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets totalDDR
	 * @return totalDDR the totalDDR
	 */
	public Double getTotalDDR()
	{
		return totalDDR;
	}

	/**
	 * Sets totalDDR
	 * @param totalDDR the totalDDR to set
	 */
	public void setTotalDDR(Double totalDDR)
	{
		this.totalDDR = totalDDR;
	}
	
	public void setRole(String entity)
	{
		this.getViewState().put("selectedRole", entity);
	}

	public Boolean getDocInvalid()
	{
		return (Boolean) this.getViewState().get("docInvalid");
	}
	
	public void setDocInvalid(Boolean docInvalid)
	{
		this.getViewState().put("docInvalid", docInvalid);
	}

	public String getRole()
	{
		return (String) this.getViewState().get("selectedRole");
	}
	
	public String getSelectedCategory()
	{
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

	public Documents getDocument()
	{
		return document;
	}
	
	public void setDocument(Documents document)
	{
		this.document = document;
	}

	public UploadedFile getDocumentUpFile()
	{
		return documentUpFile;
	}

	public void setDocumentUpFile(UploadedFile documentUpFile)
	{
		this.documentUpFile = documentUpFile;
	}

	public List<SelectItem> getListSelectRoles()
	{
		return listSelectRoles;
	}

	public void setListSelectRoles(List<SelectItem> listSelectRoles)
	{
		this.listSelectRoles = listSelectRoles;
	}

	public List<SelectItem> getListPartnersDoc()
	{
		return listPartnersDoc;
	}

	public void setListPartnersDoc(List<SelectItem> listPartnersDoc)
	{
		this.listPartnersDoc = listPartnersDoc;
	}

	public List<SelectItem> getCategories()
	{
		return categories;
	}

	public void setCategories(List<SelectItem> categories)
	{
		this.categories = categories;
	}

	public String getDocTitle()
	{
		return docTitle;
	}

	public void setDocTitle(String docTitle)
	{
		this.docTitle = docTitle;
	}

	/**
	 * Gets userRole
	 * @return userRole the userRole
	 */
	public Roles getUserRole()
	{
		return userRole;
	}

	/**
	 * Sets userRole
	 * @param userRole the userRole to set
	 */
	public void setUserRole(Roles userRole)
	{
		this.userRole = userRole;
	}

	public String getAxis() {
		return (String) this.getViewState().get("axis");
	}

	public void setAxis(String axis) {
		this.getViewState().put("axis", axis);
	}

	public List<SelectItem> getAxisList() {
		return axisList;
	}

	public void setAxisList(List<SelectItem> axisList) {
		this.axisList = axisList;
	}
	
	
	
}
