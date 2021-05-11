package com.infostroy.paamns.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * TC39 model.
 * 
 * @author Ingloba360
 *
 */
public class PaymentCausality {
	
	public static Map<String, String> getPaymentCausality(String paymentTypeCode) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (PaymentType.PAYMENT.code.equals(paymentTypeCode) || PaymentType.PAYMENT_FINANCIAL.code.equals(paymentTypeCode)) {
			//TC39 payment model
			map.put("ANT", "tc39.advance");
			map.put("INT", "tc39.intermediate");
			map.put("SLD", "tc39.balance");
		} else if (PaymentType.CORRECTION.code.equals(paymentTypeCode) || PaymentType.CORRECTION_FINANCIAL.code.equals(paymentTypeCode)) {
			//TC39 correction model
			map.put("ALT", "tc39.other");
			map.put("SNA", "tc39.ineligible");
		}
		
		return map;
	}
}
