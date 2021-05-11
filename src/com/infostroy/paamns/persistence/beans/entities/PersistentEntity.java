package com.infostroy.paamns.persistence.beans.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;

/**
 * 
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2009,2010.
 * 
 */
@MappedSuperclass
public class PersistentEntity extends Entity
{
    /**
     * 
     */
    private static final long serialVersionUID = -7513259957739654708L;
    
    /**
     * 
     */
    @Column(name = "create_date")
    private Date              createDate;
    
    /**
     * Stores deleted
     */
    @Column(name = "deleted")
    private Boolean           deleted;
    
    /**
     * Sets deleted
     * 
     * @param deleted
     *            the deleted to set
     */
    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }
    
    /**
     * Gets deleted
     * 
     * @return deleted the deleted
     */
    public Boolean getDeleted()
    {
        return deleted;
    }
    
    /**
     * Sets createDate
     * 
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }
    
    /**
     * Gets createDate
     * 
     * @return createDate the createDate
     */
    @Export(propertyName = "exportCreateDate", seqXLS = 4, type = FieldTypes.DATE, place = ExportPlaces.Projects)
    public Date getCreateDate()
    {
        return createDate;
    }
    
}
