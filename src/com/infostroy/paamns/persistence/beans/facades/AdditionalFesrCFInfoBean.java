package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrCFInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;

public class AdditionalFesrCFInfoBean extends PersistenceEntityBean<AdditionalFesrCFInfo>{

	@SuppressWarnings("unchecked")
	public List<AdditionalFesrCFInfo> LoadByAxis(String axis) throws PersistenceBeanException{
		Criteria crit = PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrCFInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("axis", axis));			
			return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrCFInfo> LoadByProject(String projectId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		return PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrCFInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrCFInfo> LoadByCFPartnerAnagraphs(Long anagraphId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		return PersistenceSessionManager.getBean().getSession()
				.createCriteria(AdditionalFesrCFInfo.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("fesrBf.id", anagraphId))
				.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdditionalFesrCFInfo> LoadByProjectCFPartnerAnagraphsAndTypeAndRole(String anagraphId, String transferType, String projectId, Long roleId, Long userId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{	Criteria crit = PersistenceSessionManager.getBean().getSession()
			.createCriteria(AdditionalFesrCFInfo.class)
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
		if(userId!=null){
			crit.add(Restrictions.eq("user.id", userId));
		}
		
		return crit.list();
	}
	
}
