/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2015.
 *
 */
public enum PhaseAsse3Types {
	// edited 2019/02/26
	COMITE_MIXTE_DE_SUIVI, 
	COMITES_DE_SELECTION_DES_PROJETS, 
	AUTORITE_DE_GESTION, 
	STC_ET_ANTENNE, 
	COMMUNICATION, 
	AUTORITE_DAUDIT, 
	POINT_DE_CONTROLE, 
	POINT_DE_CONTRACT_NATIONALE, 
	CONTINGENCE;

	public String toString() {
		return Utils.getString(this.getClass().getSimpleName() + this.name());
	}
}
