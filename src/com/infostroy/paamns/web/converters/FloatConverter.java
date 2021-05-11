/**
 * 
 */
package com.infostroy.paamns.web.converters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class FloatConverter implements Converter
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    public Object getAsObject(FacesContext context, UIComponent component,
            String value)
    {
        if ((value != null) && (!value.isEmpty()))
        {
            try
            {
                value = value.replaceAll(",", ".");
                DecimalFormat dec = new DecimalFormat("###.##",
                        DecimalFormatSymbols.getInstance(Locale.US));
                return dec.format(Double.parseDouble(value));
            }
            catch(Exception e)
            {
                log.error(e);
                return null;
            }
        }
        else
            return null;
    }
    
    public String getAsString(FacesContext context, UIComponent component,
            Object value)
    {
        if (value != null && !String.valueOf(value).isEmpty())
        {
            try
            {
                DecimalFormat dec = new DecimalFormat("###.##");
                
                return String.valueOf(dec.format(Double.parseDouble(String
                        .valueOf(value))));
            }
            catch(Exception e)
            {
                log.error(e);
                
            }
        }
        return null;
    }
}
