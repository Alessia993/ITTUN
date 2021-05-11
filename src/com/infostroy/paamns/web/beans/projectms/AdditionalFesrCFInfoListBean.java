package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrCFInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

public class AdditionalFesrCFInfoListBean extends  EntityProjectListBean<AdditionalFesrCFInfo>
{
	private Long selectedPartnerId;
	
	private Boolean canAdd;
	
	private List<SelectItem> partnerList;
	
	private List<SelectItem> transferTypes;

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{	List<UserRoles> roles = this.getCurrentUser().getRoles();
		Long roleId = null;
		for (UserRoles role : roles)
		{
			if (role.getRole().getCode().equals(UserRoleTypes.BP.getValue()))
			{
				if(roleId!=null){
					roleId=null;
					break;
				}
				roleId = role.getRole().getId();
			}
			else if (role.getRole().getCode()
					.equals(UserRoleTypes.AGU.getValue()))
			{
				if(roleId!=null){
					roleId=null;
					break;
				}
				roleId = role.getRole().getId();
			}
		}
		List<AdditionalFesrCFInfo> list = BeansFactory.AdditionalFESRCFInfo().LoadByProjectCFPartnerAnagraphsAndTypeAndRole(this.getSelectedPartnerId(), this.getSelectedTransferType(), this.getProjectId(), null,null);//roleId);
		this.setList(list);
		this.setCanAdd(Boolean.TRUE);
		fillPartners();
		fillTransferTypes();
	}

	
	public void fillPartners(){
		try
		{
			
			List<CFPartnerAnagraphs> partnerAnagraphs = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
			if(this.getPartnerList()!=null){
				this.getPartnerList().clear();
			}else{
				this.setPartnerList(new ArrayList<SelectItem>());
			}
			this.getPartnerList().add(SelectItemHelper.getFirst());
			for(CFPartnerAnagraphs partnerAnagraph : partnerAnagraphs){
				this.getPartnerList().add(new SelectItem(partnerAnagraph.getId().toString(), partnerAnagraph.getNaming()));
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			log.error(e);
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		
	}
	
	public void fillTransferTypes(){
		if(this.getTransferTypes()==null){
			this.setTransferTypes(new ArrayList<SelectItem>());
		}else{
			this.getTransferTypes().clear();
		}
		this.getTransferTypes().add(SelectItemHelper.getFirst());
		for(TransferTypes type : TransferTypes.values()){
			this.getTransferTypes().add(new SelectItem(type.getCode().toString(), type.toString()));
		}

	}
	
	
	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#addEntity()
	 */
	@Override
	public void addEntity()
	{
		this.goTo(PagesTypes.ADDITIONALFESRCFINFOEDIT);
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#editEntity()
	 */
	@Override
	public void editEntity()
	{
		this.getSession().put("additionalFesrInfoCFEdit", this.getEntityEditId());
		this.goTo(PagesTypes.ADDITIONALFESRCFINFOEDIT);
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityProjectListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Gets canAdd
	 * @return canAdd the canAdd
	 */
	public Boolean getCanAdd()
	{
		return canAdd;
	}

	/**
	 * Sets canAdd
	 * @param canAdd the canAdd to set
	 */
	public void setCanAdd(Boolean canAdd)
	{
		this.canAdd = canAdd;
	}

	/**
	 * Gets partnerList
	 * @return partnerList the partnerList
	 */
	public List<SelectItem> getPartnerList()
	{
		return partnerList;
	}

	/**
	 * Sets partnerList
	 * @param partnerList the partnerList to set
	 */
	public void setPartnerList(List<SelectItem> partnerList)
	{
		this.partnerList = partnerList;
	}


	public String getSelectedPartnerId()
	{
		if (getViewState().containsKey("selectedPartnerId"))
		{
			return (String) getViewState().get("selectedPartnerId");
		}
		return null;
	}

	/**
	 * Sets selectedPartnerId
	 * 
	 * @param selectedPartnerId
	 *            the selectedPartnerId to set
	 */
	public void setSelectedPartnerId(String selectedPartnerId)
	{
		getViewState().put("selectedPartnerId", selectedPartnerId);
	}

	
	public void handleChangePartnerFilter()
	{
		try
		{
			this.Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}
	
	public void handleChangeTransferTypeFilter()
	{
		try
		{
			this.Page_Load();
		}
		catch (Exception e)
		{
			log.error(e);
		}
	}


	/**
	 * Gets transferTypes
	 * @return transferTypes the transferTypes
	 */
	public List<SelectItem> getTransferTypes()
	{
		return transferTypes;
	}


	/**
	 * Sets transferTypes
	 * @param transferTypes the transferTypes to set
	 */
	public void setTransferTypes(List<SelectItem> transferTypes)
	{
		this.transferTypes = transferTypes;
	}
	
	public String getSelectedTransferType()
	{
		if (getViewState().containsKey("transferType"))
		{
			return (String) getViewState().get("transferType");
		}
		return null;
	}

	/**
	 * Sets selectedPartnerId
	 * 
	 * @param selectedPartnerId
	 *            the selectedPartnerId to set
	 */
	public void setSelectedTransferType(String transferType)
	{
		getViewState().put("transferType", transferType);
	}
	
	

	
}