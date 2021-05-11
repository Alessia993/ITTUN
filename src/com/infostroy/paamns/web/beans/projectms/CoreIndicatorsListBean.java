/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.CoreIndicators;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;
import com.infostroy.paamns.web.beans.wrappers.CoreIndicatorWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class CoreIndicatorsListBean extends
        EntityProjectListBean<CoreIndicatorWrapper>
{
    
    private static List<CoreIndicatorWrapper> allList;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(new ArrayList<CoreIndicatorWrapper>());
        setAllList(new ArrayList<CoreIndicatorWrapper>());
        List<PhisicalInitializationToCoreIndicators> listCores = BeansFactory
                .PhisicalInizializationToCoreIndicators().LoadLastByProject(
                        this.getProjectId());
        List<CoreIndicators> listCore = BeansFactory.CoreIndicators().Load();
        if (listCores.size() == 0)
        {
            this.getViewState().put("selectedRowIndexCore", null);
        }
        if (this.getRequestUrl().contains("List"))
        {
            for (PhisicalInitializationToCoreIndicators item : listCores)
            {
                getList().add(new CoreIndicatorWrapper(item));
            }
        }
        else if (this.getRequestUrl().contains("Edit"))
        {
            for (CoreIndicators item : listCore)
            {
                getAllList().add(
                        new CoreIndicatorWrapper(item, getValue(listCore,
                                listCores, item)));
            }
            
            if (getAllList().get(getAllList().size() - 1).getProgrammedValues() == null
                    || getAllList().get(getAllList().size() - 1)
                            .getProgrammedValues().equals(""))
            {
                getAllList().get(getAllList().size() - 1).setProgrammedValues(
                        "0");
            }
        }
    }
    
    private String getValue(List<CoreIndicators> listCore,
            List<PhisicalInitializationToCoreIndicators> list,
            CoreIndicators item)
    {
        for (PhisicalInitializationToCoreIndicators indi : list)
        {
            if (indi.getIndicator().equals(item))
            {
                /*
                 * PhisicalInitializationEditBean.setSelectedCore(Long.parseLong(
                 * String.valueOf(listCore .indexOf(item))));
                 */
                this.getViewState().put("selectedRowIndexCore",
                        Long.parseLong(String.valueOf(listCore.indexOf(item))));
                return indi.getProgrammedValues();
            }
        }
        
        return null;
    }
    
    /**
     * Sets allList
     * 
     * @param allList
     *            the allList to set
     */
    public void setAllList(List<CoreIndicatorWrapper> allList)
    {
        CoreIndicatorsListBean.allList = allList;
    }
    
    /**
     * Gets allList
     * 
     * @return allList the allList
     */
    public List<CoreIndicatorWrapper> getAllList()
    {
        return allList;
    }
    
    public static CoreIndicatorWrapper getByRow(Long row)
    {
        CoreIndicatorWrapper item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        return item;
    }
    
    public static boolean isValid(Long row)
    {
        CoreIndicatorWrapper item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        if (item.getProgrammedValues() != null
                && !item.getProgrammedValues().isEmpty()
                && ValidatorBean.IsNumber(item.getProgrammedValues()))
        {
            return true;
        }
        
        return false;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        // TODO Auto-generated method stub
        
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
