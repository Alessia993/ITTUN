/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class RegionsBean extends LocalizableEnumerationBean<Regions>
{
    /**
     * @author Nickolay Sumyatin
     * @param countryId
     * @return
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public List<Regions> LoadByCountry(String countryId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager.getBean().getSession()
                .createCriteria(Regions.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("country.id", Long.valueOf(countryId)))
                .list();
    }
}
