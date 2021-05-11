/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ApplicationSettings;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ApplicationSettingsBean extends
        PersistenceEntityBean<ApplicationSettings>
{
    public String getValue(ApplicationSettingTypes type)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ApplicationSettings.class);
        crit.add(Restrictions.eq("code", type.getKey()));
        
        ApplicationSettings set = (ApplicationSettings) crit.uniqueResult();
        if (set != null)
        {
            return set.getValue();
        }
        return null;
    }
    
    public ApplicationSettings getItem(ApplicationSettingTypes type)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ApplicationSettings.class);
        crit.add(Restrictions.eq("code", type.getKey()));
        
        return (ApplicationSettings) crit.uniqueResult();
    }
}
