/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class BudgetInputSourceDividedBean extends
        PersistenceEntityBean<BudgetInputSourceDivided>
{
    /**
	 * Stores NULL value of entity.
	 */
	private static final String NULL = "null";

	@SuppressWarnings("unchecked")
    public static List<BudgetInputSourceDivided> GetByProject(String projectId)
            throws PersistenceBeanException
	{
		List<BudgetInputSourceDivided> list = new ArrayList<BudgetInputSourceDivided>();

		if (projectId != null && !projectId.isEmpty()
				&& !NULL.equalsIgnoreCase(projectId))
		{
			list = PersistenceSessionManager.getBean().getSession()
					.createCriteria(BudgetInputSourceDivided.class)
					.add(Restrictions.eq("deleted", false)).add(Restrictions
							.like("project.id", Long.valueOf(projectId)))
					.list();
		}

		return list;
	}
    
    @SuppressWarnings("unchecked")
    public static List<BudgetInputSourceDivided> GetByProject(Long projectId)
            throws PersistenceBeanException
	{
		List<BudgetInputSourceDivided> list = new ArrayList<BudgetInputSourceDivided>();

		if (projectId != null)
		{
			list = PersistenceSessionManager.getBean().getSession()
					.createCriteria(BudgetInputSourceDivided.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.like("project.id", projectId)).list();
		}

		return list;
	}
}
