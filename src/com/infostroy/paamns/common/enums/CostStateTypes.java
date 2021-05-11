/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public enum CostStateTypes
{
    NotYetValidate(1l),
    DAECVerify(2l),
    CILVerify(3l),
    DAECApproval(4l),
    CILValidate(5l),
    CFValidation(6l),
    CFValidate(7l),
    FullValidate(8l),
    RefusedCIL(9l),
    RefusedDAEC(10l),
    Dismiss(11l),
    Closed(12l),
    Draft(13l);
    
    private CostStateTypes(Long value)
    {
        this.setState(value);
    }
    
    /**
     * @param state
     * @return
     */
    public static CostStateTypes getByState(Long state)
    {
        for (CostStateTypes item : CostStateTypes.values())
        {
            if (item.getState().equals(state))
            {
                return item;
            }
        }
        
        return null;
    }
    
    /**
     * Sets country
     * 
     * @param country
     *            the country to set
     */
    public void setState(Long country)
    {
        this.state = country;
    }
    
    /**
     * Gets country
     * 
     * @return country the country
     */
    public Long getState()
    {
        return state;
    }
    
    private Long state;
    
    /**
     * @param type
     * @return
     */
    public static CostStateTypes getNext(CostStateTypes type)
    {
        return CostStateTypes.getByState(type.getState() + 1);
    }
    
    public static CostStateTypes skipNext(CostStateTypes type)
    {
        return CostStateTypes.getByState(type.getState() + 2);
    }
    
    /**
     * @param type
     * @return
     */
    public static CostStateTypes getPrevious(CostStateTypes type)
    {
        return CostStateTypes.getByState(type.getState() - 1);
    }
    
    public static CostStateTypes skipPrevious(CostStateTypes type)
    {
        return CostStateTypes.getByState(type.getState() - 2);
    }
}
