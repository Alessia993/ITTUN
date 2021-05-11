/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.EntityProxy;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "procedure_tipology")
public class ProcedureTipology extends EntityProxy
{
    
    @OneToMany(mappedBy = "procedureTipology")
    private List<ProcedureTipologyStep> steps;
    
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private int                         code;
    
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String                      description;
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(int code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public int getCode()
    {
        return code;
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(this,
                LocaleSessionManager.getInstance().getInstanceBean()
                        .getCurrLocale().toString(), this.description,
                description));
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(this, LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
    
    /**
     * Overrides toString method
     */
    public String toString()
    {
        return this.getDescription();
    }
    
    /**
     * Sets steps
     * @param steps the steps to set
     */
    public void setSteps(List<ProcedureTipologyStep> steps)
    {
        this.steps = steps;
    }
    
    /**
     * Gets steps
     * @return steps the steps
     */
    public List<ProcedureTipologyStep> getSteps()
    {
        return steps;
    }
}
