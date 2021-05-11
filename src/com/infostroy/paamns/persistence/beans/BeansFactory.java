/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrCFInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.Addresses;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.ApplicationSettings;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CEPaymentRequest;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DocumentsAdm;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclaration;
import com.infostroy.paamns.persistence.beans.entities.domain.FESRInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalCheckToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalChecks;
import com.infostroy.paamns.persistence.beans.entities.domain.Localizations;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.NonRegularySituations;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipology;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStep;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStepInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjectDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjects;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.Responsibilities;
import com.infostroy.paamns.persistence.beans.entities.domain.RotationFoundInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.SignedDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.UsersLogins;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActivationProcedureTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Ateco;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CPT;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CUP;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Cities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ControllerTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Departments;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DocumentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DurStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.EconomicActivities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.FinancingForms;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Naming;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentWays;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProgressValidationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Provinces;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonsForDelay;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibilityTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibleUsers;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Sections;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Typologies;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;
import com.infostroy.paamns.persistence.beans.entities.domain.localization.LocalValue;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.AnagraphicalData;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.EmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.FinancialData;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.FinancingPlan;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.IdentificationData;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Legend;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PhisicalClassificationPriorityThema;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PhisicalClassificationQSNGoal;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramImplementation;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.ActivationProcedureAnagraphBean;
import com.infostroy.paamns.persistence.beans.facades.ActivationProcedureInfoBean;
import com.infostroy.paamns.persistence.beans.facades.AdditionalFesrCFInfoBean;
import com.infostroy.paamns.persistence.beans.facades.AdditionalFesrInfoBean;
import com.infostroy.paamns.persistence.beans.facades.AddressesBean;
import com.infostroy.paamns.persistence.beans.facades.AdvancesIncludedInPaymentApplicationBean;
import com.infostroy.paamns.persistence.beans.facades.AmountsToBeRecoveredAtTheCloseBean;
import com.infostroy.paamns.persistence.beans.facades.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriodBean;
import com.infostroy.paamns.persistence.beans.facades.AnnualProfilesBean;
import com.infostroy.paamns.persistence.beans.facades.ApplicationSettingsBean;
import com.infostroy.paamns.persistence.beans.facades.AssignmentProceduresBean;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.persistence.beans.facades.CFPartnerAnagraphCompletationsBean;
import com.infostroy.paamns.persistence.beans.facades.CFPartnerAnagraphProjectBean;
import com.infostroy.paamns.persistence.beans.facades.CFPartnerAnagraphsBean;
import com.infostroy.paamns.persistence.beans.facades.CategoryBean;
import com.infostroy.paamns.persistence.beans.facades.ClosingOfAccountsBean;
import com.infostroy.paamns.persistence.beans.facades.ControllerUserAnagraphBean;
import com.infostroy.paamns.persistence.beans.facades.CostAsse3Bean;
import com.infostroy.paamns.persistence.beans.facades.CostDefinitionsBean;
import com.infostroy.paamns.persistence.beans.facades.CostDefinitionsToDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.CostDefinitionsToNotesBean;
import com.infostroy.paamns.persistence.beans.facades.CostItemAsse3Bean;
import com.infostroy.paamns.persistence.beans.facades.CostManagementBean;
import com.infostroy.paamns.persistence.beans.facades.DocumentsAdmBean;
import com.infostroy.paamns.persistence.beans.facades.DocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.DurCompilationsBean;
import com.infostroy.paamns.persistence.beans.facades.DurInfoToResponsiblePeopleBean;
import com.infostroy.paamns.persistence.beans.facades.DurInfosBean;
import com.infostroy.paamns.persistence.beans.facades.DurReimbursementsBean;
import com.infostroy.paamns.persistence.beans.facades.DurSummariesBean;
import com.infostroy.paamns.persistence.beans.facades.ExpenditureDeclarationAccountingPeriodBean;
import com.infostroy.paamns.persistence.beans.facades.ExpenditureDeclarationAnnex1dWrapperBean;
import com.infostroy.paamns.persistence.beans.facades.ExpenditureDeclarationBean;
import com.infostroy.paamns.persistence.beans.facades.ExpenditureDeclarationDurCompilationsBean;
import com.infostroy.paamns.persistence.beans.facades.ExpendituresOfPaymentApplicationBean;
import com.infostroy.paamns.persistence.beans.facades.ExpirationDateBean;
import com.infostroy.paamns.persistence.beans.facades.FESRInfoBean;
import com.infostroy.paamns.persistence.beans.facades.GeneralBudgetsBean;
import com.infostroy.paamns.persistence.beans.facades.GiuridicalEngagesBean;
import com.infostroy.paamns.persistence.beans.facades.LocalCheckToDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.LocalChecksBean;
import com.infostroy.paamns.persistence.beans.facades.LocalizationsBean;
import com.infostroy.paamns.persistence.beans.facades.MailSettingsBean;
import com.infostroy.paamns.persistence.beans.facades.MailsBean;
import com.infostroy.paamns.persistence.beans.facades.NonRegularySituationsBean;
import com.infostroy.paamns.persistence.beans.facades.PartnersBudgetsBean;
import com.infostroy.paamns.persistence.beans.facades.PaymentApplicationBean;
import com.infostroy.paamns.persistence.beans.facades.PaymentApplicationDurCompilationsBean;
import com.infostroy.paamns.persistence.beans.facades.PaymentRequestBean;
import com.infostroy.paamns.persistence.beans.facades.PaymentRequestItemBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToCommunicationIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToCoreIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToEmploymentIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToProgIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToProgramRealizationIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToProgramResultIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToQsnIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationsBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalProgressNotesBean;
import com.infostroy.paamns.persistence.beans.facades.PhisicalProgressToDocumentBean;
import com.infostroy.paamns.persistence.beans.facades.PotentialProjectsBean;
import com.infostroy.paamns.persistence.beans.facades.PotentialProjectsToDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.ProcedureNotesBean;
import com.infostroy.paamns.persistence.beans.facades.ProcedureTipologyBean;
import com.infostroy.paamns.persistence.beans.facades.ProcedureTipologyStepBean;
import com.infostroy.paamns.persistence.beans.facades.ProcedureTipologyStepInfosBean;
import com.infostroy.paamns.persistence.beans.facades.ProceduresBean;
import com.infostroy.paamns.persistence.beans.facades.ProgressValidationObjectDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.ProgressValidationObjectsBean;
import com.infostroy.paamns.persistence.beans.facades.ProjectIformationCompletationsBean;
import com.infostroy.paamns.persistence.beans.facades.ProjectIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.ProjectStatesChangesBean;
import com.infostroy.paamns.persistence.beans.facades.ProjectsBean;
import com.infostroy.paamns.persistence.beans.facades.RecoveriesBean;
import com.infostroy.paamns.persistence.beans.facades.RegistredAmountBean;
import com.infostroy.paamns.persistence.beans.facades.ReportingsBean;
import com.infostroy.paamns.persistence.beans.facades.ResponsibilitiesBean;
import com.infostroy.paamns.persistence.beans.facades.RotationFoundInfoBean;
import com.infostroy.paamns.persistence.beans.facades.SignedDocumentsBean;
import com.infostroy.paamns.persistence.beans.facades.SuperAdminChangesBean;
import com.infostroy.paamns.persistence.beans.facades.UnrecoverableAmountsAtTheCloseBean;
import com.infostroy.paamns.persistence.beans.facades.UserActivityLogBean;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;
import com.infostroy.paamns.persistence.beans.facades.UsersLoginsBean;
import com.infostroy.paamns.persistence.beans.facades.VersionBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ActTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ActivationProcedureTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.AtecoBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CFPartnerAnagraphTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CPTBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CUPBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CitiesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ControllerTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CostDefinitionPhaseBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CostItemsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CostStatesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CountriesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.CurrenciesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.DepartmentsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.DocumentTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.DurStatesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.EconomicActivitiesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ExpenditureDeclarationStatesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.FinancingFormsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.NamingBean;
import com.infostroy.paamns.persistence.beans.facades.enums.NotRegularTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.PaymentTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.PaymentWaysBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ProgressValidationStatesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ProjectStatesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ProvincesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ReasonForAbsenceCIGBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ReasonsForDelayBean;
import com.infostroy.paamns.persistence.beans.facades.enums.RegionsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.RequestTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ResponsibilityTypesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ResponsibleUsersBean;
import com.infostroy.paamns.persistence.beans.facades.enums.SectionsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.SpecificGoalsBean;
import com.infostroy.paamns.persistence.beans.facades.enums.TypologiesBean;
import com.infostroy.paamns.persistence.beans.facades.enums.ZonesBean;
import com.infostroy.paamns.persistence.beans.facades.localization.LocalValueBean;
import com.infostroy.paamns.persistence.beans.facades.statics.AnagraphicalDataBean;
import com.infostroy.paamns.persistence.beans.facades.statics.CoreIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.EmploymentIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.FinancialDataBean;
import com.infostroy.paamns.persistence.beans.facades.statics.FinancingPlanBean;
import com.infostroy.paamns.persistence.beans.facades.statics.HierarchicalLevelBean;
import com.infostroy.paamns.persistence.beans.facades.statics.IdentificationDataBean;
import com.infostroy.paamns.persistence.beans.facades.statics.LegendBean;
import com.infostroy.paamns.persistence.beans.facades.statics.PhisicalClassificationPriorityThemaBean;
import com.infostroy.paamns.persistence.beans.facades.statics.PhisicalClassificationQSNGoalBean;
import com.infostroy.paamns.persistence.beans.facades.statics.PrioritaryReasonsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.ProgramImplementationBean;
import com.infostroy.paamns.persistence.beans.facades.statics.ProgramIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.ProgramRealizationIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.ProgramResultIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.QSNGoalBean;
import com.infostroy.paamns.persistence.beans.facades.statics.QsnIndicatorsBean;
import com.infostroy.paamns.persistence.beans.facades.statics.RolesBean;
import com.infostroy.paamns.web.beans.programms.CEPaymentRequestBean;

