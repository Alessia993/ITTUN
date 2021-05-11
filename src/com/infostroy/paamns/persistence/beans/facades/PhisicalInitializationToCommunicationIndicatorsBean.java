/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;



public class PhisicalInitializationToCommunicationIndicatorsBean extends
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
		return super.GetByProject(projectId, ProgramIndicatorsType.Communication);
	}

	public List<PhisicalInitializationToProgramIndicators> getByProject(
			String projectId) throws PersistenceBeanException
	{
		return super.getByProject(projectId, ProgramIndicatorsType.Communication);
	}

}