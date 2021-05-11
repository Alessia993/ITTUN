package com.infostroy.paamns.common.enums;

/**
 * TC16 model.
 * 
 * @author Ingloba360
 *
 */

public enum MacroStrategy {
	
	EUSAIR("01", "tc16.eusair"),
	WMI("2", "tc16.wmi")/*,
	FSC14_20("3", "tc15.fsc14_20"),
	APQUAR1("APQUAR1", "tc15.iti_apq"),
	ATPONRI("ATPONRI", "tc15.pon_research_innovation")*/;
	//TODO omessi gli altri valori
	
	public String code;
	public String description;
	
	private MacroStrategy(String code, String description) {
		this.code = code;
		this.description = description;
	}	
	
	
	

}
