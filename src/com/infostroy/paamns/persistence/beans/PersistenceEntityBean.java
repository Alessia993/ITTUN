/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

import java.io.IOException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

/**
 * 
 * @author Alexander Chelombitko
 * @author Sergey Manoylo InfoStroy Co., 2009, 2010.
 * @param <T>
 * 
 */
public class PersistenceEntityBean<T extends PersistentEntity> extends
        EntityBean<T>
{
    
    /**
     * @return
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public List<T> Load() throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass())
                .add(Restrictions.eq("deleted", false));
        List<T> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public Long getMaxId() throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(
                        BeansFactory.getByType((Object) this).getClass()).setProjection(Projections.max("id"));

        return (Long)criterion.uniqueResult();
    }
    
    /**
     * 
     * @param <T>
     * @param obj
     * @return
     * @throws IOException
     * @throws PersistenceBeanException
     */
    /*
     * @SuppressWarnings("unchecked") static public <T> List<T> Load(Class<T>
     * entityClass) throws IOException, PersistenceBeanException { Criteria
     * criterion = PersistenceSessionManager.getBean().getSession()
     * .createCriteria(entityClass).add( Restrictions.eq("deleted", false));
     * List<T> list = criterion.list(); return list; }
     */
    
    /**
     * @param id
     * @return
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public T Load(long id) throws PersistenceBeanException, HibernateException,
            PersistenceBeanException
    {
        Object item = PersistenceSessionManager.getBean().getSession()
                .get(BeansFactory.getByType((Object) this).getClass(), id);
        return (T) item;
    }
    
    /**
     * @param id
     * @return
     * @throws IOException
     * @throws PersistenceBeanException
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    static public <T> T Load(long id, Class<T> entityClass) throws IOException,
            HibernateException, PersistenceBeanException
    {
        Object item = PersistenceSessionManager.getBean().getSession()
                .get(entityClass, id);
        return (T) item;
    }
    
    /**
     * @param id
     * @return
     * @throws IOException
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     */
    public T Load(String id) throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        return this.Load(Long.valueOf(id));
    }
    
    /**
     * @param id
     * @return
     * @throws IOException
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    static public <T> T Load(String id, Class<T> entityClass)
            throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        Object item = PersistenceSessionManager.getBean().getSession()
                .get(entityClass, Long.parseLong(id));
        return (T) item;
    }
    
    /**
     * 
     * @throws HibernateException
     * @throws PersistenceBeanException
     * @throws IOException
     */
    public void DeleteAll() throws HibernateException, PersistenceBeanException
    {
        // Empty for base class
        // Override this method if necessary within child
    }
    
    /**
     * @param id
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws IOException
     */
    public void Delete(long id) throws PersistenceBeanException,
            HibernateException, PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        DeleteInTransaction(id);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * 
     * @param id
     * @throws IOException
     * @throws HibernateException
     * @throws NumberFormatException
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     */
    public void Delete(String id) throws NumberFormatException,
            HibernateException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.Delete(Long.parseLong(id));
    }
    
    /**
     * @param id
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     * @throws HibernateException
     * @throws IOException
     */
    public void DeleteInTransaction(long id) throws PersistenceBeanException,
            HibernateException, PersistenceBeanException
    {
        PersistentEntity item = (PersistentEntity) Load(id);
        if (item != null)
        {
            item.setDeleted(true);
            PersistenceSessionManager.getBean().getSession().update(item);
        }
    }
    
    /**
     * @param id
     * @throws IOException
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
     * 
     */
    @Override
    public void Insert(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        InsertInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void Save(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        SaveInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    public void PermSave(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        PermSaveInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void Update(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        UpdateInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void Delete(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        DeleteInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * 
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public void PermenentlyDelete(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        Transaction tr = PersistenceSessionManager.getBean().getSession()
                .beginTransaction();
        PermenentlyDeleteInTransaction(objEntity);

		if (tr != null)
		{
			tr.commit();
		}
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    public void Merge(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        if (ValidatorHelper.validate(this))
        {
            Transaction tr = PersistenceSessionManager.getBean().getSession()
                    .beginTransaction();
            PersistenceSessionManager.getBean().getSession().merge(objEntity);

    		if (tr != null)
    		{
    			tr.commit();
    		}
        }
    }
    
	public void MergeInTransaction(T objEntity)
			throws HibernateException, PersistenceBeanException
	{
		if (ValidatorHelper.validate(this))
		{
			PersistenceSessionManager.getBean().getSession().merge(objEntity);
		}
	}
    
    /**
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public void PermenentlyDeleteInTransaction(T objEntity)
            throws HibernateException, PersistenceBeanException
    {
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().delete(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void DeleteInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PreDelete(objEntity);
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().update(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void InsertInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PreSave(objEntity);
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().save(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void SaveInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PreSave(objEntity);
        if (ValidatorHelper.validate(objEntity))
        {
            if (PersistenceSessionManager.getBean().getSession()
                    .contains(objEntity))
            {
                PersistenceSessionManager.getBean().getSession()
                        .merge(objEntity);
            }
            else
			{
				try
				{
					PersistenceSessionManager.getBean().getSession()
							.saveOrUpdate(objEntity);
				}
				catch (NonUniqueObjectException e)
				{
					PersistenceSessionManager.getBean().getSession()
							.merge(objEntity);
				}
			}
        }
    }
    
    public void PermSaveInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PreSave(objEntity);
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().save(objEntity);
        }
    }
    
    /**
     * @throws PersistenceBeanException
     * @throws HibernateException
     * 
     */
    @Override
    public void UpdateInTransaction(T objEntity) throws HibernateException,
            PersistenceBeanException
    {
        PreSave(objEntity);
        if (ValidatorHelper.validate(objEntity))
        {
            PersistenceSessionManager.getBean().getSession().update(objEntity);
        }
    }
    
    /**
     * @param entity
     */
    private void PreSave(T objEntity)
    {
        objEntity.setDeleted(false);
        if (objEntity.getCreateDate() == null)
        {
            objEntity.setCreateDate(DateTime.getNow());
        }
    }
    
    /**
     * @param entity
     */
    private void PreDelete(T objEntity)
    {
        objEntity.setDeleted(true);
    }
    
}
