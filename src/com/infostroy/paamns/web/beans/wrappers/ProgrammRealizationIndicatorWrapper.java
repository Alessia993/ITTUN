/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class ProgrammRealizationIndicatorWrapper
{
	private ProgramIndicators indicator;
	
	private Integer projectCount;
	
	public static ProgrammRealizationIndicatorWrapper wrap(ProgramIndicators indicator) throws HibernateException, PersistenceBeanException{
		if(new Integer(ProgramIndicatorsType.Realization.getCode()).equals(indicator.getType())){
			ProgrammRealizationIndicatorWrapper wrap = new ProgrammRealizationIndicatorWrapper();
			wrap.setIndicator(indicator);
			wrap.setProjectCount((Integer)PersistenceSessionManager.getBean().getSession().createCriteria(PhisicalInitializationToProgramIndicators.class).add(Restrictions.eq("program.id", indicator.getId())).
			createAlias("indicator", "indic").createAlias("indic.programParent", "program").setProjection(Projections.rowCount()).uniqueResult());
			return wrap;
		}
		return null;
		
		
	}

	public Integer getProjectCount()
	{
		return projectCount;
	}

	public void setProjectCount(Integer projectCount)
	{
		this.projectCount = projectCount;
	}

	public ProgramIndicators getIndicator()
	{
		return indicator;
	}

	public void setIndicator(ProgramIndicators indicator)
	{
		this.indicator = indicator;
	}
}
