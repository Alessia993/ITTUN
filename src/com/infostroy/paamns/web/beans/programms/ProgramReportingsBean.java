/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.ReportingsLevel;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Reportings;
import com.infostroy.paamns.web.beans.EntityViewBean;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class ProgramReportingsBean extends EntityViewBean<Reportings>
{
	
	private List<SelectItem> reportings;
	
	private Long selectedReporting;
	
	private String currentAction;
	
	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
	 */
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, PersistenceBeanException
	{
		List<Reportings> reps = BeansFactory.Reportings().loadByLevelAndRoles(ReportingsLevel.PROGRAM, Arrays.asList(getCurrentUser().getRolesNames().split(", ")));
		this.setReportings( new ArrayList<SelectItem>());
		this.getReportings().add(SelectItemHelper.getFirst());
		this.setCurrentAction(null);
		for(Reportings rep : reps){
			
			this.getReportings().add(new SelectItem(rep.getId(), rep.getLabel()));
		}
		
	}

	/* (non-Javadoc)
	 * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{
		// TODO Auto-generated method stub
		
	}

	public List<SelectItem> getReportings()
	{
		return reportings;
	}

	public void setReportings(List<SelectItem> reportings)
	{
		this.reportings = reportings;
	}

	public Long getSelectedReporting()
	{
		return selectedReporting;
	}

	public void setSelectedReporting(Long selectedReporting)
	{
		this.selectedReporting = selectedReporting;
	}

	public String getCurrentAction()
	{
		return (String)this.getViewState().get("currentAction");
	}

	public void setCurrentAction(String currentAction)
	{
		this.getViewState().put("currentAction", currentAction);
	}
	
	public void reportingChange(ValueChangeEvent event){
		try{
			Long newValue = (Long)event.getNewValue();
			this.setCurrentAction(BeansFactory.Reportings().Load(newValue).getAction());
		}catch(Exception e){
			log.error("Error in reportings", e);
			this.setCurrentAction(null);
		}
		
	}

}
