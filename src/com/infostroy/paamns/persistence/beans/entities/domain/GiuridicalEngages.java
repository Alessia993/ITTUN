package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ActTypes;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * modified by Ingloba360
 * 
 */
@Entity
@Table(name = "giuridical_engages")
public class GiuridicalEngages extends PersistentEntity {
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = 2495941469498667891L;
    
    @ManyToOne
    @JoinColumn
    private Projects          project;
    
    /**
     * Stores actType value of entity.
     */
    @ManyToOne
    @JoinColumn(name = "act_type_id")
    private ActTypes          actType;
    
    
    @ManyToOne
    @JoinColumn(name="create_cf_partner_id")
    private CFPartnerAnagraphs createPartner;
    /**
     * Stores date value of entity.
     */
    @Column
    private Date              date;
    
    /**
     * Stores number value of entity.
     */
    @Column
    private String          number;
    
    /**
     * Stores amount value of entity.
     */
    @Column
    private Double            amount;
    
    /**
     * Stores text value of entity.
     */
    @Column
    private String            text;
    
    
    @Column(name="residual_amount")
    private Double			 residualAmount;
    
    
    @Column(name="report_number")
    private String			 reportNumber;
    
    @ManyToOne
	@JoinColumn(name = "temp_document_id")
	private Documents				tempDocument;
    
	@Column(name = "hierarchical_level_code", length = 100)
	private String hierarchicalLevelCode;
	
	@Column(name = "assigment_procedure_id")
	private Long assigmentProcedure;
	
    
    /**
     * Sets actType
     * 
     * @param actType
     *            the actType to set
     */
    public void setActType(ActTypes actType)
    {
        this.actType = actType;
    }
    
    /**
     * Gets actType
     * 
     * @return actType the actType
     */
    @Export(propertyName = "exportActType", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
    public ActTypes getActType()
    {
        return actType;
    }
    
    /**
     * Sets date
     * 
     * @param date
     *            the date to set
     */
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    /**
     * Gets date
     * 
     * @return date the date
     */
    @Export(propertyName = "exportDate", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
    public Date getDate()
    {
        return date;
    }
    

    
    /**
	 * Gets number
	 * @return number the number
	 */
    @Export(propertyName = "exportNumber", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
	public String getNumber()
	{
		return number;
	}

	/**
	 * Sets number
	 * @param number the number to set
	 */
	public void setNumber(String number)
	{
		this.number = number;
	}

	/**
     * Sets amount
     * 
     * @param amount
     *            the amount to set
     */

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }
    
    /**
     * Gets amount
     * 
     * @return amount the amount
     */
    @Export(propertyName = "exportAmount", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
    public Double getAmount()
    {
        return amount;
    }
    
    /**
     * Sets text
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text)
    {
        this.text = text;
    }
    
    /**
     * Gets text
     * 
     * @return text the text
     */
    
    @Export(propertyName = "exportNote", seqXLS = 6, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
    public String getText()
    {
        return text;
    }
    
    /**
     * Sets project
     * 
     * @param project
     *            the project to set
     */
    public void setProject(Projects project)
    {
        this.project = project;
    }
    
    /**
     * Gets project
     * 
     * @return project the project
     */
    public Projects getProject()
    {
        return project;
    }

	/**
	 * Gets tempDocument
	 * @return tempDocument the tempDocument
	 */
	public Documents getTempDocument()
	{
		return tempDocument;
	}

	/**
	 * Sets tempDocument
	 * @param tempDocument the tempDocument to set
	 */
	public void setTempDocument(Documents tempDocument)
	{
		this.tempDocument = tempDocument;
	}

	public CFPartnerAnagraphs getCreatePartner()
	{
		return createPartner;
	}

	public void setCreatePartner(CFPartnerAnagraphs createPartner)
	{
		this.createPartner = createPartner;
	}
	
    @Export(propertyName = "exportResidualAmount", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
	public Double getResidualAmount()
	{
		return residualAmount;
	}
	
	public void setResidualAmount(Double residualAmount)
	{
		this.residualAmount = residualAmount;
	}
	
    @Export(propertyName = "exportReportNumber", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.GiuridicalEngages)
	public String getReportNumber()
	{
		return reportNumber;
	}

	public void setReportNumber(String reportNumber)
	{
		this.reportNumber = reportNumber;
	}	
	
	/**
	 * Gets hierarchicalLevelCode.
	 * @return hierarchicalLevelCode the hierarchicalLevelCode
	 */
	public String getHierarchicalLevelCode() {
		return hierarchicalLevelCode;
	}
	
	/**
	 * Sets hierarchicalLevelCode.
	 * @param hierarchicalLevelCode the hierarchicalLevelCode to set
	 */
	public void setHierarchicalLevelCode(String hierarchicalLevelCode) {
		this.hierarchicalLevelCode = hierarchicalLevelCode;
	}

	/**
	 * Gets assigmentProcedureId
	 * @return assigmentProcedureId the assigmentProcedureId
	 */
	public Long getAssigmentProcedure() {
		return assigmentProcedure;
	}

	/**
	 * Sets assigmentProcedureId.
	 * @param assigmentProcedureId the assigmentProcedureId to set
	 */
	public void setAssigmentProcedure(Long assigmentProcedure) {
		this.assigmentProcedure = assigmentProcedure;
	}
	
	
}
