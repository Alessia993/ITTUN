/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;
import com.infostroy.paamns.web.beans.EntityViewBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class QSNGoalViewBean extends EntityViewBean<QSNGoal>
{
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(BeansFactory.QSNGoal().Load());
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        
    }
    
}
