/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class ProjectEditBean extends EntityEditBean<Projects>
{
    private Projects currentProject;
    
    /**
     * Stores project code
     */
    private String   code;
    
    private String   title;
    
    private Long     id;
    
    private boolean  hasRights;
    
    @Override
    public void AfterSave()
    {
        if (this.getSession().get("projectEdit") != null)
        {
            this.GoBack();
        }
        else
        {
            this.getSession().put("project", this.getId());
            this.goTo(PagesTypes.PROJECTSTART);
        }
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.PROJECTLIST);
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSessionBean().isAGUW() || this.getSessionBean().isSTCW())
        {
            this.setHasRights(true);
        }
        else
        {
            this.setHasRights(false);
        }
        
        if (this.getSession().get("projectEdit") != null)
        {
            this.currentProject = BeansFactory.Projects().Load(
                    String.valueOf(this.getSession().get("projectEdit")));
            
            this.setCode(this.currentProject.getCode());
            this.setTitle(this.currentProject.getTitle());
        }
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        // Empty for a while
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException, PersistenceBeanException
    {
        Projects entityToSave = new Projects();
        
        if (this.getSession().get("projectEdit") != null)
        {
            entityToSave = BeansFactory.Projects().Load(
                    String.valueOf(this.getSession().get("projectEdit")));
        }
        else
        {
            entityToSave.setState(BeansFactory.ProjectStates().GetProjectState(
                    ProjectState.FoundingEligible));
        }
        
        entityToSave.setCode(this.getCode());
        entityToSave.setTitle(this.getTitle());
        
        BeansFactory.Projects().SaveInTransaction(entityToSave);
        
        this.setId(entityToSave.getId());
    }
    
    /**
     * Sets code
     * 
     * @param code
     *            the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * 
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets title
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * Gets title
     * 
     * @return title the title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Sets id
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * Gets id
     * 
     * @return id the id
     */
    public Long getId()
    {
        return id;
    }
    
    /**
     * Sets hasRights
     * 
     * @param hasRights
     *            the hasRights to set
     */
    public void setHasRights(boolean hasRights)
    {
        this.hasRights = hasRights;
    }
    
    /**
     * Gets hasRights
     * 
     * @return hasRights the hasRights
     */
    public boolean isHasRights()
    {
        return hasRights;
    }
}
