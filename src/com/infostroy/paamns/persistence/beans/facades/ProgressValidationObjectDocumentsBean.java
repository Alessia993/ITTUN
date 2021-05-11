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
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjectDocuments;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProgressValidationObjectDocumentsBean extends
        PersistenceEntityBean<ProgressValidationObjectDocuments>
{
    @SuppressWarnings("unchecked")
    public List<ProgressValidationObjectDocuments> LoadByProgressValidationFlow(
            Long progressValidationObjectId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProgressValidationObjectDocuments.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("progressValidationObject.id",
                progressValidationObjectId));
        
        return criterion.list();
    }
    
    public ProgressValidationObjectDocuments LoadByProgressValidationFlowAndDocument(
            Long progressValidationObjectId, Long documentId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProgressValidationObjectDocuments.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("progressValidationObject.id",
                progressValidationObjectId));
        criterion.add(Restrictions.eq("document.id", documentId));
        
        return (ProgressValidationObjectDocuments) criterion.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<ProgressValidationObjectDocuments> LoadByDocument(
            Long documentId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProgressValidationObjectDocuments.class);
        
        criterion.add(Restrictions.eq("document.id", documentId));
        criterion.add(Restrictions.eq("deleted", false));
        return criterion.list();
    }
}
