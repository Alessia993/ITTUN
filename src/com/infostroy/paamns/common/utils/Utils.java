package com.infostroy.paamns.common.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.persistence.LocaleSessionManager;

public class Utils
{
    protected transient final static Log log = LogFactory.getLog(Utils.class);
    
    /**
     * 
     * @param bundleName
     * @param resourceId
     * @param params
     * @return
     */
    public static FacesMessage getMessage(String bundleName, String resourceId,
            Object[] params)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        String appBundle = app.getMessageBundle();
        Locale locale = getLocale();
        ClassLoader loader = getClassLoader();
        String summary = getString(appBundle, bundleName, resourceId, locale,
                loader, params);
        if (summary == null)
            summary = "???" + resourceId + "???";
        String detail = getString(appBundle, bundleName,
                resourceId + "_detail", locale, loader, params);
        return new FacesMessage(summary, detail);
    }
    
    /**
     * 
     * @param bundle
     * @param resourceId
     * @param params
     * @return
     */
    public static String getString(String bundle, String resourceId,
            Object[] params)
    {
        String appBundle = FacesContext.getCurrentInstance().getApplication()
                .getMessageBundle();
        Locale locale = getLocale();
        ClassLoader loader = getClassLoader();
        return getString(appBundle, bundle, resourceId, locale, loader, params);
    }
    
    public static String getString(String resourceId)
    {
        String str = null;
        try
        {
            str = getString("Resources", resourceId, null);
        }
        catch(Exception e)
        {
            log.error(e);
        }
        
        if (str != null && !str.isEmpty())
        {
            return str;
        }
        else
        {
            return getString("Resources", "converter.nodata", null);
        }
    }
    
    /**
     * 
     * @param bundle1
     * @param bundle2
     * @param resourceId
     * @param locale
     * @param loader
     * @param params
     * @return
     */
    public static String getString(String bundle1, String bundle2,
            String resourceId, Locale locale, ClassLoader loader,
            Object[] params)
    {
        String resource = null;
        ResourceBundle bundle;
        
        if (bundle1 != null)
        {
            bundle = ResourceBundle.getBundle(bundle1, locale, loader);
            if (bundle != null)
                try
                {
                    resource = bundle.getString(resourceId);
                }
                catch(MissingResourceException ex)
                {
                    log.error(ex);
                }
        }
        
        if (resource == null)
        {
            bundle = ResourceBundle.getBundle(bundle2, locale, loader);
            if (bundle != null)
                try
                {
                    resource = bundle.getString(resourceId);
                }
                catch(MissingResourceException ex)
                {
                    log.error(ex);
                }
        }
        
        if (resource == null)
            return null; // no match
        if (params == null)
            return resource;
        
        MessageFormat formatter = new MessageFormat(resource, locale);
        return formatter.format(params);
    }
    
    /**
     * 
     * @return
     */
    public static Locale getLocale()
    {
        return LocaleSessionManager.getInstance().getInstanceBean()
                .getCurrLocale();
    }
    
    /**
     */
    public static ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = ClassLoader.getSystemClassLoader();
        return loader;
    }
}
