package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum OrganizationType 
{
	LOCAL_AUTHORITY, NGO, CHAMBER_OF_COMMERCE, UNIVERSITY, ETC;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
