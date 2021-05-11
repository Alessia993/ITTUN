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

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonsForDelay;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "procedure_tipology_step_infos")
public class ProcedureTipologyStepInfos extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    public ProcedureTipologyStepInfos()
    {
    }
    
    public ProcedureTipologyStepInfos(ProcedureTipologyStep step)
    {
        this.setTipologyStep(step);
    }
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long     serialVersionUID = -1303422420564515934L;
    
    /**
     * Stores procedure value of entity.
     */
    @ManyToOne
    @JoinColumn
    private AssignmentProcedures  procedure;
    
    /**
     * Stores tipology value of entity.
     */
    @ManyToOne
    @JoinColumn
    private ProcedureTipologyStep tipologyStep;
    
    /**
     * Stores description value of entity.
     */
    @Column
    private String                description;
    
    /**
     * Stores estimationDate value of entity.
     */
    @Column(name = "estimation_date")
    private Date                  estimationDate;
    
    /**
     * Stores realDate value of entity.
     */
    @Column(name = "real_date")
    private Date                  realDate;
    
    /**
     * Stores competencePeople value of entity.
     */
    @Column(name = "competence_people")
    private String                competencePeople;
    
    /**
     * Stores amount value of entity.
     */
    @Column
    private Double                amount;
    
    /**
     * Stores differenceReason value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "reason_for_delay_id")
    private ReasonsForDelay       differenceReason;
    
    @Transient
    private String                diffReasonId;
    
    /**
     * Stores note value of entity.
     */
    @Column
    private String                note;
    
    /**
     * Sets procedure
     * 
     * @param procedure
     *            the procedure to set
     */
    public void setProcedure(AssignmentProcedures procedure)
    {
        this.procedure = procedure;
    }
    
    /**
     * Gets procedure
     * 
     * @return procedure the procedure
     */
    public AssignmentProcedures getProcedure()
    {
        return procedure;
    }
    
    /**
     * Sets description
     * 
     * @param description
     *            the description to set
     */
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
     * Sets realDate
     * 
     * @param realDate
     *            the realDate to set
     */
    public void setRealDate(Date realDate)
    {
        this.realDate = realDate;
    }
    
    /**
     * Gets realDate
     * 
     * @return realDate the realDate
     */
    public Date getRealDate()
    {
        return realDate;
    }
    
    /**
     * Sets estimationDate
     * 
     * @param estimationDate
     *            the estimationDate to set
     */
    public void setEstimationDate(Date estimationDate)
    {
        this.estimationDate = estimationDate;
    }
    
    /**
     * Gets estimationDate
     * 
     * @return estimationDate the estimationDate
     */
    public Date getEstimationDate()
    {
        return estimationDate;
    }
    
    /**
     * Sets competencePeople
     * 
     * @param competencePeople
     *            the competencePeople to set
     */
    public void setCompetencePeople(String competencePeople)
    {
        this.competencePeople = competencePeople;
    }
    
    /**
     * Gets competencePeople
     * 
     * @return competencePeople the competencePeople
     */
    public String getCompetencePeople()
    {
        return competencePeople;
    }
    
    /**
     * Sets differenceReason
     * 
     * @param differenceReason
     *            the differenceReason to set
     */
    public void setDifferenceReason(ReasonsForDelay differenceReason)
    {
        this.differenceReason = differenceReason;
    }
    
    /**
     * Gets differenceReason
     * 
     * @return differenceReason the differenceReason
     */
    public ReasonsForDelay getDifferenceReason()
    {
        return differenceReason;
    }
    
    /**
     * Sets note
     * 
     * @param note
     *            the note to set
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    
    /**
     * Gets note
     * 
     * @return note the note
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * Sets tipologyStep
     * 
     * @param tipologyStep
     *            the tipologyStep to set
     */
    public void setTipologyStep(ProcedureTipologyStep tipologyStep)
    {
        this.tipologyStep = tipologyStep;
    }
    
    /**
     * Gets tipologyStep
     * 
     * @return tipologyStep the tipologyStep
     */
    public ProcedureTipologyStep getTipologyStep()
    {
        return tipologyStep;
    }
    
    /**
     * Sets diffReasonId
     * 
     * @param diffReasonId
     *            the diffReasonId to set
     */
    public void setDiffReasonId(String diffReasonId)
    {
        this.diffReasonId = diffReasonId;
        try
        {
            if (diffReasonId != null && !diffReasonId.isEmpty())
            {
                this.setDifferenceReason(BeansFactory.ReasonsForDelay().Load(
                        diffReasonId));
            }
        }
        catch(NumberFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Gets diffReasonId
     * 
     * @return diffReasonId the diffReasonId
     */
    public String getDiffReasonId()
    {
        if (this.getDifferenceReason() != null)
        {
            this.diffReasonId = this.getDifferenceReason().getId().toString();
        }
        
        return diffReasonId;
    }
    
    /**
     * Sets amount
     * 
     * @param amount
     *            the amount to set
     */
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    
    /**
     * Gets amount
     * 
     * @return amount the amount
     */
    public Double getAmount()
    {
        return amount;
    }
}
