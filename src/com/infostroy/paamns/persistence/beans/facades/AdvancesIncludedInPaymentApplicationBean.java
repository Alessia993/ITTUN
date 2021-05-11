package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AdvancesIncludedInPaymentApplication;

public class AdvancesIncludedInPaymentApplicationBean extends PersistenceEntityBean<AdvancesIncludedInPaymentApplication>{

	@SuppressWarnings("unchecked")
	public List<AdvancesIncludedInPaymentApplication> loadByPaymentApplicationId(Long id) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdvancesIncludedInPaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("paymentApplication.id", id));
		return criterion.list();
	}
}
