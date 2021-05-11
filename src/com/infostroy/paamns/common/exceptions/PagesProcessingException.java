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
public class PagesProcessingException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -735881659350015329L;
    
    public PagesProcessingException()
    {
        
    }
    
    public PagesProcessingException(String message)
    {
        super(message);
    }
    
    public PagesProcessingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
