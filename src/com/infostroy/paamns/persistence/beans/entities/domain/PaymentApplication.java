package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;

@Entity
@Table(name = "payment_application")
public class PaymentApplication extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3824949776561669331L;

	@Column(name = "compilation_date")
	private Date compilationDate;
	
	@Column(name = "suspend_payment_date")
	private Date suspPaymentDate;
	
	@Column(name = "protocol_number")
	private String protocolNumber;

	@Column(name = "protocol_date")
	private Date protocolDate;

	@OneToOne
	@JoinColumn(name = "type")
	private RequestTypes requestType;

	@Column(name = "amount_state_aid_in_payment_application")
	private Double amountStateAidInPaymentApplication;

	@Column(name = "amount_eligible_expenses")
	private Double amountEligibleExpenses;

	@Column(name = "total_amount_of_public_expenditure")
	private Double totalAmountOfPublicExpenditure;

	@Column(name = "total_amount_cofinancing")
	private Double totalAmountCofinancing;
	
	@Column(name = "amount_cofinancing")
	private Double amountCofinancing;
	
	@OneToOne
	@JoinColumn(name = "payment_application_state_id")
	private ExpenditureDeclarationStates paymentApplicationState;

	// @OneToMany
	// private List<DurCompilations> durCompilations;
	@Transient
	private List<PaymentApplicationDurCompilations> paymentApplicationDurCompilations;

	@OneToMany(mappedBy = "paymentApplication")
	@JoinColumn(name = "advance")
	private List<AdvancesIncludedInPaymentApplication> advances;

	@Column(name = "accounting_period_from")
	private Date accountingPeriodFrom;

	@Column(name = "accounting_period_to")
	private Date accountingPeriodTo;

	@Column(name = "submission_date")
	private Date submissionDate;

	@Column(name = "total_Amount_of_certifications")
	private Double totalAmountOfCertifications;

	public PaymentApplication() {
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public Date getSuspPaymentDate() {
		return suspPaymentDate;
	}

	public void setSuspPaymentDate(Date suspPaymentDate) {
		this.suspPaymentDate = suspPaymentDate;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Date getProtocolDate() {
		return protocolDate;
	}

	public void setProtocolDate(Date protocolDate) {
		this.protocolDate = protocolDate;
	}

	public RequestTypes getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypes requestType) {
		this.requestType = requestType;
	}

	public Double getAmountStateAidInPaymentApplication() {
		return amountStateAidInPaymentApplication;
	}

	public void setAmountStateAidInPaymentApplication(Double amountStateAidInPaymentApplication) {
		this.amountStateAidInPaymentApplication = amountStateAidInPaymentApplication;
	}

	public Double getAmountEligibleExpenses() {
		return amountEligibleExpenses;
	}

	public void setAmountEligibleExpenses(Double amountEligibleExpenses) {
		this.amountEligibleExpenses = amountEligibleExpenses;
	}

	public Double getTotalAmountOfPublicExpenditure() {
		return totalAmountOfPublicExpenditure;
	}

	public void setTotalAmountOfPublicExpenditure(Double totalAmountOfPublicExpenditure) {
		this.totalAmountOfPublicExpenditure = totalAmountOfPublicExpenditure;
	}

	public Double getAmountCofinancing() {
		return amountCofinancing;
	}

	public void setAmountCofinancing(Double amountCofinancing) {
		this.amountCofinancing = amountCofinancing;
	}

	public Double getTotalAmountCofinancing() {
		return totalAmountCofinancing;
	}

	public void setTotalAmountCofinancing(Double totalAmountCofinancing) {
		this.totalAmountCofinancing = totalAmountCofinancing;
	}

	public ExpenditureDeclarationStates getPaymentApplicationState() {
		return paymentApplicationState;
	}

	public void setPaymentApplicationState(ExpenditureDeclarationStates paymentApplicationState) {
		this.paymentApplicationState = paymentApplicationState;
	}

	// public List<DurCompilations> getDurCompilations() {
	// return durCompilations;
	// }
	//
	// public void setDurCompilations(List<DurCompilations> durCompilations) {
	// this.durCompilations = durCompilations;
	// }

	public List<AdvancesIncludedInPaymentApplication> getAdvances() {
		return advances;
	}

	public void setAdvances(List<AdvancesIncludedInPaymentApplication> advances) {
		this.advances = advances;
	}

	public List<PaymentApplicationDurCompilations> getPaymentApplicationDurCompilations() {
		return paymentApplicationDurCompilations;
	}

	public void setPaymentApplicationDurCompilations(
			List<PaymentApplicationDurCompilations> paymentApplicationDurCompilations) {
		this.paymentApplicationDurCompilations = paymentApplicationDurCompilations;
	}

	public Date getAccountingPeriodFrom() {
		return accountingPeriodFrom;
	}

	public void setAccountingPeriodFrom(Date accountingPeriodFrom) {
		this.accountingPeriodFrom = accountingPeriodFrom;
	}

	public Date getAccountingPeriodTo() {
		return accountingPeriodTo;
	}

	public void setAccountingPeriodTo(Date accountingPeriodTo) {
		this.accountingPeriodTo = accountingPeriodTo;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Double getTotalAmountOfCertifications() {
		return totalAmountOfCertifications;
	}

	public void setTotalAmountOfCertifications(Double totalAmountOfCertifications) {
		this.totalAmountOfCertifications = totalAmountOfCertifications;
	}
}