/**
 * 
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2009, 2010.
 * 
 */
public class BeansFactory {

	public static AmountsToBeRecoveredAtTheCloseBean AmountsToBeRecoveredAtTheClose() {
		return new AmountsToBeRecoveredAtTheCloseBean();
	}

	public static AmountsWithdrawnAndRecoveredDuringTheAccountingPeriodBean AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod() {
		return new AmountsWithdrawnAndRecoveredDuringTheAccountingPeriodBean();
	}

	public static UnrecoverableAmountsAtTheCloseBean UnrecoverableAmountsAtTheClose() {
		return new UnrecoverableAmountsAtTheCloseBean();
	}

	public static RegistredAmountBean RegistredAmount() {
		return new RegistredAmountBean();
	}

	public static PaymentApplicationBean PaymentApplications() {
		return new PaymentApplicationBean();
	}

	public static ClosingOfAccountsBean ClosingOfAccounts() {
		return new ClosingOfAccountsBean();
	}

	public static ExpenditureDeclarationBean ExpenditureDeclarations() {
		return new ExpenditureDeclarationBean();
	}

	public static ExpenditureDeclarationAccountingPeriodBean ExpenditureDeclarationAccountingPeriod() {
		return new ExpenditureDeclarationAccountingPeriodBean();
	}

