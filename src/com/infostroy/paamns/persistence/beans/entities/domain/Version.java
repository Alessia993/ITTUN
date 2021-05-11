/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2011.
 *
 */
@javax.persistence.Entity
@Table(name = "version")
public class Version extends Entity
{
    @Column
    private String number;
    
    @Column(name = "create_date")
    private Date   createDate;
    
    /**
     * Sets number
     * @param number the number to set
     */
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    /**
     * Gets number
     * @return number the number
     */
    public String getNumber()
    {
        return number;
    }
    
    /**
     * Sets createDate
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }
    
    /**
     * Gets createDate
     * @return createDate the createDate
     */
    public Date getCreateDate()
    {
        return createDate;
    }
}
