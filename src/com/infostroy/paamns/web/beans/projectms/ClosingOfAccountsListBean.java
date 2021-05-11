package com.infostroy.paamns.web.beans.projectms;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AmountsToBeRecoveredAtTheClose;
import com.infostroy.paamns.persistence.beans.entities.domain.AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod;
import com.infostroy.paamns.persistence.beans.entities.domain.ClosingAccounts;
import com.infostroy.paamns.persistence.beans.entities.domain.RegistredAmount;
import com.infostroy.paamns.persistence.beans.entities.domain.UnrecoverableAmountsAtTheClose;
import com.infostroy.paamns.web.beans.EntityListBean;

public class ClosingOfAccountsListBean extends EntityListBean<ClosingAccounts>{
	
	private Long entityEditId;

	private Long entityDeleteId;
	
	private Transaction tr;
	
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		List<ClosingAccounts> coas = null;
			coas = BeansFactory.ClosingOfAccounts().LoadAll();
		this.setList(coas);
		this.ReRenderScroll();
	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub
		
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
	
	public void editItem() {
		if (this.getEntityEditId() != null) {
			this.getSession().put("closingAccountsEdit", true);
			this.getSession().put("closingAccountsEntityId", this.getEntityEditId());
			this.getSession().put("closingAccountsNew", false);
			goTo(PagesTypes.CLOSINGACCOUNTSEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				log.error("not entity found");
			}
		}
	}
	
	public void showItem() {
		// logica per visualizzare i
		if (this.getEntityEditId() != null) {
			this.getSession().put("closingAccountsEdit", false);
			this.getSession().put("closingAccountsNew", false);
			this.getSession().put("closingAccountsEntityId", this.getEntityEditId());
			goTo(PagesTypes.CLOSINGACCOUNTSEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				log.error("not entity found");
			}
		}
	}
	
	public void pageEdit() throws PersistenceBeanException {
		this.getSession().put("closingAccountsNew", true);
		this.goTo(PagesTypes.CLOSINGACCOUNTSEDIT);
		this.PreLoad();
	}
	
	public void delete() {

		Long id = this.getEntityDeleteId();
		try {
			ClosingAccounts ca = BeansFactory.ClosingOfAccounts().LoadById(id);
			List<RegistredAmount> registredAmountList = ca.getRegistredAmountList();
			List<AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod> amountsWithdrawnAndRecoveredDuringTheAccountingPeriod = ca.getAmountsWithdrawnAndRecoveredDuringTheAccountingPeriod();
			List<AmountsToBeRecoveredAtTheClose> amountsToBeRecoveredAtTheClose = ca.getAmountsToBeRecoveredAtTheClose();
			List<UnrecoverableAmountsAtTheClose> unrecoverableAmountsAtTheClose = ca.getUnrecoverableAmountsAtTheClose();
			
			tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			if(registredAmountList!=null){
				for(RegistredAmount ra : registredAmountList){
//				PersistenceSessionManager.getBean().getSession().delete(ra);
					ra.setDeleted(true);
					PersistenceSessionManager.getBean().getSession().merge(ra);
				}
			}
			if(amountsWithdrawnAndRecoveredDuringTheAccountingPeriod!=null){
				for(AmountsWithdrawnAndRecoveredDuringTheAccountingPeriod aw :amountsWithdrawnAndRecoveredDuringTheAccountingPeriod){
//					PersistenceSessionManager.getBean().getSession().delete(aw);
					aw.setDeleted(true);
					PersistenceSessionManager.getBean().getSession().merge(aw);
				}
			}
			if(amountsToBeRecoveredAtTheClose!=null){
				for(AmountsToBeRecoveredAtTheClose atbr :amountsToBeRecoveredAtTheClose){
					atbr.setDeleted(true);
//					PersistenceSessionManager.getBean().getSession().delete(atbr);
					PersistenceSessionManager.getBean().getSession().merge(atbr);
				}
			}
			if(unrecoverableAmountsAtTheClose!=null){
				for(UnrecoverableAmountsAtTheClose ura :unrecoverableAmountsAtTheClose){
					ura.setDeleted(true);
					PersistenceSessionManager.getBean().getSession().merge(ura);
//					PersistenceSessionManager.getBean().getSession().delete(ura);
				}
			}
//			PersistenceSessionManager.getBean().getSession().delete(ca);
			ca.setDeleted(true);
			PersistenceSessionManager.getBean().getSession().merge(ca);
		} catch (HibernateException | NumberFormatException | PersistenceBeanException e) {
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
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
			log.error("error delete");
		}
	}
	
	public Long getEntityEditId() {
		return entityEditId;
	}

	public void setEntityEditId(Long entityEditId) {
		this.entityEditId = entityEditId;
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
