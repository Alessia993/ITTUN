package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.FesrCnType;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.enums.ResponsibilityTypeTypes;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.helpers.mail.CFandPartnerMailSender;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Addresses;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.Responsibilities;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Ateco;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Cities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Departments;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Naming;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Provinces;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibilityTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

public class CFPartnerAnagraphCompletationsEditBean extends
		EntityProjectEditBean<CFPartnerAnagraphCompletations>
{
	/**
	 * Form header
	 */


	private final static String	nonApplicabile	= "CFPartnerAnagraphCompletationsEditNamingNonApplicabile";

	private String				partnerNameTitle;

	/**
	 * Tab header and roles and partners information
	 */

	private String				partnerCountry;

	private String				partnerPartita;

	private String				partnerName;

	private String				partnerFirstLevelController;

	private String				partnerDaec;

	private String				partnerReferentName;

	private String				partnerReferentSurname;

	private String				partnerCfReferent;

	private String				partnerEmailReferent;

	@SuppressWarnings("unused")
	private String				tabCFPartnerAnagraphViewHeader;

	private String				firstAddressEmail;

	private String				firstAddressPhoneNumber;

	private String				firstAddressFaxNumber;

	private String				secondAddressEmail;

	private String				secondAddressPhoneNumber;

	private String				secondAddressFaxNumber;

	private Boolean				bNUT2;

	private Boolean				bNUT3;

	private String				notes;
	
	private Boolean 			recoverableVAT;

	/**
	 * Lists for comboboxes
	 */

	private List<SelectItem>	lstNaming1;

	private List<SelectItem>	lstNaming2;

	private List<SelectItem>	lstNaming3;

	private List<SelectItem>	lstATECOcode1;

	private List<SelectItem>	lstATECOcode2;

	private List<SelectItem>	lstATECOcode3;

	private List<SelectItem>	lstATECOcode4;

	private List<SelectItem>	lstFirstAddressRegion;

	private List<SelectItem>	lstSecondAddressRegion;

	private List<SelectItem>	lstFirstAddressProvince;

	private List<SelectItem>	lstSecondAddressProvince;

	private List<SelectItem>	lstFirstAddressCity;

	private List<SelectItem>	lstSecondAddressCity;

	/**
	 * Law responsible section
	 */

	private String				lsFiscalCode;

	private String				lsName;

	private String				lsSurname;

	private String				lsRole;

	private String				lsAddress;

	private String				lsEmail;

	private String				lsPhoneNumber;

	private String				lsFaxNumber;

	/**
	 * Responsible people section
	 */

	private String				rpFiscalCode;

	private String				rpName;

	private String				rpSurname;

	private String				rpRole;

	private String				rpAddress;

	private String				rpEmail;

	private String				rpPhoneNumber;

	private String				rpFaxNumber;

	/**
	 * Financial Responsible section
	 */
	private String				frFiscalCode;

	private String				frName;

	private String				frSurname;

	private String				frRole;

	private String				frAddress;

	private String				frEmail;

	private String				frPhoneNumber;

	private String				frFaxNumber;

	private boolean				view;

	List<InfoObject>			listCFPMS;
	
	private List<SelectItem>	listFesrCnValue;
	
	private String fesrCnValue;
	
	private boolean validationFailed;
	
	public Boolean BeforeSave(){
		/*checkEmpty("EditForm:tabCFPartnerAnagraphCompletationsEdit1:tabCFPartnerAnagraphCompletationsCommon:fesrCnValue", this.getFesrCnValue());
		if (this.validationFailed) {
			return false;
		} */
		return true;
	}
	
	private boolean checkEmpty(String id, Object value) {
		return checkEmpty(id, value, false);
	}

	private boolean checkEmpty(String id, Object value, boolean skip) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (!skip && (str == null || str.isEmpty() || str.trim().isEmpty())) {
			this.validationFailed = true;
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}
	
	private void fillListFesrCnValue(){
		this.listFesrCnValue = new ArrayList<SelectItem>();
		this.listFesrCnValue.add(new SelectItem(String.valueOf(FesrCnType.FESR85CN15.getValue()), Utils.getString("Resources",
				"CFPartnerAnagraphCompletationsEditFesr85Cn15", null)));
		this.listFesrCnValue.add(new SelectItem(String.valueOf(FesrCnType.FESR50CN50.getValue()), Utils.getString("Resources",
				"CFPartnerAnagraphCompletationsEditFesr50Cn50", null)));
	}

	@Override
	public void AfterSave()
	{
		if (listCFPMS != null)
		{
			// new CFandPartnerMailSender(listCFPMS).start();
			Transaction tr = null;
			try
			{
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();
				List<Mails> mails = new ArrayList<Mails>();
				CFandPartnerMailSender cFandPartnerMailSender = new CFandPartnerMailSender(
						listCFPMS);
				mails.addAll(cFandPartnerMailSender
						.completeMails(PersistenceSessionManager.getBean()
								.getSession()));

				if (mails != null && !mails.isEmpty())
				{
					for (Mails mail : mails)
					{
						BeansFactory.Mails().SaveInTransaction(mail);
					}

				}

			}
			catch (Exception e)
			{
				if (tr != null)
				{
					tr.rollback();
				}
				log.error(e);
			}
			finally
			{
				if (tr != null && !tr.wasRolledBack() && tr.isActive())
				{
					tr.commit();
				}
			}
		}

		this.GoBack();
	}

	@Override
	public void GoBack()
	{
		if (this.getSession().get("from_listcfpartner") != null)
		{
			this.getSession().put("from_listcfpartner", null);
			this.goTo(PagesTypes.CFPARTNERANAGRAPHLIST);
		}
		else
		{
			this.goTo(PagesTypes.PROJECTINDEX);
		}
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException,
			PersistenceBeanException
	{
		if (this.getSession().get("cfPartneranagraphcompletationView") != null)
		{
			this.view = (Boolean) this.getSession().get(
					"cfPartneranagraphcompletationView");
		}

		this.fillListFesrCnValue();
		this.loadCFPartnerAnagraphs();
		this.FillComboboxes();
		
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException,
			PersistenceBeanException
	{
		this.clearSession();
		this.loadEntity();
		this.setFailedValidation(new ArrayList<String>());

		// this.FillComboboxes();
	}

	private void loadEntity() throws PersistenceBeanException
	{
		if (this.getSession().get("cfpartner") != null)
		{
			// Load
			CFPartnerAnagraphCompletations cfpartner = null;
			CFPartnerAnagraphProject cfproject = BeansFactory
					.CFPartnerAnagraphProject().LoadByPartner(
							Long.parseLong(this.getProjectId()),
							Long.parseLong(String.valueOf(this.getSession()
									.get("cfpartner"))));
			List<CFPartnerAnagraphCompletations> lst = BeansFactory
					.CFPartnerAnagraphCompletations().GetEntitiesForCFAnagraph(
							cfproject.getId(), this.getProjectId());

			if (lst != null && !lst.isEmpty())
			{
				cfpartner = lst.get(0);
			}
			
			if (cfpartner != null)
			{	
					this.setNaming1(cfpartner.getNaming1());
					this.setNaming2(cfpartner.getNaming2());
					this.setNaming3(cfpartner.getNaming());
					this.setRecoverableVAT(cfpartner.getRecoverableVAT());
					this.setFesrCnValue(cfpartner.getFesrCnValue());
					if (cfpartner.getAtecoCode4() != null)
				{
					if(!cfpartner.getAtecoCode4().equals(Utils.getString(nonApplicabile))){
						Ateco a4 = BeansFactory.Ateco().LoadByCode(
								cfpartner.getAtecoCode4());
						if (a4 != null)
						{
							this.setATECOcode4(a4.getId().toString());
						}
					}
					else
						this.setATECOcode4(Utils.getString(nonApplicabile));

				}

				if (cfpartner.getAtecoCode3() != null)
				{
					if(!cfpartner.getAtecoCode3().equals(Utils.getString(nonApplicabile))){
						Ateco a3 = BeansFactory.Ateco().LoadByCode(
								cfpartner.getAtecoCode3());
						if (a3 != null)
						{
							this.setATECOcode3(a3.getId().toString());
						}
					}
					else
						this.setATECOcode3(Utils.getString(nonApplicabile));

				}

				if (cfpartner.getAtecoCode2() != null)
				{
					if(!cfpartner.getAtecoCode2().equals(Utils.getString(nonApplicabile))){
						Ateco a2 = BeansFactory.Ateco().LoadByCode(
								cfpartner.getAtecoCode2());
						if (a2 != null)
						{
							this.setATECOcode2(a2.getId().toString());
						}
					}
					else
						this.setATECOcode2(Utils.getString(nonApplicabile));

				}

				if (cfpartner.getAtecoCode1() != null)
					if (cfpartner.getAtecoCode1() != null)
					{
						if(!cfpartner.getAtecoCode1().equals(Utils.getString(nonApplicabile))){
							Ateco a1 = BeansFactory.Ateco().LoadByCode(
									cfpartner.getAtecoCode1());
							if (a1 != null)
							{
								this.setATECOcode1(a1.getId().toString());
							}
						}
						else
							this.setATECOcode1(Utils.getString(nonApplicabile));

					}

				this.setbNUT2(cfpartner.getNut2());
				this.setbNUT3(cfpartner.getNut3());
				this.setRecoverableVAT(cfpartner.getRecoverableVAT());
				this.setFesrCnValue(cfpartner.getFesrCnValue());
				this.setNotes(cfpartner.getNote());

				if (cfpartner.getFirstAddress() != null)
				{
					this.setFirstAddressEmail(cfpartner.getFirstAddress()
							.getEmail());
					this.setFirstAddressFaxNumber(cfpartner.getFirstAddress()
							.getFax());
					this.setFirstAddressPhoneNumber(cfpartner.getFirstAddress()
							.getPhone());
					this.setFirstAddressAddress(cfpartner.getFirstAddress()
							.getAddress());

					if (cfpartner.getFirstAddress().getRegion() != null)
					{
						this.setFirstAddressRegion(cfpartner.getFirstAddress()
								.getRegion().getId().toString());

						if (cfpartner.getFirstAddress().getRegion()
								.getIsForFrLoc())
						{
							if (cfpartner.getFirstAddress().getZone() != null
									&& this.getFirstAddressCity() == null)
							{
								this.setFirstAddressCity(cfpartner
										.getFirstAddress().getZone().getId()
										.toString());
							}
							if (cfpartner.getFirstAddress().getDepartment() != null
									&& this.getFirstAddressProvince() == null)
							{
								this.setFirstAddressProvince(String
										.valueOf(1000 + cfpartner
												.getFirstAddress()
												.getDepartment().getId()));
							}
						}
						else
						{
							if (cfpartner.getFirstAddress().getCity() != null
									&& this.getFirstAddressCity() == null)
							{
								this.setFirstAddressCity(cfpartner
										.getFirstAddress().getCity().getId()
										.toString());
							}
							if (cfpartner.getFirstAddress().getProvince() != null
									&& this.getFirstAddressProvince() == null)
							{
								this.setFirstAddressProvince(cfpartner
										.getFirstAddress().getProvince()
										.getId().toString());
							}
						}
					}
				}
				if (cfpartner.getSecondAddress() != null)
				{
					this.setSecondAddressEmail(cfpartner.getSecondAddress()
							.getEmail());
					this.setSecondAddressFaxNumber(cfpartner.getSecondAddress()
							.getFax());
					this.setSecondAddressPhoneNumber(cfpartner
							.getSecondAddress().getPhone());
					this.setSecondAddressAddress(cfpartner.getSecondAddress()
							.getAddress());
					if (cfpartner.getSecondAddress().getRegion() != null)
					{
						this.setSecondAddressRegion(cfpartner
								.getSecondAddress().getRegion().getId()
								.toString());

						if (cfpartner.getSecondAddress().getRegion()
								.getIsForFrLoc())
						{
							if (cfpartner.getSecondAddress().getZone() != null
									&& this.getSecondAddressCity() == null)
							{
								this.setSecondAddressCity(cfpartner
										.getSecondAddress().getZone().getId()
										.toString());
							}
							if (cfpartner.getSecondAddress().getDepartment() != null
									&& this.getSecondAddressProvince() == null)
							{
								this.setSecondAddressProvince(String
										.valueOf(1000 + cfpartner
												.getSecondAddress()
												.getDepartment().getId()));
							}
						}
						else
						{
							if (cfpartner.getSecondAddress().getCity() != null
									&& this.getSecondAddressCity() == null)
							{
								this.setSecondAddressCity(cfpartner
										.getSecondAddress().getCity().getId()
										.toString());
							}
							if (cfpartner.getSecondAddress().getProvince() != null
									&& this.getSecondAddressProvince() == null)
							{
								this.setSecondAddressProvince(cfpartner
										.getSecondAddress().getProvince()
										.getId().toString());
							}
						}
					}
				}

				ResponsibilityTypes type = BeansFactory.ResponsibilityTypes()
						.LoadByType(ResponsibilityTypeTypes.LAWRESPONSIBLE);
				Responsibilities ls = BeansFactory.Responsibilities()
						.LoadByCFAnagraphAndType(cfproject.getId(), type);
				if (ls != null)
				{
					this.setLsAddress(ls.getAddress());
					this.setLsEmail(ls.getEmail());
					this.setLsFaxNumber(ls.getFax());
					this.setLsFiscalCode(ls.getFiscalCode());
					this.setLsPhoneNumber(ls.getPhone());
					this.setLsRole(ls.getRole());
					this.setLsName(ls.getName());
					this.setLsSurname(ls.getSurname());
				}

				ResponsibilityTypes type2 = BeansFactory.ResponsibilityTypes()
						.LoadByType(ResponsibilityTypeTypes.RESPONSIBLEPEOPLE);
				Responsibilities rp = BeansFactory.Responsibilities()
						.LoadByCFAnagraphAndType(cfproject.getId(), type2);
				if (rp != null)
				{
					this.setRpAddress(rp.getAddress());
					this.setRpEmail(rp.getEmail());
					this.setRpFaxNumber(rp.getFax());
					this.setRpFiscalCode(rp.getFiscalCode());
					this.setRpPhoneNumber(rp.getPhone());
					this.setRpRole(rp.getRole());
					this.setRpName(rp.getName());
					this.setRpSurname(rp.getSurname());
				}

				ResponsibilityTypes type3 = BeansFactory.ResponsibilityTypes()
						.LoadByType(
								ResponsibilityTypeTypes.FINANCIALRESPONSIBLE);
				Responsibilities fr = BeansFactory.Responsibilities()
						.LoadByCFAnagraphAndType(cfproject.getId(), type3);
				if (fr != null)
				{
					this.setFrAddress(fr.getAddress());
					this.setFrEmail(fr.getEmail());
					this.setFrFaxNumber(fr.getFax());
					this.setFrFiscalCode(fr.getFiscalCode());
					this.setFrPhoneNumber(fr.getPhone());
					this.setFrRole(fr.getRole());
					this.setFrName(fr.getName());
					this.setFrSurname(fr.getSurname());
				}
			}
		}
	}

	private void loadCFPartnerAnagraphs() throws PersistenceBeanException
	{
		if (this.getSession().get("cfpartner") != null)
		{
			CFPartnerAnagraphs cf = BeansFactory.CFPartnerAnagraphs()
					.Load(Long.valueOf(this.getSession().get("cfpartner")
							.toString()));

			CFPartnerAnagraphProject cfProject = BeansFactory
					.CFPartnerAnagraphProject().LoadByPartner(
							Long.valueOf(this.getProjectId()), cf.getId());

			if (cf != null)
			{
				this.setPartnerNameTitle(cf.getName());

				this.setPartnerCountry(cf.getCountry().getValue());
				this.setPartnerPartita(cf.getPartita());
				if (cfProject.getDaec() != null)
				{
					this.setPartnerDaec(cfProject.getDaec().getUser().getName()
							+ " " + cfProject.getDaec().getUser().getSurname());
				}

				if (cf.getUser() != null)
				{
					this.setPartnerCfReferent(cf.getUser().getFiscalCode());
					this.setPartnerEmailReferent(cf.getUser().getMail());
					this.setPartnerReferentName(cf.getUser().getName());
					this.setPartnerReferentSurname(cf.getUser().getSurname());
				}

				this.setPartnerName(cf.getName());
				if (cfProject.getCil() != null)
				{
					this.setPartnerFirstLevelController(cfProject.getCil()
							.getUser().getName()
							+ " " + cfProject.getCil().getUser().getSurname());
				}
			}
		}
	}

	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException,
			PersistenceBeanException, PersistenceBeanException
	{
		CFPartnerAnagraphCompletations entityToSave = new CFPartnerAnagraphCompletations();
		CFPartnerAnagraphProject cfpa = null;

		if (this.getSession().get("cfpartner") != null)
		{
			cfpa = BeansFactory.CFPartnerAnagraphProject().LoadByPartner(
					Long.parseLong(this.getProjectId()),
					Long.parseLong(this.getSession().get("cfpartner")
							.toString()));
			if (cfpa != null)
			{
				List<CFPartnerAnagraphCompletations> lst = BeansFactory
						.CFPartnerAnagraphCompletations()
						.GetEntitiesForCFAnagraph(cfpa.getId(),
								this.getProjectId());

				if (!lst.isEmpty())
				{
					entityToSave = lst.get(0);
				}
			}
		}
		
		//CHECK THIS
		/*
		
		if(this.getNaming1().equals(Utils.getString(nonApplicabile))){
			this.setNaming2(Utils.getString(nonApplicabile));
			this.setNaming3(Utils.getString(nonApplicabile));
		}
		else if(this.getNaming2().equals(Utils.getString(nonApplicabile))){
			this.setNaming3(Utils.getString(nonApplicabile));
		}
		//
		 * 
		 */
		entityToSave.setCfPartnerAnagraphproject(cfpa);
		entityToSave.setNaming(this.getNaming3());
		entityToSave.setNaming1(this.getNaming1());
		entityToSave.setNaming2(this.getNaming2());
		entityToSave.setRecoverableVAT(this.getRecoverableVAT());
		entityToSave.setFesrCnValue(this.getFesrCnValue());
		
		if (this.getATECOcode4() != null && !this.getATECOcode4().isEmpty() && !this.getATECOcode4().equals(Utils.getString(nonApplicabile)))
		{
			entityToSave.setAtecoCode4(BeansFactory.Ateco()
					.Load(this.getATECOcode4()).getCode());
		}
		else{
			entityToSave.setAtecoCode4(Utils.getString(nonApplicabile));
		}
		if (this.getATECOcode3() != null && !this.getATECOcode3().isEmpty() && !this.getATECOcode3().equals(Utils.getString(nonApplicabile)))
		{
			entityToSave.setAtecoCode3(BeansFactory.Ateco()
					.Load(this.getATECOcode3()).getCode());
		}
		else{
			entityToSave.setAtecoCode3(Utils.getString(nonApplicabile));
		}
		if (this.getATECOcode2() != null && !this.getATECOcode2().isEmpty() && !this.getATECOcode2().equals(Utils.getString(nonApplicabile)))
		{
			entityToSave.setAtecoCode2(BeansFactory.Ateco()
					.Load(this.getATECOcode2()).getCode());
		}
		else{
			entityToSave.setAtecoCode2(Utils.getString(nonApplicabile));
		}
		if (this.getATECOcode1() != null && !this.getATECOcode1().isEmpty() && !this.getATECOcode1().equals(Utils.getString(nonApplicabile)))
		{
			entityToSave.setAtecoCode1(BeansFactory.Ateco()
					.Load(this.getATECOcode1()).getCode());
		}
		else{
			entityToSave.setAtecoCode1(Utils.getString(nonApplicabile));
		}

		entityToSave.setNote(this.getNotes());
		entityToSave.setNut2(this.getbNUT2());
		entityToSave.setNut3(this.getbNUT3());
		entityToSave.setProject(this.getProject());

		Addresses first = entityToSave.getFirstAddress();
		if (first == null)
		{
			first = new Addresses();
		}

		if (!this.getFirstAddressRegion().isEmpty())
		{
			Regions fRegion = BeansFactory.Regions().Load(
					this.getFirstAddressRegion());
			first.setRegion(fRegion);

			if (fRegion.getIsForFrLoc())
			{
				if (!this.getFirstAddressCity().isEmpty())
				{
					Zones fZone = BeansFactory.Zones().Load(
							this.getFirstAddressCity());
					first.setZone(fZone);
				}

				if (!this.getFirstAddressProvince().isEmpty())
				{
					Departments fDepartment = BeansFactory
							.Departments()
							.Load(Long.parseLong(this.getFirstAddressProvince()) - 1000);
					first.setDepartment(fDepartment);
				}
			}
			else
			{
				if (!this.getFirstAddressCity().isEmpty())
				{
					Cities fCity = BeansFactory.Cities().Load(
							this.getFirstAddressCity());
					first.setCity(fCity);
				}

				if (!this.getFirstAddressProvince().isEmpty())
				{
					Provinces fProvince = BeansFactory.Provinces().Load(
							this.getFirstAddressProvince());
					first.setProvince(fProvince);
				}
			}
		}

		first.setAddress(this.getFirstAddressAddress());
		first.setEmail(this.getFirstAddressEmail());
		first.setFax(this.getFirstAddressFaxNumber());
		first.setPhone(this.getFirstAddressPhoneNumber());
		entityToSave.setFirstAddress(first);
		BeansFactory.Addresses().SaveInTransaction(first);

		Addresses second = entityToSave.getSecondAddress();
		if (second == null)
		{
			second = new Addresses();

		}

		if (!this.getSecondAddressRegion().isEmpty())
		{
			Regions sRegion = BeansFactory.Regions().Load(
					this.getSecondAddressRegion());
			second.setRegion(sRegion);

			if (sRegion.getIsForFrLoc())
			{
				if (!this.getSecondAddressCity().isEmpty())
				{
					Zones sZone = BeansFactory.Zones().Load(
							this.getSecondAddressCity());
					second.setZone(sZone);
				}

				if (!this.getSecondAddressProvince().isEmpty())
				{
					Departments sDepartment = BeansFactory.Departments()
							.Load(Long.parseLong(this
									.getSecondAddressProvince()) - 1000);
					second.setDepartment(sDepartment);
				}
			}
			else
			{
				if (!this.getSecondAddressCity().isEmpty())
				{
					Cities sCity = BeansFactory.Cities().Load(
							this.getSecondAddressCity());
					second.setCity(sCity);
				}

				if (!this.getSecondAddressProvince().isEmpty())
				{
					Provinces sProvince = BeansFactory.Provinces().Load(
							this.getSecondAddressProvince());
					second.setProvince(sProvince);
				}
			}

		}

		second.setAddress(this.getSecondAddressAddress());
		second.setEmail(this.getSecondAddressEmail());
		second.setFax(this.getSecondAddressFaxNumber());
		second.setPhone(this.getSecondAddressPhoneNumber());
		entityToSave.setSecondAddress(second);
		BeansFactory.Addresses().SaveInTransaction(second);

		BeansFactory.CFPartnerAnagraphCompletations().SaveInTransaction(
				entityToSave);

		// Save Law responsible data
		ResponsibilityTypes type = BeansFactory.ResponsibilityTypes()
				.LoadByType(ResponsibilityTypeTypes.LAWRESPONSIBLE);
		Responsibilities ls = BeansFactory.Responsibilities()
				.LoadByCFAnagraphAndType(cfpa.getId(), type);
		if (ls == null)
		{
			ls = new Responsibilities();
		}
		ls.setAddress(this.getLsAddress());
		ls.setEmail(this.getLsEmail());
		ls.setFax(this.getLsFaxNumber());
		ls.setFiscalCode(this.getLsFiscalCode());
		ls.setPhone(this.getLsPhoneNumber());
		ls.setRole(this.getLsRole());
		ls.setName(this.getLsName());
		ls.setSurname(this.getLsSurname());
		ls.setCfPartnerAnagraph(cfpa);
		ls.setType(type);

		BeansFactory.Responsibilities().SaveInTransaction(ls);

		// if (this.getSessionBean().isCF())
		// {
		ResponsibilityTypes type2 = BeansFactory.ResponsibilityTypes()
				.LoadByType(ResponsibilityTypeTypes.RESPONSIBLEPEOPLE);

		Responsibilities rp = BeansFactory.Responsibilities()
				.LoadByCFAnagraphAndType(cfpa.getId(), type2);
		if (rp == null)
		{
			rp = new Responsibilities();
		}
		rp.setAddress(this.getRpAddress());
		rp.setEmail(this.getRpEmail());
		rp.setFax(this.getRpFaxNumber());
		rp.setFiscalCode(this.getRpFiscalCode());
		rp.setPhone(this.getRpPhoneNumber());
		rp.setRole(this.getRpRole());
		rp.setName(this.getRpName());
		rp.setSurname(this.getRpSurname());
		rp.setCfPartnerAnagraph(cfpa);
		rp.setType(type2);

		BeansFactory.Responsibilities().SaveInTransaction(rp);

		ResponsibilityTypes type3 = BeansFactory.ResponsibilityTypes()
				.LoadByType(ResponsibilityTypeTypes.FINANCIALRESPONSIBLE);
		Responsibilities fr = BeansFactory.Responsibilities()
				.LoadByCFAnagraphAndType(cfpa.getId(), type3);
		if (fr == null)
		{
			fr = new Responsibilities();
		}

		fr.setAddress(this.getFrAddress());
		fr.setEmail(this.getFrEmail());
		fr.setFax(this.getFrFaxNumber());
		fr.setFiscalCode(this.getFrFiscalCode());
		fr.setPhone(this.getFrPhoneNumber());
		fr.setRole(this.getFrRole());
		fr.setName(this.getFrName());
		fr.setSurname(this.getFrSurname());
		fr.setCfPartnerAnagraph(cfpa);
		fr.setType(type3);

		BeansFactory.Responsibilities().SaveInTransaction(fr);
		// }

		Projects project = BeansFactory.Projects().Load(
				String.valueOf(this.getSession().get("project")));

		listCFPMS = new ArrayList<InfoObject>();

		if (this.getProjectState().equals(ProjectState.Actual))
		{
			if (this.getSessionBean().isPartner())
			{
				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.STC))
				{
					InfoObject info = new InfoObject();
					info.setMail(item.getMail());
					info.setName(this.getCurrentUser().getName());
					info.setSurname(this.getCurrentUser().getSurname());
					info.setProjectName(project.getTitle());
					listCFPMS.add(info);
				}

				for (Users item : BeansFactory.Users().getByRole(
						UserRoleTypes.AGU))
				{
					InfoObject info = new InfoObject();
					info.setMail(item.getMail());
					info.setName(this.getCurrentUser().getName());
					info.setSurname(this.getCurrentUser().getSurname());
					info.setProjectName(project.getTitle());
					listCFPMS.add(info);
				}

				InfoObject ob = new InfoObject();
				ob.setProjectName(this.getProject().getTitle());
				ob.setName(this.getCurrentUser().getName());
				ob.setSurname(this.getCurrentUser().getSurname());
				List<CFPartnerAnagraphs> cpap = BeansFactory
						.CFPartnerAnagraphs()
						.GetCFAnagraphForProject(this.getProjectId(),
								CFAnagraphTypes.CFAnagraph);
				if (cpap.size() > 0 && cpap.get(0).getUser() != null)
				{
					ob.setMail(cpap.get(0).getUser().getMail());

					listCFPMS.add(ob);
				}

			}
		}
	}

	private void FillComboboxes() throws HibernateException,
			PersistenceBeanException
	{
		this.FillNamings();
		this.FillAtecos();
		this.FillFirstAddress();
		this.FillSecondAddress();
	}

	private void FillFirstAddress() throws HibernateException,
			PersistenceBeanException
	{
		this.lstFirstAddressRegion = new ArrayList<SelectItem>();
		this.lstFirstAddressRegion.add(SelectItemHelper.getFirst());

		List<Regions> lst1 = new ArrayList<Regions>();

		try
		{
			lst1 = BeansFactory.Regions().Load();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

		for (Regions r : lst1)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(r.getId()));
			item.setLabel(r.getValue());

			this.lstFirstAddressRegion.add(item);
		}

		this.lstFirstAddressProvince = new ArrayList<SelectItem>();
		this.lstFirstAddressProvince.add(SelectItemHelper.getFirst());

		if (this.getFirstAddressRegion() != null
				&& !this.getFirstAddressRegion().isEmpty())
		{
			Regions region = BeansFactory.Regions().Load(
					this.getFirstAddressRegion());
			if (region.getIsForItLoc())
			{
				List<Provinces> lst2 = new ArrayList<Provinces>();
				try
				{
					if (this.getFirstAddressRegion() != null
							&& !this.getFirstAddressRegion().isEmpty())
					{
						lst2 = BeansFactory.Provinces().LoadByRegion(
								this.getFirstAddressRegion());
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Provinces p : lst2)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(p.getId()));
					item.setLabel(p.getValue());

					this.lstFirstAddressProvince.add(item);
				}

				this.lstFirstAddressCity = new ArrayList<SelectItem>();
				this.lstFirstAddressCity.add(SelectItemHelper.getFirst());

				List<Cities> lst3 = new ArrayList<Cities>();

				try
				{
					if (this.getFirstAddressProvince() != null
							&& !this.getFirstAddressProvince().isEmpty())
					{
						lst3 = BeansFactory.Cities().LoadByProvince(
								this.getFirstAddressProvince());
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Cities c : lst3)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(c.getId()));
					item.setLabel(c.getValue());

					this.lstFirstAddressCity.add(item);
				}
			}
			else
			{
				List<Departments> lst2 = new ArrayList<Departments>();
				try
				{
					lst2 = BeansFactory.Departments().LoadByRegion(
							this.getFirstAddressRegion());
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Departments p : lst2)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(1000 + p.getId()));
					item.setLabel(p.getValue());

					this.lstFirstAddressProvince.add(item);
				}

				this.lstFirstAddressCity = new ArrayList<SelectItem>();
				this.lstFirstAddressCity.add(SelectItemHelper.getFirst());

				List<Zones> lst3 = new ArrayList<Zones>();

				try
				{
					if (this.getFirstAddressProvince() != null
							&& !this.getFirstAddressProvince().isEmpty())
					{
						lst3 = BeansFactory.Zones().LoadByDepartment(
								String.valueOf(Long.parseLong(this
										.getFirstAddressProvince()) - 1000));
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Zones c : lst3)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(c.getId()));
					item.setLabel(c.getValue());

					this.lstFirstAddressCity.add(item);
				}
			}
		}
		else
		{
			this.lstFirstAddressCity = new ArrayList<SelectItem>();
			this.lstFirstAddressCity.add(SelectItemHelper.getFirst());
		}
	}

	private void FillSecondAddress() throws HibernateException,
			PersistenceBeanException
	{
		this.lstSecondAddressRegion = new ArrayList<SelectItem>();
		this.lstSecondAddressRegion.add(SelectItemHelper.getFirst());

		List<Regions> lst1 = new ArrayList<Regions>();

		try
		{
			lst1 = BeansFactory.Regions().Load();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

		for (Regions r : lst1)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(r.getId()));
			item.setLabel(r.getValue());

			this.lstSecondAddressRegion.add(item);
		}

		this.lstSecondAddressProvince = new ArrayList<SelectItem>();
		this.lstSecondAddressProvince.add(SelectItemHelper.getFirst());

		if (this.getSecondAddressRegion() != null
				&& !this.getSecondAddressRegion().isEmpty())
		{
			Regions region = BeansFactory.Regions().Load(
					this.getSecondAddressRegion());
			if (region.getIsForItLoc())
			{
				List<Provinces> lst2 = new ArrayList<Provinces>();

				try
				{
					if (this.getSecondAddressRegion() != null
							&& !this.getSecondAddressRegion().isEmpty())
					{
						lst2 = BeansFactory.Provinces().LoadByRegion(
								this.getSecondAddressRegion());
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Provinces p : lst2)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(p.getId()));
					item.setLabel(p.getValue());

					this.lstSecondAddressProvince.add(item);
				}

				this.lstSecondAddressCity = new ArrayList<SelectItem>();
				this.lstSecondAddressCity.add(SelectItemHelper.getFirst());

				List<Cities> lst3 = new ArrayList<Cities>();

				try
				{
					if (this.getSecondAddressProvince() != null
							&& !this.getSecondAddressProvince().isEmpty())
					{
						lst3 = BeansFactory.Cities().LoadByProvince(
								this.getSecondAddressProvince());
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Cities c : lst3)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(c.getId()));
					item.setLabel(c.getValue());

					this.lstSecondAddressCity.add(item);
				}
			}
			else
			{
				List<Departments> lst2 = new ArrayList<Departments>();

				try
				{
					if (this.getSecondAddressRegion() != null
							&& !this.getSecondAddressRegion().isEmpty())
					{
						lst2 = BeansFactory.Departments().LoadByRegion(
								this.getSecondAddressRegion());
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Departments p : lst2)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(1000 + p.getId()));
					item.setLabel(p.getValue());

					this.lstSecondAddressProvince.add(item);
				}

				this.lstSecondAddressCity = new ArrayList<SelectItem>();
				this.lstSecondAddressCity.add(SelectItemHelper.getFirst());

				List<Zones> lst3 = new ArrayList<Zones>();

				try
				{
					if (this.getSecondAddressProvince() != null
							&& !this.getSecondAddressProvince().isEmpty())
					{
						lst3 = BeansFactory.Zones().LoadByDepartment(
								String.valueOf(Long.parseLong(this
										.getSecondAddressProvince()) - 1000));
					}
				}
				catch (PersistenceBeanException e)
				{
					log.error(e);
				}

				for (Zones c : lst3)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(c.getId()));
					item.setLabel(c.getValue());

					this.lstSecondAddressCity.add(item);
				}
			}
		}
		else
		{
			this.lstSecondAddressCity = new ArrayList<SelectItem>();
			this.lstSecondAddressCity.add(SelectItemHelper.getFirst());

		}

	}

	private void FillNamings() throws HibernateException,
			PersistenceBeanException
	{
		SelectItem nonApplicabileSelectItem = new SelectItem();
		nonApplicabileSelectItem.setValue(Utils.getString(nonApplicabile));
		nonApplicabileSelectItem.setLabel(Utils.getString(nonApplicabile));

		// Direct filling
		this.lstNaming1 = new ArrayList<SelectItem>();
		this.lstNaming1.add(SelectItemHelper.getFirst());

		List<Naming> lst1 = new ArrayList<Naming>();

		try
		{
			lst1 = BeansFactory.Naming().LoadRoots();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

		for (Naming n : lst1)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(n.getId()));
			item.setLabel(n.getValue());

			this.lstNaming1.add(item);
		}
		this.lstNaming1.add(nonApplicabileSelectItem);

		// if(!lst1.isEmpty())
		{
			this.lstNaming2 = new ArrayList<SelectItem>();


			/*
			 * if(this.getNaming1() == null && lst1.size() > 0) {
			 * this.setNaming1(lst1.get(0).getId().toString()); }
			 */

			List<Naming> lst2 = new ArrayList<Naming>();

			if (this.getNaming1() != null && !this.getNaming1().isEmpty()
					&& !this.getNaming1().equals(Utils.getString(nonApplicabile)))
			{
				lst2 = BeansFactory.Naming().LoadByParent(
						Long.valueOf(this.getNaming1()));
				this.lstNaming2.add(SelectItemHelper.getFirst());
				for (Naming n : lst2)
				{
					SelectItem item = new SelectItem();
					item.setValue(String.valueOf(n.getId()));
					item.setLabel(n.getValue());

					this.lstNaming2.add(item);
				}

			}
			this.lstNaming2.add(nonApplicabileSelectItem);

			// if(!lst2.isEmpty())
			{
				this.lstNaming3 = new ArrayList<SelectItem>();
	

				/*
				 * if(this.getNaming2() == null && lst2.size() > 0) {
				 * this.setNaming2(lst2.get(0).getId().toString()); }
				 */

				List<Naming> lst3 = new ArrayList<Naming>();
				if (this.getNaming2() != null && !this.getNaming2().isEmpty()
						&& !this.getNaming2().equals(Utils.getString(nonApplicabile)))
				{
					lst3 = BeansFactory.Naming().LoadByParent(
							Long.valueOf(this.getNaming2()));
				
					this.lstNaming3.add(SelectItemHelper.getFirst());
					for (Naming n : lst3)
					{
						SelectItem item = new SelectItem();
						item.setValue(String.valueOf(n.getId()));
						item.setLabel(n.getValue());

						this.lstNaming3.add(item);
					}
				}
				this.lstNaming3.add(nonApplicabileSelectItem);

				/*
				 * if(!lst3.isEmpty()) { if(this.getNaming3() == null) {
				 * this.setNaming3(lst3.get(0).getId().toString()); } }
				 */
			}
		}
	}

	private void FillAtecos() throws HibernateException,
			PersistenceBeanException
	{
		SelectItem nonApplicabileSelectItem = new SelectItem();
		nonApplicabileSelectItem.setValue(Utils.getString(nonApplicabile));
		nonApplicabileSelectItem.setLabel(Utils.getString(nonApplicabile));

		// Direct filling
		this.lstATECOcode1 = new ArrayList<SelectItem>();
		this.lstATECOcode1.add(SelectItemHelper.getFirst());

		List<Ateco> lst1 = new ArrayList<Ateco>();

		try
		{
			lst1 = BeansFactory.Ateco().LoadRoots();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

		for (Ateco a : lst1)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(a.getId()));
			item.setLabel(a.getValue());

			this.lstATECOcode1.add(item);
		}
		this.lstATECOcode1.add(nonApplicabileSelectItem);

		this.lstATECOcode2 = new ArrayList<SelectItem>();

		List<Ateco> lst2 = new ArrayList<Ateco>();

		if (this.getATECOcode1() != null && !this.getATECOcode1().isEmpty()
				&& !this.getATECOcode1().equals(Utils.getString(nonApplicabile)))
		{
			lst2 = BeansFactory.Ateco().LoadByParent(
					Long.valueOf(this.getATECOcode1()));
			this.lstATECOcode2.add(SelectItemHelper.getFirst());
			for (Ateco a : lst2)
			{
				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(a.getId()));
				item.setLabel(a.getValue());

				this.lstATECOcode2.add(item);
			}
		}
		this.lstATECOcode2.add(nonApplicabileSelectItem);

		this.lstATECOcode3 = new ArrayList<SelectItem>();


		List<Ateco> lst3 = new ArrayList<Ateco>();
		if (this.getATECOcode2() != null && !this.getATECOcode2().isEmpty()
				&& !this.getATECOcode2().equals(Utils.getString(nonApplicabile)))
		{
			lst3 = BeansFactory.Ateco().LoadByParent(
					Long.valueOf(this.getATECOcode2()));
			this.lstATECOcode3.add(SelectItemHelper.getFirst());
			for (Ateco a : lst3)
			{
				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(a.getId()));
				item.setLabel(a.getValue());

				this.lstATECOcode3.add(item);
			}
		}
		this.lstATECOcode3.add(nonApplicabileSelectItem);

		this.lstATECOcode4 = new ArrayList<SelectItem>();


		List<Ateco> lst4 = new ArrayList<Ateco>();

		if (this.getATECOcode3() != null && !this.getATECOcode3().isEmpty()
				&& !this.getATECOcode3().equals(Utils.getString(nonApplicabile)))
		{
			lst4 = BeansFactory.Ateco().LoadByParent(
					Long.valueOf(this.getATECOcode3()));
			this.lstATECOcode4.add(SelectItemHelper.getFirst());
			for (Ateco a : lst4)
			{
				SelectItem item = new SelectItem();
				item.setValue(String.valueOf(a.getId()));
				item.setLabel(a.getValue());

				this.lstATECOcode4.add(item);
			}
		}
		this.lstATECOcode4.add(nonApplicabileSelectItem);

		/*
		 * if (!lst4.isEmpty()) { if (this.getATECOcode4() == null) {
		 * this.setATECOcode4(lst4.get(0).getId().toString()); } }
		 */
	}

	/**
	 * Event handlers
	 * 
	 */

	public void naming1Change(ValueChangeEvent event)
			throws HibernateException, PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			if (event.getNewValue().equals(Utils.getString(nonApplicabile)))
			{
				this.setNaming1(Utils.getString(nonApplicabile));
				this.setNaming2(Utils.getString(nonApplicabile));
				this.setNaming3(Utils.getString(nonApplicabile));
				this.FillNamings();
			}
			else
			{
				this.setNaming1(event.getNewValue().toString());
				this.setNaming2(null);
				this.setNaming3(null);

				this.FillNamings();
			}
		}
	}

	public void naming2Change(ValueChangeEvent event)
			throws HibernateException, PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			if (event.getNewValue().equals(Utils.getString(nonApplicabile)))
			{
				this.setNaming2(Utils.getString(nonApplicabile));
				this.setNaming3(Utils.getString(nonApplicabile));
				this.FillNamings();
			}
			else
			{
				this.setNaming2(event.getNewValue().toString());
				this.setNaming3(null);
				this.FillNamings();
			}
		}
	}

	public void naming3Change(ValueChangeEvent event)
	{
		if (event.getNewValue() != null)
		{
			this.setNaming3(event.getNewValue().toString());
		}
	}

	public void ateco1Change(ValueChangeEvent event) throws HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			if (event.getNewValue().equals(Utils.getString(nonApplicabile)))
			{
				this.setATECOcode1(Utils.getString(nonApplicabile));
				this.setATECOcode2(Utils.getString(nonApplicabile));
				this.setATECOcode3(Utils.getString(nonApplicabile));
				this.setATECOcode4(Utils.getString(nonApplicabile));

				this.FillAtecos();

			}
			else
			{
				this.setATECOcode1(event.getNewValue().toString());
				this.setATECOcode2(null);
				this.setATECOcode3(null);
				this.setATECOcode4(null);

				this.FillAtecos();

			}

		}
	}

	public void ateco2Change(ValueChangeEvent event) throws HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			if (event.getNewValue().equals(Utils.getString(nonApplicabile)))
			{
				this.setATECOcode2(Utils.getString(nonApplicabile));
				this.setATECOcode3(Utils.getString(nonApplicabile));
				this.setATECOcode4(Utils.getString(nonApplicabile));

				this.FillAtecos();
			}
			this.setATECOcode2(event.getNewValue().toString());
			this.setATECOcode3(null);
			this.setATECOcode4(null);

			this.FillAtecos();
		}
	}

	public void ateco3Change(ValueChangeEvent event) throws HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			if (event.getNewValue().equals(Utils.getString(nonApplicabile)))
			{
				this.setATECOcode3(Utils.getString(nonApplicabile));
				this.setATECOcode4(Utils.getString(nonApplicabile));

				this.FillAtecos();
			}
			else
			{
				this.setATECOcode3(event.getNewValue().toString());
				this.setATECOcode4(null);

				this.FillAtecos();
			}
		}
	}

	public void ateco4Change(ValueChangeEvent event)
	{
		if (event.getNewValue() != null)
		{
			this.setATECOcode4(event.getNewValue().toString());
		}
	}

	public void firstRegionChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			this.setFirstAddressRegion(event.getNewValue().toString());
			this.setFirstAddressProvince("");
			this.setFirstAddressCity("");

			this.FillFirstAddress();
		}
	}

	public void secondRegionChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			this.setSecondAddressRegion(event.getNewValue().toString());
			this.setSecondAddressProvince("");
			this.setSecondAddressCity("");

			this.FillSecondAddress();
		}
	}

	public void firstProvinceChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			this.setFirstAddressProvince(event.getNewValue().toString());
			this.setFirstAddressCity("");

			this.FillFirstAddress();
		}
	}

	public void secondProvinceChange(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		if (event.getNewValue() != null)
		{
			this.setSecondAddressProvince(event.getNewValue().toString());
			this.setSecondAddressCity("");

			this.FillSecondAddress();
		}
	}

	/**
	 * Public Properties
	 * 
	 */

	public void setbNUT2(Boolean bNUT2)
	{
		this.bNUT2 = bNUT2;
	}

	public Boolean getbNUT2()
	{
		return bNUT2;
	}

	public void setbNUT3(Boolean bNUT3)
	{
		this.bNUT3 = bNUT3;
	}

	public Boolean getbNUT3()
	{
		return bNUT3;
	}

	public void setFirstAddressEmail(String firstAddressEmail)
	{
		this.firstAddressEmail = firstAddressEmail;
	}

	public String getFirstAddressEmail()
	{
		return firstAddressEmail;
	}

	public void setFirstAddressPhoneNumber(String firstAddressPhoneNumber)
	{
		this.firstAddressPhoneNumber = firstAddressPhoneNumber;
	}

	public String getFirstAddressPhoneNumber()
	{
		return firstAddressPhoneNumber;
	}

	public void setFirstAddressFaxNumber(String firstAddressFaxNumber)
	{
		this.firstAddressFaxNumber = firstAddressFaxNumber;
	}

	public String getFirstAddressFaxNumber()
	{
		return firstAddressFaxNumber;
	}

	public void setSecondAddressEmail(String secondAddressEmail)
	{
		this.secondAddressEmail = secondAddressEmail;
	}

	public String getSecondAddressEmail()
	{
		return secondAddressEmail;
	}

	public void setSecondAddressPhoneNumber(String secondAddressPhoneNumber)
	{
		this.secondAddressPhoneNumber = secondAddressPhoneNumber;
	}

	public String getSecondAddressPhoneNumber()
	{
		return secondAddressPhoneNumber;
	}

	public void setSecondAddressFaxNumber(String secondAddressFaxNumber)
	{
		this.secondAddressFaxNumber = secondAddressFaxNumber;
	}

	public String getSecondAddressFaxNumber()
	{
		return secondAddressFaxNumber;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setFrFiscalCode(String frFiscalCode)
	{
		this.frFiscalCode = frFiscalCode;
	}

	public String getFrFiscalCode()
	{
		return frFiscalCode;
	}

	public void setFrName(String frName)
	{
		this.frName = frName;
	}

	public String getFrName()
	{
		return frName;
	}

	public void setFrSurname(String frSurname)
	{
		this.frSurname = frSurname;
	}

	public String getFrSurname()
	{
		return frSurname;
	}

	public void setFrRole(String frRole)
	{
		this.frRole = frRole;
	}

	public String getFrRole()
	{
		return frRole;
	}

	public void setFrAddress(String frAddress)
	{
		this.frAddress = frAddress;
	}

	public String getFrAddress()
	{
		return frAddress;
	}

	public void setFrEmail(String frEmail)
	{
		this.frEmail = frEmail;
	}

	public String getFrEmail()
	{
		return frEmail;
	}

	public void setFrPhoneNumber(String frPhoneNumber)
	{
		this.frPhoneNumber = frPhoneNumber;
	}

	public String getFrPhoneNumber()
	{
		return frPhoneNumber;
	}

	public void setFrFaxNumber(String frFaxNumber)
	{
		this.frFaxNumber = frFaxNumber;
	}

	public String getFrFaxNumber()
	{
		return frFaxNumber;
	}

	public void setRpFiscalCode(String rpFiscalCode)
	{
		this.rpFiscalCode = rpFiscalCode;
	}

	public String getRpFiscalCode()
	{
		return rpFiscalCode;
	}

	public void setRpName(String rpName)
	{
		this.rpName = rpName;
	}

	public String getRpName()
	{
		return rpName;
	}

	public void setRpSurname(String rpSurname)
	{
		this.rpSurname = rpSurname;
	}

	public String getRpSurname()
	{
		return rpSurname;
	}

	public void setRpRole(String rpRole)
	{
		this.rpRole = rpRole;
	}

	public String getRpRole()
	{
		return rpRole;
	}

	public void setRpAddress(String rpAddress)
	{
		this.rpAddress = rpAddress;
	}

	public String getRpAddress()
	{
		return rpAddress;
	}

	public void setRpEmail(String rpEmail)
	{
		this.rpEmail = rpEmail;
	}

	public String getRpEmail()
	{
		return rpEmail;
	}

	public void setRpPhoneNumber(String rpPhoneNumber)
	{
		this.rpPhoneNumber = rpPhoneNumber;
	}

	public String getRpPhoneNumber()
	{
		return rpPhoneNumber;
	}

	public void setRpFaxNumber(String rpFaxNumber)
	{
		this.rpFaxNumber = rpFaxNumber;
	}

	public String getRpFaxNumber()
	{
		return rpFaxNumber;
	}

	public void setLsFiscalCode(String lsFiscalCode)
	{
		this.lsFiscalCode = lsFiscalCode;
	}

	public String getLsFiscalCode()
	{
		return lsFiscalCode;
	}

	public void setLsName(String lsName)
	{
		this.lsName = lsName;
	}

	public String getLsName()
	{
		return lsName;
	}

	public void setLsRole(String lsRole)
	{
		this.lsRole = lsRole;
	}

	public String getLsRole()
	{
		return lsRole;
	}

	public void setLsAddress(String lsAddress)
	{
		this.lsAddress = lsAddress;
	}

	public String getLsAddress()
	{
		return lsAddress;
	}

	public void setLsEmail(String lsEmail)
	{
		this.lsEmail = lsEmail;
	}

	public String getLsEmail()
	{
		return lsEmail;
	}

	public void setLsPhoneNumber(String lsPhoneNumber)
	{
		this.lsPhoneNumber = lsPhoneNumber;
	}

	public String getLsPhoneNumber()
	{
		return lsPhoneNumber;
	}

	public void setLsFaxNumber(String lsFaxNumber)
	{
		this.lsFaxNumber = lsFaxNumber;
	}

	public String getLsFaxNumber()
	{
		return lsFaxNumber;
	}

	public void setLsSurname(String lsSurname)
	{
		this.lsSurname = lsSurname;
	}

	public String getLsSurname()
	{
		return lsSurname;
	}

	public void setFirstAddressRegion(String firstAddressRegion)
	{
		this.getSession().put("firstAddressRegion", firstAddressRegion);
	}

	public String getFirstAddressRegion()
	{
		if (this.getSession().get("firstAddressRegion") == null)
		{
			return null;
		}

		return this.getSession().get("firstAddressRegion").toString();
	}

	public void setSecondAddressRegion(String secondAddressRegion)
	{
		this.getSession().put("secondAddressRegion", secondAddressRegion);
	}

	public String getSecondAddressRegion()
	{
		if (this.getSession().get("secondAddressRegion") == null)
		{
			return null;
		}

		return this.getSession().get("secondAddressRegion").toString();
	}

	public void setSecondAddressAddress(String secondAddressAddress)
	{
		this.getSession().put("secondAddressAddress", secondAddressAddress);
	}

	public String getSecondAddressAddress()
	{
		if (this.getSession().get("secondAddressAddress") == null)
		{
			return null;
		}

		return this.getSession().get("secondAddressAddress").toString();
	}

	public void setFirstAddressAddress(String firstAddressAddress)
	{
		this.getSession().put("firstAddressAddress", firstAddressAddress);
	}

	public String getFirstAddressAddress()
	{
		if (this.getSession().get("firstAddressAddress") == null)
		{
			return null;
		}

		return this.getSession().get("firstAddressAddress").toString();
	}

	public void setFirstAddressProvince(String firstAddressProvince)
	{
		this.getSession().put("firstAddressProvince", firstAddressProvince);
	}

	public String getFirstAddressProvince()
	{
		if (this.getSession().get("firstAddressProvince") == null)
		{
			return null;
		}

		return this.getSession().get("firstAddressProvince").toString();
	}

	public void setSecondAddressProvince(String secondAddressProvince)
	{
		this.getSession().put("secondAddressProvince", secondAddressProvince);
	}

	public String getSecondAddressProvince()
	{
		if (this.getSession().get("secondAddressProvince") == null)
		{
			return null;
		}

		return this.getSession().get("secondAddressProvince").toString();
	}

	public void setFirstAddressCity(String firstAddressCity)
	{
		this.getSession().put("firstAddressCity", firstAddressCity);
	}

	public String getFirstAddressCity()
	{
		if (this.getSession().get("firstAddressCity") == null)
		{
			return null;
		}

		return this.getSession().get("firstAddressCity").toString();
	}

	public void setSecondAddressCity(String secondAddressCity)
	{
		this.getSession().put("secondAddressCity", secondAddressCity);
	}

	public String getSecondAddressCity()
	{
		if (this.getSession().get("secondAddressCity") == null)
		{
			return null;
		}

		return this.getSession().get("secondAddressCity").toString();
	}

	public void setLstFirstAddressRegion(List<SelectItem> lstFirstAddressRegion)
	{
		this.lstFirstAddressRegion = lstFirstAddressRegion;
	}

	public List<SelectItem> getLstFirstAddressRegion()
	{
		return lstFirstAddressRegion;
	}

	public void setLstSecondAddressRegion(
			List<SelectItem> lstSecondAddressRegion)
	{
		this.lstSecondAddressRegion = lstSecondAddressRegion;
	}

	public List<SelectItem> getLstSecondAddressRegion()
	{
		return lstSecondAddressRegion;
	}

	public void setLstFirstAddressProvince(
			List<SelectItem> lstFirstAddressProvince)
	{
		this.lstFirstAddressProvince = lstFirstAddressProvince;
	}

	public List<SelectItem> getLstFirstAddressProvince()
	{
		return lstFirstAddressProvince;
	}

	public void setLstSecondAddressProvince(
			List<SelectItem> lstSecondAddressProvince)
	{
		this.lstSecondAddressProvince = lstSecondAddressProvince;
	}

	public List<SelectItem> getLstSecondAddressProvince()
	{
		return lstSecondAddressProvince;
	}

	public void setLstFirstAddressCity(List<SelectItem> lstFirstAddressCity)
	{
		this.lstFirstAddressCity = lstFirstAddressCity;
	}

	public List<SelectItem> getLstFirstAddressCity()
	{
		return lstFirstAddressCity;
	}

	public void setLstSecondAddressCity(List<SelectItem> lstSecondAddressCity)
	{
		this.lstSecondAddressCity = lstSecondAddressCity;
	}

	public List<SelectItem> getLstSecondAddressCity()
	{
		return lstSecondAddressCity;
	}

	public void setLstNaming1(List<SelectItem> lstNaming1)
	{
		this.lstNaming1 = lstNaming1;
	}

	public List<SelectItem> getLstNaming1()
	{
		return lstNaming1;
	}

	public void setLstATECOcode1(List<SelectItem> lstATECOcode1)
	{
		this.lstATECOcode1 = lstATECOcode1;
	}

	public List<SelectItem> getLstATECOcode1()
	{
		return lstATECOcode1;
	}

	public void setLstNaming2(List<SelectItem> lstNaming2)
	{
		this.lstNaming2 = lstNaming2;
	}

	public List<SelectItem> getLstNaming2()
	{
		return lstNaming2;
	}

	public void setLstNaming3(List<SelectItem> lstNaming3)
	{
		this.lstNaming3 = lstNaming3;
	}

	public List<SelectItem> getLstNaming3()
	{
		return lstNaming3;
	}

	public void setLstATECOcode2(List<SelectItem> lstATECOcode2)
	{
		this.lstATECOcode2 = lstATECOcode2;
	}

	public List<SelectItem> getLstATECOcode2()
	{
		return lstATECOcode2;
	}

	public void setLstATECOcode3(List<SelectItem> lstATECOcode3)
	{
		this.lstATECOcode3 = lstATECOcode3;
	}

	public List<SelectItem> getLstATECOcode3()
	{
		return lstATECOcode3;
	}

	public void setNaming1(String naming1)
	{
		this.getSession().put("naming1", naming1);
	}

	public String getNaming1()
	{
		if (this.getSession().get("naming1") == null)
		{
			return null;
		}

		return this.getSession().get("naming1").toString();
	}

	public void setNaming2(String naming1)
	{
		this.getSession().put("naming2", naming1);
	}

	public String getNaming2()
	{
		if (this.getSession().get("naming2") == null)
		{
			return null;
		}

		return this.getSession().get("naming2").toString();
	}

	public void setNaming3(String naming1)
	{
		this.getSession().put("naming3", naming1);
	}

	public String getNaming3()
	{
		if (this.getSession().get("naming3") == null)
		{
			return null;
		}

		return this.getSession().get("naming3").toString();
	}

	public void setATECOcode1(String aTECOcode1)
	{
		this.getSession().put("ATECOcode1", aTECOcode1);
	}

	public String getATECOcode1()
	{
		if (this.getSession().get("ATECOcode1") == null)
		{
			return null;
		}

		return this.getSession().get("ATECOcode1").toString();
	}

	public void setATECOcode2(String aTECOcode1)
	{
		this.getSession().put("ATECOcode2", aTECOcode1);
	}

	public String getATECOcode2()
	{
		if (this.getSession().get("ATECOcode2") == null)
		{
			return null;
		}

		return this.getSession().get("ATECOcode2").toString();
	}

	public void setATECOcode3(String aTECOcode1)
	{
		this.getSession().put("ATECOcode3", aTECOcode1);
	}

	public String getATECOcode3()
	{
		if (this.getSession().get("ATECOcode3") == null)
		{
			return null;
		}

		return this.getSession().get("ATECOcode3").toString();
	}

	public void setATECOcode4(String aTECOcode1)
	{
		this.getSession().put("ATECOcode4", aTECOcode1);
	}

	public String getATECOcode4()
	{
		if (this.getSession().get("ATECOcode4") == null)
		{
			return null;
		}

		return this.getSession().get("ATECOcode4").toString();
	}
	
	public void setRecoverableVAT(Boolean recoverableVAT)
	{
		this.getSession().put("recoverableVAT", recoverableVAT);
	}

	public Boolean getRecoverableVAT()
	{
		if (this.getSession().get("recoverableVAT") == null)
		{
			return false;
		}

		return (Boolean) this.getSession().get("recoverableVAT");
	}

	private void clearSession()
	{
		this.getSession().put("naming1", null);
		this.getSession().put("naming2", null);
		this.getSession().put("naming3", null);
		this.getSession().put("ATECOcode1", null);
		this.getSession().put("ATECOcode2", null);
		this.getSession().put("ATECOcode3", null);
		this.getSession().put("ATECOcode4", null);
		this.getSession().put("recoverableVAT", null);
		this.getSession().put("fesrCnValue", null);
		this.getSession().put("firstAddressRegion", null);
		this.getSession().put("secondAddressRegion", null);
		this.getSession().put("firstAddressProvince", null);
		this.getSession().put("secondAddressProvince", null);
		this.getSession().put("firstAddressCity", null);
		this.getSession().put("secondAddressCity", null);
		this.getSession().put("firstAddressAddress", null);
		this.getSession().put("secondAddressAddress", null);
	}

	public void setLstATECOcode4(List<SelectItem> lstATECOcode4)
	{
		this.lstATECOcode4 = lstATECOcode4;
	}

	public List<SelectItem> getLstATECOcode4()
	{
		return lstATECOcode4;
	}

	public Boolean getIsCF()
	{
		return this.getSession().get("isCFAnagraph") != null;
	}

	public Boolean getIsPartner()
	{
		return this.getSession().get("isCFPartnerAnagraph") != null;
	}

	public void setTabCFPartnerAnagraphViewHeader(
			String tabCFPartnerAnagraphViewHeader)
	{
		this.tabCFPartnerAnagraphViewHeader = tabCFPartnerAnagraphViewHeader;
	}

	public String getTabCFPartnerAnagraphViewHeader()
	{
		if (this.getSession().get("isCFAnagraph") != null)
		{

			return Utils
					.getString("CFPartnerAnagraphCompletationsViewTabHeaderCF");
		}

		if (this.getSession().get("isCFPartnerAnagraph") != null)
		{
			return Utils
					.getString("CFPartnerAnagraphCompletationsViewTabHeaderPartner");
		}

		return "";
	}

	public String getPartnerCfReferent()
	{
		return partnerCfReferent;
	}

	public void setPartnerCfReferent(String partnerCfReferent)
	{
		this.partnerCfReferent = partnerCfReferent;
	}

	public String getPartnerCountry()
	{
		return partnerCountry;
	}

	public void setPartnerCountry(String partnerCountry)
	{
		this.partnerCountry = partnerCountry;
	}

	public String getPartnerDaec()
	{
		return partnerDaec;
	}

	public void setPartnerDaec(String partnerDaec)
	{
		this.partnerDaec = partnerDaec;
	}

	public String getPartnerEmailReferent()
	{
		return partnerEmailReferent;
	}

	public void setPartnerEmailReferent(String partnerEmailReferent)
	{
		this.partnerEmailReferent = partnerEmailReferent;
	}

	public String getPartnerFirstLevelController()
	{
		return partnerFirstLevelController;
	}

	public void setPartnerFirstLevelController(
			String partnerFirstLevelController)
	{
		this.partnerFirstLevelController = partnerFirstLevelController;
	}

	public String getPartnerName()
	{
		return partnerName;
	}

	public void setPartnerName(String partnerName)
	{
		this.partnerName = partnerName;
	}

	public String getPartnerPartita()
	{
		return partnerPartita;
	}

	public void setPartnerPartita(String partnerPartita)
	{
		this.partnerPartita = partnerPartita;
	}

	public String getPartnerReferentName()
	{
		return partnerReferentName;
	}

	public void setPartnerReferentName(String partnerReferentName)
	{
		this.partnerReferentName = partnerReferentName;
	}

	public String getPartnerReferentSurname()
	{
		return partnerReferentSurname;
	}

	public void setPartnerReferentSurname(String partnerReferentSurname)
	{
		this.partnerReferentSurname = partnerReferentSurname;
	}

	public void setIsPartner(Boolean isPartner)
	{
		this.getSession().put("isPartner", isPartner);
	}

	public void setIsCF(Boolean isCF)
	{
		this.getSession().put("isCF", isCF);
	}

	public void setPartnerNameTitle(String partnerNameTitle)
	{
		this.partnerNameTitle = partnerNameTitle;
	}

	public String getPartnerNameTitle()
	{
		return partnerNameTitle;
	}

	/**
	 * Sets view
	 * 
	 * @param view
	 *            the view to set
	 */
	public void setView(boolean view)
	{
		this.view = view;
	}

	/**
	 * Gets view
	 * 
	 * @return view the view
	 */
	public boolean getView()
	{
		return view;
	}

	public String getNaming1Title()
	{
		try
		{
			if (this.getNaming1() != null)
			{
				return BeansFactory.Naming().Load(this.getNaming1()).getValue();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getNaming2Title()
	{
		try
		{
			if (this.getNaming2() != null)
			{
				return BeansFactory.Naming().Load(this.getNaming2()).getValue();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getNaming3Title()
	{
		try
		{
			if (this.getNaming3() != null)
			{
				return BeansFactory.Naming().Load(this.getNaming3()).getValue();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getAteco1Title()
	{
		try
		{
			if(!this.getATECOcode1().equals(Utils.getString(nonApplicabile)))
			{
				return BeansFactory.Ateco().Load(this.getATECOcode1())
						.getValue();
			}
			else
				return Utils.getString(nonApplicabile);

		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getAteco2Title()
	{
		try
		{
			if(!this.getATECOcode2().equals(Utils.getString(nonApplicabile)))
			{
				return BeansFactory.Ateco().Load(this.getATECOcode2())
						.getValue();
			}
			else
				return Utils.getString(nonApplicabile);

		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getAteco3Title()
	{
		try
		{
			if (this.getATECOcode3() != null)
			{
				if(!this.getATECOcode3().equals(Utils.getString(nonApplicabile)))
				{
					return BeansFactory.Ateco().Load(this.getATECOcode3())
							.getValue();
				}
				else
					return Utils.getString(nonApplicabile);

			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getAteco4Title()
	{
		try
		{
			if (this.getATECOcode4() != null)
			{
				if(!this.getATECOcode4().equals(Utils.getString(nonApplicabile)))
				{
					return BeansFactory.Ateco().Load(this.getATECOcode4())
							.getValue();
				}
				else
					return Utils.getString(nonApplicabile);

			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getFirstAddressProvinceText()
	{
		try
		{
			if (this.getFirstAddressProvince() != null)
			{
				if (BeansFactory.Regions().Load(this.getFirstAddressRegion())
						.getIsForFrLoc())
				{
					return BeansFactory
							.Departments()
							.Load(Long.parseLong(this.getFirstAddressProvince()) - 1000)
							.getValue();
				}
				else
				{
					return BeansFactory.Provinces()
							.Load(this.getFirstAddressProvince()).getValue();
				}

			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getFirstAddressRegionText()
	{
		try
		{
			if (this.getFirstAddressRegion() != null)
			{
				return BeansFactory.Regions()
						.Load(this.getFirstAddressRegion()).getValue();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getFirstAddressCityText()
	{
		try
		{
			if (this.getFirstAddressCity() != null)
			{
				if (BeansFactory.Regions().Load(this.getFirstAddressRegion())
						.getIsForFrLoc())
				{
					return BeansFactory.Zones()
							.Load(this.getFirstAddressCity()).getValue();
				}
				else
				{
					return BeansFactory.Cities()
							.Load(this.getFirstAddressCity()).getValue();
				}

			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getSecondAddressProvinceText()
	{
		try
		{
			if (this.getSecondAddressProvince() != null)
			{
				if (BeansFactory.Regions().Load(this.getSecondAddressRegion())
						.getIsForFrLoc())
				{
					return BeansFactory
							.Departments()
							.Load(Long.parseLong(this
									.getSecondAddressProvince()) - 1000)
							.getValue();
				}
				else
				{
					return BeansFactory.Provinces()
							.Load(this.getSecondAddressProvince()).getValue();
				}

			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getSecondAddressRegionText()
	{
		try
		{
			if (this.getSecondAddressRegion() != null)
			{
				return BeansFactory.Regions()
						.Load(this.getSecondAddressRegion()).getValue();
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public String getSecondAddressCityText()
	{
		try
		{
			if (this.getSecondAddressCity() != null)
			{
				if (BeansFactory.Regions().Load(this.getSecondAddressRegion())
						.getIsForFrLoc())
				{
					return BeansFactory.Zones()
							.Load(this.getSecondAddressCity()).getValue();
				}
				else
				{
					return BeansFactory.Cities()
							.Load(this.getSecondAddressCity()).getValue();
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	public List<SelectItem> getListFesrCnValue() {
		return listFesrCnValue;
	}

	public void setListFesrCnValue(List<SelectItem> listFesrCnValue) {
		this.listFesrCnValue = listFesrCnValue;
	}

	public String getFesrCnValue() {
		//return fesrCnValue;

		if (this.getViewState().get("FesrCNValue") != null)
		{
			return String.valueOf(this.getViewState().get(
					"FesrCNValue"));
		}
		else
		{
			return null;
		}
	}

	public void setFesrCnValue(String fesrCnValue) {
		//this.fesrCnValue = fesrCnValue;
		this.getViewState().put("FesrCNValue", fesrCnValue);
	}
	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponentsCFP", arrayList);
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponentsCFP");
	}
}
