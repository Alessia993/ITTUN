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
import javax.persistence.Transient;

import com.infostroy.paamns.persistence.SessionManager;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "local_checks")
public class LocalChecks extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long	serialVersionUID	= 5611341835026721801L;

	/**
	 * Stores project value of entity.
	 */
	@ManyToOne
	@JoinColumn
	private Projects			project;

	/**
	 * Stores checkStartDate value of entity.
	 */
	@Column(name = "check_start_date")
	private Date				checkStartDate;

	/**
	 * Stores evaluation value of entity.
	 */
	@Column
	private String				evaluation;

	/**
	 * Stores controlexpense value of entity.
	 */
	@Column
	private String				controlexpense;	
	
	
	/**
	 * 
	 * // new acagnoni 2019/03/18
	 * Stores finalAmountIneligibleExpenditure value of entity.
	 */
	@Column
	private String				finalAmountIneligibleExpenditure;	
	


	/**
	 * Stores inelegibleexpense value of entity.
	 */
	@Column
	private String				ineligibleexpense;		
	
	/**
	 * Stores results value of entity.
	 */
	@Column
	private String				results;

	/**
	 * Stores resume value of entity.
	 */
	@Column
	private String				resume;

	/**
	 * Stores checkClosureDate value of entity.
	 */
	@Column(name = "check_closure_date")
	private Date				checkClosureDate;

	@Column(name = "date_of_issue_provisional_control")
	private Date				dateOfIssueprovisionalControl;
	
	/**
	 * Stores docuent value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn
	private Documents			docuent;

	/**
	 * Stores closedControl value of entity.
	 */
	@Column(name = "closed_control")
	private boolean				closedControl;

	@ManyToOne
	@JoinColumn(name = "partner_id")
	private CFPartnerAnagraphs	partner;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users				user;

	@Column(name = "inserted_user_name")
	private String				insertedUserName;

	@Column(name = "inserted_user_surname")
	private String				insertedUserSurname;

	@Column(name = "inserted_user_role")
	private String				insertedUserRole;
	
	@Column(name="protocol_number")
	private Integer				protocolNumber;

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
	 * Sets checkStartDate
	 * 
	 * @param checkStartDate
	 *            the checkStartDate to set
	 */
	public void setCheckStartDate(Date checkStartDate)
	{
		this.checkStartDate = checkStartDate;
	}

	/**
	 * Gets checkStartDate
	 * 
	 * @return checkStartDate the checkStartDate
	 */
	public Date getCheckStartDate()
	{
		return checkStartDate;
	}

	/**
	 * Sets evaluation
	 * 
	 * @param evaluation
	 *            the evaluation to set
	 */
	public void setEvaluation(String evaluation)
	{
		this.evaluation = evaluation;
	}

	/**
	 * Gets evaluation
	 * 
	 * @return evaluation the evaluation
	 */
	public String getEvaluation()
	{
		return evaluation;
	}

	public String getControlexpense() {
		return controlexpense;
	}

	public void setControlexpense(String controlexpense) {
		this.controlexpense = controlexpense;
	}

	

	public String getIneligibleexpense() {
		return ineligibleexpense;
	}

	public void setIneligibleexpense(String ineligibleexpense) {
		this.ineligibleexpense = ineligibleexpense;
	}

	/**
	 * Sets results
	 * 
	 * @param results
	 *            the results to set
	 */
	public void setResults(String results)
	{
		this.results = results;
	}

	/**
	 * Gets results
	 * 
	 * @return results the results
	 */
	public String getResults()
	{
		return results;
	}

	/**
	 * Sets resume
	 * 
	 * @param resume
	 *            the resume to set
	 */
	public void setResume(String resume)
	{
		this.resume = resume;
	}

	/**
	 * Gets resume
	 * 
	 * @return resume the resume
	 */
	public String getResume()
	{
		return resume;
	}

	/**
	 * Sets checkClosureDate
	 * 
	 * @param checkClosureDate
	 *            the checkClosureDate to set
	 */
	public void setCheckClosureDate(Date checkClosureDate)
	{
		this.checkClosureDate = checkClosureDate;
	}

	/**
	 * Gets checkClosureDate
	 * 
	 * @return checkClosureDate the checkClosureDate
	 */
	public Date getCheckClosureDate()
	{
		return checkClosureDate;
	}

	/**
	 * Sets docuent
	 * 
	 * @param docuent
	 *            the docuent to set
	 */
	public void setDocuent(Documents docuent)
	{
		this.docuent = docuent;
	}

	/**
	 * Gets docuent
	 * 
	 * @return docuent the docuent
	 */
	public Documents getDocuent()
	{
		return docuent;
	}

	/**
	 * Sets closedControl
	 * 
	 * @param closedControl
	 *            the closedControl to set
	 */
	public void setClosedControl(Boolean closedControl)
	{
		this.closedControl = closedControl;
	}

	/**
	 * Gets closedControl
	 * 
	 * @return closedControl the closedControl
	 */
	public Boolean getClosedControl()
	{
		return closedControl;
	}

	/**
	 * Gets partner
	 * 
	 * @return partner the partner
	 */
	public CFPartnerAnagraphs getPartner()
	{
		return partner;
	}

	public String getPartnerName()
	{
		return partner == null ? null : partner.getUser() == null ? partner
				.getName() : partner.getUser().getName() + ' '
				+ partner.getUser().getSurname();
	}

	/**
	 * Sets partner
	 * 
	 * @param partner
	 *            the partner to set
	 */
	public void setPartner(CFPartnerAnagraphs partner)
	{
		this.partner = partner;
	}

	@Transient
	public boolean getCanEdit()
	{
		return this.getUser() == null ? false : SessionManager.getInstance()
				.getSessionBean().getCurrentUser().getId()
				.equals(this.getUser().getId());
	}

	/**
	 * Gets user
	 * 
	 * @return user the user
	 */
	public Users getUser()
	{
		return user;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(Users user)
	{
		this.user = user;
	}

	/**
	 * Gets insertedUserName
	 * 
	 * @return insertedUserName the insertedUserName
	 */
	public String getInsertedUserName()
	{
		return insertedUserName;
	}

	/**
	 * Sets insertedUserName
	 * 
	 * @param insertedUserName
	 *            the insertedUserName to set
	 */
	public void setInsertedUserName(String insertedUserName)
	{
		this.insertedUserName = insertedUserName;
	}

	/**
	 * Gets insertedUserSurname
	 * 
	 * @return insertedUserSurname the insertedUserSurname
	 */
	public String getInsertedUserSurname()
	{
		return insertedUserSurname;
	}

	/**
	 * Sets insertedUserSurname
	 * 
	 * @param insertedUserSurname
	 *            the insertedUserSurname to set
	 */
	public void setInsertedUserSurname(String insertedUserSurname)
	{
		this.insertedUserSurname = insertedUserSurname;
	}

	/**
	 * Gets insertedUserRole
	 * 
	 * @return insertedUserRole the insertedUserRole
	 */
	public String getInsertedUserRole()
	{
		return insertedUserRole;
	}

	/**
	 * Sets insertedUserRole
	 * 
	 * @param insertedUserRole
	 *            the insertedUserRole to set
	 */
	public void setInsertedUserRole(String insertedUserRole)
	{
		this.insertedUserRole = insertedUserRole;
	}

	@Transient
	public String getUserFullname()
	{
		StringBuilder sb = new StringBuilder();

		if (this.getInsertedUserName() != null)
		{
			sb.append(this.getInsertedUserName());
		}

		if (this.getInsertedUserSurname() != null)
		{
			sb.append(this.getInsertedUserSurname());
		}

		return sb.toString();
	}

	/**
	 * Gets protocolNumber
	 * @return protocolNumber the protocolNumber
	 */
	public Integer getProtocolNumber()
	{
		return protocolNumber;
	}

	/**
	 * Sets protocolNumber
	 * @param protocolNumber the protocolNumber to set
	 */
	public void setProtocolNumber(Integer protocolNumber)
	{
		this.protocolNumber = protocolNumber;
	}

	public Date getDateOfIssueprovisionalControl() {
		return dateOfIssueprovisionalControl;
	}

	public void setDateOfIssueprovisionalControl(Date dateOfIssueprovisionalControl) {
		this.dateOfIssueprovisionalControl = dateOfIssueprovisionalControl;
	}
	
	
	public String getFinalAmountIneligibleExpenditure() {
		return finalAmountIneligibleExpenditure;
	}

	public void setFinalAmountIneligibleExpenditure(String finalAmountIneligibleExpenditure) {
		this.finalAmountIneligibleExpenditure = finalAmountIneligibleExpenditure;
	}
	
	

}
