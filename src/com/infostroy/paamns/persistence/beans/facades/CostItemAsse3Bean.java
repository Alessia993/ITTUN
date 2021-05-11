/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3;



/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class CostItemAsse3Bean extends EntityBean<CostItemAsse3>
{
	
	@SuppressWarnings("unchecked")
	public List<CostItemAsse3> GetByProjectAndItem(String projectId, Long costPhaseItemId)
			throws PersistenceBeanException
	{
		List<CostItemAsse3> list = PersistenceSessionManager.getBean()
				.getSession().createCriteria(CostItemAsse3.class).createAlias("partnersBudgets", "budget").createAlias("budget.project", "pr")
				.add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("pr.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("costPhase.id", costPhaseItemId))
				.addOrder(Order.asc("id")).list();
		

		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<CostItemAsse3> GetByPartnerBudgetAndItem(String projectId, Long costPhaseItemId)
			throws PersistenceBeanException
	{
		List<CostItemAsse3> list = PersistenceSessionManager.getBean()
				.getSession().createCriteria(CostItemAsse3.class)//.createAlias("partnersBudgets", "budget").createAlias("budget.project", "pr")
				.add(Restrictions.eq("deleted", false))
				/*.addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("partnersBudgets.project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("costPhase.id", costPhaseItemId))
				.addOrder(Order.asc("id"))*/.list();
		

		return list;
	}
	
}
