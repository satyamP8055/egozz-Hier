package com.hss.egoz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.hss.egoz.service.util.DataService;

@Entity
public class User {

	@Id
	@SequenceGenerator(name="user_seq", allocationSize = 1, initialValue = 1, sequenceName = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Integer userId;

	@Column(unique = true)
	private String userName;

	@JsonIgnore
	private String password;

	@Column(unique = true)
	private String mail;
	
	private String phone;
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String fullName;

	@JsonIgnore
	private final String userRole;
	
	public User() {
		userRole="CUSTOMER";
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
	
	public String toString() {
		String ret="";
//		ret+=this.fullName+DataService.STRING_BREAKER;
//		ret+=this.mail+DataService.STRING_BREAKER;
//		ret+=this.password+DataService.STRING_BREAKER;
//		ret+=this.userName+DataService.STRING_BREAKER;
//		ret+=this.phone;
		return ret;
	}
	
}
