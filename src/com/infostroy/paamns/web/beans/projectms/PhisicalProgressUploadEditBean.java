package com.infostroy.paamns.web.beans.projectms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressToDocument;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.web.beans.EntityEditBean;

public class PhisicalProgressUploadEditBean extends EntityEditBean<ProjectIndicators> {

	private Boolean showDoc;

	private UploadedFile newDocumentUpFile;

	private String editDocTitle;

	private String protocolNumber;
	
	private String description;
	
	private Date documentDate;
	
	private List<Documents> docList;
	
	private Integer entityDeleteId;
	
	@SuppressWarnings("unchecked")
	public void deleteDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			docs.remove((int) this.getEntityDeleteId());
			this.getViewState().put("newDocument", docs);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void downloadDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityDownloadId());
			FileHelper.sendFile(new DocumentInfo(doc.getDocumentDate(), doc.getName(), doc.getFileName(), null, doc.getSignflag()),
					doc.getId() == null);
		}
	}

	public void addDoc() {
		setEntityEditId(null);
		setDescription(null);
		setDocumentDate(null);
		setProtocolNumber(null);
		setShowDoc(false);
	}

	@SuppressWarnings({ "unchecked" })
	public void editDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityEditId());
			setDescription(doc.getDescription());
			setDocumentDate(doc.getCreateDate());
			setProtocolNumber(doc.getProtocolNumber());
			setEditDocTitle(doc.getTitle());
		}
		setShowDoc(false);
	}

	@SuppressWarnings({ "unchecked" })
	public void showDocFromList() {
		if (this.getViewState().get("newDocument") != null) {
			List<Documents> docs = (List<Documents>) this.getViewState().get("newDocument");
			Documents doc = docs.get(this.getEntityEditId());
			setDescription(doc.getDescription());
			setDocumentDate(doc.getCreateDate());
			setProtocolNumber(doc.getProtocolNumber());
			setEditDocTitle(doc.getTitle());
		}
		setShowDoc(true);
	}

	@SuppressWarnings("unchecked")
	public void addNewDocument() throws NumberFormatException, PersistenceBeanException {
		try {
			if (getEntityEditId() != null) {
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}
				docs.get(getEntityEditId()).setDescription(getDescription());
				docs.get(getEntityEditId()).setProtocolNumber(this.getProtocolNumber());
				docs.get(getEntityEditId()).setCreateDate(this.getDocumentDate());
				if (getNewDocumentUpFile() != null) {
					String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
					OutputStream os = new FileOutputStream(new File(fileName));
					os.write(getNewDocumentUpFile().getBytes());
					os.close();
					DocumentInfo doc = new DocumentInfo(new Date(), getNewDocumentUpFile().getName(), fileName, null, null, null);
					docs.get(getEntityEditId()).setTitle(FileHelper.getFileNameWOExtension(doc.getName()));
					docs.get(getEntityEditId()).setName(doc.getName());
					docs.get(getEntityEditId()).setDocumentDate(doc.getDate());
					docs.get(getEntityEditId()).setProtocolNumber(doc.getProtNumber());
					docs.get(getEntityEditId()).setFileName(doc.getFileName());
				}
				this.getViewState().put("newDocument", docs);
			} else if (getNewDocumentUpFile() != null) {
				String fileName = FileHelper.newTempFileName(getNewDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(getNewDocumentUpFile().getBytes());
				os.close();
				DocumentInfo doc = new DocumentInfo(new Date(), getNewDocumentUpFile().getName(), fileName, null, null, null);
				List<Documents> docs = new ArrayList<Documents>();
				if (this.getViewState().get("newDocument") != null) {
					docs = (List<Documents>) this.getViewState().get("newDocument");
				}
				Documents newDoc = new Documents(FileHelper.getFileNameWOExtension(doc.getName()), doc.getName(),
						this.getCurrentUser(), doc.getDate(), doc.getProtNumber(), null, this.getProject(),
						doc.getFileName(), doc.getCategory(), doc.getIsPublic(), 0, doc.getSignFlag());
				newDoc.setDescription(getDescription());
				newDoc.setProtocolNumber(this.getProtocolNumber());
				newDoc.setCreateDate(this.getDocumentDate());
				docs.add(newDoc);
				this.getViewState().put("newDocument", docs);
			}
			//this.Page_Load();
		} catch (HibernateException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@Override
	public void Page_Load()
			throws NumberFormatException, HibernateException, PersistenceBeanException, PersistenceBeanException {
		ProjectIndicators entity = null;
		if(this.getSession().get("entityUpload")!=null){
			Long id =Long.valueOf((String) this.getSession().get("entityUpload"));
			entity = BeansFactory.ProjectIndicators().Load(id);
		}else{
			entity = new ProjectIndicators();
		}
		setEntity(entity);
				List<Documents> docs = new ArrayList<Documents>();
		if (this.getViewState().get("newDocument") != null) {
			docs = (List<Documents>) this.getViewState().get("newDocument");
		}
		if (entity != null && this.getViewState().get("newDocument") == null) {
			//this.getViewState().put("newDocument",null);
			for (PhisicalProgressToDocument item : BeansFactory.PhisicalProgressToDocument().getByProjectIndicators(entity.getId())) {
				if (item.getDocument() != null) {
					docs.add(item.getDocument());
				}
			}
		}
		setDocList(docs);
		this.getViewState().put("newDocument", docs);
//		List<Documents> docs = new ArrayList<Documents>();
//		if (this.getViewState().get("newDocument") != null) {
//			docs = (List<Documents>) this.getViewState().get("newDocument");
//		}
//		if (this.getSession().get("potentialProjectEntityView") != null) {
//			setView(true);
//		}
//		setDocList(docs);
	}

//	@Override
//	public void addEntity() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void editEntity() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void deleteEntity() {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		
	}

	public void GoBack() {
		this.getSession().put("entityUpload", null);
		goTo(PagesTypes.PHISICALPROGRESSVIEW);
	}

	public Boolean getShowDoc() {
		return showDoc;
	}

	public void setShowDoc(Boolean showDoc) {
		this.showDoc = showDoc;
	}

	public Integer getEntityEditId() {
		return (Integer) getViewState().get("entityEditId");
	}

	public void setEntityEditId(Integer entityEditId) {
		this.getViewState().put("entityEditId", entityEditId);
	}

	public UploadedFile getNewDocumentUpFile() {
		return newDocumentUpFile;
	}

	public void setNewDocumentUpFile(UploadedFile newDocumentUpFile) {
		this.newDocumentUpFile = newDocumentUpFile;
	}

	public Integer getEntityDownloadId() {
		return (Integer) getViewState().get("entityDownloadId");
	}

	public void setEntityDownloadId(Integer entityDownloadId) {
		this.getViewState().put("entityDownloadId", entityDownloadId);
	}

	public String getEditDocTitle() {
		return editDocTitle;
	}

	public void setEditDocTitle(String editDocTitle) {
		this.editDocTitle = editDocTitle;
	}

	public Boolean getView() {
		return getViewState().get("view") != null ? (Boolean) getViewState().get("view") : false;
	}

	public void setView(Boolean view) {
		getViewState().put("view", view);
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Date getDocumentDate() {
		return documentDate;
	}
	
	public List<Documents> getDocList() {
		return docList;
	}

	public void setDocList(List<Documents> docList) {
		this.docList = docList;
	}
	public Integer getEntityDeleteId() {
		return entityDeleteId;
	}

	public void setEntityDeleteId(Integer entityDeleteId) {
		this.entityDeleteId = entityDeleteId;
	}

	@Override
	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		//ricerca dell'id
		//getEntity().setProjectIndicators(projectIndicators);
		
		//getEntity().setDocument(document);
		saveDocuments();
		
	}
	
	@SuppressWarnings("unchecked")
	private void saveDocuments() throws PersistenceBeanException, IOException {
		List<PhisicalProgressToDocument> phisProgToDoc = BeansFactory.PhisicalProgressToDocument()
				.getByProjectIndicators(getEntity().getId());
		if ((phisProgToDoc != null && !phisProgToDoc.isEmpty()) || this.getViewState().get("newDocument") != null) {
			List<Documents> docs = new ArrayList<Documents>();
			if (this.getViewState().get("newDocument") != null) {
				docs = (List<Documents>) this.getViewState().get("newDocument");
			}
			List<Documents> oldDocs = new ArrayList<Documents>();
			for (PhisicalProgressToDocument doc : phisProgToDoc) {
				oldDocs.add(doc.getDocument());
			}
			for (Documents doc : oldDocs) {
				if (doc != null && !Contains(docs, doc)) {
					PhisicalProgressToDocument phisProgtodef = BeansFactory.PhisicalProgressToDocument()
							.getByDocumentAndPhisProg(doc.getId(), getEntity().getId());
					BeansFactory.PhisicalProgressToDocument().DeleteInTransaction(phisProgtodef);
					if (BeansFactory.PhisicalProgressToDocument().getByDocument(doc.getId()).size() == 0) {
						BeansFactory.Documents().DeleteInTransaction(doc);
					}
				}
			}
			
			Long section = 0l;
			if(this.getSession().get("isCommUpload")!=null && (boolean) this.getSession().get("isCommUpload")){
				section = DocumentSections.CommunicationDeliverables.getValue();
				this.getSession().put("isCommUpload", null);
			}else if(this.getSession().get("isProgUpload")!=null && (boolean) this.getSession().get("isProgUpload")){
				section = DocumentSections.ProjectManagementDeliverables.getValue();
				this.getSession().put("isProgUpload",null);
			}else if(this.getSession().get("isResultUpload")!=null && (boolean) this.getSession().get("isResultUpload")){
				section = DocumentSections.ResultIndicators.getValue();
				this.getSession().put("isResultUpload",null);
			}
			else if(this.getSession().get("isProjUpload")!=null && (boolean) this.getSession().get("isProjUpload")){
				section = DocumentSections.ProjectIndicator.getValue();
				this.getSession().put("isProjUpload",null);
			}
			
			for (Documents doc : docs) {
				if (doc.isNew()) {
					String newFileName = FileHelper.newFileName(doc.getName());
					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setSection(BeansFactory.Sections().Load(section));
						doc.setUser(this.getCurrentUser());
						doc.setProject(this.getProject());
						BeansFactory.Documents().SaveInTransaction(doc);
						ProjectIndicators pi = BeansFactory.ProjectIndicators().Load(Long.valueOf((String)this.getSession().get("entityUpload")));
						PhisicalProgressToDocument phisicalProgressToDocuments = new PhisicalProgressToDocument(doc, getEntity());
						BeansFactory.PhisicalProgressToDocument().SaveInTransaction(phisicalProgressToDocuments);
					}
				} else {
					String newFileName = FileHelper.newFileName(doc.getName());

					if (FileHelper.copyFile(new File(doc.getFileName()), new File(newFileName))) {
						doc.setFileName(newFileName);
						doc.setSection(BeansFactory.Sections().Load(section));
						doc.setUser(this.getCurrentUser());
						doc.setProject(this.getProject());
					}
					BeansFactory.Documents().MergeInTransaction(doc);
				}
			}
		}
	}
	
	private boolean Contains(List<Documents> list, Documents item) {
		for (Documents doc : list) {
			if (doc.getId() != null && doc.getId().equals(item.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void AfterSave() {
		// TODO Auto-generated method stub
		this.GoBack();
	}
	
}
