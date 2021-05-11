/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class NotRegularTypesBean extends
        LocalizableEnumerationBean<NotRegularTypes>
{
    @SuppressWarnings("unchecked")
    public List<NotRegularTypes> LoadParent() throws HibernateException,
            PersistenceBeanException
    {
        List<NotRegularTypes> list = PersistenceSessionManager.getBean()
                .getSession().createCriteria(NotRegularTypes.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.isNull("parent")).list();
        return list;
        
    }
}
