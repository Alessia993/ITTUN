/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.infostroy.paamns.common.utils.Utils;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "annual_profiles")
public class AnnualProfiles extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long         serialVersionUID = -407371251867673343L;
    
    @Transient
    private String                    string;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Projects                  project;
    
    /**
     * Stores year value of entity.
     */
    @Column
    private Integer                   year;
    
    /**
     * Stores amountExpected value of entity.
     */
    @Column(name = "amounte_expected")
    private Double                    amountExpected;
    
    /**
     * Stores amountRealized value of entity.
     */
    @Column(name = "amount_realized")
    private Double                    amountRealized;
    
    /**
     * Stores amountToAchieve value of entity.
     */
    @Column(name = "amount_to_achieve")
    private Double                    amountToAchieve;
    
    /**
     * Stores note value of entity.
     */
    @Column
    private String                    note;
    
    /**
     * Stores progressValidationObject value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "progress_validation_object_id")
    private ProgressValidationObjects progressValidationObject;
    
    @Column(name = "version")
    private Long                      version;
    
    public AnnualProfiles clone()
    {
        AnnualProfiles ap = new AnnualProfiles();
        ap.setCreateDate(this.getCreateDate());
        ap.setAmountExpected(this.getAmountExpected());
        ap.setAmountRealized(this.getAmountRealized());
        ap.setAmountToAchieve(this.getAmountToAchieve());
        ap.setNote(this.getNote());
        ap.setProgressValidationObject(this.getProgressValidationObject());
        ap.setProject(this.getProject());
        ap.setVersion(this.getVersion());
        ap.setYear(this.getYear());
        ap.setString(this.getString());
        
        return ap;
    }
    
    /**
     * Sets project
     * @param project the project to set
     */
    public void setProject(Projects project)
    {
        this.project = project;
    }
    
    /**
     * Gets project
     * @return project the project
     */
    public Projects getProject()
    {
        return project;
    }
    
    /**
     * Sets year
     * @param year the year to set
     */
    public void setYear(Integer year)
    {
        this.year = year;
    }
    
    /**
     * Gets year
     * @return year the year
     */
    public Integer getYear()
    {
        return year;
    }
    
    /**
     * Sets amountExpected
     * @param amountExpected the amountExpected to set
     */
    public void setAmountExpected(Double amountExpected)
    {
        this.amountExpected = amountExpected;
    }
    
    /**
     * Gets amountExpected
     * @return amountExpected the amountExpected
     */
    public Double getAmountExpected()
    {
        return amountExpected;
    }
    
    /**
     * Sets amountRealized
     * @param amountRealized the amountRealized to set
     */
    public void setAmountRealized(Double amountRealized)
    {
        this.amountRealized = amountRealized;
    }
    
    /**
     * Gets amountRealized
     * @return amountRealized the amountRealized
     */
    public Double getAmountRealized()
    {
        return amountRealized;
    }
    
    /**
     * Sets amountToAchieve
     * @param amountToAchieve the amountToAchieve to set
     */
    public void setAmountToAchieve(Double amountToAchieve)
    {
        this.amountToAchieve = amountToAchieve;
    }
    
    /**
     * Gets amountToAchieve
     * @return amountToAchieve the amountToAchieve
     */
    public Double getAmountToAchieve()
    {
        return amountToAchieve;
    }
    
    /**
     * Sets note
     * @param note the note to set
     */
    public void setNote(String note)
    {
        this.note = note;
    }
    
    /**
     * Gets note
     * @return note the note
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * Sets progressValidationObject
     * @param progressValidationObject the progressValidationObject to set
     */
    public void setProgressValidationObject(
            ProgressValidationObjects progressValidationObject)
    {
        this.progressValidationObject = progressValidationObject;
    }
    
    /**
     * Gets progressValidationObject
     * @return progressValidationObject the progressValidationObject
     */
    public ProgressValidationObjects getProgressValidationObject()
    {
        return progressValidationObject;
    }
    
    /**
     * Sets version
     * @param version the version to set
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }
    
    /**
     * Gets version
     * @return version the version
     */
    public Long getVersion()
    {
        return version;
    }
    
    /**
     * Sets string
     * @param string the string to set
     */
    public void setString(String string)
    {
        this.string = string;
    }
    
    /**
     * Gets string
     * @return string the string
     */
    public String getString()
    {
        if (string == null || string.isEmpty())
        {
            string = Utils.getString("annualProfileYear") + " "
                    + this.getYear();
        }
        
        return string;
    }
}
