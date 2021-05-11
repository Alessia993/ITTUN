package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ImplementingTool;
import com.infostroy.paamns.common.enums.MacroStrategy;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Asses;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CPT;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CUP;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Typologies;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.HierarchicalLevel;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Sergey Zorin
 * @author Sergey Manoylo InfoStroy Co., 2010. modified by Ingloba360
 * 
 */
public class ProjectStartBean extends EntityEditBean<Projects> {

	private Projects currentProject;

	/**
	 * Stores project code
	 */
	private String code;

	private String description;

	private String descriptionEng;

	private String organization;

	private Date sustainDate;

	private String title;

	private String projectTypology;

	private ProjectStates projectStates;

	private String expectedResults;

	private String asse;

	private String specificGoal;

	// private String thematicGoal;

	private String priorityInvestment;

	private String implementingToolCode;

	private String macroStrategyCode;

	// private String qsnGoal;

	@SuppressWarnings("unused")
	private String classificationTypeCode;

	private String classificationSpecificationCode;

	private String priorReasons;

	private String activationProcedure;

	private String cup;

	@SuppressWarnings("unused")
	private String cupLevel1;

	@SuppressWarnings("unused")
	private String cupLevel2;

	private String cpt;

	private boolean cup1Changed;

	private boolean cup2Changed;

	// / Lists for combo boxes bounding

	private List<SelectItem> listTypologies;

	private List<SelectItem> listAsses;

	private List<SelectItem> listSpecificGoals;

	// private List<SelectItem> QSNGoals;

	private List<SelectItem> listThematicGoals;

	private List<SelectItem> listPriorityInvestments;

	// private List<SelectItem> listQSNGoals;

	private List<SelectItem> listImplementingTool;

	private List<SelectItem> listMacroStrategy;

	private List<SelectItem> listClassificationTypeCodes;

	private List<SelectItem> listClassificationSpecificationCodes;

	private List<SelectItem> listProritaryReasons;

	private List<SelectItem> listActivationProcedures;

	private List<SelectItem> listCPT;

	private List<SelectItem> listCUP;

	private List<SelectItem> listCUPLevel1;

	private List<SelectItem> listCUPLevel2;

	private boolean hasRights;

	private String level2CupVisibility;

	private String level3CupVisibility;

	private boolean canEdit;

	private boolean editMode;

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	public void GoBack() {
		if (this.getSession().get("projectEditBackToSearch") != null
				&& (Boolean) this.getSession().get("projectEditBackToSearch") == true) {
			this.goTo(PagesTypes.SEARCH);
		} else {
			this.goTo(PagesTypes.PROJECTLIST);
		}
	}

	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException,
			PersistenceBeanException, PersistenceBeanException {
		this.setCanEdit(true);
		this.setOrganization(Utils.getString("projectMA"));

		if (this.getSession().get("projectEditShowOnly") == null) {
			this.getSession().put("projectEditShowOnly", true);
		}

		this.setEditMode(!Boolean.valueOf(String.valueOf(this.getSession().get("projectEditShowOnly"))));

		if (this.getSessionBean().isAGUW() || this.getSessionBean().isSTCW()) {
			this.setHasRights(true);
		} else {
			this.setHasRights(false);
		}

		if (this.getSession().get("projectEdit") != null) {
			this.currentProject = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("projectEdit")));

			if ((this.getSessionBean().isAGU() && this.getSessionBean().getListCurrentUserRolesSize() == 1)
					&& this.currentProject.getAsse() != 5) {
				this.setCanEdit(false);
			} else if (this.getSessionBean().isSTC() && this.getSessionBean().getListCurrentUserRolesSize() == 1
					&& this.currentProject.getAsse() == 5) {
				this.setCanEdit(false);
			} else {
				this.setCanEdit(true);
			}

			if (!this.isEditMode()) {
				this.setCanEdit(false);
			}
			this.setDescription(this.currentProject.getDescripion());
			this.setDescriptionEng(this.currentProject.getDescripionEng());
			this.setExpectedResults(this.currentProject.getExpectedResults());
			this.setOrganization(this.currentProject.getOrganization());
			this.setSustainDate(this.currentProject.getSustainDate());
			this.setCode(this.currentProject.getCode());
			this.setTitle(this.currentProject.getTitle());
			this.setProjectStates(this.currentProject.state);

			if ((this.currentProject.getTypology() != null && this.getProjectTypology() == null)) {
				this.setProjectTypology(String.valueOf(this.currentProject.getTypology().getId()));
			}

			if ((this.currentProject.getAsse() != 0 && this.getAsse() == null)) {
				this.setAsse(String.valueOf(this.currentProject.getAsse()));
			}

			if ((this.currentProject.getSpecificGoal() != null && this.getSpecificGoal() == null)) {
				this.setSpecificGoal(String.valueOf(this.currentProject.getSpecificGoal().getId()));
			}

			if (this.currentProject.getThematicGoal() != null && this.getThematicGoal() == null) {
				this.setThematicGoal(String.valueOf(this.currentProject.getThematicGoal().getId()));
			}

			if (this.currentProject.getPrioritaryGoal() != null) {
				this.setPriorityInvestment(String.valueOf(this.currentProject.getPrioritaryGoal().getId()));
			}

			/*
			 * if (this.currentProject.getQsnGoal() != null) {
			 * this.setQsnGoal(String.valueOf(this.currentProject.getQsnGoal()
			 * .getId())); }
			 */

			if (this.currentProject.getImplementingToolCode() != null) {
				this.setImplementingToolCode(this.currentProject.getImplementingToolCode());
			}

			if (this.currentProject.getMacroStrategyCode() != null) {
				this.setMacroStrategyCode(this.currentProject.getMacroStrategyCode());
			}

			if (this.currentProject.getPrioritaryReason() != null) {
				this.setPriorReasons(String.valueOf(this.currentProject.getPrioritaryReason().getId()));
			}

			if (this.currentProject.getActivationProcedure() != null) {
				this.setActivationProcedure(String.valueOf(this.currentProject.getActivationProcedure().getId()));
			}

			if (this.currentProject.getCup() != null) {
				if (this.getCup() == null) {
					this.setCup(String.valueOf(this.currentProject.getCup().getId()));
				}

				boolean changed = false;
				if (this.getCupLevel1() == null || this.getCupLevel1().equals(SelectItemHelper.getFirst().getValue())) {
					this.setCupLevel2(String.valueOf(this.currentProject.getCup().getParent().getId()));
					changed = true;
				}

				if (this.getCupLevel2() == null || this.getCupLevel2().equals(SelectItemHelper.getFirst().getValue())) {
					this.setCupLevel1(String.valueOf(this.currentProject.getCup().getParent().getParent().getId()));
				} else {
					if (changed) {
						this.setCupLevel1(String.valueOf(this.currentProject.getCup().getParent().getParent().getId()));
					}
				}

				this.setLevel2CupVisibility("");
				this.setLevel3CupVisibility("");
			} else {
				this.setLevel2CupVisibility("none");
				this.setLevel3CupVisibility("none");

			}

