/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CoreIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "phisical_initialization_to_core_indicator")
public class PhisicalInitializationToCoreIndicators extends
        PhisicalInitializationToIndicators
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long         serialVersionUID = -5877068613551301475L;
    
    /**
     * Stores indicator value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private CoreIndicators            indicator;
    
    @Column(name = "version")
    private Long                      version;
    
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "progress_validation_object_id")
    private ProgressValidationObjects progressValidationObject;
    
    /**
     * Sets indicator
     * @param indicator the indicator to set
     */
    public void setIndicator(CoreIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public CoreIndicators getIndicator()
    {
        return indicator;
    }
    
    /**
     * Sets version
     * @param version the version to set
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }
    
    /**
     * Gets version
     * @return version the version
     */
    public Long getVersion()
    {
        return version;
    }
    
    public PhisicalInitializationToCoreIndicators clone()
    {
        PhisicalInitializationToCoreIndicators newEntity = new PhisicalInitializationToCoreIndicators();
        
        newEntity.setClosureValue(this.getClosureValue());
        newEntity.setCreateDate(this.getCreateDate());
        newEntity.setDeleted(this.getDeleted());
        newEntity.setEngageValue(this.getEngageValue());
        newEntity.setGotValue(this.getGotValue());
        newEntity.setIndicator(this.getIndicator());
        newEntity.setPhisicalInitialization(this.getPhisicalInitialization());
        newEntity.setProgrammedValues(this.getProgrammedValues());
        newEntity.setProgrammedValueUpdate(this.getProgrammedValueUpdate());
        newEntity.setVersion(this.getVersion());
        
        return newEntity;
    }
    
    public static boolean IsChanged(
            PhisicalInitializationToCoreIndicators obj1,
            PhisicalInitializationToCoreIndicators obj2)
    {
        return (!obj1.getClosureValue().equals(obj2.getClosureValue())
                || !obj1.getEngageValue().equals(obj2.getEngageValue())
                || !obj1.getGotValue().equals(obj2.getGotValue())
                || !obj1.getProgrammedValues().equals(
                        obj2.getProgrammedValues()) || !obj1
                .getProgrammedValueUpdate().equals(
                        obj2.getProgrammedValueUpdate()));
    }
    
    /**
     * Sets progressValidationObject
     * @param progressValidationObject the progressValidationObject to set
     */
    public void setProgressValidationObject(
            ProgressValidationObjects progressValidationObject)
    {
        this.progressValidationObject = progressValidationObject;
    }
    
    /**
     * Gets progressValidationObject
     * @return progressValidationObject the progressValidationObject
     */
    public ProgressValidationObjects getProgressValidationObject()
    {
        return progressValidationObject;
    }
}
