/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import com.infostroy.paamns.common.logging.InfoLog;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2013.
 *
 */
public class ActionLogHelper
{
    public static void saveActionToLog(String mes)
    {
        try
        {
            InfoLog.getInstance().addMessage(mes);
        }
        catch(Exception e)
        {
            
        }
    }
}
