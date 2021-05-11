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
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ProgressValidationStateTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.AccessGrantHelper;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AnnualProfiles;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToCoreIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToEmploymentIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToProgramIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.PhisicalInitializationToQsnIndicators;
import com.infostroy.paamns.persistence.beans.entities.domain.Procedures;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjectDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.ProgressValidationObjects;
import com.infostroy.paamns.web.beans.EntityProjectEditBean;
import com.infostroy.paamns.web.beans.wrappers.ProgressValidationObjectWrapper;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class ProgressValidationFlowEditBean extends
        EntityProjectEditBean<ProgressValidationObjectWrapper>
{
    private boolean                                            editMode;
    
    private boolean                                            CFMode;
    
    private boolean                                            STCMode;
    
    private boolean                                            AGUMode;
    
    private String                                             engageStyle;
    
    private String                                             gotStyle;
    
    private String                                             programmedUpdateStyle;
    
    private boolean                                            canEditPhysical;
    
    private boolean                                            canFillMonitoringDate;
    
    private String                                             monitoringDateRowVisibility;
    
    private ProgressValidationObjects                          progressValidationObject;
    
    @SuppressWarnings("unused")
    private List<ProgressValidationObjectDocuments>            listDocuments;
    
    private static List<SelectItem>                            itemsPerPageList;
    
    // Financial
    private List<AnnualProfiles>                               listFinancial;
    
    private static Boolean                                     showScrollFin   = false;
    
    private static String                                      itemsPerPageFin;
    
    private int                                                selectTab;
    
    // Physical
    private List<PhisicalInitializationToCoreIndicators>       listCoreIndicators;
    
    private List<PhisicalInitializationToEmploymentIndicators> listEmploymentIndicators;
    
    private List<PhisicalInitializationToProgramIndicators>    listRealization;
    
    private List<PhisicalInitializationToQsnIndicators>        listQsn;
    
    private List<PhisicalInitializationToProgramIndicators>    listResults;
    
    // Procedure
    private List<Procedures>                                   listProcedures;
    
    private static Boolean                                     showScrollProcs = false;
    
    private static String                                      itemsPerPageProcs;
    
    // New document variables
    private ProgressValidationObjectDocuments                  newDocument;
    
    private UploadedFile                                       newDocumentUpFile;
    
    @SuppressWarnings("unused")
    private String                                             entityCoreEditId;
    
    @SuppressWarnings("unused")
    private String                                             entityEmpEditId;
    
    @SuppressWarnings("unused")
    private String                                             entityRealEditId;
    
    private String                                             errorMessage;
    
    private int                                                selectedIndex;
    
    private String                                             editFinancial;
    
    private String                                             editProcedure;
    
    private List<SelectItem>                                   listSelectRoles;
    
    private List<SelectItem>                                   categories;
    
    private String                                             selectedCategory;
    
    @Override
    public void AfterSave()
    {
        this.GoBack();
    }
    
    @Override
    public void GoBack()
    {
        this.goTo(PagesTypes.PROGRESSVALIDATIONFLOWVIEW);
    }
    
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException
    {
        if (this.getSession().get("progressValidationShow") != null)
        {
            this.setEditMode(!Boolean.valueOf(String.valueOf(this.getSession()
                    .get("progressValidationShow"))));
        }
        
        if (this.getSession().get("progressValidationEntityId") != null)
        {
            this.setProgressValidationObject(BeansFactory
                    .ProgressValidationObjects().Load(
                            String.valueOf(this.getSession().get(
                                    "progressValidationEntityId"))));
            
            this.setNewEntity(false);
        }
        else
        {
            this.setProgressValidationObject(new ProgressValidationObjects());
            this.setNewEntity(true);
        }
        
        this.CheckRoles();
        this.LoadEntities();
        
        this.ReRenderScrollFinancial();
        this.ReRenderScrollProcedure();
        
        this.CreateNewDocument();
        
        if (this.getSession().get("getBackToProcedureProgressTab") != null)
        {
            this.setSelectTab(2);
            this.getSession().put("getBackToProcedureProgressTab", null);
        }
        
        fillRoles();
        fillCategories();
    }
    
    private void fillCategories() throws PersistenceBeanException
    {
        categories = new ArrayList<SelectItem>();
        
        for (Category item : BeansFactory.Category().Load())
        {
            categories.add(new SelectItem(String.valueOf(item.getId()), item
                    .getName()));
        }
    }
    
    private void fillRoles()
    {
        this.setListSelectRoles(new ArrayList<SelectItem>());
        
        this.getListSelectRoles().add(SelectItemHelper.getFirst());
        for (UploadDocumentRoleType item : UploadDocumentRoleType.values())
        {
            this.getListSelectRoles().add(
                    new SelectItem(String.valueOf(item.getId()), item
                            .getDisplayName()));
        }
    }
    
    private void CreateNewDocument()
    {
        ProgressValidationObjectDocuments newEntity = new ProgressValidationObjectDocuments();
        newEntity.setDocument(new Documents());
        newEntity.getDocument().setDocumentDate(DateTime.getNow());
        
        this.setNewDocument(newEntity);
    }
    
    @Override
    public void Page_Load_Static() throws PersistenceBeanException
    {
        if (!(AccessGrantHelper.IsProgressValidationAvailable()
                && AccessGrantHelper.IsActual() && !AccessGrantHelper
                    .IsProjectClosed()))
        {
            this.goTo(PagesTypes.PROJECTINDEX);
        }
        
        this.InitPaging();
        
        if (this.getSession().get("progressValidationEntityId") != null)
        {
            this.setProgressValidationObject(BeansFactory
                    .ProgressValidationObjects().Load(
                            String.valueOf(this.getSession().get(
                                    "progressValidationEntityId"))));
            
            this.setListDocuments(BeansFactory
                    .ProgressValidationObjectDocuments()
                    .LoadByProgressValidationFlow(
                            this.getProgressValidationObject().getId()));
            
            this.setNewEntity(false);
        }
        else
        {
            this.setProgressValidationObject(new ProgressValidationObjects());
            this.setListDocuments(new ArrayList<ProgressValidationObjectDocuments>());
            this.setNewEntity(true);
        }
        
        fillRoles();
    }
    
    @Override
    public void SaveEntity() throws HibernateException,
            PersistenceBeanException, NumberFormatException, IOException
    {
        if (this.isNewEntity())
        {
            this.getProgressValidationObject().setCreateDate(DateTime.getNow());
            this.getProgressValidationObject().setDeleted(false);
            this.getProgressValidationObject().setProject(this.getProject());
            this.getProgressValidationObject().setState(
                    BeansFactory.ProgressValidationStates().Load(
                            ProgressValidationStateTypes.Create.getValue()));
            
            BeansFactory.ProgressValidationObjects().SaveInTransaction(
                    this.getProgressValidationObject());
            
            if (this.getListFinancial().size() > 1)
            {
                for (AnnualProfiles ap : this.getListFinancial().subList(0,
                        this.getListFinancial().size() - 1))
                {
                    ap.setProgressValidationObject(this
                            .getProgressValidationObject());
                    BeansFactory.AnnualProfiles().SaveInTransaction(ap);
                }
            }
            else
            {
                for (AnnualProfiles ap : this.getListFinancial())
                {
                    ap.setProgressValidationObject(this
                            .getProgressValidationObject());
                    BeansFactory.AnnualProfiles().SaveInTransaction(ap);
                }
            }
            
            for (Procedures p : this.getListProcedures())
            {
                p.setProgressValidationObject(this
                        .getProgressValidationObject());
                BeansFactory.Procedures().SaveInTransaction(p);
            }
            
            for (PhisicalInitializationToCoreIndicators item : this
                    .getListCoreIndicators())
            {
                item.setProgressValidationObject(this
                        .getProgressValidationObject());
                BeansFactory.PhisicalInizializationToCoreIndicators()
                        .SaveInTransaction(item);
            }
            
            for (PhisicalInitializationToEmploymentIndicators item : this
                    .getListEmploymentIndicators())
            {
                item.setProgressValidationObject(this
                        .getProgressValidationObject());
                BeansFactory.PhisicalInizializationToEmploymentIndicators()
                        .SaveInTransaction(item);
            }
            
            for (PhisicalInitializationToProgramIndicators item : this
                    .getListRealization())
            {
                item.setProgressValidationObject(this
                        .getProgressValidationObject());
                BeansFactory
                        .PhisicalInizializationToProgramRealizationIndicators()
                        .SaveInTransaction(item);
            }
        }
        else
        {
            BeansFactory.ProgressValidationObjects().SaveInTransaction(
                    this.getProgressValidationObject());
        }
        
        this.SaveDocuments();
    }
    
    private void SaveDocuments() throws HibernateException,
            PersistenceBeanException, IOException
    {
        List<ProgressValidationObjectDocuments> listDocs = BeansFactory
                .ProgressValidationObjectDocuments()
                .LoadByProgressValidationFlow(
                        this.getProgressValidationObject().getId());
        
        if ((listDocs != null && !listDocs.isEmpty())
                || this.getListDocuments() != null)
        {
            List<Documents> docs = new ArrayList<Documents>();
            if (this.getListDocuments() != null)
            {
                for (ProgressValidationObjectDocuments item : this
                        .getListDocuments())
                {
                    docs.add(item.getDocument());
                }
            }
            
            List<Documents> oldDocs = new ArrayList<Documents>();
            
            for (ProgressValidationObjectDocuments doc : listDocs)
            {
                oldDocs.add(doc.getDocument());
            }
            
            for (Documents doc : oldDocs)
            {
                if (!docs.contains(doc))
                {
                    ProgressValidationObjectDocuments costtodef = BeansFactory
                            .ProgressValidationObjectDocuments()
                            .LoadByProgressValidationFlowAndDocument(
                                    this.getProgressValidationObject().getId(),
                                    doc.getId());
                    BeansFactory.ProgressValidationObjectDocuments()
                            .DeleteInTransaction(costtodef);
                    /*
                     * if (BeansFactory.ProgressValidationObjectDocuments()
                     * .LoadByDocument(doc.getId()).size() == 0) {
                     * BeansFactory.Documents().DeleteInTransaction(doc); }
                     */
                }
            }
            
            for (Documents doc : docs)
            {
                if (doc.isNew())
                {
                    String newFileName = FileHelper.newFileName(doc.getName());
                    if (FileHelper.copyFile(new File(doc.getFileName()),
                            new File(newFileName)))
                    {
                        doc.setFileName(newFileName);
                        doc.setProject(this.getProject());
                        doc.setSection(BeansFactory.Sections().Load(
                                DocumentSections.LocalCheck.getValue()));
                        doc.setUser(this.getCurrentUser());
                        BeansFactory.Documents().SaveInTransaction(doc);
                        ProgressValidationObjectDocuments newItem = new ProgressValidationObjectDocuments(
                                this.getProgressValidationObject(), doc);
                        BeansFactory.ProgressValidationObjectDocuments()
                                .SaveInTransaction(newItem);
                    }
                }
            }
        }
        
        /*
         * List<ProgressValidationObjectDocuments> listDocs = BeansFactory
         * .ProgressValidationObjectDocuments() .LoadByProgressValidationFlow(
         * this.getProgressValidationObject().getId());
         * 
         * for (ProgressValidationObjectDocuments doc : listDocs) {
         * BeansFactory.ProgressValidationObjectDocuments().Delete(doc);
         * BeansFactory.Documents().Delete(doc.getDocument()); }
         * 
         * for (ProgressValidationObjectDocuments doc : this.getListDocuments())
         * { doc.getDocument().setDeleted(false);
         * doc.getDocument().setCreateDate(DateTime.getNow());
         * doc.getDocument().setUser(this.getCurrentUser());
         * doc.getDocument().setProject(this.getProject()); doc.getDocument()
         * .setSection( BeansFactory.Sections().Load(
         * DocumentSections.ProgressValidationFlow .getValue()));
         * 
         * BeansFactory.Documents().Save(doc.getDocument());
         * 
         * doc.setCreateDate(DateTime.getNow()); doc.setDeleted(false);
         * doc.setProgressValidationObject(this.getProgressValidationObject());
         * 
         * BeansFactory.ProgressValidationObjectDocuments().Save(doc); }
         */
    }
    
    private void LoadEntities() throws HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        this.LoadFinancialData();
        this.LoadPhysicalData();
        this.LoadProcedureData();
    }
    
    private void LoadFinancialData() throws HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        List<AnnualProfiles> lst = new ArrayList<AnnualProfiles>();
        if (this.isNewEntity())
        {
            lst = BeansFactory.AnnualProfiles().LoadLastByProject(
                    this.getProjectId());
            
        }
        else
        {
            lst = BeansFactory.AnnualProfiles().LoadByProgressValidationObject(
                    this.getProgressValidationObject().getId());
        }
        
        if (this.listFinancial == null)
        {
            this.listFinancial = new ArrayList<AnnualProfiles>();
        }
        else
        {
            this.listFinancial.clear();
        }
        
        for (AnnualProfiles item : lst)
        {
            if (item.getYear() != null)
            {
                this.listFinancial.add(item);
            }
        }
        
        this.AddTotalRow(this.listFinancial);
    }
    
    private void AddTotalRow(List<AnnualProfiles> list)
    {
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        for (AnnualProfiles item : list)
        {
            sum1 += item.getAmountExpected();
            sum2 += item.getAmountRealized();
            sum3 += item.getAmountToAchieve();
        }
        
        AnnualProfiles total = new AnnualProfiles();
        total.setAmountExpected(sum1);
        total.setAmountRealized(sum2);
        total.setAmountToAchieve(sum3);
        total.setString(Utils.getString("total"));
        list.add(total);
    }
    
    private void LoadPhysicalData() throws PersistenceBeanException
    {
        this.listQsn = BeansFactory.PhisicalInizializationToQsnIndicators()
                .getByProject(this.getProjectId());
        this.listResults = BeansFactory
                .PhisicalInizializationToProgramResultIndicators()
                .getByProject(this.getProjectId());
        
        if (this.isNewEntity())
        {
            this.listRealization = BeansFactory
                    .PhisicalInizializationToProgramRealizationIndicators()
                    .LoadLastByProject(this.getProjectId());
            this.listEmploymentIndicators = BeansFactory
                    .PhisicalInizializationToEmploymentIndicators()
                    .LoadLastByProject(this.getProjectId());
            this.listCoreIndicators = BeansFactory
                    .PhisicalInizializationToCoreIndicators()
                    .LoadLastByProject(this.getProjectId());
        }
        else
        {
            this.listRealization = BeansFactory
                    .PhisicalInizializationToProgramRealizationIndicators()
                    .LoadByProgressValidationObject(
                            this.getProgressValidationObject().getId());
            
            this.listEmploymentIndicators = BeansFactory
                    .PhisicalInizializationToEmploymentIndicators()
                    .LoadByProgressValidationObject(
                            this.getProgressValidationObject().getId());
            
            this.listCoreIndicators = BeansFactory
                    .PhisicalInizializationToCoreIndicators()
                    .LoadByProgressValidationObject(
                            this.getProgressValidationObject().getId());
        }
    }
    
    private void LoadProcedureData() throws HibernateException,
            NumberFormatException, PersistenceBeanException
    {
        if (this.isNewEntity())
        {
            this.listProcedures = BeansFactory.Procedures().LoadLastByProject(
                    this.getProjectId());
        }
        else
        {
            this.listProcedures = BeansFactory.Procedures()
                    .LoadByProgressValidationObject(
                            this.getProgressValidationObject().getId());
        }
    }
    
    private void CheckRoles()
    {
        if (this.getSessionBean().isCF()
                || (this.getSessionBean().isAGU() && this.getProjectAsse() == 5))
        {
            this.setCFMode(true);
        }
        else if (this.getSessionBean().isSTC())
        {
            this.setSTCMode(true);
        }
        else if (this.getSessionBean().isAGU())
        {
            this.setAGUMode(true);
        }
        
        if (this.getSessionBean().isAGUW()
                && this.getProgressValidationObject() != null
                && this.getProgressValidationObject().getState() != null
                && this.getProgressValidationObject()
                        .getState()
                        .getId()
                        .equals(ProgressValidationStateTypes.AGUEvaluation
                                .getValue()))
        {
            this.setCanFillMonitoringDate(true);
            this.setMonitoringDateRowVisibility("");
        }
        else
        {
            this.setCanFillMonitoringDate(false);
            this.setMonitoringDateRowVisibility("none");
        }
        
        if (this.isCFMode()
                && !this.isNewEntity()
                && this.getProgressValidationObject().getState().getId()
                        .equals(ProgressValidationStateTypes.Create.getValue()))
        {
            this.setCanEditPhysical(true);
        }
        else
        {
            this.setCanEditPhysical(false);
        }
    }
    
    private void InitPaging()
    {
        itemsPerPageFin = "10";
        itemsPerPageProcs = "10";
        
        setItemsPerPageList(new ArrayList<SelectItem>());
        
        for (int i = 1; i < 6; i++)
        {
            getItemsPerPageList().add(
                    new SelectItem(String.valueOf(10 * i), String
                            .valueOf(10 * i)));
        }
    }
    
    public void ReRenderScrollFinancial()
    {
        if (this.listFinancial != null
                && this.listFinancial.size() > Integer.parseInt(this
                        .getItemsPerPageFin()))
        {
            this.setShowScrollFin(true);
        }
        else
        {
            this.setShowScrollFin(false);
        }
    }
    
    public void ReRenderScrollProcedure()
    {
        if (this.listProcedures != null
                && this.listProcedures.size() > Integer.parseInt(this
                        .getItemsPerPageProcs()))
        {
            this.setShowScrollProcs(true);
        }
        else
        {
            this.setShowScrollProcs(false);
        }
    }
    
    /**
     * Adding new document
     * 
     * @throws PersistenceBeanException
     * @throws NumberFormatException
     */
    @SuppressWarnings("unchecked")
    public void addNewDocument() throws NumberFormatException,
            PersistenceBeanException
    {
        try
        {
            if (getNewDocumentUpFile() != null && newDocument != null)
            {
                String fileName = FileHelper
                        .newTempFileName(getNewDocumentUpFile().getName());
                OutputStream os = new FileOutputStream(new File(fileName));
                os.write(getNewDocumentUpFile().getBytes());
                os.close();
                
                Category cat = null;
                if (this.getSelectedCategory() != null
                        && !this.getSelectedCategory().isEmpty())
                {
                    cat = BeansFactory.Category().Load(
                            this.getSelectedCategory());
                }
                DocumentInfo doc = new DocumentInfo(this.getNewDocument()
                        .getDocument().getDocumentDate(),
                        getNewDocumentUpFile().getName(), fileName, this
                                .getNewDocument().getDocument()
                                .getProtocolNumber(), cat, getNewDocument()
                                .getDocument().getIsPublic(), this.getNewDocument()
                                .getDocument().getSignflag());
                
                List<ProgressValidationObjectDocuments> docs = new ArrayList<ProgressValidationObjectDocuments>();
                
                if (this.getViewState().get("newDocument") != null)
                {
                    docs = (List<ProgressValidationObjectDocuments>) this
                            .getViewState().get("newDocument");
                }
                
                ProgressValidationObjectDocuments newDoc = new ProgressValidationObjectDocuments();
                newDoc.setProgressValidationObject(this
                        .getProgressValidationObject());
                newDoc.setCreateDate(DateTime.getNow());
                newDoc.setDeleted(false);
                newDoc.setDocument(new Documents(FileHelper
                        .getFileNameWOExtension(doc.getName()), doc.getName(),
                        this.getCurrentUser(), doc.getDate(), doc
                                .getProtNumber(), null, this.getProject(), doc
                                .getFileName(), doc.getCategory(), doc
                                .getIsPublic(), this.getRole().isEmpty() ? 0
                                : Integer.parseInt(this.getRole()), doc.getSignFlag()));
                
                docs.add(newDoc);
                
                this.getViewState().put("newDocument", docs);
            }
            this.Page_Load();
        }
        catch(HibernateException e)
        {
            log.error(e);
        }
        catch(FileNotFoundException e)
        {
            log.error(e);
        }
        catch(IOException e)
        {
            log.error(e);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public void downloadDocFromList()
    {
        if (this.getViewState().get("newDocument") != null)
        {
            List<ProgressValidationObjectDocuments> docs = (List<ProgressValidationObjectDocuments>) this
                    .getViewState().get("newDocument");
            Documents doc = docs.get(this.getEntityDownloadId()).getDocument();
            
            FileHelper.sendFile(
                    new DocumentInfo(doc.getDocumentDate(), doc.getName(), doc
                            .getFileName(), null, doc.getSignflag()), doc.getId() == null);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void deleteDocFromList()
    {
        if (this.getViewState().get("newDocument") != null)
        {
            List<ProgressValidationObjectDocuments> docs = (List<ProgressValidationObjectDocuments>) this
                    .getViewState().get("newDocument");
            docs.remove((int) this.getEntityDeleteId());
            this.getViewState().put("newDocument", docs);
        }
        
        try
        {
            this.Page_Load();
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
        }
        catch(PersistenceBeanException e)
        {
            e.printStackTrace();
        }
        
        this.setSelectTab(3);
    }
    
    public void editCore()
    {
        setEntityEmpEditId(null);
        setEntityRealEditId(null);
        this.setSelectTab(1);
        setSelectedIndex(1);
        this.RefreshErrors();
    }
    
    public void editReal()
    {
        setEntityEmpEditId(null);
        setEntityCoreEditId(null);
        this.setSelectTab(1);
        setSelectedIndex(4);
        this.RefreshErrors();
    }
    
    public void editEmp()
    {
        setEntityCoreEditId(null);
        setEntityRealEditId(null);
        this.setSelectTab(1);
        setSelectedIndex(2);
        this.RefreshErrors();
    }
    
    private void RefreshErrors()
    {
        this.setEngageStyle("");
        this.setGotStyle("");
        this.setProgrammedUpdateStyle("");
    }
    
    public void saveCore() throws HibernateException, PersistenceBeanException
    {
        this.setSelectTab(1);
        setSelectedIndex(1);
        for (PhisicalInitializationToCoreIndicators item : this
                .getListCoreIndicators())
        {
            if (item.getId().equals(Long.parseLong(this.getEntityCoreEditId())))
            {
                boolean isProgrammedValueUpdate = item
                        .getProgrammedValueUpdate() == null
                        || item.getProgrammedValueUpdate().isEmpty();
                boolean isGotValue = item.getGotValue() == null
                        || item.getGotValue().isEmpty();
                boolean isEngageValue = item.getEngageValue() == null
                        || item.getEngageValue().isEmpty();
                
                if (isEngageValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.engageValueIsMandatory"));
                    setEngageStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isGotValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.gotValueIsMandatory"));
                    setGotStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isProgrammedValueUpdate)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.programmedValueUpdateIsMandatory"));
                    setProgrammedUpdateStyle("background-color: #FFE5E5;");
                    return;
                }
            }
            
            if (item.getId().equals(Long.parseLong(this.getEntityCoreEditId())))
            {
                BeansFactory.PhisicalInizializationToCoreIndicators()
                        .Save(item);
                break;
            }
        }
        
        setEntityEmpEditId(null);
        setEntityCoreEditId(null);
        setEntityRealEditId(null);
        
    }
    
    public void editEntityFinancialProgress()
    {
        this.getSession().put("financialProgress", this.getEditFinancial());
        this.getSession().put("FromProgressValidationFlowFinancialProgress",
                true);
        goTo(PagesTypes.FINANCILAPROGRESSEDIT);
    }
    
    public void editEntityProcedureProgress()
    {
        this.getSession().put("procedure", this.getEditProcedure());
        this.getSession().put("FromProgressValidationProcedureProgress", true);
        this.getSession().put("getBackToProcedureProgressTab", true);
        this.goTo(PagesTypes.PROCEDUREPROGRESSEDIT);
    }
    
    public void saveEmp() throws HibernateException, PersistenceBeanException
    {
        this.setSelectTab(1);
        setSelectedIndex(2);
        for (PhisicalInitializationToEmploymentIndicators item : this
                .getListEmploymentIndicators())
        {
            if (item.getId().equals(Long.parseLong(this.getEntityEmpEditId())))
            {
                boolean isProgrammedValueUpdate = item
                        .getProgrammedValueUpdate() == null
                        || item.getProgrammedValueUpdate().isEmpty();
                boolean isGotValue = item.getGotValue() == null
                        || item.getGotValue().isEmpty();
                boolean isEngageValue = item.getEngageValue() == null
                        || item.getEngageValue().isEmpty();
                
                if (isEngageValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.engageValueIsMandatory"));
                    setEngageStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isGotValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.gotValueIsMandatory"));
                    setGotStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isProgrammedValueUpdate)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.programmedValueUpdateIsMandatory"));
                    setProgrammedUpdateStyle("background-color: #FFE5E5;");
                    return;
                }
            }
            
            if (item.getId().equals(Long.parseLong(this.getEntityEmpEditId())))
            {
                BeansFactory.PhisicalInizializationToEmploymentIndicators()
                        .Save(item);
                break;
            }
        }
        
        setEntityEmpEditId(null);
        setEntityCoreEditId(null);
        setEntityRealEditId(null);
        
    }
    
    public void saveReal() throws HibernateException, PersistenceBeanException
    {
        this.setSelectTab(1);
        setSelectedIndex(4);
        for (PhisicalInitializationToProgramIndicators item : this
                .getListRealization())
        {
            if (item.getId().equals(Long.parseLong(this.getEntityRealEditId())))
            {
                boolean isProgrammedValueUpdate = item
                        .getProgrammedValueUpdate() == null
                        || item.getProgrammedValueUpdate().isEmpty();
                boolean isGotValue = item.getGotValue() == null
                        || item.getGotValue().isEmpty();
                boolean isEngageValue = item.getEngageValue() == null
                        || item.getEngageValue().isEmpty();
                
                if (isEngageValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.engageValueIsMandatory"));
                    setEngageStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isGotValue)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.gotValueIsMandatory"));
                    setGotStyle("background-color: #FFE5E5;");
                    return;
                }
                else if (isProgrammedValueUpdate)
                {
                    this.setErrorMessage(Utils
                            .getString("validator.programmedValueUpdateIsMandatory"));
                    setProgrammedUpdateStyle("background-color: #FFE5E5;");
                    return;
                }
            }
            
            if (item.getId().equals(Long.parseLong(this.getEntityRealEditId())))
            {
                BeansFactory
                        .PhisicalInizializationToProgramRealizationIndicators()
                        .Save(item);
                break;
            }
        }
        
        setEntityEmpEditId(null);
        setEntityCoreEditId(null);
        setEntityRealEditId(null);
        
    }
    
    /**
     * Sets editMode
     * 
     * @param editMode
     *            the editMode to set
     */
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }
    
    /**
     * Gets editMode
     * 
     * @return editMode the editMode
     */
    public boolean isEditMode()
    {
        return editMode;
    }
    
    /**
     * Sets CFMode
     * 
     * @param cFMode
     *            the cFMode to set
     */
    public void setCFMode(boolean cFMode)
    {
        CFMode = cFMode;
    }
    
    /**
     * Gets CFMode
     * 
     * @return CFMode the cFMode
     */
    public boolean isCFMode()
    {
        return CFMode;
    }
    
    /**
     * Sets STCMode
     * 
     * @param sTCMode
     *            the sTCMode to set
     */
    public void setSTCMode(boolean sTCMode)
    {
        STCMode = sTCMode;
    }
    
    /**
     * Gets STCMode
     * 
     * @return STCMode the sTCMode
     */
    public boolean isSTCMode()
    {
        return STCMode;
    }
    
    /**
     * Sets AGUMode
     * 
     * @param aGUMode
     *            the aGUMode to set
     */
    public void setAGUMode(boolean aGUMode)
    {
        AGUMode = aGUMode;
    }
    
    /**
     * Gets AGUMode
     * 
     * @return AGUMode the aGUMode
     */
    public boolean isAGUMode()
    {
        return AGUMode;
    }
    
    /**
     * Sets listFinancial
     * 
     * @param listFinancial
     *            the listFinancial to set
     */
    public void setListFinancial(List<AnnualProfiles> listFinancial)
    {
        this.listFinancial = listFinancial;
    }
    
    /**
     * Gets listFinancial
     * 
     * @return listFinancial the listFinancial
     */
    public List<AnnualProfiles> getListFinancial()
    {
        return listFinancial;
    }
    
    /**
     * Sets listCoreIndicators
     * 
     * @param listCoreIndicators
     *            the listCoreIndicators to set
     */
    public void setListCoreIndicators(
            List<PhisicalInitializationToCoreIndicators> listCoreIndicators)
    {
        this.listCoreIndicators = listCoreIndicators;
    }
    
    /**
     * Gets listCoreIndicators
     * 
     * @return listCoreIndicators the listCoreIndicators
     */
    public List<PhisicalInitializationToCoreIndicators> getListCoreIndicators()
    {
        return listCoreIndicators;
    }
    
    /**
     * Sets listEmploymentIndicators
     * 
     * @param listEmploymentIndicators
     *            the listEmploymentIndicators to set
     */
    public void setListEmploymentIndicators(
            List<PhisicalInitializationToEmploymentIndicators> listEmploymentIndicators)
    {
        this.listEmploymentIndicators = listEmploymentIndicators;
    }
    
    /**
     * Gets listEmploymentIndicators
     * 
     * @return listEmploymentIndicators the listEmploymentIndicators
     */
    public List<PhisicalInitializationToEmploymentIndicators> getListEmploymentIndicators()
    {
        return listEmploymentIndicators;
    }
    
    /**
     * Sets listRealization
     * 
     * @param listRealization
     *            the listRealization to set
     */
    public void setListRealization(
            List<PhisicalInitializationToProgramIndicators> listRealization)
    {
        this.listRealization = listRealization;
    }
    
    /**
     * Gets listRealization
     * 
     * @return listRealization the listRealization
     */
    public List<PhisicalInitializationToProgramIndicators> getListRealization()
    {
        return listRealization;
    }
    
    /**
     * Sets listQsn
     * 
     * @param listQsn
     *            the listQsn to set
     */
    public void setListQsn(List<PhisicalInitializationToQsnIndicators> listQsn)
    {
        this.listQsn = listQsn;
    }
    
    /**
     * Gets listQsn
     * 
     * @return listQsn the listQsn
     */
    public List<PhisicalInitializationToQsnIndicators> getListQsn()
    {
        return listQsn;
    }
    
    /**
     * Sets listResults
     * 
     * @param listResults
     *            the listResults to set
     */
    public void setListResults(
            List<PhisicalInitializationToProgramIndicators> listResults)
    {
        this.listResults = listResults;
    }
    
    /**
     * Gets listResults
     * 
     * @return listResults the listResults
     */
    public List<PhisicalInitializationToProgramIndicators> getListResults()
    {
        return listResults;
    }
    
    /**
     * Sets listProcedures
     * 
     * @param listProcedures
     *            the listProcedures to set
     */
    public void setListProcedures(List<Procedures> listProcedures)
    {
        this.listProcedures = listProcedures;
    }
    
    /**
     * Gets listProcedures
     * 
     * @return listProcedures the listProcedures
     */
    public List<Procedures> getListProcedures()
    {
        return listProcedures;
    }
    
    /**
     * Sets itemsPerPageList
     * 
     * @param itemsPerPageList
     *            the itemsPerPageList to set
     */
    public void setItemsPerPageList(List<SelectItem> itemsPerPageList)
    {
        ProgressValidationFlowEditBean.itemsPerPageList = itemsPerPageList;
    }
    
    /**
     * Gets itemsPerPageList
     * 
     * @return itemsPerPageList the itemsPerPageList
     */
    public List<SelectItem> getItemsPerPageList()
    {
        return itemsPerPageList;
    }
    
    /**
     * Sets showScrollFin
     * 
     * @param showScrollFin
     *            the showScrollFin to set
     */
    public void setShowScrollFin(Boolean showScrollFin)
    {
        ProgressValidationFlowEditBean.showScrollFin = showScrollFin;
    }
    
    /**
     * Gets showScrollFin
     * 
     * @return showScrollFin the showScrollFin
     */
    public Boolean getShowScrollFin()
    {
        return showScrollFin;
    }
    
    /**
     * Sets itemsPerPageFin
     * 
     * @param itemsPerPageFin
     *            the itemsPerPageFin to set
     */
    public void setItemsPerPageFin(String itemsPerPageFin)
    {
        ProgressValidationFlowEditBean.itemsPerPageFin = itemsPerPageFin;
    }
    
    /**
     * Gets itemsPerPageFin
     * 
     * @return itemsPerPageFin the itemsPerPageFin
     */
    public String getItemsPerPageFin()
    {
        return itemsPerPageFin;
    }
    
    /**
     * Sets showScrollProcs
     * 
     * @param showScrollProcs
     *            the showScrollProcs to set
     */
    public void setShowScrollProcs(Boolean showScrollProcs)
    {
        ProgressValidationFlowEditBean.showScrollProcs = showScrollProcs;
    }
    
    /**
     * Gets showScrollProcs
     * 
     * @return showScrollProcs the showScrollProcs
     */
    public Boolean getShowScrollProcs()
    {
        return showScrollProcs;
    }
    
    /**
     * Sets itemsPerPageProcs
     * 
     * @param itemsPerPageProcs
     *            the itemsPerPageProcs to set
     */
    public void setItemsPerPageProcs(String itemsPerPageProcs)
    {
        ProgressValidationFlowEditBean.itemsPerPageProcs = itemsPerPageProcs;
    }
    
    /**
     * Gets itemsPerPageProcs
     * 
     * @return itemsPerPageProcs the itemsPerPageProcs
     */
    public String getItemsPerPageProcs()
    {
        return itemsPerPageProcs;
    }
    
    /**
     * Sets progressValidationObject
     * 
     * @param progressValidationObject
     *            the progressValidationObject to set
     */
    public void setProgressValidationObject(
            ProgressValidationObjects progressValidationObject)
    {
        this.progressValidationObject = progressValidationObject;
    }
    
    /**
     * Gets progressValidationObject
     * 
     * @return progressValidationObject the progressValidationObject
     */
    public ProgressValidationObjects getProgressValidationObject()
    {
        return progressValidationObject;
    }
    
    /**
     * Sets listDocuments
     * 
     * @param listDocuments
     *            the listDocuments to set
     */
    public void setListDocuments(
            List<ProgressValidationObjectDocuments> listDocuments)
    {
        this.getViewState().put("newDocument", listDocuments);
    }
    
    /**
     * Gets listDocuments
     * 
     * @return listDocuments the listDocuments
     */
    @SuppressWarnings("unchecked")
    public List<ProgressValidationObjectDocuments> getListDocuments()
    {
        return (List<ProgressValidationObjectDocuments>) this.getViewState()
                .get("newDocument");
    }
    
    /**
     * Sets newDocument
     * 
     * @param newDocument
     *            the newDocument to set
     */
    public void setNewDocument(ProgressValidationObjectDocuments newDocument)
    {
        this.newDocument = newDocument;
    }
    
    /**
     * Gets newDocument
     * 
     * @return newDocument the newDocument
     */
    public ProgressValidationObjectDocuments getNewDocument()
    {
        return newDocument;
    }
    
    /**
     * Sets newDocumentUpFile
     * 
     * @param newDocumentUpFile
     *            the newDocumentUpFile to set
     */
    public void setNewDocumentUpFile(UploadedFile newDocumentUpFile)
    {
        this.newDocumentUpFile = newDocumentUpFile;
    }
    
    /**
     * Gets newDocumentUpFile
     * 
     * @return newDocumentUpFile the newDocumentUpFile
     */
    public UploadedFile getNewDocumentUpFile()
    {
        return newDocumentUpFile;
    }
    
    /**
     * Sets entityDeleteId
     * 
     * @param entityDeleteId
     *            the entityDeleteId to set
     */
    public void setEntityDeleteId(Integer entityDeleteId)
    {
        this.getViewState().put("progressValidationFlowEditDeleteId",
                entityDeleteId);
    }
    
    /**
     * Gets entityDeleteId
     * 
     * @return entityDeleteId the entityDeleteId
     */
    public Integer getEntityDeleteId()
    {
        return (Integer) this.getViewState().get(
                "progressValidationFlowEditDeleteId");
    }
    
    /**
     * Sets entityDownloadId
     * 
     * @param entityDownloadId
     *            the entityDownloadId to set
     */
    public void setEntityDownloadId(Integer entityDownloadId)
    {
        this.getViewState().put("progressValidationFlowEditDownloadId",
                entityDownloadId);
    }
    
    /**
     * Gets entityDownloadId
     * 
     * @return entityDownloadId the entityDownloadId
     */
    public Integer getEntityDownloadId()
    {
        return (Integer) this.getViewState().get(
                "progressValidationFlowEditDownloadId");
    }
    
    /**
     * Sets selectTab
     * 
     * @param selectTab
     *            the selectTab to set
     */
    public void setSelectTab(int selectTab)
    {
        this.selectTab = selectTab;
    }
    
    /**
     * Gets selectTab
     * 
     * @return selectTab the selectTab
     */
    public int getSelectTab()
    {
        return selectTab;
    }
    
    /**
     * Sets newEntity
     * 
     * @param newEntity
     *            the newEntity to set
     */
    public void setNewEntity(boolean newEntity)
    {
        this.getSession().put("progressValidationFlowIsNewEntity", newEntity);
    }
    
    /**
     * Gets newEntity
     * 
     * @return newEntity the newEntity
     */
    public boolean isNewEntity()
    {
        return (Boolean) this.getSession().get(
                "progressValidationFlowIsNewEntity");
    }
    
    /**
     * Sets canFillMonitoringDate
     * 
     * @param canFillMonitoringDate
     *            the canFillMonitoringDate to set
     */
    public void setCanFillMonitoringDate(boolean canFillMonitoringDate)
    {
        this.canFillMonitoringDate = canFillMonitoringDate;
    }
    
    /**
     * Gets canFillMonitoringDate
     * 
     * @return canFillMonitoringDate the canFillMonitoringDate
     */
    public boolean isCanFillMonitoringDate()
    {
        return canFillMonitoringDate;
    }
    
    /**
     * Sets monitoringDateRowVisibility
     * 
     * @param monitoringDateRowVisibility
     *            the monitoringDateRowVisibility to set
     */
    public void setMonitoringDateRowVisibility(
            String monitoringDateRowVisibility)
    {
        this.monitoringDateRowVisibility = monitoringDateRowVisibility;
    }
    
    /**
     * Gets monitoringDateRowVisibility
     * 
     * @return monitoringDateRowVisibility the monitoringDateRowVisibility
     */
    public String getMonitoringDateRowVisibility()
    {
        return monitoringDateRowVisibility;
    }
    
    /**
     * Sets entityCoreEditId
     * 
     * @param entityCoreEditId
     *            the entityCoreEditId to set
     */
    public void setEntityCoreEditId(String entityCoreEditId)
    {
        this.getViewState().put("coreEditId", entityCoreEditId);
    }
    
    /**
     * Gets entityCoreEditId
     * 
     * @return entityCoreEditId the entityCoreEditId
     */
    public String getEntityCoreEditId()
    {
        return (String) this.getViewState().get("coreEditId");
    }
    
    /**
     * Sets entityEmpEditId
     * 
     * @param entityEmpEditId
     *            the entityEmpEditId to set
     */
    public void setEntityEmpEditId(String entityEmpEditId)
    {
        this.getViewState().put("empEditId", entityEmpEditId);
    }
    
    /**
     * Gets entityEmpEditId
     * 
     * @return entityEmpEditId the entityEmpEditId
     */
    public String getEntityEmpEditId()
    {
        return (String) this.getViewState().get("empEditId");
    }
    
    /**
     * Sets entityRealEditId
     * 
     * @param entityRealEditId
     *            the entityRealEditId to set
     */
    public void setEntityRealEditId(String entityRealEditId)
    {
        this.getViewState().put("realEditId", entityRealEditId);
    }
    
    /**
     * Gets entityRealEditId
     * 
     * @return entityRealEditId the entityRealEditId
     */
    public String getEntityRealEditId()
    {
        return (String) this.getViewState().get("realEditId");
    }
    
    /**
     * Sets errorMessage
     * 
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Gets errorMessage
     * 
     * @return errorMessage the errorMessage
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    /**
     * Sets selectedIndex
     * 
     * @param selectedIndex
     *            the selectedIndex to set
     */
    public void setSelectedIndex(Integer selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }
    
    /**
     * Gets selectedIndex
     * 
     * @return selectedIndex the selectedIndex
     */
    public Integer getSelectedIndex()
    {
        return selectedIndex;
    }
    
    /**
     * Sets canEditPhysical
     * 
     * @param canEditPhysical
     *            the canEditPhysical to set
     */
    public void setCanEditPhysical(boolean canEditPhysical)
    {
        this.canEditPhysical = canEditPhysical;
    }
    
    /**
     * Gets canEditPhysical
     * 
     * @return canEditPhysical the canEditPhysical
     */
    public boolean getCanEditPhysical()
    {
        return canEditPhysical;
    }
    
    /**
     * Sets editFinancial
     * 
     * @param editFinancial
     *            the editFinancial to set
     */
    public void setEditFinancial(String editFinancial)
    {
        this.editFinancial = editFinancial;
    }
    
    /**
     * Gets editFinancial
     * 
     * @return editFinancial the editFinancial
     */
    public String getEditFinancial()
    {
        return editFinancial;
    }
    
    /**
     * Sets editProcedure
     * 
     * @param editProcedure
     *            the editProcedure to set
     */
    public void setEditProcedure(String editProcedure)
    {
        this.editProcedure = editProcedure;
    }
    
    /**
     * Gets editProcedure
     * 
     * @return editProcedure the editProcedure
     */
    public String getEditProcedure()
    {
        return editProcedure;
    }
    
    /**
     * Sets engageStyle
     * 
     * @param engageStyle
     *            the engageStyle to set
     */
    public void setEngageStyle(String engageStyle)
    {
        this.engageStyle = engageStyle;
    }
    
    /**
     * Gets engageStyle
     * 
     * @return engageStyle the engageStyle
     */
    public String getEngageStyle()
    {
        return engageStyle;
    }
    
    /**
     * Sets gotStyle
     * 
     * @param gotStyle
     *            the gotStyle to set
     */
    public void setGotStyle(String gotStyle)
    {
        this.gotStyle = gotStyle;
    }
    
    /**
     * Gets gotStyle
     * 
     * @return gotStyle the gotStyle
     */
    public String getGotStyle()
    {
        return gotStyle;
    }
    
    /**
     * Sets programmedUpdateStyle
     * 
     * @param programmedUpdateStyle
     *            the programmedUpdateStyle to set
     */
    public void setProgrammedUpdateStyle(String programmedUpdateStyle)
    {
        this.programmedUpdateStyle = programmedUpdateStyle;
    }
    
    /**
     * Gets programmedUpdateStyle
     * 
     * @return programmedUpdateStyle the programmedUpdateStyle
     */
    public String getProgrammedUpdateStyle()
    {
        return programmedUpdateStyle;
    }
    
    public String getRole()
    {
        return (String) this.getViewState().get("selectedRole");
    }
    
    public void setRole(String entity)
    {
        this.getViewState().put("selectedRole", entity);
    }
    
    /**
     * Sets listSelectRoles
     * 
     * @param listSelectRoles
     *            the listSelectRoles to set
     */
    public void setListSelectRoles(List<SelectItem> listSelectRoles)
    {
        this.listSelectRoles = listSelectRoles;
    }
    
    /**
     * Gets listSelectRoles
     * 
     * @return listSelectRoles the listSelectRoles
     */
    public List<SelectItem> getListSelectRoles()
    {
        return listSelectRoles;
    }
    
    /**
     * Gets categories
     * @return categories the categories
     */
    public List<SelectItem> getCategories()
    {
        return categories;
    }
    
    /**
     * Sets categories
     * @param categories the categories to set
     */
    public void setCategories(List<SelectItem> categories)
    {
        this.categories = categories;
    }
    
    /**
     * Gets selectedCategory
     * @return selectedCategory the selectedCategory
     */
    public String getSelectedCategory()
    {
        return selectedCategory;
    }
    
    /**
     * Sets selectedCategory
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(String selectedCategory)
    {
        this.selectedCategory = selectedCategory;
    }
}
