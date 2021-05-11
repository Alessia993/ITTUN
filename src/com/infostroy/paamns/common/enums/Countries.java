package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum Countries 
{
	ITALY, TUNISIA;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
