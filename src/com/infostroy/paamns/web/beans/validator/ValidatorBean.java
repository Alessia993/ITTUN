/**
 * 
 */
package com.infostroy.paamns.web.beans.validator;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.org.w3c.tidy.AttrCheckImpl.CheckLength;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.facades.ControllerUserAnagraphBean;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;
import com.infostroy.paamns.web.beans.PageBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2009.
 *
 */
public final class ValidatorBean extends PageBean
{
    private static String tabMessage;
    
    /**
     * 
     */
    public ValidatorBean()
    {
        setTabs(new HashMap<String, Integer>());
        setStaticTabMessage(null);
    }
    
    public void requiredPage(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        MarkReq(context.getViewRoot().getChildren());
    }
    
    private HashMap<String, Integer> tabs;
    
    private void MarkReq(List<UIComponent> list)
    {
    	FacesContext ctx = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) ctx
				.getExternalContext().getRequest();
		String path = request.getServletPath();
        for (UIComponent component : list)
        {
    		if(path.substring(1).equals(PagesTypes.DURCOMPILATIONEDIT.getPagesContext()) && 
    				component.getAttributes().get("id") != null &&
    				(component.getAttributes().get("id").equals("docBlink") || component.getAttributes().get("id").equals("docClink")) && 
    				component.getAttributes().get("data-id") != null &&
    				component.getAttributes().get("data-id").equals("required"))
    		{
    			ValidatorHelper.markNotVAlid(component,
                        Utils.getString("validatorMessage"),
                        FacesContext.getCurrentInstance(), tabs);
    		}
            if (component.getAttributes().get("required") != null
                    && component.isRendered()
                    && Boolean.parseBoolean(String.valueOf(component
                            .getAttributes().get("required"))) == true)
            {
                if ((component.getAttributes().get("submittedValue") == null || String
                        .valueOf(
                                component.getAttributes().get("submittedValue"))
                        .isEmpty())
                        || (component.getAttributes().get("submittedValue")
                                .getClass().equals(String[].class) && ((String[]) component
                                .getAttributes().get("submittedValue")).length == 0))
                {
                    if (Boolean.parseBoolean(String
                            .valueOf(((UIComponent) component.getAttributes()
                                    .get("parent")).getAttributes().get(
                                    "rendered"))) == true
                            && Boolean.parseBoolean(String.valueOf(component
                                    .getAttributes().get("rendered"))))
                    {
                        ValidatorHelper.markNotVAlid(component,
                                Utils.getString("validatorMessage"),
                                FacesContext.getCurrentInstance(), tabs);
                    }
                }
                else
                {
                    ValidatorHelper.markVAlid(component,
                            FacesContext.getCurrentInstance(), tabs);
                }
            }
            
            MarkReq(component.getChildren());
        }
    }
    
    public void from11To16(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (((String) value).length() != 11 && ((String) value).length() != 16)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.codeLenth", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void length15(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (((String) value).length() != 15)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.cupCodeLength", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void lengthMoreThan16(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (((String) value).length() !=16)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.fiscalLenthItalian", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }  
    }
    
    public void checkExpectedAndReal(FacesContext context,
            UIComponent component, Object value) throws ValidatorException
    {
        this.isFloat92(context, component, value);
        Double val = Double.valueOf(String.valueOf(value));
        Double val2 = Double.valueOf(String.valueOf(this.getSession().get(
                "expectedvalue")));
        if (val > val2)
        {
            FacesMessage tmpMessage = new FacesMessage(
                    Utils.getString("validator.amountReal"));
            this.getViewState().put("financialError", tmpMessage.getSummary());
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkOldPassword(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (this.getSession().get("oldPassword") == null
                || !MD5.encodeString(String.valueOf(value), null).equals(
                        String.valueOf(this.getSession().get("oldPassword"))))
        {
            FacesMessage tmpMessage = new FacesMessage(
                    Utils.getString("validator.oldPasswordError"));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkIsEmail(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String mail = (String) value;
        if (mail != null && !mail.isEmpty())
        {
            boolean isMail = Pattern
                    .matches(
                            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}\\s{0,10}|[0-9]{1,3}\\s{0,10})(\\]?)$",
                            mail);
            
            if (!isMail)
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.mailError", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
    }
    
    public void checkMailUnique(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String mail = (String) value;
        boolean isMail = Pattern
                .matches(
                        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}\\s{0,10}|[0-9]{1,3}\\s{0,10})(\\]?)$",
                        mail);
        if (isMail)
        {
            String userId = null;
            if (this.getSession().get("user") != null)
            {
                userId = String.valueOf(this.getSession().get("user"));
            }
            Long id = 0l;
            if ((userId != null) && (!userId.isEmpty()))
            {
                id = Long.parseLong(userId);
            }
            if (UsersBean.IsMailExists(mail, id))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.uniqueMailError", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        else
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.mailError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    /**
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     */
    public void checkEmpty(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String str = String.valueOf(value);
        if (str != null && !str.isEmpty() && str.trim().isEmpty())
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validatorMessage", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkCUMailUnique(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String mail = (String) value;
        boolean isMail = Pattern
                .matches(
                        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}\\s{0,10}|[0-9]{1,3}\\s{0,10})(\\]?)$",
                        mail);
        if (isMail)
        {
            String userId = null;
            if (this.getSession().get("controllerUserUser") != null)
            {
                userId = String.valueOf(this.getSession().get(
                        "controllerUserUser"));
            }
            Long id = 0l;
            if ((userId != null) && (!userId.isEmpty()))
            {
                id = Long.parseLong(userId);
            }
            if (UsersBean.IsMailExists(mail, id))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.uniqueMailError", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        else
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.mailError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkFiscalCode(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        /*if (((String) value).length() != 11 && ((String) value).length() != 15
                && ((String) value).length() != 16)
       */
        if(((String) value).length() > 20)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.fiscalLenth", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String userId = null;
        if (this.getSession().get("user") != null)
        {
            userId = String.valueOf(this.getSession().get("user"));
        }
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        /*if (UsersBean.IsFiscalCodeExists(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueFiscalCodeError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }*/
    }
    
    public void checkInseeNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (((String) value).length() != 11 && ((String) value).length() != 16)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.fiscalLenth", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String userId = null;
        if (this.getSession().get("controllerUser") != null)
        {
            userId = String.valueOf(this.getSession().get("controllerUser"));
        }
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        if (ControllerUserAnagraphBean.IsInseeNumber(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueInseeNumberError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkTvaNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (((String) value).length() != 11 && ((String) value).length() != 16)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.fiscalLenth", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String userId = null;
        if (this.getSession().get("controllerUser") != null)
        {
            userId = String.valueOf(this.getSession().get("controllerUser"));
        }
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        if (ControllerUserAnagraphBean.IsTvaNumber(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueTvaNumberError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkVatNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (((String) value).length() != 11)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.vat", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String userId = null;
        if (this.getSession().get("controllerUser") != null)
        {
            userId = String.valueOf(this.getSession().get("controllerUser"));
        }
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        if (ControllerUserAnagraphBean.IsVatNumber(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueVatNumberError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkfiscalNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (this.getViewState().get("cilCountry")
                .equals(CountryTypes.Italy.getCountry()))
        {
            if (((String) value).length() != 11
                    && ((String) value).length() != 16)
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.fiscalLenth", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        else if (this.getViewState().get("cilCountry")
                .equals(CountryTypes.France.getCountry()))
        {
            if (((String) value).length() != 15)
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.inseeLenth", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        
        String userId = null;
        if (this.getSession().get("controllerUser") != null)
        {
            userId = String.valueOf(this.getSession().get("controllerUser"));
        }
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        if (ControllerUserAnagraphBean.IsFiscalCode(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueFiscalCodeError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (this.getSession().get("controllerUserUser") != null)
        {
            userId = String
                    .valueOf(this.getSession().get("controllerUserUser"));
        }
        else
        {
            userId = null;
        }
        id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        if (UsersBean.IsFiscalCodeExists(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueFiscalCodeError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkUserName(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        String userId = (String) value;
        if (userId != null && !userId.isEmpty() && userId.trim().isEmpty())
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validatorMessage", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        if (userId != null && (userId.length() < 3 || userId.length() > 32))
        {
            FacesMessage message = Utils.getMessage("Resources",
                    "validator.wrongLengthUsername", null);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, message, context, tabs);
            throw new ValidatorException(message);
        }
        String currUserId = null;
        if (this.getSession().get("user") != null)
        {
            currUserId = String.valueOf(this.getSession().get("user"));
        }
        Long id = 0l;
        if ((currUserId != null) && (!currUserId.isEmpty()))
        {
            id = Long.parseLong(currUserId);
        }
        if (UsersBean.IsLoginExists((String) value, id))
        {
            FacesMessage message = Utils.getMessage("Resources",
                    "validator.loginInUseError", null);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, message, context, tabs);
            throw new ValidatorException(message);
        }
    }
    
    public void checkPasswordEquals(FacesContext context,
            UIComponent component, Object value) throws ValidatorException
    {
        FacesMessage tmpMessage = null;
        String passwordId = (String) component.getAttributes()
                .get("passwordId");
        UIInput passwordInput = (UIInput) context.getViewRoot().findComponent(
                passwordId);
        
        String password = (String) passwordInput.getValue();
        String confirm = (String) value;
        if (password == null)
        {
            tmpMessage = new FacesMessage(Utils.getString("Resources",
                    "validator.confirmPasswordError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            ValidatorHelper.markNotVAlid(
                    context.getViewRoot().findComponent(passwordId),
                    tmpMessage, context, tabs);
            
            throw new ValidatorException(tmpMessage);
        }
        if (!password.equals(confirm))
        {
            tmpMessage = new FacesMessage(Utils.getString("Resources",
                    "validator.confirmPasswordError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            ValidatorHelper.markNotVAlid(
                    context.getViewRoot().findComponent(passwordId),
                    tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        else
        {
            if (!checkPassword(password))
            {
                tmpMessage = new FacesMessage(
                        Utils.getString("validator.shortPasswordMessage"));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                ValidatorHelper.markNotVAlid(context.getViewRoot()
                        .findComponent(passwordId), tmpMessage, context, tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        
    }
    
    public void  checkMailEquals(FacesContext context,
            UIComponent component, Object value) throws ValidatorException
    {
        FacesMessage tmpMessage = null;
        String mailId = (String) component.getAttributes()
                .get("mailId");
        UIInput mailInput = (UIInput) context.getViewRoot().findComponent(
                mailId);
        
        String mail = (String) mailInput.getValue();
        String confirm = (String) value;
        if (mail == null)
        {
            tmpMessage = new FacesMessage(Utils.getString("Resources",
                    "validator.confirmMailError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            ValidatorHelper.markNotVAlid(
                    context.getViewRoot().findComponent(mailId),
                    tmpMessage, context, tabs);
            
            throw new ValidatorException(tmpMessage);
        }
        if (!mail.equals(confirm))
        {
            tmpMessage = new FacesMessage(Utils.getString("Resources",
                    "validator.confirmMailError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            ValidatorHelper.markNotVAlid(
                    context.getViewRoot().findComponent(mailId),
                    tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public boolean checkPassword(String value)
    {
        if (value != null && (value.length() != 0) && (value.length() < 8))
        {
            return false;
        }
        
        boolean digit = false;
        boolean upperCase = false;
        boolean lowerCase = false;
        
        for (char ch : value.toCharArray())
        {
            if (Character.isUpperCase(ch))
            {
                upperCase = true;
            }
            if (Character.isLowerCase(ch))
            {
                lowerCase = true;
            }
            if (Character.isDigit(ch))
            {
                digit = true;
            }
        }
        
        if (!digit || !lowerCase || !upperCase)
        {
            return false;
        }
        return true;
    }
    
    public void isNumber(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (!IsNumber((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.thisFieldShouldBeNumeric", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void isNumber_s(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (!IsNumber(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.thisFieldShouldBeNumeric", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkActProcCode(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if ((String) value != null)
        {
            String code = (String) value;
            
            if (!this.IsAlpanumerical(code))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.alphanumeric", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
            
            String actId = null;
            if (this.getSession().get("actProc") != null)
            {
                actId = String.valueOf(this.getSession().get("actProc"));
            }
            
            Long id = 0l;
            if ((actId != null) && (!actId.isEmpty()))
            {
                id = Long.parseLong(actId);
            }
            
            if (ActivationProcedureAnagraph.IsCodeExists(code, id))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.uniqueCode", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
    }
    
    public void isFloatRequired(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if ((String.valueOf(value)).isEmpty()
                || (String.valueOf(value)) == null)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validatorMessage", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        try
        {
            Double.parseDouble(String.valueOf(value));
        }
        catch(Exception e)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.checkFieldType", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String valueToCheck = String.valueOf(value);
        if (valueToCheck.contains("."))
        {
            if (valueToCheck.indexOf(".") < valueToCheck.length() - 3)
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.checkFieldType", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
    }
    
    public void isFloat(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if ((String.valueOf(value)).isEmpty()
                || (String.valueOf(value)) == null)
        {
            return;
        }
        try
        {
            Double.parseDouble(String.valueOf(value));
        }
        catch(Exception e)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.checkFieldType", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String valueToCheck = String.valueOf(value);
        if (valueToCheck.contains("."))
        {
            if (valueToCheck.indexOf(".") < valueToCheck.length() - 3)
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.checkFieldType", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
    }
    
    public void isFloat92(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if ((String.valueOf(value)).isEmpty()
                || (String.valueOf(value)) == null)
        {
            return;
        }
        String str = String.valueOf(value);
        str = str.replaceAll(",", ".");
        try
        {
            Double.valueOf(str);
        }
        catch(Exception e)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.checkFieldTypeFloat", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (!Pattern.matches("^([1-9][0-9]{0,8}\\.{1}[0-9]{1,2}|"
                + "[1-9][0-9]{0,8}|[0]{1}\\.{1}[0-9]{1,2}|[0]{1})$", str))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.checkFieldTypeFloat", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
    }
    
    public static boolean IsNumber(String value)
    {
        if (value == null || value.isEmpty())
        {
            return true;
        }
        
        char[] valueMas = value.toCharArray();
        for (Character c : valueMas)
        {
            if (!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }
    
    public void checkProjectCodeStart(FacesContext context,
            UIComponent component, Object value) throws ValidatorException
    {
        if ((String) value != null)
        {
            String code = (String) value;
            
            String projectId = null;
            if (this.getSession().get("projectEdit") != null)
            {
                projectId = String
                        .valueOf(this.getSession().get("projectEdit"));
            }
            
            Long id = 0l;
            if ((projectId != null) && (!projectId.isEmpty()))
            {
                id = Long.parseLong(projectId);
            }
            
            if (Projects.IsCodeExists(code, id))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.uniqueCode", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
    }
    
    public void checkFiscalCodeForCfPartner(FacesContext context,
            UIComponent component, Object value) throws ValidatorException,
            NumberFormatException, HibernateException, PersistenceBeanException
    {
        String code = (String) value;
        
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        if (((String) value).length() != 11 && ((String) value).length() != 16)
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.fiscalLenth", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        
        String userId = null;
        if (this.getSession().get("cfPartnerEditSelectedPartner") != null)
        {
            String partnerId = String.valueOf(this.getSession().get(
                    "cfPartnerEditSelectedPartner"));
            if (!partnerId.equals(String.valueOf(SelectItemHelper
                    .getVirtualEntity().getValue())))
            {
                userId = String.valueOf(BeansFactory.CFPartnerAnagraphs()
                        .Load(partnerId).getUser().getId());
            }
        }
        
        Long id = 0l;
        if ((userId != null) && (!userId.isEmpty()))
        {
            id = Long.parseLong(userId);
        }
        
       /* if (UsersBean.IsFiscalCodeExists(code, id))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.uniqueFiscalCodeError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }*/
    }
    
    public void checkMailUniqueCFPartner(FacesContext context,
            UIComponent component, Object value) throws ValidatorException,
            NumberFormatException, HibernateException, PersistenceBeanException
    {
        String mail = (String) value;
        boolean isMail = Pattern
                .matches(
                        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}\\s{0,10}|[0-9]{1,3}\\s{0,10})(\\]?)$",
                        mail);
        if (isMail)
        {
            String userId = null;
            if (this.getSession().get("cfPartnerEditSelectedPartner") != null)
            {
                String partnerId = String.valueOf(this.getSession().get(
                        "cfPartnerEditSelectedPartner"));
                
                if (!partnerId.equals(String.valueOf(SelectItemHelper
                        .getVirtualEntity().getValue())))
                {
                    userId = String.valueOf(BeansFactory
                            .CFPartnerAnagraphs()
                            .Load(String.valueOf(this.getSession().get(
                                    "cfPartnerEditSelectedPartner"))).getUser()
                            .getId());
                }
            }
            
            Long id = 0l;
            if ((userId != null) && (!userId.isEmpty()))
            {
                id = Long.parseLong(userId);
            }
            if (UsersBean.IsMailExists(mail, id))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.uniqueMailError", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
        }
        else
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.mailError", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void isAlphaNumerical(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (!IsAlpanumerical((String) value))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void DurCompilationNumber(FacesContext context,
            UIComponent component, Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        if (!IsNumber(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.numberThreeDigits", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        else
        {
            if (this.getSession().get("durCompilationEdit") != null)
            {
                DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
                        Long.valueOf(String.valueOf(this.getSession().get(
                                "durCompilationEdit"))));
                
                List<Integer> listNumbers = BeansFactory.DurInfos()
                        .LoadNumbers(
                                String.valueOf(di.getId()),
                                Long.parseLong(String.valueOf(this.getSession()
                                        .get("project"))));
                
                if (listNumbers
                        .contains(Integer.valueOf(String.valueOf(value))))
                {
                    FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                            "Resources", "validator.numberInUse", null));
                    tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ValidatorHelper.markNotVAlid(component, tmpMessage,
                            context, tabs);
                    throw new ValidatorException(tmpMessage);
                }
            }
            else
            {
                List<Integer> listNumbers = BeansFactory.DurInfos()
                        .LoadNumbers(
                                null,
                                Long.parseLong(String.valueOf(this.getSession()
                                        .get("project"))));
                
                if (listNumbers
                        .contains(Integer.valueOf(String.valueOf(value))))
                {
                    FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                            "Resources", "validator.numberInUse", null));
                    tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ValidatorHelper.markNotVAlid(component, tmpMessage,
                            context, tabs);
                    throw new ValidatorException(tmpMessage);
                }
            }
        }
    }
    
    public void DurCompilationProtocolNumber(FacesContext context,
            UIComponent component, Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        if (!IsDURProtocolNumber(String.valueOf(value)))
        {
            
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "durProtocolNumber", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
            
        }
        else
        {
            if (this.getSession().get("durCompilationEdit") != null)
            {
                DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
                        Long.valueOf(String.valueOf(this.getSession().get(
                                "durCompilationEdit"))));
                
                List<String> listNumbers = BeansFactory.DurInfos()
                        .LoadProtocolNumbers(String.valueOf(di.getId()));
                
                if (listNumbers.contains(String.valueOf(value))
                        && !value.equals(""))
                {
                    FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                            "Resources", "validator.numberInUse", null));
                    tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ValidatorHelper.markNotVAlid(component, tmpMessage,
                            context, tabs);
                    throw new ValidatorException(tmpMessage);
                }
            }
            else
            {
                List<String> listNumbers = BeansFactory.DurInfos()
                        .LoadProtocolNumbers(null);
                
                if (listNumbers.contains(String.valueOf(value))
                        && !value.equals(""))
                {
                    FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                            "Resources", "validator.numberInUse", null));
                    tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ValidatorHelper.markNotVAlid(component, tmpMessage,
                            context, tabs);
                    throw new ValidatorException(tmpMessage);
                }
            }
        }
    }
    
    public void DurReimbursementNumber(FacesContext context,
            UIComponent component, Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        int number = Integer.parseInt(String.valueOf(value));
        if (!IsNumber(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.numberThreeDigits", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        else
        {
            DurInfos durInfo = BeansFactory.DurInfos().LoadByDurCompilation(
                    Long.parseLong(String.valueOf(this.getSession().get(
                            "durReimbEdit"))));
            if (number != durInfo.getDurInfoNumber())
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.sameAsInDur", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
            /*List<Integer> listNumbers = null;
            
            if (this.getSession().get("fesrinfo") != null)
            {
                listNumbers = BeansFactory.DurReimbursements()
                        .LoadReimbursementNumbers(
                                String.valueOf(this.getSession()
                                        .get("fesrinfo")));
            }
            else
            {
                listNumbers = BeansFactory.DurReimbursements()
                        .LoadReimbursementNumbers(null);
            }
            
            if (listNumbers.contains(Integer.valueOf(String.valueOf(value))))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.numberInUse", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }*/
            
        }
    }
    
    public void DurRotationNumber(FacesContext context, UIComponent component,
            Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        int number = Integer.parseInt(String.valueOf(value));
        if (Integer.parseInt(String.valueOf(value)) != 0)
        {
            if (!IsNumber(String.valueOf(value)))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.numberThreeDigits", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
            else
            {
                DurInfos durInfo = BeansFactory.DurInfos()
                        .LoadByDurCompilation(
                                Long.parseLong(String.valueOf(this.getSession()
                                        .get("durReimbEdit"))));
                if (number != durInfo.getDurInfoNumber())
                {
                    FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                            "Resources", "validator.sameAsInDur", null));
                    tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ValidatorHelper.markNotVAlid(component, tmpMessage,
                            context, tabs);
                    throw new ValidatorException(tmpMessage);
                }
                
                /*   
                   List<Integer> listNumbers = null;
                   
                   if (this.getSession().get("rotationfoundinfo") != null)
                   {
                       listNumbers = BeansFactory.DurReimbursements()
                               .LoadRotationReimbursementNumbers(
                                       String.valueOf(this.getSession().get(
                                               "rotationfoundinfo")));
                   }
                   else
                   {
                       listNumbers = BeansFactory.DurReimbursements()
                               .LoadRotationReimbursementNumbers(null);
                   }
                   
                   if (listNumbers
                           .contains(Integer.valueOf(String.valueOf(value))))
                   {
                       FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                               "Resources", "validator.numberInUse", null));
                       tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                       ValidatorHelper.markNotVAlid(component, tmpMessage,
                               context, tabs);
                       throw new ValidatorException(tmpMessage);
                   }
                   */
                
            }
        }
    }
    
    public void DurFesrCRONumber(FacesContext context, UIComponent component,
            Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        if (!IsNumber(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "valiator.numberElevenDigits", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        else
        {
            
            List<String> listNumbers = null;
            
            if (this.getSession().get("fesrinfo") != null)
            {
                listNumbers = BeansFactory.DurReimbursements().LoadFesrCROs(
                        String.valueOf(this.getSession().get("fesrinfo")));
            }
            else
            {
                listNumbers = BeansFactory.DurReimbursements().LoadFesrCROs(
                        null);
            }
            
            if (listNumbers.contains(String.valueOf(value)))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.numberInUse", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
            
        }
    }
    
    public void DurRotationCRONumber(FacesContext context,
            UIComponent component, Object value) throws NumberFormatException,
            PersistenceBeanException
    {
        if (!IsNumber(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "valiator.numberElevenDigits", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
        else
        {
            
            List<String> listNumbers = null;
            
            if (this.getSession().get("rotationfoundinfo") != null)
            {
                listNumbers = BeansFactory.DurReimbursements()
                        .LoadRotationCROs(
                                String.valueOf(this.getSession().get(
                                        "rotationfoundinfo")));
            }
            else
            {
                listNumbers = BeansFactory.DurReimbursements()
                        .LoadRotationCROs(null);
            }
            
            if (listNumbers.contains(String.valueOf(value)))
            {
                FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                        "Resources", "validator.numberInUse", null));
                tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                ValidatorHelper.markNotVAlid(component, tmpMessage, context,
                        tabs);
                throw new ValidatorException(tmpMessage);
            }
            
        }
    }
    
    public void CheckIBAN(FacesContext context, UIComponent component,
            Object value) throws ValidatorException
    {
        if (!IsAlpanumerical(String.valueOf(value)))
        {
            FacesMessage tmpMessage = new FacesMessage(Utils.getString(
                    "Resources", "validator.alphanumerical", null));
            tmpMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            ValidatorHelper.markNotVAlid(component, tmpMessage, context, tabs);
            throw new ValidatorException(tmpMessage);
        }
    }
    
    public void checkFiscalLength16(FacesContext context, UIComponent component, 
    		Object value)throws ValidatorException
    {
    	checkFiscalCode(context, component, value);
    	lengthMoreThan16(context, component, value);
    }
            
            
            
    
    private boolean IsAlpanumerical(String value)
    {
        for (Character c : value.toCharArray())
        {
            if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c))
            {
                return false;
            }
        }
        return true;
    }
    
    private boolean IsDURProtocolNumber(String value)
    {
        for (Character c : value.toCharArray())
        {
            if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c)
                    && !c.equals('/'))
            {
                return false;
            }
        }
        
        return true;
    }
    
    public void setTabMessage(String tabMessage)
    {
        ValidatorBean.tabMessage = tabMessage;
    }
    
    public String getTabMessage()
    {
        return tabMessage;
    }
    
    public static void setStaticTabMessage(String tabMessage)
    {
        ValidatorBean.tabMessage = tabMessage;
    }
    
    /**
     * Sets tabs
     * @param tabs the tabs to set
     */
    public void setTabs(HashMap<String, Integer> tabs)
    {
        this.tabs = tabs;
    }
    
    /**
     * Gets tabs
     * @return tabs the tabs
     */
    public HashMap<String, Integer> getTabs()
    {
        return tabs;
    }
    
}
