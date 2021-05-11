/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2013.
 * 
 */
public class CustomPaginator implements Serializable
{
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID		= -303973407418864234L;

	private static final int	DEFAULT_ITEMS_PER_PAGE	= 50;

	private List<SelectItem>	itemsPerPageVar;

	private Integer				itemsPerPageSelected;

	private List<Integer>		pages;

	private Integer				pageCount;

	private Integer				selectedPage;

	/**
	 * 
	 */
	public CustomPaginator()
	{
		super();

		itemsPerPageVar = new ArrayList<SelectItem>();
		itemsPerPageVar.add(new SelectItem(20, "20"));
		itemsPerPageVar.add(new SelectItem(50, "50"));
		itemsPerPageVar.add(new SelectItem(100, "100"));
		itemsPerPageVar.add(new SelectItem(150, "150"));

		this.itemsPerPageSelected = DEFAULT_ITEMS_PER_PAGE;
	}

	/**
	 * Gets pages
	 * 
	 * @return pages the pages
	 */
	public List<Integer> getPages()
	{
		return pages;
	}

	/**
	 * Sets pages
	 * 
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(List<Integer> pages)
	{
		this.pages = pages;
	}

	/**
	 * Gets pageCount
	 * 
	 * @return pageCount the pageCount
	 */
	public Integer getPageCount()
	{
		return pageCount;
	}

	/**
	 * Sets pageCount
	 * 
	 * @param pageCount
	 *            the pageCount to set
	 */
	public void setPageCount(Integer pageCount)
	{
		this.pageCount = pageCount;
	}

	/**
	 * Gets selectedPage
	 * 
	 * @return selectedPage the selectedPage
	 */
	public Integer getSelectedPage()
	{
		if (selectedPage == null)
		{
			return 1;
		}
		return selectedPage;
	}

	/**
	 * Sets selectedPage
	 * 
	 * @param selectedPage
	 *            the selectedPage to set
	 */
	public void setSelectedPage(Integer selectedPage)
	{
		this.selectedPage = selectedPage;
	}

	public List<Integer> getPagesToShow()
	{
		List<Integer> pagesToShow = new ArrayList<Integer>();

		if (this.getPages() != null)
		{
			if (this.getSelectedPage() < 5)
			{
				for (int i = 0; i < Math.min(10, getPages().size()); ++i)
				{
					pagesToShow.add(getPages().get(i));
				}
			}
			else if ((this.getSelectedPage() + 5) >= getPages().size())
			{
				int countNotEnough = (this.getSelectedPage() + 5)
						- getPages().size();

				for (int i = (this.getSelectedPage() - (5 + countNotEnough)); i < (this
						.getSelectedPage() - 1); ++i)
				{
					if (i >= 0)
					{
						pagesToShow.add(getPages().get(i));
					}
				}
				for (int i = (this.getSelectedPage() - 1); i < Math.min(
						(this.getSelectedPage() + (5 - countNotEnough)),
						getPages().size()); ++i)
				{
					pagesToShow.add(getPages().get(i));
				}
			}
			else
			{

				for (int i = (this.getSelectedPage() - 5); i < (this
						.getSelectedPage() - 1); ++i)
				{
					pagesToShow.add(getPages().get(i));
				}
				for (int i = (this.getSelectedPage() - 1); i < Math.min(
						(this.getSelectedPage() + 5), getPages().size()); ++i)
				{
					pagesToShow.add(getPages().get(i));
				}
			}
		}

		return pagesToShow;
	}

	public Integer getLastPageIndex()
	{
		if (this.getPages() != null && !this.getPages().isEmpty())
		{
			return this.getPages().size();
		}
		return 1;
	}

	public Integer getPrevPageIndex()
	{
		if (!this.getSelectedPage().equals(1))
		{
			return (this.getSelectedPage() - 1);
		}
		return 1;
	}

	public Integer getNextPageIndex()
	{
		if (this.getPages() == null || this.getPages().isEmpty())
		{
			return 1;
		}
		else if (!this.getSelectedPage().equals(this.getPages().size()))
		{
			return (this.getSelectedPage() + 1);
		}
		return this.getPages().size();

	}

	/**
	 * Gets itemsPerPageVar
	 * 
	 * @return itemsPerPageVar the itemsPerPageVar
	 */
	public List<SelectItem> getItemsPerPageVar()
	{
		return itemsPerPageVar;
	}

	/**
	 * Sets itemsPerPageVar
	 * 
	 * @param itemsPerPageVar
	 *            the itemsPerPageVar to set
	 */
	public void setItemsPerPageVar(List<SelectItem> itemsPerPageVar)
	{
		this.itemsPerPageVar = itemsPerPageVar;
	}

	/**
	 * Gets itemsPerPageSelected
	 * 
	 * @return itemsPerPageSelected the itemsPerPageSelected
	 */
	public Integer getItemsPerPageSelected()
	{
		return itemsPerPageSelected;
	}

	/**
	 * Sets itemsPerPageSelected
	 * 
	 * @param itemsPerPageSelected
	 *            the itemsPerPageSelected to set
	 */
	public void setItemsPerPageSelected(Integer itemsPerPageSelected)
	{
		if (!itemsPerPageSelected.equals(this.itemsPerPageSelected))
		{
			this.selectedPage = 1;
		}
		this.itemsPerPageSelected = itemsPerPageSelected;
	}

}
