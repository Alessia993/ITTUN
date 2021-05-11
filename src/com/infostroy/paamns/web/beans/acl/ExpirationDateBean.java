package com.infostroy.paamns.web.beans.acl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpirationDate;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;
import com.infostroy.paamns.web.beans.EntityEditBean;

public class ExpirationDateBean extends EntityEditBean<ExpirationDate>
{

	private Date expirationDate;
	private String showErrorMessage;
	
	@SuppressWarnings("static-access")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException,
			NullPointerException 
	{
		ExpirationDate expirationDate = BeansFactory.ExpirationDate().getExpirationDate(); 	
		if(expirationDate == null)
		{
			setExpirationDate(new Date());
		}
		else
		{
			setExpirationDate(expirationDate.getExpirationDate());
		}
		setShowErrorMessage("none");
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException 
	{
	}

	@Override
	public Boolean BeforeSave()
	{
		if(getExpirationDate() == null)
		{
			return false;
		}
		try 
		{
			List<PotentialProjects> projects = BeansFactory.PotentialProjects().Load();
			if(projects.size() > 0)
			{
				for(PotentialProjects project : projects)
				{
					if(project.getUpdateDate().after(getExpirationDate()))
					{
						setShowErrorMessage("");
						return false;		
					}
				}
			}
		} 
		catch (PersistenceBeanException e) 
		{
			log.error(e);
		}
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException 
	{
		ExpirationDate expirationDate = BeansFactory.ExpirationDate().getExpirationDate(); 	
		if(expirationDate == null)
		{
			expirationDate = new ExpirationDate();
		}
		expirationDate.setExpirationDate(getExpirationDate());
		BeansFactory.ExpirationDate().SaveInTransaction(expirationDate);
	}

	@Override
	public void GoBack() 
	{
		this.goTo(PagesTypes.INDEX);	
	}

	@Override
	public void AfterSave() 
	{
		this.GoBack();
	}

	public Date getExpirationDate() 
	{
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) 
	{
		this.expirationDate = expirationDate;
	}

	public String getShowErrorMessage() 
	{
		return showErrorMessage;
	}

	public void setShowErrorMessage(String showErrorMessage) 
	{
		this.showErrorMessage = showErrorMessage;
	}

}
