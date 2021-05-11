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
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureNotes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProcedureNotesBean extends PersistenceEntityBean<ProcedureNotes>
{
    @SuppressWarnings("unchecked")
    public List<ProcedureNotes> LoadByProcedure(String procedureId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(ProcedureNotes.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("procedure.id", Long.valueOf(procedureId)))
                .list();
    }
}
