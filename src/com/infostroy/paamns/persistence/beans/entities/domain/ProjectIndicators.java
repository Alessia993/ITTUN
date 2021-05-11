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

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2014.
 *
 */

@Entity
@Table(name = "projects_indicators")
public class ProjectIndicators extends PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long serialVersionUID = -330244975138542772L;

	@Column(length = 10000)
	private String name;

	@Column(name = "measure_unit")
	private String measureUnit;

	@Column
	private String target;

	@Column(name = "product_code")
	private String productCode;

	@Column
	private String phase;

	@Column(name = "typology_product_indicator")
	private Long typologyProductIndicator;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Projects project;

	@Column(name = "asse1")
	private String asse1;

	@Column(name = "asse2")
	private String asse2;

	@Column(name = "asse3")
	private String asse3;

	@Column(name = "asse4")
	private String asse4;

	@Column(name = "asse5")
	private String asse5;

	@Column(name = "asse6")
	private String asse6;

	@Column(name = "asse7")
	private String asse7;

	@Column(name = "asse8")
	private String asse8;

	@Column(name = "asse9")
	private String asse9;

	@Column(name = "asse10")
	private String asse10;

	@Column(name = "specific_objective")
	private String specificObjective;

	@Column(name = "communication_objective")
	private String communicationObjective;

	@Column(name = "target_group")
	private String targetGroup;

	@Column(name = "indicator_id")
	private Long indicatorId;

	@Column(name = "program_indicator_id")
	private Long programIndicatorId;

	@Column(name = "expected_date_realization")
	private Date expectedDateRealization;

	@Column(name = "actual_date_realization")
	private Date actualDateRealization;

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets measureUnit
	 * 
	 * @return measureUnit the measureUnit
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * Sets measureUnit
	 * 
	 * @param measureUnit
	 *            the measureUnit to set
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * Gets target
	 * 
	 * @return target the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets target
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Gets phase
	 * 
	 * @return phase the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * Sets phase
	 * 
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
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
	 * Sets project
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(Projects project) {
		this.project = project;
	}

	/**
	 * Gets asse1
	 * 
	 * @return asse1 the asse1
	 */
	public String getAsse1() {
		return asse1;
	}

	/**
	 * Sets asse1
	 * 
	 * @param asse1
	 *            the asse1 to set
	 */
	public void setAsse1(String asse1) {
		this.asse1 = asse1;
	}

	/**
	 * Gets asse2
	 * 
	 * @return asse2 the asse2
	 */
	public String getAsse2() {
		return asse2;
	}

	/**
	 * Sets asse2
	 * 
	 * @param asse2
	 *            the asse2 to set
	 */
	public void setAsse2(String asse2) {
		this.asse2 = asse2;
	}

	/**
	 * Gets asse3
	 * 
	 * @return asse3 the asse3
	 */
	public String getAsse3() {
		return asse3;
	}

	/**
	 * Sets asse3
	 * 
	 * @param asse3
	 *            the asse3 to set
	 */
	public void setAsse3(String asse3) {
		this.asse3 = asse3;
	}

	/**
	 * Gets asse4
	 * 
	 * @return asse4 the asse4
	 */
	public String getAsse4() {
		return asse4;
	}

	/**
	 * Sets asse4
	 * 
	 * @param asse4
	 *            the asse4 to set
	 */
	public void setAsse4(String asse4) {
		this.asse4 = asse4;
	}

	/**
	 * Gets asse5
	 * 
	 * @return asse5 the asse5
	 */
	public String getAsse5() {
		return asse5;
	}

	/**
	 * Sets asse5
	 * 
	 * @param asse5
	 *            the asse5 to set
	 */
	public void setAsse5(String asse5) {
		this.asse5 = asse5;
	}

	/**
	 * Gets asse6
	 * 
	 * @return asse6 the asse6
	 */
	public String getAsse6() {
		return asse6;
	}

	/**
	 * Sets asse6
	 * 
	 * @param asse6
	 *            the asse6 to set
	 */
	public void setAsse6(String asse6) {
		this.asse6 = asse6;
	}

	/**
	 * Gets asse7
	 * 
	 * @return asse7 the asse7
	 */
	public String getAsse7() {
		return asse7;
	}

	/**
	 * Sets asse7
	 * 
	 * @param asse7
	 *            the asse7 to set
	 */
	public void setAsse7(String asse7) {
		this.asse7 = asse7;
	}

	/**
	 * Gets asse8
	 * 
	 * @return asse8 the asse8
	 */
	public String getAsse8() {
		return asse8;
	}

	/**
	 * Sets asse8
	 * 
	 * @param asse8
	 *            the asse8 to set
	 */
	public void setAsse8(String asse8) {
		this.asse8 = asse8;
	}

	/**
	 * Gets asse9
	 * 
	 * @return asse9 the asse9
	 */
	public String getAsse9() {
		return asse9;
	}

	/**
	 * Sets asse9
	 * 
	 * @param asse9
	 *            the asse9 to set
	 */
	public void setAsse9(String asse9) {
		this.asse9 = asse9;
	}

	/**
	 * Gets asse10
	 * 
	 * @return asse10 the asse10
	 */
	public String getAsse10() {
		return asse10;
	}

	/**
	 * Sets asse10
	 * 
	 * @param asse10
	 *            the asse10 to set
	 */
	public void setAsse10(String asse10) {
		this.asse10 = asse10;
	}

	/**
	 * Gets specificObjective
	 * 
	 * @return specificObjective the specificObjective
	 */
	public String getSpecificObjective() {
		return specificObjective;
	}

	/**
	 * Sets specificObjective
	 * 
	 * @param specificObjective
	 *            the specificObjective to set
	 */
	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	/**
	 * Gets communicationObjective
	 * 
	 * @return communicationObjective the communicationObjective
	 */
	public String getCommunicationObjective() {
		return communicationObjective;
	}

	/**
	 * Sets communicationObjective
	 * 
	 * @param communicationObjective
	 *            the communicationObjective to set
	 */
	public void setCommunicationObjective(String communicationObjective) {
		this.communicationObjective = communicationObjective;
	}

	/**
	 * Gets targetGroup
	 * 
	 * @return targetGroup the targetGroup
	 */
	public String getTargetGroup() {
		return targetGroup;
	}

	/**
	 * Sets targetGroup
	 * 
	 * @param targetGroup
	 *            the targetGroup to set
	 */
	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}

	/**
	 * Gets indicatorId
	 * 
	 * @return indicatorId the indicatorId
	 */
	public Long getIndicatorId() {
		return indicatorId;
	}

	/**
	 * Sets indicatorId
	 * 
	 * @param indicatorId
	 *            the indicatorId to set
	 */
	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}

	public Long getTypologyProductIndicator() {
		return typologyProductIndicator;
	}

	public void setTypologyProductIndicator(Long typologyProductIndicator) {
		this.typologyProductIndicator = typologyProductIndicator;
	}

	public Long getProgramIndicatorId() {
		return programIndicatorId;
	}

	public void setProgramIndicatorId(Long programIndicatorId) {
		this.programIndicatorId = programIndicatorId;
	}

	public Date getExpectedDateRealization() {
		return expectedDateRealization;
	}

	public void setExpectedDateRealization(Date expectedDateRealization) {
		this.expectedDateRealization = expectedDateRealization;
	}

	public Date getActualDateRealization() {
		return actualDateRealization;
	}

	public void setActualDateRealization(Date actualDateRealization) {
		this.actualDateRealization = actualDateRealization;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
