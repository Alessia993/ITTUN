/**
 * 
 */
package com.infostroy.paamns.web.beans.session;

import java.util.Map;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PagesProcessingException;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */
public class PagesProcessorImpl implements PagesProcessor
{
    Map<String, GrantProcessor> grantProcessors = null;
    
    /**
     * 
     */
    public PagesProcessorImpl()
    {
        
    }
    
    /**
     * 
     * @return
     */
    public Map<String, GrantProcessor> getGrantProcessors()
    {
        return grantProcessors;
    }
    
    /**
     * 
     * @param grantProcessors
     */
    public void setGrantProcessors(Map<String, GrantProcessor> grantProcessors)
    {
        this.grantProcessors = grantProcessors;
    }
    
    /**
     * 
     * @param pages
     * @return
     * @throws PagesProcessingException 
     */
    public boolean isGrantForPage(PagesTypes page)
            throws PagesProcessingException
    {
        if (page == null || grantProcessors == null)
        {
            throw new PagesProcessingException("");
        }
        
        GrantedAuthority[] auths = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        
        GrantProcessor gp;
        for (GrantedAuthority auth : auths)
        {
            gp = grantProcessors.get(auth.getAuthority());
            if (gp != null)
            {
                if (gp.isGrantForPage(page) == true)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
