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
import javax.persistence.Transient;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FesrQuotaTypes;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "fesr_info")
public class FESRInfo extends PersistentEntity
{
    /**
     * Stores durReimbursements value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "dur_reimbursements_id")
    private DurReimbursements  durReimbursements;
    
    /**
     * Stores fesrReimbursmentNumber value of entity.
     */
    
    @Column(name = "fesr_reimbursment_number")
    private Integer            fesrReimbursmentNumber;
    
    /**
     * Stores fesrBfName value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "fesr_bf_id")
    private CFPartnerAnagraphs fesrBf;
    
    /**
     * Stores fesrTotalAmount value of entity.
     */
    @Column(name = "fesr_total_amount")
    private Double             fesrTotalAmount;
    
    /**
     * Stores fesrBfIban value of entity.
     */
    @Column(name = "fesr_bf_iban")
    private String             fesrBfIban;
    
    /**
     * Stores fesrPaymentAct value of entity.
     */
    @Column(name = "fesr_payment_act")
    private Date               fesrPaymentAct;
    
    /**
     * Stores fesrDate value of entity.
     */
    @Column(name = "fesr_date")
    private Date               fesrDate;
    
    /**
     * Stores fesrCro value of entity.
     */
    @Column(name = "fesr_cro")
    private String             fesrCro;
    
    @Column(name = "reimbursement_amount")
    private Double             reimbursementAmount;
    
    @Column(name = "mode_of_crediting_funds")
    private String             modeOfCreditingFunds;
    
    @Column(name = "number_mandate_payment")
    private String             numberMandatePayment;
    
    @Column(name = "number_settlement_act")
    private String             numberSettlementAct;
    
    @Column(name = "how_to_pay")
    private String             howToPay;
    
    @Column(name = "quota")
    private Long               quota;
    
    /**
     * Gets fesrReimbursmentNumber
     * @return fesrReimbursmentNumber the fesrReimbursmentNumber
     */
    @Export(propertyName = "durReimbNumber", seqXLS = 1, type = FieldTypes.NUMBER, place = ExportPlaces.DurReimbursementEdit)
    public Integer getFesrReimbursmentNumber()
    {
        return fesrReimbursmentNumber;
    }
    
    /**
     * Sets fesrReimbursmentNumber
     * @param fesrReimbursmentNumber the fesrReimbursmentNumber to set
     */
    public void setFesrReimbursmentNumber(Integer fesrReimbursmentNumber)
    {
        this.fesrReimbursmentNumber = fesrReimbursmentNumber;
    }
    
    /**
     * Gets fesrBf
     * @return fesrBf the fesrBf
     */
    public CFPartnerAnagraphs getFesrBf()
    {
        return fesrBf;
    }
    
    /**
     * Sets fesrBf
     * @param fesrBf the fesrBf to set
     */
    public void setFesrBf(CFPartnerAnagraphs fesrBf)
    {
        this.fesrBf = fesrBf;
    }
    
