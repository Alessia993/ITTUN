package com.infostroy.paamns.web.beans.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.security.context.SecurityContextHolder;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.RoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.security.api.UserDetailsImpl;
import com.infostroy.paamns.common.security.beans.LoginBean;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;

/**
 * 
 * InfoStroy Co., 2009.
 * 
 */
public class SessionBean
{
	protected transient final Log	log			= LogFactory.getLog(getClass());

	public Map<String, Object>		ViewState	= new HashMap<String, Object>();

	public Map<String, Object>		Session		= new HashMap<String, Object>();

	public Users					currentUser;

	private List<RoleTypes>			listCurrentUserRoles;

	private Locale					curLocale;

	private boolean					isActualSate;

	private boolean					isActualSateAndAguRead;

	private boolean					isAguRead;

	private boolean					isProjectClosed;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getViewState()
	{
		String var = this.Session.get("project") != null ? String
				.valueOf(this.Session.get("project")) : "";
		if (this.Session.get(this.getRequestUrl() + var) == null)
		{
			this.Session.put(this.getRequestUrl() + var,
					new HashMap<String, Object>());
		}

		return (Map<String, Object>) this.Session.get(this.getRequestUrl()
				+ var);
	}

	private String getRequestUrl()
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) ctx
				.getExternalContext().getRequest();

		String path = request.getServletPath();
		return path;
	}

	private Map<String, Object> getSession()
	{
		try
		{
			return SessionManager.getInstance().getSessionBean().Session;
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	/**
	 * 
	 * @author Sergey Manoylo InfoStroy Co., 2009.
	 * 
	 */

	/**
	 * 
	 * @return
	 */
	public Users getCurrentUser()
	{
		try
		{
			if (this.currentUser == null)
			{
				if (!SecurityContextHolder.getContext().getAuthentication()
						.isAuthenticated())
				{

				}

				// /
				// SecurityContextHolder.getContext().getAuthentication().isAuthenticated()

				Object principal = SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

				// System.out.println("SecurityContextHolder.getInitializeCount() : "
				// + SecurityContextHolder.getInitializeCount());

				if (principal instanceof UserDetailsImpl)
				{
					UserDetailsImpl userDetails = (UserDetailsImpl) principal;
					try
					{
						this.currentUser = UsersBean.GetByLogin(userDetails
								.getUsername());
					}
					catch (PersistenceBeanException e)
					{
						log.error(e);
					}
				}
				else if (principal instanceof String)
				{
					// System.out.println("User has been loggined as :" +
					// principal);
					// log.warn("User has been loggined as :" + principal);
				}
				else
				{
					// System.out.println("User has been loggined as anonimous...");
					// log.warn("User has been loggined as anonimous...");
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return this.currentUser;
	}

	public boolean getProjectLock()
	{

		if (this.getSession().get("project") != null
				&& !String.valueOf(this.getSession().get("project")).isEmpty())
		{
			Projects p = null;
			try
			{
				p = BeansFactory.Projects().Load(
						this.getSession().get("project").toString());

				if (p == null || p.getLocked() == null)
				{
					return false;
				}
				else
				{
					return p.getLocked();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}

	public boolean getProjectWithTechAsistType()
	{
		if (this.getSession().get("project") != null
				&& !String.valueOf(this.getSession().get("project")).isEmpty())
		{
			Projects p = null;
			try
			{
				p = BeansFactory.Projects().Load(
						this.getSession().get("project").toString());

				if (p == null || p.getTypology() == null)
				{
					return false;
				}
				else
				{
					return (p.getTypology().getValue()
							.equals("Assistenza tecnica") || p.getTypology()
							.getValue().equals("Assistance technique"));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}
	
	public boolean getProjectWithStrategicType()
	{
		if (this.getSession().get("project") != null
				&& !String.valueOf(this.getSession().get("project")).isEmpty())
		{
			Projects p = null;
			try
			{
				p = BeansFactory.Projects().Load(
						this.getSession().get("project").toString());

				if (p == null || p.getTypology() == null)
				{
					return false;
				}
				else
				{
					return (p.getTypology().getValue()
							.equals("Progetto strategico") || p.getTypology()
							.getValue().equals("Projets stratégiques"));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}
	
	public boolean getProjectWithSimpliceType()
	{
		if (this.getSession().get("project") != null
				&& !String.valueOf(this.getSession().get("project")).isEmpty())
		{
			Projects p = null;
			try
			{
				p = BeansFactory.Projects().Load(
						this.getSession().get("project").toString());

				if (p == null || p.getTypology() == null)
				{
					return false;
				}
				else
				{
					return (p.getTypology().getValue()
							.equals("Progetto semplice") || p.getTypology()
							.getValue().equals("Simple projet"));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

		return false;
	}



	/**
	 * 
	 * @param user
	 */
	public void setCurrentUser(Users user)
	{
		this.currentUser = user;
	}

	/**
	 * Sets curLocale
	 * 
	 * @param curLocale
	 *            the curLocale to set
	 */
	public void setCurLocale(Locale curLocale)
	{
		this.curLocale = curLocale;
	}

	/**
	 * Gets curLocale
	 * 
	 * @return curLocale the curLocale
	 */
	public Locale getCurLocale()
	{
		return curLocale;
	}

	/**
	 * Sets listCurrentUserRoles
	 * 
	 * @param listCurrentUserRoles
	 *            the listCurrentUserRoles to set
	 */
	public void setListCurrentUserRoles(List<RoleTypes> listCurrentUserRoles)
	{
		this.listCurrentUserRoles = listCurrentUserRoles;
	}

	public int getListCurrentUserRolesSize()
	{
		return this.listCurrentUserRoles == null ? 0
				: this.listCurrentUserRoles.size();
	}

	/*
	 * public List<RoleTypes> getListCurrentUserRoles() { if
	 * (listCurrentUserRoles == null) { try { new LoginBean().doLogout(); }
	 * catch(ServletException e) { Log.writeToLog(e, SessionBean.class); }
	 * catch(IOException e) { Log.writeToLog(e, SessionBean.class); }
	 * 
	 * return new ArrayList<RoleTypes>(); } return listCurrentUserRoles; }
	 */

	public boolean isPartner()
	{
		if (this.getSession() != null
				&& this.getSession().get("project") != null)
		{
			try
			{
				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.GetByUser(this.getCurrentUser().getId());

				if (partner != null)
				{
					CFPartnerAnagraphProject partnerToProject = BeansFactory
							.CFPartnerAnagraphProject().LoadByPartner(
									String.valueOf(this.getSession().get(
											"project")),
									String.valueOf(partner.getId()));

					if (partnerToProject != null
							&& partnerToProject
									.getType()
									.getId()
									.equals(CFAnagraphTypes.PartnerAnagraph
											.getCfType()))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			catch (PersistenceBeanException e)
			{
				System.out.println(e);
			}
		}

		return isRole(RoleTypes.B_READ, RoleTypes.B_WRITE);

	}

	public boolean isCF()
	{
		if (this.getSession() != null
				&& this.getSession().get("project") != null)
		{
			try
			{
				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.GetByUser(this.getCurrentUser().getId());

				if (partner != null)
				{
					CFPartnerAnagraphProject partnerToProject = BeansFactory
							.CFPartnerAnagraphProject().LoadByPartner(
									String.valueOf(this.getSession().get(
											"project")),
									String.valueOf(partner.getId()));

					if (partnerToProject != null
							&& partnerToProject
									.getType()
									.getId()
									.equals(CFAnagraphTypes.CFAnagraph
											.getCfType()))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			catch (Exception e)
			{

			}
		}

		return isRole(RoleTypes.BP_READ, RoleTypes.BP_WRITE);
	}

	public boolean isOnlyCF()
	{
		return isRole(RoleTypes.BP_READ, RoleTypes.BP_WRITE);
	}

	public boolean isCFW()
	{
		if (this.getSession() != null
				&& this.getSession().get("project") != null)
		{
			try
			{
				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.GetByUser(this.getCurrentUser().getId());

				if (partner != null)
				{
					CFPartnerAnagraphProject partnerToProject = BeansFactory
							.CFPartnerAnagraphProject().LoadByPartner(
									String.valueOf(this.getSession().get(
											"project")),
									String.valueOf(partner.getId()));

					if (partnerToProject != null
							&& partnerToProject
									.getType()
									.getId()
									.equals(CFAnagraphTypes.CFAnagraph
											.getCfType()))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			catch (PersistenceBeanException e)
			{

			}
		}

		return isRole(RoleTypes.BP_WRITE);
	}

	public boolean isAGUASSE5() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		return isRole(RoleTypes.AGU_READ, RoleTypes.AGU_WRITE)
				&& this.getProject().getAsse() == 5;
	}

	public String getProjectId()
	{
		if (this.getSession().get("project") != null)
		{
			return String.valueOf(this.getSession().get("project"));
		}
		else
		{
			return null;
		}
	}

	public Projects getProject() throws NumberFormatException,
			HibernateException, PersistenceBeanException,
			PersistenceBeanException
	{
		if (this.getSession().get("PAAMSN_Session_project_storage") == null
				|| !this.getSession().get("stored_project_id").toString()
						.equals(this.getProjectId()))
		{
			if (this.getProjectId() == null)
			{
				return null;
			}

			this.getSession().put("PAAMSN_Session_project_storage",
					BeansFactory.Projects().Load(this.getProjectId()));
			this.getSession().put("stored_project_id", this.getProjectId());
		}

		return (Projects) this.getSession().get(
				"PAAMSN_Session_project_storage");
	}

	public boolean isAGU()
	{
		return isRole(RoleTypes.AGU_READ, RoleTypes.AGU_WRITE);
	}

	public boolean isAGUW()
	{
		return isRole(RoleTypes.AGU_WRITE);
	}

	public boolean isACU()
	{
		return isRole(RoleTypes.ACU_READ, RoleTypes.ACU_WRITE);
	}

	public boolean isACUW()
	{
		return isRole(RoleTypes.ACU_WRITE);
	}

	public boolean isACUM()
	{
		return isRole(RoleTypes.ACUM_READ, RoleTypes.ACUM_WRITE);
	}

	public boolean isACUMW()
	{
		return isRole(RoleTypes.ACUM_WRITE);
	}

	public boolean isAAU()
	{
		return isRole(RoleTypes.AAU_READ, RoleTypes.AAU_WRITE);
	}

	public boolean isAPU()
	{
		return isRole(RoleTypes.APU_READ, RoleTypes.APU_WRITE);
	}

	public boolean isCIL()
	{
		return isRole(RoleTypes.CIL_READ, RoleTypes.CIL_WRITE);
	}

	public boolean isDAEC()
	{
		return isRole(RoleTypes.DAEC_READ, RoleTypes.DAEC_WRITE);
	}

	public boolean isSTC()
	{
		return isRole(RoleTypes.STC_READ, RoleTypes.STC_WRITE);
	}

	public boolean isSTCW()
	{
		return isRole(RoleTypes.STC_WRITE);
	}
	
	public boolean isCFP()
	{
		return isRole(RoleTypes.CFP_READ, RoleTypes.CFP_WRITE);
	}

	public boolean isCFPW()
	{
		return isRole(RoleTypes.CFP_WRITE);
	}
	
	public boolean isSuperAdmin()
	{
		return isRole(RoleTypes.SUPER_ADMIN_READ, RoleTypes.SUPER_ADMIN_WRITE)
				&& !Boolean.TRUE.equals(this.getCurrentUser().getAdmin());
	}

	public boolean isSuperAdminW()
	{
		return isRole(RoleTypes.SUPER_ADMIN_WRITE)
				&& !Boolean.TRUE.equals(this.getCurrentUser().getAdmin());
	}
	
	public boolean isUC() 
	{
		return isRole(RoleTypes.UC_READ, RoleTypes.UC_WRITE);
	}
	
	public boolean isUCW() 
	{
		return isRole(RoleTypes.UC_WRITE);
	}
	
	public boolean isUCR() 
	{
		return isRole(RoleTypes.UC_READ);
	}
	
	public boolean isANCM()
	{
		return isRole(RoleTypes.ANCM_READ, RoleTypes.ANCM_WRITE);
	}
	
	public boolean isANCMW()
	{
		return isRole(RoleTypes.ANCM_WRITE);
	}
	
	public boolean isANCMR()
	{
		return isRole(RoleTypes.ANCM_READ);
	}


	public boolean isRole(RoleTypes typeRead, RoleTypes typeWrite)
	{
		if (listCurrentUserRoles == null)
		{
			try
			{
				(new LoginBean()).doLogout();
			}
			catch (ServletException e)
			{
				log.error(e);
			}
			catch (IOException e)
			{
				log.error(e);
			}
		}
		if (this.listCurrentUserRoles != null
				&& (this.listCurrentUserRoles.contains(typeRead) || this.listCurrentUserRoles
						.contains(typeWrite)))
		{
			return true;
		}

		return false;
	}

	private boolean isRole(RoleTypes type)
	{
		if (listCurrentUserRoles == null)
		{
			try
			{
				(new LoginBean()).doLogout();
			}
			catch (ServletException e)
			{
				log.error(e);
			}
			catch (IOException e)
			{
				log.error(e);
			}
		}
		if (this.listCurrentUserRoles != null
				&& this.listCurrentUserRoles.contains(type))
		{
			return true;
		}

		return false;
	}

	/**
	 * Sets isActualSate
	 * 
	 * @param isActualSate
	 *            the isActualSate to set
	 */
	public void setIsActualSate(boolean isActualSate)
	{
		this.isActualSate = isActualSate;
	}

	/**
	 * Gets isActualSate
	 * 
	 * @return isActualSate the isActualSate
	 */
	public boolean getIsActualSate()
	{
		return isActualSate;
	}

	/**
	 * Sets isActualSateAndAguRead
	 * 
	 * @param isActualSateAndAguRead
	 *            the isActualSateAndAguRead to set
	 */
	public void setIsActualSateAndAguRead(boolean isActualSateAndAguRead)
	{
		this.isActualSateAndAguRead = isActualSateAndAguRead;
	}

	/**
	 * Gets isActualSateAndAguRead
	 * 
	 * @return isActualSateAndAguRead the isActualSateAndAguRead
	 */
	public boolean getIsActualSateAndAguRead()
	{
		return isActualSateAndAguRead;
	}

	/**
	 * Sets isAguRead
	 * 
	 * @param isAguRead
	 *            the isAguRead to set
	 */
	public void setIsAguRead(boolean isAguRead)
	{
		this.isAguRead = isAguRead;
	}

	/**
	 * Gets isAguRead
	 * 
	 * @return isAguRead the isAguRead
	 */
	public boolean getIsAguRead()
	{
		return isAguRead;
	}

	/**
	 * Sets isProjectClosed
	 * 
	 * @param isProjectClosed
	 *            the isProjectClosed to set
	 */
	public void setIsProjectClosed(boolean isProjectClosed)
	{
		this.isProjectClosed = isProjectClosed;
	}

	/**
	 * Gets isProjectClosed
	 * 
	 * @return isProjectClosed the isProjectClosed
	 */
	public boolean getIsProjectClosed()
	{
		return isProjectClosed;
	}
}
