package com.infostroy.paamns.common.enums;

public enum FesrCnType {
	FESR85CN15(1L), FESR50CN50(2L);

	private Long value;

	private FesrCnType(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

}
