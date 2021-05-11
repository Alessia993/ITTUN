package com.infostroy.paamns.common.security.beans;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.stereotype.Component;

import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.security.api.UserDetailsImpl;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.facades.UsersLoginsBean;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */
@Component
@Scope("request")
public class LoginBean
{
    protected transient final Log log         = LogFactory
                                                      .getLog(AccessGrantHelper.class);
    
    private String                username    = null;
    
    private String                password    = null;
    
    private boolean               loggedIn    = false;
    
    private String                authMessage = null;
    
    /**
     * 
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public String doLogin() throws IOException, ServletException
    {
        SessionManager.getInstance().getSessionBean().Session.put("project",
                null);
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
        
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                .getRequestDispatcher("/j_spring_security_check");
        
        dispatcher.forward((ServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
        
        FacesContext.getCurrentInstance().responseComplete();
        
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication()
                        .isAuthenticated())
        {
            setAuthMessage(null);
            this.loggedIn = true;
            UsersLoginsBean.AddSuccessMessage(username);
            log.info("Auth is ok...");
        }
        else
        {
            UsersLoginsBean.AddFailMessage(username);
            setAuthMessage(Utils.getString("validator.wrongLoginInfo"));
            this.loggedIn = false;
            log.info("Auth isn't ok...");
        }
        
        log.info("End processing");
        log.info("-------------------------------------------------------------------");
        return null;
    }
    
    /**
     * 
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String doLogout() throws ServletException, IOException
    {
        UsersLoginsBean.AddLogoutMessage(username);
        log.info("-------------------------------------------------------------------");
        log.info("Start processing");
        try
        {
            ExternalContext context = FacesContext.getCurrentInstance()
                    .getExternalContext();
            
            RequestDispatcher dispatcher = ((ServletRequest) context
                    .getRequest())
                    .getRequestDispatcher("/j_spring_security_logout");
            
            dispatcher.forward((ServletRequest) context.getRequest(),
                    (ServletResponse) context.getResponse());
            SessionManager.getInstance().getSessionBean().setCurrentUser(null);
            
            FacesContext.getCurrentInstance().responseComplete();
        }
        catch(Exception e)
        {
            log.error(e);
        }
        log.info("End processing");
        log.info("-------------------------------------------------------------------");
        SessionManager.getInstance().getSessionBean().Session.put("project",
                null);
        
        return null;
    }
    
    /**
     * 
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String doReset() throws ServletException, IOException
    {
        log.info("-------------------------------------------------------------------");
        log.info("Start processing");
        
        log.info("End processing");
        log.info("-------------------------------------------------------------------");
        
        return null;
    }
    
    /**
     * 
     */
    @PostConstruct
    @SuppressWarnings("unused")
    private void handleErrorMessage()
    {
        Exception e = (Exception) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
        
        if (e instanceof BadCredentialsException)
        {
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY,
                            null);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Username or password not valid.", null));
        }
    }
    
    /**
     * 
     * @return
     */
    public String getUsername()
    {
        log.info("getUsername() : " + this.username);
        return this.username;
    }
    
    /**
     * 
     * @param uathUserName
     */
    public void setAuthUsername(String uathUserName)
    {
    }
    
    /**
     * 
     * @return
     */
    @SuppressWarnings("unused")
    public String getAuthUsername()
    {
        try
        {
            Object udObject = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            
            if (udObject != null)
            {
                if (udObject instanceof String)
                {
                    return (String) udObject;
                }
                else
                {
                    UserDetailsImpl userDetails = (UserDetailsImpl) udObject;
                    
                    if (userDetails != null)
                    {
                        userDetails.getAuthorities();
                        return userDetails.getUsername();
                    }
                    else
                    {
                        return "anonimous";
                    }
                }
            }
            else
            {
                return "anonimous";
            }
        }
        catch(Exception e)
        {
            try
            {
                this.doLogout();
            }
            catch(ServletException e1)
            {
            }
            catch(IOException e1)
            {
            }
        }
        
        return "anonimous";
    }
    
    /**
     * 
     * @param username
     */
    public void setUsername(final String username)
    {
        log.info("setUsername(" + username + ")");
        this.username = username;
    }
    
    /**
     * 
     * @return
     */
    public String getPassword()
    {
        log.info("getPassword() : " + this.password);
        return this.password;
    }
    
    /**
     * 
     * @param password
     */
    public void setPassword(final String password)
    {
        log.info("setPassword(" + password + ")");
        this.password = password;
    }
    
    public void setLoggedIn(final boolean loggedIn)
    {
        log.info("setLoggedIn(" + loggedIn + ")");
        this.loggedIn = loggedIn;
    }
    
    /**
     * 
     * @return
     */
    public boolean getLoggedIn()
    {
        log.info("getLoggedIn() : " + loggedIn);
        return this.loggedIn;
    }
    
    /**
     * 
     * @param authMessage
     */
    public void setAuthMessage(String authMessage)
    {
        this.authMessage = authMessage;
    }
    
    /**
     * 
     * @return
     */
    public String getAuthMessage()
    {
        return this.authMessage;
    }
    
}
