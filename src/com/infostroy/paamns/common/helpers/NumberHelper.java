/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.text.DecimalFormat;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class NumberHelper
{
    public static double Round(Double num1)
    {
    	if(num1==null){
    		return 0d;
    	}
        DecimalFormat df = new DecimalFormat("#.#####");
        
        return Double.valueOf((df.format(num1)).replaceAll(",", "."));
    }
    public static double CurrencyFormat(double num1)
    {
        return Math.round(num1*100)/100.0;
    }
    
	public static boolean isNumeric(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
}
