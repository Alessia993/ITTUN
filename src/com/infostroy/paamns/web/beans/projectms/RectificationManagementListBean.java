/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.LogActionTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.RectificationStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.logging.InfoLog;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequestItem;
import com.infostroy.paamns.persistence.beans.entities.domain.Recoveries;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.UserActivityLog;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsWrapper;

/**
 * 
 * @author Dmitry Pogorelov InfoStroy Co., 2011.
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2012.
 * 
 */
public class RectificationManagementListBean extends
		EntityListBean<CostDefinitions>
{
	private class RectificationComparator implements
			Comparator<CostDefinitions>
	{
		@Override
		public int compare(CostDefinitions o1, CostDefinitions o2)
		{
			return (o1.getRectificationDate().getTime() > o2
					.getRectificationDate().getTime() ? -1 : (o1 == o2 ? 0 : 1));
		}
	}

	private Date	rectificationDate;

	private Double	rectificationAmount;

	private Boolean	canSelectAll;

	private Boolean	canSelectAllResend;

	private String	actionMotivation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		List<CostDefinitions> list = null;

		if (this.getRectificationDate() == null)
		{
			this.setRectificationDate(new Date());
		}

		if (this.getListCostDefinitionsWeb() == null || getNeedReload())
		{
			setNeedReload(false);
			list = BeansFactory.CostDefinitions().GetRectified(
					this.getSessionBean().getProjectId(),
					this.getSessionBean().isSTC(),
					this.getSessionBean().isACUM(),
					this.getSessionBean().isAGU());

			RectificationComparator comparator = new RectificationComparator();
			Collections.sort(list, comparator);

			this.setListCostDefinitionsWeb(list);

			this.setList(list);
		}
		else
		{
			list = this.getListCostDefinitionsWeb();
			this.setList(list);
		}

		Map<Long, CostDefinitionsWrapper> map = new HashMap<Long, CostDefinitionsWrapper>();
		for (CostDefinitions item : this.getList())
		{
			map.put(item.getId(), new CostDefinitionsWrapper(item));
			if (item.getRectificationConfirmed())
			{
				List<Recoveries> listRec = BeansFactory.Recoveries()
						.loadByCostDefinition(item.getId());
				if (listRec != null)
				{
					Double sum = 0d;
					for (Recoveries rec : listRec)
					{
						sum += rec.getAmountRecovered();
					}

					item.setRecoveryAmount(sum);
					item.setStillBeRecovered(item.getRectificationAmount()
							- sum);
				}
			}
		}
		if (!this.isPostback())
		{
			this.getViewState().put("oldValues", map);
		}
		checkCanSelect();
	}

	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(),
					ExportPlaces.RectificationManagement);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(
					Utils.getString("exportRectificationManagement") + ".xls",
					is, data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public String updateSelectedBySuperAdmin() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		if (this.getActionMotivation() == null
				|| this.getActionMotivation().isEmpty())
		{
			return null;
		}
		this.setErrorAmount(false);

		CostDefinitions cd = null;
		for (CostDefinitions cf : getList())
		{
			if (cf.getId().equals(getSelectedSave()))
			{
				cd = cf;
				break;
			}
		}

		if (cd.getRectifiedBySTC())
		{
			if (cd.getRectificationAmount() > cd.getCilCheck()
					|| (cd.getRectificationRefused()
							&& cd.getRectificationAmount() != null && cd
							.getRectificationAmount() > cd.getCilCheck()))
			{
				this.setErrorAmount(true);
				return null;
			}
		}
		else if (cd.getRectifiedByAGU())
		{
			if (cd.getRectificationAmount() > cd.getStcCertification()
					|| (cd.getRectificationRefused()
							&& cd.getRectificationAmount() != null && cd
							.getRectificationAmount() > cd
							.getStcCertification()))
			{
				this.setErrorAmount(true);
				return null;
			}
		}
		else if (cd.getRectifiedByACU())
		{
			if (cd.getRectificationAmount() > cd.getAguCertification())
			{
				this.setErrorAmount(true);
				return null;
			}
		}

		try
		{
			BeansFactory.CostDefinitions().Save(cd);
			if (getSessionBean().isSuperAdmin())
			{
				@SuppressWarnings("unchecked")
				Map<Long, CostDefinitionsWrapper> oldValues = (Map<Long, CostDefinitionsWrapper>) this
						.getViewState().get("oldValues");
				CostDefinitionsWrapper oldItem = oldValues.get(cd.getId());
				SuperAdminChange changes = new SuperAdminChange(
						ChangeType.VALUE_CHANGE, this.getCurrentUser(),
						this.getActionMotivation(), this.getSessionBean()
								.getProject());
				StringBuilder changeLogMessages = new StringBuilder();
				if (cd.getRectificationAmount() != null
						&& !cd.getRectificationAmount().equals(
								oldItem.getRectificationAmount())
						|| oldItem.getRectificationAmount() != null
						&& !oldItem.getRectificationAmount().equals(
								cd.getRectificationAmount()))
				{
					changeLogMessages
							.append(Utils
									.getString("superAdminChangeLogMessage")
									.replace(
											"%1",
											Utils.getString("rectificationManagementListAmountRectified"))
									.replace(
											"%2",
											oldItem.getRectificationAmount()
													.toString())
									.replace(
											"%3",
											cd.getRectificationAmount()
													.toString())).append(
									"<br/>");
					oldItem.setRectificationAmount(cd.getRectificationAmount());
				}
				if (changeLogMessages.length() > 0)
				{
					changeLogMessages
							.insert(0,
									Utils.getString(
											"superAdminChangeLogMessageHeader")
											.replace(
													"%1",
													Utils.getString("superAdminChangesCostDefinition"))
											.replace(
													"%2",
													cd.getProgressiveId()
															.toString())
											.replace(
													"%3",
													Utils.getString("rectificationManagementListName"))
											+ "<br/>");

					changes.setChanges(changeLogMessages.toString());
					BeansFactory.SuperAdminChanges().Save(changes);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}

		this.setNeedReload(true);
		this.setSelectAll(false);
		this.setSelectAllRefused(false);
		clearMotivation(null);
		this.Page_Load();
		return null;
	}

	public void doSelectAll() throws PersistenceBeanException
	{
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAll())
		{
			for (CostDefinitions cd : this.getList())
			{
				if (cd.getCanEditFlow())
				{
					cd.setSelected(true);
					listIndices.add(cd.getId());
				}
			}
			this.setSelectNormal(true);
			this.setSelectRefused(false);
		}
		else
		{
			this.setSelectNormal(false);
			for (CostDefinitions cd : this.getList())
			{
				cd.setSelected(false);
			}
		}

		this.getViewState().put("rectManListCD", listIndices);
		this.Page_Load();
	}

	public void doSelectAllRefused() throws PersistenceBeanException
	{
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAllRefused())
		{
			for (CostDefinitions cd : this.getList())
			{
				if (cd.getCanResend() != null && cd.getCanResend())
				{
					cd.setSelectedResend(true);
					listIndices.add(cd.getId());
				}
			}
			this.setSelectNormal(false);
			this.setSelectRefused(true);
		}
		else
		{
			this.setSelectRefused(false);
			for (CostDefinitions cd : this.getList())
			{
				cd.setSelectedResend(false);
			}
		}

		this.getViewState().put("rectManListCDRefused", listIndices);
		this.Page_Load();
	}

	public void checkCanSelect()
	{
		this.setCanSelectAll(false);
		this.setCanSelectAllResend(false);
		for (CostDefinitions item : this.getList())
		{
			if (this.getSessionBean().isSTC()
					&& item.getRectifiedBySTC()
					&& item.getRectificationState().equals(
							RectificationStateTypes.New.getState()))
			{
				item.setCanEditFlow(true);
				item.setCanResend(false);
				this.setCanSelectAll(true);
			}
			else if (this.getSessionBean().isAGU()
					&& item.getRectifiedByAGU() != null
					&& item.getRectifiedByAGU()
					&& item.getRectificationState().equals(
							RectificationStateTypes.New.getState()))
			{
				item.setCanEditFlow(true);
				item.setCanResend(false);
				this.setCanSelectAll(true);
			}
			else if (this.getSessionBean().isACUM()
					&& item.getRectifiedByACU() != null
					&& item.getRectifiedByACU()
					&& item.getRectificationState().equals(
							RectificationStateTypes.New.getState()))
			{
				item.setCanEditFlow(true);
				item.setCanResend(false);
				this.setCanSelectAll(true);
			}
			else
			{
				item.setCanEditFlow(false);
			}

			if (this.getSessionBean().isAGU())
			{
				if (item.getRectifiedBySTC()
						&& item.getRectificationState().equals(
								RectificationStateTypes.SentToAgu.getState()))
				{
					item.setCanEditFlow(true);
					this.setCanSelectAll(true);
				}
			}
			else if (this.getSessionBean().isACUM())
			{
				if ((item.getRectifiedBySTC() || item.getRectifiedByAGU())
						&& item.getRectificationState().equals(
								RectificationStateTypes.SentToAcu.getState()))
				{
					item.setCanEditFlow(true);
					this.setCanSelectAll(true);
				}
			}

			if (this.getSessionBean().isSTC()
					&& item.getRectifiedBySTC()
					&& (item.getRectificationState().equals(
							RectificationStateTypes.RefusedByAgu.getState()) || item
							.getRectificationState().equals(
									RectificationStateTypes.RefusedByAcu
											.getState())))
			{
				this.setCanSelectAllResend(true);
				item.setCanEditFlow(false);
				item.setCanResend(true);
			}
			else if (this.getSessionBean().isAGU()
					&& item.getRectifiedByAGU()
					&& (item.getRectificationState().equals(
							RectificationStateTypes.RefusedByAgu.getState()) || item
							.getRectificationState().equals(
									RectificationStateTypes.RefusedByAcu
											.getState())))
			{
				this.setCanSelectAllResend(true);
				item.setCanEditFlow(false);
				item.setCanResend(true);
			}
			else if (this.getSessionBean().isACUM()
					&& item.getRectifiedByACU()
					&& item.getRectificationState().equals(
							RectificationStateTypes.RefusedByAcu.getState()))
			{
				this.setCanSelectAllResend(true);
				item.setCanEditFlow(false);
				item.setCanResend(true);
			}
			else
			{
				item.setCanResend(false);
			}
		}
	}

	public void doSelectItem() throws PersistenceBeanException
	{
		this.setSelectRefused(false);
		this.setSelectNormal(true);
		this.setSelectAll(false);
		List<Long> listIndices = new ArrayList<Long>();
		int counter = 0;
		for (CostDefinitions cd : this.getList())
		{
			if (cd.getSelected())
			{
				listIndices.add(cd.getId());
				++counter;
			}
		}

		if (counter == 0)
		{
			this.setSelectNormal(false);
		}

		this.getViewState().put("rectManListCD", listIndices);
		this.setListCostDefinitionsWeb(this.getList());
		this.Page_Load();
	}

	public void doSelectItemRefused() throws PersistenceBeanException
	{
		this.setSelectRefused(true);
		this.setSelectNormal(false);
		this.setSelectAllRefused(false);
		int counter = 0;
		List<Long> listIndices = new ArrayList<Long>();
		for (CostDefinitions cd : this.getList())
		{
			if (cd.isSelectedResend())
			{
				listIndices.add(cd.getId());
				++counter;
			}
		}
		if (counter == 0)
		{
			this.setSelectRefused(false);
		}

		this.getViewState().put("rectManListCDRefused", listIndices);
		this.setListCostDefinitionsWeb(this.getList());
		this.Page_Load();
	}

	public void setSelectAll(boolean selectAll)
	{
		this.getViewState().put("suspManEditSelectAll", selectAll);
	}

	public boolean isSelectAll()
	{
		return Boolean.valueOf(String.valueOf(this.getViewState().get(
				"suspManEditSelectAll")));
	}

	public void setSelectAllRefused(boolean selectAll)
	{
		this.getViewState().put("suspManEditSelectAllRefused", selectAll);
	}

	public boolean isSelectAllRefused()
	{
		return Boolean.valueOf(String.valueOf(this.getViewState().get(
				"suspManEditSelectAllRefused")));
	}

	public Boolean getSelectNormal()
	{
		if (this.getViewState().containsKey("selectNormal"))
		{
			return (Boolean) this.getViewState().get("selectNormal");
		}
		return false;
	}

	public void setSelectNormal(Boolean selectNormal)
	{
		this.getViewState().put("selectNormal", selectNormal);
	}

	public Boolean getSelectRefused()
	{
		if (this.getViewState().containsKey("selectRefused"))
		{
			return (Boolean) this.getViewState().get("selectRefused");
		}
		return false;
	}

	public void setSelectRefused(Boolean selectRefused)
	{
		this.getViewState().put("selectRefused", selectRefused);
	}

	private void saveActionToLog(String actionName)
	{
		try
		{
			String logFormat = "%s %s %s %s %s %s %s %s";
			String ip = ((HttpServletRequest) FacesContext.getCurrentInstance()
					.getExternalContext().getRequest()).getRemoteAddr();
			String mes = String.format(logFormat, this.getSessionBean()
					.getCurrentUser().getName(), this.getSessionBean()
					.getCurrentUser().getSurname(), this.getSessionBean()
					.getCurrentUser().getRolesNames(), ip,
					" Eseguita l�azione di ", actionName, " in ", new Date());
			InfoLog.getInstance().addMessage(mes);
		}
		catch (Exception e)
		{

		}
	}

	private void saveActionToDB(LogActionTypes type)
	{
		try
		{
			UserActivityLog userAction = BeansFactory.UserActivityLog()
					.getByUserAndAction(
							getSessionBean().getCurrentUser().getId(),
							type.getId());
			if (userAction == null)
			{
				userAction = new UserActivityLog();
			}
			userAction.setUser(BeansFactory.Users().Load(
					getSessionBean().getCurrentUser().getId()));
			userAction.setActionText(type.getValue());
			userAction.setActionCode(type.getId());
			userAction.setLastPerfomedDate(new Date());
			BeansFactory.UserActivityLog().SaveInTransaction(userAction);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void annul() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setErrorAmount(false);

		List<CostDefinitions> list = this.getListCostDefinitionsWeb();

		for (CostDefinitions cd : list)
		{
			if (cd.isSelectedResend())
			{
				cd.setRectificationAmount(null);
				cd.setRectificationDate(null);
				cd.setRectificationReason(null);
				cd.setRectificationState(null);
				cd.setRectifiedBySTC(false);
				cd.setRectifiedByAGU(false);
				cd.setRectifiedByACU(false);

				try
				{
					BeansFactory.CostDefinitions().Save(cd);
					this.setList(list);
					this.setListCostDefinitionsWeb(list);
				}
				catch (HibernateException e)
				{
					log.error(e);
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				saveActionToLog(LogActionTypes.AnnulRectificationFlow
						.getValue());
				saveActionToDB(LogActionTypes.AnnulRectificationFlow);
				cd.setSelectedResend(false);
			}

		}
		this.setNeedReload(true);
		Page_Load();
	}

	public void refuse() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setErrorAmount(false);
		org.hibernate.Transaction trans = null;
		trans = PersistenceSessionManager.getBean().getSession()
				.beginTransaction();
		setErrorRefuse(false);
		List<CostDefinitions> list = this.getListCostDefinitionsWeb();
		for (CostDefinitions cd : list)
		{
			if (cd.getSelected())
			{
				if (this.getSessionBean().isAGU())
				{
					if (cd.getRectifiedByAGU())
					{
						setErrorRefuse(true);
						break;
					}
					cd.setRectificationState(RectificationStateTypes.RefusedByAgu
							.getState());
				}
				else if (this.getSessionBean().isACUM())
				{
					cd.setRectificationState(RectificationStateTypes.RefusedByAcu
							.getState());
				}

				try
				{
					BeansFactory.CostDefinitions().SaveInTransaction(cd);
					this.setList(list);
					this.setListCostDefinitionsWeb(list);
				}
				catch (HibernateException e)
				{
					log.error(e);
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}
				saveActionToLog(LogActionTypes.RefuseRectificationFlow
						.getValue());
				saveActionToDB(LogActionTypes.RefuseRectificationFlow);
				cd.setSelected(false);
			}
		}
		if (getErrorRefuse())
		{
			trans.rollback();
		}
		else
		{
			trans.commit();
		}
		this.setSelectAll(false);
		this.setSelectNormal(false);
		this.setNeedReload(true);
		this.Page_Load();
	}

	public void send() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setErrorAmount(false);

		List<CostDefinitions> list = this.getListCostDefinitionsWeb();

		for (CostDefinitions cd : list)
		{
			if (cd.getSelected() || cd.isSelectedResend())
			{

				if (this.getSessionBean().isSTC())
				{
					if (cd.getRectificationAmount() > cd.getCilCheck()
							|| (cd.getRectificationRefused()
									&& cd.getRectificationAmount() != null && cd
									.getRectificationAmount() > cd
									.getCilCheck()))
					{
						this.setErrorAmount(true);
						return;
					}
					if (cd.getRectificationRefused())
					{
						cd.setRectificationDate(this.rectificationDate);

					}
					cd.setRectificationState(RectificationStateTypes.SentToAgu
							.getState());
				}
				else if (this.getSessionBean().isAGU())
				{
					if (cd.getRectificationAmount() > cd.getStcCertification()
							|| (cd.getRectificationRefused()
									&& cd.getRectificationAmount() != null && cd
									.getRectificationAmount() > cd
									.getStcCertification()))
					{
						this.setErrorAmount(true);
						return;
					}
					if (cd.getRectificationRefused())
					{
						cd.setRectificationDate(this.rectificationDate);
					}
					cd.setRectificationState(RectificationStateTypes.SentToAcu
							.getState());
					if (cd.getRectifiedByAGU() == null
							|| !cd.getRectifiedByAGU())
					{

					}
				}
				else if (this.getSessionBean().isACUM())
				{
					if (cd.getRectificationAmount() > cd.getAguCertification())
					{
						this.setErrorAmount(true);
						return;
					}
					cd.setRectificationState(RectificationStateTypes.Confirmed
							.getState());
					cd.setRectificationDateAcuManager(this.rectificationDate);
					if (cd.getRectifiedByACU() == null
							|| !cd.getRectifiedByACU())
					{

					}

					createPaymentRequestItem(cd);
				}

				try
				{
					BeansFactory.CostDefinitions().Save(cd);
					saveActionToLog(LogActionTypes.SendRetificationFlow
							.getValue());
					saveActionToDB(LogActionTypes.SendRetificationFlow);
					this.setList(list);
					this.setListCostDefinitionsWeb(list);
				}
				catch (HibernateException e)
				{
					log.error(e);
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}
				cd.setSelected(false);
				cd.setSelectedResend(false);
			}

		}
		this.setNeedReload(true);
		this.setSelectAll(false);
		this.setSelectAllRefused(false);
		this.Page_Load();
	}

	private void createPaymentRequestItem(CostDefinitions cd)
			throws PersistenceBeanException
	{
		// TODO CR 0027730
		PaymentRequestItem paymentRequestItem = BeansFactory
				.PaymentRequestItem().getByDurCompilationId(
						cd.getDurCompilation().getId().longValue());

		if (paymentRequestItem == null)
		{
			PaymentRequest paymentRequest = BeansFactory.PaymentRequest()
					.getWithMaxNumber();

			PaymentRequestItem pri = new PaymentRequestItem();

			pri.setAnnuledAmount(0d);

			if (cd.getRectificationAmount() != null)
			{
				pri.setRectifiedAmount(cd.getRectificationAmount());
			}
			else
			{
				pri.setRectifiedAmount(0d);
			}

			pri.setTotalAmount(0d);

			pri.setDurCompilations(cd.getDurCompilation());
			pri.setDurNumber(cd.getDurCompilation().getDurNumberTransient());
			pri.setPaymentRequest(paymentRequest);

			BeansFactory.PaymentRequestItem().Save(pri);

			paymentRequest.setTotalAmount(paymentRequest.getTotalAmount()
					- cd.getRectificationAmount());

			BeansFactory.PaymentRequest().Save(paymentRequest);
		}
		else
		{
			paymentRequestItem.setRectifiedAmount(paymentRequestItem
					.getRectifiedAmount() + cd.getRectificationAmount());

			paymentRequestItem.setTotalAmount(paymentRequestItem
					.getTotalAmount() - cd.getRectificationAmount());

			BeansFactory.PaymentRequestItem().Save(paymentRequestItem);

			PaymentRequest paymentRequest = paymentRequestItem
					.getPaymentRequest();

			paymentRequest.setTotalAmount(paymentRequest.getTotalAmount()
					- cd.getRectificationAmount());

			BeansFactory.PaymentRequest().Save(paymentRequest);
		}
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		CostDefinitions cd = null;
		try
		{
			cd = BeansFactory.CostDefinitions().Load(this.getEntityEditId());
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		if (cd != null && !cd.getDeleted())
		{
			this.getSession().put("rectificationManagementEdit",
					this.getEntityEditId());
			this.getSession().put("rectificationManagementEditShow", false);
			this.getSession().put("rectificationManagementEditBack", true);
			this.goTo(PagesTypes.RECTIFICATIONMANAGEMENTEDIT);
		}
	}

	public void showItem()
	{
		CostDefinitions cd = null;
		try
		{
			cd = BeansFactory.CostDefinitions().Load(this.getEntityEditId());
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		if (cd != null && !cd.getDeleted())
		{
			this.getSession().put("rectificationManagementEdit",
					this.getEntityEditId());
			this.getSession().put("rectificationManagementEditShow", true);
			this.getSession().put("rectificationManagementEditBack", true);
			this.goTo(PagesTypes.RECTIFICATIONMANAGEMENTEDIT);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getListCostDefinitionsWeb()
	{
		if (this.getViewState().containsKey("listCostDefinitionsWeb"))
		{
			return (List<CostDefinitions>) getViewState().get(
					"listCostDefinitionsWeb");
		}
		return null;
	}

	public void setListCostDefinitionsWeb(
			List<CostDefinitions> listCostDefinitionsWeb)
	{
		this.getViewState().put("listCostDefinitionsWeb",
				listCostDefinitionsWeb);
	}

	public Boolean getNeedReload()
	{
		if (this.getViewState().containsKey("needReload"))
		{
			return (Boolean) this.getViewState().get("needReload");
		}
		return false;
	}

	public void setNeedReload(Boolean needReload)
	{
		this.getViewState().put("needReload", needReload);
	}

	public void setErrorAmount(Boolean errorAmount)
	{
		this.getViewState().put("errorAmount", errorAmount);
	}

	public Boolean getErrorAmount()
	{
		if (this.getViewState().containsKey("errorAmount"))
		{
			return (Boolean) this.getViewState().get("errorAmount");
		}
		return false;
	}

	public void setErrorRefuse(Boolean errorRefuse)
	{
		this.getViewState().put("errorRefuse", errorRefuse);
	}

	public Boolean getErrorRefuse()
	{
		if (this.getViewState().containsKey("errorRefuse"))
		{
			return (Boolean) this.getViewState().get("errorRefuse");
		}
		return false;
	}

	public Date getRectificationDate()
	{
		return rectificationDate;
	}

	public void setRectificationDate(Date rectificationDate)
	{
		this.rectificationDate = rectificationDate;
	}

	public Long getCdId()
	{
		return (Long) this.getViewState().get("cdId");
	}

	public void setCdId(Long cdId)
	{
		this.getViewState().put("cdId", cdId);
	}

	public Double getRectificationAmount()
	{
		return rectificationAmount;
	}

	public void setRectificationAmount(Double amountRectified)
	{
		this.rectificationAmount = amountRectified;
	}

	public Boolean getCanSelectAll()
	{
		return canSelectAll;
	}

	public void setCanSelectAll(Boolean canSelectAll)
	{
		this.canSelectAll = canSelectAll;
	}

	public Boolean getCanSelectAllResend()
	{
		return canSelectAllResend;
	}

	public void setCanSelectAllResend(Boolean canSelectAllResend)
	{
		this.canSelectAllResend = canSelectAllResend;
	}

	public Long getSelectedSave()
	{
		return (Long) this.getViewState().get("selectedSave");
	}

	public void setSelectedSave(Long selectedSave)
	{
		this.getViewState().put("selectedSave", selectedSave);
	}

	public String getActionMotivation()
	{
		return actionMotivation;
	}

	public void setActionMotivation(String actionMotivation)
	{
		this.actionMotivation = actionMotivation;
	}

	public void clearMotivation(ActionEvent event)
	{
		this.setActionMotivation("");
	}

}