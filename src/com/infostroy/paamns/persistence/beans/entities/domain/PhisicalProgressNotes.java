package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.SessionManager;

@javax.persistence.Entity
@Table(name = "phisical_progress_notes")
public class PhisicalProgressNotes extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1258495094045945113L;

	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.ALL })
	@JoinColumn
	private ProjectIndicators projectIndicators;

	/**
	 * Stores note value of entity.
	 */
	@Column
	private String note;
	
	@ManyToOne
    @JoinColumn
    private Users           user;
    
    @Column(name = "inserted_date")
    private Date            insertedDate;
    
    
    public PhisicalProgressNotes(){
    	if (FacesContext.getCurrentInstance() != null
                && SessionManager.getInstance() != null
                && SessionManager.getInstance().getSessionBean() != null
                && SessionManager.getInstance().getSessionBean().currentUser != null)
        {
            user = SessionManager.getInstance().getSessionBean().currentUser;
        }
        
        insertedDate = new Date();
    }

	public ProjectIndicators getProjectIndicators() {
		return projectIndicators;
	}

	public void setProjectIndicators(ProjectIndicators projectIndicators) {
		this.projectIndicators = projectIndicators;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}
	
	 public String getUserName()
	    {
	        return this.getUser() != null ? this.getUser().toString() : "";
	    }

}
