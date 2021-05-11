/**
 * 
 */
package com.infostroy.paamns.web.beans.servicefunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.GiuridicalEngages;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityListBean;

/**
 *
 * @author Sergey Vasnev InfoStroy Co., 2015.
 *
 */
public class GiuridicalEnageDuplicateManagementBean extends
		EntityListBean<Object>
{

	private List<SelectItem>	projects;

	private Long				projectId;

	private String				text;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException
	{
		this.setProjects(new ArrayList<SelectItem>());
		for (Projects project : BeansFactory.Projects().Load())
		{
			this.getProjects().add(
					new SelectItem(project.getId(), project.getTitle()));
		}

	}

	public void manageDuplicates() throws PersistenceBeanException
	{
		try
		{

			if (this.getProjectId() != null)
			{
				log.info("Starting engage management.");
				List<GiuridicalEngages> allEngages = BeansFactory
						.GiuridicalEngages().getByProject(
								String.valueOf(this.getProjectId()));
				List<List<GiuridicalEngages>> duplicates = new ArrayList<List<GiuridicalEngages>>();

				while (!allEngages.isEmpty())
				{

					List<GiuridicalEngages> duplicatesPart = new ArrayList<GiuridicalEngages>();
					duplicatesPart.add(allEngages.get(0));
					allEngages.remove(0);
					Iterator<GiuridicalEngages> iterator = allEngages
							.iterator();
					while (iterator.hasNext())
					{
						GiuridicalEngages eng = iterator.next();
						if (((eng.getCreatePartner() == null && duplicatesPart
								.get(0).getCreatePartner() == null) || (eng
								.getCreatePartner() != null
								&& duplicatesPart.get(0).getCreatePartner() != null && eng
								.getCreatePartner()
								.getId()
								.equals(duplicatesPart.get(0)
										.getCreatePartner().getId())))
								&& ((eng.getDate() == null && duplicatesPart
										.get(0).getDate() == null) || (eng
										.getDate() != null
										&& duplicatesPart.get(0).getDate() != null && eng
										.getDate()
										.equals(duplicatesPart.get(0).getDate())))
								&& ((eng.getNumber() == null && duplicatesPart
										.get(0) == null) || (eng.getNumber() != null
										&& duplicatesPart.get(0).getNumber() != null && eng
										.getNumber().equals(
												duplicatesPart.get(0)
														.getNumber()))))
														
						{

							duplicatesPart.add(eng);
							iterator.remove();
						}

					}
					if (duplicatesPart.size() > 1)
					{
						duplicates.add(duplicatesPart);
					}

				}
				if (!duplicates.isEmpty())
				{
					int counterDuplicates=0;
					int counterCd = 0;
					
					Transaction tr = null;
					try
					{
						tr = PersistenceSessionManager.getBean().getSession()
								.beginTransaction();
						List<GiuridicalEngages> newItems = new ArrayList<GiuridicalEngages>();
						for (List<GiuridicalEngages> part : duplicates)
						{
							GiuridicalEngages newEngage = new GiuridicalEngages();
							newEngage.setActType(part.get(0).getActType());
							newEngage.setCreatePartner(part.get(0)
									.getCreatePartner());
							newEngage.setDate(part.get(0).getDate());
							newEngage.setNumber(part.get(0).getNumber());
							newEngage.setProject(part.get(0).getProject());
							newEngage.setTempDocument(part.get(0)
									.getTempDocument());
							newEngage.setText(part.get(0).getText());
							newEngage.setAmount(0d);
							BeansFactory.GiuridicalEngages().SaveInTransaction(
									newEngage);
							log.info("Engages management - creating new Engage from " + part.size() + "duplicates for items with: "
									+ "Act Type: " + newEngage.getActType() + " Create partner: " + newEngage.getCreatePartner() 
									+ " Date: " + newEngage.getDate() + " Number: " + newEngage.getNumber());
							for (GiuridicalEngages oldEngage : part)
							{
								log.info("Processing duplicate with id: " + oldEngage.getId());
								if (oldEngage.getAmount() != null)
								{
									log.info("Adding amount to engage: " + oldEngage.getAmount());
									
									newEngage.setAmount(newEngage.getAmount()
											+ oldEngage.getAmount());
									log.info("Current amount for new Engage: " + oldEngage.getAmount() );

								}

								List<CostDefinitions> cdList = BeansFactory
										.CostDefinitions()
										.GetByGuiridicalEngage(
												oldEngage.getId());
								log.info("Processing costdefinitions for duplicate, count: " +cdList.size());
								for (CostDefinitions cd : cdList)
								{
									
									cd.setGiuridicalEngages(newEngage);
									BeansFactory.CostDefinitions()
											.SaveInTransaction(cd);
									log.info("Relating CD with id " + cd.getId() + " to new engage");
									counterCd++;
								}
								log.info("Set duplicate with id: " +oldEngage.getId() + " deleted" );
								BeansFactory.GiuridicalEngages()
										.DeleteInTransaction(oldEngage);
								counterDuplicates++;
							}
							log.info("Saving new engage with id: " +newEngage.getId() );
							BeansFactory.GiuridicalEngages().SaveInTransaction(
									newEngage);

						}
					}
					catch (Exception e)
					{
						this.setText("Error during operation");
						log.error("Exception inGiuridicalEngage management");
						if (tr != null)
						{
							tr.rollback();
						}
					}
					finally
					{
						if (tr != null && tr.isActive() && !tr.wasRolledBack())
						{
							tr.commit();
							this.setText("Operation successfull. Found " + duplicates.size() + " duplicate groups. With " + counterDuplicates + " duplicates inside. " 
							+ counterCd + " cost definitions was reassigned.");
						}
					}
				}
				else
				{
					this.setText("No items");
				}

			}
		}
		catch (Exception e)
		{
			this.setText("Error during operation");e.printStackTrace();
			log.error("Exception inGiuridicalEngage management");
		}
		log.info(this.getText());
	}
	
	
	public void fillGiuridicalEngages() throws PersistenceBeanException{
		List<GiuridicalEngages> engages = BeansFactory.GiuridicalEngages().Load();
		
		Transaction tr = null;
		try
		{
			tr = PersistenceSessionManager.getBean().getSession()
					.beginTransaction();

			for (GiuridicalEngages engage : engages)
			{

				List<CostDefinitions> costs = BeansFactory.CostDefinitions()
						.GetByGuiridicalEngage(engage.getId());
				List<Integer> numbers = new ArrayList<Integer>();
				double summ = 0d;
				for (CostDefinitions cost : costs)
				{
					if (cost.getAmountReport() != null)
					{
						summ += cost.getAmountReport().doubleValue();
					}
					if (cost.getReportNumber() != null
							&& !numbers.contains(cost.getReportNumber()))
					{
						numbers.add(cost.getReportNumber());
					}
				}
				Collections.sort(numbers);
				if(engage.getAmount()!=null){
					engage.setResidualAmount(new Double(engage.getAmount().doubleValue()  - summ));
				}

				engage.setReportNumber(Arrays.toString(numbers.toArray())
						.replace("]", "").replace("[", ""));
				BeansFactory.GiuridicalEngages().SaveInTransaction(engage);

			}
		}
		catch (Exception e)
		{
			if (tr != null)
			{
				tr.rollback();
			}
		}
		finally
		{
			if (tr != null && tr.isActive() && !tr.wasRolledBack())
			{
				tr.commit();
			}
		}
		
	}


	@Override
	public void Page_Load_Static() throws HibernateException,
			PersistenceBeanException
	{

	}


	@Override
	public void addEntity()
	{

	}


	@Override
	public void editEntity()
	{

	}


	@Override
	public void deleteEntity()
	{
		
	}

	public List<SelectItem> getProjects()
	{
		return projects;
	}

	public void setProjects(List<SelectItem> projects)
	{
		this.projects = projects;
	}

	public Long getProjectId()
	{
		return projectId;
	}

	public void setProjectId(Long projectId)
	{
		this.projectId = projectId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	
	
}