	public static AdvancesIncludedInPaymentApplicationBean AdvancesIncludedInPaymentApplications() {
		return new AdvancesIncludedInPaymentApplicationBean();
	}
	
	public static ExpendituresOfPaymentApplicationBean ExpendituresOfPaymentApplication() {
		return new ExpendituresOfPaymentApplicationBean();
	}

	public static RequestTypesBean RequestTypes() {
		return new RequestTypesBean();
	}

	public static ExpenditureDeclarationStatesBean ExpenditureDeclarationStates() {
		return new ExpenditureDeclarationStatesBean();
	}
	
	public static ExpenditureDeclarationAnnex1dWrapperBean ExpenditureDeclarationAnnex1dWrapper() {
		return new ExpenditureDeclarationAnnex1dWrapperBean();
	}

	public static VersionBean Version() {
		return new VersionBean();
	}

	public static CostItemAsse3Bean CostItemAsse3() {
		return new CostItemAsse3Bean();
	}

	public static CostAsse3Bean CostAsse3() {
		return new CostAsse3Bean();
	}

	public static FESRInfoBean FESRInfo() {
		return new FESRInfoBean();
	}

	public static AdditionalFesrInfoBean AdditionalFESRInfo() {
		return new AdditionalFesrInfoBean();
	}

	public static AdditionalFesrCFInfoBean AdditionalFESRCFInfo() {
		return new AdditionalFesrCFInfoBean();
	}

	public static ReportingsBean Reportings() {
		return new ReportingsBean();
	}

	public static LegendBean Legend() {
		return new LegendBean();
	}

	public static FinancingPlanBean FinancingPlan() {
		return new FinancingPlanBean();
	}

	public static RotationFoundInfoBean RotationFoundInfo() {
		return new RotationFoundInfoBean();
	}

	public static ActivationProcedureAnagraphBean ActivationProcedureAnagraph() {
		return new ActivationProcedureAnagraphBean();
	}

	public static ActivationProcedureInfoBean ActivationProcedureInfo() {
		return new ActivationProcedureInfoBean();
	}

	public static AddressesBean Addresses() {
		return new AddressesBean();
	}

	public static ApplicationSettingsBean ApplicationSettings() {
		return new ApplicationSettingsBean();
	}

	public static AnnualProfilesBean AnnualProfiles() {
		return new AnnualProfilesBean();
	}

	public static AssignmentProceduresBean AssignmentProcedures() {
		return new AssignmentProceduresBean();
	}

	public static BudgetInputSourceDividedBean BudgetInputSourceDivided() {
		return new BudgetInputSourceDividedBean();
	}

	public static CategoryBean Category() {
		return new CategoryBean();
	}

	public static CFPartnerAnagraphCompletationsBean CFPartnerAnagraphCompletations() {
		return new CFPartnerAnagraphCompletationsBean();
	}

	public static CFPartnerAnagraphsBean CFPartnerAnagraphs() {
		return new CFPartnerAnagraphsBean();
	}

	public static CFPartnerAnagraphProjectBean CFPartnerAnagraphProject() {
		return new CFPartnerAnagraphProjectBean();
	}

	public static ControllerUserAnagraphBean ControllerUserAnagraph() {
		return new ControllerUserAnagraphBean();
	}
	
	public static CEPaymentRequestBean ControllerCePaymentRequest() {
		return new CEPaymentRequestBean();
	}

	public static ProgramImplementationBean ProgramImplementation() {
		return new ProgramImplementationBean();
	}

	public static CostDefinitionsBean CostDefinitions() {
		return new CostDefinitionsBean();
	}

	public static CostManagementBean CostManagement() {
		return new CostManagementBean();
	}

	public static DocumentsBean Documents() {
		return new DocumentsBean();
	}

	public static SignedDocumentsBean SignedDocuments() {  // new
		return new SignedDocumentsBean();
	}	
	
	public static DocumentsAdmBean DocumentsAdm() {
		return new DocumentsAdmBean();
	}

	public static DurCompilationsBean DurCompilations() {
		return new DurCompilationsBean();
	}

