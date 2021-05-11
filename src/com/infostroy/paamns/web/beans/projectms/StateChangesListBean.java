/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectStatesChanges;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.StateChangeWrapper;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class StateChangesListBean extends EntityListBean<StateChangeWrapper>
{
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        List<ProjectStatesChanges> listChanges = BeansFactory
                .ProjectStatesChanges().LoadByProject(this.getProjectId());
        this.setList(new ArrayList<StateChangeWrapper>());
        
        for (ProjectStatesChanges item : listChanges)
        {
            this.getList().add(new StateChangeWrapper(item));
        }
    }
    
    @Override
    public void addEntity()
    {
        // Not used
    }
    
    @Override
    public void deleteEntity()
    {
        // Not available
    }
    
    @Override
    public void editEntity()
    {
        // Not available        
    }
    
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("project") == null)
        {
            this.goTo(PagesTypes.PROJECTLIST);
        }
    }
    
    public void getDocument()
    {
        ProjectStatesChanges currentChange = null;
        
        try
        {
            currentChange = BeansFactory.ProjectStatesChanges().Load(
                    this.getEntityDownloadId());
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        Documents document = new StateChangeWrapper(currentChange);
        
        DocumentInfo docinfo = new DocumentInfo(document.getDocumentDate(),
                document.getName(), document.getFileName(),
                document.getProtocolNumber(), null, document.getSignflag());
        
        FileHelper.sendFile(docinfo, false);
    }
    
    /**
     * Sets entityDownloadId
     * @param entityDownloadId the entityDownloadId to set
     */
    public void setEntityDownloadId(Long entityDownloadId)
    {
        this.getViewState().put("downloadId", entityDownloadId);
    }
    
    /**
     * Gets entityDownloadId
     * @return entityDownloadId the entityDownloadId
     */
    public Long getEntityDownloadId()
    {
        return (Long) this.getViewState().get("downloadId");
    }
    
    private Long getProjectId()
    {
        return Long.valueOf(String.valueOf(this.getSession().get("project")));
    }
}
