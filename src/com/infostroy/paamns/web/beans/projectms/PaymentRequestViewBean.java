/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequestItem;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2011.
 * 
 */
public class PaymentRequestViewBean extends EntityListBean<PaymentRequestItem>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{

		if (this.getSession().containsKey("paymentRequestIdShow"))
		{
			Long number = (Long) this.getSession().get("paymentRequestIdShow");

			if (number != null)
			{
				List<PaymentRequest> plList = BeansFactory.PaymentRequest()
						.loadPaymentRequestsWithPrevious(number.longValue());

				this.setList(new ArrayList<PaymentRequestItem>());

				for (PaymentRequest paymentRequest : plList)
				{
					Double totalAmount = 0d;
					for (PaymentRequestItem item : paymentRequest.getItems())
					{
						totalAmount += item.getTotalAmount();
						item.setTransientTotalAmount(totalAmount);
					}

					this.getList().addAll(paymentRequest.getItems());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
	}

	public void goBack()
	{
		this.goTo(PagesTypes.PAYMENTREQUESTLIST);
	}

}
