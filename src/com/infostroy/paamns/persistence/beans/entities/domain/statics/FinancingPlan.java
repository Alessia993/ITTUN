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
@Table(name = "financing_plan")
public class FinancingPlan extends LocalizableStaticEntity
{
    @Column
    private String year;
    
    @Column
    private String contribution;
    
    /**
     * Sets year
     * @param year the year to set
     */
    public void setYear(String year)
    {
        this.year = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.year, year));
    }
    
    /**
     * Gets year
     * @return year the year
     */
    public String getYear()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.year, null));
    }
    
    /**
     * Sets contribution
     * @param contribution the contribution to set
     */
    public void setContribution(String contribution)
    {
        this.contribution = contribution;
    }
    
    /**
     * Gets contribution
     * @return contribution the contribution
     */
    public String getContribution()
    {
        return contribution;
    }
}
