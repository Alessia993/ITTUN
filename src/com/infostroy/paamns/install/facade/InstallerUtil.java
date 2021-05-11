/**
 * 
 */
package com.infostroy.paamns.install.facade;

import java.io.File;
import java.lang.reflect.Proxy;

import com.infostroy.paamns.install.classloader.UnpackJarClassLoader;
import com.infostroy.paamns.install.proxy.JProxyHandler;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public abstract class InstallerUtil
{
    
    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static IInstaller getInstallerFacade()
    {
        IInstaller fcdInterface = null;
        try
        {
            UnpackJarClassLoader ojcl = new UnpackJarClassLoader();
            
            Class<? extends IInstaller> clsFacade = (Class<? extends IInstaller>) ojcl
                    .loadClass("com.infostroy.paamns.install.facade.impl.Installer");
            
            Object objFacade = clsFacade.newInstance();
            
            JProxyHandler proxyHandler = new JProxyHandler(objFacade);
            
            Object proxy = Proxy.newProxyInstance(ojcl, new Class[] {
                IInstaller.class
            }, proxyHandler);
            
            fcdInterface = (IInstaller) proxy;
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            
            e.printStackTrace();
        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        return fcdInterface;
    }
    
    /**
     * 
     * @param dir
     */
    public static boolean deleteDirectory(File delDir)
    {
        if (delDir.isDirectory())
        {
            String[] children = delDir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDirectory(new File(delDir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        return delDir.delete();
    }
    
}
