/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "financial_data")
public class FinancialData extends LocalizableStaticEntity
{
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private String code;
    
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String description;
    
    /**
     * Stores tipology value of entity.
     */
    @Column(name = "tipology")
    private String tipology;
    
    /**
     * Stores total value of entity.
     */
    @Column(name = "total")
    private String total;
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.code, code));
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.code, null));
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, description));
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
    
    /**
     * Sets tipology
     * @param tipology the tipology to set
     */
    public void setTipology(String tipology)
    {
        this.tipology = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.tipology, tipology));
    }
    
    /**
     * Gets tipology
     * @return tipology the tipology
     */
    public String getTipology()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.tipology,
                null));
    }
    
    /**
     * Sets total
     * @param total the total to set
     */
    public void setTotal(String total)
    {
        this.total = total;
    }
    
    /**
     * Gets total
     * @return total the total
     */
    public String getTotal()
    {
        return total;
    }
}
