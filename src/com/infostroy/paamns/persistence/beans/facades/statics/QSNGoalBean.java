/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class QSNGoalBean extends LocalizableStaticEntityBean<QSNGoal>
{
    public QSNGoal GetByCode(String code) throws HibernateException,
            PersistenceBeanException
    {
        QSNGoal item = (QSNGoal) PersistenceSessionManager.getBean()
                .getSession().createCriteria(QSNGoal.class)
                .add(Restrictions.eq("priorityCode", code));
        return item;
    }
    
    @SuppressWarnings("unchecked")
    public List<QSNGoal> GetByAsse(String asse) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM QSNGoal AS qsnGoal ");
        sb.append("WHERE qsnGoal.id IN ( ");
        sb.append("SELECT pcqg.qsnGoal.id FROM PhisicalClassificationQSNGoal AS pcqg WHERE pcqg.asse = :asse");
        sb.append(")");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("asse", Integer.valueOf(asse));
        
        return query.list();
    }
}
