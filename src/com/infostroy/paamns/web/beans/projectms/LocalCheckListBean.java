/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalChecks;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class LocalCheckListBean extends EntityProjectListBean<LocalChecks>
{
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (this.getFilterValue() == null || this.getFilterValue().isEmpty())
        {
            this.setFilterValue("all");
        }
        else
        {
            this.loadList();
        }
    }
    
    /**
     * @throws PersistenceBeanException 
     * @throws NumberFormatException 
     * @throws HibernateException 
     * 
     */
    private void loadList() throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        Boolean closed = null;
        
        if (this.getFilterValue().equals("open"))
        {
            closed = false;
        }
        else if (this.getFilterValue().equals("closed"))
        {
            closed = true;
        }
        
        boolean loadAll = true;
        List<Long> partnerIds = new ArrayList<Long>();
        
        if (this.getSessionBean().isPartner())
        {
            loadAll = false;
            CFPartnerAnagraphs partner = BeansFactory.CFPartnerAnagraphs()
                    .GetByUser(this.getCurrentUser().getId());
            if (partner != null)
            {
                partnerIds.add(partner.getId());
            }
        }
        if (this.getSessionBean().isCIL())
        {
            loadAll = false;
            ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph()
                    .GetByUser(this.getCurrentUser().getId());
            if (cua != null)
            {
                List<CFPartnerAnagraphs> partnersLoc = BeansFactory
                        .CFPartnerAnagraphs().GetByCIL(this.getProjectId(),
                                cua.getId());
                if (partnersLoc != null)
                {
                    for (CFPartnerAnagraphs item : partnersLoc)
                    {
                        CFPartnerAnagraphProject cfpap = BeansFactory
                                .CFPartnerAnagraphProject().LoadByPartner(
                                        Long.valueOf(this.getProjectId()),
                                        item.getId());
                        if (cfpap != null && cfpap.getRemovedFromProject())
                        {
                            continue;
                        }
                        
                        partnerIds.add(item.getId());
                    }
                    
                }
            }
        }
        if (this.getSessionBean().isCF())
        {
            loadAll = false;
            List<CFPartnerAnagraphs> partners = BeansFactory
                    .CFPartnerAnagraphs().LoadByProject(this.getProjectId());
            if (partners != null)
            {
                for (CFPartnerAnagraphs item : partners)
                {
                    partnerIds.add(item.getId());
                }
            }
        }
        if (this.getSessionBean().isDAEC())
        {
            loadAll = false;
            ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph()
                    .GetByUser(this.getCurrentUser().getId());
            if (cua != null)
            {
                List<CFPartnerAnagraphs> partnersLoc = BeansFactory
                        .CFPartnerAnagraphs().GetByDAEC(this.getProjectId(),
                                cua.getId());
                if (partnersLoc != null)
                {
                    for (CFPartnerAnagraphs item : partnersLoc)
                    {
                        partnerIds.add(item.getId());
                    }
                }
            }
        }
        if (loadAll)
        {
            setList(BeansFactory.LocalChecks().getByProject(
                    this.getProjectId(), closed));
        }
        else
        {
            setList(BeansFactory.LocalChecks().getByProjectAndPartners(
                    this.getProjectId(), partnerIds, closed));
        }
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        this.getSession().put("localCheck", null);
        this.getSession().put("localCheckView", null);
        
        this.goTo(PagesTypes.LOCALCHECKEDIT);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        try
        {
            BeansFactory.LocalChecks().Delete(this.getEntityDeleteId());
            this.Page_Load();
        }
        catch(HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        this.getSession().put("localCheck", this.getEntityEditId());
        this.getSession().put("localCheckView", null);
        
        this.goTo(PagesTypes.LOCALCHECKEDIT);
    }
    
    public void viewEntity()
    {
        this.getSession().put("localCheck", this.getEntityEditId());
        this.getSession().put("localCheckView", true);
        
        this.goTo(PagesTypes.LOCALCHECKEDIT);
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
        if (!this.getSessionBean().getIsActualSate()
                || this.getSessionBean().getIsProjectClosed())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    /**
     * Sets filterValue
     * 
     * @param filterValue
     *            the filterValue to set
     * @throws PersistenceBeanException
     */
    public void setFilterValue(String filterValue)
            throws PersistenceBeanException
    {
        this.getViewState().put("filterValue", filterValue);
        
        this.loadList();
    }
    
    /**
     * Gets filterValue
     * 
     * @return filterValue the filterValue
     */
    public String getFilterValue()
    {
        return (String) this.getViewState().get("filterValue");
    }
    
}