			if (this.getCpt() != null) {
				this.setCpt(this.currentProject.getCpt().getValue());
			}
		} else {
			if (this.getSpecificGoal() == null
					|| this.getSpecificGoal().equals(String.valueOf(SelectItemHelper.getFirst().getValue()))) {
				this.setThematicGoal(String.valueOf(SelectItemHelper.getFirst().getValue()));
			}

		}
		this.FillComboBoxes();
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException {

	}

	@Override
	public void SaveEntity()
			throws HibernateException, PersistenceBeanException, NumberFormatException, PersistenceBeanException {
		Projects entityToSave = new Projects();
		if (this.getSession().get("projectEdit") != null) {
			entityToSave = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("projectEdit")));
		} else {
			entityToSave.setState(BeansFactory.ProjectStates().GetProjectState(ProjectState.FoundingEligible));
		}
		entityToSave.setDescripion(this.getDescription());
		entityToSave.setExpectedResults(this.getExpectedResults());
		entityToSave.setDescripionEng(this.getDescriptionEng());
		this.setOrganization(Utils.getString("projectMA"));
		entityToSave.setSustainDate(this.getSustainDate());
		entityToSave.setCode(this.getCode());
		entityToSave.setTitle(this.getTitle());
		entityToSave.setTypology(BeansFactory.Typologies().Load(this.getProjectTypology()));
		entityToSave.setAsse(Integer.valueOf(this.getAsse()));
		entityToSave.setSpecificGoal(BeansFactory.SpecificGoals().Load(this.getSpecificGoal()));
		if (this.getThematicGoal() != null && !this.getThematicGoal().trim().isEmpty()) {
			entityToSave.setThematicGoal(BeansFactory.SpecificGoals().Load(this.getThematicGoal()));
		} else if (this.getThematicGoal() == null || this.getThematicGoal().trim().isEmpty()) {
			entityToSave.setThematicGoal(null);
		}
		if (this.getPriorityInvestment() != null && !this.getPriorityInvestment().trim().isEmpty()) {
			entityToSave.setPrioritaryGoal(BeansFactory.SpecificGoals().Load(this.getPriorityInvestment()));
		} else if (this.getPriorityInvestment() == null || this.getPriorityInvestment().trim().isEmpty()) {
			entityToSave.setPrioritaryGoal(null);
		}

		entityToSave.setImplementingToolCode(this.getImplementingToolCode());
		entityToSave.setMacroStrategyCode(this.getMacroStrategyCode());
		// entityToSave.setClassificationTypeCode(this.getClassificationTypeCode());
		// entityToSave.setClassificationSpecificationCode(this.getClassificationSpecificationCode());
		// entityToSave.setQsnGoal(BeansFactory.QSNGoal().Load(this.getQsnGoal()));
		// entityToSave.setPrioritaryReason(BeansFactory.PrioritaryReasons().Load(this.getPriorReasons()));
		entityToSave
				.setActivationProcedure(BeansFactory.ActivationProcedureAnagraph().Load(this.getActivationProcedure()));
		entityToSave.setCup(BeansFactory.CUP().Load(this.cup));
		// entityToSave.setCpt(BeansFactory.CPT().Load(this.cpt));
		List<HierarchicalLevel> hlList = BeansFactory.HierarchicalLevel().GetAll();
		/*
		 * for (HierarchicalLevel hl : hlList) { // eliminata questa parte da
		 * ITMAL perchè non permetteva il salvataggio del progetto in qunato in
		 * ITTUN sono stati eliminati oboiettivo tematico e priorità di
		 * investimento if (Integer.valueOf(this.getAsse()) == 4) { if (
		 * "Assistenza Tecnica".equals(hl.getDescription())) {
		 * entityToSave.setHierarchicalLevelCode(hl.getCodHierarchicalLevel());
		 * break; } } else { if
		 * (entityToSave.getThematicGoal().getThematic_object().contains(hl.
		 * getDescription())) {
		 * entityToSave.setHierarchicalLevelCode(hl.getCodHierarchicalLevel());
		 * break; } }
		 * 
		 * }
		 */
		BeansFactory.Projects().Save(entityToSave);
	}

	private void FillComboBoxes() throws HibernateException, PersistenceBeanException, PersistenceBeanException {
		this.FillPriorThemes();
		this.FillActivationProcedures();
		this.FillCUP(false);
		this.FillCPT();
		this.fillImplementingTools();
		this.fillMacroStrategies();

		this.FillTypologies();
		this.FillAsses();
		this.FillSpecificGoals();
		this.FillThematicGoals();
		this.FillPrioritaryGoals();
		// this.FillQSNGoals();

		// this.fillClassificationTypes();
		// this.fillClassificationSpecifications();

	}

	/**
	 * 
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 */
	private void FillTypologies() throws HibernateException, PersistenceBeanException, PersistenceBeanException {
		// Fill typologies

		this.listTypologies = new ArrayList<SelectItem>();
		this.listTypologies.clear();
		this.listTypologies.add(SelectItemHelper.getFirst());

		List<Typologies> listTypes = new ArrayList<Typologies>();

		if (this.getSessionBean().isAGU()) {
			listTypes = BeansFactory.Typologies().Load();
		} else {
			listTypes = BeansFactory.Typologies().Load(false);
		}

		for (Typologies type : listTypes) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(type.getId()));

			if (type.getValue() != null) {
				item.setLabel(type.getValue());
			}

			this.listTypologies.add(item);
		}

		if (this.getSession().get("projectEdit") != null) {
			if (this.getProjectTypology() == null
					|| this.getProjectTypology().equals(SelectItemHelper.getFirst().getValue())) {
				if (this.currentProject.getTypology() != null) {
					this.setProjectTypology(String.valueOf(BeansFactory.Projects()
							.Load(String.valueOf(this.getSession().get("projectEdit"))).getTypology().getId()));
				}
			}
		}

		// if (this.getSession().get("projectEdit") != null) {
		//
		// if (this.projectTypology == null) {
		// if (this.currentProject.getTypology() != null) {
		// this.setProjectTypology(String.valueOf(BeansFactory.Projects()
		// .Load(String.valueOf(this.getSession().get("projectEdit"))).getTypology().getId()));
		// }
		// }
		//
		// } else {
		// if (this.projectTypology == null) {
		// this.setProjectTypology((String)
		// SelectItemHelper.getFirst().getValue());
		// }
		// }
	}

	private void FillAsses() throws PersistenceBeanException {
		// Fill asses according to user role

		this.listAsses = new ArrayList<SelectItem>();
		this.listAsses.add(SelectItemHelper.getFirst());

		if (this.isEditMode()) {

			if (this.getSessionBean().isSTC()) {
				if (this.getProjectTypology() != null && !this.getProjectTypology().isEmpty()
						&& (this.getProjectTypology().equals("Assistenza Tecnica")
								|| this.getProjectTypology().equals("3"))) {
					int nTechAssistanceAssesLimit = 13; // imposto il limite per
														// il
														// caricamento delle
														// risorse
														// dedicate alla
														// tipologia
														// Assistenza Tecnica
														// partendo
														// dall'indice 5 per
														// eviatare il
														// caso Asse 4 di ITTUN
					for (int i = 4; i < nTechAssistanceAssesLimit; i++) {
						SelectItem item = new SelectItem();
						item.setValue(Asses.values()[i].toString());
						item.setLabel(Utils.getString("Resources", "programPriorityASSE_" + i, null));
						this.listAsses.add(item);
					}
				} else {
					int nAsses = 3; // in ITMAL era 4

					for (int i = 0; i < nAsses; i++) {
						SelectItem item = new SelectItem();
						item.setValue(Asses.values()[i].toString());
						item.setLabel(Utils.getString("Resources", "asse", null) + " " + Asses.values()[i].toString());

						this.listAsses.add(item);
					}
				}
			}
		} else {
			if (this.getProjectTypology() != null && this.getProjectTypology().equals("Assistenza Tecnica")
					|| this.getProjectTypology().equals("3")) {
				for (int i = 4; i < 13; i++) {
					SelectItem item = new SelectItem();
					item.setValue(Asses.values()[i].toString());
					item.setLabel(Utils.getString("Resources", "programPriorityASSE_" + i, null));
					this.listAsses.add(item);
				}
			} else {
				for (int i = 0; i < 3; i++) { // in ITMAL era 4

					SelectItem item = new SelectItem();
					item.setValue(Asses.values()[i].toString());
					item.setLabel(Utils.getString("Resources", "asse", null) + " " + Asses.values()[i].toString());

					this.listAsses.add(item);
				}
			}
		}

		if (this.getSession().get("projectEdit") != null) {
			if (this.getAsse() == null || this.getAsse().equals(SelectItemHelper.getFirst().getValue())) {
				if (this.currentProject.getAsse() != 0) {
					this.setAsse(String.valueOf(BeansFactory.Projects()
							.Load(String.valueOf(this.getSession().get("projectEdit"))).getAsse()));
				}
			}
		}

	}

	private void FillSpecificGoals() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.listSpecificGoals = new ArrayList<SelectItem>();
		this.listSpecificGoals.add(SelectItemHelper.getFirst());

		List<SpecificGoals> listSpecificGoals = new ArrayList<SpecificGoals>();

		if (this.getAsse() != null && !this.getAsse().equals("0")
				&& !this.getAsse().equals(SelectItemHelper.getFirst().getValue())) {
			listSpecificGoals = BeansFactory.SpecificGoals().GetSpecificGoalsForAsse(String.valueOf(Integer.parseInt(this.getAsse())-1));
		}

		for (SpecificGoals specGoal : listSpecificGoals) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(specGoal.getId()));
			item.setLabel(specGoal.getCode() + " " + specGoal.getValue());
			this.listSpecificGoals.add(item);
		}
	}

	private void FillThematicGoals() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.listThematicGoals = new ArrayList<SelectItem>();
		this.listThematicGoals.add(SelectItemHelper.getFirst());

		List<SpecificGoals> listThematicGoals = new ArrayList<SpecificGoals>();
		if (this.getSpecificGoal() != null && !this.getSpecificGoal().equals("0")) {
			// listThematicGoals =
			// BeansFactory.SpecificGoals().GetAllSpecificGoals();
			String code = BeansFactory.SpecificGoals().Load(this.getSpecificGoal()).getCode();
			listThematicGoals = BeansFactory.SpecificGoals().GetThematicGoalsForSpecificGoals(code);
		}
		Collections.sort(listThematicGoals, new Comparator<SpecificGoals>() {
			@Override
			public int compare(SpecificGoals goal1, SpecificGoals goal2) {

				return goal1.getThematic_object().compareTo(goal2.getThematic_object());
			}
		});
		for (SpecificGoals specGoal : listThematicGoals) {
			SelectItem item = new SelectItem();
			if (specGoal.getThematic_object() != null && !specGoal.getThematic_object().isEmpty()) {
				item.setValue(String.valueOf(specGoal.getId()));
				item.setLabel(specGoal.getThematic_object());
				this.listThematicGoals.add(item);
			}

		}
	}

	private void FillPrioritaryGoals() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.listPriorityInvestments = new ArrayList<SelectItem>();
		this.listPriorityInvestments.add(SelectItemHelper.getFirst());

		List<SpecificGoals> listPriorityInvestments = new ArrayList<SpecificGoals>();
		// if (this.getThematicGoal() != null &&
		// !this.getThematicGoal().equals("0")
		// ) {
		// listPriorityInvestments =
		// BeansFactory.SpecificGoals().GetAllSpecificGoals();
		// }

		if (this.getThematicGoal() != null && !this.getThematicGoal().equals("0")) {

			String code = BeansFactory.SpecificGoals().Load(this.getThematicGoal()).getCode();
			listPriorityInvestments = BeansFactory.SpecificGoals().GetPrioritaryGoalsForThematicGoal(code);// GetAllSpecificGoals();
		}
		Collections.sort(listPriorityInvestments, new Comparator<SpecificGoals>() {
			@Override
			public int compare(SpecificGoals goal1, SpecificGoals goal2) {

				return goal1.getPriority_investment().compareTo(goal2.getPriority_investment());
			}
		});
		for (SpecificGoals specGoal : listPriorityInvestments) {
			SelectItem item = new SelectItem();
			if (specGoal.getPriority_investment() != null && !specGoal.getPriority_investment().isEmpty()) {
				item.setValue(String.valueOf(specGoal.getId()));
				item.setLabel(specGoal.getPriority_investment());
				this.listPriorityInvestments.add(item);
			}

		}
	}

	/*
	 * private void FillQSNGoals() throws HibernateException,
	 * NumberFormatException, PersistenceBeanException { this.listQSNGoals = new
	 * ArrayList<SelectItem>();
	 * this.listQSNGoals.add(SelectItemHelper.getFirst());
	 * 
	 * if (this.getSession().get("projectEdit") != null) { List<QSNGoal>
	 * listQSNGoals = new ArrayList<QSNGoal>();
	 * 
	 * try { if (this.currentProject.getAsse() != 0) { listQSNGoals =
	 * BeansFactory.QSNGoal().GetByAsse( String.valueOf(BeansFactory .Projects()
	 * .Load(String.valueOf(this.getSession().get( "projectEdit"))).getAsse()));
	 * }
	 * 
	 * if (!this.getAsse().equals( SelectItemHelper.getFirst().getValue())) {
	 * listQSNGoals = BeansFactory.QSNGoal().GetByAsse( this.getAsse()); } }
	 * catch (PersistenceBeanException e) { log.error(e); e.printStackTrace(); }
	 * 
	 * for (QSNGoal specGoal : listQSNGoals) { if
	 * (specGoal.getSpecificObjectiveDescription() != null) { SelectItem item =
	 * new SelectItem(); item.setValue(String.valueOf(specGoal.getId()));
	 * item.setLabel(specGoal.getCodeSpecificObjective() + " " +
	 * specGoal.getSpecificObjectiveDescription()); this.listQSNGoals.add(item);
	 * } } } else { if (this.getAsse() != null) { if (!this.getAsse().equals(
	 * SelectItemHelper.getFirst().getValue())) { List<QSNGoal> listQSNGoals =
	 * new ArrayList<QSNGoal>();
	 * 
	 * try { listQSNGoals = BeansFactory.QSNGoal().GetByAsse( this.getAsse()); }
	 * catch (PersistenceBeanException e) { log.error(e); e.printStackTrace(); }
	 * 
	 * for (QSNGoal specGoal : listQSNGoals) { if
	 * (specGoal.getSpecificObjectiveDescription() != null) { SelectItem item =
	 * new SelectItem(); item.setValue(String.valueOf(specGoal.getId()));
	 * item.setLabel(specGoal.getCodeSpecificObjective() + " " +
	 * specGoal.getSpecificObjectiveDescription()); this.listQSNGoals.add(item);
	 * } } } } } }
	 */

	/**
	 * Fill implementing tool list.
	 */
	private void fillImplementingTools() {

		this.listImplementingTool = new ArrayList<SelectItem>();
		this.listImplementingTool.add(SelectItemHelper.getFirst());

		// Locale currentLocale =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		Locale currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

		for (ImplementingTool implementingTool : ImplementingTool.values()) {
			SelectItem item = new SelectItem();
			item.setLabel(resourceBundle.getString(implementingTool.description));
			item.setValue(implementingTool.code);

			this.listImplementingTool.add(item);
		}
	}

	/**
	 * Fill macro strategy list.
	 */
	private void fillMacroStrategies() {

		this.listMacroStrategy = new ArrayList<SelectItem>();
		this.listMacroStrategy.add(SelectItemHelper.getFirst());

		// Locale currentLocale =
		// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		Locale currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

		for (MacroStrategy macroStrategy : MacroStrategy.values()) {
			SelectItem item = new SelectItem();
			item.setLabel(resourceBundle.getString(macroStrategy.description));
			item.setValue(macroStrategy.code);

			this.listMacroStrategy.add(item);
		}
	}

	/**
	 * Fill classification type list.
	 * 
	 * @throws NumberFormatException
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 */
	// private void fillClassificationTypes() throws NumberFormatException,
	// HibernateException, PersistenceBeanException {
	// this.listClassificationTypeCodes = new ArrayList<SelectItem>();
	// this.listClassificationTypeCodes.add(SelectItemHelper.getFirst());
	//
	// Locale currentLocale =
	// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	// ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources",
	// currentLocale);
	//
	// for (ClassificationType classificationType : ClassificationType.values())
	// {
	// SelectItem item = new SelectItem();
	// item.setLabel(resourceBundle.getString(classificationType.description));
	// item.setValue(classificationType.code);
	//
	// this.listClassificationTypeCodes.add(item);
	// }
	//
	// if (this.getSession().get("projectEdit") != null) {
	// if (this.getClassificationTypeCode() == null ||
	// this.getClassificationTypeCode().equals(SelectItemHelper.getFirst().getValue()))
	// {
	// if ((this.currentProject.getClassificationTypeCode() != null) &&
	// !this.currentProject.getClassificationTypeCode().equals(SelectItemHelper.getFirst().getValue()))
	// {
	// this.setClassificationTypeCode(BeansFactory.Projects().Load(String.valueOf(this.getSession().get("projectEdit"))).getClassificationTypeCode());
	// }
	// }
	// }
	// }

	/**
	 * Fill classification specification list.
	 */
	// private void fillClassificationSpecifications() {
	// this.listClassificationSpecificationCodes = new ArrayList<SelectItem>();
	// this.listClassificationSpecificationCodes.add(SelectItemHelper.getFirst());
	//
	// if ((this.getClassificationTypeCode() != null) &&
	// !this.getClassificationTypeCode().equals(SelectItemHelper.getFirst().getValue()))
	// {
	// Locale currentLocale =
	// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	// ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources",
	// currentLocale);
	//
	// Map<String, String> classificationSpecificationMap =
	// ClassificationSpecification.getClassificationSpecification(this.getClassificationTypeCode());
	// for (String key : classificationSpecificationMap.keySet()) {
	// SelectItem item = new SelectItem();
	// item.setLabel(resourceBundle.getString(classificationSpecificationMap.get(key)));
	// item.setValue(key);
	//
	// this.listClassificationSpecificationCodes.add(item);
	// }
	// }
	// }

	private void FillPriorThemes() throws HibernateException, NumberFormatException, PersistenceBeanException {
		this.listProritaryReasons = new ArrayList<SelectItem>();
		this.listProritaryReasons.add(SelectItemHelper.getFirst());

		if (this.getSession().get("projectEdit") != null) {
			List<PrioritaryReasons> listQSNGoals = new ArrayList<PrioritaryReasons>();

			try {
				if (this.currentProject.getAsse() != 0) {
					listQSNGoals = BeansFactory.PrioritaryReasons().GetByAsse(String.valueOf(BeansFactory.Projects()
							.Load(String.valueOf(this.getSession().get("projectEdit"))).getAsse()));
				}

				if (!this.getAsse().equals(SelectItemHelper.getFirst().getValue())) {
					listQSNGoals = BeansFactory.PrioritaryReasons().GetByAsse(this.getAsse());
				}
			} catch (PersistenceBeanException e) {
				log.error(e);
				e.printStackTrace();
			}

			for (PrioritaryReasons priorReason : listQSNGoals) {
				StringBuilder sb = new StringBuilder();
				sb.append(priorReason.getCode().toString());
				sb.append(". ");
				sb.append(priorReason.getPriorityIssues());

				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(priorReason.getId()));
				item.setLabel(sb.toString());
				this.listProritaryReasons.add(item);
			}
		} else {
			if (this.getAsse() != null) {
				if (!this.getAsse().equals(SelectItemHelper.getFirst().getValue())) {
					List<PrioritaryReasons> listPriorReasons = new ArrayList<PrioritaryReasons>();

					try {
						listPriorReasons = BeansFactory.PrioritaryReasons().GetByAsse(this.getAsse());
					} catch (PersistenceBeanException e) {
						log.error(e);
						e.printStackTrace();
					}

					for (PrioritaryReasons priorReason : listPriorReasons) {
						StringBuilder sb = new StringBuilder();
						sb.append(priorReason.getCode().toString());
						sb.append(". ");
						sb.append(priorReason.getPriorityIssues());

						SelectItem item = new SelectItem();
						item.setValue(String.valueOf(priorReason.getId()));
						item.setLabel(sb.toString());
						this.listProritaryReasons.add(item);
					}
				}
			}
		}
	}

	private void FillActivationProcedures() throws PersistenceBeanException {
		this.listActivationProcedures = new ArrayList<SelectItem>();
		this.listActivationProcedures.add(SelectItemHelper.getFirst());

		List<ActivationProcedureAnagraph> listActProcs = new ArrayList<ActivationProcedureAnagraph>();

		if (this.getAsse() != null) {
			if (this.getAsse().equals("5")) {
				listActProcs = BeansFactory.ActivationProcedureAnagraph().LoadByUser(true);
			} else {
				listActProcs = BeansFactory.ActivationProcedureAnagraph().LoadByUser(false);
			}
		}

		for (ActivationProcedureAnagraph actProc : listActProcs) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(actProc.getId()));

			item.setLabel(actProc.getCode());

			this.listActivationProcedures.add(item);
		}
	}

	private void FillCPT() throws HibernateException, PersistenceBeanException {
		this.listCPT = new ArrayList<SelectItem>();
		this.listCPT.add(SelectItemHelper.getFirst());

		if (this.getSession().get("projectEdit") != null) {
			try {
				if (this.currentProject.getCpt() != null) {
					this.cpt = String.valueOf(BeansFactory.Projects()
							.Load(String.valueOf(this.getSession().get("projectEdit"))).getCpt().getId());
				} else {
					this.setCpt((String) SelectItemHelper.getFirst().getValue());
				}
			} catch (NumberFormatException e) {
				log.error(e);
				e.printStackTrace();
			} catch (HibernateException e) {
				log.error(e);
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				log.error(e);
				e.printStackTrace();
			}
		} else {
			this.cpt = String.valueOf(SelectItemHelper.getFirst().getValue());
		}

		List<CPT> listCPTCommon = new ArrayList<CPT>();

		try {
			listCPTCommon = BeansFactory.CPT().Load();
		} catch (PersistenceBeanException e) {
			log.error(e);
			e.printStackTrace();
		}

		for (CPT cpt : listCPTCommon) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(cpt.getId()));
			item.setLabel(cpt.getValue());
			this.listCPT.add(item);
		}

	}

	private void FillCUP(boolean clear3level) throws HibernateException, PersistenceBeanException {
		this.listCUP = new ArrayList<SelectItem>();
		this.listCUP.add(SelectItemHelper.getFirst());

		this.listCUPLevel1 = new ArrayList<SelectItem>();
		this.listCUPLevel2 = new ArrayList<SelectItem>();
		this.listCUPLevel1.add(SelectItemHelper.getFirst());
		this.listCUPLevel2.add(SelectItemHelper.getFirst());

		List<CUP> listCUPCommon = new ArrayList<CUP>();
		List<CUP> listCUPLvl1 = new ArrayList<CUP>();
		List<CUP> listCUPLvl2 = new ArrayList<CUP>();

		if (this.getSession().get("projectEdit") != null) {
			try {
				if (this.currentProject.getCup() != null) {
					this.cup = String.valueOf(BeansFactory.Projects()
							.Load(String.valueOf(this.getSession().get("projectEdit"))).getCup().getId());
				} else {
					this.cup = (String) SelectItemHelper.getFirst().getValue();
				}
			} catch (NumberFormatException e) {
				log.error(e);
				e.printStackTrace();
			} catch (HibernateException e) {
				log.error(e);
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				log.error(e);
				e.printStackTrace();
			}
		} else {
			if (this.getCupLevel1() == null || this.getCupLevel1().equals(SelectItemHelper.getFirst().getValue())) {
				this.setCupLevel1(String.valueOf(SelectItemHelper.getFirst().getValue()));
			}

			if (this.getCupLevel2() == null || this.getCupLevel2().equals(SelectItemHelper.getFirst().getValue())) {
				this.setCupLevel2(String.valueOf(SelectItemHelper.getFirst().getValue()));
			}

			if (this.cup == null) {
				this.cup = String.valueOf(SelectItemHelper.getFirst().getValue());
			}
		}

		listCUPLvl1 = BeansFactory.CUP().GetEntitiesByLevel(1);

		if (this.getCupLevel1() != null
				&& !this.getCupLevel1().equals(String.valueOf(SelectItemHelper.getFirst().getValue()))) {
			listCUPLvl2 = BeansFactory.CUP().GetEntitiesByParent(Long.valueOf(this.getCupLevel1()));
			this.setLevel2CupVisibility("");
		} else {
			this.setLevel2CupVisibility("none");
		}

		if (this.getCupLevel2() != null
				&& !this.getCupLevel2().equals(String.valueOf(SelectItemHelper.getFirst().getValue()))) {
			if (!clear3level) {
				listCUPCommon = BeansFactory.CUP().GetEntitiesByParent(Long.valueOf(this.getCupLevel2()));
			}

			this.setLevel3CupVisibility("");
		} else {
			this.setLevel3CupVisibility("none");
		}

		for (CUP cup : listCUPLvl1) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(cup.getId()));
			item.setLabel(cup.getValue());
			this.listCUPLevel1.add(item);
		}

		for (CUP cup : listCUPLvl2) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(cup.getId()));
			item.setLabel(cup.getValue());
			this.listCUPLevel2.add(item);
		}

		for (CUP cup : listCUPCommon) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(cup.getId()));
			item.setLabel(cup.getValue());
			this.listCUP.add(item);
		}
	}

	public void typologyChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (event.getNewValue() != null) {
			this.setProjectTypology(String.valueOf(event.getNewValue()));
			if (String.valueOf(event.getNewValue()).equals("3")) {
				this.setAsse("6");
			}

			List<SelectItem> l = new ArrayList<>();
			l.add(SelectItemHelper.getFirst());
			this.setListSpecificGoals(l);
			this.FillTypologyInfo();

		}
	}

	public void asseChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setAsse(String.valueOf(event.getNewValue()));
		this.FillAsseInfo();
		// List<SelectItem> l = new ArrayList<>();
		// l.add(SelectItemHelper.getFirst());
		// this.setListSpecificGoals(l);
		// this.setListPriorityInvestments(l);
	}

	public void specificGoalChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
