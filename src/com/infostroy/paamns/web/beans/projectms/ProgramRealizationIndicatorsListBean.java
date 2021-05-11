/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;
import com.infostroy.paamns.web.beans.wrappers.ProgramRealizationIndicatorWrapper;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class ProgramRealizationIndicatorsListBean extends
        EntityProjectListBean<ProgramRealizationIndicatorWrapper>
{
    
    /**
     * Stores allList value of entity.
     */
    private static List<ProgramRealizationIndicatorWrapper> allList;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        setAllList(new ArrayList<ProgramRealizationIndicatorWrapper>());
        setList(new ArrayList<ProgramRealizationIndicatorWrapper>());
        
        List<PhisicalInitializationToProgramIndicators> listIndic = BeansFactory
                .PhisicalInizializationToProgramRealizationIndicators()
                .LoadLastByProject(this.getProjectId());
        
       /* List<ProgramIndicators> listRealization = BeansFactory
                .ProgramResultIndicators().Load(
                        ProgramIndicatorsType.Realization,
                        Integer.valueOf(this.getProject().getAsse()).toString());
        */
        
        List<ProgramIndicators> listRealization = BeansFactory
        .ProgramRealizationIndicators().Load();

        if (this.getRequestUrl().contains("List"))
        {
            for (PhisicalInitializationToProgramIndicators item : listIndic)
            {
                getList().add(new ProgramRealizationIndicatorWrapper(item));
            }
        }
        else if (this.getRequestUrl().contains("Edit"))
        {
            for (ProgramIndicators item : listRealization)
            {
                String str = isSelected(listIndic, item);
                getAllList().add(
                        new ProgramRealizationIndicatorWrapper(item, str,
                                str != null));
            }
        }
    }
    
    /**
     * @param listIndic
     * @param item
     * @return
     */
    private String isSelected(
            List<PhisicalInitializationToProgramIndicators> listIndic,
            ProgramIndicators item)
    {
        for (PhisicalInitializationToProgramIndicators indicator : listIndic)
        {
            if (indicator.getProgramIndicator().equals(item))
            {
                return indicator.getProgrammedValues();
            }
        }
        
        return null;
    }
    
    /**
     * Sets allList
     * @param allList the allList to set
     */
    public void setAllList(List<ProgramRealizationIndicatorWrapper> allList)
    {
        ProgramRealizationIndicatorsListBean.allList = allList;
    }
    
    /**
     * Gets allList
     * @return allList the allList
     */
    public List<ProgramRealizationIndicatorWrapper> getAllList()
    {
        return allList;
    }
    
    public static List<ProgramRealizationIndicatorWrapper> getAllIndiList()
    {
        return allList;
    }
    
    public static boolean isValid()
    {
       /* for (ProgramRealizationIndicatorWrapper item : allList)
        {
            if (item.getSelected()
                    && (item.getProgrammedValues() == null
                            || item.getProgrammedValues().isEmpty() || !ValidatorBean
                                .IsNumber(item.getProgrammedValues())))
            {
                return false;
            }
        }*/
        
        return true;
    }
    
    public static boolean isEmpty()
    {
        int flag = 0;
        for (ProgramRealizationIndicatorWrapper item : allList)
        {
            if (item.getSelected())
            {
                flag++;
            }
        }
        
        if (flag == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
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
