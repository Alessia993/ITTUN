package com.infostroy.paamns.web.converters;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.infostroy.paamns.common.helpers.DateTime;

public class DateConverter implements Converter
{
    
    public Object getAsObject(FacesContext context, UIComponent component,
            String value)
    {
        if ((value != null) && (!value.isEmpty()) && (value.length() > 2))
            return DateTime.FromString(value);
        else
            return null;
    }
    
	public String getAsString(FacesContext context, UIComponent component,
			Object value)
	{
		if (value != null)
		{
			if (value instanceof Date)
			{
				return DateTime.ToString((Date) value);
			}
			else if (value.getClass().equals(String.class))
			{
				return (String) value;
			}

		}
		return null;
	}
}
