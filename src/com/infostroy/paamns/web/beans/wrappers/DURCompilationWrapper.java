package com.infostroy.paamns.web.beans.wrappers;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.annotations.Exports;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DurStates;

public class DURCompilationWrapper {

	private String style;

	public DURCompilationWrapper(int number, DurCompilations durC, DurStates state, DurInfos durInfo,
			DurSummaries durSummaries) throws HibernateException, PersistenceBeanException {
		this.setId(durC.getId());
		this.setNumber(number);
		this.setCompilationDate(durInfo.getDurCompilationDate());
		this.setProtocolNumber(durInfo.getProtocolNumber());
		this.setRowNumber(durInfo.getDurInfoNumber());
		this.setOutstandingAmount(durC.getOutstandingAmount());

		if (state.getValue().equals("STC evaluation") || state.getValue().equals("l'évaluation de STC")
				|| state.getValue().equals("In valutazione STC")) {
			this.setState(state.getValue() + " / UC");
		} else {
			this.setState(state.getValue());
		}

		this.setStateId(state.getId());
		this.setPaymentRequestNumber(durC.getNumberPaymentRequest());
		this.setPaymentRequestDate(durC.getDatePaymentRequest());
		this.setDurNumberTransient(durC.getDurNumberTransient());

		List<CostDefinitions> listCD = BeansFactory.CostDefinitions().GetByDurCompilation(durC.getId());

		if (listCD != null && !listCD.isEmpty()) {
			this.setProject(listCD.get(0).getProject());
		}

		if (listCD != null && !listCD.isEmpty()) {
			this.setSumAmoutCertification(recalculateAmounts(listCD));
			this.setSumPublicAmoutCertification(recalculateAmountsPublic(listCD));
		} else {
			this.setSumAmoutCertification(durSummaries.getTotalAmount());
			this.setSumPublicAmoutCertification(durSummaries.getTotalAmount());
		}

		if (state.getId().equals(DurStateTypes.SendToExpenditureDeclaration.getValue())) {
			this.setSelectable(true);
		} else {
			this.setSelectable(false);
			this.setSelected(false);
		}

		if (durC.getCertifiedDate() != null) {
			this.setCertificationDate(durC.getCertifiedDate());
		}

		if (durC.getType() != null) {
			this.setColor("Red");
			this.setStyle("background-color: #FFDDDD;");
		} else {
			this.setColor("Green");
		}

		Boolean canPR = true;

		this.setTotalAmountAnnuled(durSummaries.getTotalAmountAnnulled());
		this.setTotalRectified(durSummaries.getTotalRectified());

		for (CostDefinitions item : listCD) {
			if (item.getSuspendedByACU() != null && item.getSuspendedByACU() && !item.getSuspendConfirmedACU()) {
				canPR = false;
				break;
			}
		}
		if (getStateId().equals(DurStateTypes.Certifiable.getValue())) {
			canPR = true;
		}

		this.setCanCreatePaymentRequest(canPR);

		this.setTotalAmountEligibleExpenditure(this.calculateTotalAmountEligibleExpenditure(durC));
		this.setTotalAmountPublicExpenditure(this.calculateAmountPublicExpenditure(durC));
		this.setTotalAmountPrivateExpenditure(this.calculateTotalAmountPrivateExpenditure(durC));
		this.setTotalAFAmount(this.calculateAdditionalFinansing2(durC));
		this.recalculateTotalReimbursement(durC);
	}

	private void recalculateTotalReimbursement(DurCompilations durC) {
		Double sum;
		try {
			sum = (this.calculateFesrAmount2(durC) == null ? 0d
					: (this.calculateFesrAmount2(durC).doubleValue())
							+ (this.calculateItCnReimbursementAmount(durC) == null ? 0d
									: this.calculateItCnReimbursementAmount(durC).doubleValue()));
		} catch (PersistenceBeanException ex) {
			sum = 0d;
		}
		this.setTotalReimbursement(sum);
	}

