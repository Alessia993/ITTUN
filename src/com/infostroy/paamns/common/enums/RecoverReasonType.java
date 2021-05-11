package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum RecoverReasonType {
	UE_RECOVER(1l),
	SUSPENSION_FOR_JUDICIAL_AND_ADMINISTRATIVE_PROCEEDINGS(2l),
	PARTIAL_OR_TOTAL_SUPPRESSION(3l),
	CLERICAL_ERROR(4l),
	INELIGIBILITY_AND_DISQUALIFICATION(5l),
	IRREGULARITY(6l),
	FRAUD(7l),
	OTHER(8l);
	
	private Long id;
	
	private RecoverReasonType(Long id)
	{
		this.id = id;
	}
	
	@Override
	public String toString() {
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}

	public Long getId() {
		return id;
	}

}
