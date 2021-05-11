/**
 * 
 */
package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphProject;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.ControllerUserAnagraph;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalCheckToDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.LocalChecks;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class LocalCheckEditBean extends EntityProjectEditBean<LocalChecks> {
	private List<Documents> docList;

	private int selectTab;

	private UploadedFile newDocumentUpFile;

	private Documents newDocument;

	private boolean isView;

	private List<SelectItem> listPartners;

	private List<SelectItem> categories;

	private static List<SelectItem> listSelectRoles;

	private String selectedCategory;

	private String userName;

	private String userSurname;

	private String userRoleName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#AfterSave()
	 */
	@Override
	public void AfterSave() {
		this.GoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#GoBack()
	 */
	@Override
	public void GoBack() {
		this.goTo(PagesTypes.LOCALCHECKLIST);
	}

	@SuppressWarnings("unchecked")
	public void deleteDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			docs.remove((int) this.getEntityDeleteId());
			this.getViewState().put("newDocument", docs);
		}

		try {
			this.Page_Load();
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (HibernateException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}

		this.setSelectTab(1);
	}

	@SuppressWarnings("unchecked")
	public void downloadDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityDownloadId());

			FileHelper.sendFile(
					new DocumentInfo(doc.getDocumentDate(), doc.getName(), doc.getFileName(), null, doc.getSignflag()),
					false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getSession().get("localCheckView") != null) {
			setIsView(true);
		}

		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}

		setDocList(docs);
		setNewDocument(new Documents());
		getNewDocument().setDocumentDate(new Date());
		getNewDocument().setEditableDate(new Date());

		this.fillPartners();
		this.fillCategories();
		this.fillRoles();
		this.fillUserInfo();
	}

	private void fillUserInfo() {
		if (this.getEntity().isNew()) {
			this.setUserName(getCurrentUser().getName());
			this.setUserSurname(getCurrentUser().getSurname());
			this.setUserRoleName(getCurrentUser().getRoles().get(0).getRole().getCode());
		} else {
			this.setUserName(this.getEntity().getInsertedUserName());
			this.setUserSurname(this.getEntity().getInsertedUserSurname());
			this.setUserRoleName(this.getEntity().getInsertedUserRole());
		}
	}

	private void fillRoles() throws HibernateException, PersistenceBeanException {
		listSelectRoles = new ArrayList<SelectItem>();
		listSelectRoles.add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();
		for (UserRoles userRole : UserRolesBean.GetByUser(String.valueOf(this.getCurrentUser().getId()))) {
			if (userRole.getRole().getCode().equals("DAEC")) {
				continue;
			}
			if (temp.add(userRole.getRole().getCode())) {
				UploadDocumentRoleType roleType = UploadDocumentRoleType.getByName(userRole.getRole().getCode());
				if (roleType != null) {
					this.getListSelectRoles()
							.add(new SelectItem(String.valueOf(roleType.getId()), roleType.getDisplayName()));
				}
			}
		}
	}

	private void fillPartners() throws HibernateException, PersistenceBeanException {
		listPartners = new ArrayList<SelectItem>();

		List<CFPartnerAnagraphs> partners = new ArrayList<CFPartnerAnagraphs>();

		if (this.getSessionBean().isCIL()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByCIL(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					for (CFPartnerAnagraphs item : partnersLoc) {
						CFPartnerAnagraphProject cfpap = BeansFactory.CFPartnerAnagraphProject()
								.LoadByPartner(Long.valueOf(this.getProjectId()), item.getId());
						if (cfpap != null && cfpap.getRemovedFromProject()) {
							continue;
						}

						partners.add(item);
					}

				}
			}
		}
		if (this.getSessionBean().isDAEC()) {
			ControllerUserAnagraph cua = BeansFactory.ControllerUserAnagraph().GetByUser(this.getCurrentUser().getId());
			if (cua != null) {
				List<CFPartnerAnagraphs> partnersLoc = BeansFactory.CFPartnerAnagraphs().GetByDAEC(this.getProjectId(),
						cua.getId());
				if (partnersLoc != null) {
					partners.addAll(partnersLoc);
				}
			}
		}
		if (this.getSessionBean().isAAU()) {
			partners = BeansFactory.CFPartnerAnagraphs().LoadByProject(this.getProjectId());
		}

		listPartners.add(SelectItemHelper.getFirst());

		for (CFPartnerAnagraphs item : partners) {
			StringBuilder sb = new StringBuilder();
			SelectItem selectItem = new SelectItem();

			if (item.getUser() == null) {
				sb.append(item.getName());
			} else {
				sb.append(item.getUser().getName());
				sb.append(item.getUser().getSurname());
			}

			selectItem.setValue(String.valueOf(item.getId()));
			selectItem.setLabel(sb.toString());

			listPartners.add(selectItem);
		}

		if (listPartners.size() == 1) {
			listPartners.clear();
		}
	}

	private void fillCategories() throws PersistenceBeanException {
		categories = new ArrayList<SelectItem>();

		for (Category item : BeansFactory.Category().Load()) {
			categories.add(new SelectItem(String.valueOf(item.getId()), item.getName()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infostroy.paamns.web.beans.EntityProjectEditBean#Page_Load_Static()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void Page_Load_Static() throws PersistenceBeanException {
		if (!this.getSessionBean().getIsActualSateAndAguRead() || this.getSessionBean().getIsProjectClosed()) {
			this.goTo(PagesTypes.PROJECTINDEX);
		}

		if (this.getSession().get("localCheck") != null
				&& !String.valueOf(this.getSession().get("localCheck")).isEmpty()) {
			setEntity(BeansFactory.LocalChecks().Load(String.valueOf(this.getSession().get("localCheck"))));
			if (!(this.isView || this.getEntity() == null || this.getEntity().getPartner() == null)) {
				this.setSelectedPartnerEntity(String.valueOf(this.getEntity().getPartner().getId()));
			}
		} else {
			setEntity(new LocalChecks());
		}

		List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}

		if (this.getEntity() != null && !this.getEntity().isNew()) {
			for (LocalCheckToDocuments item : BeansFactory.LocalCheckToDocuments()
					.getByLocalCheck(this.getEntity().getId())) {
				docs.add(item.getDocument());
			}
		}

		setDocList(docs);
		this.getViewState().put("newDocument", docs);
	}

	@SuppressWarnings("unchecked")
	public void addNewDocument() {
		try {
			if (getNewDocumentUpFile() != null && getNewDocument() != null) {
				String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();

				Category cat = null;
				if (this.getSelectedCategory() != null && !this.getSelectedCategory().isEmpty()) {
					cat = BeansFactory.Category().Load(this.getSelectedCategory());
				}
				DocumentInfo doc = new DocumentInfo(getNewDocument().getDocumentDate(),
						getNewDocumentUpFile().getName(), fileName, getNewDocument().getProtocolNumber(), cat,
						getNewDocument().getIsPublic(), getNewDocument().getSignflag());
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}

				docs.add(new Documents(FileHelper.getFileNameWOExtension(doc.getName()), doc.getName(),
						this.getCurrentUser(), doc.getDate(), doc.getProtNumber(), null, this.getProject(),
						doc.getFileName(), doc.getCategory(), doc.getIsPublic(),
						this.getNewDocRole().isEmpty() ? 0 : Integer.parseInt(this.getNewDocRole()),
						getNewDocument().getDocumentNumber(), getNewDocument().getNote(),
						getNewDocument().getEditableDate(), doc.getSignFlag()));

				this.getViewState().put("newDocument", docs);
			}
			this.setSelectedCategory(null);
			this.setNewDocRole(null);
			this.setSelectedPartner(null);

			this.Page_Load();
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (NumberFormatException e) {
			log.error(e);
		} catch (PersistenceBeanException e) {
			log.error(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityProjectEditBean#SaveEntity()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		LocalChecks entityToSave = new LocalChecks();
		if (this.getSession().get("localCheck") != null
				&& !String.valueOf(this.getSession().get("localCheck")).isEmpty()) {
			entityToSave = BeansFactory.LocalChecks().Load(String.valueOf(this.getSession().get("localCheck")));
		}

		entityToSave.setCheckClosureDate(this.getEntity().getCheckClosureDate());
		entityToSave.setDateOfIssueprovisionalControl(this.getEntity().getDateOfIssueprovisionalControl());
		entityToSave.setCheckStartDate(this.getEntity().getCheckStartDate());
		entityToSave.setClosedControl(this.getEntity().getClosedControl());
		entityToSave.setEvaluation(this.getEntity().getEvaluation());
		entityToSave.setControlexpense(this.getEntity().getControlexpense());
		entityToSave.setIneligibleexpense(this.getEntity().getIneligibleexpense());

		entityToSave.setProject(this.getProject());
		entityToSave.setResults(this.getEntity().getResults());
		entityToSave.setResume(this.getEntity().getResume());
		entityToSave.setProtocolNumber(this.getEntity().getProtocolNumber());

		entityToSave.setFinalAmountIneligibleExpenditure(this.getEntity().getFinalAmountIneligibleExpenditure());
		if (this.getEntity().isNew()) {
			entityToSave.setUser(this.getCurrentUser());
			entityToSave.setInsertedUserName(getUserName());
			entityToSave.setInsertedUserSurname(getUserSurname());
			entityToSave.setInsertedUserRole(getUserRoleName());
		}

		if (this.getSelectedPartnerEntity() != null && !this.getSelectedPartnerEntity().isEmpty()) {
			entityToSave.setPartner(BeansFactory.CFPartnerAnagraphs().Load(this.getSelectedPartnerEntity()));
		}

		BeansFactory.LocalChecks().SaveInTransaction(entityToSave);

		List<LocalCheckToDocuments> locToDoc = BeansFactory.LocalCheckToDocuments()
				.getByLocalCheck(entityToSave.getId());

		if ((locToDoc != null && !locToDoc.isEmpty()) || this.getViewState().get("newDocument") != null) {
			List<Documents> docs = new ArrayList<Documents>();
			if (this.getViewState().get("newDocument") != null) {
				docs = (List<Documents>) this.getViewState().get("newDocument");
			}
			List<Documents> oldDocs = new ArrayList<Documents>();

			for (LocalCheckToDocuments doc : locToDoc) {
				oldDocs.add(doc.getDocument());
			}

			for (Documents doc : oldDocs) {
				boolean docsContains = false;
				for (Documents itemDoc : docs) {
					if (itemDoc.getId().equals(doc.getId())) {
						docsContains = true;
						break;
					}
				}
				if (!docsContains) {
					LocalCheckToDocuments costtodef = BeansFactory.LocalCheckToDocuments()
							.getByDocumentAndLocalCheck(doc.getId(), entityToSave.getId());
					BeansFactory.LocalCheckToDocuments().DeleteInTransaction(costtodef);
					if (BeansFactory.LocalCheckToDocuments().getByDocument(doc.getId()).size() == 0) {
						BeansFactory.Documents().DeleteInTransaction(doc);
					}
				}
			}

			for (Documents doc : docs) {
				if (doc.isNew()) {
					String newFileName = FileHelper.newFileName(doc.getName());
					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setProject(this.getProject());
						doc.setSection(BeansFactory.Sections().Load(DocumentSections.LocalCheck.getValue()));
						doc.setUser(this.getCurrentUser());
						BeansFactory.Documents().SaveInTransaction(doc);
						LocalCheckToDocuments newItem = new LocalCheckToDocuments(doc, entityToSave);
						BeansFactory.LocalCheckToDocuments().SaveInTransaction(newItem);
					}
				}
			}
		}
	}

	public void closedChanged(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.setIsClosed(true);
		} else {
			this.setIsClosed(false);
		}
	}

	public LocalChecks getEntity() {
		this.entity = (LocalChecks) this.getViewState().get("LocalChecks");
		return this.entity;
	}

	public void setEntity(LocalChecks entity) {
		this.entity = entity;
		this.getViewState().put("LocalChecks", entity);
	}

	/**
	 * Sets docList
	 * 
	 * @param docList
	 *            the docList to set
	 */
	public void setDocList(List<Documents> docList) {
		this.docList = docList;
	}

	/**
	 * Gets docList
	 * 
	 * @return docList the docList
	 */
	public List<Documents> getDocList() {
		return docList;
	}

	/**
	 * Sets entityDownloadId
	 * 
	 * @param entityDownloadId
	 *            the entityDownloadId to set
	 */
	public void setEntityDownloadId(Integer entityDownloadId) {
		this.getViewState().put("entityDownloadId", entityDownloadId);
	}

	/**
	 * Gets entityDownloadId
	 * 
	 * @return entityDownloadId the entityDownloadId
	 */
	public Integer getEntityDownloadId() {
		return (Integer) this.getViewState().get("entityDownloadId");
	}

	/**
	 * Sets entityDeleteId
	 * 
	 * @param entityDeleteId
	 *            the entityDeleteId to set
	 */
	public void setEntityDeleteId(Integer entityDeleteId) {
		this.getViewState().put("entityDeleteId", entityDeleteId);
	}

	/**
	 * Gets entityDeleteId
	 * 
	 * @return entityDeleteId the entityDeleteId
	 */
	public Integer getEntityDeleteId() {
		return (Integer) this.getViewState().get("entityDeleteId");
	}

	/**
	 * Sets selectTab
	 * 
	 * @param selectTab
	 *            the selectTab to set
	 */
	public void setSelectTab(int selectTab) {
		this.selectTab = selectTab;
	}

	/**
	 * Gets selectTab
	 * 
	 * @return selectTab the selectTab
	 */
	public int getSelectTab() {
		return selectTab;
	}

	/**
	 * Sets newDocumentUpFile
	 * 
	 * @param newDocumentUpFile
	 *            the newDocumentUpFile to set
	 */
	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile) {
		this.newDocumentUpFile = newDocumentUpFile;
	}

	/**
	 * Gets newDocumentUpFile
	 * 
	 * @return newDocumentUpFile the newDocumentUpFile
	 */
	public UploadedFile getNewDocumentUpFile() {
		return newDocumentUpFile;
	}

	/**
	 * Sets newDocument
	 * 
	 * @param newDocument
	 *            the newDocument to set
	 */
	public void setNewDocument(Documents newDocument) {
		this.newDocument = newDocument;
	}

	/**
	 * Gets newDocument
	 * 
	 * @return newDocument the newDocument
	 */
	public Documents getNewDocument() {
		return newDocument;
	}

	/**
	 * Sets isClosed
	 * 
	 * @param isClosed
	 *            the isClosed to set
	 */
	public void setIsClosed(boolean isClosed) {
		this.getViewState().put("isClosed", isClosed);
	}

	/**
	 * Gets isClosed
	 * 
	 * @return isClosed the isClosed
	 */
	public boolean getIsClosed() {
		if (this.getViewState().get("isClosed") == null) {
			return false;
		}
		return (Boolean) this.getViewState().get("isClosed");
	}

	/**
	 * Sets isView
	 * 
	 * @param isView
	 *            the isView to set
	 */
	public void setIsView(boolean isView) {
		this.isView = isView;
	}

	/**
	 * Gets isView
	 * 
	 * @return isView the isView
	 */
	public boolean getIsView() {
		return isView;
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner) {
		this.getViewState().put("selectedPartner", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner() {
		return (String) this.getViewState().get("selectedPartner");
	}

	/**
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartnerEntity(String selectedPartner) {
		this.getViewState().put("selectedPartnerEntity", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartnerEntity() {
		return (String) this.getViewState().get("selectedPartnerEntity");
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners) {
		this.listPartners = listPartners;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners() {
		return listPartners;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories() {
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SelectItem> categories) {
		this.categories = categories;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	/**
	 * Gets listSelectRoles
	 * 
	 * @return listSelectRoles the listSelectRoles
	 */
	public List<SelectItem> getListSelectRoles() {
		return listSelectRoles;
	}

	/**
	 * Sets listSelectRoles
	 * 
	 * @param listSelectRoles
	 *            the listSelectRoles to set
	 */
	public void setListSelectRoles(List<SelectItem> listSelectRoles) {
		LocalCheckEditBean.listSelectRoles = listSelectRoles;
	}

	public String getNewDocRole() {
		return (String) this.getViewState().get("selectedNewRole");
	}

	public void setNewDocRole(String entity) {
		this.getViewState().put("selectedNewRole", entity);
	}

	public Boolean getCanEditCategory() {
		if (getSessionBean().isSTC() || getSessionBean().isAGU() || getSessionBean().isACU()
				|| getSessionBean().isAGUW() || getSessionBean().isSTCW() || getSessionBean().isACUW()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets userName
	 * 
	 * @return userName the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets userName
	 * 
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets userSurname
	 * 
	 * @return userSurname the userSurname
	 */
	public String getUserSurname() {
		return userSurname;
	}

	/**
	 * Sets userSurname
	 * 
	 * @param userSurname
	 *            the userSurname to set
	 */
	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	/**
	 * Gets userRoleName
	 * 
	 * @return userRoleName the userRoleName
	 */
	public String getUserRoleName() {
		return userRoleName;
	}

	/**
	 * Sets userRoleName
	 * 
	 * @param userRoleName
	 *            the userRoleName to set
	 */
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

}
