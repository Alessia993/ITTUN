package com.infostroy.paamns.web.beans;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.web.beans.session.SessionBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PageBean
{
	protected transient final Log	log	= LogFactory.getLog(getClass());

	public PageBean()
	{

	}

	/**
	 * Gets the Session map to srote object over session of the application.
	 * 
	 * @return
	 */
	public Map<String, Object> getSession()
	{
		SessionManager sm = SessionManager.getInstance();

		if (sm != null)
		{
			SessionBean sessionBean = sm.getSessionBean();

			if (sessionBean != null)
			{
				if (sessionBean.Session != null)
				{
					return sessionBean.Session;
				}
				else
				{
					this.goTo(PagesTypes.LOGIN);
					return null;
				}
			}
			else
			{
				this.goTo(PagesTypes.LOGIN);
				return null;
			}
		}
		else
		{
			this.goTo(PagesTypes.LOGIN);
			return null;
		}
	}

	/**
	 * Gets the Session map to srote object over page of the application. This
	 * object will be killer when page changes.
	 * 
	 * @return
	 */
	public Map<String, Object> getViewState()
	{
		return SessionManager.getInstance().getSessionBean().getViewState();
	}

	/**
	 * Gets the current user that is loged in.
	 * 
	 * @return
	 */
	public Users getCurrentUser()
	{
		return SessionManager.getInstance().getSessionBean().getCurrentUser();
	}

	/**
	 * Gets the sessions class that is present through the application.
	 * 
	 * @return
	 */
	public SessionBean getSessionBean()
	{
		return SessionManager.getInstance().getSessionBean();
	}

	/**
	 * Gets the current http sessions.
	 * 
	 * @return
	 */
	public HttpSession getHttpSession()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		return session;
	}

	/**
	 * Gets the current request url.
	 * 
	 * @return
	 */
	public String getRequestUrl()
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) ctx
				.getExternalContext().getRequest();

		String path = request.getServletPath();
		return path;
	}

	/**
	 * Sends the redirect to another page.
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void sendRedirect(String url) throws IOException
	{
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		response.sendRedirect(url);
	}

	public FacesContext getContext()
	{
		return FacesContext.getCurrentInstance();
	}

	/**
	 * @param type
	 */
	public void goTo(PagesTypes type)
	{
		try
		{
			this.sendRedirect("../" + type.getPagesContext());
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void goTo(PagesTypes type, String anchor)
	{
		try
		{
			this.sendRedirect("../" + type.getPagesContext() + "#" + anchor);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	@SuppressWarnings(
	{ "deprecation", "rawtypes" })
	public boolean isPostback()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map requestScope = (Map) facesContext.getApplication()
				.createValueBinding("#{requestScope}").getValue(facesContext);
		boolean ispostback = ((Boolean) requestScope.get("ispostback"))
				.booleanValue();
		return ispostback;
	}

	public boolean getExpiredPassword()
	{
		if (this.getCurrentUser() != null)
		{
			if ((this.getCurrentUser().getPasswordExpired() || this
					.getCurrentUser().getShouldChangePassword())
					&& !this.getRequestUrl().contains(
							PagesTypes.CHANGEPASSWORD.getPagesContext()))
			{
				return true;
			}
		}
		return false;
	}

	public void checkPasswordExpiration()
	{
		if (this.getExpiredPassword())
		{
			goTo(PagesTypes.CHANGEPASSWORD);
		}
	}

	public UIComponent getComponentById(String id)
	{
		UIComponent component = this.getContext().getViewRoot()
				.findComponent(id);

		return component;
	}

	public void markInvalid(UIComponent component, String message)
	{
		com.infostroy.paamns.common.helpers.ValidatorHelper.markNotVAlid(
				component, message == null ? "" : Utils.getString(message),
				this.getContext(), null);
	}

	public void markValid(UIComponent component, String message)
	{
		com.infostroy.paamns.common.helpers.ValidatorHelper.markVAlid(
				component, this.getContext(), null);
	}

}
