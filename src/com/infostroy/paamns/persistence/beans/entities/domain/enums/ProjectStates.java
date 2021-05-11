package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 * modified by Ingloba360
 *
 */
@Entity
@Table(name = "project_states")
public class ProjectStates extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -2853618048101993756L;
    
    @Column(name = "code")
    private int               code;
    
    @Column(name = "project_state")
    private String projectState;
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(int code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public int getCode()
    {
        return code;
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getProjectState() {
        return projectState;
    }
}
