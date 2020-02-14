package com.hss.egoz.authentication.jwt;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Token {

	@Id
	@SequenceGenerator(name="jwt_seq", allocationSize = 1, initialValue = 1, sequenceName = "jwt_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_seq")
	private Integer id;
	
	@Column(length = 355)
	private String tokenName;
	
	private Date expiryDate;
	
	private Date generatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(Date generatedAt) {
		this.generatedAt = generatedAt;
	}

}
