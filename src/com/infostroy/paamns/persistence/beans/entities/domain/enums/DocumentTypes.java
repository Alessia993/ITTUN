/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "document_types")
public class DocumentTypes extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -2323633568842588480L;
    
}
