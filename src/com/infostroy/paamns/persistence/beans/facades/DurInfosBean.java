/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class DurInfosBean extends PersistenceEntityBean<DurInfos>
{
    public DurInfos LoadByDurCompilation(Long durCompilationId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurInfos.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("durCompilation.id", durCompilationId));
        
        Object result = criterion.uniqueResult();
        if (result != null)
        {
            return (DurInfos) result;
        }
        else
        {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Integer> LoadNumbers(String skipId, Long project)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT dur_infos.dur_info_number ");
        sb.append("FROM dur_infos INNER JOIN cost_definitions ");
        sb.append("ON dur_infos.dur_compilation_id = cost_definitions.dur_compilations_id ");
        sb.append("WHERE cost_definitions.project_id = :project_id ");
        if (skipId != null)
        {
            sb.append("AND dur_infos.id != :skipId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString());
        
        if (skipId != null)
        {
            query.setParameter("skipId", Long.valueOf(skipId));
        }
        
        query.setLong("project_id", project);
        
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<String> LoadProtocolNumbers(String skipId)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT di.protocolNumber ");
        sb.append("FROM DurInfos di ");
        
        if (skipId != null)
        {
            sb.append("WHERE di.id != :skipId");
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
    public List<DurInfos> LoadByDocument(Long documentId)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT di FROM DurInfos di WHERE di.activityResumeDocument.id = :documentId OR di.financialDetailDocument.id = :documentId");
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("documentId", documentId);
        
        return query.list();
    }
}
