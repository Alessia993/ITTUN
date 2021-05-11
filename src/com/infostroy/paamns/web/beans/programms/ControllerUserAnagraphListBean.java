/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ControllerUserAnagraphListBean extends
        EntityListBean<ControllerUserAnagraphWebBean>
{
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(new ArrayList<ControllerUserAnagraphWebBean>());
        List<ControllerUserAnagraph> listcontrollers = BeansFactory
                .ControllerUserAnagraph().Load();
        for (ControllerUserAnagraph item : listcontrollers)
        {
            getList().add(new ControllerUserAnagraphWebBean(item));
        }
    }
    
    /**
     * 
     */
    public void deleteItem()
    {
        try
        {
            BeansFactory.ControllerUserAnagraph().Delete(
                    this.getEntityDeleteId());
            ControllerUserAnagraph user = BeansFactory.ControllerUserAnagraph()
                    .Load(this.getEntityDeleteId());
            BeansFactory.Users().Delete(user.getUser());
            this.Page_Load();
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
    }
    
    public void editItem()
    {
        this.getSession().put("controllerUser", this.getEntityEditId());
        goTo(PagesTypes.CONTROLLERUSERANAGRAPHEDIT);
    }
    
    public void addItem()
    {
        this.getSession().put("controllerUser", null);
        goTo(PagesTypes.CONTROLLERUSERANAGRAPHEDIT);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
}
