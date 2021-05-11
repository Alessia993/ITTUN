/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "phisical_classification_priority_thema")
public class PhisicalClassificationPriorityThema extends
        LocalizableStaticEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -4313639918125072787L;
    
    /**
     * Stores asse value of entity.
     */
    @Column(name = "asse_id")
    private Integer           asse;
    
    /**
     * Stores prioritaryReason value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "prioritary_reason_id")
    private PrioritaryReasons prioritaryReason;
    
    /**
     * Sets prioritaryReason
     * @param prioritaryReason the prioritaryReason to set
     */
    public void setPrioritaryReason(PrioritaryReasons prioritaryReason)
    {
        this.prioritaryReason = prioritaryReason;
    }
    
    /**
     * Gets prioritaryReason
     * @return prioritaryReason the prioritaryReason
     */
    public PrioritaryReasons getPrioritaryReason()
    {
        return prioritaryReason;
    }
    
    /**
     * Sets asse
     * @param asse the asse to set
     */
    public void setAsse(Integer asse)
    {
        this.asse = asse;
    }
    
    /**
     * Gets asse
     * @return asse the asse
     */
    public Integer getAsse()
    {
        return asse;
    }
}
