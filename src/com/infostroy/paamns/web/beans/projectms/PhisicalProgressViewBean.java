/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.enums.TypologyProductIndicator;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PhisicalProgressViewBean extends EntityProjectEditBean<Entity> {
	private boolean editable;

	private String coreStyle;

	private String engageStyle;

	private String gotStyle;

	private String programmedUpdateStyle;

	private String employmentStyle;

	private String implementationStyle;

	private List<PhisicalInitializationToCoreIndicators> coreList;

	private List<PhisicalInitializationToEmploymentIndicators> employmentList;

	private List<PhisicalInitializationToProgramIndicators> realizList;

	private List<PhisicalInitializationToQsnIndicators> qsnList;

	private List<ProjectIndicators> resultList;

	private List<ProjectIndicators> commList;

	private List<ProjectIndicators> progList;

	private List<ProjectIndicators> projList;

	// Old values to get difference and compare

	private List<PhisicalInitializationToCoreIndicators> coreListOld;

	private List<PhisicalInitializationToEmploymentIndicators> employmentListOld;

	private List<PhisicalInitializationToProgramIndicators> realizListOld;

	// Ids

	private String entityCoreEditId;

	private String entityEmpEditId;

	private String entityRealEditId;

	private String entityResEditId;

	private String entityComEditId;

	private String entityProgEditId;

	private String entityProjEditId;

	private Integer selectedIndex;

	private String errorMessage;

	private Integer asseNumber;

	private String asseStyle;

	private String entityCoreUploadId;

	private String entityCommUploadId;

	private String entityProgUploadId;

	private String entityResultUploadId;

	private String entityProjUploadId;

	private String entityCommNoteId;

	private String entityProgNoteId;

	private String entityResultNoteId;

	private String entityProjNoteId;
	
	 private Date expectedDateRealization;
	    
	    private Date actualDateRealization;

	// public void coreUpload(){
	// //crea sessione per entityCoreUploadId ed apri l'interfaccia
	// System.out.println("sono dentro coreuploat");
	// }

	public void commNote() {
		this.getSession().put("entityNote", entityCommNoteId);
		if (this.isEditable()) {
			this.getSession().put("isPhisicalProgress", true);
		} else {
			this.getSession().put("isPhisicalProgress", false);
		}
		this.goTo(PagesTypes.PHISICALPROGRESSNOTESBEAN);
	}

	public void progNote() {
		this.getSession().put("entityNote", entityProgNoteId);
		if (this.isEditable()) {
			this.getSession().put("isPhisicalProgress", true);
		} else {
			this.getSession().put("isPhisicalProgress", false);
		}
		this.goTo(PagesTypes.PHISICALPROGRESSNOTESBEAN);
	}

	public void resultNote() {
		this.getSession().put("entityNote", entityResultNoteId);
		if (this.isEditable()) {
			this.getSession().put("isPhisicalProgress", true);
		} else {
			this.getSession().put("isPhisicalProgress", false);
		}
		this.goTo(PagesTypes.PHISICALPROGRESSNOTESBEAN);
	}

	public void projNote() {
		this.getSession().put("entityNote", entityProjNoteId);
		if (this.isEditable()) {
			this.getSession().put("isPhisicalProgress", true);
		} else {
			this.getSession().put("isPhisicalProgress", false);
		}this.goTo(PagesTypes.PHISICALPROGRESSNOTESBEAN);
	}

	public void commUpload() {
		this.getSession().put("entityUpload", entityCommUploadId);
		 this.getSession().put("isCommUpload", true);
		this.goTo(PagesTypes.PHISICALPROGRESSUPLOADEDITBEAN);
	}

	public void progUpload() {
		this.getSession().put("entityUpload", entityProgUploadId);
		 this.getSession().put("isProgUpload", true);
		this.goTo(PagesTypes.PHISICALPROGRESSUPLOADEDITBEAN);
	}

	public void resultUpload() {
		this.getSession().put("entityUpload", entityResultUploadId);
		 this.getSession().put("isResultUpload", true);
		this.goTo(PagesTypes.PHISICALPROGRESSUPLOADEDITBEAN);
	}

	public void projUpload() {
		this.getSession().put("entityUpload", entityProjUploadId);
		 this.getSession().put("isProjUpload", true);
		this.goTo(PagesTypes.PHISICALPROGRESSUPLOADEDITBEAN);
	}

	/*
	 * public void editCore() { setEntityEmpEditId(null);
	 * setEntityRealEditId(null); setSelectedIndex(1); this.RefreshErrors(); }
	 * 
	 * public void editReal() { setEntityEmpEditId(null);
	 * setEntityCoreEditId(null); setSelectedIndex(4); this.RefreshErrors(); }
	 * 
	 * public void editEmp() { setEntityCoreEditId(null);
	 * setEntityRealEditId(null); setSelectedIndex(2); this.RefreshErrors(); }
	 */

	public void editComm() {
		setEntityResEditId(null);
		setEntityProgEditId(null);
		setEntityProjEditId(null);
		setSelectedIndex(0);

	}

	public void editProg() {
		setEntityResEditId(null);
		setEntityComEditId(null);
		setEntityProjEditId(null);
		setSelectedIndex(1);

	}

	public void editRes() {
		setEntityComEditId(null);
		setEntityProgEditId(null);
		setEntityProjEditId(null);
		setSelectedIndex(2);

	}

	public void editProj() {
		setEntityResEditId(null);
		setEntityComEditId(null);
		setEntityProgEditId(null);
		setSelectedIndex(3);

	}

	/*
	 * private void RefreshErrors() { this.setEngageStyle("");
	 * this.setGotStyle(""); this.setProgrammedUpdateStyle(""); }
	 */

	public void saveRes() {
		try {
			for (ProjectIndicators item : this.getResultList()) {
				if (item.getId().equals(Long.parseLong(this.getEntityResEditId()))) {
					Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					PersistenceSessionManager.getBean().getSession().merge(item);
					tr.commit();
				}
			}

			setEntityResEditId(null);
			setEntityComEditId(null);
			setEntityProgEditId(null);
			setEntityProjEditId(null);

			calculateAdvancesForResultIndicator();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void saveComm() {
		try {
			for (ProjectIndicators item : this.getCommList()) {
				if (item.getId().equals(Long.parseLong(this.getEntityComEditId()))) {
					Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					PersistenceSessionManager.getBean().getSession().merge(item);
					tr.commit();
				}
			}

			setEntityResEditId(null);
			setEntityComEditId(null);
			setEntityProgEditId(null);
			setEntityProjEditId(null);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void saveProg() {
		try {
			for (ProjectIndicators item : this.getProgList()) {
				if (item.getId().equals(Long.parseLong(this.getEntityProgEditId()))) {
					Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					PersistenceSessionManager.getBean().getSession().merge(item);
					tr.commit();
				}
			}

			setEntityResEditId(null);
			setEntityComEditId(null);
			setEntityProgEditId(null);
			setEntityProjEditId(null);

		} catch (Exception e) {
			log.error(e);
		}
	}

	private void calculateAdvancesForResultIndicator() throws HibernateException, PersistenceBeanException {
		// lista di indicatori creati
		List<ProjectIndicators> listProjectIndicators = BeansFactory.ProjectIndicators()
				.LoadAllByTypology(TypologyProductIndicator.ResultIndicator.getCode());
		// lista di indicatori dell'interfaccia "indicatori" - "indicatori di
		// risultato"
		List<ProgramIndicators> listProgramIndicators = new ArrayList<ProgramIndicators>();
		listProgramIndicators = BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.ResultView);
		calculateAvances(listProjectIndicators, listProgramIndicators);
	}

	private void calculateAdvancesForOutputIndicators() throws PersistenceBeanException {
		// lista di indicatori creati
		List<ProjectIndicators> listProjectIndicators = BeansFactory.ProjectIndicators()
				.LoadAllByTypology(TypologyProductIndicator.OutputIndicator.getCode());
		// lista di indicatori dell'interfaccia "indicatori" - "indicatori di
		// risultato"
		List<ProgramIndicators> listProgramIndicators = new ArrayList<ProgramIndicators>();
		listProgramIndicators = BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.Realization);
		calculateAvances(listProjectIndicators, listProgramIndicators);
	}

	private void calculateAvances(List<ProjectIndicators> listProjectIndicators,
			List<ProgramIndicators> listProgramIndicators) throws PersistenceBeanException {
		for (ProgramIndicators pi : listProgramIndicators) {
			Double avances1 = 0d;
			Double avances2 = 0d;
			Double avances3 = 0d;
			Double avances4 = 0d;
			for (ProjectIndicators pji : listProjectIndicators) {
				if (pji.getProgramIndicatorId().equals(pi.getId())) {
					avances1 += (pji.getAsse1() != null) ? Double.parseDouble(pji.getAsse1()) : 0d;
					avances2 += (pji.getAsse2() != null) ? Double.parseDouble(pji.getAsse2()) : 0d;
					avances3 += (pji.getAsse3() != null) ? Double.parseDouble(pji.getAsse3()) : 0d;
					avances4 += (pji.getAsse4() != null) ? Double.parseDouble(pji.getAsse4()) : 0d;
				}

			}
			pi.setAvances(String.valueOf(avances1));
			pi.setAvances2(String.valueOf(avances2));
			pi.setAvances3(String.valueOf(avances3));
			pi.setAvances4(String.valueOf(avances4));
			BeansFactory.ProgramIndicators().Merge(pi);
		}
	}

	public void saveProj() {
		try {
			for (ProjectIndicators item : this.getProjList()) {
				if (item.getId().equals(Long.parseLong(this.getEntityProjEditId()))) {
					Transaction tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
					PersistenceSessionManager.getBean().getSession().merge(item);
					tr.commit();
				}
			}

			setEntityResEditId(null);
			setEntityComEditId(null);
			setEntityProgEditId(null);
			setEntityProjEditId(null);

			// salvare i dati degli avanzamenti
			calculateAdvancesForOutputIndicators();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/*
	 * public void saveCore() { try { PhisicalInitializationToCoreIndicators
	 * oldItem = null;
	 * 
	 * for (PhisicalInitializationToCoreIndicators item : this
	 * .getCoreListOld()) { if (item.getId().equals(
	 * Long.parseLong(this.getEntityCoreEditId()))) { oldItem = item; break; } }
	 * 
	 * for (PhisicalInitializationToCoreIndicators item : coreList) { if
	 * (item.getId().equals( Long.parseLong(this.getEntityCoreEditId()))) {
	 * boolean isProgrammedValueUpdate = item .getProgrammedValueUpdate() ==
	 * null || item.getProgrammedValueUpdate().isEmpty(); boolean isGotValue =
	 * item.getGotValue() == null || item.getGotValue().isEmpty(); boolean
	 * isEngageValue = item.getEngageValue() == null ||
	 * item.getEngageValue().isEmpty();
	 * 
	 * if (isEngageValue) { this.setErrorMessage(Utils
	 * .getString("validator.engageValueIsMandatory")); setEngageStyle(
	 * "background-color: #FFE5E5;"); return; } else if (isGotValue) {
	 * this.setErrorMessage(Utils .getString("validator.gotValueIsMandatory"));
	 * setGotStyle("background-color: #FFE5E5;"); return; } else if
	 * (isProgrammedValueUpdate) { this.setErrorMessage(Utils
	 * .getString("validator.programmedValueUpdateIsMandatory"));
	 * setProgrammedUpdateStyle("background-color: #FFE5E5;"); return; } }
	 * 
	 * if (item.getId().equals(oldItem.getId()) &&
	 * PhisicalInitializationToCoreIndicators.IsChanged( item, oldItem)) {
	 * this.SaveAll(); break; } }
	 * 
	 * setEntityEmpEditId(null); setEntityCoreEditId(null);
	 * setEntityRealEditId(null); } catch(HibernateException e) {
	 * e.printStackTrace(); } catch(PersistenceBeanException e) {
	 * e.printStackTrace(); } }
	 * 
	 * public void saveEmp() { try {
	 * PhisicalInitializationToEmploymentIndicators oldItem = null; for
	 * (PhisicalInitializationToEmploymentIndicators item : this
	 * .getEmploymentListOld()) { if (item.getId().equals(
	 * Long.parseLong(this.getEntityEmpEditId()))) { oldItem = item; break; } }
	 * 
	 * for (PhisicalInitializationToEmploymentIndicators item : employmentList)
	 * { if (item.getId().equals( Long.parseLong(this.getEntityEmpEditId()))) {
	 * boolean isProgrammedValueUpdate = item .getProgrammedValueUpdate() ==
	 * null || item.getProgrammedValueUpdate().isEmpty(); boolean isGotValue =
	 * item.getGotValue() == null || item.getGotValue().isEmpty(); boolean
	 * isEngageValue = item.getEngageValue() == null ||
	 * item.getEngageValue().isEmpty();
	 * 
	 * if (isEngageValue) { this.setErrorMessage(Utils
	 * .getString("validator.engageValueIsMandatory")); setEngageStyle(
	 * "background-color: #FFE5E5;"); return; } else if (isGotValue) {
	 * this.setErrorMessage(Utils .getString("validator.gotValueIsMandatory"));
	 * setGotStyle("background-color: #FFE5E5;"); return; } else if
	 * (isProgrammedValueUpdate) { this.setErrorMessage(Utils
	 * .getString("validator.programmedValueUpdateIsMandatory"));
	 * setProgrammedUpdateStyle("background-color: #FFE5E5;"); return; } }
	 * 
	 * if (item.getId().equals(oldItem.getId()) &&
	 * PhisicalInitializationToEmploymentIndicators .IsChanged(item, oldItem)) {
	 * this.SaveAll(); break; } }
	 * 
	 * setEntityEmpEditId(null); setEntityCoreEditId(null);
	 * setEntityRealEditId(null); } catch(HibernateException e) {
	 * e.printStackTrace(); } catch(PersistenceBeanException e) {
	 * e.printStackTrace(); } }
	 * 
	 * public void saveReal() { try { PhisicalInitializationToProgramIndicators
	 * oldItem = null; for (PhisicalInitializationToProgramIndicators item :
	 * this .getRealizListOld()) { if (item.getId().equals(
	 * Long.parseLong(this.getEntityRealEditId()))) { oldItem = item; break; } }
	 * 
	 * for (PhisicalInitializationToProgramIndicators item : realizList) { if
	 * (item.getId().equals( Long.parseLong(this.getEntityRealEditId()))) {
	 * boolean isProgrammedValueUpdate = item .getProgrammedValueUpdate() ==
	 * null || item.getProgrammedValueUpdate().isEmpty(); boolean isGotValue =
	 * item.getGotValue() == null || item.getGotValue().isEmpty(); boolean
	 * isEngageValue = item.getEngageValue() == null ||
	 * item.getEngageValue().isEmpty();
	 * 
	 * if (isEngageValue) { this.setErrorMessage(Utils
	 * .getString("validator.engageValueIsMandatory")); setEngageStyle(
	 * "background-color: #FFE5E5;"); return; } else if (isGotValue) {
	 * this.setErrorMessage(Utils .getString("validator.gotValueIsMandatory"));
	 * setGotStyle("background-color: #FFE5E5;"); return; } else if
	 * (isProgrammedValueUpdate) { this.setErrorMessage(Utils
	 * .getString("validator.programmedValueUpdateIsMandatory"));
	 * setProgrammedUpdateStyle("background-color: #FFE5E5;"); return; } }
	 * 
	 * if (item.getId().equals(oldItem.getId()) &&
	 * PhisicalInitializationToProgramIndicators.IsChanged( item, oldItem)) {
	 * this.SaveAll(); break; } }
	 * 
	 * setEntityEmpEditId(null); setEntityCoreEditId(null);
	 * setEntityRealEditId(null); } catch(HibernateException e) {
	 * e.printStackTrace(); } catch(PersistenceBeanException e) {
	 * e.printStackTrace(); } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setProjList(BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "4"));

		this.setAsseNumber(
				BeansFactory.ProjectIformationCompletations().LoadByProject(this.getProjectId()).getYearDuration());

		// Core list
		/*
		 * setCoreList(BeansFactory.PhisicalInizializationToCoreIndicators()
		 * .LoadLastByProject(this.getProjectId()));
		 * 
		 * this.setCoreListOld(new
		 * ArrayList<PhisicalInitializationToCoreIndicators>());
		 * 
		 * for (PhisicalInitializationToCoreIndicators ent : this.getCoreList())
		 * { if (ent.getVersion() == null) { ent.setVersion(1l);
		 * BeansFactory.PhisicalInizializationToCoreIndicators().Save(ent); }
		 * 
		 * PhisicalInitializationToCoreIndicators newEntity = ent.clone();
		 * newEntity.setId(ent.getId());
		 * 
		 * this.getCoreListOld().add(newEntity); }
		 * 
		 * for (PhisicalInitializationToCoreIndicators ent : this.getCoreList())
		 * { if (ent.getProgressValidationObject() != null) {
		 * this.setEditable(false); break; } else { this.setEditable(true);
		 * break; } }
		 */

		// Employment list
		/*
		 * setEmploymentList(BeansFactory
		 * .PhisicalInizializationToEmploymentIndicators()
		 * .LoadLastByProject(this.getProjectId()));
		 * 
		 * this.setEmploymentListOld(new
		 * ArrayList<PhisicalInitializationToEmploymentIndicators>());
		 * 
		 * for (PhisicalInitializationToEmploymentIndicators ent : this
		 * .getEmploymentList()) { if (ent.getVersion() == null) {
		 * ent.setVersion(1l);
		 * BeansFactory.PhisicalInizializationToEmploymentIndicators()
		 * .Save(ent); }
		 * 
		 * PhisicalInitializationToEmploymentIndicators newEntity = ent
		 * .clone(); newEntity.setId(ent.getId());
		 * 
		 * this.getEmploymentListOld().add(newEntity); }
		 */

		// Realization list
		/*
		 * setRealizList(BeansFactory
		 * .PhisicalInizializationToProgramRealizationIndicators()
		 * .LoadLastByProject(this.getProjectId()));
		 * 
		 * this.setRealizListOld(new
		 * ArrayList<PhisicalInitializationToProgramIndicators>());
		 * 
		 * for (PhisicalInitializationToProgramIndicators ent : this
		 * .getRealizList()) { if (ent.getVersion() == null) {
		 * ent.setVersion(1l); BeansFactory
		 * .PhisicalInizializationToProgramRealizationIndicators() .Save(ent); }
		 * 
		 * PhisicalInitializationToProgramIndicators newEntity = ent.clone();
		 * newEntity.setId(ent.getId());
		 * 
		 * this.getRealizListOld().add(newEntity); }
		 */

		// Non-versioned entities

		/*
		 * setCommList(BeansFactory.
		 * PhisicalInizializationToCommunicationIndicators()
		 * .getByProject(this.getProjectId()));
		 */
		this.setCommList(BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "1"));

		/*
		 * setProgList(BeansFactory.PhisicalInizializationToProgIndicators()
		 * .getByProject(this.getProjectId()));
		 */
		this.setProgList(BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "2"));

		/*
		 * setResultList(BeansFactory
		 * .PhisicalInizializationToProgramResultIndicators()
		 * .getByProject(this.getProjectId()));
		 */
		this.setResultList(BeansFactory.ProjectIndicators().LoadByProject(this.getProjectId(), "3"));

		/*
		 * setQsnList(BeansFactory.PhisicalInizializationToQsnIndicators()
		 * .getByProject(this.getProjectId()));
		 */
		this.setEditable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		if (!this.getSessionBean().getIsActualSate()) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}
		setSelectedIndex(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		// Empty
	}

	/*
	 * private void SaveAll() throws HibernateException,
	 * PersistenceBeanException { for (PhisicalInitializationToCoreIndicators
	 * item : this.getCoreList()) { PhisicalInitializationToCoreIndicators
	 * newEntity = item.clone(); newEntity.setCreateDate(DateTime.getNow());
	 * newEntity.setVersion(item.getVersion() + 1);
	 * 
	 * BeansFactory.PhisicalInizializationToCoreIndicators().Save( newEntity); }
	 * 
	 * for (PhisicalInitializationToEmploymentIndicators item : this
	 * .getEmploymentList()) { PhisicalInitializationToEmploymentIndicators
	 * newEntity = item .clone(); newEntity.setCreateDate(DateTime.getNow());
	 * newEntity.setVersion(item.getVersion() + 1);
	 * 
	 * BeansFactory.PhisicalInizializationToEmploymentIndicators().Save(
	 * newEntity); }
	 * 
	 * for (PhisicalInitializationToProgramIndicators item : this
	 * .getRealizList()) { PhisicalInitializationToProgramIndicators newEntity =
	 * item.clone(); newEntity.setCreateDate(DateTime.getNow());
	 * newEntity.setVersion(item.getVersion() + 1);
	 * 
	 * BeansFactory.PhisicalInizializationToProgramRealizationIndicators()
	 * .Save(newEntity); } }
	 */

	/**
	 * Sets coreList
	 * 
	 * @param coreList
	 *            the coreList to set
	 */
	public void setCoreList(List<PhisicalInitializationToCoreIndicators> coreList) {
		this.coreList = coreList;
	}

	/**
	 * Gets coreList
	 * 
	 * @return coreList the coreList
	 */
	public List<PhisicalInitializationToCoreIndicators> getCoreList() {
		return coreList;
	}

	/**
	 * Sets realizList
	 * 
	 * @param realizList
	 *            the realizList to set
	 */
	public void setRealizList(List<PhisicalInitializationToProgramIndicators> realizList) {
		this.realizList = realizList;
	}

	/**
	 * Gets realizList
	 * 
	 * @return realizList the realizList
	 */
	public List<PhisicalInitializationToProgramIndicators> getRealizList() {
		return realizList;
	}

	/**
	 * Sets employmentList
	 * 
	 * @param employmentList
	 *            the employmentList to set
	 */
	public void setEmploymentList(List<PhisicalInitializationToEmploymentIndicators> employmentList) {
		this.employmentList = employmentList;
	}

	/**
	 * Gets employmentList
	 * 
	 * @return employmentList the employmentList
	 */
	public List<PhisicalInitializationToEmploymentIndicators> getEmploymentList() {
		return employmentList;
	}

	/**
	 * Sets entityCoreEditId
	 * 
	 * @param entityCoreEditId
	 *            the entityCoreEditId to set
	 */
	public void setEntityCoreEditId(String entityCoreEditId) {
		this.getViewState().put("entityCoreEditId", entityCoreEditId);
		this.entityCoreEditId = entityCoreEditId;
	}

	/**
	 * Gets entityCoreEditId
	 * 
	 * @return entityCoreEditId the entityCoreEditId
	 */
	public String getEntityCoreEditId() {
		entityCoreEditId = (String) this.getViewState().get("entityCoreEditId");
		return entityCoreEditId;
	}

	/**
	 * Sets entityCoreEditId
	 * 
	 * @param entityCoreEditId
	 *            the entityCoreEditId to set
	 */
	public void setEntityCoreUploadId(String entityCoreUploadId) {
		this.getViewState().put("entityCoreUploadId", entityCoreUploadId);
		this.entityCoreUploadId = entityCoreUploadId;
	}

	/**
	 * Gets entityCoreEditId
	 * 
	 * @return entityCoreEditId the entityCoreEditId
	 */
	public String getEntityCoreUploadId() {
		entityCoreUploadId = (String) this.getViewState().get("entityCoreUploadId");
		return entityCoreUploadId;
	}

	/**
	 * Sets entityCommEditId
	 * 
	 * @param entityCommEditId
	 *            the entityCommEditId to set
	 */
	public void setEntityCommUploadId(String entityCommUploadId) {
		this.getViewState().put("entityCommUploadId", entityCommUploadId);
		this.entityCommUploadId = entityCommUploadId;
	}

	/**
	 * Gets entityCommEditId
	 * 
	 * @return entityCommEditId the entityCommEditId
	 */
	public String getEntityCommUploadId() {
		entityCommUploadId = (String) this.getViewState().get("entityCommUploadId");
		return entityCommUploadId;
	}

	/**
	 * Sets entityProgUploadId
	 * 
	 * @param entityProgUploadId
	 *            the entityProgUploadId to set
	 */
	public void setEntityProgUploadId(String entityProgUploadId) {
		this.getViewState().put("entityProgUploadId", entityProgUploadId);
		this.entityProgUploadId = entityProgUploadId;
	}

	/**
	 * Gets entityProgUploadId
	 * 
	 * @return entityProgUploadId the entityProgUploadId
	 */
	public String getEntityProgUploadId() {
		entityProgUploadId = (String) this.getViewState().get("entityProgUploadId");
		return entityProgUploadId;
	}

	/**
	 * Sets entityResultUploadId
	 * 
	 * @param entityResultUploadId
	 *            the entityResultUploadId to set
	 */
	public void setEntityResultUploadId(String entityResultUploadId) {
		this.getViewState().put("entityResultUploadId", entityResultUploadId);
		this.entityResultUploadId = entityResultUploadId;
	}

	/**
	 * Gets entityResultUploadId
	 * 
	 * @return entityResultUploadId the entityResultUploadId
	 */
	public String getEntityResultUploadId() {
		entityResultUploadId = (String) this.getViewState().get("entityResultUploadId");
		return entityResultUploadId;
	}

	/**
	 * Sets entityProjUploadId
	 * 
	 * @param entityProjUploadId
	 *            the entityProjUploadId to set
	 */
	public void setEntityProjUploadId(String entityProjUploadId) {
		this.getViewState().put("entityProjUploadId", entityProjUploadId);
		this.entityProjUploadId = entityProjUploadId;
	}

	/**
	 * Gets entityProjUploadId
	 * 
	 * @return entityProjUploadId the entityProjUploadId
	 */
	public String getEntityProjUploadId() {
		entityProjUploadId = (String) this.getViewState().get("entityProjUploadId");
		return entityProjUploadId;
	}

	/**
	 * Sets entityEmpEditId
	 * 
	 * @param entityEmpEditId
	 *            the entityEmpEditId to set
	 */
	public void setEntityEmpEditId(String entityEmpEditId) {
		this.getViewState().put("entityEmpEditId", entityEmpEditId);
		this.entityEmpEditId = entityEmpEditId;
	}

	/**
	 * Gets entityEmpEditId
	 * 
	 * @return entityEmpEditId the entityEmpEditId
	 */
	public String getEntityEmpEditId() {
		entityEmpEditId = (String) this.getViewState().get("entityEmpEditId");
		return entityEmpEditId;
	}

	/**
	 * Sets entityRealEditId
	 * 
	 * @param entityRealEditId
	 *            the entityRealEditId to set
	 */
	public void setEntityRealEditId(String entityRealEditId) {
		this.getViewState().put("entityRealEditId", entityRealEditId);
		this.entityRealEditId = entityRealEditId;
	}

	/**
	 * Gets entityRealEditId
	 * 
	 * @return entityRealEditId the entityRealEditId
	 */
	public String getEntityRealEditId() {
		entityRealEditId = (String) this.getViewState().get("entityRealEditId");
		return entityRealEditId;
	}

	/**
	 * Sets selectedIndex
	 * 
	 * @param selectedIndex
	 *            the selectedIndex to set
	 */
	public void setSelectedIndex(Integer selectedIndex) {
		this.getViewState().put("selectedIndex", selectedIndex);
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Gets selectedIndex
	 * 
	 * @return selectedIndex the selectedIndex
	 */
	public Integer getSelectedIndex() {
		selectedIndex = (Integer) this.getViewState().get("selectedIndex");
		return selectedIndex;
	}

	/**
	 * Sets errorMessage
	 * 
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets errorMessage
	 * 
	 * @return errorMessage the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets qsnList
	 * 
	 * @param qsnList
	 *            the qsnList to set
	 */
	public void setQsnList(List<PhisicalInitializationToQsnIndicators> qsnList) {
		this.qsnList = qsnList;
	}

	/**
	 * Gets qsnList
	 * 
	 * @return qsnList the qsnList
	 */
	public List<PhisicalInitializationToQsnIndicators> getQsnList() {
		return qsnList;
	}

	/**
	 * Sets resultList
	 * 
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<ProjectIndicators> resultList) {
		this.resultList = resultList;
	}

	/**
	 * Gets resultList
	 * 
	 * @return resultList the resultList
	 */
	public List<ProjectIndicators> getResultList() {
		return resultList;
	}

	/**
	 * Sets coreListOld
	 * 
	 * @param coreListOld
	 *            the coreListOld to set
	 */
	public void setCoreListOld(List<PhisicalInitializationToCoreIndicators> coreListOld) {
		this.coreListOld = coreListOld;
	}

	/**
	 * Gets coreListOld
	 * 
	 * @return coreListOld the coreListOld
	 */
	public List<PhisicalInitializationToCoreIndicators> getCoreListOld() {
		return coreListOld;
	}

	/**
	 * Sets employmentListOld
	 * 
	 * @param employmentListOld
	 *            the employmentListOld to set
	 */
	public void setEmploymentListOld(List<PhisicalInitializationToEmploymentIndicators> employmentListOld) {
		this.employmentListOld = employmentListOld;
	}

	/**
	 * Gets employmentListOld
	 * 
	 * @return employmentListOld the employmentListOld
	 */
	public List<PhisicalInitializationToEmploymentIndicators> getEmploymentListOld() {
		return employmentListOld;
	}

	/**
	 * Sets realizListOld
	 * 
	 * @param realizListOld
	 *            the realizListOld to set
	 */
	public void setRealizListOld(List<PhisicalInitializationToProgramIndicators> realizListOld) {
		this.realizListOld = realizListOld;
	}

	/**
	 * Gets realizListOld
	 * 
	 * @return realizListOld the realizListOld
	 */
	public List<PhisicalInitializationToProgramIndicators> getRealizListOld() {
		return realizListOld;
	}

	/**
	 * Sets editable
	 * 
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Gets editable
	 * 
	 * @return editable the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Sets coreStyle
	 * 
	 * @param coreStyle
	 *            the coreStyle to set
	 */
	public void setCoreStyle(String coreStyle) {
		this.coreStyle = coreStyle;
	}

	/**
	 * Gets coreStyle
	 * 
	 * @return coreStyle the coreStyle
	 */
	public String getCoreStyle() {
		return coreStyle;
	}

	/**
	 * Sets employmentStyle
	 * 
	 * @param employmentStyle
	 *            the employmentStyle to set
	 */
	public void setEmploymentStyle(String employmentStyle) {
		this.employmentStyle = employmentStyle;
	}

	/**
	 * Gets employmentStyle
	 * 
	 * @return employmentStyle the employmentStyle
	 */
	public String getEmploymentStyle() {
		return employmentStyle;
	}

	/**
	 * Sets implementationStyle
	 * 
	 * @param implementationStyle
	 *            the implementationStyle to set
	 */
	public void setImplementationStyle(String implementationStyle) {
		this.implementationStyle = implementationStyle;
	}

	/**
	 * Gets implementationStyle
	 * 
	 * @return implementationStyle the implementationStyle
	 */
	public String getImplementationStyle() {
		return implementationStyle;
	}

	/**
	 * Sets engageStyle
	 * 
	 * @param engageStyle
	 *            the engageStyle to set
	 */
	public void setEngageStyle(String engageStyle) {
		this.engageStyle = engageStyle;
	}

	/**
	 * Gets engageStyle
	 * 
	 * @return engageStyle the engageStyle
	 */
	public String getEngageStyle() {
		return engageStyle;
	}

	/**
	 * Sets gotStyle
	 * 
	 * @param gotStyle
	 *            the gotStyle to set
	 */
	public void setGotStyle(String gotStyle) {
		this.gotStyle = gotStyle;
	}

	/**
	 * Gets gotStyle
	 * 
	 * @return gotStyle the gotStyle
	 */
	public String getGotStyle() {
		return gotStyle;
	}

	/**
	 * Sets programmedUpdateStyle
	 * 
	 * @param programmedUpdateStyle
	 *            the programmedUpdateStyle to set
	 */
	public void setProgrammedUpdateStyle(String programmedUpdateStyle) {
		this.programmedUpdateStyle = programmedUpdateStyle;
	}

	/**
	 * Gets programmedUpdateStyle
	 * 
	 * @return programmedUpdateStyle the programmedUpdateStyle
	 */
	public String getProgrammedUpdateStyle() {
		return programmedUpdateStyle;
	}

	/**
	 * Gets asseNumber
	 * 
	 * @return asseNumber the asseNumber
	 */
	public Integer getAsseNumber() {
		return asseNumber;
	}

	/**
	 * Sets asseNumber
	 * 
	 * @param asseNumber
	 *            the asseNumber to set
	 */
	public void setAsseNumber(Integer asseNumber) {
		this.asseNumber = asseNumber;
	}

	/**
	 * Gets asseStyle
	 * 
	 * @return asseStyle the asseStyle
	 */
	public String getAsseStyle() {
		return asseStyle;
	}

	/**
	 * Sets asseStyle
	 * 
	 * @param asseStyle
	 *            the asseStyle to set
	 */
	public void setAsseStyle(String asseStyle) {
		this.asseStyle = asseStyle;
	}

	/**
	 * Gets entityResEditId
	 * 
	 * @return entityResEditId the entityResEditId
	 */
	public String getEntityResEditId() {
		return entityResEditId;
	}

	/**
	 * Sets entityResEditId
	 * 
	 * @param entityResEditId
	 *            the entityResEditId to set
	 */
	public void setEntityResEditId(String entityResEditId) {
		this.entityResEditId = entityResEditId;
	}

	/**
	 * Gets entityComEditId
	 * 
	 * @return entityComEditId the entityComEditId
	 */
	public String getEntityComEditId() {
		return entityComEditId;
	}

	/**
	 * Sets entityComEditId
	 * 
	 * @param entityComEditId
	 *            the entityComEditId to set
	 */
	public void setEntityComEditId(String entityComEditId) {
		this.entityComEditId = entityComEditId;
	}

	/**
	 * Gets entityProgEditId
	 * 
	 * @return entityProgEditId the entityProgEditId
	 */
	public String getEntityProgEditId() {
		return entityProgEditId;
	}

	/**
	 * Sets entityProgEditId
	 * 
	 * @param entityProgEditId
	 *            the entityProgEditId to set
	 */
	public void setEntityProgEditId(String entityProgEditId) {
		this.entityProgEditId = entityProgEditId;
	}

	/**
	 * Gets commList
	 * 
	 * @return commList the commList
	 */
	public List<ProjectIndicators> getCommList() {
		return commList;
	}

	/**
	 * Sets commList
	 * 
	 * @param commList
	 *            the commList to set
	 */
	public void setCommList(List<ProjectIndicators> commList) {
		this.commList = commList;
	}

	/**
	 * Gets progList
	 * 
	 * @return progList the progList
	 */
	public List<ProjectIndicators> getProgList() {
		return progList;
	}

	/**
	 * Sets progList
	 * 
	 * @param progList
	 *            the progList to set
	 */
	public void setProgList(List<ProjectIndicators> progList) {
		this.progList = progList;
	}

	/**
	 * Gets projList
	 * 
	 * @return projList the projList
	 */
	public List<ProjectIndicators> getProjList() {
		return projList;
	}

	/**
	 * Sets projList
	 * 
	 * @param projList
	 *            the projList to set
	 */
	public void setProjList(List<ProjectIndicators> projList) {
		this.projList = projList;
	}

	/**
	 * Gets entityProjEditId
	 * 
	 * @return entityProjEditId the entityProjEditId
	 */
	public String getEntityProjEditId() {
		return entityProjEditId;
	}

	/**
	 * Sets entityProjEditId
	 * 
	 * @param entityProjEditId
	 *            the entityProjEditId to set
	 */
	public void setEntityProjEditId(String entityProjEditId) {
		this.entityProjEditId = entityProjEditId;
	}

	/**
	 * Sets entityCommNoteId
	 * 
	 * @param entityCommNoteId
	 *            the entityCommNoteId to set
	 */
	public void setEntityCommNoteId(String entityCommNoteId) {
		this.getViewState().put("entityCommNoteId", entityCommNoteId);
		this.entityCommNoteId = entityCommNoteId;
	}

	/**
	 * Gets entityCommNoteId
	 * 
	 * @return entityCommNoteId the entityCommNoteId
	 */
	public String getEntityCommNoteId() {
		entityCommNoteId = (String) this.getViewState().get("entityCommNoteId");
		return entityCommNoteId;
	}

	/**
	 * Sets entityProgNoteId
	 * 
	 * @param entityProgNoteId
	 *            the entityProgNoteId to set
	 */
	public void setEntityProgNoteId(String entityProgNoteId) {
		this.getViewState().put("entityProgNoteId", entityProgNoteId);
		this.entityProgNoteId = entityProgNoteId;
	}

	/**
	 * Gets entityProgNoteId
	 * 
	 * @return entityProgNoteId the entityProgNoteId
	 */
	public String getEntityProgNoteId() {
		entityCommNoteId = (String) this.getViewState().get("entityProgNoteId");
		return entityProgNoteId;
	}

	/**
	 * Sets entityResultNoteId
	 * 
	 * @param entityResultNoteId
	 *            the entityResultNoteId to set
	 */
	public void setEntityResultNoteId(String entityResultNoteId) {
		this.getViewState().put("entityResultNoteId", entityResultNoteId);
		this.entityResultNoteId = entityResultNoteId;
	}

	/**
	 * Gets entityResultNoteId
	 * 
	 * @return entityResultNoteId the entityResultNoteId
	 */
	public String getEntityResultNoteId() {
		entityResultNoteId = (String) this.getViewState().get("entityResultNoteId");
		return entityResultNoteId;
	}

	/**
	 * Sets entityProjNoteId
	 * 
	 * @param entityProjNoteId
	 *            the entityProjNoteId to set
	 */
	public void setEntityProjNoteId(String entityProjNoteId) {
		this.getViewState().put("entityProjNoteId", entityProjNoteId);
		this.entityProjNoteId = entityProjNoteId;
	}

	/**
	 * Gets entityProjNoteId
	 * 
	 * @return entityProjNoteId the entityProjNoteId
	 */
	public String getEntityProjNoteId() {
		entityProjNoteId = (String) this.getViewState().get("entityProjNoteId");
		return entityProjNoteId;
	}

	public Date getExpectedDateRealization() {
		expectedDateRealization = (Date) this.getViewState().get("expectedDateRealization");
		return expectedDateRealization;
	}

	public void setExpectedDateRealization(Date expectedDateRealization) {
		this.getViewState().put("expectedDateRealization", expectedDateRealization);
		this.expectedDateRealization = expectedDateRealization;
	}

	public Date getActualDateRealization() {
		actualDateRealization = (Date) this.getViewState().get("actualDateRealization");
		return actualDateRealization;
	}

	public void setActualDateRealization(Date actualDateRealization) {
		this.getViewState().put("actualDateRealization", actualDateRealization);
		this.actualDateRealization = actualDateRealization;
	}
	
	

}