	public static ExpenditureDeclarationDurCompilationsBean ExpenditureDeclarationDurCompilations() {
		return new ExpenditureDeclarationDurCompilationsBean();
	}

	public static PaymentApplicationDurCompilationsBean PaymentApplicationDurCompilations() {
		return new PaymentApplicationDurCompilationsBean();
	}

	public static DurInfosBean DurInfos() {
		return new DurInfosBean();
	}

	public static DurReimbursementsBean DurReimbursements() {
		return new DurReimbursementsBean();
	}

	public static DurSummariesBean DurSummaries() {
		return new DurSummariesBean();
	}

	public static GeneralBudgetsBean GeneralBudgets() {
		return new GeneralBudgetsBean();
	}

	public static GiuridicalEngagesBean GiuridicalEngages() {
		return new GiuridicalEngagesBean();
	}

	public static LocalChecksBean LocalChecks() {
		return new LocalChecksBean();
	}

	public static LocalizationsBean Localizations() {
		return new LocalizationsBean();
	}

	public static MailsBean Mails() {
		return new MailsBean();
	}

	public static MailSettingsBean MailSettings() {
		return new MailSettingsBean();
	}

	public static NonRegularySituationsBean NonRegularySituations() {
		return new NonRegularySituationsBean();
	}

	public static PartnersBudgetsBean PartnersBudgets() {
		return new PartnersBudgetsBean();
	}

	public static PartnersBudgetsPhasesBean PartnersBudgetsPhases() {
		return new PartnersBudgetsPhasesBean();
	}

	public static PhisicalInitializationsBean PhisicalInizializations() {
		return new PhisicalInitializationsBean();
	}

	public static PhisicalInitializationToCoreIndicatorsBean PhisicalInizializationToCoreIndicators() {
		return new PhisicalInitializationToCoreIndicatorsBean();
	}

	public static PhisicalInitializationToEmploymentIndicatorsBean PhisicalInizializationToEmploymentIndicators() {
		return new PhisicalInitializationToEmploymentIndicatorsBean();
	}

	public static PhisicalInitializationToProgramRealizationIndicatorsBean PhisicalInizializationToProgramRealizationIndicators() {
		return new PhisicalInitializationToProgramRealizationIndicatorsBean();
	}

	public static PhisicalInitializationToProgramResultIndicatorsBean PhisicalInizializationToProgramResultIndicators() {
		return new PhisicalInitializationToProgramResultIndicatorsBean();
	}

	public static PhisicalInitializationToCommunicationIndicatorsBean PhisicalInizializationToCommunicationIndicators() {
		return new PhisicalInitializationToCommunicationIndicatorsBean();
	}

	public static PhisicalInitializationToQsnIndicatorsBean PhisicalInizializationToQsnIndicators() {
		return new PhisicalInitializationToQsnIndicatorsBean();
	}

	public static ProcedureNotesBean ProcedureNotes() {
		return new ProcedureNotesBean();
	}

	public static PhisicalProgressNotesBean PhisicalProgressNotes() {
		return new PhisicalProgressNotesBean();
	}

	public static ProceduresBean Procedures() {
		return new ProceduresBean();
	}

	public static ProcedureTipologyBean ProcedureTipology() {
		return new ProcedureTipologyBean();
	}

	public static ProcedureTipologyStepBean ProcedureTipologyStep() {
		return new ProcedureTipologyStepBean();
	}

	public static ProcedureTipologyStepInfosBean ProcedureTipologyStepInfos() {
		return new ProcedureTipologyStepInfosBean();
	}

	public static ProgressValidationObjectDocumentsBean ProgressValidationObjectDocuments() {
		return new ProgressValidationObjectDocumentsBean();
	}

	public static ProgressValidationObjectsBean ProgressValidationObjects() {
		return new ProgressValidationObjectsBean();
	}

	public static ProjectIformationCompletationsBean ProjectIformationCompletations() {
		return new ProjectIformationCompletationsBean();
	}

	public static ProjectsBean Projects() {
		return new ProjectsBean();
	}

	public static ProjectStatesChangesBean ProjectStatesChanges() {
		return new ProjectStatesChangesBean();
	}

	public static ResponsibilitiesBean Responsibilities() {
		return new ResponsibilitiesBean();
	}

	public static RecoveriesBean Recoveries() {
		return new RecoveriesBean();
	}

	public static SuperAdminChangesBean SuperAdminChanges() {
		return new SuperAdminChangesBean();
	}

	public static UserRolesBean UserRoles() {
		return new UserRolesBean();
	}

	public static UsersBean Users() {
		return new UsersBean();
	}

	public static ExpirationDateBean ExpirationDate() {
		return new ExpirationDateBean();
	}

	public static UsersLoginsBean UsersLogins() {
		return new UsersLoginsBean();
	}

	public static ActTypesBean ActTypes() {
		return new ActTypesBean();
	}

	public static CFPartnerAnagraphTypesBean CFPartnerAnagraphTypes() {
		return new CFPartnerAnagraphTypesBean();
	}

	public static ControllerTypesBean ControllerTypes() {
		return new ControllerTypesBean();
	}

