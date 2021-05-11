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
import com.infostroy.paamns.persistence.beans.entities.domain.statics.EmploymentIndicators;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class EmploymentIndicatorsBean extends
        LocalizableStaticEntityBean<EmploymentIndicators>
{
    @SuppressWarnings("unchecked")
    public List<EmploymentIndicators> GetByProject(String projectId)
            throws PersistenceBeanException, HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        Long[] ids = BeansFactory
                .PhisicalInizializationToEmploymentIndicators().GetByProject(
                        projectId);
        if (ids.length > 0)
        {
            List<EmploymentIndicators> list = PersistenceSessionManager
                    .getBean().getSession()
                    .createCriteria(EmploymentIndicators.class)
                    .add(Restrictions.in("id", ids)).list();
            return list;
        }
        else
        {
            return new ArrayList<EmploymentIndicators>();
        }
    }
}
