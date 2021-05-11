/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author acagnoni
 * InfoStroy Co., 2015.
 *
 */
public enum CostItemsTypology3Types
{
	COST_ITEM_1,
	COST_ITEM_2,
	COST_ITEM_3,
	COST_ITEM_4,
	COST_ITEM_5,
	COST_ITEM_6,
	COST_ITEM_7,
	COST_ITEM_8,
	COST_ITEM_9,
	COST_ITEM_10,
	COST_ITEM_11,
	COST_ITEM_12;
	
	public String toString(){
		return Utils.getString(this.getClass().getSimpleName() + this.name());
	}
}
