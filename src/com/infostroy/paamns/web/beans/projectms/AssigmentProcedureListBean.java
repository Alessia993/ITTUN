/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AssignmentProcedures;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class AssigmentProcedureListBean extends
        EntityProjectListBean<AssignmentProcedures>
{
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        //setList(BeansFactory.AssignmentProcedures().LoadByProject(
                //this.getProjectId()));
    	if (this.getSessionBean().isSTC() || this.getSessionBean().isCIL() || this.getSessionBean().isAGU() || this.getSessionBean().isUC())
    	{
    		setList(BeansFactory.AssignmentProcedures().LoadByProject(this.getProjectId()));
    	}
    	else {
    		setList(BeansFactory.AssignmentProcedures().LoadByProjectAndUser(this.getProjectId(), this.getCurrentUser().getId().toString()));
    	}
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        this.getSession().put("assigmentProcedure", null);
        this.getSession().put("assigmentProcedureView", null);
        this.goTo(PagesTypes.ASSIGMENTPROCEDUREEDIT);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        try
        {
            BeansFactory.AssignmentProcedures()
                    .Delete(this.getEntityDeleteId());
            
            this.Page_Load();
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
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        this.getSession().put("assigmentProcedure", this.getEntityEditId());
        this.getSession().put("assigmentProcedureView", null);
        this.goTo(PagesTypes.ASSIGMENTPROCEDUREEDIT);
    }
    
    /**
     * 
     */
    public void viewEntity()
    {
        this.getSession().put("assigmentProcedure", this.getEntityEditId());
        this.getSession().put("assigmentProcedureView", true);
        this.goTo(PagesTypes.ASSIGMENTPROCEDUREEDIT);
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
}
