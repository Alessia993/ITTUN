/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.FinancialData;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.FinancingPlan;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.ProgramImplementation;
import com.infostroy.paamns.web.beans.EntityViewBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class FinancialDataViewBean extends EntityViewBean<FinancialData>
{
    private List<ProgramImplementation> progImplList;
    
    private List<YearProfile> listYearProfile;
    
   // private List<FinancingPlan>         finPlan;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
     */
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(BeansFactory.FinancialData().Load());
        setProgImplList(BeansFactory.ProgramImplementation().Load());
      //  setFinPlan(BeansFactory.FinancingPlan().Load());
        fillYearProfiles();
    }
    
    
    
    /*
     * (non-Javadoc)
     * 
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Sets progImplList
     * 
     * @param progImplList
     *            the progImplList to set
     */
    public void setProgImplList(List<ProgramImplementation> progImplList)
    {
        this.progImplList = progImplList;
    }
    
    private void fillYearProfiles(){
    	this.setListYearProfile(new ArrayList<YearProfile>());
    	
    	this.getListYearProfile().add(new YearProfile("2015", "3.823.381 ,00", "0,00", "0,00", "0,00"));
    	this.getListYearProfile().add(new YearProfile("2016", "4.405.484,00", "166.774,00", "220.541,00", "166.774,00"));
    	this.getListYearProfile().add(new YearProfile("2017", "6.727.571,00", "6.404.125,00", "690.871,00", "250.258,00"));
    	this.getListYearProfile().add(new YearProfile("2018", "5.789.348,00", " 3.103.287,00", "733.871,00", "300.387,00"));
    	this.getListYearProfile().add(new YearProfile("2019", "6.252.816,00", "4.269.417,00", "519.877,00", "667.096,00"));
    	this.getListYearProfile().add(new YearProfile("2020", "6.356.221,00", "4.901.869,00", "533.548,00", "500.322,00"));
    	this.getListYearProfile().add(new YearProfile("2021", "0,00", "5.503.545,00", "366.774,00", "400.258,00"));
    	this.getListYearProfile().add(new YearProfile("2022", "0,00", "6.404.125,00", "270.000,00", "400.258,00"));
    	this.getListYearProfile().add(new YearProfile("2023", "0,00", "2.601.678,00", "0,00", "200.129,00"));
    	this.getListYearProfile().add(new YearProfile("2024", "0,00", "0,00", "0,00", "450.000,00"));
    	this.getListYearProfile().add(new YearProfile(Utils.getString("Resources", "total", null).toUpperCase(), "33.354.820,00", " 33.354.820,00", "3.335.482,00", "3.335.482,00"));
    }
    
    public class YearProfile{
    	
    	public YearProfile(String year, String fesr, String cpn, String techass, String paytechass){
    		this.setCpn(cpn);
    		this.setFesr(fesr);
    		this.setYear(year);
    		this.setTechass(techass);
    		this.setPaytechass(paytechass);
    	}
    	
    	private String year;
    	
    	private String fesr;
    	
    	private String cpn;
    	
    	private String techass;
    	
    	private String paytechass;

		/**
		 * Gets year
		 * @return year the year
		 */
		public String getYear()
		{
			return year;
		}

		/**
		 * Sets year
		 * @param year the year to set
		 */
		public void setYear(String year)
		{
			this.year = year;
		}
		
		/**
		 * Gets year
		 * @return year the year
		 */
		public String getTechass()
		{
			return techass;
		}

		/**
		 * Sets year
		 * @param year the year to set
		 */
		public void setTechass(String techass)
		{
			this.techass = techass;
		}
		
		
		/**
		 * Gets year
		 * @return year the year
		 */
		public String getPaytechass()
		{
			return paytechass;
		}

		/**
		 * Sets year
		 * @param year the year to set
		 */
		public void setPaytechass(String paytechass)
		{
			this.paytechass = paytechass;
		}

		/**
		 * Gets fesr
		 * @return fesr the fesr
		 */
		public String getFesr()
		{
			return fesr;
		}

		/**
		 * Sets fesr
		 * @param fesr the fesr to set
		 */
		public void setFesr(String fesr)
		{
			this.fesr = fesr;
		}

		/**
		 * Gets cpn
		 * @return cpn the cpn
		 */
		public String getCpn()
		{
			return cpn;
		}

		/**
		 * Sets cpn
		 * @param cpn the cpn to set
		 */
		public void setCpn(String cpn)
		{
			this.cpn = cpn;
		}
    	
    	
    }
    
    /**
     * Gets progImplList
     * 
     * @return progImplList the progImplList
     */
    public List<ProgramImplementation> getProgImplList()
    {
        return progImplList;
    }



	/**
	 * Gets listYearProfile
	 * @return listYearProfile the listYearProfile
	 */
	public List<YearProfile> getListYearProfile()
	{
		return listYearProfile;
	}



	/**
	 * Sets listYearProfile
	 * @param listYearProfile the listYearProfile to set
	 */
	public void setListYearProfile(List<YearProfile> listYearProfile)
	{
		this.listYearProfile = listYearProfile;
	}
    
    /**
     * Sets finPlan
     * 
     * @param finPlan
     *            the finPlan to set
     */
    /*public void setFinPlan(List<FinancingPlan> finPlan)
    {
        this.finPlan = finPlan;
    }
    */
    /**
     * Gets finPlan
     * 
     * @return finPlan the finPlan
     */
    /*public List<FinancingPlan> getFinPlan()
    {
        return finPlan;
    }
    */
    
    
}
