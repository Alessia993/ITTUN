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
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "phisical_initialization_to_qsn_indicators")
public class PhisicalInitializationToQsnIndicators extends
        PhisicalInitializationToIndicators
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 6500495925560522061L;
    
    /**
     * Stores indicator value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private QsnIndicators     indicator;
    
    /**
     * Sets indicator
     * @param indicator the indicator to set
     */
    public void setIndicator(QsnIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public QsnIndicators getIndicator()
    {
        return indicator;
    }
}
