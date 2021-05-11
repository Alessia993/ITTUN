/**
 * 
 */
package com.infostroy.paamns.web.beans.dynamicdatastructure;

import java.util.List;

import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;



/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class DataStructureTab 
{

	private PartnersBudgets budget;
	
	private List<DataStructureTable>	tables;

	
	public List<DataStructureTable> getTables()
	{
		return tables;
	}

	public void setTables(List<DataStructureTable> tables)
	{
		this.tables = tables;
	}

	public PartnersBudgets getBudget()
	{
		return budget;
	}

	public void setBudget(PartnersBudgets budget)
	{
		this.budget = budget;
	}


	
	
}
