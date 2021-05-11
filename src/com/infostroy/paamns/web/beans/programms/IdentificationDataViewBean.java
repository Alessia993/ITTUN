/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.Date;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.IdentificationData;
import com.infostroy.paamns.web.beans.EntityViewBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class IdentificationDataViewBean extends
        EntityViewBean<IdentificationData>
{
    
    /**
     * Stores description value of entity.
     */
    private String description;
    
    /**
     * Stores cciCode value of entity.
     */
    private String cciCode;
    
    /**
     * Stores approvalAct value of entity.
     */
    private String approvalAct;
    
    /**
     * Stores approvalActNumber value of entity.
     */
    private String approvalActNumber;
    
    /**
     * Stores approvalDate value of entity.
     */
    private Date   approvalDate;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
     */
    @Override
    public void Page_Load() throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(BeansFactory.IdentificationData().Load());
        if (getList().size() != 0)
        {
            IdentificationData item = getList().get(0);
            this.setApprovalAct(item.getApprovalAct());
            this.setApprovalActNumber(item.getApprovalActNumber());
            this.setApprovalDate(item.getApprovalDate());
            this.setCciCode(item.getCciCode());
            this.setDescription(item.getDescription());
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets cciCode
     * @param cciCode the cciCode to set
     */
    public void setCciCode(String cciCode)
    {
        this.cciCode = cciCode;
    }
    
    /**
     * Gets cciCode
     * @return cciCode the cciCode
     */
    public String getCciCode()
    {
        return cciCode;
    }
    
    /**
     * Sets approvalAct
     * @param approvalAct the approvalAct to set
     */
    public void setApprovalAct(String approvalAct)
    {
        this.approvalAct = approvalAct;
    }
    
    /**
     * Gets approvalAct
     * @return approvalAct the approvalAct
     */
    public String getApprovalAct()
    {
        return approvalAct;
    }
    
    /**
     * Sets approvalActNumber
     * @param approvalActNumber the approvalActNumber to set
     */
    public void setApprovalActNumber(String approvalActNumber)
    {
        this.approvalActNumber = approvalActNumber;
    }
    
    /**
     * Gets approvalActNumber
     * @return approvalActNumber the approvalActNumber
     */
    public String getApprovalActNumber()
    {
        return approvalActNumber;
    }
    
    /**
     * Sets approvalDate
     * @param approvalDate the approvalDate to set
     */
    public void setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
    }
    
    /**
     * Gets approvalDate
     * @return approvalDate the approvalDate
     */
    public Date getApprovalDate()
    {
        return approvalDate;
    }
    
}
