/**
 * 
 */
package com.infostroy.paamns.common.exceptions;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2009.
 *
 */
public class SecurityException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = 6835316542614586938L;
    
    public SecurityException()
    {
    }
    
    public SecurityException(String message)
    {
        super(message);
    }
}
