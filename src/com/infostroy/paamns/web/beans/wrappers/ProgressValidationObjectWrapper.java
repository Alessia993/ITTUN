/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ProgressValidationStateTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjects;

/**
 *
 * @author Sergey Zorin
 * InfoStroy Co., 2010.
 *
 */
public class ProgressValidationObjectWrapper extends ProgressValidationObjects
{
    private String  progressName;
    
    private String  stateString;
    
    private boolean editable;
    
    private boolean canShow;
    
    private String  usedBy;
    
    private boolean sendable;
    
    private boolean deniable;
    
    public ProgressValidationObjectWrapper(
    
    ProgressValidationObjects pvo, int number)
    {
        this.setCreateDate(pvo.getCreateDate());
        this.setDeleted(pvo.getDeleted());
        this.setId(pvo.getId());
        this.setProject(pvo.getProject());
        this.setState(pvo.getState());
        this.setApprovalDate(pvo.getApprovalDate());
        
        if (pvo.getState() != null)
        {
            if (pvo.getState().getId()
                    .equals(ProgressValidationStateTypes.Create.getValue()))
            {
                this.setUsedBy("CF/STC/AGU");
            }
            else if (pvo
                    .getState()
                    .getId()
                    .equals(ProgressValidationStateTypes.STCEvaluation
                            .getValue()))
            {
                this.setUsedBy("STC/AGU");
            }
            else if (pvo
                    .getState()
                    .getId()
                    .equals(ProgressValidationStateTypes.AGUEvaluation
                            .getValue()))
            {
                this.setUsedBy("AGU");
            }
            else if (pvo.getState().getId()
                    .equals(ProgressValidationStateTypes.Confirmed.getValue()))
            {
                this.setUsedBy("");
            }
        }
        
        this.setProgressName(new StringBuilder()
                .append(Utils.getString("Resources",
                        "progressValidationFlowViewProgress", null))
                .append(" ").append(number).toString());
        
        try
        {
            this.setStateString(pvo.getState().getValue());
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        // TODO: Add edit and show control
        
        this.setEditable(true);
        this.setCanShow(true);
    }
    
    /**
     * Sets editable
     * @param editable the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }
    
    /**
     * Gets editable
     * @return editable the editable
     */
    public boolean isEditable()
    {
        return editable;
    }
    
    /**
     * Sets canShow
     * @param canShow the canShow to set
     */
    public void setCanShow(boolean canShow)
    {
        this.canShow = canShow;
    }
    
    /**
     * Gets canShow
     * @return canShow the canShow
     */
    public boolean isCanShow()
    {
        return canShow;
    }
    
    /**
     * Sets progressName
     * @param progressName the progressName to set
     */
    public void setProgressName(String progressName)
    {
        this.progressName = progressName;
    }
    
    /**
     * Gets progressName
     * @return progressName the progressName
     */
    public String getProgressName()
    {
        return progressName;
    }
    
    /**
     * Sets stateString
     * @param stateString the stateString to set
     */
    public void setStateString(String stateString)
    {
        this.stateString = stateString;
    }
    
    /**
     * Gets stateString
     * @return stateString the stateString
     */
    public String getStateString()
    {
        return stateString;
    }
    
    /**
     * Sets usedBy
     * @param usedBy the usedBy to set
     */
    public void setUsedBy(String usedBy)
    {
        this.usedBy = usedBy;
    }
    
    /**
     * Gets usedBy
     * @return usedBy the usedBy
     */
    public String getUsedBy()
    {
        return usedBy;
    }
    
    /**
     * Sets sendable
     * @param sendable the sendable to set
     */
    public void setSendable(boolean sendable)
    {
        this.sendable = sendable;
    }
    
    /**
     * Gets sendable
     * @return sendable the sendable
     */
    public boolean isSendable()
    {
        return sendable;
    }
    
    /**
     * Sets deniable
     * @param deniable the deniable to set
     */
    public void setDeniable(boolean deniable)
    {
        this.deniable = deniable;
    }
    
    /**
     * Gets deniable
     * @return deniable the deniable
     */
    public boolean isDeniable()
    {
        return deniable;
    }
}
