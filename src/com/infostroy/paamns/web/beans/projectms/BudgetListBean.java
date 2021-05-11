/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.helpers.SuperAdminHelper;
import com.infostroy.paamns.common.helpers.mail.BudgetApprovedMailSender;
import com.infostroy.paamns.common.helpers.mail.BudgetChangedMailSender;
import com.infostroy.paamns.common.helpers.mail.BudgetRefusedMailSender;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureColumn;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureRow;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureTab;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureTable;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * @author Vito Rifino - Ingloba360 srl, 2017.
 * 
 */
public class BudgetListBean extends EntityProjectEditBean<Entity>
{
	private List<GeneralBudgets>			listGB;

	private List<PartnersBudgets>			listPB;

	private List<Phase>						listPhase;

	private ProjectIformationCompletations	pic;

	private List<CFPartnerAnagraphs>		listPartners;

	private BudgetInputSourceDivided		sourceBudget;

	private List<SelectItem>				listWebPartners;

	private List<CostDefinitionPhases>		listSubProjects;

	private String							spanValidatorVisibility;

	private String							generalBudgetMinorValidatorMessage;

	private String							spanPartnerValidatorVisibility;

	private String							spanGBSaved;

	private String							spanPBSaved;

	private boolean							approved;

	private boolean							denied;

	@SuppressWarnings("unused")
	private String							selectedPartner;

	private GeneralBudgets					currentGeneralBudget;

	@SuppressWarnings("unused")
	private String							selectedPartnerStc;

	@SuppressWarnings("unused")
	private String							selectedBudget;

	@SuppressWarnings("unused")
	private String							spanApprovedVisibility;

	@SuppressWarnings("unused")
	private Boolean							approveAvailable;

	private List<PartnersBudgets>			listPartnersBudgetsStc;

	private List<SelectItem>				listPartnerBudgetsStcWeb;

	@SuppressWarnings("unused")
	private Boolean							showConfirmationMessage;

	@SuppressWarnings("unused")
	private Boolean							partnerChangedFlag;

	private String							spanLessThanCost;

	private String							stringLessThanCost;

	private boolean							cf;

	private CFPartnerAnagraphs				curCF;

	private String							stcCannotApprove;

	private String							notCorrectBudget;

	private List<InfoObject>				mailsBudgetApproved;

	private List<InfoObject>				mailsBudgetRefused;
	
	private boolean							acce3;
	
	private static final String				YEARS	="2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024";
	
	private List<CostAsse3>					legend;
	
	private DataStructureTab tabForAsse3;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		this.setAcce3(3 == this.getProject().getTypology().getId());
		
		this.listPB = new ArrayList<PartnersBudgets>();
		this.setLegend(BeansFactory.CostAsse3().Load());
		
		if (this.getSessionBean().isCF() || this.getSessionBean().isAGU()
				&& this.getProject().getAsse() == 5)
		{
			this.setCurCF(BeansFactory.CFPartnerAnagraphs().GetByUser(
					this.getCurrentUser().getId()));

			if (this.getCurCF() != null || this.getSessionBean().isAGU())
			{
				this.setCurrentUserPartner(true);
				this.setCf(true);
			}
		}
		
		this.setSpanValidatorVisibility("none");
		this.setSpanPartnerValidatorVisibility("none");
		this.setGeneralBudgetMinorValidatorMessage("none");
		this.setSpanLessThanCost("none");
		this.setSpanGBSaved("none");
		this.setSpanPBSaved("none");
		this.setSpanApprovedVisibility("none");

		this.LoadEntities();

