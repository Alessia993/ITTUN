/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "procedure_tipology_step")
public class ProcedureTipologyStep extends EntityProxy
{
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private int               code;
    
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String            description;
    
    @ManyToOne
    @JoinColumn
    private ProcedureTipology procedureTipology;
    
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
     * Sets procedureTipologyStep
     * @param procedureTipologyStep the procedureTipologyStep to set
     */
    public void setProcedureTipologyStep(ProcedureTipology procedureTipology)
    {
        this.procedureTipology = procedureTipology;
    }
    
    /**
     * Gets procedureTipologyStep
     * @return procedureTipologyStep the procedureTipologyStep
     */
    public ProcedureTipology getProcedureTipologyStep()
    {
        return procedureTipology;
    }
}
