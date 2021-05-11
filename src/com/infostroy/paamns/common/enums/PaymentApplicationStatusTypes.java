package com.infostroy.paamns.common.enums;

public enum PaymentApplicationStatusTypes {

	Compiled("compiled", 1l), Generated("generated", 2l), Submitted("submitted", 3l);

	private PaymentApplicationStatusTypes(String val, Long id) {
		this.setValue(val);
		this.setId(id);
	}

	/**
	 * Sets value
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets value
	 * 
	 * @return value the value
	 */
	public String getValue() {
		return value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String value;

	private Long id;
}
