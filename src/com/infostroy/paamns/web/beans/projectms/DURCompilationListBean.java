/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.CostDefinitionSuspendStatus;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.RoleTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurACUAcceptMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurAGUAcceptMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurCFSendMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurSTCApproveMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurSTCRefuseMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurUCApproveMailSender;
import com.infostroy.paamns.common.helpers.mail.ManagmentDurUCRefuseMailSender;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.DurStates;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.DURCompilationWrapper;
import com.infostroy.paamns.web.converters.BaseConverter;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class DURCompilationListBean extends EntityProjectListBean<Object>
{
	private static Long					entitySendId;

	private static Long					entityDenyId;

	private boolean						canAdd;

	// Role support

	@SuppressWarnings("unused")
	private RoleTypes					currentRoleType;

	@SuppressWarnings("unused")
	private String						filter;

	// /////////////////////////////////////////////////////////////////////////////

	// Variables

	private List<DURCompilationWrapper>	listDurs;

	private List<SelectItem>			listFilter;

	private Map<String, String>			showAttention;

	List<InfoObject>					mailsCFSend;

	List<InfoObject>					mailsUCSend;

	List<InfoObject>					mailsSTCApprove;

	List<InfoObject>					mailsUCApprove;

	List<InfoObject>					mailsSTCRefuse;

	List<InfoObject>					mailsUCRefuse;

	List<InfoObject>					mailsAGUApprove;

	List<InfoObject>					mailsACUApprove;

	private List<SelectItem>			filterDurStatusValues;

	private List<SelectItem>			filterDurCostItem;
	
	private List<SelectItem>			filterPhase;
	
	private List<SelectItem>			filterLocation;

	private String						durReimbNumber;

	private String						numberPayment;

	private String						partner;

	private String						idPartner;

	private boolean						filterCertifiedDUR;

	private HtmlDataScroller			scroller;

	private String						actionMotivation;

	// //////////////////////////////////////////////////////////////////////////////

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		if (this.getFilter() == null || this.getFilter().equals(""))
		{
			this.getViewState().put("durCompilationListFilter", "0");
		}
		this.showAttention = new HashMap<String, String>();

		this.getShowAttention().put("ProtNumber", "none");
		this.getShowAttention().put("Moving", "none");

		List<CostDefinitions> listCDFree = BeansFactory.CostDefinitions()
				.GetByDurCompilation(null);
		if (listCDFree.isEmpty())
		{
			this.setCanAdd(false);
		}
		else if (getSessionBean().isCFW())
		{
			this.setCanAdd(true);
		}
		else if (getSessionBean().isAGUW())
		{
			this.setCanAdd(true);
		}
		else
		{
			this.setCanAdd(false);
		}

		this.GetCurrentRole();
		this.LoadEntities();
		this.ReRenderScroll();
		this.LoadFilterValues();
		this.fillDropDown();
		this.doFilter();
	}

	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListDurs(),
					ExportPlaces.DurCompilations);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportDurCompilations")
					+ ".xls", is, data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void doFilter() throws PersistenceBeanException
	{
		List<DURCompilationWrapper> tempList = new ArrayList<DURCompilationWrapper>();
		for (DURCompilationWrapper item : this.getListDurs())
		{
			if (this.getDurFilterValidationStatus() != null
					&& !this.getDurFilterValidationStatus().isEmpty())
			{
				if (!item.getStateId().equals(
						Long.parseLong(this.getDurFilterValidationStatus())))
				{
					continue;
				}
			}
			if (this.getDurReimbNumber() != null
					&& !this.getDurReimbNumber().isEmpty())
			{
				try
				{
					int reimbNumber = Integer
							.parseInt(this.getDurReimbNumber());
					if (item.getRowNumber() != reimbNumber)
					{
						continue;
					}
				}
				catch (NumberFormatException e)
				{
					log.error(e);
				}
			}
			if (this.getNumberPayment() != null
					&& !getNumberPayment().isEmpty())
			{
				if (item.getPaymentRequestNumber() == null)
				{
					continue;
				}
				if (!item.getPaymentRequestNumber().toString()
						.contains(getNumberPayment()))
				{
					continue;
				}
			}
			if (this.getPartner() != null && !this.getPartner().isEmpty())
			{
				boolean containAuthorName = false;
				try
				{
					List<CostDefinitions> listCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(item.getId());
					for (CostDefinitions cd : listCD)
					{

						CFPartnerAnagraphs anagraph = BeansFactory
								.CFPartnerAnagraphs().GetByUser(
										cd.getUser().getId());

						if (anagraph != null)
						{
							String naming = anagraph.getNaming().toLowerCase();
							String partnerName = this.getPartner()
									.toLowerCase().trim();
							if (naming.contains(partnerName))
							{
								containAuthorName = true;
								break;
							}
						}
					}

				}
				catch (PersistenceBeanException e)
				{

				}
				if (!containAuthorName)
				{
					continue;
				}
			}
			if (this.getIdPartner() != null && !getIdPartner().isEmpty())
			{
				boolean containAuthorID = false;
				try
				{
					List<CostDefinitions> listCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(item.getId());
					for (CostDefinitions cd : listCD)
					{
						CFPartnerAnagraphs anagraph = BeansFactory
								.CFPartnerAnagraphs().GetByUser(
										cd.getUser().getId());
						if (anagraph != null)
						{
							if (anagraph.getProgressiveId().equals(
									Long.parseLong(this.getIdPartner())))

							{
								containAuthorID = true;
								break;
							}
						}
					}
					/*
					 * List<CostDefinitions> listCD = BeansFactory
					 * .CostDefinitions() .GetByDurCompilation(item.getId());
					 * for (CostDefinitions cd : listCD) { if
					 * (cd.getUser().getId()
					 * .equals(Long.parseLong(this.getIdPartner()))) {
					 * containAuthorID = true; break; } }
					 */

				}
				catch (PersistenceBeanException e)
				{

				}
				if (!containAuthorID)
				{
					continue;
				}
			}
			
			
			if (Boolean.TRUE.equals(this.getAsse3Mode()))
			{
				if (this.getSelectedCostPhaseAsse3() != null
						&& !this.getSelectedCostPhaseAsse3().isEmpty())
				{
					List<CostDefinitions> listCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(item.getId());
					boolean containsPhase = false;
					for (CostDefinitions cd : listCD)
					{
						if (cd.getCostItemPhaseAsse3().getPhase() == null
								|| cd.getCostItemPhaseAsse3()
										.getPhase()
										.name()
										.equals(this
												.getSelectedCostPhaseAsse3()))
						{
							containsPhase = true;
							break;
						}
					}

					if (!containsPhase)
					{
						continue;
					}

				}

				if (this.getSelectedCostItemAsse3() != null
						&& !this.getSelectedCostItemAsse3().isEmpty())
				{
					List<CostDefinitions> listCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(item.getId());

					boolean containsItem = false;
					for (CostDefinitions cd : listCD)
					{
						if (cd.getCostItemPhaseAsse3().getPhase() == null
								|| cd.getCostItemPhaseAsse3()
										.getValue()
										.equals(this.getSelectedCostItemAsse3()))
						{
							containsItem = true;
							break;
						}
					}
					if (!containsItem)
					{
						continue;
					}

				}
			}
			else
			{
				if (getFilterPhaseId() != null && !getFilterPhaseId().isEmpty())
				{
					boolean containPhase = false;
					try
					{
						List<CostDefinitions> listCD = BeansFactory
								.CostDefinitions().GetByDurCompilation(
										item.getId());
						for (CostDefinitions cd : listCD)
						{
							if (cd.getCostDefinitionPhase() != null
									&& cd.getCostDefinitionPhase()
											.getId()
											.equals(Long
													.parseLong(getFilterPhaseId())))
							{
								containPhase = true;
								break;
							}
						}

					}
					catch (PersistenceBeanException e)
					{

					}
					if (!containPhase)
					{
						continue;
					}
				}
				if (getDurFilterCostItem() != null
						&& !getDurFilterCostItem().isEmpty())
				{
					boolean containCostItem = false;
					try
					{
						List<CostDefinitions> listCD = BeansFactory
								.CostDefinitions().GetByDurCompilation(
										item.getId());
						for (CostDefinitions cd : listCD)
						{
							if (cd.getCostItem() != null
									&& cd.getCostItem()
											.getId()
											.equals(Long
													.parseLong(getDurFilterCostItem())))
							{
								containCostItem = true;
								break;
							}
						}

					}
					catch (PersistenceBeanException e)
					{

					}
					if (!containCostItem)
					{
						continue;
					}
				}
			}
			if (getFilterLocationId() != null
					&& !getFilterLocationId().isEmpty())
			{
				boolean containLocation = false;
				try
				{
					List<CostDefinitions> listCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(item.getId());
					for (CostDefinitions cd : listCD)
					{
						if (cd.getLocation() != null
								&& cd.getLocation().name().equals(getFilterLocationId()))
						{
							containLocation = true;
							break;
						}
					}

				}
				catch (PersistenceBeanException e)
				{

				}
				if (!containLocation)
				{
					continue;
				}
			}
			
			if (getFilterCertifiedDUR() != false)
			{
				if (item.getCertificationDate() == null)
				{
					continue;
				}
			}
			tempList.add(item);
		}
		this.setListDurs(tempList);
	}

	public void fillDropDown() throws PersistenceBeanException
	{
		filterDurStatusValues = new ArrayList<SelectItem>();
		this.filterDurStatusValues.add(SelectItemHelper.getAllElement());
		boolean canSeeExt = false;

		for (UserRoles ur : this.getCurrentUser().getRoles())
		{
			if (ur.getRole().getCode().equals(UserRoleTypes.BP.getValue())
					|| ur.getRole().getCode()
							.equals(UserRoleTypes.STC.getValue())
					|| ur.getRole().getCode()
							.equals(UserRoleTypes.UC.getValue())
					|| ur.getRole().getCode()
							.equals(UserRoleTypes.AGU.getValue()))
			{
				canSeeExt = true;
				break;
			}
		}

		if (canSeeExt)
		{
			for (DurStates item : BeansFactory.DurStates().Load())
			{
				if (item.getId().equals(DurStateTypes.STCEvaluation.getValue())
						|| item.getId().equals(
								DurStateTypes.AGUEvaluation.getValue())
						|| item.getId().equals(
								DurStateTypes.Create.getValue())
						|| item.getId().equals(
								DurStateTypes.ACUEvaluation.getValue())
						|| item.getId().equals(
								DurStateTypes.Certifiable.getValue()))
				{
					filterDurStatusValues.add(new SelectItem(item.getId()
							.toString(), item.getValue()));
				}
			}
		}

		boolean canSeeAcu = false;
		for (UserRoles ur : this.getCurrentUser().getRoles())
		{
			if (ur.getRole().getCode().equals(UserRoleTypes.ACU.getValue()))
			{
				canSeeAcu = true;
				break;
			}
		}
		if (canSeeAcu)
		{
			for (DurStates item : BeansFactory.DurStates().Load())
			{
				if (item.getId().equals(DurStateTypes.ACUEvaluation.getValue()))
				{
					if (canSeeExt == false)
					{
						filterDurStatusValues.add(new SelectItem(item.getId()
								.toString(), item.getValue()));
					}
				}
			}
		}
		
		this.filterLocation = new ArrayList<SelectItem>();
		
		this.filterLocation.add(SelectItemHelper.getAllElement());
		for(LocationForCostDef loc : LocationForCostDef.values()){
			this.filterLocation.add(new SelectItem(loc.name(), loc.toString()));
		}
		if(Boolean.FALSE.equals(this.getAsse3Mode())){
			this.filterPhase = new ArrayList<SelectItem>();
			
			this.filterPhase.add(SelectItemHelper.getAllElement());
			for(CostDefinitionPhases phase : BeansFactory.CostDefinitionPhase().Load()){
				this.filterPhase.add(new SelectItem(String.valueOf(phase.getId()), phase.getValue()));
			}
			

			filterDurCostItem = new ArrayList<SelectItem>();
			this.filterDurCostItem.add(SelectItemHelper.getAllElement());
			for (CostItems item : BeansFactory.CostItems().Load())
			{
				filterDurCostItem.add(new SelectItem(String.valueOf(item.getId()), item
						.getValue()));
			}
		}
		else{
			FillAsse3Phases();
			FillCostItemsAsse3();
		}
		
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
	
	public void search()
	{
		try
		{
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
			this.setDurFilterValidationStatus(null);
			this.setDurReimbNumber(null);
			this.setNumberPayment(null);
			this.setPartner(null);
			this.setIdPartner(null);
			this.setFilterCertifiedDUR(false);
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

	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		if (!this.getSessionBean().getIsActualSate())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
		//this.setAsse3Mode(Boolean.valueOf(this.getProject().getAsse()==3));
		this.setAsse3Mode(Boolean.FALSE);
		if (!(AccessGrantHelper.IsReadyDurCompilation() && AccessGrantHelper
				.CheckRolesDURCompilation()))
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	public void ReRenderScroll()
	{
		if (this.getListDurs() != null
				&& this.getListDurs().size() > Integer
						.parseInt(getItemsPerPage()))
		{
			setShowScroll(true);
		}
		else
		{
			setShowScroll(false);
		}
	}

	private void LoadFilterValues()
	{
		this.listFilter = new ArrayList<SelectItem>();
		this.listFilter.add(new SelectItem("0", Utils.getString("Resources",
				"all", null)));
		this.listFilter.add(new SelectItem("1", Utils.getString("Resources",
				"durCompilationListFilterRegular", null)));
		this.listFilter.add(new SelectItem("2", Utils.getString("Resources",
				"durCompilationListFilterIrregular", null)));

		if (this.getCurrentRoleType().equals(RoleTypes.ACU_READ)
				|| this.getCurrentRoleType().equals(RoleTypes.ACU_WRITE))
		{
			this.listFilter.add(new SelectItem("3", Utils.getString(
					"Resources", "durCompilationListFilterCertifiable", null)));

			this.listFilter.add(new SelectItem("4", Utils.getString(
					"Resources", "durRefusedByACUManager", null)));
		}
	}
	
	public void asse3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException{
		this.setSelectedCostPhaseAsse3((String)event.getNewValue());
		this.FillCostItemsAsse3();
	}
	
	private void LoadEntities() throws HibernateException,
			NumberFormatException, PersistenceBeanException
	{
		this.setListDurs(new ArrayList<DURCompilationWrapper>());

		List<DurCompilations> listDurs = BeansFactory.DurCompilations()
				.LoadByProject(Long.valueOf(this.getProjectId()),
						this.getFilter());

		int counter = 0;
		for (DurCompilations dc : listDurs)
		{
			counter++;

			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
					dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(
					dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(counter, dc,
					dc.getDurState(), di, ds);
			dcw.setLocked(dc.getLocked());
			// dcw.setSendAvailable(!(this.getCurrentRoleType().equals(
			// RoleTypes.ACU_READ) || this.getCurrentRoleType().equals(
			// RoleTypes.ACU_WRITE)));

			List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
					.GetByDurCompilation(dc.getId());

			if (listCD == null || listCD.isEmpty())
			{
				dcw.setSendAvailable(false);
				dcw.setDeleteAvailable(false);
				dcw.setDenyAvailable(false);
				dcw.setEditAvailable(false);
				dcw.setShowAvailable(false);
			}
			else
			{
				recalculateDate(dcw, listCD);
				boolean hasRole = false;
				dcw.setShowAvailable(true);

				if (RoleTypes.BP_READ.equals(this.getCurrentRoleType())
						|| RoleTypes.BP_WRITE.equals(this.getCurrentRoleType())
						|| (this.getSessionBean().isAGU()
								&& this.getProject().getAsse() == 5 && BeansFactory
								.DurCompilations().Load(dcw.getId())
								.getDurState().getId()
								.equals(DurStateTypes.Create.getValue())))
				{
					if (dc.getDurState().getId()
							.equals(DurStateTypes.Create.getValue()))
					{
						dcw.setSendAvailableAdd(true);
						dcw.setEditAvailableAdd(true);
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(false);
						dcw.setDeleteAvailableAdd(true);
						hasRole = true;
					}
					else
					{
						dcw.setSendAvailableAdd(false);
						dcw.setEditAvailableAdd(false);
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(false);
						dcw.setDeleteAvailableAdd(false);
						hasRole = true;
					}
				}
				if (this.getSessionBean().isSTC()
						|| this.getSessionBean().isSTCW()
						|| this.getSessionBean().isUC()
						|| this.getSessionBean().isUCW())
				{
					if (dc.getDurState().getId()
							.equals(DurStateTypes.STCEvaluation.getValue())
							|| dc.getDurState()
									.getId()
									.equals(DurStateTypes.UCValidation
											.getValue()))
					{
						dcw.setSendAvailableAdd(this
								.CheckStcToAguAvailable(listCD)
								&& (this.getSessionBean().isSTCW() || this
										.getSessionBean().isUC()));
						dcw.setEditAvailableAdd(this.getSessionBean().isSTCW()
								|| this.getSessionBean().isACUW()
								|| this.getSessionBean().isUCW());
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(this.getSessionBean().isSTCW()
								|| this.getSessionBean().isACUW()
								|| this.getSessionBean().isUCW());
						dcw.setDeleteAvailableAdd(true);
						getListDurs().add(dcw);
						continue;
					}
					else
					{
						dcw.setSendAvailableAdd(false);
						dcw.setEditAvailableAdd(false);
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(false);
						dcw.setDeleteAvailableAdd(false);
						hasRole = true;
					}
				}
				if (this.getSessionBean().isAGU()
						|| this.getSessionBean().isAGUW())
				{
					if (dc.getDurState().getId()
							.equals(DurStateTypes.AGUEvaluation.getValue()))
					{
						dcw.setSendAvailableAdd(this
								.CheckAguToAcuAvailable(listCD)
								&& this.getSessionBean().isAGUW());
						dcw.setEditAvailableAdd(this.getSessionBean().isAGUW());
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(this.getSessionBean().isAGUW());
						dcw.setDeleteAvailableAdd(true);
						getListDurs().add(dcw);
						continue;
					}
					else if (!(this.getProject().getAsse() == 5 && dc
							.getDurState().getId()
							.equals(DurStateTypes.Create.getValue())))
					{
						dcw.setSendAvailableAdd(false);
						if(dc.getDurState().getId().equals(DurStateTypes.Certified.getValue())){
							dcw.setEditAvailableAdd(this.getSessionBean().isAGUW());
						}else{
							dcw.setEditAvailableAdd(false);
						}						
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(false);
						dcw.setDeleteAvailableAdd(false);
						hasRole = true;
					}
				}
				if (this.getSessionBean().isACU()
						|| this.getSessionBean().isACUW())
				{
					if (dc.getDurState().getId()
							.equals(DurStateTypes.ACUEvaluation.getValue()))
					{
						dcw.setSendAvailableAdd(this.CheckAcuAvailable(listCD)
								&& this.getSessionBean().isACUW());
						dcw.setEditAvailableAdd(this.getSessionBean().isACUW());
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(this.getSessionBean().isACUW());
						dcw.setDeleteAvailableAdd(true);
						getListDurs().add(dcw);
						continue;
					}
					else if (dc.getDurState().getId().equals(DurStateTypes.Certified.getValue()))
					{
                        dcw.setEditAvailableAdd(this.getSessionBean().isACUW());
                        dcw.setShowAvailableAdd(true); 
					}
					else
					{
						dcw.setSendAvailableAdd(false);
						if(dc.getDurState().getId().equals(DurStateTypes.Certified.getValue())){
							dcw.setEditAvailableAdd(this.getSessionBean().isACUW());
						}else{
							dcw.setEditAvailableAdd(false);
						}	
						dcw.setShowAvailableAdd(true);
						dcw.setDenyAvailableAdd(false);
						dcw.setDeleteAvailableAdd(false);
						hasRole = true;
					}
				}
				if (this.getSessionBean().isAAU())
				{
					dcw.setShowAvailable(true);
					hasRole = true;
				}

				if (this.getSessionBean().isSuperAdmin())
				{
					dcw.setEditAvailableAdd(true);
					dcw.setShowAvailableAdd(true);
					hasRole = true;
				}

				if (this.getSessionBean().isACUM()
						|| this.getSessionBean().isACUMW())
				{
					dcw.setSendAvailableAdd(false);
					dcw.setEditAvailableAdd(false);
					dcw.setShowAvailableAdd(true);
					dcw.setDenyAvailableAdd(false);
					dcw.setDeleteAvailableAdd(false);
					getListDurs().add(dcw);
					continue;
				}
				else if (!hasRole)
				{
					if (!(this.getSessionBean().isACU() || this.getSessionBean().isACUW())) {
						dcw.setEditAvailable(false);
						dcw.setShowAvailable(false);
					}
					dcw.setSendAvailable(false);
					dcw.setDeleteAvailable(false);
					dcw.setDenyAvailable(false);
				}
			}
			getListDurs().add(dcw);
		}
	}

	private void recalculateDate(DURCompilationWrapper dcw,
			List<CostDefinitions> listCD)
	{
		// for CR 0027351

		dcw.setCompilationDate(listCD.get(0).getCilDate());

		if (listCD.get(0).getCfDate() != null
				&& dcw.getCompilationDate().before(listCD.get(0).getCfDate()))
		{
			dcw.setCompilationDate(listCD.get(0).getCfDate());
		}
		if (listCD.get(0).getSTCSertified()
				&& listCD.get(0).getStcCertDate() != null
				&& dcw.getCompilationDate().before(
						listCD.get(0).getStcCertDate()))
		{
			dcw.setCompilationDate(listCD.get(0).getStcCertDate());
		}
		if (listCD.get(0).getAGUSertified()
				&& listCD.get(0).getAguCertDate() != null
				&& dcw.getCompilationDate().before(
						listCD.get(0).getAguCertDate()))
		{
			dcw.setCompilationDate(listCD.get(0).getAguCertDate());
		}
		if (listCD.get(0).getACUSertified()
				&& listCD.get(0).getAcuCertDate() != null
				&& dcw.getCompilationDate().before(
						listCD.get(0).getAcuCertDate()))
		{
			dcw.setCompilationDate(listCD.get(0).getAcuCertDate());
		}
	}

	private boolean CheckStcToAguAvailable(List<CostDefinitions> list)
	{
		boolean valid = true;

		for (CostDefinitions cd : list)
		{
			if (!cd.getSTCSertified())
			{
				valid = false;
				break;
			}
		}

		return valid;
	}

	private boolean CheckAguToAcuAvailable(List<CostDefinitions> list)
	{
		boolean valid = true;

		for (CostDefinitions cd : list)
		{
			if (!cd.getAGUSertified())
			{
				valid = false;
				break;
			}
		}

		return valid;
	}

	private boolean CheckAcuAvailable(List<CostDefinitions> list)
	{
		boolean valid = true;

		for (CostDefinitions cd : list)
		{
			if (!cd.getACUSertified())
			{
				valid = false;
				break;
			}
		}

		return valid;
	}

	/**
	 * Resolves the current user role
	 */
	private void GetCurrentRole()
	{
		if (this.getSessionBean().isCF())
		{
			this.setCurrentRoleType(RoleTypes.BP_WRITE);
		}
		else if (this.getSessionBean().isSTC())
		{
			this.setCurrentRoleType(RoleTypes.STC_WRITE);
		}
		else if (this.getSessionBean().isAGU())
		{
			this.setCurrentRoleType(RoleTypes.AGU_WRITE);
		}
		else if (this.getSessionBean().isACU())
		{
			this.setCurrentRoleType(RoleTypes.ACU_WRITE);
		}
		else if (this.getSessionBean().isACUM())
		{
			this.setCurrentRoleType(RoleTypes.ACUM_WRITE);
		}
		else if (this.getSessionBean().isAAU())
		{
			this.setCurrentRoleType(RoleTypes.AAU_WRITE);
		}
		else if (this.getSessionBean().isSuperAdmin())
		{
			this.setCurrentRoleType(RoleTypes.SUPER_ADMIN_WRITE);
		}
		else if (this.getSessionBean().isUC())
		{
			this.setCurrentRoleType(RoleTypes.UC_WRITE);
		}
	}

	@Override
	public void addEntity()
	{
		this.getSession().put("durCompilationEdit", null);
		this.getSession().put("durCompilationEditShow", false);
		this.getSession().put("durCompilationEditBack", true);

		this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW, false);
		this.getSession()
				.put(DURCompilationEditBean.FILTER_PARTNER_VALUE, null);
		this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE, null);
		this.getSession().put(DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE,
				null);
		this.getSession().put(DURCompilationEditBean.FILTER_COST_ITEM, null);
		this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW, false);

		this.goTo(PagesTypes.DURCOMPILATIONEDIT);
	}

	@Override
	public void deleteEntity()
	{
		DurCompilations dc = null;
		try
		{
			dc = BeansFactory.DurCompilations().Load(this.getEntityDeleteId());
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (dc != null && !dc.getDeleted())
		{
			DurInfos durInfo = null;
			try
			{
				durInfo = BeansFactory.DurInfos().LoadByDurCompilation(
						this.getEntityDeleteId());
				BeansFactory.DurInfos().Delete(durInfo);
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}

			DurSummaries durSummaries = null;
			try
			{
				durSummaries = BeansFactory.DurSummaries()
						.LoadByDurCompilation(this.getEntityDeleteId());
				BeansFactory.DurSummaries().Delete(durSummaries);
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}

			List<CostDefinitions> listCD = null;
			try
			{
				listCD = BeansFactory.CostDefinitions().GetByDurCompilation(
						this.getEntityDeleteId());

				if (listCD != null)
				{
					for (CostDefinitions cd : listCD)
					{
						cd.setDurCompilation(null);
						cd.setAcuCertification(null);
						cd.setAguCertification(null);
						cd.setStcCertification(null);
						cd.setAcuCertDate(null);
						cd.setStcCertDate(null);
						cd.setAguCertDate(null);
						cd.setSTCSertified(false);
						cd.setAGUSertified(false);
						cd.setACUSertified(false);
						BeansFactory.CostDefinitions().Save(cd);
					}
				}
			}
			catch (PersistenceBeanException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void editEntity()
	{
		DurCompilations dc = null;
		try
		{
			dc = BeansFactory.DurCompilations().Load(this.getEntityEditId());
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (dc != null && !dc.getDeleted())
		{
			this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW,
					false);
			this.getSession().put(DURCompilationEditBean.FILTER_PARTNER_VALUE,
					null);
			this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE,
					null);
			this.getSession().put(
					DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE, null);
			this.getSession()
					.put(DURCompilationEditBean.FILTER_COST_ITEM, null);
			this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW,
					false);

			this.getSession().put("durCompilationEdit", this.getEntityEditId());
			this.getSession().put("durCompilationEditShow", false);
			this.getSession().put("durCompilationEditBack", true);
			this.goTo(PagesTypes.DURCOMPILATIONEDIT);
		}
	}

	public void preSendItem() throws HibernateException,
			PersistenceBeanException
	{
		DurCompilations dc = BeansFactory.DurCompilations().Load(
				this.getEntitySendId());
		if (dc != null && !dc.getDeleted())
		{
			if (dc.getDurState().getId()
					.equals(DurStateTypes.Create.getValue()))
			{
				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
						dc.getId());
				if (di.getProtocolNumber().isEmpty())
				{
					this.getShowAttention().put("ProtNumber", "");
				}
				else
				{
					this.getShowAttention().put("ProtNumber", "none");
					this.sendItem();
				}
			}
			else
			{
				this.sendItem();
			}
		}
		else
		{
			this.sendItem();
		}
	}

	private void recalcSum(DurCompilations dc, DurSummaries ds)
			throws HibernateException, PersistenceBeanException
	{
		if (dc == null || ds == null)
		{
			return;
		}
		
		Double sum = 0d;
		List<CostDefinitions> listCostTable = BeansFactory.CostDefinitions()
				.GetByDurCompilation(dc.getId());
		for (CostDefinitions cd : listCostTable)
		{

			if (cd.getAcuCertification() != null
					&& cd.getAcuCertification() > 0 && cd.getACUSertified())
			{
				sum += cd.getAcuCertification();
			}
			else if (cd.getAguCertification() != null
					&& cd.getAguCertification() > 0 && cd.getAGUSertified())
			{
				sum += cd.getAguCertification();
			}
			else if (cd.getStcCertification() != null
					&& cd.getStcCertification() > 0 && cd.getSTCSertified())
			{
				sum += cd.getStcCertification();
			}
			else if (cd.getCfCheck() != null)
			{
				sum += cd.getCfCheck();
			}
			else if (cd.getCilCheck() != null)
			{
				sum += cd.getCilCheck();
			}
			else if(cd.getAmountReport() != null)
			{
				sum += cd.getAmountReport();
			}
		}

		ds.setTotalAmount(sum);
		BeansFactory.DurSummaries().Save(ds);
	}

	public String moveToPreviousStep() throws HibernateException,
			PersistenceBeanException
	{
		if (this.getActionMotivation() == null
				|| this.getActionMotivation().isEmpty()
				|| !this.getSessionBean().isSuperAdmin())
		{
			return null;
		}
		DurCompilations dc = BeansFactory.DurCompilations().Load(
				this.getEntitySendId());
		DurInfos di = null;
		if (evaluateDur(dc.getId()))
		{
			SuperAdminChange changes = new SuperAdminChange(
					ChangeType.MOVING_TO_PREVIOUS_STEP, this.getCurrentUser(),
					this.getActionMotivation(), this.getSessionBean()
							.getProject());
			changes.setChanges(Utils
					.getString("superAdminChangeLogMessageHeader")
					.replace("%1",
							Utils.getString("superAdminChangesDurCompilation"))
					.replace("%3", Utils.getString("durCompilationListName"))
					+ "<br/>"
					+ Utils.getString("superAdminChangeLogMessage")
							.replace(
									"%1",
									Utils.getString("durCompilationListHeaderState"))
							.replace("%2", dc.getDurState().toString()));

			if (dc.getDurState().getId()
					.equals(DurStateTypes.STCEvaluation.getValue()))
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.Create);

				for (CostDefinitions cd : BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId()))
				{
					cd.setStcCertification(null);
					cd.setStcCertDate(null);
					BeansFactory.CostDefinitions().Save(cd);
				}

				di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());

				BeansFactory.DurInfos().Save(di);

				mailsCFSend = getMailsForSend(UserRoleTypes.BP, di
						.getDurInfoNumber().toString(),
						di.getDurCompilationDate(), ds.getTotalAmount());
			}
			else if (dc.getDurState().getId()
					.equals(DurStateTypes.AGUEvaluation.getValue()))
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.STCEvaluation);

				for (CostDefinitions cd : BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId()))
				{
					cd.setAguCertification(null);
					cd.setAguCertDate(null);
					BeansFactory.CostDefinitions().Save(cd);
				}
				di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());

				BeansFactory.DurInfos().Save(di);

				mailsSTCApprove = getMailsForSend(UserRoleTypes.STC, di
						.getDurInfoNumber().toString(),
						di.getDurCompilationDate(), ds.getTotalAmount());
			}
			else if (dc.getDurState().getId()
					.equals(DurStateTypes.ACUEvaluation.getValue()))
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.AGUEvaluation);

				dc.setReimbursement(false);
				dc.setReimbursed(false);

				for (CostDefinitions cd : BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId()))
				{
					cd.setAcuCertification(null);
					cd.setAcuCertDate(null);
					BeansFactory.CostDefinitions().Save(cd);
				}

				di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());

				BeansFactory.DurInfos().Save(di);

				mailsAGUApprove = getMailsForSend(UserRoleTypes.AGU, di
						.getDurInfoNumber().toString(),
						di.getDurCompilationDate(), ds.getTotalAmount());
			}
			BeansFactory.DurCompilations().Save(dc);
			changes.setChanges(changes.getChanges()
					.replace("%2", String.valueOf(di.getDurInfoNumber()))
					.replace("%3", dc.getDurState().toString()));
			BeansFactory.SuperAdminChanges().Save(changes);
			LoadEntities();
		}
		else
		{
			this.getShowAttention().put("Moving", "");
		}
		clearMotivation(null);
		return null;
	}

	/**
	 * @param dc
	 * @param newStateType
	 *            TODO
	 * @return
	 * @throws PersistenceBeanException
	 */
	private DurSummaries changeStateAndRecalculateSum(DurCompilations dc,
			DurStateTypes newStateType) throws PersistenceBeanException
	{
		DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(
				dc.getId());

		recalcSum(dc, ds);

		dc.setDurState(BeansFactory.DurStates().Load(newStateType.getValue()));
		return ds;
	}

	/**
	 * @param userRoleType
	 *            TODO
	 * @param durInfoNumber
	 *            TODO
	 * @param durCompilationDate
	 *            TODO
	 * @param totalAmount
	 *            TODO
	 * @param ds
	 * @param di
	 * @return
	 * @throws PersistenceBeanException
	 */
	private List<InfoObject> getMailsForSend(UserRoleTypes userRoleType,
			String durInfoNumber, Date durCompilationDate, Double totalAmount)
			throws PersistenceBeanException
	{
		ArrayList<InfoObject> result = new ArrayList<InfoObject>();
		for (Users item : BeansFactory.Users().getByRole(userRoleType))
		{
			InfoObject ob = new InfoObject();
			ob.setProjectName(this.getProject().getTitle());
			ob.setNumber(durInfoNumber);
			ob.setDurCompilationDate(BaseConverter
					.convertToDateString(durCompilationDate));
			ob.setTotalAmountOfDUR(BaseConverter
					.convertToDoubleString(totalAmount));
			ob.setMail(item.getMail());
			result.add(ob);
		}
		return result;
	}

	/**
	 * @param id
	 * @return
	 * @throws PersistenceBeanException
	 */
	private boolean evaluateDur(Long id) throws PersistenceBeanException
	{
		for (CostDefinitions cd : BeansFactory.CostDefinitions()
				.GetByDurCompilation(id))
		{
			if (cd.getSuspensionStatus() != null
					|| cd.getRectificationState() != null)
			{
				return false;
			}
		}
		return true;
	}

	public void sendItem() throws HibernateException, PersistenceBeanException
	{
		DurCompilations dc = BeansFactory.DurCompilations().Load(
				this.getEntitySendId());
		boolean sent = false;

		if (DurStateTypes.Create.getValue().equals(dc.getDurState().getId()))
		{
			sent = true;
		}

		if (dc != null && !dc.getDeleted())
		{

			if (dc.getDurState().getId()
					.equals(DurStateTypes.Create.getValue()))
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.STCEvaluation);

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				for (CostDefinitions cd : listCD)
				{
					cd.setStcCertification(null);
					BeansFactory.CostDefinitions().Save(cd);
				}

				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
						dc.getId());

				di.setDurCompilationDate(new Date());
				BeansFactory.DurInfos().Save(di);
				
				if(this.getSessionBean().isSTC())
				{
					mailsCFSend = getMailsForSend(UserRoleTypes.STC, di
							.getDurInfoNumber().toString(),
							di.getDurCompilationDate(), ds.getTotalAmount());
				} 
				else if(this.getSessionBean().isUC())
				{
					mailsCFSend = getMailsForSend(UserRoleTypes.UC, di
							.getDurInfoNumber().toString(),
							di.getDurCompilationDate(), ds.getTotalAmount());
				}
			}
			else if (dc.getDurState().getId()
					.equals(DurStateTypes.STCEvaluation.getValue())
					&& (this.getSessionBean().isSTC() || this.getSessionBean()
							.isUC()))
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.AGUEvaluation);

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				for (CostDefinitions cd : listCD)
				{
					cd.setAguCertification(null);
					BeansFactory.CostDefinitions().Save(cd);
				}
				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
						dc.getId());

				BeansFactory.DurInfos().Save(di);

				if (this.getSessionBean().isSTC())
				{
					mailsSTCApprove = getMailsForSend(UserRoleTypes.AGU, di
							.getDurInfoNumber().toString(),
							di.getDurCompilationDate(), ds.getTotalAmount());

				} 
				else if(this.getSessionBean().isSTC())
				{
					mailsUCApprove = getMailsForSend(UserRoleTypes.AGU, di
							.getDurInfoNumber().toString(),
							di.getDurCompilationDate(), ds.getTotalAmount());
				}
			}
			else if (dc.getDurState().getId()
					.equals(DurStateTypes.AGUEvaluation.getValue())
					&& this.getSessionBean().isAGU())
			{
//				DurSummaries ds = changeStateAndRecalculateSum(dc,
//						DurStateTypes.ACUEvaluation);
				DurSummaries ds = changeStateAndRecalculateSum(dc, DurStateTypes.SendToExpenditureDeclaration);

				dc.setReimbursement(true);
				dc.setReimbursed(false);

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				for (CostDefinitions cd : listCD)
				{
					cd.setAcuCertification(null);
					BeansFactory.CostDefinitions().Save(cd);
				}

				DURCompilationWrapper previousDUR = null;

				for (int i = 0; (i + 1) < this.getListDurs().size(); ++i)
				{
					if (dc.getId().equals(this.getListDurs().get(i).getId()))
					{
						previousDUR = this.getListDurs().get(i + 1);

						break;
					}
				}

				if (previousDUR != null)
				{
					List<CostDefinitions> listPrevCD = BeansFactory
							.CostDefinitions()
							.GetByDurCompilation(previousDUR.getId());

					if (listPrevCD != null)
					{
						Double outstandingAmount = 0.0;
						Double amountWithdrawn = 0.0;
						
						for(CostDefinitions cd : listPrevCD)
						{
							if (cd.getValueSuspendACU() != null
									&& !CostDefinitionSuspendStatus.CANCEL_SUSPEND
											.getState()
											.equals(cd.getSuspensionStatus()))
							{
								outstandingAmount += cd.getValueSuspendACU();
								cd.setConsiderInDUR(Boolean.TRUE);
								BeansFactory.CostDefinitions().Save(cd);
							}
							
							if(cd.getAmountRetreat() != null&& !CostDefinitionSuspendStatus.CANCEL_SUSPEND
									.getState()
									.equals(cd.getSuspensionStatus()))
							{
								amountWithdrawn += cd.getAmountRetreat();
							}
						}
						
						if (!outstandingAmount.equals(0.0))
						{
							dc.setOutstandingAmount(outstandingAmount);
						}
						
						if(!amountWithdrawn.equals(0.0))
						{
							dc.setAmountWithdrawn(amountWithdrawn);
						}
					}
					
					if (previousDUR.getOutstandingAmount() != null)
					{
						DURCompilationWrapper durForCancelCase = null;

						for (int i = 0; (i + 2) < this.getListDurs()
								.size(); ++i)
						{
							if (previousDUR.getId()
									.equals(this.getListDurs().get(i).getId()))
							{
								durForCancelCase = this.getListDurs()
										.get(i + 1);

								break;
							}
						}

						if (durForCancelCase != null)
						{
							List<CostDefinitions> listCancelCD = BeansFactory
									.CostDefinitions().GetByDurCompilation(
											durForCancelCase.getId());

							if (listCancelCD != null)
							{
								Double cancelAmount = 0.0;

								for (CostDefinitions cd : listCancelCD)
								{
									if (cd.getValueSuspendACU() != null
											&& CostDefinitionSuspendStatus.CANCEL_SUSPEND
													.getState().equals(
															cd.getSuspensionStatus())
											&& Boolean.TRUE.equals(
													cd.getConsiderInDUR()))
									{
										cancelAmount += cd.getValueSuspendACU();
									}
								}

								if (!cancelAmount.equals(0.0))
								{
									dc.setOutstandingAmountUndo(cancelAmount);
								}
							}
						}
					}
				}

				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
						dc.getId());

				BeansFactory.DurInfos().Save(di);

				mailsAGUApprove = getMailsForSend(UserRoleTypes.ACU, di
						.getDurInfoNumber().toString(),
						di.getDurCompilationDate(), ds.getTotalAmount());
			}
			else if (dc.getDurState().getId()
					.equals(DurStateTypes.ACUEvaluation.getValue())
					&& this.getSessionBean().isACU())
			{
				DurSummaries ds = changeStateAndRecalculateSum(dc,
						DurStateTypes.Certifiable);
				dc.setRefusedByAcuManager(false);

				List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
						.GetByDurCompilation(dc.getId());

				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
						dc.getId());

				BeansFactory.DurInfos().Save(di);

				mailsACUApprove = getMailsForSend(UserRoleTypes.AGU, di
						.getDurInfoNumber().toString(),
						di.getDurCompilationDate(), ds.getTotalAmount());

				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.STC))
				{
					InfoObject ob = new InfoObject();
					ob.setProjectName(this.getProject().getTitle());
					ob.setNumber(di.getDurInfoNumber().toString());
					ob.setDurCompilationDate(BaseConverter
							.convertToDateString(di.getDurCompilationDate()));
					ob.setTotalAmountOfDUR(BaseConverter
							.convertToDoubleString(ds.getTotalAmount()));
					ob.setCertifyDate(BaseConverter.convertToDateString(listCD
							.get(0).getAcuCertDate()));
					ob.setMail(item.getMail());
					mailsACUApprove.add(ob);
				}

				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setNumber(di.getDurInfoNumber().toString());
				ob.setDurCompilationDate(BaseConverter.convertToDateString(di
						.getDurCompilationDate()));
				ob.setTotalAmountOfDUR(BaseConverter.convertToDoubleString(ds
						.getTotalAmount()));
				ob.setCertifyDate(BaseConverter.convertToDateString(listCD.get(
						0).getAcuCertDate()));
				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());

					mailsACUApprove.add(ob);
				}
			}

			BeansFactory.DurCompilations().Save(dc);
			//
			Transaction tr = null;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();
				List<Mails> mails = new ArrayList<Mails>();
				ManagmentDurCFSendMailSender managmentDurCFSendMailSender = new ManagmentDurCFSendMailSender(
						mailsCFSend);
				mails.addAll(managmentDurCFSendMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));

				ManagmentDurACUAcceptMailSender managmentDurACUAcceptMailSender = new ManagmentDurACUAcceptMailSender(
						mailsACUApprove);
				mails.addAll(managmentDurACUAcceptMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
				ManagmentDurAGUAcceptMailSender managmentDurAGUAcceptMailSender = new ManagmentDurAGUAcceptMailSender(
						mailsAGUApprove);
				mails.addAll(managmentDurAGUAcceptMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
				ManagmentDurCFSendMailSender managmentDurCFSendMailSender2 = new ManagmentDurCFSendMailSender(
						mailsSTCApprove);
				mails.addAll(managmentDurCFSendMailSender2
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
				ManagmentDurSTCApproveMailSender managmentDurSTCApproveMailSender = new ManagmentDurSTCApproveMailSender(
						mailsSTCApprove);
				mails.addAll(managmentDurSTCApproveMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));

				ManagmentDurUCApproveMailSender managmentDurUCApproveMailSender = new ManagmentDurUCApproveMailSender(
						mailsUCApprove);
				mails.addAll(managmentDurUCApproveMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
				
				ManagmentDurCFSendMailSender managmentDurCFSendMailSender3 = new ManagmentDurCFSendMailSender(
						mailsUCApprove);
				mails.addAll(managmentDurCFSendMailSender3
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));

				if (mails != null && !mails.isEmpty())
				{
					for (Mails mail : mails)
					{
						BeansFactory.Mails().SaveInTransaction(mail);
					}

				}

			}
			catch (Exception e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
			}
			finally
			{
				if (tr != null && !tr.wasRolledBack() && tr.isActive())
				{
					tr.commit();

				}
			}

			if (!sent)
			{
				try
				{
					ActivityLog.getInstance().addExtendedActivity(
							Utils.getString("extendedActLogDurListSend"),
							"",
							Long.parseLong(dc.getDurNumberTransient()
									.toString()));
				}
				catch (Exception e)
				{
					log.error(e);
				}
			}
			else
			{
				try
				{
					ActivityLog.getInstance().addExtendedActivity(
							Utils.getString("extendedActLogDurListSendVF"),
							"",
							Long.parseLong(dc.getDurNumberTransient()
									.toString()));
				}
				catch (Exception e)
				{
					log.error(e);
				}
			}
			//
			// new ManagmentDurCFSendMailSender(mailsCFSend).start();
			// new ManagmentDurACUAcceptMailSender(mailsACUApprove).start();
			// new ManagmentDurAGUAcceptMailSender(mailsAGUApprove).start();
			// new ManagmentDurCFSendMailSender(mailsSTCApprove).start();
			// new ManagmentDurSTCApproveMailSender(mailsSTCApprove).start();
			this.Page_Load();
		}
	}

	public void denyItem() throws PersistenceBeanException
	{
		DurCompilations dc = BeansFactory.DurCompilations().Load(
				this.getEntityDenyId());

		if (dc != null && !dc.getDeleted())
		{
			List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
					.GetByDurCompilation(this.getEntityDenyId());

			for (CostDefinitions cd : listCD)
			{
				cd.setCostState(BeansFactory.CostStates().Load(
						CostStateTypes.CFValidate.getState()));
				cd.setDurCompilation(null);
				cd.setAcuCertification(null);
				cd.setAguCertification(null);
				cd.setStcCertification(null);
				cd.setAcuCertDate(null);
				cd.setStcCertDate(null);
				cd.setAguCertDate(null);
				cd.setSTCSertified(false);
				cd.setAGUSertified(false);
				cd.setACUSertified(false);
				cd.setRefucedFromDur(true);
				BeansFactory.CostDefinitions().Save(cd);
			}

			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
					dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(
					dc.getId());
			if (this.getSessionBean().isSTC())
			{

				mailsSTCRefuse = new ArrayList<InfoObject>();
				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.AGU))
				{
					InfoObject ob = new InfoObject();
					ob.setProjectName(this.getProject().getTitle());
					ob.setNumber(di.getDurInfoNumber().toString());
					ob.setDurCompilationDate(BaseConverter
							.convertToDateString(di.getDurCompilationDate()));
					ob.setTotalAmountOfDUR(BaseConverter
							.convertToDoubleString(ds.getTotalAmount()));

					ob.setMail(item.getMail());
					mailsSTCRefuse.add(ob);
				}

				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setNumber(di.getDurInfoNumber().toString());
				ob.setDurCompilationDate(BaseConverter.convertToDateString(di
						.getDurCompilationDate()));
				ob.setTotalAmountOfDUR(BaseConverter.convertToDoubleString(ds
						.getTotalAmount()));

				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());

					mailsSTCRefuse.add(ob);
				}
			}

			if (this.getSessionBean().isUC())
			{

				mailsUCRefuse = new ArrayList<InfoObject>();
				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.AGU))
				{
					InfoObject ob = new InfoObject();
					ob.setProjectName(this.getProject().getTitle());
					ob.setNumber(di.getDurInfoNumber().toString());
					ob.setDurCompilationDate(BaseConverter
							.convertToDateString(di.getDurCompilationDate()));
					ob.setTotalAmountOfDUR(BaseConverter
							.convertToDoubleString(ds.getTotalAmount()));

					ob.setMail(item.getMail());
					mailsUCRefuse.add(ob);
				}

				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setNumber(di.getDurInfoNumber().toString());
				ob.setDurCompilationDate(BaseConverter.convertToDateString(di
						.getDurCompilationDate()));
				ob.setTotalAmountOfDUR(BaseConverter.convertToDoubleString(ds
						.getTotalAmount()));

				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());

					mailsUCRefuse.add(ob);
				}
			}

			BeansFactory.DurInfos().Delete(di);

			BeansFactory.DurSummaries().Delete(ds);

			BeansFactory.DurCompilations().Delete(dc);

			try
			{
				ActivityLog.getInstance().addExtendedActivity(
						Utils.getString("extendedActLogDurListDeny"), "",
						Long.parseLong(dc.getDurNumberTransient().toString()));
			}
			catch (Exception e)
			{
				log.error(e);
			}
		}

		// new ManagmentDurSTCRefuseMailSender(mailsSTCRefuse).start();
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();

			if (this.getSessionBean().isSTC())
			{
				ManagmentDurSTCRefuseMailSender managmentDurSTCRefuseMailSender = new ManagmentDurSTCRefuseMailSender(
						mailsCFSend);
				mails.addAll(managmentDurSTCRefuseMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
			}
			else if (this.getSessionBean().isUC())
			{
				ManagmentDurUCRefuseMailSender managmentDurUCRefuseMailSender = new ManagmentDurUCRefuseMailSender(
						mailsCFSend);
				mails.addAll(managmentDurUCRefuseMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));
			}

			if (mails != null && !mails.isEmpty())
			{
				for (Mails mail : mails)
				{
					BeansFactory.Mails().SaveInTransaction(mail);
				}

			}

		}
		catch (Exception e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
			}
		}

		this.Page_Load();
	}

	public void showItem() throws HibernateException, PersistenceBeanException
	{
		DurCompilations dc = BeansFactory.DurCompilations().Load(
				this.getEntityEditId());
		if (dc != null && !dc.getDeleted())
		{
			Boolean canEdit = false;
			for (DURCompilationWrapper dr : this.getListDurs())
			{
				if (dr.getId().equals(this.getEntityEditId()))
				{
					canEdit = dr.isEditAvailable();
					break;
				}
			}
			this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW,
					false);
			this.getSession().put(DURCompilationEditBean.FILTER_PARTNER_VALUE,
					null);
			this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE,
					null);
			this.getSession().put(
					DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE, null);
			this.getSession()
					.put(DURCompilationEditBean.FILTER_COST_ITEM, null);
			this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW,
					canEdit);

			this.getSession().put("durCompilationEdit", this.getEntityEditId());
			this.getSession().put("durCompilationEditShow", true);
			this.getSession().put("durCompilationEditBack", true);

			this.goTo(PagesTypes.DURCOMPILATIONEDIT);
		}
	}

	/**
	 * Sets entitySendId
	 * 
	 * @param entitySendId
	 *            the entitySendId to set
	 */
	public void setEntitySendId(Long entitySendIdVal)
	{
		entitySendId = entitySendIdVal;
	}

	/**
	 * Gets entitySendId
	 * 
	 * @return entitySendId the entitySendId
	 */
	public Long getEntitySendId()
	{
		return entitySendId;
	}

	/**
	 * Sets entityDenyId
	 * 
	 * @param entityDenyId
	 *            the entityDenyId to set
	 */
	public void setEntityDenyId(Long entityDenyIdVal)
	{
		entityDenyId = entityDenyIdVal;
	}

	/**
	 * Gets entityDenyId
	 * 
	 * @return entityDenyId the entityDenyId
	 */
	public Long getEntityDenyId()
	{
		return entityDenyId;
	}

	/**
	 * Sets currentRoleType
	 * 
	 * @param currentRoleType
	 *            the currentRoleType to set
	 */
	public void setCurrentRoleType(RoleTypes currentRoleType)
	{
		this.getViewState().put("durCompilationUserRole", currentRoleType);
	}

	/**
	 * Gets currentRoleType
	 * 
	 * @return currentRoleType the currentRoleType
	 */
	public RoleTypes getCurrentRoleType()
	{
		if (this.getViewState().get("durCompilationUserRole") != null)
		{
			return RoleTypes.valueOf(String.valueOf(this.getViewState().get(
					"durCompilationUserRole")));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets listDurs
	 * 
	 * @param listDurs
	 *            the listDurs to set
	 */
	public void setListDurs(List<DURCompilationWrapper> listDurs)
	{
		this.listDurs = listDurs;
	}

	/**
	 * Gets listDurs
	 * 
	 * @return listDurs the listDurs
	 */
	public List<DURCompilationWrapper> getListDurs()
	{
		return listDurs;
	}

	/**
	 * Sets filter
	 * 
	 * @param filter
	 *            the filter to set
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 */
	public void setFilter(String filter) throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		this.getViewState().put("durCompilationListFilter", filter);
		this.Page_Load();
	}

	/**
	 * Gets filter
	 * 
	 * @return filter the filter
	 */
	public String getFilter()
	{
		if (this.getViewState().get("durCompilationListFilter") != null)
		{
			return String.valueOf(this.getViewState().get(
					"durCompilationListFilter"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets listFilter
	 * 
	 * @param listFilter
	 *            the listFilter to set
	 */
	public void setListFilter(List<SelectItem> listFilter)
	{
		this.listFilter = listFilter;
	}

	/**
	 * Gets listFilter
	 * 
	 * @return listFilter the listFilter
	 */
	public List<SelectItem> getListFilter()
	{
		return listFilter;
	}

	/**
	 * Gets showAttention
	 * 
	 * @return showAttention the showAttention
	 */
	public Map<String, String> getShowAttention()
	{
		return showAttention;
	}

	/**
	 * Sets canAdd
	 * 
	 * @param canAdd
	 *            the canAdd to set
	 */
	public void setCanAdd(boolean canAdd)
	{
		this.canAdd = canAdd;
	}

	/**
	 * Gets canAdd
	 * 
	 * @return canAdd the canAdd
	 */
	public boolean getCanAdd()
	{
		return canAdd;
	}

	public List<SelectItem> getFilterDurStatusValues()
	{
		return filterDurStatusValues;
	}

	public void setFilterDurStatusValues(List<SelectItem> filterDurStatusValues)
	{
		this.filterDurStatusValues = filterDurStatusValues;
	}

	public void setDurFilterValidationStatus(String durFilterValidationStatus)
	{
		this.getSession().put("durFilterValidationStatus",
				durFilterValidationStatus);
	}

	public String getDurFilterValidationStatus()
	{
		return (String) this.getSession().get("durFilterValidationStatus");
	}

	public void setDurFilterCostItem(String durFilterCostItem)
	{
		this.getSession().put("durFilterCostItem", durFilterCostItem);
	}

	public String getDurFilterCostItem()
	{
		return (String) this.getSession().get("durFilterCostItem");
	}
	
	public void setFilterPhaseId(String filterPhaseId)
	{
		this.getSession().put("filterPhaseId", filterPhaseId);
	}

	public String getFilterLocationId()
	{
		return (String) this.getSession().get("filterLocationId");
	}
	
	public void setFilterLocationId(String filterLocationId)
	{
		this.getSession().put("filterLocationId", filterLocationId);
	}

	public String getFilterPhaseId()
	{
		return (String) this.getSession().get("filterPhaseId");
	}

	public String getNumberPayment()
	{
		return numberPayment;
	}

	public void setNumberPayment(String numberPayment)
	{
		this.numberPayment = numberPayment;
	}

	public String getPartner()
	{
		return partner;
	}

	public void setPartner(String partner)
	{
		this.partner = partner;
	}

	public String getIdPartner()
	{
		return idPartner;
	}

	public void setIdPartner(String idPartner)
	{
		this.idPartner = idPartner;
	}

	public List<SelectItem> getFilterDurCostItem()
	{
		return filterDurCostItem;
	}

	public void setFilterDurCostItem(List<SelectItem> filterDurCostItem)
	{
		this.filterDurCostItem = filterDurCostItem;
	}

	public boolean getFilterCertifiedDUR()
	{
		return filterCertifiedDUR;
	}

	public void setFilterCertifiedDUR(boolean filterCertifiedDUR)
	{
		this.filterCertifiedDUR = filterCertifiedDUR;
	}

	public HtmlDataScroller getScroller()
	{
		return scroller;
	}

	public void setScroller(HtmlDataScroller scroller)
	{
		this.scroller = scroller;
	}

	public String getDurReimbNumber()
	{
		return durReimbNumber;
	}

	public void setDurReimbNumber(String durReimbNumber)
	{
		this.durReimbNumber = durReimbNumber;
	}

	/**
	 * Class wrapper for DURCompilations
	 * 
	 * @author Sergey Zorin InfoStroy Co., 2010.
	 * 
	 */
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
		DurCompilations dc = null;
		try
		{
			dc = BeansFactory.DurCompilations().Load(this.getEntityEditId());
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		if (dc != null)
		{
			dc.setLocked(lock);
			for (DURCompilationWrapper item : this.getListDurs())
			{
				if (this.getEntityEditId() == item.getId())
				{
					item.setLocked(lock);
					break;
				}
			}
			try
			{
				BeansFactory.DurCompilations().Save(dc);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
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

	public List<SelectItem> getFilterPhase()
	{
		return filterPhase;
	}

	public void setFilterPhase(List<SelectItem> filterPhase)
	{
		this.filterPhase = filterPhase;
	}

	public List<SelectItem> getFilterLocation()
	{
		return filterLocation;
	}

	public void setFilterLocation(List<SelectItem> filterLocation)
	{
		this.filterLocation = filterLocation;
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
