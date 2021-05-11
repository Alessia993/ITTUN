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
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ActivationProcedureAnagraphBean extends
        PersistenceEntityBean<ActivationProcedureAnagraph>
{
    /**
     * Method loads activation procedures by user role
     * @param isAGU
     * @return List of activation procedure anagraphs
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public List<ActivationProcedureAnagraph> LoadByUser(boolean isAGU)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ActivationProcedureAnagraph.class)
                .add(Restrictions.eq("deleted", false));
        
        criterion.add(Restrictions.eq("isCreatedByAGUUser", isAGU));
        criterion.addOrder(Order.desc("createDate"));
        
        List<ActivationProcedureAnagraph> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<ActivationProcedureAnagraph> LoadSorted()
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ActivationProcedureAnagraph.class)
                .add(Restrictions.eq("deleted", false));
        
        criterion.addOrder(Order.desc("createDate"));
        
        return criterion.list();
    }
}