	private Double calculateItCnReimbursementAmount(DurCompilations durC) throws PersistenceBeanException {
		List<CostDefinitions> cds = durC.getCostDefinitions();
		CFPartnerAnagraphs ca = null;
		double amount = 0d;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
				if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
					if (ca.getCountry().getCode().equals(CountryTypes.Italy.getCountry())) {
						amount += (this.getCostSubAdditionalFinansing(cost) * 0.15);
					}
				}
			}
		}
		return amount;
	}

	private Double calculateAdditionalFinansing2(DurCompilations durC) throws PersistenceBeanException {
		List<CostDefinitions> cds = durC.getCostDefinitions();
		double amount = 0d;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
					amount += cost.getAcuCheckAdditionalFinansingAmount();
				} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
					amount += cost.getAguCheckAdditionalFinansingAmount();
				} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
					amount += cost.getStcCheckAdditionalFinansingAmount();
				} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
					amount += cost.getCfCheckAdditionalFinansingAmount();
				} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
					amount += cost.getCilCheckAdditionalFinansingAmount();
				}
			}
		}
		return amount;
	}

	private Double calculateTotalAmountPrivateExpenditure(DurCompilations durC) throws PersistenceBeanException {
		List<CostDefinitions> cds = durC.getCostDefinitions();
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double amount = 0d;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
				List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets()
						.GetItemsForCFAnagraph(String.valueOf(cost.getProject().getId()), ca.getId());
				gb = gbs.get(gbs.size() - 1);
				if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
					if (gb != null
							&& (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
									* 100) == 50)) {
						amount += (this.getValidateAmount(cost)* 0.5);//(this.getCostSubAdditionalFinansing(cost) * 0.5);
					} else {
						amount += (this.getValidateAmount(cost)* 0.15);//(this.getCostSubAdditionalFinansing(cost) * 0.15);
					}

				}
			}
		}
		return amount;
	}

	private Double calculateAmountPublicExpenditure(DurCompilations durC) throws PersistenceBeanException {
		List<CostDefinitions> cds = durC.getCostDefinitions();
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double amount = 0d;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
				List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets()
						.GetItemsForCFAnagraph(String.valueOf(cost.getProject().getId()), ca.getId());
				gb = gbs.get(gbs.size() - 1);
				if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
					if (gb != null
							&& (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
									* 100) == 50)) {
						amount += (this.getCostSubAdditionalFinansing(cost) * 0.5);
					} else {
						amount += (this.getCostSubAdditionalFinansing(cost) * 0.85);
					}

				} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
					amount += this.getValidateAmount(cost);//this.getCostSubAdditionalFinansing(cost);
				}
			}
		}
		return amount;
	}

	private Double calculateTotalAmountEligibleExpenditure(DurCompilations durC) throws PersistenceBeanException {
		double amount = 0;
		List<CostDefinitions> cds = durC.getCostDefinitions();

		CFPartnerAnagraphs ca = null;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
				if (ca.getPublicSubject() != null) {
					amount += this.getValidateAmount(cost);
				}
			}
		}
		return amount;
	}

	private Double calculateFesrAmount2(DurCompilations durC) throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = durC.getCostDefinitions();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (cds != null) {
			for (CostDefinitions cost : cds) {
				amount = this.getCostSubAdditionalFinansing(cost);
				ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
				List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets()
						.GetItemsForCFAnagraph(String.valueOf(cost.getProject().getId()), ca.getId());
				gb = gbs.get(gbs.size() - 1);

				if (gb != null) {
				}
				if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
					if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
							* 100) == 50) {
						amountTotal += (amount * 0.5);
					} else {
						amountTotal += (amount * 0.85);
					}

				} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
					amountTotal += (amount * 0.85);
				}
			}

		}
		return amountTotal;
	}

	private Double getValidateAmount(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			amount += cost.getAcuCertification();
		} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			amount += cost.getAguCertification();
		} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			amount += cost.getStcCertification();
		} else if (cost.getCfCheck() != null) {
			amount += cost.getCfCheck();
		} else if (cost.getCilCheck() != null) {
			amount += cost.getCilCheck();
		}
		return amount;
	}

	private Double getCostSubAdditionalFinansing(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			amount += cost.getAcuCertification();
		} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			amount += cost.getAguCertification();
		} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			amount += cost.getStcCertification();
		} else if (cost.getCfCheck() != null) {
			amount += cost.getCfCheck();
		} else if (cost.getCilCheck() != null) {
			amount += cost.getCilCheck();
		}
		if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
			if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAcuCheckAdditionalFinansingAmount();
			} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAguCheckAdditionalFinansingAmount();
			} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getStcCheckAdditionalFinansingAmount();
			} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCfCheckAdditionalFinansingAmount();
			} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCilCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private double recalculateAmountsPublic(List<CostDefinitions> cdList)
			throws HibernateException, PersistenceBeanException {
		double amountTotal = 0d;

		for (CostDefinitions cost : cdList) {
			if (cost.getAdditionalFinansing() == null || Boolean.FALSE.equals(cost.getAdditionalFinansing())) {
				if (cost.getACUSertified() && cost.getAcuCheckPublicAmount() != null && cost.getAcuCertification() != null && cost.getAcuCertification() != 0) {
					amountTotal += cost.getAcuCheckPublicAmount(); //.getAcuCertification();
				} else if (cost.getAGUSertified() && cost.getAguCheckPublicAmount() != null && cost.getAguCertification() != null && cost.getAguCertification() != 0 && cost.getAcuCertification() != null && cost.getAcuCertification() != 0) {
					amountTotal += cost.getAguCheckPublicAmount(); //   getAguCertification();
				} else if (cost.getSTCSertified() && cost.getStcCheckPublicAmount() != null && cost.getStcCertification() != null && cost.getStcCertification() != 0 && cost.getAcuCertification() != null && cost.getAcuCertification() != 0) {
					amountTotal += cost.getStcCheckPublicAmount();    // .getStcCertification();
				} else if (cost.getCfCheckPublicAmount() != null && cost.getCfCheck() != null && cost.getCfCheck() != 0 && cost.getAcuCertification() != null && cost.getAcuCertification() != 0) {
					amountTotal += cost.getCfCheckPublicAmount();     //   .getCfCheck();
				} else if (cost.getCilCheckPublicAmount() != null && cost.getCilCheck() != null && cost.getCilCheck() != 0 && cost.getAcuCertification() != null && cost.getAcuCertification() != 0) {
					amountTotal += cost.getCilCheckPublicAmount();     //  .getCilCheck();
				}
			}
		}

		return amountTotal;
	}
	
	private double recalculateAmounts(List<CostDefinitions> cdList)
			throws HibernateException, PersistenceBeanException {
		double amountTotal = 0d;

		for (CostDefinitions cost : cdList) {
			if (cost.getAdditionalFinansing() == null || Boolean.FALSE.equals(cost.getAdditionalFinansing())) {
				if (cost.getACUSertified() && cost.getAcuCertification() != null) {
					amountTotal += cost.getAcuCertification();
				} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
					amountTotal += cost.getAguCertification();
				} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
					amountTotal += cost.getStcCertification();
				} else if (cost.getCfCheck() != null) {
					amountTotal += cost.getCfCheck();
				} else if (cost.getCilCheck() != null) {
					amountTotal += cost.getCilCheck();
				}
			}
		}

		return amountTotal;
	}	

	private Long id;

	private int number;

	private int rowNumber;

	private Date compilationDate;

	private String protocolNumber;

	private Double sumAmoutCertification;
	
	private Double sumPublicAmoutCertification;

	private String state;

	private Long stateId;

	private String color;

	private boolean sendAvailable;

	private boolean denyAvailable;

	private boolean editAvailable;

	private boolean showAvailable;

	private boolean deleteAvailable;

	private Date certificationDate;

	private String type;

	private boolean selected;

	private boolean selectable;

	private Projects project;

	private Integer paymentRequestNumber;

	private Date paymentRequestDate;

	private Boolean canCreatePaymentRequest;

	private Double totalAmountAnnuled;

	private Double totalRectified;

	private Double outstandingAmount;

	private Integer durNumberTransient;
	private Boolean locked;

	private Double totalAmountEligibleExpenditure;

	private Double totalAmountPublicExpenditure;

	private Double totalAmountPrivateExpenditure;

	private Double totalAFAmount;

	private Double totalReimbursement;

	public Integer getDurNumberTransient() {
		return durNumberTransient;
	}

	public void setDurNumberTransient(Integer durNumberTransient) {
		this.durNumberTransient = durNumberTransient;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets id
	 * 
	 * @return id the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets rowNumber
	 * 
	 * @param rowNumber
	 *            the rowNumber to set
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * Gets rowNumber
	 * 
	 * @return rowNumber the rowNumber
	 */
	@Exports({
			@Export(propertyName = "exportReimbursementNumber", seqXLS = 3, type = FieldTypes.NUMBER, place = ExportPlaces.DurCompilations),
			@Export(propertyName = "exportReimbursementNumber", seqXLS = 2, type = FieldTypes.NUMBER, place = ExportPlaces.CostCertification) })
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * Sets compilationDate
	 * 
	 * @param compilationDate
	 *            the compilationDate to set
	 */
	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	/**
	 * Gets compilationDate
	 * 
	 * @return compilationDate the compilationDate
	 */
	@Export(propertyName = "exportDurCompilationDate", seqXLS = 1, type = FieldTypes.DATE, place = ExportPlaces.DurCompilations)
	public Date getCompilationDate() {
		return compilationDate;
	}

	/**
	 * Sets protocolNumber
	 * 
	 * @param protocolNumber
	 *            the protocolNumber to set
	 */
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	/**
	 * Gets protocolNumber
	 * 
	 * @return protocolNumber the protocolNumber
	 */
	@Exports({
			@Export(propertyName = "exportProtocolNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.DurCompilations),
			@Export(propertyName = "exportProtocolNumber", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.CostCertification) })
	public String getProtocolNumber() {
		return protocolNumber;
	}

	/**
	 * Sets sumAmoutCertification
	 * 
	 * @param sumAmoutCertification
	 *            the sumAmoutCertification to set
	 */
	public void setSumAmoutCertification(Double sumAmoutCertification) {
		this.sumAmoutCertification = sumAmoutCertification;
	}

	/**
	 * Gets sumAmoutCertification
	 * 
	 * @return sumAmoutCertification the sumAmoutCertification
	 */
	@Exports({
			//@Export(propertyName = "exportSumAmount", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations),
			@Export(propertyName = "exportSumAmount", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.CostCertification) })
	public Double getSumAmoutCertification() {
		return sumAmoutCertification;
	}
	
	/**
	 * Sets sumPublicAmoutCertification
	 * 
	 * @param sumPublicAmoutCertification
	 *            the sumPublicAmoutCertification to set
	 */
	public void setSumPublicAmoutCertification(Double sumPublicAmoutCertification) {
		this.sumPublicAmoutCertification = sumPublicAmoutCertification;
	}

	/**
	 * Gets sumPublicAmoutCertification
	 * 
	 * @return sumPublicAmoutCertification sumPublicAmoutCertification sumAmoutCertification
	 */
	@Exports({
			//@Export(propertyName = "exportSumAmount", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations),
			@Export(propertyName = "exportSumPublicAmount", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.CostCertification) })
	public Double getSumPublicAmoutCertification() {
		return sumPublicAmoutCertification;
	}	
	

	@Export(propertyName = "exportCode", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.CostCertification)
	public String getProjectCode() {
		return project == null ? null : project.getCode();
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets state
	 * 
	 * @return state the state
	 */
	@Exports({
			@Export(propertyName = "exportStatus", seqXLS = 9, type = FieldTypes.STRING, place = ExportPlaces.DurCompilations),
			@Export(propertyName = "exportStatus", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.CostCertification) })
	public String getState() {
		return state;
	}

	/**
	 * Sets number
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Gets number
	 * 
	 * @return number the number
	 */
	@Export(propertyName = "exportNumber", seqXLS = 0, type = FieldTypes.NUMBER, place = ExportPlaces.DurCompilations)
	public int getNumber() {
		return number;
	}

	/**
	 * Sets color
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Gets color
	 * 
	 * @return color the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets sendAvailable
	 * 
	 * @param sendAvailable
	 *            the sendAvailable to set
	 */
	public void setSendAvailable(boolean sendAvailable) {
		this.sendAvailable = sendAvailable;

	}

	public void setSendAvailableAdd(boolean sendAvailable) {
		this.sendAvailable |= sendAvailable;
	}

	/**
	 * Gets sendAvailable
	 * 
	 * @return sendAvailable the sendAvailable
	 */
	public boolean isSendAvailable() {
		return sendAvailable;
	}

	/**
	 * Sets denyAvailable
	 * 
	 * @param denyAvailable
	 *            the denyAvailable to set
	 */
	public void setDenyAvailable(boolean denyAvailable) {
		this.denyAvailable = denyAvailable;
	}

	public void setDenyAvailableAdd(boolean denyAvailable) {
		this.denyAvailable |= denyAvailable;
	}

	/**
	 * Gets denyAvailable
	 * 
	 * @return denyAvailable the denyAvailable
	 */
	public boolean isDenyAvailable() {
		return denyAvailable;
	}

	/**
	 * Sets editAvailable
	 * 
	 * @param editAvailable
	 *            the editAvailable to set
	 */
	public void setEditAvailable(boolean editAvailable) {
		this.editAvailable = editAvailable;
	}

	public void setEditAvailableAdd(boolean editAvailable) {
		this.editAvailable |= editAvailable;
	}

	/**
	 * Gets editAvailable
	 * 
	 * @return editAvailable the editAvailable
	 */
	public boolean isEditAvailable() {
		return editAvailable;
	}

	/**
	 * Sets showAvailable
	 * 
	 * @param showAvailable
	 *            the showAvailable to set
	 */
	public void setShowAvailable(boolean showAvailable) {
		this.showAvailable = showAvailable;
	}

	public void setShowAvailableAdd(boolean showAvailable) {
		this.showAvailable |= showAvailable;
	}

	/**
	 * Gets showAvailable
	 * 
	 * @return showAvailable the showAvailable
	 */
	public boolean isShowAvailable() {
		return showAvailable;
	}

	/**
	 * Sets deleteAvailable
	 * 
	 * @param deleteAvailable
	 *            the deleteAvailable to set
	 */
	public void setDeleteAvailable(boolean deleteAvailable) {
		this.deleteAvailable = deleteAvailable;
	}

	public void setDeleteAvailableAdd(boolean deleteAvailable) {
		this.deleteAvailable |= deleteAvailable;
	}

	/**
	 * Gets deleteAvailable
	 * 
	 * @return deleteAvailable the deleteAvailable
	 */
	public boolean isDeleteAvailable() {
		return deleteAvailable;
	}

	/**
	 * Sets type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets type
	 * 
	 * @return type the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets certificationDate
	 * 
	 * @param certificationDate
	 *            the certificationDate to set
	 */
	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}

	/**
	 * Gets certificationDate
	 * 
	 * @return certificationDate the certificationDate
	 */
	@Export(propertyName = "exportCertifiedDateDur", seqXLS = 0, type = FieldTypes.DATE, place = ExportPlaces.CostCertification)
	public Date getCertificationDate() {
		return certificationDate;
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
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets selectable
	 * 
	 * @param selectable
	 *            the selectable to set
	 */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	/**
	 * Gets selectable
	 * 
	 * @return selectable the selectable
	 */
	public boolean isSelectable() {
		return selectable;
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
	 * Sets style
	 * 
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Gets style
	 * 
	 * @return style the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets paymentRequestNumber
	 * 
	 * @param paymentRequestNumber
	 *            the paymentRequestNumber to set
	 */
	public void setPaymentRequestNumber(Integer paymentRequestNumber) {
		this.paymentRequestNumber = paymentRequestNumber;
	}

	/**
	 * Gets paymentRequestNumber
	 * 
	 * @return paymentRequestNumber the paymentRequestNumber
	 */
	@Export(propertyName = "exportPaymentRequestNumber", seqXLS = 6, type = FieldTypes.NUMBER, place = ExportPlaces.CostCertification)
	public Integer getPaymentRequestNumber() {
		return paymentRequestNumber;
	}

	/**
	 * Sets paymentRequestDate
	 * 
	 * @param paymentRequestDate
	 *            the paymentRequestDate to set
	 */
	public void setPaymentRequestDate(Date paymentRequestDate) {
		this.paymentRequestDate = paymentRequestDate;
	}

	/**
	 * Gets paymentRequestDate
	 * 
	 * @return paymentRequestDate the paymentRequestDate
	 */
	@Export(propertyName = "exportPaymentRequestDate", seqXLS = 7, type = FieldTypes.DATE, place = ExportPlaces.CostCertification)
	public Date getPaymentRequestDate() {
		return paymentRequestDate;
	}

	/**
	 * Sets stateId
	 * 
	 * @param stateId
	 *            the stateId to set
	 */
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	/**
	 * Gets stateId
	 * 
	 * @return stateId the stateId
	 */
	public Long getStateId() {
		return stateId;
	}

	public Boolean getCanCreatePaymentRequest() {
		return canCreatePaymentRequest;
	}

	public void setCanCreatePaymentRequest(Boolean canCreatePaymentRequest) {
		this.canCreatePaymentRequest = canCreatePaymentRequest;
	}

	public Double getTotalAmountAnnuled() {
		return totalAmountAnnuled;
	}

	public void setTotalAmountAnnuled(Double totalAmountAnnuled) {
		this.totalAmountAnnuled = totalAmountAnnuled;
	}

	public Double getTotalRectified() {
		return totalRectified;
	}

	public void setTotalRectified(Double totalRectified) {
		this.totalRectified = totalRectified;
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

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	@Export(propertyName = "durCompilationListHeaderTotalAmountEligibleExpenditure", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations)
	public Double getTotalAmountEligibleExpenditure() {
		return totalAmountEligibleExpenditure;
	}

	public void setTotalAmountEligibleExpenditure(Double totalAmountEligibleExpenditure) {
		this.totalAmountEligibleExpenditure = totalAmountEligibleExpenditure;
	}
	
	@Export(propertyName = "durCompilationListHeaderTotalAmountPublicExpenditure", seqXLS = 5, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations)
	public Double getTotalAmountPublicExpenditure() {
		return totalAmountPublicExpenditure;
	}

	public void setTotalAmountPublicExpenditure(Double totalAmountPublicExpenditure) {
		this.totalAmountPublicExpenditure = totalAmountPublicExpenditure;
	}

	@Export(propertyName = "durCompilationListHeaderTotalAmountPrivateExpenditure", seqXLS = 6, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations)
	public Double getTotalAmountPrivateExpenditure() {
		return totalAmountPrivateExpenditure;
	}

	public void setTotalAmountPrivateExpenditure(Double totalAmountPrivateExpenditure) {
		this.totalAmountPrivateExpenditure = totalAmountPrivateExpenditure;
	}

	@Export(propertyName = "durCompilationListHeaderTotalAFAmount", seqXLS = 7, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations)
	public Double getTotalAFAmount() {
		return totalAFAmount;
	}

	public void setTotalAFAmount(Double totalAFAmount) {
		this.totalAFAmount = totalAFAmount;
	}

	@Export(propertyName = "durCompilationListHeaderFesrReimbursementAmount", seqXLS = 8, type = FieldTypes.MONEY, place = ExportPlaces.DurCompilations)
	public Double getTotalReimbursement() {
		return totalReimbursement;
	}

	public void setTotalReimbursement(Double totalReimbursement) {
		this.totalReimbursement = totalReimbursement;
	}

}
