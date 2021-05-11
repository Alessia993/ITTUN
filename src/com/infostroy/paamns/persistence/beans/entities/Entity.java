/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2009,2010.
 * 
 */
@MappedSuperclass
public class Entity implements Serializable
{
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= -3584155507869926034L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long	id;

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public boolean isNew()
	{
		if (id == null || id == 0)
		{
			return true;
		}

		return false;
	}

	public boolean getIsNew()
	{
		if (id == null || id == 0)
		{
			return true;
		}

		return false;
	}
}
