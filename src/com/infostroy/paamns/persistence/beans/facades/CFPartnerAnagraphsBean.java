/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CFPartnerAnagraphsBean extends
		PersistenceEntityBean<CFPartnerAnagraphs>
{
	/**
	 * Stores NULL value of entity.
	 */
	private static final String NULL = "null";

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> GetCFAnagraphForProject(String projectId,
			CFAnagraphTypes type) throws PersistenceBeanException,
			HibernateException, PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa.cfPartnerAnagraphs ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId AND cfpa.deleted = false ");
		sb.append("AND cfpa.removedFromProject = false AND ( cfpa.cfPartnerAnagraphs.user.fictive = false OR cfpa.cfPartnerAnagraphs.user.fictive IS NULL) ");

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

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> LoadAllSortedByUsername()
			throws PersistenceBeanException, PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager
				.getBean()
				.getSession()
				.createCriteria(
						BeansFactory.getByType((Object) this).getClass())
				.createAlias("user", "u")
				.add(Restrictions.eq("deleted", false))
				.addOrder(Order.asc("u.name"));
		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> GetCFAnagraphForProjectAndUser(
			String projectId, Long cfPartnerId)
			throws PersistenceBeanException, HibernateException,
			PersistenceBeanException
	{				
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa.cfPartnerAnagraphs ");
		sb.append("FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId AND cfpa.deleted = false ");
		sb.append("AND cfpa.cfPartnerAnagraphs.id IN ");
		sb.append("(SELECT cfp FROM CFPartnerAnagraphs cfp WHERE cfp.deleted = false AND cfp.user.id = :userId)");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		query.setParameter("userId", cfPartnerId);

		return query.list();
	}

	/**
	 * 
	 * @param projectId
	 * @return CFPartnerAnagraphs entity for project
	 * @throws HibernateException
	 * @throws NumberFormatException
	 * @throws PersistenceBeanException
	 */
	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> LoadByProject(String projectId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		if (projectId != null && !projectId.isEmpty()
				&& !NULL.equalsIgnoreCase(projectId))
		{
			StringBuilder sb = new StringBuilder();
			sb.append(
					"SELECT cfpa.cfPartnerAnagraphs FROM CFPartnerAnagraphProject cfpa ");
			sb.append("WHERE cfpa.project.id = :projectId ");
			sb.append("AND cfpa.deleted = false ");
			sb.append("AND cfpa.removedFromProject = false ");

			Query query = PersistenceSessionManager.getBean().getSession()
					.createQuery(sb.toString());
			query.setParameter("projectId", Long.valueOf(projectId));

			return query.list();
		}
		else
		{
			return new ArrayList<CFPartnerAnagraphs>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> GetByPartnerSurname(String partnerSurname)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa FROM CFPartnerAnagraphs cfpa ");
		sb.append("WHERE cfpa.user.surname = :partnerSurname");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("partnerSurname", partnerSurname);

		return query.list();
	}

	public CFPartnerAnagraphs getFictivePartnerByProjectAndCountry(
			Long projectId, String country) throws HibernateException,
			NumberFormatException, PersistenceBeanException
	{
		return (CFPartnerAnagraphs)PersistenceSessionManager
				.getBean().getSession().createQuery(
						"Select cfPartnerAnagraphs as apa from CFPartnerAnagraphProject ap "
								+ "where ap.cfPartnerAnagraphs.country.code=:COUNTRY "
								+ "and ap.cfPartnerAnagraphs.user.fictive = true and ap.deleted = false "
								+ "and ap.cfPartnerAnagraphs.deleted=false and ap.project.id=:PROJECT_ID")
				.setParameter("PROJECT_ID", projectId)
				.setParameter("COUNTRY", country).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> GetByDAEC(String projectId, Long daecId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa.cfPartnerAnagraphs FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId ");
		sb.append("AND cfpa.deleted = false ");
		sb.append("AND cfpa.daec.id = :daecId");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		query.setParameter("daecId", daecId);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> GetByCIL(String projectId, Long cilId)
			throws HibernateException, NumberFormatException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpa.cfPartnerAnagraphs FROM CFPartnerAnagraphProject cfpa ");
		sb.append("WHERE cfpa.project.id = :projectId ");
		sb.append("AND cfpa.deleted = false ");
		sb.append("AND cfpa.cil.id = :cilId");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		query.setParameter("cilId", cilId);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CFPartnerAnagraphs> LoadByType(CFAnagraphTypes type)
			throws PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT * FROM cf_partner_anagraphs WHERE cf_partner_anagraphs.deleted = FALSE ")
				.append("AND cf_partner_anagraphs. cf_partner_anagraphs.id IN ")
				.append("(SELECT cf_partner_anagraphs_projects.cfPartnerAnagraphs_id FROM ")
				.append("cf_partner_anagraphs_projects WHERE cf_partner_anagraphs_projects.type_id = :type ")
				.append("AND cf_partner_anagraphs_projects.deleted = FALSE ")
				.append("GROUP BY cf_partner_anagraphs_projects.cfPartnerAnagraphs_id)");

		SQLQuery query = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString())
				.addEntity(CFPartnerAnagraphs.class);
		query.setLong("type", type.getCfType());

		return query.list();
	}

	public CFPartnerAnagraphs GetByUser(Long userId)
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CFPartnerAnagraphs.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("user.id", userId));

		return (CFPartnerAnagraphs) criterion.uniqueResult();
	}
}
