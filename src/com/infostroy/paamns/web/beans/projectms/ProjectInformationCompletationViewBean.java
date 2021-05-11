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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.ProjectInformationCompletationMailSender;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DocumentsAdm;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.EconomicActivities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.FinancingForms;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Nickolay Sumyatin nikolay.sumyatin@infostroy.com.ua InfoStroy Co.,
 *         22.03.2010.
 * 
 */
public class ProjectInformationCompletationViewBean extends EntityProjectEditBean<ProjectIformationCompletations> {
	private Boolean canEdit;

	private String durationError;

	private String decreaseError;

	/**
	 * Document uploading
	 */
	private String picDocTitle;

	private UploadedFile picDocumentUpFile;

	/**
	 * Document admission uploading
	 */
	private String picDocAdmTitle;

	private UploadedFile picDocumentAdmUpFile;

	/**
	 * Stores project fields
	 */
	private String code;

	private String title;

	private String projectTypology;

	private ProjectStates projectStates;

	private String asse;

	private String specificGoal;

	private String qsnGoal;

	private String priorReasons;

	private String activationProcedure;

	private String cup;

	private String cpt;

	private boolean yearDecreased = false;

	/**
	 * List for comboboxes
	 */
	private List<SelectItem> listYearDuration;

	private List<SelectItem> listYears;

	private List<SelectItem> listFinancingCode;

	private List<SelectItem> listTerritorialCode;

	private List<SelectItem> listActualCode;

	private List<SelectItem> listEconomicActivity;

	private List<SelectItem> listFinancingForms;

	private List<SelectItem> listSelectRoles;

	private List<SelectItem> listPartners;

	private boolean isNewEntity;

	private boolean lastSave;

	List<InfoObject> listPICMS;

	private boolean canChangeDuration;

	private List<SelectItem> categories;

	private String selectedCategory;

	private String selectedAdmCategory;

	@Override
	public void AfterSave() {

		Transaction tr = null;
		try {
			tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();
			ProjectInformationCompletationMailSender flowSFSendMailSender = new ProjectInformationCompletationMailSender(
					listPICMS);
			mails.addAll(flowSFSendMailSender.completeMails(PersistenceSessionManager.getBean().getSession()));

			if (mails != null && !mails.isEmpty()) {
				for (Mails mail : mails) {
					BeansFactory.Mails().SaveInTransaction(mail);
				}

			}

		} catch (Exception e) {
			if (tr != null) {
				tr.rollback();
			}
			log.error(e);
		} finally {
			if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
				tr.commit();
			}
		}

