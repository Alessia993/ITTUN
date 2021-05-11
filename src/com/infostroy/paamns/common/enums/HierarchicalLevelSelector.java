package com.infostroy.paamns.common.enums;

/**
 * {@link HierarchicalLevel} selector.
 * 
 * @author Ingloba360
 *
 */
public class HierarchicalLevelSelector {
	
	/**
	 * Gets the default hierarchical level for impegno contestuale.
	 * @return default hierarchical level
	 */
	public static HierarchicalLevel getHierarchicalLevelImpegnoContestuale() {
		return HierarchicalLevel.TECHICAL_ASSISTANCE;
	}
	
	/**
	 * TC36 model.
	 * 
	 * @author Ingloba360
	 *
	 */
	public enum HierarchicalLevel {
		
		TECHICAL_ASSISTANCE("2014IT05FMOP001_1", "tc36.technical_assistance"),
		FOOD_POVERTY("2014IT05FMOP001_2", "tc36.food_poverty"),
		MATERIAL_DEPRIVATION_SCHOOL("2014IT05FMOP001_3", "tc36.material_deprivation_school"),
		FOOD_EDUCATIONAL_DEPRIVATION("2014IT05FMOP001_4", "tc36.food_educational_deprivation"),
		MATERIAL_DEPRIVATION_HOMELESS("2014IT05FMOP001_5", "tc36.material_deprivation_homeless");
		//TODO omessi gli altri valori
		
		public String code;
		public String description;
		
		private HierarchicalLevel(String code, String description) {
			this.code = code;
			this.description = description;
		}
	}
}
