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
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureInfo;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ActivationProcedureInfoBean extends
        PersistenceEntityBean<ActivationProcedureInfo>
{
    @SuppressWarnings("unchecked")
    public List<ActivationProcedureInfo> GetInfoForProcedure(Object actProcId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ActivationProcedureInfo.class);
        
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("activationProcedureAnagraph.id",
                actProcId));
        criterion.addOrder(Order.asc("step"));
        
        return criterion.list();
    }
}
