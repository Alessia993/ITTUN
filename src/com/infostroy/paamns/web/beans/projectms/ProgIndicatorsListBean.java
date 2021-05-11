/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2014.
 *
 */
public class ProgIndicatorsListBean extends
		EntityListBean<ProjectIndicators>
{

	private Boolean editAvailable;
	
	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */

	
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setEditAvailable(Boolean.TRUE);
        if (this.getSession().get("project") != null)
        {
            this.setList(BeansFactory.ProjectIndicators().LoadByProject(
                    String.valueOf(this.getSession().get("project")), "2"));
            
            this.setEditAvailable(Boolean.valueOf(BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project"))).getState().getCode()!=2));
        }
		
        
		
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

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		this.getSession().put("communicationIndicators", null);
		this.getSession().put("programIndicators", null);
		this.getSession().put("resultIndicators", null);
		this.getSession().put("projectIndicators", null);
		this.getSession().put("isCommunicationIndicator", false);
		this.getSession().put("isProgramIndicator", true);
		this.getSession().put("isResultIndicator", false);
		this.getSession().put("isProjectIndicator", false);
		this.goTo(PagesTypes.PROJECTINDICATORSEDIT);
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		this.getSession().put("communicationIndicators", null);
		this.getSession().put("resultIndicators", null);
		this.getSession().put("projectIndicators", null);
		this.getSession().put("programIndicators", this.getEntityEditId());
		this.getSession().put("isCommunicationIndicator", false);
		this.getSession().put("isProgramIndicator", true);
		this.getSession().put("isResultIndicator", false);
		this.getSession().put("isProjectIndicator", false);
		this.goTo(PagesTypes.PROJECTINDICATORSEDIT);
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
        try
        {
            BeansFactory.ProjectIndicators().Delete(
                    String.valueOf(getEntityDeleteId()));
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
        catch(NumberFormatException e)
        {
            log.error(e);
        }
		
	}

	public Boolean getEditAvailable()
	{
		return editAvailable;
	}

	public void setEditAvailable(Boolean editAvailable)
	{
		this.editAvailable = editAvailable;
	}
	
}
