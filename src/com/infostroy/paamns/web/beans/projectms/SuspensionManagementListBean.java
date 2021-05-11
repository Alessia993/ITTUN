package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CostDefinitionSuspendStatus;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.LogActionTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ReasonType;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.logging.InfoLog;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.UserActivityLog;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsWrapper;

/**
 * 
 * @author Zrazhevskiy Vladimir InfoStroy Co., 2011.
 * 
 */
public class SuspensionManagementListBean extends EntityProjectListBean<CostDefinitions> {
	private class SuspendComparator implements Comparator<CostDefinitions> {
		@Override
		public int compare(CostDefinitions o1, CostDefinitions o2) {
			if (o1.getSuspendDate() == null && o2.getSuspendDate() == null) {
				return 0;
			}

			if (o1.getSuspendDate() == null) {
				return 1;
			}

			if (o2.getSuspendDate() == null) {
				return -1;
			}

			return (o1.getSuspendDate().getTime() > o2.getSuspendDate().getTime() ? -1 : (o1 == o2 ? 0 : 1));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */

	private Date confirmDate;

	private Date resendDate;

	private List<SelectItem> listFilter;

	private static Long entitySendId;

	private Date finalActionDate;

	private static Long entityAnnulId;

	private Boolean canSelectAll;

	private Boolean canSelectAllResend;

	private HtmlDataScroller scroller;

	private String actionMotivation;

	private String cancelActNumber;

	private Date cancelDate;

	private String cancelReason;

	private String cancelNote;

	private Documents cancelSuspendDocument;

	private List<SelectItem> listSelectRoles;

	private List<SelectItem> listPartners;

	private String selectedCategory;

	private List<SelectItem> categories;

	private UploadedFile suspendDocumentUpFile;

	private Date retreatDate;

	private String retreatActNumber;

	private Date retreatActDate;

	private String retreatAmount;

	private Long selctedRetreatReason;

	private String retreatNote;

	private Documents retreatDocument;

	private UploadedFile retreatDocumentUpFile;

	private List<SelectItem> allReasonTypes;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getFilter() == null || this.getFilter().equals("")) {
			this.setFilter("0");
		}

		if (this.getResendDate() == null) {
			this.setResendDate(new Date());
		}

		this.setCancelSuspendDocument(new Documents());
		this.setFinalActionDate(new Date());
		this.setConfirmDate(new Date());
		List<CostDefinitions> list = null;
		loadFilterValues();
		if (this.getListCostDefinitionsWeb() == null || getNeedReload()) {
			setNeedReload(false);
			list = BeansFactory.CostDefinitions().GetSuspended(this.getSessionBean().getProjectId());

			SuspendComparator comparator = new SuspendComparator();

			if (list != null) {
				Collections.sort(list, comparator);
			}

			this.setListCostDefinitionsWeb(list);

			this.setList(filterLoadedList(list));
		} else {
			list = this.getListCostDefinitionsWeb();

			this.setList(list);
		}

		recheckCostItems(list);

		checkCanSelect();

		if (!isPostback()) {
			Map<Long, CostDefinitionsWrapper> map = new HashMap<Long, CostDefinitionsWrapper>();
			this.getViewState().put("oldValues", map);
			for (CostDefinitions costDefinition : list) {
				map.put(costDefinition.getId(), new CostDefinitionsWrapper(costDefinition));
			}
		}
		this.ReRenderScroll();

		this.fillRoles();
		this.fillPartners();
		this.fillCategories();

		this.setCancelSuspendDocument(new Documents());
		this.getCancelSuspendDocument().setDocumentDate(new Date());
		this.getCancelSuspendDocument().setEditableDate(new Date());
		this.setCancelDate(new Date());
		this.setRetreatDate(new Date());

		this.setRetreatDocument(new Documents());
		this.getRetreatDocument().setDocumentDate(new Date());
		this.getRetreatDocument().setEditableDate(new Date());

