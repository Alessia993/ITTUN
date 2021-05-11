/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2011.
 * 
 */
public class PaymentRequestListBean extends EntityListBean<PaymentRequest>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */
	Long	paymentRequestNumberToView;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setList(BeansFactory.PaymentRequest().loadForList());

		if (this.getList() != null)
		{
			double totalAmount = 0d;
			for (PaymentRequest paymentRequest : this.getList())
			{
				totalAmount += paymentRequest.getTotalAmount();
				paymentRequest.setTransientTotalAmount(totalAmount);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		// TODO Auto-generated method stub

	}

	public void showItem() throws HibernateException, PersistenceBeanException
	{
		if (this.getPaymentRequestNumberToView() != null)
		{
			this.getSession().put("paymentRequestIdShow",
					this.getPaymentRequestNumberToView());

			this.goTo(PagesTypes.PAYMENTREQUESTVIEW);
		}
	}

	public Long getPaymentRequestNumberToView()
	{
		return paymentRequestNumberToView;
	}

	public void setPaymentRequestNumberToView(Long paymentRequestNumberToView)
	{
		this.paymentRequestNumberToView = paymentRequestNumberToView;
	}

}
