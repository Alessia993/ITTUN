/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "countries")
public class Countries extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 7166231274118745113L;
    
    @Column(name = "code")
    private String            code;
    
    /**
     * Stores isDefault value of entity.
     */
    @Column(name = "is_default")
    private Boolean           isDefault;
    
    /**
     * Sets isDefault
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(Boolean isDefault)
    {
        this.isDefault = isDefault;
    }
    
    /**
     * Gets isDefault
     * @return isDefault the isDefault
     */
    public Boolean getIsDefault()
    {
        return isDefault;
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
}
