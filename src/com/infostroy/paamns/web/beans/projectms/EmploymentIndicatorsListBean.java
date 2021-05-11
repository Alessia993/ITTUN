/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.EmploymentIndicators;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;
import com.infostroy.paamns.web.beans.wrappers.EmploymentIndicatorWebBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class EmploymentIndicatorsListBean extends
        EntityProjectListBean<EmploymentIndicatorWebBean>
{
    private static List<EmploymentIndicatorWebBean> allList;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        setList(new ArrayList<EmploymentIndicatorWebBean>());
        /*List<EmploymentIndicators> listIndic = BeansFactory
                .EmploymentIndicators().GetByProject(this.getProjectId());*/
        
        List<PhisicalInitializationToEmploymentIndicators> listQsns = BeansFactory
                .PhisicalInizializationToEmploymentIndicators()
                .LoadLastByProject(this.getProjectId());
        if (listQsns.isEmpty())
        {
            this.getViewState().put("selectedRowIndexEmployment", null);
        }
        
        if (this.getRequestUrl().contains("List"))
        {
            for (PhisicalInitializationToEmploymentIndicators item : listQsns)
            {
                getList().add(new EmploymentIndicatorWebBean(item));
            }
        }
        else if (this.getRequestUrl().contains("Edit"))
        {
            setAllList(new ArrayList<EmploymentIndicatorWebBean>());
            
            List<EmploymentIndicators> listEmployment = BeansFactory
                    .EmploymentIndicators().Load();
            
            for (EmploymentIndicators item : listEmployment)
            {
                String value = getValue(listEmployment, listQsns, item);
                getAllList().add(new EmploymentIndicatorWebBean(item, value));
            }
        }
    }
    
    private String getValue(List<EmploymentIndicators> listQsn,
            List<PhisicalInitializationToEmploymentIndicators> list,
            EmploymentIndicators item)
    {
        for (PhisicalInitializationToEmploymentIndicators indi : list)
        {
            if (indi.getIndicator().equals(item))
            {
                this.getViewState().put("selectedRowIndexEmployment",
                        Long.parseLong(String.valueOf(listQsn.indexOf(item))));
                
                return indi.getProgrammedValues();
            }
        }
        
        return null;
    }
    
    @SuppressWarnings("unused")
    private boolean isSelected(List<EmploymentIndicators> list,
            EmploymentIndicators item)
    {
        if (list.contains(item))
        {
            return true;
        }
        return false;
    }
    
    public static EmploymentIndicatorWebBean getByRow(Long row)
    {
        EmploymentIndicatorWebBean item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        return item;
    }
    
    public static boolean isValid(Long row)
    {
        EmploymentIndicatorWebBean item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        if (item.getProgrammedValues() != null
                && !item.getProgrammedValues().isEmpty()
                && ValidatorBean.IsNumber(item.getProgrammedValues()))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Sets allList
     * @param allList the allList to set
     */
    public void setAllList(List<EmploymentIndicatorWebBean> allList)
    {
        EmploymentIndicatorsListBean.allList = allList;
    }
    
    /**
     * Gets allList
     * @return allList the allList
     */
    public List<EmploymentIndicatorWebBean> getAllList()
    {
        return allList;
    }
    
    public static List<EmploymentIndicatorWebBean> getAllEmploymentList()
    {
        return allList;
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
}
