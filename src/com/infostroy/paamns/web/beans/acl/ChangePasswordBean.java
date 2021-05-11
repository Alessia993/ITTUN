/**
 * 
 */
package com.infostroy.paamns.web.beans.acl;

import java.util.Date;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ChangePasswordBean extends EntityEditBean<Users>
{
    private Boolean isLoggedIn;
    
    private String  pwd;
    
    private String  password;
    
    private String  confirmPwd;
    
    private String  exMessage;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditController#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        this.GoBack();
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditController#GoBack()
     */
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.INDEX);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditController#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (getCurrentUser() == null)
        {
            setIsLoggedIn(false);
        }
        else
        {
            setIsLoggedIn(true);
            if (this.getCurrentUser().getShouldChangePassword())
            {
                this.setExMessage(Utils.getString("passwordChangeNeed"));
            }
            else if (this.getCurrentUser().getPasswordExpired())
            {
                this.setExMessage(Utils.getString("passwordExpired"));
            }
        }
        
        this.getSession().put("oldPassword",
                this.getCurrentUser().getPassword());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.infostroy.paamns.web.beans.EntityEditController#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditController#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException
    {
        this.getCurrentUser().setPassword(MD5.encodeString(getPwd(), null));
        this.getCurrentUser().setPassword2(MD5.encodeString(this.getCurrentUser().getPassword(), null));
        this.getCurrentUser().setLastPasswordChange(new Date());
        this.getCurrentUser().setShouldChangePassword(false);
        BeansFactory.Users().SaveInTransaction(this.getCurrentUser());
    }
    
    /**
     * Sets pwd
     * 
     * @param pwd
     *            the pwd to set
     */
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
    
    /**
     * Gets pwd
     * 
     * @return pwd the pwd
     */
    public String getPwd()
    {
        return pwd;
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
    
    /**
     * Sets confirmPwd
     * 
     * @param confirmPwd
     *            the confirmPwd to set
     */
    public void setConfirmPwd(String confirmPwd)
    {
        this.confirmPwd = confirmPwd;
    }
    
    /**
     * Gets confirmPwd
     * 
     * @return confirmPwd the confirmPwd
     */
    public String getConfirmPwd()
    {
        return confirmPwd;
    }
    
    /**
     * Sets isLoggedIn
     * 
     * @param isLoggedIn
     *            the isLoggedIn to set
     */
    public void setIsLoggedIn(Boolean isLoggedIn)
    {
        this.isLoggedIn = isLoggedIn;
    }
    
    /**
     * Gets isLoggedIn
     * 
     * @return isLoggedIn the isLoggedIn
     */
    public Boolean getIsLoggedIn()
    {
        return isLoggedIn;
    }
    
    /**
     * Sets exMessage
     * 
     * @param exMessage
     *            the exMessage to set
     */
    public void setExMessage(String exMessage)
    {
        this.exMessage = exMessage;
    }
    
    /**
     * Gets exMessage
     * 
     * @return exMessage the exMessage
     */
    public String getExMessage()
    {
        return exMessage;
    }
    
}
