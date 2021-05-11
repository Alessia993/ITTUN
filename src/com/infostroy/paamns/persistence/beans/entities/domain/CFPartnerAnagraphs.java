package com.infostroy.paamns.persistence.beans.entities.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * modified by Ingloba360
 * 
 */
@Entity
@Table(name = "cf_partner_anagraphs")
public class CFPartnerAnagraphs extends PersistentEntity implements Serializable {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= 5989905052010264238L;

	/**
	 * Stores country value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Countries			country;

	/**
	 * Stores partita value of entity.
	 */
	@Column
	private String				partita;
	
	/**
     * Stores cupCode value of entity.
     */
    @Column(name = "cup_code")
    private String             cupCode;

	/**
	 * Stores name value of entity.
	 */
	@Column
	private String				name;

	/**
	 * Stores user value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Users				user;
	
	@Transient
	private boolean					removedFromProject;
	
	@Column(name = "public_subject")
	private Boolean publicSubject;

	/**
	 * Sets country
	 * 
	 * @param country
	 *            the country to set
	 */
	public void setCountry(Countries country)
	{
		this.country = country;
	}

	/**
	 * Gets country
	 * 
	 * @return country the country
	 */
	public Countries getCountry()
	{
		return country;
	}

	/**
	 * Sets partita
	 * 
	 * @param partita
	 *            the partita to set
	 */
	public void setPartita(String partita)
	{
		this.partita = partita;
	}

	/**
	 * Gets partita
	 * 
	 * @return partita the partita
	 */
	public String getPartita()
	{
		return partita;
	}
	
	 /**
     * Sets cupCode
     * @param cupCode the cupCode to set
     */
    public void setCupCode(String cupCode)
    {
        this.cupCode = cupCode;
    }
    
    /**
     * Gets cupCode
     * @return cupCode the cupCode
     */
    public String getCupCode()
    {
        return cupCode;
    }

	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getName()
	{
		if(this.getUser()!= null && Boolean.TRUE.equals(this.getUser().getFictive())){
			return Utils
					.getString(CountryTypes.Italy.getCountry().equals(
							this.getCountry().getCode()) ? "fictivePartnerAnagraphItalia"
							: "fictivePartnerAnagraphFrancia");
		}
		return name;
	}

