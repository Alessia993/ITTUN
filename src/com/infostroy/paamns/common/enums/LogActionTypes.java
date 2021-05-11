/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 *
 * @author Zrazhevskiy Vladimir
 * InfoStroy Co., 2011.
 *
 */
public enum LogActionTypes
{
    SendFirstFlow(1l, "confermata nel primo flusso"),
    SendSecondFlow(2l, "confermato nel secondo flusso"),
    DoFinalAction(3l, "completato l'azione finale"),
    RefuseFirstFlow(4l, "invertito nel primo flusso"),
    RefuseSecondFlow(5l, "invertito nel secondo flusso"),
    Annul(6l, "annullato"),
    SendRetificationFlow(7l, "inviare il flusso di rettifica"),
    RefuseRectificationFlow(8l, "rifiutare il flusso di rettifica"),
    AnnulRectificationFlow(9l, "rettificazione del flusso annullare");
    
    private LogActionTypes(Long id, String value)
    {
        setId(id);
        setValue(value);
    }
    
    private Long   id;
    
    private String value;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
}
