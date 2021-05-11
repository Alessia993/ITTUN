package com.infostroy.paamns.web.beans.acl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.Countries;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.JmcDecision;
import com.infostroy.paamns.common.enums.OrganizationType;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PartnerTypes;
import com.infostroy.paamns.common.enums.PotentialProjectStatuses;
import com.infostroy.paamns.common.enums.ProgramPriority;
import com.infostroy.paamns.common.enums.ProgramPriorityInvestment;
import com.infostroy.paamns.common.enums.ProvinceDepartment;
import com.infostroy.paamns.common.enums.SpecificObjective;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpirationDate;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjectsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.web.beans.EntityEditBean;

public class PotentialProjectEditBean extends EntityEditBean<PotentialProjects> {

	private String showErrorMessage;

	private boolean validationFailed;

	private List<SelectItem> partnerTypes;

	private List<SelectItem> provinceDepartments;

	private List<SelectItem> specificObjectives;

	private List<SelectItem> programPriorityInvestments;

	private List<SelectItem> programPriorities;

	private List<SelectItem> organizationTypes;
	
	private List<SelectItem> decisionsJmc;

	private List<SelectItem> countries;

	private List<SelectItem> outsideAreaCooperations;

	private List<SelectItem> statuses;

	private UploadedFile newDocumentUpFile;

	private String documentDescription;

	private List<Documents> docList;

	private Integer entityDeleteId;

	private String editDocTitle;

	private Boolean showDoc;

