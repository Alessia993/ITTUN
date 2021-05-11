package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.CostManagement;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

public class CostManagementListBean extends EntityProjectListBean<CostManagement> {

	public enum CostItemTypes {
		StaffCostsRealCosts("2", "Staff costs - real costs", 0.4), StaffCostsFlatRate("3", "Staff costs - flat rate",
				0.2), OfficeAndAdministrativeExpenditure("4", "Office and administrative expenditure", 0.1);

		private CostItemTypes(String code, String description, double percentage) {
			this.code = code;
			this.description = description;
			this.percentage = percentage;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}

		private String code;

		private String description;

		private double percentage;

	}

	private boolean show;

	private boolean empty;

	private Transaction tr;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.isShow()) {
			check();
		}
	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		this.setShow(false);
	}

	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		try {

			for (CostManagement cm : this.getList()) {
				BeansFactory.CostManagement().SaveInTransaction(cm);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Boolean BeforeSave() throws PersistenceBeanException {
		return true;
	}

	public void Page_Save() {
		if (getSaveFlag() == 0) {
			try {
				if (!this.BeforeSave()) {
					return;
				}
			} catch (PersistenceBeanException e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			} catch (Exception e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			}

			try {
				tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			} catch (HibernateException e1) {
				log.error("EntityEditBean exception:", e1);
			} catch (PersistenceBeanException e1) {
				log.error("EntityEditBean exception:", e1);
			}
			setSaveFlag(1);
			try {
				this.SaveEntity();
			} catch (HibernateException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (PersistenceBeanException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (NumberFormatException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (IOException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (Exception e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} finally {
				if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
					tr.commit();
				}
			}

			setSaveFlag(0);
		}
	}

	public void check() throws HibernateException, PersistenceBeanException {
		this.setShow(true);
		fillList();
		this.ReRenderScroll();
	}

	private void fillList() throws PersistenceBeanException {
		String projectId = String.valueOf(this.getSession().get("project"));
		List<CFPartnerAnagraphProject> listCFPartner = this.getCFPartnerList(projectId);

		this.setEmpty(true);
		this.setList(new ArrayList<CostManagement>());

		for (CFPartnerAnagraphProject cf : listCFPartner) {
			Users users = cf.getCfPartnerAnagraphs().getUser();
			//cfpa.type.id
			
			List<CostDefinitions> cds = BeansFactory.CostDefinitions().GetByPartnerInDur(projectId, users.getId(), DurStateTypes.Create.getValue());
			BigDecimal staffCostsRealCostsSum = new BigDecimal(0);
			BigDecimal staffCostsFlatRateSum = new BigDecimal(0);
			BigDecimal officeAndAdministrativeExpenditureSum = new BigDecimal(0);
			BigDecimal costDirectSum = new BigDecimal(0);
			for (CostDefinitions cd : cds) {
				BigDecimal cost = new BigDecimal(getCost(cd,cf.getType()));
				if (CostItemTypes.StaffCostsRealCosts.getCode().equals(String.valueOf(cd.getCostItem().getId()))) {
					staffCostsRealCostsSum = staffCostsRealCostsSum.add(cost);
				} else if (CostItemTypes.StaffCostsFlatRate.getCode()
						.equals(String.valueOf(cd.getCostItem().getId()))) {
					staffCostsFlatRateSum = staffCostsFlatRateSum.add(cost);
				} else if (CostItemTypes.OfficeAndAdministrativeExpenditure.getCode()
						.equals(String.valueOf(cd.getCostItem().getId()))) {
					officeAndAdministrativeExpenditureSum = officeAndAdministrativeExpenditureSum.add(cost);
				} else {
					//costDirectSum += cost;
					costDirectSum = costDirectSum.add(cost);
				}
			}
			verificationCostItem(users, officeAndAdministrativeExpenditureSum.setScale(2, RoundingMode.HALF_UP).doubleValue(), staffCostsRealCostsSum.setScale(2, RoundingMode.HALF_UP).doubleValue(),
					staffCostsFlatRateSum.setScale(2, RoundingMode.HALF_UP).doubleValue(), costDirectSum.setScale(2, RoundingMode.HALF_UP).doubleValue());

		}
		Page_Save();
	}

	private List<CFPartnerAnagraphProject> getCFPartnerList(String projectId)
			throws HibernateException, PersistenceBeanException {

		List<CFPartnerAnagraphProject> listCFs = BeansFactory.CFPartnerAnagraphProject()
				.GetCFAnagraphForProject(projectId, CFAnagraphTypes.CFAnagraph);

		List<CFPartnerAnagraphProject> listPartners = BeansFactory.CFPartnerAnagraphProject()
				.GetCFAnagraphForProject(projectId, CFAnagraphTypes.PartnerAnagraph);
		List<CFPartnerAnagraphProject> listCFPartner = new ArrayList<>(listCFs);
		listCFPartner.addAll(listPartners);
		return listCFPartner;
	}

	private void verificationCostItem(Users users, double officeAndAdministrativeExpenditureSum,
			double staffCostsRealCostsSum, double staffCostsFlatRateSum, double costDirectSum)
			throws PersistenceBeanException {

		List<CostManagement> list = this.getList();
		for (CostItemTypes cit : CostItemTypes.values()) {
			list.add(this.verificationCostItems(cit, users, officeAndAdministrativeExpenditureSum,
					staffCostsRealCostsSum, staffCostsFlatRateSum, costDirectSum));
		}
	}

	private CostManagement verificationCostItems(CostItemTypes cit, Users users,
			double officeAndAdministrativeExpenditureSum, double staffCostsRealCostsSum, double staffCostsFlatRateSum,
			double costDirectSum) throws PersistenceBeanException {
		
		

		double calculatedAmount = this.calculatedAmountForCostItem(cit, officeAndAdministrativeExpenditureSum,
				staffCostsRealCostsSum, staffCostsFlatRateSum);
		double amountBeAchieved = amountBeAchievedForCostItem(cit, staffCostsRealCostsSum, staffCostsFlatRateSum,
				costDirectSum);

		
		double absoluteDifference = new BigDecimal(calculatedAmount - amountBeAchieved).setScale(2, RoundingMode.HALF_UP).doubleValue();
		double percentageVariation = 0;
		if(amountBeAchieved == 0 && calculatedAmount == 0){
			percentageVariation = 0;
		}else if(amountBeAchieved == 0 ){
			percentageVariation = -100;
		}else if(calculatedAmount == 0){
			percentageVariation = 100;
		}else{
			percentageVariation = new BigDecimal(((amountBeAchieved / calculatedAmount) * 100) - 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
		}
		//double percentageVariation = ((amountBeAchieved / calculatedAmount) * 100) - 100;
		boolean conciliated = absoluteDifference <= 0;

		CostItems ci = BeansFactory.CostItems().LoadByCode(cit.getCode());
		CostManagement cm = BeansFactory.CostManagement().loadByProjectUserCostItem(Long.valueOf(this.getProjectId()),
				users.getId(), ci.getId());
		if (cm == null) {
			cm = new CostManagement(users, ci, calculatedAmount, amountBeAchieved, absoluteDifference,
					String.valueOf(percentageVariation), conciliated, this.getProject());
		} else {
			cm.setCalculatedAmount(calculatedAmount);
			cm.setAmountBeAchieved(amountBeAchieved);
			cm.setAbsoluteDifference(absoluteDifference);
			cm.setPercentageVariation(String.valueOf(percentageVariation));
			cm.setConciliated(conciliated);
		}
		return cm;
	}

	private double calculatedAmountForCostItem(CostItemTypes cit, double officeAndAdministrativeExpenditureSum,
			double staffCostsRealCostsSum, double staffCostsFlatRateSum) {
		BigDecimal calculatedAmount = new BigDecimal(0);
		switch (cit.getCode()) {
		case "2":
			calculatedAmount = new BigDecimal(staffCostsRealCostsSum);
			break;
		case "3":
			calculatedAmount = new BigDecimal(staffCostsFlatRateSum);
			break;
		case "4":
			calculatedAmount = new BigDecimal(officeAndAdministrativeExpenditureSum);
			break;
		}
		return calculatedAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	private double amountBeAchievedForCostItem(CostItemTypes cit, double staffCostsRealCostsSum,
			double staffCostsFlatRateSum, double costDirectSum) {
		BigDecimal amountBeAchieved = new BigDecimal(0);
		switch (cit.getCode()) {
		case "2":
			amountBeAchieved = new BigDecimal((costDirectSum + staffCostsRealCostsSum) * cit.getPercentage());
			break;
		case "3":
			amountBeAchieved =  new BigDecimal(costDirectSum * cit.getPercentage());
			break;
		case "4":
			amountBeAchieved =  new BigDecimal((staffCostsRealCostsSum + staffCostsFlatRateSum) * cit.getPercentage());
			break;
		}
		return amountBeAchieved.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	

	private double getCost(CostDefinitions cost, CFPartnerAnagraphTypes type) {
//		if (cd.getDurCompilation().getCertifiedDate() != null
//				&& cd.getDurCompilation().getDurState().getId().equals(DurStateTypes.Certified.getValue()) && cd.getAcuCertification()!=null) {
//			return cd.getAcuCertification();
//		} else if (cd.getDurCompilation().getCertifiedDate() == null && cd.getACUSertified() && cd.getAcuCertification()!=null) {
//			return cd.getAcuCertification();
//		} else if (cd.getDurCompilation().getCertifiedDate() == null && cd.getAGUSertified() && cd.getAguCertification()!=null) {
//			return cd.getAguCertification();
//		} else if (cd.getDurCompilation().getCertifiedDate() == null && cd.getSTCSertified() && cd.getStcCertification()!=null) {
//			return cd.getStcCertification();
//		}else if (cd.getDurCompilation().getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue()) && cd.getCilCheck()!=null && type.getId().equals(CFAnagraphTypes.CFAnagraph.getCfType())) {
//			return cd.getCilCheck();
//		}else if(cd.getDurCompilation().getDurState().getId().equals(DurStateTypes.STCEvaluation.getValue()) && cd.getCfCheck()!=null && type.getId().equals(CFAnagraphTypes.PartnerAnagraph.getCfType())){
//			return cd.getCfCheck();
//		}
		
		if (cost.getACUSertified() && cost.getAcuCertification() != null) {
			return cost.getAcuCertification();
		} else if (cost.getAGUSertified() && cost.getAguCertification() != null) {
			return cost.getAguCertification();
		} else if (cost.getSTCSertified() && cost.getStcCertification() != null) {
			return cost.getStcCertification();
		} else if (cost.getCfCheck() != null) {
			return cost.getCfCheck();
		} else if (cost.getCilCheck() != null) {
			return cost.getCilCheck();
		}
		
		return 0;
	}

	public boolean isShow() {
		this.show = (boolean) this.getViewState().get("show");
		return show;
	}

	public void setShow(boolean show) {
		this.getViewState().put("show", show);
		this.show = show;
	}

	public boolean isEmpty() {
		this.empty = (boolean) this.getViewState().get("empty");
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.getViewState().put("empty", empty);
		this.empty = empty;
	}

	@Override
	public void editEntity() {
	}

	@Override
	public void deleteEntity() {
	}

	@Override
	public void addEntity() {
	}

	/**
	 * Sets saveFlag
	 * 
	 * @param saveFlag
	 *            the saveFlag to set
	 */
	public void setSaveFlag(int saveFlag) {
		this.getViewState().put("saveFlag", saveFlag);
	}

	/**
	 * Gets saveFlag
	 * 
	 * @return saveFlag the saveFlag
	 */
	public int getSaveFlag() {
		return this.getViewState().get("saveFlag") == null ? 0 : (Integer) this.getViewState().get("saveFlag");
	}

}
