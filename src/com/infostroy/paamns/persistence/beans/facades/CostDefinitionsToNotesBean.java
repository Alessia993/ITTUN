package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToNotes;

public class CostDefinitionsToNotesBean extends
        EntityBean<CostDefinitionsToNotes>
{
    @SuppressWarnings("unchecked")
    public List<CostDefinitionsToNotes> getByCostDefinition(Long id)
            throws PersistenceBeanException
    {
        Criteria crit = (Criteria) PersistenceSessionManager.getBean()
                .getSession().createCriteria(CostDefinitionsToNotes.class);
        
        crit.add(Restrictions.eq("costDefinition.id", id));
        return crit.list();
    }
}
