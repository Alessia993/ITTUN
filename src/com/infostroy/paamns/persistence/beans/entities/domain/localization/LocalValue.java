/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.localization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "local_values")
public class LocalValue extends PersistentEntity
{
    /**
     * 
     */
    @Column(name = "local_id", length = 64)
    private String localId;
    
    /**
     * 
     */
    @Column(name = "entity_id", length = 128)
    private String entityId;
    
    /**
     * 
     */
    @Column(name = "locale", length = 8)
    private String locale;
    
    /**
     * 
     */
    @Column(name = "value", length = 512)
    private String value;
    
    /**
     * 
     * @param localId
     */
    public void setLocalId(String localId)
    {
        this.localId = localId;
    }
    
    /**
     * 
     * @return
     */
    public String getLocalId()
    {
        return this.localId;
    }
    
    /**
     * 
     * @param entityId
     */
    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
    }
    
    /**
     * 
     * @return
     */
    public String getEntityId()
    {
        return this.entityId;
    }
    
    /**
     * 
     * @param locale
     */
    public void setLocale(String locale)
    {
        this.locale = locale;
    }
    
    /**
     * 
     * @return
     */
    public String getLocale()
    {
        return this.locale;
    }
    
    /**
     * 
     * @param locale
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * 
     * @param locale
     */
    public String getValue()
    {
        return this.value;
    }
    
}
