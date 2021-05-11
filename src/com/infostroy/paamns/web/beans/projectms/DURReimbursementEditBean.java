/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.annotations.Exports;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.RectificationStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.FESRInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.RotationFoundInfo;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class DURReimbursementEditBean extends EntityProjectEditBean<Entity>
{
	protected class AdditionalFormWrapper
	{
		private String	label;

		private String	additionalValue;

		private Double	value;

		private String	valueString;

		public AdditionalFormWrapper()
		{
			super();
		}

		public AdditionalFormWrapper(String label, Double value)
		{
			super();
			this.label = label;
			this.value = value;
		}

		private AdditionalFormWrapper(String label, String valueString)
		{
			super();
			this.label = label;
			this.valueString = valueString;
		}

		/**
		 * Gets label
		 * 
		 * @return label the label
		 */
		@Exports(
		{
				@Export(propertyName = "durReimbNumber", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEdit),
				@Export(propertyName = "durReimbNumber", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEditNote) })
		public String getLabel()
		{
			return label;
		}

		/**
		 * Sets label
		 * 
		 * @param label
		 *            the label to set
		 */
		public void setLabel(String label)
		{
			this.label = label;
		}

		/**
		 * Gets additionalValue
		 * 
		 * @return additionalValue the additionalValue
		 */
		@Exports(
		{
				@Export(propertyName = "durReimbBFNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEdit),
				@Export(propertyName = "durReimbBFNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEditNote) })
		public String getAdditionalValue()
		{
			return additionalValue;
		}

		/**
		 * Sets additionalValue
		 * 
		 * @param additionalValue
		 *            the additionalValue to set
		 */
		public void setAdditionalValue(String additionalValue)
		{
			this.additionalValue = additionalValue;
		}

		/**
		 * Gets value
		 * 
		 * @return value the value
		 */
		@Export(propertyName = "durReimbPaymentAct", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.DurReimbursementEdit)
		public Double getValue()
		{
			return value;
		}

		/**
		 * Sets value
		 * 
		 * @param value
		 *            the value to set
		 */
		public void setValue(Double value)
		{
			this.value = value;
		}

		/**
		 * Gets valueString
		 * 
		 * @return valueString the valueString
		 */
		@Export(propertyName = "durReimbPaymentAct", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.DurReimbursementEditNote)
		public String getValueString()
		{
			return valueString;
		}

		/**
		 * Sets valueString
		 * 
		 * @param valueString
		 *            the valueString to set
		 */
		public void setValueString(String valueString)
		{
			this.valueString = valueString;
		}

	}

	private DurCompilations			durCompilations;

	private DurReimbursements		durReimbursement;

	private List<FESRInfo>			listF;

	private List<RotationFoundInfo>	listR;

	/**
	 * Stores entityEditId value of entity.
	 */
	private Long					entityFEditId;

	private Long					entityREditId;

	private boolean					canAddF;

	private boolean					canAddR;

	private boolean					isEdit;

	private Double					totalReimbursmentAmount;

	private Double					totalDurAmount;

	private Double					previouslyRefundedFesr;

	private Double					previouslyRefundedCn;

	private String					note;

	private Boolean					additionalFormValidateFailed;

	private Double					frCnReimbursement;

	private Boolean					closeReimbursementFailed;

	private Boolean					percentValidationFailed;

	private Boolean					closeReimbrValidationFailed;

	private Double					percentFesr;

	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	@Override
	public void GoBack()
	{
		this.goTo(PagesTypes.DURREIMBURSEMENTLIST);
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		if (this.getSession().get("durReimbEdit") != null)
		{
			this.durCompilations = BeansFactory.DurCompilations().Load(
					String.valueOf(this.getSession().get("durReimbEdit")));

			this.durReimbursement = BeansFactory.DurReimbursements()
					.LoadByDurCompilation(this.durCompilations.getId());
			if (this.durReimbursement != null && !this.durReimbursement.isNew())
			{
				this.getSession().put("durReimbursement",
						this.durReimbursement.getId());
			}
			else
			{
				this.durReimbursement = new DurReimbursements();
				this.durReimbursement.setDurCompilation(this.durCompilations);
				BeansFactory.DurReimbursements().Save(this.durReimbursement);
				this.getSession().put("durReimbursement",
						this.durReimbursement.getId());
			}

			this.setListF(BeansFactory.FESRInfo().LoadByDurReimbursment(
					String.valueOf(this.getSession().get("durReimbursement"))));

			fillPartnerList();
			calculateTotalReimbursementAmount();
			doFilter();
			fillAdditionalFieldForm();

			this.setListR(BeansFactory.RotationFoundInfo()
					.LoadByDurReimbursment(
							String.valueOf(this.getSession().get(
									"durReimbursement"))));

			boolean canAddRotation = false;
			List<DurInfoToResponsiblePeople> list = BeansFactory
					.DurInfoToResponsiblePeople().getByDurInfo(
							BeansFactory
									.DurInfos()
									.LoadByDurCompilation(
											this.getDurCompilations().getId())
									.getId());
			for (DurInfoToResponsiblePeople item : list)
			{
				if (item.getPerson().getCountry().getCode()
						.equals(CountryTypes.Italy.getCountry()))
				{
					canAddRotation = true;
				}
			}

			this.setCanAddF(!this.durReimbursement.getReimbursmentFinished());
			this.setCanAddR(canAddRotation
					&& !this.durReimbursement.getReimbursmentFinished());

			if (!this.getCanAddF() && !this.getCanAddR())
			{
				this.durCompilations.setReimbursement(false);
				this.durCompilations.setReimbursed(true);

				BeansFactory.DurCompilations().Save(this.durCompilations);
			}

			if ((Boolean) this.getSession().get("durReimbEditShow") == true)
			{
				this.setCanAddF(false);
				this.setCanAddR(false);
				this.setIsEdit(false);
			}
			else
			{
				this.setIsEdit(true);
			}
		}
		else
		{
			this.setCanAddF(true);
			this.setCanAddR(true);

		}

	}

	public Boolean getRenderEditLink()
	{
		if (durReimbursement != null)
		{
			if (durReimbursement.getReimbursmentFinished())
			{
				if (getCurrentUser().getAdmin())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

	public Boolean getIsContainSuspendOrRectif()
	{
		try
		{
			List<CostDefinitions> listCosts = BeansFactory.CostDefinitions()
					.GetByDurCompilation(this.durCompilations.getId());

			for (CostDefinitions cd : listCosts)
			{
				if (cd.getRectificationState() != null)
				{
					if (!RectificationStateTypes.Confirmed.getState().equals(
							cd.getRectificationState()))
					{
						return true;
					}
				}
			}
		}
		catch (Exception e)
		{

		}
		return false;
	}

	private void fillAdditionalFieldForm()
	{
		if (this.durReimbursement != null)
		{
			this.setPreviouslyRefundedFesr(this.durReimbursement
					.getPreviouslyRefundedFesr());
			this.setPreviouslyRefundedCn(this.durReimbursement
					.getPreviouslyRefundedCn());
			this.setNote(this.durReimbursement.getNote());

			try
			{
				DurSummaries durSummaries = BeansFactory.DurSummaries()
						.LoadByDurCompilation(
								Long.parseLong(String.valueOf(this.getSession()
										.get("durReimbEdit"))));

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(
								Long.parseLong(String.valueOf(this.getSession()
										.get("durReimbEdit"))));

				if (listCD != null && !listCD.isEmpty())
				{
					this.setFrCnReimbursement(NumberHelper
							.Round(recalculateFrReimb(listCD)));
				}
				else
				{
					this.setFrCnReimbursement(durSummaries
							.getFrCnReimbursementAmount());
				}
				if(this.getPreviouslyRefundedFesr()==null){
					if(durSummaries!=null){
						this.setPreviouslyRefundedFesr(NumberHelper
								.Round(durSummaries.getFesrAmount()));
					}
				}
				if(this.getPreviouslyRefundedCn()==null) {
					double prevRefundedCn = (this.getTotalDurAmount()!=null?this.getTotalDurAmount():0)-
							(this.getPreviouslyRefundedFesr()!=null?this.getPreviouslyRefundedFesr():0)-
							(this.getFrCnReimbursement()!=null?this.getFrCnReimbursement():0);
					
					this.setPreviouslyRefundedCn(prevRefundedCn>0 ? NumberHelper
							.Round(prevRefundedCn) : 0);
				}

			}
			catch (Exception e)
			{
				log.error(e);
			}
		}
	}

	public void cancelAdditionalFormChanges()
	{
		try
		{
			fillAdditionalFieldForm();
		}
		catch (Exception e)
		{

		}
	}

	public void saveAdditinalFormData()
	{
		try
		{
			if (!validateAdditinalFormData())
			{
				this.additionalFormValidateFailed = true;
				return;
			}

			this.durReimbursement
					.setPreviouslyRefundedFesr(getPreviouslyRefundedFesr());
			this.durReimbursement
					.setPreviouslyRefundedCn(getPreviouslyRefundedCn());
			this.durReimbursement.setNote(getNote());

			BeansFactory.DurReimbursements().Save(this.durReimbursement);
		}
		catch (Exception e)
		{
			log.error(e);
		}

		try
		{
			this.Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
		}

	}

	private Boolean validateAdditinalFormData() throws NumberFormatException,
			PersistenceBeanException
	{
		Double totalReimbAm = this.totalReimbursmentAmount != null ? this.totalReimbursmentAmount
				: 0d;

		Double refunded = 0d;

		if (previouslyRefundedFesr != null)
		{
			refunded += previouslyRefundedFesr;
		}

		if (previouslyRefundedCn != null)
		{
			refunded += previouslyRefundedCn;
		}

		// �Quota FESR finora rimborsata� + �Quota CN italiana finora
		// rimborsata� <= �Totale importo rimborsato�
		if (refunded <= totalReimbAm && NumberHelper.Round(refunded)>0.001)
		{
			return true;
		}

		return false;
	}

	private double recalculateAmounts(List<CostDefinitions> cdList)
			throws HibernateException, PersistenceBeanException
	{
		double amountTotal = 0d;

		for (CostDefinitions cost : cdList)
		{
			if (cost.getACUSertified() && cost.getAcuCertification() != null)
			{
				amountTotal += cost.getAcuCertification();
			}
			else if (cost.getAGUSertified()
					&& cost.getAguCertification() != null)
			{
				amountTotal += cost.getAguCertification();
			}
			else if (cost.getSTCSertified()
					&& cost.getStcCertification() != null)
			{
				amountTotal += cost.getStcCertification();
			}
			else if (cost.getCfCheck() != null)
			{
				amountTotal += cost.getCfCheck();
			}
			else if (cost.getCilCheck() != null)
			{
				amountTotal += cost.getCilCheck();
			}

		}

		return amountTotal;
	}

	private void calculateTotalReimbursementAmount()
	{
		Double total = 0d;
		if (this.getListF() != null)
		{
			for (FESRInfo fesrInfo : this.getListF())
			{
				if (fesrInfo.getReimbursementAmount() != null)
				{
					total += fesrInfo.getReimbursementAmount();
				}
			}

		}

		setTotalReimbursmentAmount(total);
		try
		{
			DurSummaries durSummaries = BeansFactory.DurSummaries()
					.LoadByDurCompilation(
							Long.parseLong(String.valueOf(this.getSession()
									.get("durReimbEdit"))));

			List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
					.GetByDurCompilation(
							Long.parseLong(String.valueOf(this.getSession()
									.get("durReimbEdit"))));

			if (listCD != null && !listCD.isEmpty())
			{
				this.setTotalDurAmount(recalculateAmounts(listCD));
			}
			else
			{
				this.setTotalDurAmount(durSummaries.getTotalAmount());
			}
			durSummaries = null;
		}
		catch (Exception e)
		{
			log.error(e);
		}

	}

	private Double recalculateFrReimb(List<CostDefinitions> cdList)
	{
		double amountFrCn = 0d;

		for (CostDefinitions cost : cdList)
		{

			if (cost.getCreatedByPartner())
			{
				List<CFPartnerAnagraphProject> partnerProjectList = null;
				try
				{
					partnerProjectList = BeansFactory
							.CFPartnerAnagraphProject()
							.GetCFAnagraphForProjectAndUser(
									this.getProjectId(), cost.getUser().getId());
				}
				catch (Exception e)
				{
					log.error(e);
				}

				if (partnerProjectList != null && !partnerProjectList.isEmpty())
				{
					if (partnerProjectList.get(0).getCfPartnerAnagraphs()
							.getCountry().getCode()
							.equals(CountryTypes.Italy.getCountry()))
					{
						// amountItCn += (cost.getCfCheck() == null ? (cost
						// .getCilCheck() == null ? 0d : cost
						// .getCilCheck()) : cost.getCfCheck()) * 0.25;
					}
					else
					{
						amountFrCn += (cost.getCfCheck() == null ? (cost
								.getCilCheck() == null ? 0d : cost
								.getCilCheck()) : cost.getCfCheck()) * 0.25;
					}
				}
			}
			else
			{
				if (cost.getCreatedByAGU())
				{
					// amountItCn += (cost.getCilCheck() == null ? 0d : cost
					// .getCilCheck()) * 0.25;
				}
				else
				{
					List<CFPartnerAnagraphProject> partnerProjectList = null;
					try
					{
						partnerProjectList = BeansFactory
								.CFPartnerAnagraphProject()
								.GetCFAnagraphForProjectAndUser(
										this.getProjectId(),
										cost.getUser().getId());
					}
					catch (Exception e)
					{
						log.error(e);
					}
					if (partnerProjectList != null
							&& !partnerProjectList.isEmpty())
					{
						if (partnerProjectList.get(0).getCfPartnerAnagraphs()
								.getCountry().getCode()
								.equals(CountryTypes.Italy.getCountry()))
						{
							// amountItCn += (cost.getCilCheck() == null ?
							// 0d
							// : cost.getCilCheck()) * 0.25;
						}
						else
						{
							amountFrCn += (cost.getCilCheck() == null ? 0d
									: cost.getCilCheck()) * 0.25;
						}
					}

				}
			}

		}
		return amountFrCn;
	}

	public void handleChangePartnerFilter()
	{
		try
		{
			this.Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void exportSummary()
	{
		try
		{
			XlsExport exporter = new XlsExport();

			List<FESRInfo> listToExp = BeansFactory.FESRInfo()
					.LoadByDurReimbursment(
							String.valueOf(this.getSession().get(
									"durReimbursement")));

			List<AdditionalFormWrapper> additionalFormWrappers = new ArrayList<DURReimbursementEditBean.AdditionalFormWrapper>();
			//
			additionalFormWrappers.add(new AdditionalFormWrapper(Utils
					.getString("durReimbBottomFormTotaleImportoRimborsato"),
					this.getTotalReimbursmentAmount()));
			additionalFormWrappers.add(new AdditionalFormWrapper(Utils
					.getString("durReimbBottomFormImportoComplessivoDUR"), this
					.getTotalDurAmount()));
			additionalFormWrappers.add(new AdditionalFormWrapper(Utils
					.getString("durReimbBottomFormQuotaFESRFinoraRimborsata"),
					this.getPreviouslyRefundedFesr()));
			additionalFormWrappers.add(new AdditionalFormWrapper(Utils
					.getString("durReimbBottomFormQuotaCNFinoraRimborsata"),
					this.getPreviouslyRefundedCn()));
			additionalFormWrappers
					.add(new AdditionalFormWrapper(
							Utils.getString("durReimbBottomFormContropartitaNazionaleFrancese"),
							this.getFrCnReimbursement()));

			List<AdditionalFormWrapper> additionalFormWrappersNote = new ArrayList<DURReimbursementEditBean.AdditionalFormWrapper>();
			additionalFormWrappersNote.add(new AdditionalFormWrapper(Utils
					.getString("durReimbBottomFormNote"), this.getNote()));

			byte[] data = exporter.createXls(listToExp,
					ExportPlaces.DurReimbursementEdit, additionalFormWrappers,
					ExportPlaces.DurReimbursementEdit,
					additionalFormWrappersNote,
					ExportPlaces.DurReimbursementEditNote);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportDurReimbursementEdit")
					+ ".xls", is, data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void tryClose()
	{
		try
		{
			if (!validateAdditinalFormData())
			{
				this.additionalFormValidateFailed = true;
				this.setCloseReimbrValidationFailed(true);
				return;
			}
			this.setCloseReimbrValidationFailed(false);
			Double quotaSum = 0d;
			quotaSum += (getPreviouslyRefundedFesr() != null ? getPreviouslyRefundedFesr()
					: 0d);
			quotaSum += (getPreviouslyRefundedCn() != null ? getPreviouslyRefundedCn()
					: 0d);

			Double durCompilationAmount = (getTotalDurAmount() != null ? getTotalDurAmount()
					: 0d);
			durCompilationAmount -= (getFrCnReimbursement() != null ? getFrCnReimbursement()
					: 0d);

			if (Math.abs(NumberHelper.Round(quotaSum)
					- NumberHelper.Round(durCompilationAmount)) < 0.009d && NumberHelper.Round(quotaSum)>0)
			{
				this.setCloseReimbursementFailed(false);
			}
			else
			{
				this.setCloseReimbrValidationFailed(true);
				this.setCloseReimbursementFailed(true);
			}

			Double qfr = getPreviouslyRefundedFesr() != null ? getPreviouslyRefundedFesr()
					: 0d;

			Double totalAmountDur = getTotalDurAmount() != null ? getTotalDurAmount()
					: 0d;

			Double percent = ((qfr / totalAmountDur) * 100.0d);

			if (percent >= 74.8d && percent <= 75.2d)
			{
				this.setPercentValidationFailed(false);
			}
			else
			{
				this.setCloseReimbrValidationFailed(true);
				this.setPercentValidationFailed(true);
			}

			setPercentFesr(percent);

		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void fillPartnerList()
	{
		List<SelectItem> items = new ArrayList<SelectItem>();

		items.add(SelectItemHelper.getAllElement());
		for (FESRInfo fesrInfo : this.getListF())
		{
			boolean alreadyAdded = false;
			if (fesrInfo.getFesrBf() == null)
			{
				continue;
			}
			for (SelectItem item : items)
			{
				if (fesrInfo.getFesrBf().getId().toString()
						.equals(item.getValue().toString()))
				{
					alreadyAdded = true;
					break;
				}
			}
			if (!alreadyAdded)
			{
				items.add(new SelectItem(fesrInfo.getFesrBf().getId()
						.toString(), fesrInfo.getFesrBf().getName()));
			}
		}

		this.setPartnerList(items);
	}

	public void doFilter()
	{
		if (this.getSelectedPartnerId() == null
				|| this.getSelectedPartnerId().isEmpty())
		{
			return;
		}
		Long id = null;
		try
		{
			id = Long.parseLong(this.getSelectedPartnerId());
		}
		catch (Exception e)
		{
			return;
		}

		List<FESRInfo> tempList = new ArrayList<FESRInfo>();

		for (FESRInfo item : getListF())
		{
			if (item.getFesrBf() == null
					|| !item.getFesrBf().getId().equals(id))
			{
				continue;
			}
			tempList.add(item);
		}

		this.setListF(tempList);

	}

	public void addFEntity()
	{
		this.getSession().put("fesrinfo", null);
		this.goTo(PagesTypes.FESRINFO);
	}

	public void addREntity()
	{
		this.getSession().put("rotationfoundinfo", null);
		this.goTo(PagesTypes.ROTATIONFOUNDINFO);
	}

	public void editFEntity()
	{
		this.getSession().put("fesrinfo", this.getEntityFEditId());
		this.goTo(PagesTypes.FESRINFO);
	}

	public void editREntity()
	{
		this.getSession().put("rotationfoundinfo", this.getEntityREditId());
		this.goTo(PagesTypes.ROTATIONFOUNDINFO);
	}

	public void deleteFEntity()
	{
	}

	public void deleteREntity()
	{
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		if (!this.getSessionBean().getIsActualSate())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}

		if (!(AccessGrantHelper.IsReadyDurCompilation() && AccessGrantHelper
				.CheckRolesDURCompilation()))
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException
	{
		if (this.durReimbursement != null)
		{
			this.durReimbursement.setReimbursmentFinished(true);

			BeansFactory.DurReimbursements().SaveInTransaction(
					this.durReimbursement);

			this.durCompilations.setReimbursement(false);
			this.durCompilations.setReimbursed(true);

			BeansFactory.DurCompilations().SaveInTransaction(
					this.durCompilations);
		}
	}

	/**
	 * Sets durReimbursement
	 * 
	 * @param durReimbursement
	 *            the durReimbursement to set
	 */
	public void setDurReimbursement(DurReimbursements durReimbursement)
	{
		this.durReimbursement = durReimbursement;
	}

	/**
	 * Gets durReimbursement
	 * 
	 * @return durReimbursement the durReimbursement
	 */
	public DurReimbursements getDurReimbursement()
	{
		return durReimbursement;
	}

	/**
	 * Sets durCompilations
	 * 
	 * @param durCompilations
	 *            the durCompilations to set
	 */
	public void setDurCompilations(DurCompilations durCompilations)
	{
		this.durCompilations = durCompilations;
	}

	/**
	 * Gets durCompilations
	 * 
	 * @return durCompilations the durCompilations
	 */
	public DurCompilations getDurCompilations()
	{
		return durCompilations;
	}

	/**
	 * Sets listF
	 * 
	 * @param listF
	 *            the listF to set
	 */
	public void setListF(List<FESRInfo> listF)
	{
		this.listF = listF;
	}

	/**
	 * Gets listF
	 * 
	 * @return listF the listF
	 */
	public List<FESRInfo> getListF()
	{
		return listF;
	}

	/**
	 * Sets listR
	 * 
	 * @param listR
	 *            the listR to set
	 */
	public void setListR(List<RotationFoundInfo> listR)
	{
		this.listR = listR;
	}

	/**
	 * Gets listR
	 * 
	 * @return listR the listR
	 */
	public List<RotationFoundInfo> getListR()
	{
		return listR;
	}

	/**
	 * Sets canAddF
	 * 
	 * @param canAddF
	 *            the canAddF to set
	 */
	public void setCanAddF(boolean canAddF)
	{
		this.canAddF = canAddF;
	}

	/**
	 * Gets canAddF
	 * 
	 * @return canAddF the canAddF
	 */
	public boolean getCanAddF()
	{
		return canAddF;
	}

	/**
	 * Sets canAddR
	 * 
	 * @param canAddR
	 *            the canAddR to set
	 */
	public void setCanAddR(boolean canAddR)
	{
		this.canAddR = canAddR;
	}

	/**
	 * Gets canAddR
	 * 
	 * @return canAddR the canAddR
	 */
	public boolean getCanAddR()
	{
		return canAddR;
	}

	/**
	 * Sets entityFEditId
	 * 
	 * @param entityFEditId
	 *            the entityFEditId to set
	 */
	public void setEntityFEditId(Long entityFEditId)
	{
		this.entityFEditId = entityFEditId;
	}

	/**
	 * Gets entityFEditId
	 * 
	 * @return entityFEditId the entityFEditId
	 */
	public Long getEntityFEditId()
	{
		return entityFEditId;
	}

	/**
	 * Sets entityFDeleteId
	 * 
	 * @param entityFDeleteId
	 *            the entityFDeleteId to set
	 */
	public void setEntityFDeleteId(Long entityFDeleteId)
	{
		this.getViewState().put("entityFDeleteId", entityFDeleteId);
	}

	/**
	 * Gets entityFDeleteId
	 * 
	 * @return entityFDeleteId the entityFDeleteId
	 */
	public Long getEntityFDeleteId()
	{
		return (Long) this.getViewState().get("entityFDeleteId");
	}

	/**
	 * Sets entityREditId
	 * 
	 * @param entityREditId
	 *            the entityREditId to set
	 */
	public void setEntityREditId(Long entityREditId)
	{
		this.entityREditId = entityREditId;
	}

	/**
	 * Gets entityREditId
	 * 
	 * @return entityREditId the entityREditId
	 */
	public Long getEntityREditId()
	{
		return entityREditId;
	}

	/**
	 * Sets entityRDeleteId
	 * 
	 * @param entityRDeleteId
	 *            the entityRDeleteId to set
	 */
	public void setEntityRDeleteId(Long entityRDeleteId)
	{
		this.getViewState().put("entityRDeleteId", entityRDeleteId);

	}

	/**
	 * Gets entityRDeleteId
	 * 
	 * @return entityRDeleteId the entityRDeleteId
	 */
	public Long getEntityRDeleteId()
	{
		return (Long) this.getViewState().get("entityRDeleteId");
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
	}

	/**
	 * Gets isEdit
	 * 
	 * @return isEdit the isEdit
	 */
	public boolean getIsEdit()
	{
		return isEdit;
	}

	/**
	 * Gets partnerList
	 * 
	 * @return partnerList the partnerList
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getPartnerList()
	{
		if (getViewState().containsKey("partnerList"))
		{
			return (List<SelectItem>) getViewState().get("partnerList");
		}
		return null;
	}

	/**
	 * Sets partnerList
	 * 
	 * @param partnerList
	 *            the partnerList to set
	 */
	public void setPartnerList(List<SelectItem> partnerList)
	{
		getViewState().put("partnerList", partnerList);
	}

	/**
	 * Gets selectedPartnerId
	 * 
	 * @return selectedPartnerId the selectedPartnerId
	 */
	public String getSelectedPartnerId()
	{
		if (getViewState().containsKey("selectedPartnerId"))
		{
			return (String) getViewState().get("selectedPartnerId");
		}
		return null;
	}

	/**
	 * Sets selectedPartnerId
	 * 
	 * @param selectedPartnerId
	 *            the selectedPartnerId to set
	 */
	public void setSelectedPartnerId(String selectedPartnerId)
	{
		getViewState().put("selectedPartnerId", selectedPartnerId);
	}

	/**
	 * Gets totalReimbursmentAmount
	 * 
	 * @return totalReimbursmentAmount the totalReimbursmentAmount
	 */
	public Double getTotalReimbursmentAmount()
	{
		return totalReimbursmentAmount;
	}

	/**
	 * Sets totalReimbursmentAmount
	 * 
	 * @param totalReimbursmentAmount
	 *            the totalReimbursmentAmount to set
	 */
	public void setTotalReimbursmentAmount(Double totalReimbursmentAmount)
	{
		this.totalReimbursmentAmount = totalReimbursmentAmount;
	}

	/**
	 * Gets totalDurAmount
	 * 
	 * @return totalDurAmount the totalDurAmount
	 */
	public Double getTotalDurAmount()
	{
		return totalDurAmount;
	}

	/**
	 * Sets totalDurAmount
	 * 
	 * @param totalDurAmount
	 *            the totalDurAmount to set
	 */
	public void setTotalDurAmount(Double totalDurAmount)
	{
		this.totalDurAmount = totalDurAmount;
	}

	/**
	 * Gets previouslyRefundedFesr
	 * 
	 * @return previouslyRefundedFesr the previouslyRefundedFesr
	 */
	public Double getPreviouslyRefundedFesr()
	{
		return previouslyRefundedFesr;
	}

	/**
	 * Sets previouslyRefundedFesr
	 * 
	 * @param previouslyRefundedFesr
	 *            the previouslyRefundedFesr to set
	 */
	public void setPreviouslyRefundedFesr(Double previouslyRefundedFesr)
	{
		this.previouslyRefundedFesr = previouslyRefundedFesr;
	}

	/**
	 * Gets previouslyRefundedCn
	 * 
	 * @return previouslyRefundedCn the previouslyRefundedCn
	 */
	public Double getPreviouslyRefundedCn()
	{
		return previouslyRefundedCn;
	}

	/**
	 * Sets previouslyRefundedCn
	 * 
	 * @param previouslyRefundedCn
	 *            the previouslyRefundedCn to set
	 */
	public void setPreviouslyRefundedCn(Double previouslyRefundedCn)
	{
		this.previouslyRefundedCn = previouslyRefundedCn;
	}

	/**
	 * Gets note
	 * 
	 * @return note the note
	 */
	public String getNote()
	{
		return note;
	}

	/**
	 * Sets note
	 * 
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note)
	{
		this.note = note;
	}

	/**
	 * Gets additionalFormValidateFailed
	 * 
	 * @return additionalFormValidateFailed the additionalFormValidateFailed
	 */
	public Boolean getAdditionalFormValidateFailed()
	{
		return additionalFormValidateFailed;
	}

	/**
	 * Sets additionalFormValidateFailed
	 * 
	 * @param additionalFormValidateFailed
	 *            the additionalFormValidateFailed to set
	 */
	public void setAdditionalFormValidateFailed(
			Boolean additionalFormValidateFailed)
	{
		this.additionalFormValidateFailed = additionalFormValidateFailed;
	}

	/**
	 * Gets frCnReimbursement
	 * 
	 * @return frCnReimbursement the frCnReimbursement
	 */
	public Double getFrCnReimbursement()
	{
		return frCnReimbursement;
	}

	/**
	 * Sets frCnReimbursement
	 * 
	 * @param frCnReimbursement
	 *            the frCnReimbursement to set
	 */
	public void setFrCnReimbursement(Double frCnReimbursement)
	{
		this.frCnReimbursement = frCnReimbursement;
	}

	/**
	 * Gets closeReimbursementFailed
	 * 
	 * @return closeReimbursementFailed the closeReimbursementFailed
	 */
	public Boolean getCloseReimbursementFailed()
	{
		return closeReimbursementFailed;
	}

	/**
	 * Sets closeReimbursementFailed
	 * 
	 * @param closeReimbursementFailed
	 *            the closeReimbursementFailed to set
	 */
	public void setCloseReimbursementFailed(Boolean closeReimbursementFailed)
	{
		this.closeReimbursementFailed = closeReimbursementFailed;
	}

	/**
	 * Gets percentValidationFailed
	 * 
	 * @return percentValidationFailed the percentValidationFailed
	 */
	public Boolean getPercentValidationFailed()
	{
		return percentValidationFailed;
	}

	/**
	 * Sets percentValidationFailed
	 * 
	 * @param percentValidationFailed
	 *            the percentValidationFailed to set
	 */
	public void setPercentValidationFailed(Boolean percentValidationFailed)
	{
		this.percentValidationFailed = percentValidationFailed;
	}

	/**
	 * Gets closeReimbrValidationFailed
	 * 
	 * @return closeReimbrValidationFailed the closeReimbrValidationFailed
	 */
	public Boolean getCloseReimbrValidationFailed()
	{
		return closeReimbrValidationFailed;
	}

	/**
	 * Sets closeReimbrValidationFailed
	 * 
	 * @param closeReimbrValidationFailed
	 *            the closeReimbrValidationFailed to set
	 */
	public void setCloseReimbrValidationFailed(
			Boolean closeReimbrValidationFailed)
	{
		this.closeReimbrValidationFailed = closeReimbrValidationFailed;
	}

	/**
	 * Gets percentFesr
	 * 
	 * @return percentFesr the percentFesr
	 */
	public Double getPercentFesr()
	{
		return percentFesr;
	}

	/**
	 * Sets percentFesr
	 * 
	 * @param percentFesr
	 *            the percentFesr to set
	 */
	public void setPercentFesr(Double percentFesr)
	{
		this.percentFesr = percentFesr;
	}

}
