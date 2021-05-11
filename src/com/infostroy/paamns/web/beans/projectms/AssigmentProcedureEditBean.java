/**
 * 
 */
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

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipology;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStep;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStepInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonForAbsenceCIG;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonsForDelay;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class AssigmentProcedureEditBean extends EntityProjectEditBean<AssignmentProcedures> {
	private String errorMessage;

	private String entityEditId;

	private static List<SelectItem> typologies;

	private static List<SelectItem> reasonsAbsenceCIG;

	private static List<SelectItem> differenceReasons;

	private List<ProcedureTipologyStep> steps;

	private List<ProcedureTipology> procedures;

	private List<ProcedureTipologyStepInfos> stepsInfo;

	private boolean isView;

	private Long stepEditId;

	private String docTitle;

	private UploadedFile documentUpFile;

	private Documents document;

	private static List<SelectItem> categories;

	private String selectedCategory;

	private Users user;

	private boolean validationFailed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave() {
		this.GoBack();
	}

	public Boolean BeforeSave() {
		this.validationFailed = false;
		for (ProcedureTipologyStepInfos step : getStepsInfo()) {
			if (step.getEstimationDate() == null) {
				this.setErrorMessage(Utils.getString("validatorCheckAllFields"));
				return false;
			}
		}

		this.checkCIGFields("EditForm1:cigProcedure", this.getEntity().getCigProcedure(), "EditForm1:reasonAbsenceCIG", this.getEntity().getReasonForAbsenceCIG().getId());

		if(this.validationFailed){
			return false;
		}
		return true;
	}

	private boolean checkCIGFields(String id1, Object value1, String id2, Object value2) {
		String str1 = null, str2 = null;

		if (value1 != null) {
			try {
				str1 = String.valueOf(value1);
			} catch (Exception e) {
			}
		}
		
		if(value2 != null){
			try {
				str2 = String.valueOf(value2);
			} catch (Exception e) {
			}
		}
		
		if(str1!=null && str1.trim().equals("9999") && (str2 == null || str2.isEmpty() || str2.trim().isEmpty() || str2.trim().equals("0"))){
			this.validationFailed = true;
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id2),
					Utils.getString("assignmentProcedureReasonAbsenceCIGError"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id1);
			this.getFailedValidation().add(id2);
			return false;
		} else {
			if (this.getFailedValidation().contains(id2)) {
				this.getFailedValidation().remove(id2);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id2),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack() {
		this.goTo(PagesTypes.ASSIGMENTPROCEDURELIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.FillLists();
		this.fillDocument();

		isView = this.getSession().get("assigmentProcedureView") != null;
	}

	/**
	 * @throws PersistenceBeanException
	 */

	private void fillDocument() {
		document = new Documents();
		document.setEditableDate(new Date());
		if (this.getViewState().get("document") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("document");

			setDocTitle(docinfo.getName());
			document.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			document.setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getDocument() != null) {
			setDocTitle(this.getEntity().getDocument().getName());
			document.setTitle(this.getEntity().getDocument().getTitle());
			document.setDocumentDate(this.getEntity().getDocument().getDocumentDate());
		} else {
			document.setDocumentDate(new Date());
			setDocTitle("");
		}

	}

	private void FillLists() throws PersistenceBeanException {
		if (this.getListSelectRoles() == null) {
			this.setListSelectRoles(new ArrayList<SelectItem>());
			fillRoles();
		} else {
			this.getListSelectRoles().clear();
			fillRoles();
		}
		if (categories == null) {
			categories = new ArrayList<SelectItem>();
			fillCategories();
		} else {
			categories.clear();
			fillCategories();
		}
		if (typologies == null) {
			typologies = new ArrayList<SelectItem>();
		} else {
			typologies.clear();
		}
		typologies.add(SelectItemHelper.getFirst());
		for (ProcedureTipology item : BeansFactory.ProcedureTipology().Load()) {
			typologies.add(new SelectItem(String.valueOf(item.getId()), item.getDescription()));
		}

		if (differenceReasons == null) {
			differenceReasons = new ArrayList<SelectItem>();
		} else {
			differenceReasons.clear();
		}
		differenceReasons.add(SelectItemHelper.getFirst());
		for (ReasonsForDelay item : BeansFactory.ReasonsForDelay().Load()) {
			differenceReasons.add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}

		if (reasonsAbsenceCIG == null) {
			reasonsAbsenceCIG = new ArrayList<SelectItem>();
		} else {
			reasonsAbsenceCIG.clear();
		}
		reasonsAbsenceCIG.add(SelectItemHelper.getFirst());
		for (ReasonForAbsenceCIG item : BeansFactory.ReasonForAbsenceCIG().Load()) {
			reasonsAbsenceCIG.add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		if (!this.getSessionBean().getIsActualSateAndAguRead()) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}

		if (this.getSession().get("assigmentProcedure") != null) {
			this.setEntity(BeansFactory.AssignmentProcedures()
					.Load(String.valueOf(this.getSession().get("assigmentProcedure"))));
			if (this.getEntity().getTipology() != null) {
				this.setFirstTypology(String.valueOf(this.getEntity().getTipology().getId()));
				this.setTypologyTitle(this.getEntity().getTipology().getDescription());
			}
			if(this.getEntity().getReasonForAbsenceCIG()==null){
				this.getEntity().setReasonForAbsenceCIG(new ReasonForAbsenceCIG());
			}

		} else {
			if (this.getEntity() == null) {
				this.setEntity(new AssignmentProcedures());
				this.getEntity().setReasonForAbsenceCIG(new ReasonForAbsenceCIG());

			}
		}
		
		this.setFailedValidation(new ArrayList<String>());

		if (this.getSession().get("saveTypology") != null) {
			this.setTypology((String) this.getSession().get("saveTypology"));
			this.getSession().put("saveTypology", null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		AssignmentProcedures item = new AssignmentProcedures();
		if (this.getSession().get("assigmentProcedure") != null) {
			item = BeansFactory.AssignmentProcedures()
					.Load(String.valueOf(this.getSession().get("assigmentProcedure")));
		}
		if (this.getEntity().getUser() == null) {
			item.setUser(this.getSessionBean().getCurrentUser());
		}
		item.setProcedureDescription(this.getEntity().getProcedureDescription());
		item.setPublicationDate(this.getEntity().getPublicationDate());
		item.setAwardDate(this.getEntity().getAwardDate());
		// item.setAssignedProcedureAmount(this.getAssignedProcedureAmount());
		item.setAssignedProcedureAmount(this.getEntity().getAssignedProcedureAmount());
		// item.setBaseProcedureAmount(this.getBaseProcedureAmount());
		item.setBaseProcedureAmount(this.getEntity().getBaseProcedureAmount());
		// item.setCigProcedure(this.getCigProcedure());
		item.setCigProcedure(this.getEntity().getCigProcedure());
		item.setDescription(this.getDescription());
		item.setNote(this.getEntity().getNote().length() > 254 ? this.getEntity().getNote().substring(0, 254)
				: this.getEntity().getNote());
		item.setTipology(BeansFactory.ProcedureTipology().Load(this.getTypology()));
		item.setReasonForAbsenceCIG(
				BeansFactory.ReasonForAbsenceCIG().Load(this.getEntity().getReasonForAbsenceCIG().getId()));
		item.setProject(this.getProject());
		item.setDifferentPercentage(((item.getBaseProcedureAmount() - item.getAssignedProcedureAmount()) * 100)
				/ item.getBaseProcedureAmount());
		BeansFactory.AssignmentProcedures().SaveInTransaction(item);

		BeansFactory.ProcedureTipologyStepInfos().deleteByProcedureInTransaction(item.getId());

		for (ProcedureTipologyStepInfos step : getStepsInfo()) {
			ProcedureTipologyStepInfos newItem = new ProcedureTipologyStepInfos();
			newItem.setCompetencePeople(step.getCompetencePeople());
			newItem.setDescription(step.getDescription());
			newItem.setEstimationDate(step.getEstimationDate());
			newItem.setDifferenceReason(step.getDifferenceReason());
			newItem.setNote(step.getNote());
			newItem.setAmount(step.getAmount());
			newItem.setProcedure(item);
			newItem.setRealDate(step.getRealDate());
			newItem.setTipologyStep(step.getTipologyStep());
			BeansFactory.ProcedureTipologyStepInfos().SaveInTransaction(newItem);
		}
		Documents doc = null;
		if (this.getViewState().get("document") != null) {

			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("document");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				doc = new Documents();
				doc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				doc.setName(docinfo.getName());
				doc.setUser(this.getSessionBean().getCurrentUser());
				doc.setSection(BeansFactory.Sections().Load(DocumentSections.AwardProcedure.getValue()));
				doc.setProject(this.getProject());
				doc.setDocumentDate(docinfo.getDate());
				doc.setProtocolNumber(docinfo.getProtNumber());
				doc.setIsPublic(docinfo.getIsPublic());
				doc.setCategory(docinfo.getCategory());
				if (this.getDocRole() != null && !this.getDocRole().isEmpty()) {
					doc.setUploadRole(Integer.parseInt(this.getDocRole()));
				}
				doc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(doc);
			}
		}
		if (doc != null) {
			item.setDocument(doc);
		}
		if (this.getViewState().get("documentToDel") != null) {
			BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("documentToDel"));
			item.setDocument(doc);
		}
	}

	/**
	 * @param event
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 */
	public void typologyChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setTypology(String.valueOf(event.getNewValue()));
		// this.getSession().put("saveTypology", getTypology());
		this.setEntityEditId(null);

		// this.goTo(PagesTypes.ASSIGMENTPROCEDUREEDIT);
		this.Page_Load();
		setStepsInfo(getStepsInfo());
	}

	public void saveStepInfo() {
		try {
			setEntityEditId(null);
			if (!this.getEntity().isNew()) {
				String typology = (String) this.getViewState().get("savedTypology");
				if (String.valueOf(getStepsInfo().get(0).getTipologyStep().getProcedureTipologyStep().getId())
						.equals(typology)) {
					Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					BeansFactory.ProcedureTipologyStepInfos().deleteByProcedureInTransaction(this.getEntityId());

					for (ProcedureTipologyStepInfos step : getStepsInfo()) {
						ProcedureTipologyStepInfos newItem = new ProcedureTipologyStepInfos();
						newItem.setCompetencePeople(step.getCompetencePeople());
						newItem.setDescription(step.getDescription());
						newItem.setEstimationDate(step.getEstimationDate());
						newItem.setDifferenceReason(step.getDifferenceReason());
						newItem.setNote(step.getNote());
						newItem.setAmount(step.getAmount());
						newItem.setProcedure(this.getEntity());
						newItem.setRealDate(step.getRealDate());
						newItem.setTipologyStep(step.getTipologyStep());
						BeansFactory.ProcedureTipologyStepInfos().SaveInTransaction(newItem);
					}

					tr.commit();
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * Sets typologys
	 * 
	 * @param typologys
	 *            the typologys to set
	 */
	public void setTypologies(List<SelectItem> typologys) {
		AssigmentProcedureEditBean.typologies = typologys;
	}

	/**
	 * Gets typologys
	 * 
	 * @return typologys the typologys
	 */
	public List<SelectItem> getTypologies() {
		return typologies;
	}

	/**
	 * Sets typology
	 * 
	 * @param typology
	 *            the typology to set
	 */
	public void setTypology(String typology) {
		try {
			// this.typology = typology;
			this.getViewState().put("Typology", typology);
			if (this.getTypology() != null && !this.getTypology().isEmpty()) {
				ProcedureTipology item;
				item = BeansFactory.ProcedureTipology().Load(this.getTypology());

				this.setSteps(BeansFactory.ProcedureTipologyStep().getByTypology(this.getTypology()));
				this.setCode(String.valueOf(item.getCode()));
				this.setDescription(item.getDescription());
				if (this.getProcedures() == null) {
					this.setProcedures(new ArrayList<ProcedureTipology>());
				} else {
					this.getProcedures().clear();
				}
				this.getProcedures().add(item);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFirstTypology(String typology) {
		try {
			// this.typology = typology;
			this.getViewState().put("Typology", typology);
			this.getViewState().put("savedTypology", typology);
			if (this.getTypology() != null && !this.getTypology().isEmpty()) {
				ProcedureTipology item;
				item = BeansFactory.ProcedureTipology().Load(this.getTypology());
				this.setFirstSteps(BeansFactory.ProcedureTipologyStep().getByTypology(this.getTypology()));
				this.setCode(String.valueOf(item.getCode()));
				this.setDescription(item.getDescription());
				if (this.getProcedures() == null) {
					this.setProcedures(new ArrayList<ProcedureTipology>());
				} else {
					this.getProcedures().clear();
				}
				this.getProcedures().add(item);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets typology
	 * 
	 * @return typology the typology
	 */
	public String getTypology() {
		return (String) this.getViewState().get("Typology");
	}

	public void setReasonAbsenceCIG(String reasonAbsenceCIG) {
		this.getViewState().put("reasonAbsenceCIG", reasonAbsenceCIG);
	}

	public String getReasonAbsenceCIG() {
		return (String) this.getViewState().get("reasonAbsenceCIG");
	}

	/**
	 * Sets isEdit
	 * 
	 * @param isEdit
	 *            the isEdit to set
	 */
	public void setIsView(boolean isEdit) {
		this.isView = isEdit;
	}

	/**
	 * Gets isEdit
	 * 
	 * @return isEdit the isEdit
	 */
	public boolean getIsView() {
		return isView;
	}

	/**
	 * Sets steps
	 * 
	 * @param steps
	 *            the steps to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setSteps(List<ProcedureTipologyStep> steps)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (getStepsInfo() == null) {
			this.setStepsInfo(new ArrayList<ProcedureTipologyStepInfos>());
		}

		if (getLastTypology() != null && steps != null && !steps.isEmpty()
				&& getLastTypology().equals(String.valueOf(steps.get(0).getProcedureTipologyStep().getId()))) {
			return;
		} else if (steps != null && !steps.isEmpty()) {
			setLastTypology(String.valueOf(steps.get(0).getProcedureTipologyStep().getId()));
			this.setStepsInfo(new ArrayList<ProcedureTipologyStepInfos>());
		} else {
			this.setStepsInfo(new ArrayList<ProcedureTipologyStepInfos>());
		}

		for (ProcedureTipologyStep item : steps) {
			if (this.getEntity().getId() != null) {
				ProcedureTipologyStepInfos fromDB = BeansFactory.ProcedureTipologyStepInfos()
						.getByProcedureAndTypologyStep(this.getEntity().getId(), item.getId());
				if (fromDB != null) {
					this.getStepsInfo().add(fromDB);
				} else {
					this.getStepsInfo().add(new ProcedureTipologyStepInfos(item));
				}
			} else {
				this.getStepsInfo().add(new ProcedureTipologyStepInfos(item));
			}
		}
		this.steps = steps;
	}

	public void setFirstSteps(List<ProcedureTipologyStep> steps)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (getStepsInfo() == null) {
			this.setStepsInfo(new ArrayList<ProcedureTipologyStepInfos>());
		}

		if (getLastTypology() != null) {
			return;
		} else if (steps != null && !steps.isEmpty()) {
			setLastTypology(String.valueOf(steps.get(0).getProcedureTipologyStep().getId()));
		}
		for (ProcedureTipologyStep item : steps) {
			if (this.getEntity().getId() != null) {
				ProcedureTipologyStepInfos fromDB = BeansFactory.ProcedureTipologyStepInfos()
						.getByProcedureAndTypologyStep(this.getEntity().getId(), item.getId());
				if (fromDB != null) {
					this.getStepsInfo().add(fromDB);
				} else {
					this.getStepsInfo().add(new ProcedureTipologyStepInfos(item));
				}
			} else {
				this.getStepsInfo().add(new ProcedureTipologyStepInfos(item));
			}
		}
		this.steps = steps;
	}

	public void SaveDocument() {
		try {
			if (getDocumentUpFile() != null && document != null) {
				String fileName = FileHelper.newTempFileName(getDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(document.getDocumentDate(), getDocumentUpFile().getName(), fileName,
						document.getProtocolNumber(), cat, document.getIsPublic(), document.getSignflag());
				this.getViewState().put("document", doc);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

	}

	public void getDoc() {
		if (this.getViewState().get("document") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("document");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getDocument().getName(),
					this.getEntity().getDocument().getFileName(), null, this.getEntity().getDocument().getSignflag()), false);
		}
	}

	public void deleteDoc() {
		try {
			if (this.getViewState().get("document") != null) {
				this.getViewState().put("document", null);
			} else if (this.getEntity().getDocument() != null) {
				this.getViewState().put("documentToDel",
						BeansFactory.Documents().Load(this.getEntity().getDocument().getId()).getId());
				this.getEntity().setDocument(null);
			}

			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	private void fillRoles() throws HibernateException, PersistenceBeanException {

		this.getListSelectRoles().add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();
		for (UserRoles userRole : UserRolesBean.GetByUser(String.valueOf(this.getCurrentUser().getId()))) {
			if (userRole.getRole().getCode().equals("DAEC")) {
				continue;
			}
			if (temp.add(userRole.getRole().getCode())) {
				UploadDocumentRoleType roleType = UploadDocumentRoleType.getByName(userRole.getRole().getCode());
				if (roleType != null) {
					this.getListSelectRoles()
							.add(new SelectItem(String.valueOf(roleType.getId()), roleType.getDisplayName()));
				}
			}
		}
	}

	private void fillCategories() throws PersistenceBeanException {
		categories.add(SelectItemHelper.getFirst());
		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	/**
	 * Gets steps
	 * 
	 * @return steps the steps
	 */
	public List<ProcedureTipologyStep> getSteps() {

		return steps;
	}

	/**
	 * Sets stepsInfo
	 * 
	 * @param stepsInfo
	 *            the stepsInfo to set
	 */
	public void setStepsInfo(List<ProcedureTipologyStepInfos> stepsInfo) {
		this.getViewState().put("ProcedureTipologyStepInfos", stepsInfo);
		this.stepsInfo = stepsInfo;
	}

	/**
	 * Gets stepsInfo
	 * 
	 * @return stepsInfo the stepsInfo
	 */
	@SuppressWarnings("unchecked")
	public List<ProcedureTipologyStepInfos> getStepsInfo() {
		if (this.getViewState().get("ProcedureTipologyStepInfos") == null) {
			this.getViewState().put("ProcedureTipologyStepInfos", new ArrayList<ProcedureTipologyStepInfos>());
		}

		stepsInfo = (List<ProcedureTipologyStepInfos>) this.getViewState().get("ProcedureTipologyStepInfos");
		return stepsInfo;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.getViewState().put("description", description);
	}

	/**
	 * Gets description
	 * 
	 * @return description the description
	 */
	public String getDescription() {
		return (String) this.getViewState().get("description");
	}

	/**
	 * Sets code
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.getViewState().put("code", code);
	}

	/**
	 * Gets code
	 * 
	 * @return code the code
	 */
	public String getCode() {
		return (String) this.getViewState().get("code");
	}

	/**
	 * Sets typologyTitle
	 * 
	 * @param typologyTitle
	 *            the typologyTitle to set
	 */
	public void setTypologyTitle(String typologyTitle) {
		this.getViewState().put("typologyTitle", typologyTitle);
	}

	/**
	 * Gets typologyTitle
	 * 
	 * @return typologyTitle the typologyTitle
	 */
	public String getTypologyTitle() {
		return (String) this.getViewState().get("typologyTitle");
	}

	/**
	 * @return
	 */
	public String getLastTypology() {
		if (this.getViewState().get("LastTypology") == null) {
			return null;
		}

		return (String) this.getViewState().get("LastTypology");
	}

	/**
	 * @param lastTypology
	 */
	public void setLastTypology(String lastTypology) {
		this.getViewState().put("LastTypology", lastTypology);
	}

	/**
	 * Sets stepEditId
	 * 
	 * @param stepEditId
	 *            the stepEditId to set
	 */
	public void setStepEditId(Long stepEditId) {
		this.stepEditId = stepEditId;
	}

	/**
	 * Gets stepEditId
	 * 
	 * @return stepEditId the stepEditId
	 */
	public Long getStepEditId() {
		return stepEditId;
	}

	/**
	 * Sets entityEditId
	 * 
	 * @param entityEditId
	 *            the entityEditId to set
	 */
	public void setEntityEditId(String entityEditId) {
		this.getViewState().put("entityEditId", entityEditId);
		this.entityEditId = entityEditId;
	}

	/**
	 * Gets entityEditId
	 * 
	 * @return entityEditId the entityEditId
	 */
	public String getEntityEditId() {
		if (this.getViewState().get("entityEditId") != null) {
			entityEditId = (String) this.getViewState().get("entityEditId");
		}
		return entityEditId;
	}

	/**
	 * Sets differenceReasons
	 * 
	 * @param differenceReasons
	 *            the differenceReasons to set
	 */
	public void setDifferenceReasons(List<SelectItem> differenceReasons) {
		AssigmentProcedureEditBean.differenceReasons = differenceReasons;
	}

	/**
	 * Gets differenceReasons
	 * 
	 * @return differenceReasons the differenceReasons
	 */
	public List<SelectItem> getDifferenceReasons() {
		return differenceReasons;
	}

	/**
	 * Sets differenceReasons
	 * 
	 * @param differenceReasons
	 *            the differenceReasons to set
	 */
	public void setReasonsAbsenceCIG(List<SelectItem> reasonsAbsenceCIG) {
		AssigmentProcedureEditBean.reasonsAbsenceCIG = reasonsAbsenceCIG;
	}

	/**
	 * Gets differenceReasons
	 * 
	 * @return differenceReasons the differenceReasons
	 */
	public List<SelectItem> getReasonsAbsenceCIG() {
		return reasonsAbsenceCIG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#getEntity()
	 */
	public AssignmentProcedures getEntity() {
		this.entity = (AssignmentProcedures) this.getViewState().get("costDefEntity");
		return this.entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityEditBean#setEntity(java.lang.Object)
	 */
	public void setEntity(AssignmentProcedures entity) {
		this.getViewState().put("costDefEntity", entity);
		this.entity = entity;
	}

	/**
	 * Sets procedures
	 * 
	 * @param procedures
	 *            the procedures to set
	 */
	public void setProcedures(List<ProcedureTipology> procedures) {
		this.getViewState().put("Procedures", procedures);
		this.procedures = procedures;
	}

	/**
	 * Gets procedures
	 * 
	 * @return procedures the procedures
	 */
	@SuppressWarnings("unchecked")
	public List<ProcedureTipology> getProcedures() {
		this.procedures = (List<ProcedureTipology>) this.getViewState().get("Procedures");
		return procedures;
	}

	/**
	 * Sets errorMessage
	 * 
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets errorMessage
	 * 
	 * @return errorMessage the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Gets docTitle
	 * 
	 * @return docTitle the docTitle
	 */
	public String getDocTitle() {
		return docTitle;
	}

	/**
	 * Sets docTitle
	 * 
	 * @param docTitle
	 *            the docTitle to set
	 */
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	/**
	 * Gets documentUpFile
	 * 
	 * @return documentUpFile the documentUpFile
	 */
	public UploadedFile getDocumentUpFile() {
		return documentUpFile;
	}

	/**
	 * Sets documentUpFile
	 * 
	 * @param documentUpFile
	 *            the documentUpFile to set
	 */
	public void setDocumentUpFile(UploadedFile documentUpFile) {
		this.documentUpFile = documentUpFile;
	}

	/**
	 * Gets document
	 * 
	 * @return document the document
	 */
	public Documents getDocument() {
		return document;
	}

	/**
	 * Sets document
	 * 
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Documents document) {
		this.document = document;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories() {
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SelectItem> categories) {
		AssigmentProcedureEditBean.categories = categories;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getDocRole() {
		return (String) this.getViewState().get("selectedDocRole");
	}

	public void setDocRole(String entity) {
		this.getViewState().put("selectedDocRole", entity);
	}

	/**
	 * Sets listSelectRoles
	 * 
	 * @param listSelectRoles
	 *            the listSelectRoles to set
	 */
	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		this.getViewState().put("listSelectRoles", listSelectRoles);
	}

	/**
	 * Gets listSelectRoles
	 * 
	 * @return listSelectRoles the listSelectRoles
	 */
	public List<SelectItem> getListSelectRoles() {
		return (List<SelectItem>) this.getViewState().get("listSelectRoles");
	}

	/**
	 * Gets baseProcedureAmount
	 * 
	 * @return baseProcedureAmount the baseProcedureAmount
	 */
	public Double getBaseProcedureAmount() {
		return (Double) this.getViewState().get("baseProcedureAmount");
	}

	/**
	 * Sets baseProcedureAmount
	 * 
	 * @param baseProcedureAmount
	 *            the baseProcedureAmount to set
	 */
	public void setBaseProcedureAmount(Double baseProcedureAmount) {
		this.getViewState().put("baseProcedureAmount", baseProcedureAmount);
	}

	/**
	 * Gets assignedProcedureAmount
	 * 
	 * @return assignedProcedureAmount the assignedProcedureAmount
	 */
	public Double getAssignedProcedureAmount() {
		return (Double) this.getViewState().get("assignedProcedureAmount");
	}

	/**
	 * Sets assignedProcedureAmount
	 * 
	 * @param assignedProcedureAmount
	 *            the assignedProcedureAmount to set
	 */
	public void setAssignedProcedureAmount(Double assignedProcedureAmount) {
		this.getViewState().put("assignedProcedureAmount", assignedProcedureAmount);
	}

	/**
	 * Gets cigProcedure
	 * 
	 * @return cigProcedure the cigProcedure
	 */
	public String getCigProcedure() {
		return (String) this.getViewState().get("cigProcedure");
	}

	/**
	 * Sets cigProcedure
	 * 
	 * @param cigProcedure
	 *            the cigProcedure to set
	 */
	public void setCigProcedure(String cigProcedure) {
		this.getViewState().put("cigProcedure", cigProcedure);
	}

	/**
	 * Sets isView
	 * 
	 * @param isView
	 *            the isView to set
	 */
	public void setView(boolean isView) {
		this.isView = isView;
	}

	/**
	 * Gets user
	 * 
	 * @return user
	 */
	public Users getUser() {
		return user;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            is user to set
	 */
	public void setUser(Users user) {
		this.user = user;
	}


	/**
	 * @param arrayList
	 */
	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}


}
