package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

@Entity
@Table(name = "hierarchical_level")
public class HierarchicalLevel extends LocalizableStaticEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7948063852845445199L;

	@Column(name = "cod_hierarchical_level")
	private String codHierarchicalLevel;

	@Column(name = "data_collected_value")
	private String dataCollectedValue;

	@Column(name="description", columnDefinition="VARCHAR(2000)")
	private String description;

	public String toString() {
		return getDescription();
	}

	public String getCodHierarchicalLevel() {
		return codHierarchicalLevel;
	}

	public void setCodHierarchicalLevel(String codHierarchicalLevel) {
		this.codHierarchicalLevel = codHierarchicalLevel;
	}

	public String getDataCollectedValue() {
		return dataCollectedValue;
	}

	public void setDataCollectedValue(String dataCollectedValue) {
		this.dataCollectedValue = dataCollectedValue;
	}

	public String getDescription() {
		return getLocalValue(
				getLocalParams(this, LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.description, null));
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = setLocalValue(
				getLocalParams(this, LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString(),
						this.description, description));
	}

}
