/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "prioritary_reasons")
public class PrioritaryReasons extends LocalizableStaticEntity
{
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private String code;
    
    /**
     * Stores priorityIssues value of entity.
     */
    @Column(name = "priority_issues") 
    private String priorityIssues;
    
    /**
     * Stores amount value of entity.
     */
    @Column(name = "amount")
    private String amount;
    
	/**
     * Sets code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * 
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets priorityIssues
     * 
     * @param priorityIssues
     *            the priorityIssues to set
     */
    public void setPriorityIssues(String priorityIssues)
    {
        this.priorityIssues = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.priorityIssues, priorityIssues));
    }
    
    /**
     * Gets priorityIssues
     * 
     * @return priorityIssues the priorityIssues
     */
    public String getPriorityIssues()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.priorityIssues, null));
    }
    
    /**
     * Sets amount
     * 
     * @param amount
     *            the amount to set
     */
    public void setAmount(String amount)
    {
        this.amount = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.amount, amount));
    }
    
    /**
     * Gets amount
     * 
     * @return amount the amount
     */
    public String getAmount()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.amount,
                null));
    }
}
