package com.hss.egoz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Admin{

	@Id
	@SequenceGenerator(name="admin_seq", allocationSize = 1, initialValue = 1, sequenceName = "ADMIN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq")
	private Integer adminId;
	
	@Column(unique = true)
	private String userName;

	@JsonIgnore
	private String password;

	@Column(unique = true)
	private String mail;
	
	private String fullName;
	
	@JsonIgnore
	private final String userRole;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="roleId")
	private Role role;
	
	public Admin() {
		userRole="ADMIN";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserRole() {
		return userRole;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
