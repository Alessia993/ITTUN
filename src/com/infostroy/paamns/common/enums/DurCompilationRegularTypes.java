/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public enum DurCompilationRegularTypes
{
    Regular(1l), Irregular(2l);
    
    private DurCompilationRegularTypes(Long id)
    {
        this.setState(id);
    }
    
    /**
     * Sets state
     * 
     * @param state
     *            the state to set
     */
    public void setState(Long state)
    {
        this.state = state;
    }
    
    /**
     * Gets state
     * 
     * @return state the state
     */
    public Long getState()
    {
        return state;
    }
    
    private Long state;
    
}
