package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.HierarchicalLevelSelector.HierarchicalLevel;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActTypes;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * modified by Ingloba360
 * 
 */
public class GiuridicalEngageEditBean extends EntityProjectEditBean<GiuridicalEngages> {
	
	/**
	 * Stores actTypes value of entity.
	 */
	public class EntityWrapper {
		
		private String	actType;
		private Date	date;
		private String	number;
		private Double	amount;
		private String	text;
		private String hierarchicalLevelCode;
		private Long assigmentProcedure;

		/**
		 * Gets actType
		 * 
		 * @return actType the actType
		 */
		public String getActType()
		{
			return actType;
		}

		/**
		 * Sets actType
		 * 
		 * @param actType
		 *            the actType to set
		 */
		public void setActType(String actType)
		{
			this.actType = actType;
		}

		/**
		 * Gets date
		 * 
		 * @return date the date
		 */
		public Date getDate()
		{
			return date;
		}

		/**
		 * Sets date
		 * 
		 * @param date
		 *            the date to set
		 */
		public void setDate(Date date)
		{
			this.date = date;
		}

		/**
		 * Gets number
		 * 
		 * @return number the number
		 */
		

		/**
		 * Gets amount
		 * 
		 * @return amount the amount
		 */
		public Double getAmount()
		{
			return amount;
		}

		/**
		 * Gets number
		 * @return number the number
		 */
		public String getNumber()
		{
			return number;
		}

		/**
		 * Sets number
		 * @param number the number to set
		 */
		public void setNumber(String number)
		{
			this.number = number;
		}

		/**
		 * Sets amount
		 * 
		 * @param amount
		 *            the amount to set
		 */
		public void setAmount(Double amount)
		{
			this.amount = amount;
		}

		/**
		 * Gets text
		 * 
		 * @return text the text
		 */
		public String getText()
		{
			return text;
		}

		/**
		 * Sets text
		 * 
		 * @param text
		 *            the text to set
		 */
		public void setText(String text)
		{
			this.text = text;
		}	
		
		/**
		 * Gets hierarchicalLevelCode.
		 * @return hierarchicalLevelCode
		 */
		public String getHierarchicalLevelCode() {
			return hierarchicalLevelCode;
		}
		
		/**
		 * Sets hierarchicalLevelCode.
		 * @param hierarchicalLevelCode
		 */
		public void setHierarchicalLevelCode(String hierarchicalLevelCode) {
			this.hierarchicalLevelCode = hierarchicalLevelCode;
		}

		/**
		 * Gets assigmentProcedure.
		 * @return assigmentProcedure
		 */
		public Long getAssigmentProcedure() {
			return assigmentProcedure;
		}

		/**
		 * Sets assigmentProcedure
		 * @param assigmentProcedure
		 */
		public void setAssigmentProcedure(Long assigmentProcedure) {
			this.assigmentProcedure = assigmentProcedure;
		}
		
		
	}
	
	@Override
	public Boolean BeforeSave(){
		if(this.getNewDocTitle()==null || this.getNewDocTitle().isEmpty()){
			this.getContext().getViewRoot()
			.findComponent("EditForm:doc1link").getAttributes().put("style", "color: #E00000;");
			return false;
		}
		
		return true;
	}
	
	private static List<SelectItem>	actTypes	= new ArrayList<SelectItem>();

	private UploadedFile			newDocumentUpFile;

	private Documents				newDocument;

	private String					newDocTitle;

	private String					selectedCategory;

	private static List<SelectItem>	categories;
	
	private List<SelectItem> listHierarchicalLevel;
	
	private List<SelectItem> listAssigmentProcedures;

