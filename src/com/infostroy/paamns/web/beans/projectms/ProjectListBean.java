package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DeletedSpecificationState;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.ProjectsWrapper;

/**
 * 
 * @author Sergey Zorin
 * @author Sergey Manoylo InfoStroy Co., 2010.
 * modified by Ingloba360
 * 
 */
public class ProjectListBean extends EntityListBean<Projects> {

	private boolean					newProjectAvailable;

	/**
	 * Search pattern for projects by code
	 */
	@SuppressWarnings("unused")
	private String					searchPattern;

	@SuppressWarnings("unused")
	private String					searchTitlePattern;
	
	@SuppressWarnings("unused")
	private String 					searchAssePattern;
	
	@SuppressWarnings("unused")
	private String					searchSpecificGoalPattern;

	private HtmlDataScroller		scroller;

	private List<ProjectsWrapper>	listProjects;

	private boolean					generalBudgetMinorValidatorMessage;

	@SuppressWarnings("unused")
	private Integer deletedSpecificationState;
	
	private List<SelectItem> listDeletedSpecificationState;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{

		try
		{
			if (this.CheckNewProjectButtonVisibility()
					&& this.IsCompiledToPointF())
			{
				this.setNewProjectAvailable(true);
			}
			else
			{
				this.setNewProjectAvailable(false);
			}

			boolean oneOfConditionsFired = false;
			List<Projects> tempProjectsList = new ArrayList<Projects>();
			if (this.getCurrentUser().getAdmin()
					|| this.getSessionBean().isAAU())
			{
				tempProjectsList.addAll(BeansFactory.Projects().LoadByPattern(
						this.getSearchPattern(), this.getSearchTitlePattern(), this.getSearchAssePattern()));
				oneOfConditionsFired = true;
			}
			if ((this.getSessionBean().isPartner() || this.getSessionBean()
					.isCF())
					&& !(this.getSessionBean().isAGU()
							|| this.getSessionBean().isSTC() || this
							.getSessionBean().isAAU()))
			{
				tempProjectsList.addAll(BeansFactory.Projects()
						.LoadByPatternAndUser(this.getSearchPattern(),
								this.getSearchTitlePattern(),
								this.getSearchAssePattern(),
								this.getSessionBean().getCurrentUser().getId(),
								false));
				oneOfConditionsFired = true;
			}
			if ((this.getSessionBean().isCIL() || this.getSessionBean()
					.isDAEC())
					&& !(this.getSessionBean().isAGU()
							|| this.getSessionBean().isSTC()
							|| this.getSessionBean().isAAU() || this
							.getSessionBean().isACU()))
			{
				tempProjectsList
						.addAll(BeansFactory.Projects()
								.LoadByPatternAndController(
										this.getSearchPattern(),
										this.getSearchTitlePattern(),
										this.getSearchAssePattern(),
										this.getSessionBean().getCurrentUser()
												.getId()));
				oneOfConditionsFired = true;
			}
			if (!oneOfConditionsFired)
			{
				tempProjectsList.addAll(BeansFactory.Projects().LoadByPattern(
						this.getSearchPattern(), this.getSearchTitlePattern(), this.getSearchAssePattern()));
			}

			Collections.sort(tempProjectsList, new Comparator<Projects>()
			{
				public int compare(Projects p1, Projects p2)
				{
					return (-1 * p1.getCreateDate().compareTo(
							p2.getCreateDate()));
				}
			});
			this.setList(tempProjectsList);

			this.listProjects = new ArrayList<ProjectsWrapper>();

			for (Projects p : this.getList())
			{

				ProjectsWrapper pw = new ProjectsWrapper(p);

				if (this.getSessionBean().isAGU()
						&& this.getSessionBean().getListCurrentUserRolesSize() == 1
						&& p.getAsse() != 5)
				{
					pw.setEditable(false);
				}
				else if (this.getSessionBean().isSTC()
						&& this.getSessionBean().getListCurrentUserRolesSize() == 1
						&& p.getAsse() == 5)
				{
					pw.setEditable(false);
				}
				else
				{
					pw.setEditable(true);
				}

				this.listProjects.add(pw);
			}
			if(this.getSearchSpecificGoalPattern()!=null)
			{
				if(!this.getSearchSpecificGoalPattern().equals("")){
					for(Iterator<ProjectsWrapper> pwIter = this.getListProjects().iterator(); pwIter.hasNext();){
						 ProjectsWrapper currentPw = pwIter.next();
						  if (!currentPw.getSpecificGoal().toString().startsWith(this.getSearchSpecificGoalPattern())) {
						     pwIter.remove();
						   }
						}
				}
			}

			// this.ReRenderScroll();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}
	}

	public void export()
	{
		try
		{
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getListProjects(),
					ExportPlaces.Projects);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportProjects") + ".xls", is,
					data.length);
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}

