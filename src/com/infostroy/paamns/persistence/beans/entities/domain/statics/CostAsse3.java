/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */

@Entity
@Table(name = "cost_asse3")
public class CostAsse3 extends LocalizableStaticEntity 
{
	@Column
	private PhaseAsse3Types phase;
	
	@Column
	private String value;
	
	@Column
	private String description;

	
	
	

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public PhaseAsse3Types getPhase()
	{
		return phase;
	}

	public void setPhase(PhaseAsse3Types phase)
	{
		this.phase = phase;
	}
	
	
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		return new StringBuilder().append(this.getPhase().toString()).append(" : ")
				.append(this.getValue()).toString();
	}
	
	public String getString(){
		return this.toString();
	}
}
