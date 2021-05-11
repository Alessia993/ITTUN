/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "parnters_budgets")
public class PartnersBudgets extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
		implements Cloneable, Serializable
{
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= 7680730654314041037L;

	@Column
	private Boolean				approved;

	@Column(columnDefinition = "BIT DEFAULT FALSE")
	private Boolean				denied;

	@Column
	private Double				counter;

	@Column
	private int					year;

	/**
	 * Stores project value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Projects			project;

	/**
	 * Stores cfPartnerAnagraph value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "cf_partner_anagraph_id")
	private CFPartnerAnagraphs	cfPartnerAnagraph;

	/**
	 * Stores totalAmount value of entity.
	 */
	@Column(name = "total_amount")
	private Double				totalAmount;

	/**
	 * Stores quotaPrivate value of entity.
	 */
	@Column(name = "quota_private")
	private Double				quotaPrivate;

	/**
	 * Stores isOld value of entity.
	 */
	@Column(name = "is_old")
	private Boolean				isOld;

	@Column
	private Long				version;

	@Column(name = "budget_release")
	private Double				budgetRelease;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partnersBudgets")
	private List<Phase>			phaseBudgets;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partnersBudgets")
	private List<CostItemAsse3> itemsAsse3;
	
	@Transient
	private String				description;

	@Transient
	private boolean				totalRow;

	/**
	 * Sets totalAmount
	 * 
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	/**
	 * Gets totalAmount
	 * 
	 * @return totalAmount the totalAmount
	 */
	public Double getTotalAmount()
	{
		return NumberHelper.Round(totalAmount);
	}

	/**
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project)
	{
		this.project = project;
	}

	/**
	 * Gets project
	 * 
	 * @return project the project
	 */
	public Projects getProject()
	{
		return project;
	}

	/**
	 * Sets cfPartnerAnagraph
	 * 
	 * @param cfPartnerAnagraph
	 *            the cfPartnerAnagraph to set
	 */
	public void setCfPartnerAnagraph(CFPartnerAnagraphs cfPartnerAnagraph)
	{
		this.cfPartnerAnagraph = cfPartnerAnagraph;
	}

	/**
	 * Gets cfPartnerAnagraph
	 * 
	 * @return cfPartnerAnagraph the cfPartnerAnagraph
	 */
	public CFPartnerAnagraphs getCfPartnerAnagraph()
	{
		return cfPartnerAnagraph;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Gets description
	 * 
	 * @return description the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets totalRow
	 * 
	 * @param totalRow
	 *            the totalRow to set
	 */
	public void setTotalRow(boolean totalRow)
	{
		this.totalRow = totalRow;
	}

	/**
	 * Gets totalRow
	 * 
	 * @return totalRow the totalRow
	 */
	public boolean isTotalRow()
	{
		return totalRow;
	}

	public PartnersBudgets clone()
	{
		PartnersBudgets pb = new PartnersBudgets();
		pb.setPhaseBudgets(new LinkedList<Phase>());

		pb.setCfPartnerAnagraph(this.cfPartnerAnagraph);
		pb.setCreateDate(this.getCreateDate());
		pb.setDeleted(this.getDeleted());
		pb.setDescription(this.getDescription());
		pb.setId(this.getId());
		pb.setIsOld(this.isOld);

		for (Phase currentPhase : this.getPhaseBudgets())
		{
			Phase newPhase = new Phase();
			newPhase.setPhase(currentPhase.getPhase());
			newPhase.setPartnersBudgets(pb);
			newPhase.setPartAdvInfoProducts(currentPhase
					.getPartAdvInfoProducts());
			newPhase.setPartAdvInfoPublicEvents(currentPhase
					.getPartAdvInfoPublicEvents());
			newPhase.setPartDurableGoods(currentPhase.getPartDurableGoods());
			newPhase.setPartDurables(currentPhase.getPartDurables());
			newPhase.setPartHumanRes(currentPhase.getPartHumanRes());
			newPhase.setPartMissions(currentPhase.getPartMissions());
			newPhase.setPartOther(currentPhase.getPartOther());
			newPhase.setPartGeneralCosts(currentPhase.getPartGeneralCosts());
			newPhase.setPartOverheads(currentPhase.getPartOverheads());
			newPhase.setPartProvisionOfService(currentPhase
					.getPartProvisionOfService());
			
			newPhase.setPartContainer(currentPhase.getPartContainer());
			newPhase.setPartPersonalExpenses(currentPhase.getPartPersonalExpenses());
			newPhase.setPartCommunicationAndInformation(currentPhase.getPartCommunicationAndInformation());
			newPhase.setPartOrganizationOfCommitees(currentPhase.getPartOrganizationOfCommitees());
			newPhase.setPartOtherFees(currentPhase.getPartOtherFees());
			newPhase.setPartAutoCoordsTunis(currentPhase.getPartAutoCoordsTunis());
			newPhase.setPartContactPoint(currentPhase.getPartContactPoint());
			newPhase.setPartSystemControlAndManagement(currentPhase.getPartSystemControlAndManagement());
			newPhase.setPartAuditOfOperations(currentPhase.getPartAuditOfOperations());
			newPhase.setPartAuthoritiesCertification(currentPhase.getPartAuthoritiesCertification());
			newPhase.setPartIntermEvaluation(currentPhase.getPartIntermEvaluation());
			
			newPhase.setCreateDate(currentPhase.getCreateDate());
			newPhase.setId(currentPhase.getId());
			newPhase.setDeleted(currentPhase.getDeleted());
			pb.getPhaseBudgets().add(newPhase);
		}
		pb.setProject(this.getProject());
		pb.setQuotaPrivate(this.getQuotaPrivate());
		pb.setTotalAmount(this.getTotalAmount());
		pb.setTotalRow(this.isTotalRow());
		pb.setYear(this.getYear());
		pb.setBudgetRelease(this.getBudgetRelease());

		return pb;
	}

	/**
	 * Sets version
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Long version)
	{
		this.version = version;
	}

	/**
	 * Gets version
	 * 
	 * @return version the version
	 */
	public Long getVersion()
	{
		return version;
	}

	public void setApproved(Boolean approved)
	{
		this.approved = approved;
	}

	public Boolean getApproved()
	{
		return approved;
	}

	/**
	 * Sets counter
	 * 
	 * @param counter
	 *            the counter to set
	 */
	public void setCounter(Double counter)
	{
		this.counter = counter;
	}

	/**
	 * Gets counter
	 * 
	 * @return counter the counter
	 */
	public Double getCounter()
	{
		return counter;
	}

	/**
	 * Sets budgetRelease
	 * 
	 * @param budgetRelease
	 *            the budgetRelease to set
	 */
	public void setBudgetRelease(Double budgetRelease)
	{
		this.budgetRelease = budgetRelease;
	}

	/**
	 * Gets budgetRelease
	 * 
	 * @return budgetRelease the budgetRelease
	 */
	public Double getBudgetRelease()
	{
		return budgetRelease;
	}

	/**
	 * Sets denied
	 * 
	 * @param denied
	 *            the denied to set
	 */
	public void setDenied(Boolean denied)
	{
		this.denied = denied;
	}

	/**
	 * Gets denied
	 * 
	 * @return denied the denied
	 */
	public Boolean getDenied()
	{
		return denied;
	}

	/**
	 * Sets quotaPrivate
	 * 
	 * @param quotaPrivate
	 *            the quotaPrivate to set
	 */
	public void setQuotaPrivate(Double quotaPrivate)
	{
		this.quotaPrivate = quotaPrivate;
	}

	/**
	 * Gets quotaPrivate
	 * 
	 * @return quotaPrivate the quotaPrivate
	 */
	public Double getQuotaPrivate()
	{
		return quotaPrivate;
	}

	/**
	 * Sets isOld
	 * 
	 * @param isOld
	 *            the isOld to set
	 */
	public void setIsOld(Boolean isOld)
	{
		this.isOld = isOld;
	}

	/**
	 * Gets isOld
	 * 
	 * @return isOld the isOld
	 */
	public Boolean getIsOld()
	{
		return isOld;
	}

	public static PartnersBudgets create(Projects project,
			CFPartnerAnagraphs cfAnagraph) throws PersistenceBeanException
	{
		PartnersBudgets pb = new PartnersBudgets();
		pb.setPhaseBudgets(new LinkedList<Phase>());
		pb.setCfPartnerAnagraph(cfAnagraph);
		pb.setProject(project);
		pb.setYear(0);
		pb.setApproved(true);
		pb.setTotalRow(false);
		
		for(CostDefinitionPhases phase : BeansFactory.CostDefinitionPhase().Load())
		{
			Phase newPhase = new Phase();
			newPhase.setPartnersBudgets(pb);
			newPhase.setPartAdvInfoProducts(0d);
			newPhase.setPartAdvInfoPublicEvents(0d);
			newPhase.setPartDurableGoods(0d);
			newPhase.setPartDurables(0d);
			newPhase.setPartHumanRes(0d);
			newPhase.setPartMissions(0d);
			newPhase.setPartOther(0d);
			newPhase.setPartOverheads(0d);
			newPhase.setPartGeneralCosts(0d);
			newPhase.setPartProvisionOfService(0d);
			
			newPhase.setPartContainer(0d);
			newPhase.setPartPersonalExpenses(0d);
			newPhase.setPartCommunicationAndInformation(0d);
			newPhase.setPartOrganizationOfCommitees(0d);
			newPhase.setPartOtherFees(0d);
			newPhase.setPartAutoCoordsTunis(0d);
			newPhase.setPartContactPoint(0d);
			newPhase.setPartSystemControlAndManagement(0d);
			newPhase.setPartAuditOfOperations(0d);
			newPhase.setPartAuthoritiesCertification(0d);
			newPhase.setPartIntermEvaluation(0d);
			
			newPhase.setPhase(phase);
			pb.getPhaseBudgets().add(newPhase);
		}
		
		pb.setTotalAmount(0d);
		pb.setBudgetRelease(0d);
		pb.setCounter(0d);
		pb.setQuotaPrivate(0d);
		pb.setIsOld(false);
		pb.setDenied(false);
		pb.setVersion(0l);
		return pb;
	}

	/**
	 * Gets phaseBudgets
	 * 
	 * @return phaseBudgets the phaseBudgets
	 */
	public List<Phase> getPhaseBudgets()
	{
		return phaseBudgets;
	}

	/**
	 * Sets phaseBudgets
	 * 
	 * @param phaseBudgets
	 *            the phaseBudgets to set
	 */
	public void setPhaseBudgets(List<Phase> phaseBudgets)
	{
		this.phaseBudgets = phaseBudgets;
	}

	/**
	 * Gets year
	 * 
	 * @return year the year
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * Sets year
	 * 
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year)
	{
		this.year = year;
	}

	public List<CostItemAsse3> getItemsAsse3()
	{
		return itemsAsse3;
	}

	public void setItemsAsse3(List<CostItemAsse3> itemsAsse3)
	{
		this.itemsAsse3 = itemsAsse3;
	}
	
	
}
