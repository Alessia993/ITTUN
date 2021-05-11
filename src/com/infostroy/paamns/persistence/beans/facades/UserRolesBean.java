/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class UserRolesBean extends PersistenceEntityBean<UserRoles>
{
    protected transient final static Log log = LogFactory
                                                     .getLog(UserRolesBean.class);
    
    public static void DeleteAll(Users user)
    {
        try
        {
            List<UserRoles> list = GetByUser(String.valueOf(user.getId()));
            for (UserRoles item : list)
            {
                BeansFactory.UserRoles().DeleteInTransaction(item);
            }
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public static List<UserRoles> GetByUser(String userId)
            throws PersistenceBeanException
    {
        List<UserRoles> list = PersistenceSessionManager.getBean().getSession()
                .createCriteria(UserRoles.class)
                .add(Restrictions.like("user.id", Long.parseLong(userId)))
                .add(Restrictions.eq("deleted", false)).list();
        return list;
    }
}
