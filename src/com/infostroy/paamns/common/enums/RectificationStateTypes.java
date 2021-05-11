/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Dmitry Pogorelov InfoStroy Co., 2011.
 * 
 */
public enum RectificationStateTypes
{
    New(1l, "rectificationManagementReasonSecondNew"),
    SentToAgu(2l, "rectificationManagementStateSecondSentToAgu"),
    SentToAcu(3l, "rectificationManagementStateSecondSentToAcu"),
    RefusedByAgu(4l, "rectificationManagementStateRefusedByAgu"),
    RefusedByAcu(5l, "rectificationManagementStateRefusedByAcu"),
    Confirmed(6l, "rectificationManagementStateConfirmed"),
    Recovered(7l, "rectificationManagementStateRecovered");
    
    private RectificationStateTypes(Long value, String code)
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
