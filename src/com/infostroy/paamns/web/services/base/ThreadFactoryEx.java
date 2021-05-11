/**
 * 
 */
package com.infostroy.paamns.web.services.base;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public class ThreadFactoryEx implements ThreadFactory
{
    private String name;
    
    /**
     * 
     */
    public ThreadFactoryEx(String name)
    {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r)
    {
        return new Thread(r, this.name);
    }
    
}
