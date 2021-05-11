package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclaration;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationAccountingPeriod;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DurStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;
import com.infostroy.paamns.web.beans.EntityListBean;

public class ExpenditureDeclarationListBean extends EntityListBean<ExpenditureDeclaration> {

	private Date edCompilationDate;

	private String edProtocolNumber;

	private Date protocolDate;

	private String edTotalExpenditure;

	private String edType;

	private String expenditureDeclarationNumber;

	private List<SelectItem> types;

	private List<RequestTypes> requestTypes;

	private HtmlDataScroller scroller;

	private Transaction tr;

	private Long entityDeleteId;
	
	private Long entitySendId;
	
	private List<ExpenditureDeclarationStates> states;

	private final String THIS_PAGE_NAME = "ExpenditureDeclarationList.jsf";

	private final String EDIT_PAGE_NAME = "ExpenditureDeclarationEdit.jsf";
	private String editPage = EDIT_PAGE_NAME;

	private static Logger logger = Logger.getLogger(ExpenditureDeclarationListBean.class.toString());

	public void sendItem(){
		if(this.getEntitySendId()!=null){
			try {
				ExpenditureDeclaration ed = BeansFactory.ExpenditureDeclarations().LoadById(this.getEntitySendId());
				//List<DurCompilations> dcs = ed.getDurCompilations();
				List<DurCompilations> dcs = new ArrayList<>();
				List<ExpenditureDeclarationDurCompilations> edds = BeansFactory.ExpenditureDeclarationDurCompilations().getAllByExpenditureDeclaratios(this.getEntitySendId());
				for(ExpenditureDeclarationDurCompilations eddcs : edds){//ed.getExpenditureDeclarationDurCompilations()){
					dcs.add(BeansFactory.DurCompilations().Load(eddcs.getDurCompilationsId()));
				}
				DurStates ds =BeansFactory.DurStates().Load(DurStateTypes.ACUEvaluation.getValue());
//				DurStates ds =BeansFactory.DurStates().Load(DurStateTypes.Certifiable.getValue());
				
				ed.setToBeTrasmitted(true);
				tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
				for(DurCompilations dc: dcs){
					dc.setDurState(ds);
					PersistenceSessionManager.getBean().getSession().merge(dc);
				}
				PersistenceSessionManager.getBean().getSession().merge(ed);
				tr.commit();
				this.Page_Load();
			} catch (HibernateException | NumberFormatException | PersistenceBeanException e) {
				log.error("db error");
				if (tr != null) {
					tr.rollback();
				}
			}
		}
	}
	
