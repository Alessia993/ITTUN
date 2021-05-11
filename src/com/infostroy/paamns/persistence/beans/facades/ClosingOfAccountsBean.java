package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ClosingAccounts;

public class ClosingOfAccountsBean extends PersistenceEntityBean<ClosingAccounts>{

	@SuppressWarnings("unchecked")
	public List<ClosingAccounts> LoadAll()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ClosingAccounts.class);
		criterion.add(Restrictions.eq("deleted", false));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public ClosingAccounts LoadById(Long id)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ClosingAccounts.class);
		criterion.add(Restrictions.eq("id", id));
		return (ClosingAccounts) criterion.uniqueResult();
	}
}
