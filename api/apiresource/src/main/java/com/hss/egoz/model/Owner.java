package com.hss.egoz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Owner {

	@Id
	@SequenceGenerator(name="owner_seq", allocationSize = 1, initialValue = 1, sequenceName = "OWNER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "owner_seq")
	private Integer ownerId;

	@Column(unique = true)
	private String userName;

	@JsonIgnore
	private String password;

	@Column(unique = true)
	private String mail;
	
	private String fullName;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String phone;
	
	@JsonIgnore
	private final String ownerRole;
	
	public Owner() {
		ownerRole="OWNER";
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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

	public String getOwnerRole() {
		return ownerRole;
	}
	
}
