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
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.CostManagement;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Localizations;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
public class StateManagmentBean extends EntityProjectEditBean<Projects> {

	private static List<SelectItem> states = new ArrayList<SelectItem>();

	private boolean showConfirmation;

	private String showAdditionalInfo;

	private UploadedFile documentFile;

	private Documents document;

	private String documentTitle;

	private ProjectStatesChanges projectStateChanges;

	private boolean showSaveButton;

	private List<SelectItem> categories;

	private String selectedCategory;

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	@Override
	public void GoBack() {
		this.goTo(PagesTypes.PROJECTINDEX);
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException {
		if (this.getProjectState().getCode() == ProjectState.Closed.getCode()
				|| this.getProjectState().getCode() == ProjectState.Revoked.getCode()) {
			this.FillOneState(this.getProjectState());
		} else if (this.getProjectState().getCode() == ProjectState.Suspended.getCode()) {
			this.FillStatesForSuspended();
		} else if (this.getProjectState().getCode() == ProjectState.FoundingEligible.getCode()) {
			this.FillStatesForFoundingEligible();
		} else if (this.getProjectState().getCode() == ProjectState.Actual.getCode()) {
			this.FillStatesForActual();
		}

		setEntity(this.getProject());
		if (getState() == null) {
			setState(String.valueOf(this.getEntity().getState().getId()));
		}

		this.fillCategories();
		this.CheckAddInfo();
		this.setShowSaveButton(this.CheckButtonSaveVisibility());
		stateChanged();
	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	private void CheckAddInfo()
			throws NumberFormatException, HibernateException, PersistenceBeanException, IOException {
		this.setProjectStateChanges(new ProjectStatesChanges());

		if (this.getProject().getState().getId().equals(Long.valueOf(ProjectState.Suspended.getCode()))
				&& (this.getState().equals(String.valueOf(ProjectState.Actual.getCode()))
						|| this.getState().equals(String.valueOf(ProjectState.Revoked.getCode())))) {
			this.setShowAdditionalInfo("");

			if (this.getViewState().get("Document") != null) {
				DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("Document");

				this.setDocumentTitle(docinfo.getName());

				String newFileName = FileHelper.newFileName(docinfo.getName());

				if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName)) == true) {
					this.setDocument(new Documents());
					this.getDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
					this.getDocument().setDocumentDate(docinfo.getDate());
					this.getDocument().setCreateDate(DateTime.getNow());
					this.getDocument().setDeleted(false);

					this.getDocument().setIsPublic(docinfo.getIsPublic());
					this.getDocument().setDocumentDate(docinfo.getDate());
					this.getDocument().setName(docinfo.getName());
					this.getDocument().setCategory(docinfo.getCategory());
					this.getDocument().setFileName(newFileName);
				}
			} else {
				this.setDocument(this.getProjectStateChanges().getDocument() == null ? new Documents()
						: this.getProjectStateChanges().getDocument());

				if (this.getDocument().getDocumentDate() == null) {
					this.getDocument().setDocumentDate(new Date());
				}

				if (this.getProjectStateChanges().getDocument() != null) {
					this.setDocumentTitle(this.getProjectStateChanges().getDocument().getFileName());
				}
			}

			if (this.getDocumentNumber() == null) {
				this.setDocumentNumber(this.getProjectStateChanges().getDocumentNumber());
			}

			if (this.getChangeReason() == null) {
				this.setChangeReason(this.getProjectStateChanges().getDescription());
			}

		} else {
			this.setDocument(null);
			this.setShowAdditionalInfo("none");
		}