	/**
	 * Stores actType value of entity.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack()
	{
		this.goTo(PagesTypes.GIURIDICALENGAGELIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.FillTypes();
		fillCategories();
		this.fillHierarchicalLevel();
		this.fillListAssigmentProcedures();

		if (this.getViewState().get("newdocument") != null)
		{
			DocumentInfo doc = (DocumentInfo) this.getViewState().get(
					"newdocument");
			setNewDocTitle(doc.getName());
		}
		else
		{
			setNewDocTitle(null);
		}

		if (newDocument == null)
		{
			newDocument = new Documents();
			newDocument.setDocumentDate(new Date());
		}

		if (this.getSession().get("giuridicalEngage") != null)
		{
			this.setEntity(BeansFactory.GiuridicalEngages().Load(
					String.valueOf(this.getSession().get("giuridicalEngage"))));
			this.getEntityWrapperForStoreInViewState().setActType(
					String.valueOf(this.getEntity().getActType().getId()));
			this.getEntityWrapperForStoreInViewState().setDate(this.getEntity().getDate());
			this.getEntityWrapperForStoreInViewState().setNumber(this.getEntity().getNumber());
			this.getEntityWrapperForStoreInViewState().setAmount(this.getEntity().getAmount());
			this.getEntityWrapperForStoreInViewState().setText(this.getEntity().getText());
			
			if ((this.getEntityWrapperForStoreInViewState().getHierarchicalLevelCode() == null) ||
					this.getEntityWrapperForStoreInViewState().getHierarchicalLevelCode().equals(SelectItemHelper.getFirst().getValue())) {
				if ((this.getEntity().getHierarchicalLevelCode() != null) &&
						!this.getEntity().getHierarchicalLevelCode().equals(SelectItemHelper.getFirst().getValue())) {
					this.getEntityWrapperForStoreInViewState().setHierarchicalLevelCode(this.getEntity().getHierarchicalLevelCode());
				}
			}
			if ((this.getEntityWrapperForStoreInViewState().getAssigmentProcedure() == null) ||
					this.getEntityWrapperForStoreInViewState().getAssigmentProcedure().equals(SelectItemHelper.getFirst().getValue())) {
				if ((this.getEntity().getAssigmentProcedure() != null) &&
						!this.getEntity().getAssigmentProcedure().equals(SelectItemHelper.getFirst().getValue())) {
					this.getEntityWrapperForStoreInViewState().setAssigmentProcedure(this.getEntity().getAssigmentProcedure());
				}
			}
		}
		else
		{
			this.setEntity(new GiuridicalEngages());
		}
		if (this.getEntity().getTempDocument() != null)
		{
			setNewDocTitle(this.getEntity().getTempDocument().getName());
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
	 * @throws PersistenceBeanException
	 */
	private void FillTypes() throws PersistenceBeanException
	{
		if (actTypes == null)
		{
			actTypes = new ArrayList<SelectItem>();
		}
		else
		{
			actTypes.clear();
		}
		actTypes.add(SelectItemHelper.getFirst());
		for (ActTypes item : BeansFactory.ActTypes().Load())
		{
			actTypes.add(new SelectItem(String.valueOf(item.getId()), item
					.getValue()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		if (!this.getSessionBean().getIsActualSateAndAguRead())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException
	{
		GiuridicalEngages item = new GiuridicalEngages();
		if (this.getSession().get("giuridicalEngage") != null)
		{
			item = BeansFactory.GiuridicalEngages().Load(
					String.valueOf(this.getSession().get("giuridicalEngage")));
		}
		
		Projects pj = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));

		item.setActType(BeansFactory.ActTypes().Load(
				this.getEntityWrapperForStoreInViewState().getActType()));
		item.setAmount(this.getEntityWrapperForStoreInViewState().getAmount());
		item.setDate(this.getEntityWrapperForStoreInViewState().getDate());
		item.setNumber(this.getEntityWrapperForStoreInViewState().getNumber());
		item.setProject(this.getProject());
		//item.setHierarchicalLevelCode(this.getEntityWrapperForStoreInViewState().getHierarchicalLevelCode());
		item.setHierarchicalLevelCode(pj.getHierarchicalLevelCode());
		item.setAssigmentProcedure(this.getEntityWrapperForStoreInViewState().getAssigmentProcedure());
		item.setText(this.getEntityWrapperForStoreInViewState().getText());
		item.setCreatePartner(BeansFactory.CFPartnerAnagraphs().GetByUser(this.getCurrentUser().getId()));
		if (this.getViewState().get("newdocument") != null)
		{
			Documents documens = saveDoc();
			item.setTempDocument(documens);
		}
		BeansFactory.GiuridicalEngages().SaveInTransaction(item);
		this.getViewState().remove("EntityWrapper");
	}

	public void getNewDoc()
	{
		if (this.getEntity().getTempDocument() != null)
		{
			Documents tempDoc =this.getEntity().getTempDocument();
			DocumentInfo docinfo = new DocumentInfo(tempDoc.getDocumentDate(),
					tempDoc.getName(), tempDoc.getFileName(), tempDoc.getProtocolNumber(), 
					tempDoc.getCategory(), tempDoc.getIsPublic(), null); 
			FileHelper.sendFile(docinfo, false);
		}
	}

	public void deleteNewDoc() throws NumberFormatException, HibernateException, PersistenceBeanException
	{
		if (this.getViewState().get("newdocument") != null)
		{
			this.getViewState().put("newdocument", null);
			// this.getViewState().put("savedDocument", null);
		}

		if (this.getViewState().get("savedDocument") != null)
		{
			Transaction tr;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();

				if (getEntity().getTempDocument() != null)
				{
					BeansFactory.Documents().DeleteInTransaction(
							getEntity().getTempDocument());
					getEntity().setTempDocument(null);
					BeansFactory.GiuridicalEngages().SaveInTransaction(
							getEntity());
				}

				tr.commit();
				this.Page_Load();
				this.getViewState().put("savedDocument", null);
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
		Transaction tr = PersistenceSessionManager.getBean().getSession()
				.beginTransaction();

		if (getEntity().getTempDocument() != null)
		{
			BeansFactory.Documents().DeleteInTransaction(
					getEntity().getTempDocument());
			getEntity().setTempDocument(null);
			BeansFactory.GiuridicalEngages().SaveInTransaction(
					getEntity());
		}

		tr.commit();
		this.Page_Load();
		this.getViewState().put("savedDocument", null);
		setNewDocTitle(null);
		
	}

	private Documents saveDoc() throws HibernateException,
			PersistenceBeanException, IOException
	{
		Documents doc = null;
		DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
				"newdocument");

		String newFileName = FileHelper.newFileName(docinfo.getName());
		if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(
				newFileName)) == true)
		{
			doc = new Documents();
			doc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			doc.setName(docinfo.getName());
			doc.setUser(this.getCurrentUser());
			doc.setDocumentDate(docinfo.getDate());
			doc.setProject(this.getProject());
			doc.setProtocolNumber(docinfo.getProtNumber());
			doc.setSection(BeansFactory.Sections().Load(
					DocumentSections.AwardProcedure.getValue()));
			doc.setCategory(docinfo.getCategory());

			doc.setIsPublic(docinfo.getIsPublic());
			doc.setFileName(newFileName);
			BeansFactory.Documents().SaveInTransaction(doc);
		}

		return doc;
	}

