package com.infostroy.paamns.web.beans.acl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.pdf.PotentialProjectPdfExport;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpirationDate;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjectsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.web.beans.EntityListBean;

public class PotentialProjectsListBean extends EntityListBean<PotentialProjects>
{

    private List<PotentialProjects> potentialProjectsList;
    
    private String                  editPage;
    
    private Long                    selectedEntityId;
    
    private Boolean                 dateOver;
    
    private HtmlDataScroller        scroller;
	   
	@SuppressWarnings("static-access")
	@Override
	public void Page_Load()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException 
	{
		this.setEditPage(
				((HttpServletRequest) FacesContext.getCurrentInstance()
						.getExternalContext().getRequest()).getRequestURI()
								.replace(PagesTypes.POTENTIALPROJECTSLIST.getPagesContext(), PagesTypes.POTENTIALPROJECTEDIT.getPagesContext()));
		boolean isCFP = false;
		for(UserRoles role : getCurrentUser().getRoles())
		{
			if(role.getRole().getCode().equals(UserRoleTypes.CFP.name()))
			{
				isCFP = true;
				break;
			}
		}
		if(isCFP)
		{
			setPotentialProjectsList(BeansFactory.PotentialProjects().LoadByUserAndFilters(getCurrentUser().getId(), getSearchProjectAcronym(), getSearchProjectTitle(), getSearchLeadNameOrganization(), getSearchProgramPriority()));
		}
		else
		{
			setPotentialProjectsList(BeansFactory.PotentialProjects().LoadByFilters(getSearchProjectAcronym(), getSearchProjectTitle(), getSearchLeadNameOrganization(), getSearchProgramPriority()));
		}
		setDateOver(false);
		ExpirationDate expirationDate = BeansFactory.ExpirationDate().getExpirationDate();
		if(expirationDate != null)
		{
			if(expirationDate.getExpirationDate().before(new Date()))
			{
				setDateOver(true);
			}
		}
	}

	@Override
	public void addEntity() 
	{	
	}

	@Override
	public void editEntity() 
	{	
	}

	@SuppressWarnings("static-access")
	@Override
	public void deleteEntity() 
	{
		try 
		{
			ExpirationDate expirationDate = BeansFactory.ExpirationDate().getExpirationDate();
			if(expirationDate != null)
			{
				if(expirationDate.getExpirationDate().before(new Date()))
				{
					this.Page_Load();
					return;
				}
			}
		} 
		catch (Exception e) 
		{
			log.error(e);
		} 
		try 
		{
			List<Long> docs = new ArrayList<Long>();
			List<PotentialProjectsToDocuments> listPD = BeansFactory.PotentialProjectsToDocuments()
					.getByPotentialProject(this.getEntityDeleteId());
			for (PotentialProjectsToDocuments pd : listPD) 
			{
				if (pd.getDocument() != null) 
				{
					docs.add(pd.getDocument().getId());
				}
				BeansFactory.PotentialProjectsToDocuments().Delete(pd);
			}
			BeansFactory.PotentialProjects().Delete(this.getEntityDeleteId());
			for (Long id : docs) 
			{
				BeansFactory.Documents().Delete(id);
			}
			this.Page_Load();
		} 
		catch (HibernateException e) 
		{
			log.error(e);
		} 
		catch (PersistenceBeanException e) 
		{
			log.error(e);
		}
	}
	
	public void exportToPdf() 
	{
		PotentialProjectPdfExport pdf = new PotentialProjectPdfExport();
		byte[] data = pdf.createDocument(getSelectedEntityId());
		InputStream is = new ByteArrayInputStream(data);
		FileHelper.sendFile(Utils.getString("exportPotentialProject")
				+ ".pdf", is, data.length);
	}

	public void exportToExcel()
    {
        try
        {
            XlsExport exporter = new XlsExport();
            byte[] data = exporter.createXls(this.getPotentialProjectsList(),
                    ExportPlaces.PotentialProjects);
            InputStream is = new ByteArrayInputStream(data);
            FileHelper.sendFile(Utils.getString("exportPotentialProject") + ".xls", is,
                    data.length);
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }
	
	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException 
	{
		this.setEditPage(PagesTypes.POTENTIALPROJECTEDIT.getPagesContext());	
	}
	
	public List<PotentialProjects> getPotentialProjectsList()
	{
		return potentialProjectsList;
	}

	public void setPotentialProjectsList(List<PotentialProjects> potentialProjectsList)
	{
		this.potentialProjectsList = potentialProjectsList;
	}
	
	public String getEditPage()
	{
		return editPage;
	}

	public void setEditPage(String editPage)
	{
		this.editPage = editPage;
	}

	public Long getSelectedEntityId() 
	{
		return selectedEntityId;
	}

	public void setSelectedEntityId(Long selectedEntityId) 
	{
		this.selectedEntityId = selectedEntityId;
	}

	public Boolean getDateOver() 
	{
		return dateOver;
	}

	public void setDateOver(Boolean dateOver) 
	{
		this.dateOver = dateOver;
	}
    
    public HtmlDataScroller getScroller()
    {
        return scroller;
    }

    public void setScroller(HtmlDataScroller scroller)
    {
        this.scroller = scroller;
    }

    public String getSearchProjectAcronym()
    {
        if (this.getSession().get("searchProjectAcronym") != null)
        {
            return String.valueOf(this.getSession().get(
                    "searchProjectAcronym"));
        }
        else
        {
            return null;
        }
    }

    public void setSearchProjectAcronym(String searchProjectAcronym)
    {
        this.getSession().put("searchProjectAcronym", searchProjectAcronym);

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
    
    public String getSearchProjectTitle()
    {
        if (this.getSession().get("searchProjectTitle") != null)
        {
            return String.valueOf(this.getSession().get(
                    "searchProjectTitle"));
        }
        else
        {
            return null;
        }
    }
    
    public void setSearchProjectTitle(String searchProjectTitle)
    {
        this.getSession().put("searchProjectTitle", searchProjectTitle);
        
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
    
    public String getSearchLeadNameOrganization()
    {
        if (this.getSession().get("searchLeadNameOrganization") != null)
        {
            return String.valueOf(this.getSession().get(
                    "searchLeadNameOrganization"));
        }
        else
        {
            return null;
        }
    }
    
    public void setSearchLeadNameOrganization(String searchLeadNameOrganization)
    {
        this.getSession().put("searchLeadNameOrganization", searchLeadNameOrganization);
        
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
    
    public String getSearchProgramPriority()
    {
        if (this.getSession().get("searchProgramPriority") != null)
        {
            return String.valueOf(this.getSession().get(
                    "searchProgramPriority"));
        }
        else
        {
            return null;
        }
    }
    
    public void setSearchProgramPriority(String searchProgramPriority)
    {
        this.getSession().put("searchProgramPriority", searchProgramPriority);
        
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

}
