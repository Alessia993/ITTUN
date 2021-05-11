/**
 * 
 */
package com.infostroy.paamns.common.enums;

import com.infostroy.paamns.common.utils.Utils;

/**
 * 
 * @author Vladimir Zrazhevskiy
 *  InfoStroy Co., 2013.
 * 
 */
public enum CertificatedByType
{
	CertificateSTC("CertificatedByTypeCertificateSTC"), CertificateAGU(
			"CertificatedByTypeCertificateAGU"), CertificateACU(
			"CertificatedByTypeCertificateACU");

	/**
	 * @param localName
	 */
	private CertificatedByType(String localName)
	{
		this.localName = localName;
	}

	private String	localName;

	/**
	 * Gets localName
	 * 
	 * @return localName the localName
	 */
	public String getLocalName()
	{
		return localName;
	}

	@Override
	public String toString()
	{
		return Utils.getString(localName);
	}

}
