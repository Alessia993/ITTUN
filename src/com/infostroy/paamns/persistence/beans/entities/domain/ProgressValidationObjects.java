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

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProgressValidationStates;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "progress_validation_objects")
public class ProgressValidationObjects extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long        serialVersionUID = 1744456371441948293L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Projects                 project;
    
    /**
     * Stores state value of entity.
     */
    @ManyToOne
    @JoinColumn
    private ProgressValidationStates state;
    
    @Column(name = "approval_date")
    private Date                     approvalDate;
    
    /**
     * Sets state
     * @param state the state to set
     */
    public void setState(ProgressValidationStates state)
    {
        this.state = state;
    }
    
    /**
     * Gets state
     * @return state the state
     */
    public ProgressValidationStates getState()
    {
        return state;
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
     * Sets approvalDate
     * @param approvalDate the approvalDate to set
     */
    public void setApprovalDate(Date approvalDate)
    {
        this.approvalDate = approvalDate;
    }
    
    /**
     * Gets approvalDate
     * @return approvalDate the approvalDate
     */
    public Date getApprovalDate()
    {
        return approvalDate;
    }
}
