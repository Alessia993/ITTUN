/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializations;
import com.infostroy.paamns.persistence.beans.facades.PhisicalInitializationToCommunicationIndicatorsBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.wrappers.CommunicationIndicatorWrapper;
import com.infostroy.paamns.web.beans.wrappers.CoreIndicatorWrapper;
import com.infostroy.paamns.web.beans.wrappers.EmploymentIndicatorWebBean;
import com.infostroy.paamns.web.beans.wrappers.ProgIndicatorWrapper;
import com.infostroy.paamns.web.beans.wrappers.ProgramRealizationIndicatorWrapper;
import com.infostroy.paamns.web.beans.wrappers.ProgramResultIndicatorWrapper;
import com.infostroy.paamns.web.beans.wrappers.QSNIndicatorWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class PhisicalInitializationEditBean extends
		EntityProjectEditBean<Entity>
{
	private int		selectedIndex;

	private String	coreStyle;

	private String	employmentStyle;

	private String	implementationStyle;

	private String	qsnStyle;

	private String	resultStyle;

	@SuppressWarnings("unused")
	private Long	selectedRowIndexQSN;

	@SuppressWarnings("unused")
	private Long	selectedRowIndexCore;

	@SuppressWarnings("unused")
	private Long	selectedRowIndexEmployment;

	private String	qsnError;

	private String	coreError;

	private String	resultError;

	private String	implementationError;

	private String	employmentError;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack()
	{
		goTo(PagesTypes.PHISICALINITIALIZATIONLIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		if (this.getSessionBean().getIsActualSateAndAguRead())
		{
			goTo(PagesTypes.PROJECTINDEX);
		}
	}

	public Boolean BeforeSave() throws PersistenceBeanException
	{
		if (BeansFactory.PhisicalInizializations().getByProject(
				this.getProjectId()) == null)
		{
			PhisicalInitializations phisInit = new PhisicalInitializations();
			phisInit.setProject(this.getProject());
			BeansFactory.PhisicalInizializations().Save(phisInit);
		}

		/*
		 * if (this.getSelectedRowIndexQSN() == null) { setSelectedIndex(0);
		 * setQsnStyle("background-color: #FFE5E5;");
		 * setCoreError(Utils.getString("qsnError")); return false; }
		 * 
		 * if (this.getSelectedRowIndexCore() != null &&
		 * !CoreIndicatorsListBean.isValid(this .getSelectedRowIndexCore())) {
		 * setSelectedIndex(1); setCoreStyle("background-color: #FFE5E5;");
		 * setCoreError(Utils.getString("programmedValueExeption")); return
		 * false; } else if (this.getSelectedRowIndexCore() == null) {
		 * setSelectedIndex(1); setCoreStyle("background-color: #FFE5E5;");
		 * setCoreError(Utils.getString("coreError")); return false; }
		 * 
		 * if (this.getSelectedRowIndexEmployment() != null &&
		 * !EmploymentIndicatorsListBean.isValid(this
		 * .getSelectedRowIndexEmployment())) { setSelectedIndex(2);
		 * setEmploymentStyle("background-color: #FFE5E5;");
		 * setCoreError(Utils.getString("programmedValueExeption")); return
		 * false; } else if (this.getSelectedRowIndexEmployment() == null) {
		 * setSelectedIndex(2);
		 * setEmploymentStyle("background-color: #FFE5E5;");
		 * setCoreError(Utils.getString("employmentError")); return false; }
		 */
/*		if (ProgramResultIndicatorsListBean.isEmpty())
		{
			setSelectedIndex(1);
			setResultStyle("background-color: #FFE5E5;");
			setCoreError(Utils.getString("resultError"));
			return false;
		}
		
		if(CommunicationIndicatorsListBean.isEmpty())
		{
			setSelectedIndex(2);
			setResultStyle("background-color: #FFE5E5;");
			setCoreError(Utils.getString("communicationIndicatorsError"));
			return false;
		}
		
		if(ProgIndicatorsListBean.isEmpty())
		{
			setSelectedIndex(3);
			setResultStyle("background-color: #FFE5E5;");
			setCoreError(Utils.getString("progIndicatorsError"));
			return false;
		}
*/
/*		if (ProgramRealizationIndicatorsListBean.isEmpty())
		{
			setSelectedIndex(4);
			setImplementationStyle("background-color: #FFE5E5;");
			setCoreError(Utils.getString("implementatonError"));
			return false;
		}

		if (!ProgramRealizationIndicatorsListBean.isValid())
		{
			setSelectedIndex(4);
			setImplementationStyle("background-color: #FFE5E5;");
			setResultError(Utils.getString("programmedValueExeption"));
			return false;
		}*/

		/*
		 * BeansFactory.PhisicalInizializationToEmploymentIndicators().DeleteAll(
		 * this.getProjectId());
		 * BeansFactory.PhisicalInizializationToProgramRealizationIndicators()
		 * .DeleteAll(this.getProjectId());
		 * BeansFactory.PhisicalInizializationToQsnIndicators().DeleteAll(
		 * this.getProjectId());
		 * 
		 * BeansFactory.PhisicalInizializationToCoreIndicators().DeleteAll(
		 * this.getProjectId());
		 * 
		 * BeansFactory.PhisicalInizializationToCommunicationIndicators().DeleteAll
		 * ( this.getProjectId());
		 * 
		 * BeansFactory.PhisicalInizializationToProgIndicators().DeleteAll(
		 * this.getProjectId());
		 */
		BeansFactory.PhisicalInizializationToProgramResultIndicators().DeleteAll(
				this.getProjectId());


		
		

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException
	{
		PersistenceSessionManager.getBean().getSession().clear();
		// PhisicalInitializationToQsnIndicators

/*		if (this.getSelectedRowIndexQSN() != null)
		{
			QSNIndicatorWrapper qsnWeb = QSNIndicatorsListBean.getByRow(this
					.getSelectedRowIndexQSN());
			PhisicalInitializationToQsnIndicators qsnIndiItem = new PhisicalInitializationToQsnIndicators();
			qsnIndiItem.setProgrammedValues(qsnWeb.getProgrammedValues());
			qsnIndiItem.setIndicator(qsnWeb.getIndicator());
			qsnIndiItem.setPhisicalInitialization(BeansFactory
					.PhisicalInizializations()
					.getByProject(this.getProjectId()));
			BeansFactory.PhisicalInizializationToQsnIndicators()
					.SaveInTransaction(qsnIndiItem);
		}*/

		// PhisicalInitializationToCoreIndicators

/*		if (this.getSelectedRowIndexCore() != null)
		{
			CoreIndicatorWrapper coreWeb = CoreIndicatorsListBean.getByRow(this
					.getSelectedRowIndexCore());
			PhisicalInitializationToCoreIndicators coreIndiItem = new PhisicalInitializationToCoreIndicators();
			coreIndiItem.setProgrammedValues(coreWeb.getProgrammedValues());
			coreIndiItem.setIndicator(coreWeb.getIndicator());
			coreIndiItem.setPhisicalInitialization(BeansFactory
					.PhisicalInizializations()
					.getByProject(this.getProjectId()));
			BeansFactory.PhisicalInizializationToCoreIndicators()
					.SaveInTransaction(coreIndiItem);
		}*/

		// PhisicalInitializationToProgramIndicators
/*
		for (ProgramResultIndicatorWrapper item : ProgramResultIndicatorsListBean
				.getAllIndiList())
		{
			if (item.getSelected())
			{
				PhisicalInitializationToProgramIndicators resultIndiItem = new PhisicalInitializationToProgramIndicators();
				resultIndiItem.setProgrammedValues(item
						.getProgrammedValues());
				resultIndiItem.setProgramIndicator(item.getIndicator());
				resultIndiItem.setPhisicalInitialization(BeansFactory
						.PhisicalInizializations().getByProject(
								this.getProjectId()));
				resultIndiItem.setTargetValue(item.getTargetValue());
				BeansFactory.PhisicalInizializationToProgramResultIndicators()
						.SaveInTransaction(resultIndiItem);

			}
		}
		
		for (CommunicationIndicatorWrapper item : CommunicationIndicatorsListBean
				.getAllIndiList())
		{
			if (item.getSelected())
			{
				PhisicalInitializationToProgramIndicators resultIndiItem = new PhisicalInitializationToProgramIndicators();
				resultIndiItem.setProgrammedValues(item
						.getProgrammedValues());
				resultIndiItem.setProgramIndicator(item.getIndicator());
				resultIndiItem.setPhisicalInitialization(BeansFactory
						.PhisicalInizializations().getByProject(
								this.getProjectId()));
				resultIndiItem.setTargetValue(item.getTargetValue());
				resultIndiItem.setUnitValue(item.getUnitValue());
				BeansFactory.PhisicalInizializationToCommunicationIndicators()
						.SaveInTransaction(resultIndiItem);

			}
		}
		
		for (ProgIndicatorWrapper item : ProgIndicatorsListBean
				.getAllIndiList())
		{
			if (item.getSelected())
			{
				PhisicalInitializationToProgramIndicators resultIndiItem = new PhisicalInitializationToProgramIndicators();
				resultIndiItem.setProgrammedValues(item
						.getProgrammedValues());
				resultIndiItem.setProgramIndicator(item.getIndicator());
				resultIndiItem.setPhisicalInitialization(BeansFactory
						.PhisicalInizializations().getByProject(
								this.getProjectId()));
				resultIndiItem.setTargetValue(item.getTargetValue());
				resultIndiItem.setUnitValue(item.getUnitValue());
				BeansFactory.PhisicalInizializationToProgIndicators()
						.SaveInTransaction(resultIndiItem);

			}
		}
*/
/*		for (ProgramRealizationIndicatorWrapper item : ProgramRealizationIndicatorsListBean
				.getAllIndiList())
		{
			if (item.getSelected())
			{
				PhisicalInitializationToProgramIndicators realIndiItem = new PhisicalInitializationToProgramIndicators();
				realIndiItem.setProgramIndicator(item.getIndicator());
				realIndiItem.setProgrammedValues(item.getProgrammedValues());
				realIndiItem.setPhisicalInitialization(BeansFactory
						.PhisicalInizializations().getByProject(
								this.getProjectId()));
				BeansFactory
						.PhisicalInizializationToProgramRealizationIndicators()
						.SaveInTransaction(realIndiItem);
			}
		}*/

/*		if (this.getSelectedRowIndexEmployment() != null)
		{
			EmploymentIndicatorWebBean coreWeb = EmploymentIndicatorsListBean
					.getByRow(this.getSelectedRowIndexEmployment());
			PhisicalInitializationToEmploymentIndicators realIndiItem = new PhisicalInitializationToEmploymentIndicators();
			realIndiItem.setProgrammedValues(coreWeb.getProgrammedValues());
			realIndiItem.setIndicator(coreWeb.getIndicator());
			realIndiItem.setPhisicalInitialization(BeansFactory
					.PhisicalInizializations()
					.getByProject(this.getProjectId()));
			BeansFactory.PhisicalInizializationToEmploymentIndicators()
					.SaveInTransaction(realIndiItem);
		}*/

	}

	/**
	 * Sets selectedRowIndexCore
	 * 
	 * @param selectedRowIndexCore
	 *            the selectedRowIndexCore to set
	 */
	public void setSelectedRowIndexCore(Long selectedRowIndexCore)
	{
		this.getViewState().put("selectedRowIndexCore", selectedRowIndexCore);
	}

	/**
	 * Gets selectedRowIndexCore
	 * 
	 * @return selectedRowIndexCore the selectedRowIndexCore
	 */
	public Long getSelectedRowIndexCore()
	{
		return (Long) this.getViewState().get("selectedRowIndexCore");
	}

	/**
	 * Sets selectedRowIndexQSN
	 * 
	 * @param selectedRowIndexQSN
	 *            the selectedRowIndexQSN to set
	 */
	public void setSelectedRowIndexQSN(Long selectedRowIndexQSN)
	{
		this.getViewState().put("selectedRowIndexQSN", selectedRowIndexQSN);
	}

	/**
	 * Gets selectedRowIndexQSN
	 * 
	 * @return selectedRowIndexQSN the selectedRowIndexQSN
	 */
	public Long getSelectedRowIndexQSN()
	{
		return (Long) this.getViewState().get("selectedRowIndexQSN");
	}

	/**
	 * Sets qsnError
	 * 
	 * @param qsnError
	 *            the qsnError to set
	 */
	public void setQsnError(String qsnError)
	{
		this.qsnError = qsnError;
	}

	/**
	 * Gets qsnError
	 * 
	 * @return qsnError the qsnError
	 */
	public String getQsnError()
	{
		return qsnError;
	}

	/**
	 * Sets coreError
	 * 
	 * @param coreError
	 *            the coreError to set
	 */
	public void setCoreError(String coreError)
	{
		this.coreError = coreError;
	}

	/**
	 * Gets coreError
	 * 
	 * @return coreError the coreError
	 */
	public String getCoreError()
	{
		return coreError;
	}

	/**
	 * Sets resultError
	 * 
	 * @param resultError
	 *            the resultError to set
	 */
	public void setResultError(String resultError)
	{
		this.resultError = resultError;
	}

	/**
	 * Gets resultError
	 * 
	 * @return resultError the resultError
	 */
	public String getResultError()
	{
		return resultError;
	}

	/**
	 * Sets implementationError
	 * 
	 * @param implementationError
	 *            the implementationError to set
	 */
	public void setImplementationError(String implementationError)
	{
		this.implementationError = implementationError;
	}

	/**
	 * Gets implementationError
	 * 
	 * @return implementationError the implementationError
	 */
	public String getImplementationError()
	{
		return implementationError;
	}

	/**
	 * Sets employmentError
	 * 
	 * @param employmentError
	 *            the employmentError to set
	 */
	public void setEmploymentError(String employmentError)
	{
		this.employmentError = employmentError;
	}

	/**
	 * Gets employmentError
	 * 
	 * @return employmentError the employmentError
	 */
	public String getEmploymentError()
	{
		return employmentError;
	}

	public void setSelectedRowIndexEmployment(Long selectedRowIndexEmployment)
	{
		this.getViewState().put("selectedRowIndexEmployment",
				selectedRowIndexEmployment);
	}

	public Long getSelectedRowIndexEmployment()
	{
		return (Long) this.getViewState().get("selectedRowIndexEmployment");
	}

	/**
	 * Sets coreStyle
	 * 
	 * @param coreStyle
	 *            the coreStyle to set
	 */
	public void setCoreStyle(String coreStyle)
	{
		this.coreStyle = coreStyle;
	}

	/**
	 * Gets coreStyle
	 * 
	 * @return coreStyle the coreStyle
	 */
	public String getCoreStyle()
	{
		return coreStyle;
	}

	/**
	 * Sets employmentStyle
	 * 
	 * @param employmentStyle
	 *            the employmentStyle to set
	 */
	public void setEmploymentStyle(String employmentStyle)
	{
		this.employmentStyle = employmentStyle;
	}

	/**
	 * Gets employmentStyle
	 * 
	 * @return employmentStyle the employmentStyle
	 */
	public String getEmploymentStyle()
	{
		return employmentStyle;
	}

	/**
	 * Sets ImplementationStyle
	 * 
	 * @param implementationStyle
	 *            the implementationStyle to set
	 */
	public void setImplementationStyle(String implementationStyle)
	{
		this.implementationStyle = implementationStyle;
	}

	/**
	 * Gets ImplementationStyle
	 * 
	 * @return ImplementationStyle the implementationStyle
	 */
	public String getImplementationStyle()
	{
		return implementationStyle;
	}

	/**
	 * Sets qsnStyle
	 * 
	 * @param qsnStyle
	 *            the qsnStyle to set
	 */
	public void setQsnStyle(String qsnStyle)
	{
		this.qsnStyle = qsnStyle;
	}

	/**
	 * Gets qsnStyle
	 * 
	 * @return qsnStyle the qsnStyle
	 */
	public String getQsnStyle()
	{
		return qsnStyle;
	}

	/**
	 * Sets resultStyle
	 * 
	 * @param resultStyle
	 *            the resultStyle to set
	 */
	public void setResultStyle(String resultStyle)
	{
		this.resultStyle = resultStyle;
	}

	/**
	 * Gets resultStyle
	 * 
	 * @return resultStyle the resultStyle
	 */
	public String getResultStyle()
	{
		return resultStyle;
	}

	/**
	 * Sets selectedIndex
	 * 
	 * @param selectedIndex
	 *            the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex)
	{
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Gets selectedIndex
	 * 
	 * @return selectedIndex the selectedIndex
	 */
	public int getSelectedIndex()
	{
		return selectedIndex;
	}

}
