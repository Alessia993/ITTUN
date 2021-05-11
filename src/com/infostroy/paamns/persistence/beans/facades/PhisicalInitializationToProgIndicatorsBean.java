/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramResultIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2014.
 *
 */
public class PhisicalInitializationToProgIndicatorsBean extends
		PhisicalInitializationToProgramIndicatorsBean
{
	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws PersistenceBeanException
	 */
	public Long[] GetByProject(String projectId)
			throws PersistenceBeanException
	{
		return super.GetByProject(projectId, ProgramIndicatorsType.Prog);
	}

	public List<PhisicalInitializationToProgramIndicators> getByProject(
			String projectId) throws PersistenceBeanException
	{
		return super.getByProject(projectId, ProgramIndicatorsType.Prog);
	}
}
