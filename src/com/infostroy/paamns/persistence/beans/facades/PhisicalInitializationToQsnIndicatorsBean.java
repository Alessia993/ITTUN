/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PhisicalInitializationToQsnIndicatorsBean extends
        PersistenceEntityBean<PhisicalInitializationToQsnIndicators>
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    @SuppressWarnings("unchecked")
    public Long[] GetByProject(String projectId)
            throws PersistenceBeanException, HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        Long[] idss = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        if (idss.length > 0)
        {
            List<PhisicalInitializationToQsnIndicators> list = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(PhisicalInitializationToQsnIndicators.class)
                    .add(Restrictions.in("phisicalInitialization.id", idss))
                    .list();
            
            List<Long> ids = new ArrayList<Long>();
            for (PhisicalInitializationToQsnIndicators item : list)
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
            sb.append(PhisicalInitializationToQsnIndicators.class
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
    public List<PhisicalInitializationToQsnIndicators> getByProject(
            String projectId) throws PersistenceBeanException,
            HibernateException, NumberFormatException, PersistenceBeanException
    {
        Long[] ids = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        if (ids.length > 0)
        {
            List<PhisicalInitializationToQsnIndicators> list = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(PhisicalInitializationToQsnIndicators.class)
                    .add(Restrictions.in("phisicalInitialization.id", ids))
                    .list();
            
            return list;
        }
        else
        {
            return new ArrayList<PhisicalInitializationToQsnIndicators>();
        }
    }
}
