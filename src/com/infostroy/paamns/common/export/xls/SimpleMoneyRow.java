/**
 * 
 */
package com.infostroy.paamns.common.export.xls;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;

/**
 *
 * @author Dmitry Pogorelov
 * InfoStroy Co., 2012.
 *
 */
public class SimpleMoneyRow
{
    private String title;
    
    private Double value;
    
    /**
     * Gets title
     * @return title the title
     */
    @Export(propertyName = "exportSimpleTitle", seqXLS = 0, type = FieldTypes.STRING, place = ExportPlaces.DurSummary)
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Sets title
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /**
     * @param title
     * @param value
     */
    public SimpleMoneyRow(String title, Double value)
    {
        super();
        this.title = title;
        this.value = value;
    }
    
    /**
     * Gets value
     * @return value the value
     */
    @Export(propertyName = "exportSimpleValue", seqXLS = 1, type = FieldTypes.MONEY, place = ExportPlaces.DurSummary)
    public Double getValue()
    {
        return value;
    }
    
    /**
     * Sets value
     * @param value the value to set
     */
    public void setValue(Double value)
    {
        this.value = value;
    }
}
