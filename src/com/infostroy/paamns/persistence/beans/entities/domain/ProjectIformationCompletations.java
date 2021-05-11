/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.EconomicActivities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.FinancingForms;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "project_iformation_completations")
public class ProjectIformationCompletations extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = 6551758073184087490L;

	/**
	 * Stores project value of entity.
	 */
	@OneToOne
	@JoinColumn
	private Projects project;

	private Date dateAdmissionToFinancing;

	private Date dateOfSubmission;

	private Date startYear;
	
	private Date endYear;

	/**
	 * Stores description value of entity.
	 */
	@Column
	private String description;
	
	/**
	 * Stores description value of entity.
	 */
	@Column
	private String descriptionEng;	

	/**
	 * Stores yearDuration value of entity.
	 */
	@Column(name = "year_duration")
	private Integer yearDuration;

	/**
	 * Stores monthDuration value of entity.
	 */
	@Column(name = "month_duration")
	private Integer monthDuration;

	/**
	 * Stores cupCode value of entity.
	 */
	@Column(name = "cup_code")
	private String cupCode;

	/**
	 * Stores cupAttachment value of entity.
	 */
	@Column(name = "cup_attachment")
	private String cupAttachment;

	/**
	 * Stores economicActivity value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "economic_activity_id")
	private EconomicActivities economicActivity;

	@ManyToOne
	@JoinColumn(name = "financing_forms_id")
	private FinancingForms financingForms;

	/**
	 * Stores inboundAmmount value of entity.
	 */
	@Column(name = "inbound_ammount")
	private Boolean inboundAmmount;

	/**
	 * Stores l442001 value of entity.
	 */
	@Column
	private Boolean l442001;

	/**
	 * Stores otherFound value of entity.
	 */
	@Column(name = "other_found")
	private Boolean otherFound;

	/**
	 * Stores bigProject value of entity.
	 */
	@Column(name = "big_project")
	private Boolean bigProject;

	/**
	 * Stores occupationalFinancing value of entity.
	 */
	@Column(name = "occupational_financing")
	private Boolean occupationalFinancing;

	/**
	 * Stores actionPlan value of entity.
	 */
	@Column(name = "action_plan")
	private Boolean actionPlan;

	/**
	 * Stores financingCode value of entity.
	 */
	@Column(name = "financing_code")
	private String financingCode;

	/**
	 * Stores financingCode value of entity.
	 */
	@Column(name = "territorial_code")
	private String territorialCode;

	/**
	 * Stores financingCode value of entity.
	 */
	@Column(name = "actual_code")
	private String actualCode;

	/**
	 * Stores note value of entity.
	 */
	@Column
	private String note;
	
	@Column
	private String results;
	
	@Column
	private String engResults;
	
	@Column
	private String url;

	/**
	 * Stores picDocument value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "pic_document_id")
	private Documents picDocument;

	@ManyToOne
	@JoinColumn(name = "pic_documentadm_id")
	private DocumentsAdm picDocumentAdm;

	/**
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project) {
		this.project = project;
	}

	/**
	 * Gets project
	 * 
	 * @return project the project
	 */
	public Projects getProject() {
		return project;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets description
	 * 
	 * @return description the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescriptionEng(String descriptionEng) {
		this.descriptionEng = descriptionEng;
	}

	/**
	 * Gets description
	 * 
	 * @return description the description
	 */
	public String getDescriptionEng() {
		return descriptionEng;
	}

	/**
	 * @return dateAdmissionToFinancing
	 */
	public Date getDateAdmissionToFinancing() {
		return dateAdmissionToFinancing;
	}

	/**
	 * set Date Admission To Financing
	 * 
	 * @param dateAdmissionToFinancing
	 */
	public void setDateAdmissionToFinancing(Date dateAdmissionToFinancing) {
		this.dateAdmissionToFinancing = dateAdmissionToFinancing;
	}

	/**
	 * Sets yearDuration
	 * 
	 * @param yearDuration
	 *            the yearDuration to set
	 */
	public void setYearDuration(Integer yearDuration) {
		this.yearDuration = yearDuration;
	}

	/**
	 * Gets yearDuration
	 * 
	 * @return yearDuration the yearDuration
	 */
	public Integer getYearDuration() {
		return yearDuration;
	}

	/**
	 * Sets monthDuration
	 * 
	 * @param monthDuration
	 *            the monthDuration to set
	 */
	public void setMonthDuration(Integer monthDuration) {
		this.monthDuration = monthDuration;
	}

	/**
	 * Gets monthDuration
	 * 
	 * @return monthDuration the monthDuration
	 */
	public Integer getMonthDuration() {
		return monthDuration;
	}

	/**
	 * Sets cupCode
	 * 
	 * @param cupCode
	 *            the cupCode to set
	 */
	public void setCupCode(String cupCode) {
		this.cupCode = cupCode;
	}

	/**
	 * Gets cupCode
	 * 
	 * @return cupCode the cupCode
	 */
	public String getCupCode() {
		return cupCode;
	}

	/**
	 * Sets cupAttachment
	 * 
	 * @param cupAttachment
	 *            the cupAttachment to set
	 */
	public void setCupAttachment(String cupAttachment) {
		this.cupAttachment = cupAttachment;
	}

	/**
	 * Gets cupAttachment
	 * 
	 * @return cupAttachment the cupAttachment
	 */
	public String getCupAttachment() {
		return cupAttachment;
	}

	/**
	 * Sets economicActivity
	 * 
	 * @param economicActivity
	 *            the economicActivity to set
	 */
	public void setEconomicActivity(EconomicActivities economicActivity) {
		this.economicActivity = economicActivity;
	}

	/**
	 * Gets economicActivity
	 * 
	 * @return economicActivity the economicActivity
	 */
	public EconomicActivities getEconomicActivity() {
		return economicActivity;
	}

	/**
	 * Sets inboundAmmount
	 * 
	 * @param inboundAmmount
	 *            the inboundAmmount to set
	 */
	public void setInboundAmmount(Boolean inboundAmmount) {
		this.inboundAmmount = inboundAmmount;
	}

	/**
	 * Gets inboundAmmount
	 * 
	 * @return inboundAmmount the inboundAmmount
	 */
	public boolean getInboundAmmount() {
		return inboundAmmount;
	}

	/**
	 * Sets l442001
	 * 
	 * @param l442001
	 *            the l442001 to set
	 */
	public void setL442001(Boolean l442001) {
		this.l442001 = l442001;
	}

	/**
	 * Gets l442001
	 * 
	 * @return l442001 the l442001
	 */
	public Boolean getL442001() {
		return l442001;
	}

	/**
	 * Sets otherFound
	 * 
	 * @param otherFound
	 *            the otherFound to set
	 */
	public void setOtherFound(Boolean otherFound) {
		this.otherFound = otherFound;
	}

	/**
	 * Gets otherFound
	 * 
	 * @return otherFound the otherFound
	 */
	public Boolean getOtherFound() {
		return otherFound;
	}

	/**
	 * Sets note
	 * 
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Gets note
	 * 
	 * @return note the note
	 */
	public String getNote() {
		return note;
	}
	
	
	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getEngResults() {
		return engResults;
	}

	public void setEngResults(String engResults) {
		this.engResults = engResults;
	}

	/**
	 * Sets url
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets url
	 * 
	 * @return url the note
	 */
	public String getUrl() {
		return url;
	}	

	public void setPicDocument(Documents picDocument) {
		this.picDocument = picDocument;
	}

	public Documents getPicDocument() {
		return picDocument;
	}

	public void setPicDocumentAdm(DocumentsAdm picDocumentAdm) {
		this.picDocumentAdm = picDocumentAdm;
	}

	public DocumentsAdm getPicDocumentAdm() {
		return picDocumentAdm;
	}

	/**
	 * Sets financingForms
	 * 
	 * @param financingForms
	 *            the financingForms to set
	 */
	public void setFinancingForms(FinancingForms financingForms) {
		this.financingForms = financingForms;
	}

	/**
	 * Gets financingForms
	 * 
	 * @return financingForms the financingForms
	 */
	public FinancingForms getFinancingForms() {
		return financingForms;
	}

	/**
	 * Sets startYear
	 * 
	 * @param startYear
	 *            the startYear to set
	 */
	public void setStartYear(Date startYear) {
		this.startYear = startYear;
	}

	/**
	 * Gets startYear
	 * 
	 * @return startYear the startYear
	 */
	public Date getStartYear() {
		return startYear;
	}

	/**
	 * Sets endYear
	 * 
	 * @param endYear
	 *            the endYear to set
	 */
	public void setEndYear(Date endYear) {
		this.endYear = endYear;
	}

	/**
	 * Gets endYear
	 * 
	 * @return endYear the endYear
	 */
	public Date getEndYear() {
		return endYear;
	}	
	
	/**
	 * @return the bigProject
	 */
	public Boolean getBigProject() {
		return bigProject;
	}

	/**
	 * @param bigProject
	 *            the bigProject to set
	 */
	public void setBigProject(Boolean bigProject) {
		this.bigProject = bigProject;
	}

	/**
	 * @return the occupationalFinancing
	 */
	public Boolean getOccupationalFinancing() {
		return occupationalFinancing;
	}

	/**
	 * @param occupationalFinancing
	 *            the occupationalFinancing to set
	 */
	public void setOccupationalFinancing(Boolean occupationalFinancing) {
		this.occupationalFinancing = occupationalFinancing;
	}

	/**
	 * @return the actionPlan
	 */
	public Boolean getActionPlan() {
		return actionPlan;
	}

	/**
	 * @param actionPlan
	 *            the actionPlan to set
	 */
	public void setActionPlan(Boolean actionPlan) {
		this.actionPlan = actionPlan;
	}

	/**
	 * @return the financingCode
	 */
	public String getFinancingCode() {
		return financingCode;
	}

	/**
	 * @param financingCode
	 *            the financingCode to set
	 */
	public void setFinancingCode(String financingCode) {
		this.financingCode = financingCode;
	}

	/**
	 * @return the territorialCode
	 */
	public String getTerritorialCode() {
		return territorialCode;
	}

	/**
	 * @param territorialCode
	 *            the territorialCode to set
	 */
	public void setTerritorialCode(String territorialCode) {
		this.territorialCode = territorialCode;
	}

	/**
	 * @return the actualCode
	 */
	public String getActualCode() {
		return actualCode;
	}

	/**
	 * @param actualCode
	 *            the actualCode to set
	 */
	public void setActualCode(String actualCode) {
		this.actualCode = actualCode;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

}
