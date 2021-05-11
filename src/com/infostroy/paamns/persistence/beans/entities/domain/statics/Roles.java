/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;

/**
 *
 * @author Alexander Chelombitko
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "roles")
public class Roles extends LocalizableStaticEntity
{
    /**
     * Stores shortName value of entity.
     */
    @Column(name = "code")
    private String          code;
    
    /**
     * Stores shortName value of entity.
     */
    @Column(name = "description")
    private String          description;
    
    /**
     * 
     */
    @OneToMany(mappedBy = "role")
    private List<UserRoles> userRoles;
    
    /**
     * Sets shortName
     * @param shortName the shortName to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets shortName
     * @return shortName the shortName
     */
    public String getCode()
    {
        return this.code;
    }
    
    /**
     * 
     */
    @Override
    public String toString()
    {
        return getDescription();
    }
    
    /**
     * Sets userRoles
     * @param userRoles the userRoles to set
     */
    public void setUserRoles(List<UserRoles> userRoles)
    {
        this.userRoles = userRoles;
    }
    
    /**
     * Gets userRoles
     * @return userRoles the userRoles
     */
    public List<UserRoles> getUserRoles()
    {
        return userRoles;
    }
    
    /**
     * 
     * @return
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
    
    /**
     * 
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.description,
                description));
    }
    
}
