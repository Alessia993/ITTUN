/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ControllerUserAnagraphWebBean
{
    protected transient final Log log = LogFactory.getLog(getClass());
    
    public ControllerUserAnagraphWebBean()
    {
        
    }
    
    public ControllerUserAnagraphWebBean(ControllerUserAnagraph item)
            throws HibernateException, PersistenceBeanException
    {
        setId(item.getId());
        try
        {
            setCountry(item.getCountry().getValue());
        }
        catch(Exception e)
        {
            log.error(e);
        }
        try
        {
            setControllerType(item.getControllerType().getValue());
        }
        catch(Exception e)
        {
            log.error(e);
        }
        Users user = item.getUser();
        
        if (user != null && user.getFiscalCode() != null
                && !user.getFiscalCode().isEmpty())
        {
            this.setCode(user.getFiscalCode());
        }
        else if (item.getVatNumber() != null)
        {
            this.setCode(item.getVatNumber());
        }
        
        if (user != null)
        {
            if (user.getRoles() != null
                    && ControllerUserAnagraphEditBean.isCil(user.getRoles()))
            {
                this.setRole(Utils.getString("Resources",
                        ControllerUserRoles.CIL.getRole(), null));
            }
            else if (user.getRoles() != null
                    && ControllerUserAnagraphEditBean.isDaec(user.getRoles()))
            {
                if (item.getCountry().getCode()
                        .equals(CountryTypes.Italy.getCountry()))
                {
                    this.setRole(Utils.getString("Resources",
                            ControllerUserRoles.VALIDATOR.getRole(), null));
                }
                else if (item.getCountry().getCode()
                        .equals(CountryTypes.France.getCountry()))
                {
                    this.setRole(Utils.getString("Resources",
                            ControllerUserRoles.DAEC.getRole(), null));
                }
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append(user.getName());
            sb.append(" ");
            sb.append(user.getSurname());
            this.setUserName(sb.toString());
            this.setUserEmail(user.getMail());
        }
    }
    
    private long   id;
    
    private String role;
    
    private String controllerType;
    
    private String country;
    
    private String code;
    
    private String userName;
    
    private String userEmail;
    
    /**
     * Sets controllerType
     * 
     * @param controllerType
     *            the controllerType to set
     */
    public void setControllerType(String controllerType)
    {
        this.controllerType = controllerType;
    }
    
    /**
     * Gets controllerType
     * 
     * @return controllerType the controllerType
     */
    public String getControllerType()
    {
        return controllerType;
    }
    
    /**
     * Sets country
     * 
     * @param country
     *            the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    /**
     * Gets country
     * 
     * @return country the country
     */
    public String getCountry()
    {
        return country;
    }
    
    /**
     * Sets code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * 
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets userName
     * 
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /**
     * Gets userName
     * 
     * @return userName the userName
     */
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * Sets userEmail
     * 
     * @param userEmail
     *            the userEmail to set
     */
    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }
    
    /**
     * Gets userEmail
     * 
     * @return userEmail the userEmail
     */
    public String getUserEmail()
    {
        return userEmail;
    }
    
    /**
     * Sets id
     * 
     * @param id
     *            the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }
    
    /**
     * Gets id
     * 
     * @return id the id
     */
    public long getId()
    {
        return id;
    }
    
    /**
     * Sets role
     * 
     * @param role
     *            the role to set
     */
    public void setRole(String role)
    {
        this.role = role;
    }
    
    /**
     * Gets role
     * 
     * @return role the role
     */
    public String getRole()
    {
        return role;
    }
}
