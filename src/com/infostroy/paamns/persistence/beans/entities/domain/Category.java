/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.persistence.LocaleSessionManager;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2012.
 *
 */
@Entity
@Table(name = "category")
public class Category extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    /**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= -6784931854386346314L;

	@Column(name = "name_it")
    private String nameIt;
    
    @Column(name = "name_fr")
    private String nameFr;
    
    /**
     * Gets name
     * @return name the name
     */
    @Transient
    public String getName()
    {
        Locale loc = LocaleSessionManager.getInstance().getInstanceBean()
                .getCurrLocale();
        
        if (loc != null)
        {
            if (loc.equals(Locale.ITALIAN))
            {
                return nameIt;
            }
            else if (loc.equals(Locale.FRENCH))
            {
                return nameFr;
            }
        }
        
        return null;
    }
    
    /**
     * Sets name
     * @param name the name to set
     */
    @Transient
    public void setName(String name)
    {        
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
    /**
     * Gets nameFr
     * @return nameFr the nameFr
     */
    public String getNameFr()
    {
        return nameFr;
    }
    
    /**
     * Sets nameFr
     * @param nameFr the nameFr to set
     */
    public void setNameFr(String nameFr)
    {
        this.nameFr = nameFr;
    }
    
    /**
     * Gets nameIt
     * @return nameIt the nameIt
     */
    public String getNameIt()
    {
        return nameIt;
    }
    
    /**
     * Sets nameIt
     * @param nameIt the nameIt to set
     */
    public void setNameIt(String nameIt)
    {
        this.nameIt = nameIt;
    }
}
