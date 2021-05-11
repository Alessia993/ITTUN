/**
 * 
 */
package com.infostroy.paamns.web.beans;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public abstract class EntityProjectListBean<T extends Object> extends
        EntityListBean<T>
{
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#PreLoad()
     */
    protected void PreLoad() throws PersistenceBeanException
    {
        if (this.getSession().get("project") == null
                || String.valueOf(this.getSession().get("project")).isEmpty())
        {
            goTo(PagesTypes.PROJECTLIST);
        }
        if (this.getSessionBean().getIsProjectClosed())
        {
            goTo(PagesTypes.PROJECTLIST);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * 
     */
    public abstract void Page_Load() throws NumberFormatException,
            HibernateException, PersistenceBeanException,
            PersistenceBeanException;
    
    /**
     * Gets projectId
     * 
     * @return projectId the projectId
     */
    public String getProjectId()
    {
        return String.valueOf(this.getSession().get("project"));
    }
    
    public ProjectState getProjectState()
    {
        try
        {
            return ProjectState.getByState(BeansFactory.Projects()
                    .Load(this.getProjectId()).getState().getCode());
        }
        catch(NumberFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @return
     * @throws NumberFormatException
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public Projects getProject() throws NumberFormatException,
            HibernateException, PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSession().get("PAAMSN_Session_project_storage") == null
                || !this.getSession().get("stored_project_id").toString()
                        .equals(this.getProjectId()))
        {
            if (this.getProjectId() == null)
            {
                return null;
            }
            
            this.getSession().put("PAAMSN_Session_project_storage",
                    BeansFactory.Projects().Load(this.getProjectId()));
            this.getSession().put("stored_project_id", this.getProjectId());
        }
        
        return (Projects) this.getSession().get(
                "PAAMSN_Session_project_storage");
    }
    
    /**
     * 
     */
    public abstract void addEntity();
    
    /**
     * 
     */
    public abstract void editEntity();
    
    /**
     * 
     */
    public abstract void deleteEntity();
    
    public int getProjectAsse()
    {
        try
        {
            return this.getProject().getAsse();
        }
        catch(NumberFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
}
