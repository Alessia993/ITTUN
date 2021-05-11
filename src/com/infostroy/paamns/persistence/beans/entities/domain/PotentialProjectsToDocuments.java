package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.Entity;

@javax.persistence.Entity
@Table(name = "potential_projects_documents")
public class PotentialProjectsToDocuments extends Entity
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7919972962203454559L;

    public PotentialProjectsToDocuments(Documents document,
    		PotentialProjects potentialProjects)
    {
        super();
        this.document = document;
        this.potentialProjects = potentialProjects;
    }
    
    public PotentialProjectsToDocuments()
    {      
    }
    

    @ManyToOne
    @JoinColumn
    private Documents       document;
    
    @ManyToOne
    @JoinColumn
    private PotentialProjects potentialProjects;
    
    public void setDocument(Documents document)
    {
        this.document = document;
    }
    
    public Documents getDocument()
    {
        return document;
    }

	public PotentialProjects getPotentialProjects() {
		return potentialProjects;
	}

	public void setPotentialProjects(PotentialProjects potentialProjects) {
		this.potentialProjects = potentialProjects;
	}
   
}

