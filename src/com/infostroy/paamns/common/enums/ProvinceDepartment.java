package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.helpers.EnumHelper;
import com.infostroy.paamns.common.utils.Utils;

public enum ProvinceDepartment 
{
	PALERMO, TRAPANI, CALTANISSETTA, AGRIGENTO, RAGUSA, SIRACUSA, ENNA, CATANIA, MESSINA, ROMA, ITALIA, TUNISIA, 
	ARIANA, BEJA, BENAROUS, BIZERTE, GABES, GAFSA, JENDOUBA, KAIROUAN, KASSERINE, KEBILI, LEKEF, MAHDIA, MANUBAH, 
	MEDENINE, MONASTIR, NABEUL, SFAX, SIDIBOUZID, SILIANA, SOUSSE, TATAOUINE, TOZEUR, TUNIS, ZAGHOUAN, OTHER;
	
	@Override
	public String toString()
	{
		return Utils.getString(EnumHelper.toStringFormatter(this));
	}
}
