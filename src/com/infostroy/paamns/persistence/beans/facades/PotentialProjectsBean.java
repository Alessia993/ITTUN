package com.infostroy.paamns.persistence.beans.facades;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.ProgramPriority;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;

public class PotentialProjectsBean extends PersistenceEntityBean<PotentialProjects>
{
    @SuppressWarnings("unchecked")
    public List<PotentialProjects> LoadByUser(Long id) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(PotentialProjects.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("user.id", id));
        List<PotentialProjects> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<PotentialProjects> LoadByUserAndFilters(Long userId, String projectAcronym, String projectTitle, String leadNameOrganization, String programPriority) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(PotentialProjects.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("user.id", userId));
        if(projectAcronym != null && !projectAcronym.isEmpty())
        {
            criterion.add(Restrictions.like("projectAcronym", projectAcronym,
                    MatchMode.ANYWHERE));
        }
        if(projectTitle != null && !projectTitle.isEmpty())
        {
            criterion.add(Restrictions.like("projectTitle", projectTitle,
                    MatchMode.ANYWHERE));
        }
        if(leadNameOrganization != null && !leadNameOrganization.isEmpty())
        {
            criterion.add(Restrictions.like("leadNameOrganization", leadNameOrganization,
                    MatchMode.ANYWHERE));
        }
        if(programPriority != null && !programPriority.isEmpty())
        {
            List<ProgramPriority> programPriorities = new ArrayList<ProgramPriority>();
            for(ProgramPriority item : ProgramPriority.values())
            {
                if(item.toString().toLowerCase().contains(programPriority.toLowerCase()))
                {
                    programPriorities.add(item);
                }
            }
            if(programPriorities.isEmpty())
            {
                criterion.add(Restrictions.isNull("programPriority"));
            }
            else
            {
                criterion.add(Restrictions.in("programPriority", programPriorities));
            }
        }
        List<PotentialProjects> list = criterion.list();
        return list;
    }
    
    @SuppressWarnings("unchecked")
    public List<PotentialProjects> LoadByFilters(String projectAcronym, String projectTitle, String leadNameOrganization, String programPriority) throws PersistenceBeanException,
            PersistenceBeanException
    {
        Criteria criterion = PersistenceSessionManager
                .getBean()
                .getSession()
                .createCriteria(PotentialProjects.class)
                .add(Restrictions.eq("deleted", false));
        if(projectAcronym != null && !projectAcronym.isEmpty())
        {
            criterion.add(Restrictions.like("projectAcronym", projectAcronym,
                    MatchMode.ANYWHERE));
        }
        if(projectTitle != null && !projectTitle.isEmpty())
        {
            criterion.add(Restrictions.like("projectTitle", projectTitle,
                    MatchMode.ANYWHERE));
        }
        if(leadNameOrganization != null && !leadNameOrganization.isEmpty())
        {
            criterion.add(Restrictions.like("leadNameOrganization", leadNameOrganization,
                    MatchMode.ANYWHERE));
        }
        if(programPriority != null && !programPriority.isEmpty())
        {
            List<ProgramPriority> programPriorities = new ArrayList<ProgramPriority>();
            for(ProgramPriority item : ProgramPriority.values())
            {
                if(item.toString().toLowerCase().contains(programPriority.toLowerCase()))
                {
                    programPriorities.add(item);
                }
            }
            if(programPriorities.isEmpty())
            {
                criterion.add(Restrictions.isNull("programPriority"));
            }
            else
            {
                criterion.add(Restrictions.in("programPriority", programPriorities));
            }
        }
        List<PotentialProjects> list = criterion.list();
        return list;
    }
    
}
