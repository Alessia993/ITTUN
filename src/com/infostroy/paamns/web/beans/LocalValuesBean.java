/**
 * 
 */
package com.infostroy.paamns.web.beans;

import java.util.HashMap;
import java.util.List;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.localization.LocalValue;
import com.infostroy.paamns.persistence.beans.proxy.ifc.LocalParams;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class LocalValuesBean
{
    private String                        value;
    
    private String                        value2;
    
    public static HashMap<String, String> localValues;
    
    public LocalValuesBean()
    {
        //HibernateUtil.cleanFactory();
        value = "";
        localValues = new HashMap<String, String>();
        
        try
        {
            List<LocalValue> list = BeansFactory.LocalValueBean().Load();
            for (LocalValue item : list)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(item.getLocale());
                sb.append("-");
                sb.append(item.getEntityId());
                sb.append("-");
                sb.append(item.getLocalId());
                localValues.put(sb.toString(), item.getValue());
            }
        }
        catch(PersistenceBeanException e)
        {
            
        }
        
    }
    
    public static String get(LocalParams localParams)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(localParams.getLocale());
        sb.append("-");
        sb.append(localParams.getEntityId());
        sb.append("-");
        sb.append(localParams.getLocalId());
        return localValues.get(sb.toString());
    }
    
    /**
     * Sets value
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * @return value the value
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * Sets value2
     * @param value2 the value2 to set
     */
    public void setValue2(String value2)
    {
        this.value2 = value2;
    }
    
    /**
     * Gets value2
     * @return value2 the value2
     */
    public String getValue2()
    {
        //PageProcessorHelper.disable();
        return value2;
    }
}
