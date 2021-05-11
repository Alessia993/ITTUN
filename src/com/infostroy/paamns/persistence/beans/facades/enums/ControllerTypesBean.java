/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ControllerTypeTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ControllerTypes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ControllerTypesBean extends
        LocalizableEnumerationBean<ControllerTypes>
{
    public ControllerTypes GetControllerType(ControllerTypeTypes type)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ControllerTypes.class);
        
        criterion.add(Restrictions.eq("code", type.getControllerType()));
        
        return (ControllerTypes) criterion.uniqueResult();
    }
    
    public ControllerTypes GetControllerType(String code)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ControllerTypes.class);
        
        criterion.add(Restrictions.eq("code", code));
        
        return (ControllerTypes) criterion.uniqueResult();
    }
}
