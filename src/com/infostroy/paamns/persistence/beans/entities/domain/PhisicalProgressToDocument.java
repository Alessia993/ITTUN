package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.Entity;

@javax.persistence.Entity
@Table(name = "phisical_progress_documents")
public class PhisicalProgressToDocument extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9125872882548475767L;

	public PhisicalProgressToDocument(Documents document, ProjectIndicators projectIndicators) {
		super();
		this.document = document;
		this.projectIndicators = projectIndicators;
	}

	public PhisicalProgressToDocument() {
	}

	@ManyToOne
	@JoinColumn
	private Documents document;

	@ManyToOne
	@JoinColumn
	private ProjectIndicators projectIndicators;


	public void setDocument(Documents document) {
		this.document = document;
	}

	public Documents getDocument() {
		return document;
	}

	public ProjectIndicators getProjectIndicators() {
		return projectIndicators;
	}

	public void setProjectIndicators(ProjectIndicators projectIndicators) {
		this.projectIndicators = projectIndicators;
	}

}
