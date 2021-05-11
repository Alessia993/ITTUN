/**
 * 
 */
package com.infostroy.paamns.persistence.beans.proxy.impl;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.facades.localization.LocalValueBean;
import com.infostroy.paamns.persistence.beans.facades.localization.LocalValueBean.LocalValueBeanParams;
import com.infostroy.paamns.persistence.beans.proxy.ifc.LocalParams;
import com.infostroy.paamns.persistence.beans.proxy.ifc.ProxyInterface;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class ProxyImpl implements ProxyInterface
{
    
    /**
     * 
     */
    public String load(LocalParams localParams) throws HibernateException,
            PersistenceBeanException
    {
        LocalValueBean lvbean = BeansFactory.LocalValueBean();
        
        LocalValueBeanParams lvbeanParams = lvbean.createLocalValueBeanParams();
        
        lvbeanParams.setEntityId(localParams.getEntityId());
        lvbeanParams.setLocale(localParams.getLocale());
        lvbeanParams.setLocalId(localParams.getLocalId());
        lvbeanParams.setValue(localParams.getValue());
        
        return BeansFactory.LocalValueBean().load(lvbeanParams);
    }
    
    /**
     * 
     */
    public String set(LocalParams localParams) throws HibernateException,
            PersistenceBeanException
    {
        LocalValueBean lvbean = BeansFactory.LocalValueBean();
        
        LocalValueBeanParams lvbeanParams = lvbean.createLocalValueBeanParams();
        
        lvbeanParams.setEntityId(localParams.getEntityId());
        lvbeanParams.setLocale(localParams.getLocale());
        lvbeanParams.setLocalId(localParams.getLocalId());
        lvbeanParams.setValue(localParams.getValue());
        
        return BeansFactory.LocalValueBean().set(lvbeanParams);
    }
    
}
