/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2014.
 * 
 */
public enum LocationForCostDef
{
	AGRIGENTO("ProvinceOfAgrigento", 0, 0, 0), CALTANISSETTA(
			"ProvinceOfCaltanissetta", 0, 0, 0), PALERMO("ProvinceOfPalermo",
			20, 0, 0), CATANIA("ProvinceOfCatania", 20, 0, 0), TRAPANI(
			"ProvinceOfTrapani", 0, 0, 0), ENNA("ProvinceOfEnna", 0, 0, 0), MESSINA(
			"ProvinceOfMessina", 0, 0, 0), RAGUSA("ProvinceOfRagusa", 0, 0, 0), SYRACUSE(
			"ProvinceOfSyracuse", 0, 0, 0), ITALY(
			"Italy", 0, 60, 50), ROMA("ProvinceOfRome", 0, 0, 0), TUNISIA("Tunisia", 0, 0, 0), 
			ARIANA("ProvinceOfAriana", 0, 0, 0), BEJA("ProvinceOfBeja", 0, 0, 0),
			BENAROUS("ProvinceOfBenArous", 0, 0, 0), BIZERTE("ProvinceOfBizerte", 0, 0, 0), 
			GABES("ProvinceOfGabes", 0, 0, 0), GAFSA("ProvinceOfGafsa", 0, 0, 0), 
			JENDOUBA("ProvinceOfJendouba", 0, 0, 0), KAIROUAN("ProvinceOfKairouan", 0, 0, 0),
			KASSERINE("ProvinceOfKasserine", 0, 0, 0), KEBILI("ProvinceOfKebili", 0, 0, 0),
			LEKEF("ProvinceOfLekef", 0, 0, 0), MAHDIA("ProvinceOfMahdia", 0, 0, 0), 
			MANUBAH("ProvinceOfManubah", 0, 0, 0), MEDENINE("ProvinceOfMedenine", 0, 0, 0),
			MONASTIR("ProvinceOfMonastir", 0, 0, 0), NABEUL("ProvinceOfNabeul", 0, 0, 0),
			SIDIBOUZID("ProvinceOfSidiBouZid", 0, 0, 0), SILIANA("ProvinceOfSiliana", 0, 0, 0),
			SFAX("ProvinceOfSfax", 0, 0, 0), SOUSSE("ProvinceOfSousse", 0, 0, 0), 
			TATAOUINE("ProvinceOfTataouine", 0, 0, 0), TOZEUR("ProvinceOfTozeur", 0, 0, 0), 
			TUNIS("ProvinceOfTunis", 0, 0, 0), ZAGHOUAN("ProvinceOfZaghouan", 0, 0, 0), 
			OTHER("ProvinceOfOtherTerritory", 0, 0, 0);

	LocationForCostDef(String location, Integer limit,
			Integer simpleProjectLimit, Integer strategicProjectLimit)
	{
		this.location = location;
		this.limit = limit;
		this.simpleProjectLimit = simpleProjectLimit;
		this.strategicProjectLimit = strategicProjectLimit;
	}

	private String	location;
	private Integer	limit;
	private Integer	simpleProjectLimit;
	private Integer	strategicProjectLimit;

	/**
	 * Gets location
	 * 
	 * @return location the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * Gets limit
	 * 
	 * @return limit the limit
	 */
	public Integer getLimit()
	{
		return limit;
	}

	/**
	 * Gets simpleProjectLimit
	 * 
	 * @return simpleProjectLimit the simpleProjectLimit
	 */
	public Integer getSimpleProjectLimit()
	{
		return simpleProjectLimit;
	}

	/**
	 * Gets strategicProjectLimit
	 * 
	 * @return strategicProjectLimit the strategicProjectLimit
	 */
	public Integer getStrategicProjectLimit()
	{
		return strategicProjectLimit;
	}

	@Override
	public String toString()
	{
		return Utils.getString(location);
	}

}
