/**
 * 
 */
package com.infostroy.paamns.common.logging;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.FileHelper;

/**
 *
 * @author Mary Fesenko
 * InfoStroy Co., 2011.
 *
 */
/**
*
* @author Vladimir Zrazhevskiy
* InfoStroy Co., 2011.
*
*/
public class InfoLog
{
    private static InfoLog        instance;
    
    protected transient final Log log         = LogFactory.getLog(getClass());
    
    private static final String   folder      = "logs";
    
    private static final String   file        = "ITTUN.action.log";
    
    private static final String   oldFileTemp = "ITTUN.action.log.%d";
    
    private static final String   logFormat   = "%s";
    
    public void addMessage(String message)
    {
        try
        {
            File dir = new File(folder);
            dir.mkdir();
            renameFiles();
            
            File log = new File(dir.getAbsolutePath(), file);
            FileOutputStream outStream = new FileOutputStream(log, true);
            DataOutputStream out = new DataOutputStream(outStream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            if (log.length() != 0)
            {
                bw.newLine();
            }
            bw.append(message);
            bw.flush();
            bw.close();
            
            out.close();
        }
        catch(Exception e)
        {
            log.error(e);
        }
    }
    
    private void renameFiles() throws IOException, NumberFormatException,
            PersistenceBeanException, NullPointerException
    {
        File dir = new File(FileHelper.getFileDirectory(), folder);
        boolean oversized = false;
        for (File file : dir.listFiles())
        {
            if (file.exists() && file.isFile()
                    && file.getName().equalsIgnoreCase(InfoLog.file))
            {
                if (file.length() > 1048576)
                {
                    oversized = true;
                }
            }
        }
        
        List<File> files = new ArrayList<File>();
        for (File file : dir.listFiles())
        {
            files.add(file);
        }
        
        Collections.sort(files, new FileComparer());
        
        if (oversized)
        {
            for (int i = 0; i < dir.listFiles().length; i++)
            {
                File file = files.get(i);
                if (file.exists() && file.isFile()
                        && checkFileName(file.getName()))
                {
                    rename(file, getNewFileName(file.getName()));
                    file.delete();
                }
            }
        }
    }
    
    private String getNewFileName(String oldName)
    {
        if (oldName.equalsIgnoreCase(file))
        {
            return String.format(oldFileTemp, 1);
        }
        
        try
        {
            return String.format(
                    oldFileTemp,
                    1 + Integer.parseInt(oldName.substring(
                            oldName.lastIndexOf('.') + 1, oldName.length())));
        }
        catch(Exception e)
        {
            return oldName;
        }
        
    }
    
    private boolean checkFileName(String fileName)
    {
        if (fileName.indexOf(file.substring(0, file.length() - 4)) != -1)
        {
            return true;
        }
        return false;
    }
    
    //    public void addActivity()
    //    {
    //        try
    //        {
    //            FacesContext ctx = FacesContext.getCurrentInstance();
    //            HttpServletRequest request = (HttpServletRequest) ctx
    //                    .getExternalContext().getRequest();
    //            
    //            String path = request.getServletPath();
    //            if(SessionManager.getInstance().getSessionBean()
    //                    .getCurrentUser()!=null)
    //            {
    //                addEntity(SessionManager.getInstance().getSessionBean()
    //                        .getCurrentUser().getLogin(), path, new Date(), request
    //                        .getRemoteAddr());
    //            }
    //            else
    //            {
    //                addEntity("not_loged_in", path, new Date(), request
    //                        .getRemoteAddr());
    //            }
    //        }
    //        catch(Exception e)
    //        {
    //            log.error(e);
    //            log.error(LogHelper.readStackTrace(e));
    //        }
    //    }
    
    private File rename(File f, String name) throws IOException,
            PersistenceBeanException
    {
        File fn = new File(FileHelper.getFileDirectory(), File.separator
                + folder + File.separator + name);
        FileOutputStream outStream = new FileOutputStream(fn);
        DataOutputStream out = new DataOutputStream(outStream);
        
        FileInputStream fstream = new FileInputStream(f);
        
        DataInputStream in = new DataInputStream(fstream);
        byte[] b = new byte[(int) f.length()];
        in.read(b);
        out.write(b);
        
        in.close();
        out.close();
        
        return fn;
    }
    
    /**
     * Getter method for variable instance
     * @return the value of instance the instance
     */
    public static InfoLog getInstance()
    {
        if (instance == null)
        {
            instance = new InfoLog();
        }
        return instance;
    }
    
    public class FileComparer implements Comparator<File>
    {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(File o1, File o2)
        {
            if (o1.getName().equalsIgnoreCase(InfoLog.file))
            {
                return 1;
            }
            if (o2.getName().equalsIgnoreCase(InfoLog.file))
            {
                return -1;
            }
            try
            {
                return ((Integer) Integer.parseInt(o2.getName().substring(
                        o2.getName().lastIndexOf('.'),
                        o2.getName().length() - 1)))
                        .compareTo((Integer) Integer.parseInt(o1.getName()
                                .substring(o1.getName().lastIndexOf('.'),
                                        o1.getName().length() - 1)));
            }
            catch(Exception e)
            {
                return 0;
            }
        }
        
    }
}
