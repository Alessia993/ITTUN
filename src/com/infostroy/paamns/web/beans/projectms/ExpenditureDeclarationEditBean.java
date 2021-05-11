package com.infostroy.paamns.web.beans.projectms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.ExpenditureDeclarationStatusTypes;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.enums.ReasonType;
import com.infostroy.paamns.common.enums.RecoverReasonType;
import com.infostroy.paamns.common.enums.TransferTypes;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.export.xls.XlsExport;
import com.infostroy.paamns.common.helpers.DateTime;
import com.infostroy.paamns.common.helpers.FileHelper;
import com.infostroy.paamns.common.helpers.SelectItemHelper;
import com.infostroy.paamns.common.helpers.ValidatorHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.PersistenceSessionManager;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.AdditionalFesrInfo;
import com.infostroy.paamns.persistence.beans.entities.domain.CFPartnerAnagraphs;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.DurInfos;
import com.infostroy.paamns.persistence.beans.entities.domain.DurSummaries;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclaration;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationAccountingPeriod;
import com.infostroy.paamns.persistence.beans.entities.domain.ExpenditureDeclarationDurCompilations;
import com.infostroy.paamns.persistence.beans.entities.domain.GeneralBudgets;
import com.infostroy.paamns.persistence.beans.entities.domain.Projects;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.ExpenditureDeclarationStates;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.PaymentWays;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.RequestTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;
import com.infostroy.paamns.web.beans.EntityListBean;
import com.infostroy.paamns.web.beans.wrappers.Annex1lWrapper;
import com.infostroy.paamns.web.beans.wrappers.Annex1mWrapper;
import com.infostroy.paamns.web.beans.wrappers.CostDefinitionsSpecificGoalsWrapper;
import com.infostroy.paamns.web.beans.wrappers.DURCompilationWrapper;
import com.infostroy.paamns.web.beans.wrappers.ExpenditureDeclarationAnnex1cWrapper;
import com.infostroy.paamns.web.beans.wrappers.ExpenditureDeclarationAnnex1dWrapper;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;

import jxl.write.WriteException;

public class ExpenditureDeclarationEditBean extends EntityListBean<DURCompilationWrapper> {
	enum TypeOfDetail {
		Annex1a("expenditureDeclarationEditAnnex1a", 1l, 7), Annex1b("expenditureDeclarationEditAnnex1b", 2l,
				0), Annex1c("expenditureDeclarationEditAnnex1c", 3l, 9), Annex1d("expenditureDeclarationEditAnnex1d",
						4l, 14), Annex1e("expenditureDeclarationEditAnnex1e", 5l, 14), Annex1f(
								"expenditureDeclarationEditAnnex1f", 6l,
								14), Annex1g("expenditureDeclarationEditAnnex1g", 7l, 13), Annex1h(
										"expenditureDeclarationEditAnnex1h", 8l,
										13), Annex1l("expenditureDeclarationEditAnnex1l", 10l, 12), Annex1m(
												"expenditureDeclarationEditAnnex1m", 11l,
												7), Annex2("expenditureDeclarationEditAnnex2", 12l, 0);

		private String key;

		private Long code;

		private int numberOfColumns;

		private TypeOfDetail(String key, Long code, int numberOfColumns) {
			this.key = key;
			this.code = code;
			this.numberOfColumns = numberOfColumns;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Long getCode() {
			return code;
		}

		public void setCode(Long code) {
			this.code = code;
		}

		public int getNumberOfColumns() {
			return numberOfColumns;
		}

		public void setNumberOfColumns(int numberOfColumns) {
			this.numberOfColumns = numberOfColumns;
		}

	}

	private List<SelectItem> types;

	private String expenditureDeclarationId;

	private Date compilationDate;

	private String protocolNumber;

	private Date protocolDate;

	private Double totAmountEligibleExpenditure;

	private Double totAmountPublicExpenditure;

	private Double totAmountPrivateExpenditure;

	private Double totAmountStateAid;

	private Double totAmountCoverageAdvanceAid;

	private Double totContributionInKindPubShare;

	private Double totalExpenditure;

	private boolean edit;

	private boolean submitted;

	private Long state_id;

	private boolean validationFailed;

	private boolean rendered;

	private String error;

	private boolean selectAll;

	private boolean showSelectAll;

	private List<DurCompilations> listSelectedDur;

	private Transaction tr;

	private Long entityId;

	private ExpenditureDeclaration ed;

	private List<RequestTypes> requestTypes;

	private boolean showButton;

	private boolean canDelete;

	private Long entityEditId;

	private List<SpecificGoals> lsg;

	private boolean showDetails;

	private List<SelectItem> annexList;

	private String message;

	private void fillTableExpenditureDeclaration2() {
		try {
			if (this.getDateFrom() != null && this.getDateTo() != null) {
				List<CostDefinitionsSpecificGoalsWrapper> cdsws = BeansFactory.CostDefinitions()
						.getByExpenditureDeclarationSpecificGoal(this.getDateFrom(), this.getDateTo());

				Calendar today = new GregorianCalendar();
				List<DURCompilationWrapper> dcwsnew = new ArrayList<>();

				boolean recompute = false;
				if (this.getSession().get("expanditureDeclarationEntityId") != null) {
					Long entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
					List<ExpenditureDeclarationAccountingPeriod> list = BeansFactory
							.ExpenditureDeclarationAccountingPeriod().getAllByExpenditureDeclaration(entityId);

					for (ExpenditureDeclarationAccountingPeriod edap : this.getListExpDeclWrapper()) {
						for (ExpenditureDeclarationAccountingPeriod edapDB : list) {
							if (edap.getSpecificObjective() != null && edapDB.getSpecificObjective() != null
									&& edap.getSpecificObjective().equals(edapDB.getSpecificObjective())) {
								edap.setAmountOfPastExDecl(edapDB.getAmountOfPastExDecl());
								edap.setAmountOfExDeclForAccountingPeriod(
										edapDB.getAmountOfExDeclForAccountingPeriod());
								edap.setAmountOfWithdrawalsAccountingPeriod(
										edapDB.getAmountOfWithdrawalsAccountingPeriod());
								edap.setSuspensionAmount(edapDB.getSuspensionAmount());
								edap.setCumulativeAmount(edapDB.getCumulativeAmount());
							}
						}
					}

					int i = 0;

					for (DURCompilationWrapper dcw : this.getList()) {
						if (dcw.isSelected()) {

							boolean founded = false;
							for (ExpenditureDeclarationDurCompilations eddc : BeansFactory
									.ExpenditureDeclarationDurCompilations().getAllByExpenditureDeclaratios(entityId)) {
								if (eddc.getDurCompilationsId().equals(dcw.getId())) {
									founded = true;

								}
							}
							if (!founded) {
								dcwsnew.add(dcw);
							}
							i++;
						}
					}
					if (BeansFactory.ExpenditureDeclarationDurCompilations().getAllByExpenditureDeclaratios(entityId)
							.size() != i) {
						recompute = true;
					}
				}

				if (recompute) {
					fillSpecificObjectiveForEditExpenditureDeclaration(cdsws, dcwsnew);
				}

				if ((Boolean) this.getSession().get("expenditureDeclarationNew")) {
					fillSpecificObjectiveForNewExpenditureDeclaration(cdsws, today);
				}

				fillTotalAxisRow();

			}

		} catch (

		Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}

	}

