/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.persistence.beans.facades.statics.ProgramIndicatorsBean;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2015.
 *
 */
public class ProgramResultViewIndicatorsBean extends
		ProgramIndicatorsBean<ProgramIndicators>
{
	/**
	 * @return
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 * @throws IOException
	 */
	public List<ProgramIndicators> Load() throws PersistenceBeanException,
			PersistenceBeanException
	{
		return super.Load(ProgramIndicatorsType.ResultView);
	}

	/**
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws PersistenceBeanException
	 */
	public static List<ProgramIndicators> Load(
			Class<ProgramIndicators> entityClass) throws IOException,
			PersistenceBeanException
	{
		return Load(entityClass, ProgramIndicatorsType.ResultView);
	}

	/**
	 * 
	 * @param projectId
	 * @return
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 */
	public List<ProgramIndicators> GetByProject(String projectId)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{
		return GetByProject(projectId, ProgramIndicatorsType.ResultView);
	}
}
