/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import javax.persistence.Column;

import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class QSNGoalWebBean extends LocalizableStaticEntity
{
    
    private String priorityCode;
    
    /**
     * Stores priorityDescription value of entity.
     */
    private String priorityDescription;
    
    /**
     * Stores generalPurposeCode value of entity.
     */
    
    private String generalPurposeCode;
    
    /**
     * Stores descriptionDeneralPurpose value of entity.
     */
    
    private String descriptionDeneralPurpose;
    
    /**
     * Stores specificObjectiveDescription value of entity.
     */
    
    private String specificObjectiveDescription;
    
    /**
     * Stores codeSpecificObjective value of entity.
     */
    
    private String codeSpecificObjective;
    
    /**
     * Sets priorityCode
     * @param priorityCode the priorityCode to set
     */
    public void setPriorityCode(String priorityCode)
    {
        this.priorityCode = priorityCode;
    }
    
    /**
     * Gets priorityCode
     * @return priorityCode the priorityCode
     */
    public String getPriorityCode()
    {
        return this.priorityCode;
    }
    
    /**
     * Sets priorityDescription
     * @param priorityDescription the priorityDescription to set
     */
    public void setPriorityDescription(String priorityDescription)
    {
        this.priorityDescription = priorityDescription;
    }
    
    /**
     * Gets priorityDescription
     * @return priorityDescription the priorityDescription
     */
    public String getPriorityDescription()
    {
        return this.priorityDescription;
    }
    
    /**
     * Sets generalPurposeCode
     * @param generalPurposeCode the generalPurposeCode to set
     */
    public void setGeneralPurposeCode(String generalPurposeCode)
    {
        this.generalPurposeCode = generalPurposeCode;
    }
    
    /**
     * Gets generalPurposeCode
     * @return generalPurposeCode the generalPurposeCode
     */
    public String getGeneralPurposeCode()
    {
        return this.generalPurposeCode;
    }
    
    /**
     * Sets descriptionDeneralPurpose
     * @param descriptionDeneralPurpose the descriptionDeneralPurpose to set
     */
    public void setDescriptionDeneralPurpose(String descriptionDeneralPurpose)
    {
        this.descriptionDeneralPurpose = descriptionDeneralPurpose;
    }
    
    /**
     * Gets descriptionDeneralPurpose
     * @return descriptionDeneralPurpose the descriptionDeneralPurpose
     */
    public String getDescriptionDeneralPurpose()
    {
        return this.descriptionDeneralPurpose;
    }
    
    /**
     * Sets specificObjectiveDescription
     * @param specificObjectiveDescription the specificObjectiveDescription to set
     */
    public void setSpecificObjectiveDescription(
            String specificObjectiveDescription)
    {
        this.specificObjectiveDescription = specificObjectiveDescription;
    }
    
    /**
     * Gets specificObjectiveDescription
     * @return specificObjectiveDescription the specificObjectiveDescription
     */
    public String getSpecificObjectiveDescription()
    {
        return this.specificObjectiveDescription;
    }
    
    /**
     * Sets codeSpecificObjective
     * @param codeSpecificObjective the codeSpecificObjective to set
     */
    public void setCodeSpecificObjective(String codeSpecificObjective)
    {
        this.codeSpecificObjective = codeSpecificObjective;
    }
    
    /**
     * Gets codeSpecificObjective
     * @return codeSpecificObjective the codeSpecificObjective
     */
    public String getCodeSpecificObjective()
    {
        return this.codeSpecificObjective;
    }
    
    public QSNGoalWebBean()
    {
        
    }
    
    public QSNGoalWebBean(QSNGoal qsnGoal, boolean selected)
    {
        this.setCodeSpecificObjective(qsnGoal.getCodeSpecificObjective());
        this.setDescriptionDeneralPurpose(qsnGoal
                .getDescriptionDeneralPurpose());
        this.setGeneralPurposeCode(qsnGoal.getGeneralPurposeCode());
        this.setId(qsnGoal.getId());
        this.setPriorityCode(qsnGoal.getPriorityCode());
        this.setPriorityDescription(qsnGoal.getPriorityDescription());
        this.setSpecificObjectiveDescription(qsnGoal
                .getSpecificObjectiveDescription());
        this.setSelected(selected);
    }
    
    @Column(name = "selected")
    private boolean selected;
    
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