		this.setAllReasonTypes(new ArrayList<SelectItem>());
		this.allReasonTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));

		for (ReasonType type : ReasonType.values()) {
			this.allReasonTypes.add(new SelectItem(type.getId(), type.toString(Boolean.FALSE)));
		}
	}

	public void firstPage() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.Page_Load();
		if (getScroller() != null && getScroller().isPaginator()) {
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	private void loadFilterValues() {
		this.listFilter = new ArrayList<SelectItem>();
		this.listFilter.add(new SelectItem("0", Utils.getString("all")));
		if (getSessionBean().isSTC()) {
			this.listFilter.add(new SelectItem("1", Utils.getString("suspensionManagmentListFilterRefisedByAgu")));
		}
		if (getSessionBean().isSTC() || getSessionBean().isAGU()) {
			this.listFilter.add(new SelectItem("2", Utils.getString("suspensionManagmentListFilterRefisedByAcu")));
		}
		this.listFilter.add(new SelectItem("3", Utils.getString("suspensionManagmentListFilterConfirmedAmount")));
		this.listFilter.add(new SelectItem("4", Utils.getString("suspensionManagmentListFilterAnnuledAmount")));
	}

	public void sendRefusedItem() {

	}

	public void updateSelectedBySuperAdmin() throws HibernateException, PersistenceBeanException {
		if (this.getActionMotivation() == null || this.getActionMotivation().isEmpty()) {
			return;
		}
		update();
		clearMotivation(null);
		this.setNeedReload(true);
		this.Page_Load();
	}

	public void update() throws HibernateException, PersistenceBeanException {

	}

	public void fillPreviousSuspensionAmount() throws HibernateException, PersistenceBeanException {
		for (CostDefinitions item : getList()) {
			if (item.getPreviousSuspensionAmount() == null) {
				if (item.getValueSuspendSTC() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendSTC());
				} else if (item.getValueSuspendACU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendACU());
				} else if (item.getValueSuspendAGU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendAGU());
				}
			}
			BeansFactory.CostDefinitions().SaveInTransaction(item);
		}
	}

	public void sendRefusedItems() throws HibernateException, PersistenceBeanException {

	}

	private List<CostDefinitions> filterLoadedList(List<CostDefinitions> origlist) {
		List<CostDefinitions> newList = new ArrayList<CostDefinitions>();

		if (this.getSessionBean().isACUM()) {
			for (CostDefinitions item : origlist) {
				if (item.getSuspendedByACU() && item.getSuspendRefusedACU()) {
					item.setCanResend(true);
					item.setCanPerformFinalAction(false);
					item.setCanEditFlow(false);
					newList.add(item);
					continue;
				}
				if (item.getSuspendedByACU() && item.getSuspendConfirmedACU() && !item.getSuspendSecondConfirmedACU()) {
					item.setSecFlowAnnulAvailable(true);
				} else {
					item.setSecFlowAnnulAvailable(false);
				}

				if (item.getSuspendedByACU()) {
					if (item.getSuspendConfirmedACU() && !item.getSuspendSecondConfirmedACU()) {
						item.setCanPerformFinalAction(true);
					} else {
						item.setCanPerformFinalAction(false);
					}
				} else {
					item.setCanPerformFinalAction(false);
				}

				if (item.getSuspendConfirmedACU()) {
					if (!item.getSuspendedByACU() && !item.getSuspendSecondConfirmedAGU()) {
						item.setCanEditFlow(false);
					} else {
						item.setCanEditFlow(true);
					}
					if (item.getSuspendSecondConfirmedACU()) {
						item.setCanEditFlow(false);
					} else if (item.getSuspendSecondConfirmedAGU()) {
						item.setCanEditFlow(true);
					}
					if (item.getSuspendedByACU()) {
						item.setCanEditFlow(false);
					}
				} else {
					if (!item.getSuspendedByACU() && !item.getSuspendConfirmedAGU()) {
						item.setCanEditFlow(false);
					} else {
						item.setCanEditFlow(true);
					}
				}

				newList.add(item);
			}
		} else if (this.getSessionBean().isACU()) {
			for (CostDefinitions item : origlist) {
				item.setSecFlowAnnulAvailable(false);
				item.setCanPerformFinalAction(false);
				item.setCanEditFlow(false);
				item.setCanResend(false);
				newList.add(item);
			}
		}
		if (this.getSessionBean().isSuperAdmin()) {
			return origlist;
		}

		return newList;
	}

	private void saveActionToLog(String actionName) {
		try {
			String logFormat = "%s %s %s %s %s %s %s %s";
			String ip = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
					.getRemoteAddr();
			String mes = String.format(logFormat, this.getSessionBean().getCurrentUser().getName(),
					this.getSessionBean().getCurrentUser().getSurname(),
					this.getSessionBean().getCurrentUser().getRolesNames(), ip, " Eseguita l�azione di ", actionName,
					" in ", new Date());
			InfoLog.getInstance().addMessage(mes);
		} catch (Exception e) {

		}
	}

	private void saveActionToDB(LogActionTypes type) {
		try {
			UserActivityLog userAction = BeansFactory.UserActivityLog()
					.getByUserAndAction(getSessionBean().getCurrentUser().getId(), type.getId());
			if (userAction == null) {
				userAction = new UserActivityLog();
			}
			userAction.setUser(BeansFactory.Users().Load(getSessionBean().getCurrentUser().getId()));
			userAction.setActionText(type.getValue());
			userAction.setActionCode(type.getId());
			userAction.setLastPerfomedDate(new Date());
			BeansFactory.UserActivityLog().SaveInTransaction(userAction);
		} catch (Exception e) {
			log.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity() {
		try {
			CostDefinitions cd = BeansFactory.CostDefinitions().Load(this.getEntityEditId());
			if (cd != null && !cd.getDeleted()) {
				this.getSession().put("suspensionManagementList", this.getEntityEditId());
				this.goTo(PagesTypes.SUSPENSIONMANAGEMENTEDIT);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity() {

	}

	public void doSelectAll() throws PersistenceBeanException {
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAll()) {
			for (CostDefinitions cd : this.getList()) {
				if (cd.getCanSuspend()) {
					cd.setSelected(true);
					listIndices.add(cd.getId());
				}
			}

			this.setSelectNormal(true);
			this.setSelectRefused(false);
		} else {
			this.setSelectNormal(false);
			for (CostDefinitions cd : this.getList()) {
				cd.setSelected(false);
			}
		}

		this.setNeedReload(true);

		this.getViewState().put("suspManListCD", listIndices);
		this.Page_Load();
	}

	public void doSelectAllRefused() throws PersistenceBeanException {
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAllRefused()) {
			for (CostDefinitions cd : this.getList()) {
				if (cd.getCanResend() != null && cd.getCanResend()) {
					cd.setSelectedResend(true);
					listIndices.add(cd.getId());
				}
			}
			this.setSelectNormal(false);
			this.setSelectRefused(true);
		} else {
			this.setSelectRefused(false);
			for (CostDefinitions cd : this.getList()) {
				cd.setSelectedResend(false);
			}
		}

		this.getViewState().put("suspManListCDRefused", listIndices);
		this.Page_Load();
	}

	public void send() throws NumberFormatException, HibernateException, PersistenceBeanException {

	}

	public void refuse() throws NumberFormatException, HibernateException, PersistenceBeanException {

	}

	@SuppressWarnings("unchecked")
	public void annul() throws NumberFormatException, HibernateException, PersistenceBeanException {
		Transaction tr = null;
		tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
		this.setAnnulError(false);
		List<Long> listIndices = null;
		if (this.getViewState().get("suspManListCD") != null) {
			listIndices = (List<Long>) this.getViewState().get("suspManListCD");
		}
		if (listIndices == null) {
			listIndices = new ArrayList<Long>();
		}
		try {
			for (CostDefinitions item : this.getList()) {
				if ((item.isSelectedResend() && !this.getSessionBean().isACUM())
						|| (item.getSelected() && this.getSessionBean().isACUM())) {
					if (item.getSuspendedByACU() && !this.getSessionBean().isACUM()) {
						setAnnulError(true);
						break;
					}

					if (this.getSessionBean().isACUM() && item.getSuspendedByACU()) {
						recalcSumWhenAnnul(item);
						//item.setAcuCertification(item.getAcuCertification() + item.getValueSuspendACU());
						item.setAcuCertification(item.getAcuCertification());
						item.setDateSuspendSTC(null);
						item.setDateSuspendAGU(null);
						item.setDateSuspendACU(null);

						item.setValueSuspendACU(null);
						item.setSuspendedByACU(false);
						item.setSuspendConfirmedSTC(false);
						item.setSuspendConfirmedAGU(false);
						item.setSuspendConfirmedACU(false);
						item.setSuspendRefusedAGU(false);
						item.setSuspendRefusedACU(false);
						item.setSuspendSecondConfirmedSTC(false);
						item.setSuspendSecondConfirmedAGU(false);
						item.setSuspendSecondConfirmedACU(false);
						item.setSuspendSecondRefusedAGU(false);
						item.setSuspendSecondRefusedACU(false);
						item.setAmountToSuspend(null);
						item.setAmountToOut(null);
						item.setSuspensionStatus(null);
						item.setDateSuspensionConfirmedFlow1(null);
						item.setDateSuspensionConfirmedFlow2(null);
						item.setDateSuspensionStartFlow2(null);
						item.setSelected(false);
						listIndices.remove(item.getId());
					}

					try {
						BeansFactory.CostDefinitions().SaveInTransaction(item);
						saveActionToLog(LogActionTypes.Annul.getValue());
						saveActionToDB(LogActionTypes.Annul);
					} catch (HibernateException e) {
						log.error(e);
					} catch (PersistenceBeanException e) {
						log.error(e);
					}
				}
			}

			if (!getAnnulError()) {
				this.setSelectRefused(false);
				this.setSelectAllRefused(false);
				this.setNeedReload(true);
				this.setErrorAmount(false);
				this.setRefuseError(false);
				this.setErrorFinalAction(false);
				this.setNeedReload(true);
				tr.commit();
			} else {
				tr.rollback();
			}
		} catch (Exception e) {
			if (tr != null) {
				tr.rollback();
			}
		}
		this.Page_Load();
	}

	private void recalcSumWhenAnnul(CostDefinitions item) throws HibernateException, PersistenceBeanException {
		Double sum = 0d;

		DurSummaries durSummaries = BeansFactory.DurSummaries().LoadByDurCompilation(item.getDurCompilation().getId());

		if (durSummaries.getTotalAmount() != null) {
			sum += durSummaries.getTotalAmount();
		}

		if (item.getSuspendedByACU() && item.getACUSertified()) {
			sum += item.getValueSuspendACU();
		}

		item.setPreviousSuspensionAmount(null);
		durSummaries.setTotalAmount(sum);

		BeansFactory.DurSummaries().SaveInTransaction((durSummaries));
	}

	@SuppressWarnings("unchecked")
	public void annulSecondFlow() throws HibernateException, PersistenceBeanException {
		Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
		List<Long> listIndices = null;
		if (this.getViewState().get("suspManListCD") != null) {
			listIndices = (List<Long>) this.getViewState().get("suspManListCD");
		}
		if (listIndices == null) {
			listIndices = new ArrayList<Long>();
		}
		for (CostDefinitions item : this.getList()) {
			if (item.getId().equals(this.getEntityAnnulId())) {
				try {
					if (this.getSessionBean().isACUM() && item.getSuspendedByACU()) {
						recalcSumWhenAnnul(item);
						item.setSuspendedByACU(false);
						item.setSuspendConfirmedSTC(false);
						item.setSuspendConfirmedAGU(false);
						item.setSuspendConfirmedACU(false);
						item.setSuspendRefusedAGU(false);
						item.setSuspendRefusedACU(false);
						item.setSuspendSecondConfirmedSTC(false);
						item.setSuspendSecondConfirmedAGU(false);
						item.setSuspendSecondConfirmedACU(false);
						item.setSuspendSecondRefusedAGU(false);
						item.setSuspendSecondRefusedACU(false);
						item.setDateSuspendSTC(null);
						item.setDateSuspendAGU(null);
						item.setDateSuspendACU(null);

						if (item.getAcuCertification() != null) {
							//item.setAcuCertification(item.getAcuCertification() + item.getValueSuspendACU());
							item.setAcuCertification(item.getAcuCertification());
						}

						item.setAmountToSuspend(null);
						item.setAmountToOut(null);
						item.setSuspensionStatus(null);
						item.setDateSuspensionConfirmedFlow1(null);
						item.setDateSuspensionConfirmedFlow2(null);
						item.setDateSuspensionStartFlow2(null);
						// recalcSum(item);
						item.setValueSuspendACU(null);
						item.setSelected(false);
						listIndices.remove(item.getId());
					}
					BeansFactory.CostDefinitions().SaveInTransaction(item);
					saveActionToLog(LogActionTypes.Annul.getValue());
					saveActionToDB(LogActionTypes.Annul);
					tr.commit();
					this.setNeedReload(true);
					this.Page_Load();
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}
			}
		}
	}

	public void finalAction() throws HibernateException, PersistenceBeanException {

	}

	public void retreatItem() {
		retreatItem(false);
	}

	public void retreatItemWithDocument() {
		retreatItem(true);
	}

	public void retreatItem(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				this.saveRetreatDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		for (CostDefinitions item : this.getList()) {
			if (item.getSelected()) {
				Double value = item.getValueSuspendACU();
				if (value == null) {
					value = Double.parseDouble(this.getRetreatAmount());
				}
				double certValue = 0d;

				if (item.getAcuCertification() != null) {
					certValue = item.getAguCertification();
				}

				if (value > certValue) {
					continue;
				}

				item.setRetreatDate(this.getRetreatDate());
				item.setRetreatActNumber(this.getRetreatActNumber());
				item.setRetreatActDate(this.getRetreatActDate());
				item.setValueSuspendACU(value);
				item.setRectifiedByACU(Boolean.TRUE);
				item.setSelctedRetreatReason(this.getSelctedRetreatReason());
				item.setRetreatNote(this.getRetreatNote());
				item.setSuspensionStatus(CostDefinitionSuspendStatus.RETREAT.getState());

				if (needSaveDocument) {
					if (getRetreatDocument() != null) {
						try {
							BeansFactory.Documents().Save(getRetreatDocument());
						} catch (Exception e) {
							log.error(e);
							e.printStackTrace();
						}
					}

					item.setCancelSuspendDocument(getRetreatDocument());
				}

				try {
					Transaction tr = null;
					tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					if (PersistenceSessionManager.getBean().getSession().contains(item)) {
						PersistenceSessionManager.getBean().getSession().merge(item);
					} else {
						PersistenceSessionManager.getBean().getSession().saveOrUpdate(item);
					}
					tr.commit();
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isRetreatAmount() {
		for (CostDefinitions item : this.getList()) {
			if (item.getSelected() && item.getValueSuspendACU() == null) {
				return true;
			}
		}
		return false;
	}

	public void suspendItem() {
		suspendItem(false);
	}

	public void suspendItemWithDocument() {
		suspendItem(true);
	}

	private void suspendItem(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				this.saveCancelSuspendDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		for (CostDefinitions item : this.getList()) {
			if (item.getSelected()) {
				item.setCancelSuspendActNumber(getCancelActNumber());
				item.setCancelSuspendActDate(getCancelDate());
				item.setCancelSuspendReason(getCancelReason());
				item.setCancelSuspendNote(getCancelNote());
				item.setSuspensionStatus(CostDefinitionSuspendStatus.CANCEL_SUSPEND.getState());

				if (needSaveDocument) {
					if (getCancelSuspendDocument() != null) {
						try {
							BeansFactory.Documents().Save(getCancelSuspendDocument());
						} catch (Exception e) {
							log.error(e);
							e.printStackTrace();
						}
					}

					item.setCancelSuspendDocument(getCancelSuspendDocument());
				}

				try {
					Transaction tr = null;
					tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					if (PersistenceSessionManager.getBean().getSession().contains(item)) {
						PersistenceSessionManager.getBean().getSession().merge(item);
					} else {
						PersistenceSessionManager.getBean().getSession().saveOrUpdate(item);
					}
					tr.commit();
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}
		}
	}

	private void fillRoles() throws PersistenceBeanException {
		this.listSelectRoles = new ArrayList<SelectItem>();

		this.listSelectRoles.add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();
		for (UserRoles userRole : UserRolesBean.GetByUser(String.valueOf(this.getCurrentUser().getId()))) {
			if (userRole.getRole().getCode().equals("DAEC")) {
				continue;
			}
			if (temp.add(userRole.getRole().getCode())) {
				UploadDocumentRoleType roleType = UploadDocumentRoleType.getByName(userRole.getRole().getCode());
				if (roleType != null) {
					this.getListSelectRoles()
							.add(new SelectItem(String.valueOf(roleType.getId()), roleType.getDisplayName()));
				}
			}
		}
	}

	public String getProjectId() {
		if (this.getSession().get("project") != null) {
			return String.valueOf(this.getSession().get("project"));
		} else {
			return null;
		}
	}

	private void fillPartners() throws HibernateException, PersistenceBeanException {
		listPartners = new ArrayList<SelectItem>();

		List<CFPartnerAnagraphs> partners = new ArrayList<CFPartnerAnagraphs>();

		if (this.getSessionBean().isCIL()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByCIL(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					for (CFPartnerAnagraphs item : partnersLoc) {
						CFPartnerAnagraphProject cfpap = BeansFactory.CFPartnerAnagraphProject()
								.LoadByPartner(Long.valueOf(this.getProjectId()), item.getId());
						if (cfpap != null && cfpap.getRemovedFromProject()) {
							continue;
						}

						partners.add(item);
					}

				}
			}
		}
		if (this.getSessionBean().isDAEC()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByDAEC(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					partners.addAll(partnersLoc);
				}
			}
		}
		if (this.getSessionBean().isAAU()) {
			partners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
		}

		if (!partners.isEmpty()) {
			listPartners.add(SelectItemHelper.getFirst());
		}

		for (CFPartnerAnagraphs item : partners) {
			StringBuilder sb = new StringBuilder();
			SelectItem selectItem = new SelectItem();

			if (item.getUser() == null) {
				sb.append(item.getName());
			} else {
				sb.append(item.getUser().getName());
				sb.append(item.getUser().getSurname());
			}

			selectItem.setValue(String.valueOf(item.getId()));
			selectItem.setLabel(sb.toString());

			listPartners.add(selectItem);
		}
	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	public void saveRetreatDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getRetreatDocumentUpFile() != null && this.getRetreatDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getRetreatDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getRetreatDocumentUpFile().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(this.getRetreatDocument().getDocumentDate(),
						this.getRetreatDocumentUpFile().getName(), fileName,
						this.getRetreatDocument().getProtocolNumber(), cat, this.getRetreatDocument().getIsPublic(), null);

				this.getRetreatDocument().setFileName(fileName);
				this.getRetreatDocument().setName(this.getRetreatDocumentUpFile().getName());
				this.getRetreatDocument().setCategory(cat);
				this.getRetreatDocument().setUser(this.getCurrentUser());
				this.getRetreatDocument().setProject(this.getProject());
				this.getRetreatDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("suspendDocument", doc);
				this.setSelectedSuspendPartner(null);
				this.setSuspendRole(null);
				this.setSelectedCategory(null);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	public void saveCancelSuspendDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getSuspendDocumentUpFile() != null && this.getCancelSuspendDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getSuspendDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getSuspendDocumentUpFile().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(this.getCancelSuspendDocument().getDocumentDate(),
						this.getSuspendDocumentUpFile().getName(), fileName,
						this.getCancelSuspendDocument().getProtocolNumber(), cat,
						this.getCancelSuspendDocument().getIsPublic(), null);

				this.getCancelSuspendDocument().setFileName(fileName);
				this.getCancelSuspendDocument().setName(this.getSuspendDocumentUpFile().getName());
				this.getCancelSuspendDocument().setCategory(cat);
				this.getCancelSuspendDocument().setUser(this.getCurrentUser());
				this.getCancelSuspendDocument().setProject(this.getProject());
				this.getCancelSuspendDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("suspendDocument", doc);
				this.setSelectedSuspendPartner(null);
				this.setSuspendRole(null);
				this.setSelectedCategory(null);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	public Projects getProject()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException {
		if (this.getSession().get("PAAMSN_Session_project_storage") == null
				|| !this.getSession().get("stored_project_id").toString().equals(this.getProjectId())) {
			if (this.getProjectId() == null) {
				return null;
			}

			this.getSession().put("PAAMSN_Session_project_storage", BeansFactory.Projects().Load(this.getProjectId()));
			this.getSession().put("stored_project_id", this.getProjectId());
		}

		return (Projects) this.getSession().get("PAAMSN_Session_project_storage");
	}

	@SuppressWarnings("unchecked")
	private void recheckCostItems(List<CostDefinitions> list)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("suspManListCD") != null) {
			List<Long> listIndices = (List<Long>) this.getViewState().get("suspManListCD");

			for (CostDefinitions cd : list) {
				if (listIndices.contains(cd.getId())) {
					cd.setSelected(true);
				} else {
					cd.setSelected(false);
				}
			}
		}
	}

	public void doSelectItem() throws PersistenceBeanException {
		this.setSelectRefused(false);
		this.setSelectNormal(true);
		this.setSelectAll(false);
		List<Long> listIndices = new ArrayList<Long>();
		int counter = 0;
		for (CostDefinitions cd : this.getList()) {
			if (cd.getSelected()) {
				listIndices.add(cd.getId());
				++counter;
			}
		}

		if (counter == 0) {
			this.setSelectNormal(false);
		}

		this.getViewState().put("suspManListCD", listIndices);
		this.setListCostDefinitionsWeb(this.getList());
		this.Page_Load();
	}

	public void doSelectItemRefused() throws PersistenceBeanException {
		this.setSelectRefused(true);
		this.setSelectNormal(false);
		this.setSelectAllRefused(false);
		int counter = 0;
		List<Long> listIndices = new ArrayList<Long>();
		for (CostDefinitions cd : this.getList()) {
			if (cd.isSelectedResend()) {
				listIndices.add(cd.getId());
				++counter;
			}
		}
		if (counter == 0) {
			this.setSelectRefused(false);
		}

		this.getViewState().put("suspManListCDRefused", listIndices);
		this.setListCostDefinitionsWeb(this.getList());
		this.Page_Load();
	}

	public void checkCanSelect() {
		this.setCanSelectAll(false);
		this.setCanSelectAllResend(false);
		for (CostDefinitions item : this.getList()) {
			if (Boolean.TRUE.equals(item.getCanEditFlow())) {
				this.setCanSelectAll(true);
			}
			if (Boolean.TRUE.equals(item.getCanResend())) {
				this.setCanSelectAllResend(true);
			}
		}
	}

	public void setSelectAll(boolean selectAll) {
		this.getViewState().put("suspManEditSelectAll", selectAll);
	}

	public boolean isSelectAll() {
		return Boolean.valueOf(String.valueOf(this.getViewState().get("suspManEditSelectAll")));
	}

	public void setSelectAllRefused(boolean selectAll) {
		this.getViewState().put("suspManEditSelectAllRefused", selectAll);
	}

	public boolean isSelectAllRefused() {
		return Boolean.valueOf(String.valueOf(this.getViewState().get("suspManEditSelectAllRefused")));
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getListCostDefinitionsWeb() {
		if (this.getViewState().containsKey("listCostDefinitionsWeb")) {
			return (List<CostDefinitions>) getViewState().get("listCostDefinitionsWeb");
		}
		return null;
	}

	public void setListCostDefinitionsWeb(List<CostDefinitions> listCostDefinitionsWeb) {
		this.getViewState().put("listCostDefinitionsWeb", listCostDefinitionsWeb);
	}

	public Boolean getErrorAmount() {
		if (this.getViewState().containsKey("errorAmount")) {
			return (Boolean) this.getViewState().get("errorAmount");
		}
		return false;
	}

	public void setErrorFinalAction(Boolean finalAction) {
		this.getViewState().put("errorFinalAction", finalAction);
	}

	public Boolean getErrorFinalAction() {
		if (this.getViewState().containsKey("errorFinalAction")) {
			return (Boolean) this.getViewState().get("errorFinalAction");
		}
		return false;
	}

	public void setErrorAmount(Boolean errorAmount) {
		this.getViewState().put("errorAmount", errorAmount);
	}

	public Boolean getNeedReload() {
		if (this.getViewState().containsKey("needReload")) {
			return (Boolean) this.getViewState().get("needReload");
		}
		return false;
	}

	public void setNeedReload(Boolean needReload) {
		this.getViewState().put("needReload", needReload);
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public void setRefuseError(Boolean refuseError) {
		this.getViewState().put("refuseError", refuseError);
	}

	public Boolean getRefuseError() {
		if (this.getViewState().containsKey("refuseError")) {
			return (Boolean) this.getViewState().get("refuseError");
		}
		return false;
	}

	public void setAnnulError(Boolean annulError) {
		this.getViewState().put("annulError", annulError);
	}

	public Boolean getAnnulError() {
		if (this.getViewState().containsKey("annulError")) {
			return (Boolean) this.getViewState().get("annulError");
		}
		return false;
	}

	public void setFilter(String filter) throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().containsKey("suspensionManagmentListFilter")) {
			if (!this.getFilter().equals(filter)) {
				this.getViewState().put("suspensionManagmentListFilter", filter);
				this.setNeedReload(true);
				this.Page_Load();
			}
		} else {
			this.getViewState().put("suspensionManagmentListFilter", filter);
			this.setNeedReload(true);
			this.Page_Load();
		}
	}

	public String getFilter() {
		if (this.getViewState().get("suspensionManagmentListFilter") != null) {
			return String.valueOf(this.getViewState().get("suspensionManagmentListFilter"));
		} else {
			return null;
		}
	}

	public List<SelectItem> getListFilter() {
		return listFilter;
	}

	public void setListFilter(List<SelectItem> listFilter) {
		this.listFilter = listFilter;
	}

	public Long getEntitySendId() {
		return entitySendId;
	}

	public void setEntitySendId(Long entitySendId) {
		SuspensionManagementListBean.entitySendId = entitySendId;
	}

	public Date getFinalActionDate() {
		return finalActionDate;
	}

	public void setFinalActionDate(Date finalActionDate) {
		this.finalActionDate = finalActionDate;
	}

	public Long getEntityAnnulId() {
		return entityAnnulId;
	}

	public void setEntityAnnulId(Long entityAnnulId) {
		SuspensionManagementListBean.entityAnnulId = entityAnnulId;
	}

	public Boolean getSelectNormal() {
		if (this.getViewState().containsKey("selectNormal")) {
			return (Boolean) this.getViewState().get("selectNormal");
		}
		return false;
	}

	public void setSelectNormal(Boolean selectNormal) {
		this.getViewState().put("selectNormal", selectNormal);
	}

	public Boolean getSelectRefused() {
		if (this.getViewState().containsKey("selectRefused")) {
			return (Boolean) this.getViewState().get("selectRefused");
		}
		return false;
	}

	public void setSelectRefused(Boolean selectRefused) {
		this.getViewState().put("selectRefused", selectRefused);
	}

	public Date getResendDate() {
		return resendDate;
	}

	public void setResendDate(Date resendDate) {
		this.resendDate = resendDate;
	}

	public Boolean getCanSelectAll() {
		return canSelectAll;
	}

	public void setCanSelectAll(Boolean canSelectAll) {
		this.canSelectAll = canSelectAll;
	}

	public Boolean getCanSelectAllResend() {
		return canSelectAllResend;
	}

	public void setCanSelectAllResend(Boolean canSelectAllResend) {
		this.canSelectAllResend = canSelectAllResend;
	}

	public HtmlDataScroller getScroller() {
		return scroller;
	}

	public void setScroller(HtmlDataScroller scroller) {
		this.scroller = scroller;
	}

	public Long getSelectedSave() {
		return (Long) this.getViewState().get("selectedSave");
	}

	public void setSelectedSave(Long selectedSave) {
		this.getViewState().put("selectedSave", selectedSave);
	}

	public String getActionMotivation() {
		return actionMotivation;
	}

	public void setActionMotivation(String actionMotivation) {
		this.actionMotivation = actionMotivation;
	}

	public void clearMotivation(ActionEvent event) {
		this.setActionMotivation("");
	}

	public String getCancelActNumber() {
		return cancelActNumber;
	}

	public void setCancelActNumber(String cancelActNumber) {
		this.cancelActNumber = cancelActNumber;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Documents getCancelSuspendDocument() {
		return cancelSuspendDocument;
	}

	public void setCancelSuspendDocument(Documents cancelSuspendDocument) {
		this.cancelSuspendDocument = cancelSuspendDocument;
	}

	public String getSuspendRole() {
		return (String) this.getViewState().get("selectedSuspendRole");
	}

	public void setSuspendRole(String entity) {
		this.getViewState().put("selectedSuspendRole", entity);
	}

	public String getRetreatRole() {
		return (String) this.getViewState().get("selectedRetreatRole");
	}

	public void setRetreatRole(String entity) {
		this.getViewState().put("selectedRetreatRole", entity);
	}

	public List<SelectItem> getListSelectRoles() {
		return listSelectRoles;
	}

	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		this.listSelectRoles = listSelectRoles;
	}

	public void setSelectedSuspendPartner(String selectedPartner) {
		this.getViewState().put("selectedSuspendPartner", selectedPartner);
	}

	public String getSelectedSuspendPartner() {
		return (String) this.getViewState().get("selectedSuspendPartner");
	}

	public void setSelectedRetreatPartner(String selectedPartner) {
		this.getViewState().put("selectedRetreatPartner", selectedPartner);
	}

	public String getSelectedRetreatPartner() {
		return (String) this.getViewState().get("selectedRetreatPartner");
	}

	public List<SelectItem> getListPartners() {
		return listPartners;
	}

	public void setListPartners(List<SelectItem> listPartners) {
		this.listPartners = listPartners;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<SelectItem> getCategories() {
		return categories;
	}

	public void setCategories(List<SelectItem> categories) {
		this.categories = categories;
	}

	public UploadedFile getSuspendDocumentUpFile() {
		return suspendDocumentUpFile;
	}

	public void setSuspendDocumentUpFile(UploadedFile suspendDocumentUpFile) {
		this.suspendDocumentUpFile = suspendDocumentUpFile;
	}

	public Date getRetreatDate() {
		return retreatDate;
	}

	public void setRetreatDate(Date retreatDate) {
		this.retreatDate = retreatDate;
	}

	public String getRetreatActNumber() {
		return retreatActNumber;
	}

	public void setRetreatActNumber(String retreatActNumber) {
		this.retreatActNumber = retreatActNumber;
	}

	public Date getRetreatActDate() {
		return retreatActDate;
	}

	public void setRetreatActDate(Date retreatActDate) {
		this.retreatActDate = retreatActDate;
	}

	public String getRetreatAmount() {
		return retreatAmount;
	}

	public void setRetreatAmount(String retreatAmount) {
		this.retreatAmount = retreatAmount;
	}

	public Long getSelctedRetreatReason() {
		return selctedRetreatReason;
	}

	public void setSelctedRetreatReason(Long selctedRetreatReason) {
		this.selctedRetreatReason = selctedRetreatReason;
	}

	public String getRetreatNote() {
		return retreatNote;
	}

	public void setRetreatNote(String retreatNote) {
		this.retreatNote = retreatNote;
	}

	public Documents getRetreatDocument() {
		return retreatDocument;
	}

	public void setRetreatDocument(Documents retreatDocument) {
		this.retreatDocument = retreatDocument;
	}

	public UploadedFile getRetreatDocumentUpFile() {
		return retreatDocumentUpFile;
	}

	public void setRetreatDocumentUpFile(UploadedFile retreatDocumentUpFile) {
		this.retreatDocumentUpFile = retreatDocumentUpFile;
	}

	public List<SelectItem> getAllReasonTypes() {
		return allReasonTypes;
	}

	public void setAllReasonTypes(List<SelectItem> allReasonTypes) {
		this.allReasonTypes = allReasonTypes;
	}

}
