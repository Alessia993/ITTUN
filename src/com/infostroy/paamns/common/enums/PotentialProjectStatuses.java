package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum PotentialProjectStatuses 
{
	FROM_APPROVE, APPROVED, REJECTED, PROJECT_ELIGIBLE_FOR_FUNDING, ELIGIBLE_PROJECTS_FOR_FUNDING_UNDER_CONDITION, PERMISSIBLE_BUT_NOT_ELIGIBLE_PROJECT, PROJECT_REJECTED_NOT_ALLOWED;;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
