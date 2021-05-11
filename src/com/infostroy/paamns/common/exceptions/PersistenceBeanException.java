/**
 * 
 */
package com.infostroy.paamns.common.exceptions;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class PersistenceBeanException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 6835316542600586938L;
    
    public PersistenceBeanException()
    {
    }
    
    public PersistenceBeanException(String message)
    {
        super(message);
    }
    
    public PersistenceBeanException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
