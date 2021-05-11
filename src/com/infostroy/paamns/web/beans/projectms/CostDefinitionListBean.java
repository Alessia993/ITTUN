/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CertificatedByType;
import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.SuperAdminHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitionsToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CostDefinitionListBean extends
		EntityProjectListBean<CostDefinitions>
{

	public static String	SAVED_PAGE_INDEX			= "SAVED_PAGE_INDEX";

	public static String	SAVED_CDL_ITEMS_PER_PAGE	= "SAVED_CDL_ITEMS_PER_PAGE";

	public static String	CLONE_CD_ID					= "CLONE_CD_ID";

	public enum FilterValues
	{
		All("1"), Validate("2"), ToMe("3"), ToOthers("4"), CanBeSendToAdaUser(
				"5");

		FilterValues(String i)
		{
			this.value = i;
		}

		public static FilterValues getByValue(String value)
		{
			for (FilterValues item : FilterValues.values())
			{
				if (item.getValue().equals(value))
				{
					return item;
				}
			}

			return null;
		}

		/**
		 * Sets value
		 * 
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value)
		{
			this.value = value;
		}

		/**
		 * Gets value
		 * 
		 * @return value the value
		 */
		public String getValue()
		{
			return value;
		}

		private String	value;
	}

	private boolean					selectAll;

	private boolean					canSendToAda;

	private String					filterValue;

	private static List<SelectItem>	costStates;

	private String					errorMessage;

	// private Date documentDate;

	

	// private String partnerID;

	// private String costDefID;

	// private String durNumber;

	private HtmlDataScroller		scroller;

	private String					actionMotivation;

	private String					certificatedByTypeFiltered;
	
	private String 					editPage;
	
	private final String			THIS_PAGE_NAME ="CostDefinitionList.jsf";
	
	private final String			EDIT_PAGE_NAME = "CostDefinitionEdit.jsf";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		this.setEditPage(
				((HttpServletRequest) FacesContext.getCurrentInstance()
						.getExternalContext().getRequest()).getRequestURI()
								.replace(THIS_PAGE_NAME, EDIT_PAGE_NAME));
		
		if(getNeedReload())
		{
			setDisplaySelectAll(false);
			if (this.getFilterValue() == null
					|| String.valueOf(this.getFilterValue()).isEmpty())
			{
				setFilterValue(FilterValues.All.getValue());
				return;
			}

			resetParams();

			List<CostDefinitions> tempList = new ArrayList<CostDefinitions>();
			boolean isOneConditionFired = false;
			if (this.getCurrentUser().getAdmin())
			{
				for (CostDefinitions cost : BeansFactory.CostDefinitions()
						.GetByProject(this.getProjectId()))
				{
					if (!tempList.contains(cost))
					{
						tempList.add(cost);
					}
				}

				isOneConditionFired = true;
			}
			if (this.getSessionBean().isCF())
			{
				if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.All)
				{

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByCFWithPartnerAndBudgedOwner(
									this.getProjectId(),
									this.getCurrentUser().getId()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.Validate)
				{
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartnerAndBudgedOwner(this.getProjectId(),
									this.getCurrentUser().getId(),
									CostStateTypes.FullValidate.getState()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.ToMe)
				{
					Long[] ids = new Long[4];
					ids[0] = CostStateTypes.CFValidate.getState();
					ids[1] = CostStateTypes.CFValidation.getState();
					ids[2] = CostStateTypes.CILValidate.getState();
					ids[3] = CostStateTypes.NotYetValidate.getState();

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByCFWithPartner(this.getProjectId(),
									this.getCurrentUser().getId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.ToOthers)
				{
					Long[] ids = new Long[6];

					ids[0] = CostStateTypes.CILVerify.getState();
					ids[1] = CostStateTypes.DAECApproval.getState();
					ids[2] = CostStateTypes.DAECVerify.getState();
					ids[3] = CostStateTypes.FullValidate.getState();
					ids[4] = CostStateTypes.RefusedCIL.getState();
					ids[5] = CostStateTypes.RefusedDAEC.getState();

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartner(this.getProjectId(),
									this.getCurrentUser().getId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				isOneConditionFired = true;
			}
			if (this.getSessionBean().isAGU())
			{
				if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.All)
				{
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetAgusWithPartners(this.getProjectId()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.Validate)
				{
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetAgus(this.getProjectId(),
									CostStateTypes.FullValidate))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.ToMe)
				{
					Long[] ids = new Long[3];
					ids[0] = CostStateTypes.NotYetValidate.getState();
					ids[1] = CostStateTypes.CILValidate.getState();
					ids[2] = CostStateTypes.CFValidation.getState();
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetAgusWithPartners(this.getProjectId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.ToOthers)
				{
					Long[] ids = new Long[7];

					ids[0] = CostStateTypes.CILVerify.getState();
					ids[1] = CostStateTypes.DAECApproval.getState();
					ids[2] = CostStateTypes.DAECVerify.getState();
					ids[3] = CostStateTypes.FullValidate.getState();
					ids[4] = CostStateTypes.RefusedCIL.getState();
					ids[5] = CostStateTypes.RefusedDAEC.getState();
					ids[6] = CostStateTypes.CFValidate.getState();

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetAgus(this.getProjectId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				isOneConditionFired = true;
			}
			if (this.getSessionBean().isPartner())
			{
				if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.All)
				{
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartnerAndBudgetOwner(this.getProjectId(),
									this.getCurrentUser().getId()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.Validate)
				{

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartnerAndBudgetOwner(this.getProjectId(),
									this.getCurrentUser().getId(),
									CostStateTypes.FullValidate.getState()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues
						.getByValue(this.getFilterValue()) == FilterValues.ToMe)
				{
					Long[] ids = new Long[3];
					ids[0] = CostStateTypes.NotYetValidate.getState();
					ids[1] = CostStateTypes.CILValidate.getState();
					ids[2] = CostStateTypes.RefusedCIL.getState();
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartner(this.getProjectId(),
									this.getCurrentUser().getId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else if (FilterValues.getByValue(
						this.getFilterValue()) == FilterValues.ToOthers)
				{
					Long[] ids = new Long[8];

					ids[0] = CostStateTypes.CILVerify.getState();
					ids[1] = CostStateTypes.DAECApproval.getState();
					ids[2] = CostStateTypes.DAECVerify.getState();
					ids[3] = CostStateTypes.FullValidate.getState();
					ids[4] = CostStateTypes.RefusedCIL.getState();
					ids[5] = CostStateTypes.CFValidate.getState();
					ids[6] = CostStateTypes.CFValidation.getState();
					ids[7] = CostStateTypes.RefusedDAEC.getState();

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByPartner(this.getProjectId(),
									this.getCurrentUser().getId(), ids))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				isOneConditionFired = true;
			}

			if (!isOneConditionFired || this.getSessionBean().isSTC())
			{
				if (this.getSessionBean().isAAU())
				{
					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetForAAU(this.getProjectId()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
				else
				{

					for (CostDefinitions cost : BeansFactory.CostDefinitions()
							.GetByProject(this.getProjectId()))
					{
						if (!tempList.contains(cost))
						{
							tempList.add(cost);
						}
					}
				}
			}
			Collections.sort(tempList, new Comparator<CostDefinitions>()
			{
				public int compare(CostDefinitions c1, CostDefinitions c2)
				{
                    if(c1 == null || c1.getCreateDate() == null){
                        return 1;
                    }
                    if(c2 == null || c2.getCreateDate() == null){
                        return -1;
                    }
                    return (c2.getCreateDate().compareTo(c1.getCreateDate()));
				}
			});
			this.setCostDefinitionsList(tempList);

			for (CostDefinitions cd : this.getCostDefinitionsList())
			{
				cd.setSelected(this.getIds().contains(cd.getId()));

				if (cd.getCostState().getId()
						.equals(CostStateTypes.CFValidation.getState())
						&& (this.getSessionBean().isCF() || (this
								.getSessionBean().isAGU()
								&& !this.getSessionBean().getIsAguRead())))
				{
					this.setCanSendToDur(true);
					cd.setCfCheck(cd.getCilCheck());
					cd.setCfCheckPublicAmount(cd.getCilCheckPublicAmount());
					cd.setCfCheckPrivateAmount(cd.getCilCheckPrivateAmount());
					cd.setCfCheckAdditionalFinansingAmount(cd.getCilCheckAdditionalFinansingAmount());
					cd.setCfCheckStateAidAmount(cd.getCilCheckStateAidAmount());
					cd.setCfCheckOutsideEligibleAreas(cd.getCilCheckOutsideEligibleAreas());
					cd.setCfCheckInkindContributions(cd.getCilCheckInkindContributions());
				}

				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.GetByUser(cd.getUser().getId());

				cd.setUserInsertedName(partner != null ? partner.getName()
						: cd.getUser().getName() + " "
								+ cd.getUser().getSurname());

			}

			if (FilterValues.getByValue(
					this.getFilterValue()) == FilterValues.CanBeSendToAdaUser)
			{
				List<CostDefinitions> canBeSendToAda = new ArrayList<CostDefinitions>();
				for (CostDefinitions cd : BeansFactory.CostDefinitions()
						.GetAgusWithPartners(this.getProjectId()))
				{
					if ((cd.getIsSentToAAU() == null
							|| cd.getIsSentToAAU() == false)
							&& cd.getACUSertified())
					{
						canBeSendToAda.add(cd);

					}
				}
				this.setCostDefinitionsList(canBeSendToAda);
			}
			if (this.getSessionBean().isAAU())
			{
				for (CostDefinitions cd : this.getCostDefinitionsList())
				{
					if ((cd.getIsSentToAAU() == null
							|| cd.getIsSentToAAU() == false)
							&& cd.getACUSertified())
					{
						this.setCanSendToAda(true);
						break;
					}
				}
			}

			// search section
			List<CostDefinitions> resultList = new ArrayList<CostDefinitions>();

			CertificatedByType certificatedByType = null;

			if (this.getCertificatedByTypeFiltered() != null
					&& !this.getCertificatedByTypeFiltered().isEmpty())
			{
				for (CertificatedByType type : CertificatedByType.values())
				{
					if (type.name()
							.equals(this.getCertificatedByTypeFiltered()))
					{
						certificatedByType = type;
						break;
					}
				}
			}

			for (CostDefinitions cd : this.getCostDefinitionsList())
			{
				if (this.getFilterValidationStatus() != null
						&& !this.getFilterValidationStatus().isEmpty())
				{
					if (!cd.getCostStateId()
							.equals(getFilterValidationStatus()))
					{
						continue;
					}
				}
				if (this.getDurNumber() != null
						&& !this.getDurNumber().isEmpty())
				{
					if (cd.getDurCompilation() != null)
					{
						try
						{
							Long idDur = Long.parseLong(this.getDurNumber());
							DurInfos di = BeansFactory.DurInfos()
									.LoadByDurCompilation(
											cd.getDurCompilation().getId());
							if (di == null || di.getDurInfoNumber() == null)
							{
								continue;
							}
							Long infoNumber = Long.parseLong(
									di.getDurInfoNumber().toString());
							if (infoNumber == null || !infoNumber.equals(idDur))
							{
								continue;
							}
						}
						catch (Exception e)
						{
							continue;
						}
					}
					else
					{
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

				if (this.getDocumentDate() != null)
				{
					if (cd.getDocumentDate() == null)
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
					secondDate.setTime(cd.getDocumentDate());
					secondDate.clear(Calendar.HOUR);
					secondDate.clear(Calendar.MINUTE);
					secondDate.clear(Calendar.SECOND);
					secondDate.clear(Calendar.MILLISECOND);
					if (!firstDate.equals(secondDate))
					{
						continue;
					}
				}

				if (this.getPartnerID() != null
						&& !this.getPartnerID().isEmpty())
				{
					CFPartnerAnagraphs partner = BeansFactory
							.CFPartnerAnagraphs()
							.GetByUser(cd.getUser().getId());
					if (!partner.getProgressiveId()
							.equals(Long.parseLong(this.getPartnerID())))
					{
						continue;
					}

					/*
					 * if
					 * (!cd.getUser().equals(Long.parseLong(this.getPartnerID(
					 * )))) { continue; }
					 */
				}

				if (this.getReportNumber() != null
						&& !this.getReportNumber().isEmpty())
				{
					if (cd.getReportNumber() == null
							|| Integer.parseInt(this.getReportNumber()) != cd
									.getReportNumber().intValue())
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
							List<Long> ids = BeansFactory
									.ControllerUserAnagraph()
									.GetUsersIndicesByType(UserRoleTypes.AGU);
							if (ids != null
									&& ids.contains(cd.getUser().getId()))
							{
								skip = false;
							}

						}

						if (skip)
						{
							continue;
						}
					}
					else if (!cd.getUser().getId()
							.equals(Long.parseLong(this.getFilterPartner()))
							&& !cd.getBudgetOwner().getId().equals(
									Long.parseLong(this.getFilterPartner())))
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
								|| !cd.getCostItemPhaseAsse3()
										.getPhase()
										.name()
										.equals(this
												.getSelectedCostPhaseAsse3()))
						{
							continue;
						}

					}
					
					if (this.getSelectedCostItemAsse3() != null
							&& !this.getSelectedCostItemAsse3().isEmpty())
					{

						if (cd.getCostItemPhaseAsse3().getPhase() == null
								|| !cd.getCostItemPhaseAsse3()
										.getValue()
										.equals(this.getSelectedCostItemAsse3()))
						{
							continue;
						}

					}
				}
				else
				{
					if (this.getFilterCostItem() != null
							&& !this.getFilterCostItem().isEmpty())
					{
						if (cd.getCostItem() == null)
						{
							continue;
						}

						if (!cd.getCostItem()
								.getId()
								.equals(Long.parseLong(this.getFilterCostItem())))
						{
							continue;
						}
					}

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
				}
			

				if (this.getCostDefID() != null
						&& !this.getCostDefID().isEmpty())
				{
					if (!cd.getProgressiveId()
							.equals(Long.parseLong(this.getCostDefID())))
					{
						continue;
					}
				}



				if (certificatedByType != null)
				{
					if (CertificatedByType.CertificateSTC
							.equals(certificatedByType))
					{
						if (Boolean.TRUE.equals(cd.getSTCSertified()))
						{
							continue;
						}
					}
					else if (CertificatedByType.CertificateAGU
							.equals(certificatedByType))
					{
						if (!Boolean.TRUE.equals(cd.getSTCSertified()))
						{
							continue;
						}
						if (Boolean.TRUE.equals(cd.getAGUSertified()))
						{
							continue;
						}
					}
					else if (CertificatedByType.CertificateACU
							.equals(certificatedByType))
					{
						if (!Boolean.TRUE.equals(cd.getSTCSertified()))
						{
							continue;
						}
						if (!Boolean.TRUE.equals(cd.getAGUSertified()))
						{
							continue;
						}
						if (Boolean.TRUE.equals(cd.getACUSertified()))
						{
							continue;
						}
					}
				}
				
				if (!getDisplaySelectAll() && (("6".equals(cd.getCostStateId())
						&& !Boolean.TRUE.equals(cd.getLocked()))
						|| ((cd.getIsSentToAAU() == null
								|| !cd.getIsSentToAAU()) && getCanSendToAda())))
				{
					setDisplaySelectAll(true);
				}

				resultList.add(cd);
			}
			this.setCostDefinitionsList(resultList);
			this.setList(getCostDefinitionsList());

			ReRenderScroll();

			if (this.getSession().containsKey(SAVED_PAGE_INDEX))
			{
				Integer itemsPerPageSaved = 10;
				if (this.getSession().containsKey(SAVED_CDL_ITEMS_PER_PAGE))
				{
					setItemsPerPage((String) this.getSession()
							.get(SAVED_CDL_ITEMS_PER_PAGE));

					try
					{
						itemsPerPageSaved = Integer.parseInt(getItemsPerPage());
					}
					catch (Exception e)
					{
						log.error("CostDefinitionListBean exception:", e);
					}

					this.getSession().remove(SAVED_CDL_ITEMS_PER_PAGE);
				}
				setTableFirstRow(
						((Integer) this.getSession().get(SAVED_PAGE_INDEX) - 1)
								* itemsPerPageSaved);
				this.getSession().remove(SAVED_PAGE_INDEX);
			}

			setNeedReload(false);
		}
		else
		{
			this.setList(getCostDefinitionsList());
		}
	}
	
	public void resetParams(){
		this.getSession().remove("costDefinitionEdit");
		this.getSession().remove("costDefinitionView");
		this.getSession().remove("CLONE_CD_ID");
	}
	
	
	public void doSendToAda()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			for (CostDefinitions item : getCostDefinitionsList())
			{
				if (item.getSelected())
				{
					if ((this.getSessionBean().isAAU() && (item.getIsSentToAAU() == null || item.getIsSentToAAU() == false)) 
							&& this.getSessionBean().isAAU() 
							&& item.getACUSertified())
					{
						item.setIsSentToAAU(true);
						PersistenceSessionManager.getBean().getSession()
								.save(item);
					}
				}
			}
			this.getViewState().put("selectedToAda", false);
			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (PersistenceBeanException e1)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			e1.printStackTrace();
		}
		finally
		{
			if (tr != null)
			{
				tr.commit();
			}
		}
	}

	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getCostDefinitionsList(),
					ExportPlaces.CostDefinitions);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportCostDefinitions")
					+ ".xls", is, data.length);
		}
		catch (Exception e)
		{
			log.error("CostDefinitionListBean exception:",e);
		}
	}

	public void search()
	{
		try
		{
			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (Exception e)
		{
		}
		this.setTableFirstRow(0);
		this.ReRenderScroll();
	}

	public void clear()
	{
		try
		{
			this.setFilterValidationStatus(null);
			this.setDurNumber(null);
			this.setDocumentDate(null);
			this.setPartnerID(null);
			this.setReportNumber(null);
			this.setFilterPartner(null);
			this.setFilterCostItem(null);
			this.setCostDefID(null);
			this.setFilterPhase(null);
			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (Exception e)
		{
		}
		this.setTableFirstRow(0);
		this.ReRenderScroll();
	}

	public void fillDropDown() throws HibernateException,
			PersistenceBeanException
	{
		this.setFilterValidationStatusValues(new ArrayList<SelectItem>());
		this.getFilterValidationStatusValues().add(SelectItemHelper.getAllElement());
		for (CostStates item : BeansFactory.CostStates().Load())
		{
			if (item.getId().equals(CostStateTypes.CILVerify.getState())
					|| item.getId()
							.equals(CostStateTypes.CFValidate.getState())
					|| item.getId().equals(
							CostStateTypes.CILValidate.getState())
					|| item.getId().equals(
							CostStateTypes.CFValidation.getState())
					|| item.getId().equals(
							CostStateTypes.FullValidate.getState())
					|| item.getId().equals(
							CostStateTypes.NotYetValidate.getState())
					|| item.getId()
							.equals(CostStateTypes.RefusedCIL.getState())
							|| item.getId().equals(CostStateTypes.Draft.getState()))
			{
				this.getFilterValidationStatusValues().add(new SelectItem(item
						.getId().toString(), item.getValue()));
			}
		}

		
		this.setFilterPartnerValues(new ArrayList<SelectItem>());
		this.getFilterPartnerValues().add(SelectItemHelper.getAllElement());

		/*
		 * List<CFPartnerAnagraphs> listPart = BeansFactory.CFPartnerAnagraphs()
		 * .GetCFAnagraphForProject(this.getProjectId(),
		 * CFAnagraphTypes.PartnerAnagraph);
		 */
		List<CFPartnerAnagraphs> listPart = BeansFactory.CFPartnerAnagraphs()
				.LoadByProject(this.getProjectId());
		for (CFPartnerAnagraphs partner : listPart)
		{
			if (partner.getUser() == null)
			{
				if (this.getProject().getAsse() == 5)
				{
					this.getFilterPartnerValues().add(new SelectItem("-1", partner
							.getNaming()));
				}
			}
			else
			{
				this.getFilterPartnerValues().add(new SelectItem(String
						.valueOf(partner.getUser().getId()), partner
						.getNaming()));
			}
		}
		if (Boolean.TRUE.equals(this.getAsse3Mode()))
		{
			FillAsse3Phases();
			FillCostItemsAsse3();
		}
		else
		{
			this.setFilterCostItemValues(new ArrayList<SelectItem>());
			this.getFilterCostItemValues().add(SelectItemHelper.getAllElement());
			for (CostItems item : BeansFactory.CostItems().Load())
			{
				getFilterCostItemValues()
						.add(new SelectItem(item.getId().toString(), item
								.getValue()));
			}

			fillPhases();
		}

		fillCertificatedByFilter();
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
	
	private void fillCertificatedByFilter()
	{
		this.setFilterCertificatedBy(new ArrayList<SelectItem>());
		this.getFilterCertificatedBy().add(SelectItemHelper.getAllElement());
		for (CertificatedByType type : CertificatedByType.values())
		{
			this.getFilterCertificatedBy().add(
					new SelectItem(type.name(), type.toString()));
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
			getFilterPhases()
					.add(new SelectItem(item.getId(), item.getValue()));
		}
	}

	public String movePrevStep()
	{
		try
		{
			if (SuperAdminHelper.moveCostDefinitionPrevStep(
					this.getEntitySendId(), this.getActionMotivation(),
					this.getCurrentUser(), this.getSessionBean().getProject(),
					"costDefinition"))
			{
				this.setNeedReload(true);
				this.Page_Load();
				this.setActionMotivation("");
			}
		}
		catch (Exception e)
		{
			log.error("CostDefinitionListBean exception:",e);
		}
		return null;
	}
	
	public void asse3PhaseChange(ValueChangeEvent event) throws PersistenceBeanException{
		this.setSelectedCostPhaseAsse3((String)event.getNewValue());
		this.FillCostItemsAsse3();
	}
	
	public void sendToDur()
	{
		try
		{
			Transaction tr = null;

			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();

			for (CostDefinitions item : getCostDefinitionsList())
			{
				if (item.getSelected())
				{
					if (item.getCostState().getId()
							.equals(CostStateTypes.CFValidation.getState())
							&& (this.getSessionBean().isCF() || (this
									.getSessionBean().isAGU() && !this
									.getSessionBean().getIsAguRead())))
					{

						
						
						if((item.getCilCheckPrivateAmount() == null ? 0d : item
								.getCilCheckPrivateAmount()) < (item.getCfCheckPrivateAmount() == null ? 0d
								: item.getCfCheckPrivateAmount())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessPrivate"));
							return;
						}
						
						if((item.getCilCheckPublicAmount() == null ? 0d : item
								.getCilCheckPublicAmount()) < (item.getCfCheckPublicAmount() == null ? 0d
								: item.getCfCheckPublicAmount())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessPublic"));
							return;
						}
						
						if((item.getCilCheckStateAidAmount() == null ? 0d : item
								.getCilCheckStateAidAmount()) < (item.getCfCheckStateAidAmount() == null ? 0d
								: item.getCfCheckStateAidAmount())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessStateAid"));
							return;
						}
						
						if((item.getCilCheckAdditionalFinansingAmount() == null ? 0d : item
								.getCilCheckAdditionalFinansingAmount()) < (item.getCfCheckAdditionalFinansingAmount() == null ? 0d
								: item.getCfCheckAdditionalFinansingAmount())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessAdditionalFinansing"));
							return;
						}
						
						if((item.getCilCheckInkindContributions() == null ? 0d : item
								.getCilCheckInkindContributions()) < (item.getCfCheckInkindContributions() == null ? 0d
								: item.getCfCheckInkindContributions())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessInkindContributions"));
							return;
						}
						
						if((item.getCilCheckOutsideEligibleAreas() == null ? 0d : item
								.getCilCheckOutsideEligibleAreas()) < (item.getCfCheckOutsideEligibleAreas() == null ? 0d
								: item.getCfCheckOutsideEligibleAreas())){
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("validator.cilChecklessOutsideEligibleAreas"));
							return;
						}
						
						if ((item.getCilCheck() == null ? 0d : item
								.getCilCheck()) < (item.getCfCheck() == null ? 0d
								: item.getCfCheck()))
						{
							tr.rollback();
							this.setErrorMessage(Utils
									.getString("costDefinitionCfamountError"));
							return;
						}

						item.setCfDate(new Date());
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.CFValidate.getState()));
						BeansFactory.CostDefinitions().SaveInTransaction(item);

					}
				}
			}
			try
			{
				for (CostDefinitions item : getCostDefinitionsList())
				{
					if (item.getSelected())
					{
						ActivityLog.getInstance().addExtendedActivity(
								Utils.getString("extendedActLogCdSendToDur"),
								null, item.getProgressiveId());
					}
				}
			}
			catch (Exception e)
			{
				log.error("CostDefinitionListBean exception",e);
			}
			if (tr != null)
			{
				tr.commit();
			}

			this.setSelectAll(false);
			this.setIds(new ArrayList<Long>());
			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (HibernateException e1)
		{
			log.error("CostDefinitionListBean exception",e1);
		}
		catch (PersistenceBeanException e1)
		{
			log.error("CostDefinitionListBean exception",e1);
		}
	}

	public void doSelectAll()
	{
		List<Long> ids = new ArrayList<Long>();

		if (getSelectAll())
		{
			for (CostDefinitions item : getCostDefinitionsList())
			{
				if (item.getCostState().getId()
						.equals(CostStateTypes.CFValidation.getState()))
				{
					ids.add(item.getId());
				}
				if ((item.getIsSentToAAU() == null || item.getIsSentToAAU() == false)
						&& item.getACUSertified())
				{
					ids.add(item.getId());
				}
			}
		}

		this.setIds(ids);

		try
		{
			this.setNeedReload(true);
			Page_Load();
		}
		catch (NumberFormatException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
		catch (HibernateException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
		catch (PersistenceBeanException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
	 */
	@SuppressWarnings("unused")
	@Override
	public void addEntity()
	{
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.getSession().put("costDefinitionEdit", null);
		this.getSession().put("costDefinitionView", null);
		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		try
		{
			List<Long> docs = new ArrayList<Long>();

			List<CostDefinitionsToDocuments> listCD = BeansFactory
					.CostDefinitionsToDocuments().getByCostDefinition(
							this.getEntityDeleteId());

			for (CostDefinitionsToDocuments cd : listCD)
			{
				if (cd.getDocument() != null)
				{
					docs.add(cd.getDocument().getId());
				}
				BeansFactory.CostDefinitionsToDocuments().Delete(cd);
			}

			CostDefinitions cd = BeansFactory.CostDefinitions().Load(
					this.getEntityDeleteId());

			if (cd != null)
			{
				if (cd.getCostDocument() != null)
				{
					docs.add(cd.getCostDocument().getId());
				}

				if (cd.getPaymentDocument() != null)
				{
					docs.add(cd.getPaymentDocument().getId());
				}

				BeansFactory.CostDefinitions().Delete(this.getEntityDeleteId());
			}

			for (Long id : docs)
			{
				BeansFactory.Documents().Delete(id);
			}

			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (HibernateException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
		catch (PersistenceBeanException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		this.getSession().put("costDefinitionEdit", this.getEntityEditId());
		this.getSession().put("costDefinitionView", null);
		this.getSession().put(SAVED_PAGE_INDEX, scroller.getPageIndex());
		this.getSession().put(SAVED_CDL_ITEMS_PER_PAGE, this.getItemsPerPage());

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	public void cloneEntity()
	{
		this.getSession().put("costDefinitionEdit", null);
		this.getSession().put("costDefinitionView", null);
		this.getSession().put(CLONE_CD_ID, this.getEntityEditId());

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	public String dismissSuperAdmin()
	{
		if (this.getActionMotivation() == null
				|| this.getActionMotivation().isEmpty())
		{
			return null;
		}
		dismissEntity();
		clearMotivation(null);
		return null;
	}

	public void dismissEntity()
	{
		try
		{
			CostDefinitions cost = BeansFactory.CostDefinitions().Load(
					this.getEntityDeleteId());
			cost.setDismiss(true);
			cost.setCostState(BeansFactory.CostStates().Load(
					CostStateTypes.Dismiss.getState()));
			BeansFactory.CostDefinitions().Save(cost);

			try
			{
				ActivityLog.getInstance().addExtendedActivity(
						Utils.getString("extendedActLogCdDismiss"), null,
						cost.getProgressiveId());
			}
			catch (Exception e)
			{
				log.error("CostDefinitionListBean exception",e);
			}

			if (getSessionBean().isSuperAdmin())
			{
				SuperAdminChange changes = new SuperAdminChange(
						ChangeType.DISMISS, this.getCurrentUser(),
						this.getActionMotivation(), getSessionBean()
								.getProject());
				StringBuilder changeLogMessages = new StringBuilder();
				changeLogMessages
						.append(Utils
								.getString("superAdminChangeLogMessageHeader")
								.replace(
										"%1",
										Utils.getString("superAdminChangesCostDefinition"))
								.replace("%2", cost.getId().toString())
								.replace("%3",
										Utils.getString("costDefinition")))

						.append("<br/>");
				changeLogMessages
						.append(Utils
								.getString("superAdminChangeLogMessage")
								.replace("%1", Utils.getString("dismissCost"))
								.replace(
										"%2",
										Utils.getString("superAdminChangesFalseValue"))
								.replace(
										"%3",
										Utils.getString("superAdminChangesTrueValue")))
						.append("<br/>");
				changes.setChanges(changeLogMessages.toString());
				BeansFactory.SuperAdminChanges().Save(changes);
			}
			this.setNeedReload(true);
			this.Page_Load();
		}
		catch (HibernateException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
		catch (PersistenceBeanException e)
		{
			log.error("CostDefinitionListBean exception",e);
		}
	}

	public void viewEntity()
	{
		this.getSession().put("costDefinitionEdit", this.getEntityEditId());
		this.getSession().put("costDefinitionView", this.getEntityEditId());
		this.getSession().put("costDefinitionViewBackToValidationFlow", null);
		this.getSession().put("costDefinitionViewBackToDUREditFlow", null);

		this.getSession().put(SAVED_PAGE_INDEX, scroller.getPageIndex());
		this.getSession().put(SAVED_CDL_ITEMS_PER_PAGE, this.getItemsPerPage());

		this.goTo(PagesTypes.COSTDEFINITIONEDIT);
	}

	public void setCostStates(List<SelectItem> costStates)
	{
		CostDefinitionListBean.costStates = costStates;
	}

	public List<SelectItem> getCostStates()
	{
		return costStates;
	}

	/**
	 * Stores tr value of entity.
	 */
	private Transaction	tr;

	/**
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 */
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException
	{
		for (CostDefinitions item : getCostDefinitionsList())
		{
			item.setCostState(BeansFactory.CostStates().Load(
					item.getRealCostStateId()));

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
			log.error("CostDefinitionListBean exception",e1);
		}
		catch (PersistenceBeanException e1)
		{
			log.error("CostDefinitionListBean exception",e1);
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
			log.error("CostDefinitionListBean exception",e);
		}
		catch (PersistenceBeanException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error("CostDefinitionListBean exception",e);
		}
		catch (NumberFormatException e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error("CostDefinitionListBean exception",e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
			}
		}

	}

	/**
	 * Sets filterValue
	 * 
	 * @param filterValue
	 *            the filterValue to set
	 */
	public void setFilterValue(String filterValue)
	{
		boolean needReload = this.filterValue != null
				? !this.filterValue.equals(filterValue) : true;
		this.filterValue = filterValue;
		this.getViewState().put("filter", filterValue);
		try
		{
			this.setNeedReload(needReload);
			this.Page_Load();
			this.setTableFirstRow(0);
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets filterValue
	 * 
	 * @return filterValue the filterValue
	 */
	public String getFilterValue()
	{
		filterValue = (String) this.getViewState().get("filter");
		return filterValue;
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
		fillDropDown();
		setNeedReload(true);
	}

	/**
	 * Sets canSendToDur
	 * 
	 * @param canSendToDur
	 *            the canSendToDur to set
	 */
	public void setCanSendToDur(boolean canSendToDur)
	{
		this.getViewState().put("canSendToDur", canSendToDur);
	}

	/**
	 * Gets canSendToDur
	 * 
	 * @return canSendToDur the canSendToDur
	 */
	public boolean getCanSendToDur()
	{
		if (this.getViewState().containsKey("canSendToDur"))
		{
			return (Boolean) this.getViewState().get("canSendToDur");
		}
		
		return false;
	}

	/**
	 * Sets ids
	 * 
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(List<Long> ids)
	{
		this.getViewState().put("costDefinitions", ids);
	}

	/**
	 * Gets ids
	 * 
	 * @return ids the ids
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getIds()
	{
		if (this.getViewState().get("costDefinitions") == null)
		{
			this.getViewState().put("costDefinitions", new ArrayList<Long>());
		}

		return (List<Long>) this.getViewState().get("costDefinitions");

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
	 * Sets errorMessage
	 * 
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets errorMessage
	 * 
	 * @return errorMessage the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterValidationStatusValues()
	{
		return (List<SelectItem>) this.getViewState()
				.get("filterValidationStatusValues");
	}

	public void setFilterValidationStatusValues(
			List<SelectItem> filterValidationStatusValues)
	{
		this.getViewState().put("filterValidationStatusValues",
				filterValidationStatusValues);
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

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCostItemValues()
	{
		return (List<SelectItem>) this.getViewState().get("filterCostItemValues");
	}

	public void setFilterCostItemValues(List<SelectItem> filterCostItemValues)
	{
		this.getViewState().put("filterCostItemValues", filterCostItemValues);
	}

	public String getPartnerID()
	{
		if (getViewState().containsKey("partnerID"))
		{
			return (String) getViewState().get("partnerID");
		}
		return null;
	}

	public void setPartnerID(String partnerID)
	{
		getViewState().put("partnerID", partnerID);
	}
	
	public String getReportNumber()
	{
		if (getViewState().containsKey("reportNumber"))
		{
			return (String) getViewState().get("reportNumber");
		}
		return null;
	}

	public void setReportNumber(String reportNumber)
	{
		getViewState().put("reportNumber", reportNumber);
	}

	public String getCostDefID()
	{
		if (getViewState().containsKey("costDefID"))
		{
			return (String) getViewState().get("costDefID");
		}
		return null;
	}

	public void setCostDefID(String costDefID)
	{
		getViewState().put("costDefID", costDefID);
	}

	public String getDurNumber()
	{
		if (getViewState().containsKey("durNumber"))
		{
			return (String) getViewState().get("durNumber");
		}
		return null;
	}

	public void setDurNumber(String durNumber)
	{
		getViewState().put("durNumber", durNumber);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterPartnerValues()
	{
		return (List<SelectItem>) this.getViewState()
				.get("filterPartnerValues");
	}

	public void setFilterPartnerValues(List<SelectItem> filterPartnerValues)
	{
		this.getViewState().put("filterPartnerValues", filterPartnerValues);
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
	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterPhases()
	{
		return (List<SelectItem>) this.getViewState().get("filterPhases");
	}

	/**
	 * Sets filterPhases
	 * 
	 * @param filterPhases
	 *            the filterPhases to set
	 */
	public void setFilterPhases(List<SelectItem> filterPhases)
	{
		this.getViewState().put("filterPhases", filterPhases);
	}

	/**
	 * Gets filterPhase
	 * 
	 * @return filterPhase the filterPhase
	 */
	public Long getFilterPhase()
	{
		return (Long) this.getViewState().get("filterPhase");
	}

	/**
	 * Sets filterPhase
	 * 
	 * @param filterPhase
	 *            the filterPhase to set
	 */
	public void setFilterPhase(Long filterPhase)
	{
		this.getViewState().put("filterPhase", filterPhase);
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
			for (CostDefinitions item : this.getCostDefinitionsList())
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
	 * Gets tableFirstRow
	 * 
	 * @return tableFirstRow the tableFirstRow
	 */
	public Integer getTableFirstRow()
	{
		if (this.getViewState().containsKey("tableFirstRow"))
		{
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
	public void setTableFirstRow(Integer tableFirstRow)
	{
		this.getViewState().put("tableFirstRow", tableFirstRow);
	}

	/**
	 * Gets filterCertificatedBy
	 * 
	 * @return filterCertificatedBy the filterCertificatedBy
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getFilterCertificatedBy()
	{
		return (List<SelectItem>) this.getViewState()
				.get("filterCertificatedBy");
	}

	/**
	 * Sets filterCertificatedBy
	 * 
	 * @param filterCertificatedBy
	 *            the filterCertificatedBy to set
	 */
	public void setFilterCertificatedBy(List<SelectItem> filterCertificatedBy)
	{
		this.getViewState().put("filterCertificatedBy", filterCertificatedBy);
	}

	/**
	 * Gets certificatedByTypeFiltered
	 * 
	 * @return certificatedByTypeFiltered the certificatedByTypeFiltered
	 */
	public String getCertificatedByTypeFiltered()
	{
		return certificatedByTypeFiltered;
	}

	/**
	 * Sets certificatedByTypeFiltered
	 * 
	 * @param certificatedByTypeFiltered
	 *            the certificatedByTypeFiltered to set
	 */
	public void setCertificatedByTypeFiltered(String certificatedByTypeFiltered)
	{
		this.certificatedByTypeFiltered = certificatedByTypeFiltered;
	}

	/**
	 * Gets canSendToAda
	 * 
	 * @return canSendToAda the canSendToAda
	 */
	public boolean getCanSendToAda()
	{
		return canSendToAda;
	}

	/**
	 * Sets canSendToAda
	 * 
	 * @param canSendToAda
	 *            the canSendToAda to set
	 */
	public void setCanSendToAda(boolean canSendToAda)
	{
		this.canSendToAda = canSendToAda;
	}

	/**
	 * Gets selectedToSend
	 * 
	 * @return selectedToSend the selectedToSend
	 */
	public boolean getSelectedToSend()
	{
		if (this.getViewState().get("selectedToAda") == null)
			return false;

		return (Boolean) this.getViewState().get("selectedToAda");
	}

	/**
	 * Sets selectedToSend
	 * 
	 * @param selectedToSend
	 *            the selectedToSend to set
	 */
	public void setSelectedToSend(boolean selectedToSend)
	{
		this.getViewState().put("selectedToAda", selectedToSend);
	}

	/**
	 * Gets editPage
	 * @return editPage the editPage
	 */
	public String getEditPage()
	{
		return editPage;
	}

	/**
	 * Sets editPage
	 * @param editPage the editPage to set
	 */
	public void setEditPage(String editPage)
	{
		this.editPage = editPage;
	}


	public void setAdditionalFinansing(Boolean additionalFinansing){
		this.getViewState().put("additionalFinansing", additionalFinansing);
	}
	
	public Boolean getAdditionalFinansing(){
		return (Boolean)this.getViewState().get("additionalFinansing");
	}
	
	public Boolean getNeedReload()
	{
		if (this.getViewState().containsKey("needReload"))
		{
			return (Boolean) this.getViewState().get("needReload");
		}
		else
		{
			return false;
		}
	}
	
	public void setNeedReload(Boolean needReload)
	{
		this.getViewState().put("needReload", needReload);
	}
	
	@SuppressWarnings("unchecked")
	public List<CostDefinitions> getCostDefinitionsList()
	{
		return (List<CostDefinitions>) this.getViewState()
				.get("tempCostDefinitionsList");
	}

	public void setCostDefinitionsList(List<CostDefinitions> tempCostDefinitionsList)
	{
		this.getViewState().put("tempCostDefinitionsList",
				tempCostDefinitionsList);
	}

	public Boolean getDisplaySelectAll()
	{
		return (Boolean) this.getViewState().get("displaySelectAll");
	}

	public void setDisplaySelectAll(Boolean displaySelectAll)
	{
		this.getViewState().put("displaySelectAll", displaySelectAll);
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
