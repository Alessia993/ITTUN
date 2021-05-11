/**
 * 
 */
package com.infostroy.paamns.web.beans.dynamicdatastructure;

import java.util.ArrayList;
import java.util.List;

import com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3;

/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class DataStructureRow 
{
	
	public Boolean total;
	
	public List<CostItemAsse3> getItems()
	{
		return items;
	}

	public void setItems(List<CostItemAsse3> items)
	{
		this.items = items;
	}

	private List<CostItemAsse3>	items;

	public List<DataStructureColumn> getColumns()
	{
		if (items != null && !items.isEmpty())
		{
			List<DataStructureColumn> cols = new ArrayList<DataStructureColumn>();
			int i = 0;
			for (CostItemAsse3 item : items)
			{
				cols.add(new com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureColumn(new Integer(i), item
						.getCostPhase().getValue()));
				i++;
			}
			return cols;
		}
		else
		{
			return new ArrayList<DataStructureColumn>();
		}
	}

	public String getTabName()
	{
		if (items != null && !items.isEmpty())
		{
			return items.get(0).getCostPhase().getPhase().toString();
		}
		else
		{
			return null;
		}
	}

	public Boolean getTotal()
	{
		return total;
	}

	public void setTotal(Boolean total)
	{
		this.total = total;
	}
	
	
}
