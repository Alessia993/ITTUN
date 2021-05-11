package com.infostroy.paamns.common.helpers.mail;

public class InfoObject
{
	private String	mail;

	private String	name;

	private String	surname;

	private String	projectName;

	private String	password;

	private String	login;

	private String	message;

	private String	number;

	private String	durCompilationDate;

	private String	totalAmountOfDUR;

	private String	certifyDate;

	private String	denomination;

	public InfoObject()
	{
	}

	/**
	 * @param mail
	 * @param name
	 * @param surname
	 * @param projectName
	 * @param password
	 * @param login
	 * @param message
	 */
	public InfoObject(String mail, String name, String surname,
			String projectName, String password, String login, String message)
	{
		super();
		this.mail = mail;
		this.name = name;
		this.surname = surname;
		this.projectName = projectName;
		this.password = password;
		this.login = login;
		this.message = message;
	}

	public InfoObject(String mail, String name, String surname, String message)
	{
		this.setMail(mail);
		this.setName(name);
		this.setSurname(surname);
		this.setMessage(message);
	}

	public String getMail()
	{
		return mail;
	}

	public void setMail(String mail)
	{
		this.mail = mail;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getLogin()
	{
		return login;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public boolean isEmpty()
	{

		if (this.mail != null && !this.mail.isEmpty())
		{
			return false;
		}

		return true;
	}

	/**
	 * Sets message
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Gets message
	 * 
	 * @return message the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Sets number
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number)
	{
		this.number = number;
	}

	/**
	 * Gets number
	 * 
	 * @return number the number
	 */
	public String getNumber()
	{
		return number;
	}

	/**
	 * Sets durCompilationDate
	 * 
	 * @param durCompilationDate
	 *            the durCompilationDate to set
	 */
	public void setDurCompilationDate(String durCompilationDate)
	{
		this.durCompilationDate = durCompilationDate;
	}

	/**
	 * Gets durCompilationDate
	 * 
	 * @return durCompilationDate the durCompilationDate
	 */
	public String getDurCompilationDate()
	{
		return durCompilationDate;
	}

	/**
	 * Sets totalAmountOfDUR
	 * 
	 * @param totalAmountOfDUR
	 *            the totalAmountOfDUR to set
	 */
	public void setTotalAmountOfDUR(String totalAmountOfDUR)
	{
		this.totalAmountOfDUR = totalAmountOfDUR;
	}

	/**
	 * Gets totalAmountOfDUR
	 * 
	 * @return totalAmountOfDUR the totalAmountOfDUR
	 */
	public String getTotalAmountOfDUR()
	{
		return totalAmountOfDUR;
	}

	/**
	 * Sets certifyDate
	 * 
	 * @param certifyDate
	 *            the certifyDate to set
	 */
	public void setCertifyDate(String certifyDate)
	{
		this.certifyDate = certifyDate;
	}

	/**
	 * Gets certifyDate
	 * 
	 * @return certifyDate the certifyDate
	 */
	public String getCertifyDate()
	{
		return certifyDate;
	}

	/**
	 * Gets denomination
	 * 
	 * @return denomination the denomination
	 */
	public String getDenomination()
	{
		return denomination;
	}

	/**
	 * Sets denomination
	 * 
	 * @param denomination
	 *            the denomination to set
	 */
	public void setDenomination(String denomination)
	{
		this.denomination = denomination;
	}

}
