/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "project_states_changes")
public class ProjectStatesChanges extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 2998513309112973142L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Projects          project;
    
    /**
     * Stores fromState value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "from_state_id")
    private ProjectStates     fromState;
    
    /**
     * Stores toState value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "to_state_id")
    private ProjectStates     toState;
    
    /**
     * Stores description value of entity.
     */
    @Column
    private String            description;
    
    /**
     * Stores documentNumber value of entity.
     */
    @Column(name = "document_number")
    private String            documentNumber;
    
    /**
     * Stores document value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Documents         document;
    
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
     * Sets fromState
     * 
     * @param fromState
     *            the fromState to set
     */
    public void setFromState(ProjectStates fromState)
    {
        this.fromState = fromState;
    }
    
    /**
     * Gets fromState
     * 
     * @return fromState the fromState
     */
    public ProjectStates getFromState()
    {
        return fromState;
    }
    
    /**
     * Sets toState
     * 
     * @param toState
     *            the toState to set
     */
    public void setToState(ProjectStates toState)
    {
        this.toState = toState;
    }
    
    /**
     * Gets toState
     * 
     * @return toState the toState
     */
    public ProjectStates getToState()
    {
        return toState;
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
     * Sets documentNumber
     * 
     * @param documentNumber
     *            the documentNumber to set
     */
    public void setDocumentNumber(String documentNumber)
    {
        this.documentNumber = documentNumber;
    }
    
    /**
     * Gets documentNumber
     * 
     * @return documentNumber the documentNumber
     */
    public String getDocumentNumber()
    {
        return documentNumber;
    }
    
    /**
     * Sets document
     * 
     * @param document
     *            the document to set
     */
    public void setDocument(Documents document)
    {
        this.document = document;
    }
    
    /**
     * Gets document
     * 
     * @return document the document
     */
    public Documents getDocument()
    {
        return document;
    }
}
