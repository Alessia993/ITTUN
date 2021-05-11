package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;

@Entity
@Table(name = "closing_accounts")
public class ClosingAccounts extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3325920485770278536L;

	@Column(name = "compilation_date")
	private Date compilationDate;

	@Column(name = "description")
	private String description;

	@Column(name = "reference_period")
	private Date referencePeriod;

	@Column(name = "final_expendses_for_proj_above_1mil")
	private Boolean finalExpendsesForProjAbove1Mil;

	@Column(name = "total_amount_of_eligible_expenses")
	private Double totalAmountOfEligibleExpenses;

	@Column(name = "total_amount_of_public_expenditure")
	private Double totalAmountOfPublicExpenditure;

	@Column(name = "total_amount_of_transfers_linked_to_payments_to_eligible_ex")
	private Double totalAmountOfTransfersLinkedToPaymentsToEligibleEx;

	@Column(name = "total_amount_of_eligible_expenditure_withdrawn")
	private Double totalAmountOfEligibleExpenditureWithdrawn;

	@Column(name = "total_amount_of_public_expenditure_withdrawn")
	private Double totalAmountOfPublicExpenditureWithdrawn;

	@Column(name = "total_amount_of_eligible_expenditure_recovered")
	private Double totalAmountOfEligibleExpenditureRecovered;

	@Column(name = "total_amount_of_public_expenditure_recovered")
	private Double totalAmountOfPublicExpenditureRecovered;

	@Column(name = "total_amount_of_eligible_expenditure_to_be_recovered")
	private Double totalAmountOfEligibleExpenditureToBeRecovered;

	@Column(name = "total_amount_of_public_expenditure_to_be_recovered")
	private Double totalAmountOfPublicExpenditureToBeRecovered;

	@Column(name = "tot_amount_eligible_expend_recovered_in_accounting_period")
	private Double totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;

	@Column(name = "tot_amount_public_expend_recovered_in_accounting_period")
	private Double totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;

	@Column(name = "tot_amount_eligible_expend_not_be_recovered")
	private Double totalAmountOfEligibleExpenditureThatCanNotBeRecovered;

	@Column(name = "tot_amount_public_expenditure_not_be_recovered")
	private Double totalAmountOfPublicExpenditureThatCanNotBeRecovered;

	@OneToOne
	@JoinColumn(name = "state_id")
	private ExpenditureDeclarationStates state;

	@OneToMany
	@JoinColumn(name = "registred_amount")
	private List<RegistredAmount> registredAmountList;

	@OneToMany
	@JoinColumn(name = "amounts_withdrawn_and_recovered")
	private List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> amountsWithdrawnAndRecoveredDuringTheAccountingPeriod;

	@OneToMany
	@JoinColumn(name = "amounts_to_be_recovered_at_the_close")
	private List<AmountsToBeRecoveredAtTheClose> amountsToBeRecoveredAtTheClose;

	@OneToMany
	@JoinColumn(name = "unrecoverable_amounts_at_the_close")
	private List<UnrecoverableAmountsAtTheClose> unrecoverableAmountsAtTheClose;

	public ClosingAccounts() {
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getReferencePeriod() {
		return referencePeriod;
	}

	public void setReferencePeriod(Date referencePeriod) {
		this.referencePeriod = referencePeriod;
	}

	public Boolean getFinalExpendsesForProjAbove1Mil() {
		return finalExpendsesForProjAbove1Mil;
	}

	public void setFinalExpendsesForProjAbove1Mil(Boolean finalExpendsesForProjAbove1Mil) {
		this.finalExpendsesForProjAbove1Mil = finalExpendsesForProjAbove1Mil;
	}

	public Double getTotalAmountOfEligibleExpenses() {
		return totalAmountOfEligibleExpenses;
	}

	public void setTotalAmountOfEligibleExpenses(Double totalAmountOfEligibleExpenses) {
		this.totalAmountOfEligibleExpenses = totalAmountOfEligibleExpenses;
	}

	public Double getTotalAmountOfPublicExpenditure() {
		return totalAmountOfPublicExpenditure;
	}

	public void setTotalAmountOfPublicExpenditure(Double totalAmountOfPublicExpenditure) {
		this.totalAmountOfPublicExpenditure = totalAmountOfPublicExpenditure;
	}

	public Double getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx() {
		return totalAmountOfTransfersLinkedToPaymentsToEligibleEx;
	}

	public void setTotalAmountOfTransfersLinkedToPaymentsToEligibleEx(
			Double totalAmountOfTransfersLinkedToPaymentsToEligibleEx) {
		this.totalAmountOfTransfersLinkedToPaymentsToEligibleEx = totalAmountOfTransfersLinkedToPaymentsToEligibleEx;
	}

	public Double getTotalAmountOfEligibleExpenditureWithdrawn() {
		return totalAmountOfEligibleExpenditureWithdrawn;
	}

	public void setTotalAmountOfEligibleExpenditureWithdrawn(Double totalAmountOfEligibleExpenditureWithdrawn) {
		this.totalAmountOfEligibleExpenditureWithdrawn = totalAmountOfEligibleExpenditureWithdrawn;
	}

	public Double getTotalAmountOfPublicExpenditureWithdrawn() {
		return totalAmountOfPublicExpenditureWithdrawn;
	}

	public void setTotalAmountOfPublicExpenditureWithdrawn(Double totalAmountOfPublicExpenditureWithdrawn) {
		this.totalAmountOfPublicExpenditureWithdrawn = totalAmountOfPublicExpenditureWithdrawn;
	}

	public Double getTotalAmountOfEligibleExpenditureRecovered() {
		return totalAmountOfEligibleExpenditureRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureRecovered(Double totalAmountOfEligibleExpenditureRecovered) {
		this.totalAmountOfEligibleExpenditureRecovered = totalAmountOfEligibleExpenditureRecovered;
	}

	public Double getTotalAmountOfPublicExpenditureRecovered() {
		return totalAmountOfPublicExpenditureRecovered;
	}

	public void setTotalAmountOfPublicExpenditureRecovered(Double totalAmountOfPublicExpenditureRecovered) {
		this.totalAmountOfPublicExpenditureRecovered = totalAmountOfPublicExpenditureRecovered;
	}

	public Double getTotalAmountOfEligibleExpenditureToBeRecovered() {
		return totalAmountOfEligibleExpenditureToBeRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureToBeRecovered(Double totalAmountOfEligibleExpenditureToBeRecovered) {
		this.totalAmountOfEligibleExpenditureToBeRecovered = totalAmountOfEligibleExpenditureToBeRecovered;
	}

	public Double getTotalAmountOfPublicExpenditureToBeRecovered() {
		return totalAmountOfPublicExpenditureToBeRecovered;
	}

	public void setTotalAmountOfPublicExpenditureToBeRecovered(Double totalAmountOfPublicExpenditureToBeRecovered) {
		this.totalAmountOfPublicExpenditureToBeRecovered = totalAmountOfPublicExpenditureToBeRecovered;
	}

	public Double getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod() {
		return totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;
	}

	public void setTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod(
			Double totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod) {
		this.totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod = totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;
	}

	public Double getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod() {
		return totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;
	}

	public void setTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod(
			Double totalAmountOfPublicExpenditureRecoveredInAccountingPeriod) {
		this.totalAmountOfPublicExpenditureRecoveredInAccountingPeriod = totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;
	}

	public Double getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered() {
		return totalAmountOfEligibleExpenditureThatCanNotBeRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureThatCanNotBeRecovered(
			Double totalAmountOfEligibleExpenditureThatCanNotBeRecovered) {
		this.totalAmountOfEligibleExpenditureThatCanNotBeRecovered = totalAmountOfEligibleExpenditureThatCanNotBeRecovered;
	}

	public Double getTotalAmountOfPublicExpenditureThatCanNotBeRecovered() {
		return totalAmountOfPublicExpenditureThatCanNotBeRecovered;
	}

	public void setTotalAmountOfPublicExpenditureThatCanNotBeRecovered(
			Double totalAmountOfPublicExpenditureThatCanNotBeRecovered) {
		this.totalAmountOfPublicExpenditureThatCanNotBeRecovered = totalAmountOfPublicExpenditureThatCanNotBeRecovered;
	}

	public ExpenditureDeclarationStates getState() {
		return state;
	}

	public void setState(ExpenditureDeclarationStates state) {
		this.state = state;
	}

	public List<RegistredAmount> getRegistredAmountList() {
		return registredAmountList;
	}

	public void setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod(List<RegistredAmount> registredAmountList) {
		this.registredAmountList = registredAmountList;
	}

	public List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod() {
		return amountsWithdrawnAndRecoveredDuringTheAccountingPeriod;
	}

	public void setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod(
			List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> amountsWithdrawnAndRecoveredDuringTheAccountingPeriod) {
		this.amountsWithdrawnAndRecoveredDuringTheAccountingPeriod = amountsWithdrawnAndRecoveredDuringTheAccountingPeriod;
	}

	public List<AmountsToBeRecoveredAtTheClose> getAmountsToBeRecoveredAtTheClose() {
		return amountsToBeRecoveredAtTheClose;
	}

	public void setAmountsToBeRecoveredAtTheClose(List<AmountsToBeRecoveredAtTheClose> amountsToBeRecoveredAtTheClose) {
		this.amountsToBeRecoveredAtTheClose = amountsToBeRecoveredAtTheClose;
	}

	public List<UnrecoverableAmountsAtTheClose> getUnrecoverableAmountsAtTheClose() {
		return unrecoverableAmountsAtTheClose;
	}

	public void setUnrecoverableAmountsAtTheClose(List<UnrecoverableAmountsAtTheClose> unrecoverableAmountsAtTheClose) {
		this.unrecoverableAmountsAtTheClose = unrecoverableAmountsAtTheClose;
	}

	public void setRegistredAmountList(List<RegistredAmount> registredAmountList) {
		this.registredAmountList = registredAmountList;
	}

}