		if (this.getDocument() != null && this.getDocument().getDocumentDate() == null) {
			this.getDocument().setDocumentDate(new Date());
		} else if (this.getDocument() == null) {
			this.setDocument(new Documents());
			this.getDocument().setDocumentDate(new Date());
		}
	}

	private boolean CheckButtonSaveVisibility()
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		return !this.getProject().getState().getId().equals(Long.valueOf(this.getState()));
	}

	public boolean isActualAvalable() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (!AccessGrantHelper.CheckBudgetsExists()) {
			return false;
		}

		PhisicalInitializations item = BeansFactory.PhisicalInizializations().getByProject(this.getProjectId());
		if (item == null) {
			return false;
		}

		List<Localizations> locals = BeansFactory.Localizations().LoadByProject(this.getProjectId());
		if (locals == null || locals.isEmpty()) {
			return false;
		}

		List<AnnualProfiles> profiles = BeansFactory.AnnualProfiles().LoadByProject(this.getProjectId());
		if (profiles == null || profiles.isEmpty()) {
			return false;
		}

		List<Procedures> procedures = BeansFactory.Procedures().LoadByProject(this.getProjectId());
		if (procedures != null && procedures.size() != 3) {
			for (Procedures procedure : procedures) {
				if (procedure.getExpectedStartDate() == null) {
					return false;
				}
			}
		}

		List<CFPartnerAnagraphs> partners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
		for (CFPartnerAnagraphs partner : partners) {
			CFPartnerAnagraphProject cfProj = BeansFactory.CFPartnerAnagraphProject()
					.LoadByPartner(Long.parseLong(this.getProjectId()), partner.getId());
			CFPartnerAnagraphCompletations completation = BeansFactory.CFPartnerAnagraphCompletations()
					.GetByCFAnagraph(cfProj.getId(), this.getProjectId());
			if (completation == null) {
				return false;
			}
		}

		return true;

	}

	public void FillStates() {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {
			for (ProjectStates item : BeansFactory.ProjectStates().Load()) {
				getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
			}
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void FillStatesForActual() {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {

			ProjectStates item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Actual);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Closed);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Revoked);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Suspended);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void FillStatesForFoundingEligible() {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {

			ProjectStates item = BeansFactory.ProjectStates().GetProjectState(ProjectState.FoundingEligible);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Actual);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void FillStatesForSuspended() {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {

			ProjectStates item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Actual);

			if (isActualAvalable()) {
				getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
			}
			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Suspended);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
			item = BeansFactory.ProjectStates().GetProjectState(ProjectState.Revoked);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));

		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void FillOneState(ProjectState state) {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {
			ProjectStates item = BeansFactory.ProjectStates().GetProjectState(state);
			getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void FillStatesWOAtual() {
		if (getStates() == null) {
			setStates(new ArrayList<SelectItem>());
		} else {
			getStates().clear();
		}

		try {
			for (ProjectStates item : BeansFactory.ProjectStates().Load()) {
				if (item.getCode() != ProjectState.Actual.getCode()) {
					getStates().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
				}
			}
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void getDoc() {
		if (this.getViewState().get("Document") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("Document");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getDocument().getName(), this.getDocument().getFileName(),
					null, this.getDocument().getSignflag()), false);
		}
	}

	public void deleteDoc() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException {
		if (this.getViewState().get("Document") != null) {
			this.getViewState().put("Document", null);
		} else if (this.getDocument() != null) {
			this.setDocument(null);
		}

		this.Page_Load();
	}

	public void saveDocument() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException {
		try {
			if (this.getDocumentFile() != null && this.getDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getDocumentFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getDocumentFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getDocument().getDocumentDate(),
						this.getDocumentFile().getName(), fileName, this.getDocument().getProtocolNumber(), cat,
						this.getDocument().getIsPublic(), this.getDocument().getSignflag());

				this.getViewState().put("Document", doc);
				this.setDocumentTitle(this.getDocumentFile().getName());
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		// TODO Auto-generated method stub
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		this.getSession().put("PAAMSN_Session_project_storage", null);
		Projects pr = BeansFactory.Projects().Load(this.getProjectId());

		ProjectStates oldState = pr.getState();
		pr.setState(BeansFactory.ProjectStates().Load(getState()));
		BeansFactory.Projects().SaveInTransaction(pr);
		this.getSessionBean().setIsActualSate(AccessGrantHelper.IsActual());
		this.getSessionBean().setIsActualSateAndAguRead(AccessGrantHelper.IsActualAndAguAccess());
		this.getSessionBean().setIsAguRead(AccessGrantHelper.IsAguReadAccess());
		this.getSessionBean().setIsProjectClosed(AccessGrantHelper.IsProjectClosed());

		if (this.getProjectStateChanges() != null) {
			this.getProjectStateChanges().setCreateDate(DateTime.getNow());
			this.getProjectStateChanges().setDeleted(false);
			this.getProjectStateChanges().setDescription(this.getChangeReason());
			this.getProjectStateChanges().setDocumentNumber(this.getDocumentNumber());
			this.getProjectStateChanges().setFromState(oldState);
			this.getProjectStateChanges().setToState(pr.getState());
			this.getProjectStateChanges().setProject(pr);

			if (this.getDocument() != null && this.getDocument().getFileName() != null
					&& !this.getDocument().getFileName().isEmpty()) {
				if (this.getDocument().getCreateDate() == null) {
					this.getDocument().setCreateDate(DateTime.getNow());
				}

				this.getDocument().setUser(this.getCurrentUser());
				this.getDocument().setProject(pr);
				this.getDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.ProjectStateChanges.getValue()));
				BeansFactory.Documents().Save(this.getDocument());
				this.getProjectStateChanges().setDocument(this.getDocument());
			}

			BeansFactory.ProjectStatesChanges().Save(this.getProjectStateChanges());
		}

		this.getSession().put("PAAMSN_Session_project_storage", pr);
	}

	public void PreSave() throws HibernateException, NumberFormatException, PersistenceBeanException, IOException {
		if (this.getState().equals(String.valueOf(ProjectState.Actual.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.FoundingEligible.getCode()) {
			this.FillPreRequstsForActual();
			if (this.CheckForActual()) {
				setShowConfirmation(true);
			}
		} else if (this.getState().equals(String.valueOf(ProjectState.Revoked.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.Suspended.getCode()) {
			setShowConfirmation(true);
		} else if (this.getState().equals(String.valueOf(ProjectState.Closed.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.Actual.getCode()) {
			this.FillPreRequstsForClosure();
			if (this.CheckForClosed()) {
				setShowConfirmation(true);
			}
		} else if (this.getState().equals(String.valueOf(ProjectState.Revoked.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.Actual.getCode()) {
			setShowConfirmation(true);
		} else {
			this.SaveEntity();
			this.AfterSave();
		}
	}

	private boolean CheckForClosed() throws PersistenceBeanException {
		boolean isClosureFilled = true;
		boolean isEfStDateFilled = true;
		boolean isEfEndDateFilled = true;
		boolean isCostConcilied = true;

		for (PhisicalInitializationToCoreIndicators item : BeansFactory.PhisicalInizializationToCoreIndicators()
				.LoadLastByProject(this.getProjectId())) {
			if (item.getClosureValue() == null || item.getClosureValue().isEmpty()) {
				isClosureFilled = false;
				break;
			}
		}

		if (isClosureFilled) {
			for (PhisicalInitializationToEmploymentIndicators item : BeansFactory
					.PhisicalInizializationToEmploymentIndicators().LoadLastByProject(this.getProjectId())) {
				if (item.getClosureValue() == null || item.getClosureValue().isEmpty()) {
					isClosureFilled = false;
					break;
				}
			}
		}

		if (isClosureFilled) {
			for (PhisicalInitializationToProgramIndicators item : BeansFactory
					.PhisicalInizializationToProgramRealizationIndicators().LoadLastByProject(this.getProjectId())) {
				if (item.getClosureValue() == null || item.getClosureValue().isEmpty()) {
					isClosureFilled = false;
					break;
				}
			}
		}

		for (Procedures item : BeansFactory.Procedures().LoadLastByProject(this.getProjectId())) {
			if (item.getEffectiveStartDate() == null) {
				isEfStDateFilled = false;
			}

			if (item.getEffectiveEndDate() == null) {
				isEfEndDateFilled = false;
			}
		}

		List<CostManagement> cms = BeansFactory.CostManagement().lostByProject(Long.valueOf(this.getProjectId()));
		if (cms == null || cms.isEmpty()) {
			isCostConcilied = false;
		} else if (cms != null && !cms.isEmpty()) {
			for (CostManagement cm : cms) {
				if (!cm.getConciliated()) {
					isCostConcilied = false;
					break;
				}
			}
		}
		if (this.getProjectAsse() == 4) {
			isCostConcilied = true;
		}

		if (this.getPrerequestsValue() == null) {
			this.setPrerequestsValue(new ArrayList<String>());
		} else {
			this.getPrerequestsValue().clear();
		}

		if (isClosureFilled) {
			this.getPrerequestsValue().add("1");
		}

		if (isEfEndDateFilled) {
			this.getPrerequestsValue().add("3");
		}

		if (isEfStDateFilled) {
			this.getPrerequestsValue().add("2");
		}
		if (isCostConcilied) {
			this.getPrerequestsValue().add("4");
		}

		if (!isClosureFilled || !isEfEndDateFilled || !isEfStDateFilled || !isCostConcilied) {
			return false;
		}

		return true;
	}

	private boolean CheckForActual() throws PersistenceBeanException {
		boolean isBudgetSourceFilled = true;
		boolean isGeneralBudgetFilled = true;
		boolean isPartnerBudgetFilled = true;
		boolean isPhisicalInitFilled = true;
		boolean isCFPartnerFilled = true;
		boolean isProjectComilationFilled = true;
		boolean isCFPartnerCompilationFilled = true;
		boolean isLocalizationFilled = true;
		boolean isAnnualProfileFilled = true;
		boolean isProceduresFilled = true;

		try {
			this.getProject();

			List<BudgetInputSourceDivided> listbudgets = BudgetInputSourceDividedBean.GetByProject(this.getProjectId());
			if (listbudgets == null || listbudgets.isEmpty()) {
				isBudgetSourceFilled = false;
			}

			List<CFPartnerAnagraphs> listCFAnagraphs = null;

			listCFAnagraphs = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
			if (listCFAnagraphs == null || listCFAnagraphs.isEmpty()
			/* || listCFAnagraphs.size() < 2 */ || (this.getProjectAsse() != 4 && listCFAnagraphs.size() < 2)
					|| (this.getProjectAsse() == 4 && listCFAnagraphs.size() < 1)) {
				isCFPartnerFilled = false;
			}

			List<GeneralBudgets> listGb = null;
			listGb = BeansFactory.GeneralBudgets().GetItemsForProject(Long.parseLong(this.getProjectId()));
			if (listGb != null && !listGb.isEmpty() && listGb.size() == listCFAnagraphs.size()) {
				for (GeneralBudgets gb : listGb) {
					if (gb.getBudgetTotal() == null) {
						isGeneralBudgetFilled = false;
						break;
					}
				}
			} else {
				isGeneralBudgetFilled = false;
			}

			ProjectIformationCompletations pic = null;
			pic = BeansFactory.ProjectIformationCompletations().LoadByProject(this.getProjectId());
			if (pic == null) {
				isProjectComilationFilled = false;
			}
			if (listCFAnagraphs.isEmpty()) {
				isPartnerBudgetFilled = false;
			} else {
				for (CFPartnerAnagraphs cfpa : listCFAnagraphs) {

					List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
							.GetByProjectAndPartner(this.getProjectId(), String.valueOf(cfpa.getId()), true);

					if (listPB == null || listPB.isEmpty()) {
						isPartnerBudgetFilled = false;
						break;
					}

				}
			}

			List<ProjectIndicators> comItems = BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "1");
			List<ProjectIndicators> progItems = BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(),
					"2");
			List<ProjectIndicators> resItems = BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "3");
			List<ProjectIndicators> projItems = BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(),
					"4");

			/*
			 * PhisicalInitializations item = BeansFactory
			 * .PhisicalInizializations() .getByProject(this.getProjectId()); if
			 * (item == null) { isPhisicalInitFilled = false; }
			 */

			if ((comItems == null || comItems.isEmpty()) && (progItems == null || progItems.isEmpty())
					&& (resItems == null || resItems.isEmpty()) && (projItems == null || projItems.isEmpty())) {
				isPhisicalInitFilled = false;
			}

			List<Localizations> locals = BeansFactory.Localizations().LoadByProject(this.getProjectId());
			if (locals == null || locals.isEmpty()) {
				isLocalizationFilled = false;
			}

			List<AnnualProfiles> profiles = BeansFactory.AnnualProfiles().LoadByProject(this.getProjectId());

			if (profiles != null && !profiles.isEmpty()) {
				double sum = 0d;
				for (AnnualProfiles profile : profiles) {
					sum += profile.getAmountToAchieve() == null ? 0d : profile.getAmountToAchieve();
				}

				if (listbudgets == null || listbudgets.isEmpty()
						|| !listbudgets.get(0).getTotalBudget().equals(NumberHelper.Round(sum))) {
					isAnnualProfileFilled = false;
				}
			} else {
				isAnnualProfileFilled = false;
			}

			List<Procedures> procedures = BeansFactory.Procedures().LoadByProject(this.getProjectId());
			if (procedures != null && procedures.size() >= 3) {
				int count = 0;
				for (Procedures procedure : procedures) {
					if (procedure.getStep() == 4) {
						continue;
					} else {
						count++;
					}
					if (procedure.getExpectedStartDate() == null) {
						isProceduresFilled = false;
						break;
					}
				}
				if (count != 3) {
					isProceduresFilled = false;
				}
			} else {
				isProceduresFilled = false;
			}

			List<CFPartnerAnagraphs> partners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
			if (partners.isEmpty()) {
				isCFPartnerCompilationFilled = false;
			} else {
				for (CFPartnerAnagraphs partner : partners) {
					CFPartnerAnagraphProject cfProj = BeansFactory.CFPartnerAnagraphProject()
							.LoadByPartner(Long.parseLong(this.getProjectId()), partner.getId());
					CFPartnerAnagraphCompletations completation = BeansFactory.CFPartnerAnagraphCompletations()
							.GetByCFAnagraph(cfProj.getId(), this.getProjectId());
					if (completation == null) {
						isCFPartnerCompilationFilled = false;
						break;
					}
				}
			}

			if (this.getPrerequestsValue() == null) {
				this.setPrerequestsValue(new ArrayList<String>());
			} else {
				this.getPrerequestsValue().clear();
			}
			if (isPhisicalInitFilled) {
				this.getPrerequestsValue().add("1");
			}
			if (isCFPartnerFilled) {
				this.getPrerequestsValue().add("2");
			}
			if (isBudgetSourceFilled) {
				this.getPrerequestsValue().add("3");
			}
			if (isGeneralBudgetFilled) {
				this.getPrerequestsValue().add("4");
			}
			if (isPartnerBudgetFilled) {
				this.getPrerequestsValue().add("5");
			}
			if (isProjectComilationFilled) {
				this.getPrerequestsValue().add("6");
			}
			if (isCFPartnerCompilationFilled) {
				this.getPrerequestsValue().add("7");
			}
			if (isLocalizationFilled) {
				this.getPrerequestsValue().add("8");
			}
			if (isAnnualProfileFilled) {
				this.getPrerequestsValue().add("9");
			}
			if (isProceduresFilled) {
				this.getPrerequestsValue().add("10");
			}

		} catch (Exception e) {
			log.error(e);
		} catch (Throwable tw) {
			log.error(tw);
		}

		if (!isAnnualProfileFilled || !isBudgetSourceFilled || !isCFPartnerCompilationFilled || !isCFPartnerFilled
				|| !isGeneralBudgetFilled || !isLocalizationFilled || !isPartnerBudgetFilled || !isPhisicalInitFilled
				|| !isProceduresFilled || !isProjectComilationFilled) {
			return false;
		}

		return true;
	}

	private void FillPreRequstsForClosure() {
		if (this.getPrerequests() == null) {
			this.setPrerequests(new ArrayList<SelectItem>());
		} else {
			this.getPrerequests().clear();
		}

		this.getPrerequests().add(new SelectItem("1", Utils.getString("closureValueIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("2", Utils.getString("effectiveStartDateIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("3", Utils.getString("effectiveEndDateIsMandatoryToMove")));
		if (this.getProjectAsse() != 4) {
			this.getPrerequests().add(new SelectItem("4", Utils.getString("costManagementMandatoryToMove")));
		}
	}

	private void FillPreRequstsForActual() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getPrerequests() == null) {
			this.setPrerequests(new ArrayList<SelectItem>());
		} else {
			this.getPrerequests().clear();
		}

		this.getPrerequests().add(new SelectItem("1", Utils.getString("phisicalInitIsMandatoryToMove")));
		if (this.getProject() != null && this.getProject().getTypology().getId() != 3)
			this.getPrerequests().add(new SelectItem("2", Utils.getString("cfPartnerIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("3", Utils.getString("budgetSourceIsMandatoryToMove")));
		if (this.getProject() != null && this.getProject().getTypology().getId() != 3)
			this.getPrerequests().add(new SelectItem("4", Utils.getString("generalBudgetIsMandatoryToMove")));
		if (this.getProject() != null && this.getProject().getTypology().getId() != 3)
			this.getPrerequests().add(new SelectItem("5", Utils.getString("partnerBudgetIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("6", Utils.getString("projectComilationIsMandatoryToMove")));
		if (this.getProject() != null && this.getProject().getTypology().getId() != 3)
			this.getPrerequests().add(new SelectItem("7", Utils.getString("cfPartnerCompilationIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("8", Utils.getString("localizationIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("9", Utils.getString("annualProfileIsMandatoryToMove")));
		this.getPrerequests().add(new SelectItem("10", Utils.getString("proceduresFilledIsMandatoryToMove")));
	}

	public void stateChanged(ValueChangeEvent event) throws IOException {
		try {
			this.setState((String) event.getNewValue());

			stateChanged();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}
	}

	private void stateChanged() throws PersistenceBeanException, IOException {
		if (this.getState().equals(String.valueOf(ProjectState.Closed.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.Actual.getCode()) {
			if (this.CheckForClosed()) {
				this.CheckAddInfo();
				this.setShowSaveButton(this.CheckButtonSaveVisibility());
			}
			this.FillPreRequstsForClosure();

		} else if (this.getState().equals(String.valueOf(ProjectState.Actual.getCode()))
				&& this.getEntity().getState().getCode() == ProjectState.FoundingEligible.getCode()) {
			if (this.CheckForActual()) {
				this.CheckAddInfo();
				this.setShowSaveButton(this.CheckButtonSaveVisibility());
			}
			this.FillPreRequstsForActual();
		} else {
			this.getPrerequests().clear();
			this.CheckAddInfo();
			this.setShowSaveButton(this.CheckButtonSaveVisibility());
		}
	}

	/**
	 * Sets states
	 * 
	 * @param states
	 *            the states to set
	 */
	public void setStates(List<SelectItem> states) {
		StateManagmentBean.states = states;
	}

	/**
	 * Gets states
	 * 
	 * @return states the states
	 */
	public List<SelectItem> getStates() {
		return states;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.getViewState().put("state", state);
	}

	/**
	 * Gets state
	 * 
	 * @return state the state
	 */
	public String getState() {
		return (String) this.getViewState().get("state");
	}

	/**
	 * Sets showConfirmation
	 * 
	 * @param showConfirmation
	 *            the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	/**
	 * Gets showConfirmation
	 * 
	 * @return showConfirmation the showConfirmation
	 */
	public boolean getShowConfirmation() {
		return showConfirmation;
	}

	/**
	 * Sets showAdditionalInfo
	 * 
	 * @param showAdditionalInfo
	 *            the showAdditionalInfo to set
	 */
	public void setShowAdditionalInfo(String showAdditionalInfo) {
		this.showAdditionalInfo = showAdditionalInfo;
	}

	/**
	 * Gets showAdditionalInfo
	 * 
	 * @return showAdditionalInfo the showAdditionalInfo
	 */
	public String getShowAdditionalInfo() {
		return showAdditionalInfo;
	}

	/**
	 * Sets changeReason
	 * 
	 * @param changeReason
	 *            the changeReason to set
	 */
	public void setChangeReason(String changeReason) {
		this.getViewState().put("changeReason", changeReason);
	}

	/**
	 * Gets changeReason
	 * 
	 * @return changeReason the changeReason
	 */
	public String getChangeReason() {
		return (String) this.getViewState().get("changeReason");
	}

	/**
	 * Sets documentNumber
	 * 
	 * @param documentNumber
	 *            the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.getViewState().put("docNumber", documentNumber);
	}

	/**
	 * Gets documentNumber
	 * 
	 * @return documentNumber the documentNumber
	 */
	public String getDocumentNumber() {
		return (String) this.getViewState().get("docNumber");
	}

	/**
	 * Sets documentFile
	 * 
	 * @param documentFile
	 *            the documentFile to set
	 */
	public void setDocumentFile(UploadedFile documentFile) {
		this.documentFile = documentFile;
	}

	/**
	 * Gets documentFile
	 * 
	 * @return documentFile the documentFile
	 */
	public UploadedFile getDocumentFile() {
		return documentFile;
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
	 * Gets document
	 * 
	 * @return document the document
	 */
	public Documents getDocument() {
		return document;
	}

	/**
	 * Sets documentTitle
	 * 
	 * @param documentTitle
	 *            the documentTitle to set
	 */
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	/**
	 * Gets documentTitle
	 * 
	 * @return documentTitle the documentTitle
	 */
	public String getDocumentTitle() {
		return documentTitle;
	}

	/**
	 * Sets projectStateChanges
	 * 
	 * @param projectStateChanges
	 *            the projectStateChanges to set
	 */
	public void setProjectStateChanges(ProjectStatesChanges projectStateChanges) {
		this.projectStateChanges = projectStateChanges;
	}

	/**
	 * Gets projectStateChanges
	 * 
	 * @return projectStateChanges the projectStateChanges
	 */
	public ProjectStatesChanges getProjectStateChanges() {
		return projectStateChanges;
	}

	/**
	 * Sets showSaveButton
	 * 
	 * @param showSaveButton
	 *            the showSaveButton to set
	 */
	public void setShowSaveButton(boolean showSaveButton) {
		this.showSaveButton = showSaveButton;
	}

	/**
	 * Gets showSaveButton
	 * 
	 * @return showSaveButton the showSaveButton
	 */
	public boolean isShowSaveButton() {
		return showSaveButton;
	}

	/**
	 * Sets prerequests
	 * 
	 * @param prerequests
	 *            the prerequests to set
	 */
	public void setPrerequests(List<SelectItem> prerequests) {
		this.getViewState().put("prerequests", prerequests);
	}

	/**
	 * Gets prerequests
	 * 
	 * @return prerequests the prerequests
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getPrerequests() {
		if (this.getViewState().get("prerequests") == null) {
			this.setPrerequests(new ArrayList<SelectItem>());
		}

		return (List<SelectItem>) this.getViewState().get("prerequests");
	}

	/**
	 * Sets prerequestsValue
	 * 
	 * @param prerequestsValue
	 *            the prerequestsValue to set
	 */
	public void setPrerequestsValue(List<String> prerequestsValue) {
		this.getViewState().put("prerequestsValue", prerequestsValue);
	}

	/**
	 * Gets prerequestsValue
	 * 
	 * @return prerequestsValue the prerequestsValue
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPrerequestsValue() {
		if (this.getViewState().get("prerequestsValue") == null) {
			this.setPrerequestsValue(new ArrayList<String>());
		}

		return (List<String>) this.getViewState().get("prerequestsValue");

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

}
