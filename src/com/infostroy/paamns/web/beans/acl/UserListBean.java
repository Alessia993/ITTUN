package com.infostroy.paamns.web.beans.acl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public final class UserListBean extends EntityListBean<Users>
{
	private String	deleteMessage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.base.EntityListController#Page_Load()
	 */
	@Override
	public void Page_Load() throws PersistenceBeanException,
			PersistenceBeanException
	{
		// setList(BeansFactory.Users().Load());
		/*
		 * if (filtredName == null) { setFiltredName(""); } if (filtredSurname
		 * == null) { setFiltredSurname(""); }
		 */
		doSearch();
	}

	public void doSearch()
	{
		try
		{
			if (this.getFiltredSurname() != null)
			{
				setList(BeansFactory.Users().loadByCriteria(
						new Criterion[]
						{ Restrictions.like("surname", this.getFiltredSurname()
								+ "%") }, new Order[]
						{ Order.asc("surname") }));
			}
			else
			{
				setList(BeansFactory.Users().loadByCriteria(new Criterion[]
				{}, new Order[]
				{ Order.asc("surname") }));
			}
		}
		catch (PersistenceBeanException e)
		{
		}
		ReRenderScroll();
	}

	public void editUser()
	{
		try
		{
			if (BeansFactory.Users().Load(this.getEntityEditId()) != null)
			{
				this.getSession().put("user", this.getEntityEditId());

				this.goTo(PagesTypes.USEREDIT);
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
	}

	public void deleteUser()
	{
		try
		{
			if (BeansFactory.Users().Load(this.getEntityDeleteId()) != null)
			{
				Long userId = getEntityDeleteId();
				BeansFactory.Users().Delete(userId);
				ControllerUserAnagraph item = BeansFactory
						.ControllerUserAnagraph().GetByUser(userId);
				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.GetByUser(userId);
				if (partner != null)
				{
					List<CFPartnerAnagraphProject> projects = BeansFactory
							.CFPartnerAnagraphProject()
							.LoadByPartnerWithoutProject(partner.getId());
					if (projects != null)
					{
						for (CFPartnerAnagraphProject cfPartnerAnagraphProject : projects)
						{

							CFPartnerAnagraphs.DeleteAllCFAnagraphs(partner
									.getId(), cfPartnerAnagraphProject
									.getProject().getId());
						}
					}

					BeansFactory.CFPartnerAnagraphs().Delete(partner);
				}

				if (item != null)
				{
					BeansFactory.ControllerUserAnagraph().Delete(item);
				}

				this.Page_Load();
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
	}

	public void addUser()
	{
		this.getSession().put("user", null);
		this.goTo(PagesTypes.USEREDIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets entityDeleteId
	 * 
	 * @param entityDeleteId
	 *            the entityDeleteId to set
	 */
	public void setEntityDeleteId(Long entityDeleteId)
	{
		this.getViewState().put("entityDeleteId", entityDeleteId);
		List<UserRoles> userRolesList = null;
		try
		{
			userRolesList = UserRolesBean.GetByUser(String
					.valueOf(entityDeleteId));
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
		for (UserRoles item : userRolesList)
		{
			if (item.getRole().getCode().equals(UserRoleTypes.BP.getValue())
					|| item.getRole().getCode()
							.equals(UserRoleTypes.B.getValue())
					|| item.getRole().getCode()
							.equals(UserRoleTypes.AGU.getValue()))
			{
				this.setDeleteMessage(com.infostroy.paamns.common.utils.Utils
						.getString("partnerBudgetDelete"));
				return;
			}
		}

		this.setDeleteMessage(com.infostroy.paamns.common.utils.Utils
				.getString("delete"));
	}

	/**
	 * Sets deleteMessage
	 * 
	 * @param deleteMessage
	 *            the deleteMessage to set
	 */
	public void setDeleteMessage(String deleteMessage)
	{
		this.deleteMessage = deleteMessage;
	}

	/**
	 * Gets deleteMessage
	 * 
	 * @return deleteMessage the deleteMessage
	 */
	public String getDeleteMessage()
	{
		return deleteMessage;
	}

	public String getFiltredSurname()
	{
		if (this.getSession().get("filtredSurname") != null)
		{
			return String.valueOf(this.getSession().get("filtredSurname"));
		}
		else
		{
			return null;
		}
	}

	public void setFiltredSurname(String filtredSurname)
	{

		this.getSession().put("filtredSurname", filtredSurname);
		try
		{
			Page_Load();
		}
		catch (PersistenceBeanException e)
		{
		}
	}

}
