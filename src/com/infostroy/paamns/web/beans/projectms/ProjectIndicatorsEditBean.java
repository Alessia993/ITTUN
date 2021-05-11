/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.enums.TypologyProductIndicator;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Typologies;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2014.
 *
 */
public class ProjectIndicatorsEditBean extends EntityProjectEditBean<ProjectIndicators> {

	private Long typology;

	private String name;

	private String measureUnit;

	private String target;

	private String phase;

	private String indicatorId;

	private List<SelectItem> listPhases;

	private List<SelectItem> listComObjectives;

	private List<SelectItem> listResultNames;

	private String specificObjective;

	private String communicationObjective;

	private String targetGroup;
	
	private String productCode;

	private Boolean editCommunication;

	private Boolean editProgram;

	private Boolean editResult;

	private Boolean editProject;

	private String titlePage;
	
	private String programId;
	
	private String communicationObjectiveId;

	private void FillPhases() {

		this.listPhases = new ArrayList<SelectItem>();

		//for (int i = 3; i < 7; i++) {
		for (int i = 1; i < 7; i++) {	
			String val = "GT" + i;
			listPhases.add(new SelectItem(val, val));
		}
	}

	private void FillComObjectives() throws HibernateException, PersistenceBeanException {
		this.listComObjectives = new ArrayList<SelectItem>();
		this.listComObjectives.clear();

		List<ProgramIndicators> listProgramIndicators = new ArrayList<ProgramIndicators>();
		listProgramIndicators = BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.Realization);

		listProgramIndicators = BeansFactory.ProgramIndicators().LoadByProgramIndicatorAxis(ProgramIndicatorsType.Realization, String.valueOf(this.getProjectAsse()));
		this.listComObjectives.add(SelectItemHelper.getFirst());
		for (ProgramIndicators programs : listProgramIndicators) {
			SelectItem item = new SelectItem();

			if (programs.getSpecificObjective() != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(programs.getAxisId());
				sb.append(" - ");
				sb.append(programs.getSpecificObjective());

				//item.setValue(sb.toString());
				item.setValue(String.valueOf(programs.getId()));
				item.setLabel(sb.toString());
			}

			this.listComObjectives.add(item);
		}
//		this.listComObjectives.add(SelectItemHelper.getOther());
		SelectItem item = new SelectItem();
		item.setValue("1000");
		item.setLabel(Utils.getString("otherElement"));
		this.listComObjectives.add(item);
	}

	private void FillResultNames() throws HibernateException, PersistenceBeanException {
		this.listResultNames = new ArrayList<SelectItem>();
		this.listResultNames.clear();

		List<ProgramIndicators> listProgramIndicators = new ArrayList<ProgramIndicators>();
		listProgramIndicators = BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.ResultView);
		this.listResultNames.add(SelectItemHelper.getFirst());
		for (ProgramIndicators programs : listProgramIndicators) {
			SelectItem item = new SelectItem();

			if (programs.getSpecificObjective() != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(programs.getAxisId());
				sb.append(" - ");
				sb.append(programs.getSpecificObjective());

				//item.setValue(sb.toString());

				item.setValue(String.valueOf(programs.getId()));
				item.setLabel(sb.toString());
			}

			this.listResultNames.add(item);
		}
