/**
 * 
 */
package com.infostroy.paamns.web.services.base;


import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public abstract class BaseService implements Runnable
{
    @SuppressWarnings("unused")
    private static final Integer DEFAULT_SERVICE_POLL_TIME = 60;                       // 60
                                                                                        // minutes
                                                                                        
    protected final Log          log                       = LogFactory
                                                                   .getLog(getClass());
    
    protected volatile Object    monitor;
    
    protected volatile long      sleepTimeMs;
    
    protected boolean            stopFlag;
    
    private ExecutorService      executorService;
    
    protected String             name;
    
    private boolean              isRunning;
    
    private Date                 lastStartTime;
    
    public BaseService(String name)
    {
        stopFlag = true;
        this.name = name;
        
        this.monitor = new Object();
       
    }
    
    public void start()
    {
        executorService = Executors
                .newSingleThreadExecutor(new ThreadFactoryEx(name));
        stopFlag = false;
        executorService.execute(this);
    }
    
    public void stop()
    {
        stopFlag = true;
        if (executorService!=null && !executorService.isShutdown())
        {
            executorService.shutdownNow();
            executorService = null;
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        isRunning = true;
        synchronized (monitor)
        {
            try
            {
                monitor.wait(1 * 60 * 1000);
            }
            catch(InterruptedException e)
            {
                isRunning = false;
                System.out.println("interrupted.. " + this.name);
                return;
            }
        }
        
        // Do not use while(true)
        int i = 0;
        
        while (i < 10)
        {
            lastStartTime = new Date();
            if (this.monitor == null || stopFlag)
            {
                isRunning = false;
                return;
            }
            
            //            System.out.println(this.name + " started");
            
            runInternal();
            
            if (this.monitor != null && !stopFlag)
            {
                synchronized (monitor)
                {
                    try
                    {
                        monitor.wait(this.sleepTimeMs);
                    }
                    catch(InterruptedException e)
                    {
                        isRunning = false;
                        System.out.println("interrupted.. " + this.name);
                        return;
                    }
                }
            }
            else
            {
                // Object will be destroyed
                isRunning = false;
                onDestroy();
                return;
            }
            
            i++;
            if (i > 5)
            {
                i = 0;
            }
        }
        isRunning = false;
    }
    
    /**
     * 
     */
    protected void runInternal()
    {
        try
        {
            this.preRoutineFuncInternal();
            this.preRoutineFunc();
            this.routineFunc();
            this.postRoutineFunc();
            
        }
        catch(Exception e)
        {
            isRunning = false;
        }
        finally
        {
            this.postRoutineFuncInternal();
        }
    }
    
    protected void onDestroy()
    {
        
    }
    
    private void preRoutineFuncInternal()
    {
        if (stopFlag)
        {
            return;
        }
        
        this.updateSleepTime();
    }
    
    protected void postRoutineFuncInternal()
    {
        if (stopFlag)
        {
            return;
        }
    }
    
    protected final void routineFunc()
    {
        if (stopFlag)
        {
            return;
        }
        
        this.routineFuncInternal();
    }
    
    protected abstract void routineFuncInternal();
    
    protected void preRoutineFunc()
    {
        if (stopFlag)
        {
            return;
        }
    }
    
    protected void postRoutineFunc()
    {
        if (stopFlag)
        {
            return;
        }
    }
    
    // Poll time methods
    
    protected abstract int getPollTimeKey();
    
    protected void updateSleepTime()
    {
        Integer pollTime = getPollTimeKey();
        
        this.sleepTimeMs = pollTime * 60 * 1000;
    }
    
    protected void addToLog(Exception e)
    {
        this.log.error(e);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        if (this.monitor != null)
        {
            synchronized (monitor)
            {
                monitor = null;
            }
        }
        
        super.finalize();
    }
    
    protected Integer parseIntegerValue(String value)
    {
        if (value != null && !value.isEmpty())
        {
            Integer val = null;
            
            try
            {
                val = Integer.parseInt(value);
            }
            catch(NumberFormatException e)
            {
                
            }
            
            return val;
        }
        
        return null;
    }
    
    /**
     * Ottiene isRunning
     * @return il isRunning del BaseService
     */
    public boolean isRunning()
    {
        return isRunning;
    }
    
    /**
     * Imposta isRunning del BaseService
     * @param isRunning per impostare
     */
    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

    /**
     * Ottiene lastStartTime
     * @return il lastStartTime del BaseService
     */
    public Date getLastStartTime()
    {
        return lastStartTime;
    }

    /**
     * Imposta lastStartTime del BaseService
     * @param lastStartTime per impostare
     */
    public void setLastStartTime(Date lastStartTime)
    {
        this.lastStartTime = lastStartTime;
    }
}
