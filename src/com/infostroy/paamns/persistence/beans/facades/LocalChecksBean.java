/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalChecks;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class LocalChecksBean extends PersistenceEntityBean<LocalChecks>
{
    @SuppressWarnings("unchecked")
    public List<LocalChecks> getByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(LocalChecks.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.add(Restrictions.eq("deleted", false));
        crit.addOrder(Order.desc("createDate"));
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<LocalChecks> getByProject(String projectId, Boolean closed)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(LocalChecks.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.add(Restrictions.eq("deleted", false));
        if (closed != null)
        {
            crit.add(Restrictions.eq("closedControl", closed));
        }
        crit.addOrder(Order.desc("createDate"));
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<LocalChecks> getByProjectAndPartners(String projectId,
            List<Long> partnerIds, Boolean closed)
            throws PersistenceBeanException
    {
        if(partnerIds.isEmpty())
        {
            return null; 
        }
        
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(LocalChecks.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.add(Restrictions.eq("deleted", false));
        crit.add(Restrictions.in("partner.id", partnerIds));
        if (closed != null)
        {
            crit.add(Restrictions.eq("closedControl", closed));
        }
        crit.addOrder(Order.desc("createDate"));
        return crit.list();
    }
}
