/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class FinancialProgressEditBean extends
        EntityProjectEditBean<AnnualProfiles>
{
    private String               errorMesage;
    
    private AnnualProfiles       oldItem;
    
    private List<AnnualProfiles> list;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        if (this.getSession()
                .get("FromProgressValidationFlowFinancialProgress") != null)
        {
            this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWEDIT);
        }
        else
        {
            this.goTo(PagesTypes.FINANCILAPROGRESSVIEW);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSateAndAguRead())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        /*this.setEntity(BeansFactory.AnnualProfiles().Load(
                String.valueOf(this.getSession().get("financialProgress"))));
        this.getSession().put("expectedvalue",
                this.getEntity().getAmountExpected());
        this.oldItem = this.getEntity().clone();
        this.oldItem.setId(this.getEntity().getId());*/
        
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
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSateAndAguRead())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        if (this.getSession()
                .get("FromProgressValidationFlowFinancialProgress") != null)
        {
            List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                    .LoadLastByProject(this.getProjectId());
            
            for (int i = 0; i < lst.size(); i++)
            {
                lst.get(i).setAmountRealized(
                        this.getList().get(i).getAmountRealized());
                lst.get(i).setAmountToAchieve(
                        this.getList().get(i).getAmountToAchieve());
                
                BeansFactory.AnnualProfiles().SaveInTransaction(lst.get(i));
            }
            
            /*AnnualProfiles item = BeansFactory.AnnualProfiles().Load(
                    String.valueOf(this.getSession().get("financialProgress")));
            item.setAmountRealized(this.getEntity().getAmountRealized());
            BeansFactory.AnnualProfiles().SaveInTransaction(item);*/
        }
        else
        {
            if (this.CheckModifications())
            {
                /*List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                        .LoadLastByProject(this.getProjectId());*/
                
                for (int i = 0; i < this.getList().size(); i++)
                {
                    if (!this.getList().get(i).getString()
                            .equals(Utils.getString("total")))
                    {
                        AnnualProfiles newEntity = this.getList().get(i)
                                .clone();
                        newEntity
                                .setVersion(this.getList().get(i).getVersion() + 1);
                        newEntity.setProgressValidationObject(null);
                        
                        /*newEntity.setAmountRealized(
                                lst.get(i).getAmountRealized());
                        newEntity.setAmountToAchieve(
                                lst.get(i).getAmountToAchieve());*/
                        
                        BeansFactory.AnnualProfiles().SaveInTransaction(
                                newEntity);
                    }
                }
            }
            
            /*if (!oldItem.getAmountRealized().equals(
                    this.getEntity().getAmountRealized()))
            {
                List<AnnualProfiles> listAP = BeansFactory.AnnualProfiles()
                        .LoadLastByProject(
                                String.valueOf(oldItem.getProject().getId()));
                
                for (AnnualProfiles ap : listAP)
                {
                    AnnualProfiles newEntity = ap.clone();
                    newEntity.setVersion(ap.getVersion() + 1);
                    newEntity.setProgressValidationObject(null);
                    
                    if (ap.getId().equals(oldItem.getId()))
                    {
                        newEntity.setAmountRealized(this.getEntity()
                                .getAmountRealized());
                    }
                    
                    BeansFactory.AnnualProfiles().SaveInTransaction(newEntity);
                }
            }*/
        }
    }
    
    @Override
    public Boolean BeforeSave() throws PersistenceBeanException
    {
        this.getList().remove(this.getList().size() - 1);
        this.AddTotalRow();
        
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
    
    private boolean CheckModifications() throws PersistenceBeanException
    {
        List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                .LoadLastByProject(this.getProjectId());
        
        for (int i = 0; i < lst.size(); i++)
        {
            if (!lst.get(i).getAmountRealized()
                    .equals(this.getList().get(i).getAmountRealized())
                    || !lst.get(i).getAmountToAchieve()
                            .equals(this.getList().get(i).getAmountToAchieve()))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sets oldItem
     * @param oldItem the oldItem to set
     */
    public void setOldItem(AnnualProfiles oldItem)
    {
        this.oldItem = oldItem;
    }
    
    /**
     * Gets oldItem
     * @return oldItem the oldItem
     */
    public AnnualProfiles getOldItem()
    {
        return oldItem;
    }
    
    /**
     * Gets error
     * @return error the error
     */
    public String getError()
    {
        return (String) this.getViewState().get("financialError");
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
}
