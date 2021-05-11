/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@MappedSuperclass
public class PhisicalInitializationToIndicators extends PersistentEntity
{
    /**
     * Stores phisicalInizialization value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "phisical_initialization_id")
    private PhisicalInitializations phisicalInitialization;
    
    /**
     * Stores programmedValues value of entity.
     */
    @Column(name = "programmed_values")
    private String                  programmedValues;
    
    /**
     * Stores engageValue value of entity.
     */
    @Column(name = "engage_value")
    private String                  engageValue;
    
    /**
     * Stores gotValue value of entity.
     */
    @Column(name = "got_value")
    private String                  gotValue;
    
    /**
     * Stores closureValue value of entity.
     */
    @Column(name = "closure_value")
    private String                  closureValue;
    
    /**
     * Stores programmedValueUpdate value of entity.
     */
    @Column(name = "programmed_value_update")
    private String                  programmedValueUpdate;
    
    @Column (name="target_value")
    private String					targetValue;
    
    @Column (name="unit_value")
    private String					unitValue;
    
    @Column (name="asse1")
    private String					asse1;
    
    @Column (name="asse2")
    private String					asse2;    
    
    @Column (name="asse3")
    private String					asse3;    
    
    @Column (name="asse4")
    private String					asse4;    
    
    @Column (name="asse5")
    private String					asse5;    
    
    @Column (name="asse6")
    private String					asse6;    
    
    @Column (name="asse7")
    private String					asse7;    
    
    @Column (name="asse8")
    private String					asse8;    
    
    @Column (name="asse9")
    private String					asse9;    
    
    @Column (name="asse10")
    private String					asse10;                         
    
    /**
     * Sets phisicalInizialization
     * 
     * @param phisicalInizialization
     *            the phisicalInizialization to set
     */
    public void setPhisicalInitialization(
            PhisicalInitializations phisicalInizialization)
    {
        this.phisicalInitialization = phisicalInizialization;
    }
    
    /**
     * Gets phisicalInizialization
     * 
     * @return phisicalInizialization the phisicalInizialization
     */
    public PhisicalInitializations getPhisicalInitialization()
    {
        return phisicalInitialization;
    }
    
    /**
     * Sets programmedValueUpdate
     * 
     * @param programmedValueUpdate
     *            the programmedValueUpdate to set
     */
    public void setProgrammedValueUpdate(String programmedValueUpdate)
    {
        this.programmedValueUpdate = programmedValueUpdate;
    }
    
    /**
     * Gets programmedValueUpdate
     * 
     * @return programmedValueUpdate the programmedValueUpdate
     */
    public String getProgrammedValueUpdate()
    {
        return programmedValueUpdate;
    }
    
    /**
     * Sets programmedValues
     * 
     * @param programmedValues
     *            the programmedValues to set
     */
    public void setProgrammedValues(String programmedValues)
    {
        this.programmedValues = programmedValues;
    }
    
    /**
     * Gets programmedValues
     * 
     * @return programmedValues the programmedValues
     */
    public String getProgrammedValues()
    {
        return programmedValues;
    }
    
    /**
     * Sets engageValue
     * 
     * @param engageValue
     *            the engageValue to set
     */
    public void setEngageValue(String engageValue)
    {
        this.engageValue = engageValue;
    }
    
    /**
     * Gets engageValue
     * 
     * @return engageValue the engageValue
     */
    public String getEngageValue()
    {
        return engageValue;
    }
    
    /**
     * Sets gotValue
     * 
     * @param gotValue
     *            the gotValue to set
     */
    public void setGotValue(String gotValue)
    {
        this.gotValue = gotValue;
    }
    
    /**
     * Gets gotValue
     * 
     * @return gotValue the gotValue
     */
    public String getGotValue()
    {
        return gotValue;
    }
    
    /**
     * Sets closureValue
     * 
     * @param closureValue
     *            the closureValue to set
     */
    public void setClosureValue(String closureValue)
    {
        this.closureValue = closureValue;
    }
    
