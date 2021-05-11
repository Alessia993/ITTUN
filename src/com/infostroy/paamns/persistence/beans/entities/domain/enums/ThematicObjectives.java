package com.infostroy.paamns.persistence.beans.entities.domain.enums;

public enum ThematicObjectives {

	Thematic1(1l, "thematic1"), 
	Thematic2(2l, "thematic2"), 
	Thematic3(3l, "thematic3"), 
	Thematic4(4l, "thematic4"), 
	Thematic5(5l, "thematic5");

	private ThematicObjectives(Long id, String value) {
		setId(id);
		setValue(value);
	}

	private Long id;

	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
