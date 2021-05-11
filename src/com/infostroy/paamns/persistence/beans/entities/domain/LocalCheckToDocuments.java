/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@javax.persistence.Entity
@Table(name = "local_check_documents")
public class LocalCheckToDocuments extends Entity
{
    public LocalCheckToDocuments(Documents doc, LocalChecks localCheck)
    {
        this.setDocument(doc);
        this.setLocalCheck(localCheck);
    }
    
    public LocalCheckToDocuments()
    {
        
    }
    
    @ManyToOne
    @JoinColumn
    private Documents   document;
    
    @ManyToOne
    @JoinColumn
    private LocalChecks localCheck;
    
    /**
     * Sets localCheck
     * @param localCheck the localCheck to set
     */
    public void setLocalCheck(LocalChecks localCheck)
    {
        this.localCheck = localCheck;
    }
    
    /**
     * Gets localCheck
     * @return localCheck the localCheck
     */
    public LocalChecks getLocalCheck()
    {
        return localCheck;
    }
    
    /**
     * Sets document
     * @param document the document to set
     */
    public void setDocument(Documents document)
    {
        this.document = document;
    }
    
    /**
     * Gets document
     * @return document the document
     */
    public Documents getDocument()
    {
        return document;
    }
}
