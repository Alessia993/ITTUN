/**
 * 
 */
package com.infostroy.paamns.persistence;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import com.infostroy.paamns.web.beans.session.SessionBean;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */
public class SessionManager
{
    /**
     * 
     */
    
    private static SessionManager instance      = new SessionManager();
    
    private final static String   STR_BEAN_NAME = "#{Session}";
    
    private SessionManager()
    {
    }
    
    public static SessionManager getInstance()
    {
        return SessionManager.instance;
    }
    
    public SessionBean getSessionBean()
    {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        ELContext elCtx = fCtx.getELContext();
        ExpressionFactory ef = fCtx.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elCtx, STR_BEAN_NAME,
                SessionBean.class);
        SessionBean session = (SessionBean) ve.getValue(elCtx);
        return session;
    }
    
}
