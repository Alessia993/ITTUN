/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.LocalizableEnumerationBean;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class ProgramIndicatorsBean<T extends ProgramIndicators> extends
        LocalizableEnumerationBean<T>
{
    /**
     * @return
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     * @throws IOException 
     */
	@SuppressWarnings("unchecked")
	public List<T> Load(ProgramIndicatorsType type)
			throws PersistenceBeanException, PersistenceBeanException
	{
		Criteria criterion = PersistenceSessionManager
				.getBean()
				.getSession()
				.createCriteria(
						BeansFactory.getByType((Object) this).getClass())
				.add(Restrictions.eq("type", type.getCode()))
				.add(Restrictions.eq("deleted", false));
		return criterion.list();
	}
    
    /**
    * @return
    * @throws PersistenceBeanException 
    * @throws PersistenceBeanException 
    * @throws IOException 
    */
    public static ProgramIndicators Load(Long id)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(ProgramIndicators.class)
                .add(Restrictions.eq("id", id))
                .add(Restrictions.eq("deleted", false));
        ProgramIndicators list = (ProgramIndicators) criterion.uniqueResult();        
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> Load(ProgramIndicatorsType type, String asse)
            throws PersistenceBeanException, PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass())
                .add(Restrictions.eq("type", type.getCode()))
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("asse", asse));
        List<T> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> LoadByProgramIndicatorAxis(ProgramIndicatorsType type, String asse)
            throws PersistenceBeanException, PersistenceBeanException
    {
      
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT program_indicators.*, local_values.value ");
        sb.append("from program_indicators ");
        sb.append("left join local_values on local_values.local_id=program_indicators.asse ");
        sb.append("where "); 
        sb.append("local_values.locale=:locale and "); //locale='it'-'en'-'fr'
        sb.append("program_indicators.type=:type and ");//type=0
        sb.append("local_values.value=:axis_value");//axis_value=1
        Query q = PersistenceSessionManager.getBean().getSession()
				.createSQLQuery(sb.toString()).addEntity(ProgramIndicators.class);
        q.setString("locale", LocaleSessionManager.getInstance().getInstanceBean().getCurrLocale().toString());
        q.setInteger("type", type.getCode());
        q.setString("axis_value", asse);
        return q.list();
    }
    
    /**
     * 
     * @param <T>
     * @param obj
     * @return
     * @throws IOException
     * @throws PersistenceBeanException 
     */
    @SuppressWarnings("unchecked")
    static public <T> List<T> Load(Class<T> entityClass,
            ProgramIndicatorsType type) throws IOException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager.getBean().getSession()
                .createCriteria(entityClass)
                .add(Restrictions.eq("type", type.getCode()))
                .add(Restrictions.eq("deleted", false));
        List<T> list = criterion.list();
        return list;
    }
    
    /**
     * @param projectId
     * @param type
     * @return
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public List<ProgramIndicators> GetByProject(String projectId,
            ProgramIndicatorsType type) throws PersistenceBeanException,
            HibernateException, PersistenceBeanException
    {
        Criterion cr = null;
        
        if (type == ProgramIndicatorsType.Realization)
        {
            cr = Restrictions.in("id", BeansFactory
                    .PhisicalInizializationToProgramRealizationIndicators()
                    .GetByProject(projectId));
        }
        else if (type == ProgramIndicatorsType.Result)
        {
            cr = Restrictions.in("id", BeansFactory
                    .PhisicalInizializationToProgramResultIndicators()
                    .GetByProject(projectId));
        }
        
        Restrictions.in("id", BeansFactory
                .PhisicalInizializationToProgramRealizationIndicators()
                .GetByProject(projectId));
        
        List<ProgramIndicators> list = PersistenceSessionManager.getBean()
                .getSession().createCriteria(ProgramIndicators.class)
                .add(Restrictions.eq("type", type.getCode())).add(cr).list();
        return list;
    }
    
}
