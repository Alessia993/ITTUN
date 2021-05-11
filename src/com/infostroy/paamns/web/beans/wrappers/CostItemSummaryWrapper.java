/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.CostItemTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;

public class CostItemSummaryWrapper
{
	private boolean			showOnlyFirstColumn;

	private String			name;

	private Double			amountFromBudget;

	private Double			amountByCf;

	private Double			amountCostCertificate;

	private Double			amountRequest;

	private Double			absorption;

	private Double			stcCertificated;

	private Double			aguCertificated;

	private Double			acuCertificated;

	private Double			rectified;

	private Double			reinstated;

	private Double			suspended;
	
	private	Double			durSpendingPt;
	
	private	Double			durSpendingPt2;
	
	private Double			requestSpendingPt;
	
	private Double 			requestSpending;
	
	private Double 			previousExpenditureRequisted;
	
	private Long			phaseId;
	
	private Long 			userId;

	private CostItemTypes[]	types;

	public CostItemSummaryWrapper(CostItemTypes[] types)
	{
		this.setTypes(types);
		amountFromBudget = 0d;
		amountByCf = 0d;
		amountCostCertificate = 0d;
		amountRequest = 0d;
		absorption = 0d;
		stcCertificated = 0d;
		aguCertificated = 0d;
		acuCertificated = 0d;
		rectified = 0d;
		reinstated = 0d;
		suspended = 0d;
		requestSpending = 0d;
		durSpendingPt2 = 0d;
		previousExpenditureRequisted = 0d;
	}

	public CostItemSummaryWrapper()
	{
		super();
		amountFromBudget = 0d;
		amountByCf = 0d;
		amountCostCertificate = 0d;
		amountRequest = 0d;
		absorption = 0d;
		stcCertificated = 0d;
		aguCertificated = 0d;
		acuCertificated = 0d;
		rectified = 0d;
		reinstated = 0d;
		suspended = 0d;
		requestSpending = 0d;
		durSpendingPt2 = 0d;
		previousExpenditureRequisted = 0d;
	}

	public void setShowOnlyFirstColumn(boolean showOnlyFirstColumn)
	{
		this.showOnlyFirstColumn = showOnlyFirstColumn;
	}

