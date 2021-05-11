/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CUP;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class CUPBean extends LocalizableEnumerationBean<CUP>
{
    @SuppressWarnings("unchecked")
    public List<CUP> GetEntitiesByLevel(int level) throws HibernateException,
            PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CUP.class);
        criteria.add(Restrictions.eq("level", level));
        
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<CUP> GetEntitiesByParent(Long parentId)
            throws HibernateException, PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CUP.class);
        if (parentId != null)
        {
            criteria.add(Restrictions.eq("parent.id", parentId));
        }
        
        return criteria.list();
    }
}
