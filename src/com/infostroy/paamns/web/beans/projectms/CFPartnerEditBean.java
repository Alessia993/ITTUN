package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.CountryTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.enums.UserRoleTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.mail.InfoObject;
import com.infostroy.paamns.common.helpers.mail.NewProjectUserMailSender;
import com.infostroy.paamns.common.helpers.mail.NewUserMailSender;
import com.infostroy.paamns.common.helpers.mail.SimpleMailSender;
import com.infostroy.paamns.common.security.crypto.MD5;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphCompletations;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.persistence.beans.entities.domain.PartnersBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Phase;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CFPartnerAnagraphTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CostDefinitionPhases;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.wrappers.CFAnagraphWrapper;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * modified by Ingloba360
 * 
 */
public class CFPartnerEditBean extends EntityProjectEditBean<CFPartnerAnagraphs> {
	
	private List<SelectItem>			listCountries;

	private List<SelectItem>			listCIL;

	private List<SelectItem>			listDAEC;

	private boolean						isPartner;

	private String						daecLabel;

	private String						headingString;

	private String						innerHeadingString;

	private String						userRoleLabel;

	private List<SelectItem>			listPartners;

	private CFPartnerAnagraphProject	cfPartnerProject;

	private CFPartnerAnagraphs			partner;

	private boolean						editMode;

	private boolean						editActualMode;

	private String						aguMode;

	private String						autoCil;

	private String						nonAutoCil;

	private String						countryTitle;

	private List<InfoObject>			listNPUMS;

	private List<InfoObject>			listNUMS;

	private List<InfoObject>			mailToSend;

	private List<SelectItem>			listNamings;

	private String						naming;

	private boolean						disableNaming;

	@Override
	public void AfterSave()
	{
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();
			List<Mails> mails = new ArrayList<Mails>();
			NewProjectUserMailSender newProjectUserMailSender = new NewProjectUserMailSender(
					listNPUMS);
			mails.addAll(newProjectUserMailSender
					.completeMails(PersistenceSessionManager.getBean()
							.getSession()));
			NewUserMailSender newUserMailSender = new NewUserMailSender(
					listNUMS);
			mails.addAll(newUserMailSender
					.completeMails(PersistenceSessionManager.getBean()
							.getSession()));
			SimpleMailSender simpleMailSender = new SimpleMailSender(mailToSend);
			mails.addAll(simpleMailSender
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
		//
		// new NewProjectUserMailSender(listNPUMS).start();
		// new NewUserMailSender(listNUMS).start();
		// new SimpleMailSender(mailToSend).start();

		this.GoBack();
	}

	@Override
	public void GoBack()
	{
		this.goTo(PagesTypes.CFPARTNERLIST);
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		if (this.getSession().get("isCFpartner") != null)
		{
			this.isPartner = Boolean.valueOf(String.valueOf(this.getSession()
					.get("isCFpartner")));
			if (this.isPartner)
			{
				this.headingString = Utils.getString("Resources",
						"cfPartnerHeading2", null);
				this.innerHeadingString = Utils.getString("Resources",
						"cfPartnerEditPageName2", null);

				this.setUserRoleLabel(Utils.getString("Resources",
						"cfPartnerEditUserPartner", null));
			}
			else
			{
				this.headingString = Utils.getString("Resources",
						"cfPartnerHeading", null);
				this.innerHeadingString = Utils.getString("Resources",
						"cfPartnerEditPageName", null);

				this.setUserRoleLabel(Utils.getString("Resources",
						"cfPartnerEditUserCF", null));

				if (this.getProject().getAsse() == 5)
				{
					if (this.getSession().get("cfanagraph") == null)
					{
						this.getSession().put("cfanagraph", -1l);
					}
				}
			}
		}

		Long partnerId = (Long) this.getViewState().get("cfanagraph2");

		if (this.getSelectedPartner() != null
				&& this.getSelectedPartner().equals("-1")
				&& this.getProjectState().equals(ProjectState.Actual))
		{
			this.setEditActualMode(true);

			this.setEditActualMode(false);

			this.setReferentName("");
			this.setReferentSurname("");
			this.setCfReferent("");
			this.setEmailReferent("");
			this.setName("");
			this.setPartita("");
			this.setPublicSubject(false);
			this.setCountry("");
			this.setCupCode("");
			this.setEditMode(true);

			this.setEditMode(true);
			this.setAguMode("");
			this.setAutoCil("");

			if (this.getProjectId() != null && partnerId != null)
			{
				this.cfPartnerProject = BeansFactory.CFPartnerAnagraphProject()
						.LoadByPartner(Long.parseLong(this.getProjectId()),
								partnerId);

				this.setName(this.cfPartnerProject.getNaming());
				this.setNaming(this.cfPartnerProject.getNaming());
			}
			this.setDisableNaming(true);
		}
		else
		{
			if (this.getSession().get("cfanagraph") != null
					&& this.getSessionBean().getIsActualSate())
			{

				this.setEditActualMode(true);
			}
			else if (this.getSession().get("cfanagraph") == null
					&& this.getSessionBean().getIsActualSate())
			{
				this.setEditActualMode(false);
			}
			else
			{
				this.setEditActualMode(false);
			}

			if (this.getSession().get("cfanagraph") != null)
			{
				if (this.getSelectedPartner() != null
						&& !this.getSession().get("cfanagraph").toString()
								.equals("-1"))
				{
					if (partnerId == null)
					{
						this.getViewState().put("cfanagraph2",
								this.getSession().get("cfanagraph"));
					}

					this.getSession().put("cfanagraph",Long.parseLong(
							this.getSelectedPartner()));
				}

				CFPartnerAnagraphs entity = null;

				if (!Long.valueOf(
						String.valueOf(this.getSession().get("cfanagraph")))
						.equals(-1l))
				{
					entity = BeansFactory.CFPartnerAnagraphs()
							.Load(String.valueOf(this.getSession().get(
									"cfanagraph")));

					this.setEditMode(true);
					this.setAguMode("");
					this.setAutoCil("");
				}
				else
				{
					if (this.getSession().get("cfanagraph").equals(-1l))
					{
						entity = CFAnagraphWrapper.CreateAguWrapper();
						this.setEditMode(false);
						this.setAguMode("none");
						this.setAutoCil("");
					}
				}

				if (!Long.valueOf(
						String.valueOf(this.getSession().get("cfanagraph")))
						.equals(-1l))
				{
					this.cfPartnerProject = BeansFactory
							.CFPartnerAnagraphProject().LoadByPartner(
									Long.valueOf(this.getProjectId()),
									entity.getId());

					if (this.cfPartnerProject != null
							&& this.cfPartnerProject.getProject().getAsse() == 5
							&& !this.isPartner)
					{
						this.setEditMode(false);
						this.setAguMode("none");
						this.setAutoCil("none");
					}

					if (this.getSelectedPartner() == null)
					{
						this.setSelectedPartner(entity.getId() == null ? null
								: entity.getId().toString());
					}

					if (this.getSelectedPartner() != null
							&& !this.getSelectedPartner().equals(
									String.valueOf(SelectItemHelper
											.getVirtualEntity().getValue())))
					{
						if (this.getSelectedPartner() == null)
						{
							if (this.cfPartnerProject != null)
							{
								this.setSelectedPartner(String
										.valueOf(this.cfPartnerProject
												.getCfPartnerAnagraphs()
												.getId()));
							}

							entity = BeansFactory.CFPartnerAnagraphs().Load(
									this.getSelectedPartner());
						}

						if (this.getCountry() == null
								|| this.getCountry().equals(
										SelectItemHelper.getFirst().getValue()))
						{
							if (entity.getCountry() != null)
							{
								this.setCountry(String.valueOf(entity
										.getCountry().getId()));

								if (entity
										.getCountry()
										.getCode()
										.equals(CountryTypes.Italy.getCountry()))
								{
									this.setAutoCil("");
								}
								else
								{
									this.setAutoCil("none");
								}
							}
						}

						this.setPartita(entity.getPartita());
						this.fillNaming(entity);
						this.setName(entity.getNaming());
						this.setCupCode(entity.getCupCode());
						this.setPublicSubject(entity.getPublicSubject());
						if (this.getEditActualMode())
						{
							this.setCountry(String.valueOf(entity.getCountry()
									.getId()));
							if (entity.getCountry().getCode()
									.equals(CountryTypes.France.getCountry()))
							{
								this.setNonAutoCil("");
								this.setAutoCil("none");
							}
							else if (entity.getCountry().getCode()
									.equals(CountryTypes.Italy.getCountry()))
							{
								this.setNonAutoCil("none");
								this.setAutoCil("");
							}
						}
						if (this.cfPartnerProject != null)
						{

							this.naming = this.cfPartnerProject.getNaming();

							this.setName(this.cfPartnerProject.getNaming());

							// remove to update
							// this.setDisableNaming(true);
							if (this.cfPartnerProject.getCil() != null)
							{
								this.setFirstLevelController(String
										.valueOf(this.cfPartnerProject.getCil()
												.getId()));
							}
							else
							{
								if (this.getFirstLevelController() == null)
								{
									this.setFirstLevelController(String
											.valueOf(SelectItemHelper
													.getFirst().getValue()));
								}
							}

							if (this.cfPartnerProject.getDaec() != null)
							{
								this.setDaec(String
										.valueOf(this.cfPartnerProject
												.getDaec().getId()));
							}
							else
							{
								if (this.getDaec() == null)
								{
									this.setDaec(String
											.valueOf(SelectItemHelper
													.getFirst().getValue()));
								}
							}
						}
						else if (this.getEditActualMode())
						{
							CFPartnerAnagraphs entity2 = BeansFactory
									.CFPartnerAnagraphs().Load(partnerId);
							CFPartnerAnagraphProject cfPartnerProject2 = BeansFactory
									.CFPartnerAnagraphProject().LoadByPartner(
											Long.valueOf(this.getProjectId()),
											entity2.getId());
							this.naming = cfPartnerProject2.getNaming();
							this.setName(entity2.getName());
							if (cfPartnerProject2.getCil() != null)
							{
								this.setFirstLevelController(String
										.valueOf(cfPartnerProject2.getCil()
												.getId()));
							}
							else
							{
								if (this.getFirstLevelController() == null)
								{
									this.setFirstLevelController(String
											.valueOf(SelectItemHelper
													.getFirst().getValue()));
								}
							}

							if (cfPartnerProject2.getDaec() != null)
							{
								this.setDaec(String.valueOf(cfPartnerProject2
										.getDaec().getId()));
							}
							else
							{
								if (this.getDaec() == null)
								{
									this.setDaec(String
											.valueOf(SelectItemHelper
													.getFirst().getValue()));
								}
							}
						}

						if (entity.getUser() != null)
						{
							this.setReferentName(entity.getUser().getName());
							this.setReferentSurname(entity.getUser()
									.getSurname());
							this.setCfReferent(entity.getUser().getFiscalCode());
							this.setEmailReferent(entity.getUser().getMail());
						}
					}
					else
					{
						this.setCountry(String.valueOf(SelectItemHelper
								.getFirst().getValue()));
						this.setPartita(null);
						this.setName(null);
						this.setPublicSubject(null);
						this.setReferentName(null);
						this.setReferentSurname(null);
						this.setCfReferent(null);
						this.setEmailReferent(null);
					}
				}
				else if (entity != null)
				{
					if (entity.getCountry() != null)
					{
						this.setCountry(String.valueOf(entity.getCountry()
								.getId()));
						this.cfPartnerProject = new CFPartnerAnagraphProject();

						if (entity.getCountry().getCode()
								.equals(CountryTypes.Italy.getCountry()))
						{
							this.setAutoCil("");
						}
						else
						{
							this.setAutoCil("none");
						}

						this.fillNaming(entity);
						// this.naming = this.cfPartnerProject.getNaming();
						// this.setName(this.cfPartnerProject.getNaming());
					}
					this.setPartita(entity.getPartita());
					this.setName(entity.getName());
					this.setPublicSubject(entity.getPublicSubject());
					this.setCupCode(entity.getCupCode());
				}
			}
			else
			{
				this.cfPartnerProject = new CFPartnerAnagraphProject();

				this.setEditMode(true);
				this.setAguMode("");
				this.setAutoCil("");

				if (this.getSelectedPartner() != null
						&& !this.getSelectedPartner().equals(
								String.valueOf(SelectItemHelper
										.getVirtualEntity().getValue())))
				{
					CFPartnerAnagraphs partner = BeansFactory
							.CFPartnerAnagraphs().Load(
									this.getSelectedPartner());

					if ((this.getCountry() == null || this.getCountry().equals(
							SelectItemHelper.getFirst().getValue()))
							&& partner.getCountry() != null)
					{
						this.setCountry(String.valueOf(partner.getCountry()
								.getId()));

						if (partner.getCountry().getCode()
								.equals(CountryTypes.Italy.getCountry()))
						{
							this.setAutoCil("");
						}
						else
						{
							this.setAutoCil("none");
						}
					}
					this.setName(partner.getNaming());
					this.fillNaming(partner);
					this.setPartita(partner.getPartita());
					// this.setName("");
					// this.setNaming("");
					this.setPublicSubject(partner.getPublicSubject());
					this.setCupCode(partner.getCupCode());
					if (partner.getUser() != null)
					{
						this.setReferentName(partner.getUser().getName());
						this.setReferentSurname(partner.getUser().getSurname());
						this.setCfReferent(partner.getUser().getFiscalCode());
						this.setEmailReferent(partner.getUser().getMail());
					}
				}
				else
				{
					if (this.getCountry() == null
							|| this.getCountry().equals(
									String.valueOf(SelectItemHelper.getFirst()
											.getValue())))
					{
						this.setCountry(String.valueOf(SelectItemHelper
								.getFirst().getValue()));
					}
				}

				if (this.getFirstLevelController() == null)
				{
					this.setFirstLevelController(String
							.valueOf(SelectItemHelper.getFirst().getValue()));
				}

				if (this.getDaec() == null)
				{
					this.setDaec(String.valueOf(SelectItemHelper.getFirst()
							.getValue()));
				}
			}
		}

		this.getSession().put("selectedPartnercfedit", null);
		this.fillComboBoxes();
		this.SetLabel();
	}

	private void fillNaming(CFPartnerAnagraphs entity)
			throws HibernateException, PersistenceBeanException
	{
		if (this.listNamings == null)
		{
			this.listNamings = new ArrayList<SelectItem>();
		}
		if (entity != null)
		{
			this.listNamings.clear();
			this.listNamings.add(SelectItemHelper.getFirst());

			for (String naming : BeansFactory.CFPartnerAnagraphProject()
					.getNamingForCF(this.getProjectId(), entity.getId()))
			{
				if (naming != null)
				{
					this.listNamings.add(new SelectItem(naming, naming));
				}
			}
		}
		else if (entity == null && this.listNamings.size() == 0)
		{
			this.listNamings.add(SelectItemHelper.getFirst());
		}
	}

	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		CFPartnerAnagraphs cf = null;
		if (getSession().get("cfanagraph") != null)
		{
			cf = BeansFactory.CFPartnerAnagraphs().Load(
					(Long) getSession().get("cfanagraph"));
		}
		else
		{
			cf = new CFPartnerAnagraphs();
			cf.setDeleted(false);
		}
		setEntity(cf);

	}

