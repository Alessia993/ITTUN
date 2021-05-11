/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "cost_items")
public class CostItems extends LocalizableEnumeration {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = 769595377171475435L;

	@Column(name = "code")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
