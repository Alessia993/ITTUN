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
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStep;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProcedureTipologyStepBean extends
        PersistenceEntityBean<ProcedureTipologyStep>
{
    @SuppressWarnings("unchecked")
    public List<ProcedureTipologyStep> getByTypology(String tylogyId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(ProcedureTipologyStep.class)
                .add(Restrictions.eq("procedureTipology.id",
                        Long.parseLong(tylogyId)))
                .add(Restrictions.eq("deleted", false)).list();
    }
}
