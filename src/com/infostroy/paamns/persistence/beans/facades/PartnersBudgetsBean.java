/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PartnersBudgetsBean extends PersistenceEntityBean<PartnersBudgets>
{
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("isOld", false));
        criterion.addOrder(Order.asc("year"));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetAllByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProjectAndPartner(String projectId,
            String cfAnagraphId, boolean loadActual)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        
        if (loadActual)
        {
            criterion.add(Restrictions.eq("isOld", false));
        }
        
        if (loadActual)
        {
            criterion.addOrder(Order.asc("year"));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProjectAndPartnerWDenied(
            String projectId, String cfAnagraphId, boolean loadActual)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("version",
                this.GetLastVersionNumber(projectId, cfAnagraphId, null)));
        
        if (loadActual)
        {
            criterion.add(Restrictions.eq("isOld", false));
        }
        
        if (loadActual)
        {
            criterion.addOrder(Order.asc("year"));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProjectAndPartnerNotDenied(
            String projectId, String cfAnagraphId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("version", this
                .GetLastVersionNumberNotDenied(projectId, cfAnagraphId, null)));
        
        criterion.addOrder(Order.asc("year"));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProjectAndUser(String projectId,
            String userId, boolean loadActual) throws PersistenceBeanException
    {
        Criteria criterionPartners = PersistenceSessionManager.getBean()
                .getSession().createCriteria(CFPartnerAnagraphs.class);
        
        criterionPartners.add(Restrictions.eq("deleted", false));
        criterionPartners.add(Restrictions.eq("user.id", Long.valueOf(userId)));
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.in("cfPartnerAnagraph",
                criterionPartners.list()));
        
        if (loadActual)
        {
            criterion.add(Restrictions.eq("isOld", false));
        }
        
        criterion.addOrder(Order.asc("year"));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByProjectPartner(String projectId,
            String cfAnagraphId, boolean loadActual, boolean approved)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("approved", approved));
        
        if (loadActual)
        {
            criterion.add(Restrictions.eq("isOld", false));
        }
        
        criterion.addOrder(Order.desc("createDate"));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByPartner(String projectId,
            String cfAnagraphId, Boolean isApproved)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.addOrder(Order.desc("version"));
        
        if (isApproved != null)
        {
            criterion.add(Restrictions.eq("approved", isApproved));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetByPartnerApproverLessThanVersion(
            String projectId, String cfAnagraphId, Long version, Boolean approve)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        Long lVersion = this.GetVersionNumberLT(projectId, cfAnagraphId,
                version);
        
        if (lVersion != null)
        {
            criterion.add(Restrictions.eq("version", lVersion));
        }
        
        if (approve != null)
        {
            criterion.add(Restrictions.eq("approved", approve));
        }
        
        criterion.addOrder(Order.asc("year"));
        
        return criterion.list();
    }
    
    public Long GetLastVersionNumber(String projectId, String cfAnagraphId,
            Boolean isApproved) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM PartnersBudgets pb ");
        sb.append("WHERE pb.project.id = :projectId AND pb.cfPartnerAnagraph.id = :partnerId ");
        
        if (isApproved != null)
        {
            sb.append("AND pb.approved = :approved");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("partnerId", Long.valueOf(cfAnagraphId));
        query.setParameter("projectId", Long.valueOf(projectId));
        
        if (isApproved != null)
        {
            query.setParameter("approved", isApproved);
        }
        
        return (Long)query.uniqueResult();
    }
    
    public Long GetLastVersionNumberNotDenied(String projectId,
            String cfAnagraphId, Boolean isApproved) throws HibernateException,
            PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM PartnersBudgets pb ");
        sb.append("WHERE pb.project.id = :projectId AND pb.denied = FALSE AND pb.cfPartnerAnagraph.id = :partnerId ");
        
        if (isApproved != null)
        {
            sb.append("AND pb.approved = :approved");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("partnerId", Long.valueOf(cfAnagraphId));
        query.setParameter("projectId", Long.valueOf(projectId));
        
        if (isApproved != null)
        {
            query.setParameter("approved", isApproved);
        }
        
        Object obj = query.uniqueResult();
        
        if (obj != null)
        {
            return Long.valueOf(String.valueOf(query.uniqueResult()));
        }
        else
        {
            return null;
        }
    }
    
    public Long GetVersionNumberLT(String projectId, String cfAnagraphId,
            Long version) throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Select Max(version) FROM PartnersBudgets pb ");
        sb.append("WHERE pb.project.id = :projectId AND pb.cfPartnerAnagraph.id = :partnerId ");
        sb.append("AND pb.approved = :approved AND pb.version < :version");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("partnerId", Long.valueOf(cfAnagraphId));
        query.setParameter("projectId", Long.valueOf(projectId));
        query.setParameter("approved", true);
        query.setParameter("version", version);
        
        Object obj = query.uniqueResult();
        
        if (obj != null)
        {
            return Long.valueOf(String.valueOf(query.uniqueResult()));
        }
        else
        {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetLastNonApprovedByPartner(String projectId,
            String cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.ne("approved", true));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        Long lastVersionNumber = this.GetLastVersionNumber(projectId,
                cfAnagraphId, true);
        Long lastVersionNumberNonApproved = this.GetLastVersionNumber(
                projectId, cfAnagraphId, false);
        if (lastVersionNumber != null)
        {
            criterion.add(Restrictions.ge("version", lastVersionNumber));
            
            if (lastVersionNumberNonApproved != null)
            {
                criterion.add(Restrictions.eq("version",
                        lastVersionNumberNonApproved));
            }
        }
        else
        {
            criterion.add(Restrictions.eq("version",
                    this.GetLastVersionNumber(projectId, cfAnagraphId, null)));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> GetLastByPartner(String projectId,
            String cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id",
                Long.valueOf(cfAnagraphId)));
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        Long lastVersionNumber = this.GetLastVersionNumber(projectId,
                cfAnagraphId, null);
        //Long lastVersionNumberNonApproved = this.GetLastVersionNumber(projectId, cfAnagraphId, true);
        if (lastVersionNumber != null)
        {
            criterion.add(Restrictions.eq("version", lastVersionNumber));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> LoadNotApprovedByProject(Long projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", projectId));
        criterion.add(Restrictions.eq("isOld", false));
        criterion.add(Restrictions.eq("approved", false));
        
        return criterion.list();
        
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> LoadByProjectAndYear(String projectId, int year)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("isOld", false));
        criterion.add(Restrictions.eq("year", year));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> LoadByProject(String projectId)
            throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("denied", false));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        criterion.add(Restrictions.eq("isOld", false));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<PartnersBudgets> LoadByPartnerApproved(String projectId,
            Long cfAnagraphId) throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(PartnersBudgets.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.eq("cfPartnerAnagraph.id", cfAnagraphId));
        criterion.add(Restrictions.eq("approved", true));
        criterion.add(Restrictions.eq("project.id", Long.valueOf(projectId)));
        
        Long lastVersion = this.GetLastVersionNumber(projectId,
                String.valueOf(cfAnagraphId), true);
        
        criterion.add(Restrictions.eq("version", lastVersion));
        
        return criterion.list();
    }
}
