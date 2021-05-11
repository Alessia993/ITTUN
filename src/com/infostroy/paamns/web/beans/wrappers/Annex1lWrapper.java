package com.infostroy.paamns.web.beans.wrappers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentWays;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

public class Annex1lWrapper implements Comparable<Annex1lWrapper>{

	private String axis;

	private String specificObjective;

	private String cup;

	private String localCode;

	private String systemCode;

	private String title;

	private String documentType;

	private String paymentNumber;

	private Date paymentDate;

	private Double amountToCoverAdvanceStateAid;

	private Double lastAmountToCoverAdvanceStateAid;

	private Double amountNotCoverAdvanceStateAid;

	public Annex1lWrapper(){}
	
	public Annex1lWrapper(CostDefinitions cd, Projects p, SpecificGoals sg, AdditionalFesrInfo afi, PaymentWays pw, Double amountNotCoverAdvanceStateAid)
			throws HibernateException, PersistenceBeanException {
		this.axis = String.valueOf(p.getAsse());
		this.specificObjective = sg.getCode();
		this.cup = p.getCupCode();
		this.localCode = String.valueOf(p.getId());
		this.systemCode = p.getCode();
		this.title = p.getTitle();
		this.documentType = pw.getValue();
		this.paymentNumber = cd.getPaymentNumber();
		this.paymentDate = cd.getPaymentDate();
		amountToCoverAdvanceStateAidComputation(afi);
		lastAmountToCoverAdvanceStateAidComputation(cd, afi);
		this.amountNotCoverAdvanceStateAid = amountNotCoverAdvanceStateAid;
	}

	private void lastAmountToCoverAdvanceStateAidComputation(CostDefinitions cd, AdditionalFesrInfo afi) {
		Calendar diff = new GregorianCalendar();
		diff.setTimeInMillis(afi.getCreateDate().getTime() - cd.getCreateDate().getTime());
		if (Math.abs((diff.get(Calendar.YEAR) - 1970)) < 3) {
			this.lastAmountToCoverAdvanceStateAid = (cd.getAguCheckStateAidAmount() != null
					? cd.getAguCheckStateAidAmount() : 0d);
		} else {
			this.lastAmountToCoverAdvanceStateAid = 0d;
		}
	}

	private void amountToCoverAdvanceStateAidComputation(AdditionalFesrInfo afi) {
		if (afi.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidDeMinimis.getCode())) {
				//|| afi.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidExemptionScheme.getCode())) {
			this.amountToCoverAdvanceStateAid = (afi.getTransferImport() != null ? afi.getTransferImport() : 0d);
		} else {
			this.amountToCoverAdvanceStateAid = 0d;
		}
	}

	@Export(propertyName="asse", place=ExportPlaces.Annex1L, seqXLS=0, type=FieldTypes.STRING)
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@Export(propertyName="expenditureDeclarationEditObjectiveSpecific", place=ExportPlaces.Annex1L, seqXLS=1, type=FieldTypes.STRING)
	public String getSpecificObjective() {
		return specificObjective;
	}

	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	@Export(propertyName="expenditureDeclarationEditCUP", place=ExportPlaces.Annex1L, seqXLS=2, type=FieldTypes.STRING)
	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	@Export(propertyName="expenditureDeclarationEditLocalCode", place=ExportPlaces.Annex1L, seqXLS=3, type=FieldTypes.STRING)
	public String getLocalCode() {
		return localCode;
	}

	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	@Export(propertyName="expenditureDeclarationEditSystemCode", place=ExportPlaces.Annex1L, seqXLS=4, type=FieldTypes.STRING)
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	@Export(propertyName="expenditureDeclarationEditOperationTitle", place=ExportPlaces.Annex1L, seqXLS=5, type=FieldTypes.STRING)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Export(propertyName="expenditureDeclarationEditDocumentType", place=ExportPlaces.Annex1L, seqXLS=6, type=FieldTypes.STRING)
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	@Export(propertyName="expenditureDeclarationEditPaymentNumber", place=ExportPlaces.Annex1L, seqXLS=7, type=FieldTypes.STRING)
	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	@Export(propertyName="expenditureDeclarationEditPaymentDate", place=ExportPlaces.Annex1L, seqXLS=8, type=FieldTypes.DATE)
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Export(propertyName="expenditureDeclarationEditPaymentAmountStateAid", place=ExportPlaces.Annex1L, seqXLS=9, type=FieldTypes.MONEY)
	public Double getAmountToCoverAdvanceStateAid() {
		return amountToCoverAdvanceStateAid;
	}

	public void setAmountToCoverAdvanceStateAid(Double amountToCoverAdvanceStateAid) {
		this.amountToCoverAdvanceStateAid = amountToCoverAdvanceStateAid;
	}

	@Export(propertyName="expenditureDeclarationEditLastAmountToCoverAdvanceStateAid", place=ExportPlaces.Annex1L, seqXLS=10, type=FieldTypes.MONEY)
	public Double getLastAmountToCoverAdvanceStateAid() {
		return lastAmountToCoverAdvanceStateAid;
	}

	public void setLastAmountToCoverAdvanceStateAid(Double lastAmountToCoverAdvanceStateAid) {
		this.lastAmountToCoverAdvanceStateAid = lastAmountToCoverAdvanceStateAid;
	}

	@Export(propertyName="expenditureDeclarationEditAmountNotCoverAdvanceStateAid", place=ExportPlaces.Annex1L, seqXLS=11, type=FieldTypes.MONEY)
	public Double getAmountNotCoverAdvanceStateAid() {
		return amountNotCoverAdvanceStateAid;
	}

	public void setAmountNotCoverAdvanceStateAid(Double amountNotCoverAdvanceStateAid) {
		this.amountNotCoverAdvanceStateAid = amountNotCoverAdvanceStateAid;
	}

	@Override
	public int compareTo(Annex1lWrapper o) {
		return this.specificObjective.compareTo(o.specificObjective);
	}

}