    /**
     * Gets closureValue
     * 
     * @return closureValue the closureValue
     */
    public String getClosureValue()
    {
        return closureValue;
    }

	/**
	 * Gets targetValue
	 * @return targetValue the targetValue
	 */
	public String getTargetValue()
	{
		return targetValue;
	}

	/**
	 * Sets targetValue
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(String targetValue)
	{
		this.targetValue = targetValue;
	}

	/**
	 * Gets unitValue
	 * @return unitValue the unitValue
	 */
	public String getUnitValue()
	{
		return unitValue;
	}

	/**
	 * Sets unitValue
	 * @param unitValue the unitValue to set
	 */
	public void setUnitValue(String unitValue)
	{
		this.unitValue = unitValue;
	}

	/**
	 * Gets asse1
	 * @return asse1 the asse1
	 */
	public String getAsse1()
	{
		return asse1;
	}

	/**
	 * Sets asse1
	 * @param asse1 the asse1 to set
	 */
	public void setAsse1(String asse1)
	{
		this.asse1 = asse1;
	}

	/**
	 * Gets asse2
	 * @return asse2 the asse2
	 */
	public String getAsse2()
	{
		return asse2;
	}

	/**
	 * Sets asse2
	 * @param asse2 the asse2 to set
	 */
	public void setAsse2(String asse2)
	{
		this.asse2 = asse2;
	}

	/**
	 * Gets asse3
	 * @return asse3 the asse3
	 */
	public String getAsse3()
	{
		return asse3;
	}

	/**
	 * Sets asse3
	 * @param asse3 the asse3 to set
	 */
	public void setAsse3(String asse3)
	{
		this.asse3 = asse3;
	}

	/**
	 * Gets asse4
	 * @return asse4 the asse4
	 */
	public String getAsse4()
	{
		return asse4;
	}

	/**
	 * Sets asse4
	 * @param asse4 the asse4 to set
	 */
	public void setAsse4(String asse4)
	{
		this.asse4 = asse4;
	}

	/**
	 * Gets asse5
	 * @return asse5 the asse5
	 */
	public String getAsse5()
	{
		return asse5;
	}

	/**
	 * Sets asse5
	 * @param asse5 the asse5 to set
	 */
	public void setAsse5(String asse5)
	{
		this.asse5 = asse5;
	}

	/**
	 * Gets asse6
	 * @return asse6 the asse6
	 */
	public String getAsse6()
	{
		return asse6;
	}

	/**
	 * Sets asse6
	 * @param asse6 the asse6 to set
	 */
	public void setAsse6(String asse6)
	{
		this.asse6 = asse6;
	}

	/**
	 * Gets asse7
	 * @return asse7 the asse7
	 */
	public String getAsse7()
	{
		return asse7;
	}

	/**
	 * Sets asse7
	 * @param asse7 the asse7 to set
	 */
	public void setAsse7(String asse7)
	{
		this.asse7 = asse7;
	}

	/**
	 * Gets asse8
	 * @return asse8 the asse8
	 */
	public String getAsse8()
	{
		return asse8;
	}

	/**
	 * Sets asse8
	 * @param asse8 the asse8 to set
	 */
	public void setAsse8(String asse8)
	{
		this.asse8 = asse8;
	}

	/**
	 * Gets asse9
	 * @return asse9 the asse9
	 */
	public String getAsse9()
	{
		return asse9;
	}

	/**
	 * Sets asse9
	 * @param asse9 the asse9 to set
	 */
	public void setAsse9(String asse9)
	{
		this.asse9 = asse9;
	}

	/**
	 * Gets asse10
	 * @return asse10 the asse10
	 */
	public String getAsse10()
	{
		return asse10;
	}

	/**
	 * Sets asse10
	 * @param asse10 the asse10 to set
	 */
	public void setAsse10(String asse10)
	{
		this.asse10 = asse10;
	}
	
	
    
    
}
