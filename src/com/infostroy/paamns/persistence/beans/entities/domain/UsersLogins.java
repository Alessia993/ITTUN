/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "users_logins")
public class UsersLogins extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -7252270623704271209L;
    
    /**
     * Stores value about user login
     */
    @Column(name = "is_logged")
    private Boolean           isLogged;
    
    @Column(name = "is_log_out")
    private Boolean           isLogout;
    
    @Column
    private String            ip;
    
    /**
     * Stores message value of entity.
     */
    @Column(name = "user_name", length = 255)
    private String            userName;
    
    /**
     * Sets isLogged
     * @param isLogged the isLogged to set
     */
    public void setIsLogged(Boolean isLogged)
    {
        this.isLogged = isLogged;
    }
    
    /**
     * Gets isLogged
     * @return isLogged the isLogged
     */
    public Boolean getIsLogged()
    {
        return isLogged;
    }
    
    /**
     * Sets userName
     * @param userName the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /**
     * Gets userName
     * @return userName the userName
     */
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * Sets isLogOut
     * @param isLogOut the isLogOut to set
     */
    public void setIsLogout(Boolean isLogout)
    {
        this.isLogout = isLogout;
    }
    
    /**
     * Gets isLogOut
     * @return isLogOut the isLogOut
     */
    public Boolean getIsLogout()
    {
        return isLogout;
    }
    
    /**
     * Sets ip
     * @param ip the ip to set
     */
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    /**
     * Gets ip
     * @return ip the ip
     */
    public String getIp()
    {
        return ip;
    }
}
