package com.infostroy.paamns.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * TC12.X model.
 * 
 * @author Ingloba360
 *
 */
public class ClassificationSpecification {
	
	public static Map<String, String> getClassificationSpecification(String classificationTypeCode) {
		Map<String, String> map = new HashMap<String, String>();
		
		if (ClassificationType.INTERVENTION_FIELD.code.equals(classificationTypeCode)) {
			//TC12.1 model
			map.put("001", "tc12.1.pmi_investment");
			map.put("002", "tc12.1.big_company_research_innovation");
			map.put("003", "tc12.1.big_company_investment");
			map.put("004", "tc12.1.company_investment_tic");
			map.put("005", "tc12.1.electricity");
			//TODO omessi gli altri valori
		} else if (ClassificationType.FINANCING_FORMS.code.equals(classificationTypeCode)) {
			//TC12.2 model
			map.put("01", "tc12.2.lost_fund");
			map.put("02", "tc12.2.refundable");
			map.put("03", "tc12.2.risk");
			map.put("04", "tc12.2.loan");
			map.put("05", "tc12.2.guarantee");
			map.put("06", "tc12.2.technical");
			map.put("07", "tc12.2.awards");
			map.put("08", "tc12.2.not_financial");
		} else if (ClassificationType.TERRITORY_TYPE.code.equals(classificationTypeCode)) {
			//TC12.3 model
			map.put("01", "tc12.3.large_urban_area");
			map.put("02", "tc12.3.small_urban_area");
			map.put("03", "tc12.3.rural_area");
			map.put("04", "tc12.3.macro_regional_area");
			map.put("05", "tc12.3.nactional_cooperation");
			map.put("06", "tc12.3.transnactional_cooperation");
			map.put("07", "tc12.3.not_relevant");
			map.put("I-08", "tc12.3.disadvantaged_area");
			map.put("I-09", "tc12.3.nitrate_area");
			map.put("I-10", "tc12.3.2000_area");
			map.put("I-11", "tc12.3.protected_area");
			map.put("I-12", "tc12.3.hnv_area");
			map.put("I-13", "tc12.3.municipal_aggregation");
		} else if (ClassificationType.TERRITORIAL_DELIVERY_MECHANISMS.code.equals(classificationTypeCode)) {
			//TC12.4 model
			map.put("01", "tc12.4.integrated_investment");
			map.put("02", "tc12.4.urban_investment");
			map.put("03", "tc12.4.other_investment");
			map.put("04", "tc12.4.other_rural");
			map.put("05", "tc12.4.other_urban");
			map.put("06", "tc12.4.local");
			map.put("07", "tc12.4.not_relevant");
		} else if (ClassificationType.ECONOMIC_ACTIVITY.code.equals(classificationTypeCode)) {
			//TC12.5 model
			map.put("01", "tc12.5.agriculture");
			map.put("02", "tc12.5.fishing");
			map.put("03", "tc12.5.food");
			map.put("04", "tc12.5.textile");
			map.put("05", "tc12.5.transport");
			//TODO omessi gli altri valori
		} else if (ClassificationType.SECONDARY_THEMATIC_DIMENSION.code.equals(classificationTypeCode)) {
			//TC12.6 model
			map.put("01", "tc12.6.low_carbon");
			map.put("02", "tc12.6.social");
			map.put("03", "tc12.6.pmi");
			map.put("04", "tc12.6.research");
			map.put("05", "tc12.6.itc");
			map.put("06", "tc12.6.not_discrimination");
			map.put("07", "tc12.6.equality");
			map.put("08", "tc12.6.not_relevant");
		} else if (ClassificationType.EXPECTED_RESULT.code.equals(classificationTypeCode)) {
			//TC12.7 model
			map.put("01.1", "tc12.7.business_innovation");
			map.put("01.2", "tc12.7.innovation_system");
			map.put("01.3", "tc12.7.innovation_market");
			map.put("01.4", "tc12.7.innovative_specialization");
			map.put("01.5", "tc12.7.research_innovation_develop");
			//TODO omessi gli altri valori
		} else if (ClassificationType.FEASR_INTERVENTION_TYPE.code.equals(classificationTypeCode)) {
			//TC12.8 model
			map.put("1.1.11.112A22014IT06RDRP001", "tc12.8.training_course");
			map.put("1.1.11.112A22014IT06RDRP002", "tc12.8.knowledge_transfer");
			map.put("1.1.11.112A22014IT06RDRP003", "tc12.8.training_support_action");
			map.put("1.1.11.112A22014IT06RDRP004", "tc12.8.training_support");
			map.put("1.1.11.112A22014IT06RDRP005", "tc12.8.skill_acquisition");
			//TODO omessi gli altri valori
		} else if (ClassificationType.OTHER_CLASSIFICATIONS.code.equals(classificationTypeCode)) {
			//TC12.9 model
			map.put("CAV0713", "tc12.9.project_between");
			map.put("S01", "tc12.9.aerospace");
			map.put("S02", "tc12.9.agrifood");
			map.put("S03", "tc12.9.blue_growth");
			map.put("S04", "tc12.9.green_chemistry");
			//TODO omessi gli altri valori
		} else if (ClassificationType.ACTION_ACTIONLINE.code.equals(classificationTypeCode)) {
			//TC12.10 model
			map.put("01.101.12014IT16RFOP018", "tc12.10.company_innovation_increment");
			map.put("01.501.52014IT16RFOP018", "tc12.10.research_innovation_capability");
			map.put("03.103.12014IT16RFOP018", "tc12.10.production_system_relaunch");
			map.put("03.503.52014IT16RFOP018", "tc12.10.pmi_born");
			map.put("04.104.12014IT16RFOP018", "tc12.10.energetic_consumption_reduction");
			//TODO omessi gli altri valori
		}
		
		return map;
	}
}
