package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExpenditureDeclarationStatusTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PaymentApplicationStatusTypes;
import com.infostroy.paamns.common.enums.RequestType;
import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AdvancesIncludedInPaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpendituresOfPaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.DURCompilationWrapper;

public class PaymentApplicationEditBean extends EntityListBean<DURCompilationWrapper> {

	private String paymentApplicationId;

	private Date compilationDate;

	private String protocolNumber;

	private Date protocolDate;

	private Double amountStateAidInPaymentApplication;

	private Double amountEligibleExpenses;

	private Double totalAmountOfPublicExpenditure;

	private List<RequestTypes> requestTypes;

	private List<SelectItem> listTypes;

	private boolean rendered;

	private boolean edit;

	private boolean showButton;

	private boolean showSelectAll;

	private boolean canDelete;

	private Long entityId;

	private Long entityEditId;

	private PaymentApplication pa;

	private boolean submitted;

	private List<DurCompilations> listSelectedDur;

	private Transaction tr;

	private Long state_id;

	private boolean selectAll;

	private boolean validationFailed;

	private String error;

	private Double totalAmountOfCertifications;

	private boolean typeEdit;

	private List<AdvancesIncludedInPaymentApplication> listAdvances;

	private List<ExpendituresOfPaymentApplication> expenditures;

	private boolean itemSelected;

