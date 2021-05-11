package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;

@Entity
@Table(name = "cost_management")
public class CostManagement extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7046574051572784338L;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;

	@ManyToOne
	@JoinColumn(name = "cost_item_id")
	private CostItems costItem;

	@Column(name = "")
	private Double calculatedAmount;

	@Column(name = "")
	private Double amountBeAchieved;

	@Column(name = "absolute_difference")
	private Double absoluteDifference;

	@Column(name = "percentage_variation")
	private String percentageVariation;

	@Column(name = "conciliated")
	private Boolean conciliated;
	
	@ManyToOne
	@JoinColumn
	private Projects project;
	
	public CostManagement() {

	}
	
	public CostManagement(Users user, CostItems costItem, Double calculatedAmount, Double amountBeAchieved,
			Double absoluteDifference, String percentageVariation, Boolean conciliated, Projects project) {
		super();
		this.user = user;
		this.costItem = costItem;
		this.calculatedAmount = calculatedAmount;
		this.amountBeAchieved = amountBeAchieved;
		this.absoluteDifference = absoluteDifference;
		this.percentageVariation = percentageVariation;
		this.conciliated = conciliated;
		this.project = project;
	}



	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public CostItems getCostItem() {
		return costItem;
	}

	public void setCostItem(CostItems costItem) {
		this.costItem = costItem;
	}

	public Double getCalculatedAmount() {
		return calculatedAmount;
	}

	public void setCalculatedAmount(Double calculatedAmount) {
		this.calculatedAmount = calculatedAmount;
	}

	public Double getAmountBeAchieved() {
		return amountBeAchieved;
	}

	public void setAmountBeAchieved(Double amountBeAchieved) {
		this.amountBeAchieved = amountBeAchieved;
	}

	public Double getAbsoluteDifference() {
		return absoluteDifference;
	}

	public void setAbsoluteDifference(Double absoluteDifference) {
		this.absoluteDifference = absoluteDifference;
	}

	public String getPercentageVariation() {
		return percentageVariation;
	}

	public void setPercentageVariation(String percentageVariation) {
		this.percentageVariation = percentageVariation;
	}

	public Boolean getConciliated() {
		return conciliated;
	}

	public void setConciliated(Boolean conciliated) {
		this.conciliated = conciliated;
	}

	public Projects getProject() {
		return project;
	}

	public void setProject(Projects project) {
		this.project = project;
	}

}
