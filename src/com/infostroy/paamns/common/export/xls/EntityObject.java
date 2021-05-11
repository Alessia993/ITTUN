/**
 * 
 */
package com.infostroy.paamns.common.export.xls;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2012.
 *
 */
public class EntityObject
{
    private List<Field> fields;
    
    public EntityObject()
    {
        super();
        fields = new ArrayList<Field>();
    }
    
    /**
     * Gets fields
     * @return fields the fields
     */
    public List<Field> getFields()
    {
        return fields;
    }
    
    /**
     * Sets fields
     * @param fields the fields to set
     */
    public void setFields(List<Field> fields)
    {
        this.fields = fields;
    }
}
