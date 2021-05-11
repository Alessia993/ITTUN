/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Query;

import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.facades.ProjectsBean;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2012.
 * 
 */

@Entity
@Table(name = "payment_request_item")
public class PaymentRequestItem extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	private static final long	serialVersionUID	= 3831395664818918385L;

	@ManyToOne
	@JoinColumn(name = "payment_request")
	private PaymentRequest		paymentRequest;

	@Column(name = "dur_number")
	private Integer				durNumber;

	@Column(name = "total_amount")
	private Double				totalAmount;

	@Column(name = "annuled_amount")
	private Double				annuledAmount;

	@Column(name = "rectified_amount")
	private Double				rectifiedAmount;

	@ManyToOne
	@JoinColumn(name = "dur_compilation_id")
	private DurCompilations		durCompilations;

	@Transient
	private Long				projectId;

	@Transient
	private Double				transientTotalAmount;

	public PaymentRequest getPaymentRequest()
	{
		return paymentRequest;
	}

	public void setPaymentRequest(PaymentRequest paymentRequest)
	{
		this.paymentRequest = paymentRequest;
	}

	public Integer getDurNumber()
	{
		return durNumber;
	}

	public void setDurNumber(Integer durNumber)
	{
		this.durNumber = durNumber;
	}

	public Double getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public Double getAnnuledAmount()
	{
		return annuledAmount;
	}

	public void setAnnuledAmount(Double annuledAmount)
	{
		this.annuledAmount = annuledAmount;
	}

	public Double getRectifiedAmount()
	{
		return rectifiedAmount;
	}

	public void setRectifiedAmount(Double rectifiedAmount)
	{
		this.rectifiedAmount = rectifiedAmount;
	}

	public DurCompilations getDurCompilations()
	{
		return durCompilations;
	}

	public void setDurCompilations(DurCompilations durCompilations)
	{
		this.durCompilations = durCompilations;
	}

	@Transient
	public String getProjectName()
	{
		try
		{
			if (projectId == null)
			{
				if (getDurCompilations() != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("SELECT cd.project.id ");
					sb.append("FROM CostDefinitions cd ");
					sb.append("WHERE cd.deleted = false AND ");
					sb.append("cd.durCompilation.id = :durCompId");

					Query query = PersistenceSessionManager.getBean()
							.getSession().createQuery(sb.toString());
					query.setParameter("durCompId", getDurCompilations()
							.getId());

					@SuppressWarnings("unchecked")
					List<Long> listIndices = query.list();

					if (listIndices != null && !listIndices.isEmpty())
					{
						projectId = listIndices.get(0);
						Projects project = BeansFactory.Projects().Load(
								listIndices.get(0));
						if (project != null)
						{
							return project.getCode();
						}
					}
				}
			}
			else
			{
				Projects project = BeansFactory.Projects().Load(projectId);
				if (project != null)
				{
					return project.getCode();
				}
			}
		}
		catch (Exception e)
		{
		}
		return "";
	}

	@Transient
	public String getProjectAsse()
	{
		try
		{
			if (projectId == null)
			{
				if (getDurCompilations() != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("SELECT cd.project.id ");
					sb.append("FROM CostDefinitions cd ");
					sb.append("WHERE cd.deleted = false AND ");
					sb.append("cd.durCompilation.id = :durCompId");

					Query query = PersistenceSessionManager.getBean()
							.getSession().createQuery(sb.toString());
					query.setParameter("durCompId", getDurCompilations()
							.getId());

					@SuppressWarnings("unchecked")
					List<Long> listIndices = query.list();

					if (listIndices != null && !listIndices.isEmpty())
					{
						projectId = listIndices.get(0);
						Projects project = BeansFactory.Projects().Load(
								listIndices.get(0));
						if (project != null)
						{
							return String.valueOf(project.getAsse());
						}
					}
				}
			}
			else
			{
				Projects project = BeansFactory.Projects().Load(projectId);
				if (project != null)
				{
					return String.valueOf(project.getAsse());
				}
			}
		}
		catch (Exception e)
		{
		}
		return "";
	}

	public Double getTransientTotalAmount()
	{
		return transientTotalAmount;
	}

	public void setTransientTotalAmount(Double transientTotalAmount)
	{
		this.transientTotalAmount = transientTotalAmount;
	}
}
