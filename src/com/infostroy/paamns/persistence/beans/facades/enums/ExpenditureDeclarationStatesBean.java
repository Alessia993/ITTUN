package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;

public class ExpenditureDeclarationStatesBean extends LocalizableEnumerationBean<ExpenditureDeclarationStates>{

	@SuppressWarnings("unchecked")
    public List<ExpenditureDeclarationStates> GetSelectable() throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass())
                .add(Restrictions.eq("isDefault", true));
        List<ExpenditureDeclarationStates> list = criterion.list();
        return list;
    }
    
    public ExpenditureDeclarationStates GetExpenditureDeclarationStatus(CountryTypes type)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ExpenditureDeclarationStates.class);
        
        criterion.add(Restrictions.eq("code", type.getCountry()));
        
        return (ExpenditureDeclarationStates) criterion.uniqueResult();
    }
    
    public ExpenditureDeclarationStates GetExpenditureDeclarationStatus(String code) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ExpenditureDeclarationStates.class);
        
        criterion.add(Restrictions.eq("code", code));
        
        return (ExpenditureDeclarationStates) criterion.uniqueResult();
    }
}
