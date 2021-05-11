/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@javax.persistence.Entity
@Table(name = "cost_defenitions_notes")
public class CostDefinitionsToNotes extends Entity
{
    /**
     * @param document
     * @param costDefinition
     */
    public CostDefinitionsToNotes(String note, CostDefinitions costDefinition)
    {
        super();
        this.setNote(note);
        this.costDefinition = costDefinition;
        insertedDate = new Date();
        if (SessionManager.getInstance() != null
                && SessionManager.getInstance().getSessionBean() != null
                && SessionManager.getInstance().getSessionBean().currentUser != null)
        {
            user = SessionManager.getInstance().getSessionBean().currentUser;
        }
    }
    
    public CostDefinitionsToNotes()
    {
        
        if (FacesContext.getCurrentInstance() != null
                && SessionManager.getInstance() != null
                && SessionManager.getInstance().getSessionBean() != null
                && SessionManager.getInstance().getSessionBean().currentUser != null)
        {
            user = SessionManager.getInstance().getSessionBean().currentUser;
        }
        
        insertedDate = new Date();
    }
    
    @ManyToOne
    @JoinColumn
    private Users           user;
    
    @Column(name = "inserted_date")
    private Date            insertedDate;
    
    /**
     * Stores document value of entity.
     */
    @Column(columnDefinition = "varchar(1000)")
    private String          note;
    
    /**
     * Stores costDefinition value of entity.
     */
    @ManyToOne
    @JoinColumn
    private CostDefinitions costDefinition;
    
    /**
     * @param costDefinition
     */
    public void setCostDefinition(CostDefinitions costDefinition)
    {
        this.costDefinition = costDefinition;
    }
    
    /**
     * @return
     */
    public CostDefinitions getCostDefinition()
    {
        return costDefinition;
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
     * Sets insertedDate
     * @param insertedDate the insertedDate to set
     */
    public void setInsertedDate(Date insertedDate)
    {
        this.insertedDate = insertedDate;
    }
    
    /**
     * Gets insertedDate
     * @return insertedDate the insertedDate
     */
    public Date getInsertedDate()
    {
        return insertedDate;
    }
    
    /**
     * Sets user
     * @param user the user to set
     */
    public void setUser(Users user)
    {
        this.user = user;
    }
    
    /**
     * Gets user
     * @return user the user
     */
    public Users getUser()
    {
        return user;
    }
    
    public String getUserName()
    {
        return this.getUser() != null ? this.getUser().toString() : "";
    }
}