    @Export(propertyName = "durReimbBFNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEdit)
    public String getFesrBfName()
    {
        if (fesrBf != null)
        {
            return fesrBf.getName();
        }
        return "";
    }
    
    /**
     * Gets fesrTotalAmount
     * @return fesrTotalAmount the fesrTotalAmount
     */
    public Double getFesrTotalAmount()
    {
        return fesrTotalAmount;
    }
    
    /**
     * Sets fesrTotalAmount
     * @param fesrTotalAmount the fesrTotalAmount to set
     */
    public void setFesrTotalAmount(Double fesrTotalAmount)
    {
        this.fesrTotalAmount = fesrTotalAmount;
    }
    
    /**
     * Gets fesrBfIban
     * @return fesrBfIban the fesrBfIban
     */
    public String getFesrBfIban()
    {
        return fesrBfIban;
    }
    
    /**
     * Sets fesrBfIban
     * @param fesrBfIban the fesrBfIban to set
     */
    public void setFesrBfIban(String fesrBfIban)
    {
        this.fesrBfIban = fesrBfIban;
    }
    
    /**
     * Gets fesrPaymentAct
     * @return fesrPaymentAct the fesrPaymentAct
     */
    @Export(propertyName = "durReimbPaymentAct", seqXLS = 3, type = FieldTypes.DATE, place = ExportPlaces.DurReimbursementEdit)
    public Date getFesrPaymentAct()
    {
        return fesrPaymentAct;
    }
    
    /**
     * Sets fesrPaymentAct
     * @param fesrPaymentAct the fesrPaymentAct to set
     */
    public void setFesrPaymentAct(Date fesrPaymentAct)
    {
        this.fesrPaymentAct = fesrPaymentAct;
    }
    
    /**
     * Gets fesrDate
     * @return fesrDate the fesrDate
     */
    @Export(propertyName = "durReimbFesrDate", seqXLS = 5, type = FieldTypes.DATE, place = ExportPlaces.DurReimbursementEdit)
    public Date getFesrDate()
    {
        return fesrDate;
    }
    
    /**
     * Sets fesrDate
     * @param fesrDate the fesrDate to set
     */
    public void setFesrDate(Date fesrDate)
    {
        this.fesrDate = fesrDate;
    }
    
    /**
     * Gets fesrCro
     * @return fesrCro the fesrCro
     */
    public String getFesrCro()
    {
        return fesrCro;
    }
    
    /**
     * Sets fesrCro
     * @param fesrCro the fesrCro to set
     */
    public void setFesrCro(String fesrCro)
    {
        this.fesrCro = fesrCro;
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
     * Gets reimbursementAmount
     * @return reimbursementAmount the reimbursementAmount
     */
    @Export(propertyName = "durReimbursementAmount", seqXLS = 6, type = FieldTypes.MONEY, place = ExportPlaces.DurReimbursementEdit)
    public Double getReimbursementAmount()
    {
        return reimbursementAmount;
    }
    
    /**
     * Sets reimbursementAmount
     * @param reimbursementAmount the reimbursementAmount to set
     */
    public void setReimbursementAmount(Double reimbursementAmount)
    {
        this.reimbursementAmount = reimbursementAmount;
    }
    
    /**
     * Gets modeOfCreditingFunds
     * @return modeOfCreditingFunds the modeOfCreditingFunds
     */
    public String getModeOfCreditingFunds()
    {
        return modeOfCreditingFunds;
    }
    
    /**
     * Sets modeOfCreditingFunds
     * @param modeOfCreditingFunds the modeOfCreditingFunds to set
     */
    public void setModeOfCreditingFunds(String modeOfCreditingFunds)
    {
        this.modeOfCreditingFunds = modeOfCreditingFunds;
    }
    
    /**
     * Gets numberMandatePayment
     * @return numberMandatePayment the numberMandatePayment
     */
    public String getNumberMandatePayment()
    {
        return numberMandatePayment;
    }
    
    /**
     * Sets numberMandatePayment
     * @param numberMandatePayment the numberMandatePayment to set
     */
    public void setNumberMandatePayment(String numberMandatePayment)
    {
        this.numberMandatePayment = numberMandatePayment;
    }
    
    /**
     * Gets howToPay
     * @return howToPay the howToPay
     */
    public String getHowToPay()
    {
        return howToPay;
    }
    
    /**
     * Sets howToPay
     * @param howToPay the howToPay to set
     */
    public void setHowToPay(String howToPay)
    {
        this.howToPay = howToPay;
    }
    
    /**
     * Gets quota
     * @return quota the quota
     */
    public Long getQuota()
    {
        return quota;
    }
    
    /**
     * Sets quota
     * @param quota the quota to set
     */
    public void setQuota(Long quota)
    {
        this.quota = quota;
    }
    
    /**
     * Gets numberSettlementAct
     * @return numberSettlementAct the numberSettlementAct
     */
    @Export(propertyName = "durReimbNumberSettlementAct", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEdit)
    public String getNumberSettlementAct()
    {
        return numberSettlementAct;
    }
    
    /**
     * Sets numberSettlementAct
     * @param numberSettlementAct the numberSettlementAct to set
     */
    public void setNumberSettlementAct(String numberSettlementAct)
    {
        this.numberSettlementAct = numberSettlementAct;
    }
    
    @Transient
    @Export(propertyName = "durReimbQuota", seqXLS = 7, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEdit)
    public String getQuotaString()
    {
        if (this.quota == null)
        {
            return "";
        }
        else if (FesrQuotaTypes.QuotaContoCapitale.getId().equals(this.quota))
        {
            return Utils.getString("durReimbQuotaContoCapitale");
        }
        else if (FesrQuotaTypes.QuotaContoCorrente.getId().equals(this.quota))
        {
            return Utils.getString("durReimbQuotaContoCorrente");
        }
        return "";
    }
    
}
