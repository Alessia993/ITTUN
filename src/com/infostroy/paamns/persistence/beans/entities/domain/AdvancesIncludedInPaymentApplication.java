package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name="advances_included_in_payment_application")
public class AdvancesIncludedInPaymentApplication extends PersistentEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -604768224570706860L;

	@Column(name ="asse")
	private String asse;
	
	@Column(name ="amount_paid")
	private Double amountPaid;
	
	@Column(name ="amount_covered")
	private Double amountCovered;
	
	@Column(name ="amount_not_covered")
	private Double amountNotCovered;
	
	@ManyToOne
	@JoinColumn(name="payment_application_id")
	private PaymentApplication paymentApplication;
	
	public AdvancesIncludedInPaymentApplication(){}

	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getAmountCovered() {
		return amountCovered;
	}

	public void setAmountCovered(Double amountCovered) {
		this.amountCovered = amountCovered;
	}

	public Double getAmountNotCovered() {
		return amountNotCovered;
	}

	public void setAmountNotCovered(Double amountNotCovered) {
		this.amountNotCovered = amountNotCovered;
	}

	public PaymentApplication getPaymentApplication() {
		return paymentApplication;
	}

	public void setPaymentApplication(PaymentApplication paymentApplication) {
		this.paymentApplication = paymentApplication;
	}
	
	
}
