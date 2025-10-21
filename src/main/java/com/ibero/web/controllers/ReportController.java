package com.ibero.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ibero.web.entities.UserEntity;
import com.ibero.web.services.UserIService;
import com.ibero.web.utils.view.ReporteXlsxView;

@Controller
@RequestMapping("reports")
public class ReportController {

	@Autowired
	private UserIService userService;
	
	@GetMapping(value = "/userdpf", produces = "application/pdf")
	public ModelAndView exportPDF(Model model) {
	    ModelAndView modelAndView = new ModelAndView("userdpf");
	    addAuthenticatedUserToModel(model);
	    return modelAndView;
	}
	
	@GetMapping(value = "/userxls", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public ModelAndView exportExcel(Model model) {
	    ModelAndView modelAndView = new ModelAndView(new ReporteXlsxView());
	    addAuthenticatedUserToModel(model);
	    return modelAndView;
	}
	
	
	private void addAuthenticatedUserToModel(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    UserEntity user = userService.findByUsername(username);
	    model.addAttribute("user", user);
	    model.addAttribute("employee", user.getEmployee());
	}

}
