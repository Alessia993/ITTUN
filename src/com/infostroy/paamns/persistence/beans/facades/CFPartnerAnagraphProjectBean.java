/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class CFPartnerAnagraphProjectBean extends
		PersistenceEntityBean<CFPartnerAnagraphProject>
{
	public CFPartnerAnagraphProject LoadByPartner(Long projectId, Long partnerId)
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CFPartnerAnagraphProject.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("project.id", projectId));
		criterion.add(Restrictions.eq("cfPartnerAnagraphs.id", partnerId));

		return (CFPartnerAnagraphProject) criterion.uniqueResult();
	}
	
	public List<CFPartnerAnagraphProject> LoadByProject(Long projectId)
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CFPartnerAnagraphProject.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("project.id", projectId));

		return criterion.list();
	}

	public CFPartnerAnagraphProject LoadByPartner(String projectId,
			String partnerId) throws PersistenceBeanException,
			NumberFormatException
	{
		if (projectId != null && partnerId != null && !projectId.isEmpty()
				&& !partnerId.isEmpty())
		{
			return this.LoadByPartner(Long.parseLong(projectId),
					Long.parseLong(partnerId));
		}
		else
		{
			return null;
		}
	}

	public CFPartnerAnagraphProject getByProgressiveIdAndProjectID(
			Long projectId, Long progressiveId) throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CFPartnerAnagraphProject.class);

		criterion.add(Restrictions.eq("project.id", projectId));
		criterion.add(Restrictions.eq("progressiveId", progressiveId));

		return (CFPartnerAnagraphProject) criterion.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphProject> GetCFAnagraphForProject(
			String projectId, CFAnagraphTypes type)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId AND cfpa.deleted = false ");

		if (type != null)
		{
			sb.append("AND cfpa.type.id = :type)");
		}

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));

		if (type != null)
		{
			query.setParameter("type", type.getCfType());
		}

		return query.list();
	}

	public long GetMaxIdCFAnagraphForProject(String projectId)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT MAX(cfpa.progressiveId) ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId AND cfpa.deleted = false ");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		Long id = (Long) query.uniqueResult();

		return id != null ? id + 1 : 1;
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphProject> GetCFAnagraphForProjectAndUser(
			String projectId, Long cfPartnerId)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId AND cfpa.deleted = false ");
		sb.append("AND cfpa.cfPartnerAnagraphs.id IN ");
		sb.append("(SELECT cfp FROM CFPartnerAnagraphs cfp WHERE cfp.deleted = false AND cfp.user.id = :userId)");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		if (cfPartnerId != null)
			query.setParameter("userId", cfPartnerId);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getNamingForCF(String projectId, Long cfPartnerId)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT cfpa.naming ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.deleted = false ");
		sb.append("AND cfpa.cfPartnerAnagraphs.id = :id ");
		sb.append("AND cfpa.naming != NULL ");
		if (projectId != null)
		{
			sb.append("AND cfpa.project.id = :projectId ");
		}

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("id", cfPartnerId);
		if (projectId != null)
		{
			query.setParameter("projectId", Long.parseLong(projectId));
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphProject> LoadByPartnerWithoutProject(
			Long partnerId) throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CFPartnerAnagraphProject.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("cfPartnerAnagraphs.id", partnerId));

		return criterion.list();
	}

	public List<SelectItem> getPartnersForCil(String projectId, Long id)
			throws HibernateException, PersistenceBeanException
	{
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(SelectItemHelper.getFirst());
		for (CFPartnerAnagraphProject item : BeansFactory
				.CFPartnerAnagraphProject().GetCFAnagraphForProject(projectId,
						null))
		{
			if (item.getCil() != null && item.getCil().getUser() != null
					&& item.getCil().getUser().getId().equals(id))
			{
				if (item.getCfPartnerAnagraphs().getUser() != null
						&& item.getType()
								.getId()
								.equals(CFAnagraphTypes.PartnerAnagraph
										.getCfType()))
				{
					StringBuilder sb = new StringBuilder();

					sb.append(item.getCfPartnerAnagraphs().getUser().getName());
					sb.append(" - ");
					sb.append(item.getCfPartnerAnagraphs().getUser()
							.getSurname());

					SelectItem selectItem = new SelectItem();
					selectItem.setLabel(sb.toString());
					selectItem.setValue(String.valueOf(item
							.getCfPartnerAnagraphs().getUser().getId()));
					list.add(selectItem);
				}
			}
		}

		if (list.size() == 1)
		{
			list.clear();
		}

		return list;
	}
	
	public List<SelectItem> getPartnersForCil2(String projectId, Long id)
			throws HibernateException, PersistenceBeanException
	{
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(SelectItemHelper.getFirst());
		for (CFPartnerAnagraphProject item : BeansFactory
				.CFPartnerAnagraphProject().GetCFAnagraphForProject(projectId,
						null))
		{
			if (item.getCil() != null && item.getCil().getUser() != null
					&& item.getCil().getUser().getId().equals(id))
			{
				if (item.getCfPartnerAnagraphs().getUser() != null
						)
				{
					StringBuilder sb = new StringBuilder();

					sb.append(item.getCfPartnerAnagraphs().getUser().getName());
					sb.append(" - ");
					sb.append(item.getCfPartnerAnagraphs().getUser()
							.getSurname());

					SelectItem selectItem = new SelectItem();
					selectItem.setLabel(sb.toString());
					selectItem.setValue(String.valueOf(item
							.getCfPartnerAnagraphs().getUser().getId()));
					list.add(selectItem);
				}
			}
		}

		if (list.size() == 1)
		{
			list.clear();
		}

		return list;
	}

}
