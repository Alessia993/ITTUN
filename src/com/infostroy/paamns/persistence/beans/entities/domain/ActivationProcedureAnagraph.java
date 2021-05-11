/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActivationProcedureTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibleUsers;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "activation_procedure_anagraph")
public class ActivationProcedureAnagraph extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long        serialVersionUID = 2421559239101635876L;
    
    /**
     * Stores code value of entity.
     */
    @Column(name = "code")
    private String                   code;
    
    /**
     * Stores procedureTipology value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "activation_procedure_type")
    private ActivationProcedureTypes activationProcedureType;
    
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String                   description;
    
    /**
     * Stores responsibleUser value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "responsible_user_id")
    private ResponsibleUsers         responsibleUser;
    
    /**
     * Stores responsibleUserName value of entity.
     */
    @Column(name = "responsible_user_name")
    private String                   responsibleUserName;
    
    /**
     * Stores amount value of entity.
     */
    @Column(name = "amount")
    private Double                   amount;
    
    @Column(name = "full_amount")
    private Double                   fullAmount;
    
    @Column(name = "is_created_agu", nullable = false)
    private boolean                  isCreatedByAGUUser;
    
    /**
     * Sets amount
     * @param amount the amount to set
     */
    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    
    /**
     * Gets amount
     * @return amount the amount
     */
    public Double getAmount()
    {
        return amount;
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets responsibleUser
     * @param responsibleUser the responsibleUser to set
     */
    public void setResponsibleUser(ResponsibleUsers responsibleUser)
    {
        this.responsibleUser = responsibleUser;
    }
    
    /**
     * Gets responsibleUser
     * @return responsibleUser the responsibleUser
     */
    public ResponsibleUsers getResponsibleUser()
    {
        return responsibleUser;
    }
    
    /**
     * Sets responsibleUserName
     * @param responsibleUserName the responsibleUserName to set
     */
    public void setResponsibleUserName(String responsibleUserName)
    {
        this.responsibleUserName = responsibleUserName;
    }
    
    /**
     * Gets responsibleUserName
     * @return responsibleUserName the responsibleUserName
     */
    public String getResponsibleUserName()
    {
        return responsibleUserName;
    }
    
    /**
     * Sets fullAmount
     * @param fullAmount the fullAmount to set
     */
    public void setFullAmount(Double fullAmount)
    {
        this.fullAmount = fullAmount;
    }
    
    /**
     * Gets fullAmount
     * @return fullAmount the fullAmount
     */
    public Double getFullAmount()
    {
        return fullAmount;
    }
    
    @SuppressWarnings("unchecked")
    public static Boolean IsCodeExists(String value, Long id)
    {
        List<ActivationProcedureAnagraph> list = new ArrayList<ActivationProcedureAnagraph>();
        try
        {
            list = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(ActivationProcedureAnagraph.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.like("code", value))
                    .add(Restrictions.ne("id", id.equals(null) ? 0 : id))
                    .list();
        }
        catch(Exception e)
        {
            Logger.getLogger(ActivationProcedureAnagraph.class).error(
                    e.getStackTrace());
        }
        if (list.size() == 0)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Sets isCreatedByAGUUser
     * @param isCreatedByAGUUser the isCreatedByAGUUser to set
     */
    public void setIsCreatedByAGUUser(Boolean isCreatedByAGUUser)
    {
        this.isCreatedByAGUUser = isCreatedByAGUUser;
    }
    
    /**
     * Gets isCreatedByAGUUser
     * @return isCreatedByAGUUser the isCreatedByAGUUser
     */
    public Boolean getIsCreatedByAGUUser()
    {
        return isCreatedByAGUUser;
    }
    
    public String toString()
    {
        return this.getDescription();
    }
    
    /**
     * Sets activationProcedureType
     * @param activationProcedureType the activationProcedureType to set
     */
    public void setActivationProcedureType(
            ActivationProcedureTypes activationProcedureType)
    {
        this.activationProcedureType = activationProcedureType;
    }
    
    /**
     * Gets activationProcedureType
     * @return activationProcedureType the activationProcedureType
     */
    public ActivationProcedureTypes getActivationProcedureType()
    {
        return activationProcedureType;
    }
}
