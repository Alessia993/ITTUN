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
import com.infostroy.paamns.persistence.beans.entities.domain.FESRInfo;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class FESRInfoBean extends PersistenceEntityBean<FESRInfo>
{
    @SuppressWarnings("unchecked")
    public List<FESRInfo> LoadByDurReimbursment(String id)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        List<FESRInfo> list = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(FESRInfo.class)
                .add(Restrictions.eq("durReimbursements.id", Long.parseLong(id)))
                .add(Restrictions.eq("deleted", false)).list();
        
        return list;
    }
}