	public void SaveNewDocument()
	{
		try
		{
			if (getNewDocumentUpFile() != null && getNewDocument() != null)
			{
				String fileName = FileHelper
						.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null
						&& !this.getSelectedCategory().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(getNewDocument()
						.getDocumentDate(), getNewDocumentUpFile().getName(),
						fileName, getNewDocument().getProtocolNumber(), cat,
						getNewDocument().getIsPublic(), null);
				this.getViewState().put("newdocument", doc);
			}
			this.Page_Load();
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
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

	}

	/**
	 * Sets actTypes
	 * 
	 * @param actTypes
	 *            the actTypes to set
	 */
	@SuppressWarnings("static-access")
	public void setActTypes(List<SelectItem> actTypes)
	{
		this.actTypes = actTypes;
	}

	/**
	 * Gets actTypes
	 * 
	 * @return actTypes the actTypes
	 */
	public List<SelectItem> getActTypes()
	{
		return actTypes;
	}

	/**
	 * Gets newDocumentUpFile
	 * 
	 * @return newDocumentUpFile the newDocumentUpFile
	 */
	public UploadedFile getNewDocumentUpFile()
	{
		return newDocumentUpFile;
	}

	/**
	 * Sets newDocumentUpFile
	 * 
	 * @param newDocumentUpFile
	 *            the newDocumentUpFile to set
	 */
	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile)
	{
		this.newDocumentUpFile = newDocumentUpFile;
	}

