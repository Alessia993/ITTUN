package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name = "expenditure_declaration_dur_compilations")
public class ExpenditureDeclarationDurCompilations extends PersistentEntity implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6392124014297123171L;

	@Column(name = "expenditure_declaration_id")
	private Long expenditureDeclarationId;

	@Column(name = "durCompilations_id")
	private Long durCompilationsId;

	public ExpenditureDeclarationDurCompilations() {
		super();
	}

	public ExpenditureDeclarationDurCompilations(Long expenditureDeclarationId, Long durCompilationsId) {
		super();
		this.expenditureDeclarationId = expenditureDeclarationId;
		this.durCompilationsId = durCompilationsId;
	}

	public Long getExpenditureDeclarationId() {
		return expenditureDeclarationId;
	}

	public void setExpenditureDeclarationId(Long expenditureDeclarationId) {
		this.expenditureDeclarationId = expenditureDeclarationId;
	}

	public Long getDurCompilationsId() {
		return durCompilationsId;
	}

	public void setDurCompilationsId(Long durCompilationsId) {
		this.durCompilationsId = durCompilationsId;
	}

	public Object clone() {
		ExpenditureDeclarationDurCompilations eddc = null;
		try {
			eddc = (ExpenditureDeclarationDurCompilations) super.clone();
		} catch (CloneNotSupportedException e) {
			eddc = new ExpenditureDeclarationDurCompilations(this.expenditureDeclarationId, this.durCompilationsId);
			e.printStackTrace();
		}
		return eddc;
	}
}
