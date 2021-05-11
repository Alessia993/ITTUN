package com.infostroy.paamns.persistence.beans.facades;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ExpenditureDeclarationStatusTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclaration;

public class ExpenditureDeclarationBean extends PersistenceEntityBean<ExpenditureDeclaration> {

	public List<ExpenditureDeclaration> LoadAllSubmittedExpenditure() throws PersistenceBeanException{
		Criteria criterion =PersistenceSessionManager.getBean().getSession().createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.eq("expenditureDeclarationState.id", BeansFactory.ExpenditureDeclarationStates()
				.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue()).getId()));
		criterion.add(Restrictions.eq("deleted", false));
		criterion.addOrder(Order.asc("submissionDate"));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclaration> LoadAllWithoutSubmittedStatus() throws PersistenceBeanException{
		Criteria criterion =PersistenceSessionManager.getBean().getSession().createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.ne("expenditureDeclarationState.id", BeansFactory.ExpenditureDeclarationStates()
				.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue()).getId()));
		criterion.add(Restrictions.eq("deleted", false));
		return criterion.list();
	}
	
	public List<ExpenditureDeclaration> LoadAllWithoutSubmittedStatusAndExcludingExpenditureDeclaration(Long expenditureDeclarationId) throws PersistenceBeanException{
		Criteria criterion =PersistenceSessionManager.getBean().getSession().createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.ne("expenditureDeclarationState.id", BeansFactory.ExpenditureDeclarationStates()
				.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue()).getId()));
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.ne("id", expenditureDeclarationId));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclaration> LoadByProject(String projectId)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		return PersistenceSessionManager.getBean().getSession().createCriteria(ExpenditureDeclaration.class).list();
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclaration> LoadAll(String id, Date compilationDate, String protocolNumber,
			Date protocolDate, String expenditureIncrease, String totalExpenditure, String type)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Calendar c = new GregorianCalendar();
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.eq("deleted", false));
		if (id != null && !id.trim().isEmpty()) {
			criterion.add(Restrictions.eq("id", Long.valueOf(id)));
		}
		if (compilationDate != null) {
			c.setTime(compilationDate);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);

			criterion.add(Restrictions.ge("compilationDate", compilationDate));
			criterion.add(Restrictions.le("compilationDate", c.getTime()));
		}
		if (protocolNumber != null && !protocolNumber.trim().isEmpty()) {
			criterion.add(Restrictions.like("protocolNumber", protocolNumber.trim()));
		}
		if (protocolDate != null) {
			c.setTime(protocolDate);
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);

			criterion.add(Restrictions.ge("protocolDate", protocolDate));
			criterion.add(Restrictions.le("protocolDate", c.getTime()));
		}
		if (expenditureIncrease != null && !expenditureIncrease.trim().isEmpty()) {
			criterion.add(Restrictions.eq("expenditureIncrease", Double.valueOf(expenditureIncrease)));
		}
		if (totalExpenditure != null && !totalExpenditure.trim().isEmpty()) {
			criterion.add(Restrictions.eq("totalExpenditure", Double.valueOf(totalExpenditure)));
		}
		
		if (type != null && !type.trim().isEmpty() && !type.trim().equals("")) {
			Long typeId = Long.valueOf(type);
			if(typeId!=0){
			criterion.add(Restrictions.eq("expenditureDeclarationState.id", typeId));
			}
		}
		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclaration> LoadAll()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.eq("deleted", false));
		return criterion.list();
	}

	@SuppressWarnings("unchecked")
	public ExpenditureDeclaration LoadById(Long id)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(ExpenditureDeclaration.class);
		criterion.add(Restrictions.eq("id", id));
		return (ExpenditureDeclaration) criterion.uniqueResult();
	}

}
