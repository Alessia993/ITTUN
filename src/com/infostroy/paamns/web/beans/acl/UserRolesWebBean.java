/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class UserRolesWebBean
{
    
    /**
     * Stores role value of entity.
     */
    private Roles  role;
    
    /**
     * Stores writeAccess value of entity.
     */
    private String writeAccess = "null";
    
    /**
     * Sets role
     * 
     * @param role
     *            the role to set
     */
    public void setRole(Roles role)
    {
        this.role = role;
    }
    
    /**
     * Gets role
     * 
     * @return role the role
     */
    public Roles getRole()
    {
        return role;
    }
    
    /**
     * Sets writeAccess
     * 
     * @param writeAccess
     *            the writeAccess to set
     */
    public void setWriteAccess(String writeAccess)
    {
        this.writeAccess = writeAccess;
    }
    
    /**
     * Gets writeAccess
     * 
     * @return writeAccess the writeAccess
     */
    public String getWriteAccess()
    {
        return writeAccess;
    }
    
}
