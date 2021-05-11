/**
 * 
 */
package com.infostroy.paamns.web.listeners;

import java.io.File;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.infostroy.paamns.persistence.HibernateUtil;
import com.infostroy.paamns.persistence.IConnectionListner;
import com.infostroy.paamns.web.services.MailSenderService;
import com.infostroy.paamns.web.services.base.ServiceHolder;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public class ApplicationListener implements ServletContextListener,
        IConnectionListner
{
    //private FileModificationListenerService fileModificationListenerService;
    
    private String realPath;
    
    private String reportsPath;
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        tryStopServices();
        
    }
    
    /**
     * 
     */
    private void tryStopServices()
    {
        try
        {
            if (ServiceHolder.getInstance().getMailSenderService() != null)
            {
                System.out.println("Stopping MailSenderService...");
                ServiceHolder.getInstance().getMailSenderService().stop();
                ServiceHolder.getInstance().setMailSenderService(null);
            }
            
        }
        catch(Exception e)
        {
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        System.out.println("Using JSF: "
                + FacesContext.class.getPackage().getImplementationVersion());
        this.realPath = servletContextEvent.getServletContext()
                .getRealPath("/");
        HibernateUtil.addConnectionListener(this);
        HibernateUtil.getSessionFactory();
        
    }
    
    private String getBaseDir()
    {
        return new File(new File(this.realPath).getParent()).getAbsolutePath();
    }
    
    public String getFileDir()
    {
        File file = new File(getBaseDir(), "File");
        file.mkdir();
        
        return file.getPath();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see it.nexera.persistence.IConnectionListner#fireConnetionEstablished()
     */
    @Override
    public void fireConnetionEstablished()
    {
        tryStopServices();
        
        startServices();
    }
    
    /**
     * @param servletContext
     */
    private void startServices()
    {
        System.out.println("Running MailSenderService...");
        ServiceHolder.getInstance().setMailSenderService(
                new MailSenderService("MailSenderService"));
        ServiceHolder.getInstance().getMailSenderService().start();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see it.nexera.persistence.IConnectionListner#fireConnetionResufed()
     */
    @Override
    public void fireConnetionResufed()
    {
        tryStopServices();
        
    }
}
