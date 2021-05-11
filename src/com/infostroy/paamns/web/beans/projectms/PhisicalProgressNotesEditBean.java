package com.infostroy.paamns.web.beans.projectms;

import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalProgressNotes;
import com.infostroy.paamns.persistence.beans.entities.domain.ProjectIndicators;
import com.infostroy.paamns.web.beans.EntityProjectListBean;

public class PhisicalProgressNotesEditBean extends EntityProjectListBean<PhisicalProgressNotes> {

	private Boolean isEdit;

	private Boolean isAdd;

	private String noteAdd;

	private String noteEdit;

	private Long noteEditId;

	private boolean addEnable;

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException,
			PersistenceBeanException, PersistenceBeanException {
		if (this.getSession().get("entityNote") != null) {
			this.setList(BeansFactory.PhisicalProgressNotes()
					.LoadByProjectIndicator(this.getSession().get("entityNote").toString()));
		}

		if (this.getSession().get("isPhisicalProgress") != null
				&& (Boolean) this.getSession().get("isPhisicalProgress")) {
			setAddEnable(true);
		}
	}

	@Override
	public void addEntity() {
		this.setIsAdd(true);
	}

	@Override
	public void deleteEntity() {
		try {
			BeansFactory.PhisicalProgressNotes().Delete(this.getEntityDeleteId());
			this.Page_Load();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void editEntity() {
		try {
			PhisicalProgressNotes pn = BeansFactory.PhisicalProgressNotes().Load(this.getNoteEditId());
			this.setNoteEdit(pn.getNote());
			this.setIsEdit(true);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenceBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
		this.getViewState().put("noteEdit", isEdit);

		if (isEdit == true) {
			this.setIsAdd(false);
		}
	}

	public Boolean getIsEdit() {
		if (this.getViewState().get("noteEdit") != null) {
			this.isEdit = (Boolean) this.getViewState().get("noteEdit");
		}

		return isEdit;
	}

	public void setNoteAdd(String note) {
		this.noteAdd = note;
	}

	public String getNoteAdd() {
		return this.noteAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
		this.getViewState().put("noteAdd", isAdd);

		if (isAdd == true) {
			this.setIsEdit(false);
		}
	}

	public Boolean getIsAdd() {
		if (this.getViewState().get("noteAdd") != null) {
			this.isAdd = (Boolean) this.getViewState().get("noteAdd");
		}

		return this.isAdd;
	}

	public void setNoteEdit(String noteEdit) {
		this.noteEdit = noteEdit;
		this.getViewState().put("noteEditpp", noteEdit);
	}

	public String getNoteEdit() {
//		return noteEdit;
		
		noteEdit = (String) this.getViewState().get("noteEditpp");
		return noteEdit;
	}

	public void addNote() throws NumberFormatException, HibernateException, PersistenceBeanException {
		PhisicalProgressNotes pn = new PhisicalProgressNotes();

		ProjectIndicators projectIndicators = BeansFactory.ProjectIndicators().Load(this.getSession().get("entityNote").toString());

		pn.setProjectIndicators(projectIndicators);
		pn.setNote(this.getNoteAdd());

		BeansFactory.PhisicalProgressNotes().Save(pn);

		this.setIsAdd(false);

		this.setNoteAdd(null);
		this.Page_Load();
	}

	public void updateNote() throws HibernateException, PersistenceBeanException {
		PhisicalProgressNotes pn = BeansFactory.PhisicalProgressNotes().Load(this.getNoteEditId());
		pn.setNote(this.getNoteEdit());

		BeansFactory.PhisicalProgressNotes().Save(pn);

		this.setIsEdit(false);

		this.Page_Load();
	}

	public void updateNoteCancel() {
		this.setIsEdit(false);
	}

	public void addNoteCancel() {
		this.setIsAdd(false);
	}

	public void goBack() {
//		if (this.getSession().get("isPhisicalProgress") != null) {
//			boolean isProcedureProgress = (Boolean) this.getSession().get("isPhisicalProgress");
//			if (isProcedureProgress)
//			//	this.goTo(PagesTypes.PROCEDUREPROGRESSLIST);
//			this.goTo(PagesTypes.PHISICALPROGRESSNOTESBEAN);
//			else
//				//this.goTo(PagesTypes.PROCEDUREINSERTLIST);
//				this.goTo(PagesTypes.PHISICALPROGRESSVIEW);
//		} else {
//			//this.goTo(PagesTypes.PROCEDUREINSERTLIST);
//			this.goTo(PagesTypes.PHISICALPROGRESSVIEW);
//		}
		this.getSession().put("entityNote", null);
		this.goTo(PagesTypes.PHISICALPROGRESSVIEW);
	}

	public void setNoteEditId(Long noteEditId) {
		this.noteEditId = noteEditId;

		this.getViewState().put("noteEditId", noteEditId);
	}

	public Long getNoteEditId() {
		if (this.getViewState().get("noteEditId") != null) {
			return (Long) this.getViewState().get("noteEditId");
		}

		return noteEditId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infostroy.paamns.web.beans.EntityListBean#Page_Load_Static()
	 */
	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		// TODO Auto-generated method stub
	}

	public boolean getAddEnable() {
		return addEnable;
	}

	public void setAddEnable(boolean value) {
		addEnable = value;
	}
}
