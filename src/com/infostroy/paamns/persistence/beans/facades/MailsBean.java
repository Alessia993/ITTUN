/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class MailsBean extends PersistenceEntityBean<Mails>
{
    @SuppressWarnings("unchecked")
    public List<Mails> Load() throws HibernateException,
            PersistenceBeanException
    {
        List<Mails> list = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Mails.class)
                .add(Restrictions.eq("deleted", false))
                .addOrder(Order.desc("createDate")).list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<Mails> Load(boolean success) throws HibernateException,
            PersistenceBeanException
    {
        List<Mails> list = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Mails.class)
                .add(Restrictions.eq("deleted", false))
                .addOrder(Order.desc("createDate"))
                .add(Restrictions.eq("success", success)).list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<Mails> Load(Users user) throws HibernateException,
            PersistenceBeanException
    {
        List<Mails> list = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Mails.class)
                .add(Restrictions.eq("deleted", false))
                .addOrder(Order.desc("createDate"))
                .add(Restrictions.eq("user.id", user.getId())).list();
        return list;
    }
}
