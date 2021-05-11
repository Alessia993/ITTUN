/**
 * 
 */
package com.infostroy.paamns.persistence.beans.proxy.facade;

import java.lang.reflect.Proxy;

import com.infostroy.paamns.persistence.beans.proxy.handler.JProxyHandler;
import com.infostroy.paamns.persistence.beans.proxy.ifc.ProxyInterface;
import com.infostroy.paamns.persistence.beans.proxy.impl.ProxyImpl;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class ProxyFacade
{
    /**
     * 
     */
    private static ProxyFacade _instance = new ProxyFacade();
    
    /**
     * 
     */
    private ProxyInterface     _proxy    = null;
    
    /**
     * 
     */
    private ProxyFacade()
    {
        initProxy();
    }
    
    /**
     * 
     */
    private void initProxy()
    {
        ProxyImpl proxyImpl = new ProxyImpl();
        
        JProxyHandler proxyHandler = new JProxyHandler(proxyImpl);
        
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[] {
                    ProxyInterface.class
                }, proxyHandler);
        
        _proxy = (ProxyInterface) proxy;
    }
    
    /**
     * 
     * @return
     */
    public static ProxyFacade getInstance()
    {
        return _instance;
    }
    
    /**
     * 
     */
    public ProxyInterface getProxyIfc()
    {
        return _proxy;
    }
    
}
