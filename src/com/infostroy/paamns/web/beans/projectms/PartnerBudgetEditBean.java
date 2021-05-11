/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.PhaseAsse3Types;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostItemAsse3;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CostAsse3;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureColumn;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureRow;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureTab;
import com.infostroy.paamns.web.beans.dynamicdatastructure.DataStructureTable;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class PartnerBudgetEditBean extends EntityProjectEditBean<PartnersBudgets> {
	private CFPartnerAnagraphs currentPartner;

	private ProjectIformationCompletations projectCompletion;

	private GeneralBudgets currentGeneralBudget;

	private String spanValidatorVisibility;

	private List<PartnersBudgets> listPB;

	private List<CostDefinitionPhases> cdPhases;

	private Boolean showPhase7;

	private Boolean isAsse3;

	private static final String YEARS = "2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024";

	private DataStructureTab tabForAsse3;

	private List<CostAsse3> legend;

	public Boolean BeforeSave() {
		boolean isCorrect = this.getCurrentGeneralBudget().getBudgetTotal().equals(this.CalculateTotalSum());
		recalculateTab();
		if (isCorrect) {
			this.setSpanValidatorVisibility("none");
		} else {
			this.setSpanValidatorVisibility("");
		}

		return isCorrect;
	}

	@Override
	public void AfterSave() {
		this.GoBack();
	}

	@Override
	public void GoBack() {
		this.goTo(PagesTypes.PARTNERBUDGETLIST);
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {

		this.setLegend(BeansFactory.CostAsse3().Load());
		
		this.setAsse3(3 == this.getProject().getTypology().getId());
		
		
		this.projectCompletion = BeansFactory.ProjectIformationCompletations().LoadByProject(this.getProjectId());
		if (this.projectCompletion == null) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}
		if (this.getSession().get("partnerbudgetedit") != null) {
			this.setCurrentPartner(
					BeansFactory.CFPartnerAnagraphs().Load(String.valueOf(this.getSession().get("partnerbudgetedit"))));
			List<GeneralBudgets> listGB = BeansFactory.GeneralBudgets().GetItemsForCFAnagraph(this.getProjectId(),
					this.currentPartner.getId());

			if (listGB == null || listGB.isEmpty()) {
				this.goTo(PagesTypes.PROJECTINDEX);
			} else {
				this.setCurrentGeneralBudget(listGB.get(listGB.size() - 1));
			}
			this.setSpanValidatorVisibility("none");

			List<PartnersBudgets> listAux = BeansFactory.PartnersBudgets().GetByProjectAndPartner(this.getProjectId(),
					this.currentPartner.getId().toString(), true);
			for (PartnersBudgets item : listAux) {
				item.setPhaseBudgets(BeansFactory.PartnersBudgetsPhases().loadByPartnerBudget(item.getId()));

			}
			this.setListPB(new ArrayList<PartnersBudgets>());

			if (Boolean.FALSE.equals(this.getAsse3())) {
				for (PartnersBudgets pb : listAux) {
					this.getListPB().add(pb.clone());
				}
				this.setCdPhases(BeansFactory.CostDefinitionPhase().Load());
				/*
				 * PartnersBudgets totalEntity = new PartnersBudgets(); // Total
				 * // entity totalEntity.setPhaseBudgets(new
				 * LinkedList<Phase>());
				 * 
				 * for (CostDefinitionPhases cdPhase : this.getCdPhases()) {
				 * Phase newPhase = new Phase(); newPhase.setPhase(cdPhase);
				 * newPhase.setPartAdvInfoProducts(0d);
				 * newPhase.setPartAdvInfoPublicEvents(0d);
				 * newPhase.setPartDurableGoods(0d);
				 * newPhase.setPartDurables(0d); newPhase.setPartHumanRes(0d);
				 * newPhase.setPartMissions(0d); newPhase.setPartOther(0d);
				 * newPhase.setPartGeneralCosts(0d);
				 * newPhase.setPartOverheads(0d);
				 * newPhase.setPartProvisionOfService(0d);
				 * newPhase.setPartnersBudgets(totalEntity);
				 * totalEntity.getPhaseBudgets().add(newPhase); }
				 */
				if (this.getListPB() == null || this.getListPB().isEmpty()) {
					PartnersBudgets newEntity = new PartnersBudgets();
					newEntity.setPhaseBudgets(new LinkedList<Phase>());
					for (CostDefinitionPhases cdPhase : this.getCdPhases()) {
						Phase newPhase = new Phase();
						newPhase.setPhase(cdPhase);
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
						newPhase.setPartnersBudgets(newEntity);

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

						newEntity.getPhaseBudgets().add(newPhase);
					}
					newEntity.setTotalAmount(0d);
					newEntity.setYear(1);
					this.getListPB().add(newEntity);

					/*
					 * this.getListPB().add(newEntity); this.setListPB(new
					 * ArrayList<PartnersBudgets>());
					 * 
					 * for (int i = 0; i <
					 * this.projectCompletion.getYearDuration(); i++) {
					 * PartnersBudgets newEntity = new PartnersBudgets();
					 * newEntity.setPhaseBudgets(new LinkedList<Phase>()); for
					 * (CostDefinitionPhases cdPhase : this.getCdPhases()) {
					 * Phase newPhase = new Phase(); newPhase.setPhase(cdPhase);
					 * newPhase.setPartAdvInfoProducts(0d);
					 * newPhase.setPartAdvInfoPublicEvents(0d);
					 * newPhase.setPartDurableGoods(0d);
					 * newPhase.setPartDurables(0d);
					 * newPhase.setPartHumanRes(0d);
					 * newPhase.setPartMissions(0d); newPhase.setPartOther(0d);
					 * newPhase.setPartGeneralCosts(0d);
					 * newPhase.setPartOverheads(0d);
					 * newPhase.setPartProvisionOfService(0d);
					 * newPhase.setPartnersBudgets(newEntity);
					 * newEntity.getPhaseBudgets().add(newPhase); }
					 * newEntity.setTotalAmount(0d); newEntity.setYear(i + 1);
					 * 
					 * this.getListPB().add(newEntity); }
					 */
				}

				/*
				 * while (this.getListPB().size() < this.getProjectCompletion()
				 * .getYearDuration()) { PartnersBudgets newEntity = new
				 * PartnersBudgets(); newEntity.setPhaseBudgets(new
				 * LinkedList<Phase>());
				 * 
				 * for (CostDefinitionPhases cdPhase : this.getCdPhases()) {
				 * Phase newPhase = new Phase(); newPhase.setPhase(cdPhase);
				 * newPhase.setPartAdvInfoProducts(0d);
				 * newPhase.setPartAdvInfoPublicEvents(0d);
				 * newPhase.setPartDurableGoods(0d);
				 * newPhase.setPartDurables(0d); newPhase.setPartHumanRes(0d);
				 * newPhase.setPartMissions(0d); newPhase.setPartOther(0d);
				 * newPhase.setPartGeneralCosts(0d);
				 * newPhase.setPartOverheads(0d);
				 * newPhase.setPartProvisionOfService(0d);
				 * newPhase.setPartnersBudgets(newEntity);
				 * newEntity.getPhaseBudgets().add(newPhase); }
				 * newEntity.setTotalAmount(0d);
				 * newEntity.setYear(this.getListPB().size() + 1);
				 * 
				 * this.getListPB().add(newEntity); }
				 * 
				 * this.getListPB().add(this.RecalculateTotalRow(totalEntity));
				 */
			} else {
				for (PartnersBudgets pb : listAux) {
					this.getListPB().add(pb);
				}
				if (this.getListPB() == null || this.getListPB().isEmpty()) {
					generateNewTabMdoelAsse3();
				} else {
					generateExistingTabMdoelAsse3();
				}

			}
		}
	}

	private void recalculateTab() {
		if (this.getAsse3()) {
			for (DataStructureTable table : this.getTabForAsse3().getTables()) {
				table.getRows().remove(table.getRows().size() - 1);
				table.getRows().add(createTotalRow(table));
			}
		}
	}

	private void generateNewTabMdoelAsse3() throws PersistenceBeanException {

		PartnersBudgets newEntity = new PartnersBudgets();
		newEntity.setItemsAsse3(new ArrayList<CostItemAsse3>());
		newEntity.setYear(1);
		DataStructureTab tab = new DataStructureTab();
		tab.setTables(new ArrayList<DataStructureTable>());

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

					CostItemAsse3 newItem = new CostItemAsse3();
					newItem.setCostPhase(item);
					newItem.setPartnersBudgets(null);
					newItem.setValue(new Double(0d));
					newItem.setYear(year);
					row.getItems().add(newItem);
					newEntity.getItemsAsse3().add(newItem);
				}

				table.getRows().add(row);

			}
			table.getRows().add(createTotalRow(table));
			tab.getTables().add(table);

		}
		this.getListPB().add(newEntity);
		this.setTabForAsse3(tab);

	}

	private void generateExistingTabMdoelAsse3() throws PersistenceBeanException {

		DataStructureTab tab = new DataStructureTab();
		tab.setTables(new ArrayList<DataStructureTable>());

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

					for (CostItemAsse3 asse3 : this.getListPB().get(0).getItemsAsse3()) {
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

		this.setTabForAsse3(tab);

	}

	private DataStructureRow createTotalRow(DataStructureTable table) {

		DataStructureRow totalRow = new DataStructureRow();
		totalRow.setTotal(Boolean.TRUE);
		totalRow.setItems(new ArrayList<CostItemAsse3>());
		for (DataStructureColumn col : table.getRows().get(0).getColumns()) {
			CostItemAsse3 item = new CostItemAsse3();
			item.setYear(Utils.getString("Resources", "partnerBudgetFooterTotal", null));
			totalRow.getItems().add(item);
		}
		for (DataStructureRow rowInTable : table.getRows()) {
			for (int i = 0; i < table.getRows().get(0).getColumns().size(); i++) {
				totalRow.getItems().get(i).setValue(
						(totalRow.getItems().get(i).getValue() == null ? 0d : totalRow.getItems().get(i).getValue())
								+ rowInTable.getItems().get(i).getValue());
			}
		}
		return totalRow;

	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException, PersistenceBeanException {
		if (this.getSessionBean().getIsActualSateAndAguRead()) {
			goTo(PagesTypes.PROJECTINDEX);
		}

		if (!AccessGrantHelper.getIsAGUAsse5()) {
			this.goTo(PagesTypes.PROJECTINDEX);
			return;
		}
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException,
			PersistenceBeanException, PersistenceBeanException {
		if (Boolean.FALSE.equals(this.getAsse3())) {
			if (this.CheckModifications()) {
				List<PartnersBudgets> listPartnersBudgets = BeansFactory.PartnersBudgets().GetByProjectAndPartner(
						this.getProjectId(), String.valueOf(this.currentPartner.getId()), false);

				// Set flag OLD for all items previously saved
				for (PartnersBudgets pb : listPartnersBudgets) {
					pb.setIsOld(true);
					BeansFactory.PartnersBudgets().SaveInTransaction(pb);
				}

				List<PartnersBudgets> listToSave = this.CopyEntities();

				Long maxVersion = BeansFactory.PartnersBudgets().GetLastVersionNumber(this.getProjectId(),
						this.currentPartner.getId().toString(), null);

				if (maxVersion == null) {
					maxVersion = 0l;
				}

				for (PartnersBudgets pb : listToSave) {
					pb.setCfPartnerAnagraph(this.currentPartner);
					pb.setCreateDate(DateTime.getNow());
					pb.setDeleted(false);
					pb.setIsOld(false);
					pb.setProject(this.getProject());
					pb.setVersion(maxVersion + 1);
					pb.setApproved(false);
					pb.setDenied(false);
					BeansFactory.PartnersBudgets().SaveInTransaction(pb);
					for (Phase phase : pb.getPhaseBudgets()) {
						phase.setPartnersBudgets(pb);
						BeansFactory.PartnersBudgetsPhases().SaveInTransaction(phase);
					}
				}
			}
		} else {
			if (checkModificationsAsse3()) {
				List<PartnersBudgets> listPartnersBudgets = BeansFactory.PartnersBudgets().GetByProjectAndPartner(
						this.getProjectId(), String.valueOf(this.currentPartner.getId()), false);

				for (PartnersBudgets pb : listPartnersBudgets) {
					pb.setIsOld(true);
					BeansFactory.PartnersBudgets().SaveInTransaction(pb);
				}

				List<PartnersBudgets> listToSave = this.CopyEntities();

				Long maxVersion = BeansFactory.PartnersBudgets().GetLastVersionNumber(this.getProjectId(),
						this.currentPartner.getId().toString(), null);

				if (maxVersion == null) {
					maxVersion = 0l;
				}

				for (PartnersBudgets pb : listToSave) {
					pb.setCfPartnerAnagraph(this.currentPartner);
					pb.setCreateDate(DateTime.getNow());
					pb.setDeleted(false);
					pb.setIsOld(false);
					pb.setProject(this.getProject());
					pb.setVersion(maxVersion + 1);
					pb.setApproved(false);
					pb.setDenied(false);
					BeansFactory.PartnersBudgets().SaveInTransaction(pb);
					for (CostItemAsse3 asse3Item : pb.getItemsAsse3()) {
						asse3Item.setPartnersBudgets(pb);
						BeansFactory.CostItemAsse3().SaveInTransaction(asse3Item);
					}
				}
			}
		}

	}

	private boolean checkModificationsAsse3() throws PersistenceBeanException {

		if (this.getListPB().get(0).isNew()) {
			return true;
		}

		List<PartnersBudgets> listPartnersBudgetsOld = BeansFactory.PartnersBudgets()
				.GetByProjectAndPartner(this.getProjectId(), this.currentPartner.getId().toString(), true);

		for (CostItemAsse3 item : listPartnersBudgetsOld.get(0).getItemsAsse3()) {

			for (CostItemAsse3 itemChanged : this.getListPB().get(0).getItemsAsse3()) {
				if (item.getId().equals(itemChanged.getId())) {
					if (!item.getValue().equals(itemChanged.getValue())) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	/**
	 * Copies entities to another list without last; Last element is total
	 * 
	 * @return
	 */
	private List<PartnersBudgets> CopyEntities() {
		List<PartnersBudgets> listAux = new ArrayList<PartnersBudgets>();

		// for (int i = 0; i < this.getProjectCompletion().getYearDuration();
		// i++)
		// {
		if (Boolean.FALSE.equals(this.getAsse3())) {
			listAux.add(this.CopyInfo(this.getListPB().get(0)));
		} else {
			listAux.add(this.CopyInfoAsse3(this.getListPB().get(0)));
		}

		// }

		return listAux;
	}

	private PartnersBudgets CopyInfoAsse3(PartnersBudgets entityToCopy) {
		PartnersBudgets newEntity = new PartnersBudgets();

		newEntity.setCfPartnerAnagraph(entityToCopy.getCfPartnerAnagraph());
		newEntity.setCreateDate(entityToCopy.getCreateDate());
		newEntity.setDeleted(entityToCopy.getDeleted());
		newEntity.setDescription(entityToCopy.getDescription());
		newEntity.setIsOld(entityToCopy.getIsOld());

		newEntity.setProject(entityToCopy.getProject());
		newEntity.setTotalAmount(entityToCopy.getTotalAmount());
		newEntity.setYear(entityToCopy.getYear());
		newEntity.setItemsAsse3(new ArrayList<CostItemAsse3>());
		for (CostItemAsse3 asse3 : entityToCopy.getItemsAsse3()) {
			CostItemAsse3 newAsse3 = new CostItemAsse3();
			newAsse3.setCostPhase(asse3.getCostPhase());
			newAsse3.setValue(asse3.getValue());
			newAsse3.setYear(asse3.getYear());
			newEntity.getItemsAsse3().add(newAsse3);
		}

		return newEntity;
	}

	private PartnersBudgets CopyInfo(PartnersBudgets entityToCopy) {
		PartnersBudgets newEntity = new PartnersBudgets();
		newEntity.setPhaseBudgets(new LinkedList<Phase>());

		newEntity.setCfPartnerAnagraph(entityToCopy.getCfPartnerAnagraph());
		newEntity.setCreateDate(entityToCopy.getCreateDate());
		newEntity.setDeleted(entityToCopy.getDeleted());
		newEntity.setDescription(entityToCopy.getDescription());
		newEntity.setIsOld(entityToCopy.getIsOld());

		for (Phase phaseToCopy : entityToCopy.getPhaseBudgets()) {
			Phase newPhase = new Phase();
			newPhase.setPhase(phaseToCopy.getPhase());
			newPhase.setPartAdvInfoProducts(phaseToCopy.getPartAdvInfoProducts());
			newPhase.setPartAdvInfoPublicEvents(phaseToCopy.getPartAdvInfoPublicEvents());
			newPhase.setPartDurableGoods(phaseToCopy.getPartDurableGoods());
			newPhase.setPartDurables(phaseToCopy.getPartDurables());
			newPhase.setPartHumanRes(phaseToCopy.getPartHumanRes());
			newPhase.setPartMissions(phaseToCopy.getPartMissions());
			newPhase.setPartOther(phaseToCopy.getPartOther());
			newPhase.setPartGeneralCosts(phaseToCopy.getPartGeneralCosts());
			newPhase.setPartOverheads(phaseToCopy.getPartOverheads());
			newPhase.setPartProvisionOfService(phaseToCopy.getPartProvisionOfService());

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

			newEntity.getPhaseBudgets().add(newPhase);
		}
		newEntity.setProject(entityToCopy.getProject());
		newEntity.setTotalAmount(entityToCopy.getTotalAmount());
		newEntity.setYear(entityToCopy.getYear());

		return newEntity;
	}

	private boolean CheckModifications() {
		List<PartnersBudgets> listPartnersBudgetsOld = new ArrayList<PartnersBudgets>();

		try {
			listPartnersBudgetsOld = BeansFactory.PartnersBudgets().GetByProjectAndPartner(this.getProjectId(),
					this.currentPartner.getId().toString(), true);
		} catch (PersistenceBeanException e) {
			e.printStackTrace();
		}

		if (listPartnersBudgetsOld == null || listPartnersBudgetsOld.isEmpty()) {
			return true;
		}

		boolean isModified = false;

		if (listPartnersBudgetsOld.size() != this.getListPB().size()) {
			return true;
		}

		// for (int i = 0; i < this.projectCompletion.getYearDuration(); i++)
		// {
		PartnersBudgets arg1 = listPartnersBudgetsOld.get(0);
		PartnersBudgets arg2 = this.getListPB().get(0);
		for (int j = 0; j < arg1.getPhaseBudgets().size(); j++) {

			if (!arg1.getPhaseBudgets().get(j).getPartAdvInfoProducts()
					.equals(arg2.getPhaseBudgets().get(j).getPartAdvInfoProducts())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).equals(arg2.getPhaseBudgets().get(j))) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).equals(arg2.getPhaseBudgets().get(j))) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartDurables()
					.equals(arg2.getPhaseBudgets().get(j).getPartDurables())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartHumanRes()
					.equals(arg2.getPhaseBudgets().get(j).getPartHumanRes())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartMissions()
					.equals(arg2.getPhaseBudgets().get(j).getPartMissions())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartGeneralCosts()
					.equals(arg2.getPhaseBudgets().get(j).getPartGeneralCosts())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartOther().equals(arg2.getPhaseBudgets().get(j).getPartOther())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartOverheads()
					.equals(arg2.getPhaseBudgets().get(j).getPartOverheads())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartProvisionOfService()
					.equals(arg2.getPhaseBudgets().get(j).getPartProvisionOfService())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartContainer()
					.equals(arg2.getPhaseBudgets().get(j).getPartContainer())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartCommunicationAndInformation()
					.equals(arg2.getPhaseBudgets().get(j).getPartCommunicationAndInformation())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartOrganizationOfCommitees()
					.equals(arg2.getPhaseBudgets().get(j).getPartOrganizationOfCommitees())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartPersonalExpenses()
					.equals(arg2.getPhaseBudgets().get(j).getPartPersonalExpenses())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartOtherFees()
					.equals(arg2.getPhaseBudgets().get(j).getPartOtherFees())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartAutoCoordsTunis()
					.equals(arg2.getPhaseBudgets().get(j).getPartAutoCoordsTunis())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartContactPoint()
					.equals(arg2.getPhaseBudgets().get(j).getPartContactPoint())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartSystemControlAndManagement()
					.equals(arg2.getPhaseBudgets().get(j).getPartSystemControlAndManagement())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartAuditOfOperations()
					.equals(arg2.getPhaseBudgets().get(j).getPartAuditOfOperations())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartAuthoritiesCertification()
					.equals(arg2.getPhaseBudgets().get(j).getPartAuthoritiesCertification())) {
				isModified = true;
				break;
			}
			if (!arg1.getPhaseBudgets().get(j).getPartIntermEvaluation()
					.equals(arg2.getPhaseBudgets().get(j).getPartIntermEvaluation())) {
				isModified = true;
				break;
			}

		}
		// }

		return isModified;
	}

	private Double CalculateTotalSum() {
		Double totalsum = 0d;
		if (Boolean.FALSE.equals(this.getAsse3())) {

			for (PartnersBudgets pb : this.getListPB())// .subList(0,
			// this.getListPB().size() - 1))
			{
				Double sum = 0d;
				for (Phase phase : pb.getPhaseBudgets()) {
					sum += phase.getPartAdvInfoProducts();
					sum += phase.getPartAdvInfoPublicEvents();
					sum += phase.getPartDurableGoods();
					sum += phase.getPartDurables();
					sum += phase.getPartHumanRes();
					sum += phase.getPartMissions();
					sum += phase.getPartOther();
					sum += phase.getPartOverheads();
					sum += phase.getPartProvisionOfService();
					sum += phase.getPartGeneralCosts();
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

				pb.setTotalAmount(sum);
				totalsum += NumberHelper.Round(sum);
			}
		} else {
			for (PartnersBudgets pb : this.getListPB()) {
				for (CostItemAsse3 item : pb.getItemsAsse3()) {
					totalsum += NumberHelper.Round(item.getValue());
				}
				pb.setTotalAmount(totalsum);

			}
		}

		/*
		 * this.getListPB().set( this.getListPB().size() - 1,
		 * this.RecalculateTotalRow(this.getListPB().get(
		 * this.getListPB().size() - 1)));
		 */

		return NumberHelper.Round(totalsum);
	}

	private PartnersBudgets RecalculateTotalRow(PartnersBudgets totalEntity) {
		totalEntity.setCfPartnerAnagraph(this.getCurrentPartner());
		for (Phase phase : totalEntity.getPhaseBudgets()) {
			phase.setPartAdvInfoProducts(0d);
			phase.setPartAdvInfoPublicEvents(0d);
			phase.setPartDurableGoods(0d);
			phase.setPartDurables(0d);
			phase.setPartHumanRes(0d);
			phase.setPartMissions(0d);
			phase.setPartOther(0d);
			phase.setPartGeneralCosts(0d);
			phase.setPartOverheads(0d);
			phase.setPartProvisionOfService(0d);

			phase.setPartAdvInfoProducts(0d);
			phase.setPartAdvInfoPublicEvents(0d);
			phase.setPartDurableGoods(0d);
			phase.setPartDurables(0d);
			phase.setPartHumanRes(0d);
			phase.setPartMissions(0d);
			phase.setPartOther(0d);
			phase.setPartOverheads(0d);
			phase.setPartProvisionOfService(0d);
		}
		totalEntity.setTotalAmount(0d);
		totalEntity.setDescription(Utils.getString("Resources", "partnerBudgetFooterTotal", null));
		totalEntity.setTotalRow(true);

		for (int i = 0; i < this.projectCompletion.getYearDuration(); i++) {
			PartnersBudgets currEntity = this.getListPB().get(i);

			currEntity.setDescription(Utils.getString("Resources", "partnerBudgetYear", null) + " "
					+ String.valueOf(currEntity.getYear()));
			currEntity.setTotalRow(false);

			for (int j = 0; j < totalEntity.getPhaseBudgets().size(); j++) {

				totalEntity.getPhaseBudgets().get(j)
						.setPartAdvInfoProducts(totalEntity.getPhaseBudgets().get(j).getPartAdvInfoProducts()
								+ currEntity.getPhaseBudgets().get(j).getPartAdvInfoProducts());
				totalEntity.getPhaseBudgets().get(j)
						.setPartAdvInfoPublicEvents(totalEntity.getPhaseBudgets().get(j).getPartAdvInfoPublicEvents()
								+ currEntity.getPhaseBudgets().get(j).getPartAdvInfoPublicEvents());
				totalEntity.getPhaseBudgets().get(j)
						.setPartDurableGoods(totalEntity.getPhaseBudgets().get(j).getPartDurableGoods()
								+ currEntity.getPhaseBudgets().get(j).getPartDurableGoods());
				totalEntity.getPhaseBudgets().get(j)
						.setPartDurables(totalEntity.getPhaseBudgets().get(j).getPartDurables()
								+ currEntity.getPhaseBudgets().get(j).getPartDurables());
				totalEntity.getPhaseBudgets().get(j)
						.setPartHumanRes(totalEntity.getPhaseBudgets().get(j).getPartHumanRes()
								+ currEntity.getPhaseBudgets().get(j).getPartHumanRes());
				totalEntity.getPhaseBudgets().get(j)
						.setPartMissions(totalEntity.getPhaseBudgets().get(j).getPartMissions()
								+ currEntity.getPhaseBudgets().get(j).getPartMissions());
				totalEntity.getPhaseBudgets().get(j).setPartOther(totalEntity.getPhaseBudgets().get(j).getPartOther()
						+ currEntity.getPhaseBudgets().get(j).getPartOther());
				totalEntity.getPhaseBudgets().get(j)
						.setPartGeneralCosts(totalEntity.getPhaseBudgets().get(j).getPartGeneralCosts()
								+ currEntity.getPhaseBudgets().get(j).getPartGeneralCosts());

				totalEntity.getPhaseBudgets().get(j)
						.setPartOverheads(totalEntity.getPhaseBudgets().get(j).getPartOverheads()
								+ currEntity.getPhaseBudgets().get(j).getPartOverheads());
				totalEntity.getPhaseBudgets().get(j)
						.setPartProvisionOfService(totalEntity.getPhaseBudgets().get(j).getPartProvisionOfService()
								+ currEntity.getPhaseBudgets().get(j).getPartProvisionOfService());
				totalEntity.getPhaseBudgets().get(j)
						.setPartContainer(totalEntity.getPhaseBudgets().get(j).getPartContainer()
								+ currEntity.getPhaseBudgets().get(j).getPartContainer());

				totalEntity.getPhaseBudgets().get(j)
						.setPartPersonalExpenses(totalEntity.getPhaseBudgets().get(j).getPartPersonalExpenses()
								+ currEntity.getPhaseBudgets().get(j).getPartPersonalExpenses());

				totalEntity.getPhaseBudgets().get(j).setPartCommunicationAndInformation(
						totalEntity.getPhaseBudgets().get(j).getPartCommunicationAndInformation()
								+ currEntity.getPhaseBudgets().get(j).getPartCommunicationAndInformation());

				totalEntity.getPhaseBudgets().get(j).setPartOrganizationOfCommitees(
						totalEntity.getPhaseBudgets().get(j).getPartOrganizationOfCommitees()
								+ currEntity.getPhaseBudgets().get(j).getPartOrganizationOfCommitees());

				totalEntity.getPhaseBudgets().get(j)
						.setPartOtherFees(totalEntity.getPhaseBudgets().get(j).getPartOtherFees()
								+ currEntity.getPhaseBudgets().get(j).getPartOtherFees());

				totalEntity.getPhaseBudgets().get(j)
						.setPartAutoCoordsTunis(totalEntity.getPhaseBudgets().get(j).getPartAutoCoordsTunis()
								+ currEntity.getPhaseBudgets().get(j).getPartAutoCoordsTunis());

				totalEntity.getPhaseBudgets().get(j)
						.setPartContactPoint(totalEntity.getPhaseBudgets().get(j).getPartContactPoint()
								+ currEntity.getPhaseBudgets().get(j).getPartContactPoint());

				totalEntity.getPhaseBudgets().get(j).setPartSystemControlAndManagement(
						totalEntity.getPhaseBudgets().get(j).getPartSystemControlAndManagement()
								+ currEntity.getPhaseBudgets().get(j).getPartSystemControlAndManagement());

				totalEntity.getPhaseBudgets().get(j)
						.setPartAuditOfOperations(totalEntity.getPhaseBudgets().get(j).getPartAuditOfOperations()
								+ currEntity.getPhaseBudgets().get(j).getPartAuditOfOperations());

				totalEntity.getPhaseBudgets().get(j).setPartAuthoritiesCertification(
						totalEntity.getPhaseBudgets().get(j).getPartAuthoritiesCertification()
								+ currEntity.getPhaseBudgets().get(j).getPartAuthoritiesCertification());

				totalEntity.getPhaseBudgets().get(j)
						.setPartIntermEvaluation(totalEntity.getPhaseBudgets().get(j).getPartIntermEvaluation()
								+ currEntity.getPhaseBudgets().get(j).getPartIntermEvaluation());

			}

			Double totalSum = 0.;

			for (Phase phase : totalEntity.getPhaseBudgets()) {

				totalSum = totalSum + phase.getPartAdvInfoProducts() + phase.getPartAdvInfoPublicEvents()
						+ phase.getPartDurableGoods() + phase.getPartDurables() + phase.getPartHumanRes()
						+ phase.getPartMissions() + phase.getPartOther() + phase.getPartOverheads()
						+ phase.getPartProvisionOfService() + phase.getPartContainer() + phase.getPartPersonalExpenses()
						+ phase.getPartCommunicationAndInformation() + phase.getPartOrganizationOfCommitees()
						+ phase.getPartOtherFees() + phase.getPartAutoCoordsTunis() + phase.getPartContactPoint()
						+ phase.getPartSystemControlAndManagement() + phase.getPartAuditOfOperations()
						+ phase.getPartAuthoritiesCertification() + phase.getPartIntermEvaluation();
			}
			totalEntity.setTotalAmount(totalSum);
		}

		return totalEntity;
	}

	/**
	 * Sets listPB
	 * 
	 * @param listPB
	 *            the listPB to set
	 */
	public void setListPB(List<PartnersBudgets> listPB) {
		this.listPB = listPB;
	}

	/**
	 * Gets listPB
	 * 
	 * @return listPB the listPB
	 */
	public List<PartnersBudgets> getListPB() {
		return this.listPB;
	}

	/**
	 * Gets currentPartner
	 * 
	 * @return currentPartner the currentPartner
	 */
	public CFPartnerAnagraphs getCurrentPartner() {
		return currentPartner;
	}

	/**
	 * Sets currentPartner
	 * 
	 * @param currentPartner
	 *            the currentPartner to set
	 */
	public void setCurrentPartner(CFPartnerAnagraphs currentPartner) {
		this.currentPartner = currentPartner;
	}

	/**
	 * Sets projectCompletion
	 * 
	 * @param projectCompletion
	 *            the projectCompletion to set
	 */
	public void setProjectCompletion(ProjectIformationCompletations projectCompletion) {
		this.projectCompletion = projectCompletion;
	}

	/**
	 * Gets projectCompletion
	 * 
	 * @return projectCompletion the projectCompletion
	 */
	public ProjectIformationCompletations getProjectCompletion() {
		return projectCompletion;
	}

	/**
	 * Sets currentGeneralBudget
	 * 
	 * @param currentGeneralBudget
	 *            the currentGeneralBudget to set
	 */
	public void setCurrentGeneralBudget(GeneralBudgets currentGeneralBudget) {
		this.currentGeneralBudget = currentGeneralBudget;
	}

	/**
	 * Gets currentGeneralBudget
	 * 
	 * @return currentGeneralBudget the currentGeneralBudget
	 */
	public GeneralBudgets getCurrentGeneralBudget() {
		return currentGeneralBudget;
	}

	/**
	 * Sets spanValidatorVisibility
	 * 
	 * @param spanValidatorVisibility
	 *            the spanValidatorVisibility to set
	 */
	public void setSpanValidatorVisibility(String spanValidatorVisibility) {
		this.spanValidatorVisibility = spanValidatorVisibility;
	}

	public String getSpanValidatorVisibility() {
		return spanValidatorVisibility;
	}

	/**
	 * Gets cdPhases
	 * 
	 * @return cdPhases the cdPhases
	 */
	public List<CostDefinitionPhases> getCdPhases() {
		return cdPhases;
	}

	/**
	 * Sets cdPhases
	 * 
	 * @param cdPhases
	 *            the cdPhases to set
	 */
	public void setCdPhases(List<CostDefinitionPhases> cdPhases) {
		this.cdPhases = cdPhases;
	}

	/**
	 * Gets showPhase7
	 * 
	 * @return showPhase7 the showPhase7
	 */
	public Boolean getShowPhase7() {
		return showPhase7;
	}

	/**
	 * Sets showPhase7
	 * 
	 * @param showPhase7
	 *            the showPhase7 to set
	 */
	public void setShowPhase7(Boolean showPhase7) {
		this.showPhase7 = showPhase7;
	}

	public Boolean getAsse3() {
		return isAsse3;
	}

	public void setAsse3(Boolean isAsse3) {
		this.isAsse3 = isAsse3;
	}

	public DataStructureTab getTabForAsse3() {
		return tabForAsse3;
	}

	public void setTabForAsse3(DataStructureTab tabsForAsse3) {
		this.tabForAsse3 = tabsForAsse3;
	}

	public List<CostAsse3> getLegend() {
		return legend;
	}

	public void setLegend(List<CostAsse3> legend) {
		this.legend = legend;
	}

}
