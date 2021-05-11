/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.BudgetInputSourceDivided;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.facades.BudgetInputSourceDividedBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 *
 * @author Sergey Zorin InfoStroy Co., 2010.
 * @author Vito Rifino - Ingloba360 srl, 2017.
 *
 */
public class GeneralBudgetViewBean extends
        EntityProjectEditBean<GeneralBudgets>
{
    @SuppressWarnings("unused")
    private boolean                  isEdit;
    
    private boolean                  editAvailable;
    
    private BudgetInputSourceDivided sourceBudget;
    
    @SuppressWarnings("unused")
    private String                   spanValidatorVisibility;
    
    public Boolean BeforeSave() throws PersistenceBeanException
    {
        double FESRSum = 0;
        double CNPublicSum = 0;
        double CNPrivateSum = 0;
        
        for (GeneralBudgets gb : this.getListGeneralBudgets().subList(0,
                this.getListGeneralBudgets().size() - 1))
        {
            if (gb.getCfPartnerAnagraph() != null)
            {
                if (gb.getFesr() != null)
                {
                    FESRSum += gb.getFesr();
                }
                if (gb.getCnPublic() != null)
                {
                    CNPublicSum += gb.getCnPublic();
                }
                if (gb.getQuotaPrivate() != null)
                {
                    CNPrivateSum += gb.getQuotaPrivate();
                }
            }
        }
        
        this.DeleteSummery();
        this.AddSummery();
        
        if (NumberHelper.Round(FESRSum) == this.sourceBudget.getFesr()
                && NumberHelper.Round(CNPublicSum) == this.sourceBudget
                        .getCnPublic()
                && NumberHelper.Round(CNPrivateSum) == this.sourceBudget
                        .getCnPrivate())
        {
            this.setSpanValidatorVisibility("none");
            return true;
        }
        else
        {
            this.setSpanValidatorVisibility("");
            return false;
        }
    }
    
    private void DeleteSummery()
    {
        Collections.sort(this.getListGeneralBudgets(),
                new GeneralBudgetComparer());
        this.setListGeneralBudgets(this.getListGeneralBudgets().subList(0,
                this.getListGeneralBudgets().size() - 1));
    }
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        this.setIsEdit(false);
        this.setIsPreSaved(false);
        this.setListGeneralBudgets(null);
        try
        {
            this.Page_Load();
        }
        catch(NumberFormatException e)
        {
            log.error(e);
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.setIsNew(false);
        boolean isPartnerView;
        this.setListOldBudgets(BeansFactory.GeneralBudgets()
                .GetItemsForProject(Long.valueOf(this.getProjectId())));
        if (this.getSessionBean().isPartner() && !this.getSessionBean().isCF()
                && !this.getSessionBean().isAAU()
                && !this.getSessionBean().isAGU())
        {
            isPartnerView = true;
            this.setEditAvailable(false);
        }
        else
        {
            isPartnerView = false;
        }
        
        List<CFPartnerAnagraphs> listPartners = BeansFactory
                .CFPartnerAnagraphs().GetCFAnagraphForProject(
                        this.getProjectId(), null);
        
        if (listPartners == null || listPartners.isEmpty())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        this.setEditAvailable(false);
        for (CFPartnerAnagraphs partner : listPartners)
        {
            List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
                    .GetByPartner(this.getProjectId(),
                            partner.getId().toString(), null);
            
            if ((listPB == null || listPB.isEmpty()) && !isPartnerView)
            {
                this.setEditAvailable(true);
                break;
            }
        }
        
        if (this.getSession().get("project") != null)
        {
            this.setSpanValidatorVisibility("none");
            
            List<BudgetInputSourceDivided> listSourceBudget = BudgetInputSourceDividedBean
                    .GetByProject(this.getProjectId());
            
            if (listSourceBudget.size() > 0)
            {
                this.sourceBudget = listSourceBudget.get(0);
            }
            else
            {
                this.goTo(PagesTypes.PROJECTINDEX);
            }
            
            // If budgets for some CF Anagraph does not exists, create him            
            
            if (getListGeneralBudgets() == null
                    || getListGeneralBudgets().isEmpty())
            {
                List<GeneralBudgets> listAux = BeansFactory.GeneralBudgets()
                        .GetItemsForProject(
                                Long.valueOf(String.valueOf(this.getSession()
                                        .get("project"))));
                this.setListGeneralBudgets(new ArrayList<GeneralBudgets>());
                for (GeneralBudgets gb : listAux)
                {
                    this.getListGeneralBudgets().add(gb.clone());
                }
            }
            else
            {
                this.DeleteSummery();
            }
            
            List<Long> listCFAnagraphsIndices = new ArrayList<Long>();
            
            for (GeneralBudgets gb : this.getListGeneralBudgets())
            {
                if (gb.getCfPartnerAnagraph() != null)
                {
                    listCFAnagraphsIndices.add(gb.getCfPartnerAnagraph()
                            .getId());
                }
            }
            
            List<CFPartnerAnagraphs> listCFAnagraphs = BeansFactory
                    .CFPartnerAnagraphs().LoadByProject(
                            String.valueOf(this.getSession().get("project")));
            
            if (listCFAnagraphs == null)
            {
                listCFAnagraphs = new ArrayList<CFPartnerAnagraphs>();
            }
            
            int count = 0;
            for (CFPartnerAnagraphs cfAnagraph : listCFAnagraphs)
            {
                if (!listCFAnagraphsIndices.contains(cfAnagraph.getId()))
                {
                    GeneralBudgets newEntity = GeneralBudgets.create(this.getProject(), cfAnagraph);                                        
                    
                    this.getListGeneralBudgets().add(newEntity);
                }
                
            }
            
            for (GeneralBudgets gb : this.getListGeneralBudgets())
            {
                if (gb.getBudgetTotal() == null)
                {
                    count++;
                }
            }
            
            if (this.getListGeneralBudgets().size() == count)
            {
                setIsNew(true);
            }
            else
            {
                setIsNew(false);
            }
            
            // Sort by type
            Collections.sort(this.getListGeneralBudgets(),
                    new GeneralBudgetComparer());
            
            this.AddSummery();
        }
        
    }
    
    private void AddSummery()
    {
        double sum_t = 0d;
        double sum_f = 0d;
        double sum_pub = 0d;
        double sum_priv = 0d;        
        double sumPublicOther = 0d;
        double sumPrivateReal = 0d;
        double sumNetRevenue = 0d;
        
        for (GeneralBudgets gb : this.getListGeneralBudgets())
        {
            sum_t += gb.getBudgetTotal() != null ? gb.getBudgetTotal() : 0d;
            sum_f += gb.getFesr() != null ? gb.getFesr() : 0d;
            sum_pub += gb.getCnPublic() != null ? gb.getCnPublic() : 0d;
            sum_priv += gb.getQuotaPrivate() != null ? gb.getQuotaPrivate() : 0d;            
            sumPublicOther += gb.getCnPublicOther() != null ? gb.getCnPublicOther() : 0d;
            sumPrivateReal += gb.getCnPrivateReal() != null ? gb.getCnPrivateReal() : 0d;
            sumNetRevenue += gb.getNetRevenue() != null ? gb.getNetRevenue() : 0d;
        }        
        
        GeneralBudgets newEntity = new GeneralBudgets();
        newEntity.setBudgetTotal(sum_t);
        newEntity.setCnPublic(sum_pub);
        newEntity.setQuotaPrivate(sum_priv);
        newEntity.setFesr(sum_f);
        newEntity.setCnPublicOther(sumPublicOther);
        newEntity.setCnPrivateReal(sumPrivateReal);
        newEntity.setNetRevenue(sumNetRevenue);
                
        this.getListGeneralBudgets().add(this.getListGeneralBudgets().size(),
                newEntity);
        Collections.sort(this.getListGeneralBudgets(),
                new GeneralBudgetComparer());
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException,
            PersistenceBeanException
    {
        if (!AccessGrantHelper.getIsAGUAsse5())
        {
            if (BudgetInputSourceDividedBean.GetByProject(this.getProjectId())
                    .size() < 1)
            {
                this.goTo(PagesTypes.PROJECTINDEX);
            }
        }
        
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException, PersistenceBeanException
    {
        for (GeneralBudgets gb : this.getListOldBudgets())
        {
            if (gb.getCfPartnerAnagraph() != null)
            {
                gb.setIsOld(true);
                BeansFactory.GeneralBudgets().SaveInTransaction(gb);
            }
        }
        
        for (GeneralBudgets gb : this.getListGeneralBudgets())
        {
            if (gb.getCfPartnerAnagraph() != null)
            {
                gb.setIsOld(false);
//                gb.setBudgetTotal(NumberHelper.Round(gb.getFesr()
//                        + gb.getCnPublic() + gb.getQuotaPrivate()));
                gb.setBudgetTotal(NumberHelper.Round(gb.getFesr()+gb.getCnPublic()+gb.getCnPrivateReal()+gb.getQuotaPrivate()+gb.getNetRevenue())); //+gb.getCnPublicOther()
                BeansFactory.GeneralBudgets().SaveInTransaction(gb);
            }
        }
        
        List<PartnersBudgets> listPB = BeansFactory.PartnersBudgets()
                .GetAllByProject(this.getProjectId());
        for (PartnersBudgets budget : listPB)
        {
            BeansFactory.PartnersBudgets().DeleteInTransaction(budget);
        }
    }
    
    private boolean CheckModifications()
    {
        boolean isModified = false;
        
        Collections.sort(this.getListOldBudgets(), new GeneralBudgetComparer());
        Collections.sort(this.getListGeneralBudgets(),
                new GeneralBudgetComparer());
        
        if (this.getListOldBudgets() != null
                && this.getListOldBudgets().size() != this
                        .getListGeneralBudgets().size())
        {
            return true;
        }
        
        int counter = -1;
        for (GeneralBudgets gb : this.getListOldBudgets())
        {
            counter++;
            
            if (gb.getCnPublic() != null
                    && this.getListGeneralBudgets().get(counter).getCnPublic() != null
                    && !gb.getCnPublic().equals(
                            this.getListGeneralBudgets().get(counter)
                                    .getCnPublic()))
            {
                isModified = true;
                break;
            }
            if (gb.getQuotaPrivate() != null
                    && this.getListGeneralBudgets().get(counter)
                            .getQuotaPrivate() != null
                    && !gb.getQuotaPrivate().equals(
                            this.getListGeneralBudgets().get(counter)
                                    .getQuotaPrivate()))
            {
                isModified = true;
                break;
            }
            if (gb.getFesr() != null
                    && this.getListGeneralBudgets().get(counter).getFesr() != null
                    && !gb.getFesr()
                            .equals(this.getListGeneralBudgets().get(counter)
                                    .getFesr()))
            {
                isModified = true;
                break;
            }
        }
        
        return isModified;
    }
    
    public void preSave()
    {
        this.setIsPreSaved(true);
    }
    
    public boolean getIsModified()
    {
        try
        {
            if (this.getIsEdit())
            {
                if (this.getIsNew())
                {
                    if (this.getIsPreSaved() && this.BeforeSave())
                    {
                        Transaction tr = PersistenceSessionManager.getBean()
                                .getSession().beginTransaction();
                        SaveEntity();
                        tr.commit();
                        this.GoBack();
                        return false;
                    }
                    else
                    {
                        return false;
                    }
                }
                
                boolean isModified = false;
                
                if (!this.BeforeSave())
                {
                    return false;
                }
                
                if (!this.getIsPreSaved())
                {
                    return false;
                }
                
                isModified = this.CheckModifications();
                
                if (!isModified)
                {
                    //SaveEntity();
                    
                    this.GoBack();
                }
                
                return isModified;
            }
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
        
        return false;
    }
    
    public void editItem() throws PersistenceBeanException
    {
        PersistenceSessionManager.getBean().getSession().clear();
        this.setIsEdit(true);
    }
    
    /**
     * Sets isEdit
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
        this.getViewState().put("generalBudgetEdit", isEdit);
    }
    
    /**
     * Gets isEdit
     * @return isEdit the isEdit
     */
    public boolean getIsEdit()
    {
        if (this.getViewState().get("generalBudgetEdit") != null)
        {
            return Boolean.valueOf(String.valueOf(this.getViewState().get(
                    "generalBudgetEdit")));
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Sets listGeneralBudgets
     * @param listGeneralBudgets the listGeneralBudgets to set
     */
    public void setListGeneralBudgets(List<GeneralBudgets> listGeneralBudgets)
    {
        this.getViewState().put("listGeneralBudgets", listGeneralBudgets);
    }
    
    /**
     * Gets listGeneralBudgets
     * @return listGeneralBudgets the listGeneralBudgets
     */
    @SuppressWarnings("unchecked")
    public List<GeneralBudgets> getListGeneralBudgets()
    {
        return (List<GeneralBudgets>) this.getViewState().get(
                "listGeneralBudgets");
    }
    
    /**
     * Sets sourceBudget
     * @param sourceBudget the sourceBudget to set
     */
    public void setSourceBudget(BudgetInputSourceDivided sourceBudget)
    {
        this.sourceBudget = sourceBudget;
    }
    
    /**
     * Gets sourceBudget
     * @return sourceBudget the sourceBudget
     */
    public BudgetInputSourceDivided getSourceBudget()
    {
        return sourceBudget;
    }
    
    /**
     * Sets spanValidatorVisibility
     * @param spanValidatorVisibility the spanValidatorVisibility to set
     */
    public void setSpanValidatorVisibility(String spanValidatorVisibility)
    {
        this.getViewState().put("generalBudgetEditSpanVisibility",
                spanValidatorVisibility);
    }
    
    /**
     * Gets spanValidatorVisibility
     * @return spanValidatorVisibility the spanValidatorVisibility
     */
    public String getSpanValidatorVisibility()
    {
        if (this.getViewState().get("generalBudgetEditSpanVisibility") != null)
        {
            return String.valueOf(this.getViewState().get(
                    "generalBudgetEditSpanVisibility"));
        }
        else
        {
            return "none";
        }
    }
    
    /**
     * Sets editAvailable
     * @param editAvailable the editAvailable to set
     */
    public void setEditAvailable(boolean editAvailable)
    {
        this.editAvailable = editAvailable;
    }
    
    /**
     * Gets editAvailable
     * @return editAvailable the editAvailable
     */
    public boolean getEditAvailable()
    {
        return editAvailable;
    }
    
    /**
     * Sets isNew
     * @param isNew the isNew to set
     */
    public void setIsNew(boolean isNew)
    {
        this.getViewState().put("isNew", isNew);
    }
    
    /**
     * Gets isNew
     * @return isNew the isNew
     */
    public Boolean getIsNew()
    {
        return (Boolean) this.getViewState().get("isNew");
    }
    
    /**
     * Sets isNew
     * @param isNew the isNew to set
     */
    public void setIsPreSaved(boolean isNew)
    {
        this.getViewState().put("IsPreSaved", isNew);
    }
    
    /**
     * Gets isNew
     * @return isNew the isNew
     */
    public Boolean getIsPreSaved()
    {
        if (this.getViewState().get("IsPreSaved") == null)
        {
            return false;
        }
        return (Boolean) this.getViewState().get("IsPreSaved");
    }
    
    /**
     * Sets listOldBudgets
     * @param listOldBudgets the listOldBudgets to set
     */
    public void setListOldBudgets(List<GeneralBudgets> listOldBudgets)
    {
        this.getViewState().put("listOldBudgets", listOldBudgets);
    }
    
    /**
     * Gets listOldBudgets
     * @return listOldBudgets the listOldBudgets
     */
    @SuppressWarnings("unchecked")
    public List<GeneralBudgets> getListOldBudgets()
    {
        return (List<GeneralBudgets>) this.getViewState().get("listOldBudgets");
    }
    
    public class GeneralBudgetComparer implements Comparator<GeneralBudgets>
    {
        @Override
        public int compare(GeneralBudgets arg0, GeneralBudgets arg1)
        {
            if (arg0.getCfPartnerAnagraph() == null)
            {
                return 1;
            }
            if (arg1.getCfPartnerAnagraph() == null)
            {
                return -1;
            }
            CFPartnerAnagraphProject p1 = null;
            try
            {
                p1 = BeansFactory.CFPartnerAnagraphProject().LoadByPartner(
                        arg0.getProject().getId(),
                        arg0.getCfPartnerAnagraph().getId());
            }
            catch(PersistenceBeanException e)
            {
            }
            CFPartnerAnagraphProject p2 = null;
            try
            {
                p2 = BeansFactory.CFPartnerAnagraphProject().LoadByPartner(
                        arg1.getProject().getId(),
                        arg1.getCfPartnerAnagraph().getId());
            }
            catch(PersistenceBeanException e)
            {
            }
            return (p1.getType().getValue().compareTo(p2.getType().getValue()));
        }
        
    }
    
    public class GeneralBudgetsComparator implements Comparator<GeneralBudgets>
    {
        @Override
        public int compare(GeneralBudgets arg0, GeneralBudgets arg1)
        {
            return arg0.getCfPartnerAnagraph().getId()
                    .compareTo(arg1.getCfPartnerAnagraph().getId());
        }
    }
}
