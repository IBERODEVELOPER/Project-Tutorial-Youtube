package com.ibero.web.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibero.web.entities.CustomUserDetails;
import com.ibero.web.entities.UserEntity;
import com.ibero.web.repositories.UserIRepository;

@Service
public class UserServiceImpl implements UserIService, UserDetailsService {
	
	@Autowired
	private UserIRepository userrepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> findAllUsers() {
		logger.info("Listar usuarios");
		return (List<UserEntity>) userrepo.findAll();
	}
	
	@Transactional(readOnly = true)
	@Override
	public UserEntity findByUsername(String username) {
		return userrepo.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserEntity> findAll(Pageable pageable) {
		return userrepo.findAll(pageable);
	}

	@Override
	@Transactional
	public void saveuser(UserEntity user) {
		// Encriptar la contraseña del usuario
		if (user != null && user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
		userrepo.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserEntity findUserById(Integer id) {
		return userrepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteUserById(Integer id) {
		userrepo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userrepo.findByUsername(username);

		List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().name()));	
		
		if (authorities.isEmpty()) {
			throw new UsernameNotFoundException("Error en el login: usuario '" + username + "' no tiene roles asignados");
		}
		
		String userPhoto = null;
		if (user.getEmployee() != null) {
	        // Si el usuario tiene un empleado asociado, obtener su foto
			userPhoto = user.getEmployee().getFoto();
	    }
		return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities, user,userPhoto);
	}

	@Override
	@Transactional(readOnly = true)
	public UserEntity findByEmail(String email) {
		return userrepo.findByEmail(email);
	}

	@Override
	@Transactional
	public void updateOtpById(Integer id, String otp) {
		userrepo.updateOtpById(id, otp);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean verifyOtpById(Integer id, String otp) {
		return userrepo.existsByIdAndOtp(id, otp);
	}

	@Override
	@Transactional
	public void updatePass(Integer id, String userPassword) {
		userrepo.updatePass(id, userPassword);
	}

}
