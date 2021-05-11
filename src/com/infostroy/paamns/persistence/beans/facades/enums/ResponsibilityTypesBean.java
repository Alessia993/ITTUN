/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ResponsibilityTypeTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibilityTypes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ResponsibilityTypesBean extends
        EnumerationBean<ResponsibilityTypes>
{
    @SuppressWarnings("unchecked")
    public ResponsibilityTypes LoadByType(ResponsibilityTypeTypes type)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ResponsibilityTypes.class);
        criterion.add(Restrictions.eq("value", type.getResponsibilityType()));
        //.add(Restrictions.eq("deleted", false));
        
        List<ResponsibilityTypes> lst = criterion.list();
        
        if (!lst.isEmpty())
        {
            return lst.get(0);
        }
        
        return null;
    }
}
