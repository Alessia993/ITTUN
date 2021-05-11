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

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "rotation_found_info")
public class RotationFoundInfo extends PersistentEntity
{
    @ManyToOne
    @JoinColumn(name = "dur_reimbursements_id")
    private DurReimbursements  durReimbursements;
    
    /**
     * Stores rotationReimbursmentNumber value of entity.
     */
    @Column(name = "rotation_reimbursment_number")
    private Integer            rotationReimbursmentNumber;
    
    /**
     * Stores rotationPartnerName value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "rotation_partner_id")
    private CFPartnerAnagraphs rotationPartner;
    
    /**
     * Gets rotationReimbursmentNumber
     * @return rotationReimbursmentNumber the rotationReimbursmentNumber
     */
    public Integer getRotationReimbursmentNumber()
    {
        return rotationReimbursmentNumber;
    }
    
    /**
     * Sets rotationReimbursmentNumber
     * @param rotationReimbursmentNumber the rotationReimbursmentNumber to set
     */
    public void setRotationReimbursmentNumber(Integer rotationReimbursmentNumber)
    {
        this.rotationReimbursmentNumber = rotationReimbursmentNumber;
    }
    
    /**
     * Gets rotationPartner
     * @return rotationPartner the rotationPartner
     */
    public CFPartnerAnagraphs getRotationPartner()
    {
        return rotationPartner;
    }
    
    /**
     * Sets rotationPartner
     * @param rotationPartner the rotationPartner to set
     */
    public void setRotationPartner(CFPartnerAnagraphs rotationPartner)
    {
        this.rotationPartner = rotationPartner;
    }
    
    /**
     * Gets rotationTotalAmount
     * @return rotationTotalAmount the rotationTotalAmount
     */
    public Double getRotationTotalAmount()
    {
        return rotationTotalAmount;
    }
    
    /**
     * Sets rotationTotalAmount
     * @param rotationTotalAmount the rotationTotalAmount to set
     */
    public void setRotationTotalAmount(Double rotationTotalAmount)
    {
        this.rotationTotalAmount = rotationTotalAmount;
    }
    
    /**
     * Gets rotationPartnerIban
     * @return rotationPartnerIban the rotationPartnerIban
     */
    public String getRotationPartnerIban()
    {
        return rotationPartnerIban;
    }
    
    /**
     * Sets rotationPartnerIban
     * @param rotationPartnerIban the rotationPartnerIban to set
     */
    public void setRotationPartnerIban(String rotationPartnerIban)
    {
        this.rotationPartnerIban = rotationPartnerIban;
    }
    
    /**
     * Gets rotationPaymentAct
     * @return rotationPaymentAct the rotationPaymentAct
     */
    public Date getRotationPaymentAct()
    {
        return rotationPaymentAct;
    }
    
    /**
     * Sets rotationPaymentAct
     * @param rotationPaymentAct the rotationPaymentAct to set
     */
    public void setRotationPaymentAct(Date rotationPaymentAct)
    {
        this.rotationPaymentAct = rotationPaymentAct;
    }
    
    /**
     * Gets rotationDate
     * @return rotationDate the rotationDate
     */
    public Date getRotationDate()
    {
        return rotationDate;
    }
    
    /**
     * Sets rotationDate
     * @param rotationDate the rotationDate to set
     */
    public void setRotationDate(Date rotationDate)
    {
        this.rotationDate = rotationDate;
    }
    
    /**
     * Gets rptationCro
     * @return rptationCro the rptationCro
     */
    public String getRptationCro()
    {
        return rptationCro;
    }
    
    /**
     * Sets rptationCro
     * @param rptationCro the rptationCro to set
     */
    public void setRptationCro(String rptationCro)
    {
        this.rptationCro = rptationCro;
    }
    
    /**
     * Sets durReimbursements
     * @param durReimbursements the durReimbursements to set
     */
    public void setDurReimbursements(DurReimbursements durReimbursements)
    {
        this.durReimbursements = durReimbursements;
    }
    
    /**
     * Gets durReimbursements
     * @return durReimbursements the durReimbursements
     */
    public DurReimbursements getDurReimbursements()
    {
        return durReimbursements;
    }
    
    /**
     * Stores rotationTotalAmount value of entity.
     */
    @Column(name = "rotation_total_amount")
    private Double rotationTotalAmount;
    
    /**
     * Stores rotationPartnerIban value of entity.
     */
    @Column(name = "rotation_partner_iban")
    private String rotationPartnerIban;
    
    /**
     * Stores rotationPaymentAct value of entity.
     */
    @Column(name = "rotation_payment_act")
    private Date   rotationPaymentAct;
    
    /**
     * Stores rotationDate value of entity.
     */
    @Column(name = "rotation_date")
    private Date   rotationDate;
    
    /**
     * Stores rptationCro value of entity.
     */
    @Column(name = "rptation_cro")
    private String rptationCro;
}
