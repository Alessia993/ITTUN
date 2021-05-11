/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.security.beans.LoginBean;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ProjectsBean extends PersistenceEntityBean<Projects>
{
	@SuppressWarnings("unchecked")
	public List<Projects> LoadByPattern(String searchPattern,
			String searchTitlePattern, String searchAssePattern) throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));

		if (searchPattern != null)
		{
			if (!searchPattern.equals(""))
			{
				criterion.add(Restrictions.like("code", searchPattern,
						MatchMode.ANYWHERE));
			}
		}

		if (searchTitlePattern != null)
		{
			if (!searchTitlePattern.equals(""))
			{
				criterion.add(Restrictions.like("title", searchTitlePattern,
						MatchMode.ANYWHERE));
			}
		}
		if(searchAssePattern != null)
		{
			if (!searchAssePattern.equals(""))
			{
				criterion.add(Restrictions.like("asse", Integer.parseInt(searchAssePattern)));
			}
		}

		criterion.addOrder(Order.desc("createDate"));

		List<Projects> list = criterion.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projects> LoadByActivationProcedure(String actProcId)
			throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));

		criterion.add(Restrictions.eq("activationProcedure.id",
				Long.valueOf(actProcId)));

		List<Projects> list = criterion.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projects> Load() throws PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));
		criterion.addOrder(Order.desc("createDate"));
		List<Projects> list = criterion.list();
		return list;
	}

	public Projects GetByAsse(String projId, Integer asse)
			throws PersistenceBeanException
	{
		if (projId == null)
		{
			try
			{
				new LoginBean().doLogout();
			}
			catch (Exception e)
			{
			}
			return new Projects();
		}
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("id", Long.valueOf(projId)))
				.add(Restrictions.eq("asse", asse));
		criterion.add(Restrictions.eq("deleted", false));

		Projects project = (Projects) criterion.uniqueResult();
		return project;
	}

	public List<Projects> LoadByActivationProcedure(
			ActivationProcedureAnagraph entity) throws PersistenceBeanException
	{
		return this.LoadByActivationProcedure(String.valueOf(entity.getId()));
	}

	/**
	 * This method will be filled in future
	 * 
	 * @throws PersistenceBeanException
	 */
	public void Delete(String id) throws NumberFormatException,
			HibernateException, PersistenceBeanException,
			PersistenceBeanException
	{
		BeansFactory.Projects().Delete(Long.parseLong(id));

		// TODO: Add new features
	}

	@SuppressWarnings("unchecked")
	public List<Projects> LoadByPatternAndUser(String searchPattern,
			String searchTitlePattern, String searchAssePattern, Long userId)
			throws PersistenceBeanException
	{
		return LoadByPatternAndUser(searchPattern, searchTitlePattern, searchAssePattern, userId,
				null);
	}

	@SuppressWarnings("unchecked")
	public List<Projects> LoadByPatternAndUser(String searchPattern,
			String searchTitlePattern, String searchAssePattern, Long userId, Boolean notAssigned)
			throws PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpap.project.id FROM CFPartnerAnagraphProject cfpap WHERE cfpap.deleted = false AND cfpap.removedFromProject = false AND cfpap.cfPartnerAnagraphs.id IN (");
		sb.append("SELECT cfpa.id FROM CFPartnerAnagraphs cfpa WHERE cfpa.deleted = false AND cfpa.user.id = :userId)");
		if (notAssigned != null)
		{
			sb.append(" and cfpap.notAssigned=:NOT_ASSIGNED");
		}
		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("userId", userId);
		if (notAssigned != null)
		{
			query.setParameter("NOT_ASSIGNED", notAssigned);
		}
		List<Long> listIndices = query.list();

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));

		if (searchPattern != null)
		{
			if (!searchPattern.equals(""))
			{
				criterion.add(Restrictions.like("code", searchPattern,
						MatchMode.ANYWHERE));
			}
		}

		if (searchTitlePattern != null)
		{
			if (!searchTitlePattern.equals(""))
			{
				criterion.add(Restrictions.like("title", searchTitlePattern,
						MatchMode.ANYWHERE));
			}
		}
		
		if (searchAssePattern != null)
		{
			if (!searchAssePattern.equals(""))
			{
				criterion.add(Restrictions.like("asse", Integer.parseInt(searchAssePattern)));
			}
		}
		
		List<Projects> list = new ArrayList<Projects>();
		if (!(listIndices == null || listIndices.isEmpty()))
		{
			criterion.add(Restrictions.in("id", listIndices));

			criterion.addOrder(Order.desc("createDate"));

			list = criterion.list();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projects> LoadByPatternAndUserAndAsse1_4(String searchPattern,
			String searchTitlePattern, Long userId)
			throws PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cfpap.project.id FROM CFPartnerAnagraphProject cfpap WHERE cfpap.deleted = false AND cfpap.cfPartnerAnagraphs.id IN (");
		sb.append("SELECT cfpa.id FROM CFPartnerAnagraphs cfpa WHERE cfpa.deleted = false AND cfpa.user.id = :userId)");

		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString());
		query.setParameter("userId", userId);

		List<Long> listIndices = query.list();

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));

		if (searchPattern != null)
		{
			if (!searchPattern.equals(""))
			{
				criterion.add(Restrictions.like("code", searchPattern,
						MatchMode.ANYWHERE));
			}
		}

		if (searchTitlePattern != null)
		{
			if (!searchTitlePattern.equals(""))
			{
				criterion.add(Restrictions.like("title", searchTitlePattern,
						MatchMode.ANYWHERE));
			}
		}

		List<Projects> list = new ArrayList<Projects>();
		if (!(listIndices == null || listIndices.isEmpty()))
		{
			criterion.add(Restrictions.in("id", listIndices));

			criterion.add(Restrictions.ne("asse", 5));
			criterion.addOrder(Order.desc("createDate"));
			list = criterion.list();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projects> LoadByPatternAndController(String searchPattern,
			String searchTitlePattern, String searchAssePattern, Long controllerId)
			throws PersistenceBeanException
	{
		Criteria criterionControllers = PersistenceSessionManager.getBean()
				.getSession().createCriteria(CFPartnerAnagraphProject.class);

		criterionControllers.add(Restrictions.eq("deleted", false));

		List<CFPartnerAnagraphProject> listPartners = criterionControllers
				.list();
		List<Long> projectIndices = new ArrayList<Long>();

		for (CFPartnerAnagraphProject p : listPartners)
		{
			if (p.getCil() != null
					&& !projectIndices.contains(p.getProject().getId()))
			{
				if (p.getCil().getUser().getId().equals(controllerId))
				{
					projectIndices.add(p.getProject().getId());
				}
			}

			if (p.getDaec() != null
					&& !projectIndices.contains(p.getProject().getId()))
			{
				if (p.getDaec().getUser().getId().equals(controllerId))
				{
					projectIndices.add(p.getProject().getId());
				}
			}
		}

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(Projects.class)
				.add(Restrictions.eq("deleted", false));

		if (searchPattern != null)
		{
			if (!searchPattern.equals(""))
			{
				criterion.add(Restrictions.like("code", searchPattern,
						MatchMode.ANYWHERE));
			}
		}

		if (searchTitlePattern != null)
		{
			if (!searchTitlePattern.equals(""))
			{
				criterion.add(Restrictions.like("title", searchTitlePattern,
						MatchMode.ANYWHERE));
			}
		}
		
		if (searchAssePattern != null)
		{
			if (!searchAssePattern.equals(""))
			{
				criterion.add(Restrictions.like("asse", Integer.parseInt(searchAssePattern)));
			}
		}

		List<Projects> list = new ArrayList<Projects>();
		if (projectIndices != null && !projectIndices.isEmpty())
		{
			criterion.add(Restrictions.in("id", projectIndices));
			criterion.addOrder(Order.desc("createDate"));
			list = criterion.list();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projects> SearchProjects(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, boolean withCertifiableDurs,
			Boolean withDaeiToCertCosts) throws HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT projects.* FROM projects LEFT OUTER JOIN ");
		sb.append("project_iformation_completations ON ");
		sb.append("projects.id = project_iformation_completations.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs_projects ON ");
		sb.append("projects.id = cf_partner_anagraphs_projects.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("cf_partner_anagraphs_projects.cfPartnerAnagraphs_id = ");
		sb.append("cf_partner_anagraphs.id ");
		sb.append("WHERE ");

		if (notEmpty(code))
		{
			sb.append("projects.code LIKE '%");
			sb.append(code);
			sb.append("%' AND ");
		}

		if (notEmpty(asse))
		{
			sb.append("projects.asse = :asse AND ");
		}

		if (notEmpty(title))
		{
			sb.append("projects.title LIKE '%");
			sb.append(title);
			sb.append("%' AND ");
		}

		if (notEmpty(state))
		{
			sb.append("projects.state_id = :state AND ");
		}

		if (createS != null)
		{
			sb.append("projects.`create_date` > :createS AND ");
		}

		if (createE != null)
		{
			sb.append("projects.`create_date` < :createE AND ");
		}

		if (notEmpty(cupCode))
		{
			sb.append("project_iformation_completations.cup_code LIKE '%");
			sb.append(cupCode);
			sb.append("%' AND ");
		}

		if (notEmpty(cfName))
		{
			sb.append("cf_partner_anagraphs.name LIKE '%");
			sb.append(cfName);
			sb.append("%' AND ");
		}

		if (withCertifiableDurs)
		{
			sb.append(" projects.id IN (");
			sb.append("Select cost_definitions.project_id ");
			sb.append("FROM cost_definitions LEFT OUTER JOIN dur_compilations ON cost_definitions.dur_compilations_id = dur_compilations.id ");
			sb.append("WHERE dur_compilations.dur_state_id = 7");
			sb.append(") AND ");
		}

		if (Boolean.TRUE.equals(withDaeiToCertCosts))
		{
			sb.append(" projects.id IN (");
			sb.append("Select cost_definitions.project_id ");
			sb.append("FROM cost_definitions ");
			sb.append("WHERE cost_definitions.cost_state_id = 2 and deleted = false");
			sb.append(") AND ");
		}

		sb.append("projects.deleted = false ");

		sb.append("ORDER BY create_date DESC");

		Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(Projects.class);
		if (notEmpty(asse))
		{
			q.setInteger("asse", Integer.parseInt(asse));
		}
		if (notEmpty(state))
		{
			q.setLong("state", Long.parseLong(state));
		}
		if (createS != null)
		{
			q.setDate("createS", createS);
		}
		if (createE != null)
		{
			q.setDate("createE", createE);
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Projects> SearchProjectsByCIL(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, Long userId,
			Boolean withDaeiToCertCosts) throws HibernateException,
			PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT projects.* FROM projects LEFT OUTER JOIN ");
		sb.append("project_iformation_completations ON ");
		sb.append("projects.id = project_iformation_completations.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs_projects ON ");
		sb.append("projects.id = cf_partner_anagraphs_projects.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("cf_partner_anagraphs_projects.cfPartnerAnagraphs_id = ");
		sb.append("cf_partner_anagraphs.id ");
		// sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("LEFT OUTER JOIN controller_user_anagraph ON ");
		sb.append("controller_user_anagraph.id = ");
		sb.append("cf_partner_anagraphs_projects.cil_id WHERE ");

		sb.append("controller_user_anagraph.user_id = :userId AND ");

		if (notEmpty(code))
		{
			sb.append("projects.code LIKE '%");
			sb.append(code);
			sb.append("%' AND ");
		}

		if (notEmpty(asse))
		{
			sb.append("projects.asse = :asse AND ");
		}

		if (notEmpty(title))
		{
			sb.append("projects.title LIKE '%");
			sb.append(title);
			sb.append("%' AND ");
		}

		if (notEmpty(state))
		{
			sb.append("projects.state_id = :state AND ");
		}

		if (createS != null)
		{
			sb.append("projects.`create_date` > :createS AND ");
		}

		if (createE != null)
		{
			sb.append("projects.`create_date` < :createE AND ");
		}

		if (notEmpty(cupCode))
		{
			sb.append("project_iformation_completations.cup_code LIKE '%");
			sb.append(cupCode);
			sb.append("%' AND ");
		}

		if (notEmpty(cfName))
		{
			sb.append("cf_partner_anagraphs.name LIKE '%");
			sb.append(cfName);
			sb.append("%' AND ");
		}

		if (Boolean.TRUE.equals(withDaeiToCertCosts))
		{
			sb.append(" projects.id IN (");
			sb.append("Select cost_definitions.project_id ");
			sb.append("FROM cost_definitions ");
			sb.append("WHERE cost_definitions.cost_state_id = 2 and deleted = false");
			sb.append(") AND ");
		}

		sb.append("projects.deleted = false ");

		sb.append("ORDER BY create_date DESC");

		Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(Projects.class);

		q.setLong("userId", userId);

		if (notEmpty(asse))
		{
			q.setInteger("asse", Integer.parseInt(asse));
		}
		if (notEmpty(state))
		{
			q.setLong("state", Long.parseLong(state));
		}
		if (createS != null)
		{
			q.setDate("createS", createS);
		}
		if (createE != null)
		{
			q.setDate("createE", createE);
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Projects> SearchProjectsByDAEC(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, Long userId)
			throws HibernateException, PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT projects.* FROM projects LEFT OUTER JOIN ");
		sb.append("project_iformation_completations ON ");
		sb.append("projects.id = project_iformation_completations.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs_projects ON ");
		sb.append("projects.id = cf_partner_anagraphs_projects.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("cf_partner_anagraphs_projects.cfPartnerAnagraphs_id = ");
		sb.append("cf_partner_anagraphs.id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("controller_user_anagraph.id = ");
		sb.append("cf_partner_anagraphs_projects.daec_id WHERE ");

		sb.append("controller_user_anagraph.user_id = :userId AND ");

		if (notEmpty(code))
		{
			sb.append("projects.code LIKE '%");
			sb.append(code);
			sb.append("%' AND ");
		}

		if (notEmpty(asse))
		{
			sb.append("projects.asse = :asse AND ");
		}

		if (notEmpty(title))
		{
			sb.append("projects.title LIKE '%");
			sb.append(title);
			sb.append("%' AND ");
		}

		if (notEmpty(state))
		{
			sb.append("projects.state_id = :state AND ");
		}

		if (createS != null)
		{
			sb.append("projects.`create_date` > :createS AND ");
		}

		if (createE != null)
		{
			sb.append("projects.`create_date` < :createE AND ");
		}

		if (notEmpty(cupCode))
		{
			sb.append("project_iformation_completations.cup_code LIKE '%");
			sb.append(cupCode);
			sb.append("%' AND ");
		}

		if (notEmpty(cfName))
		{
			sb.append("cf_partner_anagraphs.name LIKE '%");
			sb.append(cfName);
			sb.append("%' AND ");
		}

		sb.append("projects.deleted = false ");

		sb.append("ORDER BY create_date DESC");

		Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(Projects.class);

		q.setLong("userId", userId);

		if (notEmpty(asse))
		{
			q.setInteger("asse", Integer.parseInt(asse));
		}
		if (notEmpty(state))
		{
			q.setLong("state", Long.parseLong(state));
		}
		if (createS != null)
		{
			q.setDate("createS", createS);
		}
		if (createE != null)
		{
			q.setDate("createE", createE);
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Projects> SearchProjectsCF(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, Long userId)
			throws HibernateException, PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT projects.* FROM projects LEFT OUTER JOIN ");
		sb.append("project_iformation_completations ON ");
		sb.append("projects.id = project_iformation_completations.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs_projects ON ");
		sb.append("projects.id = cf_partner_anagraphs_projects.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("cf_partner_anagraphs_projects.cfPartnerAnagraphs_id = ");
		sb.append("cf_partner_anagraphs.id WHERE ");

		sb.append("cf_partner_anagraphs.user_id = :userId AND ");
		sb.append("projects.asse < 5 AND ");

		if (notEmpty(code))
		{
			sb.append("projects.code LIKE '%");
			sb.append(code);
			sb.append("%' AND ");
		}

		if (notEmpty(asse))
		{
			sb.append("projects.asse = :asse AND ");
		}

		if (notEmpty(title))
		{
			sb.append("projects.title LIKE '%");
			sb.append(title);
			sb.append("%' AND ");
		}

		if (notEmpty(state))
		{
			sb.append("projects.state_id = :state AND ");
		}

		if (createS != null)
		{
			sb.append("projects.`create_date` > :createS AND ");
		}

		if (createE != null)
		{
			sb.append("projects.`create_date` < :createE AND ");
		}

		if (notEmpty(cupCode))
		{
			sb.append("project_iformation_completations.cup_code LIKE '%");
			sb.append(cupCode);
			sb.append("%' AND ");
		}

		if (notEmpty(cfName))
		{
			sb.append("cf_partner_anagraphs.name LIKE '%");
			sb.append(cfName);
			sb.append("%' AND ");
		}

		sb.append("projects.deleted = false ");

		sb.append("ORDER BY create_date DESC");

		Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(Projects.class);

		q.setLong("userId", userId);

		if (notEmpty(asse))
		{
			q.setInteger("asse", Integer.parseInt(asse));
		}
		if (notEmpty(state))
		{
			q.setLong("state", Long.parseLong(state));
		}
		if (createS != null)
		{
			q.setDate("createS", createS);
		}
		if (createE != null)
		{
			q.setDate("createE", createE);
		}
		return q.list();
	}

	public List<Projects> SearchProjectsPartner(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, Long userId)
			throws HibernateException, PersistenceBeanException
	{
		return SearchProjectsPartner(code, title, asse, createS, createE,
				state, cupCode, cfName, userId, null);
	}

	@SuppressWarnings("unchecked")
	public List<Projects> SearchProjectsPartner(String code, String title,
			String asse, Date createS, Date createE, String state,
			String cupCode, String cfName, Long userId, Boolean notAssigned)
			throws HibernateException, PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT projects.* FROM projects LEFT OUTER JOIN ");
		sb.append("project_iformation_completations ON ");
		sb.append("projects.id = project_iformation_completations.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs_projects ON ");
		sb.append("projects.id = cf_partner_anagraphs_projects.project_id ");
		sb.append("LEFT OUTER JOIN cf_partner_anagraphs ON ");
		sb.append("cf_partner_anagraphs_projects.cfPartnerAnagraphs_id = ");
		sb.append("cf_partner_anagraphs.id WHERE ");
		sb.append("cf_partner_anagraphs_projects.deleted = false AND ");
		sb.append("cf_partner_anagraphs_projects.removed_from_project = false AND ");
		sb.append("cf_partner_anagraphs.user_id = :userId AND ");

		if (notEmpty(code))
		{
			sb.append("projects.code LIKE '%");
			sb.append(code);
			sb.append("%' AND ");
		}

		if (notEmpty(asse))
		{
			sb.append("projects.asse = :asse AND ");
		}

		if (notEmpty(title))
		{
			sb.append("projects.title LIKE '%");
			sb.append(title);
			sb.append("%' AND ");
		}

		if (notEmpty(state))
		{
			sb.append("projects.state_id = :state AND ");
		}

		if (createS != null)
		{
			sb.append("projects.`create_date` > :createS AND ");
		}

		if (createE != null)
		{
			sb.append("projects.`create_date` < :createE AND ");
		}

		if (notEmpty(cupCode))
		{
			sb.append("project_iformation_completations.cup_code LIKE '%");
			sb.append(cupCode);
			sb.append("%' AND ");
		}

		if (notEmpty(cfName))
		{
			sb.append("cf_partner_anagraphs.name LIKE '%");
			sb.append(cfName);
			sb.append("%' AND ");
		}

		sb.append("projects.deleted = false ");

		if (notAssigned != null)
		{
			sb.append(" and cf_partner_anagraphs_projects.not_assigned=:NOT_ASSIGNED ");
		}

		sb.append("ORDER BY create_date DESC");

		Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(Projects.class);

		q.setLong("userId", userId);

		if (notEmpty(asse))
		{
			q.setInteger("asse", Integer.parseInt(asse));
		}
		if (notEmpty(state))
		{
			q.setLong("state", Long.parseLong(state));
		}
		if (createS != null)
		{
			q.setDate("createS", createS);
		}
		if (createE != null)
		{
			q.setDate("createE", createE);
		}
		if (notAssigned != null)
		{
			q.setParameter("NOT_ASSIGNED", notAssigned);
		}
		return q.list();
	}

	private boolean notEmpty(String str)
	{
		if (str == null || str.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
