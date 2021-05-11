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
@Table(name = "employment_indicators")
public class EmploymentIndicators extends LocalizableStaticEntity
{
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String  description;
    
    /**
     * Stores measurementUnits value of entity.
     */
    @Column(name = "measurement_units")
    private String  measurementUnits;
    
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private Integer code;
    
    /**
     * Sets measurementUnits
     * @param measurementUnits the measurementUnits to set
     */
    public void setMeasurementUnits(String measurementUnits)
    {
        this.measurementUnits = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.measurementUnits,
                measurementUnits));
    }
    
    /**
     * Gets measurementUnits
     * @return measurementUnits the measurementUnits
     */
    public String getMeasurementUnits()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.measurementUnits, null));
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.description,
                description));
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, null));
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
