/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Ateco;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class AtecoBean extends LocalizableEnumerationBean<Ateco>
{
    @SuppressWarnings("unchecked")
    public List<Ateco> LoadRoots() throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Ateco.class);
        criterion.add(Restrictions.isNull("parent.id")).add(
                Restrictions.eq("level", 1));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Ateco> LoadByParent(Long parentId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Ateco.class);
        criterion.add(Restrictions.eq("parent.id", parentId));
        
        return criterion.list();
    }
    
    public Ateco LoadByCode(String code) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Ateco.class);
        criterion.add(Restrictions.eq("code", code));
        
        return (Ateco) criterion.uniqueResult();
    }
}
