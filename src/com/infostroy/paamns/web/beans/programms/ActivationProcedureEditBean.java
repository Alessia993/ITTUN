/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.ActivationProcedureInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActivationProcedureTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonsForDelay;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibleUsers;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class ActivationProcedureEditBean extends
        EntityEditBean<ActivationProcedureInfo>
{
    private String                              errorMessage;
    
    private boolean                             isViewOnly;
    
    private List<SelectItem>                    listTypes;
    
    private List<SelectItem>                    listRespUsers;
    
    private List<ActivationProcedureInfoCommon> listActProcInfo;
    
    private List<SelectItem>                    listReasonsDelay;
    
    private String                              selectedType;
    
    private String                              selectedUser;
    
    private ActivationProcedureAnagraph         currentActProc;
    
    private String                              code;
    
    private ActivationProcedureTypes            type;
    
    private String                              description;
    
    private ResponsibleUsers                    responsible_user;
    
    private String                              responsible_user_name;
    
    private String                              amount;
    
    private Boolean                             tab1Visibility;
    
    private Boolean                             tab2Visibility;
    
    @SuppressWarnings("unused")
    private String                              fullAmountFieldVisibility;
    
    @SuppressWarnings("unused")
    private String                              fullAmountSpanVisibility;
    
    private String                              fullAmount;
    
    private String                              tab1Style;
    
    private String                              tab2Style;
    
    private String                              errorStyle = "background-color: #FFE5E5;";
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.ACTIVATEPROCEDURELIST);
    }
    
    public Boolean BeforeSave()
    {
        for (int i = 0; i < 5; i++)
        {
            if (this.listActProcInfo.get(i).getDateExpected() == null)
            {
                this.listActProcInfo.get(i).setStyle(errorStyle);
                this.listActProcInfo.get(i).setTitle(
                        Utils.getString("validatorMessage"));
                this.setTab2Style(errorStyle);
            }
        }
        
        for (int i = 0; i < 5; i++)
        {
            if (this.listActProcInfo.get(i).getDateEffective() != null)
            {
                for (int j = 0; j < i; j++)
                {
                    if (this.listActProcInfo.get(j).getDateEffective() == null)
                    {
                        this.listActProcInfo.get(i).setStyle2(errorStyle);
                        this.listActProcInfo
                                .get(i)
                                .setTitle2(
                                        Utils.getString("validator.activationProcedureStepCompile"));
                        this.setTab2Style(errorStyle);
                    }
                }
            }
        }
        
        if (this.getTab2Style() == null)
        {
            for (int i = 1; i < 5; i++)
            {
                if (this.listActProcInfo.get(i).getDateEffective() != null)
                {
                    if (this.listActProcInfo
                            .get(i)
                            .getDateEffective()
                            .before(this.listActProcInfo.get(i - 1)
                                    .getDateEffective()))
                    {
                        this.listActProcInfo.get(i).setStyle2(errorStyle);
                        this.listActProcInfo
                                .get(i)
                                .setTitle2(
                                        Utils.getString("validator.activationProcedureDateCheck"));
                        this.setTab2Style(errorStyle);
                    }
                }
                if (this.listActProcInfo.get(i).getDateExpected() != null)
                {
                    if (this.listActProcInfo
                            .get(i)
                            .getDateExpected()
                            .before(this.listActProcInfo.get(i - 1)
                                    .getDateExpected()))
                    {
                        
                        this.listActProcInfo.get(i).setStyle(errorStyle);
                        this.listActProcInfo
                                .get(i)
                                .setTitle(
                                        Utils.getString("validator.activationProcedureDateCheck"));
                        this.setTab2Style(errorStyle);
                    }
                }
            }
        }
        
        if (this.getTab2Style() != null)
        {
            this.setErrorMessage(Utils.getString("validatorCheckAllFields"));
            return false;
        }
        else
        {
            this.setErrorMessage("");
        }
        
        if (this.getFullAmountFieldVisibility() != null
                && this.getFullAmountFieldVisibility().equals("none"))
        {
            Boolean isAllFilled = true;
            for (ActivationProcedureInfoCommon actProcInfo : this.listActProcInfo)
            {
                if (actProcInfo.getDateEffective() == null
                        || actProcInfo.getDateExpected() == null)
                {
                    isAllFilled = false;
                    break;
                }
            }
            
            if (isAllFilled)
            {
                try
                {
                    Double.parseDouble(this.fullAmount);
                }
                catch(NumberFormatException e)
                {
                    this.setFullAmountFieldVisibility("");
                    this.setFullAmountSpanVisibility("");
                    this.setTab1Style(errorStyle);
                    return false;
                }
                
                this.setFullAmountSpanVisibility("none");
                return true;
                
            }
            else
            {
                this.setFullAmount(null);
                this.setFullAmountFieldVisibility("none");
                this.setFullAmountSpanVisibility("none");
                return true;
            }
        }
        else
        {
            Boolean isAllFilled = true;
            for (ActivationProcedureInfoCommon actProcInfo : this.listActProcInfo)
            {
                if (actProcInfo.getDateEffective() == null
                        || actProcInfo.getDateExpected() == null)
                {
                    isAllFilled = false;
                    break;
                }
            }
            
            if (isAllFilled)
            {
                if (this.fullAmount != null && !this.fullAmount.equals(""))
                {
                    try
                    {
                        Double.parseDouble(this.fullAmount);
                    }
                    catch(NumberFormatException e)
                    {
                        this.setFullAmountFieldVisibility("");
                        this.setFullAmountSpanVisibility("");
                        this.setTab1Style(errorStyle);
                        return false;
                    }
                    
                    this.setFullAmountSpanVisibility("none");
                }
                else
                {
                    this.setFullAmountSpanVisibility("");
                    this.setTab1Style(errorStyle);
                    return false;
                }
                
                return true;
            }
            else
            {
                this.setFullAmount(null);
                this.setFullAmountFieldVisibility("none");
                this.setFullAmountSpanVisibility("none");
                return true;
            }
        }
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (this.getSession().get("actProc") != null)
        {
            this.currentActProc = BeansFactory.ActivationProcedureAnagraph()
                    .Load(String.valueOf(this.getSession().get("actProc")));
            
            this.setCode(this.currentActProc.getCode());
            this.setType(this.currentActProc.getActivationProcedureType());
            this.setDescription(this.currentActProc.getDescription());
            this.setResponsible_user(this.currentActProc.getResponsibleUser());
            this.setResponsible_user_name(this.currentActProc
                    .getResponsibleUserName());
            DecimalFormat dec = new DecimalFormat("###.##");
            
            if (this.currentActProc.getAmount() != null)
            {
                this.setAmount(dec.format(this.currentActProc.getAmount()));
            }
            else
            {
                this.setAmount(null);
            }
            
            if (this.currentActProc.getFullAmount() != null)
            {
                this.setFullAmount(dec.format(this.currentActProc
                        .getFullAmount()));
            }
            else
            {
                this.setFullAmount(null);
            }
        }
        
        if (this.getSession().get("viewOnly") != null
                && this.getSession().get("viewOnly").equals(true))
        {
            this.setIsViewOnly(true);
        }
        else
        {
            this.setIsViewOnly(false);
        }
        
        this.setTab1Visibility(true);
        
        this.FillComboTypes();
        this.FillResponsibleUsers();
        this.FillGridItems();
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException
    {
        ActivationProcedureAnagraph actProc = new ActivationProcedureAnagraph();
        
        if (this.getSession().get("actProc") != null)
        {
            actProc = BeansFactory.ActivationProcedureAnagraph().Load(
                    this.getSession().get("actProc").toString());
        }
        
        actProc.setCode(this.getCode());
        actProc.setActivationProcedureType(BeansFactory
                .ActivationProcedureTypes().Load(this.selectedType));
        actProc.setDescription(this.getDescription());
        actProc.setResponsibleUser(BeansFactory.ResponsibleUsers().Load(
                this.selectedUser));
        actProc.setResponsibleUserName(this.getResponsible_user_name());
        
        if (this.getAmount() != null)
        {
            if (!this.getAmount().equals(""))
            {
                actProc.setAmount(Double.parseDouble(this.getAmount()));
            }
        }
        else
        {
            actProc.setAmount(null);
        }
        
        if (this.getFullAmount() != null)
        {
            if (!this.getFullAmount().equals(""))
            {
                actProc.setFullAmount(Double.valueOf(this.getFullAmount()));
            }
            else
            {
                actProc.setFullAmount(null);
            }
        }
        else
        {
            actProc.setFullAmount(null);
        }
        
        if (this.getSession().get("actProc") == null)
        {
            actProc.setIsCreatedByAGUUser(this.getSessionBean().isAGU());
            
            if (this.getSessionBean().isAGU() && this.getSessionBean().isSTC())
            {
                actProc.setIsCreatedByAGUUser(false);
            }
        }
        
        BeansFactory.ActivationProcedureAnagraph().SaveInTransaction(actProc);
        
        // Activation procedure info saving       
        
        if (this.getSession().get("actProc") != null)
        {
            List<ActivationProcedureInfo> list = BeansFactory
                    .ActivationProcedureInfo().GetInfoForProcedure(
                            this.getSession().get("actProc"));
            
            int counter = -1;
            for (ActivationProcedureInfo actProcInfo : list)
            {
                counter++;
                actProcInfo.setStep(Integer.valueOf(this.listActProcInfo.get(
                        counter).getStep()));
                actProcInfo.setDescription(this.listActProcInfo.get(counter)
                        .getDescription());
                actProcInfo.setDateExpected(this.listActProcInfo.get(counter)
                        .getDateExpected());
                actProcInfo.setEffectiveDate(this.listActProcInfo.get(counter)
                        .getDateEffective());
                String note = this.listActProcInfo.get(counter).getNote()
                        .toString();
                actProcInfo.setNote(note.length() > 255 ? note
                        .substring(0, 254) : note);
                
                if (this.listActProcInfo.get(counter).getListDelay() != null)
                {
                    if (this.listActProcInfo.get(counter).getListDelay() != SelectItemHelper
                            .getFirst().getValue())
                    {
                        actProcInfo.setReasonsForDelay(BeansFactory
                                .ReasonsForDelay().Load(
                                        this.listActProcInfo.get(counter)
                                                .getListDelay()));
                    }
                    else
                    {
                        actProcInfo.setReasonsForDelay(null);
                    }
                }
                actProcInfo.setActivationProcedureAnagraph(actProc);
                
                BeansFactory.ActivationProcedureInfo().SaveInTransaction(
                        actProcInfo);
            }
        }
        else
        {
            for (ActivationProcedureInfoCommon actProcInfo : this.listActProcInfo)
            {
                ActivationProcedureInfo entity = new ActivationProcedureInfo();
                entity.setStep(Integer.valueOf(actProcInfo.getStep()));
                entity.setDescription(actProcInfo.getDescription());
                entity.setDateExpected(actProcInfo.getDateExpected());
                entity.setEffectiveDate(actProcInfo.getDateEffective());
                String note = actProcInfo.getNote().toString();
                entity.setNote(note.length() > 255 ? note.substring(0, 254)
                        : note);
                if (actProcInfo.getListDelay() != SelectItemHelper.getFirst()
                        .getValue())
                {
                    entity.setReasonsForDelay(BeansFactory.ReasonsForDelay()
                            .Load(actProcInfo.getListDelay()));
                }
                
                entity.setActivationProcedureAnagraph(actProc);
                
                BeansFactory.ActivationProcedureInfo()
                        .SaveInTransaction(entity);
            }
        }
    }
    
    private void FillComboTypes() throws HibernateException,
            PersistenceBeanException
    {
        this.listTypes = new ArrayList<SelectItem>();
        this.listReasonsDelay = new ArrayList<SelectItem>();
        this.listTypes.clear();
        this.listReasonsDelay.clear();
        
        this.listTypes.add(SelectItemHelper.getFirst());
        this.listReasonsDelay.add(SelectItemHelper.getFirst());
        
        List<ReasonsForDelay> listReasForDelay = new ArrayList<ReasonsForDelay>();
        
        try
        {
            listReasForDelay = BeansFactory.ReasonsForDelay().Load();
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
        
        for (ReasonsForDelay reason : listReasForDelay)
        {
            SelectItem item = new SelectItem();
            item.setLabel(reason.getValue());
            item.setValue(String.valueOf(reason.getId()));
            this.listReasonsDelay.add(item);
        }
        
        try
        {
            List<ActivationProcedureTypes> listProcTypes = BeansFactory
                    .ActivationProcedureTypes().Load();
            
            for (ActivationProcedureTypes type : listProcTypes)
            {
                if (type != null)
                {
                    SelectItem item = new SelectItem();
                    item.setLabel(type.getValue());
                    item.setValue(String.valueOf(type.getId()));
                    this.listTypes.add(item);
                }
            }
            
            if (this.getSession().get("actProc") != null)
            {
                this.selectedType = String.valueOf(BeansFactory
                        .ActivationProcedureAnagraph()
                        .Load(String.valueOf(this.getSession().get("actProc")))
                        .getActivationProcedureType().getId());
            }
            else
            {
                this.selectedType = (String) SelectItemHelper.getFirst()
                        .getValue();
            }
            
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    private void FillResponsibleUsers() throws PersistenceBeanException
    {
        this.listRespUsers = new ArrayList<SelectItem>();
        
        this.listRespUsers.add(SelectItemHelper.getFirst());
        
        try
        {
            List<ResponsibleUsers> listResUsers = BeansFactory
                    .ResponsibleUsers().Load();
            
            for (ResponsibleUsers respUser : listResUsers)
            {
                if (respUser != null)
                {
                    SelectItem item = new SelectItem();
                    item.setLabel(respUser.getValue());
                    item.setValue(String.valueOf(respUser.getId()));
                    this.listRespUsers.add(item);
                }
            }
            
            if (this.getSession().get("actProc") != null)
            {
                this.selectedUser = String.valueOf(BeansFactory
                        .ActivationProcedureAnagraph()
                        .Load(String.valueOf(this.getSession().get("actProc")))
                        .getResponsibleUser().getId());
            }
            else
            {
                this.selectedUser = (String) SelectItemHelper.getFirst()
                        .getValue();
            }
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
            e.printStackTrace();
        }
    }
    
    private void FillGridItems() throws NumberFormatException,
            HibernateException, PersistenceBeanException
    {
        this.listActProcInfo = new ArrayList<ActivationProcedureInfoCommon>();
        
        if (this.getSession().get("actProc") != null)
        {
            List<ActivationProcedureInfo> listInfo = new ArrayList<ActivationProcedureInfo>();
            
            try
            {
                listInfo = BeansFactory.ActivationProcedureInfo()
                        .GetInfoForProcedure(this.getSession().get("actProc"));
            }
            catch(PersistenceBeanException e)
            {
                log.error(e);
                e.printStackTrace();
            }
            
            Boolean isAllDatesFilled = true;
            for (ActivationProcedureInfo actProcInfo : listInfo)
            {
                ActivationProcedureInfoCommon entity = new ActivationProcedureInfoCommon();
                entity.setStep(String.valueOf(actProcInfo.getStep()));
                entity.setDescription(this
                        .GetDescriptionByStepIndex(actProcInfo.getStep()));
                entity.setDateExpected(actProcInfo.getDateExpected());
                entity.setDateEffective(actProcInfo.getEffectiveDate());
                entity.setNote(actProcInfo.getNote());
                
                if (actProcInfo.getReasonsForDelay() != null)
                {
                    entity.setListDelay(String.valueOf(actProcInfo
                            .getReasonsForDelay().getId()));
                    entity.setValue(actProcInfo.getReasonsForDelay().getValue());
                }
                
                this.listActProcInfo.add(entity);
            }
            
            for (ActivationProcedureInfo actProcInfo : listInfo)
            {
                if (actProcInfo.getDateExpected() == null
                        || actProcInfo.getEffectiveDate() == null)
                {
                    isAllDatesFilled = false;
                    break;
                }
            }
            
            if (isAllDatesFilled)
            {
                this.setFullAmountFieldVisibility("");
                
                if (BeansFactory.ActivationProcedureAnagraph()
                        .Load(String.valueOf(this.getSession().get("actProc")))
                        .getFullAmount() != null)
                {
                    this.setFullAmountSpanVisibility("none");
                }
                else
                {
                    this.setFullAmountSpanVisibility("");
                }
            }
            else
            {
                if (this.getFullAmountFieldVisibility() == null)
                {
                    this.setFullAmountFieldVisibility("none");
                    this.setFullAmountSpanVisibility("none");
                }
            }
        }
        else
        {
            for (int i = 1; i <= 5; i++)
            {
                ActivationProcedureInfoCommon actprocInfo = new ActivationProcedureInfoCommon();
                actprocInfo.setStep(String.valueOf(i));
                actprocInfo.setDescription(this.GetDescriptionByStepIndex(i));
                
                this.listActProcInfo.add(actprocInfo);
            }
            
            if (this.getFullAmountFieldVisibility() == null)
            {
                this.setFullAmountFieldVisibility("none");
                this.setFullAmountSpanVisibility("none");
            }
        }
    }
    
    /**
     * Returns step description by the step index
     * @param stepIndex
     * @return
     */
    private String GetDescriptionByStepIndex(int stepIndex)
    {
        String str = "viewDescriptionStep";
        str += stepIndex;
        return Utils.getString("Resources", str, null);
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * Sets description
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    /**
     * Gets description
     * @return description the description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Sets responsible_user
     * @param responsible_user the responsible_user to set
     */
    public void setResponsible_user(ResponsibleUsers responsible_user)
    {
        this.responsible_user = responsible_user;
    }
    
    /**
     * Gets responsible_user
     * @return responsible_user the responsible_user
     */
    public ResponsibleUsers getResponsible_user()
    {
        return responsible_user;
    }
    
    /**
     * Sets responsible_user_name
     * @param responsible_user_name the responsible_user_name to set
     */
    public void setResponsible_user_name(String responsible_user_name)
    {
        this.responsible_user_name = responsible_user_name;
    }
    
    /**
     * Gets responsible_user_name
     * @return responsible_user_name the responsible_user_name
     */
    public String getResponsible_user_name()
    {
        return responsible_user_name;
    }
    
    /**
     * Sets amount
     * @param amount the amount to set
     */
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    /**
     * Gets amount
     * @return amount the amount
     */
    public String getAmount()
    {
        return amount;
    }
    
    /**
     * Sets tab1Visibility
     * @param tab1Visibility the tab1Visibility to set
     */
    public void setTab1Visibility(Boolean tab1Visibility)
    {
        this.tab1Visibility = tab1Visibility;
    }
    
    /**
     * Gets tab1Visibility
     * @return tab1Visibility the tab1Visibility
     */
    public Boolean getTab1Visibility()
    {
        return tab1Visibility;
    }
    
    /**
     * Sets tab2Visibility
     * @param tab2Visibility the tab2Visibility to set
     */
    public void setTab2Visibility(Boolean tab2Visibility)
    {
        this.tab2Visibility = tab2Visibility;
    }
    
    /**
     * Gets tab2Visibility
     * @return tab2Visibility the tab2Visibility
     */
    public Boolean getTab2Visibility()
    {
        return tab2Visibility;
    }
    
    /**
     * Sets listTypes
     * @param listTypes the listTypes to set
     */
    public void setListTypes(List<SelectItem> listTypes)
    {
        this.listTypes = listTypes;
    }
    
    /**
     * Gets listTypes
     * @return listTypes the listTypes
     */
    public List<SelectItem> getListTypes()
    {
        return listTypes;
    }
    
    /**
     * Sets listRespUsers
     * @param listRespUsers the listRespUsers to set
     */
    public void setListRespUsers(List<SelectItem> listRespUsers)
    {
        this.listRespUsers = listRespUsers;
    }
    
    /**
     * Gets listRespUsers
     * @return listRespUsers the listRespUsers
     */
    public List<SelectItem> getListRespUsers()
    {
        return listRespUsers;
    }
    
    /**
     * Sets selectedUser
     * @param selectedUser the selectedUser to set
     */
    public void setSelectedUser(String selectedUser)
    {
        this.selectedUser = selectedUser;
    }
    
    /**
     * Gets selectedType
     * @return selectedType the selectedType
     */
    public String getSelectedType()
    {
        return selectedType;
    }
    
    /**
     * Sets selectedType
     * @param selectedType the selectedType to set
     */
    public void setSelectedType(String selectedType)
    {
        this.selectedType = selectedType;
    }
    
    /**
     * Gets selectedUser
     * @return selectedUser the selectedUser
     */
    public String getSelectedUser()
    {
        return selectedUser;
    }
    
    /**
     * Gets list of reasons delay
     * @return
     */
    public List<SelectItem> getListReasonsDelay()
    {
        return this.listReasonsDelay;
    }
    
    /**
     * Sets list of reasons for delay
     * @param list
     */
    public void setListReasonsDelay(List<SelectItem> list)
    {
        this.listReasonsDelay = list;
    }
    
    /**
     * Sets list of activation procedure info common
     * @param list
     */
    public void setListActProcInfo(List<ActivationProcedureInfoCommon> list)
    {
        this.listActProcInfo = list;
    }
    
    /**
     * Gets list of ActProcInfoCommon
     * @return
     */
    public List<ActivationProcedureInfoCommon> getListActProcInfo()
    {
        return this.listActProcInfo;
    }
    
    /**
     * Sets fullAmountFieldVisibility
     * @param fullAmountFieldVisibility the fullAmountFieldVisibility to set
     */
    public void setFullAmountFieldVisibility(String fullAmountFieldVisibility)
    {
        this.getViewState().put("fullamountvisibility",
                fullAmountFieldVisibility);
    }
    
    /**
     * Gets fullAmountFieldVisibility
     * @return fullAmountFieldVisibility the fullAmountFieldVisibility
     */
    public String getFullAmountFieldVisibility()
    {
        return (String) this.getViewState().get("fullamountvisibility");
    }
    
    /**
     * Sets fullAmount
     * @param fullAmount the fullAmount to set
     */
    public void setFullAmount(String fullAmount)
    {
        this.fullAmount = fullAmount;
    }
    
    /**
     * Gets fullAmount
     * @return fullAmount the fullAmount
     */
    public String getFullAmount()
    {
        return fullAmount;
    }
    
    /**
     * Sets isViewOnly
     * @param isViewOnly the isViewOnly to set
     */
    public void setIsViewOnly(boolean isViewOnly)
    {
        this.isViewOnly = isViewOnly;
    }
    
    /**
     * Gets isViewOnly
     * @return isViewOnly the isViewOnly
     */
    public boolean getIsViewOnly()
    {
        return isViewOnly;
    }
    
    /**
     * Sets type
     * @param type the type to set
     */
    public void setType(ActivationProcedureTypes type)
    {
        this.type = type;
    }
    
    public ActivationProcedureTypes getType()
    {
        return type;
    }
    
    /**
     * Sets fullAmountSpanVisibility
     * @param fullAmountSpanVisibility the fullAmountSpanVisibility to set
     */
    public void setFullAmountSpanVisibility(String fullAmountSpanVisibility)
    {
        this.getViewState().put("fullamountspanvisibility",
                fullAmountSpanVisibility);
    }
    
    /**
     * Gets fullAmountSpanVisibility
     * @return fullAmountSpanVisibility the fullAmountSpanVisibility
     */
    public String getFullAmountSpanVisibility()
    {
        return (String) this.getViewState().get("fullamountspanvisibility");
    }
    
    /**
     * Class is common for grid filling
     *
     * @author Sergey Zorin
     * InfoStroy Co., 2010.
     *
     */
    public class ActivationProcedureInfoCommon
    {
        private String step;
        
        private String description;
        
        private Date   dateExpected;
        
        private Date   dateEffective;
        
        private String style;
        
        private String style2;
        
        private String title;
        
        private String title2;
        
        private String note;
        
        /**
         * Gets title
         * @return title the title
         */
        public String getTitle()
        {
            return title;
        }
        
        /**
         * Sets title
         * @param title the title to set
         */
        public void setTitle(String title)
        {
            this.title = title;
        }
        
        private String listDelay;
        
        private String value;
        
        /**
         * Sets step
         * @param step the step to set
         */
        public void setStep(String step)
        {
            this.step = step;
        }
        
        /**
         * Gets step
         * @return step the step
         */
        public String getStep()
        {
            return step;
        }
        
        /**
         * Sets description
         * @param description the description to set
         */
        public void setDescription(String description)
        {
            this.description = description;
        }
        
        /**
         * Gets description
         * @return description the description
         */
        public String getDescription()
        {
            return description;
        }
        
        /**
         * Sets dateExpected
         * @param dateExpected the dateExpected to set
         */
        public void setDateExpected(Date dateExpected)
        {
            this.dateExpected = dateExpected;
        }
        
        /**
         * Gets dateExpected
         * @return dateExpected the dateExpected
         */
        public Date getDateExpected()
        {
            return dateExpected;
        }
        
        /**
         * Sets dateEffective
         * @param dateEffective the dateEffective to set
         */
        public void setDateEffective(Date dateEffective)
        {
            this.dateEffective = dateEffective;
        }
        
        /**
         * Gets dateEffective
         * @return dateEffective the dateEffective
         */
        public Date getDateEffective()
        {
            return dateEffective;
        }
        
        /**
         * Sets listDelay
         * @param listDelay the listDelay to set
         */
        public void setListDelay(String listDelay)
        {
            this.listDelay = listDelay;
        }
        
        /**
         * Gets listDelay
         * @return listDelay the listDelay
         */
        public String getListDelay()
        {
            return listDelay;
        }
        
        /**
         * Sets value
         * @param value the value to set
         */
        public void setValue(String value)
        {
            this.value = value;
        }
        
        /**
         * Gets value
         * @return value the value
         */
        public String getValue()
        {
            return value;
        }
        
        /**
         * Sets style
         * @param style the style to set
         */
        public void setStyle(String style)
        {
            this.style = style;
        }
        
        /**
         * Gets style
         * @return style the style
         */
        public String getStyle()
        {
            return style;
        }
        
        /**
         * Sets note
         * @param note the note to set
         */
        public void setNote(String note)
        {
            this.note = note;
        }
        
        /**
         * Gets note
         * @return note the note
         */
        public String getNote()
        {
            return note;
        }
        
        /**
         * Sets style2
         * @param style2 the style2 to set
         */
        public void setStyle2(String style2)
        {
            this.style2 = style2;
        }
        
        /**
         * Gets style2
         * @return style2 the style2
         */
        public String getStyle2()
        {
            return style2;
        }
        
        /**
         * Sets title2
         * @param title2 the title2 to set
         */
        public void setTitle2(String title2)
        {
            this.title2 = title2;
        }
        
        /**
         * Gets title2
         * @return title2 the title2
         */
        public String getTitle2()
        {
            return title2;
        }
    }
    
    /**
     * Gets tab1Style
     * @return tab1Style the tab1Style
     */
    public String getTab1Style()
    {
        return tab1Style;
    }
    
    /**
     * Sets tab1Style
     * @param tab1Style the tab1Style to set
     */
    public void setTab1Style(String tab1Style)
    {
        this.tab1Style = tab1Style;
    }
    
    /**
     * Gets tab2Style
     * @return tab2Style the tab2Style
     */
    public String getTab2Style()
    {
        return tab2Style;
    }
    
    /**
     * Sets tab2Style
     * @param tab2Style the tab2Style to set
     */
    public void setTab2Style(String tab2Style)
    {
        this.tab2Style = tab2Style;
    }
    
    /**
     * Sets errorMessage
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Gets errorMessage
     * @return errorMessage the errorMessage
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
}
