/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalCheckToDocuments;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class LocalCheckToDocumentsBean extends
        EntityBean<LocalCheckToDocuments>
{
    @SuppressWarnings("unchecked")
    public List<LocalCheckToDocuments> getByLocalCheck(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(LocalCheckToDocuments.class);
        
        crit.add(Restrictions.eq("localCheck.id", id));
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<LocalCheckToDocuments> getByDocument(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(LocalCheckToDocuments.class);
        
        crit.add(Restrictions.eq("document.id", id));
        return crit.list();
    }
    
    public LocalCheckToDocuments getByDocumentAndLocalCheck(Long docId,
            Long localCheckId) throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(LocalCheckToDocuments.class);
        
        crit.add(Restrictions.eq("document.id", docId));
        crit.add(Restrictions.eq("localCheck.id", localCheckId));
        return (LocalCheckToDocuments) crit.uniqueResult();
    }
}
