/**
 * 
 */
package com.infostroy.paamns.persistence;

import java.util.Locale;

import org.apache.log4j.Logger;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class LocaleSession
{
    /**
     * 
     */
    @SuppressWarnings("unused")
    private static final Logger log         = Logger.getLogger(LocaleSession.class);
    
    /**
     * 
     */
    private Locale              _currLocale = Locale.ENGLISH;                       ///  Locale.getDefault();
                                                                                     
    /**
     * 
     */
    public LocaleSession()
    {
    }
    
    /**
     * 
     * @return
     */
    public static LocaleSession getInstance()
    {
        return new LocaleSession();
    }
    
    /**
     * 
     * @return
     */
    public Locale getCurrLocale()
    {
        try
        {
            return SessionManager.getInstance().getSessionBean().getCurLocale();
        }
        catch(Exception e)
        {
            //log.warn(e);
        }
        return _currLocale;
    }
    
    /**
     * 
     * @param locale
     */
    public void setCurrLocale(Locale locale)
    {
        try
        {
            SessionManager.getInstance().getSessionBean().setCurLocale(locale);
        }
        catch(Exception e)
        {
            //log.warn(e);
        }
        _currLocale = locale;
    }
    
    /**
     * 
     * @param strLocale
     * @return
     */
    public static Locale getLocaleFromString(String strLocale)
    {
        if (Locale.ENGLISH.toString().equals(strLocale) == true)
        {
            return Locale.ENGLISH;
        }
        if (Locale.ITALIAN.toString().equals(strLocale) == true)
        {
            return Locale.ITALIAN;
        }
        if (Locale.FRENCH.toString().equals(strLocale) == true)
        {
            return Locale.FRENCH;
        }
        if (Locale.US.toString().equals(strLocale) == true)
        {
            return Locale.US;
        }
        if (Locale.ITALY.toString().equals(strLocale) == true)
        {
            return Locale.ITALY;
        }
        if (Locale.FRANCE.toString().equals(strLocale) == true)
        {
            return Locale.FRANCE;
        }
        return null;
    }
    
}
