/**
 * 
 */
package com.infostroy.paamns.common.security.beans;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */

@Component
@Scope("request")
public class CurrUserDetailInfoBean
{
    private static final Logger log = Logger.getLogger(LoginBean.class);
    
    /**
     * 
     * @return
     */
    public String getCurrentUserName()
    {
        log.info("-------------------------------------------------------------------");
        log.info("Start processing");
        
        String curUsername = null;
        
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        if (principal != null && principal instanceof UserDetails)
        {
            curUsername = ((UserDetails) principal).getUsername();
            log.info("curUsername : " + curUsername);
        }
        else
        {
            curUsername = principal.toString();
            log.info("curUsername : " + curUsername);
        }
        log.info("End processing");
        log.info("-------------------------------------------------------------------");
        return curUsername;
    }
    
    /**
     * 
     * @return
     */
    public String getCurrentGrants()
    {
        log.info("-------------------------------------------------------------------");
        log.info("Start processing");
        
        String currGrants = new String();
        
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        if (principal != null && principal instanceof UserDetails)
        {
            UserDetails ud = (UserDetails) principal;
            GrantedAuthority[] auths = ud.getAuthorities();
            if (auths != null)
            {
                for (GrantedAuthority auth : auths)
                {
                    currGrants += (auth.getAuthority() + "; ");
                }
            }
            log.info("curGrants : " + currGrants);
        }
        else
        {
            currGrants = principal.toString();
            log.info("curGrants : " + currGrants);
        }
        log.info("End processing");
        log.info("-------------------------------------------------------------------");
        return currGrants;
    }
}
