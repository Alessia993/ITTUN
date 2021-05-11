package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressToDocument;

public class PhisicalProgressToDocumentBean extends EntityBean<PhisicalProgressToDocument>{

	
	@SuppressWarnings("unchecked")
    public List<PhisicalProgressToDocument> getByProjectIndicators(Long id)
            throws PersistenceBeanException
    {
		Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(PhisicalProgressToDocument.class);
        
        crit.add(Restrictions.eq("projectIndicators.id", id));
        return crit.list();
    }
	
	 public PhisicalProgressToDocument getByDocumentAndPhisProg(Long docId,
	            Long projectIndicators) throws PersistenceBeanException
	    {
	        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
	                .getSession().createCriteria(PhisicalProgressToDocument.class);
	        
	        crit.add(Restrictions.eq("document.id", docId));
	        crit.add(Restrictions.eq("projectIndicators.id", projectIndicators));
	        return (PhisicalProgressToDocument) crit.uniqueResult();
	    }
	 
	 @SuppressWarnings("unchecked")
		public List<PhisicalProgressToDocument> getByDocument(Long id)
	            throws PersistenceBeanException
	    {
	        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
	                .getSession().createCriteria(PhisicalProgressToDocument.class);
	        
	        crit.add(Restrictions.eq("document.id", id));
	        return crit.list();
	    }
		
}
