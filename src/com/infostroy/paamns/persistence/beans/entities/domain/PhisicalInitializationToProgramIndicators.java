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
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "phisical_initialization_to_program_indicators")
public class PhisicalInitializationToProgramIndicators extends
        PhisicalInitializationToIndicators
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long         serialVersionUID = -5538328073655416219L;
    
    /**
     * Stores indicator value of entity.
     */
    @ManyToOne
    @JoinColumn
    private ProgramIndicators         indicator;
    
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
    public void setProgramIndicator(ProgramIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public ProgramIndicators getProgramIndicator()
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
    
    public PhisicalInitializationToProgramIndicators clone()
    {
        PhisicalInitializationToProgramIndicators newEntity = new PhisicalInitializationToProgramIndicators();
        
        newEntity.setClosureValue(this.getClosureValue());
        newEntity.setCreateDate(this.getCreateDate());
        newEntity.setDeleted(this.getDeleted());
        newEntity.setEngageValue(this.getEngageValue());
        newEntity.setGotValue(this.getGotValue());
        newEntity.setPhisicalInitialization(this.getPhisicalInitialization());
        newEntity.setProgramIndicator(this.getProgramIndicator());
        newEntity.setProgrammedValues(this.getProgrammedValues());
        newEntity.setProgrammedValueUpdate(this.getProgrammedValueUpdate());
        newEntity.setVersion(this.getVersion());
        newEntity.setTargetValue(this.getTargetValue());
        newEntity.setUnitValue(this.getUnitValue());
        newEntity.setAsse1(this.getAsse1());
        newEntity.setAsse2(this.getAsse2());
        newEntity.setAsse3(this.getAsse3());
        newEntity.setAsse4(this.getAsse4());
        newEntity.setAsse5(this.getAsse5());
        newEntity.setAsse6(this.getAsse6());
        newEntity.setAsse7(this.getAsse7());
        newEntity.setAsse8(this.getAsse8());
        newEntity.setAsse9(this.getAsse9());
        newEntity.setAsse10(this.getAsse10());
        return newEntity;
    }
    
    public static boolean IsChanged(
            PhisicalInitializationToProgramIndicators obj1,
            PhisicalInitializationToProgramIndicators obj2)
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
