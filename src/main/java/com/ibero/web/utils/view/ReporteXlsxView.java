package com.ibero.web.utils.view;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;

import java.io.IOException;
import java.net.URL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ReporteXlsxView extends AbstractXlsxView {

   	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
   		response.setHeader("Content-Disposition", "attachment; filename=\"reporte_usuario.xlsx\"");

		// Crear la hoja
		Sheet sheet = workbook.createSheet("Datos del Usuario");
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);

		// Agregar el logo
		addLogo(workbook, sheet);

		// Crear el título
		createTitleRow(workbook, sheet);

		// Agregar los detalles del reporte
		createReportDetails(workbook, sheet, model);

		// Crear el encabezado de la tabla
		createTableHeader(workbook, sheet);

		// Agregar los datos a la tabla
		addTableRows(workbook, sheet);
	}

	private void addLogo(Workbook workbook, Sheet sheet) throws Exception {
		Drawing<?> drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
		anchor.setCol1(6); // Columna F
		anchor.setRow1(0); // Fila 1
		anchor.setCol2(7); // Columna G
		anchor.setRow2(2); // Fila 2

		// Ruta del logo en el classpath
		URL logoUrl = getClass().getClassLoader().getResource("static/images/iconibero.png");
		if (logoUrl == null) {
			throw new IOException("No se encontró el logo en la ruta especificada.");
		}

		byte[] logoBytes = logoUrl.openStream().readAllBytes();
		int pictureIndex = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
		drawing.createPicture(anchor, pictureIndex);
	}

	private void createTitleRow(Workbook workbook, Sheet sheet) {
		Row titleRow = sheet.createRow(3);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Datos del Usuario");
		titleCell.setCellStyle(createTitleCellStyle(workbook));
		// Unir celdas para que el título ocupe varias columnas
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));
	}

	private CellStyle createTitleCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Helvetica");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

	private void createReportDetails(Workbook workbook, Sheet sheet,Map<String, Object> model) {
		
		//acceder a los datos enviados por model
		UserEntity user = (UserEntity) model.get("user");
		EmployeeEntity employee = (EmployeeEntity) model.get("employee");
		
		// Fila 5: Reporte de Asistencia del Empleado
		Row row1 = sheet.createRow(5);
		row1.createCell(0).setCellValue(
				"Usuario Registrado: " + employee.getNomCompleto());

		// Fila 6: Desde - Hasta
		Row row2 = sheet.createRow(6);
		row2.createCell(0).setCellValue("Tipo : " + employee.gettDocumento());
		row2.createCell(1).setCellValue("N° : " + employee.getDocumento());

		// Fila 7: Días de asistencia
		Row row4 = sheet.createRow(7);
		row4.createCell(0).setCellValue("Usuario : " + user.getUsername());

		}

	private void createTableHeader(Workbook workbook, Sheet sheet) {
		Row headerRow = sheet.createRow(12);
		String[] columns = { "Nombre Completo", "Día", "Fecha", "H. Ingreso", "Tardanza"};

		CellStyle headerStyle = workbook.createCellStyle();
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Helvetica");
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		headerStyle.setFont(font);
		headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerStyle);
		}
		// Crear la celda "Justificación" y fusionar dos columnas (5 y 6)
	    Cell justifyHeaderCell = headerRow.createCell(5);
	    justifyHeaderCell.setCellValue("Justificación");
	    justifyHeaderCell.setCellStyle(headerStyle);
	    sheet.addMergedRegion(new CellRangeAddress(12, 12, 5, 6));
	}

	private void addTableRows(Workbook workbook, Sheet sheet) {
		int rowCount = 13;
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		// Autoajustar el ancho de las columnas después de llenar los datos
	    for (int i = 0; i <= 6; i++) {
	        sheet.autoSizeColumn(i);
	    }
	}
}
