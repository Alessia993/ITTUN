package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations;

public class PaymentApplicationDurCompilationsBean extends PersistenceEntityBean<PaymentApplicationDurCompilations>{

	
	@SuppressWarnings("unchecked")
	public List<PaymentApplicationDurCompilations> getAllByPaymentApplication(Long paymentApplicationId) throws PersistenceBeanException{
		Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PaymentApplicationDurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("paymentApplicationId",paymentApplicationId));
        return criterion.list();
	}
}
