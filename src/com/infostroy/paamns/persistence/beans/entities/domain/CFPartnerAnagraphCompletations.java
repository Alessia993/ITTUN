/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "cf_partner_anagraph_completations")
public class CFPartnerAnagraphCompletations extends com.infostroy.paamns.persistence.beans.entities.PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = -4587626557654451430L;

	/**
	 * Stores cfPartnerAnagraph value of entity.
	 */
	@ManyToOne
	@JoinColumn(name = "cf_partner_anagraph_project_id")
	private CFPartnerAnagraphProject cfPartnerAnagraphproject;

	@ManyToOne
	@JoinColumn
	private Projects project;

	/**
	 * Stores third naming value of entity.
	 */
	@Column
	private String naming;

	/**
	 * Stores first naming value of entity.
	 */

	@Column
	private String naming1;

	/**
	 * Stores second naming value of entity.
	 */

	@Column
	private String naming2;

	/**
	 * Stores atecoCode value of entity.
	 */
	@Column(name = "ateco_code_1")
	private String atecoCode1;

	@Column(name = "ateco_code_2")
	private String atecoCode2;

	@Column(name = "ateco_code_3")
	private String atecoCode3;

	@Column(name = "ateco_code_4")
	private String atecoCode4;

	@Column(name = "recoverable_vat")
	private Boolean recoverableVAT;

	@Column(name = "fesr_cn_value")
	private String fesrCnValue;

	/**
	 * Stores firstAddress value of entity.
	 */
	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "first_address_id")
	private Addresses firstAddress;

	/**
	 * Stores secondAddress value of entity.
	 */
	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "second_address_id")
	private Addresses secondAddress;

	/**
	 * Stores nut2 value of entity.
	 */
	@Column
	private Boolean nut2;

	/**
	 * Stores nut3 value of entity.
	 */
	@Column
	private Boolean nut3;

	/**
	 * Stores note value of entity.
	 */
	@Column
	private String note;

	/**
	 * Sets cfPartnerAnagraph
	 * 
	 * @param cfPartnerAnagraph
	 *            the cfPartnerAnagraph to set
	 */
	public void setCfPartnerAnagraphproject(CFPartnerAnagraphProject cfPartnerAnagraph) {
		this.cfPartnerAnagraphproject = cfPartnerAnagraph;
	}

	/**
	 * Gets cfPartnerAnagraph
	 * 
	 * @return cfPartnerAnagraph the cfPartnerAnagraph
	 */
	public CFPartnerAnagraphProject getCfPartnerAnagraphproject() {
		return cfPartnerAnagraphproject;
	}

	/**
	 * Sets naming
	 * 
	 * @param naming
	 *            the naming to set
	 */
	public void setNaming(String naming) {
		this.naming = naming;
	}

	/**
	 * Gets naming
	 * 
	 * @return naming the naming
	 */
	public String getNaming() {
		return naming;
	}

	/**
	 * Sets firstAddress
	 * 
	 * @param firstAddress
	 *            the firstAddress to set
	 */
	public void setFirstAddress(Addresses firstAddress) {
		this.firstAddress = firstAddress;
	}

	/**
	 * Gets firstAddress
	 * 
	 * @return firstAddress the firstAddress
	 */
	public Addresses getFirstAddress() {
		return firstAddress;
	}

	/**
	 * Sets secondAddress
	 * 
	 * @param secondAddress
	 *            the secondAddress to set
	 */
	public void setSecondAddress(Addresses secondAddress) {
		this.secondAddress = secondAddress;
	}

	/**
	 * Gets secondAddress
	 * 
	 * @return secondAddress the secondAddress
	 */
	public Addresses getSecondAddress() {
		return secondAddress;
	}

	/**
	 * Sets nut2
	 * 
	 * @param nut2
	 *            the nut2 to set
	 */
	public void setNut2(Boolean nut2) {
		this.nut2 = nut2;
	}

	/**
	 * Gets nut2
	 * 
	 * @return nut2 the nut2
	 */
	public Boolean getNut2() {
		return nut2;
	}

	/**
	 * Sets nut3
	 * 
	 * @param nut3
	 *            the nut3 to set
	 */
	public void setNut3(Boolean nut3) {
		this.nut3 = nut3;
	}

	/**
	 * Gets nut3
	 * 
	 * @return nut3 the nut3
	 */
	public Boolean getNut3() {
		return nut3;
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
	 * Gets atecoCode1
	 * 
	 * @return atecoCode1 the atecoCode1
	 */
	public String getAtecoCode1() {
		return atecoCode1;
	}

	/**
	 * Sets atecoCode1
	 * 
	 * @param atecoCode1
	 *            the atecoCode1 to set
	 */
	public void setAtecoCode1(String atecoCode1) {
		this.atecoCode1 = atecoCode1;
	}

	/**
	 * Gets atecoCode2
	 * 
	 * @return atecoCode2 the atecoCode2
	 */
	public String getAtecoCode2() {
		return atecoCode2;
	}

	/**
	 * Sets atecoCode2
	 * 
	 * @param atecoCode2
	 *            the atecoCode2 to set
	 */
	public void setAtecoCode2(String atecoCode2) {
		this.atecoCode2 = atecoCode2;
	}

	/**
	 * Gets atecoCode3
	 * 
	 * @return atecoCode3 the atecoCode3
	 */
	public String getAtecoCode3() {
		return atecoCode3;
	}

	/**
	 * Sets atecoCode3
	 * 
	 * @param atecoCode3
	 *            the atecoCode3 to set
	 */
	public void setAtecoCode3(String atecoCode3) {
		this.atecoCode3 = atecoCode3;
	}

	/**
	 * Gets atecoCode4
	 * 
	 * @return atecoCode4 the atecoCode4
	 */
	public String getAtecoCode4() {
		return atecoCode4;
	}

	/**
	 * Sets atecoCode4
	 * 
	 * @param atecoCode4
	 *            the atecoCode4 to set
	 */
	public void setAtecoCode4(String atecoCode4) {
		this.atecoCode4 = atecoCode4;
	}

	/**
	 * Gets naming1
	 * 
	 * @return naming1 the naming1
	 */
	public String getNaming1() {
		return naming1;
	}

	/**
	 * Sets naming1
	 * 
	 * @param naming1
	 *            the naming1 to set
	 */
	public void setNaming1(String naming1) {
		this.naming1 = naming1;
	}

	/**
	 * Gets naming2
	 * 
	 * @return naming2 the naming2
	 */
	public String getNaming2() {
		return naming2;
	}

	/**
	 * Sets naming2
	 * 
	 * @param naming2
	 *            the naming2 to set
	 */
	public void setNaming2(String naming2) {
		this.naming2 = naming2;
	}

	public Boolean getRecoverableVAT() {
		return recoverableVAT;
	}

	public void setRecoverableVAT(Boolean recoverableVAT) {
		this.recoverableVAT = recoverableVAT;
	}

	public String getFesrCnValue() {
		return fesrCnValue;
	}

	public void setFesrCnValue(String fesrCnValue) {
		this.fesrCnValue = fesrCnValue;
	}

}
