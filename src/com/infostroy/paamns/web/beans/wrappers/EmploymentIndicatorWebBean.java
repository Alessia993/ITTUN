/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.EmploymentIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class EmploymentIndicatorWebBean extends
        PhisicalInitializationToIndicators
{
    public EmploymentIndicatorWebBean(EmploymentIndicators indicator,
            String programmedValues)
    {
        this.indicator = indicator;
        this.setProgrammedValues(programmedValues);
    }
    
    public EmploymentIndicatorWebBean(
            PhisicalInitializationToEmploymentIndicators indicator)
    {
        this.indicator = indicator.getIndicator();
        this.setClosureValue(indicator.getClosureValue());
        this.setEngageValue(indicator.getEngageValue());
        this.setGotValue(indicator.getGotValue());
        this.setProgrammedValues(indicator.getProgrammedValues());
        this.setProgrammedValueUpdate(indicator.getProgrammedValueUpdate());
        this.setId(indicator.getId());
    }
    
    private boolean              selected;
    
    private EmploymentIndicators indicator;
    
    /**
     * Sets indicator
     * @param indicator the indicator to set
     */
    public void setIndicator(EmploymentIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public EmploymentIndicators getIndicator()
    {
        return indicator;
    }
    
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
