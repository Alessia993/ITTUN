/**
 * 
 */
package com.infostroy.paamns.web.beans.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.ProjectsWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class SearchBean extends EntityListBean<ProjectsWrapper>
{
	private String				code;

	private Date				createDateS;

	private Date				createDateE;

	private String				asse;

	private List<SelectItem>	asses;

	private String				state;

	private List<SelectItem>	states;

	private String				title;

	private String				cupCode;

	private String				cfName;

	private boolean				withCertifiableDur;

	private Boolean				withDaeiToCertCosts;

	private List<SelectItem>	filterDURValues;

	private HtmlDataScroller	scroller;

	private Boolean				canSearchACU;

	private Boolean				canSearchDaei;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		checkUserRoles();
		fillFilters();
		this.FillDropDowns();
		this.LoadEntities();
	}

	/**
	 * 
	 */
	private void checkUserRoles()
	{
		if (this.getSessionBean().isACU())
		{
			this.setCanSearchACU(Boolean.TRUE);
		}
		else
		{
			this.setCanSearchACU(Boolean.FALSE);
		}
		if (this.getSessionBean().isDAEC())
		{
			this.setCanSearchDaei(Boolean.TRUE);
		}
		else
		{
			this.setCanSearchDaei(Boolean.FALSE);
		}
	}

	private void fillFilters()
	{
		this.filterDURValues = new ArrayList<SelectItem>();
		this.filterDURValues.add(SelectItemHelper.getAllElement());
		this.filterDURValues.add(new SelectItem("stc", Utils
				.getString("durValidateSTC")));
		this.filterDURValues.add(new SelectItem("agu", Utils
				.getString("durValidateAGU")));
		this.filterDURValues.add(new SelectItem("acu", Utils
				.getString("durValidateACU")));
	}

	public void LoadEntities() throws HibernateException,
			PersistenceBeanException
	{
		List<Projects> listPro = new ArrayList<Projects>();

		if (this.getCurrentUser().getAdmin() || this.getSessionBean().isAAU())
		{
			listPro = BeansFactory.Projects().SearchProjects(this.getCode(),
					this.getTitle(), this.getAsse(), this.getCreateDateS(),
					this.getCreateDateE(), this.getState(), this.getCupCode(),
					this.getCfName(), this.getWithCertifiableDur(),
					withDaeiToCertCosts);
		}
		else if ((this.getSessionBean().isPartner() || this.getSessionBean()
				.isCF())
				&& !(this.getSessionBean().isAGU() || this.getSessionBean()
						.isSTC()))
		{
			listPro = BeansFactory.Projects().SearchProjectsPartner(
					this.getCode(), this.getTitle(), this.getAsse(),
					this.getCreateDateS(), this.getCreateDateE(),
					this.getState(), this.getCupCode(), this.getCfName(),
					this.getSessionBean().getCurrentUser().getId(), false);
		}
		else if (this.getSessionBean().isCIL()
				&& !(this.getSessionBean().isAGU() || this.getSessionBean()
						.isSTC()))
		{
			listPro = BeansFactory.Projects().SearchProjectsByCIL(
					this.getCode(), this.getTitle(), this.getAsse(),
					this.getCreateDateS(), this.getCreateDateE(),
					this.getState(), this.getCupCode(), this.getCfName(),
					this.getSessionBean().getCurrentUser().getId(),
					Boolean.FALSE);
		}
		else if ((this.getSessionBean().isDAEC())
				&& !(this.getSessionBean().isAGU()
						|| this.getSessionBean().isSTC()
						|| this.getSessionBean().isAAU() || this
						.getSessionBean().isACU()))
		{
			listPro = BeansFactory.Projects().SearchProjectsByCIL(
					this.getCode(), this.getTitle(), this.getAsse(),
					this.getCreateDateS(), this.getCreateDateE(),
					this.getState(), this.getCupCode(), this.getCfName(),
					this.getSessionBean().getCurrentUser().getId(),
					this.getWithDaeiToCertCosts());
		}
		else
		{
			listPro = BeansFactory.Projects().SearchProjects(this.getCode(),
					this.getTitle(), this.getAsse(), this.getCreateDateS(),
					this.getCreateDateE(), this.getState(), this.getCupCode(),
					this.getCfName(), this.getWithCertifiableDur(),
					this.getWithDaeiToCertCosts());
		}

		if (this.getList() == null)
		{
			this.setList(new ArrayList<ProjectsWrapper>());
		}
		else
		{
			this.getList().clear();
		}

		for (Projects item : listPro)
		{
			if (!checkDurState(item))
			{
				continue;
			}

			if (getFilterBudgetValidation() && !checkBudgets(item))
			{
				continue;
			}
			if (getFilterACUCertifiable() && !checkAcuCertifiable(item))
			{
				continue;
			}

			this.getList()
					.add(new com.infostroy.paamns.web.beans.wrappers.ProjectsWrapper(
							item, this.isEditable(item)));
		}
	}

	private boolean checkDurState(Projects project)
			throws PersistenceBeanException
	{
		DurStateTypes type = DurStateTypes.STCEvaluation;
		if (getFilterDURValue() == null || getFilterDURValue().isEmpty())
		{
			return true;
		}
		else if (getFilterDURValue().equals("stc"))
		{
			type = DurStateTypes.STCEvaluation;
		}
		else if (getFilterDURValue().equals("agu"))
		{
			type = DurStateTypes.AGUEvaluation;
		}
		else if (getFilterDURValue().equals("acu"))
		{
			type = DurStateTypes.ACUEvaluation;
		}

		List<DurCompilations> listDurs = BeansFactory.DurCompilations()
				.LoadByState(project.getId(), type);
		return !listDurs.isEmpty();
	}

	private boolean checkAcuCertifiable(Projects project)
	{
		try
		{
			List<DurCompilations> durList = BeansFactory.DurCompilations()
					.LoadByProject(project.getId(), null);
			for (DurCompilations durComp : durList)
			{
				if (durComp.getCertifiedDate() != null)
				{
					return true;
				}
			}
			return false;
		}
		catch (HibernateException e)
		{
		}
		catch (PersistenceBeanException e)
		{
		}
		return false;
	}

	private boolean checkBudgets(Projects project)
			throws PersistenceBeanException
	{
		if (project.getState().getCode() != ProjectState.Actual.getCode())
		{
			return false;
		}

		List<PartnersBudgets> budgets = BeansFactory.PartnersBudgets()
				.LoadNotApprovedByProject(project.getId());

		return !budgets.isEmpty();
	}

	public boolean isEditable(Projects p)
	{
		if (this.getSessionBean().isAGU()
				&& this.getSessionBean().getListCurrentUserRolesSize() == 1
				&& p.getAsse() != 5)
		{
			return false;
		}
		else if (this.getSessionBean().isSTC()
				&& this.getSessionBean().getListCurrentUserRolesSize() == 1
				&& p.getAsse() == 5)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void FillDropDowns() throws PersistenceBeanException
	{
		if (this.getAsses() == null)
		{
			this.setAsses(new ArrayList<SelectItem>());
		}
		else
		{
			this.getAsses().clear();
		}
		this.getAsses().add(SelectItemHelper.getFirst());
		if (this.getSessionBean().isCF()
				&& !(this.getSessionBean().isAGU() || this.getSessionBean()
						.isSTC()))
		{
			for (int i = 1; i < 5; i++)
			{
				this.getAsses().add(
						new SelectItem(String.valueOf(i), String.valueOf(i)));
			}
		}
		else
		{
			for (int i = 1; i < 6; i++)
			{
				this.getAsses().add(
						new SelectItem(String.valueOf(i), String.valueOf(i)));
			}
		}

		if (this.getStates() == null)
		{
			this.setStates(new ArrayList<SelectItem>());
		}
		else
		{
			this.getStates().clear();
		}

		this.getStates().add(SelectItemHelper.getFirst());
		for (ProjectStates item : BeansFactory.ProjectStates().Load())
		{
			this.getStates().add(
					new SelectItem(String.valueOf(item.getId()), item
							.getValue(), item.getValue()));
		}
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		try
		{
			Projects project = BeansFactory.Projects().Load(
					String.valueOf(getEntityDeleteId()));
			if (project != null && !project.getDeleted())
			{
				List<CFPartnerAnagraphProject> listPartnersToProject = BeansFactory
						.CFPartnerAnagraphProject().GetCFAnagraphForProject(
								String.valueOf(this.getEntityDeleteId()), null);

				for (CFPartnerAnagraphProject cfpap : listPartnersToProject)
				{
					List<CFPartnerAnagraphProject> listPartnerProjects = BeansFactory
							.CFPartnerAnagraphProject()
							.LoadByPartnerWithoutProject(
									cfpap.getCfPartnerAnagraphs().getId());

					if (listPartnerProjects != null
							&& listPartnerProjects.size() == 1)
					{
						CFPartnerAnagraphs.DeleteCFAnagraph(cfpap
								.getCfPartnerAnagraphs().getId(), this
								.getEntityDeleteId());
					}
				}

				BeansFactory.Projects().Delete(
						String.valueOf(getEntityDeleteId()));
			}
			else
			{
				this.goTo(PagesTypes.PROJECTLIST);
			}

			if (this.getSession().get("project") != null)
			{
				if (this.getEntityDeleteId().equals(
						Long.valueOf(String.valueOf(this.getSession().get(
								"project")))))
				{
					this.getSession().remove("project");
				}
			}

			this.Page_Load();
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
		catch (NumberFormatException e)
		{
			log.error(e);
		}
	}

	public void search()
	{
		try
		{
			this.LoadEntities();
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	/**
     * 
     */
	public void clear()
	{
		this.ClearValues();
		try
		{
			this.LoadEntities();
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
		this.ReRenderScroll();
	}

	/**
     * 
     */
	private void ClearValues()
	{
		this.setAsse(null);
		this.setCfName(null);
		this.setCode(null);
		this.setCreateDateE(null);
		this.setCreateDateS(null);
		this.setCupCode(null);
		this.setState(null);
		this.setTitle(null);
		this.setFilterBudgetValidation(false);
		this.setFilterDURValue(null);
	}

	public void selectProject() throws PersistenceBeanException
	{
		this.getSession().put("project", this.getEntityEditId());
		this.getSession().put("projectEdit", this.getEntityEditId());
		this.getSession().put("projectEditShowOnly", true);
		this.getSession().put("projectEditBackToSearch", true);

		this.getSessionBean().setIsActualSate(AccessGrantHelper.IsActual());
		this.getSessionBean().setIsActualSateAndAguRead(
				AccessGrantHelper.IsActualAndAguAccess());
		this.getSessionBean().setIsAguRead(AccessGrantHelper.IsAguReadAccess());
		this.getSessionBean().setIsProjectClosed(
				AccessGrantHelper.IsProjectClosed());

		if (this.getSessionBean().isSTC() || this.getSessionBean().isAGU())
		{
			Projects project = BeansFactory.Projects().Load(
					String.valueOf(getEntityEditId()));
			if (project != null && !project.getDeleted())
			{
				this.goTo(PagesTypes.PROJECTSTART);
			}
			else
			{
				this.goTo(PagesTypes.PROJECTLIST);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		this.getSession().put("projectEdit", this.getEntityEditId());
		this.getSession().put("projectEditShowOnly", false);
		this.getSession().put("projectEditBackToSearch", true);

		Projects project;
		try
		{
			project = BeansFactory.Projects().Load(
					String.valueOf(getEntityEditId()));
			if (project != null && !project.getDeleted())
			{
				this.goTo(PagesTypes.PROJECTSTART);
			}
			else
			{
				this.goTo(PagesTypes.PROJECTLIST);
			}
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
	 * Sets code
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * Gets code
	 * 
	 * @return code the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets createDateS
	 * 
	 * @param createDateS
	 *            the createDateS to set
	 */
	public void setCreateDateS(Date createDateS)
	{
		this.createDateS = createDateS;
	}

	/**
	 * Gets createDateS
	 * 
	 * @return createDateS the createDateS
	 */
	public Date getCreateDateS()
	{
		return createDateS;
	}

	/**
	 * Sets createDateE
	 * 
	 * @param createDateE
	 *            the createDateE to set
	 */
	public void setCreateDateE(Date createDateE)
	{
		this.createDateE = createDateE;
	}

	/**
	 * Gets createDateE
	 * 
	 * @return createDateE the createDateE
	 */
	public Date getCreateDateE()
	{
		return createDateE;
	}

	/**
	 * Sets asse
	 * 
	 * @param asse
	 *            the asse to set
	 */
	public void setAsse(String asse)
	{
		this.asse = asse;
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public String getAsse()
	{
		return asse;
	}

	/**
	 * Sets asses
	 * 
	 * @param asses
	 *            the asses to set
	 */
	public void setAsses(List<SelectItem> asses)
	{
		this.getSession().put("asses", asses);
		this.asses = asses;
	}

	/**
	 * Gets asses
	 * 
	 * @return asses the asses
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getAsses()
	{
		asses = (List<SelectItem>) this.getSession().get("asses");
		return asses;
	}

	/**
	 * Sets cfName
	 * 
	 * @param cfName
	 *            the cfName to set
	 */
	public void setCfName(String cfName)
	{
		this.cfName = cfName;
	}

	/**
	 * Gets cfName
	 * 
	 * @return cfName the cfName
	 */
	public String getCfName()
	{
		return cfName;
	}

	/**
	 * Sets cupCode
	 * 
	 * @param cupCode
	 *            the cupCode to set
	 */
	public void setCupCode(String cupCode)
	{
		this.cupCode = cupCode;
	}

	/**
	 * Gets cupCode
	 * 
	 * @return cupCode the cupCode
	 */
	public String getCupCode()
	{
		return cupCode;
	}

	/**
	 * Sets title
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Gets title
	 * 
	 * @return title the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets states
	 * 
	 * @param states
	 *            the states to set
	 */
	public void setStates(List<SelectItem> states)
	{
		this.getSession().put("states", states);
		this.states = states;
	}

	/**
	 * Gets states
	 * 
	 * @return states the states
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getStates()
	{
		states = (List<SelectItem>) this.getSession().get("states");
		return states;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Gets state
	 * 
	 * @return state the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * Sets withCertifiableDur
	 * 
	 * @param withCertifiableDur
	 *            the withCertifiableDur to set
	 */
	public void setWithCertifiableDur(boolean withCertifiableDur)
	{
		this.withCertifiableDur = withCertifiableDur;
	}

	/**
	 * Gets withCertifiableDur
	 * 
	 * @return withCertifiableDur the withCertifiableDur
	 */
	public boolean getWithCertifiableDur()
	{
		return withCertifiableDur;
	}

	/**
	 * Sets filterDURValues
	 * 
	 * @param filterDURValues
	 *            the filterDURValues to set
	 */
	public void setFilterDURValues(List<SelectItem> filterDURValues)
	{
		this.filterDURValues = filterDURValues;
	}

	/**
	 * Gets filterDURValues
	 * 
	 * @return filterDURValues the filterDURValues
	 */
	public List<SelectItem> getFilterDURValues()
	{
		return filterDURValues;
	}

	/**
	 * Sets filterDURValue
	 * 
	 * @param filterDURValue
	 *            the filterDURValue to set
	 */
	public void setFilterDURValue(String filterDURValue)
	{
		this.getSession().put("filterDURValue", filterDURValue);
	}

	/**
	 * Gets filterDURValue
	 * 
	 * @return filterDURValue the filterDURValue
	 */
	public String getFilterDURValue()
	{
		return (String) this.getSession().get("filterDURValue");
	}

	/**
	 * Sets filterBudgetValidation
	 * 
	 * @param filterBudgetValidation
	 *            the filterBudgetValidation to set
	 */
	public void setFilterBudgetValidation(boolean filterBudgetValidation)
	{
		this.getSession().put("filterBudgetValidation", filterBudgetValidation);
	}

	/**
	 * Gets filterBudgetValidation
	 * 
	 * @return filterBudgetValidation the filterBudgetValidation
	 */
	public boolean getFilterBudgetValidation()
	{
		return this.getSession().get("filterBudgetValidation") == null ? false
				: (Boolean) this.getSession().get("filterBudgetValidation");
	}

	public void setFilterACUCertifiable(boolean acuCertifiable)
	{
		this.getSession().put("acuCertifiable", acuCertifiable);
	}

	public boolean getFilterACUCertifiable()
	{
		return this.getSession().get("acuCertifiable") == null ? false
				: (Boolean) this.getSession().get("acuCertifiable");
	}

	/**
	 * Sets scroller
	 * 
	 * @param scroller
	 *            the scroller to set
	 */
	public void setScroller(HtmlDataScroller scroller)
	{
		this.scroller = scroller;
	}

	/**
	 * Gets scroller
	 * 
	 * @return scroller the scroller
	 */
	public HtmlDataScroller getScroller()
	{
		return scroller;
	}

	public Boolean getCanSearchACU()
	{
		return canSearchACU;
	}

	public void setCanSearchACU(Boolean canSearchACU)
	{
		this.canSearchACU = canSearchACU;
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
			for (ProjectsWrapper item : this.getList())
			{
				if (this.getEntityEditId().equals(item.getId()))
				{
					item.setLocked(lock);

					Projects p = BeansFactory.Projects().Load(item.getId());
					if (p != null)
					{
						p.setLocked(lock);
						BeansFactory.Projects().Save(p);
					}
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets withDaeiToCertCosts
	 * 
	 * @return withDaeiToCertCosts the withDaeiToCertCosts
	 */
	public Boolean getWithDaeiToCertCosts()
	{
		return withDaeiToCertCosts;
	}

	/**
	 * Sets withDaeiToCertCosts
	 * 
	 * @param withDaeiToCertCosts
	 *            the withDaeiToCertCosts to set
	 */
	public void setWithDaeiToCertCosts(Boolean withDaeiToCertCosts)
	{
		this.withDaeiToCertCosts = withDaeiToCertCosts;
	}

	/**
	 * Gets canSearchDaei
	 * 
	 * @return canSearchDaei the canSearchDaei
	 */
	public Boolean getCanSearchDaei()
	{
		return canSearchDaei;
	}

	/**
	 * Sets canSearchDaei
	 * 
	 * @param canSearchDaei
	 *            the canSearchDaei to set
	 */
	public void setCanSearchDaei(Boolean canSearchDaei)
	{
		this.canSearchDaei = canSearchDaei;
	}

}
