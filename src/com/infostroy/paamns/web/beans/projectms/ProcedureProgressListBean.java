package com.infostroy.paamns.web.beans.projectms;

import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityListBean;

public class ProcedureProgressListBean extends EntityListBean<Procedures>
{
    private boolean editable;
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSession().get("project") != null)
        {
            Projects project = BeansFactory.Projects().Load(
                    this.getSession().get("project").toString());
            
            List<Procedures> lst = BeansFactory.Procedures().LoadLastByProject(
                    this.getSession().get("project").toString());
            
            if (lst.size() == 0)
            {
                // Create default
                Procedures step1 = new Procedures();
                step1.setProject(project);
                step1.setStep(1);
                step1.setDeleted(false);
                step1.setCreateDate(DateTime.getNow());
                step1.setVersion(1l);
                step1.setDescription(Utils
                        .getString("procedureInsertStep1Description"));
                
                BeansFactory.Procedures().Save(step1);
                
                Procedures step2 = new Procedures();
                step2.setProject(project);
                step2.setStep(2);
                step2.setDeleted(false);
                step2.setCreateDate(DateTime.getNow());
                step2.setVersion(1l);
                step2.setDescription(Utils
                        .getString("procedureInsertStep2Description"));
                
                BeansFactory.Procedures().Save(step2);
                
                Procedures step3 = new Procedures();
                step3.setProject(project);
                step3.setStep(3);
                step3.setDeleted(false);
                step3.setCreateDate(DateTime.getNow());
                step3.setVersion(1l);
                step3.setDescription(Utils
                        .getString("procedureInsertStep3Description"));
                
                BeansFactory.Procedures().Save(step3);
                
                Procedures step4 = new Procedures();
                step4.setProject(project);
                step4.setStep(4);
                step4.setDeleted(false);
                step4.setCreateDate(DateTime.getNow());
                step4.setVersion(1l);
                step4.setDescription(Utils
                        .getString("procedureInsertStep4Description"));
                
                BeansFactory.Procedures().Save(step4);
                
                lst = BeansFactory.Procedures().LoadLastByProject(
                        this.getSession().get("project").toString());
            }
            
            for (Procedures p : lst)
            {
                if (p.getVersion() == null)
                {
                    p.setVersion(1l);
                    
                    BeansFactory.Procedures().Save(p);
                }
            }
            
            for (Procedures p : lst)
            {
                if (p.getProgressValidationObject() != null)
                {
                    this.setEditable(false);
                    break;
                }
                else
                {
                    this.setEditable(true);
                    break;
                }
            }
            
            this.setList(lst);
        }
    }
    
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void editEntity()
    {
        this.getSession().put("procedure", this.getEntityEditId());
        this.getSession().put("FromProgressValidationProcedureProgress", null);
        
        this.goTo(PagesTypes.PROCEDUREPROGRESSEDIT);
    }
    
    public void editNotes()
    {
        this.getSession().put("procedure", this.getEntityEditId());
        this.getSession().put("isProcedureProgress", true);
        this.goTo(PagesTypes.PROCEDUREINSERTNOTESLIST);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    /**
     * Sets editable
     * 
     * @param editable
     *            the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    /**
     * Gets editable
     * 
     * @return editable the editable
     */
    public boolean isEditable()
    {
        return editable;
    }
}