	public CFPartnerAnagraphs getEntity()
	{
		this.entity = (CFPartnerAnagraphs) this.getViewState().get(
				"cfanagraphEntity");
		return this.entity;
	}

	public void setEntity(CFPartnerAnagraphs entity)
	{
		this.getViewState().put("cfanagraphEntity", entity);
		this.entity = entity;
	}

	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException
	{
		boolean reSendEmails = false;
		listNPUMS = new ArrayList<InfoObject>();
		listNUMS = new ArrayList<InfoObject>();
		if (this.getSelectedPartner() != null
				&& this.getSelectedPartner().equals("-1")
				&& this.getProjectState().equals(ProjectState.Actual)
				&& !getEntity().isNew())
		{
			CFPartnerAnagraphs entityToSave = new CFPartnerAnagraphs();
			fillNewPartnerAnagraph(entityToSave);
			BeansFactory.CFPartnerAnagraphs().SaveInTransaction(entityToSave);
			replacePartner(entityToSave);
			
			List<GiuridicalEngages> giuridicalEngages = BeansFactory
					.GiuridicalEngages().getByProjectAndUserPartner(
							getProjectId(), this.getEntity().getUser().getId());
			
			if(giuridicalEngages != null)
			{
				for(GiuridicalEngages ge : giuridicalEngages)
				{
					ge.setCreatePartner(entityToSave);
					BeansFactory.GiuridicalEngages().SaveInTransaction(ge);
				}
			}

			List<CostDefinitions> costDefinitions = BeansFactory
					.CostDefinitions().GetByPartner(getProjectId(),
							this.getEntity().getUser().getId());
			
			if(costDefinitions != null)
			{
				for(CostDefinitions cd : costDefinitions)
				{
					cd.setUser(entityToSave.getUser());
					BeansFactory.CostDefinitions().SaveInTransaction(cd);
				}
			}
		}
		else if (this.getSelectedPartner() != null
				&& this.getSelectedPartner().equals("-1")
				&& this.getProjectState().equals(ProjectState.Actual)
				&& getEntity().isNew())
		{
			CFPartnerAnagraphs entityToSave = getEntity();

			if (getEntity().isNew())
			{
				// to update
				fillNewPartnerAnagraph(entityToSave);
			}

			InfoObject info = new InfoObject();
			info.setMail(entityToSave.getUser().getMail());
			info.setName(entityToSave.getUser().getName());
			info.setSurname(entityToSave.getUser().getSurname());
			info.setPassword(entityToSave.getUser().getPassword());
			info.setLogin(entityToSave.getUser().getLogin());
			listNUMS.add(info);

			CFPartnerAnagraphProject cfPartnerAnagraphProject = null;

			if (getEntity().isNew())
			{
				cfPartnerAnagraphProject = new CFPartnerAnagraphProject();
			}
			else
			{
				cfPartnerAnagraphProject = BeansFactory
						.CFPartnerAnagraphProject().LoadByPartner(
								getProject().getId(), entityToSave.getId());
			}

			if (!this.getFirstLevelController().equals(
					SelectItemHelper.getFirst().getValue()))
			{
				cfPartnerAnagraphProject.setCil(BeansFactory
						.ControllerUserAnagraph().Load(
								this.getFirstLevelController()));
				this.setFirstLevelController(this.getFirstLevelController());
			}
			else if (!this.getFirstLevelControllerIt().equals(
					SelectItemHelper.getFirst().getValue()))
			{
				cfPartnerAnagraphProject.setCil(BeansFactory
						.ControllerUserAnagraph().Load(
								this.getFirstLevelControllerIt()));
				// this.setDaec(this.getFirstLevelControllerIt());
			}
			else
			{
				cfPartnerAnagraphProject.setCil(null);

			}

			if (!this.getDaec().equals(SelectItemHelper.getFirst().getValue())
					&& entityToSave.getCountry().getCode()
							.equals(CountryTypes.France.getCountry()))
			{
				cfPartnerAnagraphProject.setDaec(BeansFactory
						.ControllerUserAnagraph().Load(this.getDaec()));
			}
			else
			{
				cfPartnerAnagraphProject.setDaec(null);
				this.setDaec("");
			}

			// BeansFactory.CFPartnerAnagraphs().SaveInTransaction(entityToSave);

			cfPartnerAnagraphProject.setProject(this.getProject());

			if (getSessionBean().getProjectLock() && getSessionBean().isSTC())
			{
				cfPartnerAnagraphProject.setCfPartnerAnagraphs(entityToSave);

				cfPartnerAnagraphProject.setProgressiveId(BeansFactory
						.CFPartnerAnagraphProject()
						.GetMaxIdCFAnagraphForProject(this.getProjectId()));

				CFPartnerAnagraphTypes c = BeansFactory
						.CFPartnerAnagraphTypes().GetByType(
								CFAnagraphTypes.PartnerAnagraph);
				cfPartnerAnagraphProject.setType(c);
				cfPartnerAnagraphProject.setNaming(this.getName());
			}

			BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
					cfPartnerAnagraphProject);

			if (getSessionBean().getProjectLock() && getSessionBean().isSTC()
					&& getEntity().isNew())
			{
				Long maxVersion = BeansFactory.PartnersBudgets()
						.GetLastVersionNumber(this.getProjectId(),
								entityToSave.getId().toString(), null);

				if (maxVersion == null)
				{
					maxVersion = 0l;
				}

				PartnersBudgets pbf = new PartnersBudgets();
				pbf.setCfPartnerAnagraph(entityToSave);
				pbf.setProject(getSessionBean().getProject());
				pbf.setApproved(true);
				pbf.setTotalRow(false);
				pbf.setPhaseBudgets(new ArrayList<Phase>());
				for (CostDefinitionPhases cdPhase : BeansFactory.CostDefinitionPhase()
						.Load())
				{
					Phase phase = new Phase();
					phase.setPhase(cdPhase);
					phase.setPartAdvInfoProducts(0d);
					phase.setPartAdvInfoPublicEvents(0d);
					phase.setPartDurableGoods(0d);
					phase.setPartDurables(0d);
					phase.setPartHumanRes(0d);
					phase.setPartMissions(0d);
					phase.setPartOther(0d);
					phase.setPartGeneralCosts(0d);
					phase.setPartOverheads(0d);
					phase.setPartProvisionOfService(0d);
					
					phase.setPartContainer(0d);
					phase.setPartPersonalExpenses(0d);
					phase.setPartCommunicationAndInformation(0d);
					phase.setPartOrganizationOfCommitees(0d);
					phase.setPartOtherFees(0d);
					phase.setPartAutoCoordsTunis(0d);
					phase.setPartContactPoint(0d);
					phase.setPartSystemControlAndManagement(0d);
					phase.setPartAuditOfOperations(0d);
					phase.setPartAuthoritiesCertification(0d);
					phase.setPartIntermEvaluation(0d);
					
					pbf.getPhaseBudgets().add(phase);
				}
				pbf.setTotalAmount(0d);
				pbf.setBudgetRelease(0d);
				pbf.setCounter(0d);
				pbf.setQuotaPrivate(0d);
				pbf.setIsOld(false);
				pbf.setDenied(false);
				pbf.setVersion(maxVersion);

				BeansFactory.PartnersBudgets().SaveInTransaction(pbf);
			}

			Projects currentProject = BeansFactory.Projects().Load(
					String.valueOf(this.getSession().get("project")));

			ControllerUserAnagraph firstLevelController = null;

			if (!this.getFirstLevelController().equals(
					SelectItemHelper.getFirst().getValue()))
			{
				firstLevelController = BeansFactory.ControllerUserAnagraph()
						.Load(this.getFirstLevelController());
			}

			ControllerUserAnagraph daec = null;

			if (!this.getDaec().equals(SelectItemHelper.getFirst().getValue()))
			{
				daec = BeansFactory.ControllerUserAnagraph().Load(
						this.getDaec());
			}

			if (firstLevelController != null)
			{

				firstLevelController.getUser().setActive(true);
				BeansFactory.Users().SaveInTransaction(
						firstLevelController.getUser());

				info = new InfoObject();
				info.setMail(firstLevelController.getUser().getMail());
				info.setName(firstLevelController.getUser().getName());
				info.setSurname(firstLevelController.getUser().getSurname());
				info.setProjectName(currentProject.getTitle());
				listNPUMS.add(info);

			}

			if (daec != null)
			{
				daec.getUser().setActive(true);
				BeansFactory.Users().SaveInTransaction(daec.getUser());

				info = new InfoObject();

				info.setMail(daec.getUser().getMail());
				info.setName(daec.getUser().getName());
				info.setSurname(daec.getUser().getSurname());
				info.setProjectName(currentProject.getTitle());
				listNPUMS.add(info);
			}

			Long o = (Long) this.getViewState().get("cfanagraph2");

			if (o != null)
			{
				entity = BeansFactory.CFPartnerAnagraphs().Load(o);

				this.cfPartnerProject = BeansFactory.CFPartnerAnagraphProject()
						.LoadByPartner(this.getProject().getId(),
								entity.getId());

				cfPartnerAnagraphProject.setCfPartnerAnagraphs(entityToSave);

				cfPartnerAnagraphProject.setNaming(this.cfPartnerProject
						.getNaming());

				cfPartnerAnagraphProject.setProgressiveId(BeansFactory
						.CFPartnerAnagraphProject()
						.GetMaxIdCFAnagraphForProject(this.getProjectId()));

				cfPartnerAnagraphProject.setProject(this.cfPartnerProject
						.getProject());
				cfPartnerAnagraphProject.setType(this.cfPartnerProject
						.getType());

				BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
						cfPartnerAnagraphProject);

				this.cfPartnerProject.setRemovedFromProject(true);
				BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
						cfPartnerProject);

