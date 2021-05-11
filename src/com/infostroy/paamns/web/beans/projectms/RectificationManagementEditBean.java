package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.Recoveries;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.NotRegularTypes;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 * 
 * @author Vladimir Zrazhevskiy InfoStroy Co., 2011.
 * 
 */

public class RectificationManagementEditBean extends
        EntityEditBean<CostDefinitions>
{
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    private int              selectedIndex;
    
    private Long             entityId;
    
    private Double           amountCertificate;
    
    private Double           amountRectified;
    
    private Boolean          editMode;
    
    private List<SelectItem> notRegularTypes;
    
    private String           notRegularType;
    
    private String           notRegularChildType;
    
    private List<SelectItem> notRegularChildTypes;
    
    private Double           irregularAmount;
    
    private Integer          referenceYear;
    
    private Boolean          certification;
    
    private Boolean          beRecovered;
    
    private Date             recoveryDate;
    
    private Double           amountRecovery;
    
    private Date             retreatDate;
    
    private Double           amountRetreat;
    
    private Integer          durNumber;
    
    // Recoveries feature
    private Double           amountRecovered;
    
    private Date             dateAmountRecovered;
    
    private String           reasonOfRecovery;
    
    private Boolean          rectificationConfirmed;
    
    private List<Recoveries> listRecoveries;
    
    private Integer          itemsPerPageList;
    
    private Double           sumRecoveries;
    
    //
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, IOException, NullPointerException
    {
        parseSessionParameters();
        
        loadNotRegular();
        
        if (this.getDateAmountRecovered() == null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR, 11);
            this.setDateAmountRecovered(calendar.getTime());
        }
        
        if (this.getAmountRecovered() == null)
        {
            this.setAmountRecovered(0d);
        }
        
        try
        {
            DurInfos durInfo = BeansFactory.DurInfos().LoadByDurCompilation(
                    getEntity().getDurCompilation().getId());
            
            this.setDurNumber(durInfo.getDurInfoNumber());
        }
        catch(Exception ex)
        {
            log.error(ex);
        }
        
        if (getEntity().getRectifiedBySTC())
        {
            this.amountCertificate = getEntity().getCilCheck();
        }
        else if (getEntity().getRectifiedByAGU())
        {
            this.amountCertificate = getEntity().getStcCertification();
        }
        else if (getEntity().getRectifiedByACU())
        {
            this.amountCertificate = getEntity().getAguCertification();
        }
        
        List<NotRegularTypes> listTypes = BeansFactory.NotRegularTypes()
                .LoadParent();
        
        this.notRegularTypes = new ArrayList<SelectItem>();
        this.notRegularTypes.add(SelectItemHelper.getFirst());
        
        for (NotRegularTypes type : listTypes)
        {
            SelectItem item = new SelectItem();
            item.setLabel(type.getValue());
            item.setValue(String.valueOf(type.getId()));
            this.notRegularTypes.add(item);
        }
        this.setEditMode(!(Boolean) this.getViewState().get(
                "rectificationManagementEditShow"));
        //
        this.notRegularTypes = new ArrayList<SelectItem>();
        this.notRegularTypes.add(SelectItemHelper.getFirst());
        
        for (NotRegularTypes type : listTypes)
        {
            SelectItem item = new SelectItem();
            item.setLabel(type.getValue());
            item.setValue(String.valueOf(type.getId()));
            this.notRegularTypes.add(item);
        }
        
        if (this.getNotRegularType() != null
                && !this.getNotRegularType().isEmpty())
        {
            this.FillNotRegularTypeChild(BeansFactory.NotRegularTypes().Load(
                    this.getNotRegularType()));
        }
        else
        {
            this.notRegularChildTypes = new ArrayList<SelectItem>();
            this.notRegularChildTypes.add(SelectItemHelper.getFirst());
        }
        
        if (this.getEntity().getRectificationConfirmed() != null
                && this.getEntity().getRectificationConfirmed())
        {
            this.setRectificationConfirmed(true);
        }
        else
        {
            this.setRectificationConfirmed(false);
        }
        
        if (getPrevRectAmount() == null)
        {
            setPrevRectAmount(this.getEntity().getRectificationAmount());
        }
        
        this.fillRecoveriesList();
        
    }
    
    private void loadNotRegular()
    {
        this.setIrregularAmount(getEntity().getIrregularAmount());
        this.setBeRecovered(getEntity().getBeRecovered());
        
        this.setDurNumber(getEntity().getDurNumber());
        this.setReferenceYear(getEntity().getReferenceYear());
        this.setCertification(getEntity().getCertification());
        
        this.setAmountRecovery(getEntity().getAmountRecovery());
        this.setAmountRetreat(getEntity().getAmountRetreat());
        this.setRecoveryDate(getEntity().getRecoveryDate());
        this.setRetreatDate(getEntity().getRetreatDate());
        
    }
    
    private void fillRecoveriesList() throws PersistenceBeanException
    {
        this.setListRecoveries(BeansFactory.Recoveries().loadByCostDefinition(
                this.getEntity().getId()));
        
        sumRecoveries = 0d;
        for (Recoveries item : this.getListRecoveries())
        {
            if (item.getAmountRecovered() != null)
            {
                sumRecoveries += item.getAmountRecovered();
            }
        }
    }
    
    private void parseSessionParameters() throws HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().containsKey("rectificationManagementEdit"))
        {
            Long id = (Long) this.getSession().get(
                    "rectificationManagementEdit");
            this.setEntity(BeansFactory.CostDefinitions().Load(id));
            this.getViewState().put("rectificationManagementEdit",
                    this.getSession().get("rectificationManagementEdit"));
            // this.getSession().remove("rectificationManagementEdit");
            
        }
        else
        {
            if (!this.getViewState().containsKey("rectificationManagementEdit"))
            {
                this.GoBack();
            }
        }
        
        if (!this.getViewState().containsKey("rectificationManagementEditShow"))
        {
            Boolean show = true;
            if (this.getSession()
                    .containsKey("rectificationManagementEditShow"))
            {
                show = (Boolean) this.getSession().get(
                        "rectificationManagementEditShow");
                // this.getSession().remove("rectificationManagementEditShow");
            }
            this.getViewState().put("rectificationManagementEditShow", show);
            
        }
        
        if (!this.getViewState().containsKey("rectificationManagementEditBack"))
        {
            Boolean back = false;
            if (this.getSession()
                    .containsKey("rectificationManagementEditBack"))
            {
                back = (Boolean) this.getSession().get(
                        "rectificationManagementEditBack");
                // this.getSession().remove("rectificationManagementEditBack");
            }
            this.getViewState().put("rectificationManagementEditBack", back);
        }
        /*
         * if(this.getV().containsKey("rectificationManagementEdit")) {
         * 
         * } else { goTo(PagesTypes.RECTIFICATIONMANAGEMENTLIST); }
         */
        // BeansFactory.CostDefinitions().Load(id)
    }
    
    public void addRecoveries() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            NullPointerException, IOException
    {
        if (this.getAmountRecovered() == null)
        {
            return;
        }
        if (this.getSumRecoveries() + this.getAmountRecovered() <= this
                .getEntity().getRectificationAmount())
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.getDateAmountRecovered());
            calendar.set(Calendar.HOUR, 11);
            
            if (this.getReasonOfRecovery().length() > 254)
            {
                this.setReasonOfRecovery(this.getReasonOfRecovery().substring(
                        0, 254));
            }
            
            Recoveries recoveries = new Recoveries();
            recoveries.setAmountRecovered(this.getAmountRecovered());
            recoveries.setDateAmountRecovered(calendar.getTime());
            recoveries.setReasonOfRecovery(this.getReasonOfRecovery());
            recoveries.setCostDefinition(this.getEntity());
            BeansFactory.Recoveries().Save(recoveries);
            
            this.setErrorRecoverAmount(false);
        }
        else
        {
            this.setErrorRecoverAmount(true);
        }
        
        this.Page_Load();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        if (this.getPrevRectAmount() >= this.getEntity()
                .getRectificationAmount())
        {
            BeansFactory.CostDefinitions().Save(this.getEntity());
            this.setErrorRectifyAmount(false);
        }
        else
        {
            this.setErrorRectifyAmount(true);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        /*
         * if (this.getViewState().get("rectificationManagementEditBack") !=
         * null) { boolean value =
         * Boolean.valueOf(String.valueOf(this.getSession()
         * .get("rectificationManagementEditBack")));
         * this.getViewState().remove("rectificationManagementEditBack"); if
         * (value) { this.goTo(PagesTypes.RECTIFICATIONMANAGEMENTLIST); } else {
         * this.goTo(PagesTypes.COSTCERTIFICATIONLIST); } }
         */
        this.goTo(PagesTypes.RECTIFICATIONMANAGEMENTLIST);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    public void notRegularTypeChanged(ValueChangeEvent event)
            throws HibernateException, PersistenceBeanException
    {
        if (event.getNewValue() != null)
        {
            this.setNotRegularType((String) event.getNewValue());
            this.FillNotRegularTypeChild(BeansFactory.NotRegularTypes().Load(
                    (String) event.getNewValue()));
        }
    }
    
    public void FillNotRegularTypeChild(NotRegularTypes parent)
            throws HibernateException, PersistenceBeanException
    {
        if (this.notRegularChildTypes == null)
        {
            this.notRegularChildTypes = new ArrayList<SelectItem>();
        }
        else
        {
            this.notRegularChildTypes.clear();
        }
        
        this.notRegularChildTypes.add(SelectItemHelper.getFirst());
        
        for (NotRegularTypes type : parent.getChilds())
        {
            this.notRegularChildTypes.add(new SelectItem(String.valueOf(type
                    .getId()), type.getValue()));
        }
    }
    
    public int getSelectedIndex()
    {
        return selectedIndex;
    }
    
    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }
    
    public Long getEntityId()
    {
        return entityId;
    }
    
    public void setEntityId(Long entityId)
    {
        this.entityId = entityId;
    }
    
    public Double getAmountCertificate()
    {
        return amountCertificate;
    }
    
    public void setAmountCertificate(Double amountCertificate)
    {
        this.amountCertificate = amountCertificate;
    }
    
    public Double getAmountRectified()
    {
        return amountRectified;
    }
    
    public void setAmountRectified(Double amountRectified)
    {
        this.amountRectified = amountRectified;
    }
    
    public Boolean getEditMode()
    {
        return editMode;
    }
    
    public void setEditMode(Boolean editMode)
    {
        this.editMode = editMode;
    }
    
    public void setNotRegularType(String notRegularType)
    {
        this.getViewState().put("notRegularType", notRegularType);
        this.notRegularType = notRegularType;
    }
    
    public String getNotRegularType()
    {
        if (this.getViewState().get("notRegularType") != null)
        {
            return (String) this.getViewState().get("notRegularType");
        }
        
        return notRegularType == null ? "" : notRegularType;
    }
    
    public void performNotRegular() throws NumberFormatException,
            HibernateException, PersistenceBeanException
    {
        CostDefinitions item = this.getEntity();
        if (this.getNotRegularType() != null
                && !this.getNotRegularType().isEmpty()
                && this.getNotRegularChildType() != null
                && !this.getNotRegularChildType().isEmpty())
        {
            item.setNotRegularType(BeansFactory.NotRegularTypes().Load(
                    this.getNotRegularChildType()));
        }
        else
        {
            item.setNotRegularType(null);
        }
        
        // item.setResponsible(this.getResponsible());
        item.setIrregularAmount(this.getIrregularAmount());
        item.setBeRecovered(this.getBeRecovered());
        item.setDate(new Date());
        item.setDurNumber(this.getDurNumber());
        item.setReferenceYear(this.getReferenceYear());
        item.setCertification(this.getCertification());
        item.setCertifyUser(this.getCurrentUser());
        // if (getManageMode())
        // {
        item.setAmountRecovery(this.getAmountRecovery());
        item.setAmountRetreat(this.getAmountRetreat());
        item.setRecoveryDate(this.getRecoveryDate());
        item.setRetreatDate(this.getRetreatDate());
        if ((this.getAmountRecovery() != null && this.getAmountRecovery() > 0d)
                || (this.getAmountRetreat() != null && this.getAmountRetreat() > 0d))
        {
            item.setIsRecoveryRetreat(true);
        }
        // }
        
        BeansFactory.CostDefinitions().Save(item);
    }
    
    public List<SelectItem> getNotRegularTypes()
    {
        return notRegularTypes;
    }
    
    public void setNotRegularTypes(List<SelectItem> notRegularTypes)
    {
        this.notRegularTypes = notRegularTypes;
    }
    
    public List<SelectItem> getNotRegularChildTypes()
    {
        return notRegularChildTypes;
    }
    
    public void setNotRegularChildTypes(List<SelectItem> notRegularChildTypes)
    {
        this.notRegularChildTypes = notRegularChildTypes;
    }
    
    public Double getIrregularAmount()
    {
        return irregularAmount;
    }
    
    public void setIrregularAmount(Double irregularAmount)
    {
        this.irregularAmount = irregularAmount;
    }
    
    public Integer getReferenceYear()
    {
        return referenceYear;
    }
    
    public void setReferenceYear(Integer referenceYear)
    {
        this.referenceYear = referenceYear;
    }
    
    public Boolean getCertification()
    {
        return certification;
    }
    
    public void setCertification(Boolean certification)
    {
        this.certification = certification;
    }
    
    public Boolean getBeRecovered()
    {
        return beRecovered;
    }
    
    public void setBeRecovered(Boolean beRecovered)
    {
        this.beRecovered = beRecovered;
    }
    
    public Date getRecoveryDate()
    {
        return recoveryDate;
    }
    
    public void setRecoveryDate(Date recoveryDate)
    {
        this.recoveryDate = recoveryDate;
    }
    
    public Double getAmountRecovery()
    {
        return amountRecovery;
    }
    
    public void setAmountRecovery(Double amountRecovery)
    {
        this.amountRecovery = amountRecovery;
    }
    
    public Date getRetreatDate()
    {
        return retreatDate;
    }
    
    public void setRetreatDate(Date retreatDate)
    {
        this.retreatDate = retreatDate;
    }
    
    public Double getAmountRetreat()
    {
        return amountRetreat;
    }
    
    public void setAmountRetreat(Double amountRetreat)
    {
        this.amountRetreat = amountRetreat;
    }
    
    public String getNotRegularChildType()
    {
        return notRegularChildType;
    }
    
    public void setNotRegularChildType(String notRegularChildType)
    {
        this.notRegularChildType = notRegularChildType;
    }
    
    public Integer getDurNumber()
    {
        return durNumber;
    }
    
    public void setDurNumber(Integer durNumber)
    {
        this.durNumber = durNumber;
    }
    
    public Double getAmountRecovered()
    {
        return amountRecovered;
    }
    
    public void setAmountRecovered(Double amountRecovered)
    {
        this.amountRecovered = amountRecovered;
    }
    
    public Date getDateAmountRecovered()
    {
        return dateAmountRecovered;
    }
    
    public void setDateAmountRecovered(Date dateAmountRecovered)
    {
        this.dateAmountRecovered = dateAmountRecovered;
    }
    
    public String getReasonOfRecovery()
    {
        return reasonOfRecovery;
    }
    
    public void setReasonOfRecovery(String reasonOfRecovery)
    {
        this.reasonOfRecovery = reasonOfRecovery;
    }
    
    public Boolean getRectificationConfirmed()
    {
        return rectificationConfirmed;
    }
    
    public void setRectificationConfirmed(Boolean rectificationConfirmed)
    {
        this.rectificationConfirmed = rectificationConfirmed;
    }
    
    public List<Recoveries> getListRecoveries()
    {
        return listRecoveries;
    }
    
    public void setListRecoveries(List<Recoveries> listRecoveries)
    {
        this.listRecoveries = listRecoveries;
    }
    
    public Integer getItemsPerPageList()
    {
        return itemsPerPageList;
    }
    
    public void setItemsPerPageList(Integer itemsPerPageList)
    {
        this.itemsPerPageList = itemsPerPageList;
    }
    
    public Double getSumRecoveries()
    {
        return sumRecoveries;
    }
    
    public void setSumRecoveries(Double sumRecoveries)
    {
        this.sumRecoveries = sumRecoveries;
    }
    
    public Boolean getErrorRecoverAmount()
    {
        if (this.getViewState().containsKey("errorRecoverAmount"))
        {
            return (Boolean) this.getViewState().get("errorRecoverAmount");
        }
        return false;
    }
    
    public void setErrorRecoverAmount(Boolean errorRecoverAmount)
    {
        this.getViewState().put("errorRecoverAmount", errorRecoverAmount);
    }
    
    public Boolean getErrorRectifyAmount()
    {
        if (this.getViewState().containsKey("errorRectifyAmount"))
        {
            return (Boolean) this.getViewState().get("errorRectifyAmount");
        }
        return false;
    }
    
    public void setErrorRectifyAmount(Boolean rectifyAmount)
    {
        this.getViewState().put("errorRectifyAmount", rectifyAmount);
    }
    
    public void setPrevRectAmount(Double amount)
    {
        this.getViewState().put("prevRectAmount", amount);
    }
    
    public Double getPrevRectAmount()
    {
        if (this.getViewState().containsKey("prevRectAmount"))
        {
            return (Double) this.getViewState().get("prevRectAmount");
        }
        
        return null;
    }
    
}
