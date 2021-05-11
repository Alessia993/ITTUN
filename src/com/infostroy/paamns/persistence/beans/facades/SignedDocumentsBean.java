package com.infostroy.paamns.persistence.beans.facades;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.PersistenceEntityBean;
//import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.SignedDocuments;

public class SignedDocumentsBean extends PersistenceEntityBean<SignedDocuments> {

	public SignedDocuments LoadByDocumentId(Long documentId) throws PersistenceBeanException {
		Criteria criteria = PersistenceSessionManager.getBean().getSession().createCriteria(SignedDocuments.class);
		criteria.add(Restrictions.eq("forDocument.id", documentId));
		criteria.add(Restrictions.eq("deleted", false));
		return (SignedDocuments) criteria.uniqueResult();
	}
}
