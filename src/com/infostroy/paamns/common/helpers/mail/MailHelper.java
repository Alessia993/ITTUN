/**
 * 
 */
package com.infostroy.paamns.common.helpers.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.MailStatuses;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.TextCoder;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class MailHelper
{
	public static synchronized MailHelper getInstance()
	{
		return new MailHelper();
	}

	public void sendMail(Mails mail, org.hibernate.Session session)
			throws AddressException, HibernateException, MessagingException,
			PersistenceBeanException
	{
		sendMail(mail, BeansFactory.MailSettings().getSmtpSettings(session)
				.getValue(),
				BeansFactory.MailSettings().getUserNameSettings(session)
						.getValue(), BeansFactory.MailSettings()
						.getPasswordSettings(session).getValue(), BeansFactory
						.MailSettings().getFromSettings(session).getValue(),
				session);
	}

	private void save(PersistentEntity entity, org.hibernate.Session session)
	{
		entity.setDeleted(false);
		if (entity.getCreateDate() == null)
		{
			entity.setCreateDate(DateTime.getNow());
		}

		Transaction tr = null;
		try
		{
			tr = session.beginTransaction();
			session.saveOrUpdate(entity);

		}
		catch (Exception e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
			log.error(e);
		}
		finally
		{
			if (tr != null && !tr.wasRolledBack() && tr.isActive())
			{
				tr.commit();
				tr = null;
			}
		}
	}

	public void sendMail(Mails mail, String mailServer, String userName,
			String password, String from, org.hibernate.Session session)
			throws MessagingException, AddressException, HibernateException,
			PersistenceBeanException
	{

		// Mails mail = new Mails();
		// mail.setUser(BeansFactory.Users().getByMail(to, session));
		// mail.setMessage(TextCoder.Encode(messageBody));
		// mail.setSuccess(false);

		// save(mail, session);

		if (mailServer == null || from == null || mail.getToEmail() == null
				|| mail.getSubject() == null || mail.getMessage() == null
				|| userName == null || password == null)
		{
			return;
		}

		boolean needAuth = !BeansFactory.MailSettings()
				.getSmtpAuthSettings(session).getValue().isEmpty() ? Boolean
				.parseBoolean(BeansFactory.MailSettings()
						.getSmtpAuthSettings(session).getValue()) : true;
		String portStr = BeansFactory.MailSettings()
				.getSmtpPortSettings(session).getValue();
		boolean useSSL = !BeansFactory.MailSettings()
				.getSecureProtocolSettings(session).getValue().isEmpty() ? Boolean
				.parseBoolean(BeansFactory.MailSettings()
						.getSecureProtocolSettings(session).getValue()) : true;
		// Setup mail server
		/*
		 * Properties props = System.getProperties();
		 * props.put("mail.smtp.host", mailServer);
		 * 
		 * // Get a mail session Session session =
		 * Session.getDefaultInstance(props, null);
		 * 
		 * // Define a new mail message Message message = new
		 * MimeMessage(session); message.setFrom(new InternetAddress(from));
		 * message.addRecipient(Message.RecipientType.TO, new
		 * InternetAddress(to)); message.setSubject(subject);
		 * 
		 * // Create a message part to represent the body text BodyPart
		 * messageBodyPart = new MimeBodyPart();
		 * messageBodyPart.setText(messageBody);
		 * 
		 * //use a MimeMultipart as we need to handle the file attachments
		 * Multipart multipart = new MimeMultipart();
		 * 
		 * //add the message body to the mime message
		 * multipart.addBodyPart(messageBodyPart);
		 * 
		 * // Put all message parts in the message
		 * message.setContent(multipart);
		 * 
		 * // Send the message Transport.send(message);
		 */
		Properties props = new Properties();
		props.put("mail.smtp.host", mailServer);
		if (needAuth)
		{
			props.put("mail.smtp.auth", "true");
		}
		if (portStr != null && !portStr.isEmpty())
		{
			props.put("mail.smtp.port", portStr);
		}
		// props.put("mail.transport.protocol", "smtp");
		//
		// props.put("mail.smtp.port", "587");
		// props.put("mail.smtp.starttls.enable","true");
		// props.put("mail.debug", "true");

		Session mailSession = null;
		if (needAuth)
		{
			PopupAuthenticator auth = new PopupAuthenticator();
			auth.setPasswordAuthentication(userName, TextCoder.Decode(password));

			mailSession = Session.getDefaultInstance(props, auth);
		}
		else
		{
			mailSession = Session.getDefaultInstance(props);
		}

		Message simpleMessage = new MimeMessage(mailSession);

		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		try
		{
			fromAddress = new InternetAddress(from);

			toAddress = new InternetAddress(mail.getToEmail());

			Transport t = null;
			if (useSSL)
			{
				t = mailSession.getTransport("smtps");
			}
			else
			{
				t = mailSession.getTransport("smtp");
			}

			simpleMessage.setFrom(fromAddress);

			simpleMessage.setRecipient(RecipientType.TO, toAddress);

			simpleMessage.setSubject(mail.getSubject());

			simpleMessage.setText(mail.getMessage());

			t.connect(mailServer, userName, TextCoder.Decode(password));

			t.sendMessage(simpleMessage,
					simpleMessage.getRecipients(Message.RecipientType.TO));

			t.close();
			mail.setSuccess(true);
			mail.setStatus(MailStatuses.Sent.getId());
			save(mail, session);
		}
		catch (MessagingException e)
		{
			mail.setSuccess(false);
			mail.setStatus(MailStatuses.Error.getId());
			save(mail, session);
			log.error(e);
		}

	}

	protected transient final Log	log	= LogFactory.getLog(MailHelper.class);

	static class PopupAuthenticator extends Authenticator
	{
		private String	userName;

		private String	password;

		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(this.getUserName(),
					this.getPassword());
		}

		public void setPasswordAuthentication(String userName, String password)
		{
			this.setPassword(password);
			this.setUserName(userName);
		}

		public void setPassword(String password)
		{
			this.password = password;
		}

		public String getPassword()
		{
			return password;
		}

		public void setUserName(String userName)
		{
			this.userName = userName;
		}

		public String getUserName()
		{
			return userName;
		}
	}

	public String completeMessage(String template, String name, String surname,
			String password, String login)
	{
		try
		{
			if (template == null)
			{
				return null;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" ");
			sb.append(surname);

			template = template.replaceAll("<full name>", sb.toString());
			if (password.isEmpty())
			{
				password = "Not changed";
			}
			template = template.replaceAll("<password>", password);
			template = template.replaceAll("<user name>", login);

		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;

	}

	public String completeMessageForProjectUser(String template, String name,
			String surname, String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" ");
			sb.append(surname);

			template = template.replaceAll("<full name>", sb.toString());
			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageManagmentDur(String template,
			String projectName, String number, String durCompilationDate,
			String totalAmountOfDUR, String certifyDate)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			if (projectName != null)
			{
				template = template.replaceAll("<project name>", projectName);
			}
			else
			{
				template = template.replaceAll("<project name>", "");
			}

			if (number != null)
			{
				template = template.replaceAll("<number>", number);
			}
			else
			{
				template = template.replaceAll("<number>", "");
			}

			if (durCompilationDate != null)
			{
				template = template.replaceAll("<dur compilation date>",
						durCompilationDate);
			}
			else
			{
				template = template.replaceAll("<dur compilation date>", "");
			}

			if (totalAmountOfDUR != null)
			{
				template = template.replaceAll("<Total amount of DUR>",
						totalAmountOfDUR);
			}
			else
			{
				template = template.replaceAll("<Total amount of DUR>", "");
			}

			if (certifyDate != null)
			{
				template = template.replaceAll("<certify date>", certifyDate);
			}
			else
			{
				template = template.replaceAll("<certify date>", "");
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageValidationFlow(String template,
			String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageForProjectInformationCompletation(
			String template, String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageForLocalization(String template, String name,
			String surname, String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" ");
			sb.append(surname);

			template = template.replaceAll("<full name>", sb.toString());
			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageForBudgetChanged(String template,
			String denomination, String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			// StringBuilder sb = new StringBuilder();
			// sb.append(name);
			// sb.append(" ");
			// sb.append(surname);

			template = template.replaceAll("[CFPartnerList].<Denomination>",
					denomination);
			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}

	public String completeMessageForCFandPartner(String template, String name,
			String surname, String projectName)
	{
		try
		{
			if (template == null)
			{
				return null;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" ");
			sb.append(surname);

			template = template.replaceAll("<full name>", sb.toString());
			template = template.replaceAll("<project name>", projectName);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return template;
	}
}