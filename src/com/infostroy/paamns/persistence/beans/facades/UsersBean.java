/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;

import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.DomainManager;
import com.infostroy.paamns.persistence.HibernateUtil;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2009.
 * 
 */
public class UsersBean extends PersistenceEntityBean<Users>
{
    // private static final String USER_SELECT =
    // "from User where businessUnit=:businessUnit and deleted=false";
    
    @SuppressWarnings("unchecked")
    public List<Users> loadByCriteria(Criterion[] criterions, Order[] orderList)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Users.class);
        if (criterions != null)
        {
            for (Criterion crit : criterions)
            {
                criteria.add(crit);
            }
        }
        
        if (orderList != null)
        {
            for (Order o : orderList)
            {
                criteria.addOrder(o);
            }
        }
        
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    public static Users GetByLogin(String login)
            throws PersistenceBeanException
    {
        try
        {
            List<Users> list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(Users.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("login", login))
                    .add(Restrictions.eq("active", true)).list();
            if (list.size() == 0)
            {
                throw new PersistenceBeanException(
                        "UserController.GetByLogin : User by login : " + login
                                + " isn't found...");
            }
            return list.get(0);
        }
        catch(JDBCConnectionException e)
        {
            HibernateUtil.cleanFactory();
            PersistenceSessionManager.getBean().closeSession();
            List<Users> list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(Users.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("login", login))
                    .add(Restrictions.eq("active", true)).list();
            if (list.size() == 0)
            {
                throw new PersistenceBeanException(
                        "UserController.GetByLogin : User by login : " + login
                                + " isn't found...");
            }
            return list.get(0);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsLoginExists(String login, Long id)
    {
        DomainManager.getCurrent().beginTransaction();
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(Users.class)
                    .add(Restrictions.like("login", login))
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.ne("id", id == null ? 0 : id)).list();
        }
        catch(Exception e)
        {
            Logger.getLogger(UsersBean.class).error(e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsMailExists(String mail, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(Users.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("mail", mail))
                    .add(Restrictions.ne("id", id == null ? 0 : id)).list();
        }
        catch(Exception e)
        {
            Logger.getLogger(UsersBean.class).error(e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    public Users getByMail(String mail, Session session)
            throws HibernateException, PersistenceBeanException
    {
        @SuppressWarnings("unchecked")
        List<Users> list = (List<Users>) session.createCriteria(Users.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.like("mail", mail)).list();
        
        return list==null || list.isEmpty()? null : list.get(0);
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsFiscalCodeExists(String mail, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(Users.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("fiscalCode", mail))
                    .add(Restrictions.ne("id", id == null ? 0 : id)).list();
        }
        catch(Exception e)
        {
            Logger.getLogger(UsersBean.class).error(e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Users> Load() throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass())
                .add(Restrictions.eq("deleted", false))
                .addOrder(Order.asc("surname"));
        List<Users> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<Users> getByRole(UserRoleTypes type) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT users.* ")
                .append("FROM users LEFT OUTER JOIN `user_roles` ON users.`id`=`user_roles`.`user_id` ")
                .append("LEFT OUTER JOIN roles ON roles.`id` = `user_roles`.`role_id` ")
                .append("WHERE roles.`code`= :type");
        SQLQuery query = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString());
        query.setString("type", type.getValue());
        query.addEntity(Users.class);
        return query.list();
    }
    
}
