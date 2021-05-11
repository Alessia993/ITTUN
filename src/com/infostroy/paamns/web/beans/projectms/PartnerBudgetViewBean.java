package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

public class PartnerBudgetViewBean extends
		EntityProjectListBean<PartnersBudgets>
{
	private List<PartnersBudgets>		listPartnerBudgets;

	private List<PartnerBudgetWrapper>	listPBToShow;

	private boolean						currentUserIsPB;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		this.listPBToShow = new ArrayList<PartnerBudgetWrapper>();

		this.CheckCurrentUserIsPB();

		if (this.getProjectId() != null)
		{
			ProjectIformationCompletations pic = null;

			pic = BeansFactory.ProjectIformationCompletations().LoadByProject(
					this.getProjectId());

			if (pic == null)
			{
				this.goTo(PagesTypes.PROJECTINDEX);
				return;
			}

			this.listPartnerBudgets = BeansFactory.PartnersBudgets()
					.GetByProject(this.getProjectId());

			/*
			 * if (!this.isCurrentUserIsPB()) { this.listPartnerBudgets =
			 * BeansFactory.PartnersBudgets()
			 * .GetByProject(this.getProjectId()); } else {
			 * this.listPartnerBudgets = BeansFactory.PartnersBudgets()
			 * .GetByProjectAndUser( this.getProjectId(),
			 * this.getSessionBean().getCurrentUser().getId() .toString(),
			 * true); }
			 */

			if (this.listPartnerBudgets == null
					|| this.listPartnerBudgets.isEmpty())
			{
				this.listPartnerBudgets = new ArrayList<PartnersBudgets>();
			}

			List<CFPartnerAnagraphs> listPartners = BeansFactory
					.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
			/*
			 * if (!this.isCurrentUserIsPB()) { listPartners =
			 * BeansFactory.CFPartnerAnagraphs().LoadByProject(
			 * this.getProjectId()); } else { listPartners =
			 * BeansFactory.CFPartnerAnagraphs()
			 * .GetCFAnagraphForProjectAndUser(this.getProjectId(),
			 * this.getSessionBean().getCurrentUser().getId()); }
			 */

			if (listPartners == null)
			{
				listPartners = new ArrayList<CFPartnerAnagraphs>();
			}

			List<Long> partnersIndices = new ArrayList<Long>();

			for (PartnersBudgets pb : listPartnerBudgets)
			{
				if (pb.getCfPartnerAnagraph() != null
						&& !partnersIndices.contains(pb.getCfPartnerAnagraph()
								.getId()))
				{
					partnersIndices.add(pb.getCfPartnerAnagraph().getId());
				}
			}

			for (CFPartnerAnagraphs partner : listPartners)
			{
				if (!partnersIndices.contains(partner.getId()))
				{
					for (int i = 0; i < pic.getYearDuration(); i++)
					{
						PartnersBudgets newEntity = new PartnersBudgets();
						newEntity.setPhaseBudgets(new LinkedList<Phase>());

						newEntity.setCreateDate(DateTime.getNow());
						newEntity.setDeleted(false);
						newEntity.setIsOld(false);
						newEntity.setProject(this.getProject());
						newEntity.setYear(i + 1);
						newEntity.setCfPartnerAnagraph(partner);

						for (CostDefinitionPhases cdPhase : BeansFactory
								.CostDefinitionPhase().Load())
						{
							Phase phase = new Phase();
							phase.setPhase(cdPhase);
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
							
							newEntity.getPhaseBudgets().add(phase);
						}
						newEntity.setTotalAmount(0d);
						newEntity.setQuotaPrivate(0d);
						this.listPartnerBudgets.add(newEntity);
					}
				}
			}

			Collections.sort(this.listPartnerBudgets,
					new PartnerBudgetComparer());

			for (CFPartnerAnagraphs partner : listPartners)
			{
				PartnerBudgetWrapper pbw = new PartnerBudgetWrapper();
				pbw.setId(partner.getId());
				pbw.setName(partner.getName());
				pbw.setTotalBudget(0d);

				for (PartnersBudgets pb : this.listPartnerBudgets)
				{
					if (pb.getCfPartnerAnagraph() != null)
					{
						if (pb.getCfPartnerAnagraph().getId() == partner
								.getId())
						{
							pbw.setTotalBudget((pbw.getTotalBudget() == null ? 0
									: pbw.getTotalBudget())
									+ (pb.getTotalAmount() == null ? 0 : pb
											.getTotalAmount()));
						}
					}
				}

				if (this.getSessionBean().isPartner()
						&& partner.getUser() != null
						&& !partner.getUser().getId()
								.equals(this.getCurrentUser().getId()))
				{
					pbw.setEditAvailable(false);
				}
				else
				{
					pbw.setEditAvailable(true);
				}

				this.listPBToShow.add(pbw);
			}
		}
	}

	public void Page_Load_Static() throws PersistenceBeanException
	{
		/*
		 * if (!AccessGrantHelper.getIsAGUAsse5()) { if
		 * (!AccessGrantHelper.CheckIsCFOrPBProject()) {
		 * this.goTo(PagesTypes.PROJECTINDEX); } }
		 */
	}

	private void CheckCurrentUserIsPB()
	{
		if (this.getSessionBean().isPartner())
		{
			if (!(this.getSessionBean().isSTC()
					|| this.getSessionBean().isAGU() || this.getSessionBean()
					.isCF()))
			{
				this.setCurrentUserIsPB(true);
			}
			else
			{
				this.setCurrentUserIsPB(false);
			}
		}
		else
		{
			this.setCurrentUserIsPB(false);
		}
	}

	public void editItem()
	{
		this.getSession().put("partnerbudgetedit", this.getEntityEditId());
		this.goTo(PagesTypes.PARTNERBUDGETEDIT);
	}

	/**
	 * Gets listPBToShow
	 * 
	 * @return listPBToShow the listPBToShow
	 */
	public List<PartnerBudgetWrapper> getListPBToShow()
	{
		return listPBToShow;
	}

	/**
	 * Sets listPBToShow
	 * 
	 * @param listPBToShow
	 *            the listPBToShow to set
	 */
	public void setListPBToShow(List<PartnerBudgetWrapper> listPBToShow)
	{
		this.listPBToShow = listPBToShow;
	}

	/**
	 * Class wrapper for partner budget
	 * 
	 * @author Sergey Zorin InfoStroy Co., 2010.
	 * 
	 */
	public class PartnerBudgetWrapper
	{
		private Long	id;

		private String	name;

		private Double	totalBudget;

		private boolean	editAvailable;

		/**
		 * Sets id
		 * 
		 * @param id
		 *            the id to set
		 */
		public void setId(Long id)
		{
			this.id = id;
		}

		/**
		 * Gets id
		 * 
		 * @return id the id
		 */
		public Long getId()
		{
			return id;
		}

		/**
		 * Sets name
		 * 
		 * @param name
		 *            the name to set
		 */
		public void setName(String name)
		{
			this.name = name;
		}

		/**
		 * Gets name
		 * 
		 * @return name the name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Sets totalBudget
		 * 
		 * @param totalBudget
		 *            the totalBudget to set
		 */
		public void setTotalBudget(Double totalBudget)
		{
			this.totalBudget = totalBudget;
		}

		/**
		 * Gets totalBudget
		 * 
		 * @return totalBudget the totalBudget
		 */
		public Double getTotalBudget()
		{
			return totalBudget;
		}

		/**
		 * Sets editAvailable
		 * 
		 * @param editAvailable
		 *            the editAvailable to set
		 */
		public void setEditAvailable(boolean editAvailable)
		{
			this.editAvailable = editAvailable;
		}

		/**
		 * Gets editAvailable
		 * 
		 * @return editAvailable the editAvailable
		 */
		public boolean getEditAvailable()
		{
			return editAvailable;
		}
	}

	/**
	 * Custom comparer for partner budgets. Sort by partner anagraph type
	 * 
	 * @author Sergey Zorin InfoStroy Co., 2010.
	 * 
	 */
	public class PartnerBudgetComparer implements Comparator<PartnersBudgets>
	{
		@Override
		public int compare(PartnersBudgets arg0, PartnersBudgets arg1)
		{
			if (arg0.getCfPartnerAnagraph() == null
					|| arg1.getCfPartnerAnagraph() == null)
			{
				return 0;
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
			if (p1.getType() == null || p2.getType() == null)
			{
				return 0;
			}
			return (p1.getType().getValue().compareTo(p2.getType().getValue()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets currentUserIsPB
	 * 
	 * @param currentUserIsPB
	 *            the currentUserIsPB to set
	 */
	public void setCurrentUserIsPB(boolean currentUserIsPB)
	{
		this.currentUserIsPB = currentUserIsPB;
	}

	/**
	 * Gets currentUserIsPB
	 * 
	 * @return currentUserIsPB the currentUserIsPB
	 */
	public boolean isCurrentUserIsPB()
	{
		return currentUserIsPB;
	}
}