	public void doSearch() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.Page_Load();
	}

	public void clear() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setProtocolDate(null);
		this.setEdProtocolNumber(null);
		this.setEdCompilationDate(null);
		this.setExpenditureDeclarationNumber(null);
		//this.setExpenditureIncrease(null);
		this.setEdTotalExpenditure(null);
		this.setEdType(null);
		this.Page_Load();
	}

	private void fillTypes() throws HibernateException, PersistenceBeanException {
		this.types = new ArrayList<>();
		try {
			setStates(BeansFactory.ExpenditureDeclarationStates().Load());

		} catch (PersistenceBeanException e) {

			log.error("db error");
		}
		SelectItem item = new SelectItem();
		this.types.add(SelectItemHelper.getFirst());

		for(ExpenditureDeclarationStates eds : this.getStates()){
			item = new SelectItem();
			item.setValue(String.valueOf(eds.getId()));
			item.setLabel(eds.getValue());
			this.types.add(item);
		}
		if (this.getEdType() == null) {
			if (!this.types.isEmpty()) {
				this.setEdType(this.types.get(0).getValue().toString());

			}
		}

	}

	public Date getDocumentDate()

	{
		return (Date) this.getSession().get("expenditureDeclarationDate");
	}

	public void setDocumentDate(Date expenditureDeclarationDate) {
		this.getSession().put("expenditureDeclarationDate", expenditureDeclarationDate);
	}

	public void pageEdit() throws PersistenceBeanException {
		this.getSession().put("expenditureDeclarationNew", true);
		this.goTo(PagesTypes.EXPENDITUREDECLARATIONEDIT);
		this.PreLoad();
	}

	@Override
	public void Page_Load()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException {
		List<ExpenditureDeclaration> eds = null;
		this.setList(null);
		if (this.getProtocolDate() != null || this.getEdProtocolNumber() != null || this.getEdCompilationDate() != null
				|| (this.getExpenditureDeclarationNumber() != null
						&& !this.getExpenditureDeclarationNumber().trim().isEmpty())
				/*|| this.getExpenditureIncrease() != null */|| this.getEdType() != null) {
			try{
			eds = BeansFactory.ExpenditureDeclarations().LoadAll(this.getExpenditureDeclarationNumber(),
					this.getEdCompilationDate(), this.getEdProtocolNumber(), this.getProtocolDate(),
					/*this.getExpenditureIncrease()*/ null, this.getEdTotalExpenditure(), this.getEdType());
			}catch(Exception ex){
				ex.printStackTrace();
			}
		} else {
			eds = BeansFactory.ExpenditureDeclarations().LoadAll();
		}
		this.fillTypes();
		this.setList(eds);
		this.ReRenderScroll();
	}

	@Override
	public void addEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editEntity() {
		// TODO Auto-generated method stub

	}

	public void delete() {

		Long id = this.getEntityDeleteId();
		try {
			ExpenditureDeclaration ed = BeansFactory.ExpenditureDeclarations().LoadById(id);
			List<DurCompilations> durs = new ArrayList<>();//ed.getDurCompilations();
			//for(ExpenditureDeclarationDurCompilations eddc : ed.getExpenditureDeclarationDurCompilations()){
			for(ExpenditureDeclarationDurCompilations eddc : BeansFactory.ExpenditureDeclarationDurCompilations().getAllByExpenditureDeclaratios(id)){
				durs.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
			}

			tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			for (DurCompilations dur : durs) {
				dur.setExpenditureDeclaration(null);
				BeansFactory.DurCompilations().UpdateInTransaction(dur);
			}
			
			List<ExpenditureDeclarationAccountingPeriod> listEDAP = BeansFactory.ExpenditureDeclarationAccountingPeriod().getAllByExpenditureDeclaration(id);
			for(ExpenditureDeclarationAccountingPeriod edap : listEDAP){
				edap.setDeleted(true);
				PersistenceSessionManager.getBean().getSession().merge(edap);
			}
			ed.setDeleted(true);
			PersistenceSessionManager.getBean().getSession().merge(ed);
			
			List<ExpenditureDeclarationDurCompilations> eddcs = BeansFactory.ExpenditureDeclarationDurCompilations().getAllByExpenditureDeclaratios(ed.getId());
			for(ExpenditureDeclarationDurCompilations eddc : eddcs ){
				eddc.setDeleted(true);
				PersistenceSessionManager.getBean().getSession().merge(eddc);
			}
			//PersistenceSessionManager.getBean().getSession().delete(ed);

		} catch (HibernateException | NumberFormatException | PersistenceBeanException e) {
			log.error("db error: entity not found");
			if (tr != null) {
				tr.rollback();
			}
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e1) {
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

	@Override
	public void deleteEntity() {
	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub
		this.setList(null);
	}

	public Boolean getNeedReload() {
		if (this.getViewState().containsKey("needReload")) {
			return (Boolean) this.getViewState().get("needReload");
		} else {
			return false;
		}
	}

	public void setNeedReload(Boolean needReload) {
		this.getViewState().put("needReload", needReload);
	}

	public void editItem() {
		if (this.getEntityEditId() != null) {
			this.getSession().put("expanditureDeclarationEdit", true);
			this.getSession().put("expanditureDeclarationEntityId", this.getEntityEditId());
			this.getSession().put("expenditureDeclarationNew", false);
			goTo(PagesTypes.EXPENDITUREDECLARATIONEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("not entity found");
			}
		}
	}

	public void showItem() {
		// logica per visualizzare i
		if (this.getEntityEditId() != null) {
			this.getSession().put("expanditureDeclarationEdit", false);
			this.getSession().put("expenditureDeclarationNew", false);
			this.getSession().put("expanditureDeclarationEntityId", this.getEntityEditId());
			goTo(PagesTypes.EXPENDITUREDECLARATIONEDIT);
		} else {
			try {
				this.Page_Load();
			} catch (NumberFormatException | HibernateException | PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("not entity found");
			}
		}
	}

	/**
	 * Gets editPage
	 * 
	 * @return editPage the editPage
	 */
	public String getEditPage() {
		return this.editPage;
	}

	/**
	 * Sets editPage
	 * 
	 * @param editPage
	 *            the editPage to set
	 */
	public void setEditPage(String editPage) {
		this.editPage = editPage;
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureDeclaration> getExpenditureDeclarationsList() {
		return (List<ExpenditureDeclaration>) this.getViewState().get("tempExpenditureDeclarationsList");
	}

	public void setExpenditureDeclarationsList(List<ExpenditureDeclaration> tempExpenditureDeclarationsList) {
		this.getViewState().put("tempExpenditureDeclarationsList", tempExpenditureDeclarationsList);
	}

	/**
	 * Gets tableFirstRow
	 * 
	 * @return tableFirstRow the tableFirstRow
	 */
	public Integer getTableFirstRow() {
		if (this.getViewState().containsKey("tableFirstRow")) {
			return (Integer) getViewState().get("tableFirstRow");
		}
		return null;
	}

	/**
	 * Sets tableFirstRow
	 * 
	 * @param tableFirstRow
	 *            the tableFirstRow to set
	 */
	public void setTableFirstRow(Integer tableFirstRow) {
		this.getViewState().put("tableFirstRow", tableFirstRow);
	}

	/**
	 * Gets scroller
	 * 
	 * @return scroller the scroller
	 */
	public HtmlDataScroller getScroller() {
		return scroller;
	}

	/**
	 * Sets scroller
	 * 
	 * @param scroller
	 *            the scroller to set
	 */
	public void setScroller(HtmlDataScroller scroller) {
		this.scroller = scroller;
	}

	/**
	 * Sets entityDeleteId
	 * 
	 * @param entityDeleteId
	 *            the entityDeleteId to set
	 */
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

	public Date getEdCompilationDate() {
		return edCompilationDate;
	}

	public void setEdCompilationDate(Date edCompilationDate) {
		this.edCompilationDate = edCompilationDate;
	}

	public String getEdProtocolNumber() {
		return edProtocolNumber;
	}

	public void setEdProtocolNumber(String edProtocolNumber) {
		this.edProtocolNumber = edProtocolNumber;
	}

	public Date getProtocolDate() {
		return protocolDate;
	}

	public void setProtocolDate(Date protocolDate) {
		this.protocolDate = protocolDate;
	}

	public String getEdTotalExpenditure() {
		return edTotalExpenditure;
	}

	public void setEdTotalExpenditure(String edTotalExpenditure) {
		this.edTotalExpenditure = edTotalExpenditure;
	}

	public String getEdType() {
		return (String) this.getSession().get("edType");
	}

	public void setEdType(String edType) {
		this.getSession().put("edType", edType);
		this.edType = edType;
	}

	public String getExpenditureDeclarationNumber() {
		return expenditureDeclarationNumber;
	}

	public void setExpenditureDeclarationNumber(String expenditureDeclarationNumber) {
		this.expenditureDeclarationNumber = expenditureDeclarationNumber;
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

	public Long getEntitySendId() {
		return entitySendId;
	}

	public void setEntitySendId(Long entitySendId) {
		this.entitySendId = entitySendId;
	}
	
	public List<ExpenditureDeclarationStates> getStates() {
		return states;
	}

	public void setStates(List<ExpenditureDeclarationStates> states) {
		this.states = states;
	}

}