	private void fillSpecificObjectiveForEditExpenditureDeclaration(List<CostDefinitionsSpecificGoalsWrapper> cdsws,
			List<DURCompilationWrapper> dcwsnew) throws PersistenceBeanException {
		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			for (CostDefinitionsSpecificGoalsWrapper cdsw : cdsws) {
				if (edw.getSpecificObjective() != null
						&& edw.getSpecificObjective().equals(cdsw.getSpecificObjective())) {
					for (DURCompilationWrapper dcwnew : dcwsnew) {
						DurCompilations dc = BeansFactory.DurCompilations().Load(dcwnew.getId());

						for (CostDefinitions cd : dc.getCostDefinitions()) {
							if (cd != null && edw.getSpecificObjective() != null
									&& edw.getSpecificObjective().equals(cd.getProject().getSpecificGoal().getCode())) {
								edw.setAmountOfExDeclForAccountingPeriod(edw.getAmountOfExDeclForAccountingPeriod()
										+ (cd.getAguCertification() != null ? cd.getAguCertification() : 0d));

							}
						}
					}
				}
			}
		}
		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			edw.setCumulativeAmount(edw.getAmountOfPastExDecl() + edw.getAmountOfExDeclForAccountingPeriod()
					- edw.getAmountOfWithdrawalsAccountingPeriod() - edw.getSuspensionAmount());

		}
	}

	private void fillSpecificObjectiveForNewExpenditureDeclaration(List<CostDefinitionsSpecificGoalsWrapper> cdsws,
			Calendar today) throws PersistenceBeanException {
		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			for (CostDefinitionsSpecificGoalsWrapper cdsw : cdsws) {
				if (edw.getSpecificObjective() != null
						&& edw.getSpecificObjective().equals(cdsw.getSpecificObjective())) {
					if (cdsw.getExpDecSubmissionDate().before(today.getTime())) {
						// colonna a
						edw.setAmountOfPastExDecl(edw.getAmountOfPastExDecl()
								+ (cdsw.getAguCertification() != null ? cdsw.getAguCertification() : 0d));
						// colonna c
						edw.setAmountOfWithdrawalsAccountingPeriod(edw.getAmountOfWithdrawalsAccountingPeriod()
								+ ((cdsw.getTotAmountEliglExpRetired() != null) ? cdsw.getTotAmountEliglExpRetired()
										: 0d));
						// colonna d
						edw.setSuspensionAmount(
								edw.getSuspensionAmount() + ((cdsw.getTotAmountEligExpSuspended() != null)
										? cdsw.getTotAmountEligExpSuspended() : 0d));
					}
				}

			}

			for (DURCompilationWrapper dcw : this.getList()) {
				if (dcw.isSelected()) {
					DurCompilations dc = BeansFactory.DurCompilations().Load(dcw.getId());
					for (CostDefinitions cd : dc.getCostDefinitions()) {
						if (cd != null && edw.getSpecificObjective() != null
								&& edw.getSpecificObjective().equals(cd.getProject().getSpecificGoal().getCode())) {
							// colonna b
							edw.setAmountOfExDeclForAccountingPeriod(edw.getAmountOfExDeclForAccountingPeriod()
									+ (cd.getAguCertification() != null ? cd.getAguCertification() : 0d));
						}
					}
				}
			}
		}
		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			edw.setCumulativeAmount(edw.getAmountOfPastExDecl() + edw.getAmountOfExDeclForAccountingPeriod()
					- edw.getAmountOfWithdrawalsAccountingPeriod() - edw.getSuspensionAmount());
		}
	}

	private void fillTotalAxisRow() {
		Double amountOfPastExDecl[] = { 0d, 0d, 0d, 0d };
		Double amountOfExDeclForAccountingPeriod[] = { 0d, 0d, 0d, 0d };
		Double amountOfWithdrawalsAccountingPeriod[] = { 0d, 0d, 0d, 0d };
		Double suspensionAmount[] = { 0d, 0d, 0d, 0d };

		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			if (edw.getAxis().equals("1")) {
				amountOfPastExDecl[0] += edw.getAmountOfPastExDecl();
				amountOfExDeclForAccountingPeriod[0] += edw.getAmountOfExDeclForAccountingPeriod();
				amountOfWithdrawalsAccountingPeriod[0] += edw.getAmountOfWithdrawalsAccountingPeriod();
				suspensionAmount[0] += edw.getSuspensionAmount();
			} else if (edw.getAxis().equals("2")) {
				amountOfPastExDecl[1] += edw.getAmountOfPastExDecl();
				amountOfExDeclForAccountingPeriod[1] += edw.getAmountOfExDeclForAccountingPeriod();
				amountOfWithdrawalsAccountingPeriod[1] += edw.getAmountOfWithdrawalsAccountingPeriod();
				suspensionAmount[1] += edw.getSuspensionAmount();
			} else if (edw.getAxis().equals("3")) {
				amountOfPastExDecl[2] += edw.getAmountOfPastExDecl();
				amountOfExDeclForAccountingPeriod[2] += edw.getAmountOfExDeclForAccountingPeriod();
				amountOfWithdrawalsAccountingPeriod[2] += edw.getAmountOfWithdrawalsAccountingPeriod();
				suspensionAmount[2] += edw.getSuspensionAmount();
			} else if (edw.getAxis().equals("4")) {
				amountOfPastExDecl[3] += edw.getAmountOfPastExDecl();
				amountOfExDeclForAccountingPeriod[3] += edw.getAmountOfExDeclForAccountingPeriod();
				amountOfWithdrawalsAccountingPeriod[3] += edw.getAmountOfWithdrawalsAccountingPeriod();
				suspensionAmount[3] += edw.getSuspensionAmount();
			}
			if (edw.getAxis().equals(Utils.getString("expenditureDeclarationEditTotalFund"))) {
				edw.setAmountOfPastExDecl(0d);
				edw.setAmountOfExDeclForAccountingPeriod(0d);
				edw.setAmountOfWithdrawalsAccountingPeriod(0d);
				edw.setSuspensionAmount(0d);
				edw.setCumulativeAmount(0d);
			}
		}
		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			for (int i = 1; i <= amountOfPastExDecl.length; i++) {
				if (edw.getAxis().equals(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i)) {
					edw.setAmountOfPastExDecl(amountOfPastExDecl[i - 1]);
					edw.setAmountOfExDeclForAccountingPeriod(amountOfExDeclForAccountingPeriod[i - 1]);
					edw.setAmountOfWithdrawalsAccountingPeriod(amountOfWithdrawalsAccountingPeriod[i - 1]);
					edw.setSuspensionAmount(suspensionAmount[i - 1]);
					edw.setCumulativeAmount(edw.getAmountOfPastExDecl() + edw.getAmountOfExDeclForAccountingPeriod()
							- edw.getAmountOfWithdrawalsAccountingPeriod() - edw.getSuspensionAmount());
				}
			}
		}

		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			if (edw.getAxis().equals(Utils.getString("expenditureDeclarationEditTotalFund"))) {
				for (int i = 1; i <= amountOfPastExDecl.length; i++) {
					edw.setAmountOfPastExDecl(edw.getAmountOfPastExDecl() + amountOfPastExDecl[i - 1]);
					edw.setAmountOfExDeclForAccountingPeriod(
							edw.getAmountOfExDeclForAccountingPeriod() + amountOfExDeclForAccountingPeriod[i - 1]);
					edw.setAmountOfWithdrawalsAccountingPeriod(
							edw.getAmountOfWithdrawalsAccountingPeriod() + amountOfWithdrawalsAccountingPeriod[i - 1]);
					edw.setSuspensionAmount(edw.getSuspensionAmount() + suspensionAmount[i - 1]);
					edw.setCumulativeAmount(edw.getAmountOfPastExDecl() + edw.getAmountOfExDeclForAccountingPeriod()
							- edw.getAmountOfWithdrawalsAccountingPeriod() - edw.getSuspensionAmount());
				}
			}
		}
	}

	public void export() {
		try {
			XlsExport exporter = new XlsExport();
			byte[] data = exporter.createXls(this.getList(), ExportPlaces.CostCertification);
			InputStream is = new ByteArrayInputStream(data);
			FileHelper.sendFile(Utils.getString("exportCostCertification") + ".xls", is, data.length);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public InputStream exportXls() {
		InputStream is = null;
		byte[] data = null;
		try {
			XlsExport exporter = new XlsExport();

			String annex = "";
			if (TypeOfDetail.Annex1a.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1a.getKey();
				List<String> msg1 = annex1Apart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListExpDeclWrapper(), ExportPlaces.Annex1A, msg1, msg2);

			} else if (TypeOfDetail.Annex1b.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1b.getKey();
				List<String> msg1 = annex1Bpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsOnlyMessages(msg1, msg2);
			} else if (TypeOfDetail.Annex1c.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1c.getKey();
				List<String> msg1 = annex1Cpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1c(), ExportPlaces.Annex1C, msg1, msg2);
			} else if (TypeOfDetail.Annex1d.getCode().equals(this.getAnnex())) {
				// da qui
				annex = TypeOfDetail.Annex1d.getKey();
				List<String> msg1 = annex1Dpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1d(), ExportPlaces.Annex1D, msg1, msg2);
			} else if (TypeOfDetail.Annex1e.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1e.getKey();
				List<String> msg1 = annex1Epart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1e(), ExportPlaces.Annex1E, msg1, msg2);
			} else if (TypeOfDetail.Annex1f.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1f.getKey();
				List<String> msg1 = annex1Fpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1f(), ExportPlaces.Annex1F, msg1, msg2);
			} else if (TypeOfDetail.Annex1g.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1g.getKey();
				List<String> msg1 = annex1Gpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1g(), ExportPlaces.Annex1G, msg1, msg2);
			} else if (TypeOfDetail.Annex1h.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1h.getKey();
				List<String> msg1 = annex1Hpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1h(), ExportPlaces.Annex1H, msg1, msg2);
			} else if (TypeOfDetail.Annex1l.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1l.getKey();
				List<String> msg1 = annex1Lpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1l(), ExportPlaces.Annex1L, msg1, msg2);
			} else if (TypeOfDetail.Annex1m.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex1m.getKey();
				List<String> msg1 = annex1Mpart1();
				List<String> msg2 = part2();
				data = exporter.createXlsWithMessages(this.getListOfAnnex1m(), ExportPlaces.Annex1M, msg1, msg2);
			} else if (TypeOfDetail.Annex2.getCode().equals(this.getAnnex())) {
				annex = TypeOfDetail.Annex2.getKey();
				List<String> msg1 = annex2part1();
				List<String> msg2 = part2();
				data = exporter.createXlsOnlyMessages(msg1, msg2);
			}
			is = new ByteArrayInputStream(data);
			FileHelper
					.sendFile(
							Utils.getString(annex) + "_" + Utils.getString("expenditureDeclarationEditAccountingPeriod")
									+ "_" + this.getDateFromString() + "_" + this.getDateToString() + ".xls",
							is, data.length);
		} catch (WriteException | IllegalArgumentException | IllegalAccessException | InvocationTargetException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return is;

	}

	public void exportPdf() throws WriteException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IOException {
		byte[] data = null;
		InputStream input_document = null;

		XlsExport exporter = new XlsExport();

		// We will create output PDF document objects at this point
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(byteOutStream);
		PdfDocument pdf = new PdfDocument(writer);

		// pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new
		// SeascapeEventHandler());

		Document document = null;

		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

		String annex = "";
		List<String> msg1 = null;
		List<String> msg2 = null;
		TypeOfDetail annexSupp = null;
		if (TypeOfDetail.Annex1a.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1a;
			annex = TypeOfDetail.Annex1a.getKey();
			msg1 = annex1Apart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Apart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListExpDeclWrapper(), ExportPlaces.Annex1A);

		} else if (TypeOfDetail.Annex1b.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1b;
			annex = TypeOfDetail.Annex1b.getKey();
			msg1 = annex1Bpart1();
			document = new Document(pdf, PageSize.A4);
			document = annex1Bpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXlsOnlyMessages(msg1, msg2);
		} else if (TypeOfDetail.Annex1c.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1c;
			annex = TypeOfDetail.Annex1c.getKey();
			msg1 = annex1Cpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Cpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1c(), ExportPlaces.Annex1C);
		} else if (TypeOfDetail.Annex1c.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1c;
			annex = TypeOfDetail.Annex1c.getKey();
			msg1 = annex1Cpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Cpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1c(), ExportPlaces.Annex1C);
		} else if (TypeOfDetail.Annex1d.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1d;
			annex = TypeOfDetail.Annex1d.getKey();
			msg1 = annex1Dpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Dpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1d(), ExportPlaces.Annex1D);
		} else if (TypeOfDetail.Annex1e.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1e;
			annex = TypeOfDetail.Annex1e.getKey();
			msg1 = annex1Epart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Epart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1e(), ExportPlaces.Annex1E);
		} else if (TypeOfDetail.Annex1f.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1f;
			annex = TypeOfDetail.Annex1f.getKey();
			msg1 = annex1Fpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Fpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1f(), ExportPlaces.Annex1F);
		} else if (TypeOfDetail.Annex1g.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1g;
			annex = TypeOfDetail.Annex1g.getKey();
			msg1 = annex1Gpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Gpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1g(), ExportPlaces.Annex1G);
		} else if (TypeOfDetail.Annex1h.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1h;
			annex = TypeOfDetail.Annex1h.getKey();
			msg1 = annex1Hpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Hpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1h(), ExportPlaces.Annex1H);
		} else if (TypeOfDetail.Annex1l.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1l;
			annex = TypeOfDetail.Annex1l.getKey();
			msg1 = annex1Lpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Lpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1l(), ExportPlaces.Annex1L);
		} else if (TypeOfDetail.Annex1m.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex1m;
			annex = TypeOfDetail.Annex1m.getKey();
			msg1 = annex1Mpart1();
			document = new Document(pdf, PageSize.A4.rotate());
			document = annex1Mpart1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXls(this.getListOfAnnex1m(), ExportPlaces.Annex1M);
		} else if (TypeOfDetail.Annex2.getCode().equals(this.getAnnex())) {
			annexSupp = TypeOfDetail.Annex2;
			annex = TypeOfDetail.Annex2.getKey();
			msg1 = annex2part1();
			document = new Document(pdf, PageSize.A4);
			document = annex2part1ForPdf(document, font);
			msg2 = part2();
			data = exporter.createXlsOnlyMessages(msg1, msg2);
		}

		input_document = new ByteArrayInputStream(data);
		// Read workbook into HSSFWorkbook
		HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
		// Read worksheet into HSSFSheet
		HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
		// To iterate over the rows
		Iterator<Row> rowIterator = my_worksheet.iterator();

		Table table = null;
		if (!TypeOfDetail.Annex1b.getCode().equals(this.getAnnex())
				&& !TypeOfDetail.Annex2.getCode().equals(this.getAnnex())) {

			List<Float> floatList = new ArrayList<>();
			for (int i = 0; i < annexSupp.getNumberOfColumns(); i++) {
				floatList.add(1f);
			}

			// table = new Table(annexSupp.getNumberOfColumns());
			float[] floatArray = new float[floatList.size()];
			int i = 0;

			for (Float f : floatList) {
				floatArray[i++] = 1f; // Or whatever default you want.
			}
			table = new Table(floatArray);
			// if(annexSupp.getNumberOfColumns()>10){
			// table.setWidthPercent(70);
			//
			// }else{
			// table.setWidthPercent(100);
			// }
			table.setWidthPercent(100);

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next(); // Fetch CELL
					switch (cell.getCellType()) {
					// Identify CELL type // you need to add more code here
					// based on
					// // your requirement / transformations
					case Cell.CELL_TYPE_STRING:
						// Push the data from Excel to PDF Cell
						table.addCell(new Paragraph(cell.getStringCellValue()).setMultipliedLeading(1f).setFontSize(10))
								.setFont(font);
						// feel free to move the code below to suit to your //
						// needs
						break;
					case Cell.CELL_TYPE_NUMERIC:
						table.addCell(new Paragraph(String.valueOf(cell.getNumericCellValue())).setMultipliedLeading(1f)
								.setFontSize(10)).setFont(font);
						break;
					}
					// next line }
				}
			}
			document.add(table);
		}

		document = this.part2ForPdf(document, font);

		document.close();
		byte[] data2 = byteOutStream.toByteArray();
		InputStream is = new ByteArrayInputStream(data2);
		FileHelper.sendFile(Utils.getString(annex) + "_" + Utils.getString("expenditureDeclarationEditAccountingPeriod")
				+ "_" + this.getDateFromString() + "_" + this.getDateToString() + ".pdf", is, data2.length);

	}

	private List<String> annex2part1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Il sottoscritto ___________________________________________, Dirigente Generale del Dipartimento Regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG V-A Italia-Tunisia dichiara:");
		msg1.add(
				"1)	che ha accertato che le spese di ciascun beneficiario siano state verificate dal controllore competente designato rispettivamente per l’Italia e per Tunisia (Art. 23 del Reg. (UE) 1299/2013); ");
		msg1.add(
				"2)	che ha accertato che gli esiti delle verifiche delle spese dei beneficiari italiani e tunisini (desk e in loco ove previsto) sono stati regolarmente caricati e validati all’interno del sistema Ulysses; nello specifico sono presenti:   ");
		msg1.add(
				"      1.	l’indicazione dell’ultima versione adottata delle procedure (manuali e relativi format) dell’AdG per i controlli di primo livello;");
		msg1.add(
				"      2.	gli estremi del certificato di convalida e del relativo verbale di controllo in loco ove previsto; ");
		msg1.add("      3.	l'ammontare della spesa controllata e della spesa ammissibile; ");
		msg1.add("      4.	numero di verifiche sul posto effettuate;");
		msg1.add("      5.	le irregolarità rilevate, le relative segnalazioni; ");
		msg1.add("      6.	le correzioni apportate; ");
		msg1.add("      7.	le azioni di follow up intraprese;");
		msg1.add("      8.	gli esiti delle azioni di follow up;");
		return msg1;
	}

	private List<String> annex1Mpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare trasferimenti di risorse ai beneficiari sono rese disponibili sul sistema Ulysses:");
		return msg1;
	}

	private List<String> annex1Lpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare pagamenti riferiti ad anticipazioni e delle relative coperture con spese sostenute dai beneficiari:");
		return msg1;
	}

	private List<String> annex1Hpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare importi irrecuperabili sono rese disponibili sul sistema Caronte:");
		return msg1;
	}

	private List<String> annex1Gpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare recuperi pendenti, sono rese disponibili sul sistema Caronte:");
		return msg1;
	}

	private List<String> annex1Fpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare recuperi, sono rese disponibili sul sistema Caronte:");
		return msg1;
	}

	private List<String> annex1Epart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare ritiri temporanei, sono rese disponibili sul sistema Caronte:");
		return msg1;
	}

	private List<String> annex1Dpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare ritiri, sono rese disponibili sul sistema Ulysses ");
		return msg1;
	}

	private List<String> annex1Cpart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add("Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare ");
		msg1.add("avanzamento della spesa ai fini della certificazione, sono rese disponibili sul sistema Ulysses:");
		return msg1;
	}

	private Document annex1Cpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph("Le informazioni di seguito indicate in relazione alle operazioni che hanno "
				+ "fatto registrare avanzamento della spesa ai fini della certificazione, sono rese disponibili sul sistema Ulysses:")
						.setFont(font));
		return document;
	}

	private Document annex1Bpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Il sottoscritto ___________________________________________, Dirigente Generale del Dipartimento regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG V-A Italia-Tunisia dichiara che:")
						.setFont(font));
		com.itextpdf.layout.element.List list1 = new com.itextpdf.layout.element.List(ListNumberingType.DECIMAL)
				.setSymbolIndent(12).setFont(font).setTextAlignment(TextAlignment.JUSTIFIED)

				.add("le spese dichiarate sono corrette e conformi alle disposizioni del Regolamento (UE) n. "
						+ "1303/2013 e del Reg. delegato 481/2013 e sono state sostenute in relazione ad operazioni"
						+ "selezionate per il finanziamento, conformemente ai criteri applicabili al programma operativo "
						+ "e alle norme comunitarie e nazionali, in particolare: ");

		com.itextpdf.layout.element.List list2 = new com.itextpdf.layout.element.List();
		list2.setSymbolIndent(12);
		list2.setListSymbol("\u2022");
		list2.setFont(font);

		list2.add("le norme sugli aiuti di Stato;");
		list2.add("le norme in materia di appalti pubblici;");// );
		list2.add(
				"le disposizioni in materia di pari opportunità e non discriminazione (art. 7)  Regolamento (UE) n 1303/2013);");
		list2.add("le norme in materia di sviluppo sostenibile (art. 8 Regolamento (UE) n 1303/2013);");
		ListItem subItem2 = new ListItem().setListSymbol("");
		subItem2.add(list2);
		list1.add(subItem2);
		com.itextpdf.layout.element.List list3 = new com.itextpdf.layout.element.List(ListNumberingType.DECIMAL)
				.setSymbolIndent(12)

				.setFont(font).setTextAlignment(TextAlignment.JUSTIFIED).setItemStartIndex(2);

		list3.add("le regole sulla giustificazione di anticipi è stata effettuata nel "
				+ "rispetto del par. 5 dell’art. 131 del RDC nel quadro degli aiuti di Stato ai sensi dell’articolo 107 del TFUE;")
				.add("la spesa sostenuta è corretta, proviene da sistemi di contabilità affidabili, è basata su"
						+ " documenti giustificativi verificabili ed è stata oggetto di controllo di I livello (Reg. 1303/2013 art. 125 par. 4);")

				.add("di garantire che i beneficiari coinvolti nell’attuazione di operazioni rimborsate sulla base "
						+ "dei costi ammissibili effettivamente sostenuti mantengono un sistema di contabilità "
						+ "separata o una codificazione contabile adeguata per tutte le transazioni relative alle "
						+ "operazioni secondo quanto previsto dal Reg. 1303/2013 art. 125 par. 4  lett. b;")

				.add("le spese dichiarate sono relative alle categorie ammissibili stabilite dalla normativa "
						+ "comunitaria e nazionale, dal programma di cooperazione INTERREG V-A Italia-Tunisia, "
						+ "dall’avviso pubblico per la selezione delle operazioni e dal contratto di sovvenzione;")

				.add("il contributo erogato per singola operazione è stato correttamente determinato rispetto a "
						+ "quanto stabilito nel bando o avviso pubblico, nel contratto o nella convenzione di "
						+ "riferimento, nonché rispetto all’importo di spesa rendicontata e considerata ammissibile;")

				.add("gli importi dichiarati si riferiscono a spese effettivamente realizzate durante il periodo di "
						+ "eleggibilità stabilito nella Decisione, nell’avviso pubblico e nel contratto di sovvenzione;")

				.add("le spese attestate non risultano rendicontate in altri Programmi comunitari o altre forme di "
						+ "sostegno pubblico;")
				.add("i prodotti e i servizi cofinanziati sono stati forniti;")
				.add(" beneficiari hanno ricevuto l’importo del contributo pubblico nel rispetto dell’art.132 del "
						+ "Reg.(CE) 1303/2013;")

				.add("gli importi dichiarati non contengono spese ineleggibili, inammissibili, o conseguenti ad "
						+ "operazioni sospese, revocate, irregolari, anche per frodi ed errori materiali;")
				.add("le informazioni sulle operazioni da tenere a disposizione per qualunque tipo di controllo, "
						+ "su richiesta, comprovanti le attività svolte, le spese sostenute e le verifiche "
						+ "amministrative effettuate, sono correttamente conservate - anche elettronicamente - sotto "
						+ "forma di originali o di copie autenticate su supporti comunemente accettati che si "
						+ "trovano presso gli uffici indicati nella pista di controllo, custoditi dal responsabile del "
						+ "procedimento (art. 125 par. 4 lett.d Regolamento (UE) n 1303/2013 e art. 25 "
						+ "Regolamento delegato (UE) n. 480/2014);")

				.add("i documenti comprovanti le attività svolte, le spese sostenute e le verifiche "
						+ "amministrative effettuate sono resi disponibili per almeno i due anni successivi alla "
						+ "presentazione dei conti (fatte salve le deroghe della normativa comunitaria e i termini più "
						+ "ampi previsti dalla normativa nazionale o da disposizioni specifiche dell’Autorità di "
						+ "Gestione) (art. 140 Regolamento (UE) n 1303/2013);")

				.add("l’attestazione delle spese tiene conto degli obblighi connessi alla gestione di eventuali "
						+ "irregolarità, di eventuali importi ritirati, importi recuperati e importi sospesi, e  di "
						+ "dall’Autorità di Gestione o dei controlli svolti dall’Autorità di Audit, dalla Commissione "
						+ "Europea o dalla Corte dei Conti Europea/Nazionale/Regionale, secondo le indicazioni "
						+ "del Sistema di Gestione e Controllo del Programma. (art. 122 Regolamento (UE) n "
						+ "1303/2013).")

				.add("sono stati rispettati gli obblighi in materia di informazione e pubblicità (cfr. art. 116 "
						+ "Regolamento(UE) n 1303/2013);");

		ListItem subItem1 = new ListItem();
		subItem1.add(list1);

		ListItem subItem3 = new ListItem();
		subItem3.add(list3);
		com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
		list.setListSymbol("");
		list.add(subItem1);
		list.add(subItem3);
		document.add(list);
		return document;
	}

	private List<String> annex1Bpart1() {
		List<String> msg1 = new ArrayList<>();

		msg1.add(
				"Il sottoscritto ___________________________________________, Dirigente Generale del Dipartimento regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG V-A Italia-Tunisia dichiara che:");
		msg1.add(" 1)	le spese dichiarate sono corrette e conformi alle disposizioni del Regolamento (UE) n.");
		msg1.add("      1303/2013 e del Reg. delegato 481/2013 e sono state sostenute in relazione ad operazioni");
		msg1.add("      selezionate per il finanziamento, conformemente ai criteri applicabili al programma ");
		msg1.add("      operativo e alle norme comunitarie e nazionali, in particolare: ");
		msg1.add("      •	le norme sugli aiuti di Stato; ");
		msg1.add("      •	le norme in materia di appalti pubblici;");
		msg1.add("      •	le disposizioni in materia di pari opportunità e non discriminazione (art. 7");
		msg1.add("          Regolamento (UE) n 1303/2013);");
		msg1.add("      •	le norme in materia di sviluppo sostenibile (art. 8 Regolamento (UE) n 1303/2013);");
		msg1.add(
				" 2)	le regole sulla giustificazione di anticipi è stata effettuata nel rispetto del par. 5 dell’art. ");
		msg1.add("      131 del RDC nel quadro degli aiuti di Stato ai sensi dell’articolo 107 del TFUE;");
		msg1.add(" 3)	la spesa sostenuta è corretta, proviene da sistemi di contabilità affidabili, è basata su ");
		msg1.add("      documenti giustificativi verificabili ed è stata oggetto di controllo di I livello (Reg. ");
		msg1.add("     1303/2013 art. 125 par. 4);");
		msg1.add(" 4)	di garantire che i beneficiari coinvolti nell’attuazione di operazioni rimborsate sulla base ");
		msg1.add("      dei costi ammissibili effettivamente sostenuti mantengono un sistema di contabilità ");
		msg1.add("      separata o una codificazione contabile adeguata per tutte le transazioni relative alle ");
		msg1.add("      operazioni secondo quanto previsto dal Reg. 1303/2013 art. 125 par. 4  lett. b;");
		msg1.add(" 5)	le spese dichiarate sono relative alle categorie ammissibili stabilite dalla normativa ");
		msg1.add("      comunitaria e nazionale, dal programma di cooperazione INTERREG V-A Italia-Tunisia, ");
		msg1.add("      dall’avviso pubblico per la selezione delle operazioni e dal contratto di sovvenzione;");
		msg1.add(" 6)	il contributo erogato per singola operazione è stato correttamente determinato rispetto a ");
		msg1.add("      quanto stabilito nel bando o avviso pubblico, nel contratto o nella convenzione di ");
		msg1.add("      riferimento, nonché rispetto all’importo di spesa rendicontata e considerata ammissibile;");
		msg1.add(
				" 7)	gli importi dichiarati si riferiscono a spese effettivamente realizzate durante il periodo di ");
		msg1.add("      eleggibilità stabilito nella Decisione, nell’avviso pubblico e nel contratto di sovvenzione;");
		msg1.add(" 8)	le spese attestate non risultano rendicontate in altri Programmi comunitari o altre forme di ");
		msg1.add("      sostegno pubblico;");
		msg1.add(" 9)	i prodotti e i servizi cofinanziati sono stati forniti;");
		msg1.add(" 10)	i beneficiari hanno ricevuto l’importo del contributo pubblico nel rispetto dell’art.132 del");
		msg1.add("       Reg.(CE) 1303/2013;");
		msg1.add(" 11)	gli importi dichiarati non contengono spese ineleggibili, inammissibili, o conseguenti ad ");
		msg1.add("      operazioni sospese, revocate, irregolari, anche per frodi ed errori materiali;");
		msg1.add(" 12)	le informazioni sulle operazioni da tenere a disposizione per qualunque tipo di controllo, ");
		msg1.add("      su richiesta, comprovanti le attività svolte, le spese sostenute e le verifiche ");
		msg1.add("      amministrative effettuate, sono correttamente conservate - anche elettronicamente - sotto ");
		msg1.add("      forma di originali o di copie autenticate su supporti comunemente accettati che si ");
		msg1.add("      trovano presso gli uffici indicati nella pista di controllo, custoditi dal responsabile del ");
		msg1.add("      procedimento (art. 125 par. 4 lett.d Regolamento (UE) n 1303/2013 e art. 25 ");
		msg1.add("      Regolamento delegato (UE) n. 480/2014);");
		msg1.add(" 13)	i documenti comprovanti le attività svolte, le spese sostenute e le verifiche ");
		msg1.add("      amministrative effettuate sono resi disponibili per almeno i due anni successivi alla ");
		msg1.add("      presentazione dei conti (fatte salve le deroghe della normativa comunitaria e i termini più ");
		msg1.add("      ampi previsti dalla normativa nazionale o da disposizioni specifiche dell’Autorità di ");
		msg1.add("      Gestione) (art. 140 Regolamento (UE) n 1303/2013);");
		msg1.add(" 14)	l’attestazione delle spese tiene conto degli obblighi connessi alla gestione di eventuali ");
		msg1.add("      irregolarità, di eventuali importi ritirati, importi recuperati e importi sospesi, e  di ");
		msg1.add("      eventuali interessi ricevuti (interessi di mora), in seguito alle verifiche effettuate ");
		msg1.add("      dall’Autorità di Gestione o dei controlli svolti dall’Autorità di Audit, dalla Commissione ");
		msg1.add("      Europea o dalla Corte dei Conti Europea/Nazionale/Regionale, secondo le indicazioni ");
		msg1.add("      del Sistema di Gestione e Controllo del Programma. (art. 122 Regolamento (UE) n ");
		msg1.add("      1303/2013).");
		msg1.add(" 15)	sono stati rispettati gli obblighi in materia di informazione e pubblicità (cfr. art. 116 ");
		msg1.add("      Regolamento(UE) n 1303/2013);");
		return msg1;
	}

	private Document annex2part1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Il sottoscritto ___________________________________________, Dirigente Generale del Dipartimento Regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG V-A Italia-Tunisia dichiara:")
						.setFont(font));
		com.itextpdf.layout.element.List list1 = new com.itextpdf.layout.element.List(ListNumberingType.DECIMAL);
		list1.setPostSymbolText(") ");
		list1.setSymbolIndent(12).setFont(font).setTextAlignment(TextAlignment.JUSTIFIED)

				.add("che ha accertato che le spese di ciascun beneficiario siano state verificate dal "
						+ "controllore competente designato rispettivamente per l’Italia e per Tunisia (Art. 23 del Reg. (UE) 1299/2013); ")
				.add("che ha accertato che gli esiti delle verifiche delle spese dei beneficiari italiani e tunisini (desk e in loco ove previsto) sono stati regolarmente caricati e validati all’interno del sistema Ulysses; nello specifico sono presenti:   ");

		com.itextpdf.layout.element.List list2 = new com.itextpdf.layout.element.List();
		list2.setSymbolIndent(12);
		// list2.setListSymbol("\u2022");
		list2.setListSymbol(ListNumberingType.DECIMAL);

		list2.setFont(font);

		list2.add(
				"l’indicazione dell’ultima versione adottata delle procedure (manuali e relativi format) dell’AdG per i controlli di primo livello;");
		list2.add("gli estremi del certificato di convalida e del relativo verbale di controllo in loco ove previsto;");// );
		list2.add("l'ammontare della spesa controllata e della spesa ammissibile;");
		list2.add("numero di verifiche sul posto effettuate;");
		list2.add("le irregolarità rilevate, le relative segnalazioni;");
		list2.add("le correzioni apportate;");
		list2.add("le azioni di follow up intraprese;");
		list2.add("gli esiti delle azioni di follow up;");
		ListItem subItem2 = new ListItem().setListSymbol("");
		subItem2.add(list2);
		list1.add(subItem2);
		// com.itextpdf.layout.element.List list3 = new
		// com.itextpdf.layout.element.List(ListNumberingType.DECIMAL)
		// .setSymbolIndent(12)
		//
		// .setFont(font).setTextAlignment(TextAlignment.JUSTIFIED).setItemStartIndex(2);
		//
		// list3.add("le regole sulla giustificazione di anticipi è stata
		// effettuata nel "
		// + "rispetto del par. 5 dell’art. 131 del RDC nel quadro degli aiuti
		// di Stato ai sensi dell’articolo 107 del TFUE;")
		// .add("la spesa sostenuta è corretta, proviene da sistemi di
		// contabilità affidabili, è basata su"
		// + " documenti giustificativi verificabili ed è stata oggetto di
		// controllo di I livello (Reg. 1303/2013 art. 125 par. 4);")
		//
		// .add("di garantire che i beneficiari coinvolti nell’attuazione di
		// operazioni rimborsate sulla base "
		// + "dei costi ammissibili effettivamente sostenuti mantengono un
		// sistema di contabilità "
		// + "separata o una codificazione contabile adeguata per tutte le
		// transazioni relative alle "
		// + "operazioni secondo quanto previsto dal Reg. 1303/2013 art. 125
		// par. 4 lett. b;")
		//
		// .add("le spese dichiarate sono relative alle categorie ammissibili
		// stabilite dalla normativa "
		// + "comunitaria e nazionale, dal programma di cooperazione INTERREG
		// V-A Italia-Tunisia, "
		// + "dall’avviso pubblico per la selezione delle operazioni e dal
		// contratto di sovvenzione;")
		//
		// .add("il contributo erogato per singola operazione è stato
		// correttamente determinato rispetto a "
		// + "quanto stabilito nel bando o avviso pubblico, nel contratto o
		// nella convenzione di "
		// + "riferimento, nonché rispetto all’importo di spesa rendicontata e
		// considerata ammissibile;")
		//
		// .add("gli importi dichiarati si riferiscono a spese effettivamente
		// realizzate durante il periodo di "
		// + "eleggibilità stabilito nella Decisione, nell’avviso pubblico e nel
		// contratto di sovvenzione;")
		//
		// .add("le spese attestate non risultano rendicontate in altri
		// Programmi comunitari o altre forme di "
		// + "sostegno pubblico;")
		// .add("i prodotti e i servizi cofinanziati sono stati forniti;")
		// .add(" beneficiari hanno ricevuto l’importo del contributo pubblico
		// nel rispetto dell’art.132 del "
		// + "Reg.(CE) 1303/2013;")
		//
		// .add("gli importi dichiarati non contengono spese ineleggibili,
		// inammissibili, o conseguenti ad "
		// + "operazioni sospese, revocate, irregolari, anche per frodi ed
		// errori materiali;")
		// .add("le informazioni sulle operazioni da tenere a disposizione per
		// qualunque tipo di controllo, "
		// + "su richiesta, comprovanti le attività svolte, le spese sostenute e
		// le verifiche "
		// + "amministrative effettuate, sono correttamente conservate - anche
		// elettronicamente - sotto "
		// + "forma di originali o di copie autenticate su supporti comunemente
		// accettati che si "
		// + "trovano presso gli uffici indicati nella pista di controllo,
		// custoditi dal responsabile del "
		// + "procedimento (art. 125 par. 4 lett.d Regolamento (UE) n 1303/2013
		// e art. 25 "
		// + "Regolamento delegato (UE) n. 480/2014);")
		//
		// .add("i documenti comprovanti le attività svolte, le spese sostenute
		// e le verifiche "
		// + "amministrative effettuate sono resi disponibili per almeno i due
		// anni successivi alla "
		// + "presentazione dei conti (fatte salve le deroghe della normativa
		// comunitaria e i termini più "
		// + "ampi previsti dalla normativa nazionale o da disposizioni
		// specifiche dell’Autorità di "
		// + "Gestione) (art. 140 Regolamento (UE) n 1303/2013);")
		//
		// .add("l’attestazione delle spese tiene conto degli obblighi connessi
		// alla gestione di eventuali "
		// + "irregolarità, di eventuali importi ritirati, importi recuperati e
		// importi sospesi, e di "
		// + "dall’Autorità di Gestione o dei controlli svolti dall’Autorità di
		// Audit, dalla Commissione "
		// + "Europea o dalla Corte dei Conti Europea/Nazionale/Regionale,
		// secondo le indicazioni "
		// + "del Sistema di Gestione e Controllo del Programma. (art. 122
		// Regolamento (UE) n "
		// + "1303/2013).")
		//
		// .add("sono stati rispettati gli obblighi in materia di informazione e
		// pubblicità (cfr. art. 116 "
		// + "Regolamento(UE) n 1303/2013);");

		ListItem subItem1 = new ListItem();
		subItem1.add(list1);

		// ListItem subItem3 = new ListItem();
		// subItem3.add(list3);
		com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
		list.setListSymbol("");
		list.add(subItem1);
		// list.add(subItem3);
		document.add(list);
		return document;
	}

	private Document annex1Mpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare trasferimenti di risorse ai beneficiari sono rese disponibili sul sistema Ulysses:")
						.setFont(font));
		return document;
	}

	private Document annex1Lpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare pagamenti riferiti ad anticipazioni e delle relative coperture con spese sostenute dai beneficiari:")
						.setFont(font));
		return document;
	}

	private Document annex1Hpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare importi irrecuperabili sono rese disponibili sul sistema Caronte:")
						.setFont(font));
		return document;
	}

	private Document annex1Gpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare recuperi pendenti, sono rese disponibili sul sistema Caronte:")
						.setFont(font));
		return document;
	}

	private Document annex1Fpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare recuperi, sono rese disponibili sul sistema Caronte:")
						.setFont(font));
		return document;
	}

	private Document annex1Epart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare ritiri temporanei, sono rese disponibili sul sistema Caronte:")
						.setFont(font));
		return document;
	}

	private Document annex1Dpart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Le informazioni di seguito indicate in relazione alle operazioni che hanno fatto registrare ritiri, sono rese disponibili sul sistema Ulysses:")
						.setFont(font));
		return document;
	}

	private Document annex1Apart1ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph(
				"Il sottoscritto ______________________________________, Dirigente Generale del Dipartimento "
						+ "regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG "
						+ "V-A Italia-Tunisia dichiara che le spese comprese nel rendiconto allegato si riferiscono a "
						+ "spese che rispondono ai criteri di ammissibilità stabiliti dall’art. 65 del Regolamento (UE)"
						+ " n. 1303/2013 e sono state sostenute per l’attuazione delle operazioni selezionate nel quadro"
						+ " del programma di ooperazione INTERREG V-A Italia-Tunisia, durante il periodo di ammissibilità "
						+ "stabilito nella Decisione di approvazione del programma, ovvero dopo il 1 gennaio 2014.")
								.setFont(font));
		document.add(
				new Paragraph("Tali spese, come risultanti dal sistema Ulysses del Programma, relative al periodo dal "
						+ "____/____/_________al ____/____/_________, sono pari a:_______________________")
								.setFont(font));
		return document;
	}

	private List<String> annex1Apart1() {
		List<String> msg1 = new ArrayList<>();
		msg1.add("Il sottoscritto ______________________________________, Dirigente Generale del Dipartimento");
		msg1.add("regionale della Programmazione, nella qualità di AdG del programma di cooperazione INTERREG");
		msg1.add("V-A Italia-Tunisia dichiara che le spese comprese nel rendiconto allegato si riferiscono a spese che ");
		msg1.add("rispondono ai criteri di ammissibilità stabiliti dall’art. 65 del Regolamento (UE) n. 1303/2013 e");
		msg1.add("sono state sostenute per l’attuazione delle operazioni selezionate nel quadro del programma di ");
		msg1.add("cooperazione INTERREG V-A Italia-Tunisia, durante il periodo di ammissibilità stabilito nella");
		msg1.add("Decisione di approvazione del programma, ovvero dopo il 1 gennaio 2014.");
		msg1.add("Tali spese, come risultanti dal sistema Ulysses del Programma, relative al periodo dal");
		msg1.add("____/__/_____al __/__/_____, sono pari a:_______________________");
		return msg1;
	}

	private Document part2ForPdf(Document document, PdfFont font) {
		document.add(new Paragraph("Data ____/____/_________").setFont(font));
		document.add(new Paragraph("").setFont(font));
		document.add(new Paragraph("(timbro e  firma)").setFont(font).setTextAlignment(TextAlignment.RIGHT));
		return document;
	}

	private List<String> part2() {
		List<String> msg2 = new ArrayList<>();
		msg2.add("Data __/__/_____");
		msg2.add("                                                                            (timbro e  firma)");
		msg2.add("                                                                                              ");
		msg2.add("                                                                           ____________________");
		return msg2;
	}

	private double expenditureIncreaseComputation() {
		double sum = 0d;
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				sum += dcw.getSumAmoutCertification();
			}
		}
		return sum;
	}

	private double totalExpenditureComputation()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		double sum = expenditureIncreaseComputation();
		List<ExpenditureDeclaration> eds = BeansFactory.ExpenditureDeclarations().LoadAll();
		List<DurCompilations> dcList = new ArrayList<>();
		for (ExpenditureDeclaration ed : eds) {
			if (ed.getExpenditureDeclarationState().getCode()
					.equals(ExpenditureDeclarationStatusTypes.Submitted.getValue())) {

				List<ExpenditureDeclarationDurCompilations> eddcs = BeansFactory.ExpenditureDeclarationDurCompilations()
						.getAllByExpenditureDeclaratios(ed.getId());

				for (ExpenditureDeclarationDurCompilations eddc : eddcs) {
					dcList.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
				}
			}
		}
		for (DurCompilations dc : dcList) {
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);
			sum += dcw.getSumAmoutCertification();
		}
		return sum;
	}

	private void fillFields() throws HibernateException, NumberFormatException, PersistenceBeanException {
		DurCompilations dc = null;
		CFPartnerAnagraphs ca = null;
		GeneralBudgets gb = null;
		double totalAmountEligibleExpenditure = 0d;
		double totAmountPublicExpenditure = 0d;
		double totAmountPrivateExpenditure = 0d;
		double totAmountStateAid = 0d;
		double totAmountCoverageAdvanceAid = 0d;
		double totContributionInKindPubShare = 0d;
		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				dc = BeansFactory.DurCompilations().Load(dcw.getId());
				for (CostDefinitions cd : dc.getCostDefinitions()) {
					ca = BeansFactory.CFPartnerAnagraphs().GetByUser(cd.getUser().getId());
					if (ca.getPublicSubject() != null) {
						totalAmountEligibleExpenditure += cd.getAguCertification();// this.getValidateAmount(cd);
					}

					List<GeneralBudgets> gbs = BeansFactory.GeneralBudgets()
							.GetItemsForCFAnagraph(String.valueOf(cd.getProject().getId()), ca.getId());
					gb = gbs.get(gbs.size() - 1);
					if (ca.getPublicSubject() != null && !ca.getPublicSubject()) {
						if (gb != null
								&& (((gb.getFesr() / (gb.getBudgetTotal() - gb.getQuotaPrivate() - gb.getNetRevenue()))
										* 100) == 50)) {
							totAmountPublicExpenditure += ((cd.getAguCertification()
									- cd.getAguCheckAdditionalFinansingAmount()) * 0.5d);// (this.getCostSubAdditionalFinansing(cd)
																							// *
																							// 0.5);
							totAmountPrivateExpenditure += (cd.getAguCertification() * 0.5d);// (this.getValidateAmount(cd)*
																								// 0.5d);//(this.getCostSubAdditionalFinansing(cd)
																								// *
																								// 0.5);
						} else {
							totAmountPublicExpenditure += ((cd.getAguCertification()
									- cd.getAguCheckAdditionalFinansingAmount()) * 0.85d);// (this.getCostSubAdditionalFinansing(cd)
																							// *
																							// 0.85);
							totAmountPrivateExpenditure += (cd.getAguCertification() * 0.15d);// (this.getValidateAmount(cd)*
																								// 0.15d);//(this.getCostSubAdditionalFinansing(cd)
																								// *
																								// 0.15);
						}

					} else if (ca.getPublicSubject() != null && ca.getPublicSubject()) {
						totAmountPublicExpenditure += cd.getAguCertification();// this.getValidateAmount(cd);//this.getCostSubAdditionalFinansing(cd);
					}
					if (cd.getStateAid()) {
						totAmountStateAid += (cd.getAguCertification()
								- (cd.getAguCheckStateAidAmount() != null ? cd.getAguCheckStateAidAmount() : 0d));
						totAmountCoverageAdvanceAid += (cd.getAguCheckStateAidAmount() != null
								? cd.getAguCheckStateAidAmount() : 0d);
					}
					totContributionInKindPubShare += (cd.getAguCheckInkindContributions() != null
							? cd.getAguCheckInkindContributions() : 0d);
				}
			}
		}
		this.setAccountingPeriod();
		this.setTotAmountEligibleExpenditure(totalAmountEligibleExpenditure);
		this.setTotAmountPublicExpenditure(totAmountPublicExpenditure);
		this.setTotAmountPrivateExpenditure(totAmountPrivateExpenditure);
		this.setTotAmountStateAid(totAmountStateAid);
		this.setTotAmountCoverageAdvanceAid(totAmountCoverageAdvanceAid);
		this.setTotContributionInKindPubShare(totContributionInKindPubShare);
	}

	private void setAccountingPeriod() {
		Calendar now = new GregorianCalendar();
		Calendar june30 = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JUNE, 30);
		Calendar from = null, to = null;
		if (now.after(june30)) {
			from = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JULY, 1);
			to = new GregorianCalendar(now.get(Calendar.YEAR) + 1, Calendar.JUNE, 30);
		} else {
			from = new GregorianCalendar(now.get(Calendar.YEAR) - 1, Calendar.JULY, 1);
			to = new GregorianCalendar(now.get(Calendar.YEAR), Calendar.JUNE, 30);
		}
		this.setDateFrom(from.getTime());
		this.setDateTo(to.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		this.setDateFromString(df.format(this.getDateFrom()));
		this.setDateToString(df.format(this.getDateTo()));

	}

	public void showItem() throws HibernateException, PersistenceBeanException {
		CostDefinitions cd = BeansFactory.CostDefinitions()
				.getFirstByDurCompilationWithoutProject(this.getEntityEditId());

		this.getSession().put("costCertProjectId", cd.getProject().getId());
		this.getSession().put("project", cd.getProject().getId());

		DurCompilations dc = BeansFactory.DurCompilations().Load(this.getEntityEditId());
		if (dc != null && !dc.getDeleted()) {
			Boolean canEdit = false;
			for (DURCompilationWrapper dr : this.getList()) {
				if (dr.getId().equals(this.getEntityEditId())) {
					canEdit = dr.isEditAvailable();
					break;
				}
			}
			this.getSession().put(DURCompilationEditBean.DUR_EDIT_FROM_VIEW, false);
			this.getSession().put(DURCompilationEditBean.FILTER_PARTNER_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_PHASES_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_COST_DEF_ID_VALUE, null);
			this.getSession().put(DURCompilationEditBean.FILTER_COST_ITEM, null);
			this.getSession().put(DURCompilationEditBean.CAN_EDIT_FROM_VIEW, canEdit);

			this.getSession().put("durCompilationEdit", this.getEntityEditId());
			this.getSession().put("durCompilationEditShow", true);
			this.getSession().put("durCompilationEditBack", true);
			this.getSession().put("expenditureDeclaration", true);
			this.goTo(PagesTypes.DURCOMPILATIONEDIT);
		}
	}

	public void temporarySave() {
		this.setSubmitted(false);
		getSelectedDur();
		generateStatus();
		this.Page_Save();
	}

	public void showDetailsFunction() {
		this.setShowDetails(true);
	}

	public void avoid() {
		this.setShowDetails(false);
	}

	public void definitiveSave() {
		getSelectedDur();
		if (validateFieldsToGenerateStatus()) {
			return;
		}
		this.Page_Save();
	}

	private List<ExpenditureDeclarationAccountingPeriod> setFieldsIntoOtherExpenditure()
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAccountingPeriod> listOldEDAP = null;

		List<BigInteger> listOfExpenditureDeclarationId = BeansFactory.ExpenditureDeclarationAccountingPeriod()
				.getExpenditureDeclarationIdDistinct(this.getDateFrom(), this.getDateTo());
		if (listOfExpenditureDeclarationId != null) {
			for (BigInteger id : listOfExpenditureDeclarationId) {
				if (!id.equals((Long) this.getSession().get("expanditureDeclarationEntityId"))) {
					listOldEDAP = BeansFactory.ExpenditureDeclarationAccountingPeriod()
							.getAllByExpenditureDeclaration(id.longValue());
					for (ExpenditureDeclarationAccountingPeriod edw : listOldEDAP) {
						for (ExpenditureDeclarationAccountingPeriod edwNow : this.getListExpDeclWrapper()) {
							if (edwNow.getSpecificObjective() != null
									&& edw.getSpecificObjective().equals(edwNow.getSpecificObjective())) {
								// colonna a
								edw.setAmountOfPastExDecl(
										edw.getAmountOfPastExDecl() + edwNow.getAmountOfExDeclForAccountingPeriod());
								edw.setCumulativeAmount(
										edw.getCumulativeAmount() + edwNow.getAmountOfExDeclForAccountingPeriod());

							}
						}
					}
				}

			}
		}
		return listOldEDAP;
	}

	private void getSelectedDur() {
		listSelectedDur = new ArrayList<>();

		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelected()) {
				DurCompilations dc;
				try {
					dc = BeansFactory.DurCompilations().Load(dcw.getId());
					listSelectedDur.add(dc);
				} catch (HibernateException | PersistenceBeanException e) {
					log.error("db: retrive error " + e);
				}

			}
		}
	}

	private void generateStatus() {
		ExpenditureDeclarationStates eds = null;
		try {
			boolean statusValidated = true;
			if (this.getProtocolNumber() == null || this.getProtocolNumber().isEmpty()
					|| this.getProtocolNumber().trim().isEmpty()) {
				statusValidated = false;
			}
			if (this.getType() == null || this.getType().isEmpty() || this.getType().trim().isEmpty()
					|| this.getType().equals("0")) {
				statusValidated = false;
			}

			if (this.getTotalExpenditure() == null || this.getTotalExpenditure() == 0) {
				statusValidated = false;
			}
			if (listSelectedDur.size() <= 0) {
				statusValidated = false;
			}
			if (statusValidated) {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Generated.getValue());
			} else {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Compiled.getValue());
			}
		} catch (PersistenceBeanException e) {
			log.error("db error");
		}
		setState_id(eds.getId());
	}

	private boolean validateFieldsToGenerateStatus() {
		this.validationFailed = false;
		checkEmpty("EditForm:tab1:protocolNumber", this.getProtocolNumber());
		checkEmpty("EditForm:tab1:totalExpenditure", this.getTotalExpenditure());
		checkSelectedList("EditForm:tab1:type", this.getType());
		checkProtocolDateEmpty("EditForm:tab1:createFrom2", this.getProtocolDate());

		if (listSelectedDur.size() <= 0) {
			this.validationFailed = true;
		}

		if (this.validationFailed) {
			setError(Utils.getString("Compila i campi obbligatori"));
		} else {
			ExpenditureDeclarationStates eds = null;
			try {
				eds = BeansFactory.ExpenditureDeclarationStates()
						.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());
			} catch (PersistenceBeanException e) {
				// TODO Auto-generated catch block
				log.error("db error");
			}
			setState_id(eds.getId());
		}
		return this.validationFailed;
	}

	private boolean checkSelectedList(String id, Object value) {
		if (value.equals("0")) {
			this.validationFailed = true;
			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
		}
		return true;
	}

	private boolean checkEmpty(String id, Object value) {
		String str = null;

		if (value != null) {
			try {
				str = String.valueOf(value);
			} catch (Exception e) {
			}
		}

		if (str == null || str.isEmpty() || str.trim().isEmpty()) {
			this.validationFailed = true;

			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}

	private boolean checkProtocolDateEmpty(String id, Object value) {

		if (value == null) {
			this.validationFailed = true;

			ValidatorHelper.markNotVAlid(this.getContext().getViewRoot().findComponent(id),
					Utils.getString("validatorMessage"), FacesContext.getCurrentInstance(), null);
			this.getFailedValidation().add(id);
			return false;
		} else {
			if (this.getFailedValidation().contains(id)) {
				this.getFailedValidation().remove(id);
				ValidatorHelper.markVAlid(this.getContext().getViewRoot().findComponent(id),
						FacesContext.getCurrentInstance(), null);
			}
			return true;
		}

	}

	@Override
	public void Page_Load() throws NumberFormatException, HibernateException, PersistenceBeanException {
		this.setFailedValidation(new ArrayList<String>());
		
		if (Boolean.TRUE.equals(this.getSession().get("expenditureDeclarationNew"))) {
			newExpenditureDeclarationPage();
		}
		if (this.getSession().get("expanditureDeclarationEntityId") != null) {
			editExpenditureDeclarationPage();
			showExpenditureDeclarationPage();
		}
		try {
			this.initTableExpenditureDeclaration2();
			this.fillTableExpenditureDeclaration2();
			this.fillListElementAnnex1c();
			// this.fillListElementAnnex1def();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ReRenderScroll();
	}

	private void fillListElementAnnex1defghlm() throws PersistenceBeanException {
		ExpenditureDeclaration ed = this.getLastExpenditureDeclaration();
		// prendere la data di presentazione e cercare tutte le altre
		// dichiarazioni di spesa presentate con data di presentazione
		// precedenti alla data di presentazione dell'ultima dichiarazione di
		// spesa presentata
		if (ed != null && ed.getSubmissionDate() != null) {
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def = BeansFactory
					.ExpenditureDeclarationAnnex1dWrapper()
					.getAllAfterSubmissionDate(ed.getSubmissionDate(), ed.getId());

			if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1d.getCode())) {
				elementsAnnex1d(elementsAnnex1def);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1e.getCode())) {
				elementsAnnex1e(elementsAnnex1def);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1f.getCode())) {
				elementsAnnex1f(elementsAnnex1def);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1g.getCode())) {
				elementsAnnex1g(elementsAnnex1def);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1h.getCode())) {
				elementsAnnex1h(elementsAnnex1def);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1l.getCode())) {
				elementsAnnex1l(ed);
			} else if (this.getAnnex() != null && this.getAnnex().equals(TypeOfDetail.Annex1m.getCode())) {
				elementsAnnex1m(ed);
			}
		}
		// recuperare tutte le ddr della lista delle dichiarazioni di spesa
		// recuperare tutte le spese associate alle ddr
		// andare a recuperare le info necessarie e settarle nella lista di
		// elementi del allegato
	}

	private void elementsAnnex1m(ExpenditureDeclaration lastEd) throws PersistenceBeanException {
		Date dateTo = dateTo();
		Date dateFrom = lastEd.getSubmissionDate();

		Annex1mWrapper a1mw = null;
		List<Annex1mWrapper> listAnnex1mWrapper = new ArrayList<>();

		List<Projects> projects = BeansFactory.Projects().Load();
		for (Projects p : projects) {
			List<AdditionalFesrInfo> afi = BeansFactory.AdditionalFESRInfo().LoadByProject(String.valueOf(p.getId()));
			Double amountTransferred = 0d;
			for (AdditionalFesrInfo afii : afi) {
				if (afii.getCreateDate().before(dateTo) && afii.getCreateDate().after(dateFrom)) {
					amountTransferred += afii.getTransferImport();
				}
			}
			if (amountTransferred != 0d) {
				SpecificGoals sg = BeansFactory.SpecificGoals().Load(p.getSpecificGoal().getId());
				a1mw = new Annex1mWrapper(p, sg, amountTransferred);
				listAnnex1mWrapper.add(a1mw);
			}
		}

		Collections.sort(listAnnex1mWrapper);

		Double amountTransferred[] = { 0d, 0d, 0d, 0d };

		annex1mFillFields(listAnnex1mWrapper, amountTransferred);

		List<Annex1mWrapper> listSupp = new ArrayList<>();

		annex1mCompleteRows(listAnnex1mWrapper, amountTransferred, listSupp);

		this.setListOfAnnex1m(listSupp);
	}

	private Date dateTo() throws PersistenceBeanException {
		Date stop = null;
		if (this.getSession().get("expanditureDeclarationEntityId") == null) {
			stop = new GregorianCalendar().getTime();
		} else {
			Long entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			ExpenditureDeclaration ed = BeansFactory.ExpenditureDeclarations().Load(entityId);
			stop = (ed.getSubmissionDate() != null ? ed.getSubmissionDate() : (new GregorianCalendar()).getTime());
		}
		return stop;
	}

	private void elementsAnnex1l(ExpenditureDeclaration lastEd) throws PersistenceBeanException {
		List<ExpenditureDeclarationDurCompilations> listEddc = BeansFactory.ExpenditureDeclarationDurCompilations()
				.getAllByExpenditureDeclaratios(lastEd.getId());
		List<DurCompilations> listdc = new ArrayList<>();
		for (ExpenditureDeclarationDurCompilations eddci : listEddc) {
			if (!Boolean.TRUE.equals(eddci.getDeleted())) {
				listdc.add(BeansFactory.DurCompilations().Load(eddci.getDurCompilationsId()));
			}
		}
		List<CostDefinitions> listCd = new ArrayList<>();
		for (DurCompilations dc : listdc) {
			listCd.addAll(dc.getCostDefinitions());
		}

		List<Annex1lWrapper> listAnnex1lWrapper = new ArrayList<>();
		List<AdditionalFesrInfo> listAfi = null;

		for (CostDefinitions cdi : listCd) {
			if (cdi.getAdditionalFesrInfo() != null) {

				AdditionalFesrInfo afi = BeansFactory.AdditionalFESRInfo().Load(cdi.getAdditionalFesrInfo().getId());
				if (afi.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidDeMinimis.getCode())) {
				//if (afi.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidDeMinimis.getCode())
						//|| afi.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidExemptionScheme.getCode())) {
					Projects p = BeansFactory.Projects().Load(cdi.getProject().getId());
					SpecificGoals sg = BeansFactory.SpecificGoals().Load(p.getSpecificGoal().getId());

					PaymentWays pw = BeansFactory.PaymentWays().Load(cdi.getPaymentWay().getId());
					listAfi = BeansFactory.AdditionalFESRInfo().LoadByProject(String.valueOf(cdi.getProject().getId()));
					Double amountNotCoverAdvanceStateAid = 0d;
					for (AdditionalFesrInfo af : listAfi) {
						if ((af.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidDeMinimis.getCode()))
								&& af.getFesrBf().getId().equals(afi.getFesrBf().getId())) {
						//if ((af.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidDeMinimis.getCode()) || af
								//.getTransferTypeCode().equals(TransferTypes.AdvanceStateAidExemptionScheme.getCode()))
								//&& af.getFesrBf().getId().equals(afi.getFesrBf().getId())) {
							try {
								List<CostDefinitions> cds = BeansFactory.CostDefinitions()
										.LoadByAdditionalFesrInfo(af.getId());
								if (cds == null || (cds != null && cds.isEmpty())) {
									Calendar diff = new GregorianCalendar();
									diff.setTimeInMillis(af.getCreateDate().getTime() - cdi.getCreateDate().getTime());
									if (Math.abs((diff.get(Calendar.YEAR) - 1970)) < 3) {
										amountNotCoverAdvanceStateAid += af.getTransferImport();
									}

								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}

					Annex1lWrapper a1lw = new Annex1lWrapper(cdi, p, sg, afi, pw, amountNotCoverAdvanceStateAid);
					listAnnex1lWrapper.add(a1lw);
				}
			}

		}
		
		Collections.sort(listAnnex1lWrapper);

		Double amountToCoverAdvanceStateAid[] = { 0d, 0d, 0d, 0d };
		Double lastAmountToCoverAdvanceStateAid[] = { 0d, 0d, 0d, 0d };
		Double amountNotCoverAdvanceStateAid[] = { 0d, 0d, 0d, 0d };

		annex1lFillFields(listAnnex1lWrapper, amountToCoverAdvanceStateAid, lastAmountToCoverAdvanceStateAid,
				amountNotCoverAdvanceStateAid);

		List<Annex1lWrapper> listSupp = new ArrayList<>();

		annex1lCompleteRows(listAnnex1lWrapper, amountToCoverAdvanceStateAid, lastAmountToCoverAdvanceStateAid,
				amountNotCoverAdvanceStateAid, listSupp);

		this.setListOfAnnex1l(listSupp);
	}

	private void elementsAnnex1h(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1h = new ArrayList<>();

		elaborateFieldsOfAnnex1h(elementsAnnex1def, elementsAnnex1h);

		Double paymentAmount[] = { 0d, 0d, 0d, 0d };
		Double recoveryAmount[] = { 0d, 0d, 0d, 0d };

		annex1hFillFields(elementsAnnex1h, paymentAmount, recoveryAmount);

		List<ExpenditureDeclarationAnnex1dWrapper> listSupp = new ArrayList<>();

		annex1hCompleteRows(elementsAnnex1h, paymentAmount, recoveryAmount, listSupp);

		this.setListOfAnnex1h(listSupp);
	}

	private void elementsAnnex1g(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1g = new ArrayList<>();

		elaborateFieldsOfAnnex1g(elementsAnnex1def, elementsAnnex1g);

		Double paymentAmount[] = { 0d, 0d, 0d, 0d };
		Double recoveryAmount[] = { 0d, 0d, 0d, 0d };

		annex1gFillFields(elementsAnnex1g, paymentAmount, recoveryAmount);

		List<ExpenditureDeclarationAnnex1dWrapper> listSupp = new ArrayList<>();

		annex1gCompleteRows(elementsAnnex1g, paymentAmount, recoveryAmount, listSupp);

		this.setListOfAnnex1g(listSupp);
	}

	private void elementsAnnex1f(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1f = new ArrayList<>();

		elaborateFieldsOfAnnex1f(elementsAnnex1def, elementsAnnex1f);

		Double paymentAmount[] = { 0d, 0d, 0d, 0d };
		Double recoveryAmount[] = { 0d, 0d, 0d, 0d };

		annex1fFillFields(elementsAnnex1f, paymentAmount, recoveryAmount);

		List<ExpenditureDeclarationAnnex1dWrapper> listSupp = new ArrayList<>();

		annex1fCompleteRows(elementsAnnex1f, paymentAmount, recoveryAmount, listSupp);

		this.setListOfAnnex1f(listSupp);
	}

	private void elementsAnnex1e(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e = new ArrayList<>();

		elaborateFieldsOfAnnex1e(elementsAnnex1def, elementsAnnex1e);

		Double paymentAmount[] = { 0d, 0d, 0d, 0d };
		Double suspendedAmount[] = { 0d, 0d, 0d, 0d };

		annex1defFillFields(elementsAnnex1e, paymentAmount, suspendedAmount);

		List<ExpenditureDeclarationAnnex1dWrapper> listSupp = new ArrayList<>();

		annex1deCompleteRows(elementsAnnex1e, paymentAmount, suspendedAmount, listSupp);

		this.setListOfAnnex1e(listSupp);
	
	}

	private void elementsAnnex1d(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1d = new ArrayList<>();

		elaborateFieldsOfAnnex1d(elementsAnnex1def, elementsAnnex1d);

		Double paymentAmount[] = { 0d, 0d, 0d, 0d };
		Double retiredAmount[] = { 0d, 0d, 0d, 0d };

		annex1defFillFields(elementsAnnex1d, paymentAmount, retiredAmount);

		List<ExpenditureDeclarationAnnex1dWrapper> listSupp = new ArrayList<>();

		annex1deCompleteRows(elementsAnnex1d, paymentAmount, retiredAmount, listSupp);

		this.setListOfAnnex1d(listSupp);
	}

	private void elaborateFieldsOfAnnex1h(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def,
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1h) throws PersistenceBeanException {
		DateTime.setDatePattern("dd/MM/yyyy");
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1def) {
			if (Boolean.TRUE.equals(eda1di.getRecoverededByAGUACU()) && eda1di.getDurNotRegularIsRecoverable() != null
					&& Boolean.FALSE.equals(eda1di.getDurNotRegularIsRecoverable())) {
				eda1di.setDocumentType(BeansFactory.PaymentWays().Load(eda1di.getDocumentTypeId()).getValue());
				eda1di.setRecoveredActExtreme(String.valueOf(eda1di.getDurNotRegularActNumber()));
				for (RecoverReasonType type : RecoverReasonType.values()) {
					if (type.getId().equals(eda1di.getSelctedDurRecoveryReason())) {
						eda1di.setMotivation(type.toString());
					}
				}
				elementsAnnex1h.add(eda1di);
			}
		}
	}

	private void elaborateFieldsOfAnnex1g(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def,
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1g) throws PersistenceBeanException {
		DateTime.setDatePattern("dd/MM/yyyy");
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1def) {
			if (Boolean.TRUE.equals(eda1di.getRecoverededByAGUACU()) && eda1di.getDurNotRegularActNumber() == null && eda1di.getDurNotRegularIsRecoverable() == null) {
				eda1di.setDocumentType(BeansFactory.PaymentWays().Load(eda1di.getDocumentTypeId()).getValue());
				eda1di.setRecoveredActExtreme(eda1di.getDurRecoveryActNumber());
				for (RecoverReasonType type : RecoverReasonType.values()) {
					if (type.getId().equals(eda1di.getSelctedDurRecoveryReason())) {
						eda1di.setMotivation(type.toString());
					}
				}
				elementsAnnex1g.add(eda1di);

			}
		}
	}

	private void elaborateFieldsOfAnnex1f(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def,
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1f) throws PersistenceBeanException {
		DateTime.setDatePattern("dd/MM/yyyy");
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1def) {
			if (Boolean.TRUE.equals(eda1di.getRecoverededByAGUACU())
					&& Boolean.TRUE.equals(eda1di.getDurNotRegularIsRecoverable())) {
				eda1di.setDocumentType(BeansFactory.PaymentWays().Load(eda1di.getDocumentTypeId()).getValue());
				eda1di.setRecoveredActExtreme(String
						.valueOf(eda1di.getDurNotRegularActNumber() != null ? eda1di.getDurNotRegularActNumber() : ""));
				for (RecoverReasonType type : RecoverReasonType.values()) {
					if (type.getId().equals(eda1di.getSelctedDurRecoveryReason())) {
						eda1di.setMotivation(type.toString());
					}
				}
				elementsAnnex1f.add(eda1di);

			}
		}
	}

	private void elaborateFieldsOfAnnex1e(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def,
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e) throws PersistenceBeanException {
		DateTime.setDatePattern("dd/MM/yyyy");
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1def) {
			if (Boolean.TRUE.equals(eda1di.getSuspendedByACU()) && !Boolean.TRUE.equals(eda1di.getRectifiedByACU())) {
				eda1di.setDocumentType(BeansFactory.PaymentWays().Load(eda1di.getDocumentTypeId()).getValue());
				eda1di.setSuspensionActExtreme(eda1di.getSuspendActNumber() + " - "
						+ (eda1di.getDateSuspendACU() != null ? DateTime.ToString(eda1di.getDateSuspendACU()) : ""));
				for (ReasonType type : ReasonType.values()) {
					if (type.getId().equals(eda1di.getSuspendReasonId())) {
						eda1di.setMotivation(type.toString(Boolean.TRUE));
					}
				}
				elementsAnnex1e.add(eda1di);

			}
		}
	}

	private void elaborateFieldsOfAnnex1d(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1def,
			List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1d) throws PersistenceBeanException {
		DateTime.setDatePattern("dd/MM/yyyy");
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1def) {
			eda1di.setDocumentType(BeansFactory.PaymentWays().Load(eda1di.getDocumentTypeId()).getValue());
			eda1di.setRetreatActExtreme(
					eda1di.getRetreatActNumber() + " - " + (eda1di.getRetreatDateDecertification() != null
							? DateTime.ToString(eda1di.getRetreatDateDecertification()) : ""));

			if (Boolean.TRUE.equals(eda1di.getRectifiedByACU())) {
				for (ReasonType type : ReasonType.values()) {
					if (type.getId().equals(eda1di.getMotivationId())) {
						eda1di.setMotivation(type.toString(Boolean.FALSE));
					}
				}
				elementsAnnex1d.add(eda1di);
			}
		}
	}

	private void annex1mCompleteRows(List<Annex1mWrapper> elementsAnnex1m, Double[] amountTransferred,
			List<Annex1mWrapper> listSupp) {
		String axis = "";
		for (Annex1mWrapper a1mw : elementsAnnex1m) {
			if (axis.equals("") && Integer.valueOf(a1mw.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(a1mw.getAxis()); i++) {
					Annex1mWrapper a1mwTot = new Annex1mWrapper();
					a1mwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					a1mwTot.setAmountTransferred(amountTransferred[Integer.valueOf(i) - 1]);
					listSupp.add(a1mwTot);
				}
			}

			if (!axis.equals("") && !axis.equals(a1mw.getAxis())) {
				Annex1mWrapper a1mwTot = new Annex1mWrapper();
				a1mwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				a1mwTot.setAmountTransferred(amountTransferred[Integer.valueOf(axis) - 1]);
				listSupp.add(a1mwTot);
			}
			axis = a1mw.getAxis();
			listSupp.add(a1mw);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= amountTransferred.length; i++) {
				Annex1mWrapper a1mwTot = new Annex1mWrapper();
				a1mwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				a1mwTot.setAmountTransferred(amountTransferred[i - 1]);
				listSupp.add(a1mwTot);
			}

		} else {
			for (int i = 0; i < amountTransferred.length; i++) {
				Annex1mWrapper a1mwTot = new Annex1mWrapper();
				a1mwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				a1mwTot.setAmountTransferred(0d);

				listSupp.add(a1mwTot);
			}
		}
		Annex1mWrapper tot = new Annex1mWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setAmountTransferred(
				amountTransferred[0] + amountTransferred[1] + amountTransferred[2] + amountTransferred[3]);
		listSupp.add(tot);
	}

	private void annex1lCompleteRows(List<Annex1lWrapper> elementsAnnex1l, Double[] amountToCoverAdvanceStateAid,
			Double[] lastAmountToCoverAdvanceStateAid, Double[] amountNotCoverAdvanceStateAid,
			List<Annex1lWrapper> listSupp) {
		String axis = "";
		for (Annex1lWrapper a1lw : elementsAnnex1l) {
			if (axis.equals("") && Integer.valueOf(a1lw.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(a1lw.getAxis()); i++) {
					Annex1lWrapper a1lwTot = new Annex1lWrapper();
					a1lwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					a1lwTot.setAmountToCoverAdvanceStateAid(amountToCoverAdvanceStateAid[Integer.valueOf(i) - 1]);
					a1lwTot.setLastAmountToCoverAdvanceStateAid(
							lastAmountToCoverAdvanceStateAid[Integer.valueOf(i) - 1]);
					a1lwTot.setAmountNotCoverAdvanceStateAid(amountNotCoverAdvanceStateAid[Integer.valueOf(i) - 1]);
					listSupp.add(a1lwTot);
				}
			}
			
			if (!axis.equals("") && !axis.equals(a1lw.getAxis())) {
				Annex1lWrapper a1kwTot = new Annex1lWrapper();
				a1kwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				a1kwTot.setAmountToCoverAdvanceStateAid(amountToCoverAdvanceStateAid[Integer.valueOf(axis)-1]);
				a1kwTot.setLastAmountToCoverAdvanceStateAid(lastAmountToCoverAdvanceStateAid[Integer.valueOf(axis)-1]);
				a1kwTot.setAmountNotCoverAdvanceStateAid(amountNotCoverAdvanceStateAid[Integer.valueOf(axis)-1]);
				listSupp.add(a1kwTot);
			}
			axis = a1lw.getAxis();
			listSupp.add(a1lw);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= amountToCoverAdvanceStateAid.length; i++) {
				Annex1lWrapper a1lwTot = new Annex1lWrapper();
				a1lwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				a1lwTot.setAmountToCoverAdvanceStateAid(amountToCoverAdvanceStateAid[i - 1]);
				a1lwTot.setLastAmountToCoverAdvanceStateAid(lastAmountToCoverAdvanceStateAid[i - 1]);
				a1lwTot.setAmountNotCoverAdvanceStateAid(amountNotCoverAdvanceStateAid[i - 1]);
				listSupp.add(a1lwTot);
			}

		} else {
			for (int i = 0; i < amountToCoverAdvanceStateAid.length; i++) {
				Annex1lWrapper a1lwTot = new Annex1lWrapper();
				a1lwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				a1lwTot.setAmountToCoverAdvanceStateAid(0d);
				a1lwTot.setLastAmountToCoverAdvanceStateAid(0d);
				a1lwTot.setAmountNotCoverAdvanceStateAid(0d);

				listSupp.add(a1lwTot);
			}
		}
		Annex1lWrapper tot = new Annex1lWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setAmountToCoverAdvanceStateAid(amountToCoverAdvanceStateAid[0] + amountToCoverAdvanceStateAid[1]
				+ amountToCoverAdvanceStateAid[2] + amountToCoverAdvanceStateAid[3]);
		tot.setLastAmountToCoverAdvanceStateAid(
				lastAmountToCoverAdvanceStateAid[0] + lastAmountToCoverAdvanceStateAid[1]
						+ lastAmountToCoverAdvanceStateAid[2] + lastAmountToCoverAdvanceStateAid[3]);
		tot.setAmountNotCoverAdvanceStateAid(amountNotCoverAdvanceStateAid[0] + amountNotCoverAdvanceStateAid[1]
				+ amountNotCoverAdvanceStateAid[2] + amountNotCoverAdvanceStateAid[3]);
		listSupp.add(tot);
	}

	private void annex1hCompleteRows(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e, Double[] paymentAmount,
			Double[] recoveryAmount, List<ExpenditureDeclarationAnnex1dWrapper> listSupp) {
		String axis = "";
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1e) {
			if (axis.equals("") && Integer.valueOf(eda1di.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(eda1di.getAxis()); i++) {
					ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
					edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					edTot.setPaymentAmount(paymentAmount[Integer.valueOf(i) - 1]);
					edTot.setTotEligibleExpensesPublicSupportNonRecoverable(recoveryAmount[Integer.valueOf(i) - 1]);
					//edTot.setDurNotRegularActNumber(recoveryAmount[Integer.valueOf(i) - 1]);
					listSupp.add(edTot);
				}
			}
			
			if (!axis.equals("") && !axis.equals(eda1di.getAxis())) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				edTot.setPaymentAmount(paymentAmount[Integer.valueOf(axis)-1]);
				edTot.setTotEligibleExpensesPublicSupportNonRecoverable(recoveryAmount[Integer.valueOf(axis) - 1]);
//				edTot.setDurNotRegularActNumber(recoveryAmount[Integer.valueOf(axis)-1]);
				listSupp.add(edTot);
			}
			axis = eda1di.getAxis();
			listSupp.add(eda1di);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				edTot.setPaymentAmount(paymentAmount[i - 1]);
				edTot.setTotEligibleExpensesPublicSupportNonRecoverable(recoveryAmount[i - 1]);
				//edTot.setDurNotRegularActNumber(recoveryAmount[i - 1]);

				listSupp.add(edTot);
			}

		} else {
			for (int i = 0; i < paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				edTot.setPaymentAmount(0d);
				edTot.setTotEligibleExpensesPublicSupportNonRecoverable(0d);
//				edTot.setDurNotRegularActNumber(0d);

				listSupp.add(edTot);
			}
		}
		ExpenditureDeclarationAnnex1dWrapper tot = new ExpenditureDeclarationAnnex1dWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setPaymentAmount(paymentAmount[0] + paymentAmount[1] + paymentAmount[2] + paymentAmount[3]);
		tot.setTotEligibleExpensesPublicSupportNonRecoverable(recoveryAmount[0] + recoveryAmount[1] + recoveryAmount[2] + recoveryAmount[3]);
		//tot.setDurNotRegularActNumber(recoveryAmount[0] + recoveryAmount[1] + recoveryAmount[2] + recoveryAmount[3]);
		listSupp.add(tot);
	}

	private void annex1gCompleteRows(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e, Double[] paymentAmount,
			Double[] recoveryAmount, List<ExpenditureDeclarationAnnex1dWrapper> listSupp) {
		String axis = "";
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1e) {
			if (axis.equals("") && Integer.valueOf(eda1di.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(eda1di.getAxis()); i++) {
					ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
					edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					edTot.setPaymentAmount(paymentAmount[Integer.valueOf(i) - 1]);
					edTot.setCostRecovery(recoveryAmount[Integer.valueOf(i) - 1]);
					listSupp.add(edTot);
				}
			}
			
			if (!axis.equals("") && !axis.equals(eda1di.getAxis())) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				edTot.setPaymentAmount(paymentAmount[Integer.valueOf(axis)]);
				edTot.setCostRecovery(recoveryAmount[Integer.valueOf(axis)]);
				listSupp.add(edTot);
			}
			axis = eda1di.getAxis();
			listSupp.add(eda1di);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				edTot.setPaymentAmount(paymentAmount[i - 1]);
				edTot.setCostRecovery(recoveryAmount[i - 1]);

				listSupp.add(edTot);
			}

		} else {
			for (int i = 0; i < paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				edTot.setPaymentAmount(0d);
				edTot.setCostRecovery(0d);

				listSupp.add(edTot);
			}
		}
		ExpenditureDeclarationAnnex1dWrapper tot = new ExpenditureDeclarationAnnex1dWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setPaymentAmount(paymentAmount[0] + paymentAmount[1] + paymentAmount[2] + paymentAmount[3]);
		tot.setCostRecovery(recoveryAmount[0] + recoveryAmount[1] + recoveryAmount[2] + recoveryAmount[3]);
		listSupp.add(tot);
	}

	private void annex1fCompleteRows(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e, Double[] paymentAmount,
			Double[] recoveryAmount, List<ExpenditureDeclarationAnnex1dWrapper> listSupp) {
		String axis = "";
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1e) {
			if (axis.equals("") && Integer.valueOf(eda1di.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(eda1di.getAxis()); i++) {
					ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
					edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					edTot.setPaymentAmount(paymentAmount[Integer.valueOf(i) - 1]);
					edTot.setDurTotEligibleExpensesPublicSupportReimbursed(recoveryAmount[Integer.valueOf(i) - 1]);
					listSupp.add(edTot);
				}
			}
			
			if (!axis.equals("") && !axis.equals(eda1di.getAxis())) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				edTot.setPaymentAmount(paymentAmount[Integer.valueOf(axis)]);
				edTot.setDurTotEligibleExpensesPublicSupportReimbursed(recoveryAmount[Integer.valueOf(axis)]);
				listSupp.add(edTot);
			}
			axis = eda1di.getAxis();
			listSupp.add(eda1di);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				edTot.setPaymentAmount(paymentAmount[i - 1]);
				edTot.setDurTotEligibleExpensesPublicSupportReimbursed(recoveryAmount[i - 1]);

				listSupp.add(edTot);
			}

		} else {
			for (int i = 0; i < paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				edTot.setPaymentAmount(0d);
				edTot.setDurTotEligibleExpensesPublicSupportReimbursed(0d);

				listSupp.add(edTot);
			}
		}
		ExpenditureDeclarationAnnex1dWrapper tot = new ExpenditureDeclarationAnnex1dWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setPaymentAmount(paymentAmount[0] + paymentAmount[1] + paymentAmount[2] + paymentAmount[3]);
		tot.setDurTotEligibleExpensesPublicSupportReimbursed(
				recoveryAmount[0] + recoveryAmount[1] + recoveryAmount[2] + recoveryAmount[3]);
		listSupp.add(tot);
	}

	private void annex1deCompleteRows(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1e,
			Double[] paymentAmount, Double[] retiredAmount, List<ExpenditureDeclarationAnnex1dWrapper> listSupp) {
		String axis = "";
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1e) {
			if (axis.equals("") && Integer.valueOf(eda1di.getAxis()) > 1) {
				for (int i = 1; i < Integer.valueOf(eda1di.getAxis()); i++) {
					ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
					edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
					edTot.setPaymentAmount(paymentAmount[Integer.valueOf(i) - 1]);
					edTot.setRetiredAmount(retiredAmount[Integer.valueOf(i) - 1]);
					listSupp.add(edTot);
				}
			}
			
			if (!axis.equals("") && !axis.equals(eda1di.getAxis())) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
				edTot.setPaymentAmount(paymentAmount[Integer.valueOf(axis)-1]);
				edTot.setRetiredAmount(retiredAmount[Integer.valueOf(axis)-1]);
				listSupp.add(edTot);
			}
			axis = eda1di.getAxis();
			listSupp.add(eda1di);
		}
		if (!axis.equals("")) {
			for (int i = Integer.valueOf(axis); i <= paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + i);
				edTot.setPaymentAmount(paymentAmount[i - 1]);
				edTot.setRetiredAmount(retiredAmount[i - 1]);

				listSupp.add(edTot);
			}

		} else {
			for (int i = 0; i < paymentAmount.length; i++) {
				ExpenditureDeclarationAnnex1dWrapper edTot = new ExpenditureDeclarationAnnex1dWrapper();
				edTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + (i + 1));
				edTot.setPaymentAmount(0d);
				edTot.setRetiredAmount(0d);

				listSupp.add(edTot);
			}
		}
		ExpenditureDeclarationAnnex1dWrapper tot = new ExpenditureDeclarationAnnex1dWrapper();
		tot.setAxis(Utils.getString("expenditureDeclarationEditTotal"));
		tot.setPaymentAmount(paymentAmount[0] + paymentAmount[1] + paymentAmount[2] + paymentAmount[3]);
		tot.setRetiredAmount(retiredAmount[0] + retiredAmount[1] + retiredAmount[2] + retiredAmount[3]);
		listSupp.add(tot);
	}

	private void annex1mFillFields(List<Annex1mWrapper> elementsAnnex1m, Double[] amountTransferred) {
		for (Annex1mWrapper a1mw : elementsAnnex1m) {
			if (a1mw.getAxis().equals("1")) {
				amountTransferred[0] += (a1mw.getAmountTransferred() != null ? a1mw.getAmountTransferred() : 0d);
			}
			if (a1mw.getAxis().equals("2")) {
				amountTransferred[1] += (a1mw.getAmountTransferred() != null ? a1mw.getAmountTransferred() : 0d);
			}
			if (a1mw.getAxis().equals("3")) {
				amountTransferred[2] += (a1mw.getAmountTransferred() != null ? a1mw.getAmountTransferred() : 0d);
			}
			if (a1mw.getAxis().equals("4")) {
				amountTransferred[3] += (a1mw.getAmountTransferred() != null ? a1mw.getAmountTransferred() : 0d);
			}
		}
	}

	private void annex1lFillFields(List<Annex1lWrapper> elementsAnnex1l, Double[] amountToCoverAdvanceStateAid,
			Double[] lastAmountToCoverAdvanceStateAid, Double[] amountNotCoverAdvanceStateAid) {
		for (Annex1lWrapper a1lw : elementsAnnex1l) {
			if (a1lw.getAxis().equals("1")) {
				amountToCoverAdvanceStateAid[0] += (a1lw.getAmountToCoverAdvanceStateAid() != null
						? a1lw.getAmountToCoverAdvanceStateAid() : 0d);
				lastAmountToCoverAdvanceStateAid[0] += (a1lw.getLastAmountToCoverAdvanceStateAid() != null
						? a1lw.getLastAmountToCoverAdvanceStateAid() : 0d);
				amountNotCoverAdvanceStateAid[0] += (a1lw.getAmountNotCoverAdvanceStateAid() != null
						? a1lw.getAmountNotCoverAdvanceStateAid() : 0d);
			}
			if (a1lw.getAxis().equals("2")) {
				amountToCoverAdvanceStateAid[1] += (a1lw.getAmountToCoverAdvanceStateAid() != null
						? a1lw.getAmountToCoverAdvanceStateAid() : 0d);
				lastAmountToCoverAdvanceStateAid[1] += (a1lw.getLastAmountToCoverAdvanceStateAid() != null
						? a1lw.getLastAmountToCoverAdvanceStateAid() : 0d);
				amountNotCoverAdvanceStateAid[1] += (a1lw.getAmountNotCoverAdvanceStateAid() != null
						? a1lw.getAmountNotCoverAdvanceStateAid() : 0d);
			}
			if (a1lw.getAxis().equals("3")) {
				amountToCoverAdvanceStateAid[2] += (a1lw.getAmountToCoverAdvanceStateAid() != null
						? a1lw.getAmountToCoverAdvanceStateAid() : 0d);
				lastAmountToCoverAdvanceStateAid[2] += (a1lw.getLastAmountToCoverAdvanceStateAid() != null
						? a1lw.getLastAmountToCoverAdvanceStateAid() : 0d);
				amountNotCoverAdvanceStateAid[2] += (a1lw.getAmountNotCoverAdvanceStateAid() != null
						? a1lw.getAmountNotCoverAdvanceStateAid() : 0d);
			}
			if (a1lw.getAxis().equals("4")) {
				amountToCoverAdvanceStateAid[3] += (a1lw.getAmountToCoverAdvanceStateAid() != null
						? a1lw.getAmountToCoverAdvanceStateAid() : 0d);
				lastAmountToCoverAdvanceStateAid[3] += (a1lw.getLastAmountToCoverAdvanceStateAid() != null
						? a1lw.getLastAmountToCoverAdvanceStateAid() : 0d);
				amountNotCoverAdvanceStateAid[3] += (a1lw.getAmountNotCoverAdvanceStateAid() != null
						? a1lw.getAmountNotCoverAdvanceStateAid() : 0d);
			}
		}
	}

	private void annex1hFillFields(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1f, Double[] paymentAmount,
			Double[] recoveryAmount) {
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1f) {
			if (eda1di.getAxis().equals("1")) {
				paymentAmount[0] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				//recoveryAmount[0] += (eda1di.getDurNotRegularActNumber() != null ? eda1di.getDurNotRegularActNumber()
				recoveryAmount[0] += (eda1di.getTotEligibleExpensesPublicSupportNonRecoverable() != null ? eda1di.getTotEligibleExpensesPublicSupportNonRecoverable()
							: 0d);
			}
			if (eda1di.getAxis().equals("2")) {
				paymentAmount[1] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[1] += (eda1di.getTotEligibleExpensesPublicSupportNonRecoverable() != null ? eda1di.getTotEligibleExpensesPublicSupportNonRecoverable()
						: 0d);
			}
			if (eda1di.getAxis().equals("3")) {
				paymentAmount[2] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[2] += (eda1di.getTotEligibleExpensesPublicSupportNonRecoverable() != null ? eda1di.getTotEligibleExpensesPublicSupportNonRecoverable()
						: 0d);
			}
			if (eda1di.getAxis().equals("4")) {
				paymentAmount[3] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[3] += (eda1di.getTotEligibleExpensesPublicSupportNonRecoverable() != null ? eda1di.getTotEligibleExpensesPublicSupportNonRecoverable()
						: 0d);
			}
		}
	}

	private void annex1gFillFields(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1f, Double[] paymentAmount,
			Double[] recoveryAmount) {
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1f) {
			if (eda1di.getAxis().equals("1")) {
				paymentAmount[0] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[0] += (eda1di.getCostRecovery() != null ? eda1di.getCostRecovery() : 0d);
			}
			if (eda1di.getAxis().equals("2")) {
				paymentAmount[1] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[1] += (eda1di.getCostRecovery() != null ? eda1di.getCostRecovery() : 0d);
			}
			if (eda1di.getAxis().equals("3")) {
				paymentAmount[2] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[2] += (eda1di.getCostRecovery() != null ? eda1di.getCostRecovery() : 0d);
			}
			if (eda1di.getAxis().equals("4")) {
				paymentAmount[3] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[3] += (eda1di.getCostRecovery() != null ? eda1di.getCostRecovery() : 0d);
			}
		}
	}

	private void annex1fFillFields(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1f, Double[] paymentAmount,
			Double[] recoveryAmount) {
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1f) {
			if (eda1di.getAxis().equals("1")) {
				paymentAmount[0] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[0] += (eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() != null
						? eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() : 0d);
			}
			if (eda1di.getAxis().equals("2")) {
				paymentAmount[1] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[1] += (eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() != null
						? eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() : 0d);
			}
			if (eda1di.getAxis().equals("3")) {
				paymentAmount[2] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[2] += (eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() != null
						? eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() : 0d);
			}
			if (eda1di.getAxis().equals("4")) {
				paymentAmount[3] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				recoveryAmount[3] += (eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() != null
						? eda1di.getDurTotEligibleExpensesPublicSupportReimbursed() : 0d);
			}
		}
	}

	private void annex1defFillFields(List<ExpenditureDeclarationAnnex1dWrapper> elementsAnnex1de,
			Double[] paymentAmount, Double[] retiredAmount) {
		for (ExpenditureDeclarationAnnex1dWrapper eda1di : elementsAnnex1de) {
			if (eda1di.getAxis().equals("1")) {
				paymentAmount[0] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				retiredAmount[0] += (eda1di.getRetiredAmount() != null ? eda1di.getRetiredAmount() : 0d);
			}
			if (eda1di.getAxis().equals("2")) {
				paymentAmount[1] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				retiredAmount[1] += (eda1di.getRetiredAmount() != null ? eda1di.getRetiredAmount() : 0d);
			}
			if (eda1di.getAxis().equals("3")) {
				paymentAmount[2] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				retiredAmount[2] += (eda1di.getRetiredAmount() != null ? eda1di.getRetiredAmount() : 0d);
			}
			if (eda1di.getAxis().equals("4")) {
				paymentAmount[3] += (eda1di.getPaymentAmount() != null ? eda1di.getPaymentAmount() : 0d);
				retiredAmount[3] += (eda1di.getRetiredAmount() != null ? eda1di.getRetiredAmount() : 0d);
			}
		}
	}

	private void fillListElementAnnex1c() throws HibernateException, PersistenceBeanException {
		ExpenditureDeclaration ed = this.getLastExpenditureDeclaration();
		List<DurCompilations> listDC = null;

		try {
			if (ed != null) {
				listDC = this.getDurCompilations(ed);
			} else {
				listDC = new ArrayList<>();
			}
			this.setListOfAnnex1c(this.listAnnex1c(listDC));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private List<ExpenditureDeclarationAnnex1cWrapper> listAnnex1c(List<DurCompilations> listDC)
			throws PersistenceBeanException {
		List<ExpenditureDeclarationAnnex1cWrapper> listOS11 = null;
		List<ExpenditureDeclarationAnnex1cWrapper> listOS21 = null;
		List<ExpenditureDeclarationAnnex1cWrapper> listOS22 = null;
		List<ExpenditureDeclarationAnnex1cWrapper> listOS31 = null;
		List<ExpenditureDeclarationAnnex1cWrapper> listOS32 = null;
		List<ExpenditureDeclarationAnnex1cWrapper> listOS41 = null;

		// inizializza liste
		for (DurCompilations dc : listDC) {
			String specificGoal = dc.getCostDefinitions().get(0).getProject().getSpecificGoal().getCode();
			if (specificGoal.equals("1.1")) {
				listOS11 = this.initListsOfAnnex1c(dc, listOS11);
			} else if (specificGoal.equals("2.1")) {
				listOS21 = this.initListsOfAnnex1c(dc, listOS21);
			} else if (specificGoal.equals("2.2")) {
				listOS22 = this.initListsOfAnnex1c(dc, listOS22);
			} else if (specificGoal.equals("3.1")) {
				listOS31 = this.initListsOfAnnex1c(dc, listOS31);
			} else if (specificGoal.equals("3.2")) {
				listOS32 = this.initListsOfAnnex1c(dc, listOS32);
			} else if (specificGoal.equals("4.1")) {
				listOS41 = this.initListsOfAnnex1c(dc, listOS41);
			}
		}

		// aggiorna liste in base alla selezione delle ddr nell'interfaccia
		if (this.getList() != null) {
			for (DURCompilationWrapper dcw : this.getList()) {
				if (dcw.isSelected()) {
					DurCompilations dcSupport = BeansFactory.DurCompilations().Load(dcw.getId());
					String specificGoal = dcSupport.getCostDefinitions().get(0).getProject().getSpecificGoal()
							.getCode();
					if (specificGoal.equals("1.1")) {
						listOS11 = this.reviewListsOfAnnex1c(dcSupport, listOS11);
					} else if (specificGoal.equals("2.1")) {
						listOS21 = this.reviewListsOfAnnex1c(dcSupport, listOS21);
					} else if (specificGoal.equals("2.2")) {
						listOS22 = this.reviewListsOfAnnex1c(dcSupport, listOS22);
					} else if (specificGoal.equals("3.1")) {
						listOS31 = this.reviewListsOfAnnex1c(dcSupport, listOS31);
					} else if (specificGoal.equals("3.2")) {
						listOS32 = this.reviewListsOfAnnex1c(dcSupport, listOS32);
					} else if (specificGoal.equals("4.1")) {
						listOS41 = this.reviewListsOfAnnex1c(dcSupport, listOS41);
					}
				}
			}
		}

		List<ExpenditureDeclarationAnnex1cWrapper> result = new ArrayList<>();

		result.addAll(this.setTotalOfListAnnex1c(listOS11, "1"));
		List<ExpenditureDeclarationAnnex1cWrapper> listSupp2 = new ArrayList<>();
		if (listOS21 != null)
			listSupp2.addAll(listOS21);
		if (listOS22 != null)
			listSupp2.addAll(listOS22);
		result.addAll(this.setTotalOfListAnnex1c(listSupp2, "2"));
		List<ExpenditureDeclarationAnnex1cWrapper> listSupp3 = new ArrayList<>();
		if (listOS31 != null)
			listSupp3.addAll(listOS31);
		if (listOS32 != null)
			listSupp3.addAll(listOS32);
		result.addAll(this.setTotalOfListAnnex1c(listSupp3, "3"));
		result.addAll(this.setTotalOfListAnnex1c(listOS41, "4"));
		ExpenditureDeclarationAnnex1cWrapper eda1w = new ExpenditureDeclarationAnnex1cWrapper();
		Double totalLastED = this.getTotalLastED(listOS11, listSupp2, listSupp3, listOS41);
		Double totalED = this.getTotalED(listOS11, listSupp2, listSupp3, listOS41);
		eda1w.setLastExpenditureDeclaration(totalLastED);
		eda1w.setExpenditureDeclaration(totalED);
		eda1w.setAdvancement(totalED - totalLastED);
		eda1w.setAxis(Utils.getString("expenditureDeclarationEditTotalFund"));
		result.add(eda1w);

		return result;
	}

	private Double getTotalLastED(List<ExpenditureDeclarationAnnex1cWrapper> list1,
			List<ExpenditureDeclarationAnnex1cWrapper> list2, List<ExpenditureDeclarationAnnex1cWrapper> list3,
			List<ExpenditureDeclarationAnnex1cWrapper> list4) {
		Double result = 0d;
		if (list1 != null && !list1.isEmpty()) {
			result += list1.get(list1.size() - 1).getLastExpenditureDeclaration() != null
					? list1.get(list1.size() - 1).getLastExpenditureDeclaration() : 0d;
		}
		if (list2 != null && !list2.isEmpty()) {
			result += list2.get(list2.size() - 1).getLastExpenditureDeclaration() != null
					? list2.get(list2.size() - 1).getLastExpenditureDeclaration() : 0d;
		}
		if (list3 != null && !list3.isEmpty()) {
			result += list3.get(list3.size() - 1).getLastExpenditureDeclaration() != null
					? list3.get(list3.size() - 1).getLastExpenditureDeclaration() : 0d;
		}
		if (list4 != null && !list4.isEmpty()) {
			result += list4.get(list4.size() - 1).getLastExpenditureDeclaration() != null
					? list4.get(list4.size() - 1).getLastExpenditureDeclaration() : 0d;
		}
		return result;
	}

	private Double getTotalED(List<ExpenditureDeclarationAnnex1cWrapper> list1,
			List<ExpenditureDeclarationAnnex1cWrapper> list2, List<ExpenditureDeclarationAnnex1cWrapper> list3,
			List<ExpenditureDeclarationAnnex1cWrapper> list4) {
		Double result = 0d;
		if (list1 != null && !list1.isEmpty()) {
			result += list1.get(list1.size() - 1).getExpenditureDeclaration() != null
					? list1.get(list1.size() - 1).getExpenditureDeclaration() : 0d;
		}
		if (list2 != null && !list2.isEmpty()) {
			result += list2.get(list2.size() - 1).getExpenditureDeclaration() != null
					? list2.get(list2.size() - 1).getExpenditureDeclaration() : 0d;
		}
		if (list3 != null && !list3.isEmpty()) {
			result += list3.get(list3.size() - 1).getExpenditureDeclaration() != null
					? list3.get(list3.size() - 1).getExpenditureDeclaration() : 0d;
		}
		if (list4 != null && !list4.isEmpty()) {
			result += list4.get(list4.size() - 1).getExpenditureDeclaration() != null
					? list4.get(list4.size() - 1).getExpenditureDeclaration() : 0d;
		}

		return result;
	}

	private List<ExpenditureDeclarationAnnex1cWrapper> setTotalOfListAnnex1c(
			List<ExpenditureDeclarationAnnex1cWrapper> list, String axis) {
		ExpenditureDeclarationAnnex1cWrapper eda1w = null;
		if (list != null && !list.isEmpty()) {
			eda1w = new ExpenditureDeclarationAnnex1cWrapper();
			eda1w.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
			for (ExpenditureDeclarationAnnex1cWrapper edw : list) {
				eda1w.setLastExpenditureDeclaration((eda1w.getLastExpenditureDeclaration() != null
						? eda1w.getLastExpenditureDeclaration() : 0d)
						+ (edw.getLastExpenditureDeclaration() != null ? edw.getLastExpenditureDeclaration() : 0d));
				eda1w.setExpenditureDeclaration(
						(eda1w.getExpenditureDeclaration() != null ? eda1w.getExpenditureDeclaration() : 0d)
								+ (edw.getExpenditureDeclaration() != null ? edw.getExpenditureDeclaration() : 0d));
				eda1w.setAdvancement(eda1w.getExpenditureDeclaration() - eda1w.getLastExpenditureDeclaration());
			}
		} else {
			list = new ArrayList<>();
			eda1w = new ExpenditureDeclarationAnnex1cWrapper();
			eda1w.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
			eda1w.setLastExpenditureDeclaration(0d);
			eda1w.setExpenditureDeclaration(0d);
			eda1w.setAdvancement(0d);
		}
		list.add(eda1w);
		return list;
	}

	private List<ExpenditureDeclarationAnnex1cWrapper> reviewListsOfAnnex1c(DurCompilations dc,
			List<ExpenditureDeclarationAnnex1cWrapper> list) throws PersistenceBeanException {
		ExpenditureDeclarationAnnex1cWrapper ed = fillExpenditureDeclarationAnnex1cWrappper(dc);
		ed.setExpenditureDeclaration(ed.getLastExpenditureDeclaration());
		ed.setLastExpenditureDeclaration(0d);
		boolean founded = false;
		if (list == null) {
			list = new ArrayList<>();
		}
		for (ExpenditureDeclarationAnnex1cWrapper eda1cw : list) {
			if (ed.equals(eda1cw)) {
				founded = true;
				eda1cw.setExpenditureDeclaration(eda1cw.getExpenditureDeclaration() + ed.getExpenditureDeclaration());
				eda1cw.setAdvancement(eda1cw.getExpenditureDeclaration() - eda1cw.getLastExpenditureDeclaration());
			}
		}

		if (!founded) {
			ed.setAdvancement(ed.getExpenditureDeclaration() - ed.getLastExpenditureDeclaration());
			list.add(ed);
		}
		return list;
	}

	private List<ExpenditureDeclarationAnnex1cWrapper> initListsOfAnnex1c(DurCompilations dc,
			List<ExpenditureDeclarationAnnex1cWrapper> list) throws PersistenceBeanException {
		if (list == null) {
			list = new ArrayList<>();
			list.add(fillExpenditureDeclarationAnnex1cWrappper(dc));
		} else {
			ExpenditureDeclarationAnnex1cWrapper ed = fillExpenditureDeclarationAnnex1cWrappper(dc);
			boolean founded = false;
			for (ExpenditureDeclarationAnnex1cWrapper eda1cw : list) {
				if (ed.equals(eda1cw)) {
					founded = true;
				}
			}
			if (!founded) {
				list.add(ed);
			}
		}
		return list;
	}

	private List<DurCompilations> getDurCompilations(ExpenditureDeclaration ed) throws PersistenceBeanException {
		List<ExpenditureDeclarationDurCompilations> listEDC = BeansFactory.ExpenditureDeclarationDurCompilations()
				.getAllByExpenditureDeclaratios(ed.getId());// ed.getExpenditureDeclarationDurCompilations();

		List<DurCompilations> listDC = new ArrayList<>();
		for (ExpenditureDeclarationDurCompilations eddci : listEDC) {
			listDC.add(BeansFactory.DurCompilations().Load(eddci.getDurCompilationsId()));
		}
		return listDC;
	}

	private ExpenditureDeclaration getLastExpenditureDeclaration() throws PersistenceBeanException {
		ExpenditureDeclaration ed = null;
		List<ExpenditureDeclaration> listED = BeansFactory.ExpenditureDeclarations().LoadAllSubmittedExpenditure();
		if (this.getSession().get("expanditureDeclarationEntityId") != null) {
			Long entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			ExpenditureDeclaration edTmp = null;
			for (ExpenditureDeclaration edi : listED) {
				edi.setExpenditureDeclarationDurCompilations(BeansFactory.ExpenditureDeclarationDurCompilations()
						.getAllByExpenditureDeclaratios(edi.getId()));

				if (edi.getId().equals(entityId)) {
					// effettuare una copia profonda
					try {
						ed = new ExpenditureDeclaration();
						if (edTmp != null) {
							ed = (ExpenditureDeclaration) edTmp.clone();
						}
						return ed;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
				}
				edTmp = edi;
			}
		} else {
			if (listED != null && listED.size() > 0) {
				return ed = listED.get(listED.size() - 1);
			}
		}
		return ed;
	}

	private ExpenditureDeclarationAnnex1cWrapper fillExpenditureDeclarationAnnex1cWrappper(DurCompilations dc)
			throws PersistenceBeanException {
		DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
		DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());
		DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);
		CostDefinitions cd = dc.getCostDefinitions().get(0);
		ExpenditureDeclarationAnnex1cWrapper eda1cw = new ExpenditureDeclarationAnnex1cWrapper();
		eda1cw.setAxis(String.valueOf(cd.getProject().getAsse()));
		eda1cw.setSpecificObjective(cd.getProject().getSpecificGoal().getCode());
		eda1cw.setCup(cd.getProject().getCupCode());
		eda1cw.setLocalCode(String.valueOf(cd.getProject().getId()));
		eda1cw.setSystemCode(cd.getProject().getCode());
		eda1cw.setTitle(cd.getProject().getTitle());
		eda1cw.setLastExpenditureDeclaration(dcw.getSumAmoutCertification());
		eda1cw.setExpenditureDeclaration(0d);
		eda1cw.setAdvancement(eda1cw.getExpenditureDeclaration() - eda1cw.getLastExpenditureDeclaration());
		return eda1cw;
	}

	private void initTableExpenditureDeclaration2() throws PersistenceBeanException {
		lsg = new ArrayList<>();
		lsg = BeansFactory.SpecificGoals().GetAllSpecificGoals();
		this.setLsg(lsg);

		ExpenditureDeclarationAccountingPeriod edw = null;

		this.setListExpDeclWrapper(new ArrayList<ExpenditureDeclarationAccountingPeriod>());

		if (lsg != null && !lsg.isEmpty()) {
			String axis = lsg.get(0).getCode().substring(0, lsg.get(0).getCode().indexOf('.'));
			for (SpecificGoals sg : lsg) {
				edw = new ExpenditureDeclarationAccountingPeriod(sg);
				if (!axis.equals(edw.getAxis())) {
					ExpenditureDeclarationAccountingPeriod edwTot = new ExpenditureDeclarationAccountingPeriod();
					edwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
					this.getListExpDeclWrapper().add(edwTot);
				}
				this.getListExpDeclWrapper().add(edw);
				axis = sg.getCode().substring(0, sg.getCode().indexOf('.'));
			}
			ExpenditureDeclarationAccountingPeriod edwTot = new ExpenditureDeclarationAccountingPeriod();
			edwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalAxis") + " " + axis);
			this.getListExpDeclWrapper().add(edwTot);
			edwTot = new ExpenditureDeclarationAccountingPeriod();
			edwTot.setAxis(Utils.getString("expenditureDeclarationEditTotalFund"));
			this.getListExpDeclWrapper().add(edwTot);
		}
	}

	private void showExpenditureDeclarationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if (!(Boolean) this.getSession().get("expanditureDeclarationEdit")) {
			this.setShowButton(true);
			this.fillComboboxes();
			entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			ed = BeansFactory.ExpenditureDeclarations().LoadById(entityId);
			this.fillDurListForShowPage();
			this.setRendered(true);
			setEntityForEditShowPage();
			// fillFields();
		}
	}

	private void newExpenditureDeclarationPage() throws PersistenceBeanException {
		this.fillComboboxes();
		this.setCompilationDate(new GregorianCalendar().getTime());
		this.setRendered(false);
		this.fillDurListForNewPage();
		this.setEdit(false);
		this.setShowButton(false);
		this.setTotalExpenditure(totalExpenditureComputation());
		this.fillFields();
	}

	private void editExpenditureDeclarationPage()
			throws HibernateException, NumberFormatException, PersistenceBeanException {
		if ((Boolean) this.getSession().get("expanditureDeclarationEdit")) {
			this.fillComboboxes();
			entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			ed = BeansFactory.ExpenditureDeclarations().LoadById(entityId);
			if (ed.getExpenditureDeclarationState().getCode()
					.equals(ExpenditureDeclarationStatusTypes.Submitted.getValue())) {
				this.getSession().put("expanditureDeclarationEdit", false);
				showExpenditureDeclarationPage();
				this.setCanDelete(true);
			} else {
				this.setShowButton(false);
				this.fillDurListForEditPage();
				this.setRendered(true);
				setEntityForEditShowPage();
				this.setTotalExpenditure(totalExpenditureComputation());
			}

		}

	}

	private void fillDurListForNewPage() throws PersistenceBeanException {
		List<DurCompilations> listDurs;
		List<Long> listIndices = initializeFillDurList();
		listDurs = BeansFactory.DurCompilations().LoadByState(DurStateTypes.SendToExpenditureDeclaration);
		endFillDurList(listDurs, listIndices);

	}

	public void fillDurListForShowPage() throws PersistenceBeanException {
		List<DurCompilations> listDurs = null;
		List<Long> listIndices = initializeFillDurList();

		entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");

		if (listDurs == null) {
			listDurs = new ArrayList<>();
		}

		List<ExpenditureDeclarationDurCompilations> eddcs = BeansFactory.ExpenditureDeclarationDurCompilations()
				.getAllByExpenditureDeclaratios(entityId);

		for (ExpenditureDeclarationDurCompilations eddc : eddcs) {
			listDurs.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
		}

		for (DurCompilations dc : listDurs) {
			listIndices.add(dc.getId());
		}

		endFillDurList(listDurs, listIndices);

	}

	private void fillDurListForEditPage() throws PersistenceBeanException {
		List<Long> listIndices = initializeFillDurList();

		List<DurCompilations> listDursSupport = new ArrayList<>();
		List<DurCompilations> listDursED = null;

		List<DurCompilations> listDurs = BeansFactory.DurCompilations()
				.LoadByState(DurStateTypes.SendToExpenditureDeclaration);

		entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");

		if (listDursED == null) {
			listDursED = new ArrayList<>();
		}

		List<ExpenditureDeclarationDurCompilations> eddcs = BeansFactory.ExpenditureDeclarationDurCompilations()
				.getAllByExpenditureDeclaratios(entityId);

		for (ExpenditureDeclarationDurCompilations eddc : eddcs) {
			listDursED.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
		}

		listDursSupport.addAll(listDursED);
		listDursSupport.addAll(listDurs);

		boolean bFoundSelected = false;

		for (DurCompilations dc : listDursSupport) {
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);
			if (listIndices.contains(dcw.getId())) {
				bFoundSelected = true;
				dcw.setSelected(true);
			} else {
				dcw.setSelected(false);
			}
			for (DurCompilations dcs : listDursED) {
				if (dcs.getId().equals(dc.getId())) {
					dcw.setSelected(true);
				}
			}
			this.getList().add(dcw);

		}
		this.setShowSelectAll(bFoundSelected);
		this.ReRenderScroll();
	}

	private List<Long> initializeFillDurList() {
		this.setList(new ArrayList<DURCompilationWrapper>());

		this.setShowSelectAll(false);

		List<Long> listIndices = new ArrayList<Long>();

		if (this.getViewState().get("costCertificationListCD") != null) {
			listIndices = (List<Long>) this.getViewState().get("costCertificationListCD");
		}

		List<DurCompilations> listDurs = null;
		return listIndices;
	}

	private void endFillDurList(List<DurCompilations> listDurs, List<Long> listIndices)
			throws PersistenceBeanException {
		boolean bFoundSelected = false;

		for (DurCompilations dc : listDurs) {
			DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
			DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

			DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);
			if (listIndices.contains(dcw.getId())) {
				bFoundSelected = true;
				dcw.setSelected(true);
			} else {
				dcw.setSelected(false);
			}
			this.getList().add(dcw);
		}
		this.setShowSelectAll(bFoundSelected);
		this.ReRenderScroll();
	}

	public void setEntityForEditShowPage() throws NumberFormatException, HibernateException, PersistenceBeanException {
		if (this.getSession().get("expanditureDeclarationEntityId") != null) {
			entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			this.setEdit(!(Boolean) this.getSession().get("expanditureDeclarationEdit"));
			this.setRendered(true);
			ed = BeansFactory.ExpenditureDeclarations().LoadById(entityId);
			this.setProtocolNumber(ed.getProtocolNumber());
			this.setCompilationDate(ed.getCompilationDate());
			this.setProtocolDate(ed.getProtocolDate());
			this.setTotAmountEligibleExpenditure(ed.getTotAmountEligibleExpenditure());
			this.setTotAmountPublicExpenditure(ed.getTotAmountPublicExpenditure());
			this.setTotAmountPrivateExpenditure(ed.getTotAmountPrivateExpenditure());
			this.setTotAmountStateAid(ed.getTotAmountStateAid());
			this.setTotAmountCoverageAdvanceAid(ed.getTotAmountCoverageAdvanceAid());
			this.setTotContributionInKindPubShare(ed.getTotContributionInKindPubShare());
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			if (this.getDateFrom() == null) {
				this.setDateFrom(ed.getAccountingPeriodFrom());
				Calendar from = new GregorianCalendar();
				from.setTime(this.getDateFrom());
				this.setDateFromString(df.format(this.getDateFrom()));
			}
			if (this.getDateTo() == null) {
				this.setDateTo(ed.getAccountingPeriodTo());
				Calendar to = new GregorianCalendar();
				to.setTime(this.getDateTo());
				this.setDateToString(df.format(this.getDateTo()));
			}
			this.setTotalExpenditure(ed.getTotalExpenditure());
			this.setExpenditureDeclarationId(String.valueOf(ed.getId()));

			RequestTypes rt = ed.getRequestType();
			this.setType(String.valueOf(rt.getId()));
		}
	}

	public void fillDurList() throws PersistenceBeanException {
		List<DurCompilations> listDurs = null;
		List<Long> listIndices = initializeFillDurList();
		if ((Boolean) this.getSession().get("expenditureDeclarationNew")) {

			listDurs = BeansFactory.DurCompilations().LoadByState(DurStateTypes.SendToExpenditureDeclaration);
		} else if (!(Boolean) this.getSession().get("expanditureDeclarationEdit")) {
			entityId = (Long) this.getSession().get("expanditureDeclarationEntityId");
			ExpenditureDeclaration ex = BeansFactory.ExpenditureDeclarations().LoadById(entityId);

			if (listDurs == null) {
				listDurs = new ArrayList<>();
			}
			for (ExpenditureDeclarationDurCompilations eddc : ex.getExpenditureDeclarationDurCompilations()) {
				listDurs.add(BeansFactory.DurCompilations().Load(eddc.getDurCompilationsId()));
			}
		}
		if ((Boolean) this.getSession().get("expenditureDeclarationNew")
				|| !(Boolean) this.getSession().get("expanditureDeclarationEdit")) {
			boolean bFoundSelected = false;

			for (DurCompilations dc : listDurs) {
				DurInfos di = BeansFactory.DurInfos().LoadByDurCompilation(dc.getId());
				DurSummaries ds = BeansFactory.DurSummaries().LoadByDurCompilation(dc.getId());

				DURCompilationWrapper dcw = new DURCompilationWrapper(0, dc, dc.getDurState(), di, ds);

				if (listIndices.contains(dcw.getId())) {
					bFoundSelected = true;

					dcw.setSelected(true);
				} else {
					dcw.setSelected(false);
				}
				this.getList().add(dcw);
			}
			this.setShowSelectAll(bFoundSelected);
			this.ReRenderScroll();
		}
		fillDurListForEditPage();
	}

	private void fillComboboxes() throws HibernateException, PersistenceBeanException {
		this.fillTypes();
		this.fillListOfDetails();
	}

	private void fillListOfDetails() {
		this.annexList = new ArrayList<>();
		SelectItem item = new SelectItem();
		this.annexList.add(SelectItemHelper.getFirst());
		for (TypeOfDetail tod : TypeOfDetail.values()) {
			item = new SelectItem();
			item.setValue(String.valueOf(tod.getCode()));
			item.setLabel(Utils.getString(tod.getKey()));
			this.annexList.add(item);
		}
	}

	private void fillTypes() throws HibernateException, PersistenceBeanException {
		this.types = new ArrayList<>();
		try {

			setRequestTypes(BeansFactory.RequestTypes().selectAllExeptIntermediateFinalState());
		} catch (PersistenceBeanException e) {
			log.error("errore");
		}
		SelectItem item = new SelectItem();
		this.types.add(SelectItemHelper.getFirst());
		for (RequestTypes c : this.getRequestTypes()) {
			item = new SelectItem();
			item.setValue(String.valueOf(c.getId()));
			item.setLabel(c.getValue());
			this.types.add(item);
		}
		if (this.getType() == null) {
			if (!this.types.isEmpty()) {
				this.setType(this.types.get(0).getValue().toString());

			}
		}

	}

	public void annexChange() {
		String msg = null;
		for (TypeOfDetail tod : TypeOfDetail.values()) {
			if (tod.getCode().equals(this.getAnnex())) {
				msg = Utils.getString("expenditureDeclarationEditAnnexDownload") + " " + Utils.getString(tod.getKey());

			}
		}
		this.setMessage(msg);
		try {
			this.fillListElementAnnex1defghlm();
		} catch (PersistenceBeanException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	@Override
	public void Page_Load_Static() throws HibernateException, PersistenceBeanException {
		this.setFailedValidation(new ArrayList<String>());
		this.setShowDetails(false);
	}

	@SuppressWarnings("unchecked")
	private List<String> getFailedValidation() {
		return (List<String>) this.getViewState().get("FailedValidationComponents");
	}

	private void setFailedValidation(List<String> arrayList) {
		this.getViewState().put("FailedValidationComponents", arrayList);
	}

	@Override
	public void addEntity() {
	}

	@Override
	public void editEntity() {
	}

	public void Page_Save() {
		if (getSaveFlag() == 0) {
			try {
				if (!this.BeforeSave()) {
					return;
				}
			} catch (PersistenceBeanException e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			} catch (Exception e2) {
				log.error("EntityEditBean exception:", e2);
				return;
			}

			try {
				tr = PersistenceSessionManager.getBean().getSession().beginTransaction();
			} catch (HibernateException e1) {
				log.error("EntityEditBean exception:", e1);

			} catch (PersistenceBeanException e1) {
				log.error("EntityEditBean exception:", e1);

			}
			setSaveFlag(1);
			try {
				this.SaveEntity();
			} catch (HibernateException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);

			} catch (PersistenceBeanException e) {
				if (tr != null) {
					tr.rollback();
				}

				log.error("EntityEditBean exception:", e);
			} catch (NumberFormatException e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} catch (IOException e) {
				if (tr != null) {
					tr.rollback();
				}

				log.error("EntityEditBean exception:", e);
			} catch (Exception e) {
				if (tr != null) {
					tr.rollback();
				}
				log.error("EntityEditBean exception:", e);
			} finally {
				if (tr != null && !tr.wasRolledBack() && tr.isActive()) {
					tr.commit();
				}
			}

			this.AfterSave();
			setSaveFlag(0);
		}
	}

	public void AfterSave() {
		this.GoBack();

	}

	public void GoBack() {
		if ((Boolean) this.getSession().get("expenditureDeclarationNew")) {
			this.getSession().remove("expenditureDeclarationNew");
		}
		if (this.getSession().get("expanditureDeclarationEntityId") != null) {
			this.getSession().remove("expanditureDeclarationEntityId");
			this.getSession().remove("expanditureDeclarationEdit");
		}
		this.setType(null);
		this.setList(null);
		this.goTo(PagesTypes.EXPENDITUREDECLARATIONLIST);
	}

	public void SaveEntity() throws HibernateException, PersistenceBeanException, NumberFormatException, IOException {
		ExpenditureDeclaration entityToSave = null;
		if (Boolean.valueOf(this.getSession().get("expenditureDeclarationNew").toString())) {
			entityToSave = new ExpenditureDeclaration();
			entityToSave.setDeleted(false);
			entityToSave.setCreateDate(new GregorianCalendar().getTime());
		} else {
			entityToSave = ed;
		}
		if (this.getCompilationDate() != null) {
			entityToSave.setCompilationDate(this.getCompilationDate());
		}
		if (this.getProtocolNumber() != null && !this.getProtocolNumber().trim().equals("")
				&& !this.getProtocolNumber().isEmpty()) {
			entityToSave.setProtocolNumber(this.getProtocolNumber());
		}
		if (this.getProtocolDate() != null) {
			entityToSave.setProtocolDate(this.getProtocolDate());
		}

		if (this.getTotAmountEligibleExpenditure() != null) {
			entityToSave.setTotAmountEligibleExpenditure(this.getTotAmountEligibleExpenditure());
		}
		if (this.getTotAmountPublicExpenditure() != null) {
			entityToSave.setTotAmountPublicExpenditure(this.getTotAmountPublicExpenditure());
		}
		if (this.getTotAmountPrivateExpenditure() != null) {
			entityToSave.setTotAmountPrivateExpenditure(this.getTotAmountPrivateExpenditure());
		}
		if (this.getTotAmountStateAid() != null) {
			entityToSave.setTotAmountStateAid(this.getTotAmountStateAid());
		}
		if (this.getTotAmountCoverageAdvanceAid() != null) {
			entityToSave.setTotAmountCoverageAdvanceAid(this.getTotAmountCoverageAdvanceAid());
		}
		if (this.getTotContributionInKindPubShare() != null) {
			entityToSave.setTotContributionInKindPubShare(this.getTotContributionInKindPubShare());
		}

		if (this.getTotalExpenditure() != null && !this.getTotalExpenditure().equals("")) {
			entityToSave.setTotalExpenditure(this.getTotalExpenditure());
		}
		ExpenditureDeclarationStates es = null;
		if (this.getState_id() == null) {
			es = BeansFactory.ExpenditureDeclarationStates()
					.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Compiled.getValue());
			setState_id(es.getId());
		}
		if (this.getState_id() != null) {
			es = new ExpenditureDeclarationStates();
			es.setId(this.getState_id());
			entityToSave.setExpenditureDeclarationState(es);
		}
		es = BeansFactory.ExpenditureDeclarationStates()
				.GetExpenditureDeclarationStatus(ExpenditureDeclarationStatusTypes.Submitted.getValue());
		if (es.getId().equals(this.getState_id())) {
			entityToSave.setSubmissionDate(new GregorianCalendar().getTime());
			// inserire il salvataggio delle eventuali vecchie Dichiarazioni di
			// spesa
			List<ExpenditureDeclarationAccountingPeriod> listOldEDAP = this.setFieldsIntoOtherExpenditure();
			if (listOldEDAP != null) {
				for (ExpenditureDeclarationAccountingPeriod edac : listOldEDAP) {
					PersistenceSessionManager.getBean().getSession().merge(edac);

				}
			}

		}

		if (this.getDateFrom() != null) {
			entityToSave.setAccountingPeriodFrom(this.getDateFrom());
		}

		if (this.getDateTo() != null) {
			entityToSave.setAccountingPeriodTo(this.getDateTo());
		}

		if (this.getType() != null) {
			for (RequestTypes rt : this.getRequestTypes()) {
				if (this.getType().equals(String.valueOf(rt.getId()))) {
					entityToSave.setRequestType(rt);
				}
			}

		}

		ExpenditureDeclaration savedEntity = (ExpenditureDeclaration) PersistenceSessionManager.getBean().getSession()
				.merge(entityToSave);

		List<ExpenditureDeclarationDurCompilations> eddcs = new ArrayList<>();
		ExpenditureDeclarationDurCompilations eddc = null;
		for (DurCompilations dc : this.getListSelectedDur()) {
			eddc = new ExpenditureDeclarationDurCompilations(savedEntity.getId(), dc.getId());
			eddc.setCreateDate(new GregorianCalendar().getTime());
			eddc.setDeleted(false);
			eddcs.add(eddc);
			List<ExpenditureDeclarationDurCompilations> eds = BeansFactory.ExpenditureDeclarationDurCompilations()
					.getAllByExpenditureDeclaratios(savedEntity.getId());

			if (eds != null && !eds.isEmpty()) {
				boolean founded = false;
				for (ExpenditureDeclarationDurCompilations ed : eds) {

					if (ed.getDurCompilationsId().equals(dc.getId())) {
						founded = true;
					}
				}
				if (!founded) {
					PersistenceSessionManager.getBean().getSession().merge(eddc);
				}
			} else {
				PersistenceSessionManager.getBean().getSession().merge(eddc);
			}

		}

		for (ExpenditureDeclarationAccountingPeriod edw : this.getListExpDeclWrapper()) {
			if (edw.getSpecificObjective() != null && !edw.getSpecificObjective().trim().equals("")) {

				edw.setExpenditureDeclaration(savedEntity);
				edw.setCreateDate(new GregorianCalendar().getTime());
				edw.setDeleted(false);
				PersistenceSessionManager.getBean().getSession().merge(edw);
			}
		}

		if (this.getListSelectedDur().size() > 0) {
			for (DurCompilations dur : this.getListSelectedDur()) {
				dur.setExpenditureDeclaration(savedEntity);
				BeansFactory.DurCompilations().UpdateInTransaction(dur);
			}
		}
	}

	public Boolean BeforeSave() throws PersistenceBeanException {
		checkEmpty("EditForm:tab1:type", this.getType());
		if (this.validationFailed) {
			return false;
		}
		return true;
	}

	public void setType(String type) {
		this.getSession().put("type", type);
	}

	public String getType() {
		if (this.getSession().get("type") == null) {
			return null;
		}

		return this.getSession().get("type").toString();
	}

	public void setAnnex(Long annex) {
		this.getViewState().put("annex", annex);
	}

	public Long getAnnex() {
		return (Long) this.getViewState().get("annex");
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
		return this.getViewState().get("saveFlag") == null ? 0 : (Integer) this.getViewState().get("saveFlag");
	}

	public void selectItem() throws NumberFormatException, HibernateException, PersistenceBeanException {
		List<Long> listIndices = new ArrayList<Long>();

		this.setSelectAll(false);

		for (DURCompilationWrapper dcw : this.getList()) {
			if (dcw.isSelectable() && dcw.isSelected()) {
				listIndices.add(dcw.getId());
			}
		}

		this.getViewState().put("costCertificationListCD", listIndices);

		try {
			this.Page_Load();
		} catch (NullPointerException e) {
			log.error("error selectItem");
		}
	}

	public void selectAll() throws NumberFormatException, HibernateException, PersistenceBeanException,
			NullPointerException, IOException {
		List<Long> listIndices = new ArrayList<Long>();
		if (this.isSelectAll()) {
			for (DURCompilationWrapper dcw : this.getList()) {
				if (dcw.isSelectable() && dcw.getCanCreatePaymentRequest()) {
					listIndices.add(dcw.getId());
				}
			}
		}

		this.getViewState().put("costCertificationListCD", listIndices);

		this.Page_Load();
	}

	public List<SelectItem> getListType() {
		return types;
	}

	public void setListType(List<SelectItem> types) {
		this.types = types;
	}

	public List<SelectItem> getTypes() {
		return types;
	}

	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}

	public String getExpenditureDeclarationId() {
		return expenditureDeclarationId;
	}

	public void setExpenditureDeclarationId(String expenditureDeclarationId) {
		this.expenditureDeclarationId = expenditureDeclarationId;
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public Date getProtocolDate() {
		return protocolDate;
	}

	public void setProtocolDate(Date protocolDate) {
		this.protocolDate = protocolDate;
	}

	public Double getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(Double totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public boolean isValidationFailed() {
		return validationFailed;
	}

	public void setValidationFailed(boolean validationFailed) {
		this.validationFailed = validationFailed;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public boolean isShowSelectAll() {
		return showSelectAll;
	}

	public void setShowSelectAll(boolean showSelectAll) {
		this.showSelectAll = showSelectAll;
	}

	public List<DurCompilations> getListSelectedDur() {
		return listSelectedDur;
	}

	public void setListSelectedDur(List<DurCompilations> listSelectedDur) {
		this.listSelectedDur = listSelectedDur;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public ExpenditureDeclaration getEd() {
		return ed;
	}

	public void setEd(ExpenditureDeclaration ed) {
		this.ed = ed;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public List<RequestTypes> getRequestTypes() {
		return requestTypes;
	}

	public void setRequestTypes(List<RequestTypes> requestTypes) {
		this.requestTypes = requestTypes;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public Long getEntityEditId() {
		return entityEditId;
	}

	public void setEntityEditId(Long entityEditId) {
		this.entityEditId = entityEditId;
	}

	public Double getTotAmountEligibleExpenditure() {
		return totAmountEligibleExpenditure;
	}

	public void setTotAmountEligibleExpenditure(Double totAmountEligibleExpenditure) {
		this.totAmountEligibleExpenditure = totAmountEligibleExpenditure;
	}

	public Double getTotAmountPublicExpenditure() {
		return totAmountPublicExpenditure;
	}

	public void setTotAmountPublicExpenditure(Double totAmountPublicExpenditure) {
		this.totAmountPublicExpenditure = totAmountPublicExpenditure;
	}

	public Double getTotAmountPrivateExpenditure() {
		return totAmountPrivateExpenditure;
	}

	public void setTotAmountPrivateExpenditure(Double totAmountPrivateExpenditure) {
		this.totAmountPrivateExpenditure = totAmountPrivateExpenditure;
	}

	public Double getTotAmountStateAid() {
		return totAmountStateAid;
	}

	public void setTotAmountStateAid(Double totAmountStateAid) {
		this.totAmountStateAid = totAmountStateAid;
	}

	public Double getTotAmountCoverageAdvanceAid() {
		return totAmountCoverageAdvanceAid;
	}

	public void setTotAmountCoverageAdvanceAid(Double totAmountCoverageAdvanceAid) {
		this.totAmountCoverageAdvanceAid = totAmountCoverageAdvanceAid;
	}

	public Double getTotContributionInKindPubShare() {
		return totContributionInKindPubShare;
	}

	public void setTotContributionInKindPubShare(Double totContributionInKindPubShare) {
		this.totContributionInKindPubShare = totContributionInKindPubShare;
	}

	@Override
	public void deleteEntity() {

	}

	public Date getDateTo() {
		return (Date) this.getViewState().get("dateTo");
	}

	public void setDateTo(Date dateTo) {
		this.getViewState().put("dateTo", dateTo);
	}

	public Date getDateFrom() {
		return (Date) this.getViewState().get("dateFrom");
	}

	public void setDateFrom(Date dateFrom) {
		this.getViewState().put("dateFrom", dateFrom);
	}

	public List<SpecificGoals> getLsg() {
		return (List<SpecificGoals>) this.getViewState().get("lsg");
	}

	public void setLsg(List<SpecificGoals> lsg) {
		this.getViewState().put("lsg", lsg);
	}

	public List<ExpenditureDeclarationAccountingPeriod> getListExpDeclWrapper() {
		return (List<ExpenditureDeclarationAccountingPeriod>) this.getViewState().get("listExpDeclWrapper");
	}

	public void setListExpDeclWrapper(List<ExpenditureDeclarationAccountingPeriod> listExpDeclWrapper) {
		this.getViewState().put("listExpDeclWrapper", listExpDeclWrapper);
	}

	public List<ExpenditureDeclarationAnnex1cWrapper> getListOfAnnex1c() {
		return (List<ExpenditureDeclarationAnnex1cWrapper>) this.getViewState().get("listOfAnnex1c");
	}

	public void setListOfAnnex1c(List<ExpenditureDeclarationAnnex1cWrapper> listOfAnnex1c) {
		this.getViewState().put("listOfAnnex1c", listOfAnnex1c);
	}

	public List<ExpenditureDeclarationAnnex1dWrapper> getListOfAnnex1d() {
		return (List<ExpenditureDeclarationAnnex1dWrapper>) this.getViewState().get("listOfAnnex1d");
	}

	public void setListOfAnnex1d(List<ExpenditureDeclarationAnnex1dWrapper> listOfAnnex1d) {
		this.getViewState().put("listOfAnnex1d", listOfAnnex1d);
	}

	public List<ExpenditureDeclarationAnnex1dWrapper> getListOfAnnex1e() {
		return (List<ExpenditureDeclarationAnnex1dWrapper>) this.getViewState().get("listOfAnnex1e");
	}

	public void setListOfAnnex1e(List<ExpenditureDeclarationAnnex1dWrapper> listOfAnnex1e) {
		this.getViewState().put("listOfAnnex1e", listOfAnnex1e);
	}

	public List<ExpenditureDeclarationAnnex1dWrapper> getListOfAnnex1f() {
		return (List<ExpenditureDeclarationAnnex1dWrapper>) this.getViewState().get("listOfAnnex1f");
	}

	public void setListOfAnnex1f(List<ExpenditureDeclarationAnnex1dWrapper> listOfAnnex1f) {
		this.getViewState().put("listOfAnnex1f", listOfAnnex1f);
	}

	public List<ExpenditureDeclarationAnnex1dWrapper> getListOfAnnex1g() {
		return (List<ExpenditureDeclarationAnnex1dWrapper>) this.getViewState().get("listOfAnnex1g");
	}

	public void setListOfAnnex1g(List<ExpenditureDeclarationAnnex1dWrapper> listOfAnnex1g) {
		this.getViewState().put("listOfAnnex1g", listOfAnnex1g);
	}

	public List<ExpenditureDeclarationAnnex1dWrapper> getListOfAnnex1h() {
		return (List<ExpenditureDeclarationAnnex1dWrapper>) this.getViewState().get("listOfAnnex1h");
	}

	public void setListOfAnnex1h(List<ExpenditureDeclarationAnnex1dWrapper> listOfAnnex1h) {
		this.getViewState().put("listOfAnnex1h", listOfAnnex1h);
	}

	public List<Annex1lWrapper> getListOfAnnex1l() {
		return (List<Annex1lWrapper>) this.getViewState().get("listOfAnnex1l");
	}

	public void setListOfAnnex1l(List<Annex1lWrapper> listOfAnnex1l) {
		this.getViewState().put("listOfAnnex1l", listOfAnnex1l);
	}

	public List<Annex1mWrapper> getListOfAnnex1m() {
		return (List<Annex1mWrapper>) this.getViewState().get("listOfAnnex1m");
	}

	public void setListOfAnnex1m(List<Annex1mWrapper> listOfAnnex1m) {
		this.getViewState().put("listOfAnnex1m", listOfAnnex1m);
	}

	public String getDateToString() {
		return (String) this.getViewState().get("dateToString");
	}

	public void setDateToString(String dateToString) {
		this.getViewState().put("dateToString", dateToString);
	}

	public String getDateFromString() {
		return (String) this.getViewState().get("dateFromString");
	}

	public void setDateFromString(String dateFromString) {
		this.getViewState().put("dateFromString", dateFromString);
	}

	public boolean getShowDetails() {
		if (this.getViewState().get("showDetails") == null) {
			return false;
		} else {
			return (boolean) this.getViewState().get("showDetails");
		}
	}
	
	public void setShowDetails(boolean showDetails) {
		this.getViewState().put("showDetails", showDetails);
	}
	
	public boolean getShowExports(){
		if (this.getViewState().get("showExports") == null) {
			return false;
		} else {
			return (boolean) this.getViewState().get("showExports");
		}
	}
	
	public void setShowExports(boolean showExports) {
		this.getViewState().put("showExports", showExports);
	}
	
	public List<SelectItem> getAnnexList() {
		return annexList;
	}

	public void setAnnexList(List<SelectItem> annexList) {
		this.annexList = annexList;
	}

	public String getMessage() {
		return (String) this.getViewState().get("message");
	}

	public void setMessage(String message) {
		this.getViewState().put("message", message);
	}

}
