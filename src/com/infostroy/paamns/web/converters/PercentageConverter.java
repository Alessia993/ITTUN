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

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2011.
 *
 */
public class PercentageConverter implements Converter
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    public String getAsString(FacesContext context, UIComponent component,
            Object value)
    {
        if (value != null && !String.valueOf(value).isEmpty())
        {
            try
            {
                return BaseConverter.convertToPercentageString(value);
            }
            catch(Exception e)
            {
                log.error(e);                
            }
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public Object getAsObject(FacesContext context, UIComponent componnt,
            String value) throws ConverterException
    {
        return null;
    }
}
