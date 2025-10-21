package com.ibero.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;
import com.ibero.web.repositories.EmployeeIRepository;

@Service
public class EmployeeIServiceImpl implements EmployeeIService{

	@Autowired 
	private EmployeeIRepository emplerepo;
	
	@Override
	@Transactional(readOnly = true)
	public Page<EmployeeEntity> findAll(Pageable pageable) {
		return emplerepo.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeEntity> findAll() {
		return (List<EmployeeEntity>) emplerepo.findAll();
	}

	@Override
	@Transactional
	public void regEmpleado(EmployeeEntity empl) {
		emplerepo.save(empl);
	}

	@Override
	@Transactional(readOnly=true)
	public EmployeeEntity findEmpleadoById(Integer id) {
		return emplerepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		emplerepo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeEntity findByDocumento(String documento) {
		return emplerepo.findByDocumento(documento);
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeEntity findByUserEntity(UserEntity userEntity) {
		return emplerepo.findByUserEntity(userEntity);
	}
}
