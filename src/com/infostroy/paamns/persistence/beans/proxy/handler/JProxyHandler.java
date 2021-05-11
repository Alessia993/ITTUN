/**
 * 
 */
package com.infostroy.paamns.persistence.beans.proxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class JProxyHandler implements InvocationHandler
{
    /**
     * 
     */
    private Object _target;
    
    /**
     * 
     * @param target
     */
    public JProxyHandler(Object target)
    {
        _target = target;
    }
    
    /**
     * 
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        Object res = method.invoke(_target, args);
        return res;
    }
    
}
