/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.HtmlAjaxCommandButton;
import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PageProcessorHelper
{
    protected transient final static Log log = LogFactory
                                                     .getLog(PageProcessorHelper.class);
    
    public static void disable()
    {
        disableChildren(FacesContext.getCurrentInstance().getViewRoot()
                .getChildren());
    }
    
    private static void disableChildren(List<UIComponent> list)
    {
        for (UIComponent component : list)
        {
            if (component instanceof HtmlInputText)
            {
                try
                {
                    component.getAttributes().put("readonly", true);
                }
                catch(Exception e)
                {
                    log.error(e);
                }
            }
            else if (component instanceof HtmlInputTextarea)
            {
                try
                {
                    component.getAttributes().put("readonly", true);
                }
                catch(Exception e)
                {
                    log.error(e);
                }
            }
            else if (component instanceof HtmlSelectOneMenu)
            {
                try
                {
                    component.getAttributes().put("readonly", true);
                    
                }
                catch(Exception e)
                {
                    
                    log.error(e);
                    try
                    {
                        component.getAttributes().put("displayValueOnly", true);
                    }
                    catch(Exception e1)
                    {
                        log.error(e);
                    }
                }
            }
            else if (component instanceof HtmlAjaxCommandButton)
            {
                try
                {
                    component.getAttributes().put("rendered", false);
                }
                catch(Exception e)
                {
                    log.error(e);
                }
            }
            else if (component instanceof HtmlAjaxCommandLink)
            {
                try
                {
                    component.getAttributes().put("rendered", false);
                }
                catch(Exception e)
                {
                    log.error(e);
                }
            }
            
            disableChildren(component.getChildren());
        }
    }
}
