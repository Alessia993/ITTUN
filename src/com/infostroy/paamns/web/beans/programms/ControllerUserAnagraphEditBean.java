/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.ControllerTypeTypes;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.NewUserMailSender;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ControllerTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;
import com.infostroy.paamns.web.beans.EntityEditBean;

enum ControllerUserRoles
{
    DAEC("daec"), 
    VALIDATOR("validator"), 
    CIL("cil");
    
    private ControllerUserRoles(String value)
    {
        this.setRole(value);
    }
    
    /**
     * Sets role
     * @param role the role to set
     */
    public void setRole(String role)
    {
        this.role = role;
    }
    
    /**
     * Gets role
     * @return role the role
     */
    public String getRole()
    {
        return role;
    }
    
    private String role;
}

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ControllerUserAnagraphEditBean extends
        EntityEditBean<ControllerUserAnagraph>
{
    private String           countryToShow;
    
    private String           role;
    
    private List<SelectItem> rolesList;
    
    private String           country;
    
    private List<SelectItem> countryList;
    
    private List<SelectItem> countryAllList;
    
    private String           nation;
    
    private String           controllerType;
    
    private List<SelectItem> controllerTypeList;
    
    private String           fiscalCode;
    
    private String           name;
    
    private String           surname;
    
    private Date             birthDate;
    
    private String           placeOfLive;
    
    private String           address;
    
    private String           telephoneNumber;
    
    private String           faxNumber;
    
    private String           email;
    
    private String           vatNumber;
    
    private String           denomination;
    
    private String           giuridicalType;
    
    private String           lawAddress;
    
    private String           legalPhone;
    
    private String           legalFax;
    
    private Integer          tabIndex;
    
    private boolean          showPhisical;
    
    private boolean          showGiuridical;
    
    private boolean          showCountryList;
    
    private Roles            roleForSave;
    
    private Countries        countryForSave;
    
    private List<InfoObject> listMails;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        //new NewUserMailSender(listMails).start();
        Transaction tr = null;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            List<Mails> mails = new ArrayList<Mails>();
            NewUserMailSender newUserMailSender = new NewUserMailSender(
                    listMails);
            
            mails.addAll(newUserMailSender
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
        this.GoBack();
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        goTo(PagesTypes.CONTROLLERUSERANAGRAPHLIST);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceException, PersistenceBeanException
    {
        this.FillDropDowns();
        
        if (this.getSession().get("controllerUser") != null)
        {
            ControllerUserAnagraph item = BeansFactory.ControllerUserAnagraph()
                    .Load(String.valueOf(this.getSession()
                            .get("controllerUser")));
            this.setAddress(item.getAddress());
            this.setBirthDate(item.getBirthDay());
            
            if (this.getViewState().get("cilControllerType") == null)
            {
                this.setControllerType(String.valueOf(item.getControllerType()
                        .getCode()));
                this.getViewState().put("cilControllerType",
                        this.getControllerType());
            }
            
            if (this.getViewState().get("cilCountry") == null)
            {
                this.setCountry(String.valueOf(item.getCountry().getCode()));
                this.getViewState().put("cilCountry", this.getCountry());
            }
            this.setDenomination(item.getDenomination());
            Users user = item.getUser();
            if (user != null)
            {
                this.getSession().put("controllerUserUser", user.getId());
                this.setSurname(user.getSurname());
                this.setName(user.getName());
                this.setFiscalCode(user.getFiscalCode());
                this.setEmail(user.getMail());
            }
            
            this.setFaxNumber(item.getFax());
            this.setGiuridicalType(item.getGiuridicalType());
            this.setLawAddress(item.getLawAddress());
            this.setLegalFax(item.getLegalFax());
            this.setLegalPhone(item.getLegalPhone());
            
            if (item.getNation() != null)
            {
                this.setNation(String.valueOf(item.getNation().getCode()));
            }
            this.setPlaceOfLive(item.getPlaceOfLive());
            this.setTelephoneNumber(item.getPhone());
            this.setVatNumber(item.getVatNumber());
            
            if (user != null && user.getRoles() != null
                    && this.getViewState().get("ControllerUserRole") == null)
            {
                if (isCil(user.getRoles()))
                {
                    this.setRole(ControllerUserRoles.CIL.getRole());
                    this.getViewState().put("ControllerUserRole",
                            this.getRole());
                    
                    this.setShowCountryList(true);
                }
                else if (isDaec(user.getRoles()))
                {
                    if (String.valueOf(this.getViewState().get("cilCountry"))
                            .equals(CountryTypes.France.getCountry()))
                    {
                        this.setRole(ControllerUserRoles.DAEC.getRole());
                        this.getViewState().put("ControllerUserRole",
                                this.getRole());
                    }
                    else if (String.valueOf(
                            this.getViewState().get("cilCountry")).equals(
                            CountryTypes.Italy.getCountry()))
                    {
                        this.setRole(ControllerUserRoles.VALIDATOR.getRole());
                        this.getViewState().put("ControllerUserRole",
                                this.getRole());
                    }
                    this.setShowCountryList(false);
                    
                }
            }
        }
        else
        {
            this.getSession().put("controllerUserUser", null);
        }
        
        this.reRenderCilForm();
    }
    
    private void FillDropDowns() throws PersistenceBeanException
    {
        if (getCountryList() == null)
        {
            setCountryList(new ArrayList<SelectItem>());
        }
        else
        {
            getCountryList().clear();
        }
        List<Countries> listContries = BeansFactory.Countries().GetSelectable();
        getCountryList().add(SelectItemHelper.getFirst());
        for (Countries item : listContries)
        {
            getCountryList().add(
                    new SelectItem(String.valueOf(item.getCode()), item
                            .getValue()));
        }
        
        if (getCountryAllList() == null)
        {
            setCountryAllList(new ArrayList<SelectItem>());
        }
        else
        {
            getCountryAllList().clear();
        }
        setCountryAllList(new ArrayList<SelectItem>());
        List<Countries> listAllContries = BeansFactory.Countries().Load();
        getCountryAllList().add(SelectItemHelper.getFirst());
        for (Countries item : listAllContries)
        {
            getCountryAllList().add(
                    new SelectItem(String.valueOf(item.getCode()), item
                            .getValue()));
        }
        
        if (getControllerTypeList() == null)
        {
            setControllerTypeList(new ArrayList<SelectItem>());
        }
        else
        {
            getControllerTypeList().clear();
        }
        setControllerTypeList(new ArrayList<SelectItem>());
        List<ControllerTypes> listcCntrollerTypes = BeansFactory
                .ControllerTypes().Load();
        getControllerTypeList().add(SelectItemHelper.getFirst());
        for (ControllerTypes item : listcCntrollerTypes)
        {
            getControllerTypeList().add(
                    new SelectItem(String.valueOf(item.getCode()), item
                            .getValue()));
        }
        
        if (getRolesList() == null)
        {
            setRolesList(new ArrayList<SelectItem>());
        }
        else
        {
            getRolesList().clear();
        }
        setRolesList(new ArrayList<SelectItem>());
        getRolesList().add(SelectItemHelper.getFirst());
      //getRolesList().add(
      //       new SelectItem(ControllerUserRoles.DAEC.getRole(), Utils
      //             .getString("Resources",
      //                   ControllerUserRoles.DAEC.getRole(), null)));
        getRolesList().add(
                new SelectItem(ControllerUserRoles.CIL.getRole(), Utils
                        .getString("Resources",
                                ControllerUserRoles.CIL.getRole(), null)));
        /*rolesList.add(new SelectItem(ControllerUserRoles.VALIDATOR.getRole(),
                Utils.getString("Resources", ControllerUserRoles.VALIDATOR
                        .getRole(), null)));*/
    }
    
    public static boolean isCil(List<UserRoles> list)
    {
        for (UserRoles role : list)
        {
            if (role.getRole().getCode().equals(UserRoleTypes.CIL.getValue()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isDaec(List<UserRoles> list)
    {
        for (UserRoles role : list)
        {
            if (role.getRole().getCode().equals(UserRoleTypes.DAEC.getValue()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceException,
            PersistenceBeanException
    {
        
    }
    
    /**
     * @param event
     */
    public void countryChange(ValueChangeEvent event)
    {
        this.setCountry(String.valueOf(event.getNewValue()));
        this.getViewState().put("cilCountry", this.getCountry());
        this.reRenderCilForm();
    }
    
    /**
     * @param event
     */
    public void cilControllerTypeChange(ValueChangeEvent event)
    {
        this.setControllerType(String.valueOf(event.getNewValue()));
        this.getViewState().put("cilControllerType", this.getControllerType());
        this.reRenderCilForm();
    }
    
    public void roleChange(ValueChangeEvent event)
    {
        if (event.getNewValue() != null
                && event.getNewValue()
                        .equals(ControllerUserRoles.CIL.getRole()))
        {
            this.getViewState().put("ControllerUserRole", event.getNewValue());
            showCountryList = true;
            reRenderCilForm();
        }
        else if (event.getNewValue() != null
                && event.getNewValue().equals(
                        ControllerUserRoles.DAEC.getRole()))
        {
            this.getViewState().put("ControllerUserRole", event.getNewValue());
            showCountryList = false;
            this.getViewState().put("cilCountry",
                    CountryTypes.France.getCountry());
            reRenderCilForm();
        }
        else if (event.getNewValue() != null
                && event.getNewValue().equals(
                        ControllerUserRoles.VALIDATOR.getRole()))
        {
            this.getViewState().put("ControllerUserRole", event.getNewValue());
            showCountryList = false;
            this.getViewState().put("cilCountry",
                    CountryTypes.Italy.getCountry());
            reRenderCilForm();
        }
        else if (event.getNewValue() == null)
        {
            this.showCountryList = true;
            this.showPhisical = false;
            this.showGiuridical = false;
        }
    }
    
    /**
     * 
     */
    public void reRenderCilForm()
    {
        if (this.getViewState().get("ControllerUserRole") != null)
        {
            if (this.getViewState().get("ControllerUserRole")
                    .equals(ControllerUserRoles.CIL.getRole()))
            {
                showCountryList = true;
            }
            else if (this.getViewState().get("ControllerUserRole")
                    .equals(ControllerUserRoles.DAEC.getRole()))
            {
                showCountryList = false;
                this.setCountryToShow(Utils.getString("Resources", "france",
                        null));
                this.getViewState().put("cilCountry",
                        CountryTypes.France.getCountry());
            }
            else if (this.getViewState().get("ControllerUserRole")
                    .equals(ControllerUserRoles.VALIDATOR.getRole()))
            {
                showCountryList = false;
                this.setCountryToShow(Utils.getString("Resources", "italy",
                        null));
                this.getViewState().put("cilCountry",
                        CountryTypes.Italy.getCountry());
            }
            
            if (this.getViewState().get("cilControllerType") != null
                    && this.getViewState().get("cilCountry") != null)
            {
                this.showPhisical = this
                        .getViewState()
                        .get("cilControllerType")
                        .equals(ControllerTypeTypes.Phisical
                                .getControllerType());
                this.showGiuridical = this
                        .getViewState()
                        .get("cilControllerType")
                        .equals(ControllerTypeTypes.Juridical
                                .getControllerType());
            }
            else
            {
                this.showPhisical = false;
                this.showGiuridical = false;
            }
        }
        else
        {
            showCountryList = true;
            this.showPhisical = false;
            this.showGiuridical = false;
        }
    }
    
    /**
     * @param item
     * @param country
     * @throws NumberFormatException
     * @throws HibernateException
     * @throws PersistenceException
     */
    public void save(ControllerUserAnagraph item) throws NumberFormatException,
            HibernateException, PersistenceException
    {
        item.setAddress(this.getAddress());
        item.setBirthDay(this.getBirthDate());
        if (this.getControllerType() != null)
        {
            try
            {
                item.setControllerType(BeansFactory.ControllerTypes()
                        .GetControllerType(this.getControllerType()));
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        if (this.getCountry() != null)
        {
            try
            {
                item.setCountry(BeansFactory.Countries().GetCountry(
                        this.getCountry()));
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        else
        {
            try
            {
                if (this.getViewState().get("ControllerUserRole")
                        .equals(ControllerUserRoles.VALIDATOR.getRole()))
                {
                    item.setCountry(BeansFactory.Countries().GetCountry(
                            CountryTypes.Italy));
                }
                else if (this.getViewState().get("ControllerUserRole")
                        .equals(ControllerUserRoles.DAEC.getRole()))
                {
                    item.setCountry(BeansFactory.Countries().GetCountry(
                            CountryTypes.France));
                }
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        
        item.setDenomination(this.getDenomination());
        item.setFax(this.getFaxNumber());
        item.setFiscalCode(this.getFiscalCode());
        item.setGiuridicalType(this.getGiuridicalType());
        item.setLawAddress(this.getLawAddress());
        item.setLegalFax(this.getLegalFax());
        item.setLegalPhone(this.getLegalPhone());
        if (this.getNation() != null)
        {
            try
            {
                item.setNation(BeansFactory.Countries().GetCountry(
                        this.getNation()));
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        item.setPhone(this.getTelephoneNumber());
        item.setPlaceOfLive(this.getPlaceOfLive());
        item.setVatNumber(this.getVatNumber());
    }
    
    public void save(Users item) throws NumberFormatException,
            HibernateException, PersistenceException, PersistenceBeanException
    {
        item.setAdmin(false);
        item.setActive(false);
        item.setMail(this.getEmail());
        item.setName(this.getName());
        item.setSurname(this.getSurname());
        item.setFiscalCode(this.getFiscalCode());
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(".");
        sb.append(this.getSurname());
        if (this.getSession().get("controllerUser") == null)
        {
            if (UsersBean.IsLoginExists(sb.toString(), null))
            {
                sb.append(".");
                sb.append(UUID.randomUUID().toString().substring(0, 4));
                item.setLogin(sb.toString().length() > 32 ? sb.toString()
                        .substring(0, 31) : sb.toString());
            }
            else
            {
                item.setLogin(sb.toString().length() > 32 ? sb.toString()
                        .substring(0, 31) : sb.toString());
            }
            item.setShouldChangePassword(true);
        }
        
        if (this.getSession().get("controllerUser") == null
                && (item.getPassword() == null || item.getPassword().isEmpty()))
        {
            String pwd = UUID.randomUUID().toString().substring(0, 8);
            item.setPassword(MD5.encodeString(pwd, null));
            item.setPassword2(MD5.encodeString(item.getPassword(), null));
            BeansFactory.Users().SaveInTransaction(item);
            
            InfoObject info = new InfoObject();
            info.setMail(item.getMail());
            info.setName(item.getName());
            info.setSurname(item.getSurname());
            info.setPassword(pwd);
            info.setLogin(item.getLogin());
            listMails.add(info);
        }
        else
        {
            BeansFactory.Users().SaveInTransaction(item);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException, PersistenceException,
            PersistenceBeanException
    {
        listMails = new ArrayList<InfoObject>();
        Roles role = null;
        if (getRole().equals(ControllerUserRoles.CIL.getRole()))
        {
            role = BeansFactory.Roles().GetRoleByName(UserRoleTypes.CIL);
        }
        else if (getRole().equals(ControllerUserRoles.DAEC.getRole()))
        {
            role = BeansFactory.Roles().GetRoleByName(UserRoleTypes.DAEC);
        }
        else if (getRole().equals(ControllerUserRoles.VALIDATOR.getRole()))
        {
            role = BeansFactory.Roles().GetRoleByName(UserRoleTypes.DAEC);
        }
        
        ControllerUserAnagraph item = new ControllerUserAnagraph();
        Users user = new Users();
        if (this.getSession().get("controllerUser") != null)
        {
            item = BeansFactory.ControllerUserAnagraph().Load(
                    String.valueOf(this.getSession().get("controllerUser")));
            user = item.getUser();
        }
        this.save(item);
        this.save(user);
        
        if (this.getSession().get("controllerUser") == null)
        {
            BeansFactory.UserRoles().SaveInTransaction(
                    new UserRoles(user, role, true));
        }
        item.setUser(user);
        BeansFactory.ControllerUserAnagraph().SaveInTransaction(item);
        
    }
    
    /**
     * Sets country
     * @param country the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    /**
     * Gets country
     * @return country the country
     */
    public String getCountry()
    {
        return country;
    }
    
    /**
     * Sets countryList
     * @param countryList the countryList to set
     */
    public void setCountryList(List<SelectItem> countryList)
    {
        this.getViewState().put("countryList", countryList);
        this.countryList = countryList;
    }
    
    /**
     * Gets countryList
     * @return countryList the countryList
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountryList()
    {
        this.countryList = (List<SelectItem>) this.getViewState().get(
                "countryList");
        return this.countryList;
    }
    
    /**
     * Sets controllerType
     * @param controllerType the controllerType to set
     */
    public void setControllerType(String controllerType)
    {
        this.controllerType = controllerType;
    }
    
    /**
     * Gets controllerType
     * @return controllerType the controllerType
     */
    public String getControllerType()
    {
        return controllerType;
    }
    
    /**
     * Sets controllerTypeList
     * @param controllerTypeList the controllerTypeList to set
     */
    public void setControllerTypeList(List<SelectItem> controllerTypeList)
    {
        this.getViewState().put("controllerTypeList", controllerTypeList);
        this.controllerTypeList = controllerTypeList;
    }
    
    /**
     * Gets controllerTypeList
     * @return controllerTypeList the controllerTypeList
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getControllerTypeList()
    {
        this.controllerTypeList = (List<SelectItem>) this.getViewState().get(
                "controllerTypeList");
        return this.controllerTypeList;
    }
    
    /**
     * Sets fiscalCode
     * @param fiscalCode the fiscalCode to set
     */
    public void setFiscalCode(String fiscalCode)
    {
        this.fiscalCode = fiscalCode;
    }
    
    /**
     * Gets fiscalCode
     * @return fiscalCode the fiscalCode
     */
    public String getFiscalCode()
    {
        return fiscalCode;
    }
    
    /**
     * Sets name
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets name
     * @return name the name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets surname
     * @param surname the surname to set
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    
    /**
     * Gets surname
     * @return surname the surname
     */
    public String getSurname()
    {
        return surname;
    }
    
    /**
     * Sets birthDate
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }
    
    /**
     * Gets birthDate
     * @return birthDate the birthDate
     */
    public Date getBirthDate()
    {
        return birthDate;
    }
    
    /**
     * Sets placeOfLive
     * @param placeOfLive the placeOfLive to set
     */
    public void setPlaceOfLive(String placeOfLive)
    {
        this.placeOfLive = placeOfLive;
    }
    
    /**
     * Gets placeOfLive
     * @return placeOfLive the placeOfLive
     */
    public String getPlaceOfLive()
    {
        return placeOfLive;
    }
    
    /**
     * Sets address
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    /**
     * Gets address
     * @return address the address
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * Sets telephoneNumber
     * @param telephoneNumber the telephoneNumber to set
     */
    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }
    
    /**
     * Gets telephoneNumber
     * @return telephoneNumber the telephoneNumber
     */
    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }
    
    /**
     * Sets email
     * @param email the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    /**
     * Gets email
     * @return email the email
     */
    public String getEmail()
    {
        return email;
    }
    
    /**
     * Sets faxNumber
     * @param faxNumber the faxNumber to set
     */
    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }
    
    /**
     * Gets faxNumber
     * @return faxNumber the faxNumber
     */
    public String getFaxNumber()
    {
        return faxNumber;
    }
    
    /**
     * Sets vatNumber
     * @param vatNumber the vatNumber to set
     */
    public void setVatNumber(String vatNumber)
    {
        this.vatNumber = vatNumber;
    }
    
    /**
     * Gets vatNumber
     * @return vatNumber the vatNumber
     */
    public String getVatNumber()
    {
        return vatNumber;
    }
    
    /**
     * Sets denomination
     * @param denomination the denomination to set
     */
    public void setDenomination(String denomination)
    {
        this.denomination = denomination;
    }
    
    /**
     * Gets denomination
     * @return denomination the denomination
     */
    public String getDenomination()
    {
        return denomination;
    }
    
    /**
     * Sets giuridicalType
     * @param giuridicalType the giuridicalType to set
     */
    public void setGiuridicalType(String giuridicalType)
    {
        this.giuridicalType = giuridicalType;
    }
    
    /**
     * Gets giuridicalType
     * @return giuridicalType the giuridicalType
     */
    public String getGiuridicalType()
    {
        return giuridicalType;
    }
    
    /**
     * Sets lawAddress
     * @param lawAddress the lawAddress to set
     */
    public void setLawAddress(String lawAddress)
    {
        this.lawAddress = lawAddress;
    }
    
    /**
     * Gets lawAddress
     * @return lawAddress the lawAddress
     */
    public String getLawAddress()
    {
        return lawAddress;
    }
    
    /**
     * Sets legalPhone
     * @param legalPhone the legalPhone to set
     */
    public void setLegalPhone(String legalPhone)
    {
        this.legalPhone = legalPhone;
    }
    
    /**
     * Gets legalPhone
     * @return legalPhone the legalPhone
     */
    public String getLegalPhone()
    {
        return legalPhone;
    }
    
    /**
     * Sets legalFax
     * @param legalFax the legalFax to set
     */
    public void setLegalFax(String legalFax)
    {
        this.legalFax = legalFax;
    }
    
    /**
     * Gets legalFax
     * @return legalFax the legalFax
     */
    public String getLegalFax()
    {
        return legalFax;
    }
    
    /**
     * Sets countryAllList
     * @param countryAllList the countryAllList to set
     */
    public void setCountryAllList(List<SelectItem> countryAllList)
    {
        this.getViewState().put("countryAllList", countryAllList);
        this.countryAllList = countryAllList;
    }
    
    /**
     * Gets countryAllList
     * @return countryAllList the countryAllList
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountryAllList()
    {
        this.countryAllList = (List<SelectItem>) this.getViewState().get(
                "countryAllList");
        return countryAllList;
    }
    
    /**
     * Sets tabIndex
     * @param tabIndex the tabIndex to set
     */
    public void setTabIndex(Integer tabIndex)
    {
        this.tabIndex = tabIndex;
    }
    
    /**
     * Gets tabIndex
     * @return tabIndex the tabIndex
     */
    public Integer getTabIndex()
    {
        return tabIndex;
    }
    
    /**
     * Sets nation
     * @param nation the nation to set
     */
    public void setNation(String nation)
    {
        this.nation = nation;
    }
    
    /**
     * Gets nation
     * @return nation the nation
     */
    public String getNation()
    {
        return nation;
    }
    
    /**
     * Sets rolesList
     * @param rolesList the rolesList to set
     */
    public void setRolesList(List<SelectItem> rolesList)
    {
        this.rolesList = rolesList;
    }
    
    /**
     * Gets rolesList
     * @return rolesList the rolesList
     */
    public List<SelectItem> getRolesList()
    {
        return rolesList;
    }
    
    /**
     * Sets role
     * @param role the role to set
     */
    public void setRole(String role)
    {
        this.role = role;
    }
    
    /**
     * Gets role
     * @return role the role
     */
    public String getRole()
    {
        return role;
    }
    
    /**
     * Sets showCountryList
     * @param showCountryList the showCountryList to set
     */
    public void setShowCountryList(boolean showCountryList)
    {
        this.showCountryList = showCountryList;
    }
    
    /**
     * Gets showCountryList
     * @return showCountryList the showCountryList
     */
    public boolean getShowCountryList()
    {
        return showCountryList;
    }
    
    /**
     * Sets roleForSave
     * @param roleForSave the roleForSave to set
     */
    public void setRoleForSave(Roles roleForSave)
    {
        this.roleForSave = roleForSave;
    }
    
    /**
     * Gets roleForSave
     * @return roleForSave the roleForSave
     */
    public Roles getRoleForSave()
    {
        return roleForSave;
    }
    
    /**
     * Sets countryForSave
     * @param countryForSave the countryForSave to set
     */
    public void setCountryForSave(Countries countryForSave)
    {
        this.countryForSave = countryForSave;
    }
    
    /**
     * Gets countryForSave
     * @return countryForSave the countryForSave
     */
    public Countries getCountryForSave()
    {
        return countryForSave;
    }
    
    /**
     * Sets showPhisical
     * @param showPhisical the showPhisical to set
     */
    public void setShowPhisical(boolean showPhisical)
    {
        this.showPhisical = showPhisical;
    }
    
    /**
     * Gets showPhisical
     * @return showPhisical the showPhisical
     */
    public boolean getShowPhisical()
    {
        return showPhisical;
    }
    
    /**
     * Sets showGiuridical
     * @param showGiuridical the showGiuridical to set
     */
    public void setShowGiuridical(boolean showGiuridical)
    {
        this.showGiuridical = showGiuridical;
    }
    
    /**
     * Gets showGiuridical
     * @return showGiuridical the showGiuridical
     */
    public boolean getShowGiuridical()
    {
        return showGiuridical;
    }
    
    /**
     * Sets countryToShow
     * @param countryToShow the countryToShow to set
     */
    public void setCountryToShow(String countryToShow)
    {
        this.countryToShow = countryToShow;
    }
    
    /**
     * Gets countryToShow
     * @return countryToShow the countryToShow
     */
    public String getCountryToShow()
    {
        return countryToShow;
    }
    
    /**
     * Sets listMails
     * @param listMails the listMails to set
     */
    public void setListMails(List<InfoObject> listMails)
    {
        this.listMails = listMails;
    }
    
    /**
     * Gets listMails
     * @return listMails the listMails
     */
    public List<InfoObject> getListMails()
    {
        return listMails;
    }
    
}
