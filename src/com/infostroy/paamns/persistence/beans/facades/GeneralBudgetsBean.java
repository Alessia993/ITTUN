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
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class GeneralBudgetsBean extends PersistenceEntityBean<GeneralBudgets>
{
    @SuppressWarnings("unchecked")
    public List<GeneralBudgets> GetItemsForProject(Long projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GeneralBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", projectId));
        criterion.add(Restrictions.eq("isOld", false));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<GeneralBudgets> GetItemsForCFAnagraph(String projectId,
            Long cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GeneralBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id", cfAnagraphId));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<GeneralBudgets> GetActualItemsForCFAnagraph(String projectId,
            Long cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GeneralBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id", cfAnagraphId));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("isOld", false));
        
        return criterion.list();
    }
    
    public GeneralBudgets GetActualItemForCFAnagraph(String projectId,
            Long cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GeneralBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id", cfAnagraphId));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("isOld", false));
        
        return (GeneralBudgets) criterion.uniqueResult();
    }
}
