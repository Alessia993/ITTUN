/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.AnagraphicalData;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "specific_goals")
public class SpecificGoals extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -3541962628515717331L;
    
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "anagraphical_data_id")
    private AnagraphicalData  anagraphicalData;
    
    @Column(name = "thematic_object")
    private String              thematic_object;
    
    @Column(name = "priority_investment")
    private String              priority_investment;
    
    /**
     * Sets anagraphicalData
     * 
     * @param anagraphicalData
     *            the anagraphicalData to set
     */
    public void setAnagraphicalData(AnagraphicalData anagraphicalData)
    {
        this.anagraphicalData = anagraphicalData;
    }
    
    @Column
    private String code;
    
    /**
     * Gets anagraphicalData
     * 
     * @return anagraphicalData the anagraphicalData
     */
    public AnagraphicalData getAnagraphicalData()
    {
        return anagraphicalData;
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
	 * Sets code
	 * @param code the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}
    
    /**
	 * Gets thematic_object
	 * @return thematic_object the thematic_object
	 */
    public String getThematic_object()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.thematic_object, null));
    }	
    
    /**
	 * Sets thematic_object
	 * @param thematic_object the thematic_object to set
	 */
    public void setThematic_object(String thematic_object)
    {
        this.thematic_object = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.thematic_object, thematic_object));
    }

	/**
	 * Gets priority_investment
	 * @return priority_investment the priority_investment
	 */
	public String getPriority_investment() {
		return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.priority_investment, null));
	}

	/**
	 * Sets priority_investment
	 * @param priority_investment the priority_investment to set
	 */
	public void setPriority_investment(String priority_investment) {
		this.priority_investment = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.priority_investment, priority_investment));
	}
    
    
}
