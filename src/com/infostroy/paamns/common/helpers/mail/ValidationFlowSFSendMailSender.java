package com.infostroy.paamns.common.helpers.mail;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.hibernate.Session;

import com.infostroy.paamns.common.enums.MailStatuses;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.TextCoder;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ValidationFlowSFSendMailSender extends MailSender
{

	/**
	 * @param info
	 */
	public ValidationFlowSFSendMailSender(List<InfoObject> info)
	{
		super(info);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.common.helpers.mail.MailSender#completeMail(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * org.hibernate.Session)
	 */
	@Override
	public Mails completeMail(String mail, String name, String surname,
			String password, String login, String certifyDate,
			String durComplitationDate, String message, String number,
			String projectName, String totalAmountOfDur, String denomination,
			Session session) throws PersistenceBeanException, AddressException,
			MessagingException, PersistenceBeanException
	{
		Mails mailEnt = new Mails();

		mailEnt.setSubject(BeansFactory.MailSettings()
				.getValidationFlowSFSendSubjectSettings(session).getValue());
		mailEnt.setMessage(TextCoder.Encode(MailHelper.getInstance()
				.completeMessageValidationFlow(
						BeansFactory
								.MailSettings()
								.getValidationFlowSFSendTemplateSettings(
										session).getValue(), projectName)));
		mailEnt.setStatus(MailStatuses.Completed.getId());
		mailEnt.setSuccess(false);
		mailEnt.setToEmail(mail);
		mailEnt.setUser(BeansFactory.Users().getByMail(mail, session));

		return mailEnt;
	}
}
