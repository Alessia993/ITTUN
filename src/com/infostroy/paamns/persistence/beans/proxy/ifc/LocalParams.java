/**
 * 
 */
package com.infostroy.paamns.persistence.beans.proxy.ifc;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class LocalParams
{
    private String _localId;
    
    private String _entityId;
    
    private String _locale;
    
    private String _value;
    
    /**
     * 
     * @param localId
     */
    public void setLocalId(String localId)
    {
        _localId = localId;
    }
    
    /**
     * 
     * @return
     */
    public String getLocalId()
    {
        return _localId;
    }
    
    /**
     * 
     * @param entityId
     */
    public void setEntityId(String entityId)
    {
        _entityId = entityId;
    }
    
    /**
     * 
     * @return
     */
    public String getEntityId()
    {
        return _entityId;
    }
    
    /**
     * 
     * @param locale
     */
    public void setLocale(String locale)
    {
        _locale = locale;
    }
    
    /**
     * 
     * @return
     */
    public String getLocale()
    {
        return _locale;
    }
    
    /**
     * 
     * @param value
     */
    public void setValue(String value)
    {
        _value = value;
    }
    
    /**
     * 
     * @return
     */
    public String getValue()
    {
        return _value;
    }
    
}

/// public String Set(String localId, String entityId, String locale, String value) throws HibernateException, PersistentControllerException;
