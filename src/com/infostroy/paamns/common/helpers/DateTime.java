/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2009.
 *
 */
public class DateTime
{
    private static final Logger log                    = Logger.getLogger(DateTime.class);
    
    /**
     * Stores value value of entity.
     */
    private Date                value;
    
    private static String       datePattern            = "dd/MM/yyyy";
    
    private static String       mySQLDatePattern       = "yyyy-MM-dd";
    
    private static String       pathDatePattern        = "dd-MM-yyyy";
    
    private static String       dotsDatePattern        = "dd.MM.yyyy";
    
    private static String       datePatternWithMinutes = "dd/MM/yyyy HH:mm";
    
    private static String       datePatternWithSeconds = "dd/MM/yyyy HH:mm:ss";
    
    private static DateFormat   fromCalendar           = new SimpleDateFormat(
                                                               "EEE MMM dd HH:mm:ss z yyyy",
                                                               Locale.US);
    
    private static DateFormat   dfm                    = new SimpleDateFormat(
                                                               DateTime.getDatePattern());
    
    private static DateFormat   dotsFormatter          = new SimpleDateFormat(
                                                               dotsDatePattern,
                                                               Locale.US);
    
    /**
     * 
     */
    public DateTime()
    {
        setDate((Date) null);
    }
    
    public static Date getNow()
    {
        Date date = new Date();
        
        return date;
    }
    
    @SuppressWarnings("deprecation")
    public static Date getToday()
    {
        Date date = new Date();
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        return date;
    }
    
    @SuppressWarnings("deprecation")
    public static boolean DateLessThenMaxDate(Date date, Date maxDate)
    {
        Date today = maxDate;
        if (date.getYear() < today.getYear())
        {
            return true;
        }
        else if (date.getYear() == today.getYear()
                && date.getMonth() < today.getMonth())
        {
            return true;
        }
        else if (date.getYear() == today.getYear()
                && date.getMonth() == today.getMonth()
                && date.getDay() <= today.getDay())
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public static Date FromString(String str)
    {
        if (str != "")
        {
            try
            {
                return dfm.parse(str);
            }
            catch(ParseException e2)
            {
                //                log.warn("DateTime.FromString(dfm1.parse(str)) : " + e2);
                try
                {
                    return dotsFormatter.parse(str);
                }
                catch(ParseException e)
                {
                    //log.warn("DateTime.FromString(dfm1.parse(str)) : " + e2);
                }
                
            }
            try
            {
                return fromCalendar.parse(str);
            }
            catch(ParseException e1)
            {
                //log.warn("DateTime.FromString(fromCalendar.parse(str)) : "+ e1);
                return null;
            }
            
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * @param str
     */
    public DateTime(String str)
    {
        setDate((Date) null);
        try
        {
            this.setDate(dfm.parse(str));
        }
        catch(ParseException e)
        {
            log.warn("DateTime.DateTime : " + e);
            e.printStackTrace();
        }
    }
    
    /**
     * @return
     */
    public static String ToString(Date value)
    {
        return toFormatedString(value, getDatePattern());
    }
    
    public static String ToStringWithMinutes(Date value)
    {
        return toFormatedString(value, getDatePatternWithMinutes());
    }
    
    public static String ToStringWithSeconds(Date value)
    {
        return toFormatedString(value, getDatePatternWithSeconds());
    }
    
    public static String ToPathString(Date value)
    {
        return toFormatedString(value, getPathDatePattern());
    }
    
    private static String toFormatedString(Date value, String format)
    {
        SimpleDateFormat df = null;
        String returnValue = "";
        
        if (value == null)
        {
            log.error("aDate is null!");
        }
        else
        {
            df = new SimpleDateFormat(format);
            returnValue = df.format(value);
        }
        
        return (returnValue);
    }
    
    public static String ToMySqlString(Date value)
    {
        return toFormatedString(value, getMySQLDatePattern());
    }
    
    /**
     * @param str
     */
    public void setDate(String str)
    {
        try
        {
            this.setDate(dfm.parse(str));
        }
        catch(ParseException e)
        {
            log.warn("DateTime.setDate : " + e);
        }
    }
    
    /**
     * @return
     */
    public String ToString()
    {
        return dfm.format(this.value);
    }
    
    /**
     * Sets date
     * @param date the date to set
     */
    public void setDate(Date value)
    {
        this.value = value;
    }
    
    /**
     * Gets date
     * @return date the date
     */
    public Date getDate()
    {
        return value;
    }
    
    /**
     * Sets datePattern
     * @param datePattern the datePattern to set
     */
    public static void setDatePattern(String datePattern)
    {
        DateTime.datePattern = datePattern;
    }
    
    /**
     * Gets datePattern
     * @return datePattern the datePattern
     */
    public static String getDatePattern()
    {
        return datePattern;
    }
    
    /**
     * Sets datePatternWithoutSeconds
     * @param datePatternWithoutSeconds the datePatternWithoutSeconds to set
     */
    public static void setDatePatternWithMinutes(
            String datePatternWithoutSeconds)
    {
        DateTime.datePatternWithMinutes = datePatternWithoutSeconds;
    }
    
    /**
     * Gets datePatternWithoutSeconds
     * @return datePatternWithoutSeconds the datePatternWithoutSeconds
     */
    public static String getDatePatternWithMinutes()
    {
        return datePatternWithMinutes;
    }
    
    /**
     * Sets datePatternWithSeconds
     * @param datePatternWithSeconds the datePatternWithSeconds to set
     */
    public static void setDatePatternWithSeconds(String datePatternWithSeconds)
    {
        DateTime.datePatternWithSeconds = datePatternWithSeconds;
    }
    
    /**
     * Gets datePatternWithSeconds
     * @return datePatternWithSeconds the datePatternWithSeconds
     */
    public static String getDatePatternWithSeconds()
    {
        return datePatternWithSeconds;
    }
    
    /**
     * Sets mySQLDatePattern
     * @param mySQLDatePattern the mySQLDatePattern to set
     */
    public static void setMySQLDatePattern(String mySQLDatePattern)
    {
        DateTime.mySQLDatePattern = mySQLDatePattern;
    }
    
    /**
     * Gets mySQLDatePattern
     * @return mySQLDatePattern the mySQLDatePattern
     */
    public static String getMySQLDatePattern()
    {
        return mySQLDatePattern;
    }
    
    /**
     * Sets pathDatePattern
     * @param pathDatePattern the pathDatePattern to set
     */
    public static void setPathDatePattern(String pathDatePattern)
    {
        DateTime.pathDatePattern = pathDatePattern;
    }
    
    /**
     * Gets pathDatePattern
     * @return pathDatePattern the pathDatePattern
     */
    public static String getPathDatePattern()
    {
        return pathDatePattern;
    }
    
    public static Date getDayStart(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
    }
    
    public static Date getDayEnd(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        
        return c.getTime();
    }
    
}
