/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2011.
 * 
 */
public enum UploadDocumentRoleType {
	AGU("documentRoleAgu", 1), BP("documentRoleBp", 2), APU("documentRoleApu",
			3), AAU("documentRoleAau", 4), ACU("documentRoleAcu", 5), STC(
			"documentRoleStc", 6), B("documentRoleB", 7), REV(
			"documentRoleCil", 8), DAEC("documentRoleDaec", 9), ACUM(
			"documentRoleAcum", 10);

	private String displayName;

	private int id;

	/**
     * 
     */
	private UploadDocumentRoleType(String name, int id) {
		setDisplayName(name);
		this.setId(id);
	}

	/**
	 * Sets displayName
	 * 
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets displayName
	 * 
	 * @return displayName the displayName
	 */
	public String getDisplayName() {
		return Utils.getString(displayName);
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets id
	 * 
	 * @return id the id
	 */
	public int getId() {
		return id;
	}

	public static UploadDocumentRoleType getById(int id) {
		for (UploadDocumentRoleType type : UploadDocumentRoleType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		
		return null;
	}

	public static UploadDocumentRoleType getByName(String name) {
		for (UploadDocumentRoleType type : UploadDocumentRoleType.values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}

		return null;
	}
}
