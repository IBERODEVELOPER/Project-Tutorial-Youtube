package com.ibero.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;
import com.ibero.web.enums.RoleName;
import com.ibero.web.paginator.PageRender;
import com.ibero.web.services.EmployeeIService;
import com.ibero.web.services.UserIService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserIService userService;

	@Autowired
	private EmployeeIService emplservice;
	
	
	
	@GetMapping("/list")
	public String showUsers(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<UserEntity> users = userService.findAll(pageRequest);
		PageRender<UserEntity> pageRender = new PageRender<UserEntity>("/users", users);
		long totalUser = users.getTotalElements();
		model.addAttribute("titleForm", "Usuarios Activos");
		model.addAttribute("page", pageRender);
		model.addAttribute("totalRecords", totalUser);
		model.addAttribute("users", users);
		return "user-list";
	}

	@GetMapping(value = "/form")
	public String showForm(Model model) {
		// Obtener la lista completa de empleados
		List<EmployeeEntity> allPeople = emplservice.findAll();
		// Empleados que ya tienen un usuario
		Set<Integer> employeeIdsWithUsers = new HashSet<>();
		// Rellenar el conjunto con los IDs de empleados asociados a un usuario
		for (EmployeeEntity empl : allPeople) {
			if (empl.getUserEntity() != null) {
				employeeIdsWithUsers.add(empl.getId());
			}
		}
		// Filtrar la lista de empleados para remover aquellos que ya tienen un usuario
		List<EmployeeEntity> availablePeople = new ArrayList<>();
		for (EmployeeEntity person : allPeople) {
			if (!employeeIdsWithUsers.contains(person.getId())) {
				availablePeople.add(person);
			}
		}
		model.addAttribute("titleForm", "Registro de Usuario");
		model.addAttribute("empleados", availablePeople);
		model.addAttribute("listaRoles", RoleName.values());
		model.addAttribute("user", new UserEntity());
		return "form_user";
	}

	@PostMapping(value = "/create")
	public String processForm(@Valid UserEntity user, BindingResult result, Model model,RedirectAttributes message) {
		if (result.hasErrors()) {
			model.addAttribute("titleForm", "Registro de Usuario");
			model.addAttribute("user", user);
			return "form_user";
		}
		// guardar el nuevo usuario
		userService.saveuser(user);
		message.addFlashAttribute("success", "Datos registrados Correctamente");
		return "redirect:/users/list";
	}
	
	@GetMapping(value = "/deleteById/{id}")
	public String deleteEmployee(@PathVariable Integer id) {
		if (id > 0) {
			userService.deleteUserById(id);
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/profileuser")
	public String showProfile(Model model) {
		//Obtener el usuario autenticado
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName(); // Nombre del usuario autenticado
		// Recuperar los datos del usuario y del empleado relacionado desde el servicio
		UserEntity user = userService.findByUsername(username);
		// Pasar los datos al modelo
		model.addAttribute("user", user);
		model.addAttribute("employee", user.getEmployee());
		return "profileuser";
	}
	
}
