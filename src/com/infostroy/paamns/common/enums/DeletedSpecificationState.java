package com.infostroy.paamns.common.enums;

/**
 * TC14 model.
 * 
 * @author Ingloba360
 *
 */
public enum DeletedSpecificationState {

	CANCELED(3, "tc14.canceled"),
	TRANSFERRED(1, "tc14.transferred"),
	OUTSIDE(2, "tc14.outside"),
	REJECTED(4, "tc14.rejected");
	
	public int code;
	public String description;
	
	private DeletedSpecificationState(int code, String description) {
		this.code = code;
		this.description = description;
	}
}
