/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public enum CostItemTypes
{
    HumanResources(1l),
    ProvisionOfService(2l),
    Missions(3l),
    DurablesInfrastructure(4l),
    DurableGoods(5l),
    AdvInfoPubEvents(6l),
    AdvInfoProducts(7l),
    Overheads(8l),
    GeneralCosts(9l),
    Other(10l);
    
    private CostItemTypes(Long val)
    {
        this.value = val;
    }
    
    /**
     * Sets value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(Long value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * 
     * @return value the value
     */
    public Long getValue()
    {
        return value;
    }
    
    private Long value;
}
