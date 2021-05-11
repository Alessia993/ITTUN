/**
 * 
 */
package com.infostroy.paamns.common.export.xls;

import com.infostroy.paamns.common.enums.FieldTypes;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2012.
 *
 */
public class Field
{
    private String     title;
    
    private FieldTypes type;
    
    private Object     obj;
    
    private int        num;
    
    /**
     * @param title
     * @param type
     * @param obj
     * @param num
     */
    public Field(String title, FieldTypes type, Object obj, int num)
    {
        super();
        this.title = title;
        this.type = type;
        this.obj = obj;
        this.num = num;
    }
    
    /**
     * Gets title
     * @return title the title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Sets title
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * Gets type
     * @return type the type
     */
    public FieldTypes getType()
    {
        return type;
    }
    
    /**
     * Sets type
     * @param type the type to set
     */
    public void setType(FieldTypes type)
    {
        this.type = type;
    }
    
    /**
     * Gets obj
     * @return obj the obj
     */
    public Object getObj()
    {
        return obj;
    }
    
    /**
     * Sets obj
     * @param obj the obj to set
     */
    public void setObj(Object obj)
    {
        this.obj = obj;
    }
    
    /**
     * Gets num
     * @return num the num
     */
    public int getNum()
    {
        return num;
    }
    
    /**
     * Sets num
     * @param num the num to set
     */
    public void setNum(int num)
    {
        this.num = num;
    }
}
