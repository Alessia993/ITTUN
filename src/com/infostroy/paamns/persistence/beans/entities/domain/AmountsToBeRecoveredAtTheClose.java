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
@Table(name = "amoounts_to_be_recovered_at_the_close")
public class AmountsToBeRecoveredAtTheClose extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1517868704567929723L;

	@Column(name = "asse")
	private String asse;

	@Column(name = "eligible_expenditure")
	private Double eligibleExpenditure;

	@Column(name = "public_spending")
	private Double publicSpending;

	@ManyToOne
	@JoinColumn(name = "closing_of_accounts_id")
	private ClosingAccounts closingOfAccounts;

	public AmountsToBeRecoveredAtTheClose() {
	}
	
	@Export(propertyName = "closingOfAccountsEditAmountsToBeRecoveredAtTheCloseAxis", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.AmountsToBeRecoveredAtTheClose)
	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	@Export(propertyName = "closingOfAccountsEditAmountsToBeRecoveredAtTheCloseEligibleExpenditure", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.AmountsToBeRecoveredAtTheClose)
	public Double getEligibleExpenditure() {
		return eligibleExpenditure;
	}

	public void setEligibleExpenditure(Double eligibleExpenditure) {
		this.eligibleExpenditure = eligibleExpenditure;
	}

	@Export(propertyName = "closingOfAccountsEditAmountsToBeRecoveredAtTheClosePublicSpending", seqXLS = 2, type = FieldTypes.MONEY, place = ExportPlaces.AmountsToBeRecoveredAtTheClose)
	public Double getPublicSpending() {
		return publicSpending;
	}

	public void setPublicSpending(Double publicSpending) {
		this.publicSpending = publicSpending;
	}

	public ClosingAccounts getClosingOfAccounts() {
		return closingOfAccounts;
	}

	public void setClosingOfAccounts(ClosingAccounts closingOfAccounts) {
		this.closingOfAccounts = closingOfAccounts;
	}

}
