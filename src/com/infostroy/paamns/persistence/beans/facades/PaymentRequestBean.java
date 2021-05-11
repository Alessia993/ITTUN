/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2012.
 * 
 */
public class PaymentRequestBean extends PersistenceEntityBean<PaymentRequest>
{
	@SuppressWarnings("unchecked")
	public List<PaymentRequest> loadForList() throws PersistenceBeanException,
			PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
				.createCriteria(PaymentRequest.class)
				.add(Restrictions.eq("deleted", false));
		criterion.addOrder(Order.asc("number"));
		List<PaymentRequest> list = criterion.list();
		return list;
	}

	public List<PaymentRequest> loadPaymentRequestsWithPrevious(
			long paymentRequestNumber) throws PersistenceBeanException,
			PersistenceBeanException
	{
		String query = "select * from payment_request as pr where pr.number <= :number order by pr.number desc";

		SQLQuery q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(query).addEntity(PaymentRequest.class);

		q.setLong("number", paymentRequestNumber);

		@SuppressWarnings("unchecked")
		List<PaymentRequest> list = q.list();
		return list;
	}

	public PaymentRequest getWithMaxNumber() throws PersistenceBeanException,
			PersistenceBeanException
	{
		String query = "SELECT * FROM payment_request HAVING MAX(payment_request.number)";

		SQLQuery q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(query).addEntity(PaymentRequest.class);

		return (PaymentRequest) q.uniqueResult();
	}
}
