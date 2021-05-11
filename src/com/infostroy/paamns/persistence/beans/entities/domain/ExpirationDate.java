package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "expiration_date")
public class ExpirationDate extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1859250136555054657L;
	
	@Column(name = "expiration_date")
    private Date expirationDate;

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
