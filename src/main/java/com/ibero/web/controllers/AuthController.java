package com.ibero.web.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibero.web.entities.UserEntity;
import com.ibero.web.services.EmailService;
import com.ibero.web.services.UserIService;
import com.ibero.web.utils.EmailValuesDTO;

@Controller
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserIService userService;
	
	@Autowired
	private EmailService emailservice;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${spring.mail.username}")
	private String mailFrom;//configurar el emisor 
	
	@Value("${mail.subject.otp}")
	private String subject;//asunto del email
	
	@GetMapping({"/","/login"})
	public String showLogin(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, 
			Model model , Authentication aut) {
		if (aut != null && aut.isAuthenticated()) {
	        return "redirect:/listar";
	    }
		if (error != null) {
	        model.addAttribute("error", "Usuario o contraseña inválidos.");
	    }
		if (logout != null) {
	        model.addAttribute("mensajeout", "Has cerrado sesión correctamente.");
	    }
		return "login";
	}
	
	@PostMapping("/send-email")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestParam String email) {
		
		Map<String, Object> response = new HashMap<>();
		
		UserEntity user = userService.findByEmail(email);
		
		if (user == null) {
			response.put("success", false);
	        response.put("message", "No existe ninguna coincidencia con el correo proporcionado");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	
		String otp = String.valueOf(new Random().nextInt(900000) + 100000); // genera OTP de 6 dígitos
		user.setOtp(otp); //settea el otp al user
		userService.updateOtpById(user.getId(),user.getOtp()); //guarda el OTP en BD
		
		//Para el email
		EmailValuesDTO dto = new EmailValuesDTO();
		dto.setMailFrom(mailFrom);//De
		dto.setMailTo(user.getEmail());//Para
		dto.setSubject(subject);//Asunto
		
		//Se necesita enviar el usuario propietario del correo
		dto.setUserName(user.getUsername());
		dto.setOtp(otp);
		
		emailservice.sendEmail(dto);// Envía email
		response.put("success", true);
	    response.put("message", "Se envió el código OTP.");
	    response.put("userId", user.getId());
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping({"/validate-otp"})
	public String vericarOtp(@RequestParam("id") Integer idUsuario,
			@RequestParam(value = "otp", required = false) String otp,Model model) {
		
		boolean response = userService.verifyOtpById(idUsuario, otp);
		
		if(response == false) {
			model.addAttribute("error", "No existe ningun usuario con el OTP ó ID");
			return "/login";
		}
		
		model.addAttribute("sucess", "Código Verificado");
		model.addAttribute("userID", idUsuario);
		return "/formChangePass";
	}
	
	@PostMapping("/changekey")
	public String processFormchangePassword(@RequestParam("id") Integer id,
			@RequestParam("userPassword") String userPassword, RedirectAttributes flash) {
	
		String encodePass = passwordEncoder.encode(userPassword);
		userService.updatePass(id, encodePass);
		flash.addFlashAttribute("sucess", "Contraseña actualizada exitosamente.");
		
		return "redirect:/login";
	}

}
