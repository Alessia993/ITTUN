/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.infostroy.paamns.common.enums.ReportingsLevel;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.web.beans.LocalizeBean;

/**
 *
 * @author Sergey Vasnev
 * InfoStroy Co., 2015.
 *
 */
@Entity
@Table(name="reportings")
public class Reportings extends
		com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
	@Column(name="name_it")
	private String nameIt;
	
	@Column(name="name_en")
	private String nameEn;
	
	@Column
	private String action;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ReportingsLevel level;
	
	@ManyToMany
	@JoinTable(name="reportings_roles")
	private List<Roles> roles;

	public String getNameIt()
	{
		return nameIt;
	}

	public void setNameIt(String nameIt)
	{
		this.nameIt = nameIt;
	}

	public String getNameEn()
	{
		return nameEn;
	}

	public void setNameEn(String nameEn)
	{
		this.nameEn = nameEn;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public ReportingsLevel getLevel()
	{
		return level;
	}

	public void setLevel(ReportingsLevel level)
	{
		this.level = level;
	}

	public List<Roles> getRoles()
	{
		return roles;
	}

	public void setRoles(List<Roles> roles)
	{
		this.roles = roles;
	}
	
	public String getLabel(){
		if(getLocalizeBean().getCurrentLocale().equals("it")){
			return this.getNameIt();
		}else{
			return this.getNameEn();
		}
	}
	
    private LocalizeBean getLocalizeBean(){
        FacesContext fCtx = FacesContext.getCurrentInstance();
        ELContext elCtx = fCtx.getELContext();
        ExpressionFactory ef = fCtx.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elCtx, "#{LocalizeBean}",
        		LocalizeBean.class);
        LocalizeBean local = (LocalizeBean) ve.getValue(elCtx);
        return local;
    }
}
