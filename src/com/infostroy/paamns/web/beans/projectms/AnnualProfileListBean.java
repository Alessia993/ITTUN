/**
 * 
 *
 * @author Nickolay Sumyatin
 * nikolay.sumyatin@infostroy.com.ua
 * InfoStroy Co., 12.04.2010.
 *
 */

package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIformationCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

public class AnnualProfileListBean extends
        EntityProjectListBean<AnnualProfiles>
{
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        Projects currentProject = BeansFactory.Projects().Load(
                this.getProjectId());
        List<AnnualProfiles> lst = BeansFactory.AnnualProfiles()
                .LoadLastByProject(this.getProjectId());
        
        if (lst.size() == 0)
        {
            // Create empty items
            ProjectIformationCompletations pic = BeansFactory
                    .ProjectIformationCompletations().LoadByProject(
                            this.getProjectId());
            
            if (pic == null)
            {
                this.setList(new ArrayList<AnnualProfiles>());
                return;
            }
            
            for (int i = 0; i < pic.getYearDuration(); i++)
            {
                AnnualProfiles ap = new AnnualProfiles();
                ap.setProject(currentProject);
                ap.setYear(i + 1);
                ap.setAmountRealized(0.0);
                ap.setVersion(1l);
                BeansFactory.AnnualProfiles().Save(ap);
            }
            
            lst = BeansFactory.AnnualProfiles().LoadLastByProject(
                    this.getProjectId());
        }
        ProjectIformationCompletations pic = BeansFactory
                .ProjectIformationCompletations().LoadByProject(
                        this.getProjectId());
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
                    ap.setVersion(lst.get(0).getVersion());
                    BeansFactory.AnnualProfiles().Save(ap);
                }
            }
            
            lst = BeansFactory.AnnualProfiles().LoadLastByProject(
                    this.getProjectId());
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
        double sum1 = 0d;
        double sum2 = 0d;
        double sum3 = 0d;
        
        for (AnnualProfiles ap : lst)
        {
            
            if (ap.getAmountExpected() == null)
            {
                ap.setAmountExpected(0.0);
            }
            
            if (ap.getAmountRealized() == null)
            {
                ap.setAmountRealized(0.0);
            }
            
            if (ap.getAmountToAchieve() == null)
            {
                ap.setAmountToAchieve(0.0);
            }
            
            // Update field Importo Previsto
            List<PartnersBudgets> lstPB = BeansFactory.PartnersBudgets()
                    .LoadByProjectAndYear(this.getProjectId(), ap.getYear());
            
            double totalAmount = 0.0;
            for (PartnersBudgets pb : lstPB)
            {
                totalAmount += pb.getTotalAmount();
            }
            
            ap.setAmountExpected(totalAmount);
            ap.setString(Utils.getString("annualProfileYear") + " "
                    + ap.getYear());
            /*
             * ap.setAmountToAchieve(ap.getAmountExpected() -
             * ap.getAmountRealized());
             */
            
            sum1 += ap.getAmountExpected();
            
            sum2 += ap.getAmountRealized();
            
            sum3 += ap.getAmountToAchieve();
            
            BeansFactory.AnnualProfiles().Save(ap);
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
        // this.getSession().put("annualProfiles", this.getEntityEditId());
        
        this.goTo(PagesTypes.ANNUALPROFILESEDIT);
        
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
    
}
