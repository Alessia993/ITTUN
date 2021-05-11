/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class QsnIndicatorsBean extends
        LocalizableStaticEntityBean<QsnIndicators>
{
    @SuppressWarnings("unchecked")
    public List<QsnIndicators> GetByProject(String projectId)
            throws PersistenceBeanException, HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        Long[] ids = BeansFactory.PhisicalInizializationToQsnIndicators()
                .GetByProject(projectId);
        if (ids.length > 0)
        {
            List<QsnIndicators> list = PersistenceSessionManager.getBean()
                    .getSession().createCriteria(QsnIndicators.class)
                    .add(Restrictions.in("id", ids)).list();
            return list;
        }
        else
        {
            return new ArrayList<QsnIndicators>();
        }
    }
    
}
