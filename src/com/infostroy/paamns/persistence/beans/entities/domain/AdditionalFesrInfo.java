/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2015.
 *
 */
@Entity
@Table(name = "additional_fesr_info")
public class AdditionalFesrInfo extends PersistentEntity {
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = 6825968719274941846L;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Projects project;

	@ManyToOne
	@JoinColumn(name = "fesr_bf_id")
	private CFPartnerAnagraphs fesrBf;

	@ManyToOne
	@JoinColumn(name = "add_user_role_id")
	private Roles addUserRole;

	@Column(name = "total_ddr")
	private Double totalDDR;

	@Column(name = "transfer_type_code")
	private Long transferTypeCode;

	@Column(name = "transfer_import")
	private Double transferImport;

	@Column(name = "totalTransfer")
	private Double totalTransfer;

	@Column(name = "totalAdvances")
	private Double totalAdvances;

	@Column(name = "totalRepayments")
	private Double totalRepayments;

	@Column(name = "mode_of_crediting_funds")
	private String modeOfCreditingFunds;

	@Column(name = "number_settlement_act")
	private String numberSettlementAct;

	@Column(name = "fesr_payment_act")
	private Date fesrPaymentAct;

	@Column(name = "number_mandate_payment")
	private String numberMandatePayment;

	@Column(name = "fesr_date")
	private Date fesrDate;

	@Column(name = "fesr_cro")
	private String fesrCro;

	@Column(name = "quota_fesr")
	private Double quotaFesr;

	@Column(name = "quota_cpn")
	private Double quotaCpn;

	@ManyToOne
	@JoinColumn(name = "document_id")
	private Documents document;

	@Column(name = "advance_state_aid_de_minimis")
	private Double advanceStateAidDeMinimis;

	@Column(name = "advance_state_aid_exemption_scheme")
	private Double advanceStateAidExemptionScheme;

	@Column(name = "axis")
	private String axis;

	/**
	 * Gets project
	 * 
	 * @return project the project
	 */
	public Projects getProject() {
		return project;
	}

	/**
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project) {
		this.project = project;
	}

	/**
	 * Gets fesrBf
	 * 
	 * @return fesrBf the fesrBf
	 */
	public CFPartnerAnagraphs getFesrBf() {
		return fesrBf;
	}

	/**
	 * Sets fesrBf
	 * 
	 * @param fesrBf
	 *            the fesrBf to set
	 */
	public void setFesrBf(CFPartnerAnagraphs fesrBf) {
		this.fesrBf = fesrBf;
	}

	/**
	 * Gets transferTypeCode
	 * 
	 * @return transferTypeCode the transferTypeCode
	 */
	public Long getTransferTypeCode() {
		return transferTypeCode;
	}

	/**
	 * Sets transferTypeCode
	 * 
	 * @param transferTypeCode
	 *            the transferTypeCode to set
	 */
	public void setTransferTypeCode(Long transferTypeCode) {
		this.transferTypeCode = transferTypeCode;
	}

	/**
	 * Gets transferImport
	 * 
	 * @return transferImport the transferImport
	 */
	public Double getTransferImport() {
		return transferImport;
	}

	/**
	 * Sets transferImport
	 * 
	 * @param transferImport
	 *            the transferImport to set
	 */
	public void setTransferImport(Double transferImport) {
		this.transferImport = transferImport;
	}

	/**
	 * Gets totalTransfer
	 * 
	 * @return totalTransfer the totalTransfer
	 */
	public Double getTotalTransfer() {
		return totalTransfer;
	}

	/**
	 * Sets totalTransfer
	 * 
	 * @param totalTransfer
	 *            the totalTransfer to set
	 */
	public void setTotalTransfer(Double totalTransfer) {
		this.totalTransfer = totalTransfer;
	}

	/**
	 * Gets modeOfCreditingFunds
	 * 
	 * @return modeOfCreditingFunds the modeOfCreditingFunds
	 */
	public String getModeOfCreditingFunds() {
		return modeOfCreditingFunds;
	}

	/**
	 * Sets modeOfCreditingFunds
	 * 
	 * @param modeOfCreditingFunds
	 *            the modeOfCreditingFunds to set
	 */
	public void setModeOfCreditingFunds(String modeOfCreditingFunds) {
		this.modeOfCreditingFunds = modeOfCreditingFunds;
	}

	/**
	 * Gets numberSettlementAct
	 * 
	 * @return numberSettlementAct the numberSettlementAct
	 */
	public String getNumberSettlementAct() {
		return numberSettlementAct;
	}

	/**
	 * Sets numberSettlementAct
	 * 
	 * @param numberSettlementAct
	 *            the numberSettlementAct to set
	 */
	public void setNumberSettlementAct(String numberSettlementAct) {
		this.numberSettlementAct = numberSettlementAct;
	}

