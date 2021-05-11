/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class SpecificGoalsBean extends
        LocalizableEnumerationBean<SpecificGoals>
{
    @SuppressWarnings("unchecked")
    public List<SpecificGoals> GetSpecificGoalsForAsse(String asse)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM SpecificGoals AS specGoals ");
        sb.append("where specGoals.anagraphicalData.asse = :asse");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("asse", asse);
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<SpecificGoals> GetThematicGoalsForSpecificGoals(String specificGoalsCode)
            throws PersistenceBeanException
    {
    	Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(SpecificGoals.class);
        criterion.add(Restrictions.like("code",	specificGoalsCode, MatchMode.START));
       
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<SpecificGoals> GetPrioritaryGoalsForThematicGoal(String thematicGoal)
            throws PersistenceBeanException
    {
    	Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(SpecificGoals.class);
        criterion.add(Restrictions.like("code",	thematicGoal, MatchMode.START));
       
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<SpecificGoals> GetAllSpecificGoals()
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM SpecificGoals AS specGoals ");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        return query.list();
    }

}
