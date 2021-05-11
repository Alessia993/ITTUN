package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.annotations.Exports;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name = "expenditure_declaration_annex_wrapper")
public class ExpenditureDeclarationAnnex1dWrapper extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109545503472890194L;

	@Column(name = "axis")
	private String axis;

	@Column(name = "specific_objective")
	private String specificObjective;

	@Column(name = "cup")
	private String cup;

	@Column(name = "local_code")
	private String localCode;

	@Column(name = "system_code")
	private String systemCode;

	@Column(name = "title")
	private String title;

	// mezzo pagamento id
	@Column(name = "document_type_id")
	private String documentTypeId;

	@Transient
	private String documentType;

	@Column(name = "payment_number")
	private String paymentNumber;

	@Column(name = "payment_date")
	private Date paymentDate;

	@Column(name = "payment_amount")
	private Double paymentAmount;

	@Column(name = "retired_amount")
	private Double retiredAmount;

	@Column(name = "dur_Tot_eligible_expenses_public_support_reimbursed")
	private Double durTotEligibleExpensesPublicSupportReimbursed;

	@Column(name = "retreat_act_number")
	private String retreatActNumber;

	@Column(name = "suspend_act_number")
	private String suspendActNumber;

	@Column(name = "dur_recovery_act_number")
	private String durRecoveryActNumber;

	@Column(name = "dur_not_regular_act_number")
	private Double durNotRegularActNumber;

	@Column(name = "retreat_date_decertification")
	private Date retreatDateDecertification;

	@Column(name = "date_suspend_acu")
	private Date dateSuspendACU;

	@Column(name = "dur_recovery_act_date")
	private Date durRecoveryActDate;

	@Transient
	private String retreatActExtreme;

	@Transient
	private String suspensionActExtreme;

	@Transient
	private String recoveredActExtreme;

	@Column(name = "retired_date")
	private Date retiredDate;

	@Column(name = "suspend_act_date")
	private Date suspendActDate;

	@Column(name = "dur_not_regular_act_date")
	private Date durNotRegularActDate;

	@Column(name = "motivation_id")
	private Long motivationId;

	@Column(name = "dur_recovery_reason_id")
	private Long selctedDurRecoveryReason;

	@Transient
	private String motivation;

	@Column(name = "suspended_by_acu")
	private Boolean suspendedByACU;

	@Column(name = "rectified_by_acu")
	private Boolean rectifiedByACU;

	@Column(name = "recovered_by_agu_acu")
	private Boolean recoverededByAGUACU;

	@Column(name = "dur_not_regular_is_recoverable")
	private Boolean durNotRegularIsRecoverable;

	@Column(name = "cost_recovery")
	private Double costRecovery;

	@Column(name = "tot_eligible_expenses_public_support_non_recoverable")
	private Double totEligibleExpensesPublicSupportNonRecoverable;
	
	@Column(name = "suspend_reason_id")
	private Long suspendReasonId;

	public ExpenditureDeclarationAnnex1dWrapper() {
	}

	public ExpenditureDeclarationAnnex1dWrapper(Date paymentDate, Double paymentAmount, Double retiredAmount,
			String retreatActNumber, Date retreatDateDecertification, Date retiredDate, Long motivationId, String axis,
			String specificObjective, String cup, String localCode, String systemCode, String title,
			String documentTypeId, String paymentNumber, Boolean suspendedByACU, Boolean rectifiedByACU,
			Boolean recoverededByAGUACU, String suspendActNumber, Date dateSuspendACU, Date suspendActDate,
			String durRecoveryActNumber, Date durRecoveryActDate, Long selctedDurRecoveryReason,
			Double durTotEligibleExpensesPublicSupportReimbursed, Boolean durNotRegularIsRecoverable,
			Double durNotRegularActNumber, Date durNotRegularActDate, Double costRecovery,
			Double totEligibleExpensesPublicSupportNonRecoverable) {
		super();
		this.axis = axis;
		this.specificObjective = specificObjective;
		this.cup = cup;
		this.localCode = localCode;
		this.systemCode = systemCode;
		this.title = title;
		this.documentTypeId = documentTypeId;
		this.paymentNumber = paymentNumber;
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.retiredAmount = retiredAmount;
		this.retreatActNumber = retreatActNumber;
		this.retreatDateDecertification = retreatDateDecertification;
		this.retiredDate = retiredDate;
		this.motivationId = motivationId;
		this.suspendedByACU = suspendedByACU;
		this.rectifiedByACU = rectifiedByACU;
		this.recoverededByAGUACU = recoverededByAGUACU;
		this.suspendActNumber = suspendActNumber;
		this.dateSuspendACU = dateSuspendACU;
		this.suspendActDate = suspendActDate;
		this.durRecoveryActNumber = durRecoveryActNumber;
		this.durRecoveryActDate = durRecoveryActDate;
		this.selctedDurRecoveryReason = selctedDurRecoveryReason;
		this.durTotEligibleExpensesPublicSupportReimbursed = durTotEligibleExpensesPublicSupportReimbursed;
		this.durNotRegularIsRecoverable = durNotRegularIsRecoverable;
		this.durNotRegularActNumber = durNotRegularActNumber;
		this.durNotRegularActDate = durNotRegularActDate;
		this.costRecovery = costRecovery;
		this.totEligibleExpensesPublicSupportNonRecoverable = totEligibleExpensesPublicSupportNonRecoverable;
	}

	@Exports({ @Export(propertyName = "asse", place = ExportPlaces.Annex1H, seqXLS = 0, type = FieldTypes.STRING),
			@Export(propertyName = "asse", place = ExportPlaces.Annex1G, seqXLS = 0, type = FieldTypes.STRING),
			@Export(propertyName = "asse", place = ExportPlaces.Annex1F, seqXLS = 0, type = FieldTypes.STRING),
			@Export(propertyName = "asse", place = ExportPlaces.Annex1E, seqXLS = 0, type = FieldTypes.STRING),
			@Export(propertyName = "asse", place = ExportPlaces.Annex1D, seqXLS = 0, type = FieldTypes.STRING) })
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", place = ExportPlaces.Annex1H, seqXLS = 1, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", place = ExportPlaces.Annex1G, seqXLS = 1, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", place = ExportPlaces.Annex1F, seqXLS = 1, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", place = ExportPlaces.Annex1E, seqXLS = 1, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", place = ExportPlaces.Annex1D, seqXLS = 1, type = FieldTypes.STRING) })
	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditCUP", place = ExportPlaces.Annex1H, seqXLS = 2, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditCUP", place = ExportPlaces.Annex1G, seqXLS = 2, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditCUP", place = ExportPlaces.Annex1F, seqXLS = 2, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditCUP", place = ExportPlaces.Annex1E, seqXLS = 2, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditCUP", place = ExportPlaces.Annex1D, seqXLS = 2, type = FieldTypes.STRING) })
	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditLocalCode", place = ExportPlaces.Annex1H, seqXLS = 3, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditLocalCode", place = ExportPlaces.Annex1G, seqXLS = 3, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditLocalCode", place = ExportPlaces.Annex1F, seqXLS = 3, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditLocalCode", place = ExportPlaces.Annex1E, seqXLS = 3, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditLocalCode", place = ExportPlaces.Annex1D, seqXLS = 3, type = FieldTypes.STRING) })
	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditSystemCode", place = ExportPlaces.Annex1H, seqXLS = 4, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditSystemCode", place = ExportPlaces.Annex1G, seqXLS = 4, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditSystemCode", place = ExportPlaces.Annex1F, seqXLS = 4, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditSystemCode", place = ExportPlaces.Annex1E, seqXLS = 4, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditSystemCode", place = ExportPlaces.Annex1D, seqXLS = 4, type = FieldTypes.STRING) })
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditOperationTitle", place = ExportPlaces.Annex1H, seqXLS = 5, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditOperationTitle", place = ExportPlaces.Annex1G, seqXLS = 5, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditOperationTitle", place = ExportPlaces.Annex1F, seqXLS = 5, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditOperationTitle", place = ExportPlaces.Annex1E, seqXLS = 5, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditOperationTitle", place = ExportPlaces.Annex1D, seqXLS = 5, type = FieldTypes.STRING) })
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditDocumentType", place = ExportPlaces.Annex1H, seqXLS = 6, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditDocumentType", place = ExportPlaces.Annex1G, seqXLS = 6, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditDocumentType", place = ExportPlaces.Annex1F, seqXLS = 6, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditDocumentType", place = ExportPlaces.Annex1E, seqXLS = 6, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditDocumentType", place = ExportPlaces.Annex1D, seqXLS = 6, type = FieldTypes.STRING) })
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditPaymentNumber", place = ExportPlaces.Annex1H, seqXLS = 7, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentNumber", place = ExportPlaces.Annex1G, seqXLS = 7, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentNumber", place = ExportPlaces.Annex1F, seqXLS = 7, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentNumber", place = ExportPlaces.Annex1E, seqXLS = 7, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentNumber", place = ExportPlaces.Annex1D, seqXLS = 7, type = FieldTypes.STRING) })
	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditPaymentDate", place = ExportPlaces.Annex1H, seqXLS = 8, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentDate", place = ExportPlaces.Annex1G, seqXLS = 8, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentDate", place = ExportPlaces.Annex1F, seqXLS = 8, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentDate", place = ExportPlaces.Annex1E, seqXLS = 8, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditPaymentDate", place = ExportPlaces.Annex1D, seqXLS = 8, type = FieldTypes.STRING) })
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditPaymentAmount", place = ExportPlaces.Annex1H, seqXLS = 9, type = FieldTypes.MONEY),
			@Export(propertyName = "expenditureDeclarationEditPaymentAmount", place = ExportPlaces.Annex1G, seqXLS = 9, type = FieldTypes.MONEY),
			@Export(propertyName = "expenditureDeclarationEditPaymentAmount", place = ExportPlaces.Annex1F, seqXLS = 9, type = FieldTypes.MONEY),
			@Export(propertyName = "expenditureDeclarationEditPaymentAmount", place = ExportPlaces.Annex1E, seqXLS = 9, type = FieldTypes.MONEY),
			@Export(propertyName = "expenditureDeclarationEditPaymentAmount", place = ExportPlaces.Annex1D, seqXLS = 9, type = FieldTypes.MONEY) })
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditTemporaryRetiredAmount", place = ExportPlaces.Annex1E, seqXLS = 10, type = FieldTypes.MONEY),
			@Export(propertyName = "xpenditureDeclarationEditRetiredAmount", place = ExportPlaces.Annex1D, seqXLS = 10, type = FieldTypes.MONEY) })
	public Double getRetiredAmount() {
		return retiredAmount;
	}

	public void setRetiredAmount(Double retiredAmount) {
		this.retiredAmount = retiredAmount;
	}

	@Export(propertyName = "expenditureDeclarationEditRetiredDate", place = ExportPlaces.Annex1D, seqXLS = 12, type = FieldTypes.DATE) 
	public Date getRetiredDate() {
		return retiredDate;
	}

	public void setRetiredDate(Date retiredDate) {
		this.retiredDate = retiredDate;
	}

	@Exports({
			@Export(propertyName  = "expenditureDeclarationEditIrrecoverabilityMotivation", place = ExportPlaces.Annex1H, seqXLS = 12, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditRevocationMotivation", place = ExportPlaces.Annex1G, seqXLS = 12, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditRevocationMotivation", place = ExportPlaces.Annex1F, seqXLS = 13, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditMotivation", place = ExportPlaces.Annex1D, seqXLS = 13, type = FieldTypes.STRING)  })
	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	@Export(propertyName = "expenditureDeclarationEditRetreatActExtreme", place = ExportPlaces.Annex1D, seqXLS = 11, type = FieldTypes.STRING) 
	public String getRetreatActExtreme() {
		return retreatActExtreme;
	}

	public void setRetreatActExtreme(String retreatActExtreme) {
		this.retreatActExtreme = retreatActExtreme;
	}

	public String getRetreatActNumber() {
		return retreatActNumber;
	}

	public void setRetreatActNumber(String retreatActNumber) {
		this.retreatActNumber = retreatActNumber;
	}

	public Date getRetreatDateDecertification() {
		return retreatDateDecertification;
	}

	public void setRetreatDateDecertification(Date retreatDateDecertification) {
		this.retreatDateDecertification = retreatDateDecertification;
	}

	public Long getMotivationId() {
		return motivationId;
	}

	public void setMotivationId(Long motivationId) {
		this.motivationId = motivationId;
	}

	public Boolean getSuspendedByACU() {
		return suspendedByACU;
	}

	public void setSuspendedByACU(Boolean suspendedByACU) {
		this.suspendedByACU = suspendedByACU;
	}

	public Boolean getRectifiedByACU() {
		return rectifiedByACU;
	}

	public void setRectifiedByACU(Boolean rectifiedByACU) {
		this.rectifiedByACU = rectifiedByACU;
	}

	public Boolean getRecoverededByAGUACU() {
		return recoverededByAGUACU;
	}

	public void setRecoverededByAGUACU(Boolean recoverededByAGUACU) {
		this.recoverededByAGUACU = recoverededByAGUACU;
	}

	public String getSuspendActNumber() {
		return suspendActNumber;
	}

	public void setSuspendActNumber(String suspendActNumber) {
		this.suspendActNumber = suspendActNumber;
	}

	public Date getDateSuspendACU() {
		return dateSuspendACU;
	}

	public void setDateSuspendACU(Date dateSuspendACU) {
		this.dateSuspendACU = dateSuspendACU;
	}

	@Export(propertyName = "expenditureDeclarationEditRetreatActExtreme", place = ExportPlaces.Annex1E, seqXLS = 11, type = FieldTypes.STRING)
	public String getSuspensionActExtreme() {
		return suspensionActExtreme;
	}

	public void setSuspensionActExtreme(String suspensionActExtreme) {
		this.suspensionActExtreme = suspensionActExtreme;
	}

	@Export(propertyName = "expenditureDeclarationEditRetiredDate", place = ExportPlaces.Annex1E, seqXLS = 12, type = FieldTypes.DATE)
	public Date getSuspendActDate() {
		return suspendActDate;
	}

	public void setSuspendActDate(Date suspendActDate) {
		this.suspendActDate = suspendActDate;
	}

	@Exports({
			@Export(propertyName = "expenditureDeclarationEditRecoveredActExtreme", place = ExportPlaces.Annex1H, seqXLS = 11, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditRecoveredActExtreme", place = ExportPlaces.Annex1G, seqXLS = 11, type = FieldTypes.STRING),
			@Export(propertyName = "expenditureDeclarationEditRecoveredActExtreme", place = ExportPlaces.Annex1F, seqXLS = 11, type = FieldTypes.STRING) })
	public String getRecoveredActExtreme() {
		return recoveredActExtreme;
	}

	public void setRecoveredActExtreme(String recoveredActExtreme) {
		this.recoveredActExtreme = recoveredActExtreme;
	}

	public String getDurRecoveryActNumber() {
		return durRecoveryActNumber;
	}

	public void setDurRecoveryActNumber(String durRecoveryActNumber) {
		this.durRecoveryActNumber = durRecoveryActNumber;
	}

	public Date getDurRecoveryActDate() {
		return durRecoveryActDate;
	}

	public void setDurRecoveryActDate(Date durRecoveryActDate) {
		this.durRecoveryActDate = durRecoveryActDate;
	}

	public Long getSelctedDurRecoveryReason() {
		return selctedDurRecoveryReason;
	}

	public void setSelctedDurRecoveryReason(Long selctedDurRecoveryReason) {
		this.selctedDurRecoveryReason = selctedDurRecoveryReason;
	}

	@Export(propertyName = "expenditureDeclarationEditRecoveryAmount", place = ExportPlaces.Annex1F, seqXLS = 10, type = FieldTypes.MONEY)
	public Double getDurTotEligibleExpensesPublicSupportReimbursed() {
		return durTotEligibleExpensesPublicSupportReimbursed;
	}

	public void setDurTotEligibleExpensesPublicSupportReimbursed(Double durTotEligibleExpensesPublicSupportReimbursed) {
		this.durTotEligibleExpensesPublicSupportReimbursed = durTotEligibleExpensesPublicSupportReimbursed;
	}

	public Boolean getDurNotRegularIsRecoverable() {
		return durNotRegularIsRecoverable;
	}

	public void setDurNotRegularIsRecoverable(Boolean durNotRegularIsRecoverable) {
		this.durNotRegularIsRecoverable = durNotRegularIsRecoverable;
	}

	public Double getDurNotRegularActNumber() {
		return durNotRegularActNumber;
	}

	public void setDurNotRegularActNumber(Double durNotRegularActNumber) {
		this.durNotRegularActNumber = durNotRegularActNumber;
	}

	@Export(propertyName = "expenditureDeclarationEditRecoveredDate", place = ExportPlaces.Annex1F, seqXLS = 12, type = FieldTypes.DATE)
	public Date getDurNotRegularActDate() {
		return durNotRegularActDate;
	}

	public void setDurNotRegularActDate(Date durNotRegularActDate) {
		this.durNotRegularActDate = durNotRegularActDate;
	}

	@Export(propertyName = "expenditureDeclarationEditRecoveryAmountPending", place = ExportPlaces.Annex1G, seqXLS = 10, type = FieldTypes.MONEY)
	public Double getCostRecovery() {
		return costRecovery;
	}

	public void setCostRecovery(Double costRecovery) {
		this.costRecovery = costRecovery;
	}

	@Export(propertyName = "expenditureDeclarationEditAmountDeemedIrrecoverable", place = ExportPlaces.Annex1H, seqXLS = 10, type = FieldTypes.MONEY)
	public Double getTotEligibleExpensesPublicSupportNonRecoverable() {
		return totEligibleExpensesPublicSupportNonRecoverable;
	}

	public void setTotEligibleExpensesPublicSupportNonRecoverable(
			Double totEligibleExpensesPublicSupportNonRecoverable) {
		this.totEligibleExpensesPublicSupportNonRecoverable = totEligibleExpensesPublicSupportNonRecoverable;
	}

	@Export(propertyName = "expenditureDeclarationEditMotivation", place = ExportPlaces.Annex1E, seqXLS = 13, type = FieldTypes.STRING)
	public Long getSuspendReasonId() {
		return suspendReasonId;
	}

	public void setSuspendReasonId(Long suspendReasonId) {
		this.suspendReasonId = suspendReasonId;
	}

}
