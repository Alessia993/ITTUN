/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CoreIndicators;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class CoreIndicatorsBean extends
        LocalizableStaticEntityBean<CoreIndicators>
{
    @SuppressWarnings("unchecked")
    public List<CoreIndicators> GetByProject(String projectId)
            throws PersistenceBeanException, HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        List<CoreIndicators> list = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(CoreIndicators.class)
                .add(Restrictions.in("id",
                        BeansFactory.PhisicalInizializationToCoreIndicators()
                                .GetByProject(projectId))).list();
        return list;
    }
}
