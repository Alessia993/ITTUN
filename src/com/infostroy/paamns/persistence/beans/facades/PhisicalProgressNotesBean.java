package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressNotes;

public class PhisicalProgressNotesBean extends PersistenceEntityBean<PhisicalProgressNotes>{

	
	@SuppressWarnings("unchecked")
    public List<PhisicalProgressNotes> LoadByProjectIndicator(String projectIndicatorId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(PhisicalProgressNotes.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("projectIndicators.id", Long.valueOf(projectIndicatorId)))
                .list();
    }
}
