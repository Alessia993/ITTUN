/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2011.
 * 
 */
public class PaymentRequestWrapper
{
    private Integer paymentRequestNumber;
    
    private Date    paymentRequestDate;
    
    private Double  totalAmount;
    
    public PaymentRequestWrapper(Integer paymentRequestNumber,
            Date paymentRequestDate, Double totalAmount)
    {
        super();
        this.paymentRequestNumber = paymentRequestNumber;
        this.paymentRequestDate = paymentRequestDate;
        this.totalAmount = totalAmount;
    }
    
    public Integer getPaymentRequestNumber()
    {
        return paymentRequestNumber;
    }
    
    public void setPaymentRequestNumber(Integer paymentRequestNumber)
    {
        this.paymentRequestNumber = paymentRequestNumber;
    }
    
    public Date getPaymentRequestDate()
    {
        return paymentRequestDate;
    }
    
    public void setPaymentRequestDate(Date paymentRequestDate)
    {
        this.paymentRequestDate = paymentRequestDate;
    }
    
    public Double getTotalAmount()
    {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
}
