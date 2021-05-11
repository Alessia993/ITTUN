/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsSpecificGoalsWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CostDefinitionsBean extends PersistenceEntityBean<CostDefinitions> {
	private DurCompilations durCompilation;
	
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> LoadByAdditionalFesrInfo(Long additionalFesrInfoId) throws PersistenceBeanException{
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("additionalFesrInfo.id", additionalFesrInfoId));
		criteria.add(Restrictions.eq("deleted", false));
		return criteria.list();
	}
	
	public CostDefinitionsSpecificGoalsWrapper getByExpenditureDeclarationSpecificGoal(
			Long costDefinitionsId)
			throws HibernateException, PersistenceBeanException {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT cost_definitions.id, cost_definitions.create_date, cost_definitions.deleted, cost_definitions.acu_sertified, ");
		sb.append(
				"cost_definitions.acu_certification, cost_definitions.agu_sertified, cost_definitions.agu_certification, ");
		sb.append("cost_definitions.stc_sertified, cost_definitions.stc_certification, cost_definitions.cf_check, ");
		sb.append("cost_definitions.cil_check, cost_definitions.tot_amount_eligl_exp_retired, ");
		sb.append(
				"cost_definitions.tot_amount_elig_exp_suspended, cost_definitions.date_suspend_acu, cost_definitions.retreat_date,  ");
		sb.append(
				"expenditure_declaration.accounting_period_from as exp_dec_accounting_period_from, expenditure_declaration.accounting_period_to as exp_dec_accounting_period_to, ");
		sb.append("expenditure_declaration.submission_date as exp_dec_submission_date, ");
		sb.append("specific_goals.code as specific_objective , projects.asse as axis ");
		sb.append("from cost_definitions ");
		sb.append(
				"inner join expenditure_declaration_dur_compilations on expenditure_declaration_dur_compilations.durCompilations_id=cost_definitions.dur_compilations_id ");
		sb.append(
				"inner join expenditure_declaration on expenditure_declaration.id = expenditure_declaration_dur_compilations.expenditure_declaration_id ");
		sb.append("left join projects on projects.id = cost_definitions.project_id ");
		sb.append("left join specific_goals on specific_goals.id=projects.specific_goal_id ");
		//
		sb.append("WHERE ");
		sb.append("cost_definitions.deleted = false AND ");
		sb.append("cost_definitions.id = :cost_definitions_id ");

		Query q = PersistenceSessionManager.getBean().getSession().createSQLQuery(sb.toString())
				.addEntity(CostDefinitionsSpecificGoalsWrapper.class);

		q.setLong("cost_definitions_id", costDefinitionsId);
		
	    List<CostDefinitionsSpecificGoalsWrapper> list = q.list();
	    if(list != null && !list.isEmpty()){
	    	return list.get(0);
	    }
	    return null;
	}

	public List<CostDefinitionsSpecificGoalsWrapper> getByExpenditureDeclarationSpecificGoal(
			Date accounting_period_from, Date accounting_period_to)
			throws HibernateException, PersistenceBeanException {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT cost_definitions.id, cost_definitions.create_date, cost_definitions.deleted, cost_definitions.acu_sertified, ");
		sb.append(
				"cost_definitions.acu_certification, cost_definitions.agu_sertified, cost_definitions.agu_certification, ");
		sb.append("cost_definitions.stc_sertified, cost_definitions.stc_certification, cost_definitions.cf_check, ");
		sb.append("cost_definitions.cil_check, cost_definitions.tot_amount_eligl_exp_retired, ");
		sb.append(
				"cost_definitions.tot_amount_elig_exp_suspended, cost_definitions.date_suspend_acu, cost_definitions.retreat_date,  ");
		sb.append(
				"expenditure_declaration.accounting_period_from as exp_dec_accounting_period_from, expenditure_declaration.accounting_period_to as exp_dec_accounting_period_to, ");
		sb.append("expenditure_declaration.submission_date as exp_dec_submission_date, ");
		sb.append("specific_goals.code as specific_objective , projects.asse as axis ");
		sb.append("from cost_definitions ");
		sb.append(
				"inner join expenditure_declaration_dur_compilations on expenditure_declaration_dur_compilations.durCompilations_id=cost_definitions.dur_compilations_id ");
		sb.append(
				"inner join expenditure_declaration on expenditure_declaration.id = expenditure_declaration_dur_compilations.expenditure_declaration_id ");
		sb.append("left join projects on projects.id = cost_definitions.project_id ");
		sb.append("left join specific_goals on specific_goals.id=projects.specific_goal_id ");
		//
		sb.append("WHERE ");
		sb.append("cost_definitions.deleted = false AND ");
		sb.append("expenditure_declaration.expenditure_declaration_state_id='3' ");
		sb.append("and expenditure_declaration.accounting_period_from = :accounting_period_from ");
		sb.append("and expenditure_declaration.accounting_period_to = :accounting_period_to");

		Query q = PersistenceSessionManager.getBean().getSession().createSQLQuery(sb.toString())
				.addEntity(CostDefinitionsSpecificGoalsWrapper.class);

		q.setDate("accounting_period_from", accounting_period_from);
		q.setDate("accounting_period_to", accounting_period_to);

		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByProject(String projectId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.addOrder(Order.asc("id")).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByDurCompilationWithoutProject(Long durCompilationId)
			throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("dismiss", false));
		if (durCompilationId != null) {
			criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
			criterion.addOrder(Order.desc("createDate"));
		} else {
			criterion.add(Restrictions.isNull("durCompilation"));
			criterion.add(Restrictions.eq("costState.id", CostStateTypes.CFValidate.getState()));
		}

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartner(String projectId, Long partnerId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("user.id", partnerId)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerAdditionalFinansing(String projectId, Long partnerId)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("additionalFinansing", Boolean.TRUE)).add(Restrictions.eq("user.id", partnerId))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerAndBudgetOwner(String projectId, Long partnerId)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions
						.or(Restrictions.eq("budgetOwner.id", partnerId), Restrictions.eq("user.id", partnerId)))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByBudgetOwner(String projectId, Long userId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("budgetOwner.id", userId)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByCFWithPartner(String projectId, Long partnerId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate"))
				.add(Restrictions
						.eq("project.id",
								Long.valueOf(
										projectId)))
				.add(Restrictions.or(Restrictions.eq("user.id", partnerId), Restrictions.or(
						Restrictions.and(Restrictions.eq("costState.id", CostStateTypes.CFValidation.getState()),
								Restrictions.eq("createdByPartner", true)),
						Restrictions.and(Restrictions.eq("refucedFromDur", true),
								Restrictions.eq("createdByPartner", true)))))
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByCFWithPartnerAndBudgedOwner(String projectId, Long partnerId)
			throws PersistenceBeanException {
		// LogicalExpression notState = Restrictions
		// .and(Restrictions.ne("costState.id",
		// CostStateTypes.Draft.getState()), Restrictions.and(
		// Restrictions.ne("costState.id",
		// CostStateTypes.RefusedCIL.getState()),
		// Restrictions.ne("costState.id",
		// CostStateTypes.RefusedDAEC.getState())));
		SimpleExpression notState = Restrictions.ne("costState.id", CostStateTypes.Draft.getState());

		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(Restrictions.eq("user.id", partnerId),
						Restrictions.or(Restrictions.eq("budgetOwner.id", partnerId), Restrictions.or(Restrictions.and(

								Restrictions.and(
										Restrictions.ge("costState.id", CostStateTypes.CFValidation.getState()),
										notState),

								Restrictions.eq("createdByPartner", true)),
								Restrictions.and(Restrictions.eq("refucedFromDur", true),
										Restrictions.eq("createdByPartner", true))))))
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetExcludePartner(String projectId, Long partnerId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.not(Restrictions.eq("user.id", partnerId))).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartners(String projectId, Long[] partnerIds) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.in("user.id", partnerIds))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByValues(String projectId, String docNumber, Date docDate, String docCode, Long id,
			Long userId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("documentNumber", docNumber)).add(Restrictions.eq("documentDate", docDate))
				.add(Restrictions.ne("id", id == null ? 0 : id)).add(Restrictions.eq("cfPivaVta", docCode))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState()))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("budgetOwner.id", userId)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByGuiridicalEngage(Long id) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("giuridicalEngages.id", id)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByValues(String projectId, String costItemId, Long id, Long userId)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("budgetOwner.id", userId)).add(Restrictions.ne("id", id == null ? 0 : id))
				.addOrder(Order.desc("createDate"))
				// .add(Restrictions.ne("costState.id",
				// CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState()))
				.add(Restrictions.eq("costItem.id", Long.parseLong(costItemId))).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByValuesPhase(String projectId, String costItemId, Long id, Long userId)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("budgetOwner.id", userId))
				// .add(Restrictions.eq("costDefinitionPhase.id", phaseId))
				.add(Restrictions.ne("id", id == null ? 0 : id)).addOrder(Order.desc("createDate"))
				// .add(Restrictions.ne("costState.id",
				// CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState()))
				.add(Restrictions.eq("costItem.id", Long.parseLong(costItemId))).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetForProjectCheckValues(String projectId, Long userId)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("budgetOwner.id", userId)).addOrder(Order.desc("createDate"))
				// .add(Restrictions.ne("costState.id",
				// CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState())).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByProjectAndState(String projectId, CostStates state)
			throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("dismiss", false));
		criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criterion.add(Restrictions.eq("costState", state)).addOrder(Order.desc("createDate"));

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByDurCompilation(Long durCompilationId) throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("dismiss", false));
		if (durCompilationId != null) {
			criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
			criterion.addOrder(Order.desc("createDate"));
		} else {
			criterion.add(Restrictions.isNull("durCompilation"));
			criterion.add(Restrictions.eq("costState.id", CostStateTypes.CFValidate.getState()));
			criterion.add(Restrictions.eq("project.id", Long
					.valueOf(String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")))));
		}

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public CostDefinitions getFirstByDurCompilationWithoutProject(Long durCompilationId) throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("dismiss", false));
		if (durCompilationId != null) {
			criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
			criterion.addOrder(Order.desc("createDate"));
		}

		return (CostDefinitions) criterion.list().get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getByDurCompilationWithoutProject(Long durCompilationId) throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("dismiss", false));
		if (durCompilationId != null) {
			criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
			criterion.addOrder(Order.desc("createDate"));
		}

		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByProject(String projectId, Long[] types) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.in("costState.id", types))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByProject(String projectId, Long type) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("costState.id", type))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByProject(String projectId, CostStateTypes type) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("costState.id", type.getState())).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartner(String projectId, Long partnerId, Long[] types)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("user.id", partnerId))
				.addOrder(Order.desc("createDate")).add(Restrictions.in("costState.id", types)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerInDur(String projectId, Long partnerId, Long NotDurStateId)
			throws PersistenceBeanException {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT cost_definitions.* FROM cost_definitions LEFT JOIN ");
		sb.append("dur_compilations ON ");
		sb.append("cost_definitions.dur_compilations_id = dur_compilations.id ");
		sb.append("WHERE ");
		sb.append("cost_definitions.deleted = false AND ");

		if (projectId != null && !projectId.trim().equals("")) {
			sb.append("cost_definitions.project_id = :projectId AND ");
		}
		if (partnerId != null) {
			sb.append("cost_definitions.user_id = :userId AND ");
		}
		if (NotDurStateId != null) {
			sb.append("dur_compilations.dur_state_id != :durStateId ");
		}

		Query q = PersistenceSessionManager.getBean().getSession().createSQLQuery(sb.toString())
				.addEntity(CostDefinitions.class);

		if (projectId != null && !projectId.trim().equals("")) {
			q.setString("projectId", projectId);
		}
		if (partnerId != null) {
			q.setLong("userId", partnerId);
		}
		if (NotDurStateId != null) {
			q.setLong("durStateId", NotDurStateId);
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerAndBudgetOwner(String projectId, Long partnerId, Long[] types)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(Restrictions.eq("user.id", partnerId),
						Restrictions.eq("budgetOwner.id", partnerId)))
				.addOrder(Order.desc("createDate")).add(Restrictions.in("costState.id", types)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartner(String projectId, Long partnerId, Long type)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("user.id", partnerId))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("costState.id", type)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerAndBudgetOwner(String projectId, Long partnerId, Long type)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(Restrictions.eq("budgetOwner.id", partnerId),
						Restrictions.eq("user.id", partnerId)))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("costState.id", type)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartnerAndBudgedOwner(String projectId, Long partnerId, Long type)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(Restrictions.eq("user.id", partnerId),
						Restrictions.eq("budgetOwner.id", partnerId)))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("costState.id", type)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByCFWithPartner(String projectId, Long partnerId, Long[] types)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(
						Restrictions.and(Restrictions.eq("user.id", partnerId), Restrictions.in("costState.id", types)),
						Restrictions.and(Restrictions.eq("costState.id", CostStateTypes.CFValidation.getState()),
								Restrictions.eq("createdByPartner", true))))
				.addOrder(Order.desc("createDate")).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetExcludePartner(String projectId, Long partnerId, CostStateTypes type)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.not(Restrictions.eq("user.id", partnerId))).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("costState.id", type.getState())).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartners(String projectId, Long[] partnerIds, CostStateTypes type)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("dismiss", false))
				.add(Restrictions.in("user.id", partnerIds)).addOrder(Order.desc("createDate"))
				.add(Restrictions.eq("costState.id", type.getState())).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetByPartners(String projectId, Long[] partnerIds, Long[] types)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("dismiss", false))
				.add(Restrictions.in("user.id", partnerIds)).addOrder(Order.desc("createDate"))
				.add(Restrictions.in("costState.id", types)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgus(String projectId, CostStateTypes type) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("createdByAGU", true)).add(Restrictions.eq("costState.id", type.getState()))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgus(String projectId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("dismiss", false)).add(Restrictions.eq("createdByAGU", true)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgusWithPartners(String projectId) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				/*
				 * .add(Restrictions.or( Restrictions.eq("createdByAGU", true),
				 * Restrictions.or( Restrictions.and(
				 * Restrictions.eq("costState.id", CostStateTypes.CFValidation
				 * .getState()), Restrictions.eq("createdByPartner", true)),
				 * Restrictions.and(Restrictions.eq( "refucedFromDur", true),
				 * Restrictions .eq("createdByPartner", true)))))
				 */ .list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgus(String projectId, String costItemId, Long id) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("createdByAGU", true))
				.add(Restrictions.eq("costItem.id", Long.parseLong(costItemId)))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState()))
				.add(Restrictions.ne("id", id == null ? 0 : id)).add(Restrictions.eq("dismiss", false)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgusPhase(String projectId, String costItemId, Long id)
			throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("createDate")).add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.eq("createdByAGU", true))
				.add(Restrictions.eq("costItem.id", Long.parseLong(costItemId)))
				// .add(Restrictions.eq("costDefinitionPhase.id", phaseId))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedCIL.getState()))
				.add(Restrictions.ne("costState.id", CostStateTypes.RefusedDAEC.getState()))
				.add(Restrictions.ne("id", id == null ? 0 : id)).add(Restrictions.eq("dismiss", false)).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgus(String projectId, Long[] types) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("createdByAGU", true))
				.add(Restrictions.in("costState.id", types)).addOrder(Order.desc("createDate")).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAgusWithPartners(String projectId, Long[] types) throws PersistenceBeanException {
		List<CostDefinitions> list = PersistenceSessionManager.getBean().getSession()
				.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("project.id", Long.valueOf(projectId)))
				.add(Restrictions.or(
						Restrictions.and(Restrictions.eq("createdByAGU", true), Restrictions.in("costState.id", types)),
						Restrictions.and(Restrictions.eq("costState.id", CostStateTypes.CFValidation.getState()),
								Restrictions.eq("createdByPartner", true))))
				.addOrder(Order.desc("createDate")).list();

		return list;
	}

	public long GetMaxId(String projectId)
			throws PersistenceBeanException, HibernateException, PersistenceBeanException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT MAX(cd.progressiveId) ");
		sb.append("FROM CostDefinitions cd ");
		sb.append("WHERE cd.project.id = :projectId AND cd.deleted = false ");

		Query query = PersistenceSessionManager.getBean().getSession().createQuery(sb.toString());
		query.setParameter("projectId", Long.valueOf(projectId));
		Long id = (Long) query.uniqueResult();

		return id != null ? id + 1 : 1;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetSuspended(String projectId) throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));

		criteria.add(Restrictions.isNotNull("suspensionStatus"));

		criteria.addOrder(Order.desc("createDate"));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetAllWithSuspensionStatus() throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));

		criteria.add(Restrictions.isNotNull("suspensionStatus"));

		criteria.addOrder(Order.desc("createDate"));

		return criteria.list();
	}

	/**
	 * @param projectId
	 * @param stc
	 * @param acu
	 * @param agu
	 * @return
	 * @throws PersistenceBeanException
	 */
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetRectified(String projectId, boolean stc, boolean acu, boolean agu)
			throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.addOrder(Order.desc("createDate")).list();

		criteria.add(Restrictions.or(Restrictions.eq("rectifiedBySTC", true),
				Restrictions.or(Restrictions.eq("rectifiedByAGU", true), Restrictions.eq("rectifiedByACU", true))));
		criteria.addOrder(Order.desc("rectificationDate"));
		/*
		 * if (stc) { criteria.add(Restrictions.eq("rectifiedBySTC", stc)); }
		 * else if (agu) { criteria.add(Restrictions.or(
		 * Restrictions.eq("rectifiedByAGU", agu), Restrictions.eq(
		 * "rectificationState",
		 * RectificationStateTypes.SentToAgu.getState())));
		 * 
		 * } else if (acu) { criteria.add(Restrictions.or(
		 * Restrictions.eq("rectifiedByACU", acu), Restrictions.eq(
		 * "rectificationState",
		 * RectificationStateTypes.SentToAcu.getState()))); }
		 */

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllForThisProjectWithSameLocation(LocationForCostDef location, String projectId)
			throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.add(Restrictions.eq("location", location));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllWithCurrentLocations(List<LocationForCostDef> locationsForLoading)
			throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.or(Restrictions.eq("location", locationsForLoading.get(0)),
				Restrictions.eq("location", locationsForLoading.get(1))));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllWithCurrentLocationsForProject(List<LocationForCostDef> locationsForLoading,
			String projectId) throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));

		criteria.add(Restrictions.or(Restrictions.eq("location", locationsForLoading.get(0)),
				Restrictions.eq("location", locationsForLoading.get(1))));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllWithExpendituresOutsideEligibleAreasForProject(String projectId)
			throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.add(Restrictions.eq("expenditureOutsideEligibleAreas", true));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllWithExpendituresOutsideEligibleAreasAmountForProject(String projectId)
			throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.add(Restrictions.gt("expenditureOutsideEligibleAreasAmount", 0d));

		return criteria.list();
	}

	/**
	 * @param projectId
	 * @return
	 * @throws PersistenceBeanException
	 */
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> GetForAAU(String projectId) throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.add(Restrictions.eq("ACUSertified", true));
		return criteria.list();
	}

	public Long GetCountByPartners(String projectId, Long[] partnerIds, CostStateTypes type)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		Long count = 0l;

		if (projectId != null && partnerIds != null && partnerIds.length > 0 && type != null) {
			count = Long.parseLong(String.valueOf(PersistenceSessionManager.getBean().getSession()
					.createCriteria(CostDefinitions.class).add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("project.id", Long.valueOf(projectId))).add(Restrictions.eq("dismiss", false))
					.add(Restrictions.in("user.id", partnerIds)).add(Restrictions.eq("costState.id", type.getState()))
					.setProjection(Projections.countDistinct("id")).uniqueResult()));
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllReimbursedCostDefinitions(String projectId) throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
		criteria.add(Restrictions.eq("recoverededByAGUACU", true));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getAllReimbursedCostDefinitions() throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(CostDefinitions.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("recoverededByAGUACU", true));

		return criteria.list();
	}

	public void Save(CostDefinitions objEntity, DurCompilations durCompilation)
			throws HibernateException, PersistenceBeanException {
		setDurCompilation(durCompilation);
		super.Save(objEntity);
	}

	public void SaveInTransaction(CostDefinitions objEntity, DurCompilations durCompilation)
			throws HibernateException, PersistenceBeanException {
		setDurCompilation(durCompilation);
		SaveInTransaction(objEntity);
	}

	public void SaveInTransaction(CostDefinitions objEntity) throws HibernateException, PersistenceBeanException {
		if (getDurCompilation() == null) {
			setDurCompilation(objEntity.getDurCompilation());
		}
		recalculateDurAndSave(objEntity);
		super.SaveInTransaction(objEntity);
	}

	public void recalculateDurAndSave(CostDefinitions objEntity) {
		if (objEntity == null || durCompilation == null) {
			return;
		}
		double zoneCurrentTotalValue = 0d;
		try {

			List<CostDefinitions> listCD = new ArrayList<CostDefinitions>();
			// if(durCompilation.getId()!=null){
			listCD.addAll(BeansFactory.CostDefinitions().GetByDurCompilation(durCompilation.getId()));
			// }
			// else{
			// for(CostDefinitions cd :durCompilation.getCostDefinitions()){
			// CostDefinitions cds =
			// BeansFactory.CostDefinitions().Load(cd.getId());
			// listCD.add(cds);
			// }
			// }
			if (durCompilation.getDurState() == null
					|| DurStateTypes.Create.getValue().equals(durCompilation.getDurState().getId())
					|| DurStateTypes.STCEvaluation.getValue().equals(durCompilation.getDurState().getId())) {
				for (CostDefinitions cost : listCD) {
					// if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
					// continue;
					// }
					if (cost.getId() == objEntity.getId()) {
						cost = objEntity;
					}
					// if (LocationForCostDef.PALERMO.equals(cost.getLocation())
					// || LocationForCostDef.CATANIA
					// .equals(cost.getLocation()))
					if (cost.getExpenditureOutsideEligibleAreasAmount() != null
							&& cost.getExpenditureOutsideEligibleAreasAmount() != 0) {
						if (cost.getCfCheck() != null) {
							zoneCurrentTotalValue += cost.getCfCheck();
							if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
									&& cost.getCfCheckAdditionalFinansingAmount() != null) {
								zoneCurrentTotalValue -= cost.getCfCheckAdditionalFinansingAmount();
							}
						} else {
							zoneCurrentTotalValue += cost.getCilCheck() != null ? cost.getCilCheck() : 0d;
							if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
									&& cost.getCilCheckAdditionalFinansingAmount() != null) {
								zoneCurrentTotalValue -= cost.getCilCheckAdditionalFinansingAmount();
							}
						}
					}
				}
			} else if (DurStateTypes.AGUEvaluation.getValue().equals(durCompilation.getDurState().getId())) {
				for (CostDefinitions cost : listCD) {
					// if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
					// continue;
					// }
					if (cost.getId() == objEntity.getId()) {
						cost = objEntity;
					}
					// if (LocationForCostDef.PALERMO.equals(cost.getLocation())
					// || LocationForCostDef.CATANIA
					// .equals(cost.getLocation()))
					if (cost.getExpenditureOutsideEligibleAreasAmount() != null
							&& cost.getExpenditureOutsideEligibleAreasAmount() != 0) {
						zoneCurrentTotalValue += cost.getStcCertification() != null
								? cost.getStcCertification().doubleValue() : 0d;
						if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
								&& cost.getStcCheckAdditionalFinansingAmount() != null) {
							zoneCurrentTotalValue -= cost.getStcCheckAdditionalFinansingAmount();
						}
					}
				}
			} else if (DurStateTypes.ACUEvaluation.getValue().equals(durCompilation.getDurState().getId())) {
				for (CostDefinitions cost : listCD) {
					// if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
					// continue;
					// }
					if (cost.getId() == objEntity.getId()) {
						cost = objEntity;
					}
					// if (LocationForCostDef.PALERMO.equals(cost.getLocation())
					// || LocationForCostDef.CATANIA
					// .equals(cost.getLocation()))
					if (cost.getExpenditureOutsideEligibleAreasAmount() != null
							&& cost.getExpenditureOutsideEligibleAreasAmount() != 0) {
						zoneCurrentTotalValue += cost.getAguCertification() != null
								? cost.getAguCertification().doubleValue() : 0d;
						if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
								&& cost.getAguCheckAdditionalFinansingAmount() != null) {
							zoneCurrentTotalValue -= cost.getAguCheckAdditionalFinansingAmount();
						}
					}
				}
			} else if (DurStateTypes.ACUEvaluation.getValue().doubleValue() < durCompilation.getDurState().getId()
					.doubleValue()) {
				for (CostDefinitions cost : listCD) {
					// if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
					// continue;
					// }
					if (cost.getId() == objEntity.getId()) {
						cost = objEntity;
					}
					// if (LocationForCostDef.PALERMO.equals(cost.getLocation())
					// || LocationForCostDef.CATANIA
					// .equals(cost.getLocation()))
					if (cost.getExpenditureOutsideEligibleAreasAmount() != null
							&& cost.getExpenditureOutsideEligibleAreasAmount() != 0) {
						zoneCurrentTotalValue += cost.getAguCertification() != null
								? cost.getAguCertification().doubleValue() : 0d;
						if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
								&& cost.getAcuCheckAdditionalFinansingAmount() != null) {
							zoneCurrentTotalValue -= cost.getAcuCheckAdditionalFinansingAmount();
						}
					}
				}
			}
			durCompilation.setZoneCurrentTotal(zoneCurrentTotalValue * 0.85);
			BeansFactory.DurCompilations().SaveInTransaction(durCompilation);
			setDurCompilation(null);
		} catch (Exception e) {
		}
	}

	public void setDurCompilation(DurCompilations durCompilation) {
		this.durCompilation = durCompilation;
	}

	public DurCompilations getDurCompilation() {
		return durCompilation;
	}

}
