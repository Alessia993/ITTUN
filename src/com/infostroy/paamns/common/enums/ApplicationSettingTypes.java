/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public enum ApplicationSettingTypes
{
    ACTIVITY_LOG_SIZE("ACTIVITY_LOG_SIZE"),
    PASSWORD_EXPIRATION_TIME("PASSWORD_EXPIRATION_TIME"),
    UPLOAD_FILE_PARENT_DIRECTORY("UPLOAD_FILE_PARENT_DIRECTORY"),
    THRESHOLD_CONTROL_COSTS("THRESHOLD_CONTROL_COSTS");
    
    private String key;
    
    private ApplicationSettingTypes(String name)
    {
        this.setKey(name);
    }
    
    /**
     * Sets key
     * @param key the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    
    /**
     * Gets key
     * @return key the key
     */
    public String getKey()
    {
        return key;
    }
}
