package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 * modified by Ingloba360
 *
 */
@Entity
@Table(name = "procedures")
public class Procedures extends PersistentEntity {
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long         serialVersionUID = 2314609574386006523L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Projects                  project;
    
    /**
     * Stores step value of entity.
     */
    @Column
    private Integer                   step;
    
    /**
     * Stores description value of entity.
     */
    @Column
    private String                    description;
    
    /**
     * Stores partnerResponsible value of entity.
     */
    @Column(name = "partner_responsible")
    private String                    partnerResponsible;
    
    /**
     * Stores expectedStartDate value of entity.
     */
    @Column(name = "expected_start_date")
    private Date                      expectedStartDate;
    
    /**
     * Stores effectiveStartDate value of entity.
     */
    @Column(name = "effective_start_date")
    private Date                      effectiveStartDate;
    
    /**
     * Stores plannedEndDate value of entity.
     */
    @Column(name = "planned_end_date")
    private Date                      plannedEndDate;
    
    /**
     * Stores effectiveEndDate value of entity.
     */
    @Column(name = "effective_end_date")
    private Date                      effectiveEndDate;
    
    /**
     * Stores note value of entity.
     */
    @Column
    private String                    note;
    
    /**
     * Stores progressValidationObject value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "progress_validation_object_id")
    private ProgressValidationObjects progressValidationObject;
    
    @Column(name = "version")
    private Long                      version;
    
    @Column(name = "cod_phase", columnDefinition="char(4)")
    private String codPhase;
    
    public Procedures clone()
    {
        Procedures newEntity = new Procedures();
        
        newEntity.setCreateDate(this.getCreateDate());
        newEntity.setDeleted(this.getDeleted());
        newEntity.setDescription(this.getDescription());
        newEntity.setEffectiveEndDate(this.getEffectiveEndDate());
        newEntity.setEffectiveStartDate(this.getEffectiveStartDate());
        newEntity.setExpectedStartDate(this.getExpectedStartDate());
        newEntity.setNote(this.getNote());
        newEntity.setPartnerResponsible(this.getPartnerResponsible());
        newEntity.setPlannedEndDate(this.getPlannedEndDate());
        newEntity.setProgressValidationObject(this
                .getProgressValidationObject());
        newEntity.setProject(this.getProject());
        newEntity.setStep(this.getStep());
        newEntity.setVersion(this.getVersion());
        
        return newEntity;
    }
    
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
     * Sets step
     * @param step the step to set
     */
    public void setStep(Integer step)
    {
        this.step = step;
    }
    
    /**
     * Gets step
     * @return step the step
     */
    public Integer getStep()
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
     * Sets partnerResponsible
     * @param partnerResponsible the partnerResponsible to set
     */
    public void setPartnerResponsible(String partnerResponsible)
    {
        this.partnerResponsible = partnerResponsible;
    }
    
    /**
     * Gets partnerResponsible
     * @return partnerResponsible the partnerResponsible
     */
    public String getPartnerResponsible()
    {
        return partnerResponsible;
    }
    
    /**
     * Sets expectedStartDate
     * @param expectedStartDate the expectedStartDate to set
     */
    public void setExpectedStartDate(Date expectedStartDate)
    {
        this.expectedStartDate = expectedStartDate;
    }
    
    /**
     * Gets expectedStartDate
     * @return expectedStartDate the expectedStartDate
     */
    public Date getExpectedStartDate()
    {
        return expectedStartDate;
    }
    
    /**
     * Sets effectiveStartDate
     * @param effectiveStartDate the effectiveStartDate to set
     */
    public void setEffectiveStartDate(Date effectiveStartDate)
    {
        this.effectiveStartDate = effectiveStartDate;
    }
    
    /**
     * Gets effectiveStartDate
     * @return effectiveStartDate the effectiveStartDate
     */
    public Date getEffectiveStartDate()
    {
        return effectiveStartDate;
    }
    
    /**
     * Sets plannedEndDate
     * @param plannedEndDate the plannedEndDate to set
     */
    public void setPlannedEndDate(Date plannedEndDate)
    {
        this.plannedEndDate = plannedEndDate;
    }
    
    /**
     * Gets plannedEndDate
     * @return plannedEndDate the plannedEndDate
     */
    public Date getPlannedEndDate()
    {
        return plannedEndDate;
    }
    
    /**
     * Sets effectiveEndDate
     * @param effectiveEndDate the effectiveEndDate to set
     */
    public void setEffectiveEndDate(Date effectiveEndDate)
    {
        this.effectiveEndDate = effectiveEndDate;
    }
    
    /**
     * Gets effectiveEndDate
     * @return effectiveEndDate the effectiveEndDate
     */
    public Date getEffectiveEndDate()
    {
        return effectiveEndDate;
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
    
    /**
     * Sets progressValidationObject
     * @param progressValidationObject the progressValidationObject to set
     */
    public void setProgressValidationObject(
            ProgressValidationObjects progressValidationObject)
    {
        this.progressValidationObject = progressValidationObject;
    }
    
    /**
     * Gets progressValidationObject
     * @return progressValidationObject the progressValidationObject
     */
    public ProgressValidationObjects getProgressValidationObject()
    {
        return progressValidationObject;
    }
    
    /**
     * Gets notes for procedure 
     * @return
     */
    public List<ProcedureNotes> getNotes()
    {
        try
        {
            return BeansFactory.ProcedureNotes().LoadByProcedure(
                    this.getId().toString());
        }
        catch(PersistenceBeanException e)
        {
            
            return new ArrayList<ProcedureNotes>();
        }
    }
    
    public Boolean getHasNotes()
    {
        return this.getNotes().size() > 0;
    }
    
    /**
     * Sets version
     * @param version the version to set
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }
    
    /**
     * Gets version
     * @return version the version
     */
    public Long getVersion()
    {
        return version;
    }
    
    /**
     * Sets codPhase.
     * @param codPhase
     */
    public void setCodPhase(String codPhase) {
        this.codPhase = codPhase;
    }
    
    /**
     * Gets codPhase.
     * @return codPhase
     */
    public String getCodPhase() {
        return codPhase;
    }
}
