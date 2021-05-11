/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public enum TransferTypes
{
	Advance(1l, "transferTypesAdvance"), InermediateReimbursement(2l, "transferTypesInermediateReimbursement"), 
	BalanceReimbursement(3l, "transferTypesBalanceReimbursement"), AdvanceStateAidDeMinimis(4l, "advanceStateAidDeMinimis"); //, AdvanceStateAidExemptionScheme(5l, "advanceStateAidExemptionScheme");
	
    private TransferTypes(Long code, String value)
    {
        setValue(value);
        setCode(code);
    }
    
    private Long   code;
    
    private String value;
    
  
    
    /**
	 * Gets code
	 * @return code the code
	 */
	public Long getCode()
	{
		return code;
	}



	/**
	 * Sets code
	 * @param code the code to set
	 */
	public void setCode(Long code)
	{
		this.code = code;
	}



	/**
	 * Gets value
	 * @return value the value
	 */
	public String getValue()
	{
		return value;
	}



	/**
	 * Sets value
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}



	public static TransferTypes getByCode(Long code)
	{
		for (TransferTypes e : TransferTypes.values())
		{
			if (e.getCode().equals(code))
			{
				return e;
			}

		}
		return null;
	}
	
	public String toString(){
		return Utils.getString(this.getValue());
	}
    
}
