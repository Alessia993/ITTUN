/**
 * 
 */
package com.infostroy.paamns.web.beans.dynamicdatastructure;

/**
 *
 * @author Sergei Vasnev
 * InfoStroy Co., 2015.
 *
 */
public class DataStructureColumn
{
	private Integer number;
	
	private String header;
	
	
	
	public DataStructureColumn(Integer number, String header)
	{
		super();
		this.number = number;
		this.header = header;
	}
	public Integer getNumber()
	{
		return number;
	}
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	public String getHeader()
	{
		return header;
	}
	public void setHeader(String header)
	{
		this.header = header;
	}
	
	
}
