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
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "dur_reimbursements")
public class DurReimbursements extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -4432560203096717902L;
    
    @ManyToOne
    @JoinColumn(name = "dur_compilation_id")
    private DurCompilations   durCompilation;
    
    @Column(name = "reimbursment_finished")
    private boolean           reimbursmentFinished;
    
    @Column(name = "previously_refunded")
    private Double            previouslyRefundedFesr;
    
    @Column(name = "previously_refunded_cn")
    private Double            previouslyRefundedCn;
    
    @Column(name = "note")
    private String            note;
    
    /**
     * Sets durCompilation
     * @param durCompilation the durCompilation to set
     */
    public void setDurCompilation(DurCompilations durCompilation)
    {
        this.durCompilation = durCompilation;
    }
    
    /**
     * Gets durCompilation
     * @return durCompilation the durCompilation
     */
    public DurCompilations getDurCompilation()
    {
        return durCompilation;
    }
    
    /**
     * Sets reimbursmentFinished
     * @param reimbursmentFinished the reimbursmentFinished to set
     */
    public void setReimbursmentFinished(boolean reimbursmentFinished)
    {
        this.reimbursmentFinished = reimbursmentFinished;
    }
    
    /**
     * Gets reimbursmentFinished
     * @return reimbursmentFinished the reimbursmentFinished
     */
    public boolean getReimbursmentFinished()
    {
        return reimbursmentFinished;
    }
    
    /**
     * Gets previouslyRefundedFesr
     * @return previouslyRefundedFesr the previouslyRefundedFesr
     */
    public Double getPreviouslyRefundedFesr()
    {
        return previouslyRefundedFesr;
    }
    
    /**
     * Sets previouslyRefundedFesr
     * @param previouslyRefundedFesr the previouslyRefundedFesr to set
     */
    public void setPreviouslyRefundedFesr(Double previouslyRefundedFesr)
    {
        this.previouslyRefundedFesr = previouslyRefundedFesr;
    }
    
    /**
     * Gets previouslyRefundedCn
     * @return previouslyRefundedCn the previouslyRefundedCn
     */
    public Double getPreviouslyRefundedCn()
    {
        return previouslyRefundedCn;
    }
    
    /**
     * Sets previouslyRefundedCn
     * @param previouslyRefundedCn the previouslyRefundedCn to set
     */
    public void setPreviouslyRefundedCn(Double previouslyRefundedCn)
    {
        this.previouslyRefundedCn = previouslyRefundedCn;
    }
    
    /**
     * Gets note
     * @return note the note
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * Sets note
     * @param note the note to set
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    
}
