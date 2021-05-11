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
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ZonesBean extends LocalizableEnumerationBean<Zones>
{
    
    /**
     * @author Nickolay Sumyatin
     * @param regionId
     * @return
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public List<Zones> LoadByDepartment(String departmetnId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(Zones.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("department.id",
                        Long.valueOf(departmetnId))).list();
    }
}
