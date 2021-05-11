/**
 * 
 */
package com.infostroy.paamns.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */
public class NoDataConverter implements Converter
{
    /**
     * 
     * @param context
     * @param component
     * @param value
     * @return
     */
    public Object getAsObject(FacesContext context, UIComponent component,
            String value)
    {
        if ((value != null) && !value.isEmpty())
        {
            return value;
        }
        else
        {
            return Utils.getString("Resources", "converter.nodata", null);
        }
    }
    
    /**
     * 
     * @param context
     * @param component
     * @param value
     * @return
     */
    public String getAsString(FacesContext context, UIComponent component,
            Object value)
    {
        if ((value != null))
        {
            return value.toString();
        }
        else
        {
            return Utils.getString("Resources", "converter.nodata", null);
        }
    }
}
