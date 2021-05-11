/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2013.
 *
 */
public enum FesrQuotaTypes
{
    QuotaContoCapitale(1l), QuotaContoCorrente(2l);
    
    private FesrQuotaTypes(Long id)
    {
        this.id = id;
    }
    
    private Long id;
    
    /**
     * Gets id
     * @return id the id
     */
    public Long getId()
    {
        return id;
    }
    
    /**
     * Sets id
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
}
