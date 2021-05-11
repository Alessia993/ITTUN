package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.LocalizationMailSender;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Localizations;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Cities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Departments;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Provinces;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
*
* @author Nickolay Sumyatin
* nikolay.sumyatin@infostroy.com.ua
* InfoStroy Co., 29.03.2010.
* modified by Ingloba360
*
*/
public class LocalizationEditBean extends EntityProjectEditBean<Localizations> {
	
    /*
     * Form fields
     */
    
	private String codCap;
	
    private String           address;
    
    private String           coordinateX;
    
    private String           coordinateY;
    
    private List<SelectItem> listCountries;
    
    private List<SelectItem> listRegiones;
    
    private List<SelectItem> listProvinces;
    
    private List<SelectItem> listDepartments;
    
    private List<SelectItem> listCities;
    
    private List<SelectItem> listZones;
    
    List<InfoObject>         listLMS;
    
    /**
     * Private methods
     */
    
    private void FillCountries() throws HibernateException,
            PersistenceBeanException
    {
        this.listCountries = new ArrayList<SelectItem>();
        //this.listCountries.add(SelectItemHelper.getFirst());
        
        List<Countries> lst = new ArrayList<Countries>();
        
        try
        {
            lst = BeansFactory.Countries().Load();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        for (Countries c : lst)
        {
            SelectItem item = new SelectItem();
            item.setValue(String.valueOf(c.getId()));
            item.setLabel(c.getValue());
            
            this.listCountries.add(item);
        }
        
        if (this.getCountry() == null)
        {
            if (!this.listCountries.isEmpty())
            {
                this.setCountry(this.listCountries.get(0).getValue().toString());
                if (lst.get(0).getCode().equalsIgnoreCase("italy"))
                {
                    this.setIsItaly("");
                    this.setIsFrance("none");
                    this.setRequiredFrance("false");
                    this.setRequiredItaly("true");
                }
                if (lst.get(0).getCode().equalsIgnoreCase("france"))
                {
                    this.setIsItaly("none");
                    this.setIsFrance("");
                    this.setRequiredFrance("true");
                    this.setRequiredItaly("false");
                }
            }
        }
        else
        {
            if (!this.getCountry().isEmpty())
            {
                Countries c = BeansFactory.Countries().Load(this.getCountry());
                
                if (c.getCode().equalsIgnoreCase("italy"))
                {
                    this.setIsItaly("");
                    this.setIsFrance("none");
                    this.setRequiredFrance("false");
                    this.setRequiredItaly("true");
                }
                if (c.getCode().equalsIgnoreCase("france"))
                {
                    this.setIsItaly("none");
                    this.setIsFrance("");
                    this.setRequiredFrance("true");
                    this.setRequiredItaly("false");
                }
            }
        }
        
        this.FillRegiones();
    }
    
    private void FillRegiones() throws HibernateException,
            PersistenceBeanException
    {
        this.listRegiones = new ArrayList<SelectItem>();
        this.listRegiones.add(SelectItemHelper.getFirst());
        
        List<Regions> lst = new ArrayList<Regions>();
        
        if (this.getCountry() != null)
        {
            try
            {
                lst = BeansFactory.Regions().LoadByCountry(this.getCountry());
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
            
            for (Regions r : lst)
            {
                SelectItem item = new SelectItem();
                item.setValue(String.valueOf(r.getId()));
                item.setLabel(r.getValue());
                
                this.listRegiones.add(item);
            }
            
            if (this.getRegion() == null && !this.listRegiones.isEmpty())
            {
                this.setRegion(this.listRegiones.get(0).getValue().toString());
            }
            
            Countries c = BeansFactory.Countries().Load(this.getCountry());
            
            if (c.getCode().equalsIgnoreCase("italy"))
            {
                this.FillProvinces();
            }
            if (c.getCode().equalsIgnoreCase("france"))
            {
                this.FillDepartments();
            }
        }
    }
    
    private void FillProvinces() throws HibernateException,
            PersistenceBeanException
    {
        this.listProvinces = new ArrayList<SelectItem>();
        this.listProvinces.add(SelectItemHelper.getFirst());
        
        List<Provinces> lst = new ArrayList<Provinces>();
        
        if (this.getRegion() != null && !this.getRegion().isEmpty())
        {
            try
            {
                lst = BeansFactory.Provinces().LoadByRegion(this.getRegion());
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
            
            for (Provinces p : lst)
            {
                SelectItem item = new SelectItem();
                item.setValue(String.valueOf(p.getId()));
                item.setLabel(p.getValue());
                
                this.listProvinces.add(item);
            }
            
            if (this.getProvince() == null && !this.listProvinces.isEmpty())
            {
                this.setProvince(this.listProvinces.get(0).getValue()
                        .toString());
            }
            
        }
        
        this.FillCities();
    }
    
    private void FillDepartments() throws HibernateException,
            PersistenceBeanException
    {
        this.listDepartments = new ArrayList<SelectItem>();
        this.listDepartments.add(SelectItemHelper.getFirst());
        
        List<Departments> lst = new ArrayList<Departments>();
        
        if (this.getRegion() != null && !this.getRegion().isEmpty())
        {
            try
            {
                lst = BeansFactory.Departments().LoadByRegion(this.getRegion());
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
            
            for (Departments d : lst)
            {
                SelectItem item = new SelectItem();
                item.setValue(String.valueOf(d.getId()));
                item.setLabel(d.getValue());
                
                this.listDepartments.add(item);
            }
            
            if (this.getDepartment() == null && !this.listDepartments.isEmpty())
            {
                this.setDepartment(this.listDepartments.get(0).getValue()
                        .toString());
            }
        }
        
        this.FillZones();
    }
    
    private void FillCities() throws HibernateException,
            PersistenceBeanException
    {
        this.listCities = new ArrayList<SelectItem>();
        this.listCities.add(SelectItemHelper.getFirst());
        
        List<Cities> lst = new ArrayList<Cities>();
        
        if (this.getProvince() != null && !this.getProvince().isEmpty())
        {
            try
            {
                lst = BeansFactory.Cities().LoadByProvince(this.getProvince());
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
            
            for (Cities c : lst)
            {
                SelectItem item = new SelectItem();
                item.setValue(String.valueOf(c.getId()));
                item.setLabel(c.getValue());
                
                this.listCities.add(item);
            }
        }
    }
    
    private void FillZones() throws HibernateException,
            PersistenceBeanException
    {
        this.listZones = new ArrayList<SelectItem>();
        this.listZones.add(SelectItemHelper.getFirst());
        
        List<Zones> lst = new ArrayList<Zones>();
        
        if (this.getDepartment() != null && !this.getDepartment().isEmpty())
        {
            try
            {
                lst = BeansFactory.Zones().LoadByDepartment(
                        this.getDepartment());
            }
            catch(PersistenceBeanException e)
            {
                e.printStackTrace();
            }
            
            for (Zones z : lst)
            {
                SelectItem item = new SelectItem();
                item.setValue(String.valueOf(z.getId()));
                item.setLabel(z.getValue());
                
                this.listZones.add(item);
            }
        }
    }
    
    private void FillComboboxes() throws HibernateException,
            PersistenceBeanException
    {
        this.FillCountries();
        this.FillRegiones();
        this.FillProvinces();
        this.FillDepartments();
        this.FillCities();
        this.FillZones();
    }
    
    /**
     * Event handlers
     */
    
    /**
     * @throws PersistenceBeanException 
     * @throws HibernateException 
     * @throws NumberFormatException 
     * 
     */
    public void countryChange(ValueChangeEvent event)
            throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (event.getNewValue() != null)
        {
            this.setCountry(event.getNewValue().toString());
            Countries currCountry = BeansFactory.Countries().Load(
                    this.getCountry());
            if (currCountry.getCode().equalsIgnoreCase("france"))
            {
                //Hide/show corresponding controls
                this.setIsItaly("none");
                this.setIsFrance("");
                this.setRequiredFrance("true");
                this.setRequiredItaly("false");
            }
            else
            {
                if (currCountry.getCode().equalsIgnoreCase("italy"))
                {
                    //Hide/show corresponding controls
                    this.setIsItaly("");
                    this.setIsFrance("none");
                    this.setRequiredFrance("false");
                    this.setRequiredItaly("true");
                }
            }
            
            this.FillRegiones();
        }
    }
    
    public void regionChange(ValueChangeEvent event)
            throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (event.getNewValue() != null)
        {
            Countries currCountry = BeansFactory.Countries().Load(
                    this.getCountry());
            
            this.setRegion(event.getNewValue().toString());
            
            if (currCountry.getCode().equalsIgnoreCase("France"))
            {
                //Load French departments
                this.FillDepartments();
            }
            else
            {
                if (currCountry.getCode().equalsIgnoreCase("Italy"))
                {
                    //Load Italian provinces
                    this.FillProvinces();
                }
            }
        }
    }
    
    public void provinceChange(ValueChangeEvent event)
            throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (event.getNewValue() != null)
        {
            this.setProvince(event.getNewValue().toString());
            
            this.FillCities();
        }
    }
    
    public void departmentChange(ValueChangeEvent event)
            throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (event.getNewValue() != null)
        {
            this.setDepartment(event.getNewValue().toString());
            
            this.FillZones();
        }
    }
    
    @Override
    public void AfterSave()
    {
        //        new LocalizationMailSender(listLMS).start();
        Transaction tr = null;
        try
        {
            tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            List<Mails> mails = new ArrayList<Mails>();
            LocalizationMailSender localizationMailSender = new LocalizationMailSender(
                    listLMS);
            mails.addAll(localizationMailSender
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
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.LOCALIZATIONLIST);
        
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.FillComboboxes();
    }
    
    private void loadEntity() throws PersistenceBeanException
    {
        if (this.getSession().get("localization") != null)
        {
            Localizations entityToLoad = BeansFactory.Localizations().Load(
                    String.valueOf(this.getSession().get("localization")));
            
            this.setCountry(entityToLoad.getCountry().getId().toString());
            this.setCoordinateX(entityToLoad.getCoordinateX());
            this.setCoordinateY(entityToLoad.getCoordinateY());
            this.setCodCap(entityToLoad.getCodCap());
            this.setAddress(entityToLoad.getAddress());
            this.setRegion(entityToLoad.getRegion().getId().toString());
            
            if (entityToLoad.getCountry().getCode().equalsIgnoreCase("italy"))
            {
                this.setProvince(entityToLoad.getProvince().getId().toString());
                this.setCity(entityToLoad.getCity().getId().toString());
            }
            else
            {
                if (entityToLoad.getCountry().getCode()
                        .equalsIgnoreCase("france"))
                {
                    this.setDepartment(entityToLoad.getDepartment().getId()
                            .toString());
                    //this.setZone(entityToLoad.getZone().getId().toString());
                }
            }
        }
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException,
            PersistenceBeanException
    {
        this.clearSessionValues();
        
        if (!AccessGrantHelper.getIsAGUAsse5())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        this.loadEntity();
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException, PersistenceBeanException
    {
        listLMS = new ArrayList<InfoObject>();
        Localizations entityToSave = new Localizations();
        
        Projects project = BeansFactory.Projects().Load(
                String.valueOf(this.getSession().get("project")));
        
        if (this.getSession().get("localization") != null)
        {
            entityToSave = BeansFactory.Localizations().Load(
                    String.valueOf(this.getSession().get("localization")));
        }
        else
        {
            entityToSave.setProject(project);
        }
        
        List<Localizations> lstProjectLocalizations = BeansFactory
                .Localizations().LoadByProject(project.getId().toString());
        if (lstProjectLocalizations.size() == 0)
        {
            entityToSave.setMain(true);
        }
        else
        {
            if (lstProjectLocalizations.size() == 1)
            {
                Localizations first = lstProjectLocalizations.get(0);
                
                if (first.getId() == entityToSave.getId())
                {
                    entityToSave.setMain(true);
                }
            }
        }

        entityToSave.setCodCap(this.getCodCap());
        entityToSave.setAddress(this.getAddress());
        entityToSave.setCoordinateX(this.getCoordinateX());
        entityToSave.setCoordinateY(this.getCoordinateY());
        Countries cCountry = BeansFactory.Countries().Load(this.getCountry());
        entityToSave.setCountry(cCountry);
        Regions cRegion = BeansFactory.Regions().Load(this.getRegion());
        entityToSave.setRegion(cRegion);
        
        if (cCountry.getCode().equalsIgnoreCase("france"))
        {
            Departments fDepartment = BeansFactory.Departments().Load(
                    this.getDepartment());
            entityToSave.setDepartment(fDepartment);
            //Zones fZone = BeansFactory.Zones().Load(this.getZone());
            //entityToSave.setZone(fZone);
        }
        else
        {
            if (cCountry.getCode().equalsIgnoreCase("italy"))
            {
                Provinces iProvince = BeansFactory.Provinces().Load(
                        this.getProvince());
                entityToSave.setProvince(iProvince);
                Cities iCity = BeansFactory.Cities().Load(this.getCity());
                entityToSave.setCity(iCity);
            }
        }
        
        BeansFactory.Localizations().Save(entityToSave);
        
        //  Send mail notification
        if (this.getProjectState().equals(ProjectState.Actual))
        {
            List<CFPartnerAnagraphs> cpap = BeansFactory.CFPartnerAnagraphs()
                    .GetCFAnagraphForProject(this.getProjectId(),
                            CFAnagraphTypes.CFAnagraph);
            if (cpap.size() > 0)
            {
                if (this.getProject().getAsse() != 5)
                {
                    for (Users item : BeansFactory.Users().getByRole(
                            UserRoleTypes.AGU))
                    {
                        InfoObject info = new InfoObject();
                        info.setMail(item.getMail());
                        info.setName(cpap.get(0).getUser().getName());
                        info.setSurname(cpap.get(0).getUser().getSurname());
                        info.setProjectName(project.getTitle());
                        listLMS.add(info);
                    }
                    
                }
                
                for (Users item : BeansFactory.Users().getByRole(
                        UserRoleTypes.STC))
                {
                    InfoObject info = new InfoObject();
                    info.setMail(item.getMail());
                    info.setName(cpap.get(0).getUser() == null ? cpap.get(0)
                            .getName() : cpap.get(0).getUser().getName());
                    info.setSurname(cpap.get(0).getUser() == null ? "" : cpap
                            .get(0).getUser().getSurname());
                    info.setProjectName(project.getTitle());
                    listLMS.add(info);
                }
            }
        }
    }
    
    public void setCountry(String country)
    {
        this.getSession().put("country", country);
    }
    
    public String getCountry()
    {
        if (this.getSession().get("country") == null)
        {
            return null;
        }
        
        return this.getSession().get("country").toString();
    }
    
    public void setListCountries(List<SelectItem> listCountries)
    {
        this.listCountries = listCountries;
    }
    
    public List<SelectItem> getListCountries()
    {
        return listCountries;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setCoordinateX(String coordinateX)
    {
        this.coordinateX = coordinateX;
    }
    
    public String getCoordinateX()
    {
        return coordinateX;
    }
    
    public void setCoordinateY(String coordinateY)
    {
        this.coordinateY = coordinateY;
    }
    
    public String getCoordinateY()
    {
        return coordinateY;
    }
    
    public void setRegion(String region)
    {
        this.getSession().put("region", region);
    }
    
    public String getRegion()
    {
        if (this.getSession().get("region") == null)
        {
            return null;
        }
        
        return this.getSession().get("region").toString();
    }
    
    public void setListRegiones(List<SelectItem> listRegiones)
    {
        this.listRegiones = listRegiones;
    }
    
    public List<SelectItem> getListRegiones()
    {
        return listRegiones;
    }
    
    public void setProvince(String province)
    {
        this.getSession().put("province", province);
    }
    
    public String getProvince()
    {
        if (this.getSession().get("province") == null)
        {
            return null;
        }
        
        return this.getSession().get("province").toString();
    }
    
    public void setListProvinces(List<SelectItem> listProvinces)
    {
        this.listProvinces = listProvinces;
    }
    
    public List<SelectItem> getListProvinces()
    {
        if (this.listProvinces == null)
            return new ArrayList<SelectItem>();
        
        return listProvinces;
    }
    
    public void setDepartment(String department)
    {
        this.getSession().put("department", department);
    }
    
    public String getDepartment()
    {
        if (this.getSession().get("department") == null)
        {
            return null;
        }
        
        return this.getSession().get("department").toString();
    }
    
    public void setListDepartments(List<SelectItem> listDepartments)
    {
        this.listDepartments = listDepartments;
    }
    
    public List<SelectItem> getListDepartments()
    {
        if (this.listDepartments == null)
        {
            return new ArrayList<SelectItem>();
        }
        
        return listDepartments;
    }
    
    public void setCity(String city)
    {
        this.getSession().put("city", city);
    }
    
    public String getCity()
    {
        if (this.getSession().get("city") == null)
        {
            return null;
        }
        
        return this.getSession().get("city").toString();
    }
    
    public void setZone(String zone)
    {
        this.getSession().put("zone", zone);
    }
    
    public String getZone()
    {
        if (this.getSession().get("zone") == null)
        {
            return null;
        }
        
        return this.getSession().get("zone").toString();
    }
    
    public void setListCities(List<SelectItem> listCities)
    {
        this.listCities = listCities;
    }
    
    public List<SelectItem> getListCities()
    {
        if (this.listCities == null)
        {
            return new ArrayList<SelectItem>();
        }
        
        return listCities;
    }
    
    public List<SelectItem> getListZones()
    {
        if (this.listZones == null)
        {
            return new ArrayList<SelectItem>();
        }
        
        return listZones;
    }
    
    public void setListZones(List<SelectItem> listZones)
    {
        this.listZones = listZones;
    }
    
    public void setIsFrance(String isFrance)
    {
        this.getSession().put("isFrance", isFrance);
    }
    
    public String getIsFrance()
    {
        return this.getSession().get("isFrance").toString();
    }
    
    public void setIsItaly(String isItaly)
    {
        this.getSession().put("isItaly", isItaly);
    }
    
    public String getIsItaly()
    {
        return this.getSession().get("isItaly").toString();
    }
    
    public void setRequiredFrance(String requiredFrance)
    {
        this.getSession().put("requiredFrance", requiredFrance);
    }
    
    public String getRequiredFrance()
    {
        return this.getSession().get("requiredFrance").toString();
    }
    
    public void setRequiredItaly(String requiredItaly)
    {
        this.getSession().put("requiredItaly", requiredItaly);
    }
    
    public String getRequiredItaly()
    {
        return this.getSession().get("requiredItaly").toString();
    }
    
    private void clearSessionValues()
    {
        this.getSession().put("region", null);
        this.getSession().put("province", null);
        this.getSession().put("department", null);
        this.getSession().put("city", null);
        this.getSession().put("zone", null);
        this.getSession().put("isFrance", null);
        this.getSession().put("isItaly", null);
        this.getSession().put("requiredFrance", null);
        this.getSession().put("requiredItaly", null);
    }
    
    /**
     * Sets codCap.
     * @param codCap
     */
    public void setCodCap(String codCap) {
        this.codCap = codCap;
    }
    
    /**
     * Gets codCap.
     * @return codCap
     */
    public String getCodCap() {
        return codCap;
    }
}
