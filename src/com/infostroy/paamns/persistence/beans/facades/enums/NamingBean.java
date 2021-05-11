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
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Naming;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class NamingBean extends LocalizableEnumerationBean<Naming>
{
    @SuppressWarnings("unchecked")
    public List<Naming> LoadRoots() throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Naming.class);
        criterion.add(Restrictions.isNull("parent.id")).add(
                Restrictions.eq("level", 1));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Naming> LoadByParent(Long parentId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Naming.class);
        criterion.add(Restrictions.eq("parent.id", parentId));
        
        return criterion.list();
    }
}
