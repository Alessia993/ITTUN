/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2012.
 * 
 */

@Entity
@Table(name = "payment_request")
public class PaymentRequest extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	@Column()
	private Integer						number;

	@Column(name = "payment_request_date")
	private Date						paymentRequestDate;

	@Column(name = "total_amount")
	private Double						totalAmount;

	@Transient
	private Double						transientTotalAmount;

	@OneToMany(mappedBy = "paymentRequest")
	private List<PaymentRequestItem>	items;

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
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

	public List<PaymentRequestItem> getItems()
	{
		return items;
	}

	public void setItems(List<PaymentRequestItem> items)
	{
		this.items = items;
	}

	/**
	 * Gets transientTotalAmount
	 * 
	 * @return transientTotalAmount the transientTotalAmount
	 */
	public Double getTransientTotalAmount()
	{
		return transientTotalAmount;
	}

	/**
	 * Sets transientTotalAmount
	 * 
	 * @param transientTotalAmount
	 *            the transientTotalAmount to set
	 */
	public void setTransientTotalAmount(Double transientTotalAmount)
	{
		this.transientTotalAmount = transientTotalAmount;
	}

}
