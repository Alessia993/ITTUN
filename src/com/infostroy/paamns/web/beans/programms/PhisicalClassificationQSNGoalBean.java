/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PhisicalClassificationPriorityThema;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PhisicalClassificationQSNGoal;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.QSNGoal;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PhisicalClassificationQSNGoalBean extends
        EntityEditBean<PhisicalClassificationPriorityThema>
{
    private String                        errorMessage;
    
    private static HashMap<Long, QSNGoal> qsnGoal;
    
    private String                        tab1Style;
    
    private String                        tab2Style;
    
    private String                        tab3Style;
    
    //private String                        tab4Style;
    
    //private String                        tab5Style;
    
    private static List<QSNGoalWebBean>   qsnAsse1List;
    
    private static List<QSNGoalWebBean>   qsnAsse2List;
    
    private static List<QSNGoalWebBean>   qsnAsse3List;
    
    //private static List<QSNGoalWebBean>   qsnAsse4List;
    
    //private static List<QSNGoalWebBean>   qsnAsse5List;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#AfterSave()
     */
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#GoBack()
     */
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.INDEX);
    }
    
	private void InitQSNLists() throws PersistenceBeanException
	{
		qsnAsse1List = new ArrayList<QSNGoalWebBean>();
		qsnAsse2List = new ArrayList<QSNGoalWebBean>();
		qsnAsse3List = new ArrayList<QSNGoalWebBean>();
		/*
		 * qsnAsse4List = new ArrayList<QSNGoalWebBean>(); qsnAsse5List = new
		 * ArrayList<QSNGoalWebBean>();
		 */
		List<QSNGoal> list = BeansFactory.QSNGoal().Load();
		List<QSNGoal> list1 = BeansFactory.PhisicalClassificationQSNGoal()
				.getByAsse(1);
		List<QSNGoal> list2 = BeansFactory.PhisicalClassificationQSNGoal()
				.getByAsse(2);
		List<QSNGoal> list3 = BeansFactory.PhisicalClassificationQSNGoal()
				.getByAsse(3);
		/*
		 * List<QSNGoal> list4 = BeansFactory.PhisicalClassificationQSNGoal()
		 * .getByAsse(4); List<QSNGoal> list5 =
		 * BeansFactory.PhisicalClassificationQSNGoal() .getByAsse(5);
		 */

		if (this.getCurrentUser().getAdmin())
		{
			for (QSNGoal item : list)
			{
				qsnAsse1List.add(new QSNGoalWebBean(item, list1 == null
						|| list1.contains(item)));
				qsnAsse2List.add(new QSNGoalWebBean(item, list2 == null
						|| list2.contains(item)));
				qsnAsse3List.add(new QSNGoalWebBean(item, list3 == null
						|| list3.contains(item)));
				/*
				 * qsnAsse4List.add(new QSNGoalWebBean(item, list4 == null ||
				 * list4.contains(item))); qsnAsse5List.add(new
				 * QSNGoalWebBean(item, list5 == null || list5.contains(item)));
				 */
			}
		}
		else
		{
			for (QSNGoal item : list1)
			{
				qsnAsse1List.add(new QSNGoalWebBean(item, false));
			}
			for (QSNGoal item : list2)
			{
				qsnAsse2List.add(new QSNGoalWebBean(item, false));
			}
			for (QSNGoal item : list3)
			{
				qsnAsse3List.add(new QSNGoalWebBean(item, false));
			}
		}
	}
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException,
            PersistenceBeanException
    {
        this.InitQSNLists();
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException,
            PersistenceBeanException
    {
        
        qsnGoal = new HashMap<Long, QSNGoal>();
        for (QSNGoal item : BeansFactory.QSNGoal().Load())
        {
            qsnGoal.put(item.getId(), item);
        }
    }
    
    public Boolean BeforeSave() throws PersistenceBeanException
    {
        boolean isAsse1 = isSelectedQSN(qsnAsse1List);
        boolean isAsse2 = isSelectedQSN(qsnAsse2List);
        boolean isAsse3 = isSelectedQSN(qsnAsse3List);
      /*  boolean isAsse4 = isSelectedQSN(qsnAsse4List);
        boolean isAsse5 = isSelectedQSN(qsnAsse5List);
      */  if (!isAsse1 || !isAsse2 || !isAsse3 /*|| !isAsse4 || !isAsse5*/)
        {
            this.setErrorMessage(Utils.getString("Resources",
                    "validator.selectQSN", null));
            
            if (!isAsse1)
            {
                this.setTab1Style(ValidatorHelper.errorStyle);
            }
            
            if (!isAsse2)
            {
                this.setTab2Style(ValidatorHelper.errorStyle);
            }
            
            if (!isAsse3)
            {
                this.setTab3Style(ValidatorHelper.errorStyle);
            }
            
           /* if (!isAsse4)
            {
                this.setTab4Style(ValidatorHelper.errorStyle);
            }
            
            if (!isAsse5)
            {
                this.setTab5Style(ValidatorHelper.errorStyle);
            }*/
            return false;
        }
        
        if (BeansFactory.PhisicalClassificationQSNGoal().Load().size() != 0)
        {
            BeansFactory.PhisicalClassificationQSNGoal().DeleteAll();
        }
        return true;
    }
    
    /**
     * @param list
     * @return
     */
    private boolean isSelectedQSN(List<QSNGoalWebBean> list)
    {
        for (QSNGoalWebBean item : list)
        {
            if (item.getSelected())
            {
                return true;
            }
        }
        
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#SaveEntity()
     */
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException,
            PersistenceBeanException, PersistenceBeanException
    {
        for (QSNGoalWebBean item : qsnAsse1List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationQSNGoal goal = new PhisicalClassificationQSNGoal();
                goal.setAsse(1);
                goal.setQsnGoal(qsnGoal.get(item.getId()));
                BeansFactory.PhisicalClassificationQSNGoal().SaveInTransaction(
                        goal);
            }
        }
        
        for (QSNGoalWebBean item : qsnAsse2List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationQSNGoal goal = new PhisicalClassificationQSNGoal();
                goal.setAsse(2);
                goal.setQsnGoal(qsnGoal.get(item.getId()));
                BeansFactory.PhisicalClassificationQSNGoal().SaveInTransaction(
                        goal);
            }
        }
        
        for (QSNGoalWebBean item : qsnAsse3List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationQSNGoal goal = new PhisicalClassificationQSNGoal();
                goal.setAsse(3);
                goal.setQsnGoal(qsnGoal.get(item.getId()));
                BeansFactory.PhisicalClassificationQSNGoal().SaveInTransaction(
                        goal);
            }
        }
        
        /*for (QSNGoalWebBean item : qsnAsse4List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationQSNGoal goal = new PhisicalClassificationQSNGoal();
                goal.setAsse(4);
                goal.setQsnGoal(qsnGoal.get(item.getId()));
                BeansFactory.PhisicalClassificationQSNGoal().SaveInTransaction(
                        goal);
            }
        }
        */
        /*for (QSNGoalWebBean item : qsnAsse5List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationQSNGoal goal = new PhisicalClassificationQSNGoal();
                goal.setAsse(5);
                goal.setQsnGoal(qsnGoal.get(item.getId()));
                BeansFactory.PhisicalClassificationQSNGoal().SaveInTransaction(
                        goal);
            }
        }*/
    }
    
    /**
     * Gets errorMessage
     * @return errorMessage the errorMessage
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }
    
    /**
     * Sets errorMessage
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Gets qsnGoal
     * @return qsnGoal the qsnGoal
     */
    public static HashMap<Long, QSNGoal> getQsnGoal()
    {
        return qsnGoal;
    }
    
    /**
     * Sets qsnGoal
     * @param qsnGoal the qsnGoal to set
     */
    public static void setQsnGoal(HashMap<Long, QSNGoal> qsnGoal)
    {
        PhisicalClassificationQSNGoalBean.qsnGoal = qsnGoal;
    }
    
    /**
     * Gets qsnAsse1List
     * @return qsnAsse1List the qsnAsse1List
     */
    public List<QSNGoalWebBean> getQsnAsse1List()
    {
        return PhisicalClassificationQSNGoalBean.qsnAsse1List;
    }
    
    /**
     * Sets qsnAsse1List
     * @param qsnAsse1List the qsnAsse1List to set
     */
    public void setQsnAsse1List(List<QSNGoalWebBean> qsnAsse1List)
    {
        PhisicalClassificationQSNGoalBean.qsnAsse1List = qsnAsse1List;
    }
    
    /**
     * Gets qsnAsse2List
     * @return qsnAsse2List the qsnAsse2List
     */
    public List<QSNGoalWebBean> getQsnAsse2List()
    {
        return PhisicalClassificationQSNGoalBean.qsnAsse2List;
    }
    
    /**
     * Sets qsnAsse2List
     * @param qsnAsse2List the qsnAsse2List to set
     */
    public void setQsnAsse2List(List<QSNGoalWebBean> qsnAsse2List)
    {
        PhisicalClassificationQSNGoalBean.qsnAsse2List = qsnAsse2List;
    }
    
    /**
     * Gets qsnAsse3List
     * @return qsnAsse3List the qsnAsse3List
     */
    public List<QSNGoalWebBean> getQsnAsse3List()
    {
        return PhisicalClassificationQSNGoalBean.qsnAsse3List;
    }
    
    /**
     * Sets qsnAsse3List
     * @param qsnAsse3List the qsnAsse3List to set
     */
    public void setQsnAsse3List(List<QSNGoalWebBean> qsnAsse3List)
    {
        PhisicalClassificationQSNGoalBean.qsnAsse3List = qsnAsse3List;
    }
    
    /**
     * Gets qsnAsse4List
     * @return qsnAsse4List the qsnAsse4List
     */
   /* public List<QSNGoalWebBean> getQsnAsse4List()
    {
        return PhisicalClassificationQSNGoalBean.qsnAsse4List;
    }
   */ 
    /**
     * Sets qsnAsse4List
     * @param qsnAsse4List the qsnAsse4List to set
     */
   /* public void setQsnAsse4List(List<QSNGoalWebBean> qsnAsse4List)
    {
        PhisicalClassificationQSNGoalBean.qsnAsse4List = qsnAsse4List;
    }
   */ 
    /**
     * Gets qsnAsse5List
     * @return qsnAsse5List the qsnAsse5List
     */
   /* public List<QSNGoalWebBean> getQsnAsse5List()
    {
        return PhisicalClassificationQSNGoalBean.qsnAsse5List;
    }
   */ 
    /**
     * Sets qsnAsse5List
     * @param qsnAsse5List the qsnAsse5List to set
     */
    /*public void setQsnAsse5List(List<QSNGoalWebBean> qsnAsse5List)
    {
        PhisicalClassificationQSNGoalBean.qsnAsse5List = qsnAsse5List;
    }
    */
    /**
     * Gets tab1Style
     * @return tab1Style the tab1Style
     */
    public String getTab1Style()
    {
        return tab1Style;
    }
    
    /**
     * Sets tab1Style
     * @param tab1Style the tab1Style to set
     */
    public void setTab1Style(String tab1Style)
    {
        this.tab1Style = tab1Style;
    }
    
    /**
     * Gets tab2Style
     * @return tab2Style the tab2Style
     */
    public String getTab2Style()
    {
        return tab2Style;
    }
    
    /**
     * Sets tab2Style
     * @param tab2Style the tab2Style to set
     */
    public void setTab2Style(String tab2Style)
    {
        this.tab2Style = tab2Style;
    }
    
    /**
     * Gets tab3Style
     * @return tab3Style the tab3Style
     */
    public String getTab3Style()
    {
        return tab3Style;
    }
    
    /**
     * Sets tab3Style
     * @param tab3Style the tab3Style to set
     */
    public void setTab3Style(String tab3Style)
    {
        this.tab3Style = tab3Style;
    }
    
    /**
     * Gets tab4Style
     * @return tab4Style the tab4Style
     */
    /*public String getTab4Style()
    {
        return tab4Style;
    }
    */
    /**
     * Sets tab4Style
     * @param tab4Style the tab4Style to set
     */
   /* public void setTab4Style(String tab4Style)
    {
        this.tab4Style = tab4Style;
    }
   */ 
    /**
     * Gets tab5Style
     * @return tab5Style the tab5Style
     */
   /* public String getTab5Style()
    {
        return tab5Style;
    }
   */ 
    /**
     * Sets tab5Style
     * @param tab5Style the tab5Style to set
     */
   /* public void setTab5Style(String tab5Style)
    {
        this.tab5Style = tab5Style;
    }
   */ 
}
