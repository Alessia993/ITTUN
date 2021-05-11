/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.proxy.ifc.LocalParams;

/**
 * 
 * @author Alexander Chelombitko 
 * @author Sergey Manoylo
 * InfoStroy Co., 2009,2010.
 * 
 */
@MappedSuperclass
public abstract class LocalizableEnumeration extends EntityProxy
{
    /**
     * 
     */
    @Column(name = "value_id")
    protected String _valueId;
    
    /**
     * @throws PersistenceBeanException 
     * @throws HibernateException 
     * 
     */
    public void setValue(String value) throws HibernateException,
            PersistenceBeanException
    {
        this._valueId = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this._valueId, value));
    }
    
    /**
     * @throws PersistenceBeanException 
     * @throws HibernateException 
     * 
     */
    public void setValue(String locale, String value)
            throws HibernateException, PersistenceBeanException
    {
        this._valueId = setLocalValue(getLocalParams(locale, this._valueId,
                value));
    }
    
    /**
     * 
     * @return
     * @throws PersistenceBeanException 
     * @throws HibernateException 
     */
    public String getValue() throws HibernateException,
            PersistenceBeanException
    {
        LocalParams lp = getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this._valueId,
                null);
        
        if (lp != null)
        {
            String sLocaleValue = getLocalValue(lp);
            
            if (sLocaleValue != null && !sLocaleValue.isEmpty())
            {
                return sLocaleValue;
            }
        }
        
        return this._valueId;
    }
    
    /**
     * 
     * @param locale
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public String getValue(String locale) throws HibernateException,
            PersistenceBeanException
    {
        LocalParams lp = getLocalParams(locale, this._valueId, null);
        
        if (lp != null)
        {
            String sLocaleValue = getLocalValue(lp);
            
            if (sLocaleValue != null && !sLocaleValue.isEmpty())
            {
                return sLocaleValue;
            }
        }
        
        return this._valueId;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        
        try
        {
            return getValue();
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
        
        return "";
    }
    
}
