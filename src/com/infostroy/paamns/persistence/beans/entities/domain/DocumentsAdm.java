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
import javax.persistence.Transient;

import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Sections;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2014.
 *
 */
@Entity
@Table(name="documents_adm")
public class DocumentsAdm extends
com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	 
    /**
     * 
     */
    public DocumentsAdm()
    {
        super();
    }
    
    /**
     * @param title
     * @param name
     * @param releaseFrom
     * @param documentDate
     * @param protocolNumber
     * @param section
     * @param fileName
     */
    public DocumentsAdm(String title, String name, Users user, Date documentDate,
            String protocolNumber, Sections section, Projects project,
            String fileName, Category category)
    {
        super();
        this.title = title;
        this.name = name;
        this.setUser(user);
        this.documentDate = documentDate;
        this.protocolNumber = protocolNumber;
        this.section = section;
        this.setFileName(fileName);
        this.category = category;
    }
    
    public DocumentsAdm(String title, String name, Users user, Date documentDate,
            String protocolNumber, Sections section, Projects project,
            String fileName, Category category, boolean isPublic, int role)
    {
        super();
        this.title = title;
        this.name = name;
        this.setUser(user);
        this.documentDate = documentDate;
        this.protocolNumber = protocolNumber;
        this.section = section;
        this.setFileName(fileName);
        this.setIsPublic(isPublic);
        this.setUploadRole(role);
        this.category = category;
    }
    
    public DocumentsAdm(String title, String name, Users user, Date documentDate,
            String protocolNumber, Sections section, Projects project,
            String fileName, Category category, boolean isPublic, int role,
            Long documentNumber, String note, Date editableDate)
    {
        super();
        this.title = title;
        this.name = name;
        this.setUser(user);
        this.documentDate = documentDate;
        this.protocolNumber = protocolNumber;
        this.section = section;
        this.setFileName(fileName);
        this.setIsPublic(isPublic);
        this.setUploadRole(role);
        this.category = category;
        this.documentNumber = documentNumber;
        this.note = note;
        this.editableDate = editableDate;
    }
    
    public DocumentsAdm(String title, String name, Users user, Date documentDate,
            String protocolNumber, Sections section, Projects project,
            String fileName, Category category, boolean isPublic)
    {
        super();
        this.title = title;
        this.name = name;
        this.setUser(user);
        this.documentDate = documentDate;
        this.protocolNumber = protocolNumber;
        this.section = section;
        this.setFileName(fileName);
        this.setIsPublic(isPublic);
        this.category = category;
    }
    
    public DocumentsAdm(String title, String name, Users user, Date documentDate,
            String protocolNumber, Sections section, Projects project,
            String fileName, Category category, boolean isPublic, Users partner)
    {
        super();
        this.title = title;
        this.name = name;
        this.setUser(user);
        this.documentDate = documentDate;
        this.protocolNumber = protocolNumber;
        this.section = section;
        this.setFileName(fileName);
        this.setIsPublic(isPublic);
        this.setForPartner(partner);
        this.category = category;
    }
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 3268752235958081837L;
    
    @ManyToOne
    @JoinColumn(name = "for_partner_id")
    private Users             forPartner;
    
    @Column(name = "is_for_daec")
    private Boolean           isForDaec;
    
    @Column(name = "is_public", columnDefinition = "bit(1) NOT NULL default false", nullable = false)
    private Boolean           isPublic;
    
    @Column(name = "upload_role")
    private Integer           uploadRole;
    
    @Column
    private String            title;
    
    @Column
    private String            name;
    
    /**
     * Stores releaseFrom value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Users             user;
    
    /**
     * Stores documentDate value of entity.
     */
    @Column(name = "document_date")
    private Date              documentDate;
    
    /**
     * Stores protocolNumber value of entity.
     */
    @Column(name = "protocol_number")
    private String            protocolNumber;
    
    /**
     * Stores section value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Sections          section;
    
    @ManyToOne
    @JoinColumn
    private Projects          project;
    
    /**
     * Stores fileName value of entity.
     */
    @Column(name = "file_name")
    private String            fileName;
    
    @ManyToOne
    @JoinColumn
    private Category          category;
    
    @Column(name = "document_number")
    private Long              documentNumber;
    
    @Column
    private String            note;
    
    @Column(name = "editable_date")
    private Date              editableDate;
    
    @Column(name = "load_from_documents")
    private Boolean loadFromDocuments;
    
    @Transient
    private boolean           editable;
    
    /**
     * Sets title
     * @param title the title to set
     */
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
     * Sets protocolNumber
     * @param protocolNumber the protocolNumber to set
     */
    public void setProtocolNumber(String protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Gets protocolNumber
     * @return protocolNumber the protocolNumber
     */
    public String getProtocolNumber()
    {
        return protocolNumber;
    }
    
    /**
     * Sets section
     * @param section the section to set
     */
    public void setSection(Sections section)
    {
        this.section = section;
    }
    
    /**
     * Gets section
     * @return section the section
     */
    public Sections getSection()
    {
        return section;
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
    
    public void setDocumentDate(Date documentDate)
    {
        this.documentDate = documentDate;
    }
    
    public Date getDocumentDate()
    {
        return documentDate;
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
     * Sets editable
     * @param editable the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    /**
     * Gets editable
     * @return editable the editable
     */
    public boolean isEditable()
    {
        return editable;
    }
    
    /**
     * Sets isPublic
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(Boolean isPublic)
    {
        this.isPublic = isPublic;
    }
    
    /**
     * Gets isPublic
     * @return isPublic the isPublic
     */
    public Boolean getIsPublic()
    {
        return isPublic == null ? false : isPublic;
    }
    
    /**
     * Sets uploadRole
     * @param uploadRole the uploadRole to set
     */
    public void setUploadRole(Integer uploadRole)
    {
        this.uploadRole = uploadRole;
    }
    
    /**
     * Gets uploadRole
     * @return uploadRole the uploadRole
     */
    public Integer getUploadRole()
    {
    	return uploadRole;
    }
   
    public UploadDocumentRoleType getUploadRoleType()
    {
        return UploadDocumentRoleType.getById(uploadRole);
    }
    
   
    
    /**
     * Sets forPartner
     * @param forPartner the forPartner to set
     */
    public void setForPartner(Users forPartner)
    {
        this.forPartner = forPartner;
    }
    
    /**
     * Gets forPartner
     * @return forPartner the forPartner
     */
    public Users getForPartner()
    {
        return forPartner;
    }
    
    /**
     * Gets category
     * @return category the category
     */
    public Category getCategory()
    {
        return category;
    }
    
    /**
     * Sets category
     * @param category the category to set
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }
    
    /**
     * Gets isForDaec
     * @return isForDaec the isForDaec
     */
    public Boolean getIsForDaec()
    {
        return isForDaec;
    }
    
    /**
     * Sets isForDaec
     * @param isForDaec the isForDaec to set
     */
    public void setIsForDaec(Boolean isForDaec)
    {
        this.isForDaec = isForDaec;
    }
    
    /**
     * Gets documentNumber
     * @return documentNumber the documentNumber
     */
    public Long getDocumentNumber()
    {
        return documentNumber;
    }
    
    /**
     * Sets documentNumber
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(Long documentNumber)
    {
        this.documentNumber = documentNumber;
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
	 * Gets loadFromDocuments
	 * @return loadFromDocuments the loadFromDocuments
	 */
	public Boolean getLoadFromDocuments()
	{
		return loadFromDocuments;
	}

	/**
	 * Sets loadFromDocuments
	 * @param loadFromDocuments the loadFromDocuments to set
	 */
	public void setLoadFromDocuments(Boolean loadFromDocuments)
	{
		this.loadFromDocuments = loadFromDocuments;
	}

	public CFPartnerAnagraphs getPartnerAnagraph()  {
    	if(this.getUser()!= null) {
    		try {
				return BeansFactory
				        .CFPartnerAnagraphs().GetByUser(
				        		this.getUser().getId());
			} catch (PersistenceBeanException e) {
			}
    	}
    	return null;
    }
    
    
}
