/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import com.infostroy.paamns.persistence.beans.entities.domain.Projects;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ProjectsWrapper extends Projects
{
    private boolean editable;
    
    public ProjectsWrapper(Projects p)
    {
        this.setActivationProcedure(p.getActivationProcedure());
        this.setAsse(p.getAsse());
        this.setCode(p.getCode() == null ? "" : p.getCode());
        this.setCpt(p.getCpt());
        this.setCreateDate(p.getCreateDate());
        this.setCup(p.getCup());
        this.setDeleted(p.getDeleted());
        this.setId(p.getId());
        this.setPrioritaryReason(p.getPrioritaryReason());
        this.setQsnGoal(p.getQsnGoal());
        this.setSpecificGoal(p.getSpecificGoal());
        this.setState(p.getState());
        this.setTitle(p.getTitle());
        this.setTypology(p.getTypology());
        this.setCupCode(p.getCupCode());
        this.setLocked(p.getLocked());
    }
    
    public ProjectsWrapper(Projects p, boolean editable)
    {
        this.setActivationProcedure(p.getActivationProcedure());
        this.setAsse(p.getAsse());
        this.setCode(p.getCode());
        this.setCpt(p.getCpt());
        this.setCreateDate(p.getCreateDate());
        this.setCup(p.getCup());
        this.setDeleted(p.getDeleted());
        this.setId(p.getId());
        this.setPrioritaryReason(p.getPrioritaryReason());
        this.setQsnGoal(p.getQsnGoal());
        this.setSpecificGoal(p.getSpecificGoal());
        this.setState(p.getState());
        this.setTitle(p.getTitle());
        this.setTypology(p.getTypology());
        this.setEditable(editable);
        this.setCupCode(p.getCupCode());
        this.setLocked(p.getLocked());
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
