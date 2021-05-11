/**
 * 
 */
package com.infostroy.paamns.common.security.beans;

import java.io.IOException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.security.api.SecurityInfo;
import com.infostroy.paamns.common.security.api.SecurityInfoFactMethod;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class PersistSecurityBean implements SecurityInfoFactMethod
{
    /**
     * 
     */
    public PersistSecurityBean()
    {
    }
    
    /**
     * 
     */
    public SecurityInfo getSecurityInfoByLogin(String userLogin)
            throws IOException, PersistenceBeanException
    {
        Users user = UsersBean.GetByLogin(userLogin);
        
        return new SecurityInfo(user.getId(), user.getLogin(),
                user.getPassword(), user.getRoles());
    }
}
