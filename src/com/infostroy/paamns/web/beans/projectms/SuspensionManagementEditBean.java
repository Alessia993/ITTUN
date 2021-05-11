package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationAccountingPeriod;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsSpecificGoalsWrapper;

public class SuspensionManagementEditBean extends EntityProjectEditBean<Entity> {

	private CostDefinitions durCostDefinition;

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	@Override
	public void GoBack() {
		if (this.getSession().get("notRegularDurList") != null) {
			this.getSession().put("notRegularDurList", null);
		}
		this.goTo(PagesTypes.SUSPENSIONMANAGEMENTLIST);
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException,
			NullPointerException {
		if (this.getSession().get("suspensionManagementList") != null) {
			this.durCostDefinition = BeansFactory.CostDefinitions()
					.Load(String.valueOf(this.getSession().get("suspensionManagementList")));
		}
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		this.durCostDefinition.setTotAmountEligExpSuspended(this.durCostDefinition.getTotAmountEligExpSuspended());
		this.durCostDefinition.setTotAmountPublExpSuspended(this.durCostDefinition.getTotAmountPublExpSuspended());
		this.durCostDefinition
				.setTotAmountEligExpToBeWithdrawn(this.durCostDefinition.getTotAmountEligExpToBeWithdrawn());
		this.durCostDefinition
				.setTotAmountPublExpToBeWithdrawn(this.durCostDefinition.getTotAmountPublExpToBeWithdrawn());
		this.durCostDefinition.setTotAmountEliglExpRetired(this.durCostDefinition.getTotAmountEliglExpRetired());
		this.durCostDefinition.setTotAmountPublExpRetired(this.durCostDefinition.getTotAmountPublExpRetired());

		this.defineAccountingPeriod();
		this.setFieldsIntoOtherExpenditure();
	}

	private void setFieldsIntoOtherExpenditure() {
		List<ExpenditureDeclarationAccountingPeriod> listOldEDAP = null;
		// due casi
		// se la dichiarazione di spesa che si sta per salvare ha già un id
		try {
			CostDefinitionsSpecificGoalsWrapper cdw = BeansFactory.CostDefinitions().getByExpenditureDeclarationSpecificGoal(this.durCostDefinition.getId());
			List<BigInteger> listOfExpenditureDeclarationId = BeansFactory.ExpenditureDeclarationAccountingPeriod().getExpenditureDeclarationIdDistinct(this.getDateFrom(), this.getDateTo());
			if(listOfExpenditureDeclarationId!= null){
				for(BigInteger id :listOfExpenditureDeclarationId){
					listOldEDAP = BeansFactory.ExpenditureDeclarationAccountingPeriod().getAllByExpenditureDeclaration(id.longValue());

					for(ExpenditureDeclarationAccountingPeriod exap : listOldEDAP){
						if(cdw!= null && exap.getSpecificObjective()!= null && exap.getSpecificObjective().equals(cdw.getSpecificObjective())){
							// colonna c
							exap.setAmountOfWithdrawalsAccountingPeriod(exap.getAmountOfWithdrawalsAccountingPeriod()
									+ ((this.durCostDefinition.getTotAmountEliglExpRetired() != null) ? this.durCostDefinition.getTotAmountEliglExpRetired()
											: 0d) - (cdw.getTotAmountEliglExpRetired() != null ? cdw.getTotAmountEliglExpRetired() : 0d));
							// colonna d
							exap.setSuspensionAmount(
									exap.getSuspensionAmount() + ((this.durCostDefinition.getTotAmountEligExpSuspended() != null)
											? this.durCostDefinition.getTotAmountEligExpSuspended() : 0d) - (cdw.getTotAmountEligExpSuspended()!=null ? cdw.getTotAmountEligExpSuspended() : 0d));
							exap.setCumulativeAmount(exap.getAmountOfPastExDecl() + exap.getAmountOfExDeclForAccountingPeriod()
							- exap.getAmountOfWithdrawalsAccountingPeriod() - exap.getSuspensionAmount());
						
						}
					}
				}
			}
		} catch (PersistenceBeanException ex) {
			log.error(ex.toString());
		}
	}
	
	private void defineAccountingPeriod() {
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
	}

	public CostDefinitions getDurCostDefinition() {
		return durCostDefinition;
	}

	public void setDurCostDefinition(CostDefinitions durCostDefinition) {
		this.durCostDefinition = durCostDefinition;
	}

	public Date getDateTo() {
		return (Date) this.getViewState().get("dateTo");
	}

	public void setDateTo(Date dateTo) {
		this.getViewState().put("dateTo", dateTo);
	}

	public Date getDateFrom() {
		return (Date) this.getViewState().get("dateFrom");
	}

	public void setDateFrom(Date dateFrom) {
		this.getViewState().put("dateFrom", dateFrom);
	}
}
