/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.SQLQuery;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequestItem;

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2012.
 * 
 */
public class PaymentRequestItemBean extends
		PersistenceEntityBean<PaymentRequestItem>
{
	public PaymentRequestItem getByDurCompilationId(long durCompilationId)
			throws PersistenceBeanException, PersistenceBeanException
	{
		String query = "select pri.* from payment_request_item as pri where pri.dur_compilation_id= :durCompilationId";

		SQLQuery q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(query).addEntity(PaymentRequestItem.class);

		q.setLong("durCompilationId", durCompilationId);

		return (PaymentRequestItem) q.uniqueResult();
	}
}
