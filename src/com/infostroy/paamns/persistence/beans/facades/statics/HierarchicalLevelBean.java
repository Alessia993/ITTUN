package com.infostroy.paamns.persistence.beans.facades.statics;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.LocalizableStaticEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.HierarchicalLevel;

public class HierarchicalLevelBean extends LocalizableStaticEntityBean<HierarchicalLevel>{

	
	 @SuppressWarnings("unchecked")
	    public List<HierarchicalLevel> GetAll()
	            throws PersistenceBeanException
	    {
	        Criteria criterion = PersistenceSessionManager.getBean().getSession()
	                .createCriteria(HierarchicalLevel.class);
	        
	        criterion.add(Restrictions.eq("deleted", false));
	         
	        return criterion.list();
	    }
}
