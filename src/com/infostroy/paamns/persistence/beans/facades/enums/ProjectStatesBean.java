/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ProjectStatesBean extends
        LocalizableEnumerationBean<ProjectStates>
{
    public ProjectStates GetProjectState(ProjectState state)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectStates.class);
        criteria.add(Restrictions.eq("code", state.getCode()));
        
        return (ProjectStates) criteria.uniqueResult();
    }
}
