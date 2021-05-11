/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "controller_types")
public class ControllerTypes extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -5713913376483199355L;
    
    private String            code;
    
    /**
     * Sets code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * 
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
}
