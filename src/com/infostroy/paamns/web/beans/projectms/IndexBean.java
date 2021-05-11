/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.web.beans.PageBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class IndexBean extends PageBean
{
    private String value;
    
    public IndexBean()
    {
        if (this.getSession().get("project") != null
                && !String.valueOf(this.getSession().get("project")).isEmpty()
                && (this.getSessionBean().isAGU() || this.getSessionBean()
                        .isSTC()))
        {
            this.getSession().put("projectEditShowOnly", true);
            this.goTo(PagesTypes.PROJECTSTART);
        }
        else if (this.getSession().get("project") != null
                && !String.valueOf(this.getSession().get("project")).isEmpty())
        {
            this.goTo(PagesTypes.PROJECTINFORMATIONCOMPLETATTION);
        }
    }
    
    /**
     * Sets value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * 
     * @return value the value
     */
    public String getValue()
    {
        return value;
    }
}
