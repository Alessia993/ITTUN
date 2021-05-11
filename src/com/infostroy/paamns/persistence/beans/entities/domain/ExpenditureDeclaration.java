package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.ArrayList;
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
@Table(name = "expenditure_declaration")
public class ExpenditureDeclaration extends PersistentEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1442604506376463286L;

	public ExpenditureDeclaration() {
	}

	@Column(name = "compilation_date")
	private Date compilationDate;

	@Column(name = "protocol_number")
	private String protocolNumber;

	@Column(name = "protocol_date")
	private Date protocolDate;

	@Column(name = "accounting_period_from")
	private Date accountingPeriodFrom;

	@Column(name = "accounting_period_to")
	private Date accountingPeriodTo;

	// @Column(name = "expenditure_increase")
	// private Double expenditureIncrease;

	@Column(name = "tot_amount_eligible_expenditure")
	private Double totAmountEligibleExpenditure;

	@Column(name = "tot_amount_public_expenditure")
	private Double totAmountPublicExpenditure;

	@Column(name = "tot_amount_private_expenditure")
	private Double totAmountPrivateExpenditure;

	@Column(name = "tot_amount_state_aid")
	private Double totAmountStateAid;

	@Column(name = "tot_amount_coverage_advance_aid")
	private Double totAmountCoverageAdvanceAid;

	@Column(name = "tot_contribution_in_kind_pub_share")
	private Double totContributionInKindPubShare;

	@Column(name = "total_expenditure")
	private Double totalExpenditure;

	// @OneToMany
	// private List<DurCompilations> durCompilations;
	@Transient
	private List<ExpenditureDeclarationDurCompilations> expenditureDeclarationDurCompilations;

	@OneToOne
	@JoinColumn(name = "expenditure_declaration_state_id")
	private ExpenditureDeclarationStates expenditureDeclarationState;

	@OneToOne
	@JoinColumn(name = "type")
	private RequestTypes requestType;

	@Column(name = "to_be_trasmitted")
	private Boolean toBeTrasmitted;

	@Column(name = "submission_date")
	private Date submissionDate;

	// public List<DurCompilations> getDurCompilations() {
	// return durCompilations;
	// }
	//
	// public void setDurCompilations(List<DurCompilations> durCompilations) {
	// this.durCompilations = durCompilations;
	// }

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public RequestTypes getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestTypes requestType) {
		this.requestType = requestType;
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

	// public List<String> getType() {
	// return type;
	// }
	//
	// public void setType(List<String> type) {
	// this.type = type;
	// }

	// public Double getExpenditureIncrease() {
	// return expenditureIncrease;
	// }
	//
	// public void setExpenditureIncrease(Double expenditureIncrease) {
	// this.expenditureIncrease = expenditureIncrease;
	// }

	public Double getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(Double totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public ExpenditureDeclarationStates getExpenditureDeclarationState() {
		return expenditureDeclarationState;
	}

	public void setExpenditureDeclarationState(ExpenditureDeclarationStates expenditureDeclarationState) {
		this.expenditureDeclarationState = expenditureDeclarationState;
	}

	public Boolean getToBeTrasmitted() {
		return toBeTrasmitted;
	}

	public void setToBeTrasmitted(Boolean toBeTrasmitted) {
		this.toBeTrasmitted = toBeTrasmitted;
	}

	public Double getTotAmountEligibleExpenditure() {
		return totAmountEligibleExpenditure;
	}

	public void setTotAmountEligibleExpenditure(Double totAmountEligibleExpenditure) {
		this.totAmountEligibleExpenditure = totAmountEligibleExpenditure;
	}

	public Double getTotAmountPublicExpenditure() {
		return totAmountPublicExpenditure;
	}

	public void setTotAmountPublicExpenditure(Double totAmountPublicExpenditure) {
		this.totAmountPublicExpenditure = totAmountPublicExpenditure;
	}

	public Double getTotAmountPrivateExpenditure() {
		return totAmountPrivateExpenditure;
	}

	public void setTotAmountPrivateExpenditure(Double totAmountPrivateExpenditure) {
		this.totAmountPrivateExpenditure = totAmountPrivateExpenditure;
	}

	public Double getTotAmountStateAid() {
		return totAmountStateAid;
	}

	public void setTotAmountStateAid(Double totAmountStateAid) {
		this.totAmountStateAid = totAmountStateAid;
	}

	public Double getTotAmountCoverageAdvanceAid() {
		return totAmountCoverageAdvanceAid;
	}

	public void setTotAmountCoverageAdvanceAid(Double totAmountCoverageAdvanceAid) {
		this.totAmountCoverageAdvanceAid = totAmountCoverageAdvanceAid;
	}

	public Double getTotContributionInKindPubShare() {
		return totContributionInKindPubShare;
	}

	public void setTotContributionInKindPubShare(Double totContributionInKindPubShare) {
		this.totContributionInKindPubShare = totContributionInKindPubShare;
	}

	public List<ExpenditureDeclarationDurCompilations> getExpenditureDeclarationDurCompilations() {
		return expenditureDeclarationDurCompilations;
	}

	public void setExpenditureDeclarationDurCompilations(
			List<ExpenditureDeclarationDurCompilations> expenditureDeclarationDurCompilations) {
		this.expenditureDeclarationDurCompilations = expenditureDeclarationDurCompilations;
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

	public Object clone() {
		ExpenditureDeclaration ed = null;
		try {
			ed = (ExpenditureDeclaration) super.clone();
			ed.expenditureDeclarationDurCompilations = new ArrayList<>();
			for (ExpenditureDeclarationDurCompilations eddc : this.expenditureDeclarationDurCompilations) {
				ed.expenditureDeclarationDurCompilations.add((ExpenditureDeclarationDurCompilations) eddc.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return ed;
	}

}
