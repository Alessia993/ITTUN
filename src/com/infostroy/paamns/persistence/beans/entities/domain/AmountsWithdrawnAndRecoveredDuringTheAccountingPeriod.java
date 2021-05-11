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
@Table(name = "amounts_withdrawn_and_recovered_during_the_accounting_period")
public class AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5318096091161759037L;

	@Column(name = "asse")
	private String asse;

	@Column(name = "withdraw_eligible_expenditure")
	private Double withdrawEligibleExpenditure;

	@Column(name = "withdraw_public_spending")
	private Double withdrawPublicSpending;

	@Column(name = "recovery_eligible_expenditure")
	private Double recoveryEligibleExpenditure;

	@Column(name = "recovery_public_spending")
	private Double recoveryPublicSpending;

	@ManyToOne
	@JoinColumn(name = "closing_of_accounts_id")
	private ClosingAccounts closingOfAccounts;

	public AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod() {
	}

	@Export(propertyName = "closingOfAccountsEditRegistredAmountAxis", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod)
	public String getAsse() {
		return asse;
	}

	public void setAsse(String asse) {
		this.asse = asse;
	}
	
	@Export(propertyName = "closingOfAccountsEditAmountsWithdrawnAndRecoveredWithdrawEligibleExpenditure", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod)
	public Double getWithdrawEligibleExpenditure() {
		return withdrawEligibleExpenditure;
	}

	public void setWithdrawEligibleExpenditure(Double withdrawEligibleExpenditure) {
		this.withdrawEligibleExpenditure = withdrawEligibleExpenditure;
	}

	@Export(propertyName = "closingOfAccountsEditAmountsWithdrawnAndRecoveredWithdrawPublicSpending", seqXLS = 2, type = FieldTypes.MONEY, place = ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod)
	public Double getWithdrawPublicSpending() {
		return withdrawPublicSpending;
	}

	public void setWithdrawPublicSpending(Double withdrawPublicSpending) {
		this.withdrawPublicSpending = withdrawPublicSpending;
	}

	@Export(propertyName = "closingOfAccountsEditAmountsWithdrawnAndRecoveredRecoveryEligibleExpenditure", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod)
	public Double getRecoveryEligibleExpenditure() {
		return recoveryEligibleExpenditure;
	}

	public void setRecoveryEligibleExpenditure(Double recoveryEligibleExpenditure) {
		this.recoveryEligibleExpenditure = recoveryEligibleExpenditure;
	}

	@Export(propertyName = "closingOfAccountsEditAmountsWithdrawnAndRecoveredRecoveryPublicSpending", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod)
	public Double getRecoveryPublicSpending() {
		return recoveryPublicSpending;
	}

	public void setRecoveryPublicSpending(Double recoveryPublicSpending) {
		this.recoveryPublicSpending = recoveryPublicSpending;
	}

	public ClosingAccounts getClosingOfAccounts() {
		return closingOfAccounts;
	}

	public void setClosingOfAccounts(ClosingAccounts closingOfAccounts) {
		this.closingOfAccounts = closingOfAccounts;
	}

}
