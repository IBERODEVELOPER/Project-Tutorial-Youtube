package com.ibero.web.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.paginator.PageRender;
import com.ibero.web.services.EmployeeIService;
import com.ibero.web.validations.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

@Controller
@SessionAttributes(names = "empleado")
public class EmployeeController {

	@Autowired
	private EmployeeIService emplser;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@GetMapping("/listar")
	public String showList(@RequestParam(name = "page",defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest =  PageRequest.of(page, 10);
		Page<EmployeeEntity> empleados = emplser.findAll(pageRequest);
		PageRender<EmployeeEntity> pageRender = new PageRender< EmployeeEntity>("/listar", empleados);
		long totalRecords = empleados.getTotalElements();
		
		model.addAttribute("titleForm", "Empleado Activos");
		model.addAttribute("page", pageRender);
		model.addAttribute("totalRecords", totalRecords);
		model.addAttribute("empleados", empleados);
		return "all";
	}

	@GetMapping(value = "/form")
	public String showForm(Model model) {
		model.addAttribute("titleForm", "Formulario de Registro");
		model.addAttribute("empleado", new EmployeeEntity());
		return "form";
	}

	@ModelAttribute
	public void setActualId(@RequestParam(value = "id", required = false) Integer id) {
		if (id != null) {
			RequestContext.setIdActual(id);
		}
	}

	@PostMapping(value = "/form")
	public String processForm(@Valid EmployeeEntity empleado, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes message, SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titleForm", "Registro de Empleado");
			return "form";
		}
		if (!foto.isEmpty()) {
			// Obtener el nombre del archivo original y la extensión
			String originalFilename = foto.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			
			// Crear el nuevo nombre de archivo utilizando el nombre del empleado
			String nuevoNombreArchivo = empleado.getNombre().replace(" ", "_")+ extension;
			
			String uriC = "C://Web_Fotos";
			Path direc = Paths.get(uriC).resolve(nuevoNombreArchivo);
			
			String rootPath = direc.toFile().getAbsolutePath();
			
			if (empleado.getFoto() != null && !empleado.getFoto().isEmpty()) {
				Path pathFotoAnterior = Paths.get(uriC).resolve(empleado.getFoto()).toAbsolutePath();
				File archivoFotoAnterior = pathFotoAnterior.toFile();
				if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			try {
				byte[] bytes = foto.getBytes();
				Files.write(direc, bytes);
				empleado.setFoto(nuevoNombreArchivo);
				message.addFlashAttribute("info", "Has subido correctamente '" + nuevoNombreArchivo + "'");
			} catch (Exception e) {
			}
		}
		else {
			message.addFlashAttribute("error", "No hay ninguna foto seleccionado");
		}
		
		if (empleado.getId() == null) {
			message.addFlashAttribute("success", "Datos registrados Correctamente");
		} else {
			message.addFlashAttribute("success", "Datos actualizados correctamente");
		}
		emplser.regEmpleado(empleado);
		status.setComplete();
		RequestContext.clear();
		return "redirect:/listar";
	}

	@GetMapping(value = "/form/{id}")
	public String editForm(@PathVariable Integer id, Model model) {
		EmployeeEntity empleado = null;
		if (id > 0) {
			empleado = emplser.findEmpleadoById(id);
		}
		model.addAttribute("titleForm", "Editar Registro");
		model.addAttribute("empleado", empleado);
		return "form";
	}

	@GetMapping(value = "/deleteById/{id}")
	public String deleteEmployee(@PathVariable Integer id) {
		if (id > 0) {
			emplser.deleteById(id);
		}
		return "redirect:/listar";
	}
}
