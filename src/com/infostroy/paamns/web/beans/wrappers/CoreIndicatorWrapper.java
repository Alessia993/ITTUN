/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CoreIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class CoreIndicatorWrapper extends PhisicalInitializationToIndicators
{
    public CoreIndicatorWrapper(CoreIndicators indicator,
            String programmedValues)
    {
        this.indicator = indicator;
        this.setProgrammedValues(programmedValues);
    }
    
    public CoreIndicatorWrapper(PhisicalInitializationToCoreIndicators indicator)
    {
        this.indicator = indicator.getIndicator();
        this.setClosureValue(indicator.getClosureValue());
        this.setEngageValue(indicator.getEngageValue());
        this.setGotValue(indicator.getGotValue());
        this.setProgrammedValues(indicator.getProgrammedValues());
        this.setProgrammedValueUpdate(indicator.getProgrammedValueUpdate());
        this.setId(indicator.getId());
    }
    
    private CoreIndicators indicator;
    
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
}
