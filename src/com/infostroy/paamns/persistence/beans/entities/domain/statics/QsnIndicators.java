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
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "qsn_indicators")
public class QsnIndicators extends LocalizableStaticEntity
{
    /**
     * Stores priorityCode value of entity.
     */
    @Column(name = "priority_code")
    private String priorityCode;
    
    /**
     * Stores descriptionPriorirty value of entity.
     */
    @Column(name = "description_priorirty")
    private String descriptionPriorirty;
    
    /**
     * Stores descriptionResult value of entity.
     */
    @Column(name = "description_result")
    private String descriptionResult;
    
    /**
     * Stores measurementUnits value of entity.
     */
    @Column(name = "qsn_resalto")
    private String qsnResalto;
    
    /**
     * Sets descriptionPriorirty
     * @param descriptionPriorirty the descriptionPriorirty to set
     */
    public void setDescriptionPriorirty(String descriptionPriorirty)
    {
        this.descriptionPriorirty = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.descriptionPriorirty,
                descriptionPriorirty));
    }
    
    /**
     * Gets descriptionPriorirty
     * @return descriptionPriorirty the descriptionPriorirty
     */
    public String getDescriptionPriorirty()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.descriptionPriorirty, null));
    }
    
    /**
     * Sets priorityCode
     * @param priorityCode import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;the priorityCode to set
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
        return priorityCode;
    }
    
    /**
     * Sets qsnResalto
     * @param qsnResalto the qsnResalto to set
     */
    public void setQsnResalto(String qsnResalto)
    {
        this.qsnResalto = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.qsnResalto,
                        qsnResalto));
    }
    
    /**
     * Gets measurementUnits
     * @return measurementUnits the measurementUnits
     */
    public String getQsnResalto()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.qsnResalto, null));
    }
    
    /**
     * Sets descriptionResult
     * @param descriptionResult the descriptionResult to set
     */
    public void setDescriptionResult(String descriptionResult)
    {
        this.descriptionResult = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.descriptionResult,
                descriptionResult));
    }
    
    /**
     * Gets descriptionResult
     * @return descriptionResult the descriptionResult
     */
    public String getDescriptionResult()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.descriptionResult, null));
    }
    
}
