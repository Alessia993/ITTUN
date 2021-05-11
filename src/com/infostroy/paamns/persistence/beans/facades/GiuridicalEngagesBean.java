/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
/*return PersistenceSessionManager.getBean().getSession()
        .createCriteria(AssignmentProcedures.class)
        .add(Restrictions.eq("deleted", false))
        .add(Restrictions.eq("project.id", Long.valueOf(projectId)))
        .list();*/

public class GiuridicalEngagesBean extends
        PersistenceEntityBean<GiuridicalEngages>
{
    @SuppressWarnings("unchecked")
    public List<GiuridicalEngages> getByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GiuridicalEngages.class);
        crit.add(Restrictions.eq("project.id", Long.valueOf(projectId))); //Long.parseLong(projectId)));
        crit.add(Restrictions.eq("deleted", false));
        
        return crit.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<GiuridicalEngages> getByProjectAndUserPartner(String projectId, Long userId)
            throws PersistenceBeanException
    {
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GiuridicalEngages.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.createAlias("createPartner.user", "partnerUser");
        crit.add(Restrictions.eq("partnerUser.id", userId));
        crit.add(Restrictions.eq("deleted", false));
        
        return crit.list();
    }
    
	public List<GiuridicalEngages> getByProjectAndUserPartnerAndFilters(
			String projectId, Long userId, Date dateFilter,
			String numberFilter, String importFilter) throws PersistenceBeanException
	{
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GiuridicalEngages.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.createAlias("createPartner.user", "partnerUser");
        crit.add(Restrictions.eq("partnerUser.id", userId));
        crit.add(Restrictions.eq("deleted", false));
        if(dateFilter!=null){
        	crit.add(Restrictions.le("date", DateTime.getDayEnd(dateFilter)));
        	crit.add(Restrictions.ge("date", DateTime.getDayStart(dateFilter)));
        }
        
        if(numberFilter!=null){
        	crit.add(Restrictions.like("number", numberFilter, MatchMode.START));
        }
        
		if (importFilter != null)
		{
			Double val = null;
			try
			{
				val = Double.parseDouble(importFilter);
				crit.add(Restrictions.eq("amount", val));

			}
			catch (NumberFormatException e)
			{

			}

		}
        
        
        return crit.list();
	}
	
	public List<GiuridicalEngages> getByProjectAndFilters(
			String projectId,  Date dateFilter,
			String numberFilter, String importFilter) throws PersistenceBeanException
	{
        Criteria crit = PersistenceSessionManager.getBean().getSession()
                .createCriteria(GiuridicalEngages.class);
        crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
        crit.createAlias("createPartner.user", "partnerUser");
        crit.add(Restrictions.eq("deleted", false));
        if(dateFilter!=null){
        	crit.add(Restrictions.le("date", DateTime.getDayEnd(dateFilter)));
        	crit.add(Restrictions.ge("date", DateTime.getDayStart(dateFilter)));
        }
        
        if(numberFilter!=null){
        	crit.add(Restrictions.like("number", numberFilter, MatchMode.START));
        }
        
		if (importFilter != null)
		{
			Double val = null;
			try
			{
				val = Double.parseDouble(importFilter);
				crit.add(Restrictions.eq("amount", val));

			}
			catch (NumberFormatException e)
			{

			}

		}
        
        
        return crit.list();
	}
}
