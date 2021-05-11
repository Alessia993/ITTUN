/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import java.io.File;
import java.io.IOException;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ApplicationSettings;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ApplicationSettingsBean extends
        EntityEditBean<ApplicationSettings>
{
    private String activityLogSize;
    
    private String passwordExpirationTime;
    
    private String uploadFileParentDirectory;
    
    private String fileDirectoryError;
    
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
        if (!(new File(this.getUploadFileParentDirectory()).exists()))
        {
            this.setFileDirectoryError(Utils
                    .getString("fileDirectoryDoNotExists"));
            return false;
        }
        
        return true;
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
        activityLogSize = BeansFactory.ApplicationSettings().getValue(
                ApplicationSettingTypes.ACTIVITY_LOG_SIZE);
        passwordExpirationTime = BeansFactory.ApplicationSettings().getValue(
                ApplicationSettingTypes.PASSWORD_EXPIRATION_TIME);
        uploadFileParentDirectory = BeansFactory.ApplicationSettings()
                .getValue(ApplicationSettingTypes.UPLOAD_FILE_PARENT_DIRECTORY);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
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
        ApplicationSettings set1 = BeansFactory.ApplicationSettings().getItem(
                ApplicationSettingTypes.ACTIVITY_LOG_SIZE);
        set1.setValue(this.getActivityLogSize());
        
        BeansFactory.ApplicationSettings().SaveInTransaction(set1);
        
        ApplicationSettings set2 = BeansFactory.ApplicationSettings().getItem(
                ApplicationSettingTypes.PASSWORD_EXPIRATION_TIME);
        set2.setValue(this.getPasswordExpirationTime());
        
        BeansFactory.ApplicationSettings().SaveInTransaction(set2);
        
        ApplicationSettings set3 = BeansFactory.ApplicationSettings().getItem(
                ApplicationSettingTypes.UPLOAD_FILE_PARENT_DIRECTORY);
        set3.setValue(this.getUploadFileParentDirectory());
        
        BeansFactory.ApplicationSettings().SaveInTransaction(set3);
    }
    
    /**
     * Sets activityLogSize
     * 
     * @param activityLogSize
     *            the activityLogSize to set
     */
    public void setActivityLogSize(String activityLogSize)
    {
        this.activityLogSize = activityLogSize;
    }
    
    /**
     * Gets activityLogSize
     * 
     * @return activityLogSize the activityLogSize
     */
    public String getActivityLogSize()
    {
        return activityLogSize;
    }
    
    /**
     * Sets passwordExpirationTime
     * 
     * @param passwordExpirationTime
     *            the passwordExpirationTime to set
     */
    public void setPasswordExpirationTime(String passwordExpirationTime)
    {
        this.passwordExpirationTime = passwordExpirationTime;
    }
    
    /**
     * Gets passwordExpirationTime
     * 
     * @return passwordExpirationTime the passwordExpirationTime
     */
    public String getPasswordExpirationTime()
    {
        return passwordExpirationTime;
    }
    
    /**
     * Sets uploadFileParentDirectory
     * 
     * @param uploadFileParentDirectory
     *            the uploadFileParentDirectory to set
     */
    public void setUploadFileParentDirectory(String uploadFileParentDirectory)
    {
        this.uploadFileParentDirectory = uploadFileParentDirectory;
    }
    
    /**
     * Gets uploadFileParentDirectory
     * 
     * @return uploadFileParentDirectory the uploadFileParentDirectory
     */
    public String getUploadFileParentDirectory()
    {
        return uploadFileParentDirectory;
    }
    
    /**
     * Sets fileDirectoryError
     * 
     * @param fileDirectoryError
     *            the fileDirectoryError to set
     */
    public void setFileDirectoryError(String fileDirectoryError)
    {
        this.fileDirectoryError = fileDirectoryError;
    }
    
    /**
     * Gets fileDirectoryError
     * 
     * @return fileDirectoryError the fileDirectoryError
     */
    public String getFileDirectoryError()
    {
        return fileDirectoryError;
    }
    
}
