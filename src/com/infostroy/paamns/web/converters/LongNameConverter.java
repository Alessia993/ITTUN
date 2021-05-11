/**
 * 
 */
package com.infostroy.paamns.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2011.
 *
 */
public class LongNameConverter implements Converter
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    public String getAsString(FacesContext context, UIComponent component,
            Object value)
    {
        if (value != null && !String.valueOf(value).isEmpty())
        {
            if (component instanceof HtmlOutputText)
            {
                return checkLongName(String.valueOf(value),
                        (HtmlOutputText) component);
            }
            else
            {
                return checkLongName(String.valueOf(value));
            }
        }
        return null;
    }
    
    private String checkLongName(String str, HtmlOutputText component)
    {
        component.setTitle(str);
        if ((str.indexOf(' ') == -1 || str.indexOf(' ') > 20)
                && str.length() > 20)
        {
            
            return str.substring(0, 15) + "..."
                    + str.substring(str.length() - 5, str.length());
        }
        
        return str;
    }
    
    private String checkLongName(String str)
    {
        if ((str.indexOf(' ') == -1 || str.indexOf(' ') > 20)
                && str.length() > 20)
        {
            
            return str.substring(0, 15) + "..."
                    + str.substring(str.length() - 5, str.length());
        }
        return str;
    }
    
    /* (non-Javadoc)
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
            throws ConverterException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
