/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities;

import java.util.Locale;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.proxy.facade.ProxyFacade;
import com.infostroy.paamns.persistence.beans.proxy.ifc.LocalParams;
import com.infostroy.paamns.web.beans.LocalValuesBean;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public abstract class EntityProxy extends PersistentEntity
{
    /**
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public String getLocalValue(LocalParams localParams)
    {
        try
        {
            if (LocalValuesBean.localValues != null
                    && !LocalValuesBean.localValues.isEmpty())
            {
                return LocalValuesBean.get(localParams);
            }
            else
            {
                return ProxyFacade.getInstance().getProxyIfc()
                        .load(localParams);
            }
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch blocksetValue
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public String setLocalValue(LocalParams localParams)
    {
        try
        {
            return ProxyFacade.getInstance().getProxyIfc().set(localParams);
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
        return null;
    }
    
    /**
     * 
     * @param entityObj
     * @param locale
     * @param localId
     * @return
     */
    protected LocalParams getLocalParams(String locale, String localValueId,
            String value)
    {
        LocalParams lp = new LocalParams();
        lp.setEntityId(this.getClass().toString());
        if (localValueId != null)
        {
            lp.setLocalId(localValueId);
        }
        if (value != null)
        {
            lp.setValue(value);
        }
        if (locale != null)
        {
            lp.setLocale(locale);
        }
        else
        {
            lp.setLocale(Locale.getDefault().toString());
        }
        
        return lp;
    }
    
    /**
     * 
     * @param entityObj
     * @param locale
     * @param localId
     * @return
     */
    protected LocalParams getLocalParams(Class<?> clazz, String locale,
            String localValueId, String value)
    {
        LocalParams lp = new LocalParams();
        if (clazz != null)
        {
            lp.setEntityId(clazz.toString());
        }
        else
        {
            lp.setEntityId(this.getClass().toString());
        }
        if (localValueId != null)
        {
            lp.setLocalId(localValueId);
        }
        if (value != null)
        {
            lp.setValue(value);
        }
        if (locale != null)
        {
            lp.setLocale(locale);
        }
        else
        {
            lp.setLocale(Locale.getDefault().toString());
        }
        
        return lp;
    }
    
    /**
     * 
     * @param entityObj
     * @param locale
     * @param localId
     * @return
     */
    protected static LocalParams getLocalParams(Object entityObj,
            String locale, String localValueId, String value)
    {
        LocalParams lp = new LocalParams();
        
        if (entityObj != null)
        {
            lp.setEntityId(entityObj.getClass().toString());
        }
        if (localValueId != null)
        {
            lp.setLocalId(localValueId);
        }
        if (value != null)
        {
            lp.setValue(value);
        }
        if (locale != null)
        {
            lp.setLocale(locale);
        }
        else
        {
            lp.setLocale(Locale.getDefault().toString());
        }
        return lp;
    }
    
}
