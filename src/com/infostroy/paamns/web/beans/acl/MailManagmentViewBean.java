/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.SimpleMailSender;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class MailManagmentViewBean extends EntityListBean<Mails>
{
    private String           filterValue;
    
    private Long             rowId;
    
    private List<SelectItem> filterValues;
    
    private boolean          selectAll;
    
    private Long             sendEntityId;
    
    public void addToCache()
    {
        List<Long> ids = new ArrayList<Long>();
        for (Mails item : getList())
        {
            if (item.getSelected())
            {
                ids.add(item.getId());
            }
        }
        
        setSelectAll(false);
        this.getViewState().put("mails", ids);
    }
    
    public void doSelectAll()
    {
        List<Long> ids = new ArrayList<Long>();
        if (getSelectAll())
        {
            for (Mails item : getList())
            {
                ids.add(item.getId());
            }
        }
        
        this.getViewState().put("mails", ids);
        
        try
        {
            Page_Load();
        }
        catch(NumberFormatException e)
        {
            log.error(e);
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
     */
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getFilterValue() == null || this.getFilterValue().isEmpty())
        {
            this.setFilterValue("all");
        }
        
        this.FillList();
    }
    
    public void changeFilter() {
		if (this.getShowScroll())
		{
			resetDataScroller(null);
		}
   	
    }
    
    @SuppressWarnings("unchecked")
    private void FillList() throws HibernateException, PersistenceBeanException
    {
        setList(new ArrayList<Mails>());
        List<Mails> listMails = new ArrayList<Mails>();
        
        if (this.getFilterValue().equals("all"))
        {
            listMails = BeansFactory.Mails().Load();
        }
        else if (this.getFilterValue().equals("success"))
        {
            listMails = BeansFactory.Mails().Load(true);
        }
        else if (this.getFilterValue().equals("fail"))
        {
            listMails = BeansFactory.Mails().Load(false);
        }
        
        List<Long> ids = (List<Long>) this.getViewState().get("mails");
        
        if (ids == null)
        {
            ids = new ArrayList<Long>();
        }
        for (Mails item : listMails)
        {
            if (item.getUser() != null && !item.getUser().getDeleted())
            {
                item.setSelected(ids.contains(item.getId()));
                getList().add(item);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        try
        {
            BeansFactory.Mails().Delete(this.getEntityDeleteId());
            this.Page_Load();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void deleteEntities()
    {
        try
        {
            Transaction tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            
            List<Long> ids = (List<Long>) this.getViewState().get("mails");
            for (Long id : ids)
            {
                BeansFactory.Mails().DeleteInTransaction(id);
            }
            tr.commit();
            this.getViewState().put("mails", null);
            this.setSelectAll(false);
            
            this.Page_Load();
            this.ReRenderScroll();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        
    }
    
    public void sendEntity()
    {
        try
        {
            Mails mail = BeansFactory.Mails().Load(this.getSendEntityId());
            //            new SimpleMailSender(new InfoObject(mail.getUser().getMail(), mail
            //                    .getUser().getName(), mail.getUser().getSurname(),
            //                    mail.getMessage())).start();
            
            Transaction tr = null;
            try
            {
                tr = PersistenceSessionManager.getBean().getSession()
                        .beginTransaction();
                List<Mails> listMails = new ArrayList<Mails>();
                
                SimpleMailSender simpleMailSender = new SimpleMailSender(
                        new InfoObject(mail.getUser().getMail(), mail.getUser()
                                .getName(), mail.getUser().getSurname(),
                                mail.getMessage()));
                listMails.addAll(simpleMailSender
                        .completeMails(PersistenceSessionManager.getBean()
                                .getSession()));
                
                if (listMails != null && !listMails.isEmpty())
                {
                    for (Mails m : listMails)
                    {
                        BeansFactory.Mails().SaveInTransaction(m);
                    }
                    
                }
                
            }
            catch(Exception e)
            {
                if (tr != null)
                {
                    tr.rollback();
                }
                log.error(e);
            }
            finally
            {
                if (tr != null && !tr.wasRolledBack() && tr.isActive())
                {
                    tr.commit();
                }
            }
            BeansFactory.Mails().Delete(this.getSendEntityId());
            this.Page_Load();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void sentEntities() throws HibernateException,
            PersistenceBeanException
    {
        //        Transaction tr = PersistenceSessionManager.getBean().getSession()
        //                .beginTransaction();
        //        List<InfoObject> mails = new ArrayList<InfoObject>();
        //        List<Long> ids = (List<Long>) this.getViewState().get("mails");
        //        for (Long id : ids)
        //        {
        //            Mails item = BeansFactory.Mails().Load(id);
        //            mails.add(new InfoObject(item.getUser().getMail(), item.getUser()
        //                    .getName(), item.getUser().getSurname(), item.getMessage()));
        //            BeansFactory.Mails().DeleteInTransaction(id);
        //        }
        //        //new SimpleMailSender(mails).start();
        //        
        //        tr.commit();
        
        Transaction tr = null;
        try
        {
            List<InfoObject> mails = new ArrayList<InfoObject>();
            List<Long> ids = (List<Long>) this.getViewState().get("mails");
            for (Long id : ids)
            {
                Mails item = BeansFactory.Mails().Load(id);
                mails.add(new InfoObject(item.getUser().getMail(), item
                        .getUser().getName(), item.getUser().getSurname(), item
                        .getMessage()));
                BeansFactory.Mails().DeleteInTransaction(id);
            }
            //
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            List<Mails> listMails = new ArrayList<Mails>();
            
            SimpleMailSender simpleMailSender = new SimpleMailSender(mails);
            listMails.addAll(simpleMailSender
                    .completeMails(PersistenceSessionManager.getBean()
                            .getSession()));
            
            if (listMails != null && !listMails.isEmpty())
            {
                for (Mails m : listMails)
                {
                    BeansFactory.Mails().SaveInTransaction(m);
                }
                
            }
            
        }
        catch(Exception e)
        {
            if (tr != null)
            {
                tr.rollback();
            }
            log.error(e);
        }
        finally
        {
            if (tr != null && !tr.wasRolledBack() && tr.isActive())
            {
                tr.commit();
            }
        }
        this.getViewState().put("mails", null);
        this.setSelectAll(false);
        this.Page_Load();
    }
    
    /**
     * Sets filterValueD
     * 
     * @param filterValueD
     *            the filterValueD to set
     */
    public void setFilterValue(String filterValue)
    {
        this.filterValue = filterValue;
        this.getViewState().put("filterValue", filterValue);
        try
        {
            this.FillList();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    /**
     * Gets filterValueD
     * 
     * @return filterValueD the filterValueD
     */
    public String getFilterValue()
    {
        this.filterValue = (String) this.getViewState().get("filterValue");
        return this.filterValue;
    }
    
    /**
     * Sets rowId
     * 
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(Long rowId)
    {
        this.rowId = rowId;
    }
    
    /**
     * Gets rowId
     * 
     * @return rowId the rowId
     */
    public Long getRowId()
    {
        return rowId;
    }
    
    /**
     * Sets filterValuesD
     * 
     * @param filterValuesD
     *            the filterValuesD to set
     */
    public void setFilterValues(List<SelectItem> filterValuesD)
    {
        this.filterValues = filterValuesD;
    }
    
    /**
     * Gets filterValuesD
     * 
     * @return filterValuesD the filterValuesD
     */
    public List<SelectItem> getFilterValues()
    {
        return filterValues;
    }
    
    /**
     * Sets selectAll
     * 
     * @param selectAll
     *            the selectAll to set
     */
    public void setSelectAll(boolean selectAll)
    {
        this.selectAll = selectAll;
    }
    
    /**
     * Gets selectAll
     * 
     * @return selectAll the selectAll
     */
    public boolean getSelectAll()
    {
        return selectAll;
    }
    
    /**
     * Sets sendEntityId
     * 
     * @param sendEntityId
     *            the sendEntityId to set
     */
    public void setSendEntityId(Long sendEntityId)
    {
        this.sendEntityId = sendEntityId;
    }
    
    /**
     * Gets sendEntityId
     * 
     * @return sendEntityId the sendEntityId
     */
    public Long getSendEntityId()
    {
        return sendEntityId;
    }
}
