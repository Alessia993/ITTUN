package com.infostroy.paamns.common.helpers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTab;
import org.richfaces.component.html.HtmlCalendar;

import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.ValidatorManager;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;

public class ValidatorHelper
{
    public static String                 errorStyle = "background-color: #FFE5E5;";
    
    protected transient final static Log log        = LogFactory
                                                            .getLog(ValidatorHelper.class);
    
    public static boolean validate(Object obj)
    {
        Set<ConstraintViolation<Object>> violations = ValidatorManager
                .getValidator().validate(obj);
        if (violations.size() != 0)
        {
            for (ConstraintViolation<Object> violation : violations)
            {
                log.error(violation.getRootBeanClass().toString()
                        + " message: " + violation.getMessage());
            }
            
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public static void markNotVAlid(UIComponent component,
            FacesMessage tmpMessage, FacesContext context,
            HashMap<String, Integer> tabs)
    {
        component.getAttributes().put("style", "background-color: #FFE5E5;");
        component.getAttributes().put("title", tmpMessage.getSummary());
        ValidatorBean.setStaticTabMessage(Utils
                .getString("validatorCheckAllFields"));
        while (component.getParent() != null)
        {
            component = component.getParent();
            if (component instanceof HtmlPanelTab)
            {
                component.getAttributes().put("style",
                        "background-color: #FFE5E5;");
                if (tabs != null)
                {
                    tabs.put(component.getClientId(context), 1);
                }
                break;
            }
        }
    }
    
    public static void markNotVAlid(UIComponent component, String tmpMessage,
            FacesContext context, HashMap<String, Integer> tabs)
    {
        //UIComponent comp = context.getViewRoot().findComponent()
        if (component instanceof HtmlCalendar)
        {
            component.getAttributes().put("inputStyle",
                    "background-color: #FFE5E5;");
            component.getAttributes().put("title", tmpMessage);
        }
        
        component.getAttributes().put("style", "background-color: #FFE5E5;");
        component.getAttributes().put("title", tmpMessage);
        ValidatorBean.setStaticTabMessage(Utils
                .getString("validatorCheckAllFields"));
        while (component.getParent() != null)
        {
            component = component.getParent();
            if (component instanceof HtmlPanelTab)
            {
                component.getAttributes().put("style",
                        "background-color: #FFE5E5;");
                if (tabs != null)
                {
                    tabs.put(component.getClientId(context), 1);
                }
                break;
            }
        }
    }
    
    public static void markVAlid(UIComponent component, FacesContext context,
            HashMap<String, Integer> tabs)
    {
        if (component instanceof HtmlCalendar)
        {
            component.getAttributes().put("inputStyle", "");
            component.getAttributes().put("title", "");
        }
        component.getAttributes().put("style", "");
        component.getAttributes().put("title", "");
        //ValidatorBean.setStaticTabMessage(Utils.getString("validatorCheckAllFields"));
        while (component.getParent() != null)
        {
            component = component.getParent();
            if (component instanceof HtmlPanelTab)
            {
                if (tabs != null
                        && tabs.get(component.getClientId(context)) == null)
                {
                    component.getAttributes().put("style", "");
                }
                break;
            }
        }
    }
}
