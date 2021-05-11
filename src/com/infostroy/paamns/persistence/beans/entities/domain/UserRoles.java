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

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "user_roles")
public class UserRoles extends PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -9200461008285840416L;
    
    public UserRoles(Users user, Roles role, boolean isWrite)
    {
        this.setUser(user);
        this.setRole(role);
        this.setWriteAccess(isWrite);
    }
    
    public UserRoles()
    {
    }
    
    /**
     * Stores user value of entity.
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private Users   user;
    
    /**
     * Stores role value of entity.
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id")
    private Roles   role;
    
    /**
     * Stores writeAccess value of entity.
     */
    @Column(name = "write_access")
    private Boolean writeAccess;
    
    /**
     * Sets user
     * 
     * @param user
     *            the user to set
     */
    public void setUser(Users user)
    {
        this.user = user;
    }
    
    /**
     * Gets user
     * 
     * @return user the user
     */
    public Users getUser()
    {
        return user;
    }
    
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
    public void setWriteAccess(Boolean writeAccess)
    {
        this.writeAccess = writeAccess;
    }
    
    /**
     * Gets writeAccess
     * 
     * @return writeAccess the writeAccess
     */
    public Boolean getWriteAccess()
    {
        return writeAccess;
    }
}
