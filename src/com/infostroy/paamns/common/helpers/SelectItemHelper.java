/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import javax.faces.model.SelectItem;

import com.infostroy.paamns.common.utils.Utils;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class SelectItemHelper
{
    public static SelectItem getFirst()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("- ");
        sb.append(Utils.getString("Resources", "notSelected", null));
        sb.append(" -");
        return new SelectItem("", sb.toString());
    }
    
    public static SelectItem getVirtualEntity()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("- ");
        sb.append(Utils.getString("Resources", "notSelected", null));
        sb.append(" -");
        
        return new SelectItem("-1", sb.toString());
    }
    
    public static SelectItem getAllElement()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("- ");
        sb.append(Utils.getString("Resources", "all", null));
        sb.append(" -");
        return new SelectItem("", sb.toString());
    }
    
    public static SelectItem getOther()
    {
    	StringBuilder sb = new StringBuilder();
        sb.append("- ");
        sb.append(Utils.getString("Resources", "otherElement", null));
        sb.append(" -");
        return new SelectItem("", sb.toString());
    }
}
