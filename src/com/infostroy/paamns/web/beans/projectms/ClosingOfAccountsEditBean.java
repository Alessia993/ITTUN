package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CostDefinitionSuspendStatus;
import com.infostroy.paamns.common.enums.ExpenditureDeclarationStatusTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PaymentApplicationStatusTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AmountsToBeRecoveredAtTheClose;
import com.infostroy.paamns.persistence.beans.entities.domain.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod;
import com.infostroy.paamns.persistence.beans.entities.domain.ClosingAccounts;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.RegistredAmount;
import com.infostroy.paamns.persistence.beans.entities.domain.UnrecoverableAmountsAtTheClose;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.web.beans.EntityEditBean;

public class ClosingOfAccountsEditBean extends EntityEditBean<ClosingAccounts> {

	private boolean rendered;

	private String closingOfAccountsId;

	private String description;

	private boolean edit;

	private Date referencePeriod;

	private Date compilationDate;

	private boolean finalExpendsesForProjAbove1Mil;

	private String totalAmountOfEligibleExpenses;

	private String totalAmountOfPublicExpenditure;

	private String totalAmountOfTransfersLinkedToPaymentsToEligibleEx;

	private String totalAmountOfEligibleExpenditureWithdrawn;

	private String totalAmountOfPublicExpenditureWithdrawn;

	private String totalAmountOfEligibleExpenditureRecovered;

	private String totalAmountOfPublicExpenditureRecovered;

	private String totalAmountOfEligibleExpenditureToBeRecovered;

	private String totalAmountOfPublicExpenditureToBeRecovered;

	private String totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;

	private String totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;

	private String totalAmountOfEligibleExpenditureThatCanNotBeRecovered;

	private String totalAmountOfPublicExpenditureThatCanNotBeRecovered;

	private List<RegistredAmount> registredAmountList;

	private List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList;

	private List<AmountsToBeRecoveredAtTheClose> amountsToBeRecoveredAtTheCloseList;

	private List<UnrecoverableAmountsAtTheClose> unrecoverableAmountsAtTheCloseList;

	private boolean showButton;

	private boolean submitted;

	private ClosingAccounts ca;

	private Long state_id;

	private Long entityId;

	private boolean canDelete;

	private boolean validationFailed;

	private List<PaymentApplication> paList;

