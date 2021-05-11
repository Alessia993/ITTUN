/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;
import com.infostroy.paamns.web.beans.EntityProjectListBean;
import com.infostroy.paamns.web.beans.wrappers.DurReimbursementWrapper;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */

public class DURReimbursementListBean extends
        EntityProjectListBean<DurReimbursementWrapper>
{
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        List<DurCompilations> listAux = new ArrayList<DurCompilations>();
        
        listAux = BeansFactory.DurCompilations().LoadForReimbursement(
                Long.valueOf(this.getProjectId()));
        
        this.setList(new ArrayList<DurReimbursementWrapper>());
        
        for (DurCompilations dc : listAux)
        {
            DurReimbursements dr = BeansFactory.DurReimbursements()
                    .LoadByDurCompilation(dc.getId());
            DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(
                    dc.getId());
            
            DurReimbursementWrapper drw = new DurReimbursementWrapper(dr, di,
                    dc);
            
            this.getList().add(drw);
        }
    }
    
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        if (!(AccessGrantHelper.IsReadyDurCompilation() && AccessGrantHelper
                .CheckRolesDURCompilation()))
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        List<DurCompilations> listDC = BeansFactory.DurCompilations()
                .LoadForReimbursement(
                        Long.valueOf(String.valueOf(this.getSession().get(
                                "project"))));
        
        if (listDC == null || listDC.isEmpty())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    @Override
    public void addEntity()
    {
        // Do nothing        
    }
    
    @Override
    public void deleteEntity()
    {
        // Do nothing        
    }
    
    @Override
    public void editEntity()
    {
        DurCompilations dr = null;
        
        try
        {
            dr = BeansFactory.DurCompilations().Load(this.getEntityEditId());
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        if (dr != null && !dr.getDeleted())
        {
            this.getSession().put("durReimbEdit", this.getEntityEditId());
            this.getSession().put("durReimbEditShow", false);
            this.goTo(PagesTypes.DURREIMBURSEMENTEDIT);
        }
    }
    
    public void showEntity()
    {
        DurCompilations dr = null;
        
        try
        {
            dr = BeansFactory.DurCompilations().Load(this.getEntityEditId());
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        if (dr != null && !dr.getDeleted())
        {
            this.getSession().put("durReimbEdit", this.getEntityEditId());
            this.getSession().put("durReimbEditShow", true);
            this.goTo(PagesTypes.DURREIMBURSEMENTEDIT);
        }
    }
}
