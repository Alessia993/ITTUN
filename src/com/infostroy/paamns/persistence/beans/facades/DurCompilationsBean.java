/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class DurCompilationsBean extends PersistenceEntityBean<DurCompilations>
{
	
	@SuppressWarnings("unchecked")
	public List<DurCompilations> LoadAllWithPaymentApplication()
            throws PersistenceBeanException
    {    	
    	 Criteria criterion = PersistenceSessionManager.getBean().getSession()
                 .createCriteria(DurCompilations.class);
         criterion.add(Restrictions.eq("deleted", false));
         criterion.add((Restrictions.isNotNull("paymentApplication.id")));
         return criterion.list();
    }
	
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadByProject(Long projectId, String filter)
            throws HibernateException, PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cd.durCompilation.id ");
        sb.append("FROM CostDefinitions cd ");
        sb.append("WHERE cd.deleted = false AND ");
        sb.append("cd.project.id = :projectId AND cd.durCompilation.id IS NOT NULL");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", projectId);
        
        List<Long> listIndices = query.list();
        
        if (listIndices == null || listIndices.isEmpty())
        {
            return new ArrayList<DurCompilations>();
        }
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.in("id", listIndices));
        criterion.addOrder(Order.desc("createDate"));
        
        if (!(filter == null || filter.equals("0") || filter.equals("")
                || filter.equals("3") || filter.equals("4")))
        {
            if (filter.equals("1"))
            {
                criterion.add(Restrictions.isNull("type"));
            }
            else
            {
                criterion.add(Restrictions.isNotNull("type"));
            }
            
            criterion.add(Restrictions.disjunction()
                    .add(Restrictions.eq("reimbursement", true))
                    .add(Restrictions.eq("reimbursed", true)));
        }
        else if (filter == null)
        {
            return criterion.list();
        }
        else if (filter.equals("3"))
        {
            criterion.add(Restrictions.eq("durState.id",
                    DurStateTypes.Certifiable.getValue()));
        }
        else if (filter.equals("4"))
        {
            criterion.add(Restrictions.eq("refusedByAcuManager", true));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings({
        "unchecked"
    })
    public List<DurCompilations> LoadForCostCertification(Long projectId,
            Date startDate, Date endDate) throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cd.durCompilation.id ");
        sb.append("FROM CostDefinitions cd ");
        sb.append("WHERE cd.deleted = false AND cd.durCompilation.id IS NOT NULL ");
        
        if (projectId != null)
        {
            sb.append("AND cd.project.id = :projectId");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (projectId != null)
        {
            query.setParameter("projectId", projectId);
        }
        
        List<Long> listIndices = query.list();
        
        if (listIndices == null || listIndices.isEmpty())
        {
            return new ArrayList<DurCompilations>();
        }
        
        List<Long> listTypes = new ArrayList<Long>();
        listTypes.add(DurStateTypes.Certifiable.getValue());
        listTypes.add(DurStateTypes.Certified.getValue());
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.in("id", listIndices));
        criterion.add(Restrictions.in("durState.id", listTypes)); // Possible to
                                                                  // add one
                                                                  // more type
        
        if (startDate != null)
        {
            criterion.add(Restrictions.ge("certifiedDate", startDate));
        }
        
        if (endDate != null)
        {
            criterion.add(Restrictions.le("certifiedDate", endDate));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadForCostCertificationByAsse(Date startDate,
            Date endDate, int asse, int certified, Integer withRequest,
            String protocolNumber, String projectCode,
            Integer paymentRequestNum, Date startPaymRequest,
            Date endPaymRequest) throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cd.durCompilation.id ");
        sb.append("FROM CostDefinitions cd ");
        sb.append("WHERE cd.deleted = false AND cd.durCompilation.id is not null ");
        
        if (asse != 0)
        {
            sb.append("AND cd.project.id IN (SELECT p.id FROM Projects p WHERE p.asse = :asse)");
        }
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        
        if (asse != 0)
        {
            query.setParameter("asse", asse);
        }
        
        List<Long> listIndices = query.list();
        
        if (listIndices == null || listIndices.isEmpty())
        {
            return new ArrayList<DurCompilations>();
        }
        
        List<Long> listTypes = new ArrayList<Long>();
        listTypes.add(DurStateTypes.Certifiable.getValue());
        listTypes.add(DurStateTypes.Certified.getValue());
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.in("id", listIndices));
        criterion.add(Restrictions.in("durState.id", listTypes)); // Possible to
                                                                  // add one
                                                                  // more type
        
        if (startDate != null)
        {
            criterion.add(Restrictions.ge("certifiedDate", startDate));
        }
        
        if (endDate != null)
        {
            criterion.add(Restrictions.le("certifiedDate", endDate));
        }
        
        // Certified value
        if (certified != 0)
        {
            if (certified == 1) // load certified
            {
                criterion.add(Restrictions.in(
                        "durState.id",
                        new ArrayList<Long>(Arrays
                                .asList(DurStateTypes.Certified.getValue()))));
            }
            else if (certified == 2) // load not certified
            {
                criterion.add(Restrictions.not(Restrictions.in(
                        "durState.id",
                        new ArrayList<Long>(Arrays
                                .asList(DurStateTypes.Certified.getValue())))));
            }
        }
        
        // With request
        if (withRequest != null || withRequest != 0)
        {
            if (withRequest == 1) // load with request
            {
                criterion.add(Restrictions.isNotNull("numberPaymentRequest"));
            }
            else if (withRequest == 2)
            {
                criterion.add(Restrictions.isNull("numberPaymentRequest"));
            }
        }
        
        // Protocol number
        if (protocolNumber != null && !protocolNumber.isEmpty())
        {
            StringBuilder sbProt = new StringBuilder();
            sbProt.append("SELECT di.durCompilation.id FROM DurInfos di WHERE di.deleted = false");
            sbProt.append(" AND di.protocolNumber LIKE '%");
            sbProt.append(protocolNumber);
            sbProt.append("%'");
            Query protQuery = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sbProt.toString());
            if (!protQuery.list().isEmpty())
            {
                criterion.add(Restrictions.in("id", protQuery.list()));
            }
            else
            {
                return new ArrayList<DurCompilations>();
            }
        }
        
        // Project code
        if (projectCode != null && !projectCode.isEmpty())
        {
            StringBuilder sbProjCode = new StringBuilder();
            sbProjCode
                    .append("SELECT p.id FROM Projects p WHERE p.deleted = false");
            sbProjCode.append(" AND p.code LIKE '%");
            sbProjCode.append(projectCode);
            sbProjCode.append("%'");
            
            Query projQuery = PersistenceSessionManager.getBean().getSession()
                    .createQuery(sbProjCode.toString());
            
            if (!projQuery.list().isEmpty())
            {
                StringBuilder sbCD = new StringBuilder();
                sbCD.append("SELECT cd.durCompilation.id FROM CostDefinitions cd WHERE ");
                sbCD.append(" cd.durCompilation is not null AND cd.deleted = false AND cd.project.id IN (:listIndices)");
                Query cdQuery = PersistenceSessionManager.getBean()
                        .getSession().createQuery(sbCD.toString());
                
                List<Long> listIndicesCd = projQuery.list();
                cdQuery.setParameterList("listIndices", listIndicesCd);
                
                if (!cdQuery.list().isEmpty())
                {
                    criterion.add(Restrictions.in("id", cdQuery.list()));
                }
                else
                {
                    return new ArrayList<DurCompilations>();
                }
            }
            else
            {
                return new ArrayList<DurCompilations>();
            }
        }
        
        if (paymentRequestNum != null)
        {
            criterion.add(Restrictions.like("numberPaymentRequest",
                    paymentRequestNum));
        }
        
        if (startPaymRequest != null)
        {
            criterion.add(Restrictions.ge("datePaymentRequest",
                    startPaymRequest));
        }
        
        if (endPaymRequest != null)
        {
            criterion
                    .add(Restrictions.le("datePaymentRequest", endPaymRequest));
        }
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadByState(Long projectId, DurStateTypes type)
            throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cd.durCompilation.id ");
        sb.append("FROM CostDefinitions cd ");
        sb.append("WHERE cd.deleted = false AND ");
        sb.append("cd.project.id = :projectId AND cd.durCompilation.id IS NOT NULL ");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", projectId);
        
        List<Long> listIndices = query.list();
        
        if (listIndices == null || listIndices.isEmpty())
        {
            return new ArrayList<DurCompilations>();
        }
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.in("id", listIndices));
        criterion.add(Restrictions.eq("durState.id", type.getValue()));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadByState(DurStateTypes type)
            throws PersistenceBeanException
    {    	
    	 Criteria criterion = PersistenceSessionManager.getBean().getSession()
                 .createCriteria(DurCompilations.class);
         criterion.add(Restrictions.eq("deleted", false));
         criterion.add(Restrictions.isNull("expenditureDeclaration.id"));
         criterion.add(Restrictions.eq("durState.id", type.getValue()));
         
         return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadByState(DurStateTypes type, boolean searchExpenditureDeclaration)
            throws PersistenceBeanException
    {    	
    	 Criteria criterion = PersistenceSessionManager.getBean().getSession()
                 .createCriteria(DurCompilations.class);
         criterion.add(Restrictions.eq("deleted", false));
         criterion.add(Restrictions.eq("durState.id", type.getValue()));
         criterion.add((Restrictions.isNull("paymentApplication.id")));
         if(searchExpenditureDeclaration){
        	 criterion.add(Restrictions.isNotNull("expenditureDeclaration.id"));
         }
         return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    /**
     * This method load both reimbursement and reimbursed entities
     */
    public List<DurCompilations> LoadForReimbursement(Long projectId)
            throws PersistenceBeanException
    {
        StringBuilder sbDurs = new StringBuilder();
        sbDurs.append("SELECT dc FROM DurCompilations dc WHERE dc.deleted = false AND ");
        sbDurs.append("(dc.reimbursement = true OR dc.reimbursed = true) AND ");
        sbDurs.append("dc.id IN (");
        sbDurs.append("SELECT cd.durCompilation.id "
                + "FROM CostDefinitions cd "
                + "WHERE cd.deleted = false AND "
                + "cd.project.id = :projectId AND cd.durCompilation.id IS NOT NULL");
        sbDurs.append(")");
        
        Query queryDurs = PersistenceSessionManager.getBean().getSession()
                .createQuery(sbDurs.toString());
        queryDurs.setParameter("projectId", projectId);
        
        return queryDurs.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadReimbursedOnly(Long projectId)
            throws PersistenceBeanException
    {
        StringBuilder sbDurs = new StringBuilder();
        sbDurs.append("SELECT dc FROM DurCompilations dc WHERE dc.deleted = false AND ");
        sbDurs.append("dc.reimbursed = true AND ");
        sbDurs.append("dc.id IN (");
        sbDurs.append("SELECT cd.durCompilation.id "
                + "FROM CostDefinitions cd "
                + "WHERE cd.deleted = false AND "
                + "cd.project.id = :projectId AND cd.durCompilation.id IS NOT NULL");
        sbDurs.append(")");

        Query queryDurs = PersistenceSessionManager.getBean().getSession()
                .createQuery(sbDurs.toString());
        queryDurs.setParameter("projectId", projectId);
        
        return queryDurs.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> LoadByState(Long projectId,
            List<DurStateTypes> types) throws PersistenceBeanException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cd.durCompilation.id ");
        sb.append("FROM CostDefinitions cd ");
        sb.append("WHERE cd.deleted = false AND ");
        sb.append("cd.project.id = :projectId AND cd.durCompilation.id IS NOT NULL ");
        
        Query query = PersistenceSessionManager.getBean().getSession()
                .createQuery(sb.toString());
        query.setParameter("projectId", projectId);
        
        List<Long> listIndices = query.list();
        
        if (listIndices == null || listIndices.isEmpty())
        {
            return new ArrayList<DurCompilations>();
        }
        
        List<Long> listStateValues = new ArrayList<Long>();
        
        for (DurStateTypes dst : types)
        {
            listStateValues.add(dst.getValue());
        }
        
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        criterion.add(Restrictions.eq("deleted", false));
        criterion.add(Restrictions.in("id", listIndices));
        criterion.add(Restrictions.in("durState.id", listStateValues));
        
        return criterion.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> loadPaymentRequests(Long projectId)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        
        criteria.add(Restrictions.isNotNull("numberPaymentRequest"));
        criteria.addOrder(Order.desc("datePaymentRequest"));
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DurCompilations> loadByPaymentNumber(Integer paymentNumber)
            throws PersistenceBeanException
    {
        Criteria criteria = PersistenceSessionManager.getBean().getSession()
                .createCriteria(DurCompilations.class);
        
        criteria.add(Restrictions.eq("numberPaymentRequest", paymentNumber));
        return criteria.list();
    }
}
