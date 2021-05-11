package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.CostItemsTypology3Types;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.FesrCnType;
import com.infostroy.paamns.common.enums.HierarchicalLevelSelector;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PaymentCausality;
import com.infostroy.paamns.common.enums.PaymentType;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DocumentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentWays;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.converters.BaseConverter;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010. modified by Ingloba360
 * 
 */
public class CostDefinitionEditBean extends EntityProjectEditBean<CostDefinitions> {

	private final static String GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE = "CostDefinitionEditGiuridicalImmediateEngage";

	private static Integer entityDeleteId;

	private final static Double PERCENT_FERS_LIMIT = 0.2;

	private final static Double PERCENT_ELIGIBLE_AREA_AMOUNTS = 0.85;

	private String noteAdd;

	private int selectTab;

	private boolean isDocView;

	private static Integer entityDownloadId;

	private List<Documents> docList;

	private List<CostDefinitionsToNotes> notesList;

	private String costDocTitle;

	private UploadedFile costDocumentUpFile;

	private UploadedFile newDocumentUpFile;

	private Documents costDocument;

	private Documents newDocument;

	private String paymentDocTitle;

	private UploadedFile paymentDocumentUpFile;

	private Documents paymentDocument;

	private String paymentAttachmentDocTitle;

	private UploadedFile paymentAttachmentDocumentUpFile;

	private Documents paymentAttachmentDocument;

	private String error;

	private boolean isEdit;

	private static List<SelectItem> docTypes = new ArrayList<SelectItem>();

	private String docTitle;

	@SuppressWarnings("unused")
	private List<SelectItem> costItems = new ArrayList<SelectItem>();

	private String costTitle;

	private static List<SelectItem> paymentWays = new ArrayList<SelectItem>();

	private String paymentWayTitle;

	private static List<SelectItem> phases = new ArrayList<SelectItem>();

	private String phasesTitle;

	private static List<SelectItem> paymentTypes = new ArrayList<SelectItem>();

	private String paymentTypeTitle;

	private static List<SelectItem> categories;

	private String selectedCategory;

	private boolean validationFailed;

	private Boolean saveAsDraft;

	@SuppressWarnings("unused")
	private String paymentAmount;

	@SuppressWarnings("unused")
	private String amountReport;

	@SuppressWarnings("unused")
	private String publicAmountReport;

	@SuppressWarnings("unused")
	private String privateAmountReport;

	@SuppressWarnings("unused")
	private String amountToCoverAdvanceStateAid;

	private String actionMotivation;

	private static List<SelectItem> locations = new ArrayList<SelectItem>();

	private String locationSumLimitsError;

	private String locationLimitsErrorType;

	private boolean ignorelocationSumLimits = false;

	@SuppressWarnings("unused")
	private List<SelectItem> costItemsAsse3;

	@SuppressWarnings("unused")
	private List<SelectItem> costPhasesAsse3;

	@SuppressWarnings("unused")
	private Boolean asse3Mode;

	@SuppressWarnings("unused")
	private Boolean typology3Mode;

	private String asse3PhaseTitle;

	private String asse3CostItemsTitle;

	@SuppressWarnings("unused")
	private List<SelectItem> costPhasesTypology3;

	// private String selectedGiuridicalEngagesId;

	@SuppressWarnings("unused")
	private Boolean stateAid;

	@SuppressWarnings("unused")
	private Boolean expenditureOutsideEligibleAreas;

	@SuppressWarnings("unused")
	private Boolean expenditureForLandPurchase;

	@SuppressWarnings("unused")
	private Boolean inkindContributions;

	private boolean renderAmountToCoverAdvanceStateAid;

	private List<SelectItem> listPaymentTypeFn06;

	@SuppressWarnings("unused")
	private String paymentTypeCode;

	private List<SelectItem> listPaymentMotivation;

	private String paymentMotivationCode;

	private boolean cfpartnerPublic;

	private String fesrCnValue;

	private String transfersId;

	private List<SelectItem> transfers;

	/**
	 * Gets locationSumLimitsError
	 * 
	 * @return locationSumLimitsError the locationSumLimitsError
	 */
	public String getLocationSumLimitsError() {
		return locationSumLimitsError;
	}

	/**
	 * 
	 * @param locationSumLimitsError
	 *            the locationSumLimitsError to set
	 */
	public void setLocationSumLimitsError(String locationSumLimitsError) {
		this.locationSumLimitsError = locationSumLimitsError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave() {
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack() {
		if (this.getSession().get("costDefinitionViewBackToValidationFlow") != null) {
			this.getSession().put("costDefinitionViewBackToValidationFlow", null);
			goTo(PagesTypes.VALIDATIONFLOWVIEW);
		} else if (!(this.getSession().get("costDefinitionViewBackToDUREditFlow") == null
				&& this.getSession().get("costDefinitionViewBackToDUREditFlowShowAll") == null)) {
			this.getSession().put("costDefinitionViewBackToDUREditFlow", null);
			this.getSession().put("costDefinitionViewBackToDUREditFlowShowAll", null);
			this.getSession().put("costDefinitionViewToDUREdit", true);
			if (this.getPageIndex() != null) {
				this.getSession().put(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN, this.getPageIndex());
			}
			if (this.getSavedItemsPerPage() != null && !this.getSavedItemsPerPage().isEmpty()) {
				this.getSession().put(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP,
						Integer.parseInt(this.getSavedItemsPerPage()));
			}

			this.getSession().put(DURCompilationEditBean.DUR_COMPILATION_SAVE_FILTERS, Boolean.TRUE);

			goTo(PagesTypes.DURCOMPILATIONEDIT, this.getEntityId().toString());
		} else {
			if (this.getPageIndex() != null) {
				this.getSession().put(CostDefinitionListBean.SAVED_PAGE_INDEX, this.getPageIndex());
			}
			if (this.getSavedItemsPerPage() != null && !this.getSavedItemsPerPage().isEmpty()) {
				this.getSession().put(CostDefinitionListBean.SAVED_CDL_ITEMS_PER_PAGE, this.getSavedItemsPerPage());
			}
			goTo(PagesTypes.COSTDEFINITIONLIST);
		}
	}

	public void additionalFinansingAmountChange() {
		if (this.getAdditionalFinansingAmount() == null || this.getAdditionalFinansingAmount().trim().isEmpty()
				|| this.getAdditionalFinansingAmount().trim().equals("")) {
			this.setAdditionalFinansingAmount("0");
		}
		this.setAdditionalFinansingAmount(this.getAdditionalFinansingAmount().replaceAll(",", "."));
		this.setAmountReport(this.getAdditionalFinansingAmount());
		if (this.isCfpartnerPublic()) {
			this.setPublicAmountReport(this.getAdditionalFinansingAmount());
			this.setPrivateAmountReport("0");
		} else {
			this.setPublicAmountReport("0");
			this.setPrivateAmountReport(this.getAdditionalFinansingAmount());
		}
	}

	public void amountReportChange() throws ParseException {
		if (this.getAmountReport() == null || this.getAmountReport().trim().isEmpty()
				|| this.getAmountReport().trim().equals("")) {
			this.setAmountReport("0");
		}

		if (this.isCfpartnerPublic()) {
			this.setPrivateAmountReport("0");
			this.setPublicAmountReport(this.getAmountReport());
		} else {
			if (this.getFesrCnValue() != null) {
				Double sub = 0d;
				if (this.getFesrCnValue().equals(String.valueOf(FesrCnType.FESR85CN15.getValue()))) {
					this.setPublicAmountReport(String.valueOf(
							BaseConverter.convertToDoubleString(this.getValue(this.getAmountReport()) * 0.85d)));
					this.setPrivateAmountReport(String.valueOf(
							BaseConverter.convertToDoubleString(this.getValue(this.getAmountReport()) * 0.15d)));
				} else if (this.getFesrCnValue().equals(String.valueOf(FesrCnType.FESR50CN50.getValue()))) {
					this.setPublicAmountReport(String.valueOf(
							BaseConverter.convertToDoubleString(this.getValue(this.getAmountReport()) * 0.5d)));
					this.setPrivateAmountReport(String.valueOf(
							BaseConverter.convertToDoubleString(this.getValue(this.getAmountReport()) * 0.5d)));

				}

				sub = this.getValue(this.getAmountReport())
						- (this.getValue(this.getPublicAmountReport()) + this.getValue(this.getPrivateAmountReport()));
				if (!sub.equals(0)) {
					this.setPublicAmountReport(String.valueOf(
							BaseConverter.convertToDoubleString(this.getValue(this.getPublicAmountReport()) + sub)));
				}
			} else {
				setError(Utils.getString("CostDefinitionEditFesrCnNullValue") + " "
						+ Utils.getString("CFPartnerAnagraphCompletationsList"));
			}
		}
	}

	private Double getValue(String value) {
		if (value.contains(",")) {
			value = value.replace(',', '.');
		}
		return Double.valueOf(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException,
			PersistenceBeanException, PersistenceBeanException {
		if (this.getSession().containsKey(CostDefinitionListBean.SAVED_PAGE_INDEX)) {
			this.setPageIndex((Integer) this.getSession().get(CostDefinitionListBean.SAVED_PAGE_INDEX));
			this.getSession().remove(CostDefinitionListBean.SAVED_PAGE_INDEX);
		}

		if (this.getSession().containsKey(CostDefinitionListBean.SAVED_CDL_ITEMS_PER_PAGE)) {
			this.setSavedItemsPerPage((String) this.getSession().get(CostDefinitionListBean.SAVED_CDL_ITEMS_PER_PAGE));
			this.getSession().remove(CostDefinitionListBean.SAVED_CDL_ITEMS_PER_PAGE);
		}

		if (this.getSession().containsKey(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN)) {
			this.setPageIndex((Integer) this.getSession().get(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN));
			this.getSession().remove(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_PN);
		}

		if (this.getSession().containsKey(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP)) {
			this.setSavedItemsPerPage(
					this.getSession().get(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP).toString());
			this.getSession().remove(DURCompilationEditBean.DUR_COMPILATION_EDIT_SAVED_IPP);
		}

		if (this.getSession().containsKey(CostDefinitionListBean.CLONE_CD_ID)) {
			this.getSession().put("costDefinitionEdit", null);
			this.getSession().put("costDefinitionView", null);
			Long cloneId = (Long) this.getSession().get(CostDefinitionListBean.CLONE_CD_ID);
			this.getSession().remove(CostDefinitionListBean.CLONE_CD_ID);
			doClone(cloneId);
		}

		setIsEdit(this.getSession().get("costDefinitionView") == null);

		if (this.getSession().get("costDefinitionViewBackToDUREditFlow") != null) {
			setIsEdit(false);
			setIsDocView(true);
		} else if (this.getAAUvalidation()) {
			setIsDocView(false);
			setIsEdit(false);
		} else {
			setIsDocView(false);
		}
		costDocument = new Documents();
		costDocument.setEditableDate(new Date());
		if (this.getViewState().get("costdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("costdocument");

			setCostDocTitle(docinfo.getName());
			costDocument.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			costDocument.setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getCostDocument() != null) {
			setCostDocTitle(this.getEntity().getCostDocument().getName());
			costDocument.setTitle(this.getEntity().getCostDocument().getTitle());
			costDocument.setDocumentDate(this.getEntity().getCostDocument().getDocumentDate());
		} else {
			costDocument.setDocumentDate(new Date());
			setCostDocTitle("");
		}

		paymentDocument = new Documents();
		paymentDocument.setEditableDate(new Date());
		if (this.getViewState().get("paymentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentdocument");

			setPaymentDocTitle(docinfo.getName());
			paymentDocument.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			paymentDocument.setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getPaymentDocument() != null) {
			setPaymentDocTitle(this.getEntity().getPaymentDocument().getName());
			paymentDocument.setTitle(this.getEntity().getPaymentDocument().getTitle());
			paymentDocument.setDocumentDate(this.getEntity().getPaymentDocument().getDocumentDate());
		} else {
			paymentDocument.setDocumentDate(new Date());
			setPaymentDocTitle("");
		}

		paymentAttachmentDocument = new Documents();
		paymentAttachmentDocument.setEditableDate(new Date());
		if (this.getViewState().get("paymentattachmentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentattachmentdocument");

			setPaymentAttachmentDocTitle(docinfo.getName());
			paymentAttachmentDocument.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
			paymentAttachmentDocument.setDocumentDate(docinfo.getDate());
		} else if (this.getEntity().getPaymentAttachmentDocument() != null) {
			setPaymentAttachmentDocTitle(this.getEntity().getPaymentAttachmentDocument().getName());
			paymentAttachmentDocument.setTitle(this.getEntity().getPaymentAttachmentDocument().getTitle());
			paymentAttachmentDocument
					.setDocumentDate(this.getEntity().getPaymentAttachmentDocument().getDocumentDate());
		} else {
			paymentAttachmentDocument.setDocumentDate(new Date());
			setPaymentAttachmentDocTitle("");
		}

		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}

		setDocList(docs);
		newDocument = new Documents();
		newDocument.setDocumentDate(new Date());
		newDocument.setEditableDate(new Date());

		if (!this.isPostback()) {
			DecimalFormat dec = new DecimalFormat("###.##");
			if (this.getPaymentAmount() == null || this.getPaymentAmount().isEmpty()) {

				if (getEntity().getPaymentAmount() != null) {
					this.setPaymentAmount(String
							.valueOf(dec.format(Double.parseDouble(String.valueOf(getEntity().getPaymentAmount())))));
				} else {
					this.setPaymentAmount("");
				}
			}

			if (this.getAmountReport() == null || this.getAmountReport().isEmpty()) {

				if (getEntity().getAmountReport() != null) {
					this.setAmountReport(String
							.valueOf(dec.format(Double.parseDouble(String.valueOf(getEntity().getAmountReport())))));
				} else {
					this.setAmountReport("");
				}
			}

			if (this.getPublicAmountReport() == null || this.getPublicAmountReport().isEmpty()) {

				if (getEntity().getPublicAmountReport() != null) {
					this.setPublicAmountReport(String.valueOf(
							dec.format(Double.parseDouble(String.valueOf(getEntity().getPublicAmountReport())))));
				} else {
					this.setPublicAmountReport("");
				}
			}

			if (this.getPrivateAmountReport() == null || this.getPrivateAmountReport().isEmpty()) {

				if (getEntity().getPrivateAmountReport() != null) {
					this.setPrivateAmountReport(String.valueOf(
							dec.format(Double.parseDouble(String.valueOf(getEntity().getPrivateAmountReport())))));
				} else {
					this.setPrivateAmountReport("");
				}
			}

			if (this.getTransfersId() == null && this.getEntity().getAdditionalFesrInfo() != null) {
				this.setTransfersId(String.valueOf(this.getEntity().getAdditionalFesrInfo().getId()));
			}

			// if (this.getAdditionalFinansing() == null) {
			//
			// if (getEntity().getAdditionalFinansing() != null) {
			// this.setAdditionalFinansing(getEntity().getAdditionalFinansing());
			// } else {
			// this.setAdditionalFinansing(Boolean.FALSE);
			// }
			// }
			if (this.getAdditionalFinansing() == null) {
				if (getEntity().getAdditionalFinansing() != null) {
					this.setAdditionalFinansing(getEntity().getAdditionalFinansing());
				}
				/*
				 * else if (this.getAdditionalFinansingAmount() != null &&
				 * !this.getAdditionalFinansingAmount().isEmpty() &&
				 * !this.getAdditionalFinansing().equals("")) {
				 * this.setAdditionalFinansing(Boolean.TRUE); }
				 */else {
					this.setAdditionalFinansing(Boolean.FALSE);
				}
			}

			if (this.getAdditionalFinansingAmount() == null || this.getAdditionalFinansingAmount().isEmpty()) {
				if (getEntity().getAdditionalFinansingAmount() != null) {
					this.setAdditionalFinansingAmount(String.valueOf(getEntity().getAdditionalFinansingAmount()));

				} else {
					this.setAdditionalFinansingAmount("");
					// System.out.println(this.getAdditionalFinansingAmount());
				}
			}

			if (this.getExpenditureOutsideEligibleAreasAmount() == null
					|| this.getExpenditureOutsideEligibleAreasAmount().isEmpty()) {
				if (getEntity().getExpenditureOutsideEligibleAreasAmount() != null) {
					this.setExpenditureOutsideEligibleAreasAmount(
							String.valueOf(getEntity().getExpenditureOutsideEligibleAreasAmount()));
				} else {
					this.setExpenditureOutsideEligibleAreasAmount("");
				}
			}

			if (this.getInkindContributionsAmount() == null || this.getInkindContributionsAmount().isEmpty()) {
				if (getEntity().getInkindContributionsAmount() != null) {
					this.setInkindContributionsAmount(String.valueOf(getEntity().getInkindContributionsAmount()));
				} else {
					this.setInkindContributionsAmount("");
				}
			}

			if (this.getViewState().get("renderAmountToCoverAdvanceStateAid") == null) {
				this.setRenderAmountToCoverAdvanceStateAid(false);
			}

			if (this.getStateAid() == null) {
				if (getEntity().getStateAid() != null) {
					this.setStateAid(getEntity().getStateAid());
				} else {
					this.setStateAid(Boolean.FALSE);
				}
			}

			if (this.getAmountToCoverAdvanceStateAid() == null) {
				if (this.getEntity().getAmountToCoverAdvanceStateAid() != null) {
					this.setAmountToCoverAdvanceStateAid(
							String.valueOf(this.getEntity().getAmountToCoverAdvanceStateAid()));
				}
			}

			if (getSessionBean().isSuperAdmin() && !this.getEntity().isNew()) {
				HashMap<String, Object> oldValues = new HashMap<String, Object>();
				oldValues.put("docTitle", this.getEntity().getDocumentType().getValue());
				oldValues.put("motivations", this.getEntity().getMotivations());
				oldValues.put("documentNumber", this.getEntity().getDocumentNumber());
				oldValues.put("documentDate", this.getEntity().getDocumentDate());
				oldValues.put("documentCode", this.getEntity().getCfPivaVta());
				oldValues.put("issuesNames", this.getEntity().getIssuesNames());
				oldValues.put("documentAmount", this.getEntity().getDocumentAmount());
				oldValues.put("iva", this.getEntity().getIva());
				oldValues.put("retain", this.getEntity().getRetain());
				oldValues.put("amountProjectRelate", this.getEntity().getAmountProjectRelate());
				oldValues.put("externalCosts", this.getEntity().getExternalCosts());
				oldValues.put("costItem", this.getEntity().getCostItem().getValue());
				oldValues.put("phase", this.getEntity().getCostDefinitionPhase().getValue());
				oldValues.put("costNote", this.getEntity().getCostNote());
				oldValues.put("costDocId", this.getEntity().getCostDocument() != null
						? String.valueOf(this.getEntity().getCostDocument().getId()) : "");
				oldValues.put("costDocName",
						this.getEntity().getCostDocument() != null ? this.getEntity().getCostDocument().getName() : "");
				oldValues.put("paymentWay", this.getEntity().getPaymentWay().getValue());
				oldValues.put("paymentType", this.getEntity().getPaymentType().getValue());
				oldValues.put("paymentNumber", this.getEntity().getPaymentNumber());
				oldValues.put("paymentDate", this.getEntity().getPaymentDate());
				oldValues.put("paymentAmount", this.getEntity().getPaymentAmount());
				oldValues.put("paymentNotes", this.getEntity().getPaymentNotes());
				oldValues.put("paymentDocId", this.getEntity().getPaymentDocument() != null
						? String.valueOf(this.getEntity().getPaymentDocument().getId()) : "");
				oldValues.put("paymentDocName", this.getEntity().getPaymentDocument() != null
						? this.getEntity().getPaymentDocument().getName() : "");

				oldValues.put("paymentAttachmentDocId", this.getEntity().getPaymentAttachmentDocument() != null
						? String.valueOf(this.getEntity().getPaymentAttachmentDocument().getId()) : "");
				oldValues.put("paymentAttachmentDocName", this.getEntity().getPaymentAttachmentDocument() != null
						? this.getEntity().getPaymentAttachmentDocument().getName() : "");

				oldValues.put("amountReport", this.getEntity().getAmountReport());
				oldValues.put("publicAmountReport", this.getEntity().getPublicAmountReport());
				oldValues.put("privateAmountReport", this.getEntity().getPrivateAmountReport());

				oldValues.put("amountNotes", this.getEntity().getAmountNotes());
				this.getViewState().put("oldValues", oldValues);
			}
		}

		if ((this.getEntity() != null) && (this.getEntity().getPaymentMotivationCode() != null)) {
			this.setPaymentMotivationCode(this.getEntity().getPaymentMotivationCode());
		}

		if ((this.getPaymentTypeCode() == null) && (this.getEntity() != null)) {
			this.setPaymentTypeCode(this.getEntity().getPaymentTypeCode());
		}

		// this.fillPaymentTypeFn06();
		this.setPaymentTypeCode(PaymentType.PAYMENT.code);
		Map<String, String> paymentCausalityMap = PaymentCausality.getPaymentCausality(this.getPaymentTypeCode());
		this.setPaymentMotivationCode("SLD");

		if (this.getCurrentUser() != null) {
			CFPartnerAnagraphs cf = BeansFactory.CFPartnerAnagraphs()
					.GetByUser(Long.valueOf(this.getCurrentUser().getId().toString()));
			if (cf != null) {
				this.setCfpartnerPublic(cf.getPublicSubject());
				CFPartnerAnagraphProject cfProj = BeansFactory.CFPartnerAnagraphProject()
						.LoadByPartner(Long.parseLong(this.getProjectId()), cf.getId());
				if (cfProj != null) {
					CFPartnerAnagraphCompletations completation = BeansFactory.CFPartnerAnagraphCompletations()
							.GetByCFAnagraph(cfProj.getId(), this.getProjectId());
					if (completation != null) {
						this.setFesrCnValue(completation.getFesrCnValue());
					}
				}
			}

		}

		// this.fillPaymentMotivation();
	}

	public void processParams() {
		HttpServletRequest req = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest());
		if (req.getParameterMap().containsKey("view")) {
			this.getSession().put("costDefinitionEdit",
					Long.parseLong(((String[]) req.getParameterMap().get("view"))[0]));
			this.getSession().put("costDefinitionView",
					Long.parseLong(((String[]) req.getParameterMap().get("view"))[0]));
			this.getSession().put("costDefinitionViewBackToValidationFlow", null);
			this.getSession().put("costDefinitionViewBackToDUREditFlow", null);
		} else if (req.getParameterMap().containsKey("edit")) {
			this.getSession().put("costDefinitionEdit",
					Long.parseLong(((String[]) req.getParameterMap().get("edit"))[0]));
			this.getSession().put("costDefinitionView", null);
		} else if (req.getParameterMap().containsKey("clone")) {
			this.getSession().put("costDefinitionEdit", null);
			this.getSession().put("costDefinitionView", null);
			this.getSession().put("CLONE_CD_ID", Long.parseLong(((String[]) req.getParameterMap().get("clone"))[0]));
		}
	}

	public void doClone(Long originCostDefId) throws HibernateException, PersistenceBeanException {
		CostDefinitions originCD = BeansFactory.CostDefinitions().Load(originCostDefId);
		if (originCD.getDocumentType() != null) {
			this.getEntity().setDocumentType(originCD.getDocumentType());
		}
		this.getEntity().setMotivations(originCD.getMotivations());
		this.getEntity().setDocumentNumber(originCD.getDocumentNumber());
		this.getEntity().setDocumentDate(originCD.getDocumentDate());
		this.getEntity().setCfPivaVta(originCD.getCfPivaVta());
		this.getEntity().setIssuesNames(originCD.getIssuesNames());
		this.getEntity().setDocumentAmount(originCD.getDocumentAmount());
		this.getEntity().setIva(originCD.getIva());
		this.getEntity().setTotal(originCD.getTotal());
		this.getEntity().setRetain(originCD.getRetain());
		this.getEntity().setToPay(originCD.getToPay());
		this.getEntity().setAmountProjectRelate(originCD.getAmountProjectRelate());
		this.getEntity().setPaymentReciveDate(originCD.getPaymentReciveDate());
		this.getEntity().setCostDocument(originCD.getCostDocument());
		this.getEntity().setCostItem(originCD.getCostItem());
		this.getEntity().setCostDefinitionPhase(originCD.getCostDefinitionPhase());
		this.getEntity().setCostNote(originCD.getCostNote());
		this.getEntity().setPaymentWay(originCD.getPaymentWay());
		this.getEntity().setPaymentTypeCode(originCD.getPaymentTypeCode());
		this.getEntity().setPaymentMotivationCode(originCD.getPaymentMotivationCode());
		this.getEntity().setPaymentDocument(originCD.getPaymentDocument());
		this.getEntity().setPaymentAttachmentDocument(originCD.getPaymentAttachmentDocument());
		this.getEntity().setPaymentType(originCD.getPaymentType());
		this.getEntity().setPaymentNumber(originCD.getPaymentNumber());
		this.getEntity().setPaymentDate(originCD.getPaymentDate());
		this.getEntity().setPaymentAmount(originCD.getPaymentAmount());
		this.getEntity().setExternalCosts(originCD.getExternalCosts());
		this.getEntity().setPaymentNotes(originCD.getPaymentNotes());
		this.getEntity().setAmountReport(originCD.getAmountReport());
		this.getEntity().setPublicAmountReport(originCD.getPublicAmountReport());
		this.getEntity().setPrivateAmountReport(originCD.getPrivateAmountReport());
		this.getEntity().setAdditionalFinansing(originCD.getAdditionalFinansing());
		this.getEntity().setAdditionalFinansingAmount(originCD.getAdditionalFinansingAmount());
		this.getEntity().setExpenditureOutsideEligibleAreasAmount(originCD.getExpenditureOutsideEligibleAreasAmount());
		this.getEntity().setInkindContributionsAmount(originCD.getInkindContributionsAmount());
		this.getEntity().setAmountNotes(originCD.getAmountNotes());
		this.getEntity().setLocation(originCD.getLocation());
		this.setSelectedLocationId(String.valueOf(originCD.getLocation().ordinal()));
		this.getEntity().setReportNumber(originCD.getReportNumber());
		this.getEntity().setReportDate(originCD.getReportDate());
		this.getEntity().setCostItemPhaseAsse3(originCD.getCostItemPhaseAsse3());
		if (originCD.getGiuridicalEngages() != null) {
			this.setSelectedGiuridicalEngagesId(String.valueOf(originCD.getGiuridicalEngages().getId()));
		} else {
			this.setSelectedGiuridicalEngagesId(Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE));
		}

		// fill page fields
		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			if (this.getEntity().getCostItemPhaseAsse3() != null) {
				this.setAsse3CostItemsTitle(this.getEntity().getCostItemPhaseAsse3().getValue());
				this.setSelectedCostItemAsse3(this.getEntity().getCostItemPhaseAsse3().getValue());
				if (this.getEntity().getCostItemPhaseAsse3().getPhase() != null) {
					this.setSelectedPhaseAsse3(this.getEntity().getCostItemPhaseAsse3().getPhase().name());
					this.setAsse3PhaseTitle(this.getEntity().getCostItemPhaseAsse3().getPhase().toString());
				}
			}
			this.FillCostItemsAsse3();
		} else {
			this.setCostItem(this.getEntity().getCostItem().getId().toString());
			if (this.getEntity().getCostDefinitionPhase() != null) {
				this.setPhase(this.getEntity().getCostDefinitionPhase() != null
						? this.getEntity().getCostDefinitionPhase().getId().toString() : "");
			}
		}

		this.setDocType(this.getEntity().getDocumentType().getId().toString());
		if (this.getEntity().getPaymentType() != null) {
			this.setPaymentType(this.getEntity().getPaymentType() != null
					? this.getEntity().getPaymentType().getId().toString() : "");
		}
		if (this.getEntity().getPaymentWay() != null) {
			this.setPaymentWay(this.getEntity().getPaymentWay() != null
					? this.getEntity().getPaymentWay().getId().toString() : "");
		}

	}

	/**
	 *
	 */
	public void addNote() {
		if (this.getNoteAdd() != null && !this.getNoteAdd().isEmpty()) {
			if (getNotesList() == null) {
				setNotesList(new ArrayList<CostDefinitionsToNotes>());
			}

			getNotesList().add(new CostDefinitionsToNotes(
					this.getNoteAdd().length() > 255 ? this.getNoteAdd().substring(0, 254) : this.getNoteAdd(),
					new CostDefinitions()));
			setNoteAdd(null);

			try {
				if (!this.isEdit) {
					saveNotes(this.getEntity());
				}
				this.Page_Load();
			} catch (NumberFormatException e) {
				log.error("CostDefinitionEdit exception:", e);
			} catch (HibernateException e) {
				log.error("CostDefinitionEdit exception:", e);
			} catch (PersistenceBeanException e) {
				log.error("CostDefinitionEdit exception:", e);
			}
		}
	}

	public boolean getAAUvalidation() {
		if ((this.getEntity().getValidateByAAU() == null || this.getEntity().getValidateByAAU() == false)
				&& this.getSessionBean().isAAU() && !(this.getEntity().getIsSentToAAU() == null)
				&& this.getEntity().getIsSentToAAU()) {
			return true;
		}
		return false;
	}

	/**
	 *
	 */
	public void deleteCostDoc() {
		try {
			if (this.getViewState().get("costdocument") != null) {
				this.getViewState().put("costdocument", null);
			} else if (this.getEntity().getCostDocument() != null) {

				Documents doc = BeansFactory.Documents().Load(this.getEntity().getCostDocument().getId());
				this.getViewState().put("costdocumentToDel", doc != null ? doc.getId() : null);
				this.getEntity().setCostDocument(null);
			}

			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}
	}

	public void getCostDoc() {
		if (this.getViewState().get("costdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("costdocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getCostDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getCostDocument().getName(),
					this.getEntity().getCostDocument().getFileName(), null, null, null), false);
		}
	}