	public static ActivationProcedureTypesBean ActivationProcedureTypes() {
		return new ActivationProcedureTypesBean();
	}

	public static CitiesBean Cities() {
		return new CitiesBean();
	}

	public static CostItemsBean CostItems() {
		return new CostItemsBean();
	}

	public static CostStatesBean CostStates() {
		return new CostStatesBean();
	}

	public static CountriesBean Countries() {
		return new CountriesBean();
	}

	public static DepartmentsBean Departments() {
		return new DepartmentsBean();
	}

	public static DocumentTypesBean DocumentTypes() {
		return new DocumentTypesBean();
	}
	
	public static CurrenciesBean Currencies() {
		return new CurrenciesBean();
	}	

	public static DurStatesBean DurStates() {
		return new DurStatesBean();
	}

	public static EconomicActivitiesBean EconomicActivities() {
		return new EconomicActivitiesBean();
	}

	public static NotRegularTypesBean NotRegularTypes() {
		return new NotRegularTypesBean();
	}

	public static PaymentTypesBean PaymentTypes() {
		return new PaymentTypesBean();
	}

	public static PaymentWaysBean PaymentWays() {
		return new PaymentWaysBean();
	}

	public static ProgressValidationStatesBean ProgressValidationStates() {
		return new ProgressValidationStatesBean();
	}

	public static ProjectStatesBean ProjectStates() {
		return new ProjectStatesBean();
	}

	public static ProvincesBean Provinces() {
		return new ProvincesBean();
	}

	public static ProjectIndicatorsBean ProjectIndicators() {
		return new ProjectIndicatorsBean();
	}

	public static ReasonsForDelayBean ReasonsForDelay() {
		return new ReasonsForDelayBean();
	}

	public static ReasonForAbsenceCIGBean ReasonForAbsenceCIG() {
		return new ReasonForAbsenceCIGBean();
	}

	public static RegionsBean Regions() {
		return new RegionsBean();
	}

	public static ResponsibilityTypesBean ResponsibilityTypes() {
		return new ResponsibilityTypesBean();
	}

	public static ResponsibleUsersBean ResponsibleUsers() {
		return new ResponsibleUsersBean();
	}

	public static RolesBean Roles() {
		return new RolesBean();
	}

	public static HierarchicalLevelBean HierarchicalLevel() {
		return new HierarchicalLevelBean();
	}

	public static SectionsBean Sections() {
		return new SectionsBean();
	}

	public static SpecificGoalsBean SpecificGoals() {
		return new SpecificGoalsBean();
	}

	public static TypologiesBean Typologies() {
		return new TypologiesBean();
	}

	public static ZonesBean Zones() {
		return new ZonesBean();
	}

	public static AnagraphicalDataBean AnagraphicalData() {
		return new AnagraphicalDataBean();
	}

	public static CoreIndicatorsBean CoreIndicators() {
		return new CoreIndicatorsBean();
	}

	public static EmploymentIndicatorsBean EmploymentIndicators() {
		return new EmploymentIndicatorsBean();
	}

	public static FinancialDataBean FinancialData() {
		return new FinancialDataBean();
	}

	public static IdentificationDataBean IdentificationData() {
		return new IdentificationDataBean();
	}

	public static PhisicalClassificationPriorityThemaBean PhisicalClassificationPriorityThema() {
		return new PhisicalClassificationPriorityThemaBean();
	}

	public static PhisicalClassificationQSNGoalBean PhisicalClassificationQSNGoal() {
		return new PhisicalClassificationQSNGoalBean();
	}

	public static PrioritaryReasonsBean PrioritaryReasons() {
		return new PrioritaryReasonsBean();
	}

	public static ProgramRealizationIndicatorsBean ProgramRealizationIndicators() {
		return new ProgramRealizationIndicatorsBean();
	}

	public static ProgramIndicatorsBean<ProgramIndicators> ProgramIndicators() {
		return new ProgramIndicatorsBean<ProgramIndicators>();
	}

	public static ProgramResultIndicatorsBean ProgramResultIndicators() {
		return new ProgramResultIndicatorsBean();
	}

	public static QSNGoalBean QSNGoal() {
		return new QSNGoalBean();
	}

	public static QsnIndicatorsBean QsnIndicators() {
		return new QsnIndicatorsBean();
	}

	public static CPTBean CPT() {
		return new CPTBean();
	}

	public static CUPBean CUP() {
		return new CUPBean();
	}

	public static LocalValueBean LocalValueBean() {
		return new LocalValueBean();
	}

	public static NamingBean Naming() {
		return new NamingBean();
	}

	public static AtecoBean Ateco() {
		return new AtecoBean();
	}

	public static CostDefinitionsToDocumentsBean CostDefinitionsToDocuments() {
		return new CostDefinitionsToDocumentsBean();
	}

	public static CostDefinitionsToNotesBean CostDefinitionsToNotes() {
		return new CostDefinitionsToNotesBean();
	}

	public static LocalCheckToDocumentsBean LocalCheckToDocuments() {
		return new LocalCheckToDocumentsBean();
	}

	public static FinancingFormsBean FinancingForms() {
		return new FinancingFormsBean();
	}

