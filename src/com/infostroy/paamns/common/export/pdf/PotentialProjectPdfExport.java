package com.infostroy.paamns.common.export.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjects;
import com.infostroy.paamns.persistence.beans.entities.domain.PotentialProjectsToDocuments;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;


public class PotentialProjectPdfExport 
{
	
	private static final Log log = LogFactory.getLog(PotentialProjectPdfExport.class);
	private String projectAcronym;
	private String leadNameOrganization;
	private String saveHour;
	private String saveDate;
	private List<String> descriptions;
	
	public byte[] createDocument(Long potentialProjectId) 
	{
		try 
		{
			fillData(potentialProjectId);
			return createPdf();
		} 
		catch (Exception e) 
		{
			log.error(e);
		}
		return null;
	}
	
	private byte[] createPdf() throws IOException{
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(byteOutStream);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf, PageSize.A4);
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		document.add(new Paragraph(Utils.getString("potentialProjectPdfHeader")).setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
		document.add(new Paragraph("PC INTERREG V/A Italia-Tunisia").setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
		document.add(new Paragraph("").setHeight(20f));
		
		StringBuilder str = new StringBuilder();
		str.append("Attestazione del caricamento della proposta progettuale: ");
		str.append(getProjectAcronym());
		str.append(" presentata da ");
		str.append(leadNameOrganization);
		str.append(" effettuato alle ore ");
		str.append(getSaveHour());
		str.append(" del giorno ");
		str.append(getSaveDate()).append(".");
		document.add(new Paragraph(str.toString()).setFont(font).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED));
	
		if(getDescriptions() != null && getDescriptions().size() > 0){
			com.itextpdf.layout.element.List list1 = new com.itextpdf.layout.element.List();
			list1.setFont(font).setTextAlignment(TextAlignment.LEFT);
			for(String description : getDescriptions()){
				list1.add(description);
			}
			document.add(list1);
		}
		document.close();
		return byteOutStream.toByteArray();
	}


	private void fillData(Long potentialProjectId) 
	{
		try 
		{
			PotentialProjects project = BeansFactory.PotentialProjects().Load(potentialProjectId);
			setProjectAcronym(project.getProjectAcronym());
			setLeadNameOrganization(project.getLeadNameOrganization());
			Calendar c = Calendar.getInstance();
			c.setTime(project.getUpdateDate());
			setSaveHour(String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(c.get(Calendar.MINUTE)));
			setSaveDate(DateTime.ToString(project.getUpdateDate()));
			List<PotentialProjectsToDocuments> potentialProjectsToDocuments = BeansFactory.PotentialProjectsToDocuments().getByPotentialProject(potentialProjectId);
			if(potentialProjectsToDocuments.size() > 0)
			{
				setDescriptions(new ArrayList<String>());
				for(PotentialProjectsToDocuments document : potentialProjectsToDocuments)
				{
					getDescriptions().add(document.getDocument().getDescription());
				}
			}
		} 
		catch (HibernateException e) 
		{
			log.error(e);
		} 
		catch (PersistenceBeanException e) 
		{
			log.error(e);
		}
	}
	
	public String getProjectAcronym() 
	{
		return projectAcronym;
	}

	public void setProjectAcronym(String projectAcronym) 
	{
		this.projectAcronym = projectAcronym;
	}

	public String getLeadNameOrganization() 
	{
		return leadNameOrganization;
	}

	public void setLeadNameOrganization(String leadNameOrganization) 
	{
		this.leadNameOrganization = leadNameOrganization;
	}

	public String getSaveHour() 
	{
		return saveHour;
	}

	public void setSaveHour(String saveHour) 
	{
		this.saveHour = saveHour;
	}

	public String getSaveDate() 
	{
		return saveDate;
	}

	public void setSaveDate(String saveDate) 
	{
		this.saveDate = saveDate;
	}

	public List<String> getDescriptions() 
	{
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) 
	{
		this.descriptions = descriptions;
	}
	
}
