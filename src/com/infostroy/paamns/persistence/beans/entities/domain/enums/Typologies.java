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
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "typologies")
public class Typologies extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 4230669973595385930L;
    
    @Column(name = "for_asse5", columnDefinition = "BIT DEFAULT FALSE")
    private Boolean           forAsse5;
    
    /**
     * Sets forAsse5
     * 
     * @param forAsse5
     *            the forAsse5 to set
     */
    public void setForAsse5(boolean forAsse5)
    {
        this.forAsse5 = forAsse5;
    }
    
    /**
     * Gets forAsse5
     * 
     * @return forAsse5 the forAsse5
     */
    public boolean getForAsse5()
    {
        return forAsse5;
    }
}
