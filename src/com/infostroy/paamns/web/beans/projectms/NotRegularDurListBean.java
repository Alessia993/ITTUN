/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.SimpleMailSender;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.DURCompilationWrapper;

/**
 *
 * @author Sergey Zorin InfoStroy Co., 2010.
 *
 */
public class NotRegularDurListBean extends EntityProjectListBean<CostDefinitions> {

	/*
	 * @Override public void Page_Load() throws NumberFormatException,
	 * HibernateException, PersistenceBeanException, PersistenceBeanException {
	 * List<DurCompilations> listDurs = BeansFactory.DurCompilations()
	 * .LoadReimbursedOnly(Long.valueOf(this.getProjectId()));
	 * 
	 * this.setList(new ArrayList<DURCompilationWrapper>());
	 * 
	 * for (DurCompilations dc : listDurs) { DurInfos di =
	 * BeansFactory.DurInfos().LoadByDurCompilation( dc.getId()); DurSummaries
	 * ds = BeansFactory.DurSummaries().LoadByDurCompilation( dc.getId());
	 * 
	 * DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc,
	 * dc.getDurState(), di, ds);
	 * 
	 * this.getList().add(dcw); } }
	 */

	@Override
	public void Page_Load()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException {
		List<CostDefinitions> listCostDefinitions = BeansFactory.CostDefinitions()
				.getAllReimbursedCostDefinitions(this.getProjectId());

		this.setList(new ArrayList<CostDefinitions>());

		for (CostDefinitions cd : listCostDefinitions) {
			cd.getDurCompilation();
			this.getList().add(cd);

		}

	}

	public void Page_Load_Static() throws NumberFormatException, PersistenceBeanException {
		if (!this.getSessionBean().getIsActualSate()) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}

		if (!AccessGrantHelper.IsNotRegularSituationAvailable()) {
			this.goTo(PagesTypes.PROJECTLIST);
		}
	}

	@Override
	public void addEntity() {
		// This feature is not applicable
	}

	@Override
	public void deleteEntity() {
		// This feature is not applicable
	}

	@Override
	public void editEntity() {
		try {
			/*
			 * DurCompilations dc =
			 * BeansFactory.DurCompilations().Load(this.getEntityEditId());
			 * 
			 * if (dc != null && !dc.getDeleted()) {
			 * this.getSession().put("notRegularDurList",
			 * this.getEntityEditId());
			 * this.getSession().put("notRegularDurListShowOnly", false);
			 * 
			 * this.goTo(PagesTypes.NOTREGULARDUREDIT); }
			 */
			CostDefinitions cd = BeansFactory.CostDefinitions().Load(this.getEntityEditId());
			if (cd != null && !cd.getDeleted()) {
				this.getSession().put("notRegularDurList", this.getEntityEditId());
				this.getSession().put("notRegularDurListShowOnly", false);

				this.goTo(PagesTypes.NOTREGULARDUREDIT);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}
	}

	public void showItem() {
		try {
			DurCompilations dc = BeansFactory.DurCompilations().Load(this.getEntityEditId());

			if (dc != null && !dc.getDeleted()) {
				this.getSession().put("notRegularDurList", this.getEntityEditId());
				this.getSession().put("notRegularDurListShowOnly", true);

				this.goTo(PagesTypes.NOTREGULARDUREDIT);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}
	}

	public void addToCache() throws HibernateException, PersistenceBeanException {

		for (CostDefinitions cd : this.getList()) {
			if (cd.getSelected()) {
				this.getViewState().put("notRegularDurIdToConvert", cd.getId());
				break;
			}
		}
	}

	public void trToRecoverable() {
		try {
			Long entityId = (Long) this.getViewState().get("notRegularDurIdToConvert");
			if(entityId != null){
				CostDefinitions cd = BeansFactory.CostDefinitions().Load(entityId);
				if (cd != null && !cd.getDeleted()) {
					this.getSession().put("notRegularDurList", entityId);
					this.getSession().put("notRegularDurListShowOnly", false);
					this.getSession().put("notRegularDurRecoverable", true);

					this.goTo(PagesTypes.NOTREGULARDUREDIT);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}
	}

	public void trToNotRecoverable() {
		try {
			Long entityId = (Long) this.getViewState().get("notRegularDurIdToConvert");
			if(entityId != null){
				CostDefinitions cd = BeansFactory.CostDefinitions().Load(entityId);
				if (cd != null && !cd.getDeleted()) {
					this.getSession().put("notRegularDurList", entityId);
					this.getSession().put("notRegularDurListShowOnly", false);
					this.getSession().put("notRegularDurRecoverable", false);
					
					this.goTo(PagesTypes.NOTREGULARDUREDIT);
				}
			}
			
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}
	}
}
