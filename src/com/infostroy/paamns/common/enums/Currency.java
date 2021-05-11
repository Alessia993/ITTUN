package com.infostroy.paamns.common.enums;

public enum Currency {
	
	EURO("01", "tc17.euro"),
	DINARO("2", "tc17.dinaro")/*,
	FSC14_20("3", "tc15.fsc14_20"),
	APQUAR1("APQUAR1", "tc15.iti_apq"),
	ATPONRI("ATPONRI", "tc15.pon_research_innovation")*/;
	//TODO omessi gli altri valori
	
	public String code;
	public String description;
	
	private Currency(String code, String description) {
		this.code = code;
		this.description = description;
	}	
}
