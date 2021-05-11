/**
 * 
 */
package com.infostroy.paamns.web.beans;

import java.io.IOException;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public abstract class EntityProjectEditBean<T extends Entity> extends
        EntityEditBean<T>
{
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#PreLoad()
     */
    protected void PreLoad() throws PersistenceBeanException
    {
        if (this.getSession().get("project") == null
                || String.valueOf(this.getSession().get("project")).isEmpty())
        {
            goTo(PagesTypes.PROJECTLIST);
        }
        if (this.getSessionBean().getIsProjectClosed()
                && !this.getRequestUrl().contains("StateManagment"))
        {
            goTo(PagesTypes.PROJECTLIST);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public abstract void AfterSave();
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public abstract void GoBack();
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public abstract void Page_Load() throws NumberFormatException,
            HibernateException, PersistenceBeanException, IOException,
            NullPointerException;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public abstract void Page_Load_Static() throws PersistenceBeanException;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public abstract void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException;
    
    public ProjectState getProjectState()
    {
        try
        {
            return ProjectState.getByState(BeansFactory.Projects()
                    .Load(this.getProjectId()).getState().getCode());
        }
        catch(NumberFormatException e)
        {
            log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
        	  log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
        	  log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        return null;
    }
    
    public int getProjectAsse()
    {
        try
        {
            return this.getProject().getAsse();
        }
        catch(NumberFormatException e)
        {
        	  log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
        	  log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
        	  log.error("EntityProjectEditBean exception:", e);
            e.printStackTrace();
        }
        return -1;
    }
}
