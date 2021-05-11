/**
 * 
 */
package com.infostroy.paamns.web.beans;

import java.util.Locale;

import com.infostroy.paamns.persistence.LocaleSession;
import com.infostroy.paamns.persistence.LocaleSessionManager;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class LocalizeBean
{
    public LocalizeBean()
    {
        doSetItalianLocal();
    }
    
    /**
     * 
     */
    public void doSetEnglishLocal()
    {
        LocaleSessionManager.getInstance().getInstanceBean()
                .setCurrLocale(Locale.ENGLISH);
        //SessionManager.getInstance().getSessionBean().setCurLocale(Locale.ENGLISH);
    }
    
    /**
     * 
     */
    public void doSetItalianLocal()
    {
        LocaleSessionManager.getInstance().getInstanceBean()
                .setCurrLocale(Locale.ITALIAN);
        //SessionManager.getInstance().getSessionBean().setCurLocale(Locale.ITALIAN);
    }
    
    /**
     * 
     */
    public void doSetFrenchLocal()
    {
        LocaleSessionManager.getInstance().getInstanceBean()
                .setCurrLocale(Locale.FRENCH);
        //SessionManager.getInstance().getSessionBean().setCurLocale(Locale.FRENCH);
    }
    
    /**
     * 
     * @return
     */
    public String getCurrentLocale()
    {    	
        return LocaleSessionManager.getInstance().getInstanceBean()
                .getCurrLocale().toString();
        //return SessionManager.getInstance().getSessionBean().getCurLocale().toString();
    }
    
    /**
     * 
     * @param locale
     */
    public void setCurrentLocale(String strLocale)
    {
        LocaleSessionManager.getInstance().getInstanceBean()
                .setCurrLocale(LocaleSession.getLocaleFromString(strLocale));
        //SessionManager.getInstance().getSessionBean().setCurLocale(new Locale(strLocale));
        //return;
    }
    
    public Locale getCurrentLocaleValue(){
    	if(LocaleSessionManager.getInstance().getInstanceBean()
                .getCurrLocale().equals(Locale.FRENCH)){
    		return Locale.ENGLISH;
    	}
    	
    	return LocaleSessionManager.getInstance().getInstanceBean()
                .getCurrLocale();
    }
    
}
