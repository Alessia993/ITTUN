package com.infostroy.paamns.web.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.utils.Utils;

public class BaseConverter
{
    public static String convertToMoneyString(Object value)
    {
        DecimalFormat dec = new DecimalFormat("###,###,##0.00 \u20AC",
                DecimalFormatSymbols.getInstance(Locale.ITALIAN));
        
        return String.valueOf(
                dec.format(Double.parseDouble(String.valueOf(value)))).replace(
                " ", "\u00a0");
    }
    
    public static String convertToMoneyStringEuroSign(Object value)
    {
        DecimalFormat dec = new DecimalFormat("###,###,##0.00 €",
                DecimalFormatSymbols.getInstance(Locale.ITALIAN));
        
        return dec.format(Double.parseDouble(String.valueOf(value)));
    }
    
    public static String convertToDoubleString(Object value)
    {
        return (new DecimalFormat("###.##")).format((new BigDecimal(
                (Double) value)).setScale(2, RoundingMode.HALF_UP));        
    }
    
    public static String convertToDecimalString(Object value)
    {
        DecimalFormat dec = new DecimalFormat("###,###,###.#",
                DecimalFormatSymbols.getInstance(Locale.ITALIAN));
        
        return String.valueOf(dec.format(Double.parseDouble(String
                .valueOf(value))));
    }
    
    public static String convertToDateString(Object value)
    {
        if (value != null)
        {
            return DateTime.ToString((Date) value);
        }
        return "";
    }
    
    public static String convertToPercentageString(Object value)
    {
        if (value instanceof Double)
        {
            return convertToDoubleString(value) + " \u0025";
        }
        return value.toString() + " \u0025";
    }
    
    public static String convertToNumberString(Object value)
    {
        return value.toString();
    }
    
    public static String convertToBooleanString(Object value)
    {
        if ((Boolean) value)
        {
            return Utils.getString("yes");
        }
        
        return Utils.getString("no");
    }
}
