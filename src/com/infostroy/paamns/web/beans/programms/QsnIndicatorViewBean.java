/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;


import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QsnIndicators;
import com.infostroy.paamns.web.beans.EntityViewBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class QsnIndicatorViewBean extends EntityViewBean<QsnIndicators>
{
	/*private List<ProgramIndicators> italyMalta;
	private List<ProgramIndicators> qsnItaly;
	private List<ProgramIndicators> nsrfMalta;

	private List<QsnIndicatorViewBean> indicatorsData;
*/	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
	 */
	@Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
		/*setItalyMalta(BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.ItalyMalta));
		setQsnItaly(BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.QsnItaly));
		setNsrfMalta(BeansFactory.ProgramIndicators().Load(ProgramIndicatorsType.NsrfMalta));
		this.setIndicatorsData(new ArrayList<QsnIndicatorViewBean>());
		this.getIndicatorsData().add(this);
    */
		setList(BeansFactory.QsnIndicators().Load());
		}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		// TODO Auto-generated method stub
	}
	
	

	/**
	 * Gets italyMalta
	 * @return italyMalta the italyMalta
	 *//*
	public List<ProgramIndicators> getItalyMalta()
	{
		return italyMalta;
	}

	*//**
	 * Sets italyMalta
	 * @param italyMalta the italyMalta to set
	 *//*
	public void setItalyMalta(List<ProgramIndicators> italyMalta)
	{
		this.italyMalta = italyMalta;
	}

	*//**
	 * Gets qsnItaly
	 * @return qsnItaly the qsnItaly
	 *//*
	public List<ProgramIndicators> getQsnItaly()
	{
		return qsnItaly;
	}

	*//**
	 * Sets qsnItaly
	 * @param qsnItaly the qsnItaly to set
	 *//*
	public void setQsnItaly(List<ProgramIndicators> qsnItaly)
	{
		this.qsnItaly = qsnItaly;
	}

	*//**
	 * Gets nsrfMalta
	 * @return nsrfMalta the nsrfMalta
	 *//*
	public List<ProgramIndicators> getNsrfMalta()
	{
		return nsrfMalta;
	}

	*//**
	 * Sets nsrfMalta
	 * @param nsrfMalta the nsrfMalta to set
	 *//*
	public void setNsrfMalta(List<ProgramIndicators> nsrfMalta)
	{
		this.nsrfMalta = nsrfMalta;
	}

	*//**
	 * Gets indicatorsData
	 * @return indicatorsData the indicatorsData
	 *//*
	public List<QsnIndicatorViewBean> getIndicatorsData()
	{
		return indicatorsData;
	}

	*//**
	 * Sets indicatorsData
	 * @param indicatorsData the indicatorsData to set
	 *//*
	public void setIndicatorsData(List<QsnIndicatorViewBean> indicatorsData)
	{
		this.indicatorsData = indicatorsData;
	}*/
	
}
