/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "non_regulary_situations")
public class NonRegularySituations extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 8929415125699607700L;
    
    /**
     * Stores costDefinition value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "cost_definition_id")
    private CostDefinitions   costDefinition;
    
    /**
     * Stores recoverAmount value of entity.
     */
    @Column(name = "recover_amount")
    private Double            recoverAmount;
    
    /**
     * Stores type value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private NotRegularTypes   type;
    
    /**
     * Stores responsible value of entity.
     */
    @Column
    private String            responsible;
    
    @Column(name = "dur_number")
    private Integer           durNumber;
    
    @Column(name = "irregular_amount")
    private Double            irregularAmount;
    
    @Column(name = "reference_year")
    private Integer           referenceYear;
    
    @Column
    private Boolean           certification;
    
    @ManyToOne
    @JoinColumn
    private Users             user;
    
    @Column
    private Date              date;
    
    @Column(name = "be_recovered")
    private boolean           beRecovered;
    
    /**
     * Sets costDefinition
     * @param costDefinition the costDefinition to set
     */
    public void setCostDefinition(CostDefinitions costDefinition)
    {
        this.costDefinition = costDefinition;
    }
    
    /**
     * Gets costDefinition
     * @return costDefinition the costDefinition
     */
    public CostDefinitions getCostDefinition()
    {
        return costDefinition;
    }
    
    /**
     * Sets recoverAmount
     * @param recoverAmount the recoverAmount to set
     */
    public void setRecoverAmount(Double recoverAmount)
    {
        this.recoverAmount = recoverAmount;
    }
    
    /**
     * Gets recoverAmount
     * @return recoverAmount the recoverAmount
     */
    public Double getRecoverAmount()
    {
        return recoverAmount;
    }
    
    /**
     * Sets type
     * @param type the type to set
     */
    public void setType(NotRegularTypes type)
    {
        this.type = type;
    }
    
    /**
     * Gets type
     * @return type the type
     */
    public NotRegularTypes getType()
    {
        return type;
    }
    
    /**
     * Sets responsible
     * @param responsible the responsible to set
     */
    public void setResponsible(String responsible)
    {
        this.responsible = responsible;
    }
    
    /**
     * Gets responsible
     * @return responsible the responsible
     */
    public String getResponsible()
    {
        return responsible;
    }
    
    /**
     * Gets durNumber
     * @return durNumber the durNumber
     */
    public Integer getDurNumber()
    {
        return durNumber;
    }
    
    /**
     * Sets durNumber
     * @param durNumber the durNumber to set
     */
    public void setDurNumber(Integer durNumber)
    {
        this.durNumber = durNumber;
    }
    
    /**
     * Gets irregularAmount
     * @return irregularAmount the irregularAmount
     */
    public Double getIrregularAmount()
    {
        return irregularAmount;
    }
    
    /**
     * Sets irregularAmount
     * @param irregularAmount the irregularAmount to set
     */
    public void setIrregularAmount(Double irregularAmount)
    {
        this.irregularAmount = irregularAmount;
    }
    
    /**
     * Gets referenceYear
     * @return referenceYear the referenceYear
     */
    public Integer getReferenceYear()
    {
        return referenceYear;
    }
    
    /**
     * Sets referenceYear
     * @param referenceYear the referenceYear to set
     */
    public void setReferenceYear(Integer referenceYear)
    {
        this.referenceYear = referenceYear;
    }
    
    /**
     * Gets certification
     * @return certification the certification
     */
    public Boolean getCertification()
    {
        return certification;
    }
    
    /**
     * Sets certification
     * @param certification the certification to set
     */
    public void setCertification(Boolean certification)
    {
        this.certification = certification;
    }
    
    /**
     * Gets user
     * @return user the user
     */
    public Users getUser()
    {
        return user;
    }
    
    /**
     * Sets user
     * @param user the user to set
     */
    public void setUser(Users user)
    {
        this.user = user;
    }
    
    /**
     * Gets date
     * @return date the date
     */
    public Date getDate()
    {
        return date;
    }
    
    /**
     * Sets date
     * @param date the date to set
     */
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    /**
     * Gets beRecovered
     * @return beRecovered the beRecovered
     */
    public boolean isBeRecovered()
    {
        return beRecovered;
    }
    
    /**
     * Sets beRecovered
     * @param beRecovered the beRecovered to set
     */
    public void setBeRecovered(boolean beRecovered)
    {
        this.beRecovered = beRecovered;
    }
    
    /**
     * Gets serialVersionUID
     * @return serialVersionUID the serialversionuid
     */
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
}
