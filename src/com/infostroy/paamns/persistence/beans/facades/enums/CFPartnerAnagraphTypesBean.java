/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.enums;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.EnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CFPartnerAnagraphTypesBean extends
        EnumerationBean<CFPartnerAnagraphTypes>
{
    public CFPartnerAnagraphTypes GetByType(CFAnagraphTypes type)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(CFPartnerAnagraphTypes.class);
        criterion.add(Restrictions.eq("id", type.getCfType()));
        
        return (CFPartnerAnagraphTypes) criterion.uniqueResult();
    }
}
