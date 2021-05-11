/**
 * 
 */
package com.infostroy.paamns.persistence.beans.proxy.ifc;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public interface ProxyInterface
{
    
    /**
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public String load(LocalParams localParams) throws HibernateException,
            PersistenceBeanException;
    
    /**
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    public String set(LocalParams localParams) throws HibernateException,
            PersistenceBeanException;
}
