/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * @author Vito Rifino - Ingloba360 srl, 2017.
 * 
 */
public class BudgetViewBean extends
		EntityProjectEditBean<BudgetInputSourceDivided>
{

	/**
	 * Stores list value of entity.
	 */
	private List<BudgetInputSourceDivided>	list;

	private BudgetInputSourceDivided		oldBudget	= null;

	private boolean							isEdit;

	private boolean							isNew		= false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		list = BudgetInputSourceDividedBean.GetByProject(this.getProjectId());
		if (list.size() > 0)
		{
			try
			{
				if (this.getViewState().get("oldBudget") == null)
				{
					BudgetInputSourceDivided budget = list.get(0);
					BudgetInputSourceDivided copy;
					copy = (BudgetInputSourceDivided) budget.clone();
					this.getViewState().put("oldBudget", copy);
				}

				this.setEntity((BudgetInputSourceDivided) this.getViewState()
						.get("oldBudget"));
			}
			catch (CloneNotSupportedException e)
			{
				log.error(e);
			}
		}
		else
		{
			list.add(new BudgetInputSourceDivided());
			this.setEntity(new BudgetInputSourceDivided());
			this.setIsNew(true);
		}
	}

	public void budgetSave()
	{
		if (getSaveFlag() == 0)
		{
			try
			{
				if (!this.BeforeSave())
				{
					return;
				}
			}
			catch (Exception e)
			{
				log.error(e);
				return;
			}
			Transaction tr = null;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();
				setSaveFlag(1);
				saveNewBudgetInputSourceDivider();
			}
			catch (Exception e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				e.printStackTrace();
				log.error(e);
			}
			finally
			{
				if (tr != null && !tr.wasRolledBack() && tr.isActive())
				{
					tr.commit();
				}
			}

			this.AfterSave();
			setSaveFlag(0);
		}
	}

	/**
	 * Sets list
	 * 
	 * @param list
	 *            the list to set
	 */
	public void setList(List<BudgetInputSourceDivided> list)
	{
		this.list = list;
	}

	/**
	 * Gets list
	 * 
	 * @return list the list
	 */
	public List<BudgetInputSourceDivided> getList()
	{
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack()
	{
		this.getViewState().put("oldBudget", null);
		try
		{
			Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
		}
		super.goTo(PagesTypes.BUDGETVIEW);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException,
			PersistenceBeanException, PersistenceBeanException
	{
		BudgetInputSourceDivided item = saveNewBudgetInputSourceDivider();
		oldBudget = (BudgetInputSourceDivided) this.getViewState().get(
				"oldBudget");
		if (!equals(item, oldBudget))
		{
			List<GeneralBudgets> listGB = BeansFactory.GeneralBudgets()
					.GetItemsForProject(Long.parseLong(this.getProjectId()));
			for (GeneralBudgets budget : listGB)
			{
				BeansFactory.GeneralBudgets().DeleteInTransaction(budget);
			}

			List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
					.GetAllByProject(String.valueOf(this.getProjectId()));
			for (PartnersBudgets budget : listPB)
			{
				BeansFactory.PartnersBudgets().DeleteInTransaction(budget);
			}
		}
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	private BudgetInputSourceDivided saveNewBudgetInputSourceDivider()
			throws PersistenceBeanException
	{
		BudgetInputSourceDivided item = new BudgetInputSourceDivided();
		if (list.size() > 0)
		{
			item = list.get(0);
		}

		item.setFesr(this.getEntity().getFesr());
		item.setCnPrivate(this.getEntity().getCnPrivate());
		item.setCnPublic(this.getEntity().getCnPublic());
		item.setCnPublicOther(this.getEntity().getCnPublicOther());
		item.setCnPrivateReal(this.getEntity().getCnPrivateReal());
		item.setNetRevenue(this.getEntity().getNetRevenue());
		item.setCnTotal(this.getEntity().getCnPrivate()
				+ this.getEntity().getCnPublic());
		item.setTotalBudget(NumberHelper.Round(item.getFesr()+item.getCnPublic()+item.getCnPrivateReal()+item.getCnPrivate()+item.getNetRevenue()));
		if (item.getTotalBudget() != 0)
		{
			item.setFesrPct((item.getFesr()/(item.getTotalBudget()-item.getCnPrivate()-item.getNetRevenue()))*100);
			item.setCnPct(item.getCnTotal() / item.getTotalBudget()* 100);
			item.setCnPrivatePct(item.getCnPrivate() / item.getTotalBudget()
					* 100);
			item.setCnPublicPct(item.getCnPublic() / (item.getCnPublic() + item.getFesr())
					* 100);
		}
		else
		{
			item.setFesrPct(item.getTotalBudget());
			item.setCnPct(item.getTotalBudget());
			item.setCnPrivatePct(item.getTotalBudget());
			item.setCnPublicPct(item.getTotalBudget());
		}
		item.setProject(this.getProject());
		BeansFactory.BudgetInputSourceDivided().SaveInTransaction(item);
		return item;
	}

	public boolean equals(BudgetInputSourceDivided first,
			BudgetInputSourceDivided second)
	{
		if (first != null && second != null)
		{
			if ((first.getTotalBudget() != null && second.getTotalBudget() != null)
					&& first.getTotalBudget().equals(second.getTotalBudget()))
			{
				if ((first.getCnPrivate() != null && second.getCnPrivate() != null)
						&& first.getCnPrivate().equals(second.getCnPrivate()))
				{
					if ((first.getCnPublic() != null && second.getCnPublic() != null)
							&& first.getCnPublic().equals(second.getCnPublic()))
					{
						if ((first.getCnTotal() != null && second.getCnTotal() != null)
								&& first.getCnTotal().equals(
										second.getCnTotal()))
						{
							if ((first.getFesr() != null && second.getFesr() != null)
									&& first.getFesr().equals(second.getFesr()))
							{
								return true;
							}

							else
								return false;
						}
						else
							return false;
					}
					else
						return false;
				}
				else
					return false;
			}
			else
				return false;
		}
		else
		{
			return false;
		}
	}

	public void editItem()
	{
		setIsEdit(true);
	}

	/**
	 * Sets isEdit
	 * 
	 * @param isEdit
	 *            the isEdit to set
	 */
	public void setIsEdit(boolean isEdit)
	{
		this.isEdit = isEdit;
		this.getViewState().put("budgetEdit", true);
	}

	/**
	 * Gets isEdit
	 * 
	 * @return isEdit the isEdit
	 */
	public boolean getIsEdit()
	{
		if (this.getViewState().get("budgetEdit") != null)
		{
			isEdit = true;
		}

		return isEdit;
	}

	public void setIsNew(boolean isNew)
	{
		this.isNew = isNew;
	}

	public boolean getIsNew()
	{
		return isNew;
	}

	public void PreSave()
	{
		if (list != null && list.size() > 0)
		{
			if (list.get(0).getTotalBudget() != null
					&& list.get(0).getTotalBudget() > 0)
			{
				isNew = false;
			}
			else
			{
				isNew = true;
			}
		}
		else
		{
			isNew = true;
		}
		if (isNew)
		{
			try
			{
				Transaction tr = PersistenceSessionManager.getBean()
						.getSession().beginTransaction();
				this.SaveEntity();
				tr.commit();
			}
			catch (Exception e)
			{
				log.error(e);
			}
			finally
			{
				this.GoBack();
			}
		}

	}

}
