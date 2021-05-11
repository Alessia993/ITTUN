package com.infostroy.paamns.web.beans.projectms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.CFAnagraphTypes;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.CFAnagraphWrapper;

public class CFPartnerAnagraphCompletationsListBean extends EntityListBean<CFPartnerAnagraphs> {
	private List<CFAnagraphWrapper> listCFAnagraph;

	private List<CFAnagraphWrapper> listCFPartnerAnagraphs;

	private Long entityPartnerEditId;

	private boolean currentUserIsPb;

	private int asse;

	public void Page_Load_Static() throws PersistenceBeanException {
	}

	public void ReRenderScroll() {
		if (getListCFPartnerAnagraphs() != null
				&& getListCFPartnerAnagraphs().size() > Integer.parseInt(this.getItemsPerPage())) {
			this.setShowScroll(true);
		} else {
			this.setShowScroll(false);
		}
	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException,
			PersistenceBeanException, PersistenceBeanException {
		this.CheckCurrentUserIsPB();

		Projects currentProject = BeansFactory.Projects().Load(String.valueOf(this.getSession().get("project")));
		if (currentProject != null) {
			this.setAsse(currentProject.getAsse());
		}
		if (this.getSession().get("project") != null) {
			List<CFPartnerAnagraphProject> listCFs = null;
			List<CFPartnerAnagraphProject> listPartners = null;

			if (!this.currentUserIsPb || this.getSessionBean().isAAU()) {
				listCFs = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProject(
						String.valueOf(this.getSession().get("project")), CFAnagraphTypes.CFAnagraph);

				listPartners = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProject(
						String.valueOf(this.getSession().get("project")), CFAnagraphTypes.PartnerAnagraph);
			} else {
				listPartners = BeansFactory.CFPartnerAnagraphProject().GetCFAnagraphForProjectAndUser(
						String.valueOf(this.getSession().get("project")),
						this.getSessionBean().getCurrentUser().getId());
			}

			this.listCFAnagraph = new ArrayList<CFAnagraphWrapper>();
			this.listCFPartnerAnagraphs = new ArrayList<CFAnagraphWrapper>();

			if (listCFs != null) {
				for (CFPartnerAnagraphProject cfpa : listCFs) {
					CFAnagraphWrapper newEntity = new CFAnagraphWrapper(cfpa);
					this.listCFAnagraph.add(newEntity);
				}
			}

			if (listPartners != null) {
				for (CFPartnerAnagraphProject cfpa : listPartners) {
					CFAnagraphWrapper newEntity = new CFAnagraphWrapper(cfpa);
					this.listCFPartnerAnagraphs.add(newEntity);
				}
			}
		}
	}

	private void CheckCurrentUserIsPB() {
		if (this.getSessionBean().isPartner()) {
			if (!(this.getSessionBean().isSTC() || this.getSessionBean().isAGU() || this.getSessionBean().isCF())) {
				this.setCurrentUserIsPb(true);
			} else {
				this.setCurrentUserIsPb(false);
			}
		} else {
			this.setCurrentUserIsPb(false);
		}
	}

	/**
	 * Gets listCFPartnerAnagraphs
	 * 
	 * @return listCFPartnerAnagraphs the listCFPartnerAnagraphs
	 */
	public List<CFAnagraphWrapper> getListCFPartnerAnagraphs() {
		return listCFPartnerAnagraphs;
	}

	/**
	 * Sets listCFPartnerAnagraphs
	 * 
	 * @param listCFPartnerAnagraphs
	 *            the listCFPartnerAnagraphs to set
	 */
	public void setListCFPartnerAnagraphs(List<CFAnagraphWrapper> listCFPartnerAnagraphs) {
		this.listCFPartnerAnagraphs = listCFPartnerAnagraphs;
	}

	/**
	 * Gets listCFAnagraph
	 * 
	 * @return listCFAnagraph the listCFAnagraph
	 */
	public List<CFAnagraphWrapper> getListCFAnagraph() {
		return listCFAnagraph;
	}

	/**
	 * Sets listCFAnagraph
	 * 
	 * @param listCFAnagraph
	 *            the listCFAnagraph to set
	 */
	public void setListCFAnagraph(List<CFAnagraphWrapper> listCFAnagraph) {
		this.listCFAnagraph = listCFAnagraph;
	}

	/**
	 * Sets entityPartnerEditId
	 * 
	 * @param entityPartnerEditId
	 *            the entityPartnerEditId to set
	 */
	public void setEntityPartnerEditId(Long entityPartnerEditId) {
		this.entityPartnerEditId = entityPartnerEditId;
	}

	/**
	 * Gets entityPartnerEditId
	 * 
	 * @return entityPartnerEditId the entityPartnerEditId
	 */
	public Long getEntityPartnerEditId() {
		return entityPartnerEditId;
	}

	/**
	 * Sets currentUserIsPb
	 * 
	 * @param currentUserIsPb
	 *            the currentUserIsPb to set
	 */
	public void setCurrentUserIsPb(boolean currentUserIsPb) {
		this.currentUserIsPb = currentUserIsPb;
	}

	/**
	 * Gets currentUserIsPb
	 * 
	 * @return currentUserIsPb the currentUserIsPb
	 */
	public boolean isCurrentUserIsPb() {
		return currentUserIsPb;
	}

	public void editCFAnagraphItem() {
		this.getSession().put("from_listcfpartner", true);
		this.getSession().put("cfpartner", this.getEntityEditId());
		this.getSession().put("isCFAnagraph", true);
		this.getSession().put("isCFPartnerAnagraph", null);
		this.getSession().put("cfPartneranagraphcompletationView", null);
		this.goTo(PagesTypes.CFPARTNERANAGRAPHEDIT);
	}

	public void editCFPartnerAnagraphItem() {
		this.getSession().put("from_listcfpartner", true);
		this.getSession().put("cfpartner", this.getEntityPartnerEditId());
		this.getSession().put("isCFAnagraph", null);
		this.getSession().put("isCFPartnerAnagraph", true);
		this.getSession().put("cfPartneranagraphcompletationView", null);
		this.goTo(PagesTypes.CFPARTNERANAGRAPHEDIT);
	}

	public void viewCFAnagraphItem() {
		this.getSession().put("from_listcfpartner", true);
		this.getSession().put("cfpartner", this.getEntityEditId());
		this.getSession().put("isCFAnagraph", true);
		this.getSession().put("isCFPartnerAnagraph", null);
		this.getSession().put("cfPartneranagraphcompletationView", true);
		this.goTo(PagesTypes.CFPARTNERANAGRAPHEDIT);
	}

	public void viewCFPartnerAnagraphItem() {
		this.getSession().put("from_listcfpartner", true);
		this.getSession().put("cfpartner", this.getEntityPartnerEditId());
		this.getSession().put("isCFAnagraph", null);
		this.getSession().put("isCFPartnerAnagraph", true);
		this.getSession().put("cfPartneranagraphcompletationView", true);
		this.goTo(PagesTypes.CFPARTNERANAGRAPHEDIT);
	}

	/**
	 * Deletes specified item
	 */
	public void deleteItem() {
		try {
			BeansFactory.CFPartnerAnagraphCompletations().Delete(String.valueOf(getEntityDeleteId()));
			this.Page_Load();
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		} catch (NumberFormatException e) {
			log.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#addEntity()
	 */
	@Override
	public void addEntity() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#deleteEntity()
	 */
	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#editEntity()
	 */
	@Override
	public void editEntity() {
		// TODO Auto-generated method stub

	}

	public int getAsse() {
		return asse;
	}

	public void setAsse(int asse) {
		this.asse = asse;
	}

}
