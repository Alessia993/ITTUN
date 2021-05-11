package com.infostroy.paamns.common.enums;

/**
 * TC11 model.
 * 
 * @author Ingloba360
 *
 */
public enum ClassificationType {

	ECONOMIC_ACTIVITY("AE", "tc11.economic_activity"),
	OTHER_CLASSIFICATIONS("AL", "tc11.other_classifications"),
	INTERVENTION_FIELD("CI", "tc11.intervention_field"),
	SECONDARY_THEMATIC_DIMENSION("DTS", "tc11.secondary_thematic_dimension"),
	FINANCING_FORMS("FF", "tc11.financing_forms"),
	ACTION_ACTIONLINE("LA", "tc11.action_actionline"),
	TERRITORIAL_DELIVERY_MECHANISMS("MET", "tc11.territorial_delivery_mechanisms"),
	EXPECTED_RESULT("RA", "tc11.expected_result"),
	FEASR_INTERVENTION_TYPE("TI", "tc11.feasr_intervention_type"),
	TERRITORY_TYPE("TT", "tc11.territory_type");
	
	public String code;
	public String description;
	
	private ClassificationType(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
