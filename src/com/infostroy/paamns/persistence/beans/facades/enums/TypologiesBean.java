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
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Typologies;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class TypologiesBean extends LocalizableEnumerationBean<Typologies>
{
    @SuppressWarnings("unchecked")
    public List<Typologies> Load(Boolean forAsse5)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Typologies.class)
                .add(Restrictions.eq("deleted", false));
        
        if (forAsse5 != null)
        {
            crit.add(Restrictions.eq("forAsse5", forAsse5));
        }
        
        return crit.list();
    }
}
