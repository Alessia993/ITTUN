/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@javax.persistence.Entity
@Table(name = "dur_info_to_responsible_people")
public class DurInfoToResponsiblePeople extends PersistentEntity
{
    /**
     * @param durComplitation
     * @param person
     */
    public DurInfoToResponsiblePeople(DurInfos durComplitation,
            CFPartnerAnagraphs person)
    {
        super();
        this.durComplitation = durComplitation;
        this.person = person;
    }
    
    public DurInfoToResponsiblePeople()
    {
        super();
    }
    
    /**
     * Stores durComplitation value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "dur_complitation")
    private DurInfos           durComplitation;
    
    /**
     * Stores person value of entity.
     */
    @ManyToOne
    @JoinColumn
    private CFPartnerAnagraphs person;
    
    /**
     * Sets durComplitation
     * @param durComplitation the durComplitation to set
     */
    public void setDurComplitation(DurInfos durComplitation)
    {
        this.durComplitation = durComplitation;
    }
    
    /**
     * Gets durComplitation
     * @return durComplitation the durComplitation
     */
    public DurInfos getDurComplitation()
    {
        return durComplitation;
    }
    
    /**
     * Sets person
     * @param person the person to set
     */
    public void setPerson(CFPartnerAnagraphs person)
    {
        this.person = person;
    }
    
    /**
     * Gets person
     * @return person the person
     */
    public CFPartnerAnagraphs getPerson()
    {
        return person;
    }
}
