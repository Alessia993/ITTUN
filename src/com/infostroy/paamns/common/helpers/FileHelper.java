/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ApplicationSettings;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2009.
 * 
 */
public class FileHelper
{
    protected transient final static Log log = LogFactory
                                                     .getLog(FileHelper.class);
    
    public static String getFileName(String path)
    {
        String name = EncodingHelper.ConvertToUTF8String(path);
        return (new File(name)).getName();
    }
    
    public static String getFileExtension(String path)
    {
        if (path == null || path.lastIndexOf('.') == -1)
        {
            return "";
        }
        
        return path.substring(path.lastIndexOf('.'), path.length());
    }
    
    public static String getFileNameWOExtension(String path)
    {
        if (path.lastIndexOf('.') == -1)
        {
            return path;
        }
        
        return path.substring(0, path.lastIndexOf('.'));
    }
    
    public static String getRealPath()
    {
        ServletContext context = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();
        return new File(new File(context.getRealPath("/")).getParent())
                .getParent();
    }
    
    public static String getFileDirectory() throws PersistenceBeanException
    {
        ApplicationSettings directorySetting = BeansFactory
                .ApplicationSettings().getItem(
                        ApplicationSettingTypes.UPLOAD_FILE_PARENT_DIRECTORY);
        if (directorySetting.getValue() == null
                || directorySetting.getValue().isEmpty()
                || !new File(directorySetting.getValue()).exists())
        {
            directorySetting.setValue(getRealPath());
            BeansFactory.ApplicationSettings().Save(directorySetting);
        }
        
        return directorySetting.getValue();
    }
    
    public static String newFileName(String name)
            throws PersistenceBeanException
    {
        File file = new File(getFileDirectory(), "Files");
        file.mkdir();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(getFileDirectory() + File.separator + "Files"
                + File.separator);
        sb.append(UUID.randomUUID().toString());
        sb.append(FileHelper.getFileExtension(name));
        
        return sb.toString();
    }
    
    public static String newTempFileName(String name)
            throws PersistenceBeanException
    {
        File file = new File(getFileDirectory(), "Temp");
        file.mkdir();
        try
        {
            clearTempFolder();
        }
        catch(IOException e)
        {
            log.error(e);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(getFileDirectory() + File.separator + "Temp" + File.separator);
        sb.append(UUID.randomUUID().toString());
        sb.append(FileHelper.getFileExtension(name));
        
        return sb.toString();
    }
    
    public static void clearTempFolder() throws IOException,
            PersistenceBeanException
    {
        File file = new File(getFileDirectory(), "Temp");
        file.mkdir();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -2);
        for (String str : file.list())
        {
            File tempf = new File(file, str);
            Date d = new Date(tempf.lastModified());
            if (d.before(c.getTime()))
            {
                delete(new File(file.getAbsolutePath(), str));
            }
        }
    }
    
    public static boolean delete(File resource) throws IOException
    {
        if (resource.isDirectory())
        {
            File[] childFiles = resource.listFiles();
            
            for (File child : childFiles)
            {
                delete(child);
            }
        }
        
        return resource.delete();
        
    }
    
    public static boolean copyFile(File source, File dest) throws IOException
    {
        if (!dest.exists())
        {
            dest.createNewFile();
        }
        
        InputStream in = null;
        OutputStream out = null;
        
        try
        {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
        }
        catch(Exception e)
        {
            log.error(e);
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
            
            return false;
        }
        
        in.close();
        out.close();
        return true;
    }
    
    /**
     * Send file throu response
     * 
     * @param data
     * @param fileName
     */
    public static void sendFile(DocumentInfo docinfo, boolean containsDir)
    {
        if (docinfo == null)
        {
            log.error("File download error: Document is null");
            return;
        }
        byte[] data = null;
        String fileName = "";
        FileInputStream inputFile;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context
                .getExternalContext().getResponse();
        ServletOutputStream output = null;
        try
        {
            File file = containsDir ? new File(docinfo.getFileName())
                    : new File(getFileDirectory(), docinfo.getFileName());
            
            if(!file.exists())
            {
            	log.error("File download error: Document not found");
            	return;
            }
            inputFile = new FileInputStream(file);
            data = new byte[(int) file.length()];
            inputFile.read(data);
            
            fileName = docinfo.getName();
            // document.setLastViewDate(DateTime.getNow());
            // We must get first our context
            
            // Then we have to get the Response where to write our file
            
            response.reset();
            response.setHeader("Content-Type",
                    FileHelper.getFileExtension(fileName));
            response.setHeader("Content-Length", String.valueOf(data.length));
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + fileName + "\"");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            // Streams we will use to read, write the file bytes to our response
            
            output = response.getOutputStream();
            output.write(data);
            output.flush();
            
        }
        catch(Exception e)
        {
            log.error(e);
        }
        finally
        {
            context.responseComplete();
            try
            {
                if (output != null)
                {
                    output.close();
                }
                else
                {
                    // response.sendRedirect("../"+PagesTypes.INDEX.getPagesContext());
                }
                
            }
            catch(IOException e)
            {
                log.error(e);
            }
        }
    }
    
    /**
     * Send file throu response 
     * @param fileName
     * @param inputFile
     * @param dataLength 
     * @param dataLength
     */
    public static void sendFile(String fileName, InputStream inputFile,
            int dataLength)
    {
        byte[] data = new byte[dataLength];
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context
                .getExternalContext().getResponse();
        ServletOutputStream output = null;
        try
        {
            int length = inputFile.read(data);
            
            response.reset();
            response.setHeader("Content-Type",
                    FileHelper.getFileExtension(fileName));
            response.setHeader("Content-Length", String.valueOf(length));
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + fileName + "\"");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            //Streams we will use to read, write the file bytes to our response
            
            output = response.getOutputStream();
            output.write(data);
            output.flush();
            
        }
        catch(Exception e)
        {
            log.error(e);
        }
        finally
        {
            
            try
            {
                if (output != null)
                {
                    context.responseComplete();
                    output.close();
                }
            }
            catch(IOException e)
            {
                log.error(e);
            }
            try
            {
                if (inputFile != null)
                {
                    inputFile.close();
                }
            }
            catch(IOException e)
            {
                log.error(e);
            }
            
        }
        
    }
}
