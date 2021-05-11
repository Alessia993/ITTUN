/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.UserActivityLog;

/**
 *
 * @author Zrazhevskiy Vladimir
 * InfoStroy Co., 2011.
 *
 */
public class UserActivityLogBean extends PersistenceEntityBean<UserActivityLog>
{
    public UserActivityLog getByUserAndAction(Long userId, Long actionCode)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(UserActivityLog.class);
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("actionCode", actionCode));
        
        return (UserActivityLog) criteria.uniqueResult();
    }
}