				// ---------------------------- refreshing data

				refreshDataOfPartner(entityToSave, cfPartnerAnagraphProject,
						cfPartnerProject);

				// -------------------------------------

				info = new InfoObject();
				info.setMail(entityToSave.getUser().getMail());
				info.setName(entityToSave.getUser().getName());
				info.setSurname(entityToSave.getUser().getSurname());
				info.setProjectName(this.getProject().getTitle());
				listNPUMS.add(info);
			}
		}
		else if (this.getEditActualMode()
				&& this.getViewState().get("cfanagraph2") != null
				&& this.cfPartnerProject == null)
		{
			CFPartnerAnagraphs entityToSave = BeansFactory.CFPartnerAnagraphs()
					.Load(String.valueOf(this.getSession().get("cfanagraph")));
			replacePartner(entityToSave);
		}
		else if (this.getEditActualMode()
				&& this.getViewState().get("cfanagraph2") != null
				&& this.cfPartnerProject != null
				&& this.cfPartnerProject.getRemovedFromProject())
		{
			CFPartnerAnagraphs entityToSave = BeansFactory.CFPartnerAnagraphs()
					.Load(String.valueOf(this.getSession().get("cfanagraph")));
			entity = BeansFactory.CFPartnerAnagraphs().Load(
					String.valueOf(this.getViewState().get("cfanagraph2")));
			CFPartnerAnagraphProject cfPartnerAnagraphProject = BeansFactory
					.CFPartnerAnagraphProject().LoadByPartner(
							this.getProject().getId(), entity.getId());

			cfPartnerAnagraphProject.setRemovedFromProject(true);
			if (!this.getFirstLevelController().equals(
					SelectItemHelper.getFirst().getValue()))
			{
				this.cfPartnerProject.setCil(BeansFactory
						.ControllerUserAnagraph().Load(
								this.getFirstLevelController()));
			}
			else
			{
				this.cfPartnerProject.setCil(null);
			}

			if (!this.getDaec().equals(SelectItemHelper.getFirst().getValue())
					&& entityToSave.getCountry().getCode()
							.equals(CountryTypes.France.getCountry()))
			{
				this.cfPartnerProject.setDaec(BeansFactory
						.ControllerUserAnagraph().Load(this.getDaec()));
			}
			else
			{
				this.cfPartnerProject.setDaec(null);
				this.setDaec("");
			}

			BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
					cfPartnerAnagraphProject);

			this.cfPartnerProject.setRemovedFromProject(false);
			BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
					cfPartnerProject);

			// ---------------------------- refreshing data

			refreshDataOfPartner(entityToSave, this.cfPartnerProject,
					cfPartnerAnagraphProject);

			// ----------------------------

			InfoObject info = new InfoObject();
			info.setMail(entityToSave.getUser().getMail());
			info.setName(entityToSave.getUser().getName());
			info.setSurname(entityToSave.getUser().getSurname());
			info.setProjectName(this.getProject().getTitle());
			listNPUMS.add(info);
		}
		else
		{
			if (this.getSession().get("cfanagraph") != null
					&& !Long.valueOf(
							String.valueOf(this.getSession().get("cfanagraph")))
							.equals(-1l))
			{
				CFPartnerAnagraphs entityToSave = BeansFactory
						.CFPartnerAnagraphs().Load(
								String.valueOf(this.getSession().get(
										"cfanagraph")));

				if (this.getViewState().get("cfanagraph2") != null)
				{
					this.cfPartnerProject = BeansFactory
							.CFPartnerAnagraphProject()
							.LoadByPartner(
									this.getProject().getId(),
									Long.valueOf(String.valueOf(this
											.getViewState().get("cfanagraph2"))));
				}

				// remove to update
				entityToSave.setName(this.getName());
				// entityToSave.setName(this.getNaming().isEmpty()?this.getName():this.getNaming());
				entityToSave.setCountry(BeansFactory.Countries().Load(
						this.getCountry()));
				entityToSave.setPartita(this.getPartita());
				entityToSave.setPublicSubject(this.getPublicSubject());
				entityToSave.setCupCode(this.getCupCode());

				boolean cilChanged = true;
				if (this.cfPartnerProject.getCil() != null)
				{
					cilChanged = !this.getFirstLevelController().equals(
							this.cfPartnerProject.getCil().getId().toString());
				}
				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue())
						&& entityToSave.getCountry().getCode()
								.equals(CountryTypes.France.getCountry()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController()));
				}
				else if (!this.getFirstLevelControllerIt().equals(
						SelectItemHelper.getFirst().getValue())
						&& entityToSave.getCountry().getCode()
								.equals(CountryTypes.Italy.getCountry()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelControllerIt()));

					// this.setDaec(this.getFirstLevelControllerIt());
				}
				else
				{
					this.cfPartnerProject.setCil(null);

					if (entityToSave.getCountry().getCode()
							.equals(CountryTypes.Italy.getCountry()))
					{
						this.cfPartnerProject.setDaec(null);
						this.setDaec(String.valueOf(SelectItemHelper.getFirst()
								.getValue()));
					}
				}

				boolean daecChanged = true;
				if (this.cfPartnerProject.getDaec() != null)
				{
					daecChanged = !this.getDaec().equals(
							this.cfPartnerProject.getDaec().getId().toString());
				}

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setDaec(BeansFactory
							.ControllerUserAnagraph().Load(this.getDaec()));
				}
				else
				{
					this.cfPartnerProject.setDaec(null);
				}

				this.cfPartnerProject.setCfPartnerAnagraphs(entityToSave);

				// remove to update
				this.cfPartnerProject.setNaming(this.getName());
				// this.cfPartnerProject.setNaming(this.getNaming().isEmpty()?this.getName():this.getNaming());

				// Modify user

				if (entityToSave.getUser() != null)
				{
					Users user = entityToSave.getUser();
					user.setName(this.getReferentName());
					user.setSurname(this.getReferentSurname());
					if (user.getMail() != null
							&& !user.getMail().equalsIgnoreCase(
									this.getEmailReferent()))
					{
						reSendEmails = true;
					}

					user.setMail(this.getEmailReferent());
					user.setFiscalCode(this.getCfReferent());
					user.setActive(true);

					BeansFactory.Users().SaveInTransaction(user);

					entityToSave.setUser(user);

					if (reSendEmails)
					{
						mailToSend = new ArrayList<InfoObject>();
						List<Mails> listMails = BeansFactory.Mails().Load(user);
						for (Mails mail : listMails)
						{
							mailToSend.add(new InfoObject(user.getMail(), user
									.getName(), user.getSurname(), mail
									.getMessage()));
							BeansFactory.Mails().DeleteInTransaction(mail);
						}
					}
				}

				BeansFactory.CFPartnerAnagraphs().SaveInTransaction(
						entityToSave);
				BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
						this.cfPartnerProject);

				Projects currentProject = BeansFactory.Projects().Load(
						String.valueOf(this.getSession().get("project")));

				ControllerUserAnagraph firstLevelController = null;

				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					firstLevelController = BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController());
				}

				ControllerUserAnagraph daec = null;

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					daec = BeansFactory.ControllerUserAnagraph().Load(
							this.getDaec());
				}

				if (firstLevelController != null && cilChanged)
				{

					firstLevelController.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(
							firstLevelController.getUser());

					InfoObject info = new InfoObject();
					info.setMail(firstLevelController.getUser().getMail());
					info.setName(firstLevelController.getUser().getName());
					info.setSurname(firstLevelController.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);
				}

				if (daec != null && daecChanged)
				{
					daec.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(daec.getUser());

					InfoObject info = new InfoObject();
					info.setMail(daec.getUser().getMail());
					info.setName(daec.getUser().getName());
					info.setSurname(daec.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);
				}
			}
			else if (this.getSession().get("cfanagraph") != null
					&& Long.valueOf(
							String.valueOf(this.getSession().get("cfanagraph")))
							.equals(-1l))
			{
				CFPartnerAnagraphs entityToSave = new CFPartnerAnagraphs();

				entityToSave.setName(this.getName());
				entityToSave.setCountry(BeansFactory.Countries().Load(
						this.getCountry()));
				entityToSave.setPartita(this.getPartita());
				entityToSave.setPublicSubject(this.getPublicSubject());
				entityToSave.setCupCode(this.getCupCode());
				this.cfPartnerProject.setType(BeansFactory
						.CFPartnerAnagraphTypes().Load(
								CFAnagraphTypes.CFAnagraph.getCfType()));
				entityToSave.setDeleted(false);
				entityToSave.setCreateDate(DateTime.getNow());

				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController()));
					this.setDaec(this.getFirstLevelController());
				}
				else if (!this.getFirstLevelControllerIt().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelControllerIt()));
					// this.setDaec(this.getFirstLevelControllerIt());
				}
				else
				{
					this.cfPartnerProject.setCil(null);

				}

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setDaec(BeansFactory
							.ControllerUserAnagraph().Load(this.getDaec()));
				}
				else
				{
					this.cfPartnerProject.setDaec(null);
				}

				BeansFactory.CFPartnerAnagraphs().SaveInTransaction(
						entityToSave);
				this.cfPartnerProject.setCfPartnerAnagraphs(entityToSave);
				this.cfPartnerProject.setProject(this.getProject());
				this.cfPartnerProject.setNaming(this.getName());
				BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
						this.cfPartnerProject);

				Projects currentProject = BeansFactory.Projects().Load(
						String.valueOf(this.getSession().get("project")));

				ControllerUserAnagraph firstLevelController = null;

				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					firstLevelController = BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController());
				}

				ControllerUserAnagraph daec = null;

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					daec = BeansFactory.ControllerUserAnagraph().Load(
							this.getDaec());
				}

				if (firstLevelController != null)
				{

					firstLevelController.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(
							firstLevelController.getUser());

					InfoObject info = new InfoObject();
					info.setMail(firstLevelController.getUser().getMail());
					info.setName(firstLevelController.getUser().getName());
					info.setSurname(firstLevelController.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);

				}

				if (daec != null)
				{
					daec.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(daec.getUser());

					InfoObject info = new InfoObject();

					info.setMail(daec.getUser().getMail());
					info.setName(daec.getUser().getName());
					info.setSurname(daec.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);
				}
			}
			else
			{
				CFPartnerAnagraphs entityToSave = null;

				if (this.getSelectedPartner() == null
						|| this.getSelectedPartner().equals(
								String.valueOf(SelectItemHelper
										.getVirtualEntity().getValue())))
				{
					entityToSave = new CFPartnerAnagraphs();
				}
				else
				{
					entityToSave = BeansFactory.CFPartnerAnagraphs().Load(
							this.getSelectedPartner());
				}

				entityToSave.setCountry(BeansFactory.Countries().Load(
						this.getCountry()));
				entityToSave.setPartita(this.getPartita());
				entityToSave.setPublicSubject(this.getPublicSubject());
				entityToSave.setCupCode(this.getCupCode());

				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController()));
				}
				else if (!this.getFirstLevelControllerIt().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setCil(BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelControllerIt()));
					// this.setDaec(this.getFirstLevelControllerIt());
				}
				else
				{
					this.cfPartnerProject.setCil(null);
				}

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					this.cfPartnerProject.setDaec(BeansFactory
							.ControllerUserAnagraph().Load(this.getDaec()));
				}
				else
				{
					this.cfPartnerProject.setDaec(null);
				}

				entityToSave.setName(this.getName());
				this.cfPartnerProject.setProject(BeansFactory.Projects().Load(
						String.valueOf(this.getSession().get("project"))));
				this.cfPartnerProject.setNaming(this.getName());
				if (Boolean.valueOf(String.valueOf(this.getSession().get(
						"isCFpartner"))))
				{
					this.cfPartnerProject.setType(BeansFactory
							.CFPartnerAnagraphTypes().GetByType(
									CFAnagraphTypes.PartnerAnagraph));
					if (cfPartnerProject.isNew())
					{
						if (this.getProjectAsse() == 5)
						{
							long id = BeansFactory.CFPartnerAnagraphProject()
									.GetMaxIdCFAnagraphForProject(
											this.getProjectId());
							cfPartnerProject.setProgressiveId(id == 1 ? id + 1
									: id);
						}
						else
						{
							cfPartnerProject.setProgressiveId(BeansFactory
									.CFPartnerAnagraphProject()
									.GetMaxIdCFAnagraphForProject(
											this.getProjectId()));
						}
					}
				}
				else
				{
					this.cfPartnerProject.setType(BeansFactory
							.CFPartnerAnagraphTypes().GetByType(
									CFAnagraphTypes.CFAnagraph));
					if (cfPartnerProject.isNew())
					{
						cfPartnerProject.setProgressiveId(BeansFactory
								.CFPartnerAnagraphProject()
								.GetMaxIdCFAnagraphForProject(
										this.getProjectId()));
					}
				}

				Projects currentProject = BeansFactory.Projects().Load(
						String.valueOf(this.getSession().get("project")));

				Users newUser = null;

				if (this.getSelectedPartner() == null
						|| this.getSelectedPartner().equals(
								String.valueOf(SelectItemHelper
										.getVirtualEntity().getValue())))
				{
					newUser = new Users();
					newUser.setAdmin(false);
					newUser.setFiscalCode(this.getCfReferent());
					newUser.setLogin(this.BuildLogin());
					newUser.setName(this.getReferentName());
					newUser.setSurname(this.getReferentSurname());
					newUser.setMail(this.getEmailReferent());
					newUser.setActive(true);
					newUser.setShouldChangePassword(true);
					String pwd = this.BuildPassword();
					newUser.setPassword(MD5.encodeString(pwd, null));
					newUser.setPassword2(MD5.encodeString(newUser.getPassword(), null));
					BeansFactory.Users().SaveInTransaction(newUser);

					if (this.isPartner)
					{
						UserRoles ur = new UserRoles(newUser, BeansFactory
								.Roles().GetRoleByName(UserRoleTypes.B), true);
						BeansFactory.UserRoles().SaveInTransaction(ur);
					}
					else
					{
						UserRoles ur = new UserRoles(newUser, BeansFactory
								.Roles().GetRoleByName(UserRoleTypes.BP), true);
						BeansFactory.UserRoles().SaveInTransaction(ur);
					}

					entityToSave.setUser(newUser);

					InfoObject info = new InfoObject();
					info.setMail(newUser.getMail());
					info.setName(newUser.getName());
					info.setSurname(newUser.getSurname());
					info.setPassword(pwd);
					info.setLogin(newUser.getLogin());
					listNUMS.add(info);

					// this.SendMail(newUser, pwd);
				}
				else
				{
					newUser = entityToSave.getUser();
					newUser.setFiscalCode(this.getCfReferent());
					newUser.setName(this.getReferentName());
					newUser.setSurname(this.getReferentSurname());
					if (newUser.getMail() != null
							&& !newUser.getMail().equalsIgnoreCase(
									this.getEmailReferent()))
					{
						reSendEmails = true;
					}

					newUser.setMail(this.getEmailReferent());
					BeansFactory.Users().SaveInTransaction(newUser);

					List<UserRoles> roles = UserRolesBean.GetByUser(newUser
							.getId().toString());

					if (Boolean.valueOf(String.valueOf(this.getSession().get(
							"isCFpartner"))))
					{
						if (!this.Contains(roles, BeansFactory.Roles()
								.GetRoleByName(UserRoleTypes.B)))
						{
							UserRoles ur = new UserRoles(newUser, BeansFactory
									.Roles().GetRoleByName(UserRoleTypes.B),
									true);
							BeansFactory.UserRoles().SaveInTransaction(ur);
						}
					}
					else
					{
						if (!this.Contains(roles, BeansFactory.Roles()
								.GetRoleByName(UserRoleTypes.BP)))
						{
							UserRoles ur = new UserRoles(newUser, BeansFactory
									.Roles().GetRoleByName(UserRoleTypes.BP),
									true);
							BeansFactory.UserRoles().SaveInTransaction(ur);
						}
					}

					entityToSave.setUser(newUser);

					if (reSendEmails)
					{
						mailToSend = new ArrayList<InfoObject>();
						List<Mails> listMails = BeansFactory.Mails().Load(
								newUser);
						for (Mails mail : listMails)
						{
							mailToSend.add(new InfoObject(newUser.getMail(),
									newUser.getName(), newUser.getSurname(),
									mail.getMessage()));
							BeansFactory.Mails().DeleteInTransaction(mail);
						}
					}
				}

				// New user assigned for the project
				InfoObject info = new InfoObject();
				info.setMail(newUser.getMail());
				info.setName(newUser.getName());
				info.setSurname(newUser.getSurname());
				info.setProjectName(currentProject.getTitle());
				listNPUMS.add(info);

				BeansFactory.CFPartnerAnagraphs().SaveInTransaction(
						entityToSave);
				this.cfPartnerProject.setCfPartnerAnagraphs(entityToSave);
				BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
						this.cfPartnerProject);

				// Sending mails

				ControllerUserAnagraph firstLevelController = null;

				if (!this.getFirstLevelController().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					firstLevelController = BeansFactory
							.ControllerUserAnagraph().Load(
									this.getFirstLevelController());
				}

				if (firstLevelController != null)
				{
					firstLevelController.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(
							firstLevelController.getUser());

					// Send mail to I level controller
					info = new InfoObject();
					info.setMail(firstLevelController.getUser().getMail());
					info.setName(firstLevelController.getUser().getName());
					info.setSurname(firstLevelController.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);

				}

				ControllerUserAnagraph daec = null;

				if (!this.getDaec().equals(
						SelectItemHelper.getFirst().getValue()))
				{
					daec = BeansFactory.ControllerUserAnagraph().Load(
							this.getDaec());
				}

				if (daec != null)
				{
					daec.getUser().setActive(true);
					BeansFactory.Users().SaveInTransaction(daec.getUser());

					// Send mail to DAEC

					info = new InfoObject();
					info.setMail(daec.getUser().getMail());
					info.setName(daec.getUser().getName());
					info.setSurname(daec.getUser().getSurname());
					info.setProjectName(currentProject.getTitle());
					listNPUMS.add(info);

				}
			}
		}
	}

	/**
	 * @param entityToSave
	 * @throws PersistenceBeanException
	 */
	private void fillNewPartnerAnagraph(CFPartnerAnagraphs entityToSave)
			throws PersistenceBeanException
	{
		entityToSave.setName(this.getName());
		entityToSave.setCountry(BeansFactory.Countries()
				.Load(this.getCountry()));
		entityToSave.setPartita(this.getPartita());
		entityToSave.setPublicSubject(this.getPublicSubject());

		Users newUser = createNewUser();
		entityToSave.setUser(newUser);
	}

	/**
	 * @param entityToSave
	 * @throws PersistenceBeanException
	 */
	private void replacePartner(CFPartnerAnagraphs entityToSave)
			throws PersistenceBeanException
	{
		entity = BeansFactory.CFPartnerAnagraphs().Load(
				String.valueOf(this.getViewState().get("cfanagraph2")));
		this.cfPartnerProject = BeansFactory.CFPartnerAnagraphProject()
				.LoadByPartner(this.getProject().getId(), entity.getId());
		CFPartnerAnagraphProject cfPartnerAnagraphProject = new CFPartnerAnagraphProject();
		cfPartnerAnagraphProject.setCfPartnerAnagraphs(entityToSave);
		if (!this.getFirstLevelController().equals(
				SelectItemHelper.getFirst().getValue()))
		{
			cfPartnerAnagraphProject.setCil(BeansFactory
					.ControllerUserAnagraph().Load(
							this.getFirstLevelController()));
		}
		else
		{
			cfPartnerAnagraphProject.setCil(null);
		}

		if (!this.getDaec().equals(SelectItemHelper.getFirst().getValue())
				&& entityToSave.getCountry().getCode()
						.equals(CountryTypes.France.getCountry()))
		{
			cfPartnerAnagraphProject.setDaec(BeansFactory
					.ControllerUserAnagraph().Load(this.getDaec()));
		}
		else
		{
			cfPartnerAnagraphProject.setDaec(null);
			this.setDaec("");
		}

		cfPartnerAnagraphProject.setNaming(this.cfPartnerProject.getNaming());
		// cfPartnerAnagraphProject.setProgressiveId(BeansFactory
		// .CFPartnerAnagraphProject().GetMaxIdCFAnagraphForProject(
		// this.getProjectId()));
		cfPartnerAnagraphProject.setProgressiveId(this.cfPartnerProject
				.getProgressiveId());
		cfPartnerAnagraphProject.setProject(this.cfPartnerProject.getProject());
		cfPartnerAnagraphProject.setType(this.cfPartnerProject.getType());

		BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
				cfPartnerAnagraphProject);

		this.cfPartnerProject.setRemovedFromProject(true);
		BeansFactory.CFPartnerAnagraphProject().SaveInTransaction(
				cfPartnerProject);

		// ---------------------------- refreshing data

		refreshDataOfPartner(entityToSave, cfPartnerAnagraphProject,
				cfPartnerProject);

		// ----------------------------------

		InfoObject info = new InfoObject();
		info.setMail(entityToSave.getUser().getMail());
		info.setName(entityToSave.getUser().getName());
		info.setSurname(entityToSave.getUser().getSurname());
		info.setProjectName(this.getProject().getTitle());
		listNPUMS.add(info);
	}

	/**
	 * @return
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	private Users createNewUser() throws HibernateException,
			PersistenceBeanException
	{
		Users newUser = null;
		newUser = new Users();
		newUser.setAdmin(false);
		newUser.setFiscalCode(this.getCfReferent());
		newUser.setLogin(this.BuildLogin());
		newUser.setName(this.getReferentName());
		newUser.setSurname(this.getReferentSurname());
		newUser.setMail(this.getEmailReferent());
		newUser.setActive(true);
		newUser.setShouldChangePassword(true);
		String pwd = this.BuildPassword();
		newUser.setPassword(MD5.encodeString(pwd, null));
		newUser.setPassword2(MD5.encodeString(newUser.getPassword(), null));
		BeansFactory.Users().SaveInTransaction(newUser);

		if (this.isPartner)
		{
			UserRoles ur = new UserRoles(newUser, BeansFactory.Roles()
					.GetRoleByName(UserRoleTypes.B), true);
			BeansFactory.UserRoles().SaveInTransaction(ur);
		}
		else
		{
			UserRoles ur = new UserRoles(newUser, BeansFactory.Roles()
					.GetRoleByName(UserRoleTypes.BP), true);
			BeansFactory.UserRoles().SaveInTransaction(ur);
		}
		return newUser;
	}

	/**
	 * @param entityToSave
	 * @param cfPartnerAnagraphProject
	 * @throws PersistenceBeanException
	 */
	private void refreshDataOfPartner(CFPartnerAnagraphs entityToSave,
			CFPartnerAnagraphProject toSavecfPartnerAnagraphProject,
			CFPartnerAnagraphProject cfPartnerAnagraphProject)
			throws PersistenceBeanException
	{
		List<PartnersBudgets> listBudgets = BeansFactory.PartnersBudgets()
				.GetByPartner(this.getProjectId(), entity.getId().toString(),
						null);
		for (PartnersBudgets item : listBudgets)
		{
			item.setCfPartnerAnagraph(entityToSave);
			BeansFactory.PartnersBudgets().SaveInTransaction(item);
		}

		List<GeneralBudgets> listGBudgets = BeansFactory.GeneralBudgets()
				.GetActualItemsForCFAnagraph(this.getProjectId(),
						entity.getId());
		for (GeneralBudgets item : listGBudgets)
		{
			item.setCfPartnerAnagraph(entityToSave);
			BeansFactory.GeneralBudgets().SaveInTransaction(item);
		}

		List<CostDefinitions> listCostDefinitions = BeansFactory
				.CostDefinitions().GetByBudgetOwner(this.getProjectId(),
						entity.getUser().getId());
		for (CostDefinitions item : listCostDefinitions)
		{
			item.setBudgetOwner(entityToSave.getUser());
			BeansFactory.CostDefinitions().SaveInTransaction(item);
		}

		CFPartnerAnagraphCompletations completation = BeansFactory
				.CFPartnerAnagraphCompletations().GetByCFAnagraph(
						cfPartnerAnagraphProject.getId(), this.getProjectId());
		if (completation != null)
		{
			completation
					.setCfPartnerAnagraphproject(toSavecfPartnerAnagraphProject);
			BeansFactory.CFPartnerAnagraphCompletations().SaveInTransaction(
					completation);
		}
	}

	private boolean Contains(List<UserRoles> list, Roles item)
	{
		for (UserRoles ur : list)
		{
			if (ur.getRole().getId().equals(item.getId()))
			{
				return true;
			}
		}

		return false;
	}

	private void SetLabel() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		// Check country and set label near daec user
		if (this.getViewState().get("cfanagraphcountry") != null
				&& !String
						.valueOf(this.getViewState().get("cfanagraphcountry"))
						.equals(String.valueOf(SelectItemHelper.getFirst()
								.getValue())))
		{
			Countries country = BeansFactory.Countries()
					.Load(this.getCountry());

			if (country.getCode().equals(CountryTypes.Italy.getCountry()))
			{
				this.daecLabel = Utils.getString("Resources",
						"cfPartnerListTableValidator", null);
			}
			else if (country.getCode().equals(CountryTypes.France.getCountry()))
			{
				this.daecLabel = Utils.getString("Resources",
						"cfPartnerListTableDAEC", null);
			}
			else
			{
				this.daecLabel = Utils.getString("Resources",
						"cfPartnerListTableDAEC", null);
			}
		}
		else
		{
			if (this.getSession().get("cfanagraph") != null)
			{
				CFPartnerAnagraphs entity = BeansFactory.CFPartnerAnagraphs()
						.Load(String.valueOf(this.getSession()
								.get("cfanagraph")));
				if (entity.getCountry().getCode()
						.equals(CountryTypes.Italy.getCountry()))
				{
					this.daecLabel = Utils.getString("Resources",
							"cfPartnerListTableValidator", null);
				}
				else if (entity.getCountry().getCode()
						.equals(CountryTypes.France.getCountry()))
				{
					this.daecLabel = Utils.getString("Resources",
							"cfPartnerListTableDAEC", null);
				}
				else
				{
					this.daecLabel = Utils.getString("Resources",
							"cfPartnerListTableDAEC", null);
				}
			}
			else
			{
				this.daecLabel = Utils.getString("Resources",
						"cfPartnerListTableDAEC", null);
			}
		}
	}

	/**
	 * Fills combo boxes with data
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	private void fillComboBoxes() throws HibernateException,
			PersistenceBeanException
	{
		this.fillPartners();
		this.FillCountries();
		this.FillFirstLevelControllerComboBox();
		this.FillDaecComboBox();
		this.fillNaming(null);
	}

	private void fillPartners() throws PersistenceBeanException
	{
		List<CFPartnerAnagraphs> listAnagraphs = null;
		this.listPartners = new ArrayList<SelectItem>();
		boolean filterOnNaming = this.getProjectState().equals(
				ProjectState.Actual);
		if (Boolean.TRUE.equals(getProject().getLocked())
				&& getEntity().isNew())
		{
			filterOnNaming = false;
		}
		// if (!filterOnNaming)
		{
			// this.listPartners.add(SelectItemHelper.getFirst());
			this.listPartners.add(SelectItemHelper.getVirtualEntity());
		}
		/*
		 * if (!this.isPartner) { listAnagraphs =
		 * BeansFactory.CFPartnerAnagraphs().LoadByType(
		 * CFAnagraphTypes.CFAnagraph); } else { listAnagraphs =
		 * BeansFactory.CFPartnerAnagraphs().LoadByType(
		 * CFAnagraphTypes.PartnerAnagraph); }
		 */

		listAnagraphs = BeansFactory.CFPartnerAnagraphs()
				.LoadAllSortedByUsername();

		List<CFPartnerAnagraphs> listExclude = BeansFactory
				.CFPartnerAnagraphs().LoadByProject(this.getProjectId());

		List<Long> listExcludeIndices = new ArrayList<Long>();

		for (CFPartnerAnagraphs cfpa : listExclude)
		{
			listExcludeIndices.add(cfpa.getId());
		}

		Long editId = null;

		if (this.getViewState().get("cfanagraph2") != null
				|| this.getSession().get("cfanagraph") != null)
		{
			editId = Long.valueOf(String.valueOf(this.getViewState().get(
					"cfanagraph2") != null ? this.getViewState().get(
					"cfanagraph2") : this.getSession().get("cfanagraph")));
		}

		for (CFPartnerAnagraphs partner : listAnagraphs)
		{
			if (((!listExcludeIndices.contains(partner.getId()) || (filterOnNaming
					&& (partner.getId().equals(editId)) || checkRemoved(partner)))
					|| partner.getId().equals(editId) || (this.cfPartnerProject == null
					&& filterOnNaming && partner.getId().equals(
					Long.valueOf(String.valueOf(this.getViewState().get(
							"cfanagraph2"))))))
					&& partner.getUser() != null
					&& (!filterOnNaming || (checkNamings(partner)))
					&& !Boolean.TRUE.equals(partner.getUser().getFictive()))
			{
				StringBuilder sb = new StringBuilder();

				sb.append(partner.getUser().getName());
				sb.append(" - ");
				sb.append(partner.getUser().getSurname());

				SelectItem item = new SelectItem();
				item.setLabel(sb.toString());
				item.setValue(String.valueOf(partner.getId()));

				this.listPartners.add(item);
			}
		}
	}

	private boolean checkRemoved(CFPartnerAnagraphs partner)
			throws NumberFormatException, PersistenceBeanException
	{
		CFPartnerAnagraphProject pp = BeansFactory.CFPartnerAnagraphProject()
				.LoadByPartner(this.getProjectId(), partner.getId().toString());
		return pp.getRemovedFromProject();
	}

	private boolean checkNamings(CFPartnerAnagraphs partner)
			throws HibernateException, PersistenceBeanException
	{
		String naming = "";
		if (this.cfPartnerProject == null)
		{
			CFPartnerAnagraphs entity = BeansFactory.CFPartnerAnagraphs().Load(
					String.valueOf(this.getViewState().get("cfanagraph2")));
			naming = BeansFactory
					.CFPartnerAnagraphProject()
					.LoadByPartner(Long.valueOf(this.getProjectId()),
							entity.getId()).getNaming();

		}
		else
		{
			naming = this.cfPartnerProject.getNaming();
		}

		for (String item : BeansFactory.CFPartnerAnagraphProject()
				.getNamingForCF(null, partner.getId()))
		{
			if (item != null && item.equalsIgnoreCase(naming))
			{
				return true;
			}
		}

		if (partner.getName() == null)
		{
			return false;
		}
		else
		{
			return partner.getName().equalsIgnoreCase(naming);
		}
	}

	/**
	 * Fills coutries combo box
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 */
	private void FillCountries() throws HibernateException,
			PersistenceBeanException
	{
		this.listCountries = new ArrayList<SelectItem>();
		listCountries.add(SelectItemHelper.getFirst());

		List<Countries> listCountries = new ArrayList<Countries>();

		try
		{
			listCountries = BeansFactory.Countries().Load();
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		for (Countries country : listCountries)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(country.getId()));
			item.setLabel(country.getValue());
			this.listCountries.add(item);
		}
	}

	private void FillFirstLevelControllerComboBox()
	{
		this.listCIL = new ArrayList<SelectItem>();
		this.listCIL.add(SelectItemHelper.getFirst());
		List<ControllerUserAnagraph> listCILs = new ArrayList<ControllerUserAnagraph>();

		try
		{
			listCILs = BeansFactory.ControllerUserAnagraph().GetUsersByRole(
					UserRoleTypes.CIL, null);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		for (ControllerUserAnagraph cil : listCILs)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(cil.getId()));
			item.setLabel(cil.getUser().getName() + " "
					+ cil.getUser().getSurname());
			this.listCIL.add(item);
		}
	}

	/**
	 * Fills daec combo box
	 * 
	 * @throws PersistenceBeanException
	 */
	private void FillDaecComboBox()
	{
		this.listDAEC = new ArrayList<SelectItem>();
		this.listDAEC.add(SelectItemHelper.getFirst());
		List<ControllerUserAnagraph> listDAECs = new ArrayList<ControllerUserAnagraph>();

		try
		{
			if (this.getCountry() != null
					&& !this.getCountry().equals(
							SelectItemHelper.getFirst().getValue()))
			{
				Countries country = BeansFactory.Countries().Load(
						this.getCountry());

				if (country.getCode().equals(CountryTypes.Italy.getCountry()))
				{
					listDAECs = BeansFactory.ControllerUserAnagraph()
							.GetUsersByRole(UserRoleTypes.CIL, null);
				}
				else
				{
					listDAECs = BeansFactory.ControllerUserAnagraph()
							.GetUsersByRole(UserRoleTypes.DAEC,
									Long.valueOf(this.getCountry()));
				}
			}
			else
			{
				listDAECs = BeansFactory.ControllerUserAnagraph()
						.GetUsersByRole(UserRoleTypes.DAEC, null);
			}
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
			e.printStackTrace();
		}

		for (ControllerUserAnagraph daec : listDAECs)
		{
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(daec.getId()));
			item.setLabel(daec.getUser().getName() + " "
					+ daec.getUser().getSurname());
			this.listDAEC.add(item);
		}
	}

	private String BuildLogin()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.getReferentName());
		sb.append(".");
		sb.append(this.getReferentSurname());

		sb = new StringBuilder(sb.toString().length() > 32 ? sb.toString()
				.substring(0, 31) : sb.toString());

		if (UsersBean.IsLoginExists(sb.toString(), null))
		{
			sb.append(UUID.randomUUID().toString().substring(0, 4));
			return sb.toString();
		}
		else
		{
			return sb.toString();
		}
	}

	private String BuildPassword()
	{
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public void countryChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setCountry(String.valueOf(event.getNewValue()));
		Countries country = BeansFactory.Countries().Load(this.getCountry());

		if (country.getCode().equals(CountryTypes.Italy.getCountry()))
		{
			if (!this.getAguMode().equals("none"))
			{
				this.setAutoCil("");
			}
			else
			{
				this.setAutoCil("none");
			}
		}
		else
		{
			this.setAutoCil("none");
		}

		try
		{
			this.SetLabel();
			this.FillDaecComboBox();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
	}

	public void partnerChanged(ValueChangeEvent event)
	{
		this.setSelectedPartner(String.valueOf(event.getNewValue()));

		this.getViewState().put("createnew", null);
		if (!event.getNewValue().equals("-1"))
		{
			this.getSession().put("selectedPartnercfedit",
					this.getSelectedPartner());
			goTo(PagesTypes.CFPARTNEREDIT);
		}
		else if (this.getProjectState().equals(ProjectState.Actual))
		{
			this.getViewState().put("createnew", true);
		}
	}

	public void cilChanged(ValueChangeEvent event)
			throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setFirstLevelController(String.valueOf(event.getNewValue()));
		Countries country = BeansFactory.Countries().Load(this.getCountry());

		if (country.getCode().equals(CountryTypes.Italy.getCountry()))
		{
			this.setDaec(this.getFirstLevelController());
		}

		this.Page_Load();
	}

	/**
	 * Sets country
	 * 
	 * @param country
	 *            the country to set
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public void setCountry(String country) throws HibernateException,
			NumberFormatException, PersistenceBeanException
	{
		this.getViewState().put("cfanagraphcountry", country);

		if (country != null
				&& !country.equals(SelectItemHelper.getFirst().getValue()))
		{
			this.setCountryTitle(BeansFactory.Countries().Load(country)
					.getValue());
		}
	}

	/**
	 * Gets country
	 * 
	 * @return country the country
	 */
	public String getCountry()
	{
		if (this.getViewState().get("cfanagraphcountry") != null)
		{
			return String.valueOf(this.getViewState().get("cfanagraphcountry"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets partita
	 * 
	 * @param partita
	 *            the partita to set
	 */
	public void setPartita(String partita)
	{
		this.getViewState().put("cfPartnerEditPartita", partita);
	}

	/**
	 * Gets partita
	 * 
	 * @return partita the partita
	 */
	public String getPartita()
	{
		if (this.getViewState().get("cfPartnerEditPartita") != null)
		{
			return String.valueOf(this.getViewState().get(
					"cfPartnerEditPartita"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.getViewState().put("Name", name);
	}

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getName()
	{
		return (String) this.getViewState().get("Name");
	}
	
	/**
	 * Sets publicSubject in the view state.
	 * @param publicSubject
	 */
	public void setPublicSubject(Boolean publicSubject) {
		if (publicSubject == null) {
			publicSubject = false;
		}
		this.getViewState().put("cfPartnerEditPublicSubject", publicSubject);
	}
	
	/**
	 * Gets publicSubject.
	 * @return publicSubject
	 */
	public Boolean getPublicSubject() {
		boolean isPublicSubject;
		
		Object isPublicSubjectObject = this.getViewState().get("cfPartnerEditPublicSubject");
		if (isPublicSubjectObject != null) {
			String isPublicSubjectString = String.valueOf(isPublicSubjectObject);
			isPublicSubject = Boolean.valueOf(isPublicSubjectString);
		} else {
			isPublicSubject = false;
		}
		
		return isPublicSubject;
	}
	
	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setCupCode(String cupCode)
	{
		this.getViewState().put("cupCode", cupCode);
	}

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getCupCode()
	{
		return (String) this.getViewState().get("cupCode");
	}

	/**
	 * Sets firstLevelController
	 * 
	 * @param firstLevelController
	 *            the firstLevelController to set
	 */
	public void setFirstLevelController(String firstLevelController)
	{
		this.getViewState().put("cfPartnerFLC", firstLevelController);
		this.getViewState().put("cfPartnerFLC2", firstLevelController);
	}

	/**
	 * Gets firstLevelController
	 * 
	 * @return firstLevelController the firstLevelController
	 */
	public String getFirstLevelController()
	{
		if (this.getViewState().get("cfPartnerFLC") != null)
		{
			return String.valueOf(this.getViewState().get("cfPartnerFLC"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets daec
	 * 
	 * @param daec
	 *            the daec to set
	 */
	public void setDaec(String daec)
	{
		this.getViewState().put("cfPartnerDaec", daec);
	}

	/**
	 * Gets daec
	 * 
	 * @return daec the daec
	 */
	public String getDaec()
	{
		if (this.getViewState().get("cfPartnerDaec") != null)
		{
			return String.valueOf(this.getViewState().get("cfPartnerDaec"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets referentName
	 * 
	 * @param referentName
	 *            the referentName to set
	 */
	public void setReferentName(String referentName)
	{
		this.getViewState().put("ReferentName", referentName);
	}

	/**
	 * Gets referentName
	 * 
	 * @return referentName the referentName
	 */
	public String getReferentName()
	{
		return (String) this.getViewState().get("ReferentName");
	}

	/**
	 * Sets referentSurname
	 * 
	 * @param referentSurname
	 *            the referentSurname to set
	 */
	public void setReferentSurname(String referentSurname)
	{
		this.getViewState().put("ReferentSurname", referentSurname);
	}

	/**
	 * Gets referentSurname
	 * 
	 * @return referentSurname the referentSurname
	 */
	public String getReferentSurname()
	{
		return (String) this.getViewState().get("ReferentSurname");
	}

	/**
	 * Sets cfReferent
	 * 
	 * @param cfReferent
	 *            the cfReferent to set
	 */
	public void setCfReferent(String cfReferent)
	{
		this.getViewState().put("CfReferent", cfReferent);
	}

	/**
	 * Gets cfReferent
	 * 
	 * @return cfReferent the cfReferent
	 */
	public String getCfReferent()
	{
		return (String) this.getViewState().get("CfReferent");
	}

	/**
	 * Sets emailReferent
	 * 
	 * @param emailReferent
	 *            the emailReferent to set
	 */
	public void setEmailReferent(String emailReferent)
	{
		this.getViewState().put("EmailReferent", emailReferent);
	}

	/**
	 * Gets emailReferent
	 * 
	 * @return emailReferent the emailReferent
	 */
	public String getEmailReferent()
	{
		return (String) this.getViewState().get("EmailReferent");
	}

	/**
	 * Sets listCountries
	 * 
	 * @param listCountries
	 *            the listCountries to set
	 */
	public void setListCountries(List<SelectItem> listCountries)
	{
		this.listCountries = listCountries;
	}

	/**
	 * Gets listCountries
	 * 
	 * @return listCountries the listCountries
	 */
	public List<SelectItem> getListCountries()
	{
		return listCountries;
	}

	/**
	 * Sets listCIL
	 * 
	 * @param listCIL
	 *            the listCIL to set
	 */
	public void setListCIL(List<SelectItem> listCIL)
	{
		this.listCIL = listCIL;
	}

	/**
	 * Gets listCIL
	 * 
	 * @return listCIL the listCIL
	 */
	public List<SelectItem> getListCIL()
	{
		return listCIL;
	}

	/**
	 * Sets listDAEC
	 * 
	 * @param listDAEC
	 *            the listDAEC to set
	 */
	public void setListDAEC(List<SelectItem> listDAEC)
	{
		this.listDAEC = listDAEC;
	}

	/**
	 * Gets listDAEC
	 * 
	 * @return listDAEC the listDAEC
	 */
	public List<SelectItem> getListDAEC()
	{
		return listDAEC;
	}

	/**
	 * Sets isPartner
	 * 
	 * @param isPartner
	 *            the isPartner to set
	 */
	public void setIsPartner(boolean isPartner)
	{
		this.isPartner = isPartner;
	}

	/**
	 * Gets isPartner
	 * 
	 * @return isPartner the isPartner
	 */
	public boolean getIsPartner()
	{
		return isPartner;
	}

	/**
	 * Sets daecLabel
	 * 
	 * @param daecLabel
	 *            the daecLabel to set
	 */
	public void setDaecLabel(String daecLabel)
	{
		this.daecLabel = daecLabel;
	}

	/**
	 * Gets daecLabel
	 * 
	 * @return daecLabel the daecLabel
	 */
	public String getDaecLabel()
	{
		return daecLabel;
	}

	/**
	 * Sets headingString
	 * 
	 * @param headingString
	 *            the headingString to set
	 */
	public void setHeadingString(String headingString)
	{
		this.headingString = headingString;
	}

	/**
	 * Gets headingString
	 * 
	 * @return headingString the headingString
	 */
	public String getHeadingString()
	{
		return headingString;
	}

	/**
	 * Sets innerHeadingString
	 * 
	 * @param innerHeadingString
	 *            the innerHeadingString to set
	 */
	public void setInnerHeadingString(String innerHeadingString)
	{
		this.innerHeadingString = innerHeadingString;
	}

	/**
	 * Gets innerHeadingString
	 * 
	 * @return innerHeadingString the innerHeadingString
	 */
	public String getInnerHeadingString()
	{
		return innerHeadingString;
	}

	/**
	 * Sets userRoleLabel
	 * 
	 * @param userRoleLabel
	 *            the userRoleLabel to set
	 */
	public void setUserRoleLabel(String userRoleLabel)
	{
		this.userRoleLabel = userRoleLabel;
	}

	/**
	 * Gets userRoleLabel
	 * 
	 * @return userRoleLabel the userRoleLabel
	 */
	public String getUserRoleLabel()
	{
		return userRoleLabel;
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner)
	{
		this.getViewState().put("cfPartnerEditSelPartner", selectedPartner);
		this.getSession().put("cfPartnerEditSelectedPartner", selectedPartner);

		try
		{
			Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner()
	{
		if (this.getSession().get("selectedPartnercfedit") != null
				&& !String.valueOf(
						this.getSession().get("selectedPartnercfedit"))
						.isEmpty())
		{
			this.setSelectedPartner(String.valueOf(this.getSession().get(
					"selectedPartnercfedit")));

		}

		return this.getViewState().get("cfPartnerEditSelPartner") == null ? null
				: String.valueOf(this.getViewState().get(
						"cfPartnerEditSelPartner"));
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners)
	{
		this.listPartners = listPartners;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners()
	{
		return listPartners;
	}

	/**
	 * Sets cfPartnerProject
	 * 
	 * @param cfPartnerProject
	 *            the cfPartnerProject to set
	 */
	public void setCfPartnerProject(CFPartnerAnagraphProject cfPartnerProject)
	{
		this.cfPartnerProject = cfPartnerProject;
	}

	/**
	 * Gets cfPartnerProject
	 * 
	 * @return cfPartnerProject the cfPartnerProject
	 */
	public CFPartnerAnagraphProject getCfPartnerProject()
	{
		return cfPartnerProject;
	}

	/**
	 * Sets editMode
	 * 
	 * @param editMode
	 *            the editMode to set
	 */
	public void setEditMode(boolean editMode)
	{
		this.editMode = editMode;
	}

	/**
	 * Gets editMode
	 * 
	 * @return editMode the editMode
	 */
	public boolean isEditMode()
	{
		return editMode;
	}

	/**
	 * Sets partner
	 * 
	 * @param partner
	 *            the partner to set
	 */
	public void setPartner(CFPartnerAnagraphs partner)
	{
		this.partner = partner;
	}

	/**
	 * Gets partner
	 * 
	 * @return partner the partner
	 */
	public CFPartnerAnagraphs getPartner()
	{
		return partner;
	}

	/**
	 * Sets aguMode
	 * 
	 * @param aguMode
	 *            the aguMode to set
	 */
	public void setAguMode(String aguMode)
	{
		this.aguMode = aguMode;
	}

	/**
	 * Gets aguMode
	 * 
	 * @return aguMode the aguMode
	 */
	public String getAguMode()
	{
		return aguMode;
	}

	/**
	 * Sets autoCil
	 * 
	 * @param autoCil
	 *            the autoCil to set
	 */
	public void setAutoCil(String autoCil)
	{
		this.autoCil = autoCil;

		if (this.getAguMode().equals("none"))
		{
			this.setNonAutoCil("none");
			this.autoCil = "";
		}
		else
		{
			this.setNonAutoCil(this.autoCil.equals("") ? "none" : "");
		}
	}

	/**
	 * Gets autoCil
	 * 
	 * @return autoCil the autoCil
	 */
	public String getAutoCil()
	{
		return autoCil;
	}

	/**
	 * Sets nonAutoCil
	 * 
	 * @param nonAutoCil
	 *            the nonAutoCil to set
	 */
	public void setNonAutoCil(String nonAutoCil)
	{
		this.nonAutoCil = nonAutoCil;
	}

	/**
	 * Gets nonAutoCil
	 * 
	 * @return nonAutoCil the nonAutoCil
	 */
	public String getNonAutoCil()
	{
		return nonAutoCil;
	}

	/**
	 * Sets firstLevelControllerIt
	 * 
	 * @param firstLevelControllerIt
	 *            the firstLevelControllerIt to set
	 */
	public void setFirstLevelControllerIt(String firstLevelControllerIt)
	{
		this.getViewState().put("cfPartnerFLC2", firstLevelControllerIt);
	}

	/**
	 * Gets firstLevelControllerIt
	 * 
	 * @return firstLevelControllerIt the firstLevelControllerIt
	 */
	public String getFirstLevelControllerIt()
	{
		if (this.getViewState().get("cfPartnerFLC2") != null)
		{
			return String.valueOf(this.getViewState().get("cfPartnerFLC2"));
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets countryTitle
	 * 
	 * @param countryTitle
	 *            the countryTitle to set
	 */
	public void setCountryTitle(String countryTitle)
	{
		this.countryTitle = countryTitle;
	}

	/**
	 * Gets countryTitle
	 * 
	 * @return countryTitle the countryTitle
	 */
	public String getCountryTitle()
	{
		return countryTitle;
	}

	/**
	 * Sets editActualMode
	 * 
	 * @param editActualMode
	 *            the editActualMode to set
	 */
	public void setEditActualMode(boolean editActualMode)
	{
		this.editActualMode = editActualMode;
	}

	/**
	 * Gets editActualMode
	 * 
	 * @return editActualMode the editActualMode
	 */
	public boolean getEditActualMode()
	{
		return editActualMode;
	}

	/**
	 * Sets listNPUMS
	 * 
	 * @param listNPUMS
	 *            the listNPUMS to set
	 */
	public void setListNPUMS(List<InfoObject> listNPUMS)
	{
		this.listNPUMS = listNPUMS;
	}

	/**
	 * Gets listNPUMS
	 * 
	 * @return listNPUMS the listNPUMS
	 */
	public List<InfoObject> getListNPUMS()
	{
		return listNPUMS;
	}

	/**
	 * Sets listNUMS
	 * 
	 * @param listNUMS
	 *            the listNUMS to set
	 */
	public void setListNUMS(List<InfoObject> listNUMS)
	{
		this.listNUMS = listNUMS;
	}

	/**
	 * Gets listNUMS
	 * 
	 * @return listNUMS the listNUMS
	 */
	public List<InfoObject> getListNUMS()
	{
		return listNUMS;
	}

	/**
	 * Sets mailToSend
	 * 
	 * @param mailToSend
	 *            the mailToSend to set
	 */
	public void setMailToSend(List<InfoObject> mailToSend)
	{
		this.mailToSend = mailToSend;
	}

	/**
	 * Gets mailToSend
	 * 
	 * @return mailToSend the mailToSend
	 */
	public List<InfoObject> getMailToSend()
	{
		return mailToSend;
	}

	/**
	 * Sets listNamings
	 * 
	 * @param listNamings
	 *            the listNamings to set
	 */
	public void setListNamings(List<SelectItem> listNamings)
	{
		this.listNamings = listNamings;
	}

	/**
	 * Gets listNamings
	 * 
	 * @return listNamings the listNamings
	 */
	public List<SelectItem> getListNamings()
	{
		return listNamings;
	}

	/**
	 * Sets naming
	 * 
	 * @param naming
	 *            the naming to set
	 */
	public void setNaming(String naming)
	{
		this.naming = naming;
	}

	/**
	 * Gets naming
	 * 
	 * @return naming the naming
	 */
	public String getNaming()
	{
		return naming;
	}

	/**
	 * Sets disableNaming
	 * 
	 * @param disableNaming
	 *            the disableNaming to set
	 */
	public void setDisableNaming(boolean disableNaming)
	{
		this.disableNaming = disableNaming;
	}

	/**
	 * Gets disableNaming
	 * 
	 * @return disableNaming the disableNaming
	 */
	public boolean getDisableNaming()
	{
		return disableNaming;
	}
}
