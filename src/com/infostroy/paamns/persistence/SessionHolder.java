/**
 * 
 */
package com.infostroy.paamns.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public class SessionHolder
{
    private final Log            log       = LogFactory
                                                   .getLog(SessionHolder.class);
    
    private Session              session;
    
    private int                  openCount = 0;
    
    private static SessionHolder instance;
    
    private Object               monitor   = new Object();
    
    public synchronized static SessionHolder getInstance()
    {
        if (instance == null)
        {
            instance = new SessionHolder();
        }
        
        return instance;
    }
    
    public void openSession()
    {
        synchronized (SessionHolder.class)
        {
            if (session == null && openCount == 0)
            {
                try
                {
                    session = HibernateUtil.getSessionFactory().openSession();

                    session.setFlushMode(FlushMode.COMMIT);
                    session.setCacheMode(CacheMode.IGNORE);
                    session.flush();
                    session.clear();
                }
                catch(Exception e)
                {
                    log.error(e);
                }
            }
            
            openCount++;
        }
    }
    
    public void closeSession()
    {
        synchronized (SessionHolder.class)
        {
            if (openCount > 0)
            {
                openCount--;
            }
            else
            {
                log.error("ERROR SESSION HOLDING");
            }
            
            if (openCount == 0 && session != null)
            {
                session.close();
                session = null;
                
            }
        }
    }
    
    public synchronized void save(Entity ent) throws Exception
    {
        synchronized (SessionHolder.class)
        {
            Transaction tr = null;
            try
            {
                if (this.getSession().getTransaction().isActive())
                {
                    tr = this.getSession().getTransaction();
                }
                else
                {
                    tr = this.getSession().beginTransaction();
                }
                
                this.getSession().saveOrUpdate(ent);
            }
            catch(Exception e)
            {
                if (tr != null && tr.isActive())
                {
                    tr.rollback();
                }
                
                throw e;
            }
            finally
            {
                if (tr != null && tr.isActive() && !tr.wasRolledBack())
                {
                    tr.commit();
                }
                tr = null;
            }
        }
    }
    
    public synchronized void remove(Entity ent) throws Exception
    {
        synchronized (SessionHolder.class)
        {
            Transaction tr = null;
            try
            {
                if (this.getSession().getTransaction().isActive())
                {
                    tr = this.getSession().getTransaction();
                }
                else
                {
                    tr = this.getSession().beginTransaction();
                }
                
                this.getSession().delete(ent);
            }
            catch(Exception e)
            {
                if (tr != null && tr.isActive())
                {
                    tr.rollback();
                }
                
                throw e;
            }
            finally
            {
                if (tr != null && tr.isActive() && !tr.wasRolledBack())
                {
                    tr.commit();
                }
                tr = null;
            }
        }
    }
    
    public Session getSession() throws Exception
    {
        if (session != null)
        {
            synchronized (monitor)
            {
                return session;
            }
        }
        else
        {
            throw new Exception("Session was not opened");
        }
    }
}
