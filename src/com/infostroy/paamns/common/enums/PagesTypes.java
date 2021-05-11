/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public enum PagesTypes
{
    ACCESSDENIED("Acl/AccessDenied.jsf"),
    ACTIVATEPROCEDUREEDIT("ProgramMS/ActivationProcedureEdit.jsf"),
    ACTIVATEPROCEDURELIST("ProgramMS/ActivationProcedureList.jsf"),
    ADDITIONALFESRINFOLIST("ProjectMS/AdditionalFesrInfoList.jsf"),
    ADDITIONALFESRICFNFOLIST("ProjectMS/AdditionalFesrCFInfoList.jsf"),
    ADDITIONALFESRINFOEDIT("ProjectMS/AdditionalFesrInfoEdit.jsf"),
    ADDITIONALFESRCFINFOEDIT("ProjectMS/AdditionalFesrCFInfoEdit.jsf"),
    ANAGRAPHICALDATAVIEW("ProgramMS/AnagraphicalDataView.jsf"),
    ANNUALPROFILESEDIT("ProjectMS/AnnualProfileEdit.jsf"),
    ANNUALPROFILESLIST("ProjectMS/AnnualProfileList.jsf"),
    APPLICATIONSETTINGS("Acl/AppSettings.jsf"),
    ASSIGMENTPROCEDUREEDIT("ProjectMS/AssignmentProcedureEdit.jsf"),
    ASSIGMENTPROCEDURELIST("ProjectMS/AssignmentProcedureList.jsf"),
    BUDGETLIST("ProjectMS/BudgetList.jsf"),
    BUDGETVIEW("ProjectMS/BudgetView.jsf"),
    CFPARTNERANAGRAPHEDIT("ProjectMS/CFPartnerAnagraphCompletationsEdit.jsf"),
    CFPARTNERANAGRAPHLIST("ProjectMS/CFPartnerAnagraphCompletationsList.jsf"),
    CFPARTNEREDIT("ProjectMS/CFPartnerEdit.jsf"),
    CFPARTNERLIST("ProjectMS/CFPartnerList.jsf"),
    CHANGEPASSWORD("Acl/ChangePassword.jsf"),
    CONTROLLERUSERANAGRAPHEDIT("ProgramMS/ControllerUserAnagraphEdit.jsf"),
    CONTROLLERUSERANAGRAPHLIST("ProgramMS/ControllerUserAnagraphList.jsf"),
    CEPAYMENTREQUEST("ProgramMS/CEPaymentRequest.jsf"),
    COSTCERTIFICATIONLIST("ProjectMS/CostCertificationList.jsf"),
    COSTDEFINITIONEDIT("ProjectMS/CostDefinitionEdit.jsf"),
    COSTDEFINITIONLIST("ProjectMS/CostDefinitionList.jsf"),
    DOCUMENTEDIT("DocumentMS/DocumentEdit.jsf"),
    DOCUMENTLIST("DocumentMS/DocumentList.jsf"),
    DURCOMPILATIONEDIT("ProjectMS/DURCompilationEdit.jsf"),
    SUSPENSIONMANAGEMENTEDIT("ProjectMS/SuspensionManagementEdit.jsf"),
    DURCOMPILATIONLIST("ProjectMS/DURCompilationList.jsf"),
    DURREIMBURSEMENTEDIT("ProjectMS/DURReimbursementEdit.jsf"),
    DURREIMBURSEMENTLIST("ProjectMS/DURReimbursementList.jsf"),
    FESRINFO("ProjectMS/FESRInfo.jsf"),
    FINANCIALDATAVIEW("ProgramMS/FinancialDataView.jsf"),
    FINANCILAPROGRESSEDIT("ProjectMS/FinancialProgressEdit.jsf"),
    FINANCILAPROGRESSVIEW("ProjectMS/FinancialProgressView.jsf"),
    FOOTER("/Footer.xhtml"),
    GENERALBUDGETVIEW("ProjectMS/GeneralBudgetView.jsf"),
    GIURIDICALENGAGEEDIT("ProjectMS/JuridicalEngageEdit.jsf"),
    GIURIDICALENGAGELIST("ProjectMS/JuridicalEngageList.jsf"),
    HEADER("/Header.xhtml"),
    IDEVTIFICATIONDATAVIEW("ProgramMS/IdentificationDataView.jsf"),
    INDEX("Common/Index.jsf"),
    INDICATORSVIEW("ProgramMS/IndicatorsView.jsf"),
    LEGENDVIEW("ProgramMS/LegendView.jsf"),
    LOCALCHECKEDIT("ProjectMS/LocalCheckEdit.jsf"),
    LOCALCHECKLIST("ProjectMS/LocalCheckList.jsf"),
    LOCALIZATIONEDIT("ProjectMS/LocalizationEdit.jsf"),
    LOCALIZATIONLIST("ProjectMS/LocalizationList.jsf"),
    LOGIN("Login.jsf"),
    LOGINHISTORY("Acl/LoginHistory.jsf"),
    MAILMANAGMANTVIEW("Acl/MailManagmentView.jsf"),
    MAILSETTINGSEDIT("Acl/MailSettingsEdit.jsf"),
    MAILSETTINGSLIST("Acl/MailSettingsList.jsf"),
    MENU("/Menu.xhtml"),
    NOTREGULARDUREDIT("ProjectMS/NotRegularDurEdit.jsf"),
    NOTREGULARDURLIST("ProjectMS/NotRegularDurList.jsf"),
    PARTNERBUDGETEDIT("ProjectMS/PartnerBudgetEdit.jsf"),
    PARTNERBUDGETLIST("ProjectMS/PartnerBudgetView.jsf"),
    PAYMENTREQUESTLIST("ProjectMS/PaymentRequestList.jsf"),
    PAYMENTREQUESTVIEW("ProjectMS/PaymentRequestView.jsf"),
    PHISICALCLASSIFICATIONPRIORITYTHEMAVIEW(
                                            "ProgramMS/PhisicalClassificationPriorityThemaView.jsf"),
    PHISICALCLASSIFICATIONQSNGOALVIEW(
                                      "ProgramMS/PhisicalClassificationQSNGoalView.jsf"),
    PHISICALINITIALIZATIONEDIT("ProjectMS/PhisicalInitializationEdit.jsf"),
    PHISICALINITIALIZATIONLIST("ProjectMS/PhisicalInitializationList.jsf"),
    PHISICALPROGRAMCLASSIFICATIONVIEW(
                                      "ProgramMS/PhisicalProgramClassificationView.jsf"),
    PHISICALPROGRESSVIEW("ProjectMS/PhisicalProgressView.jsf"),
    PRIORITARYREASENSVIEW("ProgramMS/PrioritaryReasonsView.jsf"),
    PROCEDUREINSERTEDIT("ProjectMS/ProcedureInsertEdit.jsf"),
    PROCEDUREINSERTLIST("ProjectMS/ProcedureInsertList.jsf"),
    PROCEDUREINSERTNOTESLIST("ProjectMS/ProcedureInsertNotesList.jsf"),
    PROCEDUREPROGRESSEDIT("ProjectMS/ProcedureProgressEdit.jsf"),
    PROCEDUREPROGRESSLIST("ProjectMS/ProcedureProgressList.jsf"),
    PROGRESSVALIDATIONFLOWEDIT("ProjectMS/ProgressValidationFlowEdit.jsf"),
    PROGRESSVALIDATIONFLOWVIEW("ProjectMS/ProgressValidationFlowView.jsf"),
    PROJECTEDIT("ProjectMS/ProjectEdit.jsf"),
    PROJECTINDEX("ProjectMS/Index.jsf"),
    PROJECTINFORMATIONCOMPLETATTION(
                                    "ProjectMS/ProjectInformationCompletationView.jsf"),
    PROJECTINDICATORSEDIT("ProjectMS/ProjectIndicatorsEdit.jsf"),
    PROJECTLIST("ProjectMS/ProjectList.jsf"),
    PROJECTSTART("ProjectMS/ProjectStart.jsf"),
    QSNGOALVIEW("ProgramMS/QSNGoalView.jsf"),
    RECTIFICATIONMANAGEMENTLIST("ProjectMS/RectificationManagementList.jsf"),
    RECTIFICATIONMANAGEMENTEDIT("ProjectMS/RectificationManagementEdit.jsf"),
    ROTATIONFOUNDINFO("ProjectMS/RotationFoundInfo.jsf"),
    SEARCH("ProjectMS/Search.jsf"),
    STATECHANGESLIST("ProjectMS/StateChangesList.jsf"),
    STATEMANAGMENT("ProjectMS/StateManagment.jsf"),
    SUSPENSIONMANAGEMENTLIST("ProjectMS/SuspensionManagementList.jsf"),
    USEREDIT("Acl/UserEdit.jsf"),
    USERLIST("Acl/UserList.jsf"),
    VALIDATIONFLOWVIEW("ProjectMS/ValidationFlowView.jsf"),
    POTENTIALPROJECTSLIST("Acl/PotentialProjectsList.jsf"),
    POTENTIALPROJECTEDIT("Acl/PotentialProjectEdit.jsf"),
    EXPENDITUREDECLARATIONEDIT("ProjectMS/ExpenditureDeclarationEdit.jsf"),
	EXPENDITUREDECLARATIONLIST("ProjectMS/ExpenditureDeclarationList.jsf"),
	PAYMENTAPPLICATIONEDIT("ProjectMS/PaymentApplicationEdit.jsf"),
	PAYMENTAPPLICATIONLIST("ProjectMS/PaymentApplicationList.jsf"),
	CLOSINGACCOUNTSEDIT("ProjectMS/ClosingOfAccountsEdit.jsf"),
	CLOSINGACCOUNTSLIST("ProjectMS/ClosingOfAccountsList.jsf"),
	PHISICALPROGRESSUPLOADEDITBEAN("ProjectMS/PhisicalProgressUploadEdit.jsf"),
	PHISICALPROGRESSNOTESBEAN("ProjectMS/PhisicalProgressNotesEdit.jsf");
    
    public static String getPageByPath(String path)
    {
        for (PagesTypes type : PagesTypes.values())
        {
            if (path.indexOf(type.getPagesContext()) != -1)
            {
                return type.getPagesContext();
            }
        }
        
        return "";
    }
    
    private String page;
    
    /**
     * 
     * @param page
     */
    private PagesTypes(String page)
    {
        this.page = page;
    }
    
    public String getPagesContext()
    {
        return page;
    }
}
