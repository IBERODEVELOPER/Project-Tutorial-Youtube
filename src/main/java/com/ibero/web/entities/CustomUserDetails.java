package com.ibero.web.entities;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomUserDetails extends User {

	private String userPhoto;
	private UserEntity userEntity;

	public CustomUserDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities,
			UserEntity userEntity,String userPhoto) {
		super(username, password, authorities);
		this.userPhoto = userPhoto;
		this.userEntity = userEntity;
	}

	@Override
	public boolean isEnabled() {
		return Boolean.TRUE.equals(userEntity.getEnabled());
	}

	// Validar Role
	public boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}
		Authentication aut = context.getAuthentication();
		if (aut == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = aut.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	private static final long serialVersionUID = 1L;

}
