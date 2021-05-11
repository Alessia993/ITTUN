/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class DurInfoToResponsiblePeopleBean extends
        PersistenceEntityBean<DurInfoToResponsiblePeople>
{
    @SuppressWarnings("unchecked")
    public List<DurInfoToResponsiblePeople> getByDurInfo(Long id)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        List<DurInfoToResponsiblePeople> list = PersistenceSessionManager
                .getBean().getSession()
                .createCriteria(DurInfoToResponsiblePeople.class)
                .add(Restrictions.eq("durComplitation.id", id))
                .add(Restrictions.eq("deleted", false)).list();
        return list;
    }
}
