/**
 * 
 */
package com.infostroy.paamns.web.beans.session;

import java.util.List;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PagesProcessingException;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public class GrantProcessorImpl implements GrantProcessor
{
    private String       grant     = null;
    
    private List<String> resources = null;
    
    /**
     * 
     */
    public GrantProcessorImpl()
    {
        
    }
    
    /**
     * 
     * @param grant
     */
    public void setGrant(String grant)
    {
        this.grant = grant;
    }
    
    /**
     * 
     * @return
     */
    public String getGrant()
    {
        return this.grant;
    }
    
    /**
     * 
     * @param resources
     */
    public void setResources(List<String> resources)
    {
        this.resources = resources;
    }
    
    /**
     * 
     * @return
     */
    public List<String> getResources()
    {
        return this.resources;
    }
    
    /**
     * 
     * @param pages
     * @return
     * @throws PagesProcessingException
     */
    private boolean isGrantForStr(String page) throws PagesProcessingException
    {
        if (page == null)
        {
            throw new PagesProcessingException("");
        }
        for (String resource : resources)
        {
            if (page.equals(resource) == true)
            {
                return true;
            }
        }
        return false;
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
        return isGrantForStr(page.getPagesContext());
    }
}
