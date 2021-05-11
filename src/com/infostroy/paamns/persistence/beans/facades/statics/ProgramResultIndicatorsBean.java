/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.statics;

import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ProgramIndicatorsType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramIndicators;

/**
 * 
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2010.
 * 
 */
public class ProgramResultIndicatorsBean extends
        ProgramIndicatorsBean<ProgramIndicators>
{
    /**
     * @return
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     * @throws IOException
     */
    public List<ProgramIndicators> Load() throws PersistenceBeanException,
            PersistenceBeanException
    {
        return super.Load(ProgramIndicatorsType.Result);
    }
    
    /**
     * 
     * @param <T>
     * @param obj
     * @return
     * @throws IOException
     * @throws PersistenceBeanException
     */
    
    public static List<ProgramIndicators> Load(
            Class<ProgramIndicators> entityClass) throws IOException,
            PersistenceBeanException
    {
        return Load(entityClass, ProgramIndicatorsType.Result);
    }
    
    public List<ProgramIndicators> GetByProject(String projectId)
            throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        return GetByProject(projectId, ProgramIndicatorsType.Result);
    }
    
}
