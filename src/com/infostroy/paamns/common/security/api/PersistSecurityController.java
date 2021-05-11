/**
 * 
 */
package com.infostroy.paamns.common.security.api;

import java.io.IOException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.entities.domain.Users;
import com.infostroy.paamns.persistence.beans.facades.UsersBean;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public class PersistSecurityController implements SecurityController
{
    
    /**
     * 
     */
    public PersistSecurityController()
    {
    }
    
    /**
     * 
     * @param userLogin
     * @return
     * @throws PersistenceBeanException
     * @throws IOException
     */
    public SecurityInfo getSecurityInfoByLogin(String userLogin)
            throws PersistenceBeanException, IOException
    {
        Users user = UsersBean.GetByLogin(userLogin);
        
        return new SecurityInfo(user.getId(), user.getLogin(),
                user.getPassword(), user.getRoles());
    }
}
