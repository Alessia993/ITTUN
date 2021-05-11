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
@Table(name = "core_indicators")
public class CoreIndicators extends LocalizableStaticEntity
{
    /**
     * Stores descriptionOfIndicator value of entity.
     */
    @Column(name = "description_of_indicator")
    private String  descriptionOfIndicator;
    
    /**
     * Stores descriptionUnitsMeasure value of entity.
     */
    @Column(name = "description_units_measure")
    private String  descriptionUnitsMeasure;
    
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private Integer code;
    
    /**
     * Sets descriptionOfIndicator
     * @param descriptionOfIndicator the descriptionOfIndicator to set
     */
    public void setDescriptionOfIndicator(String descriptionOfIndicator)
    {
        this.descriptionOfIndicator = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(),
                this.descriptionOfIndicator, descriptionOfIndicator));
    }
    
    /**
     * Gets descriptionOfIndicator
     * @return descriptionOfIndicator the descriptionOfIndicator
     */
    public String getDescriptionOfIndicator()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.descriptionOfIndicator, null));
    }
    
    /**
     * Sets descriptionUnitsMeasure
     * @param descriptionUnitsMeasure the descriptionUnitsMeasure to set
     */
    public void setDescriptionUnitsMeasure(String descriptionUnitsMeasure)
    {
        this.descriptionUnitsMeasure = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(),
                this.descriptionUnitsMeasure, descriptionUnitsMeasure));
    }
    
    /**
     * Gets descriptionUnitsMeasure
     * @return descriptionUnitsMeasure the descriptionUnitsMeasure
     */
    public String getDescriptionUnitsMeasure()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.descriptionUnitsMeasure, null));
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(Integer code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public Integer getCode()
    {
        return code;
    }
}