	public static DurInfoToResponsiblePeopleBean DurInfoToResponsiblePeople() {
		return new DurInfoToResponsiblePeopleBean();
	}

	public static CostDefinitionPhaseBean CostDefinitionPhase() {
		return new CostDefinitionPhaseBean();
	}

	public static UserActivityLogBean UserActivityLog() {
		return new UserActivityLogBean();
	}

	public static PaymentRequestBean PaymentRequest() {
		return new PaymentRequestBean();
	}

	public static PaymentRequestItemBean PaymentRequestItem() {
		return new PaymentRequestItemBean();
	}

	public static PhisicalInitializationToProgIndicatorsBean PhisicalInizializationToProgIndicators() {
		return new PhisicalInitializationToProgIndicatorsBean();
	}

	public static ProgramResultViewIndicatorsBean ProgramResultViewIndicators() {
		return new ProgramResultViewIndicatorsBean();
	}

	public static PotentialProjectsBean PotentialProjects() {
		return new PotentialProjectsBean();
	}

	public static PotentialProjectsToDocumentsBean PotentialProjectsToDocuments() {
		return new PotentialProjectsToDocumentsBean();
	}

	public static PhisicalProgressToDocumentBean PhisicalProgressToDocument() {
		return new PhisicalProgressToDocumentBean();
	}

