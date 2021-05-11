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
@Table(name = "unrecoverable_amounts_at_the_close")
public class UnrecoverableAmountsAtTheClose extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7916682754570895037L;

	@Column(name = "asse")
	private String asse;

	@Column(name = "eligible_expenditure")
	private Double eligibleExpenditure;

	@Column(name = "public_spending")
	private Double publicSpending;

	@Column(name = "observations")
	private String observations;

	@ManyToOne
	@JoinColumn(name = "closing_of_accounts_id")
	private ClosingAccounts closingOfAccounts;

	public UnrecoverableAmountsAtTheClose() {
	}

	@Export(propertyName = "closingOfAccountsEditUnrecoverableAmountsAtTheCloseAxis", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.UnrecoverableAmountsAtTheClose)
	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}

	@Export(propertyName = "closingOfAccountsEditUnrecoverableAmountsAtTheCloseEligibleExpenditure", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.UnrecoverableAmountsAtTheClose)
	public Double getEligibleExpenditure() {
		return eligibleExpenditure;
	}

	public void setEligibleExpenditure(Double eligibleExpenditure) {
		this.eligibleExpenditure = eligibleExpenditure;
	}

	@Export(propertyName = "closingOfAccountsEditUnrecoverableAmountsAtTheClosePublicSpending", seqXLS = 2, type = FieldTypes.MONEY, place = ExportPlaces.UnrecoverableAmountsAtTheClose)
	public Double getPublicSpending() {
		return publicSpending;
	}

	public void setPublicSpending(Double publicSpending) {
		this.publicSpending = publicSpending;
	}

	@Export(propertyName = "closingOfAccountsEditUnrecoverableAmountsAtTheCloseObservations", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.UnrecoverableAmountsAtTheClose)
	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public ClosingAccounts getClosingOfAccounts() {
		return closingOfAccounts;
	}

	public void setClosingOfAccounts(ClosingAccounts closingOfAccounts) {
		this.closingOfAccounts = closingOfAccounts;
	}

}
