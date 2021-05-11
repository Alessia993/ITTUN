/**
 * 
 */
package com.infostroy.paamns.common.security.api;

import java.io.IOException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public interface SecurityController
{
    /**
     * 
     * @param userLogin
     * @return
     * @throws PersistenceBeanException
     * @throws IOException
     */
    SecurityInfo getSecurityInfoByLogin(String userLogin)
            throws PersistenceBeanException, IOException;
    
}
