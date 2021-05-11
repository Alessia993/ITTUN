package com.infostroy.paamns.common.enums;

public enum TypologyProductIndicator {
	ComunicationProduct(1L, "Comunication Product"), ProjectManagementProduct(2L,
			"Project Management Product"), ResultIndicator(3L,
					"Result Indicator"), OutputIndicator(4L, "Output Indicator");

	private Long code;

	private String descriptio;

	private TypologyProductIndicator(Long code, String descriptio) {
		this.code = code;
		this.descriptio = descriptio;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getDescriptio() {
		return descriptio;
	}

	public void setDescriptio(String descriptio) {
		this.descriptio = descriptio;
	}
}
