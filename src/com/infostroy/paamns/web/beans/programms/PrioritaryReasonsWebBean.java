/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PrioritaryReasonsWebBean extends LocalizableStaticEntity
{
    
    private String code;
    
    private String priorityIssues;
    
    private String amount;
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets priorityIssues
     * @param priorityIssues the priorityIssues to set
     */
    public void setPriorityIssues(String priorityIssues)
    {
        this.priorityIssues = priorityIssues;
    }
    
    /**
     * Gets priorityIssues
     * @return priorityIssues the priorityIssues
     */
    public String getPriorityIssues()
    {
        return this.priorityIssues;
    }
    
    /**
     * Sets amount
     * @param amount the amount to set
     */
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    /**
     * Gets amount
     * @return amount the amount
     */
    public String getAmount()
    {
        return this.amount;
    }
    
    private boolean selected;
    
    public PrioritaryReasonsWebBean()
    {
        
    }
    
    public PrioritaryReasonsWebBean(PrioritaryReasons item, boolean selected)
    {
        this.setAmount(item.getAmount());
        this.setCode(item.getCode());
        this.setPriorityIssues(item.getPriorityIssues());
        this.setId(item.getId());
        this.setSelected(selected);
    }
    
    /**
     * Sets selected
     * @param selected the selected to set
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    /**
     * Gets selected
     * @return selected the selected
     */
    public boolean getSelected()
    {
        return selected;
    }
}
