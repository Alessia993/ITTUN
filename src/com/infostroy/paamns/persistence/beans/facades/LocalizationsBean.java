/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Localizations;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class LocalizationsBean extends PersistenceEntityBean<Localizations>
{
    /**
     * @author Nickolay Sumyatin
     * @param projectId
     * @return
     * @throws PersistentException
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistentException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public List<Localizations> LoadByProject(String projectId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager.getBean().getSession()
                .createCriteria(Localizations.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                .list();
    }
}
