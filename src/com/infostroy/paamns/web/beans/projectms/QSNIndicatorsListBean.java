/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;
import com.infostroy.paamns.web.beans.wrappers.QSNIndicatorWrapper;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class QSNIndicatorsListBean extends EntityProjectListBean<QsnIndicators>
{
    /**
     * Stores allList value of entity.
     */
    private static List<QSNIndicatorWrapper> allList;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        this.setAllList(new ArrayList<QSNIndicatorWrapper>());
        this.setList(new ArrayList<QsnIndicators>());
        
        List<QsnIndicators> listQsns = BeansFactory.QsnIndicators()
                .GetByProject(this.getProjectId());
        
        List<QsnIndicators> listQsn = BeansFactory.QsnIndicators().Load();
        
        if (listQsns.size() == 0)
        {
            this.getViewState().put("selectedRowIndexQSN", null);
        }
        
        if (this.getRequestUrl().contains("List"))
        {
            setList(listQsns);
        }
        else if (this.getRequestUrl().contains("Edit"))
        {
            for (QsnIndicators item : listQsn)
            {
                getSelected(listQsn, listQsns, item);
                getAllList().add(new QSNIndicatorWrapper(item));
            }
        }
    }
    
    private void getSelected(List<QsnIndicators> listQsn,
            List<QsnIndicators> listQsns, QsnIndicators item)
    {
        for (QsnIndicators indi : listQsns)
        {
            if (indi.equals(item))
            {
                this.getViewState().put("selectedRowIndexQSN",
                        Long.parseLong(String.valueOf(listQsn.indexOf(item))));
            }
        }
    }
    
    /**
     * Sets allList
     * @param allList the allList to set
     */
    public void setAllList(List<QSNIndicatorWrapper> allList)
    {
        QSNIndicatorsListBean.allList = allList;
    }
    
    /**
     * Gets allList
     * @return allList the allList
     */
    public List<QSNIndicatorWrapper> getAllList()
    {
        return allList;
    }
    
    public static QSNIndicatorWrapper getByRow(Long row)
    {
        QSNIndicatorWrapper item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        return item;
    }
    
    public static boolean isValid(Long row)
    {
        QSNIndicatorWrapper item = allList.get(Integer.parseInt(String
                .valueOf(row)));
        if (item.getProgrammedValues() != null
                && !item.getProgrammedValues().isEmpty()
                && ValidatorBean.IsNumber(item.getProgrammedValues()))
        {
            return true;
        }
        
        return false;
    }
    
    public static QSNIndicatorWrapper getOne(Long row)
    {
        return allList.get(Integer.parseInt(String.valueOf(row)));
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
