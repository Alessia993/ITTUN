/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class GiuridicalEngageListBean extends
        EntityProjectListBean<GiuridicalEngages>
{
    	
	
	private HtmlDataScroller		scroller;
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
	{ 
		if (this.getProjectId() != null)
		{

			/*if (this.getSessionBean().isCF())
			{
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndFilters(this.getProjectId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}
			else
			{ */
			

			if (this.getSessionBean().isSTC() || this.getSessionBean().isCIL() || this.getSessionBean().isAGU() || this.getSessionBean().isUC())
			{
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndFilters(this.getProjectId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}
			else
			{ 
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndUserPartnerAndFilters(
								this.getProjectId(),
								this.getCurrentUser().getId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}
			//}

		}
		else if (this.getProject() != null)
		{
			/*if (this.getSessionBean().isCF())
			{
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndFilters(this.getProjectId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}
			else
			{ */
			
			if (this.getSessionBean().isSTC() || this.getSessionBean().isCIL() || this.getSessionBean().isAGU() || this.getSessionBean().isUC())
			{
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndFilters(this.getProjectId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}
			else
			{ 
				setList(BeansFactory.GiuridicalEngages()
						.getByProjectAndUserPartnerAndFilters(
								String.valueOf(this.getProject().getId()),
								this.getCurrentUser().getId(),
								getSearchDatePattern(),
								getSearchNumberPattern(),
								getSearchImportPattern()));
			}

			//}
		}
		else
		{
			this.setList(new ArrayList<GiuridicalEngages>());
		}

	}
    
	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(),
					ExportPlaces.GiuridicalEngages);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportGiuridicalEngages") + ".xls", is,
					data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        this.getSession().put("giuridicalEngage", null);
        this.goTo(PagesTypes.GIURIDICALENGAGEEDIT);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        try
        {
            BeansFactory.GiuridicalEngages().Delete(this.getEntityDeleteId());
            this.Page_Load();
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
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        this.getSession().put("giuridicalEngage", this.getEntityEditId());
        goTo(PagesTypes.GIURIDICALENGAGEEDIT);
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
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }

	public Date getSearchDatePattern()
	{
		return (Date)this.getViewState().get("searchDatePattern");
	}

	public void setSearchDatePattern(Date searchDatePattern)
	{
		this.getViewState().put("searchDatePattern", searchDatePattern);
		
		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	public String getSearchNumberPattern()
	{
		return (String)this.getViewState().get("searchNumberPattern");
	}

	public void setSearchNumberPattern(String searchNumberPattern)
	{
		this.getViewState().put("searchNumberPattern", searchNumberPattern);
		
		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	public String getSearchImportPattern()
	{
		return (String)this.getViewState().get("searchImportPattern");
		
		
	}

	public void setSearchImportPattern(String searchImportPattern)
	{
		this.getViewState().put("searchImportPattern", searchImportPattern);
		
		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	public HtmlDataScroller getScroller()
	{
		return scroller;
	}

	public void setScroller(HtmlDataScroller scroller)
	{
		this.scroller = scroller;
	}
	
	
	
}
