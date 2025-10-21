package com.ibero.web.controllers;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ErrorHandlerController {

	 @ExceptionHandler({ConversionFailedException.class, MethodArgumentTypeMismatchException.class})
	    public String handleDateConversionException(Exception ex, Model model) {
	        model.addAttribute("formError", "Error de formato: Verifica que la fecha sea válida (yyyy-MM-dd)");
	        model.addAttribute("message", ex.getMessage());
	        return "error/errorform";
	    }

	    @ExceptionHandler(Exception.class)
	    public String handleGeneralException(Exception ex, Model model) {
	        model.addAttribute("formError", "Ocurrió un error en la aplicación.");
	        model.addAttribute("message", ex.getMessage());
	        return "error/errorform";
	    }
}
