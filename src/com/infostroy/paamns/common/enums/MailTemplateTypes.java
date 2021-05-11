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
public enum MailTemplateTypes
{
    newUser("newUser", 1l),
    newProjectUser("newProjectUser", 2l),
    projectInformationCompletation("projectInformationCompletation", 3l),
    localization("localization", 4l),
    cFandPartner("cFandPartner", 5l),
    budgetChanged("budgetChanged", 6l),
    budgetRefused("budgetRefused", 7l),
    budgetApproved("budgetApproved", 8l),
    managmentDurCFSend("managmentDurCFSend", 9l),
    managmentDurSTCApprove("managmentDurSTCApprove", 10l),
    managmentDurSTCRefuse("managmentDurSTCRefuse", 11l),
    managmentDurAGUAccept("managmentDurAGUAccept", 12l),
    managmentDurACUAccept("managmentDurACUAccept", 13l),
    validationFlowSFSend("validationFlowSFSend", 14l),
    validationFlowSTCSend("validationFlowSTCSend", 15l),
    validationFlowAGUConfirm("validationFlowAGUConfirm", 16l);
    
    private String value;
    
    private Long   id;
    
    private MailTemplateTypes(String value, Long id)
    {
        this.setValue(value);
        this.setId(id);
    }
    
    public static MailTemplateTypes getById(Long id)
    {
        for (MailTemplateTypes type : MailTemplateTypes.values())
        {
            if (type.getId().equals(id))
            {
                return type;
            }
        }
        
        return null;
    }
    
    /**
     * Sets value
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    /**
     * Gets value
     * @return value the value
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * Sets id
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * Gets id
     * @return id the id
     */
    public Long getId()
    {
        return id;
    }
}
