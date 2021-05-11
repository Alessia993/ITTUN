/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public enum CountryTypes
{
    France("France"), Italy("Italy");
    
    private CountryTypes(String value)
    {
        this.setCountry(value);
    }
    
    /**
     * Sets country
     * @param country the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    /**
     * Gets country
     * @return country the country
     */
    public String getCountry()
    {
        return country;
    }
    
    private String country;
}
