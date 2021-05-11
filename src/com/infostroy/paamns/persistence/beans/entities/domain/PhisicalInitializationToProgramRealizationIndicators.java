/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramRealizationIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "phisical_initialization_to_program_realization_indicators")
public class PhisicalInitializationToProgramRealizationIndicators extends
        PhisicalInitializationToIndicators
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long            serialVersionUID = -6160280946324960011L;
    
    /**
     * Stores indicator value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private ProgramRealizationIndicators indicator;
    
    /**
     * Sets indicator
     * @param indicator the indicator to set
     */
    public void setIndicator(ProgramRealizationIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public ProgramRealizationIndicators getIndicator()
    {
        return indicator;
    }
}
