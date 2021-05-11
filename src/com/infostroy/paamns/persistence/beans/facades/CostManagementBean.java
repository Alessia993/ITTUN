package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CostManagement;

public class CostManagementBean extends PersistenceEntityBean<CostManagement> {

	@SuppressWarnings("unchecked")
	public CostManagement loadByProjectUserCostItem(Long projectId, Long userId, Long costItemId)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		List<CostManagement> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostManagement.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("user.id", Long.valueOf(userId))).add(Restrictions.eq("costItem.id", costItemId))
				.list();

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CostManagement> lostByProject(Long projectId) throws HibernateException, PersistenceBeanException{
		List<CostManagement> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostManagement.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.list();
		return list;
	}

}
