/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.MailTemplateTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.TextCoder;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.MailSettingWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class MailSettingListBean extends EntityListBean<MailSettingWrapper>
{
    private String  smtp;
    
    private String  from;
    
    private String  name;
    
    private String  password;
    
    private String  port;
    
    private boolean smtpAuth;
    
    private boolean secureProtocol;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        this.setName(BeansFactory
                .MailSettings()
                .getUserNameSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue());
        this.setPassword(BeansFactory
                .MailSettings()
                .getPasswordSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue());
        this.setFrom(BeansFactory
                .MailSettings()
                .getFromSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue());
        this.setSmtp(BeansFactory
                .MailSettings()
                .getSmtpSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue());
        this.setSmtpAuth((BeansFactory
                .MailSettings()
                .getSmtpAuthSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue() != null && !BeansFactory
                .MailSettings()
                .getSmtpAuthSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue().isEmpty()) ? Boolean.parseBoolean(BeansFactory
                .MailSettings()
                .getSmtpAuthSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue()) : true);
        this.setSecureProtocol((BeansFactory
                .MailSettings()
                .getSecureProtocolSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue() != null && !BeansFactory
                .MailSettings()
                .getSecureProtocolSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue().isEmpty()) ? Boolean.parseBoolean(BeansFactory
                .MailSettings()
                .getSecureProtocolSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue()) : true);
        this.setPort(BeansFactory
                .MailSettings()
                .getSmtpPortSettings(
                        PersistenceSessionManager.getBean().getSession())
                .getValue());
        this.setList(new ArrayList<MailSettingWrapper>());
        this.getList().clear();
        for (MailTemplateTypes type : MailTemplateTypes.values())
        {
            this.getList().add(
                    new MailSettingWrapper(type.getId(), type.getValue(), Utils
                            .getString(type.getValue())));
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
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        this.getSession().put("MailSettingTemplate", this.getEntityEditId());
        this.goTo(PagesTypes.MAILSETTINGSEDIT);
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
    
    /**
     * Gets smtp
     * 
     * @return smtp the smtp
     */
    public String getSmtp()
    {
        return smtp;
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
    
    /**
     * Gets from
     * 
     * @return from the from
     */
    public String getFrom()
    {
        return from;
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
     * Sets password
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * Gets password
     * 
     * @return password the password
     */
    public String getPassword()
    {
        return password;
    }
    
    private Transaction tr;
    
    /**
     * Sets saveFlag
     * 
     * @param saveFlag
     *            the saveFlag to set
     */
    public void setSaveFlag(int saveFlag)
    {
        this.getViewState().put("saveFlag", saveFlag);
    }
    
    /**
     * Gets saveFlag
     * 
     * @return saveFlag the saveFlag
     */
    public int getSaveFlag()
    {
        return this.getViewState().get("saveFlag") == null ? 0 : (Integer) this
                .getViewState().get("saveFlag");
    }
    
    public void Page_Save()
    {
        if (getSaveFlag() == 0)
        {
            
            try
            {
                tr = PersistenceSessionManager.getBean().getSession()
                        .beginTransaction();
            }
            catch(HibernateException e1)
            {
                log.error(e1);
            }
            catch(PersistenceBeanException e1)
            {
                log.error(e1);
            }
            setSaveFlag(1);
            try
            {
                this.SaveEntity();
            }
            catch(HibernateException e)
            {
                if (tr != null)
                {
                    tr.rollback();
                }
                log.error(e);
            }
            catch(PersistenceBeanException e)
            {
                if (tr != null)
                {
                    tr.rollback();
                }
                log.error(e);
            }
            catch(NumberFormatException e)
            {
                if (tr != null)
                {
                    tr.rollback();
                }
                log.error(e);
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
            
            setSaveFlag(0);
        }
    }
    
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        BeansFactory.MailSettings().setFromSettings(getFrom());
        BeansFactory.MailSettings().setUserNameSettings(getName());
        if (this.getPassword() != null && !this.getPassword().isEmpty())
        {
            BeansFactory.MailSettings().setPasswordSettings(
                    TextCoder.Encode(getPassword()));
        }
        
        BeansFactory.MailSettings().setSmtpSettings(getSmtp());
        BeansFactory.MailSettings().setSmtpAuthSettings(
                String.valueOf(this.getSmtpAuth()));
        BeansFactory.MailSettings().setSecureProtocolSettings(
                String.valueOf(this.getSecureProtocol()));
        BeansFactory.MailSettings().setSmtpPortSettings(this.getPort());
    }
    
    /**
     * Sets smtpAuth
     * 
     * @param smtpAuth
     *            the smtpAuth to set
     */
    public void setSmtpAuth(boolean smtpAuth)
    {
        this.smtpAuth = smtpAuth;
    }
    
    /**
     * Gets smtpAuth
     * 
     * @return smtpAuth the smtpAuth
     */
    public boolean getSmtpAuth()
    {
        return smtpAuth;
    }
    
    /**
     * Sets secureProtocol
     * 
     * @param secureProtocol
     *            the secureProtocol to set
     */
    public void setSecureProtocol(boolean secureProtocol)
    {
        this.secureProtocol = secureProtocol;
    }
    
    /**
     * Gets secureProtocol
     * 
     * @return secureProtocol the secureProtocol
     */
    public boolean getSecureProtocol()
    {
        return secureProtocol;
    }
    
    /**
     * Sets port
     * 
     * @param port
     *            the port to set
     */
    public void setPort(String port)
    {
        this.port = port;
    }
    
    /**
     * Gets port
     * 
     * @return port the port
     */
    public String getPort()
    {
        return port;
    }
    
}
