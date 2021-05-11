/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Asses;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.AnagraphicalData;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class AnagraphicalDataBean extends
		LocalizableStaticEntityBean<AnagraphicalData>
{
	@SuppressWarnings("unchecked")
	public List<AnagraphicalData> GetAnagraphicalDataForAsse(Asses asse)
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(AnagraphicalData.class);

		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("asse", asse));

		return criterion.list();
	}

	public AnagraphicalData GetAnagraphicalDataForTotalAsse()
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(AnagraphicalData.class);

		return (AnagraphicalData) criterion
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("asse", "Total")).uniqueResult();
	}

}
