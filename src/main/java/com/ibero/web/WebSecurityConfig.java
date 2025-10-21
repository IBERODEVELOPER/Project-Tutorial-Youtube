package com.ibero.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	private final UserDetailsService userDetailsService;

    WebSecurityConfig(UserDetailsService userDetailsService) {
    	this.userDetailsService = userDetailsService;
    }
    
    @Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

	/*Dar acceso a las rutas o restringir dependiendo del rol ACL*/
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz -> authz
				.requestMatchers("/", "/send-email","/validate-otp","/changekey","/css/**", "/js/**", "/images/**").permitAll()
				.requestMatchers("/uploads/**").hasAnyRole("USER","ADMIN")
				.requestMatchers( "/listar").hasAnyRole("USER","ADMIN")
				.requestMatchers("/form/**","/users/form/**", "/deleteById/**").hasRole("ADMIN")
				.anyRequest().authenticated())
		.formLogin(login -> login
		        .loginPage("/login")
				.defaultSuccessUrl("/listar", true)
				.permitAll())
		.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll());
		return http.build();
	}

}