	public void ReRenderScroll()
	{
		if (getListProjects() != null)
		{
			if (getItemsPerPage() == null)
			{
				setItemsPerPage("10");
			}

			if (getListProjects().size() > Integer.parseInt(this
					.getItemsPerPage()))
			{
				this.setShowScroll(true);
			}
			else
			{
				this.setShowScroll(false);
			}
		}
		else
		{
			this.setShowScroll(false);
		}
	}

	/**
	 * Checks the ability of new project creating
	 * 
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 */
	private boolean CheckNewProjectButtonVisibility()
			throws PersistenceBeanException, PersistenceBeanException
	{
		if (this.getSessionBean().isAGU())
		{
			if (this.getSessionBean().isSTC())
			{
				try
				{
					return BeansFactory.ActivationProcedureAnagraph().Load()
							.size() > 0;
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					return BeansFactory.ActivationProcedureAnagraph()
							.LoadByUser(true).size() > 0;
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
					e.printStackTrace();
				}
			}
		}
		else
		{
			try
			{
				return BeansFactory.ActivationProcedureAnagraph()
						.LoadByUser(false).size() > 0;
			}
			catch (PersistenceBeanException e)
			{
				log.error(e);
				e.printStackTrace();
			}
		}

		return false;
	}

	private boolean IsCompiledToPointF()
	{
		if (this.getSession().get("InActualState") != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Add project button click handler
	 */
	public void addProject()
	{
		this.getSession().put("projectEdit", null);
		this.getSession().put("projectEditShowOnly", false);
		this.getSession().put("projectEditBackToSearch", false);

		this.goTo(PagesTypes.PROJECTSTART);
	}

	/**
	 * Edit project button click handler
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 */
	public void editProject() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.getSession().put("projectEdit", this.getEntityEditId());
		this.getSession().put("projectEditShowOnly", false);
		this.getSession().put("projectEditBackToSearch", false);

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

	/**
	 * Select project button click handler
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 */
	public void selectProject() throws NumberFormatException,
			HibernateException, PersistenceBeanException
	{
		this.getSession().put("project", this.getEntityEditId());
		this.getSession().put("projectEdit", this.getEntityEditId());
		this.getSession().put("projectEditShowOnly", true);
		this.getSession().put("projectEditBackToSearch", false);

		this.getSession().put("PAAMSN_Session_project_storage",
				BeansFactory.Projects().Load(this.getEntityEditId()));
		this.getSession().put("stored_project_id",
				this.getEntityEditId().toString());

		this.getSessionBean().setIsActualSate(AccessGrantHelper.IsActual());
		this.getSessionBean().setIsActualSateAndAguRead(
				AccessGrantHelper.IsActualAndAguAccess());
		this.getSessionBean().setIsAguRead(AccessGrantHelper.IsAguReadAccess());
		this.getSessionBean().setIsProjectClosed(
				AccessGrantHelper.IsProjectClosed());

		if (this.getSessionBean().isSTC() || this.getSessionBean().isAGU()
				|| this.getSessionBean().isSuperAdmin())
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
	
	/**
	 * Deletes specified item.
	 */
	public void deleteItem() {
		try {
			Projects project = BeansFactory.Projects().Load(String.valueOf(getEntityDeleteId()));
			if ((project != null) && !project.getDeleted()) {
				List<CFPartnerAnagraphProject> listPartnersToProject = BeansFactory
						.CFPartnerAnagraphProject().GetCFAnagraphForProject(String.valueOf(this.getEntityDeleteId()), null);

				for (CFPartnerAnagraphProject cfpap : listPartnersToProject) {
					List<CFPartnerAnagraphProject> listPartnerProjects = BeansFactory
							.CFPartnerAnagraphProject()
							.LoadByPartnerWithoutProject(cfpap.getCfPartnerAnagraphs().getId());

					if ((listPartnerProjects != null) && listPartnerProjects.size() == 1) {
						CFPartnerAnagraphs.DeleteCFAnagraph(cfpap.getCfPartnerAnagraphs().getId(), this.getEntityDeleteId());
					}
				}

				project.setDeletedSpecificationState(this.getDeletedSpecificationState());
				BeansFactory.Projects().Update(project);
				
				BeansFactory.Projects().Delete(String.valueOf(getEntityDeleteId()));
			} else {
				this.goTo(PagesTypes.PROJECTLIST);
			}

			if (this.getSession().get("project") != null) {
				if (this.getEntityDeleteId().equals(Long.valueOf(String.valueOf(this.getSession().get("project"))))) {
					this.getSession().remove("project");
				}
			}

			this.Page_Load();
		} catch (HibernateException | PersistenceBeanException | NumberFormatException e) {
			log.error(e);
		}
	}

	/**
	 * Sets searchPattern
	 * 
	 * @param searchPattern
	 *            the searchPattern to set
	 */
	public void setSearchPattern(String searchPattern)
	{
		this.getSession().put("projectSearchPattern", searchPattern);

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}

		this.ReRenderScroll();

	}

	/**
	 * Gets searchPattern
	 * 
	 * @return searchPattern the searchPattern
	 */
	public String getSearchPattern()
	{
		if (this.getSession().get("projectSearchPattern") != null)
		{
			return String
					.valueOf(this.getSession().get("projectSearchPattern"));
		}
		else
		{
			return null;
		}
	}

	public boolean isNewProjectAvailable()
	{
		return newProjectAvailable;
	}

	public void setNewProjectAvailable(boolean newProjectAvailable)
	{
		this.newProjectAvailable = newProjectAvailable;
	}

	/**
	 * Sets searchTitlePattern
	 * 
	 * @param searchTitlePattern
	 *            the searchTitlePattern to set
	 */
	public void setSearchTitlePattern(String searchTitlePattern)
	{
		this.getSession().put("projectSearchTitlePattern", searchTitlePattern);

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	/**
	 * Gets searchTitlePattern
	 * 
	 * @return searchTitlePattern the searchTitlePattern
	 */
	public String getSearchTitlePattern()
	{
		if (this.getSession().get("projectSearchTitlePattern") != null)
		{
			return String.valueOf(this.getSession().get(
					"projectSearchTitlePattern"));
		}
		else
		{
			return null;
		}
	}
	
	
	/**
	 * Gets searchAssePattern
	 * @return searchAssePattern the searchAssePattern
	 */
	public String getSearchAssePattern()
	{
		if (this.getSession().get("projectSearchAssePattern") != null)
		{
			return String.valueOf(this.getSession().get(
					"projectSearchAssePattern"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets searchAssePattern
	 * @param searchAssePattern the searchAssePattern to set
	 */
	public void setSearchAssePattern(String searchAssePattern)
	{
		this.getSession().put("projectSearchAssePattern", searchAssePattern);

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}
	
	/**
	 * Gets searchSpecificGoalPattern
	 * @return searchSpecificGoalPattern the searchSpecificGoalPattern
	 */
	public String getSearchSpecificGoalPattern()
	{
		if (this.getSession().get("projectSearchSpecificGoalPattern") != null)
		{
			return String.valueOf(this.getSession().get(
					"projectSearchSpecificGoalPattern"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets searchSpecificGoalPattern
	 * @param searchSpecificGoalPattern the searchSpecificGoalPattern to set
	 */
	public void setSearchSpecificGoalPattern(String searchSpecificGoalPattern)
	{
		this.getSession().put("projectSearchSpecificGoalPattern", searchSpecificGoalPattern);

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		if (getScroller() != null && getScroller().isPaginator())
		{
			this.scroller.getUIData().setFirst(0);
		}
		this.ReRenderScroll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{

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

	/**
	 * Gets listProjects
	 * 
	 * @return listProjects the listProjects
	 */
	public List<ProjectsWrapper> getListProjects()
	{
		return listProjects;
	}

	/**
	 * Sets listProjects
	 * 
	 * @param listProjects
	 *            the listProjects to set
	 */
	public void setListProjects(List<ProjectsWrapper> listProjects)
	{
		this.listProjects = listProjects;
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
		for(UserRoles role : getCurrentUser().getRoles())
		{
			if(role.getRole().getCode().equals(UserRoleTypes.CFP.name()))
			{
				goTo(PagesTypes.POTENTIALPROJECTSLIST);
			}
		}
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
			if (!lock)
			{
				double FESRSum = 0;
				double CNPublicSum = 0;
				double CNPrivateSum = 0;
				Long projectId = Long.valueOf(this.getEntityEditId());
				List<GeneralBudgets> listAuxGB = BeansFactory.GeneralBudgets()
						.GetItemsForProject(projectId);
				for (GeneralBudgets gb : listAuxGB)
				{
					FESRSum += gb.getFesr() == null ? 0 : gb.getFesr();
					CNPublicSum += gb.getCnPublic() == null ? 0 : gb
							.getCnPublic();
					CNPrivateSum += gb.getQuotaPrivate() == null ? 0 : gb
							.getQuotaPrivate();
				}

				BudgetInputSourceDivided sourceBudget = BudgetInputSourceDividedBean
						.GetByProject(projectId).get(0);
				double firstCheck = Math.abs(NumberHelper.Round(FESRSum)
						- NumberHelper.Round(sourceBudget.getFesr()));
				double secondCheck = Math.abs(NumberHelper.Round(CNPublicSum)
						- NumberHelper.Round(sourceBudget.getCnPublic()));

				double thirdCheck = Math.abs(NumberHelper.Round(CNPrivateSum)
						- NumberHelper.Round(sourceBudget.getCnPrivate()));
				if (!(firstCheck < 0.1 && secondCheck < 0.1 && thirdCheck < 0.1))
				{
					setGeneralBudgetMinorValidatorMessage(true);
					return;
				}

			}
			for (Projects item : this.getList())
			{
				if (this.getEntityEditId().equals(item.getId()))
				{
					item.setLocked(lock);
					BeansFactory.Projects().Save(item);
					break;
				}
			}
			for (Projects item : this.getListProjects())
			{
				if (this.getEntityEditId().equals(item.getId()))
				{
					item.setLocked(lock);
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
	 * Gets generalBudgetMinorValidatorMessage
	 * 
	 * @return generalBudgetMinorValidatorMessage the
	 *         generalBudgetMinorValidatorMessage
	 */
	public boolean isGeneralBudgetMinorValidatorMessage()
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
			boolean generalBudgetMinorValidatorMessage)
	{
		this.generalBudgetMinorValidatorMessage = generalBudgetMinorValidatorMessage;
	}
	
	/**
	 * Gets deletedSpecificationState.
	 * @return deletedSpecificationState
	 */
	public Integer getDeletedSpecificationState() {
		if (this.getViewState().get("projectListDeletedSPecificationState") != null) {
			String deletedSpecificationStateString = String.valueOf(this.getViewState().get("projectListDeletedSPecificationState"));
			return Integer.valueOf(deletedSpecificationStateString);
		} else {
			return null;
		}
	}
	
	/**
	 * Sets deletedSpecificationState.
	 * @param deletedSpecificationState
	 */
	public void setDeletedSpecificationState(Integer deletedSpecificationState) {
		this.getViewState().put("projectListDeletedSPecificationState", deletedSpecificationState);
	}
	
	/**
	 * Gets listDeletedSpecificationState.
	 * @return listDeletedSpecificationState
	 */
	public List<SelectItem> getListDeletedSpecificationState() {
		fillListDeletedSpecificationState();	
		
		return listDeletedSpecificationState;
	}
	
	/**
	 * Sets listDeletedSpecificationState.
	 * @param listDeletedSpecificationState
	 */
	public void setListDeletedSpecificationState(List<SelectItem> listDeletedSpecificationState) {
		this.listDeletedSpecificationState = listDeletedSpecificationState;
	}
	
	/**
	 * Fill listDeletedSpecificationState.
	 */
	private void fillListDeletedSpecificationState() {
		listDeletedSpecificationState = new ArrayList<SelectItem>();
		
		Locale currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("Resources", currentLocale);
		
		for (DeletedSpecificationState deletedSpecificationState : DeletedSpecificationState.values()) {
			SelectItem item = new SelectItem();
			item.setLabel(resourceBundle.getString(deletedSpecificationState.description));
			item.setValue(deletedSpecificationState.code);
			
			this.listDeletedSpecificationState.add(item);
		}
	}
}
