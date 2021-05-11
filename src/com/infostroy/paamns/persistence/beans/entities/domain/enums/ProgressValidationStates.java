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
@Table(name = "progress_validation_states")
public class ProgressValidationStates extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -8830940451726080170L;
    
}
