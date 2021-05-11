package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum ProgramPriorityInvestment 
{
	PRIORITY_INVESTMENT_1B, PRIORITY_INVESTMENT_3A, PRIORITY_INVESTMENT_8E, PRIORITY_INVESTMENT_5B, PRIORITY_INVESTMENT_6D;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
