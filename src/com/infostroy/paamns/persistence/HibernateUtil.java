/**
 * 
 */
package com.infostroy.paamns.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class HibernateUtil implements IConnectionManager
{
    private static SessionFactory           sessionFactory;
    
    private static List<IConnectionListner> connectionListners = new ArrayList<IConnectionListner>();
    
    public static synchronized SessionFactory getSessionFactory()
    {
		try
		{
			if (sessionFactory == null)
			{
				sessionFactory = new AnnotationConfiguration().configure()
						.buildSessionFactory();
				checkConnection(new Date());
			}

		}
		catch (Throwable ex)
		{
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
        if (sessionFactory.isClosed())
        {
            cleanFactory();
        }
        return sessionFactory;
    }
    
    public static void cleanFactory()
    {
        try
        {
            if (sessionFactory != null && !sessionFactory.isClosed())
            {
                sessionFactory.close();
            }
            
            sessionFactory = new AnnotationConfiguration().configure()
                    .buildSessionFactory();
        }
        catch(Throwable ex)
        {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static void addConnectionListener(IConnectionListner listener)
    {
        connectionListners.add(listener);
    }
    
    public static void removeConnectionListener(IConnectionListner listener)
    {
        connectionListners.remove(listener);
    }
    
    public static void checkConnection() throws Exception
    {
        Session s = sessionFactory.openSession();
        s.beginTransaction().commit();
        SessionTracker.getInstance().sessionOpening("HibernateUtil");
        s.close();
        SessionTracker.getInstance().sessionClosing("HibernateUtil");
        s = null;
        
    }
    
    private static void checkConnection(Date d1)
    {
        try
        {
            checkConnection();
        }
        catch(Exception e)
        {
            System.out.println("Opening a DB connection... Failed");
            System.out.println(e);
            onConnectionFail();
            return;
        }
        
        System.out.println(String.format(
                "Connection was successfully opened in %d seconds",
                ((new Date().getTime() - d1.getTime()) / 1000)));
        onSuccessConnection();
    }
    
    private static void onSuccessConnection()
    {
        if (connectionListners == null)
        {
            return;
        }
        for (IConnectionListner item : connectionListners)
        {
            item.fireConnetionEstablished();
        }
    }
    
    private static void onConnectionFail()
    {
        if (connectionListners == null)
        {
            return;
        }
        for (IConnectionListner item : connectionListners)
        {
            item.fireConnetionResufed();
        }
    }
}