	public boolean isShowOnlyFirstColumn()
	{
		return showOnlyFirstColumn;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Export(propertyName = "exportCostItem", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.GridDurSummary)
	public String getName()
	{
		return name;
	}

	public void setAmountByCf(Double amountByCf)
	{
		this.amountByCf = amountByCf;
	}

	@Export(propertyName = "exportAmountByCf", seqXLS = 2, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAmountByCf()
	{
		return amountByCf;
	}

	public void setAmountFromBudget(Double amountFromBudget)
	{
		this.amountFromBudget = amountFromBudget;
	}

	@Export(propertyName = "exportAmountFromBudget", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAmountFromBudget()
	{
		return amountFromBudget;
	}

	public void setAmountCostCertificate(Double amountCostCertificate)
	{
		this.amountCostCertificate = amountCostCertificate;
	}

	@Export(propertyName = "exportCostCertificate", seqXLS = 3, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAmountCostCertificate()
	{
		return amountCostCertificate;
	}

	public void setAmountRequest(Double amountRequest)
	{
		this.amountRequest = amountRequest;
	}

	@Export(propertyName = "exportAmountRequest", seqXLS = 4, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAmountRequest()
	{
		return amountRequest;
	}

	public void setAbsorption(Double absorption)
	{
		this.absorption = absorption;
	}

	//@Export(propertyName = "exportAbsorption", seqXLS = 8, type = FieldTypes.PERCENTAGE, place = ExportPlaces.GridDurSummary)
	public Double getAbsorption()
	{
		return absorption;
	}

	@Export(propertyName = "exportStc", seqXLS = 5, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getStcCertificated()
	{
		return stcCertificated;
	}

	public void setStcCertificated(Double stcCertificated)
	{
		this.stcCertificated = stcCertificated;
	}

	@Export(propertyName = "exportAgu", seqXLS = 6, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAguCertificated()
	{
		return aguCertificated;
	}

	public void setAguCertificated(Double aguCertificated)
	{
		this.aguCertificated = aguCertificated;
	}

	@Export(propertyName = "exportAcu", seqXLS = 7, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getAcuCertificated()
	{
		return acuCertificated;
	}

	public void setAcuCertificated(Double acuCertificated)
	{
		this.acuCertificated = acuCertificated;
	}

	public Double getRectified()
	{
		return rectified;
	}

	public void setRectified(Double rectified)
	{
		this.rectified = rectified;
	}

	public Double getSuspended()
	{
		return suspended;
	}

	public void setSuspended(Double suspended)
	{
		this.suspended = suspended;
	}

	public CostItemTypes[] getTypes()
	{
		return types;
	}

	public void setTypes(CostItemTypes[] types)
	{
		this.types = types;
	}

	public Double getReinstated()
	{
		return reinstated;
	}

	public void setReinstated(Double reinstated)
	{
		this.reinstated = reinstated;
	}
	
	
	public Double getDurSpendingPt()
	{
		Double value=Double.valueOf(0d);
		if(amountRequest!=null && amountFromBudget!=null && amountFromBudget.doubleValue()!=0d){
			value = Double.valueOf(amountRequest.doubleValue()/amountFromBudget.doubleValue()*100d);
		}
		return value;
	}

	public void setDurSpendingPt(Double durSpendingPt)
	{
		this.durSpendingPt = durSpendingPt;
	}
	
	@Export(propertyName = "requestSpendingPt", seqXLS = 10, type = FieldTypes.PERCENTAGE, place = ExportPlaces.GridDurSummary)
	public Double getRequestSpendingPt()
	{
		Double value=Double.valueOf(0d);
		if(aguCertificated!=null && amountFromBudget!=null && amountFromBudget.doubleValue()!=0d){
			value = Double.valueOf(aguCertificated.doubleValue()/amountFromBudget.doubleValue()*100d);
		}
		return value;
	}

	public void setRequestSpendingPt(Double requestSpendingPt)
	{
		this.requestSpendingPt = requestSpendingPt;
	}

	public Long getPhaseId()
	{
		return phaseId;
	}

	public void setPhaseId(Long phaseId)
	{
		this.phaseId = phaseId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	@Export(propertyName = "requestSpending", seqXLS = 8, type = FieldTypes.PERCENTAGE, place = ExportPlaces.GridDurSummary)
	public Double getRequestSpending() {
		Double requestSpending=Double.valueOf(0d);
		if(amountRequest!=null && amountFromBudget!=null && amountFromBudget.doubleValue()!=0d){
			requestSpending = Double.valueOf((amountRequest.doubleValue()/amountFromBudget.doubleValue())*100d);
		}
		return requestSpending;
	}

	public void setRequestSpending(Double requestSpending) {
		this.requestSpending = requestSpending;
	}

	//@Export(propertyName = "durSpendingPt", seqXLS = 9, type = FieldTypes.PERCENTAGE, place = ExportPlaces.GridDurSummary)
	public Double getDurSpendingPt2() {
		Double durSpendingPt2=Double.valueOf(0d);
		if(amountRequest!=null && amountFromBudget!=null && amountFromBudget.doubleValue()!=0d){
			requestSpending = Double.valueOf((amountRequest.doubleValue()/amountFromBudget.doubleValue())*100d);
		}
		return durSpendingPt2;
	}

	
	public void setDurSpendingPt2(Double durSpendingPt2) {
		this.durSpendingPt2 = durSpendingPt2;
	}

	@Export(propertyName = "durPreviousExpenditureRequisted", seqXLS = 11, type = FieldTypes.MONEY, place = ExportPlaces.GridDurSummary)
	public Double getPreviousExpenditureRequisted() {
		return previousExpenditureRequisted;
	}

	public void setPreviousExpenditureRequisted(Double previousExpenditureRequisted) {
		this.previousExpenditureRequisted = previousExpenditureRequisted;
	}
	
	
}
