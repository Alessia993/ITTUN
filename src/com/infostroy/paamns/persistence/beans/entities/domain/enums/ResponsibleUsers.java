/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "responsible_users")
public class ResponsibleUsers extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 6272379123473072602L;
    
    /**
     * Do not remove this overrided method.
     */
    public String toString()
    {
        try
        {
            return this.getValue();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponsibleUsers.class.toString();
    }
}