	public void deletePaymentDoc() {
		try {
			if (this.getViewState().get("paymentdocument") != null) {
				this.getViewState().put("paymentdocument", null);
			} else if (this.getEntity().getPaymentDocument() != null) {
				this.getViewState().put("paymentdocumentToDel",
						BeansFactory.Documents().Load(this.getEntity().getPaymentDocument().getId()).getId());
				this.getEntity().setPaymentDocument(null);
			}

			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}
	}

	public void getPaymentDoc() {
		if (this.getViewState().get("paymentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentdocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getPaymentDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getPaymentDocument().getName(),
					this.getEntity().getPaymentDocument().getFileName(), null, null, null), false);
		}
	}

	@SuppressWarnings("unchecked")
	public void downloadDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityDownloadId());

			FileHelper.sendFile(
					new DocumentInfo(doc.getDocumentDate(), doc.getName(), doc.getFileName(), null, null, null),
					doc.getId() == null);
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			docs.remove((int) this.getEntityDeleteId());
			this.getViewState().put("newDocument", docs);
			this.setSelectTab(3);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

		this.setSelectTab(3);
	}

	public void deletePaymentAttachmentDoc() {
		try {
			if (this.getViewState().get("paymentattachmentdocument") != null) {
				this.getViewState().put("paymentattachmentdocument", null);
			} else if (this.getEntity().getPaymentAttachmentDocument() != null) {
				this.getViewState().put("paymentattachmentdocumentToDel",
						BeansFactory.Documents().Load(this.getEntity().getPaymentAttachmentDocument().getId()).getId());
				this.getEntity().setPaymentAttachmentDocument(null);
			}

			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}
	}

	public void getPaymentAttachmentDoc() {
		if (this.getViewState().get("paymentattachmentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentattachmentdocument");

			FileHelper.sendFile(docinfo, true);
		} else if (this.getEntity().getPaymentAttachmentDocument() != null) {
			FileHelper.sendFile(new DocumentInfo(null, this.getEntity().getPaymentAttachmentDocument().getName(),
					this.getEntity().getPaymentAttachmentDocument().getFileName(), null, null, null), false);
		}
	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void FillLists() throws PersistenceBeanException {
		this.FillDocTypes();
		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			this.FillAsse3Phases();
			this.FillCostItemsAsse3();
		} else if (Boolean.TRUE.equals(this.getTypology3Mode())) {
			this.FillTypology3Phases();
			this.FillCostItemsTypology3();
		} else {
			this.FillCostItems();
			this.FillPhases();
		}

		this.FillTransfers();

		this.FillPaymentTypes();
		this.FillPaymentWays();

		this.fillRoles();
		this.fillCategories();
		this.fillLocations();
		this.FillGiuridicalEngages();
	}

	public void asse3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException {
		this.setSelectedPhaseAsse3((String) event.getNewValue());
		this.FillCostItemsAsse3();
	}

	public void typology3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException {
		this.setSelectedPhaseTypology3((String) event.getNewValue());
		this.FillCostItemsTypology3();
	}

	/**
	 * 
	 */
	private void fillLocations() {
		setLocations(new ArrayList<SelectItem>());
		getLocations().add(SelectItemHelper.getFirst());
		for (LocationForCostDef location : LocationForCostDef.values()) {

			getLocations().add(new SelectItem(String.valueOf(location.ordinal()), location.toString()));
		}
	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();
		categories.add(SelectItemHelper.getFirst());
		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	private void fillRoles() throws HibernateException, PersistenceBeanException {
		this.setListSelectRoles(new ArrayList<SelectItem>());
		this.getListSelectRoles().add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();

		List<UserRoles> roles = new ArrayList<UserRoles>();
		try {
			roles = UserRolesBean.GetByUser(String.valueOf(this.getCurrentUser().getId()));
		} catch (Exception e) {
			log.error("Exception whe getting userRoles", e);
		}
		for (UserRoles userRole : roles) {
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

	/**
	 * @param list
	 * @param costItem
	 * @return
	 */
	private double countSum(List<PartnersBudgets> list, int costItem) {
		double sum = 0d;
		switch (costItem) {
		case 1:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartHumanRes();
				}
			}
			break;
		case 2:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartProvisionOfService();
				}
			}
			break;
		case 3:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartMissions();
				}
			}
			break;
		case 4:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartDurables();
				}
			}
			break;
		case 5:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartDurableGoods();
				}
			}
			break;
		case 6:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartAdvInfoPublicEvents();
				}
			}
			break;
		case 7:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartAdvInfoProducts();
				}
			}
			break;
		case 8:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartOverheads();
				}
			}
			break;
		case 9:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartGeneralCosts();
				}
			}
			break;

		case 10:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartOther();
				}
			}
			break;

		case 11:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartContainer();
				}
			}
			break;

		case 12:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartPersonalExpenses();
				}
			}
			break;

		case 13:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartCommunicationAndInformation();
				}
			}
			break;

		case 14:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartOrganizationOfCommitees();
				}
			}
			break;

		case 15:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartOtherFees();
				}
			}
			break;

		case 16:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartAutoCoordsTunis();
				}
			}
			break;

		case 17:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartContactPoint();
				}
			}
			break;

		case 18:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartSystemControlAndManagement();
				}
			}
			break;

		case 19:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartAuditOfOperations();
				}
			}
			break;

		case 20:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartAuthoritiesCertification();
				}
			}
			break;

		case 21:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					sum += phase.getPartIntermEvaluation();
				}
			}
			break;

		}

		return NumberHelper.Round(sum);
	}

	@SuppressWarnings("unused")
	private double countSumWithPhase(List<PartnersBudgets> list, int costItem, Long phaseid) {
		double sum = 0d;
		switch (costItem) {
		case 1:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartHumanRes();
						break;
					}

				}
			}
			break;
		case 2:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartProvisionOfService();
						break;
					}
				}
			}
			break;
		case 3:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartMissions();
						break;
					}
				}
			}
			break;
		case 4:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartDurables();
						break;
					}
				}
			}
			break;
		case 5:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartDurableGoods();
						break;
					}
				}
			}
			break;
		case 6:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartAdvInfoPublicEvents();
						break;
					}
				}
			}
			break;
		case 7:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartAdvInfoProducts();
						break;
					}
				}
			}
			break;
		case 8:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartOverheads();
						break;
					}
				}
			}
			break;
		case 9:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartGeneralCosts();
						break;
					}
				}
			}
			break;

		case 10:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartOther();
						break;
					}
				}
			}
			break;

		case 11:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartContainer();
						break;
					}
				}
			}
			break;

		case 12:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartPersonalExpenses();
						break;
					}
				}
			}
			break;

		case 13:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartCommunicationAndInformation();
						break;
					}
				}
			}
			break;

		case 14:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartOrganizationOfCommitees();
						break;
					}
				}
			}
			break;

		case 15:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartOtherFees();
						break;
					}
				}
			}
			break;

		case 16:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartAutoCoordsTunis();
						break;
					}
				}
			}
			break;

		case 17:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartContactPoint();
						break;
					}
				}
			}
			break;

		case 18:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartSystemControlAndManagement();
						break;
					}
				}
			}
			break;

		case 19:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartAuditOfOperations();
						break;
					}
				}
			}
			break;

		case 20:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartAuthoritiesCertification();
						break;
					}
				}
			}
			break;

		case 21:
			for (PartnersBudgets item : list) {
				for (Phase phase : item.getPhaseBudgets()) {
					if (phase.getPhase().getId().equals(phaseid)) {
						sum += phase.getPartIntermEvaluation();
						break;
					}
				}
			}
			break;

		}

		return NumberHelper.Round(sum);
	}

	private boolean cointainSameYear(List<PartnersBudgets> list, PartnersBudgets item) {
		for (PartnersBudgets pb : list) {
			if (pb.getYear() == item.getYear()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void FillDocTypes() throws PersistenceBeanException {
		setDocTypes(new ArrayList<SelectItem>());
		List<DocumentTypes> list = BeansFactory.DocumentTypes().Load();
		getDocTypes().add(SelectItemHelper.getFirst());
		for (DocumentTypes item : list) {
			getDocTypes().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void FillCostItems() throws PersistenceBeanException {
		setCostItems(new ArrayList<SelectItem>());
		List<CostItems> list = BeansFactory.CostItems().Load();
		getCostItems().add(SelectItemHelper.getFirst());

		// Boolean flag = this.getProject().getAsse() > 2;

		for (CostItems item : list) {
			// System.out.println("item "+item.getCode());

			// if (flag) {
			// if (item.getId() > 10) {
			getCostItems().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
			// }

			// } else {
			// if (item.getId() <= 10) {
			// getCostItems().add(new SelectItem(String.valueOf(item.getId()),
			// item.getValue()));
			// }
			// }

		}
	}

	private void FillCostItemsAsse3() throws PersistenceBeanException {
		setCostItemsAsse3(new ArrayList<SelectItem>());
		getCostItemsAsse3().add(SelectItemHelper.getFirst());
		if (this.getSelectedPhaseAsse3() != null && !this.getSelectedPhaseAsse3().isEmpty()) {
			PhaseAsse3Types phase = null;
			try {
				phase = PhaseAsse3Types.valueOf(this.getSelectedPhaseAsse3());
			} catch (Exception e) {
				log.error("Enum is not compatible", e);
				return;
			}
			List<CostAsse3> costs = BeansFactory.CostAsse3().GetByPhase(phase);

			for (CostAsse3 cost : costs) {
				getCostItemsAsse3().add(new SelectItem(cost.getValue(), cost.getValue()));
			}

		}

	}

	private void FillCostItemsTypology3() throws PersistenceBeanException {
		setCostItemsTypology3(new ArrayList<SelectItem>());
		getCostItemsTypology3().add(SelectItemHelper.getFirst());

		for (CostItemsTypology3Types type : CostItemsTypology3Types.values()) {

			getCostItemsTypology3().add(new SelectItem(type.name(), type.toString()));
		}

	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void FillPaymentWays() throws PersistenceBeanException {
		setPaymentWays(new ArrayList<SelectItem>());
		List<PaymentWays> list = BeansFactory.PaymentWays().Load();
		getPaymentWays().add(SelectItemHelper.getFirst());
		for (PaymentWays item : list) {
			getPaymentWays().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	private void FillPhases() throws PersistenceBeanException {
		setPhases(new ArrayList<SelectItem>());
		List<CostDefinitionPhases> list = BeansFactory.CostDefinitionPhase().Load();
		getPhases().add(SelectItemHelper.getFirst());
		for (CostDefinitionPhases item : list) {

			getPhases().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	private void FillAsse3Phases() {
		setCostPhasesAsse3(new ArrayList<SelectItem>());
		getCostPhasesAsse3().add(SelectItemHelper.getFirst());
		for (PhaseAsse3Types type : PhaseAsse3Types.values()) {
			// if (type.equals(PhaseAsse3Types.CONTACT_POINT) ||
			// type.equals(PhaseAsse3Types.INTERMEDIATE_EVALUATION)) {
			// continue;
			// } never used
			getCostPhasesAsse3().add(new SelectItem(type.name(), type.toString()));
		}

	}

	private void FillTypology3Phases() {
		setCostPhasesTypology3(new ArrayList<SelectItem>());
		getCostPhasesTypology3().add(SelectItemHelper.getFirst());
		for (PhaseAsse3Types type : PhaseAsse3Types.values()) {

			getCostPhasesTypology3().add(new SelectItem(type.name(), type.toString()));
		}

	}

	private void FillTransfers() {
		setTransfers(new ArrayList<SelectItem>());
		getTransfers().add(SelectItemHelper.getFirst());
		List<AdditionalFesrInfo> list;
		try {
			CFPartnerAnagraphs partnerAnagraph = BeansFactory.CFPartnerAnagraphs()
					.GetByUser(this.getCurrentUser().getId());
			if (partnerAnagraph != null) {
				Long partnerId = partnerAnagraph.getId();
				list = BeansFactory.AdditionalFESRInfo().LoadByProjectCFPartnerAnagraphsAndTypeAndRole(
						String.valueOf(partnerId), null, this.getProjectId(), null);
				for (AdditionalFesrInfo item : list) {
					this.getTransfers().add(new SelectItem(String.valueOf(item.getId()), String.valueOf(item.getId())));
				}
			}

		} catch (HibernateException | NumberFormatException | PersistenceBeanException e) {

			e.printStackTrace();
		} // roleId);

	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void FillPaymentTypes() throws PersistenceBeanException {
		setPaymentTypes(new ArrayList<SelectItem>());
		List<PaymentTypes> list = BeansFactory.PaymentTypes().Load();
		getPaymentTypes().add(SelectItemHelper.getFirst());
		for (PaymentTypes item : list) {
			getPaymentTypes().add(new SelectItem(String.valueOf(item.getId()), item.getValue()));
		}
	}

	private void FillGiuridicalEngages() throws PersistenceBeanException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		setGiuridicalEngagesList(new ArrayList<SelectItem>());
		List<GiuridicalEngages> list = BeansFactory.GiuridicalEngages().getByProjectAndUserPartner(this.getProjectId(),
				this.getCurrentUser().getId());
		getGiuridicalEngagesList().add(SelectItemHelper.getFirst());
		for (GiuridicalEngages item : list) {
			getGiuridicalEngagesList()
					.add(new SelectItem(String.valueOf(item.getId()),
							String.valueOf(item.getActType() + " - " + item.getNumber() + " - "
									+ (item.getDate() != null ? sdf.format(item.getDate()) : "") + " - "
									+ item.getAmount())));
		}
		SelectItem ImmediateEngage = new SelectItem(Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE),
				Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE));
		getGiuridicalEngagesList().add(ImmediateEngage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		processParams();
		this.setShowAlert(Boolean.FALSE);
		this.setAsse3Mode(Boolean.valueOf(this.getProject().getAsse() == 4));
		this.setTypology3Mode(Boolean.valueOf(this.getProject().getTypology().getId() == 3)); //ingloba acagnoni new 2019/02
		if (!this.getSessionBean().getIsActualSate()) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}

		if (this.getSession().get("costDefinitionEdit") != null
				&& !String.valueOf(this.getSession().get("costDefinitionEdit")).isEmpty()) {
			this.setEntity(
					BeansFactory.CostDefinitions().Load(String.valueOf(this.getSession().get("costDefinitionEdit"))));
		}
		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}

		if (this.getEntity() != null && !this.getEntity().isNew()) {
			for (CostDefinitionsToDocuments item : BeansFactory.CostDefinitionsToDocuments()
					.getByCostDefinition(this.getEntity().getId())) {
				if (item.getDocument() != null) {
					docs.add(item.getDocument());
				}
			}
		}

		setDocList(docs);
		this.getViewState().put("newDocument", docs);

		this.FillLists();

		if (this.getSession().get("costDefinitionEdit") != null
				&& !String.valueOf(this.getSession().get("costDefinitionEdit")).isEmpty() && BeansFactory
						.CostDefinitions().Load(String.valueOf(this.getSession().get("costDefinitionEdit"))) != null) {
			this.setEntity(
					BeansFactory.CostDefinitions().Load(String.valueOf(this.getSession().get("costDefinitionEdit"))));
			if (this.getEntity().getDocumentType() != null) {
				this.setDocType(this.getEntity().getDocumentType().getId().toString());
			}
			if (this.getEntity().getLocation() != null) {
				this.setSelectedLocationId(String.valueOf(this.getEntity().getLocation().ordinal()));
			}

			if (this.getEntity().getGiuridicalEngages() != null) {
				this.setSelectedGiuridicalEngagesId(String.valueOf(this.getEntity().getGiuridicalEngages().getId()));
			} else {
				this.setSelectedGiuridicalEngagesId(Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE));
			}
			if (this.getEntity().getPaymentType() != null) {
				this.setPaymentType(this.getEntity().getPaymentType().getId().toString());
			}
			if (this.getEntity().getPaymentWay() != null) {
				this.setPaymentWay(this.getEntity().getPaymentWay().getId().toString());
			}
			if (Boolean.TRUE.equals(this.getAsse3Mode())) {
				if (this.getEntity().getCostItemPhaseAsse3() != null) {
					this.setSelectedCostItemAsse3(this.getEntity().getCostItemPhaseAsse3().getValue());
					this.setAsse3CostItemsTitle(this.getEntity().getCostItemPhaseAsse3().getValue());
					this.setSelectedPhaseAsse3(this.getEntity().getCostItemPhaseAsse3().getPhase().name());
					this.setAsse3PhaseTitle(this.getEntity().getCostItemPhaseAsse3().getPhase().toString());
					this.FillCostItemsAsse3();
				}
			} else {
				if (this.getEntity().getCostDefinitionPhase() != null) {
					this.setPhase(this.getEntity().getCostDefinitionPhase().getId().toString());
				}
				if (this.getEntity().getCostItem() != null) {
					this.setCostItem(this.getEntity().getCostItem().getId().toString());
				}
			}

			if (getNotesList() == null) {
				setNotesList(BeansFactory.CostDefinitionsToNotes().getByCostDefinition(this.getEntity().getId()));
			}
		} else {
			this.setEntity(new CostDefinitions());
			this.getEntity().setAmountReport(0d);
			this.getEntity().setPublicAmountReport(0d);
			this.getEntity().setPrivateAmountReport(0d);
			this.getEntity().setAdditionalFinansingAmount(0d);
			this.getEntity().setAmountToCoverAdvanceStateAid(0d);
		}

		this.setFailedValidation(new ArrayList<String>());

		if ((this.getEntity() != null) && (this.getEntity().getPaymentMotivationCode() != null)) {
			this.setPaymentMotivationCode(this.getEntity().getPaymentMotivationCode());
		} else {
			this.setPaymentMotivationCode((String) SelectItemHelper.getFirst().getValue());
		}
	}

	public boolean getCanSaveAsDraft() throws HibernateException, PersistenceBeanException {
		return this.getEntity() != null && (this.getEntity().isNew()
				|| (this.getEntity().getCostState() != null && this.getEntity().getCostState().getId()
						.equals(BeansFactory.CostStates().Load(CostStateTypes.Draft.getState()).getId())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#BeforeSave()
	 */
	public Boolean BeforeSave() {
		if (this.getFesrCnValue() == null) {
			setError(Utils.getString("CostDefinitionEditFesrCnNullValue") + " "
					+ Utils.getString("CFPartnerAnagraphCompletationsList"));
			return false;
		}

		if (this.getPaymentAmount() == null || this.getPaymentAmount().isEmpty()) {
			this.getEntity().setPaymentAmount(null);
		} else {
			try {

				this.getEntity().setPaymentAmount(Double.parseDouble(getPaymentAmount().replaceAll(",", ".")));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		if (this.getAmountReport() == null || this.getAmountReport().isEmpty()) {
			this.getEntity().setAmountReport(null);
		} else {
			try {
				this.getEntity().setAmountReport(Double.parseDouble(getAmountReport().replaceAll(",", ".")));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		if (this.getPublicAmountReport() == null || this.getPublicAmountReport().isEmpty()) {
			this.getEntity().setPublicAmountReport(null);
		} else {
			try {
				this.getEntity()
						.setPublicAmountReport(Double.parseDouble(getPublicAmountReport().replaceAll(",", ".")));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		if (this.getPrivateAmountReport() == null || this.getPrivateAmountReport().isEmpty()) {
			this.getEntity().setPrivateAmountReport(null);
		} else {
			try {
				this.getEntity()
						.setPrivateAmountReport(Double.parseDouble(getPrivateAmountReport().replaceAll(",", ".")));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		if (this.getAdditionalFinansingAmount() == null || this.getAdditionalFinansingAmount().isEmpty()) {
			this.getEntity().setAdditionalFinansingAmount(null);
		} else {
			try {
				this.getEntity().setAdditionalFinansingAmount(
						Double.parseDouble(getAdditionalFinansingAmount().replaceAll(",", ".")));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		this.validationFailed = false;

		checkEmpty("EditForm:tab1:doctypes", this.getDocType());
		checkEmpty("EditForm:tab1:motivations", this.getEntity().getMotivations());
		checkEmpty("EditForm:tab1:documentNumber", this.getEntity().getDocumentNumber());
		checkEmpty("EditForm:tab1:documentDate", this.getEntity().getDocumentDate());
		checkEmpty("EditForm:tab1:locations", this.getSelectedLocationId());
		checkEmpty("EditForm:tab1:impegno", this.getSelectedGiuridicalEngagesId());
		if (checkEmpty("EditForm:tab1:documentCode", this.getEntity().getCfPivaVta())) {
			checkEmptyFrom8And10And11And16("EditForm:tab1:documentCode", this.getEntity().getCfPivaVta());
		}
		checkEmpty("EditForm:tab1:issuesNames", this.getEntity().getIssuesNames());
		checkEmpty("EditForm:tab1:documentAmount", this.getEntity().getDocumentAmount());
		checkEmpty("EditForm:tab1:iva", this.getEntity().getIva());
		checkEmpty("EditForm:tab1:amountProjectRelate", this.getEntity().getAmountProjectRelate());
		if (!checkEmpty("EditForm:tab1:costDocLink", this.getCostDocTitle(), this.getSaveAsDraft())) {
			this.getContext().getViewRoot().findComponent("EditForm:tab1:doc1link").getAttributes().put("style",
					"color: #E00000;");
		}

		checkEmpty("EditForm:tab3:amountReport", this.getEntity().getAmountReport());
		checkEmpty("EditForm:tab3:publicAmountReport", this.getEntity().getPublicAmountReport());
		checkEmpty("EditForm:tab3:privateAmountReport", this.getEntity().getPrivateAmountReport());
		checkEmpty("EditForm:tab2:paymentWay", this.getPaymentWay(), this.getSaveAsDraft());
		checkEmpty("EditForm:tab2:paymentTypesFn06", this.getPaymentTypeCode());
		checkEmpty("EditForm:tab2:paymentMotivations", this.getPaymentMotivationCode());
		checkEmpty("EditForm:tab2:paymentReciveDate", this.getEntity().getPaymentReciveDate(), this.getSaveAsDraft());
		checkEmpty("EditForm:tab2:paymentNumber", this.getEntity().getPaymentNumber(), this.getSaveAsDraft());
		checkEmpty("EditForm:tab2:paymentDate", this.getEntity().getPaymentDate(), this.getSaveAsDraft());
		checkEmpty("EditForm:tab2:paymentAmount", this.getEntity().getPaymentAmount(), this.getSaveAsDraft());
		if (!checkEmpty("EditForm:tab2:doc2link", this.getPaymentDocTitle(), this.getSaveAsDraft())) {
			this.getContext().getViewRoot().findComponent("EditForm:tab2:doc2link").getAttributes().put("style",
					"color: #E00000;");
		}
		if (!checkEmpty("EditForm:tab2:doc4link", this.getPaymentAttachmentDocTitle(), this.getSaveAsDraft())) {
			this.getContext().getViewRoot().findComponent("EditForm:tab2:doc4link").getAttributes().put("style",
					"color: #E00000;");
		}
		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			checkEmpty("EditForm:tab3:phaseAsse3", this.getSelectedPhaseAsse3());
			checkEmpty("EditForm:tab3:costItemAsse3", this.getSelectedCostItemAsse3());
		} else {
			checkEmpty("EditForm:tab3:phase", this.getPhase());
			checkEmpty("EditForm:tab3:costItem", this.getCostItem());
		}
		if (!this.getSaveAsDraft()) {
			checkEmpty("EditForm:tab3:numberReport", this.getEntity().getReportNumber());
			checkEmpty("EditForm:tab3:reportDate", this.getEntity().getReportDate());
		}

		// if (this.getAmountReport() != null) {
		// checkComponentsOfAmountReport("EditForm:tab3:amountReport",
		// this.getEntity().getAmountReport());
		// }

		if (this.getStateAid() != null && this.getStateAid()) {
			checkEmpty("EditForm:tab3:amountToCoverAdvanceStateAid", this.getAmountToCoverAdvanceStateAid());
		}

		if (this.validationFailed) {
			return false;
		}

		if (Boolean.FALSE.equals(this.getAsse3Mode())) {

			if (!this.getSaveAsDraft()) {
				double sum = 0d;
				this.getEntity()
						.setTotal(NumberHelper.Round(this.getEntity().getDocumentAmount() + this.getEntity().getIva()));

				this.getEntity()
						.setToPay(NumberHelper.Round(this.getEntity().getTotal() - this.getEntity().getRetain()));

				try {
					List<CostDefinitions> list = BeansFactory.CostDefinitions().GetByValues(this.getProjectId(),
							this.getEntity().getDocumentNumber(), this.getEntity().getDocumentDate(),
							this.getEntity().getCfPivaVta(), this.getEntity().getId(), this.getCurrentUser().getId());
					if (list != null && !list.isEmpty()) {

						this.getEntity().setAmountProjectRelate(list.get(0).getAmountProjectRelate());

						sum = 0d;
						for (CostDefinitions item : list) {
							if (item.getDismiss().equals(true)) {
								continue;
							}
							if (item.getAcuCertification() != null) {
								sum += item.getAcuCertification();
							} else if (item.getAmountReport() != null) {
								sum += item.getAmountReport();
							}
						}

						if (this.getEntity().getAcuCertification() == null) {
							sum += this.getEntity().getAmountReport();
						} else {
							sum += this.getEntity().getAcuCertification();
						}

						if (NumberHelper.Round(sum) > this.getEntity().getAmountProjectRelate()) {
							setError(Utils.getString("validator.amountProjectRelateLess"));
							return false;
						}
					}
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				if (this.getEntity().getAmountProjectRelate() > this.getEntity().getTotal()) {
					setError(Utils.getString("validator.amountProjectRelateLessTotal"));
					return false;
				}

				sum = 0d;

				if (Boolean.FALSE.equals(this.getAdditionalFinansing())) {

					try {
						List<PartnersBudgets> list = null;
						CFPartnerAnagraphs partner = null;
						if (this.getSessionBean().isCF()) {
							partner = BeansFactory.CFPartnerAnagraphs().GetByUser(this.getCurrentUser().getId());
						} else if (this.getSessionBean().isPartner()) {
							partner = BeansFactory.CFPartnerAnagraphs().GetByUser(this.getCurrentUser().getId());
						} else if (this.getSessionBean().isAGU()) {
							partner = getAguPartnerForThisProject();
						} else if (this.getSessionBean().isSuperAdmin()) {
							if (this.getProjectAsse() == 5
									&& hasUserRole(this.getEntity().getUser(), UserRoleTypes.AGU)) {
								partner = getAguPartnerForThisProject();
							} else {
								partner = BeansFactory.CFPartnerAnagraphs()
										.GetByUser(this.getEntity().getUser().getId());
							}
						}

						if (partner == null) {
							return false;
						}

						list = BeansFactory.PartnersBudgets().GetByProjectAndPartner(this.getProjectId(),
								String.valueOf(partner.getId()), true);
						for (PartnersBudgets item : list) {
							item.setPhaseBudgets(
									BeansFactory.PartnersBudgetsPhases().loadByPartnerBudget(item.getId()));
						}

						if (list != null && list.size() > 0 && list.get(0).getApproved() != null
								&& list.get(0).getApproved()) {
							sum += countSum(list, Integer.parseInt(this.getCostItem()));
						} else if (list.size() == 0) {

						} else if (list.get(0).getApproved() == null || !list.get(0).getApproved()) {
							List<PartnersBudgets> list2 = BeansFactory.PartnersBudgets().GetByProjectPartner(
									this.getProjectId(), String.valueOf(partner.getId()), false, true);
							List<PartnersBudgets> list3 = new ArrayList<PartnersBudgets>();

							for (PartnersBudgets item : list2) {
								if (item.getApproved() != null && item.getApproved()
										&& !cointainSameYear(list3, item)) {
									list3.add(item);
								}
							}

							if (list3.size() == 0) {
								sum += countSum(list, Integer.parseInt(this.getCostItem()));
							} else {
								double sum1 = countSum(list, Integer.parseInt(this.getCostItem()));
								double sum2 = countSum(list3, Integer.parseInt(this.getCostItem()));
								sum += sum1 < sum2 ? sum1 : sum2;
							}

						}

						List<CostDefinitions> listByCostItem = new ArrayList<CostDefinitions>();

						if (this.getSessionBean().isCF() || this.getSessionBean().isPartner()) {
							listByCostItem = BeansFactory.CostDefinitions().GetByValuesPhase(this.getProjectId(),
									this.getCostItem(), this.getEntity().getId(), this.getCurrentUser().getId());
						} else if (this.getSessionBean().isAGU()) {
							listByCostItem = BeansFactory.CostDefinitions().GetAgusPhase(this.getProjectId(),
									this.getCostItem(), this.getEntity().getId());
						}

						double sumA = 0d;
						for (CostDefinitions item : listByCostItem) {
							if (Boolean.TRUE.equals(item.getAdditionalFinansing())) {
								continue;
							}
							if (item.getCostState() != null
									&& item.getCostState()
											.equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
									&& !item.getWasRefusedByCil() || item.getDismiss().equals(true)) {
								continue;
							}
							if (item.getAcuCertification() != null
									// && item.getAcuCertification() > 0
									&& item.getACUSertified()) {
								sumA += item.getAcuCertification();
							} else if (item.getAguCertification() != null
									// && item.getAguCertification() > 0
									&& item.getAGUSertified()) {
								sumA += item.getAguCertification();
							} else if (item.getStcCertification() != null
									// && item.getStcCertification() > 0
									&& item.getSTCSertified()) {
								sumA += item.getStcCertification();
							} else if (item.getCfCheck() != null
							// && item.getCfCheck()
							// > 0
							) {
								sumA += item.getCfCheck();
							} else if (item.getCilCheck() != null
									// && item.getCilCheck()
									// > 0

									&& !item.getCilIntermediateStepSave()) {
								sumA += item.getCilCheck();
							} else {
								sumA += (item.getAmountReport() != null ? item.getAmountReport() : 0d);
							}
						}
						sumA += this.getEntity().getAmountReport();
						if (NumberHelper.Round(sumA) > NumberHelper.Round(sum)) {
							setError(Utils.getString("validator.amountReportToCostItem"));
							return false;
						}
					} catch (HibernateException e) {
						log.error("CostDefinitionEdit exception:", e);
					} catch (NumberFormatException e) {
						log.error("CostDefinitionEdit exception:", e);
					} catch (PersistenceBeanException e) {
						log.error("CostDefinitionEdit exception:", e);
					}
				}
				if (this.validationFailed == false && this.ignorelocationSumLimits == false) {
					try {
						validateLocationLimits();
						if (this.validationFailed == true) {
							return false;
						}
					} catch (Exception e1) {
						log.error("CostDefinitionEdit exception:", e1);
						return false;
					}
				}
			} else {
				try {
					double sum = 0d;
					List<CostDefinitions> list = BeansFactory.CostDefinitions().GetByValues(this.getProjectId(),
							this.getEntity().getDocumentNumber(), this.getEntity().getDocumentDate(),
							this.getEntity().getCfPivaVta(), this.getEntity().getId(), this.getCurrentUser().getId());
					if (list != null && !list.isEmpty()) {

						this.getEntity().setAmountProjectRelate(list.get(0).getAmountProjectRelate());

						sum = 0d;
						for (CostDefinitions item : list) {
							if (item.getDismiss().equals(true)) {
								continue;
							}
							if (item.getAcuCertification() != null) {
								sum += item.getAcuCertification();
							} else if (item.getAmountReport() != null) {
								sum += item.getAmountReport();
							}
						}

						if (this.getEntity().getAcuCertification() == null) {
							sum += this.getEntity().getAmountReport();
						} else {
							sum += this.getEntity().getAcuCertification();
						}

						if (NumberHelper.Round(sum) > this.getEntity().getAmountProjectRelate()) {
							setError(Utils.getString("validator.amountProjectRelateLess"));
							return false;
						}
					}
				} catch (PersistenceBeanException e) {
					log.error(e);
				}

				if (this.getEntity().getTotal() != null) {
					if (this.getEntity().getAmountProjectRelate() > this.getEntity().getTotal()) {
						setError(Utils.getString("validator.amountProjectRelateLessTotal"));
						return false;
					}
				}

			}
		}
		if (this.getSelectedGiuridicalEngagesId() != null && !Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE)
				.equals(this.getSelectedGiuridicalEngagesId())) {
			try {
				Double summ = 0d;

				List<CostDefinitions> cdList = BeansFactory.CostDefinitions()
						.GetByGuiridicalEngage(Long.valueOf(this.getSelectedGiuridicalEngagesId()));
				for (CostDefinitions cd : cdList) {
					if (!this.getEntity().isNew()) {
						if (cd.getId().equals(this.getEntity().getId())) {
							continue;
						}
					}

					if (cd.getAmountReport() != null) {
						summ += cd.getAmountReport();
					}

				}
				if (this.getEntity().getAmountReport() != null) {
					summ += this.getEntity().getAmountReport();
				}

				GiuridicalEngages ge = BeansFactory.GiuridicalEngages().Load(this.getSelectedGiuridicalEngagesId());
				if (ge.getAmount() == null || ge.getAmount() < NumberHelper.Round(summ)) {
					setError(Utils.getString("validator.giuridicalImpegnoMoreThen"));
					return false;
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}
		}
		if (this.getEntity().getPaymentAmount() != null && this.getEntity().getAmountReport() != null) {
			double minor = this.getEntity().getPaymentAmount() < this.getEntity().getAmountProjectRelate()
					? this.getEntity().getPaymentAmount() : this.getEntity().getAmountProjectRelate();

			if (this.getEntity().getAmountReport() > NumberHelper.Round(minor)) {
				setError(Utils.getString("validator.amountReportLessThan"));
				return false;
			}
		}
		if (Boolean.TRUE.equals(this.getAdditionalFinansing())) {
			try {
				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs().GetByUser(this.getCurrentUser().getId());

				if (partner == null) {
					return false;
				}

				GeneralBudgets budgets = BeansFactory.GeneralBudgets().GetActualItemForCFAnagraph(this.getProjectId(),
						partner.getId());

				List<CostDefinitions> costs = BeansFactory.CostDefinitions()
						.GetByPartnerAdditionalFinansing(this.getProjectId(), partner.getUser().getId());

				Double summAddedCosts = 0d;

				for (CostDefinitions cost : costs) {
					if (cost.getAmountReport() != null) {
						summAddedCosts += cost.getAmountReport();
					}
				}
				if (this.getAmountReport() != null && !this.getAmountReport().isEmpty()) {
					summAddedCosts += Double.parseDouble(this.getAmountReport());
				}

				if (NumberHelper.Round(summAddedCosts) > NumberHelper.Round(budgets.getQuotaPrivate())) {
					if (!this.getShowAlert()) {
						this.setShowAlert(Boolean.TRUE);
						return false;
					}
				}

			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}

		}

		if (this.getExpenditureOutsideEligibleAreasAmount() == null
				|| this.getExpenditureOutsideEligibleAreasAmount().isEmpty()
				|| this.getExpenditureOutsideEligibleAreasAmount().equals("0.0")) {
			this.getEntity().setExpenditureOutsideEligibleAreasAmount(0d);
		} else {
			try {
				Double oldAmounts = this.calculateCostInProjectForTotalExpendituresOutsideEligibleAreas();
				Double partialAmounts = (Double.valueOf(this.getExpenditureOutsideEligibleAreasAmount()) + oldAmounts)
						* PERCENT_ELIGIBLE_AREA_AMOUNTS;
				boolean valid = partialAmounts < (loadProjectFesr() * PERCENT_FERS_LIMIT);
				if (valid == false) {
					setError(Utils.getString("validator.OutsideAmountGreaterThan"));
					return false;
				}
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
				return false;
			}
		}

		this.setShowAlert(Boolean.FALSE);
		return true;
	}

	public void cancelWarningDialog() {
		this.setShowAlert(Boolean.FALSE);
	}

	public void selectStateAid(ValueChangeEvent event)
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getStateAid() != null) {
			if (this.getStateAid()) {
				this.setStateAid(false);
			} else {
				this.setStateAid(true);
			}
		}
		if (this.getStateAid() == null) {
			this.setRenderAmountToCoverAdvanceStateAid(false);
		}
	}

	/**
	 * @param user
	 * @param userRoleType
	 * @return
	 * @throws PersistenceBeanException
	 */
	private boolean hasUserRole(Users user, UserRoleTypes userRoleType) throws PersistenceBeanException {
		Roles roleByName = BeansFactory.Roles().GetRoleByName(userRoleType);
		for (UserRoles role : user.getRoles()) {
			if (roleByName.equals(role.getRole()))
				return true;
		}
		return false;
	}

	/**
	 * @param partner
	 * @return
	 * @throws PersistenceBeanException
	 */
	private CFPartnerAnagraphs getAguPartnerForThisProject() throws PersistenceBeanException {
		List<CFPartnerAnagraphs> listCFP = BeansFactory.CFPartnerAnagraphs()
				.GetCFAnagraphForProject(this.getProjectId(), CFAnagraphTypes.CFAnagraph);
		if (listCFP != null && !listCFP.isEmpty()) {
			return listCFP.get(0);
		}
		return null;
	}

	public void checkEmptyFrom8And10And11And16(String id, Object value) {
		if (((String) value).length() != 11 && ((String) value).length() != 16 && ((String) value).length() != 8
				&& ((String) value).length() != 10) {
			this.validationFailed = true;
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validator.codeLenth"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
		}

	}

	private boolean checkEmpty(String id, Object value) {
		return checkEmpty(id, value, false);
	}

	private boolean checkEmpty(String id, Object value, boolean skip) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (!skip && (str == null || str.isEmpty() || str.trim().isEmpty())) {
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

	/**
	 * Controlla il campo importo rendicontato
	 * 
	 * @param id
	 *            del campo
	 * @param value
	 *            del campo
	 * @return boolean: true se il controllo è andato a buon fine, false
	 *         altrimenti
	 */
	private boolean checkValidateAmountReport(String id, Object value) {
		return checkValidateAmountReport(id, value, false);
	}

	// private boolean checkComponentsOfAmountReport(String id, Object value) {
	// return checkValidateComponentsOfAmountReport(id, value, false);
	// }

	/**
	 * Controlla il campo importo rendicontato
	 * 
	 * @param id
	 *            del campo
	 * @param value
	 *            del campo
	 * @param skip:
	 *            boolean. Se true non viene realizzato il controllo
	 * @return boolean: true se il controllo è andato a buon fine, false
	 *         altrimenti
	 */
	private boolean checkValidateAmountReport(String id, Object value, boolean skip) {
		@SuppressWarnings("unused")
		String str = null;
		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (!skip && !validateAmountReportLimits()) {
			setError(Utils.getString("validator.amountReportLessThen20PercentFers"));
			return false;
		}

		return true;

	}

	// private boolean checkValidateComponentsOfAmountReport(String id, Object
	// value, boolean skip) {
	// @SuppressWarnings("unused")
	// String str = null;
	//
	// if (value != null) {
	// try {
	// str = String.valueOf(value);
	// } catch (Exception e) {
	// }
	// }
	// if (!compareSumComponentToAmountReport()) {
	// this.validationFailed = true;
	// setError(Utils.getString("validator.amountReportAndComponents"));
	// return true;
	// }
	// return false;
	// }

	/**
	 *
	 */
	public void SaveCostDocument() {
		try {
			if (getCostDocumentUpFile() != null && costDocument != null) {
				String fileName = FileHelper.newTempFileName(getCostDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getCostDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(costDocument.getDocumentDate(), getCostDocumentUpFile().getName(),
						fileName, costDocument.getProtocolNumber(), cat, costDocument.getIsPublic(), null);
				this.getViewState().put("costdocument", doc);
			}
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (FileNotFoundException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (IOException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

	}

	/**
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void addNewDocument() throws NumberFormatException, PersistenceBeanException {
		try {
			if (getNewDocumentUpFile() != null && getNewDocument() != null) {
				String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(getNewDocument().getDocumentDate(),
						getNewDocumentUpFile().getName(), fileName, getNewDocument().getProtocolNumber(), cat,
						getNewDocument().getIsPublic(), null);
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}

				docs.add(new Documents(FileHelper.getFileNameWOExtension(doc.getName()), doc.getName(),
						this.getCurrentUser(), doc.getDate(), doc.getProtNumber(), null, this.getProject(),
						doc.getFileName(), doc.getCategory(), doc.getIsPublic(),
						this.getNewDocRole().isEmpty() ? 0 : Integer.parseInt(this.getNewDocRole()),
						getNewDocument().getDocumentNumber(), getNewDocument().getNote(),
						getNewDocument().getEditableDate(), null));

				this.getViewState().put("newDocument", docs);
			}
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (FileNotFoundException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (IOException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

		try {
			if (!this.isEdit) {
				saveDocuments(this.getEntity());
			}
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (IOException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

	}

	/**
	 *
	 */
	public void SavePaymentDocument() {
		try {
			if (getPaymentDocumentUpFile() != null && this.getPaymentDocument() != null) {
				String fileName = FileHelper.newTempFileName(getPaymentDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getPaymentDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getPaymentDocument().getDocumentDate(),
						getPaymentDocumentUpFile().getName(), fileName, this.getPaymentDocument().getProtocolNumber(),
						cat, this.getPaymentDocument().getIsPublic(), null);
				this.getViewState().put("paymentdocument", doc);
			}
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (FileNotFoundException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (IOException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

	}

	public void SavePaymentAttachmentDocument() {
		try {
			if (getPaymentAttachmentDocumentUpFile() != null && this.getPaymentAttachmentDocument() != null) {
				String fileName = FileHelper.newTempFileName(getPaymentAttachmentDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getPaymentAttachmentDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(this.getPaymentAttachmentDocument().getDocumentDate(),
						getPaymentAttachmentDocumentUpFile().getName(), fileName,
						this.getPaymentAttachmentDocument().getProtocolNumber(), cat,
						this.getPaymentAttachmentDocument().getIsPublic(), null);
				this.getViewState().put("paymentattachmentdocument", doc);
			}
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (FileNotFoundException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (IOException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (HibernateException e) {
			log.error("CostDefinitionEdit exception:", e);
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}
	}

	public String saveSuperAdmin() {
		if (this.getActionMotivation() == null || this.getActionMotivation().isEmpty()) {
			return null;
		}
		Page_Save();
		clearMotivation(null);
		return null;
	}

	public void aauValidate() {
		org.hibernate.Transaction tr;
		try {
			tr = PersistenceSessionManager.getBean().getSession().getTransaction();
			tr.begin();
			CostDefinitions cd = BeansFactory.CostDefinitions().Load(this.entity.getId());
			cd.setAauNote(this.entity.getAauNote());
			cd.setValidateByAAU(this.getEntity().getValidateByAAU());
			tr.commit();
		} catch (PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
		}
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException,
			PersistenceBeanException, PersistenceBeanException, IOException {
		Documents costdoc = null;
		if (this.getViewState().get("costdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("costdocument");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				costdoc = new Documents();
				costdoc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				costdoc.setName(docinfo.getName());
				costdoc.setUser(this.getSessionBean().getCurrentUser());
				costdoc.setSection(BeansFactory.Sections().Load(DocumentSections.CostDefinition.getValue()));
				costdoc.setProject(this.getProject());
				costdoc.setDocumentDate(docinfo.getDate());
				costdoc.setProtocolNumber(docinfo.getProtNumber());
				costdoc.setIsPublic(docinfo.getIsPublic());
				costdoc.setCategory(docinfo.getCategory());
				if (this.getCostDocRole() != null && !this.getCostDocRole().isEmpty()) {
					costdoc.setUploadRole(Integer.parseInt(this.getCostDocRole()));
				}
				costdoc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(costdoc);
			}
		}

		Documents paymentdoc = null;
		if (this.getViewState().get("paymentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentdocument");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				paymentdoc = new Documents();
				paymentdoc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				paymentdoc.setName(docinfo.getName());
				paymentdoc.setUser(this.getCurrentUser());
				paymentdoc.setSection(BeansFactory.Sections().Load(DocumentSections.CostDefinition.getValue()));
				paymentdoc.setProject(this.getProject());
				paymentdoc.setDocumentDate(docinfo.getDate());
				paymentdoc.setProtocolNumber(docinfo.getProtNumber());
				paymentdoc.setIsPublic(docinfo.getIsPublic());
				paymentdoc.setCategory(docinfo.getCategory());
				if (this.getPaymentDocRole() != null && !this.getPaymentDocRole().isEmpty()) {
					paymentdoc.setUploadRole(Integer.parseInt(this.getPaymentDocRole()));
				}
				paymentdoc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(paymentdoc);
			}
		}

		Documents paymentattachmentdoc = null;
		if (this.getViewState().get("paymentattachmentdocument") != null) {
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get("paymentattachmentdocument");
			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(newFileName))) {
				paymentattachmentdoc = new Documents();
				paymentattachmentdoc.setTitle(FileHelper.getFileNameWOExtension(docinfo.getName()));
				paymentattachmentdoc.setName(docinfo.getName());
				paymentattachmentdoc.setUser(this.getCurrentUser());
				paymentattachmentdoc
						.setSection(BeansFactory.Sections().Load(DocumentSections.CostDefinition.getValue()));
				paymentattachmentdoc.setProject(this.getProject());
				paymentattachmentdoc.setDocumentDate(docinfo.getDate());
				paymentattachmentdoc.setProtocolNumber(docinfo.getProtNumber());
				paymentattachmentdoc.setIsPublic(docinfo.getIsPublic());
				paymentattachmentdoc.setCategory(docinfo.getCategory());
				if (this.getPaymentDocRole() != null && !this.getPaymentDocRole().isEmpty()) {
					paymentattachmentdoc.setUploadRole(Integer.parseInt(this.getPaymentAttachmentDocRole()));
				}
				paymentattachmentdoc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(paymentattachmentdoc);
			}
		}

		CostDefinitions item = new CostDefinitions();
		if (this.getSession().get("costDefinitionEdit") != null
				&& !String.valueOf(this.getSession().get("costDefinitionEdit")).isEmpty()) {
			item = BeansFactory.CostDefinitions().Load(String.valueOf(this.getSession().get("costDefinitionEdit")));
		}

		if (item.isNew()) {
			item.setProgressiveId(BeansFactory.CostDefinitions().GetMaxId(this.getProjectId()));
			item.setCreatedByAGU(this.getSessionBean().isAGU() && this.getProjectAsse() == 5);
			item.setCreatedByPartner(this.getSessionBean().isPartner());
		}
		item.setLocation(LocationForCostDef.values()[Integer.parseInt(this.getSelectedLocationId())]);

		item.setAmountNotes(this.getEntity().getAmountNotes());
		item.setAmountProjectRelate(this.getEntity().getAmountProjectRelate());
		item.setAmountReport(this.getEntity().getAmountReport());
		item.setPublicAmountReport(this.getEntity().getPublicAmountReport());
		item.setPrivateAmountReport(this.getEntity().getPrivateAmountReport());
		if (this.getTransfersId() != null && !this.getTransfersId().trim().equals("")) {
			item.setAdditionalFesrInfo(BeansFactory.AdditionalFESRInfo().Load(this.getTransfersId()));
		} else {
			item.setAdditionalFesrInfo(null);
		}
		// item.setAdditionalFinansing(this.getAdditionalFinansing());
		if (this.getAdditionalFinansingAmount() == null || this.getAdditionalFinansingAmount().trim().isEmpty()) {
			this.setAdditionalFinansingAmount("0");
		}

		this.setAdditionalFinansingAmount(this.getAdditionalFinansingAmount().replaceAll(",", "."));

		if (!Double.valueOf(this.getAdditionalFinansingAmount()).equals(0d)) {
			item.setAdditionalFinansing(true);
		} else {
			item.setAdditionalFinansing(false);
		}
		item.setAdditionalFinansingAmount(Double.valueOf(this.getAdditionalFinansingAmount()));
		item.setStateAid(this.getStateAid());
		if (this.getAmountToCoverAdvanceStateAid() != null) {
			item.setAmountToCoverAdvanceStateAid(Double.valueOf(this.getAmountToCoverAdvanceStateAid()));
		}

		if (this.getExpenditureOutsideEligibleAreasAmount() == null
				|| this.getExpenditureOutsideEligibleAreasAmount().trim().isEmpty()) {
			this.setExpenditureOutsideEligibleAreasAmount("0");
		}
		item.setExpenditureOutsideEligibleAreasAmount(Double.valueOf(this.getExpenditureOutsideEligibleAreasAmount()));
		if (this.getInkindContributionsAmount() == null || this.getInkindContributionsAmount().trim().isEmpty()) {
			this.setInkindContributionsAmount("0");
		}
		item.setInkindContributionsAmount(Double.valueOf(this.getInkindContributionsAmount()));
		item.setCfPivaVta(this.getEntity().getCfPivaVta());
		item.setReportNumber(this.getEntity().getReportNumber());
		item.setImportAda(this.getEntity().getImportAda());
		item.setReportDate(this.getEntity().getReportDate());
		item.setPaymentReciveDate(this.getEntity().getPaymentReciveDate());
		if (costdoc != null) {
			item.setCostDocument(costdoc);
		} else if (this.getEntity().getCostDocument() != null) {
			item.setCostDocument(this.getEntity().getCostDocument());
		}
		if (this.getViewState().get("costdocumentToDel") != null) {
			BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("costdocumentToDel"));
			item.setCostDocument(costdoc);
		}
		if (paymentdoc != null) {
			item.setPaymentDocument(paymentdoc);
		} else if (this.getEntity().getPaymentDocument() != null) {
			item.setPaymentDocument(this.getEntity().getPaymentDocument());
		}
		if (this.getViewState().get("paymentdocumentToDel") != null) {
			BeansFactory.Documents().DeleteInTransaction((Long) this.getViewState().get("paymentdocumentToDel"));
			item.setPaymentDocument(paymentdoc);
		}
		if (paymentattachmentdoc != null) {
			item.setPaymentAttachmentDocument(paymentattachmentdoc);
		} else if (this.getEntity().getPaymentAttachmentDocument() != null) {
			item.setPaymentAttachmentDocument(this.getEntity().getPaymentAttachmentDocument());
		}
		if (this.getViewState().get("paymentattachmentdocumentToDel") != null) {
			BeansFactory.Documents()
					.DeleteInTransaction((Long) this.getViewState().get("paymentattachmentdocumentToDel"));
			item.setPaymentAttachmentDocument(paymentattachmentdoc);
		}

		item.setCostNote(this.getEntity().getCostNote());
		item.setDocumentAmount(this.getEntity().getDocumentAmount());
		item.setDocumentNumber(this.getEntity().getDocumentNumber());
		item.setDocumentDate(this.getEntity().getDocumentDate());

		if (Boolean.TRUE.equals(this.getAsse3Mode())) {
			if (this.getSelectedCostItemAsse3() != null && !this.getSelectedCostItemAsse3().isEmpty()
					&& this.getSelectedPhaseAsse3() != null && !this.getSelectedPhaseAsse3().isEmpty()) {
				item.setCostItemPhaseAsse3(BeansFactory.CostAsse3().GetByPhaseAndValue(
						PhaseAsse3Types.valueOf(this.getSelectedPhaseAsse3()), this.getSelectedCostItemAsse3()));
			}
		} else if (Boolean.TRUE.equals(this.getTypology3Mode())) {
			if (this.getSelectedCostItemTypology3() != null && !this.getSelectedCostItemTypology3().isEmpty()
					&& this.getSelectedPhaseTypology3() != null && !this.getSelectedPhaseTypology3().isEmpty()) {
				CostAsse3 cost = new CostAsse3();
				cost.setPhase(PhaseAsse3Types.valueOf(this.getSelectedPhaseTypology3()));
				cost.setValue(this.getSelectedCostItemTypology3());
				item.setCostItemPhaseAsse3(cost);
				// TODO verificare
			}
		} else {
			try {
				if (this.getCostItem() != null && !this.getCostItem().isEmpty())
					item.setCostItem(BeansFactory.CostItems().Load(this.getCostItem()));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
			}
			try {
				if (!this.getPhase().isEmpty() && this.getPhase() != null)
					item.setCostDefinitionPhase(BeansFactory.CostDefinitionPhase().Load(this.getPhase()));
			} catch (Exception e) {
				log.error("CostDefinitionEdit exception:", e);
			}
		}

		try {
			item.setDocumentType(BeansFactory.DocumentTypes().Load(this.getDocType()));
		} catch (Exception e) {
			log.error("CostDefinitionEdit exception:", e);
		}
		item.setExternalCosts(this.getEntity().getExternalCosts());
		item.setIssuesNames(this.getEntity().getIssuesNames());
		item.setIva(this.getEntity().getIva());
		item.setMotivations(this.getEntity().getMotivations());
		item.setPaymentAmount(this.getEntity().getPaymentAmount());
		item.setPaymentDate(this.getEntity().getPaymentDate());
		item.setPaymentNotes(this.getEntity().getPaymentNotes());
		item.setPaymentNumber(this.getEntity().getPaymentNumber());
		item.setStcCertification(this.getEntity().getStcCertification());
		item.setAcuCertification(this.getEntity().getAcuCertification());
		item.setAguCertification(this.getEntity().getAguCertification());
		try {
			if (this.getPaymentType() != null && !this.getPaymentType().trim().isEmpty()) {
				item.setPaymentType(BeansFactory.PaymentTypes().Load(this.getPaymentType()));
			}
		} catch (Exception e) {
			log.error("CostDefinitionEdit exception:", e);
		}
		try {
			if (this.getPaymentWay() != null && !this.getPaymentWay().trim().isEmpty()) {
				item.setPaymentWay(BeansFactory.PaymentWays().Load(this.getPaymentWay()));
			}
		} catch (Exception e) {
			log.error("CostDefinitionEdit exception:", e);
		}
		item.setPaymentTypeCode(this.getPaymentTypeCode());
		item.setPaymentMotivationCode(this.getPaymentMotivationCode());
		item.setProject(this.getProject());
		item.setRetain(this.getEntity().getRetain());
		item.setTotal(item.getDocumentAmount() + item.getIva());
		item.setToPay(item.getTotal() - item.getRetain());
		if (this.getSaveAsDraft()) {
			item.setCostState(BeansFactory.CostStates().Load(CostStateTypes.Draft.getState()));
			item.setUser(this.getCurrentUser());
			item.setBudgetOwner(this.getCurrentUser());
		} else if (item.getCostState() == null || item.getCostState().getId()
				.equals(BeansFactory.CostStates().Load(CostStateTypes.Draft.getState()).getId())) {
			item.setCostState(BeansFactory.CostStates().Load(1));
			item.setUser(this.getCurrentUser());
			item.setBudgetOwner(this.getCurrentUser());
		}

		item.setCilIntermediateStepSave(false);

		GiuridicalEngages giue;
		if (!this.getSelectedGiuridicalEngagesId().equals(Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE))
				&& NumberHelper.isNumeric(this.getSelectedGiuridicalEngagesId())) {
			giue = BeansFactory.GiuridicalEngages().Load(this.getSelectedGiuridicalEngagesId());
			if (giue.getNumber().contains("IMP_CONT ")) {
				giue.setAmount(item.getAmountProjectRelate()); // new 20/11/2018
																// SV per gli
																// impegni
																// contestuali
			}

		} else {
			giue = new GiuridicalEngages();
			// if (giue.getNumber().contains("IMP_CONT ")) {
			giue.setAmount(item.getAmountProjectRelate()); // new 20/11/2018 SV
															// per gli impegni
															// contestuali
			// }
			// else {
			// giue.setAmount(item.getDocumentAmount()); // new 20/11/2018 SV
			// per gli impegni non contestuali
			// }
			//

			giue.setDate(item.getDocumentDate());
			giue.setHierarchicalLevelCode(HierarchicalLevelSelector.getHierarchicalLevelImpegnoContestuale().code);
			giue.setCreatePartner(BeansFactory.CFPartnerAnagraphs().GetByUser(this.getCurrentUser().getId()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			giue.setNumber("IMP_CONT " + sdf.format(item.getDocumentDate()) + " "
					+ BeansFactory.GiuridicalEngages().getMaxId() + 1);
			giue.setActType(BeansFactory.ActTypes().Load(1l));
			giue.setProject(this.getProject());

		}
		if (!giue.isNew()) {
			List<CostDefinitions> costs = BeansFactory.CostDefinitions().GetByGuiridicalEngage(giue.getId());
			double summ = 0d;
			for (CostDefinitions cost : costs) {
				if (item.isNew() || !item.getId().equals(cost.getId())) {
					summ += cost.getAmountReport() != null ? cost.getAmountReport().doubleValue() : 0d;
				}

			}
			summ += item.getAmountReport().doubleValue();

			if (giue.getNumber().contains("IMP_CONT ")) { // new 20/11/2018 SV
															// per gli impegni
															// contestuali
				giue.setResidualAmount(item.getAmountProjectRelate() - summ);
			} else {
				giue.setResidualAmount(new Double(giue.getAmount().doubleValue() - summ)); // new
																							// 20/11/2018
																							// SV
																							// per
																							// gli
																							// impegni
																							// non
																							// contestuali
			}

		} else {
			if (giue.getNumber().contains("IMP_CONT ")) { // new 20/11/2018 SV
															// per gli impegni
															// contestuali
				giue.setResidualAmount(item.getAmountProjectRelate() - item.getAmountReport().doubleValue());
			} else {
				giue.setResidualAmount(
						new Double(giue.getAmount().doubleValue() - item.getAmountReport().doubleValue())); // new
																											// 20/11/2018
																											// SV
																											// per
																											// gli
																											// impegni
																											// non
																											// contestuali
			}
		}

		if (giue.getReportNumber() == null) {
			giue.setReportNumber(item.getReportNumber().toString());
		} else {
			if (!giue.getReportNumber().contains(item.getReportNumber().toString())) {
				giue.setReportNumber(giue.getReportNumber() + ", " + item.getReportNumber().toString());
			}

		}
		BeansFactory.GiuridicalEngages().SaveInTransaction(giue);
		item.setGiuridicalEngages(giue);
		BeansFactory.CostDefinitions().SaveInTransaction(item);
		if (getSessionBean().isSuperAdmin() && !item.isNew()) {
			@SuppressWarnings("unchecked")
			Map<String, Object> oldValues = (Map<String, Object>) this.getViewState().get("oldValues");
			SuperAdminChange changes = new SuperAdminChange(ChangeType.VALUE_CHANGE, this.getCurrentUser(),
					this.getActionMotivation(), getSessionBean().getProject());
			StringBuilder changeLogMessages = new StringBuilder();
			String docTitle = (String) oldValues.get("docTitle");
			String docType = BeansFactory.DocumentTypes().Load(this.getDocType()).getValue();
			if (!docTitle.equals(docType)) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("documentType")).replace("%2", docTitle).replace("%3", docType))
						.append("<br/>");
			}
			String motivations = (String) oldValues.get("motivations");
			if (!motivations.equals(item.getMotivations())) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("motivation"))
								.replace("%2", motivations).replace("%3", item.getMotivations()))
						.append("<br/>");
			}
			String documentNumber = (String) oldValues.get("documentNumber");
			if (!documentNumber.equals(item.getDocumentNumber())) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("documentNumber"))
								.replace("%2", documentNumber).replace("%3", item.getDocumentNumber()))
						.append("<br/>");
			}
			Date documentDate = (Date) oldValues.get("documentDate");
			if (documentDate.getTime() != item.getDocumentDate().getTime()) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("documentDate"))
								.replace("%2", DateTime.ToPathString(documentDate))
								.replace("%3", DateTime.ToPathString(item.getDocumentDate())))
						.append("<br/>");
			}
			String documentCode = (String) oldValues.get("documentCode");
			if (!documentCode.equals(item.getCfPivaVta())) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("documentCode"))
								.replace("%2", documentCode).replace("%3", item.getCfPivaVta()))
						.append("<br/>");
			}
			String issuesNames = (String) oldValues.get("issuesNames");
			if (!issuesNames.equals(item.getIssuesNames())) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("issuesNames"))
								.replace("%2", issuesNames).replace("%3", item.getIssuesNames()))
						.append("<br/>");
			}
			Double documentAmount = (Double) oldValues.get("documentAmount");
			if (!documentAmount.equals(item.getDocumentAmount())) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("documentAmount")).replace("%2", String.valueOf(documentAmount))
						.replace("%3", item.getDocumentAmount().toString())).append("<br/>");
			}
			Double iva = (Double) oldValues.get("iva");
			if (item.getIva() != null && !item.getIva().equals(iva) || iva != null && !iva.equals(item.getIva())) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("iva"))
								.replace("%2", String.valueOf(iva)).replace("%3", String.valueOf(item.getIva())))
						.append("<br/>");
			}
			Double retain = (Double) oldValues.get("retain");
			if (item.getIva() != null && !item.getRetain().equals(retain)
					|| retain != null && !retain.equals(item.getRetain())) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("retain"))
								.replace("%2", String.valueOf(retain)).replace("%3", String.valueOf(item.getRetain())))
						.append("<br/>");
			}
			Double amountProjectRelate = (Double) oldValues.get("amountProjectRelate");
			if (item.getAmountProjectRelate() != null && !item.getAmountProjectRelate().equals(amountProjectRelate)
					|| amountProjectRelate != null && !amountProjectRelate.equals(item.getAmountProjectRelate())) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("amountProjectRelate"))
						.replace("%2", String.valueOf(amountProjectRelate))
						.replace("%3", String.valueOf(item.getAmountProjectRelate()))).append("<br/>");
			}
			String costItem = (String) oldValues.get("costItem");
			if (!item.getCostItem().getValue().equals(costItem)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("costItem"))
								.replace("%2", costItem).replace("%3", item.getCostItem().getValue()))
						.append("<br/>");
			}
			String phase = (String) oldValues.get("phase");
			if (!item.getCostDefinitionPhase().getValue().equals(phase)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("phase"))
								.replace("%2", phase).replace("%3", item.getCostDefinitionPhase().getValue()))
						.append("<br/>");
			}
			String costNote = (String) oldValues.get("costNote");
			if (costNote != null && !costNote.equals(item.getCostNote())
					|| item.getCostNote() != null && !item.getCostNote().equals(costNote)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("notes"))
								.replace("%2", costNote).replace("%3", item.getCostNote()))
						.append("<br/>");
			}
			String costDoc = (String) oldValues.get("costDocId");
			String costDocCurrent = item.getCostDocument() != null ? String.valueOf(item.getCostDocument().getId())
					: "";
			if (!costDoc.equals(costDocCurrent)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("attachment"))
								.replace("%2", (String) oldValues.get("costDocName"))
								.replace("%3", item.getCostDocument() != null ? item.getCostDocument().getName() : ""))
						.append("<br/>");
			}
			String paymentWay = (String) oldValues.get("paymentWay");
			String paymentWayTitle = BeansFactory.PaymentWays().Load(getPaymentWay()).getValue();
			if (!paymentWay.equals(paymentWayTitle)) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("paymentWay"))
								.replace("%2", paymentWay).replace("%3", paymentWayTitle))
						.append("<br/>");
			}
			String paymentType = (String) oldValues.get("paymentType");
			String paymentTypeTitle = BeansFactory.PaymentTypes().Load(getPaymentType()).getValue();
			if (!paymentType.equals(paymentTypeTitle)) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("paymentType"))
								.replace("%2", paymentType).replace("%3", paymentTypeTitle))
						.append("<br/>");
			}
			String paymentNumber = (String) oldValues.get("paymentNumber");
			if (!paymentNumber.equals(item.getPaymentNumber())) {
				changeLogMessages.append(
						Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("paymentNumber"))
								.replace("%2", paymentNumber).replace("%3", item.getPaymentNumber()))
						.append("<br/>");
			}
			Date paymentDate = (Date) oldValues.get("paymentDate");
			if (paymentDate.getTime() != item.getPaymentDate().getTime()) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("paymentDate")).replace("%2", DateTime.ToPathString(paymentDate))
						.replace("%3", DateTime.ToPathString(item.getPaymentDate()))).append("<br/>");
			}
			Double paymentAmount = (Double) oldValues.get("paymentAmount");
			if (!paymentAmount.equals(item.getPaymentAmount())) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("paymentAmount")).replace("%2", paymentAmount.toString())
						.replace("%3", item.getPaymentAmount().toString())).append("<br/>");
			}
			Boolean externalCosts = (Boolean) oldValues.get("externalCosts");
			if (externalCosts != null && !externalCosts.equals(item.getExternalCosts())
					|| item.getExternalCosts() != null && !item.getExternalCosts().equals(externalCosts)) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("externalCosts")).replace("%2", paymentAmount.toString())
						.replace("%3", String.valueOf(item.getExternalCosts()))).append("<br/>");
			}
			String paymentNotes = (String) oldValues.get("paymentNotes");
			if (paymentNotes != null && !paymentNotes.equals(item.getPaymentNotes())
					|| item.getPaymentNotes() != null && !item.getPaymentNotes().equals(paymentNotes)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("notes"))
								.replace("%2", paymentNotes).replace("%3", item.getPaymentNotes()))
						.append("<br/>");
			}
			String paymentDoc = (String) oldValues.get("paymentDocId");
			String paymentDocCurrent = item.getPaymentDocument() != null
					? String.valueOf(item.getPaymentDocument().getId()) : "";
			if (!paymentDoc.equals(paymentDocCurrent)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("attachment"))
								.replace("%2", (String) oldValues.get("paymentDocName")).replace("%3",
										item.getPaymentDocument() != null ? item.getPaymentDocument().getName() : ""))
						.append("<br/>");
			}
			Double amountReport = (Double) oldValues.get("amountReport");
			if (!amountReport.equals(item.getAmountReport())) {
				changeLogMessages.append(Utils.getString("superAdminChangeLogMessage")
						.replace("%1", Utils.getString("amountReport")).replace("%2", amountReport.toString())
						.replace("%3", item.getAmountReport().toString())).append("<br/>");
			}
			String amountNotes = (String) oldValues.get("amountNotes");
			if (amountNotes != null && !amountNotes.equals(item.getAmountNotes())
					|| item.getAmountNotes() != null && !item.getAmountNotes().equals(amountNotes)) {
				changeLogMessages
						.append(Utils.getString("superAdminChangeLogMessage").replace("%1", Utils.getString("notes"))
								.replace("%2", amountNotes).replace("%3", item.getAmountNotes()))
						.append("<br/>");
			}
			if (changeLogMessages.length() > 0) {
				changeLogMessages.insert(0,
						Utils.getString("superAdminChangeLogMessageHeader")
								.replace("%1", Utils.getString("superAdminChangesCostDefinition"))
								.replace("%2", item.getProgressiveId().toString())
								.replace("%3", Utils.getString("costDefinition") + " Edit") + "<br/>");
				changes.setChanges(changeLogMessages.toString());
				BeansFactory.SuperAdminChanges().SaveInTransaction(changes);
			}
		}

		saveDocuments(item);

		saveNotes(item);
	}

	public void saveIgnorelocationSumLimits() {
		this.ignorelocationSumLimits = true;
		this.Page_Save();
	}

	/**
	 * Verifica che la somma degli importi rendicontati non superino il 20% del
	 * FERS del Progetto
	 * 
	 * @return true se è validata l'asserzione
	 */
	private Boolean validateAmountReportLimits() {
		// somma dei fers del progetto per ogni partner
		Boolean validation = false;
		try {
			validation = calculateCostInProjectForExpendituresOutsideEligibleAreas() < (loadProjectFesr()
					* PERCENT_FERS_LIMIT);
		} catch (NumberFormatException | PersistenceBeanException e) {
			log.error("CostDefinitionEdit exception:", e);
			return validation;
		}
		return validation;
	}

	// private Boolean compareSumComponentToAmountReport() {
	// Boolean validation = false;
	// try {
	// Double publicAmount = Double.valueOf(this.getPublicAmountReport());
	// Double privateAmount = Double.valueOf(this.getPrivateAmountReport());
	// Double amount = Double.valueOf(this.getAmountReport());
	// if ((publicAmount + privateAmount) == amount) {
	// validation = true;
	// }
	// } catch (NumberFormatException ex) {
	// return validation;
	// }
	//
	// return validation;
	// }

	private void validateLocationLimits() throws PersistenceBeanException {
		Integer threshold = loadThreshold();

		switch (defineLocationLimitsState(threshold)) {
		case WARNING_MESSAGE:
			this.validationFailed = true;
			break;
		case PROHIBITING_MESSAGE:
			this.validationFailed = true;
			break;
		case CONTINUATION:

		}

	}

	private LocationLimitsState defineLocationLimitsState(Integer threshold) throws PersistenceBeanException {

		LocationForCostDef currentLocation = LocationForCostDef.values()[Integer
				.parseInt(this.getSelectedLocationId())];

		Double currentRatio;
		// List<LocationForCostDef> locationsForFesrCalculation = new
		// ArrayList<LocationForCostDef>();

		switch (currentLocation) {
		// case CATANIA:
		// locationsForFesrCalculation.add(LocationForCostDef.CATANIA);
		// locationsForFesrCalculation.add(LocationForCostDef.PALERMO);
		// currentRatio =
		// (calculateCostInProjectForManyLocations(locationsForFesrCalculation)*0.85
		// / loadProjectFesr()) * 100;
		// return limitStateAnalizator(currentRatio,
		// currentLocation.getLimit(), threshold);
		// case PALERMO:
		//
		// locationsForFesrCalculation.add(LocationForCostDef.CATANIA);
		// locationsForFesrCalculation.add(LocationForCostDef.PALERMO);
		// currentRatio =
		// (calculateCostInProjectForManyLocations(locationsForFesrCalculation)*0.85
		// / loadProjectFesr()) * 100;
		// return limitStateAnalizator(currentRatio,
		// currentLocation.getLimit(), threshold);
		// case TUNISIA:
		// if (this.getSessionBean().getProjectWithStrategicType()) {
		// currentRatio = (calculateCostInProject(currentLocation) /
		// loadTotalProjectBudget()) * 100;
		// return limitStateAnalizator(currentRatio,
		// currentLocation.getStrategicProjectLimit(), threshold);
		// } else if (this.getSessionBean().getProjectWithSimpliceType()) {
		// currentRatio = (calculateCostInProject(currentLocation) /
		// loadTotalProjectBudget()) * 100;
		// return limitStateAnalizator(currentRatio,
		// currentLocation.getSimpleProjectLimit(), threshold);
		// }
		// break;
		case ITALY:
			if (this.getSessionBean().getProjectWithStrategicType()) {
				currentRatio = (calculateCostInProject(currentLocation) / loadTotalProjectBudget()) * 100;
				return limitStateAnalizator(currentRatio, currentLocation.getStrategicProjectLimit(), threshold);
			} else if (this.getSessionBean().getProjectWithSimpliceType()) {
				currentRatio = (calculateCostInProject(currentLocation) / loadTotalProjectBudget()) * 100;
				return limitStateAnalizator(currentRatio, currentLocation.getSimpleProjectLimit(), threshold);
			}
			break;
		default:
			break;
		}

		return LocationLimitsState.CONTINUATION;
	}

	/**
	 * @param currentLocation
	 * @return
	 * @throws PersistenceBeanException
	 */
	@SuppressWarnings("unused")
	private Double calculateFesrForWholeSystemForSeveralLocations(List<LocationForCostDef> locationsForFesrCalculation)
			throws PersistenceBeanException {
		List<CostDefinitions> oldCostDef = BeansFactory.CostDefinitions()
				.getAllWithCurrentLocations(locationsForFesrCalculation);
		Double oldSum = 0.;
		for (CostDefinitions item : oldCostDef) {
			if (item.getCostState() != null
					&& item.getCostState().equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
					&& !item.getWasRefusedByCil() || item.getDismiss().equals(true)) {
				continue;
			}
			/*
			 * if (item.getAcuCertification() != null && item.getACUSertified())
			 * { oldSum += item.getAcuCertification(); } else if
			 * (item.getAguCertification() != null && item.getAGUSertified()) {
			 * oldSum += item.getAguCertification(); } else if
			 * (item.getStcCertification() != null && item.getSTCSertified()) {
			 * oldSum += item.getStcCertification(); } else if
			 * (item.getCfCheck() != null) { oldSum += item.getCfCheck(); } else
			 * if (item.getCilCheck() != null &&
			 * !item.getCilIntermediateStepSave()) { oldSum +=
			 * item.getCilCheck(); } else {
			 */
			oldSum += (item.getAmountReport() != null ? item.getAmountReport() : 0d);
			// }
		}

		return NumberHelper.Round((oldSum += this.getEntity().getAmountReport()) * 0.75);
	}

	@SuppressWarnings("unused")
	private Double calculateCostInProjectForManyLocations(List<LocationForCostDef> locations)
			throws PersistenceBeanException {
		Double result = 0.;

		List<CostDefinitions> oldCostDef = BeansFactory.CostDefinitions()
				.getAllWithCurrentLocationsForProject(locations, this.getSessionBean().getProjectId());
		Double oldSum = 0.;
		for (CostDefinitions item : oldCostDef) {
			if ((item.getCostState() != null
					&& item.getCostState().equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
					&& !item.getWasRefusedByCil() || item.getDismiss().equals(true))
					|| item.getId().equals(this.getEntity().getId())) {
				continue;
			}
			/*
			 * if (item.getAcuCertification() != null && item.getACUSertified())
			 * { oldSum += item.getAcuCertification(); } else if
			 * (item.getAguCertification() != null && item.getAGUSertified()) {
			 * oldSum += item.getAguCertification(); } else if
			 * (item.getStcCertification() != null && item.getSTCSertified()) {
			 * oldSum += item.getStcCertification(); } else if
			 * (item.getCfCheck() != null) { oldSum += item.getCfCheck(); } else
			 * if (item.getCilCheck() != null &&
			 * !item.getCilIntermediateStepSave()) { oldSum +=
			 * item.getCilCheck(); } else {
			 */

			oldSum += (item.getAmountReport() != null ? item.getAmountReport() : 0d);
			// }
		}

		return oldSum += this.getEntity().getAmountReport();
	}

	/**
	 * calculateCostInProjectForExpendituresOutsideEligibleAreas() calcola gli
	 * importi rendicontati di tutte le spese del progetto sostenute al di fuori
	 * dell'area eleggibile del Programma
	 * 
	 * @return Somma degli importi rendicontati sostenuti al di fuori dell'area
	 *         eleggibile del Programma, incluso l'importo rendicontato della
	 *         spesa di pagamento non ancora memorizzato nel db
	 * @throws PersistenceBeanException
	 */
	private Double calculateCostInProjectForExpendituresOutsideEligibleAreas() throws PersistenceBeanException {
		@SuppressWarnings("unused")
		Double result = 0.;

		List<CostDefinitions> oldCostDef = BeansFactory.CostDefinitions()
				.getAllWithExpendituresOutsideEligibleAreasForProject(this.getSessionBean().getProjectId());
		Double oldSum = 0.;
		for (CostDefinitions item : oldCostDef) {
			if ((item.getCostState() != null
					&& item.getCostState().equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
					&& !item.getWasRefusedByCil() || item.getDismiss().equals(true))
					|| item.getId().equals(this.getEntity().getId())) {
				continue;
			}

			oldSum += (item.getAmountReport() != null ? item.getAmountReport() : 0d);
		}

		return oldSum += this.getEntity().getAmountReport();
	}

	/**
	 * calculateCostInProjectForTotalExpendituresOutsideEligibleAreas() calcola
	 * gli importi rendicontati di tutte le spese del progetto sostenute al di
	 * fuori dell'area eleggibile del Programma
	 * 
	 * @return Somma degli importi rendicontati sostenuti al di fuori dell'area
	 *         eleggibile del Programma
	 * @throws PersistenceBeanException
	 */
	private Double calculateCostInProjectForTotalExpendituresOutsideEligibleAreas() throws PersistenceBeanException {
		List<CostDefinitions> oldCostDef = BeansFactory.CostDefinitions()
				.getAllWithExpendituresOutsideEligibleAreasAmountForProject(this.getSessionBean().getProjectId());
		Double oldSum = 0.;
		for (CostDefinitions item : oldCostDef) {
			if ((item.getCostState() != null
					&& item.getCostState().equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
					&& !item.getWasRefusedByCil() || item.getDismiss().equals(true))
					|| item.getId().equals(this.getEntity().getId())) {
				continue;
			}
			oldSum += (item.getExpenditureOutsideEligibleAreasAmount() != null
					? item.getExpenditureOutsideEligibleAreasAmount() : 0d);
		}

		return oldSum;
	}

	private Double calculateCostInProject(LocationForCostDef currentLocation) throws PersistenceBeanException {
		List<CostDefinitions> oldCostDef = BeansFactory.CostDefinitions()
				.getAllForThisProjectWithSameLocation(currentLocation, this.getSessionBean().getProjectId());
		Double oldSum = 0.;
		for (CostDefinitions item : oldCostDef) {
			if ((item.getCostState() != null
					&& item.getCostState().equals(BeansFactory.CostStates().Load(CostStateTypes.Closed.getState()))
					&& !item.getWasRefusedByCil() || item.getDismiss().equals(true))
					|| item.getId().equals(this.getEntity().getId())) {
				continue;
			}
			/*
			 * if (item.getAcuCertification() != null && item.getACUSertified())
			 * { oldSum += item.getAcuCertification(); } else if
			 * (item.getAguCertification() != null && item.getAGUSertified()) {
			 * oldSum += item.getAguCertification(); } else if
			 * (item.getStcCertification() != null && item.getSTCSertified()) {
			 * oldSum += item.getStcCertification(); } else if
			 * (item.getCfCheck() != null) { oldSum += item.getCfCheck(); } else
			 * if (item.getCilCheck() != null &&
			 * !item.getCilIntermediateStepSave()) { oldSum +=
			 * item.getCilCheck(); } else {
			 */
			oldSum += (item.getAmountReport() != null ? item.getAmountReport() : 0d);
			// }
		}

		return oldSum += this.getEntity().getAmountReport();
	}

	private enum LocationLimitsState {
		WARNING_MESSAGE, PROHIBITING_MESSAGE, CONTINUATION;

	}

	private Integer loadThreshold() throws PersistenceBeanException {
		String threshold = BeansFactory.ApplicationSettings().getValue(ApplicationSettingTypes.THRESHOLD_CONTROL_COSTS);
		if (threshold == null) {
			return 0;
		}
		return Integer.parseInt(threshold);
	}

	@SuppressWarnings("unused")
	private Double loadSystemFesr() throws PersistenceBeanException {
		// AnagraphicalData anagraphicalData = BeansFactory.AnagraphicalData()
		// .GetAnagraphicalDataForTotalAsse();
		String fesr = "€ 30.148.017,00"; // anagraphicalData.getImportFESR();
		return Double.parseDouble(fesr.substring(1).replaceAll("[.]", "").replace(",", "."));
	}

	private Double loadTotalProjectBudget() throws PersistenceBeanException {
		List<GeneralBudgets> listAux = BeansFactory.GeneralBudgets()
				.GetItemsForProject(Long.valueOf(String.valueOf(this.getSession().get("project"))));
		Double totalProjectBudget = 0.;
		for (GeneralBudgets gb : listAux) {
			if (gb.getBudgetTotal() != null) {
				totalProjectBudget += gb.getBudgetTotal();
			}
		}
		return totalProjectBudget;
	}

	private Double loadProjectFesr() throws PersistenceBeanException {
		List<GeneralBudgets> listAux = BeansFactory.GeneralBudgets()
				.GetItemsForProject(Long.valueOf(String.valueOf(this.getSession().get("project"))));
		Double projectFesr = 0.;
		for (GeneralBudgets gb : listAux) {
			if (gb.getFesr() != null) {
				projectFesr += gb.getFesr();
			}
		}
		return projectFesr;
	}

	private LocationLimitsState limitStateAnalizator(Double currentRatio, Integer currentLocationLimit,
			Integer threshold) {
		if (currentLocationLimit <= currentRatio) {
			if ((currentLocationLimit + threshold) < currentRatio) {
				this.setLocationSumLimitsError(
						String.format(Utils.getString("prohibitingLocationLimitMessage"), currentLocationLimit));
				this.setLocationLimitsErrorType("PROHIBITING");
				return LocationLimitsState.PROHIBITING_MESSAGE;
			} else {
				this.setLocationSumLimitsError(
						String.format(Utils.getString("warningLocationLimitMessage"), currentLocationLimit));
				this.setLocationLimitsErrorType("WARNING");
				return LocationLimitsState.WARNING_MESSAGE;

			}
		} else {
			return LocationLimitsState.CONTINUATION;

		}
	}

	/**
	 * @param item
	 * @throws PersistenceBeanException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void saveDocuments(CostDefinitions item) throws PersistenceBeanException, IOException {
		List<CostDefinitionsToDocuments> costToDoc = BeansFactory.CostDefinitionsToDocuments()
				.getByCostDefinition(item.getId());
		if ((costToDoc != null && !costToDoc.isEmpty()) || this.getViewState().get("newDocument") != null) {
			List<Documents> docs = new ArrayList<Documents>();

			if (this.getViewState().get("newDocument") != null) {
				docs = (List<Documents>) this.getViewState().get("newDocument");
			}
			List<Documents> oldDocs = new ArrayList<Documents>();
			for (CostDefinitionsToDocuments doc : costToDoc) {
				oldDocs.add(doc.getDocument());
			}

			for (Documents doc : oldDocs) {
				if (doc != null && !Contains(docs, doc)) {
					CostDefinitionsToDocuments costtodef = BeansFactory.CostDefinitionsToDocuments()
							.getByDocumentAndCostDef(doc.getId(), item.getId());
					BeansFactory.CostDefinitionsToDocuments().DeleteInTransaction(costtodef);
					if (BeansFactory.CostDefinitionsToDocuments().getByDocument(doc.getId()).size() == 0) {
						BeansFactory.Documents().DeleteInTransaction(doc);
					}
				}
			}

			for (Documents doc : docs) {
				if (doc.isNew()) {
					String newFileName = FileHelper.newFileName(doc.getName());
					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setSection(BeansFactory.Sections().Load(DocumentSections.CostDefinition.getValue()));
						doc.setUser(this.getCurrentUser());
						doc.setProject(this.getProject());
						BeansFactory.Documents().SaveInTransaction(doc);
						CostDefinitionsToDocuments newItem = new CostDefinitionsToDocuments(doc, item);
						BeansFactory.CostDefinitionsToDocuments().SaveInTransaction(newItem);
					}
				}
			}
		}
	}

	private void saveNotes(CostDefinitions item) throws PersistenceBeanException {
		if (this.getNotesList() != null) {
			for (CostDefinitionsToNotes noteItem : getNotesList()) {
				if (!noteItem.getCostDefinition().equals(new CostDefinitions())) {
					noteItem.setCostDefinition(item);
					BeansFactory.CostDefinitionsToNotes().SaveInTransaction(noteItem);
				}
			}
		}
	}

	private boolean Contains(List<Documents> list, Documents item) {
		for (Documents doc : list) {
			if (doc.getId().equals(item.getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets docTypes
	 * 
	 * @param docTypes
	 *            the docTypes to set
	 */
	public void setDocTypes(List<SelectItem> docTypes) {
		CostDefinitionEditBean.docTypes = docTypes;
	}

	/**
	 * Gets docTypes
	 * 
	 * @return docTypes the docTypes
	 */
	public List<SelectItem> getDocTypes() {
		return docTypes;
	}

	/**
	 * Sets costItems
	 * 
	 * @param costItems
	 *            the costItems to set
	 */
	public void setCostItems(List<SelectItem> costItems) {
		this.getViewState().put("costItems", costItems);
	}

	/**
	 * Gets costItems
	 * 
	 * @return costItems the costItems
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getCostItems() {
		return (List<SelectItem>) this.getViewState().get("costItems");
	}

	/**
	 * Sets paymentWays
	 * 
	 * @param paymentWays
	 *            the paymentWays to set
	 */
	public void setPaymentWays(List<SelectItem> paymentWays) {
		CostDefinitionEditBean.paymentWays = paymentWays;
	}

	/**
	 * Gets paymentWays
	 * 
	 * @return paymentWays the paymentWays
	 */
	public List<SelectItem> getPaymentWays() {
		return paymentWays;
	}

	/**
	 * Sets paymentTypes
	 * 
	 * @param paymentTypes
	 *            the paymentTypes to set
	 */
	public void setPaymentTypes(List<SelectItem> paymentTypes) {
		CostDefinitionEditBean.paymentTypes = paymentTypes;
	}

	/**
	 * Gets paymentTypes
	 * 
	 * @return paymentTypes the paymentTypes
	 */
	public List<SelectItem> getPaymentTypes() {
		return paymentTypes;
	}

	/**
	 * Sets paymentWay
	 * 
	 * @param paymentWay
	 *            the paymentWay to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setPaymentWay(String paymentWay)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (paymentWay != null && !paymentWay.isEmpty()) {
			setPaymentWayTitle(BeansFactory.PaymentWays().Load(paymentWay).getValue());
		}

		this.getViewState().put("PaymentWay", paymentWay);
	}

	/**
	 * Gets paymentWay
	 * 
	 * @return paymentWay the paymentWay
	 */
	public String getPaymentWay() {
		return (String) this.getViewState().get("PaymentWay");
	}

	public void setPhase(String phase) throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (phase != null && !phase.isEmpty()) {
			CostDefinitionPhases cdp = BeansFactory.CostDefinitionPhase().Load(phase);
			if (cdp != null) {
				Integer value = null;

				try {
					value = Integer.valueOf(cdp.getCode());
				} catch (NumberFormatException e) {

				}

				if (value != null && value != 20) {
					setPhasesTitle(cdp.getValue());
				} else if (value != null && value == 20) {
					setPhasesTitle(Utils.getString("Resources", "costDefinitionNonApplicable", null));
				}
			}
		}

		this.getViewState().put("phase", phase);
	}

	/**
	 * Gets paymentWay
	 * 
	 * @return paymentWay the paymentWay
	 */
	public String getPhase() {
		return (String) this.getViewState().get("phase");
	}

	/**
	 * Sets paymentType
	 * 
	 * @param paymentType
	 *            the paymentType to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setPaymentType(String paymentType)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (paymentType != null && !paymentType.isEmpty()) {
			setPaymentTypeTitle(BeansFactory.PaymentTypes().Load(paymentType).getValue());
		}

		this.getViewState().put("PaymentType", paymentType);
	}

	/**
	 * Gets paymentType
	 * 
	 * @return paymentType the paymentType
	 */
	public String getPaymentType() {
		return (String) this.getViewState().get("PaymentType");
	}

	/**
	 * Sets costItem
	 * 
	 * @param costItem
	 *            the costItem to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setCostItem(String costItem)
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (costItem != null && !costItem.isEmpty()) {
			setCostTitle(BeansFactory.CostItems().Load(costItem).getValue());
		}
		this.getViewState().put("CostItem", costItem);

	}

	/**
	 * Gets costItem
	 * 
	 * @return costItem the costItem
	 */
	public String getCostItem() {
		return (String) this.getViewState().get("CostItem");
	}

	/**
	 * Sets docType
	 * 
	 * @param docType
	 *            the docType to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setDocType(String docType) throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (docType != null && !docType.isEmpty()) {
			setDocTitle(BeansFactory.DocumentTypes().Load(docType).getValue());
		}

		this.getViewState().put("DocType", docType);
	}

	/**
	 * Gets docType
	 * 
	 * @return docType the docType
	 */
	public String getDocType() {
		return (String) this.getViewState().get("DocType");
	}

	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public boolean getIsEdit() {
		return isEdit;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setCostDocumentUpFile(UploadedFile costDocumentUpFile) {
		this.costDocumentUpFile = costDocumentUpFile;
	}

	public UploadedFile getCostDocumentUpFile() {
		return costDocumentUpFile;
	}

	public void setCostDocument(Documents costDocument) {
		this.costDocument = costDocument;
	}

	public Documents getCostDocument() {
		return costDocument;
	}

	public void setCostDocTitle(String costDocTitle) {
		this.costDocTitle = costDocTitle;
	}

	public String getCostDocTitle() {
		return costDocTitle;
	}

	public void setPaymentDocTitle(String paymentDocTitle) {
		this.paymentDocTitle = paymentDocTitle;
	}

	public String getPaymentDocTitle() {
		return paymentDocTitle;
	}

	public void setPaymentDocumentUpFile(UploadedFile paymentDocumentUpFile) {
		this.paymentDocumentUpFile = paymentDocumentUpFile;
	}

	public UploadedFile getPaymentDocumentUpFile() {
		return paymentDocumentUpFile;
	}

	public void setPaymentDocument(Documents paymentDocument) {
		this.paymentDocument = paymentDocument;
	}

	public Documents getPaymentDocument() {
		return paymentDocument;
	}

	public void setDocList(List<Documents> docList) {
		this.docList = docList;
	}

	public List<Documents> getDocList() {
		return docList;
	}

	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile) {
		this.newDocumentUpFile = newDocumentUpFile;
	}

	public UploadedFile getNewDocumentUpFile() {
		return newDocumentUpFile;
	}

	public void setNewDocument(Documents newDocument) {
		this.newDocument = newDocument;
	}

	public Documents getNewDocument() {
		return newDocument;
	}

	/**
	 * Sets entityDeleteId
	 * 
	 * @param entityDeleteId
	 *            the entityDeleteId to set
	 */
	public void setEntityDeleteId(Integer entityDeleteId) {
		CostDefinitionEditBean.entityDeleteId = entityDeleteId;
	}

	/**
	 * Gets entityDeleteId
	 * 
	 * @return entityDeleteId the entityDeleteId
	 */
	public Integer getEntityDeleteId() {
		return entityDeleteId;
	}

	/**
	 * Sets entityDownloadId
	 * 
	 * @param entityDownloadId
	 *            the entityDownloadId to set
	 */
	public void setEntityDownloadId(Integer entityDownloadId) {
		CostDefinitionEditBean.entityDownloadId = entityDownloadId;
	}

	/**
	 * Gets entityDownloadId
	 * 
	 * @return entityDownloadId the entityDownloadId
	 */
	public Integer getEntityDownloadId() {
		return entityDownloadId;
	}

	/**
	 * Sets paymentTypeTitle
	 * 
	 * @param paymentTypeTitle
	 *            the paymentTypeTitle to set
	 */
	public void setPaymentTypeTitle(String paymentTypeTitle) {
		this.paymentTypeTitle = paymentTypeTitle;
	}

	/**
	 * Gets paymentTypeTitle
	 * 
	 * @return paymentTypeTitle the paymentTypeTitle
	 */
	public String getPaymentTypeTitle() {
		return paymentTypeTitle;
	}

	/**
	 * Sets paymentWayTitle
	 * 
	 * @param paymentWayTitle
	 *            the paymentWayTitle to set
	 */
	public void setPaymentWayTitle(String paymentWayTitle) {
		this.paymentWayTitle = paymentWayTitle;
	}

	/**
	 * Gets paymentWayTitle
	 * 
	 * @return paymentWayTitle the paymentWayTitle
	 */
	public String getPaymentWayTitle() {
		return paymentWayTitle;
	}

	/**
	 * Sets costTitle
	 * 
	 * @param costTitle
	 *            the costTitle to set
	 */
	public void setCostTitle(String costTitle) {
		this.costTitle = costTitle;
	}

	/**
	 * Gets costTitle
	 * 
	 * @return costTitle the costTitle
	 */
	public String getCostTitle() {
		return costTitle;
	}

	/**
	 * Sets docTitle
	 * 
	 * @param docTitle
	 *            the docTitle to set
	 */
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	/**
	 * Gets docTitle
	 * 
	 * @return docTitle the docTitle
	 */
	public String getDocTitle() {
		return docTitle;
	}

	/**
	 * Sets selectTab
	 * 
	 * @param selectTab
	 *            the selectTab to set
	 */
	public void setSelectTab(int selectTab) {
		this.selectTab = selectTab;
	}

	/**
	 * Gets selectTab
	 * 
	 * @return selectTab the selectTab
	 */
	public int getSelectTab() {
		return selectTab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityEditBean#getEntity()
	 */
	public CostDefinitions getEntity() {
		this.entity = (CostDefinitions) this.getViewState().get("costDefEntity");
		return this.entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityEditBean#setEntity(java.lang.Object)
	 */
	public void setEntity(CostDefinitions entity) {
		this.getViewState().put("costDefEntity", entity);
		this.entity = entity;
	}

	/**
	 * Sets notesList
	 * 
	 * @param notesList
	 *            the notesList to set
	 */
	public void setNotesList(List<CostDefinitionsToNotes> notesList) {
		this.getViewState().put("CostDefinitionsToNotes", notesList);
		this.notesList = notesList;
	}

	/**
	 * Gets notesList
	 * 
	 * @return notesList the notesList
	 */
	@SuppressWarnings("unchecked")
	public List<CostDefinitionsToNotes> getNotesList() {
		notesList = (List<CostDefinitionsToNotes>) this.getViewState().get("CostDefinitionsToNotes");
		return notesList;
	}

	/**
	 * Sets noteAdd
	 * 
	 * @param noteAdd
	 *            the noteAdd to set
	 */
	public void setNoteAdd(String noteAdd) {
		this.getViewState().put("noteAdd", noteAdd);
		this.noteAdd = noteAdd;
	}

	/**
	 * Gets noteAdd
	 * 
	 * @return noteAdd the noteAdd
	 */
	public String getNoteAdd() {
		noteAdd = (String) this.getViewState().get("noteAdd");
		return noteAdd;
	}

	/**
	 * Sets documentNumber
	 * 
	 * @param documentNumber
	 *            the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.getViewState().put("documentNumber", documentNumber);
	}

	/**
	 * Gets documentNumber
	 * 
	 * @return documentNumber the documentNumber
	 */
	public String getDocumentNumber() {
		return (String) this.getViewState().get("documentNumber");
	}

	/**
	 * Sets phases
	 * 
	 * @param phases
	 *            the phases to set
	 */
	public void setPhases(List<SelectItem> phases) {
		CostDefinitionEditBean.phases = phases;
	}

	/**
	 * Gets phases
	 * 
	 * @return phases the phases
	 */
	public List<SelectItem> getPhases() {
		return phases;
	}

	/**
	 * Sets phasesTitle
	 * 
	 * @param phasesTitle
	 *            the phasesTitle to set
	 */
	public void setPhasesTitle(String phasesTitle) {
		this.phasesTitle = phasesTitle;
	}

	/**
	 * Gets phasesTitle
	 * 
	 * @return phasesTitle the phasesTitle
	 */
	public String getPhasesTitle() {
		return phasesTitle;
	}

	/**
	 * Sets isDocView
	 * 
	 * @param isDocView
	 *            the isDocView to set
	 */
	public void setIsDocView(boolean isDocView) {
		this.isDocView = isDocView;
	}

	/**
	 * Gets isDocView
	 * 
	 * @return isDocView the isDocView
	 */
	public boolean getIsDocView() {
		return isDocView;
	}

	public String getCostDocRole() {
		return (String) this.getViewState().get("selectedCostRole");
	}

	public void setCostDocRole(String entity) {
		this.getViewState().put("selectedCostRole", entity);
	}

	public String getPaymentDocRole() {
		return (String) this.getViewState().get("selectedPaymentRole");
	}

	public void setPaymentDocRole(String entity) {
		this.getViewState().put("selectedPaymentRole", entity);
	}

	public String getPaymentAttachmentDocRole() {
		return (String) this.getViewState().get("selectedPaymentAttachmentRole");
	}

	public void setPaymentAttachmentDocRole(String entity) {
		this.getViewState().put("selectedPaymentAttachmentRole", entity);
	}

	public String getNewDocRole() {
		return (String) this.getViewState().get("selectedNewRole");
	}

	public void setNewDocRole(String entity) {
		this.getViewState().put("selectedNewRole", entity);
	}

	/**
	 * Sets listSelectRoles
	 * 
	 * @param listSelectRoles
	 *            the listSelectRoles to set
	 */
	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		this.getViewState().put("listSelectRoles", listSelectRoles);
	}

	/**
	 * Gets listSelectRoles
	 * 
	 * @return listSelectRoles the listSelectRoles
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListSelectRoles() {
		return (List<SelectItem>) this.getViewState().get("listSelectRoles");
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
		CostDefinitionEditBean.categories = categories;
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

	/**
	 * @param arrayList
	 */
	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}

	/**
	 * Gets saveAsDraft
	 * 
	 * @return saveAsDraft the saveAsDraft
	 */
	public boolean getSaveAsDraft() {
		return saveAsDraft == null ? false : saveAsDraft;
	}

	/**
	 * Sets saveAsDraft
	 * 
	 * @param saveAsDraft
	 *            the saveAsDraft to set
	 */
	public void setSaveAsDraft(boolean saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	/**
	 * Gets paymentAmount
	 * 
	 * @return paymentAmount the paymentAmount
	 */
	public String getPaymentAmount() {
		if (this.getViewState().containsKey("paymentAmount")) {
			return (String) getViewState().get("paymentAmount");
		}
		return null;
	}

	/**
	 * Sets paymentAmount
	 * 
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(String paymentAmount) {
		getViewState().put("paymentAmount", paymentAmount);
	}

	/**
	 * Gets amountReport
	 * 
	 * @return amountReport the amountReport
	 */
	public String getAmountReport() {
		if (getViewState().containsKey("amountReport")) {
			return (String) getViewState().get("amountReport");
		}
		return null;
	}

	/**
	 * Sets amountReport
	 * 
	 * @param amountReport
	 *            the amountReport to set
	 */
	public void setAmountReport(String amountReport) {
		getViewState().put("amountReport", amountReport);
	}

	/**
	 * Gets publicAmountReport
	 * 
	 * @return publicAmountReport the publicAmountReport
	 */
	public String getPublicAmountReport() {
		if (getViewState().containsKey("publicAmountReport")) {
			return (String) getViewState().get("publicAmountReport");
		}
		return null;
	}

	/**
	 * Sets publicAmountReport
	 * 
	 * @param publicAmountReport
	 *            the publicAmountReport to set
	 */
	public void setPublicAmountReport(String publicAmountReport) {
		getViewState().put("publicAmountReport", publicAmountReport);
	}

	/**
	 * Gets privateAmountReport
	 * 
	 * @return privateAmountReport the privateAmountReport
	 */
	public String getPrivateAmountReport() {
		if (getViewState().containsKey("privateAmountReport")) {
			return (String) getViewState().get("privateAmountReport");
		}
		return null;
	}

	/**
	 * Sets privateAmountReport
	 * 
	 * @param privateAmountReport
	 *            the privateAmountReport to set
	 */
	public void setPrivateAmountReport(String privateAmountReport) {
		getViewState().put("privateAmountReport", privateAmountReport);
	}

	public String getAmountToCoverAdvanceStateAid() {
		if (getViewState().containsKey("amountToCoverAdvanceStateAid")) {
			return (String) getViewState().get("amountToCoverAdvanceStateAid");
		}
		return null;
	}

	public void setAmountToCoverAdvanceStateAid(String amountToCoverAdvanceStateAid) {
		getViewState().put("amountToCoverAdvanceStateAid", amountToCoverAdvanceStateAid);
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

	/**
	 * Gets pageIndex
	 * 
	 * @return pageIndex the pageIndex
	 */
	public Integer getPageIndex() {
		if (this.getViewState().containsKey("pageIndex")) {
			return (Integer) this.getViewState().get("pageIndex");
		}
		return null;
	}

	/**
	 * Sets pageIndex
	 * 
	 * @param pageIndex
	 *            the pageIndex to set
	 */
	public void setPageIndex(Integer pageIndex) {
		this.getViewState().put("pageIndex", pageIndex);
	}

	/**
	 * Gets savedItemsPerPage
	 * 
	 * @return savedItemsPerPage the savedItemsPerPage
	 */
	public String getSavedItemsPerPage() {
		if (this.getViewState().containsKey("savedItemsPerPage")) {
			return (String) this.getViewState().get("savedItemsPerPage");
		}
		return null;
	}

	/**
	 * Sets savedItemsPerPage
	 * 
	 * @param savedItemsPerPage
	 *            the savedItemsPerPage to set
	 */
	public void setSavedItemsPerPage(String savedItemsPerPage) {
		this.getViewState().put("savedItemsPerPage", savedItemsPerPage);
	}

	public String getSelectedLocationId() {
		return (String) this.getViewState().get("selectedLocationId");
	}

	public void setSelectedLocationId(String selectedLocationId) {
		this.getViewState().put("selectedLocationId", selectedLocationId);
	}

	public String getSelectedGiuridicalEngagesId() {
		return (String) this.getViewState().get("selectedGiuridicalEngagesId");
	}

	public void setSelectedGiuridicalEngagesId(String giuridicalEngagesId) {
		this.getViewState().put("selectedGiuridicalEngagesId", giuridicalEngagesId);
	}

	public String getSelectedLocation() {
		return this.getEntity().getLocation().toString();
	}

	public String getSelectedGiuridicalEngages() {
		GiuridicalEngages item = getEntity().getGiuridicalEngages();
		if (item != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return String.valueOf(item.getActType() + " - " + item.getNumber() + " - " + sdf.format(item.getDate())
					+ " - " + item.getAmount());
		} else
			return Utils.getString(GIURIDICAL_ENGAGES_IMMEDIATE_ENGAGE);
	}

	/**
	 * Gets locations
	 * 
	 * @return locations the locations
	 */
	public List<SelectItem> getLocations() {
		return locations;
	}

	/**
	 * Sets locations
	 * 
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(List<SelectItem> locations) {
		CostDefinitionEditBean.locations = locations;
	}

	/**
	 * Gets locationLimitsErrorType
	 * 
	 * @return locationLimitsErrorType the locationLimitsErrorType
	 */
	public String getLocationLimitsErrorType() {
		return locationLimitsErrorType;
	}

	/**
	 * Sets locationLimitsErrorType
	 * 
	 * @param locationLimitsErrorType
	 *            the locationLimitsErrorType to set
	 */
	public void setLocationLimitsErrorType(String locationLimitsErrorType) {
		this.locationLimitsErrorType = locationLimitsErrorType;
	}

	/**
	 * Gets giuridicalEngages
	 * 
	 * @return giuridicalEngages the giuridicalEngages
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getGiuridicalEngagesList() {
		return (List<SelectItem>) this.getViewState().get("giuridicalEngagesList");
	}

	/**
	 * Sets giuridicalEngages
	 * 
	 * @param giuridicalEngages
	 *            the giuridicalEngages to set
	 */
	public void setGiuridicalEngagesList(List<SelectItem> giuridicalEngagesList) {
		this.getViewState().put("giuridicalEngagesList", giuridicalEngagesList);
	}

	/**
	 * Gets paymentAttachmentDocTitle
	 * 
	 * @return paymentAttachmentDocTitle the paymentAttachmentDocTitle
	 */
	public String getPaymentAttachmentDocTitle() {
		return paymentAttachmentDocTitle;
	}

	/**
	 * Sets paymentAttachmentDocTitle
	 * 
	 * @param paymentAttachmentDocTitle
	 *            the paymentAttachmentDocTitle to set
	 */
	public void setPaymentAttachmentDocTitle(String paymentAttachmentDocTitle) {
		this.paymentAttachmentDocTitle = paymentAttachmentDocTitle;
	}

	/**
	 * Gets paymentAttachmentDocumentUpFile
	 * 
	 * @return paymentAttachmentDocumentUpFile the
	 *         paymentAttachmentDocumentUpFile
	 */
	public UploadedFile getPaymentAttachmentDocumentUpFile() {
		return paymentAttachmentDocumentUpFile;
	}

	/**
	 * Sets paymentAttachmentDocumentUpFile
	 * 
	 * @param paymentAttachmentDocumentUpFile
	 *            the paymentAttachmentDocumentUpFile to set
	 */
	public void setPaymentAttachmentDocumentUpFile(UploadedFile paymentAttachmentDocumentUpFile) {
		this.paymentAttachmentDocumentUpFile = paymentAttachmentDocumentUpFile;
	}

	/**
	 * Gets paymentAttachmentDocument
	 * 
	 * @return paymentAttachmentDocument the paymentAttachmentDocument
	 */
	public Documents getPaymentAttachmentDocument() {
		return paymentAttachmentDocument;
	}

	/**
	 * Sets paymentAttachmentDocument
	 * 
	 * @param paymentAttachmentDocument
	 *            the paymentAttachmentDocument to set
	 */
	public void setPaymentAttachmentDocument(Documents paymentAttachmentDocument) {
		this.paymentAttachmentDocument = paymentAttachmentDocument;
	}

	public void setAdditionalFinansing(Boolean additionalFinansing) {
		this.getViewState().put("additionalFinansing", additionalFinansing);
	}

	public Boolean getAdditionalFinansing() {
		return (Boolean) this.getViewState().get("additionalFinansing");
	}

	public void setAdditionalFinansingAmount(String aditionalFinansingAmount) {
		this.getViewState().put("aditionalFinansingAmount", aditionalFinansingAmount);
	}

	public String getAdditionalFinansingAmount() {
		return (String) this.getViewState().get("aditionalFinansingAmount");
	}

	public void setStateAid(Boolean stateAid) {
		this.getViewState().put("stateAid", stateAid);
	}

	public Boolean getStateAid() {
		return (Boolean) this.getViewState().get("stateAid");
	}

	// public Boolean getFlatRateCost() {
	// return (Boolean) this.getViewState().get("flatRateCost");
	// }
	//
	// public void setFlatRateCost(Boolean flatRateCost) {
	// this.getViewState().put("flatRateCost", flatRateCost);
	// }

	// public String getExpenditureOutsideEligibleAreas() {
	// return (String)
	// this.getViewState().get("expenditureOutsideEligibleAreas");
	// }
	//
	// public void setExpenditureOutsideEligibleAreas(String
	// expenditureOutsideEligibleAreas) {
	// this.getViewState().put("expenditureOutsideEligibleAreas",
	// expenditureOutsideEligibleAreas);
	// }

	public String getExpenditureOutsideEligibleAreasAmount() {
		return (String) this.getViewState().get("expenditureOutsideEligibleAreasAmount");
	}

	public void setExpenditureOutsideEligibleAreasAmount(String expenditureOutsideEligibleAreasAmount) {
		this.getViewState().put("expenditureOutsideEligibleAreasAmount", expenditureOutsideEligibleAreasAmount);
	}

	public Boolean getExpenditureForLandPurchaseAmount() {
		return (Boolean) this.getViewState().get("expenditureForLandPurchase");
	}

	public void setExpenditureForLandPurchase(Boolean expenditureForLandPurchase) {
		this.getViewState().put("expenditureForLandPurchase", expenditureForLandPurchase);
	}

	// public String getInkindContributions() {
	// return (String) this.getViewState().get("inkindContributions");
	// }
	//
	// public void setInkindContributions(String inkindContributions) {
	// this.getViewState().put("inkindContributions", inkindContributions);
	// }

	public String getInkindContributionsAmount() {
		return (String) this.getViewState().get("inkindContributionsAmount");
	}

	public void setInkindContributionsAmount(String inkindContributionsAmount) {
		this.getViewState().put("inkindContributionsAmount", inkindContributionsAmount);
	}

	public void setShowAlert(Boolean showAlert) {
		this.getViewState().put("showAlert", showAlert);
	}

	public Boolean getShowAlert() {
		return (Boolean) this.getViewState().get("showAlert");
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCostItemsAsse3() {
		return (List<SelectItem>) this.getViewState().get("costItemsAsse3");
	}

	public void setCostItemsAsse3(List<SelectItem> costItemsAsse3) {
		this.getViewState().put("costItemsAsse3", costItemsAsse3);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCostItemsTypology3() {
		return (List<SelectItem>) this.getViewState().get("costItemsTypology3");
	}

	public void setCostItemsTypology3(List<SelectItem> costItemsTypology3) {
		this.getViewState().put("costItemsTypology3", costItemsTypology3);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCostPhasesAsse3() {
		return (List<SelectItem>) this.getViewState().get("costPhasesAsse3");
	}

	public void setCostPhasesAsse3(List<SelectItem> costPhasesAsse3) {
		this.getViewState().put("costPhasesAsse3", costPhasesAsse3);
	}

	public Boolean getAsse3Mode() {
		return (Boolean) this.getViewState().get("asse3Mode");
	}

	public void setAsse3Mode(Boolean asse3Mode) {
		this.getViewState().put("asse3Mode", asse3Mode);
	}

	public Boolean getTypology3Mode() {
		return (Boolean) this.getViewState().get("typology3Mode");
	}

	public void setTypology3Mode(Boolean typology3Mode) {
		this.getViewState().put("typology3Mode", typology3Mode);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCostPhasesTypology3() {
		return (List<SelectItem>) this.getViewState().get("costPhasesTypology3");
	}

	public void setCostPhasesTypology3(List<SelectItem> costPhasesTypology3) {
		this.getViewState().put("costPhasesTypology3", costPhasesTypology3);
	}

	public void setSelectedPhaseAsse3(String selectedPhaseAsse3) {
		this.getViewState().put("selectedPhaseAsse3", selectedPhaseAsse3);
	}

	public String getSelectedPhaseAsse3() {
		return (String) this.getViewState().get("selectedPhaseAsse3");
	}

	public void setSelectedCostItemAsse3(String selectedCostItemAsse3) {
		this.getViewState().put("selectedCostItemAsse3", selectedCostItemAsse3);
	}

	public void setSelectedPhaseTypology3(String selectedPhaseTypology3) {
		this.getViewState().put("selectedPhaseTypology3", selectedPhaseTypology3);
	}

	public String getSelectedPhaseTypology3() {
		return (String) this.getViewState().get("selectedPhaseTypology3");
	}

	public void setSelectedCostItemTypology3(String selectedCostItemTypology3) {
		this.getViewState().put("selectedCostItemTypology3", selectedCostItemTypology3);
	}

	public String getSelectedCostItemAsse3() {
		return (String) this.getViewState().get("selectedCostItemAsse3");
	}

	public String getSelectedCostItemTypology3() {
		return (String) this.getViewState().get("selectedCostItemTypology3");
	}

	public String getAsse3PhaseTitle() {
		return asse3PhaseTitle;
	}

	public void setAsse3PhaseTitle(String asse3PhaseTitle) {
		this.asse3PhaseTitle = asse3PhaseTitle;
	}

	public String getAsse3CostItemsTitle() {
		return asse3CostItemsTitle;
	}

	public void setAsse3CostItemsTitle(String asse3CostItemsTitle) {
		this.asse3CostItemsTitle = asse3CostItemsTitle;
	}

	public boolean isRenderAmountToCoverAdvanceStateAid() {
		// return renderAmountToCoverAdvanceStateAid;
		return (boolean) this.getViewState().get("renderAmountToCoverAdvanceStateAid");
	}

	public void setRenderAmountToCoverAdvanceStateAid(boolean renderAmountToCoverAdvanceStateAid) {
		// this.renderAmountToCoverAdvanceStateAid =
		// renderAmountToCoverAdvanceStateAid;
		this.getViewState().put("renderAmountToCoverAdvanceStateAid", renderAmountToCoverAdvanceStateAid);
	}

	public String getTransfersId() {
		return (String) this.getViewState().get("transfersId");
	}

	public void setTransfersId(String transfersId) {
		this.getViewState().put("transfersId", transfersId);
	}

	/**
	 * Gets paymentTypeCode.
	 * 
	 * @return paymentTypeCode
	 */
	public String getPaymentTypeCode() {
		if (this.getViewState().get("costDefinitionPaymentTypeCode") != null) {
			return String.valueOf(this.getViewState().get("costDefinitionPaymentTypeCode"));
		} else {
			return null;
		}
	}

	/**
	 * Sets paymentTypeCode.
	 * 
	 * @param paymentTypeCode
	 */
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.getViewState().put("costDefinitionPaymentTypeCode", paymentTypeCode);
	}

	/**
	 * Gets payment type description with the specified paymentTypeCode.
	 * 
	 * @return payment type description
	 */
	public String getPaymentTypeDescription() {
		if ((this.getPaymentTypeCode() != null) && !this.getPaymentTypeCode().isEmpty()) {
			for (PaymentType paymentType : PaymentType.values()) {
				if (paymentType.code.equals(this.getPaymentTypeCode())) {
					Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
					ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

					return resourceBundle.getString(paymentType.description);
				}
			}
		}

		return "";
	}

	/**
	 * Gets listPaymentTypeFn06.
	 * 
	 * @return listPaymentTypeFn06
	 */
	public List<SelectItem> getListPaymentTypeFn06() {
		return listPaymentTypeFn06;
	}

	/**
	 * Sets listPaymentTypeFn06.
	 * 
	 * @param listPaymentTypeFn06
	 */
	public void setListPaymentTypeFn06(List<SelectItem> listPaymentTypeFn06) {
		this.listPaymentTypeFn06 = listPaymentTypeFn06;
	}

	/**
	 * Fill payment type list
	 */
	private void fillPaymentTypeFn06() {
		this.listPaymentTypeFn06 = new ArrayList<SelectItem>();
		this.listPaymentTypeFn06.add(SelectItemHelper.getFirst());

		Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

		for (PaymentType paymentType : PaymentType.values()) {
			SelectItem item = new SelectItem();
			item.setLabel(resourceBundle.getString(paymentType.description));
			item.setValue(paymentType.code);

			this.listPaymentTypeFn06.add(item);
		}

		if ((this.getPaymentTypeCode() == null)
				|| this.getPaymentTypeCode().equals(SelectItemHelper.getFirst().getValue())) {
			if ((this.getEntity() != null) && (this.getEntity().getPaymentTypeCode() != null)
					&& !this.getEntity().getPaymentTypeCode().equals(SelectItemHelper.getFirst().getValue())) {
				this.setPaymentTypeCode(this.getEntity().getPaymentTypeCode());
			}
		}
	}

	/**
	 * Gets paymentMotivationCode.
	 * 
	 * @return paymentMotivationCode
	 */
	public String getPaymentMotivationCode() {
		return paymentMotivationCode;
	}

	/**
	 * Sets paymentMotivationCode.
	 * 
	 * @param paymentMotivationCode
	 */
	public void setPaymentMotivationCode(String paymentMotivationCode) {
		this.paymentMotivationCode = paymentMotivationCode;
	}

	/**
	 * Gets payment motivation description with the specified
	 * paymentMotivationCode.
	 * 
	 * @return payment motivation description
	 */
	public String getPaymentMotivationDescription() {
		if ((this.getPaymentTypeCode() != null) && !this.getPaymentTypeCode().isEmpty()
				&& (this.getPaymentMotivationCode() != null) && !this.getPaymentMotivationCode().isEmpty()) {
			Map<String, String> paymentMotivationMap = PaymentCausality.getPaymentCausality(this.getPaymentTypeCode());
			for (String key : paymentMotivationMap.keySet()) {
				if (key.equals(this.getPaymentMotivationCode())) {
					Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
					ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);

					return resourceBundle.getString(paymentMotivationMap.get(key));
				}
			}
		}

		return "";
	}

	/**
	 * Gets listPaymentMotivation.
	 * 
	 * @return listPaymentMotivation
	 */
	public List<SelectItem> getListPaymentMotivation() {
		return listPaymentMotivation;
	}

	/**
	 * Sets listPaymentMotivation.
	 * 
	 * @param listPaymentMotivation
	 */
	public void setListPaymentMotivation(List<SelectItem> listPaymentMotivation) {
		this.listPaymentMotivation = listPaymentMotivation;
	}

	public boolean isCfpartnerPublic() {
		return cfpartnerPublic;
	}

	public void setCfpartnerPublic(boolean cfpartnerPublic) {
		this.cfpartnerPublic = cfpartnerPublic;
	}

	public String getFesrCnValue() {
		return fesrCnValue;
	}

	public void setFesrCnValue(String fesrCnValue) {
		this.fesrCnValue = fesrCnValue;
	}

	public List<SelectItem> getTransfers() {
		// return transfers;
		return (List<SelectItem>) this.getViewState().get("transfers");
	}

	public void setTransfers(List<SelectItem> transfers) {
		// this.transfers = transfers;
		this.getViewState().put("transfers", transfers);
	}

	/**
	 * Fill payment motivation list
	 */
	// private void fillPaymentMotivation() {
	// this.listPaymentMotivation = new ArrayList<SelectItem>();
	// this.listPaymentMotivation.add(SelectItemHelper.getFirst());
	//
	// if ((this.getPaymentTypeCode()) != null
	// &&
	// !this.getPaymentTypeCode().equals(SelectItemHelper.getFirst().getValue()))
	// {
	// Locale currentLocale =
	// FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	// ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources",
	// currentLocale);
	//
	// Map<String, String> paymentCausalityMap =
	// PaymentCausality.getPaymentCausality(this.getPaymentTypeCode());
	// for (String key : paymentCausalityMap.keySet()) {
	// SelectItem item = new SelectItem();
	// item.setLabel(resourceBundle.getString(paymentCausalityMap.get(key)));
	// item.setValue(key);
	//
	// this.listPaymentMotivation.add(item);
	// }
	// }
	// }

	/**
	 * paymentTypeCode on change event
	 * 
	 * @param event
	 * @throws NumberFormatException
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 */
	// public void paymentTypeCodeChange(ValueChangeEvent event) {
	// this.setPaymentTypeCode(String.valueOf(event.getNewValue()));
	// this.fillPaymentMotivation();
	// this.setPaymentMotivationCode(String.valueOf(SelectItemHelper.getFirst().getValue()));
	// }

	public String getProjectTypology() {
		Long result = null;
		try {
			result = this.getProject().getTypology().getId();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(result);
	}
}
