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
@Table(name = "procedure_notes")
public class ProcedureNotes extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -8847495737805146048L;
    
    /**
     * Stores procedure value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Procedures        procedure;
    
    /**
     * Stores note value of entity.
     */
    @Column
    private String            note;
    
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
     * Sets procedure
     * 
     * @param procedure
     *            the procedure to set
     */
    public void setProcedure(Procedures procedure)
    {
        this.procedure = procedure;
    }
    
    /**
     * Gets procedure
     * 
     * @return procedure the procedure
     */
    public Procedures getProcedure()
    {
        return procedure;
    }
}
