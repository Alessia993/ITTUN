package com.infostroy.paamns.common.enums;

public enum RequestType {
	
	IntermediateFinal("intermediate final", 3l),
	Intermediate("intermediate", 1l),
	Final("final", 2l);

	private String code;

	private Long id;

	private RequestType(String code, Long id) {
		this.code = code;
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
