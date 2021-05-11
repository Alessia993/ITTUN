package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.DocumentsAdm;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;


public class DocumentsAdmBean extends PersistenceEntityBean<DocumentsAdm> {
	@SuppressWarnings("unchecked")
	public List<DocumentsAdm> LoadByParams(DocumentsBeanBaseRequest baseRequest,
			String releaseFrom, 
			String partnerId, String userRole)

	throws PersistenceBeanException {
		List<Long> userIds = getUsersIds(releaseFrom, null);

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(DocumentsAdm.class);
		addBaseRestrictions(baseRequest,criterion);

		if (releaseFrom != null && !releaseFrom.equals("")) {
			if (userIds != null && !userIds.isEmpty()) {
				criterion.add(Restrictions.or(
						Restrictions.in("user.id", userIds),
						Restrictions.eq("isPublic", true)));
			} else {
				return new ArrayList<DocumentsAdm>();
			}
		}

		if (partnerId != null && !partnerId.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("FROM CFPartnerAnagraphProject cfpa ");
			sb.append("WHERE cfpa.deleted = false ");
			sb.append("AND cfpa.progressiveId = :progressiveId ");
			if (baseRequest.getProjectId() != null) {
				sb.append("AND cfpa.project.id = :projectId ");
			}

			Query query = PersistenceSessionManager.getBean().getSession()
					.createQuery(sb.toString());
			query.setParameter("progressiveId", Long.valueOf(partnerId));
			if (baseRequest.getProjectId() != null) {
				query.setParameter("projectId", baseRequest.getProjectId());
			}
			List<CFPartnerAnagraphProject> listCfs = query.list();
			List<Long> partnerIds = new ArrayList<Long>();
			for (CFPartnerAnagraphProject cfProject : listCfs) {
				if (cfProject.getCfPartnerAnagraphs().getUser() != null) {
					partnerIds.add(cfProject.getCfPartnerAnagraphs().getUser()
							.getId());
				} else {
					List<Users> userList = BeansFactory.Users().getByRole(
							UserRoleTypes.AGU);
					for (Users user : userList) {
						partnerIds.add(user.getId());
					}
				}
			}

			criterion.add(Restrictions.in("user.id", partnerIds));
		}

		addRoleRestriction(userRole, criterion);

		criterion.addOrder(Order.desc("createDate"));

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<DocumentsAdm> LoadByParams(DocumentsBeanBaseRequest baseRequest,String releaseFrom,
			List<Long> userids, String partnerId, String userRole,
			boolean forDaec)

	throws PersistenceBeanException {
		List<Long> userIds = null;

		if (releaseFrom != null && !releaseFrom.equals("")) {
			userIds = getUsersIds(releaseFrom, null);
		} else {
			userIds = new ArrayList<Long>();
			userIds.addAll(userids);
		}

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(DocumentsAdm.class);
		addBaseRestrictions(baseRequest,criterion);

		if (userIds != null && !userIds.isEmpty()) {
			if (forDaec) {
				criterion.add(Restrictions.or(Restrictions.in("user.id",
						userIds), Restrictions.or(
						Restrictions.eq("isForDaec", true),
						Restrictions.eq("isPublic", true))));
			} else {
				criterion.add(Restrictions.or(
						Restrictions.in("user.id", userIds),
						Restrictions.eq("isPublic", true)));
			}

		} else {
			return new ArrayList<DocumentsAdm>();
		}

		addPartnerRestriction(partnerId, criterion);

		addRoleRestriction(userRole, criterion);

		criterion.addOrder(Order.desc("createDate"));

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<DocumentsAdm> LoadByParamsForCILAsse5(DocumentsBeanBaseRequest baseRequest,String releaseFrom, 
			List<Long> userids, String partnerId, String userRole)

	throws PersistenceBeanException {
		List<Long> userIds = null;

		if (releaseFrom != null && !releaseFrom.equals("")) {
			userIds = getUsersIds(releaseFrom, null);
		} else {
			userIds = new ArrayList<Long>();
			userIds.addAll(userids);
		}

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(DocumentsAdm.class);
		addBaseRestrictions(baseRequest,criterion);

		if (userIds != null && !userIds.isEmpty()) {
			criterion.add(Restrictions.or(Restrictions.or(
					Restrictions.in("user.id", userIds),
					Restrictions.eq("uploadRole", 2)), Restrictions.eq(
					"isPublic", true)));
		} else {
			return new ArrayList<DocumentsAdm>();
		}

		addPartnerRestriction(partnerId, criterion);

		addRoleRestriction(userRole, criterion);

		criterion.addOrder(Order.desc("createDate"));

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<DocumentsAdm> LoadByParamsForPartner(DocumentsBeanBaseRequest baseRequest,
			String releaseFrom, 
			List<Long> userids, String partnerId, String userRole)

	throws PersistenceBeanException {
		List<Long> userIds = null;

		if (releaseFrom != null && !releaseFrom.equals("")) {
			userIds = getUsersIds(releaseFrom, null);
		} else {
			userIds = new ArrayList<Long>();
			userIds.addAll(userids);
		}

		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(DocumentsAdm.class);
		addBaseRestrictions(baseRequest,criterion);

		if (userIds != null && !userIds.isEmpty()) {
			criterion.add(Restrictions.or(Restrictions.or(
					Restrictions.in("user.id", userIds),
					Restrictions.eq("isPublic", true)), Restrictions.eq(
					"forPartner.id", userIds.get(0))));
		} else {
			return new ArrayList<DocumentsAdm>();
		}

		addPartnerRestriction(partnerId, criterion);

		addRoleRestriction(userRole, criterion);

		criterion.addOrder(Order.desc("createDate"));

		return criterion.list();
	}

	/**
	 * @param userRole
	 * @param criterion
	 * @throws PersistenceBeanException
	 */
	private void addRoleRestriction(String userRole, Criteria criterion)
			throws PersistenceBeanException {
		if (userRole != null && !userRole.isEmpty()) {
			try {
				Long userRoleId = Long.valueOf(userRole);

				StringBuilder query = new StringBuilder(
						"SELECT ur.user.id FROM UserRoles ur WHERE ");
				query.append("ur.role.id = :userRole");

				Query queryUr = PersistenceSessionManager.getBean()
						.getSession().createQuery(query.toString());
				queryUr.setParameter("userRole", userRoleId);
				List<Long> userInd = queryUr.list();

				criterion.add(Restrictions.in("user.id", userInd));
			} catch (NumberFormatException e) {
			}
		}
	}

	/**
	 * @param projectId
	 * @param title
	 * @param dateFrom
	 * @param dateTo
	 * @param criterion
	 * @param protocolNumber
	 * @param sectionId
	 */
	private void addBaseRestrictions(DocumentsBeanBaseRequest request, Criteria criterion) {
		criterion.add(Restrictions.eq("deleted", false));

		if (request.getProjectId() != null) {
			criterion.add(Restrictions.eq("project.id", request.getProjectId()));
		}

		if (request.getTitle() != null && !request.getTitle().equals("")) {
			criterion
					.add(Restrictions.like("title", request.getTitle(), MatchMode.ANYWHERE));
		}

		if (request.getDateFrom() != null) {
			criterion.add(Restrictions.ge("documentDate", request.getDateFrom()));
		}

		if (request.getDateTo() != null) {
			criterion.add(Restrictions.le("documentDate", request.getDateTo()));
		}
		
		if (request.getEditDate() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(request.getEditDate());
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			criterion.add(Restrictions.ge("editableDate", calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
			criterion.add(Restrictions.lt("editableDate", calendar.getTime()));
		}

		if (request.getProtocolNumber() != null && !request.getProtocolNumber().equals("")) {
			criterion.add(Restrictions.like("protocolNumber", request.getProtocolNumber(),
					MatchMode.ANYWHERE));
		}

		if (request.getSectionId() != null) {
			criterion.add(Restrictions.eq("section.id", request.getSectionId()));
		}
	
		if (request.getDocumentNumber() != null && !request.getDocumentNumber().isEmpty()) {
			criterion.add(Restrictions.eq("documentNumber", Long.parseLong(request.getDocumentNumber())));
		}
		if(request.isOnlyDocuments()){
			criterion.add(Restrictions.eq("loadFromDocuments", true));
		}
		
	}

	/**
	 * @param releaseFrom
	 * @param userIds
	 * @return
	 * @throws PersistenceBeanException
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getUsersIds(String releaseFrom, List<Long> userIds)
			throws PersistenceBeanException {
		if (releaseFrom != null && !releaseFrom.isEmpty()) {
			String[] nWords = releaseFrom.split(" ");
			StringBuilder query = new StringBuilder(
					"SELECT u.id FROM Users u WHERE ");

			for (int i = 0; i < nWords.length; i++) {
				String word = nWords[i];
				if (!word.isEmpty()) {
					query.append(" u.name LIKE '%");
					query.append(word);
					query.append("%' OR ");
					query.append(" u.surname LIKE '%");
					query.append(word);
					query.append("%'");
				}

				if (i != nWords.length - 1) {
					query.append(" OR ");
				}
			}
			if (userIds != null) {
				query.append(" AND u.id IN (:userIndices)");
			}
			Query hQuery = PersistenceSessionManager.getBean().getSession()
					.createQuery(query.toString());
			if (userIds != null) {
				hQuery.setParameterList("userIndices", userIds);
			}
			return hQuery.list();
		}
		return null;
	}
	
	/**
	 * @param partnerId
	 * @param criterion
	 * @throws PersistenceBeanException
	 */
	private void addPartnerRestriction(String partnerId, Criteria criterion)
			throws PersistenceBeanException {
		if (partnerId != null && !partnerId.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT cfpa.cfPartnerAnagraphs.id ");
			sb.append("FROM CFPartnerAnagraphProject cfpa ");
			sb.append("WHERE cfpa.deleted = false ");
			sb.append("AND cfpa.progressiveId = :progressiveId ");

			Query query = PersistenceSessionManager.getBean().getSession()
					.createQuery(sb.toString());
			query.setParameter("progressiveId", Long.valueOf(partnerId));
			List<Long> listCfIds = query.list();

			criterion.add(Restrictions.in("forPartner.id", listCfIds));
		}
	}

	public List<DocumentsAdm> loadByCostDefinitionParams(Long projectId,Long progressiveId,
			Long phaseId)
	{
		ArrayList<DocumentsAdm> result = new ArrayList<DocumentsAdm>();
		try
		{
			result.addAll(getDocuments(projectId,progressiveId, phaseId,true));
			result.addAll(getDocuments(projectId,progressiveId, phaseId,false));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param progressiveId
	 * @param phaseId
	 * @param result
	 * @throws PersistenceBeanException
	 */
	private ArrayList<DocumentsAdm> getDocuments(Long projectId, Long progressiveId, Long phaseId,
			boolean isCostDocument) throws PersistenceBeanException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select cost.")
				.append(isCostDocument ? "costDocument" : "paymentDocument")
				.append(" from CostDefinitions cost where cost.project.id=:PROJECT_ID and cost.")
				.append(isCostDocument ? "costDocument" : "paymentDocument")
				.append("!= null");
		if (progressiveId != null)
		{
			sb.append(" and cost.progressiveId=:PROGRESSIVE_ID");
		}
		if (phaseId != null)
		{
			sb.append(" and cost.costDefinitionPhase.id=:PHASE_ID");
		}
		Query query = PersistenceSessionManager.getBean().getSession()
				.createQuery(sb.toString()).setParameter("PROJECT_ID", projectId);
		if (progressiveId != null)
		{
			query.setParameter("PROGRESSIVE_ID", progressiveId);
		}
		if (phaseId != null)
		{
			query.setParameter("PHASE_ID", phaseId);
		}
		return (ArrayList<DocumentsAdm>) query.list();
	}
}

