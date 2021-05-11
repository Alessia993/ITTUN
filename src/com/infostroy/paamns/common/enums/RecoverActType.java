package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum RecoverActType {
	DEFINITION(1l), 
	RESOLUTION(2l), 
	DECISION(3l), 
	DECREE(4l), 
	CIRCULAR(5l), 
	OTHER(6l);

	private Long id;

	private RecoverActType(Long id)
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
