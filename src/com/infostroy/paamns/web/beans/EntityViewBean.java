/**
 * 
 */
package com.infostroy.paamns.web.beans;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public abstract class EntityViewBean<T extends Entity> extends PageBean
{
	private List<T>	list	= new ArrayList<T>();

	/**
     * 
     */
	public EntityViewBean()
	{
		if (!this.isPostback())
		{
			try
			{
				this.checkPasswordExpiration();
				ActivityLog.getInstance().addActivity();
				this.getViewState().clear();
				this.Page_Load_Static();
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
			}
			catch (HibernateException e)
			{
				log.error(e);
			}
		}

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
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

	/**
	 * Use this methos to fill fields.
	 * 
	 * @throws PersistenceBeanException
	 */
	public abstract void Page_Load() throws NumberFormatException,
			HibernateException, PersistenceBeanException,
			PersistenceBeanException;

	/**
	 * Use this methos to fill static lists. It will calls only once on page
	 * load event.
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	public abstract void Page_Load_Static() throws HibernateException,
			PersistenceBeanException;

	/**
	 * Sets list
	 * 
	 * @param list
	 *            the list to set
	 */
	public void setList(List<T> list)
	{
		this.list = list;
	}

	/**
	 * Gets list
	 * 
	 * @return list the list
	 */
	public List<T> getList()
	{
		return list;
	}
}
