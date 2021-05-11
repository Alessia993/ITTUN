/**
 * 
 */
package com.infostroy.paamns.persistence;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public final class PersistenceSessionManager extends GenericManager
{
    public static String                     HibernateSessionAttribute = "HibernateSessionAttribute";
    
    /**
     * 
     */
    private static PersistenceSessionManager instance                  = new PersistenceSessionManager();
    
    /**
     * 
     */
    private PersistenceSession               instanceBean              = null;
    
    /**
     * 
     */
    private Object                           syncObject                = new Object();
    
    /**
     * 
     */
    private PersistenceSessionManager()
    {
    }
    
    /**
     * 
     * @return
     */
    public static PersistenceSessionManager getInstance()
    {
        return instance;
    }
    
    /**
     * 
     * @return
     */
    public PersistenceSession getInstanceBean()
    {
        synchronized (syncObject)
        {
            if (instanceBean == null)
            {
                instanceBean = new PersistenceSession();
            }
            syncObject.notifyAll();
        }
        return instanceBean;
    }
    
    /**
     * 
     * @return
     */
    public PersistenceSession bean()
    {
        if (FacesContext.getCurrentInstance() != null)
        {
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            
            if (request.getSession().getAttribute(HibernateSessionAttribute) == null)
            {
                request.getSession().setAttribute(HibernateSessionAttribute,
                        new PersistenceSession());
                
            }
            
            instanceBean = (PersistenceSession) request.getSession()
                    .getAttribute(HibernateSessionAttribute);
            
            return instanceBean;
        }
        else
        {
            if (this.factory != null)
            {
                instanceBean = (PersistenceSession) this.factory.getBean(
                        "PersistenceSession", PersistenceSession.class);
                return instanceBean;
            }
            else
            {
                return getInstanceBean();
            }
        }
        
    }
    
    /**
     * 
     * @return
     */
    public static PersistenceSession getBean()
    {
        return instance.bean();
    }
}
