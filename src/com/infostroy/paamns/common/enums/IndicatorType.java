/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public enum IndicatorType
{
    QSN("indicatorsQSN"),
    CORE("coreIndicators"),
    PROGRAMRESULT("resultIndicators"),
    EMPLOYMENT("employmentIndicators"),
    PROGRAMIMPLEMENTATION("implementationIndicators");
    
    private IndicatorType(String str)
    {
        this.type = str;
    }
    
    public String getTypeName()
    {
        return type;
    }
    
    private String type;
}
