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
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjects;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProgressValidationObjectsBean extends
        PersistenceEntityBean<ProgressValidationObjects>
{
    @SuppressWarnings("unchecked")
    public List<ProgressValidationObjects> LoadByProject(Long projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProgressValidationObjects.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", projectId));
        
        return criterion.list();
    }
}
