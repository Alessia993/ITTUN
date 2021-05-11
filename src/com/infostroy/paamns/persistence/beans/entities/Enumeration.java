/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Alexander Chelombitko 
 * @author Sergey Manoylo
 * InfoStroy Co., 2009,2010.
 * 
 */
@MappedSuperclass
public class Enumeration extends Entity
{
    /**
     * Stores value value of entity.
     */
    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    protected String value;
    
    /**
     * 
     * @param value
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return this.getValue();
    }
}
