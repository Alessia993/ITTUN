/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class AssignmentProceduresBean extends
        PersistenceEntityBean<AssignmentProcedures>
{
    @SuppressWarnings("unchecked")
    public List<AssignmentProcedures> LoadByProject(String projectId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager.getBean().getSession()
                .createCriteria(AssignmentProcedures.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                .list();
    }
    
    @SuppressWarnings("unchecked")
	public List<AssignmentProcedures> LoadByProjectAndUser(String projectId, String userId) throws HibernateException, NumberFormatException, PersistenceBeanException{
    	 return PersistenceSessionManager.getBean().getSession()
                 .createCriteria(AssignmentProcedures.class)
                 .add(Restrictions.eq("deleted", false))
                 .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                 .add(Restrictions.eq("user.id", Long.valueOf(userId)))
                 .list();
    }
}
