/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public enum ResponsibilityTypeTypes
{
    LAWRESPONSIBLE("Law responsible"),
    FINANCIALRESPONSIBLE("Financial responsible"),
    RESPONSIBLEPEOPLE("Responsible people");
    
    private ResponsibilityTypeTypes(String value)
    {
        this.setResponsibilityType(value);
    }
    
    /**
     * Sets responsibilityType
     * 
     * @param responsibilityType
     *            the responsibilityType to set
     */
    public void setResponsibilityType(String responsibilityType)
    {
        this.responsibilityType = responsibilityType;
    }
    
    /**
     * Gets responsibilityType
     * 
     * @return responsibilityType the responsibilityType
     */
    public String getResponsibilityType()
    {
        return responsibilityType;
    }
    
    private String responsibilityType;
}
