/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class StateChangeWrapper extends Documents
{
    private ProjectStates from;
    
    private ProjectStates to;
    
    public StateChangeWrapper(ProjectStatesChanges change)
    {
        this.setId(change.getId());
        this.setCreateDate(change.getCreateDate());
        this.setDeleted(change.getDeleted());
        this.setFrom(change.getFromState());
        this.setTo(change.getToState());
        
        if (change.getDocument() != null)
        {
            this.setDocumentDate(change.getDocument().getDocumentDate());
            this.setFileName(change.getDocument().getFileName());
            this.setName(change.getDocument().getName());
            this.setProtocolNumber(change.getDocument().getProtocolNumber());
            this.setUser(change.getDocument().getUser());
            this.setSection(change.getDocument().getSection());
            this.setTitle(change.getDocument().getTitle());
        }
    }
    
    /**
     * Sets from
     * @param from the from to set
     */
    public void setFrom(ProjectStates from)
    {
        this.from = from;
    }
    
    /**
     * Gets from
     * @return from the from
     */
    public ProjectStates getFrom()
    {
        return from;
    }
    
    /**
     * Sets to
     * @param to the to to set
     */
    public void setTo(ProjectStates to)
    {
        this.to = to;
    }
    
    /**
     * Gets to
     * @return to the to
     */
    public ProjectStates getTo()
    {
        return to;
    }
}
