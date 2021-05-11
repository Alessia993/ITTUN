/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.RecoverActType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class NotRegularDurEditBean extends EntityProjectEditBean<Entity>
{
    private Double                amountRecovery;
    
    private Double                amountRetreat;
    
    private Date                  recoveryDate;
    
    private Date                  retreatDate;
    
    private Integer               durNumber;
    
    private Double                irregularAmount;
    
    private Integer               referenceYear;
    
    private Boolean               certification;   

	private String                responsible;
    
    private boolean               editMode;
    
    private boolean               recoveredMode;
    
    private boolean               notRecoveredMode;

	private DurCompilations       durCompilation;
    
    private CostDefinitions		  durCostDefinition;
    
    private DurInfos              durInfo;
    
    private List<CostDefinitions> listCD;
    
    private List<SelectItem>      notRegularTypes;
    
    private String                notRegularType;
    
    private String                notRegularChildType;
    
    private List<SelectItem>      notRegularChildTypes;
    
    private Long 			      selectedNotRegularDurRecoveryActType;

	private List<SelectItem>      allNotRegularDurRecoveryActTypes;
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.NOTREGULARDURLIST);
    }
       
    public void Page_Load() throws NumberFormatException, HibernateException,
    PersistenceBeanException
    {
    	
    	if (this.getSession().get("notRegularDurList") != null)
        {
    		this.durCostDefinition = BeansFactory.CostDefinitions().Load(String.valueOf(this.getSession().get("notRegularDurList")));
    		this.setAllNotRegularDurRecoveryActTypes(new ArrayList<SelectItem>());
            this.allNotRegularDurRecoveryActTypes.add(new SelectItem(0l, SelectItemHelper.getFirst().getLabel()));
            for (RecoverActType type : RecoverActType.values()) {
    			this.allNotRegularDurRecoveryActTypes.add(new SelectItem(type.getId(), type.toString()));
    		}
        }
        
        if (this.getSession().get("notRegularDurListShowOnly") != null)
        {
            this.setEditMode(!Boolean.valueOf(String.valueOf(this.getSession()
                    .get("notRegularDurListShowOnly"))));
        }
        
        if (this.getSession().get("notRegularDurRecoverable").equals(true))
        {
            this.setRecoveredMode(true);
            this.setNotRecoveredMode(false);
        }else{
            this.setRecoveredMode(false);
            this.setNotRecoveredMode(true);
        }

    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSateAndAguRead())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        if (!AccessGrantHelper.IsNotRegularSituationAvailable())
        {
            this.goTo(PagesTypes.PROJECTSTART);
        }
    }
    
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        //this.durCostDefinition.setDurRecoveryImport(this.durCostDefinition.getDurRecoveryImport());    	
    	this.durCostDefinition.setSelectedNotRegularDurRecoveryActType(this.durCostDefinition.getSelectedNotRegularDurRecoveryActType());
    	this.durCostDefinition.setDurNotRegularActNumber(this.getDurCostDefinition().getDurNotRegularActNumber());
    	this.durCostDefinition.setDurNotRegularActDate(this.getDurCostDefinition().getDurNotRegularActDate());
    	
    	// new acagnoni 2019/03/18
    	this.durCostDefinition.setDocumentationContentiousRecovery(this.getDurCostDefinition().getDocumentationContentiousRecovery());
    	this.durCostDefinition.setDocumentationUnsuccessfulRecovery(this.getDurCostDefinition().getDocumentationUnsuccessfulRecovery());
    	this.durCostDefinition.setDateOfPaymentProgrammeOrBody(this.getDurCostDefinition().getDateOfPaymentProgrammeOrBody());
    	this.durCostDefinition.setSupportingDocumentsWaivingDecision(this.getDurCostDefinition().getSupportingDocumentsWaivingDecision());
    	this.durCostDefinition.setAmountOfRecoveryWaived(this.getDurCostDefinition().getAmountOfRecoveryWaived());
    	//
    	if(this.isRecoveredMode()){
    		this.durCostDefinition.setDurNotRegularIsRecoverable(true);
    		this.durCostDefinition.setDurRecoverySustainImport(null);
    		this.durCostDefinition.setTotEligibleExpensesPublicSupportNonRecoverable(null);
    		this.durCostDefinition.setDurAmountPublicSupportReimbursed(this.durCostDefinition.getDurAmountPublicSupportReimbursed());
    		this.durCostDefinition.setDurTotEligibleExpensesPublicSupportReimbursed(this.durCostDefinition.getDurTotEligibleExpensesPublicSupportReimbursed());
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromBeneficiary(this.durCostDefinition.getDurRecoveryReimbursementDateFromBeneficiary());
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromCE(this.durCostDefinition.getDurRecoveryReimbursementDateFromCE());
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromCMS(this.durCostDefinition.getDurRecoveryReimbursementDateFromCMS());
    		this.getDurCostDefinition().setAcuCheckPublicAmount(this.durCostDefinition.getAcuCheckPublicAmount() - this.durCostDefinition.getDurTotEligibleExpensesPublicSupportReimbursed());
    	}else{
    		this.durCostDefinition.setDurNotRegularIsRecoverable(false);
    		this.durCostDefinition.setDurRecoverySustainImport(this.durCostDefinition.getDurRecoverySustainImport());
    		this.durCostDefinition.setTotEligibleExpensesPublicSupportNonRecoverable(this.durCostDefinition.getTotEligibleExpensesPublicSupportNonRecoverable());
    		this.durCostDefinition.setDurAmountPublicSupportReimbursed(null);
    		this.durCostDefinition.setDurTotEligibleExpensesPublicSupportReimbursed(null);
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromBeneficiary(null);
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromCE(null);
    		this.durCostDefinition.setDurRecoveryReimbursementDateFromCMS(null);
    	}
    }
    
    
    /**
     * Sets editMode
     * 
     * @param editMode
     *            the editMode to set
     */
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }
    
    /**
     * Gets editMode
     * 
     * @return editMode the editMode
     */
    public boolean isEditMode()
    {
        return editMode;
    }
    
    /**
     * Sets recoveredMode
     * 
     * @param recoveredMode
     *            the recoveredMode to set
     */
    public void setRecoveredMode(boolean recoveredMode)
    {
        this.recoveredMode = recoveredMode;
    }
    
    /**
     * Gets recoveredMode
     * 
     * @return recoveredMode the recoveredMode
     */
    public boolean isRecoveredMode()
    {
        return recoveredMode;
    }
    
    /**
   	 * @return the notRecoveredMode
   	 */
   	public boolean isNotRecoveredMode() {
   		return notRecoveredMode;
   	}

   	/**
   	 * @param notRecoveredMode the notRecoveredMode to set
   	 */
   	public void setNotRecoveredMode(boolean notRecoveredMode) {
   		this.notRecoveredMode = notRecoveredMode;
   	}
    
    /**
     * Sets durCompilation
     * 
     * @param durCompilation
     *            the durCompilation to set
     */
    public void setDurCompilation(DurCompilations durCompilation)
    {
        this.durCompilation = durCompilation;
    }
    
    /**
     * Gets durCompilation
     * 
     * @return durCompilation the durCompilation
     */
    public DurCompilations getDurCompilation()
    {
        return durCompilation;
    }
    
    /**
     * Sets listCD
     * 
     * @param listCD
     *            the listCD to set
     */
    public void setListCD(List<CostDefinitions> listCD)
    {
        this.listCD = listCD;
    }
    
    /**
     * Gets listCD
     * 
     * @return listCD the listCD
     */
    public List<CostDefinitions> getListCD()
    {
        return listCD;
    }
    
    /**
     * Sets notRegularTypes
     * 
     * @param notRegularTypes
     *            the notRegularTypes to set
     */
    public void setNotRegularTypes(List<SelectItem> notRegularTypes)
    {
        this.notRegularTypes = notRegularTypes;
    }
    
    /**
     * Gets notRegularTypes
     * 
     * @return notRegularTypes the notRegularTypes
     */
    public List<SelectItem> getNotRegularTypes()
    {
        return notRegularTypes;
    }
    
    /**
     * Sets notRegularChildTypes
     * 
     * @param notRegularChildTypes
     *            the notRegularChildTypes to set
     */
    public void setNotRegularChildTypes(List<SelectItem> notRegularChildTypes)
    {
        this.notRegularChildTypes = notRegularChildTypes;
    }
    
    /**
     * Gets notRegularChildTypes
     * 
     * @return notRegularChildTypes the notRegularChildTypes
     */
    public List<SelectItem> getNotRegularChildTypes()
    {
        return notRegularChildTypes;
    }
    
    /**
     * Sets costItemEditId
     * 
     * @param costItemEditId
     *            the costItemEditId to set
     */
    public void setCostItemEditId(String costItemEditId)
    {
        this.getViewState().put("costItemEditId", costItemEditId);
    }
    
    /**
     * Gets costItemEditId
     * 
     * @return costItemEditId the costItemEditId
     */
    public String getCostItemEditId()
    {
        return (String) this.getViewState().get("costItemEditId");
    }
    
    /**
     * Sets notRegularType
     * 
     * @param notRegularType
     *            the notRegularType to set
     */
    public void setNotRegularType(String notRegularType)
    {
        this.getViewState().put("notRegularType", notRegularType);
        this.notRegularType = notRegularType;
    }
    
    /**
     * Gets notRegularType
     * 
     * @return notRegularType the notRegularType
     */
    public String getNotRegularType()
    {
        if (this.getViewState().get("notRegularType") != null)
        {
            return (String) this.getViewState().get("notRegularType");
        }
        
        return notRegularType == null ? "" : notRegularType;
    }
    
    /**
     * Sets notRegularChildType
     * 
     * @param notRegularChildType
     *            the notRegularChildType to set
     */
    public void setNotRegularChildType(String notRegularChildType)
    {
        this.notRegularChildType = notRegularChildType;
    }
    
    /**
     * Gets notRegularChildType
     * 
     * @return notRegularChildType the notRegularChildType
     */
    public String getNotRegularChildType()
    {
        return notRegularChildType;
    }
    
    /**
     * Sets responsible
     * 
     * @param responsible
     *            the responsible to set
     */
    public void setResponsible(String responsible)
    {
        this.responsible = responsible;
    }
    
    /**
     * Gets responsible
     * 
     * @return responsible the responsible
     */
    public String getResponsible()
    {
        return responsible;
    }
    
    /**
     * Gets durNumber
     * 
     * @return durNumber the durNumber
     */
    public Integer getDurNumber()
    {
        return durNumber;
    }
    
    /**
     * Sets durNumber
     * 
     * @param durNumber
     *            the durNumber to set
     */
    public void setDurNumber(Integer durNumber)
    {
        this.durNumber = durNumber;
    }
    
    /**
     * Gets irregularAmount
     * 
     * @return irregularAmount the irregularAmount
     */
    public Double getIrregularAmount()
    {
        return irregularAmount;
    }
    
    /**
     * Sets irregularAmount
     * 
     * @param irregularAmount
     *            the irregularAmount to set
     */
    public void setIrregularAmount(Double irregularAmount)
    {
        this.irregularAmount = irregularAmount;
    }
    
    /**
     * Gets referenceYear
     * 
     * @return referenceYear the referenceYear
     */
    @Export(propertyName = "notRegularEditReferenceYear", seqXLS = 2, type = FieldTypes.NUMBER, place = ExportPlaces.NotRegular)
    public Integer getReferenceYear()
    {
        return referenceYear;
    }
    
    /**
     * Sets referenceYear
     * 
     * @param referenceYear
     *            the referenceYear to set
     */
    public void setReferenceYear(Integer referenceYear)
    {
        this.referenceYear = referenceYear;
    }
    
    /**
     * Gets certification
     * 
     * @return certification the certification
     */
    @Export(propertyName = "notRegularEditCertification", seqXLS = 3, type = FieldTypes.BOOLEAN, place = ExportPlaces.NotRegular)
    public Boolean getCertification()
    {
        return certification;
    }
    
    /**
     * Sets certification
     * 
     * @param certification
     *            the certification to set
     */
    public void setCertification(Boolean certification)
    {
        this.certification = certification;
    }
        
    /**
     * Gets amountRecovery
     * 
     * @return amountRecovery the amountRecovery
     */
    public Double getAmountRecovery()
    {
        return amountRecovery;
    }
    
    /**
     * Sets amountRecovery
     * 
     * @param amountRecovery
     *            the amountRecovery to set
     */
    public void setAmountRecovery(Double amountRecovery)
    {
        this.amountRecovery = amountRecovery;
    }
    
    /**
     * Gets amountRetreat
     * 
     * @return amountRetreat the amountRetreat
     */
    public Double getAmountRetreat()
    {
        return amountRetreat;
    }
    
    /**
     * Sets amountRetreat
     * 
     * @param amountRetreat
     *            the amountRetreat to set
     */
    public void setAmountRetreat(Double amountRetreat)
    {
        this.amountRetreat = amountRetreat;
    }
    
    /**
     * Gets recoveryDate
     * 
     * @return recoveryDate the recoveryDate
     */
    public Date getRecoveryDate()
    {
        return recoveryDate;
    }
    
    /**
     * Sets recoveryDate
     * 
     * @param recoveryDate
     *            the recoveryDate to set
     */
    public void setRecoveryDate(Date recoveryDate)
    {
        this.recoveryDate = recoveryDate;
    }
    
    /**
     * Gets retreatDate
     * 
     * @return retreatDate the retreatDate
     */
    public Date getRetreatDate()
    {
        return retreatDate;
    }
    
    /**
     * Sets retreatDate
     * 
     * @param retreatDate
     *            the retreatDate to set
     */
    public void setRetreatDate(Date retreatDate)
    {
        this.retreatDate = retreatDate;
    }
    
    /**
     * Gets durInfo
     * 
     * @return durInfo the durInfo
     */
    public DurInfos getDurInfo()
    {
        return durInfo;
    }
    
    /**
     * Sets durInfo
     * 
     * @param durInfo
     *            the durInfo to set
     */
    public void setDurInfo(DurInfos durInfo)
    {
        this.durInfo = durInfo;
    }
    
    /**
     * Sets manageMode
     * 
     * @param manageMode
     *            the manageMode to set
     */
    public void setManageMode(boolean manageMode)
    {
        this.getViewState().put("manageMode", manageMode);
    }
    
    /**
     * Gets manageMode
     * 
     * @return manageMode the manageMode
     */
    public boolean getManageMode()
    {
        return this.getViewState().get("manageMode") == null ? false
                : (Boolean) this.getViewState().get("manageMode");
    }

	/**
	 * @return the durCostDefinition
	 */
	public CostDefinitions getDurCostDefinition() {
		return durCostDefinition;
	}

	/**
	 * @param durCostDefinition the durCostDefinition to set
	 */
	public void setDurCostDefinition(CostDefinitions durCostDefinition) {
		this.durCostDefinition = durCostDefinition;
	}
	
	public Long getSelectedNotRegularDurRecoveryActType() {
		return selectedNotRegularDurRecoveryActType;
	}

	public void setSelectedNotRegularDurRecoveryActType(Long selectedNotRegularDurRecoveryActType) {
		this.selectedNotRegularDurRecoveryActType = selectedNotRegularDurRecoveryActType;
	}

	public List<SelectItem> getAllNotRegularDurRecoveryActTypes() {
		return allNotRegularDurRecoveryActTypes;
	}

	public void setAllNotRegularDurRecoveryActTypes(List<SelectItem> allNotRegularDurRecoveryActTypes) {
		this.allNotRegularDurRecoveryActTypes = allNotRegularDurRecoveryActTypes;
	}
		
}
