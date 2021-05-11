/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@javax.persistence.Entity
@Table(name = "mail_settings")
public class MailSettings extends Entity
{
    
    /**
     * Stores template value of entity.
     */
    @Column(columnDefinition = "TEXT", name = "value")
    private String value;
    
    /**
     * Stores host value of entity.
     */
    @Column(name = "key_field")
    private String key;
    
    /**
     * Sets value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * 
     * @return value the value
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * Sets key
     * 
     * @param key
     *            the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    
    /**
     * Gets key
     * 
     * @return key the key
     */
    public String getKey()
    {
        return key;
    }
    
}
