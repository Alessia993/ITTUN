/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class FinancialProgressViewBean extends
        EntityProjectListBean<AnnualProfiles>
{
    private boolean editable;
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSession().get("project") != null)
        {
            Projects currentProject = BeansFactory.Projects().Load(
                    this.getSession().get("project").toString());
            
            List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                    .LoadLastByProject(
                            this.getSession().get("project").toString());
            
            ProjectIformationCompletations pic = BeansFactory
                    .ProjectIformationCompletations().LoadByProject(
                            this.getSession().get("project").toString());
            
            if (lst.size() == 0)
            {
                if (pic == null)
                {
                    this.goTo(PagesTypes.PROJECTINDEX);
                }
                
                for (int i = 0; i < pic.getYearDuration(); i++)
                {
                    AnnualProfiles ap = new AnnualProfiles();
                    ap.setProject(currentProject);
                    ap.setYear(i + 1);
                    ap.setAmountRealized(0.0);
                    ap.setDeleted(false);
                    ap.setCreateDate(DateTime.getNow());
                    ap.setVersion(1l);
                    
                    BeansFactory.AnnualProfiles().Save(ap);
                }
                
                lst = BeansFactory.AnnualProfiles().LoadLastByProject(
                        this.getSession().get("project").toString());
            }
            List<AnnualProfiles> todel = new ArrayList<AnnualProfiles>();
            
            if (lst.size() < pic.getYearDuration())
            {
                for (int i = 0; i < pic.getYearDuration(); i++)
                {
                    if (lst.size() <= i)
                    {
                        AnnualProfiles ap = new AnnualProfiles();
                        ap.setProject(currentProject);
                        ap.setYear(i + 1);
                        ap.setAmountRealized(0.0);
                        ap.setDeleted(false);
                        ap.setCreateDate(DateTime.getNow());
                        ap.setVersion(lst.get(0).getVersion());
                        
                        BeansFactory.AnnualProfiles().Save(ap);
                    }
                }
                
                lst = BeansFactory.AnnualProfiles().LoadLastByProject(
                        this.getSession().get("project").toString());
            }
            else if (lst.size() > pic.getYearDuration())
            {
                for (AnnualProfiles item : lst)
                {
                    if (lst.indexOf(item) > pic.getYearDuration() - 1)
                    {
                        todel.add(item);
                    }
                }
            }
            
            for (AnnualProfiles item : todel)
            {
                BeansFactory.AnnualProfiles().Delete(item);
                lst.remove(lst.indexOf(item));
            }
            
            for (AnnualProfiles ap : lst)
            {
                if (ap.getVersion() == null)
                {
                    ap.setVersion(1l);
                }
                
                if (ap.getAmountToAchieve() == null)
                {
                    ap.setAmountToAchieve(0.0);
                }
                
                // Update field Importo Previsto
                List<PartnersBudgets> lstPB = BeansFactory.PartnersBudgets()
                        .LoadByProjectAndYear(
                                this.getSession().get("project").toString(),
                                ap.getYear());
                
                double totalAmount = 0.0;
                for (PartnersBudgets pb : lstPB)
                {
                    totalAmount += pb.getTotalAmount();
                }
                
                ap.setAmountExpected(totalAmount);
                //ap.setAmountToAchieve(ap.getAmountExpected()-ap.getAmountRealized());
                BeansFactory.AnnualProfiles().Save(ap);
            }
            double sum1 = 0d;
            double sum2 = 0d;
            double sum3 = 0d;
            
            for (AnnualProfiles ap : lst)
            {
                ap.setString(Utils.getString("annualProfileYear") + " "
                        + ap.getYear());
                sum1 += ap.getAmountExpected();
                sum2 += ap.getAmountRealized();
                sum3 += ap.getAmountToAchieve();
                if (ap.getProgressValidationObject() != null)
                {
                    this.setEditable(false);
                }
                else
                {
                    this.setEditable(true);
                }
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(pic.getStartYear());
            
            int year = cal.get(Calendar.YEAR);
            for (AnnualProfiles item : lst)
            {
                item.setString(String.valueOf(year + lst.indexOf(item)));
            }
            
            AnnualProfiles total = new AnnualProfiles();
            total.setAmountExpected(sum1);
            total.setAmountRealized(sum2);
            total.setAmountToAchieve(sum3);
            total.setString(Utils.getString("total"));
            lst.add(total);
            
            this.setList(lst);
        }
    }
    
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void editEntity()
    {
        this.getSession().put("financialProgress", this.getEntityEditId());
        this.getSession().put("FromProgressValidationFlowFinancialProgress",
                null);
        goTo(PagesTypes.FINANCILAPROGRESSEDIT);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    /**
     * Sets editable
     * @param editable the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    /**
     * Gets editable
     * @return editable the editable
     */
    public boolean isEditable()
    {
        return editable;
    }
}
