/**
 * 
 */
package com.infostroy.paamns.web.services.base;

import org.hibernate.Session;

import com.infostroy.paamns.persistence.SessionHolder;
import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public abstract class BaseDBService extends BaseService
{
    
    /**
     * @param name
     */
    public BaseDBService(String name)
    {
        super(name);
    }
    
    /* (non-Javadoc)
     * @see it.nexera.web.services.BaseService#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        this.closeSession();
    }
    
    /**
     * 
     */
    protected void runInternal()
    {
        try
        {
            if (!stopFlag)
            {
                this.preRoutineFuncInternal();
                this.preRoutineFunc();
                this.routineFunc();
                this.postRoutineFunc();
            }
        }
        finally
        {
            try
            {
                this.postRoutineFuncInternal();
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
    }
    
    protected Session getSession()
    {
        try
        {
            return SessionHolder.getInstance().getSession();
        }
        catch(Exception e)
        {
            log.error(e);
        }
        return null;
    }
    
    protected void preRoutineFuncInternal()
    {
        if (stopFlag)
        {
            return;
        }
        
        this.updateSleepTime();
        
        this.checkSession();
    }
    
    protected final void postRoutineFuncInternal()
    {
        this.closeSession();
    }
    
    private void initSession()
    {
        SessionHolder.getInstance().openSession();
    }
    
    protected final void checkSession()
    {
        initSession();
    }
    
    protected final void closeSession()
    {
        SessionHolder.getInstance().closeSession();
    }
    
    private final void beforeSaveOrUpdate(Entity object)
    {
        //nothing do now
    }
    
    protected final void save(Entity ent) throws Exception
    {
        beforeSaveOrUpdate(ent);
        SessionHolder.getInstance().save(ent);
    }
    
    protected final void remove(Entity ent) throws Exception
    {
        SessionHolder.getInstance().remove(ent);
    }
}
