/**
 * 
 */
package com.infostroy.paamns.web.beans.menu;

import java.text.ParseException;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.PageBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class MenuBean extends PageBean
{
    public String selectStyle  = "menu_item_seleted";
    
    public String defaultStyle = "menu_item";
    
    /**
     * 
     */
    public MenuBean()
    {
        this.SelectMenuItem();
    }
    
    /**
     * 
     */
    private void SelectMenuItem()
    {
        /* if (this.getRequestUrl().contains("Acl/UserList.jsf")
                 || this.getRequestUrl().contains("Acl/UserEdit.jsf"))
         {
             this.getContext().getViewRoot().findComponent("Menu:UserList")
                     .getAttributes().put("style", "color: red;");
         }*/
    }
    
    private Boolean inActualState;
    
    public boolean getInActualState()
    {
    	if(inActualState!=null){
    		return inActualState.booleanValue();
    	}
    	inActualState = Boolean.valueOf(AccessGrantHelper.getInActualState());
        return AccessGrantHelper.getInActualState();
    }
    
    private Boolean isProjectClosed;
    
    public boolean IsProjectClosed() throws PersistenceBeanException
 {
		if (isProjectClosed != null) {
			return isProjectClosed.booleanValue();
		}

		if (AccessGrantHelper.IsProjectClosed()) {
			isProjectClosed = Boolean.TRUE;
			return true;
		}
		isProjectClosed = Boolean.FALSE;
		return false;
	}
    
	private Boolean showStateManagement;
    
    public boolean getShowStateManagment() throws NumberFormatException,
            HibernateException, PersistenceBeanException
    {
		if(showStateManagement!=null){
			return showStateManagement.booleanValue();
		}
		
        Projects project = BeansFactory.Projects().Load(
                String.valueOf(this.getSession().get("project")));
        
        if (project.getAsse() < 5
                && SessionManager.getInstance().getSessionBean().isSTC())
        {
			showStateManagement = Boolean.TRUE;
            return true;
        }
        else if (project.getAsse() == 5
                && SessionManager.getInstance().getSessionBean().isAGU())
        {
			showStateManagement=Boolean.TRUE;
            return true;
        }
        else
        {
			showStateManagement=Boolean.FALSE;
            return false;
        }
    }
    
    
    private Boolean projectSelected;
    
    public boolean getProjectSelected()
    {
    	if(projectSelected!=null){
    		return projectSelected.booleanValue();
    	}
        if (this.getSession().get("project") != null
                && !String.valueOf(this.getSession().get("project")).isEmpty())
        {
        	projectSelected = Boolean.TRUE;
            return true;
        }
        projectSelected = Boolean.FALSE;
        return false;
    }
    
    private Boolean generalBudgetFilled;
    
    public boolean getGeneralBudgetFilled() throws NumberFormatException,
            PersistenceBeanException
    {
    	if(generalBudgetFilled!=null){
    		return generalBudgetFilled.booleanValue();
    	}
    	
        if (this.getSession().get("project") != null
                && !String.valueOf(this.getSession().get("project")).isEmpty())
        {
            
            List<GeneralBudgets> listAux = BeansFactory.GeneralBudgets()
                    .GetItemsForProject(
                            Long.valueOf(String.valueOf(this.getSession().get(
                                    "project"))));
            
            if (listAux == null || listAux.isEmpty())
            {
            	generalBudgetFilled= Boolean.FALSE;
                return false;
            }
            
            ProjectIformationCompletations pic = BeansFactory
                    .ProjectIformationCompletations().LoadByProject(
                            String.valueOf(this.getSession().get("project")));
            
            if (pic == null)
            {
            	generalBudgetFilled= Boolean.FALSE;
                return false;
            }
            else
            {
            	generalBudgetFilled= Boolean.TRUE;
                return true;
            }
        }
        generalBudgetFilled= Boolean.FALSE;
        return false;
    }
    
    private Boolean forBudgetListFilled;
    
    public boolean getForBudgetListFilled() throws PersistenceBeanException
    {
    	if(forBudgetListFilled!=null){
    		return forBudgetListFilled.booleanValue();
    	}
    	
        if (!this.getProjectSelected())
        {
        	forBudgetListFilled=Boolean.FALSE;
            return false;
            
        }
        
        String projectId = String.valueOf(this.getSession().get("project"));
        
        try
        {
            List<BudgetInputSourceDivided> listbudgets = BudgetInputSourceDividedBean
                    .GetByProject(projectId);
            if (listbudgets == null || listbudgets.isEmpty())
            {
            	forBudgetListFilled = Boolean.FALSE;
                return false;
            }
        }
        catch(PersistenceBeanException e1)
        {
            e1.printStackTrace();
        }
        
        List<CFPartnerAnagraphs> listCFAnagraphs = null;
        
        try
        {
            listCFAnagraphs = BeansFactory.CFPartnerAnagraphs().LoadByProject(
                    projectId);
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        if (listCFAnagraphs == null || listCFAnagraphs.isEmpty())
        {
        	forBudgetListFilled = Boolean.FALSE;
            return false;
        }
        
        List<GeneralBudgets> listGb = null;
        try
        {
            listGb = BeansFactory.GeneralBudgets().GetItemsForProject(
                    Long.valueOf(projectId));
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        if (listGb == null || listGb.isEmpty())
        {
        	forBudgetListFilled = Boolean.FALSE;
            return false;
        }
        
        if (listGb.size() != listCFAnagraphs.size())
        {
        	forBudgetListFilled = Boolean.FALSE;
            return false;
        }
        
        ProjectIformationCompletations pic = null;
        try
        {
            pic = BeansFactory.ProjectIformationCompletations().LoadByProject(
                    projectId);
            
            if (pic == null)
            {
            	forBudgetListFilled = Boolean.FALSE;
                return false;
            }
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        for (CFPartnerAnagraphs cfpa : listCFAnagraphs)
        {
            try
            {
                List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
                        .GetByProjectAndPartner(projectId,
                                String.valueOf(cfpa.getId()), true);
                
                if (listPB == null || listPB.isEmpty())
                {
                	forBudgetListFilled = Boolean.FALSE;
                    return false;
                }
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
        }
        
        if (!AccessGrantHelper.CheckIsCFOrPBProject())
        {
            if (!AccessGrantHelper.getIsAGUAsse5())
            {
                if (!(this.getSessionBean().isSTC()))
                {
                	forBudgetListFilled = Boolean.FALSE;
                    return false;
                }
            }
        }
    	forBudgetListFilled = Boolean.TRUE;
        return true;
    }
    
    private String projectAxis;
    
    public String getProjectAxis(){
    	if(projectAxis!=null){
    		return projectAxis;
    	}
    	 if (this.getSession().get("project") != null
                 && !String.valueOf(this.getSession().get("project")).isEmpty())
         {
             try
             {
                 Projects project = BeansFactory.Projects().Load(
                         String.valueOf(this.getSession().get("project")));
                 if (project != null && project.getDeleted() != null
                         && !project.getDeleted())
                 {
                	 projectAxis = String.valueOf(project.getAsse()) == null ? "" : String.valueOf(project.getAsse());
                     return projectAxis;
                 }
                 else
                 {
                     this.getSession().put("project", null);
                     projectAxis="";
                     return "";
                 }
             }
             catch(NumberFormatException e)
             {
                 log.error(e);
             }
             catch(HibernateException e)
             {
                 log.error(e);
             }
             catch(PersistenceBeanException e)
             {
                 log.error(e);
             }
         }
         projectName="";
         return "";
    }
    
    private String projectName;
    
    public String getProjectName()
    {
    	
    	if(projectName!=null){
    		return projectName;
    	}
        if (this.getSession().get("project") != null
                && !String.valueOf(this.getSession().get("project")).isEmpty())
        {
            try
            {
                Projects project = BeansFactory.Projects().Load(
                        String.valueOf(this.getSession().get("project")));
                if (project != null && project.getDeleted() != null
                        && !project.getDeleted())
                {
                	projectName = project.getCode() == null ? "" : project.getCode();
                    return projectName;
                }
                else
                {
                    this.getSession().put("project", null);
                    projectName="";
                    return "";
                }
            }
            catch(NumberFormatException e)
            {
                log.error(e);
            }
            catch(HibernateException e)
            {
                log.error(e);
            }
            catch(PersistenceBeanException e)
            {
                log.error(e);
            }
        }
        projectName="";
        return "";
    }
    
    
    private Boolean forPartner;
    
    public boolean getcForPartner()
 {
		if (forPartner != null) {
			return forPartner.booleanValue();
		}

		if (this.getSession().get("project") != null) {
			try {
				if (this.getSessionBean().isCF()
						|| this.getSessionBean().isPartner()) {
					forPartner = Boolean.TRUE;
					return true;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		forPartner = Boolean.FALSE;
		return false;
	}
    
    private Boolean aguAsse5;
    
    public boolean getAguAsse5()
    {
    	if(aguAsse5!=null){
    		return aguAsse5;
    	}
    	
        if (this.getSession().get("project") != null)
        {
            try
            {
                if (this.getSessionBean().getListCurrentUserRolesSize() == 1
                        && (this.getSessionBean().isAGU()))
                {
                    if (BeansFactory.Projects()
                            .GetByAsse(
                                    String.valueOf(this.getSession().get(
                                            "project")), 5) != null)
                    {
                    	aguAsse5 = Boolean.TRUE;
                        return true;
                    }
                    else
                    {
                    	aguAsse5 = Boolean.FALSE;
                        return false;
                    }
                }
                else
                {
                	aguAsse5 = Boolean.TRUE;
                    return true;
                }
            }
            catch(NumberFormatException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(HibernateException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(PersistenceBeanException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        aguAsse5 = Boolean.FALSE;
        return false;
    }
    
    
    private Boolean projectStateActual;
    
    public boolean getProjectStateActual()
    {
    	if(projectStateActual==null){
    		projectStateActual = AccessGrantHelper.IsInActualState();
    	}
    	
        return projectStateActual.booleanValue();
    }
    
    private Boolean durExists;
    
    public boolean getDurExists(){
    	if(durExists!=null){
    		return durExists.booleanValue();
    	}
    	
        try
        {
        	durExists = AccessGrantHelper.DurExists();
            return durExists.booleanValue();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        durExists = Boolean.FALSE;
        return false;
    }
    
    private Boolean readyDurCompilation;
    
    public boolean getReadyDurCompilation()
    {
    	if(readyDurCompilation!=null){
    		return readyDurCompilation.booleanValue();
    	}
    	
        try
        {
        	readyDurCompilation = AccessGrantHelper.IsReadyDurCompilation();
            return readyDurCompilation.booleanValue();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        readyDurCompilation = Boolean.FALSE;
        return false;
    }
    
    public void goToProjectStart()
    {
        if (this.getSession().get("project") != null)
        {
            this.getSession().put("projectEdit",
                    String.valueOf(this.getSession().get("project")));
            this.goTo(PagesTypes.PROJECTSTART);
        }
    }
    
    private Boolean durCompilationAvailable;
    
    public boolean getDurCompilationAvailable() throws HibernateException,
            PersistenceBeanException
    {
    	if(durCompilationAvailable==null){
    		durCompilationAvailable = AccessGrantHelper.CheckRolesDURCompilation();
    	}
        return durCompilationAvailable.booleanValue();
    }
    
    private Boolean durReimbursementAvailable;
    
    public boolean getDurReimbursementAvailable() throws NumberFormatException,
            PersistenceBeanException
    {
    	if(durReimbursementAvailable!=null){
    		return durReimbursementAvailable.booleanValue();
    	}
    	
        List<DurCompilations> listDC = BeansFactory.DurCompilations()
                .LoadForReimbursement(
                        Long.valueOf(String.valueOf(this.getSession().get(
                                "project"))));
        if (listDC != null && !listDC.isEmpty())
        {
        	durReimbursementAvailable = new Boolean(AccessGrantHelper.CheckRolesDURCompilation());
            return durReimbursementAvailable.booleanValue();
        }
        else
        {
        	durReimbursementAvailable= Boolean.FALSE;
            return false;
        }
    }
    
    private Boolean generalBudgetAvailable;
    
    public boolean getGeneralBudgetAvailable() throws PersistenceBeanException
    {
		if (generalBudgetAvailable != null) {
			return generalBudgetAvailable.booleanValue();
		}
    	
        if (!this.getProjectSelected())
        {
        	generalBudgetAvailable = Boolean.FALSE;
            return false;
        }
        
        if (!AccessGrantHelper.getIsAGUAsse5())
        {
            if (BudgetInputSourceDividedBean.GetByProject(
                    String.valueOf(this.getSession().get("project"))).size() < 1)
            {
            	generalBudgetAvailable = Boolean.FALSE;
                return false;
            }
        }
        
        if (BudgetInputSourceDividedBean.GetByProject(
                String.valueOf(this.getSession().get("project"))).size() < 1)
        {
        	generalBudgetAvailable = Boolean.FALSE;
            return false;
        }
        
        if (BeansFactory
                .CFPartnerAnagraphs()
                .LoadByProject(String.valueOf(this.getSession().get("project")))
                .size() < 1)
        {
        	generalBudgetAvailable = Boolean.FALSE;
            return false;
        }
        generalBudgetAvailable = Boolean.TRUE;
        return true;
    }
    
    private Boolean notRegularValue;
    
    public boolean getNotRegularAvailable() throws NumberFormatException,
            PersistenceBeanException
    {
    	if(notRegularValue==null){
    		notRegularValue = new Boolean(AccessGrantHelper.IsNotRegularSituationAvailable());
    	}
        return notRegularValue.booleanValue();
    }
    
    
    private Boolean partnerVisible;
    
    public boolean getPartnerVisible()
    {
    	if(partnerVisible!=null){
    		return partnerVisible.booleanValue();
    	}
        
        if (this.getSessionBean().isPartner()
                && this.getSessionBean().getListCurrentUserRolesSize() == 1
                && AccessGrantHelper.getProjectState().equals(
                        ProjectState.FoundingEligible))
        {
        	partnerVisible=Boolean.FALSE;
            return false;
        }
        else
        {	
        	partnerVisible = Boolean.TRUE;
            return true;
        }
    }
    
    private Boolean costCertificationAvailable;
    
    public boolean getCostCertificationAvailable()
            throws NumberFormatException, PersistenceBeanException,
            ParseException
    {
    	if(costCertificationAvailable==null){
    		costCertificationAvailable = new Boolean(AccessGrantHelper.IsCostCertificationAvailable());
    	}
        return costCertificationAvailable.booleanValue();
    }
    
    
    public boolean getProgressValidationFlowAvailable()
    {
        return AccessGrantHelper.IsProgressValidationAvailable();
    }
    
    public boolean getCanViewBudgetMenu()
    {
        if (getSessionBean().isDAEC() && !getSessionBean().isCF() && !getSessionBean().isAAU() && !getSessionBean().isACU() && !getSessionBean().isAGU() && !getSessionBean().isSTC())
        {
            return false;
        }
        return true;
    }

    public boolean getCanViewFoundingEligible()
    {
		if (getSessionBean().isDAEC() && !getSessionBean().isCF() && !getSessionBean().isAAU() && !getSessionBean().isACU() && !getSessionBean().isAGU() && !getSessionBean().isSTC())
		{
            return false;
        }
        return true;
    }

    public boolean getCanViewCostDefinitionList()
    {
        if (getSessionBean().isDAEC() && !getSessionBean().isCF() && !getSessionBean().isAAU() && !getSessionBean().isACU() && !getSessionBean().isAGU() && !getSessionBean().isSTC())
        {
            return false;
        }
        return true;
    }

    public boolean getCanViewAvanzamenti()
    {
        if (getSessionBean().isDAEC() && !getSessionBean().isCF() && !getSessionBean().isAAU() && !getSessionBean().isACU() && !getSessionBean().isAGU() && !getSessionBean().isSTC())
        {
            return false;
        }
        return true;
    }
    
    private String checkSelect(String str)
    {
        if (this.getRequestUrl().contains(str))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    private String checkSelect(String str, String str2)
    {
        if (this.getRequestUrl().contains(str)
                || this.getRequestUrl().contains(str2))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    private String checkSelect(String str, String str2, String str3)
    {
        if (this.getRequestUrl().contains(str)
                || this.getRequestUrl().contains(str2)
                || this.getRequestUrl().contains(str3))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getUserList()
    {
        return checkSelect("UserList", "UserEdit");
    }
    
    public String getLoginHistory()
    {
        return checkSelect("LoginHistory");
    }
    
    public String getChangePassword()
    {
        if (this.getRequestUrl().contains("ChangePassword"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getPotentialProjectsList()
    {
        if (this.getRequestUrl().contains("PotentialProject"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getExpirationDate()
    {
        if (this.getRequestUrl().contains("ExpirationDate"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getLegendView()
    {
        if (this.getRequestUrl().contains("LegendView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getMailSettingsEdit()
    {
        if (this.getRequestUrl().contains("MailSettingsEdit")
                || this.getRequestUrl().contains("MailSettingsList"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getMailManagmentView()
    {
        if (this.getRequestUrl().contains("MailManagmentView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getIdentificationDataView()
    {
        if (this.getRequestUrl().contains("IdentificationDataView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getAnagraphicalDataView()
    {
        if (this.getRequestUrl().contains("AnagraphicalDataView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getFinancialDataView()
    {
        if (this.getRequestUrl().contains("FinancialDataView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getPrioritaryReasonsView()
    {
        if (this.getRequestUrl().contains("PrioritaryReasonsView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getQsnGoalView()
    {
        if (this.getRequestUrl().contains("/QSNGoalView"))
        {
            return selectStyle;
        }
        return defaultStyle;
    }
    
    public String getProjectInformationCompletationView()
    {
        return checkSelect("ProjectInformationCompletation");
    }
    
    public String getPhisicalClassificationPriorityThemaView()
    {
        return checkSelect("PhisicalClassificationPriorityThemaView");
    }
    
    public String getActivationProcedureList()
    {
        return checkSelect("ActivationProcedure");
    }
    
    public String getControllerUserAnagraphList()
    {
        return checkSelect("ControllerUserAnagraph");
    }
    
    public String getProjectList()
    {
        return checkSelect("ProjectList");
    }
    
    public String getSearch()
    {
        return checkSelect("Search");
    }
    
    public String getStateManagment()
    {
        return checkSelect("StateManagment");
    }
    
    public String getStateChangesList()
    {
        return checkSelect("StateChanges");
    }
    
    public String getProjectStart()
    {
        return checkSelect("ProjectStart");
    }
    
    public String getPhisicalInitializationList()
    {
        return checkSelect("PhisicalInitialization");
    }
    
    public String getBudgetView()
    {
        return checkSelect("/BudgetView");
    }
    
    public String getCfPartnerList()
    {
        return checkSelect("CFPartnerList", "CFPartnerEdit");
    }
    
    public String getGeneralBudgetView()
    {
        return checkSelect("/GeneralBudgetView");
    }
    
    public String getPartnerBudgetView()
    {
        return checkSelect("/PartnerBudget");
    }
    
    public String getBudgetList()
    {
        return checkSelect("/BudgetList", "/BudgetEdit");
    }
    
    public String getLocalizationList()
    {
        return checkSelect("Localization");
    }
    
    public String getCfPartnerAnagraphCompletationsList()
    {
        return checkSelect("CFPartnerAnagraphCompletations");
    }
    
    public String getAnnualProfileList()
    {
        return checkSelect("AnnualProfile");
    }
    
    public String getProcedureInsertList()
    {
        return checkSelect("ProcedureInsert");
    }
    
    public String getAssignmentProcedureList()
    {
        return checkSelect("AssignmentProcedure");
    }
    
    public String getCostDefinitionList()
    {
        return checkSelect("CostDefinition");
    }
    
    public String getValidationFlowView()
    {
        return checkSelect("/ValidationFlowView");
    }
    
    public String getDurReimbursementList()
    {
        return checkSelect("DURReimbursement", "FESRInfo", "RotationFoundInfo");
    }
    
    public String getDurCompilationList()
    {
        return checkSelect("DURCompilation");
    }

    public String getSuperAdminChangeList()
    {
    	return checkSelect("SuperAdminChange");
    }
    
    public String getSuspensionManagementList()
    {
        return checkSelect("SuspensionManagement");
    }
    
    public String getExpenditureDeclarationList()
    {
        return checkSelect("ExpenditureDeclaration");
    }
    
    public String getCostManagementList()
    {
        return checkSelect("CostManagementList");
    }
    
    public String getRectificationManagementList()
    {
        return checkSelect("RectificationManagement");
    }
    
    public String getPaymentRequestList()
    {
        return checkSelect("PaymentRequest");
    }
    
    public String getNotRegularDurList()
    {
        return checkSelect("NotRegularDur");
    }
    
    public String getCostCertificationList()
    {
        return checkSelect("CostCertification");
    }
    
    public String getPhisicalProgressView()
    {
        return checkSelect("PhisicalProgress");
    }
    
    public String getFinancialProgressView()
    {
        return checkSelect("FinancialProgress");
    }
    
    public String getProcedureProgressList()
    {
        return checkSelect("ProcedureProgress");
    }
    
    public String getJuridicalEngageList()
    {
        return checkSelect("JuridicalEngage");
    }
    
    public String getProgressValidationFlowView()
    {
        return checkSelect("ProgressValidationFlow");
    }
    
    public String getLocalCheckList()
    {
        return checkSelect("LocalCheck");
    }
    
    public String getPhisicalClassificationQSNGoalView()
    {
        return checkSelect("PhisicalClassificationQSNGoalView");
    }
    
    public String getIndicatorsView()
    {
        return checkSelect("Indicators");
    }
    
    public String getDocumentList()
    {
        return checkSelect("DocumentList", "DocumentEdit");
    }
    
    public String getAppSettings()
    {
        return checkSelect("AppSettings");
    }
    
    public String getThresholdControlCosts()
    {
        return checkSelect("ThresholdControlCosts");
    }
    
	public String getProgramReportings()
	{
		return checkSelect("ProgramReportingsView");
	}
    
	public String getProjectReportings()
	{
		return checkSelect("ProjectReportingsView");
	}
    
}
