/**
 * 
 */
package com.infostroy.paamns.common.security.beans;

import java.util.ArrayList;
import java.util.List;

import com.infostroy.paamns.common.enums.RoleTypes;
import com.infostroy.paamns.common.security.api.SecurityInfo;
import com.infostroy.paamns.common.security.api.SecurityInfoProc;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public class SecurityInfoProcBean implements SecurityInfoProc
{
    
    /**
     * 
     */
    public SecurityInfoProcBean()
    {
    }
    
    /**
     * 
     * @param si
     * @return
     */
    public List<String> procSecurityInfo(SecurityInfo si)
    {
        SessionManager.getInstance().getSessionBean()
                .setListCurrentUserRoles(null);
        
        List<String> resList = new ArrayList<String>();
        if (si == null)
        {
            throw new SecurityException(
                    "SecurityInfoProcImpl.procSecurityInfo : exception has been generated as SecurityInfo isn't defined...");
        }
        
        resList.add(RoleTypes.USER_ROLE.getRoleName());
        
        List<RoleTypes> listCurrentUserRoles = new ArrayList<RoleTypes>();
        
        for (UserRoles item : si.get_roleList())
        {
            listCurrentUserRoles.add(RoleTypes.getRoleType(item.getRole()
                    .getCode(), item.getWriteAccess()));
            resList.add(RoleTypes.getRoleName(item.getRole().getCode(),
                    item.getWriteAccess()));
        }
        
        SessionManager.getInstance().getSessionBean()
                .setListCurrentUserRoles(listCurrentUserRoles);
        
        return resList;
    }
}
