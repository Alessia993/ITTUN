/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class CFAnagraphWrapper extends CFPartnerAnagraphs
{
	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long		serialVersionUID	= 233234847988645923L;

	private ControllerUserAnagraph	cil;

	private ControllerUserAnagraph	daec;

	private boolean					editable;


	private boolean					notAssigned;

	private boolean					fictive;

	public CFAnagraphWrapper(CFPartnerAnagraphProject partnerProject)
	{
		CFPartnerAnagraphs partner = partnerProject.getCfPartnerAnagraphs();
		this.setCountry(partner.getCountry());
		this.setCreateDate(partner.getCreateDate());
		this.setDeleted(partner.getDeleted());
		this.setId(partner.getId());
		this.setName(partner.getName());
		this.setPartita(partner.getPartita());

		if (partner.getUser() != null)
		{
			this.setUser(partner.getUser());
		}
		this.setCil(partnerProject.getCil());
		this.setDaec(partnerProject.getDaec());
		this.setEditable(true);
		this.setRemovedFromProject(partnerProject.getRemovedFromProject());

		this.setNotAssigned(partnerProject.isNotAssigned());
		this.fictive = partnerProject
				.getCfPartnerAnagraphs().getUser() != null
				&& Boolean.TRUE.equals(partnerProject.getCfPartnerAnagraphs().getUser()
						.getFictive());
	}

	public CFAnagraphWrapper()
	{

	}

	public static CFAnagraphWrapper CreateAguWrapper()
			throws PersistenceBeanException
	{
		CFAnagraphWrapper aguController = new CFAnagraphWrapper();
		aguController.setCil(null);
		aguController.setCountry(BeansFactory.Countries().GetCountry(
				CountryTypes.Italy));
		aguController.setCreateDate(DateTime.getNow());
		aguController.setDaec(null);
		aguController.setDeleted(false);
		aguController.setId(-1l);
		aguController.setPartita("01386030488");
		aguController.setName(Utils.getString("Resources", "cfAguName", null));
		aguController.setRemovedFromProject(false);
		return aguController;
	}

	/**
	 * Sets cil
	 * 
	 * @param cil
	 *            the cil to set
	 */
	public void setCil(ControllerUserAnagraph cil)
	{
		this.cil = cil;
	}

	/**
	 * Gets cil
	 * 
	 * @return cil the cil
	 */
	public ControllerUserAnagraph getCil()
	{
		return cil;
	}

	/**
	 * Sets daec
	 * 
	 * @param daec
	 *            the daec to set
	 */
	public void setDaec(ControllerUserAnagraph daec)
	{
		this.daec = daec;
	}

	/**
	 * Gets daec
	 * 
	 * @return daec the daec
	 */
	public ControllerUserAnagraph getDaec()
	{
		return daec;
	}

	/**
	 * Sets editable
	 * 
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}

	/**
	 * Gets editable
	 * 
	 * @return editable the editable
	 */
	public boolean isEditable()
	{
		return editable;
	}

	/**
	 * Gets notAssigned
	 * 
	 * @return notAssigned the notAssigned
	 */
	public boolean isNotAssigned()
	{
		return notAssigned;
	}

	/**
	 * Sets notAssigned
	 * 
	 * @param notAssigned
	 *            the notAssigned to set
	 */
	public void setNotAssigned(boolean notAssigned)
	{
		this.notAssigned = notAssigned;
	}

	/**
	 * Gets fictive
	 * 
	 * @return fictive the fictive
	 */
	public boolean isFictive()
	{
		return fictive;
	}

	/**
	 * Sets fictive
	 * 
	 * @param fictive
	 *            the fictive to set
	 */
	public void setFictive(boolean fictive)
	{
		this.fictive = fictive;
	}

}
