/**
 * 
 */
package com.infostroy.paamns.web.beans.documentms;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.Time;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.enums.DocumentSections;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.UploadDocumentRoleType;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.ActionLogHelper;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.DocumentInfo;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.Category;
import com.infostroy.paamns.persistence.beans.entities.domain.Documents;
import com.infostroy.paamns.persistence.beans.entities.domain.SignedDocuments;
import com.infostroy.paamns.persistence.beans.entities.domain.UserRoles;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Sections;
import com.infostroy.paamns.persistence.beans.facades.UserRolesBean;
import com.infostroy.paamns.web.beans.EntityEditBean;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.CertificateInfo;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignaturePermissions;
import com.itextpdf.signatures.SignatureUtil;

/**
 * 
 * @author Sergey Zorin InfoStroy Co., 2010.
 * 
 */
public class DocumentEditBean extends EntityEditBean<Documents> 
{
	private boolean				editMode;

	private List<SelectItem>	listSections;

	private List<SelectItem>	listSelectRoles;

	private String				section;

	private String				docTitle;
	private String				docTitle2;
	
	private String 				hashtext_original;
	private String				hashtext_signed;
	
	private byte[] 				slice_original;
	private byte[] 				slice_signed;
	
	private String				signflag;
	
	private String				signername;
	private String				signdate;

	private UploadedFile		documentUpFile;
	private UploadedFile		documentUpFile2;

	private String				showErrorMessage;
	private String				showErrorMessage2;
	
	private String                authMessage = null;
	

	@SuppressWarnings("unused")
	private String				sectionTitle;

	private List<SelectItem>	listPartners;

	private List<SelectItem>	categories;
	

	@Override
	public void AfterSave()
	{
		this.GoBack();
	}

	@Override
	public void GoBack()
	{
		//this.getSession().put("documentMSEntityId2", this.getEntityEditId2());
		this.goTo(PagesTypes.DOCUMENTLIST);
		
	}
		
	/*private String accumulateErrors(String[] errors) {
		// TODO Auto-generated method stub
		return null;
	} */

	/*private String checkForErrors(String string, String string2, String destPath,
			HashMap<Integer, List<Rectangle>> ignoredAreas) {
		// TODO Auto-generated method stub
		return null;
	} */

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException,
			PersistenceBeanException, IOException
	{
		this.setEditMode(!Boolean.valueOf(String.valueOf(this.getSession().get(
				"documentMSShowOnly"))));
		this.setShowErrorMessage("none");

		this.fillSections();
		this.fillRoles();
		this.fillCategories();
		this.LoadDocument();
		if (this.getSessionBean().isCIL())
		{
			listPartners = BeansFactory.CFPartnerAnagraphProject()
					.getPartnersForCil(this.getProjectId(),
							this.getCurrentUser().getId());			
		}
		else
		{
			listPartners = new ArrayList<SelectItem>();
			listPartners.add(SelectItemHelper.getFirst());
		}
	}

	private void fillCategories() throws PersistenceBeanException
	{
		categories = new ArrayList<SelectItem>();
		categories.add(SelectItemHelper.getFirst());

		for (Category item : BeansFactory.Category().Load())
		{
			categories.add(new SelectItem(String.valueOf(item.getId()), item
					.getName()));
		}
	}

