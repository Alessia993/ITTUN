/**
 * 
 */
package com.infostroy.paamns.persistence;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.SessionScope;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public class GenericManager
{
    /**
     * 
     */
    protected static final Logger log                   = Logger.getLogger(PersistenceSessionManager.class);
    
    /**
     * 
     */
    protected final static String STR_BEANS_CONFIG_PATH = "../applicationContext.xml";
    
    /**
     * 
     */
    protected XmlBeanFactory      factory               = null;
    
    public GenericManager()
    {
        try
        {
            log.info("GenericManager()");
            this.factory = new XmlBeanFactory(new ClassPathResource(
                    STR_BEANS_CONFIG_PATH));
            this.factory.registerScope("session", new SessionScope());
        }
        catch(Exception ex)
        {
            this.factory = null;
        }
    }
}
