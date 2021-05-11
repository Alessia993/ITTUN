/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.RectificationStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequest;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentRequestItem;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.DURCompilationWrapper;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class CostCertificationListBean extends
		EntityListBean<DURCompilationWrapper>
{
	private boolean				showCertPanel;

	private boolean				showPaymentPanel;
	private boolean				certificationDateInvalid;
	private boolean				paymentDataInvalid;

	private boolean				showSelectAll;

	private String				paymentNumber;

	private Date				paymentDate;

	private List<SelectItem>	certifiedItems		= new ArrayList<SelectItem>();

	private List<SelectItem>	withRequestItems	= new ArrayList<SelectItem>();

	private HtmlDataScroller	scroller;
	
	

	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		List<DurCompilations> listDurs = null;

		Integer paymReqNum = null;
		try
		{
			paymReqNum = Integer.valueOf(this.getPaymentRequestNumber());
		}
		catch (NumberFormatException e)
		{

		}
		
//		listDurs = BeansFactory.DurCompilations().LoadByState(DurStateTypes.Certifiable, true);
		listDurs = BeansFactory.DurCompilations()
				.LoadForCostCertificationByAsse(
						this.getStartDate() != null ? this.getStartDate()
								: null,
						this.getEndDate() != null ? this.getEndDate() : null,
						this.getAsse(), this.getCertified(),
						this.getWithRequest(), this.getSearchProtocolNumber(),
						this.getProjectCode(), paymReqNum,
						this.getStartPaymentRequest(),
						this.getEndPaymentRequest());
		
		this.setList(new ArrayList<DURCompilationWrapper>());

		List<Long> listIndices = new ArrayList<Long>();

		if (this.getViewState().get("costCertificationListCD") != null)
		{
			listIndices = (List<Long>) this.getViewState().get(
					"costCertificationListCD");
		}

		this.setShowCertPanel(false);
		this.setShowSelectAll(false);
		this.setShowPaymentPanel(false);

		boolean bFoundSelected = false;
		Long firstSelectedType = null;
		for (DurCompilations dc : listDurs)
		{
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
					dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(
					dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc,
					dc.getDurState(), di, ds);

			if (listIndices.contains(dcw.getId()))
			{
				bFoundSelected = true;
				firstSelectedType = dcw.getStateId();
				dcw.setSelected(true);
			}
			else
			{
				dcw.setSelected(false);
			}
			this.getList().add(dcw);
		}

		this.setShowSelectAll(bFoundSelected);
		this.setShowCertPanel(bFoundSelected
				&& firstSelectedType.equals(DurStateTypes.Certifiable
						.getValue()));
		this.setShowPaymentPanel(bFoundSelected
				&& firstSelectedType.equals(DurStateTypes.Certified.getValue()));
		this.RecheckSelectableProperty(!bFoundSelected, firstSelectedType);

		this.setPaymentDate(new Date());

		this.ReRenderScroll();
		this.FillCertifiedDropDown();
		this.FillWithRequestDropDown();
	}

	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(),
					ExportPlaces.CostCertification);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportCostCertification")
					+ ".xls", is, data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	private void RecheckSelectableProperty(boolean bResetToDefault, Long type)
	{
		for (DURCompilationWrapper dcw : getList())
		{
			if (!bResetToDefault)
			{
				if (dcw.getStateId().equals(type))
				{
					if (type.equals(DurStateTypes.Certified.getValue()))
					{
						dcw.setSelectable(dcw.getPaymentRequestDate() == null);
					}
					else
					{
						dcw.setSelectable(true);
					}
				}
				else
				{
					dcw.setSelectable(false);
				}
			}
			else
			{
				dcw.setSelectable(dcw.getStateId().equals(
						DurStateTypes.Certifiable.getValue())
						|| (dcw.getStateId().equals(
								DurStateTypes.Certified.getValue()) && dcw
								.getPaymentRequestDate() == null));
			}
		}
	}

	private void FillCertifiedDropDown()
	{
		this.setCertifiedItems(new ArrayList<SelectItem>());
		this.getCertifiedItems().add(
				new SelectItem(0, Utils.getString("Resources", "all", null)));
		this.getCertifiedItems().add(
				new SelectItem(1, Utils.getString("Resources", "yes", null)));
		this.getCertifiedItems().add(
				new SelectItem(2, Utils.getString("Resources", "no", null)));
	}

	private void FillWithRequestDropDown()
	{
		this.setWithRequestItems(new ArrayList<SelectItem>());
		this.getWithRequestItems().add(
				new SelectItem(0, Utils.getString("Resources", "all", null)));
		this.getWithRequestItems().add(
				new SelectItem(1, Utils.getString("Resources", "yes", null)));
		this.getWithRequestItems().add(
				new SelectItem(2, Utils.getString("Resources", "no", null)));
	}

	public void createPayment() throws HibernateException,
			PersistenceBeanException
	{
		boolean hasError = false;
		if (this.getPaymentNumber() == null
				|| this.getPaymentNumber().trim().isEmpty())
		{
			this.markInvalid(
					this.getComponentById("paymentRqst:paymentNumberTb"), null);
			this.setPaymentDataInvalid(true);
			hasError = true;
		}
		if (this.getPaymentDate() == null)
		{
			this.markInvalid(this.getComponentById("paymentRqst:paymentDate"),
					null);
			this.setPaymentDataInvalid(true);
			hasError = true;
		}
		if (hasError)
		{
			this.getContext().addMessage("",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));
			return;
		}
		else
		{
			this.setPaymentDataInvalid(false);
		}

		Transaction tr = PersistenceSessionManager.getBean().getSession()
				.beginTransaction();

		PaymentRequest paymentRequest = new PaymentRequest();

		Double totalAnnuled = 0d;
		Double totalRectified = 0d;
		Double totalAmount = 0d;

		for (DURCompilationWrapper dcw : this.getList())
		{
			if (dcw.isSelected())
			{
				DurCompilations dc = BeansFactory.DurCompilations().Load(
						dcw.getId());
				if (this.getPaymentNumber() != null
						&& !this.getPaymentNumber().isEmpty())
				{
					Integer value = Integer.valueOf(this.getPaymentNumber());
					dc.setNumberPaymentRequest(value);
				}

				if (this.getPaymentDate() != null)
				{
					dc.setDatePaymentRequest(this.getPaymentDate());
				}

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				double totalAmountForItem = 0d;
				double totalAnnuledForItem = 0d;
				double totalRectifiedForItem = 0d;

				PaymentRequestItem paymentRequestItem = new PaymentRequestItem();

				for (CostDefinitions cd : listCD)
				{
					if (cd.getAcuCertification() != null)
					{
						totalAmountForItem += cd.getAcuCertification();
					}

					if (cd.getRectificationAmount() != null
							&& cd.getRectificationState() != null
							&& cd.getRectificationState().equals(
									RectificationStateTypes.Confirmed
											.getState()))
					{
						cd.setRectificationState(RectificationStateTypes.Recovered
								.getState());
						totalRectifiedForItem += cd.getRectificationAmount();
					}

					BeansFactory.CostDefinitions().SaveInTransaction(cd);
				}

				paymentRequestItem.setTotalAmount(totalAmountForItem);
				paymentRequestItem.setDurNumber(dc.getDurNumberTransient());
				paymentRequestItem.setAnnuledAmount(totalAnnuledForItem);
				paymentRequestItem.setRectifiedAmount(totalRectifiedForItem);
				paymentRequestItem.setPaymentRequest(paymentRequest);
				paymentRequestItem.setDurCompilations(dc);

				totalAmount += totalAmountForItem;
				totalAnnuled += totalAnnuledForItem;
				totalRectified += totalRectifiedForItem;
				BeansFactory.PaymentRequestItem().SaveInTransaction(
						paymentRequestItem);
				BeansFactory.DurCompilations().SaveInTransaction(dc);
			}
			else if (dcw.getStateId()
					.equals(DurStateTypes.Certified.getValue()))
			{
				DurCompilations dc = BeansFactory.DurCompilations().Load(
						dcw.getId());

				double totalAnnuledForItem = 0d;
				double totalRectifiedForItem = 0d;
				double totalAmountForItem = 0d;
				boolean needAdd = false;

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				for (CostDefinitions cd : listCD)
				{

					if (cd.getRectificationState() != null
							&& cd.getRectificationState().equals(
									RectificationStateTypes.Confirmed
											.getState()))
					{
						cd.setRectificationState(RectificationStateTypes.Recovered
								.getState());
						totalRectifiedForItem += cd.getRectificationAmount();
						needAdd = true;
					}
					BeansFactory.CostDefinitions().SaveInTransaction(cd);
				}
				if (!needAdd)
				{
					continue;
				}

				PaymentRequestItem paymentRequestItem = new PaymentRequestItem();
				paymentRequestItem.setDurNumber(dc.getDurNumberTransient());
				paymentRequestItem.setTotalAmount(totalAmountForItem);
				paymentRequestItem.setRectifiedAmount(totalRectifiedForItem);
				paymentRequestItem.setAnnuledAmount(totalAnnuledForItem);
				paymentRequestItem.setPaymentRequest(paymentRequest);

				if (this.getPaymentNumber() != null
						&& !this.getPaymentNumber().isEmpty())
				{
					Integer value = Integer.valueOf(this.getPaymentNumber());
					dc.setNumberPaymentRequest(value);
				}

				if (this.getPaymentDate() != null)
				{
					dc.setDatePaymentRequest(this.getPaymentDate());
				}
				totalAmount += totalAmountForItem;
				totalAnnuled += totalAnnuledForItem;
				totalRectified += totalRectifiedForItem;

				BeansFactory.PaymentRequestItem().SaveInTransaction(
						paymentRequestItem);
				BeansFactory.DurCompilations().SaveInTransaction(dc);
			}
		}
		if (this.getPaymentNumber() != null
				&& !this.getPaymentNumber().isEmpty())
		{
			Integer value = Integer.valueOf(this.getPaymentNumber());
			paymentRequest.setNumber(value);
		}

		if (this.getPaymentDate() != null)
		{
			paymentRequest.setPaymentRequestDate(this.getPaymentDate());
		}

		totalAmount += totalAnnuled;
		totalAmount -= totalRectified;
		paymentRequest.setTotalAmount(totalAmount);

		BeansFactory.PaymentRequest().SaveInTransaction(paymentRequest);
		
		tr.commit();
		this.getViewState().put("costCertificationListCD", null);
		this.setPaymentNumber(null);
		this.Page_Load();
	}

	public void Page_Load_Static() throws PersistenceBeanException
	{
		if (!AccessGrantHelper.IsCostCertificationAvailable())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	public void doSearch() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.getViewState().put("costCertificationListCD", null);

		if (getScroller() != null && getScroller().isPaginator())
		{
			getScroller().getUIData().setFirst(0);
		}

		this.Page_Load();
	}

	public void clear() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setStartDate(null);
		this.setEndDate(null);
		this.setAsse(0);

		this.setCertified(0);
		this.setWithRequest(0);
		this.setSearchProtocolNumber(null);
		this.setProjectCode(null);
		this.setPaymentRequestNumber(null);
		this.setStartPaymentRequest(null);
		this.setEndPaymentRequest(null);

		this.Page_Load();
	}

	@Override
	public void addEntity()
	{
		// Not used
	}

	@Override
	public void deleteEntity()
	{
		// Not used
	}

	@Override
	public void editEntity()
	{
		// Not used
	}

