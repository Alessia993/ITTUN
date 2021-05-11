/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "signed_documents")
public class SignedDocuments extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5803386299514498873L;


	/**
     * 
     */
    public SignedDocuments()
    {
        super();
    }
    
    
    
    
    public SignedDocuments(String title, String name, String description, String fileName, String note,
			Date editableDate, Documents forDocument, String signername, String signdate) {
		super();
		this.title = title;
		this.name = name;
		this.description = description;
		this.fileName = fileName;
		this.note = note;
		this.editableDate = editableDate;
		this.forDocument = forDocument;
		this.signername = signername;
		this.signdate = signdate;
	}




	@Column
    private String            title;
    
    @Column
    private String            name;
    
    @Column
    private String			  description;   
    
    
    
    
 
    /**
     * Stores fileName value of entity.
     */
    @Column(name = "file_name")
    private String            fileName;
    
    
    @Column
    private String            note;
    
    @Column(name = "editable_date")  //TODO da verificare
    private Date              editableDate;
    
    
    @OneToOne
    @JoinColumn(name = "document_id")
    private Documents   forDocument;

	
    @Column(name = "signer_name")
    public String 		signername;

    @Column(name = "sign_date")
    public String 		signdate;
	
      
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * Gets title
     * @return title the title
     */
    public String getTitle()
    {
        return title;
    }
    
   
    /**
     * Sets fileName
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName)
    {
        if (fileName.lastIndexOf("Files") != -1)
        {
            this.fileName = fileName.substring(fileName.lastIndexOf("Files"));
        }
        else
        {
            this.fileName = fileName;
        }
    }
    
    /**
     * Gets fileName
     * @return fileName the fileName
     */
    public String getFileName()
    {
        return fileName;
    }
    
    /**
     * Sets name
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets name
     * @return name the name
     */
    public String getName()
    {
        return name;
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
     * Sets note
     * @param note the note to set
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    
    /**
     * Gets editableDate
     * @return editableDate the editableDate
     */
    public Date getEditableDate()
    {
        return editableDate;
    }
    
    /**
     * Sets editableDate
     * @param editableDate the editableDate to set
     */
    public void setEditableDate(Date editableDate)
    {
        this.editableDate = editableDate;
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
	 * Sets description
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Gets Fordocuments
	 * @return description the description
	 */
	public Documents getForDocument()
	{
		return forDocument;
	}

	/**
	 * Sets Fordocuments
	 * @param description the description to set
	 */
	public void setForDocument(Documents forDocument)
	{
		this.forDocument = forDocument;
	}
	
	
	
	   public void setSignerName(String signername)
	    {
	        this.signername = signername;
	    }
	    
	    /**
	     * Gets signer name
	     * @return signer name
	     */
	    public String getSignerName()
	    {
	        return signername;
	    }  

	    
	   public void setSignDate(String signdate)
	    {
	        this.signdate = signdate;
	    }
	    
	    /**
	     * Gets signdate
	     * @return signdate
	     */
	    public String getSignDate()
	    {
	        return signdate;
	    }  
}
