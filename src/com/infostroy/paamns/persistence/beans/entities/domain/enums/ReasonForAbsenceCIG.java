package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

@Entity
@Table(name = "reasons_for_absence_cig")
public class ReasonForAbsenceCIG extends LocalizableEnumeration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7102813107345715781L;

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
