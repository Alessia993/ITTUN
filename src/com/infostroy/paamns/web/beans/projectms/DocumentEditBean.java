/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class DocumentEditBean extends EntityEditBean<Documents>
{
    private UploadedFile _upFile;
    
    private String       file;
    
    public String upload() throws IOException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getApplicationMap()
                .put("fileupload_bytes", getUpFile().getBytes());
        facesContext.getExternalContext().getApplicationMap()
                .put("fileupload_type", getUpFile().getContentType());
        facesContext.getExternalContext().getApplicationMap()
                .put("fileupload_name", getUpFile().getName());
        System.out.print(getUpFile().getName());
        return "ok";
    }
    
    /**
     * Sets _upFile
     * @param _upFile the _upFile to set
     */
    public void setUpFile(UploadedFile upFile)
    {
        this._upFile = upFile;
    }
    
    /**
     * Gets _upFile
     * @return _upFile the _upFile
     */
    public UploadedFile getUpFile()
    {
        return _upFile;
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.setEntity(new Documents());
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException,
            PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException, PersistenceBeanException
    {
        try
        {
            String fileName = FileHelper.newFileName(getUpFile().getName());
            OutputStream os = new FileOutputStream(new File(fileName));
            os.write(getUpFile().getBytes());
            os.close();
            this.getEntity().setName(getUpFile().getName());
            this.getEntity().setFileName(fileName);
            BeansFactory.Documents().SaveInTransaction(this.getEntity());
        }
        catch(FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Sets file
     * @param file the file to set
     */
    public void setFile(String file)
    {
        this.file = file;
    }
    
    /**
     * Gets file
     * @return file the file
     */
    public String getFile()
    {
        return file;
    }
}
