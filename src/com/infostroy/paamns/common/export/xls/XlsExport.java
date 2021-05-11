package com.infostroy.paamns.common.export.xls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.annotations.Export;
import com.infostroy.paamns.common.annotations.Exports;
import com.infostroy.paamns.common.enums.ExportPlaces;
import com.infostroy.paamns.common.enums.FieldTypes;
import com.infostroy.paamns.common.helpers.NumberHelper;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.web.converters.BaseConverter;

/**
 * 
 * @author Alexander Kachur
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public class XlsExport {
	protected static transient final Log log = LogFactory.getLog(XlsExport.class);

	public <T, T2, T3> byte[] createXlsWithMessages(List<T> list, ExportPlaces place, List<T2> listMsg1,
			List<T3> listMsg2) throws IOException, WriteException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("it", "IT"));
		WritableWorkbook workbook = Workbook.createWorkbook(byteOutStream, wbSettings);

		writeXlsWithMessages(workbook, list, listMsg1, listMsg2, place);

		workbook.write();
		workbook.close();
		return byteOutStream.toByteArray();
	}
	
	public <T, T2> byte[] createXlsOnlyMessages(List<T> listMsg1,List<T2> listMsg2) throws IOException, WriteException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("it", "IT"));
		WritableWorkbook workbook = Workbook.createWorkbook(byteOutStream, wbSettings);

		writeXlsOnlyMessage(workbook, listMsg1, listMsg2);

		workbook.write();
		workbook.close();
		return byteOutStream.toByteArray();
	}

	public <T> byte[] createXls(List<T> list, ExportPlaces place) throws IOException, WriteException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("it", "IT"));
		WritableWorkbook workbook = Workbook.createWorkbook(byteOutStream, wbSettings);

		writeXls(workbook, list, place);

		workbook.write();
		workbook.close();
		return byteOutStream.toByteArray();
	}

	public <T, T2, T3> byte[] createXls(List<T> list, ExportPlaces place, List<T2> list2, ExportPlaces place2,
			List<T3> list3, ExportPlaces place3) throws IOException, WriteException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("it", "IT"));
		WritableWorkbook workbook = Workbook.createWorkbook(byteOutStream, wbSettings);

		writeXls(workbook, list, place, list2, place2, list3, place3);

		workbook.write();
		workbook.close();
		return byteOutStream.toByteArray();
	}
	
	private <T, T2> void writeXlsOnlyMessage(WritableWorkbook workbook, List<T> listMsg1,List<T2> listMsg2) throws RowsExceededException, WriteException{
		workbook.createSheet("Report", 0);
		WritableSheet sheet = workbook.getSheet(0);
		
		WritableFont body = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat bodyFormat = new WritableCellFormat(body);
		
		int row =listMsg1.size()+2;
		int column = 0; 
		
		createContentPart1(sheet, listMsg1, bodyFormat);
		createContentMessage(sheet, listMsg2, bodyFormat, row, column, column+8);
	}

	public <T, T2, T3> void writeXlsWithMessages(WritableWorkbook workbook, List<T> list, List<T2> listMsg1,
			List<T3> listMsg2, ExportPlaces place)
			throws WriteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		workbook.createSheet("Report", 0);
		WritableSheet sheet = workbook.getSheet(0);

		WritableFont timesFont = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat times = new WritableCellFormat(timesFont);

		times.setBorder(Border.ALL, BorderLineStyle.THIN);
		
		WritableFont body = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat bodyFormat = new WritableCellFormat(body);

		WritableFont timesFont1 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);

		WritableCellFormat header = new WritableCellFormat(timesFont1);
		header.setBackground(Colour.WHITE);
		header.setOrientation(Orientation.HORIZONTAL);
		header.setAlignment(Alignment.CENTRE);
		header.setBorder(Border.ALL, BorderLineStyle.THIN);

		Method[] methods = list.get(0).getClass().getMethods();
		List<EntityObject> listObjects = new ArrayList<EntityObject>();
		for (@SuppressWarnings("unused")
		T item : list) {
			listObjects.add(new EntityObject());
		}

		for (Method m : methods) {
			Annotation[] annotations = m.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				Export annot = null;
				if (a instanceof Exports) {
					if (((Exports) a).value() != null) {
						for (Export item : ((Exports) a).value()) {
							if (item.place().equals(place)) {
								annot = item;
								break;
							}
						}
					}
				} else if (a instanceof Export) {
					annot = (Export) a;
				}

				if (annot != null) {
					if (annot.place().equals(place)) {
						for (T item : list) {
							listObjects.get(list.indexOf(item)).getFields().add(new Field(annot.propertyName(),
									annot.type(), m.invoke(item, new Object[0]), annot.seqXLS()));
						}
					}

					break;
				}
			}
		}

		if (!listObjects.isEmpty()) {
			List<Field> testFields = listObjects.get(0).getFields();
			Collections.sort(testFields, new FieldsComparer());
			createContentPart1(sheet, listMsg1, bodyFormat);
			createHeaderWithMessage(sheet, listMsg1, testFields, header);
			createContentPart2(sheet, listMsg1, listObjects, times);
			createContentPart3(sheet, listMsg1, listMsg2, listObjects, bodyFormat);
		}

	}

	

	private <T> void writeXls(WritableWorkbook workbook, List<T> list, ExportPlaces place)
			throws WriteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		workbook.createSheet("Report", 0);
		WritableSheet sheet = workbook.getSheet(0);

		WritableFont timesFont = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat times = new WritableCellFormat(timesFont);

		times.setBorder(Border.ALL, BorderLineStyle.THIN);

		WritableFont timesFont1 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);

		WritableCellFormat header = new WritableCellFormat(timesFont1);
		header.setBackground(Colour.WHITE);
		header.setOrientation(Orientation.HORIZONTAL);
		header.setAlignment(Alignment.CENTRE);
		header.setBorder(Border.ALL, BorderLineStyle.THIN);

		Method[] methods = list.get(0).getClass().getMethods();
		List<EntityObject> listObjects = new ArrayList<EntityObject>();
		for (@SuppressWarnings("unused")
		T item : list) {
			listObjects.add(new EntityObject());
		}

		for (Method m : methods) {
			Annotation[] annotations = m.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				Export annot = null;
				if (a instanceof Exports) {
					if (((Exports) a).value() != null) {
						for (Export item : ((Exports) a).value()) {
							if (item.place().equals(place)) {
								annot = item;
								break;
							}
						}
					}
				} else if (a instanceof Export) {
					annot = (Export) a;
				}

				if (annot != null) {
					if (annot.place().equals(place)) {
						for (T item : list) {
							listObjects.get(list.indexOf(item)).getFields().add(new Field(annot.propertyName(),
									annot.type(), m.invoke(item, new Object[0]), annot.seqXLS()));
						}
					}

					break;
				}
			}
		}

		if (!listObjects.isEmpty()) {
			List<Field> testFields = listObjects.get(0).getFields();
			Collections.sort(testFields, new FieldsComparer());

			createHeader(sheet, testFields, header);
			createContent(sheet, listObjects, times);
		}

	}

	private <T, T2, T3> void writeXls(WritableWorkbook workbook, List<T> list, ExportPlaces place, List<T2> list2,
			ExportPlaces place2, List<T3> list3, ExportPlaces place3)
			throws WriteException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		workbook.createSheet("Report", 0);
		WritableSheet sheet = workbook.getSheet(0);

		WritableFont timesFont = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat times = new WritableCellFormat(timesFont);
		WritableFont timesFontBold = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
		WritableCellFormat times2 = new WritableCellFormat(timesFontBold);

		times.setBorder(Border.ALL, BorderLineStyle.THIN);

		WritableFont timesFont1 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);

		WritableCellFormat header = new WritableCellFormat(timesFont1);
		header.setBackground(Colour.WHITE);
		header.setOrientation(Orientation.HORIZONTAL);
		header.setAlignment(Alignment.CENTRE);
		header.setBorder(Border.ALL, BorderLineStyle.THIN);

		Method[] methods = null;
		if (list != null && !list.isEmpty()) {
			methods = list.get(0).getClass().getMethods();
		}
		Method[] methods2 = list2.get(0).getClass().getMethods();
		Method[] methods3 = list3.get(0).getClass().getMethods();
		List<EntityObject> listObjects = new ArrayList<EntityObject>();
		List<EntityObject> listObjects2 = new ArrayList<EntityObject>();
		List<EntityObject> listObjects3 = new ArrayList<EntityObject>();
		for (@SuppressWarnings("unused")
		T item : list) {
			listObjects.add(new EntityObject());
		}

		if (methods != null) {
			for (Method m : methods) {
				Annotation[] annotations = m.getDeclaredAnnotations();
				for (Annotation a : annotations) {
					Export annot = null;
					if (a instanceof Exports) {
						if (((Exports) a).value() != null) {
							for (Export item : ((Exports) a).value()) {
								if (item.place().equals(place)) {
									annot = item;
									break;
								}
							}
						}
					} else if (a instanceof Export) {
						annot = (Export) a;
					}

					if (annot != null) {
						if (annot.place().equals(place)) {
							for (T item : list) {
								listObjects.get(list.indexOf(item)).getFields().add(new Field(annot.propertyName(),
										annot.type(), m.invoke(item, new Object[0]), annot.seqXLS()));
							}
						}

						break;
					}
				}
			}
		}

		// second sub sheet

		for (@SuppressWarnings("unused")
		T2 item : list2) {
			listObjects2.add(new EntityObject());
		}

		for (Method m : methods2) {
			Annotation[] annotations = m.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				Export annot = null;
				if (a instanceof Exports) {
					if (((Exports) a).value() != null) {
						for (Export item : ((Exports) a).value()) {
							if (item.place().equals(place2)) {
								annot = item;
								break;
							}
						}
					}
				} else if (a instanceof Export) {
					annot = (Export) a;
				}

				if (annot != null) {
					if (annot.place().equals(place2)) {
						for (T2 item : list2) {
							listObjects2.get(list2.indexOf(item)).getFields().add(new Field(annot.propertyName(),
									annot.type(), m.invoke(item, new Object[0]), annot.seqXLS()));
						}
					}

					break;
				}
			}
		}

		// third sub sheet

		for (@SuppressWarnings("unused")
		T3 item : list3) {
			listObjects3.add(new EntityObject());
		}

		for (Method m : methods3) {
			Annotation[] annotations = m.getDeclaredAnnotations();
			for (Annotation a : annotations) {
				Export annot = null;
				if (a instanceof Exports) {
					if (((Exports) a).value() != null) {
						for (Export item : ((Exports) a).value()) {
							if (item.place().equals(place3)) {
								annot = item;
								break;
							}
						}
					}
				} else if (a instanceof Export) {
					annot = (Export) a;
				}

				if (annot != null) {
					if (annot.place().equals(place3)) {
						for (T3 item : list3) {
							listObjects3.get(list3.indexOf(item)).getFields().add(new Field(annot.propertyName(),
									annot.type(), m.invoke(item, new Object[0]), annot.seqXLS()));
						}
					}

					break;
				}
			}
		}

		if (!listObjects.isEmpty() || !listObjects2.isEmpty() || !listObjects3.isEmpty()) {
			if (!listObjects.isEmpty()) {
				List<Field> testFields = listObjects.get(0).getFields();
				Collections.sort(testFields, new FieldsComparer());

				createHeader(sheet, testFields, header);
			}
			createContent(sheet, listObjects, times, listObjects2, times2, listObjects3, times2);
		}

	}

	private <T> void createContentPart1(WritableSheet sheet, List<T> listMsg1, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		int row = 0;
		int column=0;
		createContentMessage(sheet, listMsg1, format, row, column, column+8);
	}
	
	private <T> void createContentPart2(WritableSheet sheet, List<T> listMsg1,List<EntityObject> list, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		int column = 0;
		int row = listMsg1.size()+2;
		
		for (EntityObject obj : list) {
			List<Field> fields = obj.getFields();
			Collections.sort(fields, new FieldsComparer());
			for (Field field : fields) {
				addLabel(sheet, column, row, field.getObj(), field.getType(), format);
				column++;
			}
			row++;
			column = 0;
		}
	}
	
	private <T, T2> void createContentPart3(WritableSheet sheet, List<T> listMsg1,List<T2> listMsg2, List<EntityObject> listObjects,
			WritableCellFormat format) throws RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		int column = 0;
		int row = listMsg1.size()+2+listObjects.size()+1;
		createContentMessage(sheet, listMsg2, format, row, column, column+8);
	}
	
	private <T> void createContentMessage(WritableSheet sheet, List<T> listMsg, WritableCellFormat format, int row, int column1 ,int column2) throws RowsExceededException, WriteException{
		for(T msg : listMsg){
			sheet.mergeCells(column1, row, column2, row);
			addLabel(sheet, column1, row, (String) msg, format);
			row++;
		}
	}

	private void createContent(WritableSheet sheet, List<EntityObject> list, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		int column = 0;
		int row = 1;
		for (EntityObject obj : list) {
			List<Field> fields = obj.getFields();
			Collections.sort(fields, new FieldsComparer());
			for (Field field : fields) {
				addLabel(sheet, column, row, field.getObj(), field.getType(), format);
				column++;
			}
			row++;
			column = 0;
		}
	}

	private void createContent(WritableSheet sheet, List<EntityObject> list, WritableCellFormat format,
			List<EntityObject> list2, WritableCellFormat format2, List<EntityObject> list3, WritableCellFormat format3)
			throws RowsExceededException, WriteException {
		int column = 0;
		int row = 1;
		for (EntityObject obj : list) {
			List<Field> fields = obj.getFields();
			Collections.sort(fields, new FieldsComparer());
			for (Field field : fields) {
				addLabel(sheet, column, row, field.getObj(), field.getType(), format);
				column++;
			}
			row++;
			column = 0;
		}
		row++;

		for (EntityObject obj : list2) {
			List<Field> fields = obj.getFields();
			Collections.sort(fields, new FieldsComparer());
			for (Field field : fields) {
				addLabel(sheet, column, row, field.getObj(), field.getType(), format2);
				column++;
			}
			row++;
			column = 0;
		}

		for (EntityObject obj : list3) {
			List<Field> fields = obj.getFields();
			Collections.sort(fields, new FieldsComparer());
			for (Field field : fields) {
				addLabel(sheet, column, row, field.getObj(), field.getType(), format3);
				column++;
			}
			row++;
			column = 0;
		}
	}

	private void createHeader(WritableSheet sheet, List<Field> list, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		int column = 0;
		for (Field field : list) {
			addLabel(sheet, column, 0, Utils.getString(field.getTitle()), format);
			column++;
		}
	}
	
	private <T> void createHeaderWithMessage(WritableSheet sheet, List<T> listMsg1, List<Field> list, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		int column = 0;
		for (Field field : list) {
			addLabel(sheet, column, listMsg1.size()+1, Utils.getString(field.getTitle()), format);
			column++;
		}
	}

	private void addLabel(WritableSheet sheet, int column, int row, Object obj, FieldTypes type,
			WritableCellFormat format) throws RowsExceededException, WriteException {
		if (type.equals(FieldTypes.STRING)) {
			addStringCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.DOUBLE)) {
			addDoubleCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.MONEY)) {
			addMoneyCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.NUMBER)) {
			try {
				Integer var = Integer.parseInt(String.valueOf(obj));
				addNumberCell(sheet, column, row, var, format);
			} catch (Exception e) {
				addStringCell(sheet, column, row, obj, format);
			}
		} else if (type.equals(FieldTypes.PERCENTAGE)) {
			addPercentageCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.DATE)) {
			addDateCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.CLASS)) {
			addClassCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.BOOLEAN)) {
			addBooleanCell(sheet, column, row, obj, format);
		} else if (type.equals(FieldTypes.DECIMAL)) {
			addDecimalCell(sheet, column, row, obj, format);
		}
	}

	private void addMoneyCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		jxl.write.Number n = null;
		if (text != null) {
			Double val = NumberHelper.Round((Double) text);
			n = new jxl.write.Number(column, row, val, this.getEuroSuffix());
		} else {
			n = new jxl.write.Number(column, row, 0d, this.getEuroSuffix());
		}
		setColumnSize(sheet, column, BaseConverter.convertToMoneyString(n.getValue()).length());
		sheet.addCell(n);
	}

	public static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}

	private WritableCellFormat euroSuffixFormat;

	private WritableCellFormat getEuroSuffix() {
		if (euroSuffixFormat == null) {
			NumberFormat euroSuffixCurrency = new NumberFormat("###,###,##0.00" + " \u20AC",
					NumberFormat.COMPLEX_FORMAT);
			euroSuffixFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8), euroSuffixCurrency);
			try {
				euroSuffixFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			} catch (WriteException e) {

			}
		}
		return euroSuffixFormat;
	}

	private WritableCellFormat decimalFormat;

	private WritableCellFormat getDecimalFormal() {
		if (decimalFormat == null) {
			NumberFormat euroSuffixCurrency = new NumberFormat("##0.0", NumberFormat.COMPLEX_FORMAT);
			decimalFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8), euroSuffixCurrency);
			try {
				decimalFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			} catch (WriteException e) {

			}
		}
		return decimalFormat;
	}

	private WritableCellFormat doubleFormat;

	private WritableCellFormat getDoubleFormat() {
		if (doubleFormat == null) {
			NumberFormat euroSuffixCurrency = new NumberFormat("##0.00", NumberFormat.COMPLEX_FORMAT);
			doubleFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8), euroSuffixCurrency);

			try {
				doubleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			} catch (WriteException e) {

			}
		}
		return doubleFormat;
	}

	private WritableCellFormat numberFormat;

	private WritableCellFormat getNumberFormat() {
		if (numberFormat == null) {
			NumberFormat euroSuffixCurrency = new NumberFormat("###", NumberFormat.COMPLEX_FORMAT);
			numberFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8), euroSuffixCurrency);

			try {
				numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			} catch (WriteException e) {

			}
		}
		return numberFormat;
	}

	private void addDecimalCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {

		jxl.write.Number n = null;
		if (text != null) {
			n = new jxl.write.Number(column, row, (Double) text, this.getDecimalFormal());
		} else {
			n = new jxl.write.Number(column, row, 0d, this.getDecimalFormal());
		}
		setColumnSize(sheet, column, BaseConverter.convertToDecimalString(n.getValue()).length());
		sheet.addCell(n);
	}

	private void addDoubleCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		jxl.write.Number n = null;
		if (text != null) {
			n = new jxl.write.Number(column, row, (Double) text, this.getDoubleFormat());
		} else {
			n = new jxl.write.Number(column, row, 0d, this.getDoubleFormat());
		}
		setColumnSize(sheet, column, BaseConverter.convertToDoubleString(n.getValue()).length());
		sheet.addCell(n);
	}

	private void addNumberCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		jxl.write.Number n = null;
		if (text != null) {
			n = new jxl.write.Number(column, row, (Integer) text, this.getNumberFormat());
		} else {
			n = new jxl.write.Number(column, row, 0d, this.getNumberFormat());
		}
		setColumnSize(sheet, column, BaseConverter.convertToNumberString(n.getValue()).length());
		sheet.addCell(n);
	}

	private void addStringCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (text != null) {
			addLabel(sheet, column, row, text.toString(), format);
		} else {
			addLabel(sheet, column, row, "", format);
		}
	}

	private void addPercentageCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (text != null) {
			addLabel(sheet, column, row, BaseConverter.convertToPercentageString(text), format);
		} else {
			addLabel(sheet, column, row, "", format);
		}
	}

	private void addDateCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (text != null) {
			addLabel(sheet, column, row, BaseConverter.convertToDateString(text), format);
		} else {
			addLabel(sheet, column, row, "", format);
		}
	}

	private void addClassCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (text != null) {
			addLabel(sheet, column, row, text.toString(), format);
		} else {
			addLabel(sheet, column, row, "", format);
		}
	}

	private void addBooleanCell(WritableSheet sheet, int column, int row, Object text, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (text != null) {
			addLabel(sheet, column, row, BaseConverter.convertToBooleanString(text), format);
		} else {
			addLabel(sheet, column, row, "", format);
		}
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s, WritableCellFormat format)
			throws RowsExceededException, WriteException {
		if (format.getFont().getBoldWeight() == 700) {
			setBoldColumnSize(sheet, column, s.length());
		} else {
			setColumnSize(sheet, column, s.length());
		}
		sheet.addCell(new Label(column, row, s, format));
	}

	/**
	 * @param sheet
	 * @param column
	 * @param i
	 */
	public void setColumnSize(WritableSheet sheet, int column, int i) {
		CellView cell = sheet.getColumnView(column);
		// cell.setAutosize(true);
		if (cell.getSize() < i * 220) {
			cell.setSize(i * 220);
		}
		sheet.setColumnView(column, cell);
	}

	public void setBoldColumnSize(WritableSheet sheet, int column, int i) {
		CellView cell = sheet.getColumnView(column);
		// cell.setAutosize(true);
		if (cell.getSize() < i * 256) {
			cell.setSize(i * 256);
		}
		sheet.setColumnView(column, cell);
	}
}