//		System.out.println("spec goal change");

		if (event.getNewValue() != null) {
			this.setSpecificGoal(String.valueOf(event.getNewValue()));
			// this.FillThematicGoals();
			// List<SelectItem> l = new ArrayList<>();
			// l.add(SelectItemHelper.getFirst());
			// this.setListPriorityInvestments(l);
		}
	}

	public void thematicGoalChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (event.getNewValue() != null) {
			this.setThematicGoal(String.valueOf(event.getNewValue()));
			// this.FillAsseInfo();
			this.FillPrioritaryGoals();
		}
	}

	public void cupL1Changed(ValueChangeEvent event) throws HibernateException, PersistenceBeanException {
		this.setCupLevel1(String.valueOf(event.getNewValue()));
		this.FillCUP(true);
	}

	public void cupL2Changed(ValueChangeEvent event) throws HibernateException, PersistenceBeanException {
		this.setCupLevel2(String.valueOf(event.getNewValue()));
		this.FillCUP(false);
	}

	/**
	 * classificationTypeCode on change event
	 * 
	 * @param event
	 * @throws NumberFormatException
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 */
	// public void classificationTypeCodeChange(ValueChangeEvent event) {
	// this.setClassificationTypeCode(String.valueOf(event.getNewValue()));
	// //this.fillClassificationSpecifications();
	// this.setClassificationSpecificationCode(String.valueOf(SelectItemHelper.getFirst().getValue()));
	// }

	private void FillTypologyInfo() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.FillAsses();
	}

	private void FillAsseInfo() throws NumberFormatException, HibernateException, PersistenceBeanException {
		// this.FillSpecificGoals();
		// this.FillThematicGoals();
		// this.FillPrioritaryGoals();
		// // this.FillQSNGoals();
		// this.FillPriorThemes();
		// this.FillActivationProcedures();

		this.FillSpecificGoals();
		// this.FillPriorThemes();
		this.FillActivationProcedures();
	}

	/**
	 * Sets code
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets code
	 * 
	 * @return code the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets title
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets title
	 * 
	 * @return title the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets projectTypology
	 * 
	 * @param projectTypology
	 *            the projectTypology to set
	 */
	public void setProjectTypology(String projectTypology) {
		this.getViewState().put("projectStartTypology", projectTypology);
	}

	/**
	 * Gets projectTypology
	 * 
	 * @return projectTypology the projectTypology
	 */
	public String getProjectTypology() {
		if (this.getViewState().get("projectStartTypology") != null) {
			String projectTypology = String.valueOf(this.getViewState().get("projectStartTypology"));
			return projectTypology;
		} else {
			return null;
		}
	}

	/**
	 * Sets asse
	 * 
	 * @param asse
	 *            the asse to set
	 */
	public void setAsse(String asse) {
		this.getViewState().put("projectStartAsse", asse);
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public String getAsse() {
		if (this.getViewState().get("projectStartAsse") != null) {
			String asse = String.valueOf(this.getViewState().get("projectStartAsse"));
			return asse;
		} else {
			return null;
		}
	}

	/**
	 * Sets specificGoal
	 * 
	 * @param specificGoal
	 *            the specificGoal to set
	 */
	public void setSpecificGoal(String specificGoal) {
		// this.specificGoal = specificGoal;
		this.getViewState().put("specificGoal", specificGoal);
	}

	/**
	 * Gets specificGoal
	 * 
	 * @return specificGoal the specificGoal
	 */
	public String getSpecificGoal() {
		// return specificGoal;
		return (String) this.getViewState().get("specificGoal");
	}

	/*
	 * Sets qsnGoal
	 * 
	 * @param qsnGoal the qsnGoal to set
	 */
	/*
	 * public void setQsnGoal(String qsnGoal) { this.qsnGoal = qsnGoal; }
	 */

	/*
	 * Gets qsnGoal
	 * 
	 * @return qsnGoal the qsnGoal
	 */
	/*
	 * public String getQsnGoal() { return qsnGoal; }
	 */

	/**
	 * Sets implementingToolCode.
	 * 
	 * @param implementingToolCode
	 *            the implementingToolCode to set
	 */
	public void setImplementingToolCode(String implementingToolCode) {
		this.implementingToolCode = implementingToolCode;
	}

	/**
	 * Gets implementingToolCode.
	 * 
	 * @return implementingToolCode the implementingToolCode
	 */
	public String getImplementingToolCode() {
		return implementingToolCode;
	}

	/**
	 * Sets macroStrategyCode.
	 * 
	 * @param macroStrategyCode
	 *            the macroStrategyCode to set
	 */
	public void setMacroStrategyCode(String macroStrategyCode) {
		this.macroStrategyCode = macroStrategyCode;
	}

	/**
	 * Gets macroStrategyCode.
	 * 
	 * @return macroStrategyCode the macroStrategyCode
	 */
	public String getMacroStrategyCode() {
		return macroStrategyCode;
	}

	/**
	 * Gets the description of the implementing tool with implementingToolCode.
	 * 
	 * @return implementing tool description
	 */
	public String getImplementingToolDescription() {
		if ((implementingToolCode != null) && !implementingToolCode.isEmpty()) {
			for (ImplementingTool implementingTool : ImplementingTool.values()) {
				if (implementingTool.code.equals(implementingToolCode)) {
					Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
					ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

					return resourceBundle.getString(implementingTool.description);
				}
			}
		}

		return "";
	}

	/**
	 * Gets the description of the macro strategy with macroStrategyCode.
	 * 
	 * @return macro strategy description
	 */
	public String getMacroStrategyDescription() {
		if ((macroStrategyCode != null) && !macroStrategyCode.isEmpty()) {
			for (MacroStrategy macroStrategy : MacroStrategy.values()) {
				if (macroStrategy.code.equals(macroStrategyCode)) {
					Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
					ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

					return resourceBundle.getString(macroStrategy.description);
				}
			}
		}

		return "";
	}

	/**
	 * Sets classificationTypeCode.
	 * 
	 * @param classificationTypeCode
	 *            the classificationTypeCode to set
	 */
	// public void setClassificationTypeCode(String classificationTypeCode) {
	// this.getViewState().put("projectStartClassificationTypeCode",
	// classificationTypeCode);
	// }

	/**
	 * Gets classificationTypeCode.
	 * 
	 * @return classificationTypeCode the classificationTypeCode
	 */
	// public String getClassificationTypeCode() {
	// if (this.getViewState().get("projectStartClassificationTypeCode") !=
	// null) {
	// return
	// String.valueOf(this.getViewState().get("projectStartClassificationTypeCode"));
	// } else {
	// return null;
	// }
	// }

	/**
	 * Gets the description of the classification type with
	 * classificationTypeCode.
	 * 
	 * @return classification type description
	 */
	// public String getClassificationTypeDescription() {
	// if ((this.getClassificationTypeCode() != null) &&
	// !this.getClassificationTypeCode().isEmpty()) {
	// for (ClassificationType classificationType : ClassificationType.values())
	// {
	// if (classificationType.code.equals(this.getClassificationTypeCode())) {
	// Locale currentLocale =
	// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	// ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources",
	// currentLocale);
	//
	// return resourceBundle.getString(classificationType.description);
	// }
	// }
	// }
	//
	// return "";
	// }

	/**
	 * Sets classificationSpecificationCode.
	 * 
	 * @param classificationSpecificationCode
	 *            the classificationSpecificationCode to set
	 */
	public void setClassificationSpecificationCode(String classificationSpecificationCode) {
		this.classificationSpecificationCode = classificationSpecificationCode;
	}

	/**
	 * Gets classificationSpecificationCode.
	 * 
	 * @return classificationSpecificationCode the
	 *         classificationSpecificationCode
	 */
	public String getClassificationSpecificationCode() {
		return classificationSpecificationCode;
	}

	/**
	 * Gets the description of the classification specification with
	 * classificationSpecificationCode.
	 * 
	 * @return classification specification description
	 */
	// public String getClassificationSpecificationDescription() {
	// if (((this.getClassificationSpecificationCode() != null) &&
	// !this.getClassificationSpecificationCode().isEmpty())
	// && ((this.getClassificationTypeCode() != null) &&
	// !this.getClassificationTypeCode().isEmpty())) {
	// Map<String, String> classificationSpecificationMap =
	// ClassificationSpecification.getClassificationSpecification(this.getClassificationTypeCode());
	// for (String key : classificationSpecificationMap.keySet()) {
	// if (key.equals(this.getClassificationSpecificationCode())) {
	// Locale currentLocale =
	// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	// ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources",
	// currentLocale);
	//
	// return resourceBundle.getString(classificationSpecificationMap.get(key));
	// }
	// }
	// }
	//
	// return "";
	// }

	/**
	 * Sets priorReasons
	 * 
	 * @param priorReasons
	 *            the priorReasons to set
	 */
	public void setPriorReasons(String priorReasons) {
		this.priorReasons = priorReasons;
	}

	/**
	 * Gets priorReasons
	 * 
	 * @return priorReasons the priorReasons
	 */
	public String getPriorReasons() {
		return priorReasons;
	}

	/**
	 * Sets activationProcedure
	 * 
	 * @param activationProcedure
	 *            the activationProcedure to set
	 */
	public void setActivationProcedure(String activationProcedure) {
		this.activationProcedure = activationProcedure;
	}

	/**
	 * Gets activationProcedure
	 * 
	 * @return activationProcedure the activationProcedure
	 */
	public String getActivationProcedure() {
		return activationProcedure;
	}

	/**
	 * Sets cup
	 * 
	 * @param cup
	 *            the cup to set
	 */
	public void setCup(String cup) {
		this.cup = cup;
	}

	/**
	 * Gets cup
	 * 
	 * @return cup the cup
	 */
	public String getCup() {
		return cup;
	}

	/**
	 * Sets cpt
	 * 
	 * @param cpt
	 *            the cpt to set
	 */
	public void setCpt(String cpt) {
		this.cpt = cpt;
	}

	/**
	 * Gets cpt
	 * 
	 * @return cpt the cpt
	 */
	public String getCpt() {
		return cpt;
	}

	/**
	 * Sets currentProject
	 * 
	 * @param currentProject
	 *            the currentProject to set
	 */
	public void setCurrentProject(Projects currentProject) {
		this.currentProject = currentProject;
	}

	/**
	 * Gets currentProject
	 * 
	 * @return currentProject the currentProject
	 */
	public Projects getCurrentProject() {
		return currentProject;
	}

	/**
	 * Sets projectStates
	 * 
	 * @param projectStates
	 *            the projectStates to set
	 */
	public void setProjectStates(ProjectStates projectStates) {
		this.projectStates = projectStates;
	}

	/**
	 * Gets projectStates
	 * 
	 * @return projectStates the projectStates
	 */
	public ProjectStates getProjectStates() {
		return projectStates;
	}

	/**
	 * Sets listTypologies
	 * 
	 * @param listTypologies
	 *            the listTypologies to set
	 */
	public void setListTypologies(List<SelectItem> listTypologies) {
		this.listTypologies = listTypologies;
	}

	/**
	 * Gets listTypologies
	 * 
	 * @return listTypologies the listTypologies
	 */
	public List<SelectItem> getListTypologies() {
		return listTypologies;
	}

	/**
	 * Sets listAsses
	 * 
	 * @param listAsses
	 *            the listAsses to set
	 */
	public void setListAsses(List<SelectItem> listAsses) {
		this.listAsses = listAsses;
	}

	/**
	 * Gets listAsses
	 * 
	 * @return listAsses the listAsses
	 */
	public List<SelectItem> getListAsses() {
		return listAsses;
	}

	/**
	 * Sets listSpecificGoals
	 * 
	 * @param listSpecificGoals
	 *            the listSpecificGoals to set
	 */
	public void setListSpecificGoals(List<SelectItem> listSpecificGoals) {
		this.listSpecificGoals = listSpecificGoals;
	}

	/**
	 * Gets listSpecificGoals
	 * 
	 * @return listSpecificGoals the listSpecificGoals
	 */
	public List<SelectItem> getListSpecificGoals() {
		return listSpecificGoals;
	}

	/*
	 * Sets listQSNGoals
	 * 
	 * @param listQSNGoals the listQSNGoals to set
	 */
	/*
	 * public void setListQSNGoals(List<SelectItem> listQSNGoals) {
	 * this.listQSNGoals = listQSNGoals; }
	 */

	/*
	 * Gets listQSNGoals
	 * 
	 * @return listQSNGoals the listQSNGoals
	 */
	/*
	 * public List<SelectItem> getListQSNGoals() { return listQSNGoals; }
	 */

	/**
	 * Sets listImplementingTool.
	 * 
	 * @param listImplementingTool
	 *            the listImplementingTool to set
	 */
	public void setListImplementingTool(List<SelectItem> listImplementingTool) {
		this.listImplementingTool = listImplementingTool;
	}

	/**
	 * Gets listImplementingTool.
	 * 
	 * @return listImplementingTool the listImplementingTool
	 */
	public List<SelectItem> getListImplementingTool() {
		return listImplementingTool;
	}

	/**
	 * Sets listMacroStrategy.
	 * 
	 * @param listMacroStrategy
	 *            the listMacroStrategy to set
	 */
	public void setListMacroStrategy(List<SelectItem> listMacroStrategy) {
		this.listMacroStrategy = listMacroStrategy;
	}

	/**
	 * Gets listMacroStrategy.
	 * 
	 * @return listMacroStrategy the listMacroStrategy
	 */
	public List<SelectItem> getListMacroStrategy() {
		return listMacroStrategy;
	}

	/**
	 * Sets listClassificationType.
	 * 
	 * @param listClassificationType
	 *            the listClassificationType to set
	 */
	public void setListClassificationTypeCodes(List<SelectItem> listClassificationType) {
		this.listClassificationTypeCodes = listClassificationType;
	}

	/**
	 * Gets listClassificationType.
	 * 
	 * @return listClassificationType the listClassificationType
	 */
	public List<SelectItem> getListClassificationTypeCodes() {
		return listClassificationTypeCodes;
	}

	/**
	 * Sets listClassificationSpecification.
	 * 
	 * @param listClassificationTypeCodes
	 *            the listClassificationType to set
	 */
	public void setListClassificationSpecificationCodes(List<SelectItem> listClassificationSpecification) {
		this.listClassificationSpecificationCodes = listClassificationSpecification;
	}

	/**
	 * Gets listClassificationSpecification.
	 * 
	 * @return listClassificationSpecification the
	 *         listClassificationSpecification
	 */
	public List<SelectItem> getListClassificationSpecificationCodes() {
		return listClassificationSpecificationCodes;
	}

	/**
	 * Sets listProritaryReasons
	 * 
	 * @param listProritaryReasons
	 *            the listProritaryReasons to set
	 */
	public void setListProritaryReasons(List<SelectItem> listProritaryReasons) {
		this.listProritaryReasons = listProritaryReasons;
	}

	/**
	 * Gets listProritaryReasons
	 * 
	 * @return listProritaryReasons the listProritaryReasons
	 */
	public List<SelectItem> getListProritaryReasons() {
		return listProritaryReasons;
	}

	/**
	 * Sets listActivationProcedures
	 * 
	 * @param listActivationProcedures
	 *            the listActivationProcedures to set
	 */
	public void setListActivationProcedures(List<SelectItem> listActivationProcedures) {
		this.listActivationProcedures = listActivationProcedures;
	}

	/**
	 * Gets listActivationProcedures
	 * 
	 * @return listActivationProcedures the listActivationProcedures
	 */
	public List<SelectItem> getListActivationProcedures() {
		return listActivationProcedures;
	}

	/**
	 * Sets listCPT
	 * 
	 * @param listCPT
	 *            the listCPT to set
	 */
	public void setListCPT(List<SelectItem> listCPT) {
		this.listCPT = listCPT;
	}

	/**
	 * Gets listCPT
	 * 
	 * @return listCPT the listCPT
	 */
	public List<SelectItem> getListCPT() {
		return listCPT;
	}

	/**
	 * Gets listCUP
	 * 
	 * @return listCUP the listCUP
	 */
	public List<SelectItem> getListCUP() {
		return listCUP;
	}

	/**
	 * Sets listCUP
	 * 
	 * @param listCUP
	 *            the listCUP to set
	 */
	public void setListCUP(List<SelectItem> listCUP) {
		this.listCUP = listCUP;
	}

	/**
	 * Sets hasRights
	 * 
	 * @param hasRights
	 *            the hasRights to set
	 */
	public void setHasRights(boolean hasRights) {
		this.hasRights = hasRights;
	}

	/**
	 * Gets hasRights
	 * 
	 * @return hasRights the hasRights
	 */
	public boolean isHasRights() {
		return hasRights;
	}

	/**
	 * Sets level2CupVisibility
	 * 
	 * @param level2CupVisibility
	 *            the level2CupVisibility to set
	 */
	public void setLevel2CupVisibility(String level2CupVisibility) {
		this.level2CupVisibility = level2CupVisibility;
	}

	/**
	 * Gets level2CupVisibility
	 * 
	 * @return level2CupVisibility the level2CupVisibility
	 */
	public String getLevel2CupVisibility() {
		return level2CupVisibility;
	}

	/**
	 * Sets level3CupVisibility
	 * 
	 * @param level3CupVisibility
	 *            the level3CupVisibility to set
	 */
	public void setLevel3CupVisibility(String level3CupVisibility) {
		this.level3CupVisibility = level3CupVisibility;
	}

	/**
	 * Gets level3CupVisibility
	 * 
	 * @return level3CupVisibility the level3CupVisibility
	 */
	public String getLevel3CupVisibility() {
		return level3CupVisibility;
	}

	/**
	 * Sets cupLevel1
	 * 
	 * @param cupLevel1
	 *            the cupLevel1 to set
	 */
	public void setCupLevel1(String cupLevel1) {
		this.getViewState().put("projectStartCup1", cupLevel1);
	}

	/**
	 * Gets cupLevel1
	 * 
	 * @return cupLevel1 the cupLevel1
	 */
	public String getCupLevel1() {
		if (this.getViewState().get("projectStartCup1") != null) {
			return String.valueOf(this.getViewState().get("projectStartCup1"));
		} else {
			return null;
		}
	}

	/**
	 * Sets cupLevel2
	 * 
	 * @param cupLevel2
	 *            the cupLevel2 to set
	 */
	public void setCupLevel2(String cupLevel2) {
		this.getViewState().put("projectStartCup2", cupLevel2);
	}

	/**
	 * Gets cupLevel2
	 * 
	 * @return cupLevel2 the cupLevel2
	 */
	public String getCupLevel2() {
		if (this.getViewState().get("projectStartCup2") != null) {
			return String.valueOf(this.getViewState().get("projectStartCup2"));
		} else {
			return null;
		}
	}

	/**
	 * Sets listCUPLevel1
	 * 
	 * @param listCUPLevel1
	 *            the listCUPLevel1 to set
	 */
	public void setListCUPLevel1(List<SelectItem> listCUPLevel1) {
		this.listCUPLevel1 = listCUPLevel1;
	}

	/**
	 * Gets listCUPLevel1
	 * 
	 * @return listCUPLevel1 the listCUPLevel1
	 */
	public List<SelectItem> getListCUPLevel1() {
		return listCUPLevel1;
	}

	/**
	 * Sets listCUPLevel2
	 * 
	 * @param listCUPLevel2
	 *            the listCUPLevel2 to set
	 */
	public void setListCUPLevel2(List<SelectItem> listCUPLevel2) {
		this.listCUPLevel2 = listCUPLevel2;
	}

	/**
	 * Gets listCUPLevel2
	 * 
	 * @return listCUPLevel2 the listCUPLevel2
	 */
	public List<SelectItem> getListCUPLevel2() {
		return listCUPLevel2;
	}

	/**
	 * Sets cup1Changed
	 * 
	 * @param cup1Changed
	 *            the cup1Changed to set
	 */
	public void setCup1Changed(boolean cup1Changed) {
		this.cup1Changed = cup1Changed;
	}

	/**
	 * Gets cup1Changed
	 * 
	 * @return cup1Changed the cup1Changed
	 */
	public boolean isCup1Changed() {
		return cup1Changed;
	}

	/**
	 * Sets cup2Changed
	 * 
	 * @param cup2Changed
	 *            the cup2Changed to set
	 */
	public void setCup2Changed(boolean cup2Changed) {
		this.cup2Changed = cup2Changed;
	}

	/**
	 * Gets cup2Changed
	 * 
	 * @return cup2Changed the cup2Changed
	 */
	public boolean isCup2Changed() {
		return cup2Changed;
	}

	/**
	 * Sets editMode
	 * 
	 * @param editMode
	 *            the editMode to set
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * Gets editMode
	 * 
	 * @return editMode the editMode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * Sets canEdit
	 * 
	 * @param canEdit
	 *            the canEdit to set
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 * Gets canEdit
	 * 
	 * @return canEdit the canEdit
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionEng() {
		return descriptionEng;
	}

	public void setDescriptionEng(String descriptionEng) {
		this.descriptionEng = descriptionEng;
	}

	public String getExpectedResults() {
		return expectedResults;
	}

	public void setExpectedResults(String expectedResults) {
		this.expectedResults = expectedResults;
	}

	/**
	 * @return the thematicGoal
	 */
	public String getThematicGoal() {
		// return thematicGoal;
		return (String) this.getViewState().get("thematicGoal");
	}

	/**
	 * @param thematicGoal
	 *            the thematicGoal to set
	 */
	public void setThematicGoal(String thematicGoal) {
		// this.thematicGoal = thematicGoal;
		this.getViewState().put("thematicGoal", thematicGoal);
	}

	/**
	 * @return the listThematicGoals
	 */
	public List<SelectItem> getListThematicGoals() {
		return listThematicGoals;
	}

	/**
	 * @param listThematicGoals
	 *            the listThematicGoals to set
	 */
	public void setListThematicGoals(List<SelectItem> listThematicGoals) {
		this.listThematicGoals = listThematicGoals;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		// return organization;
		return (String) this.getSession().get("organization");
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(String organization) {
		// this.organization = organization;
		// this.getSession().put("organization", "Autorit\u00E0 di Gestione");
		this.getSession().put("organization", Utils.getString("projectMA"));
	}

	/**
	 * @return the sustainDate
	 */
	public Date getSustainDate() {
		return sustainDate;
	}

	/**
	 * @param sustainDate
	 *            the sustainDate to set
	 */
	public void setSustainDate(Date sustainDate) {
		this.sustainDate = sustainDate;
	}

	/**
	 * @return the priorityInvestment
	 */
	public String getPriorityInvestment() {
		return priorityInvestment;
	}

	/**
	 * @param priorityInvestment
	 *            the priorityInvestment to set
	 */
	public void setPriorityInvestment(String priorityInvestment) {
		this.priorityInvestment = priorityInvestment;
	}

	/**
	 * @return the listPriorityInvestments
	 */
	public List<SelectItem> getListPriorityInvestments() {
		return listPriorityInvestments;
	}

	/**
	 * @param listPriorityInvestments
	 *            the listPriorityInvestments to set
	 */
	public void setListPriorityInvestments(List<SelectItem> listPriorityInvestments) {
		this.listPriorityInvestments = listPriorityInvestments;
	}
}
