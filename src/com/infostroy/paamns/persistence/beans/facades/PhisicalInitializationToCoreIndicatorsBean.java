/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PhisicalInitializationToCoreIndicatorsBean extends
        PersistenceEntityBean<PhisicalInitializationToCoreIndicators>
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    @SuppressWarnings("unchecked")
    public Long[] GetByProject(String projectId)
            throws PersistenceBeanException
    {
        Long[] idss = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        if (idss.length > 0)
        {
            List<PhisicalInitializationToCoreIndicators> list = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(
                            PhisicalInitializationToCoreIndicators.class)
                    .add(Restrictions.in("phisicalInitialization.id", idss))
                    .list();
            
            List<Long> ids = new ArrayList<Long>();
            for (PhisicalInitializationToCoreIndicators item : list)
            {
                ids.add(item.getIndicator().getId());
            }
            
            return ids.toArray(new Long[0]);
        }
        else
        {
            return new Long[0];
        }
    }
    
    public void DeleteAll(String projectId)
    {
        Transaction tr = null;
        Query userUpdateQuery;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("Delete from ");
            sb.append(PhisicalInitializationToCoreIndicators.class
                    .getSimpleName());
            
            sb.append(" where phisicalInitialization.id = :phisId");
            userUpdateQuery = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sb.toString());
            PhisicalInitializations item = BeansFactory
                    .PhisicalInizializations().getByProject(projectId);
            if (item != null)
            {
                userUpdateQuery.setLong("phisId", item.getId());
                userUpdateQuery.executeUpdate();
            }
        }
        catch(HibernateException e)
        {
            if (tr != null)
            {
                tr.rollback();
            }
            
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            if (tr != null)
            {
                tr.rollback();
            }
            
            log.error(e);
        }
        finally
        {
            if (tr != null && !tr.wasRolledBack() && tr.isActive())
            {
                tr.commit();
            }
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToCoreIndicators> getByProject(
            String projectId) throws PersistenceBeanException
    {
        Long[] ids = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        if (ids.length > 0)
        {
            List<PhisicalInitializationToCoreIndicators> list = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(
                            PhisicalInitializationToCoreIndicators.class)
                    .add(Restrictions.in("phisicalInitialization.id", ids))
                    .list();
            
            return list;
        }
        else
        {
            return new ArrayList<PhisicalInitializationToCoreIndicators>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToCoreIndicators> LoadLastByProject(
            String projectId) throws PersistenceBeanException
    {
        Long[] ids = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        
        if (ids.length > 0)
        {
            Criteria criterion = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(
                            PhisicalInitializationToCoreIndicators.class);
            criterion.add(Restrictions.in("phisicalInitialization.id", ids));
            
            Long lastVersion = this.GetLastVersion(projectId);
            
            if (lastVersion != null)
            {
                criterion.add(Restrictions.eq("version", lastVersion));
            }
            
            return criterion.list();
        }
        else
        {
            return new ArrayList<PhisicalInitializationToCoreIndicators>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToCoreIndicators> LoadByProgressValidationObject(
            Long progressValidationObjectId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PhisicalInitializationToCoreIndicators.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("progressValidationObject.id",
                progressValidationObjectId));
        
        return criterion.list();
    }
    
    public Long GetLastVersion(String projectId) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM PhisicalInitializationToCoreIndicators p ");
        sb.append("WHERE p.phisicalInitialization.id IN (SELECT pi.id FROM PhisicalInitializations pi WHERE pi.project.id = :projectId )");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", Long.valueOf(projectId));
        
        return (Long) query.uniqueResult();
    }
}
