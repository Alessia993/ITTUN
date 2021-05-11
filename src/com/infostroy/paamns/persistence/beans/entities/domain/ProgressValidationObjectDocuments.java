/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "progress_validation_object_documents")
public class ProgressValidationObjectDocuments extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    public ProgressValidationObjectDocuments()
    {
    }
    
    public ProgressValidationObjectDocuments(ProgressValidationObjects obj,
            Documents dco)
    {
        this.setDocument(dco);
        this.setProgressValidationObject(obj);
    }
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long         serialVersionUID = -8311412986612334977L;
    
    /**
     * Stores progressValidationObject value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "progress_validation_object_id")
    private ProgressValidationObjects progressValidationObject;
    
    /**
     * Stores document value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Documents                 document;
    
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
}
