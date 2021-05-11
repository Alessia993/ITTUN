package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.Countries;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.JmcDecision;
import com.infostroy.paamns.common.enums.OrganizationType;
import com.infostroy.paamns.common.enums.PartnerTypes;
import com.infostroy.paamns.common.enums.PotentialProjectStatuses;
import com.infostroy.paamns.common.enums.ProgramPriority;
import com.infostroy.paamns.common.enums.ProgramPriorityInvestment;
import com.infostroy.paamns.common.enums.ProvinceDepartment;
import com.infostroy.paamns.common.enums.SpecificObjective;

@Entity
@Table(name = "potential_projects")
public class PotentialProjects extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6648700603222562987L;

	@Column(name = "project_acronym")
	private String projectAcronym;

	@Column(name = "project_title")
	private String projectTitle;

	@Column(name = "lead_name_organization")
	private String leadNameOrganization;

	// new acagnoni 2019/03/18
	@Column(name = "univocal_id_lead_partners")
	private String univocalIdLeadPartners;

	// new acagnoni 2019/03/18
	@Column(name = "call_serial_number")
	private String callSerialNumber;

	// new acagnoni 2019/03/18
	@Column(name = "univocal_identification")
	private String univocalIdentification;

	// new acagnoni 2019/03/18
	@Column(name = "organization_name_english")
	private String organizationNameEnglish;

	// new acagnoni 2019/03/18
	@Column(name = "contact_person_first_name")
	private String contactPersonFirstName;

	// new acagnoni 2019/03/18
	@Column(name = "contact_person_last_name")
	private String contactPersonLastName;

	// new acagnoni 2019/03/18
	@Column(name = "organization_url")
	private String organizationURL;

	// new acagnoni 2019/03/18
	@Enumerated(EnumType.STRING)
	@Column(name = "organization_type")
	private OrganizationType organizationType;

	// new acagnoni 2019/03/18
	@Column(name = "project_name_english")
	private String projectNameEnglish;

	// new acagnoni 2019/03/18
	@Column(name = "proposal_total_budget")
	private Double proposalTotalBudget;

	// new acagnoni 2019/03/18
	@Column(name = "total_eu_funding_requested")
	private Double totalEUFundingRequested;

	// new acagnoni 2019/03/18
	@Column(name = "date_decision_jmc")
	private Date dateDecisionJMC;

	// new acagnoni 2019/03/18
	@Enumerated(EnumType.STRING)
	@Column(name = "decision_jmc")
	private JmcDecision decisionJMC;

	// new acagnoni 2019/03/18
	@Column(name = "date_appeal")
	private Date dateAppeal;

	@Enumerated(EnumType.STRING)
	@Column(name = "program_priority")
	private ProgramPriority programPriority;

	@Enumerated(EnumType.STRING)
	@Column(name = "program_priority_investment")
	private ProgramPriorityInvestment programPriorityInvestment;

	@Enumerated(EnumType.STRING)
	@Column(name = "specific_objective")
	private SpecificObjective specificObjective;

	@Column(name = "lead_partner_acronym")
	private String leadPartnerAcronym;

	@Column(name = "registered_office_address")
	private String registeredOfficeAddress;

	@Column(name = "department")
	private String department;

	@Column(name = "head_office_address")
	private String headOfficeAddress;

	@Enumerated(EnumType.STRING)
	@Column(name = "province_department")
	private ProvinceDepartment provinceDepartment;

	@Enumerated(EnumType.STRING)
	@Column(name = "country")
	private Countries country;

	@Column(name = "outside_area_cooperation")
	private Boolean outsideAreaCooperation;

	@Column(name = "name_of_representative")
	private String nameOfRepresentative;

	@Column(name = "phone")
	private String phone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "email")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "partner_type")
	private PartnerTypes partnerType;

	@Column(name = "tax_code")
	private String taxCode;

	@Enumerated(EnumType.STRING)
	@Column
	private PotentialProjectStatuses status;

	@ManyToOne
	@JoinColumn
	private Users user;

	@Column(name = "update_date")
	private Date updateDate;

	@OneToMany(mappedBy = "potentialProjects")
	private List<PotentialProjectsToDocuments> potentialProjectsToDocuments;

	@Column(name = "presentation_date")
	private Date presentationDate;

	@Export(propertyName = "documents", seqXLS = 6, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public String getFilesDescription() {
		StringBuilder result = new StringBuilder();
		if (getPotentialProjectsToDocuments() != null) {
			for (PotentialProjectsToDocuments document : getPotentialProjectsToDocuments()) {
				if (document != null && document.getDocument() != null
						&& document.getDocument().getDescription() != null
						&& !document.getDocument().getDescription().isEmpty()) {
					result.append(document.getDocument().getDescription()).append(";")
							.append(System.getProperty("line.separator"));
				}
			}
		}
		return result.toString();
	}

	public boolean isFromApproveStatus() {
		return status == PotentialProjectStatuses.FROM_APPROVE;
	}

	@Export(propertyName = "potentialProjectsProjectAcronym", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public String getProjectAcronym() {
		return projectAcronym;
	}

	public void setProjectAcronym(String projectAcronym) {
		this.projectAcronym = projectAcronym;
	}

	@Export(propertyName = "potentialProjectsProjectTitle", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	@Export(propertyName = "potentialProjectsLeadNameOrganization", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public String getLeadNameOrganization() {
		return leadNameOrganization;
	}

	public void setLeadNameOrganization(String leadNameOrganization) {
		this.leadNameOrganization = leadNameOrganization;
	}

	public String getOrganizationNameEnglish() {
		return organizationNameEnglish;
	}

	public void setOrganizationNameEnglish(String organizationNameEnglish) {
		this.organizationNameEnglish = organizationNameEnglish;
	}

	@Export(propertyName = "potentialProjectsProgramPriority", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public ProgramPriority getProgramPriority() {
		return programPriority;
	}

	public void setProgramPriority(ProgramPriority programPriority) {
		this.programPriority = programPriority;
	}

	@Export(propertyName = "potentialProjectsProgramPriorityInvestment", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.PotentialProjects)
	public ProgramPriorityInvestment getProgramPriorityInvestment() {
		return programPriorityInvestment;
	}

	public void setProgramPriorityInvestment(ProgramPriorityInvestment programPriorityInvestment) {
		this.programPriorityInvestment = programPriorityInvestment;
	}

	public SpecificObjective getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(SpecificObjective specificObjective) {
		this.specificObjective = specificObjective;
	}

	public String getLeadPartnerAcronym() {
		return leadPartnerAcronym;
	}

	public void setLeadPartnerAcronym(String leadPartnerAcronym) {
		this.leadPartnerAcronym = leadPartnerAcronym;
	}

	public String getRegisteredOfficeAddress() {
		return registeredOfficeAddress;
	}

	public void setRegisteredOfficeAddress(String registeredOfficeAddress) {
		this.registeredOfficeAddress = registeredOfficeAddress;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHeadOfficeAddress() {
		return headOfficeAddress;
	}

	public void setHeadOfficeAddress(String headOfficeAddress) {
		this.headOfficeAddress = headOfficeAddress;
	}

	public ProvinceDepartment getProvinceDepartment() {
		return provinceDepartment;
	}

	public void setProvinceDepartment(ProvinceDepartment provinceDepartment) {
		this.provinceDepartment = provinceDepartment;
	}

	public Countries getCountry() {
		return country;
	}

	public void setCountry(Countries country) {
		this.country = country;
	}

	public Boolean getOutsideAreaCooperation() {
		return outsideAreaCooperation;
	}

	public void setOutsideAreaCooperation(Boolean outsideAreaCooperation) {
		this.outsideAreaCooperation = outsideAreaCooperation;
	}

	public String getNameOfRepresentative() {
		return nameOfRepresentative;
	}

	public void setNameOfRepresentative(String nameOfRepresentative) {
		this.nameOfRepresentative = nameOfRepresentative;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PartnerTypes getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerTypes partnerType) {
		this.partnerType = partnerType;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public PotentialProjectStatuses getStatus() {
		return status;
	}

	public void setStatus(PotentialProjectStatuses status) {
		this.status = status;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<PotentialProjectsToDocuments> getPotentialProjectsToDocuments() {
		return potentialProjectsToDocuments;
	}

	public void setPotentialProjectsToDocuments(List<PotentialProjectsToDocuments> potentialProjectsToDocuments) {
		this.potentialProjectsToDocuments = potentialProjectsToDocuments;
	}

	public Date getPresentationDate() {
		return presentationDate;
	}

	public void setPresentationDate(Date presentationDate) {
		this.presentationDate = presentationDate;
	}

	public String getContactPersonFirstName() {
		return contactPersonFirstName;
	}

	public void setContactPersonFirstName(String contactPersonFirstName) {
		this.contactPersonFirstName = contactPersonFirstName;
	}

	public String getContactPersonLastName() {
		return contactPersonLastName;
	}

	public void setContactPersonLastName(String contactPersonLastName) {
		this.contactPersonLastName = contactPersonLastName;
	}

	public String getOrganizationURL() {
		return organizationURL;
	}

	public void setOrganizationURL(String organizationURL) {
		this.organizationURL = organizationURL;
	}

	public OrganizationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public String getProjectNameEnglish() {
		return projectNameEnglish;
	}

	public void setProjectNameEnglish(String projectNameEnglish) {
		this.projectNameEnglish = projectNameEnglish;
	}

	public Double getProposalTotalBudget() {
		return proposalTotalBudget;
	}

	public void setProposalTotalBudget(Double proposalTotalBudget) {
		this.proposalTotalBudget = proposalTotalBudget;
	}

	public Double getTotalEUFundingRequested() {
		return totalEUFundingRequested;
	}

	public void setTotalEUFundingRequested(Double totalEUFundingRequested) {
		this.totalEUFundingRequested = totalEUFundingRequested;
	}

	public String getCallSerialNumber() {
		return callSerialNumber;
	}

	public void setCallSerialNumber(String callSerialNumber) {
		this.callSerialNumber = callSerialNumber;
	}

	public String getUnivocalIdentification() {
		return univocalIdentification;
	}

	public void setUnivocalIdentification(String univocalIdentification) {
		this.univocalIdentification = univocalIdentification;
	}

	public String getUnivocalIdLeadPartners() {
		return univocalIdLeadPartners;
	}

	public void setUnivocalIdLeadPartners(String univocalIdLeadPartners) {
		this.univocalIdLeadPartners = univocalIdLeadPartners;
	}

	public Date getDateDecisionJMC() {
		return dateDecisionJMC;
	}

	public void setDateDecisionJMC(Date dateDecisionJMC) {
		this.dateDecisionJMC = dateDecisionJMC;
	}

	public JmcDecision getDecisionJMC() {
		return decisionJMC;
	}

	public void setDecisionJMC(JmcDecision decisionJMC) {
		this.decisionJMC = decisionJMC;
	}
	
	public Date getDateAppeal() {
		return dateAppeal;
	}

	public void setDateAppeal(Date dateAppeal) {
		this.dateAppeal = dateAppeal;
	}


}
