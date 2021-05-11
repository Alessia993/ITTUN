package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.ControllerTypes;
import com.infostroy.paamns.common.enums.CostDefinitionSuspendStatus;
import com.infostroy.paamns.common.enums.CostItemTypes;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PaymentApplicationStatusTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.ReasonType;
import com.infostroy.paamns.common.enums.RecoverActType;
import com.infostroy.paamns.common.enums.RecoverReasonType;
import com.infostroy.paamns.common.enums.RequestType;
import com.infostroy.paamns.common.enums.SuspensionReasonTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.SimpleMoneyRow;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3;
import com.infostroy.paamns.persistence.beans.entities.domain.CostManagement;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PaymentApplication;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityEditBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsWrapper;
import com.infostroy.paamns.web.beans.wrappers.CostItemSummaryWrapper;
import com.infostroy.paamns.web.beans.wrappers.CustomPaginator;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class DURCompilationEditBean extends EntityEditBean<Entity> {
	enum SuspendedCostType {
		ALL("costSuspensionAll", 1l), TRUE("costSuspensionTrue", 2l), FALSE("costSuspensionFalse", 3l);

		private String value;

		private Long id;

		private SuspendedCostType(String value, Long id) {
			this.setValue(value);
			this.setId(id);
		}

		/**
		 * Sets value
		 * 
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Gets value
		 * 
		 * @return value the value
		 */
		public String getValue() {
			return Utils.getString(value);
		}

		/**
		 * Sets id
		 * 
		 * @param id
		 *            the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * Gets id
		 * 
		 * @return id the id
		 */
		public Long getId() {
			return id;
		}
	}

	public final static String FILTER_PARTNER_VALUE = "FILTER_PARTNER_VALUE";

	public final static String FILTER_PHASES_VALUE = "FILTER_PHASES_VALUE";

	public final static String FILTER_COST_DEF_ID_VALUE = "FILTER_COST_DEF_ID_VALUE";

	public final static String FILTER_COST_ITEM = "FILTER_COST_ITEM";

	public final static String DUR_EDIT_FROM_VIEW = "DUR_EDIT_FROM_VIEW";

	public final static String CAN_EDIT_FROM_VIEW = "CAN_EDIT_FROM_VIEW";

	public final static String DUR_COMPILATION_EDIT_SAVED_IPP = "DUR_COMPILATION_EDIT_SAVED_IPP";

	public final static String DUR_COMPILATION_EDIT_SAVED_PN = "DUR_COMPILATION_EDIT_SAVED_PN";

	public final static String DUR_COMPILATION_SAVE_FILTERS = "DUR_COMPILATION_SAVE_FILTERS";

	private List<SelectItem> suspensionTypes;

	private Long entityEditId;

	private int selectedIndex;

	private static Boolean showScroll = false;

	private static String itemsPerPage;

	private static List<SelectItem> itemsPerPageList;

	// ///////////////////////////////////

	private DurCompilations durCompilation;

	private DurSummaries durSummaries;

	private Documents bDocument;

	private Documents cDocument;

	private Documents ucDocument;

	private Documents adcDocument;

	private Documents suspendDocument;

	private Documents adgDocument;

	private UploadedFile bDocumentUpFile;

	private UploadedFile cDocumentUpFile;

	private UploadedFile ucDocumentUpFile;

	private UploadedFile adcDocumentUpFile;

	private UploadedFile adgDocumentUpFile;

	private UploadedFile suspendDocumentUpFile;

	private boolean editMode;

	private boolean certified;

	private List<CostDefinitions> listCostDefinitionsWeb;

	private boolean CFMode;

	private boolean STCMode;

	private boolean UCMode;

	private boolean AGUMode;

	private boolean ACUMode;

	private boolean CFAvailable;

	private boolean UCCertAvailable;

	private boolean STCCertAvailable;

	private boolean AGUCertAvailable;

	private boolean ACUCertAvailable;

	private boolean certifiableAvailable;

	private boolean certifiableACUAGUAvailable;

	@SuppressWarnings("unused")
	private boolean selectAll;

	private boolean nonRegularSituation;

	private String nonRegType;

	private List<SelectItem> listNonRegularTypesWeb;

	// Edit fields variables
	// ////////////////////////////////////////////////////////////////////

	private List<CFPartnerAnagraphs> listInvolvedPartners;

	private List<SelectItem> listInvolvedPartnersWeb;

	private List<String> selectedPartners;

	private String selectedPartner2;

	private String involvedPartner;

	private String docB;

	private String docC;

	private String docUC;

	private String docADC;

	private String docADG;

	private String docSuspend;

	private String docDurRecovery;

	private Double totalAmountDur;

	private Double totalAmountDur2;

	private Double totalAmountOld;

	private Double fesrReimbursementAmount;

	private Double fesrReimbursementAmount2;

	private Double itCnReimbursement;

	private Double itCnReimbursement2;

	private Double frCnReimbursement;

	private Double frCnReimbursement2;

	private Double totalAmountEligibleExpenditure;

	private Double totalAmountPublicExpenditure;

	private Double totalAmountPrivateExpenditure;

	private Double totalAmountStateAid;

	private Double totalAmountStateAidAdvanceCover;

	private Double totalContributionInKindToOperatoinPublicShare;

	private Double totalReimbursement;

	private Boolean userFesrAmount;

	private Boolean userItCnReimbursementAmount;

	private Boolean userFrCnReimbursementAmount;

	// ////////////////////////////////////////////////////////////////////

	private List<CostItemSummaryWrapper> listSummary;

	private List<CostItemSummaryWrapper> listSummaryWp;

	private List<CostItemSummaryWrapper> listSummaryPartner;

	private List<CostItemSummaryWrapper> listSummaryAsse3;

	private List<CostDefinitions> listCostTable;

	private Map<Long, CostDefinitionsWrapper> costWrapers;

	private String spanWarningVisibility;

	private boolean canDenyRows;

	private boolean canSaveAmount;

	private List<SelectItem> listSelectRoles;

	private String suspendItemId;

	private String suspendAmount;

	private String suspendPercent;

	private Date suspendDate;

	private Date suspendActDate;

	private String suspendActNumber;

	private Boolean suspAmountError;

	private List<SelectItem> suspensionReasons;

	private String rectificationItemId;

	private String durRecoveryItemId;

	private String rectificationAmount;

	private Date rectificationDate;

	private Boolean rectificationAmountError;

	private Boolean durRecoveryAmountError;

	private String showSuspSaveMes;

	private String selectedSuspendIds;

	private String selectedRetreatIds;

	private String selectedDurRecoveryIds;

	private String selectedRecoveryIds;

	private List<SelectItem> allReasonTypes;

	private List<SelectItem> filterPartnerValues;

	private List<SelectItem> filterPhases;

	private List<SelectItem> filterCostItemValues;

	private List<SelectItem> filterPartnersForGrid;

	private List<SelectItem> filterPartnersForGridWp;

	private List<CostDefinitionsToNotes> notesList;

	private static List<SelectItem> categories;

	private List<SelectItem> listPartners;

	private String selectedCategory;

	private String actionMotivation;

	private List<CostDefinitions> pageCosts;

	private boolean needReload;

	private List<CFPartnerAnagraphs> partnersForProject;

	private CostItemSummaryWrapper cfSummaryWrapper;

	private CostItemSummaryWrapper cfSummaryWrapperWp;

	private CostItemSummaryWrapper cfSummaryWrapperPartner;

	private CostItemSummaryWrapper totalWrapper;

	private CostItemSummaryWrapper cnItTotalWrapper;

	private CostItemSummaryWrapper cnFrTotalWrapper;

	private Date retreatDate;

	private Date durRecoveryDate;

	private String retreatActNumber;

	private String durRecoveryActNumber;

	private Date retreatActDate;

	private Date durRecoveryActDate;

	private String retreatAmount;

	private String durRecoveryAmount;

	private String durAmountOfPublicSupport;

	private String durTotalEligibleCost;

	private Long selctedRetreatReason;

	private Long selctedRetreatController;

	private Long selctedSuspendController;

	private Long selctedDurRecoveryReason;

	private Long selctedDurRecoveryActType;

	private String retreatNote;

	private String durRecoveryNote;

	private Documents retreatDocument;

	private Documents durRecoveryDocument;

	private UploadedFile retreatDocumentUpFile;

	private UploadedFile durRecoveryDocumentUpFile;

	private List<SelectItem> allRetReasonTypes;

	private List<SelectItem> allRetControllerTypes;

	private List<SelectItem> allSuspControllerTypes;

	private List<SelectItem> allDurRecoveryReasonTypes;

	private List<SelectItem> allDurRecoveryActTypes;

	private String suspendRetreat;

	private String durRecoverySuspendRetreat;

	private String error;

	private boolean suspendedRetreatOrRecoveredDur;
	
	private boolean suspendedDur;
	
	private boolean retreatDur;
	
	private boolean recoveredDur;
	
	private Double totalSuspeded;
	
	private Double totalRetreat;
	
	private Double totalRecovered;

	@Override
	public void AfterSave() {

		try {
			RecalculateAmounts();
		} catch (Exception e) {
			log.error(e);
		}

		this.GoBack();
	}

	@Override
	public void GoBack() {
		this.getSession().put(DUR_EDIT_FROM_VIEW, false);
		this.getSession().put(FILTER_PARTNER_VALUE, null);
		this.getSession().put(FILTER_PHASES_VALUE, null);
		this.getSession().put(FILTER_COST_DEF_ID_VALUE, null);
		this.getSession().put(FILTER_COST_ITEM, null);
		this.getSession().put(CAN_EDIT_FROM_VIEW, null);

		if (this.getSession().get("durCompilationEditBack") != null) {
			boolean value = Boolean.valueOf(String.valueOf(this.getSession().get("durCompilationEditBack")));
			if (value) {
				if (this.getSession().get("expenditureDeclaration") != null) {
					boolean valueED = Boolean.valueOf(String.valueOf(this.getSession().get("expenditureDeclaration")));
					if (valueED) {
						this.getSession().put("expenditureDeclaration", null);
						this.goTo(PagesTypes.EXPENDITUREDECLARATIONEDIT);
					}
				} else if (this.getSession().get("paymentApplication") != null) {

					boolean valuePA = Boolean.valueOf(String.valueOf(this.getSession().get("paymentApplication")));
					if (valuePA) {
						this.getSession().put("paymentApplication", null);
						this.goTo(PagesTypes.PAYMENTAPPLICATIONEDIT);
					}
				} else if (this.getSession().get("costCertification") != null) {
					boolean valueCC = Boolean.valueOf(String.valueOf(this.getSession().get("costCertification")));
					if (valueCC) {
						this.getSession().put("costCertification", null);
						this.goTo(PagesTypes.COSTCERTIFICATIONLIST);
					}
				} else {
					this.goTo(PagesTypes.DURCOMPILATIONLIST);
				}

			} else {
				this.goTo(PagesTypes.COSTCERTIFICATIONLIST);
			}
		}
	}

	public void saveManageEntity() throws HibernateException, PersistenceBeanException {
		boolean isValid = false;
		for (CostDefinitions cost : this.getListCostTable()) {
			if (cost.getId().equals(this.getEntityManageId())) {
				if (cost.getSuspensionAmount() != null && !cost.getSuspensionAmount().toString().isEmpty()) {
					if (cost.getAcuCertification() != null && cost.getAcuCertification() > 0
							&& cost.getACUSertified()) {
						isValid = cost.getSuspensionAmount() <= cost.getAcuCertification();
					} else if (cost.getAguCertification() != null && cost.getAguCertification() > 0
							&& cost.getAGUSertified()) {
						isValid = cost.getSuspensionAmount() <= cost.getAguCertification();
					} else if (cost.getStcCertification() != null && cost.getStcCertification() > 0
							&& cost.getSTCSertified()) {
						isValid = cost.getSuspensionAmount() <= cost.getStcCertification();
					} else if (cost.getCfCheck() != null && cost.getCfCheck() > 0) {
						isValid = cost.getSuspensionAmount() <= cost.getCfCheck();
					} else if (cost.getCilCheck() != null && cost.getCilCheck() > 0
							&& !cost.getCilIntermediateStepSave()) {
						isValid = cost.getSuspensionAmount() <= cost.getCilCheck();
					} else {
						isValid = cost.getSuspensionAmount() <= cost.getAmountReport();
					}

					if (!isValid) {
						cost.setTitle1(Utils.getString("costSuspenseAmountLessThenCertifiedAmount"));
						cost.setStyle1("background: #fadacf");
						break;
					}

					cost.setSuspensionDate(new Date());
					BeansFactory.CostDefinitions().Save(cost, getDurCompilation());
					this.setEntityManageId(null);
					ValidatorBean.setStaticTabMessage(Utils.getString("costSuspenceWarning"));
					reCulcTotalAmount();
				}
			}
		}
		this.selectedIndex = 2;

	}

	public void search() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.selectedIndex = 2;
		needReload = true;
		Page_Load();
		recalcSum();
	}

	public void clear() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setCostAmountCIL(null);
		this.setCostAmountPayment(null);
		this.setCostAmountReport(null);
		this.setCostNumber(null);
		this.setCostSuspensionType(null);
		this.selectedIndex = 2;
		Page_Load();
	}

	public void clearFilters() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setFilterPartner(null);
		this.setFilterPhase(null);
		this.setFilterCostItem(null);
		this.setFilterCostDefID(null);
		this.selectedIndex = 2;
		Page_Load();
		recalcSum();
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

	private void FillZoneCurrentTotal() throws PersistenceBeanException {
		double fesr = 1d;
		BeansFactory.BudgetInputSourceDivided();
		BudgetInputSourceDivided source = BudgetInputSourceDividedBean.GetByProject(this.getProjectId()).get(0);
		if (source != null && source.getFesr() != null) {
			fesr = source.getFesr();
		}

		if (getDurCompilation() != null) {
			this.setZoneCurrentTotal(getDurCompilation().getZoneCurrentTotal());
		}

		this.setZoneCurrentTotalFesr(
				(this.getZoneCurrentTotal() != null ? this.getZoneCurrentTotal() : 0) / fesr * 100);

		List<DurCompilations> durs = BeansFactory.DurCompilations().LoadByProject(Long.parseLong(this.getProjectId()),
				null);
		double zoneCurrentTotalAll = 0d;
		if (this.getZoneCurrentTotal() != null) {
			zoneCurrentTotalAll += this.getZoneCurrentTotal();
		}
		for (DurCompilations dur : durs) {
			if (this.getDurCompilation() != null && dur.getId().equals(this.getDurCompilation().getId())) {
				continue;
			}

			if (dur.getZoneCurrentTotal() != null) {
				zoneCurrentTotalAll += dur.getZoneCurrentTotal();
			}

		}
		this.setZoneAllTotal(zoneCurrentTotalAll);

		this.setZoneAllTotalFesr((this.getZoneAllTotal() != null ? this.getZoneAllTotal() : 0) / fesr * 100);
	}

	public void suspendItemCancel() {
		if (this.getSuspendItemId() == null || this.getSuspendItemId().isEmpty()) {
			return;
		}

		Long id = Long.parseLong(this.getSuspendItemId());

		if (this.getSuspendAmount().isEmpty()) {
			this.setSuspendAmount("0");
		}

		for (CostDefinitions item : this.getListCostTable()) {

			if (item.getId().equals(id)) {
				for (UserRoles ur : this.getCurrentUser().getRoles()) {
					boolean stopFor = false;

					if (ur.getRole().getCode().equals(UserRoleTypes.ACU.getValue())) {
						item.setSuspendedByACU(false);
						stopFor = true;
					}

					if (stopFor) {
						break;
					}
				}
				break;
			}
		}
		setSelectedIndex(2);

	}

	public void suspendItems() {
		suspendItems(false);
	}

	public void suspendItemsWithDocument() {
		suspendItems(true);
	}

	public void suspendItems(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				saveSuspendDocument();
			}
		} catch (Exception e1) {
			log.error(e1);
			e1.printStackTrace();
		}

		String selectedIds[] = getSelectedSuspendIds().split(",");

		for (CostDefinitions item : this.getListCostTable()) {
			if (Arrays.asList(selectedIds).contains(item.getId().toString())) {
				double value = Double.parseDouble(this.getSuspendPercent());

				double certValue = 100d;

				if (value > certValue) {
					this.setSuspAmountError(true);
					return;
				}

				if (this.getCostSuspensionNote() != null && this.getCostSuspensionNote().length() > 254) {
					this.setCostSuspensionNote(this.getCostSuspensionNote().substring(0, 254));
				}

				if (this.getSessionBean().isACU()) {
					/*
					 * if (item.getAcuCertification() != null &&
					 * item.getAcuCertification() < Double
					 * .parseDouble(this.getSuspendAmount())) {
					 * this.setSuspAmountError(true); } else
					 */
					{
						item.setSuspendedByACU(true);
						item.setSuspendPercent(value);
						item.setValueSuspendACU(
								NumberHelper.CurrencyFormat((item.getAcuCertification() * value) / certValue));
						item.setDateSuspendACU(this.getSuspendDate());
						item.setAcuCertification(
								NumberHelper.CurrencyFormat(item.getAcuCertification() - item.getValueSuspendACU()));
//						item.setAcuCertification(NumberHelper.CurrencyFormat(item.getAcuCertification()));

						item.setSuspendConfirmedSTC(false);
						item.setSuspendConfirmedACU(false);
						item.setSuspendConfirmedAGU(false);
						item.setSuspendSecondConfirmedSTC(false);
						item.setSuspendSecondConfirmedACU(false);
						item.setSuspendSecondConfirmedAGU(false);
						item.setSuspendRefusedAGU(false);
						item.setSuspendRefusedACU(false);
						item.setSuspendSecondRefusedAGU(false);
						item.setSuspendSecondRefusedACU(false);
						item.setSuspensionStatus(CostDefinitionSuspendStatus.SUSPENDED.getState());
						item.setSuspendNote(this.getCostSuspensionNote());
						item.setSuspendReasonId(this.getSelectedSuspendReason());
						item.setSuspendActDate(getSuspendActDate());
						item.setSuspendActNumber(getSuspendActNumber());
						item.setAcuTotalCertification(item.getAcuCertification());
						if (needSaveDocument) {
							item.setSuspendDocument(getSuspendDocument());
						}
					}
				}

				if (item.getValueSuspendSTC() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendSTC());
				} else if (item.getValueSuspendACU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendACU());
				} else if (item.getValueSuspendAGU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendAGU());
				}
			}

		}

		this.setSuspendAmount(null);
		this.setCostSuspensionNote(null);
		this.setSuspendActDate(null);
		this.setSuspendActNumber(null);
		this.setSelectedSuspendReason(0l);
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
				saveSuspendDocument();
			}
		} catch (Exception e1) {
			log.error(e1);
			e1.printStackTrace();
		}

		Long id = Long.parseLong(this.getSuspendItemId());

		if (this.getSuspendAmount().isEmpty()) {
			this.setSuspendAmount("0");
		}

		for (CostDefinitions item : this.getListCostTable()) {

			if (item.getId().equals(id)) {

				double value = Double.parseDouble(this.getSuspendAmount());

				double certValue = 0d;

				if (item.getAcuCertification() != null) {
					certValue = item.getAguCertification();
				}

				if (value > certValue) {
					this.setSuspAmountError(true);
					return;
				}

				if (this.getCostSuspensionNote() != null && this.getCostSuspensionNote().length() > 254) {
					this.setCostSuspensionNote(this.getCostSuspensionNote().substring(0, 254));
				}

				if (this.getSessionBean().isACU()) {
					if (item.getAcuCertification() != null
							&& item.getAcuCertification() < Double.parseDouble(this.getSuspendAmount())) {
						this.setSuspAmountError(true);
					} else {
						item.setSuspendedByACU(true);
						item.setValueSuspendACU(
								NumberHelper.CurrencyFormat(Double.parseDouble(this.getSuspendAmount())));
						item.setDateSuspendACU(this.getSuspendDate());
						item.setAcuCertification(
								NumberHelper.CurrencyFormat(item.getAcuCertification() - item.getValueSuspendACU()));
						// item.setAcuCertification(
						// NumberHelper.CurrencyFormat(item.getAcuCertification()));
						item.setSuspendConfirmedSTC(false);
						item.setSuspendConfirmedACU(false);
						item.setSuspendConfirmedAGU(false);
						item.setSuspendSecondConfirmedSTC(false);
						item.setSuspendSecondConfirmedACU(false);
						item.setSuspendSecondConfirmedAGU(false);
						item.setSuspendRefusedAGU(false);
						item.setSuspendRefusedACU(false);
						item.setSuspendSecondRefusedAGU(false);
						item.setSuspendSecondRefusedACU(false);
						item.setSuspensionStatus(CostDefinitionSuspendStatus.SUSPENDED.getState());
						item.setSuspendNote(this.getCostSuspensionNote());
						item.setSuspendReasonId(this.getSelectedSuspendReason());
						item.setSuspendActDate(getSuspendActDate());
						item.setSuspendActNumber(getSuspendActNumber());
						//item.setAcuTotalCertification(item.getAcuCertification() - item.getValueSuspendACU());
						this.setTotalSuspeded((this.getTotalSuspeded()!=null ? this.getTotalSuspeded() : 0d)+ item.getValueSuspendACU());
						if (needSaveDocument) {
							item.setSuspendDocument(getSuspendDocument());
						}
					}
				}

				this.setSuspendAmount(null);
				this.setCostSuspensionNote(null);
				this.setSuspendActDate(null);
				this.setSuspendActNumber(null);
				this.setSelectedSuspendReason(0l);

				if (item.getValueSuspendSTC() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendSTC());
				} else if (item.getValueSuspendACU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendACU());
				} else if (item.getValueSuspendAGU() != null) {
					item.setPreviousSuspensionAmount(item.getValueSuspendAGU());
				}

				break;
			}
		}

		this.setNeedLoadBackup(true);
		this.setListCostDefinitionsBackUp(this.getListCostTable());

		try {
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

		setSelectedIndex(2);

		this.setShowSuspSaveMes("true");
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
						this.getRetreatDocument().getProtocolNumber(), cat, this.getRetreatDocument().getIsPublic(), this.getRetreatDocument().getSignflag());

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

	public void saveDurRecoveryDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getDurRecoveryDocumentUpFile() != null && this.getDurRecoveryDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getDurRecoveryDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getDurRecoveryDocumentUpFile().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(this.getDurRecoveryDocument().getDocumentDate(),
						this.getDurRecoveryDocumentUpFile().getName(), fileName,
						this.getDurRecoveryDocument().getProtocolNumber(), cat,
						this.getDurRecoveryDocument().getIsPublic(), this.getDurRecoveryDocument().getSignflag());

				this.getDurRecoveryDocument().setFileName(fileName);
				this.getDurRecoveryDocument().setName(this.getDurRecoveryDocumentUpFile().getName());
				this.getDurRecoveryDocument().setCategory(cat);
				this.getDurRecoveryDocument().setUser(this.getCurrentUser());
				this.getDurRecoveryDocument().setProject(this.getProject());
				this.getDurRecoveryDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("durRecoveryDocument", doc);
				this.setSelectedSuspendPartner(null);
				this.setDurRecoveryRole(null);
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

	public void rectifyItemsWithSusDoc() {
		rectifyItems(true);
	}

	public void durRecoveryItemsWithSusDoc() {
		durRecoveryItems(true);
	}

	public void rectifyItems() {
		rectifyItems(false);
	}

	public void durRecoveryItems() {
		durRecoveryItems(false);
	}

	public void rectifyItems(boolean needSaveDocument) {

		try {
			if (needSaveDocument) {
				this.saveRetreatDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		String selectedIds[] = getSelectedRetreatIds().split(",");

		for (CostDefinitions item : this.getListCostTable()) {
			if (Arrays.asList(selectedIds).contains(item.getId().toString())) {
				double value = Double.parseDouble(this.getSuspendRetreat());

				double certValue = 100d;

				if (value > certValue) {
					continue;
				}

				item.setRetreatDate(this.getRetreatDate());
				item.setRetreatActNumber(this.getRetreatActNumber());
				item.setRetreatActDate(this.getRetreatActDate());
				item.setSuspendRetreat(value);
				item.setAmountRetreat((item.getAcuCertification() * value) / certValue);
				item.setRectifiedByACU(Boolean.TRUE);
				item.setSelctedRetreatReason(this.getSelctedRetreatReason());
				item.setSelctedRetreatController(selctedRetreatController);
				item.setSelctedSuspendController(this.getSelctedSuspendController());
				item.setRetreatNote(this.getRetreatNote());
				item.setSuspensionStatus(CostDefinitionSuspendStatus.RETREAT.getState());
				item.setAcuTotalCertification(item.getAcuCertification());
				item.setAcuTotalCertification(item.getAcuCertification() - item.getAmountRetreat());
				item.setAcuCertification(item.getAcuCertification() - item.getAmountRetreat());
				this.setTotalRetreat((this.getTotalRetreat() != null ? this.getTotalRetreat() : 0d) + item.getAmountRetreat());
				
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
					BeansFactory.CostDefinitions().Save(item, getDurCompilation());
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				this.setCostRectificationReason(null);
				this.setRectificationAmount(null);
			}

		}
		this.setNeedLoadBackup(true);
		this.setListCostDefinitionsBackUp(this.getListCostTable());
		try {
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		setSelectedIndex(2);
	}

	public void durRecoveryItems(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				this.saveDurRecoveryDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		String selectedIds[] = getSelectedDurRecoveryIds().split(",");

		for (CostDefinitions item : this.getListCostTable()) {
			if (Arrays.asList(selectedIds).contains(item.getId().toString())) {
				double value = Double.parseDouble(this.getDurRecoverySuspendRetreat());

				double certValue = 100d;
				if (value > certValue) {
					continue;
				}
				item.setDurRecoveryDate(this.getDurRecoveryDate());
				item.setDurRecoveryActNumber(this.getDurRecoveryActNumber());
				item.setDurRecoveryActDate(this.getDurRecoveryActDate());
				item.setDurRecoverySuspendRetreat(value);
				item.setCostRecovery((item.getAcuCertification() * value) / certValue);
				item.setDurAmountOfPublicSupport(Double.parseDouble(this.getDurAmountOfPublicSupport()));
				item.setDurTotalEligibleCost(Double.parseDouble(this.getDurTotalEligibleCost()));
				item.setRecoverededByAGUACU(Boolean.TRUE);
				item.setSelctedDurRecoveryReason(this.getSelctedDurRecoveryReason());
				item.setSelctedDurRecoveryActType(this.getSelctedDurRecoveryActType());
				item.setDurRecoveryNote(this.getDurRecoveryNote());
				// item.setSuspensionStatus(CostDefinitionSuspendStatus.RETREAT.getState());
				item.setAcuTotalCertification(item.getAcuCertification() - item.getCostRecovery());
				item.setAcuCertification(item.getAcuCertification() - item.getCostRecovery());
				this.setTotalRecovered((this.getTotalRecovered() != null ? this.getTotalRecovered() : 0d ) + item.getCostRecovery());
				
				if (needSaveDocument) {
					if (getDurRecoveryDocument() != null) {
						try {
							BeansFactory.Documents().Save(getDurRecoveryDocument());
						} catch (Exception e) {
							log.error(e);
							e.printStackTrace();
						}
					}

					item.setCancelDurRecoveryDocument(getDurRecoveryDocument());
				}

				try {
					BeansFactory.CostDefinitions().Save(item, getDurCompilation());
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				this.setCostDurRecoveryReason(null);
				this.setDurRecoveryAmount(null);
				this.setDurAmountOfPublicSupport(null);
				this.setDurTotalEligibleCost(null);
			}

		}
		this.setNeedLoadBackup(true);
		this.setListCostDefinitionsBackUp(this.getListCostTable());
		try {
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		setSelectedIndex(2);
	}

	public void rectifyItemWithSusDoc() {
		rectifyItem(true);
	}

	public void durRecoveryItemWithSusDoc() {
		durRecoveryItem(true);
	}

	public void rectifyItem() {
		rectifyItem(false);
	}

	public void durRecoveryItem() {
		durRecoveryItem(false);
	}

	public void rectifyItem(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				this.saveRetreatDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		Long id = Long.parseLong(this.getRectificationItemId());

		for (CostDefinitions item : this.getListCostTable()) {

			if (item.getId().equals(id)) {
				double value = Double.parseDouble(this.getRetreatAmount());

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
				item.setAmountRetreat(value);
				item.setRectifiedByACU(Boolean.TRUE);
				item.setSelctedRetreatReason(this.getSelctedRetreatReason());
				item.setSelctedRetreatController(this.getSelctedRetreatController());
				item.setSelctedSuspendController(this.getSelctedSuspendController());
				item.setRetreatNote(this.getRetreatNote());
				item.setSuspensionStatus(CostDefinitionSuspendStatus.RETREAT.getState());
				item.setAcuTotalCertification(item.getAcuCertification() - item.getAmountRetreat());
				item.setAcuCertification(item.getAcuCertification() - item.getAmountRetreat());
				this.setTotalRetreat((this.getTotalRetreat() != null ? this.getTotalRetreat() : 0d )+ item.getAmountRetreat());
				
				
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
					BeansFactory.CostDefinitions().Save(item, getDurCompilation());
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				this.setCostRectificationReason(null);
				this.setRectificationAmount(null);

				break;
			}

		}
		this.setNeedLoadBackup(true);
		this.setListCostDefinitionsBackUp(this.getListCostTable());
		try {
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		setSelectedIndex(2);
	}

	public void durRecoveryItem(boolean needSaveDocument) {
		try {
			if (needSaveDocument) {
				this.saveDurRecoveryDocument();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		Long id = Long.parseLong(this.getDurRecoveryItemId());

		for (CostDefinitions item : this.getListCostTable()) {
			if (item.getId().equals(id)) {
				double value = Double.parseDouble(this.getDurRecoveryAmount());

				double certValue = 0d;

				if (item.getAcuCertification() != null) {
					certValue = item.getAguCertification();
				}

				if (value > certValue) {
					continue;
				}

				item.setDurRecoveryDate(this.getDurRecoveryDate());
				item.setDurRecoveryActNumber(this.getDurRecoveryActNumber());
				item.setDurRecoveryActDate(this.getDurRecoveryActDate());
				item.setCostRecovery(value);
				item.setDurAmountOfPublicSupport(Double.parseDouble(this.getDurAmountOfPublicSupport()));
				//item.setDurTotalEligibleCost(Double.parseDouble(this.getDurTotalEligibleCost()));
				item.setRecoverededByAGUACU(Boolean.TRUE);
				item.setSelctedDurRecoveryReason(this.getSelctedDurRecoveryReason());
				item.setSelctedDurRecoveryActType(this.getSelctedDurRecoveryActType());
				item.setDurRecoveryNote(this.getDurRecoveryNote());
				// item.setSuspensionStatus(CostDefinitionSuspendStatus.RETREAT.getState());
				item.setAcuTotalCertification(item.getAcuCertification() - item.getCostRecovery());
				item.setAcuCertification(item.getAcuCertification() - item.getCostRecovery());
				this.setTotalRecovered((this.getTotalRecovered() != null ? this.getTotalRecovered() : 0d) + item.getCostRecovery());
				
				if (needSaveDocument) {
					if (getDurRecoveryDocument() != null) {
						try {
							BeansFactory.Documents().Save(getDurRecoveryDocument());
						} catch (Exception e) {
							log.error(e);
							e.printStackTrace();
						}
					}

					item.setCancelDurRecoveryDocument(getDurRecoveryDocument());
				}

				try {
					BeansFactory.CostDefinitions().Save(item, getDurCompilation());
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				this.setCostDurRecoveryReason(null);
				this.setDurRecoveryAmount(null);

				break;
			}

		}
		this.setNeedLoadBackup(true);
		this.setListCostDefinitionsBackUp(this.getListCostTable());
		try {
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		setSelectedIndex(2);
	}

	public void rectifyItemCancel() {
		if (this.getRectificationItemId() == null || this.getRectificationItemId().isEmpty()) {
			return;
		}

		Long id = Long.parseLong(this.getRectificationItemId());

		for (CostDefinitions item : this.getListCostTable()) {
			if (item.getId().equals(id)) {
				if (this.isSTCMode()) {
					item.setRectifiedBySTC(false);
				} else if (this.isACUMode()) {
					item.setRectifiedByACU(false);
				} else if (this.isAGUMode()) {
					item.setRectifiedByAGU(false);
				}

				break;
			}

		}

		setSelectedIndex(2);
	}

	public void durRecoveryItemCancel() {
		if (this.getDurRecoveryItemId() == null || this.getDurRecoveryItemId().isEmpty()) {
			return;
		}

		Long id = Long.parseLong(this.getDurRecoveryItemId());

		for (CostDefinitions item : this.getListCostTable()) {
			if (item.getId().equals(id)) {
				if (this.isSTCMode()) {
					item.setRectifiedBySTC(false);
					item.setRecoverededByAGUACU(false);
				} else if (this.isACUMode()) {
					// item.setRectifiedByACU(false);
					item.setRecoverededByAGUACU(false);
				} else if (this.isAGUMode()) {
					// item.setRectifiedByAGU(false);
					item.setRecoverededByAGUACU(false);
				}

				break;
			}

		}

		setSelectedIndex(2);
	}

	private void reCulcTotalAmount() throws HibernateException, PersistenceBeanException {
		double sum = 0d;
		for (CostDefinitions cost : this.getListCostTable()) {

			if (cost.getSuspensionAmount() != null && !cost.getSuspensionAmount().toString().isEmpty()
					&& !Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
				if (cost.getAcuCertification() != null && cost.getAcuCertification() > 0 && cost.getACUSertified()) {
					sum += cost.getAcuCertification();
				} else if (cost.getAguCertification() != null && cost.getAguCertification() > 0
						&& cost.getAGUSertified()) {
					sum += cost.getAguCertification();
				} else if (cost.getStcCertification() != null && cost.getStcCertification() > 0
						&& cost.getSTCSertified()) {
					sum += cost.getStcCertification();
				} else if (cost.getCfCheck() != null && cost.getCfCheck() > 0) {
					sum += cost.getCfCheck();
				} else if (cost.getCilCheck() != null && cost.getCilCheck() > 0 && !cost.getCilIntermediateStepSave()) {
					sum += cost.getCilCheck();
				} else {
					sum += cost.getAmountReport();
				}

				if (cost.getSuspensionAmount() != null) {
					sum -= cost.getSuspensionAmount();
				}
			}
		}

		this.durSummaries.setTotalAmount(sum);
		this.setTotalAmountDur(sum);
		BeansFactory.DurSummaries().Save(this.durSummaries);
	}

	public void cutEntity() throws HibernateException, PersistenceBeanException {
		for (CostDefinitions cost : this.getListCostTable()) {
			if (cost.getId().equals(this.getEntityManageId())) {
				cost.setCut(true);
				BeansFactory.CostDefinitions().Save(cost, getDurCompilation());
			}
		}
		this.selectedIndex = 2;
		this.setEntityManageId(null);
	}

	public void manageEntity() {
		this.selectedIndex = 2;
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (!isPostback()) {
			this.setCustomPaginator(new CustomPaginator());
			if (this.getSession().containsKey(DURCompilationEditBean.DUR_COMPILATION_SAVE_FILTERS)) {
				this.getSession().remove(DURCompilationEditBean.DUR_COMPILATION_SAVE_FILTERS);
			} else {
				// clear filters from prev visit of page
				this.setFilterPartner(null);
				this.setFilterPhase(null);
				this.setFilterCostDefID(null);
				this.setFilterCostItem(null);
			}

			if (this.getSession().containsKey(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP)) {
				this.getCustomPaginator().setItemsPerPageSelected(
						(Integer) this.getSession().get(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP));
				this.getSession().remove(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP);
			}

			if (this.getSession().containsKey(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN)) {
				this.getCustomPaginator().setSelectedPage(
						((Integer) this.getSession().get(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN)));
				this.getSession().remove(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN);
				this.setSelectedIndex(2);
			}

		}

		this.setShowSuspSaveMes("none");
		// Check what project number is main
		if (this.getSuspendAmount() == null || this.getSuspendAmount().isEmpty()) {
			this.setSuspendAmount("0");
		}

		if (this.getSuspendDate() == null) {
			this.setSuspendDate(new Date());
		}

		if (this.getRectificationDate() == null) {
			this.setRectificationDate(new Date());
		}

		if (this.getRetreatDate() == null) {
			this.setRetreatDate(new Date());
		}

		if (this.getDurRecoveryDate() == null) {
			this.setDurRecoveryDate(new Date());
		}

		if (this.getSession().get("durCompilationEditBack") != null) {
			boolean value = Boolean.valueOf(String.valueOf(this.getSession().get("durCompilationEditBack")));

			if (value) {
				if (this.getProjectId() != null) {
					this.setCurrentProjectId(this.getProjectId());
				} else {
					if (this.getSession().get("costCertProjectId") != null) {
						this.setCurrentProjectId(String.valueOf(this.getSession().get("costCertProjectId")));
					}
				}
			} else {
				if (this.getSession().get("costCertProjectId") != null) {
					this.setCurrentProjectId(String.valueOf(this.getSession().get("costCertProjectId")));
				} else {
					if (this.getProjectId() != null) {
						this.setCurrentProjectId(this.getProjectId());
					}
				}
			}
		}

		if (this.getSession().get("durCompilationEditShow") != null) {
			this.setEditMode(!Boolean.valueOf(String.valueOf(this.getSession().get("durCompilationEditShow"))));
			this.setCertified(false);
		}

		if (this.getSession().get("durCompilationEdit") != null) {
			this.durCompilation = BeansFactory.DurCompilations()
					.Load(String.valueOf(this.getSession().get("durCompilationEdit")));
			if (isEditMode() && getSessionBean().isACUW() && getDurCompilation() != null
					&& getDurCompilation().getDurState() != null && getDurCompilation().getDurState().getId() != null
					&& getDurCompilation().getDurState().getId().equals(DurStateTypes.Certified.getValue())) {
				this.setEditMode(false);
				this.setCertified(true);
			}
			this.mergeDurInfo();
			this.durSummaries = BeansFactory.DurSummaries().LoadByDurCompilation(this.durCompilation.getId());

			this.setFesrReimbursementAmount(this.durSummaries.getFesrAmount());
			this.setFesrReimbursementAmount(this.durSummaries.getFesrAmount2());
			this.setTotalAmountDur(this.durSummaries.getTotalAmount());
			this.setTotalAmountDur2(this.durSummaries.getTotalAmount2());
			this.setTotalReimbursement(this.durSummaries.getTotalReimbursement());
			this.setFrCnReimbursement(this.durSummaries.getFrCnReimbursementAmount());
			this.setFrCnReimbursement2(this.durSummaries.getFrCnReimbursementAmount2());
			this.setItCnReimbursement(this.durSummaries.getItCnReimbursementAmount());
			this.setItCnReimbursement2(this.durSummaries.getItCnReimbursementAmount2());
			this.setUserFesrAmount(this.durSummaries.getUserFesrAmount());
			this.setUserFrCnReimbursementAmount(this.durSummaries.getUserFrCnReimbursementAmount());
			this.setUserItCnReimbursementAmount(this.durSummaries.getUserItCnReimbursementAmount());

			List<DurInfoToResponsiblePeople> listPeople = BeansFactory.DurInfoToResponsiblePeople()
					.getByDurInfo(this.getDurInfo().getId());

			if (this.getSelectedPartners() == null) {
				this.setSelectedPartners(new ArrayList<String>());
			} else {
				this.getSelectedPartners().clear();
			}

			this.setSelectedPartner2("");
			StringBuilder sb = new StringBuilder();
			for (DurInfoToResponsiblePeople item : listPeople) {
				this.getSelectedPartners().add(item.getPerson().getId().toString());

				sb.append(item.getPerson().getName());
				sb.append(", ");
			}

			if (sb.length() > 2) {
				this.setSelectedPartner2(sb.substring(0, sb.length() - 1));
			} else {
				this.setSelectedPartner2(sb.toString());
			}
		} else {
			this.durCompilation = new DurCompilations();
			this.setDurInfo(new DurInfos());
			this.getDurInfo().setIntermedio(true);
			this.durSummaries = new DurSummaries();

			this.setSelectedPartners(new ArrayList<String>());

			this.setSelectedPartner2(String.valueOf(SelectItemHelper.getFirst().getValue()));

			this.LoadBlankData();
		}

		this.setSpanWarningVisibility("none");
		this.setSpanSaveAmountsWarningVisibility("none");

		this.suspensionTypes = new ArrayList<SelectItem>();
		for (SuspendedCostType type : SuspendedCostType.values()) {
			this.suspensionTypes.add(new SelectItem(type.getId().toString(), type.getValue()));
		}

		this.suspensionReasons = new ArrayList<SelectItem>();
		for (SuspensionReasonTypes type : SuspensionReasonTypes.values()) {
			this.suspensionReasons.add(new SelectItem(type.getState().toString(), Utils.getString(type.getCode())));
		}

		this.CheckRoles();
		this.CheckCertificationAvailable();

		if (needReload || !this.isPostback() || getListCostDefinitionsBackup() == null
				|| getListCostDefinitionsBackup().isEmpty()) {
			this.RecheckCostItems(this.LoadEntities());
		} else {
			this.RecheckCostItems(getListCostDefinitionsBackup());
		}
		filterCdList();

		if (!this.isPostback() && this.isCfEdit()) {
			if (this.getFesrReimbursementAmount() == null) {
				this.setFesrReimbursementAmount(0d);
			}
			if (this.getFrCnReimbursement() == null) {
				this.setFrCnReimbursement(0d);
			}
			if (this.getItCnReimbursement() == null) {
				this.setItCnReimbursement(0d);
			}
		}

		if (this.getSession().get("durCompilationEdit") != null) {
			List<Long> listIndicesEdit = new ArrayList<Long>();
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected()) {
					listIndicesEdit.add(cd.getId());
				}
			}
			this.getViewState().put("durCompilationEditListCD", listIndicesEdit);
		}

		this.RecalculateAmounts();
		this.LoadDocuments();
		this.FillDropDowns();
		fillPartners();

		if (!this.checkEqualsAmounts() && this.getViewState().get("addedDocument") == null) {
			ValidatorBean.setStaticTabMessage(Utils.getString("costSuspenceWarning"));
		} else {
			ValidatorBean.setStaticTabMessage(null);
			this.getViewState().remove("addedDocument");
		}
		partnersForProject = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getCurrentProjectId());
		if (Boolean.FALSE.equals(this.getAsse3Mode())) {
			this.FillGridSummary();
			this.FillGridSummaryWp();
			this.FillGridSummaryPartner();
		} else {
			FillGridSummaryAsse3();
		}

		try {
			this.FillCostTable();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		this.ReRenderScroll();
		this.CaluculateAdditionalFinansing();
		this.FillZoneCurrentTotal();
		this.recalculateAmountDur();
	}

	private void mergeDurInfo() throws PersistenceBeanException {
		DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(this.durCompilation.getId());

		setDurInfo(di);
	}

	public void searchForm() throws NumberFormatException, HibernateException, PersistenceBeanException {
		List<CostDefinitions> list = new ArrayList<CostDefinitions>();

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions costDefinitions : this.getListCostDefinitionsWeb()) {
				if (this.getFilterPartnerSearch() != null && !this.getFilterPartnerSearch().isEmpty()) {
					if (this.getFilterPartnerSearch().equals("-1")) {
						boolean skip = true;
						if (this.getProject().getAsse() == 5) {
							List<Long> ids = BeansFactory.ControllerUserAnagraph()
									.GetUsersIndicesByType(UserRoleTypes.AGU);
							if (ids != null && ids.contains(costDefinitions.getUser().getId())) {
								skip = false;
							}
						}

						if (skip) {
							continue;
						}
					} else if (!costDefinitions.getUser().getId().equals(Long.parseLong(this.getFilterPartnerSearch()))
							&& !costDefinitions.getBudgetOwner().getId()
									.equals(Long.parseLong(this.getFilterPartnerSearch()))) {
						continue;
					}
				}

				if (this.getReportNumber() != null && !this.getReportNumber().isEmpty()) {
					if (costDefinitions.getReportNumber() == null || Integer
							.parseInt(this.getReportNumber()) != costDefinitions.getReportNumber().intValue()) {
						continue;
					}

				}

				if (Boolean.TRUE.equals(this.getAsse3Mode())) {
					if (this.getFilterWpAsse3Mode() != null && !this.getFilterWpAsse3Mode().isEmpty()) {

						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getPhase().name().equals(this.getFilterWpAsse3Mode())) {
							continue;
						}

					}

					if (this.getFilterExpenseItemAsse3Mode() != null
							&& !this.getFilterExpenseItemAsse3Mode().isEmpty()) {

						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getValue().equals(this.getFilterExpenseItemAsse3Mode())) {
							continue;
						}

					}
				} else {
					if (this.getFilterExpenseItem() != null && !this.getFilterExpenseItem().isEmpty()) {
						if (costDefinitions.getCostItem() == null) {
							continue;
						}

						if (!costDefinitions.getCostItem().getId()
								.equals(Long.parseLong(this.getFilterExpenseItem()))) {
							continue;
						}
					}

					if (this.getFilterWp() != null && !this.getFilterWp().equals(0l)) {
						if (costDefinitions.getCostDefinitionPhase() == null
								|| !costDefinitions.getCostDefinitionPhase().getId().equals(this.getFilterWp())) {
							continue;
						}
					}
				}

				list.add(costDefinitions);
			}
		}

		this.setListCostDefinitionsWeb(list);
	}

	public void clearForm() {
		this.setFilterPartnerSearch(null);
		this.setReportNumber(null);
		this.setFilterWpAsse3Mode(null);
		this.setFilterExpenseItemAsse3Mode(null);
		this.setFilterExpenseItem(null);
		this.setFilterWp(null);
	}

	public void asse3WpChange(ValueChangeEvent event) throws PersistenceBeanException {
		this.setFilterWpAsse3Mode((String) event.getNewValue());
		this.fillExpenseItemsAsse3Mode();
	}

	private void fillExpenseItemsAsse3Mode() throws PersistenceBeanException {
		setFilterWpsAsse3Mode(new ArrayList<SelectItem>());
		getFilterWpsAsse3Mode().add(SelectItemHelper.getFirst());
		for (PhaseAsse3Types type : PhaseAsse3Types.values()) {
			getFilterWpsAsse3Mode().add(new SelectItem(type.name(), type.toString()));
		}

		setFilterExpenseItemsAsse3Mode(new ArrayList<SelectItem>());
		getFilterExpenseItemsAsse3Mode().add(SelectItemHelper.getFirst());
		if (this.getFilterWpAsse3Mode() != null && !this.getFilterWpAsse3Mode().isEmpty()) {
			PhaseAsse3Types phase = null;
			try {
				phase = PhaseAsse3Types.valueOf(this.getFilterWpAsse3Mode());
			} catch (Exception e) {
				log.error("Enum is not compatible", e);
				return;
			}
			List<CostAsse3> costs = BeansFactory.CostAsse3().GetByPhase(phase);

			for (CostAsse3 cost : costs) {
				getFilterExpenseItemsAsse3Mode().add(new SelectItem(cost.getValue(), cost.getValue()));
			}

		}
	}

	private void fillDropdown() throws HibernateException, NumberFormatException, PersistenceBeanException {
		setFilterPartners(new ArrayList<SelectItem>());
		setFilterExpenseItems(new ArrayList<SelectItem>());
		setFilterWps(new ArrayList<SelectItem>());

		this.getFilterPartners().add(SelectItemHelper.getAllElement());

		List<CFPartnerAnagraphs> listPart = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
		for (CFPartnerAnagraphs partner : listPart) {
			if (partner.getUser() == null) {
				if (this.getProject().getAsse() == 5) {
					this.getFilterPartners().add(new SelectItem("-1", partner.getNaming()));
				}
			} else {
				this.getFilterPartners()
						.add(new SelectItem(String.valueOf(partner.getUser().getId()), partner.getNaming()));
			}
		}

		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			fillExpenseItemsAsse3Mode();
		} else {
			List<CostDefinitionPhases> list = BeansFactory.CostDefinitionPhase().Load();
			getFilterWps().add(SelectItemHelper.getAllElement());
			for (CostDefinitionPhases item : list) {
				getFilterWps().add(new SelectItem(item.getId(), item.getValue()));
			}

			this.getFilterExpenseItems().add(SelectItemHelper.getAllElement());
			for (CostItems item : BeansFactory.CostItems().Load()) {
				getFilterExpenseItems().add(new SelectItem(item.getId().toString(), item.getValue()));
			}
		}
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		this.getViewState().remove("deleteDocB");
		this.getViewState().remove("deleteDocC");
		this.getViewState().remove("addedDocument");

		if (this.getSession().get("costCertProjectId") == null) {
			if (!this.getSessionBean().getIsActualSate()) {
				this.goTo(PagesTypes.PROJECTINDEX);
			}

			if (!(AccessGrantHelper.IsReadyDurCompilation() && AccessGrantHelper.CheckRolesDURCompilation())) {
				this.goTo(PagesTypes.PROJECTINDEX);
			}
		}
		if (this.getProject() != null) {
			//this.setAsse3Mode(Boolean.valueOf(this.getProject().getAsse() == 4));
			this.setAsse3Mode(Boolean.FALSE);
		}

		if (this.getSession().get("costDefinitionViewToDUREdit") != null) {
			this.getSession().remove("costDefinitionViewToDUREdit");
			this.setSelectedIndex(2);
		}
		this.setShowSaveAttention("none");
		this.InitPaging();
		this.fillDropdown();
	}

	private void FillGridSummaryAsse3() throws PersistenceBeanException {

		List<CostItemSummaryWrapper> listSummary = new ArrayList<CostItemSummaryWrapper>();

		List<CostAsse3> asse3vals = BeansFactory.CostAsse3().Load();

		Long userId = extractUserId();

		double total = 0;
		for (CostAsse3 asse3 : asse3vals) {
			CostItemSummaryWrapper wrap = new CostItemSummaryWrapper();
			wrap.setName(asse3.toString());
			wrap.setAmountFromBudget(new Double(countAmountFromBudgetCostItemAsse3(asse3)));
			wrap.setAmountByCf(new Double(countAmounByCFCostItemAsse3(asse3, userId)));
			wrap.setAmountCostCertificate(new Double(countAmountCostCertificateCostItemAsse3(asse3, userId)));
			wrap.setAmountRequest(new Double(countAmountRequestCostItemAsse3(asse3, userId)));
			wrap.setStcCertificated(new Double(countStcCertifiedCostItemAsse3(asse3, userId)));
			wrap.setAguCertificated(new Double(countAguCertifiedCostItemAsse3(asse3, userId)));
			wrap.setAcuCertificated(new Double(countAcuCertifiedCostItemAsse3(asse3, userId)));
			wrap.setAbsorption(wrap.getAmountFromBudget().doubleValue() == 0d ? new Double(0d)
					: new Double(countLastCertifiedAmountCostItemAsse3(asse3, userId)
							/ wrap.getAmountFromBudget().doubleValue() * 100d));
			wrap.setPreviousExpenditureRequisted(countLastValidatedAmount(asse3, userId));

			double previousExpenditureRequisted = countLastValidatedAmount(asse3, userId)
					+ countOldValidateAmount(asse3, userId);
			wrap.setPreviousExpenditureRequisted(previousExpenditureRequisted);
			// previousExpenditureRequistedTotal +=
			// previousExpenditureRequisted;
			listSummary.add(wrap);
		}

		this.setListSummaryAsse3(FillOtherValuesAsse3(listSummary));

	}

	private Long extractUserId() throws PersistenceBeanException {
		Long userId = null;

		List<PartnersBudgets> listBudgets = new ArrayList<PartnersBudgets>();
		for (CFPartnerAnagraphs p : partnersForProject) {
			if (this.getSelectedPartnerForGrid() == null || this.getSelectedPartnerForGrid().longValue() == 0l) {
				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				continue;
			}

			else if (p.getId().equals(this.getSelectedPartnerForGrid())) {

				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				userId = p.getUser().getId();
				break;

			}

		}
		return userId;
	}

	private List<CostItemSummaryWrapper> FillOtherValuesAsse3(List<CostItemSummaryWrapper> listSummary)
			throws PersistenceBeanException {

		BudgetInputSourceDivided budget = BudgetInputSourceDividedBean.GetByProject(this.getCurrentProjectId()).get(0);
		CostItemSummaryWrapper cfSummaryWrapper = new CostItemSummaryWrapper();

		CostItemSummaryWrapper entityTotal = createTotalSummaryAsse3(listSummary);

		listSummary.add(entityTotal);

		//
		// Double previsionExCnPrivateOld = 0d;
		// Double previsionExCnPublicOld = 0d;
		// Double previsionExAdditionalFinansingOld = 0d;
		// Double previsionExAdditionalFinansing = 0d;
		//
		// List<DurCompilations> durList =
		// BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
		// null);
		//
		// if (durList != null) {
		// for (DurCompilations dc : durList) {
		// for (CostDefinitions cost : dc.getCostDefinitions()) {
		// boolean compute = true;
		// for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
		// if (cd.getId().equals(cost.getId())) {
		// compute = false;
		// }
		//
		// }
		// if (compute) {
		// compute = false;
		// previsionExCnPrivateOld += this.CnPrivateValidate(cost);
		// previsionExCnPublicOld += this.CnPublicValidate(cost);
		// previsionExAdditionalFinansingOld +=
		// GetLastAdditionaFinansingValidated(cost);
		// }
		// }
		// }
		// }
		//
		// if (this.getListCostDefinitionsWeb() != null) {
		// for (CostDefinitions cost : this.getListCostDefinitionsWeb()) {
		// if (cost.getSelected()) {
		// previsionExCnPrivateOld += this.CnPrivateValidate(cost);
		// previsionExCnPublicOld += this.CnPublicValidate(cost);
		// previsionExAdditionalFinansing +=
		// GetLastAdditionaFinansingValidated(cost);
		// cfSummaryWrapper.setAmountByCf(cfSummaryWrapper.getAmountByCf() +
		// (cost.getAdditionalFinansingAmount() == null ? 0d
		// : cost.getAdditionalFinansingAmount().doubleValue()));
		// cfSummaryWrapper.setAmountCostCertificate(cfSummaryWrapper.getAmountCostCertificate()
		// + (cost.getCilCheckAdditionalFinansingAmount() == null ? 0d :
		// cost.getCilCheckAdditionalFinansingAmount()));
		// if(cost.getCreatedByPartner() == true){
		// cfSummaryWrapper.setAmountRequest(cfSummaryWrapper.getAmountRequest()
		// + (cost.getCfCheckAdditionalFinansingAmount() == null ? 0d :
		// cost.getCfCheckAdditionalFinansingAmount()));
		// }else{
		// cfSummaryWrapper.setAmountRequest(cfSummaryWrapper.getAmountRequest()
		// + (cost.getCilCheckAdditionalFinansingAmount() == null ? 0d :
		// cost.getCilCheckAdditionalFinansingAmount()));
		// }
		// cfSummaryWrapper.setStcCertificated(cfSummaryWrapper.getStcCertificated()
		// + (cost.getStcCheckAdditionalFinansingAmount() == null ? 0d:
		// cost.getStcCheckAdditionalFinansingAmount()));
		// cfSummaryWrapper.setAcuCertificated(cfSummaryWrapper.getAcuCertificated()
		// + (cost.getAcuCheckAdditionalFinansingAmount() == null ? 0d :
		// cost.getAcuCheckAdditionalFinansingAmount()));
		// cfSummaryWrapper.setAguCertificated(cfSummaryWrapper.getAguCertificated()
		// + (cost.getAguCheckAdditionalFinansingAmount() == null ? 0d :
		// cost.getAguCheckAdditionalFinansingAmount()));
		// }
		// }
		// }
		Double previsionExCnPrivateOld = 0d;
		Double previsionExCnPublicOld = 0d;
		Double previsionExAdditionalFinansingOld = 0d;

		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gbudget = null;
		CFPartnerAnagraphs ca = null;

		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					boolean compute = true;
					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
						if (cd.getId().equals(cost.getId())) {
							compute = false;
						}

					}
					if (compute) {
						compute = false;
						ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
						gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
								.get(0);

						if (gbudget != null) {
							if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
								if (((gbudget.getFesr() / (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate()
										- gbudget.getNetRevenue())) * 100) == 50) {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
								} else {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
								}

							} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
								previsionExCnPublicOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							}
						}

						previsionExAdditionalFinansingOld += GetLastAdditionaFinansingValidated(cost);
					}
				}
			}
		}

		Double amountByCfPublic = 0d;
		Double amountByCfPrivate = 0d;
		Double amountCostCertificatePublic = 0d;
		Double amountCostCertificatePrivate = 0d;
		Double amountRequestPublic = 0d;
		Double amountRequestPrivate = 0d;
		Double stcCertificatedPublic = 0d;
		Double stcCertificatedPrivate = 0d;
		Double acuCertificatedPublic = 0d;
		Double acuCertificatedPrivate = 0d;
		Double aguCertificatedPublic = 0d;
		Double aguCertificatedPrivate = 0d;
		Double previsionExCnPrivate = 0d;
		Double previsionExCnPublic = 0d;
		Double previsionExAdditionalFinansing = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
							.get(0);

					if (gbudget != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gbudget.getFesr()
								/ (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate() - gbudget.getNetRevenue()))
								* 100) == 50) {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.5d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.5d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.5d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.5d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.5d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.5d);
						} else {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.15d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						previsionExCnPublic += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
						acuCertificatedPublic += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
						aguCertificatedPublic += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
						stcCertificatedPublic += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
						amountByCfPublic += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
						amountCostCertificatePublic += (this.getValidatedCostCertificateAmountSubAdditional(cost)
								* 0.15d);
						amountRequestPublic += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
					}
					previsionExAdditionalFinansing += GetLastAdditionaFinansingValidated(cost);

					cfSummaryWrapper.setAmountByCf(
							cfSummaryWrapper.getAmountByCf() + (cost.getAdditionalFinansingAmount() == null ? 0d
									: cost.getAdditionalFinansingAmount().doubleValue()));
					cfSummaryWrapper.setAmountCostCertificate(cfSummaryWrapper.getAmountCostCertificate()
							+ (cost.getCilCheckAdditionalFinansingAmount() == null ? 0d
									: cost.getCilCheckAdditionalFinansingAmount()));
					if (cost.getCreatedByPartner() == true) {
						cfSummaryWrapper.setAmountRequest(cfSummaryWrapper.getAmountRequest()
								+ (cost.getCfCheckAdditionalFinansingAmount() == null ? 0d
										: cost.getCfCheckAdditionalFinansingAmount()));
					} else {
						cfSummaryWrapper.setAmountRequest(cfSummaryWrapper.getAmountRequest()
								+ (cost.getCilCheckAdditionalFinansingAmount() == null ? 0d
										: cost.getCilCheckAdditionalFinansingAmount()));
					}
					cfSummaryWrapper.setStcCertificated(
							cfSummaryWrapper.getStcCertificated() + (cost.getStcCheckAdditionalFinansingAmount() == null
									? 0d : cost.getStcCheckAdditionalFinansingAmount()));
					cfSummaryWrapper.setAcuCertificated(
							cfSummaryWrapper.getAcuCertificated() + (cost.getAcuCheckAdditionalFinansingAmount() == null
									? 0d : cost.getAcuCheckAdditionalFinansingAmount()));
					cfSummaryWrapper.setAguCertificated(
							cfSummaryWrapper.getAguCertificated() + (cost.getAguCheckAdditionalFinansingAmount() == null
									? 0d : cost.getAguCheckAdditionalFinansingAmount()));
				}
			}

		}

		cfSummaryWrapper.setAmountFromBudget(budget.getCnPrivate());
		cfSummaryWrapper.setName(Utils.getString("Resources", "durCompilationTotalEntity", null));
		cfSummaryWrapper
				.setPreviousExpenditureRequisted(previsionExAdditionalFinansingOld + previsionExAdditionalFinansing);

		CostItemSummaryWrapper objFesrTotal = new CostItemSummaryWrapper();
		objFesrTotal.setName(Utils.getString("Resources", "durCompilationEditFesrTotal", null));
		objFesrTotal.setShowOnlyFirstColumn(true);
		objFesrTotal.setAmountFromBudget(budget.getFesr());
		// objFesrTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapper.getAmountByCf()) * 0.75d);
		// objFesrTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapper.getAmountCostCertificate()) * 0.75d);
		// objFesrTotal.setAmountRequest((entityTotal.getAmountRequest() -
		// cfSummaryWrapper.getAmountRequest()) * 0.75d);
		// objFesrTotal
		// .setStcCertificated((entityTotal.getStcCertificated() -
		// cfSummaryWrapper.getStcCertificated()) * 0.75d);
		// objFesrTotal
		// .setAcuCertificated((entityTotal.getAcuCertificated() -
		// cfSummaryWrapper.getAcuCertificated()) * 0.75d);
		// objFesrTotal
		// .setAguCertificated((entityTotal.getAguCertificated() -
		// cfSummaryWrapper.getAguCertificated()) * 0.75d);

		objFesrTotal.setAmountByCf(this.GetFesrTotalAmountByCf());
		objFesrTotal.setAmountCostCertificate(this.GetFesrTotalAmountCostCertificate());
		objFesrTotal.setAmountRequest(this.GetFesrTotalAmountRequest());
		objFesrTotal
				.setStcCertificated((entityTotal.getStcCertificated() == null || entityTotal.getStcCertificated() == 0)
						? 0d : this.GetFesrTotalAmountStcCertificated());
		objFesrTotal
				.setAcuCertificated((entityTotal.getAcuCertificated() == null || entityTotal.getAcuCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAcuCertificated());
		objFesrTotal
				.setAguCertificated((entityTotal.getAguCertificated() == null || entityTotal.getAguCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAguCertificated());

		objFesrTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapper.getSuspended()) * 0.75d);
		objFesrTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapper.getRectified()) * 0.75d);
		objFesrTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapper.getReinstated()) * 0.75d);
		objFesrTotal.setPreviousExpenditureRequisted(this.calculateFesrAmount2() + this.calculateFesrAmountOldDur());

		CostItemSummaryWrapper objCnTotal = new CostItemSummaryWrapper();
		objCnTotal.setName(Utils.getString("Resources", "durCompilationEditCnTotal", null));
		objCnTotal.setShowOnlyFirstColumn(true);
		// objCnTotal.setAmountFromBudget(budget.getCnPublic());
		// objCnTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapper.getAmountByCf()) * 0.25d);
		// objCnTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapper.getAmountCostCertificate()) * 0.25d);
		// objCnTotal.setAmountRequest((entityTotal.getAmountRequest() -
		// cfSummaryWrapper.getAmountRequest()) * 0.25d);
		// objCnTotal
		// .setStcCertificated((entityTotal.getStcCertificated() -
		// cfSummaryWrapper.getStcCertificated()) * 0.25d);
		// objCnTotal
		// .setAcuCertificated((entityTotal.getAcuCertificated() -
		// cfSummaryWrapper.getAcuCertificated()) * 0.25d);
		// objCnTotal
		// .setAguCertificated((entityTotal.getAguCertificated() -
		// cfSummaryWrapper.getAguCertificated()) * 0.25d);
		// objCnTotal.setSuspended((entityTotal.getSuspended() -
		// cfSummaryWrapper.getSuspended()) * 0.25d);
		// objCnTotal.setRectified((entityTotal.getRectified() -
		// cfSummaryWrapper.getRectified()) * 0.25d);
		// objCnTotal.setReinstated((entityTotal.getReinstated() -
		// cfSummaryWrapper.getReinstated()) * 0.25d);
		objCnTotal.setAmountFromBudget(budget.getCnPublic() + budget.getCnPublicOther());
		objCnTotal.setAmountByCf(amountByCfPrivate + amountByCfPublic);
		objCnTotal.setAmountCostCertificate(amountCostCertificatePrivate + amountCostCertificatePublic);
		objCnTotal.setAmountRequest(amountRequestPrivate + amountRequestPublic);
		objCnTotal.setStcCertificated(stcCertificatedPrivate + stcCertificatedPublic);
		objCnTotal.setAguCertificated(aguCertificatedPrivate + aguCertificatedPublic);
		objCnTotal.setAcuCertificated(acuCertificatedPrivate + acuCertificatedPublic);
		objCnTotal.setPreviousExpenditureRequisted(
				previsionExCnPublic + previsionExCnPrivate + previsionExCnPublicOld + previsionExCnPrivateOld);
		objCnTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapper.getSuspended()) * 0.25d);
		objCnTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapper.getRectified()) * 0.25d);
		objCnTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapper.getReinstated()) * 0.25d);
		// objCnTotal.setAmountByCf(this.CnPrivateAmountByCf() +
		// this.CnPublicAmountByCf());
		// objCnTotal
		// .setAmountCostCertificate(this.CnPrivateAmountCostCertificate() +
		// this.CnPublicAmountCostCertificate());
		// objCnTotal.setAmountRequest(this.CnPublicAmountRequest() +
		// this.CnPrivateAmountRequest());
		// objCnTotal.setStcCertificated(this.CnPublicStcCertified() +
		// this.CnPrivateStcCertified());
		// objCnTotal.setAguCertificated(this.CnPublicAguCertified() +
		// this.CnPrivateAguCertified());
		// objCnTotal.setAcuCertificated(this.CnPublicAcuCertified() +
		// this.CnPrivateAcuCertified());
		// objCnTotal.setPreviousExpenditureRequisted(previsionExCnPrivateOld +
		// previsionExCnPublicOld);

		CostItemSummaryWrapper objCnPublic = new CostItemSummaryWrapper();
		objCnPublic.setName(Utils.getString("Resources", "durCompilationEditPublicCN", null));
		objCnPublic.setShowOnlyFirstColumn(true);
		objCnPublic.setAmountFromBudget(budget.getCnPublic());
		// objCnPublic.setAmountByCf(this.CnPublicAmountByCf());
		// objCnPublic.setAmountCostCertificate(this.CnPublicAmountCostCertificate());
		// objCnPublic.setAmountRequest(this.CnPublicAmountRequest());
		// objCnPublic.setStcCertificated(this.CnPublicStcCertified());
		// objCnPublic.setAguCertificated(this.CnPublicAguCertified());
		// objCnPublic.setAcuCertificated(this.CnPublicAcuCertified());
		// objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublicOld);
		objCnPublic.setAcuCertificated(acuCertificatedPublic);
		objCnPublic.setAguCertificated(aguCertificatedPublic);
		objCnPublic.setStcCertificated(stcCertificatedPublic);
		objCnPublic.setAmountByCf(amountByCfPublic);
		objCnPublic.setAmountCostCertificate(amountCostCertificatePublic);
		objCnPublic.setAmountRequest(amountRequestPublic);
		objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublic + previsionExCnPublicOld);

		CostItemSummaryWrapper objCnPrivate = new CostItemSummaryWrapper();
		objCnPrivate.setName(Utils.getString("Resources", "durCompilationEditPrivateCN", null));
		objCnPrivate.setShowOnlyFirstColumn(true);
		objCnPrivate.setAmountFromBudget(budget.getCnPublicOther());
		// objCnPrivate.setAmountByCf(this.CnPrivateAmountByCf());
		// objCnPrivate.setAmountCostCertificate(this.CnPrivateAmountCostCertificate());
		// objCnPrivate.setAmountRequest(this.CnPrivateAmountRequest());
		// objCnPrivate.setStcCertificated(this.CnPrivateStcCertified());
		// objCnPrivate.setAguCertificated(this.CnPrivateAguCertified());
		// objCnPrivate.setAcuCertificated(this.CnPrivateAcuCertified());
		// objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivateOld);
		objCnPrivate.setAcuCertificated(acuCertificatedPrivate);
		objCnPrivate.setAguCertificated(aguCertificatedPrivate);
		objCnPrivate.setStcCertificated(stcCertificatedPrivate);
		objCnPrivate.setAmountByCf(amountByCfPrivate);
		objCnPrivate.setAmountCostCertificate(amountCostCertificatePrivate);
		objCnPrivate.setAmountRequest(amountRequestPrivate);
		objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivate + previsionExCnPrivateOld);

		listSummary.add(objFesrTotal);
		listSummary.add(objCnTotal);
		listSummary.add(objCnPublic);
		listSummary.add(objCnPrivate);
		listSummary.add(cfSummaryWrapper);
		this.updateTotalValues(entityTotal, objFesrTotal, objCnTotal, objCnTotal);
		this.setTotalWrapper(entityTotal);
		return listSummary;

	}

	private CostItemSummaryWrapper createTotalSummaryAsse3(List<CostItemSummaryWrapper> list) {
		CostItemSummaryWrapper total = new CostItemSummaryWrapper();
		total.setName(Utils.getString("Resources", "total", null));
		total.setShowOnlyFirstColumn(false);
		for (CostItemSummaryWrapper item : list) {
			total.setAmountFromBudget(
					total.getAmountFromBudget().doubleValue() + item.getAmountFromBudget().doubleValue());
			total.setAmountByCf(total.getAmountByCf().doubleValue() + item.getAmountByCf().doubleValue());
			total.setAmountCostCertificate(
					total.getAmountCostCertificate().doubleValue() + item.getAmountCostCertificate().doubleValue());
			total.setAmountRequest(total.getAmountRequest().doubleValue() + item.getAmountRequest().doubleValue());
			total.setStcCertificated(
					total.getStcCertificated().doubleValue() + item.getStcCertificated().doubleValue());
			total.setAguCertificated(
					total.getAguCertificated().doubleValue() + item.getAguCertificated().doubleValue());
			total.setAcuCertificated(
					total.getAcuCertificated().doubleValue() + item.getAcuCertificated().doubleValue());
			total.setAbsorption(total.getAbsorption().doubleValue() + item.getAbsorption().doubleValue());
			total.setPreviousExpenditureRequisted(
					total.getPreviousExpenditureRequisted() + item.getPreviousExpenditureRequisted());
		}

		return total;
	}

	private double countAmountFromBudgetCostItemAsse3(CostAsse3 asse3) throws PersistenceBeanException {

		List<PartnersBudgets> listAux = BeansFactory.PartnersBudgets().LoadByProject(this.getProjectId());
		// List<CostItemAsse3> items =
		// BeansFactory.CostItemAsse3().GetByPartnerBudgetAndItem(this.getProjectId(),
		// asse3.getId());

		List<CostItemAsse3> items = new ArrayList<>();

		for (PartnersBudgets pb : listAux) {
			items.addAll(pb.getItemsAsse3());
		}

		double summ = 0d;
		for (CostItemAsse3 item : items) {

			if (item.getValue() != null && asse3.getId().equals(item.getCostPhase().getId())) {
				summ += item.getValue().doubleValue();
			}
		}
		return summ;
	}

	private double countAmounByCFCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getUser() != null && (userId == null || userId.equals(cd.getUser().getId()))
					&& cd.getCostItemPhaseAsse3() != null && asse3.getId().equals(cd.getCostItemPhaseAsse3().getId())) {
				if (cd.getAmountReport() != null) {
					summ += cd.getAmountReport().doubleValue();
				}

			}
		}
		return summ;
	}

	private double countAmountCostCertificateCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getCilCheck() != null) {
					summ += cd.getCilCheck().doubleValue();
				}

			}
		}
		return summ;
	}

	private double countOldValidateAmount(CostAsse3 asse3, Long userId)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);
		Double total = 0d;

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					if (!cost.getSelected() && cost.getCostItemPhaseAsse3() != null
							&& asse3.getId().equals(cost.getCostItemPhaseAsse3().getId()) && cost.getUser() != null
							&& (userId == null || userId.equals(cost.getUser().getId()))) {
						if (cost.getACUSertified() && cost.getAcuCertification() != null) {
							total += cost.getAcuCertification();
						} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
							total += cost.getAguCertification();
						} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
							total += cost.getStcCertification();
						} else if (cost.getCfCheck() != null) {
							total += cost.getCfCheck();
						} else if (cost.getCilCheck() != null) {
							total += cost.getCilCheck();
						}
					}
				}
			}
		}
		return total;
	}

	private double countLastValidatedAmount(CostAsse3 asse3, Long userId) {
		double amount = 0d;
		for (CostDefinitions cost : this.getListCostDefinitionsWeb()) {
			if (cost.getSelected() && cost.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cost.getCostItemPhaseAsse3().getId()) && cost.getUser() != null
					&& (userId == null || userId.equals(cost.getUser().getId()))) {
				if (cost.getACUSertified() && cost.getAcuCertification() != null) {
					amount += cost.getAcuCertification();
				} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
					amount += cost.getAguCertification();
				} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
					amount += cost.getStcCertification();
				} else if (cost.getCfCheck() != null) {
					amount += cost.getCfCheck();
				} else if (cost.getCilCheck() != null) {
					amount += cost.getCilCheck();
				}
				// if (Boolean.TRUE.equals(cd.getCreatedByPartner())) {
				// if (cd.getCfCheck() != null) {
				// summ += cd.getCfCheck().doubleValue();
				// }
				// } else {
				// if (cd.getCilCheck() != null) {
				// summ += cd.getCilCheck().doubleValue();
				// }
				// }

			}
		}
		return amount;
	}

	private double countAmountRequestCostItemAsse3(CostAsse3 asse3, Long userId) {
		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getStcCertification() != null) {
					summ += cd.getStcCertification().doubleValue();
				}
				if (Boolean.TRUE.equals(cd.getCreatedByPartner())) {
					if (cd.getCfCheck() != null) {
						summ += cd.getCfCheck().doubleValue();
					}
				} else {
					if (cd.getCilCheck() != null) {
						summ += cd.getCilCheck().doubleValue();
					}
				}

			}
		}
		return summ;
	}

	private double countStcCertifiedCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getStcCertification() != null) {
					summ += cd.getStcCertification().doubleValue();
				}

			}
		}
		return summ;

	}

	private double countAguCertifiedCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getAguCertification() != null) {
					summ += cd.getAguCertification().doubleValue();
				}

			}
		}
		return summ;

	}

	private double countAcuCertifiedCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getAcuCertification() != null) {
					summ += cd.getAcuCertification().doubleValue();
				}

			}
		}
		return summ;

	}

	private double countLastCertifiedAmountCostItemAsse3(CostAsse3 asse3, Long userId) {

		double summ = 0d;
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected() && cd.getCostItemPhaseAsse3() != null
					&& asse3.getId().equals(cd.getCostItemPhaseAsse3().getId()) && cd.getUser() != null
					&& (userId == null || userId.equals(cd.getUser().getId()))) {
				if (cd.getLastCertifiedAmount() != null) {
					summ += cd.getLastCertifiedAmount().doubleValue();
				}

			}
		}
		return summ;

	}

	public Boolean checkBeforeSave() {
		boolean isValid = true;
		boolean missedRequired = false;
		boolean documentValid = true;

		if (this.STCCertAvailable || this.UCCertAvailable) {
			for (CostDefinitions cd : this.listCostTable) {
				if (cd.getStcCertification() != null || cd.getStcCertification().toString().isEmpty()) {
					try {
						if (cd.getCreatedByPartner()) {
							if (cd.getValueSuspendSTC() != null) {

								isValid = NumberHelper.Round(cd.getStcCertification() + cd.getValueSuspendSTC()) <= cd
										.getCfCheck();
							} else {
								isValid = cd.getStcCertification() <= cd.getCfCheck();
							}
						} else {
							if (cd.getValueSuspendSTC() != null) {
								isValid = NumberHelper.Round(cd.getStcCertification() + cd.getValueSuspendSTC()) <= cd
										.getCilCheck();
							} else {
								isValid = cd.getStcCertification() <= cd.getCilCheck();
							}
						}
					} catch (Exception e) {
					}

					if (!isValid) {
						break;
					}
				} else {
					missedRequired = true;
					break;
				}
			}

			// if (this.UCCertAvailable) {
			// }

		}

		if (this.AGUCertAvailable) {
			for (CostDefinitions cd : this.listCostTable) {
				if (cd.getAguCertification() != null || cd.getAguCertification().toString().isEmpty()) {
					if (cd.getValueSuspendAGU() != null) {
						isValid = (cd.getAguCertification()
								+ cd.getValueSuspendAGU()) <= (cd.getStcCertification() == null ? 0.0
										: cd.getStcCertification());
					} else {
						isValid = cd.getAguCertification() <= (cd.getStcCertification() == null ? 0.0
								: cd.getStcCertification());
					}

					if (!isValid) {
						break;
					}

				} else {
					missedRequired = true;
					break;
				}
			}

		}

		if (this.ACUCertAvailable) {
			for (CostDefinitions cd : this.listCostTable) {
				if (cd.getAcuCertification() != null || cd.getAcuCertification().toString().isEmpty()) {
					if (cd.getValueSuspendACU() != null) {
						isValid = NumberHelper
								.CurrencyFormat((cd.getAcuCertification() + cd.getValueSuspendACU())) <= NumberHelper
										.CurrencyFormat(cd.getAguCertification());
					} else {
						isValid = NumberHelper.CurrencyFormat(cd.getAcuCertification()) <= NumberHelper
								.CurrencyFormat(cd.getAguCertification());
					}

					if (!isValid) {
						break;
					}
				} else {
					missedRequired = true;
					break;
				}
			}

		}

		for (CostDefinitions cd : this.listCostTable) {
			if (this.STCCertAvailable || this.UCCertAvailable) {
				if (cd.getCreatedByPartner()) {
					// cf
					if (cd.getStcCheckPublicAmount() > cd.getCfCheckPublicAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPublic"));

						return false;
					}

					if (cd.getStcCheckPrivateAmount() > cd.getCfCheckPrivateAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPrivate"));

						return false;
					}

					if (cd.getStcCheckAdditionalFinansingAmount() > cd.getCfCheckAdditionalFinansingAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessAdditionalFinansing"));

						return false;
					}

					if (cd.getStcCheckInkindContributions() > cd.getCfCheckInkindContributions()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessInkindContributions"));

						return false;
					}

					if (cd.getStcCheckOutsideEligibleAreas() > cd.getCfCheckOutsideEligibleAreas()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessOutsideEligibleAreas"));

						return false;
					}

					if (cd.getStcCheckStateAidAmount() > cd.getCfCheckStateAidAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessStateAid"));

						return false;
					}

				} else {
					// cil
					if (cd.getStcCheckPublicAmount() > cd.getCilCheckPublicAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPublic"));

						return false;
					}

					if (cd.getStcCheckPrivateAmount() > cd.getCilCheckPrivateAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPrivate"));

						return false;
					}

					if (cd.getStcCheckAdditionalFinansingAmount() > cd.getCilCheckAdditionalFinansingAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessAdditionalFinansing"));

						return false;
					}

					/*if (cd.getStcCheckInkindContributions() > cd.getCilCheckInkindContributions()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessInkindContributions"));

						return false;
					} */

					if (cd.getStcCheckOutsideEligibleAreas() > cd.getCilCheckOutsideEligibleAreas()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessOutsideEligibleAreas"));

						return false;
					}

					if (cd.getStcCheckStateAidAmount() > cd.getCilCheckStateAidAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessStateAid"));

						return false;
					}
				}
			}
			if (this.AGUCertAvailable) {
				if (cd.getAguCertification() != null || cd.getAguCertification().toString().isEmpty()) {
					if (cd.getAguCheckPublicAmount() > cd.getStcCheckPublicAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPublic"));

						return false;
					}

					if (cd.getAguCheckPrivateAmount() > cd.getStcCheckPrivateAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPrivate"));

						return false;
					}

					if (cd.getAguCheckAdditionalFinansingAmount() > cd.getStcCheckAdditionalFinansingAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessAdditionalFinansing"));

						return false;
					}

					/*if (cd.getAguCheckInkindContributions() > cd.getStcCheckInkindContributions()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessInkindContributions"));

						return false;
					} */

					if (cd.getAguCheckOutsideEligibleAreas() > cd.getStcCheckOutsideEligibleAreas()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessOutsideEligibleAreas"));

						return false;
					}

					if (cd.getAguCheckStateAidAmount() > cd.getStcCheckStateAidAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessStateAid"));

						return false;
					}
				}
			}
			if (this.ACUCertAvailable) {
				if (cd.getAcuCertification() != null || cd.getAcuCertification().toString().isEmpty()) {

					if (cd.getAcuCheckPublicAmount() > cd.getAguCheckPublicAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPublic"));

						return false;
					}

					if (cd.getAcuCheckPrivateAmount() > cd.getAguCheckPrivateAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPrivate"));

						return false;
					}

					if (cd.getAcuCheckAdditionalFinansingAmount() > cd.getAguCheckAdditionalFinansingAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessAdditionalFinansing"));

						return false;
					}

					/*if (cd.getAcuCheckInkindContributions() > cd.getAguCheckInkindContributions()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessInkindContributions"));

						return false;
					} */

					if (cd.getAcuCheckOutsideEligibleAreas() > cd.getAguCheckOutsideEligibleAreas()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessOutsideEligibleAreas"));

						return false;
					}

					if (cd.getAcuCheckStateAidAmount() > cd.getAguCheckStateAidAmount()) {
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessStateAid"));

						return false;
					}
				}
			}
		}

		if (missedRequired) {
			ValidatorBean.setStaticTabMessage(Utils.getString("validatorCheckAllFields"));
			return false;
		}

		this.spanWarningVisibility = isValid ? "none" : "";

		if ((this.getDocB() == null || this.getDocB().equals(
				"")/* || this.getDocC() == null || this.getDocC().equals("") */) && this.getSessionBean().isCF()) {
			isValid = false;
		}

		// if (item.getCilCheck() == null
		// || item.getCilCheck() > item.getAmountReport())
		// }
		// if (item.getCilCheck() == null
		// || item.getCilCheck() > item.getAmountReport())
		// {
		// FacesMessage msg = new FacesMessage("");
		// msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		// FacesContext.getCurrentInstance().addMessage("", msg);
		//
		// this.setError(Utils.getString("validator.cilCheckless"));
		//
		// return false;
		// }

		return isValid && documentValid;
	}

	public Boolean BeforeSave() {
		if (checkBeforeSave()) {
			if (AGUCertAvailable) {
				if (!this.durCompilation.getSavedByAgu()) {
					this.setSpanSaveAmountsWarningVisibility("");
					return false;
				}
			} else if (ACUCertAvailable) {
				if (!this.durCompilation.getSavedByAcu()) {
					this.setSpanSaveAmountsWarningVisibility("");
					return false;
				}
			} else if (STCCertAvailable) {
				if (!this.durCompilation.getSavedByStc()) {
					this.setSpanSaveAmountsWarningVisibility("");
					return false;
				}
			}
		} else {
			return false;
		}

		return true;
	}

	public void pagingChanged(ActionEvent event) {

		this.save();
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
	if (AGUCertAvailable) {
			if (!this.durCompilation.getSavedByAgu()) {
				this.setSpanSaveAmountsWarningVisibility("");
				return;
			}
		} else if (ACUCertAvailable) {
			if (!this.durCompilation.getSavedByAcu()) {
				this.setSpanSaveAmountsWarningVisibility("");
				return;
			}
		} else if (STCCertAvailable) {
			if (!this.durCompilation.getSavedByStc()) {
				this.setSpanSaveAmountsWarningVisibility("");
				return;
			}
		}

		if (this.listCostTable == null || this.listCostTable.isEmpty()) {
			return;
		}

		if (this.CFMode) {
			if (this.durCompilation.getCreateDate() == null) {
				this.durCompilation.setCreateDate(DateTime.getNow());
			}

			this.durCompilation.setDeleted(false);
			this.durCompilation.setDurState(BeansFactory.DurStates().Load(DurStateTypes.Create.getValue()));
			this.durCompilation.setType(null);
			this.durCompilation.setReimbursed(false);
			this.durCompilation.setReimbursement(false);
			this.durCompilation.setZoneCurrentTotal(this.getZoneCurrentTotal());
			BeansFactory.DurCompilations().SaveInTransaction(this.durCompilation);

			this.getDurInfo().setSaldo(!this.getDurInfo().getIntermedio());

			this.getDurInfo().setDeleted(false);
			this.getDurInfo().setDurCompilation(this.durCompilation);

			/*
			 * this.getDurInfo().setPartner(BeansFactory.CFPartnerAnagraphs().
			 * Load ( this.getSelectedPartner()));
			 */

			if (this.getViewState().get("CDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("CDocumentToDel"));
			}
			if (this.getViewState().get("BDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("BDocumentToDel"));
			}

			BeansFactory.DurInfos().SaveInTransaction(this.getDurInfo());

			List<DurInfoToResponsiblePeople> listPeople = BeansFactory.DurInfoToResponsiblePeople()
					.getByDurInfo(this.getDurInfo().getId());

			for (DurInfoToResponsiblePeople item : listPeople) {
				BeansFactory.DurInfoToResponsiblePeople().DeleteInTransaction(item);
			}

			for (String item : this.getSelectedPartners()) {
				BeansFactory.DurInfoToResponsiblePeople().SaveInTransaction(new DurInfoToResponsiblePeople(
						this.getDurInfo(), BeansFactory.CFPartnerAnagraphs().Load(item)));
			}

			if (this.getbDocument().getFileName() != null && !this.getbDocument().getFileName().isEmpty()) {
				this.getbDocument().setUser(this.getCurrentUser());
				this.getbDocument().setProject(this.getProject());
				this.getbDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setActivityResumeDocument(this.getbDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getActivityResumeDocument());
			} else {
				this.getDurInfo().setActivityResumeDocument(null);
			}

			if (this.getcDocument().getFileName() != null && !this.getcDocument().getFileName().isEmpty()) {
				this.getcDocument().setUser(this.getCurrentUser());
				this.getcDocument().setProject(this.getProject());
				this.getcDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setFinancialDetailDocument(this.getcDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getFinancialDetailDocument());
			} else {
				this.getDurInfo().setFinancialDetailDocument(null);
			}

			BeansFactory.DurInfos().SaveInTransaction(this.getDurInfo());

			this.durSummaries.setCreateDate(DateTime.getNow());
			this.durSummaries.setDeleted(false);
			this.durSummaries.setDurCompilation(this.durCompilation);

			this.RecalculateAmounts(this.durSummaries);

			this.durSummaries.setTotalAmount(this.getTotalAmountDur());
			this.durSummaries.setTotalReimbursement(this.getTotalReimbursement());

			this.durSummaries.setTotalAmount2(this.getTotalAmountDur2());
			this.durSummaries.setFesrAmount2(this.getFesrReimbursementAmount2());
			this.durSummaries.setItCnReimbursementAmount2(this.getItCnReimbursement2());
			this.durSummaries.setFrCnReimbursementAmount2(this.getFrCnReimbursement2());

			BeansFactory.DurSummaries().SaveInTransaction(this.durSummaries);

			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				if (cd.getSelected()) {
					cd.setDurCompilation(this.getDurCompilation());
					cd.setCostState(BeansFactory.CostStates().Load(CostStateTypes.FullValidate.getState()));
				} else {
					cd.setCostState(BeansFactory.CostStates().Load(CostStateTypes.CFValidate.getState()));

					cd.setDurCompilation(null);
					cd.setAcuCertification(null);
					cd.setAcuCheckPublicAmount(null);
					cd.setAcuCheckPrivateAmount(null);
					cd.setAcuCheckAdditionalFinansingAmount(null);
					cd.setAcuCheckStateAidAmount(null);
					cd.setAcuCheckOutsideEligibleAreas(null);
					cd.setAcuCheckInkindContributions(null);
					cd.setAguCertification(null);
					cd.setAguCheckPublicAmount(null);
					cd.setAguCheckPrivateAmount(null);
					cd.setAguCheckAdditionalFinansingAmount(null);
					cd.setAguCheckStateAidAmount(null);
					cd.setAguCheckOutsideEligibleAreas(null);
					cd.setAguCheckInkindContributions(null);
					cd.setStcCertification(null);
					cd.setStcCheckPublicAmount(null);
					cd.setStcCheckPrivateAmount(null);
					cd.setStcCheckAdditionalFinansingAmount(null);
					cd.setStcCheckStateAidAmount(null);
					cd.setStcCheckOutsideEligibleAreas(null);
					cd.setStcCheckInkindContributions(null);
					cd.setAcuCertDate(null);
					cd.setStcCertDate(null);
					cd.setAguCertDate(null);
					cd.setSTCSertified(false);
					cd.setAGUSertified(false);
					cd.setACUSertified(false);
					cd.setSuspensionAmount(null);
				}
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}
		} else if ((this.STCCertAvailable || this.UCCertAvailable)
				&& durCompilation.getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue())) {
			for (CostDefinitions cd : this.listCostTable) {
				cd.setSTCSertified(true);
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			for (CostDefinitions cd : listCostDefinitionsWeb) {
				if (!cd.getSelected()) {
					cd.setDurCompilation(null);
					cd.setAcuCertification(null);
					cd.setAcuCheckPublicAmount(null);
					cd.setAcuCheckPrivateAmount(null);
					cd.setAcuCheckAdditionalFinansingAmount(null);
					cd.setAcuCheckStateAidAmount(null);
					cd.setAcuCheckOutsideEligibleAreas(null);
					cd.setAcuCheckInkindContributions(null);
					cd.setAguCertification(null);
					cd.setAguCheckPublicAmount(null);
					cd.setAguCheckPrivateAmount(null);
					cd.setAguCheckAdditionalFinansingAmount(null);
					cd.setAguCheckStateAidAmount(null);
					cd.setAguCheckOutsideEligibleAreas(null);
					cd.setAguCheckInkindContributions(null);
					cd.setStcCertification(null);
					cd.setStcCheckPublicAmount(null);
					cd.setStcCheckPrivateAmount(null);
					cd.setStcCheckAdditionalFinansingAmount(null);
					cd.setStcCheckStateAidAmount(null);
					cd.setStcCheckOutsideEligibleAreas(null);
					cd.setStcCheckInkindContributions(null);
					cd.setAcuCertDate(null);
					cd.setStcCertDate(null);
					cd.setAguCertDate(null);
					cd.setSTCSertified(false);
					cd.setAGUSertified(false);
					cd.setACUSertified(false);
					cd.setSuspensionAmount(null);
					cd.setCostState(BeansFactory.CostStates().Load(CostStateTypes.CFValidation.getState()));
				}
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			if (this.getViewState().get("UCDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("UCDocumentToDel"));
			}

			if (this.getViewState().get("CDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("CDocumentToDel"));
			}
			if (this.getViewState().get("BDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("BDocumentToDel"));
			}

			if (this.getUcDocument().getFileName() != null && !this.getUcDocument().getFileName().isEmpty()) {
				this.getUcDocument().setUser(this.getCurrentUser());
				this.getUcDocument().setProject(this.getProject());
				this.getUcDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setUcValidationDocument(this.getUcDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getUcValidationDocument());
			} else {
				this.getDurInfo().setUcValidationDocument(null);
			}

			if (this.getbDocument().getFileName() != null && !this.getbDocument().getFileName().isEmpty()) {
				this.getbDocument().setUser(this.getCurrentUser());
				this.getbDocument().setProject(this.getProject());
				this.getbDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setActivityResumeDocument(this.getbDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getActivityResumeDocument());
			} else {
				this.getDurInfo().setActivityResumeDocument(null);
			}

			if (this.getcDocument().getFileName() != null && !this.getcDocument().getFileName().isEmpty()) {
				this.getcDocument().setUser(this.getCurrentUser());
				this.getcDocument().setProject(this.getProject());
				this.getcDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setFinancialDetailDocument(this.getcDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getFinancialDetailDocument());
			} else {
				this.getDurInfo().setFinancialDetailDocument(null);
			}

			BeansFactory.DurInfos().SaveInTransaction(this.getDurInfo());

		} else if (this.AGUCertAvailable
				&& durCompilation.getDurState().getId().equals(DurStateTypes.AGUEvaluation.getValue())) {
			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				cd.setAGUSertified(true);
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			for (CostDefinitions cd : this.getListFilteredCostTable()) {
				if (!cd.getSelected()) {
					cd.setDurCompilation(null);
					cd.setAcuCertification(null);
					cd.setAcuCheckPublicAmount(null);
					cd.setAcuCheckPrivateAmount(null);
					cd.setAcuCheckAdditionalFinansingAmount(null);
					cd.setAcuCheckStateAidAmount(null);
					cd.setAcuCheckOutsideEligibleAreas(null);
					cd.setAcuCheckInkindContributions(null);
					cd.setAguCertification(null);
					cd.setAguCheckPublicAmount(null);
					cd.setAguCheckPrivateAmount(null);
					cd.setAguCheckAdditionalFinansingAmount(null);
					cd.setAguCheckStateAidAmount(null);
					cd.setAguCheckOutsideEligibleAreas(null);
					cd.setAguCheckInkindContributions(null);
					cd.setStcCertification(null);
					cd.setStcCheckPublicAmount(null);
					cd.setStcCheckPrivateAmount(null);
					cd.setStcCheckAdditionalFinansingAmount(null);
					cd.setStcCheckStateAidAmount(null);
					cd.setStcCheckOutsideEligibleAreas(null);
					cd.setStcCheckInkindContributions(null);
					cd.setAcuCertDate(null);
					cd.setStcCertDate(null);
					cd.setAguCertDate(null);
					cd.setSTCSertified(false);
					cd.setAGUSertified(false);
					cd.setACUSertified(false);
					cd.setSuspensionAmount(null);
					cd.setCostState(BeansFactory.CostStates().Load(CostStateTypes.CFValidation.getState()));
				}
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			if (this.getViewState().get("ADGDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("ADGDocumentToDel"));
			}

			if (this.getViewState().get("CDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("CDocumentToDel"));
			}

			if (this.getViewState().get("BDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("BDocumentToDel"));
			}

			if (this.getAdgDocument().getFileName() != null && !this.getAdgDocument().getFileName().isEmpty()) {
				this.getAdgDocument().setUser(this.getCurrentUser());
				this.getAdgDocument().setProject(this.getProject());
				this.getAdgDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setAdgValidationDocument(this.getAdgDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getAdgValidationDocument());
			} else {
				this.getDurInfo().setAdgValidationDocument(null);
			}

			if (this.getbDocument().getFileName() != null && !this.getbDocument().getFileName().isEmpty()) {
				this.getbDocument().setUser(this.getCurrentUser());
				this.getbDocument().setProject(this.getProject());
				this.getbDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setActivityResumeDocument(this.getbDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getActivityResumeDocument());
			} else {
				this.getDurInfo().setActivityResumeDocument(null);
			}

			if (this.getcDocument().getFileName() != null && !this.getcDocument().getFileName().isEmpty()) {
				this.getcDocument().setUser(this.getCurrentUser());
				this.getcDocument().setProject(this.getProject());
				this.getcDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setFinancialDetailDocument(this.getcDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getFinancialDetailDocument());
			} else {
				this.getDurInfo().setFinancialDetailDocument(null);
			}

			BeansFactory.DurInfos().SaveInTransaction(this.getDurInfo());

		} else if ((this.ACUCertAvailable
				&& durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue()))) {
			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				if (cd.getSuspendDocument() != null) {
					BeansFactory.Documents().SaveInTransaction(cd.getSuspendDocument());
				}

				cd.setACUSertified(true);
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			for (CostDefinitions cd : this.getListFilteredCostTable()) {
				if (!cd.getSelected()) {
					cd.setDurCompilation(null);
					cd.setAcuCertification(null);
					cd.setAcuCheckPublicAmount(null);
					cd.setAcuCheckPrivateAmount(null);
					cd.setAcuCheckAdditionalFinansingAmount(null);
					cd.setAcuCheckStateAidAmount(null);
					cd.setAcuCheckOutsideEligibleAreas(null);
					cd.setAcuCheckInkindContributions(null);
					cd.setAguCertification(null);
					cd.setAguCheckPublicAmount(null);
					cd.setAguCheckPrivateAmount(null);
					cd.setAguCheckAdditionalFinansingAmount(null);
					cd.setAguCheckStateAidAmount(null);
					cd.setAguCheckOutsideEligibleAreas(null);
					cd.setAguCheckInkindContributions(null);
					cd.setStcCertification(null);
					cd.setStcCheckPublicAmount(null);
					cd.setStcCheckPrivateAmount(null);
					cd.setStcCheckAdditionalFinansingAmount(null);
					cd.setStcCheckStateAidAmount(null);
					cd.setStcCheckOutsideEligibleAreas(null);
					cd.setStcCheckInkindContributions(null);
					cd.setAcuCertDate(null);
					cd.setStcCertDate(null);
					cd.setAguCertDate(null);
					cd.setSTCSertified(false);
					cd.setAGUSertified(false);
					cd.setACUSertified(false);
					cd.setSuspensionAmount(null);
					cd.setCostState(BeansFactory.CostStates().Load(CostStateTypes.CFValidation.getState()));
				}
				BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
			}

			if (this.getViewState().get("ADGDocumentToDel") != null) {
				BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("ADGDocumentToDel"));
			}

			if (this.getAdcDocument().getFileName() != null && !this.getAdcDocument().getFileName().isEmpty()) {
				this.getAdcDocument().setUser(this.getCurrentUser());
				this.getAdcDocument().setProject(this.getProject());
				this.getAdcDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getDurInfo().setAdcValidationDocument(this.getAdcDocument());
				BeansFactory.Documents().SaveInTransaction(this.getDurInfo().getAdcValidationDocument());
			}

			else {
				this.getDurInfo().setAdcValidationDocument(null);
			}

			BeansFactory.DurInfos().SaveInTransaction(this.getDurInfo());
		} else if (isCertifiableAvailable()) {
			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				PersistenceSessionManager.getBean().getSession().merge(cd);
			}
		}
		
//		if(Boolean.TRUE.equals(this.isSuspendedRetreatOrRecoveredDur())){
//			List<PaymentApplication> listPaNotSubmitted = BeansFactory.PaymentApplications().loadAllNotSubmitted();
//		}
		
			if(this.durCompilation != null && this.durCompilation.getPaymentApplication() != null){
				PaymentApplication pa = BeansFactory.PaymentApplications().Load(this.durCompilation.getPaymentApplication().getId());
				if(pa != null){
					Double amountToSubstract = 0d;
					if(this.getTotalSuspeded()!= null){
						amountToSubstract +=this.getTotalSuspeded();
					}
					if(this.getTotalRecovered()!=null){
						amountToSubstract += this.getTotalRecovered();
					}
					if(this.getTotalRetreat() != null){
						amountToSubstract += this.getTotalRetreat();
					}
					if(pa.getPaymentApplicationState().getId().equals(PaymentApplicationStatusTypes.Submitted.getId())){
						//aggiorno le domande non ancora presentate
						List<PaymentApplication> listNotSubmittedPa = BeansFactory.PaymentApplications().loadAllNotSubmitted();
						for(PaymentApplication pai : listNotSubmittedPa){
							pai.setTotalAmountOfCertifications(pai.getTotalAmountOfCertifications() - amountToSubstract );
							pai.setAmountEligibleExpenses(pai.getAmountEligibleExpenses() - amountToSubstract);
							pai.setTotalAmountOfPublicExpenditure(pai.getAmountEligibleExpenses());
						}
					}else{
						//aggiorno solo la domanda a cui la ddr è associata
						pa.setTotalAmountOfCertifications(pa.getTotalAmountOfCertifications() - amountToSubstract );	
						pa.setAmountEligibleExpenses(pa.getAmountEligibleExpenses() - amountToSubstract);
						pa.setTotalAmountOfPublicExpenditure(pa.getAmountEligibleExpenses());
					}
				}
			}
			
		
		
		saveChangesToCostManagament();

		saveChangesToActivityLog();
	}

	private void saveChangesToCostManagament() throws HibernateException, PersistenceBeanException {
		String projectId = String.valueOf(this.getSession().get("project"));
		// List<CFPartnerAnagraphProject> listCFPartner =
		// this.getCFPartnerList(projectId);
		List<CostManagement> cms = BeansFactory.CostManagement().lostByProject(Long.valueOf(this.getProjectId()));

		for (CostManagement cm : cms) {
			cm.setConciliated(false);
			BeansFactory.CostManagement().SaveInTransaction(cm);
		}
	}

	// private List<CFPartnerAnagraphProject> getCFPartnerList(String projectId)
	// throws HibernateException, PersistenceBeanException {
	//
	// List<CFPartnerAnagraphProject> listCFs =
	// BeansFactory.CFPartnerAnagraphProject()
	// .GetCFAnagraphForProject(projectId, CFAnagraphTypes.CFAnagraph);
	//
	// List<CFPartnerAnagraphProject> listPartners =
	// BeansFactory.CFPartnerAnagraphProject()
	// .GetCFAnagraphForProject(projectId, CFAnagraphTypes.PartnerAnagraph);
	// List<CFPartnerAnagraphProject> listCFPartner = new ArrayList<>(listCFs);
	// listCFPartner.addAll(listPartners);
	// return listCFPartner;
	// }

	private void saveChangesToActivityLog() {
		StringBuilder sb = new StringBuilder();
		DecimalFormat dec = new DecimalFormat("\u20AC ###,###,##0.00",
				DecimalFormatSymbols.getInstance(Locale.ITALIAN));
		for (CostDefinitions cd : listCostDefinitionsWeb) {
			CostDefinitionsWrapper costDefinitionsWrapper = this.costWrapers.get(cd.getId());
			if (costDefinitionsWrapper == null) {
				continue;
			}
			if (cd.getStcCertification() != null
					&& !cd.getStcCertification().equals(costDefinitionsWrapper.getStcCertification())
					|| costDefinitionsWrapper.getStcCertification() != null
							&& !costDefinitionsWrapper.getStcCertification().equals(cd.getStcCertification())) {
				if (sb.length() == 0) {
					sb.append(Utils.getString("extendedActLogDurSaveAmountCertifiedChangedItems"));
				}
				sb.append("\r\n");
				sb.append(Utils.getString("extendedActLogCostId"));
				sb.append(cd.getProgressiveId());
				sb.append(Utils.getString("extendedActLogStcCertificateAmount"));
				sb.append(" ");
				sb.append(String.valueOf(dec.format(Double.parseDouble(String.valueOf(cd.getStcCertification()))))
						.replace(" ", "\u00a0"));

			}
			if (cd.getAguCertification() != null
					&& !cd.getAguCertification().equals(costDefinitionsWrapper.getAguCertification())
					|| costDefinitionsWrapper.getAguCertification() != null
							&& !costDefinitionsWrapper.getAguCertification().equals(cd.getAguCertification())) {
				if (sb.length() == 0) {
					sb.append(Utils.getString("extendedActLogDurSaveAmountCertifiedChangedItems"));
				}
				sb.append("\r\n");
				sb.append(Utils.getString("extendedActLogCostId"));
				sb.append(cd.getProgressiveId());
				sb.append(Utils.getString("extendedActLogAguCertificateAmount"));
				sb.append(" ");
				sb.append(String.valueOf(dec.format(Double.parseDouble(String.valueOf(cd.getAguCertification()))))
						.replace(" ", "\u00a0"));
			}
			if (cd.getAcuCertification() != null
					&& !cd.getAcuCertification().equals(costDefinitionsWrapper.getAcuCertification())
					|| costDefinitionsWrapper.getAcuCertification() != null
							&& !costDefinitionsWrapper.getAcuCertification().equals(cd.getAcuCertification())) {
				if (sb.length() == 0) {
					sb.append(Utils.getString("extendedActLogDurSaveAmountCertifiedChangedItems"));
				}
				sb.append("\r\n");
				sb.append(Utils.getString("extendedActLogCostId"));
				sb.append(cd.getProgressiveId());
				sb.append(Utils.getString("extendedActLogAcuCertificateAmount"));
				sb.append(" ");
				sb.append(String.valueOf(dec.format(Double.parseDouble(String.valueOf(cd.getAcuCertification()))))
						.replace(" ", "\u00a0"));
			}
		}

		sb.append("\r\nDur ");
		ActivityLog.getInstance().addExtendedActivity(Utils.getString("extendedActLogDurSaveAmountCertified"),
				sb.toString(), Long.parseLong(this.getDurCompilation().getDurNumberTransient().toString()));

	}

	public String saveAmountsSuperAdmin() {
		if (this.getActionMotivation() == null || this.getActionMotivation().isEmpty()) {
			return null;
		}
		saveAmounts();
		clearMotivation(null);
		return null;
	}

	public void saveAmounts() {
		if (checkBeforeSave()) {
			if ((this.STCCertAvailable || this.UCCertAvailable)
					&& durCompilation.getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue())) {
				this.durCompilation.setSavedByStc(true);
			} else if (this.AGUCertAvailable
					&& durCompilation.getDurState().getId().equals(DurStateTypes.AGUEvaluation.getValue())) {
				this.durCompilation.setSavedByAgu(true);
			} else if (this.ACUCertAvailable
					&& durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue())) {
				this.durCompilation.setSavedByAcu(true);
			}

			try {
				BeansFactory.DurCompilations().Save(this.durCompilation);
			} catch (Exception e) {
				log.error(e);
			}
			save();
			this.setShowSaveAttention("none");
		} else if (getSessionBean().isSuperAdmin()) {
			this.setShowSaveAttention("");
		}
		setSelectedIndex(2);
		try {
			RecalculateAmounts();
			recalculateTotalReimbursement();
		} catch (HibernateException | PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void save() {
		Transaction tr;
		try {
			tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			StringBuilder changeLogMessages = new StringBuilder();

			for (CostDefinitions cd : this.listCostTable) {
				if ((cd.getSuspendedByACU() == null) && cd.getValueSuspendACU() != null) {
					cd.setValueSuspendACU(null);
					cd.setDateSuspendACU(null);
				}

				if ((this.STCCertAvailable || this.UCCertAvailable)
						&& durCompilation.getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue())) {
					cd.setSTCSertified(true);
				}

				if (this.AGUCertAvailable
						&& durCompilation.getDurState().getId().equals(DurStateTypes.AGUEvaluation.getValue())) {
					cd.setAGUSertified(true);
				}

				if (this.ACUCertAvailable
						&& durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue())) {
					cd.setACUSertified(true);
				}

				try {
					BeansFactory.CostDefinitions().SaveInTransaction(cd, getDurCompilation());
				} catch (HibernateException e) {
					log.error(e);
				} catch (PersistenceBeanException e) {
					log.error(e);
				}
				if (getSessionBean().isSuperAdmin()) {
					StringBuilder localChanges = new StringBuilder();
					CertificationWrapper certWrap = this.getCertifications().get(cd.getId());
					if (cd.getStcCertification() != null && !cd.getStcCertification().equals(certWrap.getStcCert())
							|| certWrap.getStcCert() != null
									&& !certWrap.getStcCert().equals(cd.getStcCertification())) {
						localChanges.append(Utils.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("durCompilationEditCostTableHeaderSTC"))
								.replace("%2", String.valueOf(certWrap.getStcCert()))
								.replace("%3", String.valueOf(cd.getStcCertification()))).append("<br/>");
					}
					if (cd.getAguCertification() != null && !cd.getAguCertification().equals(certWrap.getAguCert())
							|| certWrap.getAguCert() != null
									&& !certWrap.getAguCert().equals(cd.getAguCertification())) {
						localChanges.append(Utils.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("durCompilationEditCostTableHeaderAGU"))
								.replace("%2", String.valueOf(certWrap.getAguCert()))
								.replace("%3", String.valueOf(cd.getAguCertification()))).append("<br/>");
					}
					if (cd.getAcuCertification() != null && !cd.getAcuCertification().equals(certWrap.getAcuCert())
							|| certWrap.getAcuCert() != null
									&& !certWrap.getAcuCert().equals(cd.getAcuCertification())) {
						localChanges.append(Utils.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("durCompilationEditCostTableHeaderACU"))
								.replace("%2", String.valueOf(certWrap.getAcuCert()))
								.replace("%3", String.valueOf(cd.getAcuCertification()))).append("<br/>");
					}
					if (localChanges.length() > 0) {
						changeLogMessages.append(Utils.getString("superAdminChangeLogMessageHeader")
								.replace("%1", Utils.getString("superAdminChangesCostDefinition"))
								.replace("%2", cd.getProgressiveId().toString())
								.replace("%3", Utils.getString("durCompilationListName") + " Edit") + "<br/>");
						changeLogMessages.append(localChanges.toString());
					}
					this.getCertifications().put(cd.getId(), new CertificationWrapper(cd));
				}
			}
			if (getSessionBean().isSuperAdmin()) {
				if (changeLogMessages.length() > 0) {
					SuperAdminChange changes = new SuperAdminChange(ChangeType.VALUE_CHANGE, this.getCurrentUser(),
							this.getActionMotivation(), getSessionBean().getProject());
					changeLogMessages.insert(0,
							Utils.getString("superAdminChangeLogMessageHeader")
									.replace("%1", Utils.getString("superAdminChangesDurCompilation"))
									.replace("%2", getDurInfo().getDurInfoNumber().toString())
									.replace("%3", Utils.getString("durCompilationListName") + " Edit") + "<br/>");
					changes.setChanges(changeLogMessages.toString());
					BeansFactory.SuperAdminChanges().SaveInTransaction(changes);
				}

			}

			tr.commit();
			recalcSum();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	private void recalcSum() throws HibernateException, PersistenceBeanException {
		Double sum = 0d;
		for (CostDefinitions cd : this.listCostTable) {
			if (!Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
				if (cd.getAcuCertification() != null && cd.getAcuCertification() > 0 && cd.getACUSertified()) {
					sum += cd.getAcuCertification();
				} else if (cd.getAguCertification() != null && cd.getAguCertification() > 0 && cd.getAGUSertified()) {
					sum += cd.getAguCertification();
				} else if (cd.getStcCertification() != null && cd.getStcCertification() > 0 && cd.getSTCSertified()) {
					sum += cd.getStcCertification();
				} else if (cd.getCfCheck() != null) {
					sum += cd.getCfCheck();
				} else if (cd.getCilCheck() != null) {
					sum += cd.getCilCheck();
				} else {
					sum += cd.getAmountReport();
				}
			}
		}

		this.durSummaries.setTotalAmount(sum);
		this.setTotalAmountDur(sum);
		BeansFactory.DurSummaries().Save(this.durSummaries);
	}

	private void InitPaging() {
		itemsPerPage = "10";
		setItemsPerPageList(new ArrayList<SelectItem>());
		for (int i = 1; i < 6; i++) {
			getItemsPerPageList().add(new SelectItem(String.valueOf(10 * i), String.valueOf(10 * i)));
		}
	}

	public void ReRenderScroll() {
		if (this.getListCostTable() != null && this.getListCostTable().size() > Integer.parseInt(getItemsPerPage())) {
			setShowScroll(true);
		} else {
			setShowScroll(false);
		}
	}

	private void LoadDocuments() throws NumberFormatException, HibernateException, PersistenceBeanException {
		// B document
		Date documentDate = new Date();
		if (this.getViewState().get("BDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("BDocument");
			try {
				String newFileName = FileHelper.newFileName(docinfo.getName());

				FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName));
				this.setDocB(docinfo.getName());
				this.setbDocument(new Documents());
				this.getbDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				this.getbDocument().setDocumentDate(docinfo.getDate());
				this.getbDocument().setCreateDate(DateTime.getNow());
				this.getbDocument().setDeleted(false);
				this.getbDocument().setProject(this.getProject());
				this.getbDocument().setDocumentDate(docinfo.getDate());
				this.getbDocument().setName(docinfo.getName());
				this.getbDocument().setCategory(docinfo.getCategory());
				this.getbDocument().setIsPublic(docinfo.getIsPublic());
				if (this.getBRole() != null && !this.getBRole().isEmpty()) {
					this.getbDocument().setUploadRole(Integer.parseInt(this.getBRole()));
				}
				this.getbDocument().setFileName(newFileName);
				this.getbDocument().setProtocolNumber(docinfo.getProtNumber());
			} catch (IOException e) {
				log.error(e);
			}

		} else if (this.getDurInfo().getActivityResumeDocument() != null
				&& this.getViewState().get("deleteDocB") == null) {
			this.setDocB(this.getDurInfo().getActivityResumeDocument().getName());
			this.setbDocument(this.getDurInfo().getActivityResumeDocument());
		} else {
			this.setbDocument(new Documents());
			this.getbDocument().setDocumentDate(documentDate);
			this.getbDocument().setEditableDate(documentDate);
			this.setDocB("");
		}

		// C document
		if (this.getViewState().get("CDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("CDocument");
			try {
				String newFileName = FileHelper.newFileName(docinfo.getName());
				FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName));
				this.setDocC(docinfo.getName());
				this.setcDocument(new Documents());
				this.getcDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				this.getcDocument().setDocumentDate(docinfo.getDate());

				this.getcDocument().setCreateDate(DateTime.getNow());
				this.getcDocument().setDeleted(false);
				this.getcDocument().setDocumentDate(docinfo.getDate());
				this.getcDocument().setName(docinfo.getName());
				this.getcDocument().setFileName(newFileName);
				this.getcDocument().setCategory(docinfo.getCategory());
				this.getcDocument().setIsPublic(docinfo.getIsPublic());
				if (this.getCRole() != null && !this.getCRole().isEmpty()) {
					this.getcDocument().setUploadRole(Integer.parseInt(this.getCRole()));
				}
				this.getcDocument().setProtocolNumber(docinfo.getProtNumber());
				this.getcDocument().setProject(this.getProject());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (this.getDurInfo().getFinancialDetailDocument() != null
				&& this.getViewState().get("deleteDocC") == null) {
			this.setDocC(this.getDurInfo().getFinancialDetailDocument().getName());
			this.setcDocument(this.getDurInfo().getFinancialDetailDocument());
		} else {
			this.setcDocument(new Documents());
			this.getcDocument().setDocumentDate(documentDate);
			this.getcDocument().setEditableDate(documentDate);
			this.setDocC("");
		}

		if (this.getViewState().get("UCDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("UCDocument");
			try {
				String newFileName = FileHelper.newFileName(docinfo.getName());

				FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName));
				this.setDocUC(docinfo.getName());
				this.setUcDocument(new Documents());
				this.getUcDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				this.getUcDocument().setDocumentDate(docinfo.getDate());
				this.getUcDocument().setCreateDate(DateTime.getNow());
				this.getUcDocument().setDeleted(false);
				this.getUcDocument().setProject(this.getProject());
				this.getUcDocument().setDocumentDate(docinfo.getDate());
				this.getUcDocument().setName(docinfo.getName());
				this.getUcDocument().setCategory(docinfo.getCategory());
				this.getUcDocument().setIsPublic(docinfo.getIsPublic());
				if (this.getUCRole() != null && !this.getUCRole().isEmpty()) {
					this.getUcDocument().setUploadRole(Integer.parseInt(this.getUCRole()));
				}
				this.getUcDocument().setFileName(newFileName);
				this.getUcDocument().setProtocolNumber(docinfo.getProtNumber());
			} catch (IOException e) {
				log.error(e);
			}

		} else if (this.getDurInfo().getUcValidationDocument() != null) {
			this.setDocUC(this.getDurInfo().getUcValidationDocument().getName());
			this.setUcDocument(new Documents());
		} else {
			this.setUcDocument(new Documents());
			this.getUcDocument().setDocumentDate(documentDate);
			this.getUcDocument().setEditableDate(documentDate);
			this.setDocUC("");
		}

		if (this.getViewState().get("ADCDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("ADCDocument");
			try {
				String newFileName = FileHelper.newFileName(docinfo.getName());

				FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName));
				this.setDocADC(docinfo.getName());
				this.setAdcDocument(new Documents());
				this.getAdcDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				this.getAdcDocument().setDocumentDate(docinfo.getDate());
				this.getAdcDocument().setCreateDate(DateTime.getNow());
				this.getAdcDocument().setDeleted(false);
				this.getAdcDocument().setProject(this.getProject());
				this.getAdcDocument().setDocumentDate(docinfo.getDate());
				this.getAdcDocument().setName(docinfo.getName());
				this.getAdcDocument().setCategory(docinfo.getCategory());
				this.getAdcDocument().setIsPublic(docinfo.getIsPublic());
				if (this.getADCRole() != null && !this.getADCRole().isEmpty()) {
					this.getAdcDocument().setUploadRole(Integer.parseInt(this.getADCRole()));
				}
				this.getAdcDocument().setFileName(newFileName);
				this.getAdcDocument().setProtocolNumber(docinfo.getProtNumber());
			} catch (IOException e) {
				log.error(e);
			}

		} else if (this.getDurInfo().getAdcValidationDocument() != null) {
			this.setDocADC(this.getDurInfo().getAdcValidationDocument().getName());
			this.setAdcDocument(new Documents());
		} else {
			this.setAdcDocument(new Documents());
			this.getAdcDocument().setDocumentDate(documentDate);
			this.getAdcDocument().setEditableDate(documentDate);
			this.setDocADC("");
		}

		if (this.getViewState().get("ADGDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("ADGDocument");
			try {
				String newFileName = FileHelper.newFileName(docinfo.getName());

				FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName));
				this.setDocADG(docinfo.getName());
				this.setAdgDocument(new Documents());
				this.getAdgDocument().setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				this.getAdgDocument().setDocumentDate(docinfo.getDate());
				this.getAdgDocument().setCreateDate(DateTime.getNow());
				this.getAdgDocument().setDeleted(false);
				this.getAdgDocument().setProject(this.getProject());
				this.getAdgDocument().setDocumentDate(docinfo.getDate());
				this.getAdgDocument().setName(docinfo.getName());
				this.getAdgDocument().setCategory(docinfo.getCategory());
				this.getAdgDocument().setIsPublic(docinfo.getIsPublic());
				if (this.getADGRole() != null && !this.getADGRole().isEmpty()) {
					this.getAdgDocument().setUploadRole(Integer.parseInt(this.getADGRole()));
				}
				this.getAdgDocument().setFileName(newFileName);
				this.getAdgDocument().setProtocolNumber(docinfo.getProtNumber());
			} catch (IOException e) {
				log.error(e);
			}

		} else if (this.getDurInfo().getAdgValidationDocument() != null) {
			this.setDocADG(this.getDurInfo().getAdgValidationDocument().getName());
			this.setAdgDocument(new Documents());
		} else {
			this.setAdgDocument(new Documents());
			this.getAdgDocument().setDocumentDate(documentDate);
			this.getAdgDocument().setEditableDate(documentDate);
			this.setDocADG("");
		}

		this.setSuspendDocument(new Documents());
		this.getSuspendDocument().setDocumentDate(documentDate);
		this.getSuspendDocument().setEditableDate(documentDate);
		this.setDocSuspend("");

		this.setRetreatDocument(new Documents());
		this.getRetreatDocument().setDocumentDate(new Date());
		this.getRetreatDocument().setEditableDate(new Date());

		this.setDurRecoveryDocument(new Documents());
		this.getDurRecoveryDocument().setDocumentDate(new Date());
		this.getDurRecoveryDocument().setEditableDate(new Date());
	}

	private List<CostDefinitions> LoadEntities() throws PersistenceBeanException {
		List<CostDefinitions> listCD = new ArrayList<CostDefinitions>();

		if (this.getSession().get("durCompilationEdit") != null) {
			if (this.getListCostDefinitionsBackup() != null && !needReload) {
				listCD.addAll(this.getListCostDefinitionsBackup());
			} else {
				listCD.addAll(BeansFactory.CostDefinitions().GetByDurCompilation(this.durCompilation.getId()));

				// test
				this.setListCostDefinitionsBackUp(listCD);

			}

			if (this.getViewState().get("durCompilationEditListCD") == null) {
				for (CostDefinitions cd : listCD) {
					cd.setSelected(true);
				}
			}
		}

		if (this.isCFAvailable()) {
			List<CostDefinitions> listCDFree = BeansFactory.CostDefinitions().GetByDurCompilation(null);

			if (this.getViewState().get("durCompilationEditListCD") == null) {
				for (CostDefinitions cd : listCDFree) {
					cd.setSelected(false);
				}
			}

			listCD.addAll(listCDFree);
		}

		return listCD;
	}

	private void CheckRoles() {
		if (!this.getSessionBean().getIsAguRead() && (this.getDurCompilation().getDurState() == null
				|| this.getDurCompilation().getDurState().getId().equals(DurStateTypes.Create.getValue()))) {
			this.setCFMode(true);
		}
		if (this.getSessionBean().isCF()) {
			this.setCFMode(true);
		}
		if (this.getSessionBean().isSTC() || this.getSessionBean().isSuperAdmin()) {
			this.setSTCMode(true);
		}
		if (this.getSessionBean().isAGU() || this.getSessionBean().isSuperAdmin()) {
			this.setAGUMode(true);
		}
		if (this.getSessionBean().isACU() || this.getSessionBean().isSuperAdmin()) {
			this.setACUMode(true);
		}
		if (this.getSessionBean().isUC()) {
			this.setUCMode(true);
		}
	}

	public void doSelectAll() throws PersistenceBeanException {
		List<Long> listIndices = new ArrayList<Long>();

		if (this.isSelectAll()) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				cd.setSelected(true);
				listIndices.add(cd.getId());
			}
		} else {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				cd.setSelected(false);
			}
		}

		this.getViewState().put("durCompilationEditListCD", listIndices);
		this.setCostListChanged(true);
		this.RecalculateAmounts();
		needReload = true;
		this.Page_Load();
	}

	public void doSelectItem() throws PersistenceBeanException {
		this.setSelectAll(false);
		List<Long> listIndices = new ArrayList<Long>();
		for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
			if (cd.getSelected()) {
				listIndices.add(cd.getId());
			}
		}

		this.getViewState().put("durCompilationEditListCD", listIndices);
		this.setCostListChanged(true);
		this.RecalculateAmounts();
		needReload = true;
		this.Page_Load();
	}

	@SuppressWarnings("unchecked")
	private void RecheckCostItems(List<CostDefinitions> list)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("durCompilationEditListCD") != null) {
			List<Long> listIndices = (List<Long>) this.getViewState().get("durCompilationEditListCD");

			for (CostDefinitions cd : list) {
				if (listIndices.contains(cd.getId())) {
					cd.setSelected(true);
				} else {
					cd.setSelected(false);
				}
			}
		}

		this.setListCostDefinitionsWeb(list);
		this.setListCostDefinitionsBackUp(list);

	}

	private void FillDropDowns() throws HibernateException, NumberFormatException, PersistenceBeanException {
		this.FillInvolvedPartners();
		this.fillRoles();

		if (Boolean.FALSE.equals(this.getAsse3Mode())) {
			this.fillPhases();
			this.setFilterCostItemValues(new ArrayList<SelectItem>());
			this.filterCostItemValues.add(SelectItemHelper.getAllElement());
			for (CostItems item : BeansFactory.CostItems().Load()) {
				filterCostItemValues.add(new SelectItem(item.getId().toString(), item.getValue()));
			}
		} else {
			this.FillCostItemsAsse3();
			this.FillAsse3Phases();
		}

		this.fillCategories();

		this.setFilterPartnerValues(new ArrayList<SelectItem>());
		this.setAllReasonTypes(new ArrayList<SelectItem>());
		this.setAllRetReasonTypes(new ArrayList<SelectItem>());
		this.setAllRetControllerTypes(new ArrayList<SelectItem>());
		this.setAllSuspControllerTypes(new ArrayList<SelectItem>());
		this.setAllDurRecoveryReasonTypes(new ArrayList<SelectItem>());
		this.setAllDurRecoveryActTypes(new ArrayList<SelectItem>());
		this.setFilterPartnersForGrid(new ArrayList<SelectItem>());
		this.setFilterPartnersForGridWp(new ArrayList<SelectItem>());
		this.filterPartnerValues.add(SelectItemHelper.getAllElement());
		this.allReasonTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.allRetReasonTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.allRetControllerTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.allSuspControllerTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.allDurRecoveryReasonTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.allDurRecoveryActTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		List<CFPartnerAnagraphs> listPart = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());

		this.filterPartnersForGrid.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		this.filterPartnersForGridWp.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
		for (CFPartnerAnagraphs partner : listPart) {
			if (partner.getUser() == null) {
				if (this.getProject().getAsse() == 5) {
					this.filterPartnerValues.add(new SelectItem("-1", partner.getName()));
				}
			} else {
				this.filterPartnerValues.add(new SelectItem(partner.getUser().getId(), partner.getName()));
			}
			this.filterPartnersForGrid.add(new SelectItem(partner.getId(), partner.getName()));
			this.filterPartnersForGridWp.add(new SelectItem(partner.getId(), partner.getName()));

		}

		for (ReasonType type : ReasonType.values()) {
			this.allReasonTypes.add(new SelectItem(type.getId(), type.toString(Boolean.TRUE)));
		}

		for (ReasonType type : ReasonType.values()) {
			if (!ReasonType.OTHER.getId().equals(type.getId())) {
				this.allRetReasonTypes.add(new SelectItem(type.getId(), type.toString(Boolean.FALSE)));
			}
		}

		for (ControllerTypes type : ControllerTypes.values()) {
			this.allRetControllerTypes.add(new SelectItem(type.getId(), type.toString()));
		}

		for (ControllerTypes type : ControllerTypes.values()) {
			this.allSuspControllerTypes.add(new SelectItem(type.getId(), type.toString()));
		}

		for (RecoverReasonType type : RecoverReasonType.values()) {
			this.allDurRecoveryReasonTypes.add(new SelectItem(type.getId(), type.toString()));
		}

		for (RecoverActType type : RecoverActType.values()) {
			this.allDurRecoveryActTypes.add(new SelectItem(type.getId(), type.toString()));
		}
	}

	public void asse3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException {
		this.setSelectedCostPhaseAsse3((String) event.getNewValue());
		this.FillCostItemsAsse3();
	}

	private void fillPhases() throws PersistenceBeanException {
		setFilterPhases(new ArrayList<SelectItem>());
		List<CostDefinitionPhases> list = BeansFactory.CostDefinitionPhase().Load();
		getFilterPhases().add(SelectItemHelper.getAllElement());
		for (CostDefinitionPhases item : list) {
			getFilterPhases().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}

	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
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

	private void FillCostItemsAsse3() throws PersistenceBeanException {

		setFilterCostItemAsse3(new ArrayList<SelectItem>());
		getFilterCostItemAsse3().add(SelectItemHelper.getFirst());
		if (this.getSelectedCostPhaseAsse3() != null && !this.getSelectedCostPhaseAsse3().isEmpty()) {
			PhaseAsse3Types phase = null;
			try {
				phase = PhaseAsse3Types.valueOf(this.getSelectedCostPhaseAsse3());
			} catch (Exception e) {
				log.error("Enum is not compatible", e);
				return;
			}
			List<CostAsse3> costs = BeansFactory.CostAsse3().GetByPhase(phase);

			for (CostAsse3 cost : costs) {
				getFilterCostItemAsse3().add(new SelectItem(cost.getValue(), cost.getValue()));
			}

		}

	}

	private void FillAsse3Phases() {
		setFilterCostPhaseAsse3(new ArrayList<SelectItem>());
		getFilterCostPhaseAsse3().add(SelectItemHelper.getFirst());
		for (PhaseAsse3Types type : PhaseAsse3Types.values()) {
			getFilterCostPhaseAsse3().add(new SelectItem(type.name(), type.toString()));
		}

	}

	private void FillInvolvedPartners() throws HibernateException, NumberFormatException, PersistenceBeanException {
		this.listInvolvedPartners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getCurrentProjectId());
		this.listInvolvedPartnersWeb = new ArrayList<SelectItem>();
		// this.listInvolvedPartnersWeb.add(SelectItemHelper.getFirst());

		for (CFPartnerAnagraphs p : this.listInvolvedPartners) {
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(p.getId()));

			if (p.getName() != null) {
				item.setLabel(p.getName());
				if (p.getUser().getRolesNames() != null) {
					item.setLabel(p.getName() + " - " + p.getUser().getRolesNames());
				}
			}
			CFPartnerAnagraphProject partnerAnagraphProject = BeansFactory.CFPartnerAnagraphProject()
					.LoadByPartner(this.getProject().getId(), p.getId());
			if (partnerAnagraphProject != null && !partnerAnagraphProject.isNotAssigned()
					&& (p.getUser() == null || !Boolean.TRUE.equals(p.getUser().getFictive()))) {
				this.listInvolvedPartnersWeb.add(item);
			}
		}
	}

	private void LoadBlankData() {
		this.getDurInfo().setDurCompilationDate(DateTime.getNow());
	}

	// Documents logic

	public void getBDoc() {
		if (this.getViewState().get("BDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("BDocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getDurInfo().getActivityResumeDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getDurInfo().getActivityResumeDocument().getName(),
					this.getDurInfo().getActivityResumeDocument().getFileName(), null, null, null), false);
		}
	}

	public void deleteBDoc() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("BDocument") != null) {
			this.getViewState().put("BDocument", null);
		} else if (this.getDurInfo().getActivityResumeDocument() != null) {
			this.getViewState().put("BDocumentToDel",
					BeansFactory.Documents().Load(this.getDurInfo().getActivityResumeDocument().getId()).getId());
			this.getDurInfo().setActivityResumeDocument(null);
		}
		this.getViewState().put("deleteDocB", true);
		this.Page_Load();
	}

	public void getUCDoc() {
		if (this.getViewState().get("UCDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("UCDocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getDurInfo().getUcValidationDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getDurInfo().getUcValidationDocument().getName(),
					this.getDurInfo().getUcValidationDocument().getFileName(), null, null, null), false);
		}
	}

	public void deleteUCDoc() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("UCDocument") != null) {
			this.getViewState().put("UCDocument", null);
		} else if (this.getDurInfo().getUcValidationDocument() != null) {
			this.getViewState().put("UCDocumentToDel",
					BeansFactory.Documents().Load(this.getDurInfo().getUcValidationDocument().getId()).getId());
			this.getDurInfo().setUcValidationDocument(null);
		}

		this.Page_Load();
	}

	public void getADCDoc() {
		if (this.getViewState().get("ADCDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("ADCDocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getDurInfo().getAdcValidationDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getDurInfo().getAdcValidationDocument().getName(),
					this.getDurInfo().getAdcValidationDocument().getFileName(), null, 
					this.getDurInfo().getAdcValidationDocument().getSignflag()), false);
		}
	}

	public void deleteADCDoc() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("ADCDocument") != null) {
			this.getViewState().put("ADCDocument", null);
		} else if (this.getDurInfo().getAdcValidationDocument() != null) {
			this.getViewState().put("ADCDocumentToDel",
					BeansFactory.Documents().Load(this.getDurInfo().getAdcValidationDocument().getId()).getId());
			this.getDurInfo().setAdcValidationDocument(null);
		}

		this.Page_Load();
	}

	public void getADGDoc() {
		if (this.getViewState().get("ADGDocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("ADGDocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getDurInfo().getAdgValidationDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getDurInfo().getAdgValidationDocument().getName(),
					this.getDurInfo().getAdgValidationDocument().getFileName(), null, 
					this.getDurInfo().getAdgValidationDocument().getSignflag()), false);
		}
	}

	public void deleteADGDoc() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("ADGDocument") != null) {
			this.getViewState().put("ADGDocument", null);
		} else if (this.getDurInfo().getAdgValidationDocument() != null) {
			this.getViewState().put("ADGDocumentToDel",
					BeansFactory.Documents().Load(this.getDurInfo().getAdgValidationDocument().getId()).getId());
			this.getDurInfo().setAdgValidationDocument(null);
		}

		this.Page_Load();
	}

	public void getCostDoc() {
		getDoc(false);
	}

	private void getDoc(boolean payment) {
		if (this.getEntityEditId() != null && !this.getEntityEditId().equals(0l)) {
			CostDefinitions cd = null;
			try {
				cd = BeansFactory.CostDefinitions().Load(String.valueOf(this.getEntityEditId()));
			} catch (Exception e) {
				log.error(e);
			}

			if (cd != null
					&& ((!payment && cd.getCostDocument() != null) || (payment && cd.getPaymentDocument() != null))) {
				FileHelper.sendFile(new DocumentInfo(null,
						payment ? cd.getPaymentDocument().getName() : cd.getCostDocument().getName(),
						payment ? cd.getPaymentDocument().getFileName() : cd.getCostDocument().getFileName(), null, null, null),
						false);
			}
		}
	}

	public void downloadDocuments() {
		if (this.getEntityEditId() != null && !this.getEntityEditId().equals(0l)) {
			CostDefinitions cd = null;
			try {
				cd = BeansFactory.CostDefinitions().Load(String.valueOf(this.getEntityEditId()));
			} catch (Exception e) {
				log.error(e);
			}

			if (cd != null) {
				List<Documents> docs = new ArrayList<Documents>();

				try {
					for (CostDefinitionsToDocuments item : BeansFactory.CostDefinitionsToDocuments()
							.getByCostDefinition(cd.getId())) {
						if (item.getDocument() != null) {
							docs.add(item.getDocument());
						}
					}
				} catch (Exception e) {
					log.error(e);
				}

				if (docs != null && !docs.isEmpty()) {
					Map<String, byte[]> fileMap = new HashMap<String, byte[]>();

					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ZipOutputStream zipfile = new ZipOutputStream(bos);
					ZipEntry zipentry = null;
					int allSize = 0;
					FileInputStream fi = null;

					for (Documents doc : docs) {
						try {
							File file = new File(FileHelper.getFileDirectory(), doc.getFileName());
							fi = new FileInputStream(file);

							int sizeFS = (int) file.length();
							allSize += sizeFS;
							byte[] fileFS = new byte[sizeFS];
							fi.read(fileFS);
							fi.close();
							fileMap.put(doc.getName(), fileFS);
						} catch (Exception e) {
							log.error(e);
						}
					}

					Iterator<String> i = fileMap.keySet().iterator();

					try {
						while (i.hasNext()) {
							String fileName = (String) i.next();
							zipentry = new ZipEntry(fileName);
							zipfile.putNextEntry(zipentry);
							zipfile.write((byte[]) fileMap.get(fileName));
						}
						zipfile.close();

						InputStream is = new ByteArrayInputStream(bos.toByteArray());

						FileHelper.sendFile(Utils.getString("documents") + "_" + cd.getProgressiveId() + ".zip", is,
								allSize);

						is.close();
					} catch (Exception e) {
						log.error(e);
					}
				}
			}
		}
	}

	public void showNotes() {
		if (this.getEntityEditId() != null && !this.getEntityEditId().equals(0l)) {
			try {
				setNotesList(BeansFactory.CostDefinitionsToNotes().getByCostDefinition(this.getEntityEditId()));
			} catch (PersistenceBeanException e) {
				log.error(e);
			}
		}
	}

	public void getPaymentDoc() {
		getDoc(true);
	}

	// public void getCDoc() {
	// if (this.getViewState().get("CDocument") != null) {
	// DocumentInfo docinfo = (DocumentInfo)
	// this.getViewState().get("CDocument");
	//
	// FileHelper.sendFile(docinfo, true);
	// } else if (this.getDurInfo().getFinancialDetailDocument() != null) {
	// FileHelper.sendFile(new DocumentInfo(null,
	// this.getDurInfo().getFinancialDetailDocument().getName(),
	// this.getDurInfo().getFinancialDetailDocument().getFileName(), null),
	// false);
	// }
	// }

	public void deleteCDoc() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getViewState().get("CDocument") != null) {
			this.getViewState().put("CDocument", null);
		} else if (this.getDurInfo().getFinancialDetailDocument() != null) {
			this.getViewState().put("CDocumentToDel",
					BeansFactory.Documents().Load(this.getDurInfo().getFinancialDetailDocument().getId()).getId());
			this.getDurInfo().setFinancialDetailDocument(null);
		}
		this.getViewState().put("deleteDocC", true);
		this.Page_Load();
	}

	public void saveBDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getbDocumentUpFile() != null && this.getbDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getbDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getbDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getbDocument().getDocumentDate(),
						this.getbDocumentUpFile().getName(), fileName, this.getbDocument().getProtocolNumber(), cat,
						this.getbDocument().getIsPublic(),this.getbDocument().getSignflag());
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("BDocument", doc);
				this.setSelectedBPartner(null);
				this.setBRole(null);
				this.setSelectedCategory(null);
				this.getViewState().remove("deleteDocB");
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	public void saveCDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getcDocumentUpFile() != null && this.getcDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getcDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getcDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getcDocument().getDocumentDate(),
						this.getcDocumentUpFile().getName(), fileName, this.getcDocument().getProtocolNumber(), cat,
						this.getcDocument().getIsPublic(), null);
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("CDocument", doc);
				this.setSelectedCPartner(null);
				this.setCRole(null);
				this.setSelectedCategory(null);
				this.getViewState().remove("deleteDocC");
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	public void saveUCDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getUcDocumentUpFile() != null && this.getUcDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getUcDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getUcDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getUcDocument().getDocumentDate(),
						this.getUcDocumentUpFile().getName(), fileName, this.getUcDocument().getProtocolNumber(), cat,
						this.getUcDocument().getIsPublic(), null);
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("UCDocument", doc);
				this.setSelectedUCPartner(null);
				this.setUCRole(null);
				this.setSelectedCategory(null);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	public void saveADCDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getAdcDocumentUpFile() != null && this.getAdcDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getAdcDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getAdcDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getAdcDocument().getDocumentDate(),
						this.getAdcDocumentUpFile().getName(), fileName, this.getAdcDocument().getProtocolNumber(), cat,
						this.getAdcDocument().getIsPublic(), null);
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("ADCDocument", doc);
				this.setSelectedADCPartner(null);
				this.setADCRole(null);
				this.setSelectedCategory(null);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	public void calcelSaveSuspendDocument() {
		this.setNeedSaveSuspendDocument(Boolean.FALSE);
	}

	public void enableSaveSuspendDocument() {
		this.setNeedSaveSuspendDocument(Boolean.TRUE);
	}

	public void saveSuspendDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getSuspendDocumentUpFile() != null && this.getSuspendDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getSuspendDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getSuspendDocumentUpFile().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(this.getSuspendDocument().getDocumentDate(),
						this.getSuspendDocumentUpFile().getName(), fileName,
						this.getSuspendDocument().getProtocolNumber(), cat, this.getSuspendDocument().getIsPublic(), null);

				this.getSuspendDocument().setFileName(fileName);
				this.getSuspendDocument().setName(this.getSuspendDocumentUpFile().getName());
				this.getSuspendDocument().setCategory(cat);
				this.getSuspendDocument().setUser(this.getCurrentUser());
				this.getSuspendDocument().setProject(this.getProject());
				this.getSuspendDocument()
						.setSection(BeansFactory.Sections().Load(DocumentSections.DURCompilation.getValue()));
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("suspendDocument", doc);
				this.setSelectedSuspendPartner(null);
				this.setSuspendRole(null);
				this.setSelectedCategory(null);
				this.setDocSuspend(this.getSuspendDocumentUpFile().getName());
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

	}

	public void saveADGDocument() throws NumberFormatException, HibernateException, PersistenceBeanException {
		try {
			if (this.getAdgDocumentUpFile() != null && this.getAdgDocument() != null) {
				String fileName = FileHelper.newTempFileName(this.getAdgDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getAdgDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getAdgDocument().getDocumentDate(),
						this.getAdgDocumentUpFile().getName(), fileName, this.getAdgDocument().getProtocolNumber(), cat,
						this.getAdgDocument().getIsPublic(), null);
				this.getViewState().put("addedDocument", true);
				this.getViewState().put("ADGDocument", doc);
				this.setSelectedADGPartner(null);
				this.setADGRole(null);
				this.setSelectedCategory(null);
			}
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		this.Page_Load();
	}

	private void FillCostTable() throws PersistenceBeanException {
		if (getListFilteredCostTable() == null || getListFilteredCostTable().isEmpty() || needReload) {
			needReload = false;
			this.listCostTable = new ArrayList<CostDefinitions>();
			this.costWrapers = new HashMap<Long, CostDefinitionsWrapper>();
			this.setListFilteredCostTable(new ArrayList<CostDefinitions>());
			Map<Long, CertificationWrapper> certs = new HashMap<Long, CertificationWrapper>();
			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				certs.put(cd.getId(), new CertificationWrapper(cd));
				if (cd.getSelected()) {
					listCostTable.add(cd);
					costWrapers.put(cd.getId(), new CostDefinitionsWrapper(cd));
				}
			}
			if (this.getCertifications() == null) {
				this.setCertifications(certs);
			}
			for (CostDefinitions cd : listCostTable) {
				if (this.isEditMode() && this.isSTCCertAvailable()) {
					cd.setStcCertDate(DateTime.getNow());

					if (cd.getCreatedByPartner() && cd.getStcCertification() == null) {
						cd.setStcCertification(cd.getCfCheck());
						cd.setStcCheckPublicAmount(cd.getCfCheckPublicAmount());
						cd.setStcCheckPrivateAmount(cd.getCfCheckPrivateAmount());
						cd.setStcCheckAdditionalFinansingAmount(cd.getCfCheckAdditionalFinansingAmount());
						cd.setStcCheckStateAidAmount(cd.getCfCheckStateAidAmount());
						cd.setStcCheckOutsideEligibleAreas(cd.getCfCheckOutsideEligibleAreas());
						cd.setStcCheckInkindContributions(cd.getCfCheckInkindContributions());
					} else if (cd.getStcCertification() == null) {
						cd.setStcCertification(cd.getCilCheck());
						cd.setStcCheckPublicAmount(cd.getCilCheckPublicAmount());
						cd.setStcCheckPrivateAmount(cd.getCilCheckPrivateAmount());
						cd.setStcCheckAdditionalFinansingAmount(cd.getCilCheckAdditionalFinansingAmount());
						cd.setStcCheckStateAidAmount(cd.getCilCheckStateAidAmount());
						cd.setStcCheckOutsideEligibleAreas(cd.getCilCheckOutsideEligibleAreas());
						cd.setStcCheckInkindContributions(cd.getCilCheckInkindContributions());
					}
				}

				if (this.isEditMode() && this.isAGUCertAvailable()) {
					cd.setAguCertDate(DateTime.getNow());
				}

				if (cd.getAguCertification() == null && this.isAGUCertAvailable() && cd.getAguCertification() == null) {
					cd.setAguCertDate(DateTime.getNow());

					cd.setAguCertification(cd.getStcCertification());
					cd.setAguCertification(cd.getStcCertification());
					cd.setAguCheckPublicAmount(cd.getStcCheckPublicAmount());
					cd.setAguCheckPrivateAmount(cd.getStcCheckPrivateAmount());
					cd.setAguCheckAdditionalFinansingAmount(cd.getStcCheckAdditionalFinansingAmount());
					cd.setAguCheckStateAidAmount(cd.getStcCheckStateAidAmount());
					cd.setAguCheckOutsideEligibleAreas(cd.getStcCheckOutsideEligibleAreas());
					cd.setAguCheckInkindContributions(cd.getStcCheckInkindContributions());

				}

				if (this.isEditMode() && this.isACUCertAvailable()) {
					cd.setAcuCertDate(DateTime.getNow());
				}

				if (cd.getAcuCertification() == null && this.isACUCertAvailable() && cd.getAcuCertification() == null) {
					cd.setAcuCertDate(DateTime.getNow());

					cd.setAcuCertification(cd.getAguCertification());
					cd.setAcuCheckPublicAmount(cd.getAguCheckPublicAmount());
					cd.setAcuCheckPrivateAmount(cd.getAguCheckPrivateAmount());
					cd.setAcuCheckAdditionalFinansingAmount(cd.getAguCheckAdditionalFinansingAmount());
					cd.setAcuCheckStateAidAmount(cd.getAguCheckStateAidAmount());
					cd.setAcuCheckOutsideEligibleAreas(cd.getAguCheckOutsideEligibleAreas());
					cd.setAcuCheckInkindContributions(cd.getAguCheckInkindContributions());
				}

				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs().GetByUser(cd.getUser().getId());

				if (partner != null) {
					cd.setUserInsertedName(partner.getName());
					cd.setPartnerNaming(partner.getNaming());
				} else {
					cd.setUserInsertedName(cd.getUser().getName());
					cd.setPartnerNaming(cd.getUser().getName());
				}

				List<CostDefinitions> listCD = new ArrayList<>();
				if (this.getViewState().get("durCompilationEditListCD") != null) {
					for (Long id : (List<Long>) this.getViewState().get("durCompilationEditListCD")) {
						CostDefinitions c = new CostDefinitions();
						c.setId(id);
						listCD.add(c);
					}
					getDurCompilation().setCostDefinitions(listCD);
				}
				BeansFactory.CostDefinitions().Save(cd, getDurCompilation());
			}
			for (CostDefinitions costDefinitions : listCostTable) {
				if (this.getCostAmountCIL() != null && !this.getCostAmountCIL().isEmpty()
						&& costDefinitions.getCilCheck() != Double.parseDouble(this.getCostAmountCIL())) {
					continue;
				}

				if (getCostAmountPayment() != null && !getCostAmountPayment().isEmpty()
						&& costDefinitions.getPaymentAmount() != Double.parseDouble(this.getCostAmountPayment())) {
					continue;
				}

				if (getCostAmountReport() != null && !getCostAmountReport().isEmpty()
						&& costDefinitions.getAmountReport() != Double.parseDouble(this.getCostAmountReport())) {
					continue;
				}

				if (getCostNumber() != null && !getCostNumber().isEmpty()
						&& !costDefinitions.getDocumentNumber().equals(this.getCostNumber())) {
					continue;
				}

				if (getCostSuspensionType() != null
						&& !getCostSuspensionType().equals(SuspendedCostType.ALL.id.toString())) {
					if (getCostSuspensionType().equals(SuspendedCostType.TRUE.id.toString())
							&& costDefinitions.getSuspensionAmount() == null) {
						continue;
					} else if (getCostSuspensionType().equals(SuspendedCostType.FALSE.id.toString())
							&& costDefinitions.getSuspensionAmount() != null) {
						continue;
					}
				}

				if (this.getFilterPartner() != null && !this.getFilterPartner().equals(0l)) {
					if (this.getFilterPartner().equals("-1")) {
						boolean skip = true;
						if (this.getProject().getAsse() == 5) {
							List<Long> ids = BeansFactory.ControllerUserAnagraph()
									.GetUsersIndicesByType(UserRoleTypes.AGU);
							if (ids != null && ids.contains(costDefinitions.getUser().getId())) {
								skip = false;
							}

						}

						if (skip) {
							continue;
						}
					} else if (!costDefinitions.getUser().getId().equals(this.getFilterPartner())) {
						continue;
					}
				}

				if (Boolean.TRUE.equals(this.getFilterAdditionalFinansing())) {
					if (!Boolean.TRUE.equals(costDefinitions.getAdditionalFinansing())) {
						continue;
					}
				}

				if (Boolean.TRUE.equals(this.getAsse3Mode())) {
					if (this.getSelectedCostPhaseAsse3() != null && !this.getSelectedCostPhaseAsse3().isEmpty()) {
						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getPhase().name().equals(this.getSelectedCostPhaseAsse3())) {
							continue;
						}

					}

					if (this.getSelectedCostItemAsse3() != null && !this.getSelectedCostItemAsse3().isEmpty()) {
						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getValue().equals(this.getSelectedCostItemAsse3())) {
							continue;
						}

					}
				} else {
					if (this.getFilterCostItem() != null && !this.getFilterCostItem().isEmpty()) {
						if (!costDefinitions.getCostItem().getId().equals(Long.parseLong(this.getFilterCostItem()))) {
							continue;
						}
					}

					if (this.getFilterPhase() != null && !this.getFilterPhase().equals(0l)) {
						if (costDefinitions.getCostDefinitionPhase() == null
								|| !costDefinitions.getCostDefinitionPhase().getId().equals(this.getFilterPhase())) {
							continue;
						}
					}
				}

				if (this.getFilterCostDefID() != null && !this.getFilterCostDefID().isEmpty()) {
					if (!costDefinitions.getProgressiveId().equals(Long.parseLong(this.getFilterCostDefID()))) {
						continue;
					}
				}

				// if ((this.getSessionBean().isACU() ||
				// this.getSessionBean().isACUW())
				// &&
				// Boolean.TRUE.equals(costDefinitions.getAdditionalFinansing()))
				// {
				// continue;
				// }

				//
				costDefinitions.setFiltred(true);
				if (this.getIsFiltredEditFromView()) {
					if (this.getSessionFilterPartner() != null && !this.getSessionFilterPartner().equals(0l)) {
						if (this.getSessionFilterPartner().equals("-1")) {
							boolean skip = true;
							if (this.getProject().getAsse() == 5) {
								List<Long> ids = BeansFactory.ControllerUserAnagraph()
										.GetUsersIndicesByType(UserRoleTypes.AGU);
								if (ids != null && ids.contains(costDefinitions.getUser().getId())) {
									skip = false;
								}

							}

							if (skip) {
								costDefinitions.setFiltred(false);
							}
						} else if (!costDefinitions.getUser().getId().equals(this.getSessionFilterPartner())) {
							costDefinitions.setFiltred(false);
						}
					}

					if (this.getSessionFilterPhase() != null && !this.getSessionFilterPhase().equals(0l)) {
						if (costDefinitions.getCostDefinitionPhase() == null || !costDefinitions
								.getCostDefinitionPhase().getId().equals(this.getSessionFilterPhase())) {
							costDefinitions.setFiltred(false);
						}
					}

					if (this.getSessionFilterCostItem() != null && !this.getSessionFilterCostItem().isEmpty()) {
						if (!costDefinitions.getCostItem().getId()
								.equals(Long.parseLong(this.getSessionFilterCostItem()))) {
							costDefinitions.setFiltred(false);
						}
					}

					if (this.getSessionFilterCostDefID() != null && !this.getSessionFilterCostDefID().isEmpty()) {
						if (!costDefinitions.getProgressiveId()
								.equals(Long.parseLong(this.getSessionFilterCostDefID()))) {
							costDefinitions.setFiltred(false);
						}
					}
				}

				//

				costDefinitions.setAssignedDocumentsCount((Integer) BeansFactory.CostDefinitionsToDocuments()
						.getCountByCostDefinition(costDefinitions.getId(), "id"));

				this.getListFilteredCostTable().add(costDefinitions);
			}
		} else if (costWrapers == null) {
			this.costWrapers = new HashMap<Long, CostDefinitionsWrapper>();
			for (CostDefinitions cd : this.listCostDefinitionsWeb) {
				if (cd.getSelected()) {
					costWrapers.put(cd.getId(), new CostDefinitionsWrapper(cd));
				}
			}
		}

		this.listCostTable = new ArrayList<CostDefinitions>();
		listCostTable.addAll(this.getListFilteredCostTable());

		int pageCount = 0;
		if (getListFilteredCostTable() != null && getListFilteredCostTable().size() > 0) {
			pageCount = getListFilteredCostTable().size() / getCustomPaginator().getItemsPerPageSelected();

			if (getListFilteredCostTable().size() % getCustomPaginator().getItemsPerPageSelected() > 0) {
				pageCount += 1;
			}

			this.setPageCosts(getListFilteredCostTable().subList(getCustomFirstRow(),
					Math.min(getCustomFirstRow() + getCustomPaginator().getItemsPerPageSelected(),
							getListFilteredCostTable().size())));

		}

		this.getCustomPaginator().setPageCount(pageCount);

		this.getCustomPaginator().setPages(new ArrayList<Integer>());

		for (int i = 0; i < pageCount; ++i) {
			getCustomPaginator().getPages().add(i + 1);
		}

	}

	private void CaluculateAdditionalFinansing() throws PersistenceBeanException {
		double summAdditionalFinansing = 0d;
		for (CostDefinitions cost : getListCostDefinitionsBackup()) {

			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {

				if (this.getDurCompilation() == null || this.getDurCompilation().getDurState() == null
						|| this.getDurCompilation().getDurState().getId().equals(DurStateTypes.Create.getValue())) {
					if (cost.getCreatedByPartner()) {
						summAdditionalFinansing += cost.getCfCheck() != null ? cost.getCfCheck() : 0d;
					} else {
						summAdditionalFinansing += cost.getCilCheck() != null ? cost.getCilCheck() : 0d;
					}

				} else if (this.getDurCompilation().getDurState().getId()
						.equals(DurStateTypes.STCEvaluation.getValue())) {

					summAdditionalFinansing += cost.getStcCertification() != null ? cost.getStcCertification() : 0d;

				} else if (this.getDurCompilation().getDurState().getId() >= DurStateTypes.AGUEvaluation.getValue()) {

					summAdditionalFinansing += cost.getAguCertification() != null ? cost.getAguCertification() : 0d;
				}

			}

		}

		this.setTotalAdditionalFinansing(summAdditionalFinansing);
	}

	private boolean checkEqualsAmounts() {
		if (this.getFesrReimbursementAmount() != null && this.getFrCnReimbursement() != null
				&& this.getItCnReimbursement() != null && this.getTotalAmountDur() != null) {
			if ((this.getFrCnReimbursement() + this.getItCnReimbursement() + this.getFesrReimbursementAmount()) == this
					.getTotalAmountDur()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private void RecalculateAmounts() throws HibernateException, PersistenceBeanException {
		RecalculateAmounts(null);
		calculateTotalAmountFields();
	}

	private void calculateTotalAmountFields() throws PersistenceBeanException {
		this.setTotalAmountDur2(this.calculateTotalAmountDur2());
		this.setFesrReimbursementAmount2(this.calculateFesrAmount2());
		this.setItCnReimbursement2(this.calculateItCnReimbursementAmount());
		this.setFrCnReimbursement2(this.calculateFrAmount());
		this.setTotalAmountEligibleExpenditure(this.calculateTotalAmountEligibleExpenditure());
		this.setTotalAmountPublicExpenditure(this.calculateAmountPublicExpenditure());
		this.setTotalAmountPrivateExpenditure(this.calculateTotalAmountPrivateExpenditure());
		this.setTotalAmountStateAid(this.calculateTotalAmountStateAid());
		this.setTotalAmountStateAidAdvanceCover(this.calculateStateAidAdvanceCover());
		this.setTotalContributionInKindToOperatoinPublicShare(
				this.calculateTotalContributionInKindToOperatoinPublicShare());
		this.setTotalAdditionalFinansing2(this.calculateAdditionalFinansing2());
	}

	private Double calculateAdditionalFinansing2() {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
						amount += cost.getAcuCheckAdditionalFinansingAmount();
					} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
						amount += cost.getAguCheckAdditionalFinansingAmount();
					} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
						amount += cost.getStcCheckAdditionalFinansingAmount();
					} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
						amount += cost.getCfCheckAdditionalFinansingAmount();
					} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
						amount += cost.getCilCheckAdditionalFinansingAmount();
					}
				}
			}
		}
		return amount;
	}

	private Double calculateTotalContributionInKindToOperatoinPublicShare() {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					if (cost.getACUSertified() && cost.getAcuCheckInkindContributions() != null) {
						amount += cost.getAcuCheckInkindContributions();
					} else if (cost.getAGUSertified() && cost.getAguCheckInkindContributions() != null) {
						amount += cost.getAguCheckInkindContributions();
					} else if (cost.getSTCSertified() && cost.getStcCheckInkindContributions() != null) {
						amount += cost.getStcCheckInkindContributions();
					} else if (cost.getCfCheckInkindContributions() != null) {
						amount += cost.getCfCheckInkindContributions();
					} else if (cost.getCilCheckInkindContributions() != null) {
						amount += cost.getCilCheckInkindContributions();
					}
				}
			}
		}
		return amount;
	}

	private Double calculateStateAidAdvanceCover() {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected() && cost.getStateAid()) {
					if (cost.getACUSertified() && cost.getAcuCheckStateAidAmount() != null) {
						amount += cost.getAcuCheckStateAidAmount();
					} else if (cost.getAGUSertified() && cost.getAguCheckStateAidAmount() != null) {
						amount += cost.getAguCheckStateAidAmount();
					} else if (cost.getSTCSertified() && cost.getStcCheckStateAidAmount() != null) {
						amount += cost.getStcCheckStateAidAmount();
					} else if (cost.getCfCheckStateAidAmount() != null) {
						amount += cost.getCfCheckStateAidAmount();
					} else if (cost.getCilCheckStateAidAmount() != null) {
						amount += cost.getCilCheckStateAidAmount();
					}
				}
			}
		}
		return amount;
	}

	private Double calculateTotalAmountStateAid() {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected() && cost.getStateAid()) {
					amount += this.getValidateAmount(cost);
				}
			}
		}
		return amount;
	}

	private Double calculateTotalAmountPrivateExpenditure() throws PersistenceBeanException {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (gb != null
								&& (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
										* 100) == 50)) {
							amount += (this.getValidateAmount(cost) * 0.5);// (this.getCostSubAdditionalFinansing(cost)
																			// *
																			// 0.5);
						} else {
							amount += (this.getValidateAmount(cost) * 0.15);// (this.getCostSubAdditionalFinansing(cost)
																			// *
																			// 0.15);
						}

					}
				}
			}
		}
		return amount;
	}

	private Double calculateAmountPublicExpenditure() throws PersistenceBeanException {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (gb != null
								&& (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
										* 100) == 50)) {
							amount += (this.getCostSubAdditionalFinansing(cost) * 0.5);
						} else {
							amount += (this.getCostSubAdditionalFinansing(cost) * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amount += this.getValidateAmount(cost);// this.getCostSubAdditionalFinansing(cost);
					}
				}
			}
		}
		return amount;
	}

	private Double calculateTotalAmountEligibleExpenditure() throws PersistenceBeanException {
		double amount = 0;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					if (ca.getPublicSubject() != null) {
						amount += this.getValidateAmount(cost);
					}
				}
			}
		}
		return amount;
	}

	/**
	 * @param durSummaries2
	 */
	private void RecalculateAmounts(DurSummaries durSummaries) throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub

		// remove to update
		// if (this.getFesrReimbursementAmount() != null
		// && this.getFrCnReimbursement() != null
		// && this.getItCnReimbursement() != null)
		// {
		// this.setTotalAmountDur(this.getFrCnReimbursement()
		// + this.getItCnReimbursement()
		// + this.getFesrReimbursementAmount());
		// this.setTotalReimbursement(this.getFesrReimbursementAmount()
		// + this.getItCnReimbursement());
		// }

		double amountTotal = 0d;
		double amountFesr = 0d;
		double amountItCn = 0d;
		double amountFrCn = 0d;
		double fesrReimbursementAmountTemp = 0d;

		for (CostDefinitions cost : this.getListCostDefinitionsWeb()) {
			if (cost.getSelected()) {
				if (cost.getCreatedByPartner()) {
					List<CFPartnerAnagraphProject> partnerProjectList = BeansFactory.CFPartnerAnagraphProject()
							.GetCFAnagraphForProjectAndUser(this.getProjectId(), cost.getUser().getId());
					if (partnerProjectList != null && !partnerProjectList.isEmpty()) {
						if (partnerProjectList.get(0).getCfPartnerAnagraphs().getCountry().getCode()
								.equals(CountryTypes.Italy.getCountry())) {
							amountItCn += (cost.getCfCheck() == null ? 0d : cost.getCfCheck()) * 0.15;

						} else {
							// amountFrCn += (cost.getCfCheck() == null ? (cost
							// .getCilCheck() == null ? 0d : cost
							// .getCilCheck()) : cost.getCfCheck()) * 0.25;

							if (cost.getACUSertified() && cost.getAcuCertification() != null) {
								amountFrCn += cost.getAcuCertification() * 0.15;
							} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
								amountFrCn += cost.getAguCertification() * 0.15;
							} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
								amountFrCn += cost.getStcCertification() * 0.15;
							} else if (cost.getCfCheck() != null) {
								amountFrCn += cost.getCfCheck() * 0.15;
							} else if (cost.getCilCheck() != null) {
								amountFrCn += cost.getCilCheck() * 0.15;
							}
						}
					}
					fesrReimbursementAmountTemp += (cost.getCfCheck() == null ? 0d : cost.getCfCheck() * 0.85);
				} else {
					if (cost.getCreatedByAGU()) {
						// amountItCn += (cost.getCilCheck() == null ? 0d : cost
						// .getCilCheck()) * 0.25;
					} else {
						List<CFPartnerAnagraphProject> partnerProjectList = BeansFactory.CFPartnerAnagraphProject()
								.GetCFAnagraphForProjectAndUser(this.getProjectId(), cost.getUser().getId());
						if (partnerProjectList != null && !partnerProjectList.isEmpty()) {
							if (partnerProjectList.get(0).getCfPartnerAnagraphs().getCountry().getCode()
									.equals(CountryTypes.Italy.getCountry())) {
								amountItCn += (cost.getCilCheck() == null ? 0d : cost.getCilCheck()) * 0.15;
							} else {
								if (cost.getACUSertified() && cost.getAcuCertification() != null) {
									amountFrCn += cost.getAcuCertification() * 0.15;
								} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
									amountFrCn += cost.getAguCertification() * 0.15;
								} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
									amountFrCn += cost.getStcCertification() * 0.15;
								} else if (cost.getCfCheck() != null) {
									amountFrCn += cost.getCfCheck() * 0.15;
								} else if (cost.getCilCheck() != null) {
									amountFrCn += cost.getCilCheck() * 0.15;
								}
							}
						}

					}

					fesrReimbursementAmountTemp += (cost.getCilCheck() == null ? 0d : cost.getCilCheck()) * 0.25;
				}

				if (!Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
					if (cost.getACUSertified() && cost.getAcuCertification() != null) {
						amountTotal += cost.getAcuCertification();
					} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
						amountTotal += cost.getAguCertification();
					} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
						amountTotal += cost.getStcCertification();
					} else if (cost.getCfCheck() != null) {
						amountTotal += cost.getCfCheck();
					} else if (cost.getCilCheck() != null) {
						amountTotal += cost.getCilCheck();
					}
				}
			}
		}

		amountFesr = fesrReimbursementAmountTemp;// NumberHelper.Round(amountTotal
													// * 0.75);
		amountFrCn = NumberHelper.Round(amountFrCn);
		// amountItCn = NumberHelper.Round(amountTotal - amountFesr -
		// amountFrCn);

		if (durSummaries != null) {
			if (this.isCfEdit() && this.getFesrReimbursementAmount() != null
					&& !this.getFesrReimbursementAmount().equals(amountFesr)) {
				durSummaries.setFesrAmount(this.getFesrReimbursementAmount());
				durSummaries.setUserFesrAmount(true);
			} else {
				durSummaries.setFesrAmount(amountFesr);
				durSummaries.setUserFesrAmount(false);
			}

			if (this.isCfEdit() && this.getFrCnReimbursement() != null
					&& !this.getFrCnReimbursement().equals(amountFrCn)) {
				durSummaries.setFrCnReimbursementAmount(this.getFrCnReimbursement());
				durSummaries.setUserFrCnReimbursementAmount(true);
			} else {
				durSummaries.setFrCnReimbursementAmount(amountFrCn);
				durSummaries.setUserFrCnReimbursementAmount(false);
			}

			if (this.isCfEdit() && this.getItCnReimbursement() != null
					&& !this.getItCnReimbursement().equals(amountItCn)) {
				durSummaries.setItCnReimbursementAmount(this.getItCnReimbursement());
				durSummaries.setUserItCnReimbursementAmount(true);
			} else {
				durSummaries.setItCnReimbursementAmount(amountItCn);
				durSummaries.setUserItCnReimbursementAmount(false);
			}
		} else {
			boolean userFesr = false;
			boolean userFrCn = false;
			boolean userItCn = false;

			if (!getCostListChanged()) {
				if ((this.getFesrReimbursementAmount() != null || this.isCfEdit())
						&& (this.getUserFesrAmount() != null && this.getUserFesrAmount() || this.isCfEdit())) {
					userFesr = true;
				}

				if ((this.getFrCnReimbursement() != null || this.isCfEdit())
						&& (this.getUserFrCnReimbursementAmount() != null && this.getUserFrCnReimbursementAmount()
								|| this.isCfEdit())) {
					userFrCn = true;
				}

				if ((this.getItCnReimbursement() != null || this.isCfEdit())
						&& (this.getUserItCnReimbursementAmount() != null && this.getUserItCnReimbursementAmount()
								|| this.isCfEdit())) {
					userItCn = true;
				}
			}

			// !!!here it is
			if (!userFesr) {
				this.setFesrReimbursementAmount(NumberHelper.Round(fesrReimbursementAmountTemp));
			}
			// if (!userFrCn)
			// {
			this.setFrCnReimbursement(NumberHelper.Round(amountFrCn));
			// }
			if (!userItCn) {
				this.setItCnReimbursement(NumberHelper.Round(amountItCn));
			}

			this.setTotalAmountDur(amountTotal);

			/*
			 * commented for bug 0027087
			 * 
			 * this.setTotalReimbursement((this.getFesrReimbursementAmount() ==
			 * null ? 0d : this.getFesrReimbursementAmount()) +
			 * (this.getItCnReimbursement() == null ? 0d : this
			 * .getItCnReimbursement()));
			 */
		}
	}

	private Double calculateFrAmount() throws PersistenceBeanException {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					// gb =
					// BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
					// ca.getId()).get(0);
					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);
					if (ca.getCountry().getCode().equals(CountryTypes.France.getCountry())) {
						if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
							if (gb != null && (((gb.getFesr()
									/ (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
									* 100) == 50)) {
								amount += (this.getCostSubAdditionalFinansing(cost) * 0.5);
							} else {
								amount += (this.getCostSubAdditionalFinansing(cost) * 0.15);
							}

						} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
							amount += (this.getCostSubAdditionalFinansing(cost) * 0.15);
						}
					}
				}
			}
		}
		return amount;
	}

	private Double calculateItCnReimbursementAmount() throws PersistenceBeanException {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		CFPartnerAnagraphs ca = null;
		double amount = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						if (ca.getCountry().getCode().equals(CountryTypes.Italy.getCountry())) {
							amount += (this.getCostSubAdditionalFinansing(cost) * 0.15);
						}
					}
				}
			}
		}
		return amount;
	}

	private Double calculateFesrAmountOldDur() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					if (!cost.getSelected()) {
						amount = this.getCostSubAdditionalFinansing(cost);
						ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
						// gb =
						// BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
						// ca.getId())
						// .get(0);
						List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets()
								.GetItemsForCFAnagraph(this.getProjectId(), ca.getId());
						gb = gbs.get(gbs.size() - 1);

						if (gb != null) {
						}
						if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
							if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
									* 100) == 50) {
								amountTotal += (amount * 0.5);
							} else {
								amountTotal += (amount * 0.85);
							}

						} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
							amountTotal += (amount * 0.85);
						}
					}
				}
			}
		}
		return amountTotal;
	}

	private Double calculateFesrAmount2() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = this.getCostSubAdditionalFinansing(cost);
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					// gb =
					// BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
					// ca.getId()).get(0);
					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());

					gb = gbs.get(gbs.size() - 1);

					if (gb != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double getCostSubAdditionalFinansing(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			amount += cost.getAcuCertification();
		} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			amount += cost.getAguCertification();
		} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			amount += cost.getStcCertification();
		} else if (cost.getCfCheck() != null) {
			amount += cost.getCfCheck();
		} else if (cost.getCilCheck() != null) {
			amount += cost.getCilCheck();
		}
		if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
			if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAcuCheckAdditionalFinansingAmount();
			} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAguCheckAdditionalFinansingAmount();
			} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getStcCheckAdditionalFinansingAmount();
			} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCfCheckAdditionalFinansingAmount();
			} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCilCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidateAmount(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			amount += cost.getAcuCertification();
		} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			amount += cost.getAguCertification();
		} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			amount += cost.getStcCertification();
		} else if (cost.getCfCheck() != null) {
			amount += cost.getCfCheck();
		} else if (cost.getCilCheck() != null) {
			amount += cost.getCilCheck();
		}
		return amount;
	}

	private Double calculateTotalAmountDur2() throws PersistenceBeanException {
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		double amountTotal = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {

					if (cost.getACUSertified() && cost.getAcuCertification() != null) {
						amountTotal += cost.getAcuCertification();
					} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
						amountTotal += cost.getAguCertification();
					} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
						amountTotal += cost.getStcCertification();
					} else if (cost.getCfCheck() != null) {
						amountTotal += cost.getCfCheck();
					} else if (cost.getCilCheck() != null) {
						amountTotal += cost.getCilCheck();
					}
					if (Boolean.TRUE.equals(cost.getAdditionalFinansing())) {
						if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
							amountTotal -= cost.getAcuCheckAdditionalFinansingAmount();
						} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
							amountTotal -= cost.getAguCheckAdditionalFinansingAmount();
						} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
							amountTotal -= cost.getStcCheckAdditionalFinansingAmount();
						} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
							amountTotal -= cost.getCfCheckAdditionalFinansingAmount();
						} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
							amountTotal -= cost.getCilCheckAdditionalFinansingAmount();
						}
					}
				}
			}
		}
		return amountTotal;
	}

	private void recalculateAmountDur() throws NumberFormatException, PersistenceBeanException {
		CostItemSummaryWrapper entityTotal = this.getTotalWrapper();
		CostItemSummaryWrapper objCnItTotal = this.getCnItTotalWrapper();
		CostItemSummaryWrapper objCnFrTotal = this.getCnFrTotalWrapper();

		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			objCnItTotal = this.prepareTotalCnForCountry(CountryTypes.Italy);
			objCnFrTotal = this.prepareTotalCnForCountry(CountryTypes.France);

			List<GeneralBudgets> listGB = BeansFactory.GeneralBudgets()
					.GetItemsForProject(Long.valueOf(this.getCurrentProjectId()));

			for (GeneralBudgets gb : listGB) {
				if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.Italy.getCountry())) {
					if (gb.getCnPublic() != null) {
						objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getCnPublic());
					}
					if (gb.getQuotaPrivate() != null) {
						objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getQuotaPrivate());
					}

				} else if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.France.getCountry())) {
					if (gb.getCnPublic() != null) {
						objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getCnPublic());
					}
					if (gb.getQuotaPrivate() != null) {
						objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getQuotaPrivate());
					}
				}
			}
		}

		if (entityTotal != null) {
			if (entityTotal.getAcuCertificated() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getAcuCertificated() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getAcuCertificated() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(
						NumberHelper.Round(objCnItTotal.getAcuCertificated() - (getTotalAdditionalFinansing() * 0.5)));
				this.setFrCnReimbursement(
						NumberHelper.Round(objCnFrTotal.getAcuCertificated() - (getTotalAdditionalFinansing() * 0.5)));
			} else if (entityTotal.getAguCertificated() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getAguCertificated() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getAguCertificated() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAguCertificated()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAguCertificated()));
			} else if (entityTotal.getStcCertificated() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getStcCertificated() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getStcCertificated() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getStcCertificated()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getStcCertificated()));
			} else if (entityTotal.getAmountRequest() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getAmountRequest() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getAmountRequest() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountRequest()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountRequest()));
			} else if (entityTotal.getAmountCostCertificate() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getAmountCostCertificate() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(NumberHelper
						.Round((entityTotal.getAmountCostCertificate() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountCostCertificate()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountCostCertificate()));
			} else if (entityTotal.getAmountByCf() != 0d) {
				this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAmountByCf() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getAmountByCf() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountByCf()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountByCf()));
			} else if (entityTotal.getAmountFromBudget() != 0d) {
				this.setTotalAmountDur(
						NumberHelper.Round(entityTotal.getAmountFromBudget() - getTotalAdditionalFinansing()));
				this.setFesrReimbursementAmount(
						NumberHelper.Round((entityTotal.getAmountFromBudget() - getTotalAdditionalFinansing()) * 0.85));
				this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountFromBudget()));
				this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountFromBudget()));
			}
		}
		recalculateTotalReimbursement();
	}

	private boolean isCfEdit() {
		return this.isEditMode() && this.isCFMode();
	}

	private void filterCdList() {
		List<CostDefinitions> list = new ArrayList<CostDefinitions>();

		if (this.getListCostDefinitionsWeb() != null) {
			// if (this.getListFilteredCostTable() != null
			// && !this.getListFilteredCostTable().isEmpty())
			// {
			// List<CostDefinitions> list1 = this.getListCostDefinitionsWeb();
			// for (CostDefinitions cdFiltered : this
			// .getListFilteredCostTable())
			// {
			// for (CostDefinitions cd: list1)
			// {
			// if (cd.getId().equals(cdFiltered.getId()))
			// {
			// cdFiltered.updateCd(cd);
			// break;
			// }
			// }
			// }
			//
			// this.setListCostDefinitionsWeb(list1);
			// }

			for (CostDefinitions costDefinitions : this.getListCostDefinitionsWeb()) {
				if (this.getFilterPartner() != null && !this.getFilterPartner().equals(0l)) {
					if (!costDefinitions.getUser().getId().equals(this.getFilterPartner())) {
						continue;
					}
				}

				if (Boolean.TRUE.equals(this.getAsse3Mode())) {
					if (this.getSelectedCostPhaseAsse3() != null && !this.getSelectedCostPhaseAsse3().isEmpty()) {

						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getPhase().name().equals(this.getSelectedCostPhaseAsse3())) {
							continue;
						}

					}

					if (this.getSelectedCostItemAsse3() != null && !this.getSelectedCostItemAsse3().isEmpty()) {

						if (costDefinitions.getCostItemPhaseAsse3().getPhase() == null || !costDefinitions
								.getCostItemPhaseAsse3().getValue().equals(this.getSelectedCostItemAsse3())) {
							continue;
						}

					}
				} else {

					if (this.getFilterPhase() != null && !this.getFilterPhase().equals(0l)) {
						if (costDefinitions.getCostDefinitionPhase() == null
								|| !costDefinitions.getCostDefinitionPhase().getId().equals(this.getFilterPhase())) {
							continue;
						}
					}

					if (this.getFilterCostItem() != null && !this.getFilterCostItem().isEmpty()) {
						if (!costDefinitions.getCostItem().getId().equals(Long.parseLong(this.getFilterCostItem()))) {
							continue;
						}
					}
				}
				if (this.getFilterCostDefID() != null && !this.getFilterCostDefID().isEmpty()) {
					if (!costDefinitions.getProgressiveId().equals(Long.parseLong(this.getFilterCostDefID()))) {
						continue;
					}
				}
				CFPartnerAnagraphs partner;
				try {
					partner = BeansFactory.CFPartnerAnagraphs().GetByUser(costDefinitions.getUser().getId());
					costDefinitions.setPartnerNaming(partner != null ? (partner.getName())
							: (costDefinitions.getUser().getName() + " - "
									+ costDefinitions.getUser().getRolesNames()));

				} catch (PersistenceBeanException e) {
					e.printStackTrace();
				}

				list.add(costDefinitions);
			}
		}

		this.setListCostDefinitionsWeb(list);
	}

	private void FillGridSummaryWp() throws HibernateException, NumberFormatException, PersistenceBeanException {
		Long userId = null;

		List<PartnersBudgets> listBudgets = new ArrayList<PartnersBudgets>();
		for (CFPartnerAnagraphs p : partnersForProject) {
			if (this.getSelectedPartnerForGridWp() == null || this.getSelectedPartnerForGridWp() == 0l) {
				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				continue;
			}

			else if (p.getId().equals(this.getSelectedPartnerForGridWp())) {

				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				userId = p.getUser().getId();
				break;

			}

		}

		PartnersBudgets totalWp = new PartnersBudgets();
		totalWp.setPhaseBudgets(new LinkedList<Phase>());

		for (CostDefinitionPhases cdPhase : BeansFactory.CostDefinitionPhase().Load()) {
			Phase newPhase = new Phase();
			newPhase.setPhase(cdPhase);
			newPhase.setPartnersBudgets(totalWp);
			newPhase.setPartAdvInfoProducts(0d);
			newPhase.setPartAdvInfoPublicEvents(0d);
			newPhase.setPartDurableGoods(0d);
			newPhase.setPartDurables(0d);
			newPhase.setPartHumanRes(0d);
			newPhase.setPartMissions(0d);
			newPhase.setPartOther(0d);
			newPhase.setPartGeneralCosts(0d);
			newPhase.setPartOverheads(0d);
			newPhase.setPartProvisionOfService(0d);

			newPhase.setPartContainer(0d);
			newPhase.setPartPersonalExpenses(0d);
			newPhase.setPartCommunicationAndInformation(0d);
			newPhase.setPartOrganizationOfCommitees(0d);
			newPhase.setPartOtherFees(0d);
			newPhase.setPartAutoCoordsTunis(0d);
			newPhase.setPartContactPoint(0d);
			newPhase.setPartSystemControlAndManagement(0d);
			newPhase.setPartAuditOfOperations(0d);
			newPhase.setPartAuthoritiesCertification(0d);
			newPhase.setPartIntermEvaluation(0d);

			totalWp.getPhaseBudgets().add(newPhase);
		}

		for (PartnersBudgets pb : listBudgets) {
			for (int i = 0; i < pb.getPhaseBudgets().size(); i++) {
				totalWp.getPhaseBudgets().get(i)
						.setPartAdvInfoProducts(pb.getPhaseBudgets().get(i).getPartAdvInfoProducts()
								+ totalWp.getPhaseBudgets().get(i).getPartAdvInfoProducts());
				totalWp.getPhaseBudgets().get(i)
						.setPartAdvInfoPublicEvents(pb.getPhaseBudgets().get(i).getPartAdvInfoPublicEvents()
								+ totalWp.getPhaseBudgets().get(i).getPartAdvInfoPublicEvents());

				totalWp.getPhaseBudgets().get(i).setPartDurableGoods(pb.getPhaseBudgets().get(i).getPartDurableGoods()
						+ totalWp.getPhaseBudgets().get(i).getPartDurableGoods());

				totalWp.getPhaseBudgets().get(i).setPartDurables(pb.getPhaseBudgets().get(i).getPartDurables()
						+ totalWp.getPhaseBudgets().get(i).getPartDurables());

				totalWp.getPhaseBudgets().get(i).setPartHumanRes(pb.getPhaseBudgets().get(i).getPartHumanRes()
						+ totalWp.getPhaseBudgets().get(i).getPartHumanRes());
				totalWp.getPhaseBudgets().get(i).setPartMissions(pb.getPhaseBudgets().get(i).getPartMissions()
						+ totalWp.getPhaseBudgets().get(i).getPartMissions());
				totalWp.getPhaseBudgets().get(i).setPartGeneralCosts(pb.getPhaseBudgets().get(i).getPartGeneralCosts()
						+ totalWp.getPhaseBudgets().get(i).getPartGeneralCosts());

				totalWp.getPhaseBudgets().get(i).setPartOther(
						pb.getPhaseBudgets().get(i).getPartOther() + totalWp.getPhaseBudgets().get(i).getPartOther());

				totalWp.getPhaseBudgets().get(i).setPartOverheads(pb.getPhaseBudgets().get(i).getPartOverheads()
						+ totalWp.getPhaseBudgets().get(i).getPartOverheads());

				totalWp.getPhaseBudgets().get(i)
						.setPartProvisionOfService(pb.getPhaseBudgets().get(i).getPartProvisionOfService()
								+ totalWp.getPhaseBudgets().get(i).getPartProvisionOfService());

				totalWp.getPhaseBudgets().get(i).setPartContainer(totalWp.getPhaseBudgets().get(i).getPartContainer()
						+ pb.getPhaseBudgets().get(i).getPartContainer());

				totalWp.getPhaseBudgets().get(i)
						.setPartPersonalExpenses(totalWp.getPhaseBudgets().get(i).getPartPersonalExpenses()
								+ pb.getPhaseBudgets().get(i).getPartPersonalExpenses());

				totalWp.getPhaseBudgets().get(i).setPartCommunicationAndInformation(
						totalWp.getPhaseBudgets().get(i).getPartCommunicationAndInformation()
								+ pb.getPhaseBudgets().get(i).getPartCommunicationAndInformation());

				totalWp.getPhaseBudgets().get(i).setPartOrganizationOfCommitees(
						totalWp.getPhaseBudgets().get(i).getPartOrganizationOfCommitees()
								+ pb.getPhaseBudgets().get(i).getPartOrganizationOfCommitees());

				totalWp.getPhaseBudgets().get(i).setPartOtherFees(totalWp.getPhaseBudgets().get(i).getPartOtherFees()
						+ pb.getPhaseBudgets().get(i).getPartOtherFees());

				totalWp.getPhaseBudgets().get(i)
						.setPartAutoCoordsTunis(totalWp.getPhaseBudgets().get(i).getPartAutoCoordsTunis()
								+ pb.getPhaseBudgets().get(i).getPartAutoCoordsTunis());

				totalWp.getPhaseBudgets().get(i)
						.setPartContactPoint(totalWp.getPhaseBudgets().get(i).getPartContactPoint()
								+ pb.getPhaseBudgets().get(i).getPartContactPoint());

				totalWp.getPhaseBudgets().get(i).setPartSystemControlAndManagement(
						totalWp.getPhaseBudgets().get(i).getPartSystemControlAndManagement()
								+ pb.getPhaseBudgets().get(i).getPartSystemControlAndManagement());

				totalWp.getPhaseBudgets().get(i)
						.setPartAuditOfOperations(totalWp.getPhaseBudgets().get(i).getPartAuditOfOperations()
								+ pb.getPhaseBudgets().get(i).getPartAuditOfOperations());

				totalWp.getPhaseBudgets().get(i).setPartAuthoritiesCertification(
						totalWp.getPhaseBudgets().get(i).getPartAuthoritiesCertification()
								+ pb.getPhaseBudgets().get(i).getPartAuthoritiesCertification());

				totalWp.getPhaseBudgets().get(i)
						.setPartIntermEvaluation(totalWp.getPhaseBudgets().get(i).getPartIntermEvaluation()
								+ pb.getPhaseBudgets().get(i).getPartIntermEvaluation());
			}
		}
		cfSummaryWrapperWp = new CostItemSummaryWrapper();
		this.listSummaryWp = new ArrayList<CostItemSummaryWrapper>();
		Double previousExpenditureRequistedTotal = 0d;
		for (Phase phase : totalWp.getPhaseBudgets()) {
			CostItemSummaryWrapper wrap = new CostItemSummaryWrapper();
			wrap.setPhaseId(phase.getPhase().getId());
			wrap.setName(phase.getPhase().getValue());
			wrap.setShowOnlyFirstColumn(false);
			wrap.setAmountFromBudget(phase.getPhaseTotal());
			wrap.setAmountByCf(GetCostDefinitionTotalAmountForPhase(phase.getPhase().getId(), userId));
			wrap.setAmountCostCertificate(GetCostDefinitionTotalCILAmountForPhase(phase.getPhase().getId(), userId));
			wrap.setAmountRequest(GetCostDefinitionTotalRequestAGUForPhase(phase.getPhase().getId(), userId));
			wrap.setAbsorption(wrap.getAmountFromBudget() != null && wrap.getAmountFromBudget() != 0l
					? this.GetCostDefinitionLastCertifiedAmountForPhase(phase.getPhase().getId(), userId)
							/ wrap.getAmountFromBudget()
					: new Double(0d));
			wrap.setReinstated(this.GetCostDefinitionReinstatedForPhase(phase.getPhase().getId(), userId));
			wrap.setPreviousExpenditureRequisted(
					GetCostDefinitionTotalAmountForPhaseOldDDR(phase.getPhase().getId(), userId));
			previousExpenditureRequistedTotal += wrap.getPreviousExpenditureRequisted();
			listSummaryWp.add(wrap);
		}

		calcCostItemAdditionalAmountsForPhase(userId);

		CostItemSummaryWrapper entityTotal = this.prepareTotalEntityWp();
		entityTotal.setName(Utils.getString("Resources", "total", null));
		entityTotal.setShowOnlyFirstColumn(false);
		entityTotal.setPreviousExpenditureRequisted(previousExpenditureRequistedTotal);
		this.listSummaryWp.add(entityTotal);

		// Other fields
		Double amountByCfPublic = 0d;
		Double amountByCfPrivate = 0d;
		Double amountCostCertificatePublic = 0d;
		Double amountCostCertificatePrivate = 0d;
		Double amountRequestPublic = 0d;
		Double amountRequestPrivate = 0d;
		Double stcCertificatedPublic = 0d;
		Double stcCertificatedPrivate = 0d;
		Double acuCertificatedPublic = 0d;
		Double acuCertificatedPrivate = 0d;
		Double aguCertificatedPublic = 0d;
		Double aguCertificatedPrivate = 0d;
		Double previsionExCnPrivate = 0d;
		Double previsionExCnPublic = 0d;
		Double previsionExAdditionalFinansing = 0d;

		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gbudget = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
							.get(0);

					if (gbudget != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gbudget.getFesr()
								/ (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate() - gbudget.getNetRevenue()))
								* 100) == 50) {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.5d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.5d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.5d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.5d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.5d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.5d);
						} else {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.15d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						previsionExCnPublic += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
						acuCertificatedPublic += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
						aguCertificatedPublic += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
						stcCertificatedPublic += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
						amountByCfPublic += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
						amountCostCertificatePublic += (this.getValidatedCostCertificateAmountSubAdditional(cost)
								* 0.15d);
						amountRequestPublic += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
					}
					previsionExAdditionalFinansing += GetLastAdditionaFinansingValidated(cost);
				}
			}

		}
		Double previsionExCnPrivateOld = 0d;
		Double previsionExCnPublicOld = 0d;
		Double previsionExAdditionalFinansingOld = 0d;

		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					boolean compute = true;
					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
						if (cd.getId().equals(cost.getId())) {
							compute = false;
						}

					}
					if (compute) {
						compute = false;
						ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
						gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
								.get(0);

						if (gbudget != null) {
							if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
								if (((gbudget.getFesr() / (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate()
										- gbudget.getNetRevenue())) * 100) == 50) {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
								} else {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
								}

							} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
								previsionExCnPublicOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							}
						}
						previsionExAdditionalFinansingOld += GetLastAdditionaFinansingValidated(cost);
					}
				}
			}
		}

		BudgetInputSourceDivided budget = BudgetInputSourceDividedBean.GetByProject(this.getCurrentProjectId()).get(0);
		cfSummaryWrapperWp.setAmountFromBudget(budget.getCnPrivate());
		cfSummaryWrapperWp.setName(Utils.getString("Resources", "durCompilationTotalEntity", null));
		cfSummaryWrapperWp
				.setPreviousExpenditureRequisted(previsionExAdditionalFinansingOld + previsionExAdditionalFinansing);
		CostItemSummaryWrapper objFesrTotal = new CostItemSummaryWrapper();
		objFesrTotal.setName(Utils.getString("Resources", "durCompilationEditFesrTotal", null));
		objFesrTotal.setShowOnlyFirstColumn(true);
		objFesrTotal.setAmountFromBudget(budget.getFesr());
		// objFesrTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapperWp.getAmountByCf()) * 0.85d);
		// objFesrTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapperWp.getAmountCostCertificate()) * 0.85d);
		// objFesrTotal.setAmountRequest((entityTotal.getAmountRequest() -
		// cfSummaryWrapperWp.getAmountRequest()) * 0.85d);
		// objFesrTotal.setStcCertificated(
		// (entityTotal.getStcCertificated() -
		// cfSummaryWrapperWp.getStcCertificated()) * 0.85d);
		// objFesrTotal.setAcuCertificated(
		// (entityTotal.getAcuCertificated() -
		// cfSummaryWrapperWp.getAcuCertificated()) * 0.85d);
		// objFesrTotal.setAguCertificated(
		// (entityTotal.getAguCertificated() -
		// cfSummaryWrapperWp.getAguCertificated()) * 0.85d);
		objFesrTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapperWp.getSuspended()) * 0.75d);
		objFesrTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapperWp.getRectified()) * 0.75d);
		objFesrTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapperWp.getReinstated()) * 0.75d);

		objFesrTotal.setAmountByCf(this.GetFesrTotalAmountByCf());
		objFesrTotal.setAmountCostCertificate(this.GetFesrTotalAmountCostCertificate());
		objFesrTotal.setAmountRequest(this.GetFesrTotalAmountRequest());
		objFesrTotal
				.setStcCertificated((entityTotal.getStcCertificated() == null || entityTotal.getStcCertificated() == 0)
						? 0d : this.GetFesrTotalAmountStcCertificated());
		objFesrTotal
				.setAcuCertificated((entityTotal.getAcuCertificated() == null || entityTotal.getAcuCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAcuCertificated());
		objFesrTotal
				.setAguCertificated((entityTotal.getAguCertificated() == null || entityTotal.getAguCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAguCertificated());
		objFesrTotal.setPreviousExpenditureRequisted(this.calculateFesrAmount2() + this.calculateFesrAmountOldDur());

		CostItemSummaryWrapper objCnTotal = new CostItemSummaryWrapper();
		objCnTotal.setName(Utils.getString("Resources", "durCompilationEditCnTotal", null));
		objCnTotal.setShowOnlyFirstColumn(true);
		objCnTotal.setAmountFromBudget(budget.getCnPublic() + budget.getCnPublicOther());
		// objCnTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapperWp.getAmountByCf()) * 0.15d);
		// objCnTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapperWp.getAmountCostCertificate()) * 0.15d);
		// objCnTotal.setAmountRequest((entityTotal.getAmountRequest() -
		// cfSummaryWrapperWp.getAmountRequest()) * 0.15d);
		// objCnTotal.setStcCertificated(
		// (entityTotal.getStcCertificated() -
		// cfSummaryWrapperWp.getStcCertificated()) * 0.15d);
		// objCnTotal.setAcuCertificated(
		// (entityTotal.getAcuCertificated() -
		// cfSummaryWrapperWp.getAcuCertificated()) * 0.15d);
		// objCnTotal.setAguCertificated(
		// (entityTotal.getAguCertificated() -
		// cfSummaryWrapperWp.getAguCertificated()) * 0.15d);
		// objCnTotal.setSuspended((entityTotal.getSuspended() -
		// cfSummaryWrapperWp.getSuspended()) * 0.25d);
		// objCnTotal.setRectified((entityTotal.getRectified() -
		// cfSummaryWrapperWp.getRectified()) * 0.25d);
		// objCnTotal.setReinstated((entityTotal.getReinstated() -
		// cfSummaryWrapperWp.getReinstated()) * 0.25d);
		objCnTotal.setAcuCertificated(acuCertificatedPrivate + acuCertificatedPublic);
		objCnTotal.setAguCertificated(aguCertificatedPrivate + aguCertificatedPublic);
		objCnTotal.setStcCertificated(stcCertificatedPrivate + stcCertificatedPublic);
		objCnTotal.setAmountByCf(amountByCfPrivate + amountByCfPublic);
		objCnTotal.setAmountCostCertificate(amountCostCertificatePrivate + amountCostCertificatePublic);
		objCnTotal.setAmountRequest(amountRequestPrivate + amountRequestPublic);
		objCnTotal.setPreviousExpenditureRequisted(
				previsionExCnPublic + previsionExCnPrivate + previsionExCnPublicOld + previsionExCnPrivateOld);

		List<GeneralBudgets> listGB = BeansFactory.GeneralBudgets()
				.GetItemsForProject(Long.valueOf(this.getCurrentProjectId()));
		CostItemSummaryWrapper objCnItTotal = this.prepareCnForCountry(CountryTypes.Italy);
		CostItemSummaryWrapper objCnFrTotal = this.prepareCnForCountry(CountryTypes.France);

		objCnItTotal.setName(Utils.getString("Resources", "durCompilationEditCnItTotal", null));
		objCnItTotal.setShowOnlyFirstColumn(true);
		objCnItTotal.setAmountFromBudget(0d);
		objCnFrTotal.setName(Utils.getString("Resources", "durCompilationEditCnFrTotal", null));
		objCnFrTotal.setShowOnlyFirstColumn(true);
		objCnFrTotal.setAmountFromBudget(0d);

		for (GeneralBudgets gb : listGB) {
			if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.Italy.getCountry())) {
				if (gb.getCnPublic() != null) {
					objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getCnPublic());
				}
				if (gb.getQuotaPrivate() != null) {
					objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getQuotaPrivate());
				}

			} else if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.France.getCountry())) {
				if (gb.getCnPublic() != null) {
					objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getCnPublic());
				}
				if (gb.getQuotaPrivate() != null) {
					objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getQuotaPrivate());
				}
			}
		}

		CostItemSummaryWrapper objCnPublic = new CostItemSummaryWrapper();
		objCnPublic.setName(Utils.getString("Resources", "durCompilationEditPublicCN", null));
		objCnPublic.setShowOnlyFirstColumn(true);
		objCnPublic.setAmountFromBudget(budget.getCnPublic());
		objCnPublic.setAcuCertificated(acuCertificatedPublic);
		objCnPublic.setAguCertificated(aguCertificatedPublic);
		objCnPublic.setStcCertificated(stcCertificatedPublic);
		objCnPublic.setAmountByCf(amountByCfPublic);
		objCnPublic.setAmountCostCertificate(amountCostCertificatePublic);
		objCnPublic.setAmountRequest(amountRequestPublic);
		objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublic + previsionExCnPublicOld);

		CostItemSummaryWrapper objCnPrivate = new CostItemSummaryWrapper();
		objCnPrivate.setName(Utils.getString("Resources", "durCompilationEditPrivateCN", null));
		objCnPrivate.setShowOnlyFirstColumn(true);
		objCnPrivate.setAmountFromBudget(budget.getCnPublicOther());
		objCnPrivate.setAcuCertificated(acuCertificatedPrivate);
		objCnPrivate.setAguCertificated(aguCertificatedPrivate);
		objCnPrivate.setStcCertificated(stcCertificatedPrivate);
		objCnPrivate.setAmountByCf(amountByCfPrivate);
		objCnPrivate.setAmountCostCertificate(amountCostCertificatePrivate);
		objCnPrivate.setAmountRequest(amountRequestPrivate);
		objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivate + previsionExCnPrivateOld);

		// added for bug 0027352
		roundDurParametersInGrid(objCnItTotal);
		roundDurParametersInGrid(objCnFrTotal);
		//

		this.listSummaryWp.add(objFesrTotal);
		this.listSummaryWp.add(objCnTotal);
		this.listSummaryWp.add(objCnPublic);
		this.listSummaryWp.add(objCnPrivate);
		// this.listSummaryWp.add(objCnItTotal);
		// this.listSummaryWp.add(objCnFrTotal);
		this.listSummaryWp.add(cfSummaryWrapperWp);
		// added for bug 0027341
		// set the last not 0.0 column
		// updateTotalValues(entityTotal, objFesrTotal, objCnItTotal,
		// objCnFrTotal);

	}

	private Double getValidatedAcuAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			amount += cost.getAcuCertification();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getAcuCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAcuCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidatedAguAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			amount += cost.getAguCertification();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getAguCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getAguCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidatedStcAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			amount += cost.getStcCertification();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getStcCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getStcCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidatedByCfAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getAmountReport() != null) {
			amount += cost.getAmountReport();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing()) && cost.getAdditionalFinansingAmount() != null) {
				amount -= cost.getAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidatedCostCertificateAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getCilCheck() != null)

		{
			amount += cost.getCilCheck();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getCilCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCilCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private Double getValidatedRequestAmountSubAdditional(CostDefinitions cost) {
		double amount = 0d;
		if (cost.getCfCheck() != null && cost.getCreatedByPartner()) {
			amount += cost.getCfCheck();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getCfCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCfCheckAdditionalFinansingAmount();
			}
		} else if (!cost.getCreatedByPartner() && cost.getCilCheck() != null) {
			amount += cost.getCilCheck();
			if (Boolean.TRUE.equals(cost.getAdditionalFinansing())
					&& cost.getCilCheckAdditionalFinansingAmount() != null) {
				amount -= cost.getCilCheckAdditionalFinansingAmount();
			}
		}
		return amount;
	}

	private void FillGridSummaryPartner() throws HibernateException, NumberFormatException, PersistenceBeanException {

		this.listSummaryPartner = new ArrayList<CostItemSummaryWrapper>();
		cfSummaryWrapperPartner = new CostItemSummaryWrapper();
		double previousExpenditureRequistedTotal = 0d;
		for (CFPartnerAnagraphs p : partnersForProject) {

			CostItemSummaryWrapper wrap = new CostItemSummaryWrapper();
			wrap.setUserId(p.getUser().getId());
			wrap.setName(p.getNaming());
			wrap.setShowOnlyFirstColumn(false);

			List<PartnersBudgets> budgets = BeansFactory.PartnersBudgets()
					.LoadByPartnerApproved(this.getCurrentProjectId(), p.getId());
			double total = 0d;

			for (PartnersBudgets budget : budgets) {
				for (Phase phase : budget.getPhaseBudgets()) {
					total += phase.getPhaseTotal().doubleValue();
				}
			}

			wrap.setAmountFromBudget(new Double(total));
			wrap.setAmountByCf(GetCostDefinitionTotalAmountForPartner(p.getUser().getId()));
			wrap.setAmountCostCertificate(GetCostDefinitionTotalCILAmountForPartner(p.getUser().getId()));
			wrap.setAmountRequest(GetCostDefinitionTotalRequestAGUForPartner(p.getUser().getId()));
			wrap.setAbsorption(wrap.getAmountFromBudget() != null && wrap.getAmountFromBudget() != 0l
					? this.GetCostDefinitionLastCertifiedAmountForPartner(p.getUser().getId())
							/ wrap.getAmountFromBudget()
					: new Double(0d));
			wrap.setReinstated(this.GetCostDefinitionReinstatedForPartner(p.getUser().getId()));
			double previousExpenditureRequisted = this.GetCostByPartnerListWeb(p.getUser().getId())
					+ this.GetCostByPartnerOldDur(p.getUser().getId());
			wrap.setPreviousExpenditureRequisted(previousExpenditureRequisted);
			previousExpenditureRequistedTotal += previousExpenditureRequisted;
			this.listSummaryPartner.add(wrap);
		}

		calcCostItemAdditionalAmountsForPartner();

		CostItemSummaryWrapper entityTotal = this.prepareTotalEntityPartner();
		entityTotal.setName(Utils.getString("Resources", "total", null));
		entityTotal.setShowOnlyFirstColumn(false);
		entityTotal.setPreviousExpenditureRequisted(previousExpenditureRequistedTotal);
		this.listSummaryPartner.add(entityTotal);

		// Other fields
		Double previsionExCnPrivateOld = 0d;
		Double previsionExCnPublicOld = 0d;
		Double previsionExAdditionalFinansingOld = 0d;

		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gbudget = null;
		CFPartnerAnagraphs ca = null;

		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					boolean compute = true;
					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
						if (cd.getId().equals(cost.getId())) {
							compute = false;
						}

					}
					if (compute) {
						compute = false;
						ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
						gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
								.get(0);

						if (gbudget != null) {
							if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
								if (((gbudget.getFesr() / (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate()
										- gbudget.getNetRevenue())) * 100) == 50) {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
								} else {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
								}

							} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
								previsionExCnPublicOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							}
						}
						previsionExAdditionalFinansingOld += GetLastAdditionaFinansingValidated(cost);
					}
				}
			}
		}

		Double amountByCfPublic = 0d;
		Double amountByCfPrivate = 0d;
		Double amountCostCertificatePublic = 0d;
		Double amountCostCertificatePrivate = 0d;
		Double amountRequestPublic = 0d;
		Double amountRequestPrivate = 0d;
		Double stcCertificatedPublic = 0d;
		Double stcCertificatedPrivate = 0d;
		Double acuCertificatedPublic = 0d;
		Double acuCertificatedPrivate = 0d;
		Double aguCertificatedPublic = 0d;
		Double aguCertificatedPrivate = 0d;
		Double previsionExCnPrivate = 0d;
		Double previsionExCnPublic = 0d;
		Double previsionExAdditionalFinansing = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
							.get(0);

					if (gbudget != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gbudget.getFesr()
								/ (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate() - gbudget.getNetRevenue()))
								* 100) == 50) {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.5d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.5d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.5d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.5d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.5d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.5d);
						} else {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.15d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						previsionExCnPublic += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
						acuCertificatedPublic += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
						aguCertificatedPublic += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
						stcCertificatedPublic += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
						amountByCfPublic += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
						amountCostCertificatePublic += (this.getValidatedCostCertificateAmountSubAdditional(cost)
								* 0.15d);
						amountRequestPublic += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
					}
					previsionExAdditionalFinansing += GetLastAdditionaFinansingValidated(cost);
				}
			}

		}

		BudgetInputSourceDivided budget = BudgetInputSourceDividedBean.GetByProject(this.getCurrentProjectId()).get(0);
		cfSummaryWrapperPartner.setAmountFromBudget(budget.getCnPrivate());
		cfSummaryWrapperPartner.setName(Utils.getString("Resources", "durCompilationTotalEntity", null));
		cfSummaryWrapperPartner
				.setPreviousExpenditureRequisted(previsionExAdditionalFinansingOld + previsionExAdditionalFinansing);
		CostItemSummaryWrapper objFesrTotal = new CostItemSummaryWrapper();
		objFesrTotal.setName(Utils.getString("Resources", "durCompilationEditFesrTotal", null));
		objFesrTotal.setShowOnlyFirstColumn(true);
		objFesrTotal.setAmountFromBudget(budget.getFesr());
		// objFesrTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapperPartner.getAmountByCf()) * 0.85d);
		// objFesrTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapperPartner.getAmountCostCertificate()) * 0.85d);
		// objFesrTotal.setAmountRequest(
		// (entityTotal.getAmountRequest() -
		// cfSummaryWrapperPartner.getAmountRequest()) * 0.85d);
		// objFesrTotal.setStcCertificated(
		// (entityTotal.getStcCertificated() -
		// cfSummaryWrapperPartner.getStcCertificated()) * 0.85d);
		// objFesrTotal.setAcuCertificated(
		// (entityTotal.getAcuCertificated() -
		// cfSummaryWrapperPartner.getAcuCertificated()) * 0.85d);
		// objFesrTotal.setAguCertificated(
		// (entityTotal.getAguCertificated() -
		// cfSummaryWrapperPartner.getAguCertificated()) * 0.85d);
		objFesrTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapperPartner.getSuspended()) * 0.75d);
		objFesrTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapperPartner.getRectified()) * 0.75d);
		objFesrTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapperPartner.getReinstated()) * 0.75d);

		objFesrTotal.setAmountByCf(this.GetFesrTotalAmountByCf());
		objFesrTotal.setAmountCostCertificate(this.GetFesrTotalAmountCostCertificate());
		objFesrTotal.setAmountRequest(this.GetFesrTotalAmountRequest());
		objFesrTotal
				.setStcCertificated((entityTotal.getStcCertificated() == null || entityTotal.getStcCertificated() == 0)
						? 0d : this.GetFesrTotalAmountStcCertificated());
		objFesrTotal
				.setAcuCertificated((entityTotal.getAcuCertificated() == null || entityTotal.getAcuCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAcuCertificated());
		objFesrTotal
				.setAguCertificated((entityTotal.getAguCertificated() == null || entityTotal.getAguCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAguCertificated());
		objFesrTotal.setPreviousExpenditureRequisted(this.calculateFesrAmount2() + this.calculateFesrAmountOldDur());

		// CostItemSummaryWrapper objCnTotal = new CostItemSummaryWrapper();
		// objCnTotal.setName(Utils.getString("Resources",
		// "durCompilationEditCnTotal", null));
		// objCnTotal.setShowOnlyFirstColumn(true);
		// objCnTotal.setAmountFromBudget(budget.getCnPublic());
		// objCnTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapperPartner.getAmountByCf()) * 0.15d);
		// objCnTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapperPartner.getAmountCostCertificate()) * 0.15d);
		// objCnTotal.setAmountRequest(
		// (entityTotal.getAmountRequest() -
		// cfSummaryWrapperPartner.getAmountRequest()) * 0.15d);
		// objCnTotal.setStcCertificated(
		// (entityTotal.getStcCertificated() -
		// cfSummaryWrapperPartner.getStcCertificated()) * 0.15d);
		// objCnTotal.setAcuCertificated(
		// (entityTotal.getAcuCertificated() -
		// cfSummaryWrapperPartner.getAcuCertificated()) * 0.15d);
		// objCnTotal.setAguCertificated(
		// (entityTotal.getAguCertificated() -
		// cfSummaryWrapperPartner.getAguCertificated()) * 0.15d);
		// objCnTotal.setSuspended((entityTotal.getSuspended() -
		// cfSummaryWrapperPartner.getSuspended()) * 0.25d);
		// objCnTotal.setRectified((entityTotal.getRectified() -
		// cfSummaryWrapperPartner.getRectified()) * 0.25d);
		// objCnTotal.setReinstated((entityTotal.getReinstated() -
		// cfSummaryWrapperPartner.getReinstated()) * 0.25d);
		// objCnTotal.setAmountByCf(this.CnPrivateAmountByCf() +
		// this.CnPublicAmountByCf());
		// objCnTotal
		// .setAmountCostCertificate(this.CnPrivateAmountCostCertificate() +
		// this.CnPublicAmountCostCertificate());
		// objCnTotal.setAmountRequest(this.CnPublicAmountRequest() +
		// this.CnPrivateAmountRequest());
		// objCnTotal.setStcCertificated(this.CnPublicStcCertified() +
		// this.CnPrivateStcCertified());
		// objCnTotal.setAguCertificated(this.CnPublicAguCertified() +
		// this.CnPrivateAguCertified());
		// objCnTotal.setAcuCertificated(this.CnPublicAcuCertified() +
		// this.CnPrivateAcuCertified());
		// objCnTotal.setPreviousExpenditureRequisted(previsionExCnPrivateOld +
		// previsionExCnPublicOld);

		CostItemSummaryWrapper objCnTotal = new CostItemSummaryWrapper();
		objCnTotal.setName(Utils.getString("Resources", "durCompilationEditCnTotal", null));
		objCnTotal.setShowOnlyFirstColumn(true);
		objCnTotal.setAmountFromBudget(budget.getCnPublic() + budget.getCnPublicOther());
		// objCnTotal.setAmountByCf((entityTotal.getAmountByCf() -
		// cfSummaryWrapperWp.getAmountByCf()) * 0.15d);
		// objCnTotal.setAmountCostCertificate(
		// (entityTotal.getAmountCostCertificate() -
		// cfSummaryWrapperWp.getAmountCostCertificate()) * 0.15d);
		// objCnTotal.setAmountRequest((entityTotal.getAmountRequest() -
		// cfSummaryWrapperWp.getAmountRequest()) * 0.15d);
		// objCnTotal.setStcCertificated(
		// (entityTotal.getStcCertificated() -
		// cfSummaryWrapperWp.getStcCertificated()) * 0.15d);
		// objCnTotal.setAcuCertificated(
		// (entityTotal.getAcuCertificated() -
		// cfSummaryWrapperWp.getAcuCertificated()) * 0.15d);
		// objCnTotal.setAguCertificated(
		// (entityTotal.getAguCertificated() -
		// cfSummaryWrapperWp.getAguCertificated()) * 0.15d);
		objCnTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapperWp.getSuspended()) * 0.25d);
		objCnTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapperWp.getRectified()) * 0.25d);
		objCnTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapperWp.getReinstated()) * 0.25d);
		objCnTotal.setAcuCertificated(acuCertificatedPrivate + acuCertificatedPublic);
		objCnTotal.setAguCertificated(aguCertificatedPrivate + aguCertificatedPublic);
		objCnTotal.setStcCertificated(stcCertificatedPrivate + stcCertificatedPublic);
		objCnTotal.setAmountByCf(amountByCfPrivate + amountByCfPublic);
		objCnTotal.setAmountCostCertificate(amountCostCertificatePrivate + amountCostCertificatePublic);
		objCnTotal.setAmountRequest(amountRequestPrivate + amountRequestPublic);
		objCnTotal.setPreviousExpenditureRequisted(
				previsionExCnPublic + previsionExCnPrivate + previsionExCnPublicOld + previsionExCnPrivateOld);

		CostItemSummaryWrapper objCnPublic = new CostItemSummaryWrapper();
		objCnPublic.setName(Utils.getString("Resources", "durCompilationEditPublicCN", null));
		objCnPublic.setShowOnlyFirstColumn(true);
		objCnPublic.setAmountFromBudget(budget.getCnPublic());
		// objCnPublic.setAmountByCf(this.CnPublicAmountByCf());
		// objCnPublic.setAmountCostCertificate(this.CnPublicAmountCostCertificate());
		// objCnPublic.setAmountRequest(this.CnPublicAmountRequest());
		// objCnPublic.setStcCertificated(this.CnPublicStcCertified());
		// objCnPublic.setAguCertificated(this.CnPublicAguCertified());
		// objCnPublic.setAcuCertificated(this.CnPublicAcuCertified());
		// objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublicOld);
		objCnPublic.setAcuCertificated(acuCertificatedPublic);
		objCnPublic.setAguCertificated(aguCertificatedPublic);
		objCnPublic.setStcCertificated(stcCertificatedPublic);
		objCnPublic.setAmountByCf(amountByCfPublic);
		objCnPublic.setAmountCostCertificate(amountCostCertificatePublic);
		objCnPublic.setAmountRequest(amountRequestPublic);
		objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublic + previsionExCnPublicOld);

		CostItemSummaryWrapper objCnPrivate = new CostItemSummaryWrapper();
		objCnPrivate.setName(Utils.getString("Resources", "durCompilationEditPrivateCN", null));
		objCnPrivate.setShowOnlyFirstColumn(true);
		objCnPrivate.setAmountFromBudget(budget.getCnPublicOther());
		// objCnPrivate.setAmountByCf(this.CnPrivateAmountByCf());
		// objCnPrivate.setAmountCostCertificate(this.CnPrivateAmountCostCertificate());
		// objCnPrivate.setAmountRequest(this.CnPrivateAmountRequest());
		// objCnPrivate.setStcCertificated(this.CnPrivateStcCertified());
		// objCnPrivate.setAguCertificated(this.CnPrivateAguCertified());
		// objCnPrivate.setAcuCertificated(this.CnPrivateAcuCertified());
		// objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivateOld);
		objCnPrivate.setAcuCertificated(acuCertificatedPrivate);
		objCnPrivate.setAguCertificated(aguCertificatedPrivate);
		objCnPrivate.setStcCertificated(stcCertificatedPrivate);
		objCnPrivate.setAmountByCf(amountByCfPrivate);
		objCnPrivate.setAmountCostCertificate(amountCostCertificatePrivate);
		objCnPrivate.setAmountRequest(amountRequestPrivate);
		objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivate + previsionExCnPrivateOld);

		this.listSummaryPartner.add(objFesrTotal);
		this.listSummaryPartner.add(objCnTotal);
		this.listSummaryPartner.add(objCnPublic);
		this.listSummaryPartner.add(objCnPrivate);
		this.listSummaryPartner.add(cfSummaryWrapperPartner);
	}

	private void FillGridSummary() throws HibernateException, NumberFormatException, PersistenceBeanException {
		Long userId = null;

		List<PartnersBudgets> listBudgets = new ArrayList<PartnersBudgets>();
		for (CFPartnerAnagraphs p : partnersForProject) {
			if (this.getSelectedPartnerForGrid() == null || this.getSelectedPartnerForGrid() == 0l) {
				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				continue;
			}

			else if (p.getId().equals(this.getSelectedPartnerForGrid())) {

				listBudgets.addAll(
						BeansFactory.PartnersBudgets().LoadByPartnerApproved(this.getCurrentProjectId(), p.getId()));
				userId = p.getUser().getId();
				break;

			}

		}

		PartnersBudgets total = new PartnersBudgets();
		total.setPhaseBudgets(new LinkedList<Phase>());

		for (CostDefinitionPhases cdPhase : BeansFactory.CostDefinitionPhase().Load()) {
			Phase newPhase = new Phase();
			newPhase.setPhase(cdPhase);
			newPhase.setPartnersBudgets(total);
			newPhase.setPartAdvInfoProducts(0d);
			newPhase.setPartAdvInfoPublicEvents(0d);
			newPhase.setPartDurableGoods(0d);
			newPhase.setPartDurables(0d);
			newPhase.setPartHumanRes(0d);
			newPhase.setPartMissions(0d);
			newPhase.setPartOther(0d);
			newPhase.setPartGeneralCosts(0d);
			newPhase.setPartOverheads(0d);
			newPhase.setPartProvisionOfService(0d);

			newPhase.setPartContainer(0d);
			newPhase.setPartPersonalExpenses(0d);
			newPhase.setPartCommunicationAndInformation(0d);
			newPhase.setPartOrganizationOfCommitees(0d);
			newPhase.setPartOtherFees(0d);
			newPhase.setPartAutoCoordsTunis(0d);
			newPhase.setPartContactPoint(0d);
			newPhase.setPartSystemControlAndManagement(0d);
			newPhase.setPartAuditOfOperations(0d);
			newPhase.setPartAuthoritiesCertification(0d);
			newPhase.setPartIntermEvaluation(0d);

			total.getPhaseBudgets().add(newPhase);
		}

		for (PartnersBudgets pb : listBudgets) {
			for (int i = 0; i < pb.getPhaseBudgets().size(); i++) {
				total.getPhaseBudgets().get(i)
						.setPartAdvInfoProducts(pb.getPhaseBudgets().get(i).getPartAdvInfoProducts()
								+ total.getPhaseBudgets().get(i).getPartAdvInfoProducts());
				total.getPhaseBudgets().get(i)
						.setPartAdvInfoPublicEvents(pb.getPhaseBudgets().get(i).getPartAdvInfoPublicEvents()
								+ total.getPhaseBudgets().get(i).getPartAdvInfoPublicEvents());

				total.getPhaseBudgets().get(i).setPartDurableGoods(pb.getPhaseBudgets().get(i).getPartDurableGoods()
						+ total.getPhaseBudgets().get(i).getPartDurableGoods());

				total.getPhaseBudgets().get(i).setPartDurables(pb.getPhaseBudgets().get(i).getPartDurables()
						+ total.getPhaseBudgets().get(i).getPartDurables());

				total.getPhaseBudgets().get(i).setPartHumanRes(pb.getPhaseBudgets().get(i).getPartHumanRes()
						+ total.getPhaseBudgets().get(i).getPartHumanRes());
				total.getPhaseBudgets().get(i).setPartMissions(pb.getPhaseBudgets().get(i).getPartMissions()
						+ total.getPhaseBudgets().get(i).getPartMissions());
				total.getPhaseBudgets().get(i).setPartGeneralCosts(pb.getPhaseBudgets().get(i).getPartGeneralCosts()
						+ total.getPhaseBudgets().get(i).getPartGeneralCosts());

				total.getPhaseBudgets().get(i).setPartOther(
						pb.getPhaseBudgets().get(i).getPartOther() + total.getPhaseBudgets().get(i).getPartOther());

				total.getPhaseBudgets().get(i).setPartOverheads(pb.getPhaseBudgets().get(i).getPartOverheads()
						+ total.getPhaseBudgets().get(i).getPartOverheads());

				total.getPhaseBudgets().get(i)
						.setPartProvisionOfService(pb.getPhaseBudgets().get(i).getPartProvisionOfService()
								+ total.getPhaseBudgets().get(i).getPartProvisionOfService());

				total.getPhaseBudgets().get(i).setPartContainer(total.getPhaseBudgets().get(i).getPartContainer()
						+ pb.getPhaseBudgets().get(i).getPartContainer());

				total.getPhaseBudgets().get(i)
						.setPartPersonalExpenses(total.getPhaseBudgets().get(i).getPartPersonalExpenses()
								+ pb.getPhaseBudgets().get(i).getPartPersonalExpenses());

				total.getPhaseBudgets().get(i).setPartCommunicationAndInformation(
						total.getPhaseBudgets().get(i).getPartCommunicationAndInformation()
								+ pb.getPhaseBudgets().get(i).getPartCommunicationAndInformation());

				total.getPhaseBudgets().get(i)
						.setPartOrganizationOfCommitees(total.getPhaseBudgets().get(i).getPartOrganizationOfCommitees()
								+ pb.getPhaseBudgets().get(i).getPartOrganizationOfCommitees());

				total.getPhaseBudgets().get(i).setPartOtherFees(total.getPhaseBudgets().get(i).getPartOtherFees()
						+ pb.getPhaseBudgets().get(i).getPartOtherFees());

				total.getPhaseBudgets().get(i)
						.setPartAutoCoordsTunis(total.getPhaseBudgets().get(i).getPartAutoCoordsTunis()
								+ pb.getPhaseBudgets().get(i).getPartAutoCoordsTunis());

				total.getPhaseBudgets().get(i).setPartContactPoint(total.getPhaseBudgets().get(i).getPartContactPoint()
						+ pb.getPhaseBudgets().get(i).getPartContactPoint());

				total.getPhaseBudgets().get(i).setPartSystemControlAndManagement(
						total.getPhaseBudgets().get(i).getPartSystemControlAndManagement()
								+ pb.getPhaseBudgets().get(i).getPartSystemControlAndManagement());

				total.getPhaseBudgets().get(i)
						.setPartAuditOfOperations(total.getPhaseBudgets().get(i).getPartAuditOfOperations()
								+ pb.getPhaseBudgets().get(i).getPartAuditOfOperations());

				total.getPhaseBudgets().get(i).setPartAuthoritiesCertification(
						total.getPhaseBudgets().get(i).getPartAuthoritiesCertification()
								+ pb.getPhaseBudgets().get(i).getPartAuthoritiesCertification());

				total.getPhaseBudgets().get(i)
						.setPartIntermEvaluation(total.getPhaseBudgets().get(i).getPartIntermEvaluation()
								+ pb.getPhaseBudgets().get(i).getPartIntermEvaluation());
			}
		}

		this.listSummary = new ArrayList<CostItemSummaryWrapper>();

		cfSummaryWrapper = new CostItemSummaryWrapper();

		//

		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);
		Double hrTotal = 0d;
		Double psTotal = 0d;
		Double mTotal = 0d;
		Double diTotal = 0d;
		Double dgTotal = 0d;
		Double aipeTotal = 0d;
		Double aipTotal = 0d;
		Double ohTotal = 0d;
		Double gcTotal = 0d;
		Double otTotal = 0d;
		if (durList != null) {

			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					if (cost.getCostItem().getId().equals(CostItemTypes.HumanResources.getValue())) {
						hrTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.ProvisionOfService.getValue())) {
						psTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.Missions.getValue())) {
						mTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.DurablesInfrastructure.getValue())) {
						diTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.DurableGoods.getValue())) {
						dgTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.AdvInfoPubEvents.getValue())) {
						aipeTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.AdvInfoProducts.getValue())) {
						aipTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.Overheads.getValue())) {
						ohTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.GeneralCosts.getValue())) {
						gcTotal += this.getValidateAmount(cost);
					} else if (cost.getCostItem().getId().equals(CostItemTypes.Other.getValue())) {
						otTotal += this.getValidateAmount(cost);
					}
				}
			}
			if (this.getDurCompilation() != null && this.getDurCompilation().getId() == null
					&& this.getListCostDefinitionsWeb() != null) {
				for (CostDefinitions cost : this.getListCostDefinitionsWeb()) {
					if (cost.getSelected()) {
						if (cost.getCostItem() != null) {
							if (cost.getCostItem().getId().equals(CostItemTypes.HumanResources.getValue())) {
								hrTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.ProvisionOfService.getValue())) {
								psTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.Missions.getValue())) {
								mTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId()
									.equals(CostItemTypes.DurablesInfrastructure.getValue())) {
								diTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.DurableGoods.getValue())) {
								dgTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.AdvInfoPubEvents.getValue())) {
								aipeTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.AdvInfoProducts.getValue())) {
								aipTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.Overheads.getValue())) {
								ohTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.GeneralCosts.getValue())) {
								gcTotal += this.getValidateAmount(cost);
							} else if (cost.getCostItem().getId().equals(CostItemTypes.Other.getValue())) {
								otTotal += this.getValidateAmount(cost);
							}
						}
					}
				}
			}
		}
		// Calculate each cost item

		// Human resources
		CostItemSummaryWrapper objHumanResources = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.HumanResources });
		objHumanResources.setName(Utils.getString("Resources", "durCompilationEditCostItem1", null));
		objHumanResources.setShowOnlyFirstColumn(false);

		objHumanResources.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objHumanResources.setAmountFromBudget(objHumanResources.getAmountFromBudget() + phase.getPartHumanRes());

		objHumanResources.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.HumanResources, userId));
		objHumanResources
				.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.HumanResources, userId));

		objHumanResources.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.HumanResources, userId));

		if (objHumanResources.getAmountFromBudget() != 0) {
			objHumanResources
					.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.HumanResources, userId)
							/ objHumanResources.getAmountFromBudget() * 100d);
		} else {
			objHumanResources.setAbsorption(0d);
		}
		objHumanResources.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.HumanResources, userId));
		objHumanResources.setPreviousExpenditureRequisted(hrTotal);
		if (objHumanResources.getAmountFromBudget() != 0 && objHumanResources.getAmountRequest() != null) {
			objHumanResources.setDurSpendingPt2(Double.valueOf((objHumanResources.getAmountRequest().doubleValue()
					/ objHumanResources.getAmountFromBudget().doubleValue()) * 100d));
		}

		// Service provision
		CostItemSummaryWrapper objServiceProvision = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.ProvisionOfService });
		objServiceProvision.setName(Utils.getString("Resources", "durCompilationEditCostItem2", null));
		objServiceProvision.setShowOnlyFirstColumn(false);

		objServiceProvision.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objServiceProvision
					.setAmountFromBudget(objServiceProvision.getAmountFromBudget() + phase.getPartProvisionOfService());

		objServiceProvision.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.ProvisionOfService, userId));
		objServiceProvision.setAmountCostCertificate(
				this.GetCostDefinitionTotalCILAmount(CostItemTypes.ProvisionOfService, userId));
		objServiceProvision
				.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.ProvisionOfService, userId));

		if (objServiceProvision.getAmountFromBudget() != 0) {
			objServiceProvision
					.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.ProvisionOfService, userId)
							/ objServiceProvision.getAmountFromBudget() * 100d);
		} else {
			objServiceProvision.setAbsorption(0d);
		}
		objServiceProvision.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.ProvisionOfService, userId));
		objServiceProvision.setPreviousExpenditureRequisted(psTotal);

		// Mission
		CostItemSummaryWrapper objMissions = new CostItemSummaryWrapper(new CostItemTypes[] { CostItemTypes.Missions });
		objMissions.setName(Utils.getString("Resources", "durCompilationEditCostItem3", null));
		objMissions.setShowOnlyFirstColumn(false);

		objMissions.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objMissions.setAmountFromBudget(objMissions.getAmountFromBudget() + phase.getPartMissions());

		objMissions.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.Missions, userId));
		objMissions.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.Missions, userId));
		objMissions.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.Missions, userId));

		if (objMissions.getAmountFromBudget() != 0) {
			objMissions.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.Missions, userId)
					/ objMissions.getAmountFromBudget() * 100d);
		} else {
			objMissions.setAbsorption(0d);
		}
		objMissions.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.Missions, userId));
		objMissions.setPreviousExpenditureRequisted(mTotal);

		// DurablesInfrastructure
		CostItemSummaryWrapper objDurablesInf = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.DurablesInfrastructure });
		objDurablesInf.setName(Utils.getString("Resources", "durCompilationEditCostItem4", null));
		objDurablesInf.setShowOnlyFirstColumn(false);
		objDurablesInf.setAmountFromBudget(0d);
		for (Phase phase : total.getPhaseBudgets())
			objDurablesInf.setAmountFromBudget(objDurablesInf.getAmountFromBudget() + phase.getPartDurables());
		objDurablesInf.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.DurablesInfrastructure, userId));
		objDurablesInf.setAmountCostCertificate(
				this.GetCostDefinitionTotalCILAmount(CostItemTypes.DurablesInfrastructure, userId));
		objDurablesInf
				.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.DurablesInfrastructure, userId));

		if (objDurablesInf.getAmountFromBudget() != 0) {
			objDurablesInf.setAbsorption(
					(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.DurablesInfrastructure, userId))
							/ objDurablesInf.getAmountFromBudget() * 100d);
		} else {
			objDurablesInf.setAbsorption(0d);
		}
		objDurablesInf.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.DurablesInfrastructure, userId));
		objDurablesInf.setPreviousExpenditureRequisted(diTotal);

		// DurableGoods
		CostItemSummaryWrapper objDurableGoods = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.DurableGoods });
		objDurableGoods.setName(Utils.getString("Resources", "durCompilationEditCostItem5", null));
		objDurableGoods.setShowOnlyFirstColumn(false);

		objDurableGoods.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objDurableGoods.setAmountFromBudget(objDurableGoods.getAmountFromBudget() + phase.getPartDurableGoods());
		objDurableGoods.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.DurableGoods, userId));
		objDurableGoods
				.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.DurableGoods, userId));
		objDurableGoods.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.DurableGoods, userId));

		if (objDurableGoods.getAmountFromBudget() != 0) {
			objDurableGoods
					.setAbsorption((this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.DurableGoods, userId))
							/ objDurableGoods.getAmountFromBudget() * 100d);
		} else {
			objDurableGoods.setAbsorption(0d);
		}
		objDurableGoods.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.DurableGoods, userId));
		objDurableGoods.setPreviousExpenditureRequisted(dgTotal);

		// Advertising info public events ()
		CostItemSummaryWrapper objAdvInfoPubEv = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.AdvInfoPubEvents });
		objAdvInfoPubEv.setName(Utils.getString("Resources", "durCompilationEditCostItem6", null));
		objAdvInfoPubEv.setShowOnlyFirstColumn(false);

		objAdvInfoPubEv.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objAdvInfoPubEv
					.setAmountFromBudget(objAdvInfoPubEv.getAmountFromBudget() + phase.getPartAdvInfoPublicEvents());

		objAdvInfoPubEv.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.AdvInfoPubEvents, userId));
		objAdvInfoPubEv
				.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.AdvInfoPubEvents, userId));
		objAdvInfoPubEv.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.AdvInfoPubEvents, userId));

		if (objAdvInfoPubEv.getAmountFromBudget() != 0) {
			objAdvInfoPubEv
					.setAbsorption((this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.AdvInfoPubEvents, userId))
							/ objAdvInfoPubEv.getAmountFromBudget() * 100d);
		} else {
			objAdvInfoPubEv.setAbsorption(0d);
		}
		objAdvInfoPubEv.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.AdvInfoPubEvents, userId));
		objAdvInfoPubEv.setPreviousExpenditureRequisted(aipeTotal);

		// Advertising info products()
		CostItemSummaryWrapper objAdvInfoProducts = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.AdvInfoProducts });
		objAdvInfoProducts.setName(Utils.getString("Resources", "durCompilationEditCostItem7", null));
		objAdvInfoProducts.setShowOnlyFirstColumn(false);

		objAdvInfoProducts.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objAdvInfoProducts
					.setAmountFromBudget(objAdvInfoProducts.getAmountFromBudget() + phase.getPartAdvInfoProducts());

		objAdvInfoProducts.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.AdvInfoProducts, userId));
		objAdvInfoProducts
				.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.AdvInfoProducts, userId));
		objAdvInfoProducts
				.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.AdvInfoProducts, userId));

		if (objAdvInfoProducts.getAmountFromBudget() != 0) {
			objAdvInfoProducts
					.setAbsorption((this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.AdvInfoProducts, userId))
							/ objAdvInfoProducts.getAmountFromBudget() * 100d);
		} else {
			objAdvInfoProducts.setAbsorption(0d);
		}
		objAdvInfoProducts.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.AdvInfoProducts, userId));
		objAdvInfoProducts.setPreviousExpenditureRequisted(aipTotal);

		// Overheads
		CostItemSummaryWrapper objOverheads = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.Overheads });
		objOverheads.setName(Utils.getString("Resources", "durCompilationEditCostItem8", null));
		objOverheads.setShowOnlyFirstColumn(false);
		objOverheads.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objOverheads.setAmountFromBudget(objOverheads.getAmountFromBudget() + phase.getPartOverheads());

		objOverheads.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.Overheads, userId));
		objOverheads.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.Overheads, userId));
		objOverheads.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.Overheads, userId));

		if (objOverheads.getAmountFromBudget() != 0) {
			objOverheads.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.Overheads, userId)
					/ objOverheads.getAmountFromBudget() * 100d);
		} else {
			objOverheads.setAbsorption(0d);
		}
		objOverheads.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.Overheads, userId));
		objOverheads.setPreviousExpenditureRequisted(ohTotal);

		// General Costs
		CostItemSummaryWrapper objGenCosts = new CostItemSummaryWrapper(
				new CostItemTypes[] { CostItemTypes.GeneralCosts });
		objGenCosts.setName(Utils.getString("Resources", "durCompilationEditCostItem9", null));
		objGenCosts.setShowOnlyFirstColumn(false);
		objGenCosts.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objGenCosts.setAmountFromBudget(objGenCosts.getAmountFromBudget() + phase.getPartGeneralCosts());

		objGenCosts.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.GeneralCosts, userId));
		objGenCosts.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.GeneralCosts, userId));
		objGenCosts.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.GeneralCosts, userId));

		if (objGenCosts.getAmountFromBudget() != 0) {
			objGenCosts.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.GeneralCosts, userId)
					/ objGenCosts.getAmountFromBudget() * 100d);
		} else {
			objGenCosts.setAbsorption(0d);
		}
		objGenCosts.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.GeneralCosts, userId));
		objGenCosts.setPreviousExpenditureRequisted(gcTotal);

		// Other
		CostItemSummaryWrapper objOther = new CostItemSummaryWrapper(new CostItemTypes[] { CostItemTypes.Other });
		objOther.setName(Utils.getString("Resources", "durCompilationEditCostItem10", null));
		objOther.setShowOnlyFirstColumn(false);
		objOther.setAmountFromBudget(0d);

		for (Phase phase : total.getPhaseBudgets())
			objOther.setAmountFromBudget(objOther.getAmountFromBudget() + phase.getPartOther());

		objOther.setAmountByCf(this.GetCostDefinitionTotalAmount(CostItemTypes.Other, userId));
		objOther.setAmountCostCertificate(this.GetCostDefinitionTotalCILAmount(CostItemTypes.Other, userId));
		objOther.setAmountRequest(this.GetCostDefinitionTotalRequestAGU(CostItemTypes.Other, userId));

		if (objOther.getAmountFromBudget() != 0) {
			objOther.setAbsorption(this.GetCostDefinitionLastCertifiedAmount(CostItemTypes.Other, userId)
					/ objOther.getAmountFromBudget() * 100d);
		} else {
			objOther.setAbsorption(0d);
		}
		objOther.setReinstated(this.GetCostDefinitionReinstated(CostItemTypes.Other, userId));
		objOther.setPreviousExpenditureRequisted(otTotal);

		this.listSummary.add(objHumanResources);
		this.listSummary.add(objServiceProvision);
		this.listSummary.add(objMissions);
		this.listSummary.add(objDurablesInf);
		this.listSummary.add(objDurableGoods);
		this.listSummary.add(objAdvInfoPubEv);
		this.listSummary.add(objAdvInfoProducts);
		this.listSummary.add(objOverheads);
		this.listSummary.add(objGenCosts);
		this.listSummary.add(objOther);

		calcCostItemAdditionalAmounts(userId);

		// Total entity

		CostItemSummaryWrapper entityTotal = this.prepareTotalEntity();
		this.setTotalWrapper(entityTotal);
		entityTotal.setName(Utils.getString("Resources", "total", null));
		entityTotal.setShowOnlyFirstColumn(false);
		entityTotal.setPreviousExpenditureRequisted(
				hrTotal + psTotal + mTotal + diTotal + dgTotal + aipeTotal + aipTotal + ohTotal + gcTotal + otTotal);
		this.listSummary.add(entityTotal);

		// Other fields

		Double previsionExCnPrivateOld = 0d;
		Double previsionExCnPublicOld = 0d;
		Double previsionExAdditionalFinansingOld = 0d;

		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gbudget = null;
		CFPartnerAnagraphs ca = null;

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cost : dc.getCostDefinitions()) {
					boolean compute = true;
					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
						if (cd.getId().equals(cost.getId())) {
							compute = false;
						}

					}
					if (compute) {
						compute = false;
						ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
						gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
								.get(0);

						if (gbudget != null) {
							if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
								if (((gbudget.getFesr() / (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate()
										- gbudget.getNetRevenue())) * 100) == 50) {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
								} else {
									previsionExCnPrivateOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
								}

							} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
								previsionExCnPublicOld += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							}
						}

						previsionExAdditionalFinansingOld += GetLastAdditionaFinansingValidated(cost);
					}
				}
			}
		}

		Double amountByCfPublic = 0d;
		Double amountByCfPrivate = 0d;
		Double amountCostCertificatePublic = 0d;
		Double amountCostCertificatePrivate = 0d;
		Double amountRequestPublic = 0d;
		Double amountRequestPrivate = 0d;
		Double stcCertificatedPublic = 0d;
		Double stcCertificatedPrivate = 0d;
		Double acuCertificatedPublic = 0d;
		Double acuCertificatedPrivate = 0d;
		Double aguCertificatedPublic = 0d;
		Double aguCertificatedPrivate = 0d;
		Double previsionExCnPrivate = 0d;
		Double previsionExCnPublic = 0d;
		Double previsionExAdditionalFinansing = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());
					gbudget = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(), ca.getId())
							.get(0);

					if (gbudget != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gbudget.getFesr()
								/ (gbudget.getBudgetTotal() - gbudget.getQuotaPrivate() - gbudget.getNetRevenue()))
								* 100) == 50) {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.5d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.5d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.5d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.5d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.5d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.5d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.5d);
						} else {
							previsionExCnPrivate += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
							acuCertificatedPrivate += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
							aguCertificatedPrivate += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
							stcCertificatedPrivate += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
							amountByCfPrivate += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
							amountCostCertificatePrivate += (this.getValidatedCostCertificateAmountSubAdditional(cost)
									* 0.15d);
							amountRequestPrivate += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						previsionExCnPublic += (this.getCostSubAdditionalFinansing(cost) * 0.15d);
						acuCertificatedPublic += (this.getValidatedAcuAmountSubAdditional(cost) * 0.15d);
						aguCertificatedPublic += (this.getValidatedAguAmountSubAdditional(cost) * 0.15d);
						stcCertificatedPublic += (this.getValidatedStcAmountSubAdditional(cost) * 0.15d);
						amountByCfPublic += (this.getValidatedByCfAmountSubAdditional(cost) * 0.15d);
						amountCostCertificatePublic += (this.getValidatedCostCertificateAmountSubAdditional(cost)
								* 0.15d);
						amountRequestPublic += (this.getValidatedRequestAmountSubAdditional(cost) * 0.15d);
					}
					previsionExAdditionalFinansing += GetLastAdditionaFinansingValidated(cost);
				}
			}

		}

		BudgetInputSourceDivided budget = BudgetInputSourceDividedBean.GetByProject(this.getCurrentProjectId()).get(0);
		cfSummaryWrapper.setAmountFromBudget(budget.getCnPrivate());
		cfSummaryWrapper.setName(Utils.getString("Resources", "durCompilationTotalEntity", null));
		cfSummaryWrapper
				.setPreviousExpenditureRequisted(previsionExAdditionalFinansingOld + previsionExAdditionalFinansing);
		CostItemSummaryWrapper objFesrTotal = new CostItemSummaryWrapper();
		objFesrTotal.setName(Utils.getString("Resources", "durCompilationEditFesrTotal", null));
		objFesrTotal.setShowOnlyFirstColumn(true);
		objFesrTotal.setAmountFromBudget(budget.getFesr());
		objFesrTotal.setAmountByCf(this.GetFesrTotalAmountByCf());
		objFesrTotal.setAmountCostCertificate(this.GetFesrTotalAmountCostCertificate());
		objFesrTotal.setAmountRequest(this.GetFesrTotalAmountRequest());
		objFesrTotal
				.setStcCertificated((entityTotal.getStcCertificated() == null || entityTotal.getStcCertificated() == 0)
						? 0d : this.GetFesrTotalAmountStcCertificated());
		objFesrTotal
				.setAcuCertificated((entityTotal.getAcuCertificated() == null || entityTotal.getAcuCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAcuCertificated());
		objFesrTotal
				.setAguCertificated((entityTotal.getAguCertificated() == null || entityTotal.getAguCertificated() == 0)
						? 0d : this.GetFesrTotalAmountAguCertificated());

		objFesrTotal.setSuspended((entityTotal.getSuspended() - cfSummaryWrapper.getSuspended()) * 0.75d);
		objFesrTotal.setRectified((entityTotal.getRectified() - cfSummaryWrapper.getRectified()) * 0.75d);
		objFesrTotal.setReinstated((entityTotal.getReinstated() - cfSummaryWrapper.getReinstated()) * 0.75d);

		objFesrTotal.setPreviousExpenditureRequisted(this.calculateFesrAmount2() + this.calculateFesrAmountOldDur());

		CostItemSummaryWrapper objCnTotal = new CostItemSummaryWrapper();
		objCnTotal.setName(Utils.getString("Resources", "durCompilationEditCnTotal", null));
		objCnTotal.setShowOnlyFirstColumn(true);
		objCnTotal.setAmountFromBudget(budget.getCnPublic() + budget.getCnPublicOther());

		objCnTotal.setAmountByCf(amountByCfPrivate + amountByCfPublic);
		objCnTotal.setAmountCostCertificate(amountCostCertificatePrivate + amountCostCertificatePublic);
		objCnTotal.setAmountRequest(amountRequestPrivate + amountRequestPublic);
		objCnTotal.setStcCertificated(stcCertificatedPrivate + stcCertificatedPublic);
		objCnTotal.setAguCertificated(aguCertificatedPrivate + aguCertificatedPublic);
		objCnTotal.setAcuCertificated(acuCertificatedPrivate + acuCertificatedPublic);
		objCnTotal.setPreviousExpenditureRequisted(
				previsionExCnPublic + previsionExCnPrivate + previsionExCnPublicOld + previsionExCnPrivateOld);

		List<GeneralBudgets> listGB = BeansFactory.GeneralBudgets()
				.GetItemsForProject(Long.valueOf(this.getCurrentProjectId()));
		CostItemSummaryWrapper objCnItTotal = this.prepareCnForCountry(CountryTypes.Italy);
		CostItemSummaryWrapper objCnFrTotal = this.prepareCnForCountry(CountryTypes.France);

		CostItemSummaryWrapper cnItTotal = this.prepareTotalCnForCountry(CountryTypes.Italy);
		CostItemSummaryWrapper cnFrTotal = this.prepareTotalCnForCountry(CountryTypes.France);

		objCnItTotal.setName(Utils.getString("Resources", "durCompilationEditCnItTotal", null));
		objCnItTotal.setShowOnlyFirstColumn(true);
		objCnItTotal.setAmountFromBudget(0d);
		objCnFrTotal.setName(Utils.getString("Resources", "durCompilationEditCnFrTotal", null));
		objCnFrTotal.setShowOnlyFirstColumn(true);
		objCnFrTotal.setAmountFromBudget(0d);

		cnItTotal.setAmountFromBudget(0d);
		cnFrTotal.setAmountFromBudget(0d);

		for (GeneralBudgets gb : listGB) {
			if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.Italy.getCountry())) {
				if (gb.getCnPublic() != null) {
					objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getCnPublic());
					cnItTotal.setAmountFromBudget(cnItTotal.getAmountFromBudget() + gb.getCnPublic());
				}
				if (gb.getQuotaPrivate() != null) {
					objCnItTotal.setAmountFromBudget(objCnItTotal.getAmountFromBudget() + gb.getQuotaPrivate());
					cnItTotal.setAmountFromBudget(cnItTotal.getAmountFromBudget() + gb.getQuotaPrivate());
				}

			} else if (gb.getCfPartnerAnagraph().getCountry().getCode().equals(CountryTypes.France.getCountry())) {
				if (gb.getCnPublic() != null) {
					objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getCnPublic());
					cnFrTotal.setAmountFromBudget(cnFrTotal.getAmountFromBudget() + gb.getCnPublic());
				}
				if (gb.getQuotaPrivate() != null) {
					objCnFrTotal.setAmountFromBudget(objCnFrTotal.getAmountFromBudget() + gb.getQuotaPrivate());
					cnFrTotal.setAmountFromBudget(cnFrTotal.getAmountFromBudget() + gb.getQuotaPrivate());
				}
			}
		}

		CostItemSummaryWrapper objCnPublic = new CostItemSummaryWrapper();
		objCnPublic.setName(Utils.getString("Resources", "durCompilationEditPublicCN", null));
		objCnPublic.setShowOnlyFirstColumn(true);
		objCnPublic.setAmountFromBudget(budget.getCnPublic());
		objCnPublic.setAcuCertificated(acuCertificatedPublic);
		objCnPublic.setAguCertificated(aguCertificatedPublic);
		objCnPublic.setStcCertificated(stcCertificatedPublic);
		objCnPublic.setAmountByCf(amountByCfPublic);
		objCnPublic.setAmountCostCertificate(amountCostCertificatePublic);
		objCnPublic.setAmountRequest(amountRequestPublic);
		objCnPublic.setPreviousExpenditureRequisted(previsionExCnPublic + previsionExCnPublicOld);

		CostItemSummaryWrapper objCnPrivate = new CostItemSummaryWrapper();
		objCnPrivate.setName(Utils.getString("Resources", "durCompilationEditPrivateCN", null));
		objCnPrivate.setShowOnlyFirstColumn(true);
		objCnPrivate.setAmountFromBudget(budget.getCnPublicOther());
		objCnPrivate.setAcuCertificated(acuCertificatedPrivate);
		objCnPrivate.setAguCertificated(aguCertificatedPrivate);
		objCnPrivate.setStcCertificated(stcCertificatedPrivate);
		objCnPrivate.setAmountByCf(amountByCfPrivate);
		objCnPrivate.setAmountCostCertificate(amountCostCertificatePrivate);
		objCnPrivate.setAmountRequest(amountRequestPrivate);
		objCnPrivate.setPreviousExpenditureRequisted(previsionExCnPrivate + previsionExCnPrivateOld);

		// added for bug 0027352
		roundDurParametersInGrid(objCnItTotal);
		roundDurParametersInGrid(objCnFrTotal);
		//

		this.listSummary.add(objFesrTotal);
		this.listSummary.add(objCnTotal);
		this.listSummary.add(objCnPublic);
		this.listSummary.add(objCnPrivate);
		// this.listSummary.add(objCnItTotal);
		// this.listSummary.add(objCnFrTotal);
		this.listSummary.add(cfSummaryWrapper);
		// added for bug 0027341
		// set the last not 0.0 column
		updateTotalValues(entityTotal, objFesrTotal, objCnItTotal, objCnFrTotal);
		setCnItTotalWrapper(cnItTotal);
		setCnFrTotalWrapper(cnFrTotal);
		recalculateTotalReimbursement();
	}

	private Double GetLastAdditionaFinansingValidated(CostDefinitions cost) {
		if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()) {
			if (cost.getACUSertified() && cost.getAcuCheckAdditionalFinansingAmount() != null) {
				return cost.getAcuCheckAdditionalFinansingAmount();
			} else if (cost.getAGUSertified() && cost.getAguCheckAdditionalFinansingAmount() != null) {
				return cost.getAguCheckAdditionalFinansingAmount();
			} else if (cost.getSTCSertified() && cost.getStcCheckAdditionalFinansingAmount() != null) {
				return cost.getStcCheckAdditionalFinansingAmount();
			} else if (cost.getCfCheckAdditionalFinansingAmount() != null) {
				return cost.getCfCheckAdditionalFinansingAmount();
			} else if (cost.getCilCheckAdditionalFinansingAmount() != null) {
				return cost.getCilCheckAdditionalFinansingAmount();
			}
		}
		return 0d;
	}

	private Double GetFesrTotalAmountAcuCertificated() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = cost.getAcuCertification() == null ? 0d : cost.getAcuCertification().doubleValue();// this.getCostSubAdditionalFinansing(cost);
					if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
							&& cost.getAcuCheckAdditionalFinansingAmount() != null) {
						amount -= cost.getAcuCheckAdditionalFinansingAmount();
					}
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double GetFesrTotalAmountAguCertificated() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = cost.getAguCertification() == null ? 0d : cost.getAguCertification().doubleValue();// this.getCostSubAdditionalFinansing(cost);
					if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
							&& cost.getAguCheckAdditionalFinansingAmount() != null) {
						amount -= cost.getAguCheckAdditionalFinansingAmount();
					}
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (gb != null) {
					}
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double GetFesrTotalAmountStcCertificated() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = cost.getStcCertification() == null ? 0d : cost.getStcCertification().doubleValue();// this.getCostSubAdditionalFinansing(cost);
					if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
							&& cost.getStcCheckAdditionalFinansingAmount() != null) {
						amount -= cost.getStcCheckAdditionalFinansingAmount();
					}
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double GetFesrTotalAmountRequest() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					boolean added = false;
					if (cost.getCreatedByPartner() == true) {
						if (cost.getCfCheck() != null) {
							amount = cost.getCfCheck() == null ? 0d : cost.getCfCheck().doubleValue();
							added = true;
							if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
									&& cost.getCfCheckAdditionalFinansingAmount() != null) {
								amount -= cost.getCfCheckAdditionalFinansingAmount();
							}
						}
					}
					if (!added) {
						if (cost.getCilCheck() != null) {
							amount = cost.getCilCheck() == null ? 0d : cost.getCilCheck().doubleValue();
							if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
									&& cost.getCilCheckAdditionalFinansingAmount() != null) {
								amount -= cost.getCilCheckAdditionalFinansingAmount();
							}
						}
					}

					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double GetFesrTotalAmountCostCertificate() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = cost.getCilCheck() == null ? 0d : cost.getCilCheck().doubleValue();// this.getCostSubAdditionalFinansing(cost);
					if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
							&& cost.getCilCheckAdditionalFinansingAmount() != null) {
						amount -= cost.getCilCheckAdditionalFinansingAmount();
					}
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double GetFesrTotalAmountByCf() throws PersistenceBeanException {
		double amountTotal = 0d;
		double amount = 0d;
		List<CostDefinitions> cds = this.getListCostDefinitionsWeb();
		GeneralBudgets gb = null;
		CFPartnerAnagraphs ca = null;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cost : cds) {
				if (cost.getSelected()) {
					amount = cost.getAmountReport() == null ? 0d : cost.getAmountReport().doubleValue();// this.getCostSubAdditionalFinansing(cost);
					if (cost.getAdditionalFinansing() != null && cost.getAdditionalFinansing()
							&& cost.getAdditionalFinansingAmount() != null) {
						amount -= cost.getAdditionalFinansingAmount();
					}
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cost.getUser().getId());

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
							ca.getId());
					gb = gbs.get(gbs.size() - 1);

					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
								* 100) == 50) {
							amountTotal += (amount * 0.5);
						} else {
							amountTotal += (amount * 0.85);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						amountTotal += (amount * 0.85);
					}
				}
			}

		}
		return amountTotal;
	}

	private Double CnPrivateValidate(CostDefinitions cost) {
		if (cost.getACUSertified() && cost.getAcuCheckPrivateAmount() != null) {
			return cost.getAcuCheckPrivateAmount();
		} else if (cost.getAGUSertified() && cost.getAguCheckPrivateAmount() != null) {
			return cost.getAguCheckPrivateAmount();
		} else if (cost.getSTCSertified() && cost.getStcCheckPrivateAmount() != null) {
			return cost.getStcCheckPrivateAmount();
		} else if (cost.getCfCheckPrivateAmount() != null) {
			return cost.getCfCheckPrivateAmount();
		} else if (cost.getCilCheckPrivateAmount() != null) {
			return cost.getCilCheckPrivateAmount();
		}
		return 0d;
	}

	private Double CnPublicValidate(CostDefinitions cost) {
		if (cost.getACUSertified() && cost.getAcuCheckPublicAmount() != null) {
			return cost.getAcuCheckPublicAmount();
		} else if (cost.getAGUSertified() && cost.getAguCheckPublicAmount() != null) {
			return cost.getAguCheckPublicAmount();
		} else if (cost.getSTCSertified() && cost.getStcCheckPublicAmount() != null) {
			return cost.getStcCheckPublicAmount();
		} else if (cost.getCfCheckPublicAmount() != null) {
			return cost.getCfCheckPublicAmount();
		} else if (cost.getCilCheckPublicAmount() != null) {
			return cost.getCilCheckPublicAmount();
		}
		return 0d;
	}

	private Double CnPrivateAmountRequest() {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected()) {
					boolean added = false;
					if (cd.getCreatedByPartner() == true) {
						if (cd.getCfCheckPrivateAmount() != null) {
							total += cd.getCfCheckPrivateAmount();
							added = true;
						}
					}

					if (!added) {
						if (cd.getCilCheckPrivateAmount() != null) {
							total += cd.getCilCheckPrivateAmount();
						}
					}

				}
			}
		}
		return total;
	}

	private Double CnPublicAmountRequest() {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected()) {
					boolean added = false;
					if (cd.getCreatedByPartner() == true) {
						if (cd.getCfCheckPublicAmount() != null) {
							total += cd.getCfCheckPublicAmount();
							added = true;
						}
					}

					if (!added) {
						if (cd.getCilCheckPublicAmount() != null) {
							total += cd.getCilCheckPublicAmount();
						}
					}

				}
			}
		}
		return total;
	}

	private Double CnPrivateAmountCostCertificate() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCilCheckPrivateAmount() != null) {
					total += cd.getCilCheckPrivateAmount();
				}
			}
		}
		return total;
	}

	private Double CnPublicAmountCostCertificate() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCilCheckPublicAmount() != null) {
					total += cd.getCilCheckPublicAmount();
				}
			}
		}
		return total;
	}

	private Double CnPrivateAguCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getAGUSertified() && cd.getAguCheckPrivateAmount() != null) {
					total += cd.getAguCheckPrivateAmount();
				}
			}
		}
		return total;
	}

	private Double CnPublicAguCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getAGUSertified() && cd.getAguCheckPublicAmount() != null) {
					total += cd.getAguCheckPublicAmount();
				}
			}
		}
		return total;
	}

	private Double CnPrivateAcuCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getACUSertified() && cd.getAcuCheckPrivateAmount() != null) {
					total += cd.getAcuCheckPrivateAmount();
				}
			}
		}
		return total;
	}

	private Double CnPublicAcuCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getACUSertified() && cd.getAcuCheckPublicAmount() != null) {
					total += cd.getAcuCheckPublicAmount();
				}
			}
		}
		return total;
	}

	private Double CnPrivateStcCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getSTCSertified() && cd.getStcCheckPrivateAmount() != null) {
					total += cd.getStcCheckPrivateAmount();
				}
			}
		}
		return total;
	}

	private Double CnPublicStcCertified() {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getSTCSertified() && cd.getStcCheckPublicAmount() != null) {
					total += cd.getStcCheckPublicAmount();
				}
			}
		}
		return total;
	}

	private Double CnPrivateAmountByCf() {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected()) {
					total += cd.getPrivateAmountReport() == null ? 0d : cd.getPrivateAmountReport().doubleValue();
				}
			}
		}

		return total;
	}

	private Double CnPublicAmountByCf() {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected()) {
					total += cd.getPublicAmountReport() == null ? 0d : cd.getPublicAmountReport().doubleValue();
				}
			}
		}

		return total;
	}

	private void recalculateTotalReimbursement() {
		Double sum = (this.getFesrReimbursementAmount2() == null ? 0d
				: this.getFesrReimbursementAmount2().doubleValue())
				+ (this.getItCnReimbursement2() == null ? 0d : this.getItCnReimbursement2().doubleValue());
		this.setTotalReimbursement(sum);
	}

	private void updateTotalValues(CostItemSummaryWrapper entityTotal, CostItemSummaryWrapper objFesrTotal,
			CostItemSummaryWrapper objCnItTotal, CostItemSummaryWrapper objCnFrTotal) {
		if (entityTotal.getAcuCertificated() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAcuCertificated()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAcuCertificated()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAcuCertificated()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAcuCertificated()));
		} else if (entityTotal.getAguCertificated() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAguCertificated()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAguCertificated()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAguCertificated()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAguCertificated()));
		} else if (entityTotal.getStcCertificated() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getStcCertificated()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getStcCertificated()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getStcCertificated()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getStcCertificated()));
		} else if (entityTotal.getAmountRequest() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAmountRequest()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAmountRequest()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountRequest()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountRequest()));
		} else if (entityTotal.getAmountCostCertificate() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAmountCostCertificate()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAmountCostCertificate()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountCostCertificate()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountCostCertificate()));
		} else if (entityTotal.getAmountByCf() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAmountByCf()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAmountByCf()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountByCf()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountByCf()));
		} else if (entityTotal.getAmountFromBudget() != 0d) {
			this.setTotalAmountDur(NumberHelper.Round(entityTotal.getAmountFromBudget()));
			this.setFesrReimbursementAmount(NumberHelper.Round(objFesrTotal.getAmountFromBudget()));
			this.setItCnReimbursement(NumberHelper.Round(objCnItTotal.getAmountFromBudget()));
			this.setFrCnReimbursement(NumberHelper.Round(objCnFrTotal.getAmountFromBudget()));
		}
		recalculateTotalReimbursement();
	}

	private void roundDurParametersInGrid(CostItemSummaryWrapper wr) {
		wr.setAguCertificated(NumberHelper.Round(wr.getAguCertificated()));
		wr.setAcuCertificated(NumberHelper.Round(wr.getAcuCertificated()));
		wr.setStcCertificated(NumberHelper.Round(wr.getStcCertificated()));
		wr.setAmountByCf(NumberHelper.Round(wr.getAmountByCf()));
		wr.setAmountRequest(NumberHelper.Round(wr.getAmountRequest()));
		wr.setAmountCostCertificate(NumberHelper.Round(wr.getAmountCostCertificate()));
		wr.setAmountFromBudget(NumberHelper.Round(wr.getAmountFromBudget()));
		wr.setRectified(NumberHelper.Round(wr.getRectified()));
		wr.setSuspended(NumberHelper.Round(wr.getSuspended()));
	}

	/**
	 * @param france
	 * @return
	 */
	private CostItemSummaryWrapper prepareCnForCountry(CountryTypes type) {
		CostItemSummaryWrapper costItemSummary = new CostItemSummaryWrapper(null);

		if (type != null && type.getCountry() != null) {

			if (this.getListCostDefinitionsWeb() != null) {
				for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
					if (cd.getSelected()) {
						boolean calc = false;
						if (cd.getUser() == null) {
							if (type.equals(CountryTypes.Italy)) {
								calc = true;
							}
						} else {
							CFPartnerAnagraphs partner = null;
							try {
								partner = BeansFactory.CFPartnerAnagraphs().GetByUser(cd.getUser().getId());
							} catch (PersistenceBeanException e) {
								log.error(e);
							}

							if (partner != null && partner.getCountry() != null
									&& partner.getCountry().getCode() != null) {
								if (partner.getCountry().getCode().equals(type.getCountry())) {
									calc = true;
								}
							} else if (type.equals(CountryTypes.Italy)) {
								calc = true;
							}
						}

						if (calc) {
							if (cd.getAmountReport() != null) {
								costItemSummary.setAmountByCf(costItemSummary.getAmountByCf() + cd.getAmountReport());
							}

							boolean amountRequestAdded = false;
							if (cd.getCfCheck() != null) {
								costItemSummary.setAmountRequest(costItemSummary.getAmountRequest() + cd.getCfCheck());
								amountRequestAdded = true;
							}

							if (cd.getCilCheck() != null) {
								costItemSummary.setAmountCostCertificate(
										costItemSummary.getAmountCostCertificate() + cd.getCilCheck());
								if (!amountRequestAdded) {
									costItemSummary
											.setAmountRequest(costItemSummary.getAmountRequest() + cd.getCilCheck());
								}
							}

							if (cd.getStcCertification() != null && cd.getSTCSertified()) {
								costItemSummary.setStcCertificated(
										costItemSummary.getStcCertificated() + cd.getStcCertification());
							}

							if (cd.getAcuCertification() != null && cd.getACUSertified()) {
								costItemSummary.setAcuCertificated(
										costItemSummary.getAcuCertificated() + cd.getAcuCertification());
							}

							if (cd.getAguCertification() != null && cd.getAGUSertified()) {
								costItemSummary.setAguCertificated(
										costItemSummary.getAguCertificated() + cd.getAguCertification());
							}

							if (cd.getSuspendedByACU() != null && cd.getSuspendedByACU()
									&& cd.getValueSuspendACU() != null) {
								costItemSummary.setSuspended(costItemSummary.getSuspended() + cd.getValueSuspendACU());
							}

							if (cd.getRectificationAmount() != null) {
								costItemSummary
										.setRectified(costItemSummary.getRectified() + cd.getRectificationAmount());
							}
						}
					}
				}

				costItemSummary.setAmountByCf(costItemSummary.getAmountByCf() * 0.25d);
				costItemSummary.setAmountCostCertificate(costItemSummary.getAmountCostCertificate() * 0.25d);
				costItemSummary.setAmountRequest(costItemSummary.getAmountRequest() * 0.25d);
				costItemSummary.setReinstated((costItemSummary.getReinstated() * 0.25d));
				costItemSummary.setStcCertificated(costItemSummary.getStcCertificated() * 0.25d);
				costItemSummary.setAguCertificated(costItemSummary.getAguCertificated() * 0.25d);
				costItemSummary.setAcuCertificated(costItemSummary.getAcuCertificated() * 0.25d);
				costItemSummary.setSuspended(costItemSummary.getSuspended() * 0.25d);
				costItemSummary.setRectified(costItemSummary.getRectified() * 0.25d);
				costItemSummary.setAbsorption(null);
			}
		}

		return costItemSummary;
	}

	private CostItemSummaryWrapper prepareTotalCnForCountry(CountryTypes type) {
		CostItemSummaryWrapper costItemSummary = new CostItemSummaryWrapper(null);

		if (type != null && type.getCountry() != null) {

			if (this.getListCostDefinitionsWeb() != null) {
				for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
					if (cd.getSelected() && !Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						boolean calc = false;
						if (cd.getUser() == null) {
							if (type.equals(CountryTypes.Italy)) {
								calc = true;
							}
						} else {
							CFPartnerAnagraphs partner = null;
							try {
								partner = BeansFactory.CFPartnerAnagraphs().GetByUser(cd.getUser().getId());
							} catch (PersistenceBeanException e) {
								log.error(e);
							}

							if (partner != null && partner.getCountry() != null
									&& partner.getCountry().getCode() != null) {
								if (partner.getCountry().getCode().equals(type.getCountry())) {
									calc = true;
								}
							} else if (type.equals(CountryTypes.Italy)) {
								calc = true;
							}
						}

						if (calc) {
							if (cd.getAmountReport() != null) {
								costItemSummary.setAmountByCf(costItemSummary.getAmountByCf() + cd.getAmountReport());
							}

							boolean amountRequestAdded = false;
							if (cd.getCfCheck() != null) {
								costItemSummary.setAmountRequest(costItemSummary.getAmountRequest() + cd.getCfCheck());
								amountRequestAdded = true;
							}

							if (cd.getCilCheck() != null) {
								costItemSummary.setAmountCostCertificate(
										costItemSummary.getAmountCostCertificate() + cd.getCilCheck());
								if (!amountRequestAdded) {
									costItemSummary
											.setAmountRequest(costItemSummary.getAmountRequest() + cd.getCilCheck());
								}
							}

							if (cd.getStcCertification() != null && cd.getSTCSertified()) {
								costItemSummary.setStcCertificated(
										costItemSummary.getStcCertificated() + cd.getStcCertification());
							}

							if (cd.getAcuCertification() != null && cd.getACUSertified()) {
								costItemSummary.setAcuCertificated(
										costItemSummary.getAcuCertificated() + cd.getAcuCertification());
							}

							if (cd.getAguCertification() != null && cd.getAGUSertified()) {
								costItemSummary.setAguCertificated(
										costItemSummary.getAguCertificated() + cd.getAguCertification());
							}

							if (cd.getSuspendedByACU() != null && cd.getSuspendedByACU()
									&& cd.getValueSuspendACU() != null) {
								costItemSummary.setSuspended(costItemSummary.getSuspended() + cd.getValueSuspendACU());
							}

							if (cd.getRectificationAmount() != null) {
								costItemSummary
										.setRectified(costItemSummary.getRectified() + cd.getRectificationAmount());
							}
						}
					}
				}

				costItemSummary.setAmountByCf(costItemSummary.getAmountByCf() * 0.15d);
				costItemSummary.setAmountCostCertificate(costItemSummary.getAmountCostCertificate() * 0.15d);
				costItemSummary.setAmountRequest(costItemSummary.getAmountRequest() * 0.15d);
				costItemSummary.setReinstated((costItemSummary.getReinstated() * 0.15d));
				costItemSummary.setStcCertificated(costItemSummary.getStcCertificated() * 0.15d);
				costItemSummary.setAguCertificated(costItemSummary.getAguCertificated() * 0.15d);
				costItemSummary.setAcuCertificated(costItemSummary.getAcuCertificated() * 0.15d);
				costItemSummary.setSuspended(costItemSummary.getSuspended() * 0.15d);
				costItemSummary.setRectified(costItemSummary.getRectified() * 0.15d);
				costItemSummary.setAbsorption(null);
			}
		}

		return costItemSummary;
	}

	private Double GetCostDefinitionTotalAmount(CostItemTypes type, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem() != null && cd.getCostItem().getId().equals(type.getValue())
						&& (user == null || cd.getUser().getId().equals(user))) {
					total += cd.getAmountReport() == null ? 0d : cd.getAmountReport().doubleValue();

					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						cfSummaryWrapper.setAmountByCf((cfSummaryWrapper.getAmountByCf() == null ? 0d
								: cfSummaryWrapper.getAmountByCf().doubleValue())
								+ (cd.getAdditionalFinansingAmount() == null ? 0d
										: cd.getAdditionalFinansingAmount().doubleValue()));
					}
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionTotalAmountForPhase(Long phase, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem() != null && cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user)) && cd.getAmountReport() != null) {
					total += cd.getAmountReport();

					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						// cfSummaryWrapperWp.setAmountByCf(cfSummaryWrapperWp.getAmountByCf()
						// + cd.getAmountReport());
						cfSummaryWrapperWp.setAmountByCf(
								cfSummaryWrapperWp.getAmountByCf() + (cd.getAdditionalFinansingAmount() == null ? 0d
										: cd.getAdditionalFinansingAmount().doubleValue()));
					}
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionTotalAmountForPhaseOldDDR(Long phase, Long user)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);
		Double total = 0d;
		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cd : dc.getCostDefinitions()) {
					if (cd.getCostDefinitionPhase() != null && cd.getCostDefinitionPhase().getId().equals(phase)
							&& (user == null || cd.getUser().getId().equals(user)) && cd.getAmountReport() != null) {
						total += this.getValidateAmount(cd);
					}
				}
			}
		}
		if (this.getDurCompilation() != null && this.getDurCompilation().getId() == null
				&& this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostDefinitionPhase() != null
						&& cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user)) && cd.getAmountReport() != null) {
					total += this.getValidateAmount(cd);
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionTotalAmountForPartner(Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getUser().getId().equals(user)) {
					total += cd.getAmountReport();
					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						// cfSummaryWrapperPartner
						// .setAmountByCf(cfSummaryWrapperPartner.getAmountByCf()
						// + cd.getAmountReport());
						cfSummaryWrapperPartner.setAmountByCf(
								cfSummaryWrapperPartner.getAmountByCf() + (cd.getAdditionalFinansingAmount() == null
										? 0d : cd.getAdditionalFinansingAmount().doubleValue()));
					}
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionReinstated(CostItemTypes type, Long user) {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem() != null && cd.getCostItem().getId().equals(type.getValue())
						&& (user == null || cd.getUser().getId().equals(user)) && cd.getAmountToOut() != null) {
					total += cd.getAmountToOut();
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionReinstatedForPhase(Long phase, Long user) {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostDefinitionPhase() != null
						&& cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user)) && cd.getAmountToOut() != null) {
					total += cd.getAmountToOut();
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionReinstatedForPartner(Long user) {
		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getUser().getId().equals(user) && cd.getAmountToOut() != null) {
					total += cd.getAmountToOut();
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionLastCertifiedAmount(CostItemTypes type, Long user) {

		Double total = 0d;
		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem().getId().equals(type.getValue())
						&& (user == null || cd.getUser().getId().equals(user))) {
					total += cd.getLastCertifiedAmount();
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionLastCertifiedAmountForPhase(Long phase, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostDefinitionPhase() != null
						&& cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user))) {
					total += cd.getLastCertifiedAmount();
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionLastCertifiedAmountForPartner(Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getUser().getId().equals(user)) {
					total += cd.getLastCertifiedAmount();
				}
			}
		}
		return total;
	}

	private void calcCostItemAdditionalAmounts(Long user) {

		this.cfSummaryWrapper.setStcCertificated(0d);
		this.cfSummaryWrapper.setAguCertificated(0d);
		this.cfSummaryWrapper.setAcuCertificated(0d);

		if (this.listSummary != null) {
			for (CostItemSummaryWrapper costItemSummary : this.listSummary) {

				if (costItemSummary.getTypes() != null && costItemSummary.getTypes().length > 0
						&& this.getListCostDefinitionsWeb() != null) {

					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
						boolean calc = false;

						for (CostItemTypes type : costItemSummary.getTypes()) {
							if (cd.getCostItem() != null && type.getValue().equals(cd.getCostItem().getId())) {
								calc = true;
								break;
							}
						}

						if (cd.getSelected() && calc && (user == null || cd.getUser().getId().equals(user))) {
							// if (cd.getStcCertification() != null &&
							// cd.getSTCSertified() != null
							// && cd.getSTCSertified()) {
							// costItemSummary.setStcCertificated(
							// costItemSummary.getStcCertificated() +
							// cd.getStcCertification());
							// if
							// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
							// {
							// cfSummaryWrapper.setStcCertificated(
							// cfSummaryWrapper.getStcCertificated() +
							// cd.getStcCertification());
							// }
							// }
							if (cd.getStcCertification() != null && cd.getSTCSertified() != null
									&& cd.getSTCSertified()) {
								costItemSummary.setStcCertificated(
										costItemSummary.getStcCertificated() + cd.getStcCertification());
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapper.setStcCertificated(cfSummaryWrapper.getStcCertificated()
											+ (cd.getStcCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getStcCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getAguCertification() != null && cd.getAGUSertified() != null
									&& cd.getAGUSertified()) {
								costItemSummary.setAguCertificated(
										costItemSummary.getAguCertificated() + cd.getAguCertification());

								// if
								// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
								// {
								// cfSummaryWrapper.setAguCertificated(
								// cfSummaryWrapper.getAguCertificated() +
								// cd.getAguCertification());
								// }
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapper.setAguCertificated(cfSummaryWrapper.getAguCertificated()
											+ (cd.getAguCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getAguCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getAcuCertification() != null && cd.getACUSertified() != null
									&& cd.getACUSertified()) {
								costItemSummary.setAcuCertificated(
										costItemSummary.getAcuCertificated() + cd.getAcuCertification());

								// if
								// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
								// {
								// cfSummaryWrapper.setAcuCertificated(
								// cfSummaryWrapper.getAcuCertificated() +
								// cd.getAcuCertification());
								// }
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapper.setAcuCertificated(cfSummaryWrapper.getAcuCertificated()
											+ (cd.getAcuCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getAcuCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getRectificationAmount() != null) {
								costItemSummary
										.setRectified(costItemSummary.getRectified() + cd.getRectificationAmount());
							}

							if (cd.getSuspendedByACU() != null && cd.getSuspendedByACU()
									&& cd.getValueSuspendACU() != null) {
								costItemSummary.setSuspended(costItemSummary.getSuspended() + cd.getValueSuspendACU());
							}
						}
					}
				}

			}
		}
	}

	private void calcCostItemAdditionalAmountsForPhase(Long user) {

		this.cfSummaryWrapperWp.setStcCertificated(0d);
		this.cfSummaryWrapperWp.setAguCertificated(0d);
		this.cfSummaryWrapperWp.setAcuCertificated(0d);
		if (this.listSummaryWp != null) {
			for (CostItemSummaryWrapper costItemSummary : this.listSummaryWp) {

				if (this.getListCostDefinitionsWeb() != null) {

					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {

						if (cd.getSelected() && cd.getCostDefinitionPhase() != null
								&& costItemSummary.getPhaseId().equals(cd.getCostDefinitionPhase().getId())
								&& (user == null || cd.getUser().getId().equals(user))) {
							if (cd.getStcCertification() != null && cd.getSTCSertified() != null
									&& cd.getSTCSertified()) {
								costItemSummary.setStcCertificated(
										costItemSummary.getStcCertificated() + cd.getStcCertification());
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									// cfSummaryWrapperWp.setStcCertificated(
									// cfSummaryWrapperWp.getStcCertificated() +
									// cd.getStcCertification());
									cfSummaryWrapperWp.setStcCertificated(cfSummaryWrapperWp.getStcCertificated()
											+ (cd.getStcCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getStcCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getAguCertification() != null && cd.getAGUSertified() != null
									&& cd.getAGUSertified()) {
								costItemSummary.setAguCertificated(
										costItemSummary.getAguCertificated() + cd.getAguCertification());

								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									// cfSummaryWrapperWp.setAguCertificated(
									// cfSummaryWrapperWp.getAguCertificated() +
									// cd.getAguCertification());
									cfSummaryWrapperWp.setAguCertificated(cfSummaryWrapperWp.getAguCertificated()
											+ (cd.getAguCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getAguCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getAcuCertification() != null && cd.getACUSertified() != null
									&& cd.getACUSertified()) {
								costItemSummary.setAcuCertificated(
										costItemSummary.getAcuCertificated() + cd.getAcuCertification());

								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									// cfSummaryWrapperWp.setAcuCertificated(
									// cfSummaryWrapperWp.getAcuCertificated() +
									// cd.getAcuCertification());
									cfSummaryWrapperWp.setAcuCertificated(cfSummaryWrapperWp.getAcuCertificated()
											+ (cd.getAcuCheckAdditionalFinansingAmount() == null ? 0d
													: cd.getAcuCheckAdditionalFinansingAmount()));
								}
							}

							if (cd.getRectificationAmount() != null) {
								costItemSummary
										.setRectified(costItemSummary.getRectified() + cd.getRectificationAmount());
							}

							if (cd.getSuspendedByACU() != null && cd.getSuspendedByACU()
									&& cd.getValueSuspendACU() != null) {
								costItemSummary.setSuspended(costItemSummary.getSuspended() + cd.getValueSuspendACU());
							}
						}
					}
				}

			}
		}
	}

	private void calcCostItemAdditionalAmountsForPartner() {

		if (this.listSummaryPartner != null) {
			this.cfSummaryWrapperPartner.setStcCertificated(0d);
			this.cfSummaryWrapperPartner.setAguCertificated(0d);
			this.cfSummaryWrapperPartner.setAcuCertificated(0d);

			for (CostItemSummaryWrapper costItemSummary : this.listSummaryPartner) {

				if (this.getListCostDefinitionsWeb() != null) {

					for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {

						if (cd.getSelected() && cd.getUser().getId().equals(costItemSummary.getUserId())) {
							if (cd.getStcCertification() != null && cd.getSTCSertified() != null
									&& cd.getSTCSertified()) {
								costItemSummary.setStcCertificated(
										costItemSummary.getStcCertificated() + cd.getStcCertification());

								// if
								// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
								// {
								// cfSummaryWrapperPartner.setStcCertificated(
								// cfSummaryWrapperPartner.getStcCertificated()
								// + cd.getStcCertification());
								// }
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapperPartner
											.setStcCertificated(cfSummaryWrapperPartner.getStcCertificated()
													+ (cd.getStcCheckAdditionalFinansingAmount() == null ? 0d
															: cd.getStcCheckAdditionalFinansingAmount()));
								}

							}

							if (cd.getAguCertification() != null && cd.getAGUSertified() != null
									&& cd.getAGUSertified()) {
								costItemSummary.setAguCertificated(
										costItemSummary.getAguCertificated() + cd.getAguCertification());
								// if
								// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
								// {
								// cfSummaryWrapperPartner.setAguCertificated(
								// cfSummaryWrapperPartner.getAguCertificated()
								// + cd.getAguCertification());
								// }
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapperPartner
											.setAguCertificated(cfSummaryWrapperPartner.getAguCertificated()
													+ (cd.getAguCheckAdditionalFinansingAmount() == null ? 0d
															: cd.getAguCheckAdditionalFinansingAmount()));
								}

							}

							if (cd.getAcuCertification() != null && cd.getACUSertified() != null
									&& cd.getACUSertified()) {
								costItemSummary.setAcuCertificated(
										costItemSummary.getAcuCertificated() + cd.getAcuCertification());

								// if
								// (Boolean.TRUE.equals(cd.getAdditionalFinansing()))
								// {
								// cfSummaryWrapperPartner.setAcuCertificated(
								// cfSummaryWrapperPartner.getAcuCertificated()
								// + cd.getAcuCertification());
								// }
								if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
									cfSummaryWrapperPartner
											.setAcuCertificated(cfSummaryWrapperPartner.getAcuCertificated()
													+ (cd.getAcuCheckAdditionalFinansingAmount() == null ? 0d
															: cd.getAcuCheckAdditionalFinansingAmount()));
								}

							}

							if (cd.getRectificationAmount() != null) {
								costItemSummary
										.setRectified(costItemSummary.getRectified() + cd.getRectificationAmount());
							}

							if (cd.getSuspendedByACU() != null && cd.getSuspendedByACU()
									&& cd.getValueSuspendACU() != null) {
								costItemSummary.setSuspended(costItemSummary.getSuspended() + cd.getValueSuspendACU());
							}
						}
					}
				}

			}
		}
	}

	private void viewEntityInternal(boolean showAll) {
		this.getSession().put("costDefinitionEdit", this.getEntityEditId());
		this.getSession().put("costDefinitionView", this.getEntityEditId());
		this.getSession().put("costDefinitionViewBackToValidationFlow", null);
		this.getSession().put(
				showAll ? "costDefinitionViewBackToDUREditFlowShowAll" : "costDefinitionViewBackToDUREditFlow", true);
		this.getSession().put(DUR_COMPILATION_EDIT_SAVED_IPP, this.getCustomPaginator().getItemsPerPageSelected());
		this.getSession().put(DUR_COMPILATION_EDIT_SAVED_PN, this.getCustomPaginator().getSelectedPage());

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	public void viewEntity() {
		viewEntityInternal(false);
	}

	public void viewEntityShowAll() {
		viewEntityInternal(true);
	}

	private Double GetCostDefinitionTotalCILAmountForPhase(Long phase, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostDefinitionPhase() != null
						&& cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user))) {
					total += cd.getCilCheck();
					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						// cfSummaryWrapperWp.setAmountCostCertificate(
						// cfSummaryWrapperWp.getAmountCostCertificate() +
						// cd.getCilCheck());
						cfSummaryWrapperWp.setAmountCostCertificate(cfSummaryWrapperWp.getAmountCostCertificate()
								+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
										: cd.getCilCheckAdditionalFinansingAmount()));
					}
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionTotalCILAmountForPartner(Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getUser().getId().equals(user)) {
					total += cd.getCilCheck();
					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						// cfSummaryWrapperPartner.setAmountCostCertificate(
						// cfSummaryWrapperPartner.getAmountCostCertificate() +
						// cd.getCilCheck());
						cfSummaryWrapperPartner
								.setAmountCostCertificate(cfSummaryWrapperPartner.getAmountCostCertificate()
										+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
												: cd.getCilCheckAdditionalFinansingAmount().doubleValue()));

					}
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionTotalCILAmount(CostItemTypes type, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem() != null && cd.getCostItem().getId().equals(type.getValue())
						&& (user == null || cd.getUser().getId().equals(user))) {
					total += cd.getCilCheck();
					if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						cfSummaryWrapper.setAmountCostCertificate(cfSummaryWrapper.getAmountCostCertificate()
								+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
										: cd.getCilCheckAdditionalFinansingAmount()));
					}
				}
			}
		}
		return total;
	}

	public void export() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListFilteredCostTable(),
					ExportPlaces.DurCompilationsCostDefinitions);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportCostDefinitionsDur") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportDurSummary() {
		try {
			XlsExport exporter = new XlsExport();

			List<SimpleMoneyRow> list = new ArrayList<SimpleMoneyRow>();

			list.add(new SimpleMoneyRow(Utils.getString("durCompilationEditTotalAmountDur"), this.getTotalAmountDur()));
			list.add(new SimpleMoneyRow(Utils.getString("durCompilationEditFesrRAmount"),
					this.getFesrReimbursementAmount()));
			list.add(new SimpleMoneyRow(Utils.getString("durCompilationEditItRAmount"), this.getItCnReimbursement()));
			list.add(new SimpleMoneyRow(Utils.getString("durCompilationEditFrRAmount"), this.getFrCnReimbursement()));
			list.add(new SimpleMoneyRow(Utils.getString("durCompilationEditTotalReimbursement"),
					this.getTotalReimbursement()));

			byte[] data = exporter.createXls(list, ExportPlaces.DurSummary);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportDurSummary") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportGridDurSummary() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListSummary(), ExportPlaces.GridDurSummary);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportGridDurSummary") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportGridDurSummaryAsse3() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListSummaryAsse3(), ExportPlaces.GridDurSummary);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportGridDurSummary") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportGridDurSummaryWp() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListSummaryWp(), ExportPlaces.GridDurSummary);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportGridDurSummary") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void exportGridDurSummaryPartner() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListSummaryPartner(), ExportPlaces.GridDurSummary);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportGridDurSummary") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private Double GetCostDefinitionTotalRequestAGU(CostItemTypes type, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostItem() != null && cd.getCostItem().getId().equals(type.getValue())
						&& (user == null || cd.getUser().getId().equals(user))) {
					boolean added = false;
					if (cd.getCreatedByPartner() == true) {
						if (cd.getCfCheck() != null) {
							total += cd.getCfCheck();
							added = true;
						}
					}

					if (!added) {
						if (cd.getCilCheck() != null) {
							total += cd.getCilCheck();
						}
						if (Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
							cfSummaryWrapper.setAmountRequest(cfSummaryWrapper.getAmountRequest()
									+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
											: cd.getCilCheckAdditionalFinansingAmount()));
						}
					}

					if (added && Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						cfSummaryWrapper.setAmountRequest(
								cfSummaryWrapper.getAmountRequest() + (cd.getCfCheckAdditionalFinansingAmount() == null
										? 0d : cd.getCfCheckAdditionalFinansingAmount()));
					}
				}
			}
		}
		return total;
	}

	private Double GetCostDefinitionTotalRequestAGUForPhase(Long phase, Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && cd.getCostDefinitionPhase() != null
						&& cd.getCostDefinitionPhase().getId().equals(phase)
						&& (user == null || cd.getUser().getId().equals(user))) {
					boolean added = false;
					if (cd.getCreatedByPartner() == true) {
						if (cd.getCfCheck() != null) {
							total += cd.getCfCheck();
							added = true;
						}

					}

					if (!added) {
						if (cd.getCilCheck() != null) {
							total += cd.getCilCheck();
						}
						if (cd.getAdditionalFinansing() != null && Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
							cfSummaryWrapperWp.setAmountRequest(cfSummaryWrapperWp.getAmountRequest()
									+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
											: cd.getCilCheckAdditionalFinansingAmount()));
						}
					}

					if (added && cd.getAdditionalFinansing() != null
							&& Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						// cfSummaryWrapperWp.setAmountRequest(cfSummaryWrapperWp.getAmountRequest()
						// + cd.getCilCheck());
						cfSummaryWrapperWp.setAmountRequest(cfSummaryWrapperWp.getAmountRequest()
								+ (cd.getCfCheckAdditionalFinansingAmount() == null ? 0d
										: cd.getCfCheckAdditionalFinansingAmount()));
					}
				}
			}

		}
		return total;
	}

	private Double GetCostByPartnerListWeb(Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && (user == null || cd.getUser().getId().equals(user))) {
					if (cd.getACUSertified() && cd.getAcuCertification() != null) {
						total += cd.getAcuCertification();
					} else if (cd.getAGUSertified() && cd.getAguCertification() != null) {
						total += cd.getAguCertification();
					} else if (cd.getSTCSertified() && cd.getStcCertification() != null) {
						total += cd.getStcCertification();
					} else if (cd.getCfCheck() != null) {
						total += cd.getCfCheck();
					} else if (cd.getCilCheck() != null) {
						total += cd.getCilCheck();
					}
				}
			}

		}

		return total;
	}

	private Double GetCostByPartnerOldDur(Long user)
			throws HibernateException, NumberFormatException, PersistenceBeanException {

		List<DurCompilations> durList = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(this.getProjectId()),
				null);
		Double total = 0d;

		if (durList != null) {
			for (DurCompilations dc : durList) {
				for (CostDefinitions cd : dc.getCostDefinitions()) {
					if (!cd.getSelected() && (user == null || cd.getUser().getId().equals(user))) {
						if (cd.getACUSertified() && cd.getAcuCertification() != null) {
							total += cd.getAcuCertification();
						} else if (cd.getAGUSertified() && cd.getAguCertification() != null) {
							total += cd.getAguCertification();
						} else if (cd.getSTCSertified() && cd.getStcCertification() != null) {
							total += cd.getStcCertification();
						} else if (cd.getCfCheck() != null) {
							total += cd.getCfCheck();
						} else if (cd.getCilCheck() != null) {
							total += cd.getCilCheck();
						}
					}
				}
			}
		}

		return total;
	}

	private Double GetCostDefinitionTotalRequestAGUForPartner(Long user) {
		Double total = 0d;

		if (this.getListCostDefinitionsWeb() != null) {
			for (CostDefinitions cd : this.getListCostDefinitionsWeb()) {
				if (cd.getSelected() && (user == null || cd.getUser().getId().equals(user))) {
					boolean added = false;
					if (cd.getCreatedByPartner() == true) {
						if (cd.getCfCheck() != null) {
							total += cd.getCfCheck();
							added = true;
						}
					}

					if (!added) {
						if (cd.getCilCheck() != null) {
							total += cd.getCilCheck();
						}
						if (cd.getAdditionalFinansing() != null && Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
							cfSummaryWrapperPartner.setAmountRequest(cfSummaryWrapperPartner.getAmountRequest()
									+ (cd.getCilCheckAdditionalFinansingAmount() == null ? 0d
											: cd.getCilCheckAdditionalFinansingAmount()));
						}
					}

					if (added && cd.getAdditionalFinansing() != null
							&& Boolean.TRUE.equals(cd.getAdditionalFinansing())) {
						cfSummaryWrapperPartner.setAmountRequest(cfSummaryWrapperPartner.getAmountRequest()
								+ (cd.getCfCheckAdditionalFinansingAmount() == null ? 0d
										: cd.getCfCheckAdditionalFinansingAmount()));
					}
				}
			}

		}
		return total;
	}

	public Double getTotalAmountCertifiedFESRAndCN() {
		Double total = this.getTotalAmountDur() == null ? 0.0 : this.getTotalAmountDur();

		Double suspend = this.getDurCompilation().getOutstandingAmount() == null ? 0.0
				: this.getDurCompilation().getOutstandingAmount();

		Double cancelSuspend = this.getDurCompilation().getOutstandingAmountUndo() == null ? 0.0
				: this.getDurCompilation().getOutstandingAmountUndo();

		return total - suspend + cancelSuspend;
	}

	public Double getTotalRepaid() {
		Double fesrReimbursementAmount = this.getFesrReimbursementAmount() == null ? 0.0
				: this.getFesrReimbursementAmount();

		Double itCnReimbursement = this.getItCnReimbursement() == null ? 0.0 : this.getItCnReimbursement();

		Double suspend = this.getDurCompilation().getOutstandingAmount() == null ? 0.0
				: this.getDurCompilation().getOutstandingAmount();

		Double retreact = this.getDurCompilation().getAmountWithdrawn() == null ? 0.0
				: this.getDurCompilation().getAmountWithdrawn();

		Double cancelSuspend = this.getDurCompilation().getOutstandingAmountUndo() == null ? 0.0
				: this.getDurCompilation().getOutstandingAmountUndo();

		return fesrReimbursementAmount + itCnReimbursement - suspend - retreact + cancelSuspend;
	}

	private CostItemSummaryWrapper prepareTotalEntity() {
		CostItemSummaryWrapper item = new CostItemSummaryWrapper(null);

		for (CostItemSummaryWrapper cis : this.listSummary) {
			item.setAmountByCf(item.getAmountByCf() + cis.getAmountByCf());
			item.setAmountCostCertificate(item.getAmountCostCertificate() + cis.getAmountCostCertificate());
			item.setAmountFromBudget(item.getAmountFromBudget() + cis.getAmountFromBudget());
			item.setAmountRequest(item.getAmountRequest() + cis.getAmountRequest());
			item.setStcCertificated(item.getStcCertificated() + cis.getStcCertificated());
			item.setAguCertificated(item.getAguCertificated() + cis.getAguCertificated());
			item.setAcuCertificated(item.getAcuCertificated() + cis.getAcuCertificated());
			item.setSuspended(item.getSuspended() + cis.getSuspended());
			item.setRectified(item.getRectified() + cis.getRectified());
			item.setReinstated((item.getReinstated() + cis.getReinstated()));
			item.setAbsorption(item.getAbsorption() + cis.getAbsorption());
		}

		return item;
	}

	private CostItemSummaryWrapper prepareTotalEntityWp() {
		CostItemSummaryWrapper item = new CostItemSummaryWrapper(null);

		for (CostItemSummaryWrapper cis : this.listSummaryWp) {
			item.setAmountByCf(item.getAmountByCf() + cis.getAmountByCf());
			item.setAmountCostCertificate(item.getAmountCostCertificate() + cis.getAmountCostCertificate());
			item.setAmountFromBudget(item.getAmountFromBudget() + cis.getAmountFromBudget());
			item.setAmountRequest(item.getAmountRequest() + cis.getAmountRequest());
			item.setStcCertificated(item.getStcCertificated() + cis.getStcCertificated());
			item.setAguCertificated(item.getAguCertificated() + cis.getAguCertificated());
			item.setAcuCertificated(item.getAcuCertificated() + cis.getAcuCertificated());
			item.setSuspended(item.getSuspended() + cis.getSuspended());
			item.setRectified(item.getRectified() + cis.getRectified());
			item.setReinstated((item.getReinstated() + cis.getReinstated()));
			item.setAbsorption(item.getAbsorption() + cis.getAbsorption());
		}

		return item;
	}

	private CostItemSummaryWrapper prepareTotalEntityPartner() {
		CostItemSummaryWrapper item = new CostItemSummaryWrapper(null);

		for (CostItemSummaryWrapper cis : this.listSummaryPartner) {
			item.setAmountByCf(item.getAmountByCf() + cis.getAmountByCf());
			item.setAmountCostCertificate(item.getAmountCostCertificate() + cis.getAmountCostCertificate());
			item.setAmountFromBudget(item.getAmountFromBudget() + cis.getAmountFromBudget());
			item.setAmountRequest(item.getAmountRequest() + cis.getAmountRequest());
			item.setStcCertificated(item.getStcCertificated() + cis.getStcCertificated());
			item.setAguCertificated(item.getAguCertificated() + cis.getAguCertificated());
			item.setAcuCertificated(item.getAcuCertificated() + cis.getAcuCertificated());
			item.setSuspended(item.getSuspended() + cis.getSuspended());
			item.setRectified(item.getRectified() + cis.getRectified());
			item.setReinstated((item.getReinstated() + cis.getReinstated()));
			item.setAbsorption(item.getAbsorption() + cis.getAbsorption());
		}

		return item;
	}

	private void CheckCertificationAvailable() throws PersistenceBeanException {
		this.setCanDenyRows(false);

		if (this.CFMode && (this.getSessionBean().isCF()) || AccessGrantHelper.getIsAGUAsse5()) {
			if (this.durCompilation.getDurState() == null) {
				this.setCFAvailable(true);
				this.setCanDenyRows(true);
			} else if (this.durCompilation.getDurState().getId().equals(DurStateTypes.Create.getValue())) {
				this.setCFAvailable(true);
				this.setCanDenyRows(true);
			} else {
				this.setCFAvailable(false);
			}
		} else {
			this.setCFAvailable(false);
		}

		if ((this.STCMode || this.UCMode) && this.getDurCompilation().getDurState() != null
				&& this.getDurCompilation().getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue())) {
			this.setSTCCertAvailable(true);
			// this.setCanDenyRows(true);
			this.setUCCertAvailable(true);
		} else {
			this.setSTCCertAvailable(false);
		}

		if (this.AGUMode && this.getDurCompilation().getDurState() != null
				&& this.getDurCompilation().getDurState().getId().equals(DurStateTypes.AGUEvaluation.getValue())) {
			this.setAGUCertAvailable(true);
			if (getSessionBean().isSuperAdmin()) {
				this.setSTCCertAvailable(true);
			}
		} else {
			this.setAGUCertAvailable(false);
		}

		if (this.ACUMode && this.getDurCompilation().getDurState() != null
				&& this.getDurCompilation().getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue())) {
			this.setACUCertAvailable(true);
			if (getSessionBean().isSuperAdmin()) {
				this.setSTCCertAvailable(true);
				this.setAGUCertAvailable(true);
			}
		} else {
			this.setACUCertAvailable(false);
		}

		if (this.ACUMode && this.getDurCompilation().getDurState() != null
				&& this.getDurCompilation().getDurState().getId().equals(DurStateTypes.Certified.getValue())) {
			this.setCertifiableAvailable(true);
			if (getSessionBean().isSuperAdmin()) {
				this.setSTCCertAvailable(true);
				this.setAGUCertAvailable(true);
			}
		} else {
			this.setCertifiableAvailable(false);
		}

		if ((this.ACUMode || this.AGUMode) && this.getDurCompilation().getDurState() != null
				&& this.getDurCompilation().getDurState().getId().equals(DurStateTypes.Certified.getValue())) {
			this.setCertifiableACUAGUAvailable(true);
		} else {
			this.setCertifiableACUAGUAvailable(false);
		}

	}

	public void editFiltredCosts() {
		this.getSession().put(DUR_EDIT_FROM_VIEW, true);
		this.getSession().put(FILTER_PARTNER_VALUE, getFilterPartner());
		this.getSession().put(FILTER_PHASES_VALUE, getFilterPhase());
		this.getSession().put(FILTER_COST_DEF_ID_VALUE, getFilterCostDefID());
		this.getSession().put(FILTER_COST_ITEM, getFilterCostItem());
		this.setFilterPartner(null);
		this.setFilterPhase(null);
		this.setFilterCostItem(null);
		this.setFilterCostDefID(null);

		this.getSession().put("durCompilationEditShow", false);
		this.getSession().put("durCompilationEditBack", true);
		this.goTo(PagesTypes.DURCOMPILATIONEDIT);
	}

	/**
	 * Sets durCompilation
	 * 
	 * @param durCompilation
	 *            the durCompilation to set
	 */
	public void setDurCompilation(DurCompilations durCompilation) {
		this.durCompilation = durCompilation;
	}

	/**
	 * Gets durCompilation
	 * 
	 * @return durCompilation the durCompilation
	 */
	public DurCompilations getDurCompilation() {
		return durCompilation;
	}

	/**
	 * Sets editMode
	 * 
	 * @param editMode
	 *            the editMode to set
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * Gets editMode
	 * 
	 * @return editMode the editMode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	public boolean isCertified() {
		return certified;
	}

	public void setCertified(boolean certified) {
		this.certified = certified;
	}

	/**
	 * Sets listCostDefinitionsWeb
	 * 
	 * @param listCostDefinitionsWeb
	 *            the listCostDefinitionsWeb to set
	 */
	public void setListCostDefinitionsWeb(List<CostDefinitions> listCostDefinitionsWeb) {
		this.listCostDefinitionsWeb = listCostDefinitionsWeb;
	}

	/**
	 * Gets listCostDefinitionsWeb
	 * 
	 * @return listCostDefinitionsWeb the listCostDefinitionsWeb
	 */
	public List<CostDefinitions> getListCostDefinitionsWeb() {
		return this.listCostDefinitionsWeb;
	}

	/**
	 * Sets CFMode
	 * 
	 * @param cFMode
	 *            the cFMode to set
	 */
	public void setCFMode(boolean cFMode) {
		CFMode = cFMode;
	}

	/**
	 * Gets CFMode
	 * 
	 * @return CFMode the cFMode
	 */
	public boolean isCFMode() {
		return CFMode;
	}

	/**
	 * Sets STCMode
	 * 
	 * @param sTCMode
	 *            the sTCMode to set
	 */
	public void setSTCMode(boolean sTCMode) {
		STCMode = sTCMode;
	}

	/**
	 * Gets STCMode
	 * 
	 * @return STCMode the sTCMode
	 */
	public boolean isSTCMode() {
		return STCMode;
	}

	/**
	 * Sets AGUMode
	 * 
	 * @param aGUMode
	 *            the aGUMode to set
	 */
	public void setAGUMode(boolean aGUMode) {
		AGUMode = aGUMode;
	}

	/**
	 * Gets AGUMode
	 * 
	 * @return AGUMode the aGUMode
	 */
	public boolean isAGUMode() {
		return AGUMode;
	}

	/**
	 * Sets ACUMode
	 * 
	 * @param aCUMode
	 *            the aCUMode to set
	 */
	public void setACUMode(boolean aCUMode) {
		ACUMode = aCUMode;
	}

	/**
	 * Gets ACUMode
	 * 
	 * @return ACUMode the aCUMode
	 */
	public boolean isACUMode() {
		return ACUMode;
	}

	/**
	 * Sets selectAll
	 * 
	 * @param selectAll
	 *            the selectAll to set
	 */
	public void setSelectAll(boolean selectAll) {
		this.getViewState().put("durComEditSelectAll", selectAll);
	}

	/**
	 * Gets selectAll
	 * 
	 * @return selectAll the selectAll
	 */
	public boolean isSelectAll() {
		return Boolean.valueOf(String.valueOf(this.getViewState().get("durComEditSelectAll")));
	}

	/**
	 * Sets durInfo
	 * 
	 * @param durInfo
	 *            the durInfo to set
	 */
	public void setDurInfo(DurInfos durInfo) {
		this.getViewState().put("dur_info", durInfo);
	}

	/**
	 * Gets durInfo
	 * 
	 * @return durInfo the durInfo
	 */
	public DurInfos getDurInfo() {
		return (DurInfos) this.getViewState().get("dur_info");
	}

	/**
	 * Sets durSummaries
	 * 
	 * @param durSummaries
	 *            the durSummaries to set
	 */
	public void setDurSummaries(DurSummaries durSummaries) {
		this.durSummaries = durSummaries;
	}

	/**
	 * Gets durSummaries
	 * 
	 * @return durSummaries the durSummaries
	 */
	public DurSummaries getDurSummaries() {
		return durSummaries;
	}

	/**
	 * Sets involvedPartner
	 * 
	 * @param involvedPartner
	 *            the involvedPartner to set
	 */
	public void setInvolvedPartner(String involvedPartner) {
		this.involvedPartner = involvedPartner;
	}

	/**
	 * Gets involvedPartner
	 * 
	 * @return involvedPartner the involvedPartner
	 */
	public String getInvolvedPartner() {
		return involvedPartner;
	}

	/**
	 * Sets listInvolvedPartnersWeb
	 * 
	 * @param listInvolvedPartnersWeb
	 *            the listInvolvedPartnersWeb to set
	 */
	public void setListInvolvedPartnersWeb(List<SelectItem> listInvolvedPartnersWeb) {
		this.listInvolvedPartnersWeb = listInvolvedPartnersWeb;
	}

	/**
	 * Gets listInvolvedPartnersWeb
	 * 
	 * @return listInvolvedPartnersWeb the listInvolvedPartnersWeb
	 */
	public List<SelectItem> getListInvolvedPartnersWeb() {
		return listInvolvedPartnersWeb;
	}

	/**
	 * Gets docB
	 * 
	 * @return docB the docB
	 */
	public String getDocB() {
		return docB;
	}

	/**
	 * Sets docB
	 * 
	 * @param docB
	 *            the docB to set
	 */
	public void setDocB(String docB) {
		this.docB = docB;
	}

	/**
	 * Sets docC
	 * 
	 * @param docC
	 *            the docC to set
	 */
	public void setDocC(String docC) {
		this.docC = docC;
	}

	/**
	 * Gets docC
	 * 
	 * @return docC the docC
	 */
	public String getDocC() {
		return docC;
	}

	/**
	 * Sets bDocument
	 * 
	 * @param bDocument
	 *            the bDocument to set
	 */
	public void setbDocument(Documents bDocument) {
		this.bDocument = bDocument;
	}

	/**
	 * Gets bDocument
	 * 
	 * @return bDocument the bDocument
	 */
	public Documents getbDocument() {
		return bDocument;
	}

	/**
	 * Sets cDocument
	 * 
	 * @param cDocument
	 *            the cDocument to set
	 */
	public void setcDocument(Documents cDocument) {
		this.cDocument = cDocument;
	}

	/**
	 * Gets cDocument
	 * 
	 * @return cDocument the cDocument
	 */
	public Documents getcDocument() {
		return cDocument;
	}

	/**
	 * Sets bDocumentUpFile
	 * 
	 * @param bDocumentUpFile
	 *            the bDocumentUpFile to set
	 */
	public void setbDocumentUpFile(UploadedFile bDocumentUpFile) {
		this.bDocumentUpFile = bDocumentUpFile;
	}

	/**
	 * Gets bDocumentUpFile
	 * 
	 * @return bDocumentUpFile the bDocumentUpFile
	 */
	public UploadedFile getbDocumentUpFile() {
		return bDocumentUpFile;
	}

	/**
	 * Sets cDocumentUpFile
	 * 
	 * @param cDocumentUpFile
	 *            the cDocumentUpFile to set
	 */
	public void setcDocumentUpFile(UploadedFile cDocumentUpFile) {
		this.cDocumentUpFile = cDocumentUpFile;
	}

	/**
	 * Gets cDocumentUpFile
	 * 
	 * @return cDocumentUpFile the cDocumentUpFile
	 */
	public UploadedFile getcDocumentUpFile() {
		return cDocumentUpFile;
	}

	/**
	 * Sets nonRegularSituation
	 * 
	 * @param nonRegularSituation
	 *            the nonRegularSituation to set
	 */
	public void setNonRegularSituation(boolean nonRegularSituation) {
		this.nonRegularSituation = nonRegularSituation;
	}

	/**
	 * Gets nonRegularSituation
	 * 
	 * @return nonRegularSituation the nonRegularSituation
	 */
	public boolean isNonRegularSituation() {
		return nonRegularSituation;
	}

	/**
	 * Sets nonRegType
	 * 
	 * @param nonRegType
	 *            the nonRegType to set
	 */
	public void setNonRegType(String nonRegType) {
		this.nonRegType = nonRegType;
	}

	/**
	 * Gets nonRegType
	 * 
	 * @return nonRegType the nonRegType
	 */
	public String getNonRegType() {
		return nonRegType;
	}

	/**
	 * Sets listNonRegularTypesWeb
	 * 
	 * @param listNonRegularTypesWeb
	 *            the listNonRegularTypesWeb to set
	 */
	public void setListNonRegularTypesWeb(List<SelectItem> listNonRegularTypesWeb) {
		this.listNonRegularTypesWeb = listNonRegularTypesWeb;
	}

	/**
	 * Gets listNonRegularTypesWeb
	 * 
	 * @return listNonRegularTypesWeb the listNonRegularTypesWeb
	 */
	public List<SelectItem> getListNonRegularTypesWeb() {
		return listNonRegularTypesWeb;
	}

	/**
	 * Sets totalAmountDur
	 * 
	 * @param totalAmountDur
	 *            the totalAmountDur to set
	 */
	public void setTotalAmountDur(Double totalAmountDur) {
		this.totalAmountDur = totalAmountDur;
	}

	/**
	 * Gets totalAmountDur
	 * 
	 * @return totalAmountDur the totalAmountDur
	 */
	public Double getTotalAmountDur() {
		return totalAmountDur;
	}

	/**
	 * Sets totalAmountDur2
	 * 
	 * @param totalAmountDur2
	 *            the totalAmountDur2 to set
	 */
	public void setTotalAmountDur2(Double totalAmountDur2) {
		this.totalAmountDur2 = totalAmountDur2;
	}

	/**
	 * Gets totalAmountDur2
	 * 
	 * @return totalAmountDur2 the totalAmountDur2
	 */
	public Double getTotalAmountDur2() {
		return totalAmountDur2;
	}

	/**
	 * Sets fesrReimbursementAmount
	 * 
	 * @param fesrReimbursementAmount
	 *            the fesrReimbursementAmount to set
	 */
	public void setFesrReimbursementAmount(Double fesrReimbursementAmount) {
		this.fesrReimbursementAmount = fesrReimbursementAmount;
	}

	/**
	 * Gets fesrReimbursementAmount
	 * 
	 * @return fesrReimbursementAmount the fesrReimbursementAmount
	 */
	public Double getFesrReimbursementAmount() {
		return fesrReimbursementAmount;
	}

	/**
	 * Sets fesrReimbursementAmount2
	 * 
	 * @param fesrReimbursementAmount2
	 *            the fesrReimbursementAmount2 to set
	 */
	public void setFesrReimbursementAmount2(Double fesrReimbursementAmount2) {
		this.fesrReimbursementAmount2 = fesrReimbursementAmount2;
	}

	/**
	 * Gets fesrReimbursementAmount2
	 * 
	 * @return fesrReimbursementAmount2 the fesrReimbursementAmount2
	 */
	public Double getFesrReimbursementAmount2() {
		return fesrReimbursementAmount2;
	}

	/**
	 * Sets itCnReimbursement
	 * 
	 * @param itCnReimbursement
	 *            the itCnReimbursement to set
	 */
	public void setItCnReimbursement(Double itCnReimbursement) {
		this.itCnReimbursement = itCnReimbursement;
	}

	/**
	 * Gets itCnReimbursement
	 * 
	 * @return itCnReimbursement the itCnReimbursement
	 */
	public Double getItCnReimbursement() {
		return itCnReimbursement;
	}

	/**
	 * Sets frCnReimbursement
	 * 
	 * @param frCnReimbursement
	 *            the frCnReimbursement to set
	 */
	public void setFrCnReimbursement(Double frCnReimbursement) {
		this.frCnReimbursement = frCnReimbursement;
	}

	/**
	 * Gets frCnReimbursement
	 * 
	 * @return frCnReimbursement the frCnReimbursement
	 */
	public Double getFrCnReimbursement() {
		return frCnReimbursement;
	}

	/**
	 * Sets totalAmountEligibleExpenditure
	 * 
	 * @param totalAmountEligibleExpenditure
	 *            the totalAmountEligibleExpenditure to set
	 */
	public void setTotalAmountEligibleExpenditure(Double totalAmountEligibleExpenditure) {
		this.totalAmountEligibleExpenditure = totalAmountEligibleExpenditure;
	}

	/**
	 * Gets totalAmountEligibleExpenditure
	 * 
	 * @return totalAmountEligibleExpenditure the totalAmountEligibleExpenditure
	 */
	public Double getTotalAmountEligibleExpenditure() {
		return totalAmountEligibleExpenditure;
	}

	/**
	 * Sets totalAmountPublicExpenditure
	 * 
	 * @param totalAmountPublicExpenditure
	 *            the totalAmountPublicExpenditure to set
	 */
	public void setTotalAmountPublicExpenditure(Double totalAmountPublicExpenditure) {
		this.totalAmountPublicExpenditure = totalAmountPublicExpenditure;
	}

	/**
	 * Gets totalAmountPublicExpenditure
	 * 
	 * @return totalAmountPublicExpenditure the totalAmountPublicExpenditure
	 */
	public Double getTotalAmountPublicExpenditure() {
		return totalAmountPublicExpenditure;
	}

	/**
	 * Sets totalAmountPrivateExpenditure
	 * 
	 * @param totalAmountPrivateExpenditure
	 *            the totalAmountPrivateExpenditure to set
	 */
	public void setTotalAmountPrivateExpenditure(Double totalAmountPrivateExpenditure) {
		this.totalAmountPrivateExpenditure = totalAmountPrivateExpenditure;
	}

	/**
	 * Gets totalAmountPrivateExpenditure
	 * 
	 * @return totalAmountPrivateExpenditure the totalAmountPrivateExpenditure
	 */
	public Double getTotalAmountPrivateExpenditure() {
		return totalAmountPrivateExpenditure;
	}

	/**
	 * Sets totalAmountStateAid
	 * 
	 * @param totalAmountStateAid
	 *            the totalAmountStateAid to set
	 */
	public void setTotalAmountStateAid(Double totalAmountStateAid) {
		this.totalAmountStateAid = totalAmountStateAid;
	}

	/**
	 * Gets totalAmountStateAid
	 * 
	 * @return totalAmountStateAid the totalAmountStateAid
	 */
	public Double getTotalAmountStateAid() {
		return totalAmountStateAid;
	}

	/**
	 * Sets totalAmountStateAidAdvanceCover
	 * 
	 * @param totalAmountStateAidAdvanceCover
	 *            the totalAmountStateAidAdvanceCover to set
	 */
	public void setTotalAmountStateAidAdvanceCover(Double totalAmountStateAidAdvanceCover) {
		this.totalAmountStateAidAdvanceCover = totalAmountStateAidAdvanceCover;
	}

	/**
	 * Gets totalAmountStateAidAdvanceCover
	 * 
	 * @return totalAmountStateAidAdvanceCover the
	 *         totalAmountStateAidAdvanceCover
	 */
	public Double getTotalAmountStateAidAdvanceCover() {
		return totalAmountStateAidAdvanceCover;
	}

	/**
	 * Sets totalContributionInKindToOperatoinPublicShare
	 * 
	 * @param totalContributionInKindToOperatoinPublicShare
	 *            the totalContributionInKindToOperatoinPublicShare to set
	 */
	public void setTotalContributionInKindToOperatoinPublicShare(Double totalContributionInKindToOperatoinPublicShare) {
		this.totalContributionInKindToOperatoinPublicShare = totalContributionInKindToOperatoinPublicShare;
	}

	/**
	 * Gets totalContributionInKindToOperatoinPublicShare
	 * 
	 * @return totalContributionInKindToOperatoinPublicShare the
	 *         totalContributionInKindToOperatoinPublicShare
	 */
	public Double getTotalContributionInKindToOperatoinPublicShare() {
		return totalContributionInKindToOperatoinPublicShare;
	}

	/**
	 * Sets totalReimbursement
	 * 
	 * @param totalReimbursement
	 *            the totalReimbursement to set
	 */
	public void setTotalReimbursement(Double totalReimbursement) {
		this.totalReimbursement = totalReimbursement;
	}

	/**
	 * Gets totalReimbursement
	 * 
	 * @return totalReimbursement the totalReimbursement
	 */
	public Double getTotalReimbursement() {
		return totalReimbursement;
	}

	/**
	 * Sets listSummary
	 * 
	 * @param listSummary
	 *            the listSummary to set
	 */
	public void setListSummary(List<CostItemSummaryWrapper> listSummary) {
		this.listSummary = listSummary;
	}

	/**
	 * Gets listSummary
	 * 
	 * @return listSummary the listSummary
	 */
	public List<CostItemSummaryWrapper> getListSummary() {
		return listSummary;
	}

	public List<CostItemSummaryWrapper> getListSummaryWp() {
		return listSummaryWp;
	}

	public void setListSummaryWp(List<CostItemSummaryWrapper> listSummaryWp) {
		this.listSummaryWp = listSummaryWp;
	}

	public List<CostItemSummaryWrapper> getListSummaryPartner() {
		return listSummaryPartner;
	}

	public void setListSummaryPartner(List<CostItemSummaryWrapper> listSummaryPartner) {
		this.listSummaryPartner = listSummaryPartner;
	}

	/**
	 * Sets listCostTable
	 * 
	 * @param listCostTable
	 *            the listCostTable to set
	 */
	public void setListCostTable(List<CostDefinitions> listCostTable) {
		this.listCostTable = listCostTable;
	}

	/**
	 * Gets listCostTable
	 * 
	 * @return listCostTable the listCostTable
	 */
	public List<CostDefinitions> getListCostTable() {
		return listCostTable;
	}

	/**
	 * Sets STCCertAvailable
	 * 
	 * @param sTCCertAvailable
	 *            the sTCCertAvailable to set
	 */
	public void setSTCCertAvailable(boolean sTCCertAvailable) {
		STCCertAvailable = sTCCertAvailable;
	}

	/**
	 * Gets STCCertAvailable
	 * 
	 * @return STCCertAvailable the sTCCertAvailable
	 */
	public boolean isSTCCertAvailable() {
		return STCCertAvailable;
	}

	/**
	 * Gets UCCertAvailable
	 * 
	 * @return UCCertAvailable the uCCertAvailable
	 */
	public boolean isUCCertAvailable() {
		return UCCertAvailable;
	}

	/**
	 * Sets UCCertAvailable
	 * 
	 * @param uCCertAvailable
	 *            the uCCertAvailable to set
	 */
	public void setUCCertAvailable(boolean uCCertAvailable) {
		UCCertAvailable = uCCertAvailable;
	}

	/**
	 * Sets AGUCertAvailable
	 * 
	 * @param aGUCertAvailable
	 *            the aGUCertAvailable to set
	 */
	public void setAGUCertAvailable(boolean aGUCertAvailable) {
		AGUCertAvailable = aGUCertAvailable;
	}

	/**
	 * Gets AGUCertAvailable
	 * 
	 * @return AGUCertAvailable the aGUCertAvailable
	 */
	public boolean isAGUCertAvailable() {
		return AGUCertAvailable;
	}

	/**
	 * Sets ACUCertAvailable
	 * 
	 * @param aCUCertAvailable
	 *            the aCUCertAvailable to set
	 */
	public void setACUCertAvailable(boolean aCUCertAvailable) {
		ACUCertAvailable = aCUCertAvailable;
	}

	/**
	 * Gets ACUCertAvailable
	 * 
	 * @return ACUCertAvailable the aCUCertAvailable
	 */
	public boolean isACUCertAvailable() {
		return ACUCertAvailable;
	}

	public boolean isCertifiableAvailable() {
		return certifiableAvailable;
	}

	public void setCertifiableAvailable(boolean certifiableAvailable) {
		this.certifiableAvailable = certifiableAvailable;
	}

	public boolean isCertifiableACUAGUAvailable() {
		return certifiableACUAGUAvailable;
	}

	public void setCertifiableACUAGUAvailable(boolean certifiableACUAGUAvailable) {
		this.certifiableACUAGUAvailable = certifiableACUAGUAvailable;
	}

	/**
	 * Sets showScroll
	 * 
	 * @param showScroll
	 *            the showScroll to set
	 */
	public void setShowScroll(Boolean showScroll) {
		DURCompilationEditBean.showScroll = showScroll;
	}

	/**
	 * Gets showScroll
	 * 
	 * @return showScroll the showScroll
	 */
	public Boolean getShowScroll() {
		return showScroll;
	}

	/**
	 * Sets itemsPerPage
	 * 
	 * @param itemsPerPage
	 *            the itemsPerPage to set
	 */
	public void setItemsPerPage(String itemsPerPage) {
		DURCompilationEditBean.itemsPerPage = itemsPerPage;
	}

	/**
	 * Gets itemsPerPage
	 * 
	 * @return itemsPerPage the itemsPerPage
	 */
	public String getItemsPerPage() {
		return itemsPerPage;
	}

	/**
	 * Sets itemsPerPageList
	 * 
	 * @param itemsPerPageList
	 *            the itemsPerPageList to set
	 */
	public void setItemsPerPageList(List<SelectItem> itemsPerPageList) {
		DURCompilationEditBean.itemsPerPageList = itemsPerPageList;
	}

	/**
	 * Gets itemsPerPageList
	 * 
	 * @return itemsPerPageList the itemsPerPageList
	 */
	public List<SelectItem> getItemsPerPageList() {
		return itemsPerPageList;
	}

	/**
	 * Sets spanWarningVisibility
	 * 
	 * @param spanWarningVisibility
	 *            the spanWarningVisibility to set
	 */
	public void setSpanWarningVisibility(String spanWarningVisibility) {
		this.spanWarningVisibility = spanWarningVisibility;
	}

	/**
	 * Gets spanWarningVisibility
	 * 
	 * @return spanWarningVisibility the spanWarningVisibility
	 */
	public String getSpanWarningVisibility() {
		return spanWarningVisibility;
	}

	/**
	 * Sets CFAvailable
	 * 
	 * @param cFAvailable
	 *            the cFAvailable to set
	 */
	public void setCFAvailable(boolean cFAvailable) {
		CFAvailable = cFAvailable;
	}

	/**
	 * Gets CFAvailable
	 * 
	 * @return CFAvailable the cFAvailable
	 */
	public boolean isCFAvailable() {
		return CFAvailable;
	}

	/**
	 * Sets listInvolvedPartners
	 * 
	 * @param listInvolvedPartners
	 *            the listInvolvedPartners to set
	 */
	public void setListInvolvedPartners(List<CFPartnerAnagraphs> listInvolvedPartners) {
		this.listInvolvedPartners = listInvolvedPartners;
	}

	/**
	 * Gets listInvolvedPartners
	 * 
	 * @return listInvolvedPartners the listInvolvedPartners
	 */
	public List<CFPartnerAnagraphs> getListInvolvedPartners() {
		return listInvolvedPartners;
	}

	/**
	 * Sets selectedPartner2
	 * 
	 * @param selectedPartner2
	 *            the selectedPartner2 to set
	 */
	public void setSelectedPartner2(String selectedPartner2) {
		this.selectedPartner2 = selectedPartner2;
	}

	/**
	 * Gets selectedPartner2
	 * 
	 * @return selectedPartner2 the selectedPartner2
	 */
	public String getSelectedPartner2() {
		return selectedPartner2;
	}

	/**
	 * Sets currentProjectId
	 * 
	 * @param currentProjectId
	 *            the currentProjectId to set
	 */
	public void setCurrentProjectId(String currentProjectId) {
		this.getViewState().put("durCompilationEditProjectId", currentProjectId);
	}

	/**
	 * Gets currentProjectId
	 * 
	 * @return currentProjectId the currentProjectId
	 */
	public String getCurrentProjectId() {
		if (this.getViewState().get("durCompilationEditProjectId") != null) {
			return String.valueOf(this.getViewState().get("durCompilationEditProjectId"));
		} else {
			return null;
		}
	}

	/**
	 * Sets canDenyRows
	 * 
	 * @param canDenyRows
	 *            the canDenyRows to set
	 */
	public void setCanDenyRows(boolean canDenyRows) {
		this.canDenyRows = canDenyRows;
	}

	/**
	 * Gets canDenyRows
	 * 
	 * @return canDenyRows the canDenyRows
	 */
	public boolean isCanDenyRows() {
		return canDenyRows;
	}

	/**
	 * Sets selectedPartners
	 * 
	 * @param selectedPartners
	 *            the selectedPartners to set
	 */
	public void setSelectedPartners(List<String> selectedPartners) {
		this.selectedPartners = selectedPartners;
	}

	/**
	 * Gets selectedPartners
	 * 
	 * @return selectedPartners the selectedPartners
	 */
	public List<String> getSelectedPartners() {
		return selectedPartners;
	}

	/**
	 * Sets entityEditId
	 * 
	 * @param entityEditId
	 *            the entityEditId to set
	 */
	public void setEntityEditId(Long entityEditId) {
		this.entityEditId = entityEditId;
	}

	/**
	 * Gets entityEditId
	 * 
	 * @return entityEditId the entityEditId
	 */
	public Long getEntityEditId() {
		return entityEditId;
	}

	/**
	 * Sets canSaveAmount
	 * 
	 * @param canSaveAmount
	 *            the canSaveAmount to set
	 */
	public void setCanSaveAmount(boolean canSaveAmount) {
		this.canSaveAmount = canSaveAmount;
	}

	/**
	 * Gets canSaveAmount
	 * 
	 * @return canSaveAmount the canSaveAmount
	 */
	public boolean getCanSaveAmount() {
		return canSaveAmount;
	}

	/**
	 * Sets spanSaveAmountsWarningVisibility
	 * 
	 * @param spanSaveAmountsWarningVisibility
	 *            the spanSaveAmountsWarningVisibility to set
	 */
	public void setSpanSaveAmountsWarningVisibility(String spanSaveAmountsWarningVisibility) {
		this.getViewState().put("spanSaveAmountsWarningVisibility", spanSaveAmountsWarningVisibility);
	}

	/**
	 * Gets spanSaveAmountsWarningVisibility
	 * 
	 * @return spanSaveAmountsWarningVisibility the
	 *         spanSaveAmountsWarningVisibility
	 */
	public String getSpanSaveAmountsWarningVisibility() {
		return (String) this.getViewState().get("spanSaveAmountsWarningVisibility");
	}

	public String getBRole() {
		return (String) this.getViewState().get("selectedBRole");
	}

	public void setBRole(String entity) {
		this.getViewState().put("selectedBRole", entity);
	}

	public String getCRole() {
		return (String) this.getViewState().get("selectedCRole");
	}

	public void setCRole(String entity) {
		this.getViewState().put("selectedCRole", entity);
	}

	public String getUCRole() {
		return (String) this.getViewState().get("selectedUCRole");
	}

	public void setUCRole(String entity) {
		this.getViewState().put("selectedUCRole", entity);
	}

	public String getADCRole() {
		return (String) this.getViewState().get("selectedADCRole");
	}

	public void setADCRole(String entity) {
		this.getViewState().put("selectedADCRole", entity);
	}

	public String getSuspendRole() {
		return (String) this.getViewState().get("selectedSuspendRole");
	}

	public void setSuspendRole(String entity) {
		this.getViewState().put("selectedSuspendRole", entity);
	}

	public String getADGRole() {
		return (String) this.getViewState().get("selectedADGRole");
	}

	public void setADGRole(String entity) {
		this.getViewState().put("selectedADGRole", entity);
	}

	/**
	 * Sets listSelectRoles
	 * 
	 * @param listSelectRoles
	 *            the listSelectRoles to set
	 */
	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		this.listSelectRoles = listSelectRoles;
	}

	/**
	 * Gets listSelectRoles
	 * 
	 * @return listSelectRoles the listSelectRoles
	 */
	public List<SelectItem> getListSelectRoles() {
		return listSelectRoles;
	}

	/**
	 * Sets entityManageId
	 * 
	 * @param entityManageId
	 *            the entityManageId to set
	 */
	public void setEntityManageId(Long entityManageId) {
		this.getViewState().put("entityManageId", entityManageId);

	}

	/**
	 * Gets entityManageId
	 * 
	 * @return entityManageId the entityManageId
	 */
	public Long getEntityManageId() {
		return (Long) this.getViewState().get("entityManageId");
	}

	/**
	 * Sets selectedIndex
	 * 
	 * @param selectedIndex
	 *            the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Gets selectedIndex
	 * 
	 * @return selectedIndex the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Sets costNumber
	 * 
	 * @param costNumber
	 *            the costNumber to set
	 */
	public void setCostNumber(String costNumber) {
		this.getViewState().put("costNumber", costNumber);
	}

	/**
	 * Gets costNumber
	 * 
	 * @return costNumber the costNumber
	 */
	public String getCostNumber() {
		return (String) this.getViewState().get("costNumber");
	}

	/**
	 * Sets costAmountReport
	 * 
	 * @param costAmountReport
	 *            the costAmountReport to set
	 */
	public void setCostAmountReport(String costAmountReport) {
		this.getViewState().put("costAmountReport", costAmountReport);
	}

	/**
	 * Gets costAmountReport
	 * 
	 * @return costAmountReport the costAmountReport
	 */
	public String getCostAmountReport() {
		return (String) this.getViewState().get("costAmountReport");
	}

	/**
	 * Sets costAmountCIL
	 * 
	 * @param costAmountCIL
	 *            the costAmountCIL to set
	 */
	public void setCostAmountCIL(String costAmountCIL) {
		this.getViewState().put("costAmountCIL", costAmountCIL);
	}

	/**
	 * Gets costAmountCIL
	 * 
	 * @return costAmountCIL the costAmountCIL
	 */
	public String getCostAmountCIL() {
		return (String) this.getViewState().get("costAmountCIL");
	}

	/**
	 * Sets costAmountPayment
	 * 
	 * @param costAmountPayment
	 *            the costAmountPayment to set
	 */
	public void setCostAmountPayment(String costAmountPayment) {
		this.getViewState().put("costAmountPayment", costAmountPayment);
	}

	/**
	 * Gets costAmountPayment
	 * 
	 * @return costAmountPayment the costAmountPayment
	 */
	public String getCostAmountPayment() {
		return (String) this.getViewState().get("costAmountPayment");
	}

	/**
	 * Sets costSuspentionType
	 * 
	 * @param costSuspentionType
	 *            the costSuspentionType to set
	 */
	public void setCostSuspensionType(String costSuspentionType) {
		this.getViewState().put("costSuspensionType", costSuspentionType);
	}

	/**
	 * Gets costSuspentionType
	 * 
	 * @return costSuspentionType the costSuspentionType
	 */
	public String getCostSuspensionType() {
		return (String) this.getViewState().get("costSuspensionType");
	}

	/**
	 * Sets suspensionTypes
	 * 
	 * @param suspensionTypes
	 *            the suspensionTypes to set
	 */
	public void setSuspensionTypes(List<SelectItem> suspensionTypes) {
		this.suspensionTypes = suspensionTypes;
	}

	/**
	 * Gets suspensionTypes
	 * 
	 * @return suspensionTypes the suspensionTypes
	 */
	public List<SelectItem> getSuspensionTypes() {
		return suspensionTypes;
	}

	/**
	 * Sets listFilteredCostTable
	 * 
	 * @param listFilteredCostTable
	 *            the listFilteredCostTable to set
	 */
	public void setListFilteredCostTable(List<CostDefinitions> listFilteredCostTable) {
		this.getViewState().put("listFilteredCostTable", listFilteredCostTable);
	}

	/**
	 * Gets listFilteredCostTable
	 * 
	 * @return listFilteredCostTable the listFilteredCostTable
	 */
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getListFilteredCostTable() {
		return (List<CostDefinitions>) this.getViewState().get("listFilteredCostTable");
	}

	public String getSuspendItemId() {
		return suspendItemId;
	}

	public void setSuspendItemId(String suspendItemId) {
		this.suspendItemId = suspendItemId;
	}

	public String getSuspendAmount() {
		return suspendAmount;
	}

	public void setSuspendAmount(String suspendAmount) {
		this.suspendAmount = suspendAmount;
	}

	public Date getSuspendDate() {
		return suspendDate;
	}

	public void setSuspendDate(Date suspendDate) {
		this.suspendDate = suspendDate;
	}

	public Date getSuspendActDate() {
		return suspendActDate;
	}

	public void setSuspendActDate(Date suspendActDate) {
		this.suspendActDate = suspendActDate;
	}

	public String getSuspendActNumber() {
		return suspendActNumber;
	}

	public void setSuspendActNumber(String suspendActNumber) {
		this.suspendActNumber = suspendActNumber;
	}

	public Boolean getSuspAmountError() {
		return suspAmountError;
	}

	public void setSuspAmountError(Boolean suspAmountError) {
		this.suspAmountError = suspAmountError;
	}

	public List<SelectItem> getSuspensionReasons() {
		return suspensionReasons;
	}

	public void setSuspensionReasons(List<SelectItem> suspensionReasons) {
		this.suspensionReasons = suspensionReasons;
	}

	public void setCostSuspensionNote(String costSuspensionReason) {
		this.getViewState().put("costSuspensionReason", costSuspensionReason);
	}

	public String getCostSuspensionNote() {
		return (String) this.getViewState().get("costSuspensionReason");
	}

	public void setCostRectificationReason(String costRectificationReason) {
		this.getViewState().put("costRectificationReason", costRectificationReason);
	}

	public String getCostRectificationReason() {
		return (String) this.getViewState().get("costRectificationReason");
	}

	public void setCostDurRecoveryReason(String costDurRecoveryReason) {
		this.getViewState().put("costDurRecoveryReason", costDurRecoveryReason);
	}

	public String getCostDurRecoveryReason() {
		return (String) this.getViewState().get("costDurRecoveryReason");
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getListCostDefinitionsBackup() {
		if (this.getViewState().containsKey("listCostDefinitionsBackup")) {
			return (List<CostDefinitions>) getViewState().get("listCostDefinitionsBackup");
		}
		return null;
	}

	public void setListCostDefinitionsBackUp(List<CostDefinitions> listCostDefinitionsWeb) {
		this.getViewState().put("listCostDefinitionsBackup", listCostDefinitionsWeb);
	}

	public void setNeedLoadBackup(Boolean needLoadBackup) {
		this.getViewState().put("needLoadBackup", needLoadBackup);
	}

	public Boolean getNeedLoadBackup() {
		if (this.getViewState().containsKey("needLoadBackup")) {
			return (Boolean) this.getViewState().get("needLoadBackup");
		}
		return false;
	}

	/**
	 * Gets rectificationAmount
	 * 
	 * @return rectificationAmount the rectificationAmount
	 */
	public String getRectificationAmount() {
		return rectificationAmount;
	}

	/**
	 * Sets rectificationAmount
	 * 
	 * @param rectificationAmount
	 *            the rectificationAmount to set
	 */
	public void setRectificationAmount(String rectificationAmount) {
		this.rectificationAmount = rectificationAmount;
	}

	/**
	 * Gets rectificationDate
	 * 
	 * @return rectificationDate the rectificationDate
	 */
	public Date getRectificationDate() {
		return rectificationDate;
	}

	/**
	 * Sets rectificationDate
	 * 
	 * @param rectificationDate
	 *            the rectificationDate to set
	 */
	public void setRectificationDate(Date rectificationDate) {
		this.rectificationDate = rectificationDate;
	}

	/**
	 * Gets rectificationAmountError
	 * 
	 * @return rectificationAmountError the rectificationAmountError
	 */
	public Boolean getRectificationAmountError() {
		return rectificationAmountError;
	}

	/**
	 * Sets rectificationAmountError
	 * 
	 * @param rectificationAmountError
	 *            the rectificationAmountError to set
	 */
	public void setRectificationAmountError(Boolean rectificationAmountError) {
		this.rectificationAmountError = rectificationAmountError;
	}

	/**
	 * Gets rectificationItemId
	 * 
	 * @return rectificationItemId the rectificationItemId
	 */
	public String getRectificationItemId() {
		return rectificationItemId;
	}

	/**
	 * Sets rectificationItemId
	 * 
	 * @param rectificationItemId
	 *            the rectificationItemId to set
	 */
	public void setRectificationItemId(String rectificationItemId) {
		this.rectificationItemId = rectificationItemId;
	}

	public Boolean getCanRectifySTC() {
		return this.isSTCMode() && this.durCompilation.getDurState() != null
				&& (this.durCompilation.getDurState().getId().equals(DurStateTypes.AGUEvaluation.getValue())
						|| this.durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue())
						|| this.durCompilation.getDurState().getId().equals(DurStateTypes.Certifiable.getValue())
						|| this.durCompilation.getDurState().getId().equals(DurStateTypes.Certified.getValue()));
	}

	public Boolean getCanRectifyAGU() {
		return this.isAGUMode() && this.durCompilation.getDurState() != null
				&& (this.durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue())
						|| this.durCompilation.getDurState().getId().equals(DurStateTypes.Certifiable.getValue())
						|| this.durCompilation.getDurState().getId().equals(DurStateTypes.Certified.getValue()));
	}

	public Boolean getCanRectifyACU() {
		return this.ACUMode && this.durCompilation.getDurState() != null
				&& (this.durCompilation.getDurState().getId().equals(DurStateTypes.ACUEvaluation.getValue()));
	}

	public String getShowSuspSaveMes() {
		return showSuspSaveMes;
	}

	public void setShowSuspSaveMes(String showSuspSaveMes) {
		this.showSuspSaveMes = showSuspSaveMes;
	}

	public Boolean getCostListChanged() {
		if (this.getViewState().containsKey("costListChanged")) {
			return (Boolean) (this.getViewState().get("costListChanged"));
		}
		return false;
	}

	public void setCostListChanged(Boolean costListChanged) {
		this.getViewState().put("costListChanged", costListChanged);
	}

	/**
	 * Gets filterPartnerValues
	 * 
	 * @return filterPartnerValues the filterPartnerValues
	 */
	public List<SelectItem> getFilterPartnerValues() {
		return filterPartnerValues;
	}

	/**
	 * Sets filterPartnerValues
	 * 
	 * @param filterPartnerValues
	 *            the filterPartnerValues to set
	 */
	public void setFilterPartnerValues(List<SelectItem> filterPartnerValues) {
		this.filterPartnerValues = filterPartnerValues;
	}

	/**
	 * Gets filterPhases
	 * 
	 * @return filterPhases the filterPhases
	 */
	public List<SelectItem> getFilterPhases() {
		return filterPhases;
	}

	/**
	 * Sets filterPhases
	 * 
	 * @param filterPhases
	 *            the filterPhases to set
	 */
	public void setFilterPhases(List<SelectItem> filterPhases) {
		this.filterPhases = filterPhases;
	}

	/**
	 * Gets filterPhase
	 * 
	 * @return filterPhase the filterPhase
	 */
	public Long getFilterPhase() {
		return (Long) this.getSession().get("filterPhase");
	}

	/**
	 * Sets filterPhase
	 * 
	 * @param filterPhase
	 *            the filterPhase to set
	 */
	public void setFilterPhase(Long filterPhase) {
		this.getSession().put("filterPhase", filterPhase);
	}

	public Long getFilterPartner() {
		if (getSession().containsKey("FilterPartner")) {
			return (Long) getSession().get("FilterPartner");
		}
		return null;
	}

	public void setFilterPartner(Long partnerID) {
		getSession().put("FilterPartner", partnerID);
	}

	public Long getSelectedSuspendReason() {
		if (getSession().containsKey("SelectedSuspendReason")) {
			return (Long) getSession().get("SelectedSuspendReason");
		}

		return null;
	}

	public void setSelectedSuspendReason(Long reasonId) {
		getSession().put("SelectedSuspendReason", reasonId);
	}

	/**
	 * Gets allReasonTypes
	 * 
	 * @return allReasonTypes the allReasonTypes
	 */
	public List<SelectItem> getAllReasonTypes() {
		return allReasonTypes;
	}

	/**
	 * Sets allReasonTypes
	 * 
	 * @param allReasonTypes
	 *            the allReasonTypes to set
	 */
	public void setAllReasonTypes(List<SelectItem> allReasonTypes) {
		this.allReasonTypes = allReasonTypes;
	}

	/**
	 * Gets filterCostItemValues
	 * 
	 * @return filterCostItemValues the filterCostItemValues
	 */
	public List<SelectItem> getFilterCostItemValues() {
		return filterCostItemValues;
	}

	/**
	 * Sets filterCostItemValues
	 * 
	 * @param filterCostItemValues
	 *            the filterCostItemValues to set
	 */
	public void setFilterCostItemValues(List<SelectItem> filterCostItemValues) {
		this.filterCostItemValues = filterCostItemValues;
	}

	public String getFilterCostDefID() {
		return (String) this.getSession().get("costDefID");
	}

	public void setFilterCostDefID(String costDefID) {
		this.getSession().put("costDefID", costDefID);
	}

	public void setFilterCostItem(String filterCostItem) {
		this.getSession().put("filterCostItem", filterCostItem);
	}

	public String getFilterCostItem() {
		return (String) this.getSession().get("filterCostItem");
	}

	public String getSessionFilterCostDefID() {
		if (getSession().containsKey(FILTER_COST_DEF_ID_VALUE)) {
			return (String) getSession().get(FILTER_COST_DEF_ID_VALUE);
		}
		return null;
	}

	public String getSessionFilterCostItem() {
		if (getSession().containsKey(FILTER_COST_ITEM)) {
			return (String) getSession().get(FILTER_COST_ITEM);
		}
		return null;
	}

	public Long getSessionFilterPhase() {
		if (getSession().containsKey(FILTER_PHASES_VALUE)) {
			return (Long) getSession().get(FILTER_PHASES_VALUE);
		}
		return null;
	}

	public Long getSessionFilterPartner() {
		if (getSession().containsKey(FILTER_PARTNER_VALUE)) {
			return (Long) getSession().get(FILTER_PARTNER_VALUE);
		}
		return null;
	}

	public Boolean getIsFiltredEditFromView() {
		if (getSession().containsKey(DUR_EDIT_FROM_VIEW)) {
			return (Boolean) getSession().get(DUR_EDIT_FROM_VIEW);
		}
		return false;
	}

	/**
	 * Gets userFesrAmount
	 * 
	 * @return userFesrAmount the userFesrAmount
	 */
	public Boolean getUserFesrAmount() {
		return userFesrAmount;
	}

	/**
	 * Sets userFesrAmount
	 * 
	 * @param userFesrAmount
	 *            the userFesrAmount to set
	 */
	public void setUserFesrAmount(Boolean userFesrAmount) {
		this.userFesrAmount = userFesrAmount;
	}

	/**
	 * Gets userItCnReimbursementAmount
	 * 
	 * @return userItCnReimbursementAmount the userItCnReimbursementAmount
	 */
	public Boolean getUserItCnReimbursementAmount() {
		return userItCnReimbursementAmount;
	}

	/**
	 * Sets userItCnReimbursementAmount
	 * 
	 * @param userItCnReimbursementAmount
	 *            the userItCnReimbursementAmount to set
	 */
	public void setUserItCnReimbursementAmount(Boolean userItCnReimbursementAmount) {
		this.userItCnReimbursementAmount = userItCnReimbursementAmount;
	}

	/**
	 * Gets userFrCnReimbursementAmount
	 * 
	 * @return userFrCnReimbursementAmount the userFrCnReimbursementAmount
	 */
	public Boolean getUserFrCnReimbursementAmount() {
		return userFrCnReimbursementAmount;
	}

	/**
	 * Sets userFrCnReimbursementAmount
	 * 
	 * @param userFrCnReimbursementAmount
	 *            the userFrCnReimbursementAmount to set
	 */
	public void setUserFrCnReimbursementAmount(Boolean userFrCnReimbursementAmount) {
		this.userFrCnReimbursementAmount = userFrCnReimbursementAmount;
	}

	/**
	 * Gets notesList
	 * 
	 * @return notesList the notesList
	 */
	public List<CostDefinitionsToNotes> getNotesList() {
		return notesList;
	}

	/**
	 * Sets notesList
	 * 
	 * @param notesList
	 *            the notesList to set
	 */
	public void setNotesList(List<CostDefinitionsToNotes> notesList) {
		this.notesList = notesList;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories() {
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SelectItem> categories) {
		DURCompilationEditBean.categories = categories;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public Boolean getCanGoEdit() {
		if (getSession().containsKey(CAN_EDIT_FROM_VIEW)) {
			return (Boolean) getSession().get(CAN_EDIT_FROM_VIEW);
		}
		return false;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners() {
		return listPartners;
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners) {
		this.listPartners = listPartners;
	}

	public void setSelectedCPartner(String selectedPartner) {
		this.getViewState().put("selectedCPartner", selectedPartner);
	}

	public String getSelectedCPartner() {
		return (String) this.getViewState().get("selectedCPartner");
	}

	public void setSelectedBPartner(String selectedPartner) {
		this.getViewState().put("selectedBPartner", selectedPartner);
	}

	public String getSelectedBPartner() {
		return (String) this.getViewState().get("selectedBPartner");
	}

	public void setSelectedUCPartner(String selectedPartner) {
		this.getViewState().put("selectedUCPartner", selectedPartner);
	}

	public Long getSelectedPartnerForGrid() {
		return (Long) this.getViewState().get("selectedPartnerForGrid");
	}

	public void setSelectedPartnerForGrid(Long selectedPartnerForGrid) {
		this.getViewState().put("selectedPartnerForGrid", selectedPartnerForGrid);
	}

	public Long getSelectedPartnerForGridWp() {
		return (Long) this.getViewState().get("selectedPartnerForGridWp");
	}

	public void setSelectedPartnerForGridWp(Long selectedPartnerForGridWp) {
		this.getViewState().put("selectedPartnerForGridWp", selectedPartnerForGridWp);
	}

	public List<SelectItem> getFilterPartnersForGrid() {
		return filterPartnersForGrid;
	}

	public void setFilterPartnersForGrid(List<SelectItem> filterPartnersForGrid) {
		this.filterPartnersForGrid = filterPartnersForGrid;
	}

	public String getSelectedUCPartner() {
		return (String) this.getViewState().get("selectedUCPartner");
	}

	public void setSelectedADCPartner(String selectedPartner) {
		this.getViewState().put("selectedADCPartner", selectedPartner);
	}

	public String getSelectedADCPartner() {
		return (String) this.getViewState().get("selectedADCPartner");
	}

	public void setSelectedSuspendPartner(String selectedPartner) {
		this.getViewState().put("selectedSuspendPartner", selectedPartner);
	}

	public String getSelectedSuspendPartner() {
		return (String) this.getViewState().get("selectedSuspendPartner");
	}

	public void setSelectedADGPartner(String selectedPartner) {
		this.getViewState().put("selectedADGPartner", selectedPartner);
	}

	public String getSelectedADGPartner() {
		return (String) this.getViewState().get("selectedADGPartner");
	}

	public Boolean getCanEditCategory() {
		if (getSessionBean().isSTC() || getSessionBean().isAGU() || getSessionBean().isACU()
				|| getSessionBean().isAGUW() || getSessionBean().isSTCW() || getSessionBean().isACUW()) {
			return true;
		}
		return false;
	}

	public void setActionMotivation(String actionMotivation) {
		this.actionMotivation = actionMotivation;
	}

	public String getActionMotivation() {
		return actionMotivation;
	}

	public void clearMotivation(ActionEvent event) {
		this.setActionMotivation("");
	}

	public void setShowSaveAttention(String show) {
		this.getViewState().put("ShowSaveAttention", show);
	}

	public String getShowSaveAttention() {
		return (String) this.getViewState().get("ShowSaveAttention");
	}

	public Integer getCustomFirstRow() {
		if (this.getCustomPaginator().getSelectedPage() < 1) {
			return this.getCustomPaginator().getSelectedPage() * this.getCustomPaginator().getItemsPerPageSelected();
		} else {
			return ((this.getCustomPaginator().getSelectedPage() - 1)
					* this.getCustomPaginator().getItemsPerPageSelected());
		}
	}

	/**
	 * Gets pageCosts
	 * 
	 * @return pageCosts the pageCosts
	 */
	public List<CostDefinitions> getPageCosts() {
		return pageCosts;
	}

	/**
	 * Sets pageCosts
	 * 
	 * @param pageCosts
	 *            the pageCosts to set
	 */
	public void setPageCosts(List<CostDefinitions> pageCosts) {
		this.pageCosts = pageCosts;
	}

	public void reloadPageData() {
		if (getListFilteredCostTable() != null && getListFilteredCostTable().size() > 0) {

			int pageCount = 0;
			if (getListFilteredCostTable() != null && getListFilteredCostTable().size() > 0) {
				pageCount = getListFilteredCostTable().size() / getCustomPaginator().getItemsPerPageSelected();

				if (getListFilteredCostTable().size() % getCustomPaginator().getItemsPerPageSelected() > 0) {
					pageCount += 1;
				}

				this.setPageCosts(getListFilteredCostTable().subList(getCustomFirstRow(),
						Math.min(getCustomFirstRow() + getCustomPaginator().getItemsPerPageSelected(),
								getListFilteredCostTable().size())));

			}

			this.getCustomPaginator().setPageCount(pageCount);

			this.getCustomPaginator().setPages(new ArrayList<Integer>());

			for (int i = 0; i < pageCount; ++i) {
				getCustomPaginator().getPages().add(i + 1);
			}

			this.setPageCosts(getListFilteredCostTable().subList(getCustomFirstRow(),
					Math.min(getCustomFirstRow() + getCustomPaginator().getItemsPerPageSelected(),
							getListFilteredCostTable().size())));

		}
	}

	/**
	 * Gets customPaginator
	 * 
	 * @return customPaginator the customPaginator
	 */
	public CustomPaginator getCustomPaginator() {
		if (this.getViewState().containsKey("customPaginator")) {
			return (CustomPaginator) getViewState().get("customPaginator");
		}
		return null;
	}

	/**
	 * Sets customPaginator
	 * 
	 * @param customPaginator
	 *            the customPaginator to set
	 */
	public void setCustomPaginator(CustomPaginator customPaginator) {
		this.getViewState().put("customPaginator", customPaginator);
	}

	/**
	 * Gets UCMode
	 * 
	 * @return UCMode the uCMode
	 */
	public boolean isUCMode() {
		return UCMode;
	}

	/**
	 * Sets UCMode
	 * 
	 * @param uCMode
	 *            the uCMode to set
	 */
	public void setUCMode(boolean uCMode) {
		UCMode = uCMode;
	}

	public Documents getUcDocument() {
		return ucDocument;
	}

	public void setUcDocument(Documents ucDocument) {
		this.ucDocument = ucDocument;
	}

	public UploadedFile getUcDocumentUpFile() {
		return ucDocumentUpFile;
	}

	public void setUcDocumentUpFile(UploadedFile ucDocumentUpFile) {
		this.ucDocumentUpFile = ucDocumentUpFile;
	}

	public String getDocUC() {
		return docUC;
	}

	public void setDocUC(String docUC) {
		this.docUC = docUC;
	}

	public Documents getAdcDocument() {
		return adcDocument;
	}

	public void setAdcDocument(Documents adcDocument) {
		this.adcDocument = adcDocument;
	}

	public Documents getAdgDocument() {
		return adgDocument;
	}

	public void setAdgDocument(Documents adgDocument) {
		this.adgDocument = adgDocument;
	}

	public UploadedFile getAdcDocumentUpFile() {
		return adcDocumentUpFile;
	}

	public void setAdcDocumentUpFile(UploadedFile adcDocumentUpFile) {
		this.adcDocumentUpFile = adcDocumentUpFile;
	}

	public UploadedFile getAdgDocumentUpFile() {
		return adgDocumentUpFile;
	}

	public void setAdgDocumentUpFile(UploadedFile adgDocumentUpFile) {
		this.adgDocumentUpFile = adgDocumentUpFile;
	}

	public String getDocADC() {
		return docADC;
	}

	public void setDocADC(String docADC) {
		this.docADC = docADC;
	}

	public String getDocADG() {
		return docADG;
	}

	public void setDocADG(String docADG) {
		this.docADG = docADG;
	}

	public void setCertifications(Map<Long, CertificationWrapper> cert) {
		this.getViewState().put("certifications", cert);
	}

	public Map<Long, CertificationWrapper> getCertifications() {
		return (Map<Long, CertificationWrapper>) this.getViewState().get("certifications");
	}

	private class CertificationWrapper {

		Double stcCert;

		Double acuCert;

		Double aguCert;

		public CertificationWrapper(CostDefinitions cd) {
			this.stcCert = cd.getStcCertification();
			this.acuCert = cd.getAcuCertification();
			this.aguCert = cd.getAguCertification();
		}

		/**
		 * Gets stcCert
		 * 
		 * @return stcCert the stcCert
		 */
		public Double getStcCert() {
			return stcCert;
		}

		/**
		 * Sets stcCert
		 * 
		 * @param stcCert
		 *            the stcCert to set
		 */
		public void setStcCert(Double stcCert) {
			this.stcCert = stcCert;
		}

		/**
		 * Gets acuCert
		 * 
		 * @return acuCert the acuCert
		 */
		public Double getAcuCert() {
			return acuCert;
		}

		/**
		 * Sets acuCert
		 * 
		 * @param acuCert
		 *            the acuCert to set
		 */
		public void setAcuCert(Double acuCert) {
			this.acuCert = acuCert;
		}

		/**
		 * Gets aguCert
		 * 
		 * @return aguCert the aguCert
		 */
		public Double getAguCert() {
			return aguCert;
		}

		/**
		 * Sets aguCert
		 * 
		 * @param aguCert
		 *            the aguCert to set
		 */
		public void setAguCert(Double aguCert) {
			this.aguCert = aguCert;
		}

	}

	public Double getTotalAdditionalFinansing() {
		return (Double) this.getViewState().get("totalAdditionalFinansing");
	}

	public void setTotalAdditionalFinansing(Double totalAdditionalFinansing) {
		this.getViewState().put("totalAdditionalFinansing", totalAdditionalFinansing);
	}

	public Double getTotalAdditionalFinansing2() {
		return (Double) this.getViewState().get("totalAdditionalFinansing2");
	}

	public void setTotalAdditionalFinansing2(Double totalAdditionalFinansing2) {
		this.getViewState().put("totalAdditionalFinansing2", totalAdditionalFinansing2);
	}

	public Double getZoneCurrentTotal() {
		return (Double) this.getViewState().get("zoneCurrentTotal");
	}

	public void setZoneCurrentTotal(Double zoneCurrentTotal) {
		this.getViewState().put("zoneCurrentTotal", zoneCurrentTotal);
	}

	public Double getZoneCurrentTotalFesr() {
		return (Double) this.getViewState().get("zoneCurrentTotalFesr");
	}

	public void setZoneCurrentTotalFesr(Double zoneCurrentTotalFesr) {
		this.getViewState().put("zoneCurrentTotalFesr", zoneCurrentTotalFesr);
	}

	public Double getZoneAllTotal() {
		return (Double) this.getViewState().get("zoneAllTotal");
	}

	public void setZoneAllTotal(Double zoneAllTotal) {
		this.getViewState().put("zoneAllTotal", zoneAllTotal);
	}

	public Double getZoneAllTotalFesr() {
		return (Double) this.getViewState().get("zoneAllTotalFesr");
	}

	public void setZoneAllTotalFesr(Double zoneAllTotalFesr) {
		this.getViewState().put("zoneAllTotalFesr", zoneAllTotalFesr);
	}

	public List<SelectItem> getFilterPartnersForGridWp() {
		return filterPartnersForGridWp;
	}

	public void setFilterPartnersForGridWp(List<SelectItem> filterPartnersForGridWp) {
		this.filterPartnersForGridWp = filterPartnersForGridWp;
	}

	public Boolean getFilterAdditionalFinansing() {
		return (Boolean) this.getViewState().get("filterAdditionalFinansing");
	}

	public void setFilterAdditionalFinansing(Boolean filterAdditionalFinansing) {
		this.getViewState().put("filterAdditionalFinansing", filterAdditionalFinansing);
	}

	public CostItemSummaryWrapper getTotalWrapper() {
		return totalWrapper;
	}

	public void setTotalWrapper(CostItemSummaryWrapper totalWrapper) {
		this.totalWrapper = totalWrapper;
	}

	public CostItemSummaryWrapper getCnItTotalWrapper() {
		return cnItTotalWrapper;
	}

	public void setCnItTotalWrapper(CostItemSummaryWrapper cnItTotalWrapper) {
		this.cnItTotalWrapper = cnItTotalWrapper;
	}

	public CostItemSummaryWrapper getCnFrTotalWrapper() {
		return cnFrTotalWrapper;
	}

	public void setCnFrTotalWrapper(CostItemSummaryWrapper cnFrTotalWrapper) {
		this.cnFrTotalWrapper = cnFrTotalWrapper;
	}

	public void setAsse3Mode(Boolean asse3Mode) {
		this.getViewState().put("asse3Mode", asse3Mode);
	}

	public Boolean getAsse3Mode() {
		return (Boolean) this.getViewState().get("asse3Mode");
	}

	public void setFilterCostPhaseAsse3(List<SelectItem> filterCostPhaseAsse3) {
		this.getViewState().put("filterCostPhaseAsse3", filterCostPhaseAsse3);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCostPhaseAsse3() {
		return (List<SelectItem>) this.getViewState().get("filterCostPhaseAsse3");
	}

	public void setFilterCostItemAsse3(List<SelectItem> filterCostItemAsse3) {
		this.getViewState().put("filterCostItemAsse3", filterCostItemAsse3);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCostItemAsse3() {
		return (List<SelectItem>) this.getViewState().get("filterCostItemAsse3");
	}

	public void setSelectedCostPhaseAsse3(String selectedCostPhaseAsse3) {
		this.getViewState().put("selectedCostPhaseAsse3", selectedCostPhaseAsse3);
	}

	public String getSelectedCostPhaseAsse3() {
		return (String) this.getViewState().get("selectedCostPhaseAsse3");
	}

	public void setSelectedCostItemAsse3(String selectedCostItemAsse3) {
		this.getViewState().put("selectedCostItemAsse3", selectedCostItemAsse3);
	}

	public String getSelectedCostItemAsse3() {
		return (String) this.getViewState().get("selectedCostItemAsse3");
	}

	public List<CostItemSummaryWrapper> getListSummaryAsse3() {
		return listSummaryAsse3;
	}

	public void setListSummaryAsse3(List<CostItemSummaryWrapper> listSummaryAsse3) {
		this.listSummaryAsse3 = listSummaryAsse3;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterPartners() {
		return (List<SelectItem>) this.getViewState().get("filterPartners");
	}

	public void setFilterPartners(List<SelectItem> filterPartners) {
		this.getViewState().put("filterPartners", filterPartners);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterExpenseItems() {
		return (List<SelectItem>) this.getViewState().get("filterExpenseItems");
	}

	public void setFilterExpenseItems(List<SelectItem> filterExpenseItems) {
		this.getViewState().put("filterExpenseItems", filterExpenseItems);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterExpenseItemsAsse3Mode() {
		return (List<SelectItem>) this.getViewState().get("filterExpenseItemsAsse3Mode");
	}

	public void setFilterExpenseItemsAsse3Mode(List<SelectItem> filterExpenseItemsAsse3Mode) {
		this.getViewState().put("filterExpenseItemsAsse3Mode", filterExpenseItemsAsse3Mode);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterWpsAsse3Mode() {
		return (List<SelectItem>) this.getViewState().get("filterWpsAsse3Mode");
	}

	public void setFilterWpsAsse3Mode(List<SelectItem> filterWpsAsse3Mode) {
		this.getViewState().put("filterWpsAsse3Mode", filterWpsAsse3Mode);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterWps() {
		return (List<SelectItem>) this.getViewState().get("filterWps");
	}

	public void setFilterWps(List<SelectItem> filterWps) {
		this.getViewState().put("filterWps", filterWps);
	}

	public String getFilterExpenseItem() {
		return (String) this.getViewState().get("filterExpenseItem");
	}

	public void setFilterExpenseItem(String filterExpenseItem) {
		this.getViewState().put("filterExpenseItem", filterExpenseItem);
	}

	public String getFilterExpenseItemAsse3Mode() {
		return (String) this.getViewState().get("filterExpenseItemAsse3Mode");
	}

	public void setFilterExpenseItemAsse3Mode(String filterExpenseItemAsse3Mode) {
		this.getViewState().put("filterExpenseItemAsse3Mode", filterExpenseItemAsse3Mode);
	}

	public Long getFilterWp() {
		return (Long) this.getViewState().get("filterWp");
	}

	public void setFilterWp(Long filterWp) {
		this.getViewState().put("filterWp", filterWp);
	}

	public String getFilterWpAsse3Mode() {
		return (String) this.getViewState().get("filterWpAsse3Mode");
	}

	public void setFilterWpAsse3Mode(String filterWpAsse3Mode) {
		this.getViewState().put("filterWpAsse3Mode", filterWpAsse3Mode);
	}

	public String getReportNumber() {
		return (String) this.getViewState().get("reportNumber");
	}

	public void setReportNumber(String reportNumber) {
		this.getViewState().put("reportNumber", reportNumber);
	}

	public String getFilterPartnerSearch() {
		return (String) this.getViewState().get("filterPartnerSearch");
	}

	public void setFilterPartnerSearch(String filterPartnerSearch) {
		this.getViewState().put("filterPartnerSearch", filterPartnerSearch);
	}

	public Boolean getNeedSaveSuspendDocument() {
		return (Boolean) this.getViewState().get("NeedSaveSuspendDocument");
	}

	public void setNeedSaveSuspendDocument(Boolean needSaveSuspendDocument) {
		this.getViewState().put("NeedSaveSuspendDocument", needSaveSuspendDocument);
	}

	public Documents getSuspendDocument() {
		return suspendDocument;
	}

	public void setSuspendDocument(Documents suspendDocument) {
		this.suspendDocument = suspendDocument;
	}

	public String getDocSuspend() {
		return docSuspend;
	}

	public void setDocSuspend(String docSuspend) {
		this.docSuspend = docSuspend;
	}

	public UploadedFile getSuspendDocumentUpFile() {
		return suspendDocumentUpFile;
	}

	public void setSuspendDocumentUpFile(UploadedFile suspendDocumentUpFile) {
		this.suspendDocumentUpFile = suspendDocumentUpFile;
	}

	public String getSuspendPercent() {
		return suspendPercent;
	}

	public void setSuspendPercent(String suspendPercent) {
		this.suspendPercent = suspendPercent;
	}

	public String getSelectedSuspendIds() {
		return selectedSuspendIds;
	}

	public void setSelectedSuspendIds(String selectedSuspendIds) {
		this.selectedSuspendIds = selectedSuspendIds;
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

	public Long getSelctedRetreatController() {
		return selctedRetreatController;
	}

	public void setSelctedRetreatController(Long selctedRetreatController) {
		this.selctedRetreatController = selctedRetreatController;
	}

	public Long getSelctedSuspendController() {
		return selctedSuspendController;
	}

	public void setSelctedSuspendController(Long selctedSuspendController) {
		this.selctedSuspendController = selctedSuspendController;
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

	public List<SelectItem> getAllRetReasonTypes() {
		return allRetReasonTypes;
	}

	public void setAllRetReasonTypes(List<SelectItem> allRetReasonTypes) {
		this.allRetReasonTypes = allRetReasonTypes;
	}

	public List<SelectItem> getAllRetControllerTypes() {
		return allRetControllerTypes;
	}

	public void setAllRetControllerTypes(List<SelectItem> allRetControllerTypes) {
		this.allRetControllerTypes = allRetControllerTypes;
	}

	public List<SelectItem> getAllSuspControllerTypes() {
		return allSuspControllerTypes;
	}

	public void setAllSuspControllerTypes(List<SelectItem> allSuspControllerTypes) {
		this.allSuspControllerTypes = allSuspControllerTypes;
	}

	public String getRetreatRole() {
		return (String) this.getViewState().get("selectedRetreatRole");
	}

	public void setRetreatRole(String entity) {
		this.getViewState().put("selectedRetreatRole", entity);
	}

	public String getDurRecoveryRole() {
		return (String) this.getViewState().get("selectedDurRecoveryRole");
	}

	public void setDurRecoveryRole(String entity) {
		this.getViewState().put("selectedDurRecoveryRole", entity);
	}

	public void setSelectedRetreatPartner(String selectedPartner) {
		this.getViewState().put("selectedDurRecoveryPartner", selectedPartner);
	}

	public String getSelectedRetreatPartner() {
		return (String) this.getViewState().get("selectedDurRecoveryPartner");
	}

	public void setSelectedDurRecoveryPartner(String selectedPartner) {
		this.getViewState().put("selectedRetreatPartner", selectedPartner);
	}

	public String getSelectedDurRecoveryPartner() {
		return (String) this.getViewState().get("selectedRetreatPartner");
	}

	public String getSuspendRetreat() {
		return suspendRetreat;
	}

	public void setSuspendRetreat(String suspendRetreat) {
		this.suspendRetreat = suspendRetreat;
	}

	public String getSelectedRetreatIds() {
		return selectedRetreatIds;
	}

	public void setSelectedRetreatIds(String selectedRetreatIds) {
		this.selectedRetreatIds = selectedRetreatIds;
	}

	/**
	 * @return the selectedRecoveryIds
	 */
	public String getSelectedRecoveryIds() {
		return selectedRecoveryIds;
	}

	/**
	 * @param selectedRecoveryIds
	 *            the selectedRecoveryIds to set
	 */
	public void setSelectedRecoveryIds(String selectedRecoveryIds) {
		this.selectedRecoveryIds = selectedRecoveryIds;
	}

	/**
	 * @return the selectedDurRecoveryIds
	 */
	public String getSelectedDurRecoveryIds() {
		return selectedDurRecoveryIds;
	}

	/**
	 * @param selectedDurRecoveryIds
	 *            the selectedDurRecoveryIds to set
	 */
	public void setSelectedDurRecoveryIds(String selectedDurRecoveryIds) {
		this.selectedDurRecoveryIds = selectedDurRecoveryIds;
	}

	/**
	 * @return the durRecoveryDate
	 */
	public Date getDurRecoveryDate() {
		return durRecoveryDate;
	}

	/**
	 * @param durRecoveryDate
	 *            the durRecoveryDate to set
	 */
	public void setDurRecoveryDate(Date durRecoveryDate) {
		this.durRecoveryDate = durRecoveryDate;
	}

	/**
	 * @return the durRecoveryItemId
	 */
	public String getDurRecoveryItemId() {
		return durRecoveryItemId;
	}

	/**
	 * @param durRecoveryItemId
	 *            the durRecoveryItemId to set
	 */
	public void setDurRecoveryItemId(String durRecoveryItemId) {
		this.durRecoveryItemId = durRecoveryItemId;
	}

	/**
	 * @return the durRecoveryAmountError
	 */
	public Boolean getDurRecoveryAmountError() {
		return durRecoveryAmountError;
	}

	/**
	 * @param durRecoveryAmountError
	 *            the durRecoveryAmountError to set
	 */
	public void setDurRecoveryAmountError(Boolean durRecoveryAmountError) {
		this.durRecoveryAmountError = durRecoveryAmountError;
	}

	/**
	 * @return the durRecoveryActNumber
	 */
	public String getDurRecoveryActNumber() {
		return durRecoveryActNumber;
	}

	/**
	 * @param durRecoveryActNumber
	 *            the durRecoveryActNumber to set
	 */
	public void setDurRecoveryActNumber(String durRecoveryActNumber) {
		this.durRecoveryActNumber = durRecoveryActNumber;
	}

	/**
	 * @return the durRecoveryActDate
	 */
	public Date getDurRecoveryActDate() {
		return durRecoveryActDate;
	}

	/**
	 * @param durRecoveryActDate
	 *            the durRecoveryActDate to set
	 */
	public void setDurRecoveryActDate(Date durRecoveryActDate) {
		this.durRecoveryActDate = durRecoveryActDate;
	}

	/**
	 * @return the durRecoveryAmount
	 */
	public String getDurRecoveryAmount() {
		return durRecoveryAmount;
	}

	/**
	 * @param durRecoveryAmount
	 *            the durRecoveryAmount to set
	 */
	public void setDurRecoveryAmount(String durRecoveryAmount) {
		this.durRecoveryAmount = durRecoveryAmount;
	}

	/**
	 * @return the durAmountOfPublicSupport
	 */
	public String getDurAmountOfPublicSupport() {
		return durAmountOfPublicSupport;
	}

	/**
	 * @param durAmountOfPublicSupport
	 *            the durAmountOfPublicSupport to set
	 */
	public void setDurAmountOfPublicSupport(String durAmountOfPublicSupport) {
		this.durAmountOfPublicSupport = durAmountOfPublicSupport;
	}

	/**
	 * @return the durTotalEligibleCost
	 */
	public String getDurTotalEligibleCost() {
		return durTotalEligibleCost;
	}

	/**
	 * @param durTotalEligibleCost
	 *            the durTotalEligibleCost to set
	 */
	public void setDurTotalEligibleCost(String durTotalEligibleCost) {
		this.durTotalEligibleCost = durTotalEligibleCost;
	}

	/**
	 * @return the durRecoveryNote
	 */
	public String getDurRecoveryNote() {
		return durRecoveryNote;
	}

	/**
	 * @param durRecoveryNote
	 *            the durRecoveryNote to set
	 */
	public void setDurRecoveryNote(String durRecoveryNote) {
		this.durRecoveryNote = durRecoveryNote;
	}

	/**
	 * @return the durRecoveryDocument
	 */
	public Documents getDurRecoveryDocument() {
		return durRecoveryDocument;
	}

	/**
	 * @param durRecoveryDocument
	 *            the durRecoveryDocument to set
	 */
	public void setDurRecoveryDocument(Documents durRecoveryDocument) {
		this.durRecoveryDocument = durRecoveryDocument;
	}

	/**
	 * @return the durRecoveryDocumentUpFile
	 */
	public UploadedFile getDurRecoveryDocumentUpFile() {
		return durRecoveryDocumentUpFile;
	}

	/**
	 * @param durRecoveryDocumentUpFile
	 *            the durRecoveryDocumentUpFile to set
	 */
	public void setDurRecoveryDocumentUpFile(UploadedFile durRecoveryDocumentUpFile) {
		this.durRecoveryDocumentUpFile = durRecoveryDocumentUpFile;
	}

	/**
	 * @return the selctedDurRecoveryReason
	 */
	public Long getSelctedDurRecoveryReason() {
		return selctedDurRecoveryReason;
	}

	/**
	 * @param selctedDurRecoveryReason
	 *            the selctedDurRecoveryReason to set
	 */
	public void setSelctedDurRecoveryReason(Long selctedDurRecoveryReason) {
		this.selctedDurRecoveryReason = selctedDurRecoveryReason;
	}

	/**
	 * @return the selctedDurRecoveryActType
	 */
	public Long getSelctedDurRecoveryActType() {
		return selctedDurRecoveryActType;
	}

	/**
	 * @param selctedDurRecoveryActType
	 *            the selctedDurRecoveryActType to set
	 */
	public void setSelctedDurRecoveryActType(Long selctedDurRecoveryActType) {
		this.selctedDurRecoveryActType = selctedDurRecoveryActType;
	}

	/**
	 * @return the allDurRecoveryReasonTypes
	 */
	public List<SelectItem> getAllDurRecoveryReasonTypes() {
		return allDurRecoveryReasonTypes;
	}

	/**
	 * @param allDurRecoveryReasonTypes
	 *            the allDurRecoveryReasonTypes to set
	 */
	public void setAllDurRecoveryReasonTypes(List<SelectItem> allDurRecoveryReasonTypes) {
		this.allDurRecoveryReasonTypes = allDurRecoveryReasonTypes;
	}

	/**
	 * @return the allDurRecoveryActTypes
	 */
	public List<SelectItem> getAllDurRecoveryActTypes() {
		return allDurRecoveryActTypes;
	}

	/**
	 * @param allDurRecoveryActTypes
	 *            the allDurRecoveryActTypes to set
	 */
	public void setAllDurRecoveryActTypes(List<SelectItem> allDurRecoveryActTypes) {
		this.allDurRecoveryActTypes = allDurRecoveryActTypes;
	}

	/**
	 * @return the docDurRecovery
	 */
	public String getDocDurRecovery() {
		return docDurRecovery;
	}

	/**
	 * @param docDurRecovery
	 *            the docDurRecovery to set
	 */
	public void setDocDurRecovery(String docDurRecovery) {
		this.docDurRecovery = docDurRecovery;
	}

	/**
	 * @return the durRecoverySuspendRetreat
	 */
	public String getDurRecoverySuspendRetreat() {
		return durRecoverySuspendRetreat;
	}

	/**
	 * @param durRecoverySuspendRetreat
	 *            the durRecoverySuspendRetreat to set
	 */
	public void setDurRecoverySuspendRetreat(String durRecoverySuspendRetreat) {
		this.durRecoverySuspendRetreat = durRecoverySuspendRetreat;
	}

	/**
	 * @return the totalAmountOld
	 */
	public Double getTotalAmountOld() {
		return totalAmountOld;
	}

	/**
	 * @param totalAmountOld
	 *            the totalAmountOld to set
	 */
	public void setTotalAmountOld(Double totalAmountOld) {
		this.totalAmountOld = totalAmountOld;
	}

	public Double getItCnReimbursement2() {
		return itCnReimbursement2;
	}

	public void setItCnReimbursement2(Double itCnReimbursement2) {
		this.itCnReimbursement2 = itCnReimbursement2;
	}

	public Double getFrCnReimbursement2() {
		return frCnReimbursement2;
	}

	public void setFrCnReimbursement2(Double frCnReimbursement2) {
		this.frCnReimbursement2 = frCnReimbursement2;
	}

	/**
	 * Sets error
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets error
	 * 
	 * @return error the error
	 */
	public String getError() {
		return error;
	}

	public boolean isSuspendedDur() {
		return suspendedDur;
	}

	public void setSuspendedDur(boolean suspendedDur) {
		this.suspendedDur = suspendedDur;
	}

	public boolean isRetreatDur() {
		return retreatDur;
	}

	public void setRetreatDur(boolean retreatDur) {
		this.retreatDur = retreatDur;
	}

	public boolean isRecoveredDur() {
		return recoveredDur;
	}

	public void setRecoveredDur(boolean recoveredDur) {
		this.recoveredDur = recoveredDur;
	}

	public Double getTotalSuspeded() {
//		return totalSuspeded;
		return (Double) this.getViewState().get("totalSuspeded");
	}

	public void setTotalSuspeded(Double totalSuspeded) {
//		this.totalSuspeded = totalSuspeded;
		this.getViewState().put("totalSuspeded", totalSuspeded);
	}

	public Double getTotalRetreat() {
//		return totalRetreat;
		return (Double) this.getViewState().get("totalRetreat");
	}

	public void setTotalRetreat(Double totalRetreat) {
//		this.totalRetreat = totalRetreat;
		this.getViewState().put("totalRetreat", totalRetreat);
	}

	public Double getTotalRecovered() {
//		return totalRecovered;
		return (Double) this.getViewState().get("totalRecovered");
	}

	public void setTotalRecovered(Double totalRecovered) {
//		this.totalRecovered = totalRecovered;
		this.getViewState().put("totalRecovered", totalRecovered);
	}

}