	private void fillSections() throws PersistenceBeanException
	{
		List<Sections> listSections = BeansFactory.Sections().Load();
		this.setListSections(new ArrayList<SelectItem>());
		this.getListSections().add(SelectItemHelper.getFirst());

		for (Sections s : listSections)
		{
			if(s.getId().equals(DocumentSections.PotentialProjecs.getValue())){
				continue;
			}
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(s.getId()));
			item.setLabel(s.getValue());

			this.getListSections().add(item);
		}
	}

	private void fillRoles() throws PersistenceBeanException
	{
		this.setListSelectRoles(new ArrayList<SelectItem>());

		this.getListSelectRoles().add(SelectItemHelper.getFirst());
		Set<String> temp = new HashSet<String>();
		for (UserRoles userRole : UserRolesBean.GetByUser(String.valueOf(this
				.getCurrentUser().getId())))
		{
			if(userRole.getRole().getCode().equals("DAEC")){
				continue;
			}
			if (temp.add(userRole.getRole().getCode()))
			{
				UploadDocumentRoleType roleType = UploadDocumentRoleType
						.getByName(userRole.getRole().getCode());
				if (roleType != null)
				{
					this.getListSelectRoles().add(
							new SelectItem(String.valueOf(roleType.getId()),
									roleType.getDisplayName()));
				}
			}
		}
	}

	private void LoadDocument() throws HibernateException,
			PersistenceBeanException, IOException
	{
		Boolean sign_file_format = false;
		
		if (this.getViewState().get("Document") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document");
			
			this.getDocument().setTitle(
					FileHelper.getFileNameWOExtension(docinfo.getName()));
			this.getDocument().setCreateDate(DateTime.getNow());
			this.getDocument().setDeleted(false);
			this.getDocument().setDocumentDate(docinfo.getDate());
			this.getDocument().setName(docinfo.getName());
			this.getDocument().setUser(this.getSessionBean().getCurrentUser());
			this.getDocument().setFileName(docinfo.getFileName());
			this.getDocument().setIsPublic(docinfo.getIsPublic());
			docinfo.getSignFlag();

			if (docinfo.getCategory() != null)
			{
				this.setSelectedCategory(String.valueOf(docinfo.getCategory()
						.getId()));
			}
			if (this.getRole() != null && !this.getRole().isEmpty())
			{
				this.getDocument().setUploadRole(
						Integer.parseInt(this.getRole()));
			}
			if (this.getSelectedPartner() != null
					&& !this.getSelectedPartner().isEmpty())
			{
				this.getDocument().setForPartner(
						BeansFactory.Users().Load(this.getSelectedPartner()));
			}

			this.setDocTitle(this.getDocument().getTitle()); 

			this.getDocument().setSignflag(Utils.getString("no"));
			
			String path1 = this.getDocument().getFileName();
			
			if (FilenameUtils.getExtension(path1).equals("pdf")) {   // the hash computation and content file extraction can be done only for PDF documents in this code
				
				
				/////////////////////////////////////////////
				/// PDF content extraction in byte array ///
				///////////////////////////////////////////
				InputStream inputStream = null;
				inputStream = new FileInputStream(path1);
				
				byte[] buffer = new byte[1];
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();

			    int bytesRead;
			    while ((bytesRead = inputStream.read(buffer)) != -1) 
			    {
			        baos.write(buffer, 0, bytesRead); 
			    }
				String prove = "%PDF-1";
				byte[] arr_prove = prove.getBytes();
				
				int ind_pdf = -1;
				
				byte[] baos_arr = baos.toByteArray(); 
				for(int i = 0; i < baos.size() - arr_prove.length+1; ++i) {
			        boolean found = true;
			        for(int j = 0; j < arr_prove.length; ++j) {
			           if (baos_arr[i+j] != arr_prove[j]) {
			               found = false;
			               break;
			           }			           
			        }
			        if (found) {
			        	ind_pdf=i;
			        	break;
			        }
			   
			}  

				String prove2 = "%%EOF";
				byte[] arr_prove2 = prove2.getBytes();
				
				int ind_eof = -1;
				
				for(int i = 0; i < baos.size() - arr_prove2.length+1; ++i) {
			        boolean found = true;
			        for(int j = 0; j < arr_prove2.length; ++j) {
			           if (baos_arr[i+j] != arr_prove2[j]) {
			               found = false;
			               break;
			           }
			        }
			        if (found) ind_eof=i;
			        
			   
			}  		
				byte[] slice_original = Arrays.copyOfRange(baos_arr, ind_pdf, ind_eof+6);
				
				this.slice_original = slice_original;
				inputStream.close();
				
				
				/////////////////////////////
				/// PDF Hash Computation ///
				////////////////////////////
				
				com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(path1);
								
				MessageDigest messageDigest;
				try {
					messageDigest = MessageDigest.getInstance("MD5"); 
					
					int pageCount = reader.getNumberOfPages(); 
					
					;
					for(int i=1;i <= pageCount; i++)
					{
											 
					     byte[] buf = reader.getPageContent(i);
					    
					     messageDigest.update(buf, 0, buf.length);

					}
					
					byte[] hash = messageDigest.digest(); 
				     BigInteger bigInt = new BigInteger(1,hash);
				     
				     String hashtext_original = bigInt.toString(16);
				     
				     // Now we need to zero pad it if you actually want the full 32 chars.
				     while(hashtext_original.length() < 32 ){
				    	 hashtext_original = "0"+hashtext_original;
				     }
				    
				     this.hashtext_original =  hashtext_original;
				    
				
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}
		else
		{
			if (this.isNewEntity())
			{
				if (this.getDocument().getDocumentDate() == null)
				{
					this.getDocument().setDocumentDate(DateTime.getNow());
				}
				if (this.getDocument().getEditableDate() == null)
				{
					this.getDocument().setEditableDate(DateTime.getNow());
				}
				this.getDocument().setProject(getProject());
				this.getDocument().setUser(
						this.getSessionBean().getCurrentUser());
				this.getDocument()
						.setSection(
								BeansFactory.Sections().Load(
										DocumentSections.DocumentManagement
												.getValue()));
				
				this.setDocTitle("");
				
				this.setSignFLAG("");
				this.getDocument().setSignflag(Utils.getString("no"));
				
				this.getDocument().setLoadingDate(new Date());
				
			}
			else
			{
				this.setDocTitle(this.getDocument().getTitle());   

				this.getDocument().setSignflag(Utils.getString("no"));

				if(this.getDocument().getLoadingDate()==null){
					this.getDocument().setLoadingDate(new Date());
				}

				if (this.getDocument().getFileName() != null && !this.getDocument().getFileName().equals(""))
				{
										
					String path1 = FileHelper.getFileDirectory() + File.separator + this.getDocument().getFileName();
					
					if (FilenameUtils.getExtension(path1).equals("pdf"))  {  // the hash computation and content file extraction can be done only for PDF documents in this code
					
						/////////////////////////////////////////////
						/// PDF content extraction in byte array ///
						///////////////////////////////////////////
					
						InputStream inputStream = null;
						inputStream = new FileInputStream(path1);
						
						byte[] buffer = new byte[1];
					    ByteArrayOutputStream baos = new ByteArrayOutputStream();

					    int bytesRead;
					    while ((bytesRead = inputStream.read(buffer)) != -1) 
					    {
					        baos.write(buffer, 0, bytesRead); 
					    }
						String prove = "%PDF-1";
						byte[] arr_prove = prove.getBytes();
						
						int ind_pdf = -1;
						
						byte[] baos_arr = baos.toByteArray(); 
						for(int i = 0; i < baos.size() - arr_prove.length+1; ++i) {
					        boolean found = true;
					        for(int j = 0; j < arr_prove.length; ++j) {
					           if (baos_arr[i+j] != arr_prove[j]) {
					               found = false;
					               break;
					           }			           
					        }
					        if (found) {
					        	ind_pdf=i;
					        	break;
					        }
					   
					}  

						String prove2 = "%%EOF";
						byte[] arr_prove2 = prove2.getBytes();
						
						int ind_eof = -1;
						
						for(int i = 0; i < baos.size() - arr_prove2.length+1; ++i) {
					        boolean found = true;
					        for(int j = 0; j < arr_prove2.length; ++j) {
					           if (baos_arr[i+j] != arr_prove2[j]) {
					               found = false;
					               break;
					           }
					        }
					        if (found) ind_eof=i;
					        
					   
					}  		
						byte[] slice_original = Arrays.copyOfRange(baos_arr, ind_pdf, ind_eof+6);
						
						this.slice_original = slice_original;
						inputStream.close();
						
						/////////////////////////////
						/// PDF Hash Computation ///
						////////////////////////////
												
						com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(path1);
						MessageDigest messageDigest;
						try {
							messageDigest = MessageDigest.getInstance("MD5");
							int pageCount = reader  .getNumberOfPages(); 
							for(int i=1;i <= pageCount; i++)
							{
							     byte[] buf = reader.getPageContent(i);
							     messageDigest.update(buf, 0, buf.length);
	
							}
							
						     byte[] hash = messageDigest.digest(); 
	
						     BigInteger bigInt = new BigInteger(1,hash);
						     
						     String hashtext_original = bigInt.toString(16);
						     
						     // Now we need to zero pad it if you actually want the full 32 chars.
						     while(hashtext_original.length() < 32 ){
						    	 hashtext_original = "0"+hashtext_original;
						     }
						     
						     this.hashtext_original =  hashtext_original;						     
						     
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}   // end check signed PDF File
				} 
			}

		}
		
		
		/// Check for Signed Document (Document2)
		if (this.getViewState().get("Document2") != null && (this.docTitle != null && !this.docTitle.equals("")))
		{		
		//////////////////////////////////////////////////////////////////////////////////////////////
		// Check on extension of original file (this must be a PDF document to allow the signature) //
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		if(FilenameUtils.getExtension(this.getDocument().getFileName()).equals("pdf")) {
			
			
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document2");

			this.getDocument2().setTitle(
					FileHelper.getFileNameWOExtension(docinfo.getName()));
			this.getDocument2().setCreateDate(DateTime.getNow());
			this.getDocument2().setDeleted(false);
			
			this.getDocument2().setName(docinfo.getName());
			
			this.getDocument2().setFileName(docinfo.getFileName());

			this.setSignFLAG(Utils.getString("yes"));
			
			if (docinfo.getCategory() != null)
			{
				this.setSelectedCategory(String.valueOf(docinfo.getCategory()
						.getId()));
			}
			
						
			/////////////////////////////////////////////////////
			/// Check on Format of Signed Document (For PDF) ///
			////////////////////////////////////////////////////
			String path2 = this.getDocument2().getFileName();
			
			//////////////////////////////////////////////////////
			//		Set provider to read signed documents       //
			//////////////////////////////////////////////////////
			
			BouncyCastleProvider provider = new BouncyCastleProvider();
			Security.addProvider(provider);
			
			if(FilenameUtils.getExtension(path2).equals("pdf")) {
				
				/////////////////////////////////////////////////////
				/////       Hash Computation                    /////
				////////////////////////////////////////////////////
				com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(path2); 
				
				
				MessageDigest messageDigest;
				try {
					messageDigest = MessageDigest.getInstance("MD5");
					int pageCount = reader  .getNumberOfPages(); 
					for(int i=1;i <= pageCount; i++)
					{
					     byte[] buf = reader.getPageContent(i);
					     messageDigest.update(buf, 0, buf.length);					     
					     
					}
					
					byte[] hash = messageDigest.digest(); 					     
				     
				     BigInteger bigInt = new BigInteger(1,hash);
				     
				     String hashtext_signed = bigInt.toString(16);
				     
				     // Now we need to zero pad it if you actually want the full 32 chars.
				     while(hashtext_signed.length() < 32 ){
				    	 hashtext_signed = "0"+hashtext_signed;
				     }
				     this.hashtext_signed =  hashtext_signed;
				     
					////////////////////////////////////////////////////////////////////////////////
					// Check on Corrispondence between Signed and Unsigned Documents with Hash    //
					///////////////////////////////////////////////////////////////////////////////
					
					if (this.hashtext_original.equals(this.hashtext_signed)) {
					
					
						////////////////////////////////////
						/// Check if document is signed ///
						/////////////////////////////////////
						
						
						PdfDocument pdfDoc = new PdfDocument(new PdfReader(path2));  
						
						PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);  // se form è null non è un file che contiene firme (il false significa che se il documento non esiste no lo crea)
						
						if (form !=null) {
						//System.out.println("Document is signed");
	
							this.setDocTitle2(this.getDocument2().getTitle()); 
							
							SignaturePermissions perms = null;
							SignatureUtil signUtil = new SignatureUtil(pdfDoc);
							List<String> names = signUtil.getSignatureNames();  // recupera i nomi dei campi in cui sono presenti le firme
							for (String name : names) {
							//System.out.println("===== " + name + " =====");
								try {
								perms = inspectSignature(pdfDoc, signUtil, form, name, perms);
								} catch (GeneralSecurityException e) {
								// TODO Auto-generated catch block
									e.printStackTrace();
								}  // metodo per estrarre le informazioni dal file firmato
							}
							
							this.setSignername(this.getDocument2().getSignerName());
							this.setSigndate(this.getDocument2().getSignDate());
							this.getDocument().setSignflag(Utils.getString("yes"));
							
						}
						else {
							//System.out.println("Document is not signed");
							setAuthMessage(Utils.getString("validatorSignedDocuments2"));   // se il file non è firmato
							} 

						}
						else {
						setAuthMessage(Utils.getString("validatorSignedUnsigned"));  // se l'hash non coincide
						}
											
							} catch (NoSuchAlgorithmException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							
				
			}
			
			
			///////////////////////////////////////
			//  for p7m signed file format      //
			/////////////////////////////////////
			if(FilenameUtils.getExtension(path2).equals("p7m")) {
				
				InputStream inputStream = null;
				inputStream = new FileInputStream(path2);
				
				byte[] buffer = new byte[1];
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();

			    int bytesRead;
			    while ((bytesRead = inputStream.read(buffer)) != -1) 
			    {
			        baos.write(buffer, 0, bytesRead); 
			    }
				String prove = "%PDF-1";
				byte[] arr_prove = prove.getBytes();
				
				int ind_pdf = -1;
				
				byte[] baos_arr = baos.toByteArray(); 
				for(int i = 0; i < baos.size() - arr_prove.length+1; ++i) {
			        boolean found = true;
			        for(int j = 0; j < arr_prove.length; ++j) {
			           if (baos_arr[i+j] != arr_prove[j]) {
			               found = false;
			               break;
			           }
			        }
			        if (found) {
			        	ind_pdf=i;
			        	break;
			        }
			        
			   
			}  

				String prove2 = "%%EOF";
				byte[] arr_prove2 = prove2.getBytes();
				
				int ind_eof = -1;
				//byte[] baos_arr = baos.toByteArray();
				for(int i = 0; i < baos.size() - arr_prove2.length+1; ++i) {
			        boolean found = true;
			        for(int j = 0; j < arr_prove2.length; ++j) {
			           if (baos_arr[i+j] != arr_prove2[j]) {
			               found = false;
			               break;
			           }
			        }
			        if (found) ind_eof=i;
			        
			   
			}  		
				byte[] slice_signed = Arrays.copyOfRange(baos_arr, ind_pdf, ind_eof+6);
				//System.out.println(new String(slice_signed));
				this.slice_signed = slice_signed;
				inputStream.close();

				//////////////////////////////////////////////////////////////////////////////////////////////
				// Check on Corrispondence between Signed and Unsigned Documents with Content Comparison    //
				//////////////////////////////////////////////////////////////////////////////////////////////
				
				if (Arrays.equals(this.slice_original, this.slice_signed)) {   
										
					////////////////////////////////////
					/// Check if document is signed ///
					/////////////////////////////////////					
					SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
					
					try {				
						
							File f = new File(path2);
						
						byte[] buffer2 = new byte[(int) f.length()];
						
						DataInputStream in = new DataInputStream(new FileInputStream(f));
						in.readFully(buffer2);
						in.close();
						CMSSignedData signature = new CMSSignedData(buffer2);
						org.bouncycastle.util.Store<X509CertificateHolder> cs = signature.getCertificates();
						SignerInformationStore signers = signature.getSignerInfos();
						Collection c = signers.getSigners();
						java.util.Iterator it = c.iterator();
						byte[] data = null;
						
						if (!it.equals(null) && !it.equals("")) {   // it is null if document is unsigned
						while (it.hasNext()) {
					        SignerInformation signer = (SignerInformation) it.next();
					        
					       
						        AttributeTable atab = signer.getSignedAttributes();
						        Date result = null;
						        if (atab != null)
						        {
						            org.bouncycastle.asn1.cms.Attribute attr = atab.get(CMSAttributes.signingTime);
						            if (attr != null)
						            {
						                Time t = Time.getInstance(attr.getAttrValues().getObjectAt(0)
						                        .toASN1Primitive());
						                result = t.getDate();
						                
						                date_format.format(result);
						            }
						        }
						        //System.out.println(date_format.format(result));	// estrazione corretta della data della firma
						        Collection certCollection = cs.getMatches(signer.getSID());
						        java.util.Iterator certIt = certCollection.iterator();
						        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
						        
						        //System.out.println(cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());   // estrazione corretta del nome del firmatario
						        //System.out.println(cert.getSubject().getRDNs(BCStyle.T)[0].getFirst().getValue().toString());
						        						        				        
						        CMSProcessable sc = signature.getSignedContent();
						        						        
						        data = (byte[]) sc.getContent()  ;                   // qui ho l'intero contenuto del file firmato
						        //System.out.println(this.getDocument2().signdate);
						        this.getDocument2().signername = cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
						        this.setSignername(cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString());
						        						        
						        this.getDocument2().signdate = date_format.format(result);
						        this.setSigndate(date_format.format(result)); 
						        
								
							}
						
						this.setDocTitle2(this.getDocument2().getTitle());
						this.setSignername(this.getDocument2().getSignerName());
						this.setSigndate(this.getDocument2().getSignDate());
						this.getDocument().setSignflag(Utils.getString("yes"));
						}
						else {
				        	setAuthMessage(Utils.getString("validatorSignedDocuments2"));   // se il file non è firmato
				        }
											
					} catch (CMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					
					
				}  // se i contenuti non coincidono
				else {
					setAuthMessage(Utils.getString("validatorSignedUnsigned"));
				}
														
			}
			
			///////////////////////////////////////
			//  Check on signed file format      //
			/////////////////////////////////////
			if(!FilenameUtils.getExtension(path2).equals("p7m") && !FilenameUtils.getExtension(path2).equals("pdf")) {
				setAuthMessage(Utils.getString("validatorSignedDocuments3"));
			}
			
			}  // end of check if unsigned document is in PDF format
		
		else {
			setAuthMessage(Utils.getString("validatorUnsignedPdf"));
		}
	
		}
		else
		{
			if (this.isNewEntity2() && (this.docTitle != null && !this.docTitle.equals("")) && this.getDocument2() != null)  // implica che il primo file è stato caricato
			{
				if (this.getDocument2().getEditableDate() == null)
				{
					this.getDocument2().setEditableDate(DateTime.getNow());
				}
				
				this.setDocTitle2("");
				this.setSignername("");
				this.setSigndate("");
				this.getDocument().setSignflag(Utils.getString("no"));

			} 
			else {
				if (this.getDocument2() != null) {
					
					this.setDocTitle2(this.getDocument2().getTitle());  
					this.setSignername(this.getDocument2().getSignerName());
					this.setSigndate(this.getDocument2().getSignDate());
					this.getDocument().setSignflag(Utils.getString("yes"));
					this.setSignFLAG(Utils.getString("yes"));

				}

			} 

		}
	}
	
	private SignaturePermissions inspectSignature(PdfDocument pdfDoc, SignatureUtil signUtil, PdfAcroForm form,
			String name, SignaturePermissions perms)throws GeneralSecurityException, IOException {
        /*if (form.getField(name).getWidgets() != null && form.getField(name).getWidgets().size() > 0) {
            int pageNum = 0;
            Rectangle pos = form.getField(name).getWidgets().get(0).getRectangle().toRectangle();
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfPage page = pdfDoc.getPage(i);
                for (PdfAnnotation annot : page.getAnnotations()) {
                    //System.out.println();
                    if (PdfName.Sig.equals(annot.getPdfObject().get(PdfName.FT)) && name.equals(annot.getPdfObject().get(PdfName.T).toString())) {
                        pageNum = pdfDoc.getPageNumber(page);
                        break;
                    }
                }
            }
            if (pos.getWidth() == 0 || pos.getHeight() == 0) {
                System.out.println("Invisible signature");
            } else {
                System.out.println(String.format("Field on page %s; llx: %s, lly: %s, urx: %s; ury: %s", pageNum, pos.getLeft(), pos.getBottom(), pos.getRight(), pos.getTop()));
            }
        } */
        
        
        //System.out.println("Signature covers whole document: " + signUtil.signatureCoversWholeDocument(name));
        //System.out.println("Document revision: " + signUtil.getRevision(name) + " of " + signUtil.getTotalRevisions());
        PdfPKCS7 pkcs7 = signUtil.verifySignature(name);
        //System.out.println("Integrity check OK? " + pkcs7.verify());
        
        
        //PdfPKCS7 pkcs7 = super.verifySignature(signUtil, name);
        //System.out.println("Digest algorithm: " + pkcs7.getHashAlgorithm());
        //System.out.println("Encryption algorithm: " + pkcs7.getEncryptionAlgorithm());
        //System.out.println("Filter subtype: " + pkcs7.getFilterSubtype());
        X509Certificate cert = (X509Certificate) pkcs7.getSigningCertificate();
        //System.out.println("Name of the signer: " + CertificateInfo.getSubjectFields(cert).getField("CN"));
        //System.out.println(this.getDocument2().signdate);
        this.getDocument2().signername = CertificateInfo.getSubjectFields(cert).getField("CN");
        this.setSignername(CertificateInfo.getSubjectFields(cert).getField("CN"));
        
        //if (pkcs7.getSignName() != null)  
            //System.out.println("Alternative name of the signer: " + pkcs7.getSignName());
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        //System.out.println("Signed on: " + date_format.format(pkcs7.getSignDate().getTime()));
        //System.out.println(date_format.format(pkcs7.getSignDate().getTime()));
        this.getDocument2().signdate = date_format.format(pkcs7.getSignDate().getTime());
        this.setSigndate(date_format.format(pkcs7.getSignDate().getTime()));       
        
        /*if (pkcs7.getTimeStampDate() != null) {
            //System.out.println("TimeStamp: " + date_format.format(pkcs7.getTimeStampDate().getTime()));
            TimeStampToken ts = pkcs7.getTimeStampToken();
            System.out.println("TimeStamp service: " + ts.getTimeStampInfo().getTsa());
            System.out.println("Timestamp verified? " + pkcs7.verifyTimestampImprint());
        } */
        
        
       /* X509Certificate signingCertificate = pkcs7.getSigningCertificate();  // dà informazioni sul certificato
        System.out.println(signingCertificate.getPublicKey());
        try {
            // Try to verify certificate signature with its own public key
        PublicKey key = signingCertificate.getPublicKey();
        signingCertificate.verify(key);
        System.out.println("Certificate is self signed");
        } catch (SignatureException sigEx) {
            // Invalid signature --> not self-signed
            //return false;
        	System.out.println("Certificate not self-signed");
        } catch (InvalidKeyException keyEx) {
            // Invalid key --> not self-signed
            //return false;
        	System.out.println("Certificate not self-signed");
        }
        
        
        System.out.println(signingCertificate.getSerialNumber()); */
        /*try {
	        signingCertificate.checkValidity();  // restituisce true se il certificato è valido alla data e ora del controllo 	        
	        System.out.println("Certificate is still valid");
	        } catch (GeneralSecurityException e) {
	        	System.out.println("Certificate is not valid yet");
	            //return false;
	        }  */
        
        /*try {
        	signingCertificate.checkValidity(pkcs7.getSignDate().getTime());  // verifica se il certificato è valido al momento della firma     
	        System.out.println("The certificate was valid at the time of signing");
	        } catch (GeneralSecurityException e) {
	        	System.out.println("The certificate wasn't valid yet at the time of signing");
	            //return false;
	        } */
       
        /*Principal issuerDN = signingCertificate.getIssuerDN();  // capire se serve a risalire ad un certificato autofirmato o meno
        Principal subjectDN = signingCertificate.getSubjectDN();
        
        System.out.println("Location: " + pkcs7.getLocation());
        System.out.println("Reason: " + pkcs7.getReason());
        PdfDictionary sigDict = signUtil.getSignatureDictionary(name);
        PdfString contact = sigDict.getAsString(PdfName.ContactInfo);
        if (contact != null)
            System.out.println("Contact info: " + contact);
        perms = new SignaturePermissions(sigDict, perms);
        System.out.println("Signature type: " + (perms.isCertification() ? "certification" : "approval"));
        System.out.println("Filling out fields allowed: " + perms.isFillInAllowed());
        System.out.println("Adding annotations allowed: " + perms.isAnnotationsAllowed());  */
        
        /*for (SignaturePermissions.FieldLock lock : perms.getFieldLocks()) {
            System.out.println("Lock: " + lock.toString());
        } */
        return perms;
    }

	@Override
	public void Page_Load_Static() throws PersistenceBeanException
	{
		// Add user role logic if necessary
		//System.out.println(this.getSession().get("documentMSEntityId")); 
		//System.out.println(this.getViewState().get(docTitle2));
		if (this.getSession().get("documentMSEntityId") != null)  // se è diverso da null significa che il primo documento è stato già caricato
		{
			this.setDocument(BeansFactory.Documents()
					.Load(String.valueOf(this.getSession().get(
							"documentMSEntityId"))));

			if (this.getDocument().getSection() != null )								
			{
				if (this.getSelectedSection() == null)
				{
					this.setSelectedSection(String.valueOf(this.getDocument()
							.getSection().getId()));
				}
			}
			if (this.getDocument().getUploadRole() != null
					&& this.getDocument().getUploadRole() > 0)
			{
				this.setRole(String.valueOf(this.getDocument().getUploadRole()));
			}
			if (this.getDocument().getForPartner() != null)
			{
				this.setSelectedPartner(String.valueOf(this.getDocument()
						.getForPartner().getId()));
			}
			if (this.getDocument().getCategory() != null)
			{
				this.setSelectedCategory(String.valueOf(this.getDocument()
						.getCategory().getId()));
			}

			this.setNewEntity(false);

		}
		else
		{
			this.setDocument(new Documents());
			

			if (this.getSelectedSection() == null)
			{
				this.setSelectedSection(BeansFactory.Sections()
						.Load(DocumentSections.DocumentManagement.getValue())
						.getValue());
			}

			this.setNewEntity(true);
			
		}
		/////////////////////////////////// for second document uploaded
		;
		if (this.getSession().get("documentMSEntityId2") != null)  // se è diverso da null significa che il secondo documento è stato già caricato
			{
						
			this.setDocument2(BeansFactory.SignedDocuments()  
					.Load(String.valueOf(this.getSession().get(
							"documentMSEntityId2"))));
			
			this.setNewEntity2(false);
		}
		else {
			this.setDocument2(new SignedDocuments());
			if (this.getSelectedSection() == null)
				{
					this.setSelectedSection(BeansFactory.Sections()
							.Load(DocumentSections.DocumentManagement.getValue())
							.getValue());
				}
			this.setNewEntity2(true);
		}
		
	}

	@Override
	public void SaveEntity() throws HibernateException,
			PersistenceBeanException, NumberFormatException, IOException
	{
		if (this.getViewState().get("Document") != null)
		{
			String newFileName = FileHelper.newFileName(this.getDocument()
					.getName());

			FileHelper.copyFile(new File(getDocument().getFileName()),
					new File(newFileName));
			this.getDocument().setFileName(newFileName);
		}

		
		if(this.getDocument().getIsNew()){
			this.getDocument().setLoadFromDocuments(true);
			this.getDocument().setUser(this.getCurrentUser());
		}
				
		this.getDocument().setCreateDate(DateTime.getNow());
		this.getDocument().setDeleted(false);
		this.getDocument().setProject(this.getProject());		
		
		if (this.getRole() != null && !this.getRole().isEmpty())
		{
			this.getDocument().setUploadRole(Integer.parseInt(this.getRole()));
		}
		this.getDocument().setSection(
				BeansFactory.Sections().Load(this.getSelectedSection()));
						
		if (this.getSelectedCategory() != null
				&& !this.getSelectedCategory().isEmpty())
		{
			Category category = BeansFactory.Category().Load(
					this.getSelectedCategory());

			if (category != null
					&& getDocument().getCategory() != null
					&& !category.getId().equals(
							getDocument().getCategory().getId()))
			{
				try
				{
					String logFormat = "%s : %s - %s - %s";
					String ip = ((HttpServletRequest) FacesContext
							.getCurrentInstance().getExternalContext()
							.getRequest()).getRemoteAddr();
					String userName = getCurrentUser().getLogin();
					ActionLogHelper.saveActionToLog(String.format(logFormat,
							DateTime.ToStringWithMinutes(new Date()),
							Utils.getString("documentModuleNameForLog"),
							userName, ip));
				}
				catch (Exception e)
				{
					log.error(e);
				}

			}

			this.getDocument().setCategory(category);
			
		}

		BeansFactory.Documents().Save(this.getDocument());
						
		if (this.getViewState().get("Document2") != null)
		{
			String newFileName = FileHelper.newFileName(this.getDocument2()
					.getName());

			FileHelper.copyFile(new File(getDocument2().getFileName()),
					new File(newFileName));
			this.getDocument2().setFileName(newFileName);
		}
		
		if (this.getDocument2()!=null && this.getDocument2().getTitle() != null && !this.getDocument2().getTitle().equals("")) {
						
			this.getDocument2().setCreateDate(DateTime.getNow());
			this.getDocument2().setDeleted(false);
			this.getDocument2().setForDocument(BeansFactory.Documents().Load(this.getDocument().getId()));
			
			BeansFactory.SignedDocuments().Save(this.getDocument2());
			}

	}

	public Boolean BeforeSave()
	{
		if (this.getDocTitle() == null || this.getDocTitle().equals(""))
		{
			this.setShowErrorMessage("");
			return false;
		}
		else
		{
			this.setShowErrorMessage("none");
			return true;
		}
	}

	public void getDoc()
	{
		if (this.getViewState().get("Document") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document");

			FileHelper.sendFile(docinfo, true);
		}
		else if (this.getDocument() != null)
		{
			FileHelper.sendFile(new DocumentInfo(null, this.getDocument()
					.getName(), this.getDocument().getFileName(), null, this.getDocument().getSignflag()), false);
		}
	}
	
	public void getDoc2()
	{
		if (this.getViewState().get("Document2") != null)
		{
			DocumentInfo docinfo = (DocumentInfo) this.getViewState().get(
					"Document2");

			FileHelper.sendFile(docinfo, true);
		}
		else if (this.getDocument2() != null)
		{
			FileHelper.sendFile(new DocumentInfo(null, this.getDocument2()
					.getName(), this.getDocument2().getFileName(), null, this.getDocument().getSignflag()), false);
		}
	}

	public void deleteDoc()
	{
		if (this.getViewState().get("Document") != null)
		{
			this.getViewState().put("Document", null);
		}
		else if (this.getDocument() != null)
		{
			this.getDocument().setName("");
			this.getDocument().setTitle("");
			this.getDocument().setFileName("");
		}

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDoc2()
	{
		if (this.getViewState().get("Document2") != null)
		{
			this.getViewState().put("Document2", null);
		}
		else if (this.getDocument2() != null)
		{
			this.getDocument2().setName("");
			this.getDocument2().setTitle("");
			this.getDocument2().setFileName("");
			
			
		}

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveDocument()
	{
		try
		{
			if (this.getDocumentUpFile() != null && this.getDocument() != null)
			{
				String fileName = FileHelper.newTempFileName(this
						.getDocumentUpFile().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getDocumentUpFile().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null
						&& !this.getSelectedCategory().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(this.getDocument()
						.getDocumentDate(), this.getDocumentUpFile().getName(),
						fileName, this.getDocument().getProtocolNumber(), cat,
						this.getDocument().getIsPublic(), this.getDocument().getSignflag());

				this.getViewState().put("Document", doc);
				
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e);
		}
		catch (IOException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveDocument2()
	{
		try
		{
			if (this.getDocumentUpFile2() != null && this.getDocument2() != null)
			{
				String fileName = FileHelper.newTempFileName(this
						.getDocumentUpFile2().getName());
				OutputStream os = new FileOutputStream(new File(fileName));
				os.write(this.getDocumentUpFile2().getBytes());
				os.close();

				Category cat = null;

				if (this.getSelectedCategory() != null
						&& !this.getSelectedCategory().isEmpty())
				{
					cat = BeansFactory.Category().Load(
							this.getSelectedCategory());
				}

				DocumentInfo doc = new DocumentInfo(null, this.getDocumentUpFile2().getName(), fileName, cat, this.getDocument().getSignflag());

				this.getViewState().put("Document2", doc);
				
			}
		}
		catch (HibernateException e)
		{
			log.error(e);
		}
		catch (FileNotFoundException e)
		{
			log.error(e);
		}
		catch (IOException e)
		{
			log.error(e);
		}
		catch (PersistenceBeanException e)
		{
			log.error(e);
		}

		try
		{
			this.Page_Load();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		catch (PersistenceBeanException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets document
	 * 
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Documents document)
	{
		this.getViewState().put("editEntity", document);
	}

	/**
	 * Gets document
	 * 
	 * @return document the document
	 */
	public Documents getDocument()
	{
		return (Documents) this.getViewState().get("editEntity");
	}

	/**
	 * Sets document
	 * 
	 * @param document
	 *            the document to set
	 */
	public void setDocument2(SignedDocuments document2)
	{
		this.getViewState().put("editEntity2", document2);
	}
	
	/**
	 * Gets document
	 * 
	 * @return document the document
	 */
	public SignedDocuments getDocument2()
	{
		return (SignedDocuments) this.getViewState().get("editEntity2"); 
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
	 * Sets listSections
	 * 
	 * @param listSections
	 *            the listSections to set
	 */
	public void setListSections(List<SelectItem> listSections)
	{
		this.listSections = listSections;
	}

	/**
	 * Gets listSections
	 * 
	 * @return listSections the listSections
	 */
	public List<SelectItem> getListSections()
	{
		return listSections;
	}

	/**
	 * Sets docTitle
	 * 
	 * @param docTitle
	 *            the docTitle to set
	 */
	public void setDocTitle(String docTitle)
	{
		this.docTitle = docTitle;
	}

	/**
	 * Gets docTitle
	 * 
	 * @return docTitle the docTitle
	 */
	public String getDocTitle()
	{
		return docTitle;
	}
	
	public void setDocTitle2(String docTitle2)
	{
		//if (this.getDocTitle().equals("")) {
		//this.docTitle2 = ""; //docTitle2;
		//} else {
			this.docTitle2 = docTitle2;
		//}
	}	
	public String getDocTitle2()
	{
		return docTitle2;
	}
	
	
	/**
	 * Sets hashtext_original
	 * 
	 * @param hashtext_original
	 *            the hashtext_original to set
	 */
	/*public void setHashFile(String hashtext_original)
	{
		this.hashtext_original = hashtext_original;
	} */

	/**
	 * Gets hashtext_original
	 * 
	 * @return hashtext_original the hashtext_original
	 */
	/*public String getHashFile()
	{
		return hashtext_original;
	} */
	
	
	/**
	 * Sets signflag
	 * 
	 * @param signflag
	 *            the signflag to set
	 */
	public void setSignFLAG(String signflag)
	{
		this.signflag = signflag;
	}

	/**
	 * Gets signflag
	 * 
	 * @return signflag the signflag
	 */
	public String getSignFLAG()
	{
		return signflag;
	}
	
	/**
	 * Sets signername
	 * 
	 * @param signername
	 *            the signername to set
	 */
	public void setSignername(String signername)
	{
		this.signername = signername;
	}

	/**
	 * Gets signername
	 * 
	 * @return signername the signername
	 */
	public String getSignername()
	{
		return signername;
	}
	
	/**
	 * Sets signdate
	 * 
	 * @param signdate
	 *            the signdate to set
	 */
	public void setSigndate(String signdate)
	{
		this.signdate = signdate;
	}

	/**
	 * Gets signdate
	 * 
	 * @return signdate the signdate
	 */
	public String getSigndate()
	{
		return signdate;
	}	

	/**
	 * Sets documentUpFile
	 * 
	 * @param documentUpFile
	 *            the documentUpFile to set
	 */
	public void setDocumentUpFile(UploadedFile documentUpFile)
	{
		this.documentUpFile = documentUpFile;
	}

	/**
	 * Gets documentUpFile
	 * 
	 * @return documentUpFile the documentUpFile
	 */
	public UploadedFile getDocumentUpFile()
	{
		return documentUpFile;
	}
	
	/**
	 * Sets documentUpFile2
	 * 
	 * @param documentUpFile2
	 *            the documentUpFile2 to set
	 */
	public void setDocumentUpFile2(UploadedFile documentUpFile2)
	{
		this.documentUpFile2 = documentUpFile2;
	}

	/**
	 * Gets documentUpFile
	 * 
	 * @return documentUpFile the documentUpFile
	 */
	public UploadedFile getDocumentUpFile2()
	{
		return documentUpFile2;
	}

	/**
	 * Sets isNewEntity
	 * 
	 * @param isNewEntity
	 *            the isNewEntity to set
	 */
	public void setNewEntity(boolean isNewEntity)
	{
		this.getViewState().put("isNewEntity", isNewEntity);
	}

	/**
	 * Gets isNewEntity
	 * 
	 * @return isNewEntity the isNewEntity
	 */
	public boolean isNewEntity()
	{
		return (Boolean) this.getViewState().get("isNewEntity");
	}
	/**
	 * Sets isNewEntity
	 * 
	 * @param isNewEntity
	 *            the isNewEntity to set
	 */
	public void setNewEntity2(boolean isNewEntity2)
	{
		this.getViewState().put("isNewEntity2", isNewEntity2);
	}

	/**
	 * Gets isNewEntity
	 * 
	 * @return isNewEntity the isNewEntity
	 */
	public boolean isNewEntity2()
	{
		return (Boolean) this.getViewState().get("isNewEntity2");
	}
	/**
	 * Sets selectedSection
	 * 
	 * @param selectedSection
	 *            the selectedSection to set
	 */
	public void setSelectedSection(String selectedSection)
	{
		this.getViewState().put("selectedSection", selectedSection);
	}

	/**
	 * Gets selectedSection
	 * 
	 * @return selectedSection the selectedSection
	 */
	public String getSelectedSection()
	{
		return (String) this.getViewState().get("selectedSection");
	}

	/**
	 * Sets showErrorMessage
	 * 
	 * @param showErrorMessage
	 *            the showErrorMessage to set
	 */
	public void setShowErrorMessage(String showErrorMessage)
	{
		this.showErrorMessage = showErrorMessage;
	}

	/**
	 * Gets showErrorMessage
	 * 
	 * @return showErrorMessage the showErrorMessage
	 */
	public String getShowErrorMessage()
	{
		return showErrorMessage;
	}

	/**
	 * Sets showErrorMessage2
	 * 
	 * @param showErrorMessage2
	 *            the showErrorMessage2 to set
	 */
	public void setShowErrorMessage2(String showErrorMessage2)
	{
		this.showErrorMessage2 = showErrorMessage2;
	}

	/**
	 * Gets showErrorMessage2
	 * 
	 * @return showErrorMessage2 the showErrorMessage2
	 */
	public String getShowErrorMessage2()
	{
		return showErrorMessage2;
	}	
	
	/**
	 * Sets section
	 * 
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section)
	{
		this.section = section;
	}

	/**
	 * Gets section
	 * 
	 * @return section the section
	 */
	public String getSection()
	{
		return section;
	}

	/**
	 * Sets sectionTitle
	 * 
	 * @param sectionTitle
	 *            the sectionTitle to set
	 */
	public void setSectionTitle(String sectionTitle)
	{
		this.sectionTitle = sectionTitle;
	}

	/**
	 * Gets sectionTitle
	 * 
	 * @return sectionTitle the sectionTitle
	 * @throws PersistenceBeanException
	 * @throws NumberFormatException
	 * @throws HibernateException
	 */
	public String getSectionTitle() throws HibernateException,
			NumberFormatException, PersistenceBeanException
	{
		if (this.getSelectedSection() != null
				&& !this.getSelectedSection().isEmpty())
		{
			return BeansFactory.Sections().Load(this.getSelectedSection())
					.getValue();
		}
		else
		{
			return "";
		}
	}

	public String getRole()
	{
		return (String) this.getViewState().get("selectedRole");
	}

	public void setRole(String entity)
	{
		this.getViewState().put("selectedRole", entity);
	}

	public String getRoleTitle()
	{
		if (this.getRole() != null && !this.getRole().isEmpty())
		{
			return UploadDocumentRoleType.getById(
					Integer.parseInt(this.getRole())).getDisplayName();
		}
		else
		{
			return "";
		}
	}

	public String getPartnerTitle()
	{
		if (this.getDocument().getForPartner() != null)
		{
			StringBuilder sb = new StringBuilder();

			sb.append(this.getDocument().getForPartner().getName());
			sb.append(" - ");
			sb.append(this.getDocument().getForPartner().getSurname());
			return sb.toString();
		}
		return "";
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
	 * Sets selectedPartner
	 * 
	 * @param selectedPartner
	 *            the selectedPartner to set
	 */
	public void setSelectedPartner(String selectedPartner)
	{
		this.getViewState().put("selectedPartner", selectedPartner);
	}

	/**
	 * Gets selectedPartner
	 * 
	 * @return selectedPartner the selectedPartner
	 */
	public String getSelectedPartner()
	{
		return (String) this.getViewState().get("selectedPartner");
	}

	/**
	 * Sets listPartners
	 * 
	 * @param listPartners
	 *            the listPartners to set
	 */
	public void setListPartners(List<SelectItem> listPartners)
	{
		this.listPartners = listPartners;
	}

	/**
	 * Gets listPartners
	 * 
	 * @return listPartners the listPartners
	 */
	public List<SelectItem> getListPartners()
	{
		return listPartners;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories the categories
	 */
	public List<SelectItem> getCategories()
	{
		return categories;
	}

	/**
	 * Sets categories
	 * 
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<SelectItem> categories)
	{
		this.categories = categories;
	}

	/**
	 * Gets selectedCategory
	 * 
	 * @return selectedCategory the selectedCategory
	 */
	public String getSelectedCategory()
	{
		return (String) this.getViewState().get("selectedCategory");
	}

	/**
	 * Sets selectedCategory
	 * 
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(String selectedCategory)
	{
		this.getViewState().put("selectedCategory", selectedCategory);
	}

	public Boolean getCanEditCategory()
	{
		if (getSessionBean().isSTC() || getSessionBean().isAGU()
				|| getSessionBean().isACU() || getSessionBean().isAGUW()
				|| getSessionBean().isSTCW() || getSessionBean().isACUW())
		{
			return true;
		}
		return false;
	}
	
    /**
     * 
     * @param authMessage
     */
    public void setAuthMessage(String authMessage)
    {
        this.authMessage = authMessage;
    }
    
    /**
     * 
     * @return
     */
    public String getAuthMessage()
    {
        return this.authMessage;
    }
      
}
