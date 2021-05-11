package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CPT;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.CUP;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ProjectStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Typologies;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2011.
 * modified by Ingloba360
 * 
 */
@Entity
@Table(name = "projects")
public class Projects extends PersistentEntity {

	/**
	 * Stores serialVersionUID value of entity.
	 */
	private static final long			serialVersionUID	= 6883707473051383448L;

	/**
	 * Stores code value of entity.
	 */
	@Column(name = "code")
	private String						code;

	/**
	 * Stores cupCode value of entity.
	 */
	@Column(name="description")
	private String 						descripion;
	
	@Column(name="descriptionEng")
	private String 						descripionEng;	
	
	@Column(name="expectedResults")
	private String 						expectedResults;	
	
	@Column(name = "cup_code")
	private String						cupCode;

	/**
	 * Stores title value of entity.
	 */
	@Column(name = "title")
	private String						title;

	/**
	 * Stores typology value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "typology_id")
	private Typologies					typology;

	@Column(name = "asse")
	private int							asse;

	/**
	 * Stores state value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "state_id")
	public ProjectStates				state;

	/**
	 * Stores specificGoal value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "specific_goal_id")
	private SpecificGoals				specificGoal;

	/**
	 * Stores specificGoal value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "thematic_goal_id")
	private SpecificGoals				thematicGoal;
	
	/**
	 * Stores specificGoal value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "prioritary_goal_id")
	private SpecificGoals				prioritaryGoal;
	
	/**
	 * Stores qsnGoal value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "qsn_goal_id")
	private QSNGoal						qsnGoal;

	/**
	 * Stores prioritaryReason value of entity.
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "prioritary_reason_id")
	private PrioritaryReasons			prioritaryReason;

	/**
	 * Stores activation procedure for which project belongs
	 */
	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "activation_procedure_anagraph_id")
	private ActivationProcedureAnagraph	activationProcedure;

	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "cup")
	private CUP							cup;

	@ManyToOne(cascade =
	{ CascadeType.ALL, CascadeType.ALL })
	@JoinColumn(name = "cpt")
	private CPT							cpt;

	@Column
	private Boolean						locked;
	
	/**
	 * Stores organization value of entity.
	 */
	@Column(name = "organization")
	private String						organization;
	
	/**
	 * Stores sustainDate value of entity.
	 */
	@Column(name = "sustain_date")
	private Date                      sustainDate;
	
	@Column(name = "implementing_tool_code", length = 10)
	private String implementingToolCode;
	
	@Column(name = "macro_strategy_code", length = 10)
	private String macroStrategyCode;	
	
	@Column(name = "classification_type_code", length = 10)
	private String classificationTypeCode;
	
	@Column(name = "classification_specification_code", length = 200)
	private String classificationSpecificationCode;
	
	@Column(name = "deleted_specification_state")
	private Integer deletedSpecificationState;
	
	@Column(name="hierarchical_level_code")
	private String hierarchicalLevelCode;

	@SuppressWarnings("unchecked")
	public static Boolean IsCodeExists(String value, Long id)
	{
		List<Projects> list = new ArrayList<Projects>();
		try
		{
			list = PersistenceSessionManager.getBean().getSession()
					.createCriteria(Projects.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.like("code", value))
					.add(Restrictions.ne("id", id.equals(null) ? 0 : id))
					.list();
		}
		catch (Exception e)
		{
			Logger.getLogger(Projects.class).error(e.getStackTrace());
		}
		if (list.size() == 0)
		{
			return false;
		}

		return true;
	}

	/**
	 * Sets code
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * Gets code
	 * 
	 * @return code the code
	 */
	@Export(propertyName = "exportCode", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.Projects)
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets title
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Gets title
	 * 
	 * @return title the title
	 */
	@Export(propertyName = "exportTitle", seqXLS = 1, type = FieldTypes.STRING, place = ExportPlaces.Projects)
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets typology
	 * 
	 * @param typology
	 *            the typology to set
	 */
	public void setTypology(Typologies typology)
	{
		this.typology = typology;
	}

	/**
	 * Gets typology
	 * 
	 * @return typology the typology
	 */
	public Typologies getTypology()
	{
		return typology;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(ProjectStates state)
	{
		this.state = state;
	}

	/**
	 * Gets state
	 * 
	 * @return state the state
	 */
	@Export(propertyName = "exportStatus", seqXLS = 2, type = FieldTypes.CLASS, place = ExportPlaces.Projects)
	public ProjectStates getState()
	{
		return state;
	}

	/**
	 * Sets specificGoal
	 * 
	 * @param specificGoal
	 *            the specificGoal to set
	 */
	public void setSpecificGoal(SpecificGoals specificGoal)
	{
		this.specificGoal = specificGoal;
	}

	/**
	 * Gets specificGoal
	 * 
	 * @return specificGoal the specificGoal
	 */
	public SpecificGoals getSpecificGoal()
	{
		return specificGoal;
	}

	/**
	 * Sets qsnGoal
	 * 
	 * @param qsnGoal
	 *            the qsnGoal to set
	 */
	public void setQsnGoal(QSNGoal qsnGoal)
	{
		this.qsnGoal = qsnGoal;
	}

	/**
	 * Gets qsnGoal
	 * 
	 * @return qsnGoal the qsnGoal
	 */
	public QSNGoal getQsnGoal()
	{
		return qsnGoal;
	}

	/**
	 * Sets prioritaryReason
	 * 
	 * @param prioritaryReason
	 *            the prioritaryReason to set
	 */
	public void setPrioritaryReason(PrioritaryReasons prioritaryReason)
	{
		this.prioritaryReason = prioritaryReason;
	}

	/**
	 * Gets prioritaryReason
	 * 
	 * @return prioritaryReason the prioritaryReason
	 */
	public PrioritaryReasons getPrioritaryReason()
	{
		return prioritaryReason;
	}

	/**
	 * Sets activationProcedure
	 * 
	 * @param activationProcedure
	 *            the activationProcedure to set
	 */
	public void setActivationProcedure(
			ActivationProcedureAnagraph activationProcedure)
	{
		this.activationProcedure = activationProcedure;
	}

	/**
	 * Gets activationProcedure
	 * 
	 * @return activationProcedure the activationProcedure
	 */
	@Export(propertyName = "exportActivationProcedure", seqXLS = 3, type = FieldTypes.CLASS, place = ExportPlaces.Projects)
	public ActivationProcedureAnagraph getActivationProcedure()
	{
		return activationProcedure;
	}

	/**
	 * Sets cup
	 * 
	 * @param cup
	 *            the cup to set
	 */
	public void setCup(CUP cup)
	{
		this.cup = cup;
	}

	/**
	 * Gets cup
	 * 
	 * @return cup the cup
	 */
	public CUP getCup()
	{
		return cup;
	}

	/**
	 * Sets cpt
	 * 
	 * @param cpt
	 *            the cpt to set
	 */
	public void setCpt(CPT cpt)
	{
		this.cpt = cpt;
	}

	/**
	 * Gets cpt
	 * 
	 * @return cpt the cpt
	 */
	public CPT getCpt()
	{
		return cpt;
	}

	/**
	 * Sets asse
	 * 
	 * @param asse
	 *            the asse to set
	 */
	public void setAsse(int asse)
	{
		this.asse = asse;
	}

	/**
	 * Gets asse
	 * 
	 * @return asse the asse
	 */
	public int getAsse()
	{
		return asse;
	}

	/**
	 * Sets cupCode
	 * 
	 * @param cupCode
	 *            the cupCode to set
	 */
	public void setCupCode(String cupCode)
	{
		this.cupCode = cupCode;
	}

	/**
	 * Gets cupCode
	 * 
	 * @return cupCode the cupCode
	 */
	@Export(propertyName = "exportCup", seqXLS = 5, type = FieldTypes.STRING, place = ExportPlaces.Projects)
	public String getCupCode()
	{
		return cupCode;
	}

	/**
	 * Gets locked
	 * 
	 * @return locked the locked
	 */
	public Boolean getLocked()
	{
		return locked;
	}

	/**
	 * Sets locked
	 * 
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(Boolean locked)
	{
		this.locked = locked;
	}

	public boolean isProjectCanBeFrozen()
	{
		if (ProjectState.FoundingEligible.getCode() != this.getState().getId()
				.intValue()
				&& ProjectState.Closed.getCode() != this.getState().getId()
						.intValue())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getDescripion()
	{
		return descripion;
	}

	public void setDescripion(String descripion)
	{
		this.descripion = descripion;
	}
	
	public String getDescripionEng()
	{
		return descripionEng;
	}

	public void setDescripionEng(String descripionEng)
	{
		this.descripionEng = descripionEng;
	}	
	
	public String getExpectedResults()
	{
		return expectedResults;
	}

	public void setExpectedResults(String expectedResults)
	{
		this.expectedResults = expectedResults;
	}	

	/**
	 * @return the thematicGoal
	 */
	public SpecificGoals getThematicGoal() {
		return thematicGoal;
	}

	/**
	 * @param thematicGoal the thematicGoal to set
	 */
	public void setThematicGoal(SpecificGoals thematicGoal) {
		this.thematicGoal = thematicGoal;
	}

	/**
	 * @return the prioritaryGoal
	 */
	public SpecificGoals getPrioritaryGoal() {
		return prioritaryGoal;
	}

	/**
	 * @param prioritaryGoal the prioritaryGoal to set
	 */
	public void setPrioritaryGoal(SpecificGoals prioritaryGoal) {
		this.prioritaryGoal = prioritaryGoal;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * @return the sustainDate
	 */
	public Date getSustainDate() {
		return sustainDate;
	}

	/**
	 * @param sustainDate the sustainDate to set
	 */
	public void setSustainDate(Date sustainDate) {
		this.sustainDate = sustainDate;
	}
	
	/**
	 * Gets implementingToolCode.
	 * @return implementingToolCode
	 */
	public String getImplementingToolCode() {
		return implementingToolCode;
	}
	
	/**
	 * Sets implementingToolCode.
	 * @param implementingToolCode
	 */
	public void setImplementingToolCode(String implementingToolCode) {
		this.implementingToolCode = implementingToolCode;
	}
	
	/**
	 * Gets macroStrategyCode.
	 * @return macroStrategyCode
	 */
	public String getMacroStrategyCode() {
		return macroStrategyCode;
	}
	
	/**
	 * Sets macroStrategyCode.
	 * @param macroStrategyCode
	 */
	public void setMacroStrategyCode(String macroStrategyCode) {
		this.macroStrategyCode = macroStrategyCode;
	}	
	
	/**
	 * Gets classificationTypeCode.
	 * @return classificationTypeCode
	 */
	public String getClassificationTypeCode() {
		return classificationTypeCode;
	}
	
	/**
	 * Sets classificationTypeCode.
	 * @param classificationTypeCode
	 */
	public void setClassificationTypeCode(String classificationTypeCode) {
		this.classificationTypeCode = classificationTypeCode;
	}
	
	/**
	 * Gets classificationSpecificationCode.
	 * @return classificationSpecificationCode
	 */
	public String getClassificationSpecificationCode() {
		return classificationSpecificationCode;
	}
	
	/**
	 * Sets classificationSpecificationCode.
	 * @param classificationSpecificationCode
	 */
	public void setClassificationSpecificationCode(String classificationSpecificationCode) {
		this.classificationSpecificationCode = classificationSpecificationCode;
	}
	
	/**
	 * Gets deletedSpecificationState.
	 * @return deletedSpecificationState
	 */
	public Integer getDeletedSpecificationState() {
		return deletedSpecificationState;
	}
	
	/**
	 * Sets deletedSpecificationState.
	 * @param deletedSpecificationState
	 */
	public void setDeletedSpecificationState(Integer deletedSpecificationState) {
		this.deletedSpecificationState = deletedSpecificationState;
	}

	public String getHierarchicalLevelCode() {
		return hierarchicalLevelCode;
	}

	public void setHierarchicalLevelCode(String hierarchicalLevelCode) {
		this.hierarchicalLevelCode = hierarchicalLevelCode;
	}
	
	
}
