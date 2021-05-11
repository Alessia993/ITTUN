/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2011.
 *
 */
@Entity
@Table(name = "recoveries")
public class Recoveries extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    @Column(name = "amount_recovered")
    private Double          amountRecovered;
    
    @Column(name = "date_amount_recovered")
    private Date            dateAmountRecovered;
    
    @Column(name = "reason_of_recovery")
    private String          reasonOfRecovery;
    
    @ManyToOne()
    @JoinColumn(name = "cost_definition_id")
    private CostDefinitions costDefinition;
    
    public Double getAmountRecovered()
    {
        return amountRecovered;
    }
    
    public void setAmountRecovered(Double amountRecovered)
    {
        this.amountRecovered = amountRecovered;
    }
    
    public Date getDateAmountRecovered()
    {
        return dateAmountRecovered;
    }
    
    public void setDateAmountRecovered(Date dateAmountRecovered)
    {
        this.dateAmountRecovered = dateAmountRecovered;
    }
    
    public String getReasonOfRecovery()
    {
        return reasonOfRecovery;
    }
    
    public void setReasonOfRecovery(String reasonOfRecovery)
    {
        this.reasonOfRecovery = reasonOfRecovery;
    }
    
    public CostDefinitions getCostDefinition()
    {
        return costDefinition;
    }
    
    public void setCostDefinition(CostDefinitions costDefinition)
    {
        this.costDefinition = costDefinition;
    }
    
}
