import org.apache.poi.xssf.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CXls {

	CDebug m_Debug;
	
	// Styles des cellules
	Map<String, CellStyle> m_styles;
	
	// Workbook courant
	Workbook m_wb;
	
	// Current Pointer
	Sheet	m_CurrentSheet;
	Row 	m_CurrentRow;
	int		m_CurrentRowIndex;
	
	
	// Array of Item sheets
	ArrayList<Sheet> m_sheets_items;
	Sheet m_sheet_forecast;
	
	CXls(CDebug Debug){
		m_Debug = Debug;
		
		// Initialisation d'un Workbook
		m_wb = new XSSFWorkbook();
		
		m_sheets_items = new ArrayList<Sheet>();
		m_sheet_forecast = null;
		m_CurrentRowIndex = -1;
		
		// Initialiser les styles
		m_styles = createStyles(m_wb);
	}
	
	boolean CreateSheetItem(String strSheetName){
		if (m_sheets_items == null){
			m_Debug.PrintWarning("CXls: CreateSheetItem ; Setting null pointer");
			return false;
		}
		Sheet sheet = m_wb.createSheet(strSheetName);
		
		
		// Make the page feet to one page width
        // PrintSetup ps = sheet.getPrintSetup();
		// sheet.setAutobreaks(true);
		// ps.setFitWidth((short) 1);

		// Initialize the sheet
		m_CurrentSheet = sheet;
		m_CurrentRow = null;
		m_CurrentRowIndex = -1;
		
		m_sheets_items.add(sheet);
		return true;
	}
	
	boolean CreateRow(){
		if (m_CurrentSheet == null){
			m_Debug.PrintWarning("CXls: CreateRow ; no current sheet");
			return false;
		}
		m_CurrentRowIndex++;
		m_CurrentRow = m_CurrentSheet.createRow(m_CurrentRowIndex);
		return true;
	}
	
	boolean WriteCellRate(int iLineIndex, double dRate){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dRate);
        titleCell.setCellStyle(m_styles.get("rate"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellHour(int iLineIndex, double dHour){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dHour);
        titleCell.setCellStyle(m_styles.get("heure"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}
	
	boolean WriteCellHeader(int iLineIndex, String strHeader){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(strHeader);
        titleCell.setCellStyle(m_styles.get("header"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}
	
	boolean WriteCellHeaderYellow(int iLineIndex, String strHeader){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(strHeader);
        titleCell.setCellStyle(m_styles.get("header_yellow"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}
	boolean WriteCellHeader(int iLineIndex, double dHeader){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dHeader);
        titleCell.setCellStyle(m_styles.get("header"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellEuro(int iLineIndex, double dEuros){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dEuros);
        titleCell.setCellStyle(m_styles.get("euro"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellHeaderEuro(int iLineIndex, double dEuros){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dEuros);
        titleCell.setCellStyle(m_styles.get("euro_header"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellHeaderHeure(int iLineIndex, double dHeure){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dHeure);
        titleCell.setCellStyle(m_styles.get("heure_header"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellValue(int iLineIndex, double dValue){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dValue);
        titleCell.setCellStyle(m_styles.get("cell"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	
	boolean WriteCellPercent(int iLineIndex, double dPercent){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(dPercent);
        titleCell.setCellStyle(m_styles.get("percent"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}

	boolean WriteCellString(int iLineIndex, String strValue){
		
        Cell titleCell = m_CurrentRow.createCell(iLineIndex);
        titleCell.setCellValue(strValue);
        titleCell.setCellStyle(m_styles.get("cell_left"));
        //m_CurrentSheet.setColumnWidth(0, 20*256);

		return true;
	}
	
	boolean SetSheetToOnePage(){
		// Make the page feet to one page width
	    //PrintSetup ps = m_CurrentSheet.getPrintSetup();
		
		m_CurrentSheet.getPrintSetup().setLandscape(true);
		m_CurrentSheet.setAutobreaks(true);
		//m_CurrentSheet.getPrintSetup().setFitWidth((short) 1);
		//m_CurrentSheet.getPrintSetup().setFitHeight((short) 1);
		m_CurrentSheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		m_CurrentSheet.getPrintSetup().setScale((short)60);
		
		
		return true;
	}

	
	boolean WriteToFile(String strFileName) throws IOException{
		if (m_wb == null){
			m_Debug.PrintWarning("CXls : WriteToFile failed !");
			return false;
		}
		
        FileOutputStream out = new FileOutputStream(strFileName);
        m_wb.write(out);
        out.close();
		return true;
	}
    /**
     * Create a library of cell styles
     */
    private Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        Font monthFont_black = wb.createFont();
        monthFont_black.setFontHeightInPoints((short)11);
        monthFont_black.setColor(IndexedColors.BLACK.getIndex());
        
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont_black);
        style.setWrapText(true);
        styles.put("header_yellow", style);

        // Style par défaut
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        // Style par défaut
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell_left", style);

        // EK Style %
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("0.0 %"));
        styles.put("percent", style);

        // EK Style Euros / h
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("# ##0.00\" €/h\""));
        styles.put("rate", style);

        // EK Style heures
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("##0.0\" h\""));
        styles.put("heure", style);

        // EK Style heures header

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("##0.0\" h\""));

        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);

        styles.put("heure_header", style);

        // EK Style Euros
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("# ### ##0\" €\""));
        styles.put("euro", style);

        // EK Style Euros Header
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(wb.createDataFormat().getFormat("# ### ##0\" €\""));
  
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);

        styles.put("euro_header", style);


        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }
	
}