	public static Object getByType(Object obj) throws PersistenceBeanException {
		if (obj.getClass() == ActivationProcedureAnagraphBean.class) {
			return new ActivationProcedureAnagraph();
		}
		if (obj.getClass() == CostAsse3Bean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3();
		} else if (obj.getClass() == ReportingsBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.Reportings();
		} else if (obj.getClass() == ActivationProcedureInfoBean.class) {
			return new ActivationProcedureInfo();
		} else if (obj.getClass() == AddressesBean.class) {
			return new Addresses();
		} else if (obj.getClass() == AnnualProfilesBean.class) {
			return new AnnualProfiles();
		} else if (obj.getClass() == AssignmentProceduresBean.class) {
			return new AssignmentProcedures();
		} else if (obj.getClass() == BudgetInputSourceDividedBean.class) {
			return new BudgetInputSourceDivided();
		} else if (obj.getClass() == CategoryBean.class) {
			return new Category();
		} else if (obj.getClass() == CFPartnerAnagraphCompletationsBean.class) {
			return new CFPartnerAnagraphCompletations();
		} else if (obj.getClass() == CFPartnerAnagraphsBean.class) {
			return new CFPartnerAnagraphs();
		} else if (obj.getClass() == ControllerUserAnagraphBean.class) {
			return new ControllerUserAnagraph();
		} else if (obj.getClass() == CEPaymentRequestBean.class) {
			return new CEPaymentRequest();
		} else if (obj.getClass() == CostDefinitionsBean.class) {
			return new CostDefinitions();
		} else if (obj.getClass() == CostManagementBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.CostManagement();
		} else if (obj.getClass() == DocumentsBean.class) {
			return new Documents();
		} else if (obj.getClass() == SignedDocumentsBean.class) {
			return new SignedDocuments();
		} else if (obj.getClass() == DocumentsAdmBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.DocumentsAdm();
		} else if (obj.getClass() == DurCompilationsBean.class) {
			return new DurCompilations();
		} else if (obj.getClass() == ExpenditureDeclarationDurCompilationsBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationDurCompilations();
		} else if (obj.getClass() == PaymentApplicationDurCompilations.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations();
		} else if (obj.getClass() == DurInfosBean.class) {
			return new DurInfos();
		} else if (obj.getClass() == MailSettingsBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.MailSettings();
		} else if (obj.getClass() == DurReimbursementsBean.class) {
			return new DurReimbursements();
		} else if (obj.getClass() == DurSummariesBean.class) {
			return new DurSummaries();
		} else if (obj.getClass() == GeneralBudgetsBean.class) {
			return new GeneralBudgets();
		} else if (obj.getClass() == GiuridicalEngagesBean.class) {
			return new GiuridicalEngages();
		} else if (obj.getClass() == LocalChecksBean.class) {
			return new LocalChecks();
		} else if (obj.getClass() == LocalizationsBean.class) {
			return new Localizations();
		} else if (obj.getClass() == MailsBean.class) {
			return new Mails();
		} else if (obj.getClass() == NonRegularySituationsBean.class) {
			return new NonRegularySituations();
		} else if (obj.getClass() == PartnersBudgetsBean.class) {
			return new PartnersBudgets();
		} else if (obj.getClass() == PhisicalInitializationsBean.class) {
			return new PhisicalInitializations();
		} else if (obj.getClass() == PhisicalInitializationToCoreIndicatorsBean.class) {
			return new PhisicalInitializationToCoreIndicators();
		} else if (obj.getClass() == PhisicalInitializationToEmploymentIndicatorsBean.class) {
			return new PhisicalInitializationToEmploymentIndicators();
		} else if (obj.getClass() == PhisicalInitializationToProgramRealizationIndicatorsBean.class) {
			return new PhisicalInitializationToProgramIndicators();
		} else if (obj.getClass() == PhisicalInitializationToProgramResultIndicatorsBean.class) {
			return new PhisicalInitializationToProgramIndicators();
		} else if (obj.getClass() == PhisicalInitializationToQsnIndicatorsBean.class) {
			return new PhisicalInitializationToQsnIndicators();
		} else if (obj.getClass() == ProcedureNotesBean.class) {
			return new ProcedureNotes();
		} else if (obj.getClass() == PhisicalProgressNotesBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressNotes();
		} else if (obj.getClass() == ProceduresBean.class) {
			return new Procedures();
		} else if (obj.getClass() == ProcedureTipologyBean.class) {
			return new ProcedureTipology();
		} else if (obj.getClass() == ProcedureTipologyStepBean.class) {
			return new ProcedureTipologyStep();
		} else if (obj.getClass() == ProcedureTipologyStepInfosBean.class) {
			return new ProcedureTipologyStepInfos();
		} else if (obj.getClass() == ProgressValidationObjectDocumentsBean.class) {
			return new ProgressValidationObjectDocuments();
		} else if (obj.getClass() == ProgressValidationObjectsBean.class) {
			return new ProgressValidationObjects();
		} else if (obj.getClass() == ProjectIformationCompletationsBean.class) {
			return new ProjectIformationCompletations();
		} else if (obj.getClass() == ProjectsBean.class) {
			return new Projects();
		} else if (obj.getClass() == ProjectStatesChangesBean.class) {
			return new ProjectStatesChanges();
		} else if (obj.getClass() == ResponsibilitiesBean.class) {
			return new Responsibilities();
		} else if (obj.getClass() == UserRolesBean.class) {
			return new UserRoles();
		} else if (obj.getClass() == UsersBean.class) {
			return new Users();
		} else if (obj.getClass() == UsersLoginsBean.class) {
			return new UsersLogins();
		} else if (obj.getClass() == ActTypesBean.class) {
			return new ActTypes();
		} else if (obj.getClass() == CFPartnerAnagraphTypesBean.class) {
			return new CFPartnerAnagraphTypes();
		} else if (obj.getClass() == ControllerTypesBean.class) {
			return new ControllerTypes();
		} else if (obj.getClass() == CitiesBean.class) {
			return new Cities();
		} else if (obj.getClass() == CostItemsBean.class) {
			return new CostItems();
		} else if (obj.getClass() == CostStatesBean.class) {
			return new CostStates();
		} else if (obj.getClass() == CountriesBean.class) {
			return new Countries();
		} else if (obj.getClass() == RequestTypesBean.class) {
			return new RequestTypes();
		} else if (obj.getClass() == ExpenditureDeclarationStatesBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates();
		} else if (obj.getClass() == ExpenditureDeclarationBean.class) {
			return new ExpenditureDeclaration();
		} else if (obj.getClass() == PaymentApplicationBean.class) {
			return new PaymentApplication();
		} else if (obj.getClass() == ExpenditureDeclarationAccountingPeriodBean.class) {
			return new com.infostroy.paamns.persistence.beans.facades.ExpenditureDeclarationAccountingPeriodBean();
		} else if (obj.getClass() == AdvancesIncludedInPaymentApplicationBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.AdvancesIncludedInPaymentApplication();
		} else if (obj.getClass() == DepartmentsBean.class) {
			return new Departments();
		} else if (obj.getClass() == DocumentTypesBean.class) {
			return new DocumentTypes();
		} else if (obj.getClass() == DurStatesBean.class) {
			return new DurStates();
		} else if (obj.getClass() == EconomicActivitiesBean.class) {
			return new EconomicActivities();
		} else if (obj.getClass() == NotRegularTypesBean.class) {
			return new NotRegularTypes();
		} else if (obj.getClass() == PaymentTypesBean.class) {
			return new PaymentTypes();
		} else if (obj.getClass() == PaymentWaysBean.class) {
			return new PaymentWays();
		} else if (obj.getClass() == ProgressValidationStatesBean.class) {
			return new ProgressValidationStates();
		} else if (obj.getClass() == ProjectStatesBean.class) {
			return new ProjectStates();
		} else if (obj.getClass() == ProvincesBean.class) {
			return new Provinces();
		} else if (obj.getClass() == ReasonsForDelayBean.class) {
			return new ReasonsForDelay();
		} else if (obj.getClass() == ReasonForAbsenceCIGBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonForAbsenceCIG();
		} else if (obj.getClass() == RegionsBean.class) {
			return new Regions();
		} else if (obj.getClass() == ResponsibilityTypesBean.class) {
			return new ResponsibilityTypes();
		} else if (obj.getClass() == CostItemAsse3Bean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3();
		} else if (obj.getClass() == ResponsibleUsersBean.class) {
			return new ResponsibleUsers();
		} else if (obj.getClass() == RolesBean.class) {
			return new Roles();
		} else if (obj.getClass() == HierarchicalLevelBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.statics.HierarchicalLevel();
		} else if (obj.getClass() == SectionsBean.class) {
			return new Sections();
		} else if (obj.getClass() == SpecificGoalsBean.class) {
			return new SpecificGoals();
		} else if (obj.getClass() == TypologiesBean.class) {
			return new Typologies();
		} else if (obj.getClass() == ZonesBean.class) {
			return new Zones();
		} else if (obj.getClass() == AnagraphicalDataBean.class) {
			return new AnagraphicalData();
		} else if (obj.getClass() == CoreIndicatorsBean.class) {
			return new CoreIndicators();
		} else if (obj.getClass() == EmploymentIndicatorsBean.class) {
			return new EmploymentIndicators();
		} else if (obj.getClass() == FinancialDataBean.class) {
			return new FinancialData();
		} else if (obj.getClass() == IdentificationDataBean.class) {
			return new IdentificationData();
		} else if (obj.getClass() == PhisicalClassificationPriorityThemaBean.class) {
			return new PhisicalClassificationPriorityThema();
		} else if (obj.getClass() == PhisicalClassificationQSNGoalBean.class) {
			return new PhisicalClassificationQSNGoal();
		} else if (obj.getClass() == PrioritaryReasonsBean.class) {
			return new PrioritaryReasons();
		} else if (obj.getClass() == ProgramRealizationIndicatorsBean.class) {
			return new ProgramIndicators();
		} else if (obj.getClass() == ProgramResultIndicatorsBean.class) {
			return new ProgramIndicators();
		} else if (obj.getClass() == QSNGoalBean.class) {
			return new QSNGoal();
		} else if (obj.getClass() == QsnIndicatorsBean.class) {
			return new QsnIndicators();
		} else if (obj.getClass() == CUPBean.class) {
			return new CUP();
		} else if (obj.getClass() == CPTBean.class) {
			return new CPT();
		} else if (obj.getClass() == LocalValueBean.class) {
			return new LocalValue();
		} else if (obj.getClass() == ActivationProcedureTypesBean.class) {
			return new ActivationProcedureTypes();
		} else if (obj.getClass() == NamingBean.class) {
			return new Naming();
		} else if (obj.getClass() == AtecoBean.class) {
			return new Ateco();
		} else if (obj.getClass() == ProgramImplementationBean.class) {
			return new ProgramImplementation();
		} else if (obj.getClass() == CostDefinitionsToDocumentsBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments();
		} else if (obj.getClass() == CFPartnerAnagraphProjectBean.class) {
			return new CFPartnerAnagraphProject();
		} else if (obj.getClass() == CostDefinitionsToNotesBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToNotes();
		} else if (obj.getClass() == LocalCheckToDocumentsBean.class) {
			return new LocalCheckToDocuments();
		} else if (obj.getClass() == FinancingFormsBean.class) {
			return new FinancingForms();
		} else if (obj.getClass() == DurInfoToResponsiblePeopleBean.class) {
			return new DurInfoToResponsiblePeople();
		} else if (obj.getClass() == CostDefinitionPhaseBean.class) {
			return new CostDefinitionPhases();
		} else if (obj.getClass() == FESRInfoBean.class) {
			return new FESRInfo();
		} else if (obj.getClass() == AdditionalFesrInfoBean.class) {
			return new AdditionalFesrInfo();
		} else if (obj.getClass() == AdditionalFesrCFInfoBean.class) {
			return new AdditionalFesrCFInfo();
		} else if (obj.getClass() == RotationFoundInfoBean.class) {
			return new RotationFoundInfo();
		} else if (obj.getClass() == LegendBean.class) {
			return new Legend();
		} else if (obj.getClass() == ApplicationSettingsBean.class) {
			return new ApplicationSettings();
		} else if (obj.getClass() == FinancingPlanBean.class) {
			return new FinancingPlan();
		} else if (obj.getClass() == PaymentRequestBean.class) {
			return new PaymentRequest();
		} else if (obj.getClass() == SuperAdminChangesBean.class) {
			return new SuperAdminChangesBean();
		} else if (obj.getClass() == ProgramIndicatorsBean.class) {
			return new ProgramIndicators();
		} else if (obj.getClass() == ProjectIndicatorsBean.class) {
			return new ProjectIndicators();
		} else if (obj.getClass() == DocumentsAdmBean.class) {
			return new DocumentsAdm();
		} else if (obj.getClass() == PhisicalInitializationToProgIndicatorsBean.class) {
			return new PhisicalInitializationToProgIndicatorsBean();
		} else if (obj.getClass() == PhisicalInitializationToCommunicationIndicatorsBean.class) {
			return new PhisicalInitializationToCommunicationIndicatorsBean();
		} else if (obj.getClass() == ProgramResultViewIndicatorsBean.class) {
			return new ProgramResultViewIndicatorsBean();
		} else if (obj.getClass() == PotentialProjectsBean.class) {
			return new PotentialProjects();
		} else if (obj.getClass() == PotentialProjectsToDocumentsBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjectsToDocuments();
		} else if (obj.getClass() == PhisicalProgressToDocumentBean.class) {
			return new com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressToDocument();
		} else if (obj.getClass() == ExpenditureDeclarationAnnex1dWrapperBean.class) {
			return new com.infostroy.paamns.web.beans.wrappers.ExpenditureDeclarationAnnex1dWrapper();
		} else {
			throw new PersistenceBeanException(
					"Controller of type :" + obj.getClass().getName() + " cann't be found...");
		}
	}
}
