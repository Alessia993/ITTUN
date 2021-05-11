/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.Date;

import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CEPaymentRequest;

/**
 *
 * @author acagnoni Ingloba360 2019/03/20
 *
 */
public class CEPaymentRequestBean extends PersistenceEntityBean<CEPaymentRequest> {

	private Date annualNotificationCommitmentDate;
	private String annualCommitmentAmount;
	private Date preFinancingPaymentRequestDate;
	private String preFinancingSupportingDoc;
	private String preFinancingRequestedAmount;
	private Date annualReportSubmissionDate;
	private String annualReportDoc;
	private String acceptedTechAssAmountCertified;
	private String acceptedProjectsExpenditureCertifiedAmount;
	private String annualEUAllocationAmount;

	public void Page_Save() throws HibernateException, PersistenceException, PersistenceBeanException {

		CEPaymentRequest item = new CEPaymentRequest();
		item.setCreateDate(new Date());
		item.setAnnualNotificationCommitmentDate(this.annualNotificationCommitmentDate);
		item.setAnnualCommitmentAmount(Double.parseDouble(this.annualCommitmentAmount));
		item.setPreFinancingPaymentRequestDate(this.preFinancingPaymentRequestDate);
		item.setPreFinancingSupportingDoc(this.preFinancingSupportingDoc);
		item.setPreFinancingRequestedAmount(Double.parseDouble(this.preFinancingRequestedAmount));
		item.setAnnualReportSubmissionDate(this.annualReportSubmissionDate);
		item.setAnnualReportDoc(this.annualReportDoc);
		item.setAcceptedTechAssAmountCertified(Double.parseDouble(this.acceptedTechAssAmountCertified));
		item.setAcceptedProjectsExpenditureCertifiedAmount(Double.parseDouble(this.acceptedProjectsExpenditureCertifiedAmount));
		item.setAnnualEUAllocationAmount(Double.parseDouble(this.annualEUAllocationAmount));
		BeansFactory.ControllerCePaymentRequest().Save(item);

	}



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
	public String getAnnualCommitmentAmount() {
		return annualCommitmentAmount;
	}

	/**
	 * @param annualCommitmentAmount
	 *            the annualCommitmentAmount to set
	 */
	public void setAnnualCommitmentAmount(String annualCommitmentAmount) {
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
	public String getPreFinancingRequestedAmount() {
		return preFinancingRequestedAmount;
	}

	/**
	 * @param preFinancingRequestedAmount
	 *            the preFinancingRequestedAmount to set
	 */
	public void setPreFinancingRequestedAmount(String preFinancingRequestedAmount) {
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
	public String getAcceptedTechAssAmountCertified() {
		return acceptedTechAssAmountCertified;
	}

	/**
	 * @param acceptedTechAssAmountCertified
	 *            the acceptedTechAssAmountCertified to set
	 */
	public void setAcceptedTechAssAmountCertified(String acceptedTechAssAmountCertified) {
		this.acceptedTechAssAmountCertified = acceptedTechAssAmountCertified;
	}

	/**
	 * @return the acceptedProjectsExpenditureCertifiedAmount
	 */
	public String getAcceptedProjectsExpenditureCertifiedAmount() {
		return acceptedProjectsExpenditureCertifiedAmount;
	}

	/**
	 * @param acceptedProjectsExpenditureCertifiedAmount
	 *            the acceptedProjectsExpenditureCertifiedAmount to set
	 */
	public void setAcceptedProjectsExpenditureCertifiedAmount(String acceptedProjectsExpenditureCertifiedAmount) {
		this.acceptedProjectsExpenditureCertifiedAmount = acceptedProjectsExpenditureCertifiedAmount;
	}

	/**
	 * @return the annualEUAllocationAmount
	 */
	public String getAnnualEUAllocationAmount() {
		return annualEUAllocationAmount;
	}

	/**
	 * @param annualEUAllocationAmount
	 *            the annualEUAllocationAmount to set
	 */
	public void setAnnualEUAllocationAmount(String annualEUAllocationAmount) {
		this.annualEUAllocationAmount = annualEUAllocationAmount;
	}

}
