/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public enum ControllerTypeTypes
{
    Juridical("Juridical"), Phisical("Phisical");
    
    private ControllerTypeTypes(String value)
    {
        setControllerType(value);
    }
    
    /**
     * Sets controllerType
     * 
     * @param controllerType
     *            the controllerType to set
     */
    public void setControllerType(String controllerType)
    {
        this.controllerType = controllerType;
    }
    
    /**
     * Gets controllerType
     * 
     * @return controllerType the controllerType
     */
    public String getControllerType()
    {
        return controllerType;
    }
    
    private String controllerType;
}
