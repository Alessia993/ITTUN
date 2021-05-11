/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ControllerUserAnagraphBean extends
        PersistenceEntityBean<ControllerUserAnagraph>
{
    @SuppressWarnings("unchecked")
    public static boolean IsInseeNumber(String code, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(ControllerUserAnagraph.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("inseeNumber", code))
                    .add(Restrictions.ne("id", id.equals(null) ? 0 : id))
                    .list();
        }
        catch(Exception e)
        {
            Logger.getLogger(ControllerUserAnagraphBean.class).error(
                    e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsTvaNumber(String code, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(ControllerUserAnagraph.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("tvaNumber", code))
                    .add(Restrictions.ne("id", id.equals(null) ? 0 : id))
                    .list();
        }
        catch(Exception e)
        {
            Logger.getLogger(ControllerUserAnagraphBean.class).error(
                    e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsVatNumber(String code, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(ControllerUserAnagraph.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("vatNumber", code))
                    .add(Restrictions.ne("id", id.equals(null) ? 0 : id))
                    .list();
        }
        catch(Exception e)
        {
            Logger.getLogger(ControllerUserAnagraphBean.class).error(
                    e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public static boolean IsFiscalCode(String code, Long id)
    {
        List<Users> list = new ArrayList<Users>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(ControllerUserAnagraph.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("fiscalCode", code))
                    .add(Restrictions.ne("id", id.equals(null) ? 0 : id))
                    .list();
        }
        catch(Exception e)
        {
            Logger.getLogger(ControllerUserAnagraphBean.class).error(
                    e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public List<ControllerUserAnagraph> GetUsersByRole(UserRoleTypes type,
            Long countryId) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ControllerUserAnagraph.class);
        criterion.add(Restrictions.in("user.id",
                this.GetUsersIndicesByType(type)));
        criterion.add(Restrictions.eq("deleted", false));
        
        if (countryId != null)
        {
            criterion.add(Restrictions.eq("country.id", countryId));
        }
        
        return criterion.list();
    }
    
    public ControllerUserAnagraph GetByUser(Long userId)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ControllerUserAnagraph.class);
        criterion.add(Restrictions.eq("user.id", userId));
        criterion.add(Restrictions.eq("deleted", false));
        return (ControllerUserAnagraph) criterion.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<Long> GetUsersIndicesByType(UserRoleTypes type)
            throws HibernateException, PersistenceBeanException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ur.user.id ");
        sb.append("FROM UserRoles ur ");
        sb.append("WHERE ur.deleted = false AND ");
        sb.append("ur.role.id IN (SELECT r.id FROM Roles r WHERE r.code = :type)");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("type", type.getValue());
        return query.list();
    }
}
