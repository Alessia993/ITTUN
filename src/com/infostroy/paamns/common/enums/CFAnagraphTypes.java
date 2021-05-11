package com.infostroy.paamns.common.enums;

public enum CFAnagraphTypes
{
    CFAnagraph(1l), PartnerAnagraph(2l);
    
    private CFAnagraphTypes(Long value)
    {
        this.setCfType(value);
    }
    
    /**
     * Sets cfType
     * @param cfType the cfType to set
     */
    public void setCfType(Long cfType)
    {
        this.cfType = cfType;
    }
    
    /**
     * Gets cfType
     * @return cfType the cfType
     */
    public Long getCfType()
    {
        return cfType;
    }
    
    private Long cfType;
}
