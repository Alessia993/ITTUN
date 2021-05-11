package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;

import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;

/**
 * User: posuhov Date: 22.03.13
 */
public class CostDefinitionsWrapper {
	private Date cilDate;
	private String customNote;
	private Double cilCheck;
	private Double cilCheckPublicAmount;
	private Double cilCheckPrivateAmount;
	private Double cilCheckAdditionalFinansingAmount;
	private Double cilCheckStateAidAmount;
	private Double cilCheckOutsideEligibleAreas;
	private Double cilCheckInkindContributions;
	private Double rectificationAmount;
	private Double valueSuspendSTC;
	private Double valueSuspendAGU;
	private Double valueSuspendACU;
	private Double amountToOut;
	private Double amountToSuspend;
	private Double acuCertification;
	private Double stcCertification;
	private Double aguCertification;

	public CostDefinitionsWrapper(CostDefinitions costDefinition) {
		this.cilDate = costDefinition.getCilDate();
		this.customNote = costDefinition.getCustomNote();
		this.cilCheck = costDefinition.getCilCheck();
		this.cilCheckPublicAmount = costDefinition.getCilCheckPublicAmount();
		this.cilCheckPrivateAmount = costDefinition.getCilCheckPrivateAmount();
		this.cilCheckAdditionalFinansingAmount = costDefinition.getCilCheckAdditionalFinansingAmount();
		this.cilCheckStateAidAmount = costDefinition.getCilCheckStateAidAmount();
		this.cilCheckOutsideEligibleAreas = costDefinition.getCilCheckOutsideEligibleAreas();
		this.cilCheckInkindContributions = costDefinition.getCilCheckInkindContributions();
		this.rectificationAmount = costDefinition.getRectificationAmount();
		this.valueSuspendSTC = costDefinition.getValueSuspendSTC();
		this.valueSuspendAGU = costDefinition.getValueSuspendAGU();
		this.valueSuspendACU = costDefinition.getValueSuspendACU();
		this.acuCertification = costDefinition.getAcuCertification();
		this.stcCertification = costDefinition.getStcCertification();
		this.aguCertification = costDefinition.getAguCertification();
		this.amountToOut = costDefinition.getAmountToOut();
		this.amountToSuspend = costDefinition.getAmountToSuspend();
	}

	public Date getCilDate() {
		return cilDate;
	}

	public void setCilDate(Date cilDate) {
		this.cilDate = cilDate;
	}

	public String getCustomNote() {
		return customNote;
	}

	public void setCustomNote(String customNote) {
		this.customNote = customNote;
	}

	public Double getCilCheck() {
		return cilCheck;
	}

	public void setCilCheck(Double cilCheck) {
		this.cilCheck = cilCheck;
	}

	public Double getRectificationAmount() {
		return rectificationAmount;
	}

	public void setRectificationAmount(Double rectificationAmount) {
		this.rectificationAmount = rectificationAmount;
	}

	public Double getValueSuspendSTC() {
		return valueSuspendSTC;
	}

	public void setValueSuspendSTC(Double valueSuspendSTC) {
		this.valueSuspendSTC = valueSuspendSTC;
	}

	public Double getValueSuspendAGU() {
		return valueSuspendAGU;
	}

	public void setValueSuspendAGU(Double valueSuspendAGU) {
		this.valueSuspendAGU = valueSuspendAGU;
	}

	public Double getValueSuspendACU() {
		return valueSuspendACU;
	}

	public void setValueSuspendACU(Double valueSuspendACU) {
		this.valueSuspendACU = valueSuspendACU;
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

	/**
	 * Gets acuCertification
	 * 
	 * @return acuCertification the acuCertification
	 */
	public Double getAcuCertification() {
		return acuCertification;
	}

	/**
	 * Sets acuCertification
	 * 
	 * @param acuCertification
	 *            the acuCertification to set
	 */
	public void setAcuCertification(Double acuCertification) {
		this.acuCertification = acuCertification;
	}

	/**
	 * Gets stcCertification
	 * 
	 * @return stcCertification the stcCertification
	 */
	public Double getStcCertification() {
		return stcCertification;
	}

	/**
	 * Sets stcCertification
	 * 
	 * @param stcCertification
	 *            the stcCertification to set
	 */
	public void setStcCertification(Double stcCertification) {
		this.stcCertification = stcCertification;
	}

	/**
	 * Gets aguCertification
	 * 
	 * @return aguCertification the aguCertification
	 */
	public Double getAguCertification() {
		return aguCertification;
	}

	/**
	 * Sets aguCertification
	 * 
	 * @param aguCertification
	 *            the aguCertification to set
	 */
	public void setAguCertification(Double aguCertification) {
		this.aguCertification = aguCertification;
	}

	public Double getCilCheckPublicAmount() {
		return cilCheckPublicAmount;
	}

	public void setCilCheckPublicAmount(Double cilCheckPublicAmount) {
		this.cilCheckPublicAmount = cilCheckPublicAmount;
	}

	public Double getCilCheckPrivateAmount() {
		return cilCheckPrivateAmount;
	}

	public void setCilCheckPrivateAmount(Double cilCheckPrivateAmount) {
		this.cilCheckPrivateAmount = cilCheckPrivateAmount;
	}

	public Double getCilCheckAdditionalFinansingAmount() {
		return cilCheckAdditionalFinansingAmount;
	}

	public void setCilCheckAdditionalFinansingAmount(Double cilCheckAdditionalFinansingAmount) {
		this.cilCheckAdditionalFinansingAmount = cilCheckAdditionalFinansingAmount;
	}

	public Double getCilCheckStateAidAmount() {
		return cilCheckStateAidAmount;
	}

	public void setCilCheckStateAidAmount(Double cilCheckStateAidAmount) {
		this.cilCheckStateAidAmount = cilCheckStateAidAmount;
	}

	public Double getCilCheckOutsideEligibleAreas() {
		return cilCheckOutsideEligibleAreas;
	}

	public void setCilCheckOutsideEligibleAreas(Double cilCheckOutsideEligibleAreas) {
		this.cilCheckOutsideEligibleAreas = cilCheckOutsideEligibleAreas;
	}

	public Double getCilCheckInkindContributions() {
		return cilCheckInkindContributions;
	}

	public void setCilCheckInkindContributions(Double cilCheckInkindContributions) {
		this.cilCheckInkindContributions = cilCheckInkindContributions;
	}

}
