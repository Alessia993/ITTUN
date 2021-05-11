package com.infostroy.paamns.web.beans.projectms;

import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectIter;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * modified by Ingloba360
 *
 */
public class ProcedureInsertListBean extends EntityListBean<Procedures> {
	
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException, PersistenceBeanException {
        if (this.getSession().get("project") != null) {
            Projects project = BeansFactory.Projects().Load(this.getSession().get("project").toString());
            
            List<Procedures> procedureList = BeansFactory.Procedures().LoadLastByProject(this.getSession().get("project").toString());
            
            if (procedureList.size() == 0) {
                // Create default
            	
            	for(ProjectIter projectIter : ProjectIter.values()) {
            		Procedures procedure = new Procedures();
            		procedure.setProject(project);
            		procedure.setStep(projectIter.code);
            		procedure.setDescription(Utils.getString(projectIter.description));
            		procedure.setCodPhase(projectIter.codPhase);
            		
                    BeansFactory.Procedures().Save(procedure);
            	}
                
                procedureList = BeansFactory.Procedures().LoadLastByProject(this.getSession().get("project").toString());
            } else {
            	//check cod phase on all elements
            	
            	boolean changed = false;
            	for (Procedures procedure : procedureList) {
            		if ((procedure.getCodPhase() == null) || procedure.getCodPhase().isEmpty()) {
            			for (ProjectIter projectIter : ProjectIter.values()) {
            				if (procedure.getStep().equals(projectIter.code)) {
            					procedure.setCodPhase(projectIter.codPhase);
            					BeansFactory.Procedures().Save(procedure);
            					
            					changed = true;
            					break;
            				}
            			}
            		}
            	}
            	
            	if (changed) {
            		procedureList = BeansFactory.Procedures().LoadLastByProject(this.getSession().get("project").toString());
            	}
            }
            
            this.setList(procedureList);
        }
    }
    
    @Override
    public void addEntity() {}
    
    @Override
    public void deleteEntity() {}
    
    @Override
    public void editEntity()
    {
        this.getSession().put("procedure", this.getEntityEditId());
        
        this.goTo(PagesTypes.PROCEDUREINSERTEDIT);
    }
    
    public void editNotes()
    {
        this.getSession().put("procedure", this.getEntityEditId());
        this.getSession().put("isProcedureProgress", false);
        this.goTo(PagesTypes.PROCEDUREINSERTNOTESLIST);
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException, PersistenceBeanException {}
}
