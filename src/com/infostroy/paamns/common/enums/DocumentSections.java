/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public enum DocumentSections
{
    ProjectInfoCompletation(1l),
    CostDefinition(2l),
    LocalCheck(3l),
    DURCompilation(4l),
    ProjectStateChanges(5l),
    DocumentManagement(6l),
    ProgressValidationFlow(7l),
    ValidationFlow(8l),
    AwardProcedure(9l),
    //AdditionalFestInfo(10l),
    //PotentialProjecs(11l);
    AdgTransfer(10l),
    LeadTranser(11l),
    CommunicationDeliverables(12l),
    ProjectManagementDeliverables(13l),
    ResultIndicators(14l),
    ProjectIndicator(15l),
    PotentialProjecs(16l),
    PhysicalStartOfImplementation(17l),
    LPAndPartnerController(18l),
    ProcedureInsert(19l),
    GeneralBudget(20l),
    GeneralBudgetSplittedByPartner(21l),
    FinancialPlanForPartners(22l),
    ApprovalOfBudgetShift(23l),
    LPAndPartnerList(24l),
    Localization(25l),
    AnnualProfile(26l),
    IrregularitiesAndRecoveries(27l),
    ManagementSuspension(28l),
    FinancialProgress(29l),
    ProgressImplementation(30l),
    CostManagement(31l);
    
    private DocumentSections(Long section)
    {
        this.value = section;
    }
    
    /**
     * Sets value
     * @param value the value to set
     */
    public void setValue(Long value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * @return value the value
     */
    public Long getValue()
    {
        return value;
    }
    
    private Long value;
}
