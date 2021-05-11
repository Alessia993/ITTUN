package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;

public class RequestTypesBean extends LocalizableEnumerationBean<RequestTypes>{
	
	@SuppressWarnings("unchecked")
	public List<RequestTypes> selectAllExeptIntermediateFinalState() throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(RequestTypes.class);
        
        criterion.add(Restrictions.ne("code", "intermediate final"));
        
        return criterion.list();
	}

	 @SuppressWarnings("unchecked")
	    public List<RequestTypes> GetSelectable() throws PersistenceBeanException,
	            PersistenceBeanException
	    {
	        Criteria criterion = PersistenceSessionManager
	                .getBean()
	                .getSession()
	                .createCriteria(
	                        BeansFactory.getByType((Object) this).getClass())
	                .add(Restrictions.eq("isDefault", true));
	        List<RequestTypes> list = criterion.list();
	        return list;
	    }
	    
	    public RequestTypes GetRequestType(CountryTypes type)
	            throws PersistenceBeanException, PersistenceBeanException
	    {
	        Criteria criterion = PersistenceSessionManager.getBean().getSession()
	                .createCriteria(RequestTypes.class);
	        
	        criterion.add(Restrictions.eq("code", type.getCountry()));
	        
	        return (RequestTypes) criterion.uniqueResult();
	    }
	    
	    public RequestTypes GetRequestType(String code) throws PersistenceBeanException,
	            PersistenceBeanException
	    {
	        Criteria criterion = PersistenceSessionManager.getBean().getSession()
	                .createCriteria(RequestTypes.class);
	        
	        criterion.add(Restrictions.eq("code", code));
	        
	        return (RequestTypes) criterion.uniqueResult();
	    }
}
