package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Localizations;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 * 
 * @author Nickolay Sumyatin nikolay.sumyatin@infostroy.com.ua InfoStroy Co.,
 *         29.03.2010.
 * 
 */

public class LocalizationListBean extends EntityListBean<Localizations>
{
    
    private boolean newLocalizationAvailable = true;
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        if (this.getSession().get("project") != null)
        {
            this.setList(BeansFactory.Localizations().LoadByProject(
                    String.valueOf(this.getSession().get("project"))));
        }
    }
    
    public void Page_Load_Static() throws PersistenceBeanException
    {
    }
    
    /**
     * Add localization button click handler
     */
    public void addLocalization()
    {
        this.getSession().put("localization", null);
        
        this.goTo(PagesTypes.LOCALIZATIONEDIT);
    }
    
    /**
     * Edit localization button click handler
     */
    public void editLocalization()
    {
        this.getSession().put("localization", this.getEntityEditId());
        
        this.goTo(PagesTypes.LOCALIZATIONEDIT);
    }
    
    /**
     * Deletes specified item
     */
    public void deleteItem()
    {
        try
        {
            BeansFactory.Localizations().Delete(
                    String.valueOf(getEntityDeleteId()));
            this.Page_Load();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(PersistenceBeanException e)
        {
            log.error(e);
        }
        catch(NumberFormatException e)
        {
            log.error(e);
        }
    }
    
    public void setNewLocalizationAvailable(boolean newLocalizationAvailable)
    {
        this.newLocalizationAvailable = newLocalizationAvailable;
    }
    
    public boolean isNewLocalizationAvailable()
    {
        return newLocalizationAvailable;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
     */
    @Override
    public void addEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
     */
    @Override
    public void deleteEntity()
    {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
     */
    @Override
    public void editEntity()
    {
        // TODO Auto-generated method stub
        
    }
}
