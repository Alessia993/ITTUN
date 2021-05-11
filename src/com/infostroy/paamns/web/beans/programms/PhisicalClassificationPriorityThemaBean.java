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
import com.infostroy.paamns.persistence.beans.entities.domain.statics.PrioritaryReasons;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.Roles;
import com.infostroy.paamns.web.beans.EntityEditBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class PhisicalClassificationPriorityThemaBean extends
        EntityEditBean<PhisicalClassificationPriorityThema>
{
    private boolean                                 selectAll1;
    
    private boolean                                 selectAll2;
    
    private boolean                                 selectAll3;
    
    //private boolean                                 selectAll4;
    
    //private boolean                                 selectAll5;
    
    private String                                  errorMessage;
    
    private static HashMap<Long, PrioritaryReasons> prioritaryReasons;
    
    private String                                  tab1Style;
    
    private String                                  tab2Style;
    
    private String                                  tab3Style;
    
   // private String                                  tab4Style;
    
   // private String                                  tab5Style;
    
    private static List<PrioritaryReasonsWebBean>   prAsse1List;
    
    private static List<PrioritaryReasonsWebBean>   prAsse2List;
    
    private static List<PrioritaryReasonsWebBean>   prAsse3List;
    
   // private static List<PrioritaryReasonsWebBean>   prAsse4List;
    
   // private static List<PrioritaryReasonsWebBean>   prAsse5List;
    
    private static List<PhisicalClassificationPriorityThema> pcptAsse1List;
    
    private static List<PhisicalClassificationPriorityThema> pcptAsse2List;
    
    private static List<PhisicalClassificationPriorityThema> pcptAsse3List;
    
    // private static List<PhisicalClassificationPriorityThema> pcptAsse4List;
    
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
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {

        this.InitPRLists();
    }
    
    public void doSelectAll1()
    {
        for (PrioritaryReasonsWebBean item : prAsse1List)
        {
        	item.setSelected(this.getSelectAll1()); //this.getSelectAll1(); //item.setSelected(true); 
        }
    }
    
    public void doSelectAll2()
    {
        for (PrioritaryReasonsWebBean item : prAsse2List)
        {
        	item.setSelected(this.getSelectAll2()); //this.getSelectAll2();  //item.setSelected(true);
        }
    }
    
    public void doSelectAll3()
    {
        for (PrioritaryReasonsWebBean item : prAsse3List)
        {
        	item.setSelected(this.getSelectAll3()); //this.getSelectAll3();   //item.setSelected(true);
        }
    }
    
    /* public void doSelectAll4()
    {
        for (PrioritaryReasonsWebBean item : prAsse4List)
        {
            item.setSelected(this.getSelectAll4());
        }
    } */
    
    /*
    public void doSelectAll5()
    {
        for (PrioritaryReasonsWebBean item : prAsse5List)
        {
            item.setSelected(this.getSelectAll5());
        }
    }
    */
    public void selectOne1()
    {
        this.selectAll1 = false;
    }
    
    public void selectOne2()
    {
        this.selectAll2 = false;
    }
    
    public void selectOne3()
    {
        this.selectAll3 = false;
    }
    
    /* public void selectOne4()
    {
        this.selectAll4 = false;
    } */
    
   /* public void selectOne5()
    {
        this.selectAll5 = false;
    }
   */ 
    /**
     * @throws PersistenceBeanException
     */
    
    private void InitPRLists() throws PersistenceBeanException
    {
        prAsse1List = new ArrayList<PrioritaryReasonsWebBean>();
        prAsse2List = new ArrayList<PrioritaryReasonsWebBean>();
        prAsse3List = new ArrayList<PrioritaryReasonsWebBean>();
        //prAsse4List = new ArrayList<PrioritaryReasonsWebBean>();
      /*  prAsse5List = new ArrayList<PrioritaryReasonsWebBean>();
      */ 
        
        
        List<PrioritaryReasons> list = BeansFactory.PrioritaryReasons()
                .LoadNotLast();
        List<PrioritaryReasons> list1 = BeansFactory.PrioritaryReasons()
                .LoadNotLast();
       /* List<PrioritaryReasons> list1 = BeansFactory
                .PhisicalClassificationPriorityThema().getByAsse(1);*/
        List<PrioritaryReasons> list2 = BeansFactory
                .PhisicalClassificationPriorityThema().getByAsse(2);
        List<PrioritaryReasons> list3 = BeansFactory
                .PhisicalClassificationPriorityThema().getByAsse(3);
        /*
        List<PrioritaryReasons> list4 = BeansFactory
                .PhisicalClassificationPriorityThema().getByAsse(4);
                */
        /*List<PrioritaryReasons> list5 = BeansFactory
                .PhisicalClassificationPriorityThema().getByAsse(5);
        */
        
		if (this.getCurrentUser().getAdmin())
		{
			for (PrioritaryReasons item : list)
			{

				prAsse1List.add(new PrioritaryReasonsWebBean(item,
						list1 == null || list1.contains(item)));
				prAsse2List.add(new PrioritaryReasonsWebBean(item,
						list2 == null || list2.contains(item)));
				prAsse3List.add(new PrioritaryReasonsWebBean(item,
						list3 == null || list3.contains(item)));
				/*
				prAsse4List.add(new PrioritaryReasonsWebBean(item, 
						list4 == null || list4.contains(item)));
						*/
				/*
				 * prAsse5List.add(new PrioritaryReasonsWebBean(item,
				 * list5 == null || list5.contains(item)));
				 */
			}
		}
		else
		{
			for (PrioritaryReasons item : list1)
			{
				prAsse1List.add(new PrioritaryReasonsWebBean(item, false));
			}
			for (PrioritaryReasons item : list2)
			{
				prAsse2List.add(new PrioritaryReasonsWebBean(item, false));
			}
			for (PrioritaryReasons item : list3)
			{
				prAsse3List.add(new PrioritaryReasonsWebBean(item, false));
			}
			/*
			for (PrioritaryReasons item : list4)
			{
				prAsse4List.add(new PrioritaryReasonsWebBean(item, false));
			} */
		}

    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws PersistenceBeanException,
            PersistenceBeanException
    {
        prioritaryReasons = new HashMap<Long, PrioritaryReasons>();
        for (PrioritaryReasons item : BeansFactory.PrioritaryReasons().Load())
        {
            prioritaryReasons.put(item.getId(), item);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityEditBean#BeforeSave()
     */
    public Boolean BeforeSave() throws PersistenceBeanException
    {
        boolean isAsse1 = isSelectedPR(prAsse1List);
        boolean isAsse2 = isSelectedPR(prAsse2List);
        boolean isAsse3 = isSelectedPR(prAsse3List);
        //boolean isAsse4 = isSelectedPR(prAsse4List);
       /* boolean isAsse5 = isSelectedPR(prAsse5List);
       */ if (!isAsse1 )  // || !isAsse2 || !isAsse3 || !isAsse4 /*|| !isAsse5*/)
        {
            this.setErrorMessage(Utils.getString("Resources",
                    "validatorSelectPT", null));
            
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
            } */
            
            /*if (!isAsse5)
            {
                this.setTab5Style(ValidatorHelper.errorStyle);
            }
            */
            return false;
        }
        
        if (BeansFactory.PhisicalClassificationPriorityThema().Load().size() != 0)
        {
            BeansFactory.PhisicalClassificationPriorityThema().DeleteAll();
        }
        return true;
    }
    
    private boolean isSelectedPR(List<PrioritaryReasonsWebBean> list)
    {
        for (PrioritaryReasonsWebBean item : list)
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
            PersistenceBeanException, NumberFormatException
    {
        for (PrioritaryReasonsWebBean item : prAsse1List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationPriorityThema prioritaryReason = new PhisicalClassificationPriorityThema();
                prioritaryReason.setAsse(1);
                prioritaryReason.setPrioritaryReason(prioritaryReasons.get(item
                        .getId()));
                BeansFactory.PhisicalClassificationPriorityThema()
                        .SaveInTransaction(prioritaryReason);
            }
        }
        
        for (PrioritaryReasonsWebBean item : prAsse2List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationPriorityThema prioritaryReason = new PhisicalClassificationPriorityThema();
                prioritaryReason.setAsse(2);
                prioritaryReason.setPrioritaryReason(prioritaryReasons.get(item
                        .getId()));
                BeansFactory.PhisicalClassificationPriorityThema()
                        .SaveInTransaction(prioritaryReason);
            }
        }
        
        for (PrioritaryReasonsWebBean item : prAsse3List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationPriorityThema prioritaryReason = new PhisicalClassificationPriorityThema();
                prioritaryReason.setAsse(3);
                prioritaryReason.setPrioritaryReason(prioritaryReasons.get(item
                        .getId()));
                BeansFactory.PhisicalClassificationPriorityThema()
                        .SaveInTransaction(prioritaryReason);
            }
        }
        
        /*for (PrioritaryReasonsWebBean item : prAsse4List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationPriorityThema prioritaryReason = new PhisicalClassificationPriorityThema();
                prioritaryReason.setAsse(4);
                prioritaryReason.setPrioritaryReason(prioritaryReasons.get(item
                        .getId()));
                BeansFactory.PhisicalClassificationPriorityThema()
                        .SaveInTransaction(prioritaryReason);
            }
        } */
        
       /* for (PrioritaryReasonsWebBean item : prAsse5List)
        {
            if (item.getSelected())
            {
                PhisicalClassificationPriorityThema prioritaryReason = new PhisicalClassificationPriorityThema();
                prioritaryReason.setAsse(5);
                prioritaryReason.setPrioritaryReason(prioritaryReasons.get(item
                        .getId()));
                BeansFactory.PhisicalClassificationPriorityThema()
                        .SaveInTransaction(prioritaryReason);
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
     * Gets prioritaryReasons
     * @return prioritaryReasons the prioritaryReasons
     */
    public static HashMap<Long, PrioritaryReasons> getPrioritaryReasons()
    {
        return prioritaryReasons;
    }
    
    /**
     * Sets prioritaryReasons
     * @param prioritaryReasons the prioritaryReasons to set
     */
    public static void setPrioritaryReasons(
            HashMap<Long, PrioritaryReasons> prioritaryReasons)
    {
        PhisicalClassificationPriorityThemaBean.prioritaryReasons = prioritaryReasons;
    }
    
    /**
     * Gets prAsse1List
     * @return prAsse1List the prAsse1List
     */
    public List<PrioritaryReasonsWebBean> getPrAsse1List()
    {
        return PhisicalClassificationPriorityThemaBean.prAsse1List;
    }
    
    /**
     * Sets prAsse1List
     * @param prAsse1List the prAsse1List to set
     */
    public void setPrAsse1List(List<PrioritaryReasonsWebBean> prAsse1List)
    {
        PhisicalClassificationPriorityThemaBean.prAsse1List = prAsse1List;
    }
    
    /**
     * Gets prAsse2List
     * @return prAsse2List the prAsse2List
     */
    public List<PrioritaryReasonsWebBean> getPrAsse2List()
    {
        return PhisicalClassificationPriorityThemaBean.prAsse2List;
    }
    
    /**
     * Sets prAsse2List
     * @param prAsse2List the prAsse2List to set
     */
    public void setPrAsse2List(List<PrioritaryReasonsWebBean> prAsse2List)
    {
        PhisicalClassificationPriorityThemaBean.prAsse2List = prAsse2List;
    }
    
    /**
     * Gets prAsse3List
     * @return prAsse3List the prAsse3List
     */
    public List<PrioritaryReasonsWebBean> getPrAsse3List()
    {
        return PhisicalClassificationPriorityThemaBean.prAsse3List;
    }
    
    /**
     * Sets prAsse3List
     * @param prAsse3List the prAsse3List to set
     */
    public void setPrAsse3List(List<PrioritaryReasonsWebBean> prAsse3List)
    {
        PhisicalClassificationPriorityThemaBean.prAsse3List = prAsse3List;
    }
    
    /**
     * Gets prAsse4List
     * @return prAsse4List the prAsse4List
     */
    /* public List<PrioritaryReasonsWebBean> getPrAsse4List()
    {
        return PhisicalClassificationPriorityThemaBean.prAsse4List;
    } */
   
    /**
     * Sets prAsse4List
     * @param prAsse4List the prAsse4List to set
     */
    /* public void setPrAsse4List(List<PrioritaryReasonsWebBean> prAsse4List)
    {
        PhisicalClassificationPriorityThemaBean.prAsse4List = prAsse4List;
    } */
    
    /**
     * Gets prAsse5List
     * @return prAsse5List the prAsse5List
     */
  /*  public List<PrioritaryReasonsWebBean> getPrAsse5List()
    {
        return PhisicalClassificationPriorityThemaBean.prAsse5List;
    }
  */  
    /**
     * Sets prAsse5List
     * @param prAsse5List the prAsse5List to set
     */
  /*  public void setPrAsse5List(List<PrioritaryReasonsWebBean> prAsse5List)
    {
        PhisicalClassificationPriorityThemaBean.prAsse5List = prAsse5List;
    }
  */  
    /**
     * Sets tab1Style
     * @param tab1Style the tab1Style to set
     */
    public void setTab1Style(String tab1Style)
    {
        this.tab1Style = tab1Style;
    }
    
    /**
     * Gets tab1Style
     * @return tab1Style the tab1Style
     */
    public String getTab1Style()
    {
        return tab1Style;
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
     * Gets tab2Style
     * @return tab2Style the tab2Style
     */
    public String getTab2Style()
    {
        return tab2Style;
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
     * Gets tab3Style
     * @return tab3Style the tab3Style
     */
    public String getTab3Style()
    {
        return tab3Style;
    }
    
    /**
     * Sets tab4Style
     * @param tab4Style the tab4Style to set
     */
    /* public void setTab4Style(String tab4Style)
    {
        this.tab4Style = tab4Style;
    } */
    
    /**
     * Gets tab4Style
     * @return tab4Style the tab4Style
     */
    /* public String getTab4Style()
    {
        return tab4Style;
    } */
    
    /**
     * Sets tab5Style
     * @param tab5Style the tab5Style to set
     */
  /*  public void setTab5Style(String tab5Style)
    {
        this.tab5Style = tab5Style;
    }
  */  
    /**
     * Gets tab5Style
     * @return tab5Style the tab5Style
     */
  /*  public String getTab5Style()
    {
        return tab5Style;
    }
  */  
    /**
     * Sets selectAll2
     * @param selectAll2 the selectAll2 to set
     */
    public void setSelectAll2(boolean selectAll2)
    {
        this.selectAll2 = selectAll2;  // true
    }
    
    /**
     * Gets selectAll2
     * @return selectAll2 the selectAll2
     */
    public boolean getSelectAll2()
    {
        return selectAll2; //; true
    }
    
    /**
     * Sets selectAll1
     * @param selectAll1 the selectAll1 to set
     */
    public void setSelectAll1(boolean selectAll1)
    {
        this.selectAll1 = true; //selectAll1;
    }
    
    /**
     * Gets selectAll1
     * @return selectAll1 the selectAll1
     */
    public boolean getSelectAll1()
    {
        return selectAll1; //; true
    }
    
    /**
     * Sets selectAll3
     * @param selectAll3 the selectAll3 to set
     */
    public void setSelectAll3(boolean selectAll3)
    {
        this.selectAll3 = selectAll3; //; true
    }
    
    /**
     * Gets selectAll3
     * @return selectAll3 the selectAll3
     */
    public boolean getSelectAll3()
    {
        return selectAll3; //; true
    }
    
    /**
     * Sets selectAll4
     * @param selectAll4 the selectAll4 to set
     */
    /* public void setSelectAll4(boolean selectAll4)
    {
        this.selectAll4 = selectAll4;
    } */
    
    /**
     * Gets selectAll4
     * @return selectAll4 the selectAll4
     */
    /* public boolean getSelectAll4()
    {
        return selectAll4;
    } */


	/**
	 * Gets pcptAsse1List
	 * @return pcptAsse1List the pcptAsse1List
	 */
	public List<PhisicalClassificationPriorityThema> getPcptAsse1List()
	{
		return this.pcptAsse1List;
	}

	/**
	 * Sets pcptAsse1List
	 * @param pcptAsse1List the pcptAsse1List to set
	 */
	public void setPcptAsse1List(
			List<PhisicalClassificationPriorityThema> pcptAsse1List)
	{
		this.pcptAsse1List = pcptAsse1List;
	}

	/**
	 * Gets pcptAsse2List
	 * @return pcptAsse2List the pcptAsse2List
	 */
	public List<PhisicalClassificationPriorityThema> getPcptAsse2List()
	{
		return this.pcptAsse2List;
	}

	/**
	 * Sets pcptAsse2List
	 * @param pcptAsse2List the pcptAsse2List to set
	 */
	public void setPcptAsse2List(
			List<PhisicalClassificationPriorityThema> pcptAsse2List)
	{
		this.pcptAsse2List = pcptAsse2List;
	}

	/**
	 * Gets pcptAsse3List
	 * @return pcptAsse3List the pcptAsse3List
	 */
	public List<PhisicalClassificationPriorityThema> getPcptAsse3List()
	{
		return this.pcptAsse3List;
	}

	/**
	 * Sets pcptAsse3List
	 * @param pcptAsse3List the pcptAsse3List to set
	 */
	public void setPcptAsse3List(
			List<PhisicalClassificationPriorityThema> pcptAsse3List)
	{
		this.pcptAsse3List = pcptAsse3List;
	}
    
    /**
     * Sets selectAll5
     * @param selectAll5 the selectAll5 to set
     */
    /*public void setSelectAll5(boolean selectAll5)
    {
        this.selectAll5 = selectAll5;
    }
    */
    /**
     * Gets selectAll5
     * @return selectAll5 the selectAll5
     */
   /* public boolean getSelectAll5()
    {
        return selectAll5;
    }
   */ 
    
    
}
