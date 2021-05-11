/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostStates;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class AccessGrantHelper {
	/**
	 * Determines whether site is in actual state
	 * 
	 * @return
	 */
	public static boolean getInActualState() {
		if (SessionManager.getInstance().getSessionBean().Session.get("InActualState") != null) {
			return true;
		}

		try {
			if (BeansFactory.PhisicalClassificationPriorityThema().Load().size() != 0) {
				SessionManager.getInstance().getSessionBean().Session.put("InActualState", true);
				return true;

			}
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

		return false;
	}

	protected transient final static Log log = LogFactory.getLog(AccessGrantHelper.class);

	public static boolean IsInActualState() {
		if (SessionManager.getInstance().getSessionBean().Session.get("project") != null) {
			try {
				Projects project = BeansFactory.Projects()
						.Load(SessionManager.getInstance().getSessionBean().Session.get("project").toString());
				if (project != null && project.getState() != null) {
					if (project.getState().getCode() == ProjectState.Actual.getCode()) {
						return true;
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}

		}

		return false;
	}

	/**
	 * @return
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 */
	public static boolean IsReadyDurCompilation() throws HibernateException, PersistenceBeanException {
		if (IsInActualState()) {
			CostStates state = BeansFactory.CostStates().Load(CostStateTypes.CFValidate.getState());

			CostStates state2 = BeansFactory.CostStates().Load(CostStateTypes.FullValidate.getState());

			List<CostDefinitions> listCdi = BeansFactory.CostDefinitions().GetByProjectAndState(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")), state);

			List<CostDefinitions> listCdi2 = BeansFactory.CostDefinitions().GetByProjectAndState(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")), state2);

			if (listCdi == null || listCdi.isEmpty()) {
				listCdi = new ArrayList<CostDefinitions>();
			}

			listCdi.addAll(listCdi2);

			if (listCdi == null || listCdi.isEmpty()) {
				return false;
			} else {
				return listCdi.size() > 0;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * @return
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 */
	public static boolean DurExists() throws HibernateException, PersistenceBeanException {
		if (IsInActualState()) {
			List<DurCompilations> listDurs = BeansFactory.DurCompilations().LoadByProject(Long.valueOf(SessionManager.getInstance().getSessionBean().getProjectId()), null);
			
			if (listDurs != null) {
				return !listDurs.isEmpty();
			} else {
				return false;
			}
		}else {
			return false;
		}
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean CheckRolesDURCompilation() throws PersistenceBeanException {
		if (!(SessionManager.getInstance().getSessionBean().isSTC())) {
			if (!(SessionManager.getInstance().getSessionBean().isAGU())) {
				if (!(SessionManager.getInstance().getSessionBean().isACU())) {
					if (!(SessionManager.getInstance().getSessionBean().isCF())) {
						if (!(SessionManager.getInstance().getSessionBean().isACUM()))
							if (!(SessionManager.getInstance().getSessionBean().isAAU())) {
								if (!(SessionManager.getInstance().getSessionBean().isDAEC())) {
									if (!(SessionManager.getInstance().getSessionBean().isSuperAdmin())) {
										if (!(SessionManager.getInstance().getSessionBean().isUC())) {
											if (!(SessionManager.getInstance().getSessionBean().isANCM())) {
												if (!(SessionManager.getInstance().getSessionBean().isCIL())) {
											return false;
												}
											}
										}
									}
								}
							}
					}
				}
			}
		}

		return true;
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean CheckIsCFOrPBProject() throws PersistenceBeanException {
		List<CFPartnerAnagraphs> listAnagraphs = new ArrayList<CFPartnerAnagraphs>();

		if (SessionManager.getInstance().getSessionBean().Session.get("project") != null) {
			listAnagraphs.addAll(BeansFactory.CFPartnerAnagraphs().GetCFAnagraphForProject(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")),
					CFAnagraphTypes.CFAnagraph));

			listAnagraphs.addAll(BeansFactory.CFPartnerAnagraphs().GetCFAnagraphForProject(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")),
					CFAnagraphTypes.PartnerAnagraph));
		} else {
			listAnagraphs = BeansFactory.CFPartnerAnagraphs().Load();
		}

		List<Long> userIndices = new ArrayList<Long>();

		for (CFPartnerAnagraphs partnerAnagraph : listAnagraphs) {
			userIndices.add(partnerAnagraph.getId());
		}

		if (userIndices.size() > 0) {
			if (userIndices.contains(SessionManager.getInstance().getSessionBean().getCurrentUser().getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean getIsAGUAsse5() throws PersistenceBeanException {
		if (SessionManager.getInstance().getSessionBean().getListCurrentUserRolesSize() == 1
				&& (SessionManager.getInstance().getSessionBean().isAGU())) {
			if (BeansFactory.Projects().GetByAsse(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")), 5) != null) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean getIsAGUAsse1_4() throws PersistenceBeanException {
		if (SessionManager.getInstance().getSessionBean().getListCurrentUserRolesSize() == 1
				&& (SessionManager.getInstance().getSessionBean().isAGU())) {
			if (BeansFactory.Projects().GetByAsse(
					String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")), 5) == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws PersistenceBeanException
	 */
	public static boolean IsNotRegularSituationAvailable() throws NumberFormatException, PersistenceBeanException {

		List<CostDefinitions> listCostDefinitions = BeansFactory.CostDefinitions().getAllReimbursedCostDefinitions(
				String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")));
		if (listCostDefinitions == null || listCostDefinitions.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws PersistenceBeanException
	 * @throws ParseException
	 */
	public static boolean IsCostCertificationAvailable() throws NumberFormatException, PersistenceBeanException {
		List<DurCompilations> listDurs = BeansFactory.DurCompilations().LoadForCostCertification(null, null, null);

		if (listDurs != null) {
			return !listDurs.isEmpty();
		} else {
			return false;
		}
	}

	/**
	 * @return
	 */
	public static ProjectState getProjectState() {
		try {
			return ProjectState
					.getByState(
							BeansFactory.Projects()
									.Load(String.valueOf(
											SessionManager.getInstance().getSessionBean().Session.get("project")))
									.getState().getCode());
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean IsActualAndAguAccess() throws PersistenceBeanException {
		if (IsActual() || IsAguReadAccess()) {
			return true;
		}

		return false;
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 */
	public static boolean IsAguReadAccess() throws PersistenceBeanException {
		return getIsAGUAsse1_4();
	}

	public static boolean IsActual() throws PersistenceBeanException {
		return getProjectState().equals(ProjectState.Actual);
	}

	public static boolean IsProjectClosed() throws PersistenceBeanException {
		return getProjectState() == null || getProjectState().equals(ProjectState.Suspended)
				|| getProjectState().equals(ProjectState.Revoked);
	}

	public static boolean CheckBudgetsExists()
			throws NumberFormatException, HibernateException, PersistenceBeanException {
		Projects project = BeansFactory.Projects()
				.Load(String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")));

		List<BudgetInputSourceDivided> listbudgets = BudgetInputSourceDividedBean.GetByProject(project.getId());
		if (listbudgets == null || listbudgets.isEmpty()) {
			return false;
		}

		List<CFPartnerAnagraphs> listCFAnagraphs = null;
		listCFAnagraphs = BeansFactory.CFPartnerAnagraphs().LoadByProject(String.valueOf(project.getId()));
		if (listCFAnagraphs == null || listCFAnagraphs.isEmpty()) {
			return false;
		}

		List<GeneralBudgets> listGb = null;
		listGb = BeansFactory.GeneralBudgets().GetItemsForProject(project.getId());
		if (listGb == null || listGb.isEmpty()) {
			return false;
		}
		if (listGb.size() != listCFAnagraphs.size()) {
			return false;
		}

		ProjectIformationCompletations pic = null;

		pic = BeansFactory.ProjectIformationCompletations().LoadByProject(String.valueOf(project.getId()));

		if (pic == null) {
			return false;
		}

		for (CFPartnerAnagraphs cfpa : listCFAnagraphs) {

			List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
					.GetByProjectAndPartner(String.valueOf(project.getId()), String.valueOf(cfpa.getId()), true);

			if (listPB == null || listPB.isEmpty()) {
				return false;
			}

		}

		return true;
	}

	public static boolean IsProgressValidationAvailable() {
		if ((SessionManager.getInstance().getSessionBean().isCF()
				|| SessionManager.getInstance().getSessionBean().isSTC()
				|| SessionManager.getInstance().getSessionBean().isAGU()) && CheckProgressValidationAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean CheckProgressValidationAvailable() {
		if (SessionManager.getInstance().getSessionBean().Session.get("project") != null) {
			Long projectId = Long
					.valueOf(String.valueOf(SessionManager.getInstance().getSessionBean().Session.get("project")));

			List<AnnualProfiles> listAnnualProfiles = null;

			try {
				listAnnualProfiles = BeansFactory.AnnualProfiles().LoadByProject(String.valueOf(projectId));
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}

			List<Procedures> listProcs = null;

			try {
				listProcs = BeansFactory.Procedures().LoadByProject(String.valueOf(projectId));
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}

			List<PhisicalInitializationToCoreIndicators> coreList = null;

			List<PhisicalInitializationToProgramIndicators> realizList = null;

			List<PhisicalInitializationToEmploymentIndicators> employmentList = null;

			try {
				coreList = BeansFactory.PhisicalInizializationToCoreIndicators()
						.getByProject(String.valueOf(projectId));
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}

			try {
				realizList = BeansFactory.PhisicalInizializationToProgramRealizationIndicators()
						.getByProject(String.valueOf(projectId));
			} catch (PersistenceBeanException e) {
				e.printStackTrace();
			}

			if (listAnnualProfiles == null || listAnnualProfiles.isEmpty() || listProcs == null || listProcs.isEmpty()
					|| realizList == null || realizList.isEmpty() || coreList == null || coreList.isEmpty()) {
				return false;
			}

			// Annual profiles check
			boolean aFlag = false;
			for (AnnualProfiles ap : listAnnualProfiles) {
				if (ap.getAmountRealized() != null && ap.getVersion() != null && ap.getVersion() > 1) {
					aFlag = true;
					break;
				}
			}

			if (!aFlag) {
				return false;
			}

			// Procedures check
			boolean pFlag = false;

			for (Procedures p : listProcs) {
				if (p.getVersion() != null && p.getVersion() != null && p.getVersion() > 1) {
					pFlag = true;
					break;
				}
			}

			if (!pFlag) {
				return false;
			}

			boolean cFlag = false;
			for (PhisicalInitializationToCoreIndicators cores : coreList) {
				if (cores.getClosureValue() != null && cores.getVersion() != null && cores.getVersion() > 1) {
					cFlag = true;
					break;
				}
			}

			if (!cFlag) {
				return false;
			}

			boolean eFlag = false;
			for (PhisicalInitializationToEmploymentIndicators employment : employmentList) {
				if (employment.getClosureValue() != null && employment.getVersion() != null
						&& employment.getVersion() > 1) {
					eFlag = true;
					break;
				}
			}

			if (!eFlag) {
				return false;
			}

			boolean rFlag = false;
			for (PhisicalInitializationToProgramIndicators realizm : realizList) {
				if (realizm.getClosureValue() != null && realizm.getVersion() != null && realizm.getVersion() > 1) {
					rFlag = true;
					break;
				}
			}

			if (!rFlag) {
				return false;
			}

			return true;
		} else {
			return false;
		}
	}
}
