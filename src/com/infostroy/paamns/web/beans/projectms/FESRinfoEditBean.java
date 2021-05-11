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
import com.infostroy.paamns.common.enums.FesrQuotaTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfoToResponsiblePeople;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurReimbursements;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.FESRInfo;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class FESRinfoEditBean extends EntityProjectEditBean<FESRInfo>
{
    private String  partner;
    
    private Integer number;
    
    private Boolean validationError;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        if (getValidationError() == null || !getValidationError())
        {
            this.GoBack();
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.DURREIMBURSEMENTEDIT);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("fesrinfo") != null)
        {
            this.setEntity(BeansFactory.FESRInfo().Load(
                    String.valueOf(this.getSession().get("fesrinfo"))));
            this.setPartner(this.getEntity().getFesrBf().getId().toString());
            
        }
        else
        {
            this.setEntity(new FESRInfo());
            this.getEntity()
                    .setQuota(FesrQuotaTypes.QuotaContoCapitale.getId());
        }
        
        DurInfos durInfo = BeansFactory.DurInfos().LoadByDurCompilation(
                Long.parseLong(String.valueOf(this.getSession().get(
                        "durReimbEdit"))));
        this.setNumber(durInfo.getDurInfoNumber());
        
        if (!this.isPostback())
        {
            if (this.getEntity().isNew())
            {
                fillReimbursementAmount();
            }
            else
            {
                this.setReimbursementAmount(getEntity()
                        .getReimbursementAmount());
            }
        }
        
        this.FillLists();
    }
    
    private void fillReimbursementAmount()
    {
        try
        {
            Double totalReimbursementAmount = 0d;
            List<FESRInfo> fesrs = BeansFactory.FESRInfo()
                    .LoadByDurReimbursment(
                            String.valueOf(this.getSession().get(
                                    "durReimbursement")));
            if (fesrs != null)
            {
                for (FESRInfo fesr : fesrs)
                {
                    if (fesr.getId().equals(this.getEntity().getId()))
                    {
                        continue;
                    }
                    if (fesr.getReimbursementAmount() != null)
                    {
                        totalReimbursementAmount += fesr
                                .getReimbursementAmount();
                    }
                }
            }
            
            DurSummaries durSummaries = BeansFactory.DurSummaries()
                    .LoadByDurCompilation(
                            Long.parseLong(String.valueOf(this.getSession()
                                    .get("durReimbEdit"))));
            Double totalDurAmount = 0d;
            
            List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
                    .GetByDurCompilation(
                            Long.parseLong(String.valueOf(this.getSession()
                                    .get("durReimbEdit"))));
            
            if (listCD != null && !listCD.isEmpty())
            {
                totalDurAmount = recalculateAmounts(listCD);
            }
            else
            {
                totalDurAmount = durSummaries.getTotalAmount() != null ? durSummaries
                        .getTotalAmount() : 0d;
            }
            
            Double frCnR = 0d;
            if (listCD != null && !listCD.isEmpty())
            {
                frCnR = NumberHelper.Round(recalculateFrReimb(listCD));
            }
            else
            {
                frCnR = durSummaries.getFrCnReimbursementAmount();
            }
            
            this.setReimbursementAmount(((totalDurAmount - totalReimbursementAmount) - frCnR));
            
            durSummaries = null;
            listCD = null;
        }
        catch (Exception e)
        {
            log.error(e);
        }
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
            
            this.getListPartners().add(
                    new SelectItem(String.valueOf(item.getPerson().getId()),
                            item.getPerson().getName()));
            
        }
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
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
    
    public Boolean BeforeSave() throws PersistenceBeanException
    {
        try
        {
            Double totalReimbursementAmount = 0d;
            List<FESRInfo> fesrs = BeansFactory.FESRInfo()
                    .LoadByDurReimbursment(
                            String.valueOf(this.getSession().get(
                                    "durReimbursement")));
            if (fesrs != null)
            {
                for (FESRInfo fesr : fesrs)
                {
                    if (fesr.getId().equals(this.getEntity().getId()))
                    {
                        continue;
                    }
                    if (fesr.getReimbursementAmount() != null)
                    {
                        totalReimbursementAmount += fesr
                                .getReimbursementAmount();
                    }
                }
            }
            
            DurSummaries durSummaries = BeansFactory.DurSummaries()
                    .LoadByDurCompilation(
                            Long.parseLong(String.valueOf(this.getSession()
                                    .get("durReimbEdit"))));
            
            List<CostDefinitions> listCD = BeansFactory.CostDefinitions()
                    .GetByDurCompilation(
                            Long.parseLong(String.valueOf(this.getSession()
                                    .get("durReimbEdit"))));
            
            Double totalAmoutDur = 0d;
            if (listCD != null && !listCD.isEmpty())
            {
                totalAmoutDur = recalculateAmounts(listCD);
            }
            else
            {
                totalAmoutDur = durSummaries.getTotalAmount() != null ? durSummaries
                        .getTotalAmount() : 0l;
            }
            
            Double frRAmount = 0d;
            
            if (listCD != null && !listCD.isEmpty())
            {
                frRAmount = NumberHelper.Round(recalculateFrReimb(listCD));
            }
            else
            {
                frRAmount = durSummaries.getFrCnReimbursementAmount() != null ? durSummaries
                        .getFrCnReimbursementAmount() : 0l;
            }
            
            if (getReimbursementAmount() != null)
            {
                totalReimbursementAmount += getReimbursementAmount();
            }
            
            if ((Math.abs(NumberHelper.Round(totalReimbursementAmount)) <= Math
                    .abs(NumberHelper.Round(totalAmoutDur) - frRAmount))
                    || (Math.abs(NumberHelper.Round(totalReimbursementAmount)
                            - (NumberHelper.Round(totalAmoutDur) - frRAmount)) <= 0.001))
            {
                return true;
            }
            else
            {
                this.setValidationError(true);
                return false;
            }
        }
        catch (Exception e)
        {
            
            return false;
        }
    }
    
    private Double recalculateFrReimb(List<CostDefinitions> cdList)
    {
        double amountFrCn = 0d;
        
        for (CostDefinitions cost : cdList)
        {
            
            if (cost.getCreatedByPartner())
            {
                List<CFPartnerAnagraphProject> partnerProjectList = null;
                try
                {
                    partnerProjectList = BeansFactory
                            .CFPartnerAnagraphProject()
                            .GetCFAnagraphForProjectAndUser(
                                    this.getProjectId(), cost.getUser().getId());
                }
                catch (Exception e)
                {
                    log.error(e);
                }
                
                if (partnerProjectList != null && !partnerProjectList.isEmpty())
                {
                    if (partnerProjectList.get(0).getCfPartnerAnagraphs()
                            .getCountry().getCode()
                            .equals(CountryTypes.Italy.getCountry()))
                    {
                        // amountItCn += (cost.getCfCheck() == null ? (cost
                        // .getCilCheck() == null ? 0d : cost
                        // .getCilCheck()) : cost.getCfCheck()) * 0.25;
                    }
                    else
                    {
                        amountFrCn += (cost.getCfCheck() == null ? (cost
                                .getCilCheck() == null ? 0d : cost
                                .getCilCheck()) : cost.getCfCheck()) * 0.25;
                    }
                }
            }
            else
            {
                if (cost.getCreatedByAGU())
                {
                    // amountItCn += (cost.getCilCheck() == null ? 0d : cost
                    // .getCilCheck()) * 0.25;
                }
                else
                {
                    List<CFPartnerAnagraphProject> partnerProjectList = null;
                    try
                    {
                        partnerProjectList = BeansFactory
                                .CFPartnerAnagraphProject()
                                .GetCFAnagraphForProjectAndUser(
                                        this.getProjectId(),
                                        cost.getUser().getId());
                    }
                    catch (Exception e)
                    {
                        log.error(e);
                    }
                    if (partnerProjectList != null
                            && !partnerProjectList.isEmpty())
                    {
                        if (partnerProjectList.get(0).getCfPartnerAnagraphs()
                                .getCountry().getCode()
                                .equals(CountryTypes.Italy.getCountry()))
                        {
                            // amountItCn += (cost.getCilCheck() == null ?
                            // 0d
                            // : cost.getCilCheck()) * 0.25;
                        }
                        else
                        {
                            amountFrCn += (cost.getCilCheck() == null ? 0d
                                    : cost.getCilCheck()) * 0.25;
                        }
                    }
                    
                }
            }
            
        }
        return amountFrCn;
    }
    
    private double recalculateAmounts(List<CostDefinitions> cdList)
            throws HibernateException, PersistenceBeanException
    {
        double amountTotal = 0d;
        
        for (CostDefinitions cost : cdList)
        {
            if (cost.getACUSertified() && cost.getAcuCertification() != null)
            {
                amountTotal += cost.getAcuCertification();
            }
            else if (cost.getAGUSertified()
                    && cost.getAguCertification() != null)
            {
                amountTotal += cost.getAguCertification();
            }
            else if (cost.getSTCSertified()
                    && cost.getStcCertification() != null)
            {
                amountTotal += cost.getStcCertification();
            }
            else if (cost.getCfCheck() != null)
            {
                amountTotal += cost.getCfCheck();
            }
            else if (cost.getCilCheck() != null)
            {
                amountTotal += cost.getCilCheck();
            }
            
        }
        
        return amountTotal;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        FESRInfo itemToSave = new FESRInfo();
        if (this.getSession().get("fesrinfo") != null)
        {
            itemToSave = BeansFactory.FESRInfo().Load(
                    String.valueOf(this.getSession().get("fesrinfo")));
        }
        
        itemToSave.setDurReimbursements(BeansFactory.DurReimbursements().Load(
                String.valueOf(this.getSession().get("durReimbursement"))));
        if (this.getPartner() != null && !this.getPartner().isEmpty())
        {
            itemToSave.setFesrBf(BeansFactory.CFPartnerAnagraphs().Load(
                    this.getPartner()));
        }
        itemToSave.setFesrBfIban(this.getEntity().getFesrBfIban());
        itemToSave.setFesrCro(this.getEntity().getFesrCro());
        itemToSave.setFesrDate(this.getEntity().getFesrDate());
        itemToSave.setFesrPaymentAct(this.getEntity().getFesrPaymentAct());
        itemToSave.setFesrReimbursmentNumber(this.getNumber());
        itemToSave.setFesrTotalAmount(this.getEntity().getFesrTotalAmount());
        itemToSave.setReimbursementAmount(getReimbursementAmount());
        itemToSave.setModeOfCreditingFunds(this.getEntity()
                .getModeOfCreditingFunds());
        itemToSave.setNumberMandatePayment(this.getEntity()
                .getNumberMandatePayment());
        itemToSave.setHowToPay(this.getEntity().getHowToPay());
        itemToSave.setQuota(this.getEntity().getQuota());
        itemToSave.setNumberSettlementAct(this.getEntity()
                .getNumberSettlementAct());
        
        BeansFactory.FESRInfo().SaveInTransaction(itemToSave);
    }
    
    /**
     * Sets listPartners
     * 
     * @param listPartners
     *            the listPartners to set
     */
    public void setListPartners(List<SelectItem> listPartners)
    {
        this.getViewState().put("listPartners", listPartners);
    }
    
    /**
     * Gets listPartners
     * 
     * @return listPartners the listPartners
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getListPartners()
    {
        return (List<SelectItem>) this.getViewState().get("listPartners");
        
    }
    
    /**
     * Sets partner
     * 
     * @param partner
     *            the partner to set
     */
    public void setPartner(String partner)
    {
        this.partner = partner;
    }
    
    /**
     * Gets partner
     * 
     * @return partner the partner
     */
    public String getPartner()
    {
        return partner;
    }
    
    /**
     * Sets number
     * 
     * @param number
     *            the number to set
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }
    
    /**
     * Gets number
     * 
     * @return number the number
     */
    public Integer getNumber()
    {
        return number;
    }
    
    /**
     * Gets validationError
     * 
     * @return validationError the validationError
     */
    public Boolean getValidationError()
    {
        return validationError;
    }
    
    /**
     * Sets validationError
     * 
     * @param validationError
     *            the validationError to set
     */
    public void setValidationError(Boolean validationError)
    {
        this.validationError = validationError;
    }
    
    /**
     * Gets reimbursementAmount
     * 
     * @return reimbursementAmount the reimbursementAmount
     */
    public Double getReimbursementAmount()
    {
        if (this.getViewState().containsKey("reimbursementAmount"))
        {
            return (Double) getViewState().get("reimbursementAmount");
        }
        return null;
    }
    
    /**
     * Sets reimbursementAmount
     * 
     * @param reimbursementAmount
     *            the reimbursementAmount to set
     */
    public void setReimbursementAmount(Double reimbursementAmount)
    {
        getViewState().put("reimbursementAmount", reimbursementAmount);
    }
}