		this.setLastSave(true);
		this.GoBack();
	}

	@Override
	public void GoBack() {
		this.clearSession();

		if (!this.getIsNewEntity()) {
			if (this.getViewState().get("gotobudgets") == null) {
				this.goTo(PagesTypes.PROJECTINDEX);
			} else {
				this.goTo(PagesTypes.BUDGETLIST);
			}
		} else {
			if (this.getLastSave()) {
				try {
					if (!BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId()).isEmpty()) {
						this.goTo(PagesTypes.PARTNERBUDGETLIST);
					} else {
						this.goTo(PagesTypes.PROJECTINDEX);
					}
				} catch (HibernateException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (PersistenceBeanException e) {
					e.printStackTrace();
				}
			} else {
				this.goTo(PagesTypes.PROJECTINDEX);
			}
		}
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setEntity(new ProjectIformationCompletations());
		this.fillProjectInfo();
		this.loadEntity();
		this.setLastSave(false);
		// this.setCanChangeDuration(!this.getProjectState().equals(ProjectState.Actual));

		if (this.getSessionBean().isSTCW() || this.getSessionBean().isAGUW()) {
			this.canEdit = true;
		} else {
			this.canEdit = false;
		}

		if (this.getSessionBean().getIsAguRead()) {
			this.canEdit = false;
		}
		this.initDoc();
		this.initDocAdm();
		this.FillComboboxes();
	}

	private void loadEntity() throws PersistenceBeanException {

		ProjectIformationCompletations entityToLoad = BeansFactory.ProjectIformationCompletations()
				.LoadByProject(String.valueOf(this.getProject().getId()));

		if (entityToLoad != null) {
			this.setEntity(entityToLoad);
			this.setIsNewEntity(false);

			if (this.getPicDescription() == null) {
				this.setPicDescription(entityToLoad.getDescription());
			}
			
			if (this.getPicDescriptionEng() == null) {
				this.setPicDescriptionEng(entityToLoad.getDescriptionEng());
			}			

			if (this.getPicYearDuration() == null) {
				String yearDuration = String.valueOf(entityToLoad.getYearDuration());
				this.setPicYearDuration(yearDuration);
				this.getViewState().put("oldYearDuration", yearDuration);
			}

			if (this.getPicMonthDuration() == null) {
				String monthDuration = String.valueOf(entityToLoad.getMonthDuration());
				this.setPicMonthDuration(monthDuration);
				this.getViewState().put("oldMonthDuration", monthDuration);
			}

			if (this.getPicCUPcode() == null) {
				this.setPicCUPcode(entityToLoad.getCupCode());
			}

			if (this.getPicNote() == null) {
				this.setPicNote(entityToLoad.getNote());
			}
			
			if (this.getPicResults() == null) {
				this.setPicResults(entityToLoad.getResults());
			}
			
			if (this.getPicEngResults() == null) {
				this.setPicEngResults(entityToLoad.getEngResults());
			}
			
			if (this.getPicUrl() == null) {
				this.setPicUrl(entityToLoad.getUrl());
			}

			if (this.getStartYear() == null) {
				this.setStartYear(entityToLoad.getStartYear());
			}
			
			if (this.getEndYear() == null) {
				this.setEndYear(entityToLoad.getEndYear());
			}

			if (this.getDateAdmissionToFinancing() == null) {
				this.setDateAdmissionToFinancing(entityToLoad.getDateAdmissionToFinancing());
			}
			
			if(this.getDateSubmission() == null){
				this.setDateSubmission(entityToLoad.getDateOfSubmission());
			}

			if (this.getPicEconomicActivity() == null) {
				this.setPicEconomicActivity(entityToLoad.getEconomicActivity().getId().toString());
			}

			if (this.getPicFinancingCode() == null) {
				this.setPicFinancingCode(entityToLoad.getFinancingCode());
			}

			if (this.getPicTerritorialCode() == null) {
				this.setPicTerritorialCode(entityToLoad.getTerritorialCode());
			}

			if (this.getPicActualCode() == null) {
				this.setPicActualCode(entityToLoad.getActualCode());
			}

			if (this.getFinancingForm() == null && entityToLoad.getFinancingForms() != null) {
				this.setFinancingForm(entityToLoad.getFinancingForms().getId().toString());
			}

//			if (this.getPicInboundAmount() == null) {
//				this.setPicInboundAmount(entityToLoad.getInboundAmmount());
//			}

//			if (this.getPicOtherFound() == null) {
//				this.setPicOtherFound(entityToLoad.getOtherFound());
//			}

//			if (this.getPicViewL4432001() == null) {
//				this.setPicViewL4432001(entityToLoad.getL442001());
//			}
//
//			if (this.getPicBigProject() == null) {
//				this.setPicBigProject(entityToLoad.getBigProject());
//			}

//			if (this.getPicActionPlan() == null) {
//				this.setPicActionPlan(entityToLoad.getActionPlan());
//			}

//			if (this.getPicOccupationalFinancing() == null) {
//				this.setPicOccupationalFinancing(entityToLoad.getOccupationalFinancing());
//			}

		} else {
			this.setEntity(new ProjectIformationCompletations());
			this.setIsNewEntity(true);
		}
	}

	private void fillProjectInfo() throws PersistenceBeanException {
		this.setCode(this.getProject().getCode());
		this.setProjectStates(this.getProject().state);
		this.setTitle(this.getProject().getTitle());

		if (this.getProject().getTypology() != null) {
			this.setProjectTypology(this.getProject().getTypology().getValue());
		}

		if (this.getProject().getSpecificGoal() != null) {
			this.setSpecificGoal(String.valueOf(this.getProject().getSpecificGoal().getCode()));
		}

		if (this.getProject().getQsnGoal() != null) {
			this.setQsnGoal(String.valueOf(this.getProject().getQsnGoal().getCodeSpecificObjective()));
		}

		this.setAsse(String.valueOf(this.getProject().getAsse()));

		if (this.getProject().getPrioritaryReason() != null) {
			this.setPriorReasons(String.valueOf(this.getProject().getPrioritaryReason().getCode()));
		}

		if (this.getProject().getActivationProcedure() != null) {
			this.setActivationProcedure(this.getProject().getActivationProcedure().getCode());
		}

		if (this.getProject().getCup() != null) {
			StringBuilder sb = new StringBuilder();
			if (this.getProject().getCup().getParent() != null) {
				if (this.getProject().getCup().getParent().getParent() != null) {
					sb.append(this.getProject().getCup().getParent().getParent().getCode());
				}
				if (sb.length() > 0) {
					sb.append(".");
				}
				sb.append(this.getProject().getCup().getParent().getCode());
			}
			if (sb.length() > 0) {
				sb.append(".");
			}
			sb.append(this.getProject().getCup().getCode());
			sb.append(" ");
			sb.append(this.getProject().getCup().getValue());

			this.setCup(sb.toString());

			// this.setCup(BeansFactory.CUP().Load(this.getProject().getCup().getId()).getValue());
		}

		/*
		 * if(this.getProject().getCup().getParent() != null) {
		 * this.setCup2(BeansFactory
		 * .CUP().Load(this.getProject().getCup().getParent
		 * ().getId()).getValue()); }
		 * 
		 * if(this.getProject().getCup().getParent() != null &&
		 * this.getProject().getCup().getParent().getParent() != null) {
		 * this.setCup3
		 * (BeansFactory.CUP().Load(this.getProject().getCup().getParent
		 * ().getParent().getId()).getValue()); }
		 */

		if (this.getProject().getCpt() != null) {
			this.setCpt(this.getProject().getCpt().getValue());
		}
	}

	public void deletePicDoc() {
		try {
			if (this.getViewState().get("picdocument") != null) {
				this.getViewState().put("picdocument", null);
			} else if (this.getEntity().getPicDocument() != null) {
				this.getViewState().put("picdocumentToDel",
						BeansFactory.Documents().Load(this.getEntity().getPicDocument().getId()).getId());
				this.getEntity().setPicDocument(null);
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

	public void getPicDoc() {
		if (this.getViewState().get("picdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getPicDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getPicDocument().getName(),
					this.getEntity().getPicDocument().getFileName(), null, null, null), false);
		}
	}

	public void SavePicDocument() {
		try {
			if (this.getPicDocumentUpFile() != null) {
				String fileName = FileHelper.newTempFileName(this.getPicDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getPicDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getPicDocument().getDocumentDate(),
						getPicDocumentUpFile().getName(), fileName, this.getPicDocument().getProtocolNumber(), cat,
						this.getPicDocument().getIsPublic(), null);
				try {
					getPicDocument().setUploadRole(Integer.parseInt(this.getRole()));
				} catch (Exception e) {
					log.error(e);
				}
				this.getViewState().put("picdocument", doc);
				this.setSelectedCategory(null);
				this.setRole(null);
				this.setSelectedPartner(null);
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

	private void initDoc() {
		if (this.getPicDocument() == null) {
			this.setPicDocument(new Documents());
		}
		this.getPicDocument().setEditableDate(new Date());

		if (this.getViewState().get("picdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocument");

			this.setPicDocTitle(docinfo.getName());
			this.getPicDocument().setUser(this.getSessionBean().getCurrentUser());
			this.getPicDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			this.getPicDocument().setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getPicDocument() != null && this.getViewState().get("picdocumentToDel") == null) {
			this.getPicDocument().setTitle(this.getEntity().getPicDocument().getTitle());
			this.getPicDocument().setDocumentDate(this.getEntity().getPicDocument().getDocumentDate());
			this.setPicDocTitle(this.getEntity().getPicDocument().getName());
		} else {
			this.getPicDocument().setDocumentDate(new Date());
			this.setPicDocTitle("");
		}
	}

	public void getPicDocAdm() {
		if (this.getViewState().get("picdocumentadm") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocumentadm");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getPicDocumentAdm() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getPicDocumentAdm().getName(),
					this.getEntity().getPicDocumentAdm().getFileName(), null, null, null), false);
		}
	}

	public void SavePicDocumentAdm() {
		try {
			if (this.getPicDocumentAdmUpFile() != null) {
				String fileName = FileHelper.newTempFileName(this.getPicDocumentAdmUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getPicDocumentAdmUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedAdmCategory() != null && !this.getSelectedAdmCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedAdmCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getPicDocumentAdm().getDocumentDate(),
						getPicDocumentAdmUpFile().getName(), fileName, this.getPicDocumentAdm().getProtocolNumber(),
						cat, this.getPicDocumentAdm().getIsPublic(), null);
				try {
					getPicDocumentAdm().setUploadRole(Integer.parseInt(this.getAdmRole()));
				} catch (Exception e) {
					log.error(e);
				}
				this.getViewState().put("picdocumentadm", doc);
				this.setSelectedAdmCategory(null);
				this.setAdmRole(null);
				this.setSelectedAdmPartner(null);
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

	private void initDocAdm() {
		if (this.getPicDocumentAdm() == null) {
			this.setPicDocumentAdm(new DocumentsAdm());
		}
		this.getPicDocumentAdm().setEditableDate(new Date());

		if (this.getViewState().get("picdocumentadm") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocumentadm");

			this.setPicDocAdmTitle(docinfo.getName());
			this.getPicDocumentAdm().setUser(this.getSessionBean().getCurrentUser());
			this.getPicDocumentAdm().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			this.getPicDocumentAdm().setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getPicDocumentAdm() != null
				&& this.getViewState().get("picdocumentadmToDel") == null) {
			this.getPicDocumentAdm().setTitle(this.getEntity().getPicDocumentAdm().getTitle());
			this.getPicDocumentAdm().setDocumentDate(this.getEntity().getPicDocumentAdm().getDocumentDate());
			this.setPicDocAdmTitle(this.getEntity().getPicDocumentAdm().getName());
		} else {
			this.getPicDocumentAdm().setDocumentDate(new Date());
			this.setPicDocAdmTitle("");
		}
	}

	public void deletePicDocAdm() {
		try {
			if (this.getViewState().get("picdocumentadm") != null) {
				this.getViewState().put("picdocumentadm", null);
			} else if (this.getEntity().getPicDocumentAdm() != null) {
				this.getViewState().put("picdocumentadmToDel",
						BeansFactory.DocumentsAdm().Load(this.getEntity().getPicDocumentAdm().getId()).getId());
				this.getEntity().setPicDocumentAdm(null);
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

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		this.clearSession();
		// this.loadEntity();
		// this.FillComboboxes();
	}

	public void SaveEntityBudetEdit()
			throws HibernateException, NumberFormatException, PersistenceBeanException, IOException {
		this.getViewState().put("gotobudgets", true);
		this.savePicInternal();
	}

	@Override
	public void SaveEntity() throws HibernateException, NumberFormatException, PersistenceBeanException, IOException {
		this.savePicInternal();
	}

	public void savePIC() throws IOException, PersistenceBeanException {
		if (!checkEmpty("EditForm:tab2:picDocAdmLink", this.getPicDocAdmTitle())) {
			this.getContext().getViewRoot().findComponent("EditForm:tab2:doc2link").getAttributes().put("style",
					"color: #E00000;");
			return;
		}

		String sMonthDuration = this.getPicMonthDuration();
		String sYearDuration = this.getPicYearDuration();

		int monthDuration = Integer.valueOf(sMonthDuration);
		int yearDuration = Integer.valueOf(sYearDuration);

		String oldDuration = (String) this.getViewState().get("oldYearDuration");

		if (oldDuration != null && !oldDuration.isEmpty() && yearDuration < Integer.parseInt(oldDuration)) {
			this.yearDecreased = true;

			if (monthDuration <= 36 && yearDuration > 3) {
				this.setDecreaseError(Utils.getString("projectInformationCompletationViewYearDurationDecreaseConfirm"));
				return;
			} else if (monthDuration > 36 && yearDuration > 3) {
				this.setDecreaseError(
						Utils.getString("projectInformationCompletationViewMonthYearDurationDecreaseConfirm"));

				return;
			} else if (monthDuration <= 36 && yearDuration <= 3) {
				setDecreaseError(Utils.getString("partnerBudgetDelete"));
			}

			return;
		}

		if (this.getProject().getAsse() < 5) {
			if (monthDuration <= 36 && yearDuration > 3) {
				this.setDurationError(Utils.getString("projectInformationCompletationViewYearDurationConfirm"));
				this.getViewState().put("monthYearValidationFlag", true);

				this.setShowConfirmation(true);

				return;
			} else if (monthDuration > 36 && yearDuration > 3) {
				this.setDurationError(Utils.getString("projectInformationCompletationViewMonthYearDurationConfirm"));
				this.getViewState().put("monthYearValidationFlag", true);

				this.setShowConfirmation(true);

				return;
			}
		}

		if (this.getProjectState().equals(ProjectState.Actual)) {
			if (yearDuration > Integer.parseInt(oldDuration)) {
				return;
			}
		}
		this.savePicInternal();
	}

	private void savePicInternal() throws IOException, PersistenceBeanException {

		ProjectIformationCompletations entityToSave = BeansFactory.ProjectIformationCompletations()
				.LoadByProject(this.getProjectId());

		if (entityToSave == null) {
			entityToSave = new ProjectIformationCompletations();
			entityToSave.setProject(this.getProject());
		}

		listPICMS = new ArrayList<InfoObject>();
		Documents picdoc = null;
		if (this.getViewState().get("picdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocument");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				picdoc = new Documents();
				picdoc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				picdoc.setName(docinfo.getName());
				picdoc.setUser(this.getSessionBean().getCurrentUser());
				picdoc.setDocumentDate(this.getPicDocument().getDocumentDate());
				picdoc.setProtocolNumber(docinfo.getProtNumber());
				picdoc.setIsPublic(docinfo.getIsPublic());
				picdoc.setCategory(docinfo.getCategory());
				picdoc.setEditableDate(getPicDocument().getEditableDate());
				picdoc.setDocumentNumber(getPicDocument().getDocumentNumber());
				picdoc.setNote(getPicDocument().getNote());
				if (this.getSelectedPartner() != null && !this.getSelectedPartner().isEmpty()) {
					picdoc.setForPartner(BeansFactory.Users().Load(this.getSelectedPartner()));
				}
				if (getPicDocument().getUploadRole() != null) {
					picdoc.setUploadRole(getPicDocument().getUploadRole());
				}
				if (this.getIsNewEntity()) {
					picdoc.setUser(this.getSessionBean().getCurrentUser());
				}

				picdoc.setSection(BeansFactory.Sections().Load(DocumentSections.ProjectInfoCompletation.getValue()));
				picdoc.setProject(this.getProject());

				picdoc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(picdoc);

			}

			entityToSave.setPicDocument(picdoc);
		}

		if (this.getViewState().get("picdocumentToDel") != null) {
			entityToSave.setPicDocument(picdoc);
			BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("picdocumentToDel"));
		}

		DocumentsAdm picdocAdm = null;
		if (this.getViewState().get("picdocumentadm") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("picdocumentadm");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				picdocAdm = new DocumentsAdm();
				picdocAdm.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				picdocAdm.setName(docinfo.getName());
				picdocAdm.setUser(this.getSessionBean().getCurrentUser());
				picdocAdm.setDocumentDate(this.getPicDocumentAdm().getDocumentDate());
				picdocAdm.setProtocolNumber(docinfo.getProtNumber());
				picdocAdm.setIsPublic(docinfo.getIsPublic());
				picdocAdm.setCategory(docinfo.getCategory());
				picdocAdm.setEditableDate(getPicDocumentAdm().getEditableDate());
				picdocAdm.setDocumentNumber(getPicDocumentAdm().getDocumentNumber());
				picdocAdm.setNote(getPicDocumentAdm().getNote());
				if (this.getSelectedAdmPartner() != null && !this.getSelectedAdmPartner().isEmpty()) {
					picdocAdm.setForPartner(BeansFactory.Users().Load(this.getSelectedAdmPartner()));
				}
				if (getPicDocumentAdm().getUploadRole() != null) {
					picdocAdm.setUploadRole(getPicDocumentAdm().getUploadRole());
				}
				if (this.getIsNewEntity()) {
					picdocAdm.setUser(this.getSessionBean().getCurrentUser());
				}

				picdocAdm.setSection(BeansFactory.Sections().Load(DocumentSections.ProjectInfoCompletation.getValue()));
				picdocAdm.setProject(this.getProject());

				picdocAdm.setFileName(newFileName);
				BeansFactory.DocumentsAdm().SaveInTransaction(picdocAdm);
			}
			entityToSave.setPicDocumentAdm(picdocAdm);
		}

		if (this.getViewState().get("picdocumentadmToDel") != null) {
			entityToSave.setPicDocumentAdm(picdocAdm);
			BeansFactory.DocumentsAdm().DeleteInTransaction((Long) this.getViewState().get("picdocumentadmToDel"));
		}

		entityToSave.setDescription(this.getPicDescription());
		
		entityToSave.setDescriptionEng(this.getPicDescriptionEng());

		entityToSave.setYearDuration(Integer.valueOf(this.getPicYearDuration()));

		entityToSave.setMonthDuration(Integer.valueOf(this.getPicMonthDuration()));
		entityToSave.setStartYear(this.getStartYear());
		entityToSave.setEndYear(this.getEndYear());
		entityToSave.setDateAdmissionToFinancing(this.getDateAdmissionToFinancing());
		entityToSave.setDateOfSubmission(this.getDateSubmission());

		if (this.getPicYearDuration() != null && !this.getPicYearDuration().isEmpty()) {
			String oldDuration = (String) this.getViewState().get("oldYearDuration");

			Integer newDuration = Integer.parseInt(this.getPicYearDuration());

			if (oldDuration != null && !oldDuration.isEmpty() && newDuration < Integer.parseInt(oldDuration)) {
				List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
						.GetAllByProject(String.valueOf(this.getProjectId()));
				for (PartnersBudgets budget : listPB) {
					budget.setDeleted(true);
				}
			}
		}

		entityToSave.setCupCode(this.getPicCUPcode());

		Projects proj = BeansFactory.Projects().Load(this.getProjectId());
		proj.setCupCode(this.getPicCUPcode());
		BeansFactory.Projects().Save(proj);
		entityToSave.setEconomicActivity(BeansFactory.EconomicActivities().Load(this.getPicEconomicActivity()));

		entityToSave.setFinancingCode(this.getPicFinancingCode());

		entityToSave.setTerritorialCode(this.getPicTerritorialCode());

		entityToSave.setActualCode(this.getPicActualCode());

		//entityToSave.setFinancingForms(BeansFactory.FinancingForms().Load(this.getFinancingForm()));

		if (this.getPicNote() != null) {
			entityToSave.setNote(this.getPicNote());
		}
		
		if (this.getPicResults() != null) {
			entityToSave.setResults(this.getPicResults());
		}
		
		if (this.getPicEngResults() != null) {
			entityToSave.setEngResults(this.getPicEngResults());
		}
		
		if (this.getPicUrl() != null) {
			entityToSave.setUrl(this.getPicUrl());
		}

		//entityToSave.setOtherFound(this.getPicOtherFound());

		//entityToSave.setL442001(this.getPicViewL4432001());

		//entityToSave.setActionPlan(this.getPicActionPlan());

//		entityToSave.setBigProject(this.getPicBigProject());

		//entityToSave.setOccupationalFinancing(this.getPicOccupationalFinancing());

		//entityToSave.setInboundAmmount(this.getPicInboundAmount());

		BeansFactory.ProjectIformationCompletations().Save(entityToSave);

		if (this.getProjectState().equals(ProjectState.Actual)) {
			if (this.getProject().getAsse() != 5) {
				for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.AGU)) {
					InfoObject info = new InfoObject();
					info.setName(item.getName());
					info.setSurname(item.getSurname());
					info.setMail(item.getMail());
					info.setProjectName(this.getProject().getTitle());
					listPICMS.add(info);
				}

			}

			for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.STC)) {
				InfoObject info = new InfoObject();
				info.setName(item.getName());
				info.setSurname(item.getSurname());
				info.setMail(item.getMail());
				info.setProjectName(this.getProject().getTitle());
				listPICMS.add(info);
			}
		}

		this.clearSession();

		this.getViewState().put("monthYearValidationFlag", null);

		this.AfterSave();
	}

	/**
	 * Validate input to year duration
	 * 
	 * @param context
	 * @param component
	 * @param value
	 *            User input from dropdownlist
	 * @throws ValidatorException
	 */
	public void checkYearDuration(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String sYearDuration = value.toString();
		int yearDuration = Integer.valueOf(sYearDuration);

		this.setYearDurationForValidation(yearDuration);

		/*
		 * if(this.getProject().getAsse() < 5 && yearDuration > 3) {
		 * FacesMessage tmpMessage = new FacesMessage(Utils.getString(
		 * "Resources",
		 * "projectInformationCompletationViewYearDurationErrorMessage", null));
		 * tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		 * component.getAttributes().put("style", "background-color: #FFE5E5;");
		 * component.getAttributes().put("title", tmpMessage.getSummary());
		 * throw new ValidatorException(tmpMessage); }
		 */
	}

	/*
	 * private UIComponent findComponent(UIComponent c, String id) { if
	 * (id.equals(c.getId())) { return c; } Iterator<UIComponent> kids =
	 * c.getFacetsAndChildren(); while (kids.hasNext()) { UIComponent found =
	 * findComponent(kids.next(), id); if (found != null) { return found; } }
	 * return null; }
	 */

	/**
	 * Validate input to month duration
	 * 
	 * @param context
	 * @param component
	 * @param value
	 *            User input to textbox
	 * @throws ValidatorException
	 */
	public void checkMonthDuration(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		this.setShowConfirmation(false);

		String sMonthDuration = value.toString();

		if (sMonthDuration != null && !sMonthDuration.isEmpty()) {
			if (!this.isNumber(sMonthDuration)) {
				FacesMessage tmpMessage = new FacesMessage(
						Utils.getString("Resources", "validator.thisFieldShouldBeNumeric", null));
				tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				ValidatorHelper.markNotVAlid(component, tmpMessage, context, new HashMap<String, Integer>());
				throw new ValidatorException(tmpMessage);
			} else {

				int monthDuration = Integer.valueOf(sMonthDuration);
				int yearDuration = this.getYearDurationForValidation();

				if (monthDuration > yearDuration * 12) {
					FacesMessage tmpMessage = new FacesMessage(Utils.getString("Resources",
							"projectInformationCompletationViewMonthGreaterYearDuration", null));
					tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					ValidatorHelper.markNotVAlid(component, tmpMessage, context, new HashMap<String, Integer>());
					throw new ValidatorException(tmpMessage);
				}

			}
		} else {
			FacesMessage tmpMessage = new FacesMessage(Utils.getString("Resources", "validatorMessage", null));
			tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			ValidatorHelper.markNotVAlid(component, tmpMessage, context, new HashMap<String, Integer>());
			throw new ValidatorException(tmpMessage);
		}
	}

	private boolean isNumber(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}

		char[] valueMas = value.toCharArray();
		for (Character c : valueMas) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	private void FillComboboxes() throws HibernateException, PersistenceBeanException {
		this.fillYearDuration();
		this.fillEconomicActivities();
		this.fillFinancingForms();
		this.fillYears();
		this.fillFinancingCode();
		this.fillTerritorialCode();
		this.fillActualCode();
		this.fillRoles();
		this.fillCategories();
		this.fillPartners();
	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	private void fillYearDuration() {
		// Fill year duration
		this.listYearDuration = new ArrayList<SelectItem>();
		this.listYearDuration.clear();

		this.listYearDuration.add(SelectItemHelper.getFirst());

		boolean shouldBeLess = this.getProjectState().equals(ProjectState.Actual);

		for (int i = 1; i <= 3; i++) {   //for (int i = 1; i <= 10; i++) {
			if (!shouldBeLess) {
				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(i));
				item.setLabel(String.valueOf(i));

				this.listYearDuration.add(item);
			} else {
				if (i >= Integer.parseInt(getPicYearDuration())) {
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(i));
					item.setLabel(String.valueOf(i));

					this.listYearDuration.add(item);
				}
			}
		}
	}

	private void fillYears() {
		// Fill year duration
		this.listYears = new ArrayList<SelectItem>();
		this.listYears.clear();

		this.listYears.add(SelectItemHelper.getFirst());

		for (int i = 2007; i <= 2030; i++) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(i));
			item.setLabel(String.valueOf(i));

			this.listYears.add(item);
		}
	}

	private void fillFinancingCode() {
		// Fill financing code
		this.listFinancingCode = new ArrayList<SelectItem>();
		this.listFinancingCode.clear();

		this.listFinancingCode.add(SelectItemHelper.getFirst());
		SelectItem item = new SelectItem();
		item.setValue(Utils.getString("Resources", "projectInformationCompletationViewFinancingValue", null));
		item.setLabel(Utils.getString("Resources", "projectInformationCompletationViewFinancingValue", null));

		this.listFinancingCode.add(item);
	}

	private void fillTerritorialCode() {
		// Fill territorial code
		this.listTerritorialCode = new ArrayList<SelectItem>();
		this.listTerritorialCode.clear();

		this.listTerritorialCode.add(SelectItemHelper.getFirst());
		SelectItem item = new SelectItem();
		item.setValue(Utils.getString("Resources", "projectInformationCompletationViewTerritorialValue", null));
		item.setLabel(Utils.getString("Resources", "projectInformationCompletationViewTerritorialValue", null));

		this.listTerritorialCode.add(item);
	}

	private void fillActualCode() {
		// Fill actual code
		this.listActualCode = new ArrayList<SelectItem>();
		this.listActualCode.clear();

		this.listActualCode.add(SelectItemHelper.getFirst());
		SelectItem item = new SelectItem();
		item.setValue(Utils.getString("Resources", "projectInformationCompletationViewTerritorialValue", null));
		item.setLabel(Utils.getString("Resources", "projectInformationCompletationViewTerritorialValue", null));

		this.listActualCode.add(item);
	}

	private void fillPartners() throws HibernateException, PersistenceBeanException {
		listPartners = new ArrayList<SelectItem>();

		List<CFPartnerAnagraphs> partners = new ArrayList<CFPartnerAnagraphs>();

		if (this.getSessionBean().isCIL()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByCIL(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					for (CFPartnerAnagraphs item : partnersLoc) {
						CFPartnerAnagraphProject cfpap = BeansFactory.CFPartnerAnagraphProject()
								.LoadByPartner(Long.valueOf(this.getProjectId()), item.getId());
						if (cfpap != null && cfpap.getRemovedFromProject()) {
							continue;
						}

						partners.add(item);
					}

				}
			}
		}
		if (this.getSessionBean().isDAEC()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByDAEC(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					partners.addAll(partnersLoc);
				}
			}
		}
		if (this.getSessionBean().isAAU() || this.getSessionBean().isSTC()) {
			partners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
		}

		listPartners.add(SelectItemHelper.getFirst());

		for (CFPartnerAnagraphs item : partners) {
			StringBuilder sb = new StringBuilder();
			SelectItem selectItem = new SelectItem();

			if (item.getUser() == null) {
				sb.append(item.getName());
			} else {
				sb.append(item.getUser().getName());
				sb.append(item.getUser().getSurname());
			}

			selectItem.setValue(String.valueOf(item.getId()));
			selectItem.setLabel(sb.toString());

			listPartners.add(selectItem);
		}

		if (listPartners.size() == 1) {
			listPartners.clear();
		}
	}

	private void fillFinancingForms() throws HibernateException, PersistenceBeanException {
		this.listFinancingForms = new ArrayList<SelectItem>();

		this.listFinancingForms.add(SelectItemHelper.getFirst());
		for (FinancingForms item : BeansFactory.FinancingForms().Load()) {
			this.listFinancingForms.add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	private void fillRoles() throws PersistenceBeanException {
		this.listSelectRoles = new ArrayList<SelectItem>();

		this.listSelectRoles.add(SelectItemHelper.getFirst());
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

	private void fillEconomicActivities() throws HibernateException, PersistenceBeanException {
		this.listEconomicActivity = new ArrayList<SelectItem>();

		List<EconomicActivities> listEAs = new ArrayList<EconomicActivities>();

		try {
			listEAs = BeansFactory.EconomicActivities().Load();
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

		this.listEconomicActivity.add(SelectItemHelper.getFirst());

		for (EconomicActivities ea : listEAs) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(ea.getId()));
			item.setLabel(ea.getValue());
			this.listEconomicActivity.add(item);
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setProjectTypology(String projectTypology) {
		this.projectTypology = projectTypology;
	}

	public String getProjectTypology() {
		return this.projectTypology;
	}

	public void setProjectStates(ProjectStates projectStates) {
		this.projectStates = projectStates;
	}

	public ProjectStates getProjectStates() {
		return this.projectStates;
	}

	public void setSpecificGoal(String specificGoal) {
		this.specificGoal = specificGoal;
	}

	public String getSpecificGoal() {
		return this.specificGoal;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	public String getAsse() {
		return this.asse;
	}

	public void setQsnGoal(String qsnGoal) {
		this.qsnGoal = qsnGoal;
	}

	public String getQsnGoal() {
		return this.qsnGoal;
	}

	public void setPriorReasons(String priorReasons) {
		this.priorReasons = priorReasons;
	}

	public String getPriorReasons() {
		return this.priorReasons;
	}

	public void setActivationProcedure(String activationProcedure) {
		this.activationProcedure = activationProcedure;
	}

	public String getActivationProcedure() {
		return this.activationProcedure;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getCup() {
		return this.cup;
	}

	public void setCpt(String cpt) {
		this.cpt = cpt;
	}

	public String getCpt() {
		return this.cpt;
	}

	public void setPicDescription(String picDescription) {
		this.getViewState().put("picDescription", picDescription);
	}

	public String getPicDescription() {
		if (this.getViewState().get("picDescription") == null) {
			return null;
		}

		return this.getViewState().get("picDescription").toString();
	}
	
	public void setPicDescriptionEng(String picDescriptionEng) {
		this.getViewState().put("picDescriptionEng", picDescriptionEng);
	}

	public String getPicDescriptionEng() {
		if (this.getViewState().get("picDescriptionEng") == null) {
			return null;
		}

		return this.getViewState().get("picDescriptionEng").toString();
	}	
	

	public void setPicYearDuration(String picYearDuration) {
		this.getViewState().put("picYearDuration", picYearDuration);
	}

	public String getPicYearDuration() {
		if (this.getViewState().get("picYearDuration") == null) {
			return null;
		}

		return this.getViewState().get("picYearDuration").toString();
	}

	public void setListYearDuration(List<SelectItem> listYearDuration) {
		this.listYearDuration = listYearDuration;
	}

	public List<SelectItem> getListYearDuration() {
		return listYearDuration;
	}

	public void setPicNote(String picNote) {
		this.getViewState().put("picNote", picNote);
	}

	public String getPicNote() {
		if (this.getViewState().get("picNote") == null) {
			return null;
		}

		return this.getViewState().get("picNote").toString();
	}
	
	public void setPicResults(String picResults) {
		this.getViewState().put("picResults", picResults);
	}

	public String getPicResults() {
		if (this.getViewState().get("picResults") == null) {
			return null;
		}

		return this.getViewState().get("picResults").toString();
	}	
	
	public void setPicEngResults(String picEngResults) {
		this.getViewState().put("picEngResults", picEngResults);
	}

	public String getPicEngResults() {
		if (this.getViewState().get("picEngResults") == null) {
			return null;
		}

		return this.getViewState().get("picEngResults").toString();
	}		
	
	public void setPicUrl(String picUrl) {
		this.getViewState().put("picUrl", picUrl);
	}

	public String getPicUrl() {
		if (this.getViewState().get("picUrl") == null) {
			return null;
		}

		return this.getViewState().get("picUrl").toString();
	}	

//	public void setPicOtherFound(Boolean picOtherFound) {
//		this.getViewState().put("picOtherFound", picOtherFound);
//	}
//
//	public Boolean getPicOtherFound() {
//		return (Boolean) this.getViewState().get("picOtherFound");
//	}

//	public void setPicViewL4432001(Boolean picViewL4432001) {
//		this.getViewState().put("picViewL4432001", picViewL4432001);
//	}
//
//	public Boolean getPicViewL4432001() {
//		return (Boolean) this.getViewState().get("picViewL4432001");
//	}

//	public void setPicBigProject(Boolean picBigProject) {
//		this.getViewState().put("picBigProject", picBigProject);
//	}
//
//	public Boolean getPicBigProject() {
//		return (Boolean) this.getViewState().get("picBigProject");
//	}

//	public void setPicActionPlan(Boolean picActionPlan) {
//		this.getViewState().put("picActionPlan", picActionPlan);
//	}
//
//	public Boolean getPicActionPlan() {
//		return (Boolean) this.getViewState().get("picActionPlan");
//	}

//	public void setPicOccupationalFinancing(Boolean picOccupationalFinancing) {
//		this.getViewState().put("picOccupationalFinancing", picOccupationalFinancing);
//	}
//
//	public Boolean getPicOccupationalFinancing() {
//		return (Boolean) this.getViewState().get("picOccupationalFinancing");
//	}

//	public void setPicInboundAmount(Boolean picInboundAmount) {
//		this.getViewState().put("picInboundAmount", picInboundAmount);
//	}

//	public Boolean getPicInboundAmount() {
//		return (Boolean) this.getViewState().get("picInboundAmount");
//	}

	public void setPicCUPcode(String picCUPcode) {
		this.getViewState().put("picCUPcode", picCUPcode);
	}

	public String getPicCUPcode() {
		return (String) this.getViewState().get("picCUPcode");
	}

	public void setListEconomicActivity(List<SelectItem> listEconomicActivity) {
		this.listEconomicActivity = listEconomicActivity;
	}

	public List<SelectItem> getListEconomicActivity() {
		return listEconomicActivity;
	}

	public void setPicEconomicActivity(String picEconomicActivity) {
		this.getViewState().put("picEconomicActivity", picEconomicActivity);
	}

	public String getPicEconomicActivity() {
		if (this.getViewState().get("picEconomicActivity") == null) {
			return null;
		}

		return this.getViewState().get("picEconomicActivity").toString();
	}

	public void setPicFinancingCode(String picFinancingCode) {
		this.getViewState().put("picFinancingCode", picFinancingCode);
	}

	public String getPicFinancingCode() {
		if (this.getViewState().get("picFinancingCode") == null) {
			return null;
		}

		return this.getViewState().get("picFinancingCode").toString();
	}

	public void setPicTerritorialCode(String picTerritorialCode) {
		this.getViewState().put("picTerritorialCode", picTerritorialCode);
	}

	public String getPicTerritorialCode() {
		if (this.getViewState().get("picTerritorialCode") == null) {
			return null;
		}

		return this.getViewState().get("picTerritorialCode").toString();
	}

	public void setPicActualCode(String picActualCode) {
		this.getViewState().put("picActualCode", picActualCode);
	}

	public String getPicActualCode() {
		if (this.getViewState().get("picActualCode") == null) {
			return null;
		}

		return this.getViewState().get("picActualCode").toString();
	}

	public void setPicMonthDuration(String picMonthDuration) {
		this.getViewState().put("picMonthDuration", picMonthDuration);
	}

	public String getPicMonthDuration() {
		if (this.getViewState().get("picMonthDuration") == null) {
			return null;
		}

		return this.getViewState().get("picMonthDuration").toString();
	}

	public void setCup2(String cup2) {
		this.getSession().put("cup2", cup2);
	}

	public String getCup2() {
		if (this.getSession().get("cup2") == null) {
			return null;
		}

		return this.getSession().get("cup2").toString();
	}

	public void setCup3(String cup3) {
		this.getSession().put("cup3", cup3);
	}

	public String getCup3() {
		if (this.getSession().get("cup3") == null) {
			return null;
		}

		return this.getSession().get("cup3").toString();
	}

	public void setPicDocTitle(String picDocTitle) {
		this.picDocTitle = picDocTitle;
	}

	public String getPicDocTitle() {
		return picDocTitle;
	}

	public void setPicDocumentUpFile(UploadedFile picDocumentUpFile) {
		this.picDocumentUpFile = picDocumentUpFile;
	}

	public UploadedFile getPicDocumentUpFile() {
		return picDocumentUpFile;
	}

	public void setPicDocumentAdm(DocumentsAdm picDocumentAdm) {
		this.getViewState().put("picDocumentAdmDoc", picDocumentAdm);
	}

	public DocumentsAdm getPicDocumentAdm() {
		return (DocumentsAdm) this.getViewState().get("picDocumentAdmDoc");
	}

	/**
	 * Gets picDocAdmTitle
	 * 
	 * @return picDocAdmTitle the picDocAdmTitle
	 */
	public String getPicDocAdmTitle() {
		return picDocAdmTitle;
	}

	/**
	 * Sets picDocAdmTitle
	 * 
	 * @param picDocAdmTitle
	 *            the picDocAdmTitle to set
	 */
	public void setPicDocAdmTitle(String picDocAdmTitle) {
		this.picDocAdmTitle = picDocAdmTitle;
	}

	/**
	 * Gets picDocumentAdmUpFile
	 * 
	 * @return picDocumentAdmUpFile the picDocumentAdmUpFile
	 */
	public UploadedFile getPicDocumentAdmUpFile() {
		return picDocumentAdmUpFile;
	}

	/**
	 * Sets picDocumentAdmUpFile
	 * 
	 * @param picDocumentAdmUpFile
	 *            the picDocumentAdmUpFile to set
	 */
	public void setPicDocumentAdmUpFile(UploadedFile picDocumentAdmUpFile) {
		this.picDocumentAdmUpFile = picDocumentAdmUpFile;
	}

	public void setPicDocument(Documents picDocument) {
		this.getViewState().put("picDocumentDoc", picDocument);
	}

	public Documents getPicDocument() {
		return (Documents) this.getViewState().get("picDocumentDoc");
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = this.getSessionBean().isCFW() || this.getSessionBean().isAGUW();
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	private void clearSession() {
		this.getSession().put("cup3", null);
		this.getSession().put("cup2", null);
		this.getSession().put("picMonthDuration", null);
		this.getSession().put("picEconomicActivity", null);
		this.getSession().put("picFinancingCode", null);
		this.getSession().put("picTerritorialCode", null);
		this.getSession().put("picActualCode", null);
		this.getSession().put("picCUPcode", null);
		//this.getSession().put("picInboundAmount", null);
		//this.getSession().put("picViewL4432001", null);
		//this.getSession().put("picBigProject", null);
		//this.getSession().put("picActionPlan", null);
		this.getSession().put("picActualPlan", null);
		//this.getSession().put("picOccupationalFinancing", null);
		this.getSession().put("picNote", null);
		this.getSession().put("picUrl", null);
		this.getSession().put("oldYearDuration", null);
		this.getSession().put("picYearDuration", null);
		this.getSession().put("picDescription", null);
		this.getSession().put("picDescriptionEng", null);
		this.getSession().put("picDocument", null);
		this.getSession().put("picDocumentAdm", null);
	}

	public void setYearDurationForValidation(int yearDurationForValidation) {
		this.getViewState().put("yearDurationForValidation", yearDurationForValidation);
	}

	public int getYearDurationForValidation() {
		if (this.getViewState().get("yearDurationForValidation") != null) {
			return (Integer) this.getViewState().get("yearDurationForValidation");
		}

		return 1;
	}

	public void clearValidationFlag() {
		this.getViewState().put("monthYearValidationFlag", null);
	}

	public void setShowConfirmation(Boolean showConfirmation) {
		this.getViewState().put("showConfirmation", showConfirmation);
	}

	public Boolean getShowConfirmation() {
		if (this.getViewState().get("showConfirmation") == null) {
			return false;
		}

		return (Boolean) this.getViewState().get("showConfirmation");
	}

	/**
	 * Sets listFinancingForms
	 * 
	 * @param listFinancingForms
	 *            the listFinancingForms to set
	 */
	public void setListFinancingForms(List<SelectItem> listFinancingForms) {
		this.listFinancingForms = listFinancingForms;
	}

	/**
	 * Gets listFinancingForms
	 * 
	 * @return listFinancingForms the listFinancingForms
	 */
	public List<SelectItem> getListFinancingForms() {
		return listFinancingForms;
	}

	public String getFinancingForm() {
		return (String) this.getViewState().get("FinancingForm");
	}

	public void setFinancingForm(String entity) {
		this.getViewState().put("FinancingForm", entity);
	}

	public String getRole() {
		return (String) this.getViewState().get("selectedRole");
	}

	public void setRole(String entity) {
		this.getViewState().put("selectedRole", entity);
	}

	public String getAdmRole() {
		return (String) this.getViewState().get("selectedAdmRole");
	}

	public void setAdmRole(String entity) {
		this.getViewState().put("selectedAdmRole", entity);
	}

	/**
	 * Sets isNewEntity
	 * 
	 * @param isNewEntity
	 *            the isNewEntity to set
	 */
	public void setIsNewEntity(boolean isNewEntity) {
		this.isNewEntity = isNewEntity;
	}

	/**
	 * Gets isNewEntity
	 * 
	 * @return isNewEntity the isNewEntity
	 */
	public boolean getIsNewEntity() {
		return isNewEntity;
	}

	/**
	 * Sets lastSave
	 * 
	 * @param lastSave
	 *            the lastSave to set
	 */
	public void setLastSave(boolean lastSave) {
		this.lastSave = lastSave;
	}

	/**
	 * Gets lastSave
	 * 
	 * @return lastSave the lastSave
	 */
	public boolean getLastSave() {
		return lastSave;
	}

	/**
	 * Sets startYear
	 * 
	 * @param startYear
	 *            the startYear to set
	 */
	public void setStartYear(Date startYear) {
		this.getViewState().put("startYear", startYear);
	}

	/**
	 * Gets startYear
	 * 
	 * @return startYear the startYear
	 */
	public Date getStartYear() {
		return (Date) this.getViewState().get("startYear");
	}


	/**
	 * Sets endYear
	 * 
	 * @param endYear
	 *            the endYear to set
	 */
	public void setEndYear(Date endYear) {
		this.getViewState().put("endYear", endYear);
	}

	/**
	 * Gets endYear
	 * 
	 * @return endYear the endYear
	 */
	public Date getEndYear() {
		return (Date) this.getViewState().get("endYear");
	}	
	
	/**
	 * @param dateAdmissionToFinancing
	 */
	public void setDateAdmissionToFinancing(Date dateAdmissionToFinancing) {
		this.getViewState().put("dateAdmissionToFinancing", dateAdmissionToFinancing);
	}

	/**
	 * @return
	 */
	public Date getDateAdmissionToFinancing() {
		return (Date) this.getViewState().get("dateAdmissionToFinancing");
	}

	
	/**
	 * @param dateAdmissionToFinancing
	 */
	public void setDateSubmission(Date dateSubmission) {
		this.getViewState().put("dateSubmission", dateSubmission);
	}

	/**
	 * @return
	 */
	public Date getDateSubmission() {
		return (Date) this.getViewState().get("dateSubmission");
	}

	
	/**
	 * Sets listYears
	 * 
	 * @param listYears
	 *            the listYears to set
	 */
	public void setListYears(List<SelectItem> listYears) {
		this.listYears = listYears;
	}

	/**
	 * Gets listYears
	 * 
	 * @return listYears the listYears
	 */
	public List<SelectItem> getListYears() {
		return listYears;
	}

	public void setYearDecreased(boolean yearDecreased) {
		this.yearDecreased = yearDecreased;
	}

	public boolean isYearDecreased() {
		return yearDecreased;
	}

	/**
	 * Sets durationError
	 * 
	 * @param durationError
	 *            the durationError to set
	 */
	public void setDurationError(String durationError) {
		this.durationError = durationError;
	}

	/**
	 * Gets durationError
	 * 
	 * @return durationError the durationError
	 */
	public String getDurationError() {
		return durationError;
	}

	/**
	 * Sets decreaseError
	 * 
	 * @param decreaseError
	 *            the decreaseError to set
	 */
	public void setDecreaseError(String decreaseError) {
		this.decreaseError = decreaseError;
	}

	/**
	 * Gets decreaseError
	 * 
	 * @return decreaseError the decreaseError
	 */
	public String getDecreaseError() {
		return decreaseError;
	}

	/**
	 * Sets listSelectRoles
	 * 
	 * @param listSelectRoles
	 *            the listSelectRoles to set
	 */
	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		this.listSelectRoles = listSelectRoles;
	}

	/**
	 * Gets listSelectRoles
	 * 
	 * @return listSelectRoles the listSelectRoles
	 */
	public List<SelectItem> getListSelectRoles() {
		return listSelectRoles;
	}

	/**
	 * Sets canChangeDuration
	 * 
	 * @param canChangeDuration
	 *            the canChangeDuration to set
	 */
	public void setCanChangeDuration(boolean canChangeDuration) {
		this.canChangeDuration = canChangeDuration;
	}

	/**
	 * Gets canChangeDuration
	 * 
	 * @return canChangeDuration the canChangeDuration
	 */
	public boolean getCanChangeDuration() {
		return canChangeDuration;
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
		this.categories = categories;
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

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedAdmCategory() {
		return selectedAdmCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedAdmCategory(String selectedAdmCategory) {
		this.selectedAdmCategory = selectedAdmCategory;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners() {
		return listPartners;
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners) {
		this.listPartners = listPartners;
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner) {
		this.getViewState().put("selectedPartner", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner() {
		return (String) this.getViewState().get("selectedPartner");
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedAdmPartner(String selectedAdmPartner) {
		this.getViewState().put("selectedAdmPartner", selectedAdmPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedAdmPartner() {
		return (String) this.getViewState().get("selectedAdmPartner");
	}

	public Boolean getCanEditCategory() {
		if (getSessionBean().isSTC() || getSessionBean().isAGU() || getSessionBean().isACU()
				|| getSessionBean().isAGUW() || getSessionBean().isSTCW() || getSessionBean().isACUW()) {
			return true;
		}
		return false;
	}

	private boolean checkEmpty(String id, Object value) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if ((str == null || str.isEmpty() || str.trim().isEmpty())) {
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			return false;
		} else {
			return true;
		}

	}

	/**
	 * @return the listFinancingCode
	 */
	public List<SelectItem> getListFinancingCode() {
		return listFinancingCode;
	}

	/**
	 * @param listFinancingCode
	 *            the listFinancingCode to set
	 */
	public void setListFinancingCode(List<SelectItem> listFinancingCode) {
		this.listFinancingCode = listFinancingCode;
	}

	/**
	 * @return the listTerritorialCode
	 */
	public List<SelectItem> getListTerritorialCode() {
		return listTerritorialCode;
	}

	/**
	 * @param listTerritorialCode
	 *            the listTerritorialCode to set
	 */
	public void setListTerritorialCode(List<SelectItem> listTerritorialCode) {
		this.listTerritorialCode = listTerritorialCode;
	}

	/**
	 * @return the listActualCode
	 */
	public List<SelectItem> getListActualCode() {
		return listActualCode;
	}

	/**
	 * @param listActualCode
	 *            the listActualCode to set
	 */
	public void setListActualCode(List<SelectItem> listActualCode) {
		this.listActualCode = listActualCode;
	}

}
