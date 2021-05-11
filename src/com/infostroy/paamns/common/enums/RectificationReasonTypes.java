/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2011.
 *
 */
public enum RectificationReasonTypes
{
    FirstReason(1l, "rectificationManagementReasonFirst"),
    SecondReason(2l, "rectificationManagementReasonSecond");
    private RectificationReasonTypes(Long value, String code)
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
