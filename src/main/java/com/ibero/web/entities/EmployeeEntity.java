package com.ibero.web.entities;

import java.io.Serializable;
import java.time.LocalDate;

import com.ibero.web.validations.ExistsByDocumento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employee")
public class EmployeeEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "{NotBlank.empleado.nombre}")
	@Column(length = 15)
	private String nombre;
	
	@NotBlank
	private String apePaterno;
	
	@NotBlank
	private String apeMaterno;
	
	private String nomCompleto;
	
	@NotBlank
	private String tDocumento;
	
	@NotBlank
	@Size(min = 8, max = 23, message = "Complete los datos correctamente")
	@Column(name = "documento", unique = true)
	@ExistsByDocumento
	private String documento;
	
	private String foto;
	
	
	@Column(name = "fech_nacimiento")
	private LocalDate fech_nacimiento;
	
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private UserEntity userEntity;
	
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	@PrePersist
	@PreUpdate
	public void prepersiste() {
		this.nomCompleto = this.nombre + " " + this.apePaterno + " " + this.apeMaterno;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}

	public String getNomCompleto() {
		return nomCompleto;
	}

	public void setNomCompleto(String nomCompleto) {
		this.nomCompleto = nomCompleto;
	}

	public String gettDocumento() {
		return tDocumento;
	}

	public void settDocumento(String tDocumento) {
		this.tDocumento = tDocumento;
	}
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public LocalDate getFech_nacimiento() {
		return fech_nacimiento;
	}

	public void setFech_nacimiento(LocalDate fech_nacimiento) {
		this.fech_nacimiento = fech_nacimiento;
	}



	private static final long serialVersionUID = 1L;
}
