/**
 * 
 */
package com.infostroy.paamns.common.helpers;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.enums.ApplicationSettingTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class ActivityLog
{
	private static final String	folder							= "Activity Log";

	public static final String	file							= "ITTUN.activity.log";

	private static final String	oldFileTemp						= "ITTUN.activity.log.%d";

	private static final String	logFormat						= "%s: %s - %s - %s";

	private static final String	extendedLogFormatWithValue		= "%s: %s - %s - %s - %s - %s - %d";

	private static final String	extendedLogFormatWithoutValue	= "%s: %s - %s - %s - %s - %d";

	private static ActivityLog	instance;

	/**
	 * 
	 */
	private ActivityLog()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public static ActivityLog getInstance()
	{
		if (instance == null)
		{
			synchronized (ActivityLog.class)
			{
				if (instance == null)
				{
					instance = new ActivityLog();
				}
			}
		}

		return instance;
	}

	private void addEntity(String username, String page, Date date, String ip)
			throws IOException, NumberFormatException,
			PersistenceBeanException, NullPointerException
	{
		synchronized (ActivityLog.class)
		{
			File dir = new File(FileHelper.getFileDirectory(), folder);
			dir.mkdir();
			renameFiles();

			File log = new File(dir.getAbsolutePath(), file);
			FileOutputStream outStream = new FileOutputStream(log, true);
			DataOutputStream out = new DataOutputStream(outStream);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			if (log.length() != 0)
			{
				bw.newLine();
			}
			bw.append(String.format(logFormat,
					DateTime.ToStringWithMinutes(date), page, username, ip));
			bw.flush();
			bw.close();

			out.close();
		}
	}

	private void addExtendedEntity(String username, String page, Date date,
			String ip, String actionPeformed, String value, Long id)
			throws IOException, NumberFormatException,
			PersistenceBeanException, NullPointerException
	{
		synchronized (ActivityLog.class)
		{
			File dir = new File(FileHelper.getFileDirectory(), folder);
			dir.mkdir();
			renameFiles();

			File log = new File(dir.getAbsolutePath(), file);
			FileOutputStream outStream = new FileOutputStream(log, true);
			DataOutputStream out = new DataOutputStream(outStream);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			if (log.length() != 0)
			{
				bw.newLine();
			}
			if (value == null || value.isEmpty())
			{
				bw.append(String.format(extendedLogFormatWithoutValue,
						DateTime.ToStringWithMinutes(date), page, username, ip,
						actionPeformed, id));
			}
			else
			{
				bw.append(String.format(extendedLogFormatWithValue,
						DateTime.ToStringWithMinutes(date), page, username, ip,
						actionPeformed, value, id));
			}
			bw.flush();
			bw.close();

			out.close();
		}
	}

	private void renameFiles() throws IOException, NumberFormatException,
			PersistenceBeanException, NullPointerException
	{
		File dir = new File(FileHelper.getFileDirectory(), folder);
		boolean oversized = false;
		for (File file : dir.listFiles())
		{
			if (file.exists() && file.isFile()
					&& file.getName().equalsIgnoreCase(ActivityLog.file))
			{
				if (file.length() > Integer.parseInt(BeansFactory
						.ApplicationSettings().getValue(
								ApplicationSettingTypes.ACTIVITY_LOG_SIZE)) * 1048576)
				{
					oversized = true;
				}
			}
		}

		List<File> files = new ArrayList<File>();
		for (File file : dir.listFiles())
		{
			files.add(file);
		}

		Collections.sort(files, new FileComparer());

		if (oversized)
		{
			for (int i = 0; i < dir.listFiles().length; i++)
			{
				File file = files.get(i);
				if (file.exists() && file.isFile()
						&& checkFileName(file.getName()))
				{
					rename(file, getNewFileName(file.getName()));
					file.delete();
				}
			}
		}
	}

	private String getNewFileName(String oldName)
	{
		if (oldName.equalsIgnoreCase(file))
		{
			return String.format(oldFileTemp, 1);
		}

		try
		{
			return String.format(
					oldFileTemp,
					1 + Integer.parseInt(oldName.substring(
							oldName.lastIndexOf('.') + 1, oldName.length())));
		}
		catch (Exception e)
		{
			return oldName;
		}

	}

	private boolean checkFileName(String fileName)
	{
		if (fileName.indexOf(file.substring(0, file.length() - 4)) != -1)
		{
			return true;
		}
		return false;
	}

	public void addActivity()
	{
		try
		{
			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) ctx
					.getExternalContext().getRequest();

			String path = request.getServletPath();

			addEntity(SessionManager.getInstance().getSessionBean()
					.getCurrentUser().getLogin(),
					PagesTypes.getPageByPath(path), new Date(),
					request.getRemoteAddr());
		}
		catch (Exception e)
		{
			log.error("Activity exception:",e);
		}
	}

	public void addExtendedActivity(String actionPeformed, String value, Long id)
	{
		try
		{
			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) ctx
					.getExternalContext().getRequest();

			String path = request.getServletPath();

			addExtendedEntity(SessionManager.getInstance().getSessionBean()
					.getCurrentUser().getLogin(),
					PagesTypes.getPageByPath(path), new Date(),
					request.getRemoteAddr(), actionPeformed, value, id);
		}
		catch (Exception e)
		{
			log.error("Activity exception:",e);
		}
	}

	protected transient final Log	log	= LogFactory.getLog(getClass());

	private File rename(File f, String name) throws IOException,
			PersistenceBeanException
	{
		File fn = new File(FileHelper.getFileDirectory(), File.separator
				+ folder + File.separator + name);
		FileOutputStream outStream = new FileOutputStream(fn);
		DataOutputStream out = new DataOutputStream(outStream);

		FileInputStream fstream = new FileInputStream(f);

		DataInputStream in = new DataInputStream(fstream);
		byte[] b = new byte[(int) f.length()];
		in.read(b);
		out.write(b);

		in.close();
		out.close();

		return fn;
	}

	public class FileComparer implements Comparator<File>
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(File o1, File o2)
		{
			if (o1.getName().equalsIgnoreCase(ActivityLog.file))
			{
				return 1;
			}
			if (o2.getName().equalsIgnoreCase(ActivityLog.file))
			{
				return -1;
			}
			try
			{
				return ((Integer) Integer.parseInt(o2.getName().substring(
						o2.getName().lastIndexOf('.'),
						o2.getName().length() - 1)))
						.compareTo((Integer) Integer.parseInt(o1.getName()
								.substring(o1.getName().lastIndexOf('.'),
										o1.getName().length() - 1)));
			}
			catch (Exception e)
			{
				return 0;
			}
		}

	}
}
