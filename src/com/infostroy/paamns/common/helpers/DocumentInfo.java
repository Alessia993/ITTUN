/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.util.Date;

import com.infostroy.paamns.persistence.beans.entities.domain.Category;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class DocumentInfo
{
    public DocumentInfo(Date date, String name, String fileName,
            String protNumber, Category category, boolean isPublic, String signflag)
    {
        super();
        this.setDate(date);
        this.name = name;
        this.fileName = fileName;
        this.setIsPublic(isPublic);
        this.setProtNumber(protNumber);
        this.category = category;
        this.signflag = signflag;
        //this.hashfile = hashfile;
    }
    
    public DocumentInfo(Date date, String name, String fileName,
            String protNumber, Category category, String signflag)
    {
        super();
        this.setDate(date);
        this.name = name;
        this.fileName = fileName;
        this.setProtNumber(protNumber);
        this.category = category;
        this.signflag = signflag;
        //this.hashfile = hashfile;
    }
    
    public DocumentInfo(Date date, String name, String fileName,
            Category category, String signflag)
    {
        super();
        this.setDate(date);
        this.name = name;
        this.fileName = fileName;
        this.category = category;
        this.signflag = signflag;
        //this.hashfile = hashfile;
    }
    
    private String   protNumber;
    
    private Date     date;
    
    private boolean  isPublic;
    
    private String   name;
    
    private String   fileName;
    
    private Category category;

	private String signflag;
	
	//private String hashfile;
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public void setProtNumber(String protNumber)
    {
        this.protNumber = protNumber;
    }
    
    public String getProtNumber()
    {
        return protNumber;
    }
 
    
    public void setSignFlag(String signflag)
    {
        this.signflag = signflag;
    }
    
    public String getSignFlag()
    {
        return signflag;
    }
    
    /*public void setHashFile(String hashfile)
    {
        this.hashfile = hashfile;
    }
    
    public String getHashFile()
    {
        return hashfile;
    } */
    /**
     * Sets isPublic
     * 
     * @param isPublic
     *            the isPublic to set
     */
    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }
    
    /**
     * Gets isPublic
     * 
     * @return isPublic the isPublic
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }
    
    /**
     * Gets category
     * @return category the category
     */
    public Category getCategory()
    {
        return category;
    }
    
    /**
     * Sets category
     * @param category the category to set
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }
}
