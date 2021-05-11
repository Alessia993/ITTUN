/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.ChangeType;
import com.infostroy.paamns.common.enums.CostStateTypes;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.SuperAdminChange;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;

/**
 * 
 * @author posuhov InfoStroy Co., 2013.
 * 
 */
public class SuperAdminHelper
{
	public static boolean moveCostDefinitionPrevStep(Long entityId,
			String motivation, Users currentUser, Projects project,
			String source) throws HibernateException, PersistenceBeanException
	{
		if (motivation == null || motivation.isEmpty() || entityId == null)
		{
			return false;
		}
		CostDefinitions item = BeansFactory.CostDefinitions().Load(entityId);
		if (item != null)
		{
			Transaction tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			SuperAdminChange changes = new SuperAdminChange(
					ChangeType.MOVING_TO_PREVIOUS_STEP, currentUser,
					motivation, project);
			changes.setChanges(Utils
					.getString("superAdminChangeLogMessageHeader")
					.replace("%1", Utils.getString("superAdminChangesCostDefinition"))
					.replace("%2", item.getProgressiveId().toString())
					.replace("%3", Utils.getString(source))
					+ "<br/>"
					+ Utils.getString("superAdminChangeLogMessage")
							.replace(
									"%1",
									Utils.getString("durCompilationEditCostTableHeaderAssign"))
							.replace("%2", item.getCostState().toString()));
			if (rollbackStatus(item, project))
			{
				BeansFactory.CostDefinitions().SaveInTransaction(item);
				changes.setChanges(changes.getChanges().replace("%3",
						item.getCostState().toString()));
				BeansFactory.SuperAdminChanges().SaveInTransaction(changes);
				tr.commit();
			}
		}
		return true;
	}

	/**
	 * @param item
	 * @param project
	 * @throws PersistenceBeanException
	 */
	private static boolean rollbackStatus(CostDefinitions item, Projects project)
			throws PersistenceBeanException
	{
		if (CostStateTypes.getByState(item.getCostState().getId()).equals(
				CostStateTypes.CILVerify))
		{
			item.setCostState(BeansFactory.CostStates().Load(
					CostStateTypes.NotYetValidate.getState()));
			return true;
		}
		else if (CostStateTypes.getByState(item.getCostState().getId()).equals(
				CostStateTypes.DAECApproval))
		{
			item.setCostState(BeansFactory.CostStates().Load(
					CostStateTypes.CILVerify.getState()));
			item.setCilDate(null);
			item.setCilCheck(null);
			return true;
		}
		else if (CostStateTypes.getByState(item.getCostState().getId()).equals(
				CostStateTypes.CILValidate))
		{
			CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
					.GetByUser(item.getUser().getId());
			if (partner != null)
			{
				CFPartnerAnagraphProject partnerToProject = BeansFactory
						.CFPartnerAnagraphProject().LoadByPartner(
								project.getId(), partner.getId());
				if (partnerToProject != null
						&& partnerToProject
								.getType()
								.getId()
								.equals(CFAnagraphTypes.PartnerAnagraph
										.getCfType()))
				{

					if (partner.getCountry() != null
							&& partner.getCountry().getCode()
									.equals(CountryTypes.Italy.getCountry()))
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.CILVerify.getState()));
						item.setCilDate(null);
						item.setCilCheck(null);
						return true;
					}
					else
					{
						item.setCostState(BeansFactory.CostStates().Load(
								CostStateTypes.DAECApproval.getState()));
						return true;
					}
				}
			}
		}
		else if (CostStateTypes.getByState(item.getCostState().getId()).equals(
				CostStateTypes.CFValidation))
		{
			item.setCostState(BeansFactory.CostStates().Load(
					CostStateTypes.CILValidate.getState()));
			item.setCfCheck(null);
			return true;
		}
		else if (CostStateTypes.getByState(item.getCostState().getId()).equals(
				CostStateTypes.CFValidate))
		{
			CFPartnerAnagraphs partner;
			if (project.getAsse() == 5
					&& hasUserRole(item.getUser(), UserRoleTypes.AGU))
			{
				partner = getAguPartnerForThisProject(String.valueOf(project.getId()));
			}
			else
			{
				partner = BeansFactory.CFPartnerAnagraphs().GetByUser(
						item.getUser().getId());
			}
			if (partner != null)
			{
				CFPartnerAnagraphProject partnerToProject = BeansFactory
						.CFPartnerAnagraphProject().LoadByPartner(
								project.getId(), partner.getId());
				if (partnerToProject != null
						&& partnerToProject.getType().getId()
								.equals(CFAnagraphTypes.CFAnagraph.getCfType()))
				{
					item.setCostState(BeansFactory.CostStates().Load(
							CostStateTypes.CILVerify.getState()));
					item.setCfCheck(null);
					return true;
				}
				else
				{
					item.setCostState(BeansFactory.CostStates().Load(
							CostStateTypes.CFValidation.getState()));
					item.setCfCheck(null);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param user
	 *            TODO
	 * @param userRoleType
	 *            TODO
	 * @return
	 * @throws PersistenceBeanException
	 */
	private static boolean hasUserRole(Users user, UserRoleTypes userRoleType)
			throws PersistenceBeanException
	{
		Roles roleByName = BeansFactory.Roles().GetRoleByName(userRoleType);
		for (UserRoles role : user.getRoles())
		{
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
	public static CFPartnerAnagraphs getAguPartnerForThisProject(
			String projectId) throws PersistenceBeanException
	{
		List<CFPartnerAnagraphs> listCFP = BeansFactory.CFPartnerAnagraphs()
				.GetCFAnagraphForProject(projectId, CFAnagraphTypes.CFAnagraph);
		if (listCFP != null && !listCFP.isEmpty())
		{
			return listCFP.get(0);
		}
		return null;
	}

}
