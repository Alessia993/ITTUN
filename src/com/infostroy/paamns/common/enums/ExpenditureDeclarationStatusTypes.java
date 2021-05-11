package com.infostroy.paamns.common.enums;

public enum ExpenditureDeclarationStatusTypes {

	Compiled("compiled"),
	Generated("generated"),
	Submitted("submitted");
    
    private ExpenditureDeclarationStatusTypes(String val)
    {
        this.setValue(val);
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
