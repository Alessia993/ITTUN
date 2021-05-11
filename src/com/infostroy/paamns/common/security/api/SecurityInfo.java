/**
 * 
 */
package com.infostroy.paamns.common.security.api;

import java.util.List;

import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public class SecurityInfo
{
    Long                    _userID   = 0L;
    
    private List<UserRoles> _roleList = null;
    
    String                  _userName = null;
    
    String                  _password = null;
    
    /**
     * 
     */
    public SecurityInfo()
    {
    }
    
    /**
     * 
     * @param user
     * @param bu
     */
    public SecurityInfo(Long userID, String userName, String password)
    {
        this._userID = userID;
        this._userName = userName;
        this._password = password;
    }
    
    public SecurityInfo(Long userID, String userName, String password,
            List<UserRoles> roleList)
    {
        this._userID = userID;
        this._userName = userName;
        this._password = password;
        this._roleList = roleList;
    }
    
    /**
     * 
     * @param accessID
     */
    public void setUserID(Long userID)
    {
        this._userID = userID;
    }
    
    /**
     * 
     * @return
     */
    public Long getUserID()
    {
        return this._userID;
    }
    
    /**
     * 
     * @param isAdmin
     */
    public void setUserName(String userName)
    {
        this._userName = userName;
    }
    
    /**
     * 
     * @return
     */
    public String getUserName()
    {
        return this._userName;
    }
    
    /**
     * 
     * @param isAdmin
     */
    public void setPassword(String password)
    {
        this._password = password;
    }
    
    /**
     * 
     * @return
     */
    public String getPassword()
    {
        return this._password;
    }
    
    /**
     * Sets _roleList
     * 
     * @param _roleList
     *            the _roleList to set
     */
    public void set_roleList(List<UserRoles> _roleList)
    {
        this._roleList = _roleList;
    }
    
    /**
     * Gets _roleList
     * 
     * @return _roleList the _roleList
     */
    public List<UserRoles> get_roleList()
    {
        return _roleList;
    }
}
