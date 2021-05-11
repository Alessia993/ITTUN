package com.infostroy.paamns.install;

import com.infostroy.paamns.install.facade.IInstaller;
import com.infostroy.paamns.install.facade.InstallerUtil;

/**
 * 
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class Installer
{
    /**
     * 
     * @param args
     */
    static public void main(String [] args)
    {
        System.out.println("Installer : start...");
        
        IInstaller jcrf = InstallerUtil.getInstallerFacade();
        
        jcrf.doInstall(args);
        
        System.out.println("Installer : end...");
    }
    
}
