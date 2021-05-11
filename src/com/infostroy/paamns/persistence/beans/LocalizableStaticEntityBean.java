/**
 * 
 */
package com.infostroy.paamns.persistence.beans;

/*
import java.util.List;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.exceptions.PersistentException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
*/

import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class LocalizableStaticEntityBean<T extends LocalizableStaticEntity>
        extends EntityBean<T>
{
    
    /**
     * 
     */
    /*    
        private String locale = "en";
    */
    /**
     * 
     * @param locale
     */
    /*    
        public void setLocale(String locale)
        {
            this.locale = locale;
        }
    */
    /**
     * 
     * @return
     */
    /*    
        public String getLocale()
        {
            return this.locale;
        }
    */
    /**
     * 
     * @return
     */
    /*    
        public String reinitLocale()
        {
            locale =  Locale.getDefault().toString();
            return locale;
        }
    */
    /**
     * @return
     * @throws PersistenceBeanException 
     * @throws PersistentException 
     */
    
    /*    
        @SuppressWarnings("unchecked")    public void SaveInTransaction(T objEntity) throws HibernateException,
                PersistenceBeanException
        {
            if (ValidatorHelper.validate(objEntity))
            {
                PersistenceSessionManager.getBean().getSession().saveOrUpdate(
                        objEntity);
            }
        }
        public List<T> Load() throws PersistenceBeanException, PersistentException
        {
            Criteria criterion = PersistenceSessionManager.getBean().getSession()
                    .createCriteria(
                            BeansFactory.getByType((Object) this).getClass())
                    .add(Restrictions.eq("locale", locale));
            
            System.out.println("List<T> Load() : locale : " + locale);
            
            List<T> list = criterion.list();
            return list;
        }
    */
}
