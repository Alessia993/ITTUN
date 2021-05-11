/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2009.
 *
 */
public class BooleanHelper
{
    public static boolean FromString(String str)
    {
        if (str.equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public static String ToString(boolean bool)
    {
        if (bool)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
    
    public static String ToItalianString(boolean bool)
    {
        if (bool)
        {
            return Utils.getString("Resources", "yes", null);
        }
        else
        {
            return Utils.getString("Resources", "no", null);
        }
    }
    
    public static boolean FromItalianString(String str)
    {
        if (Utils.getString("Resources", "yes", null).equals(str))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static String FromItalianStringToString(String str)
    {
        if (Utils.getString("Resources", "yes", null).equals(str))
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
