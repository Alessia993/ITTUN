/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
public class CostItemsBean extends LocalizableEnumerationBean<CostItems> {
	
	public CostItems LoadByCode(String code) throws PersistenceBeanException {
		CostItems costItems = (CostItems) PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostItems.class).add(Restrictions.eq("code", code)).list().get(0);
		return costItems;
	}
}
