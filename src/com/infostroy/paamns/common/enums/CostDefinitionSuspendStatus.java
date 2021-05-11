/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Zrazhevskiy Vladimir
 * InfoStroy Co., 2011.
 *
 */
public enum CostDefinitionSuspendStatus
{
    
    SUSPENDED(1l, "suspensionManagementStatusSuspended"),
    CANCEL_SUSPEND(2l, "suspensionManagementStatusCancelSuspend"),
    RETREAT(3l, "suspensionManagementStatusRetreat");
    
    private CostDefinitionSuspendStatus(Long value, String code)
    {
        setState(value);
        setCode(code);
    }
    
    private Long   state;
    
    private String code;
    
	public static CostDefinitionSuspendStatus getById(Long id)
	{
		for (CostDefinitionSuspendStatus status : CostDefinitionSuspendStatus
				.values())
		{
			if (status.getState().equals(id))
			{
				return status;
			}
		}

		return null;
	}
	
	public String getDescription()
	{
		return Utils.getString(getCode());
	}
    
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
