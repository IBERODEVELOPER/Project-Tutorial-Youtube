package com.ibero.web.interceptos;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InterceptorHorario implements HandlerInterceptor{

	@Value("${config.horario.apertura}")
	private Integer apertura;
	
	@Value("${config.horario.cierre}")
	private Integer cierre;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception {
		
		// Verificar si hay usuario autenticado
	    var auth = request.getUserPrincipal();
	    if (auth == null) {
	        // no hay login, dejar pasar sin restricción
	        return true;
	    }	

	Calendar calendar = Calendar.getInstance();
	int hora = calendar.get(calendar.HOUR_OF_DAY);

	if(hora >= apertura && hora < cierre) {
	StringBuilder mensaje = new StringBuilder("Bienvenidos al horario de atención");
	
	request.setAttribute("success", mensaje.toString());
	
	return true;
	}
	response.sendRedirect(request.getContextPath().concat("/cerrado"));
	return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	ModelAndView modelAndView) throws Exception {
	String mensaje = (String) request.getAttribute("mensaje");
	if(modelAndView != null && handler instanceof HandlerMethod) {
	modelAndView.addObject("horario", mensaje);
	 } 
	}


}
