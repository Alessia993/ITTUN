/**
 *
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.enums.ChangeType;

/**
 * @author Sergii Kravchenko InfoStroy Co., 2013.
 */
@Entity
@Table(name = "super_admin_change")
public class SuperAdminChange extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
		implements Serializable
{
	
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= -5900087194927961L;

	@Column(name = "motivation")
	private String				motivation;

	@Column(length = 1024)
	private String				changes;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users				user;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Projects			project;

	@Enumerated(EnumType.STRING)
	@Column(name = "change_type")
	private ChangeType			changeType;

	/**
	 * 
	 */
	private SuperAdminChange()
	{
	}

	public SuperAdminChange(ChangeType changeType, Users user,
			String actionMotivation, Projects project)
	{
		this.changeType = changeType;
		this.user = user;
		this.motivation = actionMotivation;
		this.project = project;
	}

	/**
	 * Gets motivation
	 * 
	 * @return motivation the motivation
	 */
	public String getMotivation()
	{
		return motivation;
	}

	/**
	 * Sets motivation
	 * 
	 * @param motivation
	 *            the motivation to set
	 */
	public void setMotivation(String motivation)
	{
		this.motivation = motivation;
	}

	public String getChanges()
	{
		return changes;
	}

	public void setChanges(String changes)
	{
		this.changes = changes;
	}

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public Users getUser()
	{
		return user;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(Users user)
	{
		this.user = user;
	}

	public ChangeType getChangeType()
	{
		return changeType;
	}

	public void setChangeType(ChangeType changeType)
	{
		this.changeType = changeType;
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
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project)
	{
		this.project = project;
	}

}
