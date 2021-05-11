package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

public class ExpenditureDeclarationAnnex1cWrapper {

	private String axis;

	private String specificObjective;

	private String cup;

	private String localCode;

	private String systemCode;

	private String title;

	private Double lastExpenditureDeclaration;

	private Double expenditureDeclaration;

	private Double advancement;

	public ExpenditureDeclarationAnnex1cWrapper() {
		super();
	}

	public ExpenditureDeclarationAnnex1cWrapper(SpecificGoals sg) {
		this.axis = elaborateAxisFromSpecifiGoals(sg);
		this.specificObjective = sg.getCode();
	}

	private String elaborateAxisFromSpecifiGoals(SpecificGoals sg) {
		return sg.getCode().substring(0, sg.getCode().indexOf('.'));
	}

	@Export(propertyName = "asse", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@Export(propertyName = "expenditureDeclarationEditObjectiveSpecific", seqXLS = 2, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	@Export(propertyName = "expenditureDeclarationEditCUP", seqXLS = 3, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	@Export(propertyName = "expenditureDeclarationEditLocalCode", seqXLS = 4, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	@Export(propertyName = "expenditureDeclarationEditSystemCode", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@Export(propertyName = "expenditureDeclarationEditOperationTitle", seqXLS = 6, type = FieldTypes.STRING, place = ExportPlaces.Annex1C)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Export(propertyName = "expenditureDeclarationEditPreviousStatementOfExpenditure", seqXLS = 7, type = FieldTypes.MONEY, place = ExportPlaces.Annex1C)
	public Double getLastExpenditureDeclaration() {
		return lastExpenditureDeclaration;
	}

	public void setLastExpenditureDeclaration(Double lastExpenditureDeclaration) {
		this.lastExpenditureDeclaration = lastExpenditureDeclaration;
	}

	@Export(propertyName = "expenditureDeclarationEditStatementOfExpenditure", seqXLS = 8, type = FieldTypes.MONEY, place = ExportPlaces.Annex1C)
	public Double getExpenditureDeclaration() {
		return expenditureDeclaration;
	}

	public void setExpenditureDeclaration(Double expenditureDeclaration) {
		this.expenditureDeclaration = expenditureDeclaration;
	}

	@Export(propertyName = "expenditureDeclarationEditAdvancement", seqXLS = 9, type = FieldTypes.MONEY, place = ExportPlaces.Annex1C)
	public Double getAdvancement() {
		return advancement;
	}

	public void setAdvancement(Double advancement) {
		this.advancement = advancement;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpenditureDeclarationAnnex1cWrapper other = (ExpenditureDeclarationAnnex1cWrapper) obj;
		if (axis == null) {
			if (other.axis != null)
				return false;
		} else if (!axis.equals(other.axis))
			return false;
		if (cup == null) {
			if (other.cup != null)
				return false;
		} else if (!cup.equals(other.cup))
			return false;
		if (localCode == null) {
			if (other.localCode != null)
				return false;
		} else if (!localCode.equals(other.localCode))
			return false;
		if (specificObjective == null) {
			if (other.specificObjective != null)
				return false;
		} else if (!specificObjective.equals(other.specificObjective))
			return false;
		if (systemCode == null) {
			if (other.systemCode != null)
				return false;
		} else if (!systemCode.equals(other.systemCode))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	

}
