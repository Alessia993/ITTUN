package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name = "expenditures_of_payment_application")
public class ExpendituresOfPaymentApplication extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3055068196681779273L;

	@Column(name = "asse")
	private String asse;

	@Column(name = "total_amount_eligible_expenditure")
	private Double totalAmountEligibleExpenditure;

	@Column(name = "total_amount_public_expenditure")
	private Double totalAmountPublicExpenditure;

	@ManyToOne
	@JoinColumn(name = "payment_application_id")
	private PaymentApplication paymentApplication;

	public ExpendituresOfPaymentApplication() {
		super();
	}

	@Export(place = ExportPlaces.PaymentApplicationExpenditures, propertyName = "paymentApplicationEditAxis", seqXLS = 1, type = FieldTypes.STRING)
	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	@Export(place = ExportPlaces.PaymentApplicationExpenditures, propertyName = "paymentApplicationEditTotalAmountEligibleOfBeneficiaries", seqXLS = 2, type = FieldTypes.MONEY)
	public Double getTotalAmountEligibleExpenditure() {
		return totalAmountEligibleExpenditure;
	}

	public void setTotalAmountEligibleExpenditure(Double totalAmountEligibleExpenditure) {
		this.totalAmountEligibleExpenditure = totalAmountEligibleExpenditure;
	}

	@Export(place = ExportPlaces.PaymentApplicationExpenditures, propertyName = "paymentApplicationEditTotalAmountPublicOfBeneficiaries", seqXLS = 3, type = FieldTypes.MONEY)
	public Double getTotalAmountPublicExpenditure() {
		return totalAmountPublicExpenditure;
	}

	public void setTotalAmountPublicExpenditure(Double totalAmountPublicExpenditure) {
		this.totalAmountPublicExpenditure = totalAmountPublicExpenditure;
	}

	public PaymentApplication getPaymentApplication() {
		return paymentApplication;
	}

	public void setPaymentApplication(PaymentApplication paymentApplication) {
		this.paymentApplication = paymentApplication;
	}

}
