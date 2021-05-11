/**
 * 
 */
package com.infostroy.paamns.persistence;

import org.hibernate.Session;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2009.
 *
 */
public class DomainManager
{
    public Session session;
    
    public static Session getCurrent()
    {
        if (HibernateUtil.getSessionFactory().getCurrentSession() == null)
        {
            HibernateUtil.getSessionFactory().openSession();
        }
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
