/**
 * 
 */
/**
 * 
 */
package com.infostroy.paamns.common.security.api;

import java.io.IOException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public interface SecurityInfoFactMethod
{
    /**
     * 
     * @param userLogin
     * @return
     * @throws IOException
     * @throws PersistenceBeanException 
     */
    ///     SecurityInfo getSecurityInfoByLogin(String userLogin)  throws IOException;    
    SecurityInfo getSecurityInfoByLogin(String userLogin)
            throws PersistenceBeanException, IOException,
            PersistenceBeanException;
    
}
