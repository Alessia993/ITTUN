package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.web.beans.EntityEditBean;

public class AnnualProfileEditBean extends EntityEditBean<AnnualProfiles>
{
    private String               note;
    
    private String               errorMesage;
    
    private List<AnnualProfiles> list;
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public Boolean BeforeSave()
    {
        this.getList().remove(this.getList().size() - 1);
        try
        {
            this.AddTotalRow();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        double sum1 = 0d;
        double sum2 = 0d;
        double sum3 = 0d;
        
        for (AnnualProfiles item : this.getList())
        {
            if (!item.getString().equals(Utils.getString("total")))
            {
                sum1 += item.getAmountExpected();
                sum2 += item.getAmountRealized();
                sum3 += item.getAmountToAchieve();
            }
        }
        
        if (NumberHelper.Round(sum1) != NumberHelper.Round(sum2 + sum3))
        {
            setErrorMesage(Utils.getString("annualProfilenotEquals"));
            return false;
        }
        
        return true;
        
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.ANNUALPROFILESLIST);
        
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        /*
        if (this.getSession().get("annualProfiles") != null)
        {
            AnnualProfiles entityToLoad = BeansFactory.AnnualProfiles().Load(
                    this.getSession().get("annualProfiles").toString());
            this.setEntity(entityToLoad);
            this.setNote(entityToLoad.getNote());
        }
        */
        if (this.getList() == null)
        {
            this.setList(new ArrayList<AnnualProfiles>());
        }
        else
        {
            this.getList().clear();
        }
        
        List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                .LoadLastByProject(this.getProjectId());
        ProjectIformationCompletations pic = BeansFactory
                .ProjectIformationCompletations().LoadByProject(
                        this.getProjectId());
        Calendar cal = Calendar.getInstance();
        cal.setTime(pic.getStartYear());
        
        int year = cal.get(Calendar.YEAR);
        for (AnnualProfiles item : lst)
        {
            item.setString(String.valueOf(year + lst.indexOf(item)));
            this.getList().add(item.clone());
        }
        
        AddTotalRow();
    }
    
    private void AddTotalRow() throws PersistenceBeanException
    {
        double sum1 = 0d;
        double sum2 = 0d;
        double sum3 = 0d;
        
        for (AnnualProfiles item : this.getList())
        {
            sum1 += item.getAmountExpected();
            sum2 += item.getAmountRealized();
            sum3 += item.getAmountToAchieve();
        }
        
        AnnualProfiles total = new AnnualProfiles();
        total.setAmountExpected(sum1);
        total.setAmountRealized(sum2);
        total.setAmountToAchieve(sum3);
        total.setString(Utils.getString("total"));
        getList().add(total);
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (AccessGrantHelper.IsActualAndAguAccess())
        {
            this.goTo(PagesTypes.ANNUALPROFILESLIST);
        }
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException
    {
        /*if (this.getSession().get("annualProfiles") != null)
        {
            AnnualProfiles entityToSave = BeansFactory.AnnualProfiles().Load(
                    this.getSession().get("annualProfiles").toString());
            
            if (this.getNote().length() > 255)
            {
                entityToSave.setNote(this.getNote().substring(0, 255));
            }
            else
            {
                entityToSave.setNote(this.getNote());
            }
            entityToSave.setAmountToAchieve(this.getEntity()
                    .getAmountToAchieve());
            BeansFactory.AnnualProfiles().Save(entityToSave);
        }*/
        
        List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                .LoadLastByProject(this.getProjectId());
        
        for (int i = 0; i < lst.size(); i++)
        {
            lst.get(i).setAmountRealized(
                    this.getList().get(i).getAmountRealized());
            lst.get(i).setAmountToAchieve(
                    this.getList().get(i).getAmountToAchieve());
            lst.get(i).setNote(this.getList().get(i).getNote());
            
            BeansFactory.AnnualProfiles().SaveInTransaction(lst.get(i));
        }
        
    }
    
    public void setNote(String note)
    {
        this.note = note;
    }
    
    public String getNote()
    {
        return note;
    }
    
    /**
     * Sets errorMesage
     * @param errorMesage the errorMesage to set
     */
    public void setErrorMesage(String errorMesage)
    {
        this.errorMesage = errorMesage;
    }
    
    /**
     * Gets errorMesage
     * @return errorMesage the errorMesage
     */
    public String getErrorMesage()
    {
        return errorMesage;
    }
    
    /**
     * Sets list
     * @param list the list to set
     */
    public void setList(List<AnnualProfiles> list)
    {
        this.list = list;
    }
    
    /**
     * Gets list
     * @return list the list
     */
    public List<AnnualProfiles> getList()
    {
        return list;
    }
    
}
