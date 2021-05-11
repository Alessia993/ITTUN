/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Responsibilities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibilityTypes;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ResponsibilitiesBean extends
        PersistenceEntityBean<Responsibilities>
{
    public Responsibilities LoadByCFAnagraphAndType(Long cfAnagraphId,
            ResponsibilityTypes type) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(Responsibilities.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id", cfAnagraphId));
        criterion.add(Restrictions.eq("type.id", type.getId()));
        
        return (Responsibilities) criterion.uniqueResult();
    }
}
