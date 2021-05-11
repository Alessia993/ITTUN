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

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonsForDelay;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "activation_procedure_info")
public class ActivationProcedureInfo extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long           serialVersionUID = 1890617913458204842L;
    
    /**
     * Stores activationProcedureAnagraph value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "activation_procedure_anagraph_id")
    private ActivationProcedureAnagraph activationProcedureAnagraph;
    
    /**
     * Stores step value of entity.
     */
    @Column
    private int                         step;
    
    /**
     * Stores description value of entity.
     */
    private String                      description;
    
    /**
     * Stores dateExpected value of entity.
     */
    @Column(name = "date_expected")
    private Date                        dateExpected;
    
    /**
     * Stores effectiveDate value of entity.
     */
    @Column(name = "effective_date")
    private Date                        effectiveDate;
    
    /**
     * Stores reasonsForDelay value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "reasons_for_delay_id")
    private ReasonsForDelay             reasonsForDelay;
    
    @Column
    private String                      note;
    
    /**
     * Sets activationProcedureAnagraph
     * @param activationProcedureAnagraph the activationProcedureAnagraph to set
     */
    public void setActivationProcedureAnagraph(
            ActivationProcedureAnagraph activationProcedureAnagraph)
    {
        this.activationProcedureAnagraph = activationProcedureAnagraph;
    }
    
    /**
     * Gets activationProcedureAnagraph
     * @return activationProcedureAnagraph the activationProcedureAnagraph
     */
    public ActivationProcedureAnagraph getActivationProcedureAnagraph()
    {
        return activationProcedureAnagraph;
    }
    
    /**
     * Sets step
     * @param step the step to set
     */
    public void setStep(int step)
    {
        this.step = step;
    }
    
    /**
     * Gets step
     * @return step the step
     */
    public int getStep()
    {
        return step;
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets dateExpected
     * @param dateExpected the dateExpected to set
     */
    public void setDateExpected(Date dateExpected)
    {
        this.dateExpected = dateExpected;
    }
    
    /**
     * Gets dateExpected
     * @return dateExpected the dateExpected
     */
    public Date getDateExpected()
    {
        return dateExpected;
    }
    
    /**
     * Sets effectiveDate
     * @param effectiveDate the effectiveDate to set
     */
    public void setEffectiveDate(Date effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }
    
    /**
     * Gets effectiveDate
     * @return effectiveDate the effectiveDate
     */
    public Date getEffectiveDate()
    {
        return effectiveDate;
    }
    
    /**
     * Sets reasonsForDelay
     * @param reasonsForDelay the reasonsForDelay to set
     */
    public void setReasonsForDelay(ReasonsForDelay reasonsForDelay)
    {
        this.reasonsForDelay = reasonsForDelay;
    }
    
    /**
     * Gets reasonsForDelay
     * @return reasonsForDelay the reasonsForDelay
     */
    public ReasonsForDelay getReasonsForDelay()
    {
        return reasonsForDelay;
    }
    
    /**
     * Sets note
     * @param note the note to set
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    
    /**
     * Gets note
     * @return note the note
     */
    public String getNote()
    {
        return note;
    }
}
