/**
 * 
 */
package com.infostroy.paamns.web.beans.common;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.web.beans.LocalizeBean;
import com.infostroy.paamns.web.beans.PageBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class IndexPageBean extends PageBean
{
    private String val;
    
    private final String STR_BEAN_NAME_LOCALIZE = "#{LocalizeBean}";
    
    public IndexPageBean()
    {
        this.PageLoad();
    }
    
    private void PageLoad()
    {
        val = "";
		if (this.getCurrentUser() != null)
		{

			if (this.getCurrentUser() != null
					&& this.getCurrentUser().getCountry() != null)
			{
				if (this.getCurrentUser().getCountry().getId() == 1)
				{
					getLocalizeBean().doSetItalianLocal();
				}
				else if (this.getCurrentUser().getCountry().getId() == 2)
				{
					getLocalizeBean().doSetFrenchLocal();
				}
			}
		}

        
        
        if (this.getCurrentUser() != null && (this.getCurrentUser().getShouldChangePassword()
                || this.getCurrentUser().getPasswordExpired()))
        {
            this.goTo(PagesTypes.CHANGEPASSWORD);
            return;
        }
        
        if (AccessGrantHelper.getInActualState()
                && !SessionManager.getInstance().getSessionBean().isAPU())
        {
            this.goTo(PagesTypes.PROJECTLIST);
        }
        else if (!AccessGrantHelper.getInActualState()
                && !SessionManager.getInstance().getSessionBean().isAPU())
        {
            this.goTo(PagesTypes.IDEVTIFICATIONDATAVIEW);
        }
        else if (SessionManager.getInstance().getSessionBean().isAPU())
        {
            this.goTo(PagesTypes.USERLIST);
        }
    }
    
    /**
     * Sets val
     * 
     * @param val
     *            the val to set
     */
    public void setVal(String val)
    {
        this.val = val;
    }
    
    /**
     * Gets val
     * 
     * @return val the val
     */
    public String getVal()
    {
        return val;
    }
    
    private LocalizeBean getLocalizeBean(){
        FacesContext fCtx = FacesContext.getCurrentInstance();
        ELContext elCtx = fCtx.getELContext();
        ExpressionFactory ef = fCtx.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elCtx, STR_BEAN_NAME_LOCALIZE,
        		LocalizeBean.class);
        LocalizeBean local = (LocalizeBean) ve.getValue(elCtx);
        return local;
    }
}
