/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.UsersLogins;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class LoginHistoryBean extends EntityListBean<UsersLogins>
{
    @Override
    public void Page_Load() throws PersistenceBeanException
    {
        setList(BeansFactory.UsersLogins().Load());
    }
    
    /**
     * @throws PersistenceBeanException
     * 
     */
    public void deleteItem() throws PersistenceBeanException
    {
        try
        {
            BeansFactory.UsersLogins().Delete(getEntityDeleteId());
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
    
    /**
     * 
     */
    public void deleteAll()
    {
        BeansFactory.UsersLogins().DeleteAll();
        try
        {
            this.Page_Load();
            this.ReRenderScroll();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        
    }
}
