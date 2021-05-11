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
import com.infostroy.paamns.persistence.beans.entities.domain.RotationFoundInfo;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class RotationFoundInfoBean extends
        PersistenceEntityBean<RotationFoundInfo>
{
    @SuppressWarnings("unchecked")
    public List<RotationFoundInfo> LoadByDurReimbursment(String id)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        List<RotationFoundInfo> list = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(RotationFoundInfo.class)
                .add(Restrictions.eq("durReimbursements.id", Long.parseLong(id)))
                .add(Restrictions.eq("deleted", false)).list();
        
        return list;
    }
}
