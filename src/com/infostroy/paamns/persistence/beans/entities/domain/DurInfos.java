/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "dur_infos")
public class DurInfos extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 1182369979793259233L;
    
    /**
     * Stores durCompilation value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "dur_compilation_id")
    private DurCompilations   durCompilation;
    
    /**
     * Stores durInfoNumber value of entity.
     */
    @Column(name = "dur_info_number")
    private Integer           durInfoNumber;
    
    /**
     * Stores durCompilationDate value of entity.
     */
    @Column(name = "dur_compilation_date")
    private Date              durCompilationDate;
    
    /**
     * Stores protocolNumber value of entity.
     */
    @Column(name = "protocol_number")
    private String            protocolNumber;
    
    /**
     * Stores intermedio value of entity.
     */
    @Column
    private Boolean           intermedio;
    
    /**
     * Stores saldo value of entity.
     */
    @Column
    private Boolean           saldo;
    
    /**
     * Stores startDate value of entity.
     */
    @Column(name = "start_date")
    private Date              startDate;
    
    /**
     * Stores endDate value of entity.
     */
    @Column(name = "end_date")
    private Date              endDate;
    
    /**
     * Stores additionInfo value of entity.
     */
    @Column(name = "addition_info")
    private String            additionInfo;
    
    /**
     * Stores activityResumeDocument value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "activity_resume_document_id")
    private Documents         activityResumeDocument;
    
    /**
     * Stores financialDetailDocument value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "financial_detail_document_id")
    private Documents         financialDetailDocument;
    
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "uc_validation_document_id")
    private Documents         ucValidationDocument;
    
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "adc_validation_document_id")
    private Documents         adcValidationDocument;
    
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "adg_validation_document_id")
    private Documents         adgValidationDocument;
    
    @Column(name = "recover_amount")
    private Double            recoverAmount;
    
    @Column(name = "responsible")
    private String            responsible;
    
    @Column(name= "protocol_date")
    private Date			  protocolDate;
    
    /**
     * Sets durCompilation
     * @param durCompilation the durCompilation to set
     */
    public void setDurCompilation(DurCompilations durCompilation)
    {
        this.durCompilation = durCompilation;
    }
    
    /**
     * Gets durCompilation
     * @return durCompilation the durCompilation
     */
    public DurCompilations getDurCompilation()
    {
        return durCompilation;
    }
    
    /**
     * Sets durInfoNumber
     * @param durInfoNumber the durInfoNumber to set
     */
    public void setDurInfoNumber(Integer durInfoNumber)
    {
        this.durInfoNumber = durInfoNumber;
    }
    
    /**
     * Gets durInfoNumber
     * @return durInfoNumber the durInfoNumber
     */
    public Integer getDurInfoNumber()
    {
        return durInfoNumber;
    }
    
    /**
     * Sets durCompilationDate
     * @param durCompilationDate the durCompilationDate to set
     */
    public void setDurCompilationDate(Date durCompilationDate)
    {
        this.durCompilationDate = durCompilationDate;
    }
    
    /**
     * Gets durCompilationDate
     * @return durCompilationDate the durCompilationDate
     */
    public Date getDurCompilationDate()
    {
        return durCompilationDate;
    }
    
    /**
     * Sets protocolNumber
     * @param protocolNumber the protocolNumber to set
     */
    public void setProtocolNumber(String protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Gets protocolNumber
     * @return protocolNumber the protocolNumber
     */
    public String getProtocolNumber()
    {
        return protocolNumber;
    }
    
    /**
     * Sets intermedio
     * @param intermedio the intermedio to set
     */
    public void setIntermedio(Boolean intermedio)
    {
        this.intermedio = intermedio;
    }
    
    /**
     * Gets intermedio
     * @return intermedio the intermedio
     */
    public Boolean getIntermedio()
    {
        return intermedio;
    }
    
    /**
     * Sets saldo
     * @param saldo the saldo to set
     */
    public void setSaldo(Boolean saldo)
    {
        this.saldo = saldo;
    }
    
    /**
     * Gets saldo
     * @return saldo the saldo
     */
    public Boolean getSaldo()
    {
        return saldo;
    }
    
    /**
     * Sets startDate
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    
    /**
     * Gets startDate
     * @return startDate the startDate
     */
    public Date getStartDate()
    {
        return startDate;
    }
    
    /**
     * Sets endDate
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
    
    /**
     * Gets endDate
     * @return endDate the endDate
     */
    public Date getEndDate()
    {
        return endDate;
    }
    
    /**
     * Sets additionInfo
     * @param additionInfo the additionInfo to set
     */
    public void setAdditionInfo(String additionInfo)
    {
        this.additionInfo = additionInfo;
    }
    
    /**
     * Gets additionInfo
     * @return additionInfo the additionInfo
     */
    public String getAdditionInfo()
    {
        return additionInfo;
    }
    
    /**
     * Sets activityResumeDocument
     * @param activityResumeDocument the activityResumeDocument to set
     */
    public void setActivityResumeDocument(Documents activityResumeDocument)
    {
        this.activityResumeDocument = activityResumeDocument;
    }
    
    /**
     * Gets activityResumeDocument
     * @return activityResumeDocument the activityResumeDocument
     */
    public Documents getActivityResumeDocument()
    {
        return activityResumeDocument;
    }
    
    /**
     * Sets financialDetailDocument
     * @param financialDetailDocument the financialDetailDocument to set
     */
    public void setFinancialDetailDocument(Documents financialDetailDocument)
    {
        this.financialDetailDocument = financialDetailDocument;
    }
    
    /**
     * Gets financialDetailDocument
     * @return financialDetailDocument the financialDetailDocument
     */
    public Documents getFinancialDetailDocument()
    {
        return financialDetailDocument;
    }
    
    /**
     * Sets recoverAmount
     * @param recoverAmount the recoverAmount to set
     */
    public void setRecoverAmount(Double recoverAmount)
    {
        this.recoverAmount = recoverAmount;
    }
    
    /**
     * Gets recoverAmount
     * @return recoverAmount the recoverAmount
     */
    public Double getRecoverAmount()
    {
        return recoverAmount;
    }
    
    /**
     * Sets responsible
     * @param responsible the responsible to set
     */
    public void setResponsible(String responsible)
    {
        this.responsible = responsible;
    }
    
    /**
     * Gets responsible
     * @return responsible the responsible
     */
    public String getResponsible()
    {
        return responsible;
    }

	/**
	 * Gets protocolDate
	 * @return protocolDate the protocolDate
	 */
	public Date getProtocolDate()
	{
		return protocolDate;
	}

	/**
	 * Sets protocolDate
	 * @param protocolDate the protocolDate to set
	 */
	public void setProtocolDate(Date protocolDate)
	{
		this.protocolDate = protocolDate;
	}

	public Documents getUcValidationDocument()
	{
		return ucValidationDocument;
	}

	public void setUcValidationDocument(Documents ucValidationDocument)
	{
		this.ucValidationDocument = ucValidationDocument;
	}

	public Documents getAdcValidationDocument()
	{
		return adcValidationDocument;
	}

	public void setAdcValidationDocument(Documents adcValidationDocument)
	{
		this.adcValidationDocument = adcValidationDocument;
	}

	public Documents getAdgValidationDocument()
	{
		return adgValidationDocument;
	}

	public void setAdgValidationDocument(Documents adgValidationDocument)
	{
		this.adgValidationDocument = adgValidationDocument;
	}
    
	
    
}
