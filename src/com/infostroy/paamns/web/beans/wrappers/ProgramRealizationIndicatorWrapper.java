/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.PhisicalInitializationToIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProgramRealizationIndicatorWrapper extends
        PhisicalInitializationToIndicators
{
    public ProgramRealizationIndicatorWrapper(ProgramIndicators indicator,
            String programmedValue, boolean selected)
    {
        this.indicator = indicator;
        this.setProgrammedValues(programmedValue);
        this.setSelected(selected);
    }
    
    public ProgramRealizationIndicatorWrapper(
            PhisicalInitializationToProgramIndicators indicator,
            String programmedValue, boolean selected)
    {
        this.indicator = indicator.getProgramIndicator();
        this.setClosureValue(indicator.getClosureValue());
        this.setEngageValue(indicator.getEngageValue());
        this.setGotValue(indicator.getGotValue());
        this.setProgrammedValues(indicator.getProgrammedValues());
        this.setProgrammedValueUpdate(indicator.getProgrammedValueUpdate());
        this.setId(indicator.getId());
        this.setSelected(selected);
    }
    
    public ProgramRealizationIndicatorWrapper(
            PhisicalInitializationToProgramIndicators indicator)
    {
        this.indicator = indicator.getProgramIndicator();
        this.setClosureValue(indicator.getClosureValue());
        this.setEngageValue(indicator.getEngageValue());
        this.setGotValue(indicator.getGotValue());
        this.setProgrammedValues(indicator.getProgrammedValues());
        this.setProgrammedValueUpdate(indicator.getProgrammedValueUpdate());
        this.setId(indicator.getId());
    }
    
    private boolean           selected;
    
    private ProgramIndicators indicator;
    
    /**
     * Sets indicator
     * @param indicator the indicator to set
     */
    public void setIndicator(ProgramIndicators indicator)
    {
        this.indicator = indicator;
    }
    
    /**
     * Gets indicator
     * @return indicator the indicator
     */
    public ProgramIndicators getIndicator()
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
