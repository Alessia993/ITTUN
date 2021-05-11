/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "application_settings")
public class ApplicationSettings extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    @Column
    private String code;
    
    @Column
    private String value;
    
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
     * Sets key
     * @param key the key to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets key
     * @return key the key
     */
    public String getCode()
    {
        return code;
    }
}
