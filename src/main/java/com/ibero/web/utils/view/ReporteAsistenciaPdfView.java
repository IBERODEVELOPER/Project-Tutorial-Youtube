package com.ibero.web.utils.view;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("userdpf")
public class ReporteAsistenciaPdfView extends AbstractPdfView{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 // Descarga automaticamente nombre de descarga
	    //response.setHeader("Content-Disposition", "attachment; filename=usuario.pdf");
	    
		// Agregar el logo al documento
        addLogo(document);
		// Crear una fuente con color
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(51,96,216));
		// Mostrar título del documento
		Paragraph title = new Paragraph("Usuario Registrado", titleFont);
		title.setAlignment(Element.ALIGN_CENTER); // Centrando el título
		title.setSpacingBefore(10f); // Espacio antes del título
		title.setSpacingAfter(10f);  // Espacio después del título
		document.add(title);
		
		//acceder a los datos enviados por model
		UserEntity user = (UserEntity) model.get("user");
		EmployeeEntity employee = (EmployeeEntity) model.get("employee");
		
		// Fuente para etiquetas y valores
	    Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
	    Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);

	    // Agregar contenido del usuario
	    Paragraph userInfo = new Paragraph();
	    userInfo.setSpacingBefore(10f);

	    userInfo.add(new Paragraph("Datos del Usuario:", labelFont));
	    userInfo.add(new Paragraph("Nombre de usuario: " + user.getUsername(), valueFont));
	    userInfo.add(new Paragraph("Correo: " + user.getEmail(), valueFont));
	    userInfo.add(new Paragraph("Rol: " + user.getRole(), valueFont));

	    userInfo.add(new Paragraph(" ", valueFont)); // Espacio
		
	    userInfo.add(new Paragraph("Datos del Empleado:", labelFont));
	    if (employee != null) {
	        userInfo.add(new Paragraph("Nombre completo : " + employee.getNomCompleto(), valueFont));
	        userInfo.add(new Paragraph("Tipo de Documento : " + employee.gettDocumento(), valueFont));
	        userInfo.add(new Paragraph("N° Documento : " + employee.getDocumento(), valueFont));
	        userInfo.add(new Paragraph("Nacimiento : " + employee.getFech_nacimiento(), valueFont));
	    } else {
	        userInfo.add(new Paragraph("No hay datos de empleado asociados.", valueFont));
	    }

	    document.add(userInfo);
		
		
	}
	
	 private void addLogo(Document document) throws IOException, BadElementException {
	        // Ruta del logo en el classpath
	        URL logoUrl = getClass().getClassLoader().getResource("static/images/iconibero.png");
	        if (logoUrl == null) {
	            throw new IOException("No se encontró el logo en la ruta especificada.");
	        }
	        Image logo = Image.getInstance(logoUrl);
	        // Tamaño del logo
	        logo.scaleToFit(45f, 450f); 
	        //alineación del logo
	        logo.setAlignment(Image.ALIGN_RIGHT);
	        document.add(logo);
	    }

}
