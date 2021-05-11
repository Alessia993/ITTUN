/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class CountriesBean extends LocalizableEnumerationBean<Countries>
{
    @SuppressWarnings("unchecked")
    public List<Countries> GetSelectable() throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass())
                .add(Restrictions.eq("isDefault", true));
        List<Countries> list = criterion.list();
        return list;
    }
    
    public Countries GetCountry(CountryTypes type)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Countries.class);
        
        criterion.add(Restrictions.eq("code", type.getCountry()));
        
        return (Countries) criterion.uniqueResult();
    }
    
    public Countries GetCountry(String code) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Countries.class);
        
        criterion.add(Restrictions.eq("code", code));
        
        return (Countries) criterion.uniqueResult();
    }
}
