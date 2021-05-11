/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class RolesBean extends LocalizableStaticEntityBean<Roles>
{
    public Roles GetRoleByName(UserRoleTypes type)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Roles.class);
        
        criterion.add(Restrictions.eq("code", type.getValue()));
        return (Roles) criterion.uniqueResult();
    }
}
