/**
 * 
 */
package com.infostroy.paamns.common.helpers.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public abstract class MailSender
{
	// ADD THREAD STOP LOGIC
	private List<InfoObject>	infoList	= new ArrayList<InfoObject>();

	private InfoObject			info;

	public MailSender(InfoObject info)
	{
		super();
		this.info = info;
	}

	public MailSender(List<InfoObject> infoList)
	{
		this.infoList = infoList;
	}

	protected transient final Log	log	= LogFactory
												.getLog(AccessGrantHelper.class);

	public List<Mails> completeMails(Session session) throws AddressException,
			PersistenceBeanException, MessagingException
	{
		List<Mails> mails = new ArrayList<Mails>();
		if (infoList != null && infoList.size() > 0)
		{
			for (InfoObject info : infoList)
			{
				mails.add(this.completeMail(info.getMail(), info.getName(),
						info.getSurname(), info.getPassword(), info.getLogin(),
						info.getCertifyDate(), info.getDurCompilationDate(),
						info.getMessage(), info.getNumber(),
						info.getProjectName(), info.getTotalAmountOfDUR(),
						info.getDenomination(), session));

			}

		}
		else if (info != null && !info.isEmpty())
		{
			mails.add(this.completeMail(info.getMail(), info.getName(),
					info.getSurname(), info.getPassword(), info.getLogin(),
					info.getCertifyDate(), info.getDurCompilationDate(),
					info.getMessage(), info.getNumber(), info.getProjectName(),
					info.getTotalAmountOfDUR(), info.getDenomination(), session));
		}
		return mails;
	}

	public abstract Mails completeMail(String mail, String name,
			String surname, String password, String login, String certifyDate,
			String durComplitationDate, String message, String number,
			String projectName, String totalAmountOfDur, String denomination,
			Session session) throws PersistenceBeanException, AddressException,
			MessagingException, PersistenceBeanException;

}
