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
@Table(name = "economic_activities")
public class EconomicActivities extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 460589523377000304L;
    
    @Column(name = "code")
    private int               code;
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(int code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public int getCode()
    {
        return code;
    }
    
}