//		this.listResultNames.add(SelectItemHelper.getOther());
		SelectItem item = new SelectItem();
		item.setValue("1000");
		item.setLabel(Utils.getString("otherElement"));
		this.listResultNames.add(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave() {
		this.GoBack();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack() {
		this.goTo(PagesTypes.PHISICALINITIALIZATIONLIST);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, IOException,
			NullPointerException {

		if ((boolean) this.getSession().get("isCommunicationIndicator") == true) {
			this.setEditCommunication(Boolean.TRUE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);
			this.setTitlePage(Utils.getString("Resources", "communicationIndicators", null));
		}

		if ((boolean) this.getSession().get("isProgramIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.TRUE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);
			this.setTitlePage(Utils.getString("Resources", "progIndicators", null));
		}

		if ((boolean) this.getSession().get("isResultIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.TRUE);
			this.setEditProject(Boolean.FALSE);
			this.FillResultNames();
			this.setTitlePage(Utils.getString("Resources", "resultIndicators", null));
		}

		if ((boolean) this.getSession().get("isProjectIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.TRUE);
			this.FillPhases();
			this.FillComObjectives();
			this.setTitlePage(Utils.getString("Resources", "projectIndicators", null));
		}

		if (this.getSession().get("communicationIndicators") != null) {
			ProjectIndicators entityToLoad = BeansFactory.ProjectIndicators()
					.Load(String.valueOf(this.getSession().get("communicationIndicators")));
			this.specificObjective = entityToLoad.getSpecificObjective();
			this.communicationObjective = entityToLoad.getCommunicationObjective();
			this.targetGroup = entityToLoad.getTargetGroup();
			this.productCode = entityToLoad.getProductCode();
			this.name = entityToLoad.getName();
			this.target = entityToLoad.getTarget();
			this.setEditCommunication(Boolean.TRUE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);
		}

		if (this.getSession().get("programIndicators") != null) {
			ProjectIndicators entityToLoad = BeansFactory.ProjectIndicators()
					.Load(String.valueOf(this.getSession().get("programIndicators")));
			this.name = entityToLoad.getName();
			this.target = entityToLoad.getTarget();
			this.productCode = entityToLoad.getProductCode();
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.TRUE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);

		}

		if (this.getSession().get("resultIndicators") != null) {
			ProjectIndicators entityToLoad = BeansFactory.ProjectIndicators()
					.Load(String.valueOf(this.getSession().get("resultIndicators")));
			//this.name = entityToLoad.getName();
			this.programId=String.valueOf(entityToLoad.getProgramIndicatorId());//entityToLoad.getName();
			this.communicationObjective = entityToLoad.getCommunicationObjective();
			this.target = entityToLoad.getTarget();
			this.productCode = entityToLoad.getProductCode();
			this.setMeasureUnit(entityToLoad.getMeasureUnit());
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.TRUE);
			this.setEditProject(Boolean.FALSE);
			
		}

		if (this.getSession().get("projectIndicators") != null) {
			ProjectIndicators entityToLoad = BeansFactory.ProjectIndicators()
					.Load(String.valueOf(this.getSession().get("projectIndicators")));
			this.specificObjective = entityToLoad.getSpecificObjective();
			this.communicationObjectiveId=String.valueOf(entityToLoad.getProgramIndicatorId());//getCommunicationObjective();
			//this.communicationObjective = entityToLoad.getCommunicationObjective();
			this.name = entityToLoad.getName();
			this.measureUnit = entityToLoad.getMeasureUnit();
			this.target = entityToLoad.getTarget();
			this.phase = entityToLoad.getPhase();
			this.targetGroup = entityToLoad.getTargetGroup();
			this.productCode = entityToLoad.getProductCode();
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.TRUE);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		ProjectIndicators entityToSave = new ProjectIndicators();
		if ((boolean) this.getSession().get("isCommunicationIndicator") == true) {
			this.setEditCommunication(Boolean.TRUE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);
			if (this.getSession().get("communicationIndicators") != null) {
				entityToSave = BeansFactory.ProjectIndicators()
						.Load(String.valueOf(this.getSession().get("communicationIndicators")));

				entityToSave.setSpecificObjective(this.getSpecificObjective());
				entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setTargetGroup(this.getTargetGroup());
				entityToSave.setProductCode(this.getProductCode());
				entityToSave.setName(this.getName());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ComunicationProduct.getCode());
			} else {
				Projects project = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));

				entityToSave.setProject(project);
				entityToSave.setSpecificObjective(this.getSpecificObjective());
				entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setTargetGroup(this.getTargetGroup());
				entityToSave.setProductCode(this.getProductCode());
				entityToSave.setName(this.getName());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setIndicatorId(1L);
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ComunicationProduct.getCode());
			}
		}

		if ((boolean) this.getSession().get("isProgramIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.TRUE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.FALSE);
			if (this.getSession().get("programIndicators") != null) {
				entityToSave = BeansFactory.ProjectIndicators()
						.Load(String.valueOf(this.getSession().get("programIndicators")));

				entityToSave.setName(this.getName());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setProductCode(this.getProductCode());
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ProjectManagementProduct.getCode());
			} else {
				Projects project = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));
				entityToSave.setProject(project);
				entityToSave.setName(this.getName());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setProductCode(this.getProductCode());
				entityToSave.setIndicatorId(2L);
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ProjectManagementProduct.getCode());
			}
		}

		if ((boolean) this.getSession().get("isResultIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.TRUE);
			this.setEditProject(Boolean.FALSE);
			String name="";
			for(SelectItem si: listResultNames){
				if(si.getValue().equals(this.getProgramId())){
					name=si.getLabel();
					break;
				}
			}
			if (this.getSession().get("resultIndicators") != null) {
				entityToSave = BeansFactory.ProjectIndicators()
						.Load(String.valueOf(this.getSession().get("resultIndicators")));
				
				//entityToSave.setName(this.getName());
				entityToSave.setName(name);
				entityToSave.setProgramIndicatorId(Long.valueOf(this.getProgramId()));
				entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ResultIndicator.getCode());
				entityToSave.setMeasureUnit(this.getMeasureUnit());
			} else {
				Projects project = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));
				entityToSave.setProject(project);
				//entityToSave.setName(this.getName());
				entityToSave.setName(name);
				entityToSave.setProgramIndicatorId(Long.valueOf(this.getProgramId()));
				entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setIndicatorId(3L);
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.ResultIndicator.getCode());
				entityToSave.setMeasureUnit(this.getMeasureUnit());
			}
		}

		if ((boolean) this.getSession().get("isProjectIndicator") == true) {
			this.setEditCommunication(Boolean.FALSE);
			this.setEditProgram(Boolean.FALSE);
			this.setEditResult(Boolean.FALSE);
			this.setEditProject(Boolean.TRUE);
			String name="";
			for(SelectItem si: listComObjectives){
				if(si.getValue().equals(this.getCommunicationObjectiveId())){
					name=si.getLabel();
					break;
				}
			}
			if (this.getSession().get("projectIndicators") != null) {
				entityToSave = BeansFactory.ProjectIndicators()
						.Load(String.valueOf(this.getSession().get("projectIndicators")));

				entityToSave.setSpecificObjective(this.getSpecificObjective());
				//entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setCommunicationObjective(name);
				entityToSave.setName(this.getName());
				//entityToSave.setName(name);
				entityToSave.setProgramIndicatorId(Long.valueOf(this.getCommunicationObjectiveId()));
				entityToSave.setMeasureUnit(this.getMeasureUnit());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setPhase(this.getPhase());
				entityToSave.setTargetGroup(this.getTargetGroup());
				entityToSave.setProductCode(this.getProductCode());
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.OutputIndicator.getCode());
			} else {
				Projects project = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));
				entityToSave.setProject(project);
				entityToSave.setSpecificObjective(this.getSpecificObjective());
				//entityToSave.setCommunicationObjective(this.getCommunicationObjective());
				entityToSave.setCommunicationObjective(name);
				entityToSave.setName(this.getName());
				entityToSave.setProgramIndicatorId(Long.valueOf(this.getCommunicationObjectiveId()));
				//entityToSave.setName(name);
				entityToSave.setMeasureUnit(this.getMeasureUnit());
				entityToSave.setTarget(this.getTarget());
				entityToSave.setPhase(this.getPhase());
				entityToSave.setTargetGroup(this.getTargetGroup());
				entityToSave.setProductCode(this.getProductCode());
				
				entityToSave.setIndicatorId(4L);
				entityToSave.setTypologyProductIndicator(TypologyProductIndicator.OutputIndicator.getCode());
			}
		}

		BeansFactory.ProjectIndicators().Save(entityToSave);
		//calculateAdvancesForResultIndicator();
		//calculateAdvancesForOutputIndicators();
	}

	

	/**
	 * Gets name
	 * 
	 * @return name the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets measureUnit
	 * 
	 * @return measureUnit the measureUnit
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * Sets measureUnit
	 * 
	 * @param measureUnit
	 *            the measureUnit to set
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * Gets target
	 * 
	 * @return target the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets target
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Gets phase
	 * 
	 * @return phase the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * Sets phase
	 * 
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/**
	 * Gets listPhases
	 * 
	 * @return listPhases the listPhases
	 */
	public List<SelectItem> getListPhases() {
		return listPhases;
	}

	/**
	 * Sets listPhases
	 * 
	 * @param listPhases
	 *            the listPhases to set
	 */
	public void setListPhases(List<SelectItem> listPhases) {
		this.listPhases = listPhases;
	}

	/**
	 * @return the listComObjectives
	 */
	public List<SelectItem> getListComObjectives() {
		return listComObjectives;
	}

	/**
	 * @param listComObjectives
	 *            the listComObjectives to set
	 */
	public void setListComObjectives(List<SelectItem> listComObjectives) {
		this.listComObjectives = listComObjectives;
	}

	/**
	 * @return the listResultNames
	 */
	public List<SelectItem> getListResultNames() {
		return listResultNames;
	}

	/**
	 * @param listResultNames
	 *            the listResultNames to set
	 */
	public void setListResultNames(List<SelectItem> listResultNames) {
		this.listResultNames = listResultNames;
	}

	public String getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	/**
	 * @return the specificObjective
	 */
	public String getSpecificObjective() {
		return specificObjective;
	}

	/**
	 * @param specificObjective
	 *            the specificObjective to set
	 */
	public void setSpecificObjective(String specificObjective) {
		this.specificObjective = specificObjective;
	}

	/**
	 * @return the communicationObjective
	 */
	public String getCommunicationObjective() {
		return communicationObjective;
	}

	/**
	 * @param communicationObjective
	 *            the communicationObjective to set
	 */
	public void setCommunicationObjective(String communicationObjective) {
		this.communicationObjective = communicationObjective;
	}

	/**
	 * @return the targetGroup
	 */
	public String getTargetGroup() {
		return targetGroup;
	}

	/**
	 * @param targetGroup
	 *            the targetGroup to set
	 */
	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}
	
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode
	 *            the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Boolean getEditCommunication() {
		return editCommunication;
	}

	public void setEditCommunication(Boolean editCommunication) {
		this.editCommunication = editCommunication;
	}

	/**
	 * @return the editProgram
	 */
	public Boolean getEditProgram() {
		return editProgram;
	}

	/**
	 * @param editProgram
	 *            the editProgram to set
	 */
	public void setEditProgram(Boolean editProgram) {
		this.editProgram = editProgram;
	}

	/**
	 * @return the editResult
	 */
	public Boolean getEditResult() {
		return editResult;
	}

	/**
	 * @param editResult
	 *            the editResult to set
	 */
	public void setEditResult(Boolean editResult) {
		this.editResult = editResult;
	}

	/**
	 * @return the editProject
	 */
	public Boolean getEditProject() {
		return editProject;
	}

	/**
	 * @param editProject
	 *            the editProject to set
	 */
	public void setEditProject(Boolean editProject) {
		this.editProject = editProject;
	}

	/**
	 * @return the titlePage
	 */
	public String getTitlePage() {
		return titlePage;
	}

	/**
	 * @param titlePage
	 *            the titlePage to set
	 */
	public void setTitlePage(String titlePage) {
		this.titlePage = titlePage;
	}

	public Long getTypology() {
		return typology;
	}

	public void setTypology(Long typology) {
		this.typology = typology;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getCommunicationObjectiveId() {
		return communicationObjectiveId;
	}

	public void setCommunicationObjectiveId(String communicationObjectiveId) {
		this.communicationObjectiveId = communicationObjectiveId;
	}

}
