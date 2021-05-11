/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureTipologyStepInfos;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ProcedureTipologyStepInfosBean extends
        PersistenceEntityBean<ProcedureTipologyStepInfos>
{
    @SuppressWarnings("unchecked")
    public List<ProcedureTipologyStepInfos> getByProcedure(String procedureId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        return PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(ProcedureTipologyStepInfos.class)
                .add(Restrictions.eq("procedure.id",
                        Long.parseLong(procedureId)))
                .add(Restrictions.eq("deleted", false)).list();
    }
    
    public ProcedureTipologyStepInfos getByProcedureAndTypologyStep(
            Long procedureId, Long typologyStep) throws HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        return (ProcedureTipologyStepInfos) PersistenceSessionManager.getBean()
                .getSession().createCriteria(ProcedureTipologyStepInfos.class)
                .add(Restrictions.eq("procedure.id", procedureId))
                .add(Restrictions.eq("tipologyStep.id", typologyStep))
                .add(Restrictions.eq("deleted", false)).uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    public void deleteByProcedureInTransaction(Long procedureId)
            throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        List<ProcedureTipologyStepInfos> list = PersistenceSessionManager
                .getBean().getSession()
                .createCriteria(ProcedureTipologyStepInfos.class)
                .add(Restrictions.eq("procedure.id", procedureId))
                .add(Restrictions.eq("deleted", false)).list();
        
        if (list != null)
        {
            for (ProcedureTipologyStepInfos item : list)
            {
                BeansFactory.ProcedureTipologyStepInfos().DeleteInTransaction(
                        item);
            }
        }
    }
}
