package com.ibero.web.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;


public interface EmployeeIService {
	
	public List<EmployeeEntity> findAll();
	
	public Page<EmployeeEntity> findAll(Pageable pageable);
	
	public void regEmpleado(EmployeeEntity empl);
	public EmployeeEntity findEmpleadoById(Integer id);
	public void deleteById(Integer id);
	public EmployeeEntity findByDocumento(String documento);
	
	
	public EmployeeEntity findByUserEntity(UserEntity userEntity);
}
