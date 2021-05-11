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
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProjectStatesChangesBean extends
        PersistenceEntityBean<ProjectStatesChanges>
{
    public ProjectStatesChanges LoadByProjectAndStates(Long projectId,
            Long startStateId, Long endStateId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectStatesChanges.class);
        
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", projectId));
        criterion.add(Restrictions.eq("fromState.id", startStateId));
        criterion.add(Restrictions.eq("toState.id", endStateId));
        
        return (ProjectStatesChanges) criterion.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<ProjectStatesChanges> LoadByProject(Long projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectStatesChanges.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", projectId));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<ProjectStatesChanges> LoadByDocument(Long documentId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectStatesChanges.class);
        criterion.add(Restrictions.eq("document.id", documentId));
        
        return criterion.list();
    }
}
