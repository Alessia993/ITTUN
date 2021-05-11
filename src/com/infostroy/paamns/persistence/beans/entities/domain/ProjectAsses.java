/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.Asses;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "project_asses")
public class ProjectAsses extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 9141243410291927198L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "project_id")
    private Projects          project;
    
    /**
     * Stores asse value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "asse_id")
    private Asses             asse;
    
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
     * Sets asse
     * 
     * @param asse
     *            the asse to set
     */
    public void setAsse(Asses asse)
    {
        this.asse = asse;
    }
    
    /**
     * Gets asse
     * 
     * @return asse the asse
     */
    public Asses getAsse()
    {
        return asse;
    }
}