	/**
	 * Gets newDocument
	 * 
	 * @return newDocument the newDocument
	 */
	public Documents getNewDocument()
	{
		return newDocument;
	}

	/**
	 * Sets newDocument
	 * 
	 * @param newDocument
	 *            the newDocument to set
	 */
	public void setNewDocument(Documents newDocument)
	{
		this.newDocument = newDocument;
	}

	/**
	 * Gets newDocTitle
	 * 
	 * @return newDocTitle the newDocTitle
	 */
	public String getNewDocTitle()
	{
		return newDocTitle;
	}

	/**
	 * Sets newDocTitle
	 * 
	 * @param newDocTitle
	 *            the newDocTitle to set
	 */
	public void setNewDocTitle(String newDocTitle)
	{
		this.newDocTitle = newDocTitle;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory()
	{
		return selectedCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories()
	{
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	@SuppressWarnings("static-access")
	public void setCategories(List<SelectItem> categories)
	{
		this.categories = categories;
	}

	/**
	 * Gets entityWrapperForStoreInViewState
	 * 
	 * @return entityWrapperForStoreInViewState the
	 *         entityWrapperForStoreInViewState
	 */
	public EntityWrapper getEntityWrapperForStoreInViewState()
	{
		if (this.getViewState().get("EntityWrapper") == null)
		{
			this.getViewState().put("EntityWrapper", new EntityWrapper());
		}
		return (EntityWrapper) this.getViewState().get("EntityWrapper");
	}	
	/**
	 * Gets listHierarchicalLevel.
	 * @return listHierarchicalLevel
	 */
	public List<SelectItem> getListHierarchicalLevel() {
		return listHierarchicalLevel;
	}
	
	/**
	 * Sets listHierarchicalLevel.
	 * @param listHierarchicalLevel
	 */
	public void setListHierarchicalLevel(List<SelectItem> listHierarchicalLevel) {
		this.listHierarchicalLevel = listHierarchicalLevel;
	}
	
	/**
	 * Gets listAssigmentProcedures.
	 * @return listAssigmentProcedures
	 */
	public List<SelectItem> getListAssigmentProcedures() {
		return listAssigmentProcedures;
	}
	
	/**
	 * Sets listAssigmentProcedures.
	 * @param listAssigmentProcedures
	 */
	public void setListAssigmentProcedures(List<SelectItem> listAssigmentProcedures) {
		this.listAssigmentProcedures = listAssigmentProcedures;
	}
	
	/**
	 * Fill payment motivation list
	 */
	private void fillHierarchicalLevel() {
		this.listHierarchicalLevel = new ArrayList<SelectItem>();
		this.listHierarchicalLevel.add(SelectItemHelper.getFirst());
		
		Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);
		
		for (HierarchicalLevel hierarchicalLevel : HierarchicalLevel.values()) {
			SelectItem item = new SelectItem();
			item.setLabel(resourceBundle.getString(hierarchicalLevel.description));
			item.setValue(hierarchicalLevel.code);
			
			this.listHierarchicalLevel.add(item);
		}
	}
	
	/**
	 * Fill payment motivation list
	 * @throws PersistenceBeanException 
	 * @throws NumberFormatException 
	 * @throws HibernateException 
	 */
	private void fillListAssigmentProcedures() throws HibernateException, NumberFormatException, PersistenceBeanException {
		this.listAssigmentProcedures = new ArrayList<SelectItem>();
		this.listAssigmentProcedures.add(SelectItemHelper.getFirst());
		
		Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		List<AssignmentProcedures> list = BeansFactory.AssignmentProcedures().LoadByProjectAndUser(this.getProjectId(), String.valueOf(this.getSessionBean().getCurrentUser().getId()));
		
		for (AssignmentProcedures assignmentProcedure : list) {
			SelectItem item = new SelectItem();
			item.setLabel(assignmentProcedure.getProcedureDescription()+" - "+assignmentProcedure.getAssignedProcedureAmount());
			item.setValue(String.valueOf(assignmentProcedure.getId()));
			
			this.listAssigmentProcedures.add(item);
		}
	}
	
	
}
