/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
/**
 * 
 * @author Kazako Anton InfoStroy Co., 2014.
 * 
 */
public class PartnersBudgetsPhasesBean extends PersistenceEntityBean<Phase>
{
	public List<Phase> loadByPartnerBudget(Long id) throws HibernateException,
			NumberFormatException, PersistenceBeanException
	{
		@SuppressWarnings("unchecked")
		List<Phase> items = (List<Phase>) PersistenceSessionManager.getBean()
				.getSession().createCriteria(Phase.class)
				.add(Restrictions.eq("partnersBudgets.id", id)).list();

		return items;
	}

}
