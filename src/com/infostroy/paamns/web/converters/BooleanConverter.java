/**
 * 
 */
package com.infostroy.paamns.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2011.
 *
 */
public class BooleanConverter implements Converter
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    public String getAsString(FacesContext context, UIComponent component,
            Object value)
    {
        if (value != null)
        {
            try
            {
                if ((Boolean) value)
                {
                    return Utils.getString("yes");
                }
                
                return Utils.getString("no");
            }
            catch(Exception e)
            {
                log.error(e);
                
            }
        }
        return Utils.getString("no");
    }
    
    /* (non-Javadoc)
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
            throws ConverterException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