		this.setShowConfirmationMessage(false);

	}

	private void deleteSummary()
	{
		Collections.sort(this.listGB, new GeneralBudgetComparer());
		this.listGB = this.listGB.subList(0, this.listGB.size() - 1);
	}

	private void addSummary()
	{
		double sum_t = 0d;
		double sum_f = 0d;
		double sum_pub = 0d;
		double sum_pub_oth = 0d;
		double sum_priv = 0d;
		double sum_net_revenue = 0d;   // Entrate Nette

		for (GeneralBudgets gb : this.listGB)
		{
			sum_t += gb.getBudgetTotal() != null ? gb.getBudgetTotal() : 0d;
			sum_f += gb.getFesr() != null ? gb.getFesr() : 0d;
			sum_pub += gb.getCnPublic() != null ? gb.getCnPublic() : 0d;
			sum_pub_oth += gb.getCnPublicOther() != null ? gb.getCnPublicOther() : 0d; // corrisponde agli 'altri territori'
			sum_priv += gb.getQuotaPrivate() != null ? gb.getQuotaPrivate()
					: 0d;
			sum_net_revenue += gb.getNetRevenue() != null ? gb.getNetRevenue() : 0d;
		}

		GeneralBudgets newEntity = new GeneralBudgets();
		newEntity.setBudgetTotal(sum_t);
		newEntity.setCnPublic(sum_pub);
		newEntity.setCnPublicOther(sum_pub_oth);
		newEntity.setQuotaPrivate(sum_priv);
		newEntity.setFesr(sum_f);
		newEntity.setNetRevenue(sum_net_revenue);

		this.listGB.add(this.listGB.size(), newEntity);
		Collections.sort(this.listGB, new GeneralBudgetComparer());
	}

	public void Page_Load_Static() throws PersistenceBeanException
	{
		if (!this.getSessionBean().getIsActualSate())
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}		
	}

	private void LoadEntities() throws NumberFormatException,
			PersistenceBeanException
	{
		List<Long> listCFAnagraphsIndices = new ArrayList<Long>();
		List<GeneralBudgets> listAuxGB = BeansFactory.GeneralBudgets()
				.GetItemsForProject(Long.valueOf(this.getProjectId()));
		this.listGB = new ArrayList<GeneralBudgets>();

		for (GeneralBudgets gb : listAuxGB)
		{
			GeneralBudgets gbClone = gb.clone();
			if (gb.getCfPartnerAnagraph() != null)
			{
				CFPartnerAnagraphProject partnerToProject = getPartnerAnagraphProjectForUser(gb
						.getCfPartnerAnagraph().getUser());

				if (partnerToProject != null)
				{
					gbClone.getCfPartnerAnagraph().setRemovedFromProject(
							partnerToProject.getRemovedFromProject());
				}
			}
			this.listGB.add(gbClone);
			listCFAnagraphsIndices.add(gb.getCfPartnerAnagraph().getId());
			
		}
		List<CFPartnerAnagraphs> listCFAnagraphs = BeansFactory
				.CFPartnerAnagraphs().LoadByProject(
						String.valueOf(this.getSession().get("project")));

		for (CFPartnerAnagraphs cfAnagraph : listCFAnagraphs)
		{
			if (!listCFAnagraphsIndices.contains(cfAnagraph.getId()))
			{
				GeneralBudgets newEntity = GeneralBudgets.create(
						this.getProject(), cfAnagraph);
				newEntity.setBudgetTotal(0d);
				newEntity.setFesr(0d);
				newEntity.setQuotaPrivate(0d);
				newEntity.setCnPublicOther(0d);
				newEntity.setNetRevenue(0d);
				this.listGB.add(newEntity);
				BeansFactory.GeneralBudgets().Save(newEntity);
			}
		}
		this.addSummary();

		this.listPartners = BeansFactory.CFPartnerAnagraphs().LoadByProject(
				this.getProjectId());
		
		this.listSubProjects = BeansFactory.CostDefinitionPhase().Load();

		this.pic = BeansFactory.ProjectIformationCompletations().LoadByProject(
				this.getProjectId());
		this.sourceBudget = BudgetInputSourceDividedBean.GetByProject(
				this.getProjectId()).get(0);

		this.listWebPartners = new ArrayList<SelectItem>();

		if (this.getSelectedPartner() == null
				|| this.getSelectedPartner().isEmpty())
		{
			this.setSelectedPartner(String.valueOf(listPartners.get(0).getId()));
		}

		if (this.getSelectedSubproject() == null
				|| this.getSelectedSubproject().isEmpty())
		{
			this.setSelectedSubproject(String.valueOf(listSubProjects.get(0)
					.getId()));
		}

		List<GeneralBudgets> genBudg = BeansFactory.GeneralBudgets()
				.GetActualItemsForCFAnagraph(this.getProjectId(),
						Long.valueOf(this.getSelectedPartner()));
		if (genBudg == null || genBudg.isEmpty())
		{
			GeneralBudgets newEntity = new GeneralBudgets();
			newEntity.setCreateDate(DateTime.getNow());
			newEntity.setDeleted(false);
			newEntity.setCfPartnerAnagraph(BeansFactory.CFPartnerAnagraphs()
					.Load(this.getSelectedPartner()));
			newEntity.setProject(this.getProject());
			newEntity.setApproved(false);
			newEntity.setIsOld(false);

			genBudg = new ArrayList<GeneralBudgets>();
			genBudg.add(newEntity);
			BeansFactory.GeneralBudgets().Save(newEntity);
		}

		this.currentGeneralBudget = genBudg.get(0);

		for (CFPartnerAnagraphs partner : listPartners)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(partner.getId()));
			item.setLabel(partner.getName());
			this.listWebPartners.add(item);
		}

		if (this.getSessionBean().isSTC()
				&& this.getSessionBean().getProjectLock()
				|| this.getCurrentUserPartner()
				&& !this.getSessionBean().getProjectLock())
		{
			this.LoadPartnersBudgets();
		}
		else
		{
			this.LoadBudget();
		}
	}

	/**
	 * @param gb
	 * @param user
	 *            TODO
	 * @param partnerToProject
	 * @return
	 * @throws PersistenceBeanException
	 */
	private CFPartnerAnagraphProject getPartnerAnagraphProjectForUser(Users user)
			throws PersistenceBeanException
	{
		CFPartnerAnagraphProject partnerToProject = null;
		if (this.getProjectAsse() == 5 && user == null)
		{
			CFPartnerAnagraphs partner = SuperAdminHelper
					.getAguPartnerForThisProject(this.getProjectId());
			partnerToProject = BeansFactory.CFPartnerAnagraphProject()
					.LoadByPartner(this.getProject().getId(), partner.getId());
		}
		else
		{
			List<CFPartnerAnagraphProject> cfpaList = BeansFactory
					.CFPartnerAnagraphProject().GetCFAnagraphForProjectAndUser(
							this.getProjectId(), user.getId());
			if (!cfpaList.isEmpty())
			{
				partnerToProject = cfpaList.get(0);
			}
		}
		return partnerToProject;
	}

	private void LoadPartnersBudgets() throws PersistenceBeanException
	{
		List<PartnersBudgets> listAux = BeansFactory.PartnersBudgets()
				.GetByProjectAndPartnerWDenied(this.getProjectId(),
						this.getSelectedPartner(), true);

		if (!listAux.isEmpty() &&  Boolean.TRUE.equals(listAux.get(0).getDenied()))
		{
			listAux = BeansFactory.PartnersBudgets()
					.GetByProjectAndPartnerNotDenied(this.getProjectId(),
							this.getSelectedPartner());
		}
		if(!this.getAcce3()){
			for (PartnersBudgets item : listAux)
			{
				item.setPhaseBudgets(BeansFactory.PartnersBudgetsPhases()
						.loadByPartnerBudget(item.getId()));
			}
			
			this.setApproved(false);
			this.setDenied(false);
			this.getListPB().clear();
			CFPartnerAnagraphs selecterPartner = BeansFactory.CFPartnerAnagraphs()
					.Load(this.getSelectedPartner());
			CFPartnerAnagraphProject partnerToProject = getPartnerAnagraphProjectForUser(selecterPartner
					.getUser());
			for (PartnersBudgets pb : listAux)
			{
				PartnersBudgets pbClone = pb.clone();

				if (partnerToProject != null)
				{
					pbClone.getCfPartnerAnagraph().setRemovedFromProject(
							partnerToProject.getRemovedFromProject());
				}

				this.listPB.add(pbClone);
				this.setApproved(pb.getApproved());
				this.setDenied(pb.getDenied() != null ? pb.getDenied() : false);
			}

			PartnersBudgets totalEntity = new PartnersBudgets(); // Total entity
			totalEntity.setPhaseBudgets(new LinkedList<Phase>());
			for (CostDefinitionPhases phase : BeansFactory.CostDefinitionPhase()
					.Load())
			{
				Phase newPhase = new Phase();
				newPhase.setPartnersBudgets(totalEntity);
				newPhase.setPartAdvInfoProducts(0d);
				newPhase.setPartAdvInfoPublicEvents(0d);
				newPhase.setPartDurableGoods(0d);
				newPhase.setPartDurables(0d);
				newPhase.setPartHumanRes(0d);
				newPhase.setPartMissions(0d);
				newPhase.setPartOther(0d);
				newPhase.setPartOverheads(0d);
				newPhase.setPartGeneralCosts(0d);
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
				
				newPhase.setPhase(phase);
				totalEntity.getPhaseBudgets().add(newPhase);
			}

			if (this.listPB == null || this.listPB.isEmpty())
			{
				this.listPB = new ArrayList<PartnersBudgets>();

					PartnersBudgets newEntity = new PartnersBudgets();
					newEntity.setPhaseBudgets(new LinkedList<Phase>());

					for (CostDefinitionPhases phase : BeansFactory
							.CostDefinitionPhase().Load())
					{
						Phase newPhase = new Phase();
						newPhase.setPartnersBudgets(newEntity);
						newPhase.setPartAdvInfoProducts(0d);
						newPhase.setPartAdvInfoPublicEvents(0d);
						newPhase.setPartDurableGoods(0d);
						newPhase.setPartDurables(0d);
						newPhase.setPartHumanRes(0d);
						newPhase.setPartMissions(0d);
						newPhase.setPartOther(0d);
						newPhase.setPartOverheads(0d);
						newPhase.setPartGeneralCosts(0d);
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
						
						newPhase.setPhase(phase);
						newEntity.getPhaseBudgets().add(newPhase);
					}
					newEntity.setTotalAmount(0d);
					newEntity.setBudgetRelease(0d);
					newEntity.setDescription(Utils.getString("Resources",
					"partnerBudgetFooterTotal", null));
					newEntity.setCfPartnerAnagraph(selecterPartner);

					this.listPB.add(newEntity);
				
			}
		}else{
			this.setTabForAsse3(createEditTabForPb(createCopyForAsse3(listAux.get(0))));
			this.setApproved(listAux.get(0).getApproved());
			this.setDenied(false);
			this.listPB.clear();
			this.listPB.add(listAux.get(0));
		}
	
		if (IsBudgetActual(this.getListPB(),
				Long.parseLong(this.getSelectedPartner())))
		{
			this.setNotCorrectBudget(null);
		}
	}	

	public Boolean BeforeSave() throws PersistenceBeanException
	{
		this.deleteSummary();
		double FESRSum = 0;
		double CNPublicSum = 0;
		double CNPublicSumOth = 0;
		double CNPrivateSum = 0;
		double NetRevenueSum = 0;

		for (GeneralBudgets gb : this.listGB)
		{
			FESRSum += gb.getFesr();
			CNPublicSum += gb.getCnPublic();
			CNPublicSumOth += gb.getCnPublicOther();
			CNPrivateSum += gb.getQuotaPrivate();
			NetRevenueSum += gb.getNetRevenue();
		}

		boolean additionValidation = false;
		if (Boolean.TRUE.equals(this.getSessionBean().getProjectLock()))
		{
			additionValidation = this.getSessionBean().getProjectLock()
					&& this.getSessionBean().isSTCW();
		}

		if (additionValidation)
		{
			if (this.getSessionBean().getProjectLock()
					&& this.getSessionBean().isSTCW())
			{				
				for (GeneralBudgets gb : this.listGB)
				{
					List<CostDefinitions> cdList = BeansFactory
							.CostDefinitions()
							.GetForProjectCheckValues(this.getProjectId(),
									gb.getCfPartnerAnagraph().getUser().getId());
					double sumA = 0d;

					if (cdList != null && !cdList.isEmpty())
					{
						for (CostDefinitions item : cdList)
						{
							if (item.getCostState() != null
									&& item.getCostState().equals(
											BeansFactory.CostStates().Load(
													CostStateTypes.Closed
															.getState()))
									&& !item.getWasRefusedByCil()
									|| item.getDismiss().equals(true))
							{
								continue;
							}
							if (CostStateTypes.Draft.getState().equals(
									item.getCostState()))
							{
								continue;
							}
							if (item.getAcuCertification() != null
									&& item.getACUSertified())
							{
								sumA += item.getAcuCertification();
							}
							else if (item.getAguCertification() != null
									&& item.getAGUSertified())
							{
								sumA += item.getAguCertification();
							}
							else if (item.getStcCertification() != null
									&& item.getSTCSertified())
							{
								sumA += item.getStcCertification();
							}
							else if (item.getCfCheck() != null)
							{
								sumA += item.getCfCheck();
							}
							else if (item.getCilCheck() != null 
									&& !item.getCilIntermediateStepSave())
							{
								sumA += item.getCilCheck();
							}
							else
							{
								sumA += (item.getAmountReport() != null ? item
										.getAmountReport() : 0d);
							}
						}
					}

					if (NumberHelper.Round(sumA) > NumberHelper.Round(gb
							.getFesr()
							+ gb.getCnPublic()
							+ gb.getQuotaPrivate()
							+ gb.getNetRevenue()))
					{
						this.setGeneralBudgetMinorValidatorMessage("");
						this.addSummary();
						return false;
					}
				}				
			}
		}

		if (!additionValidation)
		{
			if (NumberHelper.Round(FESRSum) == NumberHelper
					.Round(this.sourceBudget.getFesr())
					&& NumberHelper.Round(CNPublicSum) == NumberHelper
							.Round(this.sourceBudget.getCnPublic())
					&& NumberHelper.Round(CNPrivateSum) == NumberHelper
							.Round(this.sourceBudget.getCnPrivate())
					&& NumberHelper.Round(NetRevenueSum) == NumberHelper
							.Round(this.sourceBudget.getNetRevenue())		
							&& NumberHelper.Round((CNPublicSumOth)) == NumberHelper.Round(this.sourceBudget.getCnPublicOther()))
			{

				this.addSummary();
				this.setSpanValidatorVisibility("none");
				return true;
			}
			else
			{
				this.addSummary();
				this.setSpanValidatorVisibility("");
				return false;
			}
		}
		else
		{

			this.addSummary();
			this.setSpanValidatorVisibility("none");
			return true;
		}
	}

	@Override
	public void AfterSave()
	{
		try
		{
			this.LoadEntities();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void GoBack()
	{
		this.goTo(PagesTypes.PROJECTINDEX);
	}

	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException
	{
		if (this.CheckModificationsGeneralBudget())
		{
			List<GeneralBudgets> listgb = BeansFactory.GeneralBudgets()
					.GetItemsForProject(Long.valueOf(this.getProjectId()));

			for (GeneralBudgets gb : listgb)
			{
				gb.setIsOld(true);
				BeansFactory.GeneralBudgets().SaveInTransaction(gb);
			}

			saveListGB();
		}
		else
		{
			List<GeneralBudgets> listOld = BeansFactory.GeneralBudgets()
					.GetItemsForProject(Long.valueOf(this.getProjectId()));

			if (listOld == null || listOld.isEmpty())
			{
				saveListGB();
			}
		}
		this.setSpanGBSaved("");
	}

	/**
	 * @throws PersistenceBeanException
	 */
	private void saveListGB() throws PersistenceBeanException
	{
		for (GeneralBudgets gb : this.listGB)
		{
			gb.setCreateDate(DateTime.getNow());
			gb.setBudgetTotal(NumberHelper.Round(gb.getFesr()
					+ gb.getCnPublic() + gb.getQuotaPrivate()
					+ gb.getNetRevenue()));
			BeansFactory.GeneralBudgets().SaveInTransaction(gb);
		}
	}

	private boolean CheckModificationsGeneralBudget()
	{
		boolean isModified = false;

		List<GeneralBudgets> listGbOld = new ArrayList<GeneralBudgets>();
		try
		{
			listGbOld = BeansFactory.GeneralBudgets().GetItemsForProject(
					Long.valueOf(this.getProjectId()));
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (listGbOld == null)
		{
			return false;
		}

		Collections.sort(listGbOld, new GeneralBudgetComparer());
		Collections.sort(this.listGB, new GeneralBudgetComparer());

		int counter = -1;
		for (GeneralBudgets gb : listGbOld)
		{
			counter++;

			if (gb.getCnPublic() != null
					&& this.listGB.get(counter).getCnPublic() != null
					&& !gb.getCnPublic().equals(
							this.listGB.get(counter).getCnPublic()))
			{
				isModified = true;
				break;
			}
			if (gb.getQuotaPrivate() != null
					&& this.listGB.get(counter).getQuotaPrivate() != null
					&& !gb.getQuotaPrivate().equals(
							this.listGB.get(counter).getQuotaPrivate()))
			{
				isModified = true;
				break;
			}
			if (gb.getFesr() != null
					&& this.listGB.get(counter).getFesr() != null
					&& !gb.getFesr().equals(this.listGB.get(counter).getFesr()))
			{
				isModified = true;
				break;
			}
			if (gb.getCnPublic() == null && gb.getFesr() == null
					&& gb.getQuotaPrivate() == null)
			{
				isModified = true;
				break;
			}
			
			if (gb.getNetRevenue() != null
					&& this.listGB.get(counter).getNetRevenue() != null
					&& !gb.getNetRevenue().equals(
							this.listGB.get(counter).getNetRevenue()))
			{
				isModified = true;
				break;
			}	
		}

		return isModified;
	}

	public void Page_SavePartner() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		this.getViewState().put("tempPartnerBudget", null);
		if (!IsBudgetActual(this.getListPB(),
				Long.parseLong(this.getSelectedPartner())))
		{
			this.setSpanPartnerValidatorVisibility("");
			return;
		} else {
			this.setSpanPartnerValidatorVisibility("none");
		}
		
		if (this.getAcce3())
		{
			PartnersBudgets budgetToSave =getTabForAsse3().getBudget();

			// Check the total value for budget
			if (!checkTotalValueBudgetAsse3(budgetToSave))
			{
				this.setSpanPartnerValidatorVisibility("");
				return;
			}

			PartnersBudgets oldBudget = null;
			try
			{
				oldBudget = this.getListPB().get(0);
			}
			catch (Exception e)
			{
				log.error("Exception during save PB for acce 3", e);
			}

			// Check presence of earlier version of budget
			// If present make earlier version isOld, and saving current change
			// as new budget
			// If not present just saving the budget
			if (oldBudget != null)
			{

				if (!checkChangesBudgetAsse3(budgetToSave, oldBudget))
				{
					return;
				}
				oldBudget.setIsOld(Boolean.TRUE);

				BeansFactory.PartnersBudgets().SaveInTransaction(oldBudget);

				Long maxVersion = BeansFactory.PartnersBudgets()
						.GetLastVersionNumber(this.getProjectId(),
								this.getSelectedPartner(), null);

				if (maxVersion == null)
				{
					maxVersion = 1l;
				}


				budgetToSave.setVersion(maxVersion+1);
				

			}else{
				budgetToSave.setVersion(0l);
			}
			budgetToSave.setApproved(Boolean.FALSE);
			BeansFactory.PartnersBudgets().SaveInTransaction(budgetToSave);
			saveAsse3(budgetToSave);
			this.setTabForAsse3(this.createEditTabForPb(createCopyForAsse3(budgetToSave)));
			this.setSpanPBSaved("");
		}
		else
		{
			if (!this.BeforeSavePartner())
			{
				return;
			}

			if (!CheckModificationsApproveCF())
			{
				this.SaveEntityPartner();

				this.AfterSavePartner();
			}
			else
			{
				this.getViewState().put("tempPartnerBudget", this.getListPB());
				this.setShowConfirmationMessage(true);
			}
		}
	}
			
	private void saveAsse3(PartnersBudgets budget){				
		Transaction tr=null;
		try {
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
		} catch (HibernateException e1) {
			log.error("EntityEditBean exception:",e1);
		} catch (PersistenceBeanException e1) {
			log.error("EntityEditBean exception:",e1);
		}

		try {
			for(CostItemAsse3 asse3 :budget.getItemsAsse3()){
				asse3.setPartnersBudgets(budget);
				BeansFactory.CostItemAsse3().Save(asse3);
			}
			
		} catch (HibernateException e) {
			if (tr != null) {
				tr.rollback();
			}
			log.error("EntityEditBean exception:",e);
		} catch (PersistenceBeanException e) {
			if (tr != null) {
				tr.rollback();
			}
			log.error("EntityEditBean exception:",e);
		} catch (NumberFormatException e) {
			if (tr != null) {
				tr.rollback();
			}
			e.printStackTrace();
			log.error("EntityEditBean exception:",e);
		} catch (Exception e) {
			if (tr != null) {
				tr.rollback();
			}
			log.error("EntityEditBean exception:",e);
		} finally {
			if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
				tr.commit();
			}
		}
	}
	
	private boolean checkTotalValueBudgetAsse3(PartnersBudgets budget){
		return this.getCurrentGeneralBudget().getBudgetTotal().equals(CalculateTotalSumForAsse3(budget));
	}
	
	private Double CalculateTotalSumForAsse3(PartnersBudgets budget)
	{
		Double totalsum = 0d;

		for (CostItemAsse3 item : budget.getItemsAsse3())
		{
			totalsum += NumberHelper.Round(item.getValue());
		}

		return NumberHelper.Round(totalsum);
	}
	
	private boolean checkChangesBudgetAsse3(PartnersBudgets budget, PartnersBudgets oldBudget)
			throws PersistenceBeanException
	{
		for (CostItemAsse3 item : oldBudget.getItemsAsse3())
		{

			for (CostItemAsse3 itemChanged : budget
					.getItemsAsse3())
			{
				if (item.getCostPhase().getId().equals(itemChanged.getCostPhase().getId()) && item.getYear().equals(itemChanged.getYear()))
				{
					if (!item.getValue().equals(itemChanged.getValue()))
					{
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
	
	private PartnersBudgets createCopyForAsse3(PartnersBudgets budget){
		PartnersBudgets newBudget = new PartnersBudgets();

		newBudget.setCfPartnerAnagraph(budget
				.getCfPartnerAnagraph());
		newBudget.setCreateDate(budget.getCreateDate());
		newBudget.setDeleted(Boolean.FALSE);
		newBudget.setDescription(budget.getDescription());
		newBudget.setIsOld(Boolean.FALSE);

		newBudget.setProject(budget.getProject());
		newBudget.setQuotaPrivate(budget.getQuotaPrivate());
		newBudget.setTotalAmount(budget.getTotalAmount());
		newBudget.setTotalRow(budget.isTotalRow());
		newBudget.setYear(budget.getYear());
		newBudget.setBudgetRelease(budget.getBudgetRelease());

		newBudget.setItemsAsse3(new ArrayList<CostItemAsse3>());
		
		for (CostItemAsse3 asse3 : budget.getItemsAsse3())
		{
			CostItemAsse3 newAsse3 = new CostItemAsse3();
			newAsse3.setCostPhase(asse3.getCostPhase());
			newAsse3.setValue(asse3.getValue());
			newAsse3.setYear(asse3.getYear());
			newBudget.getItemsAsse3().add(newAsse3);
		}
		
		return newBudget;
	}
	
	public boolean BeforeSavePartner() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		if (this.getCurrentGeneralBudget().getBudgetTotal() == null)
		{
			return false;
		}
		boolean isCorrect = this.getCurrentGeneralBudget().getBudgetTotal()
				.equals(NumberHelper.Round(this.CalculateTotalSumPartner()));

		double[] cost = new double[22];

		for (Phase phase : this.getListPB().get(this.getListPB().size() - 1)
				.getPhaseBudgets())
		{
			cost[1] = cost[1] + phase.getPartHumanRes();
			cost[2] = cost[2] + phase.getPartProvisionOfService();
			cost[3] = cost[3] + phase.getPartMissions();
			cost[4] = cost[4] + phase.getPartDurables();
			cost[5] = cost[5] + phase.getPartDurableGoods();
			cost[6] = cost[6] + phase.getPartAdvInfoPublicEvents();
			cost[7] = cost[7] + phase.getPartAdvInfoProducts();
			cost[8] = cost[8] + phase.getPartOverheads();
			cost[9] = cost[9] + phase.getPartGeneralCosts();
			cost[10] = cost[10] + phase.getPartOther();
			cost[11] = cost[11] + phase.getPartContainer();
			cost[12] = cost[12] + phase.getPartPersonalExpenses();
			cost[13] = cost[13] + phase.getPartCommunicationAndInformation();
			cost[14] = cost[14] + phase.getPartOrganizationOfCommitees();
			cost[15] = cost[15] + phase.getPartOtherFees();
			cost[16] = cost[16] + phase.getPartAutoCoordsTunis();
			cost[17] = cost[17] + phase.getPartContactPoint();
			cost[18] = cost[18] + phase.getPartSystemControlAndManagement();
			cost[19] = cost[19] + phase.getPartAuditOfOperations();
			cost[20] = cost[20] + phase.getPartAuthoritiesCertification();
			cost[21] = cost[21] + phase.getPartIntermEvaluation();
		}

		for (int i = 1; i < 21; i++)
		{
			CFPartnerAnagraphs partner = null;

			List<CostDefinitions> listCost = new ArrayList<CostDefinitions>();

			if (this.getSessionBean().isCF()
					|| this.getSessionBean().getProjectLock()
					&& this.getSessionBean().isSTCW())
			{
				partner = BeansFactory.CFPartnerAnagraphs().Load(
						this.getSelectedPartner());
				if (partner.getUser() == null && this.getProjectAsse() == 5)
				{
					listCost = BeansFactory.CostDefinitions().GetAgus(
							this.getProjectId(), String.valueOf(i), null);
				}
				else
				{
					listCost = BeansFactory.CostDefinitions().GetByValues(
							this.getProjectId(), String.valueOf(i), null,
							partner.getUser().getId());
				}
			}
			else if (this.getSessionBean().isAGU())
			{
				partner = BeansFactory.CFPartnerAnagraphs().Load(
						this.getSelectedPartner());
				CFPartnerAnagraphProject partnerForProject = BeansFactory
						.CFPartnerAnagraphProject().LoadByPartner(
								Long.parseLong(this.getProjectId()),
								Long.parseLong(this.getSelectedPartner()));
				if (partnerForProject.getType().getId()
						.equals(CFAnagraphTypes.CFAnagraph.getCfType()))
				{
					listCost = BeansFactory.CostDefinitions().GetAgus(
							this.getProjectId(), String.valueOf(i), null);
				}
				else
				{
					listCost = BeansFactory.CostDefinitions().GetByValues(
							this.getProjectId(), String.valueOf(i), null,
							partner.getUser().getId());
				}
			}

			double sum = 0d;
			for (CostDefinitions item : listCost)
			{
				if (item.getCostState() != null
						&& item.getCostState().equals(
								BeansFactory.CostStates().Load(
										CostStateTypes.Closed.getState()))
						&& !item.getWasRefusedByCil())
				{
					continue;
				}
				if (item.getCostState().equals(
						BeansFactory.CostStates().Load(
								CostStateTypes.Draft.getState())))
				{
					continue;
				}
				if (item.getAcuCertification() != null
						&& item.getACUSertified())
				{
					sum += item.getAcuCertification();
				}
				else if (item.getAguCertification() != null
						&& item.getAGUSertified())
				{
					sum += item.getAguCertification();
				}
				else if (item.getStcCertification() != null
						&& item.getSTCSertified())
				{
					sum += item.getStcCertification();
				}
				else if (item.getCfCheck() != null)
				{
					sum += item.getCfCheck();
				}
				else if (item.getCilCheck() != null
						&& !item.getCilIntermediateStepSave())
				{
					sum += item.getCilCheck();
				}
				else
				{
					sum += item.getAmountReport();
				}
			}
			if (NumberHelper.Round(sum) > cost[i])
			{
				StringBuilder sb = new StringBuilder();
				sb.append(Utils.getString("budgetCannot"));
				sb.append(" \"");
				sb.append(Utils.getString("partnerBudgetCostItem" + i));
				sb.append("\" ");
				sb.append(Utils.getString("budgetAlreadyUsed"));
				this.setStringLessThanCost(sb.toString());
				this.setSpanLessThanCost("");
				return false;
			}
			else
			{
				this.setSpanLessThanCost("none");
			}
		}

		if (isCorrect)
		{
			this.setSpanPartnerValidatorVisibility("none");
		}
		else
		{
			this.setSpanPartnerValidatorVisibility("");
		}

		return isCorrect;
	}

	public void AfterSavePartner()
	{
		// Empty
	}

	@SuppressWarnings("unchecked")
	public void SaveEntityPartner() throws PersistenceBeanException
	{
		if (this.getViewState().get("tempPartnerBudget") != null)
		{
			this.getListPB().clear();
			this.getListPB().addAll(
					(List<PartnersBudgets>) this.getViewState().get(
							"tempPartnerBudget"));
			this.getViewState().put("tempPartnerBudget", null);
		}

		if (this.CheckModificationsPartner())
		{
			List<PartnersBudgets> listPartnersBudgets = BeansFactory
					.PartnersBudgets().GetByProjectAndPartner(
							this.getProjectId(), this.getSelectedPartner(),
							false);

			// Set flag OLD for all items previously saved
			for (PartnersBudgets pb : listPartnersBudgets)
			{
				pb.setIsOld(true);
				BeansFactory.PartnersBudgets().Save(pb);
			}

			List<PartnersBudgets> listToSave = new ArrayList<PartnersBudgets>();

			for (PartnersBudgets pb : this.getListPB())
			{
				listToSave.add(this.CopyInfo(pb));
			}

			CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
					.Load(this.getSelectedPartner());
			Long maxVersion = BeansFactory.PartnersBudgets()
					.GetLastVersionNumber(this.getProjectId(),
							this.getSelectedPartner(), null);

			if (maxVersion == null)
			{
				maxVersion = 0l;
			}

			List<PartnersBudgets> listOld = BeansFactory.PartnersBudgets()
					.GetLastByPartner(this.getProjectId(),
							this.getSelectedPartner());

			double diff = this.GetDifference(listOld);

			for (PartnersBudgets pb : listToSave)
			{
				pb.setCounter(diff);
				pb.setApproved(false);
				pb.setDenied(false);
				pb.setCfPartnerAnagraph(partner);
				pb.setCreateDate(DateTime.getNow());
				pb.setDeleted(false);
				pb.setIsOld(false);
				pb.setProject(this.getProject());
				pb.setVersion(maxVersion + 1);

				BeansFactory.PartnersBudgets().Save(pb);
				for (Phase phase : pb.getPhaseBudgets())
				{
					phase.setPartnersBudgets(pb);
					BeansFactory.PartnersBudgetsPhases().Save(phase);
				}
			}
		}
		else
		{
			List<PartnersBudgets> listOld = BeansFactory.PartnersBudgets()
					.GetByProjectAndPartner(this.getProjectId(),
							this.getSelectedPartner(), true);

			if (listOld == null || listOld.isEmpty())
			{
				List<PartnersBudgets> listToSave = new ArrayList<PartnersBudgets>();

				for (PartnersBudgets pb : this.getListPB())
				{
					listToSave.add(this.CopyInfo(pb));
				}

				CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
						.Load(this.getSelectedPartner());
				Long maxVersion = BeansFactory.PartnersBudgets()
						.GetLastVersionNumber(this.getProjectId(),
								this.getSelectedPartner(), null);

				if (maxVersion == null)
				{
					maxVersion = 0l;
				}

				for (PartnersBudgets pb : listToSave)
				{
					pb.setApproved(false);
					pb.setDenied(false);
					pb.setCfPartnerAnagraph(partner);
					pb.setCreateDate(DateTime.getNow());
					pb.setDeleted(false);
					pb.setIsOld(false);
					pb.setProject(this.getProject());
					pb.setVersion(maxVersion + 1);

					BeansFactory.PartnersBudgets().Save(pb);

					for (Phase phase : pb.getPhaseBudgets())
					{
						phase.setPartnersBudgets(pb);
						BeansFactory.PartnersBudgetsPhases().Save(phase);
					}
				}
			}
		}

		CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs().Load(
				this.getSelectedPartner());

		List<InfoObject> listBCM = new ArrayList<InfoObject>();		
		if (this.getProjectAsse() != 5)
		{
			for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.AGU))
			{
				InfoObject info = new InfoObject();
				info.setName(partner.getUser() != null ? partner.getUser()
						.getName() : partner.getName());
				info.setSurname(partner.getUser() != null ? partner.getUser()
						.getSurname() : "");
				info.setMail(item.getMail());
				info.setProjectName(this.getProject().getTitle());
				if (partner.getUser() != null)
				{
					ControllerUserAnagraph cua = BeansFactory
							.ControllerUserAnagraph().GetByUser(
									partner.getUser().getId());
					if (cua != null)
					{
						info.setDenomination(cua.getDenomination() == null ? ""
								: cua.getDenomination());
					}
				}
				else
				{
					info.setDenomination("");
				}
				listBCM.add(info);
			}
		}
		for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.STC))
		{
			InfoObject info = new InfoObject();
			info.setName(partner.getUser() != null ? partner.getUser()
					.getName() : partner.getName());
			info.setSurname(partner.getUser() != null ? partner.getUser()
					.getSurname() : "");
			if (partner.getUser() != null)
			{
				ControllerUserAnagraph cua = BeansFactory
						.ControllerUserAnagraph().GetByUser(
								partner.getUser().getId());
				if (cua != null)
				{
					info.setDenomination(cua.getDenomination() == null ? ""
							: cua.getDenomination());
				}

			}
			else
			{
				info.setDenomination("");
			}
			info.setMail(item.getMail());
			info.setProjectName(this.getProject().getTitle());
			listBCM.add(info);
		}

		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();
			BudgetChangedMailSender budgetChangedMailSender = new BudgetChangedMailSender(
					listBCM);
			mails.addAll(budgetChangedMailSender
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
		this.setSpanPBSaved("");
	}

	private PartnersBudgets CopyInfo(PartnersBudgets entityToCopy)
	{
		PartnersBudgets newEntity = new PartnersBudgets();
		newEntity.setPhaseBudgets(new ArrayList<Phase>());

		newEntity.setCfPartnerAnagraph(entityToCopy.getCfPartnerAnagraph());
		newEntity.setCreateDate(entityToCopy.getCreateDate());
		newEntity.setDeleted(entityToCopy.getDeleted());
		newEntity.setDescription(entityToCopy.getDescription());
		newEntity.setIsOld(entityToCopy.getIsOld());

		for (Phase phaseToCopy : entityToCopy.getPhaseBudgets())
		{
			Phase newPhase = new Phase();
			newPhase.setPartAdvInfoProducts(phaseToCopy
					.getPartAdvInfoProducts());
			newPhase.setPartAdvInfoPublicEvents(phaseToCopy
					.getPartAdvInfoPublicEvents());
			newPhase.setPartDurableGoods(phaseToCopy.getPartDurableGoods());
			newPhase.setPartDurables(phaseToCopy.getPartDurables());
			newPhase.setPartHumanRes(phaseToCopy.getPartHumanRes());
			newPhase.setPartMissions(phaseToCopy.getPartMissions());
			newPhase.setPartOther(phaseToCopy.getPartOther());
			newPhase.setPartOverheads(phaseToCopy.getPartOverheads());
			newPhase.setPartGeneralCosts(phaseToCopy.getPartGeneralCosts());
			newPhase.setPartProvisionOfService(phaseToCopy
					.getPartProvisionOfService());
			
			newPhase.setPartContainer(phaseToCopy.getPartContainer());
			newPhase.setPartPersonalExpenses(phaseToCopy.getPartPersonalExpenses());
			newPhase.setPartCommunicationAndInformation(phaseToCopy.getPartCommunicationAndInformation());
			newPhase.setPartOrganizationOfCommitees(phaseToCopy.getPartOrganizationOfCommitees());
			newPhase.setPartOtherFees(phaseToCopy.getPartOtherFees());
			newPhase.setPartAutoCoordsTunis(phaseToCopy.getPartAutoCoordsTunis());
			newPhase.setPartContactPoint(phaseToCopy.getPartContactPoint());
			newPhase.setPartSystemControlAndManagement(phaseToCopy.getPartSystemControlAndManagement());
			newPhase.setPartAuditOfOperations(phaseToCopy.getPartAuditOfOperations());
			newPhase.setPartAuthoritiesCertification(phaseToCopy.getPartAuthoritiesCertification());
			newPhase.setPartIntermEvaluation(phaseToCopy.getPartIntermEvaluation());
			
			newPhase.setPhase(phaseToCopy.getPhase());
			
			newEntity.getPhaseBudgets().add(newPhase);
		}
		newEntity.setProject(entityToCopy.getProject());
		newEntity.setTotalAmount(entityToCopy.getTotalAmount());
		newEntity.setYear(entityToCopy.getYear());
		newEntity.setBudgetRelease(entityToCopy.getBudgetRelease());

		return newEntity;
	}

	private Double CalculateTotalSumPartner() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		Double totalsum = 0d;

		for (PartnersBudgets pb : this.getListPB())
		{
			Double sum = 0d;
			for (Phase phase : pb.getPhaseBudgets())
			{
				sum += phase.getPartAdvInfoProducts();
				sum += phase.getPartAdvInfoPublicEvents();
				sum += phase.getPartDurableGoods();
				sum += phase.getPartDurables();
				sum += phase.getPartHumanRes();
				sum += phase.getPartMissions();
				sum += phase.getPartOther();
				sum += phase.getPartOverheads();
				sum += phase.getPartGeneralCosts();
				sum += phase.getPartProvisionOfService();
				sum += phase.getPartContainer();
				sum += phase.getPartPersonalExpenses();
				sum += phase.getPartCommunicationAndInformation();
				sum += phase.getPartOrganizationOfCommitees();
				sum += phase.getPartOtherFees();
				sum += phase.getPartAutoCoordsTunis();
				sum += phase.getPartContactPoint();
				sum += phase.getPartSystemControlAndManagement();
				sum += phase.getPartAuditOfOperations();
				sum += phase.getPartAuthoritiesCertification();
				sum += phase.getPartIntermEvaluation();
			}
			sum += pb.getBudgetRelease();
			pb.setTotalAmount(sum);
			totalsum += NumberHelper.Round(sum);
		}
		
		return NumberHelper.Round(totalsum);
	}

	private boolean CheckModificationsPartner()
	{
		List<PartnersBudgets> listPartnersBudgetsOld = new ArrayList<PartnersBudgets>();

		try
		{
			listPartnersBudgetsOld = BeansFactory.PartnersBudgets()
					.GetByProjectAndPartner(this.getProjectId(),
							this.getSelectedPartner(), true);
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (listPartnersBudgetsOld == null || listPartnersBudgetsOld.isEmpty())
		{
			return true;
		}

		boolean isModified = false;

		if (listPartnersBudgetsOld.size() != this.getListPB().size() - 1)
		{
			return true;
		}

		for (int i = 0; i < this.getPic().getYearDuration(); i++)
		{
			PartnersBudgets arg1 = listPartnersBudgetsOld.get(i);
			PartnersBudgets arg2 = this.getListPB().get(i);

			for (int j = 0; j < arg1.getPhaseBudgets().size(); j++)
			{
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartAdvInfoProducts()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartAdvInfoProducts()))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}
				if (!arg1.getPhaseBudgets().get(j)
						.equals(arg2.getPhaseBudgets().get(j)))
				{
					isModified = true;
					break;
				}	
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartProvisionOfService()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartProvisionOfService()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartContainer()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartContainer()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartCommunicationAndInformation()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartCommunicationAndInformation()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartOrganizationOfCommitees()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartOrganizationOfCommitees()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartPersonalExpenses()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartPersonalExpenses()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartOtherFees()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartOtherFees()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartAutoCoordsTunis()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartAutoCoordsTunis()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartContactPoint()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartContactPoint()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartSystemControlAndManagement()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartSystemControlAndManagement()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartAuditOfOperations()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartAuditOfOperations()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartAuthoritiesCertification()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartAuthoritiesCertification()))
				{
					isModified = true;
					break;
				}
				if (!arg1
						.getPhaseBudgets()
						.get(j)
						.getPartIntermEvaluation()
						.equals(arg2.getPhaseBudgets().get(j)
								.getPartIntermEvaluation()))
				{
					isModified = true;
					break;
				}
			}

		}

		return isModified;
	}
		
	private DataStructureTab createEditTabForPb(PartnersBudgets budget) throws PersistenceBeanException {
		DataStructureTab tab = new DataStructureTab();
		tab.setTables(new ArrayList<DataStructureTable>());
		tab.setBudget(budget);

		for (PhaseAsse3Types asseType : PhaseAsse3Types.values()) {
			DataStructureTable table = new DataStructureTable();
			table.setRows(new ArrayList<DataStructureRow>());

			for (String year : YEARS.split(", ")) {
				DataStructureRow row = new DataStructureRow();
				row.setItems(new ArrayList<CostItemAsse3>());

				List<CostAsse3> costItemTypes = new ArrayList<CostAsse3>();

				for (CostAsse3 item : this.getLegend()) {
					if (asseType.equals(item.getPhase())) {
						costItemTypes.add(item);
					}
				}

				for (CostAsse3 item : costItemTypes) {
					for (CostItemAsse3 asse3 : budget.getItemsAsse3()) {
						if (asse3.getYear().equals(year) && asse3.getCostPhase().getId().equals(item.getId())) {
							row.getItems().add(asse3);
							break;
						}
					}
				}
				table.getRows().add(row);
			}
			table.getRows().add(createTotalRow(table));
			tab.getTables().add(table);
		}

//		for (int i = 0; i < tab.getTables().size(); i++) {
//			DataStructureTable d = tab.getTables().get(i);
//			if (d == null) {
//				tab.getTables().remove(i);
//			}
//		}
		return tab;
	}
	
	private DataStructureRow createTotalRow(DataStructureTable table) {
		DataStructureRow totalRow = new DataStructureRow();
		totalRow.setTotal(Boolean.TRUE);
		totalRow.setItems(new ArrayList<CostItemAsse3>());
		for (@SuppressWarnings("unused")
		DataStructureColumn col : table.getRows().get(0).getColumns()) {
			CostItemAsse3 item = new CostItemAsse3();
			item.setYear(Utils.getString("Resources", "partnerBudgetFooterTotal", null));
			totalRow.getItems().add(item);
		}
		for (DataStructureRow rowInTable : table.getRows()) {
			for (int i = 0; i < table.getRows().get(0).getColumns().size(); i++) {
				totalRow.getItems().get(i).setValue(
						(totalRow.getItems().get(i).getValue() == null ? 0d : totalRow.getItems().get(i).getValue())
								+ (rowInTable.getItems().size() > 0 ? rowInTable.getItems().get(i).getValue() : 0d));
			}
		}
		return totalRow;

	}
	
	private void LoadBudget() throws PersistenceBeanException
	{
		if (this.getSelectedPartnerStc() == null)
		{
			if (this.getCf() && this.getCurCF() != null)
			{
				this.setSelectedPartnerStc(this.getCurCF().getId().toString());
			}
			else
			{
				this.setSelectedPartnerStc(this.listPartners.get(0).getId()
						.toString());
			}
		}

		List<PartnersBudgets> listPartnersBudgets = BeansFactory
				.PartnersBudgets().GetByPartner(this.getProjectId(),
						this.getSelectedPartnerStc(), true);

		for (PartnersBudgets item : listPartnersBudgets)
		{
			item.setPhaseBudgets(BeansFactory.PartnersBudgetsPhases()
					.loadByPartnerBudget(item.getId()));
		}

		List<PartnersBudgets> lastNonApprovedPB = BeansFactory
				.PartnersBudgets().GetLastNonApprovedByPartner(
						this.getProjectId(), this.getSelectedPartnerStc());

		for (PartnersBudgets item : lastNonApprovedPB)
		{
			item.setPhaseBudgets(BeansFactory.PartnersBudgetsPhases()
					.loadByPartnerBudget(item.getId()));
		}

		if (lastNonApprovedPB != null)
		{
			lastNonApprovedPB.addAll(listPartnersBudgets);
			listPartnersBudgets = lastNonApprovedPB;
		}

		fillPartnerBudgetStcWeb(listPartnersBudgets);

		this.getListPB().clear();
		for (PartnersBudgets pb : listPartnersBudgets)
		{
			if (pb.getVersion().equals(Long.valueOf(this.getSelectedBudget())))
			{
				this.getListPB().add(pb);
				this.setApproveAvailable(!pb.getApproved()
						&& (pb.getDenied() == null || !pb.getDenied()));
				this.setDenyAvailable(!pb.getApproved()
						&& (pb.getDenied() == null || !pb.getDenied()));
				pb.setDescription(Utils.getString("Resources",
						"partnerBudgetFooterTotal", null));
			}
		}

		if (!IsBudgetActual(this.getListPB(),
				Long.parseLong(this.getSelectedPartnerStc())))
		{
			this.setApproveAvailable(false);
			this.setDenyAvailable(false);
			this.setStcCannotApprove(Utils.getString("stcCannotApproveBudget"));
		}
		else
		{
			this.setStcCannotApprove(null);
		}
		if(this.getAcce3()){
			this.setTabForAsse3(createEditTabForPb(this.getListPB().get(0)));
		}

		Collections.sort(this.getListPB(), new PartnerBudgetYearComparer());
	}

	/**
	 * @param listPartnersBudgets
	 */
	private void fillPartnerBudgetStcWeb(
			List<PartnersBudgets> listPartnersBudgets)
	{
		this.listPartnerBudgetsStcWeb = new ArrayList<SelectItem>();

		boolean isChanged = true;

		String App = Utils.getString("Resources", "budgetEditApproved", null);
		String notApp = Utils.getString("Resources", "budgetEditNotApproved",
				null);
		String Rej = Utils.getString("Resources", "budgetEditRejected", null);

		PartnersBudgets prevEnt = null;
		for (PartnersBudgets pb : listPartnersBudgets)
		{
			if (prevEnt != null)
			{
				isChanged = !prevEnt.getVersion().equals(pb.getVersion());
			}

			if (isChanged)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(DateTime.ToStringWithMinutes(pb.getCreateDate()));
				sb.append(" (");
				if (pb.getApproved())
				{
					sb.append(App);
				}
				else if (pb.getDenied() != null && pb.getDenied())
				{
					sb.append(Rej);
				}
				else
				{
					sb.append(notApp);
				}

				sb.append(")");

				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(pb.getVersion()));
				item.setLabel(sb.toString());

				this.listPartnerBudgetsStcWeb.add(item);
			}

			prevEnt = pb;
		}

		if (this.getSelectedBudget() == null)
		{
			if (!this.listPartnerBudgetsStcWeb.isEmpty())
			{
				this.setSelectedBudget(String
						.valueOf(this.listPartnerBudgetsStcWeb.get(0)
								.getValue()));
			}
		}
		else
		{
			if (this.getPartnerChangedFlag())
			{
				this.setSelectedBudget(String
						.valueOf(this.listPartnerBudgetsStcWeb.get(0)
								.getValue()));
				this.getSession().put("partnerChanges", true);
				this.setPartnerChangedFlag(false);
			}
		}
	}

	public boolean IsBudgetActual(List<PartnersBudgets> listPartnersBudgets,
			Long partnerId) throws NumberFormatException,
			PersistenceBeanException
	{
		double sum = 0d;
		for (PartnersBudgets pb : listPartnersBudgets)
		{
			sum += pb.getTotalAmount();
		}

		List<GeneralBudgets> genBudgetList = BeansFactory.GeneralBudgets()
				.GetActualItemsForCFAnagraph(this.getProjectId(), partnerId);
		if (!genBudgetList.isEmpty()
				&& genBudgetList.get(0).getBudgetTotal() != null
				&& NumberHelper.Round(genBudgetList.get(0).getBudgetTotal()) != NumberHelper
						.Round(sum))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void RecalculatePartnerBudget() throws PersistenceBeanException
	{
		PartnersBudgets totalEntity = new PartnersBudgets();
		totalEntity.setPhaseBudgets(new ArrayList<Phase>());

		for (CostDefinitionPhases cdPhase : BeansFactory.CostDefinitionPhase()
				.Load())
		{
			Phase phase = new Phase();
			phase.setPartAdvInfoProducts(0d);
			phase.setPartAdvInfoPublicEvents(0d);
			phase.setPartDurableGoods(0d);
			phase.setPartDurables(0d);
			phase.setPartHumanRes(0d);
			phase.setPartMissions(0d);
			phase.setPartOther(0d);
			phase.setPartOverheads(0d);
			phase.setPartProvisionOfService(0d);
			phase.setPartGeneralCosts(0d);
			
			phase.setPartContainer(0d);
			phase.setPartPersonalExpenses(0d);
			phase.setPartCommunicationAndInformation(0d);
			phase.setPartOrganizationOfCommitees(0d);
			phase.setPartOtherFees(0d);
			phase.setPartAutoCoordsTunis(0d);
			phase.setPartContactPoint(0d);
			phase.setPartSystemControlAndManagement(0d);
			phase.setPartAuditOfOperations(0d);
			phase.setPartAuthoritiesCertification(0d);
			phase.setPartIntermEvaluation(0d);
			
			phase.setPhase(cdPhase);
			phase.setPartnersBudgets(totalEntity);
			totalEntity.getPhaseBudgets().add(phase);
		}
		totalEntity.setTotalAmount(0d);
		totalEntity.setBudgetRelease(0d);
		totalEntity.setDescription(Utils.getString("Resources",
				"partnerBudgetFooterTotal", null));
		totalEntity.setTotalRow(true);

		for (PartnersBudgets currEntity : this.getListPB())
		{
			currEntity.setDescription(Utils.getString("Resources",
					"partnerBudgetYear", null)
					+ " "
					+ String.valueOf(currEntity.getYear()));
			currEntity.setTotalRow(false);
			if (currEntity.getBudgetRelease() == null)
			{
				currEntity.setBudgetRelease(0d);
			}

			for (int j = 0; j < totalEntity.getPhaseBudgets().size(); j++)
			{
				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartAdvInfoProducts(
								totalEntity.getPhaseBudgets().get(j)
										.getPartAdvInfoProducts()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartAdvInfoProducts());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartAdvInfoPublicEvents(
								totalEntity.getPhaseBudgets().get(j)
										.getPartAdvInfoPublicEvents()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartAdvInfoPublicEvents());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartDurableGoods(
								totalEntity.getPhaseBudgets().get(j)
										.getPartDurableGoods()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartDurableGoods());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartDurables(
								totalEntity.getPhaseBudgets().get(j)
										.getPartDurables()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartDurables());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartHumanRes(
								totalEntity.getPhaseBudgets().get(j)
										.getPartHumanRes()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartHumanRes());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartMissions(
								totalEntity.getPhaseBudgets().get(j)
										.getPartMissions()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartMissions());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartOther(
								totalEntity.getPhaseBudgets().get(j)
										.getPartOther()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartOther());

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartOverheads(
								totalEntity.getPhaseBudgets().get(j)
										.getPartOverheads()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartOverheads());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartGeneralCosts(
						totalEntity.getPhaseBudgets().get(j)
								.getPartGeneralCosts()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartGeneralCosts());				

				totalEntity
						.getPhaseBudgets()
						.get(j)
						.setPartProvisionOfService(
								totalEntity.getPhaseBudgets().get(j)
										.getPartProvisionOfService()
										+ currEntity.getPhaseBudgets().get(j)
												.getPartProvisionOfService());
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartContainer(
						totalEntity.getPhaseBudgets().get(j)
								.getPartContainer()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartContainer());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartPersonalExpenses(
						totalEntity.getPhaseBudgets().get(j)
								.getPartPersonalExpenses()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartPersonalExpenses());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartCommunicationAndInformation(
						totalEntity.getPhaseBudgets().get(j)
								.getPartCommunicationAndInformation()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartCommunicationAndInformation());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartOrganizationOfCommitees(
						totalEntity.getPhaseBudgets().get(j)
								.getPartOrganizationOfCommitees()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartOrganizationOfCommitees());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartOtherFees(
						totalEntity.getPhaseBudgets().get(j)
								.getPartOtherFees()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartOtherFees());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartAutoCoordsTunis(
						totalEntity.getPhaseBudgets().get(j)
								.getPartAutoCoordsTunis()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartAutoCoordsTunis());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartContactPoint(
						totalEntity.getPhaseBudgets().get(j)
								.getPartContactPoint()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartContactPoint());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartSystemControlAndManagement(
						totalEntity.getPhaseBudgets().get(j)
								.getPartSystemControlAndManagement()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartSystemControlAndManagement());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartAuditOfOperations(
						totalEntity.getPhaseBudgets().get(j)
								.getPartAuditOfOperations()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartAuditOfOperations());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartAuthoritiesCertification(
						totalEntity.getPhaseBudgets().get(j)
								.getPartAuthoritiesCertification()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartAuthoritiesCertification());
				
				totalEntity
				.getPhaseBudgets()
				.get(j)
				.setPartIntermEvaluation(
						totalEntity.getPhaseBudgets().get(j)
								.getPartIntermEvaluation()
								+ currEntity.getPhaseBudgets().get(j)
										.getPartIntermEvaluation());
				
				
			}

		}

		for (Phase phase : totalEntity.getPhaseBudgets())
		{

			totalEntity.setTotalAmount(totalEntity.getTotalAmount()
					+ phase.getPartAdvInfoProducts()
					+ phase.getPartAdvInfoPublicEvents()
					+ phase.getPartDurableGoods() + phase.getPartDurables()
					+ phase.getPartHumanRes() + phase.getPartMissions()
					+ phase.getPartOther() + phase.getPartOverheads() + phase.getPartGeneralCosts()
					+ phase.getPartProvisionOfService() + phase.getPartContainer() 
					+ phase.getPartPersonalExpenses() + phase.getPartCommunicationAndInformation()
					+ phase.getPartOrganizationOfCommitees() + phase.getPartOtherFees() 
					+ phase.getPartAutoCoordsTunis() + phase.getPartContactPoint() 
					+ phase.getPartSystemControlAndManagement() + phase.getPartAuditOfOperations() 
					+ phase.getPartAuthoritiesCertification() + phase.getPartIntermEvaluation());
		}
		totalEntity.setTotalAmount(totalEntity.getTotalAmount()
				+ totalEntity.getBudgetRelease());

		this.getListPB().add(totalEntity);
	}

	public void Approve_Budget() throws HibernateException,
			PersistenceBeanException
	{
		if (!CheckModificationsApprove())
		{
			this.SaveApprovedBudget(true);
		}
		else
		{
			this.setShowConfirmationMessage(true);
		}
	}

	public void Deny_Budget()
	{
		try
		{
			this.SaveApprovedBudget(false);
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

	public void SaveApprovedBudget(boolean approve) throws HibernateException,
			PersistenceBeanException
	{
		for (PartnersBudgets pb : this.getListPB())
		{
			if (approve)
			{
				pb.setApproved(true);

				mailsBudgetApproved = new ArrayList<InfoObject>();
				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
						.getCfPartnerAnagraph().getName() : pb
						.getCfPartnerAnagraph().getUser().getName());
				ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? ""
						: pb.getCfPartnerAnagraph().getUser().getSurname());
				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());
					ob.setProjectName(this.getProject().getTitle());
					mailsBudgetApproved.add(ob);
				}
				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.AGU))
				{
					ob = new InfoObject();
					ob.setProjectName(this.getProject().getTitle());
					ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
							.getCfPartnerAnagraph().getName() : pb
							.getCfPartnerAnagraph().getUser().getName());
					ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? ""
							: pb.getCfPartnerAnagraph().getUser().getSurname());
					ob.setMail(item.getMail());
					ob.setProjectName(this.getProject().getTitle());
					mailsBudgetApproved.add(ob);
				}
			}
			else
			{
				pb.setDenied(true);
				pb.setCounter(0d);

				mailsBudgetRefused = new ArrayList<InfoObject>();
				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
						.getCfPartnerAnagraph().getName() : pb
						.getCfPartnerAnagraph().getUser().getName());
				ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? ""
						: pb.getCfPartnerAnagraph().getUser().getSurname());
				if (pb.getCfPartnerAnagraph().getUser() != null)
				{
					ControllerUserAnagraph cua = BeansFactory
							.ControllerUserAnagraph()
							.GetByUser(
									pb.getCfPartnerAnagraph().getUser().getId());
					ob.setDenomination(cua==null? "" :(cua.getDenomination() == null ? "" : cua
							.getDenomination()));
				}
				else
				{
					ob.setDenomination("");
				}
				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());
					ob.setProjectName(this.getProject().getTitle());
					mailsBudgetRefused.add(ob);
				}

				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.AGU))
				{
					ob = new InfoObject();
					ob.setProjectName(this.getProject().getTitle());
					ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
							.getCfPartnerAnagraph().getName() : pb
							.getCfPartnerAnagraph().getUser().getName());
					ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? ""
							: pb.getCfPartnerAnagraph().getUser().getSurname());
					if (pb.getCfPartnerAnagraph().getUser() != null)
					{
						ControllerUserAnagraph cua = BeansFactory
								.ControllerUserAnagraph().GetByUser(
										pb.getCfPartnerAnagraph().getUser()
												.getId());
						ob.setDenomination(cua==null? "" :cua.getDenomination() == null ? ""
								: cua.getDenomination());
					}
					else
					{
						ob.setDenomination("");
					}
					ob.setMail(item.getMail());
					ob.setProjectName(this.getProject().getTitle());
					mailsBudgetRefused.add(ob);
				}
			}
			BeansFactory.PartnersBudgets().Save(pb);
		}

		this.setSpanApprovedVisibility("");
		this.setShowConfirmationMessage(false);
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();
			BudgetApprovedMailSender budgetApprovedMailSender = new BudgetApprovedMailSender(
					mailsBudgetApproved);
			mails.addAll(budgetApprovedMailSender
					.completeMails(PersistenceSessionManager.getBean()
							.getSession()));
			BudgetRefusedMailSender budgetRefusedMailSender = new BudgetRefusedMailSender(
					mailsBudgetRefused);
			mails.addAll(budgetRefusedMailSender
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
		this.Page_Load();

	}

	public void SaveApprovedBudget() throws HibernateException,
			PersistenceBeanException
	{
		for (PartnersBudgets pb : this.getListPB().subList(0,
				this.getListPB().size() - 1))
		{
			pb.setApproved(true);

			mailsBudgetApproved = new ArrayList<InfoObject>();
			InfoObject ob = new InfoObject();
			ob.setProjectName(this.getProject().getTitle());
			ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
					.getCfPartnerAnagraph().getName() : pb
					.getCfPartnerAnagraph().getUser().getName());
			ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? "" : pb
					.getCfPartnerAnagraph().getUser().getSurname());

			if (pb.getCfPartnerAnagraph().getUser() != null)
			{
				ControllerUserAnagraph cua = BeansFactory
						.ControllerUserAnagraph().GetByUser(
								pb.getCfPartnerAnagraph().getUser().getId());
				if (cua != null)
				{
					ob.setDenomination(cua.getDenomination() == null ? "" : cua
							.getDenomination());
				}
			}
			else
			{
				ob.setDenomination("");
			}

			List<CFPartnerAnagraphs> cpap = BeansFactory.CFPartnerAnagraphs()
					.GetCFAnagraphForProject(this.getProjectId(),
							CFAnagraphTypes.CFAnagraph);
			if (cpap.size() > 0 && cpap.get(0).getUser() != null)
			{
				ob.setMail(cpap.get(0).getUser().getMail());
				ob.setProjectName(this.getProject().getTitle());
				mailsBudgetApproved.add(ob);
			}
			for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.AGU))
			{
				ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setName(pb.getCfPartnerAnagraph().getUser() == null ? pb
						.getCfPartnerAnagraph().getName() : pb
						.getCfPartnerAnagraph().getUser().getName());
				ob.setSurname(pb.getCfPartnerAnagraph().getUser() == null ? ""
						: pb.getCfPartnerAnagraph().getUser().getSurname());
				ob.setMail(item.getMail());
				ob.setProjectName(this.getProject().getTitle());
				if (pb.getCfPartnerAnagraph().getUser() != null)
				{
					ControllerUserAnagraph cua = BeansFactory
							.ControllerUserAnagraph()
							.GetByUser(
									pb.getCfPartnerAnagraph().getUser().getId());
					if (cua != null)
					{
						ob.setDenomination(cua.getDenomination() == null ? ""
								: cua.getDenomination());
					}
				}
				else
				{
					ob.setDenomination("");
				}
				mailsBudgetApproved.add(ob);
			}

			BeansFactory.PartnersBudgets().Save(pb);
		}

		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();
			BudgetApprovedMailSender budgetApprovedMailSender = new BudgetApprovedMailSender(
					mailsBudgetApproved);

			mails.addAll(budgetApprovedMailSender
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
		this.setSpanApprovedVisibility("");
		this.setShowConfirmationMessage(false);
		this.Page_Load();
	}

	public void SaveApprovedBudgetCF() throws HibernateException,
			PersistenceBeanException
	{
		this.setShowConfirmationMessage(false);
		this.SaveEntityPartner();
		this.AfterSavePartner();
	}

	private boolean CheckModificationsApprove() throws NumberFormatException,
			PersistenceBeanException
	{
		List<PartnersBudgets> listOld = BeansFactory.PartnersBudgets()
				.GetByPartnerApproverLessThanVersion(this.getProjectId(),
						this.getSelectedPartnerStc(),
						Long.valueOf(this.getSelectedBudget()), true);

		if (listOld == null || listOld.isEmpty())
		{
			for (PartnersBudgets pb : this.getListPB())
			{
				pb.setCounter(0d);
			}

			return false;
		}
		else
		{
			double diff = this.GetDifference(listOld);

			if (diff > 10)
			{
				for (PartnersBudgets pb : this.getListPB())
				{
					pb.setCounter(0d);
				}

				return true;
			}
			else
			{
				for (PartnersBudgets pb : this.getListPB())
				{
					pb.setCounter(diff);
				}

				return false;
			}
		}
	}

	private boolean CheckModificationsApproveCF() throws NumberFormatException,
			PersistenceBeanException
	{
		List<PartnersBudgets> listOld = BeansFactory.PartnersBudgets()
				.GetByProjectAndPartnerWDenied(this.getProjectId(),
						this.getSelectedPartner(), true);
		if (!listOld.isEmpty() && listOld.get(0).getDenied() == true)
		{
			listOld = BeansFactory.PartnersBudgets()
					.GetByProjectAndPartnerNotDenied(this.getProjectId(),
							this.getSelectedPartner());
		}

		if (listOld == null || listOld.isEmpty())
		{
			for (PartnersBudgets pb : this.getListPB())
			{
				pb.setCounter(0d);
			}

			return false;
		}
		else
		{
			double diff = this.GetDifference(listOld);

			if (diff > 10)
			{
				return true;
			}
			else
			{
				for (PartnersBudgets pb : this.getListPB())
				{
					pb.setCounter(diff);
				}

				return false;
			}
		}
	}

	private double GetDifference(List<PartnersBudgets> listOld)
			throws PersistenceBeanException
	{
		double diff = 0d;

		if (listOld.size() == 0)
		{
			return 0d;
		}

		double item1 = 0d;
		double item2 = 0d;
		double item3 = 0d;
		double item4 = 0d;
		double item5 = 0d;
		double item6 = 0d;
		double item7 = 0d;
		double item8 = 0d;
		double item9 = 0d;
		double item10 = 0d;
		double item11 = 0d;
		double item12 = 0d;
		double item13 = 0d;
		double item14 = 0d;
		double item15 = 0d;
		double item16 = 0d;
		double item17 = 0d;
		double item18 = 0d;
		double item19 = 0d;
		double item20 = 0d;
		double item21 = 0d;		

		for (int i = 0; i < this.getListPB().size() - 1; i++)
		{
			PartnersBudgets oldPB = new PartnersBudgets();
			oldPB.setPhaseBudgets(new ArrayList<Phase>());

			if (listOld.size() <= i)
			{
				for (CostDefinitionPhases cdf : BeansFactory
						.CostDefinitionPhase().Load())
				{
					Phase phase = new Phase();
					phase.setPhase(cdf);
					phase.setPartAdvInfoProducts(0d);
					phase.setPartAdvInfoPublicEvents(0d);
					phase.setPartDurableGoods(0d);
					phase.setPartDurables(0d);
					phase.setPartHumanRes(0d);
					phase.setPartMissions(0d);
					phase.setPartOther(0d);
					phase.setPartOverheads(0d);
					phase.setPartGeneralCosts(0d);
					phase.setPartProvisionOfService(0d);
					
					phase.setPartContainer(0d);
					phase.setPartPersonalExpenses(0d);
					phase.setPartCommunicationAndInformation(0d);
					phase.setPartOrganizationOfCommitees(0d);
					phase.setPartOtherFees(0d);
					phase.setPartAutoCoordsTunis(0d);
					phase.setPartContactPoint(0d);
					phase.setPartSystemControlAndManagement(0d);
					phase.setPartAuditOfOperations(0d);
					phase.setPartAuthoritiesCertification(0d);
					phase.setPartIntermEvaluation(0d);
					
					oldPB.getPhaseBudgets().add(phase);
				}
			}
			else
			{
				oldPB = listOld.get(i);
			}

			PartnersBudgets newPB = this.getListPB().get(i);

			for (int j = 0; j < newPB.getPhaseBudgets().size(); j++)
			{
				Phase newPhase = newPB.getPhaseBudgets().get(j);
				Phase oldPhase = oldPB.getPhaseBudgets().get(j);

				if (NumberHelper.Round(newPhase.getPartAdvInfoProducts()) != NumberHelper
						.Round(oldPhase.getPartAdvInfoProducts()))
				{
					item1 += NumberHelper.Round(newPhase
							.getPartAdvInfoProducts())
							- NumberHelper.Round(oldPhase
									.getPartAdvInfoProducts());
				}

				if (NumberHelper.Round(newPhase.getPartAdvInfoPublicEvents()) != NumberHelper
						.Round(oldPhase.getPartAdvInfoPublicEvents()))
				{
					item2 += NumberHelper.Round(newPhase
							.getPartAdvInfoPublicEvents())
							- NumberHelper.Round(oldPhase
									.getPartAdvInfoPublicEvents());
				}

				if (NumberHelper.Round(newPhase.getPartDurableGoods()) != NumberHelper
						.Round(oldPhase.getPartDurableGoods()))
				{
					item3 += NumberHelper.Round(newPhase.getPartDurableGoods())
							- NumberHelper
									.Round(oldPhase.getPartDurableGoods());
				}

				if (NumberHelper.Round(newPhase.getPartDurables()) != NumberHelper
						.Round(oldPhase.getPartDurables()))
				{
					item4 += NumberHelper.Round(newPhase.getPartDurables())
							- NumberHelper.Round(oldPhase.getPartDurables());
				}

				if (NumberHelper.Round(newPhase.getPartHumanRes()) != NumberHelper
						.Round(oldPhase.getPartHumanRes()))
				{
					item5 += NumberHelper.Round(newPhase.getPartHumanRes())
							- NumberHelper.Round(oldPhase.getPartHumanRes());
				}

				if (NumberHelper.Round(newPhase.getPartMissions()) != NumberHelper
						.Round(oldPhase.getPartMissions()))
				{
					item6 += NumberHelper.Round(newPhase.getPartMissions())
							- NumberHelper.Round(oldPhase.getPartMissions());
				}

				if (NumberHelper.Round(newPhase.getPartOther()) != NumberHelper
						.Round(oldPhase.getPartOther()))
				{
					item7 += NumberHelper.Round(newPhase.getPartOther())
							- NumberHelper.Round(oldPhase.getPartOther());
				}

				if (NumberHelper.Round(newPhase.getPartOverheads()) != NumberHelper
						.Round(oldPhase.getPartOverheads()))
				{
					item8 += NumberHelper.Round(newPhase.getPartOverheads())
							- NumberHelper.Round(oldPhase.getPartOverheads());
				}

				if (NumberHelper.Round(newPhase.getPartProvisionOfService()) != NumberHelper
						.Round(oldPhase.getPartProvisionOfService()))
				{
					item9 += NumberHelper.Round(newPhase
							.getPartProvisionOfService())
							- NumberHelper.Round(oldPhase
									.getPartProvisionOfService());
				}
				
				if (NumberHelper.Round(newPhase.getPartContainer()) != NumberHelper
						.Round(oldPhase.getPartContainer()))
				{
					item10 += NumberHelper.Round(newPhase
							.getPartContainer())
							- NumberHelper.Round(oldPhase
									.getPartContainer());
				}
				
				if (NumberHelper.Round(newPhase.getPartPersonalExpenses()) != NumberHelper
						.Round(oldPhase.getPartPersonalExpenses()))
				{
					item11 += NumberHelper.Round(newPhase
							.getPartPersonalExpenses())
							- NumberHelper.Round(oldPhase
									.getPartPersonalExpenses());
				}
				
				if (NumberHelper.Round(newPhase.getPartCommunicationAndInformation()) != NumberHelper
						.Round(oldPhase.getPartCommunicationAndInformation()))
				{
					item12 += NumberHelper.Round(newPhase
							.getPartCommunicationAndInformation())
							- NumberHelper.Round(oldPhase
									.getPartCommunicationAndInformation());
				}
				
				if (NumberHelper.Round(newPhase.getPartOrganizationOfCommitees()) != NumberHelper
						.Round(oldPhase.getPartOrganizationOfCommitees()))
				{
					item13 += NumberHelper.Round(newPhase
							.getPartOrganizationOfCommitees())
							- NumberHelper.Round(oldPhase
									.getPartOrganizationOfCommitees());
				}
				
				if (NumberHelper.Round(newPhase.getPartOtherFees()) != NumberHelper
						.Round(oldPhase.getPartOtherFees()))
				{
					item14 += NumberHelper.Round(newPhase
							.getPartGeneralCosts())
							- NumberHelper.Round(oldPhase
									.getPartGeneralCosts());
				}
				
				if (NumberHelper.Round(newPhase.getPartOtherFees()) != NumberHelper
						.Round(oldPhase.getPartOtherFees()))
				{
					item15 += NumberHelper.Round(newPhase
							.getPartOtherFees())
							- NumberHelper.Round(oldPhase
									.getPartOtherFees());
				}
				
				if (NumberHelper.Round(newPhase.getPartAutoCoordsTunis()) != NumberHelper
						.Round(oldPhase.getPartAutoCoordsTunis()))
				{
					item16 += NumberHelper.Round(newPhase
							.getPartAutoCoordsTunis())
							- NumberHelper.Round(oldPhase
									.getPartAutoCoordsTunis());
				}
				
				if (NumberHelper.Round(newPhase.getPartContactPoint()) != NumberHelper
						.Round(oldPhase.getPartContactPoint()))
				{
					item17 += NumberHelper.Round(newPhase
							.getPartContactPoint())
							- NumberHelper.Round(oldPhase
									.getPartContactPoint());
				}
				
				if (NumberHelper.Round(newPhase.getPartSystemControlAndManagement()) != NumberHelper
						.Round(oldPhase.getPartSystemControlAndManagement()))
				{
					item18 += NumberHelper.Round(newPhase
							.getPartSystemControlAndManagement())
							- NumberHelper.Round(oldPhase
									.getPartSystemControlAndManagement());
				}
				
				if (NumberHelper.Round(newPhase.getPartAuditOfOperations()) != NumberHelper
						.Round(oldPhase.getPartAuditOfOperations()))
				{
					item19 += NumberHelper.Round(newPhase
							.getPartAuditOfOperations())
							- NumberHelper.Round(oldPhase
									.getPartAuditOfOperations());
				}
				
				if (NumberHelper.Round(newPhase.getPartAuthoritiesCertification()) != NumberHelper
						.Round(oldPhase.getPartAuthoritiesCertification()))
				{
					item20 += NumberHelper.Round(newPhase
							.getPartAuthoritiesCertification())
							- NumberHelper.Round(oldPhase
									.getPartAuthoritiesCertification());
				}
				
				if (NumberHelper.Round(newPhase.getPartIntermEvaluation()) != NumberHelper
						.Round(oldPhase.getPartIntermEvaluation()))
				{
					item21 += NumberHelper.Round(newPhase
							.getPartIntermEvaluation())
							- NumberHelper.Round(oldPhase
									.getPartIntermEvaluation());
				}
			}
		}

		if (item1 > 0)
		{
			diff += item1;
		}
		if (item2 > 0)
		{
			diff += item2;
		}
		if (item3 > 0)
		{
			diff += item3;
		}
		if (item4 > 0)
		{
			diff += item4;
		}
		if (item5 > 0)
		{
			diff += item5;
		}
		if (item6 > 0)
		{
			diff += item6;
		}
		if (item7 > 0)
		{
			diff += item7;
		}
		if (item8 > 0)
		{
			diff += item8;
		}
		if (item9 > 0)
		{
			diff += item9;
		}
		if (item10 > 0)
		{
			diff += item10;
		}
		if (item11 > 0)
		{
			diff += item11;
		}
		if (item12 > 0)
		{
			diff += item12;
		}
		if (item13 > 0)
		{
			diff += item13;
		}
		if (item14 > 0)
		{
			diff += item14;
		}
		if (item15 > 0)
		{
			diff += item15;
		}
		if (item16 > 0)
		{
			diff += item16;
		}
		if (item17 > 0)
		{
			diff += item17;
		}
		if (item18 > 0)
		{
			diff += item18;
		}
		if (item19 > 0)
		{
			diff += item19;
		}
		if (item20 > 0)
		{
			diff += item20;
		}
		if (item21 > 0)
		{
			diff += item21;
		}		

		double x = NumberHelper.Round(diff);

		double y, z, a4 = 0d, b4 = 0d, a5 = 0d, b5 = 0d;
		for (int i = 0; i < this.getListPB().size() - 1; i++)
		{

			PartnersBudgets oldPB = listOld.size() > i ? listOld.get(i) : null;
			PartnersBudgets newPB = this.getListPB().get(i);

			for (int j = 0; j < newPB.getPhaseBudgets().size(); j++)
			{
				if (oldPB == null
						|| !((newPB.getPhaseBudgets().get(j).getPartDurables())
								.equals(oldPB.getPhaseBudgets().get(j)
										.getPartDurables())))
				{
					a4 += newPB.getPhaseBudgets().get(j).getPartDurables()
							- (oldPB == null ? 0d : oldPB.getPhaseBudgets()
									.get(j).getPartDurables());
				}

				if (oldPB == null
						|| !newPB
								.getPhaseBudgets()
								.get(j)
								.getPartDurableGoods()
								.equals(oldPB.getPhaseBudgets().get(j)
										.getPartDurableGoods()))
				{
					b4 += newPB.getPhaseBudgets().get(j).getPartDurableGoods()
							- (oldPB == null ? 0d : oldPB.getPhaseBudgets()
									.get(j).getPartDurableGoods());
				}

				if (oldPB == null
						|| !newPB
								.getPhaseBudgets()
								.get(j)
								.getPartAdvInfoPublicEvents()
								.equals(oldPB.getPhaseBudgets().get(j)
										.getPartAdvInfoPublicEvents()))
				{
					a5 += newPB.getPhaseBudgets().get(j)
							.getPartAdvInfoPublicEvents()
							- (oldPB == null ? 0d : oldPB.getPhaseBudgets()
									.get(j).getPartAdvInfoPublicEvents());
				}

				if (oldPB == null
						|| !newPB
								.getPhaseBudgets()
								.get(j)
								.getPartAdvInfoProducts()
								.equals(oldPB.getPhaseBudgets().get(j)
										.getPartAdvInfoProducts()))
				{
					b5 += newPB.getPhaseBudgets().get(j)
							.getPartAdvInfoProducts()
							- (oldPB == null ? 0d : oldPB.getPhaseBudgets()
									.get(j).getPartAdvInfoProducts());
				}
			}
		}

		if (NumberHelper.Round(a4) * NumberHelper.Round(b4) < 0d)
		{
			y = Math.abs(NumberHelper.Round(a4)) < Math.abs(NumberHelper
					.Round(b4)) ? Math.abs(NumberHelper.Round(a4)) : Math
					.abs(NumberHelper.Round(b4));
		}
		else
		{
			y = 0;
		}

		if (NumberHelper.Round(a5) * NumberHelper.Round(b5) < 0d)
		{
			z = Math.abs(NumberHelper.Round(a5)) < Math.abs(NumberHelper
					.Round(b5)) ? Math.abs(NumberHelper.Round(a5)) : Math
					.abs(NumberHelper.Round(b5));
		}
		else
		{
			z = 0;
		}

		return NumberHelper
				.Round((this.getListPB().get(this.getListPB().size() - 1)
						.getTotalAmount() == 0d ? NumberHelper
						.Round((x - y - z))
						: (NumberHelper.Round((x - y - z)) / this.getListPB()
								.get(this.getListPB().size() - 1)
								.getTotalAmount()))
						* 100
						+ (listOld.get(0).getCounter() == null ? 0d : listOld
								.get(0).getCounter()));
	}

	public void partnerChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setSelectedPartner(String.valueOf(event.getNewValue()));
		this.Page_Load();
	}

	public void subprojectChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setSelectedSubproject(String.valueOf(event.getNewValue()));
		this.Page_Load();
	}

	public void partnerStcChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setSelectedPartnerStc(String.valueOf(event.getNewValue()));
		this.getSession().put("partnerChanges", null);
		this.Page_Load();
	}

	public void budgetChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.getSession().put("partnerChanges", null);
		this.setSelectedBudget(String.valueOf(event.getNewValue()));
		this.Page_Load();
	}

	/**
	 * Sets listGB
	 * 
	 * @param listGB
	 *            the listGB to set
	 */
	public void setListGB(List<GeneralBudgets> listGB)
	{
		this.listGB = listGB;
	}

	/**
	 * Gets listGB
	 * 
	 * @return listGB the listGB
	 */
	public List<GeneralBudgets> getListGB()
	{
		return listGB;
	}

	/**
	 * Sets getListPB()
	 * 
	 * @param getListPB
	 *            () the getListPB() to set
	 */
	public void setListPB(List<PartnersBudgets> listPB)
	{
		this.listPB = getListPB();
	}

	/**
	 * Gets getListPB()
	 * 
	 * @return getListPB() the getListPB()
	 */
	public List<PartnersBudgets> getListPB()
	{
		return listPB;
	}

	/**
	 * Sets pic
	 * 
	 * @param pic
	 *            the pic to set
	 */
	public void setPic(ProjectIformationCompletations pic)
	{
		this.pic = pic;
	}

	/**
	 * Gets pic
	 * 
	 * @return pic the pic
	 */
	public ProjectIformationCompletations getPic()
	{
		return pic;
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<CFPartnerAnagraphs> listPartners)
	{
		this.listPartners = listPartners;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<CFPartnerAnagraphs> getListPartners()
	{
		return listPartners;
	}

	/**
	 * Gets sourceBudget
	 * 
	 * @return sourceBudget the sourceBudget
	 */
	public BudgetInputSourceDivided getSourceBudget()
	{
		return sourceBudget;
	}

	/**
	 * Sets sourceBudget
	 * 
	 * @param sourceBudget
	 *            the sourceBudget to set
	 */
	public void setSourceBudget(BudgetInputSourceDivided sourceBudget)
	{
		this.sourceBudget = sourceBudget;
	}

	/**
	 * Sets listWebPartners
	 * 
	 * @param listWebPartners
	 *            the listWebPartners to set
	 */
	public void setListWebPartners(List<SelectItem> listWebPartners)
	{
		this.listWebPartners = listWebPartners;
	}

	/**
	 * Gets listWebPartners
	 * 
	 * @return listWebPartners the listWebPartners
	 */
	public List<SelectItem> getListWebPartners()
	{
		return listWebPartners;
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner)
	{
		this.getViewState().put("budgetEditSelectedPartner", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner()
	{
		if (this.getViewState().get("budgetEditSelectedPartner") != null)
		{
			return String.valueOf(this.getViewState().get(
					"budgetEditSelectedPartner"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets currentGenerelBudget
	 * 
	 * @param currentGenerelBudget
	 *            the currentGenerelBudget to set
	 */
	public void setCurrentGeneralBudget(GeneralBudgets currentGenerelBudget)
	{
		this.currentGeneralBudget = currentGenerelBudget;
	}

	/**
	 * Gets currentGenerelBudget
	 * 
	 * @return currentGenerelBudget the currentGenerelBudget
	 */
	public GeneralBudgets getCurrentGeneralBudget()
	{
		return currentGeneralBudget;
	}

	/**
	 * Sets spanValidatorVisibility
	 * 
	 * @param spanValidatorVisibility
	 *            the spanValidatorVisibility to set
	 */
	public void setSpanValidatorVisibility(String spanValidatorVisibility)
	{
		this.spanValidatorVisibility = spanValidatorVisibility;
	}

	/**
	 * Gets spanValidatorVisibility
	 * 
	 * @return spanValidatorVisibility the spanValidatorVisibility
	 */
	public String getSpanValidatorVisibility()
	{
		return this.spanValidatorVisibility;
	}

	/**
	 * Gets generalBudgetMinorValidatorMessage
	 * 
	 * @return generalBudgetMinorValidatorMessage the
	 *         generalBudgetMinorValidatorMessage
	 */
	public String getGeneralBudgetMinorValidatorMessage()
	{
		return generalBudgetMinorValidatorMessage;
	}

	/**
	 * Sets generalBudgetMinorValidatorMessage
	 * 
	 * @param generalBudgetMinorValidatorMessage
	 *            the generalBudgetMinorValidatorMessage to set
	 */
	public void setGeneralBudgetMinorValidatorMessage(
			String generalBudgetMinorValidatorMessage)
	{
		this.generalBudgetMinorValidatorMessage = generalBudgetMinorValidatorMessage;
	}

	/**
	 * Sets spanPartnerValidatorVisibility
	 * 
	 * @param spanPartnerValidatorVisibility
	 *            the spanPartnerValidatorVisibility to set
	 */
	public void setSpanPartnerValidatorVisibility(
			String spanPartnerValidatorVisibility)
	{
		this.spanPartnerValidatorVisibility = spanPartnerValidatorVisibility;
	}

	/**
	 * Gets spanPartnerValidatorVisibility
	 * 
	 * @return spanPartnerValidatorVisibility the spanPartnerValidatorVisibility
	 */
	public String getSpanPartnerValidatorVisibility()
	{
		return spanPartnerValidatorVisibility;
	}

	/**
	 * Sets spanGBSaved
	 * 
	 * @param spanGBSaved
	 *            the spanGBSaved to set
	 */
	public void setSpanGBSaved(String spanGBSaved)
	{
		this.spanGBSaved = spanGBSaved;
	}

	/**
	 * Gets spanGBSaved
	 * 
	 * @return spanGBSaved the spanGBSaved
	 */
	public String getSpanGBSaved()
	{
		return spanGBSaved;
	}

	/**
	 * Sets spanPBSaved
	 * 
	 * @param spanPBSaved
	 *            the spanPBSaved to set
	 */
	public void setSpanPBSaved(String spanPBSaved)
	{
		this.spanPBSaved = spanPBSaved;
	}

	/**
	 * Gets spanPBSaved
	 * 
	 * @return spanPBSaved the spanPBSaved
	 */
	public String getSpanPBSaved()
	{
		return spanPBSaved;
	}

	/**
	 * Sets currentUserStc
	 * 
	 * @param currentUserStc
	 *            the currentUserStc to set
	 */
	public void setCurrentUserPartner(Boolean currentUserPartner)
	{
		this.getViewState().put("currentUserPartner", currentUserPartner);
	}

	/**
	 * Gets currentUserStc
	 * 
	 * @return currentUserStc the currentUserStc
	 */
	public Boolean getCurrentUserPartner()
	{
		if (this.getViewState().get("currentUserPartner") != null)
		{
			return Boolean.valueOf(String.valueOf(this.getViewState().get(
					"currentUserPartner")));
		}
		else
		{
			return false;
		}
	}

	/**
	 * Sets selectedPartnerStc
	 * 
	 * @param selectedPartnerStc
	 *            the selectedPartnerStc to set
	 */
	public void setSelectedPartnerStc(String selectedPartnerStc)
	{
		this.getViewState().put("budgetEditSelectedPartnerStc",
				selectedPartnerStc);
		this.setPartnerChangedFlag(true);
	}

	/**
	 * Gets selectedPartnerStc
	 * 
	 * @return selectedPartnerStc the selectedPartnerStc
	 */
	public String getSelectedPartnerStc()
	{
		if (this.getViewState().get("budgetEditSelectedPartnerStc") != null)
		{
			return String.valueOf(this.getViewState().get(
					"budgetEditSelectedPartnerStc"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets listPartnersBudgetsStc
	 * 
	 * @param listPartnersBudgetsStc
	 *            the listPartnersBudgetsStc to set
	 */
	public void setListPartnersBudgetsStc(
			List<PartnersBudgets> listPartnersBudgetsStc)
	{
		this.listPartnersBudgetsStc = listPartnersBudgetsStc;
	}

	/**
	 * Gets listPartnersBudgetsStc
	 * 
	 * @return listPartnersBudgetsStc the listPartnersBudgetsStc
	 */
	public List<PartnersBudgets> getListPartnersBudgetsStc()
	{
		return listPartnersBudgetsStc;
	}

	/**
	 * Sets listPartnerBudgetsStcWeb
	 * 
	 * @param listPartnerBudgetsStcWeb
	 *            the listPartnerBudgetsStcWeb to set
	 */
	public void setListPartnerBudgetsStcWeb(
			List<SelectItem> listPartnerBudgetsStcWeb)
	{
		this.listPartnerBudgetsStcWeb = listPartnerBudgetsStcWeb;
	}

	/**
	 * Gets listPartnerBudgetsStcWeb
	 * 
	 * @return listPartnerBudgetsStcWeb the listPartnerBudgetsStcWeb
	 */
	public List<SelectItem> getListPartnerBudgetsStcWeb()
	{
		return listPartnerBudgetsStcWeb;
	}

	/**
	 * Sets selectedBudget
	 * 
	 * @param selectedBudget
	 *            the selectedBudget to set
	 */
	public void setSelectedBudget(String selectedBudget)
	{
		if (this.getSession().get("partnerChanges") != null
				&& (Boolean) this.getSession().get("partnerChanges") == true)
		{
			this.getSession().put("partnerChanges", null);
		}
		else
		{
			this.getViewState().put("budgetEditSelectedBudget", selectedBudget);
			this.setPartnerChangedFlag(false);
		}
	}

	/**
	 * Gets selectedBudget
	 * 
	 * @return selectedBudget the selectedBudget
	 */
	public String getSelectedBudget()
	{
		if (this.getViewState().get("budgetEditSelectedBudget") != null)
		{
			return String.valueOf(this.getViewState().get(
					"budgetEditSelectedBudget"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets spanApprovedVisibility
	 * 
	 * @param spanApprovedVisibility
	 *            the spanApprovedVisibility to set
	 */
	public void setSpanApprovedVisibility(String spanApprovedVisibility)
	{
		this.getViewState().put("budgetEditSpanApproved",
				spanApprovedVisibility);
	}

	/**
	 * Gets spanApprovedVisibility
	 * 
	 * @return spanApprovedVisibility the spanApprovedVisibility
	 */
	public String getSpanApprovedVisibility()
	{
		if (this.getViewState().get("budgetEditSpanApproved") != null)
		{
			return String.valueOf(this.getViewState().get(
					"budgetEditSpanApproved"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets approveAvailable
	 * 
	 * @param approveAvailable
	 *            the approveAvailable to set
	 */
	public void setApproveAvailable(Boolean approveAvailable)
	{
		this.getViewState().put("budgetEditApproveAvailable", approveAvailable);
	}

	/**
	 * Gets approveAvailable
	 * 
	 * @return approveAvailable the approveAvailable
	 */
	public Boolean getApproveAvailable()
	{
		if (this.getViewState().get("budgetEditApproveAvailable") != null)
		{
			return Boolean.valueOf(String.valueOf(this.getViewState().get(
					"budgetEditApproveAvailable")));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets showConfirmationMessage
	 * 
	 * @param showConfirmationMessage
	 *            the showConfirmationMessage to set
	 */
	public void setShowConfirmationMessage(boolean showConfirmationMessage)
	{
		this.getViewState().put("budgetEditConfirm", showConfirmationMessage);
	}

	/**
	 * Gets showConfirmationMessage
	 * 
	 * @return showConfirmationMessage the showConfirmationMessage
	 */
	public Boolean getShowConfirmationMessage()
	{
		if (this.getViewState().get("budgetEditConfirm") != null)
		{
			return Boolean.valueOf(String.valueOf(this.getViewState().get(
					"budgetEditConfirm")));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets partnerChangedFlag
	 * 
	 * @param partnerChangedFlag
	 *            the partnerChangedFlag to set
	 */
	public void setPartnerChangedFlag(Boolean partnerChangedFlag)
	{
		this.getViewState().put("partnerChanged", partnerChangedFlag);
	}

	/**
	 * Gets partnerChangedFlag
	 * 
	 * @return partnerChangedFlag the partnerChangedFlag
	 */
	public Boolean getPartnerChangedFlag()
	{
		if (this.getViewState().get("partnerChanged") != null)
		{
			return Boolean.valueOf(String.valueOf(this.getViewState().get(
					"partnerChanged")));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets approved
	 * 
	 * @param approved
	 *            the approved to set
	 */
	public void setApproved(boolean approved)
	{
		this.approved = approved;
	}

	/**
	 * Gets approved
	 * 
	 * @return approved the approved
	 */
	public boolean getApproved()
	{
		return approved;
	}

	/**
	 * Sets spanLessThanCost
	 * 
	 * @param spanLessThanCost
	 *            the spanLessThanCost to set
	 */
	public void setSpanLessThanCost(String spanLessThanCost)
	{
		this.spanLessThanCost = spanLessThanCost;
	}

	/**
	 * Gets spanLessThanCost
	 * 
	 * @return spanLessThanCost the spanLessThanCost
	 */
	public String getSpanLessThanCost()
	{
		return spanLessThanCost;
	}

	/**
	 * Sets stringLessThanCost
	 * 
	 * @param stringLessThanCost
	 *            the stringLessThanCost to set
	 */
	public void setStringLessThanCost(String stringLessThanCost)
	{
		this.stringLessThanCost = stringLessThanCost;
	}

	/**
	 * Gets stringLessThanCost
	 * 
	 * @return stringLessThanCost the stringLessThanCost
	 */
	public String getStringLessThanCost()
	{
		return stringLessThanCost;
	}

	/**
	 * Sets denyAvailable
	 * 
	 * @param denyAvailable
	 *            the denyAvailable to set
	 */
	public void setDenyAvailable(Boolean denyAvailable)
	{
		this.getViewState().put("budgetEditDenyAvailable", denyAvailable);
	}

	/**
	 * Gets denyAvailable
	 * 
	 * @return denyAvailable the denyAvailable
	 */
	public Boolean getDenyAvailable()
	{
		return this.getViewState().get("budgetEditDenyAvailable") == null ? null
				: (Boolean) this.getViewState().get("budgetEditDenyAvailable");
	}

	/**
	 * Sets denied
	 * 
	 * @param denied
	 *            the denied to set
	 */
	public void setDenied(boolean denied)
	{
		this.denied = denied;
	}

	/**
	 * Gets denied
	 * 
	 * @return denied the denied
	 */
	public boolean getDenied()
	{
		return denied;
	}

	/**
	 * Sets isPartner
	 * 
	 * @param isPartner
	 *            the isPartner to set
	 */
	public void setCf(boolean isPartner)
	{
		this.cf = isPartner;
	}

	/**
	 * Gets isPartner
	 * 
	 * @return isPartner the isPartner
	 */
	public boolean getCf()
	{
		return cf;
	}

	/**
	 * Sets curCF
	 * 
	 * @param curCF
	 *            the curCF to set
	 */
	public void setCurCF(CFPartnerAnagraphs curPartner)
	{
		this.curCF = curPartner;
	}

	/**
	 * Gets curCF
	 * 
	 * @return curCF the curCF
	 */
	public CFPartnerAnagraphs getCurCF()
	{
		return curCF;
	}

	/**
	 * Sets stcCannotApprove
	 * 
	 * @param stcCannotApprove
	 *            the stcCannotApprove to set
	 */
	public void setStcCannotApprove(String stcCannotApprove)
	{
		this.stcCannotApprove = stcCannotApprove;
	}

	/**
	 * Gets stcCannotApprove
	 * 
	 * @return stcCannotApprove the stcCannotApprove
	 */
	public String getStcCannotApprove()
	{
		return stcCannotApprove;
	}

	/**
	 * Sets notCorrectBudget
	 * 
	 * @param notCorrectBudget
	 *            the notCorrectBudget to set
	 */
	public void setNotCorrectBudget(String notCorrectBudget)
	{
		this.notCorrectBudget = notCorrectBudget;
	}

	/**
	 * Gets notCorrectBudget
	 * 
	 * @return notCorrectBudget the notCorrectBudget
	 */
	public String getNotCorrectBudget()
	{
		return notCorrectBudget;
	}

	/**
	 * Sets mailsBudgetApproved
	 * 
	 * @param mailsBudgetApproved
	 *            the mailsBudgetApproved to set
	 */
	public void setMailsBudgetApproved(List<InfoObject> mailsBudgetApproved)
	{
		this.mailsBudgetApproved = mailsBudgetApproved;
	}

	/**
	 * Gets mailsBudgetApproved
	 * 
	 * @return mailsBudgetApproved the mailsBudgetApproved
	 */
	public List<InfoObject> getMailsBudgetApproved()
	{
		return mailsBudgetApproved;
	}

	/**
	 * Sets mailsBudgetRefused
	 * 
	 * @param mailsBudgetRefused
	 *            the mailsBudgetRefused to set
	 */
	public void setMailsBudgetRefused(List<InfoObject> mailsBudgetRefused)
	{
		this.mailsBudgetRefused = mailsBudgetRefused;
	}

	/**
	 * Gets mailsBudgetRefused
	 * 
	 * @return mailsBudgetRefused the mailsBudgetRefused
	 */
	public List<InfoObject> getMailsBudgetRefused()
	{
		return mailsBudgetRefused;
	}

	/**
	 * Gets listPhase
	 * 
	 * @return listPhase the listPhase
	 */
	public List<Phase> getListPhase()
	{
		return listPhase;
	}

	/**
	 * Sets listPhase
	 * 
	 * @param listPhase
	 *            the listPhase to set
	 */
	public void setListPhase(List<Phase> listPhase)
	{
		this.listPhase = listPhase;
	}

	public class GeneralBudgetComparer implements Comparator<GeneralBudgets>
	{
		@Override
		public int compare(GeneralBudgets arg0, GeneralBudgets arg1)
		{

			if (arg0.getCfPartnerAnagraph() == null)
			{
				return 1;
			}
			if (arg1.getCfPartnerAnagraph() == null)
			{
				return -1;
			}
			CFPartnerAnagraphProject p1 = null;
			try
			{
				p1 = BeansFactory.CFPartnerAnagraphProject().LoadByPartner(
						arg0.getProject().getId(),
						arg0.getCfPartnerAnagraph().getId());
			}
			catch (PersistenceBeanException e)
			{
			}
			CFPartnerAnagraphProject p2 = null;
			try
			{
				p2 = BeansFactory.CFPartnerAnagraphProject().LoadByPartner(
						arg1.getProject().getId(),
						arg1.getCfPartnerAnagraph().getId());
			}
			catch (PersistenceBeanException e)
			{
			}
			return (p1.getType().getValue().compareTo(p2.getType().getValue()));
		}

	}

	/**
	 * Gets selectedSubproject
	 * 
	 * @return selectedSubproject the selectedSubproject
	 */
	public String getSelectedSubproject()
	{
		if (this.getViewState().get("budgetEditSelectedSubproject") != null)
		{
			return String.valueOf(this.getViewState().get(
					"budgetEditSelectedSubproject"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets selectedSubproject
	 * 
	 * @param selectedSubproject
	 *            the selectedSubproject to set
	 */
	public void setSelectedSubproject(String selectedSubproject)
	{
		this.getViewState().put("budgetEditSelectedSubproject",
				selectedSubproject);
	}

	/**
	 * Gets listSubProjects
	 * 
	 * @return listSubProjects the listSubProjects
	 */
	public List<CostDefinitionPhases> getListSubProjects()
	{
		return listSubProjects;
	}

	/**
	 * Sets listSubProjects
	 * 
	 * @param listSubProjects
	 *            the listSubProjects to set
	 */
	public void setListSubProjects(List<CostDefinitionPhases> listSubProjects)
	{
		this.listSubProjects = listSubProjects;
	}

	public class PartnerBudgetYearComparer implements
			Comparator<PartnersBudgets>
	{
		@Override
		public int compare(PartnersBudgets arg0, PartnersBudgets arg1)
		{
			if (arg0.getYear() > arg1.getYear())
			{
				return 1;
			}
			else if (arg0.getYear() < arg1.getYear())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}

	/**
	 * Gets isAcce3
	 * @return isAcce3 the isAcce3
	 */
	public boolean getAcce3()
	{
		return acce3;
	}

	/**
	 * Sets isAcce3
	 * @param isAcce3 the isAcce3 to set
	 */
	public void setAcce3(boolean acce3)
	{
		this.acce3 = acce3;
	}

	public DataStructureTab getTabForAsse3()
	{
		return tabForAsse3;
	}

	public void setTabForAsse3(DataStructureTab tabForAsse3)
	{
		this.tabForAsse3 = tabForAsse3;
	}

	public List<CostAsse3> getLegend()
	{
		return legend;
	}

	public void setLegend(List<CostAsse3> legend)
	{
		this.legend = legend;
	}

}