/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2011.
 * 
 */
public enum SuspensionReasonTypes
{
    FirstReason(1l, "suspensionManagmentListReasonFirst"),
    SecondReason(2l, "suspensionManagmentListReasonSecond");
    private SuspensionReasonTypes(Long value, String code)
    {
        setState(value);
        setCode(code);
    }
    
    private Long   state;
    
    private String code;
    
    public Long getState()
    {
        return state;
    }
    
    public void setState(Long state)
    {
        this.state = state;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
}
