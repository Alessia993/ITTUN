/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.Entity;

/**
 *
 * @author Alexander Chelombitko
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 * @param <T>
 *
 */
public class EntityBean<T extends Entity>
{
    /**
     * @return
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    @SuppressWarnings("unchecked")
    public List<T> Load() throws PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass());
        List<T> list = criterion.list();
        return list;
    }
    
    /**
     * @param id
     * @return
     * @throws HibernateException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    @SuppressWarnings("unchecked")
    public T Load(long id) throws HibernateException, PersistenceBeanException
    {
        Object item = PersistenceSessionManager.getBean().getSession()
                .get(BeansFactory.getByType((Object) this).getClass(), id);
        return (T) item;
    }
    
    /**
     * @param id
     * @return
     * @throws HibernateException 
     * @throws NumberFormatException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public T Load(String id) throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        return this.Load(Long.valueOf(id));
    }
    
    /**
     * @param id
     * @return
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public T LoadOrCreate(Long id) throws PersistenceBeanException,
            HibernateException, InstantiationException, IllegalAccessException
    {
    	Object item = PersistenceSessionManager.getBean().getSession()
                .get(BeansFactory.getByType((Object) this).getClass(), id);
        if (item == null)
        {
            item = BeansFactory.getByType((Object) this).getClass()
                    .newInstance();
        }
        
        return (T) item;
    }
    
    /**
     * @param id
     * @throws HibernateException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public void Delete(long id) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        DeleteInTransaction(id);
        tr.commit();
    }
    
    /**
     * @param id
     * @throws HibernateException 
     * @throws NumberFormatException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public void Delete(String id) throws NumberFormatException,
            HibernateException, PersistenceBeanException
    {
        this.Delete(Long.valueOf(id));
    }
    
    /**
     * @param id
     * @throws HibernateException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public void DeleteInTransaction(long id) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        Entity item = (Entity) Load(id);
        PersistenceSessionManager.getBean().getSession().delete(item);
    }
    
    /**
     * @param id
     * @throws HibernateException 
     * @throws NumberFormatException 
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     */
    public void DeleteInTransaction(String id) throws NumberFormatException,
            HibernateException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.DeleteInTransaction(Long.valueOf(id));
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    
    public void Save(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        SaveInTransaction(objEntity);
        tr.commit();
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    
    public void Update(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        UpdateInTransaction(objEntity);
        tr.commit();
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    public void Insert(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        InsertInTransaction(objEntity);
        tr.commit();
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    public void Delete(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        DeleteInTransaction(objEntity);
        tr.commit();
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    public void DeleteInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PersistenceSessionManager.getBean().getSession().delete(objEntity);
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    public void SaveInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession()
                    .saveOrUpdate(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * @throws PersistenceBeanException 
     * 
     */
    public void InsertInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().save(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws PersistenceBeanException 
     * 
     */
    public void UpdateInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().save(objEntity);
        }
    }
}
