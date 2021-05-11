/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PrioritaryReasonsBean extends
        LocalizableStaticEntityBean<PrioritaryReasons>
{
    public PrioritaryReasons GetByCode(String code) throws HibernateException,
            PersistenceBeanException
    {
        PrioritaryReasons item = (PrioritaryReasons) PersistenceSessionManager
                .getBean().getSession().createCriteria(PrioritaryReasons.class)
                .add(Restrictions.eq("code", code));
        return item;
    }
    
    @SuppressWarnings("unchecked")
    public List<PrioritaryReasons> LoadNotLast()
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PrioritaryReasons.class);
        List<PrioritaryReasons> list = criterion.list();
        /*if (list.size() > 0)
        {
            list.remove(list.size() - 1);
        } */
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<PrioritaryReasons> GetByAsse(String asse)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM PrioritaryReasons AS priorReason ");
        sb.append("WHERE priorReason.id IN ( ");
        sb.append("SELECT pcpt.prioritaryReason.id FROM PhisicalClassificationPriorityThema AS pcpt WHERE pcpt.asse = :asse");
        sb.append(")");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("asse", Integer.valueOf(asse));
        
        return query.list();
    }
}
