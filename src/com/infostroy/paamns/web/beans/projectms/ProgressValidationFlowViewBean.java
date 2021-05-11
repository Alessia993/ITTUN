/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProgressValidationStateTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.ValidationFlowAGUConfirmMailSender;
import com.infostroy.paamns.common.helpers.mail.ValidationFlowSFSendMailSender;
import com.infostroy.paamns.common.helpers.mail.ValidationFlowSTCSendMailSender;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjects;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.ProgressValidationObjectWrapper;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class ProgressValidationFlowViewBean extends
        EntityProjectListBean<ProgressValidationObjectWrapper>
{
    private boolean          CFMode;
    
    private boolean          STCMode;
    
    private boolean          AGUMode;
    
    private List<InfoObject> mailsValidationCFSend;
    
    private List<InfoObject> mailsValidationSTCSend;
    
    private List<InfoObject> mailsValidationAGUSend;
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        this.CheckRoles();
        
        List<ProgressValidationObjects> listProgresses = BeansFactory
                .ProgressValidationObjects().LoadByProject(
                        Long.valueOf(this.getProjectId()));
        
        this.setList(new ArrayList<ProgressValidationObjectWrapper>());
        
        int counter = listProgresses.size();
        for (ProgressValidationObjects pvo : listProgresses)
        {
            ProgressValidationObjectWrapper newEntity = new ProgressValidationObjectWrapper(
                    pvo, counter);
            
            if (newEntity.getState().getId()
                    .equals(ProgressValidationStateTypes.Create.getValue()))
            {
                if (this.getProjectAsse() == 5)
                {
                    newEntity.setUsedBy("AGU");
                }
                else
                {
                    newEntity.setUsedBy("BP");
                }
            }
            else if (newEntity
                    .getState()
                    .getId()
                    .equals(ProgressValidationStateTypes.AGUEvaluation
                            .getValue()))
            {
                newEntity.setUsedBy("AGU");
            }
            else if (newEntity
                    .getState()
                    .getId()
                    .equals(ProgressValidationStateTypes.STCEvaluation
                            .getValue()))
            {
                newEntity.setUsedBy("STC");
            }
            else if (newEntity.getState().getId()
                    .equals(ProgressValidationStateTypes.Confirmed.getValue()))
            {
                newEntity.setUsedBy("");
            }
            
            // Check whether progress validation can be send to the next step
            if (this.isCFMode()
                    && newEntity
                            .getState()
                            .getId()
                            .equals(ProgressValidationStateTypes.Create
                                    .getValue()))
            {
                newEntity.setSendable(true);
                newEntity.setDeniable(false);
                newEntity.setEditable(true);
                
            }
            else if (this.isSTCMode()
                    && newEntity
                            .getState()
                            .getId()
                            .equals(ProgressValidationStateTypes.STCEvaluation
                                    .getValue()))
            {
                newEntity.setSendable(true);
                newEntity.setDeniable(true);
                newEntity.setEditable(false);
                
            }
            else if (this.isAGUMode()
                    && newEntity
                            .getState()
                            .getId()
                            .equals(ProgressValidationStateTypes.AGUEvaluation
                                    .getValue()))
            {
                if (newEntity.getApprovalDate() != null)
                {
                    newEntity.setSendable(true);
                    newEntity.setDeniable(true);
                }
                else
                {
                    newEntity.setSendable(false);
                    newEntity.setDeniable(false);
                }
                
                newEntity.setEditable(true);
                
            }
            else if (newEntity.getState().getId()
                    .equals(ProgressValidationStateTypes.Confirmed.getValue()))
            {
                newEntity.setSendable(false);
                newEntity.setDeniable(false);
                newEntity.setEditable(false);
                
            }
            else
            {
                newEntity.setSendable(false);
                newEntity.setDeniable(false);
                newEntity.setEditable(false);
                
            }
            
            this.getList().add(newEntity);
            counter--;
        }
        
        this.ReRenderScroll();
    }
    
    private void CheckRoles()
    {
        if (this.getSessionBean().isCF()
                || (this.getSessionBean().isAGU() && this.getProjectAsse() == 5))
        {
            this.setCFMode(true);
            
            if (this.getSessionBean().isAGU())
            {
                this.setAGUMode(true);
            }
        }
        else if (this.getSessionBean().isSTC())
        {
            this.setSTCMode(true);
        }
        else if (this.getSessionBean().isAGU())
        {
            this.setAGUMode(true);
        }
    }
    
    @Override
    public void addEntity()
    {
        this.getSession().put("progressValidationEntityId", null);
        this.getSession().put("progressValidationShow", false);
        this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWEDIT);
    }
    
    @Override
    public void deleteEntity()
    {
        // Not used
    }
    
    @Override
    public void editEntity()
    {
        this.getSession().put("progressValidationEntityId",
                this.getEntityEditId());
        this.getSession().put("progressValidationShow", false);
        this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWEDIT);
    }
    
    public void showEntity()
    {
        this.getSession().put("progressValidationEntityId",
                this.getEntityEditId());
        this.getSession().put("progressValidationShow", true);
        this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWEDIT);
    }
    
    public void sendEntity() throws HibernateException,
            PersistenceBeanException
    {
        ProgressValidationObjects pvo = BeansFactory
                .ProgressValidationObjects().Load(this.getEntityEditId());
        
        if (pvo.getState().getId()
                .equals(ProgressValidationStateTypes.AGUEvaluation.getValue()))
        {
            this.setCFMode(false);
            this.setAGUMode(true);
        }
        
        if (this.isCFMode())
        {
            pvo.setState(BeansFactory.ProgressValidationStates().Load(
                    ProgressValidationStateTypes.STCEvaluation.getValue()));
            
            mailsValidationCFSend = new ArrayList<InfoObject>();
            for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.STC))
            {
                InfoObject ob = new InfoObject();
                ob.setProjectName(this.getProject().getTitle());
                ob.setMail(item.getMail());
                mailsValidationCFSend.add(ob);
            }
            
        }
        else if (this.isSTCMode())
        {
            pvo.setState(BeansFactory.ProgressValidationStates().Load(
                    ProgressValidationStateTypes.AGUEvaluation.getValue()));
            mailsValidationSTCSend = new ArrayList<InfoObject>();
            for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.AGU))
            {
                InfoObject ob = new InfoObject();
                ob.setProjectName(this.getProject().getTitle());
                ob.setMail(item.getMail());
                mailsValidationSTCSend.add(ob);
            }
        }
        else if (this.isAGUMode())
        {
            pvo.setState(BeansFactory.ProgressValidationStates().Load(
                    ProgressValidationStateTypes.Confirmed.getValue()));
            
            this.CreateNewEntities();
            mailsValidationAGUSend = new ArrayList<InfoObject>();
            for (Users item : BeansFactory.Users().getByRole(UserRoleTypes.STC))
            {
                InfoObject ob = new InfoObject();
                ob.setProjectName(this.getProject().getTitle());
                ob.setMail(item.getMail());
                mailsValidationAGUSend.add(ob);
            }
        }
        
        BeansFactory.ProgressValidationObjects().Save(pvo);
        Transaction tr = null;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            List<Mails> mails = new ArrayList<Mails>();
            ValidationFlowSFSendMailSender flowSFSendMailSender = new ValidationFlowSFSendMailSender(
                    mailsValidationCFSend);
            mails.addAll(flowSFSendMailSender
                    .completeMails(PersistenceSessionManager.getBean()
                            .getSession()));
            
            ValidationFlowAGUConfirmMailSender validationFlowAGUConfirmMailSender = new ValidationFlowAGUConfirmMailSender(
                    mailsValidationAGUSend);
            mails.addAll(validationFlowAGUConfirmMailSender
                    .completeMails(PersistenceSessionManager.getBean()
                            .getSession()));
            ValidationFlowSTCSendMailSender validationFlowSTCSendMailSender = new ValidationFlowSTCSendMailSender(
                    mailsValidationSTCSend);
            mails.addAll(validationFlowSTCSendMailSender
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
        catch(Exception e)
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
    
    public void denyEntity() throws HibernateException,
            PersistenceBeanException
    {
        ProgressValidationObjects pvo = BeansFactory
                .ProgressValidationObjects().Load(this.getEntityEditId());
        
        if (this.isCFMode())
        {
            // Do nothing
        }
        else if (this.isSTCMode())
        {
            pvo.setState(BeansFactory.ProgressValidationStates().Load(
                    ProgressValidationStateTypes.Create.getValue()));
        }
        else if (this.isAGUMode())
        {
            pvo.setState(BeansFactory.ProgressValidationStates().Load(
                    ProgressValidationStateTypes.STCEvaluation.getValue()));
            pvo.setApprovalDate(null);
        }
        
        BeansFactory.ProgressValidationObjects().Save(pvo);
        
        this.Page_Load();
    }
    
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        if (!(AccessGrantHelper.IsProgressValidationAvailable()
                && AccessGrantHelper.IsActual() && !AccessGrantHelper
                    .IsProjectClosed()))
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    private void CreateNewEntities() throws HibernateException,
            PersistenceBeanException
    {
        List<PhisicalInitializationToProgramIndicators> listRealization = BeansFactory
                .PhisicalInizializationToProgramRealizationIndicators()
                .LoadByProgressValidationObject(this.getEntityEditId());
        
        List<PhisicalInitializationToEmploymentIndicators> listEmploymentIndicators = BeansFactory
                .PhisicalInizializationToEmploymentIndicators()
                .LoadByProgressValidationObject(this.getEntityEditId());
        
        List<PhisicalInitializationToCoreIndicators> listCoreIndicators = BeansFactory
                .PhisicalInizializationToCoreIndicators()
                .LoadByProgressValidationObject(this.getEntityEditId());
        
        List<Procedures> listProcedures = BeansFactory.Procedures()
                .LoadByProgressValidationObject(this.getEntityEditId());
        List<AnnualProfiles> listFinancial = BeansFactory.AnnualProfiles()
                .LoadByProgressValidationObject(this.getEntityEditId());
        
        Long apLastVersion = BeansFactory.AnnualProfiles().GetLastVersion(
                this.getProjectId()) + 1;
        for (AnnualProfiles ap : listFinancial)
        {
            AnnualProfiles apCopy = ap.clone();
            apCopy.setCreateDate(DateTime.getNow());
            apCopy.setDeleted(false);
            apCopy.setVersion(apLastVersion);
            apCopy.setProgressValidationObject(null);
            
            BeansFactory.AnnualProfiles().Save(apCopy);
        }
        
        Long pLastVersion = BeansFactory.Procedures().GetLastVersion(
                this.getProjectId()) + 1;
        for (Procedures p : listProcedures)
        {
            Procedures pCopy = p.clone();
            pCopy.setCreateDate(DateTime.getNow());
            pCopy.setDeleted(false);
            pCopy.setVersion(pLastVersion);
            pCopy.setProgressValidationObject(null);
            
            BeansFactory.Procedures().Save(pCopy);
        }
        
        Long cLastVersion = BeansFactory
                .PhisicalInizializationToCoreIndicators().GetLastVersion(
                        this.getProjectId()) + 1;
        for (PhisicalInitializationToCoreIndicators item : listCoreIndicators)
        {
            PhisicalInitializationToCoreIndicators itemCopy = item.clone();
            itemCopy.setCreateDate(DateTime.getNow());
            itemCopy.setDeleted(false);
            itemCopy.setVersion(cLastVersion);
            itemCopy.setProgressValidationObject(null);
            
            BeansFactory.PhisicalInizializationToCoreIndicators()
                    .Save(itemCopy);
        }
        
        Long rLastVersion = BeansFactory
                .PhisicalInizializationToProgramRealizationIndicators()
                .GetLastVersion(this.getProjectId()) + 1;
        for (PhisicalInitializationToProgramIndicators item : listRealization)
        {
            PhisicalInitializationToProgramIndicators itemCopy = item.clone();
            itemCopy.setCreateDate(DateTime.getNow());
            itemCopy.setDeleted(false);
            itemCopy.setVersion(rLastVersion);
            itemCopy.setProgressValidationObject(null);
            
            BeansFactory.PhisicalInizializationToProgramRealizationIndicators()
                    .Save(itemCopy);
        }
        
        Long eLastVersion = BeansFactory
                .PhisicalInizializationToEmploymentIndicators().GetLastVersion(
                        this.getProjectId()) + 1;
        for (PhisicalInitializationToEmploymentIndicators item : listEmploymentIndicators)
        {
            PhisicalInitializationToEmploymentIndicators itemCopy = item
                    .clone();
            itemCopy.setCreateDate(DateTime.getNow());
            itemCopy.setDeleted(false);
            itemCopy.setVersion(eLastVersion);
            itemCopy.setProgressValidationObject(null);
            
            BeansFactory.PhisicalInizializationToEmploymentIndicators().Save(
                    itemCopy);
        }
    }
    
    public boolean getNewItemAvailable() throws PersistenceBeanException
    {
        return ((this.getSessionBean().isCFW() || (this.getSessionBean()
                .isAGUW() && this.getProjectAsse() == 5)) && this
                .CheckNewVersionsAvailable());
    }
    
    private boolean CheckNewVersionsAvailable() throws PersistenceBeanException
    {
        List<PhisicalInitializationToProgramIndicators> listRealization = BeansFactory
                .PhisicalInizializationToProgramRealizationIndicators()
                .LoadLastByProject(this.getProjectId());
        List<PhisicalInitializationToEmploymentIndicators> listEmploymentIndicators = BeansFactory
                .PhisicalInizializationToEmploymentIndicators()
                .LoadLastByProject(this.getProjectId());
        List<PhisicalInitializationToCoreIndicators> listCoreIndicators = BeansFactory
                .PhisicalInizializationToCoreIndicators().LoadLastByProject(
                        this.getProjectId());
        
        List<Procedures> listProcedures = BeansFactory.Procedures()
                .LoadLastByProject(this.getProjectId());
        List<AnnualProfiles> listFinancial = BeansFactory.AnnualProfiles()
                .LoadLastByProject(this.getProjectId());
        
        if (listRealization != null && !listRealization.isEmpty()
                && listEmploymentIndicators != null
                && !listEmploymentIndicators.isEmpty()
                && listCoreIndicators != null && !listCoreIndicators.isEmpty()
                && listProcedures != null && !listProcedures.isEmpty()
                && listFinancial != null && !listFinancial.isEmpty())
        {
            return (listRealization.get(0).getProgressValidationObject() == null
                    && listEmploymentIndicators.get(0)
                            .getProgressValidationObject() == null
                    && listCoreIndicators.get(0).getProgressValidationObject() == null
                    && listProcedures.get(0).getProgressValidationObject() == null && listFinancial
                    .get(0).getProgressValidationObject() == null);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Sets CFMode
     * 
     * @param cFMode
     *            the cFMode to set
     */
    public void setCFMode(boolean cFMode)
    {
        CFMode = cFMode;
    }
    
    /**
     * Gets CFMode
     * 
     * @return CFMode the cFMode
     */
    public boolean isCFMode()
    {
        return CFMode;
    }
    
    /**
     * Sets STCMode
     * 
     * @param sTCMode
     *            the sTCMode to set
     */
    public void setSTCMode(boolean sTCMode)
    {
        STCMode = sTCMode;
    }
    
    /**
     * Gets STCMode
     * 
     * @return STCMode the sTCMode
     */
    public boolean isSTCMode()
    {
        return STCMode;
    }
    
    /**
     * Sets AGUMode
     * 
     * @param aGUMode
     *            the aGUMode to set
     */
    public void setAGUMode(boolean aGUMode)
    {
        AGUMode = aGUMode;
    }
    
    /**
     * Gets AGUMode
     * 
     * @return AGUMode the aGUMode
     */
    public boolean isAGUMode()
    {
        return AGUMode;
    }
}
