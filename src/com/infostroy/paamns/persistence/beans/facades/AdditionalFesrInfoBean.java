/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class AdditionalFesrInfoBean extends PersistenceEntityBean<AdditionalFesrInfo>
{
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrInfo> LoadByAxis(String axis) throws PersistenceBeanException{
		Criteria crit = PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("axis", axis));			
			return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrInfo> LoadByProject(String projectId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		return PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrInfo> LoadByCFPartnerAnagraphs(Long anagraphId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		return PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("fesrBf.id", anagraphId))
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrInfo> LoadByProjectCFPartnerAnagraphsAndTypeAndRole(String anagraphId, String transferType, String projectId, Long roleId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{	Criteria crit = PersistenceSessionManager.getBean().getSession()
			.createCriteria(AdditionalFesrInfo.class)
			.add(Restrictions.eq("deleted", false));
		if(anagraphId!=null && !anagraphId.isEmpty()){
			crit.add(Restrictions.eq("fesrBf.id", Long.parseLong(anagraphId)));
		}
		if(transferType!=null && !transferType.isEmpty()){
			crit.add(Restrictions.eq("transferTypeCode", Long.parseLong(transferType)));
		}
		if(roleId!=null){
			crit.add(Restrictions.eq("addUserRole.id", roleId));
		}
		if(projectId!=null && !projectId.isEmpty()){
			crit.add(Restrictions.eq("project.id", Long.parseLong(projectId)));
		}
		
		return crit.list();
	}
	
}
