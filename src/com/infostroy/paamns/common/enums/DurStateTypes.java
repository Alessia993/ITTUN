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
public enum DurStateTypes
{
    Create(1l),
    STCEvaluation(2l),
    UCValidation(10l),
    AGUEvaluation(3l),
    ACUEvaluation(4l),
    Reimbursement(5l),
    Reimbursed(6l),
    Certifiable(7l),
    Certified(8l),
    SendToExpenditureDeclaration(9l);
    
    private DurStateTypes(Long val)
    {
        this.setValue(val);
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
