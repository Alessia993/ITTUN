package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.SuperAdminHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsWrapper;
import com.infostroy.paamns.web.beans.wrappers.Pair;

public class ValidationFlowViewBean extends
		EntityProjectListBean<CostDefinitions>
{

	private static final String		all			= "all";

	private static final String		validated	= "validated";

	private static final String		refused		= "refused";

	private static final String		toValidate	= "to validate";

	private String					filterValuePA;

	private UploadedFile			newDocumentUpFile;

	private Documents				newDocument;

	private String					newDocTitle;
	
	private UploadedFile			newDocumentUpFileCF;

	private Documents				newDocumentCF;

	private String					newDocTitleCF;

	private String					error;

	private List<SelectItem>		filterValuesPA;

	private String					filterValueD;

	private List<SelectItem>		filterValuesD;

	private String					filterValueCIL;

	private List<SelectItem>		filterValuesCIL;

	private List<SelectItem>		listPartners;

	private Long					rowId;

	private String					defaultError;

	private List<SelectItem>		filterValidationStatusValues;

	private List<SelectItem>		filterCostItemValues;

	private String					partnerID;

	private String					costDefID;
	
	private String 					costDefReportNumber;

	private String					durNumber;

	private List<SelectItem>		filterPartnerValues;

	private HtmlDataScroller		scroller;

	private List<SelectItem>		filterPhases;
	
	private List<SelectItem>		filterLocations;

	private static List<SelectItem>	categories;

	private String					selectedCategory;
	
	private String					selectedCategoryCF;
	
	private String					actionMotivation;
	private String 					attachmentIsRequredError;

	public void deleteNewDoc()
	{
		if (this.getViewState().get("newdocument") != null)
		{
			this.getViewState().put("newdocument", null);
			// this.getViewState().put("savedDocument", null);
		}

		if (this.getViewState().get("savedDocument") != null)
		{
			Transaction tr = null;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();

				for (CostDefinitions item : getList())
				{
					if (item.getTempDocument() != null)
					{
						BeansFactory.Documents().DeleteInTransaction(
								item.getTempDocument());
						item.setTempDocument(null);
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}
				}

				tr.commit();
				this.getViewState().put("savedDocument", null);
			}
			catch (HibernateException e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
				addStackTrace(e);
			}
			catch (PersistenceBeanException e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
				addStackTrace(e);
			}
		}

		try
		{
			setNewDocTitle(null);
			// this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
	}
	
	public void deleteNewDocCF()
	{
		if (this.getViewState().get("newdocumentCF") != null)
		{
			this.getViewState().put("newdocumentCF", null);
			// this.getViewState().put("savedDocument", null);
		}

		if (this.getViewState().get("savedDocumentCF") != null)
		{
			Transaction tr = null;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();

				for (CostDefinitions item : getList())
				{
					if (item.getTempDocumentCF() != null)
					{
						BeansFactory.Documents().DeleteInTransaction(
								item.getTempDocumentCF());
						item.setTempDocumentCF(null);
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}
				}

				tr.commit();
				this.getViewState().put("savedDocumentCF", null);
			}
			catch (HibernateException e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
				addStackTrace(e);
			}
			catch (PersistenceBeanException e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
				addStackTrace(e);
			}
		}

		try
		{
			setNewDocTitleCF(null);
			// this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
	}
	
	public void fillDropDown() throws HibernateException,
			PersistenceBeanException
	{
		this.filterValidationStatusValues = new ArrayList<SelectItem>();
		this.filterValidationStatusValues.add(SelectItemHelper.getAllElement());
		for (CostStates item : BeansFactory.CostStates().Load())
		{
			if (item.getId().equals(CostStateTypes.CILVerify.getState())
					|| item.getId().equals(
							CostStateTypes.DAECApproval.getState())
					|| item.getId()
							.equals(CostStateTypes.CFValidate.getState())
					||  item.getId()
							.equals(CostStateTypes.Draft.getState()))
			{
				if (this.getSessionBean().isDAEC()
						&& item.getId().equals(
								CostStateTypes.CILVerify.getState()))
				{
					continue;
				}
				this.filterValidationStatusValues.add(new SelectItem(item
						.getId().toString(), item.getValue()));
			}
		}

	
		this.setFilterPartnerValues(new ArrayList<SelectItem>());
		this.setFilterLocations(new ArrayList<SelectItem>());
		this.filterPartnerValues.add(SelectItemHelper.getAllElement());
		this.filterLocations.add(SelectItemHelper.getAllElement());

		
		for(LocationForCostDef loc : LocationForCostDef.values()){
			filterLocations.add(new SelectItem(loc.name(), loc.toString()));
		}

		List<CFPartnerAnagraphs> listPart = BeansFactory.CFPartnerAnagraphs()
				.LoadByProject(this.getProjectId());
		for (CFPartnerAnagraphs partner : listPart)
		{
			if (partner.getUser() == null)
			{
				if (this.getProject().getAsse() == 5)
				{
					this.filterPartnerValues.add(new SelectItem("-1", partner
							.getNaming()));
				}
			}
			else
			{
				this.filterPartnerValues.add(new SelectItem(String
						.valueOf(partner.getUser().getId()), partner
						.getNaming()));
			}
		}
		
		this.setFilterCostItemValues(new ArrayList<SelectItem>());
		if (Boolean.FALSE.equals(this.getAsse3Mode()))
		{
			fillPhases();
			this.filterCostItemValues.add(SelectItemHelper.getAllElement());
			for (CostItems item : BeansFactory.CostItems().Load())
			{
				filterCostItemValues.add(new SelectItem(
						item.getId().toString(), item.getValue()));
			}
		}
		else
		{
			FillAsse3Phases();
			FillCostItemsAsse3();
		}

		fillCategories();
	}
	
	public void asse3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException{
		this.setSelectedCostPhaseAsse3((String)event.getNewValue());
		this.FillCostItemsAsse3();
	}

	private void fillCategories() throws PersistenceBeanException
	{
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load())
		{
			categories.add(new SelectItem(String.valueOf(item.getId()), item
					.getName()));
		}
	}

	private void fillPhases() throws PersistenceBeanException
	{
		setFilterPhases(new ArrayList<SelectItem>());
		List<CostDefinitionPhases> list = BeansFactory.CostDefinitionPhase()
				.Load();
		getFilterPhases().add(SelectItemHelper.getAllElement());
		for (CostDefinitionPhases item : list)
		{
			Integer value = null;
			try
			{
				value = Integer.valueOf(item.getCode());
			}
			catch (NumberFormatException e)
			{

			}

			if (value != null && value != 20)
			{
				getFilterPhases().add(
						new SelectItem(String.valueOf(item.getId()), item
								.getValue()));
			}
			else if (value != null && value == 20)
			{
				getFilterPhases().add(
						new SelectItem(String.valueOf(item.getId()), Utils
								.getString("Resources",
										"costDefinitionNonApplicable", null)));
			}
		}
	}

	public void closeItem()
	{

		try
		{
			CostDefinitions cost = BeansFactory.CostDefinitions().Load(
					this.getEntityDeleteId());
			cost.setCostState(BeansFactory.CostStates().Load(
					CostStateTypes.Closed.getState()));
			cost.setWasRefusedByCil(false);
			BeansFactory.CostDefinitions().Save(cost);
			this.Page_Load_Static();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			addStackTrace(e);
		}
	}

	public void getNewDoc()
	{
		if (this.getViewState().get("newdocument") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"newdocument");

			FileHelper.sendFile(docinfo, false);
		}
	}
	
	public void getNewDocCF()
	{
		if (this.getViewState().get("newdocumentCF") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"newdocumentCF");

			FileHelper.sendFile(docinfo, false);
		}
	}

	public String updateSuperAdminStep()
	{
		if (this.getActionMotivation() == null
				|| this.getActionMotivation().isEmpty())
		{
			return null;
		}
		saveIntermediateStep();
		clearMotivation(null);
		return null;
	}

	public void saveIntermediateStep()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			Documents doc = null;
			try
			{
				doc = saveDoc(false);
			}
			catch (IOException e)
			{
				log.error(e);
			}
			boolean saved = false;
			for (CostDefinitions item : getList())
			{
				if (item.getSelected()
						&& (!this.getSessionBean().isSuperAdmin()
								&& (Boolean.FALSE.equals(item.getLocked()) || item.getLocked()==null )|| this
								.getSessionBean().isSuperAdmin()
								&& Boolean.TRUE.equals(item.getLocked())))
				{
					item.setCilIntermediateStepSave(true);
					if (getSessionBean().isSuperAdmin())
					{
						Map<Long, CostDefinitionsWrapper> oldValues = (Map<Long, CostDefinitionsWrapper>) this
								.getViewState().get("oldValues");
						CostDefinitionsWrapper oldItem = oldValues.get(item
								.getId());
						SuperAdminChange changes = new SuperAdminChange(
								ChangeType.VALUE_CHANGE, this.getCurrentUser(),
								this.getActionMotivation(), getSessionBean()
										.getProject());
						StringBuilder changeLogMessages = new StringBuilder();
						if (!(item.getCilDate() != null
								&& oldItem.getCilDate() != null && item
								.getCilDate().getTime() == oldItem.getCilDate()
								.getTime())
								&& !(oldItem.getCilDate() == null && item
										.getCilDate() == null))
						{
							changeLogMessages
									.append(Utils
											.getString(
													"superAdminChangeLogMessage")
											.replace("%1",
													Utils.getString("date"))
											.replace(
													"%2",
													oldItem.getCilDate() == null ? ""
															: DateTime
																	.ToString(oldItem
																			.getCilDate()))
											.replace(
													"%3",
													item.getCilDate() == null ? ""
															: DateTime
																	.ToString(item
																			.getCilDate())))
									.append("<br/>");
							oldItem.setCilDate(item.getCilDate());
						}
						if (item.getCilCheck() != null
								&& !item.getCilCheck().equals(
										oldItem.getCilCheck())
								|| oldItem.getCilCheck() != null
								&& !oldItem.getCilCheck().equals(
										item.getCilCheck()))
						{
							changeLogMessages
									.append(Utils
											.getString(
													"superAdminChangeLogMessage")
											.replace("%1",
													Utils.getString("check"))
											.replace(
													"%2",
													oldItem.getCilCheck() == null ? ""
															: oldItem
																	.getCilCheck()
																	.toString())
											.replace(
													"%3",
													item.getCilCheck() == null ? ""
															: item.getCilCheck()
																	.toString()))
									.append("<br/>");
							oldItem.setCilCheck(item.getCilCheck());
							oldItem.setCilCheckPublicAmount(item.getCilCheckPublicAmount());
							oldItem.setCilCheckPrivateAmount(item.getCilCheckPrivateAmount());
							oldItem.setCilCheckAdditionalFinansingAmount(item.getCilCheckAdditionalFinansingAmount());
							oldItem.setCilCheckStateAidAmount(item.getCilCheckStateAidAmount());
							oldItem.setCilCheckOutsideEligibleAreas(item.getCilCheckOutsideEligibleAreas());
							oldItem.setCilCheckInkindContributions(item.getCilCheckInkindContributions());
						}
						if (item.getCustomNoteWeb() != null
								&& !item.getCustomNoteWeb().equals(
										oldItem.getCustomNote())
								|| oldItem.getCustomNote() != null
								&& !oldItem.getCustomNote().equals(
										item.getCustomNoteWeb()))
						{
							changeLogMessages
									.append(Utils
											.getString(
													"superAdminChangeLogMessage")
											.replace("%1",
													Utils.getString("notes"))
											.replace(
													"%2",
													oldItem.getCustomNote() == null ? ""
															: oldItem
																	.getCustomNote())
											.replace(
													"%3",
													item.getCustomNote() == null ? ""
															: item.getCustomNote()))
									.append("<br/>");
							oldItem.setCustomNote(item.getCustomNote());
						}
						if (changeLogMessages.length() > 0)
						{
							changeLogMessages
									.insert(0,
											Utils.getString(
													"superAdminChangeLogMessageHeader")
													.replace(
															"%1",
															Utils.getString("superAdminChangesCostDefinition"))
													.replace(
															"%2",
															item.getProgressiveId()
																	.toString())
													.replace(
															"%3",
															this.getRequestUrl())
													+ "<br/>");

							changes.setChanges(changeLogMessages.toString());
							BeansFactory.SuperAdminChanges().SaveInTransaction(
									changes);
						}
					}
					if (!saved)
					{
						if (doc != null)
						{
							item.setTempDocument(doc);
							saved = true;

						}
						
						
						//FacesContext.getCurrentInstance().addMessage(
						//		"sdfsdf",
						//		new FacesMessage(Utils.getString(
						//				"superAdminSaveSuccess").replace("%1",
							//			item.getProgressiveId().toString())));

					}
					item.setCustomNote(item.getCustomNoteWeb());
				}
				else
				{
					item.setCilIntermediateStepSave(false);
				}
				BeansFactory.CostDefinitions().SaveInTransaction(item);
			}

			tr.commit();
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
		}
		FillLabels();

	}

	public void SaveNewDocument()
	{
		try
		{
			if (getNewDocumentUpFile() != null && getNewDocument() != null)
			{
				String fileName = FileHelper
						.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null
						&& !this.getSelectedCategory().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(getNewDocument()
						.getDocumentDate(), getNewDocumentUpFile().getName(),
						fileName, getNewDocument().getProtocolNumber(), cat,
						getNewDocument().getIsPublic(), getNewDocument().getSignflag());
				this.getViewState().put("newdocument", doc);
			}
			this.Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
			addStackTrace(e);
		}

	}

	
	public void SaveNewDocumentCF()
	{
		try
		{
			if (getNewDocumentUpFileCF() != null && getNewDocumentCF() != null)
			{
				String fileName = FileHelper
						.newTempFileName(getNewDocumentUpFileCF().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFileCF().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategoryCF() != null
						&& !this.getSelectedCategoryCF().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategoryCF());
				}
				DocumentInfo doc = new DocumentInfo(getNewDocumentCF()
						.getDocumentDate(), getNewDocumentUpFileCF().getName(),
						fileName, getNewDocumentCF().getProtocolNumber(), cat,
						getNewDocumentCF().getIsPublic(),getNewDocumentCF().getSignflag());
				this.getViewState().put("newdocumentCF", doc);
			}
			this.Page_Load();
		}
		catch (HibernateException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (IOException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			addStackTrace(e);
		}

	}
	
	/**
	 * Stores tr value of entity.
	 */
	private Transaction	tr;

	private boolean		selectAll;

	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{

		this.setDefaultError(Utils.getString("Resources",
				"validatorCheckAllFields", null));
		this.setFieldError(Utils.getString("Resources", "validatorMessage",
				null));

		if (this.getViewState().get("newdocument") != null)
		{
			DocumentInfo doc = (DocumentInfo) this.getViewState().get(
					"newdocument");
			setNewDocTitle(doc.getName());
		}
		else
		{
			setNewDocTitle(null);
		}

		if (newDocument == null)
		{
			newDocument = new Documents();
			newDocument.setDocumentDate(new Date());
		}
		
		if (this.getViewState().get("newdocumentCF") != null)
		{
			DocumentInfo doc = (DocumentInfo) this.getViewState().get(
					"newdocumentCF");
			setNewDocTitleCF(doc.getName());
		}
		else
		{
			setNewDocTitleCF(null);
		}

		if (newDocumentCF == null)
		{
			newDocumentCF = new Documents();
			newDocumentCF.setDocumentDate(new Date());
		}
		
		this.FillFilters();

		if (this.filterValueCIL == null)
		{
			this.filterValueCIL = "all";
		}

		if (this.getFilterValueD() == null
				|| String.valueOf(this.getFilterValueD()).isEmpty())
		{
			this.setFilterValueD("all");
		}

		if (this.getFilterValuePA() == null)
		{
			this.setFilterValuePA(String.valueOf(CostStateTypes.CILVerify
					.getState()));
		}

		List<Long> ids = (List<Long>) this.getViewState()
				.get("costDefinitions");

		if (ids == null)
		{
			ids = new ArrayList<Long>();
		}
		for (CostDefinitions item : getList())
		{
			item.setSelected(ids.contains(item.getId()));
			if(!item.getSelected()){
				item.setCustomNoteWeb(item.getCustomNote());
			}
		


			
			if (item.getCilIntermediateStepSave()
					&& item.getCostStateId().equals(
							CostStateTypes.CILVerify.getState().toString()))
			{
				if (item.getTempDocument() != null
						&& this.getViewState().get("savedDocument") == null)
				{
					this.getViewState().put("savedDocument",
							item.getTempDocument());
					this.getViewState().put(
							"newdocument",
							new DocumentInfo(item.getTempDocument()
									.getDocumentDate(), item.getTempDocument()
									.getName(), item.getTempDocument()
									.getFileName(), item.getTempDocument()
									.getProtocolNumber(), item
									.getTempDocument().getCategory(), null));
					// TODO
					setNewDocTitle(item.getTempDocument().getName());
				}
				
				if (item.getTempDocumentCF() != null
						&& this.getViewState().get("savedDocumentCF") == null)
				{
					this.getViewState().put("savedDocumentCF",
							item.getTempDocument());
					this.getViewState().put(
							"newdocumentCF",
							new DocumentInfo(item.getTempDocumentCF()
									.getDocumentDate(), item.getTempDocumentCF()
									.getName(), item.getTempDocumentCF()
									.getFileName(), item.getTempDocumentCF()
									.getProtocolNumber(), item
									.getTempDocumentCF().getCategory(), null));
					// TODO
					setNewDocTitle(item.getTempDocument().getName());
				}
			}
			else if(item.getCilIntermediateStepSave()
					&& item.getCostStateId().equals(
							CostStateTypes.CILVerify.getState().toString())){
				item.setCilDate(new Date());
			}

			CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
					.GetByUser(item.getUser().getId());

			if (partner != null)
			{
				item.setUserInsertedName(partner.getName());
			}
			else
			{
				item.setUserInsertedName(item.getUser().getName());
			}
		}

		if (this.getSessionBean().isCIL())
		{
			listPartners = BeansFactory.CFPartnerAnagraphProject()
					.getPartnersForCil2(this.getProjectId(),
							this.getCurrentUser().getId());
		}
		else
		{
			listPartners = new ArrayList<SelectItem>();
		}

		fillDropDown();
		// search section
		List<CostDefinitions> tempList = new ArrayList<CostDefinitions>();
		for (CostDefinitions cd : this.getList())
		{
			if (this.getFilterValidationStatus() != null
					&& !this.getFilterValidationStatus().isEmpty())
			{
				if (!cd.getCostStateId().equals(getFilterValidationStatus()))
				{
					continue;
				}
			}
			if (this.getDurNumber() != null && !this.getDurNumber().isEmpty())
			{
				if (cd.getDurCompilation() != null)
				{
					Long idDur = Long.parseLong(this.getDurNumber());
					DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
							cd.getDurCompilation().getId());
					if (di == null || di.getDurInfoNumber() == null)
					{
						continue;
					}
					Long infoNumber = Long.parseLong(di.getDurInfoNumber()
							.toString());
					if (infoNumber == null || !infoNumber.equals(idDur))
					{
						continue;
					}
				}
				else
				{
					continue;
				}
			}
			
			if (Boolean.TRUE.equals(this.getAsse3Mode()))
			{
				if (this.getSelectedCostPhaseAsse3() != null
						&& !this.getSelectedCostPhaseAsse3().isEmpty())
				{

					if (cd.getCostItemPhaseAsse3().getPhase() == null
							|| !cd.getCostItemPhaseAsse3().getPhase().name()
									.equals(this.getSelectedCostPhaseAsse3()))
					{
						continue;
					}

				}

				if (this.getSelectedCostItemAsse3() != null
						&& !this.getSelectedCostItemAsse3().isEmpty())
				{

					if (cd.getCostItemPhaseAsse3().getPhase() == null
							|| !cd.getCostItemPhaseAsse3().getValue()
									.equals(this.getSelectedCostItemAsse3()))
					{
						continue;
					}

				}
			}
			else
			{
				if (this.getFilterPhase() != null
						&& !this.getFilterPhase().equals(0l))
				{
					if (cd.getCostDefinitionPhase() == null
							|| !cd.getCostDefinitionPhase().getId()
									.equals(this.getFilterPhase()))
					{
						continue;
					}
				}

				if (this.getFilterCostItem() != null
						&& !this.getFilterCostItem().isEmpty())
				{
					if (!cd.getCostItem().getId()
							.equals(Long.parseLong(this.getFilterCostItem())))
					{
						continue;
					}
				}
			}
			
			if (this.getDocumentDate() != null)
			{
				if (cd.getCostDocument() == null
						|| cd.getCostDocument().getDocumentDate() == null)
				{
					continue;
				}
				Calendar firstDate = new GregorianCalendar();
				firstDate.setTime(this.getDocumentDate());
				firstDate.clear(Calendar.HOUR);
				firstDate.clear(Calendar.MINUTE);
				firstDate.clear(Calendar.SECOND);
				firstDate.clear(Calendar.MILLISECOND);

				Calendar secondDate = new GregorianCalendar();
				secondDate.setTime(cd.getCostDocument().getDocumentDate());
				secondDate.clear(Calendar.HOUR);
				secondDate.clear(Calendar.MINUTE);
				secondDate.clear(Calendar.SECOND);
				secondDate.clear(Calendar.MILLISECOND);
				if (!firstDate.equals(secondDate))
				{
					continue;
				}
			}

			if (this.getPartnerID() != null && !this.getPartnerID().isEmpty())
			{
				if (!cd.getUser().equals(Long.parseLong(this.getPartnerID())))
				{
					continue;
				}
			}

			if (this.getFilterPartner() != null
					&& !this.getFilterPartner().isEmpty())
			{
				if (this.getFilterPartner().equals("-1"))
				{
					boolean skip = true;
					if (this.getProject().getAsse() == 5)
					{
						List<Long> partnerIds = BeansFactory
								.ControllerUserAnagraph()
								.GetUsersIndicesByType(UserRoleTypes.AGU);
						if (partnerIds != null
								&& partnerIds.contains(cd.getUser().getId()))
						{
							skip = false;
						}

					}

					if (skip)
					{
						continue;
					}
				}
				if (!cd.getUser().getId()
						.equals(Long.parseLong(this.getFilterPartner())))
				{
					continue;
				}
			}



			if (this.getCostDefID() != null && !this.getCostDefID().isEmpty())
			{
				if (!cd.getProgressiveId().equals(
						Long.parseLong(this.getCostDefID())))
				{
					continue;
				}
			}
			
			if (this.getReportNumber() != null && !this.getReportNumber().isEmpty() && Integer.parseInt(this.getReportNumber())!=0)
			{
				if(!cd.getReportNumber().equals(new Integer(Integer.parseInt(this.getReportNumber())))){
					continue;
				}

			}
			
			if (Boolean.TRUE.equals(this.getAdditionalFinansing()))
			{
				if (!Boolean.TRUE.equals(cd.getAdditionalFinansing()))
				{
					continue;
				}
			}
				
			
			
			
			if(this.getCostDefReportNumber()!=null && !this.getCostDefReportNumber().isEmpty()){
				if(!cd.getReportNumber().equals(Integer.parseInt(this.getCostDefReportNumber()))){
					continue;
				}
			}
			if(this.getLocationId()!=null && !this.getLocationId().isEmpty()){
				if(!cd.getLocation().name().equals(this.getLocationId())){
					continue;
				}
			}

			tempList.add(cd);
		}
		this.setList(tempList);

		ReRenderScroll();
		FillLabels();
	}
	
	private void FillLabels(){
		double totalImportPartner=0d;
		double totalImportRev=0d;
		double totalImportFesr=0d;
		double totalImportCPN=0d;
		double totalImportAdditional=0d;
		long totalNumberOfCostDefinitions=0l;
		
		try
		{
			ControllerUserAnagraph userAnagr = BeansFactory
					.ControllerUserAnagraph()
					.GetByUser(this.getCurrentUser().getId());
			
			if (userAnagr != null)
			{
				List<CFPartnerAnagraphs> listParteners = BeansFactory
						.CFPartnerAnagraphs()
						.GetByCIL(this.getProjectId(), userAnagr.getId());
				
				List<Long> ids = new ArrayList<Long>();
				
				if (listParteners != null)
				{
					for (CFPartnerAnagraphs item : listParteners)
					{
						if (item.getUser() != null)
						{
							ids.add(item.getUser().getId());
						}
					}

					if (ids.size() > 0)
					{
						totalNumberOfCostDefinitions = BeansFactory
								.CostDefinitions()
								.GetCountByPartners(this.getProjectId(),
										ids.toArray(new Long[0]),
										CostStateTypes.RefusedCIL);
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
			addStackTrace(e);
		}
		
		for (CostDefinitions cost : this.getList())
		{

			if (cost.getCilIntermediateStepSave())
			{

				if (cost.getAmountReport() != null)
				{
					totalImportPartner += cost.getAmountReport();
				}

				if (cost.getCilCheck() != null)
				{
					totalImportRev += cost.getCilCheck();
				}
				if (!Boolean.TRUE.equals(cost.getAdditionalFinansing()))
				{

					if (cost.getCilCheck() != null)
					{
						totalImportFesr += cost.getCilCheck() * 0.85;
						totalImportCPN += cost.getCilCheck() * 0.15;
						

					}

				}else{
					totalImportAdditional += cost.getCilCheck();
				}

			}

		}
		
		this.setTotalImportPartner(totalImportPartner);
		this.setTotalImportRevisore(totalImportRev);
		this.setTotalValidateFesr(totalImportFesr);
		this.setTotalValidateCpn(totalImportCPN);
		this.setTotalValidateAddFin(totalImportAdditional);
		this.setTotalNumberOfCostDefinitions(totalNumberOfCostDefinitions);
	}
	
	private void FillCostItemsAsse3() throws PersistenceBeanException
	{

		setFilterCostItemAsse3(new ArrayList<SelectItem>());
		getFilterCostItemAsse3().add(SelectItemHelper.getFirst());
		if (this.getSelectedCostPhaseAsse3() != null
				&& !this.getSelectedCostPhaseAsse3().isEmpty())
		{
			PhaseAsse3Types phase = null;
			try
			{
				phase = PhaseAsse3Types.valueOf(this.getSelectedCostPhaseAsse3());
			}
			catch (Exception e)
			{
				log.error("Enum is not compatible", e);
				return;
			}
			List<CostAsse3> costs = BeansFactory.CostAsse3().GetByPhase(phase);

			for (CostAsse3 cost : costs)
			{
				getFilterCostItemAsse3().add(
						new SelectItem(cost.getValue(), cost.getValue()));
			}

		}

	}
	
	private void FillAsse3Phases()
	{
		setFilterCostPhaseAsse3(new ArrayList<SelectItem>());
		getFilterCostPhaseAsse3().add(SelectItemHelper.getFirst());
		for (PhaseAsse3Types type : PhaseAsse3Types.values())
		{
			getFilterCostPhaseAsse3().add(
					new SelectItem(type.name(), type.toString()));
		}

	}
	
	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(),
					ExportPlaces.CostDefinitions);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportValidationFlow")
					+ ".xls", is, data.length);
		}
		catch (Exception e)
		{
			log.error(e);
			addStackTrace(e);
		}
	}

	private void FillFilters()
	{
		this.filterValuesD = new ArrayList<SelectItem>();
		this.filterValuesPA = new ArrayList<SelectItem>();
		this.filterValuesCIL = new ArrayList<SelectItem>();
		try
		{
			this.filterValuesD.add(new SelectItem(all, Utils
					.getString("cilFilterAll")));
			this.filterValuesD.add(new SelectItem(validated, Utils
					.getString("cilFilterValidate")));
		  //this.filterValuesD.add(new SelectItem(String
		  //	.valueOf(CostStateTypes.DAECVerify.getState()),
		  // 		BeansFactory.CostStates()
		  //			.Load(CostStateTypes.DAECVerify.getState())
		  //			.getValue()));
			this.filterValuesD.add(new SelectItem(refused, Utils
					.getString("cilFilterRefuced")));
			/*
			 * this.filterValuesD.add(new SelectItem(String
			 * .valueOf(CostStateTypes.DAECApproval.getState()),
			 * BeansFactory.CostStates()
			 * .Load(CostStateTypes.DAECApproval.getState()) .getValue()));
			 */
			this.filterValuesPA.add(new SelectItem(String
					.valueOf(CostStateTypes.CILVerify.getState()), BeansFactory
					.CostStates().Load(CostStateTypes.CILVerify.getState())
					.getValue()));
			this.filterValuesPA.add(new SelectItem(String
					.valueOf(CostStateTypes.CILValidate.getState()),
					BeansFactory.CostStates()
							.Load(CostStateTypes.CILValidate.getState())
							.getValue()));

			this.filterValuesCIL.add(new SelectItem(all, Utils
					.getString("cilFilterAll")));
			this.filterValuesCIL.add(new SelectItem(validated, Utils
					.getString("cilFilterValidate")));
			this.filterValuesCIL.add(new SelectItem(toValidate, Utils
					.getString("cilFilterToValidate")));
			this.filterValuesCIL.add(new SelectItem(refused, Utils
					.getString("cilFilterRefuced")));
		}
		catch (HibernateException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			addStackTrace(e);
		}
	}

	public void search()
	{
		try
		{
			this.LoadEntities();
			this.Page_Load();
		}
		catch (Exception e)
		{
		}
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	public void clear()
	{
		try
		{
			this.setFilterValidationStatus(null);
			this.setDurNumber(null);
			this.setReportNumber(null);
			this.setDocumentDate(null);
			this.setPartnerID(null);
			this.setFilterPartner(null);
			this.setFilterCostItem(null);
			this.setCostDefID(null);
			this.setFilterPhase(null);
			this.LoadEntities();
			this.Page_Load();
		}
		catch (Exception e)
		{
		}
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	public void viewEntity()
	{
		this.getSession().put("costDefinitionEdit", this.getEntityEditId());
		this.getSession().put("costDefinitionView", this.getEntityEditId());
		this.getSession().put("costDefinitionViewBackToValidationFlow",
				this.getEntityEditId());
		this.getSession().put("costDefinitionViewBackToDUREditFlow", null);

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	@Override
	public void addEntity()
	{
	}

	@Override
	public void deleteEntity()
	{
	}

	@Override
	public void editEntity()
	{
		this.getSession().put("costDefinitionEdit", this.getEntityEditId());
		this.getSession().put("costDefinitionView", null);
		this.getSession().put("costDefinitionViewBackToValidationFlow",
				this.getEntityEditId());
		this.getSession().put("costDefinitionViewBackToDUREditFlow", null);

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	public void addToCache()
	{
		List<Long> ids = new ArrayList<Long>();
		for (CostDefinitions item : getList())
		{
			if (item.getSelected())
			{
				ids.add(item.getId());
			}
		}

		setSelectAll(false);
		this.getViewState().put("costDefinitions", ids);
	}

	public String movePrevStep()
	{
		try
		{
			if (SuperAdminHelper.moveCostDefinitionPrevStep(
					this.getEntitySendId(), this.getActionMotivation(),
					this.getCurrentUser(), this.getSessionBean().getProject(),
					"validationFlow"))
			{
				this.getViewState().put("costDefinitions", null);
				this.Page_Load();
				this.setActionMotivation("");
				this.LoadEntities();
			}
		}
		catch (Exception e)
		{
			log.error(e);
			addStackTrace(e);
		}
		return null;
	}

	/**
     *
     */
	public void sendToNextStep()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Pair<Long, String>> infoForActivityLog = new ArrayList<Pair<Long, String>>();
			DecimalFormat dec = new DecimalFormat("\u20AC ###,###,##0.00",
					DecimalFormatSymbols.getInstance(Locale.ITALIAN));
			
			if (this.getSessionBean().isCF() || this.getSessionBean().isPartner())
			{
				if(this.getNewDocumentUpFileCF() == null && this.getNewDocTitleCF() == null)
				{
					this.setAttachmentIsRequredError(Utils.getString("attachmentIsRequredError"));
					return;
				}
			}
			
			if (this.getSessionBean().isCF() || this.getSessionBean().isPartner())
			{
				try
				{
					Documents docForDeac = null;
					Documents docNotForDeac = null;
					
					for (CostDefinitions item : getList())
					{
						if (item.getSelected())
						{
							boolean forDeac = item
									.getCostState()
									.getId()
									.equals(BeansFactory
											.CostStates()
											.Load(CostStateTypes.DAECApproval
													.getState()).getId());
							
							Documents doc = null;
							
							if(forDeac)
							{
								if(docForDeac == null)
								{
									docForDeac = saveDocCF(forDeac);
								}
								
								doc = docForDeac;
							}
							else
							{
								if(docNotForDeac == null)
								{
									docNotForDeac = saveDocCF(forDeac);
								}
								
								doc = docNotForDeac;
							}

							if (doc != null)
							{
								CostDefinitionsToDocuments cdtd = new CostDefinitionsToDocuments(
										doc, item);
								BeansFactory.CostDefinitionsToDocuments()
										.SaveInTransaction(cdtd);
							}

						}
					}
					this.clearDocs();
				}
				catch (IOException e)
				{
					log.error(e);
					addStackTrace(e);
				}
			}
			
			for (CostDefinitions item : getList())
			{
				if (item.getSelected())
				{
					if (this.getSessionBean().isPartner()
							|| this.getSessionBean().isCF())
					{
						if (CostStateTypes.NotYetValidate.getState().equals(item
								.getCostState().getId()))
						{
							if(item.getCustomNoteWeb() != null && !item.getCustomNoteWeb().isEmpty())
							{
								item.setCustomNote(item.getCustomNoteWeb());
							}
							try
							{
								infoForActivityLog
										.add(new Pair<Long, String>(
												item.getProgressiveId(),
												String.valueOf(
														dec.format(Double
																.parseDouble(String
																		.valueOf(item
																				.getAmountReport()))))
														.replace(" ", "\u00a0")));
							}
							catch (Exception e)
							{
								log.error(e);
								addStackTrace(e);
							}
						}
						CFPartnerAnagraphs partner = BeansFactory
								.CFPartnerAnagraphs().GetByUser(
										this.getCurrentUser().getId());
						if (partner != null
								//&& partner
								//		.getCountry()
								//		.getCode()
								//		.equals(CountryTypes.Italy.getCountry())
								)
						{
							if (CostStateTypes.getByState(
									item.getCostState().getId()).equals(
									CostStateTypes.RefusedCIL))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.CILVerify
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
							else if (!CostStateTypes.getByState(
									item.getCostState().getId()).equals(
									CostStateTypes.CILValidate))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load((CostStateTypes
												.skipNext(CostStateTypes
														.getByState(item
																.getCostState()
																.getId()))
												.getState())));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
							else
							{
								if (this.getSessionBean().isPartner())
								{
									item.setCostState(BeansFactory
											.CostStates()
											.Load(CostStateTypes
													.getNext(
															CostStateTypes
																	.getByState(item
																			.getCostState()
																			.getId()))
													.getState()));
									BeansFactory.CostDefinitions()
											.SaveInTransaction(item);
								}
								else
								{
									item.setCostState(BeansFactory
											.CostStates()
											.Load(CostStateTypes
													.skipNext(
															CostStateTypes
																	.getByState(item
																			.getCostState()
																			.getId()))
													.getState()));
									BeansFactory.CostDefinitions()
											.SaveInTransaction(item);
								}
							}
						}
						
						//MALTA WORKFLOW WITH DAEK CONFIRMATION
						/*else
						{
							if (this.getSessionBean().isCF()
									&& partner != null
									&& partner
											.getCountry()
											.getCode()
											.equals(CountryTypes.France
													.getCountry()))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.skipNext(
												CostStateTypes
														.getByState(item
																.getCostState()
																.getId()))
												.getState()));
								
								 * if cost state change status from RefusedCIL
								 * to Dismiss it is necessary to set dismiss =
								 * trueP.S. fix for bug 0021705
								 
								if (item.getCostState()
										.getId()
										.equals(CostStateTypes.Dismiss
												.getState()))
								{
									item.setDismiss(true);
								}
								//
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
							else if (this.getSessionBean().isPartner()
									&& CostStateTypes.getByState(
											item.getCostState().getId())
											.equals(CostStateTypes.RefusedCIL))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.CILVerify
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
							// else if (this.getSessionBean().isPartner())
							// {
							// item.setCostState(BeansFactory.CostStates()
							// .Load(CostStateTypes.skipNext(
							// CostStateTypes
							// .getByState(item
							// .getCostState()
							// .getId()))
							// .getState()));
							// BeansFactory.CostDefinitions()
							// .SaveInTransaction(item);
							// }
							else if (this.getSessionBean().isPartner()
									&& item.getCostState()
											.getId()
											.equals(CostStateTypes.CILValidate
													.getState()))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.getNext(
												CostStateTypes
														.getByState(item
																.getCostState()
																.getId()))
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
							else
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.skipNext(
												CostStateTypes
														.getByState(item
																.getCostState()
																.getId()))
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
						} */
					}
					else if (this.getSessionBean().isAGU())
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.skipNext(
										CostStateTypes.getByState(item
												.getCostState().getId()))
										.getState()));
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}
					else if (this.getSessionBean().isDAEC())
					{
						CFPartnerAnagraphs partner = BeansFactory
								.CFPartnerAnagraphs().GetByUser(
										item.getUser().getId());
						CFPartnerAnagraphProject partnerToProject = BeansFactory
								.CFPartnerAnagraphProject().LoadByPartner(
										this.getProjectId(),
										String.valueOf(partner.getId()));
						if (partner != null
								&& partnerToProject
										.getType()
										.getId()
										.equals(CFAnagraphTypes.PartnerAnagraph
												.getCfType()))
						{
							if (item.getCostState()
									.getId()
									.equals(CostStateTypes.DAECApproval
											.getState()))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.CILValidate
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
						}
						else
						{
							if (item.getCostState()
									.getId()
									.equals(CostStateTypes.DAECApproval
											.getState()))
							{
								item.setCostState(BeansFactory.CostStates()
										.Load(CostStateTypes.CFValidate
												.getState()));
								BeansFactory.CostDefinitions()
										.SaveInTransaction(item);
							}
						}
					}
					else
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.getNext(
										CostStateTypes.getByState(item
												.getCostState().getId()))
										.getState()));
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}
				}
			}
			tr.commit();

			try
			{
				for (Pair<Long, String> pair : infoForActivityLog)
				{
					ActivityLog.getInstance().addExtendedActivity(
							Utils.getString("extendedActLogCdSendToVF"),
							pair.getSecond(), pair.getFirst());
				}
			}
			catch (Exception e)
			{
				log.error(e);
				addStackTrace(e);
			}

			this.getViewState().put("costDefinitions", null);
			this.Page_Load();
			this.LoadEntities();
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
	}

	private Documents saveDoc(boolean forDaec) throws HibernateException,
			PersistenceBeanException, IOException
	{
		Documents doc = null;
		if (this.getViewState().get("savedDocument") != null)
		{
			return (Documents) this.getViewState().get("savedDocument");
		}
		if (this.getViewState().get("newdocument") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"newdocument");

			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(
					newFileName)) == true)
			{
				doc = new Documents();
				doc.setTitle(FileHelper.getFileNameWOExtension(docinfo
						.getName()));
				doc.setName(docinfo.getName());
				doc.setUser(this.getCurrentUser());
				doc.setDocumentDate(docinfo.getDate());
				doc.setProject(this.getProject());
				doc.setProtocolNumber(docinfo.getProtNumber());
				doc.setSection(BeansFactory.Sections().Load(
						DocumentSections.ValidationFlow.getValue()));
				doc.setCategory(docinfo.getCategory());
				doc.setIsForDaec(forDaec);
				if (this.getSelectedPartner() != null
						&& !this.getSelectedPartner().isEmpty())
				{
					doc.setForPartner(BeansFactory.Users().Load(
							this.getSelectedPartner()));
				}
				doc.setIsPublic(docinfo.getIsPublic());
				doc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(doc);
			}
		}

		return doc;
	}
	
	private Documents saveDocCF(boolean forDaec) throws HibernateException,
			PersistenceBeanException, IOException
	{
		Documents doc = null;
		if (this.getViewState().get("savedDocumentCF") != null)
		{
			return (Documents) this.getViewState().get("savedDocumentCF");
		}
		if (this.getViewState().get("newdocumentCF") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"newdocumentCF");

			String newFileName = FileHelper.newFileName(docinfo.getName());
			if (FileHelper.copyFile(new File(docinfo.getFileName()), new File(
					newFileName)) == true)
			{
				doc = new Documents();
				doc.setTitle(FileHelper.getFileNameWOExtension(docinfo
						.getName()));
				doc.setName(docinfo.getName());
				doc.setUser(this.getCurrentUser());
				doc.setDocumentDate(docinfo.getDate());
				doc.setProject(this.getProject());
				doc.setProtocolNumber(docinfo.getProtNumber());
				doc.setSection(BeansFactory.Sections().Load(
						DocumentSections.ValidationFlow.getValue()));
				doc.setCategory(docinfo.getCategory());
				doc.setIsForDaec(forDaec);
				if (this.getSelectedPartner() != null
						&& !this.getSelectedPartner().isEmpty())
				{
					doc.setForPartner(BeansFactory.Users().Load(
							this.getSelectedPartner()));
				}
				doc.setIsPublic(docinfo.getIsPublic());
				doc.setFileName(newFileName);
				BeansFactory.Documents().SaveInTransaction(doc);
			}
		}

		return doc;
	}

	private void clearDocs()
	{
		this.getViewState().put("newdocumentCF", null);
		this.getViewState().put("newdocument", null);
		this.setNewDocTitle(null);
		this.setNewDocument(null);
		this.setNewDocumentUpFile(null);
		this.setNewDocTitleCF(null);
		this.setNewDocumentCF(null);
		this.setNewDocumentUpFileCF(null);
	}

	private boolean checkBeforeApprove() throws PersistenceBeanException
	{
		for (CostDefinitions item : getList())
		{
			if (item.getSelected())
			{
				if (item.getCilDate() == null)
				{
					FacesMessage msg = new FacesMessage("");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage("", msg);
					return false;
				}
			}
		}

		for (CostDefinitions item : getList())
		{
			if (item.getSelected())
			{
				if (this.getSessionBean().isCIL())
				{
					if (item.getCilCheck() == null
							|| item.getCilCheck() > item.getAmountReport())
					{
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilCheckless"));

						return false;
					}
					if(this.getNewDocumentUpFile() == null && this.getNewDocTitle() == null)
					{
						this.setAttachmentIsRequredError(Utils.getString("attachmentIsRequredError"));
						return false;
					}
					if(item.getCilCheckAdditionalFinansingAmount() == null || item.getCilCheckAdditionalFinansingAmount()>item.getAdditionalFinansingAmount()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessAdditionalFinansing"));

						return false;
					}
					if(item.getCilCheckPrivateAmount() == null || item.getCilCheckPrivateAmount()>item.getPrivateAmountReport()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPrivate"));

						return false;
					}
					
					if(item.getCilCheckPublicAmount() == null || item.getCilCheckPublicAmount()>item.getPublicAmountReport()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessPublic"));

						return false;
					}
					
					if(item.getCilCheckStateAidAmount() == null || item.getCilCheckStateAidAmount()>item.getAmountToCoverAdvanceStateAid()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessStateAid"));

						return false;
					}
					
					if(item.getCilCheckOutsideEligibleAreas() == null || item.getCilCheckOutsideEligibleAreas()>item.getExpenditureOutsideEligibleAreasAmount()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessOutsideEligibleAreas"));

						return false;
					}
					

					if(item.getCilCheckInkindContributions() == null || item.getCilCheckInkindContributions()>item.getInkindContributionsAmount()){
						FacesMessage msg = new FacesMessage("");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						FacesContext.getCurrentInstance().addMessage("", msg);

						this.setError(Utils.getString("validator.cilChecklessInkindContributions"));

						return false;
					}
				}
			}
			
			
		}

		return true;
	}

	/**
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 */
	public void approve() throws NumberFormatException,
			PersistenceBeanException
	{
		if (!checkBeforeApprove())
		{
			try
			{
				Page_Load();
				LoadEntities();
			}
			catch (HibernateException e)
			{
				log.error(e);
				addStackTrace(e);
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				addStackTrace(e);
			}

			return;
		}

		Transaction tr = PersistenceSessionManager.getBean().getSession()
				.beginTransaction();
		try
		{
			for (CostDefinitions item : getList())
			{
				item.setCilIntermediateStepSave(false);
				// item.setCustomNote(null);
				item.setTempDocument(null);

				if (item.getSelected())
				{
					if (this.getSessionBean().isCIL())
					{
						CFPartnerAnagraphs partner = BeansFactory
								.CFPartnerAnagraphs().GetByUser(
										item.getUser().getId());
						//if (partner == null
							//	|| (partner.getCountry() != null && partner
							//			.getCountry()
							//			.getCode()
							//			.equals(CountryTypes.Italy.getCountry()))
							//	|| item.getCreatedByAGU())
						//{
							if (partner != null && !item.getCreatedByAGU())
							{
								CFPartnerAnagraphProject partnerToProject = BeansFactory
										.CFPartnerAnagraphProject()
										.LoadByPartner(this.getProjectId(),
												String.valueOf(partner.getId()));
								if (partnerToProject != null
										&& partnerToProject
												.getType()
												.getId()
												.equals(CFAnagraphTypes.PartnerAnagraph
														.getCfType()))
								{
									if (item.getCilCheck()!=null && item.getCostState().getId()==3) {
										item.setCostState(BeansFactory.CostStates()
												.Load(CostStateTypes.CILValidate
														.getState()));
	
										BeansFactory.CostDefinitions()
												.SaveInTransaction(item);
									}
									else {
										FacesMessage msg = new FacesMessage("Attenzione: Inserire tutti gli importi richiesti!");
										msg.setSeverity(FacesMessage.SEVERITY_ERROR);
										FacesContext.getCurrentInstance().addMessage("", msg);
										
										System.out.println("Errore Approvazione da parte del REV nel progetto ID: " + item.getProject().getId()
												+ " cost_state_id: " + item.getCostState().getId() + " user_id: " + item.getUser().getId()
												+ " id: " + item.getId() 
												+ " user_inserted_name: " + item.getUserInsertedName() + " time: " + new Date());
										return; 
									}
								}
								else
								{
									if (item.getCilCheck()!=null && item.getCostState().getId()==3) {
										item.setCostState(BeansFactory.CostStates()
												.Load(CostStateTypes.CFValidate
														.getState()));
										BeansFactory.CostDefinitions()
												.SaveInTransaction(item);
									}
									else {
										FacesMessage msg = new FacesMessage("Attenzione: Inserire tutti gli importi richiesti!");
										msg.setSeverity(FacesMessage.SEVERITY_ERROR);
										FacesContext.getCurrentInstance().addMessage("", msg); 
										
										System.out.println("Errore Approvazione da parte del REV nel progetto ID: " + item.getProject().getId()
												+ " cost_state_id: " + item.getCostState().getId() + " user_id: " + item.getUser().getId()
												+ " id: " + item.getId() 
												+ " user_inserted_name: " + item.getUserInsertedName() + " time: " + new Date());
										return; 
									}
								}
							}
							else
							{
								if (item.getCilCheck()!=null && item.getCostState().getId()==3) {
									item.setCostState(BeansFactory.CostStates()
											.Load(CostStateTypes.CFValidate
													.getState()));
									BeansFactory.CostDefinitions()
											.SaveInTransaction(item);
								}
								else {
									FacesMessage msg = new FacesMessage("Attenzione: Inserire tutti gli importi richiesti!");
									msg.setSeverity(FacesMessage.SEVERITY_ERROR);
									FacesContext.getCurrentInstance().addMessage("", msg);
									
									System.out.println("Errore Approvazione da parte del REV nel progetto ID: " + item.getProject().getId()
											+ " cost_state_id: " + item.getCostState().getId() + " user_id: " + item.getUser().getId()
											+ " id: " + item.getId() 
											+ " user_inserted_name: " + item.getUserInsertedName() + " time: " + new Date());
									return; 
								}
							}
						//} 
						/*else
						{
							item.setCostState(BeansFactory.CostStates().Load(
									CostStateTypes.getNext(
											CostStateTypes.getByState(item
													.getCostState().getId()))
											.getState()));
							BeansFactory.CostDefinitions().SaveInTransaction(
									item);
						}*/

					}
					else
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.getNext(
										CostStateTypes.getByState(item
												.getCostState().getId()))
										.getState()));
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}
				}
			}

			if (this.getSessionBean().isCIL())
			{
				try
				{
					Documents docForDeac = null;
					Documents docNotForDeac = null;
					
					for (CostDefinitions item : getList())
					{
						if (item.getSelected())
						{
							boolean forDeac = item
									.getCostState()
									.getId()
									.equals(BeansFactory
											.CostStates()
											.Load(CostStateTypes.DAECApproval
													.getState()).getId());
							
							Documents doc = null;
							
							if(forDeac)
							{
								if(docForDeac == null)
								{
									docForDeac = saveDoc(forDeac);
								}
								
								doc = docForDeac;
							}
							else
							{
								if(docNotForDeac == null)
								{
									docNotForDeac = saveDoc(forDeac);
								}
								
								doc = docNotForDeac;
							}

							if (doc != null)
							{
								CostDefinitionsToDocuments cdtd = new CostDefinitionsToDocuments(
										doc, item);
								BeansFactory.CostDefinitionsToDocuments()
										.SaveInTransaction(cdtd);
							}

						}
					}
					this.clearDocs();
				}
				catch (IOException e)
				{
					log.error(e);
					addStackTrace(e);
				}

				for (CostDefinitions item : getList())
				{
					if (item.getSelected())
					{
						if (item.getCustomNoteWeb() != null
								&& !item.getCustomNoteWeb().isEmpty())
						{
							item.setCustomNote(item.getCustomNoteWeb());
							CostDefinitionsToNotes noteItem = new CostDefinitionsToNotes(
									item.getCustomNoteWeb().length() > 255 ? item
											.getCustomNoteWeb().substring(0,
													254)
											: item.getCustomNoteWeb(), item);
							BeansFactory.CostDefinitionsToNotes()
									.SaveInTransaction(noteItem);

							// item.setCustomNote(null);
							BeansFactory.CostDefinitions().SaveInTransaction(
									item);
						}
					}
				}

			}
			

			if (tr != null && !tr.wasRolledBack())
			{
				tr.commit();

				try
				{
					for (CostDefinitions item : getList())
					{
						if (item.getSelected())
						{
							DecimalFormat dec = new DecimalFormat(
									"\u20AC ###,###,##0.00",
									DecimalFormatSymbols
											.getInstance(Locale.ITALIAN));
							if (this.getSessionBean().isCIL())
							{
								if (item.getCilCheck() != null)
								{
									ActivityLog
											.getInstance()
											.addExtendedActivity(
													Utils.getString("extendedActLogCdApprove"),
													String.valueOf(
															dec.format(Double
																	.parseDouble(String
																			.valueOf(item
																					.getCilCheck()))))
															.replace(" ",
																	"\u00a0"),
													item.getProgressiveId());
								}
								else
								{
									ActivityLog
											.getInstance()
											.addExtendedActivity(
													Utils.getString("extendedActLogCdApprove"),
													"", item.getProgressiveId());
								}
							}
							else if (this.getSessionBean().isDAEC())
							{
								ActivityLog
										.getInstance()
										.addExtendedActivity(
												Utils.getString("extendedActLogCdApprove"),
												"", item.getProgressiveId());
							}
						}
					}
				}
				catch (Exception e)
				{
					log.error(e);
					addStackTrace(e);
				}

				this.getViewState().put("costDefinitions", null);
				setSelectAll(false);
			}

			this.Page_Load();
			this.LoadEntities();
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
	}

	/**
     *
     */
	public void deny()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			for (CostDefinitions item : getList())
			{
				if (item.getSelected())
				{
					if (this.getSessionBean().isCIL()
					/* && item.getCreatedByPartner() */)
					{
						item.setCilCheck(null);
						item.setCilCheckPublicAmount(null);
						item.setCilCheckPrivateAmount(null);
						item.setCilCheckAdditionalFinansingAmount(null);
						item.setCilCheckStateAidAmount(null);
						item.setCilCheckOutsideEligibleAreas(null);
						item.setCilCheckInkindContributions(null);
						item.setCilDate(null);
						item.setWasRefusedByCil(true);
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.RefusedCIL.getState()));
					}
					// else if (this.getSessionBean().isCIL())
					// {
					// item.setCostState(BeansFactory.CostStates().Load(
					// CostStateTypes.RefusedCIL.getState()));
					// }
					else
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.RefusedDAEC.getState()));
					}

					BeansFactory.CostDefinitions().SaveInTransaction(item);
					if (item.getCustomNoteWeb() != null
							&& !item.getCustomNoteWeb().isEmpty())
					{
						item.setCustomNote(item.getCustomNoteWeb());
						CostDefinitionsToNotes noteItem = new CostDefinitionsToNotes(
								item.getCustomNote().length() > 255 ? item
										.getCustomNote().substring(0, 254)
										: item.getCustomNote(), item);
						BeansFactory.CostDefinitionsToNotes()
								.SaveInTransaction(noteItem);
						if (this.getSessionBean().isCIL()
								&& item.getCreatedByPartner())
						{
							item.setCustomNote(null);
						}
						// item.setCustomNote(null);
						BeansFactory.CostDefinitions().SaveInTransaction(item);
					}

					try
					{
						ActivityLog.getInstance().addExtendedActivity(
								Utils.getString("extendedActLogCdRefuse"),
								null, item.getProgressiveId());
					}
					catch (Exception e)
					{
						log.error(e);
						addStackTrace(e);
					}
				}
			}

			if (this.getSessionBean().isCIL())
			{

				Documents doc = null;
				try
				{
					doc = saveDoc(false);
				}
				catch (IOException e)
				{
					log.error(e);
					addStackTrace(e);
				}

				for (CostDefinitions item : getList())
				{
					CostDefinitionsToDocuments cdtd = new CostDefinitionsToDocuments(
							doc, item);

					BeansFactory.CostDefinitionsToDocuments()
							.SaveInTransaction(cdtd);
				}
			}
			else if (this.getSessionBean().isCIL())
			{

				Documents doc = null;
				try
				{
					doc = saveDocCF(false);
				}
				catch (IOException e)
				{
					log.error(e);
					addStackTrace(e);
				}

				for (CostDefinitions item : getList())
				{
					CostDefinitionsToDocuments cdtd = new CostDefinitionsToDocuments(
							doc, item);

					BeansFactory.CostDefinitionsToDocuments()
							.SaveInTransaction(cdtd);
				}
			}

			tr.commit();
			this.clearDocs();
			this.getViewState().put("costDefinitions", null);
			this.Page_Load();
			this.LoadEntities();
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
	}

	/**
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 */
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException
	{
		for (CostDefinitions item : getList())
		{

			BeansFactory.CostDefinitions().SaveInTransaction(item);
		}
	}

	/**
	 * Call this method from the page on "Save" button.
	 */
	public void Page_Save()
	{
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
		}
		catch (HibernateException e1)
		{
			log.error(e1);
			addStackTrace(e1);
		}
		catch (PersistenceBeanException e1)
		{
			log.error(e1);
			addStackTrace(e1);
		}

		try
		{
			this.SaveEntity();
		}
		catch (HibernateException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		catch (NumberFormatException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
			addStackTrace(e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
			}
		}

	}

	public void doSelectAll()
	{
		List<Long> ids = new ArrayList<Long>();
		if (getSelectAll())
		{
			for (CostDefinitions item : getList())
			{
				if (item.getDismiss())
				{
					continue;
				}

				if (this.getSessionBean().isCIL()
						&& !item.getCostState().getId()
								.equals(CostStateTypes.CILVerify.getState())
						&& !this.getSessionBean().isAGU())
				{
					continue;
				}

				if (this.getSessionBean().isDAEC()
						&& getFilterValueD().equals(all)
						&& !item.getCostState().getId()
								.equals(CostStateTypes.DAECApproval.getState()))
				{
					continue;
				}
				if (this.getSessionBean().isSuperAdmin()
						&& Boolean.FALSE.equals(item.getLocked()))
				{
					continue;
				}

				ids.add(item.getId());
			}
		}

		this.getViewState().put("costDefinitions", ids);

		try
		{
			Page_Load();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			addStackTrace(e);
		}
	}

	/**
	 * Sets selectAll
	 * 
	 * @param selectAll
	 *            the selectAll to set
	 */
	public void setSelectAll(boolean selectAll)
	{
		this.selectAll = selectAll;
	}

	/**
	 * Gets selectAll
	 * 
	 * @return selectAll the selectAll
	 */
	public boolean getSelectAll()
	{
		return selectAll;
	}

	/**
	 * Sets rowId
	 * 
	 * @param rowId
	 *            the rowId to set
	 */
	public void setRowId(Long rowId)
	{
		this.rowId = rowId;
	}

	/**
	 * Gets rowId
	 * 
	 * @return rowId the rowId
	 */
	public Long getRowId()
	{
		return rowId;
	}

	/**
	 * Sets displayDeny
	 * 
	 * @param displayDeny
	 *            the displayDeny to set
	 */
	public void setDisplayDeny(boolean displayDeny)
	{
		this.getViewState().put("displayDeny", displayDeny);
	}

	/**
	 * Gets displayDeny
	 * 
	 * @return displayDeny the displayDeny
	 */
	public Boolean getDisplayDeny()
	{
		return (Boolean) this.getViewState().get("displayDeny");
	}

	/**
	 * Sets displayApprove
	 * 
	 * @param displayApprove
	 *            the displayApprove to set
	 */
	public void setDisplayApprove(boolean displayApprove)
	{
		this.getViewState().put("displayApprove", displayApprove);
	}

	/**
	 * Gets displayApprove
	 * 
	 * @return displayApprove the displayApprove
	 */
	public Boolean getDisplayApprove()
	{
		return (Boolean) this.getViewState().get("displayApprove");
	}

	/**
	 * Sets displayNext
	 * 
	 * @param displayNext
	 *            the displayNext to set
	 */
	public void setDisplayNext(boolean displayNext)
	{
		this.getViewState().put("displayNext", displayNext);
	}

	/**
	 * Gets displayNext
	 * 
	 * @return displayNext the displayNext
	 */
	public Boolean getDisplayNext()
	{
		return (Boolean) this.getViewState().get("displayNext");
	}

	/**
	 * Sets filterValuePA
	 * 
	 * @param filterValuePA
	 *            the filterValuePA to set
	 */
	public void setFilterValuePA(String filterValuePA)
	{
		boolean changed = false;
		this.filterValuePA = filterValuePA;
		if (this.getViewState().get("filterPA") != null
				&& !String.valueOf(this.getViewState().get("filterPA")).equals(
						filterValuePA))
		{
			changed = true;
		}

		this.getViewState().put("filterPA", filterValuePA);

		try
		{
			if (changed)
			{
				this.LoadEntities();
			}
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
			addStackTrace(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			addStackTrace(e);
		}
	}

	/**
	 * Gets filterValuePA
	 * 
	 * @return filterValuePA the filterValuePA
	 */
	public String getFilterValuePA()
	{
		if (this.getViewState().get("filterPA") != null)
		{
			filterValuePA = (String) this.getViewState().get("filterPA");
		}
		return filterValuePA;
	}

	/**
	 * Sets filterValuesPA
	 * 
	 * @param filterValuesPA
	 *            the filterValuesPA to set
	 */
	public void setFilterValuesPA(List<SelectItem> filterValuesPA)
	{
		this.filterValuesPA = filterValuesPA;
	}

	/**
	 * Gets filterValuesPA
	 * 
	 * @return filterValuesPA the filterValuesPA
	 */
	public List<SelectItem> getFilterValuesPA()
	{
		return filterValuesPA;
	}

	/**
	 * Sets filterValueD
	 * 
	 * @param filterValueD
	 *            the filterValueD to set
	 */
	public void setFilterValueD(String filterValueD)
	{
		boolean changed = false;
		this.filterValueD = filterValueD;
		if (this.getViewState().get("filterD") != null
				&& !String.valueOf(this.getViewState().get("filterD")).equals(
						filterValueD))
		{
			changed = true;
		}

		this.getViewState().put("filterD", filterValueD);

		try
		{
			if (this.getFilterValuePA() != null && changed)
			{
				this.LoadEntities();
			}
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
	}

	/**
	 * Gets filterValueD
	 * 
	 * @return filterValueD the filterValueD
	 */
	public String getFilterValueD()
	{
		if (this.getViewState().get("filterD") != null)
		{
			filterValueD = (String) this.getViewState().get("filterD");
		}
		return filterValueD;
	}

	/**
	 * Sets filterValuesD
	 * 
	 * @param filterValuesD
	 *            the filterValuesD to set
	 */
	public void setFilterValuesD(List<SelectItem> filterValuesD)
	{
		this.filterValuesD = filterValuesD;
	}

	/**
	 * Gets filterValuesD
	 * 
	 * @return filterValuesD the filterValuesD
	 */
	public List<SelectItem> getFilterValuesD()
	{
		return filterValuesD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		if (!this.getSessionBean().getIsActualSate())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
		//this.setAsse3Mode(Boolean.valueOf(this.getProjectAsse()==3));
		this.setAsse3Mode(Boolean.FALSE);
		this.FillFilters();
		if (this.filterValueCIL == null)
		{
			this.filterValueCIL = "all";
		}

		if (this.getFilterValueD() == null
				|| String.valueOf(this.getFilterValueD()).isEmpty())
		{
			this.setFilterValueD("all");
		}

		if (this.getFilterValuePA() == null)
		{
			this.setFilterValuePA(String.valueOf(CostStateTypes.CILVerify
					.getState()));
		}

		this.LoadEntities();
	}

	private void LoadEntities() throws PersistenceBeanException
	{
		List<CostDefinitions> list = new ArrayList<CostDefinitions>();
		List<Long> types = new ArrayList<Long>();
		if (this.getSessionBean().isCF())
		{
			types.add(CostStateTypes.NotYetValidate.getState());
			types.add(CostStateTypes.CILValidate.getState());
			types.add(CostStateTypes.RefusedCIL.getState());
			setDisplayApprove(false);
			//setDisplayDeny(true);
			setDisplayNext(true);
			list = BeansFactory.CostDefinitions().GetByPartnerAndBudgetOwner(
					this.getProjectId(), this.getCurrentUser().getId(),
					types.toArray(new Long[0]));
		}
		else if (this.getSessionBean().isPartner())
		{
			types.add(CostStateTypes.NotYetValidate.getState());
			types.add(CostStateTypes.CILValidate.getState());
			types.add(CostStateTypes.RefusedCIL.getState());
			setDisplayApprove(false);
			setDisplayDeny(false);
			setDisplayNext(true);

			list = BeansFactory.CostDefinitions().GetByPartnerAndBudgetOwner(
					this.getProjectId(), this.getCurrentUser().getId(),
					types.toArray(new Long[0]));
		}

		else if (this.getSessionBean().isDAEC())
		{
			if (getFilterValueD().equals(
					String.valueOf(CostStateTypes.DAECApproval.getState())))
			{
				types.add(CostStateTypes.DAECApproval.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(true);
			}
			else if (getFilterValueD().equals(
					String.valueOf(CostStateTypes.DAECVerify.getState())))
			{
				types.add(CostStateTypes.DAECVerify.getState());
				setDisplayApprove(true);
				setDisplayDeny(true);
				setDisplayNext(false);
			}
			else if (getFilterValueD().equals(all))
			{
				types.add(CostStateTypes.DAECApproval.getState());
				types.add(CostStateTypes.DAECVerify.getState());
				types.add(CostStateTypes.RefusedDAEC.getState());
				types.add(CostStateTypes.CILVerify.getState());
				types.add(CostStateTypes.CILValidate.getState());
				types.add(CostStateTypes.CFValidation.getState());
				types.add(CostStateTypes.CFValidate.getState());
				types.add(CostStateTypes.FullValidate.getState());
				types.add(CostStateTypes.RefusedCIL.getState());

				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(true);
			}
			else if (getFilterValueD().equals(validated))
			{
				types.add(CostStateTypes.DAECApproval.getState());
				types.add(CostStateTypes.CILVerify.getState());
				types.add(CostStateTypes.CILValidate.getState());
				types.add(CostStateTypes.CFValidation.getState());
				types.add(CostStateTypes.CFValidate.getState());
				types.add(CostStateTypes.FullValidate.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(false);
			}
			else if (getFilterValueD().equals(refused))
			{
				types.add(CostStateTypes.RefusedDAEC.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(false);
			}

			ControllerUserAnagraph userAnagr = BeansFactory
					.ControllerUserAnagraph().GetByUser(
							this.getCurrentUser().getId());
			if (userAnagr != null)
			{
				List<CFPartnerAnagraphs> listParteners = BeansFactory
						.CFPartnerAnagraphs().GetByDAEC(this.getProjectId(),
								userAnagr.getId());
				List<Long> ids = new ArrayList<Long>();
				if (listParteners != null)
				{
					for (CFPartnerAnagraphs item : listParteners)
					{
						ids.add(item.getUser().getId());
					}

					if (ids.size() > 0)
					{
						list = BeansFactory.CostDefinitions().GetByPartners(
								this.getProjectId(), ids.toArray(new Long[0]),
								types.toArray(new Long[0]));
					}
					if (this.getProjectAsse() == 5)
					{
						list.addAll(getAllCostDefinitionsAgus(types));
					}
				}
				else
				{
					if (this.getProjectAsse() == 5)
					{
						list.addAll(getAllCostDefinitionsAgus(types));
					}
				}
			}

		}
		else if (this.getSessionBean().isCIL())
		{
			if (this.filterValueCIL.equals(all))
			{
				types.add(CostStateTypes.CILVerify.getState());
				types.add(CostStateTypes.RefusedCIL.getState());
				types.add(CostStateTypes.CILValidate.getState());
				types.add(CostStateTypes.CFValidation.getState());
				types.add(CostStateTypes.CFValidate.getState());
				types.add(CostStateTypes.FullValidate.getState());
				types.add(CostStateTypes.DAECApproval.getState());
				setDisplayApprove(true);
				setDisplayDeny(true);
				setDisplayNext(false);
			}
			else if (this.filterValueCIL.equals(validated))
			{
				types.add(CostStateTypes.CFValidation.getState());
				types.add(CostStateTypes.CFValidate.getState());
				types.add(CostStateTypes.CILValidate.getState());
				types.add(CostStateTypes.FullValidate.getState());
				types.add(CostStateTypes.DAECApproval.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(false);
			}
			else if (this.filterValueCIL.equals(toValidate))
			{
				types.add(CostStateTypes.CILVerify.getState());
				setDisplayApprove(true);
				setDisplayDeny(true);
				setDisplayNext(false);
			}
			else if (this.filterValueCIL.equals(refused))
			{
				types.add(CostStateTypes.RefusedCIL.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(false);
			}

			ControllerUserAnagraph userAnagr = BeansFactory
					.ControllerUserAnagraph().GetByUser(
							this.getCurrentUser().getId());
			if (userAnagr != null)
			{
				List<CFPartnerAnagraphs> listParteners = BeansFactory
						.CFPartnerAnagraphs().GetByCIL(this.getProjectId(),
								userAnagr.getId());
				List<Long> ids = new ArrayList<Long>();
				if (listParteners != null)
				{
					for (CFPartnerAnagraphs item : listParteners)
					{
						if (item.getUser() != null)
						{
							ids.add(item.getUser().getId());
						}
					}

					if (ids.size() > 0)
					{
						list.addAll(BeansFactory.CostDefinitions()
								.GetByPartners(this.getProjectId(),
										ids.toArray(new Long[0]),
										types.toArray(new Long[0])));
					}

					if (this.getProjectAsse() == 5)
					{
						list.addAll(getAllCostDefinitionsAgus(types));
					}
				}
				else
				{
					if (this.getProjectAsse() == 5)
					{
						list.addAll(getAllCostDefinitionsAgus(types));
					}
				}
			}

			if (this.getSessionBean().isAGU())
			{
				types.add(CostStateTypes.NotYetValidate.getState());
				types.add(CostStateTypes.CILValidate.getState());
				setDisplayApprove(false);
				setDisplayDeny(false);
				setDisplayNext(true);

				list.addAll(getAllCostDefinitionsAgus(types));
			}
		}
		else if (this.getSessionBean().isAGU())
		{
			types.add(CostStateTypes.NotYetValidate.getState());
			types.add(CostStateTypes.CILValidate.getState());
			setDisplayApprove(false);
			setDisplayDeny(false);
			setDisplayNext(true);

			list = getAllCostDefinitionsAgus(types);
		}
		else if (getSessionBean().isSuperAdmin())
		{
			types.add(CostStateTypes.DAECVerify.getState());
			types.add(CostStateTypes.CILVerify.getState());
			types.add(CostStateTypes.CILValidate.getState());
			types.add(CostStateTypes.DAECApproval.getState());
			types.add(CostStateTypes.CFValidation.getState());
			types.add(CostStateTypes.CFValidate.getState());
			list.addAll(BeansFactory.CostDefinitions().GetByProject(
					this.getProjectId(), types.toArray(new Long[0])));
		}
		else if (getSessionBean().isSTC())
		{
			list.addAll(BeansFactory.CostDefinitions().GetByProject(
					this.getProjectId()));
		}
		Map<Long, CostDefinitionsWrapper> map = new HashMap<Long, CostDefinitionsWrapper>();
		this.getViewState().put("oldValues", map);
		for (CostDefinitions costDefinition : list)
		{
			if(!costDefinition.getCilIntermediateStepSave() && costDefinition.getCostState().getId().equals(CostStateTypes.CILVerify.getState())){
				costDefinition.setCilDate(new Date());
			}
			map.put(costDefinition.getId(), new CostDefinitionsWrapper(
					costDefinition));
		}
		setList(list);
	}

	/**
	 * @param types
	 * @return
	 * @throws PersistenceBeanException
	 */
	private List<CostDefinitions> getAllCostDefinitionsAgus(List<Long> types)
			throws PersistenceBeanException
	{
		return BeansFactory.CostDefinitions().GetAgus(this.getProjectId(),
				types.toArray(new Long[0]));
	}

	/**
	 * Sets error
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(String error)
	{
		this.error = error;
	}

	/**
	 * Gets error
	 * 
	 * @return error the error
	 */
	public String getError()
	{
		return error;
	}

	/**
	 * Sets newDocumentUpFile
	 * 
	 * @param newDocumentUpFile
	 *            the newDocumentUpFile to set
	 */
	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile)
	{
		this.newDocumentUpFile = newDocumentUpFile;
	}

	/**
	 * Gets newDocumentUpFile
	 * 
	 * @return newDocumentUpFile the newDocumentUpFile
	 */
	public UploadedFile getNewDocumentUpFile()
	{
		return newDocumentUpFile;
	}

	/**
	 * Sets newDocument
	 * 
	 * @param newDocument
	 *            the newDocument to set
	 */
	public void setNewDocument(Documents newDocument)
	{
		this.newDocument = newDocument;
	}

	/**
	 * Gets newDocument
	 * 
	 * @return newDocument the newDocument
	 */
	public Documents getNewDocument()
	{
		return newDocument;
	}

	/**
	 * Sets newDocTitle
	 * 
	 * @param newDocTitle
	 *            the newDocTitle to set
	 */
	public void setNewDocTitle(String newDocTitle)
	{
		this.newDocTitle = newDocTitle;
	}

	/**
	 * Gets newDocTitle
	 * 
	 * @return newDocTitle the newDocTitle
	 */
	public String getNewDocTitle()
	{
		return newDocTitle;
	}

	public void setList(List<CostDefinitions> list)
	{
		this.getViewState().put("CostDefinitions", list);
	}

	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getList()
	{
		return (List<CostDefinitions>) this.getViewState().get(
				"CostDefinitions");
	}

	/**
	 * Sets filterValuesCIL
	 * 
	 * @param filterValuesCIL
	 *            the filterValuesCIL to set
	 */
	public void setFilterValuesCIL(List<SelectItem> filterValuesCIL)
	{
		this.filterValuesCIL = filterValuesCIL;
	}

	/**
	 * Gets filterValuesCIL
	 * 
	 * @return filterValuesCIL the filterValuesCIL
	 */
	public List<SelectItem> getFilterValuesCIL()
	{
		return filterValuesCIL;
	}

	/**
	 * Sets filterValueCIL
	 * 
	 * @param filterValueCIL
	 *            the filterValueCIL to set
	 */
	public void setFilterValueCIL(String filterValueCIL)
	{
		boolean changed = false;
		this.filterValueCIL = filterValueCIL;
		if (this.getViewState().get("filterCIL") != null
				&& !String.valueOf(this.getViewState().get("filterCIL"))
						.equals(filterValueCIL))
		{
			changed = true;
		}
		this.setSelectAll(false);

		this.getViewState().put("filterCIL", filterValueCIL);

		try
		{
			if (filterValueCIL != null && changed)
			{
				this.LoadEntities();
			}
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
	}

	/**
	 * Gets filterValueCIL
	 * 
	 * @return filterValueCIL the filterValueCIL
	 */
	public String getFilterValueCIL()
	{
		return filterValueCIL;
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners)
	{
		this.listPartners = listPartners;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners()
	{
		return listPartners;
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner)
	{
		this.getViewState().put("selectedPartner", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner()
	{
		return (String) this.getViewState().get("selectedPartner");
	}
	
	public void setSelectedPartnerCF(String selectedPartnerCF)
	{
		this.getViewState().put("selectedPartnerCF", selectedPartnerCF);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartnerCF()
	{
		return (String) this.getViewState().get("selectedPartnerCF");
	}

	/**
	 * Sets defaultError
	 * 
	 * @param defaultError
	 *            the defaultError to set
	 */
	public void setDefaultError(String defaultError)
	{
		this.getViewState().put("FlowValidationViewBeanDefError", defaultError);
	}

	/**
	 * Gets defaultError
	 * 
	 * @return defaultError the defaultError
	 */
	public String getDefaultError()
	{
		if (this.getViewState().containsKey("FlowValidationViewBeanDefError"))
		{
			return (String) this.getViewState().get(
					"FlowValidationViewBeanDefError");
		}
		else
		{
			return "";
		}
	}

	public void setFieldError(String defaultError)
	{
		this.getViewState().put("FlowValidationViewBeanFieldError",
				defaultError);
	}

	/**
	 * Gets defaultError
	 * 
	 * @return defaultError the defaultError
	 */
	public String getFieldError()
	{
		if (this.getViewState().containsKey("FlowValidationViewBeanFieldError"))
		{
			return (String) this.getViewState().get(
					"FlowValidationViewBeanFieldError");
		}
		else
		{
			return "";
		}
	}

	public List<SelectItem> getFilterValidationStatusValues()
	{
		return filterValidationStatusValues;
	}

	public void setFilterValidationStatusValues(
			List<SelectItem> filterValidationStatusValues)
	{
		this.filterValidationStatusValues = filterValidationStatusValues;
	}

	public List<SelectItem> getFilterCostItemValues()
	{
		return filterCostItemValues;
	}

	public void setFilterCostItemValues(List<SelectItem> filterCostItemValues)
	{
		this.filterCostItemValues = filterCostItemValues;
	}

	public List<SelectItem> getFilterPartnerValues()
	{
		return filterPartnerValues;
	}

	public void setFilterPartnerValues(List<SelectItem> filterPartnerValues)
	{
		this.filterPartnerValues = filterPartnerValues;
	}

	public void setFilterValidationStatus(String filterValidationStatus)
	{
		this.getSession().put("filterValidationStatus", filterValidationStatus);
	}

	public String getFilterValidationStatus()
	{
		return (String) this.getSession().get("filterValidationStatus");
	}

	public void setFilterCostItem(String filterCostItem)
	{
		this.getSession().put("filterCostItem", filterCostItem);
	}

	public String getFilterCostItem()
	{
		return (String) this.getSession().get("filterCostItem");
	}

	public void setFilterPartner(String filterPartner)
	{
		this.getSession().put("filterPartner", filterPartner);
	}

	public String getFilterPartner()
	{
		return (String) this.getSession().get("filterPartner");
	}

	public Date getDocumentDate()
	{
		// return documentDate;
		return (Date) this.getSession().get("documentDate");
	}

	public void setDocumentDate(Date documentDate)
	{
		this.getSession().put("documentDate", documentDate);
		// this.documentDate = documentDate;
	}
	
	public Date getDocumentDateCF()
	{
		// return documentDate;
		return (Date) this.getSession().get("documentDateCF");
	}

	public void setDocumentDateCF(Date documentDateCF)
	{
		this.getSession().put("documentDateCF", documentDateCF);
		// this.documentDate = documentDate;
	}

	public String getPartnerID()
	{
		return partnerID;
	}

	public void setPartnerID(String partnerID)
	{
		this.partnerID = partnerID;
	}

	public String getCostDefID()
	{
		return costDefID;
	}

	public void setCostDefID(String costDefID)
	{
		this.costDefID = costDefID;
	}

	public String getDurNumber()
	{
		return (String) this.getSession().get("durNumber");
	}

	public void setDurNumber(String durNumber)
	{
		this.getSession().put("durNumber", durNumber);
	}

	public HtmlDataScroller getScroller()
	{
		return scroller;
	}

	public void setScroller(HtmlDataScroller scroller)
	{
		this.scroller = scroller;
	}

	/**
	 * Gets filterPhases
	 * 
	 * @return filterPhases the filterPhases
	 */
	public List<SelectItem> getFilterPhases()
	{
		return filterPhases;
	}

	/**
	 * Sets filterPhases
	 * 
	 * @param filterPhases
	 *            the filterPhases to set
	 */
	public void setFilterPhases(List<SelectItem> filterPhases)
	{
		this.filterPhases = filterPhases;
	}

	/**
	 * Gets filterPhase
	 * 
	 * @return filterPhase the filterPhase
	 */
	public Long getFilterPhase()
	{
		return (Long) this.getSession().get("filterPhase");
	}

	/**
	 * Sets filterPhase
	 * 
	 * @param filterPhase
	 *            the filterPhase to set
	 */
	public void setFilterPhase(Long filterPhase)
	{
		this.getSession().put("filterPhase", filterPhase);
	}

	/**
	 * Gets filterPhase
	 * 
	 * @return filterPhase the filterPhase
	 */
	public String getLocationId()
	{
		return (String)this.getSession().get("locationId");
	}

	/**
	 * Sets filterPhase
	 * 
	 * @param filterPhase
	 *            the filterPhase to set
	 */
	public void setLocationId(String locationId)
	{
		this.getSession().put("locationId", locationId);
	}
	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories()
	{
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SelectItem> categories)
	{
		ValidationFlowViewBean.categories = categories;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory()
	{
		return selectedCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory)
	{
		this.selectedCategory = selectedCategory;
	}

	public void lockEntity()
	{
		setLockEntity(true);
	}

	public void unlockEntity()
	{
		setLockEntity(false);
	}

	private void setLockEntity(boolean lock)
	{
		try
		{

			for (CostDefinitions item : this.getList())
			{
				if (this.getEntityEditId().equals(item.getId()))
				{
					item.setLocked(lock);
					item.setSelected(false);
					BeansFactory.CostDefinitions().Save(item);
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setActionMotivation(String actionMotivation)
	{
		this.actionMotivation = actionMotivation;
	}

	public String getActionMotivation()
	{
		return actionMotivation;
	}

	public void clearMotivation(ActionEvent event)
	{
		this.setActionMotivation("");
	}

	/**
	 * Gets entitySendId
	 * 
	 * @return entitySendId the entitySendId
	 */
	public Long getEntitySendId()
	{
		return (Long) this.getViewState().get("entitySendId");
	}

	/**
	 * Sets entitySendId
	 * 
	 * @param entitySendId
	 *            the entitySendId to set
	 */
	public void setEntitySendId(Long entitySendId)
	{
		this.getViewState().put("entitySendId", entitySendId);
	}

	/**
	 * Gets attachmentIsRequredError
	 * @return attachmentIsRequredError the attachmentIsRequredError
	 */
	public String getAttachmentIsRequredError()
	{
		return attachmentIsRequredError;
	}

	/**
	 * Sets attachmentIsRequredError
	 * @param attachmentIsRequredError the attachmentIsRequredError to set
	 */
	public void setAttachmentIsRequredError(String attachmentIsRequredError)
	{
		this.attachmentIsRequredError = attachmentIsRequredError;
	}

	public String getCostDefReportNumber()
	{
		return costDefReportNumber;
	}

	public void setCostDefReportNumber(String costDefReportNumber)
	{
		this.costDefReportNumber = costDefReportNumber;
	}

	public List<SelectItem> getFilterLocations()
	{
		return filterLocations;
	}

	public void setFilterLocations(List<SelectItem> filterLocations)
	{
		this.filterLocations = filterLocations;
	}
	
	public String getReportNumber()
	{
		if (getSession().containsKey("reportNumber"))
		{
			return (String) getSession().get("reportNumber");
		}
		return null;
	}

	public void setReportNumber(String reportNumber)
	{
		getSession().put("reportNumber", reportNumber);
	}
	
	
	public void setAdditionalFinansing(Boolean additionalFinansing)
	{
		getViewState().put("additionalFinansing", additionalFinansing);
	}

	public Boolean getAdditionalFinansing()
	{
		return (Boolean) getViewState().get("additionalFinansing");
	}
	
	public Double getTotalImportPartner()
	{
		return (Double) getViewState().get("totalImportPartner");
	}

	public void setTotalImportPartner(Double totalImportPartner)
	{
		getViewState().put("totalImportPartner", totalImportPartner);
	}
	
	public Double getTotalImportRevisore()
	{
		return (Double) getViewState().get("totalImportRevisore");
	}

	public void setTotalImportRevisore(Double totalImportRevisore)
	{
		getViewState().put("totalImportRevisore", totalImportRevisore);
	}
	
	public Double getTotalValidateFesr()
	{
		return (Double) getViewState().get("totalValidateFesr");
	}

	public void setTotalValidateFesr(Double totalValidateFesr)
	{
		getViewState().put("totalValidateFesr", totalValidateFesr);
	}
	
	public Double getTotalValidateCpn()
	{
		return (Double) getViewState().get("totalValidateCpn");
	}

	public void setTotalValidateCpn(Double totalValidateCpn)
	{
		getViewState().put("totalValidateCpn", totalValidateCpn);
	}
	
	public Double getTotalValidateAddFin()
	{
		return (Double) getViewState().get("totalValidateAddFin");
	}

	public void setTotalValidateAddFin(Double totalValidateAddFin)
	{
		getViewState().put("totalValidateAddFin", totalValidateAddFin);
	}
	
	public Long getTotalNumberOfCostDefinitions()
	{
		return (Long) getViewState().get("totalNumberOfCostDefinitions");
	}
	
	public void setTotalNumberOfCostDefinitions(Long totalNumberOfCostDefinitions)
	{
		getViewState().put("totalNumberOfCostDefinitions", totalNumberOfCostDefinitions);
	}

	public UploadedFile getNewDocumentUpFileCF()
	{
		return newDocumentUpFileCF;
	}

	public void setNewDocumentUpFileCF(UploadedFile newDocumentUpFileCF)
	{
		this.newDocumentUpFileCF = newDocumentUpFileCF;
	}

	public Documents getNewDocumentCF()
	{
		return newDocumentCF;
	}

	public void setNewDocumentCF(Documents newDocumentCF)
	{
		this.newDocumentCF = newDocumentCF;
	}

	public String getNewDocTitleCF()
	{
		return newDocTitleCF;
	}

	public void setNewDocTitleCF(String newDocTitleCF)
	{
		this.newDocTitleCF = newDocTitleCF;
	}

	public String getSelectedCategoryCF()
	{
		return selectedCategoryCF;
	}

	public void setSelectedCategoryCF(String selectedCategoryCF)
	{
		this.selectedCategoryCF = selectedCategoryCF;
	}	
	
	public void setFilterCostPhaseAsse3(List<SelectItem> filterCostPhaseAsse3)
	{
		this.getViewState().put("filterCostPhaseAsse3", filterCostPhaseAsse3);
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCostPhaseAsse3()
	{
		return (List<SelectItem>) this.getViewState().get(
				"filterCostPhaseAsse3");
	}
	
	public void setFilterCostItemAsse3(List<SelectItem> filterCostItemAsse3)
	{
		this.getViewState().put("filterCostItemAsse3", filterCostItemAsse3);
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCostItemAsse3()
	{
		return (List<SelectItem>) this.getViewState().get(
				"filterCostItemAsse3");
	}
	
	public void setSelectedCostPhaseAsse3(String selectedCostPhaseAsse3)
	{
		this.getViewState().put("selectedCostPhaseAsse3",
				selectedCostPhaseAsse3);
	}

	public String getSelectedCostPhaseAsse3()
	{
		return (String) this.getViewState().get("selectedCostPhaseAsse3");
	}

	public void setSelectedCostItemAsse3(String selectedCostItemAsse3)
	{
		this.getViewState().put("selectedCostItemAsse3", selectedCostItemAsse3);
	}

	public String getSelectedCostItemAsse3()
	{
		return (String) this.getViewState().get("selectedCostItemAsse3");
	}
	
	public void setAsse3Mode(Boolean asse3Mode)
	{
		this.getViewState().put("asse3Mode", asse3Mode);
	}

	public Boolean getAsse3Mode()
	{
		return (Boolean) this.getViewState().get("asse3Mode");
	}
	
}
