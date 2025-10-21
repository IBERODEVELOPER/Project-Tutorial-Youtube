package com.ibero.web.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.ibero.web.entities.UserEntity;

public interface UserIService {
	/*Metodo para listar todos los usuarios*/
	public List<UserEntity> findAllUsers();
	/*Metodo para listar con paginación de los usuarios*/
	public Page<UserEntity> findAll(Pageable pageable);
	public void saveuser(UserEntity user);
	/*Metodo para obtener datos del usuario*/
	public UserEntity findByUsername(String username);
	public UserEntity findUserById(Integer id);
	public void deleteUserById(Integer id);
	
	public UserEntity findByEmail(String email);
	
	public void updateOtpById(Integer id,String otp);
	public boolean verifyOtpById(Integer id,String otp);
	public void updatePass(Integer id,String userPassword);
}
