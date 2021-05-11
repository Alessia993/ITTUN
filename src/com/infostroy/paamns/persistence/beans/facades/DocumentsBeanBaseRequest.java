/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades;

import java.util.Date;

/**
 *
 * @author Volodymyr Posukhov
 * InfoStroy Co., 2013.
 *
 */
public class DocumentsBeanBaseRequest implements java.io.Serializable {
	private Long projectId;
	private String title;
	private	Date dateFrom;
	private Date dateTo;
	private String protocolNumber;
	private Long sectionId;
	private String documentNumber;
	private Date editDate;
	private boolean onlyDocuments;
	private String signflag;
	
	/**
	 * @param projectId
	 * @param title
	 * @param dateFrom
	 * @param dateTo
	 * @param criterion
	 * @param protocolNumber
	 * @param sectionId
	 * @param documentNumber
	 * @param signflag
	 */
	public DocumentsBeanBaseRequest(Long projectId, String title,
			Date dateFrom, Date dateTo, 
			String protocolNumber, Long sectionId, String documentNumber,Date documentDate,boolean onlyDocuments, String signflag) {
		this.projectId = projectId;
		this.title = title;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.protocolNumber = protocolNumber;
		this.sectionId = sectionId;
		this.documentNumber = documentNumber;
		this.editDate = documentDate;
		this.onlyDocuments = onlyDocuments;
		this.signflag = signflag;
	}
	/**
	 * Gets projectId
	 * @return projectId the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * Sets projectId
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * Gets title
	 * @return title the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets title
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Gets dateFrom
	 * @return dateFrom the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}
	/**
	 * Sets dateFrom
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	/**
	 * Gets dateTo
	 * @return dateTo the dateTo
	 */
	public Date getDateTo() {
		return dateTo;
	}
	/**
	 * Sets dateTo
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	/**
	 * Gets protocolNumber
	 * @return protocolNumber the protocolNumber
	 */
	public String getProtocolNumber() {
		return protocolNumber;
	}
	/**
	 * Sets protocolNumber
	 * @param protocolNumber the protocolNumber to set
	 */
	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
	
	/**
	 * Gets signflag
	 * @return signflag the signflag
	 */
	public String getSignFlag() {
		return signflag;
	}
	/**
	 * Sets signflag
	 * @param signflag the signflag to set
	 */
	public void setSignFlag(String signflag) {
		this.signflag = signflag;
	}	
	/**
	 * Gets sectionId
	 * @return sectionId the sectionId
	 */
	public Long getSectionId() {
		return sectionId;
	}
	/**
	 * Sets sectionId
	 * @param sectionId the sectionId to set
	 */
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}
	/**
	 * Gets documentNumber
	 * @return documentNumber the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}
	/**
	 * Sets documentNumber
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	/**
	 * Gets editDate
	 * @return editDate the editDate
	 */
	public Date getEditDate() {
		return editDate;
	}
	/**
	 * Sets editDate
	 * @param editDate the editDate to set
	 */
	public void setEditDate(Date documentDate) {
		this.editDate = documentDate;
	}
	/**
	 * Gets onlyDocuments
	 * @return onlyDocuments the onlyDocuments
	 */
	public boolean isOnlyDocuments()
	{
		return onlyDocuments;
	}
	/**
	 * Sets onlyDocuments
	 * @param onlyDocuments the onlyDocuments to set
	 */
	public void setOnlyDocuments(boolean onlyDocuments)
	{
		this.onlyDocuments = onlyDocuments;
	}
	
	
}
