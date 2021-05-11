/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "dur_summaries")
public class DurSummaries extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 707411752026754774L;
    
    /**
     * Stores durCompilation value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "dur_compilation_id")
    private DurCompilations   durCompilation;
    
    /**
     * Stores totalAmount value of entity.
     */
    @Column(name = "total_amount")
    private Double            totalAmount;
    
    @Column(name = "total_amount2")
    private Double            totalAmount2;
    
    /**
     * Stores fesrAmount value of entity.
     */
    @Column(name = "fesr_amount")
    private Double            fesrAmount;
    
    @Column(name = "fesr_amount2")
    private Double            fesrAmount2;
    
    /**
     * Stores itCnReimbursementAmount value of entity.
     */
    @Column(name = "it_cn_reimbursement_amount")
    private Double            itCnReimbursementAmount;
    
    @Column(name = "it_cn_reimbursement_amount2")
    private Double            itCnReimbursementAmount2;
    
    /**
     * Stores frCJnReimbursementAmount value of entity.
     */
    @Column(name = "fr_cn_reimbursement_amount")
    private Double            frCnReimbursementAmount;
    
    @Column(name = "fr_cn_reimbursement_amount2")
    private Double            frCnReimbursementAmount2;
    
    /**
     * Stores totalReimbursement value of entity.
     */
    @Column(name = "total_reimbursement")
    private Double            totalReimbursement;
    
    @Column(name = "total_amount_annuled")
    private Double            totalAmountAnnulled;
    
    @Column(name = "total_rectidied")
    private Double            totalRectified;
    
    @Column(name = "user_fesr_amount")
    private Boolean            userFesrAmount;
    
    /**
     * Stores itCnReimbursementAmount value of entity.
     */
    @Column(name = "user_it_cn_reimbursement_amount")
    private Boolean            userItCnReimbursementAmount;
    
    /**
     * Stores frCJnReimbursementAmount value of entity.
     */
    @Column(name = "user_fr_cn_reimbursement_amount")
    private Boolean            userFrCnReimbursementAmount;
    /**
     * Sets durCompilation
     * 
     * @param durCompilation
     *            the durCompilation to set
     */
    public void setDurCompilation(DurCompilations durCompilation)
    {
        this.durCompilation = durCompilation;
    }
    
    /**
     * Gets durCompilation
     * 
     * @return durCompilation the durCompilation
     */
    public DurCompilations getDurCompilation()
    {
        return durCompilation;
    }
    
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
        return totalAmount;
    }
    
    /**
     * Sets fesrAmount
     * 
     * @param fesrAmount
     *            the fesrAmount to set
     */
    public void setFesrAmount(Double fesrAmount)
    {
        this.fesrAmount = fesrAmount;
    }
    
    /**
     * Gets fesrAmount
     * 
     * @return fesrAmount the fesrAmount
     */
    public Double getFesrAmount()
    {
        return fesrAmount;
    }
    
    /**
     * Sets itCnReimbursementAmount
     * 
     * @param itCnReimbursementAmount
     *            the itCnReimbursementAmount to set
     */
    public void setItCnReimbursementAmount(Double itCnReimbursementAmount)
    {
        this.itCnReimbursementAmount = itCnReimbursementAmount;
    }
    
    /**
     * Gets itCnReimbursementAmount
     * 
     * @return itCnReimbursementAmount the itCnReimbursementAmount
     */
    public Double getItCnReimbursementAmount()
    {
        return itCnReimbursementAmount;
    }
    
    /**
     * Sets frCnReimbursementAmount
     * 
     * @param frCnReimbursementAmount
     *            the frCnReimbursementAmount to set
     */
    public void setFrCnReimbursementAmount(Double frCnReimbursementAmount)
    {
        this.frCnReimbursementAmount = frCnReimbursementAmount;
    }
    
    /**
     * Gets frCnReimbursementAmount
     * 
     * @return frCnReimbursementAmount the frCnReimbursementAmount
     */
    public Double getFrCnReimbursementAmount()
    {
        return frCnReimbursementAmount;
    }
    
    /**
     * Sets totalReimbursement
     * 
     * @param totalReimbursement
     *            the totalReimbursement to set
     */
    public void setTotalReimbursement(Double totalReimbursement)
    {
        this.totalReimbursement = totalReimbursement;
    }
    
    /**
     * Gets totalReimbursement
     * 
     * @return totalReimbursement the totalReimbursement
     */
    public Double getTotalReimbursement()
    {
        return totalReimbursement;
    }
    
    public Double getTotalAmountAnnulled()
    {
        return totalAmountAnnulled;
    }
    
    public void setTotalAmountAnnulled(Double totalAmountAnnulled)
    {
        this.totalAmountAnnulled = totalAmountAnnulled;
    }
    
    public Double getTotalRectified()
    {
        return totalRectified;
    }
    
    public void setTotalRectified(Double totalRectified)
    {
        this.totalRectified = totalRectified;
    }

    /**
     * Gets userFesrAmount
     * @return userFesrAmount the userFesrAmount
     */
    public Boolean getUserFesrAmount()
    {
        return userFesrAmount;
    }

    /**
     * Sets userFesrAmount
     * @param userFesrAmount the userFesrAmount to set
     */
    public void setUserFesrAmount(Boolean userFesrAmount)
    {
        this.userFesrAmount = userFesrAmount;
    }

    /**
     * Gets userItCnReimbursementAmount
     * @return userItCnReimbursementAmount the userItCnReimbursementAmount
     */
    public Boolean getUserItCnReimbursementAmount()
    {
        return userItCnReimbursementAmount;
    }

    /**
     * Sets userItCnReimbursementAmount
     * @param userItCnReimbursementAmount the userItCnReimbursementAmount to set
     */
    public void setUserItCnReimbursementAmount(
            Boolean userItCnReimbursementAmount)
    {
        this.userItCnReimbursementAmount = userItCnReimbursementAmount;
    }

    /**
     * Gets userFrCnReimbursementAmount
     * @return userFrCnReimbursementAmount the userFrCnReimbursementAmount
     */
    public Boolean getUserFrCnReimbursementAmount()
    {
        return userFrCnReimbursementAmount;
    }

    /**
     * Sets userFrCnReimbursementAmount
     * @param userFrCnReimbursementAmount the userFrCnReimbursementAmount to set
     */
    public void setUserFrCnReimbursementAmount(
            Boolean userFrCnReimbursementAmount)
    {
        this.userFrCnReimbursementAmount = userFrCnReimbursementAmount;
    }

	public Double getTotalAmount2() {
		return totalAmount2;
	}

	public void setTotalAmount2(Double totalAmount2) {
		this.totalAmount2 = totalAmount2;
	}

	public Double getFesrAmount2() {
		return fesrAmount2;
	}

	public void setFesrAmount2(Double fesrAmount2) {
		this.fesrAmount2 = fesrAmount2;
	}

	public Double getItCnReimbursementAmount2() {
		return itCnReimbursementAmount2;
	}

	public void setItCnReimbursementAmount2(Double itCnReimbursementAmount2) {
		this.itCnReimbursementAmount2 = itCnReimbursementAmount2;
	}

	public Double getFrCnReimbursementAmount2() {
		return frCnReimbursementAmount2;
	}

	public void setFrCnReimbursementAmount2(Double frCnReimbursementAmount2) {
		this.frCnReimbursementAmount2 = frCnReimbursementAmount2;
	}
    
}
