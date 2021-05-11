package com.infostroy.paamns.persistence.beans.facades;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.PaymentApplicationStatusTypes;
import com.infostroy.paamns.common.enums.RequestType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;

public class PaymentApplicationBean extends PersistenceEntityBean<PaymentApplication>{
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> loadAllNotSubmitted() throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.ne("paymentApplicationState.id", PaymentApplicationStatusTypes.Submitted.getId()));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> loadAllPreviousSubmittedPaymentApplicationBeforeDate(Date date) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.le("submissionDate", date));
		criterion.addOrder(Order.desc("submissionDate"));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> loadAllSubmittedByAccountingPeriod(Date from, Date to) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("paymentApplicationState.id", PaymentApplicationStatusTypes.Submitted.getId()));
		criterion.add(Restrictions.ne("requestType.id", RequestType.IntermediateFinal.getId()));
		criterion.add(Restrictions.eq("accountingPeriodFrom", from));
		criterion.add(Restrictions.eq("accountingPeriodTo", to));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public PaymentApplication selectLastSubmittedPaymentApplication() throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("paymentApplicationState.id", PaymentApplicationStatusTypes.Submitted.getId()));
		criterion.addOrder(Order.desc("id"));
		
		List<PaymentApplication> list = criterion.list();
		
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public PaymentApplication LoadById(Long id)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("id", id));
		return (PaymentApplication) criterion.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> LoadAll()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> LoadByState(Long stateId) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
		criterion.add(Restrictions.eq("deleted", false));
		criterion.add(Restrictions.eq("paymentApplicationState.id", stateId));
		return criterion.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentApplication> LoadAll(String id, Date compilationDate, String protocolNumber,
			String type)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		Calendar c = new GregorianCalendar();
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentApplication.class);
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
		if (type != null && !type.trim().equals("") && !type.trim().isEmpty()) {
			Long typeId = Long.valueOf(type);
			if(typeId!=0){
			criterion.add(Restrictions.eq("paymentApplicationState.id", typeId));
			}
		}
		return criterion.list();
	}

}
