/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public enum MailStatuses
{
    Completed(1l),
    Sent(2l),
    Error(3l);  
    
    private MailStatuses(Long id)
    {
        this.id = id;
    }

    private Long id;

    /**
     * Gets id
     * @return id the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets id
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    
}