	public void export() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(), ExportPlaces.CostCertification);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportCostCertification") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportExpenditures() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getExpenditures(), ExportPlaces.PaymentApplicationExpenditures);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("paymentApplicationEditExpenditureDivideByAxis") + ".xls", is,
					data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private double amountStateAidInPaymentApplicationComputation() throws HibernateException, PersistenceBeanException {
		DurCompilations dc = null;
		List<CostDefinitions> cdList = new ArrayList<>();
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				dc = BeansFactory.DurCompilations().Load(dcw.getId());
				cdList.addAll(dc.getCostDefinitions());
			}
		}
		double sum = 0d;
		for (CostDefinitions cd : cdList) {
			if (cd.getStateAid()) {
				sum += cd.getAmountReport();
			}
		}
		return sum * 0.85;
	}

	private double amountEligibleExpensesComputation() {
		double sum = 0d;
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				sum += dcw.getSumAmoutCertification();
			}
		}
		try {
			List<DurCompilations> listOldDur = BeansFactory.DurCompilations().LoadAllWithPaymentApplication();
			List<PaymentApplication> listPASubmitteOfReferencePeriod = BeansFactory.PaymentApplications()
					.loadAllSubmittedByAccountingPeriod(this.getDateFrom(), this.getDateTo());
			if (!this.getType().equals(String.valueOf(RequestType.IntermediateFinal.getId()))) {
				for (DurCompilations dc : listOldDur) {
					for (PaymentApplication pai : listPASubmitteOfReferencePeriod) {
						if (dc.getPaymentApplication().getId().equals(pai.getId())) {
							List<CostDefinitions> listOldCostDefinition = dc.getCostDefinitions();
							for (CostDefinitions cd : listOldCostDefinition) {
								sum += (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
							}
						}
					}

				}
			}

		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		return sum;
	}
	
	private double amountPublicExpensesComputation() {
		//double sum = 0d;
		double sumpublic = 0d;
		
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				//sum += dcw.getSumAmoutCertification();
				sumpublic += dcw.getSumPublicAmoutCertification();
			}
		}
		try {
			List<DurCompilations> listOldDur = BeansFactory.DurCompilations().LoadAllWithPaymentApplication();
			List<PaymentApplication> listPASubmitteOfReferencePeriod = BeansFactory.PaymentApplications()
					.loadAllSubmittedByAccountingPeriod(this.getDateFrom(), this.getDateTo());
			if (!this.getType().equals(String.valueOf(RequestType.IntermediateFinal.getId()))) {
				for (DurCompilations dc : listOldDur) {
					for (PaymentApplication pai : listPASubmitteOfReferencePeriod) {
						if (dc.getPaymentApplication().getId().equals(pai.getId())) {
							List<CostDefinitions> listOldCostDefinition = dc.getCostDefinitions();
							for (CostDefinitions cd : listOldCostDefinition) {
								sumpublic += (cd.getAcuCheckPublicAmount() != null ? cd.getAcuCheckPublicAmount() : 0d);      // (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
							}
						}
					}

				}
			}

		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		return sumpublic;
	}

	private double totalAmountOfPublicExpenditureComputation() {
		//return amountEligibleExpensesComputation();
		return amountPublicExpensesComputation();
	}

	public void showItem() throws HibernateException, PersistenceBeanException {
		CostDefinitions cd = BeansFactory.CostDefinitions()
				.getFirstByDurCompilationWithoutProject(this.getEntityEditId());

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
			this.getSession().put("paymentApplication", true);
			this.goTo(PagesTypes.DURCOMPILATIONEDIT);
		}
	}

	public void temporarySave() {
		if (!checkEmpty("EditForm:tab1:type", this.getType())) {
			return;
		}
		this.setSubmitted(false);

		getSelectedDur();
		generateStatus();
		this.Page_Save();
	}

	public void definitiveSave() {
		getSelectedDur();
		if (validateFieldsToGenerateStatus()) {
			return;
		}
		this.Page_Save();
	}

	private boolean validateFieldsToGenerateStatus() {
		this.validationFailed = false;
		checkEmpty("EditForm:tab1:protocolNumber", this.getProtocolNumber());
		checkEmpty("EditForm:tab1:type", this.getType());
		if (this.getProtocolDate() != null) {
			checkProtocolDate("EditForm:tab1:createFrom2");
		} else {
			this.validationFailed = true;
		}

		if (listSelectedDur.size() <= 0) {
			this.validationFailed = true;
		}

		if (this.validationFailed) {
			setError(Utils.getString("Compila i campi obbligatori"));
		} else {
			ExpenditureDeclarationStates eds = null;
			try {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());
			} catch (PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("db error");
			}
			setState_id(eds.getId());
		}
		return this.validationFailed;
	}

	private boolean checkProtocolDate(String id) {
		Calendar pd = new GregorianCalendar();
		pd.setTime(this.getProtocolDate());
		Calendar today = new GregorianCalendar();
		today.add(Calendar.DAY_OF_MONTH, -1);
		if (pd.after(today)) {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

		this.validationFailed = true;
		ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
				Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
		this.getFailedValidation().add(id);
		setError("La data del Protocollo deve essere maggiore di quella di compilazione");
		return false;
	}

	// private boolean checkSelectedList(String id, Object value) {
	// if (value.equals("0")) {
	// this.validationFailed = true;
	// ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
	// Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(),
	// null);
	// this.getFailedValidation().add(id);
	// return false;
	// } else {
	// if (this.getFailedValidation().contains(id)) {
	// this.getFailedValidation().remove(id);
	// ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
	// FacesContext.getCurrentInstance(), null);
	// }
	// }
	// return true;
	// }

	private boolean checkEmpty(String id, Object value) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (str == null || str.isEmpty() || str.trim().isEmpty()) {
			this.validationFailed = true;

			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}

	public void SaveEntity() throws PersistenceBeanException {
		PaymentApplication entityToSave = null;
		if (Boolean.valueOf(this.getSession().get("paymentApplicationNew").toString())) {
			entityToSave = new PaymentApplication();
			entityToSave.setDeleted(false);
			entityToSave.setCreateDate(new GregorianCalendar().getTime());
		} else {
			entityToSave = pa;
		}
		if (this.getCompilationDate() != null) {
			entityToSave.setCompilationDate(this.getCompilationDate());
		}
		if (this.getProtocolNumber() != null && !this.getProtocolNumber().trim().equals("")
				&& !this.getProtocolNumber().isEmpty()) {
			entityToSave.setProtocolNumber(this.getProtocolNumber());
		}
		if (this.getProtocolDate() != null) {
			entityToSave.setProtocolDate(this.getProtocolDate());
		}
		if (this.getAmountStateAidInPaymentApplication() != null
				&& !this.getAmountStateAidInPaymentApplication().equals("")) {
			entityToSave.setAmountStateAidInPaymentApplication(this.getAmountStateAidInPaymentApplication());
		}
		if (this.getAmountEligibleExpenses() != null && !this.getAmountEligibleExpenses().equals("")) {
			entityToSave.setAmountEligibleExpenses(this.getAmountEligibleExpenses());
		}
		if (this.getTotalAmountOfPublicExpenditure() != null && !this.getTotalAmountOfPublicExpenditure().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditure(this.getTotalAmountOfPublicExpenditure());
		}
		ExpenditureDeclarationStates es = null;
		if (this.getState_id() == null) {
			es = BeansFactory.ExpenditureDeclarationStates()
					.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Compiled.getValue());
			setState_id(es.getId());
		}
		if (this.getState_id() != null) {
			es = new ExpenditureDeclarationStates();
			es.setId(this.getState_id());
			entityToSave.setPaymentApplicationState(es);
		}

		es = BeansFactory.ExpenditureDeclarationStates()
				.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());

		if (es.getId().equals(this.getState_id())) {
			entityToSave.setSubmissionDate(new GregorianCalendar().getTime());
		}

		if (this.getDateFrom() != null) {
			entityToSave.setAccountingPeriodFrom(this.getDateFrom());
		}

		if (this.getDateTo() != null) {
			entityToSave.setAccountingPeriodTo(this.getDateTo());
		}

		if (this.getType() != null) {
			for (RequestTypes rt : this.getRequestTypes()) {
				if (this.getType().equals(String.valueOf(rt.getId()))) {
					entityToSave.setRequestType(rt);
				}
			}

		}

		entityToSave.setTotalAmountOfCertifications((this.recomputeTotaleAmountOfCertification()));

		PaymentApplication savedEntity;
		savedEntity = (PaymentApplication) PersistenceSessionManager.getBean().getSession().merge(entityToSave);

		List<PaymentApplicationDurCompilations> padcs = new ArrayList<>();
		PaymentApplicationDurCompilations padc = null;
		for (DurCompilations dc : this.getListSelectedDur()) {
			padc = new PaymentApplicationDurCompilations(savedEntity.getId(), dc.getId());
			padc.setDeleted(false);
			padc.setCreateDate(new GregorianCalendar().getTime());
			padcs.add(padc);
			List<PaymentApplicationDurCompilations> pas = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(savedEntity.getId());
			if (pas != null && !pas.isEmpty()) {
				boolean founded = false;
				for (PaymentApplicationDurCompilations pa : pas) {

					if (pa.getDurCompilationsId().equals(dc.getId())) {
						founded = true;
					}
				}
				if (!founded) {
					PersistenceSessionManager.getBean().getSession().merge(padc);
				}
			} else {
				PersistenceSessionManager.getBean().getSession().merge(padc);
			}

		}

		for (AdvancesIncludedInPaymentApplication adv : this.getListAdvances()) {
			adv.setPaymentApplication(savedEntity);
			if (adv.getCreateDate() == null) {
				adv.setCreateDate(new GregorianCalendar().getTime());
				adv.setDeleted(false);
			}

			adv = (AdvancesIncludedInPaymentApplication) PersistenceSessionManager.getBean().getSession().merge(adv);
		}
		savedEntity.setAdvances(this.getListAdvances());
		BeansFactory.PaymentApplications().UpdateInTransaction(savedEntity);
		if (this.getListSelectedDur().size() > 0) {
			for (DurCompilations dur : this.getListSelectedDur()) {
				if (dur.getPaymentApplication() == null) {
					dur.setPaymentApplication(savedEntity);
				}
			}
		}

		List<ExpendituresOfPaymentApplication> listExpendituresToSave = new ArrayList<>();

		List<ExpendituresOfPaymentApplication> oldExpenditures = BeansFactory.ExpendituresOfPaymentApplication()
				.loadByPaymentApplicationId(savedEntity.getId());
		if (oldExpenditures != null && !oldExpenditures.isEmpty()) {
			for (ExpendituresOfPaymentApplication expai : oldExpenditures) {
				for (ExpendituresOfPaymentApplication expaj : this.getExpenditures()) {
					if (expai.getAsse().equals(expaj.getAsse())) {
						expai.setTotalAmountEligibleExpenditure(expaj.getTotalAmountEligibleExpenditure());
						expai.setTotalAmountPublicExpenditure(expaj.getTotalAmountPublicExpenditure());
					}
				}
				expai = (ExpendituresOfPaymentApplication) PersistenceSessionManager.getBean().getSession()
						.merge(expai);
			}

		} else {
			for (ExpendituresOfPaymentApplication expai : this.getExpenditures()) {
				if (!expai.getAsse().equals(Utils.getString("total"))) {
					expai.setPaymentApplication(savedEntity);
					if (expai.getCreateDate() == null) {
						expai.setCreateDate(new GregorianCalendar().getTime());
						expai.setDeleted(false);
					}

					expai = (ExpendituresOfPaymentApplication) PersistenceSessionManager.getBean().getSession()
							.merge(expai);
					listExpendituresToSave.add(expai);
				}

			}
		}

		Double result = resultTotalAmountOfCertificatioToUpgrade(savedEntity);
		List<PaymentApplication> listPaNotSubmitted = BeansFactory.PaymentApplications().loadAllNotSubmitted();
		if (savedEntity.getPaymentApplicationState().getId().equals(PaymentApplicationStatusTypes.Submitted.getId())) {
			for (PaymentApplication pai : listPaNotSubmitted) {
				if (pai.getId().equals(savedEntity.getId())) {
					continue;
				}

				Double adcValidationForAmountOfCertifications = 0d;
				List<PaymentApplicationDurCompilations> listPadc = BeansFactory.PaymentApplicationDurCompilations()
						.getAllByPaymentApplication(pai.getId());
				for (PaymentApplicationDurCompilations padci : listPadc) {
					DurCompilations dc = BeansFactory.DurCompilations().Load(padci.getDurCompilationsId());
					List<CostDefinitions> listCd = dc.getCostDefinitions();
					for (CostDefinitions cd : listCd) {
						adcValidationForAmountOfCertifications += (cd.getAcuCertification() != null
								? cd.getAcuCertification() : 0d);
					}
				}
				pai.setTotalAmountOfCertifications(
						adcValidationForAmountOfCertifications + entityToSave.getTotalAmountOfCertifications());
				BeansFactory.PaymentApplications().UpdateInTransaction(pai);

				Double adcValidationForEligibleExpenditure = 0d;
				if (entityToSave.getAccountingPeriodFrom().equals(pai.getAccountingPeriodFrom())
						&& entityToSave.getAccountingPeriodTo().equals(pai.getAccountingPeriodTo())) {
					for (PaymentApplicationDurCompilations padci : listPadc) {
						DurCompilations dc = BeansFactory.DurCompilations().Load(padci.getDurCompilationsId());
						List<CostDefinitions> listCd = dc.getCostDefinitions();
						for (CostDefinitions cd : listCd) {
							adcValidationForEligibleExpenditure += (cd.getAcuCertification() != null
									? cd.getAcuCertification() : 0d);
						}
					}
					pai.setAmountEligibleExpenses(
							adcValidationForEligibleExpenditure + entityToSave.getAmountEligibleExpenses());
					pai.setTotalAmountOfPublicExpenditure(pai.getAmountEligibleExpenses());
				}
			}
		}

	}

	private Double resultTotalAmountOfCertificatioToUpgrade(PaymentApplication savedPaymentApplication)
			throws PersistenceBeanException {
		DurCompilations dc = null;
		CostDefinitions cd = null;

		Double result = 0d;
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				dc = BeansFactory.DurCompilations().Load(dcw.getId());
				if (savedPaymentApplication.getRequestType() != null
						&& savedPaymentApplication.getSubmissionDate() != null) {
					if (dc.getPaymentApplication() != null
							&& dc.getPaymentApplication().getId().equals(savedPaymentApplication.getId())) {
						cd = BeansFactory.CostDefinitions().getFirstByDurCompilationWithoutProject(dc.getId());
						result += (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
					}
				}

			}
		}
		return result;
	}

	public void Page_Save() {
		if (getSaveFlag() == 0) {
			try {
				if (!this.BeforeSave()) {
					return;
				}
			} catch (PersistenceBeanException e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			} catch (Exception e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			}

			try {
				tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			} catch (HibernateException e1) {
				log.error("EntityEditBean exception:", e1);
			} catch (PersistenceBeanException e1) {
				log.error("EntityEditBean exception:", e1);
			}
			setSaveFlag(1);
			try {
				this.SaveEntity();
			} catch (HibernateException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (PersistenceBeanException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (NumberFormatException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (Exception e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} finally {
				if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
					tr.commit();
				}
			}

			this.AfterSave();
			setSaveFlag(0);
		}
	}

	public void GoBack() {
		if ((Boolean) this.getSession().get("paymentApplicationNew")) {
			this.getSession().remove("paymentApplicationNew");
		}
		if (this.getSession().get("paymentApplicationEntityId") != null) {
			this.getSession().remove("paymentApplicationEntityId");
			this.getSession().remove("paymentApplicationEdit");
		}
		this.setType(null);
		this.setList(null);
		this.goTo(PagesTypes.PAYMENTAPPLICATIONLIST);
	}

	public void AfterSave() {
		this.GoBack();

	}

	public Boolean BeforeSave() throws PersistenceBeanException {
		return true;
	}

	private void generateStatus() {
		ExpenditureDeclarationStates eds = null;
		try {
			boolean statusValidated = true;
			if (this.getProtocolNumber() == null || this.getProtocolNumber().isEmpty()
					|| this.getProtocolNumber().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getType() == null || this.getType().isEmpty() || this.getType().trim().isEmpty()
					|| this.getType().equals("0")) {
				statusValidated = false;
			}
			if (this.getAmountStateAidInPaymentApplication() == null
					|| this.getAmountStateAidInPaymentApplication() == 0) {
				statusValidated = false;
			}
			if (this.getAmountEligibleExpenses() == null || this.getAmountEligibleExpenses() == 0) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfPublicExpenditure() == null || this.getTotalAmountOfPublicExpenditure() == 0) {
				statusValidated = false;
			}
			if (listSelectedDur.size() <= 0) {
				statusValidated = false;
			}
			if (statusValidated) {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Generated.getValue());
			} else {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Compiled.getValue());
			}
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		setState_id(eds.getId());
	}

	private void getSelectedDur() {
		listSelectedDur = new ArrayList<>();

		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				DurCompilations dc;
				try {
					dc = BeansFactory.DurCompilations().Load(dcw.getId());
					listSelectedDur.add(dc);
				} catch (HibernateException | PersistenceBeanException e) {
					// TODO Auto-generated catch block
					log.error("db: retrive error " + e);
				}

			}
		}
	}

	@Override
	public void Page_Load() {
		// TODO Auto-generated method stub
		try {
			initAdvances();

			this.setFailedValidation(new ArrayList<String>());
			if (Boolean.TRUE.equals(this.getSession().get("paymentApplicationNew"))) {
				initExpenditureOfPaymentApplication();
				newPaymentApplicationPage();
			}
			if (this.getSession().get("paymentApplicationEntityId") != null) {
				editPaymentApplicationPage();
				showPaymentApplicationPage();
			}
		} catch (PersistenceBeanException e) {
			log.error(e.getMessage());
		}
	}

	private void amountNotCoveredForAxisComputation() {
		for (AdvancesIncludedInPaymentApplication a : this.listAdvances) {
			a.setAmountNotCovered(a.getAmountPaid() - a.getAmountCovered());
		}
	}

	private void amountCoveredForAxisComputation() throws HibernateException, PersistenceBeanException {
		DurCompilations dc = null;
		double amountCoveredAxis1 = 0d;
		double amountCoveredAxis2 = 0d;
		double amountCoveredAxis3 = 0d;
		double amountCoveredAxis4 = 0d;
		List<CostDefinitions> cd = new ArrayList<>();
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				dc = BeansFactory.DurCompilations().Load(dcw.getId());
				cd.addAll(dc.getCostDefinitions());
			}
		}
		for (CostDefinitions cdi : cd) {
			if (cdi.getAmountToCoverAdvanceStateAid() != null) {
				if (cdi.getProject().getAsse() == 1) {
					amountCoveredAxis1 += cdi.getAmountToCoverAdvanceStateAid();
				} else if (cdi.getProject().getAsse() == 2) {
					amountCoveredAxis2 += cdi.getAmountToCoverAdvanceStateAid();
				} else if (cdi.getProject().getAsse() == 3) {
					amountCoveredAxis3 += cdi.getAmountToCoverAdvanceStateAid();
				} else if (cdi.getProject().getAsse() == 4) {
					amountCoveredAxis4 += cdi.getAmountToCoverAdvanceStateAid();
				}
			}
		}

		this.listAdvances.get(0).setAmountCovered(amountCoveredAxis1);
		this.listAdvances.get(1).setAmountCovered(amountCoveredAxis2);
		this.listAdvances.get(2).setAmountCovered(amountCoveredAxis3);
		this.listAdvances.get(3).setAmountCovered(amountCoveredAxis4);
		this.listAdvances.get(4)
				.setAmountCovered(amountCoveredAxis1 + amountCoveredAxis2 + amountCoveredAxis3 + amountCoveredAxis4);
	}

	private double amountPaidForAxisValueComputation(String axis) throws PersistenceBeanException {
		double sum = 0d;
		List<AdditionalFesrInfo> fesrInfo = BeansFactory.AdditionalFESRInfo().LoadByAxis(axis);
		for (AdditionalFesrInfo fi : fesrInfo) {
			if (fi.getTransferType().getCode() == TransferTypes.AdvanceStateAidDeMinimis.getCode()
					&& fi.getTransferImport() != null) {
				sum += fi.getTransferImport();
			}
			/*if (fi.getTransferType().getCode() == TransferTypes.AdvanceStateAidExemptionScheme.getCode()
					&& fi.getTransferImport() != null) {
				sum += fi.getTransferImport();
			}*/
		}
		return sum;
	}

	private void amountPaidForAxisValueComputation() throws PersistenceBeanException {
		int i = 1;
		for (AdvancesIncludedInPaymentApplication a : this.listAdvances) {
			a.setAmountPaid(amountPaidForAxisValueComputation(String.valueOf(i)));
			i++;
			if (i == 5) {
				break;
			}
		}
		listAdvances.get(listAdvances.size() - 1).setAmountPaid(totalAmountPaid(listAdvances));
	}

	private double totalAmountPaid(List<AdvancesIncludedInPaymentApplication> list) {
		double sum = 0d;
		for (AdvancesIncludedInPaymentApplication adv : list) {
			if (adv.getAmountPaid() != null) {
				sum += adv.getAmountPaid();
			}
		}
		return sum;
	}

	private void amountEligibleOfExpenditure() throws HibernateException, PersistenceBeanException {
		for (ExpendituresOfPaymentApplication e : this.getExpenditures()) {
			for (DURCompilationWrapper dcw : this.getList()) {
				if (dcw.isSelected()) {
					List<CostDefinitions> cds = BeansFactory.DurCompilations().Load(dcw.getId()).getCostDefinitions();
					for (CostDefinitions cd : cds) {
						if (e.getAsse().equals(String.valueOf(cd.getProject().getAsse()))) {
							if (cd.getAcuCertification() != 0 && cd.getAcuCertification() != null && !cd.getAdditionalFinansing()) {
								e.setTotalAmountEligibleExpenditure((e.getTotalAmountEligibleExpenditure() != null
										? e.getTotalAmountEligibleExpenditure() : 0d) + cd.getAcuCertification());
								e.setTotalAmountPublicExpenditure((e.getTotalAmountPublicExpenditure() != null
										? e.getTotalAmountPublicExpenditure() : 0d) + cd.getAcuCheckPublicAmount());
							}
							/*if (cd.getCfCheck() != null) {
								e.setTotalAmountEligibleExpenditure((e.getTotalAmountEligibleExpenditure() != null
										? e.getTotalAmountEligibleExpenditure() : 0d) + cd.getCfCheck());
							} else if (cd.getCilCheck() != null) {
								e.setTotalAmountEligibleExpenditure((e.getTotalAmountEligibleExpenditure() != null
										? e.getTotalAmountEligibleExpenditure() : 0d) + cd.getCilCheck());
							}

							if (cd.getCfCheckPublicAmount() != null) {
								e.setTotalAmountPublicExpenditure((e.getTotalAmountPublicExpenditure() != null
										? e.getTotalAmountPublicExpenditure() : 0d) + cd.getCfCheckPublicAmount());
							} else if (cd.getCilCheckPublicAmount() != null) {
								e.setTotalAmountPublicExpenditure((e.getTotalAmountPublicExpenditure() != null
										? e.getTotalAmountPublicExpenditure() : 0d) + cd.getCilCheckPublicAmount());
							} */

						}
					}
				}
			}
		}

		Double totalEligible = 0d;
		Double totalPublic = 0d;
		ExpendituresOfPaymentApplication exTotal = null;
		for (ExpendituresOfPaymentApplication e : this.getExpenditures()) {
			if (!e.getAsse().equals(Utils.getString("total"))) { 
				totalEligible += (e.getTotalAmountEligibleExpenditure() != null ? e.getTotalAmountEligibleExpenditure()
						: 0d);
				totalPublic += (e.getTotalAmountPublicExpenditure() != null ? e.getTotalAmountPublicExpenditure() : 0d);
			}
			if (e.getAsse().equals(Utils.getString("total"))) {
				exTotal = e;
			}
		}
		boolean exTotalNull = false;
		if (exTotal == null) {
			exTotal = new ExpendituresOfPaymentApplication();
			exTotal.setAsse(Utils.getString("total"));
			exTotal.setTotalAmountEligibleExpenditure(0d);
			exTotal.setTotalAmountPublicExpenditure(0d);
			exTotalNull = true;
		}
		exTotal.setTotalAmountEligibleExpenditure(totalEligible);
		exTotal.setTotalAmountPublicExpenditure(totalPublic);
		if (exTotalNull) {
			this.getExpenditures().add(exTotal);
		}
	}

	private void setAdvancesColumns() throws PersistenceBeanException {
		amountPaidForAxisValueComputation();
		amountCoveredForAxisComputation();
		amountNotCoveredForAxisComputation();
	}

	private void initAdvances() throws PersistenceBeanException {
		listAdvances = new ArrayList<>();
		AdvancesIncludedInPaymentApplication a = null;
		for (int i = 0; i <= 3; i++) {
			a = new AdvancesIncludedInPaymentApplication();
			a.setAsse(String.valueOf(i + 1));
			listAdvances.add(i, a);
		}
		a = new AdvancesIncludedInPaymentApplication();
		a.setAsse("Totale");
		listAdvances.add(4, a);
	}

	private void initExpenditureOfPaymentApplication() throws PersistenceBeanException {

		this.setExpenditures(new ArrayList<ExpendituresOfPaymentApplication>());
		ExpendituresOfPaymentApplication e = null;
		for (int i = 0; i <= 3; i++) {
			e = new ExpendituresOfPaymentApplication();
			e.setAsse(String.valueOf(i + 1));
			e.setTotalAmountEligibleExpenditure(0d);
			e.setTotalAmountPublicExpenditure(0d);
			this.getExpenditures().add(i, e);
		}
		e = new ExpendituresOfPaymentApplication();
		e.setAsse(Utils.getString("total"));
		e.setTotalAmountEligibleExpenditure(0d);
		e.setTotalAmountPublicExpenditure(0d);
		this.getExpenditures().add(4, e);
	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub

	}

	private void showPaymentApplicationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (!(Boolean) this.getSession().get("paymentApplicationEdit")) {
			this.setShowButton(true);
			this.fillComboboxes();
			entityId = (Long) this.getSession().get("paymentApplicationEntityId");
			pa = BeansFactory.PaymentApplications().LoadById(entityId);
			this.fillDurListForShowPage();
			this.setRendered(true);
			setEntityForEditShowPage();
			this.setTypeEdit(false);
		}
	}

	private void newPaymentApplicationPage() throws PersistenceBeanException {
		try {
			this.fillComboboxes();
			this.setCompilationDate(new GregorianCalendar().getTime());
			this.setRendered(false);
			this.setAccountingPeriod();
			this.fillDurListForNewPage();
			this.setEdit(false);
			this.setShowButton(false);
			this.setAmountStateAidInPaymentApplication(amountStateAidInPaymentApplicationComputation());
			this.setAmountEligibleExpenses(amountEligibleExpensesComputation());
			this.setTotalAmountOfPublicExpenditure(totalAmountOfPublicExpenditureComputation());
			this.setTotalAmountOfCertifications(totalAmountOfCertificationComputation());
			this.setAdvancesColumns();
			this.amountEligibleOfExpenditure();
			// this.setTypeEdit(true);
		} catch (Exception ex) {
			log.error(ex);
		}
	}

	private Double totalAmountOfCertificationComputation() {
		try {
			Double result = 0d;
			// CostDefinitions cd = null;

			// se la domanda è in modifica, bisogna fare attenzione a non
			// considerare due volte lo stesso importo
			// si possono selezionare tutte le domande di pagamento presentate
			// precedenti alla data di creazione di quella attuale se è già
			// stata creata
			// dalle domande di pagamento precedenti si risale alla ddr

			// consideriamo tutte le ddr associate a domande di pagamento
			// presentate la cui data di presentazione è inferiore a quella
			// attuale
			List<DurCompilations> listDurCompilations = BeansFactory.DurCompilations().LoadAllWithPaymentApplication();

			Calendar from = new GregorianCalendar();
			from.setTime(this.getDateFrom());
			Calendar to = new GregorianCalendar();
			to.setTime(this.getDateTo());
			for (DurCompilations dc : listDurCompilations) {
				PaymentApplication pa = BeansFactory.PaymentApplications().LoadById(dc.getPaymentApplication().getId());
				if (this.getType().equals(String.valueOf(RequestType.IntermediateFinal.getId()))) {

					Calendar paDateFrom = new GregorianCalendar();
					paDateFrom.setTime(pa.getAccountingPeriodFrom());

					Calendar paDateTo = new GregorianCalendar();
					paDateTo.setTime(pa.getAccountingPeriodTo());

					if (!paDateFrom.equals(from) && !paDateTo.equals(to)) {
						if (pa.getSubmissionDate() != null
								&& pa.getSubmissionDate().before(new GregorianCalendar().getTime())) {
							List<CostDefinitions> listCd = dc.getCostDefinitions();
							for (CostDefinitions cd : listCd) {
								// cd =
								// BeansFactory.CostDefinitions().getByDurCompilationWithoutProject(dc.getId());
								result += (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
							}

						}
					}
				} else {
					if (pa.getSubmissionDate() != null
							&& pa.getSubmissionDate().before(new GregorianCalendar().getTime())) {
						List<CostDefinitions> listCd = dc.getCostDefinitions();
						for (CostDefinitions cd : listCd) {
							// cd =
							// BeansFactory.CostDefinitions().getByDurCompilationWithoutProject(dc.getId());
							result += (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
						}

					}
				}
			}

			if (this.getList() != null && !this.getList().isEmpty()) {
				for (DURCompilationWrapper dcw : this.getList()) {
					if (dcw.isSelected()) {
						DurCompilations dci = BeansFactory.DurCompilations().Load(dcw.getId());
						List<CostDefinitions> listCd = dci.getCostDefinitions();
						for (CostDefinitions cd : listCd) {
							result += (cd.getAcuCertification() != null ? cd.getAcuCertification() : 0d);
						}
					}
				}
			}
			return result;
		} catch (PersistenceBeanException e) {
			log.error(e.getMessage());
		}
		return 0d;
	}

	public void typeChange(ValueChangeEvent event) {
		this.setType(String.valueOf(event.getNewValue()));
		this.Page_Load();
	}

	private void setAccountingPeriod() {

		try {
			// verificare se nel sistema sono state prodotte altre domande di
			// pagamento con stato presentata
			PaymentApplication lastPA = BeansFactory.PaymentApplications().selectLastSubmittedPaymentApplication();

			Calendar now = new GregorianCalendar();
			Calendar june30 = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JUNE, 30);
			Calendar from = null, to = null;
			if (now.after(june30)) {
				from = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JULY, 1);
				to = new GregorianCalendar(now.get(Calendar.YEAR) + 1, Calendar.JUNE, 30);
			} else {

				from = new GregorianCalendar(now.get(Calendar.YEAR) - 1, Calendar.JULY, 1);
				to = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JUNE, 30);
			}
			this.setDateFrom(from.getTime());
			this.setDateTo(to.getTime());

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			this.setDateFromString(df.format(this.getDateFrom()));
			this.setDateToString(df.format(this.getDateTo()));
			this.setTypeEdit(true);

			// se nessuna domanda è stata prodotta allora va bene quanto di
			// seguito definito
			// if(lastPA == null){
			//
			// }else{

			// se domande di pagamento sono già state prodotte, allora
			// bisogna verificare in che periodo contabile è stata prodotta
			// l'ultima domanda di pagamento presentata
			if (lastPA != null) {
				Calendar lastPaDateFrom = new GregorianCalendar();
				lastPaDateFrom.setTime(lastPA.getAccountingPeriodFrom());
				Calendar lastPaDateTo = new GregorianCalendar();
				lastPaDateTo.setTime(lastPA.getAccountingPeriodTo());
				// se il periodo contabile è diverso da quello attuale, allora

				if (!lastPaDateFrom.equals(from) && !lastPaDateTo.equals(to)) {
					// bisogna verificare che l'ultima domanda abbia come tipo
					// "finale intermedia".
					if (!lastPA.getRequestType().getId().equals(RequestType.IntermediateFinal.getId())) {
						// Se l'ultima domanda ha periodo contabile diverso da
						// quello attuale e non ha il tipo "finale intermedia",
						// allora il tipo è preimpostato e deve essere "finale
						// intermedia"
						this.setType(String.valueOf(RequestType.IntermediateFinal.getId()));
						// e non deve essere modificabile e il periodo contabile
						// di riferimento è quello precedente
						this.setTypeEdit(false);
						this.setDateFrom(lastPA.getAccountingPeriodFrom());
						this.setDateTo(lastPA.getAccountingPeriodTo());
						// inoltre la lista delle ddr saranno tutte le ddr
						// inserite nelle domande presentate con tipo
						// "intermedie"
						// e quelle non ancora inserite in alcuna domanda di
						// pagamento
						// }else{
						// //Se il periodo contabile è diverso, ma l'ultima
						// domanda presenta lo stato "finale intermedia", allora
						// si può inserire nella domanda attuale il periodo
						// contabile attuale
					}
				}

			}
		} catch (PersistenceBeanException e) {
			log.error(e.getMessage());
		}

	}

	private void editPaymentApplicationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if ((Boolean) this.getSession().get("paymentApplicationEdit")) {
			this.fillComboboxes();
			entityId = (Long) this.getSession().get("paymentApplicationEntityId");
			pa = BeansFactory.PaymentApplications().LoadById(entityId);
			if (pa.getPaymentApplicationState().getCode().equals(PaymentApplicationStatusTypes.Submitted.getValue())) {
				this.getSession().put("paymentApplicationEdit", false);
				showPaymentApplicationPage();
				this.setCanDelete(true);
			} else {
				this.setShowButton(false);
				this.fillDurListForEditPage();
				this.setRendered(true);
				setEntityForEditShowPage();
				this.setAmountStateAidInPaymentApplication(amountStateAidInPaymentApplicationComputation());
				this.setAmountEligibleExpenses(amountEligibleExpensesComputation());
				this.setTotalAmountOfPublicExpenditure(totalAmountOfPublicExpenditureComputation());
				this.setAdvancesColumns();
				if (Boolean.TRUE.equals(this.isItemSelected()))
					this.amountEligibleOfExpenditure();
			}

		}
	}

	public void setEntityForEditShowPage() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getSession().get("paymentApplicationEntityId") != null) {
			entityId = (Long) this.getSession().get("paymentApplicationEntityId");
			this.setEdit(!(Boolean) this.getSession().get("paymentApplicationEdit"));
			this.setRendered(true);
			pa = BeansFactory.PaymentApplications().LoadById(entityId);
			this.setProtocolNumber(pa.getProtocolNumber());
			this.setCompilationDate(pa.getCompilationDate());
			this.setProtocolDate(pa.getProtocolDate());
			this.setAmountStateAidInPaymentApplication(pa.getAmountStateAidInPaymentApplication());
			this.setAmountEligibleExpenses(pa.getAmountEligibleExpenses());
			this.setTotalAmountOfPublicExpenditure(pa.getTotalAmountOfPublicExpenditure());
			this.setPaymentApplicationId(String.valueOf(pa.getId()));
			this.setTotalAmountOfCertifications(pa.getTotalAmountOfCertifications());
			try {
				List<AdvancesIncludedInPaymentApplication> ladvs = BeansFactory.AdvancesIncludedInPaymentApplications()
						.loadByPaymentApplicationId(entityId);
				setListAdvances(ladvs);

				if (this.getExpenditures() == null) {
					this.setExpenditures(
							BeansFactory.ExpendituresOfPaymentApplication().loadByPaymentApplicationId(entityId));
				}

				Double totalEligible = 0d;
				Double totalPublic = 0d;
				ExpendituresOfPaymentApplication exTotal = null;
				for (ExpendituresOfPaymentApplication e : this.getExpenditures()) {
					if (!e.getAsse().equals(Utils.getString("total"))) {
						totalEligible += (e.getTotalAmountEligibleExpenditure() != null
								? e.getTotalAmountEligibleExpenditure() : 0d);
						totalPublic += (e.getTotalAmountPublicExpenditure() != null
								? e.getTotalAmountPublicExpenditure() : 0d);
					}
					if (e.getAsse().equals(Utils.getString("total"))) {
						exTotal = e;
					}
				}

				boolean exTotalNull = false;
				if (exTotal == null) {
					exTotal = new ExpendituresOfPaymentApplication();
					exTotal.setAsse(Utils.getString("total"));
					exTotal.setTotalAmountEligibleExpenditure(0d);
					exTotal.setTotalAmountPublicExpenditure(0d);
					exTotalNull = true;
				}
				exTotal.setTotalAmountEligibleExpenditure(totalEligible);
				exTotal.setTotalAmountPublicExpenditure(totalPublic);

				if (exTotalNull) {
					this.getExpenditures().add(exTotal);
				}
				// setExpenditures(expenditures);

			} catch (Exception e) {
				log.error("errore db");
			}
			RequestTypes rt = pa.getRequestType();
			if (rt != null) {
				this.setType(String.valueOf(rt.getId()));
				this.setTypeEdit(false);
			} else {
				this.setTypeEdit(true);
			}
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			if (this.getDateFrom() == null) {
				this.setDateFrom(pa.getAccountingPeriodFrom());
				Calendar from = new GregorianCalendar();
				from.setTime(this.getDateFrom());
				this.setDateFromString(df.format(this.getDateFrom()));
			}

			if (this.getDateTo() == null) {
				this.setDateTo(pa.getAccountingPeriodTo());
				Calendar to = new GregorianCalendar();
				to.setTime(this.getDateTo());
				this.setDateToString(df.format(this.getDateTo()));
			}
		}
	}

	private void fillDurListForNewPage() throws PersistenceBeanException {
		List<DurCompilations> listDurs = null;
		List<Long> listIndices = initializeFillDurList();

		// in qualsiasi condizione, se la Domanda è nuova, allora vengono sempre
		// caricate tutte le ddr certificate e non inserite in altre domande
		listDurs = BeansFactory.DurCompilations().LoadByState(DurStateTypes.Certified, true);
		if (this.getType().equals(String.valueOf(RequestType.IntermediateFinal.getId()))) {
			List<PaymentApplication> listDurForIntermediateFinalState = BeansFactory.PaymentApplications()
					.loadAllSubmittedByAccountingPeriod(this.getDateFrom(), this.getDateTo());
			for (PaymentApplication pai : listDurForIntermediateFinalState) {
				List<PaymentApplicationDurCompilations> listpadcs = BeansFactory.PaymentApplicationDurCompilations()
						.getAllByPaymentApplication(pai.getId());
				for (PaymentApplicationDurCompilations padci : listpadcs) {
					listDurs.add(BeansFactory.DurCompilations().Load(padci.getDurCompilationsId()));
					listIndices.add(padci.getDurCompilationsId());
				}
			}

		}
		endFillDurList(listDurs, listIndices);
	}

	public void fillDurListForShowPage() throws PersistenceBeanException {
		List<DurCompilations> listDurs = null;
		List<Long> listIndices = initializeFillDurList();

		entityId = (Long) this.getSession().get("paymentApplicationEntityId");
		PaymentApplication ex = BeansFactory.PaymentApplications().LoadById(entityId);

		if (listDurs == null) {
			listDurs = new ArrayList<>();
		}

		List<PaymentApplicationDurCompilations> eddcs = BeansFactory.PaymentApplicationDurCompilations()
				.getAllByPaymentApplication(entityId);

		for (PaymentApplicationDurCompilations eddc : eddcs) {
			listDurs.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
		}
		for (DurCompilations dc : listDurs) {
			listIndices.add(dc.getId());
		}

		endFillDurList(listDurs, listIndices);

	}

	public void selectItem() throws NumberFormatException, HibernateException, PersistenceBeanException {
		List<Long> listIndices = new ArrayList<Long>();
		this.setSelectAll(false);
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				listIndices.add(dcw.getId());
			}
		}
		this.getViewState().put("paymentApplicationListDDR", listIndices);

		try {
			// this.Page_Load();
			this.setExpenditures(new ArrayList<ExpendituresOfPaymentApplication>());
			initExpenditureOfPaymentApplication();
			// this.amountEligibleOfExpenditure();
			this.setItemSelected(true);
			this.Page_Load();
			this.setTotalAmountOfCertifications(recomputeTotaleAmountOfCertification());
		} catch (NullPointerException e) {
			log.error("error selectItem");
		}
	}

	private Double recomputeTotaleAmountOfCertification() throws PersistenceBeanException {
		List<DurCompilations> durOfOltPaymentApplication = new ArrayList<>();
		if (this.getType().equals(String.valueOf(RequestType.IntermediateFinal.getId()))) {
			List<PaymentApplication> listDurForIntermediateFinalState = BeansFactory.PaymentApplications()
					.loadAllSubmittedByAccountingPeriod(this.getDateFrom(), this.getDateTo());
			for (PaymentApplication pai : listDurForIntermediateFinalState) {
				List<PaymentApplicationDurCompilations> listpadcs = BeansFactory.PaymentApplicationDurCompilations()
						.getAllByPaymentApplication(pai.getId());
				for (PaymentApplicationDurCompilations padci : listpadcs) {
					durOfOltPaymentApplication.add(BeansFactory.DurCompilations().Load(padci.getDurCompilationsId()));
				}
			}

		}

		Double result = 0d;
		for (DURCompilationWrapper dcw : this.getList()) {
			boolean founded = false;
			if (dcw.isSelected()) {
				for (DurCompilations dcOld : durOfOltPaymentApplication) {
					if (dcOld.getId().equals(dcw.getId())) {
						founded = true;
					}
				}
				if (!founded) {
					List<CostDefinitions> listCd = BeansFactory.CostDefinitions()
							.getByDurCompilationWithoutProject(dcw.getId());
					for (CostDefinitions cd : listCd) {
						result += cd.getAcuCertification();
					}
				}
			}
		}
		List<PaymentApplication> listPaSubmitted = BeansFactory.PaymentApplications()
				.loadAllPreviousSubmittedPaymentApplicationBeforeDate(new GregorianCalendar().getTime());
		if (listPaSubmitted != null && !listPaSubmitted.isEmpty() && listPaSubmitted.size() > 0) {
			result += listPaSubmitted.get(0).getTotalAmountOfCertifications();
		}
		return result;
	}

	private void fillDurListForEditPage() throws PersistenceBeanException {
		List<Long> listIndices = initializeFillDurList();

		List<DurCompilations> listDursSupport = new ArrayList<>();
		List<DurCompilations> listDursED = null;
		List<DurCompilations> listDurs = BeansFactory.DurCompilations().LoadByState(DurStateTypes.Certified, true);

		entityId = (Long) this.getSession().get("paymentApplicationEntityId");
		PaymentApplication pa = BeansFactory.PaymentApplications().LoadById(entityId);

		if (listDursED == null) {
			listDursED = new ArrayList<>();
		}

		List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
				.getAllByPaymentApplication(entityId);
		for (PaymentApplicationDurCompilations padc : padcs) {
			listDursED.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
		}

		listDursSupport.addAll(listDursED);
		listDursSupport.addAll(listDurs);

		boolean bFoundSelected = false;

		for (DurCompilations dc : listDursSupport) {
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);
			if (listIndices.contains(dcw.getId())) {
				bFoundSelected = true;
				dcw.setSelected(true);
			} else {
				dcw.setSelected(false);
			}
			for (DurCompilations dcs : listDursED) {
				if (dcs.getId().equals(dc.getId())) {
					dcw.setSelected(true);
				}
			}
			this.getList().add(dcw);

		}
		this.setShowSelectAll(bFoundSelected);
		this.ReRenderScroll();
	}

	public void selectAll() throws NumberFormatException, HibernateException, PersistenceBeanException,
			NullPointerException, IOException {
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAll()) {
			for (DURCompilationWrapper dcw : this.getList()) {
				listIndices.add(dcw.getId());
			}
		}

		this.getViewState().put("paymentApplicationListDDR", listIndices);

		this.Page_Load();
	}

	private List<Long> initializeFillDurList() {
		this.setList(new ArrayList<DURCompilationWrapper>());

		this.setShowSelectAll(false);

		List<Long> listIndices = new ArrayList<Long>();

		if (this.getViewState().get("paymentApplicationListDDR") != null) {
			listIndices = (List<Long>) this.getViewState().get("paymentApplicationListDDR");
		}

		return listIndices;
	}

	private void endFillDurList(List<DurCompilations> listDurs, List<Long> listIndices)
			throws PersistenceBeanException {
		boolean bFoundSelected = false;
		for (DurCompilations dc : listDurs) {
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);

			if (listIndices.contains(dcw.getId())) {
				bFoundSelected = true;
				dcw.setSelected(true);
			} else {
				dcw.setSelected(false);
			}
			this.getList().add(dcw);
		}
		this.setShowSelectAll(bFoundSelected);
		this.ReRenderScroll();
	}

	private void fillComboboxes() throws HibernateException, PersistenceBeanException {
		this.fillTypes();
	}

	private void fillTypes() throws HibernateException, PersistenceBeanException {
		this.listTypes = new ArrayList<>();
		try {
			setRequestTypes(BeansFactory.RequestTypes().Load());
		} catch (PersistenceBeanException e) {
			log.error("errore");
		}
		SelectItem item = new SelectItem();
		// item.setValue("0");
		// item.setLabel("Not selected");
		this.listTypes.add(SelectItemHelper.getFirst());
		for (RequestTypes c : this.getRequestTypes()) {
			item = new SelectItem();
			item.setValue(String.valueOf(c.getId()));
			item.setLabel(c.getValue());
			this.listTypes.add(item);
		}
		if (this.getType() == null) {
			if (!this.listTypes.isEmpty()) {
				this.setType(this.listTypes.get(0).getValue().toString());

			}
		}

	}

	@Override
	public void addEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void editEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub

	}

	public void setType(String type) {
		this.getSession().put("type", type);
	}

	public String getType() {
		if (this.getSession().get("type") == null) {
			return null;
		}

		return this.getSession().get("type").toString();
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}

	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Date getProtocolDate() {
		return protocolDate;
	}

	public void setProtocolDate(Date protocolDate) {
		this.protocolDate = protocolDate;
	}

	public Double getAmountStateAidInPaymentApplication() {
		return amountStateAidInPaymentApplication;
	}

	public void setAmountStateAidInPaymentApplication(Double amountStateAidInPaymentApplication) {
		this.amountStateAidInPaymentApplication = amountStateAidInPaymentApplication;
	}

	public Double getAmountEligibleExpenses() {
		return amountEligibleExpenses;
	}

	public void setAmountEligibleExpenses(Double amountEligibleExpenses) {
		this.amountEligibleExpenses = amountEligibleExpenses;
	}

	public Double getTotalAmountOfPublicExpenditure() {
		return totalAmountOfPublicExpenditure;
	}

	public void setTotalAmountOfPublicExpenditure(Double totalAmountOfPublicExpenditure) {
		this.totalAmountOfPublicExpenditure = totalAmountOfPublicExpenditure;
	}

	public List<RequestTypes> getRequestTypes() {
		return requestTypes;
	}

	public void setRequestTypes(List<RequestTypes> requestTypes) {
		this.requestTypes = requestTypes;
	}

	public List<SelectItem> getListTypes() {
		return listTypes;
	}

	public void setListTypes(List<SelectItem> listTypes) {
		this.listTypes = listTypes;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public boolean isShowSelectAll() {
		return showSelectAll;
	}

	public void setShowSelectAll(boolean showSelectAll) {
		this.showSelectAll = showSelectAll;
	}

	public PaymentApplication getPa() {
		return pa;
	}

	public void setPa(PaymentApplication pa) {
		this.pa = pa;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public String getPaymentApplicationId() {
		return paymentApplicationId;
	}

	public void setPaymentApplicationId(String paymentApplicationId) {
		this.paymentApplicationId = paymentApplicationId;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public List<DurCompilations> getListSelectedDur() {
		return listSelectedDur;
	}

	public void setListSelectedDur(List<DurCompilations> listSelectedDur) {
		this.listSelectedDur = listSelectedDur;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public void setSaveFlag(int saveFlag) {
		this.getViewState().put("saveFlag", saveFlag);
	}

	public int getSaveFlag() {
		return this.getViewState().get("saveFlag") == null ? 0 : (Integer) this.getViewState().get("saveFlag");
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public Long getEntityEditId() {
		return entityEditId;
	}

	public void setEntityEditId(Long entityEditId) {
		this.entityEditId = entityEditId;
	}

	public boolean isValidationFailed() {
		return validationFailed;
	}

	public void setValidationFailed(boolean validationFailed) {
		this.validationFailed = validationFailed;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<AdvancesIncludedInPaymentApplication> getListAdvances() {
		return listAdvances;
	}

	public void setListAdvances(List<AdvancesIncludedInPaymentApplication> listAdvances) {
		this.listAdvances = listAdvances;
	}

	public String getDateFromString() {
		// return dateFromString;
		return (String) this.getViewState().get("PAdateFromString");
	}

	public void setDateFromString(String dateFromString) {
		// this.dateFromString = dateFromString;
		this.getViewState().put("PAdateFromString", dateFromString);
	}

	public Date getDateFrom() {
		// return dateFrom;
		return (Date) this.getViewState().get("PAdateFrom");
	}

	public void setDateFrom(Date dateFrom) {
		// this.dateFrom = dateFrom;
		this.getViewState().put("PAdateFrom", dateFrom);
	}

	public Date getDateTo() {
		return (Date) this.getViewState().get("PAdateTo");
		// return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.getViewState().put("PAdateTo", dateTo);
		// this.dateTo = dateTo;
	}

	public String getDateToString() {
		// return dateToString;
		return (String) this.getViewState().get("PAdateToString");
	}

	public void setDateToString(String dateToString) {
		// this.dateToString = dateToString;
		this.getViewState().put("PAdateToString", dateToString);
	}

	public Double getTotalAmountOfCertifications() {
		return totalAmountOfCertifications;
	}

	public void setTotalAmountOfCertifications(Double totalAmountOfCertifications) {
		this.totalAmountOfCertifications = totalAmountOfCertifications;
	}

	public boolean isTypeEdit() {
		return typeEdit;
	}

	public void setTypeEdit(boolean typeEdit) {
		this.typeEdit = typeEdit;
	}

	public List<ExpendituresOfPaymentApplication> getExpenditures() {
		return (List<ExpendituresOfPaymentApplication>) this.getViewState().get("expenditures");

	}

	public void setExpenditures(List<ExpendituresOfPaymentApplication> expenditures) {
		// this.expenditures = expenditures;
		this.getViewState().put("expenditures", expenditures);
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

}
