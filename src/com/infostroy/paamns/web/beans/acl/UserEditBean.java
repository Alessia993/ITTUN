package com.infostroy.paamns.web.beans.acl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.BooleanHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.NewUserMailSender;
import com.infostroy.paamns.common.helpers.mail.SimpleMailSender;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public final class UserEditBean extends EntityEditBean<Users>
{
	private static List<UserRolesWebBean>	list;

	private String							name;

	private boolean							active;

	private String							surname;

	private String							userName;

	private String							pwd;

	private String							password;

	private String							confirmPwd;

	private String							fiscalCode;

	private String							mail;

	private List<InfoObject>				mailToSend;

	private InfoObject						newUserMail;

	private List<SelectItem>				countries;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.base.EntityEditController#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException,
			PersistenceBeanException
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.base.EntityEditController#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		list = new ArrayList<UserRolesWebBean>();
		List<Roles> list = BeansFactory.Roles().Load();

		Iterator<Roles> rit = list.iterator();
		while (rit.hasNext())
		{
			Roles role = rit.next();

			UserRolesWebBean item = new UserRolesWebBean();

			item.setRole(role);
			UserEditBean.list.add(item);			
			if (role.getCode().equals("DAEC") || role.getCode().equals("SUPER_ADMIN"))
			{
				rit.remove();
			}
		}
		Iterator<UserRolesWebBean> urit = UserEditBean.list.iterator();
		while (urit.hasNext())
		{
			UserRolesWebBean item = urit.next();

			if (item.getRole().getCode().equals("DAEC") || item.getRole().getCode().equals("SUPER_ADMIN"))
			{
				urit.remove();
			}
		}

		if (this.getSession().get("user") != null)
		{
			List<UserRoles> userRoles = UserRolesBean.GetByUser(String
					.valueOf(this.getSession().get("user").toString()));
			for (UserRoles role : userRoles)
			{
				for (UserRolesWebBean item : UserEditBean.list)
				{
					if (item.getRole().equals(role.getRole()))
					{
						if (role.getWriteAccess() != null
								&& role.getWriteAccess())
						{
							item.setWriteAccess("true");
						}
						else
						{
							item.setWriteAccess("false");
						}
					}
				}
			}
		}

		if (this.getSession().get("user") != null)
		{
			Users user = BeansFactory.Users().Load(
					this.getSession().get("user").toString());
			this.setName(user.getName());
			this.setSurname(user.getSurname());
			this.setUserName(user.getLogin());
			this.setActive(user.getActive());
			if (user.getFiscalCode() != null)
			{
				this.setFiscalCode(user.getFiscalCode());
			}
			this.setMail(user.getMail());
			this.setPassword(user.getPassword());

		}
		countries=new ArrayList<SelectItem>();
		for(Countries c: Countries.values()){
			countries.add(new SelectItem(c.toString(),c.country));
		}
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.base.EntityEditController#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException,
			PersistenceBeanException
	{
		boolean reSendEmails = false;
		boolean changed = false;

		Users user = new Users();

		if (this.getSession().get("user") != null)
		{
			user = BeansFactory.Users().Load(
					this.getSession().get("user").toString());
		}
		else
		{
			user.setActive(true);
		}

		user.setFiscalCode(getFiscalCode());

		if (user.getMail() != null && !user.getMail().isEmpty()
				&& !user.getMail().equalsIgnoreCase(getMail()))
		{
			reSendEmails = true;
		}

		user.setMail(getMail());

		if (user.getLogin() != null && !user.getLogin().isEmpty()
				&& !user.getLogin().equalsIgnoreCase(getUserName()))
		{
			changed = true;
		}
		user.setLogin(getUserName());

		user.setName(getName());
		user.setSurname(getSurname());
		user.setAdmin(false);
		user.setActive(this.getActive());
		user.setCountry(BeansFactory.Countries().GetCountry(this.getCurrentCountry()));
		if (user.getPassword() != null
				&& !user.getPassword().isEmpty()
				&& getPwd() != null
				&& !getPwd().isEmpty()
				&& !user.getName().equalsIgnoreCase(
						MD5.encodeString(getPwd(), null)))
		{
			changed = true;
		}

		boolean isNew = user.getId() == null;

		if (getPwd() != null && !getPwd().isEmpty())
		{
			user.setPassword(MD5.encodeString(getPwd(), null));
			user.setPassword2(MD5.encodeString(user.getPassword(), null));
			user.setLastPasswordChange(new Date());
			user.setShouldChangePassword(true);
			changed = true;
		}

		BeansFactory.Users().SaveInTransaction(user);

		if (this.getSession().get("user") != null)
		{
			UserRolesBean.DeleteAll(user);
		}

		//checkForDoubleRoles(list);

		for (UserRolesWebBean item : list)
		{
			if (item.getWriteAccess() != null
					&& !item.getWriteAccess().equals("null")
					&& !item.getWriteAccess().isEmpty())
			{
				if (BooleanHelper.FromString(item.getWriteAccess()))
				{
					UserRoles role = new UserRoles();
					role.setUser(user);
					role.setRole(item.getRole());
					role.setWriteAccess(true);
					BeansFactory.UserRoles().SaveInTransaction(role);
				}
				else if (!BooleanHelper.FromString(item.getWriteAccess()))
				{
					UserRoles role = new UserRoles();
					role.setUser(user);
					role.setRole(item.getRole());
					role.setWriteAccess(false);
					BeansFactory.UserRoles().SaveInTransaction(role);
				}
			}
		}

		if (changed)
		{
			newUserMail = new InfoObject(user.getMail(), user.getName(),
					user.getSurname(), "", (getPwd() != null && !getPwd()
							.isEmpty()) ? getPwd() : "", user.getLogin(), "");
		}

		if (isNew
				&& (this.isInRole(user, UserRoleTypes.BP) || this.isInRole(
						user, UserRoleTypes.B)))
		{
			CFPartnerAnagraphs cfPartnetAnagraph = new CFPartnerAnagraphs();
			cfPartnetAnagraph.setUser(user);
			cfPartnetAnagraph.setCountry(user.getCountry());
			cfPartnetAnagraph.setPartita("");
			cfPartnetAnagraph.setName("");
			BeansFactory.CFPartnerAnagraphs().SaveInTransaction(
					cfPartnetAnagraph);
		}
		else if (BeansFactory.CFPartnerAnagraphs().GetByUser(user.getId()) == null
				&& (this.isInRole(user, UserRoleTypes.BP) || this.isInRole(
						user, UserRoleTypes.B)))
		{
			CFPartnerAnagraphs cfPartnetAnagraph = new CFPartnerAnagraphs();
			cfPartnetAnagraph.setUser(user);
			cfPartnetAnagraph.setCountry(user.getCountry());
			cfPartnetAnagraph.setPartita("");
			cfPartnetAnagraph.setName("");
			BeansFactory.CFPartnerAnagraphs().SaveInTransaction(
					cfPartnetAnagraph);
		}

		if (reSendEmails)
		{
			mailToSend = new ArrayList<InfoObject>();
			List<Mails> listMails = BeansFactory.Mails().Load(user);
			for (Mails mail : listMails)
			{
				mailToSend.add(new InfoObject(user.getMail(), user.getName(),
						user.getSurname(), mail.getMessage()));
				BeansFactory.Mails().DeleteInTransaction(mail);
			}
		}
	}

	/**
	 * @param list2
	 *//*
	private void checkForDoubleRoles(List<UserRolesWebBean> userRoles)
	{
		for (UserRolesWebBean item : userRoles)
		{
			if (item.getWriteAccess() != null
					&& !item.getWriteAccess().equals("null")
					&& !item.getWriteAccess().isEmpty())
			{
				if (item.getRole().getCode().equals(DoubleRoles.ANCM.getCode()))
				{
					if (BooleanHelper.FromString(item.getWriteAccess()))
					{
						changeUserRoleListWithRulesForANCM_W(userRoles);
					}
					else
					{
						changeUserRoleListWithRulesForANCM_R(userRoles);
					}
				}
			}
		}
	}

	*//**
	 * @param userRoles
	 *//*
	private void changeUserRoleListWithRulesForANCM_W(
			List<UserRolesWebBean> userRoles)
	{
		for (UserRolesWebBean item : userRoles)
		{
			if (item.getRole().getCode().equals("STC"))
			{
				item.setWriteAccess("true");
			}
		}
	}

	private void changeUserRoleListWithRulesForANCM_R(
			List<UserRolesWebBean> userRoles)
	{
		for (UserRolesWebBean item : userRoles)
		{
			if (item.getRole().getCode().equals("STC"))
			{
				item.setWriteAccess("false");

			}
		}
	}*/

	private enum DoubleRoles
	{
		ANCM("ANCM");
		private String	code;

		private DoubleRoles(String code)
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

	}

	private boolean isInRole(Users user, UserRoleTypes type)
	{
		for (UserRoles role : user.getRoles())
		{
			if (role.getRole().getCode().equalsIgnoreCase(type.getValue()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name.trim();
	}

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets surname
	 * 
	 * @param surname
	 *            the surname to set
	 */
	public void setSurname(String surname)
	{
		this.surname = surname.trim();
	}

	/**
	 * Gets surname
	 * 
	 * @return surname the surname
	 */
	public String getSurname()
	{
		return surname;
	}

	/**
	 * Sets userName
	 * 
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName.trim();
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
	 * Sets pwd
	 * 
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd)
	{
		this.pwd = pwd.trim();
	}

	/**
	 * Gets pwd
	 * 
	 * @return pwd the pwd
	 */
	public String getPwd()
	{
		return pwd;
	}

	/**
	 * Sets fiscalCode
	 * 
	 * @param fiscalCode
	 *            the fiscalCode to set
	 */
	public void setFiscalCode(String fiscalCode)
	{
		this.fiscalCode = fiscalCode.trim();
	}

	/**
	 * Gets fiscalCode
	 * 
	 * @return fiscalCode the fiscalCode
	 */
	public String getFiscalCode()
	{
		return fiscalCode;
	}

	/**
	 * Sets mail
	 * 
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(String mail)
	{
		this.mail = mail.trim();
	}

	/**
	 * Gets mail
	 * 
	 * @return mail the mail
	 */
	public String getMail()
	{
		return mail;
	}

	/**
	 * Sets confirmPwd
	 * 
	 * @param confirmPwd
	 *            the confirmPwd to set
	 */
	public void setConfirmPwd(String confirmPwd)
	{
		this.confirmPwd = confirmPwd.trim();
	}

	/**
	 * Gets confirmPwd
	 * 
	 * @return confirmPwd the confirmPwd
	 */
	public String getConfirmPwd()
	{
		return confirmPwd;
	}

	/**
	 * Sets password
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password.trim();
	}

	/**
	 * Gets password
	 * 
	 * @return password the password
	 */
	public String getPassword()
	{
		return password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.base.EntityEditController#GoBack()
	 */
	@Override
	public void GoBack()
	{
		goTo(PagesTypes.USERLIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.base.EntityEditController#AfterSave()
	 */
	@Override
	public void AfterSave()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();

			SimpleMailSender simpleMailSender = new SimpleMailSender(
					this.getMailToSend());
			mails.addAll(simpleMailSender
					.completeMails(PersistenceSessionManager.getBean()
							.getSession()));

			NewUserMailSender newUserMailSender = new NewUserMailSender(
					this.getNewUserMail());
			mails.addAll(newUserMailSender
					.completeMails(PersistenceSessionManager.getBean()
							.getSession()));

			if (mails != null && !mails.isEmpty())
			{
				for (Mails mail : mails)
				{
					BeansFactory.Mails().SaveInTransaction(mail);
				}

			}

		}
		catch (Exception e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
			}
		}
		this.GoBack();
	}

	/**
	 * Sets list
	 * 
	 * @param list
	 *            the list to set
	 */
	public void setList(List<UserRolesWebBean> list)
	{
		UserEditBean.list = list;
	}

	/**
	 * Gets list
	 * 
	 * @return list the list
	 */
	public List<UserRolesWebBean> getList()
	{
		return list;
	}

	/**
	 * Sets active
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * Gets active
	 * 
	 * @return active the active
	 */
	public boolean getActive()
	{
		return active;
	}

	/**
	 * Sets mailToSend
	 * 
	 * @param mailToSend
	 *            the mailToSend to set
	 */
	public void setMailToSend(List<InfoObject> mailToSend)
	{
		this.mailToSend = mailToSend;
	}

	/**
	 * Gets mailToSend
	 * 
	 * @return mailToSend the mailToSend
	 */
	public List<InfoObject> getMailToSend()
	{
		return mailToSend;
	}

	/**
	 * Sets newUserMail
	 * 
	 * @param newUserMail
	 *            the newUserMail to set
	 */
	public void setNewUserMail(InfoObject newUserMail)
	{
		this.newUserMail = newUserMail;
	}

	/**
	 * Gets newUserMail
	 * 
	 * @return newUserMail the newUserMail
	 */
	public InfoObject getNewUserMail()
	{
		return newUserMail;
	}
	
	private enum Countries{
		Italy("ITALY"), Tunisia("TUNISIA");
		
		private String country;
		
		/**
		 * Gets country
		 * @return country the country
		 */
		public String getCountry()
		{
			return country;
		}

		/**
		 * Sets country
		 * @param country the country to set
		 */
		public void setCountry(String country)
		{
			this.country = country;
		}

		private Countries(String country){
			this.setCountry(country);
		}
	}

	/**
	 * Gets currentCountry
	 * @return currentCountry the currentCountry
	 */
	public String getCurrentCountry()
	{
		if (this.getSession().get("currentCountry")!= null)
		{
			return String
					.valueOf(this.getSession().get("currentCountry"));
		}
		else
		{
			return Countries.Italy.toString();
		}
	}

	/**
	 * Sets currentCountry
	 * @param currentCountry the currentCountry to set
	 */
	public void setCurrentCountry(String currentCountry)
	{
		this.getSession().put("currentCountry", currentCountry);
		
	}

	/**
	 * Gets countries
	 * @return countries the countries
	 */
	public List<SelectItem> getCountries()
	{
		return countries;
	}

	/**
	 * Sets countries
	 * @param countries the countries to set
	 */
	public void setCountries(List<SelectItem> countries)
	{
		this.countries = countries;
	}
	
	public void countryChange(ValueChangeEvent event){
		this.setCurrentCountry(event.getNewValue().toString());
	}
	
	
	
}
