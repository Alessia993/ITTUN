/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "identification_data")
public class IdentificationData extends LocalizableStaticEntity
{
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String description;
    
    /**
     * Stores cciCode value of entity.
     */
    @Column(name = "cci_code")
    private String cciCode;
    
    /**
     * Stores approvalAct value of entity.
     */
    @Column(name = "approval_act")
    private String approvalAct;
    
    /**
     * Stores approvalActNumber value of entity.
     */
    @Column(name = "approval_act_number")
    private String approvalActNumber;
    
    /**
     * Stores approvalDate value of entity.
     */
    @Column(name = "approval_date")
    private Date   approvalDate;
    
    /**
     * Sets description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, description));
    }
    
    /**
     * Gets descriptiosetLocalValue(getLocalParams(this,
     * LocaleSessionManager.getInstance
     * ().getInstanceBean().getCurrLocale().toString(), approvalDate)n
     * 
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
    
    /**
     * Sets cciCode
     * 
     * @param cciCode
     *            the cciCode to set
     */
    public void setCciCode(String cciCode)
    {
        this.cciCode = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.cciCode, cciCode));
    }
    
    /**
     * Gets cciCode
     * 
     * @return cciCode the cciCode
     */
    public String getCciCode()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.cciCode,
                null));
    }
    
    /**
     * Sets approvalAct
     * 
     * @param approvalAct
     *            the approvalAct to set
     */
    public void setApprovalAct(String approvalAct)
    {
        this.approvalAct = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.approvalAct, approvalAct));
    }
    
    /**
     * Gets approvalAct
     * 
     * @return approvalAct the approvalAct
     */
    public String getApprovalAct()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.approvalAct, null));
    }
    
    /**
     * Sets approvalActNumber
     * 
     * @param approvalActNumber
     *            the approvalActNumber to set
     */
    public void setApprovalActNumber(String approvalActNumber)
    {
        this.approvalActNumber = setLocalValue(getLocalParams(
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.approvalActNumber,
                approvalActNumber));
    }
    
    /**
     * Gets approvalActNumber
     * 
     * @return approvalActNumber the approvalActNumber
     */
    public String getApprovalActNumber()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.approvalActNumber, null));
    }
    
    /**
     * Sets approvalDate
     * 
     * @param approvalDate
     *            the approvalDate to set
     */
    public void setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
    }
    
    /**
     * Gets approvalDate
     * 
     * @return approvalDate the approvalDate
     */
    public Date getApprovalDate()
    {
        return approvalDate;
    }
}
