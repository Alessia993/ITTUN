/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;

import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class DurReimbursementWrapper extends DurReimbursements
{
    private Integer number;
    
    private Date    compDate;
    
    private String  protocolNumber;
    
    public DurReimbursementWrapper(DurReimbursements dr, DurInfos di,
            DurCompilations dc)
    {
        this.setId(dc.getId());
        this.setNumber(di.getDurInfoNumber());
        this.setCompDate(di.getDurCompilationDate());
        this.setProtocolNumber(di.getProtocolNumber());
        this.setDurCompilation(dc);
    }
    
    /**
     * Sets number
     * @param number the number to set
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }
    
    /**
     * Gets number
     * @return number the number
     */
    public Integer getNumber()
    {
        return number;
    }
    
    /**
     * Sets compDate
     * @param compDate the compDate to set
     */
    public void setCompDate(Date compDate)
    {
        this.compDate = compDate;
    }
    
    /**
     * Gets compDate
     * @return compDate the compDate
     */
    public Date getCompDate()
    {
        return compDate;
    }
    
    /**
     * Sets protocolNumber
     * @param protocolNumber the protocolNumber to set
     */
    public void setProtocolNumber(String protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Gets protocolNumber
     * @return protocolNumber the protocolNumber
     */
    public String getProtocolNumber()
    {
        return protocolNumber;
    }
}
