package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.Date;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Nickolay Sumyatin
 * nikolay.sumyatin@infostroy.com.ua
 * InfoStroy Co., 14.04.2010.
 * modified by Ingloba360
 *
 */
public class ProcedureInsertEditBean extends EntityProjectEditBean<Procedures>
{
    
    private String  PartnerResponsible;
    
    private Date    expectedStartDate;
    
    private Date    expectedEndDate;
    
    private Date effectiveStartDate;
    
    private Date effectiveEndDate;
    
    private boolean isActual;
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.PROCEDUREINSERTLIST);
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("procedure") != null)
        {
            Procedures entityToLoad = BeansFactory.Procedures().Load(
                    this.getSession().get("procedure").toString());
            
            if (entityToLoad != null)
            {
                this.setPartnerResponsible(entityToLoad.getPartnerResponsible());
                this.setExpectedStartDate(entityToLoad.getExpectedStartDate());
                this.setExpectedEndDate(entityToLoad.getPlannedEndDate());
                this.setEffectiveStartDate(entityToLoad.getEffectiveStartDate());
                this.setEffectiveEndDate(entityToLoad.getEffectiveEndDate());
            }
        }
        
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (this.getSessionBean().getIsActualSateAndAguRead())
        {
            goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        if (this.getSession().get("procedure") != null)
        {
            Procedures entityToSave = BeansFactory.Procedures().Load(
                    this.getSession().get("procedure").toString());
            
            if (entityToSave != null)
            {
                entityToSave
                        .setPartnerResponsible(this.getPartnerResponsible());
                entityToSave.setExpectedStartDate(this.getExpectedStartDate());
                entityToSave.setPlannedEndDate(this.getExpectedEndDate());
                entityToSave.setEffectiveStartDate(this.getEffectiveStartDate());
                entityToSave.setEffectiveEndDate(this.getEffectiveEndDate());
                BeansFactory.Procedures().Save(entityToSave);
            }
        }
    }
    
    public void setPartnerResponsible(String partnerResponsible)
    {
        PartnerResponsible = partnerResponsible;
    }
    
    public String getPartnerResponsible()
    {
        return PartnerResponsible;
    }
    
    public void setExpectedStartDate(Date expectedStartDate)
    {
        this.expectedStartDate = expectedStartDate;
    }
    
    public Date getExpectedStartDate()
    {
        return expectedStartDate;
    }
    
    public void setExpectedEndDate(Date expectedEndDate)
    {
        this.expectedEndDate = expectedEndDate;
    }
    
    public Date getExpectedEndDate()
    {
        return expectedEndDate;
    }
    
    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }
    
    public void setEffectiveStartDate(Date effectiveStartDate) {
    	this.effectiveStartDate = effectiveStartDate;
    }
    
    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }
    
    public void setEffectiveEndDate(Date effectiveEndDate) {
    	this.effectiveEndDate = effectiveEndDate;
    }
    
    /**
     * Sets isActual
     * @param isActual the isActual to set
     */
    public void setIsActual(boolean isActual)
    {
        this.isActual = isActual;
    }
    
    /**
     * Gets isActual
     * @return isActual the isActual
     */
    public boolean getIsActual()
    {
        return isActual;
    }
}
