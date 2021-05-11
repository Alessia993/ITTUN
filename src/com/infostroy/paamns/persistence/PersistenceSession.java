/**
 * 
 */
package com.infostroy.paamns.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2009.
 * 
 */
public class PersistenceSession
{
    private static final Logger log     = Logger.getLogger(PersistenceSession.class);
    
    private Session             session = null;
    
    /**
     * 
     */
    public PersistenceSession()
    {
    }
    
    /**
     * Gets session
     * 
     * @return session the session
     * @throws PersistenceBeanException
     */
    public Session getSession() throws PersistenceBeanException
    {
        
        if (this.session == null || !this.session.isConnected()
                || !this.session.isOpen())
        {
            
            try
            {
                this.session = createSession();
            }
            catch(FileNotFoundException e)
            {
                
                log.warn("PersistenceSession.getSession : " + e);
                e.printStackTrace();
            }
        }
        
        return this.session;
        
    }
    
    public Session getSession(String host, String database)
            throws PersistenceBeanException
    {
        
        if (this.session == null)
        {
            try
            {
                this.session = createSession(host, database);
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return this.session;
    }
    
    /**
     * 
     * @return
     */
    public static PersistenceSession getInstance()
    {
        return new PersistenceSession();
    }
    
    /**
     * 
     */
    public static void InitializeDB()
    {
        Properties paamsProps = new Properties();
        
        try
        {
            InputStream is = new FileInputStream(new File("./paams.properties"));
            paamsProps.load(is);
        }
        catch(FileNotFoundException e)
        {
            InputStream is = PersistenceSession.class
                    .getResourceAsStream("/paams.properties");
            try
            {
                paamsProps.load(is);
            }
            catch(IOException e1)
            {
                return;
            }
            log.info("Paams Props has been read from jar...");
            System.out.println("Paams Props has been read from jar...");
        }
        catch(IOException e)
        {
            InputStream is = PersistenceSession.class
                    .getResourceAsStream("/paams.properties");
            try
            {
                paamsProps.load(is);
            }
            catch(IOException e1)
            {
                log.info("Paams Props hasn't been read...");
                System.out.println("Paams Props hasn't been read...");
                return;
            }
            log.info("Paams Props has been read from jar...");
            System.out.println("Paams Props has been read from jar...");
        }
        preInitDatabase(paamsProps);
        return;
    }
    
    /**
     * 
     * @return
     * @throws PersistenceBeanException
     */
    private static Session createSession() throws PersistenceBeanException,
            FileNotFoundException
    {
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            session.setFlushMode(FlushMode.COMMIT);
        }
        catch(Throwable ex)
        {
            throw new PersistenceBeanException(
                    "Error of Hibernate Session creating...", ex);
        }
        return session;
    }
    
    private static Session createSession(String host, String database)
            throws PersistenceBeanException, FileNotFoundException
    {
        log.info("Session.createSession()...");
        Session session = null;
        // Configuration cfg = null;
        
        try
        {
            // cfg = getConfiguration(log);
            // SessionFactory sessionFactory = cfg.buildSessionFactory();
            AnnotationConfiguration cfg = new AnnotationConfiguration();
            cfg.configure();
            String connection_string = String.valueOf(cfg.getProperties().get(
                    "hibernate.connection.url"));
            connection_string = connection_string.substring(0,
                    connection_string.lastIndexOf("/"));
            connection_string = connection_string.substring(0,
                    connection_string.lastIndexOf("/") + 1);
            connection_string += host + "/" + database;
            cfg.getProperties().put("hibernate.connection.url",
                    connection_string);
            SessionFactory sessionFactory = cfg.buildSessionFactory();
            session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.COMMIT);
        }
        catch(Throwable ex)
        {
            log.info("createSession(). exception : " + ex);
            throw new PersistenceBeanException(
                    "Error of Hibernate Session creating...", ex);
        }
        log.info("Session.createSession()...");
        return session;
    }
    
