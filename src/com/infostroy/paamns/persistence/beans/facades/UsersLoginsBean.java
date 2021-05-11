/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.UsersLogins;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class UsersLoginsBean extends PersistenceEntityBean<UsersLogins>
{
    public static void AddFailMessage(String userName)
    {
        AddMessage(userName, false, false);
    }
    
    public static void AddLogoutMessage(String userName)
    {
        AddMessage(userName, true, true);
    }
    
    public static void AddSuccessMessage(String userName)
    {
        AddMessage(userName, true, false);
    }
    
    @SuppressWarnings("unchecked")
    public List<UsersLogins> Load() throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(UsersLogins.class)
                .add(Restrictions.eq("deleted", false))
                .addOrder(Order.desc("createDate"));
        List<UsersLogins> list = criterion.list();
        return list;
    }
    
    protected transient final static Log log = LogFactory
                                                     .getLog(UsersLoginsBean.class);
    
    /**
     * @param userName
     * @param isLogged
     */
    private static void AddMessage(String userName, Boolean isLogged,
            Boolean isLogout)
    {
        UsersLogins item = new UsersLogins();
        item.setUserName(userName);
        item.setIsLogged(isLogged);
        item.setIsLogout(isLogout);
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        item.setIp(request.getRemoteAddr());
        try
        {
            BeansFactory.UsersLogins().Save(item);
        }
        catch(HibernateException e)
        {
            log.error(e);
            
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    public void Delete(long id)
    {
        Transaction tr = null;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM ");
            sb.append(UsersLogins.class.getSimpleName());
            sb.append(" AS ul");
            sb.append(" WHERE ul.id = :id");
            
            Query query = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sb.toString());
            query.setParameter("id", id);
            query.executeUpdate();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        tr.commit();
    }
    
    @Override
    public void DeleteAll()
    {
        Transaction tr = null;
        Query userUpdateQuery;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM ");
            sb.append(UsersLogins.class.getSimpleName());
            
            userUpdateQuery = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sb.toString());
            
            userUpdateQuery.executeUpdate();
        }
        catch(HibernateException e)
        {
            if (tr != null)
            {
                tr.rollback();
            }
            
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            if (tr != null)
            {
                tr.rollback();
            }
            
            log.error(e);
        }
        finally
        {
            if (tr != null && !tr.wasRolledBack() && tr.isActive())
            {
                tr.commit();
            }
        }
        
    }
}
