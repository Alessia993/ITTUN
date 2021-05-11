/**
 * 
 */

package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PhisicalInitializationsBean extends
        PersistenceEntityBean<PhisicalInitializations>
{
    @SuppressWarnings("unchecked")
    public Long[] GetByProject(String projectId) throws HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        List<PhisicalInitializations> list = PersistenceSessionManager
                .getBean().getSession()
                .createCriteria(PhisicalInitializations.class)
                .add(Restrictions.eq("project.id", Long.parseLong(projectId)))
                .list();
        
        List<Long> ids = new ArrayList<Long>();
        for (PhisicalInitializations item : list)
        {
            ids.add(item.getId());
        }
        
        return ids.toArray(new Long[0]);
    }
    
    public PhisicalInitializations getByProject(String projectId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        PhisicalInitializations item = (PhisicalInitializations) PersistenceSessionManager
                .getBean().getSession()
                .createCriteria(PhisicalInitializations.class)
                .add(Restrictions.eq("project.id", Long.parseLong(projectId)))
                .uniqueResult();
        
        return item;
    }
}
