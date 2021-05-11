/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CFPartnerAnagraphCompletationsBean extends
        PersistenceEntityBean<CFPartnerAnagraphCompletations>
{
    @SuppressWarnings("unchecked")
    public List<CFPartnerAnagraphCompletations> GetEntitiesForCFAnagraph(
            Long cfAnagraphId, String projectId)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CFPartnerAnagraphCompletations.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraphproject.id",
                cfAnagraphId));
        criterion.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<CFPartnerAnagraphCompletations> GetEntitiesForCFAnagraph(
            Long cfAnagraphId) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CFPartnerAnagraphCompletations.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraphproject.id",
                cfAnagraphId));
        
        return criterion.list();
    }
    
    public CFPartnerAnagraphCompletations GetByCFAnagraph(Long cfAnagraphId,
            String projectId) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CFPartnerAnagraphCompletations.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraphproject.id",
                cfAnagraphId));
        criterion.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        
        if (criterion.list() == null || criterion.list().isEmpty())
        {
            return null;
        }
        else
        {
            if (criterion.list().size() > 1)
            {
                BeansFactory.CFPartnerAnagraphCompletations()
                        .DeleteInTransaction(
                                (CFPartnerAnagraphCompletations) criterion
                                        .list().get(1));
            }
            return (CFPartnerAnagraphCompletations) criterion.list().get(0);
        }
    }
}
