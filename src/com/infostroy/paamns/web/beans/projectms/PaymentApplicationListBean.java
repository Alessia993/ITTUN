package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.RequestType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdvancesIncludedInPaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclaration;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpendituresOfPaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplicationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;
import com.infostroy.paamns.web.beans.EntityListBean;

public class PaymentApplicationListBean extends EntityListBean<PaymentApplication> {

	private Transaction tr;

	private Date protocolDate;

	private String protocolNumber;

	private Date compilationDate;

	private String paymentApplicationNumber;

	private String amountStateAidInPaymentApplication;

	private String amountEligibleExpenses;

	private String totalAmountOfPublicExpenditure;

	private String paType;

	private List<SelectItem> types;

	private List<RequestTypes> requestTypes;

	private List<ExpenditureDeclarationStates> states;

	private Long entityEditId;

	private Long entityDeleteId;

	public void doSearch() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.Page_Load();
	}

	public void clear() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setProtocolDate(null);
		this.setProtocolNumber(null);
		this.setCompilationDate(null);
		this.setPaType(null);
		this.Page_Load();
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		List<PaymentApplication> eds = null;
		if (this.getProtocolNumber() != null || this.getCompilationDate() != null
				|| (this.getPaymentApplicationNumber() != null && !this.getPaymentApplicationNumber().trim().isEmpty())
				|| this.getPaType() != null) {
			eds = BeansFactory.PaymentApplications().LoadAll(this.getPaymentApplicationNumber(),
					this.getCompilationDate(), this.getProtocolNumber(), this.getPaType());
		} else {
			eds = BeansFactory.PaymentApplications().LoadAll();
		}
		this.fillTypes();
		this.setList(eds);
		this.ReRenderScroll();
	}

	private void fillTypes() throws HibernateException, PersistenceBeanException {
		this.types = new ArrayList<>();
		try {
			setStates(BeansFactory.ExpenditureDeclarationStates().Load());
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		SelectItem item = new SelectItem();
		// item.setValue("0");
		// item.setLabel("Not selected");
		// this.types.add(item);
		this.types.add(SelectItemHelper.getFirst());
		for (ExpenditureDeclarationStates eds : this.getStates()) {
			item = new SelectItem();
			item.setValue(String.valueOf(eds.getId()));
			item.setLabel(eds.getValue());
			this.types.add(item);
		}
		if (this.getPaType() == null) {
			if (!this.types.isEmpty()) {
				this.setPaType(this.types.get(0).getValue().toString());

			}
		}

	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub

	}

	public void showItem() {
		// logica per visualizzare i
		if (this.getEntityEditId() != null) {
			this.getSession().put("paymentApplicationEdit", false);
			this.getSession().put("paymentApplicationNew", false);
			this.getSession().put("paymentApplicationEntityId", this.getEntityEditId());
			goTo(PagesTypes.PAYMENTAPPLICATIONEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("not entity found");
			}
		}
	}

	public void editItem() {
		if (this.getEntityEditId() != null) {
			this.getSession().put("paymentApplicationEdit", true);
			this.getSession().put("paymentApplicationEntityId", this.getEntityEditId());
			this.getSession().put("paymentApplicationNew", false);
			goTo(PagesTypes.PAYMENTAPPLICATIONEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("not entity found");
			}
		}
	}

	public void pageEdit() throws PersistenceBeanException {
		this.getSession().put("paymentApplicationNew", true);
		this.goTo(PagesTypes.PAYMENTAPPLICATIONEDIT);
		this.PreLoad();
	}

	public void delete() {

		Long id = this.getEntityDeleteId();
		try {
			PaymentApplication pa = BeansFactory.PaymentApplications().LoadById(id);
			// List<DurCompilations> durs = pa.getDurCompilations();
			List<DurCompilations> durs = new ArrayList<>();
			for (PaymentApplicationDurCompilations eddc : BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(id)) {
				durs.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
			}
			tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			for (DurCompilations dur : durs) {
				PaymentApplication pai = BeansFactory.PaymentApplications().Load(dur.getPaymentApplication().getId());
				if (pai.getRequestType()!=null && pa.getRequestType()!= null && pai.getRequestType().getId().equals(RequestType.IntermediateFinal.getId()) && pa.getRequestType().getId().equals(RequestType.IntermediateFinal.getId())) {
					dur.setPaymentApplication(null);
					BeansFactory.DurCompilations().UpdateInTransaction(dur);
				}else if(pa.getRequestType()!= null && !pa.getRequestType().getId().equals(RequestType.IntermediateFinal.getId())){
					dur.setPaymentApplication(null);
					BeansFactory.DurCompilations().UpdateInTransaction(dur);
				}else if(pa.getRequestType() == null){
					dur.setPaymentApplication(null);
					BeansFactory.DurCompilations().UpdateInTransaction(dur);
				}

			}

			pa.setDeleted(true);
			PersistenceSessionManager.getBean().getSession().merge(pa);

			// PersistenceSessionManager.getBean().getSession().delete(pa);

			List<PaymentApplicationDurCompilations> padcs = BeansFactory.PaymentApplicationDurCompilations()
					.getAllByPaymentApplication(pa.getId());
			for (PaymentApplicationDurCompilations padc : padcs) {
				padc.setDeleted(true);
				PersistenceSessionManager.getBean().getSession().merge(padc);
			}

			List<AdvancesIncludedInPaymentApplication> ladvs = BeansFactory.AdvancesIncludedInPaymentApplications()
					.loadByPaymentApplicationId(id);
			for (AdvancesIncludedInPaymentApplication adv : ladvs) {
				adv.setDeleted(true);
				// PersistenceSessionManager.getBean().getSession().delete(adv);
				PersistenceSessionManager.getBean().getSession().merge(adv);
			}
			
			List<ExpendituresOfPaymentApplication> eopa = BeansFactory.ExpendituresOfPaymentApplication().loadByPaymentApplicationId(id);
			for (ExpendituresOfPaymentApplication eopai : eopa) {
				eopai.setDeleted(true);
				// PersistenceSessionManager.getBean().getSession().delete(adv);
				PersistenceSessionManager.getBean().getSession().merge(eopai);
			}

		} catch (HibernateException | NumberFormatException | PersistenceBeanException e) {
			// TODO Auto-generated catch block
			log.error("db error: entity not found");
			if (tr != null) {
				tr.rollback();
			}
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
				tr.commit();
			}
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
			// TODO Auto-generated catch block
			log.error("error delete");
			e.printStackTrace();
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

	public Date getProtocolDate() {
		return protocolDate;
	}

	public void setProtocolDate(Date protocolDate) {
		this.protocolDate = protocolDate;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public String getPaymentApplicationNumber() {
		return paymentApplicationNumber;
	}

	public void setPaymentApplicationNumber(String paymentApplicationNumber) {
		this.paymentApplicationNumber = paymentApplicationNumber;
	}

	public String getAmountStateAidInPaymentApplication() {
		return amountStateAidInPaymentApplication;
	}

	public void setAmountStateAidInPaymentApplication(String amountStateAidInPaymentApplication) {
		this.amountStateAidInPaymentApplication = amountStateAidInPaymentApplication;
	}

	public String getAmountEligibleExpenses() {
		return amountEligibleExpenses;
	}

	public void setAmountEligibleExpenses(String amountEligibleExpenses) {
		this.amountEligibleExpenses = amountEligibleExpenses;
	}

	public String getTotalAmountOfPublicExpenditure() {
		return totalAmountOfPublicExpenditure;
	}

	public void setTotalAmountOfPublicExpenditure(String totalAmountOfPublicExpenditure) {
		this.totalAmountOfPublicExpenditure = totalAmountOfPublicExpenditure;
	}

	public String getPaType() {
		return paType;
	}

	public void setPaType(String paType) {
		this.paType = paType;
	}

	public List<SelectItem> getTypes() {
		return types;
	}

	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}

	public List<RequestTypes> getRequestTypes() {
		return requestTypes;
	}

	public void setRequestTypes(List<RequestTypes> requestTypes) {
		this.requestTypes = requestTypes;
	}

	public Long getEntityEditId() {
		return entityEditId;
	}

	public void setEntityEditId(Long entityEditId) {
		this.entityEditId = entityEditId;
	}

	public List<ExpenditureDeclarationStates> getStates() {
		return states;
	}

	public void setStates(List<ExpenditureDeclarationStates> states) {
		this.states = states;
	}

	public void setEntityDeleteId(Long entityDeleteId) {
		this.getViewState().put("entityDeleteId", entityDeleteId);
		this.entityDeleteId = entityDeleteId;
	}

	/**
	 * Gets entityDeleteId
	 * 
	 * @return entityDeleteId the entityDeleteId
	 */
	public Long getEntityDeleteId() {
		entityDeleteId = (Long) this.getViewState().get("entityDeleteId");
		return entityDeleteId;
	}

}
