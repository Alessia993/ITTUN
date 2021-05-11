/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;


import java.io.IOException;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ApplicationSettings;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.AnagraphicalData;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Anton Kazakov InfoStroy Co., 2014.
 * 
 */
public class ThresholdSettingsBean extends EntityEditBean<ApplicationSettings>
{
	private String	threshold;

	private String	wrongThresholdError;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
	 */
	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
	 */
	@Override
	public void GoBack()
	{
		this.goTo(PagesTypes.INDEX);
	}

	public Boolean BeforeSave()
	{
		if (!isThresholdValid())
		{
			this.setWrongThresholdError(Utils.getString("wrongThreshold"));
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	private boolean isThresholdValid()
	{
		try
		{

			Integer item = Integer.parseInt(getThreshold());
			if (item >= 0 && item <= 100)
			{
				return true;
			}
			else
			{
				return false;
			}

		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		
		threshold = BeansFactory.ApplicationSettings().getValue(
				ApplicationSettingTypes.THRESHOLD_CONTROL_COSTS);
	
		if (threshold == null)
		{
			createThresholdSetting();
			threshold = "0";
		
		}		
	}

	/**
	 * @throws PersistenceBeanException 
	 * @throws HibernateException 
	 * 
	 */
	private void createThresholdSetting() throws PersistenceBeanException
	{
		ApplicationSettings applicationSettings = new ApplicationSettings();
		applicationSettings.setCode(ApplicationSettingTypes.THRESHOLD_CONTROL_COSTS.getKey());
		applicationSettings.setValue("0");
		BeansFactory.ApplicationSettings().Insert(applicationSettings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException
	{
		ApplicationSettings setting = BeansFactory.ApplicationSettings().getItem(
				ApplicationSettingTypes.THRESHOLD_CONTROL_COSTS);
		setting.setValue(this.getThreshold());
		BeansFactory.ApplicationSettings().Save(setting);

	}

	/**
	 * Gets threshold
	 * 
	 * @return threshold the threshold
	 */
	public String getThreshold()
	{
		return threshold;
	}

	/**
	 * Sets threshold
	 * 
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(String threshold)
	{
		this.threshold = threshold;
	}

	/**
	 * Gets wrongThresholdError
	 * 
	 * @return wrongThresholdError the wrongThresholdError
	 */
	public String getWrongThresholdError()
	{
		return wrongThresholdError;
	}

	/**
	 * Sets wrongThresholdError
	 * 
	 * @param wrongThresholdError
	 *            the wrongThresholdError to set
	 */
	public void setWrongThresholdError(String wrongThresholdError)
	{
		this.wrongThresholdError = wrongThresholdError;
	}

}
