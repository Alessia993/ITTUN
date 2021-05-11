package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum ProgramPriority 
{
	ASSE_PRIORITY_ONE, ASSE_PRIORITY_TWO, ASSE_PRIORITY_THREE;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
