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
@Table(name = "qsn_goal")
public class QSNGoal extends LocalizableStaticEntity
{
    /**
     * Stores priorityCode value of entity.
     */
    @Column(name = "priorityCode")
    private String priorityCode;
    
    /**
     * Stores priorityDescription value of entity.
     */
    @Column(name = "priorityDescription")
    private String priorityDescription;
    
    /**
     * Stores generalPurposeCode value of entity.
     */
    @Column(name = "generalPurposeCode")
    private String generalPurposeCode;
    
    /**
     * Stores descriptionDeneralPurpose value of entity.
     */
    @Column(name = "descriptionDeneralPurpose")
    private String descriptionDeneralPurpose;
    
    /**
     * Stores specificObjectiveDescription value of entity.
     */
    @Column(name = "specificObjectiveDescription")
    private String specificObjectiveDescription;
    
    /**
     * Stores codeSpecificObjective value of entity.
     */
    @Column(name = "codeSpecificObjective")
    private String codeSpecificObjective;
    
    /**
     * Sets priorityCode
     * 
     * @param priorityCode
     *            the priorityCode to set
     */
    public void setPriorityCode(String priorityCode)
    {
        this.priorityCode = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.priorityCode, priorityCode));
    }
    
    /**
     * Gets priorityCode
     * 
     * @return priorityCode the priorityCode
     */
    public String getPriorityCode()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.priorityCode, null));
    }
    
    /**
     * Sets priorityDescription
     * 
     * @param priorityDescription
     *            the priorityDescription to set
     */
    public void setPriorityDescription(String priorityDescription)
    {
        this.priorityDescription = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.priorityDescription,
                priorityDescription));
    }
    
    /**
     * Gets priorityDescription
     * 
     * @return priorityDescription the priorityDescription
     */
    public String getPriorityDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.priorityDescription, null));
    }
    
    /**
     * Sets generalPurposeCode
     * 
     * @param generalPurposeCode
     *            the generalPurposeCode to set
     */
    public void setGeneralPurposeCode(String generalPurposeCode)
    {
        this.generalPurposeCode = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.generalPurposeCode,
                generalPurposeCode));
    }
    
    /**
     * Gets generalPurposeCode
     * 
     * @return generalPurposeCode the generalPurposeCode
     */
    public String getGeneralPurposeCode()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.generalPurposeCode, null));
    }
    
    /**
     * Sets descriptionDeneralPurpose
     * 
     * @param descriptionDeneralPurpose
     *            the descriptionDeneralPurpose to set
     */
    public void setDescriptionDeneralPurpose(String descriptionDeneralPurpose)
    {
        this.descriptionDeneralPurpose = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(),
                this.descriptionDeneralPurpose, descriptionDeneralPurpose));
    }
    
    /**
     * Gets descriptionDeneralPurpose
     * 
     * @return descriptionDeneralPurpose the descriptionDeneralPurpose
     */
    public String getDescriptionDeneralPurpose()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.descriptionDeneralPurpose, null));
    }
    
    /**
     * Sets specificObjectiveDescription
     * 
     * @param specificObjectiveDescription
     *            the specificObjectiveDescription to set
     */
    public void setSpecificObjectiveDescription(
            String specificObjectiveDescription)
    {
        this.specificObjectiveDescription = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(),
                this.specificObjectiveDescription, specificObjectiveDescription));
    }
    
    /**
     * Gets specificObjectiveDescription
     * 
     * @return specificObjectiveDescription the specificObjectiveDescription
     */
    public String getSpecificObjectiveDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.specificObjectiveDescription, null));
    }
    
    /**
     * Sets codeSpecificObjective
     * 
     * @param codeSpecificObjective
     *            the codeSpecificObjective to set
     */
    public void setCodeSpecificObjective(String codeSpecificObjective)
    {
        this.codeSpecificObjective = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(),
                this.codeSpecificObjective, codeSpecificObjective));
    }
    
    /**
     * Gets codeSpecificObjective
     * 
     * @return codeSpecificObjective the codeSpecificObjective
     */
    public String getCodeSpecificObjective()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.codeSpecificObjective, null));
    }
}
