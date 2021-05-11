/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 * 
 *
 * @author Sergey Manoylo InfoStroy Co., 2010. modified by Ingloba360
 *
 */
@Entity
@Table(name = "program_indicators")
public class ProgramIndicators extends LocalizableEnumeration {

	private static final long serialVersionUID = -6382285364788548393L;

	/**
	 * Stores asse value of entity.
	 */
	@Column(name = "asse")
	private String asse;

	/**
	 * Stores specificObjective value of entity.
	 */
	@Column(name = "specific_objective")
	private String specificObjective;

	@OneToOne
	@JoinColumn(name = "program_id")
	private ProgramIndicators programParent;

	/**
	 * 
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * Stores units value of entity.
	 */
	@Column(name = "units")
	private String units;

	/**
	 * Stores target value of entity.
	 */
	@Column(name = "target")
	private String target;

	/**
	 * Stores baseLine value of entity.
	 */
	@Column(name = "base_line")
	private String baseLine;

	/**
	 * Stores code value of entity.
	 */
	@Column(name = "source")
	private String source;

	@Column(name = "survey")
	private String survey;

	@Column(name = "avances")
	private String avances;

	@Column(name = "avances2")
	private String avances2;

	@Column(name = "avances3")
	private String avances3;

	@Column(name = "avances4")
	private String avances4;

	@Column(name = "axis_os")
	private String axisOs;

	@Column(name = "axis_priority")
	private String axisPriority;

	@Column(name = "axis_id")
	private String axisId;

	@Column(name = "indicator_type")
	private String indicatorType;

	@Column(name = "indicator_code")
	private String indicatorCode;

	@Column(name = "year")
	private String year;

	/**
	 * 
	 */
	public ProgramIndicators() {

	}

	/**
	 * 
	 */
	public ProgramIndicators(ProgramIndicatorsType pit) {
		type = pit.getCode();
	}

	/**
	 * Sets specificObjective
	 * 
	 * @param specificObjective
	 *            the specificObjective to set
	 */
	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = setLocalValue(
				getLocalParams(LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.specificObjective, specificObjective));
	}

	/**
	 * Gets specificObjective
	 * 
	 * @return specificObjective the specificObjective
	 */
	public String getSpecificObjective() {
		return getLocalValue(
				getLocalParams(LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.specificObjective, null));
	}

	public void setAsse(String specificObjective) {
		this.asse = setLocalValue(
				getLocalParams(LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.asse, specificObjective));
	}

	public String getAsse() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.asse, null));
	}

	/**
	 * Sets asse
	 * 
	 * @param asse
	 *            the asse to set 0 - Realization 1 - Result
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse 0 - Realization 1 - Result
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Gets units
	 * 
	 * @return units the units
	 */
	public String getUnits() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.units, null));
	}

	/**
	 * Sets units
	 * 
	 * @param units
	 *            the units to set
	 */
	public void setUnits(String units) {
		this.units = setLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.units, units));
	}

	public void setTarget(String target) {
		this.target = setLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.target, target));
	}

	public String getTarget() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.target, null));
	}

	public void setBaseLine(String baseLine) {
		this.baseLine = setLocalValue(
				getLocalParams(LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.baseLine, baseLine));
	}

	public String getBaseLine() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.baseLine, null));
	}

	public String getSource() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.source, null));
	}

	public void setSource(String source) {
		this.source = setLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.source, source));
	}

	public String getSurvey() {
		return getLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.survey, null));
	}

	public void setSurvey(String survey) {
		this.survey = setLocalValue(getLocalParams(
				LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(), this.survey, survey));
	}

	public String getAvances() {
		return avances;
	}

	public void setAvances(String avances) {
		this.avances = avances;
	}

	public String getAxisOs() {
		return axisOs;
	}

	public void setAxisOs(String axisOs) {
		this.axisOs = axisOs;
	}

	public String getAxisPriority() {
		return axisPriority;
	}

	public void setAxisPriority(String axisPriority) {
		this.axisPriority = axisPriority;
	}

	public String getAxisId() {
		return axisId;
	}

	public void setAxisId(String axisId) {
		this.axisId = axisId;
	}

	public String getIndicatorType() {
		return indicatorType;
	}

	public void setIndicatorType(String indicatorType) {
		this.indicatorType = indicatorType;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public ProgramIndicators getProgramParent() {
		return programParent;
	}

	public void setProgramParent(ProgramIndicators programParent) {
		this.programParent = programParent;
	}

	public String getAvances2() {
		return avances2;
	}

	public void setAvances2(String avances2) {
		this.avances2 = avances2;
	}

	public String getAvances3() {
		return avances3;
	}

	public void setAvances3(String avances3) {
		this.avances3 = avances3;
	}

	public String getAvances4() {
		return avances4;
	}

	public void setAvances4(String avances4) {
		this.avances4 = avances4;
	}

}
