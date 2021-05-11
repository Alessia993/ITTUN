package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name = "cost_definitions_specific_goals_wrapper")
public class CostDefinitionsSpecificGoalsWrapper extends PersistentEntity {

	/**
		 * 
		 */
	private static final long serialVersionUID = 3834378642425485187L;

	@Column(name = "axis")
	private int axis;

	@Column(name = "retreat_date")
	private Date retreatDate;

	@Column(name = "date_suspend_acu")
	private Date dateSuspendACU;

	@Column(name = "cil_check")
	private Double cilCheck;

	@Column(name = "cf_check")
	private Double cfCheck;

	@Column(name = "stc_certification")
	private Double stcCertification;

	@Column(name = "agu_certification")
	private Double aguCertification;

	@Column(name = "acu_certification")
	private Double acuCertification;

	@Column(name = "agu_sertified")
	private Boolean AGUSertified;

	@Column(name = "stc_sertified")
	private Boolean STCSertified;

	@Column(name = "acu_sertified")
	private Boolean ACUSertified;

	@Column(name = "tot_amount_eligl_exp_retired")
	private Double totAmountEliglExpRetired;

	@Column(name = "tot_amount_elig_exp_suspended")
	private Double totAmountEligExpSuspended;

	@Column(name = "specific_objective")
	private String specificObjective;

	@Column(name = "exp_dec_accounting_period_from")
	private Date expDecAccountingPeriodFrom;

	@Column(name = "exp_dec_accounting_period_to")
	private Date expDecAccountingPeriodTo;

	@Column(name = "exp_dec_submission_date")
	private Date expDecSubmissionDate;

	public CostDefinitionsSpecificGoalsWrapper() {
	}

	public CostDefinitionsSpecificGoalsWrapper(Boolean aCUSertified, Double acuCertification, Boolean aGUSertified,
			Double aguCertification, Boolean sTCSertified, Double stcCertification, Double cfCheck, Double cilCheck,
			Double totAmountEliglExpRetired, Double totAmountEligExpSuspended, Date dateSuspendACU, Date retreatDate// ,
																													// String
																													// specificObjective

	) {
		super();
		// this.idCostDefinition = idCostDefinition;
		this.retreatDate = retreatDate;
		// this.createDateCostDefinition = createDateCostDefinition;
		this.dateSuspendACU = dateSuspendACU;
		this.cilCheck = cilCheck;
		this.cfCheck = cfCheck;
		this.stcCertification = stcCertification;
		this.aguCertification = aguCertification;
		this.acuCertification = acuCertification;
		AGUSertified = aGUSertified;
		STCSertified = sTCSertified;
		ACUSertified = aCUSertified;
		this.totAmountEliglExpRetired = totAmountEliglExpRetired;
		this.totAmountEligExpSuspended = totAmountEligExpSuspended;
		// this.specificObjective = specificObjective;
	}
	//
	// public Long getIdCostDefinition() {
	// return idCostDefinition;
	// }
	//
	// public void setIdCostDefinition(Long idCostDefinition) {
	// this.idCostDefinition = idCostDefinition;
	// }

	// public Date getCreateDateCostDefinition() {
	// return createDateCostDefinition;
	// }
	//
	// public void setCreateDateCostDefinition(Date createDateCostDefinition) {
	// this.createDateCostDefinition = createDateCostDefinition;
	// }

	public Date getRetreatDate() {
		return retreatDate;
	}

	public void setRetreatDate(Date retreatDate) {
		this.retreatDate = retreatDate;
	}

	public Date getDateSuspendACU() {
		return dateSuspendACU;
	}

	public void setDateSuspendACU(Date dateSuspendACU) {
		this.dateSuspendACU = dateSuspendACU;
	}

	public Double getCilCheck() {
		return cilCheck;
	}

	public void setCilCheck(Double cilCheck) {
		this.cilCheck = cilCheck;
	}

	public Double getCfCheck() {
		return cfCheck;
	}

	public void setCfCheck(Double cfCheck) {
		this.cfCheck = cfCheck;
	}

	public Double getStcCertification() {
		return stcCertification;
	}

	public void setStcCertification(Double stcCertification) {
		this.stcCertification = stcCertification;
	}

	public Double getAguCertification() {
		return aguCertification;
	}

	public void setAguCertification(Double aguCertification) {
		this.aguCertification = aguCertification;
	}

	public Double getAcuCertification() {
		return acuCertification;
	}

	public void setAcuCertification(Double acuCertification) {
		this.acuCertification = acuCertification;
	}

	public Boolean getAGUSertified() {
		return AGUSertified;
	}

	public void setAGUSertified(Boolean aGUSertified) {
		AGUSertified = aGUSertified;
	}

	public Boolean getSTCSertified() {
		return STCSertified;
	}

	public void setSTCSertified(Boolean sTCSertified) {
		STCSertified = sTCSertified;
	}

	public Boolean getACUSertified() {
		return ACUSertified;
	}

	public void setACUSertified(Boolean aCUSertified) {
		ACUSertified = aCUSertified;
	}

	public Double getTotAmountEliglExpRetired() {
		return totAmountEliglExpRetired;
	}

	public void setTotAmountEliglExpRetired(Double totAmountEliglExpRetired) {
		this.totAmountEliglExpRetired = totAmountEliglExpRetired;
	}

	public Double getTotAmountEligExpSuspended() {
		return totAmountEligExpSuspended;
	}

	public void setTotAmountEligExpSuspended(Double totAmountEligExpSuspended) {
		this.totAmountEligExpSuspended = totAmountEligExpSuspended;
	}

	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	public Date getExpDecAccountingPeriodFrom() {
		return expDecAccountingPeriodFrom;
	}

	public void setExpDecAccountingPeriodFrom(Date expDecAccountingPeriodFrom) {
		this.expDecAccountingPeriodFrom = expDecAccountingPeriodFrom;
	}

	public Date getExpDecAccountingPeriodTo() {
		return expDecAccountingPeriodTo;
	}

	public void setExpDecAccountingPeriodTo(Date expDecAccountingPeriodTo) {
		this.expDecAccountingPeriodTo = expDecAccountingPeriodTo;
	}

	public Date getExpDecSubmissionDate() {
		return expDecSubmissionDate;
	}

	public void setExpDecSubmissionDate(Date expDecSubmissionDate) {
		this.expDecSubmissionDate = expDecSubmissionDate;
	}

	public int getAxis() {
		return axis;
	}

	public void setAxis(int axis) {
		this.axis = axis;
	}

}