    public static Configuration getConfiguration(Logger outLog)
    {
        Configuration cfg = null;
        InputStream is;
        try
        {
            is = new FileInputStream(new File("./hibernate.cfg.xml"));
            cfg = new AnnotationConfiguration().addInputStream(is).configure();
            
            outLog.info("Hibernate cfg has been read from current dir...");
        }
        catch(FileNotFoundException e)
        {
            is = PersistenceSession.class
                    .getResourceAsStream("/hibernate.cfg.xml");
            if (is != null)
            {
                cfg = new AnnotationConfiguration().addInputStream(is)
                        .configure();
                
                outLog.info("Hibernate cfg has been read by current ClassLoader...");
            }
        }
        if (cfg == null)
        {
            cfg = new AnnotationConfiguration().configure();
        }
        return cfg;
    }
    
    /**
     * 
     */
    public void closeSession()
    {
        if (this.session != null)
        {
            this.session.flush();
            this.session.close();
            this.session = null;
        }
    }
    
    /**
     * 
     */
    protected void finalize()
    {
        closeSession();
    }
    
    private static void preInitDatabase(Properties paamsProps)
    {
        
        // login info
        String user = paamsProps.getProperty("db.login");
        String pass = paamsProps.getProperty("db.passw");
        
        // section of SQL scripts retrieved from file
        String paamsSQL = paamsProps.getProperty("command.create");
        String userSQL = paamsProps.getProperty("command.createUser");
        String grantSQL = paamsProps.getProperty("command.grant");
        String dropSQL = paamsProps.getProperty("command.drop");
        
        // section of hibernate configuration properties
        Configuration cfg = getConfiguration(log);
        String driver = cfg.getProperty("hibernate.connection.driver_class");
        String hibernateURL = cfg.getProperty("hibernate.connection.url");
        String URL = hibernateURL.substring(0, hibernateURL.lastIndexOf('/'))
                + "/mysql";
        
        boolean createDatabase = "true".equals(paamsProps
                .getProperty("mode.create"));
        boolean dropDatabase = "true".equals(paamsProps
                .getProperty("mode.drop"));
        boolean createUser = "true".equals(paamsProps
                .getProperty("mode.createUser"));
        if (user == null || pass == null)
        {
            System.out
                    .println("Please specify correct login information for  database!");
            log.warn("Please specify correct login information for  database!");
            return;
        }
        Connection connection = null;
        Statement createDatabaseStatement = null;
        Statement dropDatabaseStatement = null;
        Statement createUserStatement = null;
        try
        {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(URL, user, pass);
            if (dropSQL != null && !dropSQL.isEmpty() && dropDatabase)
            {
                dropDatabaseStatement = connection.createStatement();
                log.info("Drop instance of paams db...");
                System.out.println("Drop instance of paams db...");
                log.info(dropDatabaseStatement.execute(dropSQL));
            }
            if (paamsSQL != null && !paamsSQL.isEmpty() && createDatabase)
            {
                createDatabaseStatement = connection.createStatement();
                log.info("Create instance of paams db...");
                System.out.println("Create instance of paams db...");
                log.info(createDatabaseStatement.execute(paamsSQL));
            }
            if ((userSQL != null && !userSQL.isEmpty() && createUser)
                    && (grantSQL != null && !grantSQL.isEmpty()))
            {
                createUserStatement = connection.createStatement();
                log.info("Create user for paams db...");
                System.out.println("Create user for paams db...");
                log.info(createUserStatement.execute(userSQL));
                log.info(createUserStatement.execute(grantSQL));
            }
        }
        catch(Exception e)
        {
            log.warn("PersistenceSession:createDatabaseWithJDBC" + e);
        }
        finally
        {
            if (createDatabaseStatement != null)
            {
                try
                {
                    createDatabaseStatement.close();
                }
                catch(SQLException e)
                {
                } // nothing we can do
            }
            if (dropDatabaseStatement != null)
            {
                try
                {
                    dropDatabaseStatement.close();
                }
                catch(SQLException e)
                {
                } // nothing we can do
            }
            if (createUserStatement != null)
            {
                try
                {
                    createUserStatement.close();
                }
                catch(SQLException e)
                {
                } // nothing we can do
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                } // nothing we can do
            }
        }
    }
    
}
