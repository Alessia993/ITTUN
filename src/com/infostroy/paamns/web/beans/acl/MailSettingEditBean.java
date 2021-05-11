/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.MailTemplateTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.MailSettings;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class MailSettingEditBean extends EntityEditBean<MailSettings>
{
    private String template;
    
    private String subject;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        GoBack();
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        goTo(PagesTypes.MAILSETTINGSLIST);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (this.getSession().get("MailSettingTemplate") == null)
        {
            this.GoBack();
            return;
        }
        
        Long id = (Long) this.getSession().get("MailSettingTemplate");
        MailTemplateTypes type = MailTemplateTypes.getById(id);
        if (type == null)
        {
            this.GoBack();
            return;
        }
        
        this.setSubject(BeansFactory
                .MailSettings()
                .getSettings(PersistenceSessionManager.getBean().getSession(),
                        String.format("%sSubject", type.getValue())).getValue());
        this.setTemplate(BeansFactory
                .MailSettings()
                .getSettings(PersistenceSessionManager.getBean().getSession(),
                        String.format("%sTemplate", type.getValue()))
                .getValue());
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (this.getSession().get("MailSettingTemplate") == null)
        {
            this.GoBack();
            return;
        }
        
        Long id = (Long) this.getSession().get("MailSettingTemplate");
        MailTemplateTypes type = MailTemplateTypes.getById(id);
        if (type == null)
        {
            this.GoBack();
            return;
        }
        
        BeansFactory.MailSettings().setSettings(
                String.format("%sSubject", type.getValue()), this.getSubject());
        BeansFactory.MailSettings().setSettings(
                String.format("%sTemplate", type.getValue()),
                this.getTemplate());
    }
    
    /**
     * Sets template
     * @param template the template to set
     */
    public void setTemplate(String template)
    {
        this.template = template;
    }
    
    /**
     * Gets template
     * @return template the template
     */
    public String getTemplate()
    {
        return template;
    }
    
    /**
     * Sets subject
     * @param subject the subject to set
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
    /**
     * Gets subject
     * @return subject the subject
     */
    public String getSubject()
    {
        return subject;
    }
}
