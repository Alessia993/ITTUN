/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class QSNIndicatorWrapper extends PhisicalInitializationToIndicators
{
    
    public QSNIndicatorWrapper(QsnIndicators indicator)
    {
        this.indicator = indicator;
        
    }
    
    public QSNIndicatorWrapper(PhisicalInitializationToQsnIndicators indicator)
    {
        this.indicator = indicator.getIndicator();
        this.setClosureValue(indicator.getClosureValue());
        this.setEngageValue(indicator.getEngageValue());
        this.setGotValue(indicator.getGotValue());
        this.setProgrammedValues(indicator.getProgrammedValues());
        this.setProgrammedValueUpdate(indicator.getProgrammedValueUpdate());
        this.setId(indicator.getId());
    }
    
    private QsnIndicators indicator;
    
    public void setIndicator(QsnIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    public QsnIndicators getIndicator()
    {
        return indicator;
    }
    
}
