package com.infostroy.paamns.persistence.beans;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.entities.Enumeration;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * @author Sergey Manoylo InfoStroy Co., 2010.
 * @param <T>
 * 
 */
public class EnumerationBean<T extends Enumeration>
{
    /**
     * @return
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
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
                .addOrder(Order.asc("value"));
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
    public T Load(long id) throws HibernateException, PersistenceBeanException,
            PersistenceBeanException
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
            PersistenceBeanException, PersistenceBeanException
    {
        return this.Load(Long.valueOf(id));
    }
    
    /**
     * @param id
     * @throws HibernateException
     * @throws PersistenceBeanException
     * @throws PersistenceBeanException
     */
    public void Delete(long id) throws HibernateException,
            PersistenceBeanException, PersistenceBeanException
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
            HibernateException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.Delete(Long.parseLong(id));
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
        Enumeration item = (Enumeration) Load(id);
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
    
}
