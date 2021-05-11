/**
 * 
 */
package com.infostroy.paamns.web.services.base;

import com.infostroy.paamns.web.services.MailSenderService;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */

public class ServiceHolder
{
    private MailSenderService    mailSenderService;
    
    private static ServiceHolder instance;
    
    public static synchronized ServiceHolder getInstance()
    {
        if (instance == null)
        {
            instance = new ServiceHolder();
        }
        return instance;
    }
    
    /**
     * Gets mailSenderService
     * @return mailSenderService the mailSenderService
     */
    public MailSenderService getMailSenderService()
    {
        return mailSenderService;
    }
    
    /**
     * Sets mailSenderService
     * @param mailSenderService the mailSenderService to set
     */
    public void setMailSenderService(MailSenderService mailSenderService)
    {
        this.mailSenderService = mailSenderService;
    }
    
    /**
     * 
     */
    private ServiceHolder()
    {
        // TODO Auto-generated constructor stub
    }
    
}
