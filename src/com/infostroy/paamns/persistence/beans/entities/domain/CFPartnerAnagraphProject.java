/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "cf_partner_anagraphs_projects")
public class CFPartnerAnagraphProject extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	@Column
	private String					naming;

	@Column(name = "removed_from_project", columnDefinition = "BIT(1) NULL DEFAULT FALSE")
	private boolean					removedFromProject;

	@Column(name = "progressive_id")
	private Long					progressiveId;

	/**
	 * Stores project value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Projects				project;

	/**
	 * Stores cfPartnerAnagraphs value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn
	private CFPartnerAnagraphs		cfPartnerAnagraphs;

	/**
	 * Stores cil value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private ControllerUserAnagraph	cil;

	/**
	 * Stores daec value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private ControllerUserAnagraph	daec;

	/**
	 * Stores type value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private CFPartnerAnagraphTypes	type;

	@Column(name = "not_assigned", columnDefinition = "BIT(1) NULL DEFAULT FALSE")
	private boolean					notAssigned;

	/**
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project)
	{
		this.project = project;
	}

	/**
	 * Gets project
	 * 
	 * @return project the project
	 */
	public Projects getProject()
	{
		return project;
	}

	/**
	 * Sets cfPartnerAnagraphs
	 * 
	 * @param cfPartnerAnagraphs
	 *            the cfPartnerAnagraphs to set
	 */
	public void setCfPartnerAnagraphs(CFPartnerAnagraphs cfPartnerAnagraphs)
	{
		this.cfPartnerAnagraphs = cfPartnerAnagraphs;
	}

	/**
	 * Gets cfPartnerAnagraphs
	 * 
	 * @return cfPartnerAnagraphs the cfPartnerAnagraphs
	 */
	public CFPartnerAnagraphs getCfPartnerAnagraphs()
	{
		return cfPartnerAnagraphs;
	}

	/**
	 * Sets cil
	 * 
	 * @param cil
	 *            the cil to set
	 */
	public void setCil(ControllerUserAnagraph cil)
	{
		this.cil = cil;
	}

	/**
	 * Gets cil
	 * 
	 * @return cil the cil
	 */
	public ControllerUserAnagraph getCil()
	{
		return cil;
	}

	/**
	 * Sets daec
	 * 
	 * @param daec
	 *            the daec to set
	 */
	public void setDaec(ControllerUserAnagraph daec)
	{
		this.daec = daec;
	}

	/**
	 * Gets daec
	 * 
	 * @return daec the daec
	 */
	public ControllerUserAnagraph getDaec()
	{
		return daec;
	}

	/**
	 * Sets type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(CFPartnerAnagraphTypes type)
	{
		this.type = type;
	}

	/**
	 * Gets type
	 * 
	 * @return type the type
	 */
	public CFPartnerAnagraphTypes getType()
	{
		return type;
	}

	/**
	 * Sets progressiveId
	 * 
	 * @param progressiveId
	 *            the progressiveId to set
	 */
	public void setProgressiveId(Long progressiveId)
	{
		this.progressiveId = progressiveId;
	}

	/**
	 * Gets progressiveId
	 * 
	 * @return progressiveId the progressiveId
	 */
	public Long getProgressiveId()
	{
		return progressiveId;
	}

	/**
	 * Sets naming
	 * 
	 * @param naming
	 *            the naming to set
	 */
	public void setNaming(String naming)
	{
		this.naming = naming;
	}

	/**
	 * Gets naming
	 * 
	 * @return naming the naming
	 */
	public String getNaming()
	{
		return naming;
	}

	/**
	 * Gets removedFromProject
	 * 
	 * @return removedFromProject the removedFromProject
	 */
	public boolean getRemovedFromProject()
	{
		return removedFromProject;
	}

	/**
	 * Sets removedFromProject
	 * 
	 * @param removedFromProject
	 *            the removedFromProject to set
	 */
	public void setRemovedFromProject(boolean removedFromProject)
	{
		this.removedFromProject = removedFromProject;
	}

	/**
	 * Gets notAssigned
	 * 
	 * @return notAssigned the notAssigned
	 */
	public boolean isNotAssigned()
	{
		return notAssigned;
	}

	/**
	 * Sets notAssigned
	 * 
	 * @param notAssigned
	 *            the notAssigned to set
	 */
	public void setNotAssigned(boolean notAssigned)
	{
		this.notAssigned = notAssigned;
	}

}
