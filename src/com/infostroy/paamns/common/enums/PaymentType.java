package com.infostroy.paamns.common.enums;

/**
 * FN06 payment type model.
 * 
 * @author Ingloba360
 *
 */
public enum PaymentType {

	PAYMENT("P", "fn06.payment"),
	CORRECTION("R", "fn06.correction"),
	PAYMENT_FINANCIAL("P-TR", "fn06.payment_financial"),
	CORRECTION_FINANCIAL("R-TR", "fn06.correction_financial");
	
	public String code;
	public String description;
	
	private PaymentType(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
