/**
 * 
 */
package com.infostroy.paamns.common.export.xls;

import java.util.Comparator;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2012.
 *
 */
public class FieldsComparer implements Comparator<Field>
{
    
    @Override
    public int compare(Field arg0, Field arg1)
    {
        return ((Integer) arg0.getNum()).compareTo(arg1.getNum());
    }
    
}
