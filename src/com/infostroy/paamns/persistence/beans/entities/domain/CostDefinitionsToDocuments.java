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
@Table(name = "cost_defenitions_documents")
public class CostDefinitionsToDocuments extends Entity
{
    /**
     * @param document
     * @param costDefinition
     */
    public CostDefinitionsToDocuments(Documents document,
            CostDefinitions costDefinition)
    {
        super();
        this.document = document;
        this.costDefinition = costDefinition;
    }
    
    public CostDefinitionsToDocuments()
    {
        
    }
    
    /**
     * Stores document value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Documents       document;
    
    /**
     * Stores costDefinition value of entity.
     */
    @ManyToOne
    @JoinColumn
    private CostDefinitions costDefinition;
    
    public void setDocument(Documents document)
    {
        this.document = document;
    }
    
    public Documents getDocument()
    {
        return document;
    }
    
    public void setCostDefinition(CostDefinitions costDefinition)
    {
        this.costDefinition = costDefinition;
    }
    
    public CostDefinitions getCostDefinition()
    {
        return costDefinition;
    }
}
