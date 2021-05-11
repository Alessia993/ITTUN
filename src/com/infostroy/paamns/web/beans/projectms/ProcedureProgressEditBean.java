package com.infostroy.paamns.web.beans.projectms;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProjectState;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.ProcedureNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

public class ProcedureProgressEditBean extends
        EntityProjectEditBean<Procedures>
{
    private Procedures oldProcedure;
    
    private String     partnerResponsible;
    
    private Date       expectedStartDate;
    
    private Date       effectiveStartDate;
    
    private Date       expectedEndDate;
    
    private Date       effectiveEndDate;
    
    private boolean    isActual;
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        if (this.getSession().get("FromProgressValidationProcedureProgress") != null)
        {
            this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWEDIT);
        }
        else
        {
            this.goTo(PagesTypes.PROCEDUREPROGRESSLIST);
        }
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("procedure") != null)
        {
            Procedures entityToLoad = BeansFactory.Procedures().Load(
                    this.getSession().get("procedure").toString());
            this.oldProcedure = entityToLoad.clone();
            this.oldProcedure.setId(entityToLoad.getId());
            
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
        if (!this.getSessionBean().getIsActualSate())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        if (this.getSessionBean().getIsAguRead())
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        if (this.getSession().get("procedure") != null)
        {
            if (this.getSession()
                    .get("FromProgressValidationProcedureProgress") != null)
            {
                Procedures item = BeansFactory.Procedures().Load(
                        String.valueOf(this.getSession().get("procedure")));
                
                item.setPartnerResponsible(this.getPartnerResponsible());
                item.setExpectedStartDate(this.getExpectedStartDate());
                item.setEffectiveStartDate(this.getEffectiveStartDate());
                item.setPlannedEndDate(this.getExpectedEndDate());
                item.setEffectiveEndDate(this.getEffectiveEndDate());
                BeansFactory.Procedures().SaveInTransaction(item);
            }
            else
            {
                
                if (this.oldProcedure != null && this.IsEntityChanged())
                {
                    List<Procedures> listProcedures = BeansFactory.Procedures()
                            .LoadLastByProject(
                                    String.valueOf(this.oldProcedure
                                            .getProject().getId()));
                    
                    for (Procedures p : listProcedures)
                    {
                        Procedures newEntity = p.clone();
                        
                        newEntity.setVersion(newEntity.getVersion() + 1);
                        
                        if (p.getId().equals(this.oldProcedure.getId()))
                        {
                            newEntity.setPartnerResponsible(this
                                    .getPartnerResponsible());
                            newEntity.setExpectedStartDate(this
                                    .getExpectedStartDate());
                            newEntity.setEffectiveStartDate(this
                                    .getEffectiveStartDate());
                            
                            if (this.getProjectState().equals(
                                    ProjectState.Actual))
                            {
                                this.setIsActual(true);
                                newEntity.setPlannedEndDate(this
                                        .getExpectedEndDate());
                                newEntity.setEffectiveEndDate(this
                                        .getEffectiveEndDate());
                            }
                            else
                            {
                                this.setIsActual(false);
                            }
                        }
                        
                        newEntity.setProgressValidationObject(null);
                        
                        BeansFactory.Procedures().Save(newEntity);
                        
                        List<ProcedureNotes> listNotes = BeansFactory
                                .ProcedureNotes().LoadByProcedure(
                                        String.valueOf(p.getId()));
                        for (ProcedureNotes item : listNotes)
                        {
                            item.setProcedure(newEntity);
                            BeansFactory.ProcedureNotes().SaveInTransaction(
                                    item);
                        }
                    }
                }
            }
        }
    }
    
    private boolean IsEntityChanged()
    {
        boolean isChanged = false;
        
        if (this.oldProcedure.getPartnerResponsible() != null)
        {
            isChanged = !this.oldProcedure.getPartnerResponsible().equals(
                    this.getPartnerResponsible());
        }
        else if (this.getPartnerResponsible() != null)
        {
            isChanged = !this.getPartnerResponsible().equals(
                    this.oldProcedure.getPartnerResponsible());
        }
        
        return (isChanged
                || this.DateChanged(this.oldProcedure.getExpectedStartDate(),
                        this.getExpectedStartDate())
                || this.DateChanged(this.oldProcedure.getEffectiveStartDate(),
                        this.getEffectiveStartDate())
                || this.DateChanged(this.oldProcedure.getPlannedEndDate(),
                        this.getExpectedEndDate()) || this.DateChanged(
                this.oldProcedure.getEffectiveEndDate(),
                this.getEffectiveEndDate()));
    }
    
    /**
     * Compares two dates; determines whether they are changed
     * @param date1
     * @param date2
     * @return true when date changed; otherwise, false;
     */
    private boolean DateChanged(Date date1, Date date2)
    {
        if (date1 != null && date2 != null)
        {
            return !(date1.compareTo(date2) == 0);
        }
        else if (date1 == null && date2 == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void setPartnerResponsible(String partnerResponsible)
    {
        this.partnerResponsible = partnerResponsible;
    }
    
    public String getPartnerResponsible()
    {
        return partnerResponsible;
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
    
    public void setEffectiveEndDate(Date effectiveEndDate)
    {
        this.effectiveEndDate = effectiveEndDate;
    }
    
    public Date getEffectiveEndDate()
    {
        return effectiveEndDate;
    }
    
    public void setEffectiveStartDate(Date effectiveStartDate)
    {
        this.effectiveStartDate = effectiveStartDate;
    }
    
    public Date getEffectiveStartDate()
    {
        return effectiveStartDate;
    }
    
    /**
     * Sets oldProcedure
     * @param oldProcedure the oldProcedure to set
     */
    public void setOldProcedure(Procedures oldProcedure)
    {
        this.oldProcedure = oldProcedure;
    }
    
    /**
     * Gets oldProcedure
     * @return oldProcedure the oldProcedure
     */
    public Procedures getOldProcedure()
    {
        return oldProcedure;
    }
    
}