	@Transient
	public String getNaming()
	{
		try
		{
			List<String> namings = BeansFactory.CFPartnerAnagraphProject()
					.getNamingForCF(
							String.valueOf(SessionManager.getInstance()
									.getSessionBean().Session.get("project")),
							this.getId());
			if (namings.size() > 0)
			{
				return namings.get(0);
			}
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		return name;
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

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public Users getUser()
	{
		return user;
	}

	public Long getProgressiveId()
	{
		try
		{
			if (SessionManager.getInstance() != null
					&& SessionManager.getInstance().getSessionBean() != null
					&& SessionManager.getInstance().getSessionBean().Session
							.get("project") != null && this.getUser() != null)
			{
				List<CFPartnerAnagraphProject> list = BeansFactory
						.CFPartnerAnagraphProject()
						.GetCFAnagraphForProjectAndUser(
								String.valueOf(SessionManager.getInstance()
										.getSessionBean().Session
										.get("project")),
								this.getUser().getId());
				if (list != null && list.size() > 0)
				{
					return list.get(0).getProgressiveId();
				}
			}
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		if (this.getUser() == null)
		{
			return 1l;
		}
		return 0l;
	}

	public static void DeleteCFAnagraph(Long entityId, Long projectId)
			throws PersistenceBeanException
	{
		Transaction tr = PersistenceSessionManager.getBean().getSession()
				.beginTransaction();

		List<CFPartnerAnagraphCompletations> list = BeansFactory
				.CFPartnerAnagraphCompletations().GetEntitiesForCFAnagraph(
						entityId, String.valueOf(projectId));

		for (CFPartnerAnagraphCompletations cfpac : list)
		{
			BeansFactory.CFPartnerAnagraphCompletations().DeleteInTransaction(
					cfpac);
		}
		// partners list
		List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
				.GetAllByProject(String.valueOf(projectId));
		for (PartnersBudgets pb : listPB)
		{
			BeansFactory.PartnersBudgets().DeleteInTransaction(pb);
		}
		// general
		List<GeneralBudgets> listGb = BeansFactory.GeneralBudgets()
				.GetItemsForProject(projectId);

		for (GeneralBudgets gb : listGb)
		{
			BeansFactory.GeneralBudgets().DeleteInTransaction(gb);
		}

		CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs().Load(
				entityId);

		CFPartnerAnagraphProject cfpa = BeansFactory.CFPartnerAnagraphProject()
				.LoadByPartner(projectId, partner.getId());
		if (cfpa != null)
		{
			BeansFactory.CFPartnerAnagraphProject().DeleteInTransaction(cfpa);
		}

		List<CFPartnerAnagraphProject> listPartnerProjects = BeansFactory
				.CFPartnerAnagraphProject().LoadByPartnerWithoutProject(
						partner.getId());

		if (listPartnerProjects == null || listPartnerProjects.isEmpty())
		{
			Users user = partner.getUser();
			BeansFactory.CFPartnerAnagraphs().DeleteInTransaction(partner);
			BeansFactory.Users().DeleteInTransaction(user);
		}
		tr.commit();
	}

	public static void DeleteAllCFAnagraphs(Long entityId, Long projectId)
			throws PersistenceBeanException
	{
		List<CFPartnerAnagraphCompletations> list = BeansFactory
				.CFPartnerAnagraphCompletations().GetEntitiesForCFAnagraph(
						entityId, String.valueOf(projectId));

		for (CFPartnerAnagraphCompletations cfpac : list)
		{
			BeansFactory.CFPartnerAnagraphCompletations().Delete(cfpac);
		}
		// partners list
		List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
				.GetAllByProject(String.valueOf(projectId));
		for (PartnersBudgets pb : listPB)
		{
			BeansFactory.PartnersBudgets().Delete(pb);
		}
		// general
		List<GeneralBudgets> listGb = BeansFactory.GeneralBudgets()
				.GetItemsForProject(projectId);

		for (GeneralBudgets gb : listGb)
		{
			BeansFactory.GeneralBudgets().Delete(gb);
		}

		CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs().Load(
				entityId);

		CFPartnerAnagraphProject cfpa = BeansFactory.CFPartnerAnagraphProject()
				.LoadByPartner(projectId, partner.getId());
		if (cfpa != null)
			BeansFactory.CFPartnerAnagraphProject().Delete(cfpa);

		List<CFPartnerAnagraphProject> listPartnerProjects = BeansFactory
				.CFPartnerAnagraphProject().LoadByPartnerWithoutProject(
						partner.getId());

		if (listPartnerProjects != null && !listPartnerProjects.isEmpty())
		{
			for (CFPartnerAnagraphProject cfPartnerAnagraphProject : listPartnerProjects)
			{
				cfPartnerAnagraphProject.setDeleted(true);
			}
		}
	}

	/**
	 * Gets removedFromProject
	 * @return removedFromProject the removedFromProject
	 */
	public boolean isRemovedFromProject()
	{
		return removedFromProject;
	}

	/**
	 * Sets removedFromProject
	 * @param removedFromProject the removedFromProject to set
	 */
	public void setRemovedFromProject(boolean removedFromProject)
	{
		this.removedFromProject = removedFromProject;
	}
	
	/**
	 * Gets publicSubject.
	 * @return publicSubject
	 */
	public Boolean getPublicSubject() {
		return publicSubject;
	}
	
	/**
	 * Sets publicSubject.
	 * @param publicSubject
	 */
	public void setPublicSubject(Boolean publicSubject) {
		this.publicSubject = publicSubject;
	}
	
	@Override
	public String toString() {
		return this.getId().toString();
	}
}
