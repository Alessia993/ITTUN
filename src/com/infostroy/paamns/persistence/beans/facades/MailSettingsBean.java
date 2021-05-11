/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.EntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.MailSettings;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class MailSettingsBean extends EntityBean<MailSettings>
{
    private String budgetApprovedSubject                  = "budgetApprovedSubject";
    
    private String budgetApprovedTemplate                 = "budgetApprovedTemplate";
    
    private String budgetChangedSubject                   = "budgetChangedSubject";
    
    private String budgetChangedTemplate                  = "budgetChangedTemplate";
    
    private String budgetRefusedSubject                   = "budgetRefusedSubject";
    
    private String budgetRefusedTemplate                  = "budgetRefusedTemplate";
    
    private String cFandPartnerSubject                    = "cFandPartnerSubject";
    
    private String cFandPartnerTemplate                   = "cFandPartnerTemplate";
    
    private String from                                   = "from";
    
    private String smtpAuth                               = "smtpAuth";
    
    private String smtpPort                               = "smtpPort";
    
    private String secureProtocol                         = "secureProtocol";
    
    private String localizationSubject                    = "localizationSubject";
    
    private String localizationTemplate                   = "localizationTemplate";
    
    private String managmentDurACUAcceptSubject           = "managmentDurACUAcceptSubject";
    
    private String managmentDurACUAcceptTemplate          = "managmentDurACUAcceptTemplate";
    
    private String managmentDurAGUAcceptSubject           = "managmentDurAGUAcceptSubject";
    
    private String managmentDurAGUAcceptTemplate          = "managmentDurAGUAcceptTemplate";
    
    private String managmentDurCFSendSubject              = "managmentDurCFSendSubject";
    
    private String managmentDurCFSendTemplate             = "managmentDurCFSendTemplate";
    
    private String managmentDurSTCApproveSubject          = "managmentDurSTCApproveSubject";
    
    private String managmentDurUCApproveSubject          = "managmentDurUCApproveSubject";
    
    private String managmentDurSTCApproveTemplate         = "managmentDurSTCApproveTemplate";
    
    private String managmentDurUCApproveTemplate         = "managmentDurUCApproveTemplate";

	private String managmentDurSTCRefuseSubject           = "managmentDurSTCRefuseSubject";
    
    private String managmentDurUCRefuseSubject           = "managmentDurUCRefuseSubject";
    
    private String managmentDurSTCRefuseTemplate          = "managmentDurSTCRefuseTemplate";
    
    private String managmentDurUCRefuseTemplate          = "managmentDurUCRefuseTemplate";
    
    private String newUserForProjectSubject               = "newProjectUserSubject";
    
    private String newUserForProjectTemplate              = "newProjectUserTemplate";
    
    private String newUserSubject                         = "newUserSubject";
    
    private String newUserTemplate                        = "newUserTemplate";
    
    private String password                               = "password";
    
    private String projectInformationCompletationSubject  = "projectInformationCompletationSubject";
    
    private String projectInformationCompletationTemplate = "projectInformationCompletationTemplate";
    
    private String smtp                                   = "smtp";
    
    private String userName                               = "userName";
    
    private String validationFlowAGUConfirmSubject        = "validationFlowAGUConfirmSubject";
    
    private String validationFlowAGUConfirmTemplate       = "validationFlowAGUConfirmTemplate";
    
    private String validationFlowSFSendSubject            = "validationFlowSFSendSubject";
    
    private String validationFlowSFSendTemplate           = "validationFlowSFSendTemplate";
    
    private String validationFlowSTCSendSubject           = "validationFlowSTCSendSubject";
    
    private String validationFlowSTCSendTemplate          = "validationFlowSTCSendTemplate";
    
    /**
     * Gets budgetApprovedSubject
     * 
     * @return budgetApprovedSubject the budgetApprovedSubject
     */
    public String getBudgetApprovedSubject()
    {
        return budgetApprovedSubject;
    }
    
    /**
     * Gets budgetApprovedSubject
     * 
     * @return budgetApprovedSubject the budgetApprovedSubject
     */
    public MailSettings getBudgetApprovedSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, budgetApprovedSubject);
    }
    
    /**
     * Gets budgetApprovedTemplate
     * 
     * @return budgetApprovedTemplate the budgetApprovedTemplate
     */
    public String getBudgetApprovedTemplate()
    {
        return budgetApprovedTemplate;
    }
    
    /**
     * Gets budgetApprovedTemplate
     * 
     * @return budgetApprovedTemplate the budgetApprovedTemplate
     */
    public MailSettings getBudgetApprovedTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, budgetApprovedTemplate);
    }
    
    /**
     * Gets budgetChangedSubject
     * 
     * @return budgetChangedSubject the budgetChangedSubject
     */
    public String getBudgetChangedSubject()
    {
        return budgetChangedSubject;
    }
    
    public MailSettings getBudgetChangedSubjectSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getBudgetChangedSubject());
    }
    
    /**
     * Gets budgetChangedTemplate
     * 
     * @return budgetChangedTemplate the budgetChangedTemplate
     */
    public String getBudgetChangedTemplate()
    {
        return budgetChangedTemplate;
    }
    
    public MailSettings getBudgetChangedTemplateSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getBudgetChangedTemplate());
    }
    
    /**
     * Gets budgetRefusedSubject
     * 
     * @return budgetRefusedSubject the budgetRefusedSubject
     */
    public String getBudgetRefusedSubject()
    {
        return budgetRefusedSubject;
    }
    
    /**
     * Gets budgetRefusedSubject
     * 
     * @return budgetRefusedSubject the budgetRefusedSubject
     */
    public MailSettings getBudgetRefusedSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, budgetRefusedSubject);
    }
    
    /**
     * Gets budgetRefusedTemplate
     * 
     * @return budgetRefusedTemplate the budgetRefusedTemplate
     */
    public String getBudgetRefusedTemplate()
    {
        return budgetRefusedTemplate;
    }
    
    /**
     * Gets budgetRefusedTemplate
     * 
     * @return budgetRefusedTemplate the budgetRefusedTemplate
     */
    public MailSettings getBudgetRefusedTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, budgetRefusedTemplate);
    }
    
    public String getcFandPartnerSubject()
    {
        return cFandPartnerSubject;
    }
    
    public MailSettings getCFandPartnerSubjectSetting(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getcFandPartnerSubject());
    }
    
    public String getcFandPartnerTemplate()
    {
        return cFandPartnerTemplate;
    }
    
    public MailSettings getCFandPartnerTemplateSetting(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getcFandPartnerTemplate());
    }
    
    /**
     * Gets from
     * 
     * @return from the from
     */
    public String getFrom()
    {
        return from;
    }
    
    public MailSettings getFromSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getFrom());
    }
    
    public String getLocalizationSubject()
    {
        return localizationSubject;
    }
    
    public MailSettings getLocalizationSubjectSetting(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getLocalizationSubject());
    }
    
    public String getLocalizationTemplate()
    {
        return localizationTemplate;
    }
    
    public MailSettings getLocalizationTemplateSetting(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getLocalizationTemplate());
    }
    
    /**
     * Gets managmentDurACUAcceptSubject
     * 
     * @return managmentDurACUAcceptSubject the managmentDurACUAcceptSubject
     */
    public String getManagmentDurACUAcceptSubject()
    {
        return managmentDurACUAcceptSubject;
    }
    
    /**
     * Gets managmentDurACUAcceptSubject
     * 
     * @return managmentDurACUAcceptSubject the managmentDurACUAcceptSubject
     */
    public MailSettings getManagmentDurACUAcceptSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurACUAcceptSubject);
    }
    
    /**
     * Gets managmentDurACUAcceptTemplate
     * 
     * @return managmentDurACUAcceptTemplate the managmentDurACUAcceptTemplate
     */
    public String getManagmentDurACUAcceptTemplate()
    {
        return managmentDurACUAcceptTemplate;
    }
    
    /**
     * Gets managmentDurACUAcceptTemplate
     * 
     * @return managmentDurACUAcceptTemplate the managmentDurACUAcceptTemplate
     */
    public MailSettings getManagmentDurACUAcceptTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurACUAcceptTemplate);
    }
    
    /**
     * Gets managmentDurAGUAcceptSubject
     * 
     * @return managmentDurAGUAcceptSubject the managmentDurAGUAcceptSubject
     */
    public String getManagmentDurAGUAcceptSubject()
    {
        return managmentDurAGUAcceptSubject;
    }
    
    /**
     * Gets managmentDurAGUAcceptSubject
     * 
     * @return managmentDurAGUAcceptSubject the managmentDurAGUAcceptSubject
     */
    public MailSettings getManagmentDurAGUAcceptSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurAGUAcceptSubject);
    }
    
    /**
     * Gets managmentDurAGUAcceptTemplate
     * 
     * @return managmentDurAGUAcceptTemplate the managmentDurAGUAcceptTemplate
     */
    public String getManagmentDurAGUAcceptTemplate()
    {
        return managmentDurAGUAcceptTemplate;
    }
    
    /**
     * Gets managmentDurAGUAcceptTemplate
     * 
     * @return managmentDurAGUAcceptTemplate the managmentDurAGUAcceptTemplate
     */
    public MailSettings getManagmentDurAGUAcceptTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurAGUAcceptTemplate);
    }
    
    /**
     * Gets managmentDurCFSendSubject
     * 
     * @return managmentDurCFSendSubject the managmentDurCFSendSubject
     */
    public String getManagmentDurCFSendSubject()
    {
        return managmentDurCFSendSubject;
    }
    
    /**
     * Gets managmentDurCFSendSubject
     * 
     * @return managmentDurCFSendSubject the managmentDurCFSendSubject
     */
    public MailSettings getManagmentDurCFSendSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurCFSendSubject);
    }
    
    /**
     * Gets managmentDurCFSendTemplate
     * 
     * @return managmentDurCFSendTemplate the managmentDurCFSendTemplate
     */
    public String getManagmentDurCFSendTemplate()
    {
        return managmentDurCFSendTemplate;
    }
    
    /**
     * Gets managmentDurCFSendTemplate
     * 
     * @return managmentDurCFSendTemplate the managmentDurCFSendTemplate
     */
    public MailSettings getManagmentDurCFSendTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurCFSendTemplate);
    }
    
    /**
     * Gets managmentDurSTCApproveSubject
     * 
     * @return managmentDurSTCApproveSubject the managmentDurSTCApproveSubject
     */
    public String getManagmentDurSTCApproveSubject()
    {
        return managmentDurSTCApproveSubject;
    }
    
    /**
     * Gets managmentDurSTCApproveSubject
     * 
     * @return managmentDurSTCApproveSubject the managmentDurSTCApproveSubject
     */
    public MailSettings getManagmentDurSTCApproveSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurSTCApproveSubject);
    }
    
    /**
     * Gets managmentDurSTCApproveSubject
     * 
     * @return managmentDurSTCApproveSubject the managmentDurSTCApproveSubject
     */
    public MailSettings getManagmentDurUCApproveSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurUCApproveSubject);
    }
    
    /**
     * Gets managmentDurSTCApproveTemplate
     * 
     * @return managmentDurSTCApproveTemplate the managmentDurSTCApproveTemplate
     */
    public String getManagmentDurSTCApproveTemplate()
    {
        return managmentDurSTCApproveTemplate;
    }
    
    /**
     * Gets managmentDurSTCApproveTemplate
     * 
     * @return managmentDurSTCApproveTemplate the managmentDurSTCApproveTemplate
     */
    public MailSettings getManagmentDurSTCApproveTemplateSettings(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session, managmentDurSTCApproveTemplate);
    }
    
    public MailSettings getManagmentDurUCApproveTemplateSettings(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session, managmentDurUCApproveTemplate);
    }
    
    /**
     * Gets managmentDurSTCRefuseSubject
     * 
     * @return managmentDurSTCRefuseSubject the managmentDurSTCRefuseSubject
     */
    public String getManagmentDurSTCRefuseSubject()
    {
        return managmentDurSTCRefuseSubject;
    }
    
    /**
     * Gets managmentDurSTCRefuseSubject
     * 
     * @return managmentDurSTCRefuseSubject the managmentDurSTCRefuseSubject
     */
    public MailSettings getManagmentDurSTCRefuseSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurSTCRefuseSubject);
    }
    
    /**
     * Gets managmentDurUCRefuseSubject
     * 
     * @return managmentDurUCRefuseSubject the managmentDurUCRefuseSubject
     */
    public MailSettings getManagmentDurUCRefuseSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurUCRefuseSubject);
    }
    
    /**
     * Gets managmentDurSTCRefuseTemplate
     * 
     * @return managmentDurSTCRefuseTemplate the managmentDurSTCRefuseTemplate
     */
    public String getManagmentDurSTCRefuseTemplate()
    {
        return managmentDurSTCRefuseTemplate;
    }
    
    /**
     * Gets managmentDurSTCRefuseTemplate
     * 
     * @return managmentDurSTCRefuseTemplate the managmentDurSTCRefuseTemplate
     */
    public MailSettings getManagmentDurSTCRefuseTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurSTCRefuseTemplate);
    }
    
    public MailSettings getManagmentDurUCRefuseTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, managmentDurUCRefuseTemplate);
    }
    
    public MailSettings getNewProjectUserSubject(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getNewUserForProjectSubject());
    }
    
    public MailSettings getNewProjectUserTemplate(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, this.getNewUserForProjectTemplate());
    }
    
    /**
     * Gets newUserForProjectSubject
     * 
     * @return newUserForProjectSubject the newUserForProjectSubject
     */
    private String getNewUserForProjectSubject()
    {
        return newUserForProjectSubject;
    }
    
    /**
     * Gets newUserForProjectTemplate
     * 
     * @return newUserForProjectTemplate the newUserForProjectTemplate
     */
    private String getNewUserForProjectTemplate()
    {
        return newUserForProjectTemplate;
    }
    
    /**
     * Gets newUserSubject
     * 
     * @return newUserSubject the newUserSubject
     */
    public String getNewUserSubject()
    {
        return newUserSubject;
    }
    
    public MailSettings getNewUserSubjectSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getNewUserSubject());
    }
    
    /**
     * Gets newUserTemplate
     * 
     * @return newUserTemplate the newUserTemplate
     */
    public String getNewUserTemplate()
    {
        return newUserTemplate;
    }
    
    public MailSettings getNewUserTemplateSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getNewUserTemplate());
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public MailSettings getPasswordSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getPassword());
    }
    
    public String getProjectInformationCompletationSubject()
    {
        return projectInformationCompletationSubject;
    }
    
    public MailSettings getProjectInformationCompletationSubjectSetting(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session,
                this.getProjectInformationCompletationSubject());
    }
    
    public String getProjectInformationCompletationTemplate()
    {
        return projectInformationCompletationTemplate;
    }
    
    public MailSettings getProjectInformationCompletationTemplateSetting(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session,
                this.getProjectInformationCompletationTemplate());
    }
    
    public MailSettings getSettings(Session session, String str)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        try
        {
            session.clear();
        }
        catch(Exception e)
        {
        }
        MailSettings item = (MailSettings) session
                .createCriteria(MailSettings.class)
                .add(Restrictions.eq("key", str)).uniqueResult();
        if (item == null)
        {
            item = new MailSettings();
        }
        return item;
    }
    
    /**
     * Gets smtp
     * 
     * @return smtp the smtp
     */
    public String getSmtp()
    {
        return smtp;
    }
    
    public MailSettings getSmtpSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getSmtp());
    }
    
    public MailSettings getSmtpAuthSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getSmtpAuth());
    }
    
    public MailSettings getSmtpPortSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getSmtpPort());
    }
    
    public MailSettings getSecureProtocolSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getSecureProtocol());
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public MailSettings getUserNameSettings(Session session)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return getSettings(session, getUserName());
    }
    
    /**
     * Gets validationFlowAGUConfirmSubject
     * 
     * @return validationFlowAGUConfirmSubject the
     *         validationFlowAGUConfirmSubject
     */
    public String getValidationFlowAGUConfirmSubject()
    {
        return validationFlowAGUConfirmSubject;
    }
    
    /**
     * Gets validationFlowAGUConfirmSubject
     * 
     * @return validationFlowAGUConfirmSubject the
     *         validationFlowAGUConfirmSubject
     * @throws PersistenceBeanException
     * @throws HibernateException
     */
    public MailSettings getValidationFlowAGUConfirmSubjectSettings(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session, validationFlowAGUConfirmSubject);
    }
    
    /**
     * Gets validationFlowAGUConfirmTemplate
     * 
     * @return validationFlowAGUConfirmTemplate the
     *         validationFlowAGUConfirmTemplate
     */
    public String getValidationFlowAGUConfirmTemplate()
    {
        return validationFlowAGUConfirmTemplate;
    }
    
    /**
     * Gets validationFlowAGUConfirmTemplate
     * 
     * @return validationFlowAGUConfirmTemplate the
     *         validationFlowAGUConfirmTemplate
     */
    public MailSettings getValidationFlowAGUConfirmTemplateSettings(
            Session session) throws HibernateException,
            PersistenceBeanException
    {
        return this.getSettings(session, validationFlowAGUConfirmTemplate);
    }
    
    /**
     * Gets validationFlowSFSendSubject
     * 
     * @return validationFlowSFSendSubject the validationFlowSFSendSubject
     */
    public String getValidationFlowSFSendSubject()
    {
        return validationFlowSFSendSubject;
    }
    
    /**
     * Gets validationFlowSFSendSubject
     * 
     * @return validationFlowSFSendSubject the validationFlowSFSendSubject
     */
    public MailSettings getValidationFlowSFSendSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, validationFlowSFSendSubject);
    }
    
    /**
     * Gets validationFlowSFSendTemplate
     * 
     * @return validationFlowSFSendTemplate the validationFlowSFSendTemplate
     */
    public String getValidationFlowSFSendTemplate()
    {
        return validationFlowSFSendTemplate;
    }
    
    /**
     * Gets validationFlowSFSendTemplate
     * 
     * @return validationFlowSFSendTemplate the validationFlowSFSendTemplate
     */
    public MailSettings getValidationFlowSFSendTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, validationFlowSFSendTemplate);
    }
    
    /**
     * Gets validationFlowSTCSendSubject
     * 
     * @return validationFlowSTCSendSubject the validationFlowSTCSendSubject
     */
    public String getValidationFlowSTCSendSubject()
    {
        return validationFlowSTCSendSubject;
    }
    
    /**
     * Gets validationFlowSTCSendSubject
     * 
     * @return validationFlowSTCSendSubject the validationFlowSTCSendSubject
     */
    public MailSettings getValidationFlowSTCSendSubjectSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, validationFlowSTCSendSubject);
    }
    
    /**
     * Gets validationFlowSTCSendTemplate
     * 
     * @return validationFlowSTCSendTemplate the validationFlowSTCSendTemplate
     */
    public String getValidationFlowSTCSendTemplate()
    {
        return validationFlowSTCSendTemplate;
    }
    
    /**
     * Gets validationFlowSTCSendTemplate
     * 
     * @return validationFlowSTCSendTemplate the validationFlowSTCSendTemplate
     */
    public MailSettings getValidationFlowSTCSendTemplateSettings(Session session)
            throws HibernateException, PersistenceBeanException
    {
        return this.getSettings(session, validationFlowSTCSendTemplate);
    }
    
    /**
     * Sets budgetApprovedSubject
     * 
     * @param budgetApprovedSubject
     *            the budgetApprovedSubject to set
     */
    public void setBudgetApprovedSubject(String budgetApprovedSubject)
    {
        this.budgetApprovedSubject = budgetApprovedSubject;
    }
    
    /**
     * Sets budgetApprovedSubject
     * 
     * @param budgetApprovedSubject
     *            the budgetApprovedSubject to set
     */
    public void setBudgetApprovedSubjectSettings(String budgetApprovedSubject)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.budgetApprovedSubject, budgetApprovedSubject);
    }
    
    /**
     * Sets budgetApprovedTemplate
     * 
     * @param budgetApprovedTemplate
     *            the budgetApprovedTemplate to set
     */
    public void setBudgetApprovedTemplate(String budgetApprovedTemplate)
    {
        this.budgetApprovedTemplate = budgetApprovedTemplate;
    }
    
    /**
     * Sets budgetApprovedTemplate
     * 
     * @param budgetApprovedTemplate
     *            the budgetApprovedTemplate to set
     */
    public void setBudgetApprovedTemplateSettings(String budgetApprovedTemplate)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.budgetApprovedTemplate, budgetApprovedTemplate);
    }
    
    /**
     * Sets budgetChangedSubject
     * 
     * @param budgetChangedSubject
     *            the budgetChangedSubject to set
     */
    public void setBudgetChangedSubject(String budgetChangedSubject)
    {
        this.budgetChangedSubject = budgetChangedSubject;
    }
    
    public void setBudgetChangedSubjectSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getBudgetChangedSubject(), value);
    }
    
    /**
     * Sets budgetChangedTemplate
     * 
     * @param budgetChangedTemplate
     *            the budgetChangedTemplate to set
     */
    public void setBudgetChangedTemplate(String budgetChangedTemplate)
    {
        this.budgetChangedTemplate = budgetChangedTemplate;
    }
    
    public void setBudgetChangedTemplateSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getBudgetChangedTemplate(), value);
    }
    
    /**
     * Sets budgetRefusedSubject
     * 
     * @param budgetRefusedSubject
     *            the budgetRefusedSubject to set
     */
    public void setBudgetRefusedSubject(String budgetRefusedSubject)
    {
        this.budgetRefusedSubject = budgetRefusedSubject;
    }
    
    /**
     * Sets budgetRefusedSubject
     * 
     * @param budgetRefusedSubject
     *            the budgetRefusedSubject to set
     */
    public void setBudgetRefusedSubjectSettings(String budgetRefusedSubject)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.budgetRefusedSubject, budgetRefusedSubject);
    }
    
    /**
     * Sets budgetRefusedTemplate
     * 
     * @param budgetRefusedTemplate
     *            the budgetRefusedTemplate to set
     */
    public void setBudgetRefusedTemplate(String budgetRefusedTemplate)
    {
        this.budgetRefusedTemplate = budgetRefusedTemplate;
    }
    
    /**
     * Sets budgetRefusedTemplate
     * 
     * @param budgetRefusedTemplate
     *            the budgetRefusedTemplate to set
     * @throws PersistenceBeanException
     * @throws HibernateException
     */
    public void setBudgetRefusedTemplateSettings(String budgetRefusedTemplate)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.budgetRefusedTemplate, budgetRefusedTemplate);
    }
    
    public void setcFandPartnerSubject(String cFandPartnerSubject)
    {
        this.cFandPartnerSubject = cFandPartnerSubject;
    }
    
    public void setCFandPartnerSubjectSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getcFandPartnerSubject(), value);
    }
    
    public void setcFandPartnerTemplate(String cFandPartnerTemplate)
    {
        this.cFandPartnerTemplate = cFandPartnerTemplate;
    }
    
    public void setCFandPartnerTemplateSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getcFandPartnerTemplate(), value);
    }
    
    /**
     * Sets from
     * 
     * @param from
     *            the from to set
     */
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public void setFromSettings(String value) throws PersistenceBeanException,
            HibernateException, PersistenceBeanException
    {
        setSettings(getFrom(), value);
    }
    
    public void setSmtpAuthSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getSmtpAuth(), value);
    }
    
    public void setSmtpPortSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getSmtpPort(), value);
    }
    
    public void setSecureProtocolSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getSecureProtocol(), value);
    }
    
    public void setLocalizationSubject(String localizationSubject)
    {
        this.localizationSubject = localizationSubject;
    }
    
    public void setLocalizationSubjectSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getLocalizationSubject(), value);
    }
    
    public void setLocalizationTemplate(String localizationTemplate)
    {
        this.localizationTemplate = localizationTemplate;
    }
    
    public void setLocalizationTemplateSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getLocalizationTemplate(), value);
    }
    
    /**
     * Sets managmentDurACUAcceptSubject
     * 
     * @param managmentDurACUAcceptSubject
     *            the managmentDurACUAcceptSubject to set
     */
    public void setManagmentDurACUAcceptSubject(
            String managmentDurACUAcceptSubject)
    {
        this.managmentDurACUAcceptSubject = managmentDurACUAcceptSubject;
    }
    
    /**
     * Sets managmentDurACUAcceptSubject
     * 
     * @param managmentDurACUAcceptSubject
     *            the managmentDurACUAcceptSubject to set
     */
    public void setManagmentDurACUAcceptSubjectSettings(
            String managmentDurACUAcceptSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurACUAcceptSubject,
                managmentDurACUAcceptSubject);
    }
    
    /**
     * Sets managmentDurACUAcceptTemplate
     * 
     * @param managmentDurACUAcceptTemplate
     *            the managmentDurACUAcceptTemplate to set
     */
    public void setManagmentDurACUAcceptTemplate(
            String managmentDurACUAcceptTemplate)
    {
        this.managmentDurACUAcceptTemplate = managmentDurACUAcceptTemplate;
    }
    
    /**
     * Sets managmentDurACUAcceptTemplate
     * 
     * @param managmentDurACUAcceptTemplate
     *            the managmentDurACUAcceptTemplate to set
     */
    public void setManagmentDurACUAcceptTemplateSettings(
            String managmentDurACUAcceptTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurACUAcceptTemplate,
                managmentDurACUAcceptTemplate);
    }
    
    /**
     * Sets managmentDurAGUAcceptSubject
     * 
     * @param managmentDurAGUAcceptSubject
     *            the managmentDurAGUAcceptSubject to set
     */
    public void setManagmentDurAGUAcceptSubject(
            String managmentDurAGUAcceptSubject)
    {
        this.managmentDurAGUAcceptSubject = managmentDurAGUAcceptSubject;
    }
    
    /**
     * Sets managmentDurAGUAcceptSubject
     * 
     * @param managmentDurAGUAcceptSubject
     *            the managmentDurAGUAcceptSubject to set
     */
    public void setManagmentDurAGUAcceptSubjectSettings(
            String managmentDurAGUAcceptSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurAGUAcceptSubject,
                managmentDurAGUAcceptSubject);
    }
    
    /**
     * Sets managmentDurAGUAcceptTemplate
     * 
     * @param managmentDurAGUAcceptTemplate
     *            the managmentDurAGUAcceptTemplate to set
     */
    public void setManagmentDurAGUAcceptTemplate(
            String managmentDurAGUAcceptTemplate)
    {
        this.managmentDurAGUAcceptTemplate = managmentDurAGUAcceptTemplate;
    }
    
    /**
     * Sets managmentDurAGUAcceptTemplate
     * 
     * @param managmentDurAGUAcceptTemplate
     *            the managmentDurAGUAcceptTemplate to set
     */
    public void setManagmentDurAGUAcceptTemplateSettings(
            String managmentDurAGUAcceptTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurAGUAcceptTemplate,
                managmentDurAGUAcceptTemplate);
    }
    
    /**
     * Sets managmentDurCFSendSubject
     * 
     * @param managmentDurCFSendSubject
     *            the managmentDurCFSendSubject to set
     */
    public void setManagmentDurCFSendSubject(String managmentDurCFSendSubject)
    {
        this.managmentDurCFSendSubject = managmentDurCFSendSubject;
    }
    
    /**
     * Sets managmentDurCFSendSubject
     * 
     * @param managmentDurCFSendSubject
     *            the managmentDurCFSendSubject to set
     */
    public void setManagmentDurCFSendSubjectSettings(
            String managmentDurCFSendSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurCFSendSubject,
                managmentDurCFSendSubject);
    }
    
    /**
     * Sets managmentDurCFSendTemplate
     * 
     * @param managmentDurCFSendTemplate
     *            the managmentDurCFSendTemplate to set
     */
    public void setManagmentDurCFSendTemplate(String managmentDurCFSendTemplate)
    {
        this.managmentDurCFSendTemplate = managmentDurCFSendTemplate;
    }
    
    /**
     * Sets managmentDurCFSendTemplate
     * 
     * @param managmentDurCFSendTemplate
     *            the managmentDurCFSendTemplate to set
     */
    public void setManagmentDurCFSendTemplateSettings(
            String managmentDurCFSendTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurCFSendTemplate,
                managmentDurCFSendTemplate);
    }
    
    /**
     * Sets managmentDurSTCApproveSubject
     * 
     * @param managmentDurSTCApproveSubject
     *            the managmentDurSTCApproveSubject to set
     */
    public void setManagmentDurSTCApproveSubject(
            String managmentDurSTCApproveSubject)
    {
        this.managmentDurSTCApproveSubject = managmentDurSTCApproveSubject;
    }
    
    /**
     * Sets managmentDurSTCApproveSubject
     * 
     * @param managmentDurSTCApproveSubject
     *            the managmentDurSTCApproveSubject to set
     */
    public void setManagmentDurSTCApproveSubjectSettings(
            String managmentDurSTCApproveSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurSTCApproveSubject,
                managmentDurSTCApproveSubject);
    }
    
    /**
     * Sets managmentDurSTCApproveTemplate
     * 
     * @param managmentDurSTCApproveTemplate
     *            the managmentDurSTCApproveTemplate to set
     */
    public void setManagmentDurSTCApproveTemplate(
            String managmentDurSTCApproveTemplate)
    {
        this.managmentDurSTCApproveTemplate = managmentDurSTCApproveTemplate;
    }
    
    /**
     * Sets managmentDurSTCApproveTemplate
     * 
     * @param managmentDurSTCApproveTemplate
     *            the managmentDurSTCApproveTemplate to set
     */
    public void setManagmentDurSTCApproveTemplateSettings(
            String managmentDurSTCApproveTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurSTCApproveTemplate,
                managmentDurSTCApproveTemplate);
    }
    
    /**
     * Sets managmentDurSTCRefuseSubject
     * 
     * @param managmentDurSTCRefuseSubject
     *            the managmentDurSTCRefuseSubject to set
     */
    public void setManagmentDurSTCRefuseSubject(
            String managmentDurSTCRefuseSubject)
    {
        this.managmentDurSTCRefuseSubject = managmentDurSTCRefuseSubject;
    }
    
    /**
     * Sets managmentDurSTCRefuseSubject
     * 
     * @param managmentDurSTCRefuseSubject
     *            the managmentDurSTCRefuseSubject to set
     */
    public void setManagmentDurSTCRefuseSubjectSettings(
            String managmentDurSTCRefuseSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurSTCRefuseSubject,
                managmentDurSTCRefuseSubject);
    }
    
    /**
     * Sets managmentDurSTCRefuseTemplate
     * 
     * @param managmentDurSTCRefuseTemplate
     *            the managmentDurSTCRefuseTemplate to set
     */
    public void setManagmentDurSTCRefuseTemplate(
            String managmentDurSTCRefuseTemplate)
    {
        this.managmentDurSTCRefuseTemplate = managmentDurSTCRefuseTemplate;
    }
    
    /**
     * Sets managmentDurSTCRefuseTemplate
     * 
     * @param managmentDurSTCRefuseTemplate
     *            the managmentDurSTCRefuseTemplate to set
     */
    public void setManagmentDurSTCRefuseTemplateSettings(
            String managmentDurSTCRefuseTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.managmentDurSTCRefuseTemplate,
                managmentDurSTCRefuseTemplate);
    }
    
    public void setNewProjectUserSubject(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getNewUserForProjectSubject(), value);
    }
    
    public void setNewProjectUserTemplate(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getNewUserForProjectTemplate(), value);
    }
    
    /**
     * Sets newUserForProjectSubject
     * 
     * @param newUserForProjectSubject
     *            the newUserForProjectSubject to set
     */
    @SuppressWarnings("unused")
    private void setNewUserForProjectSubject(String newUserForProjectSubject)
    {
        this.newUserForProjectSubject = newUserForProjectSubject;
    }
    
    /**
     * Sets newUserForProjectTemplate
     * 
     * @param newUserForProjectTemplate
     *            the newUserForProjectTemplate to set
     */
    @SuppressWarnings("unused")
    private void setNewUserForProjectTemplate(String newUserForProjectTemplate)
    {
        this.newUserForProjectTemplate = newUserForProjectTemplate;
    }
    
    /**
     * Sets newUserSubject
     * 
     * @param newUserSubject
     *            the newUserSubject to set
     */
    public void setNewUserSubject(String newUserSubject)
    {
        this.newUserSubject = newUserSubject;
    }
    
    public void setNewUserSubjectSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getNewUserSubject(), value);
    }
    
    /**
     * Sets newUserTemplate
     * 
     * @param newUserTemplate
     *            the newUserTemplate to set
     */
    public void setNewUserTemplate(String newUserTemplate)
    {
        this.newUserTemplate = newUserTemplate;
    }
    
    public void setNewUserTemplateSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getNewUserTemplate(), value);
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public void setPasswordSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getPassword(), value);
    }
    
    public void setProjectInformationCompletationSubject(
            String projectInformationCompletationSubject)
    {
        this.projectInformationCompletationSubject = projectInformationCompletationSubject;
    }
    
    public void setProjectInformationCompletationSubjectSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getProjectInformationCompletationSubject(), value);
    }
    
    public void setProjectInformationCompletationTemplate(
            String projectInformationCompletationTemplate)
    {
        this.projectInformationCompletationTemplate = projectInformationCompletationTemplate;
    }
    
    public void setProjectInformationCompletationTemplateSetting(String value)
            throws HibernateException, PersistenceBeanException
    {
        this.setSettings(this.getProjectInformationCompletationTemplate(),
                value);
    }
    
    public void setSettings(String str, String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        MailSettings item = (MailSettings) PersistenceSessionManager.getBean()
                .getSession().createCriteria(MailSettings.class)
                .add(Restrictions.eq("key", str)).uniqueResult();
        if (item == null)
        {
            item = new MailSettings();
            item.setKey(str);
        }
        item.setValue(value);
        BeansFactory.MailSettings().SaveInTransaction(item);
    }
    
    /**
     * Sets smtp
     * 
     * @param smtp
     *            the smtp to set
     */
    public void setSmtp(String smtp)
    {
        this.smtp = smtp;
    }
    
    public void setSmtpSettings(String value) throws PersistenceBeanException,
            HibernateException, PersistenceBeanException
    {
        setSettings(getSmtp(), value);
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public void setUserNameSettings(String value)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        setSettings(getUserName(), value);
    }
    
    /**
     * Sets validationFlowAGUConfirmSubject
     * 
     * @param validationFlowAGUConfirmSubject
     *            the validationFlowAGUConfirmSubject to set
     */
    public void setValidationFlowAGUConfirmSubject(
            String validationFlowAGUConfirmSubject)
    {
        this.validationFlowAGUConfirmSubject = validationFlowAGUConfirmSubject;
    }
    
    /**
     * Sets validationFlowAGUConfirmSubject
     * 
     * @param validationFlowAGUConfirmSubject
     *            the validationFlowAGUConfirmSubject to set
     * @throws PersistenceBeanException
     * @throws HibernateException
     */
    public void setValidationFlowAGUConfirmSubjectSettings(
            String validationFlowAGUConfirmSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowAGUConfirmSubject,
                validationFlowAGUConfirmSubject);
    }
    
    /**
     * Sets validationFlowAGUConfirmTemplate
     * 
     * @param validationFlowAGUConfirmTemplate
     *            the validationFlowAGUConfirmTemplate to set
     */
    public void setValidationFlowAGUConfirmTemplate(
            String validationFlowAGUConfirmTemplate)
    {
        this.validationFlowAGUConfirmTemplate = validationFlowAGUConfirmTemplate;
    }
    
    /**
     * Sets validationFlowAGUConfirmTemplate
     * 
     * @param validationFlowAGUConfirmTemplate
     *            the validationFlowAGUConfirmTemplate to set
     */
    public void setValidationFlowAGUConfirmTemplateSettings(
            String validationFlowAGUConfirmTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowAGUConfirmTemplate,
                validationFlowAGUConfirmTemplate);
    }
    
    /**
     * Sets validationFlowSFSendSubject
     * 
     * @param validationFlowSFSendSubject
     *            the validationFlowSFSendSubject to set
     */
    public void setValidationFlowSFSendSubject(
            String validationFlowSFSendSubject)
    {
        this.validationFlowSFSendSubject = validationFlowSFSendSubject;
    }
    
    /**
     * Sets validationFlowSFSendSubject
     * 
     * @param validationFlowSFSendSubject
     *            the validationFlowSFSendSubject to set
     */
    public void setValidationFlowSFSendSubjectSettings(
            String validationFlowSFSendSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowSFSendSubject,
                validationFlowSFSendSubject);
    }
    
    /**
     * Sets validationFlowSFSendTemplate
     * 
     * @param validationFlowSFSendTemplate
     *            the validationFlowSFSendTemplate to set
     */
    public void setValidationFlowSFSendTemplate(
            String validationFlowSFSendTemplate)
    {
        this.validationFlowSFSendTemplate = validationFlowSFSendTemplate;
    }
    
    /**
     * Sets validationFlowSFSendTemplate
     * 
     * @param validationFlowSFSendTemplate
     *            the validationFlowSFSendTemplate to set
     */
    public void setValidationFlowSFSendTemplateSettings(
            String validationFlowSFSendTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowSFSendTemplate,
                validationFlowSFSendTemplate);
    }
    
    /**
     * Sets validationFlowSTCSendSubject
     * 
     * @param validationFlowSTCSendSubject
     *            the validationFlowSTCSendSubject to set
     */
    public void setValidationFlowSTCSendSubject(
            String validationFlowSTCSendSubject)
    {
        this.validationFlowSTCSendSubject = validationFlowSTCSendSubject;
    }
    
    /**
     * Sets validationFlowSTCSendSubject
     * 
     * @param validationFlowSTCSendSubject
     *            the validationFlowSTCSendSubject to set
     */
    public void setValidationFlowSTCSendSubjectSettings(
            String validationFlowSTCSendSubject) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowSTCSendSubject,
                validationFlowSTCSendSubject);
    }
    
    /**
     * Sets validationFlowSTCSendTemplate
     * 
     * @param validationFlowSTCSendTemplate
     *            the validationFlowSTCSendTemplate to set
     */
    public void setValidationFlowSTCSendTemplate(
            String validationFlowSTCSendTemplate)
    {
        this.validationFlowSTCSendTemplate = validationFlowSTCSendTemplate;
    }
    
    /**
     * Sets validationFlowSTCSendTemplate
     * 
     * @param validationFlowSTCSendTemplate
     *            the validationFlowSTCSendTemplate to set
     */
    public void setValidationFlowSTCSendTemplateSettings(
            String validationFlowSTCSendTemplate) throws HibernateException,
            PersistenceBeanException
    {
        this.setSettings(this.validationFlowSTCSendTemplate,
                validationFlowSTCSendTemplate);
    }
    
    /**
     * Sets smtpAuth
     * 
     * @param smtpAuth
     *            the smtpAuth to set
     */
    public void setSmtpAuth(String smtpAuth)
    {
        this.smtpAuth = smtpAuth;
    }
    
    /**
     * Gets smtpAuth
     * 
     * @return smtpAuth the smtpAuth
     */
    public String getSmtpAuth()
    {
        return smtpAuth;
    }
    
    /**
     * Sets secureProtocol
     * 
     * @param secureProtocol
     *            the secureProtocol to set
     */
    public void setSecureProtocol(String secureProtocol)
    {
        this.secureProtocol = secureProtocol;
    }
    
    /**
     * Gets secureProtocol
     * 
     * @return secureProtocol the secureProtocol
     */
    public String getSecureProtocol()
    {
        return secureProtocol;
    }
    
    /**
     * Sets smtpPort
     * 
     * @param smtpPort
     *            the smtpPort to set
     */
    public void setSmtpPort(String smtpPort)
    {
        this.smtpPort = smtpPort;
    }
    
    /**
     * Gets smtpPort
     * 
     * @return smtpPort the smtpPort
     */
    public String getSmtpPort()
    {
        return smtpPort;
    }
    
    /**
	 * Sets managmentDurUCApproveSubject
	 * @param managmentDurUCApproveSubject the managmentDurUCApproveSubject to set
	 */
	public void setManagmentDurUCApproveSubject(String managmentDurUCApproveSubject)
	{
		this.managmentDurUCApproveSubject = managmentDurUCApproveSubject;
	}

	/**
	 * Sets managmentDurUCApproveTemplate
	 * @param managmentDurUCApproveTemplate the managmentDurUCApproveTemplate to set
	 */
	public void setManagmentDurUCApproveTemplate(
			String managmentDurUCApproveTemplate)
	{
		this.managmentDurUCApproveTemplate = managmentDurUCApproveTemplate;
	}

	/**
	 * Sets managmentDurUCRefuseSubject
	 * @param managmentDurUCRefuseSubject the managmentDurUCRefuseSubject to set
	 */
	public void setManagmentDurUCRefuseSubject(String managmentDurUCRefuseSubject)
	{
		this.managmentDurUCRefuseSubject = managmentDurUCRefuseSubject;
	}

	/**
	 * Sets managmentDurUCRefuseTemplate
	 * @param managmentDurUCRefuseTemplate the managmentDurUCRefuseTemplate to set
	 */
	public void setManagmentDurUCRefuseTemplate(String managmentDurUCRefuseTemplate)
	{
		this.managmentDurUCRefuseTemplate = managmentDurUCRefuseTemplate;
	}
}
