package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class AnnualProfilesBean extends PersistenceEntityBean<AnnualProfiles>
{
    /**
     * @author Nickolay Sumyatin
     * @param projectId
     * @return
     * @throws PersistenceBeanException 
     * @throws NumberFormatException 
     * @throws HibernateException 
     */
    @SuppressWarnings("unchecked")
    public List<AnnualProfiles> LoadByProject(String projectId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager.getBean().getSession()
                .createCriteria(AnnualProfiles.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
                .list();
    }
    
    @SuppressWarnings("unchecked")
    public List<AnnualProfiles> LoadLastByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(AnnualProfiles.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        Long lastVersion = this.GetLastVersion(projectId);
        
        if (lastVersion != null)
        {
            criterion.add(Restrictions.eq("version",
                    this.GetLastVersion(projectId)));
        }
        
        return criterion.list();
    }
    
    public Long GetLastVersion(String projectId)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM AnnualProfiles ap ");
        sb.append("WHERE ap.project.id = :projectId AND ap.deleted = false ");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", Long.valueOf(projectId));
        
        return (Long) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<AnnualProfiles> LoadByProgressValidationObject(
            Long progressValidationObjectId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(AnnualProfiles.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("progressValidationObject.id",
                progressValidationObjectId));
        
        return criterion.list();
    }
}
