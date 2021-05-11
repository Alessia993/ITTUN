package com.infostroy.paamns.common.enums;

/**
 * TC15 model.
 * 
 * @author Ingloba360
 *
 */
public enum ImplementingTool {

	PO14_20("01", "tc15.po14_20")/*,
	PO07_13("2", "tc15.po07_13"),
	FSC14_20("3", "tc15.fsc14_20"),
	APQUAR1("APQUAR1", "tc15.iti_apq"),
	ATPONRI("ATPONRI", "tc15.pon_research_innovation")*/;
	//TODO omessi gli altri valori
	
	public String code;
	public String description;
	
	private ImplementingTool(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
