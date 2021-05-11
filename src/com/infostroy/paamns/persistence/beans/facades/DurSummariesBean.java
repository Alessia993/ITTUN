/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class DurSummariesBean extends PersistenceEntityBean<DurSummaries>
{
    public DurSummaries LoadByDurCompilation(Long durCompilationId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurSummaries.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
        
        Object result = criterion.uniqueResult();
        
        if (result != null)
        {
            return (DurSummaries) result;
        }
        else
        {
            return null;
        }
    }
}
