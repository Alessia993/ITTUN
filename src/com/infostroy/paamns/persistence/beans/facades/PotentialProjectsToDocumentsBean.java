package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjectsToDocuments;

public class PotentialProjectsToDocumentsBean extends EntityBean<PotentialProjectsToDocuments>
{
	@SuppressWarnings("unchecked")
    public List<PotentialProjectsToDocuments> getByPotentialProject(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(PotentialProjectsToDocuments.class);
        
        crit.add(Restrictions.eq("potentialProjects.id", id));
        return crit.list();
    }
	
	@SuppressWarnings("unchecked")
	public List<PotentialProjectsToDocuments> getByDocument(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(PotentialProjectsToDocuments.class);
        
        crit.add(Restrictions.eq("document.id", id));
        return crit.list();
    }
	
	 public PotentialProjectsToDocuments getByDocumentAndPotProj(Long docId,
	            Long potProjId) throws PersistenceBeanException
	    {
	        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
	                .getSession().createCriteria(PotentialProjectsToDocuments.class);
	        
	        crit.add(Restrictions.eq("document.id", docId));
	        crit.add(Restrictions.eq("potentialProjects.id", potProjId));
	        return (PotentialProjectsToDocuments) crit.uniqueResult();
	    }
}
