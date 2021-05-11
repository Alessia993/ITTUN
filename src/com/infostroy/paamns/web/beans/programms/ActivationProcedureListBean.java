/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class ActivationProcedureListBean extends
        EntityListBean<ActivationProcedureAnagraph>
{
    @Override
    public void Page_Load() throws PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSessionBean().isAGU() || this.getSessionBean().isANCM())
        {
            if (this.getSessionBean().isSTC())
            {
                setList(BeansFactory.ActivationProcedureAnagraph().LoadSorted());
            }
            else
            {
                setList(BeansFactory.ActivationProcedureAnagraph().LoadByUser(
                        true));
            }
        }
        else if (this.getSessionBean().isSTC())
        {
            setList(BeansFactory.ActivationProcedureAnagraph()
                    .LoadByUser(false));
        }
        else
        {
            setList(BeansFactory.ActivationProcedureAnagraph().Load());
        }
    }
    
    public void deleteItem()
    {
        try
        {
            List<ActivationProcedureInfo> listActInfos = BeansFactory
                    .ActivationProcedureInfo().GetInfoForProcedure(
                            this.getEntityDeleteId());
            for (ActivationProcedureInfo actInfo : listActInfos)
            {
                BeansFactory.ActivationProcedureInfo().Delete(actInfo);
            }
            
            List<Projects> listProjects = BeansFactory.Projects()
                    .LoadByActivationProcedure(
                            String.valueOf(this.getEntityDeleteId()));
            
            for (Projects project : listProjects)
            {
                if (this.getSession().get("project") != null)
                {
                    if (project.getId().equals(
                            Long.valueOf(String.valueOf(this.getSession().get(
                                    "project")))))
                    {
                        this.getSession().remove("project");
                    }
                }
                
                BeansFactory.Projects().Delete(project);
            }
            
            BeansFactory.ActivationProcedureAnagraph().Delete(
                    getEntityDeleteId());
            
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
    
    public void viewProcedure()
    {
        this.getSession().put("actProc", this.getEntityEditId());
        this.getSession().put("viewOnly", true);
        this.goTo(PagesTypes.ACTIVATEPROCEDUREEDIT);
    }
    
    public void addProcedure()
    {
        this.getSession().put("actProc", null);
        this.getSession().put("viewOnly", false);
        this.goTo(PagesTypes.ACTIVATEPROCEDUREEDIT);
    }
    
    public void editProcedure()
    {
        this.getSession().put("actProc", this.getEntityEditId());
        this.getSession().put("viewOnly", false);
        this.goTo(PagesTypes.ACTIVATEPROCEDUREEDIT);
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
