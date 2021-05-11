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
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2014.
 *
 */
public class ProjectIndicatorsBean extends
			PersistenceEntityBean<ProjectIndicators>
{
    @SuppressWarnings("unchecked")
    public List<ProjectIndicators> LoadByProject(String projectId, String indicatorId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProjectIndicators.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                .add(Restrictions.eq("indicatorId", Long.parseLong(indicatorId)))
                .list();
    }
    
    @SuppressWarnings("unchecked")
	public List<ProjectIndicators> LoadAllByTypology(Long typology) throws HibernateException, PersistenceBeanException{
    	return PersistenceSessionManager.getBean().getSession()
    			.createCriteria(ProjectIndicators.class)
    			.add(Restrictions.eq("deleted", false))
    			.add(Restrictions.eq("typologyProductIndicator", typology)).list();
    }
}
