/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;

/**
 * 
 * @author Anton Kazakov InfoStroy Co., 2014.
 * 
 */

@Entity(name ="phases")
public class Phase extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
		implements Cloneable, Serializable
{
	@ManyToOne
	@JoinColumn(name = "partner_id")
	private PartnersBudgets partnersBudgets;
	
	@OneToOne
    @JoinColumn(name = "phase_id")
	private CostDefinitionPhases phase;

	@Column(name = "part_human_resources")
	private Double	partHumanRes;

	@Column(name = "part_provision_of_service")
	private Double	partProvisionOfService;

	@Column(name = "part_missions")
	private Double	partMissions;

	@Column(name = "part_durables")
	private Double	partDurables;

	@Column(name = "part_durable_goods")
	private Double	partDurableGoods;

	@Column(name = "part_advinfo_pubevents")
	private Double	partAdvInfoPublicEvents;

	@Column(name = "part_advinfo_products")
	private Double	partAdvInfoProducts;

	@Column(name = "part_overheads")
	private Double	partOverheads;
	
	@Column(name = "part_general_costs")
	private Double	partGeneralCosts;

	@Column(name = "part_other")
	private Double	partOther;
	
	@Column(name="part_container")
	private Double	partContainer;
	
	@Column(name="part_personal_expenses")
	private Double partPersonalExpenses;
	
	@Column(name="part_communication_and_information")
	private Double partCommunicationAndInformation;
	
	@Column(name="part_organization_of_commitees")
	private Double partOrganizationOfCommitees;
	
	@Column(name="part_other_fees")
	private Double partOtherFees;
	
	@Column(name="part_auto_coords_tunis")
	private Double partAutoCoordsTunis;
	
	@Column(name="part_contact_point")
	private Double partContactPoint;
	
	@Column(name="part_system_control_and_management")
	private Double partSystemControlAndManagement;
	
	@Column(name="part_audit_of_operations")
	private Double partAuditOfOperations;
	
	@Column(name="part_authorities_certification")
	private Double partAuthoritiesCertification;
	
	@Column(name="part_interm_evaluation")
	private Double partIntermEvaluation;
	
	@Transient
	private Double phaseTotal;

	/**
	 * Gets partnersBudgets
	 * @return partnersBudgets the partnersBudgets
	 */
	public PartnersBudgets getPartnersBudgets()
	{
		return partnersBudgets;
	}

	/**
	 * Sets partnersBudgets
	 * @param partnersBudgets the partnersBudgets to set
	 */
	public void setPartnersBudgets(PartnersBudgets partnersBudgets)
	{
		this.partnersBudgets = partnersBudgets;
	}

	/**
	 * Gets partHumanRes
	 * @return partHumanRes the partHumanRes
	 */
	public Double getPartHumanRes()
	{
		return NumberHelper.Round(partHumanRes);
	}

	/**
	 * Sets partHumanRes
	 * @param partHumanRes the partHumanRes to set
	 */
	public void setPartHumanRes(Double partHumanRes)
	{
		this.partHumanRes = partHumanRes;
	}

	/**
	 * Gets partProvisionOfService
	 * @return partProvisionOfService the partProvisionOfService
	 */
	public Double getPartProvisionOfService()
	{
		return NumberHelper.Round(partProvisionOfService);
	}

	/**
	 * Sets partProvisionOfService
	 * @param partProvisionOfService the partProvisionOfService to set
	 */
	public void setPartProvisionOfService(Double partProvisionOfService)
	{
		this.partProvisionOfService = partProvisionOfService;
	}

	/**
	 * Gets partMissions
	 * @return partMissions the partMissions
	 */
	public Double getPartMissions()
	{
		return NumberHelper.Round(partMissions);
	}

	/**
	 * Sets partMissions
	 * @param partMissions the partMissions to set
	 */
	public void setPartMissions(Double partMissions)
	{
		this.partMissions = partMissions;
	}

	/**
	 * Gets partDurables
	 * @return partDurables the partDurables
	 */
	public Double getPartDurables()
	{
		return NumberHelper.Round(partDurables);
	}

	/**
	 * Sets partDurables
	 * @param partDurables the partDurables to set
	 */
	public void setPartDurables(Double partDurables)
	{
		this.partDurables = partDurables;
	}

	/**
	 * Gets partDurableGoods
	 * @return partDurableGoods the partDurableGoods
	 */
	public Double getPartDurableGoods()
	{
		return NumberHelper.Round(partDurableGoods);
	}

	/**
	 * Sets partDurableGoods
	 * @param partDurableGoods the partDurableGoods to set
	 */
	public void setPartDurableGoods(Double partDurableGoods)
	{
		this.partDurableGoods = partDurableGoods;
	}

	/**
	 * Gets partAdvInfoPublicEvents
	 * @return partAdvInfoPublicEvents the partAdvInfoPublicEvents
	 */
	public Double getPartAdvInfoPublicEvents()
	{
		return NumberHelper.Round(partAdvInfoPublicEvents);
	}

	/**
	 * Sets partAdvInfoPublicEvents
	 * @param partAdvInfoPublicEvents the partAdvInfoPublicEvents to set
	 */
	public void setPartAdvInfoPublicEvents(Double partAdvInfoPublicEvents)
	{
		this.partAdvInfoPublicEvents = partAdvInfoPublicEvents;
	}

	/**
	 * Gets partAdvInfoProducts
	 * @return partAdvInfoProducts the partAdvInfoProducts
	 */
	public Double getPartAdvInfoProducts()
	{
		return NumberHelper.Round(partAdvInfoProducts);
	}

	/**
	 * Sets partAdvInfoProducts
	 * @param partAdvInfoProducts the partAdvInfoProducts to set
	 */
	public void setPartAdvInfoProducts(Double partAdvInfoProducts)
	{
		this.partAdvInfoProducts = partAdvInfoProducts;
	}

	/**
	 * Gets partOverheads
	 * @return partOverheads the partOverheads
	 */
	public Double getPartOverheads()
	{
		return NumberHelper.Round(partOverheads);
	}

	/**
	 * Sets partOverheads
	 * @param partOverheads the partOverheads to set
	 */
	public void setPartOverheads(Double partOverheads)
	{
		this.partOverheads = partOverheads;
	}

	
	/**
	 * Gets partGeneralCosts
	 * @return partGeneralCosts the partGeneralCosts
	 */
	public Double getPartGeneralCosts()
	{
		return partGeneralCosts;
	}

	/**
	 * Sets partGeneralCosts
	 * @param partGeneralCosts the partGeneralCosts to set
	 */
	public void setPartGeneralCosts(Double partGeneralCosts)
	{
		this.partGeneralCosts = partGeneralCosts;
	}
	
	/**
	 * Gets partOther
	 * @return partOther the partOther
	 */
	public Double getPartOther()
	{
		return NumberHelper.Round(partOther);
	}

	/**
	 * Sets partOther
	 * @param partOther the partOther to set
	 */
	public void setPartOther(Double partOther)
	{
		this.partOther = partOther;
	}

	/**
	 * Gets phase
	 * @return phase the phase
	 */
	public CostDefinitionPhases getPhase()
	{
		return phase;
	}

	/**
	 * Sets phase
	 * @param phase the phase to set
	 */
	public void setPhase(CostDefinitionPhases phase)
	{
		this.phase = phase;
	}

	/**
	 * Gets partPersonalExpenses
	 * @return partPersonalExpenses the partPersonalExpenses
	 */
	public Double getPartPersonalExpenses()
	{
		return NumberHelper.Round(partPersonalExpenses);
	}

	/**
	 * Sets partPersonalExpenses
	 * @param partPersonalExpenses the partPersonalExpenses to set
	 */
	public void setPartPersonalExpenses(Double partPersonalExpenses)
	{
		this.partPersonalExpenses = partPersonalExpenses;
	}

	/**
	 * Gets partCommunicationAndInformation
	 * @return partCommunicationAndInformation the partCommunicationAndInformation
	 */
	public Double getPartCommunicationAndInformation()
	{
		return NumberHelper.Round(partCommunicationAndInformation);
	}

	/**
	 * Sets partCommunicationAndInformation
	 * @param partCommunicationAndInformation the partCommunicationAndInformation to set
	 */
	public void setPartCommunicationAndInformation(
			Double partCommunicationAndInformation)
	{
		this.partCommunicationAndInformation = partCommunicationAndInformation;
	}

	/**
	 * Gets partOrganizationOfCommitees
	 * @return partOrganizationOfCommitees the partOrganizationOfCommitees
	 */
	public Double getPartOrganizationOfCommitees()
	{
		return NumberHelper.Round(partOrganizationOfCommitees);
	}

	/**
	 * Sets partOrganizationOfCommitees
	 * @param partOrganizationOfCommitees the partOrganizationOfCommitees to set
	 */
	public void setPartOrganizationOfCommitees(Double partOrganizationOfCommitees)
	{
		this.partOrganizationOfCommitees = partOrganizationOfCommitees;
	}

	/**
	 * Gets partOtherFees
	 * @return partOtherFees the partOtherFees
	 */
	public Double getPartOtherFees()
	{
		return NumberHelper.Round(partOtherFees);
	}

	/**
	 * Sets partOtherFees
	 * @param partOtherFees the partOtherFees to set
	 */
	public void setPartOtherFees(Double partOtherFees)
	{
		this.partOtherFees = partOtherFees;
	}

	/**
	 * Gets partAutoCoordsMalt
	 * @return partAutoCoordsMalt the partAutoCoordsMalt
	 */
	public Double getPartAutoCoordsTunis()
	{
		return NumberHelper.Round(partAutoCoordsTunis);
	}

	/**
	 * Sets partAutoCoordsMalt
	 * @param partAutoCoordsMalt the partAutoCoordsMalt to set
	 */
	public void setPartAutoCoordsTunis(Double partAutoCoordsTunis)
	{
		this.partAutoCoordsTunis = partAutoCoordsTunis;
	}

	/**
	 * Gets partContactPoint
	 * @return partContactPoint the partContactPoint
	 */
	public Double getPartContactPoint()
	{
		return NumberHelper.Round(partContactPoint);
	}

	/**
	 * Sets partContactPoint
	 * @param partContactPoint the partContactPoint to set
	 */
	public void setPartContactPoint(Double partContactPoint)
	{
		this.partContactPoint = partContactPoint;
	}

	/**
	 * Gets partSystemControlAndManagement
	 * @return partSystemControlAndManagement the partSystemControlAndManagement
	 */
	public Double getPartSystemControlAndManagement()
	{
		return NumberHelper.Round(partSystemControlAndManagement);
	}

	/**
	 * Sets partSystemControlAndManagement
	 * @param partSystemControlAndManagement the partSystemControlAndManagement to set
	 */
	public void setPartSystemControlAndManagement(
			Double partSystemControlAndManagement)
	{
		this.partSystemControlAndManagement = partSystemControlAndManagement;
	}

	/**
	 * Gets partAuditOfOperations
	 * @return partAuditOfOperations the partAuditOfOperations
	 */
	public Double getPartAuditOfOperations()
	{
		return NumberHelper.Round(partAuditOfOperations);
	}

	/**
	 * Sets partAuditOfOperations
	 * @param partAuditOfOperations the partAuditOfOperations to set
	 */
	public void setPartAuditOfOperations(Double partAuditOfOperations)
	{
		this.partAuditOfOperations = partAuditOfOperations;
	}

	/**
	 * Gets partAuthoritiesCertification
	 * @return partAuthoritiesCertification the partAuthoritiesCertification
	 */
	public Double getPartAuthoritiesCertification()
	{
		return NumberHelper.Round(partAuthoritiesCertification);
	}

	/**
	 * Sets partAuthoritiesCertification
	 * @param partAuthoritiesCertification the partAuthoritiesCertification to set
	 */
	public void setPartAuthoritiesCertification(Double partAuthoritiesCertification)
	{
		this.partAuthoritiesCertification = partAuthoritiesCertification;
	}

	/**
	 * Gets partIntermEvaluation
	 * @return partIntermEvaluation the partIntermEvaluation
	 */
	public Double getPartIntermEvaluation()
	{
		return NumberHelper.Round(partIntermEvaluation);
	}

	/**
	 * Sets partIntermEvaluation
	 * @param partIntermEvaluation the partIntermEvaluation to set
	 */
	public void setPartIntermEvaluation(Double partIntermEvaluation)
	{
		this.partIntermEvaluation = partIntermEvaluation;
	}

	/**
	 * Gets partContainer
	 * @return partContainer the partContainer
	 */
	public Double getPartContainer()
	{
		return NumberHelper.Round(partContainer);
	}

	/**
	 * Sets partContainer
	 * @param partContainer the partContainer to set
	 */
	public void setPartContainer(Double partContainer)
	{
		this.partContainer = partContainer;
	}
	
	
	public void fillTotalValue(){
		double summ =0d;
		summ+= partHumanRes==null ? 0 : partHumanRes.doubleValue();
		summ+= partProvisionOfService==null ? 0 : partProvisionOfService.doubleValue();
		summ+= partMissions==null ? 0 : partMissions.doubleValue();
		summ+= partDurables==null ? 0 : partDurables.doubleValue();
		summ+= partDurableGoods==null ? 0 : partDurableGoods.doubleValue();
		summ+= partAdvInfoPublicEvents==null ? 0 : partAdvInfoPublicEvents.doubleValue();
		summ+= partAdvInfoProducts==null ? 0 : partAdvInfoProducts.doubleValue();
		summ+= partOverheads==null ? 0 : partOverheads.doubleValue();
		summ+= partGeneralCosts==null ? 0 : partGeneralCosts.doubleValue();
		summ+= partOther==null ? 0 : partOther.doubleValue();
		this.setPhaseTotal(new Double(summ));
	}

	public Double getPhaseTotal()
	{
		if(phaseTotal==null){
			fillTotalValue();
		}
		return phaseTotal;
	}

	public void setPhaseTotal(Double phaseTotal)
	{
		this.phaseTotal = phaseTotal;
	}
	
	
	
}
