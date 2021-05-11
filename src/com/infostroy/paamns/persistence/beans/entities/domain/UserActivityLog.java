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

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2011.
 * 
 */
@Entity
@Table(name = "user_activity_log")
public class UserActivityLog extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users  user;
    
    @Column(name = "action_code")
    Long   actionCode;
    
    @Column(name = "action_text")
    String actionText;
    
    @Column(name = "last_peromed_date")
    Date   lastPerfomedDate;
    
    public Users getUser()
    {
        return user;
    }
    
    public void setUser(Users user)
    {
        this.user = user;
    }
    
    public Long getActionCode()
    {
        return actionCode;
    }
    
    public void setActionCode(Long actionCode)
    {
        this.actionCode = actionCode;
    }
    
    public String getActionText()
    {
        return actionText;
    }
    
    public void setActionText(String actionText)
    {
        this.actionText = actionText;
    }
    
    public Date getLastPerfomedDate()
    {
        return lastPerfomedDate;
    }
    
    public void setLastPerfomedDate(Date lastPerfomedDate)
    {
        this.lastPerfomedDate = lastPerfomedDate;
    }
    
}
