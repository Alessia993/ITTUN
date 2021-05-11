/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

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
@Table(name = "program_implementation")
public class ProgramImplementation extends LocalizableStaticEntity
{
    private String priorities;
    
    private String communityContribution;
    
    private String nationalCounterpart;
    
    private String publicFunding;
    
    private String privateFinancing;
    
    private String financing;
    
    private String financingRate;
    
    /**
     * Sets communityContribution
     * 
     * @param communityContribution
     *            the communityContribution to set
     */
    public void setCommunityContribution(String communityContribution)
    {
        this.communityContribution = communityContribution;
    }
    
    /**
     * Gets communityContribution
     * 
     * @return communityContribution the communityContribution
     */
    public String getCommunityContribution()
    {
        return communityContribution;
    }
    
    /**
     * Sets nationalCounterpart
     * 
     * @param nationalCounterpart
     *            the nationalCounterpart to set
     */
    public void setNationalCounterpart(String nationalCounterpart)
    {
        this.nationalCounterpart = nationalCounterpart;
    }
    
    /**
     * Gets nationalCounterpart
     * 
     * @return nationalCounterpart the nationalCounterpart
     */
    public String getNationalCounterpart()
    {
        return nationalCounterpart;
    }
    
    /**
     * Sets publicFunding
     * 
     * @param publicFunding
     *            the publicFunding to set
     */
    public void setPublicFunding(String publicFunding)
    {
        this.publicFunding = publicFunding;
    }
    
    /**
     * Gets publicFunding
     * 
     * @return publicFunding the publicFunding
     */
    public String getPublicFunding()
    {
        return publicFunding;
    }
    
    /**
     * Sets privateFinancing
     * 
     * @param privateFinancing
     *            the privateFinancing to set
     */
    public void setPrivateFinancing(String privateFinancing)
    {
        this.privateFinancing = privateFinancing;
    }
    
    /**
     * Gets privateFinancing
     * 
     * @return privateFinancing the privateFinancing
     */
    public String getPrivateFinancing()
    {
        return privateFinancing;
    }
    
    /**
     * Sets financing
     * 
     * @param financing
     *            the financing to set
     */
    public void setFinancing(String financing)
    {
        this.financing = financing;
    }
    
    /**
     * Gets financing
     * 
     * @return financing the financing
     */
    public String getFinancing()
    {
        return financing;
    }
    
    /**
     * Sets financingRate
     * 
     * @param financingRate
     *            the financingRate to set
     */
    public void setFinancingRate(String financingRate)
    {
        this.financingRate = financingRate;
    }
    
    /**
     * Gets financingRate
     * 
     * @return financingRate the financingRate
     */
    public String getFinancingRate()
    {
        return financingRate;
    }
    
    /**
     * Sets priorities
     * 
     * @param priorities
     *            the priorities to set
     */
    public void setPriorities(String priorities)
    {
        this.priorities = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.priorities, priorities));
    }
    
    /**
     * Gets priorities
     * 
     * @return priorities the priorities
     */
    public String getPriorities()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.priorities,
                null));
    }
}
