/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;
import com.infostroy.paamns.persistence.beans.entities.domain.RotationFoundInfo;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class RotationFoundInfoEditBean extends
        EntityProjectEditBean<RotationFoundInfo>
{
    private String  partner;
    
    private Integer number;
    
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
        this.goTo(PagesTypes.DURREIMBURSEMENTEDIT);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("rotationfoundinfo") != null)
        {
            this.setEntity(BeansFactory.RotationFoundInfo().Load(
                    String.valueOf(this.getSession().get("rotationfoundinfo"))));
            this.setPartner(this.getEntity().getRotationPartner().getId()
                    .toString());
        }
        else
        {
            this.setEntity(new RotationFoundInfo());
        }
        
        DurInfos durInfo = BeansFactory.DurInfos().LoadByDurCompilation(
                Long.parseLong(String.valueOf(this.getSession().get(
                        "durReimbEdit"))));
        this.setNumber(durInfo.getDurInfoNumber());
        
        this.FillLists();
    }
    
    private void FillLists() throws HibernateException, NumberFormatException,
            PersistenceBeanException
    {
        DurReimbursements durReim = BeansFactory.DurReimbursements().Load(
                String.valueOf(this.getSession().get("durReimbursement")));
        List<DurInfoToResponsiblePeople> list = BeansFactory
                .DurInfoToResponsiblePeople().getByDurInfo(
                        BeansFactory
                                .DurInfos()
                                .LoadByDurCompilation(
                                        durReim.getDurCompilation().getId())
                                .getId());
        
        if (this.getListPartners() == null)
        {
            this.setListPartners(new ArrayList<SelectItem>());
        }
        else
        {
            this.getListPartners().clear();
        }
        this.getListPartners().add(SelectItemHelper.getFirst());
        for (DurInfoToResponsiblePeople item : list)
        {
            if (item.getPerson().getCountry().getCode()
                    .equals(CountryTypes.Italy.getCountry()))
            {
                this.getListPartners().add(
                        new SelectItem(
                                String.valueOf(item.getPerson().getId()), item
                                        .getPerson().getName()));
            }
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
     */
    @Override
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
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        RotationFoundInfo itemToSave = new RotationFoundInfo();
        if (this.getSession().get("rotationfoundinfo") != null)
        {
            itemToSave = BeansFactory.RotationFoundInfo().Load(
                    String.valueOf(this.getSession().get("rotationfoundinfo")));
        }
        
        itemToSave.setDurReimbursements(BeansFactory.DurReimbursements().Load(
                String.valueOf(this.getSession().get("durReimbursement"))));
        if (this.getPartner() != null && !this.getPartner().isEmpty())
        {
            itemToSave.setRotationPartner(BeansFactory.CFPartnerAnagraphs()
                    .Load(this.getPartner()));
        }
        itemToSave.setRotationPartnerIban(this.getEntity()
                .getRotationPartnerIban());
        itemToSave.setRptationCro(this.getEntity().getRptationCro());
        itemToSave.setRotationDate(this.getEntity().getRotationDate());
        itemToSave.setRotationPaymentAct(this.getEntity()
                .getRotationPaymentAct());
        itemToSave.setRotationReimbursmentNumber(this.getNumber());
        itemToSave.setRotationTotalAmount(this.getEntity()
                .getRotationTotalAmount());
        
        BeansFactory.RotationFoundInfo().SaveInTransaction(itemToSave);
    }
    
    /**
     * Sets listPartners
     * @param listPartners the listPartners to set
     */
    public void setListPartners(List<SelectItem> listPartners)
    {
        this.getViewState().put("listPartners", listPartners);
    }
    
    /**
     * Gets listPartners
     * @return listPartners the listPartners
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getListPartners()
    {
        return (List<SelectItem>) this.getViewState().get("listPartners");
        
    }
    
    /**
     * Sets partner
     * @param partner the partner to set
     */
    public void setPartner(String partner)
    {
        this.partner = partner;
    }
    
    /**
     * Gets partner
     * @return partner the partner
     */
    public String getPartner()
    {
        return partner;
    }
    
    /**
     * Sets number
     * @param number the number to set
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }
    
    /**
     * Gets number
     * @return number the number
     */
    public Integer getNumber()
    {
        return number;
    }
}
