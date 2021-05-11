/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;

/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class CostAsse3Bean extends EntityBean<CostAsse3>
{
	@SuppressWarnings("unchecked")
	public List<CostAsse3> GetByPhase(PhaseAsse3Types phase)
			throws PersistenceBeanException
	{
		List<CostAsse3> list = PersistenceSessionManager.getBean()
				.getSession().createCriteria(CostAsse3.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("phase", phase))
				.addOrder(Order.asc("id")).list();

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public CostAsse3 GetByPhaseAndValue(PhaseAsse3Types phase, String value)
			throws PersistenceBeanException
	{
		return (CostAsse3)PersistenceSessionManager.getBean()
				.getSession().createCriteria(CostAsse3.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("phase", phase))
				.add(Restrictions.eq("value", value))
				.addOrder(Order.asc("id")).uniqueResult();
	}
}
