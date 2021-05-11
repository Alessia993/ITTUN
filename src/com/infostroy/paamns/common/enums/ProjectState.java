/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public enum ProjectState
{
    FoundingEligible(1), Actual(2), Suspended(3), Revoked(4), Closed(5);
    
    private ProjectState(int value)
    {
        this.setCode(value);
    }
    
    public static ProjectState getByState(int value)
    {
        for (ProjectState item : ProjectState.values())
        {
            if (item.getCode() == Integer.parseInt(String.valueOf(value)))
            {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Sets code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(int code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * 
     * @return code the code
     */
    public int getCode()
    {
        return code;
    }
    
    private int code;
}
