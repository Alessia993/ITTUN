/**
 * 
 */
package com.infostroy.paamns.web.beans.documentms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalCheckToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjectDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.SignedDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Sections;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.CostDefinitionsToDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.DocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.DocumentsBeanBaseRequest;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class DocumentListBean extends EntityProjectListBean<Documents>
{
	private List<SelectItem>	listSections;

	private List<SelectItem>	listUserRoles;

	private List<SelectItem>	listProjects;

	private List<SelectItem>	listCategories;

	private List<SelectItem>	costDefinitionPhases;
	
	private List<SelectItem>	categories;
	
	private List<SelectItem>	partners;
	
	private String signflag;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.LoadSections();
		this.LoadProjects();
		this.LoadUserRoles();
		this.loadCategories();
		this.fillPhases();
		this.fillPartners();
		fillCategories();
		
		SignedDocuments signedDocumentLoaded = BeansFactory.SignedDocuments().LoadByDocumentId(this.getEntityEditId()); 
		if (signedDocumentLoaded != null) {
			Long id_signdoc = signedDocumentLoaded.getId();
			this.setEntityEditId2(id_signdoc);
			this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
			this.getSession().put("signFlag", "YES");
			//System.out.println(this.getSession().put("documentMSEntityId2", this.getEntityEditId2()));
			this.signflag="YES";
			//this.setSignFlag("YES");
			//signflag = "YES";
		}
		else {
			this.getSession().put("documentMSEntityId2", null);   // this means there is no signed document associated to document of interest
			//this.signflag="NO";  //.getSession().put("signFlag", "NO");
			//this.setSignFlag("NO");
			//signflag = "NO";
		}
		
		DocumentsBeanBaseRequest baseRequest = new DocumentsBeanBaseRequest(
				this.getProject().getId(), this.getTitlePattern(),
				this.getDateFrom(), this.getDateTo(), this.getProtocolNumber(),
				this.getSection() == null ? null : this.getSection().equals(
						String.valueOf(SelectItemHelper.getAllElement()
								.getValue())) ? null : Long.valueOf(this
						.getSection()), this.getDocumentNumber(),
				this.getEditableDate(), this.getOnlyDocument() != null
						&& Boolean.TRUE.equals(this.getOnlyDocument()), this.getSignFlag());
		if (this.getSessionBean().isSTC()
				|| (!this.getSessionBean().isAAU()
						&& !this.getSessionBean().isPartner()
						&& !this.getSessionBean().isCIL() && !this
						.getSessionBean().isDAEC()))
		{
			this.setList(BeansFactory.Documents().LoadByParams(baseRequest,
					this.getUser(), this.getPartnerId(), this.getUserRole()));

			for (Documents doc : this.getList())
			{
				doc.setEditable(this.getSessionBean().isACU()
						|| this.getSessionBean().isAGU()
						|| this.getSessionBean().isSTC()
						|| doc.getUser() == null ? true : doc.getUser().getId()
						.equals(this.getSessionBean().getCurrentUser().getId()));
			}
		}
		else if (this.getSessionBean().isPartner())
		{
			List<Long> userIds = new ArrayList<Long>();
			userIds.add(this.getSessionBean().getCurrentUser().getId());

			CFPartnerAnagraphProject cfPartnerAnagraphProjectCur = null;
			for (CFPartnerAnagraphProject item : BeansFactory
					.CFPartnerAnagraphProject().GetCFAnagraphForProject(
							this.getProjectId(), null))
			{

				if (item.getCfPartnerAnagraphs() != null
						&& item.getCfPartnerAnagraphs().getUser() != null
						&& item.getCfPartnerAnagraphs()
								.getUser()
								.getId()
								.equals(this.getSessionBean().getCurrentUser()
										.getId()))
				{
					cfPartnerAnagraphProjectCur = item;
					if (cfPartnerAnagraphProjectCur.getCil() != null
							&& cfPartnerAnagraphProjectCur.getCil().getUser() != null)
					{
						if (!userIds.contains(cfPartnerAnagraphProjectCur
								.getCil().getUser().getId()))
						{
							userIds.add(cfPartnerAnagraphProjectCur.getCil()
									.getUser().getId());
						}
					}
				}
			}

			if (cfPartnerAnagraphProjectCur != null
					&& cfPartnerAnagraphProjectCur.getNaming() != null)
			{
				for (CFPartnerAnagraphProject item : BeansFactory
						.CFPartnerAnagraphProject().GetCFAnagraphForProject(
								this.getProjectId(), null))
				{

					if (item.getCfPartnerAnagraphs() != null
							&& item.getCfPartnerAnagraphs().getUser() != null
							&& item.getNaming() != null
							&& cfPartnerAnagraphProjectCur.getNaming().equals(
									item.getNaming()))
					{
						if (!userIds.contains(item.getCfPartnerAnagraphs()
								.getUser().getId()))
						{
							userIds.add(item.getCfPartnerAnagraphs().getUser()
									.getId());
						}
					}

				}
			}

			this.setList(BeansFactory.Documents().LoadByParamsForPartner(
					baseRequest, this.getUser(), userIds, this.getPartnerId(),
					this.getUserRole()));

			for (Documents doc : this.getList())
			{
				doc.setEditable(doc.getUser() == null ? true : doc.getUser()
						.getId()
						.equals(this.getSessionBean().getCurrentUser().getId()));
			}
		}
		else if (this.getSessionBean().isCIL()
				|| this.getSessionBean().isDAEC())
		{
			boolean forDaec = false;
			boolean isAguCil = false;
			List<Long> userIds = new ArrayList<Long>();
			Long curUserId = this.getSessionBean().getCurrentUser().getId();
			if (this.getSessionBean().isCIL())
			{
				for (CFPartnerAnagraphProject item : BeansFactory
						.CFPartnerAnagraphProject().GetCFAnagraphForProject(
								this.getProjectId(), null))
				{

					if (item.getCil() != null
							&& item.getCil().getUser() != null
							&& item.getCil().getUser().getId()
									.equals(curUserId))
					{
						if (item.getCfPartnerAnagraphs().getUser() != null)
						{
							userIds.add(item.getCfPartnerAnagraphs().getUser()
									.getId());
						}
						else
						{
							isAguCil = true;
						}
					}
				}

				userIds.add(curUserId);
			}
			else if (this.getSessionBean().isDAEC())
			{
				for (CFPartnerAnagraphProject item : BeansFactory
						.CFPartnerAnagraphProject().GetCFAnagraphForProject(
								this.getProjectId(), null))
				{
					if (item.getDaec() != null
							&& item.getDaec().getUser() != null
							&& item.getDaec().getUser().getId()
									.equals(curUserId))
					{
						if (item.getCfPartnerAnagraphs().getUser() != null)
						{
							userIds.add(item.getCfPartnerAnagraphs().getUser()
									.getId());
						}
					}
				}

				forDaec = true;

				userIds.add(curUserId);
			}

			if (isAguCil)
			{
				this.setList(BeansFactory.Documents().LoadByParamsForCILAsse5(
						baseRequest, this.getUser(), userIds,
						this.getPartnerId(), this.getUserRole()));
			}
			else
			{
				this.setList(BeansFactory.Documents().LoadByParams(baseRequest,

				this.getUser(), userIds, this.getPartnerId(),
						this.getUserRole(), forDaec));
			}
			for (Documents doc : this.getList())
			{
				doc.setEditable(doc.getUser() == null ? true : doc.getUser()
						.getId()
						.equals(this.getSessionBean().getCurrentUser().getId()));
			}
		}
		else if (getSessionBean().isAAU())
		{
			this.setList(BeansFactory.Documents().LoadByParams(baseRequest,

			this.getUser(), this.getPartnerId(), this.getUserRole()));

			for (Documents doc : this.getList())
			{
				doc.setEditable(doc.getUser() == null ? true : doc.getUser()
						.getId()
						.equals(this.getSessionBean().getCurrentUser().getId()));
			}
		}
		else
		{
			baseRequest.setProjectId(null);
			this.setList(BeansFactory.Documents().LoadByParams(baseRequest,

			this.getUser(), this.getPartnerId(), this.getUserRole()));

			for (Documents doc : this.getList())
			{
				doc.setEditable(true);
			}
		}

		if (this.getUser() != null && !this.getUser().isEmpty())
		{

			String[] nWords = this.getUser().split(" ");

			String name = null;

			String surname = null;

			if (nWords.length == 1)
			{
				name = nWords[0];
				Iterator<Documents> iterator = this.getList().iterator();
				while (iterator.hasNext())
				{
					Documents doc = (Documents) iterator.next();
					if (!(doc.getUser().getName().toLowerCase()
							.contains(name.toLowerCase()) || doc.getUser()
							.getSurname().toLowerCase()
							.contains(name.toLowerCase())))
					{
						iterator.remove();
					}
				}
			}
			else if (nWords.length == 2)
			{

				name = nWords[0];
				surname = nWords[1];
				Iterator<Documents> iterator = this.getList().iterator();
				while (iterator.hasNext())
				{
					Documents doc = (Documents) iterator.next();
					if (!(doc.getUser().getName().toLowerCase()
							.contains(name.toLowerCase()) && doc.getUser()
							.getSurname().toLowerCase()
							.contains(surname.toLowerCase())))
					{
						iterator.remove();
					}
				}
			}
		}
		if (this.getSelectedCategory() != null
				&& !this.getSelectedCategory().isEmpty())
		{
			Iterator<Documents> iterator = this.getList().iterator();
			long selectedCategoryId = Long
					.parseLong(this.getSelectedCategory());
			while (iterator.hasNext())
			{
				Documents document = (Documents) iterator.next();
				if (document.getCategory() != null
						&& document.getCategory().getId()
								.equals(selectedCategoryId))
				{
					continue;
				}
				iterator.remove();
			}
		}
		if (this.getDenomination() != null && !this.getDenomination().isEmpty())
		{
			Iterator<Documents> iterator = this.getList().iterator();
			while (iterator.hasNext())
			{
				Documents document = (Documents) iterator.next();
				CFPartnerAnagraphs anagraph = document.getPartnerAnagraph();

				if (anagraph != null)
				{
					String naming = anagraph.getNaming().toLowerCase();
					String denomination = this.getDenomination().toLowerCase()
							.trim();
					if (naming.contains(denomination))
					{
						continue;
					}
				}
				iterator.remove();
			}
		}
		Map<Long, Documents> rezultMap = new HashMap<Long, Documents>();
		for (Documents document : this.getList())
		{
			rezultMap.put(document.getId(), document);
		}
		List<Documents> tempList = new ArrayList<Documents>();
		if ((this.getCostDefinitionId() != null && !this.getCostDefinitionId()
				.isEmpty())
				|| (this.getCostDefinitionPhase() != null && !this
						.getCostDefinitionPhase().isEmpty()))
		{
			Long costDefinitionId = null;
			if (this.getCostDefinitionId() != null
					&& !this.getCostDefinitionId().isEmpty())
			{
				costDefinitionId = Long.parseLong(this.getCostDefinitionId());
			}
			Long costDefinitionPhaseId = null;
			if (this.getCostDefinitionPhase() != null
					&& !this.getCostDefinitionPhase().isEmpty())
			{
				costDefinitionPhaseId = Long.parseLong(this
						.getCostDefinitionPhase());
			}
			List<CostDefinitionsToDocuments> definitionsByDocument = new CostDefinitionsToDocumentsBean()
					.getByDocuments(rezultMap.keySet());
			for (CostDefinitionsToDocuments cd : definitionsByDocument)
			{
				if (findCostDefinitions(costDefinitionId,
						costDefinitionPhaseId, cd))
				{
					tempList.add(cd.getDocument());
				}
			}
			this.setList(tempList);
			this.getList().addAll(
					new DocumentsBean().loadByCostDefinitionParams(this
							.getProject().getId(), costDefinitionId,
							costDefinitionPhaseId));
		}
		this.ReRenderScroll();
		if (this.getShowScroll())
		{
			resetDataScroller(null);
		}
	}

	/**
	 * @param costDefinitionId
	 * @param definitionsByDocument
	 * @throws PersistenceBeanException
	 */
	private boolean findCostDefinitions(Long costDefinitionId,
			Long costDefinitionPhaseId, CostDefinitionsToDocuments cd)
			throws PersistenceBeanException
	{
		boolean action = costDefinitionId != null
				&& costDefinitionPhaseId != null;
		boolean result = false;
		if (costDefinitionId != null
				&& costDefinitionId.equals(cd.getCostDefinition()
						.getProgressiveId()))
		{
			if (action)
			{
				result = true;
			}
			else
			{
				return true;
			}
		}
		if (costDefinitionPhaseId != null
				&& costDefinitionPhaseId.equals(cd.getCostDefinition()
						.getCostDefinitionPhase().getId()))
		{
			if (action)
			{
				if (result)
				{
					return true;
				}
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @throws PersistenceBeanException
	 * 
	 */
	private void loadCategories() throws PersistenceBeanException
	{
		this.setListCategories(new ArrayList<SelectItem>());
		this.getListCategories().add(SelectItemHelper.getAllElement());

		for (Category item : BeansFactory.Category().Load())
		{
			this.getListCategories()
					.add(new SelectItem(String.valueOf(item.getId()), item
							.getName()));
		}
	}

	private void fillPhases() throws PersistenceBeanException
	{
		setCostDefinitionPhases(new ArrayList<SelectItem>());
		List<CostDefinitionPhases> list = BeansFactory.CostDefinitionPhase()
				.Load();
		getCostDefinitionPhases().add(SelectItemHelper.getAllElement());
		for (CostDefinitionPhases item : list)
		{
			Integer value = null;
			try
			{
				value = Integer.valueOf(item.getValue());
			}
			catch (NumberFormatException e)
			{

			}

			if (value != null && value != 20)
			{
				getCostDefinitionPhases().add(
						new SelectItem(String.valueOf(item.getId()), item
								.getValue()));
			}
			else if (value != null && value == 20)
			{
				getCostDefinitionPhases().add(
						new SelectItem(String.valueOf(item.getId()), Utils
								.getString("Resources",
										"costDefinitionNonApplicable", null)));
			}
		}
	}
	
	private void fillCategories() throws PersistenceBeanException
	{
		categories = new ArrayList<SelectItem>();
		categories.add(SelectItemHelper.getFirst());

		for (Category item : BeansFactory.Category().Load())
		{
			categories.add(new SelectItem(String.valueOf(item.getId()), item
					.getName()));
		}
	}
	
	private void fillPartners() throws NumberFormatException, PersistenceBeanException
	{
		partners = new ArrayList<SelectItem>();
		partners.add(SelectItemHelper.getFirst());
		for(CFPartnerAnagraphs cfpap : BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId())){
			partners.add(new SelectItem(String.valueOf(cfpap.getProgressiveId()), cfpap.getNaming()));
		}
		
	}
	
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		if (this.getProjectId() == null)
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	private void LoadSections() throws PersistenceBeanException
	{
		List<Sections> listSections = BeansFactory.Sections().Load();
		this.setListSections(new ArrayList<SelectItem>());
		this.getListSections().add(SelectItemHelper.getAllElement());

		for (Sections s : listSections)
		{
			String value = s.getValue() != null ? s.getValue() : "";
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(s.getId()));
			item.setLabel(value);

			this.getListSections().add(item);
		}
	}

	private void LoadUserRoles() throws PersistenceBeanException
	{
		List<Roles> listUR = BeansFactory.Roles().Load();
		this.setListUserRoles(new ArrayList<SelectItem>());
		this.getListUserRoles().add(SelectItemHelper.getAllElement());

		for (Roles ur : listUR)
		{
			if(ur.getCode().equals("DAEC")){
				continue;
			}
			String value = ur.getDescription() != null ? ur.getDescription()
					: "";
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(ur.getId()));
			item.setLabel(value);

			this.getListUserRoles().add(item);
		}
	}

	private void LoadProjects() throws PersistenceBeanException
	{
		List<Projects> listProjects = BeansFactory.Projects().Load();
		this.setListProjects(new ArrayList<SelectItem>());
		this.getListProjects().add(SelectItemHelper.getAllElement());

		for (Projects p : listProjects)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(p.getId()));
			item.setLabel(p.getCode() == null ? "" : p.getCode());

			this.getListProjects().add(item);
		}
	}

	public void doSearch()
	{
		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
	}

	public void clear()
	{
		this.setTitlePattern(null);
		this.setDateFrom(null);
		this.setDateTo(null);
		this.setUser(null);
		this.setProtocolNumber(null);
		this.setSection(String.valueOf(SelectItemHelper.getAllElement()
				.getValue()));
		this.setPartnerId(null);
		this.setUserRole(null);
		this.setCostDefinitionId(null);
		this.setCostDefinitionPhase(null);
		this.setDocumentNumber(null);
		this.setEditableDate(null);
		this.setSelectedCategory(null);
		this.setDenomination(null);
		this.setOnlyDocument(null);
		this.setSignFlag(null);

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void addEntity()
	{
		this.getSession().put("documentMSEntityId", null);
		this.getSession().put("documentMSEntityId2", null);
		this.getSession().put("documentMSShowOnly", false);
		this.goTo(PagesTypes.DOCUMENTEDIT);
	}

	@Override
	public void deleteEntity()
	{
		try
			{
				/// unsigned document can be deleted only if the correspondent signed document is not uploaded 
			//if (this.getSession().get("documentMSEntityId2") == null)   {   // this means that there is no signed document associated to this document --->>> the unsigned document can be deleted
				//System.out.println(this.getSession().get("documentMSEntityId2"));
				if (this.CheckEntityExists(this.getEntityDeleteId()))
				{
					Documents doc = BeansFactory.Documents().Load(
							this.getEntityDeleteId());
	
					if (doc.getSection() != null)
					{
						if (doc.getSection()
								.getId()
								.equals(DocumentSections.ProjectInfoCompletation
										.getValue()))
						{
							List<ProjectIformationCompletations> listPics = BeansFactory
									.ProjectIformationCompletations()
									.LoadByDocument(doc.getId());
	
							for (ProjectIformationCompletations pic : listPics)
							{
								pic.setPicDocument(null);
								BeansFactory.ProjectIformationCompletations().Save(
										pic);
							}
						}
						else if (doc.getSection().getId()
								.equals(DocumentSections.CostDefinition.getValue()))
						{
							List<CostDefinitionsToDocuments> listCD = BeansFactory
									.CostDefinitionsToDocuments().getByDocument(
											doc.getId());
	
							for (CostDefinitionsToDocuments cd : listCD)
							{
								BeansFactory.CostDefinitionsToDocuments()
										.Delete(cd);
							}
						}
						else if (doc.getSection().getId()
								.equals(DocumentSections.ValidationFlow.getValue()))
						{
							List<CostDefinitionsToDocuments> listCD = BeansFactory
									.CostDefinitionsToDocuments().getByDocument(
											doc.getId());
	
							for (CostDefinitionsToDocuments cd : listCD)
							{
								BeansFactory.CostDefinitionsToDocuments()
										.Delete(cd);
							}
						}
						else if (doc.getSection().getId()
								.equals(DocumentSections.LocalCheck.getValue()))
						{
							List<LocalCheckToDocuments> listLC = BeansFactory
									.LocalCheckToDocuments().getByDocument(
											doc.getId());
	
							for (LocalCheckToDocuments lc : listLC)
							{
								BeansFactory.LocalCheckToDocuments().Delete(lc);
							}
						}
						else if (doc.getSection().getId()
								.equals(DocumentSections.DURCompilation.getValue()))
						{
							List<DurInfos> listDurs = BeansFactory.DurInfos()
									.LoadByDocument(doc.getId());
	
							for (DurInfos di : listDurs)
							{
								if (di.getActivityResumeDocument().getId()
										.equals(doc.getId()))
								{
									di.setActivityResumeDocument(null);
								}
	
								if (di.getFinancialDetailDocument().getId()
										.equals(doc.getId()))
								{
									di.setFinancialDetailDocument(null);
								}
	
								BeansFactory.DurInfos().Save(di);
							}
						}
						else if (doc
								.getSection()
								.getId()
								.equals(DocumentSections.ProjectStateChanges
										.getValue()))
						{
							List<ProjectStatesChanges> listPS = BeansFactory
									.ProjectStatesChanges().LoadByDocument(
											doc.getId());
	
							for (ProjectStatesChanges psc : listPS)
							{
								psc.setDocument(null);
								BeansFactory.ProjectStatesChanges().Save(psc);
							}
						}
						else if (doc
								.getSection()
								.getId()
								.equals(DocumentSections.DocumentManagement
										.getValue()))
						{
							// Do nothing
						}
						else if (doc
								.getSection()
								.getId()
								.equals(DocumentSections.ProgressValidationFlow
										.getValue()))
						{
							List<ProgressValidationObjectDocuments> listPVF = BeansFactory
									.ProgressValidationObjectDocuments()
									.LoadByDocument(doc.getId());
	
							for (ProgressValidationObjectDocuments pv : listPVF)
							{
								BeansFactory.ProgressValidationObjectDocuments()
										.Delete(pv);
							}
						}
						else
						{
							// Unreachable situation
						}
					}
	
					BeansFactory.Documents().Delete(doc);
				}
	
				this.Page_Load();
			//} // end if check on existing signed document
			
			/*else if (this.getSession().get("documentMSEntityId2") != null)   { // this means there is a signed document associated to the document of interest
				SignedDocuments doc = BeansFactory.SignedDocuments().Load(
						this.getEntityDeleteId2());
				BeansFactory.SignedDocuments().Delete(doc);   // the signed document is deleted
				
			}
			this.Page_Load(); */
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void editEntity()
	{
		if (this.CheckEntityExists(this.getEntityEditId()))
		{
			//System.out.println("this.CheckEntityExists(this.getEntityEditId())" + this.CheckEntityExists(this.getEntityEditId()));
			this.getSession().put("documentMSEntityId", this.getEntityEditId());
			
			//System.out.println(this.getEntityEditId2());
			//this.getSession().put("documentMSEntityId2", this.getEntityEditId());			
			
		
			try {
				SignedDocuments signedDocumentLoaded = BeansFactory.SignedDocuments().LoadByDocumentId(this.getEntityEditId()); 
				if (signedDocumentLoaded != null) {
					Long id_signdoc = signedDocumentLoaded.getId();
					this.setEntityEditId2(id_signdoc);
					this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
					this.getSession().put("signFlag", "YES");
					this.signflag = "YES";
					//System.out.println(this.getSession().put("documentMSEntityId2", this.getEntityEditId2()));
					//this.setSignFlag("YES");
					//signflag = "YES";
				}
				else {
					this.getSession().put("documentMSEntityId2", null);   // this means there is no signed document associated to document of interest
					this.getSession().put("signFlag", "NO");
					this.signflag = "NO";
					//this.setSignFlag("NO");
					//signflag = "NO";
				}
				
			} catch (PersistenceBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.getSession().put("documentMSShowOnly", false);
			this.goTo(PagesTypes.DOCUMENTEDIT);
			
			/*if (this.getEntityEditId2() != null) {
				if (this.CheckEntityExists(this.getEntityEditId2())) {
					this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
				}
			} */
			
		}
		else
		{
			try
			{
				this.Page_Load();
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			catch (HibernateException e)
			{
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void showEntity()
	{
		if (this.CheckEntityExists(this.getEntityEditId()))
		{
			this.getSession().put("documentMSEntityId", this.getEntityEditId());
			
			try {
				SignedDocuments signedDocumentLoaded = BeansFactory.SignedDocuments().LoadByDocumentId(this.getEntityEditId()); 
				if (signedDocumentLoaded != null) {
					Long id_signdoc = signedDocumentLoaded.getId();
					this.setEntityEditId2(id_signdoc);
					this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
					this.getSession().put("signFlag", "YES");
					this.signflag="YES";
				}
				else {
					this.getSession().put("documentMSEntityId2", null);   // this means there is no signed document associated to document of interest
					this.getSession().put("signFlag", "NO");
					this.signflag="NO";
				}
				
			} catch (PersistenceBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*if (this.getEntityEditId2() !=null) {
				if (this.CheckEntityExists(this.getEntityEditId2())) {
					this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
				}
			} */
			//this.getSession().put("documentMSEntityId2", this.getEntityEditId());
			this.getSession().put("documentMSShowOnly", true);
			this.goTo(PagesTypes.DOCUMENTEDIT);
		}
		else
		{
			try
			{
				this.Page_Load();
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			catch (HibernateException e)
			{
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void downloadEntity() throws HibernateException,
			PersistenceBeanException
	{
		if (this.CheckEntityExists(this.getEntityDownloadId()))
		{
			Documents doc = BeansFactory.Documents().Load(
					this.getEntityDownloadId());

			if (doc.getFileName() != null)
			{
				DocumentInfo docinfo = new DocumentInfo(doc.getDocumentDate(),
						doc.getName(), doc.getFileName(),
						doc.getProtocolNumber(), null, doc.getSignflag());
						

				FileHelper.sendFile(docinfo, false);
			}
		}
		else
		{
			try
			{
				this.Page_Load();
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			catch (HibernateException e)
			{
				e.printStackTrace();
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean CheckEntityExists(Long entityId)
	{
		Documents doc = null;

		try
		{
			doc = BeansFactory.Documents().Load(entityId);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (doc == null || doc.getDeleted())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean CheckEntityExists2(Long entityId2)
	{
		SignedDocuments doc = null;

		try
		{
			doc = BeansFactory.SignedDocuments().Load(entityId2);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (doc == null || doc.getDeleted())
		{
			return false;
		}
		else
		{
			return true;
		}
	}	
	
	/**
	 * Sets entityDownloadId
	 * 
	 * @param entityDownloadId
	 *            the entityDownloadId to set
	 */
	public void setEntityDownloadId(Long entityDownloadId)
	{
		this.getSession().put("entityDownloadId", entityDownloadId);
	}

	/**
	 * Gets entityDownloadId
	 * 
	 * @return entityDownloadId the entityDownloadId
	 */
	public Long getEntityDownloadId()
	{
		return (Long) this.getSession().get("entityDownloadId");
	}

	/**
	 * Sets titlePattern
	 * 
	 * @param titlePattern
	 *            the titlePattern to set
	 */
	public void setTitlePattern(String titlePattern)
	{
		this.getSession().put("title", titlePattern);
	}

	/**
	 * Gets titlePattern
	 * 
	 * @return titlePattern the titlePattern
	 */
	public String getTitlePattern()
	{
		return (String) this.getSession().get("title");
	}

	/**
	 * Sets dateFrom
	 * 
	 * @param dateFrom
	 *            the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom)
	{
		this.getSession().put("dateFrom", dateFrom);
	}

	/**
	 * Gets dateFrom
	 * 
	 * @return dateFrom the dateFrom
	 */
	public Date getDateFrom()
	{
		if (this.getSession().get("dateFrom") != null)
		{
			return (Date) this.getSession().get("dateFrom");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets dateTo
	 * 
	 * @param dateTo
	 *            the dateTo to set
	 */
	public void setDateTo(Date dateTo)
	{
		this.getSession().put("dateTo", dateTo);
	}

	/**
	 * Gets dateTo
	 * 
	 * @return dateTo the dateTo
	 */
	public Date getDateTo()
	{
		if (this.getSession().get("dateTo") != null)
		{
			return (Date) this.getSession().get("dateTo");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user)
	{
		this.getSession().put("user", user);
	}

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public String getUser()
	{
		return (String) this.getSession().get("user");
	}

	/**
	 * Sets protocolNumber
	 * 
	 * @param protocolNumber
	 *            the protocolNumber to set
	 */
	public void setProtocolNumber(String protocolNumber)
	{
		this.getSession().put("protNumber", protocolNumber);
	}

	/**
	 * Gets protocolNumber
	 * 
	 * @return protocolNumber the protocolNumber
	 */
	public String getProtocolNumber()
	{
		return (String) this.getSession().get("protNumber");
	}

	
	/**
	 * Sets signflag
	 * 
	 * @param signflag
	 *            the signflag to set
	 */
	public void setSignFlag(String signflag)
	{
		this.getSession().put("signFlag", signflag);
	}

	/**
	 * Gets signflag
	 * 
	 * @return protocolNumber the protocolNumber
	 */
	public String getSignFlag()
	{
		return (String) this.getSession().get("signFlag");
	}
	/**
	 * Sets section
	 * 
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section)
	{
		this.getSession().put("section", section);
	}

	/**
	 * Gets section
	 * 
	 * @return section the section
	 */
	public String getSection()
	{
		return (String) this.getSession().get("section");
	}

	public void setPartnerId(String partnerId)
	{
		this.getSession().put("searchPartnerId", partnerId);
	}

	public String getPartnerId()
	{
		if (this.getSession().containsKey("searchPartnerId"))
		{
			return (String) this.getSession().get("searchPartnerId");
		}
		else
		{
			return null;
		}
	}

	public void setUserRole(String userRole)
	{
		this.getSession().put("searchUserRoleId", userRole);
	}

	public String getUserRole()
	{
		if (this.getSession().containsKey("searchUserRoleId"))
		{
			return (String) this.getSession().get("searchUserRoleId");
		}
		else
		{
			return null;
		}
	}

	public void setSelectedCategory(String selectedCategory)
	{
		this.getSession().put("selectedCategory", selectedCategory);
	}

	public String getSelectedCategory()
	{
		if (this.getSession().containsKey("selectedCategory"))
		{
			return (String) this.getSession().get("selectedCategory");
		}
		else
		{
			return null;
		}
	}

	public void setDocumentNumber(String documentNumber)
	{
		this.getSession().put("documentNumber", documentNumber);
	}

	public String getDocumentNumber()
	{
		if (this.getSession().containsKey("documentNumber"))
		{
			return (String) this.getSession().get("documentNumber");
		}
		else
		{
			return null;
		}
	}

	public void setDenomination(String denomination)
	{
		this.getSession().put("denomination", denomination);
	}

	public String getDenomination()
	{
		if (this.getSession().containsKey("denomination"))
		{
			return (String) this.getSession().get("denomination");
		}
		else
		{
			return null;
		}
	}

	public void setCostDefinitionId(String costDefinitionId)
	{
		this.getSession().put("costDefinitionId", costDefinitionId);
	}

	public String getCostDefinitionId()
	{
		if (this.getSession().containsKey("costDefinitionId"))
		{
			return (String) this.getSession().get("costDefinitionId");
		}
		else
		{
			return null;
		}
	}

	public void setCostDefinitionPhase(String costDefinitionPhase)
	{
		this.getSession().put("costDefinitionPhase", costDefinitionPhase);
	}

	public String getCostDefinitionPhase()
	{
		if (this.getSession().containsKey("costDefinitionPhase"))
		{
			return (String) this.getSession().get("costDefinitionPhase");
		}
		else
		{
			return null;
		}
	}

	public void setOnlyDocument(Boolean onlyDocument)
	{
		this.getSession().put("onlyDocument", onlyDocument);
	}

	public Boolean getOnlyDocument()
	{
		if (this.getSession().containsKey("onlyDocument"))
		{
			return (Boolean) this.getSession().get("onlyDocument");
		}
		else
		{
			return null;
		}
	}

	public void setEditableDate(Date editableDate)
	{
		this.getSession().put("editableDate", editableDate);
	}

	public Date getEditableDate()
	{
		if (this.getSession().containsKey("editableDate"))
		{
			return (Date) this.getSession().get("editableDate");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets section
	 * 
	 * @param section
	 *            the section to set
	 */
	public void setSelectedProject(String section)
	{
		this.getSession().put("SelectedProject", section);
	}

	/**
	 * Gets section
	 * 
	 * @return section the section
	 */
	public String getSelectedProject()
	{
		return (String) this.getSession().get("SelectedProject");
	}

	/**
	 * Sets listSections
	 * 
	 * @param listSections
	 *            the listSections to set
	 */
	public void setListSections(List<SelectItem> listSections)
	{
		this.listSections = listSections;
	}

	/**
	 * Gets listSections
	 * 
	 * @return listSections the listSections
	 */
	public List<SelectItem> getListSections()
	{
		return listSections;
	}

	public void setListUserRoles(List<SelectItem> listUserRoles)
	{
		this.listUserRoles = listUserRoles;
	}

	public List<SelectItem> getListUserRoles()
	{
		return this.listUserRoles;
	}

	/**
	 * Sets listProjects
	 * 
	 * @param listProjects
	 *            the listProjects to set
	 */
	public void setListProjects(List<SelectItem> listProjects)
	{
		this.listProjects = listProjects;
	}

	/**
	 * Gets listProjects
	 * 
	 * @return listProjects the listProjects
	 */
	public List<SelectItem> getListProjects()
	{
		return listProjects;
	}

	/**
	 * Gets listCategories
	 * 
	 * @return listCategories the listCategories
	 */
	public List<SelectItem> getListCategories()
	{
		return listCategories;
	}

	/**
	 * Sets listCategories
	 * 
	 * @param listCategories
	 *            the listCategories to set
	 */
	public void setListCategories(List<SelectItem> listCategories)
	{
		this.listCategories = listCategories;
	}

	/**
	 * Gets costDefinitionPhases
	 * 
	 * @return costDefinitionPhases the costDefinitionPhases
	 */
	public List<SelectItem> getCostDefinitionPhases()
	{
		return costDefinitionPhases;
	}

	/**
	 * Sets costDefinitionPhases
	 * 
	 * @param costDefinitionPhases
	 *            the costDefinitionPhases to set
	 */
	public void setCostDefinitionPhases(List<SelectItem> costDefinitionPhases)
	{
		this.costDefinitionPhases = costDefinitionPhases;
	}

	public List<SelectItem> getPartners()
	{
		return partners;
	}

	public void setPartners(List<SelectItem> partners)
	{
		this.partners = partners;
	}


}