//	public void showItem() throws PersistenceBeanException
//	{
//		this.getSession().put(
//				"costCertProjectId",
//				BeansFactory.CostDefinitions()
//						.GetByDurCompilation(this.getEntityEditId()).get(0)
//						.getProject().getId());
//
//		this.getSession().put("durCompilationEdit", this.getEntityEditId());
//		this.getSession().put("durCompilationEditShow", true);
//		this.getSession().put("durCompilationEditBack", false);
//
//		this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW, false);
//		this.getSession()
//				.put(DURCompilationEditBean.FILTER_PARTNER_VALUE, null);
//		this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE, null);
//		this.getSession().put(DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE,
//				null);
//		this.getSession().put(DURCompilationEditBean.FILTER_COST_ITEM, null);
//		this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW, null);
//
//		this.goTo(PagesTypes.DURCOMPILATIONEDIT);
//	}
	public void showItem() throws HibernateException, PersistenceBeanException {
		CostDefinitions cd = BeansFactory.CostDefinitions().getFirstByDurCompilationWithoutProject(this.getEntityEditId());

		this.getSession().put("costCertProjectId", cd.getProject().getId());
		this.getSession().put("project", cd.getProject().getId());

		DurCompilations dc = BeansFactory.DurCompilations().Load(this.getEntityEditId());
		if (dc != null && !dc.getDeleted()) {
			Boolean canEdit = false;
			for (DURCompilationWrapper dr : this.getList()) {
				if (dr.getId().equals(this.getEntityEditId())) {
					canEdit = dr.isEditAvailable();
					break;
				}
			}
			this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW, false);
			this.getSession().put(DURCompilationEditBean.FILTER_PARTNER_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_COST_ITEM, null);
			this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW, canEdit);

			this.getSession().put("durCompilationEdit", this.getEntityEditId());
			this.getSession().put("durCompilationEditShow", true);
			this.getSession().put("durCompilationEditBack", true);
			this.getSession().put("costCertification", true);
			this.goTo(PagesTypes.DURCOMPILATIONEDIT);
		}
	}

	public void selectAll() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAll())
		{
			for (DURCompilationWrapper dcw : this.getList())
			{
				if (dcw.isSelectable() && dcw.getCanCreatePaymentRequest())
				{
					listIndices.add(dcw.getId());
				}
			}
		}

		this.getViewState().put("costCertificationListCD", listIndices);

		this.Page_Load();
	}

	public void selectItem() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		List<Long> listIndices = new ArrayList<Long>();

		this.setSelectAll(false);

		for (DURCompilationWrapper dcw : this.getList())
		{
			if (dcw.isSelectable() && dcw.isSelected())
			{
				listIndices.add(dcw.getId());
			}
		}

		this.getViewState().put("costCertificationListCD", listIndices);

		this.Page_Load();
	}

	public void certifyItems() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		if (this.getCertificationDate() == null)
		{
			this.markInvalid(
					this.getComponentById("FormListValidate:certDate"), null);
			this.setCertificationDateInvalid(true);
			return;
		}
		else
		{
			this.markValid(this.getComponentById("FormListValidate:certDate"),
					null);
			this.setCertificationDateInvalid(false);
		}
		for (DURCompilationWrapper dcw : this.getList())
		{
			if (dcw.isSelected())
			{
				DurCompilations dc = BeansFactory.DurCompilations().Load(
						dcw.getId());
				dc.setDurState(BeansFactory.DurStates().Load(
						DurStateTypes.Certified.getValue()));
				dc.setCertifiedDate(this.getCertificationDate());

				BeansFactory.DurCompilations().Save(dc);
			}
		}

		this.getViewState().put("costCertificationListCD", null);

		this.Page_Load();
	}

	public void refuseItems() throws HibernateException,
			PersistenceBeanException
	{
		for (DURCompilationWrapper dcw : this.getList())
		{
			if (dcw.isSelected())
			{
				DurCompilations dc = BeansFactory.DurCompilations().Load(
						dcw.getId());
				dc.setRefusedByAcuManager(true);
				for (CostDefinitions cd : BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId()))
				{
					cd.setACUSertified(false);
					BeansFactory.CostDefinitions().Save(cd);
				}

				dc.setDurState(BeansFactory.DurStates().Load(
						DurStateTypes.ACUEvaluation.getValue()));
				dc.setCertifiedDate(this.getCertificationDate());

				BeansFactory.DurCompilations().Save(dc);
			}
		}

		this.getViewState().put("costCertificationListCD", null);

		this.Page_Load();
	}

	/**
	 * Gets startDate
	 * 
	 * @return startDate the startDate
	 */
	public Date getStartDate()
	{
		if (this.getViewState().get("costCertStartDate") != null)
		{
			return (Date) this.getViewState().get("costCertStartDate");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets startDate
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate)
	{
		this.getViewState().put("costCertStartDate", startDate);
	}

	/**
	 * Gets endDate
	 * 
	 * @return endDate the endDate
	 */
	public Date getEndDate()
	{
		if (this.getViewState().get("costCertEndDate") != null)
		{
			return (Date) this.getViewState().get("costCertEndDate");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets endDate
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate)
	{
		this.getViewState().put("costCertEndDate", endDate);
	}

	/**
	 * Sets selectAll
	 * 
	 * @param selectAll
	 *            the selectAll to set
	 */
	public void setSelectAll(boolean selectAll)
	{
		this.getViewState().put("costCertListSelectAll", selectAll);
	}

	/**
	 * Gets selectAll
	 * 
	 * @return selectAll the selectAll
	 */
	public boolean isSelectAll()
	{
		return Boolean.valueOf(String.valueOf(this.getViewState().get(
				"costCertListSelectAll")));
	}

	/**
	 * Sets showCertPanel
	 * 
	 * @param showCertPanel
	 *            the showCertPanel to set
	 */
	public void setShowCertPanel(boolean showCertPanel)
	{
		this.showCertPanel = showCertPanel;
	}

	/**
	 * Gets showCertPanel
	 * 
	 * @return showCertPanel the showCertPanel
	 */
	public boolean isShowCertPanel()
	{
		return showCertPanel;
	}

	/**
	 * Sets certificationDate
	 * 
	 * @param certificationDate
	 *            the certificationDate to set
	 */
	public void setCertificationDate(Date certificationDate)
	{
		this.getViewState().put("costCertificationDate", certificationDate);
	}

	/**
	 * Gets certificationDate
	 * 
	 * @return certificationDate the certificationDate
	 */
	public Date getCertificationDate()
	{
		return (Date) this.getViewState().get("costCertificationDate");
	}

	/**
	 * Sets showSelectAll
	 * 
	 * @param showSelectAll
	 *            the showSelectAll to set
	 */
	public void setShowSelectAll(boolean showSelectAll)
	{
		this.showSelectAll = showSelectAll;
	}

	/**
	 * Gets showSelectAll
	 * 
	 * @return showSelectAll the showSelectAll
	 */
	public boolean isShowSelectAll()
	{
		return showSelectAll;
	}

	/**
	 * Sets asse
	 * 
	 * @param asse
	 *            the asse to set
	 */
	public void setAsse(int asse)
	{
		this.getViewState().put("costCertificationAsse", asse);
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public int getAsse()
	{
		if (this.getViewState().get("costCertificationAsse") != null)
		{
			return Integer.valueOf(String.valueOf(this.getViewState().get(
					"costCertificationAsse")));
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Sets paymentNumber
	 * 
	 * @param paymentNumber
	 *            the paymentNumber to set
	 */
	public void setPaymentNumber(String paymentNumber)
	{
		this.paymentNumber = paymentNumber;
	}

	/**
	 * Gets paymentNumber
	 * 
	 * @return paymentNumber the paymentNumber
	 */
	public String getPaymentNumber()
	{
		return paymentNumber;
	}

	/**
	 * Sets paymentDate
	 * 
	 * @param paymentDate
	 *            the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	/**
	 * Gets paymentDate
	 * 
	 * @return paymentDate the paymentDate
	 */
	public Date getPaymentDate()
	{
		return paymentDate;
	}

	/* Additional search variables */
	public String getSearchProtocolNumber()
	{
		if (this.getViewState().get("costCertProtNumber") != null)
		{
			return (String) this.getViewState().get("costCertProtNumber");
		}
		else
		{
			return null;
		}
	}

	public void setSearchProtocolNumber(String value)
	{
		this.getViewState().put("costCertProtNumber", value);
	}

	public String getProjectCode()
	{
		if (this.getViewState().get("costCertProjectCode") != null)
		{
			return (String) this.getViewState().get("costCertProjectCode");
		}
		else
		{
			return null;
		}
	}

	public void setProjectCode(String value)
	{
		this.getViewState().put("costCertProjectCode", value);
	}

	public String getPaymentRequestNumber()
	{
		if (this.getViewState().get("costCertPaymRqstNm") != null)
		{
			return (String) this.getViewState().get("costCertPaymRqstNm");
		}
		else
		{
			return null;
		}
	}

	public void setPaymentRequestNumber(String value)
	{
		this.getViewState().put("costCertPaymRqstNm", value);
	}

	public Date getStartPaymentRequest()
	{
		if (this.getViewState().get("costCertPaymRqstStDate") != null)
		{
			return (Date) this.getViewState().get("costCertPaymRqstStDate");
		}
		else
		{
			return null;
		}
	}

	public void setStartPaymentRequest(Date date)
	{
		this.getViewState().put("costCertPaymRqstStDate", date);
	}

	public Date getEndPaymentRequest()
	{
		if (this.getViewState().get("costCertPaymRqstEndDate") != null)
		{
			return (Date) this.getViewState().get("costCertPaymRqstEndDate");
		}
		else
		{
			return null;
		}
	}

	public void setEndPaymentRequest(Date date)
	{
		this.getViewState().put("costCertPaymRqstEndDate", date);
	}

	public void setCertified(int val)
	{
		this.getViewState().put("costCertificationCertified", val);
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public int getCertified()
	{
		if (this.getViewState().get("costCertificationCertified") != null)
		{
			return Integer.valueOf(String.valueOf(this.getViewState().get(
					"costCertificationCertified")));
		}
		else
		{
			return 0;
		}
	}

	public void setWithRequest(int val)
	{
		this.getViewState().put("costCertificationWithRequest", val);
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public int getWithRequest()
	{
		if (this.getViewState().get("costCertificationWithRequest") != null)
		{
			return Integer.valueOf(String.valueOf(this.getViewState().get(
					"costCertificationWithRequest")));
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Sets certifiedItems
	 * 
	 * @param certifiedItems
	 *            the certifiedItems to set
	 */
	public void setCertifiedItems(List<SelectItem> certifiedItems)
	{
		this.getViewState().put("costCertItems", certifiedItems);
	}

	/**
	 * Gets certifiedItems
	 * 
	 * @return certifiedItems the certifiedItems
	 */
	public List<SelectItem> getCertifiedItems()
	{
		if (this.getViewState().containsKey("costCertItems"))
		{
			return (List<SelectItem>) this.getViewState().get("costCertItems");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets withRequestItems
	 * 
	 * @param withRequestItems
	 *            the withRequestItems to set
	 */
	public void setWithRequestItems(List<SelectItem> withRequestItems)
	{
		this.getViewState().put("costCertWRequestItems", withRequestItems);
	}

	/**
	 * Gets withRequestItems
	 * 
	 * @return withRequestItems the withRequestItems
	 */
	public List<SelectItem> getWithRequestItems()
	{
		if (this.getViewState().containsKey("costCertWRequestItems"))
		{
			return (List<SelectItem>) this.getViewState().get(
					"costCertWRequestItems");
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets showPaymentPanel
	 * 
	 * @param showPaymentPanel
	 *            the showPaymentPanel to set
	 */
	public void setShowPaymentPanel(boolean showPaymentPanel)
	{
		this.showPaymentPanel = showPaymentPanel;
	}

	/**
	 * Gets showPaymentPanel
	 * 
	 * @return showPaymentPanel the showPaymentPanel
	 */
	public boolean isShowPaymentPanel()
	{
		return showPaymentPanel;
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

	/**
	 * Gets certificationDateInvalid
	 * 
	 * @return certificationDateInvalid the certificationDateInvalid
	 */
	public boolean isCertificationDateInvalid()
	{
		return certificationDateInvalid;
	}

	/**
	 * Sets certificationDateInvalid
	 * 
	 * @param certificationDateInvalid
	 *            the certificationDateInvalid to set
	 */
	public void setCertificationDateInvalid(boolean certificationDateInvalid)
	{
		this.certificationDateInvalid = certificationDateInvalid;
	}

	/**
	 * Gets paymentDataInvalid
	 * 
	 * @return paymentDataInvalid the paymentDataInvalid
	 */
	public boolean isPaymentDataInvalid()
	{
		return paymentDataInvalid;
	}

	/**
	 * Sets paymentDataInvalid
	 * 
	 * @param paymentDataInvalid
	 *            the paymentDataInvalid to set
	 */
	public void setPaymentDataInvalid(boolean paymentDataInvalid)
	{
		this.paymentDataInvalid = paymentDataInvalid;
	}

}
