/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public enum ProgressValidationStateTypes
{
    Create(1l), STCEvaluation(2l), AGUEvaluation(3l), Confirmed(4l);
    
    private ProgressValidationStateTypes(Long type)
    {
        this.setValue(type);
    }
    
    /**
     * Sets value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(Long value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * 
     * @return value the value
     */
    public Long getValue()
    {
        return value;
    }
    
    private Long value;
}
