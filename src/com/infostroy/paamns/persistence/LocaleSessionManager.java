/**
 * 
 */
package com.infostroy.paamns.persistence;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class LocaleSessionManager extends GenericManager
{
    /**
     * 
     */
    private static LocaleSessionManager _instance     = new LocaleSessionManager();
    
    /**
     * 
     */
    private LocaleSession               _instanceBean = null;
    
    /**
     * 
     */
    private Object                      _syncObject   = new Object();
    
    /**
     * 
     */
    private LocaleSessionManager()
    {
        
    }
    
    /**
     * 
     * @return
     */
    public static LocaleSessionManager getInstance()
    {
        return _instance;
    }
    
    /**
     * 
     * @return
     */
    public LocaleSession getInstanceBean()
    {
        synchronized (_syncObject)
        {
            if (_instanceBean == null)
            {
                _instanceBean = new LocaleSession();
            }
            _syncObject.notifyAll();
        }
        return _instanceBean;
    }
    
    /**
     * 
     * @return
     */
    public LocaleSession bean()
    {
        if (this.factory != null)
        {
            _instanceBean = (LocaleSession) this.factory.getBean(
                    "LocaleSession", LocaleSession.class);
            return _instanceBean;
        }
        else
        {
            return getInstanceBean();
        }
    }
    
    /**
     * 
     * @return
     */
    public static LocaleSession getBean()
    {
        return _instance.bean();
    }
}