	/**
	 * Gets fesrPaymentAct
	 * 
	 * @return fesrPaymentAct the fesrPaymentAct
	 */
	public Date getFesrPaymentAct() {
		return fesrPaymentAct;
	}

	/**
	 * Sets fesrPaymentAct
	 * 
	 * @param fesrPaymentAct
	 *            the fesrPaymentAct to set
	 */
	public void setFesrPaymentAct(Date fesrPaymentAct) {
		this.fesrPaymentAct = fesrPaymentAct;
	}

	/**
	 * Gets numberMandatePayment
	 * 
	 * @return numberMandatePayment the numberMandatePayment
	 */
	public String getNumberMandatePayment() {
		return numberMandatePayment;
	}

	/**
	 * Sets numberMandatePayment
	 * 
	 * @param numberMandatePayment
	 *            the numberMandatePayment to set
	 */
	public void setNumberMandatePayment(String numberMandatePayment) {
		this.numberMandatePayment = numberMandatePayment;
	}

	/**
	 * Gets fesrDate
	 * 
	 * @return fesrDate the fesrDate
	 */
	public Date getFesrDate() {
		return fesrDate;
	}

	/**
	 * Sets fesrDate
	 * 
	 * @param fesrDate
	 *            the fesrDate to set
	 */
	public void setFesrDate(Date fesrDate) {
		this.fesrDate = fesrDate;
	}

	/**
	 * Gets fesrCro
	 * 
	 * @return fesrCro the fesrCro
	 */
	public String getFesrCro() {
		return fesrCro;
	}

	/**
	 * Sets fesrCro
	 * 
	 * @param fesrCro
	 *            the fesrCro to set
	 */
	public void setFesrCro(String fesrCro) {
		this.fesrCro = fesrCro;
	}

	/**
	 * Gets totalDDR
	 * 
	 * @return totalDDR the totalDDR
	 */
	public Double getTotalDDR() {
		return totalDDR;
	}

	/**
	 * Sets totalDDR
	 * 
	 * @param totalDDR
	 *            the totalDDR to set
	 */
	public void setTotalDDR(Double totalDDR) {
		this.totalDDR = totalDDR;
	}

	public TransferTypes getTransferType() {
		return TransferTypes.getByCode(this.getTransferTypeCode());
	}

	/**
	 * Gets totalAdvances
	 * 
	 * @return totalAdvances the totalAdvances
	 */
	public Double getTotalAdvances() {
		return totalAdvances;
	}

	/**
	 * Sets totalAdvances
	 * 
	 * @param totalAdvances
	 *            the totalAdvances to set
	 */
	public void setTotalAdvances(Double totalAdvances) {
		this.totalAdvances = totalAdvances;
	}

	/**
	 * Gets totalRepayments
	 * 
	 * @return totalRepayments the totalRepayments
	 */
	public Double getTotalRepayments() {
		return totalRepayments;
	}

	/**
	 * Sets totalRepayments
	 * 
	 * @param totalRepayments
	 *            the totalRepayments to set
	 */
	public void setTotalRepayments(Double totalRepayments) {
		this.totalRepayments = totalRepayments;
	}

	public Double getQuotaFesr() {
		return quotaFesr;
	}

	public void setQuotaFesr(Double quotaFesr) {
		this.quotaFesr = quotaFesr;
	}

	public Double getQuotaCpn() {
		return quotaCpn;
	}

	public void setQuotaCpn(Double quotaCpn) {
		this.quotaCpn = quotaCpn;
	}

	public Documents getDocument() {
		return document;
	}

	public void setDocument(Documents document) {
		this.document = document;
	}

	/**
	 * Gets addUserRole
	 * 
	 * @return addUserRole the addUserRole
	 */
	public Roles getAddUserRole() {
		return addUserRole;
	}

	/**
	 * Sets addUserRole
	 * 
	 * @param addUserRole
	 *            the addUserRole to set
	 */
	public void setAddUserRole(Roles addUserRole) {
		this.addUserRole = addUserRole;
	}

	public Double getAdvanceStateAidDeMinimis() {
		return advanceStateAidDeMinimis;
	}

	public void setAdvanceStateAidDeMinimis(Double advanceStateAidDeMinimis) {
		this.advanceStateAidDeMinimis = advanceStateAidDeMinimis;
	}

	public Double getAdvanceStateAidExemptionScheme() {
		return advanceStateAidExemptionScheme;
	}

	public void setAdvanceStateAidExemptionScheme(Double advanceStateAidExemptionScheme) {
		this.advanceStateAidExemptionScheme = advanceStateAidExemptionScheme;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

}
