package com.infostroy.paamns.common.enums;

/**
 * TC46 model.
 * 
 * @author Ingloba360
 *
 */
public enum ProjectIter {

	STIPULATION(1, "procedureInsertStep1Descrition", "0101"),
	EXECUTION(2, "procedureInsertStep2Descrition", "0202"),
	//TEST(3, "procedureInsertStep3Descrition", "0307"),
	COMPLETION(3, "procedureInsertStep4Descrition", null);
	//TODO omessi gli altri valori
	
	public int code;
	public String description;
	public String codPhase;
	
	private ProjectIter(int code, String description, String codPhase) {
		this.code = code;
		this.description = description;
		this.codPhase = codPhase;
	}
}
