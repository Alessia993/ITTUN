/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostItems;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.CFAnagraphWrapper;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class CFPartnerListBean extends EntityProjectListBean<CFPartnerAnagraphs> {
	private List<CFAnagraphWrapper> listCFAnagraph;

	private List<CFAnagraphWrapper> listCFPartnerAnagraphs;

	private boolean newCFAnagraphAvailable;

	private Long entityPartnerEditId;

	private Long entityPartnerDeleteId;

	private boolean currentUserIsPb;

	private boolean editMode;

	private boolean asse5Project;

	private int asse;

	public void ReRenderScroll() {
		if (getListCFPartnerAnagraphs() != null
				&& getListCFPartnerAnagraphs().size() > Integer.parseInt(this.getItemsPerPage())) {
			this.setShowScroll(true);
		} else {
			this.setShowScroll(false);
		}
	}

	@Override
	public void Page_Load()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException {
		this.CheckCurrentUserIsPB();

		if (this.getSession().get("project") != null) {
			Projects currentProject = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));

			if (currentProject.getAsse() < 5) {
				this.setAsse5Project(false);
			} else {
				this.setAsse5Project(true);
			}
			
			this.setAsse(currentProject.getAsse());
			
			if (!this.currentUserIsPb || this.getSessionBean().isAAU()) {
				List<CFPartnerAnagraphProject> listCF = null;
				List<CFPartnerAnagraphProject> listPartners = null;

				listCF = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProject(
						String.valueOf(this.getSession().get("project")), CFAnagraphTypes.CFAnagraph);

				if ((listCF == null || listCF.isEmpty()) && this.isAsse5Project()) {
					listCF = new ArrayList<CFPartnerAnagraphProject>();
				}

				listPartners = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProject(
						String.valueOf(this.getSession().get("project")), CFAnagraphTypes.PartnerAnagraph);

				this.newCFAnagraphAvailable = listCF.size() < 1;

				this.listCFAnagraph = new ArrayList<CFAnagraphWrapper>();
				this.listCFPartnerAnagraphs = new ArrayList<CFAnagraphWrapper>();

				for (CFPartnerAnagraphProject cfpap : listCF) {
					CFAnagraphWrapper cfAnagraph = new CFAnagraphWrapper(cfpap);
					this.listCFAnagraph.add(cfAnagraph);
				}

				for (CFPartnerAnagraphProject cfpap : listPartners) {
					CFAnagraphWrapper cfAnagraph = new CFAnagraphWrapper(cfpap);
					this.listCFPartnerAnagraphs.add(cfAnagraph);
				}
			} else {
				this.listCFAnagraph = new ArrayList<CFAnagraphWrapper>();

				List<CFPartnerAnagraphProject> listPartners = null;

				listPartners = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProjectAndUser(
						String.valueOf(this.getSession().get("project")),
						this.getSessionBean().getCurrentUser().getId());

				this.listCFPartnerAnagraphs = new ArrayList<CFAnagraphWrapper>();

				for (CFPartnerAnagraphProject cfpap : listPartners) {
					CFAnagraphWrapper anagraphWrapper = new CFAnagraphWrapper(cfpap);
					this.listCFPartnerAnagraphs.add(anagraphWrapper);
				}
			}

			for (CFAnagraphWrapper p : this.listCFAnagraph) {
				/*
				 * p .setEditable(currentProject.getState().getCode() ==
				 * ProjectState.FoundingEligible .getCode() ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isSTC() &&
				 * this.getProjectAsse() < 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.France.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isSTC() &&
				 * this.getProjectAsse() < 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.Italy.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isAGU() &&
				 * this.getProjectAsse() == 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.France.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isAGU() &&
				 * this.getProjectAsse() == 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.Italy.getCountry())));
				 */
				p.setEditable(currentProject.getState().getCode() == ProjectState.FoundingEligible.getCode()
						|| currentProject.getState().getCode() == ProjectState.Actual.getCode());
			}

			for (CFAnagraphWrapper p : this.listCFPartnerAnagraphs) {
				/*
				 * p .setEditable(currentProject.getState().getCode() ==
				 * ProjectState.FoundingEligible .getCode() ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isSTC() &&
				 * this.getProjectAsse() < 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.France.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isSTC() &&
				 * this.getProjectAsse() < 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.Italy.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isAGU() &&
				 * this.getProjectAsse() == 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.France.getCountry())) ||
				 * (currentProject.getState().getCode() == ProjectState.Actual
				 * .getCode() && this.getSessionBean().isAGU() &&
				 * this.getProjectAsse() == 5 && p
				 * .getCountry().getCode().equals(
				 * CountryTypes.Italy.getCountry())));
				 */
				p.setEditable(currentProject.getState().getCode() == ProjectState.FoundingEligible.getCode()
						|| currentProject.getState().getCode() == ProjectState.Actual.getCode());
				// if (p.isFictive())
				// {
				// p.setName(Utils
				// .getString(CountryTypes.Italy.getCountry().equals(
				// p.getCountry().getCode()) ? "fictivePartnerAnagraphItalia"
				// : "fictivePartnerAnagraphFrancia"));
				// }
			}
		}
	}

	public void Page_Load_Static() throws PersistenceBeanException {
		// if (!AccessGrantHelper.getIsAGUAsse5())
		// {
		// this.goTo(PagesTypes.PROJECTINDEX);
		// }
	}

	public void addCFAnagraph() {
		this.getSession().put("cfanagraph", null);
		this.getSession().put("isCFpartner", false);
		this.goTo(PagesTypes.CFPARTNEREDIT);
	}

	public void editCFAnagraph() {
		this.getSession().put("cfanagraph", this.getEntityEditId());
		this.getSession().put("isCFpartner", false);
		this.goTo(PagesTypes.CFPARTNEREDIT);
	}

	private void CheckCurrentUserIsPB() {
		if (this.getSessionBean().isPartner()) {
			if (!(this.getSessionBean().isSTC() || this.getSessionBean().isCF() || this.getSessionBean().isAGU())) {
				this.setCurrentUserIsPb(true);
			} else {
				this.setCurrentUserIsPb(false);
			}
		} else {
			this.setCurrentUserIsPb(false);
		}
	}

	/**
	 * Deletes CF anagraph
	 */
	public void deleteItem() {
		try {
			CFPartnerAnagraphs.DeleteCFAnagraph(this.getEntityDeleteId(),
					Long.valueOf(String.valueOf(this.getSession().get("project"))));

			this.Page_Load();
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	public void addPartnerAnagraph() {
		this.getSession().put("cfanagraph", null);
		this.getSession().put("isCFpartner", true);
		this.goTo(PagesTypes.CFPARTNEREDIT);
	}

	public void editPartnerAnagraph() {
		this.getSession().put("cfanagraph", this.getEntityPartnerEditId());
		this.getSession().put("isCFpartner", true);
		this.goTo(PagesTypes.CFPARTNEREDIT);
	}

	public void deletePartnerItem() {
		try {
			if (getSessionBean().getProjectLock() && getSessionBean().isSTC()) {
				Transaction tr = null;
				try {
					tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					CFPartnerAnagraphs cfpa = BeansFactory.CFPartnerAnagraphs().Load(this.getEntityPartnerDeleteId());

					CFPartnerAnagraphs fictivePartner = BeansFactory.CFPartnerAnagraphs()
							.getFictivePartnerByProjectAndCountry(getSessionBean().getProject().getId(),
									cfpa.getCountry().getCode());

					List<PartnersBudgets> partnerFictiveBudgetsList = new ArrayList<PartnersBudgets>();

					if (fictivePartner != null) {
						partnerFictiveBudgetsList = BeansFactory.PartnersBudgets().GetLastByPartner(getProjectId(),
								fictivePartner.getId().toString());
					}

					if (fictivePartner == null) {
						fictivePartner = new CFPartnerAnagraphs();

						Users fictiveUser = new Users();
						fictiveUser.setFictive(true);
						fictiveUser.setName("fictive_partner");
						fictiveUser.setSurname(getSessionBean().getProject().getCode());

						fictiveUser.setAdmin(false);
						fictiveUser.setFiscalCode("0000000000");
						fictiveUser
								.setLogin(this.BuildLogin("fictive_partner", getSessionBean().getProject().getCode()));
						fictiveUser.setMail(String.format("%s@aa.aa", fictiveUser.getLogin()));
						fictiveUser.setActive(true);
						fictiveUser.setShouldChangePassword(true);
						String pwd = UUID.randomUUID().toString().substring(0, 8);
						fictiveUser.setPassword(MD5.encodeString(pwd, null));
						fictiveUser.setPassword2(MD5.encodeString(fictiveUser.getPassword(), null));
						BeansFactory.Users().SaveInTransaction(fictiveUser);

						UserRoles ur = new UserRoles(fictiveUser, BeansFactory.Roles().GetRoleByName(UserRoleTypes.B),
								true);
						BeansFactory.UserRoles().SaveInTransaction(ur);

						fictivePartner.setUser(fictiveUser);
						fictivePartner.setName(
								Utils.getString(CountryTypes.Italy.getCountry().equals(cfpa.getCountry().getCode())
										? "fictivePartnerAnagraphItalia" : "fictivePartnerAnagraphFrancia"));
						fictivePartner.setCountry(cfpa.getCountry());

						BeansFactory.CFPartnerAnagraphs().SaveInTransaction(fictivePartner);

						CFPartnerAnagraphProject cfPartnerAnagraphProject = new CFPartnerAnagraphProject();

						cfPartnerAnagraphProject.setCfPartnerAnagraphs(fictivePartner);
						cfPartnerAnagraphProject.setNaming(fictivePartner.getNaming());
						cfPartnerAnagraphProject.setProgressiveId(BeansFactory.CFPartnerAnagraphProject()
								.GetMaxIdCFAnagraphForProject(this.getProjectId()));

						CFPartnerAnagraphTypes c = BeansFactory.CFPartnerAnagraphTypes()
								.GetByType(CFAnagraphTypes.PartnerAnagraph);
						cfPartnerAnagraphProject.setType(c);
						cfPartnerAnagraphProject.setProject(getSessionBean().getProject());

						BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(cfPartnerAnagraphProject);
					}
					// TODO

					CFPartnerAnagraphProject cfpAnagraphProject = BeansFactory.CFPartnerAnagraphProject()
							.LoadByPartner(this.getProject().getId(), cfpa.getId());

					if (cfpAnagraphProject != null) {
						cfpAnagraphProject.setNotAssigned(true);
					}

					BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(cfpAnagraphProject);

					ProjectIformationCompletations pic = BeansFactory.ProjectIformationCompletations()
							.LoadByProject(this.getProjectId());

					Calendar cal = Calendar.getInstance();
					cal.setTime(pic.getStartYear());

					int year = cal.get(Calendar.YEAR);

					List<PartnersBudgets> pbList = BeansFactory.PartnersBudgets()
							.GetByProjectAndPartner(this.getProjectId(), String.valueOf(cfpa.getId()), true);

					BeansFactory.CFPartnerAnagraphs().SaveInTransaction(fictivePartner);
					savePB(cfpa, fictivePartner, pbList, partnerFictiveBudgetsList, year);
					BeansFactory.CFPartnerAnagraphs().SaveInTransaction(cfpa);

					saveGB(cfpa, fictivePartner, pbList, partnerFictiveBudgetsList);
				} catch (Exception e) {
					if (tr != null && tr.isActive()) {
						tr.rollback();
					}
					log.error(e);
				} finally {
					if (tr != null && tr.isActive() && !tr.wasRolledBack()) {
						tr.commit();
					}
				}

			} else {
				CFPartnerAnagraphs.DeleteCFAnagraph(this.getEntityPartnerDeleteId(),
						Long.valueOf(String.valueOf(this.getSession().get("project"))));
			}
			this.Page_Load();
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	/**
	 * @param cfpa
	 * @param fictivePartner
	 * @param pbList
	 * @param partnerFictiveBudgetsList
	 * @param year
	 * @param mapBalances
	 * @throws PersistenceBeanException
	 */
	private void savePB(CFPartnerAnagraphs cfpa, CFPartnerAnagraphs fictivePartner, List<PartnersBudgets> pbList,
			List<PartnersBudgets> partnerFictiveBudgetsList, int year) throws PersistenceBeanException {
		Calendar c = Calendar.getInstance();
		Map<Integer, Map<Integer, Double>> mapBalances = new HashMap<Integer, Map<Integer, Double>>();

		Map<Integer, Map<Integer, Map<Integer, Double>>> freeBudgets = new HashMap<Integer, Map<Integer, Map<Integer, Double>>>();

		for (CostDefinitionPhases phase : BeansFactory.CostDefinitionPhase().Load()) {
			Map<Integer, Double> budgetForPhase = new HashMap<Integer, Double>();
			for (CostItems item : BeansFactory.CostItems().Load()) {
				budgetForPhase.put(item.getId().intValue(), 0d);
			}
			mapBalances.put(phase.getId().intValue(), budgetForPhase);
		}

		List<CostDefinitions> cdList = BeansFactory.CostDefinitions().GetForProjectCheckValues(this.getProjectId(),
				cfpa.getUser().getId());

		for (int counter = 0; counter < pbList.size(); ++counter)
		// for (PartnersBudgets pb : pbList)
		{
			PartnersBudgets pb = pbList.get(counter);

			int tempYear = year - 1 + pb.getYear();

			PartnersBudgets pbf = null;

			for (PartnersBudgets i : partnerFictiveBudgetsList) {
				if (i.getYear() == pb.getYear()) {
					pbf = i;
				}
			}

			if (pbf == null) {
				pbf = PartnersBudgets.create(getSessionBean().getProject(), fictivePartner);
				pbf.setYear(pb.getYear());
				pbf.setDescription(
						String.format("Fictive partner budget %d %s", pb.getYear(), cfpa.getCountry().getValue()));
				Long maxVersion = BeansFactory.PartnersBudgets().GetLastVersionNumber(this.getProjectId(),
						fictivePartner.getId().toString(), null);

				if (maxVersion == null) {
					maxVersion = 0l;
				}

				pbf.setVersion(maxVersion);
				partnerFictiveBudgetsList.add(pbf);
			}

			for (CostDefinitionPhases phaseItem : BeansFactory.CostDefinitionPhase().Load()) {
				for (CostItems item : BeansFactory.CostItems().Load()) {
					Double amountFromPartner = null;
					try {
						amountFromPartner = getAmountByCostItem(pb, item.getId().intValue(),
								phaseItem.getId().intValue());
					} catch (NullPointerException e) {
						amountFromPartner = 0d;
					}

					double sumA = 0d;
					if (amountFromPartner == null) {
						amountFromPartner = 0d;
					}

					if (cdList != null && !cdList.isEmpty()) {
						for (CostDefinitions cd : cdList) {
							if (cd.getDocumentDate() != null && cd.getCostItem() != null) {
								c.setTime(cd.getDocumentDate());

								boolean afterLast = false;
								if (counter == (pbList.size() - 1) && c.get(Calendar.YEAR) > tempYear) {
									afterLast = true;
								}

								if ((tempYear == c.get(Calendar.YEAR)
										|| (c.get(Calendar.YEAR) < year && tempYear == year) || (afterLast))
										&& item.equals(cd.getCostItem())
										&& phaseItem.getId().equals(cd.getCostDefinitionPhase().getId())) {

									if (cd.getCostState() != null
											&& cd.getCostState()
													.equals(BeansFactory.CostStates()
															.Load(CostStateTypes.Closed.getState()))
											&& !cd.getWasRefusedByCil() || Boolean.TRUE.equals(cd.getDismiss())) {
										continue;
									}

									if (cd.getAcuCertification() != null
											// && cd.getAcuCertification() > 0
											&& cd.getACUSertified()) {
										sumA += cd.getAcuCertification();
									} else if (cd.getAguCertification() != null
											// && cd.getAguCertification() > 0
											&& cd.getAGUSertified()) {
										sumA += cd.getAguCertification();
									} else if (cd.getStcCertification() != null
											// && cd.getStcCertification() > 0
											&& cd.getSTCSertified()) {
										sumA += cd.getStcCertification();
									} else if (// cd.getCfCheck() > 0 &&
									cd.getCfCheck() != null) {
										sumA += cd.getCfCheck();
									} else if (cd.getCilCheck() != null
											// && cd.getCilCheck() > 0
											&& !cd.getCilIntermediateStepSave()) {
										sumA += cd.getCilCheck();
									} else {
										sumA += (cd.getAmountReport() != null ? cd.getAmountReport() : 0d);
									}

								}
							}
						}
					}

					if (mapBalances.get(phaseItem.getId().intValue()).get(item.getId().intValue()) != 0) {
						sumA += mapBalances.get(phaseItem.getId().byteValue()).get(item.getId().intValue());
					}

					if (sumA > amountFromPartner) {
						mapBalances.get(phaseItem.getId().intValue()).put(item.getId().intValue(),
								sumA - amountFromPartner);

						pb = this.setAmountByCostItem(pb, item.getId().intValue(), phaseItem.getId().intValue(),
								sumA - amountFromPartner);
					} else {
						mapBalances.get(phaseItem.getId().intValue()).put(item.getId().intValue(), 0d);

						pb = this.setAmountByCostItem(pb, item.getId().intValue(), phaseItem.getId().intValue(), sumA);
						if (!freeBudgets.containsKey(phaseItem.getId().intValue())) {
							freeBudgets.put(phaseItem.getId().intValue(), new HashMap<Integer, Map<Integer, Double>>());
						}
						Map<Integer, Double> map = freeBudgets.get(phaseItem.getId().intValue()).get(counter);
						if (map == null) {
							map = new HashMap<Integer, Double>();
							freeBudgets.get(phaseItem.getId().intValue()).put(counter, map);
						}
						double freeAmount = getAmountByCostItem(pbf, item.getId().intValue(),
								phaseItem.getId().intValue()) + amountFromPartner - sumA;
						map.put(item.getId().intValue(), freeAmount);
						pbf = this.setAmountByCostItem(pbf, item.getId().intValue(), phaseItem.getId().intValue(),
								freeAmount);
					}
				} // end for costitem
			}
		} // end for partnerBudgets

		for (Map.Entry<Integer, Map<Integer, Double>> wholeEntry : mapBalances.entrySet()) {

			for (Map.Entry<Integer, Double> entry : wholeEntry.getValue().entrySet()) {
				Double remains = entry.getValue();
				if (remains > 0.0) {
					for (Entry<Integer, Map<Integer, Map<Integer, Double>>> wholeBudget : freeBudgets.entrySet())
						// year:coisItemId:Sum
						for (Entry<Integer, Map<Integer, Double>> budget : wholeBudget.getValue().entrySet()) {
							Double freeAmount = budget.getValue().get(entry.getKey());
							if (freeAmount != null) {
								Double submit = remains - freeAmount;
								{
									PartnersBudgets pb = pbList.get(budget.getKey());
									PartnersBudgets pbf = null;
									for (PartnersBudgets i : partnerFictiveBudgetsList) {
										if (i.getYear() == pb.getYear()) {
											pbf = i;
										}
									}
									double amountFromPartner = getAmountByCostItem(pb, entry.getKey(),
											wholeEntry.getKey());
									double amountFromPartnerFictive = getAmountByCostItem(pbf, entry.getKey(),
											wholeEntry.getKey());
									if (submit > 0) {
										remains -= freeAmount;
										pb = this.setAmountByCostItem(pb, entry.getKey(), wholeEntry.getKey(),
												amountFromPartner + freeAmount);
										pbf = this.setAmountByCostItem(pbf, entry.getKey(), wholeEntry.getKey(),
												amountFromPartnerFictive - freeAmount);
									} else {
										pb = this.setAmountByCostItem(pb, entry.getKey(), wholeBudget.getKey(),
												amountFromPartner + remains);
										pbf = this.setAmountByCostItem(pbf, entry.getKey(), wholeEntry.getKey(),
												amountFromPartnerFictive - remains);
										break;
									}
								}
							}
						}
				}
			}
		}

		saveBudgetList(pbList);
		saveBudgetList(partnerFictiveBudgetsList);
	}

	/**
	 * @param pbList
	 * @throws PersistenceBeanException
	 */
	private void saveBudgetList(List<PartnersBudgets> pbList) throws PersistenceBeanException {
		for (PartnersBudgets pb : pbList) {
			pb.setTotalAmount(0d);
			for (Phase phase : pb.getPhaseBudgets()) {
				pb.setTotalAmount(pb.getTotalAmount() + phase.getPartAdvInfoProducts()
						+ phase.getPartAdvInfoPublicEvents() + phase.getPartDurableGoods() + phase.getPartDurables()
						+ phase.getPartHumanRes() + phase.getPartMissions() + phase.getPartOther()
						+ phase.getPartOverheads() + phase.getPartProvisionOfService() + phase.getPartContainer()
						+ phase.getPartPersonalExpenses() + phase.getPartCommunicationAndInformation()
						+ phase.getPartOrganizationOfCommitees() + phase.getPartOtherFees()
						+ phase.getPartAutoCoordsTunis() + phase.getPartContactPoint()
						+ phase.getPartSystemControlAndManagement() + phase.getPartAuditOfOperations()
						+ phase.getPartAuthoritiesCertification() + phase.getPartIntermEvaluation());
				BeansFactory.PartnersBudgetsPhases().SaveInTransaction(phase);
			}
			pb.setTotalAmount(pb.getTotalAmount() + (pb.getBudgetRelease() == null ? 0 : pb.getBudgetRelease()));
			BeansFactory.PartnersBudgets().SaveInTransaction(pb);
		}
	}

	/**
	 * @param cfpa
	 * @param fictivePartner
	 * @param pbList
	 * @param partnerFictiveBudgetsList
	 * @throws PersistenceBeanException
	 */
	private void saveGB(CFPartnerAnagraphs cfpa, CFPartnerAnagraphs fictivePartner, List<PartnersBudgets> pbList,
			List<PartnersBudgets> partnerFictiveBudgetsList) throws PersistenceBeanException {
		GeneralBudgets deletedPartnerGB = BeansFactory.GeneralBudgets().GetActualItemForCFAnagraph(this.getProjectId(),
				cfpa.getId());
		double sumTotal = 0d;
		if (deletedPartnerGB != null) {
			for (PartnersBudgets partnersBudgets : pbList) {
				sumTotal += partnersBudgets.getTotalAmount();
			}
			GeneralBudgets cloneDelGB = deletedPartnerGB.clone();
			deletedPartnerGB.setIsOld(true);
			double fesrAmount = NumberHelper.CurrencyFormat(sumTotal * 0.75);
			cloneDelGB.setFesr(fesrAmount);
			cloneDelGB.setCnPublic(NumberHelper.CurrencyFormat(sumTotal - fesrAmount));
			cloneDelGB.setBudgetTotal(NumberHelper.CurrencyFormat(sumTotal));
			BeansFactory.GeneralBudgets().SaveInTransaction(deletedPartnerGB);
			BeansFactory.GeneralBudgets().SaveInTransaction(cloneDelGB);

			GeneralBudgets fictivePartnerGB = BeansFactory.GeneralBudgets()
					.GetActualItemForCFAnagraph(this.getProjectId(), fictivePartner.getId());
			GeneralBudgets cloneFictivePartnerGB = null;
			if (fictivePartnerGB != null) {
				cloneFictivePartnerGB = fictivePartnerGB.clone();
				fictivePartnerGB.setIsOld(true);
				BeansFactory.GeneralBudgets().SaveInTransaction(fictivePartnerGB);
			}
			if (cloneFictivePartnerGB == null) {
				cloneFictivePartnerGB = GeneralBudgets.create(this.getProject(), fictivePartner);
			}
			sumTotal = 0d;
			for (PartnersBudgets partnersBudgets : partnerFictiveBudgetsList) {
				sumTotal += partnersBudgets.getTotalAmount();
			}

			fesrAmount = NumberHelper.CurrencyFormat(sumTotal * 0.75);
			cloneFictivePartnerGB.setFesr(fesrAmount);
			cloneFictivePartnerGB.setCnPublic(NumberHelper.CurrencyFormat(sumTotal - fesrAmount));
			cloneFictivePartnerGB.setBudgetTotal(NumberHelper.CurrencyFormat(sumTotal));
			BeansFactory.GeneralBudgets().SaveInTransaction(cloneFictivePartnerGB);
		}
	}

	private Double getAmountByCostItem(PartnersBudgets pb, int costItem, int subProject) {
		switch (costItem) {
		case 1:
			return pb.getPhaseBudgets().get(subProject - 1).getPartHumanRes();
		case 2:
			return pb.getPhaseBudgets().get(subProject - 1).getPartProvisionOfService();
		case 3:
			return pb.getPhaseBudgets().get(subProject - 1).getPartMissions();
		case 4:
			return pb.getPhaseBudgets().get(subProject - 1).getPartDurables();
		case 5:
			return pb.getPhaseBudgets().get(subProject - 1).getPartDurableGoods();
		case 6:
			return pb.getPhaseBudgets().get(subProject - 1).getPartAdvInfoPublicEvents();
		case 7:
			return pb.getPhaseBudgets().get(subProject - 1).getPartAdvInfoProducts();
		case 8:
			return pb.getPhaseBudgets().get(subProject - 1).getPartOverheads();
		case 9:
			return pb.getPhaseBudgets().get(subProject - 1).getPartGeneralCosts();
		case 10:
			return pb.getPhaseBudgets().get(subProject - 1).getPartOther();
		case 11:
			return pb.getPhaseBudgets().get(subProject - 1).getPartContainer();
		case 12:
			return pb.getPhaseBudgets().get(subProject - 1).getPartPersonalExpenses();
		case 13:
			return pb.getPhaseBudgets().get(subProject - 1).getPartCommunicationAndInformation();
		case 14:
			return pb.getPhaseBudgets().get(subProject - 1).getPartOrganizationOfCommitees();
		case 15:
			return pb.getPhaseBudgets().get(subProject - 1).getPartOtherFees();
		case 16:
			return pb.getPhaseBudgets().get(subProject - 1).getPartAutoCoordsTunis();
		case 17:
			return pb.getPhaseBudgets().get(subProject - 1).getPartContactPoint();
		case 18:
			return pb.getPhaseBudgets().get(subProject - 1).getPartSystemControlAndManagement();
		case 19:
			return pb.getPhaseBudgets().get(subProject - 1).getPartAuditOfOperations();
		case 20:
			return pb.getPhaseBudgets().get(subProject - 1).getPartAuthoritiesCertification();
		case 21:
			return pb.getPhaseBudgets().get(subProject - 1).getPartIntermEvaluation();
		}
		return null;
	}

	private PartnersBudgets setAmountByCostItem(PartnersBudgets pb, int costItem, int subProject, Double value) {
		switch (costItem) {
		case 1:
			pb.getPhaseBudgets().get(subProject - 1).setPartHumanRes(value);
			break;
		case 2:
			pb.getPhaseBudgets().get(subProject - 1).setPartProvisionOfService(value);
			break;
		case 3:
			pb.getPhaseBudgets().get(subProject - 1).setPartMissions(value);
			break;
		case 4:
			pb.getPhaseBudgets().get(subProject - 1).setPartDurables(value);
			break;
		case 5:
			pb.getPhaseBudgets().get(subProject - 1).setPartDurableGoods(value);
			break;
		case 6:
			pb.getPhaseBudgets().get(subProject - 1).getPartAdvInfoPublicEvents();
			break;
		case 7:
			pb.getPhaseBudgets().get(subProject - 1).setPartAdvInfoProducts(value);
			break;
		case 8:
			pb.getPhaseBudgets().get(subProject - 1).setPartOverheads(value);
			break;
		case 9:
			pb.getPhaseBudgets().get(subProject - 1).setPartGeneralCosts(value);
			break;
		case 10:
			pb.getPhaseBudgets().get(subProject - 1).setPartOther(value);
			break;
		case 11:
			pb.getPhaseBudgets().get(subProject - 1).setPartContainer(value);
			break;
		case 12:
			pb.getPhaseBudgets().get(subProject - 1).setPartPersonalExpenses(value);
			break;
		case 13:
			pb.getPhaseBudgets().get(subProject - 1).setPartCommunicationAndInformation(value);
			break;
		case 14:
			pb.getPhaseBudgets().get(subProject - 1).setPartOrganizationOfCommitees(value);
			break;
		case 15:
			pb.getPhaseBudgets().get(subProject - 1).setPartOtherFees(value);
			break;
		case 16:
			pb.getPhaseBudgets().get(subProject - 1).setPartAutoCoordsTunis(value);
			break;
		case 17:
			pb.getPhaseBudgets().get(subProject - 1).setPartContactPoint(value);
			break;
		case 18:
			pb.getPhaseBudgets().get(subProject - 1).setPartSystemControlAndManagement(value);
			break;
		case 19:
			pb.getPhaseBudgets().get(subProject - 1).setPartAuditOfOperations(value);
			break;
		case 20:
			pb.getPhaseBudgets().get(subProject - 1).setPartAuthoritiesCertification(value);
			break;
		case 21:
			pb.getPhaseBudgets().get(subProject - 1).setPartIntermEvaluation(value);
			break;
		}
		return pb;
	}

	private String BuildLogin(String name, String surname) {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(".");
		sb.append(surname);

		sb.toString().replace(' ', '_');

		sb = new StringBuilder(sb.toString().length() > 32 ? sb.toString().substring(0, 31) : sb.toString());

		if (UsersBean.IsLoginExists(sb.toString(), null)) {
			sb.append(UUID.randomUUID().toString().substring(0, 4));
			return sb.toString();
		} else {
			return sb.toString();
		}
	}

	/**
	 * Gets listCFPartnerAnagraphs
	 * 
	 * @return listCFPartnerAnagraphs the listCFPartnerAnagraphs
	 */
	public List<CFAnagraphWrapper> getListCFPartnerAnagraphs() {
		return listCFPartnerAnagraphs;
	}

	/**
	 * Sets listCFPartnerAnagraphs
	 * 
	 * @param listCFPartnerAnagraphs
	 *            the listCFPartnerAnagraphs to set
	 */
	public void setListCFPartnerAnagraphs(List<CFAnagraphWrapper> listCFPartnerAnagraphs) {
		this.listCFPartnerAnagraphs = listCFPartnerAnagraphs;
	}

	/**
	 * Sets newCFAnagraphAvailable
	 * 
	 * @param newCFAnagraphAvailable
	 *            the newCFAnagraphAvailable to set
	 */
	public void setNewCFAnagraphAvailable(boolean newCFAnagraphAvailable) {
		this.newCFAnagraphAvailable = newCFAnagraphAvailable;
	}

	/**
	 * Gets newCFAnagraphAvailable
	 * 
	 * @return newCFAnagraphAvailable the newCFAnagraphAvailable
	 */
	public boolean isNewCFAnagraphAvailable() {
		return newCFAnagraphAvailable;
	}

	/**
	 * Gets listCFAnagraph
	 * 
	 * @return listCFAnagraph the listCFAnagraph
	 */
	public List<CFAnagraphWrapper> getListCFAnagraph() {
		return listCFAnagraph;
	}

	/**
	 * Sets listCFAnagraph
	 * 
	 * @param listCFAnagraph
	 *            the listCFAnagraph to set
	 */
	public void setListCFAnagraph(List<CFAnagraphWrapper> listCFAnagraph) {
		this.listCFAnagraph = listCFAnagraph;
	}

	/**
	 * Sets entityPartnerEditId
	 * 
	 * @param entityPartnerEditId
	 *            the entityPartnerEditId to set
	 */
	public void setEntityPartnerEditId(Long entityPartnerEditId) {
		this.entityPartnerEditId = entityPartnerEditId;
	}

	/**
	 * Gets entityPartnerEditId
	 * 
	 * @return entityPartnerEditId the entityPartnerEditId
	 */
	public Long getEntityPartnerEditId() {
		return entityPartnerEditId;
	}

	/**
	 * Sets entityPartnerDeleteId
	 * 
	 * @param entityPartnerDeleteId
	 *            the entityPartnerDeleteId to set
	 */
	public void setEntityPartnerDeleteId(Long value) {
		this.getViewState().put("entityPartnerDeleteId", value);
		entityPartnerDeleteId = value;
	}

	/**
	 * Gets entityPartnerDeleteId
	 * 
	 * @return entityPartnerDeleteId the entityPartnerDeleteId
	 */
	public Long getEntityPartnerDeleteId() {
		entityPartnerDeleteId = (Long) this.getViewState().get("entityPartnerDeleteId");
		return entityPartnerDeleteId;
	}

	/**
	 * Sets currentUserIsPb
	 * 
	 * @param currentUserIsPb
	 *            the currentUserIsPb to set
	 */
	public void setCurrentUserIsPb(boolean currentUserIsPb) {
		this.currentUserIsPb = currentUserIsPb;
	}

	/**
	 * Gets currentUserIsPb
	 * 
	 * @return currentUserIsPb the currentUserIsPb
	 */
	public boolean isCurrentUserIsPb() {
		return currentUserIsPb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity() {
		// TODO Auto-generated method stub

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

	/**
	 * Sets asse5Project
	 * 
	 * @param asse5Project
	 *            the asse5Project to set
	 */
	public void setAsse5Project(boolean asse5Project) {
		this.asse5Project = asse5Project;
	}

	/**
	 * Gets asse5Project
	 * 
	 * @return asse5Project the asse5Project
	 */
	public boolean isAsse5Project() {
		return asse5Project;
	}

	public int getAsse() {
		return asse;
	}

	public void setAsse(int asse) {
		this.asse = asse;
	}

}
