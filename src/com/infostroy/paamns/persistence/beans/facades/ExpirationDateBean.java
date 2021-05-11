package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpirationDate;

public class ExpirationDateBean extends PersistenceEntityBean<ExpirationDate> 
{

	public static ExpirationDate getExpirationDate() throws PersistenceBeanException, PersistenceBeanException 
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ExpirationDate.class);
		return (ExpirationDate) criterion.uniqueResult();
	}
}
