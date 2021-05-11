/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 * 
 * @author acagnoni Ingloba360 2019/03/20
 * 
 */
@Entity
@Table(name = "ce_payment_request")
public class CEPaymentRequest extends PersistentEntity {

	@Column(name = "annual_notification_commitment_date")
	private Date annualNotificationCommitmentDate;

	@Column(name = "annual_commitment_amount")
	private Double annualCommitmentAmount;

	@Column(name = "pre_financing_payment_request_date")
	private Date preFinancingPaymentRequestDate;

	@Column(name = "pre_financing_supporting_doc")
	private String preFinancingSupportingDoc;

	@Column(name = "pre_financing_requested_amount")
	private Double preFinancingRequestedAmount;

	@Column(name = "annual_report_submission_date")
	private Date annualReportSubmissionDate;

	@Column(name = "annual_report_doc")
	private String annualReportDoc;

	@Column(name = "accepted_tech_ass_certified_amount")
	private Double acceptedTechAssAmountCertified;

	@Column(name = "accepted_projects_expenditure_certified_amount")
	private Double acceptedProjectsExpenditureCertifiedAmount;

	@Column(name = "annual_eu_allocation_amount")
	private Double annualEUAllocationAmount;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2703923953889081749L;

	/**
	 * @return the annualNotificationCommitmentDate
	 */
	public Date getAnnualNotificationCommitmentDate() {
		return annualNotificationCommitmentDate;
	}

	/**
	 * @param annualNotificationCommitmentDate
	 *            the annualNotificationCommitmentDate to set
	 */
	public void setAnnualNotificationCommitmentDate(Date annualNotificationCommitmentDate) {
		this.annualNotificationCommitmentDate = annualNotificationCommitmentDate;
	}

	/**
	 * @return the annualCommitmentAmount
	 */
	public Double getAnnualCommitmentAmount() {
		return annualCommitmentAmount;
	}

	/**
	 * @param annualCommitmentAmount
	 *            the annualCommitmentAmount to set
	 */
	public void setAnnualCommitmentAmount(Double annualCommitmentAmount) {
		this.annualCommitmentAmount = annualCommitmentAmount;
	}

	/**
	 * @return the preFinancingPaymentRequestDate
	 */
	public Date getPreFinancingPaymentRequestDate() {
		return preFinancingPaymentRequestDate;
	}

	/**
	 * @param preFinancingPaymentRequestDate
	 *            the preFinancingPaymentRequestDate to set
	 */
	public void setPreFinancingPaymentRequestDate(Date preFinancingPaymentRequestDate) {
		this.preFinancingPaymentRequestDate = preFinancingPaymentRequestDate;
	}

	/**
	 * @return the preFinancingSupportingDoc
	 */
	public String getPreFinancingSupportingDoc() {
		return preFinancingSupportingDoc;
	}

	/**
	 * @param preFinancingSupportingDoc
	 *            the preFinancingSupportingDoc to set
	 */
	public void setPreFinancingSupportingDoc(String preFinancingSupportingDoc) {
		this.preFinancingSupportingDoc = preFinancingSupportingDoc;
	}

	/**
	 * @return the preFinancingRequestedAmount
	 */
	public Double getPreFinancingRequestedAmount() {
		return preFinancingRequestedAmount;
	}

	/**
	 * @param preFinancingRequestedAmount
	 *            the preFinancingRequestedAmount to set
	 */
	public void setPreFinancingRequestedAmount(Double preFinancingRequestedAmount) {
		this.preFinancingRequestedAmount = preFinancingRequestedAmount;
	}

	/**
	 * @return the annualReportSubmissionDate
	 */
	public Date getAnnualReportSubmissionDate() {
		return annualReportSubmissionDate;
	}

	/**
	 * @param annualReportSubmissionDate
	 *            the annualReportSubmissionDate to set
	 */
	public void setAnnualReportSubmissionDate(Date annualReportSubmissionDate) {
		this.annualReportSubmissionDate = annualReportSubmissionDate;
	}

	/**
	 * @return the annualReportDoc
	 */
	public String getAnnualReportDoc() {
		return annualReportDoc;
	}

	/**
	 * @param annualReportDoc
	 *            the annualReportDoc to set
	 */
	public void setAnnualReportDoc(String annualReportDoc) {
		this.annualReportDoc = annualReportDoc;
	}

	/**
	 * @return the acceptedTechAssAmountCertified
	 */
	public Double getAcceptedTechAssAmountCertified() {
		return acceptedTechAssAmountCertified;
	}

	/**
	 * @param acceptedTechAssAmountCertified
	 *            the acceptedTechAssAmountCertified to set
	 */
	public void setAcceptedTechAssAmountCertified(Double acceptedTechAssAmountCertified) {
		this.acceptedTechAssAmountCertified = acceptedTechAssAmountCertified;
	}

	/**
	 * @return the acceptedProjectsExpenditureCertifiedAmount
	 */
	public Double getAcceptedProjectsExpenditureCertifiedAmount() {
		return acceptedProjectsExpenditureCertifiedAmount;
	}

	/**
	 * @param acceptedProjectsExpenditureCertifiedAmount
	 *            the acceptedProjectsExpenditureCertifiedAmount to set
	 */
	public void setAcceptedProjectsExpenditureCertifiedAmount(Double acceptedProjectsExpenditureCertifiedAmount) {
		this.acceptedProjectsExpenditureCertifiedAmount = acceptedProjectsExpenditureCertifiedAmount;
	}

	/**
	 * @return the annualEUAllocationAmount
	 */
	public Double getAnnualEUAllocationAmount() {
		return annualEUAllocationAmount;
	}

	/**
	 * @param annualEUAllocationAmount
	 *            the annualEUAllocationAmount to set
	 */
	public void setAnnualEUAllocationAmount(Double annualEUAllocationAmount) {
		this.annualEUAllocationAmount = annualEUAllocationAmount;
	}

}
