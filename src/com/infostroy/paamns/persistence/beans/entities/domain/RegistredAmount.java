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
@Table(name = "registred_amount")
public class RegistredAmount extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4822506611847097476L;

	@Column(name = "asse")
	private String asse;

	@Column(name = "eligible_expenditure")
	private Double eligibleExpenditure;

	@Column(name = "public_spending")
	private Double publicSpending;

	@Column(name = "payments_made_to_the_beneficiaries")
	private Double paymentsMadeToTheBeneficiaries;

	@ManyToOne
	@JoinColumn(name = "closing_of_accounts_id")
	private ClosingAccounts closingOfAccounts;

	public RegistredAmount() {
	}

	@Export(propertyName = "closingOfAccountsEditRegistredAmountAxis", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.RegistredAmount)
	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	@Export(propertyName = "closingOfAccountsEditRegistredAmountEligibleExpenditure", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.RegistredAmount)
	public Double getEligibleExpenditure() {
		return eligibleExpenditure;
	}

	public void setEligibleExpenditure(Double eligibleExpenditure) {
		this.eligibleExpenditure = eligibleExpenditure;
	}

	@Export(propertyName = "closingOfAccountsEditRegistredAmountPublicSpending", seqXLS = 2, type = FieldTypes.MONEY, place = ExportPlaces.RegistredAmount)
	public Double getPublicSpending() {
		return publicSpending;
	}

	public void setPublicSpending(Double publicSpending) {
		this.publicSpending = publicSpending;
	}

	@Export(propertyName = "closingOfAccountsEditRegistredAmountPaymentsMadeToTheBeneficiaries", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.RegistredAmount)
	public Double getPaymentsMadeToTheBeneficiaries() {
		return paymentsMadeToTheBeneficiaries;
	}

	public void setPaymentsMadeToTheBeneficiaries(Double paymentsMadeToTheBeneficiaries) {
		this.paymentsMadeToTheBeneficiaries = paymentsMadeToTheBeneficiaries;
	}

	public ClosingAccounts getClosingOfAccounts() {
		return closingOfAccounts;
	}

	public void setClosingOfAccounts(ClosingAccounts closingOfAccounts) {
		this.closingOfAccounts = closingOfAccounts;
	}

	@Override
	public String toString() {
		return "RegistredAmount [asse=" + asse + ", eligibleExpenditure=" + eligibleExpenditure + ", publicSpending="
				+ publicSpending + ", paymentsMadeToTheBeneficiaries=" + paymentsMadeToTheBeneficiaries + "]";
	}
	
	

}
