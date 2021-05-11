/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProjectIformationCompletationsBean extends
        PersistenceEntityBean<ProjectIformationCompletations>
{
    /**
     * Load ProjectIformationCompletations by project    
     * @param projectId
     * @return
     * @throws PersistenceBeanException
     * @author Nickolay Sumyatin
     * @throws PersistenceBeanException 
     */
    @SuppressWarnings("unchecked")
    public ProjectIformationCompletations LoadByProject(String projectId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        List<ProjectIformationCompletations> lst = PersistenceSessionManager
                .getBean().getSession()
                .createCriteria(ProjectIformationCompletations.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                .list();
        
        if (lst.isEmpty())
        {
            return null;
        }
        else
        {
            return lst.get(0);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<ProjectIformationCompletations> LoadByDocument(Long documentId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectIformationCompletations.class);
        criterion.add(Restrictions.eq("picDocument.id", documentId));
        
        return criterion.list();
    }
}
