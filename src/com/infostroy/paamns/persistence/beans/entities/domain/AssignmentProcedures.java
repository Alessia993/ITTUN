/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ReasonForAbsenceCIG;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "assignment_procedures")
public class AssignmentProcedures extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = -7824583194401950841L;

	/**
	 * Stores tipology value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private ProcedureTipology tipology;

	@ManyToOne
	@JoinColumn
	private ReasonForAbsenceCIG reasonForAbsenceCIG;

	@Column(columnDefinition="VARCHAR(1500)")
	private String procedureDescription;

	@Column(name = "award_date")
	private Date awardDate;
	
	@Column(name = "publication_date")
	private Date publicationDate;

	/**
	 * Stores project value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Projects project;

	/**
	 * Stores description value of entity.
	 */
	@Column
	private String description;

	/**
	 * Stores cigProcedure value of entity.
	 */
	@Column(name = "cig_procedure", columnDefinition="VARCHAR(10)")
	private String cigProcedure;

	/**
	 * Stores baseProcedureAmount value of entity.
	 */
	@Column(name = "base_procedure_amount")
	private Double baseProcedureAmount;

	/**
	 * Stores assignedProcedureAmount value of entity.
	 */
	@Column(name = "assigned_procedure_amount")
	private Double assignedProcedureAmount;

	/**
	 * Stores differentPercentage value of entity.
	 */
	@Column(name = "different_percentage")
	private Double differentPercentage;

	/**
	 * Stores Note value of entity.
	 */
	@Column
	private String Note;

	@ManyToOne
	@JoinColumn(name = "document_id")
	private Documents document;

	@ManyToOne
	@JoinColumn
	private Users user;

	/**
	 * Sets tipology
	 * 
	 * @param tipology
	 *            the tipology to set
	 */
	public void setTipology(ProcedureTipology tipology) {
		this.tipology = tipology;
	}

	/**
	 * Gets tipology
	 * 
	 * @return tipology the tipology
	 */
	public ProcedureTipology getTipology() {
		return tipology;
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
	 * Sets cigProcedure
	 * 
	 * @param cigProcedure
	 *            the cigProcedure to set
	 */
	public void setCigProcedure(String cigProcedure) {
		this.cigProcedure = cigProcedure;
	}

	/**
	 * Gets cigProcedure
	 * 
	 * @return cigProcedure the cigProcedure
	 */
	public String getCigProcedure() {
		return cigProcedure;
	}

	/**
	 * Sets baseProcedureAmount
	 * 
	 * @param baseProcedureAmount
	 *            the baseProcedureAmount to set
	 */
	public void setBaseProcedureAmount(Double baseProcedureAmount) {
		this.baseProcedureAmount = baseProcedureAmount;
	}

	/**
	 * Gets baseProcedureAmount
	 * 
	 * @return baseProcedureAmount the baseProcedureAmount
	 */
	public Double getBaseProcedureAmount() {
		return baseProcedureAmount;
	}

	/**
	 * Sets assignedProcedureAmount
	 * 
	 * @param assignedProcedureAmount
	 *            the assignedProcedureAmount to set
	 */
	public void setAssignedProcedureAmount(Double assignedProcedureAmount) {
		this.assignedProcedureAmount = assignedProcedureAmount;
	}

	/**
	 * Gets assignedProcedureAmount
	 * 
	 * @return assignedProcedureAmount the assignedProcedureAmount
	 */
	public Double getAssignedProcedureAmount() {
		return assignedProcedureAmount;
	}

	/**
	 * Sets differentPercentage
	 * 
	 * @param differentPercentage
	 *            the differentPercentage to set
	 */
	public void setDifferentPercentage(Double differentPercentage) {
		this.differentPercentage = differentPercentage;
	}

	/**
	 * Gets differentPercentage
	 * 
	 * @return differentPercentage the differentPercentage
	 */
	public Double getDifferentPercentage() {
		return differentPercentage;
	}

	/**
	 * Sets Note
	 * 
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		Note = note;
	}

	/**
	 * Gets Note
	 * 
	 * @return Note the note
	 */
	public String getNote() {
		return Note;
	}

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
	 * Gets document
	 * 
	 * @return document the document
	 */
	public Documents getDocument() {
		return document;
	}

	/**
	 * Sets document
	 * 
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Documents document) {
		this.document = document;
	}

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public Users getUser() {
		return user;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	public ReasonForAbsenceCIG getReasonForAbsenceCIG() {
		return reasonForAbsenceCIG;
	}

	public void setReasonForAbsenceCIG(ReasonForAbsenceCIG reasonForAbsenceCIG) {
		this.reasonForAbsenceCIG = reasonForAbsenceCIG;
	}

	public String getProcedureDescription() {
		return procedureDescription;
	}

	public void setProcedureDescription(String procedureDescription) {
		this.procedureDescription = procedureDescription;
	}

	public Date getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

}
