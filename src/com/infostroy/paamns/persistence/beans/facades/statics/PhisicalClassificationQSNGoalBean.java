/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PhisicalClassificationQSNGoal;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PhisicalClassificationQSNGoalBean extends
        LocalizableStaticEntityBean<PhisicalClassificationQSNGoal>
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    /**
     * 
     */
    public void DeleteAll()
    {
        Transaction tr = null;
        Query userUpdateQuery;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("Delete from ");
            sb.append(PhisicalClassificationQSNGoal.class.getSimpleName());
            
            userUpdateQuery = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sb.toString());
            
            userUpdateQuery.executeUpdate();
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
    public List<QSNGoal> getByAsse(Integer asse) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT qsn_goal.* "
                + "FROM qsn_goal LEFT OUTER JOIN phisical_classification_qsn_goal "
                + "ON qsn_goal.id = phisical_classification_qsn_goal.qsn_goal_id "
                + "WHERE phisical_classification_qsn_goal.asse_id = :asse");
        Query qr;
        qr = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString()).addEntity(QSNGoal.class);
        qr.setInteger("asse", asse);
        return qr.list();
    }
}
