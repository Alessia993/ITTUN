package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

@Entity
@Table(name = "expenditure_declaration_accounting_period")
public class ExpenditureDeclarationAccountingPeriod extends PersistentEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5208463321272269874L;

	@Column(name="axis")
	private String axis;

	@Column(name="specific_objective")
	private String specificObjective;

	@Column(name="amount_of_past_ex_decl")
	private Double amountOfPastExDecl;

	@Column(name="amount_of_ex_decl_for_accounting_period")
	private Double amountOfExDeclForAccountingPeriod;

	@Column(name="amount_of_withdrawals_accounting_period")
	private Double amountOfWithdrawalsAccountingPeriod;

	@Column(name="suspension_amount")
	private Double suspensionAmount;

	@Column(name="cumulative_amount")
	private Double cumulativeAmount;
	
	@OneToOne
	@JoinColumn(name="expenditure_declaration_id")
	private ExpenditureDeclaration expenditureDeclaration;

	public ExpenditureDeclarationAccountingPeriod() {
		super();
		this.amountOfPastExDecl = 0d;
		this.amountOfExDeclForAccountingPeriod = 0d;
		this.amountOfWithdrawalsAccountingPeriod = 0d;
		this.suspensionAmount = 0d;
		this.cumulativeAmount = 0d;
	}
	
	public ExpenditureDeclarationAccountingPeriod(SpecificGoals sg){
		this.axis = elaborateAxisFromSpecifiGoals(sg);
		this.specificObjective = sg.getCode();
		this.amountOfPastExDecl = 0d;
		this.amountOfExDeclForAccountingPeriod = 0d;
		this.amountOfWithdrawalsAccountingPeriod = 0d;
		this.suspensionAmount = 0d;
		this.cumulativeAmount = 0d;
	}
	
	private String elaborateAxisFromSpecifiGoals(SpecificGoals sg){
		return sg.getCode().substring(0, sg.getCode().indexOf('.'));
	}

	@Export(propertyName = "asse", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.Annex1A)
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.Annex1A)
	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	@Export(propertyName = "expenditureDeclarationEditAmountOfPastExDecl", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.Annex1A)
	public Double getAmountOfPastExDecl() {
		return amountOfPastExDecl;
	}

	public void setAmountOfPastExDecl(Double amountOfPastExDecl) {
		this.amountOfPastExDecl = amountOfPastExDecl;
	}

	@Export(propertyName = "expenditureDeclarationEditAmountOfExDeclForAccountingPeriod2", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.Annex1A)
	public Double getAmountOfExDeclForAccountingPeriod() {
		return amountOfExDeclForAccountingPeriod;
	}

	public void setAmountOfExDeclForAccountingPeriod(Double amountOfExDeclForAccountingPeriod) {
		this.amountOfExDeclForAccountingPeriod = amountOfExDeclForAccountingPeriod;
	}

	@Export(propertyName = "expenditureDeclarationEditAmountOfWithdrawalsAccountingPeriod2", seqXLS = 5, type = FieldTypes.MONEY, place = ExportPlaces.Annex1A)
	public Double getAmountOfWithdrawalsAccountingPeriod() {
		return amountOfWithdrawalsAccountingPeriod;
	}

	public void setAmountOfWithdrawalsAccountingPeriod(Double amountOfWithdrawalsAccountingPeriod) {
		this.amountOfWithdrawalsAccountingPeriod = amountOfWithdrawalsAccountingPeriod;
	}

	@Export(propertyName = "expenditureDeclarationEditSuspensionAmount2", seqXLS = 6, type = FieldTypes.MONEY, place = ExportPlaces.Annex1A)
	public Double getSuspensionAmount() {
		return suspensionAmount;
	}

	public void setSuspensionAmount(Double suspensionAmount) {
		this.suspensionAmount = suspensionAmount;
	}

	@Export(propertyName = "expenditureDeclarationEditCumulativeAmount", seqXLS = 7, type = FieldTypes.MONEY, place = ExportPlaces.Annex1A)
	public Double getCumulativeAmount() {
		return cumulativeAmount;
		//return this.amountOfPastExDecl + this.amountOfExDeclForAccountingPeriod - this.amountOfWithdrawalsAccountingPeriod - this.suspensionAmount;
	}

	public void setCumulativeAmount(Double cumulativeAmount) {
		this.cumulativeAmount = cumulativeAmount;
	}

	public ExpenditureDeclaration getExpenditureDeclaration() {
		return expenditureDeclaration;
	}

	public void setExpenditureDeclaration(ExpenditureDeclaration expenditureDeclaration) {
		this.expenditureDeclaration = expenditureDeclaration;
	}

}
