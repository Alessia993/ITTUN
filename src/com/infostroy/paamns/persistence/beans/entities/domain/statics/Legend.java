/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */

@Entity
@Table(name = "legend")
public class Legend extends LocalizableStaticEntity
{
    /**
     * Stores acronyms value of entity.
     */
    private String acronyms;
    
    /**
     * Stores description value of entity.
     */
    private String description;
    
    /**
     * Sets acronyms
     * @param acronyms the acronyms to set
     */
    public void setAcronyms(String acronyms)
    {
        this.acronyms = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.acronyms, acronyms));
    }
    
    /**
     * Gets acronyms
     * @return acronyms the acronyms
     */
    public String getAcronyms()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.acronyms,
                null));
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, description));
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
}
