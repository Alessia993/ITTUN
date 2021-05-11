package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum SpecificObjective 
{
	SPECIFIC_OBJECTIVE_1_1, SPECIFIC_OBJECTIVE_2_1, SPECIFIC_OBJECTIVE_2_2, SPECIFIC_OBJECTIVE_3_1, SPECIFIC_OBJECTIVE_3_2;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
