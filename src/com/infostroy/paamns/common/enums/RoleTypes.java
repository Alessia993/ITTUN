/**
 * 
 */
package com.infostroy.paamns.common.enums;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public enum RoleTypes
{
    USER_ROLE("ROLE_USER"),
    APU_WRITE("ROLE_APU_W"),
    APU_READ("ROLE_APU_R"),
    AAU_WRITE("ROLE_AAU_W"),
    AAU_READ("ROLE_AAU_R"),
    ACU_WRITE("ROLE_ACU_W"),
    ACU_READ("ROLE_ACU_R"),
    STC_WRITE("ROLE_STC_W"),
    STC_READ("ROLE_STC_R"),
    BP_WRITE("ROLE_BP_W"),
    BP_READ("ROLE_BP_R"),
    B_WRITE("ROLE_B_W"),
    B_READ("ROLE_B_R"),
    CIL_WRITE("ROLE_CIL_W"),
    CIL_READ("ROLE_CIL_R"),
    AGU_WRITE("ROLE_AGU_W"),
    AGU_READ("ROLE_AGU_R"),
    DAEC_WRITE("ROLE_DAEC_W"),
    DAEC_READ("ROLE_DAEC_R"),
    ACUM_WRITE("ROLE_ACUM_W"),
    ACUM_READ("ROLE_ACUM_R"),
    SUPER_ADMIN_WRITE("ROLE_SUPER_ADMIN_R"),
    SUPER_ADMIN_READ("ROLE_SUPER_ADMIN_W"),
    UC_READ("ROLE_UC_R"),
    UC_WRITE("ROLE_UC_W"),
    ANCM_READ("ROLE_ANCM_R"),
    ANCM_WRITE("ROLE_ANCM_W"),
    CFP_READ("ROLE_CFP_R"),
    CFP_WRITE("ROLE_CFP_W"),
	PCI_READ("ROLE_PCI_R"),
	PCI_WRITE("ROLE_PCI_W"),
	PCT_READ("ROLE_PCT_R"),
	PCT_WRITE("ROLE_PCT_W");
    
    /**
     * 
     * @param page
     */
    private RoleTypes(String role)
    {
        this.role = role;
    }
    
    public String getRoleName()
    {
        return role;
    }
    
    public static String getRoleName(String name, Boolean isWrite)
    {
        for (RoleTypes item : RoleTypes.values())
        {
            if (isWrite != null && isWrite)
            {
                if (item.getRoleName().contains(name + "_W"))
                {
                    return item.getRoleName();
                }
            }
            else
            {
                if (item.getRoleName().contains(name + "_R"))
                {
                    return item.getRoleName();
                }
            }
        }
        
        return null;
    }
    
    public static RoleTypes getRoleType(String name, Boolean isWrite)
    {
        for (RoleTypes item : RoleTypes.values())
        {
            if (isWrite != null && isWrite)
            {
                if (item.getRoleName().contains(name + "_W"))
                {
                    return item;
                }
            }
            else
            {
                if (item.getRoleName().contains(name + "_R"))
                {
                    return item;
                }
            }
        }
        
        return null;
    }
    
    private String role;
}