	public void exportRegistredAmount() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(ca.getRegistredAmountList(), ExportPlaces.RegistredAmount);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("closingOfAccountsEditRegistredAmount") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList(),
					ExportPlaces.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("closingOfAccountsEditAmountsWithdrawnAndRecoveredName") + ".xls", is,
					data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportUnrecoverableAmountsAtTheClose() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getUnrecoverableAmountsAtTheCloseList(),
					ExportPlaces.UnrecoverableAmountsAtTheClose);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("closingOfAccountsEditUnrecoverableAmountsAtTheCloseName") + ".xls", is,
					data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportAmountsToBeRecoveredAtTheClose() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getAmountsToBeRecoveredAtTheCloseList(),
					ExportPlaces.AmountsToBeRecoveredAtTheClose);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("closingOfAccountsEditAmountsToBeRecoveredAtTheClose") + ".xls", is,
					data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		ClosingAccounts entityToSave = null;
		if (Boolean.valueOf(this.getSession().get("closingAccountsNew").toString())) {
			entityToSave = new ClosingAccounts();
			entityToSave.setDeleted(false);
			entityToSave.setCreateDate(new GregorianCalendar().getTime());
		} else {
			entityToSave = ca;
		}
		if (this.getCompilationDate() != null) {
			entityToSave.setCompilationDate(this.getCompilationDate());
		}
		if (this.getReferencePeriod() != null) {
			entityToSave.setReferencePeriod(this.getReferencePeriod());
		}
		if (this.getDescription() != null && !this.getDescription().trim().isEmpty()) {
			entityToSave.setDescription(this.getDescription());
		}
		entityToSave.setFinalExpendsesForProjAbove1Mil(this.isFinalExpendsesForProjAbove1Mil());

		if (this.getTotalAmountOfEligibleExpenses() != null
				&& !this.getTotalAmountOfEligibleExpenses().trim().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenses(Double.valueOf(this.getTotalAmountOfEligibleExpenses()));
		}
		if (this.getTotalAmountOfPublicExpenditure() != null && !this.getTotalAmountOfPublicExpenditure().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditure(Double.valueOf(this.getTotalAmountOfPublicExpenditure()));
		}
		if (this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx() != null
				&& !this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx().equals("")) {
			entityToSave.setTotalAmountOfTransfersLinkedToPaymentsToEligibleEx(
					Double.valueOf(this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx()));
		}
		if (this.getTotalAmountOfEligibleExpenditureWithdrawn() != null
				&& !this.getTotalAmountOfEligibleExpenditureWithdrawn().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenditureWithdrawn(
					Double.valueOf(this.getTotalAmountOfEligibleExpenditureWithdrawn()));
		}
		if (this.getTotalAmountOfPublicExpenditureWithdrawn() != null
				&& !this.getTotalAmountOfPublicExpenditureWithdrawn().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditureWithdrawn(
					Double.valueOf(this.getTotalAmountOfPublicExpenditureWithdrawn()));
		}
		if (this.getTotalAmountOfEligibleExpenditureRecovered() != null
				&& !this.getTotalAmountOfEligibleExpenditureRecovered().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenditureRecovered(
					Double.valueOf(this.getTotalAmountOfEligibleExpenditureRecovered()));
		}
		if (this.getTotalAmountOfPublicExpenditureRecovered() != null
				&& !this.getTotalAmountOfPublicExpenditureRecovered().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditureRecovered(
					Double.valueOf(this.getTotalAmountOfPublicExpenditureRecovered()));
		}
		if (this.getTotalAmountOfEligibleExpenditureToBeRecovered() != null
				&& !this.getTotalAmountOfEligibleExpenditureToBeRecovered().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenditureToBeRecovered(
					Double.valueOf(this.getTotalAmountOfEligibleExpenditureToBeRecovered()));
		}
		if (this.getTotalAmountOfPublicExpenditureToBeRecovered() != null
				&& !this.getTotalAmountOfPublicExpenditureToBeRecovered().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditureToBeRecovered(
					Double.valueOf(this.getTotalAmountOfPublicExpenditureToBeRecovered()));
		}
		if (this.getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod() != null
				&& !this.getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod(
					Double.valueOf(this.getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod()));
		}
		if (this.getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod() != null
				&& !this.getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod(
					Double.valueOf(this.getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod()));
		}
		if (this.getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered() != null
				&& !this.getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered().equals("")) {
			entityToSave.setTotalAmountOfEligibleExpenditureThatCanNotBeRecovered(
					Double.valueOf(this.getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered()));
		}
		if (this.getTotalAmountOfPublicExpenditureThatCanNotBeRecovered() != null
				&& !this.getTotalAmountOfPublicExpenditureThatCanNotBeRecovered().equals("")) {
			entityToSave.setTotalAmountOfPublicExpenditureThatCanNotBeRecovered(
					Double.valueOf(this.getTotalAmountOfPublicExpenditureThatCanNotBeRecovered()));
		}

		if (this.getState_id() == null) {
			ExpenditureDeclarationStates eds = BeansFactory.ExpenditureDeclarationStates()
					.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Generated.getValue());
			setState_id(eds.getId());
		}
		if (this.getState_id() != null) {
			ExpenditureDeclarationStates es = new ExpenditureDeclarationStates();
			es.setId(this.getState_id());
			entityToSave.setState(es);
		}
		ClosingAccounts savedEntity = (ClosingAccounts) PersistenceSessionManager.getBean().getSession()
				.merge(entityToSave);

		savedEntity.setUnrecoverableAmountsAtTheClose(new ArrayList<UnrecoverableAmountsAtTheClose>());
		for (UnrecoverableAmountsAtTheClose ua : this.getUnrecoverableAmountsAtTheCloseList()) {
			if (ua.getCreateDate() == null) {
				ua.setCreateDate(new GregorianCalendar().getTime());
			}
			ua.setClosingOfAccounts(savedEntity);
			ua = (UnrecoverableAmountsAtTheClose) PersistenceSessionManager.getBean().getSession().merge(ua);
			savedEntity.getUnrecoverableAmountsAtTheClose().add(ua);
		}

		savedEntity.setRegistredAmountList(new ArrayList<RegistredAmount>());
		for (RegistredAmount ra : this.getRegistredAmountList()) {
			if (ra.getCreateDate() == null) {
				ra.setCreateDate(new GregorianCalendar().getTime());
			}
			ra.setClosingOfAccounts(savedEntity);
			ra = (RegistredAmount) PersistenceSessionManager.getBean().getSession().merge(ra);
			savedEntity.getRegistredAmountList().add(ra);
		}
		savedEntity.setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod(
				new ArrayList<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod>());
		for (AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod awrd : this
				.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList()) {
			if (awrd.getCreateDate() == null) {
				awrd.setCreateDate(new GregorianCalendar().getTime());
			}
			awrd.setClosingOfAccounts(savedEntity);
			awrd = (AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod) PersistenceSessionManager.getBean()
					.getSession().merge(awrd);
			savedEntity.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod().add(awrd);
		}
		savedEntity.setAmountsToBeRecoveredAtTheClose(new ArrayList<AmountsToBeRecoveredAtTheClose>());
		for (AmountsToBeRecoveredAtTheClose ra : this.getAmountsToBeRecoveredAtTheCloseList()) {
			if (ra.getCreateDate() == null) {
				ra.setCreateDate(new GregorianCalendar().getTime());
			}
			ra.setClosingOfAccounts(savedEntity);
			ra = (AmountsToBeRecoveredAtTheClose) PersistenceSessionManager.getBean().getSession().merge(ra);
			savedEntity.getAmountsToBeRecoveredAtTheClose().add(ra);
		}

		PersistenceSessionManager.getBean().getSession().update(savedEntity);

		// entityToSave.setRegistredAmountList(this.getRegistredAmountList());
		// entityToSave.setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod(
		// this.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList());
		// entityToSave.setAmountsToBeRecoveredAtTheClose(this.getAmountsToBeRecoveredAtTheCloseList());
		// entityToSave.setUnrecoverableAmountsAtTheClose(this.getUnrecoverableAmountsAtTheCloseList());

	}

	public void temporarySave() {
		this.setSubmitted(false);
		try {
			ExpenditureDeclarationStates eds;
			eds = BeansFactory.ExpenditureDeclarationStates()
					.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Generated.getValue());
			setState_id(eds.getId());
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		// generateStatus();
		this.Page_Save();
	}

	public void definitiveSave() {
		if (validateFieldsToGenerateStatus()) {
			return;
		}
		ExpenditureDeclarationStates eds;
		try {
			eds = BeansFactory.ExpenditureDeclarationStates()
					.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());
			setState_id(eds.getId());
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		// generateStatus();

		this.Page_Save();
	}

	@Override
	public void GoBack() {
		if ((Boolean) this.getSession().get("closingAccountsNew")) {
			this.getSession().remove("closingAccountsNew");
		}
		if (this.getSession().get("closingAccountsEntityId") != null) {
			this.getSession().remove("closingAccountsEntityId");
			this.getSession().remove("closingAccountsEdit");
		}
		// this.setList(null);
		this.goTo(PagesTypes.CLOSINGACCOUNTSLIST);
	}

	private boolean validateFieldsToGenerateStatus() {
		this.validationFailed = false;
		if (this.getCompilationDate() == null) {

		}
		checkDate("EditForm:tab1:compilationDate", this.getCompilationDate());
		checkEmpty("EditForm:tab1:closingOfAccountsDescription", this.getDescription());
		checkEmpty("EditForm:tab1:totalAmountOfEligibleExpenses", this.getTotalAmountOfEligibleExpenses());
		checkEmpty("EditForm:tab1:totalAmountOfPublicExpenditure", this.getTotalAmountOfPublicExpenditure());
		checkEmpty("EditForm:tab1:totalAmountOfTransfersLinkedToPaymentsToEligibleEx",
				this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx());
		checkEmpty("EditForm:tab1:totalAmountOfEligibleExpenditureWithdrawn",
				this.getTotalAmountOfEligibleExpenditureWithdrawn());
		checkEmpty("EditForm:tab1:totalAmountOfPublicExpenditureWithdrawn",
				this.getTotalAmountOfPublicExpenditureWithdrawn());
		checkEmpty("EditForm:tab1:totalAmountOfEligibleExpenditureRecovered",
				this.getTotalAmountOfEligibleExpenditureRecovered());
		checkEmpty("EditForm:tab1:totalAmountOfPublicExpenditureRecovered",
				this.getTotalAmountOfPublicExpenditureRecovered());
		checkEmpty("EditForm:tab1:totalAmountOfEligibleExpenditureToBeRecovered",
				this.getTotalAmountOfEligibleExpenditureToBeRecovered());
		checkEmpty("EditForm:tab1:totalAmountOfPublicExpenditureToBeRecovered",
				this.getTotalAmountOfPublicExpenditureToBeRecovered());

		return this.validationFailed;
	}

	private boolean checkDate(String id, Date date) {
		if (date == null) {
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

	private void generateStatus() {
		ExpenditureDeclarationStates eds = null;
		try {
			boolean statusValidated = true;
			if (this.getCompilationDate() == null) {
				statusValidated = false;
			}
			if (this.getDescription() == null || this.getDescription().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfEligibleExpenses() == null
					|| this.getTotalAmountOfEligibleExpenses().trim().equals("")
					|| this.getTotalAmountOfEligibleExpenses().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfPublicExpenditure() == null
					|| this.getTotalAmountOfPublicExpenditure().trim().equals("")
					|| this.getTotalAmountOfPublicExpenditure().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx() == null
					|| this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx().trim().equals("")
					|| this.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfEligibleExpenditureWithdrawn() == null
					|| this.getTotalAmountOfEligibleExpenditureWithdrawn().trim().equals("")
					|| this.getTotalAmountOfEligibleExpenditureWithdrawn().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfPublicExpenditureWithdrawn() == null
					|| this.getTotalAmountOfPublicExpenditureWithdrawn().trim().equals("")
					|| this.getTotalAmountOfPublicExpenditureWithdrawn().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfEligibleExpenditureRecovered() == null
					|| this.getTotalAmountOfEligibleExpenditureRecovered().trim().equals("")
					|| this.getTotalAmountOfEligibleExpenditureRecovered().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfPublicExpenditureRecovered() == null
					|| this.getTotalAmountOfPublicExpenditureRecovered().trim().equals("")
					|| this.getTotalAmountOfPublicExpenditureRecovered().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfEligibleExpenditureToBeRecovered() == null
					|| this.getTotalAmountOfEligibleExpenditureToBeRecovered().trim().equals("")
					|| this.getTotalAmountOfEligibleExpenditureToBeRecovered().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getTotalAmountOfPublicExpenditureToBeRecovered() == null
					|| this.getTotalAmountOfPublicExpenditureToBeRecovered().trim().equals("")
					|| this.getTotalAmountOfPublicExpenditureToBeRecovered().trim().isEmpty()) {
				statusValidated = false;
			}

			if (statusValidated) {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());
			} else {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Generated.getValue());
			}
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		setState_id(eds.getId());
	}

	private List<PaymentApplication> selectSubmittedPaymentApplications() throws PersistenceBeanException {
		List<ExpenditureDeclarationStates> exStatesList = BeansFactory.ExpenditureDeclarationStates().Load();
		Long submittedId = 0L;
		for (ExpenditureDeclarationStates es : exStatesList) {
			if (es.getCode().equals(ExpenditureDeclarationStatusTypes.Submitted.getValue())) {
				submittedId = es.getId();
				break;
			}
		}
		List<PaymentApplication> list = BeansFactory.PaymentApplications().LoadByState(submittedId);
		setPaList(list);
		return list;

	}

	private double totalAmountOfEligibleExpensesComputation() throws PersistenceBeanException {
		List<PaymentApplication> paList = getPaList();// selectSubmittedPaymentApplications();
		double sum = 0d;
		for (PaymentApplication pa : paList) {
			if (pa.getAmountEligibleExpenses() != null) {
				sum += pa.getAmountEligibleExpenses();
			}
		}
		return sum;
	}

	private double totalAmountOfPublicExpenditureComputation() throws PersistenceBeanException {
		List<PaymentApplication> paList = getPaList();// selectSubmittedPaymentApplications();
		double sum = 0d;
		for (PaymentApplication pa : paList) {
			if (pa.getTotalAmountOfPublicExpenditure() != null) {
				sum += pa.getTotalAmountOfPublicExpenditure();
			}
		}
		return sum;
	}

	private double totalAmountOfTransfersLinkedToPaymentsToEligibleExComputation() throws PersistenceBeanException {
		List<AdditionalFesrInfo> fesrInfo = BeansFactory.AdditionalFESRInfo().Load();
		double sum = 0d;
		for (AdditionalFesrInfo afi : fesrInfo) {
			if (afi.getTransferImport() != null) {
				sum += afi.getTransferImport();
			}
		}
		return sum;
	}

	// Importo totale spesa ammissibile ritirata
	private double totalAmountOfEligibleExpenditureWithdrawnComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().GetAllWithSuspensionStatus();
		// for (CostDefinitions cd : cdList) {
		// if(cd.getTotAmountEliglExpRetired()!=null){
		// sum+=cd.getTotAmountEliglExpRetired();
		// }
		// }

		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
			// listDC.addAll(pa.getDurCompilations());
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getTotAmountEliglExpRetired() != null) {
				sum += cd.getTotAmountEliglExpRetired();
			}
		}
		return sum;
	}

	// Importo totale spesa pubblica ritirata
	private double totalAmountOfPublicExpenditureWithdrawnComputation() throws PersistenceBeanException {
		double sum = 0d;
		List<CostDefinitions> cdList = BeansFactory.CostDefinitions().GetAllWithSuspensionStatus();
		// for (CostDefinitions cd : cdList) {
		// if(cd.getTotAmountPublExpRetired()!=null){
		// sum+=cd.getTotAmountPublExpRetired();
		// }
		// }
		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getTotAmountPublExpRetired() != null) {
				sum += cd.getTotAmountPublExpRetired();
			}
		}
		return sum;
	}

	// importo totale spesa ammissibile recuperata
	private double totalAmountOfEligibleExpenditureRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		// for (CostDefinitions cd : cdList) {
		//
		// if(cd.getDurTotEligibleExpensesPublicSupportReimbursed()!=null){
		// sum +=cd.getDurTotEligibleExpensesPublicSupportReimbursed();
		// }
		// }
		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getDurTotEligibleExpensesPublicSupportReimbursed() != null) {
				sum += cd.getDurTotEligibleExpensesPublicSupportReimbursed();
			}
		}
		return sum;
	}

	// Importo totale spesa pubblica recuperata
	private double totalAmountOfPublicExpenditureRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		// for (CostDefinitions cd : cdList) {
		// if(cd.getDurAmountPublicSupportReimbursed()!=null){
		// sum +=cd.getDurAmountPublicSupportReimbursed();
		// }
		// }

		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getDurAmountPublicSupportReimbursed() != null) {
				sum += cd.getDurAmountPublicSupportReimbursed();
			}
		}
		return sum;
	}

	// Importo totale spesa ammissibile da recuperare
	private double totalAmountOfEligibleExpenditureToBeRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getDurTotalEligibleCost() != null) {
				sum += cd.getDurTotalEligibleCost();
			}
		}
		// for (CostDefinitions cd : cdList) {
		// if(cd.getDurTotalEligibleCost()!=null){
		// sum +=cd.getDurTotalEligibleCost();
		// }
		// }
		return sum;
	}

	// Importo totale spesa pubblica da recuperare
	private double totalAmountOfPublicExpenditureToBeRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getDurAmountOfPublicSupport() != null) {
				sum += cd.getDurAmountOfPublicSupport();
			}
		}

		// for (CostDefinitions cd : cdList) {
		// if(cd.getDurAmountOfPublicSupport()!=null){
		// sum +=cd.getDurAmountOfPublicSupport();
		// }
		// }
		return sum;
	}

	private double totalAmountOfEligibleExpenditureRecoveredInAccountingPeriodComputation()
			throws PersistenceBeanException {
		return totalAmountOfEligibleExpenditureRecoveredComputation();
	}

	private double totalAmountOfPublicExpenditureRecoveredInAccountingPeriodComputation()
			throws PersistenceBeanException {
		return totalAmountOfPublicExpenditureRecoveredComputation();
	}

	// Importo totale spesa ammissibile non recuperabile
	private double totalAmountOfEligibleExpenditureThatCanNotBeRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		// for (CostDefinitions cd : cdList) {
		//
		// if(cd.getTotEligibleExpensesPublicSupportNonRecoverable()!=null){
		// sum += cd.getTotEligibleExpensesPublicSupportNonRecoverable();
		// }
		// }

		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getTotEligibleExpensesPublicSupportNonRecoverable() != null) {
				sum += cd.getTotEligibleExpensesPublicSupportNonRecoverable();
			}
		}
		return sum;
	}

	// Importo totale spesa pubblica non recuperabile
	private double totalAmountOfPublicExpenditureThatCanNotBeRecoveredComputation() throws PersistenceBeanException {
		double sum = 0d;
		// List<CostDefinitions> cdList =
		// BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions();
		// for (CostDefinitions cd : cdList) {
		// if(cd.getDurRecoverySustainImport()!=null){
		// sum += cd.getDurRecoverySustainImport();
		// }
		// }
		List<PaymentApplication> list = selectSubmittedPaymentApplications();
		List<DurCompilations> listDC = new ArrayList<>();
		for (PaymentApplication pa : list) {
//			listDC.addAll(pa.getDurCompilations());
			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				listDC.add(BeansFactory.DurCompilations().Load(padc.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCD = new ArrayList<>();
		for (DurCompilations dc : listDC) {
			listCD.addAll(dc.getCostDefinitions());
		}
		for (CostDefinitions cd : listCD) {
			if (cd.getAguCertDate() != null && cd.getDurRecoverySustainImport() != null) {
				sum += cd.getDurRecoverySustainImport();
			}
		}
		return sum;
	}

	private void setFieldsFunctionality() {
		try {
			this.selectSubmittedPaymentApplications();
			this.setTotalAmountOfEligibleExpenses(String.valueOf(totalAmountOfEligibleExpensesComputation()));
			this.setTotalAmountOfPublicExpenditure(String.valueOf(totalAmountOfPublicExpenditureComputation()));
			this.setTotalAmountOfTransfersLinkedToPaymentsToEligibleEx(
					String.valueOf(totalAmountOfTransfersLinkedToPaymentsToEligibleExComputation()));
			this.setTotalAmountOfEligibleExpenditureWithdrawn(
					String.valueOf(totalAmountOfEligibleExpenditureWithdrawnComputation()));
			this.setTotalAmountOfPublicExpenditureWithdrawn(
					String.valueOf(totalAmountOfPublicExpenditureWithdrawnComputation()));
			this.setTotalAmountOfEligibleExpenditureRecovered(
					String.valueOf(totalAmountOfEligibleExpenditureRecoveredComputation()));
			this.setTotalAmountOfPublicExpenditureRecovered(
					String.valueOf(totalAmountOfPublicExpenditureRecoveredComputation()));
			this.setTotalAmountOfEligibleExpenditureToBeRecovered(
					String.valueOf(totalAmountOfEligibleExpenditureToBeRecoveredComputation()));
			this.setTotalAmountOfPublicExpenditureToBeRecovered(
					String.valueOf(totalAmountOfPublicExpenditureToBeRecoveredComputation()));
			this.setTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod(
					String.valueOf(totalAmountOfEligibleExpenditureRecoveredInAccountingPeriodComputation()));
			this.setTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod(
					String.valueOf(totalAmountOfPublicExpenditureRecoveredInAccountingPeriodComputation()));
			this.setTotalAmountOfEligibleExpenditureThatCanNotBeRecovered(
					String.valueOf(totalAmountOfEligibleExpenditureThatCanNotBeRecoveredComputation()));
			this.setTotalAmountOfPublicExpenditureThatCanNotBeRecovered(
					String.valueOf(totalAmountOfPublicExpenditureThatCanNotBeRecoveredComputation()));
		} catch (PersistenceBeanException e) {
			log.error("db error");
		} catch (NullPointerException e) {
			log.error("error");
		}

	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException,
			NullPointerException {
		fillList();
		this.setFailedValidation(new ArrayList<String>());
		if ((Boolean) this.getSession().get("closingAccountsNew")) {
			newPaymentApplicationPage();
		}
		if (this.getSession().get("closingAccountsEntityId") != null) {
			editPaymentApplicationPage();
			showPaymentApplicationPage();
		}
	}

	private void editPaymentApplicationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if ((Boolean) this.getSession().get("closingAccountsEdit")) {
			entityId = (Long) this.getSession().get("closingAccountsEntityId");
			ca = BeansFactory.ClosingOfAccounts().LoadById(entityId);
			if (ca.getState().getCode().equals(PaymentApplicationStatusTypes.Submitted.getValue())) {
				this.getSession().put("closingAccountsEdit", false);
				this.showPaymentApplicationPage();
				this.setCanDelete(true);
			} else {
				this.setShowButton(false);
				this.setRendered(true);
				this.setEntityForEditShowPage();
				this.setFieldsFunctionality();
			}

		}
	}

	private void showPaymentApplicationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (!(Boolean) this.getSession().get("closingAccountsEdit")) {
			this.setShowButton(true);
			entityId = (Long) this.getSession().get("closingAccountsEntityId");
			ca = BeansFactory.ClosingOfAccounts().LoadById(entityId);
			this.setRendered(true);
			setEntityForEditShowPage();
		}
	}

	public void setEntityForEditShowPage() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getSession().get("closingAccountsEntityId") != null) {
			entityId = (Long) this.getSession().get("closingAccountsEntityId");
			this.setEdit(!(Boolean) this.getSession().get("closingAccountsEdit"));
			this.setRendered(true);
			ca = BeansFactory.ClosingOfAccounts().LoadById(entityId);
			this.setClosingOfAccountsId(String.valueOf(entityId));
			this.setCompilationDate(ca.getCompilationDate());
			this.setDescription(ca.getDescription());
			this.setReferencePeriod(ca.getReferencePeriod());
			this.setFinalExpendsesForProjAbove1Mil(ca.getFinalExpendsesForProjAbove1Mil());
			this.setTotalAmountOfEligibleExpenses(String.valueOf(ca.getTotalAmountOfEligibleExpenses()));
			this.setTotalAmountOfPublicExpenditure(String.valueOf(ca.getTotalAmountOfPublicExpenditure()));
			this.setTotalAmountOfTransfersLinkedToPaymentsToEligibleEx(
					String.valueOf(ca.getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx()));
			this.setTotalAmountOfEligibleExpenditureWithdrawn(
					String.valueOf(ca.getTotalAmountOfEligibleExpenditureWithdrawn()));
			this.setTotalAmountOfPublicExpenditureWithdrawn(
					String.valueOf(ca.getTotalAmountOfPublicExpenditureWithdrawn()));
			this.setTotalAmountOfEligibleExpenditureRecovered(
					String.valueOf(ca.getTotalAmountOfEligibleExpenditureRecovered()));
			this.setTotalAmountOfPublicExpenditureRecovered(
					String.valueOf(ca.getTotalAmountOfPublicExpenditureRecovered()));
			this.setTotalAmountOfEligibleExpenditureToBeRecovered(
					String.valueOf(ca.getTotalAmountOfEligibleExpenditureToBeRecovered()));
			this.setTotalAmountOfPublicExpenditureToBeRecovered(
					String.valueOf(ca.getTotalAmountOfPublicExpenditureToBeRecovered()));
			this.setTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod(
					String.valueOf(ca.getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod()));
			this.setTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod(
					String.valueOf(ca.getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod()));
			this.setTotalAmountOfEligibleExpenditureThatCanNotBeRecovered(
					String.valueOf(ca.getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered()));
			this.setTotalAmountOfPublicExpenditureThatCanNotBeRecovered(
					String.valueOf(ca.getTotalAmountOfPublicExpenditureThatCanNotBeRecovered()));
			this.setRegistredAmountList(ca.getRegistredAmountList());
			this.setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList(
					ca.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod());
			this.setAmountsToBeRecoveredAtTheCloseList(ca.getAmountsToBeRecoveredAtTheClose());
			this.setUnrecoverableAmountsAtTheCloseList(ca.getUnrecoverableAmountsAtTheClose());

		}
	}

	private void newPaymentApplicationPage() {
		this.setRendered(false);
		this.setEdit(false);
		this.setShowButton(false);
		this.setFieldsFunctionality();
	}

	private void fillList() {
		fillRegistredAmountList();
		fillAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList();
		fillAmountsToBeRecoveredAtTheCloseList();
		fillUnrecoverableAmountsAtTheCloseList();
	}

	private void fillUnrecoverableAmountsAtTheCloseList() {
		this.unrecoverableAmountsAtTheCloseList = new ArrayList<>();
		UnrecoverableAmountsAtTheClose ua = null;
		for (int i = 1; i <= 4; i++) {
			ua = new UnrecoverableAmountsAtTheClose();
			ua.setAsse(String.valueOf(i));
			this.unrecoverableAmountsAtTheCloseList.add(ua);
		}
		ua = new UnrecoverableAmountsAtTheClose();
		ua.setAsse("Totale");
		this.unrecoverableAmountsAtTheCloseList.add(ua);
	}

	private void fillAmountsToBeRecoveredAtTheCloseList() {
		this.amountsToBeRecoveredAtTheCloseList = new ArrayList<>();
		AmountsToBeRecoveredAtTheClose atbr = null;
		for (int i = 1; i <= 4; i++) {
			atbr = new AmountsToBeRecoveredAtTheClose();
			atbr.setAsse(String.valueOf(i));
			this.amountsToBeRecoveredAtTheCloseList.add(atbr);
		}
		atbr = new AmountsToBeRecoveredAtTheClose();
		atbr.setAsse("Totale");
		this.amountsToBeRecoveredAtTheCloseList.add(atbr);
	}

	private void fillAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList() {
		this.amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList = new ArrayList<>();
		AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod award = null;
		for (int i = 1; i <= 4; i++) {
			award = new AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod();
			award.setAsse(String.valueOf(i));
			this.amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList.add(award);
		}
		award = new AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod();
		award.setAsse("Totale");
		this.amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList.add(award);
	}

	private void fillRegistredAmountList() {
		this.registredAmountList = new ArrayList<>();
		RegistredAmount ra = null;
		for (int i = 1; i <= 4; i++) {
			ra = new RegistredAmount();
			ra.setAsse(String.valueOf(i));
			this.registredAmountList.add(ra);
		}
		ra = new RegistredAmount();
		ra.setAsse("Totale");
		this.registredAmountList.add(ra);
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		// TODO Auto-generated method stub

	}

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public String getClosingOfAccountsId() {
		return closingOfAccountsId;
	}

	public void setClosingOfAccountsId(String closingOfAccountsId) {
		this.closingOfAccountsId = closingOfAccountsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Date getReferencePeriod() {
		return referencePeriod;
	}

	public void setReferencePeriod(Date referencePeriod) {
		this.referencePeriod = referencePeriod;
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public boolean isFinalExpendsesForProjAbove1Mil() {
		return finalExpendsesForProjAbove1Mil;
	}

	public void setFinalExpendsesForProjAbove1Mil(boolean finalExpendsesForProjAbove1Mil) {
		this.finalExpendsesForProjAbove1Mil = finalExpendsesForProjAbove1Mil;
	}

	public String getTotalAmountOfEligibleExpenses() {
		return totalAmountOfEligibleExpenses;
	}

	public void setTotalAmountOfEligibleExpenses(String totalAmountOfEligibleExpenses) {
		this.totalAmountOfEligibleExpenses = totalAmountOfEligibleExpenses;
	}

	public String getTotalAmountOfPublicExpenditure() {
		return totalAmountOfPublicExpenditure;
	}

	public void setTotalAmountOfPublicExpenditure(String totalAmountOfPublicExpenditure) {
		this.totalAmountOfPublicExpenditure = totalAmountOfPublicExpenditure;
	}

	public String getTotalAmountOfTransfersLinkedToPaymentsToEligibleEx() {
		return totalAmountOfTransfersLinkedToPaymentsToEligibleEx;
	}

	public void setTotalAmountOfTransfersLinkedToPaymentsToEligibleEx(
			String totalAmountOfTransfersLinkedToPaymentsToEligibleEx) {
		this.totalAmountOfTransfersLinkedToPaymentsToEligibleEx = totalAmountOfTransfersLinkedToPaymentsToEligibleEx;
	}

	public String getTotalAmountOfEligibleExpenditureWithdrawn() {
		return totalAmountOfEligibleExpenditureWithdrawn;
	}

	public void setTotalAmountOfEligibleExpenditureWithdrawn(String totalAmountOfEligibleExpenditureWithdrawn) {
		this.totalAmountOfEligibleExpenditureWithdrawn = totalAmountOfEligibleExpenditureWithdrawn;
	}

	public String getTotalAmountOfPublicExpenditureWithdrawn() {
		return totalAmountOfPublicExpenditureWithdrawn;
	}

	public void setTotalAmountOfPublicExpenditureWithdrawn(String totalAmountOfPublicExpenditureWithdrawn) {
		this.totalAmountOfPublicExpenditureWithdrawn = totalAmountOfPublicExpenditureWithdrawn;
	}

	public String getTotalAmountOfEligibleExpenditureRecovered() {
		return totalAmountOfEligibleExpenditureRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureRecovered(String totalAmountOfEligibleExpenditureRecovered) {
		this.totalAmountOfEligibleExpenditureRecovered = totalAmountOfEligibleExpenditureRecovered;
	}

	public String getTotalAmountOfPublicExpenditureRecovered() {
		return totalAmountOfPublicExpenditureRecovered;
	}

	public void setTotalAmountOfPublicExpenditureRecovered(String totalAmountOfPublicExpenditureRecovered) {
		this.totalAmountOfPublicExpenditureRecovered = totalAmountOfPublicExpenditureRecovered;
	}

	public String getTotalAmountOfEligibleExpenditureToBeRecovered() {
		return totalAmountOfEligibleExpenditureToBeRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureToBeRecovered(String totalAmountOfEligibleExpenditureToBeRecovered) {
		this.totalAmountOfEligibleExpenditureToBeRecovered = totalAmountOfEligibleExpenditureToBeRecovered;
	}

	public String getTotalAmountOfPublicExpenditureToBeRecovered() {
		return totalAmountOfPublicExpenditureToBeRecovered;
	}

	public void setTotalAmountOfPublicExpenditureToBeRecovered(String totalAmountOfPublicExpenditureToBeRecovered) {
		this.totalAmountOfPublicExpenditureToBeRecovered = totalAmountOfPublicExpenditureToBeRecovered;
	}

	public String getTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod() {
		return totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;
	}

	public void setTotalAmountOfEligibleExpenditureRecoveredInAccountingPeriod(
			String totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod) {
		this.totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod = totalAmountOfEligibleExpenditureRecoveredInAccountingPeriod;
	}

	public String getTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod() {
		return totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;
	}

	public void setTotalAmountOfPublicExpenditureRecoveredInAccountingPeriod(
			String totalAmountOfPublicExpenditureRecoveredInAccountingPeriod) {
		this.totalAmountOfPublicExpenditureRecoveredInAccountingPeriod = totalAmountOfPublicExpenditureRecoveredInAccountingPeriod;
	}

	public String getTotalAmountOfEligibleExpenditureThatCanNotBeRecovered() {
		return totalAmountOfEligibleExpenditureThatCanNotBeRecovered;
	}

	public void setTotalAmountOfEligibleExpenditureThatCanNotBeRecovered(
			String totalAmountOfEligibleExpenditureThatCanNotBeRecovered) {
		this.totalAmountOfEligibleExpenditureThatCanNotBeRecovered = totalAmountOfEligibleExpenditureThatCanNotBeRecovered;
	}

	public String getTotalAmountOfPublicExpenditureThatCanNotBeRecovered() {
		return totalAmountOfPublicExpenditureThatCanNotBeRecovered;
	}

	public void setTotalAmountOfPublicExpenditureThatCanNotBeRecovered(
			String totalAmountOfPublicExpenditureThatCanNotBeRecovered) {
		this.totalAmountOfPublicExpenditureThatCanNotBeRecovered = totalAmountOfPublicExpenditureThatCanNotBeRecovered;
	}

	public List<RegistredAmount> getRegistredAmountList() {
		return registredAmountList;
	}

	public void setRegistredAmountList(List<RegistredAmount> registredAmountList) {
		this.registredAmountList = registredAmountList;
	}

	public List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList() {
		return amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList;
	}

	public void setAmountsWithdrawnAndRecoveredDuringTheAccountingPeriodList(
			List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList) {
		this.amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList = amountsWithdrawnAndRecoveredDuringTheAccountingPeriodList;
	}

	public List<AmountsToBeRecoveredAtTheClose> getAmountsToBeRecoveredAtTheCloseList() {
		return amountsToBeRecoveredAtTheCloseList;
	}

	public void setAmountsToBeRecoveredAtTheCloseList(
			List<AmountsToBeRecoveredAtTheClose> amountsToBeRecoveredAtTheCloseList) {
		this.amountsToBeRecoveredAtTheCloseList = amountsToBeRecoveredAtTheCloseList;
	}

	public List<UnrecoverableAmountsAtTheClose> getUnrecoverableAmountsAtTheCloseList() {
		return unrecoverableAmountsAtTheCloseList;
	}

	public void setUnrecoverableAmountsAtTheCloseList(
			List<UnrecoverableAmountsAtTheClose> unrecoverableAmountsAtTheCloseList) {
		this.unrecoverableAmountsAtTheCloseList = unrecoverableAmountsAtTheCloseList;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}

	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public List<PaymentApplication> getPaList() {
		return paList;
	}

	public void setPaList(List<PaymentApplication> paList) {
		this.paList = paList;
	}

}
