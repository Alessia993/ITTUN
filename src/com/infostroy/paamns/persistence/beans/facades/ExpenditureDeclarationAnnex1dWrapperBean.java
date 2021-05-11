package com.infostroy.paamns.persistence.beans.facades;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.web.beans.wrappers.ExpenditureDeclarationAnnex1dWrapper;

public class ExpenditureDeclarationAnnex1dWrapperBean {

	
	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclarationAnnex1dWrapper> getAllAfterSubmissionDate(Date submissionDate,Long expenditureDeclarationId) throws HibernateException, PersistenceBeanException{
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("cost_definitions.id as id, ");
		sb.append("cost_definitions.create_date as create_date, ");
		sb.append("cost_definitions.deleted as deleted, ");
		sb.append("cost_definitions.payment_date as payment_date, ");
		sb.append("cost_definitions.payment_amount as payment_amount, ");
		sb.append("cost_definitions.tot_amount_eligl_exp_retired as retired_amount, ");
		sb.append("cost_definitions.retreat_act_number as retreat_act_number, ");
		sb.append("cost_definitions.retreat_date as retreat_date_decertification, ");
		sb.append("cost_definitions.retreat_act_date as retired_date, ");//, ");
		sb.append("cost_definitions.retreat_reason_id as motivation_id, ");
		sb.append("cost_definitions.suspend_reason_id as suspend_reason_id, ");
		sb.append("projects.asse as axis, specific_goals.code as specific_objective ,");
		sb.append("projects.cup_code as cup, projects.id as local_code, ");
		sb.append("projects.code as system_code, projects.title as title, ");
		sb.append("payment_ways.id as document_type_id, cost_definitions.payment_number as payment_number, ");
		sb.append("cost_definitions.suspended_by_acu as suspended_by_acu, ");
		sb.append("cost_definitions.rectified_by_acu as rectified_by_acu, ");
		sb.append("cost_definitions.recovered_by_agu_acu as recovered_by_agu_acu, ");
		sb.append("cost_definitions.suspend_act_number as suspend_act_number, ");
		sb.append("cost_definitions.date_suspend_acu as date_suspend_acu, ");
		sb.append("cost_definitions.suspend_act_date as suspend_act_date, ");
		sb.append("cost_definitions.dur_recovery_act_number as dur_recovery_act_number, ");
		sb.append("cost_definitions.dur_recovery_act_date as dur_recovery_act_date, ");
		sb.append("cost_definitions.dur_recovery_reason_id as dur_recovery_reason_id, ");
		sb.append("cost_definitions.dur_Tot_eligible_expenses_public_support_reimbursed as dur_Tot_eligible_expenses_public_support_reimbursed, ");
		sb.append("cost_definitions.dur_not_regular_is_recoverable as dur_not_regular_is_recoverable, ");
		sb.append("cost_definitions.dur_not_regular_act_number as dur_not_regular_act_number, ");
		sb.append("cost_definitions.dur_not_regular_act_date as dur_not_regular_act_date, ");
		sb.append("cost_definitions.cost_recovery as cost_recovery, ");
		sb.append("cost_definitions.tot_eligible_expenses_public_support_non_recoverable as tot_eligible_expenses_public_support_non_recoverable ");
		
		sb.append("from cost_definitions ");
		sb.append("inner join expenditure_declaration_dur_compilations on expenditure_declaration_dur_compilations.durCompilations_id=cost_definitions.dur_compilations_id  ");
		sb.append("inner join expenditure_declaration on expenditure_declaration.id = expenditure_declaration_dur_compilations.expenditure_declaration_id  ");
		sb.append("left join projects on projects.id = cost_definitions.project_id ");
		sb.append("left join specific_goals on specific_goals.id=projects.specific_goal_id ");
		sb.append("left join payment_ways on cost_definitions.payment_way_id=payment_ways.id ");
		sb.append("where ");
		sb.append("cost_definitions.deleted = false ");
		sb.append("and expenditure_declaration.deleted = false ");
		sb.append("and expenditure_declaration_dur_compilations.deleted = false ");
		sb.append("and cost_definitions.acu_certification_date is not null ");
		sb.append("and expenditure_declaration.submission_date >= :submissionDate ");
		sb.append("and expenditure_declaration.id = :expenditure_declaration_id "); 
		sb.append("order by projects.asse asc");

		Query q = PersistenceSessionManager.getBean().getSession().createSQLQuery(sb.toString()).addEntity(ExpenditureDeclarationAnnex1dWrapper.class);
		//q.setDate("submissionDate", submissionDate);
		q.setParameter("submissionDate", submissionDate);
		q.setLong("expenditure_declaration_id", expenditureDeclarationId);
		return (List<ExpenditureDeclarationAnnex1dWrapper>) q.list();
	}
}
