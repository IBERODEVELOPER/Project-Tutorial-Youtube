package com.ibero.web.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ibero.web.entities.EmployeeEntity;
import com.ibero.web.entities.UserEntity;

@Repository
public interface EmployeeIRepository extends CrudRepository<EmployeeEntity, Integer>,PagingAndSortingRepository<EmployeeEntity, Integer>{
	
	public EmployeeEntity findByDocumento(String documento);
	public EmployeeEntity findByUserEntity(UserEntity userEntity);
}
