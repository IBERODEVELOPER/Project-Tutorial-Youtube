package com.ibero.web.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.services.EmployeeIService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByDocumentoValidation implements ConstraintValidator<ExistsByDocumento, String> {
	
	@Autowired
    private EmployeeIService service;

	@Override
	public boolean isValid(String documento, ConstraintValidatorContext context) {
		
		Integer idContext = RequestContext.getIdActual();
		
		if(service == null) {
			return true;
		}
		
		EmployeeEntity empleado = service.findByDocumento(documento);
		
		if(empleado != null) {
			if(idContext != null && empleado.getId().equals(idContext)) {
				return true;
			}
			return false;
		}
		return true;
		 
	}

}
