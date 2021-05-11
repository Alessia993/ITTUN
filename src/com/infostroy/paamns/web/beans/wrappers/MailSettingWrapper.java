/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class MailSettingWrapper
{
    
    /**
     * @param id
     * @param value
     * @param displayValue
     */
    public MailSettingWrapper(Long id, String value, String displayValue)
    {
        super();
        this.id = id;
        this.value = value;
        this.displayValue = displayValue;
    }
    
    private Long   id;
    
    private String value;
    
    private String displayValue;
    
    /**
     * Sets displayValue
     * @param displayValue the displayValue to set
     */
    public void setDisplayValue(String displayValue)
    {
        this.displayValue = displayValue;
    }
    
    /**
     * Gets displayValue
     * @return displayValue the displayValue
     */
    public String getDisplayValue()
    {
        return displayValue;
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
    
    /**
     * Sets id
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * Gets id
     * @return id the id
     */
    public Long getId()
    {
        return id;
    }
}
