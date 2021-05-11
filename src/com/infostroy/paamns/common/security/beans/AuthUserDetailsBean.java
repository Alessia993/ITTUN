/**
 * 
 */
package com.infostroy.paamns.common.security.beans;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.security.api.GrantedAuthorityImpl;
import com.infostroy.paamns.common.security.api.SecurityInfoFactMethod;
import com.infostroy.paamns.common.security.api.SecurityInfoProc;
import com.infostroy.paamns.common.security.api.UserDetailsImpl;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 * 
 */
public class AuthUserDetailsBean implements UserDetailsService
{
    private static final Logger  log                 = Logger.getLogger(AuthUserDetailsBean.class);
    
    HashMap<String, UserDetails> _users              = new HashMap<String, UserDetails>();
    
    SecurityInfoFactMethod       _securityController = null;
    
    SecurityInfoProc             _securityInfoProc   = null;
    
    /**
     * 
     */
    public AuthUserDetailsBean()
    {
    }
    
    /**
     * 
     * @param securityController
     */
    public void setSecurityController(SecurityInfoFactMethod securityController)
    {
        log.info("setSecurityController");
        this._securityController = securityController;
    }
    
    /**
     * 
     * @param securityInfoProc
     */
    public void setSecurityInfoProc(SecurityInfoProc securityInfoProc)
    {
        log.info("setSecurityController");
        this._securityInfoProc = securityInfoProc;
    }
    
    /**
     * 
     */
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException, DataAccessException
    {
        //log.info("AuthUserDetailsBean.loadUserByUsername : userName = " + userName);        
        if (_securityController == null)
        {
            throw new UsernameNotFoundException("User : " + userName
                    + " isn't found as securityController isn't defined...");
        }
        if (_securityInfoProc == null)
        {
            throw new UsernameNotFoundException("User : " + userName
                    + " isn't found as _securityInfoProc isn't defined...");
        }
        com.infostroy.paamns.common.security.api.SecurityInfo secInfo = null;
        try
        {
            secInfo = _securityController.getSecurityInfoByLogin(userName);
        }
        catch(IOException e)
        {
            throw new UsernameNotFoundException("User : " + userName
                    + " isn't found as IOException has been generated...", e);
        }
        catch(PersistenceBeanException e)
        {
            throw new UsernameNotFoundException(
                    "User : "
                            + userName
                            + " isn't found as UsernameNotFoundException has been generated...",
                    e);
        }
        List<String> grantsList = _securityInfoProc.procSecurityInfo(secInfo);
        Set<GrantedAuthority> authoritiesSet = getGrantedAuthoritiesFromList(grantsList);
        //log.info("AuthUserDetailsBean.loadUserByUsername : userName = " + userName + " password = " + secInfo.getPassword());        
        
        UserDetails userDetailUser = new UserDetailsImpl(userName,
                secInfo.getPassword(),
                authoritiesSet.toArray(new GrantedAuthority[0]));
        return userDetailUser;
    }
    
    /**
     * 
     * @param grantsList
     * @return
     */
    Set<GrantedAuthority> getGrantedAuthoritiesFromList(List<String> grantsList)
    {
        Set<GrantedAuthority> authoritiesSet = new HashSet<GrantedAuthority>();
        
        GrantedAuthority grAuth = null;
        for (String grant : grantsList)
        {
            grAuth = new GrantedAuthorityImpl(grant);
            authoritiesSet.add(grAuth);
        }
        return authoritiesSet;
    }
    
}
