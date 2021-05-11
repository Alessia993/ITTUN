/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;

/**
 * 
 * @author Sergey Manoylo
 * @author Alexander Chelombitko
 * 
 *         InfoStroy Co., 2010.
 * 
 */
public class PhisicalInitializationToProgramIndicatorsBean extends
        PersistenceEntityBean<PhisicalInitializationToProgramIndicators>
{
    @SuppressWarnings("unchecked")
    public Long[] GetByProject(String projectId, ProgramIndicatorsType pi)
            throws PersistenceBeanException
    {
        Long[] idss = BeansFactory.PhisicalInizializations().GetByProject(
                projectId);
        if (idss.length > 0)
        {
            List<PhisicalInitializationToProgramIndicators> list = PersistenceSessionManager
                    .getBean()
                    .getSession()
                    .createCriteria(
                            PhisicalInitializationToProgramIndicators.class)
                    .add(Restrictions.in("phisicalInitialization.id", idss))
                    .list();
            
            List<Long> ids = new ArrayList<Long>();
            for (PhisicalInitializationToProgramIndicators item : list)
            {
                ids.add(item.getProgramIndicator().getId());
            }
            return ids.toArray(new Long[0]);
        }
        else
        {
            return new Long[0];
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToProgramIndicators> getByProject(
            String projectId, ProgramIndicatorsType pi)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select pipi.* "
                + "from phisical_initialization_to_program_indicators as pipi, "
                + "program_indicators as pind, "
                + "phisical_inizializations as phi, " + "projects as proj "
                + "where pipi.indicator_id = pind.id "
                + "and pind.type=:indiType "
                + "and pipi.phisical_initialization_id=phi.id "
                + "and phi.project_id=proj.id " + "and proj.id= :projectId");
        
        SQLQuery q = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString())
                .addEntity(PhisicalInitializationToProgramIndicators.class);
        q.setLong("projectId", Long.parseLong(projectId));
        q.setInteger("indiType", pi.getCode());
        return q.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToProgramIndicators> LoadLastByProject(
            String projectId, ProgramIndicatorsType pi)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        Long lastVersion = this.GetLastVersion(projectId);
        
        sb.append("select pipi.* "
                + "from phisical_initialization_to_program_indicators as pipi, "
                + "program_indicators as pind, "
                + "phisical_inizializations as phi, " + "projects as proj "
                + "where pipi.indicator_id = pind.id "
                + "and pind.type=:indiType "
                + "and pipi.phisical_initialization_id=phi.id "
                + "and phi.project_id=proj.id " + "and proj.id= :projectId ");
        
        if (lastVersion != null)
        {
            sb.append("and pipi.version = :version");
        }
        
        SQLQuery q = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString())
                .addEntity(PhisicalInitializationToProgramIndicators.class);
        q.setLong("projectId", Long.parseLong(projectId));
        q.setInteger("indiType", pi.getCode());
        
        if (lastVersion != null)
        {
            q.setLong("version", lastVersion);
        }
        
        return q.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PhisicalInitializationToProgramIndicators> LoadByProgressValidationObject(
            Long progressValidationObjectId, ProgramIndicatorsType pi)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select pipi.* "
                + "from phisical_initialization_to_program_indicators as pipi, "
                + "program_indicators as pind, "
                + "phisical_inizializations as phi, "
                + "projects as proj "
                + "where pipi.indicator_id = pind.id "
                + "and pind.type=:indiType "
                + "and pipi.phisical_initialization_id=phi.id "
                + "and phi.project_id=proj.id "
                + "and pipi.progress_validation_object_id = :progressValidationObjectId ");
        
        SQLQuery q = PersistenceSessionManager.getBean().getSession()
                .createSQLQuery(sb.toString())
                .addEntity(PhisicalInitializationToProgramIndicators.class);
        
        q.setInteger("indiType", pi.getCode());
        q.setLong("progressValidationObjectId", progressValidationObjectId);
        return q.list();
    }
    
    public Long GetLastVersion(String projectId) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM PhisicalInitializationToProgramIndicators p ");
        sb.append("WHERE p.phisicalInitialization.id IN (SELECT pi.id FROM PhisicalInitializations pi WHERE pi.project.id = :projectId )");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", Long.valueOf(projectId));
        
        return (Long) query.uniqueResult();
    }
}
