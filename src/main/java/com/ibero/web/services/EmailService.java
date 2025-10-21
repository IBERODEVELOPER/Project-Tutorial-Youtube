package com.ibero.web.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ibero.web.utils.EmailValuesDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
    JavaMailSender mailSender;
 
    @Autowired
    TemplateEngine templateEngine;
    
	public void sendEmail(EmailValuesDTO dto) {
		
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        Context context = new Context();
	        Map<String, Object> model = new HashMap<>();
	        model.put("userName", dto.getUserName());
	        model.put("otp", dto.getOtp());

	        context.setVariables(model);
	        
	        String htmlText = templateEngine.process("email-template", context);
	        
	        //para enviar el correo
	        helper.setFrom(dto.getMailFrom());//DE
            helper.setTo(dto.getMailTo());//PARA
            helper.setSubject(dto.getSubject());//ASUNTO
            helper.setText(htmlText, true);
            
            mailSender.send(message);//Envio
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	} 
	
}
