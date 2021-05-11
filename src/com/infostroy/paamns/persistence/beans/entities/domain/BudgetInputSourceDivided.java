/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * @author Vito Rifino - Ingloba360 srl, 2017.
 */
@Entity
@Table(name = "budget_input_source")
public class BudgetInputSourceDivided extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
        implements Cloneable
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -6057825638925324171L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects          project;
    
    /**
     * Stores totalBudget value of entity.
     */
    @Column(name = "total_budget")
    private Double            totalBudget;
    
    /**
     * Stores fesr value of entity.
     */
    @Column(name = "fesr")
    private Double            fesr;
    
    /**
     * Stores fesrPct value of entity.
     */
    @Column(name = "fesr_pct")
    private Double            fesrPct;
    
    /**
     * Stores cnTotal value of entity.
     */
    @Column(name = "cn_total")
    private Double            cnTotal;
    
    /**
     * Stores cnPct value of entity.
     */
    @Column(name = "cn_pct")
    private Double            cnPct;
    
    /**
     * Stores cnPublic value of entity.
     */
    @Column(name = "cn_public")
    private Double            cnPublic;
    
    /**
     * Stores cnPublicOther value of entity.
     */
    @Column(name = "cn_public_other")
    private Double            cnPublicOther;
    
    /**
     * Stores cnPublicPct value of entity.
     */
    @Column(name = "cn_public_pct")
    private Double            cnPublicPct;
    
    /**
     * Stores cnPrivateReal value of entity.
     */
    @Column(name = "cn_private_real")
    private Double            cnPrivateReal;
    
    /**
     * Stores cnPrivate value of entity.
     */
    @Column(name = "cn_private")
    private Double            cnPrivate;
    
    /**
     * Stores cnPrivatePct value of entity.
     */
    @Column(name = "cn_private_pct")
    private Double            cnPrivatePct;
    
    /**
     * Stores netRevenue value of entity.
     */
    @Column(name = "net_revenue")
    private Double            netRevenue;
    
    /**
     * Sets project
     * @param project the project to set
     */
    public void setProject(Projects project)
    {
        this.project = project;
    }
    
    /**
     * Gets project
     * @return project the project
     */
    public Projects getProject()
    {
        return project;
    }
    
    /**
     * Sets totalBudget
     * @param totalBudget the totalBudget to set
     */
    public void setTotalBudget(Double totalBudget)
    {
        this.totalBudget = totalBudget;
    }
    
    /**
     * Gets totalBudget
     * @return totalBudget the totalBudget
     */
    public Double getTotalBudget()
    {
        return totalBudget;
    }
    
    /**
     * Sets fesr
     * @param fesr the fesr to set
     */
    public void setFesr(Double fesr)
    {
        this.fesr = fesr;
    }
    
    /**
     * Gets fesr
     * @return fesr the fesr
     */
    public Double getFesr()
    {
        return fesr;
    }
    
    /**
     * Sets fesrPct
     * @param fesrPct the fesrPct to set
     */
    public void setFesrPct(Double fesrPct)
    {
        this.fesrPct = fesrPct;
    }
    
    /**
     * Gets fesrPct
     * @return fesrPct the fesrPct
     */
    public Double getFesrPct()
    {
        return fesrPct;
    }
    
    /**
     * Sets cnTotal
     * @param cnTotal the cnTotal to set
     */
    public void setCnTotal(Double cnTotal)
    {
        this.cnTotal = cnTotal;
    }
    
    /**
     * Gets cnTotal
     * @return cnTotal the cnTotal
     */
    public Double getCnTotal()
    {
        return cnTotal;
    }
    
    /**
     * Sets cnPct
     * @param cnPct the cnPct to set
     */
    public void setCnPct(Double cnPct)
    {
        this.cnPct = cnPct;
    }
    
    /**
     * Gets cnPct
     * @return cnPct the cnPct
     */
    public Double getCnPct()
    {
        return cnPct;
    }
    
    /**
     * Sets cnPublic
     * @param cnPublic the cnPublic to set
     */
    public void setCnPublic(Double cnPublic)
    {
        this.cnPublic = cnPublic;
    }
    
    /**
     * Gets cnPublic
     * @return cnPublic the cnPublic
     */
    public Double getCnPublic()
    {
        return cnPublic;
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
     * Sets cnPublicPct
     * @param cnPublicPct the cnPublicPct to set
     */
    public void setCnPublicPct(Double cnPublicPct)
    {
        this.cnPublicPct = cnPublicPct;
    }
    
    /**
     * Gets cnPublicPct
     * @return cnPublicPct the cnPublicPct
     */
    public Double getCnPublicPct()
    {
        return cnPublicPct;
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
     * Sets cnPrivate
     * @param cnPrivate the cnPrivate to set
     */
    public void setCnPrivate(Double cnPrivate)
    {
        this.cnPrivate = cnPrivate;
    }
    
    /**
     * Gets cnPrivate
     * @return cnPrivate the cnPrivate
     */
    public Double getCnPrivate()
    {
        return cnPrivate;
    }
    
    /**
     * Sets cnPrivatePct
     * @param cnPrivatePct the cnPrivatePct to set
     */
    public void setCnPrivatePct(Double cnPrivatePct)
    {
        this.cnPrivatePct = cnPrivatePct;
    }
    
    /**
     * Gets cnPrivatePct
     * @return cnPrivatePct the cnPrivatePct
     */
    public Double getCnPrivatePct()
    {
        return cnPrivatePct;
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
    
    public Object clone() throws CloneNotSupportedException
    {
        BudgetInputSourceDivided result = (BudgetInputSourceDivided) super
                .clone();
        return result;
    }
}
