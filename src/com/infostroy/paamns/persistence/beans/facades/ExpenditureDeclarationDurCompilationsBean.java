package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationDurCompilations;

public class ExpenditureDeclarationDurCompilationsBean extends PersistenceEntityBean<ExpenditureDeclarationDurCompilations>{

	
	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclarationDurCompilations> getAllByExpenditureDeclaratios(Long ependitureDeclarationId) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ExpenditureDeclarationDurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("expenditureDeclarationId",ependitureDeclarationId));
        return criterion.list();
	}
	
}
