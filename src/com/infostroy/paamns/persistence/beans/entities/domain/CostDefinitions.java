package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.annotations.Exports;
import com.infostroy.paamns.common.enums.CostDefinitionSuspendStatus;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.enums.RecoverReasonType;
import com.infostroy.paamns.common.enums.RectificationReasonTypes;
import com.infostroy.paamns.common.enums.RectificationStateTypes;
import com.infostroy.paamns.common.enums.SuspensionReasonTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Currencies;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DocumentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentWays;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010. modified by Ingloba360
 * 
 */
@Entity
@Table(name = "cost_definitions")
public class CostDefinitions extends PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = -1434138334655652740L;

	/**
	 * 
	 */
	public CostDefinitions() {
		setRefucedFromDur(false);
		dismiss = false;
		isRecoveryRetreat = false;
		setWasRefusedByCil(false);
		setCut(false);
	}

	@Column(name = "refuced_from_dur")
	private Boolean refucedFromDur;

	@Column(name = "suspension_amount")
	private Double suspensionAmount;

	@Column(name = "previous_suspension_amount")
	private Double previousSuspensionAmount;

	@Column(name = "documentation_contentious_recovery")
	private String documentationContentiousRecovery;
	
	@Column(name = "documentation_unsuccessful_recovery")
	private String documentationUnsuccessfulRecovery;
	
	@Column(name = "date_of_payment_programme_or_body")
	private Date dateOfPaymentProgrammeOrBody;
	
	@Column(name = "supporting_documents_waiving_decision")
	private String supportingDocumentsWaivingDecision;
	
	@Column(name = "amount_of_recovery_waived")
	private Double amountOfRecoveryWaived;
	
	@Column
	private Boolean cut;

	@Column(name = "suspension_date")
	private Date suspensionDate;

	@Column(name = "was_refused_by_cil")
	private Boolean wasRefusedByCil;

	@Column(name = "amount_recovery")
	private Double amountRecovery;

	@Column(name = "amount_retreat")
	private Double amountRetreat;

	@Column(name = "cost_recovery")
	private Double costRecovery;

	@Column(name = "dur_amount_of_public_support")
	private Double durAmountOfPublicSupport;

	@Column(name = "dur_total_eligible_cost")
	private Double durTotalEligibleCost;

	@Column(name = "recovery_date")
	private Date recoveryDate;

	@Column(name = "retreat_date")
	private Date retreatDate;

	@Column(name = "dur_recovery_date")
	private Date durRecoveryDate;

	@Column(name = "is_recovery_retreat")
	private Boolean isRecoveryRetreat;

	@Column(name = "progressive_id")
	private Long progressiveId;

	@Column(name = "cil_intermediate_step_save", columnDefinition = "bit(1) NOT NULL default false", nullable = false)
	private Boolean cilIntermediateStepSave;

	@Column(name = "dismiss", columnDefinition = "bit(1) NOT NULL default false", nullable = false)
	private Boolean dismiss;

	@ManyToOne
	@JoinColumn(name = "temp_document_id")
	private Documents tempDocument;

	@ManyToOne
	@JoinColumn(name = "temp_documentcf_id")
	private Documents tempDocumentCF;

	@ManyToOne
	@JoinColumn(name = "suspend_document_id")
	private Documents suspendDocument;

	@ManyToOne
	@JoinColumn(name = "cost_item_phase_asse3")
	private CostAsse3 costItemPhaseAsse3;

	@Transient
	private boolean editable;

	@Column(name = "custom_note", columnDefinition = "varchar(1000)")
	private String customNote;

	@Transient
	private String customNoteWeb;

	@Transient
	private boolean selected;

	@Transient
	private boolean selectedResend;

	@Transient
	private String notRegularTempId;

	@Transient
	private String notRegularTypeName;

	@Transient
	private String notRegularParentTempId;

	@Transient
	private String style1;

	@Transient
	private String title1;

	@Transient
	private String style2;

	@Transient
	private String title2;

	@Column(name = "dur_number")
	private Integer durNumber;

	@Column(name = "irregular_amount")
	private Double irregularAmount;

	@Column(name = "reference_year")
	private Integer referenceYear;

	@Column
	private Boolean certification;

	@ManyToOne
	@JoinColumn
	private Users certifyUser;

	@Column
	private Date date;

	@Column(name = "be_recovered")
	private Boolean beRecovered;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;

	@ManyToOne
	@JoinColumn(name = "budget_owner_id")
	private Users budgetOwner;

	@Column(name = "created_by_agu")
	private Boolean createdByAGU;

	@Column(name = "created_by_partner")
	private Boolean createdByPartner;

	@Column(name = "additional_finansing")
	private Boolean additionalFinansing;

	@Column(name = "additional_finansing_amount")
	private Double additionalFinansingAmount;

	@Column(name = "state_aid")
	private Boolean stateAid;

	@Column(name = "expenditure_outside_eligible_areas_amount")
	private Double expenditureOutsideEligibleAreasAmount;

	@Column(name = "expenditure_for_land_purchase")
	private Boolean expenditureForLandPurchase;

	@Column(name = "inkind_contributions_amount")
	private Double inkindContributionsAmount;

	@ManyToOne
	@JoinColumn(name = "dur_compilations_id")
	private DurCompilations durCompilation;

	@ManyToOne
	@JoinColumn(name = "cost_definition_phase_id")
	private CostDefinitionPhases costDefinitionPhase;

	@Transient
	private String costStateId;

	/**
	 * Stores project value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Projects project;

	/**
	 * Stores documentType value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentTypes documentType;
	
	
	@Column(name = "currency_code", length = 10)
	private String currencyCode;

	/**
	 * Stores motivations value of entity.
	 */
	@Column
	private String motivations;

	/**
	 * Stores documentNumber value of entity.
	 */
	@Column(name = "document_number")
	private String documentNumber;

	/**
	 * Stores documentDate value of entity.
	 */
	@Column(name = "document_date")
	private Date documentDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "location")
	private LocationForCostDef location;

	@Column(name = "report_date")
	private Date reportDate;

	@Column(name = "report_number")
	private Integer reportNumber;

	@Column(name = "stc_cert_note")
	private String stcCertificationNote;

	@Column(name = "agu_cert_note")
	private String aguCertificationNote;

	@Column(name = "cf_cert_note")
	private String cfCertificationNote;

	@Column(name = "payment_type_code", length = 4)
	private String paymentTypeCode;

	@Column(name = "payment_motivation_code", columnDefinition = "char(3)")
	private String paymentMotivationCode;

	/**
	 * Gets location
	 * 
	 * @return location the location
	 */
	@Exports({
			@Export(propertyName = "exportLocation", seqXLS = 19, type = FieldTypes.CLASS, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportLocation", seqXLS = 73, type = FieldTypes.CLASS, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public LocationForCostDef getLocation() {
		return location;
	}

	/**
	 * Sets location
	 * 
	 * @param location
	 *            the location to set
	 */
	public void setLocation(LocationForCostDef location) {
		this.location = location;
	}

	/**
	 * Stores cfPivaVta value of entity.
	 */
	@Column(name = "cf_piva_vta")
	private String cfPivaVta;

	/**
	 * Stores issuesNames value of entity.
	 */
	@Column(name = "issues_names")
	private String issuesNames;

	/**
	 * Stores documentAmount value of entity.
	 */
	@Column(name = "document_amount")
	private Double documentAmount;

	/**
	 * Stores iva value of entity.
	 */
	@Column
	private Double iva;

	/**
	 * Stores total value of entity.
	 */
	@Column
	private Double total;

	/**
	 * Stores retain value of entity.
	 */
	@Column
	private Double retain;

	/**
	 * Stores toPay value of entity.
	 */
	@Column(name = "to_pay")
	private Double toPay;

	/**
	 * Stores amountProjectRelate value of entity.
	 */
	@Column(name = "amount_project_relate")
	private Double amountProjectRelate;

	/**
	 * Stores costItem value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "cost_item_id")
	private CostItems costItem;

	@ManyToOne
	@JoinColumn(name = "giuridical_engages_id")
	private GiuridicalEngages giuridicalEngages;

	/**
	 * Stores costNote value of entity.
	 */
	@Column(name = "cost_note")
	private String costNote;

	/**
	 * Stores costDocument value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "cost_document_id")
	private Documents costDocument;

	/**
	 * Stores paymentWay value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "payment_way_id")
	private PaymentWays paymentWay;

	/**
	 * Stores paymentType value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "payment_type_id")
	private PaymentTypes paymentType;

	/**
	 * Stores paymentNumber value of entity.
	 */
	@Column(name = "payment_number")
	private String paymentNumber;

	/**
	 * Stores paymentDate value of entity.
	 */
	@Column(name = "payment_date")
	private Date paymentDate;

	/**
	 * Stores paymentAmount value of entity.
	 */
	@Column(name = "payment_amount")
	private Double paymentAmount;

	/**
	 * Stores externalCosts value of entity.
	 */
	@Column(name = "external_costs")
	private Boolean externalCosts;

	/**
	 * Stores paymentNotes value of entity.
	 */
	@Column(name = "payment_notes")
	private String paymentNotes;

	/**
	 * Stores paymentDocument value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "payment_document_id")
	private Documents paymentDocument;

	@ManyToOne
	@JoinColumn(name = "payment_attachment_document_id")
	private Documents paymentAttachmentDocument;

	/**
	 * Stores amountReport value of entity.
	 */
	@Column(name = "amount_report")
	private Double amountReport;

	@Column(name = "public_amount_report")
	private Double publicAmountReport;

	@Column(name = "private_amount_report")
	private Double privateAmountReport;

	@Column(name = "amount_to_cover_advance_state_aid")
	private Double amountToCoverAdvanceStateAid;

	/**
	 * Stores amountNotes value of entity.
	 */
	@Column(name = "amount_notes")
	private String amountNotes;

	/**
	 * Stores costState value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "cost_state_id")
	private CostStates costState;

	@Column(name = "cil_check")
	private Double cilCheck;

	@Column(name = "cil_check_public_amount")
	private Double cilCheckPublicAmount;

	@Column(name = "cil_check_private_amount")
	private Double cilCheckPrivateAmount;

	@Column(name = "cil_check_additional_finansing_amount")
	private Double cilCheckAdditionalFinansingAmount;

	@Column(name = "cil_check_state_aid_amount")
	private Double cilCheckStateAidAmount;

	@Column(name = "cil_check_outside_eligible_areas")
	private Double cilCheckOutsideEligibleAreas;

	@Column(name = "cil_check_inkind_contributions")
	private Double cilCheckInkindContributions;

	@Column(name = "cil_date")
	private Date cilDate;

	@Column(name = "cf_check")
	private Double cfCheck;

	@Column(name = "cf_check_public_amount")
	private Double cfCheckPublicAmount;

	@Column(name = "cf_check_private_amount")
	private Double cfCheckPrivateAmount;

	@Column(name = "cf_check_additional_finansing_amount")
	private Double cfCheckAdditionalFinansingAmount;

	@Column(name = "cf_check_state_aid_amount")
	private Double cfCheckStateAidAmount;

	@Column(name = "cf_check_outside_eligible_areas")
	private Double cfCheckOutsideEligibleAreas;

	@Column(name = "cf_check_inkind_contributions")
	private Double cfCheckInkindContributions;

	@Column(name = "cf_date")
	private Date cfDate;

	@Column(name = "stc_certification")
	private Double stcCertification;

	@Column(name = "stc_check_public_amount")
	private Double stcCheckPublicAmount;

	@Column(name = "stc_check_private_amount")
	private Double stcCheckPrivateAmount;

	@Column(name = "stc_check_additional_finansing_amount")
	private Double stcCheckAdditionalFinansingAmount;

	@Column(name = "stc_check_state_aid_amount")
	private Double stcCheckStateAidAmount;

	@Column(name = "stc_check_outside_eligible_areas")
	private Double stcCheckOutsideEligibleAreas;

	@Column(name = "stc_check_inkind_contributions")
	private Double stcCheckInkindContributions;

	@Column(name = "agu_certification")
	private Double aguCertification;

	@Column(name = "agu_check_public_amount")
	private Double aguCheckPublicAmount;

	@Column(name = "agu_check_private_amount")
	private Double aguCheckPrivateAmount;

	@Column(name = "agu_check_additional_finansing_amount")
	private Double aguCheckAdditionalFinansingAmount;

	@Column(name = "agu_check_state_aid_amount")
	private Double aguCheckStateAidAmount;

	@Column(name = "agu_check_outside_eligible_areas")
	private Double aguCheckOutsideEligibleAreas;

	@Column(name = "agu_check_inkind_contributions")
	private Double aguCheckInkindContributions;

	@Column(name = "acu_certification")
	private Double acuCertification;

	@Column(name = "acu_check_public_amount")
	private Double acuCheckPublicAmount;

	@Column(name = "acu_check_private_amount")
	private Double acuCheckPrivateAmount;

	@Column(name = "acu_check_additional_finansing_amount")
	private Double acuCheckAdditionalFinansingAmount;

	@Column(name = "acu_check_state_aid_amount")
	private Double acuCheckStateAidAmount;

	@Column(name = "acu_check_outside_eligible_areas")
	private Double acuCheckOutsideEligibleAreas;

	@Column(name = "acu_check_inkind_contributions")
	private Double acuCheckInkindContributions;

	@Column(name = "stc_certification_date")
	private Date stcCertDate;

	@Column(name = "agu_certification_date")
	private Date aguCertDate;

	@Column(name = "acu_certification_date")
	private Date acuCertDate;

	@Column
	private String acuNote;

	@Column
	private Double recoverAmount;

	@ManyToOne
	@JoinColumn(name = "not_regular_types_id")
	private NotRegularTypes notRegularType;

	private String responsible;

	@Transient
	private String notRegularTypeId;

	@Transient
	private String userInsertedName;

	@Column(name = "agu_sertified")
	private Boolean AGUSertified;

	@Column(name = "stc_sertified")
	private Boolean STCSertified;

	@Column(name = "acu_sertified")
	private Boolean ACUSertified;

	@Column(name = "suspended_by_acu")
	private Boolean suspendedByACU;

	@Column(name = "value_suspend_agu")
	private Double valueSuspendAGU;

	@Column(name = "value_suspend_acu")
	private Double valueSuspendACU;

	@Column(name = "consider_in_dur")
	private Boolean considerInDUR;

	@Column(name = "value_suspend_stc")
	private Double valueSuspendSTC;

	@Column(name = "date_suspend_stc")
	private Date dateSuspendSTC;

	@Column(name = "date_suspend_acu")
	private Date dateSuspendACU;

	@Column(name = "date_suspend_agu")
	private Date dateSuspendAGU;

	@Column(name = "date_suspension_confirmed_flow1")
	private Date dateSuspensionConfirmedFlow1;

	@Column(name = "date_suspension_start_flow2")
	private Date dateSuspensionStartFlow2;

	@Column(name = "date_suspension_confirmed_flow2")
	private Date dateSuspensionConfirmedFlow2;

	@Column(name = "amount_to_out")
	private Double amountToOut;

	@Column(name = "amount_to_suspend")
	private Double amountToSuspend;

	@Column(name = "suspend_second_confirmed_stc")
	private Boolean suspendSecondConfirmedSTC;

	@Column(name = "suspend_second_confirmed_agu")
	private Boolean suspendSecondConfirmedAGU;

	@Column(name = "suspend_second_confirmed_acu")
	private Boolean suspendSecondConfirmedACU;

	@Column(name = "suspend_second_refused_agu")
	private Boolean suspendSecondRefusedAGU;

	@Column(name = "suspend_second_refused_acu")
	private Boolean suspendSecondRefusedACU;

	@Column(name = "suspension_status")
	private Long suspensionStatus;

	@Column(name = "suspend_confirmed_stc")
	private Boolean suspendConfirmedSTC;

	@Column(name = "suspend_confirmed_agu")
	private Boolean suspendConfirmedAGU;

	@Column(name = "suspend_confirmed_acu")
	private Boolean suspendConfirmedACU;

	@Column(name = "suspend_confirmed_acu_date")
	private Date suspendConfirmedACUDate;

	@Column(name = "suspend_refused_agu")
	private Boolean suspendRefusedAGU;

	@Column(name = "suspend_refused_acu")
	private Boolean suspendRefusedACU;

	@Column(name = "suspend_note", length = 255)
	private String suspendNote;

	@Column(name = "suspend_reason_id")
	private Long suspendReasonId;

	@Column(name = "suspend_controller_id")
	private Long selctedSuspendController;

	@Column(name = "suspend_act_number")
	private String suspendActNumber;

	@Column(name = "suspend_act_date")
	private Date suspendActDate;

	@Column(name = "cancel_suspend_act_number")
	private String cancelSuspendActNumber;

	@Column(name = "cancel_suspend_act_date")
	private Date cancelSuspendActDate;

	@Column(name = "cancel_suspend_reason")
	private String cancelSuspendReason;

	@Column(name = "cancel_suspend_note")
	private String cancelSuspendNote;

	@ManyToOne
	@JoinColumn(name = "cancel_suspend_document_id")
	private Documents cancelSuspendDocument;

	@ManyToOne
	@JoinColumn(name = "cancel_dur_recovery_document_id")
	private Documents cancelDurRecoveryDocument;

	@Column(name = "retreat_act_number")
	private String retreatActNumber;

	@Column(name = "dur_recovery_act_number")
	private String durRecoveryActNumber;

	@Column(name = "retreat_act_date")
	private Date retreatActDate;

	@Column(name = "dur_recovery_act_date")
	private Date durRecoveryActDate;

	@Column(name = "retreat_reason_id")
	private Long selctedRetreatReason;

	@Column(name = "retreat_controller_id")
	private Long selctedRetreatController;

	@Column(name = "dur_recovery_reason_id")
	private Long selctedDurRecoveryReason;

	@Column(name = "dur_recovery_act_type_id")
	private Long selctedDurRecoveryActType;

	@Column(name = "retreat_note")
	private String retreatNote;

	@Column(name = "dur_recovery_note")
	private String durRecoveryNote;

	@ManyToOne
	@JoinColumn(name = "retreat_document_id")
	private Documents retreatDocument;

	@Column(name = "rectified_by_stc")
	private Boolean rectifiedBySTC;

	@Column(name = "rectified_by_agu")
	private Boolean rectifiedByAGU;

	@Column(name = "rectified_by_acu")
	private Boolean rectifiedByACU;

	@Column(name = "recovered_by_agu_acu")
	private Boolean recoverededByAGUACU;

	@Column(name = "rectification_amount")
	private Double rectificationAmount;

	@Column(name = "rectification_date")
	private Date rectificationDate;

	@Column(name = "rectification_date_acum")
	private Date rectificationDateAcuManager;

	@Column(name = "rectification_reason_name")
	private String rectificationReason;

	@Column(name = "rectification_state")
	private Long rectificationState;

	@Column(name = "rectification_confirmed_date")
	private Date rectificationConfirmedDate;

	@Column(name = "recovery_amount")
	private Double recoveryAmount;

	@Column(name = "amount_recovered_date")
	private Date amountRecoveredDate;

	@Column(name = "recovery_reason")
	private String recoveryReason;

	@Column
	private Boolean locked;

	@Column(name = "sent_to_aau")
	private Boolean isSentToAAU;

	@Column(name = "validate_by_aau")
	private Boolean validateByAAU;

	@Column(name = "aau_note")
	private String aauNote;

	@Column(name = "import_ada")
	private Double importAda;

	@Column(name = "suspend_percent")
	private Double suspendPercent;

	@Column(name = "suspend_retreat")
	private Double suspendRetreat;

	@Column(name = "dur_recovery_suspend_retreat")
	private Double durRecoverySuspendRetreat;

	// @Column(name = "dur_recovery_import")
	// private Double durRecoveryImport;

	@Column(name = "dur_not_regular_is_recoverable")
	private Boolean durNotRegularIsRecoverable;

	@Column(name = "dur_not_regular_recovery_act_type_id")
	private Long selectedNotRegularDurRecoveryActType;

	@Column(name = "dur_not_regular_act_number")
	private Double durNotRegularActNumber;

	@Column(name = "dur_not_regular_act_date")
	private Date durNotRegularActDate;

	@Column(name = "dur_amount_public_support_reimbursed")
	private Double durAmountPublicSupportReimbursed;

	@Column(name = "dur_Tot_eligible_expenses_public_support_reimbursed")
	private Double durTotEligibleExpensesPublicSupportReimbursed;

	@Column(name = "tot_eligible_expenses_public_support_non_recoverable")
	private Double totEligibleExpensesPublicSupportNonRecoverable;

	@Column(name = "dur_recovery_reimbursement_date_from_beneficiary")
	private Date durRecoveryReimbursementDateFromBeneficiary;
	
	@Column(name = "dur_recovery_reimbursement_date_from_ce")
	private Date durRecoveryReimbursementDateFromCE;	
	
	@Column(name = "dur_recovery_reimbursement_date_from_cms")
	private Date durRecoveryReimbursementDateFromCMS;	

	@Column(name = "dur_recovery_sustain_import")
	private Double durRecoverySustainImport;

	@Column(name = "tot_amount_eligl_exp_retired")
	private Double totAmountEliglExpRetired;

	@Column(name = "tot_amount_publ_exp_retired")
	private Double totAmountPublExpRetired;

	@Column(name = "tot_amount_elig_exp_suspended")
	private Double totAmountEligExpSuspended;

	@Column(name = "tot_amount_publ_exp_suspended")
	private Double totAmountPublExpSuspended;

	@Column(name = "tot_amount_elig_exp_to_be_withdrawn")
	private Double totAmountEligExpToBeWithdrawn;

	@Column(name = "tot_amount_publ_exp_to_be_withdrawn")
	private Double totAmountPublExpToBeWithdrawn;

	@Column(name = "acu_total_certification")
	private Double acuTotalCertification;

	@Transient
	private Boolean canEditFlow;

	@Transient
	private Boolean canResend;

	@Transient
	private Boolean canPerformFinalAction;

	@Transient
	Double stillBeRecovered;

	@Transient
	Boolean secFlowAnnulAvailable;

	@Transient
	private Integer assignedDocumentsCount;

	@Transient
	private String partnerNaming;

	@Column(name = "payment_recive_date")
	private Date paymentReciveDate;

	@Transient
	private Boolean filtred;

	@Transient
	private Boolean suspendSelect;

	@ManyToOne
	@JoinColumn(name = "additional_fesr_info_id")
	private AdditionalFesrInfo additionalFesrInfo;

	public String getSuspendStatusDescription() {
		CostDefinitionSuspendStatus status = CostDefinitionSuspendStatus.getById(getSuspensionStatus());

		return status == null ? "" : status.getDescription();
	}

	public boolean getCanSuspend() {
		return CostDefinitionSuspendStatus.SUSPENDED.getState().equals(getSuspensionStatus());
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
	 * Gets project
	 * 
	 * @return project the project
	 */
	public Projects getProject() {
		return project;
	}

	/**
	 * Sets documentType
	 * 
	 * @param documentType
	 *            the documentType to set
	 */
	public void setDocumentType(DocumentTypes documentType) {
		this.documentType = documentType;
	}

	/**
	 * Gets documentType
	 * 
	 * @return documentType the documentType
	 */
	public DocumentTypes getDocumentType() {
		return documentType;
	}
	
	
	/**
	 * Gets currencyCode.
	 * @return currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * Sets currencyCode.
	 * @param currencyCode
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}		

	/**
	 * Sets motivations
	 * 
	 * @param motivations
	 *            the motivations to set
	 */
	public void setMotivations(String motivations) {
		this.motivations = motivations;
	}

	/**
	 * Gets motivations
	 * 
	 * @return motivations the motivations
	 */
	@Exports({
			@Export(propertyName = "exportMotivations", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportMotivations", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getMotivations() {
		return motivations;
	}

	/**
	 * Sets documentNumber
	 * 
	 * @param documentNumber
	 *            the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	/**
	 * Gets documentNumber
	 * 
	 * @return documentNumber the documentNumber
	 */
	@Exports({
			@Export(propertyName = "exportNumber", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getDocumentNumber() {
		return documentNumber;
	}

	/**
	 * Sets documentDate
	 * 
	 * @param documentDate
	 *            the documentDate to set
	 */
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	/**
	 * Gets documentDate
	 * 
	 * @return documentDate the documentDate
	 */
	@Exports({
			@Export(propertyName = "exportDate", seqXLS = 3, type = FieldTypes.DATE, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportDate", seqXLS = 4, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Date getDocumentDate() {
		return documentDate;
	}

	/**
	 * Sets cfPivaVta
	 * 
	 * @param cfPivaVta
	 *            the cfPivaVta to set
	 */
	public void setCfPivaVta(String cfPivaVta) {
		this.cfPivaVta = cfPivaVta;
	}

	/**
	 * Gets cfPivaVta
	 * 
	 * @return cfPivaVta the cfPivaVta
	 */
	@Exports({
			@Export(propertyName = "exportPI", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportPI", seqXLS = 6, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getCfPivaVta() {
		return cfPivaVta;
	}

	/**
	 * Sets issuesNames
	 * 
	 * @param issuesNames
	 *            the issuesNames to set
	 */
	public void setIssuesNames(String issuesNames) {
		this.issuesNames = issuesNames;
	}

	/**
	 * Gets issuesNames
	 * 
	 * @return issuesNames the issuesNames
	 */
	@Export(propertyName = "exportIssuesNames", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getIssuesNames() {
		return issuesNames;
	}

	/**
	 * Sets documentAmount
	 * 
	 * @param documentAmount
	 *            the documentAmount to set
	 */
	public void setDocumentAmount(Double documentAmount) {
		this.documentAmount = documentAmount;
	}

	/**
	 * Gets documentAmount
	 * 
	 * @return documentAmount the documentAmount
	 */
	@Exports({
			@Export(propertyName = "exportAmount", seqXLS = 5, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportAmount", seqXLS = 7, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getDocumentAmount() {
		return documentAmount;
	}

	/**
	 * Sets iva
	 * 
	 * @param iva
	 *            the iva to set
	 */
	public void setIva(Double iva) {
		this.iva = iva;
	}

	/**
	 * Gets iva
	 * 
	 * @return iva the iva
	 */
	public Double getIva() {
		return iva == null ? 0d : iva;
	}

	/**
	 * Sets total
	 * 
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

	/**
	 * Gets total
	 * 
	 * @return total the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * Sets retain
	 * 
	 * @param retain
	 *            the retain to set
	 */
	public void setRetain(Double retain) {
		this.retain = retain;
	}

	/**
	 * Gets retain
	 * 
	 * @return retain the retain
	 */
	public Double getRetain() {
		return retain == null ? 0d : retain;
	}

	/**
	 * Sets toPay
	 * 
	 * @param toPay
	 *            the toPay to set
	 */
	public void setToPay(Double toPay) {
		this.toPay = toPay;
	}

	/**
	 * Gets toPay
	 * 
	 * @return toPay the toPay
	 */
	public Double getToPay() {
		return toPay;
	}

	/**
	 * Sets amountProjectRelate
	 * 
	 * @param amountProjectRelate
	 *            the amountProjectRelate to set
	 */
	public void setAmountProjectRelate(Double amountProjectRelate) {
		this.amountProjectRelate = amountProjectRelate;
	}

	/**
	 * Gets amountProjectRelate
	 * 
	 * @return amountProjectRelate the amountProjectRelate
	 */
	public Double getAmountProjectRelate() {
		return amountProjectRelate;
	}

	/**
	 * Sets costItem
	 * 
	 * @param costItem
	 *            the costItem to set
	 */
	public void setCostItem(CostItems costItem) {
		this.costItem = costItem;
	}

	/**
	 * Gets costItem
	 * 
	 * @return costItem the costItem
	 */
	@Export(propertyName = "exportCostItem", seqXLS = 2, type = FieldTypes.CLASS, place = ExportPlaces.RectificationManagement)
	public CostItems getCostItem() {
		return costItem;
	}

	@Exports({
			@Export(propertyName = "exportCostItem", seqXLS = 6, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCostItem", seqXLS = 8, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getExportCostItem() {
		//if (getProject() != null && getProject().getAsse() == 3) {
		if (getProject() != null && getProject().getAsse() == 4) {
			return costItemPhaseAsse3 != null ? costItemPhaseAsse3.getValue() : "";
		} else {
			return costItem != null ? costItem.toString() : "";
		}
	}

	/**
	 * Sets costNote
	 * 
	 * @param costNote
	 *            the costNote to set
	 */
	public void setCostNote(String costNote) {
		this.costNote = costNote;
	}

	/**
	 * Gets costNote
	 * 
	 * @return costNote the costNote
	 */
	@Export(propertyName = "exportCostNote", seqXLS = 9, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getCostNote() {
		return costNote;
	}

	/**
	 * Sets costDocument
	 * 
	 * @param costDocument
	 *            the costDocument to set
	 */
	public void setCostDocument(Documents costDocument) {
		this.costDocument = costDocument;
	}

	/**
	 * Gets costDocument
	 * 
	 * @return costDocument the costDocument
	 */
	public Documents getCostDocument() {
		return costDocument;
	}

	/**
	 * Sets paymentWay
	 * 
	 * @param paymentWay
	 *            the paymentWay to set
	 */
	public void setPaymentWay(PaymentWays paymentWay) {
		this.paymentWay = paymentWay;
	}

	/**
	 * Gets paymentWay
	 * 
	 * @return paymentWay the paymentWay
	 */
	public PaymentWays getPaymentWay() {
		return paymentWay;
	}

	/**
	 * Sets paymentType
	 * 
	 * @param paymentType
	 *            the paymentType to set
	 */
	public void setPaymentType(PaymentTypes paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * Gets paymentType
	 * 
	 * @return paymentType the paymentType
	 */
	public PaymentTypes getPaymentType() {
		return paymentType;
	}

	/**
	 * Sets paymentNumber
	 * 
	 * @param paymentNumber
	 *            the paymentNumber to set
	 */
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	/**
	 * Gets paymentNumber
	 * 
	 * @return paymentNumber the paymentNumber
	 */
	@Exports({
			@Export(propertyName = "exportPaymentNumber", seqXLS = 7, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportPaymentNumber", seqXLS = 15, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getPaymentNumber() {
		return paymentNumber;
	}

	/**
	 * Sets paymentDate
	 * 
	 * @param paymentDate
	 *            the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * Gets paymentDate
	 * 
	 * @return paymentDate the paymentDate
	 */
	@Exports({
			@Export(propertyName = "exportPaymentDate", seqXLS = 8, type = FieldTypes.DATE, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportPaymentDate", seqXLS = 16, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * Sets paymentAmount
	 * 
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Gets paymentAmount
	 * 
	 * @return paymentAmount the paymentAmount
	 */
	@Exports({
			@Export(propertyName = "exportPaymentAmount", seqXLS = 9, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportPaymentAmount", seqXLS = 17, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets externalCosts
	 * 
	 * @param externalCosts
	 *            the externalCosts to set
	 */
	public void setExternalCosts(Boolean externalCosts) {
		this.externalCosts = externalCosts;
	}

	/**
	 * Gets externalCosts
	 * 
	 * @return externalCosts the externalCosts
	 */
	public Boolean getExternalCosts() {
		return externalCosts;
	}

	/**
	 * Sets paymentNotes
	 * 
	 * @param paymentNotes
	 *            the paymentNotes to set
	 */
	public void setPaymentNotes(String paymentNotes) {
		this.paymentNotes = paymentNotes;
	}

	/**
	 * Gets paymentNotes
	 * 
	 * @return paymentNotes the paymentNotes
	 */
	@Export(propertyName = "exportPaymentNotes", seqXLS = 19, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getPaymentNotes() {
		return paymentNotes;
	}

	/**
	 * Sets paymentDocument
	 * 
	 * @param paymentDocument
	 *            the paymentDocument to set
	 */
	public void setPaymentDocument(Documents paymentDocument) {
		this.paymentDocument = paymentDocument;
	}

	/**
	 * Gets paymentDocument
	 * 
	 * @return paymentDocument the paymentDocument
	 */
	public Documents getPaymentDocument() {
		return paymentDocument;
	}

	/**
	 * Sets amountReport
	 * 
	 * @param amountReport
	 *            the amountReport to set
	 */
	public void setAmountReport(Double amountReport) {
		this.amountReport = amountReport;
	}

	/**
	 * Gets amountReport
	 * 
	 * @return amountReport the amountReport
	 */
	@Exports({
			@Export(propertyName = "exportAmountReport", seqXLS = 10, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportAmountReport", seqXLS = 18, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getAmountReport() {
		return amountReport;
	}

	/**
	 * Sets publicAmountReport
	 * 
	 * @param publicAmountReport
	 *            the publicAmountReport to set
	 */
	public void setPublicAmountReport(Double publicAmountReport) {
		this.publicAmountReport = publicAmountReport;
	}

	/**
	 * Gets publicAmountReport
	 * 
	 * @return publicAmountReport the publicAmountReport
	 */
	public Double getPublicAmountReport() {
		return publicAmountReport;
	}

	/**
	 * Sets privateAmountReport
	 * 
	 * @param privateAmountReport
	 *            the privateAmountReport to set
	 */
	public void setPrivateAmountReport(Double privateAmountReport) {
		this.privateAmountReport = privateAmountReport;
	}

	/**
	 * Gets privateAmountReport
	 * 
	 * @return privateAmountReport the privateAmountReport
	 */
	public Double getPrivateAmountReport() {
		return privateAmountReport;
	}

	/**
	 * Sets amountNotes
	 * 
	 * @param amountNotes
	 *            the amountNotes to set
	 */
	public void setAmountNotes(String amountNotes) {
		this.amountNotes = amountNotes;
	}

	@Transient
	private String getShortNote(String note) {
		if (note == null) {
			return null;
		} else {
			if (note.length() > 80) {
				return note.substring(0, 80) + " ...";
			} else {
				return note;
			}
		}
	}

	/**
	 * Gets amountNotes
	 * 
	 * @return amountNotes the amountNotes
	 */
	@Export(propertyName = "exportAmountNotes", seqXLS = 67, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getAmountNotes() {
		return amountNotes;
	}

	@Transient
	public String getAmountNotes80() {
		return getShortNote(amountNotes);
	}

	@Transient
	public String getPaymentNotes80() {
		return getShortNote(paymentNotes);
	}

	@Transient
	public String getCostNote80() {
		return getShortNote(costNote);
	}

	/**
	 * Sets costState
	 * 
	 * @param costState
	 *            the costState to set
	 */
	public void setCostState(CostStates costState) {
		this.costState = costState;
	}

	/**
	 * Gets costState
	 * 
	 * @return costState the costState
	 */
	@Export(propertyName = "exportStatus", seqXLS = 17, type = FieldTypes.CLASS, place = ExportPlaces.CostDefinitions)
	public CostStates getCostState() {
		return costState;
	}

	/**
	 * Sets costStateId
	 * 
	 * @param costStateId
	 *            the costStateId to set
	 */
	public void setCostStateId(String costStateId) {
		this.costStateId = costStateId;
	}

	/**
	 * Gets costStateId
	 * 
	 * @return costStateId the costStateId
	 */
	public String getCostStateId() {
		if (this.getCostState() != null) {
			return String.valueOf(this.getCostState().getId());
		}

		return "";
	}

	public String getRealCostStateId() {
		return costStateId;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public Users getUser() {
		return user;
	}

	public void setCilCheck(Double cilCheck) {
		this.cilCheck = cilCheck;
	}

	@Exports({
			@Export(propertyName = "exportCertifiedAmount", seqXLS = 11, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCertifiedAmount", seqXLS = 20, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getCilCheck() {
		if (cilCheck == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getAmountReport();
				}
			} catch (Exception e) {
			}
		}

		return cilCheck;
	}

	public void setStcCertification(Double stcCertification) {
		this.stcCertification = stcCertification;
	}

	@Exports({
			@Export(propertyName = "exportStcCertification", seqXLS = 36, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions),
			@Export(propertyName = "exportStcCertification", seqXLS = 14, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions) })
	public Double getStcCertification() {
		return stcCertification;
	}

	public void setAcuCertification(Double acuCertification) {
		this.acuCertification = acuCertification;
	}

	@Exports({
			@Export(propertyName = "exportAcuCertification", seqXLS = 54, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions),
			@Export(propertyName = "exportAcuCertification", seqXLS = 15, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions) })
	public Double getAcuCertification() {
		return acuCertification;
	}

	public void setAguCertification(Double aguCertification) {
		this.aguCertification = aguCertification;
	}

	@Exports({
			@Export(propertyName = "exportAguCertification", seqXLS = 45, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions),
			@Export(propertyName = "exportAguCertification", seqXLS = 16, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions) })
	public Double getAguCertification() {
		return aguCertification;
	}

	public void setCilDate(Date cilDate) {
		this.cilDate = cilDate;
	}

	@Exports({
			@Export(propertyName = "exportCheckCilDate", seqXLS = 12, type = FieldTypes.DATE, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCheckCilDate", seqXLS = 21, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Date getCilDate() {
		return cilDate;
	}

	/**
	 * Sets selected
	 * 
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Gets selected
	 * 
	 * @return selected the selected
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * Sets durCompilation
	 * 
	 * @param durCompilation
	 *            the durCompilation to set
	 */
	public void setDurCompilation(DurCompilations durCompilation) {
		this.durCompilation = durCompilation;
	}

	public void updateCd(CostDefinitions cd) {
		cd.refucedFromDur = this.refucedFromDur;
		cd.suspensionAmount = this.suspensionAmount;
		cd.previousSuspensionAmount = this.previousSuspensionAmount;
		cd.cut = this.cut;
		cd.suspensionDate = this.suspensionDate;
		cd.wasRefusedByCil = this.wasRefusedByCil;
		cd.amountRecovery = this.amountRecovery;
		cd.amountRetreat = this.amountRetreat;
		cd.costRecovery = this.costRecovery;
		cd.recoveryDate = this.recoveryDate;
		cd.retreatDate = this.retreatDate;
		cd.durRecoveryDate = this.durRecoveryDate;
		cd.isRecoveryRetreat = this.isRecoveryRetreat;
		cd.progressiveId = this.progressiveId;
		cd.cilIntermediateStepSave = this.cilIntermediateStepSave;
		cd.dismiss = this.dismiss;
		cd.tempDocument = this.tempDocument;
		cd.editable = this.editable;
		cd.customNote = this.customNote;
		cd.selected = this.selected;
		cd.selectedResend = this.selectedResend;
		cd.notRegularTempId = this.notRegularTempId;
		cd.notRegularTypeName = this.notRegularTypeName;
		cd.notRegularParentTempId = this.notRegularParentTempId;
		cd.style1 = this.style1;
		cd.title1 = this.title1;
		cd.style2 = this.style2;
		cd.title2 = this.title2;
		cd.durNumber = this.durNumber;
		cd.irregularAmount = this.irregularAmount;
		cd.referenceYear = this.referenceYear;
		cd.certification = this.certification;
		cd.certifyUser = this.certifyUser;
		cd.date = this.date;
		cd.beRecovered = this.beRecovered;
		cd.user = this.user;
		cd.budgetOwner = this.budgetOwner;
		cd.createdByAGU = this.createdByAGU;
		cd.createdByPartner = this.createdByPartner;
		cd.durCompilation = this.durCompilation;
		cd.costDefinitionPhase = this.costDefinitionPhase;
		cd.costStateId = this.costStateId;
		cd.project = this.project;
		cd.documentType = this.documentType;
		cd.motivations = this.motivations;
		cd.documentNumber = this.documentNumber;
		cd.documentDate = this.documentDate;
		cd.cfPivaVta = this.cfPivaVta;
		cd.issuesNames = this.issuesNames;
		cd.documentAmount = this.documentAmount;
		cd.iva = this.iva;
		cd.total = this.total;
		cd.retain = this.retain;
		cd.toPay = this.toPay;
		cd.amountProjectRelate = this.amountProjectRelate;
		cd.costItem = this.costItem;
		cd.costNote = this.costNote;
		cd.costDocument = this.costDocument;
		cd.paymentWay = this.paymentWay;
		cd.paymentType = this.paymentType;
		cd.paymentNumber = this.paymentNumber;
		cd.paymentDate = this.paymentDate;
		cd.paymentAmount = this.paymentAmount;
		cd.externalCosts = this.externalCosts;
		cd.paymentNotes = this.paymentNotes;
		cd.paymentDocument = this.paymentDocument;
		cd.amountReport = this.amountReport;
		cd.amountNotes = this.amountNotes;
		cd.costState = this.costState;
		cd.cilCheck = this.cilCheck;
		cd.cilCheckPublicAmount = this.cilCheckPublicAmount;
		cd.cilCheckPrivateAmount = this.cilCheckPrivateAmount;
		cd.cilCheckAdditionalFinansingAmount = this.cilCheckAdditionalFinansingAmount;
		cd.cilCheckStateAidAmount = this.cilCheckStateAidAmount;
		cd.cilCheckInkindContributions = this.cilCheckInkindContributions;
		cd.cilDate = this.cilDate;
		cd.cfCheck = this.cfCheck;
		cd.cfCheckPublicAmount = this.cfCheckPublicAmount;
		cd.cfCheckPrivateAmount = this.cfCheckPrivateAmount;
		cd.cfCheckAdditionalFinansingAmount = this.cfCheckAdditionalFinansingAmount;
		cd.cfCheckStateAidAmount = this.cfCheckStateAidAmount;
		cd.cfCheckInkindContributions = this.cfCheckInkindContributions;
		cd.cfDate = this.cfDate;
		cd.stcCertification = this.stcCertification;
		cd.stcCheckPublicAmount = this.stcCheckPublicAmount;
		cd.stcCheckPrivateAmount = this.stcCheckPrivateAmount;
		cd.stcCheckAdditionalFinansingAmount = this.stcCheckAdditionalFinansingAmount;
		cd.stcCheckStateAidAmount = this.stcCheckStateAidAmount;
		cd.stcCheckInkindContributions = this.stcCheckInkindContributions;
		cd.aguCertification = this.aguCertification;
		cd.aguCheckPublicAmount = this.aguCheckPublicAmount;
		cd.aguCheckPrivateAmount = this.aguCheckPrivateAmount;
		cd.aguCheckAdditionalFinansingAmount = this.aguCheckAdditionalFinansingAmount;
		cd.aguCheckStateAidAmount = this.aguCheckStateAidAmount;
		cd.aguCheckInkindContributions = this.aguCheckInkindContributions;
		cd.acuCertification = this.acuCertification;
		cd.acuCheckPublicAmount = this.acuCheckPublicAmount;
		cd.acuCheckPrivateAmount = this.acuCheckPrivateAmount;
		cd.acuCheckAdditionalFinansingAmount = this.acuCheckAdditionalFinansingAmount;
		cd.acuCheckStateAidAmount = this.acuCheckStateAidAmount;
		cd.acuCheckInkindContributions = this.acuCheckInkindContributions;
		cd.stcCertDate = this.stcCertDate;
		cd.aguCertDate = this.aguCertDate;
		cd.acuCertDate = this.acuCertDate;
		cd.acuNote = this.acuNote;
		cd.recoverAmount = this.recoverAmount;
		cd.notRegularType = this.notRegularType;
		cd.responsible = this.responsible;
		cd.notRegularTypeId = this.notRegularTypeId;
		cd.userInsertedName = this.userInsertedName;
		cd.AGUSertified = this.AGUSertified;
		cd.STCSertified = this.STCSertified;
		cd.ACUSertified = this.ACUSertified;
		cd.suspendedByACU = this.suspendedByACU;
		cd.valueSuspendAGU = this.valueSuspendAGU;
		cd.valueSuspendACU = this.valueSuspendACU;
		cd.valueSuspendSTC = this.valueSuspendSTC;
		cd.dateSuspendSTC = this.dateSuspendSTC;
		cd.dateSuspendACU = this.dateSuspendACU;
		cd.dateSuspendAGU = this.dateSuspendAGU;
		cd.dateSuspensionConfirmedFlow1 = this.dateSuspensionConfirmedFlow1;
		cd.dateSuspensionStartFlow2 = this.dateSuspensionStartFlow2;
		cd.dateSuspensionConfirmedFlow2 = this.dateSuspensionConfirmedFlow2;
		cd.amountToOut = this.amountToOut;
		cd.amountToSuspend = this.amountToSuspend;
		cd.suspendSecondConfirmedSTC = this.suspendSecondConfirmedSTC;
		cd.suspendSecondConfirmedAGU = this.suspendSecondConfirmedAGU;
		cd.suspendSecondConfirmedACU = this.suspendSecondConfirmedACU;
		cd.suspendSecondRefusedAGU = this.suspendSecondRefusedAGU;
		cd.suspendSecondRefusedACU = this.suspendSecondRefusedACU;
		cd.suspensionStatus = this.suspensionStatus;
		cd.suspendConfirmedSTC = this.suspendConfirmedSTC;
		cd.suspendConfirmedAGU = this.suspendConfirmedAGU;
		cd.suspendConfirmedACU = this.suspendConfirmedACU;
		cd.suspendConfirmedACUDate = this.suspendConfirmedACUDate;
		cd.suspendRefusedAGU = this.suspendRefusedAGU;
		cd.suspendRefusedACU = this.suspendRefusedACU;
		cd.suspendNote = this.suspendNote;
		cd.rectifiedBySTC = this.rectifiedBySTC;
		cd.rectifiedByAGU = this.rectifiedByAGU;
		cd.rectifiedByACU = this.rectifiedByACU;
		cd.recoverededByAGUACU = this.recoverededByAGUACU;
		cd.rectificationAmount = this.rectificationAmount;
		cd.rectificationDate = this.rectificationDate;
		cd.rectificationDateAcuManager = this.rectificationDateAcuManager;
		cd.rectificationReason = this.rectificationReason;
		cd.rectificationState = this.rectificationState;
		cd.rectificationConfirmedDate = this.rectificationConfirmedDate;
		cd.recoveryAmount = this.recoveryAmount;
		cd.amountRecoveredDate = this.amountRecoveredDate;
		cd.recoveryReason = this.recoveryReason;
		cd.canEditFlow = this.canEditFlow;
		cd.canResend = this.canResend;
		cd.canPerformFinalAction = this.canPerformFinalAction;
		cd.stillBeRecovered = this.stillBeRecovered;
		cd.secFlowAnnulAvailable = this.secFlowAnnulAvailable;
		cd.assignedDocumentsCount = this.assignedDocumentsCount;
		cd.paymentTypeCode = this.paymentTypeCode;
		cd.paymentMotivationCode = this.paymentMotivationCode;
	}

	/**
	 * Gets durCompilation
	 * 
	 * @return durCompilation the durCompilation
	 */
	public DurCompilations getDurCompilation() {
		return durCompilation;
	}

	/**
	 * Sets recoverAmount
	 * 
	 * @param recoverAmount
	 *            the recoverAmount to set
	 */
	public void setRecoverAmount(Double recoverAmount) {
		this.recoverAmount = recoverAmount;
	}

	/**
	 * Gets recoverAmount
	 * 
	 * @return recoverAmount the recoverAmount
	 */
	public Double getRecoverAmount() {
		return recoverAmount;
	}

	/**
	 * Sets notRegularType
	 * 
	 * @param notRegularType
	 *            the notRegularType to set
	 */
	public void setNotRegularType(NotRegularTypes notRegularType) {
		this.notRegularType = notRegularType;
	}

	/**
	 * Gets notRegularType
	 * 
	 * @return notRegularType the notRegularType
	 */
	public NotRegularTypes getNotRegularType() {
		return notRegularType;
	}

	/**
	 * Sets responsible
	 * 
	 * @param responsible
	 *            the responsible to set
	 */
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	/**
	 * Gets responsible
	 * 
	 * @return responsible the responsible
	 */
	public String getResponsible() {
		return responsible;
	}

	/**
	 * Sets notRegularTypeId
	 * 
	 * @param notRegularTypeId
	 *            the notRegularTypeId to set
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 */
	public void setNotRegularTypeId(String notRegularTypeId)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (!notRegularTypeId.equals(String.valueOf(SelectItemHelper.getFirst().getValue()))) {
			NotRegularTypes type = BeansFactory.NotRegularTypes().Load(notRegularTypeId);
			this.setNotRegularType(type);
		}

		this.notRegularTypeId = notRegularTypeId;
	}

	/**
	 * Gets notRegularTypeId
	 * 
	 * @return notRegularTypeId the notRegularTypeId
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	public String getNotRegularTypeId() throws HibernateException, PersistenceBeanException {
		if (this.getNotRegularType() != null) {
			return String.valueOf(this.getNotRegularType().getId());
		} else {
			return String.valueOf(SelectItemHelper.getFirst().getValue());
		}
	}

	/**
	 * Sets customNote
	 * 
	 * @param customNote
	 *            the customNote to set
	 */
	public void setCustomNote(String customNote) {
		this.customNote = customNote;
	}

	/**
	 * Gets customNote
	 * 
	 * @return customNote the customNote
	 */
	public String getCustomNote() {
		return customNote;
	}

	/**
	 * Sets createdByAGU
	 * 
	 * @param createdByAGU
	 *            the createdByAGU to set
	 */
	public void setCreatedByAGU(boolean createdByAGU) {
		this.createdByAGU = createdByAGU;
	}

	/**
	 * Gets createdByAGU
	 * 
	 * @return createdByAGU the createdByAGU
	 */
	public boolean getCreatedByAGU() {
		return createdByAGU;
	}

	/**
	 * Sets editable
	 * 
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Gets editable
	 * 
	 * @return editable the editable
	 */
	public boolean getEditable() {
		return editable;
	}

	/**
	 * Sets userInsertedName
	 * 
	 * @param userInsertedName
	 *            the userInsertedName to set
	 */
	public void setUserInsertedName(String userInsertedName) {
		this.userInsertedName = userInsertedName;
	}

	/**
	 * Gets userInsertedName
	 * 
	 * @return userInsertedName the userInsertedName
	 */
	@Exports({
			@Export(propertyName = "exportInsertedBy", seqXLS = 18, type = FieldTypes.STRING, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportInsertedBy", seqXLS = 72, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getUserInsertedName() {
		return userInsertedName;
	}

	/**
	 * Sets stcCertDate
	 * 
	 * @param stcCertDate
	 *            the stcCertDate to set
	 */
	public void setStcCertDate(Date stcCertDate) {
		this.stcCertDate = stcCertDate;
	}

	/**
	 * Gets stcCertDate
	 * 
	 * @return stcCertDate the stcCertDate
	 */
	@Export(propertyName = "exportStcCertDate", seqXLS = 37, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Date getStcCertDate() {
		return stcCertDate;
	}

	/**
	 * Sets aguCertDate
	 * 
	 * @param aguCertDate
	 *            the aguCertDate to set
	 */
	public void setAguCertDate(Date aguCertDate) {
		this.aguCertDate = aguCertDate;
	}

	/**
	 * Gets aguCertDate
	 * 
	 * @return aguCertDate the aguCertDate
	 */
	@Export(propertyName = "exportAguCertDate", seqXLS = 46, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Date getAguCertDate() {
		return aguCertDate;
	}

	/**
	 * Sets acuCertDate
	 * 
	 * @param acuCertDate
	 *            the acuCertDate to set
	 */
	public void setAcuCertDate(Date acuCertDate) {
		this.acuCertDate = acuCertDate;
	}

	/**
	 * Gets acuCertDate
	 * 
	 * @return acuCertDate the acuCertDate
	 */
	@Export(propertyName = "exportAcuCertDate", seqXLS = 55, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Date getAcuCertDate() {
		return acuCertDate;
	}

	/**
	 * Sets style1
	 * 
	 * @param style1
	 *            the style1 to set
	 */
	public void setStyle1(String style1) {
		this.style1 = style1;
	}

	/**
	 * Gets style1
	 * 
	 * @return style1 the style1
	 */
	public String getStyle1() {
		return style1;
	}

	/**
	 * Sets title1
	 * 
	 * @param title1
	 *            the title1 to set
	 */
	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	/**
	 * Gets title1
	 * 
	 * @return title1 the title1
	 */
	public String getTitle1() {
		return title1;
	}

	/**
	 * Sets style2
	 * 
	 * @param style2
	 *            the style2 to set
	 */
	public void setStyle2(String style2) {
		this.style2 = style2;
	}

	/**
	 * Gets style2
	 * 
	 * @return style2 the style2
	 */
	public String getStyle2() {
		return style2;
	}

	/**
	 * Sets title2
	 * 
	 * @param title2
	 *            the title2 to set
	 */
	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	/**
	 * Gets title2
	 * 
	 * @return title2 the title2
	 */
	public String getTitle2() {
		return title2;
	}

	/**
	 * Sets NotRegularTempId
	 * 
	 * @param notRegularTempId
	 *            the notRegularTempId to set
	 */
	public void setNotRegularTempId(String notRegularTempId) {
		this.notRegularTempId = notRegularTempId;
	}

	/**
	 * Gets NotRegularTempId
	 * 
	 * @return NotRegularTempId the notRegularTempId
	 */
	public String getNotRegularTempId() {
		return notRegularTempId;
	}

	/**
	 * Sets cfDate
	 * 
	 * @param cfDate
	 *            the cfDate to set
	 */
	public void setCfDate(Date cfDate) {
		this.cfDate = cfDate;
	}

	/**
	 * Gets cfDate
	 * 
	 * @return cfDate the cfDate
	 */
	@Export(propertyName = "exportCfDate", seqXLS = 29, type = FieldTypes.DATE, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Date getCfDate() {
		return cfDate;
	}

	/**
	 * Sets cfCheck
	 * 
	 * @param cfCheck
	 *            the cfCheck to set
	 */
	public void setCfCheck(Double cfCheck) {
		this.cfCheck = cfCheck;
	}

	/**
	 * Gets cfCheck
	 * 
	 * @return cfCheck the cfCheck
	 */
	@Exports({
			@Export(propertyName = "exportCfCertification", seqXLS = 13, type = FieldTypes.MONEY, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCfCertification", seqXLS = 28, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getCfCheck() {
		return cfCheck;
	}

	/**
	 * Sets createdByPartner
	 * 
	 * @param createdByPartner
	 *            the createdByPartner to set
	 */
	public void setCreatedByPartner(Boolean createdByPartner) {
		this.createdByPartner = createdByPartner;
	}

	/**
	 * Gets createdByPartner
	 * 
	 * @return createdByPartner the createdByPartner
	 */
	public Boolean getCreatedByPartner() {
		return createdByPartner == null ? false : createdByPartner;
	}

	/**
	 * Sets notRegularParentTempId
	 * 
	 * @param notRegularParentTempId
	 *            the notRegularParentTempId to set
	 */
	public void setNotRegularParentTempId(String notRegularParentTempId) {
		this.notRegularParentTempId = notRegularParentTempId;
	}

	/**
	 * Gets notRegularParentTempId
	 * 
	 * @return notRegularParentTempId the notRegularParentTempId
	 */
	public String getNotRegularParentTempId() {
		return notRegularParentTempId;
	}

	/**
	 * Sets notRegularTypeName
	 * 
	 * @param notRegularTypeName
	 *            the notRegularTypeName to set
	 */
	public void setNotRegularTypeName(String notRegularTypeName) {
		this.notRegularTypeName = notRegularTypeName;
	}

	/**
	 * Gets notRegularTypeName
	 * 
	 * @return notRegularTypeName the notRegularTypeName
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	@Export(propertyName = "exportNotRegularType", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.NotRegular)
	public String getNotRegularTypeName() throws HibernateException, PersistenceBeanException {
		return this.getNotRegularType() != null ? this.getNotRegularType().getValue() : "";
	}

	/**
	 * Sets costDefinitionPhase
	 * 
	 * @param costDefinitionPhase
	 *            the costDefinitionPhase to set
	 */
	public void setCostDefinitionPhase(CostDefinitionPhases costDefinitionPhase) {
		this.costDefinitionPhase = costDefinitionPhase;
	}

	/**
	 * Gets costDefinitionPhase
	 * 
	 * @return costDefinitionPhase the costDefinitionPhase
	 */
	public CostDefinitionPhases getCostDefinitionPhase() {
		return costDefinitionPhase;
	}

	@Exports({
			@Export(propertyName = "exportCostDefinitionPhase", seqXLS = 20, type = FieldTypes.CLASS, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCostDefinitionPhase", seqXLS = 74, type = FieldTypes.CLASS, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public String getExportCostDefinitionPhase() {
		//if (getProject() != null && getProject().getAsse() == 3) {
		if (getProject() != null && getProject().getAsse() == 4) {
			return costItemPhaseAsse3 != null ? costItemPhaseAsse3.getPhase().toString() : "";
		} else {
			return costDefinitionPhase != null ? costDefinitionPhase.toString() : "";
		}
	}

	/**
	 * Sets acuNote
	 * 
	 * @param acuNote
	 *            the acuNote to set
	 */
	public void setAcuNote(String acuNote) {
		this.acuNote = acuNote;
	}

	/**
	 * Gets acuNote
	 * 
	 * @return acuNote the acuNote
	 */
	@Export(propertyName = "exportAcuNote", seqXLS = 66, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getAcuNote() {
		return acuNote;
	}

	/**
	 * Sets cilIntermediateStepSave
	 * 
	 * @param cilIntermediateStepSave
	 *            the cilIntermediateStepSave to set
	 */
	public void setCilIntermediateStepSave(Boolean cilIntermediateStepSave) {
		this.cilIntermediateStepSave = cilIntermediateStepSave;
	}

	/**
	 * Gets cilIntermediateStepSave
	 * 
	 * @return cilIntermediateStepSave the cilIntermediateStepSave
	 */
	public boolean getCilIntermediateStepSave() {
		return this.cilIntermediateStepSave == null ? false : this.cilIntermediateStepSave;
	}

	/**
	 * Sets AGUSertified
	 * 
	 * @param aGUSertified
	 *            the aGUSertified to set
	 */
	public void setAGUSertified(Boolean aGUSertified) {
		AGUSertified = aGUSertified;
	}

	/**
	 * Gets AGUSertified
	 * 
	 * @return AGUSertified the aGUSertified
	 */
	public Boolean getAGUSertified() {
		return AGUSertified == null ? false : AGUSertified;
	}

	/**
	 * Sets STCSertified
	 * 
	 * @param sTCSertified
	 *            the sTCSertified to set
	 */
	public void setSTCSertified(Boolean sTCSertified) {
		STCSertified = sTCSertified;
	}

	/**
	 * Gets STCSertified
	 * 
	 * @return STCSertified the sTCSertified
	 */
	public Boolean getSTCSertified() {
		return STCSertified == null ? false : STCSertified;
	}

	/**
	 * Sets ACUSertified
	 * 
	 * @param aCUSertified
	 *            the aCUSertified to set
	 */
	public void setACUSertified(Boolean aCUSertified) {
		ACUSertified = aCUSertified;
	}

	/**
	 * Gets ACUSertified
	 * 
	 * @return ACUSertified the aCUSertified
	 */
	public Boolean getACUSertified() {
		return ACUSertified == null ? false : ACUSertified;
	}

	/**
	 * Sets tempDocument
	 * 
	 * @param tempDocument
	 *            the tempDocument to set
	 */
	public void setTempDocument(Documents tempDocument) {
		this.tempDocument = tempDocument;
	}

	/**
	 * Gets tempDocument
	 * 
	 * @return tempDocument the tempDocument
	 */
	public Documents getTempDocument() {
		return tempDocument;
	}

	/**
	 * Sets progressiveId
	 * 
	 * @param progressiveId
	 *            the progressiveId to set
	 */
	public void setProgressiveId(Long progressiveId) {
		this.progressiveId = progressiveId;
	}

	/**
	 * Gets progressiveId
	 * 
	 * @return progressiveId the progressiveId
	 */
	@Exports({
			@Export(propertyName = "exportCostId", seqXLS = 0, type = FieldTypes.NUMBER, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportCostId", seqXLS = 1, type = FieldTypes.NUMBER, place = ExportPlaces.DurCompilationsCostDefinitions),
			@Export(propertyName = "exportRectificationManagementCostId", seqXLS = 1, type = FieldTypes.NUMBER, place = ExportPlaces.RectificationManagement) })
	public Long getProgressiveId() {
		return progressiveId;
	}

	/**
	 * Sets dismiss
	 * 
	 * @param dismiss
	 *            the dismiss to set
	 */
	public void setDismiss(Boolean dismiss) {
		this.dismiss = dismiss;
	}

	/**
	 * Gets dismiss
	 * 
	 * @return dismiss the dismiss
	 */
	public Boolean getDismiss() {
		if (this.dismiss || (this.getCostState() != null
				&& this.getCostState().getId().equals(CostStateTypes.Dismiss.getState()))) {
			return true;
		}
		return false;
	}

	/**
	 * Gets durNumber
	 * 
	 * @return durNumber the durNumber
	 */
	public Integer getDurNumber() {
		return durNumber;
	}

	@Transient
	@Exports({
			@Export(propertyName = "exportDurNumber", seqXLS = 0, place = ExportPlaces.DurCompilationsCostDefinitions, type = FieldTypes.NUMBER),
			@Export(propertyName = "exportDurNumber", seqXLS = 0, place = ExportPlaces.RectificationManagement, type = FieldTypes.NUMBER) })
	public Integer getDurNumberTransient() {
		try {
			return BeansFactory.DurInfos().LoadByDurCompilation(this.getDurCompilation().getId()).getDurInfoNumber();
		} catch (Exception e) {

		}
		return null;
	}

	@Exports({
			@Export(propertyName = "exportDurNumberCount", seqXLS = 76, place = ExportPlaces.DurCompilationsCostDefinitions, type = FieldTypes.NUMBER),
			@Export(propertyName = "exportDurNumberCount", seqXLS = 23, place = ExportPlaces.CostDefinitions, type = FieldTypes.NUMBER) })
	public Integer getDurNumberCountTransient() {
		return getDurNumberTransient();
	}

	@Transient
	public void setDurNumberTransient(Integer durNumberTransient) {

	}

	@Transient
	public String getTestField() {
		return "testStr";
	}

	/**
	 * Sets durNumber
	 * 
	 * @param durNumber
	 *            the durNumber to set
	 */
	public void setDurNumber(Integer durNumber) {
		this.durNumber = durNumber;
	}

	/**
	 * Gets irregularAmount
	 * 
	 * @return irregularAmount the irregularAmount
	 */
	@Export(propertyName = "notRegularEditIrregularAmount", seqXLS = 1, type = FieldTypes.DOUBLE, place = ExportPlaces.NotRegular)
	public Double getIrregularAmount() {
		return irregularAmount;
	}

	/**
	 * Sets irregularAmount
	 * 
	 * @param irregularAmount
	 *            the irregularAmount to set
	 */
	public void setIrregularAmount(Double irregularAmount) {
		this.irregularAmount = irregularAmount;
	}

	/**
	 * Gets referenceYear
	 * 
	 * @return referenceYear the referenceYear
	 */
	@Export(propertyName = "notRegularEditReferenceYear", seqXLS = 2, type = FieldTypes.NUMBER, place = ExportPlaces.NotRegular)
	public Integer getReferenceYear() {
		return referenceYear;
	}

	/**
	 * Sets referenceYear
	 * 
	 * @param referenceYear
	 *            the referenceYear to set
	 */
	public void setReferenceYear(Integer referenceYear) {
		this.referenceYear = referenceYear;
	}

	/**
	 * Gets certification
	 * 
	 * @return certification the certification
	 */
	@Export(propertyName = "notRegularEditCertification", seqXLS = 3, type = FieldTypes.BOOLEAN, place = ExportPlaces.NotRegular)
	public Boolean getCertification() {
		return certification;
	}

	@Transient
	@Export(propertyName = "exportAmountCertificate", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.RectificationManagement)
	public Double getAmountCertificate() {
		if (this.getRectifiedBySTC()) {
			return this.getCilCheck();
		} else if (this.getRectifiedByAGU()) {
			return this.getStcCertification();
		} else if (this.getRectifiedByACU()) {
			return this.getAguCertification();
		} else {
			return null;
		}
	}

	/**
	 * Sets certification
	 * 
	 * @param certification
	 *            the certification to set
	 */
	public void setCertification(Boolean certification) {
		this.certification = certification;
	}

	/**
	 * Gets certifyUser
	 * 
	 * @return certifyUser the certifyUser
	 */
	public Users getCertifyUser() {
		return certifyUser;
	}

	/**
	 * Sets certifyUser
	 * 
	 * @param certifyUser
	 *            the certifyUser to set
	 */
	public void setCertifyUser(Users certifyUser) {
		this.certifyUser = certifyUser;
	}

	/**
	 * Gets date
	 * 
	 * @return date the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets date
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets beRecovered
	 * 
	 * @return beRecovered the beRecovered
	 */
	public Boolean isBeRecovered() {
		return beRecovered;
	}

	/**
	 * Sets beRecovered
	 * 
	 * @param beRecovered
	 *            the beRecovered to set
	 */
	public void setBeRecovered(Boolean beRecovered) {
		this.beRecovered = beRecovered;
	}

	/**
	 * Sets createdByAGU
	 * 
	 * @param createdByAGU
	 *            the createdByAGU to set
	 */
	public void setCreatedByAGU(Boolean createdByAGU) {
		this.createdByAGU = createdByAGU;
	}

	/**
	 * Gets amountRecovery
	 * 
	 * @return amountRecovery the amountRecovery
	 */
	@Export(propertyName = "notRegularAmountRecovery", seqXLS = 6, type = FieldTypes.MONEY, place = ExportPlaces.NotRegular)
	public Double getAmountRecovery() {
		return amountRecovery;
	}

	/**
	 * Sets amountRecovery
	 * 
	 * @param amountRecovery
	 *            the amountRecovery to set
	 */
	public void setAmountRecovery(Double amountRecovery) {
		this.amountRecovery = amountRecovery;
	}

	/**
	 * Gets amountRetreat
	 * 
	 * @return amountRetreat the amountRetreat
	 */
	@Exports({
			@Export(propertyName = "notRegularAmountRetreat", seqXLS = 8, type = FieldTypes.MONEY, place = ExportPlaces.NotRegular),
			@Export(propertyName = "exportAmountRetreat", seqXLS = 59, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getAmountRetreat() {
		return amountRetreat;
	}

	/**
	 * Sets amountRetreat
	 * 
	 * @param amountRetreat
	 *            the amountRetreat to set
	 */
	public void setAmountRetreat(Double amountRetreat) {
		this.amountRetreat = amountRetreat;
	}

	/**
	 * @return the costRecovery
	 */
	@Exports({
			@Export(propertyName = "notRegularCostRecovery", seqXLS = 9, type = FieldTypes.MONEY, place = ExportPlaces.NotRegular),
			@Export(propertyName = "exportCostRecovery", seqXLS = 60, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Double getCostRecovery() {
		return costRecovery;
	}

	/**
	 * @param costRecovery
	 *            the costRecovery to set
	 */
	public void setCostRecovery(Double costRecovery) {
		this.costRecovery = costRecovery;
	}

	/**
	 * Gets recoveryDate
	 * 
	 * @return recoveryDate the recoveryDate
	 */
	@Export(propertyName = "notRegularRecoveryDate", seqXLS = 5, type = FieldTypes.DATE, place = ExportPlaces.NotRegular)
	public Date getRecoveryDate() {
		return recoveryDate;
	}

	/**
	 * Sets recoveryDate
	 * 
	 * @param recoveryDate
	 *            the recoveryDate to set
	 */
	public void setRecoveryDate(Date recoveryDate) {
		this.recoveryDate = recoveryDate;
	}

	/**
	 * Gets retreatDate
	 * 
	 * @return retreatDate the retreatDate
	 */
	@Export(propertyName = "notRegularRetreatDate", seqXLS = 7, type = FieldTypes.DATE, place = ExportPlaces.NotRegular)
	public Date getRetreatDate() {
		return retreatDate;
	}

	/**
	 * Sets retreatDate
	 * 
	 * @param retreatDate
	 *            the retreatDate to set
	 */
	public void setRetreatDate(Date retreatDate) {
		this.retreatDate = retreatDate;
	}

	/**
	 * Gets isRecoveryRetreat
	 * 
	 * @return isRecoveryRetreat the isRecoveryRetreat
	 */
	public Boolean getIsRecoveryRetreat() {
		return isRecoveryRetreat;
	}

	/**
	 * Sets isRecoveryRetreat
	 * 
	 * @param isRecoveryRetreat
	 *            the isRecoveryRetreat to set
	 */
	public void setIsRecoveryRetreat(Boolean isRecoveryRetreat) {
		this.isRecoveryRetreat = isRecoveryRetreat;
	}

	/**
	 * Gets beRecovered
	 * 
	 * @return beRecovered the beRecovered
	 */
	@Export(propertyName = "notRegularEditBeRecovered", seqXLS = 4, type = FieldTypes.BOOLEAN, place = ExportPlaces.NotRegular)
	public Boolean getBeRecovered() {
		return beRecovered;
	}

	/**
	 * Sets wasRefusedByCil
	 * 
	 * @param wasRefusedByCil
	 *            the wasRefusedByCil to set
	 */
	public void setWasRefusedByCil(Boolean wasRefusedByCil) {
		this.wasRefusedByCil = wasRefusedByCil;
	}

	/**
	 * Gets wasRefusedByCil
	 * 
	 * @return wasRefusedByCil the wasRefusedByCil
	 */
	public Boolean getWasRefusedByCil() {
		return wasRefusedByCil;
	}

	/**
	 * Sets suspensionAmount
	 * 
	 * @param suspensionAmount
	 *            the suspensionAmount to set
	 */
	public void setSuspensionAmount(Double suspensionAmount) {
		this.suspensionAmount = suspensionAmount;
	}

	/**
	 * Gets suspensionAmount
	 * 
	 * @return suspensionAmount the suspensionAmount
	 */
	public Double getSuspensionAmount() {
		return suspensionAmount;
	}

	/**
	 * Sets suspensionDate
	 * 
	 * @param suspensionDate
	 *            the suspensionDate to set
	 */
	public void setSuspensionDate(Date suspensionDate) {
		this.suspensionDate = suspensionDate;
	}

	/**
	 * Gets suspensionDate
	 * 
	 * @return suspensionDate the suspensionDate
	 */
	public Date getSuspensionDate() {
		return suspensionDate;
	}

	/**
	 * Sets previousSuspensionAmount
	 * 
	 * @param previousSuspensionAmount
	 *            the previousSuspensionAmount to set
	 */
	public void setPreviousSuspensionAmount(Double previousSuspensionAmount) {
		this.previousSuspensionAmount = previousSuspensionAmount;
	}

	/**
	 * Gets previousSuspensionAmount
	 * 
	 * @return previousSuspensionAmount the previousSuspensionAmount
	 */
	public Double getPreviousSuspensionAmount() {
		if (previousSuspensionAmount == null) {
			if (suspendedByACU) {
				return getValueSuspendACU();
			}
		}

		return previousSuspensionAmount;
	}

	/**
	 * Sets cut
	 * 
	 * @param cut
	 *            the cut to set
	 */
	public void setCut(Boolean cut) {
		this.cut = cut;
	}

	/**
	 * Gets cut
	 * 
	 * @return cut the cut
	 */
	public Boolean getCut() {
		return cut;
	}

	/**
	 * Sets refucedFromDur
	 * 
	 * @param refucedFromDur
	 *            the refucedFromDur to set
	 */
	public void setRefucedFromDur(Boolean refucedFromDur) {
		this.refucedFromDur = refucedFromDur;
	}

	/**
	 * Gets refucedFromDur
	 * 
	 * @return refucedFromDur the refucedFromDur
	 */
	public Boolean getRefucedFromDur() {
		return refucedFromDur;
	}

	/**
	 * Gets budgetOwner
	 * 
	 * @return budgetOwner the budgetOwner
	 */
	public Users getBudgetOwner() {
		return budgetOwner;
	}

	/**
	 * Sets budgetOwner
	 * 
	 * @param budgetOwner
	 *            the budgetOwner to set
	 */
	public void setBudgetOwner(Users budgetOwner) {
		this.budgetOwner = budgetOwner;
	}

	public Boolean getSuspendedByACU() {
		if (suspendedByACU == null) {
			return false;
		}
		return suspendedByACU;
	}

	public void setSuspendedByACU(Boolean suspendedByACU) {
		this.suspendedByACU = suspendedByACU;
	}

	@Export(propertyName = "exportSuspendedAgu", seqXLS = 70, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getValueSuspendAGU() {
		return valueSuspendAGU;
	}

	public void setValueSuspendAGU(Double valueSuspendAGU) {
		this.valueSuspendAGU = valueSuspendAGU;
	}

	@Export(propertyName = "exportSuspendedAcu", seqXLS = 58, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getValueSuspendACU() {
		return valueSuspendACU;
	}

	public void setValueSuspendACU(Double valueSuspendACU) {
		this.valueSuspendACU = valueSuspendACU;
	}

	@Export(propertyName = "exportSuspendedStc", seqXLS = 68, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getValueSuspendSTC() {
		return valueSuspendSTC;
	}

	public void setValueSuspendSTC(Double valueSuspendSTC) {
		this.valueSuspendSTC = valueSuspendSTC;
	}

	public Date getDateSuspendSTC() {
		return dateSuspendSTC;
	}

	public void setDateSuspendSTC(Date dateSuspendSTC) {
		this.dateSuspendSTC = dateSuspendSTC;
	}

	public Date getDateSuspendACU() {
		return dateSuspendACU;
	}

	public void setDateSuspendACU(Date dateSuspendACU) {
		this.dateSuspendACU = dateSuspendACU;
	}

	public Date getDateSuspendAGU() {
		return dateSuspendAGU;
	}

	public void setDateSuspendAGU(Date dateSuspendAGU) {
		this.dateSuspendAGU = dateSuspendAGU;
	}

	public Double getAmountToOut() {
		return amountToOut;
	}

	public void setAmountToOut(Double amountToOut) {
		this.amountToOut = amountToOut;
	}

	public Double getAmountToSuspend() {
		return amountToSuspend;
	}

	public void setAmountToSuspend(Double amountToSuspend) {
		this.amountToSuspend = amountToSuspend;
	}

	public Boolean getSuspendConfirmedSTC() {
		return suspendConfirmedSTC == null ? Boolean.FALSE : suspendConfirmedSTC;
	}

	public void setSuspendConfirmedSTC(Boolean suspendConfirmedSTC) {
		this.suspendConfirmedSTC = suspendConfirmedSTC;
	}

	public Boolean getSuspendConfirmedAGU() {
		return suspendConfirmedAGU == null ? Boolean.FALSE : suspendConfirmedAGU;
	}

	public void setSuspendConfirmedAGU(Boolean suspendConfirmedAGU) {
		this.suspendConfirmedAGU = suspendConfirmedAGU;
	}

	public Boolean getSuspendConfirmedACU() {
		if (suspendConfirmedACU == null) {
			return false;
		}
		return suspendConfirmedACU;
	}

	public void setSuspendConfirmedACU(Boolean suspendConfirmedACU) {
		this.suspendConfirmedACU = suspendConfirmedACU;
	}

	public Boolean getSuspendRefusedAGU() {
		return suspendRefusedAGU;
	}

	public void setSuspendRefusedAGU(Boolean suspendRefusedAGU) {
		this.suspendRefusedAGU = suspendRefusedAGU;
	}

	public Boolean getSuspendRefusedACU() {
		return suspendRefusedACU == null ? Boolean.FALSE : suspendRefusedACU;
	}

	public void setSuspendRefusedACU(Boolean suspendRefusedACU) {
		this.suspendRefusedACU = suspendRefusedACU;
	}

	public Date getSuspendConfirmedACUDate() {
		return suspendConfirmedACUDate;
	}

	public void setSuspendConfirmedACUDate(Date suspendConfirmedACUDate) {
		this.suspendConfirmedACUDate = suspendConfirmedACUDate;
	}

	public Long getSuspensionStatus() {
		return suspensionStatus;
	}

	public void setSuspensionStatus(Long suspensionStatus) {
		this.suspensionStatus = suspensionStatus;
	}

	@Transient
	public String getSuspensionStatusName() {
		if (this.getSuspensionStatus() != null) {
			for (CostDefinitionSuspendStatus item : CostDefinitionSuspendStatus.values()) {
				if (item.getState().equals(this.getSuspensionStatus())) {
					return Utils.getString(item.getCode());
				}
			}
		}
		return null;
	}

	public Boolean getSuspendSecondConfirmedSTC() {
		return suspendSecondConfirmedSTC;
	}

	public void setSuspendSecondConfirmedSTC(Boolean suspendSecondConfirmedSTC) {
		this.suspendSecondConfirmedSTC = suspendSecondConfirmedSTC;
	}

	public Boolean getSuspendSecondConfirmedAGU() {
		return suspendSecondConfirmedAGU == null ? Boolean.FALSE : suspendSecondConfirmedAGU;
	}

	public void setSuspendSecondConfirmedAGU(Boolean suspendSecondConfirmedAGU) {
		this.suspendSecondConfirmedAGU = suspendSecondConfirmedAGU;
	}

	public Boolean getSuspendSecondConfirmedACU() {
		return suspendSecondConfirmedACU == null ? Boolean.FALSE : suspendSecondConfirmedACU;
	}

	public void setSuspendSecondConfirmedACU(Boolean suspendSecondConfirmedACU) {
		this.suspendSecondConfirmedACU = suspendSecondConfirmedACU;
	}

	public Boolean getSuspendSecondRefusedAGU() {
		if (suspendSecondRefusedAGU == null) {
			return false;
		}
		return suspendSecondRefusedAGU;
	}

	public void setSuspendSecondRefusedAGU(Boolean suspendSecondRefusedAGU) {
		this.suspendSecondRefusedAGU = suspendSecondRefusedAGU;
	}

	public Boolean getSuspendSecondRefusedACU() {
		return suspendSecondRefusedACU;
	}

	public void setSuspendSecondRefusedACU(Boolean suspendSecondRefusedACU) {
		this.suspendSecondRefusedACU = suspendSecondRefusedACU;
	}

	public Date getDateSuspensionConfirmedFlow1() {
		return dateSuspensionConfirmedFlow1;
	}

	public void setDateSuspensionConfirmedFlow1(Date dateSuspensionConfirmedFlow1) {
		this.dateSuspensionConfirmedFlow1 = dateSuspensionConfirmedFlow1;
	}

	public Date getDateSuspensionConfirmedFlow2() {
		return dateSuspensionConfirmedFlow2;
	}

	public void setDateSuspensionConfirmedFlow2(Date dateSuspensionConfirmedFlow2) {
		this.dateSuspensionConfirmedFlow2 = dateSuspensionConfirmedFlow2;
	}

	public Date getDateSuspensionStartFlow2() {
		return dateSuspensionStartFlow2;
	}

	public void setDateSuspensionStartFlow2(Date dateSuspensionStartFlow2) {
		this.dateSuspensionStartFlow2 = dateSuspensionStartFlow2;
	}

	public String getSuspendNote() {
		return suspendNote;
	}

	public void setSuspendNote(String suspendReason) {
		this.suspendNote = suspendReason;
	}

	@Transient
	public String getSuspensionReasonName() {
		if (this.getSuspendNote() != null) {
			for (SuspensionReasonTypes item : SuspensionReasonTypes.values()) {
				if (item.getState().equals(this.getSuspendNote())) {
					return Utils.getString(item.getCode());
				}
			}
		}
		return null;
	}

	@Transient
	public Boolean getCanEditFlow() {

		return this.canEditFlow;
	}

	@Transient
	public void setCanEditFlow(Boolean canEditFlow) {
		this.canEditFlow = canEditFlow;
	}

	/**
	 * Gets rectifiedBySTC
	 * 
	 * @return rectifiedBySTC the rectifiedBySTC
	 */
	public Boolean getRectifiedBySTC() {
		if (rectifiedBySTC == null) {
			return false;
		}
		return rectifiedBySTC;
	}

	/**
	 * Sets rectifiedBySTC
	 * 
	 * @param rectifiedBySTC
	 *            the rectifiedBySTC to set
	 */
	public void setRectifiedBySTC(Boolean rectifiedBySTC) {
		this.rectifiedBySTC = rectifiedBySTC;
	}

	/**
	 * Gets rectifiedByAGU
	 * 
	 * @return rectifiedByAGU the rectifiedByAGU
	 */
	public Boolean getRectifiedByAGU() {
		if (rectifiedByAGU == null) {
			return false;
		}
		return rectifiedByAGU;
	}

	/**
	 * Sets rectifiedByAGU
	 * 
	 * @param rectifiedByAGU
	 *            the rectifiedByAGU to set
	 */
	public void setRectifiedByAGU(Boolean rectifiedByAGU) {
		this.rectifiedByAGU = rectifiedByAGU;
	}

	/**
	 * Gets rectifiedByACU
	 * 
	 * @return rectifiedByACU the rectifiedByACU
	 */
	public Boolean getRectifiedByACU() {
		if (rectifiedByACU == null) {
			return false;
		}
		return rectifiedByACU;
	}

	/**
	 * Sets rectifiedByACU
	 * 
	 * @param rectifiedByACU
	 *            the rectifiedByACU to set
	 */
	public void setRectifiedByACU(Boolean rectifiedByACU) {
		this.rectifiedByACU = rectifiedByACU;
	}

	/**
	 * @return the recoverededByAGUACU
	 */
	public Boolean getRecoverededByAGUACU() {
		if (recoverededByAGUACU == null) {
			return false;
		}
		return recoverededByAGUACU;
	}

	/**
	 * @param recoverededByAGUACU
	 *            the recoverededByAGUACU to set
	 */
	public void setRecoverededByAGUACU(Boolean recoverededByAGUACU) {
		this.recoverededByAGUACU = recoverededByAGUACU;
	}

	/**
	 * Gets rectificationAmount
	 * 
	 * @return rectificationAmount the rectificationAmount
	 */
	@Export(propertyName = "exportAmountRectified", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.RectificationManagement)
	public Double getRectificationAmount() {
		return rectificationAmount;
	}

	/**
	 * Sets rectificationAmount
	 * 
	 * @param rectificationAmount
	 *            the rectificationAmount to set
	 */
	public void setRectificationAmount(Double rectificationAmount) {
		this.rectificationAmount = rectificationAmount;
	}

	/**
	 * Gets rectificationDate
	 * 
	 * @return rectificationDate the rectificationDate
	 */
	@Export(propertyName = "exportRectificationDate", seqXLS = 5, type = FieldTypes.DATE, place = ExportPlaces.RectificationManagement)
	public Date getRectificationDate() {
		return rectificationDate;
	}

	/**
	 * Sets rectificationDate
	 * 
	 * @param rectificationDate
	 *            the rectificationDate to set
	 */
	public void setRectificationDate(Date rectificationDate) {
		this.rectificationDate = rectificationDate;
	}

	/**
	 * Gets rectificationReason
	 * 
	 * @return rectificationReason the rectificationReason
	 */
	@Export(propertyName = "exportReason", seqXLS = 7, type = FieldTypes.STRING, place = ExportPlaces.RectificationManagement)
	public String getRectificationReason() {
		return rectificationReason;
	}

	/**
	 * Sets rectificationReason
	 * 
	 * @param rectificationReason
	 *            the rectificationReason to set
	 */
	public void setRectificationReason(String rectificationReason) {
		this.rectificationReason = rectificationReason;
	}

	/**
	 * Gets rectificationState
	 * 
	 * @return rectificationState the rectificationState
	 */
	public Long getRectificationState() {
		return rectificationState;
	}

	/**
	 * Sets rectificationState
	 * 
	 * @param rectificationState
	 *            the rectificationState to set
	 */
	public void setRectificationState(Long rectificationState) {
		this.rectificationState = rectificationState;
	}

	/**
	 * Gets rectificationConfirmedDate
	 * 
	 * @return rectificationConfirmedDate the rectificationConfirmedDate
	 */
	public Date getRectificationConfirmedDate() {
		return rectificationConfirmedDate;
	}

	/**
	 * Sets rectificationConfirmedDate
	 * 
	 * @param rectificationConfirmedDate
	 *            the rectificationConfirmedDate to set
	 */
	public void setRectificationConfirmedDate(Date rectificationConfirmedDate) {
		this.rectificationConfirmedDate = rectificationConfirmedDate;
	}

	@Transient
	public Boolean getRectificationNew() {
		return RectificationStateTypes.New.getState().equals(this.getRectificationState());
	}

	@Transient
	public Boolean getRectificationRefused() {
		return RectificationStateTypes.RefusedByAgu.getState().equals(this.getRectificationState())
				|| RectificationStateTypes.RefusedByAcu.getState().equals(this.getRectificationState());
	}

	@Transient
	public Boolean getRectificationSentToAgu() {
		return RectificationStateTypes.SentToAgu.getState().equals(this.getRectificationState());
	}

	@Transient
	public Boolean getRectificationSentToAcu() {
		return RectificationStateTypes.SentToAcu.getState().equals(this.getRectificationState());
	}

	@Transient
	public String getRectificationReasonName() {
		if (this.rectificationReason != null) {
			for (RectificationReasonTypes item : RectificationReasonTypes.values()) {
				if (item.getState().equals(this.rectificationReason)) {
					return Utils.getString(item.getCode());
				}
			}
		}
		return null;
	}

	@Transient
	@Export(propertyName = "exportRectificationState", seqXLS = 8, type = FieldTypes.STRING, place = ExportPlaces.RectificationManagement)
	public String getRectificationStateName() {
		if (this.rectificationState != null) {
			if (this.rectificationState.equals(RectificationStateTypes.New.getState())) {
				if (this.getRectifiedBySTC()) {
					return Utils.getString("rectificationManagementListRectifiedBySTC");
				} else if (this.getRectifiedByAGU()) {
					return Utils.getString("rectificationManagementListRectifiedByAGU");
				} else if (this.getRectifiedByACU()) {
					return Utils.getString("rectificationManagementListRectifiedByACU");
				}
			}
			for (RectificationStateTypes item : RectificationStateTypes.values()) {
				if (item.getState().equals(this.rectificationState)) {
					return Utils.getString(item.getCode());
				}
			}
		}
		return null;
	}

	@Transient
	public Boolean getRectificationConfirmed() {
		return this.getRectificationState().equals(RectificationStateTypes.Confirmed.getState());
	}

	@Transient
	public Boolean getCanPerformFinalAction() {
		return canPerformFinalAction;
	}

	@Transient
	public void setCanPerformFinalAction(Boolean canPerformFinalAction) {
		this.canPerformFinalAction = canPerformFinalAction;
	}

	/**
	 * Gets recoveryAmount
	 * 
	 * @return recoveryAmount the recoveryAmount
	 */
	@Export(propertyName = "exportAmountRecovered", seqXLS = 9, type = FieldTypes.MONEY, place = ExportPlaces.RectificationManagement)
	public Double getRecoveryAmount() {
		return recoveryAmount;
	}

	/**
	 * Sets recoveryAmount
	 * 
	 * @param recoveryAmount
	 *            the recoveryAmount to set
	 */
	public void setRecoveryAmount(Double recoveryAmount) {
		this.recoveryAmount = recoveryAmount;
	}

	/**
	 * Gets amountRecoveredDate
	 * 
	 * @return amountRecoveredDate the amountRecoveredDate
	 */
	public Date getAmountRecoveredDate() {
		return amountRecoveredDate;
	}

	/**
	 * Sets amountRecoveredDate
	 * 
	 * @param amountRecoveredDate
	 *            the amountRecoveredDate to set
	 */
	public void setAmountRecoveredDate(Date amountRecoveredDate) {
		this.amountRecoveredDate = amountRecoveredDate;
	}

	/**
	 * Gets recoveryReason
	 * 
	 * @return recoveryReason the recoveryReason
	 */
	public String getRecoveryReason() {
		return recoveryReason;
	}

	/**
	 * Sets recoveryReason
	 * 
	 * @param recoveryReason
	 *            the recoveryReason to set
	 */
	public void setRecoveryReason(String recoveryReason) {
		this.recoveryReason = recoveryReason;
	}

	@Transient
	@Export(propertyName = "exportAmountStillBeRecovered", seqXLS = 10, type = FieldTypes.MONEY, place = ExportPlaces.RectificationManagement)
	public Double getStillBeRecovered() {
		return stillBeRecovered;
	}

	@Transient
	public void setStillBeRecovered(Double stillBeRecovered) {
		this.stillBeRecovered = stillBeRecovered;
	}

	@Transient
	public Boolean getSecFlowAnnulAvailable() {
		return secFlowAnnulAvailable;
	}

	@Transient
	public void setSecFlowAnnulAvailable(Boolean secFlowAnnulAvailable) {
		this.secFlowAnnulAvailable = secFlowAnnulAvailable;
	}

	@Transient
	public Boolean getCanResend() {
		return canResend;
	}

	@Transient
	public void setCanResend(Boolean canResend) {
		this.canResend = canResend;
	}

	@Transient
	public boolean isSelectedResend() {
		return selectedResend;
	}

	@Transient
	public void setSelectedResend(boolean selectedResend) {
		this.selectedResend = selectedResend;
	}

	@Export(propertyName = "exportRectificationAcuDate", seqXLS = 6, type = FieldTypes.DATE, place = ExportPlaces.RectificationManagement)
	public Date getRectificationDateAcuManager() {
		return rectificationDateAcuManager;
	}

	public void setRectificationDateAcuManager(Date rectificationDateAcuManager) {
		this.rectificationDateAcuManager = rectificationDateAcuManager;
	}

	public Date getSuspendDate() {
		if (this.getSuspendedByACU()) {
			return this.getDateSuspendACU();
		}

		return null;
	}

	/**
	 * Gets assignedDocumentsCount
	 * 
	 * @return assignedDocumentsCount the assignedDocumentsCount
	 */
	public Integer getAssignedDocumentsCount() {
		return assignedDocumentsCount;
	}

	/**
	 * Sets assignedDocumentsCount
	 * 
	 * @param assignedDocumentsCount
	 *            the assignedDocumentsCount to set
	 */
	public void setAssignedDocumentsCount(Integer assignedDocumentsCount) {
		this.assignedDocumentsCount = assignedDocumentsCount;
	}

	@Transient
	@Export(propertyName = "exportRectifiedStc", seqXLS = 69, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getRectifiedStc() {
		return this.getRectifiedBySTC() ? this.getRectificationAmount() : null;
	}

	@Transient
	@Export(propertyName = "exportRectifiedAgu", seqXLS = 71, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getRectifiedAgu() {
		return this.getRectifiedByAGU() ? this.getRectificationAmount() : null;
	}

	@Transient
	public Double getRectifiedAcu() {
		return this.getRectifiedByACU() ? this.getRectificationAmount() : null;
	}

	/**
	 * @param naming
	 */
	public void setPartnerNaming(String naming) {

		if (naming != null) {
			try {
				this.partnerNaming = naming + "-" + user.getRolesNames();
			} catch (Exception e) {
				this.partnerNaming = naming;
			}
		}

	}

	public String getPartnerNaming() {
		return this.partnerNaming;

	}

	@Transient
	public Double getLastCertifiedAmount() {
		if (this.getACUSertified() && this.getAcuCertification() != null) {
			return this.getAcuCertification();
		} else if (this.getAGUSertified() && this.getAguCertification() != null) {
			return this.getAguCertification();
		} else if (this.getSTCSertified() && this.getStcCertification() != null) {
			return this.getStcCertification();
		} else if (this.getCfCheck() != null) {
			return this.getCfCheck();
		} else if (this.getCilCheck() != null) {
			return this.getCilCheck();
		} else {
			return 0d;
		}
	}

	@Transient
	public Boolean getCostNotValidate() {
		if (this.getCostState().getId().equals(CostStateTypes.NotYetValidate.getState())) {
			return true;
		}
		return false;
	}

	/**
	 * Gets filtred
	 * 
	 * @return filtred the filtred
	 */
	@Transient
	public Boolean getFiltred() {
		return filtred;
	}

	/**
	 * Sets filtred
	 * 
	 * @param filtred
	 *            the filtred to set
	 */
	@Transient
	public void setFiltred(Boolean filtred) {
		this.filtred = filtred;
	}

	@Transient
	public String getRowClass() {
		if (filtred) {
			return "visible_row";
		}
		return "not_visible_row";
	}

	/**
	 * Gets locked
	 * 
	 * @return locked the locked
	 */
	public Boolean getLocked() {
		return locked;
	}

	/**
	 * Sets locked
	 * 
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	/**
	 * Gets customNoteWeb
	 * 
	 * @return customNoteWeb the customNoteWeb
	 */
	public String getCustomNoteWeb() {
		return customNoteWeb;
	}

	/**
	 * Sets customNoteWeb
	 * 
	 * @param customNoteWeb
	 *            the customNoteWeb to set
	 */
	public void setCustomNoteWeb(String customNoteWeb) {
		this.customNoteWeb = customNoteWeb;
	}

	/**
	 * Gets isSentToAAU
	 * 
	 * @return isSentToAAU the isSentToAAU
	 */
	public Boolean getIsSentToAAU() {
		return isSentToAAU;
	}

	/**
	 * Sets isSentToAAU
	 * 
	 * @param isSentToAAU
	 *            the isSentToAAU to set
	 */
	public void setIsSentToAAU(Boolean isSentToAAU) {
		this.isSentToAAU = isSentToAAU;
	}

	/**
	 * Gets validateByAAU
	 * 
	 * @return validateByAAU the validateByAAU
	 */
	public Boolean getValidateByAAU() {
		return validateByAAU;
	}

	/**
	 * Sets validateByAAU
	 * 
	 * @param validateByAAU
	 *            the validateByAAU to set
	 */
	public void setValidateByAAU(Boolean validateByAAU) {
		this.validateByAAU = validateByAAU;
	}

	/**
	 * Gets aauNote
	 * 
	 * @return aauNote the aauNote
	 */
	public String getAauNote() {
		return aauNote;
	}

	/**
	 * Sets aauNote
	 * 
	 * @param aauNote
	 *            the aauNote to set
	 */
	public void setAauNote(String aauNote) {
		this.aauNote = aauNote;
	}

	/**
	 * Gets showStcValue
	 * 
	 * @return showStcValue the showStcValue
	 */
	public Boolean getShowStcValue() {
		try {
			if (getDurCompilation() != null && getDurCompilation().getDurState() != null) {
				return DurStateTypes.AGUEvaluation.getValue().equals(getDurCompilation().getDurState().getId());
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * Gets showAguValue
	 * 
	 * @return showAguValue the showAguValue
	 */
	public Boolean getShowAguValue() {
		try {
			if (getDurCompilation() != null && getDurCompilation().getDurState() != null) {
				return DurStateTypes.ACUEvaluation.getValue().equals(getDurCompilation().getDurState().getId());
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * Gets reportDate
	 * 
	 * @return reportDate the reportDate
	 */
	public Date getReportDate() {
		return reportDate;
	}

	/**
	 * Sets reportDate
	 * 
	 * @param reportDate
	 *            the reportDate to set
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	/**
	 * Gets reportNumber
	 * 
	 * @return reportNumber the reportNumber
	 */
	@Exports({
			@Export(propertyName = "exportReportNumber", seqXLS = 21, type = FieldTypes.NUMBER, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportReportNumber", seqXLS = 75, type = FieldTypes.NUMBER, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Integer getReportNumber() {
		return reportNumber;
	}

	/**
	 * Sets reportNumber
	 * 
	 * @param reportNumber
	 *            the reportNumber to set
	 */
	public void setReportNumber(Integer reportNumber) {
		this.reportNumber = reportNumber;
	}

	/**
	 * Gets stcCertificationNote
	 * 
	 * @return stcCertificationNote the stcCertificationNote
	 */
	@Export(propertyName = "exportStcCertificationNote", seqXLS = 44, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getStcCertificationNote() {
		return stcCertificationNote;
	}

	/**
	 * Sets stcCertificationNote
	 * 
	 * @param stcCertificationNote
	 *            the stcCertificationNote to set
	 */
	public void setStcCertificationNote(String stcCertificationNote) {
		this.stcCertificationNote = stcCertificationNote;
	}

	/**
	 * Gets aguCertificationNote
	 * 
	 * @return aguCertificationNote the aguCertificationNote
	 */
	@Export(propertyName = "exportAguCertificationNote", seqXLS = 53, type = FieldTypes.STRING, place = ExportPlaces.DurCompilationsCostDefinitions)
	public String getAguCertificationNote() {
		return aguCertificationNote;
	}

	/**
	 * Sets aguCertificationNote
	 * 
	 * @param aguCertificationNote
	 *            the aguCertificationNote to set
	 */
	public void setAguCertificationNote(String aguCertificationNote) {
		this.aguCertificationNote = aguCertificationNote;
	}

	/**
	 * Gets cfCertificationNote
	 * 
	 * @return cfCertificationNote the cfCertificationNote
	 */
	public String getCfCertificationNote() {
		return cfCertificationNote;
	}

	/**
	 * Sets cfCertificationNote
	 * 
	 * @param cfCertificationNote
	 *            the cfCertificationNote to set
	 */
	public void setCfCertificationNote(String cfCertificationNote) {
		this.cfCertificationNote = cfCertificationNote;
	}

	/**
	 * Gets showAcuValue
	 * 
	 * @return showAcuValue the showAcuValue
	 */
	public Boolean getShowAcuValue() {
		try {
			if (getDurCompilation() != null && getDurCompilation().getDurState() != null) {
				return DurStateTypes.Reimbursement.getValue().equals(getDurCompilation().getDurState().getId());
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * Gets giuridicalEngages
	 * 
	 * @return giuridicalEngages the giuridicalEngages
	 */
	public GiuridicalEngages getGiuridicalEngages() {
		return giuridicalEngages;
	}

	/**
	 * Sets giuridicalEngages
	 * 
	 * @param giuridicalEngages
	 *            the giuridicalEngages to set
	 */
	public void setGiuridicalEngages(GiuridicalEngages giuridicalEngages) {
		this.giuridicalEngages = giuridicalEngages;
	}

	/**
	 * Gets importAda
	 * 
	 * @return importAda the importAda
	 */
	public Double getImportAda() {
		return importAda;
	}

	/**
	 * Sets importAda
	 * 
	 * @param importAda
	 *            the importAda to set
	 */
	public void setImportAda(Double importAda) {
		this.importAda = importAda;
	}

	/**
	 * Gets paymentReciveDate
	 * 
	 * @return paymentReciveDate the paymentReciveDate
	 */
	public Date getPaymentReciveDate() {
		return paymentReciveDate;
	}

	/**
	 * Sets paymentReciveDate
	 * 
	 * @param paymentReciveDate
	 *            the paymentReciveDate to set
	 */
	public void setPaymentReciveDate(Date paymentReciveDate) {
		this.paymentReciveDate = paymentReciveDate;
	}

	/**
	 * Gets paymentAttachmentDocument
	 * 
	 * @return paymentAttachmentDocument the paymentAttachmentDocument
	 */
	public Documents getPaymentAttachmentDocument() {
		return paymentAttachmentDocument;
	}

	/**
	 * Sets paymentAttachmentDocument
	 * 
	 * @param paymentAttachmentDocument
	 *            the paymentAttachmentDocument to set
	 */
	public void setPaymentAttachmentDocument(Documents paymentAttachmentDocument) {
		this.paymentAttachmentDocument = paymentAttachmentDocument;
	}

	/**
	 * Gets additionalFinansing
	 * 
	 * @return additionalFinansing the additionalFinansing
	 */
	@Exports({
			@Export(propertyName = "exportAdditionalFinansing", seqXLS = 22, type = FieldTypes.BOOLEAN, place = ExportPlaces.CostDefinitions),
			@Export(propertyName = "exportAdditionalFinansing", seqXLS = 76, type = FieldTypes.BOOLEAN, place = ExportPlaces.DurCompilationsCostDefinitions) })
	public Boolean getAdditionalFinansing() {
		return additionalFinansing;
	}

	/**
	 * Sets additionalFinansing
	 * 
	 * @param additionalFinansing
	 *            the additionalFinansing to set
	 */
	public void setAdditionalFinansing(Boolean additionalFinansing) {
		this.additionalFinansing = additionalFinansing;
	}

	@Export(propertyName = "exportStateAid", seqXLS = 11, type = FieldTypes.BOOLEAN, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Boolean getStateAid() {
		return stateAid;
	}

	public void setStateAid(Boolean stateAid) {
		this.stateAid = stateAid;
	}

	public Documents getTempDocumentCF() {
		return tempDocumentCF;
	}

	public void setTempDocumentCF(Documents tempDocumentCF) {
		this.tempDocumentCF = tempDocumentCF;
	}

	public CostAsse3 getCostItemPhaseAsse3() {
		return costItemPhaseAsse3;
	}

	public void setCostItemPhaseAsse3(CostAsse3 costItemPhaseAsse3) {
		this.costItemPhaseAsse3 = costItemPhaseAsse3;
	}

	public Long getSuspendReasonId() {
		return suspendReasonId;
	}

	public void setSuspendReasonId(Long suspendReasonId) {
		this.suspendReasonId = suspendReasonId;
	}

	public String getSuspendActNumber() {
		return suspendActNumber;
	}

	public void setSuspendActNumber(String suspendActNumber) {
		this.suspendActNumber = suspendActNumber;
	}

	public Date getSuspendActDate() {
		return suspendActDate;
	}

	public void setSuspendActDate(Date suspendActDate) {
		this.suspendActDate = suspendActDate;
	}

	public Documents getSuspendDocument() {
		return suspendDocument;
	}

	public void setSuspendDocument(Documents suspendDocument) {
		this.suspendDocument = suspendDocument;
	}

	public String getCancelSuspendActNumber() {
		return cancelSuspendActNumber;
	}

	public void setCancelSuspendActNumber(String cancelSuspendActNumber) {
		this.cancelSuspendActNumber = cancelSuspendActNumber;
	}

	public Date getCancelSuspendActDate() {
		return cancelSuspendActDate;
	}

	public void setCancelSuspendActDate(Date cancelSuspendActDate) {
		this.cancelSuspendActDate = cancelSuspendActDate;
	}

	public String getCancelSuspendReason() {
		return cancelSuspendReason;
	}

	public void setCancelSuspendReason(String cancelSuspendReason) {
		this.cancelSuspendReason = cancelSuspendReason;
	}

	public String getCancelSuspendNote() {
		return cancelSuspendNote;
	}

	public void setCancelSuspendNote(String cancelSuspendNote) {
		this.cancelSuspendNote = cancelSuspendNote;
	}

	public Documents getCancelSuspendDocument() {
		return cancelSuspendDocument;
	}

	public void setCancelSuspendDocument(Documents cancelSuspendDocument) {
		this.cancelSuspendDocument = cancelSuspendDocument;
	}

	public Boolean getConsiderInDUR() {
		return considerInDUR;
	}

	public void setConsiderInDUR(Boolean considerInDUR) {
		this.considerInDUR = considerInDUR;
	}

	public Boolean getSuspendSelect() {
		return suspendSelect;
	}

	public void setSuspendSelect(Boolean suspendSelect) {
		this.suspendSelect = suspendSelect;
	}

	public String getRetreatActNumber() {
		return retreatActNumber;
	}

	public void setRetreatActNumber(String retreatActNumber) {
		this.retreatActNumber = retreatActNumber;
	}

	public Date getRetreatActDate() {
		return retreatActDate;
	}

	public void setRetreatActDate(Date retreatActDate) {
		this.retreatActDate = retreatActDate;
	}

	public Long getSelctedRetreatReason() {
		return selctedRetreatReason;
	}

	public void setSelctedRetreatReason(Long selctedRetreatReason) {
		this.selctedRetreatReason = selctedRetreatReason;
	}

	public Long getSelctedRetreatController() {
		return selctedRetreatController;
	}

	public void setSelctedRetreatController(Long selctedRetreatController) {
		this.selctedRetreatController = selctedRetreatController;
	}

	public Long getSelctedSuspendController() {
		return selctedSuspendController;
	}

	public void setSelctedSuspendController(Long selctedSuspendController) {
		this.selctedSuspendController = selctedSuspendController;
	}

	public String getRetreatNote() {
		return retreatNote;
	}

	public void setRetreatNote(String retreatNote) {
		this.retreatNote = retreatNote;
	}

	public Documents getRetreatDocument() {
		return retreatDocument;
	}

	public void setRetreatDocument(Documents retreatDocument) {
		this.retreatDocument = retreatDocument;
	}

	public Double getSuspendPercent() {
		return suspendPercent;
	}

	public void setSuspendPercent(Double suspendPercent) {
		this.suspendPercent = suspendPercent;
	}

	public Double getSuspendRetreat() {
		return suspendRetreat;
	}

	public void setSuspendRetreat(Double suspendRetreat) {
		this.suspendRetreat = suspendRetreat;
	}

	public Boolean getExpenditureForLandPurchase() {
		return expenditureForLandPurchase;
	}

	public void setExpenditureForLandPurchase(Boolean expenditureForLandPurchase) {
		this.expenditureForLandPurchase = expenditureForLandPurchase;
	}

	@Export(propertyName = "exportAmountToCoverAdvanceStateAid", seqXLS = 12, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAmountToCoverAdvanceStateAid() {
		return amountToCoverAdvanceStateAid;
	}

	public void setAmountToCoverAdvanceStateAid(Double amountToCoverAdvanceStateAid) {
		this.amountToCoverAdvanceStateAid = amountToCoverAdvanceStateAid;
	}

	/**
	 * @return the durRecoveryDate
	 */
	public Date getDurRecoveryDate() {
		return durRecoveryDate;
	}

	/**
	 * @param durRecoveryDate
	 *            the durRecoveryDate to set
	 */
	public void setDurRecoveryDate(Date durRecoveryDate) {
		this.durRecoveryDate = durRecoveryDate;
	}

	/**
	 * Gets paymentTypeCode.
	 * 
	 * @return paymentTypeCode the paymentTypeCode
	 */
	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}

	/**
	 * Sets paymentTypeCode.
	 * 
	 * @param paymentTypeCode
	 *            the paymentTypeCode to set
	 */
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}

	/**
	 * Gets paymentMotivationCode.
	 * 
	 * @return paymentMotivationCode the paymentMotivationCode
	 */
	public String getPaymentMotivationCode() {
		return paymentMotivationCode;
	}

	/**
	 * Sets paymentMotivation.
	 * 
	 * @param paymentMotivation
	 *            the paymentTypeFn06 to set
	 */
	public void setPaymentMotivationCode(String paymentMotivation) {
		this.paymentMotivationCode = paymentMotivation;
	}

	/**
	 * @return the durRecoveryActNumber
	 */
	public String getDurRecoveryActNumber() {
		return durRecoveryActNumber;
	}

	/**
	 * @param durRecoveryActNumber
	 *            the durRecoveryActNumber to set
	 */
	public void setDurRecoveryActNumber(String durRecoveryActNumber) {
		this.durRecoveryActNumber = durRecoveryActNumber;
	}

	/**
	 * @return the durRecoveryActDate
	 */
	public Date getDurRecoveryActDate() {
		return durRecoveryActDate;
	}

	/**
	 * @param durRecoveryActDate
	 *            the durRecoveryActDate to set
	 */
	public void setDurRecoveryActDate(Date durRecoveryActDate) {
		this.durRecoveryActDate = durRecoveryActDate;
	}

	/**
	 * @return the durRecoverySuspendRetreat
	 */
	public Double getDurRecoverySuspendRetreat() {
		return durRecoverySuspendRetreat;
	}

	/**
	 * @param durRecoverySuspendRetreat
	 *            the durRecoverySuspendRetreat to set
	 */
	public void setDurRecoverySuspendRetreat(Double durRecoverySuspendRetreat) {
		this.durRecoverySuspendRetreat = durRecoverySuspendRetreat;
	}

	/**
	 * @return the selctedDurRecoveryReason
	 */
	public Long getSelctedDurRecoveryReason() {
		return selctedDurRecoveryReason;
	}

	/**
	 * @param selctedDurRecoveryReason
	 *            the selctedDurRecoveryReason to set
	 */
	public void setSelctedDurRecoveryReason(Long selctedDurRecoveryReason) {
		this.selctedDurRecoveryReason = selctedDurRecoveryReason;
	}

	/**
	 * @return the selctedDurRecoveryActType
	 */
	public Long getSelctedDurRecoveryActType() {
		return selctedDurRecoveryActType;
	}

	/**
	 * @param selctedDurRecoveryActType
	 *            the selctedDurRecoveryActType to set
	 */
	public void setSelctedDurRecoveryActType(Long selctedDurRecoveryActType) {
		this.selctedDurRecoveryActType = selctedDurRecoveryActType;
	}

	public Long getSelectedNotRegularDurRecoveryActType() {
		return selectedNotRegularDurRecoveryActType;
	}

	public void setSelectedNotRegularDurRecoveryActType(Long selectedNotRegularDurRecoveryActType) {
		this.selectedNotRegularDurRecoveryActType = selectedNotRegularDurRecoveryActType;
	}

	/**
	 * @return the durRecoveryNote
	 */
	public String getDurRecoveryNote() {
		return durRecoveryNote;
	}

	/**
	 * @param durRecoveryNote
	 *            the durRecoveryNote to set
	 */
	public void setDurRecoveryNote(String durRecoveryNote) {
		this.durRecoveryNote = durRecoveryNote;
	}

	/**
	 * @return the cancelDurRecoveryDocument
	 */
	public Documents getCancelDurRecoveryDocument() {
		return cancelDurRecoveryDocument;
	}

	/**
	 * @param cancelDurRecoveryDocument
	 *            the cancelDurRecoveryDocument to set
	 */
	public void setCancelDurRecoveryDocument(Documents cancelDurRecoveryDocument) {
		this.cancelDurRecoveryDocument = cancelDurRecoveryDocument;
	}

	// /**
	// * @return the durRecoveryImport
	// */
	// public Double getDurRecoveryImport() {
	// return durRecoveryImport;
	// }
	//
	// /**
	// * @param durRecoveryImport the durRecoveryImport to set
	// */
	// public void setDurRecoveryImport(Double durRecoveryImport) {
	// this.durRecoveryImport = durRecoveryImport;
	// }

	/**
	 * @return the durRecoveryReimbursementDateFromBeneficiary
	 */
	public Date getDurRecoveryReimbursementDateFromBeneficiary() {
		return durRecoveryReimbursementDateFromBeneficiary;
	}

	/**
	 * @param durRecoveryReimbursementDateFromBeneficiary
	 *            the durRecoveryReimbursementDateFromBeneficiary to set
	 */
	public void setDurRecoveryReimbursementDateFromBeneficiary(Date durRecoveryReimbursementDateFromBeneficiary) {
		this.durRecoveryReimbursementDateFromBeneficiary = durRecoveryReimbursementDateFromBeneficiary;
	}

	public Date getDurRecoveryReimbursementDateFromCE() {
		return durRecoveryReimbursementDateFromCE;
	}

	public void setDurRecoveryReimbursementDateFromCE(Date durRecoveryReimbursementDateFromCE) {
		this.durRecoveryReimbursementDateFromCE = durRecoveryReimbursementDateFromCE;
	}

	public Date getDurRecoveryReimbursementDateFromCMS() {
		return durRecoveryReimbursementDateFromCMS;
	}

	public void setDurRecoveryReimbursementDateFromCMS(Date durRecoveryReimbursementDateFromCMS) {
		this.durRecoveryReimbursementDateFromCMS = durRecoveryReimbursementDateFromCMS;
	}

	/**
	 * @return the durRecoverySustainImport
	 */
	public Double getDurRecoverySustainImport() {
		return durRecoverySustainImport;
	}

	/**
	 * @param durRecoverySustainImport
	 *            the durRecoverySustainImport to set
	 */
	public void setDurRecoverySustainImport(Double durRecoverySustainImport) {
		this.durRecoverySustainImport = durRecoverySustainImport;
	}

	@Export(propertyName = "exportAdditionalFinansingAmount", seqXLS = 10, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAdditionalFinansingAmount() {
		return additionalFinansingAmount;
	}

	public void setAdditionalFinansingAmount(Double additionalFinansingAmount) {
		this.additionalFinansingAmount = additionalFinansingAmount;
	}

	@Export(propertyName = "exportExpenditureOutsideEligibleAreasAmount", seqXLS = 13, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getExpenditureOutsideEligibleAreasAmount() {
		return expenditureOutsideEligibleAreasAmount;
	}

	public void setExpenditureOutsideEligibleAreasAmount(Double expenditureOutsideEligibleAreasAmount) {
		this.expenditureOutsideEligibleAreasAmount = expenditureOutsideEligibleAreasAmount;
	}

	@Export(propertyName = "exportInkindContributionsAmount", seqXLS = 14, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getInkindContributionsAmount() {
		return inkindContributionsAmount;
	}

	public void setInkindContributionsAmount(Double inkindContributionsAmount) {
		this.inkindContributionsAmount = inkindContributionsAmount;
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

	public Date getDurNotRegularActDate() {
		return durNotRegularActDate;
	}

	public void setDurNotRegularActDate(Date durNotRegularActDate) {
		this.durNotRegularActDate = durNotRegularActDate;
	}

	public Double getDurAmountOfPublicSupport() {
		return durAmountOfPublicSupport;
	}

	public void setDurAmountOfPublicSupport(Double durAmountOfPublicSupport) {
		this.durAmountOfPublicSupport = durAmountOfPublicSupport;
	}

	public Double getDurTotalEligibleCost() {
		return durTotalEligibleCost;
	}

	public void setDurTotalEligibleCost(Double durTotalEligibleCost) {
		this.durTotalEligibleCost = durTotalEligibleCost;
	}

	public Double getDurAmountPublicSupportReimbursed() {
		return durAmountPublicSupportReimbursed;
	}

	public void setDurAmountPublicSupportReimbursed(Double durAmountPublicSupportReimbursed) {
		this.durAmountPublicSupportReimbursed = durAmountPublicSupportReimbursed;
	}

	public Double getDurTotEligibleExpensesPublicSupportReimbursed() {
		return durTotEligibleExpensesPublicSupportReimbursed;
	}

	public void setDurTotEligibleExpensesPublicSupportReimbursed(Double durTotEligibleExpensesPublicSupportReimbursed) {
		this.durTotEligibleExpensesPublicSupportReimbursed = durTotEligibleExpensesPublicSupportReimbursed;
	}

	public Double getTotEligibleExpensesPublicSupportNonRecoverable() {
		return totEligibleExpensesPublicSupportNonRecoverable;
	}

	public void setTotEligibleExpensesPublicSupportNonRecoverable(
			Double totEligibleExpensesPublicSupportNonRecoverable) {
		this.totEligibleExpensesPublicSupportNonRecoverable = totEligibleExpensesPublicSupportNonRecoverable;
	}

	public Double getTotAmountEliglExpRetired() {
		return totAmountEliglExpRetired;
	}

	public void setTotAmountEliglExpRetired(Double totAmountEliglExpRetired) {
		this.totAmountEliglExpRetired = totAmountEliglExpRetired;
	}

	public Double getTotAmountPublExpRetired() {
		return totAmountPublExpRetired;
	}

	public void setTotAmountPublExpRetired(Double totAmountPublExpRetired) {
		this.totAmountPublExpRetired = totAmountPublExpRetired;
	}

	public Double getTotAmountEligExpSuspended() {
		return totAmountEligExpSuspended;
	}

	public void setTotAmountEligExpSuspended(Double totAmountEligExpSuspended) {
		this.totAmountEligExpSuspended = totAmountEligExpSuspended;
	}

	public Double getTotAmountPublExpSuspended() {
		return totAmountPublExpSuspended;
	}

	public void setTotAmountPublExpSuspended(Double totAmountPublExpSuspended) {
		this.totAmountPublExpSuspended = totAmountPublExpSuspended;
	}

	public Double getTotAmountEligExpToBeWithdrawn() {
		return totAmountEligExpToBeWithdrawn;
	}

	public void setTotAmountEligExpToBeWithdrawn(Double totAmountEligExpToBeWithdrawn) {
		this.totAmountEligExpToBeWithdrawn = totAmountEligExpToBeWithdrawn;
	}

	public Double getTotAmountPublExpToBeWithdrawn() {
		return totAmountPublExpToBeWithdrawn;
	}

	public void setTotAmountPublExpToBeWithdrawn(Double totAmountPublExpToBeWithdrawn) {
		this.totAmountPublExpToBeWithdrawn = totAmountPublExpToBeWithdrawn;
	}

	/**
	 * @return the acuTotalCertification
	 */
	@Export(propertyName = "exportAcuTotalCertification", seqXLS = 61, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuTotalCertification() {
		return acuTotalCertification;
	}

	/**
	 * @param acuTotalCertification
	 *            the acuTotalCertification to set
	 */
	public void setAcuTotalCertification(Double acuTotalCertification) {
		this.acuTotalCertification = acuTotalCertification;
	}

	public String getDurPartnerName() throws PersistenceBeanException {
		long durInfoId = BeansFactory.DurInfos().LoadByDurCompilation(this.getDurCompilation().getId()).getId();
		List<DurInfoToResponsiblePeople> listDurInfoToResponsiblePeople = BeansFactory.DurInfoToResponsiblePeople()
				.getByDurInfo(durInfoId);
		String cfPartner = "";

		for (int i = 0; i < listDurInfoToResponsiblePeople.size(); i++) {
			CFPartnerAnagraphs cfp = listDurInfoToResponsiblePeople.get(i).getPerson();
			cfPartner = cfPartner + cfp.getName();
			if (i < listDurInfoToResponsiblePeople.size() - 1) {
				cfPartner = cfPartner + ", ";
			}
		}
		return cfPartner;
	}

	public String getDurRecoveryReasonValue() throws PersistenceBeanException {
		int reasonId = this.selctedDurRecoveryReason.intValue() - 1;
		String reason = RecoverReasonType.values()[reasonId].toString();
		return reason;
	}

	@Export(propertyName = "exportCilCheckPublicAmount", seqXLS = 22, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckPublicAmount() {
		if (cilCheckPublicAmount == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getPublicAmountReport();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckPublicAmount;
	}

	public void setCilCheckPublicAmount(Double cilCheckPublicAmount) {
		this.cilCheckPublicAmount = cilCheckPublicAmount;
	}

	@Export(propertyName = "exportCilCheckPrivateAmount", seqXLS = 23, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckPrivateAmount() {

		if (cilCheckPrivateAmount == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getPrivateAmountReport();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckPrivateAmount;
	}

	public void setCilCheckPrivateAmount(Double cilCheckPrivateAmount) {
		this.cilCheckPrivateAmount = cilCheckPrivateAmount;
	}

	@Export(propertyName = "exportCilCheckAdditionalFinansingAmount", seqXLS = 24, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckAdditionalFinansingAmount() {
		if (cilCheckAdditionalFinansingAmount == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getAdditionalFinansingAmount();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckAdditionalFinansingAmount;
	}

	public void setCilCheckAdditionalFinansingAmount(Double cilCheckAdditionalFinansingAmount) {
		this.cilCheckAdditionalFinansingAmount = cilCheckAdditionalFinansingAmount;
	}

	@Export(propertyName = "exportCilCheckStateAidAmount", seqXLS = 25, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckStateAidAmount() {
		if (cilCheckStateAidAmount == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getAmountToCoverAdvanceStateAid();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckStateAidAmount;
	}

	public void setCilCheckStateAidAmount(Double cilCheckStateAidAmount) {
		this.cilCheckStateAidAmount = cilCheckStateAidAmount;
	}

	@Export(propertyName = "exportCilCheckOutsideEligibleAreas", seqXLS = 26, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckOutsideEligibleAreas() {
		if (cilCheckOutsideEligibleAreas == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getExpenditureOutsideEligibleAreasAmount();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckOutsideEligibleAreas;
	}

	public void setCilCheckOutsideEligibleAreas(Double cilCheckOutsideEligibleAreas) {
		this.cilCheckOutsideEligibleAreas = cilCheckOutsideEligibleAreas;
	}

	@Export(propertyName = "exportCilCheckInkindContributions", seqXLS = 27, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCilCheckInkindContributions() {
		if (cilCheckInkindContributions == null) {
			try {
				if (SessionManager.getInstance().getSessionBean().isCIL()) {
					return this.getInkindContributionsAmount();
				}
			} catch (Exception e) {
			}
		}
		return cilCheckInkindContributions;
	}

	public void setCilCheckInkindContributions(Double cilCheckInkindContributions) {
		this.cilCheckInkindContributions = cilCheckInkindContributions;
	}

	@Export(propertyName = "exportCfCheckPublicAmount", seqXLS = 30, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckPublicAmount() {
		return cfCheckPublicAmount;
	}

	public void setCfCheckPublicAmount(Double cfCheckPublicAmount) {
		this.cfCheckPublicAmount = cfCheckPublicAmount;
	}

	@Export(propertyName = "exportCfCheckPrivateAmount", seqXLS = 31, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckPrivateAmount() {
		return cfCheckPrivateAmount;
	}

	public void setCfCheckPrivateAmount(Double cfCheckPrivateAmount) {
		this.cfCheckPrivateAmount = cfCheckPrivateAmount;
	}

	@Export(propertyName = "exportCfCheckAdditionalFinansingAmount", seqXLS = 32, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckAdditionalFinansingAmount() {
		return cfCheckAdditionalFinansingAmount;
	}

	public void setCfCheckAdditionalFinansingAmount(Double cfCheckAdditionalFinansingAmount) {
		this.cfCheckAdditionalFinansingAmount = cfCheckAdditionalFinansingAmount;
	}

	@Export(propertyName = "exportCfCheckStateAidAmount", seqXLS = 33, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckStateAidAmount() {
		return cfCheckStateAidAmount;
	}

	public void setCfCheckStateAidAmount(Double cfCheckStateAidAmount) {
		this.cfCheckStateAidAmount = cfCheckStateAidAmount;
	}

	@Export(propertyName = "exportCfCheckOutsideEligibleAreas", seqXLS = 34, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckOutsideEligibleAreas() {
		return cfCheckOutsideEligibleAreas;
	}

	public void setCfCheckOutsideEligibleAreas(Double cfCheckOutsideEligibleAreas) {
		this.cfCheckOutsideEligibleAreas = cfCheckOutsideEligibleAreas;
	}

	@Export(propertyName = "exportCfCheckInkindContributions", seqXLS = 35, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getCfCheckInkindContributions() {
		return cfCheckInkindContributions;
	}

	public void setCfCheckInkindContributions(Double cfCheckInkindContributions) {
		this.cfCheckInkindContributions = cfCheckInkindContributions;
	}

	@Export(propertyName = "exportStcCheckPublicAmount", seqXLS = 38, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckPublicAmount() {
		return stcCheckPublicAmount;
	}

	public void setStcCheckPublicAmount(Double stcCheckPublicAmount) {
		this.stcCheckPublicAmount = stcCheckPublicAmount;
	}

	@Export(propertyName = "exportStcCheckPrivateAmount", seqXLS = 39, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckPrivateAmount() {
		return stcCheckPrivateAmount;
	}

	public void setStcCheckPrivateAmount(Double stcCheckPrivateAmount) {
		this.stcCheckPrivateAmount = stcCheckPrivateAmount;
	}

	@Export(propertyName = "exportStcCheckAdditionalFinansingAmount", seqXLS = 40, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckAdditionalFinansingAmount() {
		return stcCheckAdditionalFinansingAmount;
	}

	public void setStcCheckAdditionalFinansingAmount(Double stcCheckAdditionalFinansingAmount) {
		this.stcCheckAdditionalFinansingAmount = stcCheckAdditionalFinansingAmount;
	}

	@Export(propertyName = "exportStcCheckStateAidAmount", seqXLS = 41, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckStateAidAmount() {
		return stcCheckStateAidAmount;
	}

	public void setStcCheckStateAidAmount(Double stcCheckStateAidAmount) {
		this.stcCheckStateAidAmount = stcCheckStateAidAmount;
	}

	@Export(propertyName = "exportStcCheckOutsideEligibleAreas", seqXLS = 42, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckOutsideEligibleAreas() {
		return stcCheckOutsideEligibleAreas;
	}

	public void setStcCheckOutsideEligibleAreas(Double stcCheckOutsideEligibleAreas) {
		this.stcCheckOutsideEligibleAreas = stcCheckOutsideEligibleAreas;
	}

	@Export(propertyName = "exportStcCheckInkindContributions", seqXLS = 43, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getStcCheckInkindContributions() {
		return stcCheckInkindContributions;
	}

	public void setStcCheckInkindContributions(Double stcCheckInkindContributions) {
		this.stcCheckInkindContributions = stcCheckInkindContributions;
	}

	@Export(propertyName = "exportAguCheckPublicAmount", seqXLS = 47, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckPublicAmount() {
		return aguCheckPublicAmount;
	}

	public void setAguCheckPublicAmount(Double aguCheckPublicAmount) {
		this.aguCheckPublicAmount = aguCheckPublicAmount;
	}

	@Export(propertyName = "exportAguCheckPrivateAmount", seqXLS = 48, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckPrivateAmount() {
		return aguCheckPrivateAmount;
	}

	public void setAguCheckPrivateAmount(Double aguCheckPrivateAmount) {
		this.aguCheckPrivateAmount = aguCheckPrivateAmount;
	}

	@Export(propertyName = "exportAguCheckAdditionalFinansingAmount", seqXLS = 49, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckAdditionalFinansingAmount() {
		return aguCheckAdditionalFinansingAmount;
	}

	public void setAguCheckAdditionalFinansingAmount(Double aguCheckAdditionalFinansingAmount) {
		this.aguCheckAdditionalFinansingAmount = aguCheckAdditionalFinansingAmount;
	}

	@Export(propertyName = "exportAguCheckStateAidAmount", seqXLS = 50, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckStateAidAmount() {
		return aguCheckStateAidAmount;
	}

	public void setAguCheckStateAidAmount(Double aguCheckStateAidAmount) {
		this.aguCheckStateAidAmount = aguCheckStateAidAmount;
	}

	@Export(propertyName = "exportAguCheckOutsideEligibleAreas", seqXLS = 51, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckOutsideEligibleAreas() {
		return aguCheckOutsideEligibleAreas;
	}

	public void setAguCheckOutsideEligibleAreas(Double aguCheckOutsideEligibleAreas) {
		this.aguCheckOutsideEligibleAreas = aguCheckOutsideEligibleAreas;
	}

	@Export(propertyName = "exportAguCheckInkindContributions", seqXLS = 52, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAguCheckInkindContributions() {
		return aguCheckInkindContributions;
	}

	public void setAguCheckInkindContributions(Double aguCheckInkindContributions) {
		this.aguCheckInkindContributions = aguCheckInkindContributions;
	}

	@Export(propertyName = "exportAcuCheckPublicAmount", seqXLS = 56, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckPublicAmount() {
		return acuCheckPublicAmount;
	}

	public void setAcuCheckPublicAmount(Double acuCheckPublicAmount) {
		this.acuCheckPublicAmount = acuCheckPublicAmount;
	}

	@Export(propertyName = "exportAcuCheckPrivateAmount", seqXLS = 57, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckPrivateAmount() {
		return acuCheckPrivateAmount;
	}

	public void setAcuCheckPrivateAmount(Double acuCheckPrivateAmount) {
		this.acuCheckPrivateAmount = acuCheckPrivateAmount;
	}

	@Export(propertyName = "exportAcuCheckAdditionalFinansingAmount", seqXLS = 62, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckAdditionalFinansingAmount() {
		return acuCheckAdditionalFinansingAmount;
	}

	public void setAcuCheckAdditionalFinansingAmount(Double acuCheckAdditionalFinansingAmount) {
		this.acuCheckAdditionalFinansingAmount = acuCheckAdditionalFinansingAmount;
	}

	@Export(propertyName = "exportAcuCheckStateAidAmount", seqXLS = 63, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckStateAidAmount() {
		return acuCheckStateAidAmount;
	}

	public void setAcuCheckStateAidAmount(Double acuCheckStateAidAmount) {
		this.acuCheckStateAidAmount = acuCheckStateAidAmount;
	}

	@Export(propertyName = "exportAcuCheckOutsideEligibleAreas", seqXLS = 64, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckOutsideEligibleAreas() {
		return acuCheckOutsideEligibleAreas;
	}

	public void setAcuCheckOutsideEligibleAreas(Double acuCheckOutsideEligibleAreas) {
		this.acuCheckOutsideEligibleAreas = acuCheckOutsideEligibleAreas;
	}

	@Export(propertyName = "exportAcuCheckInkindContributions", seqXLS = 65, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilationsCostDefinitions)
	public Double getAcuCheckInkindContributions() {
		return acuCheckInkindContributions;
	}

	public void setAcuCheckInkindContributions(Double acuCheckInkindContributions) {
		this.acuCheckInkindContributions = acuCheckInkindContributions;
	}

	public AdditionalFesrInfo getAdditionalFesrInfo() {
		return additionalFesrInfo;
	}

	public void setAdditionalFesrInfo(AdditionalFesrInfo additionalFesrInfo) {
		this.additionalFesrInfo = additionalFesrInfo;
	}

	public String getDocumentationContentiousRecovery() {
		return documentationContentiousRecovery;
	}

	public void setDocumentationContentiousRecovery(String documentationContentiousRecovery) {
		this.documentationContentiousRecovery = documentationContentiousRecovery;
	}

	public String getDocumentationUnsuccessfulRecovery() {
		return documentationUnsuccessfulRecovery;
	}

	public void setDocumentationUnsuccessfulRecovery(String documentationUnsuccessfulRecovery) {
		this.documentationUnsuccessfulRecovery = documentationUnsuccessfulRecovery;
	}

	public Date getDateOfPaymentProgrammeOrBody() {
		return dateOfPaymentProgrammeOrBody;
	}

	public void setDateOfPaymentProgrammeOrBody(Date dateOfPaymentProgrammeOrBody) {
		this.dateOfPaymentProgrammeOrBody = dateOfPaymentProgrammeOrBody;
	}

	public String getSupportingDocumentsWaivingDecision() {
		return supportingDocumentsWaivingDecision;
	}

	public void setSupportingDocumentsWaivingDecision(String supportingDocumentsWaivingDecision) {
		this.supportingDocumentsWaivingDecision = supportingDocumentsWaivingDecision;
	}

	public Double getAmountOfRecoveryWaived() {
		return amountOfRecoveryWaived;
	}

	public void setAmountOfRecoveryWaived(Double amountOfRecoveryWaived) {
		this.amountOfRecoveryWaived = amountOfRecoveryWaived;
	}

}
