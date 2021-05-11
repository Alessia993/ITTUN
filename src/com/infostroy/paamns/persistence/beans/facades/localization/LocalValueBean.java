/**
 * 
 */
package com.infostroy.paamns.persistence.beans.facades.localization;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.localization.LocalValue;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2010.
 * 
 */
public class LocalValueBean extends PersistenceEntityBean<LocalValue>
{
    /**
     * 
     * @author Sergey Manoylo InfoStroy Co., 2010.
     * 
     */
    public static class LocalValueBeanParams
    {
        private String _localId;
        
        private String _entityId;
        
        private String _locale;
        
        private String _value;
        
        /**
         * 
         * @param localId
         */
        public void setLocalId(String localId)
        {
            _localId = localId;
        }
        
        /**
         * 
         * @return
         */
        public String getLocalId()
        {
            return _localId;
        }
        
        /**
         * 
         * @param entityId
         */
        public void setEntityId(String entityId)
        {
            _entityId = entityId;
        }
        
        /**
         * 
         * @return
         */
        public String getEntityId()
        {
            return _entityId;
        }
        
        /**
         * 
         * @param locale
         */
        public void setLocale(String locale)
        {
            _locale = locale;
        }
        
        /**
         * 
         * @return
         */
        public String getLocale()
        {
            return _locale;
        }
        
        /**
         * 
         * @param value
         */
        public void setValue(String value)
        {
            _value = value;
        }
        
        /**
         * 
         * @return
         */
        public String getValue()
        {
            return _value;
        }
    }
    
    /**
     * 
     * @return
     */
    public LocalValueBeanParams createLocalValueBeanParams()
    {
        return new LocalValueBeanParams();
    }
    
    /**
     * 
     * 
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public String load(LocalValueBeanParams localParams)
            throws HibernateException, PersistenceBeanException
    {
        if (localParams == null)
        {
            throw new PersistenceBeanException(
                    "LocalValueBean.load()... parameter is wrong...");
        }
        
        Criteria loadCriteria = PersistenceSessionManager.getInstance()
                .getInstanceBean().getSession()
                .createCriteria(LocalValue.class);
        
        if (localParams.getLocalId() != null
                && !localParams.getLocalId().isEmpty())
        {
            loadCriteria.add(Restrictions.like("localId",
                    localParams.getLocalId()));
        }
        if (localParams.getEntityId() != null
                && !localParams.getEntityId().isEmpty())
        {
            loadCriteria.add(Restrictions.like("entityId",
                    localParams.getEntityId()));
        }
        if (localParams.getLocale() != null
                && !localParams.getLocale().isEmpty())
        {
            loadCriteria.add(Restrictions.like("locale",
                    localParams.getLocale()));
        }
        
        List<LocalValue> list = loadCriteria.list();
        
        if (list != null && list.size() != 0)
        {
            return list.get(0).getValue();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 
     * @param localParams
     * @return
     * @throws HibernateException
     * @throws PersistenceBeanException
     */
    @SuppressWarnings("unchecked")
    public String set(LocalValueBeanParams localParams)
            throws HibernateException, PersistenceBeanException
    {
        if (localParams == null)
        {
            throw new PersistenceBeanException(
                    "LocalValueBean.load()... parameter is wrong...");
        }
        
        boolean isNew = localParams.getLocalId() == null
                || localParams.getLocalId().isEmpty();
        
        if (isNew)
        {
            LocalValue lv = new LocalValue();
            lv.setCreateDate(new Date());
            lv.setDeleted(false);
            lv.setLocalId(UUID.randomUUID().toString());
            lv.setEntityId(localParams.getEntityId());
            lv.setDeleted(false);
            lv.setLocale(localParams.getLocale());
            
            if (localParams.getValue() != null)
            {
                lv.setValue(localParams.getValue());
            }
            else
            {
                lv.setValue("");
            }
            
            PersistenceSessionManager.getInstance().getInstanceBean()
                    .getSession().save(lv);
            
            return lv.getLocalId();
        }
        
        Criteria loadCriteria = PersistenceSessionManager.getInstance()
                .getInstanceBean().getSession()
                .createCriteria(LocalValue.class);
        
        if (!isNew)
        {
            loadCriteria.add(Restrictions.eq("localId",
                    localParams.getLocalId()));
        }
        if (localParams.getEntityId() != null
                && !localParams.getEntityId().isEmpty())
        {
            loadCriteria.add(Restrictions.eq("entityId",
                    localParams.getEntityId()));
        }
        if (localParams.getLocale() != null
                && !localParams.getLocale().isEmpty())
        {
            loadCriteria
                    .add(Restrictions.eq("locale", localParams.getLocale()));
        }
        
        List<LocalValue> list = loadCriteria.list();
        
        if (list != null && list.size() != 0
                && list.get(0).getValue().equals(localParams.getValue()))
        {
            LocalValue lv = list.get(0);
            return lv.getLocalId();
        }
        else if (list != null && list.size() != 0
                && !list.get(0).getValue().equals(localParams.getValue()))
        {
            LocalValue lv = list.get(0);
            
            lv.setValue(localParams.getValue());
            
            PersistenceSessionManager.getInstance().getInstanceBean()
                    .getSession().save(lv);
            return lv.getLocalId();
        }
        else
        {
            LocalValue lv = new LocalValue();
            lv.setCreateDate(new Date());
            lv.setDeleted(false);
            
            if (localParams.getLocalId() != null
                    && !localParams.getLocalId().isEmpty())
            {
                lv.setLocalId(localParams.getLocalId());
            }
            else
            {
                lv.setLocalId(UUID.randomUUID().toString());
            }
            lv.setEntityId(localParams.getEntityId());
            lv.setDeleted(false);
            lv.setLocale(localParams.getLocale());
            
            if (localParams.getValue() != null)
            {
                lv.setValue(localParams.getValue());
            }
            else
            {
                lv.setValue("");
            }
            
            PersistenceSessionManager.getInstance().getInstanceBean()
                    .getSession().save(lv);
            
            return lv.getLocalId();
        }
    }
    
}
