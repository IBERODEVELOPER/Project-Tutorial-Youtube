package com.ibero.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InterceptorController {
	
	@Value("${config.horario.apertura}")
	private Integer apertura;
	
	@Value("${config.horario.cierre}")
	private Integer cierre;
		
	@GetMapping("/cerrado")
	public String cerrado(Model model) {
	StringBuilder mensaje = new StringBuilder("Cerrado, por favor visitenos desde las");
	mensaje.append(apertura);
	mensaje.append(", atendemos hasta las ");
	mensaje.append(cierre);
	mensaje.append(" hrs.");
	mensaje.append("Gracias por su visita.");
	
	model.addAttribute("titulo", "Fuera del horario de atención. -_-");
	model.addAttribute("mensaje", mensaje.toString());
	return "cerrado";	
	} 

}
