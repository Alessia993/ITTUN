/**
 * 
 */
package com.infostroy.paamns.web.beans;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.persistence.PersistenceSessionManager;

/**
 *
 * @author Alexander Chelombitko
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public abstract class EntityListBean<T extends Object> extends PageBean
{
    private List<T>                 list;
    
    /**
    * Stores entityEditId value of entity.
    */
    private Long                    entityEditId;
    private Long                    entityEditId2;
    
    /**
     * Stores entityDeleteId value of entity.
     */
    private Long                    entityDeleteId;
    private Long                    entityDeleteId2;
    
    /**
     * Stores itemsPerPageList value of entity.
     */
    private static List<SelectItem> itemsPerPageList;
    
	private HtmlDataTable		dataTable;
    
	private HtmlDataScroller	scroller;
	
    /**
     * 
     */
    public EntityListBean()
    {
        try
        {
            PersistenceSessionManager.getBean().getSession().clear();
        }
        catch(PersistenceBeanException e1)
        {
            log.error(e1);
        }
        if (!this.isPostback())
        {
            try
            {
                this.checkPasswordExpiration();
                ActivityLog.getInstance().addActivity();
                this.PreLoad();
                this.getViewState().clear();
                this.Page_Load_Static();
            }
            catch(PersistenceBeanException e)
            {
                log.error("EntityListBean exception:",e);
            }
            catch(HibernateException e)
            {
                log.error("EntityListBean exception:",e);
            }
            
            this.InitPaging();
            entityDeleteId = null;
            entityDeleteId2 = null;
        }
        
        try
        {
            this.Page_Load();
            ReRenderScroll();
        }
        catch(NumberFormatException e)
        {
            log.error("EntityListBean exception:",e);
            addStackTrace(e);
        }
        catch(HibernateException e)
        {
            log.error("EntityListBean exception:",e);
            addStackTrace(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error("EntityListBean exception:",e);
            addStackTrace(e);
        }
        catch(NullPointerException e)
        {
            log.error("EntityListBean exception:",e);
            addStackTrace(e);
        }
    }
    
    public void addStackTrace(Throwable t)
    {
    	StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        log.error(sw.toString());
    }
    
    /**
     * @throws PersistenceBeanException 
     * 
     */
    protected void PreLoad() throws PersistenceBeanException
    {
        
    }
    
    /**
     * 
     */
    private void InitPaging()
    {
        this.setItemsPerPage("10");
        setItemsPerPageList(new ArrayList<SelectItem>());
        for (int i = 1; i < 6; i++)
        {
            getItemsPerPageList().add(
                    new SelectItem(String.valueOf(10 * i), String
                            .valueOf(10 * i)));
        }
    }
    
    /**
     * @return 
     * 
     */
    public void ReRenderScroll()
    {
		if (getList() != null)
		{
			if (getItemsPerPage() == null)
			{
				setItemsPerPage("10");
			}

			if (getList().size() > Integer.parseInt(this
					.getItemsPerPage()))
			{
				this.setShowScroll(true);
			}
			else
			{
				this.setShowScroll(false);
			}
		}
		else
		{
			this.setShowScroll(false);
		}
    }
    
    /**
     * Use this methos to fill fields.
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public abstract void Page_Load() throws NumberFormatException,
            HibernateException, PersistenceBeanException;
    
    /**
     * Use this methos to fill static lists. It will calls only once on page load event.
     */
    public abstract void Page_Load_Static() throws HibernateException,
            PersistenceBeanException;
    
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
    
    /**
     * Sets itemsPerPage
     * @param itemsPerPage the itemsPerPage to set
     */
    public void setItemsPerPage(String itemsPerPage)
    {
        this.getViewState().put("itemsPerPage", itemsPerPage);
        ReRenderScroll();
    }
    
    /**
     * Gets itemsPerPage
     * @return itemsPerPage the itemsPerPage
     */
    public String getItemsPerPage()
    {
        return (String) this.getViewState().get("itemsPerPage");
    }
    
    /**
     * Sets itemsPerPageList
     * @param itemsPerPageList the itemsPerPageList to set
     */
    public void setItemsPerPageList(List<SelectItem> itemsPerPageList)
    {
        EntityListBean.itemsPerPageList = itemsPerPageList;
        
    }
    
    /**
     * Gets itemsPerPageList
     * @return itemsPerPageList the itemsPerPageList
     */
    public List<SelectItem> getItemsPerPageList()
    {
        return itemsPerPageList;
    }
    
    /**
     * Sets entityDeleteId
     * @param entityDeleteId the entityDeleteId to set
     */
    public void setEntityDeleteId(Long entityDeleteId)
    {
        this.getViewState().put("entityDeleteId", entityDeleteId);
        this.entityDeleteId = entityDeleteId;
    }
    
    /**
     * Gets entityDeleteId
     * @return entityDeleteId the entityDeleteId
     */
    public Long getEntityDeleteId()
    {
        entityDeleteId = (Long) this.getViewState().get("entityDeleteId");
        return entityDeleteId;
    }
    
    /**
     * Sets entityDeleteId2
     * @param entityDeleteId2 the entityDeleteId2 to set
     */
    public void setEntityDeleteId2(Long entityDeleteId2)
    {
        this.getViewState().put("entityDeleteId2", entityDeleteId2);
        this.entityDeleteId2 = entityDeleteId2;
    }
    
    /**
     * Gets entityDeleteId2
     * @return entityDeleteId2 the entityDeleteId2
     */
    public Long getEntityDeleteId2()
    {
        entityDeleteId2 = (Long) this.getViewState().get("entityDeleteId2");
        return entityDeleteId2;
    }   
    
    /**
     * Sets entityEditId
     * @param entityEditId the entityEditId to set
     */
    public void setEntityEditId(Long entityEditId)
    {
        this.entityEditId = entityEditId;
    }
    
    /**
     * Gets entityEditId
     * @return entityEditId the entityEditId
     */
    public Long getEntityEditId()
    {
        return entityEditId;
    }
    
    /**
     * Sets entityEditId2
     * @param entityEditId2 the entityEditId2 to set
     */
    public void setEntityEditId2(Long entityEditId2)
    {
        this.entityEditId2 = entityEditId2;
    } 
    
    /**
     * Gets entityEditId2
     * @return entityEditId2 the entityEditId2
     */
    public Long getEntityEditId2()
    {
        return entityEditId2;
    } 
    
    /**
     * Sets list
     * @param list the list to set
     */
    public void setList(List<T> list)
    {
        this.list = list;
    }
    
    /**
     * Gets list
     * @return list the list
     */
    public List<T> getList()
    {
        return list;
    }
    
    /**
     * Sets showScroll
     * @param showScroll the showScroll to set
     */
    public void setShowScroll(Boolean showScroll)
    {
        this.getViewState().put("showScroll", showScroll);
    }
    
    /**
     * Gets showScroll
     * @return showScroll the showScroll
     */
    public Boolean getShowScroll()
    {
        return (this.getViewState().get("showScroll") == null ? false
                : (Boolean) this.getViewState().get("showScroll"));
    }

	/**
	 * Gets dataTable
	 * @return dataTable the dataTable
	 */
	public HtmlDataTable getDataTable()
	{
		return dataTable;
	}

	/**
	 * Sets dataTable
	 * @param dataTable the dataTable to set
	 */
	public void setDataTable(HtmlDataTable dataTable)
	{
		this.dataTable = dataTable;
	}

	public void resetDataScroller(ActionEvent e)
	{
		if (getDataTable() != null)
		{
			getDataTable().setFirst(0);
		}
	}

	
	public void changePagesCount()
	{
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}

		this.ReRenderScroll();

	}

	/**
	 * Gets scroller
	 * 
	 * @return scroller the scroller
	 */
	public HtmlDataScroller getScroller()
	{
		return scroller;
	}

	/**
	 * Sets scroller
	 * 
	 * @param scroller
	 *            the scroller to set
	 */
	public void setScroller(HtmlDataScroller scroller)
	{
		this.scroller = scroller;
	}
}
