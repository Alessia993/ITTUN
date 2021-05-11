/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Recoveries;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2011.
 *
 */
public class RecoveriesBean extends PersistenceEntityBean<Recoveries>
{
    @SuppressWarnings("unchecked")
    public List<Recoveries> loadByCostDefinition(Long id)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Recoveries.class);
        
        criteria.add(Restrictions.eq("costDefinition.id", id));
        
        return criteria.list();
    }
}
