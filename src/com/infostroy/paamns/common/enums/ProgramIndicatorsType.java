/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public enum ProgramIndicatorsType
{
    Realization(0), Result(1),  Programma(2), ItalyTunisia(3), QsnItaly(4), NsrfTunisia(5), Communication(6), Prog(7), ResultView(8) ; 
    
    private ProgramIndicatorsType(int value)
    {
        this.setCode(value);
    }
    
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
    
    private int code;
}
