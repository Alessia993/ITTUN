/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.JoinType;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ReportingsLevel;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Reportings;



/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class ReportingsBean extends com.infostroy.paamns.persistence.beans.PersistenceEntityBean<Reportings>
{
	
	public List<Reportings> loadByLevelAndRoles(ReportingsLevel level, List<String> roleNames) throws PersistenceBeanException{
		
		Criteria criterion = PersistenceSessionManager
				.getBean()
				.getSession()
				.createCriteria(
						BeansFactory.getByType((Object) this).getClass())
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("level", level))
				.add(Restrictions.in("forRoles.code", roleNames))
				.createAlias("roles", "forRoles");
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.groupProperty("id"));
		criterion.setProjection(projList);
		
		List<Long> ids = criterion.list();
		
		if(ids!=null && !ids.isEmpty()){
			return PersistenceSessionManager
					.getBean()
					.getSession()
					.createCriteria(
							BeansFactory.getByType((Object) this).getClass())
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.in("id", ids)).list();
		}
		else{
			return new ArrayList<Reportings>();
		}
		
	}
	
}
