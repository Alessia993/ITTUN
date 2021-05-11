/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.common.utils.Utils;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * @author Vito Rifino - Ingloba360 srl, 2017.
 * 
 */
@Entity
@Table(name = "general_budgets")
public class GeneralBudgets extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
		implements Cloneable
{

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= -5167985720109086685L;

	@Transient
	private String				listCfName;
	
	@Transient
	private String				listCfNaming;

	@Transient
	private String				listCfCountry;
	
	@Transient
	private Long				listCfProgressiveId;

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
	 * Stores budgetTotal value of entity.
	 */
	@Column(name = "buget_total")
	private Double				budgetTotal;

	/**
	 * Stores fesr value of entity.
	 */
	@Column
	private Double				fesr;

	/**
	 * Stores cnPublic value of entity.
	 */
	@Column(name = "cn_public")
	private Double				cnPublic;
	
	/**
     * Stores cnPublicOther value of entity.
     */
    @Column(name = "cn_public_other")
    private Double            cnPublicOther;
    
    /**
     * Stores cnPrivateReal value of entity.
     */
    @Column(name = "cn_private_real")
    private Double            cnPrivateReal;

	/**
	 * Stores quotaPrivate value of entity.
	 */
	@Column(name = "quota_private")
	private Double				quotaPrivate;
	
	/**
     * Stores netRevenue value of entity.
     */
    @Column(name = "net_revenue")
    private Double            netRevenue;

	/**
	 * Stores isOld value of entity.
	 */
	@Column(name = "is_old")
	private Boolean				isOld;

	/**
	 * Stores approved value of entity.
	 */
	@Column
	private Boolean				approved;

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

	/**
	 * Sets budgetTotal
	 * 
	 * @param budgetTotal
	 *            the budgetTotal to set
	 */
	public void setBudgetTotal(Double budgetTotal)
	{
		this.budgetTotal = budgetTotal;
	}

	/**
	 * Gets budgetTotal
	 * 
	 * @return budgetTotal the budgetTotal
	 */
	public Double getBudgetTotal()
	{
		return budgetTotal;
	}

	/**
	 * Sets fesr
	 * 
	 * @param fesr
	 *            the fesr to set
	 */
	public void setFesr(Double fesr)
	{
		this.fesr = fesr;
	}

	/**
	 * Gets fesr
	 * 
	 * @return fesr the fesr
	 */
	public Double getFesr()
	{
		return fesr;
	}

	public void addFesr(Double fesr)
	{
		this.fesr += fesr;
	}

	/**
	 * Sets cnPublic
	 * 
	 * @param cnPublic
	 *            the cnPublic to set
	 */
	public void setCnPublic(Double cnPublic)
	{
		this.cnPublic = cnPublic;
	}

	public void addCnPublic(Double cnPublic)
	{
		this.cnPublic += cnPublic;
	}
	
	/**
     * Gets cnPublicOther
     * @return cnPublicOther the cnPublicOther
     */
    public Double getCnPublicOther()
    {
        return cnPublicOther;
    }
    
    /**
     * Sets cnPublicOther
     * @param cnPublicOther the cnPublicOther to set
     */
    public void setCnPublicOther(Double cnPublicOther)
    {
        this.cnPublicOther = cnPublicOther;
    }

	/**
	 * Gets cnPublic
	 * 
	 * @return cnPublic the cnPublic
	 */
	public Double getCnPublic()
	{
		return cnPublic;
	}
	
	/**
     * Sets cnPrivateReal
     * @param cnPrivateReal the cnPrivateReal to set
     */
    public void setCnPrivateReal(Double cnPrivateReal)
    {
        this.cnPrivateReal = cnPrivateReal;
    }
    
    /**
     * Gets cnPrivateReal
     * @return cnPrivateReal the cnPrivateReal
     */
    public Double getCnPrivateReal()
    {
        return cnPrivateReal;
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

	public void addQuotaPrivate(Double quotaPrivate)
	{
		this.quotaPrivate += quotaPrivate;
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
     * Sets netRevenue
     * @param netRevenue the netRevenue to set
     */
    public void setNetRevenue(Double netRevenue)
    {
        this.netRevenue = netRevenue;
    }
    
    /**
     * Gets netRevenue
     * @return netRevenue the netRevenue
     */
    public Double getNetRevenue()
    {
        return netRevenue;
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

	/**
	 * Sets approved
	 * 
	 * @param approved
	 *            the approved to set
	 */
	public void setApproved(Boolean approved)
	{
		this.approved = approved;
	}

	/**
	 * Gets approved
	 * 
	 * @return approved the approved
	 */
	public Boolean getApproved()
	{
		return approved;
	}

	public GeneralBudgets clone()
	{
		GeneralBudgets newEntity = new GeneralBudgets();

		newEntity.setApproved(this.getApproved());
		newEntity.setBudgetTotal(this.getBudgetTotal());
		newEntity.setCfPartnerAnagraph(this.getCfPartnerAnagraph());
		newEntity.setCnPublic(this.getCnPublic());
		newEntity.setCnPublicOther(this.getCnPublicOther());
		newEntity.setCreateDate(this.getCreateDate());
		newEntity.setDeleted(this.getDeleted());
		newEntity.setFesr(this.getFesr());
		newEntity.setIsOld(this.getIsOld());
		newEntity.setProject(this.getProject());
		newEntity.setQuotaPrivate(this.getQuotaPrivate());
		newEntity.setCnPrivateReal(this.getCnPrivateReal());
		newEntity.setNetRevenue(this.getNetRevenue());				

		return newEntity;
	}

	/**
	 * Sets listCfCountry
	 * 
	 * @param listCfCountry
	 *            the listCfCountry to set
	 */
	public void setListCfCountry(String listCfCountry)
	{
		this.listCfCountry = listCfCountry;
	}

	/**
	 * Gets listCfCountry
	 * 
	 * @return listCfCountry the listCfCountry
	 */
	public String getListCfCountry()
	{
		if (this.getCfPartnerAnagraph() != null)
		{
			this.listCfCountry = this.getCfPartnerAnagraph().getCountry()
					.toString();
		}
		else
		{
			this.listCfCountry = "";
		}
		return listCfCountry;
	}

	/**
	 * Sets listCfName
	 * 
	 * @param listCfName
	 *            the listCfName to set
	 */
	public void setListCfName(String listCfName)
	{
		this.listCfName = listCfName;
	}

	/**
	 * Gets listCfName
	 * 
	 * @return listCfName the listCfName
	 */
	public String getListCfName()
	{
		if (this.getCfPartnerAnagraph() != null)
		{
			listCfName = this.getCfPartnerAnagraph().getName();
		}
		else
		{
			listCfName = Utils.getString("total");
		}
		return listCfName;
	}
	
	/**
	 * Gets listCfName
	 * 
	 * @return listCfName the listCfName
	 */
	public String getListCfNaming()
	{
		if (this.getCfPartnerAnagraph() != null)
		{
			listCfNaming = this.getCfPartnerAnagraph().getNaming();
		}
		else
		{
			listCfNaming = Utils.getString("total");
		}
		return listCfNaming;
	}

	/**
	 * Sets listCfNaming
	 * 
	 * @param listCfNaming
	 *            the listCfNaming to set
	 */
	public void setListCfNaming(String listCfNaming)
	{
		this.listCfNaming = listCfNaming;
	}
	
	/**
	 * Sets listCfProgressiveId
	 * 
	 * @param listCfProgressiveId
	 *            the listCfProgressiveId to set
	 */
	public void setListCfProgressiveId(Long listCfProgressiveId)
	{
		this.listCfProgressiveId = listCfProgressiveId;
	}

	/**
	 * Gets listCfProgressiveId
	 * 
	 * @return listCfProgressiveId the listCfProgressiveId
	 */
	public Long getListCfProgressiveId()
	{
		if (this.getCfPartnerAnagraph() != null)
		{
			this.listCfProgressiveId = this.getCfPartnerAnagraph().getProgressiveId();
		}
		else
		{
			this.listCfProgressiveId = 0L;
		}
		return listCfProgressiveId;
	}

	public static GeneralBudgets create(Projects project,
			CFPartnerAnagraphs cfAnagraph)
	{
		GeneralBudgets newEntity = new GeneralBudgets();
		newEntity.setCreateDate(new Date());
		newEntity.setDeleted(false);
		newEntity.setCfPartnerAnagraph(cfAnagraph);
		newEntity.setProject(project);
		newEntity.setApproved(false);
		newEntity.setIsOld(false);
		newEntity.setCnPublic(0d);
		newEntity.setCnPublicOther(0d);
		newEntity.setBudgetTotal(0d);
		newEntity.setFesr(0d);
		newEntity.setQuotaPrivate(0d);
		newEntity.setCnPrivateReal(0d);
		newEntity.setNetRevenue(0d);		
		
		return newEntity;
	}
}
