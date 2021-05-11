/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramResultIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 *
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2010.
 *
 */
public class PhisicalInitializationToProgramResultIndicatorsBean extends
		PhisicalInitializationToProgramIndicatorsBean
{
	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws PersistenceBeanException
	 */
	protected transient final Log	log	= LogFactory.getLog(getClass());

	public Long[] GetByProject(String projectId)
			throws PersistenceBeanException
	{
		return super.GetByProject(projectId, ProgramIndicatorsType.Result);
	}

	public List<PhisicalInitializationToProgramIndicators> getByProject(
			String projectId) throws PersistenceBeanException
	{
		return super.getByProject(projectId, ProgramIndicatorsType.Result);
	}

	public void DeleteAll(String projectId)
	{
		Transaction tr = null;
		Query userUpdateQuery;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			StringBuilder sb = new StringBuilder();
			sb.append("Delete from ");
			sb.append(PhisicalInitializationToProgramIndicators.class.getSimpleName());

			sb.append(" where phisicalInitialization.id = :phisId");
			userUpdateQuery = PersistenceSessionManager.getBean().getSession()
					.createQuery(sb.toString());
			PhisicalInitializations item = BeansFactory
					.PhisicalInizializations().getByProject(projectId);
			if (item != null)
			{
				userUpdateQuery.setLong("phisId", item.getId());
				userUpdateQuery.executeUpdate();
			}
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}

			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}

			log.error(e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
			}
		}
	}
}
