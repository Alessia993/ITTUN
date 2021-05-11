package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

public class Annex1mWrapper implements Comparable<Annex1mWrapper>{

	private String axis;

	private String specificObjective;

	private String cup;

	private String localCode;

	private String systemCode;

	private String title;

	private Double amountTransferred;

	public Annex1mWrapper() {
		super();
	}

	public Annex1mWrapper(Projects p, SpecificGoals sg, Double amountTransferred) {
		this.axis = String.valueOf(p.getAsse());
		this.specificObjective = sg.getCode();
		this.cup = p.getCupCode();
		this.localCode = String.valueOf(p.getId());
		this.systemCode = p.getCode();
		this.title = p.getTitle();
		this.amountTransferred = amountTransferred;
	}

	@Export(propertyName="asse", place=ExportPlaces.Annex1M, seqXLS=0, type=FieldTypes.STRING)
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@Export(propertyName="expenditureDeclarationEditObjectiveSpecific", place=ExportPlaces.Annex1M, seqXLS=1, type=FieldTypes.STRING)
	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	@Export(propertyName="expenditureDeclarationEditCUP", place=ExportPlaces.Annex1M, seqXLS=2, type=FieldTypes.STRING)
	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	@Export(propertyName="expenditureDeclarationEditLocalCode", place=ExportPlaces.Annex1M, seqXLS=3, type=FieldTypes.STRING)
	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	@Export(propertyName="expenditureDeclarationEditSystemCode", place=ExportPlaces.Annex1M, seqXLS=4, type=FieldTypes.STRING)
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@Export(propertyName="expenditureDeclarationEditOperationTitle", place=ExportPlaces.Annex1M, seqXLS=5, type=FieldTypes.STRING)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Export(propertyName="expenditureDeclarationEditAmountTransferred", place=ExportPlaces.Annex1M, seqXLS=6, type=FieldTypes.MONEY)
	public Double getAmountTransferred() {
		return amountTransferred;
	}

	public void setAmountTransferred(Double amountTransferred) {
		this.amountTransferred = amountTransferred;
	}

	@Override
	public int compareTo(Annex1mWrapper o) {
		return this.specificObjective.compareTo(o.specificObjective);
	}

}
