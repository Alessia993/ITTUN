/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;

/**
 *
 * @author Anton Kazakov
 * InfoStroy Co., 2014.
 *
 */
@Entity
@Table(name = "program_programma_indicators")
public class ProgramProgrammaIndicators extends ProgramIndicators
{
	/**
     * Stores asse value of entity.
     */
    @Column(name = "asse")
    private String asse;
    
    /**
     * Stores specificObjective value of entity.
     */
    @Column(name = "specific_objective")
    private String  specificObjective;
    
    
    /**
     * Stores units value of entity.
     */
    @Column(name = "units")
    private String  units;
    
    /**
     * Stores target value of entity.
     */
    @Column(name = "target")
    private String target;
    
    /**
     * Stores baseLine value of entity.
     */
    @Column(name = "base_line")
    private String baseLine;
    
    /**
     * Stores code value of entity.
     */
    @Column(name = "source")
    private String source;
    
    @Column(name = "survey")
    private String survey;
  
    /**
     * Sets specificObjective
     * @param specificObjective the specificObjective to set
     */
    public void setSpecificObjective(String specificObjective)
    {
        this.specificObjective = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.specificObjective,
                specificObjective));
    }
    
    /**
     * Gets specificObjective
     * @return specificObjective the specificObjective
     */
    public String getSpecificObjective()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.specificObjective, null));
    }
    
    
    public void setAsse(String specificObjective)
    {
        this.asse = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.asse,
                specificObjective));
    }
    
    
    public String getAsse()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.asse, null));
    }
    
    /**
     * Gets units
     * @return units the units
     */
    public String getUnits()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.units, null));
    }
    
    /**
     * Sets units
     * @param units the units to set
     */
    public void setUnits(String units)
    {
        this.units = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.units, units));
    }
    
    public void setTarget(String target)
    {
        this.target = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.target,
                target));
    }
    
    
    public String getTarget()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.target, null));
    }
    
    public void setBaseLine(String baseLine)
    {
        this.baseLine = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.baseLine,
                baseLine));
    }
    
    public String getBaseLine()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.baseLine, null));
    }

	public String getSource()
	{
		return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.source, null));
	}

	public void setSource(String source)
	{
		this.source = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.source, source));
    }

	public String getSurvey()
	{
		return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.survey, null));
	}

	public void setSurvey(String survey)
	{
		this.survey = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.survey, survey));
	}

}
