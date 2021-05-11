/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2009.
 *
 */
@Entity
@Table(name = "users")
public class Users extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    /**
     * 
     */
    @SuppressWarnings("unused")
    private static final long serialVersionUID = -8420963143214583722L;
    
    /**
     * Stores admin value of entity.
     */
    @Column(name = "admin")
    private Boolean           admin;
    
    @Column(name = "last_password_change")
    private Date              lastPasswordChange;
    
    @Column(name = "should_change_password")
    private Boolean           shouldChangePassword;
    
    /**
     * 
     */
    @Column(name = "name", nullable = false, length = 255)
    @NotNull(message = "name cannot be empty")
    private String            name;
    
    /**
     * 
     */
    @Column(name = "surname", nullable = false, length = 255)
    @NotNull(message = "surname cannot be empty")
    private String            surname;
    
    /**
     * 
     */
    @Column(name = "mail", nullable = false, length = 255)
    @NotNull(message = "mail cannot be empty")
    private String            mail;
    
    /**
     * 
     */
    @Column(name = "login", nullable = false, length = 255)
    @NotNull(message = "login cannot be empty")
    private String            login;
    
    @Column(name = "fiscal_code", length = 255)
    private String            fiscalCode;
    
    /**
     * 
     */
    @Column(name = "pwd", nullable = false, length = 255)
    @NotNull(message = "password cannot be empty")
    private String            password;
    
    @Column(name = "pwd2", length = 255)
    private String            password2;
    
    @Column
    private Boolean           active;
    
    @Column
    private Boolean           fictive;
    
    @OneToMany(mappedBy = "user")
    private List<UserRoles> userRoles;
    
    @ManyToOne
    @JoinColumn
    private Countries 		country;
    
    /**
     * @return
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * @return
     */
    public String getSurname()
    {
        return this.surname;
    }
    
    /**
     * @param surname
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    
    /**
     * @return
     */
    public String getMail()
    {
        return this.mail;
    }
    
    /**
     * @param mail
     */
    public void setMail(String mail)
    {
        this.mail = mail;
    }
    
    /**
     * @return
     */
    public String getLogin()
    {
        return this.login;
    }
    
    /**
     * @param login
     */
    public void setLogin(String login)
    {
        this.login = login;
    }
    
    /**
     * @return
     */
    public String getPassword()
    {
        return this.password;
    }
    
    /**
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Sets admin
     * @param admin the admin to set
     */
    public void setAdmin(Boolean admin)
    {
        this.admin = admin;
    }
    
    /**
     * Gets admin
     * @return admin the admin
     */
    public Boolean getAdmin()
    {
        return admin;
    }
    
    /**
     * Sets fiscalCode
     * @param fiscalCode the fiscalCode to set
     */
    public void setFiscalCode(String fiscalCode)
    {
        this.fiscalCode = fiscalCode;
    }
    
    /**
     * Gets fiscalCode
     * @return fiscalCode the fiscalCode
     */
    public String getFiscalCode()
    {
        return fiscalCode;
    }
    
    public List<UserRoles> getRoles()
    {
        try
        {
            return UserRolesBean.GetByUser(String.valueOf(this.getId()));
        }
        catch(PersistenceBeanException e)
        {
            
            return new ArrayList<UserRoles>();
        }
    }
    
    public String getRolesNames() throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<String>();
        
        for (UserRoles role : UserRolesBean.GetByUser(String.valueOf(this
                .getId())))
        {
			if (role != null && role.getRole() != null
					&& !list.contains(role.getRole().getCode()))
			{
				if (sb.length() == 0)
				{
					sb.append(role.getRole().getCode());
				}
				else
				{
					sb.append(", " + role.getRole().getCode());
				}

				list.add(role.getRole().getCode());
			}
        }
        return sb.toString();
    }
    
    public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets lastPasswordChange
	 * 
	 * @param lastPasswordChange
	 *            the lastPasswordChange to set
	 */
	public void setLastPasswordChange(Date lastPasswordChange) {
		this.lastPasswordChange = lastPasswordChange;
	}

	/**
	 * Gets lastPasswordChange
	 * 
	 * @return lastPasswordChange the lastPasswordChange
	 */
	public Date getLastPasswordChange() {
		return lastPasswordChange;
	}

	/**
	 * Sets shouldChangePassword
	 * 
	 * @param shouldChangePassword
	 *            the shouldChangePassword to set
	 */
	public void setShouldChangePassword(boolean shouldChangePassword) {
		this.shouldChangePassword = shouldChangePassword;
	}

	/**
	 * Gets shouldChangePassword
	 * 
	 * @return shouldChangePassword the shouldChangePassword
	 */
	public boolean getShouldChangePassword() {
		if (shouldChangePassword == null) {
			return false;
		}
		return shouldChangePassword;
	}

	protected transient final Log log = LogFactory.getLog(this.getClass());

	public boolean getPasswordExpired() {
		try {
			if (this.getLastPasswordChange() == null) {
				return true;
			}

			int days = Integer
					.parseInt(BeansFactory.ApplicationSettings().getValue(
							ApplicationSettingTypes.PASSWORD_EXPIRATION_TIME));

			Calendar c = Calendar.getInstance();
			c.setTime(this.getLastPasswordChange());
			c.add(Calendar.DATE, days);
			if (c.compareTo(Calendar.getInstance()) < 0) {
				return true;
			}

			return false;
		} catch (Exception e) {
			log.error(e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.getName() != null && this.getSurname() != null) {
			return String.format("%s %s", this.getName(), this.getSurname());
		} else if (this.getName() != null) {
			return this.getName();
		} else if (this.getSurname() != null) {
			return this.getSurname();
		}
		return "";
	}

	@Transient
	public String getProgressiveId() {
		try {
			List<CFPartnerAnagraphProject> list = BeansFactory
					.CFPartnerAnagraphProject().GetCFAnagraphForProjectAndUser(
							String.valueOf(SessionManager.getInstance()
									.getSessionBean().Session.get("project")),
							this.getId());
			if (list.size() > 0) {
				return list.get(0).getProgressiveId().toString();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Gets fictive
	 * @return fictive the fictive
	 */
	public Boolean getFictive()
	{
		return fictive;
	}

	/**
	 * Sets fictive
	 * @param fictive the fictive to set
	 */
	public void setFictive(Boolean fictive)
	{
		this.fictive = fictive;
	}

	public List<UserRoles> getUserRoles()
	{
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles)
	{
		this.userRoles = userRoles;
	}

	public Countries getCountry()
	{
		return country;
	}

	public void setCountry(Countries country)
	{
		this.country = country;
	}

	public String getPassword2()
	{
		return password2;
	}

	public void setPassword2(String password2)
	{
		this.password2 = password2;
	}
	
	public String getFullName()
	{
		return surname + " " + name;
	}
}
