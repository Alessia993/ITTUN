/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;
import com.infostroy.paamns.web.beans.EntityViewBean;
import com.infostroy.paamns.web.beans.wrappers.ProgrammRealizationIndicatorWrapper;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ProgramRealizationIndicatorViewBean extends
        EntityViewBean<ProgramIndicators>
{
    
	private List<ProgrammRealizationIndicatorWrapper> listWrappers;
	
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(BeansFactory.ProgramRealizationIndicators().Load());
        this.setListWrappers(new ArrayList<ProgrammRealizationIndicatorWrapper>());
        for(ProgramIndicators indic : this.getList()){
        	this.getListWrappers().add(new ProgrammRealizationIndicatorWrapper().wrap(indic));
        }
        
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
    
	public List<ProgrammRealizationIndicatorWrapper> getListWrappers()
	{
		return listWrappers;
	}

	public void setListWrappers(
			List<ProgrammRealizationIndicatorWrapper> listWrappers)
	{
		this.listWrappers = listWrappers;
	}
    
}
