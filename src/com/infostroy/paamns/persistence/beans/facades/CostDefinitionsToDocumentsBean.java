package com.infostroy.paamns.persistence.beans.facades;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;

public class CostDefinitionsToDocumentsBean extends
        EntityBean<CostDefinitionsToDocuments>
{
    @SuppressWarnings("unchecked")
    public List<CostDefinitionsToDocuments> getByCostDefinition(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(CostDefinitionsToDocuments.class);
        
        crit.add(Restrictions.eq("costDefinition.id", id));
        return crit.list();
    }
        
    public Object getCountByCostDefinition(Long id, String field)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(CostDefinitionsToDocuments.class);
        
        crit.setProjection(Projections.countDistinct(field));
        crit.add(Restrictions.eq("costDefinition.id", id));
        crit.add(Restrictions.isNotNull("document"));
        return crit.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<CostDefinitionsToDocuments> getByDocuments(Collection<Long> ids)
    		throws PersistenceBeanException
    		{
    	Criteria crit = (Criteria) PersistenceSessionManager.getBean()
    			.getSession().createCriteria(CostDefinitionsToDocuments.class);
    	
    	crit.add(Restrictions.in("document.id", ids));
    	return crit.list();
    		}
    
    @SuppressWarnings("unchecked")
    public List<CostDefinitionsToDocuments> getByDocument(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(CostDefinitionsToDocuments.class);
        
        crit.add(Restrictions.eq("document.id", id));
        return crit.list();
    }
    
    public CostDefinitionsToDocuments getByDocumentAndCostDef(Long docId,
            Long costDefId) throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(CostDefinitionsToDocuments.class);
        
        crit.add(Restrictions.eq("document.id", docId));
        crit.add(Restrictions.eq("costDefinition.id", costDefId));
        return (CostDefinitionsToDocuments) crit.uniqueResult();
    }
}
