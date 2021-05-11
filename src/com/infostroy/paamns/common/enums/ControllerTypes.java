package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum ControllerTypes {
	
	REV_UC_ADG(1l),
	ADA_ADC(2l),
	AN(3l),
	AC(4l),
	OTHER(5l);
	
	private Long id;
	
	private ControllerTypes(Long id)
	{
		this.id = id;
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
