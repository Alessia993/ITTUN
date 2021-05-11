/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "regions")
public class Regions extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 3258419166470329747L;
    
    /**
     * Stores isForItLoc value of entity.
     */
    @Column(name = "is_for_it_loc")
    private Boolean           isForItLoc;
    
    /**
     * Stores isForFrLoc value of entity.
     */
    @Column(name = "is_for_fr_loc")
    private Boolean           isForFrLoc;
    
    /**
     * Stores Country value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "country_id")
    private Countries         country;
    
    @Column(name = "code")
    private String            code;
    
    /**
     * Sets isForItLoc
     * @param isForItLoc the isForItLoc to set
     */
    public void setIsForItLoc(Boolean isForItLoc)
    {
        this.isForItLoc = isForItLoc;
    }
    
    /**
     * Gets isForItLoc
     * @return isForItLoc the isForItLoc
     */
    public Boolean getIsForItLoc()
    {
        return isForItLoc;
    }
    
    /**
     * Sets isForFrLoc
     * @param isForFrLoc the isForFrLoc to set
     */
    public void setIsForFrLoc(Boolean isForFrLoc)
    {
        this.isForFrLoc = isForFrLoc;
    }
    
    /**
     * Gets isForFrLoc
     * @return isForFrLoc the isForFrLoc
     */
    public Boolean getIsForFrLoc()
    {
        return isForFrLoc;
    }
    
    /**
     * Sets Country
     * @param country the country to set
     */
    public void setCountry(Countries country)
    {
        this.country = country;
    }
    
    /**
     * Gets Country
     * @return Country the country
     */
    public Countries getCountry()
    {
        return country;
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
