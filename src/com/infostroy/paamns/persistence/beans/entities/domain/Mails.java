/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.common.helpers.TextCoder;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "mails")
public class Mails extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    @Transient
    private boolean           selected;
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 7581776193803457282L;
    
    /**
     * Stores user value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Users             user;
    
    /**
     * Stores message value of entity.
     */
    @Column(columnDefinition = "TEXT")
    private String            message;
    
    /**
     * Stores success value of entity.
     */
    @Column
    private Boolean           success;
    
    @Column(name = "to_email")
    private String            toEmail;
    
    @Column(name = "subject")
    private String            subject;
    
    @Column(name = "status")
    private Long              status;
    
    /**
     * Sets message
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    /**
     * Gets message
     * @return message the message
     */
    public String getMessage()
    {
        return TextCoder.Decode(message);
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
     * Sets success
     * @param success the success to set
     */
    public void setSuccess(Boolean success)
    {
        this.success = success;
    }
    
    /**
     * Gets success
     * @return success the success
     */
    public Boolean getSuccess()
    {
        return success;
    }
    
    /**
     * Sets selected
     * @param selected the selected to set
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    
    /**
     * Gets selected
     * @return selected the selected
     */
    public boolean getSelected()
    {
        return selected;
    }
    
    /**
     * Gets status
     * @return status the status
     */
    public Long getStatus()
    {
        return status;
    }
    
    /**
     * Sets status
     * @param status the status to set
     */
    public void setStatus(Long status)
    {
        this.status = status;
    }
    
    /**
     * Gets toEmail
     * @return toEmail the toEmail
     */
    public String getToEmail()
    {
        return toEmail;
    }
    
    /**
     * Sets toEmail
     * @param toEmail the toEmail to set
     */
    public void setToEmail(String toEmail)
    {
        this.toEmail = toEmail;
    }
    
    /**
     * Gets subject
     * @return subject the subject
     */
    public String getSubject()
    {
        return subject;
    }
    
    /**
     * Sets subject
     * @param subject the subject to set
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
}
