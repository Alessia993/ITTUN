/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;

/**
 * 
 * @author Sergii Kravchenko InfoStroy Co., 2013.
 * 
 */
public class SuperAdminChangesBean extends
		PersistenceEntityBean<SuperAdminChange>
{

	@SuppressWarnings("unchecked")
	public List<SuperAdminChange> loadByCriteria(Criterion[] criterions,
			Order[] orderList) throws PersistenceBeanException
	{
		Criteria criteria = PersistenceSessionManager.getBean().getSession()
				.createCriteria(SuperAdminChange.class);
		if (criterions != null)
		{
			for (Criterion crit : criterions)
			{
				criteria.add(crit);
			}
		}

		if (orderList != null)
		{
			for (Order o : orderList)
			{
				criteria.addOrder(o);
			}
		}

		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SuperAdminChange> Load() throws PersistenceBeanException,
			PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(SuperAdminChange.class)
				.addOrder(Order.asc("id"));
		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<SuperAdminChange> loadByUserId(Long id)
			throws PersistenceBeanException
	{
		Criteria criteria = PersistenceSessionManager.getBean().getSession()
				.createCriteria(SuperAdminChange.class);

		criteria.add(Restrictions.eq("user.id", id));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<SuperAdminChange> loadByProjectId(Long id)
			throws PersistenceBeanException
	{
		Criteria criteria = PersistenceSessionManager.getBean().getSession()
				.createCriteria(SuperAdminChange.class);

		criteria.add(Restrictions.eq("project.id", id)).addOrder(Order.desc("createDate"));

		return criteria.list();
	}

}
