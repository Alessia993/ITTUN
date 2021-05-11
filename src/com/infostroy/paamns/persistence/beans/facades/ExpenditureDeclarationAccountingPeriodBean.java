package com.infostroy.paamns.persistence.beans.facades;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ExpenditureDeclarationStatusTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationAccountingPeriod;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsSpecificGoalsWrapper;

public class ExpenditureDeclarationAccountingPeriodBean
		extends PersistenceEntityBean<ExpenditureDeclarationAccountingPeriod> {

	@SuppressWarnings("unchecked")
	public List<BigInteger> getExpenditureDeclarationIdDistinct(Date accounting_period_from, Date accounting_period_to) throws PersistenceBeanException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct expenditure_declaration.id  ");//DISTINCT expenditure_declaration_id ");
		sb.append("FROM expenditure_declaration_accounting_period ");
		sb.append("left join expenditure_declaration on expenditure_declaration.id = expenditure_declaration_accounting_period.expenditure_declaration_id ");
		sb.append("WHERE ");
		sb.append("expenditure_declaration.deleted = false AND ");
		sb.append("expenditure_declaration.expenditure_declaration_state_id!='3' ");
		sb.append("and expenditure_declaration.accounting_period_from = :accounting_period_from ");
		sb.append("and expenditure_declaration.accounting_period_to = :accounting_period_to");
		Query q = PersistenceSessionManager.getBean().getSession().createSQLQuery(sb.toString());
		q.setDate("accounting_period_from", accounting_period_from);
		q.setDate("accounting_period_to", accounting_period_to);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclarationAccountingPeriod> getAllByExpenditureDeclaration(Long expenditureDeclarationId)
			throws PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ExpenditureDeclarationAccountingPeriod.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("expenditureDeclaration.id", expenditureDeclarationId));
		return criterion.list();
	}
}
