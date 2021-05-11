package com.infostroy.paamns.web.beans;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ActivityLog;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.Entity;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public abstract class EntityEditBean<T extends Entity> extends PageBean {
	public T entity;

	/**
	 * Stores tr value of entity.
	 */
	private Transaction tr;

	/**
     * 
     */
	public EntityEditBean() {
		try {
			PersistenceSessionManager.getBean().getSession().clear();
		} catch (PersistenceBeanException e1) {
			log.error("EntityEditBean exception:", e1);
		}
		if (!this.isPostback()) {
			try {
				this.checkPasswordExpiration();
				ActivityLog.getInstance().addActivity();
				this.PreLoad();
				this.getViewState().clear();
				this.Page_Load_Static();
			} catch (PersistenceBeanException e) {
				log.error(e);
			}
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error("EntityEditBean exception:",e);
		} catch (HibernateException e) {
			log.error("EntityEditBean exception:",e);
		} catch (PersistenceBeanException e) {
			log.error("EntityEditBean exception:",e);
		} catch (IOException e) {
			log.error("EntityEditBean exception:",e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("EntityEditBean exception:",e);
		}
	}

	/**
	 * @throws PersistenceBeanException
	 * 
	 */
	protected void PreLoad() throws PersistenceBeanException {

	}

	/**
	 * Gets projectId
	 * 
	 * @return projectId the projectId
	 */
	public String getProjectId() {
		if (this.getSession().get("project") != null) {
			return String.valueOf(this.getSession().get("project"));
		} else {
			return null;
		}
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 */
	public Projects getProject() throws NumberFormatException,
			HibernateException, PersistenceBeanException,
			PersistenceBeanException {
		if (this.getSession().get("PAAMSN_Session_project_storage") == null
				|| !this.getSession().get("stored_project_id").toString()
						.equals(this.getProjectId())) {
			if (this.getProjectId() == null) {
				return null;
			}

			this.getSession().put("PAAMSN_Session_project_storage",
					BeansFactory.Projects().Load(this.getProjectId()));
			this.getSession().put("stored_project_id", this.getProjectId());
		}

		return (Projects) this.getSession().get(
				"PAAMSN_Session_project_storage");
	}

	public void setProject() throws NumberFormatException, HibernateException,
			PersistenceBeanException {
		this.getSession().put("PAAMSN_Session_project_storage",
				BeansFactory.Projects().Load(this.getProjectId()));
		this.getSession().put("stored_project_id", this.getProjectId());
	}

	/**
	 * Use this methos to fill fields.
	 * 
	 * @throws PersistenceBeanException
	 * @throws HibernateException
	 * @throws NumberFormatException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 * @throws IOException
	 * @throws PersistentException
	 */
	public abstract void Page_Load() throws NumberFormatException,
			HibernateException, PersistenceBeanException, IOException,
			NullPointerException;

	/**
	 * Use this methos to fill static lists. It will calls only once on page
	 * load event.
	 * 
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 */
	public abstract void Page_Load_Static() throws PersistenceBeanException;

	/**
	 * Call this method from the page on "Save" button.
	 */
	public void Page_Save() {
		if (getSaveFlag() == 0) {
			try {
				if (!this.BeforeSave()) {
					return;
				}
			} catch (PersistenceBeanException e2) {
				log.error("EntityEditBean exception:",e2);
				return;
			} catch (Exception e2) {
				log.error("EntityEditBean exception:",e2);
				return;
			}

			try {
				tr = PersistenceSessionManager.getBean().getSession()
						.beginTransaction();
			} catch (HibernateException e1) {
				log.error("EntityEditBean exception:",e1);
			} catch (PersistenceBeanException e1) {
				log.error("EntityEditBean exception:",e1);
			}
			setSaveFlag(1);
			try {
				this.SaveEntity();
			} catch (HibernateException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:",e);
			} catch (PersistenceBeanException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:",e);
			} catch (NumberFormatException e) {
				if (tr != null) {
					tr.rollback();
				}
				e.printStackTrace();
				log.error("EntityEditBean exception:",e);
			} catch (IOException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:",e);
			} catch (Exception e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:",e);
			} finally {
				if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
					tr.commit();
				}
			}

			this.AfterSave();
			setSaveFlag(0);
		}
	}

	/**
	 * @throws PersistenceBeanException
	 * 
	 */
	public Boolean BeforeSave() throws PersistenceBeanException {
		return true;
	}

	/**
	 * Use this method for saving process.
	 * 
	 * @throws HibernateException
	 * @throws PersistenceBeanException
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws PersistenceBeanException
	 * @throws IOException
	 * @throws PersistentException
	 */
	public abstract void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException;

	/**
     * 
     */
	public abstract void GoBack();

	/**
     * 
     */
	public abstract void AfterSave();

	/**
	 * Sets entity
	 * 
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(T entity) {
		this.entity = entity;
	}

	/**
	 * Gets entity
	 * 
	 * @return entity the entity
	 */
	public T getEntity() {
		return entity;
	}

	public Long getEntityId() {
		if (getEntity() != null) {
			return getEntity().getId();
		} else {
			return null;
		}
	}

	public Long getEntityId2() {
		if (getEntity() != null) {
			return getEntity().getId();
		} else {
			return null;
		}
	}
	
	/**
	 * Sets saveFlag
	 * 
	 * @param saveFlag
	 *            the saveFlag to set
	 */
	public void setSaveFlag(int saveFlag) {
		this.getViewState().put("saveFlag", saveFlag);
	}

	/**
	 * Gets saveFlag
	 * 
	 * @return saveFlag the saveFlag
	 */
	public int getSaveFlag() {
		return this.getViewState().get("saveFlag") == null ? 0 : (Integer) this
				.getViewState().get("saveFlag");
	}


}
