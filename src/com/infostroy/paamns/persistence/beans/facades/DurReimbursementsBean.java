/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class DurReimbursementsBean extends
        PersistenceEntityBean<DurReimbursements>
{
    public DurReimbursements LoadByDurCompilation(Long durCompilationId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurReimbursements.class);
        
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
        
        Object result = criterion.uniqueResult();
        
        if (result != null)
        {
            return (DurReimbursements) result;
        }
        else
        {
            return new DurReimbursements();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<DurReimbursements> LoadByProject(Long projectId)
            throws PersistenceBeanException
    {
        List<Long> listIndices = new ArrayList<Long>();
        List<DurCompilations> listDurs = BeansFactory.DurCompilations()
                .LoadByProject(projectId, null);
        
        for (DurCompilations dc : listDurs)
        {
            listIndices.add(dc.getId());
        }
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurReimbursements.class);
        
        criterion.add(Restrictions.eq("deleted", false));
        
        if (listIndices != null && !listIndices.isEmpty())
        {
            criterion.add(Restrictions.in("durCompilation.id", listIndices));
        }
        else
        {
            return new ArrayList<DurReimbursements>();
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Integer> LoadReimbursementNumbers(String skipId)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT dr.fesrReimbursmentNumber ");
        sb.append("FROM FESRInfo dr ");
        
        if (skipId != null)
        {
            sb.append("WHERE dr.id != :skipId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (skipId != null)
        {
            query.setParameter("skipId", Long.valueOf(skipId));
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Integer> LoadRotationReimbursementNumbers(String skipId)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT dr.rotationReimbursmentNumber ");
        sb.append("FROM RotationFoundInfo dr ");
        
        if (skipId != null)
        {
            sb.append("WHERE dr.id != :skipId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (skipId != null)
        {
            query.setParameter("skipId", Long.valueOf(skipId));
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<String> LoadFesrCROs(String skipId) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT dr.fesrCro ");
        sb.append("FROM FESRInfo dr ");
        
        if (skipId != null)
        {
            sb.append("WHERE dr.id != :skipId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (skipId != null)
        {
            query.setParameter("skipId", Long.valueOf(skipId));
        }
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<String> LoadRotationCROs(String skipId)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT dr.rptationCro ");
        sb.append("FROM RotationFoundInfo dr ");
        
        if (skipId != null)
        {
            sb.append("WHERE dr.id != :skipId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (skipId != null)
        {
            query.setParameter("skipId", Long.valueOf(skipId));
        }
        
        return query.list();
    }
}
