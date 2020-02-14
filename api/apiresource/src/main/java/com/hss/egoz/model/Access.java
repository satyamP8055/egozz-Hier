package com.hss.egoz.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Access {

	@Id
	@SequenceGenerator(name="access_seq", allocationSize = 1, initialValue = 1, sequenceName = "ACCESS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_seq")
	private Integer accessId;
	
	private String accessName;
	
	private String accessUrl;
	
	private Integer accessCode;

	public Access() {}
	
	public Access(String accessName, String accessUrl) {
		this.accessName=accessName;
		this.accessUrl=accessUrl;
		this.accessCode=23;
	}
	
	public Integer getAccessId() {
		return accessId;
	}

	public void setAccessId(Integer accessId) {
		this.accessId = accessId;
	}

	public String getAccessName() {
		return accessName;
	}

	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public Integer getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(Integer accessCode) {
		this.accessCode = accessCode;
	}
	
}
