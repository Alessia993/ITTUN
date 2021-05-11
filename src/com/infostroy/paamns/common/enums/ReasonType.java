package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum ReasonType
{
	SUSPENSION_FOR_JUDICIAL_AND_ADMINISTRATIVE_PROCEEDINGS(1l),
	PARTIAL_OR_TOTAL_SUPPRESSION(2l),
	CLERICAL_ERROR(3l),
	INELIGIBILITY_AND_DISQUALIFICATION(4l),
	IRREGULARITY(5l),
	FRAUD(6l),
	OTHER(7l);
	
	private Long id;
	
	private ReasonType(Long id)
	{
		this.id = id;
	}
	
	public String toString(Boolean isSuspend)
	{
		if (isSuspend == null || isSuspend || !this.getId().equals(1l))
		{
			return this.toString();
		}
		else
		{
			return Utils.getString(
					"reasonTypeRETREAT_TO_JUDICIAL_AND_ADMINISTRATIVE_PROCEEDINGS");
		}

	}
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}

	public Long getId()
	{
		return id;
	}
}
