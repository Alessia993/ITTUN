/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;

/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */
@Entity
@Table(name="cost_item_asse3")
public class CostItemAsse3 extends PersistentEntity
{

	private static final long	serialVersionUID	= -5091910581839640708L;

	@ManyToOne
	@JoinColumn(name="ca_id")
	private CostAsse3 costPhase;
	
	@Column
	private Double value;
	
	@Column
	private String year;
	
	@ManyToOne
	@JoinColumn(name="pb_id")
	private PartnersBudgets partnersBudgets;

	public CostAsse3 getCostPhase()
	{
		return costPhase;
	}

	public void setCostPhase(CostAsse3 costPhase)
	{
		this.costPhase = costPhase;
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}
	
	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public PartnersBudgets getPartnersBudgets()
	{
		return partnersBudgets;
	}

	public void setPartnersBudgets(PartnersBudgets partnersBudgets)
	{
		this.partnersBudgets = partnersBudgets;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		if (this.getId() != null)
		{
			builder.append("Id: ").append(this.getId());
		}
		if (this.getCostPhase() != null)
		{
			builder.append(" Phase: ").append(this.getCostPhase().getPhase());
			builder.append(" Value: ").append(this.getCostPhase().getValue());
		}
		if (this.getYear() != null)
		{
			builder.append(" Year: ").append(this.getYear());
		}
		if (builder.length() == 0)
		{
			return super.toString();
		}
		else
		{
			return builder.toString();
		}
	}
	
}
