/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DurStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "dur_compilations")
public class DurCompilations extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	public DurCompilations() {
		this.refusedByAcuManager = false;
	}

	private static final long serialVersionUID = 5854331364988034301L;

	@Column(name = "refused_by_acu_manager")
	private Boolean refusedByAcuManager;

	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "dur_state_id")
	private DurStates durState;

	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "not_regular_types_id")
	private NotRegularTypes type;

	@Column(name = "certification_date")
	private Date certifiedDate;

	@Column(name = "is_in_reimbursement")
	private Boolean reimbursement;

	@Column(name = "is_reimbursed")
	private Boolean reimbursed;

	@Column(name = "saved_by_agu")
	private Boolean savedByAgu;

	@Column(name = "saved_by_acu")
	private Boolean savedByAcu;

	@Column(name = "saved_by_stc")
	private Boolean savedByStc;

	@Column(name = "number_payment_request")
	private Integer numberPaymentRequest;

	@Column(name = "date_payment_request")
	private Date datePaymentRequest;

	@Column
	private Boolean locked;

	@OneToMany(mappedBy = "durCompilation")
	private List<CostDefinitions> costDefinitions;

	@Column(name = "zone_current_total")
	private Double zoneCurrentTotal;

	@Column(name = "outstanding_amount")
	private Double outstandingAmount;

	@Column(name = "outstanding_amount_undo")
	private Double outstandingAmountUndo;

	@Column(name = "amount_withdrawn")
	private Double amountWithdrawn;

	@ManyToOne
	@JoinColumn(name = "expenditure_declaration_id")
	private ExpenditureDeclaration expenditureDeclaration;

	@ManyToOne
	@JoinColumn(name = "payment_application_id")
	private PaymentApplication paymentApplication;

	public void setDurState(DurStates durState) {
		this.durState = durState;
	}

	public DurStates getDurState() {
		return durState;
	}

	public void setType(NotRegularTypes type) {
		this.type = type;
	}

	public NotRegularTypes getType() {
		return type;
	}

	public void setCertifiedDate(Date certifiedDate) {
		this.certifiedDate = certifiedDate;
	}

	public Date getCertifiedDate() {
		return certifiedDate;
	}

	public void setReimbursement(Boolean reimbursement) {
		this.reimbursement = reimbursement;
	}

	public Boolean getReimbursement() {
		return reimbursement;
	}

	public void setReimbursed(Boolean reimbursed) {
		this.reimbursed = reimbursed;
	}

	public Boolean getReimbursed() {
		return reimbursed;
	}

	public void setSavedByAgu(Boolean savedByAgu) {
		this.savedByAgu = savedByAgu;
	}

	public Boolean getSavedByAgu() {
		return savedByAgu == null ? false : savedByAgu;
	}

	public void setSavedByAcu(Boolean savedByAcu) {
		this.savedByAcu = savedByAcu;
	}

	public Boolean getSavedByAcu() {
		return savedByAcu == null ? false : savedByAcu;
	}

	public void setSavedByStc(Boolean savedByStc) {
		this.savedByStc = savedByStc;
	}

	public Boolean getSavedByStc() {
		return savedByStc == null ? false : savedByStc;
	}

	public void setRefusedByAcuManager(Boolean refusedByAcuManager) {
		this.refusedByAcuManager = refusedByAcuManager;
	}

	public Boolean getRefusedByAcuManager() {
		return refusedByAcuManager;
	}

	public void setNumberPaymentRequest(Integer numberPaymentRequest) {
		this.numberPaymentRequest = numberPaymentRequest;
	}

	public Integer getNumberPaymentRequest() {
		return numberPaymentRequest;
	}

	public void setDatePaymentRequest(Date datePaymentRequest) {
		this.datePaymentRequest = datePaymentRequest;
	}

	public Date getDatePaymentRequest() {
		return datePaymentRequest;
	}

	@Transient
	public Integer getDurNumberTransient() {
		try {
			return BeansFactory.DurInfos().LoadByDurCompilation(this.getId()).getDurInfoNumber();
		} catch (Exception e) {

		}
		return null;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public List<CostDefinitions> getCostDefinitions() {
		return costDefinitions;
	}

	public void setCostDefinitions(List<CostDefinitions> costDefinitions) {
		this.costDefinitions = costDefinitions;
	}

	/**
	 * Gets zoneCurrentTotal
	 * 
	 * @return zoneCurrentTotal the zoneCurrentTotal
	 */
	public Double getZoneCurrentTotal() {
		return zoneCurrentTotal;
	}

	/**
	 * Sets zoneCurrentTotal
	 * 
	 * @param zoneCurrentTotal
	 *            the zoneCurrentTotal to set
	 */
	public void setZoneCurrentTotal(Double zoneCurrentTotal) {
		this.zoneCurrentTotal = zoneCurrentTotal;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Double getOutstandingAmountUndo() {
		return outstandingAmountUndo;
	}

	public void setOutstandingAmountUndo(Double outstandingAmountUndo) {
		this.outstandingAmountUndo = outstandingAmountUndo;
	}

	public Double getAmountWithdrawn() {
		return amountWithdrawn;
	}

	public void setAmountWithdrawn(Double amountWithdrawn) {
		this.amountWithdrawn = amountWithdrawn;
	}

	public ExpenditureDeclaration getExpenditureDeclaration() {
		return expenditureDeclaration;
	}

	public void setExpenditureDeclaration(ExpenditureDeclaration expenditureDeclaration) {
		this.expenditureDeclaration = expenditureDeclaration;
	}

	public PaymentApplication getPaymentApplication() {
		return paymentApplication;
	}

	public void setPaymentApplication(PaymentApplication paymentApplication) {
		this.paymentApplication = paymentApplication;
	}

	// @Override
	// public String toString() {
	// return "DurCompilations [refusedByAcuManager=" + refusedByAcuManager + ",
	// durState=" + durState + ", type="
	// + type + ", certifiedDate=" + certifiedDate + ", reimbursement=" +
	// reimbursement + ", reimbursed="
	// + reimbursed + ", savedByAgu=" + savedByAgu + ", savedByAcu=" +
	// savedByAcu + ", savedByStc="
	// + savedByStc + ", numberPaymentRequest=" + numberPaymentRequest + ",
	// datePaymentRequest="
	// + datePaymentRequest + ", locked=" + locked + ", costDefinitions=" +
	// costDefinitions
	// + ", zoneCurrentTotal=" + zoneCurrentTotal + ", outstandingAmount=" +
	// outstandingAmount
	// + ", outstandingAmountUndo=" + outstandingAmountUndo + ",
	// amountWithdrawn=" + amountWithdrawn
	// + ", expenditureDeclaration=" + expenditureDeclaration + "]";
	// }
}