	private Date presentationDate;

	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException,
			NullPointerException {
		fillLists();
		setShowErrorMessage("none");
		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}
		if (this.getSession().get("potentialProjectEntityView") != null) {
			setView(true);
		}
		setDocList(docs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		processParams();
		setSelectedTab(0);
		PotentialProjects entity = null;
		if (this.getSession().get("potentialProjectEntityEdit") != null
				&& !String.valueOf(this.getSession().get("potentialProjectEntityEdit")).isEmpty()) {
			entity = BeansFactory.PotentialProjects()
					.Load(String.valueOf(this.getSession().get("potentialProjectEntityEdit")));
		}
		if (entity != null) {
			boolean isCFP = false;
			for (UserRoles role : getCurrentUser().getRoles()) {
				if (role.getRole().getCode().equals(UserRoleTypes.CFP.name())) {
					isCFP = true;
					break;
				}
			}
			if (isCFP) {
				if (getCurrentUser().getId().longValue() != entity.getUser().getId().longValue()) {
					GoBack();
				}
			}
			setEntity(entity);
			fillFields();
		} else {
			setEntity(new PotentialProjects());
		}

		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}
		if (entity != null) {
			for (PotentialProjectsToDocuments item : BeansFactory.PotentialProjectsToDocuments()
					.getByPotentialProject(entity.getId())) {
				if (item.getDocument() != null) {
					docs.add(item.getDocument());
				}
			}
		}
		setDocList(docs);
		this.getViewState().put("newDocument", docs);
		this.setFailedValidation(new ArrayList<String>());
	}

	@SuppressWarnings("static-access")
	@Override
	public Boolean BeforeSave() {
		try {
			ExpirationDate expirationDate = BeansFactory.ExpirationDate().getExpirationDate();
			if (expirationDate != null) {
				if (expirationDate.getExpirationDate().before(new Date())) {
					setShowErrorMessage("");
					return false;
				}
			}
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		setValidationFailed(false);
		checkEmpty("EditForm:tab1:projectAcronym", getProjectAcronym());
		checkEmpty("EditForm:tab1:projectTitle", getProjectAcronym());
		if (isValidationFailed()) {
			return false;
		}
		return true;
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		getEntity().setProjectAcronym(getProjectAcronym());
		getEntity().setProjectTitle(getProjectTitle());
		getEntity().setLeadNameOrganization(getLeadNameOrganization());
		if (getPresentationDate() != null) {
			getEntity().setPresentationDate(getPresentationDate());
		}
		if (getProgramPriority() != null && !getProgramPriority().isEmpty()
				&& ProgramPriority.valueOf(getProgramPriority()) != null) {
			getEntity().setProgramPriority(ProgramPriority.valueOf(getProgramPriority()));
		}
		if (getProgramPriorityInvestment() != null && !getProgramPriorityInvestment().isEmpty()
				&& ProgramPriorityInvestment.valueOf(getProgramPriorityInvestment()) != null) {
			getEntity().setProgramPriorityInvestment(ProgramPriorityInvestment.valueOf(getProgramPriorityInvestment()));
		}
		if (getSpecificObjective() != null && !getSpecificObjective().isEmpty()
				&& SpecificObjective.valueOf(getSpecificObjective()) != null) {
			getEntity().setSpecificObjective(SpecificObjective.valueOf(getSpecificObjective()));
		}
		getEntity().setLeadPartnerAcronym(getLeadPartnerAcronym());
		getEntity().setRegisteredOfficeAddress(getRegisteredOfficeAddress());
		getEntity().setDepartment(getDepartment());
		getEntity().setHeadOfficeAddress(getHeadOfficeAddress());
		if (getProvinceDepartment() != null && !getProvinceDepartment().isEmpty()
				&& ProvinceDepartment.valueOf(getProvinceDepartment()) != null) {
			getEntity().setProvinceDepartment(ProvinceDepartment.valueOf(getProvinceDepartment()));
		}
		if (getCountry() != null && !getCountry().isEmpty() && Countries.valueOf(getCountry()) != null) {
			getEntity().setCountry(Countries.valueOf(getCountry()));
		}
		getEntity().setOutsideAreaCooperation(Boolean.valueOf(getOutsideAreaCooperation()));
		getEntity().setNameOfRepresentative(getNameOfRepresentative());
		getEntity().setPhone(getPhone());
		getEntity().setFax(getFax());
		getEntity().setEmail(getEmail());

		// new acagnoni 2019/03/18
		getEntity().setDateDecisionJMC(getDateDecisionJMC());
		if (getDecisionJMC() != null && !getDecisionJMC().isEmpty()
				&& JmcDecision.valueOf(getDecisionJMC()) != null) {
			getEntity().setDecisionJMC(JmcDecision.valueOf(getDecisionJMC()));
		}
		getEntity().setUnivocalIdLeadPartners(getUnivocalIdLeadPartners());
		getEntity().setCallSerialNumber(getCallSerialNumber());
		getEntity().setUnivocalIdentification(getUnivocalIdentification());
		getEntity().setOrganizationNameEnglish(getOrganizationNameEnglish());
		getEntity().setContactPersonFirstName(getContactPersonFirstName());
		getEntity().setContactPersonLastName(getContactPersonLastName());
		getEntity().setOrganizationURL(getOrganizationURL());
		if (getOrganizationType() != null && !getOrganizationType().isEmpty()
				&& OrganizationType.valueOf(getOrganizationType()) != null) {
			getEntity().setOrganizationType(OrganizationType.valueOf(getOrganizationType()));
		}
		getEntity().setProjectNameEnglish(getProjectNameEnglish());
		getEntity().setProposalTotalBudget(getProposalTotalBudget());
		getEntity().setTotalEUFundingRequested(getTotalEUFundingRequested());

		//

		if (getPartnerType() != null && !getPartnerType().isEmpty() && PartnerTypes.valueOf(getPartnerType()) != null) {
			getEntity().setPartnerType(PartnerTypes.valueOf(getPartnerType()));
		}
		getEntity().setTaxCode(getTaxCode());
		if (getStatus() == null || getStatus().isEmpty()) {
			getEntity().setStatus(PotentialProjectStatuses.FROM_APPROVE);
		} else {
			getEntity().setStatus(PotentialProjectStatuses.valueOf(getStatus()));
		}
		getEntity().setUpdateDate(new Date());

		if (getEntity().getUser() == null) {
			getEntity().setUser(getCurrentUser());
		}

		BeansFactory.PotentialProjects().SaveInTransaction(getEntity());
		saveDocuments();
	}

	@SuppressWarnings("unchecked")
	private void saveDocuments() throws PersistenceBeanException, IOException {
		List<PotentialProjectsToDocuments> potProjToDoc = BeansFactory.PotentialProjectsToDocuments()
				.getByPotentialProject(getEntity().getId());
		if ((potProjToDoc != null && !potProjToDoc.isEmpty()) || this.getViewState().get("newDocument") != null) {
			List<Documents> docs = new ArrayList<Documents>();

			if (this.getViewState().get("newDocument") != null) {
				docs = (List<Documents>) this.getViewState().get("newDocument");
			}
			List<Documents> oldDocs = new ArrayList<Documents>();
			for (PotentialProjectsToDocuments doc : potProjToDoc) {
				oldDocs.add(doc.getDocument());
			}

			for (Documents doc : oldDocs) {
				if (doc != null && !Contains(docs, doc)) {
					PotentialProjectsToDocuments potProjtodef = BeansFactory.PotentialProjectsToDocuments()
							.getByDocumentAndPotProj(doc.getId(), getEntity().getId());
					BeansFactory.PotentialProjectsToDocuments().DeleteInTransaction(potProjtodef);
					if (BeansFactory.PotentialProjectsToDocuments().getByDocument(doc.getId()).size() == 0) {
						BeansFactory.Documents().DeleteInTransaction(doc);
					}
				}
			}

			for (Documents doc : docs) {
				if (doc.isNew()) {
					String newFileName = FileHelper.newFileName(doc.getName());
					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setSection(BeansFactory.Sections().Load(DocumentSections.PotentialProjecs.getValue()));
						doc.setUser(this.getCurrentUser());
						BeansFactory.Documents().SaveInTransaction(doc);
						PotentialProjectsToDocuments potentialProjectsToDocuments = new PotentialProjectsToDocuments(
								doc, getEntity());
						BeansFactory.PotentialProjectsToDocuments().SaveInTransaction(potentialProjectsToDocuments);
					}
				} else {
					String newFileName = FileHelper.newFileName(doc.getName());

					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setSection(BeansFactory.Sections().Load(DocumentSections.PotentialProjecs.getValue()));
						doc.setUser(this.getCurrentUser());
					}

					BeansFactory.Documents().MergeInTransaction(doc);
				}
			}
		}
	}

	private boolean Contains(List<Documents> list, Documents item) {
		for (Documents doc : list) {
			if (doc.getId() != null && doc.getId().equals(item.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	@Override
	public void GoBack() {
		goTo(PagesTypes.POTENTIALPROJECTSLIST);
	}

	private void fillLists() {
		fillPartnerTypes();
		fillProvinceDepartments();
		fillSpecificObjectives();
		fillProgramPriorityInvestments();
		fillProgramPriorities();
		fillOrganizationTypes();
		fillDecisionsJmc();
		fillCountries();
		fillOutsideAreaCooperations();
		fillStatuses();
	}

	private void fillPartnerTypes() {
		setPartnerTypes(new ArrayList<SelectItem>());
		getPartnerTypes().add(SelectItemHelper.getFirst());
		for (PartnerTypes item : PartnerTypes.values()) {
			getPartnerTypes().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillProvinceDepartments() {
		setProvinceDepartments(new ArrayList<SelectItem>());
		getProvinceDepartments().add(SelectItemHelper.getFirst());
		for (ProvinceDepartment item : ProvinceDepartment.values()) {
			getProvinceDepartments().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillSpecificObjectives() {
		setSpecificObjectives(new ArrayList<SelectItem>());
		getSpecificObjectives().add(SelectItemHelper.getFirst());
		for (SpecificObjective item : SpecificObjective.values()) {
			getSpecificObjectives().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillProgramPriorityInvestments() {
		setProgramPriorityInvestments(new ArrayList<SelectItem>());
		getProgramPriorityInvestments().add(SelectItemHelper.getFirst());
		for (ProgramPriorityInvestment item : ProgramPriorityInvestment.values()) {
			getProgramPriorityInvestments().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillProgramPriorities() {
		setProgramPriorities(new ArrayList<SelectItem>());
		getProgramPriorities().add(SelectItemHelper.getFirst());
		for (ProgramPriority item : ProgramPriority.values()) {
			getProgramPriorities().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillOrganizationTypes() {
		setOrganizationTypes(new ArrayList<SelectItem>());
		getOrganizationTypes().add(SelectItemHelper.getFirst());
		for (OrganizationType item : OrganizationType.values()) {
			getOrganizationTypes().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}
	
	private void fillDecisionsJmc() {
		setDecisionsJmc(new ArrayList<SelectItem>());
		getDecisionsJmc().add(SelectItemHelper.getFirst());
		for (JmcDecision item : JmcDecision.values()) {
			getDecisionsJmc().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillCountries() {
		setCountries(new ArrayList<SelectItem>());
		getCountries().add(SelectItemHelper.getFirst());
		for (Countries item : Countries.values()) {
			getCountries().add(new SelectItem(String.valueOf(item.name()), item.toString()));
		}
	}

	private void fillOutsideAreaCooperations() {
		setOutsideAreaCooperations(new ArrayList<SelectItem>());
		getOutsideAreaCooperations().add(new SelectItem(true, Utils.getString("yes")));
		getOutsideAreaCooperations().add(new SelectItem(false, Utils.getString("no")));
	}

	private void fillStatuses() {
		setStatuses(new ArrayList<SelectItem>());
		getStatuses().add(SelectItemHelper.getFirst());
		for (PotentialProjectStatuses item : PotentialProjectStatuses.values()) {
			if (item != PotentialProjectStatuses.APPROVED && item != PotentialProjectStatuses.REJECTED
					&& item != PotentialProjectStatuses.FROM_APPROVE) {
				getStatuses().add(new SelectItem(String.valueOf(item.name()), item.toString()));
			}
		}
	}

	private void fillFields() {
		if (getEntity() == null) {
			return;
		}
		setProjectAcronym(getEntity().getProjectAcronym());
		setProjectTitle(getEntity().getProjectTitle());
		setLeadNameOrganization(getEntity().getLeadNameOrganization());

		// new acagnoni 2019/03/18
		setDateDecisionJMC(getEntity().getDateDecisionJMC());
		if (getEntity().getDecisionJMC() != null) {
			setDecisionJMC(getEntity().getDecisionJMC().name());
		}
		setUnivocalIdLeadPartners(getEntity().getUnivocalIdLeadPartners());
		setCallSerialNumber(getEntity().getCallSerialNumber());
		setUnivocalIdentification(getEntity().getUnivocalIdentification());
		setOrganizationNameEnglish(getEntity().getOrganizationNameEnglish());
		setContactPersonFirstName(getEntity().getContactPersonFirstName());
		setContactPersonLastName(getEntity().getContactPersonLastName());
		setOrganizationURL(getEntity().getOrganizationURL());
		if (getEntity().getOrganizationType() != null) {
			setOrganizationType(getEntity().getOrganizationType().name());
		}
		setProjectNameEnglish(getEntity().getProjectNameEnglish());
		setProposalTotalBudget(getEntity().getProposalTotalBudget());
		setTotalEUFundingRequested(getEntity().getTotalEUFundingRequested());
		//

		if (getEntity().getPresentationDate() != null) {
			setPresentationDate(getEntity().getPresentationDate());
		}
		if (getEntity().getProgramPriority() != null) {
			setProgramPriority(getEntity().getProgramPriority().name());
		}
		if (getEntity().getProgramPriorityInvestment() != null) {
			setProgramPriorityInvestment(getEntity().getProgramPriorityInvestment().name());
		}
		if (getEntity().getSpecificObjective() != null) {
			setSpecificObjective(getEntity().getSpecificObjective().name());
		}
		setLeadPartnerAcronym(getEntity().getLeadPartnerAcronym());
		setRegisteredOfficeAddress(getEntity().getRegisteredOfficeAddress());
		setDepartment(getEntity().getDepartment());
		setHeadOfficeAddress(getEntity().getHeadOfficeAddress());
		if (getEntity().getProvinceDepartment() != null) {
			setProvinceDepartment(getEntity().getProvinceDepartment().name());
		}
		if (getEntity().getCountry() != null) {
			setCountry(getEntity().getCountry().name());
		}
		setOutsideAreaCooperation(getEntity().getOutsideAreaCooperation());
		setNameOfRepresentative(getEntity().getNameOfRepresentative());
		setPhone(getEntity().getPhone());
		setFax(getEntity().getFax());
		setEmail(getEntity().getEmail());
		if (getEntity().getPartnerType() != null) {
			setPartnerType(getEntity().getPartnerType().name());
		}
		setTaxCode(getEntity().getTaxCode());
		setStatus(getEntity().getStatus().name());
	}

	public void processParams() {
		HttpServletRequest req = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest());
		if (req.getParameterMap().containsKey("view")) {
			this.getSession().put("potentialProjectEntityEdit",
					Long.parseLong(((String[]) req.getParameterMap().get("view"))[0]));
			this.getSession().put("potentialProjectEntityView",
					Long.parseLong(((String[]) req.getParameterMap().get("view"))[0]));
			setView(true);
		} else if (req.getParameterMap().containsKey("edit")) {
			this.getSession().put("potentialProjectEntityEdit",
					Long.parseLong(((String[]) req.getParameterMap().get("edit"))[0]));
			this.getSession().put("potentialProjectEntityView", null);
			setView(false);
		} else {
			this.getSession().remove("potentialProjectEntityEdit");
			this.getSession().remove("potentialProjectEntityView");
			setView(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void addNewDocument() throws NumberFormatException, PersistenceBeanException {
		try {
			if (getEntityEditId() != null) {
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}
				docs.get(getEntityEditId()).setDescription(getDocumentDescription());
				if (getNewDocumentUpFile() != null) {
					String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
					OutputStream os = new FileOutputStream(new File(fileName));
					os.write(getNewDocumentUpFile().getBytes());
					os.close();
					DocumentInfo doc = new DocumentInfo(new Date(), getNewDocumentUpFile().getName(), fileName, null,
							null);
					docs.get(getEntityEditId()).setTitle(FileHelper.getFileNameWOExtension(doc.getName()));
					docs.get(getEntityEditId()).setName(doc.getName());
					docs.get(getEntityEditId()).setDocumentDate(doc.getDate());
					docs.get(getEntityEditId()).setProtocolNumber(doc.getProtNumber());
					docs.get(getEntityEditId()).setFileName(doc.getFileName());
					docs.get(getEntityEditId()).setSignflag(doc.getSignFlag());
					// docs.get(getEntityEditId()).setHashfile(doc.getHashFile());
				}
				this.getViewState().put("newDocument", docs);
			} else if (getNewDocumentUpFile() != null) {
				String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();
				DocumentInfo doc = new DocumentInfo(new Date(), getNewDocumentUpFile().getName(), fileName, null, null,
						null);
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}
				Documents newDoc = new Documents(FileHelper.getFileNameWOExtension(doc.getName()), doc.getName(),
						this.getCurrentUser(), doc.getDate(), doc.getProtNumber(), null, this.getProject(),
						doc.getFileName(), doc.getCategory(), doc.getIsPublic(), 0, doc.getSignFlag());
				newDoc.setDescription(getDocumentDescription());
				docs.add(newDoc);
				this.getViewState().put("newDocument", docs);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void downloadDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityDownloadId());
			FileHelper.sendFile(
					new DocumentInfo(doc.getDocumentDate(), doc.getName(), doc.getFileName(), null, doc.getSignflag()),
					doc.getId() == null);
		}
	}

	public void addDoc() {
		setEntityEditId(null);
		setDocumentDescription(null);
		setSelectedTab(1);
		setShowDoc(false);
	}

	@SuppressWarnings({ "unchecked" })
	public void editDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityEditId());
			setDocumentDescription(doc.getDescription());
			setEditDocTitle(doc.getTitle());
		}
		setSelectedTab(1);
		setShowDoc(false);
	}

	@SuppressWarnings({ "unchecked" })
	public void showDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityEditId());
			setDocumentDescription(doc.getDescription());
			setEditDocTitle(doc.getTitle());
		}
		setSelectedTab(1);
		setShowDoc(true);
	}

	@SuppressWarnings("unchecked")
	public void deleteDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			docs.remove((int) this.getEntityDeleteId());
			this.getViewState().put("newDocument", docs);
			this.setSelectedTab(1);
		}
	}

	private boolean checkEmpty(String id, Object value) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (str == null || str.isEmpty() || str.trim().isEmpty()) {
			setValidationFailed(true);
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}

	public PotentialProjects getEntity() {
		this.entity = (PotentialProjects) this.getViewState().get("potentialProjectEntity");
		return this.entity;
	}

	public void setEntity(PotentialProjects entity) {
		this.getViewState().put("potentialProjectEntity", entity);
		this.entity = entity;
	}

	public String getProjectAcronym() {
		return getViewState().get("projectAcronym") != null ? String.valueOf(getViewState().get("projectAcronym"))
				: null;
	}

	public void setProjectAcronym(String projectAcronym) {
		getViewState().put("projectAcronym", projectAcronym);
	}

	public String getProjectTitle() {
		return getViewState().get("projectTitle") != null ? String.valueOf(getViewState().get("projectTitle")) : null;
	}

	public void setProjectTitle(String projectTitle) {
		getViewState().put("projectTitle", projectTitle);
	}

	public String getLeadNameOrganization() {
		return getViewState().get("leadNameOrganization") != null
				? String.valueOf(getViewState().get("leadNameOrganization")) : null;
	}

	public void setLeadNameOrganization(String leadNameOrganization) {
		getViewState().put("leadNameOrganization", leadNameOrganization);
	}

	// new acagnoni 2019/03/18

	public String getUnivocalIdLeadPartners() {
		return getViewState().get("univocalIdLeadPartners") != null
				? String.valueOf(getViewState().get("univocalIdLeadPartners")) : null;
	}

	public void setUnivocalIdLeadPartners(String univocalIdLeadPartners) {
		getViewState().put("univocalIdLeadPartners", univocalIdLeadPartners);
	}

	public String getCallSerialNumber() {
		return getViewState().get("callSerialNumber") != null ? String.valueOf(getViewState().get("callSerialNumber"))
				: null;
	}

	public void setCallSerialNumber(String callSerialNumber) {
		getViewState().put("callSerialNumber", callSerialNumber);
	}

	public String getUnivocalIdentification() {
		return getViewState().get("univocalIdentification") != null
				? String.valueOf(getViewState().get("univocalIdentification")) : null;
	}

	public void setUnivocalIdentification(String univocalIdentification) {
		getViewState().put("univocalIdentification", univocalIdentification);
	}

	public String getOrganizationNameEnglish() {
		return getViewState().get("organizationNameEnglish") != null
				? String.valueOf(getViewState().get("organizationNameEnglish")) : null;
	}

	public void setOrganizationNameEnglish(String organizationNameEnglish) {
		getViewState().put("organizationNameEnglish", organizationNameEnglish);
	}

	public String getContactPersonFirstName() {
		return getViewState().get("contactPersonFirstName") != null
				? String.valueOf(getViewState().get("contactPersonFirstName")) : null;
	}

	public void setContactPersonFirstName(String contactPersonFirstName) {
		getViewState().put("contactPersonFirstName", contactPersonFirstName);
	}

	public String getContactPersonLastName() {
		return getViewState().get("contactPersonLastName") != null
				? String.valueOf(getViewState().get("contactPersonLastName")) : null;
	}

	public void setContactPersonLastName(String contactPersonLastName) {
		getViewState().put("contactPersonLastName", contactPersonLastName);
	}

	public String getOrganizationURL() {
		return getViewState().get("organizationURL") != null ? String.valueOf(getViewState().get("organizationURL"))
				: null;
	}

	public void setOrganizationURL(String organizationURL) {
		getViewState().put("organizationURL", organizationURL);
	}

	public void setOrganizationType(String organizationType) {
		getViewState().put("organizationType", organizationType);
	}

	public String getOrganizationType() {
		if (!getView()) {
			return getViewState().get("organizationType") != null
					? String.valueOf(getViewState().get("organizationType")) : null;
		} else {
			return getViewState().get("organizationType") != null
					? OrganizationType.valueOf(String.valueOf(getViewState().get("organizationType"))).toString()
					: null;
		}
	}

	public void setDecisionJMC(String decisionJMC) {
		getViewState().put("decisionJMC", decisionJMC);
	}

	public String getDecisionJMC() {
		if (!getView()) {
			return getViewState().get("decisionJMC") != null ? String.valueOf(getViewState().get("decisionJMC")) : null;
		} else {
			return getViewState().get("decisionJMC") != null
					? JmcDecision.valueOf(String.valueOf(getViewState().get("decisionJMC"))).toString() : null;
		}
	}

	public Date getDateDecisionJMC() {
		return (Date) (getViewState().get("dateDecisionJMC") != null ? getViewState().get("dateDecisionJMC") : null);
	}

	public void setDateDecisionJMC(Date dateDecisionJMC) {
		getViewState().put("dateDecisionJMC", dateDecisionJMC);
	}
	
	public Date getDateAppeal() {
		return (Date) (getViewState().get("dateAppeal") != null ? getViewState().get("dateAppeal") : null);
	}

	public void setDateAppeal(Date dateAppeal) {
		getViewState().put("dateAppeal", dateAppeal);
	}

	public void setProjectNameEnglish(String projectNameEnglish) {
		getViewState().put("projectNameEnglish", projectNameEnglish);
	}

	public String getProjectNameEnglish() {
		return getViewState().get("projectNameEnglish") != null
				? String.valueOf(getViewState().get("projectNameEnglish")) : null;
	}

	public void setProposalTotalBudget(Double proposalTotalBudget) {
		getViewState().put("proposalTotalBudget", proposalTotalBudget);
	}

	public Double getProposalTotalBudget() {
		return (Double) (getViewState().get("proposalTotalBudget") != null ? getViewState().get("proposalTotalBudget")
				: null);
	}

	public void setTotalEUFundingRequested(Double totalEUFundingRequested) {
		getViewState().put("totalEUFundingRequested", totalEUFundingRequested);
	}

	public Double getTotalEUFundingRequested() {
		return (Double) (getViewState().get("totalEUFundingRequested") != null
				? getViewState().get("totalEUFundingRequested") : null);
	}

	//

	public String getProgramPriority() {
		if (!getView()) {
			return getViewState().get("programPriority") != null ? String.valueOf(getViewState().get("programPriority"))
					: null;
		} else {
			return getViewState().get("programPriority") != null
					? ProgramPriority.valueOf(String.valueOf(getViewState().get("programPriority"))).toString() : null;
		}
	}

	public void setProgramPriority(String programPriority) {
		getViewState().put("programPriority", programPriority);
	}

	public String getProgramPriorityInvestment() {
		if (!getView()) {
			return getViewState().get("programPriorityInvestment") != null
					? String.valueOf(getViewState().get("programPriorityInvestment")) : null;
		} else {
			return getViewState().get("programPriorityInvestment") != null ? ProgramPriorityInvestment
					.valueOf(String.valueOf(getViewState().get("programPriorityInvestment"))).toString() : null;
		}
	}

	public void setProgramPriorityInvestment(String programPriorityInvestment) {
		getViewState().put("programPriorityInvestment", programPriorityInvestment);
	}

	public String getSpecificObjective() {
		if (!getView()) {
			return getViewState().get("specificObjective") != null
					? String.valueOf(getViewState().get("specificObjective")) : null;
		} else {
			return getViewState().get("specificObjective") != null
					? SpecificObjective.valueOf(String.valueOf(getViewState().get("specificObjective"))).toString()
					: null;
		}
	}

	public void setSpecificObjective(String specificObjective) {
		getViewState().put("specificObjective", specificObjective);
	}

	public String getLeadPartnerAcronym() {
		return getViewState().get("leadPartnerAcronym") != null
				? String.valueOf(getViewState().get("leadPartnerAcronym")) : null;
	}

	public void setLeadPartnerAcronym(String leadPartnerAcronym) {
		getViewState().put("leadPartnerAcronym", leadPartnerAcronym);
	}

	public String getRegisteredOfficeAddress() {
		return getViewState().get("registeredOfficeAddress") != null
				? String.valueOf(getViewState().get("registeredOfficeAddress")) : null;
	}

	public void setRegisteredOfficeAddress(String registeredOfficeAddress) {
		getViewState().put("registeredOfficeAddress", registeredOfficeAddress);
	}

	public String getDepartment() {
		return getViewState().get("department") != null ? String.valueOf(getViewState().get("department")) : null;
	}

	public void setDepartment(String department) {
		getViewState().put("department", department);
	}

	public String getHeadOfficeAddress() {
		return getViewState().get("headOfficeAddress") != null ? String.valueOf(getViewState().get("headOfficeAddress"))
				: null;
	}

	public void setHeadOfficeAddress(String headOfficeAddress) {
		getViewState().put("headOfficeAddress", headOfficeAddress);
	}

	public String getProvinceDepartment() {
		if (!getView()) {
			return getViewState().get("provinceDepartment") != null
					? String.valueOf(getViewState().get("provinceDepartment")) : null;
		} else {
			return getViewState().get("provinceDepartment") != null
					? ProvinceDepartment.valueOf(String.valueOf(getViewState().get("provinceDepartment"))).toString()
					: null;
		}
	}

	public void setProvinceDepartment(String provinceDepartment) {
		getViewState().put("provinceDepartment", provinceDepartment);
	}

	public String getCountry() {
		if (!getView()) {
			return getViewState().get("country") != null ? String.valueOf(getViewState().get("country")) : null;
		} else {
			return getViewState().get("country") != null
					? Countries.valueOf(String.valueOf(getViewState().get("country"))).toString() : null;
		}
	}

	public void setCountry(String country) {
		getViewState().put("country", country);
	}

	public Boolean getOutsideAreaCooperation() {
		return getViewState().get("outsideAreaCooperation") != null
				? (Boolean) getViewState().get("outsideAreaCooperation") : null;
	}

	public void setOutsideAreaCooperation(Boolean outsideAreaCooperation) {
		getViewState().put("outsideAreaCooperation", outsideAreaCooperation);
	}

	public String getNameOfRepresentative() {
		return getViewState().get("nameOfRepresentative") != null
				? String.valueOf(getViewState().get("nameOfRepresentative")) : null;
	}

	public void setNameOfRepresentative(String nameOfRepresentative) {
		getViewState().put("nameOfRepresentative", nameOfRepresentative);
	}

	public String getPhone() {
		return getViewState().get("phone") != null ? String.valueOf(getViewState().get("phone")) : null;
	}

	public void setPhone(String phone) {
		getViewState().put("phone", phone);
	}

	public String getFax() {
		return getViewState().get("fax") != null ? String.valueOf(getViewState().get("fax")) : null;
	}

	public void setFax(String fax) {
		getViewState().put("fax", fax);
	}

	public String getEmail() {
		return getViewState().get("email") != null ? String.valueOf(getViewState().get("email")) : null;
	}

	public void setEmail(String email) {
		getViewState().put("email", email);
	}

	public String getPartnerType() {
		if (!getView()) {
			return getViewState().get("partnerType") != null ? String.valueOf(getViewState().get("partnerType")) : null;
		} else {
			return getViewState().get("partnerType") != null
					? PartnerTypes.valueOf(String.valueOf(getViewState().get("partnerType"))).toString() : null;
		}
	}

	public void setPartnerType(String partnerType) {
		getViewState().put("partnerType", partnerType);
	}

	public String getTaxCode() {
		return getViewState().get("taxCode") != null ? String.valueOf(getViewState().get("taxCode")) : null;
	}

	public void setTaxCode(String taxCode) {
		getViewState().put("taxCode", taxCode);
	}

	public List<SelectItem> getPartnerTypes() {
		return partnerTypes;
	}

	public void setPartnerTypes(List<SelectItem> partnerTypes) {
		this.partnerTypes = partnerTypes;
	}

	public List<SelectItem> getProvinceDepartments() {
		return provinceDepartments;
	}

	public void setProvinceDepartments(List<SelectItem> provinceDepartments) {
		this.provinceDepartments = provinceDepartments;
	}

	public List<SelectItem> getSpecificObjectives() {
		return specificObjectives;
	}

	public void setSpecificObjectives(List<SelectItem> specificObjectives) {
		this.specificObjectives = specificObjectives;
	}

	public List<SelectItem> getProgramPriorityInvestments() {
		return programPriorityInvestments;
	}

	public void setProgramPriorityInvestments(List<SelectItem> programPriorityInvestments) {
		this.programPriorityInvestments = programPriorityInvestments;
	}

	public List<SelectItem> getProgramPriorities() {
		return programPriorities;
	}

	public void setProgramPriorities(List<SelectItem> programPriorities) {
		this.programPriorities = programPriorities;
	}

	public List<SelectItem> getCountries() {
		return countries;
	}

	public void setCountries(List<SelectItem> countries) {
		this.countries = countries;
	}

	public List<SelectItem> getOutsideAreaCooperations() {
		return outsideAreaCooperations;
	}

	public void setOutsideAreaCooperations(List<SelectItem> outsideAreaCooperations) {
		this.outsideAreaCooperations = outsideAreaCooperations;
	}

	public List<SelectItem> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<SelectItem> statuses) {
		this.statuses = statuses;
	}

	public List<Documents> getDocList() {
		return docList;
	}

	public void setDocList(List<Documents> docList) {
		this.docList = docList;
	}

	public Integer getSelectedTab() {
		return getViewState().get("selectedTab") != null ? (Integer) getViewState().get("selectedTab") : 0;
	}

	public void setSelectedTab(Integer selectedTab) {
		getViewState().put("selectedTab", selectedTab);
	}

	public UploadedFile getNewDocumentUpFile() {
		return newDocumentUpFile;
	}

	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile) {
		this.newDocumentUpFile = newDocumentUpFile;
	}

	public Integer getEntityDownloadId() {
		return (Integer) getViewState().get("entityDownloadId");
	}

	public void setEntityDownloadId(Integer entityDownloadId) {
		this.getViewState().put("entityDownloadId", entityDownloadId);
	}

	public Integer getEntityDeleteId() {
		return entityDeleteId;
	}

	public void setEntityDeleteId(Integer entityDeleteId) {
		this.entityDeleteId = entityDeleteId;
	}

	public Integer getEntityEditId() {
		return (Integer) getViewState().get("entityEditId");
	}

	public void setEntityEditId(Integer entityEditId) {
		this.getViewState().put("entityEditId", entityEditId);
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getEditDocTitle() {
		return editDocTitle;
	}

	public void setEditDocTitle(String editDocTitle) {
		this.editDocTitle = editDocTitle;
	}

	public Boolean getShowDoc() {
		return showDoc;
	}

	public void setShowDoc(Boolean showDoc) {
		this.showDoc = showDoc;
	}

	public Boolean getView() {
		return getViewState().get("view") != null ? (Boolean) getViewState().get("view") : false;
	}

	public void setView(Boolean view) {
		getViewState().put("view", view);
	}

	public String getShowErrorMessage() {
		return showErrorMessage;
	}

	public void setShowErrorMessage(String showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	public boolean isValidationFailed() {
		return validationFailed;
	}

	public void setValidationFailed(boolean validationFailed) {
		this.validationFailed = validationFailed;
	}

	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}

	public String getStatus() {
		if (!getView()) {
			return getViewState().get("status") != null ? String.valueOf(getViewState().get("status")) : null;
		} else {
			return getViewState().get("status") != null
					? PotentialProjectStatuses.valueOf(String.valueOf(getViewState().get("status"))).toString() : null;
		}
	}

	public void setStatus(String status) {
		getViewState().put("status", status);
	}

	public Date getPresentationDate() {
		return (Date) (getViewState().get("presentationDate") != null ? getViewState().get("presentationDate") : null);
	}

	public void setPresentationDate(Date presentationDate) {
		getViewState().put("presentationDate", presentationDate);
	}

	public List<SelectItem> getOrganizationTypes() {
		return organizationTypes;
	}

	public void setOrganizationTypes(List<SelectItem> organizationTypes) {
		this.organizationTypes = organizationTypes;
	}
	
	public List<SelectItem> getDecisionsJmc() {
		return decisionsJmc;
	}

	public void setDecisionsJmc(List<SelectItem> decisionsJmc) {
		this.decisionsJmc = decisionsJmc;
	}

}
