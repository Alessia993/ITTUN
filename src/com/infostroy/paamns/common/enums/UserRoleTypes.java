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
public enum UserRoleTypes
{
    APU("APU"),
    AAU("AAU"),
    ACU("ACU"),
    STC("STC"),
    BP("BP"),
    B("B"),
    CIL("CIL"),
    AGU("AGU"),
    DAEC("DAEC"),
    AGUM("AGUM"),
    UC("UC"),
    ANCM("ANCM"),
    CFP("CFP"),
	PCI("PCI"),
	PCT("PCT");
    
    private UserRoleTypes(String value)
    {
        this.setValue(value);
    }
    
    /**
     * Sets value
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * @return value the value
     */
    public String getValue()
    {
        return value;
    }
    
    private String value;
}
